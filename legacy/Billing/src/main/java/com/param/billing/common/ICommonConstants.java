package com.param.billing.common;

public interface ICommonConstants {
	String SUCCESS = "success";
	String SUCCESS_CODE = "200";
	String ALREADY_EXIST_CODE = "409";
	String SUCCESS_MESSAGE = "Added Successfully.";
	String ERROR_MESSAGE = "Failed to add data.";
	String ERROR = "error";
	public static final String ERROR_CODE = "500";
	boolean TRUE = true;
	boolean FALSE = false;
	
	char ACTIVE = 'A';
	char INACTIVE = 'I';
	
	Object NULL = null;
	
	public Integer TYPE_INSURANCE = 2;
	public Integer TYPE_CREDIT_COMPANY = 3;
	
	/*MESSAGE ON JSP*/
	String COMMON_SUCCESS="Added Successfully.";
	String COMMON_DELETE="Deleted Successfully.";
	String COMMON_UPDATE="Updated Successfully.";
	String ALREADY_EXIST="Already Exists.";
	String COMMON_APPROVED="Approved Successfully";
	String ALREADY_APPROVED="Already Approved";
	String COMMON_ERROR="Fail";
	String COMMON_ERROR_CODE="501";
	String COMMON_ERROR_MESSAGE="Something went wrong!!";
	
	String COMMON_STATUS_SUCCESS="Status changed Successfully.";
	String COMMON_STATUS_FAIL="Failed To Change Status.";

	
	String PACKAGE_TYPE_ERROR = "Package Type List Not Found";
	
	String SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE = "Service available in active packages";
	String SERVICE_NOT_AVAILABLE_IN_ACTIVE_PACKAGE = "Service Not available in active packages";
	String PERIODICITY_CREATED_SUCCESS_MSG = "Periodicity Created Successfully";
	String PERIODICITY_SERV_ORDERED = "Periodicity Services Ordered";
	String PROCEDURE_ORDERED = "Procedure services ordered";
	String INVOICE_SETTLED = "Invoices Settled";
	String NOT_FOUND = "Not Found";
	String SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE_AS_INC = "Service available in active packages as inclusion service";
	String SERVICE_NOT_AVAILABLE_IN_ACTIVE_PACKAGE_AS_EXC = "Service Not available in active packages as defined in exclusion service";
	String PACKAGE_VALIDITY_EXPIRED = "Package Validty expired";
	String NOT_FOUND_CODE = "404";
	
	
	//Visit Type Constant
	public Integer OP = 1;
	public Integer IP = 2;
	public Integer DC = 3;
	public Integer ER = 4;
	
	//Package Type Constant
	public Integer PACKAGE1=1; //EHCPACKAGE
	public Integer PACKAGE2=2; //EITHERORPACKAGE
	public Integer PACKAGE3=3; //PACKAGEWITHCAP
	public Integer PACKAGE4=4; //ALLINCLUSIVEPACKAGE
	public Integer PACKAGE5=5; //MULTIENCOUNTERPACKAGE
	
	//Package Type Constant
			public Integer SELF=1; //SELF
			public Integer INSURANCE=2; //INSURANCE
			public Integer COMPANY=3; //COMPANY
			
			//Appointment Type Id Constant
			public Integer APPOINTMENTTYPEID1=1; 
			public Integer APPOINTMENTTYPEID2=2; 
			public Integer APPOINTMENTTYPEID3=3; 
			public Integer APPOINTMENTTYPEID5=5;
			public Integer APPOINTMENTTYPEID7=7;
			public Integer APPOINTMENTTYPEID6=6;
			public Integer APPOINTMENTTYPEID8=8;
			public Integer APPOINTMENTTYPEID9 =9;
			public Integer APPOINTMENTTYPEID10=10; 
			
			
			//Appointmenent status id 
			public Integer APPOINTMENTSTATUSID0=0;
			public Integer APPOINTMENTSTATUSID1=1; 
			public Integer APPOINTMENTSTATUSID2=2; 
			public Integer APPOINTMENTSTATUSID3=3; 
			public Integer APPOINTMENTSTATUSID4 =4;
			public Integer APPOINTMENTSTATUSID5=5;
			public Integer APPOINTMENTSTATUSID6=6;
			public Integer APPOINTMENTSTATUSID7=7;
			public Integer APPOINTMENTSTATUSID8=8;
}
