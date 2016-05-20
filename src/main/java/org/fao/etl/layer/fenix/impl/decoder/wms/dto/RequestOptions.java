package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

public class RequestOptions {
    @XmlElement public Collection<String> Format;
    @XmlElement public DCPType DCPType;

}
