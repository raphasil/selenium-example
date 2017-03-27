package br.com.ph.selenium.example.page;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.openqa.selenium.remote.RemoteWebDriver;

import br.com.ph.selenium.example.rule.ChromeDriverResourceRule;
import br.com.ph.selenium.example.rule.SaveEvidenceOnFailiture;

public abstract class BaseTest<P extends BasePage> {
	
	@ClassRule
	public static ChromeDriverResourceRule resource = new ChromeDriverResourceRule();
	
	@Rule
	public SaveEvidenceOnFailiture saveEvidenceOnFailiture = new SaveEvidenceOnFailiture(resource.getDriver());
	
	@Before
	public abstract void before() throws Exception;
	
	protected P page = createPage(resource.getDriver());
	
	protected abstract P createPage(RemoteWebDriver driver);
}
