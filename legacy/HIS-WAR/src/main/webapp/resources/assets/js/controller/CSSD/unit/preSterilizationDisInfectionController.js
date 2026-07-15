angular
		.module('myApp')
		.controller(
				'preSterilizationDisInfectionController',
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
								
								var disInfectedWorklistUrl = BASE_URL + ROOT_URL 
									+ GETDISINFECTEDWORKLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								
								$scope.loadingMessage="Loading...";
								$scope.preDisInfectionKitList = [];
								$rootScope.startLoader();
								GenericService
									.serviceAction("GET", disInfectedWorklistUrl)
									.then(
											function(response) {
												
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.preDisInfectionKitList = response.data.listObject;
													console.log("preDisInfectionKitList",$scope.preDisInfectionKitList);
												}
													$rootScope.stopLoader();
													if($scope.preDisInfectionKitList.length==0)
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
							
							 $scope.preDisInfectionKitList = [];
							
							 // For Get Dis Infected Worklist By Multiple Fields
								
								$scope.getDisInfectedWorklistByMultipleFields = function() 
								{
									
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
									
									var disInfectedWorklistUrl = BASE_URL + ROOT_URL 
									+ GETDISINFECTEDWORKLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
									
									if($scope.searchName != undefined )
									{
										disInfectedWorklistUrl = disInfectedWorklistUrl + "&name=" + $scope.searchName; 
									}
									
									if($scope.selectedDisInfectionStatus != undefined )
									{
										disInfectedWorklistUrl =  disInfectedWorklistUrl + "&status=" + $scope.selectedDisInfectionStatus;
									}
									$scope.preDisInfectionKitList = [];
									$scope.loadingMessage="Loading...";
									$rootScope.startLoader();
									GenericService
									.serviceAction("GET", disInfectedWorklistUrl)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.preDisInfectionKitList = response.data.listObject;
													console.log("preDisInfectionKitList",$scope.preDisInfectionKitList);												
													}
												if($scope.preDisInfectionKitList.length==0)
													$scope.loadingMessage="No record found";
												$rootScope.stopLoader();
											});
								}
								
								$scope.cssdStorePopupFromController = function(){
									$rootScope.currentCSSDScope = $scope;
									$rootScope.cssdStorePopup();
									angular.element('#cssdStoreSelectionPopup').modal('show');
								}
						
						}]);



