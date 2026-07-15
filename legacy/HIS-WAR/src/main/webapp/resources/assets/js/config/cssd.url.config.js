/******************************************
 * CSSD URL Configuration
 ******************************************/

var CSSD = "/cssd"
var API = "/api"
var GLOBAL = "/global"
var CANCEL = "/cancel"
var REJECT = "/reject"
	
	
// Sterlization type master
	
var	CREATESTERILIZATION = CSSD + "/createSterilizationType";
var GETSTERILIZATION = CSSD + "/getSterilizationTypesList";
var UPDATESTERILZATION = CSSD + "/updateSterilizationType";
var GETACTIVESTERILIZATION = CSSD + "/getActiveSterilizationTypesList";
var UPDATESTERILIZATIONSTATUS = CSSD + "/updateSterilizationTypeStatus";
var GETACTIVESTERILIZATIONCOLOR = CSSD + "/getActiveSterilizationColorList";
// Autoclave Master
var CREATEAUTOCLAVE = CSSD + "/createAutoclave";
var GETAUTOCLAVELIST = CSSD + "/getAllAutoclavesList";
var UPDATEAUTOCLAVE = CSSD + "/updateAutoclave";
var GETACTIVEAUTOCLAVESLIST = CSSD + "/getActiveAutoclavesList";
var UPDATEAUTOCLAVESTATUS = CSSD + "/updateAutoclaveStatus"

// Wrapper Expiry Master
var CREATEWRAPPEREXPIRY = CSSD + "/createWrapperExpiry";
var GETWRAPPEREXPIRYMASTERLIST = CSSD + "/getWrapperExpiryList";
var UPDATEWRAPPEREXPIRY = CSSD + "/updateWrapperExpiry";
var GETACTIVEWRAPPEREXPIRYLIST = CSSD + "/getActiveWrapperExpiryList";
var UPDATEWRAPPEREXPIRYSTATUS = CSSD + "/updateWrapperExpiryStatus";
// Kit
var CREATEKIT = CSSD + "/createKit";
var GETALLKIT = CSSD + "/getAllKitsList";
var GETSURGEONLIST = CSSD + "/getAllSurgeonList";
var UPDATEKIT = CSSD + "/updateKit";
var GETALLINSTRUMENTLIST = CSSD + "/getAllInstrumentList";

var CSSDSTORES = CSSD + "/getCssdStoreListByUserId";

// CSSD Transaction
// Pre Sterllizing Process

var SEARCHKITBYKITNAME = CSSD + "/searchKitByKitName";

var SEARCHINSTRUMENTBYKITNAME = CSSD + "/searchInstrumentByKitName";

var SEARCHINSTRUMENTASKIT = CSSD + "/searchInstrumentAsKit";

var SEARCHPREKITBYNAME = CSSD + "/searchPreSterilizationProcessList";

var GETEQUIPLIST = CSSD + "/getKitWithEquipments";
var SEARCHINSTRUMENT = CSSD + "/searchInstrument";
var UPDATEKITMASTERSTATUS = CSSD + "/updateKitStatus";
var GETPREKITLIST = CSSD + "/getKitList";
var GETCLEANEDWORKLISTBYMULTIPLEFIELDS = CSSD + "/getCleanedWorklistByMultipleFields";
var GETPENDINGCLEANINGWORKLISTBYMULTIPLEFIELDS = CSSD + "/getPendingForCleaningWorklistByMultipleFields";
var GETINSPECTEDWORKLISTBYMULTIPLEFIELDS = CSSD + "/getInspectedWorklistByMultipleFields";
var GETDISINFECTEDWORKLISTBYMULTIPLEFIELDS = CSSD + "/getDisinfectedWorklistByMultipleFields";
var GETKITFORMATIONLISTBYMULTIPLEFIELDS = CSSD + "/getKitFormationWorklistByMultipleFields";

var VIEWPREKITDETAILSLIST = CSSD + "/getPreSterilizationProcessEquipmentList";
var VIEWPREKITBYID = CSSD + "/getPreSterilizationProcessKitById";
var VIEWPREKITBYTRANSITIONNUMBER = CSSD + "/getPreSterilizationProcessByTransitionNumber";




var UPDATEEQUIPMENTCLEANSTATUS = CSSD + "/cleanEquipment";
var SAVECLEANEDWORKLIST = CSSD + "/saveCleanedWorklist";
var GETPRESTERILIZATIONEQUIPLIST = CSSD + "/getPreSterilizationProcessEquipmentList";

var VIEWPREKITEQUIPBYID = CSSD + "/getPreSterilizationProcessEquipmentList";
var UPDATEEQUIPMENTINSPECTIONSTATUS = CSSD + "/inspectEquipment";
var UPDATEEQUIPMENTCLEANINGSTATUS = CSSD + "/cleanEquipment";
var REPLACEINSTRUMENT = CSSD + "/replaceInstrument";

var UPDATEEQUIPMENTDISINFECTIONSTATUS = CSSD + "/disInfectEquipment";

var SAVECLEANEDWORKLIST = CSSD + "/saveCleanedWorklist";

var SEARCHINSTRUMENTBYMANYFIELDS = CSSD + "/searchInstrumentByMultipleFields";


var GETKITFORMATIONLISTBYTRANSNO = CSSD + "/getKitFormationWorklistByTransitionNumber";

var GETACTIVEWRAPPEREXPIRYBYCODE = CSSD + "/getActiveWrapperExpiryByCode";

