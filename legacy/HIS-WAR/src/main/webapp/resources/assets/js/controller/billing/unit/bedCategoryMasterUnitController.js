
'use strict';

/**
 * @Author
 * @name myApp.controller:bedCategoryMasterUnitController
 * @description #bedCategoryMasterUnitController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'billingBedCategoryMapperController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {

							/* init() function for form object create. */

							$scope.init = function() {
							
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

							

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								
								

								var data = {};
								
								var commonobj = {
										organizationId : $scope.organizationId
								}						

								var URI = BASE_URL + ROOT_URL
										+ "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.billingBedcategoryList = [];

													if (response.data.status == "success") {
														$scope.billingBedcategoryList = response.data.listObject;

													}
												});
								
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.unitBillingBedcategoryList = [];

											if (response.data.status == "success") {
												$scope.unitBillingBedcategoryList = response.data.listObject;
												/*$scope.billingBedcategoryFinalList = [];
												var i = 0;
												angular.forEach($scope.billingBedcategoryList, function( value, index) {
													alert($scope.unitBillingBedcategoryList.indexOf(value));
													if($scope.unitBillingBedcategoryList.indexOf(value) == -1) {
														  alert('already exists!');
														}
													else
														{
														alert('3333');
														$scope.billingBedcategoryFinalList.push($scope.billingBedcategoryList[i]);
														}
													i++;
													});*/
												//angular.extend($scope.billingBedcategoryList, $scope.unitBillingBedcategoryList);
												$scope.billingBedcategoryList = $scope.billingBedcategoryList.concat($scope.unitBillingBedcategoryList);
												$scope.billingBedcategoryFinalList = [];
												angular.forEach($scope.billingBedcategoryList, function(value, key) {
													var exists = false;
													angular.forEach($scope.billingBedcategoryFinalList, function(val2, key) {
														if(angular.equals(value.billingBedCategoryId, val2.billingBedCategoryId)){ exists = true }; 
													});
													if(exists == false && value.billingBedCategoryId != "") { $scope.billingBedcategoryFinalList.push(value); }
												});
											  //return newArr;
												
												
											}
										});
						
						
						
								
								$scope.servicesSelectedList = {
									    services: []
									  };
								$scope.servicesSelectedList1 = {
									    services1: []
									  };

							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveBillingBedMapper();
							}

							// Function For  saveBillingBed
							$scope.saveBillingBed = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
							  }
						
						} ]);
