package com.param.adt.common;

public interface ICommonConstants {
	String SUCCESS_CODE = "200";
	String SUCCESS = "success";
	String ERROR = "error";
	
	boolean TRUE = true;
	boolean FALSE = false;
	
	char ACTIVE = 'A';
	char INACTIVE = 'I';
	
	Object NULL = null;
	
	public Integer TYPE_INSURANCE = 2;
	public Integer TYPE_CREDIT_COMPANY = 3;
	
	String COMMON_SUCCESS="Added Successfully.";
	String COMMON_DELETE="Deleted Successfully.";
	String COMMON_UPDATE="Updated Successfully.";
	String ALREADY_EXIST="Already Exists.";
	String COMMON_APPROVED="Approved Successfully";
	String ALREADY_APPROVED="Already Approved";
	String STATUS_CHANGED="Status Changed Successfully";
	String COMMON_ERROR="Fail";
	String COMMON_ERROR_MESSAGE="Something went wrong !!";
	String DISCHARGE="for this patient already initiated discharge by other user ";
	String ALREADY_EXIST_CODE = "409";
	Integer DIAGNOSIS_TEMPLATE = 1;
	Integer LAB_TEMPLATE = 2;
	Integer PRESCRIPTION_TEMPLATE = 3;
	Integer ADVICE_TEMPLATE = 4;
	/* MESSAGE FOR NON REGISTERED PATIENTS */	
	
	boolean UkFlag = true;
	
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
}
