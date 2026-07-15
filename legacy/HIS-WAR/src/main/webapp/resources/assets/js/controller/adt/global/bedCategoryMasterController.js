'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:bedCategoryMasterController
 * @description # bedCategoryMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'bedCategoryMasterController',
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

							// init function description
							$scope.init = function() {

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

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for common popup

								$rootScope.loginpage = true;

								$scope.bedCategory = {
									isBedRetention : 'N',
									isIcu : 'false',
								}

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {};
								var commonobj = {
										organizationId : $scope.organizationId
								}
								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETBEDCATEGORYLIST; GenericService
								 * .serviceAction("GET", URI, data) .then(
								 * function(response) { $scope.bedCategoryList =
								 * [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.bedCategoryList =
								 * response.data.listObject;
								 *  } });
								 */

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEHIERARCHYLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.hierarchyList = [];

													if (response.data.status == "success") {
														$scope.hierarchyList = response.data.listObject;

													}
												});

								var URI = BASE_URL + ROOT_URL
										//+ GETOCCUPANCYUNITLIST;
										+ GETACTIVEOCCUPANCYUNITLIST;//minesh
								GenericService
										.serviceAction("POST", URI, commonobj)
										.then(
												function(response) {
													$scope.occupancyUnitList = [];

													if (response.data.status == "success") {
														$scope.occupancyUnitList = response.data.listObject;

													}
												});
								
								var data = {
										
								};
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.billingBedList = [];

											if (response.data.status == "success") {
												$scope.billingBedList = response.data.listObject;
												console.log("billingBedList",$scope.billingBedList);

											}
										});
						// getting auto render services
						var URI = BASE_URL + ROOT_URL+ "/billing/service/autoRendered/"+$scope.organizationId+"/"+$scope.unitId;
						GenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success")
								$scope.consultationServiceList = response.data.listObject;
								$scope.consultationServiceListForPeriodic = response.data.listObject;
								console.log('service list',$scope.consultationServiceList)
						});

							};
							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initBedCategoryMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initBedCategoryMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETBEDCATEGORYLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTBEDCATEGORYMASTER;

								var data = {
									organizationId : orgId,
									unitId :$scope.unitId ,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									unitId :$scope.unitId ,
									organizationId : $scope.organizationId
								}

								// For Get Bed Category List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.bedCategoryList = [];

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.bedCategoryList = response.data.listObject;
														console.log("bedCategoryList",$scope.bedCategoryList);

													}
												});

								// For Count Bed Category List
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
							}

							$scope.getBedCategoryMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								$rootScope.startLoader();
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETBEDCATEGORYLIST;
								var data = {

									unitId :$scope.unitId,
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Bed Category List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.bedCategoryList = [];

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.bedCategoryList = response.data.listObject;

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
									$scope.getBedCategoryMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initBedCategoryMasterList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveBedCategory();
							}

							// Save function for add speciality
							$scope.saveBedCategory = function() {
								if($scope.popUpFlag==true)
									{
									var duplicateCounter=0;
									angular.forEach($scope.bedCategoryList, function (value, key) {
										if(value.hierarchyId==parseInt($scope.bedCategory.hierarchyId) && $scope.bedCategory.isIcu!=true)
											{
											duplicateCounter++;
											}
									});
									if(duplicateCounter>=1)
										{
										growl.warning("Duplicate Hierarchy Is Not Allowed");
										return false;
										}
									}
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.bedcategoryform.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$rootScope.startLoader();
										// var data = $scope.speciality;
										var data = {
											bedCategoryCode : $scope.bedCategory.bedCategoryCode,
											bedCategoryDesc : $scope.bedCategory.bedCategoryName,
											isBedRetention : $scope.bedCategory.isBedRetention,
											isIcu : $scope.bedCategory.isIcu,
											hierarchyId : $scope.bedCategory.hierarchyId,
											occupancyUnitId : $scope.bedCategory.occupancyUnitId,
											billingBedCategoryId : $scope.bedCategory.billingBedCategoryId,
											status : 'A',
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											unitId:$scope.unitId,
											organizationId : $scope.organizationId,
											updatedDate : $scope.currentDate,
											createdDate : $scope.currentDate,
											serviceId : $scope.bedCategory.serviceId
										}

										console.log(data);
										var URI = BASE_URL + ROOT_URL
												+ ADDBEDCATEGORY;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																$rootScope.stopLoader();
																growl
																		.success(
																				'Bed Category added Successfully...!!',
																				{
																					title : 'Success!'
																				});

																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																$rootScope.stopLoader();
																growl
																		.error(
																				'Bed Category already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
															$scope
																	.initBedCategoryMasterList(
																			$scope.organizationId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
														});
									}
								}

							};

							// function for get speciality object for update
							$scope.showUpdateBtn = function(id,index) {
								$scope.indexToUpdate=index;
								$rootScope.startLoader();

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								var data = {
									bedCategoryId : id
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLEBEDCATEGORY;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														console.log("id data=",response.data.listObject);
														$scope.bedCategory.bedCategoryId = response.data.listObject[0].bedCategoryId;
														$scope.bedCategory.bedCategoryCode = response.data.listObject[0].bedCategoryCode;
														$scope.bedCategory.bedCategoryName = response.data.listObject[0].bedCategoryDesc;
														$scope.bedCategory.hierarchyId = response.data.listObject[0].hierarchyId
																.toString();
														$scope.bedCategory.occupancyUnitId = response.data.listObject[0].occupancyUnitId
																.toString();
														$scope.bedCategory.isBedRetention = response.data.listObject[0].isBedRetention;
														$scope.bedCategory.isIcu = response.data.listObject[0].isIcu;
														$scope.bedCategory.billingBedCategoryId = response.data.listObject[0].billingBedCategoryId.toString();
														$scope.bedCategory.serviceId = response.data.listObject[0].serviceId!=null ? response.data.listObject[0].serviceId : null;
													} else {
														alert("Error!!");
														$rootScope.stopLoader();
													}
												});

							};


							// function for update status speciality
							$scope.updateStatus = function(id, type) {
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";
								var data = {
									bedCategoryId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATEBEDCATEGORYSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl
																.success(
																		'Bed Category Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
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
							$scope.saveFlag=false;
							$scope.checkValidHierarchy = function(info)
							{
								if(isNaN(info.hierarchyId) || info.hierarchyId==undefined || info.hierarchyId==null || info.hierarchyId=='')
									{
										growl.error("Hierarchy is required");
										$scope.saveFlag=false;
										return false;
									}
								else
									{
									  $scope.saveFlag=true;
									}
							}
							$scope.setHierarchy = function()
							{
								var valueArr = $scope.bedCategoryList.map(function(item){ return parseInt(item.hierarchyId) });
								var isDuplicate = valueArr.some(function(item, idx){ 
								    return valueArr.indexOf(item) != idx 
								});
								if(isDuplicate==true)
									{
									growl.warning("Duplicate Hierarchy Is Not Allowed");
									return false;
									}
								var dayCareIpBedCategoryList=[];
								var data = {
										bedCategoryCode : $scope.bedCategory.bedCategoryCode,
										bedCategoryDesc : $scope.bedCategory.bedCategoryName,
										isBedRetention : $scope.bedCategory.isBedRetention,
										isIcu : $scope.bedCategory.isIcu,
										hierarchyId : $scope.bedCategory.hierarchyId,
										occupancyUnitId : $scope.bedCategory.occupancyUnitId,
										billingBedCategoryId : $scope.bedCategory.billingBedCategoryId,
										bedCategoryId : $scope.bedCategory.bedCategoryId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										serviceId: $scope.bedCategory.serviceId,
										unitId:$scope.unitId,
										organizationId : $scope.organizationId,
										dayCareIpBedCategoryList:[],
									}
								angular.forEach($scope.bedCategoryList, function (value, key) {
								var data1 = {
										bedCategoryCode : value.bedCategoryCode,
										bedCategoryDesc : value.bedCategoryDesc,
										isBedRetention : value.isBedRetention,
										isIcu : value.isIcu,
										hierarchyId : value.hierarchyId,
										occupancyUnitId : value.occupancyUnitId,
										billingBedCategoryId : value.billingBedCategoryId,
										bedCategoryId : value.bedCategoryId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										serviceId: value.serviceId,
										unitId:$scope.unitId,
										organizationId : $scope.organizationId,
									}
								dayCareIpBedCategoryList.push(data1);
								});
								data.dayCareIpBedCategoryList=dayCareIpBedCategoryList;
								var URI = BASE_URL + ROOT_URL
								+ UPDATEBEDCATEGORY +"/updateHierarchy";
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												growl
														.success(
																'Bed category updated Successfully...!!',
																{
																	title : 'Success!'
																});
												$scope
														.$broadcast('show-errors-reset');
												$scope.init();
												// alert("ds");
												$('#setHierarchy').modal('hide');
											} else {
												$rootScope.stopLoader();
												growl
														.error(
																'Bed category already exits...!!',
																{
																	title : 'Error!'
																});
											}
											$scope
											.initBedCategoryMasterList(
													$scope.organizationId,
													$scope.offset,
													$scope.noOfRecordsPerPage);
										});
								
							}
							// function for update speciality
							$scope.HirarchyChangedFlag=false;
							$scope.updateBedCategory = function() {
								var duplicateCounter=0;
								$scope.bedCategoryList[$scope.indexToUpdate].hierarchyId=$scope.bedCategory.hierarchyId;
								angular.forEach($scope.bedCategoryList, function (value, key) {
									if(value.hierarchyId==parseInt($scope.bedCategory.hierarchyId) && $scope.bedCategory.isIcu!=true)
										{
										duplicateCounter++;
										}
								});
								if(duplicateCounter>1)
									{
									growl.warning("Duplicate Hierarchy Is Not Allowed");
									return false;
									}
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');
								if ($scope.bedcategoryform.$valid) {
									$rootScope.startLoader();
									var data = {
										bedCategoryCode : $scope.bedCategory.bedCategoryCode,
										bedCategoryDesc : $scope.bedCategory.bedCategoryName,
										isBedRetention : $scope.bedCategory.isBedRetention,
										isIcu : $scope.bedCategory.isIcu,
										hierarchyId : $scope.bedCategory.hierarchyId,
										occupancyUnitId : $scope.bedCategory.occupancyUnitId,
										billingBedCategoryId : $scope.bedCategory.billingBedCategoryId,
										bedCategoryId : $scope.bedCategory.bedCategoryId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										serviceId: $scope.bedCategory.serviceId,
										unitId:$scope.unitId,
										organizationId : $scope.organizationId,
									}
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ UPDATEBEDCATEGORY;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$rootScope.stopLoader();
															growl
																	.success(
																			'Bed category updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
															// alert("ds");

														} else {
															$rootScope.stopLoader();
															growl
																	.error(
																			'Bed category already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
														$scope
																.initBedCategoryMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
													});
								}
							}

						} ]);
