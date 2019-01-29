package com.volvo.mfg.agv.pages;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.volvo.automation.commonutilities.ExcelUtils;
import com.volvo.mfg.StepDefinition.DriverSuite;

public class Tugger_PhaseTwo_Cases extends DriverSuite {

	String sheetName = "Vclas_Assignments";
	public static boolean tempVar = false;
	public int j=0;
			public static int intRackNewSeq;
	public Vclas_tasks vt = new Vclas_tasks(); 
	public HashMap<String, String> tdrow;
	MasWeb_Home mwHome = new MasWeb_Home();
	Vclas_Assignment vAssignment= new Vclas_Assignment();
	
	// Excel class object to access the function
	ExcelUtils excelUtils = new ExcelUtils();


	public void Vclas_Assignment_Navigate() throws InterruptedException {
		driver.switchTo().window(browser[2]);

		// Navigate the Page
		anyClick("Assignment Navigate", By.xpath(prop.getProperty("Vclas_Home.Assignments.Button")));
		Thread.sleep(5000);

		// Verify the Page is displayed
		verifyElementExist("Assignment Page", By.xpath(prop.getProperty("Vclas_Home.Work.Areas.Button")));

	}

	
	public void Vclas_Tugger_Assignment_Delivery_Parameters_False(String flowName) throws InterruptedException {
        try {
               //vt= new Vclas_tasks();
               String strSeqno, seqHandling;
               int Seqno, racksCount, iTempSeqNo, strLopNo;
               String rackStatus, strFlowType;
               String[] strType;
               int j=0;
               Admin_Page adp = new Admin_Page();
               

               // Navigation to Shortage
               anyClick("Shortage Screen", By.xpath(prop.getProperty("Vclas_Home.Shortage.Button")));
               Thread.sleep(5000);

               String sheetName = "Vclas_Assignments";
               tdrow = excelUtils.testCaseRetrieve(refTestDataName, sheetName);
               Seqno = Integer.parseInt(tdrow.get("Object")); // 1
               racksCount = Integer.parseInt(tdrow.get("No_of_Racks"));
               strFlowType = tdrow.get("Sequence_Type");
               strType = tdrow.get("Shortage_Type_Expected").split(",");
               strLopNo = Integer.parseInt(tdrow.get("Lopnr"));
               String strTempSeqNo;

               if (flowName.equalsIgnoreCase("Tugger")) {
                     strSeqno = String.valueOf(Seqno);// 01
                     seqHandling = " ";
                     
               } else {

                     strSeqno = tdrow.get("Object");// 0001
                     System.out.println("seq no :" + strSeqno);
                     seqHandling = StringUtils.repeat("0", 4 - (String.valueOf(Seqno).length())); // 000
                     System.out.println("seq handling:" + seqHandling);
               }

               System.out.println("flow name : " + flowName);

               vAssignment. Vclas_Shortage_Search(strFlowType, strSeqno, flowName);

               iTempSeqNo = Seqno;

               System.out.println("seq no:" + iTempSeqNo);
               // To Verify Initial Status in Shortage Screen for created racks
               for (int i = 1; i <= racksCount; i++) {

                     if (i <= racksCount) {
                            rackStatus = "Planned";
                     } else {
                            rackStatus = "Active";
                            
                     }
                     List<String> listr = vt.getList();
               System.out.println("full TASK ID: " +listr);
               
               String retval = listr.get(0);
               System.out.println("retval:" +retval);
               
               List<String> listrempty = vt.getList1();
               System.out.println("empty TASK ID: " +listrempty);
               
               String retvalempty = listrempty.get(0);
               System.out.println("retval:" +retvalempty);
               
               
                     // To verify the Full task id
               vAssignment. Vclas_Shortage_TypeVerify(strFlowType + seqHandling + String.valueOf(iTempSeqNo),
                                   listr.get(j).toString(), strType[0].toUpperCase(), rackStatus);

                     // To Verify the Empty task id
               vAssignment.  Vclas_Shortage_TypeVerify(strFlowType + seqHandling + String.valueOf(iTempSeqNo),
                                   listrempty.get(j).toString(), strType[1].toUpperCase(), "Active");
                     
                     
              j++;   //new
                     // Sequence No
                     iTempSeqNo = iTempSeqNo + 1;

                     if (iTempSeqNo == 100) {
                            iTempSeqNo = 1;
                     }

               }
               
               

               iTempSeqNo = Seqno;
               System.out.println("itemp val:" + iTempSeqNo);
               int lastSubmittedFullRack = 0;

               // Click the Planned Activity in Assignment List for Initial Two Full Racks
               for (int i = 1; i <= racksCount; i++) {

                     reportStep("--------Full rack submission: #B" + strFlowType + " " + String.valueOf(iTempSeqNo)
                                   + "#C Initiated----", "Info", false);
                     // Submitting the Rack
                     System.out.println(
                                   "rck submsn" + strType[0] + " " + strFlowType + seqHandling + String.valueOf(iTempSeqNo));
                     if (flowName.equalsIgnoreCase("Tugger")) {

                    	 vAssignment.  Vclas_AssignmentList_RackSubmission_Tugger(strType[0],
                                         strFlowType + seqHandling + String.valueOf(iTempSeqNo));
                    	 if(i==1) {
                    	 
                    	 reportStep(" #BVerification of  pickup functionality of a Tugger full rack when Node Scan on Pickup, Package scan on Pickup parameters are false for the FPOS node Test case Id:3247--completed#C", "pass", false);
                    	 }
                     }	 else {
                    	 vAssignment.  Vclas_AssignmentList_RackSubmission_JISJIT(strType[0].toUpperCase(),
                                          strFlowType + seqHandling + String.valueOf(iTempSeqNo));
                     }

                     lastSubmittedFullRack = iTempSeqNo;

                     if (flowName.equalsIgnoreCase("Tugger")) {
     					if (String.valueOf(iTempSeqNo).length() == 1) {
     						strTempSeqNo = "0" + String.valueOf(iTempSeqNo);
     						System.out.println(strTempSeqNo);
     					} else {
     						strTempSeqNo = String.valueOf(iTempSeqNo);
     						System.out.println(strTempSeqNo);
     					}

     					// Calculate LopNo for ScanId
     					strLopNo = strLopNo + Integer.parseInt(tdrow.get("Line_movement"));
     					System.out.println("strLopNo:" + strLopNo);

     					// Calculate Scan Id
     					String scanId = "T" + strFlowType + strTempSeqNo + strLopNo;
     					System.out.println("scan id" + scanId);
     					System.out.println(strFlowType + seqHandling + String.valueOf(iTempSeqNo) + "ok click ");
     					vAssignment.Vclas_AssignmentList(scanId, "Full_Rack", "No", flowName);

     				} else {

     					strTempSeqNo = StringUtils.repeat("0", 4 - String.valueOf(iTempSeqNo).length()) + iTempSeqNo;
     					System.out.println("strTempSeqNo:" + strTempSeqNo);

     					// Calculate Scan Id
     					String scanId = strFlowType + strTempSeqNo;
     					System.out.println("scan id" + scanId);
     					vAssignment.Vclas_AssignmentList(scanId, "Full_Rack", "No", flowName);
     				}
                     
                    // reportStep("#B Verification of pickup and Delivery actions  for a Tugger  full rack with Node scan on pick up,Package Scan on Pick up parameters of FPOS are set to false and Node scan on delivery, Sacn on delivery parameters of TPOS are set to false Test case Id:3250--completed#C", "pass", false);
                   
                     reportStep("--------Full rack submission: #B" + strFlowType + " " + String.valueOf(iTempSeqNo)
                                   + "#C Submission Completed----", "Info", false);

                     // Sequence No
                     iTempSeqNo = iTempSeqNo + 1;

                     if (iTempSeqNo == 100) {
                            iTempSeqNo = 1;
                     }

               }
               
               //intRackNewSeq = Seqno + racksCount - 1;
               vAssignment.Vclas_Shortage_NewRacks_Search(strFlowType,Seqno,racksCount);
               
               /*for (int i = 1; i <= racksCount; i++) {
                     Vclas_Shortage_Rack_Search(strFlowType, seqHandling + String.valueOf(intRackNewSeq), flowName);
                     
               
               
               System.out.println("intRackNewSeq :" +intRackNewSeq);
               
               mwHome.Navigate_Vclas_Task();
        
               vt.Vclas_Search_Partno();
               
                     vt.Vclas_getTuggerTask(strFlowType, String.valueOf(intRackNewSeq), i);
                     intRackNewSeq = intRackNewSeq + 1;
                     System.out.println("intRackNewSeq :" +intRackNewSeq);
                     driver.switchTo().window(browser[2]);
               }*/
               //new
               List<String> listr = vt.getList();
               System.out.println("full TASK ID: " +listr);
               
               List<String> listrempty = vt.getList1();
               System.out.println("empty TASK ID: " +listrempty);
               
               iTempSeqNo = Seqno;

               System.out.println("seq no" + Seqno);
               long clickTime;

               
               // Sending Empty Racks and Full Racks
               for (int i = 1; i <= 1; i++) {                     //changed for 19th testcase--rackCount
                     mwHome.Navigate_Admin_Simulator_Assembly_Line();
                     clickTime = 1;

                     reportStep("--------Empty rack submission: #B" + strFlowType + " " + String.valueOf(iTempSeqNo)
                                   + "#C Initiated----", "Info", false);
                     // Click Assembly Line in Masweb to get Empty Racks Planned
                     // click once to get the correct time to move rack in assembly line
                     adp.Simulator_Assemblyline_Click((int) clickTime);

                     int thersholdValue;

                     if (flowName.equalsIgnoreCase("tugger")) {

                            Parameter_differentracksize parameter = new Parameter_differentracksize();
                            thersholdValue = (int) parameter.planning();
                            double acutalThreshold =parameter.thresholdValue;
                            String threshold=String.valueOf(acutalThreshold);
                            char[] thresholdChar=threshold.toCharArray();
                            String firstChar=String.valueOf(thresholdChar[0]);
                            /*String actualValue=String.valueOf(thersholdValue);
                            char[] val=actualValue.toCharArray();
                            String actualChar=String.valueOf(val[0]);*/
                                  
                     } else {
                            Thread.sleep(50000);
                            // fetch the racksize and update it in excel
                            Sekadm sp = new Sekadm();
                            sp.JISJIT_EmptyRack_Delivery();
                            JISJIT_ParameterPlanning parameter = new JISJIT_ParameterPlanning();

                            thersholdValue = (int) parameter.planning();

                      }

                     int tempThreshold = thersholdValue;
                     reportStep("Planned Time value: " + thersholdValue, "pass", false);
                     System.out.println("Value from opti planning excel" + thersholdValue);
                     do {

                            System.out.println("wait for time value to get reflected in vclas..");
                            driver.switchTo().window(browser[2]);
                            Thread.sleep(120000);

                            // Retrieve time value to plan the racks
                            clickTime = vAssignment.Vclas_Shortage_Time_Verify(" ", strFlowType, seqHandling + String.valueOf(iTempSeqNo),
                                          flowName);
                            System.out.println("time verify done");
                            if (clickTime == thersholdValue) {
                                   reportStep("Planned time verified Actual Time: "+clickTime+" Expected Time: "+tempThreshold, "Info", false); 
                                   break;
                            }
                            if (thersholdValue != 0) {
                                   // assuming value is negative
                                   clickTime = clickTime - thersholdValue;
                                   System.out.println("After adding threshold to click time for: " + clickTime);
                            }

                            else {
                                   clickTime = clickTime + thersholdValue;
                                   System.out.println("After adding threshold to click time for: " + clickTime);
                            }
                            // check whether click time is negative or zero
                            if (clickTime == thersholdValue) {
                                   reportStep("Planned time verified Actual Time: "+clickTime+" Expected Time: "+tempThreshold, "Info", false); 
                                   break;
                            }

                            reportStep(
                                          "--------Empty rack submission: #B" + strFlowType + " " + String.valueOf(iTempSeqNo)
                                                        + "#C Assembly Movement Initiated, No. Of Clicks:  #B" + clickTime + "#C----",
                                          "Info", false);
                            // Accessing the Admin Page to move the Line
                            adp.Simulator_Assemblyline_Click((int) clickTime);

                            reportStep(
                                          "--------Empty rack submission: " + strFlowType + " " + String.valueOf(iTempSeqNo)
                                                        + " Assembly Movement Completed, No. Of Clicks: #B" + clickTime + "#C----",
                                          "Info", false);

                            // Check Whether The Expected Rack Is Planned
                            if (vAssignment.Vclas_Shortage_Time_Verify(" ", strFlowType, seqHandling + String.valueOf(iTempSeqNo),
                                          flowName) == tempThreshold) {
                                   reportStep("Planned time verified Actual Time: "+clickTime+" Expected Time: "+tempThreshold, "Info", false); 
                                   break;
                            }
                            System.out.println("after while" + clickTime + " " + thersholdValue);
                     } while (((int) clickTime != tempThreshold)); // check whether click Time is positive

                     // verify whether rack status is planned
                     vAssignment.Vclas_Shortage_SingleRackStatusVerify(strFlowType + seqHandling + String.valueOf(iTempSeqNo),
                                   "EMPTY_RACK", "Planned");
                     // Deliver The Empty Planned Rack
                     if (flowName.equalsIgnoreCase("Tugger")) {

                    	 vAssignment. Vclas_AssignmentList_RackSubmission_Tugger(strType[1],
                                          strFlowType + seqHandling + String.valueOf(iTempSeqNo));
                    	 reportStep("#BVerification of pickup functionality of a Tugger empty rack when Node Scan on Pickup, Package scan on Pickup parameters are false for the FPOS node Test case Id:3252--completed#C", "pass", false);
                     } else {
                    	 vAssignment.   Vclas_AssignmentList_RackSubmission_JISJIT(strType[1].toUpperCase(),
                                          strFlowType + seqHandling + String.valueOf(iTempSeqNo));
                     }

                     // scan the object for empty rack
                     vAssignment. Vclas_AssignmentList(strFlowType + seqHandling + String.valueOf(iTempSeqNo), "Empty_Rack", "",
                                   flowName);
                     
                     reportStep("--------Empty rack submission: #B" + strFlowType + " " + String.valueOf(iTempSeqNo)
                                   + "#C Submitted Successfully----", "Info", false);
                     reportStep("#BVerification of delivery functionality of a Tugger empty rack when Node Scan on delivery, Scan on delivery parameters are false for the TPOS node Test case Id:3254--completed#C", "pass", false);
                     reportStep("#B Verification of pickup and Delivery actions for a Tugger  empty rack with Node scan on pick up, Package Scan on Pick up parameters of FPOS are set to false and Node scan on delivery, Sacn on delivery parameters of TPOS are set to false Test case Id:3255--completed#C", "pass", false);
                     System.out.println("Delivered Empty rack");

                     iTempSeqNo = iTempSeqNo + 1;
                     System.out.println("incremented val" + iTempSeqNo);
                     System.out.println("current i value" + i);

                     lastSubmittedFullRack = lastSubmittedFullRack + 1;

                     if (flowName.equalsIgnoreCase("Tugger")) {
                            if (String.valueOf(lastSubmittedFullRack).length() == 1) {
                                   strTempSeqNo = "0" + String.valueOf(lastSubmittedFullRack);
                                   System.out.println("after empty rack delivery :" + strTempSeqNo);
                            } else {
                                   strTempSeqNo = String.valueOf(lastSubmittedFullRack);
                                   System.out.println("after empty rack delivery :" + strTempSeqNo);
                            }
                     } else {

                            strTempSeqNo = StringUtils.repeat("0", 4 - String.valueOf(lastSubmittedFullRack).length())
                                          + lastSubmittedFullRack;
                            System.out.println("after empty rack delivery :" + strTempSeqNo);
                     }

                     vAssignment.Vclas_Shortage_SingleRackStatusVerify(strFlowType + seqHandling + strTempSeqNo, "FULL_RACK", "Planned");
                     reportStep("--------Full rack submission: #B" + strFlowType + " " + strTempSeqNo + "#C Initiated----",
                                   "Info", false);

                     // Deliver the Planned Full Empty Rack
                     if (flowName.equalsIgnoreCase("Tugger")) {
                    	 vAssignment.Vclas_AssignmentList_RackSubmission_Tugger(strType[0],
     							strFlowType + seqHandling + String.valueOf(strTempSeqNo));
     				} else {
     					vAssignment.Vclas_AssignmentList_RackSubmission_JISJIT(strType[0].toUpperCase(),
     							strFlowType + seqHandling + String.valueOf(strTempSeqNo));
     				}

     				if (flowName.equalsIgnoreCase("Tugger")) {
     					// Calculate LopNo for ScanId
     					strLopNo = strLopNo + Integer.parseInt(tdrow.get("Line_movement"));
     					System.out.println("strLopNo:" + strLopNo);

     					// Calculate Scan Id
     					String scanId = "T" + strFlowType + strTempSeqNo + strLopNo;
     					System.out.println("scan id" + scanId);

     					vAssignment.Vclas_AssignmentList(scanId, "Full_Rack", "No", flowName); // ask

     				} else {
     					// Calculate Scan Id
     					String scanId = strFlowType + strTempSeqNo;
     					System.out.println("scan id" + scanId);
     					vAssignment.Vclas_AssignmentList(scanId, "Full_Rack", "Yes", flowName);

     				}

                     reportStep("--------Full rack submission: #B" + strFlowType + " " + strTempSeqNo
                                   + "#C Submitted Successfully----", "Info", false);
                     
                     
                     

                     //intRackNewSeq = Integer.parseInt(strTempSeqNo) + racksCount - 1;
               
                     
                     System.out.println("Rack Search after 1E 1F for: " + lastSubmittedFullRack);
                     
               
                     // Verify whether next full empty racks are created
                     intRackNewSeq=vAssignment.Vclas_Shortage_NewRacks_Search(strFlowType, Integer.parseInt(strTempSeqNo),racksCount);
                     System.out.println("intRackNewSeq :" +intRackNewSeq);
                     /*Vclas_Shortage_Rack_Search(strFlowType, seqHandling + String.valueOf(intRackNewSeq), flowName);
                     mwHome.Navigate_Vclas_Task();
                     vt.Vclas_Search_Partno();
                     vt.Vclas_getTuggerTask(strFlowType, String.valueOf(intRackNewSeq), i);
*/
               }
               reportStep(
                            "Rack Created: #B" + strFlowType + " " + Seqno + "#C <BR/>"
                                          + "Overall Full Rack Submission count: #B" + (lastSubmittedFullRack - Seqno + 1)
                                          + "#C <BR/>" + "Last Full Rack Submission: #B" + strFlowType + " " + (lastSubmittedFullRack)
                                          + "#C <BR/>" + "Overall Empty Rack Submission: #B" + (iTempSeqNo - Seqno) + "#C <BR/>"
                                          + "Last Empty Rack Submission: #B" + strFlowType + " " + (iTempSeqNo - 1) + "#C <BR/>"
                                          + "Started from: #B" + strFlowType + " " + Seqno + "#C and Completed till: #B" + strFlowType + " " + intRackNewSeq + "#C <BR/>",
                            "Info", false);

        } catch (Exception e) {
               e.printStackTrace();
               System.out.println("exception thrown in method");
        }

 }
	
	
	//method to set node scan parameters as false
	
