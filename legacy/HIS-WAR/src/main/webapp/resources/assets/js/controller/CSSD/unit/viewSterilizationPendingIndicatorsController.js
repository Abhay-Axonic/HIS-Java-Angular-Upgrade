angular
		.module('myApp')
		.controller(
				'viewSterilizationPendingIndicatorsController',
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
								
								/******For initial cssd store selection*********/
								
								var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								if (cssdStoreObj == undefined) {
									$rootScope.cssdStorePopup();
									
								}
								
								$scope.storeId = cssdStoreObj.storeId;
								
								/***********************************************/
								
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
								$scope.sterilizationColorObj={"colorId":0};
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEAUTOCLAVESLIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId;
								var data = {};
								// For Get Auto Clave List Using Offset
								$rootScope.startLoader();
								GenericService.serviceAction("GET", URI,data)
								.then(function(response) {
									$scope.autoClaveList = [];
									
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.autoClaveList = response.data.listObject;
										console.log("autoClaveList",$scope.autoClaveList);
									}else{
										$rootScope.stopLoader();
									}
								});
								
								$scope.viewPendingIndicatorsTemp = false;
	                           var data={};
								var URI1 = BASE_URL + ROOT_URL + GETACTIVESTERILIZATIONCOLOR;
								GenericService.serviceAction("POST", URI1,data)
									.then(function(response) {
									$scope.sterilizationColorList = [];

									if (response.data.status == "success") {
										$scope.sterilizationColorList = response.data.listObject;
										console.log("sterilizationColorList",$scope.sterilizationColorList);

										}
									});
								
								if($stateParams.batchNo){
									$scope.viewPendingIndicatorsTemp = true;
									$scope.getBatchDetailsByBatchNumber();
								}
							}
												
							$scope.getBatchDetailsByBatchNumber = function() {
						
								 data = {};
								 $scope.selectedAutoclave = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ GETBATCHDETAILSBYBATCHNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&batchNo=" + $stateParams.batchNo;
								
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.viewBatchObj = {};
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.viewBatchObj = response.data.object;
												$scope.selectedMachine = $scope.viewBatchObj.autoclaveNumber;
												
												console.log("viewBatchObj",$scope.viewBatchObj);
												for(var i = 0;i < $scope.autoClaveList.length;i++)
												{
												if($scope.autoClaveList[i].id == $scope.selectedMachine)
													{
														$scope.selectedAutoclave = $scope.autoClaveList[i];
														break;
													}
												}
											}
										});
							}
							
							$scope.viewBatchObj = {
									updated_by: $scope.userId,
									//batchNumber : "",
									/*created_by : $scope.userId,*/
                            };
							
							//To Save Pending Indicators
							$scope.savePendingIndicators = function() {
								$scope.$broadcast('show-errors-check-validity');
								$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + SAVEPENDINGINDICATORS;
									        if($scope.sterilizationColorObj.colorName!=undefined){
									         $scope.viewBatchObj.color=$scope.sterilizationColorObj.colorName;
									        }
											$scope.viewBatchObj.batchNumber = $stateParams.batchNo; 
											$scope.viewBatchObj.updated_by = $scope.userId;
											$scope.viewBatchObj.organizationId = $scope.organizationId;
											$scope.viewBatchObj.unitId = $scope.unitId;
											
											if($scope.viewBatchObj.isSterilized==undefined){
												$scope.viewBatchObj.isSterilized=false;
											}
											//return false;
											GenericService
												.serviceAction("POST",URI,$scope.viewBatchObj)
												.then(function(response) {
															if (response.data.status == "success") {
																$('.modal-backdrop').remove();
																NavigteToSterilizationPendingIndicators();
																$rootScope.stopLoader();
																growl.success('Pending Indicator Saved Successfully...!!',{title : 'Success!'});
																$scope.$broadcast('show-errors-reset');
															} else {
																$rootScope.stopLoader();
																growl.error('Something Went Wrong !',{title : 'Error!'});
															}
														});
								}
							
							var NavigteToSterilizationPendingIndicators = function(){
								$state.go('sterilizationPendingIndicators')
							}						
							
							$scope.init();
						
						}]);



