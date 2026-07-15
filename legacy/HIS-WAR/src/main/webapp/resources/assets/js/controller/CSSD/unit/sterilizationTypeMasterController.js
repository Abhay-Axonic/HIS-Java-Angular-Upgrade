'use strict';

angular
		.module('myApp')
		.controller(
				'sterilizationTypeMasterController',
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

							/**Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
								
								/******For initial cssd store selection*********/
								
								var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								if (cssdStoreObj != undefined) {
									$scope.storeId = cssdStoreObj.storeId;
								}
								
								/***********************************************/
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditSterilization = false;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.dummySterilizationMasterObj = {
										code : "",
										name : "",
										
										//status : 'A',
										created_by : $scope.userId,
										updated_by : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										storeId :  $scope.storeId

									};

								$scope.sterilizationMasterObj = $scope.dummySterilizationMasterObj;
								
							}

							$scope.init();
							
							$scope.getSterializationTypelist=function(orgId,offset, noOfRecordsPerPage){
								$rootScope.startLoader();
								var offset1 = offset != null ? offset : 0;
								var perPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								
								var data2 = {
										organizationId:orgId
										};
								
								var URI2 = BASE_URL + ROOT_URL + "/cssd/getsterilizationtypeslistcount";
								var data = {};
								
								var URI = BASE_URL + ROOT_URL
										+ GETSTERILIZATION + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId +"&offset="+offset1+"&noOfRecordsPerPage="+perPage;

								// For Get Sterilization List
								GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
									$scope.sterilizationList = [];

									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.sterilizationList = response.data.listObject;
										console.log("sterilizationList",$scope.sterilizationList);

									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){	
										$scope.sterilizationListCount={};
										$rootScope.stopLoader();
										$scope.sterilizationListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
							}
							
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.getSterializationTypelist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getSterializationTypelist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.sterilizationListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getSterializationTypelist($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize);
								}
							}
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveSterilization();
							}

							//Function for editing the entries
							$scope.editSterilization = function(sterlizationObj) {
								$scope.sterilizationMasterObj = sterlizationObj;
								$scope.sterilizationMasterObj.id = sterlizationObj.id;
								$scope.sterilizationMasterObj.updated_by = $scope.userId;
								//$scope.sterilizationMasterObj.stauts = sterlizationObj.status;
								$scope.onEditSterilization = true;
							}
							
							
							$scope.cssdStorePopupFromController = function(){
								$rootScope.currentCSSDScope = $scope;
								$rootScope.cssdStorePopup();
								angular.element('#cssdStoreSelectionPopup').modal('show');
							}
							
							/*$scope.setCssdStoreFromController = function(store){
								$cookies.putObject('selectedCssdStore',store);
								var currentURL = $window.location.href;
								console.log(currentURL);
								$scope.init();
							}*/
							
							
							
							// Function For Save Sterilization Type Master
							$scope.saveSterilization = function() {
								console.log("sterilizationMasterObj"+$scope.sterilizationMasterObj)
								$scope.$broadcast('show-errors-check-validity'); 
								
								if ($scope.sterilizationform.$valid) {
									
									var URI = BASE_URL + ROOT_URL
									+ CREATESTERILIZATION;
									if ($scope.onEditSterilization) {
										$scope.updateSterilization()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											$rootScope.startLoader();
											GenericService.serviceAction("POST", URI, $scope.sterilizationMasterObj)
											.then(function(response) {
												if (response.data.status == "success") {
													growl.success('Sterilization added Successfully...!!',{
														title : 'Success!'
													});
													$scope.$broadcast('show-errors-reset');
													$scope.init();
													$scope.getSterializationTypelist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
													$rootScope.stopLoader();
												} else {
													growl.error('Sterilization already exits...!!',{
														title : 'Error!'
													});
												}
											});
										}
									}
								}

							}

						
							
							// Function For update Sterilization status
							$scope.updateStatus = function(sterlization) {
								var statusSterilizationObj = {
										id: sterlization.id,  
										status : sterlization.status,
										updated_by : $scope.userId,
									};
							
								var URI = BASE_URL + ROOT_URL
										+ UPDATESTERILIZATIONSTATUS;
								GenericService
										.serviceAction("PUT", URI, statusSterilizationObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Sterilization Updated Successfully...!!',
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

							// Function For update Sterlization
							$scope.updateSterilization = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.sterilizationform.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATESTERILZATION;
									GenericService.serviceAction("PUT", URI, $scope.sterilizationMasterObj)
									.then(function(response) {
										if (response.data.status == "success") {
										growl.success('Sterilization updated Successfully...!!',{
												title : 'Success!'
											});
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											growl.error('Update Sterilization already exits...!!',{
												title : 'Error!'
											});
										}
									});
								}
							}
						} ]);
