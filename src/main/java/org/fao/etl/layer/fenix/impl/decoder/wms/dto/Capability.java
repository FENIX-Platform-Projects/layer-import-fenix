package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;

public class Capability {
    @XmlElement public Request Request;
    @XmlElement public RequestOptions Exception;
    @XmlElement public UserDefinedSymbolization UserDefinedSymbolization;

    @XmlElement public Layer Layer;
}
