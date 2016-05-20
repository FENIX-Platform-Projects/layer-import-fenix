package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;

public class ContactInformation {
    @XmlElement public ContactPersonPrimary ContactPersonPrimary;
    @XmlElement public String ContactPosition;
    @XmlElement public ContactAddress ContactAddress;
    @XmlElement public String ContactVoiceTelephone;
    @XmlElement public String ContactFacsimileTelephone;
    @XmlElement public String ContactElectronicMailAddress;

}
