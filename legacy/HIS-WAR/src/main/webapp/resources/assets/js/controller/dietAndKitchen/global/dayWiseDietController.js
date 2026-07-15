'use strict';


angular
		.module('myApp')
		.controller(
				'dayWiseMasterController',
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
						/*'$location'*/
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {

							/* init() function for form object create. */

							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
							/*	var url = $location.path().split('/');
							    $scope.firstParameter = url[2];*/
							   
								
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.onEditNutrition = false;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
					
								var data = {};
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETTEMPLATELIST;*/

								var URI = BASE_URL + ROOT_URL
										+ GETTEMPLATELIST + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								// For Get Diet List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.dietList = [];

													if (response.data.status == "success") {
														$scope.dietList = response.data.listObject;
														console
																.log(
																		"dietList",
																		$scope.dietList);

													}
												});
								
								
						
							}

							$scope.init();

							
							$scope.getDietValues = function(dietTemplateId) {
								 	var data = {};
									var URI = BASE_URL + ROOT_URL
								+ GETDIETTEMPLATEBYID + '?dietTemplateId=' + dietTemplateId + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
						// For Get Diet List Using Offset
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.dietTemplate = response.data.object;
												console
														.log(
																"dietTemplateList",
																$scope.dietTemplateList);

											}
										});
							};
							
							$scope.getNutritionValues = function(dietTemplateId) {
							 	var data = {};
								var URI = BASE_URL + ROOT_URL
							+ GETDAYWISENUTRITIONLIST + '?dietTemplateId=' + dietTemplateId;
					// For Get Diet List Using Offset
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										if (response.data.status == "success") {
											$scope.nutritionList = response.data.listObject;
											console
													.log(
															"nutritionList",
															$scope.nutritionList);

										}
									});
						};
							
							
							
						} ]);
