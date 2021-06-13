/**
 * 
 */
package com.persistent.daisy.ext.uielement;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.awt.Toolkit;
import java.awt.Robot;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;

import com.github.opendevl.JFlat;
import com.jayway.jsonpath.JsonPath;
import com.persistent.daisy.base.Configurations;
import com.persistent.daisy.base.CustomConfigurations;
import com.persistent.daisy.base.EncryptionUtils;
import com.persistent.daisy.base.Report;
import com.persistent.daisy.base.Utility;
import com.persistent.daisy.core.ActionData;
import com.persistent.daisy.core.CallRestAPI;
import com.persistent.daisy.core.CallAPI;
import com.persistent.daisy.core.CustomException;
import com.persistent.daisy.core.Executor;
import com.persistent.daisy.core.ICommandHandler;
import com.persistent.daisy.core.LifeCycleHandler;
import com.persistent.daisy.core.browser.SeleniumUtils;
import com.relevantcodes.extentreports.LogStatus;

//import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


/**
 * @author rahul_gade
 *
 */
public class CommandHandler implements ICommandHandler {

	/* (non-Javadoc)
	 * @see com.persistent.daisy.core.ICommandHandler#init()
	 */
	public String commands[]=new String[] {
			"WaitToLoad",
			"ReadAndLog",
			"SkipIfElementMissing",
			"SkipIfElementPresent",
			"Button",
			"ForEachElement",
			"SkipIfElementDisabled",
			"MouseActions",
			"Checkbox",
			"SkipIfVerifyFailed",
			"SkipIfVerifySuccess",
			"Link",
			"List",
			"DeSelect",
			"Radio",
			"Textbox",
			"MouseHover",
			"ByLabel",
			"SwitchToFrame",
			"SwitchToDefault",
			"AcceptAlert",
			"DismissAlert",
			"SwitchToNewWindow",
			"SwitchToDefaltWindow",
			"Lookup",
			"DeActivateVersion",
			"ActivateVersion",
			"SelectSalesApp",
			"EditOrgWideEmail",
			"SelectProcess",
			"SelectVersion",
			"GrantAccessUsingHierarchies",
			"SelectEntry",
			"ElementCount",
			"WaitForElementVisible",
			"WaitForElementToDisappear",
			"ClearText",
			"ClickJS",
			"ScrollIntoView",
			"PRINTSCREEN",
			"Delete",
			"Pageup",
			"PageDown",
			"Textarea",
			"SwitchToTab",
			"DownloadPDF",
			"IsSelected",
			"iframe",
			"ListSelect",
			
	};

	public int skipCount = 0;
	//SeleniumUtils selUtils = new SeleniumUtils();
	WebDriver webDriver = null;
	Executor exec = null;
	String response1 = null;


	@Override
	public LifeCycleHandler init(Executor exec,WebDriver webDriver) {
		this.webDriver = webDriver;
		this.exec = exec;
		return new UIElementLifeCycleHandler();
	}

