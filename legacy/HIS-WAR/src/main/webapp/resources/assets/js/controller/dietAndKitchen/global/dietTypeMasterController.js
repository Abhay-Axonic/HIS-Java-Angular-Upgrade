'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:nutritionMasterController
 * @description #nutritionMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'dietTypeMasterController',
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
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditDietType = false;

								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.dietTypeId = 0;
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;

								$scope.dummyDietTypeMasterObj = {
									dietTypeCode : "",
									dietTypeDescription : "",
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									status : 'A',
								};
								
								$scope.dietTypeMasterObj = $scope.dummyDietTypeMasterObj;
								
								var data = {};
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETALLDIETTYPEMASTERLIST;*/

								var URI = BASE_URL + ROOT_URL
										+ GETALLDIETTYPEMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;

								// For Get Diet List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.dietList = [];
													if (response.data.status == "success") {
														$scope.dietList = response.data.listObject;
														console.log("dietList",$scope.dietList);
													}
												});
							}

							$scope.init();
							
							

							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var data1 = {
									      organizationId:organizationId,
											offset:offset,
											noOfRecordsPerPage:noOfRecordsPerPage,
											unitId:unitId
											};
								
								var data2 = {
										      organizationId:organizationId,
										      unitId:unitId
											};
								var URI1 = BASE_URL + ROOT_URL + "/api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList"; 
								var URI2 = BASE_URL + ROOT_URL + "/api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationListCount";
									
								
								GenericService.serviceAction("POST",URI1,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.dietList = response.data.listObject;
									console.log("commonList",$scope.commonList);
									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){					
										$rootScope.stopLoader();
										$scope.commonListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								console.log("offset", offset);
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var data = {
										organizationId:organizationId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										unitId:unitId
										};
								
								var URI =  BASE_URL + ROOT_URL + "/api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									$rootScope.stopLoader();
									$scope.dietList = response.data.listObject;
									console.log("commonList",$scope.commonList);
								});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveDiet();
							}

							// Function for editing the Diet master entries
							$scope.editDietType = function(dietObj) {
								$scope.dietTypeMasterObj = dietObj;
								$scope.dietTypeMasterObj.dietTypeId = dietObj.dietTypeId;
								$scope.dietTypeMasterObj.dietTypeCode = dietObj.dietTypeCode;
								$scope.dietTypeMasterObj.dietTypeDescription = dietObj.dietTypeDescription;
								
								$scope.dietTypeMasterObj.updatedBy = $scope.userId;
				
								$scope.onEditDietType = true
							}

							// Function For Save Nutrition
							$scope.saveDiet = function() {

								$scope.$broadcast('show-errors-check-validity');
								// return false;

								if ($scope.diet_type_form.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ DIETTYPEMASTER;
									if ($scope.onEditDietType) {
										$scope.updateDiet()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;

										} else {

											// console.log($scope.dietTypeMasterObj);
											// return false;
											GenericService
													.serviceAction(
															"POST",
															URI,
															$scope.dietTypeMasterObj)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	growl
																			.success(
																					'Diet Added Successfully...!!',
																					{
																						title : 'Success!'
																					});
																	$scope
																			.$broadcast('show-errors-reset');
																	$scope
																			.init();
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

					
							// Function For update Diet status
							$scope.updateStatus = function(dietType) {

								var data = {
										dietTypeId : dietType.dietTypeId,
										status : dietType.status,
										updatedBy : $scope.userId,
										
									}
								var URI = BASE_URL + ROOT_URL + UPDATEDIETMASTERSTATUS;
									
								GenericService
										.serviceAction("PUT", URI,data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Diet Updated Successfully...!!',
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

							// Function For update Diet Master
							$scope.updateDiet = function() {

								$scope.$broadcast('show-errors-check-validity');

								if ($scope.diet_type_form.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ DIETTYPEMASTER;
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
									GenericService
											.serviceAction("PUT", URI,
													$scope.dietTypeMasterObj)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Diet updated successfully...!!',
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
						} ]);
