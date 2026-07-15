angular
		.module('myApp')
		.controller(
				'dietPatientRequestTransactionController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'PagerService',
						'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams) {
							
							$scope.dayEnum = {
									'monday':0,
									'tuesday':1,
									'wednesday':2,
									'thursday':3,
									'friday':4,
									'saturday':5,
									'sunday':6
							};
							$scope.dayPickerDynamicValues = [
								'Monday',
								'Tuesday',
								'Wednesday',
								'Thursday',
								'Friday',
								'Saturday',
								'Sunday'
								];
							
							$scope.datePickerForDietPlan1 = {
									opened : false
								};

							$scope.datePickerForDietPlan=new Date();

                            $scope.datePickerDynamicValues = [];



							$scope.datepicker = {
									opened : false
								};
							

							// for datePicker
							/*$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};*/
							$scope.today = new Date();
						//	$scope.tomorrow = new Date();
						 //   $scope.tomorrow.setDate($scope.tomorrow.getDate() + 1);
							$scope.dateOptionsForDietPlan = {
									minDate : $scope.today
									};
																				
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							$scope.openDatePickerForDietPlan = function() {
								$scope.datePickerForDietPlan1.opened = true;
							
							};
							
							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							}
							
							$scope.dietOrderObj = {
									mealPreferenceId : '',
									mealPreferenceDesc : '',
									selectedDietTypeId : '',
									dietTypeDescription : ''

							};
							$scope.datePickerChange = function() {
								var currentDay = $scope.datePickerForDietPlan.toDateString().split(' ');
								var currentDate = $scope.datePickerForDietPlan.toISOString().split('T');
								currentDay = currentDay[0];
								currentDate = currentDate[0];
								
								// day change logic
								var currentIndexInOldArray = 0;
								for (var i = 0 ; i < $scope.dayPickerDynamicValues.length; i++) {
									var day = $scope.dayPickerDynamicValues[i];
									if (day.includes(currentDay)) {
								    	currentIndexInOldArray = i;
								    	break;
									}
								}
								
								var earlierDays = $scope.dayPickerDynamicValues.splice(0, currentIndexInOldArray);
								for (var i = 0; i < earlierDays.length; i++) {
									var dayToAppend = earlierDays[i];
									var currentLength = $scope.dayPickerDynamicValues.length;
									$scope.dayPickerDynamicValues.splice(currentLength, 0, dayToAppend);
								}
								
								// date change logic
								$scope.datePickerDynamicValues = [];
								var firstElement = new Date(currentDate);
								for( var i = 0; i < 7 ; i++) {
									var nextDay = new Date(firstElement.getTime() + i * 24 * 60 * 60 * 1000);
									$scope.datePickerDynamicValues.push(nextDay.toISOString().slice(0,10));
								}
							}

                          /*  if($scope.datePickerForDietPlan != null && $scope.datePickerForDietPlan != undefined && $scope.datePickerForDietPlan != ""){
                                $scope.datePickerChange();
                            }*/

							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
							 
							$scope.roleName = cookieObjectAppConfig.roleName;
							$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
							$scope.doctorRole = cookieObjectAppConfig.doctorRole;
							$scope.skipAdmissionAcceptance = cookieObjectAppConfig.skipAdmissionAcceptance;
							$scope.doctorRole = cookieObjectAppConfig.doctorRole;
							
							$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
							
							$scope.nursingWardList = [];
							var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
							 if(nursingStationsWardsList == undefined){
								 growl.warning(
											'No ward mapped with selected nursing station !!',
											{
												title : 'Success!'
											});
							 }
							 else
							  {
							   console.log('nursingStationsWardsList',nursingStationsWardsList);
							   $scope.nursingWardList =nursingStationsWardsList; 
							  
							   console.log('nursingWardList',$scope.nursingWardList);
							   
							  }
							
							    $scope.nurseBedList = [];
								var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
								 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y'){
									 growl.warning(
												'No bed mapped with nurse !!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   $scope.nurseBedList =mappedNurseBedList; 
								  console.log('Nurse Bed List',$scope.nurseBedList);
								  }
							
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
							
								
								
								$scope.mealMasterPreferenceObj = {
										noOfRecordsPerPage: 10,
										organizationId: $scope.organizationId,
										offset: 0,
										unitId : cookieObject.unitId
								};

								$scope.selectedDietPreference = []
								
                                $scope.dummyDayWiseDietObj = {
                                        mealDetailsList: [
                                            { mealsList : [
                                                {dishList : [{}]},
                                                {dishList : [{}]},
                                                {dishList : [{}]},
                                                {dishList : [{}]},
                                                {dishList : [{}]},
                                                {dishList : [{}]},
                                                {dishList : [{}]}
                                            ]}
                                            
                                        ]
                                    };
								
								$scope.dayWiseNutritionalList = [{nutritionList:[]},{nutritionList:[]},
									{nutritionList:[]},{nutritionList:[]},{nutritionList:[]},{nutritionList:[]},{nutritionList:[]}];
								
								
								$scope.dayWiseDietObj = $scope.dummyDayWiseDietObj;
								
								
								$scope.dummyDietPatientRequestObj = {
										dietPreferenceId: "",
										dietTypeId: "",
										
									};

								
								
								
								
								$scope.dietPatientRequestObj = $scope.dummyDietPatientRequestObj;
								$scope.dishPropertyObjList = [ {
								
									"dishId" : '',
									"quantity" : '',
								
								} ];

								var data = {};
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDIETTYPEMASTERLIST*/

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEDIETTYPEMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;

								// For Get Diet Master List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.dietList = [];

													if (response.data.status == "success") {
														$scope.dietList = response.data.listObject;
														console
																.log(
																		"dietMasterList",
																		$scope.dietList);

													}
												});
								
								
								// Get meal Master Preference List
								/*var URI = BASE_URL + ROOT_URL
								+ GETMEALPREFERENCELIST;*/
								
								var URI = BASE_URL + ROOT_URL
								+ GETMEALPREFERENCELIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								GenericService
								.serviceAction("POST", URI, $scope.mealMasterPreferenceObj)
								.then(
										function(response) {
								$scope.mealMasterPreferenceList = [];

									if (response.data.status == "success") {
										$scope.mealMasterPreferenceList = response.data.listObject;
										console
											.log(
													"mealMasterPreferenceList",
													$scope.mealMasterPreferenceList);

								}
									
							});
								
						
								
						var URI = BASE_URL + ROOT_URL
						+ GETMEALMASTERDROPDOWN;

				// For Get Meal List Using Offset
