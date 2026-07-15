'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:nationalityMasterController
 * @description #nationalityMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nationalityMasterController',
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
								$scope.createdBy =cookieObject.userId;
								$scope.updatedBy =cookieObject.userId;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.nationality = {}
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};
								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETNATIONALITYLIST; GenericService
								 * .serviceAction("GET", URI, data) .then(
								 * function(response) { $scope.nationalityList =
								 * [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.nationalityList =
								 * response.data.listObject; console
								 * .log($scope.nationalityList);
								 *  } });
								 */

							}

							$scope.init();
							/* getActiveCountryList() function for getting country active list strat*/
							$scope.getActiveCountryList = function() {
								
								var data = {};
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.organizationId;
								GenericService.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.countryList = [];
													if (response.data.status == "success") {
														$scope.countryList = response.data.listObject;
														console.log("countryList",$scope.countryList);
													}
												});
							}

							$scope.getActiveCountryList(); 
							/* getActiveCountryList() function for getting  active state list start*/

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initNationalityMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initNationalityMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETNATIONALITYLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTNATIONALITYMASTER;

								var data = {
									organizationId : orgId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId
								}

								// For Get Nationality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.nationalityList = [];

													if (response.data.status == "success") {
														$scope.nationalityList = response.data.listObject;

													}
												});

								// For Count Nationality List
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

							$scope.getNationalityMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETNATIONALITYLIST;
								var data = {
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Nationality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.nationalityList = [];

													if (response.data.status == "success") {
														$scope.nationalityList = response.data.listObject;

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
									$scope.getNationalityMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initNationalityMasterList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveNationality();
							}

							// Function For save Nationality
							$scope.saveNationality = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');

								var data = {
									nationalityCode : $scope.nationality.nationalityCode,
									nationalityName : $scope.nationality.nationalityDesc,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									countryId : $scope.nationality.countryId
								}
								if ($scope.nationalityform.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {

										console.log(data);

										var URI = BASE_URL + ROOT_URL
												+ ADDNATIONALITY;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Nationality added successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl
																		.error(
																				'Nationality already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
															$scope
																	.initNationalityMasterList(
																			$scope.organizationId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);

														});
									}
								}

							}

							// Function For get single Nationality
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								var data = {

									nationalityId : id
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLENATIONALITY;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {

														$scope.nationality.nationalityId = response.data.listObject[0].nationalityId;
														$scope.nationality.nationalityCode = response.data.listObject[0].nationalityCode;
														$scope.nationality.nationalityDesc = response.data.listObject[0].nationalityName;
														$scope.nationality.countryId = response.data.listObject[0].countryId.toString();

													} else {
														alert("Error!!");
													}
												});

							};

							// Function For update Nationality status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									nationalityId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATENATIONALITYSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Nationality Updated successfully...!!',
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

							// Function For update Nationality
							$scope.updateNationality = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
									nationalityCode : $scope.nationality.nationalityCode,
									nationalityName : $scope.nationality.nationalityDesc,
									nationalityId : $scope.nationality.nationalityId,
									countryId : $scope.nationality.countryId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId : $scope.organizationId
								}
								if ($scope.nationalityform.$valid) {
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ UPDATENATIONALITY;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Nationality updated successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Nationality already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
														$scope
																.initNationalityMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);

													});
								}
							}

						} ]);
