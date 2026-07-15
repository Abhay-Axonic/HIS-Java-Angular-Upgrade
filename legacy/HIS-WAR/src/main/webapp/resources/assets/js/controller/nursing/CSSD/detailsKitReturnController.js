angular
		.module('myApp')
		.controller(
				'detailsKitReturnController',
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
								
								
								if($stateParams.returnNo){
									$scope.viewKitReturnDetails();
																		
								}

							}
							
														
							$scope.viewKitReturnDetails = function() {
						
								 data = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ VIEWKITBYRETURNNO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&returnNo=" + $stateParams.returnNo;
																													
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.viewKitReturnObj = {};
											if (response.data.status == "success") {
												$scope.viewKitReturnObj = response.data.object;
												console
														.log(
																"viewKitReturnObj",
																$scope.viewKitReturnObj);

											}
										});
							}
							
							$scope.viewKitIssueObj = {};
							$scope.sterilizedKitsList = [];
                            $scope.classNameForSterilizedKits = "";
                            
                            
                            $scope.showSterilizedKitListRow = function (data){
                            	
                            	$scope.subKitList = data.subKitList;
                            	
                            	$scope.classNameForSterilizedKits = ".subKitRow-"+ data.kitId;
                            	angular.element($scope.classNameForSterilizedKits).toggle();
                            	//document.getElementById("indentNo-"+info.indentNo).toggle();
                            	
                            	for(var i = 0; i < $scope.viewKitIssueObj.kitList.length; i++)
                            	{
                            		if(angular.element("subKitRow-"+$scope.viewKitIssueObj.kitList[i].kitId)[0] != undefined &&  angular.element("subKitRow-"+$scope.viewKitIssueObj.kitList[i].kitId)[0].style.display != "none" && $scope.viewKitIssueObj.kitList[i].kitId != data.kitId )
                            			{
                            				angular.element("subKitRow-"+$scope.viewKitIssueObj.kitList[i].kitId).toggle();
                            			}
                            		
                            	}
                            	
                            	
                            	
                            }
							
                            
                            $scope.indentKitInstrumentInfo = [];
                            $scope.getInstrumentInfo = function(data) {
								$scope.indentKitInstrumentInfo = data;
							}
													
							
							$scope.init();
							
							  		
								
						
						}]);



