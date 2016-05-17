package org.fao.etl.layer.fenix.services.rest;

import org.fao.etl.layer.fenix.dto.ImportStatus;

import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Path("import")
public class ImportService {

    @POST
    public ImportStatus startWmsImportByUrl(String url) throws Exception {
        return null; //TODO
    }
}