	/* (non-Javadoc)
	 * @see com.persistent.daisy.core.ICommandHandler#handle()
	 */
	@Override
	public boolean handle(ActionData actionData) throws CustomException {
		boolean isHandled = false;
		//Perform perform = Perform.valueOf(actionData.getActionType());

		switch(actionData.getActionType())
		{
		case "WaitToLoad":
		{
			if(actionData.getData()!=null && actionData.getData()!="")
			{
				if(actionData.getData().matches("-?\\d+(\\.\\d+)?")){	//isNumeric
					try {
						//exec.cmd.wait(Integer.parseInt(actionData.getData().trim()));
						int seconds = Integer.parseInt(actionData.getData().trim());
						int timeout = seconds *1000;
						Thread.sleep(timeout);
						Report.getReport().logPass("WaitToLoad applied for "+actionData.getData()+" seconds");
						System.out.println("[log] WaitToLoad applied for "+actionData.getData()+" seconds");
						//wait(Integer.parseInt(actionData.getData().trim()));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					try {

						int seconds = 4;
						Thread.sleep(seconds * 1000);							
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			isHandled = true;
			break;
		}
		case "ByLabel":
		{
			exec.isLocatorValueDataMissing(actionData,"ByLabel");
			exec.waitForPageLoaded();
			String LabelName = actionData.getLocatorValueData();

			exec.waitForElementtoBeVisible(By.xpath("//label[contains(.,'"+ LabelName +"')]"));

			String DataValue = actionData.getData();

			exec.ByLabelMethod(LabelName, DataValue);
			System.out.println("for label command handler working fine");
			Report.getReport().logPass("Successfully  run Bylabel for " + LabelName );
			isHandled = true;
			break;
		}            
		case "ReadAndLog":
		{
			exec.isLocatorTypeMissing(actionData,"ReadAndLog");
			String locator1 = actionData.getLocatorValueData();
			WebElement val = exec.driver.findElement(By.xpath(locator1));
			String message = exec.cmd.ReadandLog(actionData.getData(), val);

			System.out.println("Message From ReadAndLog: "+message);
			Report.getReport().logPass(message);
			isHandled = true;
			break;

		}


		case "ClearText":
		{
			exec.isLocatorTypeMissing(actionData,"ClearText");
			WebElement cTxt = exec.findElement((By)(actionData.getLocatorTypeData().get(0)));
			cTxt.clear();
			Report.getReport().logPass("Successfully cleared the Text " + actionData.getActionFieldName());
			System.out.println("[log] Successfully cleared the Text " + actionData.getActionFieldName());
			isHandled = true;
			break;	
		} 

		case "SkipIfElementMissing":
		{
			exec.isLocatorTypeMissing(actionData,"SkipIfElementMissing");
			exec.waitForPageLoaded();
			List<WebElement> list = exec.driver.findElements(actionData.getLocatorTypeData().get(0));
			boolean b = true;
			for(WebElement wb : list){
				if(wb.isDisplayed()){
					b = false;
					break;
				}
			} 			
			String lineC = actionData.getData();
			System.out.println("[log] Checking if XPath element with name "+actionData.getLocatorTypeData().toString()+" exist");
			if(b){
				try{
					exec.skipCount = Integer.parseInt(lineC);
				}catch(Exception ex)
				{
					System.out.println("[log] Unable to read line to skip, resuming to next line");
					exec.skipCount=0;
				}
				System.out.println("[log] Skipping "+lineC+" Lines");
			}
			else{
				System.out.println("[log] Element exist");		
			}
			System.out.println("[log] Successfully executed SkipIfElementMissing");
			Report.getReport().logPass("Successfully executed SkipIfElementMissing");
			isHandled = true;
			break;
		}
		case "SkipIfElementPresent":
			{
				exec.isLocatorTypeMissing(actionData,"SkipIfElementPresent");
				exec.waitForPageLoaded();
				List<WebElement> list = exec.driver.findElements(actionData.getLocatorTypeData().get(0));
				boolean b = false;
				for(WebElement wb : list){
					if(wb.isDisplayed()){
						b = true;
						break;
					}
				} 			
				String lineC = actionData.getData();
				System.out.println("[log] Checking if XPath element with name "+actionData.getLocatorTypeData().toString()+" exist");
				if(b){
					try{
						exec.skipCount = Integer.parseInt(lineC);
					}catch(Exception ex)
					{
						System.out.println("[log] Unable to read line to skip, resuming to next line");
						Report.getReport().logFail(" Unable to read line to skip, resuming to next line");
						exec.skipCount=0;
					}
					System.out.println("[log] Skipping "+lineC+" Lines");
				}
				else{
					System.out.println("[log] Element doesn't exist");		
				}
				System.out.println("[log] Successfully executed SkipIfElementPresent");
	            Report.getReport().logPass("Successfully executed SkipIfElementPresent");
				isHandled = true;
				break;


			}
		case "SkipIfElementDisabled":
		{
			exec.isLocatorTypeMissing(actionData,"SkipIfElementDisabled");
			exec.waitForPageLoaded();
			List<WebElement> list = exec.driver.findElements(actionData.getLocatorTypeData().get(0));
			boolean b = true;
			for(WebElement wb : list){
				if(wb.isEnabled()){
					b = false;
					break;
				}
			} 			
			String lineC = actionData.getData();
			System.out.println("[log] Checking if XPath element with name "+actionData.getLocatorTypeData().toString()+" exist");
			if(b){
				try{
					exec.skipCount = Integer.parseInt(lineC);
				}catch(Exception ex)
				{
					System.out.println("[log] Unable to read line to skip, resuming to next line");
					exec.skipCount=0;
				}
				System.out.println("[log] Skipping "+lineC+" Lines");
			}
			else{
				System.out.println("[log] Element exist");		
			}
			System.out.println("[log] Successfully executed SkipIfElementDisabled");
			Report.getReport().logPass("Successfully executed SkipIfElementDisabled");
			isHandled = true;
			break;
		}

		case "SkipIfVerifyFailed":
		{
			exec.waitForPageLoaded();
			//                List<WebElement> list = exec.driver.findElements(actionData.getLocatorTypeData().get(0));
			boolean b = !exec.skipIfVerifyFailed();
			//                for(WebElement wb : list){
			//                    if(wb.isEnabled()){
			//                        b = false;
			//                        break;
			//                    }
			//                }            
			String lineC = actionData.getData();
			//                System.out.println("[log] Checking if XPath element with name "+actionData.getLocatorTypeData().toString()+" exist");
			if(b){
				try{
					exec.skipCount = Integer.parseInt(lineC);
				}catch(Exception ex)
				{
					System.out.println("[log] Unable to read line to skip, resuming to next line");
					exec.skipCount=0;
				}
				System.out.println("[log] Skipping "+lineC+" Lines");
			}
			else{
				System.out.println("[log] Element verification passed");       
			}
			System.out.println("[log] Successfully executed SkipIfVerifyFailed");
			Report.getReport().logPass("Successfully executed SkipIfVerifyFailed");
			isHandled = true;
			break;
		}

		 case "SkipIfVerifySuccess":
            {
                exec.waitForPageLoaded();
//                List<WebElement> list = exec.driver.findElements(actionData.getLocatorTypeData().get(0));
                boolean b = !exec.skipIfVerifyFailed();
				
//                for(WebElement wb : list){
//                    if(wb.isEnabled()){
//                        b = false;
//                        break;
//                    }
//                }            
                String lineC = actionData.getData();
//                System.out.println("[log] Checking if XPath element with name "+actionData.getLocatorTypeData().toString()+" exist");
                if(!b){
                    try{
                        exec.skipCount = Integer.parseInt(lineC);
                    }catch(Exception ex)
                    {
                        System.out.println("[log] Unable to read line to skip, resuming to next line");
                        exec.skipCount=0;
                    }
                    System.out.println("[log] Skipping "+lineC+" Lines");
                }
                else{
                    System.out.println("[log] Element verification Failed");       
                }
                System.out.println("[log] Successfully executed SkipIfVerifySuccess");
                Report.getReport().logPass("Successfully executed SkipIfVerifySuccess");
                isHandled = true;
                break;
            }

		case "SkifIfVerifyCompareFailed":
		{
			exec.waitForPageLoaded();
			boolean x = !exec.SkifIfVerifyCompareFailed();

			String lineC = actionData.getData();
			if(x){
				try{
					exec.skipCount = Integer.parseInt(lineC);
				}catch(Exception ex)
				{
					System.out.println("[log] Unable to read line to skip, resuming to next line");
					exec.skipCount=0;
				}
				System.out.println("[log] Skipping "+lineC+" Lines");
			}
			else{
				System.out.println("[log] Element verification passed");       
			}
			System.out.println("[log] Successfully executed SkifIfVerifyCompareFailed");
			Report.getReport().logPass("Successfully executed SkifIfVerifyCompareFailed");
			isHandled = true;
			break;
		}

		/*    case "ForEachElement": {
		    	exec.isLocatorTypeMissing(actionData,"ForEachElement");

	            	//System.out.println("************"+actionData.getLocatorValueData()+actionData.getData());
					try {
						exec.forEachElement(actionData.getLocatorValueData(),actionData.getData());
					} catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException
							| InstantiationException | IllegalAccessException | CustomException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	            Report.getReport().logPass("Successfully executed ForEachElement");
	            System.out.println("[log] Successfully executed ForEachElement");
	            isHandled = true;
	            break;
	        }
		 */     
		/*
		 * case "NextElement": { exec.isLocatorTypeMissing(actionData,"NextElement");
		 * exec.nextElement();
		 * Report.getReport().logPass("Successfully executed NextElement");
		 * System.out.println("[log] Successfully executed NextElement"); isHandled =
		 * true; break; }
		 */

		case "Button": {
			exec.isLocatorTypeMissing(actionData,"Button");
			exec.waitForPageLoaded();
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			exec.click((By) (actionData.getLocatorTypeData().get(0)));
			Report.getReport().logPass("Successfully clicked on " + actionData.getActionFieldName() + " Button");
			System.out.println("[log] Successfully clicked on " + actionData.getActionFieldName() + " Button");
			isHandled = true;
			break;
		}

		case "Checkbox":
		{
			exec.isLocatorTypeMissing(actionData,"Checkbox");
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			if(actionData.getData().equalsIgnoreCase("Checked")){
				exec.selectCheckBox(actionData.getLocatorTypeData().get(0));
				Report.getReport().logPass("Selected checkbox for " +actionData.getActionFieldName());
				System.out.println("[log] Selected checkbox for " +actionData.getActionFieldName());
			} else {
				exec.deselectCheckBox(actionData.getLocatorTypeData().get(0));
				Report.getReport().logPass("Un Selected checkbox for " +actionData.getActionFieldName());
				System.out.println("[log] Un Selected checkbox for " +actionData.getActionFieldName());
			}
			isHandled = true;
			break;
		}
		case "Link":
		{
			exec.isLocatorTypeMissing(actionData,"Link");
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			exec.click(actionData.getLocatorTypeData().get(0));

			Report.getReport().logPass("Successfully clicked on " + actionData.getActionFieldName() + " Link");
			System.out.println("[log] Successfully clicked on " + actionData.getActionFieldName() + " Link");
			isHandled = true;
			break;
		}
		case "List":
		{
			exec.isLocatorTypeMissing(actionData,"List");
			exec.isDataMissing(actionData,"List");
			exec.waitForPageLoaded();
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));

			exec.verifyListValue(actionData.getLocatorTypeData().get(0), actionData.getData());

			Report.getReport().logPass("Selected " +actionData.getData() + " from " + actionData.getActionFieldName() + " dropdown");
			System.out.println("[log] Selected " +actionData.getData() + " from " + actionData.getActionFieldName() + " dropdown");
			isHandled = true;
			break;
		}
		case "DeSelect":
		{
			exec.isLocatorTypeMissing(actionData,"DeSelect");
			exec.isDataMissing(actionData,"DeSelect");
			exec.waitForPageLoaded();
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			exec.deSelectByVisibleText(actionData.getLocatorTypeData().get(0), actionData.getData());
			Report.getReport().logPass("De Selected " +actionData.getData() + " from " + actionData.getActionFieldName() + " dropdown");
			System.out.println("[log] DE Selected " +actionData.getData() + " from " + actionData.getActionFieldName() + " dropdown");
			isHandled = true;
			break;
		}
		case "Radio":
		{  
			exec.isLocatorTypeMissing(actionData,"Radio");
			exec.waitForPageLoaded();
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));

			if(null != actionData.getData() && actionData.getData().equalsIgnoreCase("yes")) {
				boolean selected = exec.findElement(actionData.getLocatorTypeData().get(0)).isSelected();
				if(!selected) {
					exec.click(actionData.getLocatorTypeData().get(0));
					Report.getReport().logPass("Successfully clicked on " + actionData.getActionFieldName() + " Radio Button");
					System.out.println("[log] Successfully clicked on " + actionData.getActionFieldName() + " Radio Button");
				}
			}
			isHandled = true;
			break;

		}
		case "IsSelected": {

			exec.isLocatorTypeMissing(actionData,"IsSelected");
			exec.waitForPageLoaded();
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			WebElement wb = exec.driver.findElement(actionData.getLocatorTypeData().get(0));
			boolean b = false;
			if (exec.isSelected(actionData.getLocatorTypeData().get(0))) {
				b = true;
			}
			String lineC = actionData.getData();
			System.out.println("[log] Checking if XPath element with name " + actionData.getLocatorTypeData().toString()
					+ " is selected");
			if (b) {



				try {



					exec.skipCount = Integer.parseInt(lineC);
				} catch (Exception ex) {
					System.out.println("[log] Unable to read line to skip, resuming to next line");
					exec.skipCount = 0;
				}
				System.out.println("[log] Skipping " + lineC + " Lines");
			} else {
				System.out.println("[log] Element is not selected");
			}



			System.out.println("[log] Successfully executed IsSelected");
			Report.getReport().logPass("Successfully executed IsSelected");
			isHandled = true;
			break;



		}
		case "iframe": {


			exec.isLocatorValueDataMissing(actionData,"iframe");
			String iframeXpath = actionData.getLocatorValueData();
			exec.iframe(iframeXpath);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println("iframe executed");
			Report.getReport().logPass("Successfully executed iframe");
			System.out.println("[log] Successfully executed iframe");
			isHandled = true;
			break;
		}
		/*	case "ListSelect":
        {
        	exec.isLocatorTypeMissing(actionData,"ListSelect");
            exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));

            exec.click(actionData.getLocatorTypeData().get(0));
            exec.selectCheck(actionData.getLocatorTypeData().get(0));
            /*WebDriverWait waitList = new WebDriverWait((new BrowserConfig()).driver, 10);
            waitList.until(new ExpectedCondition<Boolean>() {
                public Boolean hasMoreThanOneOptions(WebDriver driver) {
                    return driver.findElements(By.cssSelector("#alertSubCatSelectBox option")).size() > 1;
              }



                @Override
                public Boolean apply(WebDriver arg0) {
                    // TODO Auto-generated method stub
                    return null;
                }
            });*/

		/* Report.getReport().logPass("Successfully clicked on " + actionData.getActionFieldName() + " ListSelect");
            System.out.println("[log] Successfully clicked on " + actionData.getActionFieldName() + " ListSelect");
            isHandled = true;
            break;
        }*/


		case "ListSelect":
		{
			int count = 0;
			boolean clicked = false;
			while (count < 3 && !clicked)
			{
				System.out.println("*//[log] ListSelect Retry Cout: "+count);
				try 
				{
					exec.isLocatorTypeMissing(actionData,"ListSelect");
					exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
					Select select = new Select(exec.driver.findElement(actionData.getLocatorTypeData().get(0)));
					select.selectByVisibleText(actionData.getData());
					clicked = true;
				} 
				catch (StaleElementReferenceException e)
				{
					e.toString();
					System.out.println("Trying to recover from a stale element :" + e.getMessage());
					count = count+1;
				}
			}
			Report.getReport().logPass("Successfully clicked on " + actionData.getActionFieldName() + " ListSelect");
			System.out.println("[log] Successfully clicked on " + actionData.getActionFieldName() + " ListSelect");
			isHandled = true;
			break;    
		}

		/*	case "Textbox":
            {
                //exec.waitForPageLoaded();
                exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));

                String actionData_data = actionData.getData();
                Pattern pattern = null;
                Matcher matcher = null;

                pattern = Pattern.compile("\\$config\\{(.*)\\}");
                matcher = pattern.matcher(actionData_data);

                matcher.find();


                 // check for encrypted password

                if(actionData_data.contains("$config{_")) {


                    String encryptedkey_from_excel = matcher.group(1);
                    String encryptedkey = CustomConfigurations.getInstance().getAttribute(encryptedkey_from_excel);

                    try{
                        actionData.setData(EncryptionUtils.decrypt(encryptedkey));
                        Report.getReport().logPass("Entered Password Successfully");
                          System.out.println("[log] Entered Password Successfully");

                    }catch(Exception ex)
                    {
                        System.out.println("[log] Using blank password, since Error decrypting password : " + ex.getMessage());
                        actionData.setData("");
                    }


                }

                 // check for username and decrypted password

                else if(actionData_data.contains("$config{")){

                    String key_from_excel = matcher.group(1);
                    String key = CustomConfigurations.getInstance().getAttribute(key_from_excel);
                    actionData.setData(key);
                    Report.getReport().logPass("Entered " +actionData.getData() + " in " +actionData.getActionFieldName() + " field");
                    System.out.println("[log] Entered " +actionData.getData() + " in " +actionData.getActionFieldName());
                }
                exec.enter(actionData.getLocatorTypeData().get(0), actionData.getData());

                  isHandled = true;
                break;
            }*/


		case "Textbox":
		{
			exec.isLocatorTypeMissing(actionData,"Textbox");
			//exec.isDataMissing(actionData,"Textbox");
			exec.waitForPageLoaded();
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));

