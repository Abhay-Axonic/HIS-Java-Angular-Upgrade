'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:tariffMasterController
 * @description #tariffMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'tariffMasterController',
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
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								// for From date picker
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								// for To date picker
								$scope.openDatePickerTo = function() {
									$scope.datepickerTo.opened = true;
								};

								$scope.datepickerTo = {
									opened : false
								};

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.nutritionId = 0;

								var data = {};

								$scope.example14model = [];
								$scope.selectedPaymentEnt=[];	
								$scope.selectedVisitType=[];
								$scope.selectedBillingCategory=[];
								$scope.selectedPatientTypeList=[];
								$scope.tariffMasterObj = {

									tariffId : "",
									tariffCode : "",
									tariffDesc : "",
									validFrom : "",
									validTo : "",
									status:"",
								}
								// for getting active entitlement

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
								// for getting active visit type

								var URI = BASE_URL + ROOT_URL
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
												});
								
								// for getting patient type

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPATIENTCATEGORYBYORGID+"/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
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
								
								// for get active billing category list

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
							/*	// get tariffMasterList
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}

								var URI = BASE_URL + ROOT_URL
										+ GETTARIFFMASTERLIST;

								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.tariffMasterList = [];

													if (response.data.status == "success") {
														$scope.tariffMasterList = response.data.listObject;
														console
																.log(
																		"tariffMasterList",
																		$scope.tariffMasterList);

													}
												});*/

							}
							$scope.init();
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initTariffList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initTariffList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETTARIFFMASTERLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ "/api/unit/tariffMasterCount";

								var data = {
									organizationId : orgId,
									unitId : unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								console.log("data", data);

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}

								// For Get tariff List Using Offset
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.tariffMasterList = [];
													if (response.data.status == "success") {
														$scope.tariffMasterList = response.data.listObject;
														console.log("tariffMasterList",$scope.tariffMasterList);
													}
												});

								// For Count tariff List
								GenericService.serviceAction("POST", URI1, data1)
										.then(function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);

													}
												});
							}

							$scope.getTariffList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETTARIFFMASTERLIST;
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get designation List Using Offset
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.tariffMasterList = [];
													if (response.data.status == "success") {
														$scope.tariffMasterList = response.data.listObject;														
													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getTariffList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initTariffList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveTariff();
							}
							
							$scope.saveTariff = function() {
								$scope.$broadcast('show-errors-check-validity');
								
								var paymentEntitlementList = [];
								angular.forEach($scope.example14model,
										function(value, index) {
											paymentEntitlementList
													.push(value.id);
										});
//								console.log("payment ent ",$scope.selectedPaymentEnt); return false;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									tariffCode : $scope.tariffMasterObj.tariffCode,
									tariffDesc : $scope.tariffMasterObj.tariffDesc,
									validFrom : moment(
											$scope.tariffMasterObj.validFrom)
											.format('DD-MM-YYYY HH:mm:ss'),
									validTo : moment(
											$scope.tariffMasterObj.validTo)
											.format('DD-MM-YYYY HH:mm:ss'),
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									status : 'A',
									paymentEntitlementIdList : $scope.selectedPaymentEnt,
									visitTypeList:$scope.selectedVisitType,
									patientTypeList:$scope.selectedPatientTypeList,
									billingBedList:null
								}	
								console.log("saveTariffMaster", data);
