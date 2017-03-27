package br.com.ph.selenium.example.rule;

import java.util.HashMap;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import br.com.ph.selenium.example.resources.ConfigProperties;

public class ChromeDriverResourceRule extends WebDriverResourceRule {
	
	static {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch");
        String chrome = "chromedriver.exe";
        
        if(os.indexOf("win") >= 0){
            chrome = "chromedriver.exe";
        }else if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 ){
            chrome = arch.contains("64") ? "chromedriver_linux64" : "chromedriver_linux";
        }else if(os.indexOf("mac") >= 0){
            chrome = "chromedriver_mac";
        }
        
        System.setProperty("webdriver.chrome.driver", "drivers/" + chrome);
    }

	@Override
	protected RemoteWebDriver createDriver() {
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		String javaTmpDir = ConfigProperties.getInstance().getDownloadPath();
		chromePrefs.put("download.default_directory", javaTmpDir);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
		return new ChromeDriver(capabilities);
	}

}
