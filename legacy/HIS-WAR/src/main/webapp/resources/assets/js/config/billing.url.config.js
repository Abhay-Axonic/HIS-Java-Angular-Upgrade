/**
 * Billing API URL Configuration
 */
var API="/api";
var BILLING = "/billing";
var BILLING_GLOBAL = "/billing/global";
var BILLING_TRANSACTION = "/billing/transaction";
var GLOBAL ="/global";
var ORGANIZATION_MASTER = "/organizationMaster";
var SPECIALITY = "/speciality";
var SUB_SPECIALITY= "/subSpeciality";
var ORG = "/org";
var UNIT = "/unit";
var SEARCH = "/search";
var COUNT = "/count";
var STATUS="/status";
var S ="/";

/*VARIABLE PRICING SEQUENCE*/
var VARIABLE_PRICING = "/variable-pricing";
var GET_VARIABLE_PRICING_SEQ_FOR_UNIT = "/getVariablePricingSequence";
var SAVE_VARIABLE_PRICING_SEQ_FOR_UNIT = "/saveVariablePricingSequence";
var variablePricingSequence = "/variablePricingSequence"
	
var SAVE_VARIABLE_PRICING = "/saveVariablePricing";
var GET_VARIABLE_PRICING_VALUES = "/getVariablePricingFactorByVisitId"

	
	
/**DEPOSIT MATRIX */	
var SAVE_DEPOSIT_MATRIX =BASE_URL+ROOT_URL+API+BILLING+UNIT+"/depositMatrix";
var GET_DEPOSIT_MATRIX=BASE_URL+ROOT_URL+API+BILLING+UNIT+"/depositMatrix";
var UPDATE_DEPOSIT_MATRIX =BASE_URL+ROOT_URL+API+BILLING+UNIT+ "/depositMatrix";
var LIST_DEPOSIT_MATRIX =BASE_URL+ROOT_URL+API+BILLING+UNIT+"/depositMatrix";
var ACT_INCT_DEPOSIT_MATRIX =BASE_URL+ROOT_URL+API+BILLING+UNIT+"/depositMatrix";
var DEPOSIT_MATRIX_COUNT =BASE_URL+ROOT_URL+API+BILLING+UNIT+"/depositMatrix/count";		
	
/*GLOBAL SERVICE MASTER*/
var SAVE_ORGANIZATION_SERVICE_MASTER ="/saveOrganizationServiceMaster";
var GET_SUBSPECIALITY_BY_SPECIALITY_ID="/adt/getSubSpecialityBySpecialityId";
var GET_ORGANIZATION_SERVICE_MASTER_LIST=BILLING+"/listOrganizationServiceMaster";
var GET_ORGANIZATION_SERVICE_MASTER_COUNT =BILLING+"/getOrganizationServiceMasterCount";
var GET_ORGANIZATION_SERVICE_MASTER_BY_ID ="/getOrganizationServiceMasterById";
var CHANGE_ORGANIZATION_SERVICE_MASTER_STATUS ="/changeOrganizationServiceMasterStatus";
var UPDATEGLOBALSERVICEMASTER =BILLING+"/updateGlobalServiceMaster";

/*BILLING GROUP MASTER*/
var CREATE_BILLING_GROUP_MASTER = "/createBillingGroupMaster";
var CHANGE_BILLING_GROUP_MASTER_STATUS="/changeBillingGroupMasterStatus";
var GET_BILLING_GROUP_MASTER_ID="/getBillingGroupMasterById";
var UPDATE_BILLING_GROUP_MASTER="/updateBillingGroupMaster";
var GET_BILLING_GROUP_MASTER_COUNT="/getBillingGroupMasterCount";
var GET_BILLING_GROUP_MASTER_LIST="/getBillingGroupMasterList";

