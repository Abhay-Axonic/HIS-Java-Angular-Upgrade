angular
		.module('myApp')
		.controller(
				'kitReturnController',
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
								$scope.dateOptions = {
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
								
								var kitReturnUrl = BASE_URL + ROOT_URL 
								+ GETKITRETURNBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								$scope.loadingMessage="";
								$scope.kitReturnList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", kitReturnUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitReturnList = response.data.listObject;
												console
														.log(
																"kitReturnList",
																$scope.kitReturnList);

											}
											if($scope.kitReturnList.length==0)
												$scope.loadingMessage="No record found";
											$rootScope.stopLoader();
										});
								
								
								$scope.onEditKitIndent = false;
								
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
								
															
								
								if($stateParams.kitId){
									$scope.viewPreInspectionKitDetails();
									$scope.viewPreKitEquipDetails();
									
								}
								
								$scope.kitIndentFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								//For From Store By User Id
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&userId=" + $scope.userId;
								
								GenericService
								.serviceAction("GET", URI, $scope.kitIndentFromStoreObj)
								.then(
										function(response) {
								$scope.kitIndentFromStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIndentFromStoreList = response.data.listObject;
										console
											.log(
													"kitIndentFromStoreList",
													$scope.kitIndentFromStoreList);

								}
																		
							});
								
								
								//For To Store By CSSD
							/*	
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/
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
								$scope.kitIndentToStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIndentToStoreList = response.data.listObject;
										console
											.log(
													"kitIndentToStoreList",
													$scope.kitIndentToStoreList);

								}
																		
							});
								

							}
							
							
							
							
														  
							// For Get Return By Multiple Fields
							
							$scope.getKitReturnListByMultipleFields = function() 
							{
								var fromDate ='';
								
								fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
								
								var toDate ='';
								
								toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
								
								
								var kitReturnUrl = BASE_URL + ROOT_URL 
								+ GETKITRETURNBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								
								
								if($scope.selectedKitReturnFromStoreId != undefined )
								{
									kitReturnUrl = kitReturnUrl + "&fromStore=" + $scope.selectedKitReturnFromStoreId; 
								}
								
								if($scope.selectedKitReturnToStoreId != undefined )
								{
									kitReturnUrl =  kitReturnUrl + "&toStore=" + $scope.selectedKitReturnToStoreId;
								}
							
								if($scope.selectedKitReturnStatus != undefined && $scope.selectedKitReturnStatus != "" )
								{
									kitReturnUrl =  kitReturnUrl + "&status=" + $scope.selectedKitReturnStatus;
								}
								$scope.kitReturnList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", kitReturnUrl)
								.then(
										function(response) {
											
											if (response.data.status == "success") {
												$scope.kitReturnList = response.data.listObject;
												console
														.log(
																"kitReturnList",
																$scope.kitReturnList);

											}
											if($scope.kitReturnList.length==0)
												$scope.loadingMessage="No record found";
											$rootScope.stopLoader();
											
										});

							}
							
							
							//For Instrument Modal 
							
							$scope.indentKitInstrumentInfo = [];
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.indentKitInstrumentInfo = kitInfo.kitInstrumentList;
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
                            		
                            		issueNo : "",
                            		cancelReasonNote : "",
                            		cancelReasonId : ""
                            		
                            };
                            
                            $scope.toCancel = function(info)
                            {
                            	
                            	$scope.toCancelItem.issueNo = info.issueNo;
                            	$scope.getReasonsForCancel();
                            }
                            
                            // Function to cancel kit indent
                            
                            $scope.cancelKitReturn = function ()
                            {                            	                            	
                            	var URI = BASE_URL + ROOT_URL
								+ CANCELKITRETURN;// cancel kit indent
                            	
								var data = {};

								$scope.toCancelItem.cancelReasonNote = $scope.cancelReasonNote;
								$scope.toCancelItem.cancelReasonId = $scope.cancelReasonId;
								
							GenericService
							.serviceAction("PUT", URI, $scope.toCancelItem)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Return cancelled Successfully...!!',{
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
							
                            
                            
                            
                            
                            
                            //Selecting Kit Return
                            
                            $scope.selection = [];
                            
                            $scope.kitReturnSelection = function(info)
                            {
                            	var index = $scope.selection.indexOf(info);

								if(index != -1) {
									$scope.selection.splice(index, 1);
								}
								else {
									$scope.selection.push(info);
								}
								
                            }
                            
                            
                            $scope.isSaveDisabled = function(){
                            	if($scope.selection.length > 0){
                            		return false;
                            	} else {
                            		return true;
                            	}
                            }
                            
						
                            // Function to update status of kit return
                            
                            $scope.updateKitReturnStatus = function ()
                            {
                            	var URI = BASE_URL + ROOT_URL
								+ UPDATEKITRETURNSTATUS ;// update kit indent status 
								var data = {};

						
							GenericService
							.serviceAction("PUT", URI, $scope.selection)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Return Updated Successfully...!!',{
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



