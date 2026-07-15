angular
		.module('myApp')
		.controller(
				'kitIndentAcceptanceController',
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
								
								
								var date = new Date();
								$scope.datePickerForFromDate2 = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate: new Date(),
									minDate : date.setDate((new Date()).getDate() - 90),
								};
								
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
								
								var kitIssueUrl = BASE_URL + ROOT_URL 
								+ GETKITISSUEBYMULTIPLEFIELDSFORACCEPTANCE + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								$scope.loadingMessage="";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", kitIssueUrl)
								.then(
										function(response) {
											$scope.loadingMessage="Loading...";
											$scope.kitIssueList = [];
											if (response.data.status == "success") {
												$scope.kitIssueList = response.data.listObject;
												console
														.log(
																"kitIssueList",
																$scope.kitIssueList);

											}
											if($scope.kitIssueList.length==0)
												$scope.loadingMessage="No record found";
											$rootScope.stopLoader();
										});
								
								
								if($stateParams.kitId){
									$scope.viewPreInspectionKitDetails();
									$scope.viewPreKitEquipDetails();
									
								}
								
								$scope.kitIndentFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								//For From Store By User Id
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE  + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/
								var URI = BASE_URL + ROOT_URL+ "/cssd/getCssdStoreList";
								var objForStore={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										userId :cookieObject.userId
								};
								
								GenericService
								.serviceAction("POST", URI, objForStore)
								.then(
										function(response) {
								$scope.kitIssueFromStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIssueFromStoreList = response.data.listObject;
										console
											.log(
													"kitIssueFromStoreList",
													$scope.kitIssueFromStoreList);

								}
																		
							});
								
								
								//For To Store By CSSD
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId  + "&userId=" + $scope.userId;
								
								GenericService
								.serviceAction("GET", URI, $scope.storeMasterObj)
								.then(
										function(response) {
								$scope.kitIssueToStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIssueToStoreList = response.data.listObject;
										console
											.log(
													"kitIssueToStoreList",
													$scope.kitIssueToStoreList);

								}
																		
							});
								

							}
							
							
							
							// For Get Indent By Multiple Fields
							$scope.getKitIssueListByMultipleFields = function() 
							{
								$rootScope.startLoader();
								var fromDate ='';
								$scope.loadingMessage="Loading...";
								$scope.kitIssueList = [];
								fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
								
								var toDate ='';
								
								/*toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();*/
								toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();																
								
								var kitIssueUrl = BASE_URL + ROOT_URL 
								+ GETKITISSUEBYMULTIPLEFIELDSFORACCEPTANCE + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								
								
								if($scope.selectedKitIssueFromStoreId != undefined )
								{
									kitIssueUrl = kitIssueUrl + "&fromStore=" + $scope.selectedKitIssueFromStoreId; 
								}
								
								if($scope.selectedKitIssueToStoreId != undefined )
								{
									kitIssueUrl =  kitIssueUrl + "&toStore=" + $scope.selectedKitIssueToStoreId;
								}
							
								if($scope.selectedKitIssueStatus != undefined && $scope.selectedKitIssueStatus != "" )
								{
									kitIssueUrl =  kitIssueUrl + "&status=" + $scope.selectedKitIssueStatus;
								}
								
								GenericService
								.serviceAction("GET", kitIssueUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitIssueList = response.data.listObject;
												console
														.log(
																"kitIssueList",
																$scope.kitIssueList);

											}
											if($scope.kitIssueList.length==0)
											$scope.loadingMessage="No record found";
											$rootScope.stopLoader();
										});

							}
							
							
							$scope.init();
							
								
						
						}]);



