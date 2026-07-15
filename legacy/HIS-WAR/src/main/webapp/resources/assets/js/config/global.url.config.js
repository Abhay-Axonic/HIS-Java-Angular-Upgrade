/*******************************************************************************
 * Global URL Configuration /
 ******************************************************************************/
var STATUS = "/status";
var KIN_DETAILS = API + GLOBAL + "/kinDetails";
var SOURCE_MASTER = API + GLOBAL + "/sourceMaster";
var COMPANY_MASTER = API + GLOBAL + "/companyMaster";
var ASSOCIATED_COMPANY_MASTER = API + GLOBAL + "/associatedCompanyMaster";
var EMPLOYEEREGISTRATION = API + GLOBAL + "/employeeRegistration";
var GETEMPLOYEEDETAILS = API + GLOBAL + "/getEmployeeDetails";
var UPDATEEMPLOYEEDETAILS = API + GLOBAL + "/updateEmployeeDetails";
var UPDATEEMPLOYEESTATUS = API + GLOBAL + "/updateEmployeeStatus";
var DOCTORREGISTRATION = API + GLOBAL + "/doctorRegistration";
var UPDATEDOCTORDETAILS = API + GLOBAL + "/updateDoctorDetails";
var GETDOCTORDETAILS = API + GLOBAL + "/getDoctorDetails";
var UPDATEDOCTORSTATUS = API + GLOBAL + "/updateDoctorStatus";
var SEARCHDOCTORDETAILSBYCRITERIA = API + GLOBAL
		+ "/searchDoctorDetailsByCriteria";
var SEARCHEMPLOYEEBYCRITERIA = API + GLOBAL + "/searchEmployeeByCriteria";
var GET_EMPLOYEE_REGISTRATION_COUNT = API + GLOBAL + "/getEmployeeRegistrationCount";
var SEARCHDEPENDENTDETAILSBYCRITERIA = API + GLOBAL
		+ "/searchDependentDetailsByCriteria";

var GETPLAINEMPLOYEEDETAILS = API + GLOBAL + "/getPlainEmployeeDetails";
var SEARCHPLAINDOCTORDETAILSBYCRITERIA = API + GLOBAL + "/searchPlainDoctorDetailsByCriteria";
// Licence type Master
var GETLICENECETYPEMASTERLIST = "/org/getLiceneceTypeMasterList";
var SAVELICENECETYPEMASTER = "/org/saveLiceneceTypeMaster";
var GETLICENECETYPEMASTERBYID = "/org/getLiceneceTypeMasterById";
var UPDATELICENECETYPEMASTERSTATUS = "/org/updateLiceneceTypeMasterStatus";
var UPDATELICENECETYPEMASTER = "/org/updateLiceneceTypeMaster";
// generAL LEDGER
var GETGENERALLEDGERMASTERACTIVELIST = "/org/getGeneralLedgerMasterActiveList";
//Global->Billing
// doctor category
var GETDOCTORCATEGORYMASTERLIST = "/billing/getDoctorCategoryMasterList";
var GETDOCTORCATEGORYCOUNT = "/billing/getDoctorCategoryCount";
var SAVEDOCTORCATEGORYMASTER = "/billing/saveDoctorCategoryMaster";
var GETDOCTORCATEGORYMASTERBYID = "/billing/getDoctorCategoryMasterById";
var UPDATEDOCTORCATEGORYMASTERSTATUS = "/billing/updateDoctorCategoryMasterStatus";
var UPDATEDOCTORCATEGORYMASTER = "/billing/updateDoctorCategoryMaster";
var GETACTIVEDOCTORCATEGORYMASTERLIST = "/billing/getActiveDoctorCategoryMasterList";
// employee category
var GETEMPLOYEECATEGORYMASTERLIST = "/billing/getEmployeeCategoryMasterList";
var GETACTIVEEMPLOYEECATEGORYMASTERLIST = "/billing/getActiveEmployeeCategoryMasterList";//minesh
var GETEMPLOYEECATEGORYCOUNT = "/billing/getEmployeeCategoryCount";
var SAVEEMPLOYEECATEGORYMASTER = "/billing/saveEmployeeCategoryMaster";
var GETEMPLOYEECATEGORYMASTERBYID = "/billing/getEmployeeCategoryMasterById";
var UPDATEEMPLOYEECATEGORYMASTERSTATUS = "/billing/updateEmployeeCategoryMasterStatus";
var UPDATEEMPLOYEECATEGORYMASTER = "/billing/updateEmployeeCategoryMaster";
// patient category
var GETPATIENTCATEGORYMASTERLIST = GLOBAL + "/getPatientCategoryMasterList";
var GETPATIENTCATEGORYMASTERCOUNT = GLOBAL + "/getPatientCategoryMasterCount";
var SAVEPATIENTCATEGORYMASTER = GLOBAL + "/savePatientCategoryMaster";
var GETPATIENTCATEGORYMASTERBYID = GLOBAL + "/getPatientCategoryMasterById";
var UPDATEPATIENTCATEGORYMASTERSTATUS = GLOBAL
		+ "/updatePatientCategoryMasterStatus";
