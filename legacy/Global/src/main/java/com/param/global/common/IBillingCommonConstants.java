package com.param.global.common;

public interface IBillingCommonConstants {
	//--------Billing Group Master------------//
			String BILLING_GROUP_DESC_ALREADY_EXIST = "Billing Group Description Already Exist.";
			
 //Deposit Status			
   Integer DEPOSIT_PENDING = 1;
   Integer DEPOSIT_PARTIAL_COLLECTED = 2;
   Integer DEPOSIT_COLLECTED = 3;
   
   
   Integer depositScreenId = 123;
   Integer refundScreenId = 124;
   Integer refundAgainstDepositScreen = 125;
   Integer creditNoteScreen = 126;
   Integer ambulanceLPOScreenId = 147;
   Integer ambulanceBillScreenId = 148;
   
   //patient UHID
   Integer PRE_REGESTRATION_UHID = 136;
   Integer EMERGENCY_PATIENT_UHID = 135;
   Integer OTC_PATIENT_UHID = 134;
   Integer NORMAL_PATIENT_UHID = 133;
   Integer DEATH_REGESTRATION_UHID = 154;
   
   Integer ORDER_NUMBER = 140;
   Integer BILL_NUMBER = 141;
   Integer DEBIT_NOTE_NUMBER = 142;
   Integer RECIEPT_NUMBER = 143;
   Integer DEPOSIT_CONSUMPTION_NUMBER=144;
   Integer INTRIM_BILL_NUMBER = 153;
   
   Integer DONORNUMBER = 146;
   Integer BABY_PATIENT_UHID = 152;
   Integer BLOOD_BAG_ISSUE_NUMBER = 149;
   Integer BLOOD_BAG_NUMBER = 150;
   Integer REQUISITION_NUMBER=151;
   Integer BABY_PATIENT_UHID_STILL = 155;
   Integer COMPANY_INVOICE_NUMBER = 156;
   
   Integer ENCOUNTER = 157;
   Integer ADMISSION = 158;
   Integer CALL_LOG_NUMBER = 159;
   Integer PRES_NO = 164;
   Integer MIS_EXP = 176;
   Integer OT_RESERVATION_NO = 165;
   
   Integer OT_SHEET_NO = 166;
   Integer SANCTION_REQUEST_NUMBER = 169;
   Integer PREAUTH_REQUEST_NUMBER= 170;
   
   Integer INCIDENT_NO = 173;
   Integer WORKORDER_RECIEPT_NUMBER = 172;
   Integer BLOOD_BAG_LOAN_OUT_NO = 174;
   
   Integer COMPANY_RECEIPTS = 175;
   Integer SAMPLE_NUMBER = 179;
   Integer ERENCOUNTER = 180;
   
   
   Integer OP_CASH_BILL_SEQUENCE = 181;
   Integer OP_INSURANCE_BILL_SEQUENCE = 182;
   Integer OP_CORPORATE_BILL_SEQUENCE = 183;
   Integer IP_CASH_BILL_SEQUENCE = 184;
   Integer IP_INSURANCE_BILL_SEQUENCE = 185;
   Integer IP_CORPORATE_BILL_SEQUENCE = 186;
   Integer ITEM_MASTER = 188;
   Integer MRD_FILE_ALLOCATION = 189;
   Integer MRD_FILE_ALLOCATION_REQUEST_NO = 190;
   Integer MRD_FILE_ALLOCATION_ISSUE_NO = 191;
   Integer SPECIMEN_NUMBER =192;
   Integer NABH_NUMBER =193;

}
