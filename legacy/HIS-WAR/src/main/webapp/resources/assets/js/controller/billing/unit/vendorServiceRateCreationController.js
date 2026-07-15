  (function () {
  "use strict";

  function vendorServiceRateCreationController($scope, $http, $localStorage, $sessionStorage,$cookies, $rootScope, GenericService,BillingGenericService, growl, $state,PagerService,GenericServiceParamHeader,$filter) {
  
  

							// for datePicker
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};
						/*	$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};
*/							
							$scope.openDatePicker = function() {
								$scope.datepickerOpen.opened = true;
							};

							$scope.datepickerOpen = {
								opened : false
							};
							
							
							$scope.openDatePickerForNewDOA = function() {

								$scope.datepickerForNewDOA.opened = true;
							};

							$scope.datepickerForNewDOA = {
								opened : false
							};
							$scope.effectiveDateObj = {
								effectiveDate : ""
							}
							$scope.msg="No records found...!!";
							$scope.selectedTariffList=[];	
							$scope.selectedPaymentEnt=[];	
							$scope.selectedVisitType=[];
							$scope.selectedBillingCategory=[];
							$scope.selectedPatientTypeList=[];
							$scope.selectedSpeacilityList = [];
							$scope.selectedSubSpecialityList = [];
							$scope.selectedServiceList = [];
							$scope.selectedPackageList = [];
							$scope.duplicateList = [];
							$scope.selectedPaymententitlementList = [];
							$scope.selectedVendorsList = [];
							$scope.packageWise = 'N';
						
							$scope.clearPackageList = function(){
								
								if($scope.packageWise=='N')
									$scope.init();
								
							}
							
							$scope.getActiveVendors = function(){
						      $scope.activeVendorsList=[];
						      var URI = BASE_URL + ROOT_URL + API + "/unit/vendors/active/"+$scope.unitId;
						      return GenericServiceParamHeader.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if (response.status == 200) {
										angular.forEach(response.data,
														function(value,index) {
															var data = {};
															data.id = value.vendorId;
															data.label = value.name;
															$scope.activeVendorsList.push(data);
														});
										console.log('activeVendorsList',$scope.activeVendorsList);
									}
								
								});
						      
						      
						    }
							 
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
								
								  $scope.getVisitTypeList = function(searchPackageObj) {
									  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
									  $rootScope.startLoader();
									    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getVisitTypeList"
									    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success"){
												 angular.forEach(response.data.listObject,
															function(value,index) 
															{
																var data = {};
																data.id = value.id;
																data.label = value.name;
																$scope.visitTypeListOption.push(data);
															});
											}
											$rootScope.stopLoader();
										});
									  }
									
									
									$scope.getBillingBedCategoryDropdown=
									{
											onClose  : function () {
												console.log($scope.selectedVisitType);
												var visitTypeId = [];
												angular.forEach($scope.selectedVisitType,
														function(value,index) 
														{
															visitTypeId.push(value.id);
														});
												
												var data = {
														unitId: $scope.unitId,
														organizationId:$scope.organizationId,
														visitTypeListIds : visitTypeId
												}
												
												$scope.selectedBillingCategory = [];
												var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeIdMultiple";
											GenericService.serviceAction("POST", URI, data)
													.then(function(response) {
																if (response.data.status == "success") {
																	$scope.bedBillingCategoryListReference = [];
																	$scope.bedBillingCategoryListReference = response.data.listObject;
																	$scope.bedBillingCategoryList = [];
																	angular.forEach(response.data.listObject,
																					function(value,index) {
																						var data = {};
																						data.id = value.billingBedCategoryId;
																						data.label = value.billingBedDesc;
																						var found = $scope.bedBillingCategoryList.find(ar=>ar.id==value.billingBedCategoryId);
																						   if(found==undefined)
																							   {
																							      $scope.bedBillingCategoryList.push(data);
																							   }
																					});
																}
															});
													}
										}
									
									
									$scope.getOutSourceServiceList = function() {
									
										var URI = BASE_URL
												+ ROOT_URL
												+ "/billing/service/outsource/null/"+$scope.unitId;
										GenericService
												.serviceAction("GET", URI, {})
												.then(
														function(response) {
															if (response.data.status == "success") {
																$scope.serviceList = [];
																angular.forEach(response.data.listObject,
																				function(value,index) 
																				{
																					
																							var data = {};
																							data.id = value.serviceMasterId;
																							data.label = value.serviceStandardName;
																							$scope.serviceList.push(data);
																				});
																console.log('serviceList',$scope.serviceList);
															}
														});
									}
							
							$scope.getenableDisable=function(){
								if($scope.allDetailsList.length > 0 && $scope.datepicker !=null)
									$scope.enableApplyBtn = false;
							}
							
							
							$scope.activePaymentEntitlement=function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPAYMENTENTITLEMENT;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
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
							
							$scope.typeBilling = 'S';
							
							
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('applicationConfiguration');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								if(cookieObjectAppConfig == undefined){
									 $state.go('applicationConfiguration');
								  return;
								 }
								 else
								  {
									 if((cookieObjectAppConfig.defaultSelfTariffId == '' || cookieObjectAppConfig.defaultSelfTariffId == null) && (cookieObjectAppConfig.companyPatientCategory == '' || cookieObjectAppConfig.companyPatientCategory == null))
										 {
										  $state.go('applicationConfiguration');
										  return;
										 }
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.companyPatientCategory = cookieObjectAppConfig.companyPatientCategory;
								$scope.insurancePatientCategory = cookieObjectAppConfig.insurancePatientCategory;
								$scope.tariffMuliSelectList =[];
								$scope.selectedPaymentEnt=[];	
								$scope.selectedVisitType=[];
								$scope.selectedBillingCategory=[];
								$scope.selectedPatientTypeList=[];
								$scope.selectedSpeacilityList = [];
								$scope.selectedSubSpecialityList = [];
								$scope.selectedServiceList = [];
								$scope.selectedPackageList = [];
								$scope.allDetailsList=[];
								$scope.paymentEntitlementMultiselectList = [];
								$scope.tariffId=null;
								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.disableSaveBtn = true;
								$scope.enableApplyBtn = true;
								$scope.enableDatePicker = true;
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								$scope.effectiveDate = '';
								$scope.selectedTariffList=[];
								$scope.paymentEntitlementList = [];
								$scope.selectedPaymentEntitlementList=[];
								$scope.selectedVendorsList=[];
								$scope.selectedPatientTypeList=[];
								$scope.patientTypeList = [];
								$scope.visitTypeListOption = [];
								$scope.selectedVisitType = [];
								$scope.bedBillingCategoryList=[];
								$scope.selectedBedBillingCategoryList=[];
								$scope.selectedPackageList = [];
								
								$scope.getActiveVendors();
								$scope.activePatientType();
								$scope.getVisitTypeList();
								$scope.getOutSourceServiceList();
								$scope.activePaymentEntitlement();
							}
							$scope.init();
							
							
							
							
							$scope.searchVisitTypeList = [];
							 $scope.visitType = function(headerId) {
							      $scope.headerObject = $scope.visitTypeListOption.filter(function(headerMaster) {
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
								 
							      $scope.headerObject = $scope.bedBillingCategoryList.filter(function(headerMaster) {
							        	var data = {};
							         if (headerMaster.id == headerId) {
							        	 data.id=headerMaster.id;
							        	 data.lable=headerMaster.label;
							        	 $scope.searchbedTypeCategoryList.push(data);
							         }
							        });
							      console.log("bedTypeCategoryList",$scope.searchbedTypeCategoryList);
							     }
							  
							  $scope.serviceWithLabel = function(headerId) {
									 
							      $scope.headerObject = $scope.serviceList.filter(function(headerMaster) {
							        	var data = {};
							         if (headerMaster.id == headerId) {
							        	 data.id=headerMaster.id;
							        	 data.lable=headerMaster.label;
							        	 $scope.serviceTypeList.push(data);
							         }
							        });
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
							  
							  $scope.vendorsTypeList=[];
							  $scope.vendors = function(headerId) {
							      $scope.headerObject = $scope.activeVendorsList.filter(function(headerMaster) {
							        	var data = {};
							         if (headerMaster.id == headerId) {
							        	 data.id=headerMaster.id;
							        	 data.lable=headerMaster.label;
							        	 $scope.vendorsTypeList.push(data);
							         }
							        });
							      console.log("vendorsTypeList", $scope.vendorsTypeList);
							     }
							  
							  $scope.serviceWithObject = function(id){
								  $scope.headerObject = $scope.allServiceList.filter(function(headerMaster) {
							         if (headerMaster.packageServiceDetailsId == id) {
							        	 $scope.serviceTypeList.push(headerMaster);
							         }
							        });
							  }
							  //for added tarrif services search
							  
							  $scope.searchAddedTariffServices = function() {
								  
								  $rootScope.startLoader();
								  
								  	$scope.vendorListTemp = [];
									$scope.paymentEntitlementTypeList=[];
									$scope.searchpatientTypeList=[];
									$scope.searchVisitTypeList=[];
									$scope.searchbedTypeCategoryList=[];
									$scope.vendorsTypeList =[];
							

									angular.forEach($scope.selectedVendorsList,
											function(value, index) {
												$scope.vendors(value.id)
											});
									
									
									
									angular.forEach($scope.selectedPaymententitlementList,
											function(value, index) {
												$scope.paymentEntileMent(value.id)
											});
									
									
								angular.forEach($scope.selectedPatientTypeList, 
										function(value, index) {
											$scope.patientCatList(value.id)
										});
								
								
								angular.forEach($scope.selectedVisitType, 
										function(value, index) {
											$scope.visitType(value.id)
										});
								
								
								angular.forEach($scope.selectedBedBillingCategoryList,
										function(value, index) {
									$scope.bedBilling(value.id)
										});
								
							
								$scope.packageList =[];
								angular.forEach($scope.selectedPackageList,
										function(value, index) {
									$scope.getPackageDetails(value.id)
										});	
								
								$scope.serviceTypeList = [];
								angular.forEach($scope.selectedServiceList, function(value, index) {
									$scope.serviceWithLabel(value.id)
								});
								console.log("serviceTypeList",$scope.serviceModel);

								//$scope.searchTariffMasterList.push($scope.tariffId)
								
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									vendorsList : $scope.vendorsTypeList,
									paymentEntitlementList : $scope.paymentEntitlementTypeList,
									visitTypeList : $scope.searchVisitTypeList,
									patientTypeList : $scope.searchpatientTypeList,
									bedCategoryList : $scope.searchbedTypeCategoryList,
									serviceList : $scope.serviceTypeList,
									packageList : $scope.packageList

								}
									 console.log(data)
//									 return false;
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/vendorServiceRateConfig/list";
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														$scope.allDetailsList = [];
														if (response.data.status == "success") {

															$scope.allDetailsList = response.data.listObject;
															$rootScope.stopLoader();
														}else{
															$rootScope.stopLoader();
														}

													});
								}
							  
							 	$scope.searchServicesByCriteria = function() {	
							 		
							 		$rootScope.startLoader();
							 		
							 		$scope.vendorsTypeList = [];
								angular.forEach($scope.selectedVendorsList,
										function(value, index) {
											$scope.vendors(value.id)
										});

								$scope.serviceTypeList = [];
								angular.forEach($scope.selectedServiceList, function(value, index) {
									$scope.serviceWithLabel(value.id)
								});

								$scope.paymentEntitlementTypeList =[];
								angular.forEach($scope.selectedPaymententitlementList,
										function(value, index) {
											$scope.paymentEntileMent(value.id)
										});
								
							$scope.searchpatientTypeList=[];	
							angular.forEach($scope.selectedPatientTypeList, 
									function(value, index) {
										$scope.patientCatList(value.id)
									});
							
							$scope.searchVisitTypeList =[];
							angular.forEach($scope.selectedVisitType, 
									function(value, index) {
										$scope.visitType(value.id)
									});
							
							$scope.searchbedTypeCategoryList=[];
							angular.forEach($scope.selectedBedBillingCategoryList,
									function(value, index) {
								$scope.bedBilling(value.id)
									});
								
							$scope.packageList =[];
							angular.forEach($scope.selectedPackageList,
									function(value, index) {
								$scope.getPackageDetails(value.id)
									});	
								
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									vendorsList : $scope.vendorsTypeList,
									paymentEntitlementList : $scope.paymentEntitlementTypeList,
									visitTypeList : $scope.searchVisitTypeList,
									patientTypeList : $scope.searchpatientTypeList,
									bedCategoryList : $scope.searchbedTypeCategoryList,
									serviceList : $scope.serviceTypeList,
									packageList : $scope.packageList

								}
								
								if(Object.keys($scope.vendorsTypeList).length == 0)
									{
									growl
									.warning(
											"Please Select Vendor!!",
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
								}else if(Object.keys($scope.serviceTypeList).length == 0){
									growl
									.warning(
											"Please Select at least one Service!!",
											{
												title : 'Warning!'
											}
									);
									$rootScope.stopLoader();
									return false;
								}
								else {
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/vendorServiceRateConfig/generateList";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.allDetailsList = [];
													if (response.data.status == "success") {

														$scope.allDetailsList = response.data.listObject;
														console
																.log('allDetailsList',
																		response.data.listObject);
														
														$rootScope.stopLoader();
														$scope.enableDatePicker = false;
													}
													else
														{
														  $rootScope.stopLoader();
														}

												});
							 	}
							}
							 	
							 	
							 	$scope.searchPackagesByCriteria = function() {	
							 		$rootScope.startLoader();
							 		
									$scope.paymentEntitlementTypeList=[];
									$scope.searchpatientTypeList=[];
									$scope.searchVisitTypeList=[];
									$scope.searchbedTypeCategoryList=[];
									
									
								angular.forEach($scope.selectedPaymententitlementList,
											function(value, index) {
												$scope.paymentEntileMent(value.id)
											});
									
									
								angular.forEach($scope.selectedPatientTypeList, 
										function(value, index) {
											$scope.patientCatList(value.id)
										});
								
								
								angular.forEach($scope.selectedVisitType, 
										function(value, index) {
											$scope.visitType(value.id)
										});
								
								
								angular.forEach($scope.selectedBedBillingCategoryList,
										function(value, index) {
									$scope.bedBilling(value.id)
										});
								
								
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									paymentEntitlementList : $scope.paymentEntitlementTypeList,
									visitTypeList : $scope.searchVisitTypeList,
									patientTypeList : $scope.searchpatientTypeList,
									bedCategoryList : $scope.searchbedTypeCategoryList,

								}
								
								if(Object.keys($scope.paymentEntitlementTypeList).length == 0)
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
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/vendorServiceRateConfig/ehcPackage/list";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														
														$scope.ehcPackageList = [];
														if(response.data.listObject.length > 0){
															
															angular.forEach(response.data.listObject, 
																	function(value, index) {
																var data = {};
																data.id = value.packageMasterId;
																data.label= value.serviceStandardName;
																$scope.ehcPackageList.push(data);
																	});
														}
														
														console.log('ehcPackageList',response.data.listObject);
														$rootScope.stopLoader();
														$scope.enableDatePicker = false;
													}
													else
														{
														  $rootScope.stopLoader();
														}

												});
							 	}
							}
							 	
							 	
							 	  $scope.getPackageDetails = function(headerId) {
								      $scope.headerObject = $scope.selectedPackageList.filter(function(headerMaster) {
								        	var data = {};
								         if (headerMaster.id == headerId) {
								        	 data.id=headerMaster.id;
								        	 data.lable=headerMaster.label;
								        	 $scope.packageList.push(data);
								         }
								        });
								     }
							 	
							 $scope.getServicefromPackage = function(){
								 if($scope.selectedPackageList.length > 0){
									// $scope.selectedServiceList = [];
									 $scope.packageList =[];
									angular.forEach($scope.selectedPackageList,
											function(value, index) {
										$scope.getPackageDetails(value.id)
											});
									
								 var data ={
										 packageList :[]
								 }
								 data.packageList = $scope.packageList;
								 var URI = BASE_URL
									+ ROOT_URL
									+ "/api/vendorServiceRateConfig/ehcPackage/services";
							BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												if (response.data.status == "success") {		
													$scope.serviceList = [];
													$scope.allServiceList = [];
													$scope.allServiceList = response.data.listObject;
												 angular.forEach(response.data.listObject,
														function(value,index) 
														{
															
																	var data = {};
																	data.id = value.serviceId;
																	data.label = value.serviceDesc;
																	$scope.serviceList.push(data);
														});
													console.log('serviceList',$scope.serviceList);}
												else
													{
													  $rootScope.stopLoader();
													}

											});
								 }
							 }	
							 	
							 	

							$scope.applyDate = function() {
								
								$rootScope.startLoader();
								var date = moment($scope.datepicker).format(
								'YYYY-MM-DD HH:mm:ss');
									
								angular.forEach(
										$scope.allDetailsList,
										function(value, index) {
											$scope.allDetailsList[index].unitId=$scope.unitId,
											$scope.allDetailsList[index].organizationId=$scope.organizationId,
											$scope.allDetailsList[index].createdBy = $scope.userId,
											$scope.allDetailsList[index].updatedBy = $scope.userId,
											$scope.allDetailsList[index].effectiveDate = date.substring(0,10);
										});

								

								
								var data={
											effectiveDate : date,
											vendorsList:$scope.vendorsTypeList,
											paymentEntitlementList : $scope.paymentEntitlementTypeList,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											patientTypeList : $scope.searchpatientTypeList,
											bedCategoryList : $scope.searchbedTypeCategoryList,
											serviceList : $scope.serviceTypeList,
											visitTypeList : $scope.searchVisitTypeList,
											packageList : $scope.packageList
										 }
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/vendorServiceRateConfig/uniqueCheck";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.disableSaveBtn = false;
														$rootScope.stopLoader();
													} else {
														$scope.disableSaveBtn = true;
														$scope.duplicateList = response.data.resObject;
														$scope.message = 'Total '+response.data.object+' services are alredy exist with applied effective date, recommend you to change the effective date..!!';
														angular.element('#already_exist').modal('show');
													}

												});
							

							}
							
							
							$scope.remove = function(index) {
								var newDataList = [];
								var i = 1;
								angular.forEach($scope.allDetailsList, function(v) {
									if (index != i-1) {
										newDataList.push(v);
									}
									i++;
								});
								
								$scope.allDetailsList = newDataList;
							};
							
							
							$scope.saveUnitServiceTariffMaster = function() {
								var finalDetailsList = [];
								
								$rootScope.startLoader();
								var date = moment($scope.datepicker).format(
								'YYYY-MM-DD HH:mm:ss');
									
								angular.forEach($scope.allDetailsList,
										function(value, index) {
												$scope.allDetailsList[index].unitId=$scope.unitId,
												$scope.allDetailsList[index].organizationId=$scope.organizationId,
												$scope.allDetailsList[index].createdBy = $scope.userId,
												$scope.allDetailsList[index].updatedBy = $scope.userId,
												$scope.allDetailsList[index].effectiveDate = date;
												finalDetailsList.push($scope.allDetailsList[index]);
										});

								var data={
											vendorServiceRateConfigDtosList:finalDetailsList,
											duplicateIdsList : $scope.duplicateList 
										}
								
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/vendorServiceRateConfig/save";
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
													$scope.datepicker = null;
													$scope.init();	
													
													} else {
														$rootScope.stopLoader();
														$scope.allDetailsList=response.data.listObject
														console.log('Response data list --->',$scope.allDetailsList)
													}

												});
							}
							$scope.totalDisplayed = 20;
							$scope.loadMore = function () 
							{
							  $scope.totalDisplayed += 20;  
							};
							$scope.loadLess = function () 
							{
								if($scope.totalDisplayed>20)
									{
									  $scope.totalDisplayed -= 20;  
									}
							};
							
							
  }
  
  
  function config($stateProvider) {
    $stateProvider.state("vendorServiceRateCreation", {
      url: "/vendorServiceRateCreation",
      templateUrl: 'views/master/UnitMaster/billingMaster/vendor-service-rate-creation.html',
      controller: "vendormasterCre.Controller",
      title: 'Vendor Service Rate Config',
      controllerAs: "vm"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("vendormasterCre.Controller", vendorServiceRateCreationController);
})();
