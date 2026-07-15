package com.param.global.mail;

public interface IMailConstants {
	
	
	
	
/**Mail Send messages*/
	String PASSWORD_CHANGE_MAIL_SENT_SUCCESS = "Password Change Sucessfully.";
	

	//----------Mail constact---------------//
			String SMTP_HOST = "smtp.gmail.com";//"smtpout.secureserver.net";//"smtp.gmail.com";
			String SMTP_PORT = "465";//"465";
			String SMTP_USER_ID = "info@clinivantage.co.in";//"katkar.yogesh7350@gmail.com";

			public String MYLIFE_ANDROID_APP_LINK = "https://www.dropbox.com/s/xbhe7xxpprvmzq3/Athaayu%20V1.1.apk?dl=0";
			
			String MAIL_SUBJECT = "Password Reset";
			String MESSAGE = "You recently requested to reset your password for your Clinivantage account. "
							+ "\nBased on request we have successfully changed your password,  "
							+ "\nand your new password is :  ";
			String NOTE = "\n\nNote : Please don't forget to change your password once you logged in to your Clinivantage system from my profile section. "
						+ "\n\nRegards, "
						+ "\nClinivantage Team ";
			
			String PATIENT_NOTE = "\n\nNote : Please download MyLife App from below link \n"+MYLIFE_ANDROID_APP_LINK+" \nPlease don't forget to change your password once you logged in to your MyLife App from my profile section. "
					+ "\n\nRegards, "
					+ "\nClinivantage Team ";
			
			public String SALUTATION = "Hi ";
			
			public String PATIENT_REG_CONTENT =   "Dear Patrons,  "
												+ "\n<br>   "
												//+ "\n<br>Greetings from Team Athaayu.  "
												+ "\n<br>Greetings from Team Clinivantage.  "
												+ "\n<br>   "
												+ "\nWe take immense pride to be at your service every time your health needs to be taken care of. We thank you for having faith in us and our efforts to give you the best care.  "
												+ "\n<br>   "
												+ "\n<br>We would like to excel further in our services to you and be always available at your fingertips and provide you with a enriched healthcare experience.  "
												+ "\n<br>   "
												+ "\n<br>In order to provide you with a richer platform that takes the caregiving facilities to the next level, we�re now collaborating with the UK/Pune based- Clinivantage Healthcare Technologies Pvt Ltd. "
												+ "\n<br>"
												+ "\n<br>Clinivantage, with its strong expertise in powerfully deploying IT for medical services, will provide an unmatched technological edge by building an entire healthcare ecosystem on a secured, hi-tech platform. It will bring about a tectonic shift in the way  patients are treated and help doctors give a lot more quality time to listen better and treat better. "
												+ "\n<br>"
												+ "\n<br>To start this ecstatic state-of-the-art healthcare experience we are forwarding you the following link. Kindly open the link and register yourself on the same, in order to avail every healthcare facility most smoothly. Once you register you will get complete information about the app and benefits to you as patrons of Clinivantage.  " //Athaayu
												+ "\n<br>"
												+ "\n<br>We certainly look forward to getting connected to you through the 'MyLife' App and help you manage your health much easily.  "
												+ "\n<br>"
												+ "\n<br>Credentials for MyLife : ";
			
			public String MYLIFE_ANDROID_APP_LINK_CONTENT =  "\n\n<br><br> Link for MyLife application: " ;
					public String CLINI_REG_SALUTATION = "\n<br>  "
													     + "\n<br>Sincerely your  "
													    // + "\n<br>Team Athaayu  ";
													     + "\n<br>Team Clinivantage  ";
												
			public String DOCTOR_REG_CONTENT = "\nYour Registration as a Doctor is successful with Clinivantage.\nFollowing are the login credentials for Application.";
			public String EMPLOYEE_REG_CONTENT = "\nYour Registration is successful with Clinivantage.\nFurther are the login credentials for Application ";
			
			public String CHANGE_PASSWORD_NOTE = "Note:  Please don't forget to change your password once you logged in to your Clinivantage system from my profile section."
												+ "\n\nRegards, "
												+ "\nClinivantage Team ";
			public String USERNAME = "\nUsername: ";
			public String PASSWORD = "\nPassword: ";
			
			public String REG_SUBJECT = "Confirm Registration";
			
			String PASSWORD_PROTECTED_DOCUMENT_SUBJECT = "Patient Documents";
			public String PASSWORD_PROTECTED_DOCUMENT_MESSAGE = "\n follwing are the list of documents: \n";
			public String OTP_MAIL_CONTENT = "Dear Patron,  "
					+ "\n<br>   "
					+ "\n<br>Greetings from Team Clinivantage.  "
					+ "\n<br>   "
					+ "\n<br>OTP for reset password is: ";
							
			public String OTP_SUBJECT="OTP Verification";
			public String RESEND_USERNAME_SUBJECT="UserName";
			public String USERNAME_MAIL_CONTENT= "Dear Patron,  "
					+ "\n<br>   "
					+ "\n<br>Greetings from Team Clinivantage.  "
					+ "\n<br>   "
					+ "\n<br>Username is: ";
			public String RESEND_USERNAME_AND_PWD_SUBJECT = " Username and Password";
			public String PWD_CONTENT="Your Password is:";
			
}
