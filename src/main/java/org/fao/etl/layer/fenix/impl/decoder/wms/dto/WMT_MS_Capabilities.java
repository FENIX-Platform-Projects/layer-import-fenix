package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement ( name = "WMT_MS_Capabilities")
public class WMT_MS_Capabilities {
    @XmlElement public Service Service;
    @XmlElement public Capability Capability;

    @XmlAttribute public String version;
    @XmlAttribute public String updateSequence;

}
