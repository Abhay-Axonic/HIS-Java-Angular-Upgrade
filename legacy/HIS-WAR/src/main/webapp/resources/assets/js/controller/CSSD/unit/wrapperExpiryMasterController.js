'use strict';

angular
		.module('myApp')
		.controller(
				'wrapperExpiryMasterController',
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
						'CONSTANTS',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService, CONSTANTS) {

							/* init() function for form object create. */

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
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.commonList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.wrapperListCount;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditWrapperExpiry = false;
								
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
						
								$scope.dummyWrapperExpiryMasterObj = {
										name : "",
										code : "",
										duration: "",
										itemId: "",
										status: 'A',
										created_by : $scope.userId,
										updated_by : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										//storeId : $scope.storeId

									};
								// $searchText = "";
								$scope.wrapperExpiryMasterObj = $scope.dummyWrapperExpiryMasterObj;

								var data = {};
								
								
								var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.SEARCH_API + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								GenericService.serviceAction("GET", URI, data)
									.then(function(response) {
									$scope.itemMasterList = [];

									if (response.status == 200) {
										$scope.itemMasterList = response.data;
										console.log("itemMasterList",$scope.itemMasterList);

										}
									});
								

							}

							$scope.init();
							
							
							$scope.getWrapperExMstlist=function(orgId,offset, noOfRecordsPerPage){
								$rootScope.startLoader();
								var offset1 = offset != null ? offset : 0;
								var perPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								
								var data2 = {
										organizationId:orgId
										};
								
								//var URI1 = BASE_URL + ROOT_URL + GETHOSPITALACCOUNTMASTERLIST; 
								var URI2 = BASE_URL + ROOT_URL + "/cssd/getWrapperListcount";
								var data = {};
								
								$rootScope.startLoader();
								
								var URI = BASE_URL + ROOT_URL
										+ GETWRAPPEREXPIRYMASTERLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId+"&offset="+offset1+"&noOfRecordsPerPage="+perPage;

								// For Get WrapperList List Using Offset
								GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
									$scope.expiryMasterList = [];

									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.expiryMasterList = response.data.listObject;
										console.log("expiryMasterList",$scope.expiryMasterList);

									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){	
										$scope.wrapperListCount={};
										$rootScope.stopLoader();
										$scope.wrapperListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
							}

							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.getWrapperExMstlist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getWrapperExMstlist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.wrapperListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getWrapperExMstlist($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize);
								}
							}
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveWrapperExpiry();
							}
							
							

							//Function for editing the entries
							$scope.editWrapperExpiry = function(wrapperExpiryObj) {
								$scope.wrapperExpiryMasterObj = wrapperExpiryObj;
								$scope.wrapperExpiryMasterObj.id = wrapperExpiryObj.id;
								$scope.wrapperExpiryMasterObj.name = wrapperExpiryObj.name;
								$scope.wrapperExpiryMasterObj.duration = wrapperExpiryObj.duration;
								$scope.wrapperExpiryMasterObj.itemId = wrapperExpiryObj.itemId;
								$scope.wrapperExpiryMasterObj.updated_by = $scope.userId;
								$scope.onEditWrapperExpiry = true;
							}
							
							$scope.cssdStorePopupFromController = function(){
								$rootScope.currentCSSDScope = $scope;
								$rootScope.cssdStorePopup();
								angular.element('#cssdStoreSelectionPopup').modal('show');
							}
							
							// Function For Save Wrapper
							$scope.saveWrapperExpiry = function() {
								$scope.$broadcast('show-errors-check-validity');

								if ($scope.wrapper_expiry_form.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ CREATEWRAPPEREXPIRY;
									if ($scope.onEditWrapperExpiry) {
										$scope.updateWrapperExpiry()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											GenericService.serviceAction("POST", URI, $scope.wrapperExpiryMasterObj)
											.then(function(response) {
												if (response.data.status == "success") {
													growl.success('Wrapper Expiry added Successfully...!!',{
														title : 'Success!'
													});
													$scope.$broadcast('show-errors-reset');
													$scope.init();
													$scope.getWrapperExMstlist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
												} else {
													growl.error('Wrapper already exits...!!',{
														title : 'Error!'
													});
												}
											});
										}
									}
								}

							}

						

							// Function For update WRAPPER status
							$scope.updateStatus = function(wrapperExpiry) {
								var statusWrapperExpiryObj = {
										id: wrapperExpiry.id,  
										status : wrapperExpiry.status,
										updated_by : $scope.userId,
									};
								var URI = BASE_URL + ROOT_URL
										+ UPDATEWRAPPEREXPIRYSTATUS;
								GenericService
										.serviceAction("PUT", URI, statusWrapperExpiryObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Wrapper Expiry Master Updated Successfully...!!',
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

							// Function For update Nutrition
							$scope.updateWrapperExpiry = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.wrapper_expiry_form.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATEWRAPPEREXPIRY;
									GenericService.serviceAction("PUT", URI, $scope.wrapperExpiryMasterObj)
									.then(function(response) {
										if (response.data.status == "success") {
										growl.success('Wrapper Master updated Successfully...!!',{
												title : 'Success!'
											});
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											growl.error('Update Wrapper Master already exits...!!',{
												title : 'Error!'
											});
										}
									});
								}
							}
						} ]);
