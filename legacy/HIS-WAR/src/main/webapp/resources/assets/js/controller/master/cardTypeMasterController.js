'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:countryMasterController
 * @description #countryMasterController Controller of the myApp
 */
(function () {
	  "use strict";
						function cardTypeMasterController($scope, $http, $localStorage, $sessionStorage,
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

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.cardType = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

							var data = {
										orgnisationId : $scope.organizationId
									}
								var URI = BASE_URL + ROOT_URL + "/api/billing/getCardTypeMasterList";
								GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
									        $scope.pagination.data=[];
											$scope.cardTypeList = [];
											if (response.data.status == "success") {
												$scope.cardTypeList = response.data.listObject;
												
												 if($scope.cardTypeList.length > 0){            
							                         PaginationService.searchData($scope.cardTypeList, $scope.pagination);
							                       }
							                	 
											}
										});
							}

							$scope.init();

							
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveCardType();
							}

							// Function For Save Country
							$scope.saveCardType = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');
								var data = {
									cardTypeCode : $scope.cardType.cardTypeCode,
									cardTypeName : $scope.cardType.cardTypeName,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									orgnisationId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								}
								if ($scope.cardTypeform.$valid) {

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$rootScope.startLoader();
										console.log(data);

										var URI = BASE_URL + ROOT_URL
												+ "/api/billing/saveCardTypeMaster";
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Card Type added Successfully...!!',
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
																				'Card Type already exits...!!',
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
								
								$scope.cardType.cardTypeId =item.cardTypeId;
								$scope.cardType.cardTypeCode =item.cardTypeCode;
								$scope.cardType.cardTypeName= item.cardTypeName;

							};

							// Function For update country status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									cardTypeId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									orgnisationId : $scope.organizationId,
								}
								console.log(data);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ "/api/billing/updateStatusForCardType";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Card Type Updated Successfully...!!',
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
							$scope.updateCardType = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
									cardTypeCode :$scope.cardType.cardTypeCode,
									cardTypeName : $scope.cardType.cardTypeName,
									cardTypeId : $scope.cardType.cardTypeId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									orgnisationId : $scope.organizationId
								}
								if ($scope.cardTypeform.$valid) {
									console.log(data);
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ "/api/billing/updateCardTypeMaster";
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Card Type updated Successfully...!!',
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
																			'Card Type already exits...!!',
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}

														

													});
								}

							}
						} 
						function config($stateProvider) {
						    $stateProvider.state('cardTypeMaster', {
								 url: '/cardTypeMaster',
								 templateUrl: 'views/master/GlobalMaster/card-type-master.html',
								 controller: "cardTypeMasterController",
								 title: 'Card Type Master',
								})
						  }

						  angular
						    .module("myApp")
						    .config(config)
						    .controller("cardTypeMasterController", cardTypeMasterController);
						})();
