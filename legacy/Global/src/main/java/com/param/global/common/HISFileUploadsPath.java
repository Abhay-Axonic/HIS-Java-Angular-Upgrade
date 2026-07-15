package com.param.global.common;

public interface HISFileUploadsPath {

  /*public String ACCESS_KEY = "AKIAJQ556GTD7R4TNW2A";
  public String SECRET_KEY = "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";
  public String BUCKET_NAME = "his-test";*/
  
	//public String ACCESS_KEY = "AKIAZK2N6OMGE3XYMB23";
	public String SECRET_KEY = "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";
	public String ACCESS_KEY = "AKIAZK2N6OMGEEXO3T5L"; //"AKIAZK2N6OMGE3XYMB23";
//	public String SECRET_KEY = "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";//"I/ka5GFVMvOAuOBIfY3LslTgc27Pa9dzve583UbK";
	public String BUCKET_NAME = "his-docs";
	
  /** Unit Paths */
  public String AWS_BUCKET_COMMON_PATH = "https://s3.ap-south-1.amazonaws.com/";
  
  String REGISTRATION_BUCKET = "employee/documents/clinical-staff/professional/registration";
  String CERTIFICATE_BUCKET = "employee/documents/clinical-staff/professional/certificates";
  String IDENTIFICATIONS_BUCKET =  "employee/documents/clinical-staff/administrative/identifications";
  String CLINICAL_STAFF_PHOTO_BUCKET = "employee/documents/clinical-staff/administrative/photo";
  String SIGNATURE_BUCKET = "employee/documents/clinical-staff/administrative/signature";
  String NON_CLINICAL_STAFF_IDENTIFICATION_BUCKET = "employee/documents/non-clinical-staff/identification";
  String NON_CLINICAL_STAFF_PHOTO_BUCKET = "employee/documents/non-clinical-staff/photo";
  String INSURANCE_CONTRACT_DOCUMENT_BUCKET = "credit-party/insurance/contract-document";
  String CORPORATE_CONTRACT_DOCUMENT_BUCKET = "credit-party/corporate/contract-document";
  String ACCOUNT_PAYABLE_CONTRACT_DOCUMENT_BUCKET = "account-payable/contract-documents";
  String WARRANTY_DOCUMENTS_DOCUMENT_BUCKET = "procurement/warranty-documents";
  String PROCUREMENT_QUOTATIONS_BUCKET = "procurement/quotations";
  String SCRAP_DISPOSAL_QUOTATIONS_BUCKET = "scrap-disposal/quotations";
  String SCRAP_DISPOSAL_PURCHASE_ORDERS_BUCKET = "scrap-disposal/purchase-orders";
  String UNIT_LOGO_BUCKET = "unit-logo";



  /** Organization Paths */
  String ADMINISTRATIVE_PHOTO_BUCKET = "patient/documents/administrative/photo";
  String ADMINISTRATIVE_PHOTO_DONER_BUCKET =  "patient/documents/administrative/doner/photo";
  String IDENTIFICATION_BUCKET = "patient/documents/administrative/identification";
  String AUTHORIZATIONS_BUCKET =  "patient/documents/administrative/credit-documents/insurance/authorizations";
  String CORPORATE_AUTHORIZATIONS_DOC_BUCKET = "patient/documents/administrative/credit-documents/corporate/authorization-document";
  String OUTSOURCED_LAB_REPORTS_BUCKET = "patient/documents/clinical/reports/outsourced-lab-reports";
  String LAB_REPORTS_BUCKET = "patient/documents/clinical/reports/lab-reports";
  String IMAGING_REPORTS_BUCKET = "patient/documents/clinical/reports/imaging-reports";
  String OTHER_SERVICES_BUCKET = "patient/documents/clinical/reports/other-services";
  String OP_COVERSHEET_BUCKET = "patient/documents/clinical/episodic-documents/op-coversheet";
  String DISCHARGE_SUMMARY_BUCKET =  "patient/documents/clinical/episodic-documents/discharge-summary";
  String NOK_DETAILS_DOCUMENTS_BUCKET =  "patient/documents/clinical/episodic-documents/nok-details/documents";
  String EPISODIC_DOCUMENTS_ICE_BUCKET = "patient/documents/clinical/episodic-documents/ice";
  String EPISODIC_DOCUMENTS_LETTERS_BUCKET = "patient/documents/clinical/episodic-documents/letters";
  String ORGANIZATION_LOGO_BUCKET = "organization-logo";
  String ADMINISTRATIVE_DISCHARGE_SUMMARY_BUCKET = "patient/documents/administrative/dischargeSummary";
  String FUND_REQUEST_DOCUMENTS_BUCKET = "patient/documents/fund-request-documents";
  String PATIENT_DIALYSIS_DOCUMENTS_BUCKET = "patient/documents/patient-dialysis-record-documents";
  String WORKORDER_REQUEST_DOCUMENTS_BUCKET = "workorder/documents/workorder-request-documents";
  String TICKET_SYSTEM_MASTER_FILE = "ticketmaster";

}
