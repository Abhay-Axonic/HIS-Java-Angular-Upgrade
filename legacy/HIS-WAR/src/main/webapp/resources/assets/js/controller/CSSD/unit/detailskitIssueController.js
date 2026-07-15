angular
		.module('myApp')
		.controller(
				'detailskitIssueController',
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
								
								
								if($stateParams.issueNo){
									$scope.viewKitissueDetails();
																		
								}

							}
							
														
							$scope.viewKitissueDetails = function() {
						
								 data = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ VIEWKITBYISSUENO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&issueNo=" + $stateParams.issueNo;
																													
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.viewKitIssueObj = {};
											if (response.data.status == "success") {
												$scope.viewKitIssueObj = response.data.object;
												console
														.log(
																"viewKitIssueObj",
																$scope.viewKitIssueObj);

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
							
													
							
							$scope.init();
							
							  		
								
						
						}]);



