'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:keyIngredientsMasterController
 * @description #keyIngredientsMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'dishMasterController',
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
						'PaginationService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,PaginationService) {

							/* init() function for form object create. */

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
				
								
								
								

							//	$scope.unitId = 1;
							//	$scope.organizationId = 1;
							//	$scope.userId = cookieObject.userId;
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
								  $scope.selectedMealType = []
									$scope.selectedDietType = []
								
								 $scope.pagination = {
					                    currentPage: 1,
					                    totalPages: 0,
					                    pageSize: '10',
					                    search: '',
					                    searchData: PaginationService.searchData,
					                    setTotalPages: PaginationService.setTotalPages,
					                    getPagesList: PaginationService.getPagesList,
					                    setPage: PaginationService.setPage,
					                    setFirstPage: PaginationService.setFirstPage,
					                    setPreviousPage: PaginationService.setPreviousPage,
					                    setNextPage: PaginationService.setNextPage,
					                    setLastPage: PaginationService.setLastPage,
					                    sortBy: PaginationService.sortBy
					                  }
								
								$scope.onEditDishMaster = false;
								$scope.dummyDishMasterObj = {
										dishCode : "",
										dishDescription : "",
										dishValue : "",
										dietPreferenceId: '',
										uomId : '',
								
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										status : 'A',


								};
								
								$scope.dishMasterObj = $scope.dummyDishMasterObj;
								
								// for nutrition List save start
								$scope.keyIngredientObjList = [ {
									"keyIngredientId" : '',
									"value" : '',
									"uomId" : '',
									"dishMasterKeyIngredientsMapperId" : ''
								} ];
								// for nutrition List save end

								var data = {};

								

								
								var URI = BASE_URL + ROOT_URL
										+ GETALLDISHMASTER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;

								// For Get Key Ingredient Master List Using
								// Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													
												    $scope.pagination.data=[];
													$scope.dishMasterList = [];

													if (response.data.status == "success") {
														$scope.dishMasterList = response.data.listObject;
														
														 if($scope.dishMasterList.length > 0){            
									                         PaginationService.searchData($scope.dishMasterList, $scope.pagination);
									                       }
													}
												});
