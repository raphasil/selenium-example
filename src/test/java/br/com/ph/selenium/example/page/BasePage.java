package br.com.ph.selenium.example.page;

import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class BasePage {
	
	protected final RemoteWebDriver driver;	
	
	public BasePage(RemoteWebDriver driver) {		
		this.driver = driver;		
	}
	
	public abstract void startPage();
	
}
