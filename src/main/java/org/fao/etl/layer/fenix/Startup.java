package org.fao.etl.layer.fenix;

import org.fao.etl.layer.fenix.dto.BatchMode;
import org.fao.etl.layer.fenix.services.Import;
import org.fao.fenix.commons.utils.Properties;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Startup {
    private @Inject InitProperties initProperties;
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
        switch (mode) {
            case wmsUrl:
                String url = args.length==2 ? args[1] : null;
                if (url==null) {
                    System.out.println("Specify one URL to use 'wmsUrl' mode.");
                    return;
                }
                try {
                    importLogic.importWmsByUrl(args[1], initProperties.getProperty("d3s.url"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Command unsupported.");
        }
    }


}
