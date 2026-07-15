angular
		.module('myApp')
		.controller(
				'kitIssueController',
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
								
								$scope.selectedKitIssueFromStoreId = $scope.storeId; 
								
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
								//fromDate.setDate(fromDate.getDate()-30)
								fromDate = ("0" + fromDate.getDate()).slice(-2) +'-'+("0" + (fromDate.getMonth()+1)).slice(-2)+'-'+fromDate.getFullYear();
								
								var toDate = new Date();
								toDate.setDate(toDate.getDate()+1)
								toDate = ("0" + toDate.getDate()).slice(-2) +'-'+("0" + (toDate.getMonth()+1)).slice(-2)+'-'+toDate.getFullYear();
								
								var kitIssueUrl = BASE_URL + ROOT_URL 
								+ GETKITISSUEBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&fromStore=" + $scope.selectedKitIssueFromStoreId + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								$scope.loadingMessage="Loading...";
								$scope.kitIssueList = [];
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", kitIssueUrl)
								.then(
										function(response) {
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
								
								
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}
								
								//$scope.getKitIndentByMultipleFields();
																
								

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								
								
								$scope.dummyKitIndentObj = {
										code : "",
										name : "",
										surgeon: "",
										kitIndentList: [],
										created_by : $scope.userId,
										updated_by : $scope.userId
									};

								$scope.kitIndentObj = $scope.dummyKitIndentObj;
					
																			
								
								
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								$scope.kitIssueFromStoreObj = {
										
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
								.serviceAction("POST", URI,objForStore)
								.then(
										function(response) {
								$scope.kitIssueFromStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIssueFromStoreList = response.data.listObject;
										console.log(	"kitIssueFromStoreList",$scope.kitIssueFromStoreList);

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
							
							
														  
							// For Get Issue By Multiple Fields
							
							$scope.getKitIssueListByMultipleFields = function() 
							{
								
								var fromDate ='';
								
								fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
								
								var toDate ='';
								
								toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
																								
								
								var kitIssueUrl = BASE_URL + ROOT_URL 
								+ GETKITISSUEBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								
								
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
								$scope.loadingMessage="Loading...";
								$scope.kitIssueList = [];
								$rootScope.startLoader();
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
							
							
							$scope.isDisabled = function(info)
							{
								if(info.status != 'New')
									{
									return true;
									}
								
									return false;
									
							}
							
							
							
							
							//For Get Kit List By Indent Id
							$scope.getKitListByIndentNo = function(info){
																
								var URI = BASE_URL + ROOT_URL
								+ GETKITLISTBYINDENTNO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&indentNo=" + info.indentNo; 
								
								 var data = {};

						
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.kitInKitIndentList = [];
										if (response.data.status == "success") {
											$scope.kitInKitIndentList = response.data.listObject;
											console
													.log(
															"kitInKitIndentList",
															$scope.kitInKitIndentList);
	
										}
									});
								
							}
							
							
							//For Instrument Modal 
							
							$scope.indentKitInstrumentInfo = [];
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.indentKitInstrumentInfo = kitInfo;
							}
							
                            
                            
                            //Selecting Kit Indent
                            
                            $scope.selection = [];
                            
                            $scope.kitIndentSelection = function(info)
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
                            
                            
                            // Function to update status of kit indent
                            
                            $scope.updateKitIndentStatus = function ()
                            {
                            	var URI = BASE_URL + ROOT_URL
								+ UPDATEKITINDENTSTATUS ;// update kit indent status 
								var data = {};

						
							GenericService
							.serviceAction("PUT", URI, $scope.selection)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Indent Saved Successfully...!!',{
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
                            		
                            		indentNo : "",
                            		cancelReasonNote : "",
                            		cancelReasonId : ""
                            		
                            };
                            
                            $scope.toCancel = function(info)
                            {
                            	
                            	$scope.toCancelItem.indentNo = info.indentNo;
                            	$scope.getReasonsForCancel();
                            }
                            
                            // Function to cancel kit indent
                            
                            $scope.cancelKitIndent = function ()
                            {                            	                            	
                            	var URI = BASE_URL + ROOT_URL
								+ CANCELKITINDENT;// cancel kit indent
                            	
								var data = {};

								$scope.toCancelItem.cancelReasonNote = $scope.cancelReasonNote;
								$scope.toCancelItem.cancelReasonId = $scope.cancelReasonId;
								
							GenericService
							.serviceAction("PUT", URI, $scope.toCancelItem)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Indent cancelled Successfully...!!',{
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
                            
                            
                            
                            //Selecting Kit Issue
                            
                            $scope.selection = [];
                            
                            $scope.kitIssueSelection = function(info)
                            {
                            	var index = $scope.selection.indexOf(info);

								if(index != -1) {
									$scope.selection.splice(index, 1);
								}
								else {
									$scope.selection.push(info);
								}
								
                            }
                            
							
                            
                            // Function to update status of kit issue
                            
                            $scope.updateKitIssueStatus = function ()
                            {
                            	var URI = BASE_URL + ROOT_URL
								+ UPDATEKITISSUESTATUS ;// update kit indent status 
								var data = {};

						
							GenericService
							.serviceAction("PUT", URI, $scope.selection)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Issue Saved Successfully...!!',{
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
                            		cancelReasonId : "",
                            		
                            
                            		
                            };
                            
                            $scope.toCancel = function(info)
                            {
                            	
                            	$scope.toCancelItem.issueNo = info.issueNo;
                            	$scope.getReasonsForCancel();
                            }
                            
                            // Function to cancel kit indent
                            
                            $scope.cancelKitIssue = function ()
                            {                            	                            	
                            	var URI = BASE_URL + ROOT_URL
								+ CANCELKITISSUE;// cancel kit indent
                            	
								var data = {};

								$scope.toCancelItem.cancelReasonNote = $scope.cancelReasonNote;
								$scope.toCancelItem.cancelReasonId = $scope.cancelReasonId;
								$scope.toCancelItem.organizationId = $scope.organizationId;
                        		$scope.toCancelItem.unitId = $scope.unitId;
								
							GenericService
							.serviceAction("PUT", URI, $scope.toCancelItem)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Issue cancelled Successfully...!!',{
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
							
							$scope.init();
							
								
						
						}]);



