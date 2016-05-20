package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

public class Style {
    @XmlElement public String Name;
    @XmlElement public String Title;
    @XmlElement public String Abstract;
    @XmlElement public LegendURL LegendURL;

}
