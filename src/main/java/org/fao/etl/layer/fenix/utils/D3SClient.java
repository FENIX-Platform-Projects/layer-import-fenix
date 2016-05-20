package org.fao.etl.layer.fenix.utils;

import org.fao.fenix.commons.find.dto.filter.FieldFilter;
import org.fao.fenix.commons.find.dto.filter.IdFilter;
import org.fao.fenix.commons.find.dto.filter.StandardFilter;
import org.fao.fenix.commons.msd.dto.full.DSDDataset;
import org.fao.fenix.commons.msd.dto.full.DSDGeographic;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;
import org.fao.fenix.commons.msd.dto.type.RepresentationType;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@ApplicationScoped
public class D3SClient {

    public Collection<MeIdentification> retrieveMetadata(String baseUrl, String contextSystem) throws Exception {
        //Create filter
        StandardFilter filter = new StandardFilter();

        FieldFilter fieldFilter = new FieldFilter();
        fieldFilter.enumeration = Arrays.asList(contextSystem);
        filter.put("dsd.contextSystem", fieldFilter);

        fieldFilter = new FieldFilter();
        fieldFilter.enumeration = Arrays.asList(RepresentationType.geographic.name());
        filter.put("meContent.resourceRepresentationType", fieldFilter);

        //Send request
        Response response = sendRequest(baseUrl+"msd/resources/find", filter, "post", null);
        if (response.getStatus() != 200 && response.getStatus() != 201 && response.getStatus() != 204)
            throw new Exception("Error from D3S requiring existing datasets metadata");

        //Parse response
        Collection<MeIdentification<DSDGeographic>> metadataList = response.getStatus()!=204 ? response.readEntity(new GenericType<Collection<MeIdentification<DSDGeographic>>>(){}) : new LinkedList<MeIdentification<DSDGeographic>>();
        Collection<MeIdentification> result = new LinkedList<>();
        result.addAll(metadataList);
        return result;
    }

    public void insertMetadata (String baseUrl, Collection<MeIdentification> metadataList) throws Exception {
        if (metadataList==null || metadataList.size()==0)
            return;
        //Send request
        Response response = sendRequest(baseUrl+"msd/resources/massive", metadataList, "post", null);
        if (response.getStatus() != 200 && response.getStatus() != 201)
            throw new Exception("Error from D3S adding datasets metadata");
    }

    public void updateMetadata (String baseUrl, Collection<MeIdentification> metadataList) throws Exception {
        if (metadataList==null || metadataList.size()==0)
            return;
        //Send request
        Response response = sendRequest(baseUrl+"msd/resources/massive", metadataList, "put", null);
        if (response.getStatus() != 200 && response.getStatus() != 201)
            throw new Exception("Error from D3S adding datasets metadata");
    }

    public void deleteMetadata (String baseUrl, Collection<MeIdentification> metadataList) throws Exception {
        if (metadataList==null || metadataList.size()==0)
            return;

        //Create filter
        FieldFilter fieldFilter = new FieldFilter();
        fieldFilter.ids = new LinkedList<>();
        for (MeIdentification<DSDGeographic> metadata : metadataList)
            fieldFilter.ids.add(new IdFilter(metadata.getUid(), metadata.getVersion()));
        StandardFilter filter = new StandardFilter();
        filter.put("id", fieldFilter);

        //Send request
        Response response = sendRequest(baseUrl+"msd/resources/massive/delete", filter, "post", null);
        if (response.getStatus() != 200 && response.getStatus() != 201)
            throw new Exception("Error from D3S requiring existing datasets metadata");
    }

    private Response sendRequest(String url, Object entity, String method, Map<String,String> parameters) throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        return target.request(MediaType.APPLICATION_JSON_TYPE).build(method.trim().toUpperCase(), javax.ws.rs.client.Entity.json(entity)).invoke();
    }

}

