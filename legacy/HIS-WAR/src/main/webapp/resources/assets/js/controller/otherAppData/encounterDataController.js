
/**
 * @author Suraj kumbhoje
 */

angular
		.module('myApp')
		.controller(
				"encounterDataController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						'reportService',
						'$stateParams',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,reportService,$stateParams) {

							$rootScope.loginpage = true;
							
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.labResultTestDtls;
							
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.getEncounterData = function(pid) {
								try {
									
									$scope.encounterList = [];
									$rootScope.startLoader();
									var data ={
											"pid":pid,
										}
									var URI = BASE_URL + ROOT_URL+OTHER_ENCOUNER_LIST;

									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response)
													{
														if (response.data.status == 'success')
														{
														  $scope.encounterList = response.data.listObject;
														  $rootScope.stopLoader();
														}
														else
														 {
															$rootScope.stopLoader();
														 }
														
													});
								} catch (e) {
									console.log(e.message)
								}

							}
							if($stateParams.pid==null)
 							{
								$state.go('doctorDeskController');
							} else {
								$scope.getEncounterData($stateParams.pid);
							}
							
							 $scope.getEncounterDetails = function(encounterId) {
									try {
										
										if(encounterId!=null)
											{
											  $state.go('encounterDataDetails',
													{
												        encounterId : encounterId
													});
											}
									} catch (e) {
										console.log(e.message)
									}

								}
							 $scope.dateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
								};

								$scope.open = function() {
									$scope.popup.opened = true;
								};

								$scope.popup =
								{
									opened : false
								};
								
								$scope.popup1 =
								{
									opened : false
								};
								
								$scope.open1 = function() 
								{
									$scope.popup1.opened = true;
								};
								$scope.fromDate = undefined;
							    $scope.toDate = undefined;
							    $scope.encNumber = '';
							    
							    $scope.searchEncounter = function()
							    {
							    	try {
										
										$scope.encounterList = [];
										$rootScope.startLoader();
										var data ={
												"pid":$stateParams.pid,
												"fromDate":$scope.fromDate==undefined?"":moment($scope.fromDate).format('DD-MM-YYYY'),
												"toDate":$scope.toDate==undefined?"":moment($scope.toDate).format('DD-MM-YYYY'),
												"encounterId":$scope.encNumber,
											}
										var URI = BASE_URL + ROOT_URL+OTHER_ENCOUNER_LIST;

										console.log("URI", URI);
										GenericService
												.serviceAction("POST", URI, data)
												.then(
														function(response)
														{
															if (response.data.status == 'success')
															{
															  $scope.encounterList = response.data.listObject;
															  $rootScope.stopLoader();
															}
															else
															 {
																$rootScope.stopLoader();
															 }
															
														});
									} catch (e) {
										console.log(e.message)
									}
							    }
						
						} 				
						]);