//								}

								var URI = BASE_URL + ROOT_URL
										+ GETUNITMASTERDETAILS + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								// For Get Key unit of measurement Master List
								// Using

								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.unitMeasurementList = [];

													if (response.data.status == "success") {
														$scope.unitMeasurementList = response.data.listObject;
														console
																.log(
																		"unitMeasurementList",
																		$scope.unitMeasurementList);

													}
												});

								var URI = BASE_URL + ROOT_URL
								+ GERACTIVEKEYINGREDIENTMASTER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								// For Get Nutrition List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.ingredientList = [];

													if (response.data.status == "success") {
														$scope.ingredientList = response.data.listObject;
														console
																.log(
																		"ingredientList",
																		$scope.ingredientList);

													}
												});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEMEALPREFERENCELIST;
								GenericService
								.serviceAction("POST", URI, $scope.mealMasterPreferenceObj)
								.then(
										function(response) {
								$scope.mealMasterPreferenceList = [];
								$scope.mealMasterPreferenceListTemp = [];
									if (response.data.status == "success") {
										$scope.mealMasterPreferenceList = response.data.listObject;
										
										$scope.mealMasterPreferenceListTemp = response.data.listObject;
										console
											.log(
													"mealMasterPreferenceList",
													$scope.mealMasterPreferenceList);
								
								}
									
							});
								
				
								
						$scope.mealTypeList = [];	
						$scope.getMeal=function(){
							var URI = BASE_URL + ROOT_URL
							+ GETMEALTYPELIST + '?organizationId='+$scope.organizationId+'&unitId='+$scope.unitId;
							GenericService
							.serviceAction("GET", URI, {})
							.then(
									function(response) {
										$scope.mealTypeListTemp = [];
								if (response.data.status == "success") {
									$scope.mealTypeList = response.data.listObject;
									$scope.mealTypeListTemp = response.data.listObject;
							}
								
						});
							
						}
					$scope.getMeal();
					
					
					$scope.dietList = [];
					$scope.getDietType=function(){
					var URI= BASE_URL + ROOT_URL
						+ GETDIETTYPELIST + '?organizationId='+$scope.organizationId+'&unitId='+$scope.unitId;
					GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								$scope.dietListTemp = [];
						if (response.data.status == "success") {
							$scope.dietList = response.data.listObject;
							$scope.dietListTemp = response.data.listObject;
					}
						
				});
				}
					$scope.getDietType();
			}
							
						
							$scope.init();
							
							// To get dish master list
							$scope.getDishMasterList = function() {
								var URI = BASE_URL + ROOT_URL
								+ GETDISHLISTBYDIETPREFID+ "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;

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
							
							// For Checkbox onChange
							$scope.changedValue = function(item) {
								  var obj = {
										  "dietPrefId" : item.mealPreferenceId
								  }	
								 
								/*var obj = item.mealPreferenceId;*/
								  if(item.isChecked){
								    $scope.selectedDietPreference.push(obj);
						
								  }
								  else {
									  var index = null;
									  $scope.selectedDietPreference.findIndex(x => x.mealPreferenceId==item.mealPreferenceId);
									  $scope.selectedDietPreference.splice(index, 1);
									  
								  }
								  
								  $scope.getDishMasterList()
							  };
							  
							 // For Checkbox MealList Onchange
								$scope.changedValueMealType = function(item) {
									 var obj = {
											  "mealTypeId" : item.mealId
									  };	
									 
									/* var obj = item.mealId; */
									  if(item.isChecked){
									    $scope.selectedMealType.push(obj);
							
									  }
									  else {
										  var index = null;
										  $scope.selectedMealType.findIndex(x => x.mealId==item.mealId);
										  $scope.selectedMealType.splice(index, 1);
										  
									  }
							  };
							  
								 // For Checkbox DietTypeList Onchange
							  $scope.changedValueDietType = function(item) {
								  var obj = {
										  "dietTypeId" : item.dietTypeId
								  };	
								 
								/* var obj = item.dietId; */
								  if(item.isChecked){
								    $scope.selectedDietType.push(obj);
						
								  }
								  else {
									  var index = null;
									  $scope.selectedDietType.findIndex(x => x.dietTypeId==item.dietTypeId);
									  $scope.selectedDietType.splice(index, 1);
									  
								  }
							  };
							  
							
							///getDishListByDietPrefId
							$scope.getNutritionalValues = function(dishId) {
								var data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETNUTRITIONALVALUES + '?dishId=' + dishId;
								// For Get Nutrition List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.nutriciansList = [];
													$scope.KeyIngredient = [];
													if (response.data.status == "success") {
														$scope.nutriciansList = response.data.listObject;
														$scope.KeyIngredient  = response.data.listObject2;
														
														console
																.log(
																		"nutritionListValue",
																		$scope.nutriciansList);

													}
												
												});
							}
							
						
							$scope.showDishMasterDetails = function(info){
								$scope.modalIngrediantList = info.nutriciansList;
								
							}

							// Adding the Key Ingredient details
							$scope.add = function(ind) {
								var data = {

									"keyIngredientId" : '',
									"value" : '',
									"uomId" : '',
									"dishMasterKeyIngredientsMapperId" : '',
									"ingredientDescription" : ''
								};

								$scope.keyIngredientObjList.push(data);
							};

							$scope.remove = function(index) {
								var newDataList = [];
								var le = $scope.keyIngredientObjList.length;
								var i = 1;
								if (le != i) {
									angular.forEach(
											$scope.keyIngredientObjList,
											function(v) {
												if (index != i) {
													newDataList.push(v);
												}
												i++;
											});

									$scope.keyIngredientObjList = newDataList;
								}
							};

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDishMaster();
							}

							$scope.editDishMaster = function(dishObj) {
								$scope.dishMasterObj = dishObj;
								$scope.dishMasterObj.dishId = dishObj.dishId;
								$scope.dishMasterObj.dishCode = dishObj.dishCode;
								$scope.dishMasterObj.dishDescription = dishObj.dishDescription;
								$scope.dishMasterObj.dishValue = dishObj.dishValue;
							//	$scope.dishMasterObj.dietPreference = dishObj.dietPreference;
//								$scope.dishMasterObj.dietPreferenceId = dishObj.dietPreferenceId;

								//for Ingerdient List
						
								if($scope.keyIngredientObjList==undefined || $scope.keyIngredientObjList == null || $scope.keyIngredientObjList.length==0 || $scope.keyIngredientObjList== '' )
								{
									var data = {

        									"keyIngredientId" : '',
        									"value" : '',
        									"uomId" : '',
        									"dishMasterKeyIngredientsMapperId" : '',
        									"ingredientDescription" : ''
        								};

        								$scope.keyIngredientObjList.push(data);
								}
								else
									{
									$scope.keyIngredientObjList = dishObj.ingredientsList!=undefined && dishObj.ingredientsList!=null && dishObj.ingredientsList.length!=0 ? dishObj.ingredientsList : $scope.keyIngredientObjList;
									}
							
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
								 
								 
								//For MealType
								 $scope.selectedMealType = [];
									$scope.mealTypeList = [];
									$scope.mealTypeList1 = [];
									$scope.mealTypeList1 =  angular.copy($scope.mealTypeListTemp);
									var countMealType = 1;
									 angular.forEach($scope.mealTypeList1,function(value,index){
										 angular.forEach(dishObj.dietMealTypeList,function(value1,index1){
											 	if(value1.mealTypeId == value.mealId){
											 		$scope.mealTypeList1[index].isChecked = true;
											 		var obj = {
															  "mealTypeId" : value1.mealTypeId
													  }	
												 	$scope.selectedMealType.push(obj);
											 	}
											 	
											 	
											 	
		                                   });
										 if(countMealType == $scope.mealTypeList1.length){
											 $scope.mealTypeList = $scope.mealTypeList1;
										 }
										 countMealType++;
									 });
									 
									 
									//For DietType
									 $scope.selectedDietType = [];
									    $scope.dietList = [];
										$scope.dietList1 = [];
										$scope.dietList1 =  angular.copy($scope.dietListTemp);
										var countDietType = 1;
										 angular.forEach($scope.dietList1,function(value,index){
											 angular.forEach(dishObj.dietTypeList,function(value1,index1){
												 	if(value1.dietTypeId == value.dietTypeId){
												 		$scope.dietList1[index].isChecked = true;
												 		var obj = {
																  "dietTypeId" : value1.dietTypeId
														  }	
													 	$scope.selectedDietType.push(obj);
												 	}
												 	
			                                   });
											 if(countDietType == $scope.dietList1.length){
												 $scope.dietList = $scope.dietList1;
											 }
											 countDietType++;
										 });
								 
								 
								 
								$scope.dishMasterObj.uomId = dishObj.uomId;
								$scope.dishMasterObj.updatedBy = $scope.userId;	
								//$scope.keyIngredientObjList = dishObj.ingredientsList;
								//$scope.dishMasterObj.dietPreferenceList = dishObj.dietPreferenceList;
								//$scope.dishMasterObj.dietMealTypeList = dishObj.dietMealTypeList;
								//$scope.dishMasterObj.dietTypeList = dishObj.dietTypeList;
								
							
								
								 //$scope.selectedDietPreference =  dishObj.dietPreferenceList;
								//$scope.selectedMealType =  dishObj.dietMealTypeList;
								// $scope.selectedDietType = dishObj.dietTypeList;
								
								
								$scope.onEditDishMaster = true;
							}
							
							// Function For Save Ingrediants
							$scope.saveDishMaster = function() {

								 $scope.$broadcast('show-errors-check-validity');
								// return false;

								 if ($scope.dishMasterForm.$valid) {
										var URI = BASE_URL + ROOT_URL
									+ SAVEDISHMASTER;
										if ($scope.onEditDishMaster) {
											$scope.updateDishMaster()
										} else {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									$rootScope.startLoader();
									$scope.dishMasterObj.ingredientsList = [];
									$scope.dishMasterObj.ingredientsList = angular
											.copy($scope.keyIngredientObjList);
									$scope.dishMasterObj.dietPreferenceList = $scope.selectedDietPreference;
									$scope.dishMasterObj.organizationId=$scope.organizationId;
									$scope.dishMasterObj.unitId=$scope.unitId;
									$scope.dishMasterObj.dietMealTypeList = $scope.selectedMealType;
									$scope.dishMasterObj.dietTypeList = $scope.selectedDietType;
									console.log("saasd",$scope.dishMasterObj);

									if($scope.dishMasterObj.dietPreferenceList.length ==0){
                                        growl.error('Meal Preferance should not be empty...!!',{
                                                        title : 'Error!'
                                                    });
                                        $rootScope.stopLoader();
                                        return false;
                                    }

                                    if($scope.dishMasterObj.dietMealTypeList.length==0){
                                        growl.error('Meal Type should not be empty...!!',{
                                                        title : 'Error!'
                                                    });
                                        $rootScope.stopLoader();
                                        return false;
                                    }

                                    if($scope.dishMasterObj.dietTypeList.length == 0){
                                        growl.error('Diet Type should not be empty...!!',{
                                                        title : 'Error!'
                                                    });
                                        $rootScope.stopLoader();
                                        return false;
                                    }

									GenericService
											.serviceAction(
													"POST",
													URI,
													$scope.dishMasterObj)
											.then(
													function(response) {
														// alert(response.data.status);
														if (response.data.status == "success") {
															  $rootScope.stopLoader();
															growl
																	.success(
																			'Dish Master added Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
														    $rootScope.stopLoader();
															growl
																	.error(
																			'Dish Code is Already Exist...!!',
																			{
																				title : 'Error!'
																			});
														}
													});
									 }
										}
								}

							}


							// Function For update keyingredients status
							$scope.updateStatus = function(dishMaster) {


								var data = {
										dishId : dishMaster.dishId,
									status : dishMaster.status,
									updatedBy : $scope.userId,
									
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATEDISHMASTERSTATUS;
								GenericService
										.serviceAction("PUT", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Dish Master Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}

							// Function For update Dish Master
							$scope.updateDishMaster = function() {

								/*$scope.dishMasterObj.ingredientsList = [];
								$scope.dishMasterObj.ingredientsList = angular
										.copy($scope.keyIngredientObjList);*/

								 $scope.$broadcast('show-errors-check-validity');

								 if ($scope.dishMasterForm.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ UPDATEDISHMASTER;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									$rootScope.startLoader();
									$scope.dishMasterObj.ingredientsList = [];
									$scope.dishMasterObj.ingredientsList = angular
											.copy($scope.keyIngredientObjList);
									$scope.dishMasterObj.dietPreferenceList = $scope.selectedDietPreference;
									$scope.dishMasterObj.organizationId=$scope.organizationId;
									$scope.dishMasterObj.unitId=$scope.unitId;
									$scope.dishMasterObj.dietMealTypeList = $scope.selectedMealType;
									$scope.dishMasterObj.dietTypeList = $scope.selectedDietType;
									console.log("saasd",$scope.dishMasterObj);

									if($scope.dishMasterObj.dietPreferenceList.length ==0){
                                        growl.error('Meal Preferance should not be empty...!!',{
                                                        title : 'Error!'
                                                    });
                                        $rootScope.stopLoader();
                                        return false;
                                    }

                                    if($scope.dishMasterObj.dietMealTypeList.length==0){
                                        growl.error('Meal Type should not be empty...!!',{
                                                        title : 'Error!'
                                                    });
                                        $rootScope.stopLoader();
                                        return false;
                                    }

                                    if($scope.dishMasterObj.dietTypeList.length == 0){
                                        growl.error('Diet Type should not be empty...!!',{
                                                        title : 'Error!'
                                                    });
                                        $rootScope.stopLoader();
                                        return false;
                                    }

                                    GenericService
										.serviceAction("PUT", URI,
												$scope.dishMasterObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														 $rootScope.stopLoader();
														growl
																.success(
																		'Dish Master Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Dish Master already exits...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
								 }
								 }
							}
						} ]);
