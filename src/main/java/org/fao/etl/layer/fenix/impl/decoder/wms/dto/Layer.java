package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

public class Layer {
    @XmlElement public String Name;
    @XmlElement public String Title;
    @XmlElement public String Abstract;
    @XmlElement public Collection<String> SRS;

    @XmlElement public KeywordList KeywordList;
    @XmlElement public LatLonBoundingBox LatLonBoundingBox;
    @XmlElement public BoundingBox BoundingBox;
    @XmlElement public Style Style;
    @XmlElement public Collection<Layer> Layer;

}
