angular
		.module('myApp')
		.controller(
				'addDayWiseDietController',
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
							
							
							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
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
								$scope.dayWiseNutritionalList = [{nutritionList:[]},{nutritionList:[]},
									{nutritionList:[]},{nutritionList:[]},{nutritionList:[]},{nutritionList:[]},{nutritionList:[]}];
							
								
								
								$scope.mealMasterPreferenceObj = {
										noOfRecordsPerPage: 10,
										organizationId: $scope.organizationId,
										offset: 0,
										unitId : $scope.unitId
								};
								$scope.selectedDietPreference = []
								
								$scope.dishPropertyObjList = [ {
								
									"dishId" : '',
									"quantity" : '',
								
								} ];

								var data = {};

								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDIETTYPEMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var URI = BASE_URL + ROOT_URL
										+ GETALLDIETTYPEMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/

								// For Get Nutrition List Using Offset
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
								
								
								
						var URI = BASE_URL + ROOT_URL
						+ GETMEALMASTERDROPDOWN + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;

				// For Get Meal List Using Offset
				GenericService
						.serviceAction("GET", URI, data)
						.then(
								function(response) {
									$scope.mealMasterList = [];

									if (response.data.status == "success") {
										var mealList = response.data.listObject;
										for(var i = 0; i<mealList.length; i++) {
											mealList[i].mealsList = [
													{dayId : 0, dishList : []},
													{dayId : 1, dishList : []},
													{dayId : 2, dishList : []},
													{dayId : 3, dishList : []},
													{dayId : 4, dishList : []},
													{dayId : 5, dishList : []},
													{dayId : 6, dishList : []}
												]
										}
										$scope.mealMasterList = mealList;
									}
								});
					
				// For Get Meal Master PReference from ADT List Using Offset
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEMEALPREFERENCELIST  + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
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
									$scope.getDishMasterList();
								}
								
						});
			
				
							
							}
							
							$scope.init();
							
							
					/*	
							*/
							
							var selectedDietPref 
							
							$scope.getDietTemplate = function() {
						
								data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETDIETTEMPLATEBYID + '?dietTemplateId=' + $stateParams.dietTemplateId + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
						// For Get Diet List Using Offset
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												var dietTemplate = response.data.object
												$scope.getNutritionValues($stateParams.dietTemplateId)
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
							}
							
							$scope.getNutritionValues = function(dietTemplateId) {
						         var data = {};
						         var URI = BASE_URL + ROOT_URL + GETDAYWISENUTRITIONLIST + '?dietTemplateId=' + dietTemplateId;
								 GenericService
								 .serviceAction("GET", URI, data)
								 .then(
						                 function(response) {
						                         if (response.data.status == "success") {
						                                 $scope.dayWiseNutritionalList = response.data.listObject;
						                                 console
						                                                 .log(
						                                                                 "nutritionList",
						                                                                 $scope.nutritionList);

						                         }
						                 });

						};
							
							$scope.getDishMasterList = function() {
								var URI = BASE_URL + ROOT_URL
								+ GETDISHLISTBYDIETPREFID + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								/*var URI = BASE_URL + ROOT_URL
								+ GETDISHLISTBYDIETPREFID + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/

								// For Get Dish Master List Using Offset
								GenericService
								.serviceAction("POST", URI, $scope.selectedDietPreference)
								.then(
										function(response) {
											$scope.dishMasterList = [];

											if (response.data.status == "success") {
												$scope.dishMasterList = response.data.listObject;
											//	$scope.selectedDietPreference = [];
												console
														.log(
																"dishMasterList",
																$scope.dishMasterList);

											}
										});	
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
							
							$scope.selectMeal = function(mealDetail,selectedDay,selectedMeal) {
								if(mealDetail.length == 0) {
									$scope.selectedMeal = [{}]
								}
								else {
									$scope.selectedMeal = angular.copy(mealDetail);
								}
								$scope.currentSelectedDay = selectedDay;
								$scope.currentSelectedMeal = selectedMeal
							};
							
							$scope.onChangeDish = function($event, index) {
								$scope.selectedMeal[index] = angular.copy($event.meal)
							};
							
							
							//For MealPreference
							$scope.selectedDietPreference = [];
							$scope.mealMasterPreferenceList = [];
							$scope.mealMasterPreferenceList1 = [];
							$scope.mealMasterPreferenceList1 =  angular.copy($scope.mealMasterPreferenceListTemp);
							var count = 1;
							 angular.forEach($scope.mealMasterPreferenceList1,function(value,index){
								 angular.forEach(dishObj.dietPreferenceList,function(value1,index1){
									 	if(value1.dietPreferenceId == value.mealPreferenceId){
									 		$scope.mealMasterPreferenceList1[index].isChecked = true;
									 		var obj = {
													  "dietPrefId" : value1.dietPreferenceId
											  }	
										 	$scope.selectedDietPreference.push(obj);
									 	}
									 	
                                   });
								 if(count == $scope.mealMasterPreferenceList1.length){
									 $scope.mealMasterPreferenceList = $scope.mealMasterPreferenceList1;
								 }
								 count++;
							 });
							
							  $scope.changedValue = function(item,index) {
								  angular.forEach($scope.mealMasterPreferenceList,function(value,index){
									  if($scope.selectedDietPreference.length <= $scope.mealMasterPreferenceList.length)
									  {
									 	if(value.isChecked == true){
									 		var obj = {
													  "dietPrefId" : value.mealPreferenceId
											  }	
										 	$scope.selectedDietPreference.push(obj);
									 	}
										 else if (!value.isChecked) {
											    if ($scope.selectedDietPreference.length > 0) {
											        var index = $scope.selectedDietPreference.findIndex(x => x.mealPreferenceId == value.mealPreferenceId);
											        if (index !== -1) {
											            $scope.selectedDietPreference.splice(index, 1);
											        }
											    }
									  }
									  }
                                 });
								  
								  $scope.getDishMasterList()
							  };
							  
							  $scope.saveDishDetails = function() {
								  	$scope.mealMasterList
								  	[$scope.currentSelectedMeal].mealsList
								  	[$scope.currentSelectedDay].dishList = $scope.selectedMeal
								  		
								  	$scope.selectedMeal = []
								    var meals_data = $scope.mealMasterList;
								    // hard-coded to 5 as meal_type are of 5 types only
								    // currentSelectedDay will give you current selected day for which you need to 
								    // call the API
								    var meal_for_the_day = [];
								    var day_index_to_consider = $scope.currentSelectedDay;
								    for(var meal_type = 0; meal_type < 5; meal_type++) {
								    	var current_meal = meals_data[meal_type]['mealsList'][day_index_to_consider];
							    		for(var item = 0; item < current_meal['dishList'].length; item++) {
							    			if('dishId' in current_meal['dishList'][item]) {
							    				meal_for_the_day.push(current_meal['dishList'][item]);
							    			}
							    		}
							    	}
									var URI = BASE_URL + ROOT_URL
									+ GETNUTRITIONALVALUESBYDISHLIST;
									
									GenericService
									.serviceAction("POST", URI, meal_for_the_day)
									.then(
											function(response) {
												if (response.data.status == "success") {
													
													$scope.dayWiseNutritionalList[parseInt($scope.currentSelectedDay)].nutritionList = response.data.listObject;
													console.log("nutritionList",$scope.nutritionList);
												}
											});					    
							  }
								  
							  
							  
							  $scope.copySelectedMeal = function() {
									var dayId = $scope.dayEnum[$scope.copyFromDay];
									$scope.selectedMeal = angular.copy($scope.mealMasterList[$scope.currentSelectedMeal].mealsList[dayId].dishList)
							  }
							  
							  
							  
							  
								  // function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveMealMaster();
							}
							// Function For Save Meal MAster
							$scope.saveMealMaster = function() {
								$scope.$broadcast('show-errors-check-validity');
								
								var mealDetailsObj = {};
								mealDetailsObj.mealDetailsList = $scope.mealMasterList
								mealDetailsObj.dietTypeId = $scope.selectedDietTypeId;
								mealDetailsObj.createdBy = $scope.userId;
								mealDetailsObj.updatedBy = $scope.userId;
								mealDetailsObj.dietPreferenceList = $scope.selectedDietPreference;
								mealDetailsObj.organizationId = $scope.organizationId;
								mealDetailsObj.unitId = $scope.unitId;
								
								
								if ($scope.daywiseDietForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ DIETTEMPLATE;
									if ($scope.editTemplate) {
										$scope.updateMealMaster()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											
											GenericService
												.serviceAction(
														"POST",
														URI,
														mealDetailsObj)
												.then(
														function(response) {
															if (response.data.status == "success") {
																growl
																		.success(
																				'Diet Template added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
															} else {
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
										
										
								}

							}
							$scope.updateMealMaster = function() {
								 $scope.$broadcast('show-errors-check-validity');
								 
									var mealDetailsObj = {};
									mealDetailsObj.dietTemplateId = $stateParams.dietTemplateId;
									mealDetailsObj.mealDetailsList = $scope.mealMasterList;
									mealDetailsObj.dietTypeId = $scope.selectedDietTypeId;
									mealDetailsObj.createdBy = $scope.userId;
									mealDetailsObj.updatedBy = $scope.userId;
							    	mealDetailsObj.dietPreferenceList = $scope.selectedDietPreference;
							    	mealDetailsObj.organizationId = $scope.organizationId;
									mealDetailsObj.unitId = $scope.unitId;
									
							    //	mealDetailsObj.dietPreferenceList = $scope.dietTemplate.dietPreferenceList; 
							    	
							    	
							     	
								 if ($scope.daywiseDietForm.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ DIETTEMPLATE;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
								GenericService
										.serviceAction("PUT", URI,
												mealDetailsObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Dish Master updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Dish Master Update already exits...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
								 }
								 }
							
						}
						}]);

