package com.example.demo;

import java.util.Enumeration;

import org.springframework.stereotype.Service;

import jpos.JposConst;
import jpos.JposException;
import jpos.Scanner;
import jpos.config.JposEntry;
import jpos.config.JposEntryRegistry;
import jpos.events.DataEvent;
import jpos.events.DataListener;
import jpos.loader.JposServiceLoader;

@Service
public class ScannerService implements DataListener{

    Scanner scanner = new Scanner();
    
    public void openAndClaimScanner() throws JposException {
        System.out.println("Set the JPOS properties");
        System.setProperty("jpos.loader.serviceManagerClass",
                        "jpos.loader.simple.SimpleServiceManager");
        System.setProperty("jpos.config.regPopulatorClass",
                        "jpos.config.simple.xml.SimpleXmlRegPopulator");
        System.setProperty("jpos.util.tracing.TurnOnNamedTracers",
                        "JposServiceLoader,SimpleEntryRegistry,SimpleRegPopulator,XercesRegPopulator");
        System.setProperty("jpos.config.populatorFile", "src/main/resources/jpos.xml");
        
        JposServiceLoader.getManager().getProperties();
        JposEntryRegistry registry = JposServiceLoader.getManager().getEntryRegistry();
        registry.load();
        Enumeration jposRegistries = registry.getEntries();
        
        while (jposRegistries.hasMoreElements()) {
            JposEntry jposEntry = (JposEntry) jposRegistries.nextElement();
            scanner.open(jposEntry.getLogicalName());
            scanner.claim(1000);
            scanner.setDeviceEnabled(true);
            scanner.setDataEventEnabled(true);
            scanner.setDecodeData(true);
            scanner.addDataListener(this);
            System.out.println(scanner.getClaimed());
        }
        health();
    }
    
    @Override
    public void dataOccurred(DataEvent arg0) {
        System.out.println("\nA barcode is scanned: ");
        try {
            System.out.println(new String(scanner.getScanDataLabel()));
            System.out.println(scanner.getScanDataType());
            scanner.setDataEventEnabled(true);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    public void health() throws JposException {
        scanner.checkHealth(JposConst.JPOS_CH_INTERNAL);
        System.out.println(scanner.getCheckHealthText());
    }
}
