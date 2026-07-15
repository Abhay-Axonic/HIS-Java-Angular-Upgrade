(function () {
    "use strict";
        /**
         * @Author By  Akshay Narale
         */
    function NutritionalAssessment($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload, GenericServiceParamHeader) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){
            $state.go('login'); return;
        }

        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId;
        $scope.roleId = 3;
        $scope.calculatedBmi="";
        $scope.assessmentSelectedList = [];
        $scope.formData={};
        $scope.nutritionalScoreIndicatorText = "";

        //$scope.formData.bodyMass="";

        function initializeController() {
            $rootScope.loginpage = true;


            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;


            $scope.getNutritionalAssessment = function(){

                $http.get('resources/assets/js/nabhJSON/nutritional-assessment.json')
                   .then(function (response) {
                       $scope.nutritionalAssessmentList = response.data;
                       console.log("$scope.nutritionalAssessmentList===========",$scope.nutritionalAssessmentList);
                       $rootScope.stopLoader();
                   })
                   .catch(function (error) {
                       $rootScope.stopLoader();
                       console.error('Error fetching JSON:', error);
                   });

            }

            $scope.getNutritionalAssessment();

            $scope.patientSearchObj = {
                "uhIdNumber" : "",
                "patientName" : "",
                "genderId" : "",
                "mobileNo" : "",
                "birthDate" : "",
                "patientType" : 1,
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

            $scope.searchPatient = function(patientsearchObj){

                $scope.blankPatientDetails();
                $scope.formData.weight = "";
                $scope.formData.height = "";
                $scope.formData.Bml = "";
                $scope.formData.knee = "";
                $scope.nutritionalDay = "";
                $scope.nutritionalEveryDay = "";
                $scope.nutritionalEveryWeek = "";
                $scope.totalSelectedNutritionalAssessmentValue = "";
                $scope.getNutritionalAssessment();
                $scope.blankScores();
                $scope.assessmentSelectedList=[];
                $scope.getIpRenderedDateChangeFlag(patientsearchObj);
                $scope.requestStatusObject = {};
                $scope.getDisRiqFlag = true;

                $scope.requestApporivedDiscountDiv = false;
                if($scope.preBillId!=undefined && $scope.preBillId>0)
                {
                    angular.element('#billEditSessionLost').modal('show');
                    return false;
                }
                $rootScope.startLoader();
                if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
                {
                    growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
                    $scope.patientList = [];
                    $rootScope.stopLoader();
                    return false;
                }
                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchForIPDNutritionalAssessment";
                patientsearchObj.visitTypeId = $scope.admissionType;
                patientsearchObj.wardList =[];
                patientsearchObj.bedList =[];

                if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
                    patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
                }

                BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
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

            $scope.getPatientDetailsByIndex = function(index) {

                $scope.formData.weight = "";
                $scope.formData.height = "";
                $scope.formData.Bml = "";
                $scope.formData.knee = "";
                $scope.nutritionalDay = "";
                $scope.nutritionalEveryDay = "";
                $scope.nutritionalEveryWeek = "";
                $scope.totalSelectedNutritionalAssessmentValue = "";
                $scope.getNutritionalAssessment();
                $scope.blankScores();
                $scope.assessmentSelectedList=[];
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
                    $scope.patientDetails.doa = ($scope.patientList[index].doa);
                    $scope.patientDetails.patientId = ($scope.patientList[index].patientId);
                    $scope.patientDetails.admissionId = ($scope.patientList[index].admissionId);
                    $scope.patientDetails.encounterId = ($scope.patientList[index].encounterId);
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

                $scope.getCurrentAppointmentVitalMapper();
                $scope.getSaveAsDraftNutritionalAssessmentScore();

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

            $scope.weightFun=function(weight){
               if(weight >100){
                    growl.warning(" Weight Value greater than 100!",
                    {
                        title : 'warning!'
                    });
//                    $scope.formData.weight="";
//                    $scope.formData.Bml="";
                    //return false;
               }
            }
 
            $scope.heightFun=function(height){
               if(height >182){
                    growl.warning(" Height Value greater than 182!",
                    {
                        title : 'warning!'
                    });
//                    $scope.formData.height="";
//                    $scope.formData.Bml="";
//                    return false;
               }
            }


            $scope.bodyMassPoint = 0;
            $scope.midArmPoint = 0;
            $scope.calPoint = 0;
            $scope.weightlossPoint = 0;
            $scope.nutritionalProblemsPoint = 0;
            $scope.comparisonPoint = 0;
            $scope.livesIndependentlyPoint = 0;
            $scope.prescriptionDrugsPoint = 0;
            $scope.sufferedPsychologicalPoint = 0;
            $scope.mobilityPoint = 0;
            $scope.neuropsychologicalProblemsfPoint = 0;
            $scope.pressureSoresPoint = 0;
            $scope.patientEatfPoint = 0;
            $scope.comsumptionMarkersPoint = 0;
            $scope.servingsFruitsPoint = 0;
            $scope.foodIntakePoint = 0;
            $scope.fluidPoint = 0;
            $scope.modeFeedingPoint = 0;

            $scope.changeBodyMassFun=function(point,idx){
                if(point.id==0){
                   $scope.bodyMassPoint=0;
                }
                else if(point.id==1){
                   $scope.bodyMassPoint=1;
                }
                else if(point.id==2){
                   $scope.bodyMassPoint=2;
                }
                else if(point.id==3){
                   $scope.bodyMassPoint=3;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.bodyMassPoint);
            }

            $scope.changeMidArmFun=function(point,idx){
                if(point.id==0){
                   $scope.midArmPoint=0.0;
                }
                else if(point.id==1){
                   $scope.midArmPoint=0.5;
                }
                else if(point.id==2){
                   $scope.midArmPoint=1.0;
                }
                else if(point.id==3){
                   $scope.midArmPoint=3;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.midArmPoint);
            }

            $scope.changeCalfFun=function(point,idx){
                if(point.id==0){
                   $scope.calPoint=0;
                }
                else if(point.id==1){
                   $scope.calPoint=1;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.calPoint);
            }

            $scope.changeWeightlossFun=function(point,idx){
                if(point.id==0){
                   $scope.weightlossPoint=0;
                }
                else if(point.id==1){
                   $scope.weightlossPoint=1;
                }
                else if(point.id==2){
                   $scope.weightlossPoint=2;
                }
                else if(point.id==3){
                   $scope.weightlossPoint=3;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.weightlossPoint);
            }

            $scope.changeNutritionalProblemsFun=function(point,idx){
                if(point.id==0){
                   $scope.nutritionalProblemsPoint=0;
                }
                else if(point.id==1){
                   $scope.nutritionalProblemsPoint=1;
                }
                else if(point.id==2){
                   $scope.nutritionalProblemsPoint=2;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.nutritionalProblemsPoint);
            }

            $scope.changeComparisonFun=function(point,idx){
                if(point.id==0){
                   $scope.comparisonPoint=0.0;
                }
                else if(point.id==1){
                   $scope.comparisonPoint=0.5;
                }
                else if(point.id==2){
                   $scope.comparisonPoint=1.0;
                }
                else if(point.id==3){
                   $scope.comparisonPoint=2.0;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.comparisonPoint);
            }

            $scope.changeLivesIndependentlyFun=function(point,idx){
                if(point.id==0){
                   $scope.livesIndependentlyPoint=0;
                }
                else if(point.id==1){
                   $scope.livesIndependentlyPoint=1;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.livesIndependentlyPoint);
            }

            $scope.changePrescriptionDrugsFun=function(point,idx){
                if(point.id==0){
                   $scope.prescriptionDrugsPoint=0;
                }
                else if(point.id==1){
                   $scope.prescriptionDrugsPoint=1;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.prescriptionDrugsPoint);
            }

            $scope.changeSufferedPsychologicalFun=function(point,idx){
                if(point.id==0){
                   $scope.sufferedPsychologicalPoint=0;
                }
                else if(point.id==1){
                   $scope.sufferedPsychologicalPoint=2;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.sufferedPsychologicalPoint);
            }

            $scope.changeMobilityFun=function(point,idx){
                if(point.id==0){
                   $scope.mobilityPoint=0;
                }
                else if(point.id==1){
                   $scope.mobilityPoint=1;
                }
                else if(point.id==2){
                   $scope.mobilityPoint=2;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.mobilityPoint);
            }

            $scope.changeNeuropsychologicalProblemsfFun=function(point,idx){
                if(point.id==0){
                   $scope.neuropsychologicalProblemsfPoint=0;
                }
                else if(point.id==1){
                   $scope.neuropsychologicalProblemsfPoint=1;
                }
                else if(point.id==2){
                   $scope.neuropsychologicalProblemsfPoint=2;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.neuropsychologicalProblemsfPoint);
            }

            $scope.changePressureSoresFun=function(point,idx){
                if(point.id==0){
                   $scope.pressureSoresPoint=0;
                }
                else if(point.id==1){
                   $scope.pressureSoresPoint=1;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.pressureSoresPoint);
            }

            $scope.changePatientEatfFun=function(point,idx){
                if(point.id==0){
                   $scope.patientEatfPoint=0;
                }
                else if(point.id==1){
                   $scope.patientEatfPoint=1;
                }
                else if(point.id==2){
                   $scope.patientEatfPoint=2;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.patientEatfPoint);
            }

            $scope.changeComsumptionMarkersFun=function(point,idx){
                if(point.id==0){
                   $scope.comsumptionMarkersPoint=0.0;
                }
                else if(point.id==1){
                   $scope.comsumptionMarkersPoint=0.5;
                }
                else if(point.id==2){
                   $scope.comsumptionMarkersPoint=1;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.comsumptionMarkersPoint);
            }

            $scope.changeServingsFruitsFun=function(point,idx){
                if(point.id==0){
                   $scope.servingsFruitsPoint=0;
                }
                else if(point.id==1){
                   $scope.servingsFruitsPoint=1;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.servingsFruitsPoint);
            }

            $scope.changeFoodIntakeFun=function(point,idx){
                if(point.id==0){
                   $scope.foodIntakePoint=0;
                }
                else if(point.id==1){
                   $scope.foodIntakePoint=1;
                }
                else if(point.id==2){
                   $scope.foodIntakePoint=2;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.foodIntakePoint);
            }

            $scope.changeFluidFun=function(point,idx){
                if(point.id==0){
                   $scope.fluidPoint=0.0;
                }
                else if(point.id==1){
                   $scope.fluidPoint=0.5;
                }
                else if(point.id==2){
                   $scope.fluidPoint=1.0;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.fluidPoint);
            }

            $scope.changeModeFeedingFun=function(point,idx){
                if(point.id==0){
                   $scope.modeFeedingPoint=0;
                }
                else if(point.id==1){
                   $scope.modeFeedingPoint=1;
                }
                else if(point.id==2){
                   $scope.modeFeedingPoint=2;
                }
                var isChecked = null;
                $scope.totalAndPushNutrition(isChecked,point,idx,$scope.modeFeedingPoint);
            }

            $scope.nutritionalDayFun = function(isChecked,idx){
                var point = {
                    id: null,
                    name : "At least one serving of dairy products"
                }
                $scope.totalAndPushNutrition(isChecked,point,idx,null);
            }

            $scope.nutritionalEveryDayFun = function(isChecked,idx){
                var point = {
                    id: null,
                    name : "Meat, fish or poultry every day"
                }
                $scope.totalAndPushNutrition(isChecked,point,idx,null);
            }

            $scope.nutritionalEveryWeekFun = function(isChecked,idx){
                var point = {
                    id: null,
                    name : "Two or more servings of legumes"
                }
                $scope.totalAndPushNutrition(isChecked,point,idx,null);
            }

            $scope.blankScores = function(){
                 $scope.bodyMassPoint=0;
                 $scope.midArmPoint=0;
                 $scope.calPoint=0;
                 $scope.weightlossPoint=0;
                 $scope.nutritionalProblemsPoint=0;
                 $scope.comparisonPoint=0;
                 $scope.livesIndependentlyPoint=0;
                 $scope.prescriptionDrugsPoint=0;
                 $scope.sufferedPsychologicalPoint=0;
                 $scope.mobilityPoint=0;
                 $scope.neuropsychologicalProblemsfPoint=0;
                 $scope.pressureSoresPoint=0;
                 $scope.patientEatfPoint=0;
                 $scope.comsumptionMarkersPoint=0;
                 $scope.servingsFruitsPoint=0;
                 $scope.foodIntakePoint=0;
                 $scope.fluidPoint=0;
                 $scope.modeFeedingPoint=0;
            }

            $scope.totalAndPushNutrition = function(isChecked,point,idx,pointVal){
                var obj = {
                    nutritionalAssessmentNameId: point.id,
                    nutritionalAssessmentId: idx,
                    nutritionalAssessmentName: point.name,
                    nutritionalAssessmentNamePoint: pointVal,
                    consumptionMarkerFlag: isChecked
                }

                $scope.updateOrPushNutritionObject(obj);
                $scope.totalNutritionalAssessmentValue(point,idx);
                console.log("$scope.assessmentSelectedList============",$scope.assessmentSelectedList);
            }

            $scope.updateOrPushNutritionObject = function(obj){
                var foundIndex = -1;

                for (var i = 0; i < $scope.assessmentSelectedList.length; i++) {
                    if ($scope.assessmentSelectedList[i].nutritionalAssessmentId === obj.nutritionalAssessmentId) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one
                    $scope.assessmentSelectedList[foundIndex] = obj;
                } else {
                    // Push the new object to the array
                    $scope.assessmentSelectedList.push(obj);
                }

            }

            $scope.deselectNutritionalAssessment = function(index){

                console.log("adfghj=======================",index);
            }


            $scope.submitForm = function() {
              // Handle form submission here
            };

            $scope.totalNutritionalAssessmentValue = function(point,idx){

                $scope.totalSelectedNutritionalAssessmentValue = $scope.bodyMassPoint + $scope.midArmPoint + $scope.calPoint + $scope.weightlossPoint
                                            + $scope.nutritionalProblemsPoint + $scope.comparisonPoint + $scope.livesIndependentlyPoint + $scope.prescriptionDrugsPoint
                                            + $scope.sufferedPsychologicalPoint + $scope.mobilityPoint + $scope.neuropsychologicalProblemsfPoint + $scope.pressureSoresPoint
                                            + $scope.patientEatfPoint + $scope.comsumptionMarkersPoint + $scope.servingsFruitsPoint + $scope.foodIntakePoint + $scope.fluidPoint
                                            + $scope.modeFeedingPoint ;


                console.log("$scope.totalSelectedNutritionalAssessmentValue================",$scope.totalSelectedNutritionalAssessmentValue);

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

            $scope.calculateBMI = function(){

                if($scope.formData.height=="" || $scope.formData.weight=="" || $scope.formData.weight==undefined || $scope.formData.height==undefined){
                    $scope.formData.Bml="";
                }else{
                   if($scope.vitalFinalList!=null && $scope.vitalFinalList.length>0){
                       var height = Number($scope.formData.height);
                       var weight = Number($scope.formData.weight);
                       var bmi = 0.00;

                       $scope.indexOfBmi = $scope.vitalFinalList.findIndex(x => x.vitalId === 8);
                       $scope.bmiObj = $scope.vitalFinalList[$scope.indexOfBmi];

                       var ht = height / 100;
                       bmi = weight/(ht * ht);
                       $scope.bmiObj.vitalValue = parseFloat(bmi).toFixed(2);
                       if(!isNaN($scope.bmiObj.vitalValue) && $scope.bmiObj.vitalValue!=Infinity){
                           $scope.formData.Bml = $scope.bmiObj.vitalValue;
                       }

                       if(bmi <= 18.5){
                           $scope.bmiStatus='BMI : Underweight';
                       }
                       if(bmi >18.5 && bmi<=24.9){
                           $scope.bmiStatus='BMI : Normal';
                       }
                       if(bmi >=24.9 && bmi<=29.9){
                           $scope.bmiStatus='BMI : Overweight';
                       }
                       if(bmi >=30.0){
                           $scope.bmiStatus='BMI : Obese';
                       }
                   }
               }

            }

            $scope.blankGetData = function(){
             $scope.nutritionalDay = "";
             $scope.formData.knee = "";
             $scope.formData.Bml = "";
             $scope.formData.height = "";
             $scope.formData.weight = "";
             $scope.nutritionalEveryDay = "";
             $scope.nutritionalEveryWeek = "";
             $scope.formData.bodyMass = "";
             $scope.formData.midarm = "";
             $scope.formData.calf = "";
             $scope.formData.weightloss = "";
             $scope.formData.nutritionalProblems = "";
             $scope.formData.comparison = "";
             $scope.formData.livesIndependently = "";
             $scope.formData.prescriptionDrugs = "";
              $scope.formData.sufferedPsychological = "";
             $scope.formData.mobility = "";
             $scope.formData.neuropsychologicalProblems = "";
             $scope.formData.pressureSores = "";
             $scope.formData.patientEat = "";
             $scope.formData.comsumptionMarkers = "";
             $scope.formData.servingsFruits = "";
             $scope.formData.foodIntake = "";
             $scope.formData.fluid = "";
             $scope.formData.modeFeeding = "";
             $scope.totalSelectedNutritionalAssessmentValue = "";
            $scope.bodyMassPoint = 0;
            $scope.midArmPoint = 0;
            $scope.calPoint = 0;
            $scope.weightlossPoint = 0;
            $scope.nutritionalProblemsPoint = 0;
            $scope.comparisonPoint = 0;
            $scope.livesIndependentlyPoint = 0;
            $scope.prescriptionDrugsPoint = 0;
            $scope.sufferedPsychologicalPoint = 0;
            $scope.mobilityPoint = 0;
            $scope.neuropsychologicalProblemsfPoint = 0;
            $scope.pressureSoresPoint = 0;
            $scope.patientEatfPoint = 0;
            $scope.comsumptionMarkersPoint = 0;
            $scope.servingsFruitsPoint = 0;
            $scope.foodIntakePoint = 0;
            $scope.fluidPoint = 0;
            $scope.modeFeedingPoint = 0;
             }

            $scope.saveRack = function(){
                $scope.blankPatientSearchObj();
                $scope.patientList = [];
                $scope.blankPatientDetails();
                $scope.formData.weight = "";
                $scope.formData.height = "";
                $scope.formData.Bml = "";
                $scope.formData.knee = "";
                $scope.blankScores();
                $scope.nutritionalDay = "";
                $scope.nutritionalEveryDay = "";
                $scope.nutritionalEveryWeek = "";
                $scope.assessmentSelectedList = [];
                $scope.totalSelectedNutritionalAssessmentValue = "";
                $scope.getNutritionalAssessment();
            }

            

            $scope.saveNutritionalAssessmentScore = function(saveAsDraft){

                $rootScope.startLoader();

                if($scope.assessmentSelectedList.length>0){
                    var tempAssessmentSelectedList = $scope.assessmentSelectedList;
                    $scope.assessmentSelectedList = [];
                    angular.forEach(tempAssessmentSelectedList, function(value,id) {
                        var obj = {
                            consumptionMarkerFlag: value.consumptionMarkerFlag,
                            nutritionalAssessmentId: value.nutritionalAssessmentId,
                            nutritionalAssessmentName: value.nutritionalAssessmentName,
                            nutritionalAssessmentNameId: value.nutritionalAssessmentNameId,
                            nutritionalAssessmentNamePoint: value.nutritionalAssessmentNamePoint
                        }
                        $scope.assessmentSelectedList.push(obj);

                    });
                }

                if($scope.patientDetails.admissionId==null || $scope.patientDetails.admissionId==undefined || $scope.patientDetails.admissionId==""){
                    growl.error("Please select the patient", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.assessmentSelectedList.length<1 || $scope.assessmentSelectedList==undefined || $scope.assessmentSelectedList==null){
                    growl.error("Please select at least one assessment", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

//                if($scope.totalSelectedNutritionalAssessmentValue==undefined || $scope.totalSelectedNutritionalAssessmentValue==null || $scope.totalSelectedNutritionalAssessmentValue==""){
//                    growl.error("Please select at least one assessment", {
//                        title: 'Error!'
//                    });
//                    $rootScope.stopLoader();
//                    return;
//                }

                if(saveAsDraft=='N'){

                    if($scope.formData.weight==null || $scope.formData.weight==undefined || $scope.formData.weight==0){
                        growl.error("Please fill up the weight", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.formData.height==null || $scope.formData.height==undefined || $scope.formData.height==0){
                        growl.error("Please fill up the height", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.formData.Bml==null || $scope.formData.Bml==undefined || $scope.formData.Bml==0){
                        growl.error("Please fill up the BMI", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.formData.knee==null || $scope.formData.knee==undefined || $scope.formData.knee==0){
                        growl.error("Please fill up the knee height", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.assessmentSelectedList.length<21 || $scope.assessmentSelectedList==null || $scope.assessmentSelectedList==undefined){
                        growl.error("Please select all scores", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                }

                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    nutritionalAssessmentScoreMasterId :$scope.formData.nutritionalAssessmentScoreMasterId ,
                    encounterId:$scope.patientDetails.encounterId,
                    admissionId:$scope.patientDetails.admissionId,
                    patientWeight:Number($scope.formData.weight),
                    patientHeight:Number($scope.formData.height),
                    patientBmi:$scope.formData.Bml,
                    patientKneeHeight:Number($scope.formData.knee),
                    totalNutritionalAssessmentScore:$scope.totalSelectedNutritionalAssessmentValue.toString(),
                    nutritionalAssessmentScoreNamesList:$scope.assessmentSelectedList,
                    saveAsDraft:saveAsDraft,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI = BASE_URL + ROOT_URL +"/adt/saveAsDraftNutritionalAssessmentScore";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         if(saveAsDraft=='Y'){
                         $scope.patientList = [];
                          $scope.assessmentSelectedList = [];
                            $scope.blankGetData();
                            $scope.blankPatientDetails();
                             $scope.blankPatientSearchObj();
//                            $scope.getSaveAsDraftNutritionalAssessmentScore();
                         }
                         if(saveAsDraft=='N'){

                             $scope.blankPatientSearchObj();
                             $scope.patientList = [];
                             $scope.blankPatientDetails();

                             $scope.formData.weight = "";
                             $scope.formData.height = "";
                             $scope.formData.Bml = "";
                             $scope.formData.knee = "";

                             $scope.blankScores();

                             $scope.nutritionalDay = "";
                             $scope.nutritionalEveryDay = "";
                             $scope.nutritionalEveryWeek = "";

                             $scope.assessmentSelectedList = [];
                             $scope.totalSelectedNutritionalAssessmentValue = "";
                             $scope.getNutritionalAssessment();
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

            $scope.getSaveAsDraftNutritionalAssessmentScore = function(){

                var URI1 = BASE_URL + ROOT_URL +"/adt/getSaveAsDraftNutritionalAssessmentScore/"+$scope.unitId+"/"+$scope.organizationId+"/"+$scope.patientDetails.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.assessmentSelectedList = [];
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                        $scope.assessmentSelectedList = response.data.listObject;
                        
                        if(response.data.object != null){
                        	  $scope.formData.weight = response.data.object.patientWeight;
                              $scope.formData.height = response.data.object.patientHeight;
                              $scope.formData.Bml = response.data.object.patientBmi;
                              $scope.formData.knee = response.data.object.patientKneeHeight;
                              $scope.formData.nutritionalAssessmentScoreMasterId = response.data.object.nutritionalAssessmentScoreMasterId;
                        }else{
                        	 $scope.formData.nutritionalAssessmentScoreMasterId =  null;
                        }
                      
                        angular.forEach($scope.assessmentSelectedList, function(value,id) {
                            $scope.setSaveAsDraftNutritionalAssessmentScore(value,id);
                        });

                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.setSaveAsDraftNutritionalAssessmentScore =  function(value,id){

                var dnj = {
                    id:"",
                    name:""
                }


//                {
//                    "assessmentId": 0,
//                    "assessmentName": "bodyMassIndex",
//                    "assessmentList": [
//                      {
//                        "id": 0,
//                        "name": "BMI < 19 = 0 points"
//                      },
//                      {
//                        "id": 1,
//                        "name": "BMI 19 to 21 = 1 point"
//                      },
//                      {
//                        "id": 2,
//                        "name": "BMI 21 to 23 = 2 points"
//                      },
//                      {
//                        "id": 3,
//                        "name": "BMI ≥ 23 = 3 points"
//                      }
//                    ]
//                  },
                if(value.nutritionalAssessmentId==0){
                    angular.forEach($scope.nutritionalAssessmentList[0].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.bodyMass = dnj;
                            $scope.changeBodyMassFun($scope.formData.bodyMass,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[0].assessmentList[value1.id] = $scope.formData.bodyMass;
                            console.log("sd",$scope.formData.bodyMass)

                        }
                    });
                }else if(value.nutritionalAssessmentId==1){
                    angular.forEach($scope.nutritionalAssessmentList[1].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.midarm = dnj;
                            $scope.changeMidArmFun($scope.formData.midarm,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[1].assessmentList[value1.id] = $scope.formData.midarm;
                            console.log("patientEat===============",$scope.formData.midarm)

                        }
                    });
                }else if(value.nutritionalAssessmentId==2){
                    angular.forEach($scope.nutritionalAssessmentList[2].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.calf = dnj;
                            $scope.changeCalfFun($scope.formData.calf,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[2].assessmentList[value1.id] = $scope.formData.calf;
                            console.log("patientEat===============",$scope.formData.calf)

                        }
                    });
                }else if(value.nutritionalAssessmentId==3){
                    angular.forEach($scope.nutritionalAssessmentList[3].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.weightloss = dnj;
                            $scope.changeWeightlossFun($scope.formData.weightloss,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[3].assessmentList[value1.id] = $scope.formData.weightloss;
                            console.log("patientEat===============",$scope.formData.weightloss)

                        }
                    });
                }else if(value.nutritionalAssessmentId==4){
                    angular.forEach($scope.nutritionalAssessmentList[4].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.nutritionalProblems = dnj;
                            $scope.changeNutritionalProblemsFun($scope.formData.nutritionalProblems,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[4].assessmentList[value1.id] = $scope.formData.nutritionalProblems;
                            console.log("patientEat===============",$scope.formData.nutritionalProblems)

                        }
                    });
                }else if(value.nutritionalAssessmentId==5){
                    angular.forEach($scope.nutritionalAssessmentList[5].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.comparison = dnj;
                            $scope.changeComparisonFun($scope.formData.comparison,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[5].assessmentList[value1.id] = $scope.formData.comparison;
                            console.log("patientEat===============",$scope.formData.comparison)

                        }
                    });
                }else if(value.nutritionalAssessmentId==6){
                    angular.forEach($scope.nutritionalAssessmentList[6].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.livesIndependently = dnj;
                            $scope.changeLivesIndependentlyFun($scope.formData.livesIndependently,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[6].assessmentList[value1.id] = $scope.formData.livesIndependently;
                            console.log("patientEat===============",$scope.formData.livesIndependently)

                        }
                    });
                }else if(value.nutritionalAssessmentId==7){
                    angular.forEach($scope.nutritionalAssessmentList[7].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.prescriptionDrugs = dnj;
                            $scope.changePrescriptionDrugsFun($scope.formData.prescriptionDrugs,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[7].assessmentList[value1.id] = $scope.formData.prescriptionDrugs;
                            console.log("patientEat===============",$scope.formData.prescriptionDrugs)

                        }
                    });
                }else if(value.nutritionalAssessmentId==8){
                    angular.forEach($scope.nutritionalAssessmentList[8].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.sufferedPsychological = dnj;
                            $scope.changeSufferedPsychologicalFun($scope.formData.sufferedPsychological,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[8].assessmentList[value1.id] = $scope.formData.sufferedPsychological;
                            console.log("patientEat===============",$scope.formData.sufferedPsychological)

                        }
                    });
                }else if(value.nutritionalAssessmentId==9){
                    angular.forEach($scope.nutritionalAssessmentList[9].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.mobility = dnj;
                            $scope.changeMobilityFun($scope.formData.mobility,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[9].assessmentList[value1.id] = $scope.formData.mobility;
                            console.log("patientEat===============",$scope.formData.mobility)

                        }
                    });
                }else if(value.nutritionalAssessmentId==10){
                    angular.forEach($scope.nutritionalAssessmentList[10].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.neuropsychologicalProblems = dnj;
                            $scope.changeNeuropsychologicalProblemsfFun($scope.formData.neuropsychologicalProblems,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[10].assessmentList[value1.id] = $scope.formData.neuropsychologicalProblems;
                            console.log("patientEat===============",$scope.formData.neuropsychologicalProblems)

                        }
                    });
                }else if(value.nutritionalAssessmentId==11){
                    angular.forEach($scope.nutritionalAssessmentList[11].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.pressureSores = dnj;
                            $scope.changePressureSoresFun($scope.formData.pressureSores,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[11].assessmentList[value1.id] = $scope.formData.pressureSores;
                            console.log("patientEat===============",$scope.formData.pressureSores)

                        }
                    });
                }else if(value.nutritionalAssessmentId==12){
                    angular.forEach($scope.nutritionalAssessmentList[12].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.patientEat = dnj;
                            $scope.changePatientEatfFun($scope.formData.patientEat,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[12].assessmentList[value1.id] = $scope.formData.patientEat;
                            console.log("patientEat===============",$scope.formData.patientEat)

                        }
                    });
                }else if(value.nutritionalAssessmentId==13){
                    angular.forEach($scope.nutritionalAssessmentList[13].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.comsumptionMarkers = dnj;
                            $scope.changeComsumptionMarkersFun($scope.formData.comsumptionMarkers,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[13].assessmentList[value1.id] = $scope.formData.comsumptionMarkers;
                            console.log("patientEat===============",$scope.formData.comsumptionMarkers)

                        }
                    });
                }else if(value.nutritionalAssessmentId==14){
                    angular.forEach($scope.nutritionalAssessmentList[14].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.servingsFruits = dnj;
                            $scope.changeServingsFruitsFun($scope.formData.servingsFruits,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[14].assessmentList[value1.id] = $scope.formData.servingsFruits;
                            console.log("patientEat===============",$scope.formData.servingsFruits)

                        }
                    });
                }else if(value.nutritionalAssessmentId==15){
                    angular.forEach($scope.nutritionalAssessmentList[15].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.foodIntake = dnj;
                            $scope.changeFoodIntakeFun($scope.formData.foodIntake,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[15].assessmentList[value1.id] = $scope.formData.foodIntake;
                            console.log("patientEat===============",$scope.formData.foodIntake)

                        }
                    });
                }else if(value.nutritionalAssessmentId==16){
                    angular.forEach($scope.nutritionalAssessmentList[16].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.fluid = dnj;
                            $scope.changeFluidFun($scope.formData.fluid,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[16].assessmentList[value1.id] = $scope.formData.fluid;
                            console.log("patientEat===============",$scope.formData.fluid)

                        }
                    });
                }else if(value.nutritionalAssessmentId==17){
                    angular.forEach($scope.nutritionalAssessmentList[17].assessmentList, function(value1,id1) {
                        if(value.nutritionalAssessmentNameId==value1.id){
                                 dnj.id=value.nutritionalAssessmentNameId;
                                 dnj.name=value.nutritionalAssessmentName;
                                 $scope.formData.modeFeeding = dnj;
                            $scope.changeModeFeedingFun($scope.formData.modeFeeding,value.nutritionalAssessmentId);
                            $scope.nutritionalAssessmentList[17].assessmentList[value1.id] = $scope.formData.modeFeeding;
                            console.log("patientEat===============",$scope.formData.modeFeeding)

                        }
                    });
                }else if(value.nutritionalAssessmentId==19){
                    $scope.nutritionalDay = value.consumptionMarkerFlag === 1;
                }else if(value.nutritionalAssessmentId==20){
                    $scope.nutritionalEveryDay = value.consumptionMarkerFlag === 1;
                }else if(value.nutritionalAssessmentId==21){
                    $scope.nutritionalEveryWeek = value.consumptionMarkerFlag === 1;
                }

            }
            $scope.patientDetails.uhIdNumber = "";
            $scope.patientDetails.patientName = "";
            $scope.patientDetails.genderCode =  "";
            $scope.patientDetails.patientId =  "";
            $scope.patientDetails.age = "";
            $scope.patientDetails.birthDate ="";
            $scope.patientDetails.doa = "";

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("nutritionalAssessment", {
            url: "/nutritionalAssessment",
            title: 'Nutritional Assessment',
            templateUrl: 'views/Nursing/nutritional-assessment.html',
            controller: "NutritionalAssessment.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("NutritionalAssessment.Controller", NutritionalAssessment);
})();