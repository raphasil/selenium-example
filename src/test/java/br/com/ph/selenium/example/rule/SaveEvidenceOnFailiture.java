package br.com.ph.selenium.example.rule;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import com.google.common.base.Charsets;

import br.com.ph.selenium.example.helper.DateHelper;
import br.com.ph.selenium.example.resources.ConfigProperties;

public class SaveEvidenceOnFailiture extends TestWatcher {
	
	private final WebDriver driver;
	private final ConfigProperties config;
	private final DateHelper dateHelper;

	public SaveEvidenceOnFailiture(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigProperties.getInstance();
        this.dateHelper = DateHelper.getInstance();
    }

	@Override
	protected void failed(Throwable t, Description description) {
		String oldWindow = driver.getWindowHandle();
        try {
            Set<String> windows = driver.getWindowHandles();
            int idx = 0;
            
            for (String guid : windows) {
            	
                if (windows.size() > 1) {
                    idx++;                    
                }
                
                String path = buildFilePath(description, idx);
                                
                try {
                    driver.switchTo().window(guid);
                    saveImage(path);
                    saveHtml(path);
                    saveConsoleLog(path);
                    saveStackTrace(path, t);
                } catch (RuntimeException ex) {                    
                    ex.printStackTrace();
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new WebDriverException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WebDriverException(ex);
        } finally {
            driver.switchTo().window(oldWindow); // restore original active window
        }
    }
	
	private void saveImage(String path) throws IOException {
		File file = new File(path + ".gif");
		file.getCanonicalFile().getParentFile().mkdirs();
		
		File tmpFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(tmpFile, file);		
	}
	
	private void saveHtml(String path) throws IOException {
		File file = new File(path + ".html");
		file.getCanonicalFile().getParentFile().mkdirs();
		
		FileUtils.writeStringToFile(file, driver.getPageSource(), Charsets.UTF_8);		
	}
	
	private void saveStackTrace(String path, Throwable t) throws IOException {
		File file = new File(path + "-stacktrace.log");
		file.getCanonicalFile().getParentFile().mkdirs();
		
		PrintWriter pw = new PrintWriter(file);
		
		t.printStackTrace(pw);
		
		pw.close();
	}
	
	private void saveConsoleLog(String path) throws IOException {
		File file = new File(path + ".log");
		file.getCanonicalFile().getParentFile().mkdirs();
		LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
		
		StringBuilder sb = new StringBuilder();		
		
		for( LogEntry entry : logs.getAll() ) {
			sb.append("[ "+ entry.getLevel().getName()  +" ]");
			sb.append(" - "+ entry.getMessage());
			sb.append("\n");
		}		
		
		FileUtils.writeStringToFile(file, sb.toString(), Charsets.UTF_8);		
	}
	
	private String buildFilePath(Description description, int count) {
		String path = config.getEvidenceErrorPath();
		path = replaceDateTimeInPath(path, "{date:");
		path = replaceDateTimeInPath(path, "{time:");
		path = replaceAllInPath(path, "{class}", getShortClassName(description.getClassName()));
		path = replaceAllInPath(path, "{method}", description.getMethodName());
		path = replaceAllInPath(path, "{count}", count > 0 ? "-" + count : "");
		return path;		
	}
	
	private String getShortClassName(String clazz) {
		int index = clazz.lastIndexOf(".") + 1;
		return (index > 0) ? clazz.substring(index) : clazz;
	}
	
	private String replaceAllInPath(String path, String token, String str) {
		
		while(path.contains(token)) {
			path = path.replace(token, str);
		}
		
		return path;
	}
	
	private String replaceDateTimeInPath(String path, String token) {
		
		while(path.contains(token)) {
			int start = path.indexOf(token) + token.length();
			int end = path.indexOf("}", start);
			String fmt = path.substring(start, end);
			path = path.replace(token + fmt + "}", dateHelper.format(fmt));
		}
		
		return path;
	}

}
