angular
		.module('myApp')
		.controller(
				'detailsSterilizationRequestController',
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
											$scope.viewSterRequestObj = {};
											if (response.data.status == "success") {
												$scope.viewSterRequestObj = response.data.object;
												console
														.log(
																"viewSterRequestObj",
																$scope.viewSterRequestObj);

											}
										});
							}
							
							
							$scope.kitInstrumentInfo = [];
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.kitInstrumentInfo = kitInfo;
							}
							
							
							
							
							$scope.init();
							
							  		
								
						
						}]);



