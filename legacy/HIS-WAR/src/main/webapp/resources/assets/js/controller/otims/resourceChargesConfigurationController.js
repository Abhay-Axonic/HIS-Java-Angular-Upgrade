'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:resourceChargesConfigurationController
 * @description #resourceChargesConfigurationController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'resourceChargesConfigurationController',
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
						'BillingGenericService',
						'PaginationService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,BillingGenericService,PaginationService) {
							
							 $scope.dateOptions = {							 
								      formatYear: 'yyyy',
								      showWeeks: false
								  };
								 $scope.openDatePicker = function() {
									// alert("in");
								      $scope.datepicker.opened = true;
								    };
								    $scope.datepicker = {
										      opened: false
										    };

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

								$scope.resourceChargesConfigurationObj = {
										orgId  : $scope.organizationId,
										unitId :$scope.unitId,
										specialityId:'',
										roleId:'',
										serviceId:'',
										amount:'',
										status:'A',
										
								};
								
								$scope.pagination = {
					                    currentPage: 1,
					                    totalPages: 0,
					                    pageSize: '10',
					                    search: '',
					                    searchData: PaginationService.searchData,
					                    setTotalPages: PaginationService.setTotalPages,
					                    getPagesList: PaginationService.getPagesList,
					                    setPage: PaginationService.setPage,
					                    setFirstPage: PaginationService.setFirstPage,
					                    setPreviousPage: PaginationService.setPreviousPage,
					                    setNextPage: PaginationService.setNextPage,
					                    setLastPage: PaginationService.setLastPage,
					                    sortBy: PaginationService.sortBy
					                  }
								
								var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
								var obj = {organizationId:$scope.organizationId, unitId:$scope.unitId};
								BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.specialityList = [];
									if (response.data.status == "success")
										$scope.specialityList = response.data.listObject;
								});
								
								var URI = BASE_URL + ROOT_URL + "/api/procedure/getRoleList/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.roleListByDoctorCharges = [];
									if (response.data.status == "success")
										$scope.roleListByDoctorCharges = response.data.listObject;
								});
								
								var URI = BASE_URL + ROOT_URL + "/api/otims/resourceChargeConfiguration/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.resourceChargeConfigurationList = [];
									if (response.data.status == "success")
										$scope.pagination.data = [];
										$scope.resourceChargeConfigurationList = response.data.listObject;
										
										if($scope.resourceChargeConfigurationList.length > 0){            
					                         PaginationService.searchData($scope.resourceChargeConfigurationList, $scope.pagination);
					                       }
								});
								
								
								$scope.service ="";

							}
							
							
						
							$scope.init();
							
							$scope.searchProcServices = function(serviceName){
								/*var URI = BASE_URL + ROOT_URL + "/api/global/search/services/procedure/"+$scope.organizationId+"/"+$scope.unitId+"/"+serviceName;
								return BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										return response.data.listObject;
								});*/
								if($scope.resourceChargesConfigurationObj.specialityId == '')
									{
									growl
									.warning(
											'Please Select Speciality...!!',
											{
												title : 'Warning!'
											});
									}
								else
									{
										var URI = BASE_URL + ROOT_URL 
						    		     + "/global/procedureSearch/"+serviceName
						    		     +"/unit/"+$scope.unitId
						    		     +"/org/"+$scope.organizationId
						    		     +"/speciality/"+$scope.resourceChargesConfigurationObj.specialityId
						    		     console.log('URI',URI);
						    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						    			if(response.data.status == "success"){
						    			return response.data.listObject;
						    			}
						    		});
									}
							}
							
							$scope.selectProcService = function(item, model, label){
								$scope.resourceChargesConfigurationObj.serviceId = item.serviceMasterId;
							}
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveProcedureCharges();
							}
							$scope.saveProcedureCharges = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.resourceChargesCofiguForm.$valid) {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else{
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								
									$scope.resourceChargesConfigurationObj.createdBy = $scope.userId;
									$scope.resourceChargesConfigurationObj.updatedBy = $scope.userId;
									$scope.resourceChargesConfigurationObj.createdDate = $scope.currentDate;
									$scope.resourceChargesConfigurationObj.updatedDate = $scope.currentDate;
									
								console.log($scope.resourceChargesConfigurationObj);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ "/api/otims/resourceChargeConfiguration";
								
								GenericService
										.serviceAction("POST", URI, $scope.resourceChargesConfigurationObj)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
														$scope.$broadcast('show-errors-reset');
														
														$scope.init();
													} else {
														$rootScope.stopLoader();
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							   }
							}
							}
							// Function For get single country
							$scope.showUpdateBtn = function(item) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								$scope.resourceChargesConfigurationObj = item;
								$scope.service = item.serviceStandardName;
								$scope.resourceChargeConfigurationId = item.resourceChargeConfigurationId;
							};

							// Function For update country status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";
								
								var data = {
										resourceChargeConfigurationId : id,
										status : type,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate
									}
								
								
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ "/api/otims/updateResourceChargeConfigurationStatus";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}

							// Function For update country
							$scope.updateProcedureCharges = function() {
								$scope.$broadcast('show-errors-check-validity');
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								
								$scope.resourceChargesConfigurationObj.createdBy = $scope.userId;
								$scope.resourceChargesConfigurationObj.updatedBy = $scope.userId;
								$scope.resourceChargesConfigurationObj.createdDate = $scope.currentDate;
								$scope.resourceChargesConfigurationObj.updatedDate = $scope.currentDate;
								$scope.resourceChargesConfigurationObj.unitId = $scope.unitId;
								$scope.resourceChargesConfigurationObj.orgId = $scope.organizationId;
								$scope.resourceChargesConfigurationObj.resourceChargeConfigurationId = $scope.resourceChargeConfigurationId;
									//console.log(data);
								if ($scope.resourceChargesCofiguForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ "/api/otims/updateResourceChargeConfiguration";
									GenericService
											.serviceAction("POST", URI, $scope.resourceChargesConfigurationObj)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Something went wrong...!!',
																			{
																				title : 'Error!'
																			});
														}

														
													});
								

							}
						
							}
						

						} ]);
