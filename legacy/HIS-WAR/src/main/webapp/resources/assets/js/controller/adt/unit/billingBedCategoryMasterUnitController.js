'use strict';

/**
 * @Author
 * @name myApp.controller:billingBedCategoryMasterUnitController
 * @description #billingBedCategoryMasterUnitController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'billingBedCategoryMasterUnitController',
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

								/*
								 * $scope.unitId =
								 * sessionStorage.getItem("unitId");
								 * $scope.organizationId =
								 * sessionStorage.getItem("organizationId");
								 * $scope.userId =
								 * sessionStorage.getItem("userId");
								 */
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
								$state.go('login');
								return;
								}

								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.billingBedMasterObj = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.isPeriodicBilling = 'N';
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
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
								$scope.selectedVisitType=[];
								

								var data = {};
								
								var commonobj = {
										organizationId : $scope.organizationId
								}						

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEHIERARCHYLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.hierarchyList = [];

													if (response.data.status == "success") {
														$scope.hierarchyList = response.data.listObject;
														console.log("hierarchyList",$scope.hierarchyList);

													}
												});

							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initBillingBedList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initBillingBedList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 15;
								var URI = BASE_URL + ROOT_URL
										+ GETBILLINGBEDCATEGORYLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ GETBILLINGBEDCATEGORYMASTERCOUNT;

								var data = {
									organizationId : orgId,
									unitId:unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								console.log("data", data);

								var data1 = {
									organizationId : $scope.organizationId,unitId:unitId
								}

								// For Get billingBedCatList Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.billingBedCatList = [];

													if (response.data.status == "success") {
														$scope.billingBedCatList = response.data.listObject;
														console
																.log(
																		"billingBedCatList",
																		$scope.billingBedCatList);
													}
												});

								// For Count billingBedCatList 
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope
																.setPage(1,
																		false);

													}
												});
								$scope.isGstApplicableFlag=false
							}

							$scope.getBillingBedList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 15;
								var URI = BASE_URL + ROOT_URL
										+ GETBILLINGBEDCATEGORYLIST;
								var data = {
									organizationId : $scope.organizationId,
									unitId:$scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get billingBedCat List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.billingBedCatList = [];

													if (response.data.status == "success") {
														$scope.billingBedCatList = response.data.listObject;														
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
										$scope.noOfRecordsPerPage,$scope.unitId);
								if (flag) {
									$scope.getBillingBedList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initBillingBedList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveBillingBed();
							}

							// Function For  saveBillingBed
							$scope.isGstApplicableFlag=false;
							$scope.saveBillingBed = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
						 if ($scope.billingForm.$valid) {
									$scope
											.$broadcast('show-errors-check-validity');
									if ($scope.billingBedMasterObj.isGstApplicable == true) {
										$scope.isGstApplicableFlag = true
									} else {
										$scope.isGstApplicableFlag = false
									}
									var data = {
										billingBedCode : $scope.billingBedMasterObj.billingBedCode,
										billingBedDesc : $scope.billingBedMasterObj.billingBedDesc,
										hierarchyId : $scope.billingBedMasterObj.hierarchyId,
										isGstApplicable : $scope.billingBedMasterObj.isGstApplicable,
										gstAmountRange : $scope.billingBedMasterObj.gstAmountRange,
										gstPercentage : $scope.billingBedMasterObj.gstPercentage,
										status : 'A',
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdDate : $scope.currentDate,
										updatedDate : $scope.currentDate,
										isPeriodicBilling : $scope.isPeriodicBilling,
										visitTypeMasterDtosList : $scope.selectedVisitType
									}
									if ($scope.billingForm.$valid
											&& $scope.billingBedMasterObj.gstAmountRange != 0 ) {

										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;

										} else {
											console.log("save", data);
											console.log("xx",
													$scope.selectedVisitType);
											// return false;
											var URI = BASE_URL
													+ ROOT_URL
													+ SAVEBILLINGBEDCATEGORYMASTER;
											GenericService
													.serviceAction("POST", URI,
															data)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	$scope
																			.init();
																	growl
																			.success(
																					response.data.message,
																					{
																						title : 'Success!'
																					});
																} else {
																	growl
																			.error(
																					response.data.message,
																					{
																						title : 'Error!'
																					});
																}

																$scope
																		.initBillingBedList(
																				$scope.organizationId,
																				$scope.offset,
																				$scope.noOfRecordsPerPage,
																				$scope.unitId);

															});
										}
									}

									else {

										growl
												.error(
														" Amount must be greater than zero ",
														{
															title : ERROR_MSG
														});
										return false;
									}
								} else {
									growl
											.error(
													" Please enter all mandatory field",
													{
														title : ERROR_MSG
													});
									return false;
								}
						 
							}
							//$scope.addedvisitList = [];
							$scope.visitDetails = function(item)
							{
								console.log(item);
								//return false;
								$scope.addedvisitList = [];
								$scope.addedvisitList = item.visitTypeMasterDtosList;
							}
							// Is GST Applicable Flag Checked Or Not
							$scope.getIsGstApplicable=function(billingBedMasterObj)
							{
								if($scope.billingBedMasterObj.isGstApplicable==true)
									{
									$scope.isGstApplicableFlag=true;
									}
								else
									{
									$scope.isGstApplicableFlag=false;
									}
							}
							$scope.gstDetailsList=[];
							$scope.getGstDetails = function(item)
							{
								$scope.gstDetailsList=[];
								$scope.gstDetailsList.push(item);
							}
							// Function For get single billingBed entry
							$scope.showUpdateBtn = function(item) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								if(item.isGstApplicable==true)
								 {
								 $scope.isGstApplicableFlag=true
								 }
							 else
								 {
								 $scope.isGstApplicableFlag=false
								 }

								$scope.billingBedMasterObj.billingBedCode = item.billingBedCode;
								$scope.billingBedMasterObj.billingBedDesc = item.billingBedDesc;
								$scope.billingBedMasterObj.hierarchyId = item.hierarchyId.toString();
								$scope.billingBedMasterObj.billingBedCategoryId =item.billingBedCategoryId;
								$scope.billingBedMasterObj.isGstApplicable =item.isGstApplicable;
								$scope.billingBedMasterObj.gstAmountRange =item.gstAmountRange;
								$scope.billingBedMasterObj.gstPercentage =item.gstPercentage;
								$scope.isPeriodicBilling =item.isPeriodicBilling;
								$scope.selectedVisitType = [];
								$scope.oldSelectVisitList =[];
								angular .forEach(item.visitTypeMasterDtosList, function( value, index) {
									var newAr = {};
									newAr.id = value.id;
									$scope.selectedVisitType.push(newAr);
									$scope.oldSelectVisitList.push(newAr);
								});
								//$scope.selectedVisitType
								//$scope.oldSelectVisitList=item.visitTypeMasterDtosList;
							};

							// Function For update billingBed status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									billingBedCategoryId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ UPDATEBILLINGBEDCATEGORYMASTERSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.init();
														growl
																.success(
																		response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.error(
																		response.data.message,
																		{
																			title : 'Error!'
																		});
													}
												});
							}

							// Function For update billingBed
							$scope.updateBillingBed = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								if($scope.billingBedMasterObj.isGstApplicable==true)
								 {
								 $scope.isGstApplicableFlag=true
								 }
							 else
								 {
								 $scope.isGstApplicableFlag=false
								 }
								var data = {
									billingBedCode : $scope.billingBedMasterObj.billingBedCode,
									billingBedDesc : $scope.billingBedMasterObj.billingBedDesc,
									hierarchyId : $scope.billingBedMasterObj.hierarchyId,								
									billingBedCategoryId : $scope.billingBedMasterObj.billingBedCategoryId,
									isGstApplicable:$scope.billingBedMasterObj.isGstApplicable,
								    gstAmountRange:$scope.billingBedMasterObj.gstAmountRange,
									gstPercentage:$scope.billingBedMasterObj.gstPercentage,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId:$scope.organizationId,
									unitId:$scope.unitId,
									isPeriodicBilling : $scope.isPeriodicBilling
								}
								
								console.log($scope.selectedVisitType);
								console.log($scope.oldSelectVisitList);
								
								
								$scope.newlyAddedVisitTypeList = [];
								$scope.removedVisitTypeList = [];
								
								
								angular .forEach($scope.selectedVisitType, function( value, index) {
									if($scope.oldSelectVisitList.indexOf(value) !== -1) {
										  //alert('already exists!');
										}
									else
										{
										var newAr = {
												id:value.id
										}
										$scope.newlyAddedVisitTypeList.push(newAr);
										}
									});
								
								angular .forEach($scope.oldSelectVisitList, function( value, index) {
									if($scope.selectedVisitType.indexOf(value) !== -1) {
										  //alert('already exists!');
										}
									else
										{
										var newAr = {
												id:value.id
										}
										$scope.removedVisitTypeList.push(newAr);
										}
									});
								
								console.log("newlyAddedVisitTypeList",$scope.newlyAddedVisitTypeList);
								console.log("removedVisitTypeList",$scope.removedVisitTypeList);
								data.newlyAddedVisitTypeList = $scope.newlyAddedVisitTypeList;
								data.removedVisitTypeList = $scope.removedVisitTypeList;
								//return false;
								if ($scope.billingForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATEBILLINGBEDCATEGORYMASTER;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$scope.init();
															growl
																	.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
														} else {
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
														}
														$scope
																.initBillingBedList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}

							}
						} ]);