			String actionData_data = actionData.getData(); //username/password from script
			Pattern pattern = null;
			Matcher matcher = null;

			pattern = Pattern.compile("\\$config\\{(.*)\\}");
			matcher = pattern.matcher(actionData_data);
			matcher.find();

			if(actionData.getData().equalsIgnoreCase("{null}")) {
				exec.enter(actionData.getLocatorTypeData().get(0), "");
				Report.getReport().logPass("Entered null value in "+actionData.getActionFieldName()+" field");
				System.out.println("[log] Entered null value in "+actionData.getActionFieldName()+" field");
			}   

			//check for encrypted password
			else if(actionData_data.contains("$config{_")) 
			{

				String encryptedkey_from_excel = matcher.group(1);
				String encryptedkey = CustomConfigurations.getInstance().getAttribute(encryptedkey_from_excel);
				try{
					actionData.setData(EncryptionUtils.decrypt(encryptedkey));
					exec.enter(actionData.getLocatorTypeData().get(0), actionData.getData());

					Report.getReport().logPass("Entered Password Successfully");
					System.out.println("[log] Entered Password Successfully");
				}catch(Exception ex)
				{
					actionData.setData("");
					System.out.println("[log] Using blank password, since Error decrypting password : " + ex.getMessage());
				}
			}

