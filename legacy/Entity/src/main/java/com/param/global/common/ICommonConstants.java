package com.param.global.common;

public interface ICommonConstants {
	String SUCCESS = "success";
	String SUCCESS_CODE = "200";
	String SUCCESS_MESSAGE = "Added Successfully.";
	String ERROR = "error";
	String TALLY_SUCCESS = "Data Fetched Successfully";
	String TALLY_NOT_FOUND = "No Data Found";
	String TALLY_ERROR = "ERROR While Fetching Data";
	String Ledger_Not_Found = "Ledger Not Found In Tally";
	String REMOVE = "Removed Successfully.";
	public static final String ERROR_CODE = "500";
	
	public static final String SERVER_ERROR = "Server error";
	public static final String SERVER_ERROR_CODE = "500";
	
	boolean TRUE = true;
	boolean FALSE = false;
	
	char ACTIVE = 'A';
	char INACTIVE = 'I';
	char REMOVED = 'D';
	
	char YES = 'Y';
	char NO = 'N';
	
	String NOT_APPLICABLE = "NA";
	
	Object NULL = null;
	
	public Integer TYPE_INSURANCE = 2;
	public Integer TYPE_CREDIT_COMPANY = 3;
	
	/*MESSAGE ON JSP*/
	String COMMON_SUCCESS="Added Successfully.";
	String COMMON_STATUS_SUCCESS="Status changed Successfully.";
	String COMMON_DELETE="Deleted Successfully.";
	String COMMON_UPDATE="Updated Successfully.";
	String ALREADY_EXIST="Already Exists.";
	String ALREADY_EXIST_LOYALTY = "Only one entry is valid in Loyalty Card Configuration"; 
	String COMMON_APPROVED="Approved Successfully";
	String ALREADY_APPROVED="Already Approved";
	String COMMON_ERROR="Fail";
	String COMMON_ERROR_CODE="501";
	String COMMON_ERROR_MESSAGE="Something went wrong!!";
	String ALREADY_EXIST_CODE = "409";
	String NOT_FOUND_CODE = "404";
	
	//---------Global Service Master----------//
		String SERVICE_NAME_ALREADY_EXIST = "Service Name Already Exist.";
	
	//billing codes and messages
	String BILLING_ERROR_CODE = "600";
	String SERVICE_ERROR_CODE = "700";
	String DEPOSIT_ADDED_MSG = "Amount deposited successfully your deposit no is ";
	String RECHARGE_ADDED_MSG = "Amount Added to Card Successfully..";
	
	String PAYMENT_COLLECTED = "Payment collected successfully";
	String EHC_PACKAGE_CREATED_MSG = "Ehc Package Created";
	String EITHER_OR_PACKAGE_CREATED = "Either Or Package Created";
	String PACKAGE_WITH_CAP_CREATED = "Package With Cap Created";
	String MULTIENCOUNTER_PACKAGE_CREATED = "Multiencounter Package Created";
	String ALL_EXCLUSIVE_PACKAGE_CREATED = "All Exclusive Package Created";
	
	String ALL_INCLUSIVE_PACKAGE_CREATED = "All Inclusive Package Created";
	
	String COMPANY_CONTRACT_CREATED = "Company Contract Created";
	String CHARGE_CONVERSION_SAVE = "Charge conversion added successfully";
	String PACKAGE_NOT_FOUND = "Package Not Found";
	String DOCTOR_SHARE = "Added doctor share successfully";
	
	String DOCTOR_REFERRAL_SHARE = "Added doctor referral share successfully";
	
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

	String ORDER_SET_CREATED = "Order set created";
	String NO_DATA_FOUND = "No data found";
	String SERVICE_NOT_AVAILABLE_IN_ACTIVE_CONTACRT = "Service Not available in active contarct";
	String EMAIL_ID_NOT_EXIST="Please enter valid EmailId";
	String EMAIL_SENT="Mail Sent successfully";
	
	String INVALID_OTP="Invalid OTP, Please try again";
	String OTP_EXPIRED="Your OTP is Expired ,Please Generate Again";
	String PASSWORD_UPDATED="Password Updated Successfully";
	String OTP_VERIFIED="OTP Verified Successfully";
	String PASSWORD_MATCH_FOUND="New Password should not be same as current password";
	String PWD_MATCH_NOT_FOUND="Password and confirm password does not match";
	String EXISTING_PWD_MATCH="Password should be same as existing Password";
	