/*				GenericService
						.serviceAction("GET", URI, data)
						.then(
								function(response) {
									$scope.mealMasterList = [];

									if (response.data.status == "success") {
										$scope.mealMasterList = response.data.listObject;
										for(var i = 0; i<mealList.length; i++) {
											mealList[i].mealsList = [
													{dayId : 0, dishList : [{}]},
													{dayId : 1, dishList : [{}]},
													{dayId : 2, dishList : [{}]},
													{dayId : 3, dishList : [{}]},
													{dayId : 4, dishList : [{}]},
													{dayId : 5, dishList : [{}]},
													{dayId : 6, dishList : [{}]}
												]
										}
										 $scope.mealMasterList = mealList;
									}
								});
					*/
				// For Get Meal Master PReference from ADT List Using Offset
						/*	var URI = BASE_URL + ROOT_URL
							+ GETMEALPREFERENCELIST;
							GenericService
							.serviceAction("POST", URI, $scope.mealMasterPreferenceObj)
							.then(
									function(response) {
							$scope.mealMasterPreferenceList = [];

								if (response.data.status == "success") {
									$scope.mealMasterPreferenceList = response.data.listObject;
									console
										.log(
												"mealMasterPreferenceList",
												$scope.mealMasterPreferenceList);

							}
								$scope.editTemplate = false;
								
								if($stateParams.dietTemplateId){
									$scope.editTemplate = true;
									$scope.getDietTemplate();
							
								}
								
						});*/
						$scope.patientSearchObj = {
													uhIdNumber : "",
													patientName : "",
													genderId : "",
													mobileNo : "",
													birthDate : "",
													identificationNo : "",
													patientType : 1,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												};
						$scope.patientDetails = {patientName: "",genderCode: "",age: "",uhIdNumber: "",birthDate: ""};
						console.log('$stateParams.patientDetailObj',$stateParams.patientDetailObj);
						if (angular.equals({},
								$stateParams.patientDetailObj)) {
							//$state.go('dietOrderPatientList');
							//return;
						} else {
							
							$scope.patientDetails.uhIdNumber = $stateParams.patientDetailObj.uhidnumber;
							$scope.patientDetails.patientName = $stateParams.patientDetailObj.pFirstName;
							$scope.patientDetails.genderCode = $stateParams.patientDetailObj.genderCode;
							$scope.patientDetails.birthDate = $stateParams.patientDetailObj.dob;
							$scope.patientDetails.age = $stateParams.patientDetailObj.ageStr;
							$scope.patientDetails.patientId = $stateParams.patientDetailObj.patientId;
							$scope.patientDetails.admissionId = $stateParams.patientDetailObj.admissionId;
							
							var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.patientDetails.admissionId;
							GenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.admissionDetails = response.data.object;
										$scope.dateOfAdmission = typeof($scope.admissionDetails.doa) != "undefined" ? $scope.admissionDetails.doa : "";
										$scope.patientDischargeDate = typeof($scope.admissionDetails.pdd) != "undefined" ? $scope.admissionDetails.pdd : "";
										$scope.bedNumber = typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
										$scope.dietOrderObj.mealPreferenceId =  typeof($scope.admissionDetails.preferenceMealId) != "undefined" ? $scope.admissionDetails.preferenceMealId : "";
										$scope.dietOrderObj.selectedDietTypeId =  typeof($scope.admissionDetails.dietType) != "undefined" ? $scope.admissionDetails.dietType : "";
										$scope.selectedDietTypeId =  typeof($scope.admissionDetails.dietType) != "undefined" ? $scope.admissionDetails.dietType : "";
										if($scope.admissionDetails.preferenceMealId!=null && $scope.admissionDetails.preferenceMealId!=undefined && $scope.admissionDetails.preferenceMealId !=""){
                                            $scope.selectedDietPrefNew($scope.dietOrderObj.mealPreferenceId);
                                        }
                                        if($scope.admissionDetails.dietType!=null && $scope.admissionDetails.dietType!=undefined && $scope.admissionDetails.dietType !=""){
                                            $scope.selectedDietTypeFun($scope.selectedDietTypeId);
                                        }
										$scope.doctorFullName = typeof($scope.admissionDetails.doctorName) != "undefined" ? $scope.admissionDetails.doctorName : "";
									}
								});
