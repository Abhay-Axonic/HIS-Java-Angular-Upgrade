angular
		.module('myApp')
		.controller(
				'sterilizationPendingIndicatorsController',
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
								
								var pendingIndicatorsUrl = BASE_URL + ROOT_URL 
								+ GETLOADBATCHLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								$scope.pendingIndicatorsList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", pendingIndicatorsUrl)
								.then(
										function(response) {
											
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.pendingIndicatorsList = response.data.listObject;
												console.log("pendingIndicatorsList",$scope.pendingIndicatorsList);
											}else{
												$rootScope.stopLoader();
											}
											if($scope.pendingIndicatorsList.length==0)
												$scope.loadingMessage="No record found";
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
								
								$scope.getPendingIndicatorsByMultipleFields = function() 
								{
									
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
									
									var pendingIndicatorsUrl = BASE_URL + ROOT_URL 
									+ GETLOADBATCHLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
									
									if($scope.batchNo != undefined )
									{
										pendingIndicatorsUrl = pendingIndicatorsUrl + "&batchNo=" + $scope.batchNo; 
									}
																
									if($scope.selectedPendingIndicatorStatus != undefined )
									{
										pendingIndicatorsUrl =  pendingIndicatorsUrl + "&status=" + $scope.selectedPendingIndicatorStatus;
									}
									$scope.pendingIndicatorsList = [];
									$scope.loadingMessage="Loading...";
									$rootScope.startLoader();
									GenericService
									.serviceAction("GET", pendingIndicatorsUrl)
									.then(
											function(response) {
												
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.pendingIndicatorsList = response.data.listObject;
													console.log("pendingIndicatorsList",$scope.pendingIndicatorsList);
												}else{
													$rootScope.stopLoader();
												}
												if($scope.pendingIndicatorsList.length==0)
													$scope.loadingMessage="No record found";
											});
								}
								
								$scope.getBatchDetailsByBatchNumber = function(info) {
									
									data = {};
									 
									var URI = BASE_URL + ROOT_URL
										+ GETBATCHDETAILSBYBATCHNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&batchNo=" + info.batchNumber;
															
									$rootScope.startLoader();
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.viewPendingIndicatorObj = {};
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.viewPendingIndicatorObj = response.data.object;
													console.log("viewPendingIndicatorObj",$scope.viewPendingIndicatorObj);												
												}else{
													$rootScope.stopLoader();
												}
											});
								}
								
								$scope.cssdStorePopupFromController = function(){
									$rootScope.currentCSSDScope = $scope;
									$rootScope.cssdStorePopup();
									angular.element('#cssdStoreSelectionPopup').modal('show');
								}
						
						}]);



