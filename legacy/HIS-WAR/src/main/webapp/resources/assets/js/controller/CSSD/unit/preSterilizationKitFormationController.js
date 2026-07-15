angular
		.module('myApp')
		.controller(
				'preSterilizationKitFormationController',
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
								
								/*$scope.today = new Date();
								
								$scope.datePickerForFromDate = {
										maxDate : $scope.today,
										opened : false
										};
								
								$scope.datePickerForToDate = {
										maxDate : $scope.today,
										opened : false
										};*/
								
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
								
								var kitFormationUrl = BASE_URL + ROOT_URL 
								+ GETKITFORMATIONLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								$scope.kitFormationList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", kitFormationUrl)
								.then(
										function(response) {
											
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.kitFormationList = response.data.listObject;
												console
														.log(
																"kitFormationList",
																$scope.kitFormationList);

											}
											if($scope.kitFormationList.length==0)
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
							
							// $scope.preKitFormationKitList = [];
							  
								
								/*$scope.getPreKitFormationKitList = function() 
								{
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + $scope.datePickerForToDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
									
									var URI = BASE_URL + ROOT_URL
											+ GETPREKITLIST + "?from=" + fromDate + "&to=" + toDate 
											+ "&name=" + $scope.searchName + "&status=" + $scope.selectedKitFormationStatus;// search instrument 
									var data = {};

									// GET instrument list by search
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.preKitFormationKitList = response.data.listObject;
														}
													});

								}*/
								
								
								
								// For Get Kit Formation Worklist By Multiple Fields
								
								$scope.getKitFormationListByMultipleFields = function() 
								{
									
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
									
									
									var kitFormationUrl = BASE_URL + ROOT_URL 
									+ GETKITFORMATIONLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
									
									
									
									if($scope.searchName != undefined )
									{
										kitFormationUrl = kitFormationUrl + "&name=" + $scope.searchName; 
									}
									
									if($scope.selectedKitFormationStatus != undefined )
									{
										kitFormationUrl =  kitFormationUrl + "&status=" + $scope.selectedKitFormationStatus;
									}
									$scope.loadingMessage="Loading...";
									$scope.kitFormationList = [];
									$rootScope.startLoader();									
									GenericService
									.serviceAction("GET", kitFormationUrl)
									.then(
											function(response) {
												
												if (response.data.status == "success") {
													$scope.kitFormationList = response.data.listObject;
													$rootScope.stopLoader();
													console
															.log(
																	"kitFormationList",
																	$scope.kitFormationList);

												}
													$rootScope.stopLoader();
												if($scope.kitFormationList.length==0)
													$scope.loadingMessage="No record found";
											});

								}
								
							
								
								$scope.cssdStorePopupFromController = function(){
									$rootScope.currentCSSDScope = $scope;
									$rootScope.cssdStorePopup();
									angular.element('#cssdStoreSelectionPopup').modal('show');
								}
							
								
						
						}]);



