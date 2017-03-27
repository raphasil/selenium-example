package br.com.ph.selenium.example.resources;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigProperties {
	
	private static ConfigProperties instance;
	
	public synchronized static ConfigProperties getInstance() {
		if(instance == null) {
			instance = new ConfigProperties();
		}
		
		return instance;
	}

	private final CompositeConfiguration config;
	
	private ConfigProperties() {
		
		config = new CompositeConfiguration();
		
		try {
			config.addConfiguration(new PropertiesConfiguration("config.properties"));			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getEvidenceErrorPath() {
		return config.getString("path.evidence.erros", "./evidences/erros/{date:yyyyMMdd}/{class}/{time:HHmmssSSS}-{method}{count}");
	}	

	public String getDownloadPath() {
		return config.getString("path.download", System.getProperty("java.io.tmpdir"));
	}
}
