package com.param.global.common;

public interface IUserLoginConstants {

	 String INVALID_USER_CODE = "101";
	 String INVALID_USER_MSG = "Invalid Credentials";
	
	 String INVALID_USER_C = "101";
	 String INVALID_USER_M = "Username and Pasword Invalid";
	
	public String VALID_USER_CODE = "100";
	public String VALID_USER_MSG = "";
	
	String INVALID_PWD_CODE = "102";
	String INVALID_PWD_MSG = "Invalid Password";
	
	public String VALID_PWD_CODE = "103";
	public String VALID_PWD_MSG = "Login Successful";
	
	String INVALID_PWDEXPIRY_CODE = "104";
	String INVALID_PWDEXPIRY_MSG = "Password expired";
	
	String INVALID_USERSTATUS_CODE = "105";
	String INVALID_USERSTATUS_MSG = "Account Deactivated";
	
	public String INVALID_MAIL_ID_MSG = "This email-id is not registered in system \nplease check your email-id.";
	public String ERROR_UPDATION_PASSWORD = "Error occurred during updating password \nplease try after sometime.";
	public String PASSWORD_CHANGE_MAIL_SENT_SUCCESS = "Your password has been successfully changed \nplease check your mail inbox for new password.";
	public String DOCUMENT_MAIL_SENT_SUCCESS = "Document has been sent, \nplease check your mail.";
	
	public String UNAUTHORISED_CODE = "401";
	public String UNAUTHORISED_USER = "Unauthorised User.";
}
