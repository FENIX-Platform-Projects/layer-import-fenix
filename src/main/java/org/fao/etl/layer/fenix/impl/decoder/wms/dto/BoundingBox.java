package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class BoundingBox {
    @XmlAttribute public String SRS;
    @XmlAttribute public Double minx;
    @XmlAttribute public Double miny;
    @XmlAttribute public Double maxx;
    @XmlAttribute public Double maxy;

}
