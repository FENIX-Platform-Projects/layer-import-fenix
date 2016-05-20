package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;

public class Service {

    @XmlElement public String Name;
    @XmlElement public String Title;
    @XmlElement public String Abstract;
    @XmlElement public KeywordList KeywordList;
    @XmlElement public OnlineResource OnlineResource;
    @XmlElement public ContactInformation ContactInformation;
    @XmlElement public String Fees;
    @XmlElement public String AccessConstraints;

}
