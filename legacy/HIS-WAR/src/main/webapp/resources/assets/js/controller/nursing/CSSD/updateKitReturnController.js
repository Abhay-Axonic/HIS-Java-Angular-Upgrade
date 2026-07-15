angular
		.module('myApp')
		.controller(
				'updateKitReturnController',
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
								
								
								$scope.dummyKitIndentObj = {
										/*code : "",
										name : "",
										surgeon: "",
										kitIndentList: [],
										createdby : $scope.userId,
										updated_by : $scope.userId*/
									};

								$scope.kitIndentObj = $scope.dummyKitIndentObj;
								
								
								
								
								
								
															
								
								$scope.kitReturnFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								//For From Store By User Id
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&userId=" + $scope.userId;
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + '/' + $scope.userId;*/
								
								GenericService
								.serviceAction("GET", URI, $scope.kitReturnFromStoreObj)
								.then(
										function(response) {
								$scope.kitReturnFromStoreList = [];

									if (response.data.status == "success") {
										$scope.kitReturnFromStoreList = response.data.listObject;
										console
											.log(
													"kitReturnFromStoreList",
													$scope.kitReturnFromStoreList);

								}
																		
							});
								
								
								//For To Store By CSSD
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE;*/
								
								GenericService
								.serviceAction("GET", URI, $scope.storeMasterObj)
								.then(
										function(response) {
								$scope.kitReturnToStoreList = [];

									if (response.data.status == "success") {
										$scope.kitReturnToStoreList = response.data.listObject;
										console
											.log(
													"kitReturnToStoreList",
													$scope.kitReturnToStoreList);

								}
																		
							});
								
								
															
								//For Indent priority
								var URI = BASE_URL + ROOT_URL
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
																		
							});
								
								
								if($stateParams.returnNo){
									$scope.getKitReturnByReturnNo();
																		
								}
								

							}
							
							
							//For Search Kit For Indent
							
                            $scope.kitDetailsList = [];
							
							$scope.searchKitByName = function() 
							{
								var URI = BASE_URL + ROOT_URL
								+ GETKITLISTFORKITRETURN + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&fromStoreId=" + $scope.selectedFromStoreId + "&toStoreId=" + $scope.selectedToStoreId + "&name=" + $scope.searchKitField;// search instrument
								
								/*var URI = BASE_URL + ROOT_URL
										+ SEARCHKITBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&name=" + $scope.searchKitField;*/ 
								
								
								var data = {};

								// GET instrument list by search
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
							
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveKitIndent();
							}
							
							
							
														
							
							//For View Pre Kit Details
							/*$scope.list = [];
                           							
							$scope.getPreSterilizationEquipmentList = function(kitInfo) 
							{
																
								$scope.list = kitInfo.kitInstrumentList;
							}*/
							
							
							//For Selected Kit Details
							$scope.selectedKit = {};
							$scope.tempKitData = [];
							
                            $scope.selectKitInfo = function(kitInfo) {
                            	
                            	var index = $scope.tempKitData.indexOf(kitInfo);
                            	
                            	if(index != -1) {
									$scope.tempKitData.splice(index, 1);
								}
								else {
									$scope.tempKitData.push(kitInfo);
								}
                            	                            	                           	
                            	/*$scope.getPreSterilizationEquipmentList(kitInfo);*/
                            	
                            }		
                            
                            $scope.subKitList = [];
							
                            $scope.showKitInfo = function() {
                            	
                                             	
                            	/*$scope.selectedKit = $scope.tempKitData;*/ 
                            	
                            	
                            	$scope.subKitList = $scope.tempKitData;
                            	
                            }
							                              
                           
                           //For Instrument Modal 
                            $scope.getInstrumentInfo = function(data) {
								$scope.indentKitInstrumentInfo = data;
							}
							
							
                            
                          //To Remove kit
                            $scope.remove = function(index) {
								var newDataList = [];
								var le = $scope.subKitList.length;
								var i = 1;
								if (le != i) {
									angular.forEach(
											$scope.subKitList,
											function(v) {
												if (index != i) {
													newDataList.push(v);
												}
												i++;
											});

									$scope.subKitList = newDataList;
								}
							};
                            
                            
							$scope.finalKitList = [];
                            
                            $scope.selectKitsToReturn = function(data){
                            	
                            	var index = $scope.finalKitList.indexOf(data);
                            	
                            	if(index != -1) {
									$scope.finalKitList.splice(index, 1);
								}
								else {
									$scope.finalKitList.push(data);
								}
                            }
							
							
							$scope.kitReturnObj = {
									
									updatedBy: $scope.userId,
									createdBy : $scope.userId,
									fromStoreId : "",
									toStoreId: "",
									carrier: "",
									remark: ""
									
							};
							
							
							$scope.selectedKitIndents = [];
							
							//Function for editing the entries
							$scope.editKitIndent = function(kitObj) {
								$scope.kitIndentObj = kitObj;
								$scope.kitIndentObj.id = kitObj.id;
								$scope.selectedKitIndents = kitObj.kitIndentList;
								$scope.kitIndentObj.updatedBy = $scope.userId;
								$scope.kitIndentObj.createdBy = $scope.userId;
								$scope.onEditKitIndent = true;
							}
							
							
							
							$scope.updateKitReturn = function() {
								$scope.$broadcast('show-errors-check-validity');
									
								
								
								if ($scope.updateKitReturnForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ UPDATEKITRETURN;
									
									/*if ($scope.onEditKitReturn) {
										$scope.updateKitReturn()
									} else { 
										if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
	
										} else {*/
											// $scope.data = {};
											
											$scope.kitReturnObj.updatedBy = $scope.userId;
											$scope.kitReturnObj.createdBy = $scope.userId;
											$scope.kitReturnObj.subKitList = $scope.finalKitList;
											$scope.kitReturnObj.organizationId = $scope.organizationId;
											$scope.kitReturnObj.unitId = $scope.unitId;
											$scope.kitReturnObj.fromStoreId = $scope.selectedFromStoreId;
											$scope.kitReturnObj.toStoreId = $scope.selectedToStoreId;
											$scope.kitReturnObj.remark=$scope.selectedRemark;
											
											GenericService
												.serviceAction("POST",URI,$scope.kitReturnObj)
												.then(function(response) {
															if (response.data.status == "success") {
																
																growl.success('Kit Return Updated Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$location.path('/KitReturn');
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl.error(
																				'Something Went Wrong...!!',
																				{
																					title : 'Error!'
																				});
															}
														});
									     // }
									  // }
									}
								}
							
							
							
							// Function For update Kit
							/*$scope.updateKitIndent = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.kitIndentForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ GETKITINDENTBYINDENTNO + '?indentNo=' + $stateParams.indentNo;
									$scope.kitIndentObj = $scope.selectedKitIndents;
									GenericService.serviceAction("PUT", URI, $scope.kitIndentObj)
									.then(function(response) {
										if (response.data.status == "success") {
										growl.success('Kit Indent updated successfully...!!',{
												title : 'Success!'
											});
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											growl.error('Updated Kit ndent already exits...!!',{
												title : 'Error!'
											});
										}
									});
								}
							}*/
							
							
							$scope.kitReturnListByReturnNo = [];
							
							$scope.getKitReturnByReturnNo = function() {
						
								data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETKITRETURNBYRETURNNO  + '?organizationId=' + $scope.organizationId + '&unitId=' + $scope.unitId + '&returnNo=' + $stateParams.returnNo;
						
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitReturnObj = response.data.object;
												$scope.selectedFromStoreId = $scope.kitReturnObj.fromStoreId;
												$scope.selectedToStoreId = $scope.kitReturnObj.toStoreId;
												$scope.selectedRemark = $scope.kitReturnObj.remark;
												$scope.selectedKitReturnCarrier = $scope.kitReturnObj.carrier;
												$scope.subKitList = $scope.kitReturnObj.subKitList;
											}
											
										});
							}
							
							
						
							
							$scope.init();
							
													
						}]);