var UPDATEPATIENTCATEGORYMASTER = GLOBAL + "/updatePatientCategoryMaster";
var GETACTIVEPATIENTCATEGORYBYORGID = GLOBAL + "/patientcategory";
// currency master
var SAVECURRENCYMASTER = "/billing/saveCurrencyMaster";
var GETCURRENCYMASTERBYID = "/billing/getCurrencyMasterById";
var GETCURRENCYMASTERLIST = "/billing/getCurrencyMasterList";
var GETALLCURRENCYMASTERLIST = "/billing/getAllCurrencyMasterList";  //Dilip
var UPDATECURRENCYMASTER = "/billing/updateCurrencyMaster";
var UPDATECURRENCYMASTERSTATUS = "/billing/updateCurrencyMasterStatus";
var GETCURRENCYCOUNT = "/billing/getCurrencyCount";
// bank branch master
var SAVEBANKBRANCHMASTER = "/api/billing/saveBankBranchMaster";
var GETBANKBRANCHMASTERBYID = "/api/billing/getBankBranchMasterById";
var GETBANKBRANCHMASTERLIST = "/api/billing/getBankBranchMasterList";
var UPDATEBANKBRANCHMASTER = "/api/billing/updateBankBranchMaster";
var UPDATEBANKBRANCHMASTERSTATUS = "/api/billing/updateBankBranchMasterStatus";
var GETBANKBRANCHMASTERCOUNT = "/api/billing/getBankBranchMasterCount";
var GETACTIVEBANKBRANCHMASTERLIST ="/api/billing/getActiveBankBranchMasterList";

//bank master
var SAVEBANKMASTER = "/api/billing/saveBankMaster";
var GETBANKMASTERBYID = "/api/billing/getBankMasterById";
var GETBANKMASTERLIST = "/api/billing/getBankMasterList";
var UPDATEBANKMASTER = "/api/billing/updateBankMaster";
var UPDATEBANKMASTERSTATUS = "/api/billing/updateBankMasterStatus";
var GETBANKMASTERCOUNT = "/api/billing/getBankMasterCount";

//currency Denomination master
var SAVECURRENCYDENOMINATIONMASTER = "/api/billing/saveCurrencyDenominationMaster";
var GETCURRENCYDENOMINATIONMASTERBYID = "/api/billing/getCurrencyDenominationMasterById";
var GETCURRENCYDENOMINATIONMASTERLIST = "/api/billing/getCurrencyDenominationMasterList";
var UPDATECURRENCYDENOMINATIONMASTER = "/api/billing/updateCurrencyDenominationMaster";
var UPDATECURRENCYDENOMINATIONMASTERSTATUS = "/api/billing/updateCurrencyDenominationMasterStatus";
var GETCURRENCYDENOMINATIONCOUNT = "/api/billing/getCurrencyDenominationCount";

