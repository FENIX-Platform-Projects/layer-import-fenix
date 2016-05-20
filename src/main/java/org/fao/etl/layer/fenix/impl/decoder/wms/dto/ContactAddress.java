package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;

public class ContactAddress {
    @XmlElement public String AddressType;
    @XmlElement public String Address;
    @XmlElement public String City;
    @XmlElement public String StateOrProvince;
    @XmlElement public String PostCode;
    @XmlElement public String Country;

}
