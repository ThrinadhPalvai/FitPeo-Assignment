package ThrinadhPalvai.FitpeoAssignment;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Assignment {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		//entering into fitpeo Website
		driver.get("https://www.fitpeo.com/");
		// clicking on revenue calculator page
		driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/header[1]/div[1]/div[3]/div[6]/a[1]/div[1]")).click();
		
		Thread.sleep(3000);
		JavascriptExecutor js= (JavascriptExecutor)driver;
		// scrolling the window to the visibility of slider
		js.executeScript("window.scrollBy(0,400)");
		
		Thread.sleep(1000);
		// to drag the slider to 820
		WebElement slider =wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".MuiSlider-thumb.MuiSlider-thumbSizeMedium.MuiSlider-thumbColorPrimary.MuiSlider-thumb")));
		Actions a=new Actions(driver);
		a.clickAndHold(slider).moveByOffset(93, 0).release().perform();
		
			// Adjust the range and step based on observed behavior 
			a.clickAndHold(slider).moveByOffset(1, 0).release().perform();
			// Verify the slider value WebElement
			WebElement sliderValue = driver.findElement(By.cssSelector("[aria-invalid='false']"));
			String valueText = sliderValue.getAttribute("value");
			if (!valueText.isEmpty()) {
			int currentValue = Integer.parseInt(valueText);
			System.out.println("Current Slider Value: " + currentValue);
			}
			//entering value 560 in text area
		WebElement textArea = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[aria-invalid='false']")));
		textArea.click();
		
		js.executeScript("arguments[0].value = '';", textArea);
		textArea.sendKeys("560");
		
		String enteredText = textArea.getAttribute("value");
		System.out.println("Entered Text: " + enteredText);
		//scrolling window to select the check boxes
		js.executeScript("window.scrollBy(0,600)");
		Thread.sleep(5000);
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div[1]/label/span[1]/input")).click();
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div[2]/label/span[1]")).click();
		Thread.sleep(1000);
		//again scrolling the window to select 3rd check box
		js.executeScript("window.scrollBy(0,601)");
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div[3]/label/span[1]/input")).click();
		Thread.sleep(1000);
		//printing the Total Recurring Reimbursement value
		System.out.println("Total Recurring Reimbursement: " + driver.findElement(By.cssSelector("p:nth-child(4) p:nth-child(1)")).getText());
		driver.quit();
		}
	}