//Global->Common
//Priority master
var SAVEPRIORITYMASTERMASTER = "/api/common/savePriorityMasterMaster";
var GETPRIORITYMASTERBYID = "/api/common/getPriorityMasterById";
var GETPRIORITYMASTERLIST = "/api/common/getPriorityMasterList";
var UPDATEPRIORITYMASTER = "/api/common/updatePriorityMaster";
var UPDATEPRIORITYMASTERSTATUS = "/api/common/updatePriorityMasterStatus";
var GETPRIORITYMASTERCOUNT = "/api/common/getPriorityMasterCount";
// Rejection master
var SAVEREJECTIONMASTER = "/api/common/saveRejectionMaster";
var GETREJECTIONMASTERBYID = "/api/common/getRejectionMasterById";
var GETREJECTIONMASTERLIST = "/api/common/getRejectionMasterList";
var UPDATEREJECTIONMASTER = "/api/common/updateRejectionMaster";
var UPDATEREJECTIONMASTERSTATUS = "/api/common/updateRejectionMasterStatus";
var GETREJECTIONMASTERCOUNT = "/api/common/getRejectionMasterCount";
//Reason Type master
var SAVEREASONTYPEMASTER = "/api/common/saveReasonTypeMaster";
var GETREASONTYPEMASTERBYID = "/api/common/getReasonTypeMasterById";
var GETREASONTYPEMASTERLIST = "/api/common/getReasonTypeMasterList";
var UPDATEREASONTYPEMASTER = "/api/common/updateReasonTypeMaster";
var UPDATEREASONTYPEMASTERSTATUS = "/api/common/updateReasonTypeMasterStatus";
var GETREASONTYPEMASTERCOUNT = "/api/common/getReasonTypeMasterCount";
//Qualification master
var SAVEQUALIFICATIONMASTER = "/api/common/saveQualificationMaster";
var GETQUALIFICATIONMASTERBYID = "/api/common/getQualificationMasterById";
var GETQUALIFICATIONMASTERLIST = "/api/common/getQualificationMasterList";
var UPDATEQUALIFICATIONMASTER = "/api/common/updateQualificationMaster";
var UPDATEQUALIFICATIONMASTERSTATUS = "/api/common/updateQualificationMasterStatus";
var GETQUALIFICATIONMASTERCOUNT = "/api/common/getQualificationMasterCount";
//Degree master
var SAVEDEGREEMASTER = "/api/common/saveDegreeMaster";
var GETDEGREEMASTERBYID = "/api/common/getDegreeMasterById";
var GETDEGREEMASTERLIST = "/api/common/getDegreeMasterList";
var UPDATEDEGREEMASTER = "/api/common/updateDegreeMaster";
var UPDATEDEGREEMASTERSTATUS = "/api/common/updateDegreeMasterStatus";
//var GETDEGREEMASTERCOUNT = "/api/common/getQualificationMasterCount";//minesh
var GETDEGREEMASTERCOUNT = "/api/common/getDegreeMasterCount";//minesh
//Designation master
var SAVEDESIGNATIONMASTER = "/api/common/saveDesignationMaster";
var GETDESIGNATIONMASTERBYID = "/api/common/getDesignationMasterById";
var GETDESIGNATIONMASTERLIST = "/api/common/getDesignationMasterList";
var GETACTIVEDESIGNATIONMASTERLIST = "/api/common/getActiveDesignationMasterList";//minesh
var UPDATEDESIGNATIONMASTER = "/api/common/updateDesignationMaster";
var UPDATEDESIGNATIONMASTERSTATUS = "/api/common/updateDesignationMasterStatus";
var GETDESIGNATIONMASTERCOUNT = "/api/common/getDesignationMasterCount";
//Reason master
var SAVEREASONMASTER = "/api/common/saveReasonMaster";
var GETREASONMASTERBYID = "/api/common/getReasonMasterById";
var GETREASONMASTERLIST = "/api/common/getReasonMasterList";
var UPDATEREASONMASTER = "/api/common/updateReasonMaster";
var UPDATEREASONMASTERSTATUS = "/api/common/updateReasonMasterStatus";
var GETREASONMASTERCOUNT = "/api/common/getReasonMasterCount";
var GETALLACTIVETAX = "/api/global/getAllActiveTax";

//Cabin master
var SAVE_CABIN_MASTER = "/api/global/unit/saveCabinMaster";
var UPDATE_CABIN_MASTER = "/api/global/unit/updateCabinMaster";
var UPDATE_CABIN_MASTER_STATUS = "/api/global/unit/updateCabinMasterStatus";
var GET_CABIN_MASTER_BY_ID = "/api/global/unit/getCabinMasterById";
var GET_CABIN_MASTER_LIST = "/api/global/unit/getCabinMasterList";
var GET_CABIN_MASTER_COUNT = "/api/global/unit/getCabinMasterCount";
var GET_ACTIVE_CABIN_MASTER = "/api/global/unit/getActiveCabinMaster";

//Counter master
var SAVE_COUNTER_MASTER = "/api/global/unit/saveCounterMaster";
var UPDATE_COUNTER_MASTER = "/api/global/unit/updateCounterMaster";
var UPDATE_COUNTER_MASTER_STATUS = "/api/global/unit/updateCounterMasterStatus";
var GET_COUNTER_MASTER_BY_ID = "/api/global/unit/getCounterMasterById";
var GET_COUNTER_MASTER_LIST = "/api/global/unit/getCounterMasterList";
var GET_COUNTER_MASTER_COUNT = "/api/global/unit/getCounterMasterCount";
var GET_ACTIVE_COUNTER_MASTER = "/api/global/unit/getActiveCounterMaster";

