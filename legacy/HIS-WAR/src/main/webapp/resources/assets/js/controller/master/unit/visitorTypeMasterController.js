'use strict';

angular
		.module('myApp')
		.controller(
				'visitorTypeMasterController',
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
								$scope.visitorType={
									visitorPassTypeCode:"",
									visitorPassTypeDesc:"",
									visitorPassTypeId:""
									
									
								}
								
							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initVisitorTypeMasterList($scope.unitId,
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initVisitorTypeMasterList = function(unitId,orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + "/api/unit/visitor/getPassTypeMasterList";
								var URI1 = BASE_URL + ROOT_URL
										+ "/api/unit/visitor/getPassTypeMasterCount";

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

								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.visitorList = [];
													if (response.data.status == "success") {
														$scope.visitorList = response.data.listObject;
														console.log("visitorList",$scope.visitorList);
													}
												});

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

							$scope.getVisitorTypeMasterList = function(unitId,orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + "/api/unit/visitor/getPassTypeMasterList";
								var data = {
									unitId:unitId,
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.visitorList = [];

													if (response.data.status == "success") {
														$scope.visitorList = response.data.listObject;

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
									$scope.getVisitorTypeMasterList($scope.unitId,
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initVisitorTypeMasterList($scope.unitId,$scope.organizationId,
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
								
								$scope.visitorType.createdBy= $scope.userId;
								$scope.visitorType.updatedBy= $scope.userId;
								$scope.visitorType.organizationId= $scope.organizationId;
								$scope.visitorType.unitId= $scope.unitId;
								if ($scope.visitorPassForm.$valid) {

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										console.log($scope.visitorType);

										var URI = BASE_URL + ROOT_URL
												+ "/api/unit/visitor/savePassTypeMaster";
										GenericService
												.serviceAction("POST", URI,
														$scope.visitorType)
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
																growl
																		.error(
																				'Already exits...!!',
																				{
																					title : 'Error!'
																				});
															}

															$scope.initVisitorTypeMasterList($scope.unitId,$scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage);

														});
									}
								}

							}

							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

										visitorPassTypeId : id,
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+  "/api/unit/visitor/getPassTypeMasterById"
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {
														$scope.visitorType=response.data.listObject[0];														

													} else {
														alert("Error!!");
													}
												});

							};

							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == 'A') ? 'A' : 'I';

								var data = {
									visitorPassTypeId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/visitor/updatePassTypeMasterStatus";
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

							$scope.update = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								$scope.visitorType.organizationId= $scope.organizationId;
								$scope.visitorType.unitId= $scope.unitId;
								$scope.visitorType.updatedBy = $scope.userId;
								if ($scope.visitorPassForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ "/api/unit/visitor/updatePassTypeMaster";
									GenericService
											.serviceAction("POST", URI, $scope.visitorType)
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

														$scope.initVisitorTypeMasterList(
																		$scope.unitId,
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);

													});
								}

							}
						} ]);
