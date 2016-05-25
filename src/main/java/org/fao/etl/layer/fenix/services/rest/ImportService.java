package org.fao.etl.layer.fenix.services.rest;

import org.fao.etl.layer.fenix.dto.MetadataId;
import org.fao.etl.layer.fenix.dto.WMSImportParameters;
import org.fao.etl.layer.fenix.services.Import;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.LinkedList;


@Path("layer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImportService {
    @Inject Import importLogic;

    @POST
    @Path("wms")
    public Collection<MetadataId> wmsImportByUrl(WMSImportParameters importParameters) throws Exception {
        return toIdOnly(importLogic.importWmsByUrl(importParameters.getWmsUrl(), importParameters.getD3sUrl()));
    }


    //Utils
    private Collection<MetadataId> toIdOnly (Collection<MeIdentification> source) {
        Collection<MetadataId> metadataList = new LinkedList<>();
        if (source!=null)
            for (MeIdentification sourceMetadata : source)
                metadataList.add(toIdOnly(sourceMetadata));
        return metadataList.size()>0 ? metadataList : null;
    }

    private MetadataId toIdOnly(MeIdentification source) {
        if (source==null)
            return null;
        MetadataId metadata = new MetadataId();
        metadata.uid = source.getUid();
        metadata.version = source.getVersion();
        return metadata;
    }
}
