angular
		.module('myApp')
		.controller(
				'sterilizationRequestController',
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
						'PaginationService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams,PaginationService) {
							
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
								
								var date = new Date();
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate: new Date(),
									minDate : date.setDate((new Date()).getDate() - 90),
								};
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
								
								
								 $scope.pagination = {
						                    currentPage: 1,
						                    totalPages: 0,
						                    pageSize: '10',
						                    search: '',
						                    searchData: PaginationService.searchData,
						                    setTotalPages: PaginationService.setTotalPages,
						                    getPagesList: PaginationService.getPagesList,
						                    setPage: PaginationService.setPage,
						                    setFirstPage: PaginationService.setFirstPage,
						                    setPreviousPage: PaginationService.setPreviousPage,
						                    setNextPage: PaginationService.setNextPage,
						                    setLastPage: PaginationService.setLastPage,
						                    sortBy: PaginationService.sortBy
						                  }
								 
								var fromDate = new Date();
								fromDate.setDate(fromDate.getDate())
								fromDate = ("0" + fromDate.getDate()).slice(-2) +'-'+("0" + (fromDate.getMonth()+1)).slice(-2)+'-'+fromDate.getFullYear();
								
								var toDate = new Date();
								toDate.setDate(toDate.getDate()+1)
								toDate = ("0" + toDate.getDate()).slice(-2) +'-'+("0" + (toDate.getMonth()+1)).slice(-2)+'-'+toDate.getFullYear();
								
								var sterilizationRequestUrl = BASE_URL + ROOT_URL 
								+ GETSTERILIZATIONREQUESTBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								$scope.loadingMessage="Loading...";
								$scope.sterilizationRequestList = [];
								 $scope.pagination.data=[];
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", sterilizationRequestUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.sterilizationRequestList = response.data.listObject;
												
												 if($scope.sterilizationRequestList.length > 0){            
							                         PaginationService.searchData($scope.sterilizationRequestList, $scope.pagination);
							                       }
												console
														.log(
																"sterilizationRequestList",
																$scope.sterilizationRequestList);

											}
											if($scope.sterilizationRequestList.length==0)
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
								
								
								
								$scope.issuingStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								
								//For Requesting Store
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE  + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/
								
								var URI = BASE_URL + ROOT_URL+ "/cssd/getCssdStoreList";
								var objForStore={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										userId :cookieObject.userId
								};
								
								GenericService
								.serviceAction("POST", URI,objForStore)
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
							
							
                         
							
							
							
							// For Get Sterilization Request List By Multiple Fields
							
							$scope.getSterilizationRequestByMultipleFields = function() 
							{
								
								var fromDate ='';
								
								fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
								
								var toDate ='';
								
								toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
																								
								
								var sterilizationRequestUrl = BASE_URL + ROOT_URL 
								+ GETSTERILIZATIONREQUESTBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								
								
								if($scope.selectedRequestingStore != undefined )
								{
									sterilizationRequestUrl = sterilizationRequestUrl + "&fromStore=" + $scope.selectedRequestingStore; 
								}
								
								if($scope.selectedIssuingStore != undefined )
								{
									sterilizationRequestUrl =  sterilizationRequestUrl + "&toStore=" + $scope.selectedIssuingStore;
								}
							
								if($scope.selectedSterilizationRequestStatus != undefined && $scope.selectedSterilizationRequestStatus != "" )
								{
									sterilizationRequestUrl =  sterilizationRequestUrl + "&status=" + $scope.selectedSterilizationRequestStatus;
								}
								$scope.loadingMessage="Loading...";
								$scope.sterilizationRequestList = [];
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", sterilizationRequestUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.sterilizationRequestList = response.data.listObject;
												console
														.log(
																"sterilizationRequestList",
																$scope.sterilizationRequestList);

											}
											if($scope.sterilizationRequestList.length==0)
												$scope.loadingMessage="No record found";
											$rootScope.stopLoader();
										});

							}
							
							
							
							$scope.isDisabled = function(info)
							{
								if(info.status != 'New')
									{
									return true;
									}
								
									return false;
									
							}
							
							
							
							
							//To get reasons for cancel 
                            
                            $scope.getReasonsForCancel = function(){
                            	                            	
                            	var URI = BASE_URL + ROOT_URL
								+ GETREASONSFORCANCEL ;
                            	
								 var data = {};

						
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.cancelReasonsList = [];
										if (response.status == "200") {
											$scope.cancelReasonsList = response.data;
											console
													.log(
															"cancelReasonsList",
															$scope.cancelReasonsList);
	
										}
									});
                            }
                            
                            
                            
                            
                            
                            $scope.toCancelItem = {
                            		cancelReasonNote : "",
                            		cancelReasonId : ""
                            };
                            
                            $scope.toCancel = function(info)
                            {
                            	
                            	$scope.toCancelItem.requestNo = info.requestNo;
                            	$scope.getReasonsForCancel();
                            }
                            
                            // Function to cancel sterilization request
                            
                            $scope.cancelSterilizationRequest = function ()
                            {                            	                            	
                            	var URI = BASE_URL + ROOT_URL
								+ CANCELSTERILIZATIONREQUEST;
                            	
								var data = {};

								$scope.toCancelItem.cancelReasonNote = $scope.cancelReasonNote;
								$scope.toCancelItem.cancelReasonId = $scope.cancelReasonId;
								
							GenericService
							.serviceAction("PUT", URI, $scope.toCancelItem)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Sterilization Request cancelled Successfully...!!',{
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
							
								
							
							
							$scope.init();
							
						
						}]);



