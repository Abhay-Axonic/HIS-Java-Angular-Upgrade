(function () {
    "use strict";

    function AnesthesiaAssement($scope, $rootScope, $http, $state, $stateParams, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
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
        $scope.dateFormat = "dd/MM/yyyy";
        $scope.listAnesthesiaTypeMaster = [];
        $scope.previousSurgery = "";
        $scope.continuedMedicationHistoryList = [];



        function initializeController() {

            $rootScope.loginpage = true;
            $scope.patientDetails = {};
            $scope.continuedMedicationHistoryList = [];

            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.updateBtnFlag=true;
            $scope.previousSurgeryDesc = "";

            $scope.patientSearchObj = {
                "uhIdNumber" : "",
                "patientName" : "",
                "genderId" : "",
                "mobileNo" : "",
                "birthDate" : "",
                "unitId" : $scope.unitId,
                "organizationId" : $scope.organizationId
            }

            $scope.blankPatientSearchObj = function(){
                $scope.patientSearchObj.uhIdNumber= "";
                 $scope.patientSearchObj.patientName= "";
                 $scope.patientSearchObj.genderId= "";
                 $scope.patientSearchObj.mobileNo= "";
                 $scope.patientSearchObj.birthDate= "";
            }

            $scope.blankPatientDetails = function(){
                 $scope.patientDetails.age = "";
                 $scope.patientDetails.patientName = "";
                 $scope.patientDetails.genderCode = "";
                 $scope.patientDetails.uhIdNumber = "";
                 $scope.patientDetails.birthDate = "";
                 $scope.patientDetails.doa = "";
                 $scope.patientDetails.patientId = "";
                 $scope.patientDetails.admissionId = "";
            }

            $scope.openDatePickerFromDate = function() {
                $scope.datepickerFromDate.opened = true;
            };

            $scope.datepickerFromDate = {
                opened : false
            };

            $scope.dateOptions = {
                formatYear : 'yyyy',
                showWeeks : false,
                minDate : new Date()
            };

            $scope.openDatepickerFrom = function() {
                $scope.datepickerFrom.opened = true;
            };

            $scope.datepickerFrom = {
                    opened : false
                };
            // Date For To Date on Search

            $scope.openDatePickerToDate = function() {
                $scope.datepickerToDate.opened = true;
            };

            $scope.datepickerToDate = {
                opened : false
            };

            $scope.datepickerFromDate = new Date();
            $scope.datepickerToDate = new Date();

            $scope.searchPatient = function(patientsearchObj){
                $rootScope.startLoader();
                $scope.blankPatientDetails();
                $scope.datepickerFromDate = new Date();
                $scope.datepickerToDate = new Date();
                $scope.searcVitalsList = [];
                $scope.vitalDateList = [];
                $scope.vitalsListSearch = [];
                $scope.SurgeryFromServiceList = [];
                $scope.previousSurgery = "";
                $scope.blankAllCheckbox();
                $scope.selectedAnesthesiaTypeName = "";
                $scope.anesthesiaPlan = "";
                $scope.continuedMedicationHistoryList = [];
//                if(patientSearchObj.patientName!="" && patientSearchObj.patientName.length < 3){
//                    growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
//                    $rootScope.stopLoader();
//                    return false;
//                }
//                if(patientSearchObj.birthDate != "" && typeof(patientSearchObj.birthDate) != "undefined"){
//                    patientSearchObj.birthDate = moment(patientSearchObj.birthDate).format('YYYY-MM-DD');
//                }
                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
                BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                	
                	$scope.patientList = [];
                	angular.forEach(response.data.listObject,function(value,key){
                        if(value.admissionId != null || value.encounterId!= null ){
                            $scope.patientList.push(value);
                        }
                    });
                	
                    if(response.data.listObject==null){
                        growl.warning(response.data.message,{
                            title : ERROR_MSG
                        });
                        $scope.patientSearchObj.birthDate="";
                        $scope.patientList=[];
                        return false;
                    }
                     $scope.patientSearchObj.birthDate="";
                });
            };

            $scope.getPatientDetailsByIndex = function(index) {

                $scope.otpvalidated = false;
                $scope.abscondPatientFlag="";
                $rootScope.startLoader();
                $scope.blankPatientDetails();
                $scope.datepickerFromDate = new Date();
                $scope.datepickerToDate = new Date();
                $scope.searcVitalsList = [];
                $scope.vitalDateList = [];
                $scope.vitalsListSearch = [];
                $scope.SurgeryFromServiceList = [];
                $scope.previousSurgery = "";
                $scope.blankAllCheckbox();
                $scope.selectedAnesthesiaTypeName = "";
                $scope.anesthesiaPlan = "";
                $scope.continuedMedicationHistoryList = [];
//                if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
                    $scope.patientSelectedIdx = index;
                    $scope.patientDetails.age = ($scope.patientList[index].age);
                    $scope.patientDetails.patientName = ($scope.patientList[index].patientName);
                    $scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
                    $scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
                    $scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
                    $scope.patientDetails.doa = ($scope.patientList[index].doa);
                    $scope.patientDetails.patientId = ($scope.patientList[index].patientId);
                    $scope.patientDetails.admissionId = ($scope.patientList[index].admissionId);
                    $scope.patientDetails.encounterId = ($scope.patientList[index].encounterId);
                    $scope.patientDetails.specialityId = ($scope.patientList[index].specialityId);
                    $scope.patientDetails.specialityName = ($scope.patientList[index].specialityName);
                    $scope.patientDetails.doctorId = ($scope.patientList[index].doctorId);
                    $scope.patientDetails.doctorName = ($scope.patientList[index].doctorName);

                     var array = $scope.patientList[index].birthDate.split('/');
                     var newDate = "";
                     newDate = array[1]+'/'+array[0]+'/'+array[2];
                     $scope.datepickerDOB = new Date(newDate);

                    //var dt = $scope.patientList[index].birthDate;
                    $scope.age = $rootScope.getAge(newDate);

                    $scope.regType = 2;
                    $scope.fieldDisabled = true;
//                }else{
//                    $scope.abscondIndex=index;
//                    $scope.abscondPatientFlag='Y';
//                    angular.element('#abscondedPatientPopUp').modal('show');
//                }

                $scope.getCurrentAppointmentVitalMapper();
                $scope.getSurgeryFromService();
                $scope.getAnesthesiaAssessment();

            }

//          vitalFunctionality
            $scope.getVitalList = function() {
                $scope.startLoader();
                var headerObj = {
                    "unit_id" : $scope.unitId,
                    "organization_id" : $scope.organizationId
                };
                var paramObjForPer = {

                };
                var data = {};
                var URI = BASE_URL + ROOT_URL
                        + GETVITALMASTERLIST;
                GenericServiceParamHeader
                        .serviceAction(METHOD_GET, URI, data,
                                NOTIFICATION_MSG_STATUS_FALSE,
                                paramObjForPer, headerObj)
                        .then(
                                function(response) {
                                    console.log(response);
                                    // $rootScope.startLoader();
                                    if (response.data.status == "success")
                                         $scope.vitalsList = [];
                                     if($scope.patientDetails.isIcuu == true){
                                         $scope.vitalsList = response.data.listObject;
//                                         $scope.printListForVitals();
                                     }else{
                                         angular.forEach(response.data.listObject,function(value,key){
                                             if(value.isIcu == false){
                                                 $scope.vitalsList.push(value);
                                             }
                                         });
//                                         $scope.printListForVitals();
                                     }
                                    console.log("Vital List",
                                            $scope.vitalsList);
                                });
                $scope.stopLoader();
            }
			$scope.getVitalList();

            $scope.getVitalListMultiselect = function() {
                $scope.vitalListMultiselect=[];
                $scope.startLoader();
                var headerObj = {
                        unit_id : $scope.unitId,
                        organization_id : $scope.organizationId
                    };


                var paramObjForPer = {
                        patientId:$scope.patientDetails.patientId,
                        admissionId : $scope.patientDetails.admissionId,
                        roleId : $scope.roleId
                };
                var data ={};

                var URI = BASE_URL + ROOT_URL
                        + GETVITALMASTERLIST;
                GenericServiceParamHeader
                        .serviceAction(METHOD_GET, URI, data,
                                NOTIFICATION_MSG_STATUS_FALSE,
                                paramObjForPer, headerObj)
                        .then(
                                function(response) {
                                    console.log(response);
                                    // $rootScope.startLoader();
                                    if (response.data.status == "success")
                                        angular .forEach(response.data.listObject, function( value, index) {
                                            var newAr = {};
                                            newAr.id = value.vitalId;
                                            newAr.label = value.vitalName;
                                            $scope.vitalListMultiselect.push(newAr);
                                        });


                                    if($scope.showHeadCircuVital == false){
                                        $scope.vitalsListMul = [];
                                        angular.forEach(response.data.listObject, function( value,key) {
                                            if(value.vitalId != 15){
                                                  var newAr = {};
                                                  newAr.id = value.vitalId;
                                                  newAr.label = value.vitalName;
                                                  $scope.vitalsListMul.push(newAr);
                                            }
                                        });
                                        $scope.vitalListMultiselect=[];
                                        $scope.vitalListMultiselect=$scope.vitalsListMul;
                                    }

                                    console.log("$scope.vitalListMultiselect",$scope.vitalListMultiselect);
                                });


                $scope.stopLoader();
            }

            $scope.getVitalListMultiselect();

            $scope.setDateTodate = function(){
                 $scope.dateOptionsTodateTrend=[];
    //	                       		  $scope.dateOptionsTodateTrend.minDate = $scope.datepickerFromDate;
                 $scope.dateOptionsTodateTrend = {
                    formatYear : 'yyyy',
                    showWeeks : false,
                    minDate:$scope.datepickerFromDate, //max
                    maxDate:new Date(), //max
                 };

            };

            $scope.refresh = function(){
                 $scope.getCurrentAppointmentVitalMapper();
//                 $scope.selectedVital=[];
//                 $scope.myChartForSearch =false;
            }

            $scope.getCurrentAppointmentVitalMapper = function() {

                $rootScope.startLoader();
                var paramObjForVital = {
                    patientId : $scope.patientDetails.patientId,
                    encounterId : $scope.patientDetails.admissionId,
                    roleId : $scope.roleId,
                    flag : 'Y' // for admission
                }
                var headerObj = {
                    "unit_id" : $scope.unitId,
                    "organization_id" : $scope.organizationId
                };
                var data = {
                            unitId:$scope.unitId,
                           organizationId:$scope.organizationId,
                           patientId:$scope.patientDetails.patientId,
                };
                console.log("jj", paramObjForVital);
                // return false;
                var URI = BASE_URL + ROOT_URL
//										+ GETCURRENTAPPOINTMENTVITALMAPPER;
                + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
                GenericServiceParamHeader
                        .serviceAction(METHOD_POST, URI, data,
                                NOTIFICATION_MSG_STATUS_FALSE,
                                paramObjForVital, headerObj)
                        .then(
                                function(response) {
                                    console.log(response);

                                    if (response.data.status == "success")
                                        $scope.vitalFinalList = [];
                                        $scope.patientViatalList = [];

                                     if($scope.patientDetails.isIcuu == true){
                                         $scope.patientViatalList = response.data.listObject;
                                     }else{
                                         angular.forEach(response.data.listObject,function(value,key){
                                             if(value.isIcu == false){
                                                 $scope.patientViatalList.push(value);
                                             }
                                         });
                                     }

                                    angular.forEach($scope.patientViatalList,function(value,index)
                                            {
                                            var postFactoDate3 = moment(value.postFactoDate,'DD-MM-YYYY').format('DD-MMM-YYYY');
                                            if(postFactoDate3=='Invalid date')
                                                {
                                                postFactoDate3='';
                                                }else
                                                    {
                                                    postFactoDate3=postFactoDate3;
                                                    }
                                            value['postFactoDate3']=postFactoDate3;
                                            });

                                    $scope.vitalDateList = [];
                                    $scope.vitalNameList = [];


                                    angular.forEach($scope.patientViatalList, function(value,key) {
                                         $scope.flagTime=false;
                                          if($scope.vitalDateList.length > 0){
                                                angular.forEach($scope.vitalDateList, function(value3,key) {
                                                    if(value3.createdTime1 == value.postFactoTime){
                                                        $scope.flagTime = true;
                                                    }
                                                });
                                            }
                                             if(!$scope.flagTime){
                                                var newArr={};
                                                newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
                                                 newArr.createdTime1=value.postFactoTime;
                                                 newArr.createdDate=value.createdDate;
                                                 value.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
                                                $scope.vitalDateList.push(newArr);
                                                angular.forEach($scope.vitalDateList,function(value,index)
                                                        {
                                                        var createdTime3 = moment(value.createdTime,'DD-MM-YYYY HH:mm').format($scope.dateFormat.toUpperCase()+' HH:mm');
                                                        if(createdTime3=='Invalid date')
                                                            {
                                                            createdTime3='';
                                                            }else
                                                                {
                                                                createdTime3=createdTime3;
                                                                }
                                                        value['createdTime3']=createdTime3;
                                                        });
                                             }
                                             console.log("$scope.createdTime",$scope.vitalDateList);

                                    });

                                    $scope.vitalsListTest=[];
                                    angular.forEach($scope.vitalsList, function(value1 , key1) {
                                    angular.forEach($scope.patientViatalList, function(value,key) {
                                          if(value1.vitalId == value.vitalId){
                                              var newArr = {};
                                              newArr.value = value.value;
                                              newArr.vitalId = value.vitalId;
                                              newArr.appointmentVitalMapperId = value.appointmentVitalMapperId;
                                              newArr.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
                                              $scope.vitalsListTest.push(newArr);
                                          }
                                    });
                                    });

                                    console.log("sortedDataList",$scope.vitalsListTest);

                                    if($scope.forthTabLevel == 12){
                                        $scope.onClick();
                                    }

                                    console.log( "patientVitalList", response.data.listObject);
                                    console.log("vitalList", $scope.vitalsList);

                                    $scope.vitalsListFOR=[];
                                    if($scope.showHeadCircuVital == false){
                                        angular.forEach( $scope.vitalsList, function( value,key) {
                                            if(value.vitalId != 15){
                                                $scope.vitalsListFOR.push(value);
                                            }
                                        });
                                        $scope.vitalsList=[];
                                        $scope.vitalsList=$scope.vitalsListFOR;
                                    }

                                    angular.forEach( $scope.vitalsList, function( value,key) {
                                                        var checkFlag = false;
                                    angular.forEach($scope.patientViatalList, function( value1, key1) {

                                                                            if (value.vitalId == value1.vitalId) {
                                                                                checkFlag = true;
                                                                                $scope.newArr = {};
                                                                                $scope.newArr.vitalId = value.vitalId;
                                                                                $scope.newArr.minValue = value.minValue;
                                                                                $scope.newArr.maxValue = value.maxValue;
                                                                                $scope.newArr.vitalName = value.vitalName;
                                                                                $scope.newArr.vitalIconPath = value.vitalIconPath;
                                                                                $scope.newArr.unit = value.unit;
                                                                                $scope.newArr.vitalValue = value1.value;
                                                                                $scope.newArr.isReviewedFlag = value1.isReviewedFlag;
                                                                                $scope.newArr.vitalOrderOpd = value.vitalOrderOpd;
                                                                                $scope.newArr.isRequired = value.isRequired;
                                                                                $scope.newArr.checkQualifier = true;

                                                                                if ($scope.roleId == 2) {
                                                                                    $scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
                                                                                }

                                                                            }

                                                                        });
                                                        if (checkFlag == true) {
                                                            $scope.vitalFinalList.push($scope.newArr);
                                                        } else {
                                                            // alert("fdvfd");
                                                            var newArr = {};
                                                            newArr.vitalId = value.vitalId;
                                                            newArr.minValue = value.minValue;
                                                            newArr.maxValue = value.maxValue;
                                                            newArr.vitalName = value.vitalName;
                                                            newArr.vitalIconPath = value.vitalIconPath;
                                                            newArr.unit = value.unit;
                                                            newArr.isRequired = value.isRequired;
                                                            if(value.vitalValue != undefined ){
                                                                newArr.vitalValue = value.vitalValue;
                                                            }else{
                                                                newArr.vitalValue =""
                                                            }
                                                            newArr.isReviewedFlag = 'N';
                                                            newArr.vitalOrderOpd = value.vitalOrderOpd;
                                                            newArr.checkQualifier = true;
                                                            if ($scope.roleId == 2) {
                                                                newArr.appointmentVitalMapperId = '';
                                                            }
                                                            $scope.vitalFinalList
                                                                    .push(newArr);
                                                        }

                                                    });
                                      var counter=0;
                                        angular.forEach($scope.vitalFinalList,function(value,key){
                                            if( counter == 0){
                                                if(value.vitalId == 2){
                                                    angular.forEach($scope.patientViatalList, function(value1 , key1) {
                                                         if(value.vitalId == value1.vitalId)
                                                         {
                                                             if(counter == 0){
                                                             if(value1.value != "0"){
                                                                 value.vitalValue = value1.value;
                                                                 counter++;
                                                             }
                                                             }
                                                         }
                                                     });
                                                    }
                                            }

                                        });
                                        var counterWt=0;
                                        angular.forEach($scope.vitalFinalList,function(value,key){
                                            if( counterWt == 0){

                                                if(value.vitalId == 1){
                                                    angular.forEach($scope.patientViatalList, function(value1 , key1) {
                                                         if(value.vitalId == value1.vitalId)
                                                         {
                                                             if(counterWt == 0){
                                                             if(value1.value != "0"){
                                                                 $scope.patientDetails.weight = value1.value;
                                                                $scope.patientDetails.vitalUnit = value1.unitDesc;
                                                                counterWt++;
                                                             }
                                                             }
                                                         }
                                                     });
                                                    }
                                            }

                                        });
                                        var datalistVital = $scope.vitalFinalList;
                                        for (var i = 0; i < datalistVital.length; i++) {
                                            if($scope.vitalFinalList[i].vitalId != 2){
                                                $scope.vitalFinalList[i].vitalValue = '';
                                            }
                                        }
                                        console.log("refreshlist",$scope.vitalFinalList);
                                    console.log( "Final Vital Arr", JSON.stringify($scope.vitalFinalList));

                                    $rootScope.stopLoader();
                                });
            }

//            $scope.getCurrentAppointmentVitalMapper();

            $scope.selectedVital=[];
            $scope.searchVitalByDates = function(){

            	if($scope.patientDetails.patientId == undefined || $scope.patientDetails.patientId == null || $scope.patientDetails.patientId == "" ){
            		 growl.warning("Please select patient. ",
 							{
 								title : 'Warning!'
 							});
            		 
            		 return false;
            	}
            	
                $scope.myChartForSearch = true;
                $("#SaveVital").show();
                $scope.listVitalId=[];
                angular.forEach($scope.selectedVital, function(value , key) {
                    $scope.listVitalId.push(value.id);
                });

                $scope.startLoader();
                var data = {

                        organizationId: $scope.organizationId,
                        unitId:$scope.unitId,
                        patientId:$scope.patientDetails.patientId,
                        fromDate:moment($scope.datepickerFromDate).format('YYYY-MM-DD'),
                        toDate:moment($scope.datepickerToDate).format('YYYY-MM-DD'),
                        listOfSelectedVitals:$scope.listVitalId,
                        flag:'N'
                };


                var URI = BASE_URL + ROOT_URL
                + GETAPPOINTMENTVITALMAPPERBYTRAND;

                GenericServiceParamHeader
                .serviceAction(METHOD_POST, URI,
                        data,
                        NOTIFICATION_MSG_STATUS_FALSE)
                .then(
                        function(response) {
                            console.log(response);
                            // $rootScope.startLoader();
                            if (response.data.status == "success")
                                $scope.searcVitalsList = response.data.listObject;
                                console.log("searcVitalsList1",$scope.searcVitalsList);

                            $scope.vitalDateListSearch = [];
                            $scope.vitalsListSearch = [];
                            $scope.vitalsListTestSearch=[];

                            angular.forEach($scope.searcVitalsList, function(value,key) {
                                 $scope.flagTime=false;
                                  if($scope.vitalsListSearch.length > 0){
                                        angular.forEach($scope.vitalsListSearch, function(value3,key) {
                                            if(value3.vitalId == value.vitalId){
                                                $scope.flagTime = true;
                                            }
                                        });
                                    }
                                     if(!$scope.flagTime){
                                        var newArr={};
                                         newArr.vitalName = value.vitalName;
                                         newArr.vitalId = value.vitalId;
                                         newArr.unit=value.unitDesc;
                                        $scope.vitalsListSearch.push(newArr);
                                     }
                                     console.log("$scope.vitalsListSearch",$scope.vitalsListSearch);

                            });

                            angular.forEach($scope.searcVitalsList, function(value,key) {
                                 $scope.flagTime=false;
                                  if($scope.vitalDateListSearch.length > 0){
                                        angular.forEach($scope.vitalDateListSearch, function(value3,key) {
                                            if(value3.createdTime1 == value.postFactoTime){
                                                $scope.flagTime = true;
                                            }
                                        });
                                    }
                                     if(!$scope.flagTime){
                                        var newArr={};
                                        newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
                                         newArr.createdTime1=value.postFactoTime;
                                         newArr.createdDate=value.createdDate;
                                         newArr.appointmentVitalMapperId = value.appointmentVitalMapperId;
                                         value.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
                                        $scope.vitalDateListSearch.push(newArr);
                                     }
                                     console.log("$scope.vitalDateListSearch",$scope.vitalDateListSearch);

                            });


                            angular.forEach($scope.vitalsList, function(value1 , key1) {
                            angular.forEach($scope.searcVitalsList, function(value,key) {
                                  if(value1.vitalId == value.vitalId){
                                      var newArr = {};
                                      newArr.value = value.value;
                                      newArr.vitalId = value.vitalId;
                                      newArr.appointmentVitalMapperId = value.appointmentVitalMapperId;
                                      newArr.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
                                      $scope.vitalsListTestSearch.push(newArr);
                                  }
                            });
                            });

                            if($scope.searcVitalsList.length > 0){
                                  $scope.onClickSearch();
                            }

                        });
                /*else {
                    growl.error("Please select Vital for search !!",
                            {
                                title : 'Error!'
                            });
                    $scope.stopLoader();
                }*/
            }

            $scope.onClickSearch = function () {
                 $scope.vitalnameGraphListForSearch=[];
                 $scope.vitalnameForSearch=[];
                 $scope.createdTimeForSearch=[];
                 $scope.vitalDataForSearch=[];
                 $scope.vitalTimeForSearch=[];
                 $scope.vitalLabelForSearch=[];
                 $scope.createdDate=[];
                 //for label
                 if($scope.searcVitalsList.length > 0){
                     angular.forEach($scope.searcVitalsList, function(value,key) {
                         $scope.flag=false;
                          if($scope.vitalnameGraphListForSearch.length > 0){
                                angular.forEach($scope.vitalnameGraphListForSearch, function(value3,key) {
                                    if(value3.vitalName == value.vitalName){
                                        $scope.flag = true;
                                    }
                                });
                            }
                             if(!$scope.flag){
                                var newArr={};
                                 newArr.vitalName=value.vitalName
                                 newArr.unitDesc=value.unitDesc;
                                 $scope.vitalnameGraphListForSearch.push(newArr);
                             }
                             console.log("$scope.vitalnameGraphListForSearch",$scope.vitalnameGraphListForSearch);

                    });

                     angular.forEach($scope.searcVitalsList, function(value,key) {
                         $scope.flagTime=false;
                          if($scope.createdTimeForSearch.length > 0){
                                angular.forEach($scope.createdTimeForSearch, function(value3,key) {
                                    if(value3.createdTime1 == value.postFactoTime){
                                        $scope.flagTime = true;
                                    }
                                });
                            }
                             if(!$scope.flagTime){
                                var newArr={};
                                 newArr.createdTime=value.createdDate +"  "+value.postFactoTime;
                                 newArr.createdTime1=value.postFactoTime;
                                 newArr.createdDate=value.createdDate;
                                 $scope.createdTimeForSearch.push(newArr);
                             }
                             console.log("$scope.createdTimeForSearch",$scope.createdTimeForSearch);

                    });
                    for (var i = 0; i < $scope.createdTimeForSearch.length; i++) {
                        $scope.vitalLabelForSearch.push($scope.createdTimeForSearch[i].createdTime);
                    }
                    console.log("graph vitalLabelForSearch",$scope.vitalLabelForSearch);

                }
            };

//    vitalFunctionality End

            $scope.getAllActiveMedication = function() {
                $rootScope.startLoader();
                if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=="" || $scope.patientDetails.patientId==undefined){
                    growl.error("Please select the patient...", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }
                $scope.activeMedications = [];
                var patientId = $scope.patientDetails.patientId;
                var dateObj = moment(new Date()).format(
                        'YYYY-MM-DD');
                /*
                 * var date = ("0" +
                 * dateObj.getDate()).slice(-2) + "-" +
                 * ("0"+(dateObj.getMonth()+1)).slice(-2) +"-" +
                 * dateObj.getFullYear();
                 */

                var URI = BASE_URL + ROOT_URL
                        + GETACTIVEMEDICATION + "?patientId="
                        + patientId + "&date=" + dateObj + "&unitId="+ $scope.unitId
                GenericServiceParamHeader
                        .serviceAction(METHOD_GET, URI)
                        .then(
                                function(response) {
                                    if (response.data.status == "success") {
                                        $rootScope.stopLoader();
                                        $scope.activeMedications = response.data.listObject;
                                        angular.forEach($scope.activeMedications,function(value,key){
                                            value.checkTrue='N';
                                            if(value.strengthDesc == null ){
                                                value.strengthDesc = "";
                                            }
                                            if(value.doseDesc == null){
                                                value.doseDesc = "";
                                            }
                                        });
                                        console.log("activeMedication",$scope.activeMedications);
                                    }
                                });
            }


            //List Of Continued Medication of medication History
            $scope.checkMedicationList = function(){
                if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=="" || $scope.patientDetails.patientId==undefined){
                    growl.error("Please select the patient", {
                        title: 'Error!'
                    });
                    return false;
                }
                else if($scope.patientDetails.patientId!=undefined){
                    angular.element('#activeDetailsIpd').modal('show');
                    $scope.getAllActiveMedication();
                    $scope.getContinuedMedicationHistoryList()
                }
            }

            $scope.getContinuedMedicationHistoryList = function() {
                $rootScope.startLoader();
                if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=="" || $scope.patientDetails.patientId==undefined){
                    growl.error("Please select the patient", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return
                }
                $scope.continuedMedicationHistoryList = [];
                var data = {
                    organizationId : $scope.organizationId,
                    unitId : $scope.unitId,
                    patientId : $scope.patientDetails.patientId
                };
                var URI = BASE_URL + ROOT_URL
                        + "/api/opd/getContinuedMedicationList";
                GenericServiceParamHeader
                        .serviceAction(METHOD_POST, URI, data,
                                NOTIFICATION_MSG_STATUS_FALSE)
                        .then(
                                function(response) {
                                    if (response.data.status == "success") {
                                        $rootScope.stopLoader();
                                        $scope.continuedMedicationHistoryList = response.data.listObject;
                                        angular.forEach($scope.continuedMedicationHistoryList,function(value,key){
                                            value.checkTrue='N';
                                        });
                                    }
                                });
            }

            $scope.getSurgeryFromService = function(){

               $rootScope.startLoader();
                var data={};
                var paramObj={};
                var headers = {
                        'patientId' : $scope.patientDetails.patientId,
                        'flag' : true,
                        'Content-Type' : 'application/json',
                        'unitId':$scope.unitId
                    };
                var URI = BASE_URL + ROOT_URL + "/api/opd/getInvestigationsListForAnesthesia" ;
                GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE, paramObj,
                        headers)
                .then(
                    function(response) {
                        if (response.data.status == "success") {
                            $scope.SurgeryFromServiceList = response.data.listObject;
                            $rootScope.stopLoader();
                        }
                        else if(response.data.status == "error")
                            {
                               $rootScope.stopLoader();
                            }
                    });

            }

            $scope.listAnesthesiaTypeMaster = function(){

                $rootScope.startLoader();
                var URI1 = BASE_URL + ROOT_URL +"/api/otims/getListAnesthesiaTypeMaster";

                var data1 = {
                    orgId:$scope.organizationId,
                    offset:"",
                    noOfRecordsPerPage:"",
                    unitId:$scope.unitId
                };

                $scope.listAnesthesiaTypeMaster = [];
                GenericService.serviceAction("POST",URI1,data1).then(function(response){
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.listAnesthesiaTypeMaster = response.data.listObject;
                        console.log("$scope.ListAnesthesiaTypeMaster============",$scope.listAnesthesiaTypeMaster);
                        angular.forEach($scope.listAnesthesiaTypeMaster, function(value) {
                                value.test = false;
                        });
                    }
                });

            }

            $scope.listAnesthesiaTypeMaster();

            $scope.selectedPainCheckBox = "";

            $scope.prescribingCheckbox = function(idx){

                $scope.selectedPainCheckBox= "";
                if($scope.checkBox0 =='Y'){
                    var temp = "0";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox1 =='Y'){
                    var temp = "1";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox2 =='Y'){
                    var temp = "2";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox3 =='Y'){
                    var temp = "3";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox4 =='Y'){
                    var temp = "4";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox5 =='Y'){
                    var temp = "5";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox6 =='Y'){
                    var temp = "6";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox7 =='Y'){
                    var temp = "7";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox8 =='Y'){
                    var temp = "8";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox9 =='Y'){
                    var temp = "9";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox10 =='Y'){
                    var temp = "10";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox11 =='Y'){
                    var temp = "11";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox12 =='Y'){
                    var temp = "12";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox13 =='Y'){
                    var temp = "13";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox14 =='Y'){
                    var temp = "14";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox15 =='Y'){
                    var temp = "15";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox16 =='Y'){
                    var temp = "16";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox17 =='Y'){
                    var temp = "17";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox18 =='Y'){
                    var temp = "18";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox19 =='Y'){
                    var temp = "19";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox20 =='Y'){
                    var temp = "20";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox21 =='Y'){
                    var temp = "21";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox22 =='Y'){
                    var temp = "22";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox23 =='Y'){
                    var temp = "23";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox24 =='Y'){
                    var temp = "24";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox25 =='Y'){
                    var temp = "25";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox26 =='Y'){
                    var temp = "26";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox27 =='Y'){
                    var temp = "27";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox28 =='Y'){
                    var temp = "28";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox29 =='Y'){
                    var temp = "29";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox30 =='Y'){
                    var temp = "30";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox31 =='Y'){
                    var temp = "31";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox32 =='Y'){
                    var temp = "32";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox33 =='Y'){
                    var temp = "33";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox34 =='Y'){
                    var temp = "34";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox35 =='Y'){
                    var temp = "35";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox36 =='Y'){
                    var temp = "36";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox37 =='Y'){
                    var temp = "37";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox38 =='Y'){
                    var temp = "38";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox39 =='Y'){
                    var temp = "39";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox40 =='Y'){
                    var temp = "40";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox41 =='Y'){
                    var temp = "41";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox42 =='Y'){
                    var temp = "42";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox43 =='Y'){
                    var temp = "43";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox44 =='Y'){
                    var temp = "44";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox45 =='Y'){
                    var temp = "45";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox46 =='Y'){
                    var temp = "46";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox47 =='Y'){
                    var temp = "47";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox48 =='Y'){
                    var temp = "48";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox49 =='Y'){
                    var temp = "49";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox50 =='Y'){
                    var temp = "50";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox51 =='Y'){
                    var temp = "51";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox52 =='Y'){
                    var temp = "52";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox53 =='Y'){
                    var temp = "53";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox54 =='Y'){
                    var temp = "54";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox55 =='Y'){
                    var temp = "55";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox56 =='Y'){
                    var temp = "56";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox57 =='Y'){
                    var temp = "57";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox58 =='Y'){
                    var temp = "58";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox59 =='Y'){
                    var temp = "59";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox60 =='Y'){
                    var temp = "60";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox61 =='Y'){
                    var temp = "61";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox62 =='Y'){
                    var temp = "62";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox63 =='Y'){
                    var temp = "63";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox64 =='Y'){
                    var temp = "64";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox65 =='Y'){
                    var temp = "65";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox66 =='Y'){
                    var temp = "66";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                if($scope.checkBox67 =='Y'){
                    var temp = "67";
                    $scope.selectedPainCheckBox += temp+" ";
                }
                console.log("$scope.selectedCheckBox===============",$scope.selectedPainCheckBox);

            }

            $scope.selectAnesthesiaPlan = function(anesthesiaTypeName){
                $scope.anesthesiaPlan = anesthesiaTypeName;
            }

            $scope.selectedAnesthesia = "";
            $scope.selectedClassification = "";
            $scope.saveAnesthesiaAssessment = function(saveAsDraft){
                console.log("Previous surgery : " +$scope.freeText);
                $rootScope.startLoader();
                if($scope.selectedAnesthesia !==  undefined && $scope.selectedAnesthesia !== null && $scope.selectedAnesthesia !== ""){
                    $scope.selectedPainCheckBox += $scope.selectedAnesthesia +" ";
                }

                if($scope.selectedClassification !== undefined && $scope.selectedClassification !== null && $scope.selectedClassification !== ""){
                    $scope.selectedPainCheckBox += $scope.selectedClassification +" ";
                }

                if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId==undefined || $scope.patientDetails.patientId==""){
                    growl.error("Please select the patient", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.previousSurgery!=null || $scope.previousSurgery !=undefined || $scope.previousSurgery!= ""){
                    if($scope.previousSurgery==false){
                        $scope.previousSurgery = 0;
                    }else{
                        $scope.previousSurgery = 1;
                    }
                }

                if(($scope.selectedPainCheckBox=="" || $scope.selectedPainCheckBox==null || $scope.selectedPainCheckBox==undefined) &&
                                        ($scope.anesthesiaPlan=="" || $scope.anesthesiaPlan==null || $scope.anesthesiaPlan==undefined)){
                    growl.error("Please select at least one assessment", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    patientId:$scope.patientDetails.patientId,
                    isPreviousSurgery:$scope.previousSurgery,
                    selectedAssessment:$scope.selectedPainCheckBox,
                    anesthesiaPlan:$scope.anesthesiaPlan,
                    saveAsDraft:saveAsDraft,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    admissionId : $scope.patientDetails.admissionId,
                    encounterId : $scope.patientDetails.encounterId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),

                }

                if($scope.previousSurgery !== undefined && $scope.previousSurgery !== null && $scope.previousSurgery !== "" && $scope.previousSurgery == 1){
                    saveObj.previousSurgeryDesc = $scope.previousSurgeryDesc;
                }

                console.log("saveObj============",saveObj);

                var URI = BASE_URL + ROOT_URL +"/public/saveAnesthesiaAssessment";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         if(saveAsDraft=='Y'){
                            $scope.getAnesthesiaAssessment();
                         }
                         if(saveAsDraft=='N'){
                            $scope.patientDetails.patientId = "";
                            $scope.blankPatientSearchObj();
                            $scope.patientList = [];
                            $scope.blankPatientDetails();
                            $scope.datepickerFromDate = new Date();
                            $scope.datepickerToDate = new Date();
                            $scope.searcVitalsList = [];
                            $scope.vitalDateList = [];
                            $scope.vitalsListSearch = [];
                            $scope.SurgeryFromServiceList = [];
                            $scope.previousSurgery = "";
                            $scope.blankAllCheckbox();
                            $scope.selectedAnesthesiaTypeName = "";
                            $scope.anesthesiaPlan = "";
                         }

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

            $scope.getAnesthesiaAssessment = function(){

                 var URI1 = BASE_URL + ROOT_URL +"/public/getAnesthesiaAssessment/"+$scope.unitId+"/"+$scope.organizationId+"/"+$scope.patientDetails.patientId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.selectedAnesthesiaAssessment = {};
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                        $scope.selectedAnesthesiaAssessment = response.data.object;
                        if($scope.selectedAnesthesiaAssessment!=null && $scope.selectedAnesthesiaAssessment!=""){
                            if($scope.selectedAnesthesiaAssessment.isPreviousSurgery!=null){
                                $scope.previousSurgery = $scope.selectedAnesthesiaAssessment.isPreviousSurgery === 1;
                            }
                            if($scope.selectedAnesthesiaAssessment.anesthesiaPlan!=null){
    //                            for($scope.listAnesthesiaTypeMaster)
                                angular.forEach($scope.listAnesthesiaTypeMaster, function(value) {
                                    if(value.anesthesiaTypeName==$scope.selectedAnesthesiaAssessment.anesthesiaPlan){
                                        $scope.selectedAnesthesiaTypeName = value.anesthesiaTypeName; // Mark this anesthesia type as selected
                                        $scope.anesthesiaPlan = $scope.selectedAnesthesiaTypeName;
                                        value.test = true;
                                    }else{
                                        value.test = false;
                                    }
                                });
                            }
                            $scope.setSelectedAssessment($scope.selectedAnesthesiaAssessment.selectedAssessment);
                        }
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.getSexMasterList = function() {
        		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
        		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        			$scope.sexMasterList = response.data.listObject;
        			console.log($scope.sexMasterList);
        		});
        	}
            $scope.getSexMasterList();
            
            $scope.setSelectedAssessment = function(str){

                var tempString = str;
                var selectedPainAssessmentCheckBoxArray = tempString.trim().split(" ");
                console.log("selectedPainAssessmentCheckBoxArray============",selectedPainAssessmentCheckBoxArray);

                angular.forEach(selectedPainAssessmentCheckBoxArray, function(value,id) {
                    if(value=='0'){$scope.checkBox0='Y';}
                    if(value=='1'){$scope.checkBox1='Y';}
                    if(value=='2'){$scope.checkBox2='Y';}
                    if(value=='3'){$scope.checkBox3='Y';}
                    if(value=='4'){$scope.checkBox4='Y';}
                    if(value=='5'){$scope.checkBox5='Y';}
                    if(value=='6'){$scope.checkBox6='Y';}
                    if(value=='7'){$scope.checkBox7='Y';}
                    if(value=='8'){$scope.checkBox8='Y';}
                    if(value=='9'){$scope.checkBox9='Y';}
                    if(value=='10'){$scope.checkBox10='Y';}
                    if(value=='11'){$scope.checkBox11='Y';}
                    if(value=='12'){$scope.checkBox12='Y';}
                    if(value=='13'){$scope.checkBox13='Y';}
                    if(value=='14'){$scope.checkBox14='Y';}
                    if(value=='15'){$scope.checkBox15='Y';}
                    if(value=='16'){$scope.checkBox16='Y';}
                    if(value=='17'){$scope.checkBox17='Y';}
                    if(value=='18'){$scope.checkBox18='Y';}
                    if(value=='19'){$scope.checkBox19='Y';}
                    if(value=='20'){$scope.checkBox20='Y';}
                    if(value=='21'){$scope.checkBox21='Y';}
                    if(value=='22'){$scope.checkBox22='Y';}
                    if(value=='23'){$scope.checkBox23='Y';}
                    if(value=='24'){$scope.checkBox24='Y';}
                    if(value=='25'){$scope.checkBox25='Y';}
                    if(value=='26'){$scope.checkBox26='Y';}
                    if(value=='27'){$scope.checkBox27='Y';}
                    if(value=='28'){$scope.checkBox28='Y';}
                    if(value=='29'){$scope.checkBox29='Y';}
                    if(value=='30'){$scope.checkBox30='Y';}
                    if(value=='31'){$scope.checkBox31='Y';}
                    if(value=='32'){$scope.checkBox32='Y';}
                    if(value=='33'){$scope.checkBox33='Y';}
                    if(value=='34'){$scope.checkBox34='Y';}
                    if(value=='35'){$scope.checkBox35='Y';}
                    if(value=='36'){$scope.checkBox36='Y';}
                    if(value=='37'){$scope.checkBox37='Y';}
                    if(value=='38'){$scope.checkBox38='Y';}
                    if(value=='39'){$scope.checkBox39='Y';}
                    if(value=='40'){$scope.checkBox40='Y';}
                    if(value=='41'){$scope.checkBox41='Y';}
                    if(value=='42'){$scope.checkBox42='Y';}
                    if(value=='43'){$scope.checkBox43='Y';}
                    if(value=='44'){$scope.checkBox44='Y';}
                    if(value=='45'){$scope.checkBox45='Y';}
                    if(value=='46'){$scope.checkBox46='Y';}
                    if(value=='47'){$scope.checkBox47='Y';}
                    if(value=='48'){$scope.checkBox48='Y';}
                    if(value=='49'){$scope.checkBox49='Y';}
                    if(value=='50'){$scope.checkBox50='Y';}
                    if(value=='51'){$scope.checkBox51='Y';}
                    if(value=='52'){$scope.checkBox52='Y';}
                    if(value=='53'){$scope.checkBox53='Y';}
                    if(value=='54'){$scope.checkBox54='Y';}
                    if(value=='55'){$scope.checkBox55='Y';}
                    if(value=='56'){$scope.checkBox56='Y';}
                    if(value=='57'){$scope.checkBox57='Y';}
                    if(value=='58'){$scope.checkBox58='Y';}
                    if(value=='59'){$scope.checkBox59='Y';}
                    if(value=='60'){$scope.checkBox60='Y';}
                    if(value=='61'){$scope.checkBox61='Y';}
                    if(value=='62'){$scope.checkBox62='Y';}
                    if(value=='63'){$scope.checkBox63='Y';}
                    if(value=='64'){$scope.checkBox64='Y';}
                    if(value=='65'){$scope.checkBox65='Y';}
                    if(value=='66'){$scope.checkBox66='Y';}
                    if(value=='67'){$scope.checkBox67='Y';}
                });

            }

            $scope.blankAllCheckbox = function(){
                $scope.checkBox0='';
                $scope.checkBox1='';
                $scope.checkBox2='';
                $scope.checkBox3='';
                $scope.checkBox4='';
                $scope.checkBox5='';
                $scope.checkBox6='';
                $scope.checkBox7='';
                $scope.checkBox8='';
                $scope.checkBox9='';
                $scope.checkBox10='';
                $scope.checkBox11='';
                $scope.checkBox12='';
                $scope.checkBox13='';
                $scope.checkBox14='';
                $scope.checkBox15='';
                $scope.checkBox16='';
                $scope.checkBox17='';
                $scope.checkBox18='';
                $scope.checkBox19='';
                $scope.checkBox20='';
                $scope.checkBox21='';
                $scope.checkBox22='';
                $scope.checkBox23='';
                $scope.checkBox24='';
                $scope.checkBox25='';
                $scope.checkBox26='';
                $scope.checkBox27='';
                $scope.checkBox27='';
                $scope.checkBox29='';
                $scope.checkBox30='';
                $scope.checkBox31='';
                $scope.checkBox32='';
                $scope.checkBox33='';
                $scope.checkBox34='';
                $scope.checkBox35='';
                $scope.checkBox36='';
                $scope.checkBox37='';
                $scope.checkBox38='';
                $scope.checkBox39='';
                $scope.checkBox40='';
                $scope.checkBox41='';
                $scope.checkBox42='';
                $scope.checkBox43='';
                $scope.checkBox44='';
                $scope.checkBox45='';
                $scope.checkBox46='';
                $scope.checkBox47='';
                $scope.checkBox48='';
                $scope.checkBox49='';
                $scope.checkBox50='';
                $scope.checkBox51='';
                $scope.checkBox52='';
                $scope.checkBox53='';
                $scope.checkBox54='';
                $scope.checkBox55='';
                $scope.checkBox56='';
                $scope.checkBox57='';
                $scope.checkBox58='';
                $scope.checkBox59='';
                $scope.checkBox60='';
                $scope.checkBox61='';
                $scope.checkBox62='';
                $scope.checkBox63='';
                $scope.checkBox64='';
                $scope.checkBox65='';
                $scope.checkBox66='';
                $scope.checkBox67='';
                $scope.selectedAnesthesia ="";
                $scope.selectedClassification = "";
                $scope.previousSurgeryDesc = "";
                $scope.previousSurgery = "";
            }

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("anesthesiaAssement", {
            url: "/anesthesiaAssement",
            title: 'Anesthesia Assessment',
            templateUrl: 'views/master/UnitMaster/organizationMaster/anesthesia-assesment.html',
            controller: "AnesthesiaAssement.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AnesthesiaAssement.Controller", AnesthesiaAssement);
})();