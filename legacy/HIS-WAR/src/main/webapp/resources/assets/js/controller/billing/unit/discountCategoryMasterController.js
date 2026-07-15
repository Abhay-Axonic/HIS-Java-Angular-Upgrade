'use strict';

/**
 * @name myApp.controller:discountCategoryMasterController
 * @description #discountCategoryMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'discountCategoryMasterController',
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

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};
								$scope.discountType={
									code:"",
									type:"",
									discountTypeId:""
									
									
								}
								
							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initDiscountTypeMasterList($scope.unitId,
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initDiscountTypeMasterList = function(unitId,orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + "/api/global/unit/getDiscountCategoryList";
								var URI1 = BASE_URL + ROOT_URL
										+ "/api/global/unit/getDiscountCategoryCount";

								var data = {
									unitId:unitId,	
									organizationId : orgId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									unitId:unitId,	
									organizationId : $scope.organizationId
								}

								// For Get Country List Using Offset
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.discountList = [];
													if (response.data.status == "success") {
														$scope.discountList = response.data.listObject;
														console.log("discountList",$scope.discountList);
													}
												});

								// For Count Country List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);

													}
												});
							}

							$scope.getDiscountTypeMasterList = function(unitId,orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + "/api/global/unit/getDiscountCategoryList";
								var data = {
									unitId:unitId,
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Country List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.discountList = [];

													if (response.data.status == "success") {
														$scope.discountList = response.data.listObject;

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
									$scope.getDiscountTypeMasterList($scope.unitId,
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initDiscountTypeMasterList($scope.unitId,$scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.save();
							}

							// Function For Save Country
							$scope.save = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');
								var data = {
									code : $scope.discountType.code,
									type : $scope.discountType.type,
									addedBy : $scope.userId,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									addedDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								}
								if ($scope.discountForm.$valid) {

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										console.log(data);

										var URI = BASE_URL + ROOT_URL
												+ "/api/global/unit/saveDiscountCategory";
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl.error(
																				'Discount category already exists...!!',
																				{
																					title : 'Error!'
																				});
															}

															$scope.initDiscountTypeMasterList($scope.unitId,$scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage);

														});
									}
								}

							}

							// Function For get single country
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

										discountTypeId : id,
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+  "/api/global/unit/getDiscountCategoryById";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {
														$scope.discountType=response.data.listObject[0];														

													} else {
														alert("Error!!");
													}
												});

							};

							// Function For update country status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == true) ? true : false;

								var data = {
									discountTypeId : id,
									isActive : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ "/api/global/unit/updateDiscountCategoryStatus";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Updated Successfully...!!',
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
							$scope.update = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
										code : $scope.discountType.code,
										type : $scope.discountType.type,
										updatedBy : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedDate : $scope.currentDate,
										discountTypeId:$scope.discountType.discountTypeId,
										
									}
								if ($scope.discountForm.$valid) {
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ "/api/global/unit/updateDiscountCategory";
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Already exits...!!',
																			{
																				title : 'Error!'
																			});
														}

														$scope.initDiscountTypeMasterList(
																		$scope.unitId,
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);

													});
								}

							}
						} ]);
