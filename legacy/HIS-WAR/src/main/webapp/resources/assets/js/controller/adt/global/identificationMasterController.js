'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:identificationMasterController
 * @description # identificationMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'identificationMasterController',
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

							// init function description
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for common popup

								$rootScope.loginpage = true;
								
								$scope.identification = {
										isExpire : 'N',
                                        isNumerical:'N' ,
                                        isAlphanumeric:'N',
                                        isLength:'N'

								}

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {};
								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETIDENTIFICATIONLIST; GenericService
								 * .serviceAction("GET", URI, data) .then(
								 * function(response) {
								 * $scope.identificationList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.identificationList =
								 * response.data.listObject;
								 *  } });
								 */

							};
							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initIdentificationMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initIdentificationMasterList = function(
									orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETIDENTIFICATIONLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTIDENTIFICATIONMASTER;

								var data = {
									organizationId : orgId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId
								}

								// For Get Identification List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.identificationList = [];

													if (response.data.status == "success") {
														$scope.identificationList = response.data.listObject;

													}
												});

								// For Count Identification List
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

							$scope.getIdentificationMasterList = function(
									orgId, offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETIDENTIFICATIONLIST;
								var data = {
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Identification List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.identificationList = [];

													if (response.data.status == "success") {
														$scope.identificationList = response.data.listObject;

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
									$scope.getIdentificationMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initIdentificationMasterList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveIdentification();
							}

							// Function for save identification
							$scope.saveIdentification = function() {
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');
								
								var codelength;
                          	  if($scope.identification.isLength == 'Y'){
                          		codelength =document.getElementById("length").value;
                          	  }
								
								if($scope.identification.isLength == 'Y' && codelength==""){
									growl.warning("Please enter the Length");
									$rootScope.stopLoader();
									return false;
								}
								
								if ($scope.identificationform.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {

										/*
										 * if ($scope.identification.isExpire ==
										 * true) {
										 * $scope.identification.isExpire = "Y"; }
										 * else { $scope.identification.isExpire =
										 * "N"; }
										 */

										// var data = $scope.speciality;
										var data = {
											identificationCode : $scope.identification.identificationCode,
											identificationName : $scope.identification.identificationName,
											isAlphanumeric : $scope.identification.isAlphanumeric != undefined ? $scope.identification.isAlphanumeric :'N',
											isNumerical : $scope.identification.isNumerical!= undefined ? $scope.identification.isNumerical :'N',
											definedLength : $scope.identification.definedLength,
											isExpiryRequired : $scope.identification.isExpire,
											status : 'A',
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											organizationId : $scope.organizationId,
											updatedDate : $scope.currentDate,
											createdDate : $scope.currentDate
										}
										if(data.isAlphanumeric == 'Y' && data.isNumerical == 'Y'){
											growl.warning("Please mark either Numeric or Alphanumeric.");
											$rootScope.stopLoader();
											return false;
										}
										console.log(data);
										var URI = BASE_URL + ROOT_URL
												+ ADDIDENTIFICATION;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Identification Added Successfully...!!',
																				{
																					title : 'Success!'
																				});

																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
																$rootScope.stopLoader();
															} else {
																growl
																		.error(
																				'Identification already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
															$scope
																	.initIdentificationMasterList(
																			$scope.organizationId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
															$rootScope.stopLoader();
														});
									}
								}

							};

							// function for get identification object for update
							$scope.showUpdateBtn = function(id) {
								$rootScope.startLoader();
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								var data = {
									identificationId : id
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLEIDENTIFICATION;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality = [];
													if (response.data.status == "success") {
														// $scope.speciality =
														// response.data.listObject;
														// alert(response.data.listObject[0].specialityId);
														$scope.identification.identificationId = response.data.listObject[0].identificationId;
														$scope.identification.identificationCode = response.data.listObject[0].identificationCode;
														$scope.identification.identificationName = response.data.listObject[0].identificationName;
														$scope.identification.isAlphanumeric = response.data.listObject[0].isAlphanumeric;
														$scope.identification.isNumerical = response.data.listObject[0].isNumerical;
														$scope.identification.definedLength = response.data.listObject[0].definedLength;
														if($scope.identification.definedLength !=null){
															$scope.identification.isLength = 'Y';
														}
														$scope.identification.isExpire = response.data.listObject[0].isExpiryRequired;
													} else {
														alert("Error!!");
													}
													$rootScope.stopLoader();
												});

							};

							// function for update status identification
							$scope.updateStatus = function(id, type) {
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";
								var data = {
									identificationId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATEIDENTIFICATIONSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Identification Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$rootScope.stopLoader();
													} else {
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
														$rootScope.stopLoader();
													}
												});
							}

							// function for update identification
							$scope.updateIdentification = function() {
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								
                              var length;
                            	  if($scope.identification.isLength == 'Y'){
                            		  length= document.getElementById("length").value;
                            	  }
								
								if($scope.identification.isLength == 'Y' && length==""){
									growl.warning("Please enter the Length");
									$rootScope.stopLoader();
									return false;
								}

								$scope.$broadcast('show-errors-check-validity');
								if ($scope.identificationform.$valid) {
									var data = {
										identificationId : $scope.identification.identificationId,
										identificationCode : $scope.identification.identificationCode,
										identificationName : $scope.identification.identificationName,
										isAlphanumeric : $scope.identification.isAlphanumeric != undefined ? $scope.identification.isAlphanumeric :'N',
										isNumerical : $scope.identification.isNumerical!= undefined ? $scope.identification.isNumerical :'N',
										definedLength : $scope.identification.definedLength,
										isExpiryRequired : $scope.identification.isExpire,
										status : 'A',
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										organizationId:$scope.organizationId
									}
									console.log(data);
									if(data.isAlphanumeric == 'Y' && data.isNumerical == 'Y'){
										growl.warning("Please mark either Numeric or Alphanumeric.");
										$rootScope.stopLoader();
										return false;
									}
									var URI = BASE_URL + ROOT_URL
											+ UPDATEIDENTIFICATION;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Identification Updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
															$rootScope.stopLoader();
															// alert("ds");

														} else {
															growl
																	.error(
																			'Identification already exits...!!',
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														$scope
																.initIdentificationMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
														$rootScope.stopLoader();
													});
								}
							}

						} ]);
