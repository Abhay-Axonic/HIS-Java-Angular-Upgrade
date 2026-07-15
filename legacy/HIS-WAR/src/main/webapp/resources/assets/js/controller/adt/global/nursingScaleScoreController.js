

/**
 *
 */


angular.module('myApp')
		.controller("nursingScaleScoreController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','$stateParams','BillingGenericService','$http', '$location',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,$stateParams,BillingGenericService,$http,$location){

			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;

			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.admissionObj = {};


			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}

			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;

            console.log("$stateParams.obj1===============",$stateParams.obj)
            console.log("cookieObject===============",cookieObject)

            if(!angular.equals({},$stateParams.obj)){
                $scope.admissionObj =  $stateParams.obj;
                $scope.patientDetails.patientName = $scope.admissionObj.pFirstName != undefined ? $scope.admissionObj.pFirstName : $scope.admissionObj.patientName;
                $scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
                /*$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');*/
                $scope.patientDetails.uhidNumber= $scope.admissionObj.uhidnumber != undefined ? $scope.admissionObj.uhidnumber : $scope.admissionObj.uhidNumber;
                $scope.patientDetails.dobNew= $scope.admissionObj.dob;
                $scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName != undefined ? $scope.admissionObj.dFirstName: $scope.admissionObj.doctorName;
                $scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa;
                $scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd;
                $scope.patientDetails.wardName= $scope.admissionObj.wardName;
                $scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
                $scope.patientDetails.imagePath= $scope.admissionObj.imagePath;
                $scope.patientDetails.patientId= $scope.admissionObj.patientId;
                $scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
                $scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
                $scope.patientDetails.genderId= $scope.admissionObj.genderId;
                $scope.patientFromEr= $stateParams.obj.patientFromEr !== undefined && $stateParams.obj.patientFromEr !== null && $stateParams.obj.patientFromEr !== '' ? $stateParams.obj.patientFromEr : false;

            }

            $scope.closePop = function(){
              $("#getGCSScore").modal("hide");
            }

            $scope.closeAllScorePop = function(){
              $("#getAllScore").modal("hide");
            }

            $scope.closeSaveButtonPop = function(){
              $("#fallRiskScoreModal").modal("hide");
            }

            $scope.isSetFirstTabLevel = function(tabNum){
                return $scope.firstTabLevel === tabNum;
            };

            /*First Level*/
			$scope.firstTabLevel = 1 ;

            //--------------------------------Get Json for particular Tab------------------------------------------------------------------------------------------

			$scope.setFirstTabLevel = function(newTab){
				$scope.firstTabLevel = newTab;

				if(newTab==2){
                     // Fetch the JSON data
                   $http.get('resources/assets/js/nursingScoreJSON/apache-scale-score.json')
                       .then(function (response) {
                           // Success callback
                           $scope.apacheTwoScoreList = response.data;
                           $scope.totalApacheScore = "";
                           temperature = 0, arterial = 0, heart = 0, respiratory=0, oxygenation=0, serum=0, arterialPh=0;
                           serumSod=0, serumPot=0, serumSrea=0, hematocrit=0, wbc=0, gscSco=0, age=0, postOpe=0;
                           //$scope.getApacheAge($scope.admissionObj.dob, $scope.apacheTwoScoreList.nursingScaleValue[13].findings);
                           console.log("jsonData Apache ==========",$scope.apacheTwoScoreList);
                           angular.forEach($scope.apacheTwoScoreList.nursingScaleValue, function(value){
                               angular.forEach(value.findings, function(vue){
                                   vue.test=false;
                              });
                          });
                           $rootScope.stopLoader();

                       })
                       .catch(function (error) {
                           // Error callback
                           $rootScope.stopLoader();
                           console.error('Error fetching JSON:', error);
                       });

                }else if(newTab==3){
                   $http.get('resources/assets/js/nursingScoreJSON/GCS.json')
                       .then(function (response) {
                           $scope.gcsScoreList = response.data;
                           $scope.totalGCSScore = "";
                           bestEye = 0, bestVerbal = 0, bestMotor = 0;
                           console.log("jsonData GCS==========",$scope.gcsScoreList);
                           angular.forEach($scope.gcsScoreList.nursingScaleValue, function(value){
                                angular.forEach(value.findings, function(vue){
                                    vue.test=false;
                               });
                           });
                           $rootScope.stopLoader();
                       })
                       .catch(function (error) {
                           $rootScope.stopLoader();
                           console.error('Error fetching JSON:', error);
                       });

                }else if(newTab==4){
                   $http.get('resources/assets/js/nursingScoreJSON/sofa-scale-score.json')
                       .then(function (response) {
                           $scope.sofaScoreList = response.data;
                           $scope.totalSofaScore = "";
                           respiratorySys = 0, nervousSys = 0, cardiovascularSys = 0, liverSco = 0, coagulationSco = 0, kidneysSco = 0;
                           console.log("jsonData GCS==========",$scope.sofaScoreList);
                           angular.forEach($scope.sofaScoreList.nursingScaleValue, function(value){
                               angular.forEach(value.findings, function(vue){
                                   vue.test=false;
                              });
                          });
                           $rootScope.stopLoader();

                       })
                       .catch(function (error) {
                           $rootScope.stopLoader();
                           console.error('Error fetching JSON:', error);
                       });

                }else if(newTab==5){
                   $http.get('resources/assets/js/nursingScoreJSON/braden-scale-score.json')
                       .then(function (response) {
                           $scope.bradenScoreList = response.data;
                           $scope.totalBradenScore = "";
                           sensoryPer = 0, moisture = 0, activity = 0, mobility = 0, nutrition = 0, frictionShe = 0;
                           console.log("jsonData GCS==========",$scope.bradenScoreList);
                           angular.forEach($scope.bradenScoreList.nursingScaleValue, function(value){
                               angular.forEach(value.findings, function(vue){
                                   vue.test=false;
                              });
                           });
                           $rootScope.stopLoader();

                       })
                       .catch(function (error) {
                           $rootScope.stopLoader();
                           console.error('Error fetching JSON:', error);
                       });
                }else if(newTab==6){

                   $http.get('resources/assets/js/nursingScoreJSON/npuap-scale-score.json')
                       .then(function (response) {
                           $scope.npuapScoreList = response.data;
                           console.log("jsonData npuapScoreList==========",$scope.npuapScoreList);
                           $rootScope.stopLoader();
                       })
                       .catch(function (error) {
                           $rootScope.stopLoader();
                           console.error('Error fetching JSON:', error);
                       });

                }else if(newTab==8){

                   $scope.getDraftedBishopSCore();

                }
                else if(newTab==9){

                   $scope.getDraftedAldreteSCore();

                }
                else if(newTab==10){

                   $scope.getDraftedAPGARSCore();

                }
                else if(newTab==11){

                   $scope.getDraftedMallampattiClass();

                }
			};

            //-------------------------Json for particular Tab-------------------------------------------------------------------------------------------------------------------------------



            //-------------------------Start Get All Nursing Scale Scores-------------------------------------------------------------------------------------------------------------------------------

            $scope.getAllNursingScaleScore = function(){
                 var encounterId =  $scope.admissionObj.encounterId != undefined ? $scope.admissionObj.encounterId:0;
                 var admissionId =  $scope.admissionObj.admissionId != undefined ? $scope.admissionObj.admissionId:0;

                if ($scope.admissionObj.admissionId === undefined || $scope.admissionObj.admissionId === null) {
                    growl.error("Please Select any patient", {
                        title: 'Error!'
                    });
                    return;
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/getAllNursingScaleScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+admissionId+"/"+encounterId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.getAllNursingScaleScoreList = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.getAllNursingScaleScoreList = response.data.listObject;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.getAllNursingScaleScore();

            $scope.getAllNursingScoreById = function(info){
                console.log("info............................",info);

                var URI1 = BASE_URL + ROOT_URL +"/adt/getAllNursingScoreById/"+$scope.unitId+"/"+$scope.organisationId+"/"+info.admissionId+"/"+info.tabNumber;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.getAllNursingScoreByIdList = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.getAllNursingScoreByIdList = response.data.listObject;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });
            }

            //-------------------------End All Nursing Scale Scores-------------------------------------------------------------------------------------------------------------------------------

            var isParaGcsDeselect = false, isParaApacheDeselect = false, isParaSofaDeselect = false, isBradenParaGcsDeselect = false;
            $scope.parameterCheckboxChanged = function(info, tabId) {
                var dumFind = {};

                if (info.selected == false) {
                    // If parameter is unchecked, disable and reset selectedFindingId
                    angular.forEach(info.findings, function(value) {
                        value.parameterScaleId = info.parameterScaleId;

                        if(tabId==2){
                            info.selectedFindingId = value.selectedFindingId;
                        }

                        if (value.selectedFindingId == value.findingValue) {

                            $scope.removeDeselectedItem(info, value, tabId);

                            if(tabId==2){
                                value.selectedFindingId = undefined;
                            }
                        }

                        if(tabId == 2 && (value.selected == undefined || value.selected == false) ){
                              value.test = false;
                        }
                        if(tabId == 3 && (value.selected == undefined || value.selected == false) ){
                              value.test = false;
                        }
                        if(tabId == 4 && (value.selected == undefined || value.selected == false) ){
                              value.test = false;
                        }
                    });
                    info.selectedFindingId = undefined;
                }
            };

            $scope.removeDeselectedItem = function(info, value, tabId) {
                var numC = parseInt(value.findingPoints);

                if (tabId == 2) {

                    if (!isNaN(numC)) {
                        $scope.totalApacheScore = $scope.totalApacheScore - numC;
                    }
                    angular.forEach($scope.listOfSelectedApacheParam, function(v, idx) {
                        if (v.findingId == value.findingId && v.parameterScaleId==value.parameterScaleId) {
                            console.log("v Element=======", v);
                            $scope.listOfSelectedApacheParam.splice(idx, 1);
                            angular.forEach($scope.apacheTwoScoreList.nursingScaleValue,function(v1){
                                if(v.parameterScaleId == v1.parameterScaleId){
                                    angular.forEach(v1.findings,function(v2){
                                         if(v.findingId ==v2.findingId){
                                             v2.selectedFindingId = undefined;
                                             v2.test = false;
                                         }
                                    });
                                }
                            });
                            console.log("v Element listOfSelectedApacheParam=======", $scope.listOfSelectedApacheParam);
                            console.log("v Element totalApacheScore=======", $scope.totalApacheScore);
                        }
                    });

                }else if (tabId == 3) {

                    if (!isNaN(numC)) {
                        $scope.totalGCSScore = $scope.totalGCSScore - numC;
                    }
                    angular.forEach($scope.listOfSelectedGcsParam, function(v, idx) {
                        if (v.findingId == value.findingId && v.parameterScaleId==value.parameterScaleId) {
                            console.log("v Element=======", v);
                            $scope.listOfSelectedGcsParam.splice(idx, 1);
                            angular.forEach($scope.gcsScoreList.nursingScaleValue,function(v1){
                                if(v.parameterScaleId == v1.parameterScaleId){
                                    angular.forEach(v1.findings,function(v2){
                                         if(v.findingId ==v2.findingId){
                                             v2.selectedFindingId = undefined;
                                             v2.test = false;
                                         }
                                    });
                                }
                            });
                            console.log("v Element listOfSelectedGcsParam=======", $scope.listOfSelectedGcsParam);
                            console.log("v Element totalGCSScore=======", $scope.totalGCSScore);
                        }
                    });

                }else if (tabId == 4) {

                    if (!isNaN(numC)) {
                        $scope.totalSofaScore = $scope.totalSofaScore - numC;
                    }
                    angular.forEach($scope.listOfSelectedSofaParam, function(v, idx) {
                        if (v.findingId == value.findingId && v.parameterScaleId==value.parameterScaleId) {
                            console.log("v Element=======", v);
                            $scope.listOfSelectedSofaParam.splice(idx, 1);
                            angular.forEach($scope.sofaScoreList.nursingScaleValue,function(v1){
                                if(v.parameterScaleId == v1.parameterScaleId){
                                    angular.forEach(v1.findings,function(v2){
                                         if(v.findingId ==v2.findingId){
                                             v2.selectedFindingId = undefined;
                                             v2.test = false;
                                         }
                                    });
                                }
                            });
                            console.log("v Element listOfSelectedSofaParam=======", $scope.listOfSelectedSofaParam);
                            console.log("v Element totalSofaScore=======", $scope.totalSofaScore);
                        }
                    });

//                    angular.forEach($scope.listOfSelectedSofaParam, function(v, idx) {
//                        if (v.findingId == value.findingId) {
//                            console.log("v Element=======", v);
//                            $scope.listOfSelectedSofaParam.splice(idx, 1);
//                            console.log("v Element listOfSelectedSofaParam=======", $scope.listOfSelectedSofaParam);
//                        }
//                    });
                }else if (tabId == 5) {

                     if (!isNaN(numC)) {
                         $scope.totalBradenScore = $scope.totalBradenScore - numC;
                     }
                     angular.forEach($scope.listOfSelectedBradenParam, function(v, idx) {
                         if (v.findingId == value.findingId && v.parameterScaleId==value.parameterScaleId) {
                             console.log("v Element=======", v);
                             $scope.listOfSelectedBradenParam.splice(idx, 1);
                             angular.forEach($scope.bradenScoreList.nursingScaleValue,function(v1){
                                 if(v.parameterScaleId == v1.parameterScaleId){
                                     angular.forEach(v1.findings,function(v2){
                                          if(v.findingId ==v2.findingId){
                                              v2.selectedFindingId = undefined;
                                              v2.test = false;
                                          }
                                     });
                                 }
                             });
                             console.log("v Element listOfSelectedBradenParam=======", $scope.listOfSelectedBradenParam);
                             console.log("v Element totalBradenScore=======", $scope.totalBradenScore);
                         }
                     });

                }
            };

            //-------------------------Start Fall Risk Score-------------------------------------------------------------------------------------------------------------------------

            $scope.saveFallRisk =  function(){
                $rootScope.stopLoader();
                console.log("Clicked Fall Risk Tab");
            }

            //-------------------------End Fall Risk Score-------------------------------------------------------------------------------------------------------------------------

            //-------------------------Start Apache Score-------------------------------------------------------------------------------------------------------------------------


        /*    $scope.getApacheAge = function(dob, apacheFindings) {
                console.log("date of birth.....................", dob);
                console.log("apacheFindings.....................", apacheFindings);

                // Split the date string into month, day, and year components
                var dobArray = dob.split('/');
                var dobMonth = parseInt(dobArray[0], 10);
                var dobDay = parseInt(dobArray[1], 10);
                var dobYear = parseInt(dobArray[2], 10);

                // Get today's date
                var today = new Date();

                // Calculate the age
                var age = today.getFullYear() - dobYear;
                var m = today.getMonth() - dobMonth;
                if (m < 0 || (m === 0 && today.getDate() < dobDay)) {
                    age--;
                }

                console.log("Age:==================", age);
                $scope.apacheTwoScoreList.nursingScaleValue[13].selected = true;

                if(age>=75){
                    setParamFindApache(0,apacheFindings);
                }else if(age>=65 && age<=74){
                    setParamFindApache(1,apacheFindings);
                }else if(age>=55 && age<=64){
                    setParamFindApache(2,apacheFindings);
                }else if(age>=45 && age<=54){
                    setParamFindApache(3,apacheFindings);
                }else{
                    setParamFindApache(4,apacheFindings);
                }
            };*/

            var setParamFindApache = function(idx,apacheFindings){
                apacheFindings[idx].selectedFindingId=apacheFindings[idx].findingValue;
                apacheFindings[idx].test=true;
                $scope.apacheTwoScoreList.nursingScaleValue[13].selectedFindingId = apacheFindings[idx].selectedFindingId;
                $scope.totalApacheScore += $scope.totalApacheScore+parseInt(apacheFindings[idx].findingPoints)
            };

            $scope.totalApacheScore = 0, $scope.totalApacheScoreFromDraft=0;
            $scope.listOfSelectedApacheParam = [];
            var temperature = 0, arterial = 0, heart = 0, respiratory=0, oxygenation=0, serum=0, arterialPh=0;
            var serumSod=0, serumPot=0, serumSrea=0, hematocrit=0, wbc=0, gscSco=0, age=0, postOpe=0;
            var temperatureObj = {}, arterialObj = {}, heartObj = {}, respiratoryObj = {}, oxygenationObj = {}, serumObj = {}, arterialPhObj = {};
            var serumSodObj = {}, serumPotObj = {}, serumSreaObj = {}, hematocritObj = {}, wbcObj = {}, gscScoObj = {}, ageObj = {}, hsoii={}, postOpeObj = {};

			$scope.calculateApacheTotalScore = function(finding, index, info){

                if(info.parameterScaleId == 13 && $scope.isGetDraftApacheScore == false){
                    angular.forEach($scope.apacheTwoScoreList.nursingScaleValue[12].findings , function(value){
                        if(finding.selectedFindingId!=value.selectedFindingId){
                            value.selectedFindingId = undefined;
                        }
                    });
                }

                if (isParaApacheDeselect === true) {
                    $scope.totalApacheScoreFromDraft = $scope.totalApacheScore;
                }

                $scope.totalApacheScore = 0;

                var scoreNum = parseInt(finding.findingPoints)

                if(info.parameterScaleId == 1){
                        if(isNaN(scoreNum)){
                            temperature = 0;
                        }else{
                            temperature = scoreNum;
                        }
                        $scope.addParamAndVal(temperatureObj,finding,info);
                }else if(info.parameterScaleId == 2){
                        if(isNaN(scoreNum)){
                            arterial = 0;
                        }else{
                            arterial = scoreNum;
                        }
                        $scope.addParamAndVal(arterialObj,finding,info);
                }else if(info.parameterScaleId == 3){
                        if(isNaN(scoreNum)){
                            heart = 0;
                        }else{
                            heart = scoreNum;
                        }
                        $scope.addParamAndVal(heartObj,finding,info);
                }else if(info.parameterScaleId == 4){
                        if(isNaN(scoreNum)){
                            respiratory = 0;
                        }else{
                            respiratory = scoreNum;
                        }
                        $scope.addParamAndVal(respiratoryObj,finding,info);
                }else if(info.parameterScaleId == 5){
                        if(isNaN(scoreNum)){
                            oxygenation = 0;
                        }else{
                            oxygenation = scoreNum;
                        }
                        $scope.addParamAndVal(oxygenationObj,finding,info);
                }else if(info.parameterScaleId == 6){
                        if(isNaN(scoreNum)){
                            serum = 0;
                        }else{
                            serum = scoreNum;
                        }
                        $scope.addParamAndVal(serumObj,finding,info);
                }else if(info.parameterScaleId == 7){
                        if(isNaN(scoreNum)){
                            arterialPh = 0;
                        }else{
                            arterialPh = scoreNum;
                        }
                        $scope.addParamAndVal(arterialPhObj,finding,info);
                }else if(info.parameterScaleId == 8){
                        if(isNaN(scoreNum)){
                            serumSod = 0;
                        }else{
                            serumSod = scoreNum;
                        }
                        $scope.addParamAndVal(serumSodObj,finding,info);
                }else if(info.parameterScaleId == 9){
                        if(isNaN(scoreNum)){
                            serumPot = 0;
                        }else{
                            serumPot = scoreNum;
                        }
                        $scope.addParamAndVal(serumPotObj,finding,info);
                }else if(info.parameterScaleId == 10){
                        if(isNaN(scoreNum)){
                            serumSrea = 0;
                        }else{
                            serumSrea = scoreNum;
                        }
                        $scope.addParamAndVal(serumSreaObj,finding,info);
                }else if(info.parameterScaleId == 11){
                        if(isNaN(scoreNum)){
                            hematocrit = 0;
                        }else{
                            hematocrit = scoreNum;
                        }
                        $scope.addParamAndVal(hematocritObj,finding,info);
                }else if(info.parameterScaleId == 12){
                        if(isNaN(scoreNum)){
                            wbc = 0;
                        }else{
                            wbc = scoreNum;
                        }
                        $scope.addParamAndVal(wbcObj,finding,info);
                }else if(info.parameterScaleId == 13){
                        if(isNaN(scoreNum)){
                            gscSco = 0;
                        }else{
                            gscSco = scoreNum;
                        }
                        $scope.addParamAndVal(gscScoObj,finding,info);
                }else if(info.parameterScaleId == 14){
                        if(isNaN(scoreNum)){
                            age = 0;
                        }else{
                            age = scoreNum;
                        }
                        $scope.addParamAndVal(ageObj,finding,info);
                }else if(info.parameterScaleId == 15){
                        $scope.addParamAndVal(hsoii,finding,info);
                }else if(info.parameterScaleId == 16){
                        if(isNaN(scoreNum)){
                            postOpe = 0;
                        }else{
                            postOpe = scoreNum;
                        }
                        $scope.addParamAndVal(postOpeObj,finding,info);
                }

                console.log("$scope.listOfSelectedApacheParam======",$scope.listOfSelectedApacheParam);
//                $scope.totalApacheScore = $scope.totalApacheScoreFromDraft + temperature + arterial + heart + respiratory + oxygenation + serum + arterialPh +
//                                          serumSod + serumPot + serumSrea + hematocrit + wbc + gscSco + age + postOpe;

                $scope.totalApacheScore = temperature + arterial + heart + respiratory + oxygenation + serum + arterialPh +
                                                                                    serumSod + serumPot + serumSrea + hematocrit + wbc + gscSco + age + postOpe;

                isParaApacheDeselect = false, $scope.totalApacheScoreFromDraft=0;
                console.log("$scope.totalApacheScore======",$scope.totalApacheScore);
                $scope.isGcsScoreSelectUsingPopUp = false;
                $scope.isGetDraftApacheScore = false;

			}

			$scope.addParamAndVal = function(obj,finding,info){
			    obj.parameterName = info.parameterName;
                obj.parameterScaleId = info.parameterScaleId;
                obj.findingValue = finding.findingValue;
                obj.findingId = finding.findingId;
                obj.findingPoints = finding.findingPoints;
                updateOrPushApacheObject(obj);
			}

			function updateOrPushApacheObject(newObject) {
                var foundIndex = -1;

                // Iterate through the array to find the index of the existing object
                for (var i = 0; i < $scope.listOfSelectedApacheParam.length; i++) {
                    if ($scope.listOfSelectedApacheParam[i].parameterScaleId === newObject.parameterScaleId) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one
                    $scope.listOfSelectedApacheParam[foundIndex] = newObject;
                } else {
                    // Push the new object to the array
                    $scope.listOfSelectedApacheParam.push(newObject);
                }
            }


            $scope.saveTotalApacheScaleScore = function(){

                if ($scope.listOfSelectedApacheParam === undefined || $scope.listOfSelectedApacheParam === null || $scope.listOfSelectedApacheParam.length<16) {
                    growl.error("Please Check all scores", {
                        title: 'Error!'
                    });
                    return;
                }

                $rootScope.startLoader();
                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'N',
                    nursingScoreName: "Apache-II",
                    tabNumber: 2,
                    totalGcsScore:$scope.totalApacheScore,
                    nursingParamScaleScores: $scope.listOfSelectedApacheParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveApacheScaleScore";

                GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(2);
                            $scope.getAllNursingScaleScore();
                            $scope.listOfSelectedApacheParam = [];
                            $scope.closeSaveButtonPop();
//                            $scope.getAllAsDraftApacheScaleScore();
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.getTotalApacheScaleScore = function(){

                var URI1 = BASE_URL + ROOT_URL +"/adt/getApacheScaleScoreById/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.apacheTotalScoreList = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.apacheTotalScoreList = response.data.listObject;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }


            $scope.saveAsDraftApacheScore = function(){

                if ($scope.listOfSelectedApacheParam === undefined || $scope.listOfSelectedApacheParam === null || $scope.listOfSelectedApacheParam.length<1) {
                    growl.error("Please Check at least one score", {
                        title: 'Error!'
                    });
                    return;
                }

                $rootScope.startLoader();
                var saveAsDraftObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'Y',
                    totalGcsScore:$scope.totalApacheScore,
                    nursingParamScaleScores: $scope.listOfSelectedApacheParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveAsDraftApacheScaleScore";

                GenericService.serviceAction("POST", URI1, saveAsDraftObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(2);
                            $scope.getAllAsDraftApacheScaleScore();

                    }else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.isGetDraftApacheScore = false;
            $scope.getAllAsDraftApacheScaleScore = function(){

                $scope.totalApacheScore = 0;
                temperature = 0, arterial = 0, heart = 0, respiratory=0, oxygenation=0, serum=0, arterialPh=0;
                serumSod=0, serumPot=0, serumSrea=0, hematocrit=0, wbc=0, gscSco=0, age=0, postOpe=0;

                var URI1 = BASE_URL + ROOT_URL +"/adt/getAllAsDraftApacheScaleScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.listOfSelectedApacheParam = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.listOfSelectedApacheParam = response.data.listObject;
                        isParaApacheDeselect = true;

                        angular.forEach($scope.listOfSelectedApacheParam,function(data){
                           angular.forEach($scope.apacheTwoScoreList.nursingScaleValue,function(val){
                               if(data.parameterScaleId==val.parameterScaleId){
                                   console.log("valparameterScaleId==========",val);
                                    val.selected = true;
                                   angular.forEach(val.findings,function(finds){
                                       if(data.findingId==finds.findingId){
                                            console.log("finds==========",finds);
                                            finds.selectedFindingId=finds.findingValue;
                                       }
                                   })
                                   $scope.isGetDraftApacheScore = true;
                                   $scope.calculateApacheTotalScore(data,undefined,val);
                               }
                           })
                       })

//                        $scope.totalApacheScore = $scope.listOfSelectedApacheParam[0].totalGcsScore;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.findingValue=false;
            $scope.isGcsScoreSelectUsingPopUp = false;
            $scope.compareGcsScore = function(gcsData){

                $scope.isGcsScoreSelectUsingPopUp = true;
                angular.forEach( $scope.apacheTwoScoreList.nursingScaleValue[12].findings , function(value){
                     value.selectedFindingId=undefined;
                });

                angular.forEach( $scope.apacheTwoScoreList.nursingScaleValue[12].findings , function(value){
                     if(value.findingValue == gcsData.totalGcsScore.toString()){
                          value.test=true;
                          value.findingValue=value.findingValue;
                          value.selectedFindingId=undefined;
                          value.selectedFindingId=value.findingValue;
                          $scope.calculateApacheTotalScore(value,undefined,$scope.apacheTwoScoreList.nursingScaleValue[12]);

                     }else{
                          value.selectedFindingId=undefined;
                     }
                });
                $scope.closePop();
            }

            //-------------------------End Apache Score-------------------------------------------------------------------------------------------------------------------------


            //-------------------------Start GCS Score-------------------------------------------------------------------------------------------------------------------------

            $scope.totalGCSScore = 0, $scope.totalGCSScoreFromDraft = 0;
            $scope.listOfSelectedGcsParam = [];
            var bestEye = 0, bestVerbal = 0, bestMotor = 0;
            var bestEyeObj = {}, bestVerbalObj = {}, bestMotorObj = {};
            $scope.gcsCountFlagParameterFirst = 0 ;
            $scope.gcsCountFlagParameterSecond = 0 ;
            $scope.gcsCountFlag = false ;
            $scope.msg = " Glasgow Coma Score cannot be calculated (one or more components not testable) " ;
			$scope.calculateTotalGcsScore = function(finding, index, info){

			    if (isParaGcsDeselect === true) {
                    $scope.totalGCSScoreFromDraft = $scope.totalGCSScore;
                }

                $scope.totalGCSScore = 0;

                var scoreNum = parseInt(finding.findingPoints)

                if(info.parameterScaleId == 1){
                    if(isNaN(scoreNum)){
                        bestEye = 0;
                        $scope.gcsCountFlagParameterFirst = 1;
                    }else{
                        bestEye = scoreNum;
                        $scope.gcsCountFlagParameterFirst = 0;
                    }
                    bestEyeObj.parameterName = info.parameterName;
                    bestEyeObj.parameterScaleId = info.parameterScaleId;
                    bestEyeObj.findingValue = finding.findingValue;
                    bestEyeObj.findingId = finding.findingId;
                    bestEyeObj.findingPoints = finding.findingPoints;
                    updateOrPushGcsObject(bestEyeObj);
                }else if(info.parameterScaleId == 2){
                    if(isNaN(scoreNum)){
                        bestVerbal = 0;
                        $scope.gcsCountFlagParameterSecond = 1;
                    }else{
                        bestVerbal = scoreNum;
                        $scope.gcsCountFlagParameterSecond = 0;
                    }
                    bestVerbalObj.parameterName = info.parameterName;
                    bestVerbalObj.parameterScaleId = info.parameterScaleId;
                    bestVerbalObj.findingValue = finding.findingValue;
                    bestVerbalObj.findingId = finding.findingId;
                    bestVerbalObj.findingPoints = finding.findingPoints;
                    updateOrPushGcsObject(bestVerbalObj);
                }else if(info.parameterScaleId == 3){
                    if(isNaN(scoreNum)){
                        bestMotor = 0;
                    }else{
                        bestMotor = scoreNum;
                    }
                    bestMotorObj.parameterName = info.parameterName;
                    bestMotorObj.parameterScaleId = info.parameterScaleId;
                    bestMotorObj.findingValue = finding.findingValue;
                    bestMotorObj.findingId = finding.findingId;
                    bestMotorObj.findingPoints = finding.findingPoints;
                    updateOrPushGcsObject(bestMotorObj);
                }

                if(  $scope.gcsCountFlagParameterFirst == 1 || $scope.gcsCountFlagParameterSecond == 1){
                	 $scope.gcsCountFlag = true ;
                }else{
                	 $scope.gcsCountFlag = false ;
                }
                
                console.log("$scope.listOfSelectedGcsParam=======",$scope.listOfSelectedGcsParam);
//                $scope.totalGCSScore = $scope.totalGCSScoreFromDraft + bestEye + bestVerbal + bestMotor;
                $scope.totalGCSScore =  bestEye + bestVerbal + bestMotor;
                isParaGcsDeselect = false, $scope.totalGCSScoreFromDraft=0;
                console.log("$scope.totalGCSScore=======",$scope.totalGCSScore);

			}


			function updateOrPushGcsObject(newObject) {
                var foundIndex = -1;

                newObject.findingPoints = (isNaN(parseInt(newObject.findingPoints)))?0:parseInt(newObject.findingPoints);

                // Iterate through the array to find the index of the existing object
                for (var i = 0; i < $scope.listOfSelectedGcsParam.length; i++) {
                    if ($scope.listOfSelectedGcsParam[i].parameterScaleId === newObject.parameterScaleId) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one
                    $scope.listOfSelectedGcsParam[foundIndex] = newObject;
                } else {
                    // Push the new object to the array
                    $scope.listOfSelectedGcsParam.push(newObject);
                }
            }


            //-------------------------Save GCS Score------------

			$scope.isSaving = false;
            $scope.saveTotalGcsScaleScore = function(){
            	if ($scope.isSaving) return;
                if ($scope.listOfSelectedGcsParam === undefined || $scope.listOfSelectedGcsParam === null || $scope.listOfSelectedGcsParam.length<3) {
                    growl.error("Please Check all scores", {
                        title: 'Error!'
                    });
                    return;
                }

                $scope.isSaving = true;
                $rootScope.startLoader();
                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'N',
                    nursingScoreName: "GCS",
                    tabNumber: 3,
                    totalGcsScore:$scope.totalGCSScore,
                    nursingParamScaleScores: $scope.listOfSelectedGcsParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveGcsScaleScore";

                GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                	$scope.isSaving = false;
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(3);
                            $scope.getAllNursingScaleScore();
                            $scope.listOfSelectedGcsParam = [];
                            $scope.closeSaveButtonPop();
//                            $scope.getAllAsDraftGcsScaleScore();
                    }
                    else if(response.data.status =='error'){
                    	$scope.isSaving = false;
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }
            
            $scope.initFallRisk = function(){
            	
            	$scope.initFallRiskObj = {};
            	$scope.initFallRiskObj.first = "";
            	$scope.initFallRiskObj.second = "";
            	$scope.initFallRiskObj.third = "";
            	$scope.initFallRiskObj.four = "";
            	$scope.initFallRiskObj.five = "";
            	$scope.initFallRiskObj.six = "";
            	$scope.totalFallRiskScore = "";
            }
            
            $scope.initFallRisk();

            $scope.saveFallRiskBtn = false;
            $scope.saveTotalFallRiskScore = function(){
                if($scope.saveFallRiskBtn){
                    return false;
                }
                $scope.saveFallRiskBtn = true;
            	
            	var checkIn = false;
            	if($scope.initFallRiskObj.first === undefined || $scope.initFallRiskObj.first === null || $scope.initFallRiskObj.first === "" || isNaN($scope.initFallRiskObj.first)){
            		checkIn = true;
            	}
            	if($scope.initFallRiskObj.second === undefined || $scope.initFallRiskObj.second === null || $scope.initFallRiskObj.second === "" || isNaN($scope.initFallRiskObj.second)){
            		checkIn = true;
            	}
            	if($scope.initFallRiskObj.third === undefined || $scope.initFallRiskObj.third === null || $scope.initFallRiskObj.third === "" || isNaN($scope.initFallRiskObj.third)){
            		checkIn = true;
            	}
            	if($scope.initFallRiskObj.four === undefined || $scope.initFallRiskObj.four === null || $scope.initFallRiskObj.four === "" || isNaN($scope.initFallRiskObj.four)){
            		checkIn = true;
            	}
            	if($scope.initFallRiskObj.five === undefined || $scope.initFallRiskObj.five === null || $scope.initFallRiskObj.five === "" || isNaN($scope.initFallRiskObj.five)){
            		checkIn = true;
            	}
            	if($scope.initFallRiskObj.six === undefined || $scope.initFallRiskObj.six === null || $scope.initFallRiskObj.six === "" || isNaN($scope.initFallRiskObj.six)){
            		checkIn = true;
            	}
            	
            	
            	if(checkIn){
            	    $scope.saveFallRiskBtn = false;
            		 growl.error("Please Check all scores", {
                         title: 'Error!'
                     });
                     return;
            	}
            	
            	
                $rootScope.startLoader();
                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    nursingScoreName: "Fall Risk",
                    tabNumber: 1,
                    totalFallRiskScore:$scope.totalFallRiskScore,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveFallRiskScore";

                GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                        $scope.saveFallRiskBtn = false;
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(1);
                            $scope.getAllNursingScaleScore();
                            $scope.initFallRisk();
                            $scope.closeSaveButtonPop();
                    }
                    else if(response.data.status =='error'){
                        $scope.saveFallRiskBtn = false;
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }
            
            $scope.calculateFallRisk = function(){
            	
            	$scope.totalFallRiskScore = 0;
            	if(!isNaN($scope.initFallRiskObj.first)){
            		$scope.totalFallRiskScore = $scope.totalFallRiskScore + Number($scope.initFallRiskObj.first);
            	}
            	if(!isNaN($scope.initFallRiskObj.second)){
            		$scope.totalFallRiskScore = $scope.totalFallRiskScore +  Number($scope.initFallRiskObj.second);
            	}
            	if(!isNaN($scope.initFallRiskObj.third)){
            		$scope.totalFallRiskScore = $scope.totalFallRiskScore +  Number($scope.initFallRiskObj.third);
            	}
            	if(!isNaN($scope.initFallRiskObj.four)){
            		$scope.totalFallRiskScore = $scope.totalFallRiskScore +  Number($scope.initFallRiskObj.four);
            	}
            	if(!isNaN($scope.initFallRiskObj.five)){
            		$scope.totalFallRiskScore = $scope.totalFallRiskScore +  Number($scope.initFallRiskObj.five);
            	}
            	if(!isNaN($scope.initFallRiskObj.six)){
            		$scope.totalFallRiskScore = $scope.totalFallRiskScore +  Number($scope.initFallRiskObj.six);
            	}

            }

            $scope.getTotalGcsScaleScore = function(){

                var URI1 = BASE_URL + ROOT_URL +"/adt/getTotalGcsScaleScoreById/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.gcsTotalGcsScoreList = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.gcsTotalGcsScoreList = response.data.listObject;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.saveAsDraftGcsScore = function(){


                if ($scope.listOfSelectedGcsParam === undefined || $scope.listOfSelectedGcsParam === null || $scope.listOfSelectedGcsParam.length<1) {
                    growl.error("Please Check at least one score", {
                        title: 'Error!'
                    });
                    return;
                }

                $rootScope.startLoader();
                var saveAsDraftObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'Y',
                    totalGcsScore:$scope.totalGCSScore,
                    nursingParamScaleScores: $scope.listOfSelectedGcsParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }


                var URI1 = BASE_URL + ROOT_URL +"/adt/saveAsDraftGcsScaleScore";

                GenericService.serviceAction("POST", URI1, saveAsDraftObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(3);
                            $scope.getAllAsDraftGcsScaleScore();

                    }else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.getAllAsDraftGcsScaleScore = function(){

            	bestEye = 0, bestVerbal = 0, bestMotor = 0;
            	$scope.totalGCSScore = 0;

                var URI1 = BASE_URL + ROOT_URL +"/adt/getAllAsDraftGcsScaleScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.listOfSelectedGcsParam = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.listOfSelectedGcsParam = response.data.listObject;
                        isParaGcsDeselect = true;
                        $scope.gcsCountFlagParameterFirst = 0 ;
                        $scope.gcsCountFlagParameterSecond = 0 ;
                        $scope.gcsCountFlag = false ;
                         angular.forEach($scope.listOfSelectedGcsParam,function(data){
                               angular.forEach($scope.gcsScoreList.nursingScaleValue,function(val){
                                   if(data.parameterScaleId==val.parameterScaleId){
                                       console.log("valparameterScaleId==========",val);
									   val.selected = true;
                                       angular.forEach(val.findings,function(finds){
                                           if(data.findingId==finds.findingId){
                                                console.log("finds==========",finds);
                                                finds.selectedFindingId=finds.findingValue;
                                           }
                                       })
                                       $scope.calculateTotalGcsScore(data,undefined,val);
                                   }
                               })
                         })

//                        $scope.totalGCSScore = $scope.listOfSelectedGcsParam[0].totalGcsScore;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.cancelGcsScore = function(){

                $scope.listOfSelectedGcsParam = [];
                console.log("cancelGcsScore list===", $scope.listOfSelectedGcsParam);

            }

            //-------------------------End GCS Score-------------------------------------------------------------------------------------------------------------------------

            //-------------------------Start Sofa Score-------------------------------------------------------------------------------------------------------------------------

            $scope.totalSofaScore = 0, $scope.totalSofaScoreFromDraft = 0;
            $scope.listOfSelectedSofaParam = [];
            var respiratorySys = 0, nervousSys = 0, cardiovascularSys = 0, liverSco = 0, coagulationSco = 0, kidneysSco = 0;
            var respiratorySysObj = {}, nervousSysObj = {}, cardiovascularSysObj = {}, liverScoObj = {}, coagulationScoObj = {}, kidneysScoObj = {};

            $scope.calculateTotalSofaScore = function(finding, index, info){

                if (isParaSofaDeselect === true) {
                    $scope.totalSofaScoreFromDraft = $scope.totalSofaScore;
                }

                $scope.totalSofaScore = 0;
                var scoreNum = parseInt(finding.findingPoints)

                if(info.parameterScaleId == 1){
                    if(isNaN(scoreNum)){
                        respiratorySys = 0;
                    }else{
                        respiratorySys = scoreNum;
                    }
                    $scope.addSofaParamAndVal(respiratorySysObj,finding,info);
                }else if(info.parameterScaleId == 2){
                    if(isNaN(scoreNum)){
                        nervousSys = 0;
                    }else{
                        nervousSys = scoreNum;
                    }
                    $scope.addSofaParamAndVal(nervousSysObj,finding,info);
                }else if(info.parameterScaleId == 3){
                    if(isNaN(scoreNum)){
                        cardiovascularSys = 0;
                    }else{
                        cardiovascularSys = scoreNum;
                    }
                    $scope.addSofaParamAndVal(cardiovascularSysObj,finding,info);
                }else if(info.parameterScaleId == 4){
                    if(isNaN(scoreNum)){
                        liverSco = 0;
                    }else{
                        liverSco = scoreNum;
                    }
                    $scope.addSofaParamAndVal(liverScoObj,finding,info);
                }else if(info.parameterScaleId == 5){
                    if(isNaN(scoreNum)){
                        coagulationSco = 0;
                    }else{
                        coagulationSco = scoreNum;
                    }
                    $scope.addSofaParamAndVal(coagulationScoObj,finding,info);
                }else if(info.parameterScaleId == 6){
                    if(isNaN(scoreNum)){
                        kidneysSco = 0;
                    }else{
                        kidneysSco = scoreNum;
                    }
                    $scope.addSofaParamAndVal(kidneysScoObj,finding,info);
                }

                console.log("$scope.listOfSelectedSofaParam=======",$scope.listOfSelectedSofaParam);
//                $scope.totalSofaScore = $scope.totalSofaScoreFromDraft + respiratorySys + nervousSys + cardiovascularSys + liverSco + coagulationSco + kidneysSco ;
                $scope.totalSofaScore = respiratorySys + nervousSys + cardiovascularSys + liverSco + coagulationSco + kidneysSco ;
                isParaSofaDeselect = false, $scope.totalSofaScoreFromDraft=0;
                console.log("$scope.totalSofaScore=======",$scope.totalSofaScore);

            }

            $scope.addSofaParamAndVal = function(obj,finding,info){
                obj.parameterName = info.parameterName;
                obj.parameterScaleId = info.parameterScaleId;
                obj.parameterDesc = info.parameterDesc;
                obj.findingValue = finding.findingValue;
                obj.findingId = finding.findingId;
                obj.findingPoints = finding.findingPoints;
                updateOrPushSofaObject(obj);
            }

            function updateOrPushSofaObject(newObject) {
                var foundIndex = -1;

                // Iterate through the array to find the index of the existing object
                for (var i = 0; i < $scope.listOfSelectedSofaParam.length; i++) {
                    if ($scope.listOfSelectedSofaParam[i].parameterScaleId === newObject.parameterScaleId) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one
                    $scope.listOfSelectedSofaParam[foundIndex] = newObject;
                } else {
                    // Push the new object to the array
                    $scope.listOfSelectedSofaParam.push(newObject);
                }
            }


            $scope.saveTotalSofaScaleScore = function(){

                if ($scope.listOfSelectedSofaParam === undefined || $scope.listOfSelectedSofaParam === null || $scope.listOfSelectedSofaParam.length<6) {
                    growl.error("Please Check all scores", {
                        title: 'Error!'
                    });
                    return;
                }


                $rootScope.startLoader();
                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'N',
                    nursingScoreName: "SOFA",
                    tabNumber: 4,
                    totalGcsScore:$scope.totalSofaScore,
                    nursingParamScaleScores: $scope.listOfSelectedSofaParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveSofaScaleScore";

                GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(4);
                            $scope.getAllNursingScaleScore();
                            $scope.listOfSelectedSofaParam = [];
                            $scope.closeSaveButtonPop();
//                            $scope.getAllAsDraftSofaScaleScore();
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.getTotalSofaScaleScore = function(){

                var URI1 = BASE_URL + ROOT_URL +"/adt/getSofaScaleScoreById/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.sofaTotalScoreList = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.sofaTotalScoreList = response.data.listObject;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }


            $scope.saveAsDraftSofaScore = function(){

                if ($scope.listOfSelectedSofaParam === undefined || $scope.listOfSelectedSofaParam === null || $scope.listOfSelectedSofaParam.length<1) {
                    growl.error("Please Check at least one score", {
                        title: 'Error!'
                    });
                    return;
                }

                $rootScope.startLoader();
                var saveAsDraftObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'Y',
                    totalGcsScore:$scope.totalSofaScore,
                    nursingParamScaleScores: $scope.listOfSelectedSofaParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }


                var URI1 = BASE_URL + ROOT_URL +"/adt/saveAsDraftSofaScaleScore";

                GenericService.serviceAction("POST", URI1, saveAsDraftObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(4);
                            $scope.getAllAsDraftSofaScaleScore();

                    }else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.getAllAsDraftSofaScaleScore = function(){

                $scope.totalSofaScore = 0
                respiratorySys = 0, nervousSys = 0, cardiovascularSys = 0, liverSco = 0, coagulationSco = 0, kidneysSco = 0;

                var URI1 = BASE_URL + ROOT_URL +"/adt/getAllAsDraftSofaScaleScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.listOfSelectedSofaParam = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.listOfSelectedSofaParam = response.data.listObject;
                        isParaSofaDeselect = true;

                        angular.forEach($scope.listOfSelectedSofaParam,function(data){
                           angular.forEach($scope.sofaScoreList.nursingScaleValue,function(val){
                               if(data.parameterScaleId==val.parameterScaleId){
                                   console.log("valparameterScaleId==========",val);
                                   val.selected = true;
                                   angular.forEach(val.findings,function(finds){
                                       if(data.findingId==finds.findingId){
                                            console.log("finds==========",finds);
                                            finds.selectedFindingId=finds.findingValue;
                                       }
                                   })
                                   $scope.calculateTotalSofaScore(data,undefined,val);
                               }
                           })
                       })

//                        $scope.totalSofaScore = $scope.listOfSelectedSofaParam[0].totalGcsScore;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            //-------------------------End Sofa Score-------------------------------------------------------------------------------------------------------------------------

            //-------------------------Start Braden Score-------------------------------------------------------------------------------------------------------------------------

            $scope.totalBradenScore = 0, $scope.totalBradenScoreFromDraft=0;
            $scope.listOfSelectedBradenParam = [];
            var sensoryPer = 0, moisture = 0, activity = 0, mobility = 0, nutrition = 0, frictionShe = 0;
            var sensoryPerObj = {}, moistureObj = {}, activityObj = {}, mobilityObj = {}, nutritionObj = {}, frictionSheObj = {};

            $scope.calculateTotalBradenScore = function(finding, index, info){

                if (isBradenParaGcsDeselect === true) {
                    $scope.totalBradenScoreFromDraft = $scope.totalBradenScore;
                }
                $scope.totalBradenScore = 0;
                var scoreNum = parseInt(finding.findingPoints)

                if(info.parameterScaleId == 1){
                    if(isNaN(scoreNum)){
                        sensoryPer = 0;
                    }else{
                        sensoryPer = scoreNum;
                    }
                    $scope.addBradenParamAndVal(sensoryPerObj,finding,info);
                }else if(info.parameterScaleId == 2){
                    if(isNaN(scoreNum)){
                        moisture = 0;
                    }else{
                        moisture = scoreNum;
                    }
                    $scope.addBradenParamAndVal(moistureObj,finding,info);
                }else if(info.parameterScaleId == 3){
                    if(isNaN(scoreNum)){
                        activity = 0;
                    }else{
                        activity = scoreNum;
                    }
                    $scope.addBradenParamAndVal(activityObj,finding,info);
                }else if(info.parameterScaleId == 4){
                    if(isNaN(scoreNum)){
                        mobility = 0;
                    }else{
                        mobility = scoreNum;
                    }
                    $scope.addBradenParamAndVal(mobilityObj,finding,info);
                }else if(info.parameterScaleId == 5){
                    if(isNaN(scoreNum)){
                        nutrition = 0;
                    }else{
                        nutrition = scoreNum;
                    }
                    $scope.addBradenParamAndVal(nutritionObj,finding,info);
                }else if(info.parameterScaleId == 6){
                    if(isNaN(scoreNum)){
                        frictionShe = 0;
                    }else{
                        frictionShe = scoreNum;
                    }
                    $scope.addBradenParamAndVal(frictionSheObj,finding,info);
                }

                console.log("$scope.listOfSelectedBradenParam=======",$scope.listOfSelectedBradenParam);
//                $scope.totalBradenScore = $scope.totalBradenScoreFromDraft + sensoryPer + moisture + activity + mobility + nutrition + frictionShe ;
                $scope.totalBradenScore = sensoryPer + moisture + activity + mobility + nutrition + frictionShe ;
                isBradenParaGcsDeselect = false, $scope.totalBradenScoreFromDraft=0;
                console.log("$scope.totalSofaScore=======",$scope.totalBradenScore);


            }

            $scope.addBradenParamAndVal = function(obj,finding,info){
                obj.parameterName = info.parameterName;
                obj.parameterScaleId = info.parameterScaleId;
                obj.parameterDesc = info.parameterDesc;
                obj.findingValue = finding.findingValue;
                obj.findingDescription = finding.findingDescription;
                obj.findingId = finding.findingId;
                obj.findingPoints = finding.findingPoints;
                updateOrPushBradenObject(obj);
            }


            function updateOrPushBradenObject(newObject) {
                var foundIndex = -1;

                // Iterate through the array to find the index of the existing object
                for (var i = 0; i < $scope.listOfSelectedBradenParam.length; i++) {
                    if ($scope.listOfSelectedBradenParam[i].parameterScaleId === newObject.parameterScaleId) {
                        foundIndex = i;
                        break;
                    }
                }

                if (foundIndex !== -1) {
                    // Replace the existing object with the new one
                    $scope.listOfSelectedBradenParam[foundIndex] = newObject;
                } else {
                    // Push the new object to the array
                    $scope.listOfSelectedBradenParam.push(newObject);
                }
            }


            $scope.saveTotalBradenScaleScore = function(){

                if ($scope.listOfSelectedBradenParam === undefined || $scope.listOfSelectedBradenParam === null || $scope.listOfSelectedBradenParam.length<6) {
                    growl.error("Please Check all scores", {
                        title: 'Error!'
                    });
                    return;
                }

                $rootScope.startLoader();
                var saveObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'N',
                    nursingScoreName: "Braden",
                    tabNumber: 5,
                    totalGcsScore:$scope.totalBradenScore,
                    nursingParamScaleScores: $scope.listOfSelectedBradenParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveBradenScaleScore";

                GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(5);
                            $scope.getAllNursingScaleScore();
                            $scope.listOfSelectedBradenParam = [];
                            $scope.closeSaveButtonPop();
//                            $scope.getAllAsDraftBradenScaleScore();
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.getTotalBradenScaleScore = function(){

                var URI1 = BASE_URL + ROOT_URL +"/adt/getBradenScaleScoreById/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.bradenTotalScoreList = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.bradenTotalScoreList = response.data.listObject;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }


            $scope.saveAsDraftBradenScore = function(){

                if ($scope.listOfSelectedBradenParam === undefined || $scope.listOfSelectedBradenParam === null || $scope.listOfSelectedBradenParam.length<1) {
                    growl.error("Please Check at least one score", {
                        title: 'Error!'
                    });
                    return;
                }

                $rootScope.startLoader();
                var saveAsDraftObj = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organisationId,
                    encounterId:0,
                    admissionId:$scope.admissionObj.admissionId,
                    saveAsDraft:'Y',
                    totalGcsScore:$scope.totalBradenScore,
                    nursingParamScaleScores: $scope.listOfSelectedBradenParam,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveAsDraftBradenScaleScore";

                GenericService.serviceAction("POST", URI1, saveAsDraftObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(5);
                            $scope.getAllAsDraftBradenScaleScore();

                    }else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            $scope.getAllAsDraftBradenScaleScore = function(){

                $scope.totalBradenScore = 0
                sensoryPer = 0, moisture = 0, activity = 0, mobility = 0, nutrition = 0, frictionShe = 0;

                var URI1 = BASE_URL + ROOT_URL +"/adt/getAllAsDraftBradenScaleScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+$scope.admissionObj.admissionId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.listOfSelectedBradenParam = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.listOfSelectedBradenParam = response.data.listObject;
                        isBradenParaGcsDeselect = true;

                        angular.forEach($scope.listOfSelectedBradenParam,function(data){
                           angular.forEach($scope.bradenScoreList.nursingScaleValue,function(val){
                               if(data.parameterScaleId==val.parameterScaleId){
                                   console.log("valparameterScaleId==========",val);
                                   val.selected = true;
                                   angular.forEach(val.findings,function(finds){
                                       if(data.findingId==finds.findingId){
                                            console.log("finds==========",finds);
                                            finds.selectedFindingId=finds.findingValue;
                                       }
                                   })
                                   $scope.calculateTotalBradenScore(data,undefined,val);
                               }
                           })
                       })

//                        $scope.totalBradenScore = $scope.listOfSelectedBradenParam[0].totalGcsScore;
                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }


            //-------------------------End Braden Score-------------------------------------------------------------------------------------------------------------------------


            //------------------selectedDefiningChar-------Start NPUAP Score-------------------------------------------------------------------------------------------------------------------------

            $scope.selectedNpuap = "";
            $scope.selectedDefiningChar = "";
            $scope.selectedEpuap = "";
            $scope.getNpuapDataBySelect = function(npuapScaleId) {

                const foundObject = $scope.npuapScoreList.nursingScaleValue.find(obj => obj.parameterScaleId === parseInt(npuapScaleId));

                if (foundObject) {
                    $scope.selectedNpuap = foundObject.npuap;
                    $scope.selectedDefiningChar = foundObject.definingCharacteristics;
                    $scope.selectedEpuap = foundObject.epuap;
                    return "found the data";
                } else {
                    return null;
                }
            }

            $scope.cancelNpuapScore =  function(){
                $scope.selectedNpuap = "";
                $scope.selectedDefiningChar = "";
                $scope.selectedEpuap = "";
                $scope.npuapScaleId = "";
            }


            $scope.saveNpuapScaleScore = function(){

                $rootScope.startLoader();

                if ($scope.selectedDefiningChar === undefined || $scope.selectedDefiningChar === null || $scope.selectedDefiningChar.length==="" || 
                        $scope.selectedEpuap === undefined || $scope.selectedEpuap === null || $scope.selectedEpuap==="") {
                                growl.error("Please select NPUAP score", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                var saveObj = {
                    unitId: $scope.unitId,
                    organizationId: $scope.organisationId,
                    encounterId: 0,
                    admissionId: $scope.admissionObj.admissionId,
                    saveAsDraft: 'N',
                    totalGcsScore: $scope.selectedEpuap,
                    nursingScoreName: "NPUAP",
                    tabNumber: 6,
                    npuap: $scope.selectedNpuap,
                    epuap: $scope.selectedEpuap,
                    definingCharacteristics: $scope.selectedDefiningChar,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI1 = BASE_URL + ROOT_URL +"/adt/saveNpuapScaleScore";

                GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                            $scope.setFirstTabLevel(6);
                            $scope.getAllNursingScaleScore();
                            $scope.cancelNpuapScore();
                            $scope.closeSaveButtonPop();
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }

                });

            }

            //-------------------------End NPUAP Score-------------------------------------------------------------------------------------------------------------------------

            //-------------------------Start MEWS Score-------------------------------------------------------------------------------------------------------------------------



				$scope.sum = 0;
				$scope.systolic="";
				$scope.heart="";
				$scope.respiratory="";
				$scope.selectedTemperature="";
				$scope.avpu="";
				$scope.mewsCount = 0;
				$scope.commnadditionSysC = 0,$scope.commnadditionHeartC = 0,$scope.commnadditionrespiratoryC = 0,$scope.commnadditiontemperatureC = 0,$scope.commnadditionavpuC = 0;

				$scope.commnadditionSys=function (systolic,oldValue){
					if(!isNaN(oldValue) && oldValue!=""){
						 $scope.sum -= parseInt(oldValue);
						 $scope.commnadditionSysC =  0;
					}
					if(!isNaN(systolic) ){
						$scope.sum += systolic;
                    $scope.commnadditionSysC = 1;
					 console.log("sum", $scope.sum);
					}
				}
				$scope.commnadditionHeart=function (heart,oldValue){

					if(!isNaN(oldValue) && oldValue!=""){
						 $scope.sum -= parseInt(oldValue);
						 $scope.commnadditionHeartC =  0;
					}
					if(!isNaN(heart) ){
					 $scope.sum += heart;
                     $scope.commnadditionHeartC = 1;
					}
				}

	             $scope.commnadditionrespiratory=function (respiratory,oldValue){

	            	 if(!isNaN(oldValue) && oldValue!=""){
						 $scope.sum -= parseInt(oldValue);
						 $scope.commnadditionrespiratoryC = 0;
					}
	            	 if(!isNaN(respiratory) ){
	            		 $scope.sum += respiratory;
	                     $scope.commnadditionrespiratoryC = 1;
	            	 }
					
				 }

	             $scope.commnadditiontemperature=function (selectedTemperature,oldValue){

	            	 if(!isNaN(oldValue) && oldValue!=""){
							 $scope.sum -= parseInt(oldValue);
							 $scope.commnadditiontemperatureC =  0;
						}
		            	 if(!isNaN(selectedTemperature) ){
		            		 $scope.sum += selectedTemperature;
	                         $scope.commnadditiontemperatureC = 1;
		            	 }
						
				 }
	             $scope.commnadditionavpu=function (avpu,oldValue){

	            	 if(!isNaN(oldValue) && oldValue!=""){
							 $scope.sum -= parseInt(oldValue);
							 $scope.commnadditionavpuC = 0;
						}
	            	 
						 if(!isNaN(avpu) ){
							 $scope.sum += avpu;
	                         $scope.commnadditionavpuC = 1;
							}
	            	 
						 
				 }

	            

	             var data={};
				 var URI = BASE_URL + ROOT_URL
					+ "/adt/getCountOnMewsMapper/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId ;
				GenericService.serviceAction("GET", URI,data).then(function(response) {

						if (response.data.status == 'success') {
							$scope.finalscoreList=response.data.listObject;
							$rootScope.stopLoader();
							console.log("score", $scope.finalscoreList);
						}
				});
					/*}*/

                $scope.saveMewsBtn = false;
	            $scope.save = function(){
                    if($scope.saveMewsBtn){
                        return false;
                    }
                    $scope.saveMewsBtn = true;
                     $rootScope.startLoader();

                     $scope.mewsCount = $scope.commnadditionSysC+$scope.commnadditionHeartC+$scope.commnadditionrespiratoryC
                                                             +$scope.commnadditiontemperatureC+$scope.commnadditionavpuC;

                     if ($scope.mewsCount === undefined || $scope.mewsCount === null || $scope.mewsCount<5) {
                         $scope.saveMewsBtn = false;
                         growl.error("Please Select all scores", {
                             title: 'Error!'
                         });
                         $rootScope.stopLoader();
                         return;
                     }
                     if($scope.systolic==3 || $scope.heart==3 || $scope.respiratory ==3 || $scope.avpu ==3  ){
						 $scope.IsScoreWith3 = 'Y'; 
					 }
	            	 var data ={
                         totalScore:$scope.sum,
                         systolicBloodPressureId:$scope.systolic,
                         heartRateId : $scope.heart,
                         temperatureId : $scope.selectedTemperature,
                         avpuScoreId : $scope.avpu,
                         respiratoryRateId : $scope.respiratory,
                         organizationId : $scope.organizationId,
                         unitId : $scope.unitId,
                         createdBy : $scope.userId,
                         tabNumber: 7,
                         updatedBy : $scope.userId,
                         nursingScoreName: "MEWS",
                         status    : 'A',
                         createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         patientId : $scope.patientDetails.patientId,
                         admissionId : $scope.patientDetails.admissionId,
                         remark :( $scope.IsScoreWith3 != undefined && $scope.IsScoreWith3=='Y') ? "scoreWith3" : "scoreWithOut3"
        	         }


	            	  if(data != null){

                          var URI = BASE_URL + ROOT_URL+ "/adt/saveMewsMapper";
                          GenericService.serviceAction("POST", URI,data).then(function(response) {

                                if (response.data.status == 'success') {
                                	$scope.IsScoreWith3 = 'N';
                                    $scope.saveMewsBtn = false;
                                    growl.success(response.data.message,
                                            {
                                                title : 'Success!'
                                            });
                                    if($scope.patientDetails.visitTypeId == 4 ){
                                        $scope.saveMewsBtn = false;
                                        $state.reload('mapOfBedForER');
                                    }else{
                                        $scope.saveMewsBtn = false;
                                        $state.reload('bedManagement');
                                    }
                                    $scope.setFirstTabLevel(6);
                                    $scope.getAllNursingScaleScore();
                                    $scope.closeSaveButtonPop();
                                    $scope.systolic="";
                                    $scope.heart="";
                                    $scope.respiratory="";
                                    $scope.selectedTemperature="";
                                    $scope.commnadditionSysC="";
                                    $scope.commnadditionHeartC="";
                                    $scope.commnadditionrespiratoryC="";
                                    $scope.commnadditiontemperatureC="";
                                    $scope.commnadditionavpuC="";
                                    $scope.avpu="";
                                    $scope.sum = 0;
                                    $scope.mewsCount = 0;
                                    $rootScope.stopLoader();
                                    $scope.saveMewsBtn = false;
                                    return;

                                } else if (response.data.status == 'error') {
                                    $scope.saveMewsBtn = false;
                                    growl.error(response.data.message,
                                            {
                                                title : 'Error!'
                                            });
                                    $rootScope.stopLoader();
                                }
                          });

	            	  }else  {
	            	      $scope.saveMewsBtn = false;
	            		  growl.error("Please Select MEWS",
									{
										title : 'Error!'
									});
						  $rootScope.stopLoader();
	            	  }
	            }
            //-------------------------End MEWS Score-------------------------------------------------------------------------------------------------------------------------


            //-------------------------Start Bishop Score-------------------------------------------------------------------------------------------------------------------------

               $scope.BishopScoreSaveBtn = true;
               $scope.commonAdditionBishopScore = function(){

                    $scope.selectedDilation = !isNaN($scope.selectedDilation) ? $scope.selectedDilation:"";
                    $scope.selectedLength = !isNaN($scope.selectedLength) ? $scope.selectedLength:"";
                    $scope.selectedStation = !isNaN($scope.selectedStation) ? $scope.selectedStation:"";
                    $scope.selectedConsistency = !isNaN($scope.selectedConsistency) ? $scope.selectedConsistency:"";
                    $scope.selectedPosition = !isNaN($scope.selectedPosition) ? $scope.selectedPosition:"";

                    if(($scope.selectedDilation !== undefined && $scope.selectedDilation !== "") &&
                       ($scope.selectedLength !== undefined && $scope.selectedLength !== "") &&
                       ($scope.selectedStation !== undefined && $scope.selectedStation !== "") &&
                       ($scope.selectedConsistency !== undefined && $scope.selectedConsistency !== "") &&
                       ($scope.selectedPosition !== undefined && $scope.selectedPosition !== "")){
                        $scope.BishopScoreSaveBtn = false;
                    }
                    else{
                        $scope.BishopScoreSaveBtn = true;
                    }

                    var dilation = $scope.selectedDilation !== undefined && $scope.selectedDilation !== "" ? $scope.selectedDilation:0;
                    var length = $scope.selectedLength !== undefined && $scope.selectedLength !== "" ? $scope.selectedLength:0;
                    var station = $scope.selectedStation !== undefined && $scope.selectedStation !== "" ? $scope.selectedStation:0;
                    var consistency = $scope.selectedConsistency !== undefined && $scope.selectedConsistency !== "" ? $scope.selectedConsistency:0;
                    var position = $scope.selectedPosition !== undefined && $scope.selectedPosition !== "" ? $scope.selectedPosition:0;
                    $scope.sumOfBishopScore = dilation + length + station + consistency + position;
                    console.log('Selected Bishop Score : '+$scope.sumOfBishopScore);
               }






                $scope.saveAsDraftSaveMallampatti = function(){
                    console.log('selectedClass : '+ $scope.selectedClass);
                }

            $scope.getDraftedBishopSCore = function(){
                 var encounterId =  $scope.admissionObj.encounterId != undefined ? $scope.admissionObj.encounterId:0;
                 var admissionId =  $scope.admissionObj.admissionId != undefined ? $scope.admissionObj.admissionId:0;
                 if(encounterId === 0 && admissionId === 0){
                     growl.error("Please select patient", {
                          title: 'Warning'
                      });
                      $rootScope.stopLoader();
                      return false;
                 }
                 var URI = BASE_URL + ROOT_URL +"/adt/getDraftedBishopScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+admissionId+"/"+encounterId;
                 GenericService.serviceAction("GET", URI,data).then(function(response) {

                      if (response.data.status == 'success') {
                            if(response.data.listObject !== null && response.data.listObject.length>0){
                                var data = response.data.listObject[0];
                                $scope.selectedDilation = data.dilationId != undefined && data.dilationId != null ? data.dilationId :"";
                                $scope.selectedLength = data.lengthId != undefined && data.lengthId != null ? data.lengthId :"";
                                $scope.selectedStation = data.stationId != undefined && data.stationId != null ? data.stationId :"";
                                $scope.selectedConsistency = data.consistencyId != undefined && data.consistencyId !=null ? data.consistencyId :"";
                                $scope.selectedPosition = data.positionId != undefined && data.positionId != null ? data.positionId :"";
                                $scope.sumOfBishopScore = data.totalBishopScore != undefined && data.totalBishopScore != null ? data.totalBishopScore : 0;
                                $scope.commonAdditionBishopScore();
                            }
                            else{
                               $scope.BishopScoreSaveBtn = true;
                               $scope.selectedDilation="";
                               $scope.selectedLength="";
                               $scope.selectedStation="";
                               $scope.selectedConsistency="";
                               $scope.selectedPosition="";
                               $scope.sumOfBishopScore="";
                            }
                            $rootScope.stopLoader();
                            return;
                      }
                      else{
                           growl.error(response.data.message,
                           {
                               title : 'Error!'
                           });
                           $rootScope.stopLoader();
                      }
                 });
            }

            $scope.saveBishopScoreClick = _.debounce(function(flag) {
                $scope.saveBishopScore(flag);
            }, 300);

            $scope.saveBishopScoreCount = 0;
            $scope.saveBishopScore = function(flag){
                $scope.saveBishopScoreCount++;
                $scope.BishopScoreSaveBtn = true;
                if($scope.saveBishopScoreCount==1){

                    if(($scope.selectedDilation === undefined || $scope.selectedDilation === "") &&
                       ($scope.selectedLength === undefined || $scope.selectedLength === "") &&
                       ($scope.selectedStation === undefined || $scope.selectedStation === "") &&
                       ($scope.selectedConsistency === undefined || $scope.selectedConsistency === "") &&
                       ($scope.selectedPosition === undefined || $scope.selectedPosition === "")){

                             growl.error("Please select any values", {
                                 title: 'Warning'
                             });
                             $rootScope.stopLoader();
                             $scope.saveBishopScoreCount = 0;
                             return false;
                    }
                     $rootScope.startLoader();

                     if($scope.patientDetails.admissionId === undefined && $scope.patientDetails.encounterId === undefined){
                        growl.error("Please select Patient", {
                             title: 'Warning'
                         });
                         $rootScope.stopLoader();
                        $scope.saveBishopScoreCount = 0;
                         return false;
                     }

                     var data ={
                         totalBishopScore:$scope.sumOfBishopScore !== undefined && $scope.sumOfBishopScore !== "" ? $scope.sumOfBishopScore:0,
                         dilationId:$scope.selectedDilation,
                         lengthId : $scope.selectedLength,
                         stationId : $scope.selectedStation,
                         consistencyId : $scope.selectedConsistency,
                         positionId : $scope.selectedPosition,
                         organizationId : $scope.organizationId,
                         unitId : $scope.unitId,
                         createdBy : $scope.userId,
                         updatedBy : $scope.userId,
                         saveAsDraft : 'Y',
                         finalSave : 'N',
                         status    : 'A',
                         createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         admissionId : $scope.patientDetails.admissionId !== undefined ? $scope.patientDetails.admissionId:0,
                         encounterId : $scope.patientDetails.encounterId !== undefined ? $scope.patientDetails.encounterId:0
                     }

                     if(flag === 'F'){
                           data.finalSave ='Y';
                           data.saveAsDraft ='N';
                           data.nursingScoreName='BISHOP SCORE';
                           data.tabNumber = 8;
                     }

                     var URI = BASE_URL + ROOT_URL+ "/adt/saveBishopScore";
                     GenericService.serviceAction("POST", URI,data).then(function(response) {

                          if (response.data.status == 'success') {
                               growl.success(response.data.message,
                               {
                                    title : 'Success!'
                               });
                               $scope.BishopScoreSaveBtn = true;
                               $scope.selectedDilation="";
                               $scope.selectedLength="";
                               $scope.selectedStation="";
                               $scope.selectedConsistency="";
                               $scope.selectedPosition="";
                               $scope.sumOfBishopScore="";
                               $scope.getDraftedBishopSCore();
                               $scope.getAllNursingScaleScore();
                               $scope.saveBishopScoreCount = 0;
                               $rootScope.stopLoader();
                               return;

                          }
                          else{
                               growl.error(response.data.message,
                               {
                                   title : 'Error!'
                               });

                               if(flag === 'F'){
                                   $scope.BishopScoreSaveBtn = false;
                               }
                               $scope.saveBishopScoreCount = 0;
                               $rootScope.stopLoader();
                          }
                     });
                }
                else{
                    console.log('second bishop click..');
                    $scope.saveBishopScoreCount = 0;
                }
            }
            //-------------------------End Bishop Score-------------------------------------------------------------------------------------------------------------------------

            //-------------------------Start ALDRETE-------------------------------------------------------------------------------------------------------------------------

            $scope.AldreteSaveBtn = true;
            $scope.commonAdditionAldreteScore = function(){
                $scope.aldreteActivity = !isNaN($scope.aldreteActivity) ? $scope.aldreteActivity:"";
                $scope.aldreteRespiration = !isNaN($scope.aldreteRespiration) ? $scope.aldreteRespiration:"";
                $scope.aldreteCirculation = !isNaN($scope.aldreteCirculation) ? $scope.aldreteCirculation:"";
                $scope.aldreteConsciousness = !isNaN($scope.aldreteConsciousness) ? $scope.aldreteConsciousness:"";
                $scope.aldreteOxygen = !isNaN($scope.aldreteOxygen) ? $scope.aldreteOxygen:"";

                if(($scope.aldreteActivity !== undefined && $scope.aldreteActivity !== "") &&
                   ($scope.aldreteRespiration !== undefined && $scope.aldreteRespiration !== "") &&
                   ($scope.aldreteCirculation !== undefined && $scope.aldreteCirculation !== "") &&
                   ($scope.aldreteConsciousness !== undefined && $scope.aldreteConsciousness !== "") &&
                   ($scope.aldreteOxygen !== undefined && $scope.aldreteOxygen !== "")){
                    $scope.AldreteSaveBtn = false;
                }
                else{
                    $scope.AldreteSaveBtn = true;
                }

               var activity = $scope.aldreteActivity !== undefined && $scope.aldreteActivity !== "" ? $scope.aldreteActivity:0;
               var respiration = $scope.aldreteRespiration !== undefined && $scope.aldreteRespiration !== "" ? $scope.aldreteRespiration:0;
               var circulation = $scope.aldreteCirculation !== undefined && $scope.aldreteCirculation !== "" ? $scope.aldreteCirculation:0;
               var consciousness = $scope.aldreteConsciousness !== undefined && $scope.aldreteConsciousness !== "" ? $scope.aldreteConsciousness:0;
               var oxygen = $scope.aldreteOxygen !== undefined && $scope.aldreteOxygen !== "" ? $scope.aldreteOxygen:0;

               $scope.sumOfALDRETEScore = activity + respiration + circulation + consciousness + oxygen;
               console.log('Selected ALDRETE Score : '+$scope.sumOfALDRETEScore);
            }


            $scope.getDraftedAldreteSCore = function(){
                 var encounterId =  $scope.admissionObj.encounterId != undefined ? $scope.admissionObj.encounterId:0;
                 var admissionId =  $scope.admissionObj.admissionId != undefined ? $scope.admissionObj.admissionId:0;

                 if(encounterId === 0 && admissionId === 0){
                     growl.error("Please select patient", {
                          title: 'Warning'
                      });
                      $rootScope.stopLoader();
                      return false;
                 }

                 var URI = BASE_URL + ROOT_URL +"/adt/getDraftedAldreteScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+admissionId+"/"+encounterId;
                 GenericService.serviceAction("GET", URI,data).then(function(response) {

                      if (response.data.status == 'success') {
                            if(response.data.listObject !== null && response.data.listObject.length>0){
                                var data = response.data.listObject[0];
                                $scope.aldreteActivity = data.activityId != undefined && data.activityId != null ? data.activityId :"";
                                $scope.aldreteRespiration = data.respirationId != undefined && data.respirationId != null ? data.respirationId :"";
                                $scope.aldreteCirculation = data.circulationId != undefined && data.circulationId != null ? data.circulationId:"";
                                $scope.aldreteConsciousness = data.consciousnessId != undefined && data.consciousnessId !=null ? data.consciousnessId:"";
                                $scope.aldreteOxygen = data.oxygenSaturationId != undefined && data.oxygenSaturationId != null ? data.oxygenSaturationId:"";

                                $scope.sumOfALDRETEScore = data.totalAldreteScore != undefined && data.totalAldreteScore != null ? data.totalAldreteScore : 0;
                                $scope.commonAdditionAldreteScore();
                            }
                            else{
                               $scope.AldreteSaveBtn = true;
                               $scope.aldreteActivity="";
                               $scope.aldreteRespiration="";
                               $scope.aldreteCirculation="";
                               $scope.aldreteConsciousness="";
                               $scope.aldreteOxygen="";
                               $scope.sumOfALDRETEScore="";
                            }
                            $rootScope.stopLoader();
                            return;
                      }
                      else{
                           growl.error(response.data.message,
                           {
                               title : 'Error!'
                           });
                           $rootScope.stopLoader();
                      }
                 });
            }

            $scope.saveAldreteScoreClick = _.debounce(function(flag) {
                $scope.saveAldreteScore(flag);
            }, 300);

            $scope.saveAldreteScoreCount = 0;
            $scope.saveAldreteScore = function(flag){

                $scope.saveAldreteScoreCount++;
                $scope.AldreteSaveBtn = true;
                if($scope.saveAldreteScoreCount === 1 ){

                    if(($scope.aldreteActivity === undefined || $scope.aldreteActivity === "") &&
                       ($scope.aldreteRespiration === undefined || $scope.aldreteRespiration === "") &&
                       ($scope.aldreteCirculation === undefined || $scope.aldreteCirculation === "") &&
                       ($scope.aldreteConsciousness === undefined || $scope.aldreteConsciousness === "") &&
                       ($scope.aldreteOxygen === undefined || $scope.aldreteOxygen === "")){

                             growl.error("Please select any values", {
                                 title: 'Warning'
                             });
                             $rootScope.stopLoader();
                             $scope.saveAldreteScoreCount = 0;
                             return false;
                    }
                     $rootScope.startLoader();

                     if($scope.patientDetails.admissionId === undefined && $scope.patientDetails.encounterId === undefined){
                        growl.error("Please select patient", {
                             title: 'Warning'
                         });
                         $rootScope.stopLoader();
                         $scope.saveAldreteScoreCount = 0;
                         return false;
                     }

                     var data ={
                         totalAldreteScore:$scope.sumOfALDRETEScore !== undefined && $scope.sumOfALDRETEScore !== "" ? $scope.sumOfALDRETEScore:0,
                         activityId:$scope.aldreteActivity,
                         respirationId : $scope.aldreteRespiration,
                         circulationId : $scope.aldreteCirculation,
                         consciousnessId : $scope.aldreteConsciousness,
                         oxygenSaturationId : $scope.aldreteOxygen,
                         organizationId : $scope.organizationId,
                         unitId : $scope.unitId,
                         createdBy : $scope.userId,
                         updatedBy : $scope.userId,
                         saveAsDraft : 'Y',
                         finalSave : 'N',
                         status    : 'A',
                         createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         admissionId : $scope.patientDetails.admissionId !== undefined ? $scope.patientDetails.admissionId:0,
                         encounterId : $scope.patientDetails.encounterId !== undefined ? $scope.patientDetails.encounterId:0
                     }

                     if(flag === 'F'){
                           data.finalSave ='Y';
                           data.saveAsDraft ='N';
                           data.nursingScoreName='ALDRETE';
                           data.tabNumber = 9;
                     }

                     var URI = BASE_URL + ROOT_URL+ "/adt/saveAldreteScore";
                     GenericService.serviceAction("POST", URI,data).then(function(response) {

                          if (response.data.status == 'success') {
                               growl.success(response.data.message,
                               {
                                    title : 'Success!'
                               });
                               $scope.AldreteSaveBtn = true;
                               $scope.aldreteActivity="";
                               $scope.aldreteRespiration="";
                               $scope.aldreteCirculation="";
                               $scope.aldreteConsciousness="";
                               $scope.aldreteOxygen="";
                               $scope.sumOfALDRETEScore=0;
                               $scope.getDraftedAldreteSCore();
                               $scope.getAllNursingScaleScore();
                               $scope.saveAldreteScoreCount = 0;
                               $rootScope.stopLoader();
                               return;

                          }
                          else{
                               growl.error(response.data.message,
                               {
                                   title : 'Error!'
                               });
                                if(flag === 'F'){
                                   $scope.AldreteSaveBtn = false;
                                }
                                $scope.saveAldreteScoreCount = 0;
                               $rootScope.stopLoader();
                          }
                     });
                }
                else{
                    $scope.saveAldreteScoreCount = 0;
                }
            }

            //-------------------------End ALDRETE-------------------------------------------------------------------------------------------------------------------------

            //-------------------------Start APGAR-------------------------------------------------------------------------------------------------------------------------


            $scope.APGARSaveBtn = true;

            $scope.commonAgparScore = function(){

                $scope.selectedAppearance = !isNaN($scope.selectedAppearance) ? $scope.selectedAppearance:"";
                $scope.selectedPulse = !isNaN($scope.selectedPulse) ? $scope.selectedPulse:"";
                $scope.selectedGrimace = !isNaN($scope.selectedGrimace) ? $scope.selectedGrimace:"";
                $scope.selectedActivity = !isNaN($scope.selectedActivity) ? $scope.selectedActivity:"";
                $scope.selectedRespiration = !isNaN($scope.selectedRespiration) ? $scope.selectedRespiration:"";

                if(($scope.selectedAppearance !== undefined && $scope.selectedAppearance !== "") &&
                   ($scope.selectedPulse !== undefined && $scope.selectedPulse !== "") &&
                   ($scope.selectedGrimace !== undefined && $scope.selectedGrimace !== "") &&
                   ($scope.selectedActivity !== undefined && $scope.selectedActivity !== "") &&
                   ($scope.selectedRespiration !== undefined && $scope.selectedRespiration !== "")){
                    $scope.APGARSaveBtn = false;
                }
                else{
                    $scope.APGARSaveBtn = true;
                }

               var appearance = $scope.selectedAppearance !== undefined && $scope.selectedAppearance !== "" ? $scope.selectedAppearance:0;
               var pulse = $scope.selectedPulse !== undefined && $scope.selectedPulse !== "" ? $scope.selectedPulse:0;
               var grimace = $scope.selectedGrimace !== undefined && $scope.selectedGrimace !== "" ? $scope.selectedGrimace:0;
               var activity = $scope.selectedActivity !== undefined && $scope.selectedActivity !== "" ? $scope.selectedActivity:0;
               var respiration = $scope.selectedRespiration !== undefined && $scope.selectedRespiration !== "" ? $scope.selectedRespiration:0;
               $scope.sumOfAGPARScore = appearance + pulse + grimace + activity + respiration;
               console.log('Selected Apgar Score : '+$scope.sumOfAGPARScore);
            }


            $scope.getDraftedAPGARSCore = function(){
                 var encounterId =  $scope.admissionObj.encounterId != undefined ? $scope.admissionObj.encounterId:0;
                 var admissionId =  $scope.admissionObj.admissionId != undefined ? $scope.admissionObj.admissionId:0;

                 if(encounterId === 0 && admissionId === 0){
                     growl.error("Please select patient", {
                          title: 'Warning'
                      });
                      $rootScope.stopLoader();
                      return false;
                 }

                 var URI = BASE_URL + ROOT_URL +"/adt/getDraftedAPGARScore/"+$scope.unitId+"/"+$scope.organisationId+"/"+admissionId+"/"+encounterId;
                 GenericService.serviceAction("GET", URI,data).then(function(response) {

                      if (response.data.status == 'success') {
                            if(response.data.listObject !== null && response.data.listObject.length>0){
                                var data = response.data.listObject[0];
                                $scope.selectedAppearance = data.appearanceId != undefined && data.appearanceId != null ? data.appearanceId :"";
                                $scope.selectedPulse = data.pulseId != undefined && data.pulseId != null ? data.pulseId :"";
                                $scope.selectedGrimace = data.grimaceId != undefined && data.grimaceId != null ? data.grimaceId :"";
                                $scope.selectedActivity = data.activityId != undefined && data.activityId !=null ? data.activityId :"";
                                $scope.selectedRespiration = data.respirationId != undefined && data.respirationId != null ? data.respirationId :"";

                                $scope.sumOfAGPARScore = data.totalApgarScore != undefined && data.totalApgarScore != null ? data.totalApgarScore : 0;

                                $scope.commonAgparScore();
                            }
                            else{
                                $scope.APGARSaveBtn = true;
                                $scope.selectedAppearance="";
                                $scope.selectedPulse="";
                                $scope.selectedGrimace="";
                                $scope.selectedActivity="";
                                $scope.selectedRespiration="";
                                $scope.sumOfAGPARScore="";
                            }
                            $rootScope.stopLoader();
                            return;
                      }
                      else{
                           growl.error(response.data.message,
                           {
                               title : 'Error!'
                           });
                           $rootScope.stopLoader();
                      }
                 });
            }

            $scope.saveAPGARScoreClick = _.debounce(function(flag) {
                $scope.saveAPGARScore(flag);
            }, 300);

            $scope.saveAPGARScoreCount = 0;
            $scope.saveAPGARScore = function(flag){

                $scope.saveAPGARScoreCount++;
                    $scope.APGARSaveBtn = true;
                if($scope.saveAPGARScoreCount === 1){

                    if(($scope.selectedAppearance == undefined || $scope.selectedAppearance === "") &&
                       ($scope.selectedPulse === undefined || $scope.selectedPulse === "") &&
                       ($scope.selectedGrimace === undefined || $scope.selectedGrimace === "") &&
                       ($scope.selectedActivity === undefined || $scope.selectedActivity === "") &&
                       ($scope.selectedRespiration === undefined || $scope.selectedRespiration === "")){

                             growl.error("Please select any values", {
                                 title: 'Warning'
                             });

                             $rootScope.stopLoader();
                             $scope.saveAPGARScoreCount=0;
                             return false;
                    }
                     $rootScope.startLoader();

                     if($scope.patientDetails.admissionId === undefined && $scope.patientDetails.encounterId === undefined){
                        growl.error("Please select patient", {
                             title: 'Warning'
                         });
                         $rootScope.stopLoader();
                         $scope.saveAPGARScoreCount=0;
                         return false;
                     }

                     var data ={
                         totalApgarScore : $scope.sumOfAGPARScore !== undefined && $scope.sumOfAGPARScore !== "" ? $scope.sumOfAGPARScore:0,
                         appearanceId : $scope.selectedAppearance,
                         pulseId : $scope.selectedPulse,
                         grimaceId : $scope.selectedGrimace,
                         activityId : $scope.selectedActivity,
                         respirationId : $scope.selectedRespiration,
                         organizationId : $scope.organizationId,
                         unitId : $scope.unitId,
                         createdBy : $scope.userId,
                         updatedBy : $scope.userId,
                         saveAsDraft : 'Y',
                         finalSave : 'N',
                         status    : 'A',
                         createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         admissionId : $scope.patientDetails.admissionId !== undefined ? $scope.patientDetails.admissionId:0,
                         encounterId : $scope.patientDetails.encounterId !== undefined ? $scope.patientDetails.encounterId:0
                     }

                     if(flag === 'F'){
                           data.finalSave ='Y';
                           data.saveAsDraft ='N';
                           data.nursingScoreName='APGAR';
                           data.tabNumber = 10;
                     }

                     var URI = BASE_URL + ROOT_URL+ "/adt/saveAPGARScore";
                     GenericService.serviceAction("POST", URI,data).then(function(response) {

                          if (response.data.status == 'success') {
                               growl.success(response.data.message,
                               {
                                    title : 'Success!'
                               });
                               $scope.APGARSaveBtn = true;
                               $scope.selectedAppearance="";
                               $scope.selectedPulse="";
                               $scope.selectedGrimace="";
                               $scope.selectedActivity="";
                               $scope.selectedRespiration="";

                               $scope.sumOfAGPARScore=0;
                               $scope.getDraftedAPGARSCore();
                               $scope.getAllNursingScaleScore();
                               $scope.saveAPGARScoreCount=0;
                               $rootScope.stopLoader();
                               return;

                          }
                          else{
                               growl.error(response.data.message,
                               {
                                   title : 'Error!'
                               });

                               if(flag === 'F'){
                                   $scope.APGARSaveBtn = false;
                               }
                               $scope.saveAPGARScoreCount=0;
                               $rootScope.stopLoader();
                          }
                     });
                }
                else{
                    $scope.saveAPGARScoreCount=0;
                }
            }

            //-------------------------End APGAR-------------------------------------------------------------------------------------------------------------------------

            //-------------------------Start Mallampatti-------------------------------------------------------------------------------------------------------------------------


            $scope.MallampattiBtn = true;
            $scope.commonMallampattiScore = function(){

                if($scope.selectedClass !== undefined && $scope.selectedClass !== ""){
                    $scope.MallampattiBtn = false;
                }
                else{
                    $scope.MallampattiBtn = true;
                }
            }

            $scope.getDraftedMallampattiClass = function(){
                 var encounterId =  $scope.admissionObj.encounterId != undefined ? $scope.admissionObj.encounterId:0;
                 var admissionId =  $scope.admissionObj.admissionId != undefined ? $scope.admissionObj.admissionId:0;

                 if(encounterId === 0 && admissionId === 0){
                     growl.error("Please select patient", {
                          title: 'Warning'
                      });
                      $rootScope.stopLoader();
                      return false;
                 }

                 var URI = BASE_URL + ROOT_URL +"/adt/getDraftedMallampattiClass/"+$scope.unitId+"/"+$scope.organisationId+"/"+admissionId+"/"+encounterId;
                 GenericService.serviceAction("GET", URI,data).then(function(response) {

                      if (response.data.status == 'success') {
                            if(response.data.listObject !== null && response.data.listObject.length>0){
                               var data = response.data.listObject[0];
                               $scope.selectedClass = data.mallampattiClassId != undefined && data.mallampattiClassId != null ? data.mallampattiClassId :"";
                               $scope.commonMallampattiScore();
                            }
                            else{
                                 $scope.MallampattiBtn = true;
                                $scope.selectedClass="";
                            }
                            $rootScope.stopLoader();
                            return;
                      }
                      else{
                           growl.error(response.data.message,
                           {
                               title : 'Error!'
                           });
                           $rootScope.stopLoader();
                      }
                 });
            }

            $scope.saveMallampattiClassClick = _.debounce(function(flag) {
                $scope.saveMallampattiClass(flag);
            }, 300);

            $scope.saveMallampattiClassCount = 0;
            $scope.saveMallampattiClass = function(flag){

                $scope.saveMallampattiClassCount++;
                $scope.MallampattiBtn = true;
                if($scope.saveMallampattiClassCount === 1){

                    if($scope.selectedClass === undefined || $scope.selectedClass === ""){

                         growl.error("Please select any class", {
                             title: 'Warning!'
                         });

                         $rootScope.stopLoader();
                         $scope.saveMallampattiClassCount = 0;
                         return false;
                    }
                     $rootScope.startLoader();

                     if($scope.patientDetails.admissionId === undefined && $scope.patientDetails.encounterId === undefined){
                        growl.error("Please select patient", {
                             title: 'Warning'
                         });
                         $rootScope.stopLoader();
                         $scope.saveMallampattiClassCount = 0;
                         return false;
                     }

                     var data ={
                         mallampattiClassId : $scope.selectedClass,
                         totalMallampattiScore : $scope.selectedClass,
                         organizationId : $scope.organizationId,
                         unitId : $scope.unitId,
                         createdBy : $scope.userId,
                         updatedBy : $scope.userId,
                         saveAsDraft : 'Y',
                         finalSave : 'N',
                         status    : 'A',
                         createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                         admissionId : $scope.patientDetails.admissionId !== undefined ? $scope.patientDetails.admissionId:0,
                         encounterId : $scope.patientDetails.encounterId !== undefined ? $scope.patientDetails.encounterId:0
                     }

                     if(flag === 'F'){
                           data.finalSave ='Y';
                           data.saveAsDraft ='N';
                           data.nursingScoreName='MALLAMPATTI';
                           data.tabNumber = 11;
                     }

                     var URI = BASE_URL + ROOT_URL+ "/adt/saveMallampattiClass";
                     GenericService.serviceAction("POST", URI,data).then(function(response) {

                          if (response.data.status == 'success') {
                               growl.success(response.data.message,
                               {
                                    title : 'Success!'
                               });
                               $scope.MallampattiBtn = true;
                               $scope.selectedClass="";
                               $scope.getDraftedMallampattiClass();
                               $scope.getAllNursingScaleScore();
                               $scope.saveMallampattiClassCount = 0;
                               $rootScope.stopLoader();
                               return;

                          }
                          else{
                               growl.error(response.data.message,
                               {
                                   title : 'Error!'
                               });
                               $scope.MallampattiBtn = false;
                               $scope.saveMallampattiClassCount = 0;
                               $rootScope.stopLoader();
                          }
                     });
                }
                else{
                    $scope.saveMallampattiClassCount = 0;
                }
            }

             //-------------------------End Mallampatti-------------------------------------------------------------------------------------------------------------------------


             //-------------------------GET Speciality Mapped Nursing Score Tabs-------------------------------------------------------------------------------------------------------------------------

            $scope.getSpecialityMappedNursingScoreTabs = function(){
                 var doctorId =  $scope.admissionObj.doctorId != undefined ? $scope.admissionObj.doctorId:0;
                 var specialityId =  $scope.admissionObj.specialityId != undefined ? $scope.admissionObj.specialityId:0;

                var URI1 = BASE_URL + ROOT_URL +"/adt/getSpecialityMappedNursingScoreTabs/"+$scope.unitId+"/"+$scope.organisationId+"/"+doctorId+"/"+specialityId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.getSpecialityMappedNursingScoreTabsList = [];
                    if(response.data.status == "success"){
                        $rootScope.stopLoader();
                        $scope.getSpecialityMappedNursingScoreTabsList = response.data.listObject;

                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.getSpecialityMappedNursingScoreTabs();

            $scope.setTabForSpecialityNursingScore = function(){
                if($scope.getSpecialityMappedNursingScoreTabsList !== undefined && $scope.getSpecialityMappedNursingScoreTabsList !== null && $scope.getSpecialityMappedNursingScoreTabsList.length>0){
                    var tabNum = $scope.getSpecialityMappedNursingScoreTabsList[0].nursingScaleScoreId;
                    tabNum = tabNum !== undefined || tabNum !== null ? tabNum : 1;
                    if(tabNum == 2){
                        $scope.setFirstTabLevel(3);
                    }
                    else if(tabNum == 3){
                        $scope.setFirstTabLevel(3);
                    }
                    else{
                        $scope.setFirstTabLevel(tabNum);
                    }
                     $("#fallRiskScoreModal").modal("show");
                }
                else{
                    growl.warning("Nursing score not mapped with this speciality.!",{
                        title : "Warning"
                    });
                }
            }
             //-------------------------END GET Speciality Mapped Nursing Score Tabs-------------------------------------------------------------------------------------------------------------------------


        }]);



