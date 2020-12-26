package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.http.HttpStatus;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step defs for calculating R0 and uploading a CSV file
 * 
 * @author Marwah Mahate
 *
 */
public class CalculateR0StepDefs extends CucumberTest {
	
	private final String baseUrl      = "http://localhost:8080/iTrust2";
	
	private final User hcp  = new User( "hcp", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_HCP, 1 );
	
	private final String fileName = System.getProperty("user.dir") + "/passenger-data.csv";
	
	/**
	 * Log in as HCP
	 */
	@When ("I log into iTrust2 as a virologist")
	public void loginVirologist () {
		driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "hcp" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        Passenger.deleteAll();
	}
	
    /**
     * navigate to Passenger page
     */
    @When ( "I navigate to the Upload Passenger List page" )
    public void navigateToUploadPassengerPage () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('uploadPassengerList').click();" );
		waitForAngular();
    }
    
    /**
     * upload a file
     */
    @When ( "I upload a file" )
    public void uploadFile() {
    	driver.findElement(By.id("file")).sendKeys(fileName);
    	final WebElement submit = driver.findElement( By.name( "submitCSVFile" ) );
    	submit.click();
    }
    
    
    /**
     * File uploaded successfully
     */
    @Then ( "It displays the number of passenger entries made" )
    public void uploadedSuccessfully () {
        waitForAngular();
        assertTrue( driver.getPageSource().contains( "Number of new passenger entries made: " ) );
    }
    
    /**
     * navigate to Calculate R0 page
     */
    @Then ("I navigate to the Calculate R0 page")
    public void navigateToCalculateR0 () {
    	( (JavascriptExecutor) driver ).executeScript( "document.getElementById('calculate R0').click();" );
		waitForAngular();
    }
    
    
    /**
     * Click calculate
     */
    @When ("I click Calculate")
    public void clickCalculate () {
    	final WebElement click = driver.findElement( By.name( "submit" ) );
    	click.click();
    }
    
    /**
     * Successful Calculation
     */
    @Then ("it displays the calculation")
    public void successfulCalculation () {
    	waitForAngular();
        assertTrue( driver.getPageSource().contains( "Calculated R_0 value: " ) );
    }


}
