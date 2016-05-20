package org.fao.etl.layer.fenix.impl.decoder;

import org.fao.etl.layer.fenix.Decoder;
import org.fao.etl.layer.fenix.impl.decoder.wms.WMSMetadataCreator;
import org.fao.etl.layer.fenix.impl.decoder.wms.dto.Layer;
import org.fao.etl.layer.fenix.impl.decoder.wms.dto.WMT_MS_Capabilities;
import org.fao.fenix.commons.msd.dto.full.DSDGeographic;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;

@ApplicationScoped
public class WMSDecoder implements Decoder {
    @Inject private WMSMetadataCreator metadataCreator;

    @Override
    public Collection<MeIdentification> decode(InputStream input) throws Exception {
        WMT_MS_Capabilities root = parse(input);

        Collection<MeIdentification> metadataList = new LinkedList<>();
        MeIdentification<DSDGeographic> layersGroupMetadata = metadataCreator.createMetadata(root);
        metadataList.add(layersGroupMetadata);

        if (root.Capability.Layer!=null && root.Capability.Layer.Layer!=null)
            for (Layer layerXml : root.Capability.Layer.Layer)
                metadataList.add(metadataCreator.createMetadata(layerXml, root.Capability.Layer, layersGroupMetadata));

        return metadataList;
    }


    private WMT_MS_Capabilities parse(InputStream input) throws Exception {
        //Create filter
        SAXSource source = new SAXSource(
                new XMLFilterImpl(XMLReaderFactory.createXMLReader()) {
                    @Override
                    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
                        //Remove namespace from attributes
                        AttributesImpl attributes = new AttributesImpl();
                        if (arg3!=null)
                            for (int i=0; i<arg3.getLength(); i++)
                                attributes.addAttribute("", arg3.getLocalName(i), arg3.getLocalName(i), "", arg3.getValue(i));
                        //Fire start element event
                        super.startElement(arg0, arg1, arg2, attributes);
                    }
                },
                new InputSource(input)
        );
        //Parse filtered XML with JAXB
        JAXBContext jaxbContext = JAXBContext.newInstance(WMT_MS_Capabilities.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        WMT_MS_Capabilities root = (WMT_MS_Capabilities) jaxbUnmarshaller.unmarshal(source);
        //Close input stream
        input.close();
        return root;
    }
}
