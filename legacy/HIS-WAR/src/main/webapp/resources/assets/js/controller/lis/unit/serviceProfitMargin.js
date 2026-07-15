'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:tariffMasterController
 * @description #tariffMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'serviceProfitMargin',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'growl',
						'$state',
						'PagerService',
						'PaginationService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								PagerService,PaginationService) {

							$scope.selectedPaymentEnt=[];	
							$scope.selectedVisitType=[];
							$scope.selectedBillingCategory=[];
							$scope.selectedPatientTypeList=[];
							$scope.selectedSpeacilityList = [];
							$scope.selectedSubSpecialityList = [];
							$scope.selectedServiceList = [];
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
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

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
						
								$scope.selectedPaymentEntitlementList=[];
								
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}
								
						
							}
							$scope.init();
							$scope.activePaymentEntitlement=function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL+ GETACTIVEPAYMENTENTITLEMENT;
						GenericService.serviceAction("GET", URI, data)
								.then(function(response) {
											$scope.paymentEntitlementList = [];

											if (response.data.status == "success") {
												angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.paymentEntitlementId;
																	data.label = value.paymentEntitlementDesc;
																	$scope.paymentEntitlementList.push(data);
																});
												console.log('paymentEntitlementList',$scope.paymentEntitlementList);
											}
										});
							}
							$scope.activePaymentEntitlement();
							// for getting active visit type
							$scope.activeVisitType=function(){							
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								
								$scope.visitTypeList = [
									{
										id:3,
										label:"Day Care"
									},
									{
										id:4,
										label:"ER"
									},
									{
										id:2,
										label:"IP"
									},
									{
										id:1,
										label:"OP"
									}
								];
								
								
								/*var URI = BASE_URL + ROOT_URL
										+ GETACTIVEVISITTYPEMASTER+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.visitTypeList = [];

													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.visitTypeId;
																			data.label = value.visitTypeName;
																			$scope.visitTypeList.push(data);
																		});
														console.log('visitTypeList',$scope.visitTypeList);
													}
												});*/
							}
							$scope.activeVisitType();
							// for get active billing category list
							$scope.activeBillingBedCategory=function(){							
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.billingCategory = [];

													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.billingBedCategoryId;
																			data.label = value.billingBedDesc;
																			$scope.billingCategory.push(data);
																		});
														console.log('billingCategory',$scope.billingCategory);
													}
												});
							}
							$scope.activeBillingBedCategory();
							
							$scope.activePatientType=function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
									+ "/global/patientcategory/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.patientTypeList = [];
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.id;
																			data.label = value.name;
																			$scope.patientTypeList.push(data);
																		});
														console.log('patientTypeList',$scope.patientTypeList);
													}
												});
							}
							$scope.activePatientType();
							// For Speciality under modality
							$scope.activeSpeciality=function(){
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVESPECIALITY;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													if (response.data.status == "success") {
														$scope.speacilityList = [];
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.specialityId;
																			data.label = value.specialityName;
																			$scope.speacilityList.push(data);
																		});
													}
													console.log("speciality",$scope.speacilityList);
													$scope.getSpecialityAndSubspeciality();
												});	
							}
							$scope.activeSpeciality();
							$scope.typeBilling = 'S';
							$scope.getSelectedValue = function(type) 
							{
								$scope.typeBilling = type;
								console.log("tariffList",$scope.selectedTariffList);
								
								$scope.selectedVisitType = [];
								$scope.selectedPaymentEnt = [];
								$scope.selectedPatientTypeList = [];
								$scope.selectedPatientTypeList = [];
								
								var paymentEntitlementList = [];
								angular.forEach($scope.selectedTariffList, function(
										value, index) {
									paymentEntitlementList.push(value.id);
									paymentEntitlementList.push(value.lable);
								});
								var data = {
										tariffIdList : paymentEntitlementList,
									}
								if ($scope.selectedTariffList.length > 0) {
									var URI = BASE_URL
											+ ROOT_URL
											+ GETPAYMENTENTITLEMENTLISTBYTARIFFIDLIST;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.paymentEntitlementList = [];
															angular.forEach(response.data.listObject,
																			function(value,index) {
																				var data = {};
																				data.id = value.applicablePaymentEntitlementId;
																				data.label = value.paymentEntitlementDesc;
																				$scope.selectedPaymentEnt.push(data);
																			});
															$scope.activePaymentEntitlement();
															console.log('paymentEntitlementModeldata list',$scope.selectedPaymentEnt);
														}
													});
								}
								$scope.getSelectedValueForVisitType();
							}
							$scope.visitList = [];
							$scope.getSelectedValueForVisitType=function(){
								
								console.log("tariffList",$scope.selectedTariffList);
								
								angular.forEach($scope.selectedTariffList, function(
										value, index) {
									$scope.visitList.push(value.id);
								});
								var data = {
										tariffIdList : $scope.visitList,
									}
								if ($scope.selectedTariffList.length > 0) {
									var URI = BASE_URL+ ROOT_URL+ "/api/unit/getVisitTypeListByTariffIdList";
									GenericService.serviceAction("POST", URI, data)
											.then(function(response) {
														if (response.data.status == "success") {
															angular.forEach(response.data.listObject,
																			function(value,index) 
																			{
																				var data = {};
																				data.id = value.visitTypeId;
																				data.label = value.visitTypeName;
																				console.log("data.id",data.id);
																				console.log("data.label",data.label);
																				$scope.selectedVisitType.push(data);
																			});
															$scope.activeVisitType();
															console.log('selectedVisitType list',$scope.selectedVisitType);
														}
													});
									
								}
								
								$scope.getSelectedValueForpatientType();
							}
							$scope.getSelectedValueForpatientType=function(){
								console.log("tariffList",$scope.selectedTariffList);
								var patientTypelist1 = [];
								angular.forEach($scope.selectedTariffList, function(
										value, index) {
									patientTypelist1.push(value.id);
								});
								var data = {
										tariffIdList : patientTypelist1,
									}
								if ($scope.selectedTariffList.length > 0) {
									var URI = BASE_URL+ ROOT_URL+ "/api/unit/getPatientTypeListByTariffIdList";
									GenericService.serviceAction("POST", URI, data)
											.then(function(response) {
														if (response.data.status == "success") {
															$scope.selectedPatientTypeList = [];
															angular.forEach(response.data.listObject,
																			function(value,index) {
																				var data = {};
																				data.id = value.patientTypeId;
																				data.label = value.patientCategory;
																				$scope.selectedPatientTypeList.push(data);
																			});
															$scope.activePatientType();
															console.log('selectedPatientType list',$scope.selectedPatientTypeList);
														}
													});
								}
								$scope.getSelectedValueForBillingBedCategory();
								
							}
							$scope.getSelectedValueForBillingBedCategory=function(){
								
								if($scope.typeBilling == 'S'){
								console.log("tariffList",$scope.selectedTariffList);
								var billingBedCategory = [];
								angular.forEach($scope.selectedTariffList, function(
										value, index) {
									billingBedCategory.push(value.id);
								});
								var data = {
										tariffIdList : billingBedCategory,
									}
								if ($scope.selectedTariffList.length > 0) {
									var URI = BASE_URL+ ROOT_URL+ "/api/unit/getBillingBedCategoryByTariffIdList";
									GenericService.serviceAction("POST", URI, data)
											.then(function(response) {
														if (response.data.status == "success") {
															angular.forEach(response.data.listObject,
																			function(value,index) {
																				var data = {};
																				data.id = value.billingBedCategoryId;
																				data.label = value.billingBedDesc;
																				$scope.selectedBillingCategory.push(data);
																			});
															//$scope.activeBillingBedCategory();
															console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
														}
													});
								}
							}
							}
							
							$scope.getBillingBedCategory = function(visitId)
							{
								$scope.selectedBillingCategory = [];
									var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+visitId+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET", URI, "")
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.billingCategoryAdd = [];
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.billingBedCategoryId;
																			data.label = value.billingBedDesc;
																			$scope.billingCategoryAdd.push(data);
																		});
														//$scope.activeBillingBedCategory();
														//console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
													}
												});
								
								}
							
							
							$scope.clicked = function(type) {
								
								
								
								if (type == 'speciality') {
									
									$scope.selectedSubSpecialityList = [];
									$scope.selectedServiceList = [];
									$scope.subSpecialityList = [];
									if ($scope.selectedSpeacilityList.length > 0) {
										var specialityType = [];
										angular.forEach($scope.selectedSpeacilityList,
												function(value, index) {
													specialityType.push(value.id);
												});
										var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											specialityIdList : specialityType
										}
										
										var isExists;
										function checkExistsOrNot(arr, val) {
											return arr
													.some(function(arr) {
														return (val.id === arr.id);
													});
										}
										
										console.log(data);
										var URI = BASE_URL+ ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYARRAYFORTARIFF;
										BillingGenericService
												.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
															console.log(response);
															if (response.data.status == "success") {
																$scope.subSpecialityList = [];
																
																angular.forEach(response.data.listObject,
																				function(value,index) {
																					var data = {};
																					data.id = value.subSpecialityId;
																					data.label = value.subSpecialityName;
																					console.log("data",JSON.stringify(data));
																					isExists = checkExistsOrNot($scope.subSpecialityList,data);
																					if(!isExists)
																						{
																						   $scope.subSpecialityList.push(data);
																						}
																				});
																
															
															}
															console.log("subspeciality",$scope.subSpecialityList);
															
															$scope.getSpecialityAndSubspeciality();
														});
									}

								}
							}
							$scope.getSpecialityAndSubspeciality = function() {
								var specialistList = [];
								angular.forEach($scope.selectedSpeacilityList,
										function(value, index) {
											specialistList.push(value.id);
										});
								var subSpecialistList = [];
								angular.forEach($scope.selectedSubSpecialityList,
										function(value, index) {
											subSpecialistList.push(value.id);
										});
							
								var data = {
									unitId : $scope.unitId,
									orgnisationId : $scope.organizationId,
									specialityIdList : specialistList,
									subSpecialityIdList : subSpecialistList
								}
								var URI = BASE_URL
										+ ROOT_URL
										+ "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.serviceList = [];
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.serviceId;
																			data.label = value.serviceStandardName;
																			$scope.serviceList.push(data);
																		});
														console.log('serviceList',$scope.serviceList);
													}
												});
								// }
							}
							
							/*$scope.toggleDropdown = function()
							{
								$scope.getSpecialityAndSubspeciality();
							}*/
							 
							 $scope.visitType = function(headerId) {
								 
								 //$scope.searchVisitTypeList = [];
							      $scope.headerObject = $scope.visitTypeList.filter(function(headerMaster) {
							        	var data = {};
							         if (headerMaster.id == headerId) {
							        	 data.id=headerMaster.id;
							        	 data.lable=headerMaster.label;
							        	 $scope.searchVisitTypeList.push(data);
							         }
							        });
							      console.log("visitTypeList", $scope.searchVisitTypeList);
							     }
							 $scope.searchbedTypeCategoryList = [];
							  $scope.bedBilling = function(headerId) {
								 
							      $scope.headerObject = $scope.billingCategory.filter(function(headerMaster) {
							        	var data = {};
							         if (headerMaster.id == headerId) {
							        	 data.id=headerMaster.id;
							        	 data.lable=headerMaster.label;
							        	 $scope.searchbedTypeCategoryList.push(data);
							         }
							        });
							      console.log("bedTypeCategoryList",$scope.searchbedTypeCategoryList);
							     }
							  $scope.searchpatientTypeList = [];
							  $scope.patientCatList = function(headerId) {
									
								      $scope.headerObject = $scope.patientTypeList.filter(function(headerMaster) {
								        	var data = {};
								         if (headerMaster.id == headerId) {
								        	 data.id=headerMaster.id;
								        	 data.lable=headerMaster.label;
								        	 $scope.searchpatientTypeList.push(data);
								         }
								        });
								      console.log("patientTypeList", $scope.searchpatientTypeList);
								     }
							  
							  var paymentEntitlementTypeList = [];
							  $scope.paymentEntileMent = function(headerId) {
							      $scope.headerObject = $scope.paymentEntitlementList.filter(function(headerMaster) {
							        	var data = {};
							         if (headerMaster.id == headerId) {
							        	 data.id=headerMaster.id;
							        	 data.lable=headerMaster.label;
							        	 $scope.paymentEntitlementTypeList.push(data);
							         }
							        });
							      console.log("patientTypeList", $scope.paymentEntitlementTypeList);
							     }
							  
							  
							  //for added tarrif services search
							  
							  $scope.searchAddedTariffServices = function() {
								  
								  $scope.searchTariffMasterList = [];
								  $scope.searchVisitTypeList = [];
								 $scope.searchpatientTypeList = [];
								 $scope.searchbedTypeCategoryList = [];
								 
									angular.forEach($scope.selectedTariffList, function(
											value, index) {									
										$scope.selectHeader(value.id);
									});							
									angular.forEach($scope.selectedVisitType,
											function(value, index) {
										         $scope.visitType(value.id);
											});
									

									var paymentEntitlementTypeList = [];
									angular.forEach($scope.selectedPaymentEnt,
											function(value, index) {
												var data = {};
												data.id = value.id,
												data.lable = value.label
												paymentEntitlementTypeList.push(data);
											});
									console.log("paymentEntitlementTypeList",
											paymentEntitlementTypeList);
									
									 
									var patientTypeList = [];
									angular.forEach($scope.selectedPatientTypeList,
											function(value, index) {
									      	$scope.patientCatList(value.id);
											});
									
									
									  angular.forEach($scope.selectedBillingCategory,
											function(value, index) {
										     $scope.bedBilling(value.id);
											});
									
									var specialityTypeList = [];
									angular.forEach($scope.selectedSpeacilityList,
											function(value, index) {
												specialityTypeList.push(value.id);
											});
									console.log("specialityTypeList",
											specialityTypeList);

									var subSpecialityTypeList = [];
									angular.forEach($scope.selectedSubSpecialityList,
											function(value, index) {
												subSpecialityTypeList.push(value.id);
											});
									console.log("subSpecialityTypeList",
											subSpecialityTypeList);

									var serviceTypeList = [];
									angular.forEach($scope.selectedServiceList, function(value, index) {
										serviceTypeList.push(value.id);
									});
									console.log("serviceTypeList",$scope.serviceModel);

									var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										tariffMasterList : $scope.searchTariffMasterList,
										paymentEntitlementList : paymentEntitlementTypeList,
										visitTypeList : $scope.searchVisitTypeList,
										patientTypeList : $scope.searchpatientTypeList,
										bedCategoryList : $scope.searchbedTypeCategoryList,
										specialityList : specialityTypeList,
										subSpecialityList : subSpecialityTypeList,
										serviceList : serviceTypeList

									}
									 console.log("dinesh", data)
