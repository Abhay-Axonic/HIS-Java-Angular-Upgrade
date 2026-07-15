'use strict';

/**
 * @Author
 * @name myApp.controller:mealPreferenceMasterController
 * @description #mealPreferenceMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'mealPreferenceMasterController',
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
								$scope.mealPrefMasterObj = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup						

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
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETMEALPREFERENCEMASTERLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ GETMEALPREFERENCEMASTERCOUNT;

								var data = {
									organizationId : orgId,
									unitId:unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								console.log("data", data);

								var data1 = {
										unitId:unitId,
									organizationId : $scope.organizationId
								}

								// For Get billingBedCatList Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.mealPrefMasterList = [];

													if (response.data.status == "success") {
														$scope.mealPrefMasterList = response.data.listObject;
														console
																.log(
																		"mealPrefMasterList",
																		$scope.mealPrefMasterList);
													}
												});

								// For Count billingBedCatList 
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log("count",response.data);
														$scope.commonListCount = response.data.object;
														$scope
																.setPage(1,
																		false);

													}
												});
							}

							$scope.getBillingBedList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETMEALPREFERENCEMASTERLIST;
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
													$scope.mealPrefMasterList = [];

													if (response.data.status == "success") {
														$scope.mealPrefMasterList = response.data.listObject;														
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
								$scope.saveMealPrefMaster();
							}

							// Function For  saveBillingBed
							$scope.saveMealPrefMaster = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

							 $scope.$broadcast('show-errors-check-validity');
								var data = {
										mealPreferenceCode : $scope.mealPrefMasterObj.mealPreferenceCode,
										mealPreferenceDesc : $scope.mealPrefMasterObj.mealPreferenceDesc,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									unitId:$scope.unitId,
									organizationId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								}
							 if ($scope.mealPreferenceForm.$valid) {

								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									console.log("save", data);

									var URI = BASE_URL + ROOT_URL
											+ SAVEMEALPREFERENCEMASTER;
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
															$scope.$broadcast('show-errors-reset');
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

							}

							// Function For get single billingBed entry
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

								}
								var URI = BASE_URL + ROOT_URL
										+ GETMEALPREFERENCEMASTERBYID + "/"
										+ id + "/" + $scope.organizationId+"/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.mealPrefMasterObj.mealPreferenceCode = response.data.listObject[0].mealPreferenceCode;
														$scope.mealPrefMasterObj.mealPreferenceDesc = response.data.listObject[0].mealPreferenceDesc;
														$scope.mealPrefMasterObj.mealPreferenceId = response.data.listObject[0].mealPreferenceId;
													} else {
														alert("Error!!");
													}
												});								

							};

							// Function For update billingBed status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');

								type = (type == "A") ? "A" : "I";

								var data = {
									mealPreferenceId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ UPDATEMEALPREFERENCEMASTERSTATUS;
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
														$scope.$broadcast('show-errors-reset');
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

							// Function For updateMealPrefMaster
							$scope.updateMealPrefMaster = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
							$scope.$broadcast('show-errors-check-validity');
								var data = {
										mealPreferenceCode : $scope.mealPrefMasterObj.mealPreferenceCode,
										mealPreferenceDesc : $scope.mealPrefMasterObj.mealPreferenceDesc,
										mealPreferenceId : $scope.mealPrefMasterObj.mealPreferenceId,
										unitId:$scope.unitId,
										organizationId : $scope.organizationId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate
								}
							
									var URI = BASE_URL + ROOT_URL
											+ UPDATEMEALPREFERENCEMASTER;
								if ($scope.mealPreferenceForm.$valid) {
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
															$scope.$broadcast('show-errors-reset');
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
