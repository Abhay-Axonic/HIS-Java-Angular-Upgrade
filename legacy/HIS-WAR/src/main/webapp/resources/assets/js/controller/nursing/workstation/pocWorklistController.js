(function () {
    "use strict";

    function pocWorklist($scope, $rootScope, $state, $cookies, GenericService,PagerService, promiseFactory, $sessionStorage, growl, $interval,$stateParams,GenericServiceParamHeader,BillingGenericService,$window,reportService,$timeout) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){ $state.go('login');
            return;
        }

        var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');

        $scope.nurseRole = cookieObjectAppConfig.nurseRole;
        $scope.doctorRole = cookieObjectAppConfig.doctorRole;
        $scope.roleName = cookieObjectAppConfig.roleName;
        $scope.orgUnitId = cookieObject.unitId;
        $scope.orgId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId;
        $scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
        if (cookieObjectAppConfigSecond != undefined) {
        $scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
        }
        $scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
        if($scope.isBillManipulationAllowed == true){
            $scope.filterDummyService = 1;
        }else{
            $scope.filterDummyService = 2;
        }

        var cookieObject,cookieObjectAppConfig;
        [cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
        $scope.patientCategoryId = cookieObjectAppConfig.patientCategoryId;

        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
         if(cookieObjectAppConfig == undefined){
            $state.go('login');
            return;
         }
         else
         {
            console.log('cookieObjectAppConfig',cookieObjectAppConfig);
         }

        $scope.nephrologySpecialityId=cookieObjectAppConfig.hemodialysisSpecialityId;

        $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;

        $scope.dateFormat = cookieObjectAppConfig.dateFormat;

        var labConfig  =    $scope.checkLabConfig();
        $scope.isPrebarcodeGenerantion = labConfig.isPrebarcodeGenerantion;
        $scope.deptId = labConfig.labSpecialityId;
        $scope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
        $scope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
        $scope.isAcceptanceInDepartment= labConfig.isAcceptanceInDepartment;
        $scope.isWorklistDepartment= labConfig.isWorklistDepartment;
        $scope.consolidatedResultEntry = labConfig.consolidatedResultEntry;

        $scope.interventionEndDate = moment().toDate();
        $scope.interventionEndTime = new Date();

        $scope.dateOptions =
        {
            formatYear : 'yyyy',
            showWeeks : false,
            maxDate:new Date()
        };

        $scope.open = function() {
            $scope.popup.opened = true;
        };

        $scope.popup =
        {
            opened : false
        };


        function initializeController() {

            $rootScope.loginpage = true;

//            $scope.orgUnitId = cookieObject.unitId;
//            $scope.orgId = cookieObject.organizationId;
//            $scope.userId = cookieObject.userId;


            /**Variables for pagination */
            $scope.offset = 0;
            $scope.noOfRecordsPerPage = 10;
            $scope.sampleCollectionList = [];
            $scope.admissionObj = {};
            $scope.totalNoOfRecords;
            $scope.searchKeyword;
            $scope.commonListCount;
            $scope.commonList = [];
            $scope.getUnitByOrganization();

            if($scope.isAppointmentRequired  == undefined || $scope.isAppointmentRequired == null)
            {
              $scope.isAppointmentRequired = "N";
            }
            else
            {
              $scope.isAppointmentRequired = $scope.isAppointmentRequired;
            }

            $scope.initLabSampleMaster = function() {

                $scope.listSampleDtls = [];
                $scope.labSampleMaster = {
                    "labSampleId" : "",
                    "orgId" : $scope.orgId,
                    "orgUnitId" : $scope.orgUnitId,
                    "sampleGenDatetime" : "",
                    "orderId" : "",
                    "visitTypeId" : $scope.patientDetails.visitTypeId,
                    "admissionId" : $scope.patientDetails.admissionId,
                    "encounterId" : "",
                    "patientId" : $scope.patientDetails.patientId,
                    "doctorId" : "",
                    "createdBy" : $scope.userId,
                    "createdDate" : "",
                    "updatedBy" : $scope.userId,
                    "updatedDate" : "",
                    "uhid" : "",
                    "patientDetails" : "",
                    "doctorDetails" : "",
                    "priorityId" : "",
                    "priorityName" : "",
                    "colorCode" : "",
                    "isOrderComplete" : "",
                    "orderStatusId":"1", //code to change
                    "genderId" :$scope.patientDetails.genderId,
                    "visitType":"",
                    "patientDetails":"",
                    "listLabSampleDetailsMaster" : $scope.listSampleDtls
                }
            }

            $scope.labSampleDtls = {
                "labSampleDtlsId" : "",
                "orgId" : $scope.orgId,
                "orgUnitId" : $scope.orgUnitId,
                "testId" : "",
                "deptId" : "",
                "subDeptId" : "",
                "deptName" : "",
                "labSampleId" : "",
                "sampleBarcode" : "",
                "sampleId" : "",
                "sampleNo" : "",
                "orderDetailsId" : "",
                "orderId" : "",
                "orderQty" : "",
                "serviceId" : "",
                "testCode" : "",
                "testDesc" : "",
                "sampleName" : "",
                "sampleVolume" : "",
                "unitName" : "",
                "profileId" : "",
                "panelCode":"",
                "packageId" : "",
                "isCentrifugationReq" : "",
                "isAlliquoteReq" : "",
                "sampleTypeId" : "",
                "containerId" : "",
                "containerName" : "",
                "sampleReqCount" : "",
                "samplePendingCount" : "",
                "sampleGenDatetime" : "",
                "sampleGenBy" : $scope.userId,
                "currStatus" : "",
                "sampleStatusId" : "",
                "isOutsourced" : "",
                "outsourcedLabId" : "",
                "sampleSendtocrDatetime" : "",
                "sampleSendtocrBy" : "",
                "sampleCollectionDatetime" : "",
                "sampleCollectionBy" : $scope.userId,
                "sampleCentrifugationDatetime" : "",
                "sampleCentrifugationBy" : "",
                "sampleAcceptDatetime" : "",
                "sampleAcceptBy" : "",
                "sampleRejectDatetime" : "",
                "sampleRejectBy" : "",
                "sampleRejectReasonId" : "",
                "sampleRejectReason" : "",
                "sampleRecollectFlag" : "",
                "sampleRecollectAgainstId" : "",
                "createdBy" : $scope.userId,
                "createdDate" : "",
                "updatedBy" : "",
                "updatedDate" : "",
                "sampleTestTime":"",
                "patientVisitAge":"",
                "genderId":"",
                "visitType":"",
                "listTMicroSampleMediaDto":[]

            }

            $scope.patientDetails = {
                "patientName" : "",
                "genderCode" : "",
                "age" : "",
                "uhidNumber" : "",
                "dob" : "",
                "doctorFullName" : "",
                "dateOfAdmission" : "",
                "patientDischargeDate" : "",
                "wardName" : "",
                "bedNumber" : "",
                "imagePath":"",
                "patientId":"",
                "visitTypeId":"",
                "admissionId":"",
                "genderId":"",
            }

            $scope.patientSearchObj = {
              "uhIdNumber"      : "",
              "patientName"     : "",
              "genderId"        : "",
              "mobileNo"        : "",
              "birthDate"       : "",
              "identificationNo":"",
              "patientType"   :"1",
              "unitId"          :$scope.unitId,
              "organizationId"  :$scope.organizationId
            }

            $scope.listLabResultEntryDtls  = [];
            $scope.labResultMasterDto = {
                "labTestResId": "",
                "orgId": $scope.orgId,
                "orgUnitId":$scope.orgUnitId,
                "testId": "",
                "deptId": $scope.deptId,
                "patientId": "",
                "visitTypeId": "",
                "visitAdmId": "",
                "orderDetailsId": "",
                "sampleNo": "",
                "labSampleDtlsId": "",
                "resultEnterDatetime": "",
                "resultEnterBy": "",
                "resultValidatedDatetime": "",
                "resultValidatedBy": "",
                "resultAuthorisedDatetime": "",
                "resultAuthorisedBy": "",
                "resultAuthorisedFlag": "",
                "resultHandoverDatetime": "",
                "resultHandoverBy": "",
                "suggetionNotes": "",
                "footsNotes": "",
                "internalNotes": "",
                "srfId": "",
                "icmrId": "",
                "wardCode":"",
                "bedNumber":"",
                "sampleType":"",
                "testCode":"",
                "createdBy": $scope.userId,
                "createdDate": "",
                "updatedBy": $scope.userId,
                "updatedDate": "",
                "sampleBarcode": "",
                "labSampleNo": "",
                "subDeptId": $scope.subDeptId,
                "visitType": "",
                "uhid": "",
                "patientDetails": "",
                "doctorDetails": "",
                "testDesc": "",
                "priorityName": "",
                "colorCode": "",
                "panelCode":"",
                "orderDateTime":"",
                "sampleCollectionDatetime":"",
                "panelId":"",
                "genderId":"",
                "resultLevel":"",
                "sampleStatusId" :"",
                "retestCount":"",
                "listLabSampleResultDetailsMaster":$scope.labResultDetailsMasterDtoDtls
            }

            $scope.labResultDetailsMasterDtoDtls={
                "labResDtlsId": "",
                "labResultId": "",
                "parameterId": "",
                "headerId": "",
                "orgId":$scope.orgId,
                "orgUnitId": $scope.orgUnitId,
                "testId": "",
                "paramName": "",
                "paramUnit": "",
                "sampleNo": "",
                "finalResult": "",
                "firstLevelResult": "",
                "secondLevelResult": "",
                "thirdLevelResult": "",
                "resultTypeFlag": "",
                "parameterMin": "",
                "paramAbnrmlMin": "",
                "parameterMax": "",
                "paramAbnrmlMax": "",
                "paramPrintOrder": "",
                "refrangeTypeId":"",
                "textualRangeId":"",
                "multitextaulRange":"",
                "textualRangeName":"",
                "machineId": "",
                "machineResult": "",
                "remarks": "",
                "isDeltaFlag": "",
                "infromationDtls": "",
                "informationType": "",
                "informedTo": "",
                "informedDatetime": "",
                "paramRecheckedBy": "",
                "parameterFormula": "",
                "isFormula":"",
                "paramPrecision":0,
                "deltaPer" :"",
                "deltaChange":"",
                "deltaDaysId":"",
                "createdBy": $scope.userId,
                "updatedBy": $scope.userId,
                "updatedDate": ""
            }

            $scope.getSexMasterList = function() {
                var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
                BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    $scope.sexMasterList = response.data.listObject;
                });
            }

            $scope.getIdentificationTypeList = function(){
                var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
                BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success")
                        $scope.identificationTypeList = response.data.listObject;
                });
            }

            $scope.getCardTypesList = function(){
                var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
                var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
                BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success"){
                        $scope.cardTypeList = response.data.listObject;
                    }
                });
            }

            $scope.initPaymentDetailsObj = function(){
                $scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:""});
            }

            $scope.getFormattedDate = function(date,isTime){
                var tempDate = new Date(date);
                var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
                var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
                var year = tempDate.getFullYear();
                if(isTime)
                    return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+tempDate.getSeconds();
                else
                    return date + "-" + month + "-" + year + " 00:00:00";
            }

            $rootScope.getSexMasterList();
            $rootScope.getIdentificationTypeList();

            $scope.onLoadCall = function(){
                $timeout( function(){
                    if($stateParams.searchObj!=undefined && $stateParams.searchObj!=null && $stateParams.searchObj.assignUnit!=null){
                        $scope.assignUnit = $stateParams.searchObj.assignUnit;
                        $scope.fromDate = $stateParams.searchObj.fromDate;
                        $scope.toDate = $stateParams.searchObj.toDate;
                        $scope.patientId = $stateParams.searchObj.patientId;
                        $scope.selectedPatient = $stateParams.searchObj.selectedPatient;
                        $scope.uhid = $stateParams.searchObj.uhid;
                        $scope.selectedDepartMentList = $stateParams.searchObj.selectedDepartMentList;
                        $scope.selectedVisitType = $stateParams.searchObj.selectedVisitType;
                        $scope.selectedtestList = $stateParams.searchObj.selectedtestList;
                        $scope.selectedSampleNo = $stateParams.searchObj.selectedSampleNo;
                    }
//                 $scope.initResultEntryList($scope.orgId,	$scope.orgUnitId,$scope.deptId,$scope.offset,$scope.noOfRecordsPerPage);
                },1000);
            }

            $scope.getDepartMentList = function(orgId,orgUnitId) {
                try {
                    var data = "";
                    var URI = BASE_URL + ROOT_URL
                            + LIS_COMMON + GET_DEPARMENT_LIST
                            + S + orgId + S + orgUnitId;
                    console.log("URI", URI);
                    $scope.departmentList = [];
                    GenericService.serviceAction("GET", URI, data).then(function(response) {
                        $scope.departmentListtest =  response.data.listObject;
                        var specDocObj = {
                            organizationId:$scope.organizationId,
                            userId:$scope.userId
                        }
                        var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
                        BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
                        NOTIFICATION_MSG_STATUS_FALSE)
                        .then(function(response) {
                            if (response.data.status == "success"){
                                $scope.staffSpecialityList = [];
                                $scope.staffSpecialityList = response.data.listObject;
                                $scope.staffSubSpecialityList = response.data.object;
                                if( $scope.staffSubSpecialityList != null &&  $scope.staffSubSpecialityList.length > 0){
                                    angular.forEach($scope.departmentListtest, function(value, key) {
                                        angular.forEach($scope.staffSubSpecialityList, function(value1, key1) {
                                            if(value.id == value1.subSpecialityMasterId){
                                                var multiselectObj = angular.copy($scope.multiselectObj);
                                                if($scope.consolidatedResultEntry=='Y'){
                                                    if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id){
                                                        multiselectObj.id = value.id;
                                                        multiselectObj.label = 	value.name;
                                                        $scope.departmentList.push(multiselectObj);
                                                    }
                                                }else{
                                                    if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id
                                                        && labConfig.biochemistryId!=value.id && labConfig.hematologyId!=value.id
                                                        && labConfig.serologyId!=value.id && labConfig.clinicalPathologyId!=value.id
                                                        && labConfig.cystopathologyId!=value.id){
                                                        multiselectObj.id = value.id;
                                                        multiselectObj.label = 	value.name;
                                                        $scope.departmentList.push(multiselectObj);
                                                    }
                                                }
                                            }
                                        });
                                    });
                                }else{
                                    angular.forEach($scope.departmentListtest, function(value, key) {
                                        var multiselectObj = angular.copy($scope.multiselectObj);
                                        if($scope.consolidatedResultEntry=='Y'){
                                            if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id){
                                                multiselectObj.id = value.id;
                                                multiselectObj.label = 	value.name;
                                                $scope.departmentList.push(multiselectObj);
                                            }
                                        }else{
                                            if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id
                                                && labConfig.biochemistryId!=value.id && labConfig.hematologyId!=value.id
                                                && labConfig.serologyId!=value.id && labConfig.clinicalPathologyId!=value.id
                                                && labConfig.cystopathologyId!=value.id){
                                                multiselectObj.id = value.id;
                                                multiselectObj.label = 	value.name;
                                                $scope.departmentList.push(multiselectObj);
                                            }
                                        }
                                        });
                                }
                                $scope.onLoadCall();
                            }
                        });
                    });
                   } catch (e) {
                    console.log(e.message)
                }
            }
            $scope.getDepartMentList($scope.orgId,$scope.orgUnitId);


            $scope.searchPatient = function()
            {
                $rootScope.startLoader();
                $scope.DischargeSummaryFinalList=[];
                $scope.patientTemplateList=[];
                if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
                {
                    growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
                    $scope.patientList = [];
                    $rootScope.stopLoader();

                    return false;
                }
                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
                var patSearchObj = angular.copy($scope.patientSearchObj);
                if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
                    var dt = new Date($scope.patientSearchObj.birthDate);
                    patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" +  dt.getFullYear();
                }
                BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    $scope.searchPatientList = [];
                    if (response.data.status == "success")
                        {
                        $scope.patientList = response.data.listObject;
                        patSearchObj = {};

                        $scope.initPaymentDetailsObj();
                        $scope.listPaymentDetails = [];
                        $scope.patientDetailsList = [];
                        $scope.patientDetails = {};
                        $scope.patientSearchObj.birthDate="";
                        $rootScope.stopLoader();
                        }
                    else if(response.data.listObject==null){
                        growl.warning(response.data.message,{
                            title : ERROR_MSG
                        });
                        $scope.patientSearchObj.birthDate="";
                        $scope.patientList=[];
                        return false;
                    }
                    else{
                        $rootScope.stopLoader();
                    }

                });
            }
            $scope.setFirstTabLevel = function(newTab) {
                $scope.firstTabLevel = newTab;
//								if (newTab == '2') {
//								}
//								if (newTab == '1') {
//								}
            };

            $scope.isSetFirstTabLevel = function(tabNum) {
                return $scope.firstTabLevel === tabNum;

            };

            $scope.admissionId=null;
            $scope.departmentId=null;
            $scope.patientId=null;
            $scope.multiselectObj = {
                "id":"",
                "label":""
            }
            $scope.departmentList = [];

            $scope.getPatientPOCOrderedServices = function(){
                $rootScope.startLoader();
                try {

                    $scope.selectedResEntryList = [];
                    $scope.resultEntryWorklist = [];
//                    offset = offset != null ? offset : 0;
//                    noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 50;
                    $rootScope.startLoader();
//                    var patientIds = [];
//                    patientIds.push($scope.admissionObj.patientId == undefined ? null: $scope.admissionObj.patientId);
                    var data = {
                        "orgId":$scope.orgId,
                        "orgUnitId" :$scope.orgUnitId,
                        "assignUnit" :$scope.orgUnitId ,
//                        "offset":offset,
//                        "recordPerPage":noOfRecordsPerPage,
                        "deptId":$scope.deptId,
//                        "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
//                        "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
                        "patientId" :  $scope.admissionObj.patientId == undefined ? null: $scope.admissionObj.patientId,
                        "labSampleDtlsId" : $scope.labSampleDtlsId==undefined ? $scope.labSampleDtlsId:0,
//                        "visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
                        "testTypes" : $scope.selectedtestList == undefined?null :$scope.selectedtestList,
                        "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
                        "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ? "":$scope.uhid,
                        "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
                        "isSendToCrApplicable":$scope.isSendToCrApplicable,
                        "isAcceptanceInDepartment":$scope.isAcceptanceInDepartment,
                        "isSampleAcceptanceApplicable":$scope.isSampleAcceptanceApplicable,
                        "departmentList":$scope.departmentList,
                        "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
                        "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
                        "userId": $scope.userId
                    }

                    var URI = BASE_URL + ROOT_URL
                            + LIS_TRANSACTION
                            + GET_PATIENT_POC_ORDERED_SERVICES ;
                    console.log("GET_PATIENT_POC_ORDERED_SERVICES", URI);
                    GenericService
                            .serviceAction("POST", URI, data)
                            .then(
                                    function(response)
                                    {
                                        $scope.sampleCollectionList=[];
                                        $scope.sampleCollectionList = response.data.listObject;
                                        angular .forEach( $scope.sampleCollectionList, function(value, key) {

                                            value.performTestId  = value.testId;
                                            if(value.assignUnit == null){
                                                value.assignUnit  = value.orgUnitId;
                                            }

                                         });
                                        $rootScope.stopLoader();

                                    });
                    $rootScope.stopLoader();
                } catch (e) {
                    console.log(e.message)
                }
            }

