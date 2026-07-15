(function () {
    "use strict";

    function LaboratoryInspectionChecklist($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){
             $state.go('login');
             return;
        }
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId;
        $scope.labInspectionSelectedList = [];


        function initializeController() {
            $rootScope.loginpage = true;

            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.departmentList = [];
            $scope.datepickerDOB = new Date();

            /*** Lab Configuration */
            var labConfig  =    $scope.checkLabConfig();
            $scope.deptId = labConfig.labSpecialityId;
            $scope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
            $scope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
            $scope.isAcceptanceInDepartment= labConfig.isAcceptanceInDepartment;
            $scope.isWorklistDepartment= labConfig.isWorklistDepartment;
            $scope.consolidatedResultEntry = labConfig.consolidatedResultEntry;
            $scope.labSortingId =  labConfig.labSortingId;
            $scope.multiselectObj = {
                "id":"",
                "name":""
            }

            $scope.getLaboratoryInspection = function(){

                $scope.labInspectionList = [];
                $http.get('resources/assets/js/nabhJSON/laboratory-inspection.json')
                   .then(function (response) {
                       $scope.labInspectionList = response.data;
                       console.log("$scope.laboratoryInspectionList===========",$scope.labInspectionList);
                       $rootScope.stopLoader();
                   })
                   .catch(function (error) {
                       $rootScope.stopLoader();
                       console.error('Error fetching JSON:', error);
                   });

            }

            $scope.getLaboratoryInspection();


            $scope.patientSearchObj = {
                "uhIdNumber" : "",
                "patientName" : "",
                "genderId" : "",
                "mobileNo" : "",
                "birthDate" : "",
                //"indentificationTypeId":"",
                "identificationNo" : "",
                "patientType" : 1,
                "unitId" : $scope.unitId,
                "organizationId" : $scope.organizationId
            }

            // function for initializa clockpicker
            $scope.initClockPicker = function() {
                $('.clockpicker').clockpicker();
            }

            $scope.searchPatient = function(patientsearchObj) {
                console.log(patientsearchObj)
                if(patientsearchObj.uhIdNumber=="" && patientsearchObj.patientName=="" && patientsearchObj.genderId==""
                     && patientsearchObj.mobileNo == "" && patientsearchObj.birthDate=="" && patientsearchObj.identificationTypeId=="" && patientsearchObj.identificationNo=="")
                  {
                        growl.warning("Please enter searching criteria details",{title : "warning!"
                        });
                        return false;
                    }
                if(patientsearchObj.birthDate!="")
                patientsearchObj.birthDate= moment(patientsearchObj.birthDate).format('YYYY-MM-DD');

                console.log("patientsearchObj.birthDate ",patientsearchObj.birthDate);
                //return false;
                    $scope.noRecordMsg = false;
                    $rootScope.startLoader();
                var URI = BASE_URL
                        + ROOT_URL
                        + PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
                BillingGenericService
                        .serviceAction(METHOD_POST, URI,
                                patientsearchObj,
                                NOTIFICATION_MSG_STATUS_FALSE)
                        .then(
                                function(response) {
                                    console.log(response);
                                    $scope.patientList = [];
                                    // $rootScope.startLoader();
                                    if (response.data.status == "success")
                                        $scope.patientList = response.data.listObject;
                                    $scope.patientSearchObj.birthDate="";
                                    $rootScope.stopLoader();
                                });
            }

            $scope.initAdmissionReqObj = function(){
                $scope.admissionRequest = {
                    isFlexiable : "N",
                    visitTypeId : 2, //IP
                    patientId : '',
                    tPatientId : '',
                    patientCategoryId : $scope.defaultPatientCategoryId,
                    genderId : 0,
                    note : "",
                    id : '',
                    prefixId : '',
                    emailId : '',
                    admissionTypeId : '',
                    isTreatingDoctor :"N",
                    treatingDoctorSpecialityId : '',
                    treatingDoctorId : ''
                }
            }

            $scope.openDatePickerDOB = function()
                {
                    $scope.datepickerDOBOpen.opened = true;
                };

            $scope.datepickerDOBOpen = {
                opened : false
            };

            $scope.getPatientDetailsByIndex = function(index) {
                if($scope.patientList[index].admissionId){
                    var message = "";
                    if($scope.unitId==$scope.patientList[index].admissionUnitId)
                        message = "Patient is already admitted in IP...!!";
                    else
                        message = "Patient is already admitted in another unit...!!";

                    growl.warning(message,{
                        title : WARNING_MSG
                    });

                    $scope.patientList = [];
                    return false;
                }
                $scope.otpvalidated = false;
                $scope.initAdmissionReqObj();
                $scope.abscondPatientFlag="";
                if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
                    $scope.patientSelectedIdx = index;
                    $scope.patientDetails.age = ($scope.patientList[index].age);
                    $scope.patientDetails.patientName = ($scope.patientList[index].patientName);
                    $scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
                    $scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
                    $scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);

                    $scope.admissionRequest.firstName = ($scope.patientList[index].firstName);
                    $scope.admissionRequest.lastName = ($scope.patientList[index].lastName);
                    $scope.admissionRequest.middleName = ($scope.patientList[index].middleName);
                    $scope.admissionRequest.emailId = ($scope.patientList[index].email);
                    $scope.admissionRequest.birthDate = ($scope.patientList[index].birthDate);
                    $scope.admissionRequest.mobileNumber = ($scope.patientList[index].mobileNo);
                    $scope.admissionRequest.bloodGroup = ($scope.patientList[index].bloodGroup);
                //	$scope.admissionRequest.emailId = ($scope.patientList[index].emailId);

                     var array = $scope.patientList[index].birthDate.split('/');
                     var newDate = "";
                     newDate = array[1]+'/'+array[0]+'/'+array[2];
                     $scope.datepickerDOB = new Date(newDate);

                    //var dt = $scope.patientList[index].birthDate;
                    $scope.age = $rootScope.getAge(newDate);


                    if ($scope.patientList[index].patientType == 1) {
                        $scope.admissionRequest.patientId = $scope.patientList[index].patientId;
                    } else {
                        $scope.admissionRequest.tPatientId = $scope.patientList[index].patientId;
                    }

                    $scope.admissionRequest.prefixId = $scope.patientList[index].prefixId;
                    $scope.admissionRequest.id = $scope.patientList[index].genderId;
                    $scope.admissionRequest.patientCategoryId = $scope.patientList[index].patientCategoryId;

                    $scope.regType = 2;
                    $scope.fieldDisabled = true;
                }else{
                    $scope.abscondIndex=index;
                    $scope.abscondPatientFlag='Y';
                    angular.element('#abscondedPatientPopUp').modal('show');
                }

            }

            $scope.getDepartMentList = function(orgId,orgUnitId) {
                try {
                    var data = "";
                    var URI = BASE_URL + ROOT_URL
                            + LIS_COMMON + GET_DEPARMENT_LIST
                            + S + $scope.organizationId + S + $scope.unitId;
                    console.log("URI", URI);
                    $scope.departmentList = [];
                    GenericService.serviceAction("GET", URI, data)
                              .then(function(response) {
                                  $scope.departmentListtest =  response.data.listObject
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
                                                                                            multiselectObj.name = 	value.name;
                                                                                            $scope.departmentList.push(multiselectObj);
                                                                                        }
                                                                                    }else{
                                                                                        if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id
                                                                                            && labConfig.biochemistryId!=value.id && labConfig.hematologyId!=value.id
                                                                                            && labConfig.serologyId!=value.id && labConfig.clinicalPathologyId!=value.id
                                                                                            && labConfig.cystopathologyId!=value.id){
                                                                                            multiselectObj.id = value.id;
                                                                                            multiselectObj.name = 	value.name;
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
                                                                                multiselectObj.name = 	value.name;
                                                                                $scope.departmentList.push(multiselectObj);
                                                                            }
                                                                        }else{
                                                                            if(labConfig.histopathologyId!= value.id && labConfig.microbiologyId!=value.id
                                                                                && labConfig.biochemistryId!=value.id && labConfig.hematologyId!=value.id
                                                                                && labConfig.serologyId!=value.id && labConfig.clinicalPathologyId!=value.id
                                                                                && labConfig.cystopathologyId!=value.id){
                                                                                multiselectObj.id = value.id;
                                                                                multiselectObj.name = 	value.name;
                                                                                $scope.departmentList.push(multiselectObj);
                                                                            }
                                                                        }
                                                                        });
                                                                }
                                                            }
                                                         });

                                    });
                   } catch (e) {
                    console.log(e.message)
                }
            }

            $scope.getDepartMentList($scope.orgId,$scope.orgUnitId);

            $scope.addLabList = function(index,labInfo,isRadioSelect,radioNum){

                var obj = {
                    labInspectionId: index,
                    labInspectionNameId: labInfo.id,
                    labInspectionName: labInfo.name,
                    labInspectionNameFlag: isRadioSelect
                }

                $scope.updateOrPushLabInspectionObject(obj);
                console.log("$scope.labInspectionSelectedList====",$scope.labInspectionSelectedList);

            }

            $scope.updateOrPushLabInspectionObject = function(obj){
                var foundIndex = -1;

                for (var i = 0; i < $scope.labInspectionSelectedList.length; i++) {
                    if ($scope.labInspectionSelectedList[i].labInspectionNameId === obj.labInspectionNameId
                                && $scope.labInspectionSelectedList[i].labInspectionId === obj.labInspectionId) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one
                    $scope.labInspectionSelectedList[foundIndex] = obj;
                } else {
                    // Push the new object to the array
                    $scope.labInspectionSelectedList.push(obj);
                }

            }

            $scope.saveLabInspections = function(saveAsDraft){

                $rootScope.startLoader();

                if($scope.labInspectionSelectedList.length>0){
                var tempAssessmentSelectedList = $scope.labInspectionSelectedList;
                $scope.labInspectionSelectedList = [];
                angular.forEach(tempAssessmentSelectedList, function(value,id) {
                    var obj = {
                        labInspectionId: value.labInspectionId,
                        labInspectionName: value.labInspectionName,
                        labInspectionNameFlag: value.labInspectionNameFlag,
                        labInspectionNameId: value.labInspectionNameId
                    }
                    $scope.labInspectionSelectedList.push(obj);

                });
                }

                if($scope.datepickerDOB==null || $scope.datepickerDOB==undefined || $scope.datepickerDOB==""){
                    growl.error("Please choose date", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.id==null || $scope.id==undefined || $scope.id==""){
                    growl.error("Please select department", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.labInspectionSelectedList.length<1 || $scope.labInspectionSelectedList==undefined || $scope.labInspectionSelectedList==null){
                    growl.error("Please select at least one laboratory inspection", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if(saveAsDraft=='N'){
                    if($scope.labInspectionSelectedList.length<34 || $scope.labInspectionSelectedList==null || $scope.labInspectionSelectedList==undefined){
                        growl.error("Please select all laboratory inspection", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                }

                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    departmentId:$scope.id,
                    labInspectionSelectedList:$scope.labInspectionSelectedList,
                    saveAsDraft:saveAsDraft,
                    labInspectionDate:moment($scope.datepickerDOB).format('DD-MM-YYYY'),
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI = BASE_URL + ROOT_URL +"/adt/saveLabInspectionScore";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         $scope.blankAllRadioButton();
                         $scope.labInspectionSelectedList = [];
                         $scope.getLaboratoryInspection();
                        $scope.datepickerDOB = new Date();
                        $scope.id="";

                         growl.success(response.data.message,
                         {
                            title : 'Success!'
                         });
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }
                });
            }


            $scope.getSaveAsDraftLabInspections = function(departmentId){

                var URI1 = BASE_URL + ROOT_URL +"/adt/getSaveAsDraftLabInspectionScore/"+$scope.unitId+"/"+$scope.organizationId+"/"+departmentId;
                $rootScope.startLoader();
                $scope.labInspectionSelectedList = [];
                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                       $scope.blankAllRadioButton();
                        $scope.labInspectionSelectedList = response.data.listObject;
                        if($scope.labInspectionSelectedList.length>0){
                            var array = $scope.labInspectionSelectedList[0].labInspectionFromDate.split('/');
                            var newDate = "";
                            newDate = array[1]+'/'+array[0]+'/'+array[2];
                            $scope.datepickerDOB = new Date(newDate);

                            angular.forEach($scope.labInspectionSelectedList, function(value,id) {
                                $scope.setSaveAsDraftLabInspections(value,id);
                            });
                        }

                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.generalSafety = {};
            $scope.setSaveAsDraftLabInspections = function(value,id){
                if (!$scope.generalSafety[value.labInspectionId]) {
                    $scope.generalSafety[value.labInspectionId] = {};
                }
                $scope.generalSafety[value.labInspectionId][value.labInspectionNameId] = value.labInspectionNameFlag === 1;
            }


            $scope.blankAllRadioButton = function() {
                // Initialize generalSafety object if not already initialized
                $scope.generalSafety = {};

                // Loop through labInspectionList
                angular.forEach($scope.labInspectionList, function(lab, labIndex) {
                    // Initialize nested object for each labId if not already initialized
                    if (!$scope.generalSafety[lab.labId]) {
                        $scope.generalSafety[lab.labId] = {};
                    }

                    // Loop through labList of each lab
                    angular.forEach(lab.labList, function(item, itemIndex) {
                        // Initialize nested property for each labList item if not already initialized
                        if (!$scope.generalSafety[lab.labId][item.id]) {
                            $scope.generalSafety[lab.labId][item.id] = "";
                        }
                    });
                });
            };

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("LaboratoryInspectionChecklist", {
            url: "/laboratoryInspectionChecklist",
            title: 'Laboratory Inspection Checklist',
            templateUrl: 'views/Nursing/laboratory-inspection-checklist.html',
            controller: "LaboratoryInspectionChecklist.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("LaboratoryInspectionChecklist.Controller", LaboratoryInspectionChecklist);
})();