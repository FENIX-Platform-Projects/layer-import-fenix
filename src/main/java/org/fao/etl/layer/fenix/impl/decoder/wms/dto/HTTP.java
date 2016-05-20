package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;

public class HTTP {
    @XmlElement public HttpRequestMethod Get;
    @XmlElement public HttpRequestMethod Post;
    @XmlElement public HttpRequestMethod Put;
    @XmlElement public HttpRequestMethod Delete;
    @XmlElement public HttpRequestMethod Patch;
    @XmlElement public HttpRequestMethod Options;
    @XmlElement public HttpRequestMethod Head;
}