//User Counter master
var ADD_USER_COUNTER_MASTER = "/api/global/userCounterMapper";
var UPDATE_USER_COUNTER_MASTER = "/api/global/userCounterMapper";
var UPDATE_USER_COUNTER_MASTER_STATUS = "/api/global/userCounterMapper/status";
var GET_USER_COUNTER_MASTER_BY_ID = "/api/global/userCounterMapper/user";
var GET_USER_COUNTER_MASTER_LIST = "/api/global/userCounterMapper";
var GET_USER_COUNTER_MASTER_COUNT = "/api/global/userCounterMapper/size";
var GET_ACTIVE_USER_COUNTER_MASTER = "/api/global/userCounterMapper";
var GET_DETAILED_LIST_USER_COUNTER_MASTER ="/api/global/userCounterMapper/user";

//User Tube Feeding Products master
var SAVE_TUBE_FEEDING_PRODUCTS = "/api/global/saveTubeFeedingProducts";
var UPDATE_TUBE_FEEDING_PRODUCTS = "/api/global/updateTubeFeedingProducts";
var UPDATE_TUBE_FEEDING_PRODUCTS_STATUS = "/api/global/updateTubeFeedingProductsStatus";
var GET_TUBE_FEEDING_PRODUCTS_BY_ID = "/api/global/getTubeFeedingProductsById";
var GET_TUBE_FEEDING_PRODUCTS_LIST = "/api/global/getTubeFeedingProductsList";
var GET_TUBE_FEEDING_PRODUCTS_COUNT = "/api/global/getTubeFeedingProductsCount";
var GET_ACTIVE_TUBE_FEEDING_PRODUCTS = "/api/global/getActiveTubeFeedingProducts";

//Doctor Cabin master
var ADD_DOCTOR_CABIN_MAPPER = "/api/global/saveDoctorCabinMapper";
var UPDATE_DOCTOR_CABIN_MAPPER = "/api/global/updateDoctorCabinMapper";
var UPDATE_DOCTOR_CABIN_MAPPER_STATUS = "/api/global/updateDoctorCabinMapperStatus";
var GET_DOCTOR_CABIN_MAPPER_BY_ID = "/api/global/getDoctorCabinMapperById";
var GET_DOCTOR_CABIN_MAPPER_LIST = "/api/global/getDoctorCabinMapperlist";
var GET_DOCTOR_CABIN_MAPPER_COUNT = "/api/global/getDoctorCabinMapperCount";
var GET_ACTIVE_DOCTOR_CABIN_MAPPER = "/api/global/getActiveDoctorCabinMapper";
var GET_LIST_OF_DOCTOR = "/api/global/getDoctorList";
var GET_DETAILED_CABIN_LIST = "/api/global/getDetailedCabinList";

//User Nursing Station master
var SAVE_USER_NURSING_STATION_MAPPER = "/api/global/saveUserNursingStationMapper";
var UPDATE_USER_NURSING_STATION_MAPPER = "/api/global/updateUserNursingStationMapper";
var UPDATE_USER_NURSING_STATION_MAPPER_STATUS = "/api/global/updateUserNursingStationMapperStatus";
var GET_USER_NURSING_STORE_MAPPER_BY_ID = "/api/global/getUserNursingStationMapperById";
var GET_USER_NURSING_STATION_MAPPER_LIST = "/api/global/getUserNursingStationMapperlist";
var GET_USER_NURSING_STATION_MAPPER_COUNT = "/api/global/getUserNursingStationMapperCount";
var GET_ACTIVE_USER_NURSING_STATION_MAPPER = "/api/global/getActiveUserNursingStationMapper";
var GET_DETAILED_NURSING_STATION_LIST = "/api/global/getDetailedNursingStationList";
var GET_USER_LIST="/api/global/getUserList";
var GET_USER_LIST_FOR_STORE="/api/global/getUserListForStore";
var GET_NURSING_STATIONS = "/api/global/getNursingStationList";

