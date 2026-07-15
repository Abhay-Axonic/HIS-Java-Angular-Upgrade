/******************************************
 * OPD API URL Configuration
 ******************************************/
var API="/api";
var BILLING = "/billing";
var GLOBAL ="/global";
var SPECIALITY = "/speciality";
var SUB_SPECIALITY= "/subSpeciality";
var ORG = "/org";
var UNIT = "/unit";
var SEARCH = "/search";
var COUNT = "/count";
var STATUS="/status";
var KITCHEN ="/kitchen";
var PATIENT ="/patient";
var MASTER = "/master";
var ADT = "/adt";


//Nutrition Master 

 var GETALLNUTRITIONALMASTERLIST = API + KITCHEN + MASTER + "/nutritionalMaster/getAll"
 var NUTRITIONALMASTER = API + KITCHEN + MASTER + "/nutritionalMaster";
var NUTRIONALMASTERSTATUS = API + KITCHEN + MASTER + "/nutritionalMaster/updateStatus";
var GETACTIVENUTRITIONALMASTERLIST = API + KITCHEN + MASTER + "/nutritionalMaster/getActive";

//Diet Master

var GETALLDIETTYPEMASTERLIST = API + KITCHEN + MASTER + "/dietTypeMaster/getAll";
var GETACTIVEDIETTYPEMASTERLIST = API + KITCHEN + MASTER + "/dietTypeMaster/getActive";
var DIETTYPEMASTER = API + KITCHEN + MASTER + "/dietTypeMaster";
var UPDATEDIETMASTERSTATUS = API + KITCHEN + MASTER + "/dietTypeMaster/updateStatus";

// Key Ingredients Master          


var GETALLKEYINGREDIENTSMASTER = API + KITCHEN + MASTER + "/keyIngredientsMaster";
var SAVEKEYINGREDIENTSMASTER = API + KITCHEN + MASTER + "/keyIngredientsMaster";
var GERACTIVEKEYINGREDIENTMASTER = API + KITCHEN + MASTER + "/keyIngredientsMaster/getActive"
var UPDATEKEYINGREDIENTSMASTER = API + KITCHEN + MASTER + "/keyIngredientsMaster";
var UPDATEKEYINGREDIENTSMASTERSTATUS = API + KITCHEN + MASTER + "/keyIngredientsMaster/updateStatus";
var GETUNITMASTERDETAILS = API + KITCHEN + MASTER + "/keyIngredientsMaster/getUnitMasterDetails";
//var GETKEYINGREDIENTSMASTERBYID = API + KITCHEN + KITCHEN + "/getKeyIngredientsMasterById";
var GETINGREDIENTBYID = API + KITCHEN + MASTER + "/keyIngredientsMaster/getByKeyIngredientIdAndOrganizationId";


// Meal Master
var MEALMASTER = API + KITCHEN + MASTER + "/mealMaster";
var GETALLMEALMASTER = API + KITCHEN + MASTER + "/mealMaster/getAll";
var ACTIVEMEALMASTER = API +KITCHEN + MASTER + "/mealMaster/getActive";
var UPDATEMEALMASTERSTATUS = API + KITCHEN + MASTER + "/mealMaster/updateStatus"
var GETMEALMASTERDROPDOWN = API + KITCHEN + MASTER + "/mealMaster/getMealMasterDropDown";


// var MEALMASTER = 

// Dish Master
var SAVEDISHMASTER = API +KITCHEN + MASTER + "/dishMaster";
var GETALLDISHMASTER = API + KITCHEN + MASTER + "/dishMaster";
var GETDISHMASTER = API + KITCHEN + MASTER + "/dishMaster";
var GETACTIVEDISHMASTER = API + KITCHEN + MASTER + "/dishMaster/getActive";
var UPDATEDISHMASTER = API + KITCHEN + MASTER + "/dishMaster";
var UPDATEDISHMASTERSTATUS = API + KITCHEN + MASTER + "/dishMaster/updateStatus";
var GETNUTRITIONALVALUES = API + KITCHEN + MASTER + "/dishMaster/getNutritionalValues";
var GETDISHMASTERDROPDOWN = API + KITCHEN + MASTER + "/dishMaster/getDishMasterDropDown";

// Day wise Diet Master
var DIETTEMPLATE = API + KITCHEN + MASTER + "/dietTemplate";
var UPDATEDIETTEMPLATESTATUS = API + KITCHEN + MASTER + "/dietTemplate/updateStatus";
var GETALLDIETTEMPLATEMASTER = API + KITCHEN + MASTER + "/dietTemplate/getAll";
var GETACTIVEDIETTEMPLATEMASTER = API + KITCHEN + MASTER + "/dietTemplate/getActive";
var GETMEALPREFERENCELIST = API + ADT + UNIT + "/getMealPreferenceMasterList";
var GETACTIVEMEALPREFERENCELIST = API + ADT + UNIT + "/getActiveMealPreferenceMasterList";
var GETTEMPLATELIST = API + KITCHEN + MASTER + "/dietTemplate/getDietTemplateList";
var GETDIETTEMPLATEBYID = API + KITCHEN + MASTER + "/dietTemplate/getDietTemplateById";
var GETDAYWISENUTRITIONLIST = API + KITCHEN + MASTER + "/dietTemplate/getDayWiseNutritionList";
var GETDISHLISTBYDIETPREFID = API + KITCHEN + MASTER + "/dishMaster/getDishListByDietPrefId";
var GETNUTRITIONALVALUESBYDISHLIST = API + KITCHEN + MASTER + "/dishMaster/getNutritionalValuesByDishList";

// Diet and Kitchen Transaction

//Diet Plannig
var GETDIETPREFERENCEBYPATIENTID = API + KITCHEN + "/dietPlanning/getDietPreferenceByPatientId";
var GETDIETTEMPLATEBYDIETPREFID = API + KITCHEN + MASTER + "/dietTemplate/getDietTemplateByDietPrefIdAndDietTypeId";

var SAVEDIETPLANNING = API + KITCHEN + "/dietPlanning";

var GETMEALTYPELIST = API + KITCHEN + MASTER  + "/mealMaster/getActive";
var GETDIETTYPELIST = API + KITCHEN + MASTER  + "/dietTypeMaster/getActive";


// Diet Order
var GETACTIVEFLOORMASTERLIST = ADT + "/getActiveFloorList";
var GETACTIVEWARDMASTERLIST = API + ADT + "/getActiveWardMasterList";
var GETDIETORDERLIST = API + KITCHEN + "/dietOrder";

// Diet Issue
var GETINITIALDIETISSUELIST = API + KITCHEN +"/dietIssue/getByDate";
var GETDIETISSUELIST = API + KITCHEN + "/dietIssue";
var DIETISSUELISTUPDATESTATUS = API + KITCHEN + "/dietIssue/updateStatus";
var DIETISSUE = API + KITCHEN + "/dietIssue";

// Diet Acceptance 
var DIETACCPETANCE = API + KITCHEN + "/dietAcceptance";
var GETINITIALDIETACCEPTLIST = API + KITCHEN + "/dietAcceptance/getByDate"

// Diet Reject
var DIETREJECT = API + KITCHEN + "/rejectedDietList";
var GETINITIALREJECTEDDIETLIST = API + KITCHEN + "/rejectedDietList/getByDate"