	String PACKAGE_SERVICE_EXISTS ="Service is Already is used as Another Package."; 
	String ALREADY_EXIT_TEXTUAL_RANGE_CODE = "Textual Range Code is Already Exist.";
	String ALREADY_EXIT_TEXTUAL_RANGE_DESC = "Textual Range Description is Already Exist.";
	String ALREADY_EXIT_BIOCHEMICAL_TEST_CODE = " Biochemical Test Code Already Exist. ";
	String ALREADY_EXIT_BIOCHEMICAL_TEST_DESC = " Biochemical Test Name Already Exist. ";
	Integer OTP_GENERATED =1;
	Integer OTP_VALIDATED = 2;
	Integer OTP_TIME_OUT =3;
	Integer OTP_INVALID = 4;
	String PROCEDURE_CANCEL_MESSAGE = "Procedure Cancelled successfully.";
	
	String ACCOUNT_SID  = "AC1b056c5d7821135457812a1cc652a1d3";
	String AUTH_TOKEN  = "be6d9abda4028e90096aea65a627450e";
	String FromNumber  = "whatsapp:+14155238886";
	
	Integer SendMail  = 1;
	Integer SendWhatsapp  = 2;
	Integer SendTextMessage  = 3;
	Integer RetryAttemps = 3;
	Integer LastRetryAttemps = 1;
	Integer BulkTrigger = 40;
	Integer MailTrigger = 30;
	String RPTFILLISSUE = "RPF file Not generated";
	String MAILNOTSEND = "Mail not send";
	String MAILSENT = "Mail send  successfully";
	String PDFURLISSUE = "Pdf Generation Url not found";
	String WHATSAPPNOTSENT = "whatsapp not send";
	String WHATSAPPSENT = "whatsapp send  successfully";
	String TEXTMSGSENT = "Text SMS send  successfully";
	String TEXTMSGNOTSENT = "Text SMS not send";
	String SENDERIDISSUE = "Sender Id not found";
	String EMAIL_NOT_MAPPED_TO_RECEIVED_OTP="Email Not Mapped To Received OTP";
	String OTP_LIMIT_EXHAUST="OTP Limit Exhaust Please try after 24 hours";
	String WAIT_FOR_THREE_MINUEST="Wait for  3 minutes ";
	
	Integer EVENT_1 = 1;
	
	Integer EVENT_2 = 2;
	
	Integer EVENT_3 = 3;
	
	Integer EVENT_4 = 4;
	
	Integer EVENT_5 = 5;
	
	Integer EVENT_6 = 6;
	
	Integer EVENT_7 = 7;
	
	Integer EVENT_8 = 8;
	
	Integer EVENT_9 = 9;
	
	Integer EVENT_10 = 10;
	
	
	
	/*String GOOGLE_TRANSLATE =
			 "<div id=\"google_translate_element\"> <h2 class=\"notranslate\" style=\"margin-bottom: 5px;transition: none;text-align: center;\">"
					 + "Select Language</h2> </div> <script type=\"text/javascript\"> function googleTranslateElementInit() {  new google.translate.TranslateElement({pageLanguage: 'en', includedLanguages:'af,sq,am,ar,hy,az,eu,be,bn,bs,bg,ca,ceb,ny,zh-CN,zh-TW,co,hr,cs,da,nl,en,eo,et,tl,fi,fr,fy,gl,ka,de,el,gu,ht,ha,haw,iw,hi,hmn,hu,is,ig,id,ga,it,ja,jw,kn,kk,km,rw,ko,ku,ky,lo,la,lv,lt,lb,mk,mg,ms,ml,mt,mi,mr,mn,my,ne,no,or,ps,fa,pl,pt,pa,ro,ru,sm,gd,sr,st,sn,sd,si,sk,sl,so,es,su,sw,sv,tg,ta,tt,te,th,tr,tk,uk,ur,ug,uz,vi,cy,xh,yi,yo,zu', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false}, 'google_translate_element');"
					 + "}</script> <style> .page {text-align: justify; } .goog-te-banner-frame.skiptranslate {display: none!important;}   body {top: 0px!important; text-align: -webkit-left; overflow:auto !important; }  body-main{top:0px !important;} .goog-te-gadget img {vertical-align: middle; border: none;display: none;}  .goog-te-gadget-simple .goog-te-menu-value {    color: #000;     font-size: 16px;     font-weight: 700; } .goog-te-menu-"
					 + "valuespan:last-child {float: right !important } .goog-te-gadget-simple { min-width: 150px; } .goog-te-combo, .goog-te-banner , .goog-te-ftab , .goog-te-menu , .goog-te-menu2 , .goog-te-balloon * {    font-family: arial;     font-size: 14pt; } "
					 + "#binaryfile1 "
					 + "{  display: none; "
					 + "} .skiptranslate.goog-te-gadget{\r\n" + 
					 " margin-left: 41%;\r\n" + 
					 " }"
					 + "</style> <script type=\"text/javascript\" src=\"//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>";*/

