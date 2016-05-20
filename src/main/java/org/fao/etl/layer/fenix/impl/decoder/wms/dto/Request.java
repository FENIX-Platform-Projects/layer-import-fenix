package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;

public class Request {
    @XmlElement public RequestOptions GetCapabilities;
    @XmlElement public RequestOptions GetMap;
    @XmlElement public RequestOptions GetFeatureInfo;
    @XmlElement public RequestOptions GetLegendGraphic;
    @XmlElement public RequestOptions GetStyles;
    @XmlElement public RequestOptions DescribeLayer;

}
