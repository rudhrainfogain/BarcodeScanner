package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.demo")
public class DemoApplication implements CommandLineRunner{

    @Autowired
    private ScannerService scannerService;
    
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
	}

    @Override
    public void run(String... args) throws Exception {
        scannerService.openAndClaimScanner();
    }

}
