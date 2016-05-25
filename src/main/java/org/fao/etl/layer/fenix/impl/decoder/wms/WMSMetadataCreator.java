package org.fao.etl.layer.fenix.impl.decoder.wms;

import org.fao.etl.layer.fenix.impl.decoder.wms.dto.Layer;
import org.fao.etl.layer.fenix.impl.decoder.wms.dto.WMT_MS_Capabilities;
import org.fao.fenix.commons.msd.dto.full.*;
import org.fao.fenix.commons.msd.dto.type.DocumentType;
import org.fao.fenix.commons.msd.dto.type.LayerType;
import org.fao.fenix.commons.msd.dto.type.RepresentationType;
import org.fao.fenix.commons.msd.dto.type.ResponsiblePartyRole;
import org.fao.fenix.commons.utils.FileUtils;
import org.fao.fenix.commons.utils.JSONUtils;

import javax.inject.Inject;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WMSMetadataCreator {
    @Inject public FileUtils fileUtils;

    public MeIdentification<DSDGeographic> createMetadata (Layer xml, Layer parentXml, MeIdentification<DSDGeographic> parentMetadata) throws Exception {
        Collection<String> errors = new LinkedList<>();
        MeIdentification<DSDGeographic> metadata = new MeIdentification<DSDGeographic>();
        metadata.setUid(xml.Name);
        metadata.setTitle(toLabel(xml.Title==null && parentXml!=null ? parentXml.Title : xml.Title));

        MeContent meContent = new MeContent();
        meContent.setResourceRepresentationType(RepresentationType.geographic);
        meContent.setDescription(toLabel(xml.Abstract==null && parentXml!=null ? parentXml.Abstract : xml.Abstract));
        meContent.setKeywords(xml.KeywordList!=null && xml.KeywordList.Keyword!=null && xml.KeywordList.Keyword.size()>0 ? xml.KeywordList.Keyword : null);
        metadata.setMeContent(meContent);

        MeSpatialRepresentation meSpatialRepresentation = new MeSpatialRepresentation();
        meSpatialRepresentation.setLayerType(LayerType.raster); //TODO implement logic to identify layer type
        metadata.setMeSpatialRepresentation(meSpatialRepresentation);
        if (xml.LatLonBoundingBox!=null) {
            SeBoundingBox seBoundingBox = new SeBoundingBox();
            seBoundingBox.setXmin(xml.LatLonBoundingBox.minx);
            seBoundingBox.setYmin(xml.LatLonBoundingBox.miny);
            seBoundingBox.setXmax(xml.LatLonBoundingBox.maxx);
            seBoundingBox.setYmax(xml.LatLonBoundingBox.maxy);
            meSpatialRepresentation.setSeBoundingBox(seBoundingBox);
        }

        if (parentMetadata!=null) {
            MeIdentification parent = new MeIdentification<>();
            parent.setUid(parentMetadata.getUid());
            parent.setVersion(parentMetadata.getVersion());
            metadata.setParents(Arrays.asList(parent));
        }

        DSDGeographic dsd = create("layerDSD");
        metadata.setDsd(dsd);
        if (dsd!=null && xml.Style!=null) {
            Map<String, Object> extension = new HashMap<>();
            extension.put("style", xml.Style);
            dsd.setContextExtension(extension);
        }

        //Manage errors
        if (errors.size()==0) {
            removeEmpty(metadata);
            validate(metadata, errors);
        }
        if (errors.size()>0) {
            StringBuilder message = new StringBuilder("Errors creating metadata for the layer ").append(xml.Name).append('\n');
            for (String error : errors)
                message.append(error).append('\n');
            throw new Exception(message.toString());
        }
        //Return metadata
        return metadata;
    }


    public MeIdentification<DSDGeographic> createMetadata (WMT_MS_Capabilities xml) throws Exception {
        Collection<String> errors = new LinkedList<>();
        MeIdentification<DSDGeographic> metadata = new MeIdentification<DSDGeographic>();
        if (xml.Service!=null) {
            metadata.setUid(xml.Service.Name);
            metadata.setTitle(toLabel(xml.Service.Title));

            MeContent meContent = new MeContent();
            meContent.setResourceRepresentationType(RepresentationType.geographic);
            meContent.setDescription(toLabel(xml.Service.Abstract));
            meContent.setKeywords(xml.Service.KeywordList!=null && xml.Service.KeywordList.Keyword!=null && xml.Service.KeywordList.Keyword.size()>0 ? xml.Service.KeywordList.Keyword : null);
            metadata.setMeContent(meContent);

            MeSpatialRepresentation meSpatialRepresentation = new MeSpatialRepresentation();
            meSpatialRepresentation.setLayerType(LayerType.layergroup);
            metadata.setMeSpatialRepresentation(meSpatialRepresentation);

            if (xml.Service.ContactInformation!=null)
                try {
                    OjResponsibleParty contact = toContact(
                            xml.Service.ContactInformation.ContactPersonPrimary!=null ? xml.Service.ContactInformation.ContactPersonPrimary.ContactPerson : null,
                            xml.Service.ContactInformation.ContactPersonPrimary!=null ? xml.Service.ContactInformation.ContactPersonPrimary.ContactOrganization : null,
                            null,
                            xml.Service.ContactInformation.ContactPosition,
                            ResponsiblePartyRole.owner.name(),
                            null,
                            xml.Service.ContactInformation.ContactVoiceTelephone,
                            xml.Service.ContactInformation.ContactAddress!=null ? toAddress(
                                    xml.Service.ContactInformation.ContactAddress.PostCode,
                                    xml.Service.ContactInformation.ContactAddress.Country,
                                    xml.Service.ContactInformation.ContactAddress.StateOrProvince,
                                    xml.Service.ContactInformation.ContactAddress.City,
                                    xml.Service.ContactInformation.ContactAddress.Address,
                                    xml.Service.ContactInformation.ContactAddress.AddressType
                            ) : null,
                            xml.Service.ContactInformation.ContactElectronicMailAddress,
                            null,
                            null
                    );
                    if (contact != null)
                        metadata.setContacts(Arrays.asList(contact));
                } catch (Exception ex) {
                    errors.add(ex.getMessage());
                }

            DSDGeographic dsd = create("parentDSD");
            metadata.setDsd(dsd);
            if (dsd!=null && xml.Capability!=null) {
                Map<String, Object> extension = new HashMap<>();
                extension.put("userDefinedSymbolization", xml.Capability.UserDefinedSymbolization);
                extension.put("request", xml.Capability.Request);
                extension.put("exception", xml.Capability.Exception);
                dsd.setContextExtension(extension);
            }
        }
        //Manage errors
        if (errors.size()==0) {
            removeEmpty(metadata);
            validate(metadata, errors);
        }
        if (errors.size()>0) {
            StringBuilder message = new StringBuilder("Errors creating metadata for the layer group\n");
            for (String error : errors)
                message.append(error).append('\n');
            throw new Exception(message.toString());
        }
        //Return metadata
        return metadata;
    }


    private void removeEmpty (MeIdentification<DSDGeographic> metadata) {
        //TODO
    }

    private void validate (MeIdentification<DSDGeographic> metadata, Collection<String> errors) {
        if (metadata.getUid()==null)
            errors.add("Metadata uid undefined");
        if (metadata.getDsd()==null || metadata.getDsd().getContextSystem()==null)
            errors.add("Metadata context system undefined");
    }


    private static final String templatePath = "/etl/layer/fenix/metadata/templates/";
    private DSDGeographic create (String templateName) throws Exception {
        InputStream templateStream = this.getClass().getResourceAsStream(templatePath+templateName+".json");
        if (templateStream==null)
            return null;
        String templateContent = fileUtils.readTextFile(templateStream);
        return JSONUtils.toObject(templateContent, DSDGeographic.class);
    }


    private Map<String, String> toMap(String[] header, String[] record) {
        Map<String, String> mapRecord = new HashMap<>();
        for (int i=0; i<header.length && i<record.length; i++) {
            record[i] =  record[i]!=null ? record[i].trim() : null;
            mapRecord.put(header[i].trim(), record[i]!=null && record[i].length()>0 ? record[i] : null);
        }
        return mapRecord;
    }

    private MeDocuments toDocument (String documentKind, String title, String date, String notes, String link, String pointOfContact, String organization, String organizationUnit) throws Exception {
        if (
                (documentKind==null || documentKind.trim().length()==0) &&
                        (title==null || title.trim().length()==0) &&
                        (date==null || date.trim().length()==0) &&
                        (notes==null || notes.trim().length()==0) &&
                        (link==null || link.trim().length()==0) &&
                        (pointOfContact==null || pointOfContact.trim().length()==0) &&
                        (organization==null || organization.trim().length()==0) &&
                        (organizationUnit==null || organizationUnit.trim().length()==0) )
            return null;

        Collection<String> errors = new LinkedList<>();

        OjCitation document = new OjCitation();
        DocumentType type = documentKind!=null && documentKind.trim().length()>0 ? DocumentType.valueOf(documentKind.trim()) : null;
        if (type==null && documentKind!=null && documentKind.trim().length()>0)
            errors.add("Wrong document kind");
        document.setDocumentKind(type);
        document.setTitle(toLabel(title));
        try { document.setDate(toDate(date)); } catch (ParseException ex) { errors.add("Wrong date format"); }
        document.setNotes(toLabel(notes));
        document.setLink(link);
        try {
            document.setDocumentContact(toContact(pointOfContact,organization, organizationUnit, null, null, null, null, null, null, null, null));
        } catch (Exception ex) {
            errors.add(ex.getMessage());
        }

        if (errors.size()>0) {
            StringBuilder message = new StringBuilder();
            for (String error : errors)
                message.append(error).append('\n');
            throw new Exception(message.toString());
        } else {
            MeDocuments meDocuments = new MeDocuments();
            meDocuments.setDocument(document);
            return meDocuments;
        }
    }

    private Map<String,String> toLabel(String label, String ... language) {
        if (label==null)
            return null;
        Map<String,String> mapLabel = new HashMap<>();
        mapLabel.put(language!=null && language.length>0 ? language[0] : "EN", label);
        return mapLabel;
    }

    private OjCodeList toOjCodeList (String uid, String version, String ... codes) {
        Collection<String> codesList = new LinkedList<>();
        if (codes!=null)
            for (String code : codes)
                if (code!=null)
                    codesList.add(code);
        if (codesList.size()==0)
            return null;

        OjCodeList oj = new OjCodeList();
        oj.setIdCodeList(uid);
        oj.setVersion(version);
        Collection<OjCode> codesOj = new LinkedList<>();
        for (String code : codesList) {
            OjCode codeOj = new OjCode();
            codeOj.setCode(code);
            codesOj.add(codeOj);
        }
        oj.setCodes(codesOj);

        return oj;
    }

    private static final DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private Date toDate(String dateString) throws ParseException {
        return dateString!=null && dateString.trim().length()>0 ? format.parse(dateString) : null;
    }

    private OjPeriod toPeriod(String from, String to) throws ParseException {
        Date fromDate = toDate(from);
        Date toDate = toDate(to);
        if (fromDate!=null || toDate!=null) {
            OjPeriod period = new OjPeriod();
            period.setFrom(fromDate);
            period.setTo(toDate);
            return period;
        } else
            return null;
    }

    private String toAddress(String postCode, String country, String state, String city, String address, String addressType) {
        StringBuilder buffer = new StringBuilder();
        if (country!=null)
            buffer.append(" - ").append(country);
        if (state!=null)
            buffer.append(" - ").append(state);
        if (postCode!=null)
            buffer.append(" - ").append(postCode);
        if (city!=null)
            buffer.append(" - ").append(city);
        if (address!=null)
            buffer.append(" - ").append(address);
        if (addressType!=null)
            buffer.append(" (").append(addressType).append(')');

        return buffer.length()>0 ? buffer.substring(3) : null;
    }

    private OjResponsibleParty toContact (
            String pointOfContact,
            String organization,
            String organizationUnit,
            String position,
            String role,
            String specify,
            String phone,
            String address,
            String emailAddress,
            String hoursOfService,
            String contactIntruction
    ) throws Exception {

        if (
                (pointOfContact==null || pointOfContact.trim().length()==0) &&
                        (organization==null || organization.trim().length()==0) &&
                        (organizationUnit==null || organizationUnit.trim().length()==0) &&
                        (position==null || position.trim().length()==0) &&
                        (role==null || role.trim().length()==0) &&
                        (specify==null || specify.trim().length()==0) &&
                        (phone==null || phone.trim().length()==0) &&
                        (address==null || address.trim().length()==0) &&
                        (emailAddress==null || emailAddress.trim().length()==0) &&
                        (hoursOfService==null || hoursOfService.trim().length()==0) &&
                        (contactIntruction==null || contactIntruction.trim().length()==0)   )
            return null;

        ResponsiblePartyRole responsiblePartyRole = null;
        if (role!=null && role.trim().length()>0 && (responsiblePartyRole = ResponsiblePartyRole.valueOf(role.trim().toLowerCase()))==null)
            throw new Exception("Undefined contact role: "+role);

        OjResponsibleParty responsibleParty = new OjResponsibleParty();
        responsibleParty.setPointOfContact(pointOfContact);
        responsibleParty.setOrganization(toLabel(organization));
        responsibleParty.setOrganizationUnit(toLabel(organizationUnit));
        responsibleParty.setPosition(toLabel(position));
        responsibleParty.setRole(responsiblePartyRole);
        responsibleParty.setSpecify(toLabel(specify));
        responsibleParty.setPointOfContact(pointOfContact);

        if (
                (phone!=null && phone.trim().length()>0) ||
                        (address!=null && address.trim().length()>0) ||
                        (emailAddress!=null && emailAddress.trim().length()>0) ||
                        (hoursOfService!=null && hoursOfService.trim().length()>0) ||
                        (contactIntruction!=null && contactIntruction.trim().length()>0)   ) {
            OjContact contactInfo = new OjContact();
            contactInfo.setPhone(phone);
            contactInfo.setAddress(address);
            contactInfo.setEmailAddress(emailAddress);
            contactInfo.setHoursOfService(toLabel(hoursOfService));
            contactInfo.setContactInstruction(toLabel(contactIntruction));
            responsibleParty.setContactInfo(contactInfo);
        }

        return responsibleParty;
    }

}
