package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class LegendURL {
    @XmlElement public String Format;
    @XmlElement public OnlineResource OnlineResource;

    @XmlAttribute public Integer width;
    @XmlAttribute public Integer height;

}
