package org.fao.etl.layer.fenix.services;


import org.fao.etl.layer.fenix.Decoder;
import org.fao.etl.layer.fenix.InitProperties;
import org.fao.etl.layer.fenix.Loader;
import org.fao.etl.layer.fenix.dto.MetadataGroups;
import org.fao.etl.layer.fenix.impl.decoder.WMSDecoder;
import org.fao.etl.layer.fenix.impl.loader.URLLoader;
import org.fao.etl.layer.fenix.utils.D3SClient;
import org.fao.fenix.commons.msd.dto.full.DSDGeographic;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.Collection;

public class Import {
    @Inject InitProperties initProperties;
    @Inject D3SClient d3sClient;
    @Inject Instance<Loader> loaderFactory;
    @Inject Instance<Decoder> decoderFactory;

    public Collection<MeIdentification> importWmsByUrl(String url, String d3sUrl) throws Exception {
        if (url==null)
            System.out.println("Specify wms file URL.");
        return genericImport(
                loaderFactory.select(URLLoader.class).iterator().next(),
                decoderFactory.select(WMSDecoder.class).iterator().next(),
                url,
                d3sUrl
        );
    }




    private <T> Collection<MeIdentification> genericImport (Loader<T> loader, Decoder decoder, T loaderParameters, String d3sUrl) throws Exception {
        if ((d3sUrl = d3sUrl==null ? initProperties.getProperty("d3s.url") : null) == null)
            throw new Exception("D3S URL not found and not specified.");
        InputStream inputStream = loader.load(loaderParameters);
        Collection<MeIdentification> metadataList = decoder.decode(inputStream);
        sendMetadata(metadataList, d3sUrl);
        return metadataList;
    }




    private void sendMetadata (Collection<MeIdentification> source, String baseUrl) throws Exception {
        //Init
        baseUrl = baseUrl + (baseUrl.charAt(baseUrl.length() - 1) != '/' ? "/" : "");

        //Load existing metadata and create metadata groups
        Collection<MeIdentification> destination = d3sClient.retrieveMetadata(baseUrl, "externalLayersGroups");
        destination.addAll(d3sClient.retrieveMetadata(baseUrl, "externalLayers"));
        MetadataGroups updateGroups = groupMetadata(source, destination);

        //Update metadata
        if (updateGroups.update.size()>0)
            d3sClient.updateMetadata(baseUrl, updateGroups.update);
        if (updateGroups.insert.size()>0)
            d3sClient.insertMetadata(baseUrl, updateGroups.insert);
        if (updateGroups.delete.size()>0)
            d3sClient.deleteMetadata(baseUrl, updateGroups.delete);
    }

    private MetadataGroups groupMetadata(Collection<MeIdentification> source, Collection<MeIdentification> destination) {
        MetadataGroups groups = new MetadataGroups();

        groups.insert.addAll(source);
        groups.insert.removeAll(destination);

        groups.update.addAll(source);
        groups.update.retainAll(destination);

        groups.delete.addAll(destination);
        groups.delete.removeAll(source);

        return groups;
    }

}
