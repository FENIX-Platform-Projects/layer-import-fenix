package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class UserDefinedSymbolization {
    @XmlAttribute public String SupportSLD;
    @XmlAttribute public String UserLayer;
    @XmlAttribute public String UserStyle;
    @XmlAttribute public String RemoteWFS;


}
