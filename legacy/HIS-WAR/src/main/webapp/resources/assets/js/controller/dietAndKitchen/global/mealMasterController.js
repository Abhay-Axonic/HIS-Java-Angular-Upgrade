angular
		.module('myApp')
		.controller(
				'mealMasterController',
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

							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId; //not in use
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditMeal = false;
								
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
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								// end for popup
							//	$scope.mealMasterId = 0;
								
								$scope.dummyMealMasterObj = {
										code : "",
										name : "",
										servingTime: "",
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										status : 'A',
									// servingTime = moment(servingTime, "HHmm").format("HH:mm"),
									
									};

								$scope.mealMasterObj = $scope.dummyMealMasterObj;

								var data = {};

								/*var URI = BASE_URL + ROOT_URL
										+ GETALLMEALMASTER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;

								// For Get Nutrition List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.mealMasterList = [];

													if (response.data.status == "success") {
														$scope.mealMasterList = response.data.listObject;
														console.log("mealMasterList",$scope.mealMasterList);

													}
												});*/

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
											var URI1 = BASE_URL + ROOT_URL + "/api/kitchen/master/mealMaster/getAllMealMasterListPaginationList"; 
								var URI2 = BASE_URL + ROOT_URL + "/api/kitchen/master/mealMaster/getAllMealMasterListPaginationListCount";
									
								
								GenericService.serviceAction("POST",URI1,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.mealMasterList = response.data.listObject;
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
								
								var URI = BASE_URL + ROOT_URL +  "/api/kitchen/master/mealMaster/getAllMealMasterListPaginationList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									$rootScope.stopLoader();
									$scope.mealMasterList = response.data.listObject;
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
								$scope.saveMealMaster();
							}
							
							$scope.editMealMaster = function(mealObj) {
								
								$scope.saveBtnFlag = false;//added by minesh
								$scope.updateBtnFlag = true;//added by minesh
								
								$scope.mealMasterObj = mealObj;
								$scope.mealMasterObj.id = mealObj.id;
								$scope.mealMasterObj.code = mealObj.code;
								$scope.mealMasterObj.name = mealObj.name;
								$scope.mealMasterObj.servingTime = mealObj.servingTime;
								$scope.mealMasterObj.updatedBy = $scope.userId;
								$scope.mealMasterObj.organizationId = $scope.organizationId;
				
								$scope.onEditMeal = true;
							}
							
							
							// Function For Save Meal MAster
							$scope.saveMealMaster = function() {
								$scope.$broadcast('show-errors-check-validity');

								if ($scope.mealMasterForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ MEALMASTER;
									if ($scope.onEditMeal) {
										$scope.updateMeal()
								} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
									} else {
											GenericService
												.serviceAction(
														"POST",
														URI,
														$scope.mealMasterObj)
												.then(
														function(response) {
															if (response.data.status == "success") {
																growl
																		.success(
																				'Meal added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
																$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
															} else {
																growl
																		.error(
																				'Meal already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
														});
										}
									}
								}

						}

							$scope.updateStatus = function(mealMaster) {

								var data = {
										mealId : mealMaster.mealId,
										status : mealMaster.status,
										updatedBy : $scope.userId,
										
									}
								var URI = BASE_URL + ROOT_URL + UPDATEMEALMASTERSTATUS;
									
								GenericService
										.serviceAction("PUT", URI,data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Meal Updated Successfully...!!',
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

							// Function For update Nutrition
							$scope.updateMeal = function() {

								$scope.$broadcast('show-errors-check-validity');

								if ($scope.mealMasterForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ MEALMASTER;
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
									GenericService
											.serviceAction("PUT", URI,
													$scope.mealMasterObj)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Diet updated Successfully...!!',
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

						}])
						

