'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:keyIngredientsMasterController
 * @description #keyIngredientsMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'keyIngredientsMasterController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {

							/* init() function for form object create. */

							$scope.init = function() {

								/*$scope.unitId = 1;
								$scope.organizationId = 1;
								$scope.userId = cookieObject.userId;
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}*/
								
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId; // not in Used
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
							//	$scope.keyIngredientId = 0;
								$scope.onEditIngredient = false;
								$scope.dummyKeyIngredientMasterObj = {
									ingredientCode : "",
									ingredientDescription : "",
									ingredientValue : "",
									uomId : '',
									uomName : "",
									organizationId : $scope.organizationId,
								    unitId : $scope.unitId,
//									createdDate : moment(new Date()).format(
//											'DD-MM-YYYY HH:mm:ss'),
//									updatedDate : moment(new Date()).format(
//											'DD-MM-YYYY HH:mm:ss'),
									status : 'A',
//									createdBy : $scope.userId,
//									updatedBy : $scope.userId

								};
								
								$scope.keyIngredientMasterObj = $scope.dummyKeyIngredientMasterObj;
								
								// for nutrition List save start
								$scope.nutrationPropertyObjList = [ {
									"nutritionId" : '',
									"value" : '',
									"uomId" : '',
									"ingredientNutritionId" : ''
								} ];
								// for nutrition List save end

								var data = {};

								
								
//								$scope.showKeyIngredientDetails = function() {
								
								/*var URI = BASE_URL + ROOT_URL
										+ GETALLKEYINGREDIENTSMASTER;*/
								
								var URI = BASE_URL + ROOT_URL
								+ GETALLKEYINGREDIENTSMASTER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;

								// For Get Key Ingredient Master List Using
								// Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.keyIngredientMasterList = [];

													if (response.data.status == "success") {
														$scope.keyIngredientMasterList = response.data.listObject;
														console
																.log(
																		"keyIngredientMasterList",
																		$scope.keyIngredientMasterList);

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
								+ GETACTIVENUTRITIONALMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								// For Get Nutrition List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.nutritionList = [];

													if (response.data.status == "success") {
														$scope.nutritionList = response.data.listObject;
														console
																.log(
																		"nutritionList",
																		$scope.nutritionList);

													}
												});

							}

							$scope.init();
							$scope.showKeyIngredientDetails = function(info){
								$scope.modalNutriciansList = info.nutriciansList;
								
							}
							// Function For get single Nutrition
