angular
		.module('myApp')
		.controller(
				'sterilizationRequestAcceptanceController',
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
								
								$scope.selectedIssuingStore = $scope.storeId;
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								$scope.onEditKitIndent = false;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								
								
								$scope.dummyKitIndentObj = {
										code : "",
										name : "",
										surgeon: "",
										kitInstrumentList: [],
										created_by : $scope.userId,
										updated_by : $scope.userId
									};

								$scope.kitIndentObj = $scope.dummyKitIndentObj;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								
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
								
								var sterRequestAcceptanceUrl = BASE_URL + ROOT_URL 
								+ GETSTERILIZATIONREQUESTFORACCEPTANCEBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&issueingStoreId=" + $scope.selectedIssuingStore + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								$scope.sterRequestAcceptanceList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", sterRequestAcceptanceUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.sterRequestAcceptanceList = response.data.listObject;
												console
														.log(
																"sterRequestAcceptanceList",
																$scope.sterRequestAcceptanceList);

											}
											$rootScope.stopLoader();
											if($scope.sterRequestAcceptanceList.length==0)
												$scope.loadingMessage="No record found";
										});
								
								
								if($stateParams.kitId){
									$scope.viewPreInspectionKitDetails();
									$scope.viewPreKitEquipDetails();
									
								}
								
															
								
								//For Requesting Store
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE  + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								GenericService
								.serviceAction("GET", URI, $scope.issuingStoreObj)
								.then(
										function(response) {
								$scope.issuingStoreList = [];

									if (response.data.status == "success") {
										$scope.issuingStoreList = response.data.listObject;
										console
											.log(
													"issuingStoreList",
													$scope.issuingStoreList);

								}
																		
							});
								
								
								//For Issuing Store
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId  + "&userId=" + $scope.userId;
								
								GenericService
								.serviceAction("GET", URI, $scope.storeMasterObj)
								.then(
										function(response) {
								$scope.requestingStoreList = [];

									if (response.data.status == "success") {
										$scope.requestingStoreList = response.data.listObject;
										console
											.log(
													"requestingStoreList",
													$scope.requestingStoreList);

								}
																		
							});
								

							}
							
							
							
							// For Get Sterilization Request Acceptance By Multiple Fields
							
							$scope.getSterRequestAcceptanceByMultipleFields = function() 
							{
								
								var fromDate ='';
								
								fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
								
								var toDate ='';
								
								/*toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();*/
								toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();																
								
								var sterRequestAcceptanceUrl = BASE_URL + ROOT_URL 
								+ GETSTERILIZATIONREQUESTFORACCEPTANCEBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&issueingStoreId=" + $scope.selectedIssuingStore + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								
								
								if($scope.selectedRequestingStoreId != undefined )
								{
									sterRequestAcceptanceUrl = sterRequestAcceptanceUrl + "&fromStore=" + $scope.selectedRequestingStoreId; 
								}
								
								/*if($scope.selectedIssuingStoreId != undefined )
								{
									sterRequestAcceptanceUrl =  sterRequestAcceptanceUrl + "&toStore=" + $scope.selectedIssuingStoreId;
								}*/

								if($scope.selectedIssuingStore == undefined || $scope.selectedIssuingStore == null || $scope.selectedIssuingStore =='')
								{growl.error("Please select Issuing Store ");
								           return false;}
							
								if($scope.selectedSterRequestAcceptanceStatus != undefined && $scope.selectedSterRequestAcceptanceStatus != "" )
								{
									sterRequestAcceptanceUrl =  sterRequestAcceptanceUrl + "&status=" + $scope.selectedSterRequestAcceptanceStatus;
								}
								$scope.sterRequestAcceptanceList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", sterRequestAcceptanceUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.sterRequestAcceptanceList = response.data.listObject;
												console
														.log(
																"sterRequestAcceptanceList",
																$scope.sterRequestAcceptanceList);

											}
											$rootScope.stopLoader();
											if($scope.sterRequestAcceptanceList.length==0)
												$scope.loadingMessage="No record found";
										});

							}
							
							
							
							$scope.cssdStorePopupFromController = function(){
								$rootScope.currentCSSDScope = $scope;
								$rootScope.cssdStorePopup();
								angular.element('#cssdStoreSelectionPopup').modal('show');
							}
							
							
							$scope.init();
							
								
						
						}]);



