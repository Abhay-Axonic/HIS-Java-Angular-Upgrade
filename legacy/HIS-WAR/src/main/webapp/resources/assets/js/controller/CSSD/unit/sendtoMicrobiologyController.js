angular
		.module('myApp')
		.controller(
				'sendtoMicrobiologyController',
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
								
								$scope.toDate= moment().toDate();
								$scope.fromDate= moment().toDate();
								$scope.dateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate:new Date(),
									minDate : moment(new Date()).subtract(180, 'days').toDate()
								};
							    $scope.toDateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate:new Date()
								};
								
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
								
								var sendToMicrobiologyUrl = BASE_URL + ROOT_URL 
								+ GETSENDTOMICROBIOLOGYLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								
								$scope.sendToMicrobiologyList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", sendToMicrobiologyUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.sendToMicrobiologyList = response.data.listObject;
												console
														.log(
																"sendToMicrobiologyList",
																$scope.sendToMicrobiologyList);

											}
											if($scope.sendToMicrobiologyList.length==0)
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
								
								$scope.getSendToMicrobiologyListByMultipleFields = function() 
								{
									
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									/*toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();*/
									toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
									
									var sendToMicrobiologyUrl = BASE_URL + ROOT_URL 
									+ GETSENDTOMICROBIOLOGYLISTBYMULTIPLEFIELDS + "?from=" + fromDate + "&to=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
									
									
									
									if($scope.batchNo != undefined )
									{
										sendToMicrobiologyUrl = sendToMicrobiologyUrl + "&batchNo=" + $scope.batchNo; 
									}
									
									if($scope.selectedStatusForSendToMicrobiology != undefined )
									{
										sendToMicrobiologyUrl =  sendToMicrobiologyUrl + "&status=" + $scope.selectedStatusForSendToMicrobiology;
									}
									$scope.sendToMicrobiologyList = [];
									$scope.loadingMessage="Loading...";
									$rootScope.startLoader();
									GenericService
									.serviceAction("GET", sendToMicrobiologyUrl)
									.then(
											function(response) {
												$scope.sendToMicrobiologyList = [];
												if (response.data.status == "success") {
													$scope.sendToMicrobiologyList = response.data.listObject;
													console
															.log(
																	"sendToMicrobiologyList",
																	$scope.sendToMicrobiologyList);

												}
												if($scope.sendToMicrobiologyList.length==0)
													$scope.loadingMessage="No record found";
												$rootScope.stopLoader();
											});

								}
								
								$scope.currentLoadBatchItem = {};
								
								$scope.setCurrentLoadBatchItem = function(info){
								
									$scope.currentLoadBatchItem = info;
									
								}
								
								
								
								$scope.isDisabled = function(info){
									if(info.status != 'Sent To Microbiology'){
										return false;
									}else {
										return true;
									}
								}
							
								
								//Send To Microbiology
								$scope.sendToMicrobiology = function ()
	                            {
	                            	var URI = BASE_URL + ROOT_URL
									+ SENDTOMICROBIOLOGY ;
									var data = {};

							
								GenericService
								.serviceAction("PUT", URI, $scope.currentLoadBatchItem)
								.then(
										function(response) {
											if (response.data.status == "success") {
												growl.success('Sent To Microbiology Successfully...!!',{
													title : 'Success!'
												});
												$scope.$broadcast('show-errors-reset');
												$scope.init();
											} else {
												growl.error('Something went wrong !',{
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
								
								
						
						}]);