	String GOOGLE_TRANSLATE =
			"<div id=\"google_translate_element\" class=\"translate-container\">"
					+ "<h2 class=\"notranslate translate-heading\">Select Language</h2>"
					+ "</div>"
					+ "<script type=\"text/javascript\">"
					+ "function googleTranslateElementInit() {"
					+ "new google.translate.TranslateElement({"
					+ "pageLanguage: 'en',"
					+ "includedLanguages:'af,sq,am,ar,hy,az,eu,be,bn,bs,bg,ca,ceb,ny,zh-CN,zh-TW,co,hr,cs,da,nl,en,eo,et,tl,fi,fr,fy,gl,ka,de,el,gu,ht,ha,haw,iw,hi,hmn,hu,is,ig,id,ga,it,ja,jw,kn,kk,km,rw,ko,ku,ky,lo,la,lv,lt,lb,mk,mg,ms,ml,mt,mi,mr,mn,my,ne,no,or,ps,fa,pl,pt,pa,ro,ru,sm,gd,sr,st,sn,sd,si,sk,sl,so,es,su,sw,sv,tg,ta,tt,te,th,tr,tk,uk,ur,ug,uz,vi,cy,xh,yi,yo,zu',"
					+ "layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false}, 'google_translate_element');"
					+ "}"
					+ "</script>"
					+ "<style>"
					/* Hide unnecessary elements */
					+ ".goog-te-banner-frame.skiptranslate {display: none !important;}"
					+ "body {top: 0px !important; text-align: -webkit-left; overflow:auto !important;}"
					+ ".goog-te-gadget img {display: none;}"
					+ ".goog-te-gadget-simple .goog-te-menu-value {color: #000; font-size: 16px; font-weight: 700;}"
					+ ".goog-te-gadget-simple {min-width: 150px;}"

					/* Make language dropdown fully visible */
					+ ".goog-te-menu-frame {"
					+ "position: absolute !important;"
					+ "top: 50px !important;"
					+ "left: 0 !important;"
					+ "width: 100% !important;"
					+ "max-width: 350px !important;"
					+ "height: 500px !important;"
					+ "overflow: auto !important;"
					+ "z-index: 99999 !important;"
					+ "border: 1px solid #ccc !important;"
					+ "border-radius: 5px !important;"
					+ "box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.2) !important;"
					+ "}"

					/* Responsive adjustments */
					+ ".translate-container {"
					+ "display: flex; flex-direction: column; align-items: center; justify-content: center;"
					+ "width: 100%; max-width: 100px; margin: 0 auto;}"
					+ ".translate-heading {margin-bottom: 5px; transition: none; text-align: center; font-size: 18px;}"

					+ "@media screen and (max-width: 768px) {"
					+ ".translate-container {width: 50%;}"
					+ ".goog-te-menu-frame {max-width: 100% !important; height: 500px !important;}"
					+ "}"

					+ "</style>"
					+ "<script type=\"text/javascript\" src=\"//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>";





	String GOOGLE_TRANSLATE_DISC = "<div class=\"notranslate\" "
   +	"style=\"font-size: 12px;text-align: justify;position: fixed;left: 0;bottom: 0;width: 100%;text-align: center;"
	+ "\"><b>"
	+ "Disclaimer:</b>This CaseSheet has been translated for your convenience using automated translation software. No automated translation is perfect nor is it intended to replace human translators. We do not guarantee the accuracy of the translated text. Some pages may not be accurately translated due to the limitations of the translation software. Service Provider accepts no responsibility and disclaims any and all liability for damages that may occur due to outdated or incorrect translations. The"
	+ " use of this translate service shall be at the user's sole risk.</div>";

	String ID_NOT_EXISTS = "Record with given Id does not exists";

	String OPD = "opd";
	String IPD = "ipd";

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
		public Integer APPOINTMENTTYPEID9 =9;
		public Integer APPOINTMENTTYPEID8=8;
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


		//Subscription Type constants
		String MONTHLY="MONTHLY";
		String QUARTERLY="QUARTERLY";
		String HALF_YEARLY="HALF-YEARLY";
		String YEARLY="YEARLY";

	    String ERROR_SUBSCRIPTION = "SUB_5000";
	    String SUCCESS_SUBSCRIPTION1 = "SUB_5001";
	    String SUCCESS_SUBSCRIPTION15 = "SUB_5002";
		String SUBSCRIPTION_ACTIVE="ACTIVE";
		String SUBSCRIPTION_EXPIRED="EXPIRED";
		String SUBSCRIPTION_CANCELLED="CANCELLED";

		//Note: Never change this key
		//String ENCRYPTIONKEY = "sdfsfd345dfg345345";
		String ENCRYPTIONKEY = "ssshhhhhhhhhhh!!!!";
		
		
		public Integer UNREAD = 1;
		public Integer READ = 2;

