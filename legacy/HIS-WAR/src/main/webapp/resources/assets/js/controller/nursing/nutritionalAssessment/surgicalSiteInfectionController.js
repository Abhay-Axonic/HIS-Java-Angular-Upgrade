(function () {
    "use strict";

    function SurgicalSiteInfection($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
        $rootScope.loginpage = true;

        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;


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

                    $scope.listOfCounters=function(data){
                    $scope.showData = data;
                    }

                    $scope.initAdmissionReqObj = function()
                                {
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
                                    treatingDoctorId : '',
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
                            $scope.patientDetails.patientId = ($scope.patientList[index].patientId);
                            $scope.patientDetails.doa = ($scope.patientList[index].doa);

                            $scope.admissionRequest.firstName = ($scope.patientList[index].firstName);
                            $scope.admissionRequest.lastName = ($scope.patientList[index].lastName);
                            $scope.admissionRequest.middleName = ($scope.patientList[index].middleName);
                            $scope.admissionRequest.emailId = ($scope.patientList[index].email);
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

}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("SurgicalSiteInfection", {
            url: "/surgicalSiteInfection",
            title: 'Surgical Site Infection',
            templateUrl: 'views/Nursing/surgical-site-infection.html',
            controller: "SurgicalSiteInfection.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("SurgicalSiteInfection.Controller", SurgicalSiteInfection);
})();