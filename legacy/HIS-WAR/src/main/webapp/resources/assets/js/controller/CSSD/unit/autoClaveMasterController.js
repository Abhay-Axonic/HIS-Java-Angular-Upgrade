'use strict';

angular
		.module('myApp')
		.controller(
				'autoClaveMasterController',
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

							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.autoclaveListCount;
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
								
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditAutoClave = false;
								
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
				
								$scope.dummyAutoClaveMasterObj = {
										machine_name : "",
										machine_description : "",
										sterilization_type_id: "",
										sterilization_color_id : "",
										biological_indicator_day: "",
										sterilization_temperature: "",
										sterilization_pressure: "",
										created_by : $scope.userId,
										status: 'A',
										updated_by : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										storeId : $scope.storeId

									};

								$scope.autoClaveMasterObj = $scope.dummyAutoClaveMasterObj;

								var data = {};
																																
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVESTERILIZATION + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								GenericService.serviceAction("GET", URI, data)
									.then(function(response) {
									$scope.sterilizationList = [];

									if (response.data.status == "success") {
										$scope.sterilizationList = response.data.listObject;
										console.log("sterilizationList",$scope.sterilizationList);

										}
									});
								
								var URI1 = BASE_URL + ROOT_URL + GETACTIVESTERILIZATIONCOLOR;
								
								GenericService.serviceAction("POST", URI1, data)
									.then(function(response) {
									$scope.sterilizationColorList = [];

									if (response.data.status == "success") {
										$scope.sterilizationColorList = response.data.listObject;
										console.log("sterilizationColorList",$scope.sterilizationColorList);

										}
									});
								
							}

							$scope.init();
							
							$scope.getAutoclavelist=function(orgId,offset, noOfRecordsPerPage){
								$rootScope.startLoader();
								var offset1 = offset != null ? offset : 0;
								var perPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								
								var data2 = {
										organizationId:orgId
										};
								
								var URI2 = BASE_URL + ROOT_URL + "/cssd/getAutoclavesListcount";
								var data = {};
								
								var URI = BASE_URL + ROOT_URL
										+ GETAUTOCLAVELIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId +"&offset="+offset1+"&noOfRecordsPerPage="+perPage;
								GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
									$scope.autoClaveList = [];

									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.autoClaveList = response.data.listObject;
										console.log("autoClaveList",$scope.autoClaveList);

									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){	
										$scope.autoclaveListCount={};
										$rootScope.stopLoader();
										$scope.autoclaveListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
							}

							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.getAutoclavelist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getAutoclavelist($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
							
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.autoclaveListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getAutoclavelist($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize);
								}
							}
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAutoClave();
							}

							//Function for editing the entries
							$scope.editAutoClave = function(autoClaveObj) {
								$scope.autoClaveMasterObj = autoClaveObj;
								$scope.autoClaveMasterObj.id = autoClaveObj.id;
								$scope.autoClaveMasterObj.machine_name = autoClaveObj.machine_name;
								$scope.autoClaveMasterObj.machine_description = autoClaveObj.machine_description;
								$scope.autoClaveMasterObj.sterilization_type_id = autoClaveObj.sterilization_type_id;
								$scope.autoClaveMasterObj.sterilization_color_id = autoClaveObj.sterilization_color_id;
								$scope.autoClaveMasterObj.biological_indicator_day = autoClaveObj.biological_indicator_day;
								$scope.autoClaveMasterObj.sterilization_temperature = autoClaveObj.sterilization_temperature;
								$scope.autoClaveMasterObj.sterilization_pressure = autoClaveObj.sterilization_pressure;
								$scope.autoClaveMasterObj.updated_by = $scope.userId;
								$scope.onEditAutoClave = true;
							}
							
							// Function For Save Auto Clave
							$scope.saveAutoClave = function() {
								$scope.$broadcast('show-errors-check-validity');
                                //return false;
								if ($scope.autoclaveform.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ CREATEAUTOCLAVE;
									if ($scope.onEditAutoClave) {
										$scope.updateAutoClave()
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;
	
										} else {
											GenericService.serviceAction("POST", URI, $scope.autoClaveMasterObj)
											.then(function(response) {
												if (response.data.status == "success") {
													growl.success('MasterClave added Successfully...!!',{
														title : 'Success!'
													});
													$scope.$broadcast('show-errors-reset');
													$scope.init();
													$scope.getAutoclavelist();
												} else {
													growl.error('MasterClave already exits...!!',{
														title : 'Error!'
													});
												}
											});
										}
									}
								}

							}

							

							// Function For update Autoclave status
							$scope.updateStatus = function(autoClave) {
								var statusAutoClaveObj = {
										id: autoClave.id,  
										status : autoClave.status,
										updated_by : $scope.userId,
									};
								var URI = BASE_URL + ROOT_URL
										+ UPDATEAUTOCLAVESTATUS;
								GenericService
										.serviceAction("PUT", URI, statusAutoClaveObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'AutoClave Updated Successfully...!!',
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
							
							
							
							$scope.cssdStorePopupFromController = function(){
								$rootScope.currentCSSDScope = $scope;
								$rootScope.cssdStorePopup();
								angular.element('#cssdStoreSelectionPopup').modal('show');
							}

							// Function For update Autoclave
							$scope.updateAutoClave = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.autoclaveform.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATEAUTOCLAVE;
									GenericService.serviceAction("PUT", URI, $scope.autoClaveMasterObj)
									.then(function(response) {
										if (response.data.status == "success") {
										growl.success('AutoClave updated Successfully...!!',{
												title : 'Success!'
											});
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											growl.error('Update AutoClave already exits...!!',{
												title : 'Error!'
											});
										}
									});
								}
							}
						} ]);
