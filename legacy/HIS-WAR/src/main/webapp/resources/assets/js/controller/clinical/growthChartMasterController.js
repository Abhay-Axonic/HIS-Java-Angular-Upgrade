/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"growthChartMasterController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						'BillingGenericService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl, BillingGenericService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;

							/** Variables for pagination */
							
							$scope.deltaNo = true;
							$scope.deltaText = false;
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;

							/* code for setting Label */
							
						
							
							$scope.heightUnitList = [];
							$scope.weightUnitList = [];
							$scope.circumferenceUnitList = [];
							$scope.genderList = [];
							
							$scope.init = function()
							{
								$scope.growthChartMasterList = [];
								$scope.growthChartMaster = {
										  "growthChartId" : "",
										  "genderId" : "",
										  "ageInMonths" : "",
										  "ageInDays" : "",
										  "weightFrom" : "",
										  "weightTo" : "",
										  "weightUnit" : "",
										  "weightUnitId" : "",
										  "heightFrom" : "",
										  "heightTo" : "",
										  "heightUnit" : "",
										  "heightUnitString" : "",
										  "circumferenceFrom" : "",
										  "circumferenceTo" : "",
										  "circumferenceUnit" : "",
										  "circumferenceUnitString" : "",
										  "organisationId" : $scope.orgId,
										  "unitId" : $scope.orgUnitId,
										  "status" : "A",
										  "createdDate" : "",
										  "createdBy" : $scope.userId,
										  "updatedBy" : $scope.userId,
										  "updatedDate" : ""
										}
								
							}
							$scope.init();
							$scope.getHeightUnitList = function() {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											 + GET_HEIGHT_UNIT + S+
											+ $scope.orgId+S+$scope.orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.heightUnitList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getHeightUnitList();
							
							$scope.getWeightUnitList = function() {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
									 + GET_WEIGHT_UNIT + S+
									+ $scope.orgId+S+$scope.orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.weightUnitList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getWeightUnitList();

							$scope.getCircumferenceUnit = function() {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
									 + GET_CIRCUMFERENCE_UNIT + S+
									+ $scope.orgId+S+$scope.orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.circumferenceUnitList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getCircumferenceUnit();

							$scope.getGenderList = function() {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEGENDERLIST;
									BillingGenericService
											.serviceAction(METHOD_GET, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														$scope.genderList = response.data.listObject;
														console.log($scope.genderList[0])
													});

								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getGenderList();
							

					/** =====================>Code for Push Pop Objects<===============*/
				
					

							$scope.getGrowthChartList = function()
							{
								$rootScope.startLoader();
								var offset = offset != null ? offset : 0;
								var noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 100;
								var URI = BASE_URL + ROOT_URL + GROWTH_CHART_LIST + S + $scope.orgId + S +$scope.orgUnitId+S+ offset
								+ S + noOfRecordsPerPage;
								console.log("URI", URI);
								GenericService.serviceAction("GET", URI, {}).then(function(response) {
									$rootScope.stopLoader();
									$scope.growthChartMasterList = response.data.listObject;
								});
							}

							$scope.getGrowthChartList();
							
							
							$scope.deleteGrowthChart = function(growthChartId)
							{
								try {
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + DELETE_GROWTH_CHART+S+$scope.orgId+S+$scope.orgUnitId+S+growthChartId;
									console.log("ACT_INACT_URI",URI);
									GenericService.serviceAction("DELETE", URI, {}).then(function(response) 
									{
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.getGrowthChartList();
											$scope.$broadcast('show-errors-reset');
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}
							
							
							$scope.getGrowthChartById = function(growthChartId)
							{
								try {
									$rootScope.startLoader();
									$scope.updateScope = true;
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									var URI = BASE_URL + ROOT_URL + GET_GROWTH_CHART_BY_ID+S+$scope.orgId+S+$scope.orgUnitId+S+growthChartId;
									console.log("EDIT",URI);
									GenericService.serviceAction("GET", URI, {}).then(function(response) 
									{
										console.log("data",response.data);
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.growthChartMaster = response.data.object;
											
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											alert("Error In Fetching Data");
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}
							
							
							$scope.saveGrowth = function() 
							{
								$scope.$broadcast('show-errors-check-validity');
								
									try 
									{
										/*$rootScope.startLoader();*/
										
										var URI = BASE_URL + ROOT_URL + SAVE_GROWTH_CHART;
										if ($scope.growthChartMasterForm.$valid){
										GenericService.serviceAction("POST", URI, $scope.growthChartMaster).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												$scope.init();
												$scope.getGrowthChartList();
												
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
									}
									} catch (e) {
										console.log("Exception",e.message);
									}
									
								//}
							}
							$scope.updateGrowthChart = function() 
							{
								$scope.$broadcast('show-errors-check-validity');
								//if ($scope.growthChartForm.$valid)
								//{
									try {
										$rootScope.startLoader();
										$scope.growthChartMaster.createdBy = $scope.userId;
										$scope.growthChartMaster.updatedBy = $scope.userId;
										$scope.growthChartMaster.organisationId = $scope.orgId;
										$scope.growthChartMaster.unitId = $scope.orgUnitId;
										var URI = BASE_URL + ROOT_URL +UPDATE_GROWTH_CHART;
										if ($scope.growthChartMasterForm.$valid){
										GenericService.serviceAction("PUT", URI, $scope.growthChartMaster).then(function(response) 
										{
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												$scope.init();
												$scope.getGrowthChartList();
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
										}
									} catch (e) 
									{
										console.log("Exception",e.message);
									}
									
								//}
							}
							

						} ]);