		public String publikKeyForEncryption = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyqnkfveWPGIrafL0irZeg4xZI/9DtdaWkxOiXhIp1RWtsgzDj3zSTNCEfr6s1YJCJImmRI/+lDzlv/9Y0nHyMM/7FfeYllQ6FGGGL/08vn/2Pyfmf5DJpvbboV2qcqwUTax1Ffwfx3Lgp8N1BTYciQG3C9fZJsT1jG7DDNk/N1UZYwgHyykKQR89OQC+J+fvxf8+qXWYTgnIR57Cofu6V5+mkVzXef6C49p9gSapI7ANd5/tOzVr1xg+g9f07r3GdZlF11XBZDJG9uRKlP1znY+LLFiFZT43trNRXk/ff05KXJIx21Ru/xq+BpsfXSZtdqurBbwxpapg67cqr0JknQIDAQAB";
		public String privateKeyForEncryption = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDKqeR+95Y8Yitp8vSKtl6DjFkj/0O11paTE6JeEinVFa2yDMOPfNJM0IR+vqzVgkIkiaZEj/6UPOW//1jScfIwz/sV95iWVDoUYYYv/Ty+f/Y/J+Z/kMmm9tuhXapyrBRNrHUV/B/HcuCnw3UFNhyJAbcL19kmxPWMbsMM2T83VRljCAfLKQpBHz05AL4n5+/F/z6pdZhOCchHnsKh+7pXn6aRXNd5/oLj2n2BJqkjsA13n+07NWvXGD6D1/TuvcZ1mUXXVcFkMkb25EqU/XOdj4ssWIVlPje2s1FeT99/TkpckjHbVG7/Gr4Gmx9dJm12q6sFvDGlqmDrtyqvQmSdAgMBAAECggEAIWsV51zevGrIs2IRcn0jhSgju9SYHvg5YJvMepweo0k9mN5Tcgn4aJu6uWaqO9PModdD4ErpRRrsnLYXuK367JMjGWp4UhOl9x3kwW2N8nDJ03y6HL9sjRhoO4nlNR8MgwlkzkECoYIfKvLHTVpwpqPugeiwIV05i1RpW+lH7BDUW/WBM6ngg9G4rqwwnI/mN4hJxDVokzCamoKCEeqqa7YBpy6AgZsPGCx4LI/gOuevrUXLh+L6OVQyrhqXW0HuRw3p9S9m+3SY1d1LkGG2IehFTcLnzbvTogKMV8BgOnREkWjmlEum2k+HZx6OnUejLcet2WHobFb5r/SnEeRpGQKBgQDaH8UOVkz7+N2p9XdM4ZJRaq0kFlie3XdbsC3ErYbkYibU0VyYRKehEcUzjmp2PETIiWDJuLYJ0nx3Q1ewPnDKadw3sejDYIFkw0I8apz76MuduI8lUYLTxE6QA4zUHsxC8OsDOWKCPrEl1dIodB0OZ0ccF/pVX09ufmX1IJAYqQKBgQDt2tyohPXTOJ8ukJgOxrjbSseilyjrMEOjyjjIRAumKdyWnjkMZiecsmROiYOItFX9H+k8TsdSzW7YoGSo64QjLKHFctOdUFQd7aEV9f6Ad9uI0jMe+ghJqXaICDsC7JANYstNVCm4com8Os5uXAjJN8wureXTJhNbxe9tP7rg1QKBgQCPhkYQqAKh7eBVPBpS8T5o6iuhSj7n0cv+/c8v0gqnblokwNoPq2Utm9bJlNVClWehGJwiP7MwhVq20w34/y0SU1O/JgaFwJODrOOBg5lmPIj2cAFoydiu5UXYhy18SgOXrJTHG1RRZVUFE0vkqHqSBq0ItFJZKIzIpM0L3XBTeQKBgBU9Ic76cGj3+V7TEoDfRcpvFvxxuvmlSaTqfb+EM/YWbtgUMMvmo0resw5AQTqHv9Xmb5EvB6VhKrC1QcfDn/z0E03qPvMhJSG3AldPq/YUvcmt21q8wNizBR6IfZx5OzO2yqiRB1itAZeJOL2qAi7ViKnyZT9BeyEKv6Jlum4xAoGAUEU6GEIgeccWnPTlrX3Sdzq1Gq7dGSPnIuvKZn1RGbqciye5uwzp+PhhXeEOE39EXOt6cNdrmb6KS0Aq2JR+eRGOXATUCfCYdgegTvSZhtf3jLsB5WO5Ed1kR0+rM/MZ/p5enUwE2hRG822j8f3zCXsQ+xBvZT1iK6uJyqWYgxk=";

}
