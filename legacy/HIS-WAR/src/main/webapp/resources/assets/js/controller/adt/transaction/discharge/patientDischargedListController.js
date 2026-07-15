'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:readyForBillingController
 * @description #readyForBillingController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'patientDischargedListController',
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
						'$stateParams',
						'BillingGenericService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,BillingGenericService) {

							
							
							$scope.fromDateOptions =  {
						          formatYear: 'yyyy',
						          showWeeks: false,
						          maxDate: new Date()
						        },
						       $scope.toDateOption = {
						          formatYear: 'yyyy',
						          showWeeks: false,
						          maxDate: new Date()
						        }
						        
							
							$scope.OpenFromDate = function () {
							     $scope.fromDateOpened = true;
							    }

							$scope.OpenToDate = function () {
							      $scope.toDateOpened = true;
							    }
							    
							    
						        
							$scope.init = function() {
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								$scope.visitTypeId = 2;

								
								var appConfigObject = $cookies.getObject('cookieObjectAppConfig');
								$scope.radioLogySpecialityId = appConfigObject.radiologySpecialityId;
								
								$scope.roleName = appConfigObject.roleName;
								$scope.nurseRole = appConfigObject.nurseRole;	 
								$scope.doctorRole = appConfigObject.doctorRole;
								$scope.isShowPatientByBed=appConfigObject.isShowPatientByBed;
								
								var labConfig  =    $scope.checkLabConfig();
								$scope.labSpecialityId = labConfig.labSpecialityId;
								
								
								$rootScope.loginpage = true;
								
								$scope.search = {};
								$scope.search.fromDate = new Date();
								$scope.search.toDate = new Date();
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDOCTORLIST;
								GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.doctorsList = [];

											if (response.data.status == "success") {
												$scope.doctorsList = response.data.listObject;

											}
										});
								
								
								  var URI = BASE_URL + ROOT_URL + GETACTIVEFLOORLIST;
								  
	                                GenericService.serviceAction("POST", URI, commonObj).then(function(response) {
	                                    $scope.floorList = [];

	                                    if (response.data.status == "success") {
	                                        $scope.floorList = response.data.listObject;

	                                    }
	                                });
								
							     
							}
							
							
                            
							$scope.searchDischargedPatientList = function(){
							
							console.log($scope.search);
							var visitTypeIds = [];
							if($scope.visitTypeId == 5){
								visitTypeIds.push(2);
								visitTypeIds.push(3);
								visitTypeIds.push(4);
								$scope.search.visitTypeIds = visitTypeIds;
							}else{
								visitTypeIds.push($scope.visitTypeId);
								$scope.search.visitTypeIds = visitTypeIds;
							}
							$scope.search.organizationId = $scope.organizationId;
							$scope.search.unitId = $scope.unitId;
							console.log($scope.search);
							//return false;
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL
									+ "/adt/getPatientDischargedListForPrint";
							GenericService
									.serviceAction("POST", URI, $scope.search)
									.then(
											function(response) {
												$scope.listOfAdmittedPatientList = [];
												$scope.admittedPatientList = [];

												if (response.data.status == "success") {
													$scope.admittedPatientList = response.data.listObject;
													$rootScope.stopLoader();
												}else{
													$rootScope.stopLoader();
												}
											});
						}
							
							 $scope.searchGroupList = function(keyword) {

									var data = {
										patientName : keyword,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									}
									console.log(data);
									//$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ PATIENTAUTOFILLSEARCH;
									
									return BillingGenericService
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															return response.data.listObject;
														//$rootScope.stopLoader();
													});

								}
							 
	                            
	                            $scope.getWardList = function(floorId) {
									var commonData = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									switch (commonType) {
									    case 'floor': 
										    commonData.floorId = floorId;
										    var URI = BASE_URL + ROOT_URL + GETWARDLISTBYFLOORIDORGUNITID;
										    GenericService.serviceAction("POST", URI,commonData).then(function(response) {
	                                            $scope.wardList = [];
	                                                if (response.data.status == "success") {
	                                                    $scope.wardList = response.data.listObject;

	                                                }
	                                            });
										    break;
									    default:
									}
								}
	                            
	                            
								$scope.selectPatientId = function(info, model,
										label) {
									
									$scope.patientName = info.patientName;
									$scope.uhidNumber = info.uhidNumber;
									$scope.mobileNumber = info.mobileNumber;
									$scope.search.patientId = info.patientId;
								}

							$scope.init();

						} ]);
