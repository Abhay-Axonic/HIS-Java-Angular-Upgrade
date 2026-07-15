/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"serologyTestFindingController",
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
							$scope.LISDynamicLabel = "Serology ";
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
							$(".selectedPageName").text("Serology Test Finding Master");
							$state.current.title="Serology Test Finding Master";
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

							$scope.initSerologyMaster = function() {
								$scope.serologyMaster = {
									"serologyId" : $scope.common.id,
									"serologyCode" : $scope.common.code,
									"serologyTest" : $scope.common.desc,
									"status" : $scope.common.status,
									"createdBy" : $scope.common.createdBy,
									"createdDate" : $scope.common.createdDate,
									"updatedBy" : $scope.common.updatedBy,
									"updatedDate" : $scope.common.updatedDate,
									"orgId" : $scope.common.orgId,
									"unitId" : $scope.common.unitId
								}
							}

							/* START FORFOR SEROLOGY MASTER */

							$scope.setNoOfRecords = function() {
								$scope.initSerologyMasterList($scope.orgId,
										$scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initSerologyMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI1 = BASE_URL + ROOT_URL + LIS_GLOBAL
										+ LIST_SEROLOGY_MASTER + S + orgId + S
										+ $scope.unitId + S + offset + S
										+ noOfRecordsPerPage;
								var URI2 = BASE_URL + ROOT_URL + LIS_GLOBAL
										+ COUNT_SEROLOGY_MASTER + S + orgId + S
										+ $scope.unitId;
								promiseFactory
										.setPromises(URI1, URI2, "GET", "GET")
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.commonList = response[0].listObject;
													$scope.commonListCount = response[1].object;
													$scope.setPage(1, false);
													console
															.log($scope.commonList);
													console
															.log(response[0].listObject.serologyCode)
												});
							}

							$scope.getSerologyMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL
										+ LIST_SEROLOGY_MASTER + S + orgId + S
										+ $scope.unitId + S + offset + S
										+ noOfRecordsPerPage;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.commonList = response.data.listObject;
													console
															.info("LIST "
																	+ $scope.commonList)
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
									$scope.getSerologyMasterList($scope.orgId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initSerologyMasterList($scope.orgId,
									$scope.offset, $scope.noOfRecordsPerPage);
							// END FOR SEROLOGY MASTER

							/* code for Activating Inactivating Status */
							$scope.updateStatus = function(commonId, status) {
								try {
									$rootScope.startLoader();
									$scope.initSerologyMaster();
									var URI = BASE_URL + ROOT_URL + LIS_GLOBAL
											+ ACTIVE_INACTIVE_SEROLOGY_MASTER
											+ S + $scope.orgId + S + commonId
											+ S + status;
									console.log("ACT_INACT_URI", URI);
									console.info($scope.serologyMaster)
									GenericService
											.serviceAction("GET", URI,
													$scope.serologyMaster)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope
																	.stopLoader();
															growl
																	.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.initCommon();
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

							$scope.showUpdateBtn = function(commonId) {
								try {
									$rootScope.startLoader();
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									var URI = BASE_URL + ROOT_URL + LIS_GLOBAL
											+ EDIT_SEROLOGY_MASTER + S
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
															if(response.data.object.status == "I"){
																growl.warning("This entry is deactivated please active first then update.",
																		{
																			title : 'warning!'
																		});
																$scope.saveBtnFlag = true;
																$scope.updateBtnFlag = false;
																return false;
															}else{
															$scope.common = response.data.object;
															console
																	.log(
																			"$scope.common",
																			$scope.common);
															}
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
										$scope.initSerologyMaster();
										console
												.log(
														"Serology Master",
														JSON
																.stringify($scope.serologyMaster));
										var URI = BASE_URL + ROOT_URL
												+ LIS_GLOBAL
												+ ADD_SEROLOGY_MASTER;
										GenericService
												.serviceAction("POST", URI,
														$scope.serologyMaster)
												.then(
														function(response) {
															if (response.data.status == 'success') {
																$rootScope
																		.stopLoader();
																growl
																		.success(
																				response.data.message,
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope
																		.initCommon();
																$scope
																		.initSerologyMasterList(
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
										$scope.initSerologyMaster();
										console
												.log(
														"HeaderMaster",
														JSON
																.stringify($scope.serologyMaster));
										var URI = BASE_URL + ROOT_URL
												+ LIS_GLOBAL
												+ UPDATE_SEROLOGY_MASTER;
										GenericService
												.serviceAction("PUT", URI,
														$scope.serologyMaster)
												.then(
														function(response) {
															if (response.data.status == 'success') {
																$rootScope
																		.stopLoader();
																growl
																		.success(
																				response.data.message,
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope
																		.initCommon();
																$scope
																		.initSerologyMasterList(
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

						} ]);