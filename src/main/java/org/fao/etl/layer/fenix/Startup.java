package org.fao.etl.layer.fenix;

import org.fao.etl.layer.fenix.dto.BatchMode;
import org.fao.etl.layer.fenix.services.Import;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class Startup {
    private @Inject Import importLogic;

    public void mainFlow(@Observes ContainerInitialized event, @Parameters String[] args) {
        //Retrieve command
        BatchMode mode = args!=null && args.length>0 ? BatchMode.valueOf(args[0]) : null;
        if (mode==null) {
            System.out.println("Command not found or unspecified. Available commands are:\n");
            for (BatchMode command : BatchMode.values())
                System.out.println("- '"+command+"'\n");
            return;
        }
        //Execute command
        Collection<MeIdentification> metadataList = null;
        try {
            switch (mode) {
                case wmsUrl:
                    metadataList = importLogic.importWmsByUrl(args.length>1 ? args[1] : null, args.length > 2 ? args[2] : null);
                    break;
                default:
                    throw new Exception("Command unsupported.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Print result
        if (metadataList==null)
            System.out.println("Uploaded no metadata");
        else {
            System.out.println("Uploaded subsequent metadata instances:\n");
            for (MeIdentification metadata : metadataList)
                System.out.println("uid: "+metadata.getUid()+" - context: "+metadata.getDsd().getContextSystem());
        }

    }


}
