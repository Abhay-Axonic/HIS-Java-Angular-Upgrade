angular
		.module('myApp')
		.controller(
				'detailsSterilizationRequestAcceptanceController',
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
						'$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams, $location) {
							
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
								
								$scope.getReasonsForRejection();
								
								if($stateParams.requestNo){
									$scope.getSterilizationRequestByRequestNumber();
																		
								}

							}
							
														
							$scope.getSterilizationRequestByRequestNumber = function() {
								
								 data = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ GETSTERILIZATIONREQUESTBYREQUESTNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&requestNo=" + $stateParams.requestNo;
																													
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.viewSterRequestAcceptanceObj = {};
											if (response.data.status == "success") {
												$scope.viewSterRequestAcceptanceObj = response.data.object;
												console
														.log(
																"viewSterRequestAcceptanceObj",
																$scope.viewSterRequestAcceptanceObj);

											}
										});
							}
							
							
							                           
                                
                            $scope.kitInstrumentInfo = [];
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.kitInstrumentInfo = kitInfo;
							}
                           
                            
                            
                            $scope.toRejectItem = {
                            		
                            		id : "",
                            		rejectReasonNote : "",
                            		rejectReasonId : ""
                            		
                            };
                            
                            $scope.currentRejectItem = {};
                            
                            $scope.toReject = function(kitInfo)
                            {
                            	if(kitInfo.reject)
                            	{
                            		$scope.currentRejectItem = kitInfo;
                            	}
                            	
                            }
                            
                            
                            
                            $scope.setRejectIdandNote = function()
                            {
                            	var index = $scope.viewSterRequestAcceptanceObj.kitList.indexOf($scope.currentRejectItem);
                            	
                            	$scope.viewSterRequestAcceptanceObj.kitList[index].rejectReasonId = $scope.rejectReasonId;
                            	$scope.viewSterRequestAcceptanceObj.kitList[index].rejectReasonNote = $scope.rejectReasonNote;
                            	
                            }
							
							
							//For Reasons for rejection
							$scope.getReasonsForRejection = function(){
								var URI = BASE_URL + ROOT_URL
								+ GETREASONSTOREJECT;
								                            	
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
							
							
							
														
							$scope.isDisabled = function(kitDetails){
								if(kitDetails.isAccepted == true || kitDetails.isRejected == true){
									return true;
								}else {
									return false;
								}
							}
							
							
							$scope.checkPhyQty = function(instrumentInfo)
							{
								if( instrumentInfo.physicalQuantity != undefined && instrumentInfo.physicalQuantity != instrumentInfo.quantity)
								{
									if( confirm("Physical quantity is different than defined quantity. Do you want to proceed?") == false ) {
										instrumentInfo.physicalQuantity = null;
									} 
								}
							}
							
							
							$scope.acceptSterilizationRequest = function() {
								$scope.$broadcast('show-errors-check-validity');
								
								$scope.viewSterRequestAcceptanceObj.organizationId = $scope.organizationId;
								$scope.viewSterRequestAcceptanceObj.unitId = $scope.unitId;
								$scope.viewSterRequestAcceptanceObj.createdBy = $scope.userId;
								
								
									var URI = BASE_URL + ROOT_URL
									+ ACCEPTSTERREQ;
											GenericService
												.serviceAction("PUT",URI,$scope.viewSterRequestAcceptanceObj)
												.then(function(response) {
															if (response.data.status == "success") {
																
																growl.success('Sterilization Request Acceptance Saved Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$location.path('/sterilizationRequestAcceptance');
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl.error(
																				'Something Went Wrong!!!',
																				{
																					title : 'Error!'
																				});
															}
														});
									    
								}
							
							
							
							$scope.init();
							
							  		
								
						
						}]);