			// check for username and decrypted password
			else if(actionData_data.contains("$config{"))
			{

				String key_from_excel = matcher.group(1);
				String key = CustomConfigurations.getInstance().getAttribute(key_from_excel);
				actionData.setData(key);
				exec.enter(actionData.getLocatorTypeData().get(0), actionData.getData());

				//            Report.getReport().logPass("Entered " +actionData.getData() + " in " +actionData.getActionFieldName() + " field");
				//            System.out.println("[log] Entered " +actionData.getData() + " in " +actionData.getActionFieldName());
				//       
				if(actionData.getActionFieldName() != null && actionData.getActionFieldName().contains("Password"))
				{
					Report.getReport().logPass(" Entered Password Successfully");
					System.out.println("[log] Entered Password Successfully");
				}
				else
				{
					Report.getReport().logPass("Entered " +actionData.getData() + " in " +actionData.getActionFieldName() + " field");
					System.out.println("[log] Entered " +actionData.getData() + " in " +actionData.getActionFieldName() + " field");
				}
			}

			else if(actionData_data.equals("${username}")) 
			{
				//data = userName;

				String username = Configurations.getInstance().getAttribute("username");
				// System.out.println("[log] Entered "+actionData_data + " uname "+userName);
				actionData.setData(username);
				exec.enter(actionData.getLocatorTypeData().get(0), actionData.getData());
				Report.getReport().logPass("Entered " +actionData.getData() + " in " +actionData.getActionFieldName() + " field");
				System.out.println("[log] Entered " +actionData.getData() + " in " +actionData.getActionFieldName());

			}
			else if(actionData_data.equals("${password}"))
			{

				try{
					String Password = Configurations.getInstance().getAttribute("password");
					actionData.setData(EncryptionUtils.decrypt(Password));
					exec.enter(actionData.getLocatorTypeData().get(0), actionData.getData());
					Report.getReport().logPass("Entered Password Successfully");
					System.out.println("[log] Entered Password Successfully");
					//data = EncryptionUtils.decrypt(password);
				}catch(Exception ex)
				{
					System.out.println("[log] Using blank password, since Error decrypting password : " + ex.getMessage());
					//data = "";
					actionData.setData("");
				}
			}
			else {

				exec.enter(actionData.getLocatorTypeData().get(0), actionData.getData());  
				if(actionData.getActionFieldName() != null && actionData.getActionFieldName().contains("Password"))
				{
					Report.getReport().logPass(" Entered Password Successfully");
					System.out.println("[log] Entered Password Successfully");
				}
				else
				{
					Report.getReport().logPass("Entered " +actionData.getData() + " in " +actionData.getActionFieldName() + " field");
					System.out.println("[log] Entered " +actionData.getData() + " in " +actionData.getActionFieldName() + " field");
				}

			}
			isHandled = true;
			break;
		}

		case "Textarea":
		{
			exec.isLocatorTypeMissing(actionData,"Textarea");
			exec.isDataMissing(actionData,"Textarea");
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			exec.waitForPageLoaded();
			exec.enter(actionData.getLocatorTypeData().get(0), actionData.getData());
			Report.getReport().logPass("Textarea " +actionData.getActionFieldName());
			System.out.println("[log] Textarea " +actionData.getActionFieldName());
			isHandled = true;
			break;

		}


		case "MouseActions":
		{
			exec.isLocatorTypeMissing(actionData,"MouseActions");
			//exec.isDataMissing(actionData,"MouseActions");
			if(actionData.getData().equals("MouseHover")) {

				WebElement wElement = exec.findElement((By)(actionData.getLocatorTypeData().get(0)));  
				String expectedtooltip = wElement.getAttribute("title");
				exec.hoverToElement(wElement);
				Report.getReport().logPass("Successfully Hovered on element" );
				System.out.println("[log] Successfully Hovered on element ");
				isHandled = true;
				break;
			}
			else if(actionData.getData().equals("RightClick")){
				exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
				WebElement elementLocator = exec.findElement((By)(actionData.getLocatorTypeData().get(0)));
				exec.rightClick(elementLocator);
				Report.getReport().logPass("Successfully performed Right Click ");
				System.out.println("[log] Successfully performed Right Click ");
				isHandled = true;
				break;
			}

			else if(actionData.getData().equals("DoubleClick")){
				try {
					exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
					WebElement elementLocator = exec.driver.findElement(actionData.getLocatorTypeData().get(0));
					Actions actions = new Actions(exec.driver);
					actions.doubleClick(elementLocator).perform();
					System.out.println("[log] Successfully performed Double clicked");
					Report.getReport().logPass("Successfully performed Double clicked ");
				}catch (Exception e) {
					System.out.println("Exception "+ e);
				}
				isHandled =true;
				break;
			}
		}


		case "SwitchToFrame" :
		{
			exec.isLocatorTypeMissing(actionData,"SwitchToFrame");
			exec.waitForPageLoaded();
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			exec.switchToFrame(actionData.getLocatorTypeData().get(0));
			Report.getReport().logPass("Successfully switch to frame " +actionData.getActionFieldName());
			System.out.println("[log] Successfully switch to frame " +actionData.getActionFieldName());
			isHandled = true;
			break;
		}
		case "SwitchToDefault" :
		{
			exec.waitForPageLoaded();
			exec.switchToDefaultContent();
			Report.getReport().logPass("Switch to default main frame");
			System.out.println("[log] Switch to default main frame");
			isHandled = true;
			break;
		}
		case "AcceptAlert" :
		{
			//waitForPageLoaded();
			/*			try {
				wait(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			exec.switchToAlertAndAccept();
			Report.getReport().logPass("Accepted Alert " +actionData.getActionFieldName());
			System.out.println("[log] Accepted Alert " +actionData.getActionFieldName());
			isHandled = true;
			break;
		}
		case "DismissAlert" :
		{
			exec.waitForPageLoaded();
			exec.switchToAlertAndDismiss();
			Report.getReport().logPass("Dismissed Alert " +actionData.getActionFieldName());
			System.out.println("[log] Dismissed Alert " +actionData.getActionFieldName());
			isHandled = true;
			break;
		}
		case "SwitchToNewWindow" :
		{
			exec.waitForPageLoaded();
			ArrayList<String> tab_new = new ArrayList<String>(exec.getWindowHandles());
			exec.switchToWindow(tab_new.get(1));
			Report.getReport().logPass("Successfully switched to window " +actionData.getActionFieldName());
			System.out.println("[log] Successfully switched to window " +actionData.getActionFieldName());
			isHandled = true;
			break;
		}
		case "SwitchToDefaltWindow" :
		{
			exec.waitForPageLoaded();
			ArrayList<String> tab_old = new ArrayList<String>(exec.getWindowHandles());
			exec.switchToWindow(tab_old.get(0));
			Report.getReport().logPass("Successfully switched to default window " +actionData.getActionFieldName());
			System.out.println("[log] Successfully switched to default window " +actionData.getActionFieldName());
			isHandled = true;
			break;
		}
		case "Lookup" :
		{
			exec.isLocatorTypeMissing(actionData,"Lookup");
			if( (null == actionData.getData()) || actionData.getData().isEmpty() || (actionData.getData().equals("")) ) {
				break;
			}
			exec.SFCmd.lookup(actionData.getLocatorTypeData(), actionData.getData());
			isHandled = true;
			break;
		}
		case "DeActivateVersion" :
		{
			exec.isLocatorTypeMissing(actionData,"DeActivateVersion");
			exec.isDataMissing(actionData, "DeActivateVersion");
			exec.SFCmd.deActivateVersion(actionData.getLocatorTypeData(), actionData.getData());
			isHandled = true;
			break;
		}
		case "ActivateVersion" :
		{
			exec.isLocatorTypeMissing(actionData,"ActivateVersion");
			exec.isDataMissing(actionData, "ActivateVersion");
			exec.SFCmd.activateVersion(actionData.getLocatorTypeData(), actionData.getData());
			isHandled = true;
			break;
		}
		case "SelectSalesApp" :
		{
			exec.isLocatorTypeMissing(actionData,"SelectSalesApp");
			exec.SFCmd.selectSalesApp(actionData.getLocatorTypeData());
			isHandled = true;
			break;
		}
		case "EditOrgWideEmail" :
		{
			exec.isLocatorTypeMissing(actionData,"EditOrgWideEmail");
			exec.isDataMissing(actionData, "EditOrgWideEmail");
			exec.SFCmd.editOrgWideEmail(actionData.getLocatorTypeData(), actionData.getData());
			isHandled = true;
			break;
		}
		case "SelectProcess":
		{
			exec.isDataMissing(actionData, "SelectProcess");
			exec.SFCmd.selectProcess(actionData.getData());
			isHandled = true;
			break;
		}
		case "SelectVersion":
		{
			exec.isDataMissing(actionData, "SelectVersion");
			exec.squidCmd.selectVersion(actionData.getData());
			isHandled = true;
			break;
		}
		case "GrantAccessUsingHierarchies":
		{
			exec.isLocatorTypeMissing(actionData, "GrantAccessUsingHierarchies");
			exec.isDataMissing(actionData, "GrantAccessUsingHierarchies");
			exec.SFCmd.grantAccessUsingHierarchies(actionData.getLocatorTypeData(), actionData.getData());
			break;
		}
		case "SelectEntry" :
		{
			exec.isLocatorTypeMissing(actionData, "SelectEntry");
			exec.isDataMissing(actionData, "SelectEntry");
			exec.SFCmd.selectEntry(actionData.getLocatorTypeData(), actionData.getData());
			try {
				wait(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isHandled = true;
			break;
		}
		case "ElementCount":
		{

			List<WebElement> allElements = exec.driver.findElements(actionData.getLocatorTypeData().get(0));
			if(allElements == null || allElements.isEmpty()){
				exec.tempGlobalVar = "0";
			} else {
				exec.tempGlobalVar = String.valueOf(allElements.size());
			}
			isHandled = true;	
			break;
		}
		case "WaitForElementVisible" :
		{
			exec.waitForPageLoaded();
			exec.isLocatorTypeMissing(actionData, "WaitForElementVisible");
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			Report.getReport().logPass("Wait for Element To be Visible " +actionData.getActionFieldName());
			System.out.println("[log] Wait for Element To be Visible " +actionData.getActionFieldName());
			isHandled = true;
			break;
		}	
		case "WaitForElementToDisappear":
		{
			exec.isLocatorTypeMissing(actionData, "WaitForElementToDisappear");
			exec.waitForElementToDisappear(actionData.getLocatorTypeData().get(0));
			Report.getReport().logPass("Wait for Element To be Disappear " +actionData.getActionFieldName());
			System.out.println("[log] Wait for Element To be Disappear " +actionData.getActionFieldName());
			isHandled = true;
			break;	
		}
		case "ButtonClick":
		{
			exec.buttonClick((By)(actionData.getLocatorTypeData().get(0)));
			Report.getReport().logPass("Successfully clicked on " + actionData.getActionFieldName() + "Button");
			System.out.println("[log] Successfully clicked on " + actionData.getActionFieldName() + " Button");

			isHandled = true;
			break;
		}

		case "ClickJS":
		{
			exec.isLocatorTypeMissing(actionData, "ClickJS");
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			try{
				System.out.println("*//[log] Inside Try Block of clickJS method");
				exec.click((By) (actionData.getLocatorTypeData().get(0)));

			}catch(Exception e){
				System.out.println("*//[log] Inside Catch Block of clickJS method");
				exec.clickUsingJavaScript(actionData.getLocatorTypeData().get(0));
			}

			Report.getReport().logPass("Successfully clicked on " + actionData.getActionFieldName() + "Button");
			System.out.println("[log] Successfully clicked on " + actionData.getActionFieldName() + " Button");
			isHandled = true;
			break;
		}
		case "ScrollIntoView":
		{
			exec.isLocatorTypeMissing(actionData, "ScrollIntoView");
			exec.waitForElementtoBeVisible(actionData.getLocatorTypeData().get(0));
			WebElement wbElement = exec.driver.findElement((By)(actionData.getLocatorTypeData().get(0)));  
			exec.ScrollToView(wbElement);

			Report.getReport().logPass("Successfully Scrolled Into: " + wbElement.getText());
			System.out.println("[log] Successfully Scrolled Into: " + wbElement.getText());

			isHandled = true;
			break;
		}
		case "PRINTSCREEN": {
			try {

				Thread.sleep(120);
				Robot r = new Robot();
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
				File file1 = new File(System.getProperty("user.dir") + "\\Screenshot");
				String workingDirectory = System.getProperty("user.dir");

				if (!file1.exists()) {
					if (file1.mkdir()) {

						System.out.println("Screenshot Folder is creating in process....................");
						System.out.println("Screenshot Folder is created!");
					}
				}
				String filename = "\\Screenshot\\Screenshot_" + formater.format(calendar.getTime()) + ".jpg";
				// String workingDirectory = System.getProperty("user.dir");
				File file = new File(workingDirectory, filename);
				System.out.println("Final filepath : " + file.getAbsolutePath());
				Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				BufferedImage Image = r.createScreenCapture(capture);
				ImageIO.write(Image, "jpg", file);
				System.out.println("Screenshot is saved in screenshot folder");
				Report.getReport().logPass("Screenshot is saved in screenshot folder");
			}

			catch (AWTException | IOException | InterruptedException ex) {
				System.out.println(ex.getMessage());
				Report.getReport().logFail("Screenshot failed");
			} 

			isHandled = true;
			break;
		}


		/*		case "Delete":
            {
            	exec.isLocatorTypeMissing(actionData, "Delete");
            exec.waitForPageLoaded();
            WebElement textbox = exec.driver.findElement((actionData.getLocatorTypeData().get(0)));

            if(Keys.DELETE != null)
            //if(evt.getKeyCode () == KeyEvent.VK_DELETE )

            {
            //textbox.sendKeys(Keys.DELETE);

            //textbox.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
            textbox.sendKeys(Keys.CONTROL +"a",Keys.DELETE) ; 
            Report.getReport().logPass("Deleting ..... ");
            System.out.println("[log] Deleting Done...." );
            }
            isHandled = true;
            break;

            }

	       case "PageUp":
	            {  
	                if(Keys.PAGE_UP!= null){
	                    WebElement wElement = exec.findElement((By)(actionData.getLocatorTypeData().get(0)));

	                    wElement.sendKeys(Keys.PAGE_UP);
	                    exec.PageUp();
	                    Report.getReport().logPass("Page_UP is  working");
	                    System.out.println("Page_UP is working");
	                }
	                isHandled = true;
	                break;
	            }

	            case "PageDown":
	            {
	                if(Keys.PAGE_DOWN!= null){
	                    //WebElement wElement = exec.findElement((By)(actionData.getLocatorTypeData().get(0)));

	                    exec.PageDown();
	                    Report.getReport().logPass("Page_DOWN is  working");
	                    System.out.println("Page_Down is working");
	                }
	                isHandled = true;
	                break;

	            }
	            case "Space":
                {
                	exec.isLocatorTypeMissing(actionData, "Space");
                 exec.waitForPageLoaded();
                WebElement textbox = exec.driver.findElement((actionData.getLocatorTypeData().get(0)));


                        if(Keys.SPACE!=null) {     
                        //WebElement wElement = exec.findElement((By)(actionData.getLocatorTypeData().get(0)));
                        exec.Space();
                        textbox.sendKeys(Keys.SPACE) ;
                        Report.getReport().logPass("Spacing ..... ");
                        System.out.println("log] Spacebar is working");
                        }

                    else {                        
                        exec.PageDown();
                        System.out.println("log] Texbox is empty so Scrolling down......");
                    }

                    isHandled = true;
                    break;

                }*/

		case "SwitchToTab": {
			exec.switchToTab();
			Report.getReport().logPass("Successfully Switch to child tab ");
			System.out.println("log] Successfully Switch to child tab");

			isHandled = true;
			break;
		}

		case "DownloadPDF": {
			exec.downloadPDF();
			Report.getReport().logPass("PDF Download Successfully ");
			System.out.println("log] PDF Download Successfully ");	

			isHandled = true;
			break;
		}

		/*           case "CallRESTAPI":
				{

					exec.isDataMissing(actionData, "CallRESTAPI");
	                String inputValue = actionData.getLocatorValueData();
					String filePath = actionData.getData();
					System.out.println("log] CallRESTAPI=== " + filePath);
					CallRestAPI callingRestAPI = new CallRestAPI();
					Response response = callingRestAPI.execute(filePath);
					System.out.println("==============================Response Body Start===================================");
					System.out.println(response.body().asString());
					System.out.println("==============================Response Body End===================================");
					if(null != inputValue) {
						JsonPath jsonPath = response.jsonPath();
						System.out.println("josnPath is:: "+jsonPath);
						String responseValue =  null;
						List<String> responseList = null;
						if( jsonPath.get(inputValue) instanceof String){
							responseValue = jsonPath.get(inputValue);
							System.out.println("responseValue is :: "+responseValue);
						}
						if(jsonPath.get(inputValue) instanceof List) {
							responseList = jsonPath.getList(inputValue);
							System.out.println("responseValue is :: "+responseList);
						}
					}

					isHandled = true;
					break;
				}*/
		case "SetVarApi":{
            try {
            String jPath = actionData.getData();
            String userdata = actionData.getActionFieldName();
            System.out.println(jPath);
            System.out.println("Got Jpath");
            if (jPath.contains("$."))
            {
            String[] varNames = jPath.split("\\.");
            String varName = varNames[1];
            String response = exec.variableMap.get(varName);
            String result = JsonPath.parse(response).read(jPath);
            String uservar = null;
            if (userdata != null)
            {
                    uservar = userdata;
            }
            else
            {
                uservar = varNames[varNames.length-1];
            }
            
            exec.variableMap.put(uservar, result);
            Report.getReport().logPass("Got response Successfully: " +result);
            System.out.println("[Log] Got response Successfully: " +result);
            }
            else
            {
                exec.variableMap.put(userdata, jPath);
            }
            }catch(Exception e) {
                
            }
            isHandled = true;
            break;
        }

		case "RunApi": {
		            
		
		 
		
		            CallAPI callApi = new CallAPI();
		            String fileName = null;
		            try
		            {            
		                exec.isDataMissing(actionData, "RunApi");                
		                String inputValue = actionData.getActionFieldName();
		                
		                String filePath = actionData.getData();
		                Path path = Paths.get(filePath); 
		                Path file = path.getFileName(); 
		                fileName = file.toString();
		                
		                String filedata = callApi.resolver(filePath,exec);
		                String regex = "\\$\\{\\w+\\}";
		
		 
		
		                Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		                Matcher matcher = pattern.matcher(filedata);
		
		 
		
		                int count=0;
		                while (matcher.find()) {
		                    count++;
		                }
		
		 
		
		                if (count >0)
		                {
		                    throw new Exception("There are still unresolved variables in the API calling file "+fileName );
		                }
		                
		                
		                 response1 = callApi.run(filedata);
		//                if (inputValue.contains("$.") )
		//                {
		//                    callApi.savetoenv(response1, inputValue, exec);
		//                }
		                callApi.saveresponsetoenv(response1, fileName, inputValue, exec);
		                
		                Report.getReport().logPass("Response for " + fileName + ": " + response1);
		                Report.getReport().logPass("Successfully Ran API for : " + fileName);
		            } catch (Exception e1)
		            {
		                e1.printStackTrace();
		                Report.getReport().logFail("Run API failed for : "+fileName);
		                Report.getReport().logFail(e1.getMessage());
		            }
		             
		            
		            isHandled = true;
		        }
		
		 
		
		case "JsonToexcel":
		{
		    CallAPI callApi = new CallAPI();
		    try {
		        String workingDirectory = System.getProperty("user.dir");
		        File file = new File(workingDirectory);
		        File scriptdir = new File(Configurations.SCRIPT_DIR); //------
		        String filename="InputFile";
		        File file2=new File(workingDirectory+"\\API\\json2excel");
		        boolean value=file2.mkdir();
		        if(value) {
		            System.out.println("json2excel folder is created into API");
		        }
		         try {
		         FileWriter fstream = new FileWriter(workingDirectory+"\\API\\json2excel\\"+filename+".txt");
		         fstream.write( response1);
		         fstream.close();
		         } catch (Exception e) {
		                System.err.println("Error: " + e.getMessage());
		         } 
		        String str = new String(Files.readAllBytes(Paths.get(file+"\\API\\json2excel\\InputFile.txt")));    
		        String filename1="output";
		        File filename2=new File(Configurations.SCRIPT_DIR + "\\"+filename1+".xlsx"); //------
		        if(filename2.exists())
		        {
		            filename2.delete();
		        }
		        JFlat flatMe = new JFlat(str);
		        List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
		        flatMe.write2csv(file+"\\API\\json2excel\\output.txt");    
		        String str1 = new String(Files.readAllBytes(Paths.get(file+"\\API\\json2excel\\output.txt")));
		        String str2=str1.replaceAll("\""," ");
		        FileWriter fstream = new FileWriter(workingDirectory+"\\API\\json2excel\\output.txt");
		        fstream.write(str2);
		        fstream.close();
		        callApi.csvToXLSX(file+"\\API\\json2excel\\output.txt",scriptdir+"\\output.xlsx");   //------             
		    } catch (IOException e) {
		        e.printStackTrace();
		    } catch (Exception e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		        isHandled = true;
		        break;
		}        
		}//end of switch
        return isHandled ;
    	}





	/*	}//end of switch 
		return isHandled ;
	}*/

	public void xWait(int num) {
		try {
			if (exec.skipCount <= 0)
				Thread.sleep(num);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see com.persistent.daisy.core.ICommandHandler#cleanup()
	 */
	@Override
	public boolean cleanup() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getName() {
		return "UIElementHandler";
	}
	public Double getVersion() {
		return 1.0;
	}

}
