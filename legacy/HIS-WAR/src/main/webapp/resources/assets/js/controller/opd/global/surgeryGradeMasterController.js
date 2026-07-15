/**
 * surgeryGradeMasterController.js
 */
'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:surgeryGradeMasterController
 * @description #surgeryGradeMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'surgeryGradeMasterController',
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
						'BillingGenericService',
						'PaginationService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,BillingGenericService,PaginationService) {

							/* init() function for form object create. */
							$scope.init = function() {

								/*
								 * $scope.unitId =
								 * sessionStorage.getItem("unitId");
								 * $scope.organizationId =
								 * sessionStorage.getItem("organizationId");
								 * $scope.userId =
								 * sessionStorage.getItem("userId");
								 */
								
								
								/*** Check user is Loggedin */
								var cookieObject,cookieObjectAppConfig;
								[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								
								$rootScope.loginpage = true;
								$scope.nationality = {}
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								
								 $scope.pagination = {
						                    currentPage: 1,
						                    totalPages: 0,
						                    pageSize: '10',
						                    search: '',
						                    searchData: PaginationService.searchData,
						                    setTotalPages: PaginationService.setTotalPages,
						                    getPagesList: PaginationService.getPagesList,
						                    setPage: PaginationService.setPage,
						                    setFirstPage: PaginationService.setFirstPage,
						                    setPreviousPage: PaginationService.setPreviousPage,
						                    setNextPage: PaginationService.setNextPage,
						                    setLastPage: PaginationService.setLastPage,
						                    sortBy: PaginationService.sortBy
						                  }

								 
								 var data = {};
								
								var commonObj = {
										orgnisationId : $scope.organizationId
									}

									var URI = BASE_URL + ROOT_URL
											+ GETSURGERYGRADEMASTERLIST;
								
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													commonObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														if (response.data.status == "success")
															$scope.pagination.data=[];
															$scope.surgeryGradeList = response.data.listObject;
															
															 if($scope.surgeryGradeList.length > 0){            
										                         PaginationService.searchData($scope.surgeryGradeList, $scope.pagination);
										                       }
													});									
									
									$scope.surgeryGradeObj = {
											surgeryGrade:'',
											surgeryGradeCode:'',
											surgeryGradeId:''
								
									}

							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveSurgeryGrade();
							}

							// Function For save surgery grade
							$scope.saveSurgeryGrade = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								$rootScope.startLoader();
								var data = {
									surgeryGradeCode : $scope.surgeryGradeObj.surgeryGradeCode,
									surgeryGrade : $scope.surgeryGradeObj.surgeryGrade,							
									unitId : $scope.unitId,
									orgnisationId : $scope.organizationId,
									status : 'A',
									createdDate : $scope.currentDate,
									createdBy : $scope.userId,
									updatedDate : $scope.currentDate,
									updatedBy : $scope.userId,
								}
								console.log("surgery Grade",data); 
//								return false;
								if ($scope.surgeryGradeform.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										var URI = BASE_URL + ROOT_URL
												+ SAVESURGERYGRADEMASTER;
										GenericService
												.serviceAction("POST", URI,data)
												.then(function(response) {

															if (response.data.status == "success") {
																growl.success('Surgery grade added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
																$rootScope.stopLoader();
															} else {
																growl.error('Surgery grade already exits...!!',
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
															     }
														});
									}
								}

							}

							// Function For get single surgery grade
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$rootScope.startLoader();
								var data = {
										surgeryGradeId : id,
										orgnisationId:$scope.organizationId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSURGERYGRADEBYID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {

														$scope.surgeryGradeObj.surgeryGradeId = response.data.listObject[0].surgeryGradeId;
														$scope.surgeryGradeObj.surgeryGradeCode = response.data.listObject[0].surgeryGradeCode;
														$scope.surgeryGradeObj.surgeryGrade = response.data.listObject[0].surgeryGrade;
														$rootScope.stopLoader();
													} else {
														alert("Error!!");
														$rootScope.stoptLoader();
													}
												});

							};

							// Function For update surgery grade status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";
								$rootScope.startLoader();
								var data = {
									surgeryGradeId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATESTATUSFORSURGERYGRADE;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl.success('Surgery grade Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$rootScope.stopLoader();
													} else {
														growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
														$rootScope.stopLoader();
													}
												});
							}

							// Function For update surgery grade 
							$scope.updateSurgeryGrade = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
									surgeryGradeCode : $scope.surgeryGradeObj.surgeryGradeCode,
									surgeryGrade : $scope.surgeryGradeObj.surgeryGrade,			
									surgeryGradeId : $scope.surgeryGradeObj.surgeryGradeId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								if ($scope.surgeryGradeform.$valid) {
									console.log(data);
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ UPDATESURGERYGRADEMASTER;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															growl.success('Surgery grade updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
															$scope.init();
															$rootScope.stopLoader();
														} else {
															growl.error('Surgery grade already exits...!!',
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														
													});
								}
							}

				} ]);