		public void Nodes_Parameters_False_Delivery(String Position) throws InterruptedException{
		
		  String sheetName = "Vclas_Assignments";
      tdrow = excelUtils.testCaseRetrieve(refTestDataName, sheetName);
		
		
		// navigating to nodes screen
		anyClick("Home button ", By.xpath(prop.getProperty("Vclas_Home.Home.Button")));
		
		anyClick("Administration Tab", By.xpath(prop.getProperty("Vclas_Home.Administration.Button")));
		
		anyClick("Nodes menu", By.xpath(prop.getProperty("Vclas_Administration.Nodes.Button")));
		
		// verifying search textbox
		if (verifyElementExistReturn(
				By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox"))) == true) {
			Thread.sleep(2000);
			// sending fpos to search textbox
			sendKeys("Search textbox", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox")),Position);

			// clicking search button
			anyClick("Vclas Node screen search button click",By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Button")));

			//expanding the parameters 
			anyClick("Parameter expand button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Paramters.Expand.Button")));

			//making the Nodes scan on pickup as false
			if(isElementPresent( By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodeScanOnDelivery.Inherited.Checkbox.check")))) {
				driver.findElements(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodeScanOnDelivery.Inherited.Checkbox.check"))).get(0).click();
				Thread.sleep(3000);
				if (isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.uncheck")))) {
					reportStep("Nodes Scan on delivery is already inactive", "pass", true);
					System.out.println("Nodes scan on delivery is already inactive");
				}
				
				
				
				else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.check")))) {
					
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.check"))).click();
					Thread.sleep(3000);
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep(" Nodes Scan on delivery is made as inactive", "pass", true);
					System.out.println(" Nodes Scan on delivery is made as inactive");
					
				}
				
			}else {
				if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.check")))) {
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.check"))).click();
					Thread.sleep(3000);
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Nodes Scan on delivery is made as inactive", "pass", true);
					System.out.println("Nodes Scan on delivery is made as inactive");
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.uncheck")))) {
					reportStep("Nodes Scan on delivery is already inactive", "pass", true);
					System.out.println("Nodes scan on delivery is already inactive");
				}
			}
			

			
				
				//setting the  scan on delivery value as inactive
							
			//making  scan on delivery as false
			
			if(isElementPresent( By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Inherited.Checkbox.check")))) {
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Inherited.Checkbox.check"))).click();
				//if(verifyElementExist("scan on delivery value--check",By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))==true) {
				if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))) {	
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Scan on delivery is made as inactive", "pass", true);
					System.out.println("Scan on delivery is made as inactive");
					
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.uncheck")))) {
					reportStep("Scan on delivery is already inactive", "pass", true);
					System.out.println("scanon delivery is already inactive");
				}
			}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))) {
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Scan on delivery is made as inactive", "pass", true);
					System.out.println("Scan on delivery is made as inactive");
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.uncheck")))) {
					reportStep("Scan on delivery is already inactive", "pass", true);
					System.out.println("scan on delivery is already inactive");
				}
				Vclas_Assignment_Navigate();

			// clicking Assignment list button
			driver.findElements(By.xpath(prop.getProperty("Vclas_Home.Assignment.List.Button"))).get(0).click();
		}
		 else {
			reportStep("Search textbox doesn't present", "fail", false);
		}
		
	
	}
	

	
	public void Nodes_Parameters_False_PickUp(String Position) throws InterruptedException{
		
		  String sheetName = "Vclas_Assignments";
    tdrow = excelUtils.testCaseRetrieve(refTestDataName, sheetName);
		
		
		// navigating to nodes screen
		anyClick("Home button ", By.xpath(prop.getProperty("Vclas_Home.Home.Button")));
		
		anyClick("Administration Tab", By.xpath(prop.getProperty("Vclas_Home.Administration.Button")));
		
		anyClick("Nodes menu", By.xpath(prop.getProperty("Vclas_Administration.Nodes.Button")));
		
		// verifying search textbox
		if (verifyElementExistReturn(
				By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox"))) == true) {
			Thread.sleep(2000);
			// sending fpos to search textbox
			sendKeys("Search textbox", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox")),Position);

			// clicking search button
			anyClick("Vclas Node screen search button click",By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Button")));

			//expanding the parameters 
			anyClick("Parameter expand button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Paramters.Expand.Button")));

			JavascriptExecutor js = (JavascriptExecutor) driver; 
            js.executeScript("window.scrollBy(0,800)");
                                       //making the Nodes scan on pickup as false
            //if(verifyElementExist("parent checkbox of nodes scan on pickup", By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Inherited.Checkbox.check")))) {
            if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Inherited.Checkbox.check")))) {
            driver.findElements(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Inherited.Checkbox.check"))).get(0).click();
                  Thread.sleep(3000);
                  System.out.println("parent if condition");
                         
                  
                  if (isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.uncheck")))) {
                         
                         
                         System.out.println("child if condition");
                         
                         reportStep("Nodes Scan on pickup is already inactive", "pass", true);
                         System.out.println("Nodes scan on pickup is already inactive");
                  }
                  
                  
                  
                  else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.check")))) {
                         System.out.println("child else if condition");
                         
                  driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.check"))).click();
                         Thread.sleep(3000);
                         anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
                         Thread.sleep(5000);
                         anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
                         reportStep(" Nodes Scan on pickup is made as inactive", "pass", true);
                         System.out.println(" Nodes Scan on pickup is made as inactive");
                         
                         
                         
                  }
                  
                  
            }else {
            if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.check")))) {
                         System.out.println("parent else child ..if condition");
                  driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.check"))).click();
                         Thread.sleep(3000);
                         anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
                         Thread.sleep(5000);
                         anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
                         reportStep("Nodes Scan on delivery is made as inactive", "pass", true);
                         System.out.println("Nodes Scan on delivery is made as inactive");
                  }else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.uncheck")))) {
                         System.out.println("parent else child else if condition");
                         reportStep("Nodes Scan on pickup is already inactive", "pass", true);
                         System.out.println("Nodes scan on pickup is already inactive");
                  }
            }
            
            

				
				//setting the  package scan on pickup value as inactive
							
			//making  package scan on pickup as false
			
			if(isElementPresent( By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Inherited.Checkbox.check")))) {
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Inherited.Checkbox.check"))).click();
				//if(verifyElementExist("scan on delivery value--check",By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))==true) {
				if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.check")))) {	
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.check"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Package scan on pickup is made as inactive", "pass", true);
					System.out.println("Package scan on pickup is made as inactive");
					
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.uncheck")))) {
					reportStep("Package scan on pickup is already inactive", "pass", true);
					System.out.println("Package scan on pickup is already inactive");
				}
			}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.check")))==true) {
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.check"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Package scan on pickup is made as inactive", "pass", true);
					System.out.println("Package scan on pickup is made as inactive");
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.uncheck")))) {
					reportStep("Package scan on pickup is already inactive", "pass", true);
					System.out.println("Package scan on pickup is already inactive");
				}
			Vclas_Assignment_Navigate();

			// clicking Assignment list button
			driver.findElements(By.xpath(prop.getProperty("Vclas_Home.Assignment.List.Button"))).get(0).click();
		}
		 else {
			reportStep("Search textbox doesn't present", "fail", false);
		}
		
	
	}
	
	//method to set node scan on pickup and package scan on pickup as true
	
	public void Nodes_Parameters_True_PickUp(String Position) throws InterruptedException{
		
		  String sheetName = "Vclas_Assignments";
  tdrow = excelUtils.testCaseRetrieve(refTestDataName, sheetName);
		
		
		// navigating to nodes screen
		anyClick("Home button ", By.xpath(prop.getProperty("Vclas_Home.Home.Button")));
		
		anyClick("Administration Tab", By.xpath(prop.getProperty("Vclas_Home.Administration.Button")));
		
		anyClick("Nodes menu", By.xpath(prop.getProperty("Vclas_Administration.Nodes.Button")));
		
		// verifying search textbox
		if (verifyElementExistReturn(
				By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox"))) == true) {
			Thread.sleep(2000);
			// sending fpos to search textbox
			sendKeys("Search textbox", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox")),Position);

			// clicking search button
			anyClick("Vclas Node screen search button click",By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Button")));

			//expanding the parameters 
			anyClick("Parameter expand button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Paramters.Expand.Button")));

			JavascriptExecutor js = (JavascriptExecutor) driver; 
          js.executeScript("window.scrollBy(0,800)");
                                     //making the Nodes scan on pickup as false
          //if(verifyElementExist("parent checkbox of nodes scan on pickup", By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Inherited.Checkbox.check")))) {
          if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Inherited.Checkbox.check")))) {
          driver.findElements(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Inherited.Checkbox.check"))).get(0).click();
                Thread.sleep(3000);
                System.out.println("parent if condition");
                       
                
                if (isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.check")))) {
                       
                       
                       System.out.println("child if condition");
                       
                       reportStep("Nodes Scan on pickup is already active", "pass", true);
                       System.out.println("Nodes scan on pickup is already active");
                }
                
                
                
                else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.uncheck")))) {
                       System.out.println("child else if condition");
                       
                driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.uncheck"))).click();
                       Thread.sleep(3000);
                       anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
                       Thread.sleep(5000);
                       anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
                       reportStep(" Nodes Scan on pickup is made as active", "pass", true);
                       System.out.println(" Nodes Scan on pickup is made as active");
                       
                       
                       
                }
                
                
          }else {
          if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.uncheck")))) {
                       System.out.println("parent else child ..if condition");
                driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.uncheck"))).click();
                       Thread.sleep(3000);
                       anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
                       Thread.sleep(5000);
                       anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
                       reportStep("Nodes Scan on delivery is made as inactive", "pass", true);
                       System.out.println("Nodes Scan on delivery is made as inactive");
                }else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnPickup.Value.Checkbox.check")))) {
                       System.out.println("parent else child else if condition");
                       reportStep("Nodes Scan on pickup is already active", "pass", true);
                       System.out.println("Nodes scan on pickup is already active");
                }
          }
          
          

				
				//setting the  package scan on pickup value as inactive
							
			//making  package scan on pickup as false
			
			if(isElementPresent( By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Inherited.Checkbox.check")))) {
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Inherited.Checkbox.check"))).click();
				//if(verifyElementExist("scan on delivery value--check",By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))==true) {
				if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.uncheck")))) {	
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.uncheck"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Package scan on pickup is made as active", "pass", true);
					System.out.println("Package scan on pickup is made as active");
					
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.check")))) {
					reportStep("Package scan on pickup is already active", "pass", true);
					System.out.println("Package scan on pickup is already active");
				}
			}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.uncheck")))==true) {
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.uncheck"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Package scan on pickup is made as active", "pass", true);
					System.out.println("Package scan on pickup is made as active");
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.PackageScanOnPickup.Value.Checkbox.check")))) {
					reportStep("Package scan on pickup is already active", "pass", true);
					System.out.println("Package scan on pickup is already active");
				}
			Vclas_Assignment_Navigate();

			// clicking Assignment list button
			driver.findElements(By.xpath(prop.getProperty("Vclas_Home.Assignment.List.Button"))).get(0).click();
		}
		 else {
			reportStep("Search textbox doesn't present", "fail", false);
		}
		
	
	}

	//method to set node scan on delivery and scan on delivery as true
	
	public void Nodes_Parameters_True_Delivery(String Position) throws InterruptedException{
		
		  String sheetName = "Vclas_Assignments";
    tdrow = excelUtils.testCaseRetrieve(refTestDataName, sheetName);
		
		
		// navigating to nodes screen
		anyClick("Home button ", By.xpath(prop.getProperty("Vclas_Home.Home.Button")));
		
		anyClick("Administration Tab", By.xpath(prop.getProperty("Vclas_Home.Administration.Button")));
		
		anyClick("Nodes menu", By.xpath(prop.getProperty("Vclas_Administration.Nodes.Button")));
		
		// verifying search textbox
		if (verifyElementExistReturn(
				By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox"))) == true) {
			Thread.sleep(2000);
			// sending fpos to search textbox
			sendKeys("Search textbox", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Textbox")),Position);

			// clicking search button
			anyClick("Vclas Node screen search button click",By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Search.Button")));

			//expanding the parameters 
			anyClick("Parameter expand button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Paramters.Expand.Button")));

			//making the Nodes scan on pickup as false
			if(isElementPresent( By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodeScanOnDelivery.Inherited.Checkbox.check")))) {
				driver.findElements(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodeScanOnDelivery.Inherited.Checkbox.check"))).get(0).click();
				Thread.sleep(3000);
				if (isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.check")))) {
					reportStep("Nodes Scan on delivery is already active", "pass", true);
					System.out.println("Nodes scan on delivery is already active");
				}
				
				
				
				else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.uncheck")))) {
					
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.uncheck"))).click();
					Thread.sleep(3000);
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep(" Nodes Scan on delivery is made as active", "pass", true);
					System.out.println(" Nodes Scan on delivery is made as active");
					
				}
				
			}else {
				if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.uncheck")))) {
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.uncheck"))).click();
					Thread.sleep(3000);
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Nodes Scan on delivery is made as active", "pass", true);
					System.out.println("Nodes Scan on delivery is made as active");
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.NodesScanOnDelivery.Value.Checkbox.check")))) {
					reportStep("Nodes Scan on delivery is already active", "pass", true);
					System.out.println("Nodes scan on delivery is already active");
				}
			}
			

			
				
				//setting the  scan on delivery value as inactive
							
			//making  scan on delivery as false
			
			if(isElementPresent( By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Inherited.Checkbox.check")))) {
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Inherited.Checkbox.check"))).click();
				//if(verifyElementExist("scan on delivery value--check",By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))==true) {
				if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.uncheck")))) {	
				driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.uncheck"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Scan on delivery is made as active", "pass", true);
					System.out.println("Scan on delivery is made as active");
					
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))) {
					reportStep("Scan on delivery is already active", "pass", true);
					System.out.println("scanon delivery is already active");
				}
			}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.uncheck")))) {
					driver.findElement(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.uncheck"))).click();
					anyClick("Save button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					Thread.sleep(5000);
					anyClick("Refresh button", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Refresh.Button")));
					reportStep("Scan on delivery is made as inactive", "pass", true);
					System.out.println("Scan on delivery is made as inactive");
				}else if(isElementPresent(By.xpath(prop.getProperty("Vclas_Administration.Nodes.ScanOnDelivery.Value.Checkbox.check")))) {
					reportStep("Scan on delivery is already active", "pass", true);
					System.out.println("scan on delivery is already active");
				}
				Vclas_Assignment_Navigate();

			// clicking Assignment list button
			driver.findElements(By.xpath(prop.getProperty("Vclas_Home.Assignment.List.Button"))).get(0).click();
		}
		 else {
			reportStep("Search textbox doesn't present", "fail", false);
		}
		
	
	}	
	

	public void Tugger_Movebutton_3463(String flowName) throws InterruptedException {
		
		String strSeqno, seqHandling, strTempSeqNo;
		int Seqno, racksCount, iTempSeqNo, strLopNo;
		String rackStatus, strFlowType;
		String[] strType;
		int j=0;
		
		String sheetName = "Vclas_Assignments";
		tdrow = excelUtils.testCaseRetrieve(refTestDataName, sheetName);
		Seqno = Integer.parseInt(tdrow.get("Object")); // 1
		racksCount = Integer.parseInt(tdrow.get("No_of_Racks"));
		strFlowType = tdrow.get("Sequence_Type");
		strType = tdrow.get("Shortage_Type_Expected").split(",");
		strLopNo = Integer.parseInt(tdrow.get("Lopnr"));
		
		Vclas_Assignment vclasAgn = new Vclas_Assignment();
		
		// clicking Vclas Home button
		 anyClick("Vclas Home button click", By.xpath(prop.getProperty("Vclas_Home.Home.Button")));

		// clicking on the Administration button
		anyClick("Vclas Administration button click", By.xpath(prop.getProperty("Vclas_Home.Administration.Button")));

		// clicking on Nodes button
		anyClick("Vclas Nodes button click", By.xpath(prop.getProperty("Vclas_Administration.Nodes.Button")));
		
		//Clicking on Tugger Node
		anyClick("Tugger child node click", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Tugger.Childnode")));
		
		//Clicking on parameters button expansion
		anyClick("Parameters expansion button click", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Tugger.Paramters.Expand.Button")));
				
				
			if(verifyElementExistReturn(By.xpath(prop.getProperty("Vclas_Administration.Nodes.MoveButton.Checkbox.Cancel")))) {
					
				//Clicking on save button
				anyClick("Save button click", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
					
			}else {
				
				//Clicking on use move button to active, to enable yellow button in scanning window of Tugger order
				driver.findElements(By.xpath(prop.getProperty("Vclas_Administration.Nodes.MoveButton.Inherited.Checkbox"))).get(0).click();
				Thread.sleep(4000);
				
				driver.findElements(By.xpath(prop.getProperty("Vclas_Administration.Nodes.MoveButton.Value.Checkbox"))).get(0).click();
				Thread.sleep(4000);
				
				//Clicking on save button in nodes screen
				anyClick("Save button click", By.xpath(prop.getProperty("Vclas_Home.Administration.Nodes.Save.Button")));
				}
			
			vclasAgn.Vclas_Assignment_Navigate();

			// Navigation to Shortage
			anyClick("Shortage Screen", By.xpath(prop.getProperty("Vclas_Home.Shortage.Button")));
			Thread.sleep(5000);

			if (flowName.equalsIgnoreCase("Tugger")) {
				strSeqno = String.valueOf(Seqno);// 01
				seqHandling = " ";
				
			} else {

				strSeqno = tdrow.get("Object");// 0001
				System.out.println("seq no :" + strSeqno);
				seqHandling = StringUtils.repeat("0", 4 - (String.valueOf(Seqno).length())); // 000
				System.out.println("seq handling:" + seqHandling);
			}

			System.out.println("flow name : " + flowName);

			vclasAgn.Vclas_Shortage_Search(strFlowType, strSeqno, flowName);

			iTempSeqNo = Seqno;

			System.out.println("seq no:" + iTempSeqNo);
			
			// To Verify Initial Status in Shortage Screen for created racks
			int i = 0;
			if (i <= racksCount) {
				rackStatus = "Planned";
			} else {
				rackStatus = "Active";
				
			}
			List<String> listr = vt.getList();
		System.out.println("full TASK ID: " +listr);
		
		String retval = listr.get(0);
		System.out.println("retval:" +retval);
		
		List<String> listrempty = vt.getList1();
		System.out.println("empty TASK ID: " +listrempty);
		
		String retvalempty = listrempty.get(0);
		System.out.println("retval:" +retvalempty);
		
		
			// To verify the Full task id
			vclasAgn.Vclas_Shortage_TypeVerify(strFlowType + seqHandling + String.valueOf(iTempSeqNo),
					listr.get(j).toString(), strType[0].toUpperCase(), rackStatus);

			// To Verify the Empty task id
			vclasAgn.Vclas_Shortage_TypeVerify(strFlowType + seqHandling + String.valueOf(iTempSeqNo),
					listrempty.get(j).toString(), strType[1].toUpperCase(), "Active");
			 
			vclasAgn.Vclas_Shortage_PositionVerify(strFlowType + seqHandling + String.valueOf(iTempSeqNo), listr.get(j).toString(), "UNKNOWN");
                j++;   //new
			// Sequence No
			iTempSeqNo = iTempSeqNo + 1;

			if (iTempSeqNo == 100) {
				iTempSeqNo = 1;
			}

			iTempSeqNo = Seqno;
			System.out.println("itemp val:" + iTempSeqNo);
			int lastSubmittedFullRack = 0;
			
			// Click the Planned Activity in Assignment List for Initial Two Full Racks
			reportStep("--------Full rack submission: #B" + strFlowType + " " + String.valueOf(iTempSeqNo)
						+ "#C Initiated----", "Info", false);
				// Submitting the Rack
				System.out.println(
						"rck submsn" + strType[0] + " " + strFlowType + seqHandling + String.valueOf(iTempSeqNo));
				if (flowName.equalsIgnoreCase("Tugger")) {

					vclasAgn.Vclas_AssignmentList_RackSubmission_Tugger(strType[0],
							strFlowType + seqHandling + String.valueOf(iTempSeqNo));
				} else {
					vclasAgn.Vclas_AssignmentList_RackSubmission_JISJIT(strType[0].toUpperCase(),
							strFlowType + seqHandling + String.valueOf(iTempSeqNo));
				}

				lastSubmittedFullRack = iTempSeqNo;

				if (flowName.equalsIgnoreCase("Tugger")) {
					if (String.valueOf(iTempSeqNo).length() == 1) {
						strTempSeqNo = "0" + String.valueOf(iTempSeqNo);
						System.out.println(strTempSeqNo);
					} else {
						strTempSeqNo = String.valueOf(iTempSeqNo);
						System.out.println(strTempSeqNo);
					}

					// Calculate LopNo for ScanId
					strLopNo = strLopNo + Integer.parseInt(tdrow.get("Line_movement"));
					System.out.println("strLopNo:" + strLopNo);

					// Calculate Scan Id
					String scanId = "T" + strFlowType + strTempSeqNo + strLopNo;
					System.out.println("scan id" + scanId);
					System.out.println(strFlowType + seqHandling + String.valueOf(iTempSeqNo) + "ok click ");
					vclasAgn.Vclas_AssignmentList(scanId, "Full_Rack", "No", flowName);

				} else {

					strTempSeqNo = StringUtils.repeat("0", 4 - String.valueOf(iTempSeqNo).length()) + iTempSeqNo;
					System.out.println("strTempSeqNo:" + strTempSeqNo);

					// Calculate Scan Id
					String scanId = strFlowType + strTempSeqNo;
					System.out.println("scan id" + scanId);
					vclasAgn.Vclas_AssignmentList(scanId, "Full_Rack", "No", flowName);
				}

				reportStep("--------Full rack submission: #B" + strFlowType + " " + String.valueOf(iTempSeqNo)
						+ "#C Submission Completed----", "Info", false);

				// Sequence No
				iTempSeqNo = iTempSeqNo + 1;

				if (iTempSeqNo == 100) {
					iTempSeqNo = 1;
				}

			
	
	}
}
