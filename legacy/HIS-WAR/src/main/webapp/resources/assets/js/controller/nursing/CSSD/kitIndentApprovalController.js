angular
		.module('myApp')
		.controller(
				'kitIndentApprovalController',
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
								
								$scope.loadingMessage="";
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
								
								var kitIndentUrl = BASE_URL + ROOT_URL 
								+ GETKITINDENTAPPROVALBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								$scope.kitIndentList = [];
								$scope.loadingMessage="Loading...";
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", kitIndentUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitIndentList = response.data.listObject;
												console
														.log(
																"kitIndentList",
																$scope.kitIndentList);
											}
											if($scope.kitIndentList.length==0)
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
																
							/*	var URI = BASE_URL + ROOT_URL
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
							
							
							
							// For Get Indent By Multiple Fields
							
							/*$scope.getKitIndentByMultipleFields = function() 
							{
											
								if($scope.selectedIndentStatus == undefined )
									{
									$scope.selectedIndentStatus = "";
									}
								var fromDate ='';
								
								fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
								
								var toDate ='';
								
								toDate = ("0" + $scope.datePickerForToDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
								
								var URI = BASE_URL + ROOT_URL
										+ GETKITINDENTBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate 
										+ "&fromStore=" + $scope.selectedKitIndentFromStoreId + "&toStore=" + $scope.selectedKitIndentToStoreId +"&status=" + $scope.selectedKitIndentStatus;// search instrument 
								var data = {};

								// GET instrument list by search
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.kitIndentList = [];
											if (response.data.status == "success") {
												$scope.kitIndentList = response.data.listObject;
												console
														.log(
																"kitIndentList",
																$scope.kitIndentList);

											}
										});

							}*/
							
							
							//$scope.acceptRejectStatus = [];
							
							
							
							$scope.getKitIndentApprovalByMultipleFields = function() 
							{
								
								var fromDate ='';
								
								fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
								
								var toDate ='';
								
								toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
																								
								var kitIndentUrl = BASE_URL + ROOT_URL 
								+ GETKITINDENTAPPROVALBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
																
								
								if($scope.selectedKitIndentFromStoreId != undefined )
								{
									kitIndentUrl = kitIndentUrl + "&fromStore=" + $scope.selectedKitIndentFromStoreId; 
								}
								
								if($scope.selectedKitIndentToStoreId != undefined )
								{
									kitIndentUrl =  kitIndentUrl + "&toStore=" + $scope.selectedKitIndentToStoreId;
								}
							
								if($scope.selectedKitIndentStatus != undefined )
								{
									kitIndentUrl =  kitIndentUrl + "&status=" + $scope.selectedKitIndentStatus;
								}
								$scope.loadingMessage="Loading...";
								$scope.kitIndentList = [];
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", kitIndentUrl)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitIndentList = response.data.listObject;
												console
														.log(
																"kitIndentList",
																$scope.kitIndentList);

											}
											if($scope.kitIndentList.length==0)
												$scope.loadingMessage="No record found";
											$rootScope.stopLoader();
										});

							}
							
							
							//For Instrument Modal 
							
							$scope.indentKitInstrumentInfo = [];
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.indentKitInstrumentInfo = kitInfo;
							}
                            
                            
                            $scope.isDisabled = function()
							{
								if($scope.selectedIndentDetails.status == 'Pending' || $scope.selectedIndentDetails.status == 'Partially Approved')
									{
									return false;
									}
								
									return true;
									
							}
							
							
							
							//For Get Kit List By Indent Id
							$scope.getKitListByIndentNo = function(info){
								
								$scope.selectedIndentDetails = info;
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITLISTBYINDENTNO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&indentNo=" + info.indentNo;// search instrument 								
								
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
								+ CANCELKITINDENT ;// cancel kit indent 
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
							
							
							
							$scope.toRejectItem = {
                            		
                            		id : "",
                            		rejectReasonNote : "",
                            		rejectReasonId : ""
                            		
                            };
                            
                            $scope.toReject = function(kitInfo)
                            {
                            	$scope.toRejectItem.id = kitInfo.id;
                            	$scope.getReasonsForRejection();
                            }
							
							
							//For Reasons for rejection
							$scope.getReasonsForRejection = function(){
								var URI = BASE_URL + ROOT_URL
								+ GETREASONSTOREJECT;
								
                            	/*var URI = BASE_URL + ROOT_URL
								+ GETREASONSTOREJECT + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/ 
								 var data = {};

						
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.rejectReasonsList = [];
										if (response.status == "200") {
											$scope.rejectReasonsList = response.data;
											console
													.log(
															"rejectReasonsList",
															$scope.rejectReasonsList);
	
										}
									});
                            }
							
							
							
							$scope.rejectKit = function ()
                            {
                            	var URI = BASE_URL + ROOT_URL
								+ REJECTKIT ; 
								var data = {};

								$scope.toRejectItem.rejectReasonNote = $scope.rejectReasonNote;
								$scope.toRejectItem.rejectReasonId = $scope.rejectReasonId;
								
							GenericService
							.serviceAction("PUT", URI, $scope.toRejectItem)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Rejected Successfully...!!',{
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
							
							
							
							
							$scope.kitList = []
							
							$scope.onChangeChoice = function(kitInfo) {
								if($scope.kitList.indexOf(kitInfo) == -1) {
									$scope.kitList.push(kitInfo)
								}
							}
																
							
							$scope.toApproveItem = {
                            		
                            		id : ""                            		                          		
                            };
                            
                            
							$scope.setAcceptItem = function(kitInfo){
								$scope.toApproveItem.id = kitInfo.id;
							}
 							
							
							$scope.approveKit = function ()
                            {
                            	var URI = BASE_URL + ROOT_URL
								+ APPROVEKIT ; 
								var data = {};
                               																																
							GenericService
							.serviceAction("PUT", URI, $scope.toApproveItem)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Accepted Successfully...!!',{
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
							
							
							$scope.kitStatusCheck = function(status) {
								if(status == 'Rejected') {
									return true;
								}else {
									return false;
								}
							}
						
							
							$scope.init();
							
								
						
						}]);



