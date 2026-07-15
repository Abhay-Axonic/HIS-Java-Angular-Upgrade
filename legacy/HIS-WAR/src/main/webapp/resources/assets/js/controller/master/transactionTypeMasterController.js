'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:countryMasterController
 * @description #countryMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'transactionTypeMasterController',
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
						'PaginationService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService ,PaginationService) {

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

								$rootScope.loginpage = true;
								$scope.transactionType = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

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

							var data = {
										orgId : $scope.organizationId
									}
								var URI = BASE_URL + ROOT_URL + "/api/billing/getTransactionTypeMasterList";
								GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
									        $scope.pagination.data=[];
											$scope.transactionTypeList = [];
											if (response.data.status == "success") {
												$scope.transactionTypeList = response.data.listObject;
												
												 if($scope.transactionTypeList.length > 0){            
							                         PaginationService.searchData($scope.transactionTypeList, $scope.pagination);
							                       }
											}
										});
							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveTransactionType();
							}

							// Function For Save Country
							$scope.saveTransactionType = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');
								var data = {
									transactionTypeDesc : $scope.transactionType.transactionTypeDesc,
									transactionTypeName : $scope.transactionType.transactionTypeName,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									orgId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								}
								if ($scope.transactionTypeform.$valid) {

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$rootScope.startLoader();
										console.log(data);

										var URI = BASE_URL + ROOT_URL
												+ "/api/billing/saveTransactionTypeMaster";
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Transaction Type added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$rootScope.stopLoader();
																$scope.init();
															} else {
																growl
																		.error(
																				'Transaction Type already exits...!!',
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
															}

															

														});
									}
								}

							}

							// Function For get single country
							$scope.showUpdateBtn = function(item) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								$scope.transactionType.transactionTypeId =item.transactionTypeId;
								$scope.transactionType.transactionTypeDesc =item.transactionTypeDesc;
								$scope.transactionType.transactionTypeName= item.transactionTypeName;

							};

							// Function For update country status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									transactionTypeId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									orgId : $scope.organizationId,
								}
								console.log(data);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ "/api/billing/updateStatusForTransactionType";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Transaction Type Updated Successfully...!!',
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

							// Function For update country
							$scope.updateTransactionType = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
									transactionTypeDesc :$scope.transactionType.transactionTypeDesc,
									transactionTypeName : $scope.transactionType.transactionTypeName,
									transactionTypeId : $scope.transactionType.transactionTypeId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									orgId : $scope.organizationId
								}
								if ($scope.transactionTypeform.$valid) {
									console.log(data);
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ "/api/billing/updateTransactionTypeMaster";
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Transaction Type updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$rootScope.stopLoader();
															$scope.init();
															
														} else {
															growl
																	.error(
																			'Transaction Type already exits...!!',
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}

														

													});
								}

							}
						} ]);
