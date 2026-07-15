'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:consentTypeMasterController
 * @description # consentTypeMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'consentTypeMasterController',
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

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.createdBy =cookieObject.userId;
								$scope.updatedBy =cookieObject.userId;
								//$scope.userId = cookieObject.userId;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.consenttype = {};
								$scope.consenttype.admissionConsent = 'N';
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};
								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETCONSENTTYPELIST; GenericService
								 * .serviceAction("GET", URI, data) .then(
								 * function(response) { $scope.consentTypeList =
								 * [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.consentTypeList =
								 * response.data.listObject; console
								 * .log($scope.consentTypeList);
								 *  } });
								 */

							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initConsentTypeMasterList(
										$scope.organizationId,$scope.unitId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initConsentTypeMasterList = function(orgId,unitId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETCONSENTTYPELIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTCONSENTTYPEMASTER;

								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}

								// For Get Consent Type List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.consentTypeList = [];

													if (response.data.status == "success") {
														$scope.consentTypeList = response.data.listObject;

														
													}
												});

								// For Count Consent Type List
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

							$scope.getConsentTypeMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETCONSENTTYPELIST;
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Consent Type List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.consentTypeList = [];

													if (response.data.status == "success") {
														$scope.consentTypeList = response.data.listObject;

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
									$scope.getConsentTypeMasterList(
											$scope.organizationId,$scope.unitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initConsentTypeMasterList(
									$scope.organizationId,$scope.unitId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveConsentType();
							}

							// Function for save consent Type
							$scope.saveConsentType = function() {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');

								var data = {
									consentTypeMasterCode : $scope.consenttype.consentCode,
									consentTypeMasterName : $scope.consenttype.consentName,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedDate : $scope.currentDate,
									createdDate : $scope.currentDate,
									admissionConsent : $scope.consenttype.admissionConsent

								}
								if ($scope.consenttypeform.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {

										console.log(data);

										var URI = BASE_URL + ROOT_URL
												+ ADDCONSENTTYPE;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Consent type added successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl
																		.warning(
																				'Consent type already exits...!!',
																				{
																					title : 'Warning!'
																				});
															}
															$scope
																	.initConsentTypeMasterList(
																			$scope.organizationId,$scope.unitId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
														});
									}
								}

							}

							// Function for get single consent Type
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								var data = {

									consentTypeMasterId : id
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLECONSENTTYPE;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {

														$scope.consenttype.consentTypeId = response.data.listObject[0].consentTypeMasterId;
														$scope.consenttype.consentCode = response.data.listObject[0].consentTypeMasterCode;
														$scope.consenttype.consentName = response.data.listObject[0].consentTypeMasterName;
														$scope.consenttype.admissionConsent = response.data.listObject[0].admissionConsent;
													} else {
														alert("Error!!");
													}
												});

							};

							// Function for update consent Type status
							$scope.updateStatus = function(id, type) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									consentTypeMasterId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATECONSENTTYPESTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Status updated successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.warning(
																		'Something went wrong...!!',
																		{
																			title : 'Warning!'
																		});
													}
												});
							}

							// Function for update consent Type
							$scope.updateConsentType = function() {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');
								var data = {
									consentTypeMasterCode : $scope.consenttype.consentCode,
									consentTypeMasterName : $scope.consenttype.consentName,
									consentTypeMasterId : $scope.consenttype.consentTypeId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									admissionConsent : $scope.consenttype.admissionConsent
								}
								if ($scope.consenttypeform.$valid) {
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ UPDATECONSENTTYPE;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Consent type updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.warning(
																			'Consent type already exits...!!',
																			{
																				title : 'Warning!'
																			});
														}
														$scope
																.initConsentTypeMasterList(
																		$scope.organizationId,$scope.unitId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);

													});
								}
							}

						} ]);