/*UNIT SERVICE MAPPING*/
var UNIT_SERVICE_MAPPER="/UnitServiceMapper";
var SAVEDOCTORCONSULTATIONSERVICEMAPPER = "/doctorConsultationServiceMapper";
var GETDOCTORCONSULTATIONSERVICEMAPPERLIST = "/getDoctorConsultationServiceMapperList";
var GETDOCTORCONSULTATIONSERVICEMAPPERBYID = "/getDoctorConsultationServiceMapperById";
var UPDATEDOCTORCONSULTATIONSERVICEMAPPER="/updateDoctorConsultationServiceMapper";
var GETDOCTORCONSULTATIONSERVICEBYSPECIALITYANDDOCTORID="/getDoctorConsultationServiceBySpecialityAndDoctorId";


/*IP CONSULTATION CONFIGURATION*/
var SAVE_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER="/saveDoctorIPConsultationServiceMapper";
var UPDATE_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER = "/updateDoctorIPConsultationServiceMapper";
var UPDATE_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_STATUS = "/updateDoctorIPConsultationServiceMapperStatus";
var GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST="/getDoctorIPConsultationServiceMapperList";
var GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_ID="/getDoctorIPConsultationServiceMapperById";
var GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_COUNT="/getDoctorIPConsultationServiceMapperCount";
var GET_ACTIVE_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER="/getActiveDoctorIPConsultationServiceMapper";

/*UNIT SERVICE MASTER DETAILS*/
var UNIT_SERVICE_MASTER_DETAILS ="/UnitServiceMasterDetails";

/*SERVIC MASTER LIST WITH PRICING DETAILS*/
var URI_TO_GET_SERVICE_MASTER_LIST_WITH_PRICE_BY_VISITTYPE="/listServiceMasterWithBasePriceByVisitType";

//for adding tariff master
var SAVETARIFFMASTER=API+UNIT+"/saveTariffMaster";
var GETTARIFFMASTERLIST=API+UNIT+"/getTariffMasterList";
var GETTARIFFMASTERLISTOPDBILLING=API+UNIT+"/getTariffMasterListOpdBilling";
var GETPAYMENTENTITLEMENTLISTBYTARIFFID=API+UNIT+"/getPaymentEntitlementListByTariffid";
var GETTARIFFLISTBYID=API+UNIT+"/getTariffListById";
var UPDATETARIFFMASTER=API+UNIT+"/updateTariffMaster";
var UPDATETARIFFMASTERSTATUS=API+UNIT+"/updateTariffMasterStatus";
var UPDATESTATUSFORTARIFFPAYMENTMAPPER=API+UNIT+"/updateStatusForTariffPaymentMapper";
var GETACTIVETARIFFMASTERLIST=API+UNIT+"/getActiveTariffMasterList";
var GETPAYMENTENTITLEMENTLISTBYTARIFFIDLIST=API+UNIT+"/getPaymentEntitlementListByTariffIdList";
var GETMASTERSERVICELISTBYMULTIPLEPARAMETERS=API+UNIT+"/getMasterServiceListByMultipleParameters";
var SAVEUNITSERVICETARIFFMASTER=API+UNIT+"/saveUnitServiceTariffMaster";
var SAVEBILL = API + BILLING +"/saveBill";

//for global -billing -visit type master
var SAVEVISITTYPE=API+GLOBAL+"/saveVisitType";
var GETVISITTYPEBYID=API+GLOBAL+"/getVisitTypeById";
var UPDATEVISITTYPE=API+GLOBAL+"/updateVisitType";
var UPDATEVISITTYPESTATUS=API+GLOBAL+"/updateVisitTypeStatus";
var GETVISITTYPECOUNT=API+GLOBAL+"/visitTypeMasterListCount";
var GETVISITTYPELIST=API+GLOBAL+"/visitTypeMasterList";
var GETACTIVEVISITTYPEMASTER=API+GLOBAL+"/visitTypeMaster";
//for global -billing -payment mode 
var SAVEPAYMENTMODEMASTER=API+BILLING+"/savePaymentModeMaster";
var GETPAYMENTMODEMASTERBYID=API+BILLING+"/getPaymentModeMasterById";
var GETPAYMENTMODEMASTERLIST=API+BILLING+"/getPaymentModeMasterList";
var UPDATEPAYMENTMODEMASTER=API+BILLING+"/updatePaymentModeMaster";
var UPDATEPAYMENTMODEMASTERSTATUS=API+BILLING+"/updatePaymentModeMasterStatus";
var GETPAYMENTMODEMASTERCOUNT=API+BILLING+"/getPaymentModeMasterCount";