//							$scope.getDietPrefByPatientId(cookieDietObject.patientId);
						}
						$scope.patientList = [];
				
					//	$scope.searchPatient = searchPatient;
						$scope.getPatientDetailsByIndex = getPatientDetailsByIndex;
							}
							
							
							$scope.searchPatient = function(searchObj){

							  if(searchObj.uhIdNumber == '' && searchObj.patientName == '')
                                  {
                                      growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
                                      });
                                      return false;
                                  }

								var URI = BASE_URL + ROOT_URL + PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
								$rootScope.startLoader();
								GenericService.serviceAction(METHOD_POST,URI, searchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.patientList = response.data.listObject;

//										$scope.datePickerForDietPlan = "";
                                        $scope.patientDetails={};
                                        $scope.dietOrderObj = {};
                                        $scope.selectedDietTypeId = "";
                                        $scope.emptyDietTemplateFields();
                                        $scope.datePickerForDietPlan=new Date();
									}
									$rootScope.stopLoader();
								});
							}
							
							
							var patientId;
							
							$scope.getPatientDetailsByIndex = function(index) {
								$scope.patientId = $scope.patientList[index].patientId;
								$scope.patientDetails.age = ($scope.patientList[index].age);
								$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
								$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
								$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
								$('.patienttSearchResult').toggle();
							}	
							
							
							var getPatientDetailsByIndex = function(index){
								$rootScope.startLoader();
								
								$scope.patientDetails.age=($scope.patientList[index].age);
								$scope.patientDetails.patientName=($scope.patientList[index].patientName);
								$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
								$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.patientId=($scope.patientList[index].patientId);
								$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
								
								$scope.patientFullName=($scope.patientList[index].patientName)+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
								$scope.uhidNum=($scope.patientList[index].uhIdNumber);
								$scope.admissionId = ($scope.patientList[index].admissionId);
								
								//get admission details by id
								if($scope.admissionId != null && typeof($scope.admissionId) != "undefined"){
									$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
								GenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.admissionDetails = response.data.object;
											$scope.dateOfAdmission = typeof($scope.admissionDetails.doa) != "undefined" ? $scope.admissionDetails.doa : "";
											$scope.patientDischargeDate = typeof($scope.admissionDetails.pdd) != "undefined" ? $scope.admissionDetails.pdd : "";
											$scope.bedNumber = typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
											$scope.doctorFullName = typeof($scope.admissionDetails.doctorName) != "undefined" ? $scope.admissionDetails.doctorName : "";
                                            $scope.datePickerForDietPlan=new Date();
                                            $scope.emptyDietTemplateFields();
                                            if($scope.admissionDetails.preferenceMealId!=null && $scope.admissionDetails.preferenceMealId!=undefined && $scope.admissionDetails.preferenceMealId !=""){
                                                $scope.dietOrderObj.mealPreferenceId = $scope.admissionDetails.preferenceMealId;
                                                $scope.selectedDietPrefNew($scope.dietOrderObj.mealPreferenceId);
                                            }else{
                                                $scope.dietOrderObj.mealPreferenceId = "";
                                            }
                                            if($scope.admissionDetails.dietType!=null && $scope.admissionDetails.dietType!=undefined && $scope.admissionDetails.dietType !=""){
                                                $scope.selectedDietTypeId = $scope.admissionDetails.dietType;
                                                $scope.selectedDietTypeFun($scope.selectedDietTypeId);
                                            }else{
                                                $scope.selectedDietTypeId = "";
                                            }

										}
										$rootScope.stopLoader();
									});
									
									
								}
								//Assignment to model (Santosh)
						//		self.model.patientDetails.patientId=($scope.patientList[index].patientId);//4
								//ADMISSION & ENCOUNTER IDS SAME VARIABLE;
					//			self.model.encounterDetails.encounterId = ($scope.patientList[index].admissionId); 
								/*self.model.visitTypeId = 1;*/
								$scope.datePickerForDietPlan=new Date();
								$scope.dietOrderObj.mealPreferenceId = "";
                                $scope.selectedDietTypeId = "";
                                $scope.emptyDietTemplateFields();
								$(".patienttSearchResult").toggle();
								$rootScope.stopLoader();
							};
							
							
							$scope.init();
							
							
						
							
						$scope.getDietPrefByPatientId = function(patientId) {
							
							data = {};
							/*var URI = BASE_URL + ROOT_URL 
					 		+ GETDIETPREFERENCEBYPATIENTID;*/
							
							var URI = BASE_URL + ROOT_URL 
							 		+ GETDIETPREFERENCEBYPATIENTID + '?patientId=' + patientId + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
							$scope.dietPref = {};

								if (response.data.status == "success") {
									$scope.dietPref = response.data.object;
									$scope.getDishMasterList()
									console.log("dietPrefList",$scope.dietPref);

							}
								
						});
							
						}	
						$scope.getCopyDiet=function(){

                           if($scope.patientDetails.uhIdNumber=="" ||$scope.patientDetails.patientId==""){
                                 $scope.daywiseDietFormView=true;
                                 $scope.daywiseDietFormCopy=true;

                           }else{
                                $scope.daywiseDietFormView=false;
                                $scope.daywiseDietFormCopy=false;
                                if($scope.daywiseDietForm.$valid){
                                    angular.element('#copyDietToOtherPatientModal').modal('show');
                                 }
                           }

						}
						
						$scope.getDietTemplateByDietPrefId = function() {
							data = {};
							
							/*var URI = BASE_URL + ROOT_URL 
							 + GETDIETTEMPLATEBYDIETPREFID + '?dietPrefId=' + $scope.dietPref.dietPrefId + 
							 '&dietTypeId=' + $scope.selectedDietTypeId;*/
							var id= ($scope.dietPref !=null && $scope.dietPref.dietPrefId != undefined ) ? $scope.dietPref.dietPrefId :parseInt($scope.dietOrderObj.mealPreferenceId);

							var URI = BASE_URL + ROOT_URL 
									 + GETDIETTEMPLATEBYDIETPREFID + '?dietPrefId=' + id + 
									 '&dietTypeId=' + $scope.selectedDietTypeId + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
							
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
							$scope.dietTemplate = {};
							$scope.dietTemplate['mealDetailsList'] = [];
							
								if (response.data.status == "success") {
									$scope.getNutritionValues(response.data.object.dietTemplateId);
									
									var response_to_be_updated = response.data.object;
									var currentIndexToBeUpdate = $scope.dayEnum[$scope.dayPickerDynamicValues[0].toLowerCase()];
									for (var j = 0; j < response_to_be_updated['mealDetailsList'].length; j++) {
										var current_item_dict = {};
										current_item_dict['mealId'] = response_to_be_updated['mealDetailsList'][j]['mealId'];
										current_item_dict['mealName'] = response_to_be_updated['mealDetailsList'][j]['mealName'];
										current_item_dict['servingTime'] = response_to_be_updated['mealDetailsList'][j]['servingTime'];
										var current_item = response_to_be_updated['mealDetailsList'][j]['mealsList'];
										var earlierData = current_item.splice(0, currentIndexToBeUpdate);
										for (var i = 0; i < earlierData.length; i++) {
											var dataToAppend = earlierData[i];
											var currentLength = current_item.length;
											current_item.splice(currentLength, 0, dataToAppend);
										}
										for(var k=0; k<current_item.length;k++) {
											current_item[k]['dietDate'] = $scope.datePickerDynamicValues[k]
										}
										current_item_dict['mealsList'] = current_item;
										$scope.dietTemplate['mealDetailsList'].push(current_item_dict);
									}

									  angular.forEach($scope.dietTemplate.mealDetailsList , function(v,k){
									   angular.forEach(v.mealsList , function(v1,k1){
									    angular.forEach(v1.dishList , function(v2,k2){
                                                                                      $scope.isExpiredMealTime = $scope.checkExpiredMealTime(v.servingTime,$scope.datePickerDynamicValues[k1]);
                                                                                     v1.isExpiredMealTime =     $scope.isExpiredMealTime;
                                                                           });
                                                                        });
                                                                    });

							}else{
								growl.warning(response.data.message);
							}
								
						});
							
						}
							
							
							
					/*	
							*/
							
					/*		var selectedDietPref 
							
							$scope.getDietTemplate = function() {
						
								data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETDIETTEMPLATEBYID + '?dietTemplateId=' + $stateParams.dietTemplateId;
						// For Get Diet List Using Offset
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												var dietTemplate = response.data.object
												$scope.selectedDietTypeId = dietTemplate.dietTypeId;
												$scope.mealMasterList = dietTemplate.mealDetailsList;
												for(var i=0; i< $scope.mealMasterPreferenceList.length;i++){
													for(var j=0; j< dietTemplate.dietPreferenceList.length;j++) {
														if($scope.mealMasterPreferenceList[i].mealPreferenceId == dietTemplate.dietPreferenceList[j].dietPrefId) {
															$scope.mealMasterPreferenceList[i].isChecked = true
															continue;
														}
													}
												}
												selectedDietPref = dietTemplate.dietPreferenceList;
												
											}
											
										});
							}*/
							
						$scope.selectedDietPrefNew=function(id){
							$scope.dietOrderObj.mealPreferenceId=id;	
						}
						$scope.daywiseDietFormView=true;
                        $scope.daywiseDietFormCopy=true;
						$scope.selectedDietTypeFun=function(id){
							$scope.dietOrderObj.selectedDietTypeId=id;

							if(id==undefined || $scope.patientDetails.uhIdNumber=="" ||$scope.patientDetails.patientId==""){
                                    $scope.daywiseDietFormView=true;
                                  $scope.daywiseDietFormCopy=true;
                                 }
                              else{
                                 $scope.daywiseDietFormView=false;
                                 $scope.daywiseDietFormCopy=false;
                              }

                            if($scope.datePickerForDietPlan != null && $scope.datePickerForDietPlan != undefined && $scope.datePickerForDietPlan != ""){
                                $scope.datePickerChange();
                            }
						}
							
							$scope.add = function(ind) {
								var data = {

									"dishId" : '',
									"quantity" : '',
									
								};

								$scope.selectedMeal.push(data);
							};

							$scope.remove = function(index) {
								var newDataList = [];
								var le = $scope.selectedMeal.length;
								var i = 1;
								$scope.selectedMeal.splice(index-1,1)

							};

							$scope.isExpiredMealTime = false;

							$scope.selectMeal = function(mealDetail,selectedDay,selectedMeal,servingTime,date) {

//							console.log("servingTime=======",servingTime);
                                $scope.isExpiredMealTime = $scope.checkExpiredMealTime(servingTime,date);

                                if($scope.isExpiredMealTime == true){
                                    growl.warning(" Selected meal time is expired ", {
                                    										title : 'warning!'
                                    									});

                                      angular.forEach($scope.dietTemplate.mealDetailsList[selectedMeal].mealsList , function(v,k){
                                                 v.isExpiredMealTime = $scope.isExpiredMealTime;
                                       });
                                      $('#editDietTemplate').modal('hide');
                                    return false;
                                }

								if(mealDetail.length == 0) {
									var data = {
											"dishId" : '',
											"quantity" : '',
										};
										$scope.selectedMeal.push(data);
								}
								else {
									$scope.selectedMeal = angular.copy(mealDetail);
									$scope.mealNotes = mealDetail[0].mealNotes;
								}
								$scope.currentSelectedDay = selectedDay;
								$scope.currentSelectedMeal = selectedMeal
								 $scope.getDishMasterList($scope.dietOrderObj.mealPreferenceId, $scope.dietTemplate.mealDetailsList[selectedMeal].mealId, $scope.selectedDietTypeId);
							};

							$scope.checkExpiredMealTime = function(targetTime,targetDate){
							    // Get the current time on the user's device
							    var targetDateArray = targetDate.split("-");
							    var targetTimeArray = targetTime.split(":");
                                var currentTime = new Date();
                                var targetTime = new Date(targetDateArray[0],targetDateArray[1]-1,targetDateArray[2],targetTimeArray[0],targetTimeArray[1]);

                                // Compare the date and time
                                if (currentTime > targetTime) {
                                    return true;
                                }

                                return false;

                            };
							
							$scope.onChangeDish = function($event, index) {
								$scope.selectedMeal[index] = angular.copy($event.meal)
							};
							
							  $scope.getDishMasterList = function (dietPrefId, mealPreferenceId, dietTypeId) {
								  /*var URI = BASE_URL + ROOT_URL
									+ GETDISHLISTBYDIETPREFID;*/

                                $scope.selectedDietPreference = [];

							    var searchDietObj = {
                                    dietPrefId:dietPrefId,
                                    mealPreferenceId:mealPreferenceId,
                                    dietTypeId:dietTypeId
							    };

								$scope.selectedDietPreference.push(searchDietObj);

								  var URI = BASE_URL + ROOT_URL
									+ GETDISHLISTBYDIETPREFID + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								  
							// For Get Dish Master List Using Offset
							GenericService
									.serviceAction("POST", URI, $scope.selectedDietPreference)
									.then(
											function(response) {
												$scope.dishMasterList = [];

												if (response.data.status == "success") {
													$scope.dishMasterList = response.data.listObject;
													console.log("dishMasterList",$scope.dishMasterList);

												}
											});	
							  }
							  
								$scope.getNutritionValues = function(dietTemplateId) {
								 	var data = {};
									var URI = BASE_URL + ROOT_URL
								+ GETDAYWISENUTRITIONLIST + '?dietTemplateId=' + dietTemplateId;
									
									/*var URI = BASE_URL + ROOT_URL
									+ GETDAYWISENUTRITIONLIST + '?dietTemplateId=' + dietTemplateId + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/
						
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.nutritionList = []
												console.log("nutritionList",$scope.nutritionList);
												
												var nutritionList = response.data.listObject;
												for(var i = 0; i<nutritionList.length; i++) {
													$scope.dayWiseNutritionalList[i] = nutritionList[$scope.dayEnum[$scope.dayPickerDynamicValues[i].toLowerCase()]]
												}
											}
										});
							};
								
							
							  
							  $scope.isMealSelected = false;
							  $scope.saveDishDetails = function() {
								  $scope.tempList=[];
								  var isValid =true;
								  angular.forEach($scope.selectedMeal, function(val, key){
									  if(val.dishDescription != undefined &&
										val.dishId != undefined && val.quantity!= undefined){
										  var newArr={};
									  newArr.dietPreferenceList=val.dietPreferenceList;
									  newArr.dishDescription=val.dishDescription;
									  newArr.dishId=val.dishId;
									  newArr.quantity=val.quantity;
									  newArr.mealNotes=$scope.mealNotes;
									   $scope.tempList.push(newArr);
									   $scope.isMealSelected = true;
									  }else{
										  $scope.tempList=[];
										  isValid =false;
									  }
									  
								  });
								  $scope.dietTemplate.mealDetailsList
								  	[$scope.currentSelectedMeal].mealsList
								  	[$scope.currentSelectedDay].dishList = $scope.tempList;
								    var meals_data = $scope.dietTemplate.mealDetailsList;
								    // hard-coded to 5 as meal_type are of 5 types only
								    // currentSelectedDay will give you current selected day for which you need to 
								    // call the API
								    var meal_for_the_day = [];
								    var day_index_to_consider = $scope.currentSelectedDay;
								    for(var meal_type = 0; meal_type < meals_data.length; meal_type++) {
								    	var current_meal = meals_data[meal_type]['mealsList'][day_index_to_consider];
							    		for(var item = 0; item < current_meal['dishList'].length; item++) {
							    			if('dishId' in current_meal['dishList'][item]) {
							    				if(current_meal['dishList'][item].dishId != undefined && current_meal['dishList'][item].dishId != null){
													meal_for_the_day.push(current_meal['dishList'][item]);
												}
							    			}
							    		}
							    	}
								    
								    /*var URI = BASE_URL + ROOT_URL
									+ GETNUTRITIONALVALUESBYDISHLIST;*/
								    if(meal_for_the_day != null && meal_for_the_day.length > 0 && isValid != false){
										var URI = BASE_URL + ROOT_URL
									+ GETNUTRITIONALVALUESBYDISHLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
									
									GenericService.serviceAction("POST", URI, meal_for_the_day)
									.then(function(response) {
												if (response.data.status == "success") {
													$scope.dayWiseNutritionalList[parseInt($scope.currentSelectedDay)].nutritionList = response.data.listObject;
													$scope.mealNotes="";
													console.log("nutritionList",$scope.nutritionList);

												}
											});
									}else{
										$scope.mealNotes="";
										$scope.tempList=[];
										growl.warning("Please enter valid data.");
										
									}
													   
							  }
								  
							  
							  $scope.copySelectedMeal = function() {
									var dayId = $scope.dayEnum[$scope.copyFromDay.toLowerCase()];
									var copyFromMeal = $scope.dietTemplate.mealDetailsList
									[$scope.currentSelectedMeal].mealsList
									
									for(var i=0 ; i< copyFromMeal.length; i++) {
										if(copyFromMeal[i].dayId == dayId) {
											$scope.selectedMeal = angular.copy(copyFromMeal[i].dishList)
											break
										}
									}
							  }
							  
							  
								  // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveMealMaster();
							}
							
							// Function For Save Meal MAster
							$scope.saveMealMaster = function() {
								$scope.$broadcast('show-errors-check-validity');
								$rootScope.startLoader();

                                if ($scope.patientDetails === undefined || $scope.patientDetails === null || $scope.patientDetails === "") {
                                    growl.error("Please select any patient", {
                                        title: 'Error!'
                                    });
                                    $rootScope.stopLoader();
                                    return;
                                }

								if ($scope.dietOrderObj.mealPreferenceId === undefined || $scope.dietOrderObj.mealPreferenceId === null || $scope.dietOrderObj.mealPreferenceId === "") {
                                    growl.error("Please select Diet Preference", {
                                        title: 'Error!'
                                    });
                                    $rootScope.stopLoader();
                                    return;
                                }

                                if ($scope.selectedDietTypeId === undefined || $scope.selectedDietTypeId === null || $scope.selectedDietTypeId === "") {
                                    growl.error("Please select Diet Type", {
                                        title: 'Error!'
                                    });
                                    $rootScope.stopLoader();
                                    return;
                                }

                                if ($scope.isMealSelected == false) {
                                    growl.error("Please select any dish", {
                                        title: 'Error!'
                                    });
                                    $rootScope.stopLoader();
                                    return;
                                }

								var mealDetailsObj = {};
								mealDetailsObj.dietPreferenceId = ($scope.dietOrderObj.mealPreferenceId != undefined && $scope.dietOrderObj.mealPreferenceId != null && $scope.dietOrderObj.mealPreferenceId != "") ? $scope.dietOrderObj.mealPreferenceId :$scope.dietPref.dietPrefId;
								mealDetailsObj.mealDetailsList = $scope.dietTemplate['mealDetailsList']
								mealDetailsObj.dietTypeId = $scope.selectedDietTypeId;
								mealDetailsObj.createdBy = $scope.userId;
								mealDetailsObj.patientId = $scope.patientDetails.patientId;
								mealDetailsObj.organizationId = $scope.organizationId;
								mealDetailsObj.unitId = $scope.unitId;
								//	mealDetailsObj.dietPreferenceList = $scope.selectedDietPreference
								
								
								
								if ($scope.daywiseDietForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ SAVEDIETPLANNING;
									
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											
											GenericService
												.serviceAction("POST",URI,mealDetailsObj)
												.then(
														function(response) {
															if (response.data.status == "success" && response.data.message=="Added Successfully.") {
																$scope.dietTemplate = {}
																$scope.nutritionList = []
																growl
																		.success(
																				'Diet Template added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$rootScope.stopLoader();
																$scope
																		.$broadcast('show-errors-reset');
																$scope.patientDetails={};
																$scope.dietOrderObj = {};
																$scope.selectedDietTypeId = "";
																$scope.dayWiseNutritionalList = [{nutritionList:[]},{nutritionList:[]},
																	{nutritionList:[]},{nutritionList:[]},{nutritionList:[]},{nutritionList:[]},{nutritionList:[]}];
																$scope.dayWiseDietObj = $scope.dummyDayWiseDietObj;
																
																
																$scope.dummyDietPatientRequestObj = {
																		dietPreferenceId: "",
																		dietTypeId: "",
																		
																	};
																$scope.dishPropertyObjList = [ {
																	
																	"dishId" : '',
																	"quantity" : '',
																
																} ];

                                                                $scope.patientDetails={};
                                                                $scope.patientList = [];
//                                                                $scope.datePickerForDietPlan = "";
//                                                                $scope.datePickerDynamicValues = [];
                                                                $scope.datePickerForDietPlan=new Date();
                                                                $scope.isMealSelected = false;

                                                                $scope.patientSearchObj = {
                                                                        uhIdNumber : "",
                                                                        patientName : "",
                                                                        genderId : "",
                                                                        mobileNo : "",
                                                                        birthDate : "",
                                                                        identificationNo : "",
                                                                        patientType : 1,
                                                                        unitId : $scope.unitId,
                                                                        organizationId : $scope.organizationId
                                                                    };

																//$scope.init();
															}else if(response.data.status == "success" && response.data.message=="Patient Discharged"){
                                                                $rootScope.stopLoader();
                                                                $scope.emptyDietTemplateFields();
                                                                $scope.isMealSelected = false;
                                                                growl
                                                                        .error(
                                                                                'Patient already discharged...!!',
                                                                                {
                                                                                    title : 'Error!'
                                                                                });
                                                            } else {
																$rootScope.stopLoader();
																$scope.emptyDietTemplateFields();
																$scope.isMealSelected = false;
																growl
																		.error(
																				'Diet already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
														});
											}
										}
										
										
								// }

							}

							$scope.emptyDietTemplateFields = function(){
                                $scope.dietTemplate = {};
                                $scope.nutritionList = [];
                                $scope.datePickerDynamicValues = [];
                                $scope.dayWiseNutritionalList = [];
							}
							
							
							$scope.getDietPreferenceList = function() {

								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/adt/unit/getMealPreferenceMasterList";
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													$rootScope.stopLoader();
													$scope.getDietPreferenceList = [];
													$scope.getDietPreferenceList = response.data.listObject;
													console
															.log(
																	"getDietPreferenceList",
																	$scope.getDietPreferenceList);

												});
							}
							$scope.getDietPreferenceList();
							
							// For Paginations Start for nursing patient acceptance
							$scope.dateFormat = cookieObjectAppConfig.dateFormat;
							 $scope.skipHousekeeping = cookieObjectAppConfig.skipHousekeeping;
							 var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
								var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
								$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
								 if(specialtyNursingStationCookies == undefined){
									 growl.warning(
												'No nursing station and specialty mapped with current user!!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   console.log('specialtyNursingStationCookies',specialtyNursingStationCookies);
								   console.log('nursingStationCookie',nursingStationCookie);
								   $scope.nursingStationDesc=nursingStationCookie;
								   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
									console.log("ggg",$scope.departmentNursingStationMapperDtoslist);
								  }
							 
									
								    $scope.isWardChanged = function()
								    {
								       
								      
						            		//start for nursing
						        				var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
						        				  GenericService.serviceAction("GET",URI,{}).then(function(response){
						        					  if (response.data.status == "success") {
						        						  $scope.departmentNursingStationMapperDtoslist = [];
						        						  
						        							$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
						        							//alert($scope.roleName);
						        							if($scope.nurseRole == 1){
						        							if($scope.departmentNursingStationMapperDtoslist.length > 0)
						        								{
						        									if($scope.departmentNursingStationMapperDtoslist.length > 1){
						        										angular.element('#nursingStationModal').modal('show');
						        										//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
						        									}
						        									else
						        										{
						        										var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
						        										  GenericService.serviceAction("GET",URI,{}).then(function(response){
						        											  if (response.data.status == "success") {
						        												  $scope.departmentNursingStationListC = {};
						        												 // console.log("fff",response.data.object);
						        												  $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
						        												  
						        												  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
						        												  sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
						        												  sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
						        												  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
						        												  //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
						        												  sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
						        												  $scope.wardListForGetBedList = response.data.object;
						        											  }
						        										 });
						        										}
						        								}
						        					  		}
						        						}
						        				 });
								        
								    }
							$scope.setNoOfRecords = function() {
								$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
							};
							$scope.initAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
								
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								var commonObjForTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardId :$scope.wardId ,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										visitTypeId :$scope.visitTypeId != undefined ? $scope.visitTypeId :2,
//										isFromNursingAcceptancePage:'Y'
										};
								
									var URI = BASE_URL + ROOT_URL+ "/adt/getAdmittedPatientListForDietOrder";
									$rootScope.startLoader();
							GenericService
									.serviceAction("POST", URI, commonObjForTransfer)
									.then(
											function(response) {
												$scope.listOfAdmittedPatientListforAddmitted = [];
												$scope.admittedPatientList = [];

												if (response.data.status == "success") {
													
													if(response.data.listObject.length==0)
														$scope.loadingMsg="No records found...!!";
													$scope.admittedPatientList = response.data.listObject;
													$scope.commonListCount = response.data.object;
													angular.forEach($scope.admittedPatientList,
																	function(value,index) {

																		var newArr = {};
																		newArr.specialityName = value.specialityName;
																		newArr.doctorFullName = value.dFirstName;
																		newArr.pFirstName = value.pFirstName;
																		newArr.reasonDesc = value.reasonDesc;
																		newArr.dob = value.dob;
																		newArr.datepicker = moment(value.doa,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
																		newArr.datepickerPdd = moment(value.pdd,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
																		newArr.genderCode = value.genderCode;
																		newArr.genderId = value.genderId;
																		newArr.doctorId = value.doctorId;
																		newArr.specialityId = value.specialityId;
																		newArr.uhidnumber = value.uhidnumber;
																		newArr.createdTime = value.createdTime;
																		newArr.admissionNumber = value.admissionNumber;
																		newArr.patientId = value.patientId;
																		newArr.tPatientId = value.tPatientId;
																		newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																		newArr.bedNumber = value.bedNumber;
																		newArr.admissionId = value.admissionId;
																		newArr.prefixCode = value.prefixCode;
																		newArr.identificationNo = value.identificationNo;
																		newArr.wardName = value.wardName;
																		newArr.concent = value.concent;
																		newArr.bedCategoryId=value.bedCategoryId;
																		newArr.patientCategoryId=value.patientCategoryId;
																		newArr.visitTypeId=value.visitTypeId;
																		newArr.paymentEntitlementId=value.paymentEntitlementId;
																		newArr.contractId = value.contractId;
																		newArr.dischargeStatusId=value.dischargeStatusId;
																		newArr.billingBedCategoryId = value.billingBedCategoryId;
																		newArr.isCriticalOrder = value.isCriticalOrder;
																		newArr.medicoLegalId = value.medicoLegalId;
																		newArr.isVip = value.isVip;
			                                                            newArr.dischargeId = value.dischargeId;
			                                                            newArr.ageStr = value.ageStr;
			                                                            newArr.imagePath = value.imagePath;
			                                                            newArr.prefixDesc = value.prefixDesc;
			                                                            newArr.prefixId = value.prefixId;
			                                                            newArr.ageStr = value.ageStr;
			                                                            newArr.visitTypeId= value.visitTypeId;
			                                                            newArr.wardId = value.wardId;
			                                                            newArr.floorName = value.floorName;
			                                                            newArr.wardName = value.wardName;
			                                                            newArr.roomId = value.roomId;
			                                                            newArr.bedId= value.bedId;
			                                                            newArr.uuid= value.uuid;
			                                                            newArr.isIcuu= value.isIcuu;
			                                                            newArr.isFinalBill= value.isFinalBill;
			                                                            newArr.referenceNumber= value.referenceNumber;
			                                                            console.log("visitTypeData",value.visitTypeId);
			                                                            //ip
//			                                                            if($scope.visitTypeId == "2" ){
//																		if(value.visitTypeId==2){
																		$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
//																		console.log("list data",$scope.listOfAdmittedPatientListforAddmitted );
////																		}
//			                                                            }
//			                                                            //day care
//			                                                            if($scope.visitTypeId == "3" ){
////																			if(value.visitTypeId==3){
//																			$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
//																			console.log("list data",$scope.listOfAdmittedPatientListforAddmitted );
////																			}
//			                                                                }
//			                                                            
																		$scope.setPage(1,false);
																	});
													$rootScope.stopLoader();
												}else{
													$rootScope.stopLoader();
												}
											});
									

								
							}
							$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
							$scope.pager = {};
							$scope.page;
							
							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.gettAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}
							
							$scope.gettAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								var URI = BASE_URL + ROOT_URL +  "/adt/getAdmittedPatientListForDietOrder";
								
								var commonObjForTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardId :$scope.wardId ,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										visitTypeId :$scope.visitTypeId != undefined ? $scope.visitTypeId :2,