//==============For fetching past test result history for second tab ==============================
            $scope.getValidationWorklist = function() {
                try {
                    $rootScope.startLoader();
//                    offset = offset != null ? offset : 0;
//                    noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:50;

                    var data = {
                            "orgId":$scope.orgId,
                            "orgUnitId" :$scope.orgUnitId,
                            "assignUnit" :$scope.orgUnitId ,
//                            "offset":offset,
//                            "recordPerPage":noOfRecordsPerPage,
                            "deptId":$scope.deptId,
//                            "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
//                            "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
                            "patientId" :  $scope.admissionObj.patientId == undefined ? null: $scope.admissionObj.patientId,
                            "labSampleDtlsId" : $scope.labSampleDtlsId==undefined ? null:$scope.labSampleDtlsId,
//                            "visitTypes" :$scope.selectedVisitType==undefined?null :$scope.selectedVisitType,
                            "testTypes" : $scope.selectedtestList == undefined?null :$scope.selectedtestList,
                            "subDepts" : ($scope.selectedDepartMentList!=undefined && $scope.selectedDepartMentList.length>0) ? $scope.selectedDepartMentList: $scope.departmentList,
                            "uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
                            "sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
                            "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
                            "isOnHoldParameter":  ($scope.isOnHoldParameter!=undefined && $scope.isOnHoldParameter!=null) ? $scope.isOnHoldParameter:false,
                            "userId" : $scope.userId,
                            "checkForIsPOC" : true,

                        };
                    var URI = BASE_URL + ROOT_URL+ "/LIS/BioChemistry/consolidated/Validation/Search";
                    GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
                        $rootScope.stopLoader();
                        $scope.resultEntryworklistForValidation = [];
                        $scope.resultEntryworklistForValidationtest = [];
                        $scope.resultEntryworklistForValidationtest  = response.data.listObject;

                        if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
                            angular.forEach($scope.resultEntryworklistForValidationtest, function(value, key) {
                                angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
                                    if(value.subDeptId == value1.subSpecialityMasterId){
                                        $scope.resultEntryworklistForValidation.push(value);
                                    }

                                });
                             });
                        }else{
                            $scope.resultEntryworklistForValidation  = response.data.listObject;
                        }
                        if($scope.resultEntryworklistForValidation !== undefined && $scope.resultEntryworklistForValidation !== null && $scope.resultEntryworklistForValidation.length>0){
                            angular.forEach($scope.resultEntryworklistForValidation, function(value, key) {
                                value.disableIntervention = false;
                                value.disableInterventionCompletion = false;
                                value.showInterventionCompletion = true;
                                if(value.clinicalInterventionProcedureId !== undefined && value.clinicalInterventionProcedureId !== null && value.clinicalInterventionProcedureId > 0){
                                     if($scope.nurseRole === 1){
                                        value.disableIntervention = true;
                                        value.disableInterventionCompletion = true;
                                        value.showInterventionCompletion = false;
                                     }
                                     if($scope.doctorRole === 1){
                                        value.disableIntervention = true;
                                        value.disableInterventionCompletion = false;
                                        value.showInterventionCompletion = true;
                                     }
                                     if(value.interventionCompletedDoctorId !== undefined && value.interventionCompletedDoctorId !== null && value.interventionCompletedDoctorId >0){
                                        value.disableIntervention = true;
                                        value.disableInterventionCompletion = true;
//                                        value.showInterventionCompletion = true;
                                     }
                                }
                                else if($scope.nurseRole === 1){
                                    value.disableIntervention = false;
                                    value.disableInterventionCompletion = true;
                                    value.showInterventionCompletion = false;
                                }
                                else if($scope.doctorRole === 1){
                                    value.showInterventionCompletion = true;
                                    value.disableInterventionCompletion = false;
                                    value.disableIntervention = true;
                                }
                            });
                        }

                        $scope.resultEntryworklistForValidationForFiltter = $scope.resultEntryworklistForValidation ;
                        $scope.filterByDummyServices();

                    });
                } catch (e) {
                    console.log(e.message)
                }

            }

            $scope.filterByDummyServices= function()
            {
                $scope.resultEntryworklistForValidation  =[]
                if($scope.filterDummyService==1)//all
                {
                    $scope.resultEntryworklistForValidation = angular.copy($scope.resultEntryworklistForValidationForFiltter);

                }else if($scope.filterDummyService==2){//real
                    angular.forEach($scope.resultEntryworklistForValidationForFiltter, function (value, key)
                    {
                        if(value.isDummyService==false)
                            $scope.resultEntryworklistForValidation.push(value);
                    });


                }else if($scope.filterDummyService==3 ){ //Dummy
                    angular.forEach($scope.resultEntryworklistForValidationForFiltter, function (value, key)
                    {
                      if(value.isDummyService==true)
                          $scope.resultEntryworklistForValidation.push(value);
                    });

                }
            }

            $scope.getPatientDetailsById = function(patientId){
                $rootScope.startLoader();
                $scope.patientId=patientId;
                $scope.patientDetailsList = [];
                $scope.patientDetails = {};
                $scope.masterObj = {};
                $scope.DischargeSummaryFinalList=[];
                $scope.patientTemplateList=[];

                var patientO =  $scope.patientList.find(x => x.patientId ==patientId);
                $scope.patientDetails.age=patientO.age;
                $scope.admissionObj.patientId =patientO.patientId;
                 $scope.admissionObj.admissionId =patientO.admissionId;
                var admissionId = $.grep($scope.patientList, function (doc) {
                    return doc.patientId == patientId;
                })[0].admissionId;

                if(admissionId>0)
                {
                    self.billing = false;
                    $scope.admissionObj ={};
                    $scope.admissionObj.admissionId = admissionId;
                    $scope.admissionObj.patientId = patientId;
                    $rootScope.startLoader();
                    var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+admissionId;
                        BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                            if(response.data.status == "success")
                            {
                                $scope.admissionDetails = response.data.object;


                                $scope.patientDetails.patientName = $scope.admissionDetails.patientName;
                                $scope.patientDetails.uhidNumber= $scope.admissionDetails.uhidNumber;
                                $scope.patientDetails.genderCode= patientO.genderCode;
                                $scope.patientDetails.dob= $scope.admissionDetails.dob;
                                $scope.patientDetails.dobNew = $scope.admissionDetails.dob;
                                $scope.patientDetails.age= $rootScope.getAge($scope.admissionDetails.dob);
                                $scope.patientDetails.doctorFullName= $scope.admissionDetails.doctorName;
                                $scope.patientDetails.dateOfAdmission= $scope.admissionDetails.doa;

                                var dateParseDOA = $scope.patientDetails.dateOfAdmission.split('/');
                                var dateObjectDOA = new Date(dateParseDOA[2], dateParseDOA[1] - 1, dateParseDOA[0]);
                                var longValueDOA = dateObjectDOA.getTime();
                                $scope.patientDetails.dateOfAdmission = longValueDOA;
    //		                                        	moment(longValueDOA).format($scope.dateFormat.toUpperCase());

                                $scope.patientDetails.patientDischargeDate= $scope.admissionDetails.pdd;

                                var dateParsePDD = $scope.patientDetails.patientDischargeDate.split('/');
                                var dateObjectPDD = new Date(dateParsePDD[2], dateParsePDD[1] - 1, dateParsePDD[0]);
                                var longValuePDD = dateObjectPDD.getTime();
                                $scope.patientDetails.patientDischargeDate = longValuePDD;

                                $scope.patientDetails.wardName= $scope.admissionDetails.wardNo;
                                $scope.patientDetails.bedNumber= $scope.admissionDetails.bedNo;
                                $scope.patientDetails.imagePath= "";
                                $scope.patientDetails.patientId= patientId;
                                $scope.patientDetails.visitTypeId= $scope.admissionDetails.visitTypeId;
                                $scope.patientDetails.admissionId= $scope.admissionDetails.admissionId;
                                $scope.patientDetails.genderId= $scope.admissionDetails.genderId;
                                $rootScope.stopLoader();
                                $scope.initLabSampleMaster();
                                $scope.getPatientPOCOrderedServices();
                                $scope.getValidationWorklist();
                            }
                            else if(response.data.status == "error")
                                {
                                  $rootScope.stopLoader();
                                }
                        });
                }
                else{
                     $scope.patientDetails.patientName = patientO.patientName;
                     $scope.patientDetails.genderCode= patientO.genderCode;
                     $scope.patientDetails.uhidNumber= patientO.uhIdNumber;
                     $scope.patientDetails.dob=  moment(patientO.birthDate,'DD/MM/YYYY').format('DD-MM-YYYY');
                     $scope.patientDetails.genderId  = patientO.genderId;
                     $scope.sampleCollectionList=[];
                     $rootScope.stopLoader();
                    $scope.billing = true;
                   }


            }

            $scope.comorbidityFlag = false;
            $scope.notifiableFlag = false;

            if((cookieObjectAppConfigSecond != undefined)&&(cookieObjectAppConfigSecond.pocWorklistObj !== undefined)&&(!angular.equals({},cookieObjectAppConfigSecond.pocWorklistObj))){
                self.billing = false;
                $scope.patientDetails = {};
                $scope.admissionObj = cookieObjectAppConfigSecond.pocWorklistObj;
                $scope.patientDetails.patientName = $scope.admissionObj.pFirstName !== undefined && $scope.admissionObj.pFirstName !== null && $scope.admissionObj.pFirstName !== ""? $scope.admissionObj.pFirstName : $scope.admissionObj.patientName;
                $scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
                $scope.patientDetails.age= $scope.admissionObj.dob !== undefined && $scope.admissionObj.dob !== null ? moment().diff($scope.admissionObj.dob, 'years') : moment().diff($scope.admissionObj.birthDate, 'years');
                $scope.patientDetails.uhidNumber= $scope.admissionObj.uhidnumber !== undefined && $scope.admissionObj.uhidnumber !== null && $scope.admissionObj.uhidnumber !== "" ? $scope.admissionObj.uhidnumber : $scope.admissionObj.uhIdNumber;
                $scope.patientDetails.dob= $scope.admissionObj.dob !== undefined && $scope.admissionObj.dob !== null && $scope.admissionObj.dob !== "" ? $scope.admissionObj.dob : $scope.admissionObj.birthDate;
                $scope.patientDetails.dobNew= $scope.admissionObj.dob !== undefined && $scope.admissionObj.dob !== null && $scope.admissionObj.dob !== "" ? $scope.admissionObj.dob : $scope.admissionObj.birthDate;
                $scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName;
                $scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa !== undefined && $scope.admissionObj.doa !== null && $scope.admissionObj.doa !== "" ? $scope.admissionObj.doa : $scope.admissionObj.dateOfAdmission;
                $scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd !== undefined && $scope.admissionObj.pdd !== null && $scope.admissionObj.pdd !== "" ? $scope.admissionObj.pdd : $scope.admissionObj.patientDischargeDate;
                $scope.patientDetails.wardName=  $scope.admissionObj.wardName !== undefined && $scope.admissionObj.wardName !== null && $scope.admissionObj.wardName !== "" ? $scope.admissionObj.wardName : $scope.admissionObj.wardNameCover;
                $scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
                $scope.patientDetails.imagePath= "";
                $scope.patientDetails.patientId= $scope.admissionObj.patientId;
                $scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
                $scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
                $scope.patientDetails.genderId= $scope.admissionObj.genderId;

                if($scope.patientDetails.doctorFullName!== undefined && $scope.patientDetails.doctorFullName !== null && $scope.patientDetails.doctorFullName !== "" && $scope.patientDetails.doctorFullName.includes("Dr.")){
                    $scope.patientDetails.doctorFullName = $scope.patientDetails.doctorFullName.replace("Dr.","");
                }

                cookieObjectAppConfigSecond.pocWorklistObj = {};
                localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));

                $scope.initLabSampleMaster();
                $scope.getPatientPOCOrderedServices();
                $scope.getValidationWorklist();
            }
            $scope.labSampleDetails = {};

            $scope.checkAll = function() {
                if ($scope.selectedAll) {
                    $scope.selectedAll = true;
                    $scope.isItemChecked = true;
                } else {
                    $scope.selectedAll = false;
                    $scope.isItemChecked = false;
                }
                angular.forEach($scope.sampleCollectionList,function(labDetailsObj) {
                    if(labDetailsObj.sampleBarcode==null)
                    {
                        labDetailsObj.sampleStatusId = $scope.selectedAll;
                    }
                });
            }

            Array.prototype.groupBy = function(prop) {
                return self.reduce(function(groups, item) {
                const val = item[prop]
                groups[val] = groups[val] || []
                groups[val].push(item)
                return groups
                }, {})
            }

            $scope.clearDetails=function(){
                $scope.fromTimeString = "";
                $scope.fromTimeStringSlot="";
                $scope.slotId="";
                $scope.modalityId="";
                $scope.slotDetailsList=[];
                $scope.loadingMsg="";
            }

            $scope.selectedResEntryList = [];
            $scope.nextSerologyRecords = [];
            $scope.nextOtherDeptRecords = [];
            $scope.selectedIndex = [];

            $scope.sendToResultEntry= function(record){
                var resultEntryRecord = {};
                if(record === undefined || record === null || record === ''){
                    return false;
                }
                else{
                    resultEntryRecord = record;
                }

                $scope.selectedResEntryList = [];
                var subId = 0;
                subId = resultEntryRecord.subDeptId;
                if(labConfig.serologyId == resultEntryRecord.subDeptId){
                 //serology
                    $scope.nextSerologyRecords.push(resultEntryRecord);
                }else if(labConfig.serologyId !== resultEntryRecord.subDeptId){
                 // other dept
                    $scope.nextOtherDeptRecords.push(resultEntryRecord);
                }

                 var searchObj = {};
                 searchObj.assignUnit = $scope.orgUnitId;
                 searchObj.fromDate = moment(new Date());
                 searchObj.toDate = moment(new Date());
                 searchObj.patientId = $scope.patientId;
                 searchObj.selectedPatient = $scope.selectedPatient;
                 searchObj.uhid = $scope.patientDetails.uhidNumber;
                 searchObj.selectedDepartMentList = $scope.selectedDepartMentList;
                 searchObj.selectedVisitType = 4;
                 searchObj.selectedtestList = $scope.selectedtestList;
                 searchObj.selectedSampleNo = $scope.selectedSampleNo;

                if(resultEntryRecord.subDeptId != labConfig.serologyId ){

                    $scope.finalOtherDeptRecords = [];
                    if($scope.nextOtherDeptRecords!=undefined || $scope.nextOtherDeptRecords!=null){
                        angular.forEach($scope.nextOtherDeptRecords,function(val,idx){
                                var newArr = {
                                        subDeptId : "",
                                        resultMstList : [],
                                        searchObj : {}
                                };
                                    newArr.subDeptId  = val.subDeptId,
                                    newArr.resultMstList.push(val) ,
                                    newArr.searchObj = searchObj;
                                    $scope.finalOtherDeptRecords.push(newArr);
                                    $scope.selectedResEntryList.push(val);

                        });
                    }

                    var count = 0;
                     var patientIds = [];
                     var flag = false;
                    angular.forEach($scope.selectedResEntryList, function(value, key)
                    {
                        value.orgId = value.orgId;
                        value.orgUnitId = value.orgUnitId;
                        value.createdBy = $scope.userId;
                        value.listLabSampleResultDetailsMaster = [];
                        patientIds.push(value.patientId);

                        count++;
                        if(new Set(patientIds).size==1 && $scope.selectedResEntryList.length==count)
                        {
                         flag =true;
                         $timeout(function () {
                             $rootScope.stopLoader();
                             $state.go('deatilsReportEntryWorklist',
                                        {
                                          subDeptId : $scope.selectedResEntryList[0].subDeptId,
                                          resultMstList : $scope.selectedResEntryList,
                                          searchObj : searchObj,
                                          nextRecordList : $scope.finalOtherDeptRecords
                                        });

                             }, 2000);

                         }
                    });
                    if(!flag)
                    {
                          growl
                            .warning("Please select Unique Patient tests.",
                                    {
                                        title : 'Warning!'
                                    });
                          $rootScope.stopLoader();
                    }
                }else if($scope.selectedResEntryList[0].subDeptId == labConfig.serologyId){
                    $scope.finalSeroDeptRecords = [];
                    if($scope.nextSerologyRecords!=undefined || $scope.nextSerologyRecords!=null){
                        angular.forEach($scope.nextSerologyRecords,function(val,idx){
                            var newArr = {
                                    subDeptId : "",
                                    resultMstList : [],
                                    searchObj : {}
                            };
                                    newArr.resultMstList.push(val) ,
                                    newArr.searchObj = searchObj;
                                    $scope.finalSeroDeptRecords.push(newArr);
                        });
                    }

                    var count = 0;
                     var patientIds = [];
                     var flag = false;
                    angular.forEach($scope.selectedResEntryList, function(value, key) {
                        value.orgId = value.orgId;
                        value.orgUnitId = value.orgUnitId;
                        value.createdBy = $scope.userId;
                        value.listLabSampleResultDetailsMaster = [];
                        patientIds.push(value.patientId);

                        count++;
                        if(new Set(patientIds).size==1 && $scope.selectedResEntryList.length==count)
                        {
                            flag =true;
                        }
                    });
                    if(!flag)
                    {
                          growl
                            .warning("Please select Unique Patient tests.",
                                    {
                                        title : 'Warning!'
                                    });
                          $rootScope.stopLoader();
                          return false;
                    }
                     $timeout(function () {
                            $state.go('detailsSerologyReportEntry',
                                    {
                                      resultMstList : $scope.selectedResEntryList,
                                      searchObj : searchObj,
                                      nextRecordList : $scope.finalSeroDeptRecords
                                    });

                         }, 2000);
                }
            }

            $scope.selectedValEntryList = [];
            $scope.generateResultValidationUrl = function(record)
            {
                if(record === undefined || record === null || record === ''){
                    return false;
                }
                else{
                    $scope.selectedValEntryList.push(record);
                }

                var subId = 0;

                var searchObj = {};
                searchObj.assignUnit = $scope.orgUnitId;
                searchObj.fromDate = moment(new Date());
                searchObj.toDate = moment(new Date());
                searchObj.patientId = $scope.patientId;
                searchObj.selectedPatient = $scope.selectedPatient;
                searchObj.uhid = $scope.patientDetails.uhidNumber;
                searchObj.selectedDepartMentList = $scope.selectedDepartMentList;
                searchObj.selectedVisitType = 4;
                searchObj.selectedtestList = $scope.selectedtestList;
                searchObj.selectedSampleNo = $scope.selectedSampleNo;
                $scope.subDeptId = record.subDeptId;

                $rootScope.stopLoader();
                $scope.labResultMasterList = $scope.selectedValEntryList;
                $scope.getBiochemResulValidationDetails();

            }


            $scope.getBiochemResulValidationDetails = function(){
                var resultEntry = $scope.labResultMasterDto;
                try {
                    $rootScope.startLoader();
                    $scope.labResultDetailsMasterDtoDtlsList =[];
                     var dataList = [];
                    angular.forEach($scope.labResultMasterList, function(value, key) {

                    var resultEntry = value;

                    $scope.testType= resultEntry.testType;
                    $scope.patientId  = resultEntry.patientId;
                    $scope.labSampleDtlsId  =resultEntry.labSampleDtlsId;
                    var data = {
                        "orgId":resultEntry.orgId,
                        "orgUnitId":$scope.orgUnitId,
                        "testId":resultEntry.testId,
                        "testType":resultEntry.testType,
                        "deptId":resultEntry.deptId,
                        "subDeptId":resultEntry.subDeptId,
                        "patientAgeDays":resultEntry.patientAgeDays,
                        "labSampleNo":resultEntry.sampleNo,
                        "genderId":resultEntry.genderId,
                        "panelId":resultEntry.panelId,
                        "labResultId":resultEntry.labTestResId,
                        "assingnedFrom":resultEntry.orgUnitId,
                        "labSampleDtlsId":resultEntry.labSampleDtlsId
                    };
                       dataList.push(data);
                    });
                    var URI = BASE_URL + ROOT_URL
                            + LIS_BIOCHEMISTRY
                            + GET_WORK_LIST_VALIDATION_ENTERY_DETAILS;
                    GenericService.serviceAction(METHOD_POST, URI, dataList).then(
                        function(response) {
                            $rootScope.stopLoader();
                            $scope.testMasterResList =[];
                            $scope.testDescData=[];
                            $scope.testMasterResList = response.data.listObject;
                            angular.forEach($scope.testMasterResList,function(value,key){
                                $scope.testDescData.push(value.testDesc)
                            });
                            angular.forEach($scope.testMasterResList, function(value, key) {
                                value.isSelected = true;
                                var labResultMaster = $scope.labResultMasterList.find(obj => {return obj.labSampleDtlsId == value.labSampleDtlsId})
                                value.sampleRecollectAgainstId =labResultMaster.sampleRecollectAgainstId;
                                value.isRecollected =labResultMaster.sampleRecollectFlag=='Y'?true:false;
                                value.isRetestRecollected = (labResultMaster.sampleRecollectFlag=='Y'||labResultMaster.retestCount>0)?true:false;
//                                angular.forEach($scope.testMasterResList, function(value, key) {
//                                    $scope.getPreviousDeltaReport(value.testId,value.testType,value.labSampleDtlsId,key);
//                                });
                                if(value.labResultDetailsViewDto!=null){
                                     if(value.labResultDetailsViewDto.isAbnormal == null || value.labResultDetailsViewDto.isAbnormal == undefined){
                                            value.labResultDetailsViewDto.isAbnormal = 'N';
                                     }
                                    value.isAbnormal = value.labResultDetailsViewDto.isAbnormal;
                                    $scope.changeArrowPosition(key,0,0,value.testType,1,false);
                                }
                            });

                             // sneha code
                            angular.forEach($scope.testMasterResList, function(value, testIndex)
                            {
                                angular.forEach(value.listMultyParamTestHeaderDto, function(paramInHeaderObj, headerIndex)
                                {
                                    angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(labResultDetails, resultIndex)
                                    {
                                        labResultDetails.tabIndex = 'finalResult_' + testIndex + '_' + headerIndex + '_' + resultIndex;
                                    });
                                });
                            });
                            $('#pocWorklistModal').modal('show');


                    });
                } catch (e) {
                    $rootScope.stopLoader();
                    console.log(e.message)
                }
            }

            $scope.previousTestResultList = [];
            $scope.getPreviousDeltaReport = function(testId,testType,labSampleDtlsId,key) {
                var promise =   $scope.getPreviousResultId(testId,testType,labSampleDtlsId);
                promise.then(function(resultId) {
                    if(resultId>0) {
                        try {
                            var previousTestResultList = [];
                            $rootScope.startLoader();
                            var data = {
                                 "orgId":$scope.orgId,
                                 "orgUnitId":$scope.orgUnitId,
                                 "deptId": $scope.deptId,
                                 "testId":testId,
                                 "testType":testType,
                                 "labResultId":$scope.resultEntryId,
                                 "subDeptId":$scope.subDeptId,
                                 "labSampleDtlsId":""
                            }

                            var URI = BASE_URL + ROOT_URL
                                    + LIS_BIOCHEMISTRY
                                    + PRIVOUSRESULTBYSAMPLE_DETAILS;

                            GenericService.serviceAction(METHOD_POST, URI, data).then(
                                function(response)
                                {
                                    if(response.data.status=="sucess")
                                    {
                                     $rootScope.stopLoader();
                                     previousTestResultList  = response.data.listObject!=null?response.data.listObject:[];
                                     $scope.testMasterResList[key].previousTestResultList =previousTestResultList;
                                    }
                                else{
                                    $scope.testMasterResList[key].previousTestResultList =[];
                                       $rootScope.stopLoader();
                                    }
                                });
                        } catch (e) {
                            console.log(e.message)
                        }
                    }
                    else{
                        $scope.testMasterResList[key].previousTestResultList =[];
                    }
                });
            }

            $scope.getPreviousResultId = function(testId,testType,labSampleDtlsId)
            {
                try {
                    $rootScope.startLoader();
                    var data = {
                          "labSampleDtlsId" : "",
                          "orgId" :$scope.orgId,
                          "orgUnitId" :$scope.orgUnitId,
                          "offset" : "",
                          "deptId" : $scope.deptId,
                          "recordPerPage" : "",
                          "testId" : testId,
                          "testType" :  testType,
                          "patientAgeDays" : "",
                          "panelId" : "",
                          "genderId" : "",
                          "labResultId" : "",
                          "headerId" : "",
                          "subDeptId" : $scope.subDeptId,
                          "labSampleNo" : "",
                          "patientId" : $scope.patientId
                    }
                    var URI = BASE_URL + ROOT_URL
                        + LIS_BIOCHEMISTRY
                        + GET_RESULT_ID_BY_TEST;
                    console.log("URI", URI);
                    console.log("data",JSON.stringify(data));
                    return GenericService.serviceAction(METHOD_POST, URI, data).then(
                        function(response) {
                            $rootScope.stopLoader();
                            return $scope.resultEntryId  = response.data.object;
                        });
                } catch (e) {
                    console.log(e.message)
                }
            }


            $scope.changeArrowPosition  = function(testIndex,headerIndex,resultIndex,testType,flag,isEditable,id2) {

                if(testType==0){
                    if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == undefined || $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == null || $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == "")
                    {
                        if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.refrangeTypeId ==REFERENCE_VALUE){
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.arrowClass  = '';
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.isAbnormal  = 'N';
                        }
                    }
                    if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE){
                        if( $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax !=null &&  $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult > $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax) {
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = 'fa fa-arrow-up color-red';
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'Y';
                        }else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin !=null &&  $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult < $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin){
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = 'fa fa-arrow-down color-red';
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'Y';
                        }else{
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N';
                        }
                    }
                    else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==TEXTUAL_RANGE){
                        var finalResult = $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.trim().toLowerCase();
                        var textualRangeName = $scope.testMasterResList[testIndex].labResultDetailsViewDto.textualRangeName.trim().toLowerCase();
                        console.log("finalResult.localeCompare(textualRangeName)",finalResult.localeCompare(textualRangeName));
                        if(finalResult===textualRangeName)
                        {
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N';
                        }
                        if(flag==1){
                            growl.warning("Please select the Result values from  Help values ",
                            {
                                title : 'Warning!'
                            });
                            return false;
                            $rootScope.stopLoader();
                        }
                    }
                    else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==MULTIVALUED_RANGE)
                    {
                        $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
                    }
                    if($scope.testMasterResList[testIndex].previousTestResultList!=undefined && $scope.testMasterResList[testIndex].previousTestResultList.length>0){

                        var deltaChange = $scope.calculateDeltaPercentage($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult,
                               $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.finalResult,
                               $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.refrangeTypeId,
                               $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.deltaDaysId,
                               $scope.testMasterResList[testIndex].testMasterResList[testIndex].previousTestResultList[testIndex].resultAuthorisedDatetime


                        );
                        if((deltaChange>$scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaPer)||(deltaChange==-1))
                        {
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.isDeltaFlag = 'Y';
                            if($scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE)
                            {
                                $scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaChange = deltaChange;
                            }

                        }
                        else{
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.isDeltaFlag = 'N';
                            $scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaChange = 0;
                        }
                    }
                }
                else if(testType==1){
                    if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == undefined || $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == null || $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == "")
                    {

                     if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==REFERENCE_VALUE){
                           $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
                           $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
                     }

                    }

                    if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==REFERENCE_VALUE)
                    {
                     if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult > $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax) {
                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = 'fa fa-arrow-up color-red';
                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'Y';
                    }else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMin !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult < $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMin){
                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = 'fa fa-arrow-down color-red';
                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'Y';
                    }else{
                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
                    }

                    }
                    else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null
                        && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==TEXTUAL_RANGE)
                    {
                     var finalResult = $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult.trim().toLowerCase();
                     var textualRangeName = $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].textualRangeName.trim().toLowerCase();
                    if(finalResult===textualRangeName)
                       {
                       $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
                       }
                    else{
                    }
                    if(flag==1){
                       growl.warning("Please select the Result values from  Help values ",
                                {
                                    title : 'Warning!'
                                });
                       return false;
                       $rootScope.stopLoader();
                      }
                    }
                    else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null
                       && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==MULTIVALUED_RANGE)
                    {

                    if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal == undefined ||
                            $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal == null){
                                $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
                                $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  ='N';
                    }
                    }
                    if($scope.testMasterResList[testIndex].previousTestResultList!=undefined && $scope.testMasterResList[testIndex].previousTestResultList.length>0)
                    {


                    var deltaChange =  $scope.calculateDeltaPercentage(
                           $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult,
                           $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult,
                           $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId,
                           $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].deltaDaysId,
                           $scope.testMasterResList[testIndex].previousTestResultList[testIndex].resultAuthorisedDatetime);

                    if((deltaChange> $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].deltaPer)||(deltaChange==-1))
                       {
                         $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isDeltaFlag = 'Y';
                       }
                    else
                    {
                       $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isDeltaFlag = 'N';
                    }
                    }

                    if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId==REFERENCE_VALUE){

                    if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult==undefined ||
                            $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult==null ||
                            $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult== "" ){

                    }else{

                        var factor = Math.pow(10, $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].paramPrecision);
                        var finalResult = parseFloat($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult);
                        var rounded = Math.round(finalResult * factor) / factor;
                        $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult = rounded.toFixed($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].paramPrecision);
                    }

                    }

                    if( $scope.override == false){
                    if(isEditable == null || isEditable == 'N'){
                    angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(headerObj, key)
                        {
                              angular.forEach(headerObj.listLabResultDetailsViewDto, function(labResObj, key)
                                {
                                   if(labResObj.isFormula=='Y')
                                       {
                                      // labResObj.finalResult=null;
                                       var formula  = labResObj.parameterFormula;
                                       var simplified = math.simplify(math.parse(formula));
                                       var formulaDetailsList = labResObj.listFormulaDetails;
                                        var obj = {};
                                        var testList = [];
                                        var checkParameter = false;
                                          angular.forEach(formulaDetailsList, function(value, key)
                                           {

                                              angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(paramInHeaderObj, key)
                                                        {
                                                            angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(paramObj, key)
                                                              {
                                                                if(paramObj.parameterId == value.parameterId )
                                                                       {
                                                                           if(paramObj.finalResult==null||paramObj.finalResult==""||paramObj==undefined)
                                                                           {
                                                                               paramObj.finalResult=null;
                                                                               checkParameter = true;
                                                                           }
                                                                           else if((paramObj.refrangeTypeId == 1) && (paramObj.paramPrecision !== undefined && paramObj.paramPrecision !== '' && paramObj.paramPrecision !== null)){
                                                                                var factor = Math.pow(10, paramObj.paramPrecision);
                                                                                var rounded = Math.round(parseFloat(paramObj.finalResult) * factor) / factor;
                                                                                paramObj.finalResult = rounded.toFixed(paramObj.paramPrecision);
                                                                           }
                                                                           obj[value.paramVariable.toString()] = paramObj.finalResult==null?0.00:parseFloat(paramObj.finalResult);
                                                                       }
                                                            });
                                                        });

                                           });
                                          var result = simplified.eval(obj);
                                          if(result==0.00 || isNaN(result)){
                                              if(labResObj.finalResult == undefined ||labResObj.finalResult == null || labResObj.finalResult == ""){
                                                  if(checkParameter == true){
                                                      labResObj.finalResult==null;
                                                  }else{
                                                      labResObj.finalResult=0.00;
                                                  }
                                              }
                                              else
                                              {
                                              labResObj.finalResult=0.00;
                                              }
                                          }else{
                                              labResObj.finalResult =result.toFixed(labResObj.paramPrecision);
                                          }

                                          if(labResObj.finalResult==Infinity){
                                              labResObj.finalResult= String;
                                              labResObj.finalResult = "";
                                           }
                                       }

                                });
                        });
                    }
                    }
                 };

            }

            $scope.interventionRecord = {};
            $scope.interventionCompletionRecord = {};
            $scope.interventionRecordIndex = undefined;
            $scope.interventionConfirmationPopUp = false;
            $scope.interventionCompletionPopUp = false;
            $scope.openInterventionConfirmation = function(record, index){
                if(!$scope.interventionConfirmationPopUp){
                    if(record.isClinicalInterventionRequired !== undefined && record.isClinicalInterventionRequired !== null && record.isClinicalInterventionRequired ==='Y'){
                        record.isClinicalInterventionRequired = 'Y';
                        $scope.interventionRecord = record;
                        $scope.interventionRecordIndex = index;
                        $scope.interventionConfirmationPopUp = true;
                        $('#interventionConfirmationPopUp').modal('show');

                    }
                }
                else{
                    $scope.resultEntryworklistForValidation[index].isClinicalInterventionRequired = 'N';
                    $scope.interventionConfirmationPopUp = false;
                    $('#interventionConfirmationPopUp').modal('hide');
                    $scope.interventionRecord = {};
                    $scope.interventionRecordIndex = undefined;
                }
            }

            $scope.openInterventionCompletionPopUp = function(record){
                if(!$scope.interventionConfirmationPopUp){
                    $scope.interventionCompletionRecord = record;
                    $scope.interventionCompletionPopUp = true;
                    var dateObj = $scope.setInterventionEndDate(record.interventionEndDate);
                    $scope.interventionEndDate = dateObj;
                    $scope.interventionEndTime = dateObj;
                    $scope.interventionCompletionRecord.remarks = record.interventionRemarks !== undefined && record.interventionRemarks !== null && record.interventionRemarks !== '' ? record.interventionRemarks : '';
                    $scope.interventionCompletionRecord.disableInterventionCompletion = record.disableInterventionCompletion !== undefined && record.disableInterventionCompletion !== null && record.disableInterventionCompletion !== '' ? record.disableInterventionCompletion : false;

                    $('#interventionCompletionPopUp').modal('show');
                }
                else{
                    $scope.interventionCompletionPopUp = false;
                    $('#interventionCompletionPopUp').modal('hide');
                    $scope.interventionCompletionRecord = {};
                    $scope.interventionEndDate = moment().toDate();
                    $scope.interventionEndTime = new Date();
                }
            }

            $scope.setInterventionEndDate = function(dateStr){
                if (dateStr) {
                    var m = moment(dateStr, 'DD/MM/YYYY HH:mm:ss');
                    return m.isValid() ? m.toDate() : new Date();
                }
                return new Date();
            }

            $scope.saveIntervention = function(){
                try{
                    $scope.currentDate = new Date();
                    $scope.currentDate = moment($scope.currentDate)
                            .format('DD-MM-YYYY HH:mm:ss');
                    var data = {};
                    data.patientId = $scope.interventionRecord.patientId;
                    data.labSampleDtlsId = $scope.interventionRecord.labSampleDtlsId;
                    data.isClinicalInterventionRequired = $scope.interventionRecord.isClinicalInterventionRequired;
                    data.interventionRequestedNurseId = $scope.userId;
                    data.createdBy = $scope.userId;
                    data.unitId = $scope.unitId;
                    data.organizationId = $scope.organizationId;
                    data.createdDate = $scope.currentDate;
                    data.interventionStartDate = $scope.currentDate;

                    var URI = BASE_URL + ROOT_URL
                                + LIS_TRANSACTION
                                + SAVE_PATIENT_INTERVENTION ;
                    console.log("Save Intervention Required URL =", URI);
                    GenericService
                    .serviceAction("POST", URI, data)
                    .then(
                        function(response)
                        {
                            $rootScope.stopLoader();
                            $scope.interventionRecord = {};
                            $scope.interventionRecordIndex = undefined;
                            $scope.interventionCompletionPopUp = false;
                            $scope.getValidationWorklist();
                            $rootScope.stopLoader();
                        });
                    $rootScope.stopLoader();
                }
                catch(e){
                    console.log(e.message)
                }
            }

            $scope.updateIntervention = function(){
                try{
                    $scope.currentDate = new Date();
                    $scope.currentDate = moment($scope.currentDate)
                            .format('DD-MM-YYYY HH:mm:ss');
                    $scope.interventionEndDate = moment($scope.interventionEndDate)
                            .format('DD-MM-YYYY');
                    $scope.interventionEndTime = moment($scope.interventionEndTime)
                            .format('HH:mm');
                    var data = {};
                    data.patientId = $scope.interventionCompletionRecord.patientId;
                    data.labSampleDtlsId = $scope.interventionCompletionRecord.labSampleDtlsId;
                    data.interventionEndDate = $scope.interventionEndDate +" "+ $scope.interventionEndTime + ":00";
                    data.interventionCompletedDoctorId = $scope.userId;
                    data.updatedBy = $scope.userId;
                    data.updatedDate = $scope.currentDate;
                    data.unitId = $scope.unitId;
                    data.organizationId = $scope.organizationId;
                    data.clinicalInterventionProcedureId = $scope.interventionCompletionRecord.clinicalInterventionProcedureId;
                    data.remarks = $scope.interventionCompletionRecord.remarks;

                    var URI = BASE_URL + ROOT_URL
                                + LIS_TRANSACTION
                                + UPDATE_PATIENT_INTERVENTION ;
                    console.log("Update Intervention Required URL =", URI);
                    GenericService
                    .serviceAction("POST", URI, data)
                    .then(
                        function(response)
                        {
                            $scope.interventionCompletionRecord = {};
                            $scope.interventionEndDate = moment().toDate();
                            $scope.interventionEndTime = new Date();
                            $scope.interventionCompletionPopUp = false;
                            $scope.getValidationWorklist();
                            $rootScope.stopLoader();
                        });
                    $rootScope.stopLoader();
                }
                catch(e){
                    console.log(e.message)
                }
            }

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("pocWorklist", {
            url: "/pocWorklist",
            title: 'POC Worklist',
            templateUrl: 'views/Nursing/poc-worklist.html',
            controller: "pocWorklist.Controller",
             resolve : {
                        data: function($q, $state, $timeout) {
                            var deferred = $q.defer();
                            $timeout(function() {
                                var arr = JSON.parse(localStorage.getItem('permissions'))
                                .modules.filter(function(item){
                                   return (item.id == 1069);
                                });
                                if(arr.length <= 0){
                                    $state.go("accessDenied")
                                    deferred.reject();
                                 } else {
                                    deferred.resolve();
                                 }
                            });

                            return deferred.promise;
                          }
                     },
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("pocWorklist.Controller", pocWorklist);
})();