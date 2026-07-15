(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function InitialAssessmentForICU($scope, $rootScope, $http, $state, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload, $timeout) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){
            $state.go('login');
            return;
        }

        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');

        if(cookieObjectAppConfig == undefined){
            $state.go('login');
            return;
        }else{
            console.log('cookieObjectAppConfig=======',cookieObjectAppConfig);
            $scope.nurseRole = cookieObjectAppConfig.nurseRole;
        }

        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId;


        function initializeController() {
            $rootScope.loginpage = true;


            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.userId = cookieObject.userId;
            $scope.storeId = cookieObject.storeId;
            $scope.updateBtnFlag=true;

            $scope.alertList =[];
            $scope.dietList =[];
            $scope.weightchangesList =[];
            $scope.supplementsList =[];
            $scope.swallowingList =[];
            $scope.mealtimesList =[];
            $scope.monitoringList =[];
            $scope.mucousList =[];
            $scope.SkinList =[];

            //Elimination
            $scope.BladderList =[];
            $scope.BowelList =[];
            $scope.IncontinenceList =[];
            $scope.BowelsoundsList =[];
            $scope.ConstipationList =[];
            $scope.OstomiesList =[];

            //Sensory
            $scope.VisionList =[];
            $scope.HearingList =[];

            //Musculoskeletal
            $scope.MobilityList =[];
            $scope.ROMList =[];
            $scope.MotorList =[];

            //Pain Assessment
            $scope.PainAssessmentList =[];

            //skin
            $scope.SkinPainAssessmentList=[];
            $scope.SkinIntactList=[];
            $scope.SpecialCareList=[];

            //Neurological
            $scope.SensationList=[];
            $scope.VerbalList=[];
            $scope.AphasiaList=[];
            $scope.MemoryDeficitsList=[];
            $scope.ImpairedDecisionList=[];
            $scope.SleepaidsList=[];
            $scope.SeizuresList=[];

            //Circulation
             $scope.HistoryList=[];
             $scope.PulseList=[];
             $scope.SkinList=[];
             $scope.EdemaList=[];
             $scope.PittingList=[];

             //Respiration
             $scope.RespirationList=[];
             $scope.ShortnessList=[];
             $scope.TreatmentList=[];
             $scope.BreathSoundsRList=[];
             $scope.BreathSoundsRList=[];

             //Dental
             $scope.teethList=[];
             $scope.ShortnessList=[];
             $scope.DentalHygieneList=[];

             //Psychosocial
             $scope.InjuriousBehaviorList=[];
             $scope.BehaviorList=[];
             $scope.AnswersQuestionsList=[];
             $scope.HallucinationsList=[];

             $scope.initialAssessmentStatusId = "";
             $scope.vitalPulse = "";
             $scope.selectedKeyValueList = [];
             $scope.infusions = "";

            /*$scope.openDatePickerSearch = function() {
                $scope.datepicker5.opened = true;

            $scope.openDatePickerSearch = function() {
                $scope.datepicker2.opened = true;
            };

            $scope.datepicker2 = {
                opened : false
            };
            */

            $scope.openDatePickerSearch = function() {
                $scope.datepicker2.opened = true;
            };

            $scope.datepicker2 = {
                opened : false
            };

            $scope.datepicker2 = new Date();

            $scope.dateOptions = {
                formatYear : 'yyyy',
                showWeeks : false,
                maxDate : new Date()
            };

            $scope.geInitialJson = function(){
                $http.get('resources/assets/js/nabhJSON/initial-assessment-icu.json')
                   .then(function (response) {
                       $scope.alertList = response.data[0].alertList;
                       $scope.dietList = response.data[1].DietList;
                       $scope.weightchangesList = response.data[1].WeightchangesList;
                       $scope.supplementsList = response.data[1].SupplementsList;
                       $scope.swallowingList = response.data[1].swallowingList;
                       $scope.mealtimesList = response.data[1].mealtimesList;
                       $scope.monitoringList = response.data[1].monitoringList;
                       $scope.mucousList = response.data[1].mucousList;
                       $scope.SkinList = response.data[1].SkinList;
                       //Elimination
                       $scope.BladderList = response.data[2].BladderList;
                       $scope.BowelList = response.data[2].BowelList;
                       $scope.IncontinenceList = response.data[2].IncontinenceList;
                       $scope.BowelsoundsList = response.data[2].BowelsoundsList;
                       $scope.ConstipationList = response.data[2].ConstipationList;
                       $scope.OstomiesList = response.data[2].OstomiesList;
                       //Sensory
                       $scope.VisionList = response.data[3].VisionList;
                       $scope.HearingList = response.data[3].HearingList;
                       //Musculoskeletal
                       $scope.MobilityList = response.data[4].MobilityList;
                       $scope.ROMList = response.data[4].ROMList;
                       $scope.MotorList = response.data[4].MotorList;
                       //Pain Assessment
                        $scope.PainAssessmentList = response.data[5].PainAssessmentList;
                       //skin
                        $scope.SkinPainAssessmentList = response.data[6].SkinPainAssessmentList;
                        $scope.SkinIntactList = response.data[6].SkinIntactList;
                        $scope.SpecialCareList = response.data[6].SpecialCareList;
                        //Neurological
                        $scope.SensationList = response.data[7].SensationList;
                        $scope.VerbalList = response.data[7].VerbalList;
                        $scope.AphasiaList = response.data[7].AphasiaList;
                        $scope.MemoryDeficitsList = response.data[7].MemoryDeficitsList;
                        $scope.ImpairedDecisionList = response.data[7].ImpairedDecisionList;
                        $scope.SleepaidsList = response.data[7].SleepaidsList;
                        $scope.SeizuresList = response.data[7].SeizuresList;
                        //Circulation
                         $scope.HistoryList = response.data[8].HistoryList;
                         $scope.PulseList = response.data[8].PulseList;
                         $scope.SkinList = response.data[8].SkinList;
                         $scope.EdemaList = response.data[8].EdemaList;
                         $scope.PittingList = response.data[8].PittingList;
                         //Respiration
                         $scope.RespirationList=response.data[9].RespirationList;
                         $scope.ShortnessList=response.data[9].ShortnessList;
                         $scope.TreatmentList=response.data[9].TreatmentList;
                         $scope.BreathSoundsRList=response.data[9].BreathSoundsRList;
                         $scope.BreathSoundsLList=response.data[9].BreathSoundsLList;
                         //Dental
                         $scope.teethList=response.data[10].teethList;
                         $scope.DentalHygieneList=response.data[10].DentalHygieneList;
                         //Psychosocial
                          $scope.InjuriousBehaviorList=response.data[11].InjuriousBehaviorList;
                          $scope.BehaviorList=response.data[11].BehaviorList;
                          $scope.AnswersQuestionsList=response.data[11].AnswersQuestionsList;
                          $scope.HallucinationsList=response.data[11].HallucinationsList;

                       $rootScope.stopLoader();
                   })
                   .catch(function (error) {
                       $rootScope.stopLoader();
                       console.error('Error fetching JSON:', error);
                   });
            }
            $scope.geInitialJson();

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

           $scope.getIpRenderedDateChangeFlag = function(patientsearchObj){
               var URI = BASE_URL + ROOT_URL + "/adt/getIpRenderedDateChange";
               patientsearchObj.userId=$scope.userId

               BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                   if(response.data.listObject!=null){
                       $scope.ipRenderedDateChangeFlag=response.data.listObject[0];
                   }

               });
           }

            $scope.getPatientDetailsByIndex = function(index) {
                var IndexFromPatientList = index;
                $scope.otpvalidated = false;
                $scope.abscondPatientFlag="";
                if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
                    $scope.patientSelectedIdx = index;
                    $scope.patientDetails.age = ($scope.patientList[index].age);
                    $scope.patientDetails.patientName = ($scope.patientList[index].patientName);
                    $scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
                    $scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
                    $scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
                    $scope.patientDetails.doa = ($scope.patientList[index].doa);
                    $scope.patientDetails.patientId = ($scope.patientList[index].patientId);
                    $scope.patientDetails.admissionId = ($scope.patientList[index].admissionId);

                     var array = $scope.patientList[index].birthDate.split('/');
                     var newDate = "";
                     newDate = array[1]+'/'+array[0]+'/'+array[2];
                     $scope.datepickerDOB = new Date(newDate);

                    //var dt = $scope.patientList[index].birthDate;
                    $scope.age = $rootScope.getAge(newDate);

                    $scope.regType = 2;
                    $scope.fieldDisabled = true;
                }else{
                    $scope.abscondIndex=index;
                    $scope.abscondPatientFlag='Y';
                    angular.element('#abscondedPatientPopUp').modal('show');
                }

                 $scope.blankGetData();
                 $scope.getInitialAssessmentScore(IndexFromPatientList);

            }

            $scope.blankGetData = function(){
                  $scope.selectedKeyValueList=[];
                  $scope.initialAssessmentStatusId="";
                  $scope.vitalPulse="";
                  $scope.vitalSysBloodPress="";
                  $scope.vitalTemperature="";
                  $scope.vitalRespiratoryRate="";
                  $scope.vitalDaiBloodPress="";
                  $scope.vitalOxySatur="";
                  $scope.bedSideRespiratory="";
                  $scope.bedSideTidal="";
                  $scope.bedSideFlow="";
                  $scope.bedSideInspiratory="";
                  $scope.respirationFiO2="";
                  $scope.respirationPEEP="";
                  $scope.respirationPS="";
                  $scope.respirationTidal="";
                  $scope.respirationRate="";
                  $scope.respirationIE="";
                  $scope.respirationFlow="";
                  $scope.respirationITime="";
                  $scope.bladderIncontinence="";
                  $scope.bowelIncontinence="";
                  $scope.incontManageTech="";
                  $scope.bowelSoundPresent="";
                  $scope.EliminationConstipation="";
                  $scope.eliminationOostomies="";
                  $scope.skinAssessment="";
                  $scope.skinIntact="";
                  $scope.skinSpecialCare="";
                  $scope.circulationHistory="";
                  $scope.circulationPulse="";
                  $scope.circulationSkin="";
                  $scope.circulationEdema="";
                  $scope.circulationPitting="";
                  $scope.dentalTeeth="";
                  $scope.dentalHygiene="";
                  $scope.psychosocialInjurious="";
                  $scope.psychosocialBehavior="";
                  $scope.psychosocialAnswers="";
                  $scope.psychosocialDelusions="";
                  $scope.sensoryVision="";
                  $scope.sensoryHearing="";
                  $scope.musculoskeletalMobility="";
                  $scope.musculoskeletalRom="";
                  $scope.musculoskeletalMotorDev="";
                  $scope.neurologicalSensation="";
                  $scope.neurologicalVerbal="";
                  $scope.neurologicalAphasia="";
                  $scope.neurologicalMemory="";
                  $scope.neurologicalDecision="";
                  $scope.neurologicalSleep="";
                  $scope.neurologicalSeizures="";
                  $scope.respirationRespiration="";
                  $scope.respirationShortness="";
                  $scope.respirationTreatment="";
                  $scope.respirationBreathR="";
                  $scope.respirationBreathL="";
                  $scope.nutritionDiet="";
                  $scope.nutritionWeightChanges="";
                  $scope.nutritionSupplements="";
                  $scope.nutritionSwallowing="";
                  $scope.nutritionMealtimes="";
                  $scope.nutritionMonitoring="";
                  $scope.nutritionMucous="";
                  $scope.nutritionSkin="";
                  $scope.painAssessment="";
                  $scope.painMotorDev="";
                  $scope.infusions="";
                  $scope.blankAllSelectedCheckBox();
            }

            $scope.blankPatientDetails = function(){
            	 $scope.patientDetails = {};
                 $scope.patientDetails.age = "";
                 $scope.patientDetails.patientName = "";
                 $scope.patientDetails.genderCode = "";
                 $scope.patientDetails.uhIdNumber = "";
                 $scope.patientDetails.birthDate = "";
                 $scope.patientDetails.doa = "";
                 $scope.patientDetails.patientId = "";
                 $scope.patientDetails.admissionId = "";
            }
            $scope.blankPatientDetails();
            $scope.getLists = function(){

/*                //start for nursing
                var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
                  GenericService.serviceAction("GET",URI,{}).then(function(response){
                      if (response.data.status == "success") {
                          $scope.departmentNursingStationMapperDtoslist = [];

                            $scope.departmentNursingStationMapperDtoslist = response.data.listObject;
                            //alert($scope.roleName);
                            if($scope.nurseRole == 1){
                            if($scope.departmentNursingStationMapperDtoslist.length > 0)
                                {
                                    var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                                      GenericService.serviceAction("GET",URI,{}).then(function(response){
                                          if (response.data.status == "success") {
                                              $scope.departmentNursingStationListC = {};
                                             // console.log("fff",response.data.object);
                                              $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;

                                              $cookies.putObject('specialtyNursingStationCookies',$scope.departmentNursingStationListC);
                                              $cookies.putObject('specialtyNursingStationCookiesNur',$scope.departmentNursingStationListC);
                                              $cookies.putObject('nursingStationsWardsList',response.data.object);
                                              $cookies.putObject('nursingStationId',$scope.departmentNursingStationMapperDtoslist[0].nursingStationId);
                                              //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
                                              $cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc);
                                              $scope.wardListForGetBedList = response.data.object;
                                              console.log("$scope.wardListForGetBedList===",$scope.wardListForGetBedList);
                                          }
                                     });
                                }
                            }
                        }
                 });*/

            }

            $scope.getLists();

            $scope.searchPatient = function(patientSearchObj){

                $rootScope.startLoader();
                $scope.getIpRenderedDateChangeFlag(patientSearchObj);
                console.log("patientSearchObj=====",patientSearchObj);
                if(patientSearchObj.patientName!="" && patientSearchObj.patientName.length < 3){
                    growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
                    $rootScope.stopLoader();
                    return false;
                }
                patientSearchObj.wardList = $scope.wardListForGetBedList;
                patientSearchObj.doctorId = "";


                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByICUAndER";
//                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaForICUAndER";

                if(patientSearchObj.birthDate != "" && typeof(patientSearchObj.birthDate) != "undefined"){
                    patientSearchObj.birthDate = moment(patientSearchObj.birthDate).format('YYYY-MM-DD');
                }

                BillingGenericService.serviceAction(METHOD_POST,URI, patientSearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                    $scope.patientList = response.data.listObject;

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

            }

            $scope.getVitalList = function() {

                $rootScope.startLoader();
                var headerObj={
                        "unit_id" : $scope.unitId,
                        "organization_id" : $scope.organizationId
                };
                var paramObjForPer={
                };
                var data={};
                var URI = BASE_URL + ROOT_URL
                 + GETVITALMASTERLIST;

                GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer, headerObj).then(
                         function(response) {
                             console.log(response);
                             if (response.data.status == "success")
                                 $scope.vitalsList = [];
                                 if($scope.patientDetails.isIcuu == true){
                                     $scope.vitalsList = response.data.listObject;
                                 }else{
                                     angular.forEach(response.data.listObject,function(value,key){
                                         if(value.isIcu == false){
                                             $scope.vitalsList.push(value);
                                         }
                                     });
                                 }

                             console.log("Vital List---------", $scope.vitalsList);
                         });
               $rootScope.stopLoader();

            }

            $scope.getVitalList();

            $scope.getCurrentAppointmentVitalMapper=function(){

                $rootScope.startLoader();
                var paramObjForVital = {
                        patientId : $scope.patientDetails.patientId,
                        encounterId : $scope.patientDetails.admissionId,
                        roleId:$scope.roleId,
                        flag:'Y' //for admission
                }
                var headerObj={
                    "unit_id" : $scope.unitId,
                    "organization_id" : $scope.organizationId
                };
                var data={
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    patientId:$scope.patientDetails.patientId,
                };

                var URI = BASE_URL + ROOT_URL
                    + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
                GenericServiceParamHeader
                    .serviceAction(METHOD_POST, URI,
                            data,
                            NOTIFICATION_MSG_STATUS_FALSE,paramObjForVital,headerObj)
                    .then(
                        function(response) {
                            console.log(response);

                                if (response.data.status == "success")
                                    $scope.vitalFinalList = [];
                                    $scope.patientViatalList  = response.data.listObject;

                                    $scope.vitalDateList = [];
                                    $scope.vitalNameList = [];
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

                                     angular.forEach($scope.vitalsList, function(value , key) {
                                         var checkFlag = false;
                                         angular.forEach($scope.patientViatalList, function(value1 , key1) {

                                             if(value.vitalId == value1.vitalId){
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
                                                    $scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
                                                    $scope.newArr.isRequired = value.isRequired;
                                                    $scope.newArr.checkQualifier = true;
                                                    if($scope.roleId == 3)
                                                    {
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
                                                    newArr.vitalOrderOpd  = value.vitalOrderOpd;
                                                    newArr.checkQualifier = true;
                                                    if($scope.roleId == 3)
                                                    {
                                                        newArr.appointmentVitalMapperId ='';
                                                    }
                                                    $scope.vitalFinalList.push(newArr);
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
                                    console.log("refreshlist====================================",$scope.vitalFinalList);
                                    $rootScope.stopLoader();

                        });
            }

            $scope.selectedPainCheckBox= "";
            $scope.prescribing = function(num,vals,idx){
                $scope.selectedPainCheckBox= "";
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
                console.log("$scope.selectedCheckBox===============",$scope.selectedPainCheckBox);
                $scope.selectKeyValue(5,'Pain',1,'Describe Pain',$scope.selectedPainCheckBox);

            }

            $scope.prescribingNonverbal = function(num,vals,idx){
                $scope.selectedPainCheckBox= "";
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
                console.log("$scope.selectedCheckBox===============",$scope.selectedPainCheckBox);
                $scope.selectKeyValue(5,'Pain',2,'Nonverbal signs',$scope.selectedPainCheckBox);

            }


            $scope.selectedInsertionsCheckBox= "";
            $scope.prescribingInsertions = function(){
                $scope.selectedInsertionsCheckBox= "";
                if($scope.checkBox34 =='Y'){
                    var temp = "1";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox35 =='Y'){
                    var temp = "2";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox36 =='Y'){
                    var temp = "3";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox37 =='Y'){
                    var temp = "4";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox38 =='Y'){
                    var temp = "5";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox39 =='Y'){
                    var temp = "6";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox40 =='Y'){
                    var temp = "7";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox41 =='Y'){
                    var temp = "8";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox42 =='Y'){
                    var temp = "9";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }
                if($scope.checkBox43 =='Y'){
                    var temp = "10";
                    $scope.selectedInsertionsCheckBox += temp+" ";
                }

                console.log("$scope.selectedInsertionsCheckBox===============",$scope.selectedInsertionsCheckBox);
                $scope.selectKeyValue(13,'Insertions',0,'Insertions Checkbox',$scope.selectedInsertionsCheckBox);
            }

            $scope.selectedImmediateCheckBox= "";
            $scope.prescribingImmediate = function(){
                $scope.selectedImmediateCheckBox= "";
                if($scope.checkBox44 =='Y'){
                    var temp = "1";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox45 =='Y'){
                    var temp = "2";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox46 =='Y'){
                    var temp = "3";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox47 =='Y'){
                    var temp = "4";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox48 =='Y'){
                    var temp = "5";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox49 =='Y'){
                    var temp = "6";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox50 =='Y'){
                    var temp = "7";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox51 =='Y'){
                    var temp = "8";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox52 =='Y'){
                    var temp = "9";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                if($scope.checkBox53 =='Y'){
                    var temp = "10";
                    $scope.selectedImmediateCheckBox += temp+" ";
                }
                console.log("$scope.selectedImmediateCheckBox===============",$scope.selectedImmediateCheckBox);
                $scope.selectKeyValue(14,'Immediate Care',0,'Immediate Checkbox',$scope.selectedImmediateCheckBox);
            }

            $scope.selectKeyValue = function(initial,initialName,index,key,value){
            	
            	if (key == 'pulse'){
            		if ( parseInt(value) < parseInt($scope.vitalPulseMin)) {
//            			growl.warning("Pulse should be greater than min value min value of "+$scope.vitalSysBloodPressMax);
//    			        $scope.vitalPulse = "";
//    			        value = "";// Reset to min value if below minimum
    			    } else if (parseInt(value) > parseInt($scope.vitalPulseMax)) {
//    			    	growl.warning("Pulse blood pressure should be is less than max value of" + $scope.vitalPulseMax);
//    			        $scope.vitalPulse = "";  // Reset to max value if above maximum
//    			        value = "";
    			    }
            	}
            	
            	if (key == 'Systolic blood pressure'){
            		if (parseInt(value) < parseInt($scope.vitalSysBloodPressMin) ) {
//            			growl.warning("Systolic blood pressure  should be greater than min value min value of "+$scope.vitalSysBloodPressMax);
//    			        $scope.vitalSysBloodPress = "";  // Reset to min value if below minimum
//    			        value = ""; 
    			    } else if (parseInt(value) > parseInt($scope.vitalSysBloodPressMax)) {
//    			    	growl.warning("Systolic blood pressure should be is less than max value of"+$scope.vitalSysBloodPressMax);
//    			        $scope.vitalSysBloodPress = "";  // Reset to max value if above maximum
//    			        value = "";
    			    }
            	}
            	
            	if (key == 'Diastolic blood pressure'){
            		if (parseInt(value) < parseInt($scope.vitalDaiBloodPressMin) ) {
//            			growl.warning("Diastolic blood pressure should be greater than min value min value of"+$scope.vitalDaiBloodPressMin);
//    			        $scope.vitalDaiBloodPress = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > parseInt($scope.vitalDaiBloodPressMax)) {
//    			    	growl.warning("Diastolic blood pressure should be is less than max value of"+$scope.vitalDaiBloodPressMax);
//    			        $scope.vitalDaiBloodPress = ""; // Reset to max value if above maximum
//    			        value = "";
    			    }
            	}
            	if (key == 'Temperature'){
            		if ( parseInt(value) < parseInt($scope.vitalTemperatureMin) ) {
//            			growl.warning("Temperature should be greater than min value min value of"+$scope.vitalTemperatureMin);
//    			        $scope.vitalTemperature = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > parseInt($scope.vitalTemperatureMax)) {
//    			    	growl.warning("Temperature rate should be is less than max value of"+$scope.vitalTemperatureMax);
//    			        $scope.vitalTemperature =""; // Reset to max value if above maximum
//    			        value = "";
    			    }
            	}
            	
            	if (key == 'Respiratory rate'){
            		if (parseInt(value) < parseInt($scope.vitalRespiratoryRateMin) ) {
//            			growl.warning("Respiratory rate should be greater than min value min value of"+$scope.vitalRespiratoryRateMin);
//    			        $scope.vitalRespiratoryRate = "";  // Reset to min value if below minimum
//    			        $scope.bedSideRespiratory = "";
//    			        value = "";
    			    } else if (parseInt(value) > parseInt($scope.vitalRespiratoryRateMax)) {
//    			    	growl.warning("Respiratory rate should be is less than max value  of"+$scope.vitalRespiratoryRateMax);
//    			        $scope.vitalRespiratoryRate = "";
//    			        $scope.bedSideRespiratory = "";
//    			        value = ""; // Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'Rate'){
            		if (parseInt(value) < parseInt($scope.vitalRespiratoryRateMin) ) {
//            			growl.warning("Rate  should be greater than min value min value of"+$scope.vitalRespiratoryRateMin);
//    			        $scope.respirationRate = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > parseInt($scope.vitalRespiratoryRateMax)) {
//    			    	growl.warning("Rate should be is less than max value of"+$scope.vitalRespiratoryRateMax);
//    			        $scope.respirationRate = "";
//    			        value = ""; // Reset to max value if above maximum
    			    }
            	}
            	
            	
            	if (key == 'Oxygen saturation'){
            		if ( parseInt(value) < parseInt($scope.vitalOxySaturMin) ) {
//            			growl.warning("Oxygen saturation should be greater than min value of"+$scope.vitalOxySaturMin);
//    			        $scope.vitalOxySatur = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > parseInt($scope.vitalOxySaturMax)) {
//    			    	growl.warning("Oxygen saturation should be is less than max value of" +$scope.vitalOxySaturMax );
//    			        $scope.vitalOxySatur = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'Tidal Volume'){
            		if ( parseInt(value) < 4 ) {
//            			growl.warning(" Tidal volume should be greater than min value of 4");
//    			        $scope.bedSideTidal = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 8) {
//    			    	growl.warning(" Tidal volume should be is less than max value of 8"  );
//    			        $scope.bedSideTidal = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	if (key == 'Flow Rate'){
            		if ( parseInt(value) < 20 ) {
//            			growl.warning("Flow Rate should be greater than min value of 20");
//    			        $scope.bedSideFlow = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 100) {
//    			    	growl.warning("Flow Rate should be is less than max value of 100"  );
//    			        $scope.bedSideFlow = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'FiO2'){
            		if ( parseInt(value) < 21 ) {
//            			growl.warning("FiO2 should be greater than min value of 21");
//    			        $scope.respirationFiO2 = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 100) {
//    			    	growl.warning("FiO2 should be is less than max value of  100"  );
//    			        $scope.respirationFiO2 = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'PEEP'){
            		if ( parseInt(value) < 3 ) {
//            			growl.warning("PEEP should be greater than min value of 3-5");
//    			        $scope.respirationPEEP = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 25) {
//    			    	growl.warning("PEEP should be is less than max value of 20-25"  );
//    			        $scope.respirationPEEP = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'PS'){
            		if ( parseInt(value) < 5 ) {
//            			growl.warning("PS should be greater than min value of 5 ");
//    			        $scope.respirationPS = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 20) {
//    			    	growl.warning("PS should be is less than max value of 20 "  );
//    			        $scope.respirationPS = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'Tidal'){
            		if ( parseInt(value) < 4 ) {
//            			growl.warning("Tidal should be greater than min value of 4  ");
//    			        $scope.respirationTidal = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 8) {
//    			    	growl.warning("Tidal should be is less than max value of 8 "  );
//    			        $scope.respirationTidal = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'Flow'){
            		if ( parseInt(value) < 20 ) {
//            			growl.warning("Flow should be greater than min value of 20");
//    			        $scope.respirationFlow = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 100) {
//    			    	growl.warning("Flow should be is less than max value of 100"  );
//    			        $scope.respirationFlow = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'I Time'){
            		if ( parseInt(value) < 0.5 ) {
//            			growl.warning("I Time should be greater than min value of 20");
//    			        $scope.respirationITime = "";  // Reset to min value if below minimum
//    			        value = "";
    			    } else if (parseInt(value) > 2.0 ) {
//    			    	growl.warning("I Time should be is less than max value of 100"  );
//    			        $scope.respirationITime = "";
//    			        value = "";// Reset to max value if above maximum
    			    }
            	}
            	
            	if (key == 'Inspiratory Expiratory'){
            		
            		
            	    const ratioParts = $scope.bedSideInspiratory.split(":");
            	    if (ratioParts.length !== 2 || isNaN(ratioParts[0]) || isNaN(ratioParts[1])) {
            	    	growl.warning("Invalid I/E ratio format. Use format like 1:2.");
            	    	$scope.bedSideInspiratory = "";
    			          value = "";// Reset to max value if above maximum
            	    }

            	    const inspiratory = parseFloat(ratioParts[0]);
            	    const expiratory = parseFloat(ratioParts[1]);

            	    // Check the conditions
            	    if (inspiratory / expiratory < 0.25) {
            	    	growl.warning("Ratio is too low: Minimum is 1:4 (long expiration for obstructive diseases).");
            	    	  $scope.bedSideInspiratory = "";
      			          value = "";// Reset to max value if above maximum
            	    } else if (inspiratory / expiratory > 2) {
            	    	growl.warning("Ratio is too high: Maximum is 2:1 (long inspiration for oxygenation issues).");
            	    	  $scope.bedSideInspiratory = "";
      			          value = "";// Reset to max value if above maximum
            	    } else if (inspiratory / expiratory === 1 || expiratory / inspiratory === 1.5) {
            	    	  growl.warning("Usual range detected: 1:2 or 1:1.5.");
            	    	  $scope.bedSideInspiratory = "";
      			          value = "";// Reset to max value if above maximum
            	    } 
            	
            	}
            	
                var obj = {
                    assessmentId:initial,
                    assessment:initialName,
                    indexId:index,
                    assessmentKey:key,
                    assessmentValue:value.toString()
                }
                $scope.updateOrPushNutritionObject(obj);
                console.log("updated $scope.selectedKeyValueList======",$scope.selectedKeyValueList);

            }

              $scope.validation = function(initial,initialName,index,key,value){

                        	if (key == 'pulse'){
                        	if ( parseInt(value) < 0 ) {
                        			growl.warning("Pulse should be greater than min value of 0");
                			        $scope.vitalPulse = "";
                			        value = "";// Reset to min value if below minimum
                			     } else if (parseInt(value) > 250) {
                			    	growl.warning("Pulse should be is less than max value of  250");
                			        $scope.vitalPulse = "";  // Reset to max value if above maximum
                			        value = "";
                			    }
                        	}

                        	if (key == 'Systolic blood pressure'){
                        		if ( parseInt(value) < 40 ) {
                        			growl.warning("Systolic blood pressure  should be greater than min value 40");
                			        $scope.vitalSysBloodPress = "";  // Reset to min value if below minimum
                			        value = "";
                			      } else if (parseInt(value) > 350) {
                			    	growl.warning("Systolic blood pressure should be is less than max 350");
                			        $scope.vitalSysBloodPress = "";  // Reset to max value if above maximum
                			        value = "";
                			    }
                        	}

                        	if (key == 'Diastolic blood pressure'){
                        		if ( parseInt(value) < 10 ) {
                        			growl.warning("Diastolic blood pressure should be greater than min value 10");
                			        $scope.vitalDaiBloodPress = "";  // Reset to min value if below minimum
                			        value = "";
                			      } else if (parseInt(value) > 250) {
                			    	growl.warning("Diastolic blood pressure should be is less than max value 250");
                			        $scope.vitalDaiBloodPress = ""; // Reset to max value if above maximum
                			        value = "";
                			    }
                        	}
                        	if (key == 'Temperature'){
                        		if ( parseInt(value) < parseInt($scope.vitalTemperatureMin) ) {
                        			growl.warning("Temperature should be greater than min value min value of "+$scope.vitalTemperatureMin);
                			        $scope.vitalTemperature = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > parseInt($scope.vitalTemperatureMax)) {
                			    	growl.warning("Temperature rate should be is less than max value of "+$scope.vitalTemperatureMax);
                			        $scope.vitalTemperature =""; // Reset to max value if above maximum
                			        value = "";
                			    }
                        	}

                        	if (key == 'Respiratory rate'){
                        		if ( parseInt(value) < 0 ) {
                        			growl.warning("Respiratory rate should be greater than min value min value of "+$scope.vitalRespiratoryRateMin);
                			        $scope.vitalRespiratoryRate = "";  // Reset to min value if below minimum
                			        $scope.bedSideRespiratory = "";
                			        value = "";
                			     } else if (parseInt(value) > 100) {
                			    	growl.warning("Respiratory rate should be is less than max value  of "+$scope.vitalRespiratoryRateMax);
                			        $scope.vitalRespiratoryRate = "";
                			        $scope.bedSideRespiratory = "";
                			        value = ""; // Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'Rate'){
                        		if (parseInt(value) < parseInt($scope.vitalRespiratoryRateMin) ) {
                        			growl.warning("Rate  should be greater than min value min value of "+$scope.vitalRespiratoryRateMin);
                			        $scope.respirationRate = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > parseInt($scope.vitalRespiratoryRateMax)) {
                			    	growl.warning("Rate should be is less than max value of "+$scope.vitalRespiratoryRateMax);
                			        $scope.respirationRate = "";
                			        value = ""; // Reset to max value if above maximum
                			    }
                        	}


                        	if (key == 'Oxygen saturation'){
                        		if ( parseInt(value) < 50 ) {
                        			growl.warning("Oxygen saturation should be greater than min value 50");
                			        $scope.vitalOxySatur = "";  // Reset to min value if below minimum
                			        value = "";
                			     } else if (parseInt(value) > 100) {
                			    	growl.warning("Oxygen saturation should be is less than max value 100");
                			        $scope.vitalOxySatur = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'Tidal Volume'){
                        		if ( parseInt(value) < 4 ) {
                        			growl.warning(" Tidal volume should be greater than min value of 4");
                			        $scope.bedSideTidal = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > 8) {
                			    	growl.warning(" Tidal volume should be is less than max value of 8"  );
                			        $scope.bedSideTidal = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}
                        	if (key == 'Flow Rate'){
                        		if ( parseInt(value) < 20 ) {
                        			growl.warning("Flow Rate should be greater than min value of 20");
                			        $scope.bedSideFlow = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > 100) {
                			    	growl.warning("Flow Rate should be is less than max value of 100"  );
                			        $scope.bedSideFlow = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'FiO2'){
                        		if ( parseInt(value) < 21 ) {
                        			growl.warning("FiO2 should be greater than min value of 21");
                			        $scope.respirationFiO2 = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > 100) {
                			    	growl.warning("FiO2 should be is less than max value of  100"  );
                			        $scope.respirationFiO2 = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'PEEP'){
                        		if ( parseInt(value) < 3 ) {
                        			growl.warning("PEEP should be greater than min value of 3-5");
                			        $scope.respirationPEEP = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > 25) {
                			    	growl.warning("PEEP should be is less than max value of 20-25"  );
                			        $scope.respirationPEEP = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'PS'){
                        		if ( parseInt(value) < 5 ) {
                        			growl.warning("PS should be greater than min value of 5 ");
                			        $scope.respirationPS = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > 20) {
                			    	growl.warning("PS should be is less than max value of 20 "  );
                			        $scope.respirationPS = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'Tidal'){
                        		if ( parseInt(value) < 4 ) {
                        			growl.warning("Tidal should be greater than min value of 4  ");
                			        $scope.respirationTidal = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > 8) {
                			    	growl.warning("Tidal should be is less than max value of 8 "  );
                			        $scope.respirationTidal = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'Flow'){
                        		if ( parseInt(value) < 20 ) {
                        			growl.warning("Flow should be greater than min value of 20");
                			        $scope.respirationFlow = "";  // Reset to min value if below minimum
                			        value = "";
                			    } else if (parseInt(value) > 100) {
                			    	growl.warning("Flow should be is less than max value of 100"  );
                			        $scope.respirationFlow = "";
                			        value = "";// Reset to max value if above maximum
                			    }
                        	}

                        	if (key == 'I Time') {
                                const parsedValue = parseFloat(value);
                                if (parsedValue < 0.5) {
                                    growl.warning("I Time should be greater than the minimum value of 0.5");
                                    $scope.respirationITime = "";
                                    value = "";
                                } else if (parsedValue > 2.0) {
                                    growl.warning("I Time should be less than the maximum value of 2.0");
                                    $scope.respirationITime = "";
                                    value = "";
                                }
                            }

                        	if (key == 'Inspiratory Expiratory'){


                        	    const ratioParts = $scope.bedSideInspiratory.split(":");
                        	    if (ratioParts.length !== 2 || isNaN(ratioParts[0]) || isNaN(ratioParts[1])) {
                        	    	growl.warning("Invalid I/E ratio format. Use format like 1:2.");
                        	    	$scope.bedSideInspiratory = "";
                			          value = "";// Reset to max value if above maximum
                        	    }

                        	    const inspiratory = parseFloat(ratioParts[0]);
                        	    const expiratory = parseFloat(ratioParts[1]);

                        	    // Check the conditions
                        	    if (inspiratory / expiratory < 0.25) {
                        	    	growl.warning("Ratio is too low: Minimum is 1:4 (long expiration for obstructive diseases).");
                        	    	  $scope.bedSideInspiratory = "";
                  			          value = "";// Reset to max value if above maximum
                        	    } else if (inspiratory / expiratory > 2) {
                        	    	growl.warning("Ratio is too high: Maximum is 2:1 (long inspiration for oxygenation issues).");
                        	    	  $scope.bedSideInspiratory = "";
                  			          value = "";// Reset to max value if above maximum
                        	    } else if (inspiratory / expiratory === 1 || expiratory / inspiratory === 1.5) {
                        	    	  growl.warning("Usual range detected: 1:2 or 1:1.5.");
                        	    	  $scope.bedSideInspiratory = "";
                  			          value = "";// Reset to max value if above maximum
                        	    }

                        	}

                            var obj = {
                                assessmentId:initial,
                                assessment:initialName,
                                indexId:index,
                                assessmentKey:key,
                                assessmentValue:value.toString()
                            }
                            $scope.updateOrPushNutritionObject(obj);
                            console.log("updated $scope.selectedKeyValueList======",$scope.selectedKeyValueList);

                        }

            $scope.updateOrPushNutritionObject = function(obj){
                var foundIndex = -1;

                for (var i = 0; i < $scope.selectedKeyValueList.length; i++) {
                    if ($scope.selectedKeyValueList[i].assessmentId === obj.assessmentId &&
                                $scope.selectedKeyValueList[i].indexId === obj.indexId) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one if assessmentValue is not empty or null
                    if (obj.assessmentValue !== "" && obj.assessmentValue !== null) {
                        $scope.selectedKeyValueList[foundIndex] = obj;
                    } else {
                        // Remove the object at foundIndex if assessmentValue is empty or null
                        $scope.selectedKeyValueList.splice(foundIndex, 1);
                    }
                } else if (obj.assessmentValue !== "" && obj.assessmentValue !== null) {
                    // Push the new object to the array only if assessmentValue is not empty or null
                    $scope.selectedKeyValueList.push(obj);
                }

            }

            $scope.saveInitialAssessmentScore = function(saveAsDraft){

                $rootScope.startLoader();
                if($scope.selectedKeyValueList.length==0 || $scope.selectedKeyValueList==null || $scope.selectedKeyValueList==undefined){
                    growl.error("Please select any one assessment", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

              if (!$scope.startDate || !$scope.fromTime) {
                 growl.error("Please select Date and Time", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                return;
                    }

                    let timeParts = moment($scope.fromTime, "hh:mm A");

                     let combinedDateTime = new Date(
                        $scope.startDate.getFullYear(),
                        $scope.startDate.getMonth(),
                        $scope.startDate.getDate(),
                       timeParts.hour(),
                       timeParts.minute(),
                        0 
                    );


                if($scope.patientDetails.patientId == undefined || $scope.patientDetails.patientId == null || $scope.patientDetails.patientId ==""){
                    growl.error(" Please select patient ", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                } 
                
                $scope.isSaveDisabled = false; 

                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    encounterId:0,
                    admissionId:$scope.patientDetails.admissionId,
                    uhid:$scope.patientDetails.uhIdNumber,
                    initialAssessmentStatusId:$scope.initialAssessmentStatusId,
                    selectedKeyValueList:$scope.selectedKeyValueList,
                    infusions:$scope.infusions,
                    saveAsDraft:saveAsDraft,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    startDateTime: moment(combinedDateTime).format('DD-MM-YYYY HH:mm:ss')
                }

                console.log("saveObj=========",saveObj);

                var URI = BASE_URL + ROOT_URL +"/public/saveInitialAssessmentICUAndER";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         $scope.patientList = [];
                         $scope.blankPatientDetails();
                         $scope.blankPatientSearchObj();
                         $scope.blankGetData();
                         growl.success(response.data.message,
                         {
                            title : 'Success!'
                         });
                            $scope.startDate = null;
                            $scope.endDate = null;

                            $scope.fromTime = null;
                            $scope.toTime = null;
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }
                });
            }


            $scope.getInitialAssessmentScore = function(index){

                var URI1 = BASE_URL + ROOT_URL +"/public/getInitialAssessmentICUAndERList/"+$scope.unitId+"/"+$scope.organizationId+"/"+$scope.patientDetails.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.assessmentSelectedList = [];
                    if(response.data.status == "success"){
                      if (response.data.object) {
                        var parts = response.data.object.split(" ");
                        if (parts.length == 2) {
                          var dateParts = parts[0].split("/"); 
                          var day = parseInt(dateParts[0], 10);
                          var month = parseInt(dateParts[1], 10) - 1; 
                          var year = parseInt(dateParts[2], 10);
                          $scope.startDate = new Date(year, month, day);
                          $scope.fromTime = parts[1].split(":")[0] + ":" + parts[1].split(":")[1];
                        }
                      }

                       $rootScope.stopLoader();
                        $scope.selectedKeyValueList = response.data.listObject;
                        $scope.isSaveDisabled = false;
                        if($scope.selectedKeyValueList.length>0){
                            $scope.initialAssessmentStatusId = ($scope.selectedKeyValueList[0].initialAssessmentStatusId!=null)?$scope.selectedKeyValueList[0].initialAssessmentStatusId:"";
                            $scope.infusions = ($scope.selectedKeyValueList[0].infusions!=null)?$scope.selectedKeyValueList[0].infusions:"";

                            angular.forEach($scope.selectedKeyValueList, function(value,id) {
                                $scope.setInitialAssessmentScore(value,id);
                            });
                        }else{
                             $scope.blankGetData();
                        }
                    }
                    else if(response.data.status == 'error'){
                        var idForcheckbox = "patientSelectRadioCommon_" + index; 
                        var element = document.getElementById(idForcheckbox);
                        $scope.isSaveDisabled = true; 
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.setInitialAssessmentScore = function(value,id){

                console.log("selectedKeyValueList value=========",value);
//                var dnj = {
//                    id:"",
//                    name:""
//                }

                if(value.assessmentId==0){
                    if(value.indexId==0){
                        $scope.vitalPulse = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.vitalSysBloodPress = Number(value.assessmentValue);
                    }else if(value.indexId==2){
                        $scope.vitalDaiBloodPress = Number(value.assessmentValue);
                    }else if(value.indexId==3){
                        $scope.vitalTemperature = Number(value.assessmentValue);
                    }else if(value.indexId==4){
                        $scope.vitalRespiratoryRate = Number(value.assessmentValue);
                    }else if(value.indexId==5){
                        $scope.vitalOxySatur = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==1){
                    if(value.indexId==0){
                        $scope.nutritionDiet = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.nutritionWeightChanges = Number(value.assessmentValue);
                    }else if(value.indexId==2){
                        $scope.nutritionSupplements = Number(value.assessmentValue);
                    }else if(value.indexId==3){
                        $scope.nutritionSwallowing = Number(value.assessmentValue);
                    }else if(value.indexId==4){
                        $scope.nutritionMealtimes = Number(value.assessmentValue);
                    }else if(value.indexId==5){
                        $scope.nutritionMonitoring = Number(value.assessmentValue);
                    }else if(value.indexId==6){
                        $scope.nutritionMucous = Number(value.assessmentValue);
                    }else if(value.indexId==7){
                        $scope.nutritionSkin = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==2){
                    if(value.indexId==0){
                        $scope.bladderIncontinence = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.bowelIncontinence = Number(value.assessmentValue);
                    }else if(value.indexId==2){
                        $scope.incontManageTech = Number(value.assessmentValue);
                    }else if(value.indexId==3){
                        $scope.bowelSoundPresent = Number(value.assessmentValue);
                    }else if(value.indexId==4){
                        $scope.EliminationConstipation = Number(value.assessmentValue);
                    }else if(value.indexId==5){
                        $scope.eliminationOostomies = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==3){
                    if(value.indexId==0){
                        $scope.sensoryVision = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.sensoryHearing = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==4){
                    if(value.indexId==0){
                        $scope.musculoskeletalMobility = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.musculoskeletalRom = Number(value.assessmentValue);
                    }else if(value.indexId==2){
                        $scope.musculoskeletalMotorDev = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==5){
                    if(value.indexId==0){
                        $scope.painAssessment = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.setPainAssessmentCheckBox(value,id);
                    }else if(value.indexId==2){
                       $scope.setPainAssessmentCheckBox(value,id);
                    }else if(value.indexId==3){
                        $scope.painMotorDev = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==6){
                    if(value.indexId==0){
                        $scope.skinAssessment = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                       $scope.skinIntact = Number(value.assessmentValue);
                    }else if(value.indexId==2){
                        $scope.skinSpecialCare = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==7){
                    if(value.indexId==0){
                        $scope.neurologicalSensation = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.neurologicalVerbal = Number(value.assessmentValue);
                    }else if(value.indexId==2){
                        $scope.neurologicalAphasia = Number(value.assessmentValue);
                    }else if(value.indexId==3){
                        $scope.neurologicalMemory = Number(value.assessmentValue);
                    }else if(value.indexId==4){
                        $scope.neurologicalDecision = Number(value.assessmentValue);
                    }else if(value.indexId==5){
                        $scope.neurologicalSleep = Number(value.assessmentValue);
                    }else if(value.indexId==6){
                        $scope.neurologicalSeizures = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==8){
                    if(value.indexId==0){
                        $scope.circulationHistory = Number(value.assessmentValue);
                    }else if(value.indexId==1){
                        $scope.circulationPulse = Number(value.assessmentValue);
                    }else if(value.indexId==2){
                        $scope.circulationSkin = Number(value.assessmentValue);
                    }else if(value.indexId==3){
                        $scope.circulationEdema = Number(value.assessmentValue);
                    }else if(value.indexId==4){
                        $scope.circulationPitting = Number(value.assessmentValue);
                    }
                }else if(value.assessmentId==9){
                    if(value.indexId==0){
                        $scope.respirationRespiration = value.assessmentValue;
                    }else if(value.indexId==1){
                        $scope.respirationShortness = value.assessmentValue;
                    }else if(value.indexId==2){
                        $scope.respirationTreatment = value.assessmentValue;
                    }else if(value.indexId==3){
                        $scope.respirationFiO2 = value.assessmentValue;
                    }else if(value.indexId==4){
                        $scope.respirationPEEP = value.assessmentValue;
                    }else if(value.indexId==5){
                        $scope.respirationPS = value.assessmentValue;
                    }else if(value.indexId==6){
                        $scope.respirationTidal = value.assessmentValue;
                    }else if(value.indexId==7){
                        $scope.respirationRate = value.assessmentValue;
                    }else if(value.indexId==8){
                        $scope.respirationIE = value.assessmentValue;
                    }else if(value.indexId==9){
                        $scope.respirationFlow = value.assessmentValue;
                    }else if(value.indexId==10){
                        $scope.respirationITime = value.assessmentValue;
                    }else if(value.indexId==11){
                        $scope.respirationBreathR = value.assessmentValue;
                    }else if(value.indexId==12){
                        $scope.respirationBreathL = value.assessmentValue;
                    }
                }else if(value.assessmentId==10){
                    if(value.indexId==0){
                        $scope.dentalTeeth = value.assessmentValue;
                    }else if(value.indexId==1){
                        $scope.dentalHygiene = value.assessmentValue;
                    }
                }else if(value.assessmentId==11){
                    if(value.indexId==0){
                        $scope.psychosocialInjurious = value.assessmentValue;
                    }else if(value.indexId==1){
                        $scope.psychosocialBehavior = value.assessmentValue;
                    }else if(value.indexId==2){
                        $scope.psychosocialAnswers = value.assessmentValue;
                    }else if(value.indexId==3){
                        $scope.psychosocialDelusions = value.assessmentValue;
                    }
                }else if(value.assessmentId==12){
                    if(value.indexId==0){
                        $scope.bedSideRespiratory = value.assessmentValue;
                    }else if(value.indexId==1){
                        $scope.bedSideTidal = value.assessmentValue;
                    }else if(value.indexId==2){
                        $scope.bedSideFlow = value.assessmentValue;
                    }else if(value.indexId==3){
                        $scope.bedSideInspiratory = value.assessmentValue;
                    }
                }else if(value.assessmentId==13){
                     if(value.indexId==0){
                         $scope.setInsertionsCheckBox(value,id);
                     }
                }else if(value.assessmentId==14){
                     if(value.indexId==0){
                         $scope.setImmediateCheckBox(value,id);
                     }
                }

            }

            $scope.setPainAssessmentCheckBox = function(value1,id1){
                var tempString = value1.assessmentValue;
                var selectedPainAssessmentCheckBoxArray = tempString.trim().split(" ");
                console.log("selectedPainAssessmentCheckBoxArray============",selectedPainAssessmentCheckBoxArray);

                angular.forEach(selectedPainAssessmentCheckBoxArray, function(value,id) {
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
                });

            }

            $scope.setInsertionsCheckBox = function(value1,id1){
                var tempString = value1.assessmentValue;
                var selectedInsertionsCheckBoxArray = tempString.trim().split(" ");
                console.log("selectedInsertionsCheckBoxArray============",selectedInsertionsCheckBoxArray);
                angular.forEach(selectedInsertionsCheckBoxArray, function(value,id) {
                    if(value=='1'){$scope.checkBox34='Y';}
                    if(value=='2'){$scope.checkBox35='Y';}
                    if(value=='3'){$scope.checkBox36='Y';}
                    if(value=='4'){$scope.checkBox37='Y';}
                    if(value=='5'){$scope.checkBox38='Y';}
                    if(value=='6'){$scope.checkBox39='Y';}
                    if(value=='7'){$scope.checkBox40='Y';}
                    if(value=='8'){$scope.checkBox41='Y';}
                    if(value=='9'){$scope.checkBox42='Y';}
                    if(value=='10'){$scope.checkBox43='Y';}
                });
            }

            $scope.setImmediateCheckBox = function(value1,id1){
                var tempString = value1.assessmentValue;
                var selectedImmediateCheckBoxArray = tempString.trim().split(" ");
                console.log("selectedImmediateCheckBoxArray============",selectedImmediateCheckBoxArray);
                angular.forEach(selectedImmediateCheckBoxArray, function(value,id) {
                    if(value=='1'){$scope.checkBox44='Y';}
                    if(value=='2'){$scope.checkBox45='Y';}
                    if(value=='3'){$scope.checkBox46='Y';}
                    if(value=='4'){$scope.checkBox47='Y';}
                    if(value=='5'){$scope.checkBox48='Y';}
                    if(value=='6'){$scope.checkBox49='Y';}
                    if(value=='7'){$scope.checkBox50='Y';}
                    if(value=='8'){$scope.checkBox51='Y';}
                    if(value=='9'){$scope.checkBox52='Y';}
                    if(value=='10'){$scope.checkBox53='Y';}
                });
            }

            $scope.getSexMasterList = function(){
                var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
                BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                    $scope.sexMasterList = response.data.listObject;
                });
            }
            $scope.getSexMasterList();
            
    		$scope.getVitalList = function() {
				var headerObj={
						"unit_id" : $scope.unitId,
						"organization_id" : $scope.organizationId
				};
				var paramObjForPer={};
				var data={};
			 var URI = BASE_URL + ROOT_URL
             + GETVITALMASTERLIST;
			 GenericServiceParamHeader
             .serviceAction(METHOD_GET, URI, data,
                     NOTIFICATION_MSG_STATUS_FALSE,
                     paramObjForPer, headerObj)
             .then(
                     function(response) {
                         if (response.data.status == "success"){
                        	 $scope.vitalsList = [];
                             $scope.vitalsList = response.data.listObject;
                             
                             $scope.vitalPulseMin=0;
                             $scope.vitalPulseMax=0;
                             $scope.vitalSysBloodPressMin=0;
                             $scope.vitalSysBloodPressMax=0;
                             $scope.vitalDaiBloodPressMin=0;
                             $scope.vitalDaiBloodPressMax=0;
                             $scope.vitalTemperatureMin=0;
                             $scope.vitalTemperatureMax=0;
                             $scope.vitalRespiratoryRateMin=0;
                             $scope.vitalRespiratoryRateMax=0;
                             $scope.vitalOxySaturMin=0;
                             $scope.vitalOxySaturMax=0;
                             angular.forEach($scope.vitalsList,function(v,k){
                            	 if(v.vitalId == 4){
                            		 $scope.vitalPulseMin=v.minValue;
                            		 $scope.vitalPulseMax=v.maxValue;
                            	 }
                            	 if(v.vitalId == 3){
                            		 $scope.vitalSysBloodPressMin=v.minValue;
                            		 $scope.vitalSysBloodPressMax=v.maxValue;
                           	    }
                            	 if(v.vitalId == 7){
                            		 $scope.vitalDaiBloodPressMin=v.minValue;
                            		 $scope.vitalDaiBloodPressMax=v.maxValue;
                           	    }
                            	 if(v.vitalId == 6){
                            		 $scope.vitalTemperatureMin=v.minValue;
                            		 $scope.vitalTemperatureMax=v.maxValue;
                           	    }
                            	 if(v.vitalId == 10 ){
                            		 $scope.vitalRespiratoryRateMin=v.minValue;
                            		 $scope.vitalRespiratoryRateMax=v.maxValue;
                           	    }
                            	 if(v.vitalId == 12 ){
                            		 $scope.vitalOxySaturMin=v.minValue;
                            		 $scope.vitalOxySaturMax=v.maxValue;
                           	    }

                             })
                             
                             console.log("vital list",$scope.vitalsList);
                         }
                     });
		}
		$scope.getVitalList();
		

            $scope.blankAllSelectedCheckBox = function(){
                $scope.checkBox1="";
                $scope.checkBox2="";
                $scope.checkBox3="";
                $scope.checkBox4="";
                $scope.checkBox5="";
                $scope.checkBox6="";
                $scope.checkBox7="";
                $scope.checkBox8="";
                $scope.checkBox9="";
                $scope.checkBox10="";
                $scope.checkBox11="";
                $scope.checkBox12="";
                $scope.checkBox13="";
                $scope.checkBox14="";
                $scope.checkBox15="";
                $scope.checkBox16="";
                $scope.checkBox17="";
                $scope.checkBox18="";
                $scope.checkBox19="";
                $scope.checkBox20="";
                $scope.checkBox21="";
                $scope.checkBox22="";
                $scope.checkBox23="";
                $scope.checkBox24="";
                $scope.checkBox25="";
                $scope.checkBox26="";
                $scope.checkBox27="";
                $scope.checkBox28="";
                $scope.checkBox29="";
                $scope.checkBox30="";
                $scope.checkBox31="";
                $scope.checkBox32="";
                $scope.checkBox33="";
                $scope.checkBox34="";
                $scope.checkBox35="";
                $scope.checkBox36="";
                $scope.checkBox37="";
                $scope.checkBox38="";
                $scope.checkBox39="";
                $scope.checkBox40="";
                $scope.checkBox41="";
                $scope.checkBox42="";
                $scope.checkBox43="";
                $scope.checkBox44="";
                $scope.checkBox45="";
                $scope.checkBox46="";
                $scope.checkBox47="";
                $scope.checkBox48="";
                $scope.checkBox49="";
                $scope.checkBox50="";
                $scope.checkBox51="";
                $scope.checkBox52="";
                $scope.checkBox53="";
            }


        }

        initializeController();


$scope.dobPicker = {
    opened: false
};

$scope.startDate = null;
$scope.endDate = null;

$scope.fromTime = null;
$scope.toTime = null;

$scope.openDatePicker = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.dobPicker.opened = true;
};

$scope.dateOptions = {
    formatYear: 'yyyy',
    showWeeks: false,
    maxDate: new Date()
};


// // Make sure $timeout is injected
// $scope.initClockPicker = function() {
//     alert("initClockPicker called");
//     $timeout(function() {
//         // Initialize clockpicker
//         $('.clockpicker').clockpicker({
//             autoclose: true,
//             placement: 'bottom'
//         });

//         // Optional: Adjust popover position on scroll
//         $("#page-content-wrapper").scroll(function() {
//             var inputField = $(".clockpicker input");
//             if(inputField.length > 0) {
//                 var topPos = inputField.offset().top;
//                 var leftPos = inputField.offset().left;
//                 var inputHeight = inputField.outerHeight();

//                 $(".popover").css("top", topPos + inputHeight + 5);
//                 $(".popover").css("left", leftPos);
//             }
//         });
//     }, 0);
// };


$timeout(function () {
    $('#fromTime').clockpicker({
        autoclose: true,
        placement: 'bottom'
    });
}, 0);



$scope.openClockPicker = function ($event) {
    $('#fromTime').click();
};


    }

    function config($stateProvider) {
        $stateProvider.state("initialAssessmentForICU", {
            url: "/initialassessmentForICU",
            title: 'Initial Assessment for ER',
            templateUrl: 'views/master/UnitMaster/organizationMaster/initial-assessment.html',
            controller: "InitialAssessmentForICU.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("InitialAssessmentForICU.Controller", InitialAssessmentForICU);
})();