//Employee Category master
var SAVE_EMPLOYEE_CATEGORY_MASTER = "/billing/saveEmployeeCategoryMaster";
var UPDATE_EMPLOYEE_CATEGORY_MASTER = "/billing/updateEmployeeCategoryMaster";
var UPDATE_EMPLOYEE_CATEGORY_MASTER_STATUS = "/billing/updateEmployeeCategoryMasterStatus";
var GET_EMPLOYEE_CATEGORY_MASTER_BY_ID = "/billing/getEmployeeCategoryMasterById";
var GET_EMPLOYEE_CATEGORY_MASTER_LIST = "/billing/getEmployeeCategoryMasterList";
var GET_ACTIVE_EMPLOYEE_CATEGORY_MASTERLIST = "/billing/getActiveEmployeeCategoryMasterList";//minesh
var GET_EMPLOYEE_CATEGORY_MASTER_COUNT = "/billing/getEmployeeCategoryCount";

//Doctor Category master
var SAVE_DOCTOR_CATEGORY_MASTER = "/billing/saveDoctorCategoryMaster";
var UPDATE_DOCTOR_CATEGORY_MASTER = "/billing/updateDoctorCategoryMaster";
var UPDATE_DOCTOR_CATEGORY_MASTER_STATUS = "/billing/updateDoctorCategoryMasterStatus";
var GET_DOCTOR_CATEGORY_MASTER_BY_ID = "/billing/getDoctorCategoryMasterById";
var GET_DOCTOR_CATEGORY_MASTER_LIST = "/billing/getDoctorCategoryMasterList";
var GET_DOCTOR_CATEGORY_MASTER_COUNT = "/billing/getDoctorCategoryCount";
var GET_ACTIVE_DOCTOR_CATEGORY_MASTER = "/billing/getActiveDoctorCategoryMasterList";

//Loyalty Card Configuration
var SAVE_LOYALTY_CARD_CONFIGURATION = "/api/unit/saveLoyaltyCardConfiguration";
var UPDATE_LOYALTY_CARD_CONFIGURATION_STATUS = "/api/unit/updateLoyaltyCardConfigurationStatus";
var GET_LOYALTY_CARD_CONFIGURATION_BY_ID = "/api/unit/getLoyaltyCardConfigurationById";
var GET_LOYALTY_CARD_CONFIGURATION_LIST = "/api/unit/getLoyaltyCardConfigurationList";
var GET_LOYALTY_CARD_CONFIGURATION_COUNT = "/api/unit/getLoyaltyCardConfigurationCount";

//Loyalty Card Master
var SAVE_LOYALTY_CARD_MASTER = "/api/unit/saveLoyaltiCardMaster";
var UPDATE_LOYALTY_CARD_MASTER_STATUS = "/api/unit/updateLoyaltiCardMasterStatus";
var GET_LOYALTY_CARD_MASTER_LIST = "/api/unit/getLoyaltiCardMasterList";
var GET_LOYALTY_CARD_MASTER_COUNT = "/api/unit/getLoyaltiCardMasterCount";


//Vitals Add
var SAVE_VITALS = "/vital";
var GET_VITALS = "/vital/details";
var GET_VITAL_LIST = "/vitals";

//growth chart
var GET_HEIGHT_UNIT = "/api/global/growthchart/heightunit";
var GET_WEIGHT_UNIT = "/api/global/growthchart/weightunit";
var GET_CIRCUMFERENCE_UNIT = "/api/global/growthchart/circumferenceunit";
var SAVE_GROWTH_CHART = "/api/global/growthChart";
var UPDATE_GROWTH_CHART = "/api/global/growthChart";
var GROWTH_CHART_LIST = "/api/global/growthChartlist";
var GET_GROWTH_CHART_BY_ID = "/api/global/growthChart";
var DELETE_GROWTH_CHART = "/api/global/growthChart";
var SAVE_GROWTH_CHART_TRANSACTION = "/api/global/growthChart/patient";
var GET_GROWTH_CHART_TRANSACTION = "/api/global/growthChart/patient/details";

//ORDER_SET
var GET_ORDER_SET = "/api/orderset/details";
var GET_ORDER_SET_ORDERS = "/api/orderset/orders";


var PERMISSION = "/privilege/role";
var TEMPLATE_LIST_BY_SPECILATIY = "/coversheet/templates"
	
//REPORT GENERATION	
var REPORT_API_URI1 = 	BASE_URL + ROOT_URL +"/api/billing/unit/bill/Report";
var REPORT_API_URI_FOR_MIS = 	BASE_URL + ROOT_URL +"/api/billing/unit/bill/Report/MIS";
var REPORT_DOWNLOAD_URI = 	BASE_URL + ROOT_URL +"/api/billing/unit/bill/Report/file";
var REPORT_API_URI2 = 	"http://34.212.43.230/reports/display";


//OTHER APP DATA

