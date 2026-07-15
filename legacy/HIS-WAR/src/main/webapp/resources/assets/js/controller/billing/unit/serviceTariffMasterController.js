
'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:tariffMasterController
 * @description #tariffMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'serviceTariffMasterController',
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
						'promiseFactoryWithObject',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								PagerService,promiseFactoryWithObject) {

							// for datePicker
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};
											
							$scope.openDatePicker = function() {
								$scope.datepickerOpen.opened = true;
							};

							$scope.datepickerOpen = {
								opened : false
							};
							
							$scope.effectiveDatepicker =  moment().toDate();
							
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
							$scope.duplicateList = [];
							/* init() function for form object create. */
							
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('applicationConfiguration');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.fromUnitId = 0;
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

                                var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
                                if (cookieObjectAppConfigSecond != undefined) {
	                                $scope.maxInputAmount = cookieObjectAppConfigSecond.maxInputAmount;
                                }

								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.companyPatientCategory = cookieObjectAppConfig.companyPatientCategory;
								$scope.insurancePatientCategory = cookieObjectAppConfig.insurancePatientCategory;
								$scope.tariffMuliSelectList =[];
								$scope.selectedTariffList=[];	
								$scope.selectedPaymentEnt=[];	
								$scope.selectedVisitType=[];
								$scope.selectedBillingCategory=[];
								$scope.selectedPatientTypeList=[];
								$scope.selectedSpeacilityList = [];
								$scope.selectedSubSpecialityList = [];
								$scope.selectedServiceList = [];
								$scope.allDetailsList=[];
								//$scope.paymentEntitlementMultiselectList = [];
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
								$scope.selectedPaymentEntitlementList=[];
								$scope.paymentEntitlementId=null;
								
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}
								// for getting active tariff master
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVETARIFFMASTERLIST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.tariffList = response.data.listObject;
														console.log("ActiveTariffMasterList",$scope.tariffList);
														
														angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.tariffId;
																	data.label = value.tariffDesc;
																	$scope.tariffMuliSelectList.push(data);
														});
														
													}
												});	
									
							}
							$scope.init();
							
							$scope.getUnitByOrganization = function()
							{
								var URI = BASE_URL + ROOT_URL+ "/unit/org/"+$scope.organizationId;
								GenericService.serviceAction("GET", URI, {}).then(function(response) 
									{
											if (response.data.status == "success") {
												$scope.stopLoader();
												$scope.unitMasterList = response.data.listObject;
									}else if (response.data.status == "error") 
									{
										$scope.stopLoader();
									}	
								});
							}
							$scope.toTariffList = [];
							$scope.toBillingCategory= [];
							$scope.fromUnitDeptMasterList = [];
							$scope.fromUnitSubSpelMasterList = [];
							$scope.getSelectedUnitData = function(){
							
								if($scope.fromUnitId != undefined && $scope.fromUnitId != null && $scope.fromUnitId != 0 ){
									$scope.toTariffList = [];
									$scope.toBillingCategory= [];
									$scope.fromUnitDeptMasterList = [];
									$scope.fromUnitSubSpelMasterList = [];
									var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.fromUnitId
										}
										var URI = BASE_URL + ROOT_URL
												+ GETACTIVETARIFFMASTERLIST;
										GenericService
												.serviceAction("POST", URI, data)
												.then(
														function(response) {
															if (response.data.status == "success") {
																angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.tariffId;
																			data.label = value.tariffDesc;
																			$scope.toTariffList.push(data);
																});
															}
														});	
									
										
										var URI = BASE_URL + ROOT_URL
												+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.fromUnitId;
										GenericService
												.serviceAction("GET", URI, data)
												.then(
														function(response) {
															$scope.toBillingCategory= [];

															if (response.data.status == "success") {
																angular.forEach(response.data.listObject,
																				function(value,index) {
																					var data = {};
																					data.id = value.billingBedCategoryId;
																					data.label = value.billingBedDesc;
																					$scope.toBillingCategory.push(data);
																				});
															}
														});
									
										
								}
								
								
									$rootScope.startLoader();
									var obj1 = {
											organizationId :$scope.organizationId,
											unitId        :$scope.fromUnitId,
											offset        :0,
											noOfRecordsPerPage :1000
										}
									
									var obj2 = {
											organizationId :$scope.organizationId,
											unitId        :$scope.fromUnitId
										}
									var URI_TO_GET_BILLING_GROUP_LIST = BASE_URL + ROOT_URL + GETUNITSPECIALITYLIST;
									var URI_TO_GET_BILLING_GROUP_COUNT = BASE_URL + ROOT_URL +GETUNITSPECIALITYCOUNT;
									promiseFactoryWithObject.setPromisesWithObject(URI_TO_GET_BILLING_GROUP_LIST, URI_TO_GET_BILLING_GROUP_COUNT, METHOD_POST, METHOD_POST, obj1, obj2).then(function(response) 
									{
										$scope.fromUnitDeptMasterList = response[0].listObject;
									});
								
									
									var GETUNITSUBSPECIALITYLIST = BASE_URL + ROOT_URL + GETUNITSUBSPECIALITYMASTERLIST;
									var GETUNITSUBSPECIALITYCOUNT = BASE_URL + ROOT_URL + GETUNITSUBSPECIALITYMASTERCOUNT;
									
									promiseFactoryWithObject.setPromisesWithObject(GETUNITSUBSPECIALITYLIST, GETUNITSUBSPECIALITYCOUNT, METHOD_POST, METHOD_POST, obj1, obj2).then(function(response) 
									{
										$scope.fromUnitSubSpelMasterList = response[0].listObject;
									});
									
									$scope.stopLoader();
								
							}
							
							
							$scope.getUnitByOrganization();
							
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
							$scope.activePaymentEntitlement();
							// for getting active visit type
							$scope.activeVisitType=function(){							
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
								
								
							}
							
							$scope.activePaymentEntitlementMultiselectList=function(){							
								
								$scope.paymentEntitlementMultiselectList = [
									{
										id:1,
										label:"Self Entitlement"
									}/*,
									{
										id:2,
										label:"Insurance"
									},
									{
										id:3,
										label:"Company"
									}*/
								];
							}
							$scope.activePaymentEntitlementMultiselectList();
							
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
							//$scope.activeBillingBedCategory();
							
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
													$scope.patientTypeListMain = [];
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.id;
																			data.label = value.name;
																			$scope.patientTypeListMain.push(data);
																		});
														console.log('patientTypeList',$scope.patientTypeListMain);
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
							
							$scope.getSelectedValue = function() 
							{
								/*$scope.typeBilling = type;*/
								console.log("tariffList",$scope.selectedTariffList);
													
								
								$scope.selectedVisitType = [];
								$scope.selectedPaymentEnt = [];
								$scope.selectedPatientTypeListTest = [];
								var tariffIdsList = [];
								
								tariffIdsList.push($scope.tariffId);
								console.log("tariffId",tariffIdsList);
								var data = {
										tariffIdList : tariffIdsList,
									}
								if (tariffIdsList.length > 0) {
									var URI = BASE_URL
											+ ROOT_URL
											+ GETPAYMENTENTITLEMENTLISTBYTARIFFIDLIST;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.paymentEntitlementList = response.data.listObject;
															console.log('paymentEntitlementModeldata list',$scope.paymentEntitlementList);
														}
													});
									
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
																					$scope.selectedVisitType.push(data);
																				});
																$scope.activeVisitType();
																console.log('selectedVisitType list',$scope.selectedVisitType);
															}
														});
										
										
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
																					$scope.selectedPatientTypeListTest.push(data);
																				});
																$scope.activePatientType();
																console.log('selectedPatientType list',$scope.selectedPatientTypeListTest);
															}
														});
										
										
								}
								
							}
							
							$scope.getPatientTypes = function() 
							{
								
								$scope.selectedPatientTypeList = [];
								console.log("paymentEntitlementId",$scope.paymentEntitlementId);
								
								var tariffIdsList = [];
								tariffIdsList.push($scope.tariffId);
								console.log("tariffId",tariffIdsList);
								
								
										var data={
												paymentEntitlementId : $scope.paymentEntitlementId,
												patientTypeList : $scope.patientTypeListMain,
												selectedPatientTypeList : $scope.selectedPatientTypeListTest,
												insurancePatientCategory : $scope.insurancePatientCategory,
												companyPatientCategory: $scope.companyPatientCategory
										}
										
										
										var URI = BASE_URL+ ROOT_URL+ "/api/unit/filterPatientType";
										GenericService.serviceAction("POST", URI, data)
												.then(function(response) {
															if (response.data.status == "success") {
																$scope.patientTypeList=[];
																$scope.selectedPatientTypeList=[];
																$scope.patientTypeList = response.data.object;
																$scope.selectedPatientTypeList = response.data.listObject;
															}
														});
										
										
										
								
								
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
								//$scope.getSelectedValueForBillingBedCategory();
								$scope.getBillingBedCategory();
								
							}
