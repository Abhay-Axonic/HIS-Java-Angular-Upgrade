angular
		.module('myApp')
		.controller(
				'sterilizationDirectIssueController',
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
															
								
								$scope.kitReturnFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								//For Requesting Store
								
							/*	var URI = BASE_URL + ROOT_URL
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
								
								
								if($stateParams.indentNo){
									$scope.getKitIndentByIndentNo();
																		
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
							$scope.loadingMsg="";
							$scope.showLoadingMsg='N';
							$scope.searchKitByName = function() 
							{
								$scope.showLoadingMsg='Y';
								$scope.loadingMsg="Loading...";
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
													if($scope.kitDetailsList.length==0){
														$scope.loadingMsg="No record found";
													}
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
                            	
                                   $scope.showLoadingMsg='N';          	
                            	/*$scope.selectedKit = $scope.tempKitData;*/ 
                            	
                            	angular.copy($scope.tempKitData,$scope.kitList);
                            	$scope.kitDetailsList = [];
                            	$scope.tempKitData = [];
                            	$scope.searchKitField = "";
                            	
                            }
                            
                            
                            $scope.removeKit = function(kitInfo){
                            	var index = $scope.kitList.indexOf(kitInfo);
                            	if(index != -1)
                            	{
                            		$scope.kitList.splice(index, 1);
                            	}
                            }
                            
                            
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveSterilizationRequest();
							}
							
							
							
							$scope.kitInstrumentInfo = [];
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.kitInstrumentInfo = kitInfo;
							}
							
							
							
							
							$scope.sterilizationDirectRequestObj = {
									
									updatedBy: $scope.userId,
									createdBy : $scope.userId,
									requestingStoreId : "",
									issueingStoreId: "",
									carrier: "",
									remark: ""
									
							};
							
							
							 $scope.isSaveDisabled = function() {
	                                var flag = 0; 
								 
								 	if( $scope.selectedRequestingStore == undefined ){
	                                	flag = 1;
	                                }
	                                if($scope.selectedIssuingStore == undefined ){
	                                	flag = 1;
	                                }
	                                if($scope.sterilizationDirectRequestObj.carrier == undefined ){
	                                	flag = 1;
	                                }
	                            	if($scope.kitList != undefined && $scope.kitList.length < 1)
	                                {
	                                	flag = 1;
	                                }
	                            	if( flag == 1 ){
	                            		return true;
	                            	}else{
	                            		return false;
	                            	}
	                            	
	                             }
								
							
							
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
							
							
							
							
							$scope.saveSterilizationRequest = function() {
								$scope.$broadcast('show-errors-check-validity');
							
								if ($scope.sterilizationDirectRequestForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ SAVESTERILIZATIONREQUEST;
									
											$rootScope.startLoader();
											
											$scope.sterilizationDirectRequestObj.updatedBy = $scope.userId;
											$scope.sterilizationDirectRequestObj.createdBy = $scope.userId;
											$scope.sterilizationDirectRequestObj.kitList = $scope.kitList;
											$scope.sterilizationDirectRequestObj.organizationId = $scope.organizationId;
											$scope.sterilizationDirectRequestObj.unitId = $scope.unitId;
											$scope.sterilizationDirectRequestObj.requestingStoreId = $scope.selectedRequestingStore;
											$scope.sterilizationDirectRequestObj.issueingStoreId = $scope.selectedIssuingStore;
											
											for( var i = 0;i < $scope.sterilizationDirectRequestObj.kitList.length; i++ ){
												delete $scope.sterilizationDirectRequestObj.kitList[i].Selected;
											}
											
											
											GenericService
												.serviceAction("POST",URI,$scope.sterilizationDirectRequestObj)
												.then(function(response) {
															if (response.data.status == "success") {
																$('.modal-backdrop').remove();
																NavigteToSterilizationRequest();
																$rootScope.stopLoader();
																growl.success('Sterilization Direct Request Saved Successfully...!!',{title : 'Success!'});
																$scope.$broadcast('show-errors-reset');
															} else {
																$rootScope.stopLoader();
																growl.error('Something Went Wrong!!!',{title : 'Error!'});
															}
														});
									    
									}
								}
							
							var NavigteToSterilizationRequest = function(){
								$state.go('sterilizationRequest');
							}
														
							
													
							
							$scope.init();
							
													
						}]);