var OTHER_ENCOUNER_LIST = "/api/otherAppData/encounterList";
var OTHER_QUICH_SUMMERY = "/api/otherAppData/quickSummery";
var OTHER_TRIAGE_LIST = "/api/otherAppData/triageList";
var OTHER_HISTORY = "/api/otherAppData/history";
var OTHER_PHYSICAL_EXAMINATION = "/api/otherAppData/physicalExamination";
var OTHER_PRESCRITION = "/api/otherAppData/prescription";
var OTHER_LOBORATORY = "/api/otherAppData/laboratory";
var OTHER_DIAGNOSYS = "/api/otherAppData/diagonosys";
var OTHER_PROCEDURE = "/api/otherAppData/procedures";
var OTHER_RADIOLOGY = "/api/otherAppData/radiology";
var OTHER_REVIEW_NOTES = "/api/otherAppData/reviewNotes";
var PROVISIONAL_DIAGNOSYS = "/api/otherAppData/provisionalDiagnosys";

//for Hospital Account Master
var GETHOSPITALACCOUNTMASTERLIST="/api/billing/getHospitalAccountMasterList";
var SAVEHOSPITALACCOUNTMASTER = "/api/billing/saveHospitalAccountMaster";
var GETHOSPITALACCOUNTMASTERBYID = "/api/billing/getHospitalAccountMasterById";
var UPDATEHOSPITALACCOUNTMASTER = "/api/billing/updateHospitalAccountMaster";
var UPDATEHOSPITALACCOUNTMASTERSTATUS = "/api/billing/updateHospitalAccountMasterStatus";
var GETHOSPITALACCOUNTMASTERCOUNT = "/api/billing/getHospitalAccountMasterCount";

//SHIFT MASTER
var GETSHIFTMASTERLIST="/api/common/getShiftMasterList";
var SAVESHIFTMASTER = "/api/common/saveShiftMaster";
var GETSHIFTMASTERBYID = "/api/common/getShiftMasterById";
var UPDATESHIFTMASTER = "/api/common/updateShiftMaster";
var UPDATESHIFTMASTERSTATUS = "/api/common/updateShiftMasterStatus";
var GETSHIFTMASTERCOUNT = "/api/common/getShiftMasterCount";

//Nurse Allocation Master
var GETNURSEALLOCATIONMASTERLIST="/api/adt/unit/getNurseAllocationMasterList";
var SAVENURSEALLOCATIONMASTER = "/api/adt/unit/saveNurseAllocationMaster";
var GETNURSEALLOCATIONMASTERBYID = "/api/adt/unit/getNurseAllocationMasterById";
var UPDATENURSEALLOCATIONMASTER = "/api/adt/unit/updateNurseAllocationMaster";
var UPDATENURSEALLOCATIONMASTERSTATUS = "/api/adt/unit/updateNurseAllocationMasterStatus";
var GETNURSEALLOCATIONMASTERCOUNT = "/api/adt/unit/getNurseAllocationMasterCount";

// Nurse Shift Allocation
var SAVENURSESHIFTALLOCATION="/api/global/saveNurseShiftAllocation";

//RMO Allocation Master
var GETRMODOCTORALLOCATIONMASTERLIST="/api/unit/clinical/getRmoAllocationMasterList";
var SAVERMODOCTORALLOCATIONMASTER = "/api/unit/clinical/saveRmoAllocationMaster";
var GETRMODOCTORALLOCATIONMASTERBYID ="/api/unit/clinical/getRmoAllocationMasterById" ;
var UPDATERMODOCTORALLOCATIONMASTER = "/api/unit/clinical/updateRmoAllocationMaster";
var UPDATERMODOCTORALLOCATIONMASTERSTATUS ="/api/unit/clinical/updateRmoAllocationMasterStatus" ;
var GETRMODOCTORALLOCATIONMASTERCOUNT = "/api/unit/clinical/getRmoAllocationMasterCount";


//
var SAVEDOCTORFAVOURITEMEDICATION = "/api/unit/clinical/saveDoctorFavouriteMedication";
var REMOVEDOCTORFAVOURITEMEDICATION = "/api/unit/clinical/removeFromDoctorFavouritesMedication";
var UPDATEDOCTORFAVOURITEMEDICATION = "/api/unit/clinical/updateDoctorFavouriteMedication";
var GETDOCTORFAVOURITEMEDICATION = "/api/unit/clinical/getDoctorFavouriteMedication";
var GETDOCTORFAVOURITEMEDICATIONFORCOVERSHEET = "/api/unit/clinical/getDoctorFavouriteMedicationForCoversheet";