//							$scope.getSelectedValueForBillingBedCategory=function(){
//								
//								if($scope.typeBilling == 'S'){
//								console.log("tariffList",$scope.selectedTariffList);
//								var billingBedCategory = [];
//								angular.forEach($scope.selectedTariffList, function(
//										value, index) {
//									billingBedCategory.push(value.id);
//								});
//								var data = {
//										tariffIdList : billingBedCategory,
//									}
//								if ($scope.selectedTariffList.length > 0) {
//									var URI = BASE_URL+ ROOT_URL+ "/api/unit/getBillingBedCategoryByTariffIdList";
//									GenericService.serviceAction("POST", URI, data)
//											.then(function(response) {
//														if (response.data.status == "success") {
//															angular.forEach(response.data.listObject,
//																			function(value,index) {
//																				var data = {};
//																				data.id = value.billingBedCategoryId;
//																				data.label = value.billingBedDesc;
//																				$scope.selectedBillingCategory.push(data);
//																			});
//															//$scope.activeBillingBedCategory();
//															console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
//														}
//													});
//								}
//							}
//							}
							
							//var visitId=2;
							$scope.activeVisitType();
							console.log("My Visit Type isssssssss:"+$scope.visitTypeId);
							$scope.selectedBillingCategory = [];
							$scope.billingCategoryAdd = [];
							$scope.billingCategory = [];
							$scope.getBillingBedCategory = function()
							{
								
								$scope.selectedBillingCategory = [];
								$scope.billingCategoryAdd = [];
								$scope.billingCategory = [];
								
								angular.forEach($scope.selectedVisitType,
										function(value, index) {
									         //$scope.visitType(value.id);
										
									var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+value.id+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET", URI, "")
										.then(function(response) {
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.billingBedCategoryId;
																			data.label = value.billingBedDesc;
																			//$scope.billingCategoryAdd.push(data);
																			
																			var found = $scope.selectedBillingCategory.find(ar=>ar.id == value.billingBedCategoryId );
																			if(found == undefined){
																				$scope.selectedBillingCategory.push(data);
																				//$scope.billingCategoryAdd.push(data);
																			}
																			
																			var found1 = $scope.billingCategory.find(ar=>ar.id == value.billingBedCategoryId );
																			if(found1 == undefined){
																				$scope.billingCategory.push(data);
																			}
																			
																			
																			
																		});
														//$scope.activeBillingBedCategory();
														console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
													}
												});
								
								});
								
								if($scope.selectedVisitType.length == 0){
									$scope.billingCategory = [];
									$scope.selectedBillingCategory = [];
								}
							
							}
							$scope.getBillingBedCategory();
							
							
							
							$scope.getBillingBedCategoryAddNew = function(visitId)
							{
								
								$scope.selectedBillingCategory = [];
								$scope.billingCategoryAdd = [];
								$scope.billingCategory = [];
								
										
									var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+visitId+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET", URI, "")
										.then(function(response) {
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.billingBedCategoryId;
																			data.label = value.billingBedDesc;
																			//$scope.billingCategoryAdd.push(data);
																			
																			var found = $scope.selectedBillingCategory.find(ar=>ar.id == value.billingBedCategoryId );
																			if(found == undefined){
																				$scope.selectedBillingCategory.push(data);
																				//$scope.billingCategoryAdd.push(data);
																			}
																			
																			var found1 = $scope.billingCategory.find(ar=>ar.id == value.billingBedCategoryId );
																			if(found1 == undefined){
																				$scope.billingCategory.push(data);
																			}
																			
																			
																			
																		});
														//$scope.activeBillingBedCategory();
														console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
													}
												});
								
								
								
								if($scope.selectedVisitType.length == 0){
									$scope.billingCategory = [];
									$scope.selectedBillingCategory = [];
								}
							
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
																		function(value,index) 
																		{
																			if(value.isProcedure==null || value.isProcedure!='Y' )
																			{
																					var data = {};
																					data.id = value.serviceId;
																					data.label = value.serviceStandardName;
																					$scope.serviceList.push(data);
																			}
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
							 $scope.selectHeader = function(headerId) {
								 $scope.searchTariffMasterList = [];
							      $scope.headerObject = $scope.tariffMuliSelectList.filter(function(headerMaster) {
							        	var data = {};
							         if (headerMaster.id == headerId) {
							        	 data.id=headerMaster.id;
							        	 data.lable=headerMaster.label;
							        	 $scope.searchTariffMasterList.push(data);
							         }
							        });
							  	console.log("tariffMasterList",$scope.searchTariffMasterList);
							     }
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
									
								      $scope.headerObject = $scope.patientTypeListMain.filter(function(headerMaster) {
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
								  
								  $rootScope.startLoader();
								  
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
											+ "/api/unit/getUnitServiceTariffMasterList";
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
							  
							  //End added tarrif search servics 
							  
							  
							 	$scope.searchTariffServices = function() {	
							 		
							 		$rootScope.startLoader();
							 		
							 		$scope.searchTariffMasterList = [];
									$scope.paymentEntitlementTypeList=[];
									$scope.searchVisitTypeList=[];
									$scope.searchpatientTypeList=[];
									$scope.searchbedTypeCategoryList=[];
									
									
									var tarDesc = $.grep($scope.tariffList, function (disCat) {
							            return disCat.tariffId == $scope.tariffId;
							        })[0].tariffDesc;

									
									var tarData = {};
									tarData.id = $scope.tariffId;
									tarData.lable = tarDesc;
									$scope.searchTariffMasterList.push(tarData);
									
									
									var paymentEnDesc = "Self";
									
									var paymentEntData = {};
									paymentEntData.id = $scope.paymentEntitlementId;
									paymentEntData.lable = paymentEnDesc;
									$scope.paymentEntitlementTypeList.push(paymentEntData);
									
									
								$scope.visitType($scope.visitTypeId);

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

								$scope.serviceTypeList = [];
								angular.forEach($scope.selectedServiceList, function(value, index) {
									$scope.serviceTypeList.push(value.id);
								});
								console.log("serviceTypeList",$scope.serviceModel);

								//$scope.searchTariffMasterList.push($scope.tariffId)
								
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
									serviceList : $scope.serviceTypeList

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

							$scope.applyDate = function() {
								
								$rootScope.startLoader();
								var date = moment($scope.datepicker).format(
								'YYYY-MM-DD');
									
								angular.forEach(
										$scope.allDetailsList,
										function(value, index) {
											$scope.allDetailsList[index].unitId=$scope.unitId,
											$scope.allDetailsList[index].organizationId=$scope.organizationId,
											$scope.allDetailsList[index].createdBy = $scope.userId,
											$scope.allDetailsList[index].updatedBy = $scope.userId,
											$scope.allDetailsList[index].effectiveDate = date.substring(0,10);
										});

								var paymentEntiList = [];
									paymentEntiList.push(parseInt($scope.paymentEntitlementId));
								
								var VisitTypeList = [];
									VisitTypeList.push(parseInt($scope.visitTypeId));
								
								var data={
											effectiveDate : date,
											tariffId:$scope.tariffId,
											paymentEntitlementIdList : paymentEntiList,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											alreadyExist : 0,
											patientTypeList : $scope.searchpatientTypeList,
											bedCategoryList : $scope.searchbedTypeCategoryList,
											serviceList : $scope.serviceTypeList,
											visitTypeIdList : VisitTypeList
										 }
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/unit/service/tariff/uniqueCheck";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														//$scope.allDetailsList=response.data.listObject
														$scope.disableSaveBtn = false;
														$rootScope.stopLoader();
													} else {
														//$scope.allDetailsList=response.data.listObject
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
											unitServiceTariffMasterDtosList:finalDetailsList,
											duplicateIdsList : $scope.duplicateList 
										}
								/*console.log('Final', data); 
								return false;*/
								
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
													$scope.datepicker = null;
													$scope.init();	
													
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
							//the controller
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
							
							$scope.openImport = function(){
								$scope.toTariffList = [];
								$scope.fromTariffList = [];
								$scope.toBillingCategory= [];
								$scope.fromUnitId = null;
								$scope.fromTariffId = 0;
								$scope.fromBillingCategoryList = [];
								if($scope.selectedTariffList.length ==1 ){
									
									angular.forEach($scope.selectedTariffList, function(value, index) {									
										angular.forEach($scope.tariffMuliSelectList, function(val, inx) {		
											if(value.id == val.id){
												$scope.fromTariffId = val.id;
												$scope.fromTariffList.push(val);
											}
										});		
									});		
									
									var checkcount = 0;
									angular.forEach($scope.allDetailsList, function(value, index) {		
										
										
										if(checkcount == 0){
											angular.forEach($scope.billingCategory, function(val, inx) {		
												if(value.billingBedCategoryId == val.id){
													val.fromBillingCategoryId = null;
													$scope.fromBillingCategoryList.push(val);
												}
											});	
											
											checkcount = 1;
										}else{
											var checkLoop = true;
											angular.forEach($scope.fromBillingCategoryList, function(val, inx) {		
												if(value.billingBedCategoryId == val.id){
													 checkLoop = false;
												}
											});	
											
											if(checkLoop == true){
												
												angular.forEach($scope.billingCategory, function(val, inx) {		
													if(value.billingBedCategoryId == val.id){
														val.fromBillingCategoryId = null;
														$scope.fromBillingCategoryList.push(val);
													}
												});	
											}
											
										}
										
											
									});	
									$scope.setThirdTabLevel(1);
									angular.element('#importService').modal('show');
									
								}else{
									
									growl.warning(
											"Please select only one tariff ",
											{
												title : 'Warning!'
											}
									);
								}
								
							}
							
							
							$scope.toTariffId = null;
							$scope.copyAndSaveServiceTariffUnit = function(){
								
								if($scope.fromUnitId == undefined || $scope.fromUnitId == null || $scope.fromUnitId == 0  || $scope.fromUnitId == ""){
									growl.warning(
											"Please select unit ",
											{
												title : 'Warning!'
											}
									);
								}
								
								if($scope.toTariffId == undefined || $scope.toTariffId == null || $scope.toTariffId == 0  || $scope.toTariffId == ""){
									growl.warning(
											"Please select to tariff ",
											{
												title : 'Warning!'
											}
									);
								}
								
								if($scope.effectiveDatepicker == undefined || $scope.effectiveDatepicker == null  || $scope.effectiveDatepicker == ""){
									growl.warning(
											"Please select effective date ",
											{
												title : 'Warning!'
											}
									);
								}
								
								var departmentAllData = [];
								angular.forEach($scope.allDetailsList, function(value, index) {
									var checkDeptAvailable = false
									angular.forEach($scope.fromUnitDeptMasterList, function(dep, inx) {
										
										if(value.specialityId == dep.specialityId && checkDeptAvailable == false){
											checkDeptAvailable = true;
										}
										
									});
									
									if(checkDeptAvailable){
										departmentAllData.push(value);
									}
									
								});
								
								$scope.allDetailsList = departmentAllData;
								
								
								
								var subDepartmentAllData = [];
								angular.forEach($scope.allDetailsList, function(value, index) {
									var checkDeptAvailable = false
									angular.forEach($scope.fromUnitSubSpelMasterList, function(dep, inx) {
										
										if(value.subSpecialityId == dep.subSpecialityId && checkDeptAvailable == false){
											checkDeptAvailable = true;
										}
										
									});
									
									if(checkDeptAvailable){
										subDepartmentAllData.push(value);
									}
									
								});
								
								$scope.allDetailsList = subDepartmentAllData;
								
								
								
								angular.forEach($scope.allDetailsList, function(value, index) {
									angular.forEach($scope.fromBillingCategoryList, function(billingBed, inx) {
										
										if(value.billingBedCategoryId == billingBed.id ){
											value.billingBedCategoryId = billingBed.fromBillingCategoryId;
										}
										
									});
									
									value.effectiveDate = moment($scope.effectiveDatepicker).format('YYYY-MM-DD HH:mm:ss'); 
									value.unitId  =$scope.fromUnitId;
									value.tariffId  =$scope.toTariffId;
									value.organizationId=$scope.organizationId;
									value.createdBy = $scope.userId;
									value.updatedBy = $scope.userId;
									value.billingBedCatId = value.billingBedCategoryId;
								});
								
								var finalList = [];
								angular.forEach($scope.allDetailsList, function(value, index) {
									
									if(value.billingBedCategoryId != undefined &&  value.billingBedCategoryId != null && value.billingBedCatId != undefined &&  value.billingBedCatId != null ){
										finalList.push(value);
									}
									
								});
								

								var data={
											unitServiceTariffMasterDtosList : finalList ,
											duplicateIdsList : $scope.duplicateList ,
										}
								
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/unit/saveUnitServiceTariffMasterFromToUnit";
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
													$scope.effectiveDatepicker = null;
													angular.element('#importService').modal('hide');
													$scope.duplicateList = [];
													$scope.init();	
													
													} else {
														$rootScope.stopLoader();
														$scope.allDetailsList=response.data.listObject
													}

												});
							
								
							}
							//end
							
							$scope.checkExitServices = function(){
								

								$rootScope.startLoader();
								var date = moment($scope.effectiveDatepicker).format('YYYY-MM-DD');
								
								var BillingCategoryto = [];
								
								angular.forEach($scope.fromBillingCategoryList,function(value,index) {
									if(value.fromBillingCategoryId != undefined &&  value.fromBillingCategoryId != null  ){
										var data = {};
										data.id = value.fromBillingCategoryId;
										data.label = value.label;
										BillingCategoryto.push(data);
									}
								});
								
								var serviceList = [];
								angular.forEach($scope.selectedServiceList,function(value,index) {
									serviceList.push(value.id);
								});
								
								var paymentEntiList = [];
								angular.forEach($scope.paymentEntitlementMultiselectList,function(value,index) {
									paymentEntiList.push(value.id);
								});
								
								var VisitTypeList = [];
								angular.forEach($scope.searchVisitTypeList,function(value,index) {
									VisitTypeList.push(value.id);
								});
								

								var data={
											effectiveDate : date,
											tariffId:$scope.toTariffId,
											paymentEntitlementIdList : paymentEntiList,
											organizationId : $scope.organizationId,
											unitId : $scope.fromUnitId,
											alreadyExist : 0,
											patientTypeList : $scope.searchpatientTypeList,
											bedCategoryList : BillingCategoryto,
											serviceList : serviceList,
											visitTypeIdList : VisitTypeList
										 }
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/unit/service/tariff/uniqueCheck";
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
														angular.element('#already_exist_to').modal('show');
													}

												});
							

							
								
							}

                    $scope.checkMaxInoutAmount = function(index){
                        var amount = $scope.allDetailsList[index].rate;
                        if(amount !== undefined && amount !== null && amount !== ''){
                            if($scope.maxInputAmount !== undefined && $scope.maxInputAmount !== null && $scope.maxInputAmount !== ''){
                                if(amount > $scope.maxInputAmount){
                                    $scope.allDetailsList[index].rate = '0';
                                    growl.warning
                                    ("You can enter max amount up to " + $scope.maxInputAmount + "...!",
                                        {title : "WARNING!"}
                                    );
                                }
                            }
                        }
                    }
							
						} ]);