var SAVEFORMKIT = CSSD + "/formkit";

var SEARCHKITSTOLOADBATCH = CSSD + "/searchKitsToLoadBatch";

var GETRECENTBIOINDIDATE = CSSD + "/getRecentBioIndiDate";

var LOADBATCH = CSSD + "/loadBatch";

var GETLOADBATCHLISTBYMULTIPLEFIELDS = CSSD + "/getLoadBatchListByMultipleFields";

var GETBATCHDETAILSBYBATCHNUMBER = CSSD + "/getBatchDetailsByBatchNumber";

var SAVEPENDINGINDICATORS = CSSD + "/savePendingIndicators"

// Nursing CSSD
// Kit Indent
var GETKITINDENTFROMSTORE = CSSD + "/getKitIndentFromStore";
var GETKITINDENTTOSTORE = CSSD + "/getKitIndentToStore";
var ADDNEWKITINDENT = CSSD + "/saveKitIndent";
var GETKITINDENTBYMULTIPLEFIELDS = CSSD + "/getKitIndentByMultipleFields";
var GETKITLISTBYINDENTNO = CSSD + "/getKitListByIndentNo";
var UPDATEKITINDENTSTATUS = CSSD + "/updateKitIndentStatus";
var CANCELKITINDENT = CSSD + "/cancelKitIndent";
var GETKITINDENTBYINDENTNO = CSSD + "/getKitIndentByIndentNo";
var GETKITWITHINSTRUMENT = CSSD + "/getKitWithEquipments";

var GETREASONSFORCANCEL = API + GLOBAL + CANCEL + "/reasons";

var UPDATEKITINDENT = CSSD + "/updateKitIndent";


//Kit Approval
var GETKITINDENTAPPROVALBYMULTIPLEFIELDS = CSSD + "/getKitIndentApprovalByMultipleFields";
var GETREASONSTOREJECT = API + GLOBAL + REJECT + "/reasons";
var REJECTKIT = CSSD + "/rejectKit";
var APPROVEKIT = CSSD + "/approveKit";


//Kit Issue
var GETSTERILIZEDKITSBYKITID = CSSD + "/getSterilizedKitsByKitId";
var ADDNEWKITISSUE = CSSD + "/saveKitIssue";
var GETKITISSUEBYMULTIPLEFIELDS = CSSD + "/getKitIssueByMultipleFields";
var VIEWKITBYISSUENO = CSSD + "/getKitIssueByIssueNo";
var UPDATEKITISSUE = CSSD + "/updateKitIssue";
var UPDATEKITISSUESTATUS = CSSD + "/updateKitIssueStatus";
var CANCELKITISSUE = CSSD + "/cancelKitIssue";
var GETKITINDENTBYMULTIPLEFIELDSFORKITISSUE = CSSD + "/getKitIndentByMultipleFieldsForKitIssue";
var GETSTERILIZATIONREQBYMULTIPLEFIELDSFORKITISSUE = CSSD + "/getSterilizationRequestByMultipleFieldsForKitIssue";

//Kit Issue Acceptance 
var GETKITISSUEBYMULTIPLEFIELDSFORACCEPTANCE = CSSD + "/getKitIssueByMultipleFieldsForAcceptance";
var REJECTKITISSUE = CSSD + "/rejectKitIssue";
var ACCEPTKITISSUE = CSSD + "/acceptKitIssue";
var ACCEPTREJECTKITS = CSSD + "/acceptRejectKits";

//Kit Return
var GETKITLISTFORKITRETURN = CSSD + "/getKitListForKitReturn";
var ADDNEWKITRETURN = CSSD + "/saveKitReturn";
var GETKITRETURNBYMULTIPLEFIELDS = CSSD + "/getKitReturnByMultipleFields";
var UPDATEKITRETURNSTATUS = CSSD + "/updateKitReturnStatus";
var UPDATEKITRETURN = CSSD + "/updateKitReturn";
var GETKITRETURNBYRETURNNO = CSSD + "/getKitReturnByReturnNo";
var VIEWKITBYRETURNNO = CSSD + "/getKitReturnByReturnNo";
var CANCELKITRETURN = CSSD + "/cancelKitReturn";

//Send To Microbiology
var GETSENDTOMICROBIOLOGYLISTBYMULTIPLEFIELDS = CSSD + "/getLoadBatchListToSendToMicrobiologyByMultipleFields";
var SENDTOMICROBIOLOGY = CSSD + "/sendToMicrobiology";

//Sterilization Request In Nursing
var GETSTERILIZATIONREQUESTBYMULTIPLEFIELDS = CSSD + "/getSterilizationRequestByMultipleFields";
var GETKITLISTFORSTERILIZATIONREQUEST = CSSD + "/getKitListForSterilizationRequest";
var SAVESTERILIZATIONREQUEST = CSSD + "/saveSterilizationRequest";
var GETSTERILIZATIONREQUESTBYREQUESTNUMBER = CSSD + "/getSterilizationRequestByRequestNo";
var UPDATESTERILIZATIONREQUEST = CSSD + "/updateSterilizationRequest";
var CANCELSTERILIZATIONREQUEST = CSSD + "/cancelSterilizationRequest";
var ACCEPTSTERREQ = CSSD + "/acceptSterilizationRequest";

//Sterilization REquest Acceptance In CSSD
var GETSTERILIZATIONREQUESTFORACCEPTANCEBYMULTIPLEFIELDS = CSSD + "/getSterilizationRequestForAcceptanceByMultipleFields";