//								 return false;
								var URI = BASE_URL + ROOT_URL
										+ SAVETARIFFMASTER;
								if ($scope.tariffMasterForm.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$rootScope.startLoader();
								       GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl
																.success(
																		'Tariff master added successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
														$scope.getTariffList(
																$scope.organizationId,
																$scope.pager.startIndex,
																$scope.pager.pageSize,$scope.unitId);
													} else {
														$rootScope.stopLoader();
														growl
																.error(
																		'Tariff master already exits...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
								}
							}
							$scope.showMapperDetails = function(id) {
								var data = {
									tariffId : id,
								}
								console.log(data);
								//for payment entilment 
								var URI = BASE_URL + ROOT_URL
										+ GETPAYMENTENTITLEMENTLISTBYTARIFFID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.paymentEntitleDetailsList = [];
													if (response.data.status == "success") {
														$scope.paymentEntitleDetailsList = response.data.listObject;

													} else {
														alert("Error!!");
													}
												});
								//for visit Type
								var URI = BASE_URL + ROOT_URL
								+ "/api/unit/getVisitTypeListByTariffId";
						       GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											$scope.visitTypeDetailsList = [];
											if (response.data.status == "success") {
												$scope.visitTypeDetailsList = response.data.listObject;
												console.log("visitTypeDetailsList",$scope.visitTypeDetailsList);
											} else {
												alert("Error!!");
											}
										});
						     //for patient Type
								var URI = BASE_URL + ROOT_URL
								+ "/api/unit/getPatientTypeListByTariffId";
						       GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											$scope.patientTypeDetailsList = [];
											if (response.data.status == "success") {
												$scope.patientTypeDetailsList = response.data.listObject;
												console.log("patientTypeDetailsList",$scope.patientTypeDetailsList);
											} else {
												alert("Error!!");
											}
										});
						     //for billing bed category
								var URI = BASE_URL + ROOT_URL
								+ "/api/unit/getBillingBedCategoryListByTariffId";
						       GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											$scope.billingbedDetailsList = [];
											if (response.data.status == "success") {
												$scope.billingbedDetailsList = response.data.listObject;
												console.log("billingbedDetailsList",$scope.billingbedDetailsList);
											} else {
												alert("Error!!");
											}
										});

							};

							$scope.showUpdateBtn = function(id) {
								$scope.selectedPaymentEnt=[];
								$scope.selectedVisitType=[];
								$scope.selectedPatientTypeList=[];
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.tariffMasterObj.tariffId = id;
								var data = {
									tariffId : id,
								}
								var URI = BASE_URL + ROOT_URL
										+ GETTARIFFLISTBYID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(
																		"SingleRecord",
																		response.data.object);

														// return false;
														$scope.tariffMasterObj.tariffCode = response.data.object.tariffCode;
														$scope.tariffMasterObj.tariffDesc = response.data.object.tariffDesc;
														$scope.tariffMasterObj.validFrom = new Date(
																response.data.object.validFrom);
														$scope.tariffMasterObj.validTo = new Date(
																response.data.object.validTo);
														$scope.tariffId = response.data.object.tariffId;
														$scope.applicablePaymentEntitlement = response.data.object.applicablePaymentEntitlementIdList2;
														$scope.applicablePatientTypeList = response.data.object.patientTypeList;
														$scope.applicableVisitTypeList = response.data.object.visitTypeList;
														console.log("aaa",$scope.applicablePaymentEntitlement);
														$scope.example14model = [];
														angular.forEach($scope.applicablePaymentEntitlement,
																		function(value,index) {
																			var d = {};
																			d.id = value.applicablePaymentEntitlementId;
																			d.lable = value.paymentEntitlementDesc;
																			$scope.selectedPaymentEnt.push(d);
																		});
														angular.forEach($scope.applicableVisitTypeList,
																function(value,index) {
																	var d = {};
																	d.id = value.visitTypeId;
																	d.lable = value.visitTypeName;
																	$scope.selectedVisitType.push(d);
																});
														angular.forEach($scope.applicablePatientTypeList,
																function(value,index) {
																	var d = {};
																	d.id = value.patientTypeId;
																	d.lable = value.patientCategory;
																	$scope.selectedPatientTypeList.push(d);
																});

													} else {
														alert("Error!!");
													}
												});

							};

							// Function For update Nutrition
							$scope.updateTariff = function() {
								 $rootScope.startLoader();
								 $scope.$broadcast('show-errors-check-validity');

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var paymentEntitlementList = [];
								angular.forEach($scope.example14model,
										function(value, index) {
											paymentEntitlementList
													.push(value.id);
										});

								var paymentEntitlementListOld = [];
								angular.forEach($scope.applicablePaymentEntitlement,
												function(value, index) {
													paymentEntitlementListOld.push(value.applicablePaymentEntitlementId);
												});

								$scope.dataUpdate = {
									tariffId : $scope.tariffMasterObj.tariffId,
									tariffCode : $scope.tariffMasterObj.tariffCode,
									tariffDesc : $scope.tariffMasterObj.tariffDesc,
									validFrom : moment(
											$scope.tariffMasterObj.validFrom)
											.format('DD-MM-YYYY HH:mm:ss'),
									validTo : moment(
											$scope.tariffMasterObj.validTo)
											.format('DD-MM-YYYY HH:mm:ss'),
//									applicablePaymentEntitlementIdList : paymentEntitlementList,
//									applicablePaymentEntitlementIdListOld : paymentEntitlementListOld,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									paymentEntitlementIdList : $scope.selectedPaymentEnt,
									visitTypeList:$scope.selectedVisitType,
									patientTypeList:$scope.selectedPatientTypeList,
								}

//								console.log("new", paymentEntitlementList);
//								console.log("old", paymentEntitlementListOld);
//								console.log("Update", $scope.dataUpdate);
//								return false;
								if ($scope.tariffMasterForm.$valid) {
								var URI = BASE_URL + ROOT_URL
										+ UPDATETARIFFMASTER;
								GenericService.serviceAction("POST", URI,$scope.dataUpdate)
										.then(function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl.success('Tariff master updated successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.$broadcast('show-errors-reset');
														$scope.init();
														$scope.getTariffList(
																$scope.organizationId,
																$scope.pager.startIndex,
																$scope.pager.pageSize,$scope.unitId);
													} else {
														$rootScope.stopLoader();
														growl.error('Tariff master already exits...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
								}

							}
							//update tariff master status
							$scope.updateStatus = function(id, type) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									tariffId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATETARIFFMASTERSTATUS;
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {

												if (response.data.status == "success" && response.data.message == "Processing Transaction") {
													$scope.initTariffList($scope.organizationId,
															$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
													growl.warning('Tariff Is Under Transaction Can Not Be Deactivated...!!',
																	{
																		title : 'Warning!'
																	});
												}
												else if (response.data.status == "success") {
														growl.success('Tariff Master Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													}
													else {
														growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							//update tariff master mapper status
							$scope.updateStatusMapper = function(id, type) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									tariffPaymentEntitlementMapperId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATESTATUSFORTARIFFPAYMENTMAPPER;
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {

													if (response.data.status == "success") {
														growl.success('Payment Entitlement Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							//update tariff master mapper status
							$scope.updateStatusVisitMapper = function(id, type) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									tariffVisitTypeMapperId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/updateStatusForTariffVisitTypeMapper";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {

													if (response.data.status == "success") {
														growl.success('Updated successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							//update tariff master mapper status
							$scope.updateStatusPatientMapper = function(id, type) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									tariffPatientTypeMapperId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/updateStatusForTariffPatientTypeMapper";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {

													if (response.data.status == "success") {
														growl.success('Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							//update tariff master mapper status
							$scope.updateStatusBillingMapper = function(id, type) {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									tariffBillingBedCategoryMapperId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/updateStatusForTariffBillingBedCategoryMapper";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {

													if (response.data.status == "success") {
														growl.success('Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
													} else {
														growl.error('Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
												});
							}

						} ]);