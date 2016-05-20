package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class OnlineResource {
    @XmlAttribute public String type;
    @XmlAttribute public String href;
}
