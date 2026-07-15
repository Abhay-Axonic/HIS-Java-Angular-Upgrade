(function () {
    "use strict";

    function PatientFallReporting($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload,GenericServiceParamHeader) {
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

        function initializeController() {
            $rootScope.loginpage = true;

            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            $scope.assessmentSelectedList=[];


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

            // function for initializa clockpicker
            $scope.initClockPicker = function() {
                $('.clockpicker').clockpicker();
            }

            $scope.getLabInspectionList = function(){

                $scope.labInspectionList=[
                         {
                           "fallId": 0,
                           "fallName": "Conscious Level before",
                           "fallList": [
                               {id:0,name:'Conscious'},
                               {id:1,name:'Semiconscious'},
                               {id:2,name:'Unconscious'}
                           ]
                         },
                         {
                           "fallId": 1,
                           "fallName": "Conscious Level After",
                           "fallList": [
                               {id:0,name:'Conscious'},
                               {id:1,name:'Semiconscious'},
                               {id:2,name:'Unconscious'}
                           ]
                         },
                         {
                           "fallId": 2,
                           "fallName": "Mental State Before",
                           "fallList": [
                               {id:0,name:'Orientated'},
                               {id:1,name:'Disorientated'},
                               {id:2,name:'Restless'},
                               {id:3,name:'Confused'},
                               {id:4,name:'Others'}
                           ]
                         },
                         {
                           "fallId": 3,
                           "fallName": "Mental State After",
                           "fallList": [
                               {id:0,name:'Orientated'},
                               {id:1,name:'Disorientated'},
                               {id:2,name:'Restless'},
                               {id:3,name:'Confused'},
                               {id:4,name:'Others'}
                           ]
                         },
                         {
                           "fallId": 4,
                           "fallName": "Ambulation Before",
                           "fallList": [
                               {id:0,name:'Independent Ambulatory'},
                               {id:1,name:'Ambulatory with assistance'},
                               {id:2,name:'Ambulatory with walking aid'},
                               {id:3,name:'Confined to bed / chair'},
                               {id:4,name:'Other'}
                           ]
                         },
                         {
                           "fallId": 5,
                           "fallName": "Ambulation After",
                           "fallList": [
                               {id:0,name:'Independent Ambulatory'},
                               {id:1,name:'Ambulatory with assistance'},
                               {id:2,name:'Ambulatory with walking aid'},
                               {id:3,name:'Confined to bed / chair'},
                               {id:4,name:'Other'}
                           ]
                         },
                         {
                           "fallId": 6,
                           "fallName": "Disability Before",
                           "fallList": [
                               {id:0,name:'None'},
                               {id:1,name:'Hearing'},
                               {id:2,name:'Visual'},
                               {id:3,name:'Elimination'}
                           ]
                         },
                         {
                           "fallId": 7,
                           "fallName": "Disability After",
                           "fallList": [
                               {id:0,name:'None'},
                               {id:1,name:'Hearing'},
                               {id:2,name:'Visual'},
                               {id:3,name:'Elimination'}
                           ]
                         },
                         {
                           "fallId": 8,
                           "fallName": "Fall Risk Before",
                           "fallList": [
                               {id:0,name:'Yes'},
                               {id:1,name:'No'}
                           ]
                         },
                         {
                           "fallId": 9,
                           "fallName": "Fall Risk After",
                           "fallList": [
                               {id:0,name:'Yes'},
                               {id:1,name:'No'}
                           ]
                         },
                         {
                           "fallId": 10,
                           "fallName": "History of fall Before",
                           "fallList": [
                               {id:0,name:'Yes'},
                               {id:1,name:'No'}
                           ]
                         },
                         {
                           "fallId": 11,
                           "fallName": "History of fall After",
                           "fallList": [
                               {id:0,name:'Yes'},
                               {id:1,name:'No'}
                           ]
                         }
                       ];
            }

            $scope.getLabInspectionList();

            console.log("$scope.labInspectionList--------",$scope.labInspectionList);

            $scope.searchPatient = function(patientsearchObj){

                $scope.blankPatientDetails();
                $scope.blankFallReportDetails();
                $scope.getLabInspectionList();
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
                var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaForIPD";
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

            $scope.openDatePickerDOB = function()
                {
                    $scope.datepickerDOBOpen.opened = true;
                };

            $scope.datepickerDOBOpen = {
                opened : false
            };

            $scope.getPatientDetailsByIndex = function(index) {

                $scope.otpvalidated = false;
                $scope.blankFallReportDetails();
                $scope.getLabInspectionList();
                $scope.initAdmissionReqObj();
                $scope.abscondPatientFlag="";
                $scope.blankFallReportDetails();
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

                $scope.getPatientDiagnosis();
                $scope.getSaveAsDraftFallReporting();

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

            $scope.getPatientDiagnosis = function() {
                var data = {};
                $scope.diagnosisList=[];
                var headerObjDia = {
                    'unit_id' : $scope.unitId,
                    'organization_id' : $scope.organizationId,
                    'patient_id' : $scope.patientDetails.patientId,
                    'Content-Type' : 'application/json',
                };
                var URI = BASE_URL + ROOT_URL
                        + GETPATIENTDIAGNOSISBYPATIENTID;
                GenericServiceParamHeader
                        .serviceAction(METHOD_GET, URI, data,
                                NOTIFICATION_MSG_STATUS_FALSE,
                                data, headerObjDia)
                        .then(
                                function(response) {
                                    console.log(response);
                                    $scope.diagnosisList = response.data.listObject;
                                    console.log("$scope.diagnosisList===============",$scope.diagnosisList);
                                });
            }

//            $scope.convertDateTime = function(){
//                $scope.finalIncidentDateTime = "";
//                var choosedDate = moment($scope.datepickerDOB).format('DD-MM-YYYY');
//                var time = $scope.incidentHour+":"+$scope.incidentMinute+":"+$scope.incidentSecond;
//                $scope.finalIncidentDateTime = choosedDate+" "+time;
//                console.log("$scope.finalIncidentDateTime===========",$scope.finalIncidentDateTime);
//                $scope.showLatestDiagnosis($scope.finalIncidentDateTime);
//            }
//
//            $scope.showLatestDiagnosis = function(latestDate){
//                // Convert the latestDate string to a Date object
//                const latestDateTime = moment(latestDate, 'DD-MM-YYYY HH:mm:ss');
//
//                // Parse the date and time strings and merge them into a single Date object
//                const mergedDates = $scope.diagnosisList.map(item => {
//                    const [day, month, year] = item.created_date.split('/');
//                    const [hour, minute, second] = item.createdTime.split(':');
//                    return {
//                        ...item,
//                        mergedDateTime: new Date(year, month - 1, day, hour, minute, second)
//                    };
//                });
//
//                // Sort the array based on the merged Date objects
//                mergedDates.sort((a, b) => a.mergedDateTime - b.mergedDateTime);
//
//                console.log("mergedDates===========",mergedDates);
//
//                // Find the index of the nearest date before the latestDateTime
//                let nearestIndex = -1;
//                for (let i = 0; i < mergedDates.length; i++) {
//                    if (mergedDates[i].mergedDateTime >= latestDateTime) {
//                        break;
//                    }
//                    nearestIndex = i;
//                }
//
//                $scope.diagnosisName = (nearestIndex != -1)?mergedDates[nearestIndex].diagnosisName:"";
//                // Output the nearest index
//                console.log("Nearest index before latestDate:", nearestIndex);
//            }

            $scope.convertDateTime = function(){
                $scope.finalIncidentDateTime = "";
                var choosedDate = moment($scope.datepickerDOB).format('DD-MM-YYYY');
                var time = $scope.incidentHour + ":" + $scope.incidentMinute + ":00"; // Set seconds to 00
                $scope.finalIncidentDateTime = choosedDate + " " + time;
                console.log("$scope.finalIncidentDateTime===========", $scope.finalIncidentDateTime);
                $scope.showLatestDiagnosis($scope.finalIncidentDateTime);
            }

            $scope.showLatestDiagnosis = function(latestDate){
                // Convert the latestDate string to a Date object
                const latestDateTime = moment(latestDate, 'DD-MM-YYYY HH:mm:ss');

                // Parse the date and time strings and merge them into a single Date object
                const mergedDates = $scope.diagnosisList.map(item => {
                    const [day, month, year] = item.created_date.split('/');
                    const [hour, minute] = item.createdTime.split(':'); // Exclude seconds

                    const newItem = {};

                    for (let key in item) {
                        if (item.hasOwnProperty(key)) {
                            newItem[key] = item[key];
                        }
                    }
                    newItem.mergedDateTime = new Date(year, month - 1, day, hour, minute);
                    return newItem;
                });

                // Sort the array based on the merged Date objects
                mergedDates.sort((a, b) => a.mergedDateTime - b.mergedDateTime);

                console.log("mergedDates===========", mergedDates);

                // Find the index of the nearest date before the latestDateTime
                let nearestIndex = -1;
                for (let i = 0; i < mergedDates.length; i++) {
                    if (mergedDates[i].mergedDateTime >= latestDateTime) {
                        break;
                    }
                    nearestIndex = i;
                }

                $scope.diagnosisName = (nearestIndex != -1) ? mergedDates[nearestIndex].diagnosisName : "";
                // Output the nearest index
                console.log("Nearest index before latestDate:", nearestIndex);
            }


            $scope.pushUniqueNumber = function(num){

                if (!$scope.tableList.includes(num)) {
                    $scope.tableList.push(num);
                }
            }

            $scope.selectedCheckBox= "";
            $scope.checkBoxCounter = 0;
            $scope.tableList = [];

            $scope.prescribing = function(num){

                $scope.selectedCheckBox= "";
                $scope.checkBoxCounter=0;
                $scope.tableList = [];

                if($scope.checkBox1 =='Y'){
                    var temp = "1";

                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox2 =='Y'){
                    var temp = "2";
                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox3 =='Y'){
                    var temp = "3";
                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox4 =='Y'){
                    var temp = "4";
                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox5 =='Y'){
                    var temp = "5";
                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox6 =='Y'){
                     var temp = "6";
                     $scope.pushUniqueNumber(1);
                     $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox7 =='Y'){
                    var temp = "7";
                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox8 =='Y'){
                    var temp = "8";
                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox9 =='Y'){
                   var temp = "9";
                   $scope.pushUniqueNumber(1);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox10 =='Y'){
                   var temp = "10";
                   $scope.pushUniqueNumber(1);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox11 =='Y'){
                    var temp = "11";
                    $scope.pushUniqueNumber(1);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox12 =='Y'){
                   var temp = "12";
                    $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox13 =='Y'){
                   var temp = "13";
                   $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox14 =='Y'){
                   var temp = "14";
                   $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox15 =='Y'){
                   var temp = "15";
                   $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox16 =='Y'){
                   var temp = "16";
                   $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox17 =='Y'){
                   var temp = "17";
                   $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox18 =='Y'){
                   var temp = "18";
                   $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox19 =='Y'){
                   var temp = "19";
                   $scope.pushUniqueNumber(2);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox20 =='Y'){
                   var temp = "20";
                    $scope.pushUniqueNumber(3);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox21 =='Y'){
                   var temp = "21";
                   $scope.pushUniqueNumber(3);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox22 =='Y'){
                   var temp = "22";
                   $scope.pushUniqueNumber(3);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox23 =='Y'){
                   var temp = "23";
                   $scope.pushUniqueNumber(3);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox24 =='Y'){
                   var temp = "24";
                   $scope.pushUniqueNumber(3);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox25 =='Y'){
                    var temp = "25";
                    $scope.pushUniqueNumber(3);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox26 =='Y'){
                    var temp = "26";
                    $scope.pushUniqueNumber(3);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox27 =='Y'){
                    var temp = "27";
                    $scope.pushUniqueNumber(4);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox28 =='Y'){
                    var temp = "28";
                    $scope.pushUniqueNumber(4);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox29 =='Y'){
                    var temp = "29";
                    $scope.pushUniqueNumber(4);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox30 =='Y'){
                     var temp = "30";
                     $scope.pushUniqueNumber(4);
                     $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox31 =='Y'){
                    var temp = "31";
                    $scope.pushUniqueNumber(4);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox32 =='Y'){
                    var temp = "32";
                    $scope.pushUniqueNumber(4);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox33 =='Y'){
                   var temp = "33";
                   $scope.pushUniqueNumber(4);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox34 =='Y'){
                   var temp = "34";
                   $scope.pushUniqueNumber(4);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox35 =='Y'){
                    var temp = "35";
                    $scope.pushUniqueNumber(4);
                    $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox36 =='Y'){
                   var temp = "36";
                   $scope.pushUniqueNumber(4);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox37 =='Y'){
                   var temp = "37";
                   $scope.pushUniqueNumber(4);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox38 =='Y'){
                   var temp = "38";
                   $scope.pushUniqueNumber(4);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox39 =='Y'){
                   var temp = "39";
                   $scope.pushUniqueNumber(4);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox40 =='Y'){
                   var temp = "40";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox41 =='Y'){
                   var temp = "41";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox42 =='Y'){
                   var temp = "42";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox43 =='Y'){
                   var temp = "43";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox44 =='Y'){
                   var temp = "44";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox45 =='Y'){
                   var temp = "45";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox46 =='Y'){
                   var temp = "46";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox47 =='Y'){
                   var temp = "47";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox48 =='Y'){
                   var temp = "48";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }
                if($scope.checkBox49 =='Y'){
                   var temp = "49";
                   $scope.pushUniqueNumber(5);
                   $scope.selectedCheckBox += temp+" ";
                }

                console.log("selectedCheckBox=====",$scope.selectedCheckBox);
                console.log("$scope.tableList=====",$scope.tableList);

            }

            $scope.selectAssessment = function(fallId,info){

                if(info!=null){
                    var obj = {
                        fallReportingId: fallId,
                        fallReportingName: info.name,
                        fallReportingNameId: info.id
                    }

                    $scope.updateOrPushAssessmentObject(obj);
                }else{
                    $scope.assessmentSelectedList = $scope.assessmentSelectedList.filter(function(item) {
                        return item.fallReportingId !== fallId;
                    });
                }
                console.log("$scope.assessmentSelectedList=================",$scope.assessmentSelectedList);
            }

            $scope.updateOrPushAssessmentObject = function(obj){
                var foundIndex = -1;

                for (var i = 0; i < $scope.assessmentSelectedList.length; i++) {
                    if ($scope.assessmentSelectedList[i].fallReportingId === obj.fallReportingId) {
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

            $scope.saveFallReporting = function(saveAsDraft){

                $rootScope.startLoader();

                if($scope.assessmentSelectedList.length>0){
                    var tempAssessmentSelectedList = $scope.assessmentSelectedList;
                    $scope.assessmentSelectedList = [];
                    angular.forEach(tempAssessmentSelectedList, function(value,id) {
                        var obj = {
                            fallReportingId: value.fallReportingId,
                            fallReportingName: value.fallReportingName,
                            fallReportingNameId: value.fallReportingNameId
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

                if(($scope.assessmentSelectedList.length<1 || $scope.assessmentSelectedList==undefined || $scope.assessmentSelectedList==null) &&
                            ($scope.tableList.length<1 || $scope.tableList==undefined || $scope.tableList==null)){
                    growl.error("Please select at least one assessment", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if(saveAsDraft=='N'){

                    if($scope.datepickerDOB==null || $scope.datepickerDOB==undefined || $scope.datepickerDOB==0){
                        growl.error("Please fill incident Time", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.gcsScoreBefore==null || $scope.gcsScoreBefore==undefined || $scope.gcsScoreBefore==0){
                        growl.error("Please fill GCS score", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.gcsScoreAfter==null || $scope.gcsScoreAfter==undefined || $scope.gcsScoreAfter==0){
                        growl.error("Please fill GCS score", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.assessmentSelectedList.length<12 || $scope.assessmentSelectedList==null || $scope.assessmentSelectedList==undefined){
                        growl.error("Please select all assessment", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                    if($scope.tableList.length<5 || $scope.tableList==undefined || $scope.tableList==null){
                        growl.error("Please select all assessment", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                }

                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    admissionId:$scope.patientDetails.admissionId,
                    diagnosisName:$scope.diagnosisName,
                    incidentDateTime:moment($scope.datepickerDOB).format('DD-MM-YYYY HH:mm:ss'),
                    incidentHour:$scope.incidentHour,
                    incidentMinute:$scope.incidentMinute,
                    assessmentScoreNamesList:$scope.assessmentSelectedList,
                    selectedCheckBox:$scope.selectedCheckBox,
                    gcsScoreBefore:$scope.gcsScoreBefore,
                    gcsScoreAfter:$scope.gcsScoreAfter,
                    assessmentOtherRemarkBefore:$scope.assessmentOtherRemarkBefore,
                    assessmentOtherRemarkAfter:$scope.assessmentOtherRemarkAfter,
                    factorOtherText:$scope.factorOtherText,
                    saveAsDraft:saveAsDraft,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                console.log("saveObj============",saveObj);

                var URI = BASE_URL + ROOT_URL +"/adt/saveAsDraftPatientFallReporting";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         if(saveAsDraft=='Y'){
                            $scope.getSaveAsDraftFallReporting();
                         }
                         if(saveAsDraft=='N'){
                             $scope.blankPatientSearchObj();
                             $scope.patientList = [];
                             $scope.blankPatientDetails();
                             $scope.assessmentSelectedList = [];
                             $scope.getLabInspectionList();
                             $scope.blankFallReportDetails();
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

            $scope.blankFallReportDetails = function(){

                $scope.assessmentSelectedList = [];
                $scope.datepickerDOB = "";
                $scope.incidentHour = "";
                $scope.incidentMinute = "";
                $scope.diagnosisName = "";
                $scope.gcsScoreBefore = "";
                $scope.gcsScoreAfter = "";
                $scope.assessmentOtherRemarkBefore = "";
                $scope.assessmentOtherRemarkAfter = "";
                $scope.factorOtherText = "";
                $scope.selectedCheckBox = "";
                $scope.blankAllSelectedCheckBox();

            }

            $scope.getSaveAsDraftFallReporting = function(){

                $rootScope.startLoader();
                var URI1 = BASE_URL + ROOT_URL +"/adt/getSaveAsDraftPatientFallReporting/"+$scope.unitId+"/"+$scope.organizationId+"/"+$scope.patientDetails.admissionId;

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                    $scope.assessmentSelectedList = [];
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                        $scope.assessmentSelectedList = response.data.listObject;
//                        $scope.datepickerDOB = new Date($scope.assessmentSelectedList[0].incidentDateTime);
                        $scope.incidentHour = $scope.assessmentSelectedList[0].incidentHour;
                        $scope.incidentMinute = $scope.assessmentSelectedList[0].incidentMinute;
                        $scope.diagnosisName = $scope.assessmentSelectedList[0].diagnosisName;
                        $scope.gcsScoreBefore = $scope.assessmentSelectedList[0].gcsScoreBefore;
                        $scope.gcsScoreAfter = $scope.assessmentSelectedList[0].gcsScoreAfter;
                        $scope.assessmentOtherRemarkBefore = $scope.assessmentSelectedList[0].assessmentOtherRemarkBefore;
                        $scope.assessmentOtherRemarkAfter = $scope.assessmentSelectedList[0].assessmentOtherRemarkAfter;
                        $scope.factorOtherText = $scope.assessmentSelectedList[0].factorOtherText;

                        var array = $scope.assessmentSelectedList[0].incidentDateTime.split('/');
                        var newDate = "";
                        newDate = array[1]+'/'+array[0]+'/'+array[2];
                        $scope.datepickerDOB = new Date(newDate);

                        angular.forEach($scope.assessmentSelectedList, function(value,id) {
                            $scope.setSaveAsDraftFallReporting(value,id);
                        });

                        $scope.setSaveAsDraftFallReportingCheckBox($scope.assessmentSelectedList[0].selectedCheckBox);

                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.setSaveAsDraftFallReporting = function(value,id){

                var dnj = {
                    id:"",
                    name:""
                }

                if(value.fallReportingId==0){
                    angular.forEach($scope.labInspectionList[0].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.consciousBefore = dnj;
                            $scope.labInspectionList[0].fallList[value1.id] = $scope.consciousBefore;
                            console.log("sd",$scope.consciousBefore);

                        }
                    });
                }else if(value.fallReportingId==1){
                    angular.forEach($scope.labInspectionList[1].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.consciousAfter = dnj;
                            $scope.labInspectionList[1].fallList[value1.id] = $scope.consciousAfter;
                            console.log("sd",$scope.consciousAfter);

                        }
                    });
                }else if(value.fallReportingId==2){
                    angular.forEach($scope.labInspectionList[2].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.mentalStateBefore = dnj;
                            $scope.labInspectionList[2].fallList[value1.id] = $scope.mentalStateBefore;
                            console.log("sd",$scope.mentalStateBefore);

                        }
                    });
                }else if(value.fallReportingId==3){
                    angular.forEach($scope.labInspectionList[3].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.mentalStateAfter = dnj;
                            $scope.labInspectionList[3].fallList[value1.id] = $scope.mentalStateAfter;
                            console.log("sd",$scope.mentalStateAfter);

                        }
                    });
                }else if(value.fallReportingId==4){
                    angular.forEach($scope.labInspectionList[4].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.ambulationBefore = dnj;
                            $scope.labInspectionList[4].fallList[value1.id] = $scope.ambulationBefore;
                            console.log("sd",$scope.ambulationBefore);

                        }
                    });
                }else if(value.fallReportingId==5){
                    angular.forEach($scope.labInspectionList[5].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.ambulationAfter = dnj;
                            $scope.labInspectionList[5].fallList[value1.id] = $scope.ambulationAfter;
                            console.log("sd",$scope.ambulationAfter);

                        }
                    });
                }else if(value.fallReportingId==6){
                    angular.forEach($scope.labInspectionList[6].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.disabilityBefore = dnj;
                            $scope.labInspectionList[6].fallList[value1.id] = $scope.disabilityBefore;
                            console.log("sd",$scope.disabilityBefore);

                        }
                    });
                }else if(value.fallReportingId==7){
                    angular.forEach($scope.labInspectionList[7].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.disabilityAfter = dnj;
                            $scope.labInspectionList[7].fallList[value1.id] = $scope.disabilityAfter;
                            console.log("sd",$scope.disabilityAfter);

                        }
                    });
                }else if(value.fallReportingId==8){
                    angular.forEach($scope.labInspectionList[8].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.FallRiskBefore = dnj;
                            $scope.labInspectionList[8].fallList[value1.id] = $scope.FallRiskBefore;
                            console.log("sd",$scope.FallRiskBefore);

                        }
                    });
                }else if(value.fallReportingId==9){
                    angular.forEach($scope.labInspectionList[9].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.FallRiskAfter = dnj;
                            $scope.labInspectionList[9].fallList[value1.id] = $scope.FallRiskAfter;
                            console.log("sd",$scope.FallRiskAfter);

                        }
                    });
                }else if(value.fallReportingId==10){
                    angular.forEach($scope.labInspectionList[10].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.historyFallBefore = dnj;
                            $scope.labInspectionList[10].fallList[value1.id] = $scope.historyFallBefore;
                            console.log("sd",$scope.historyFallBefore);

                        }
                    });
                }else if(value.fallReportingId==11){
                    angular.forEach($scope.labInspectionList[11].fallList, function(value1,id1) {
                        if(value.fallReportingNameId==value1.id){
                                 dnj.id=value.fallReportingNameId;
                                 dnj.name=value.fallReportingName;
                                 $scope.historyFallAfter = dnj;
                            $scope.labInspectionList[11].fallList[value1.id] = $scope.historyFallAfter;
                            console.log("sd",$scope.historyFallAfter);

                        }
                    });
                }

            }

            $scope.setSaveAsDraftFallReportingCheckBox = function(selectedCheckBox){

                var selectedCheckBoxArray = selectedCheckBox.trim().split(" ");
                console.log("selectedCheckBoxArray=====",selectedCheckBoxArray);
                angular.forEach(selectedCheckBoxArray, function(value,id) {
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
                });
            }

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
            }

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("PatientFallReporting", {
            url: "/patientFallReporting",
            title: 'Patient Fall Reporting',
            templateUrl: 'views/Nursing/patient-fall-reporting.html',
            controller: "PatientFallReporting.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("PatientFallReporting.Controller", PatientFallReporting);
})();