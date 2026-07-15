angular
		.module('myApp')
		.controller(
				'detailsSendtoMicrobiologyController',
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
								
								var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								if (cssdStoreObj != undefined) {
								$scope.storeId = cssdStoreObj.storeId;
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
								
								
								if($stateParams.batchNo){
									$scope.getBatchDetailsByBatchNumber();
									//$scope.viewPreKitEquipDetails();
									
								}

							}
							
														
							$scope.getBatchDetailsByBatchNumber = function() {
								
								$rootScope.startLoader();			
								 data = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ GETBATCHDETAILSBYBATCHNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId="+ $scope.storeId + "&batchNo=" + $stateParams.batchNo;
																								
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.viewMicrobiologyObj = {};
											if (response.data.status == "success") {
												$scope.viewMicrobiologyObj = response.data.object;
												$rootScope.stopLoader();
												console.log("viewMicrobiologyObj",$scope.viewMicrobiologyObj);

											}
										});
							}
							
							
													
							
							$scope.init();
							
							  		
								
						
						}]);



