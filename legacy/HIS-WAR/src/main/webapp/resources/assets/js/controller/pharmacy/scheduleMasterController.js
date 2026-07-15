/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"scheduleMasterController",
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
						'GenericServiceFileUpload',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl,
								GenericServiceFileUpload, reportService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Schedule Description ";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/** * Check user is Loggedin */
							var cookieObject, cookieObjectAppConfig;
							[ cookieObject, cookieObjectAppConfig ] = $scope
									.checkCookies();
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;

							/* code for setting Label */
							$(".selectedPageName").text("Schedule Master");
							$state.current.title="Schedule Master";
							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.common = {
									"id" : "",
									"code" : "",
									"desc" : "",
									"status" : "A",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId
								}
							}
							$scope.initCommon();

							$scope.initScheduleMaster = function() {
								$scope.scheduleMaster = {
									"scheduleId" : $scope.common.id,
									"scheduleCode" : $scope.common.code,
									"scheduleDescription" : $scope.common.desc,
									"status" : $scope.common.status,
									"createdBy" : $scope.common.createdBy,
									"createdDate" : $scope.common.createdDate,
									"updatedBy" : $scope.common.updatedBy,
									"updatedDate" : $scope.common.updatedDate,
									"orgId" : $scope.common.orgId,
									"unitId" : $scope.common.unitId,
									"colorCode":$scope.common.colorCode
								}
							}

							/* START FORFOR SEROLOGY MASTER */

							$scope.setNoOfRecords = function() {
								$scope.initScheduleMasterList($scope.orgId,
										$scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initScheduleMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI1 = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
										+ LIST_SCHEDULE_MASTER + S + orgId + S
										+ $scope.unitId + S + offset + S
										+ noOfRecordsPerPage;
								var URI2 = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
										+ COUNT_SCHEDULE_MASTER + S + orgId + S
										+ $scope.unitId;
								promiseFactory
										.setPromises(URI1, URI2, "GET", "GET")
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.commonList = response[0].listObject;
													$scope.commonListCount = response[1].object;
													$scope.setPage(1, false);
													console.log($scope.commonList);
													
												});
							}

							$scope.getScheduleMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
										+ LIST_SCHEDULE_MASTER + S + orgId + S
										+ $scope.unitId + S + offset + S
										+ noOfRecordsPerPage;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
													$rootScope.stopLoader();
													$scope.commonList = response.data.listObject;
													console.info("LIST "+ $scope.commonList)
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getScheduleMasterList($scope.orgId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initScheduleMasterList($scope.orgId,
									$scope.offset, $scope.noOfRecordsPerPage);
							// END FOR SEROLOGY MASTER

							/* code for Activating Inactivating Status */
							$scope.updateStatus = function(commonId, status) {
								try {
									$rootScope.startLoader();
									$scope.initScheduleMaster();
									var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
											+ ACTIVE_INACTIVE_SCHEDULE_MASTER
											+ S + $scope.orgId + S + commonId
											+ S + status;
									console.log("ACT_INACT_URI", URI);
									console.info($scope.scheduleMaster)
									GenericService
											.serviceAction("GET", URI,
													$scope.scheduleMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															growl.success("Status Update successfully",
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
															$scope.initCommon();
														} else if (response.data.status == 'error') {
															$rootScope
																	.stopLoader();
															growl
																	.error(response.data.message,
																			{
																				title : 'Error!'
																			});
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}

							}

							$scope.showUpdateBtn = function(commonId) {
								try {
									$rootScope.startLoader();
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									var URI = BASE_URL + ROOT_URL + PHARMACY_GLOBAL
											+ EDIT_SCHEDULE_MASTER + S
											+ $scope.orgId + S + $scope.unitId
											+ S + commonId;
									console.log("EDIT", URI);
									GenericService
											.serviceAction("GET", URI,
													$scope.common)
											.then(
													function(response) {
														console.log("data",
																response.data);
														if (response.data.status == 'success') {
															$rootScope
																	.stopLoader();
															$scope.common = response.data.object;
															console
																	.log("$scope.common",$scope.common);
														} else if (response.data.status == 'error') {
															$rootScope
																	.stopLoader();
															alert("Error In Fetching Data");
														}
													});
								} catch (e) {
									console.log("Exception", e.message);
								}

							}

							$scope.save = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.commonform.$valid) {
									try {
										$rootScope.startLoader();
										$scope.initScheduleMaster();
										console
												.log("schedule Master",
														JSON.stringify($scope.scheduleMaster));
										var URI = BASE_URL + ROOT_URL
												+ PHARMACY_GLOBAL
												+ ADD_SCHEDULE_MASTER;
										GenericService
												.serviceAction("POST", URI,
														$scope.scheduleMaster)
												.then(
														function(response) {
															console.info(response.data.status)
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																growl.success("schedule Added Successfully",
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.common='';
																$scope.initCommon();
																$scope.initScheduleMasterList(
																				$scope.orgId,
																				$scope.offset,
																				$scope.noOfRecordsPerPage);
															} else if (response.data.status == 'error') {
																$rootScope
																		.stopLoader();
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
															}

														});
									} catch (e) {
										console.log("Exception", e.message);
									}

								}
							}
							$scope.update = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.commonform.$valid) {
									try {
										$rootScope.startLoader();
										$scope.initScheduleMaster();
										console.log("HeaderMaster",JSON.stringify($scope.scheduleMaster));
										var URI = BASE_URL + ROOT_URL
												+ PHARMACY_GLOBAL
												+ UPDATE_SCHEDULE_MASTER;
										GenericService
												.serviceAction("PUT", URI,
														$scope.scheduleMaster)
												.then(
														function(response) {
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																growl.success("Schedule Update Successfully"	,
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.initCommon();
																$scope.initScheduleMasterList(
																				$scope.orgId,
																				$scope.offset,
																				$scope.noOfRecordsPerPage);
															} else if (response.data.status == 'error') {
																$rootScope.stopLoader();
																growl.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
															}
														});
									} catch (e) {
										console.log("Exception", e.message);
									}

								}
							}

						} ]);