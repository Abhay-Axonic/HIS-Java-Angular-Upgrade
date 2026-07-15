'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:stateMasterController
 * @description #stateMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'stateMasterController',
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

							
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							
							/* init() function for form object create. */

							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.state = {}
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for common popup

								var data = {};

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.countryList = [];

													if (response.data.status == "success") {
														$scope.countryList = response.data.listObject;
														console
																.log($scope.countryList);

													}
												});

								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL + GETSTATELIST;
								 * GenericService .serviceAction("GET", URI,
								 * data) .then( function(response) {
								 * $scope.stateList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.stateList = response.data.listObject;
								 * console .log($scope.stateList);
								 *  } });
								 */
							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveState();
							}

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initStateMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initStateMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + GETSTATELIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTSTATEMASTER;

								var data = {
									organizationId : orgId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId
								}

								// For Get State List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.stateList = [];

													if (response.data.status == "success") {
														$scope.stateList = response.data.listObject;

													}
												});

								// For Count State List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope
																.setPage(1,
																		false);

													}
												});
							}

							$scope.getStateMasterList = function(orgId, offset,
									noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + GETSTATELIST;
								var data = {
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get State List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.stateList = [];

													if (response.data.status == "success") {
														$scope.stateList = response.data.listObject;

													}
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
									$scope.getStateMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initStateMasterList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End

							// Function for save state
							$scope.saveState = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
									stateCode : $scope.state.stateCode,
									stateName : $scope.state.stateName,
									countryId : $scope.state.countryId,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								}
								if ($scope.stateform.$valid) {
									console.log(data);
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										var URI = BASE_URL + ROOT_URL
												+ ADDSTATE;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'State added successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl
																		.error(
																				'State already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
															$scope
																	.initStateMasterList(
																			$scope.organizationId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);

														});
									}
								}

							}

							// Function for get single state
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								var data = {

									stateId : id,
									organizationId : $scope.organizationId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL + GETSINGLESTATE;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {
														$scope.state.stateId = response.data.listObject[0].stateId;
														$scope.state.stateCode = response.data.listObject[0].stateCode;
														$scope.state.stateName = response.data.listObject[0].stateName;
														$scope.state.countryId = response.data.listObject[0].countryId.toString();
													} else {
														alert("Error!!");
													}
												});

							};

							// Function for update state status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";
								var data = {
									stateId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId : $scope.organizationId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATESTATESTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'State Updated successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.initStateMasterList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
													} else {
														growl.warning( response.data.message,
																{
																	title : 'Warning!'
																});
														$scope.initStateMasterList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
													}
												});
							}

							
							  function testInput(event) {
						    	   var value = String.fromCharCode(event.which);
						    	   var pattern = new RegExp(/[a-zåäö ]/i);
						    	   return pattern.test(value);
						    	}

						    	$('#statename-field').bind('keypress', testInput);
							// Function for update state
							$scope.updateState = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');

								var data = {
									stateCode : $scope.state.stateCode,
									stateName : $scope.state.stateName,
									countryId : $scope.state.countryId,
									stateId : $scope.state.stateId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId : $scope.organizationId
								}
								if ($scope.stateform.$valid) {
									console.log(data);
									var URI = BASE_URL + ROOT_URL + UPDATESTATE;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'State updated successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'State already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
														$scope
																.initStateMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);

													});
								}

							}
						} ]);
