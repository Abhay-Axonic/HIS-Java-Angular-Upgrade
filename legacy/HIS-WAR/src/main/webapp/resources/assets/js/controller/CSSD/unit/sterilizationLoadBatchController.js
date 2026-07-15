angular
		.module('myApp')
		.controller(
				'sterilizationLoadBatchController',
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
						'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams) {
							
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
								
								
								$scope.biologicalIndicator = false;
								
								
								$scope.datePickerForFromDate = new Date();
								
								$scope.datePickerForToDate = new Date();
								
								$scope.openDatePickerForFromDate = function() {
									$scope.datePickerForFromDate.opened = true;
								
								};
								
								$scope.openDatePickerForToDate = function() {
									$scope.datePickerForToDate.opened = true;
								
								};
								
								var fromDate = new Date();
								fromDate.setDate(fromDate.getDate())
								fromDate = ("0" + fromDate.getDate()).slice(-2) +'-'+("0" + (fromDate.getMonth()+1)).slice(-2)+'-'+fromDate.getFullYear();
								
								var toDate = new Date();
								toDate.setDate(toDate.getDate()+1)
								toDate = ("0" + toDate.getDate()).slice(-2) +'-'+("0" + (toDate.getMonth()+1)).slice(-2)+'-'+toDate.getFullYear();
								
								var loadBatchUrl = BASE_URL + ROOT_URL 
								+ GETLOADBATCHLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId  + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								$scope.loadBatchList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", loadBatchUrl)
								.then(
										function(response) {
											
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.loadBatchList = response.data.listObject;
												console.log("loadBatchList",$scope.loadBatchList);
											}
											if($scope.loadBatchList.length==0)
												$scope.loadingMessage="No record found";
												$rootScope.stopLoader();
											
										});
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
							}
							
							$scope.init();

							// For Get Loaded Batch List By Multiple Fields
								
								$scope.getLoadBatchListByMultipleFields = function() 
								{
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
									
									var loadBatchUrl = BASE_URL + ROOT_URL 
									+ GETLOADBATCHLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
									
									if($scope.batchNo != undefined )
									{
										loadBatchUrl = loadBatchUrl + "&batchNo=" + $scope.batchNo; 
									}
									
									if($scope.biologicalIndicator != undefined && $scope.biologicalIndicator == true )
									{
										loadBatchUrl = loadBatchUrl + "&biologicalIndicator=" + $scope.biologicalIndicator; 
									}
									
									if($scope.selectedLoadBatchStatus != undefined && $scope.selectedLoadBatchStatus != "" )
									{
										loadBatchUrl =  loadBatchUrl + "&status=" + $scope.selectedLoadBatchStatus;
									}
									$scope.loadBatchList = [];
									$scope.loadingMessage="Loading...";
									$rootScope.startLoader();
									GenericService
									.serviceAction("GET", loadBatchUrl)
									.then(
											function(response) {
												
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.loadBatchList = response.data.listObject;
													console.log("loadBatchList",$scope.loadBatchList);
												}
													$rootScope.stopLoader();
												if($scope.loadBatchList.length==0)
													$scope.loadingMessage="No record found";
											});

								}
								
								$scope.cssdStorePopupFromController = function(){
									$rootScope.currentCSSDScope = $scope;
									$rootScope.cssdStorePopup();
									angular.element('#cssdStoreSelectionPopup').modal('show');
								}
						
						}]);



