package br.com.ph.selenium.example.rule;

import java.util.concurrent.TimeUnit;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class WebDriverResourceRule extends ExternalResource {
	
	private RemoteWebDriver driver;
	
	@Override
    protected void before() throws Throwable {
        driver = createDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);        
        driver.manage().window().maximize();
    }

    protected abstract RemoteWebDriver createDriver();

    @Override
    protected void after() {
        driver.quit();
    }
    
    public RemoteWebDriver getDriver() {
        return driver;
    }
}
