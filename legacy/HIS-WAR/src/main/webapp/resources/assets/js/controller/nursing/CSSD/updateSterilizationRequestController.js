angular
		.module('myApp')
		.controller(
				'updateSterilizationRequestController',
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
															
								
								$scope.issuingStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
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
								
								
															
								//For Indent priority
								/*var URI = BASE_URL + ROOT_URL
								+ "/cssd/getIndentPriorityList" + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
														
								GenericService
								.serviceAction("GET", URI)
								.then(
										function(response) {
								$scope.kitIndentPriorityList = [];

									if (response.data.status == "success") {
										$scope.kitIndentPriorityList = response.data.listObject;
										console
											.log(
													"kitIndentPriorityList",
													$scope.kitIndentPriorityList);

								}
																		
							});*/
								
								
								if($stateParams.requestNo){
									$scope.getSterilizationRequestByRequestNo();
																		
								}
								

							}
							
							
							$scope.isSearchKitDisabled = function(){
                            	if($scope.selectedRequestingStore && $scope.selectedIssuingStore){
                            		return false;
                            	} else {
                            		return true;
                            	}
                            }
							
							
							//For Search Kit
							
							$scope.kitDetailsList = [];
							
							$scope.searchKitByName = function() 
							{
								var URI = BASE_URL + ROOT_URL
								+ GETKITLISTFORSTERILIZATIONREQUEST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&requestingStoreId=" + $scope.selectedRequestingStore  + "&issueingStoreId=" + $scope.selectedIssuingStore + "&name=" + $scope.searchKitField;// search instrument	
																	
															
								var data = {};

								
								if($scope.searchKitField === "") {
									
									$scope.kitDetailsList = [] ;
								}
								
								else {
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.kitDetailsList = response.data.listObject;
												}
											});	
								}

							}
							
							
							
							$scope.selectedKit = {};
							$scope.tempKitData = [];
							
                            $scope.selectKit = function(kitInfo) {
                            	
                            	var index = $scope.tempKitData.indexOf(kitInfo);
                            	
                            	if(index != -1) {
									$scope.tempKitData.splice(index, 1);
								}
								else {
									$scope.tempKitData.push(kitInfo);
								}
                            	                            	                           	
                            	/*$scope.getPreSterilizationEquipmentList(kitInfo);*/
                            	
                            }	
							
                            $scope.kitList = [];
							
                            $scope.showKitInfo = function() {
                            	
                                             	
                            	/*$scope.selectedKit = $scope.tempKitData;*/ 
                            	
                            	angular.copy($scope.tempKitData,$scope.kitList);
                            	$scope.kitDetailsList = [];
                            	$scope.tempKitData = [];
                            	$scope.searchKitField = "";
                            	
                            }
                            
                            
							/*$scope.finalKitList = [];
                            $scope.kitList = [];
                            
                            $scope.showKitInfo = function() {
                            	             	
                            	for(var i = 0; i < $scope.kitDetailsList.length;i++)
                            	{
                            		if($scope.kitDetailsList[i].Selected)
                            		{
                            		    $scope.finalKitList.push($scope.kitDetailsList[i]);
                            		}
                            		
                            	}
                            	$scope.kitList = $scope.finalKitList;
                            	
                            	for(var i = 0;i<$scope.kitList.length;i++)
                        		{
                            		$scope.kitList[i].Selected = false; 
                        		}
                            	
                            }*/
							
                           
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.updatedSterilizationRequest();
							}
							
							
							
							$scope.sterilizationRequestObj = {
									
									updatedBy: $scope.userId,
									createdBy : $scope.userId,
									requestingStoreId : "",
									issueingStoreId: "",
									carrier: "",
									remark: ""
									
							};
							
							
							$scope.finalKitList = [];
                            
                            $scope.selectKitsToRequest = function(data){
                            	
                            	var index = $scope.finalKitList.indexOf(data);
                            	
                            	if(index != -1) {
									$scope.finalKitList.splice(index, 1);
								}
								else {
									$scope.finalKitList.push(data);
								}
                            }
							
							
							
							
							$scope.updateSterilizationRequest = function() {
								$scope.$broadcast('show-errors-check-validity');
									
								
								
								if ($scope.updateSterilizationRequestForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ UPDATESTERILIZATIONREQUEST;
									
											
											$scope.sterilizationRequestObj.updatedBy = $scope.userId;
											$scope.sterilizationRequestObj.createdBy = $scope.userId;
											$scope.sterilizationRequestObj.kitList = $scope.finalKitList;
											$scope.sterilizationRequestObj.organizationId = $scope.organizationId;
											$scope.sterilizationRequestObj.unitId = $scope.unitId;
											$scope.sterilizationRequestObj.requestingStoreId = $scope.selectedRequestingStore;
											$scope.sterilizationRequestObj.issueingStoreId = $scope.selectedIssuingStore;
											$scope.sterilizationRequestObj.remark = $scope.selectedRemark;
											
											for( var i = 0;i < $scope.sterilizationRequestObj.kitList.length; i++ ){
												delete $scope.sterilizationRequestObj.kitList[i].Selected;
											}
											
											
											GenericService
												.serviceAction("POST",URI,$scope.sterilizationRequestObj)
												.then(function(response) {
															if (response.data.status == "success") {
																
																growl.success('Sterilization Request Updated Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$location.path('/sterilizationRequest');
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
								}
							
							
							
							$scope.kitReturnListByReturnNo = [];
							
							$scope.getSterilizationRequestByRequestNo = function() {
						
								data = {};
								
								var URI = BASE_URL + ROOT_URL
								+ GETSTERILIZATIONREQUESTBYREQUESTNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&requestNo=" + $stateParams.requestNo;
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITRETURNBYRETURNNO  + '?organizationId=' + $scope.organizationId + '&unitId=' + $scope.unitId + '&requestNo=' + $stateParams.requestNo;*/
						
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.sterilizationRequestObj = response.data.object;
												$scope.selectedRequestingStore = $scope.sterilizationRequestObj.requestingStoreId;
												$scope.selectedIssuingStore = $scope.sterilizationRequestObj.issueingStoreId;
												$scope.selectedRemark = $scope.sterilizationRequestObj.remark;
												$scope.selectedCarrier = $scope.sterilizationRequestObj.carrier;
												$scope.kitList = $scope.sterilizationRequestObj.kitList;
											}
											
										});
							}
							
							
							
							
													
							
							$scope.init();
							
													
						}]);



