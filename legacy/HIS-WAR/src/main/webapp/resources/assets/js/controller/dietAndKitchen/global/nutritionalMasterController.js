'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:nutritionMasterController
 * @description #nutritionMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nutritionalMasterController',
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
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId; // not in used 
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditNutrition = false;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								//$scope.updateStatusBtnFlag = true;

								// for save update popup 
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								
								
								//for update status popup
								$scope.showUpdateStatusModal = false;
								$scope.updateStatusPopUpFlag = true;
								
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								
								// end for popup
								$scope.nutritionId = 0;
								$scope.dummyNutritionalObj = {
										nutritionCode : "",
										nutritionName : "",
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										createdDate : moment(new Date()).format(
												'DD-MM-YYYY HH:mm:ss'),
										updatedDate : moment(new Date()).format(
												'DD-MM-YYYY HH:mm:ss'),
										status : 'A',
										createdBy : $scope.userId,
										updatedBy : $scope.userId

									};

								$scope.nutritionalMasterObj = $scope.dummyNutritionalObj;

								var data = {};

								/*var URI = BASE_URL + ROOT_URL
								+ GETALLNUTRITIONALMASTERLIST;*/
								/*var URI = BASE_URL + ROOT_URL
										+ GETALLNUTRITIONALMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
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
												});*/

							}

							$scope.init();

							// function for popup ok button call
							/*$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								if( $scope.updateStatusForNutrition ){
									$scope.updateStatus();
								} else {
									$scope.saveNutrition();
								}
							}*/
							
							// function for save popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								
									$scope.saveNutrition();
								
							}
							
							
							// function for update status popup ok button call
							$scope.DietCssdPopupOkBtn = function() {
								$scope.updateStatusPopUpFlag = false;
								
									$scope.updateStatus();
								
							}
							
							

							//Function for editing the entries
							$scope.editNutrition = function(nutritionObj) {
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.nutritionalMasterObj = nutritionObj;
								$scope.nutritionalMasterObj.nutritionId = nutritionObj.nutritionId;
								$scope.nutritionalMasterObj.updatedBy = $scope.userId;
								$scope.nutritionalMasterObj.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								$scope.onEditNutrition = true
							}
							
							// Function For Save Nutrition
							$scope.saveNutrition = function() {
								$scope.$broadcast('show-errors-check-validity');

								if ($scope.nutritionform.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ NUTRITIONALMASTER;
									if ($scope.onEditNutrition) {
										$scope.updateNutrition()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											GenericService.serviceAction("POST",URI,$scope.nutritionalMasterObj)
												.then(function(response) {
															if (response.data.status == "success") {
																growl.success(
																				'Nutrition Added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
																
															} else {
																growl
																		.error(
																				'Nutrition already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
															$scope.initNutritionMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
														});
										}
									}
								}

							}
                            
							
							$scope.updateStatusForNutrition = false;

							$scope.setUpdateStatusFlag = function() {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								}
								$scope.updateStatusForNutrition = true;
							}
							
							// Function For update nutrition status
							$scope.updateStatus = function(nutrition) {
								/*$scope.updateStatusForNutrition = false;*/
								var data = {
										nutritionId : nutrition.nutritionId,
										status : nutrition.status,
										updatedBy : $scope.userId,
										//organizationId : $scope.organizationId,
										//unitId : $scope.unitId
										
									}
								
								//if ($scope.nutritionform.$valid){	
								 var URI = BASE_URL + ROOT_URL
										+ NUTRIONALMASTERSTATUS;
								
								/*if ($scope.updateStatusPopUpFlag) {
									$scope.showUpdateStatusModal = !$scope.showUpdateStatusModal;

								}
								else {*/ 
								GenericService
										.serviceAction("PUT", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Nutrition Updated Successfully...!!',
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
								//}
							}

							// Function For update Nutrition
							$scope.updateNutrition = function() {

								$scope.$broadcast('show-errors-check-validity');
								if ($scope.nutritionform.$valid) {
								
									var URI = BASE_URL + ROOT_URL
											+ NUTRITIONALMASTER;
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
									GenericService
											.serviceAction("PUT", URI,
													$scope.nutritionalMasterObj)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl.success('Nutrition Updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
															$scope.init();
															
														} else {
															growl.error('Nutrition already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
														$scope.initNutritionMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
													});
								}
								}

							}
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initNutritionMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initNutritionMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + GETALLNUTRITIONALMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&offset="+ offset + "&noOfRecordsPerPage=" +$scope.noOfRecordsPerPage;  //dilip
								var URI1 = BASE_URL + ROOT_URL + "/api/kitchen/master/nutritionalMaster/getNutritionMasterCount";


								var data1 = {
									organizationId : $scope.organizationId
								}

								// For Get Country List Using Offset
								GenericService.serviceAction("GET", URI, "")
										.then(function(response) {
													$scope.nutritionList = [];
													if (response.data.status == "success") {
														$scope.nutritionList = response.data.listObject;
														console.log("nutritionList",$scope.nutritionList);
													}
												});

								// For Count Country List
								GenericService.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);

													}
												});
							}

							$scope.getNutritionMasterList = function(orgId,offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL +  GETALLNUTRITIONALMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&offset="+ offset + "&noOfRecordsPerPage=" +$scope.noOfRecordsPerPage; //dilip								

								GenericService.serviceAction("GET", URI, "")
										.then(function(response) {
													$scope.nutritionList = [];

													if (response.data.status == "success") {
														$scope.nutritionList = response.data.listObject;

													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getNutritionMasterList($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize);
								}
							}

							$scope.initNutritionMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End
						} ]);