//							$scope.showNutritionDetails = function(id) {
//
//								var data = {
//
//										keyIngredientId : id.keyIngredientId,
//								}
//								console.log(data);
////								var URI = BASE_URL + ROOT_URL
////										+ GETKEYINGREDIENTSDETAILSBYID;
//								var URI = BASE_URL + ROOT_URL
//											+ GETINGREDIENTBYID + "?keyIngredientId="
//											+ data.keyIngredientId + "&orgId=" + + $scope.organizationId;
//											
//								GenericService
//										.serviceAction("GET", URI, data)
//										.then(
//												function(response) {
//													$scope.nutritionMasterList = [];
//													if (response.data.status == "success") {
//														console
//																.log(response.data.object);
//														$scope.nutritionMasterList = response.data.object;
//													} else {
//														alert("Error!!");
//													}
//												});
//
//							};
//
//							// Adding the nutrition details
							$scope.add = function(ind) {
								var data = {

									"nutritionId" : '',
									"value" : '',
									"uomId" : '',
									"ingredientNutritionId" : ''
								};

								$scope.nutrationPropertyObjList.push(data);
							};

							$scope.remove = function(index) {
								var newDataList = [];
								var le = $scope.nutrationPropertyObjList.length;
								var i = 1;
								if (le != i) {
									angular.forEach(
											$scope.nutrationPropertyObjList,
											function(v) {
												if (index != i) {
													newDataList.push(v);
												}
												i++;
											});

									$scope.nutrationPropertyObjList = newDataList;
								}
							};

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveKeyIngredients();
								//$scope.updateStatus();
							}

							$scope.editIngredient = function(ingredientObj) {								
								$scope.saveBtnFlag = false;//dilip
								$scope.updateBtnFlag = true; //dilip
								$scope.keyIngredientMasterObj = ingredientObj;
								$scope.keyIngredientMasterObj.keyIngredientId = ingredientObj.keyIngredientId;
								$scope.keyIngredientMasterObj.ingredientCode = ingredientObj.ingredientCode;
								$scope.keyIngredientMasterObj.ingredientDescription = ingredientObj.ingredientDescription;
								$scope.keyIngredientMasterObj.ingredientValue = ingredientObj.ingredientValue;
								$scope.keyIngredientMasterObj.organizationId = $scope.organizationId;
								$scope.keyIngredientMasterObj.unitId = $scope.unitId;
								$scope.keyIngredientMasterObj.uomId = ingredientObj.uomId;
								$scope.keyIngredientMasterObj.updatedBy = $scope.userId;	
								$scope.nutrationPropertyObjList = ingredientObj.nutriciansList;
								$scope.onEditIngredient = true
							}
							
							// Function For Save Ingrediants
							$scope.saveKeyIngredients = function() {

								 $scope.$broadcast('show-errors-check-validity');
								// return false;

								 if ($scope.keyIngredientForm.$valid) {
										var URI = BASE_URL + ROOT_URL
									+ SAVEKEYINGREDIENTSMASTER;
										if ($scope.onEditIngredient) {
											$scope.updateKeyIngredients()
										} else {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {

									$scope.keyIngredientMasterObj.nutriciansList = [];
									$scope.keyIngredientMasterObj.nutriciansList = angular
											.copy($scope.nutrationPropertyObjList);
									// console.log("abcd",$scope.keyIngredientMasterObj);
									// return false;
									// $scope.keyIngredientMasterObj.nutriciansList.push($scope.nutrationPropertyObjList);
									console.log("saasd",
											$scope.keyIngredientMasterObj);

									GenericService
											.serviceAction(
													"POST",
													URI,
													$scope.keyIngredientMasterObj)
											.then(
													function(response) {
														// alert(response.data.status);
														if (response.data.status == "success") {
															growl
																	.success(
																			'Key Ingrediants Added Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Nutrition already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
													});
									 }
										}
								}

							}

							// Function For get single Nutrition
//							$scope.showUpdateBtn = function(id) {
//
//								$scope.saveBtnFlag = false;
//								$scope.updateBtnFlag = true;
//
//								var data = {
//
//									keyIngredientId : id,
//									organizationId : $scope.organizationId
//								}
//								// console.log(data); return false;
//								var URI = BASE_URL + ROOT_URL
//										+ GETKEYINGREDIENTSMASTERBYID;
//
//								GenericService
//										.serviceAction("POST", URI, data)
//										.then(
//												function(response) {
//													// $scope.speciality =[];
//													if (response.data.status == "success") {
//														$scope.keyIngredientMasterObj.ingredientCode = response.data.object.ingredientCode;
//														$scope.keyIngredientMasterObj.ingredientDescription = response.data.object.ingredientDescription;
//														$scope.keyIngredientMasterObj.ingredientValue = response.data.object.ingredientValue;
//														$scope.keyIngredientMasterObj.uomId = response.data.object.uomId
//																.toString();
//														$scope.keyIngredientId = response.data.object.keyIngredientId;
//														console
//																.log(
//																		"nutrition list",
//																		response.data.object.nutriciansList);
//														$scope.nutrationPropertyObjList = [];
//
//														if (response.data.object.nutriciansList.length > 0) {
//															for (var i = 0; i < (response.data.object.nutriciansList.length); i++) {
//																console
//																		.log(
//																				"nutrition list",
//																				i);
//																var data = {
//
//																	"nutritionId" : response.data.object.nutriciansList[i].nutritionId
//																			.toString(),
//																	"value" : response.data.object.nutriciansList[i].value,
//																	"uomId" : response.data.object.nutriciansList[i].uomId
//																			.toString(),
//																	"ingredientNutritionId" : response.data.object.nutriciansList[i].ingredientNutritionId
//																};
//
//																$scope.nutrationPropertyObjList
//																		.push(data);
//
//															}
//														} else {
//															$scope.nutrationPropertyObjList = [ {
//																"nutritionId" : '',
//																"value" : '',
//																"uomId" : '',
//																"ingredientNutritionId" : ''
//															} ];
//														}
//														// return false;
//
//													} else {
//														alert("Error!!");
//													}
//												});
//
//							};

							// Function For update keyingredients status
							$scope.updateStatus = function(ingredient) {
								 $scope.$broadcast('show-errors-check-validity');
//								$scope.currentDate = new Date();
//								$scope.currentDate = moment($scope.currentDate)
//										.format('DD-MM-YYYY HH:mm:ss');
								 
								/* if ($scope.keyIngredientForm.$valid) {*/
								 
								var data = {
									keyIngredientId : ingredient.keyIngredientId,
									status : ingredient.status,
									updatedBy : $scope.userId,
									/*organizationId : $scope.organizationId,
									unitId : $scope.unitId,*/
									
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATEKEYINGREDIENTSMASTERSTATUS;
								GenericService
										.serviceAction("PUT", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Key Ingredients Updated Successfully...!!',
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
									
								// }
							}

							// Function For update Ingredients
							$scope.updateKeyIngredients = function() {

								$scope.keyIngredientMasterObj.nutriciansList = [];
								$scope.keyIngredientMasterObj.nutriciansList = angular
										.copy($scope.nutrationPropertyObjList);

								 $scope.$broadcast('show-errors-check-validity');

								 if ($scope.keyIngredientForm.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ UPDATEKEYINGREDIENTSMASTER;
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
								GenericService
										.serviceAction("PUT", URI,
												$scope.keyIngredientMasterObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Key Ingredient Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Key ingredient already exits...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
								 }

							}
							}
						} ]);