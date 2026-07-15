angular
		.module('myApp')
		.controller(
				'viewSterilizationLoadBatchController',
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
								
								
								if($stateParams.batchNo){
									$scope.getBatchDetailsByBatchNumber();
								}
							}
														
							$scope.getBatchDetailsByBatchNumber = function() {
						
								 data = {};
								 
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
												console.log("viewBatchObj",$scope.viewBatchObj);
											}else{
												$rootScope.stopLoader();
											}
										});
							}
							
							$scope.init();
						}]);



