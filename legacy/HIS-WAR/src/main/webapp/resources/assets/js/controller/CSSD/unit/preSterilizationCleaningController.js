angular
		.module('myApp')
		.controller(
				'preSterilizationCleaningController',
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
								
								$scope.datePickerForCleanedFromDate =new Date();
								
								$scope.datePickerForCleanedToDate = new Date();
								
								$scope.datePickerForPendingFromDate = new Date();
								
								$scope.datePickerForPendingToDate = new Date();
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								$scope.openDatePickerForCleanedFromDate = function() {
									$scope.datePickerForCleanedFromDate.opened = true;
								
								};
								
								$scope.openDatePickerForCleanedToDate = function() {
									$scope.datePickerForCleanedToDate.opened = true;
								
								};
								
								$scope.openDatePickerForPendingFromDate = function() {
									$scope.datePickerForPendingFromDate.opened = true;
								
								};
								
								$scope.openDatePickerForPendingToDate = function() {
									$scope.datePickerForPendingToDate.opened = true;
								
								};
								
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
								
								$scope.secondTabSelected = true;
								$scope.preKitList = [];
								$scope.preKitListPendingCleaning = [];
								/******For initial cssd store selection*********/
								
								var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								if (cssdStoreObj != undefined) {
									$scope.storeId = cssdStoreObj.storeId;
								}
								
								/***********************************************/
								
								//var fromDate = ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForCleanedFromDate.getFullYear();
								
								var extendedToDate = new Date();
								var	temp = $scope.datePickerForCleanedToDate.getDate();
								
								extendedToDate.setDate(temp + 1);
								
								//var toDate = ("0" + extendedToDate.getDate()).slice(-2) +'-'+("0" + (extendedToDate.getMonth()+1)).slice(-2)+'-'+extendedToDate.getFullYear();
								
								var fromDate = ($scope.datePickerForCleanedFromDate.getFullYear() +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+  ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2));

								var toDate = ($scope.datePickerForCleanedFromDate.getFullYear() +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+  ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2));
								var cleanedWorklistUrl = BASE_URL + ROOT_URL 
								+ GETCLEANEDWORKLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								$scope.loadingMsg="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", cleanedWorklistUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.preKitList = response.data.listObject;
												if($scope.preKitList.length==0)
													$scope.loadingMsg="No record found";
												console.log("preKitList",$scope.preKitList);
											}else{
												$rootScope.stopLoader();
											}
										});
								
								
								//For Pending
								//var fromDate = ("0" + $scope.datePickerForPendingFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForPendingFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForPendingFromDate.getFullYear();
								
								var extendedToDate = new Date();
								var	temp = $scope.datePickerForPendingToDate.getDate();
								
								extendedToDate.setDate(temp + 1);
								
								//var toDate = ("0" + extendedToDate.getDate()).slice(-2) +'-'+("0" + (extendedToDate.getMonth()+1)).slice(-2)+'-'+extendedToDate.getFullYear();
								
								
								var pendingWorklistUrl = BASE_URL + ROOT_URL 
								+ GETPENDINGCLEANINGWORKLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								$scope.loadingMsg="Loading...";
								GenericService
								.serviceAction("GET", pendingWorklistUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.preKitListPendingCleaning = response.data.listObject;
												if($scope.preKitListPendingCleaning.length==0)
												$scope.loadingMsg="No record found";
												console.log("preKitListPendingCleaning",$scope.preKitListPendingCleaning);
											}
										});
								
						
							}
							
							$scope.init();
							
							$scope.setTab = function(tabNumber){
								if(tabNumber == 2){
									$scope.secondTabSelected = true;
								}else{
									$scope.secondTabSelected = false;
								}
							}
							
							$scope.cssdStorePopupFromController = function(){
								$rootScope.currentCSSDScope = $scope;
								$rootScope.cssdStorePopup();
								angular.element('#cssdStoreSelectionPopup').modal('show');
							}
							
								// For Get Cleaned Worklist By Multiple Fields
								
								$scope.getCleanedWorklistByMultipleFields = function() 
								{
									
									$scope.preKitList = [];
									$scope.loadingMsg="Loading...";
									var fromDate ='';
									
									//fromDate = ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForCleanedFromDate.getFullYear();
									
									var toDate ='';
									
									//toDate = ("0" + ($scope.datePickerForCleanedToDate.getDate())).slice(-2) +'-'+("0" + ($scope.datePickerForCleanedToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForCleanedToDate.getFullYear();
									 fromDate = ($scope.datePickerForCleanedFromDate.getFullYear() +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+  ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2));

									 toDate = ($scope.datePickerForCleanedFromDate.getFullYear() +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+  ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2));
									
									var cleanedWorklistUrl = BASE_URL + ROOT_URL 
									+ GETCLEANEDWORKLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
									
									if($scope.searchNameForCleaned != undefined )
									{
										cleanedWorklistUrl = cleanedWorklistUrl + "&name=" + $scope.searchNameForCleaned; 
									}
									
									if($scope.selectedCleaningStatus != undefined )
									{
										cleanedWorklistUrl =  cleanedWorklistUrl + "&status=" + $scope.selectedCleaningStatus;
									}
								
									$rootScope.startLoader();
									GenericService
									.serviceAction("GET", cleanedWorklistUrl)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.preKitList = response.data.listObject;
													if($scope.preKitList.length==0)
														$scope.loadingMsg="No record found";
													console.log("preKitList",$scope.preKitList);
												}else{
													$rootScope.stopLoader();
												}
											});

								}
								
								// For Get Pending Cleaned Worklist By Multiple Fields
								
								$scope.getPendingCleaningWorklistByMultipleFields = function() 
								{
									$scope.preKitListPendingCleaning = [];
									$scope.loadingMsg="Loading...";
									var fromDate ='';
									
									//fromDate = ("0" + $scope.datePickerForPendingFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForPendingFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForPendingFromDate.getFullYear();
									
									var toDate ='';
									
									//toDate = ("0" + ($scope.datePickerForPendingToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForPendingToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForPendingToDate.getFullYear();
									
									//toDate = ("0" + ($scope.datePickerForPendingToDate.getDate())).slice(-2) +'-'+("0" + ($scope.datePickerForPendingToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForPendingToDate.getFullYear();
									
									 fromDate = ($scope.datePickerForCleanedFromDate.getFullYear() +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+  ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2));

									 toDate = ($scope.datePickerForCleanedFromDate.getFullYear() +'-'+("0" + ($scope.datePickerForCleanedFromDate.getMonth()+1)).slice(-2)+'-'+  ("0" + $scope.datePickerForCleanedFromDate.getDate()).slice(-2));
									
									var pendingWorklistUrl = BASE_URL + ROOT_URL 
									+ GETPENDINGCLEANINGWORKLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
									
									if($scope.searchNameForPending != undefined )
									{
										pendingWorklistUrl = pendingWorklistUrl + "&name=" + $scope.searchNameForPending; 
									}
								
									$rootScope.startLoader();									
									GenericService
									.serviceAction("GET", pendingWorklistUrl)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.preKitListPendingCleaning = response.data.listObject;
													if($scope.preKitListPendingCleaning.length==0)
														$scope.loadingMsg="No record found";
													console.log("preKitListPendingCleaning",$scope.preKitListPendingCleaning);
												}else{
													$rootScope.stopLoader();
												}
											});
								}
						
						}]);