//									 return false;
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/unit/getProfitMarginList";
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														
														$scope.pagination.data = [];
														$scope.allDetailsList = [];
														if (response.data.status == "success") {

															$scope.allDetailsList = response.data.listObject;
															
															 if($scope.allDetailsList.length > 0){            
										                         PaginationService.searchData($scope.allDetailsList, $scope.pagination);
										                       }
														}

													});
								}
							  
							  //End added tarrif search servics
							  
							  
							 	$scope.searchTariffServices = function() {	
							 		
							 		$rootScope.startLoader();
							 		
							 		$scope.searchTariffMasterList = [];
									$scope.paymentEntitlementTypeList=[];
									$scope.searchVisitTypeList=[];
									$scope.searchpatientTypeList=[];
									$scope.searchbedTypeCategoryList=[];
									
									
								angular.forEach($scope.selectedTariffList, function(
										value, index) {									
									$scope.selectHeader(value.id);
								});						
								
								console.log('$scope.selectedVisitType',JSON.stringify($scope.selectedVisitType));
								/*angular.forEach($scope.selectedVisitType,
										function(value, index) {*/
									$scope.visitType($scope.visitTypeId);
										/*});*/
								console.log('$scope.searchVisitTypeList',JSON.stringify($scope.searchVisitTypeList));
									
								console.log('ssdcd',$scope.selectedPaymentEnt);
								//var paymentEntitlementTypeList = [];
								angular.forEach($scope.selectedPaymentEnt,
										function(value, index) {
											/*var data = {};
											data.id = value.id,
											data.label = value.label
											paymentEntitlementTypeList.push(data);*/
									  $scope.paymentEntileMent(value.id);
										});
								console.log("paymentEntitlementTypeList",
										paymentEntitlementTypeList);

								var patientTypeList = [];
								$scope.searchpatientTypeList = [];
								angular.forEach($scope.selectedPatientTypeList,
										function(value, index) {
								      	$scope.patientCatList(value.id);
										});
								
								
								  angular.forEach($scope.selectedBillingCategory,
										function(value, index) {
									     $scope.bedBilling(value.id);
										});
								
								var specialityTypeList = [];
								angular.forEach($scope.selectedSpeacilityList,
										function(value, index) {
											specialityTypeList.push(value.id);
										});
								console.log("specialityTypeList",
										specialityTypeList);

								var subSpecialityTypeList = [];
								angular.forEach($scope.selectedSubSpecialityList,
										function(value, index) {
											subSpecialityTypeList.push(value.id);
										});
								console.log("subSpecialityTypeList",
										subSpecialityTypeList);

								var serviceTypeList = [];
								angular.forEach($scope.selectedServiceList, function(value, index) {
									serviceTypeList.push(value.id);
								});
								console.log("serviceTypeList",$scope.serviceModel);

								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									tariffMasterList : $scope.searchTariffMasterList,
									paymentEntitlementList : $scope.paymentEntitlementTypeList,
									visitTypeList : $scope.searchVisitTypeList,
									patientTypeList : $scope.searchpatientTypeList,
									bedCategoryList : $scope.searchbedTypeCategoryList,
									specialityList : specialityTypeList,
									subSpecialityList : subSpecialityTypeList,
									serviceList : serviceTypeList

								}
								
								if(Object.keys($scope.searchTariffMasterList).length == 0)
									{
									growl
									.warning(
											"Please Select at least one tariff!!",
											{
												title : 'Warning!'
											}
									);
									$rootScope.stopLoader();
									return false;
									}
								else if(Object.keys($scope.paymentEntitlementTypeList).length == 0)
									{
									growl
									.warning(
											"Please Select at least one paymententitlement!!",
											{
												title : 'Warning!'
											}
									);
									$rootScope.stopLoader();
									return false;
									}
								else if(Object.keys($scope.searchpatientTypeList).length == 0)
								{
								growl
								.warning(
										"Please Select at least one patient type!!",
										{
											title : 'Warning!'
										}
								);
								$rootScope.stopLoader();
								return false;
								}
								else if(Object.keys($scope.searchVisitTypeList).length == 0)
								{
								growl
								.warning(
										"Please Select at least one visit type!!",
										{
											title : 'Warning!'
										}
								);
								$rootScope.stopLoader();
								return false;
								}
								else if(Object.keys($scope.searchbedTypeCategoryList).length == 0)
								{
								growl
								.warning(
										"Please Select at least one Billing bed category!!",
										{
											title : 'Warning!'
										}
								);
								$rootScope.stopLoader();
								return false;
								}
								else {
								 //console.log("dinesh", data)
//								 return false;
								var URI = BASE_URL
										+ ROOT_URL
										+ GETMASTERSERVICELISTBYMULTIPLEPARAMETERS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.allDetailsList = [];
													if (response.data.status == "success") {

														$scope.allDetailsList = response.data.listObject;
														console
																.log(
																		"allDetailsList",
																		$scope.allDetailsList);
														
														$rootScope.stopLoader();
													}
													else
														{
														  $rootScope.stopLoader();
														}

												});
							 	}
							}

							$scope.applyDate = function() {
								var date = moment($scope.datepicker).format(
										'DD/MM/YYYY');
								angular.forEach(
												$scope.allDetailsList,
												function(value, index) {
													// $('#id_' +
													// index).val(date);
													$scope.allDetailsList[index].effectiveDate = date;
												});

							}
							
							
							$scope.remove = function(index) {
								var newDataList = [];
								var i = 1;
								angular.forEach($scope.allDetailsList, function(v) {
									if (index != i) {
										newDataList.push(v);
									}
									i++;
								});
								
								$scope.allDetailsList = newDataList;
							};
							
							
							$scope.saveUnitServiceTariffMaster = function() {
								console.log('Final', $scope.allDetailsList);
								
								$rootScope.startLoader();
								var date = moment($scope.datepicker).format(
								'YYYY-MM-DD HH:mm:ss');
									
						angular.forEach(
										$scope.allDetailsList,
										function(value, index) {
											// $('#id_' +
											// index).val(date);
											$scope.allDetailsList[index].effectiveDate = date;
										});

//								var dateNew=$scope.allDetailsList.
								var data={
										unitServiceTariffMasterDtosList:$scope.allDetailsList,
										unitId:$scope.unitId,
								        organizationId:$scope.organizationId,
								        createdBy : $scope.userId,
										updatedBy : $scope.userId,
										createdDate :moment(new Date())
												.format('DD-MM-YYYY HH:mm:ss'),
										updatedDate :moment(new Date())
										.format('DD-MM-YYYY HH:mm:ss')
								}
//								console.log('Final', data); return false;
								var URI = BASE_URL
										+ ROOT_URL
										+ SAVEUNITSERVICETARIFFMASTER;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl
																.success(
																		response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
													/*	growl
																.error(
																		response.data.message,
																		{
																			title : 'Error!'
																		}
																);*/
														$rootScope.stopLoader();
														$scope.allDetailsList=response.data.listObject
														console.log('Response data list --->',$scope.allDetailsList)
													}

												});
							}

						} ]);