//										isFromNursingAcceptancePage:'Y'
										};
								
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='Y';
									}else{
									   $scope.nursingWardList=[];
									}
								}
								else{
									commonObjForTransfer.bedList=$scope.nurseBedList;
									commonObjForTransfer.isShowPatientByBed='N';
								}

								if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
									
									var URI = BASE_URL + ROOT_URL+  "/adt/getAdmittedPatientListForDietOrder";
									$rootScope.startLoader();
							GenericService
									.serviceAction("POST", URI, commonObjForTransfer)
									.then(
											function(response) {
												$scope.listOfAdmittedPatientListforAddmitted = [];
												$scope.admittedPatientList = [];

												if (response.data.status == "success") {
													$scope.admittedPatientList = response.data.listObject;
													angular.forEach($scope.admittedPatientList,
																	function(value,index) {

																		var newArr = {};
																		newArr.specialityName = value.specialityName;
																		newArr.doctorFullName = value.dFirstName;
																		newArr.pFirstName = value.pFirstName;
																		newArr.reasonDesc = value.reasonDesc;
																		newArr.dob = value.dob;
																		newArr.datepicker = value.doa;
																		newArr.datepickerPdd = value.pdd;
																		newArr.genderCode = value.genderCode;
																		newArr.genderId = value.genderId;
																		newArr.doctorId = value.doctorId;
																		newArr.specialityId = value.specialityId;
																		newArr.uhidnumber = value.uhidnumber;
																		newArr.createdTime = value.createdTime;
																		newArr.admissionNumber = value.admissionNumber;
																		newArr.patientId = value.patientId;
																		newArr.tPatientId = value.tPatientId;
																		newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																		newArr.bedNumber = value.bedNumber;
																		newArr.admissionId = value.admissionId;
																		newArr.prefixCode = value.prefixCode;
																		newArr.identificationNo = value.identificationNo;
																		newArr.wardName = value.wardName;
																		newArr.concent = value.concent;
																		newArr.bedCategoryId=value.bedCategoryId;
																		newArr.patientCategoryId=value.patientCategoryId;
																		newArr.visitTypeId=value.visitTypeId;
																		newArr.paymentEntitlementId=value.paymentEntitlementId;
																		newArr.contractId = value.contractId;
																		newArr.dischargeStatusId=value.dischargeStatusId;
																		newArr.billingBedCategoryId = value.billingBedCategoryId;
																		newArr.medicoLegalId = value.medicoLegalId;
																		newArr.dischargeId = value.dischargeId;
																		newArr.isCriticalOrder = value.isCriticalOrder;
																		newArr.isVip = value.isVip;
																		newArr.ageStr = value.ageStr;
			                                                            newArr.imagePath = value.imagePath;
			                                                            newArr.prefixDesc = value.prefixDesc;
			                                                            newArr.prefixId = value.prefixId;
			                                                            newArr.ageStr = value.ageStr;
			                                                            newArr.wardId = value.wardId;
			                                                            newArr.floorName = value.floorName;
			                                                            newArr.wardName = value.wardName;
			                                                            newArr.roomId = value.roomId;
			                                                            newArr.bedId= value.bedId;
			                                                            newArr.uuid= value.uuid;
			                                                            newArr.referenceNumber= value.referenceNumber;
			                                                            
//																		if(value.visitTypeId==2){
																		$scope.listOfAdmittedPatientListforAddmitted.push(newArr);
																		
//																		}

																	});
													$rootScope.stopLoader();
												}else{
													$rootScope.stopLoader();
												}
											});
									}
							}
							
							$scope.saveCopiedMealMasterToAllPatient = function() {
								$rootScope.startLoader();
								var checkSelected = true;
								angular.forEach($scope.listOfAdmittedPatientListforAddmitted,
										function(value,index) {
									if(value.isSelected == "Y"){
										checkSelected = false;
									}
								});
								
								if(checkSelected == true){
									growl.warning("Please Select Patient ", {
										title : 'warning!'
									});
									return false;
								}
								angular.forEach($scope.listOfAdmittedPatientListforAddmitted,
										function(value,index) {
									if(value.isSelected == "Y"){
										var mealDetailsObj = {};
										mealDetailsObj.dietPreferenceId = ($scope.dietOrderObj.mealPreferenceId != undefined && $scope.dietOrderObj.mealPreferenceId != null && $scope.dietOrderObj.mealPreferenceId != "") ? $scope.dietOrderObj.mealPreferenceId :$scope.dietPref.dietPrefId;
										mealDetailsObj.mealDetailsList = $scope.dietTemplate['mealDetailsList']
										mealDetailsObj.dietTypeId = $scope.selectedDietTypeId;
										mealDetailsObj.createdBy = $scope.userId;
										mealDetailsObj.patientId = value.patientId;
										mealDetailsObj.organizationId = $scope.organizationId;
										mealDetailsObj.unitId = $scope.unitId;
										//	mealDetailsObj.dietPreferenceList = $scope.selectedDietPreference
										if ($scope.daywiseDietForm.$valid) {
											var URI = BASE_URL + ROOT_URL
											+ SAVEDIETPLANNING;
													GenericService
														.serviceAction("POST",URI,mealDetailsObj)
														.then(
																function(response) {
																	if (response.data.status == "success") {
																		$scope.dietTemplate = {}
																		$scope.nutritionList = []
																		growl
																				.success(
																						'Diet Template added Successfully...!!',
																						{
																							title : 'Success!'
																						});
																		$scope
																				.$broadcast('show-errors-reset');
																		$rootScope.stopLoader();
																		//$scope.init();
																	} else {
																		growl
																				.error(
																						'Diet already exits...!!',
																						{
																							title : 'Error!'
																						});
																		$rootScope.stopLoader();
																	}
																});
												}
												
											
									}
								});
								}
							
				
							
}]);