//for global -billing -Payment Entitlement
var GETPAYMENTENTITLEMENTBYID=GLOBAL+"/getPaymententitlementById";
var GETPAYMENTENTITLEMENTLIST=GLOBAL+"/getPaymententitlementList";
var SAVEPAYMENTENTITLEMENT=GLOBAL+"/savePaymententitlement";
var UPDATEPAYMENTENTITLEMENT=GLOBAL+"/updatePaymententitlement";
var UPDATEPAYMENTENTITLEMENTSTATUS=GLOBAL+"/updatePaymententitlementStatus";
var GETPAYMENTENTITLEMENTCOUNT=GLOBAL+"/getPaymententitlementCount";
var PAYMENTENTITLEMENT=GLOBAL+"/paymententitlement";

//for global -billing -BankMaster
var GETPAYMENTENTITLEMENTBYID=GLOBAL+"/getPaymententitlementById";
var GETPAYMENTENTITLEMENTLIST=GLOBAL+"/getPaymententitlementList";
var SAVEPAYMENTENTITLEMENT=GLOBAL+"/savePaymententitlement";
var UPDATEPAYMENTENTITLEMENT=GLOBAL+"/updatePaymententitlement";
var UPDATEPAYMENTENTITLEMENTSTATUS=GLOBAL+"/updatePaymententitlementStatus";
var GETPAYMENTENTITLEMENTCOUNT=GLOBAL+"/getPaymententitlementCount";

// FOR DEPOSIT
var GETDEPOSITBYPATIENTID = API + BILLING + UNIT + "/deposit/patient/"
var GETPERIODICITYSERVICESBYADMID = API + "/periodicity/order"

//FOR DISCOUNT RIGHTS
var SAVE_DISCOUNT_RIGHTS_MASTER = BILLING + "/saveDiscountRightsMaster"
var UPDATE_DISCOUNT_RIGHTS_MASTER = BILLING + "/updateDiscountRightsMaster"
var UPDATE_DISCOUNT_RIGHTS_MASTER_STATUS = BILLING + "/updateDiscountRightsMasterStatus"
var GET_DISCOUNT_RIGHTS_MASTER_LIST = BILLING + "/getDiscountRightsMasterList"
var GET_DISCOUNT_RIGHTS_MASTER_BY_ID = BILLING + "/getDiscountRightsMasterById"
var GET_ACTIVE_DISCOUNT_RIGHTS_MASTER = BILLING + "/getActiveDiscountRightsMaster"
var GET_DISCOUNT_RIGHTS_MASTER_COUNT = BILLING + "/getDiscountRightsMasterCount"
var GET_USERS_LIST = BILLING + "/getUsersList"
var GET_USERS_LIST_BY_ROLEID = BILLING + "/getUsersListByRoleId"

/**Trust Master**/
var ADD_TRUST_MASTER  ="/addTrust";
var EDIT_TRUST_MASTER = "/getTrustbyId";
var UPDATE_TRUST_MASTER ="/updateTrustMaster";
var ACTIVE_INACTIVE_TRUST_MASTER ="/activeInactiveTrust";
var LIST_TRUST_MASTER ="/listTrustMaster";
var COUNT_TRUST_MASTER  ="/getTotalTrustMasterRecord";
var GETTRUSTLIST="/getActiveTrustList"
	
var COUNTER_CHECK_IN =50;
var NURSE_CHECK_IN =51;
var DOCTOR_CHECK_IN =52;
var NEXT_CHECK_IN_CALL =53;
