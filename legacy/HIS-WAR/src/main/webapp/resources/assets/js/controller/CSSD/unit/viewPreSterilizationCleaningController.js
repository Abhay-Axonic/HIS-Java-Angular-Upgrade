angular
		.module('myApp')
		.controller(
				'viewPreSterilizationCleaningController',
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
								
								if($stateParams.transitionNumber){
									$scope.viewPreKitDetails();
								}

							}
														
							$scope.viewPreKitDetails = function() {
						
								 data = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ VIEWPREKITBYTRANSITIONNUMBER + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&transitionNumber=" + $stateParams.transitionNumber;
								
								$rootScope.startLoader();
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.viewPreKitObj = {};
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.viewPreKitObj = response.data.object;
												console.log("viewPreKitObj",$scope.viewPreKitObj);
											}else{
												$rootScope.stopLoader();
											}
										});
							}
							
							$scope.init();
								
						}]);



