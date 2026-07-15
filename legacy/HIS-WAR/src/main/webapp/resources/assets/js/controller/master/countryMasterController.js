'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:countryMasterController
 * @description #countryMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'countryMasterController',
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

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							
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

								
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.country = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};
								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETCOUNTRYLIST; GenericService
								 * .serviceAction("GET", URI, data) .then(
								 * function(response) { $scope.countryList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.countryList =
								 * response.data.listObject; console
								 * .log($scope.countryList); } });
								 */
							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initCountryMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initCountryMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + GETCOUNTRYLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTCOUNTRYMASTER;

								var data = {
									organizationId : orgId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId
								}

								// For Get Country List Using Offset
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.countryList = [];
													if (response.data.status == "success") {
														$scope.countryList = response.data.listObject;
														console.log("countryList",$scope.countryList);
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

							$scope.getCountryMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + GETCOUNTRYLIST;
								var data = {
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
													$scope.countryList = [];

													if (response.data.status == "success") {
														$scope.countryList = response.data.listObject;

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
									$scope.getCountryMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initCountryMasterList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveCountry();
							}

							// Function For Save Country
							$scope.saveCountry = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								$scope.$broadcast('show-errors-check-validity');
								var data = {
									countryCode : $scope.country.countryCode,
									countryName : $scope.country.countryName,
									countryInitial : $scope.country.countryInitial,
									countryCallingCode : $scope.country.countryCallingCode,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									contactDigit:$scope.country.contactDigit,
									organizationId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								}
							    	
								
								if ($scope.countryform.$valid) {

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										console.log(data);
										var regEx = /^[+-]?\d+$/; // regex for validating calling country code
										 if($scope.country.countryCallingCode!=null && $scope.country.countryCallingCode!=undefined && $scope.country.countryCallingCode!=''){
											  if(!regEx.test($scope.country.countryCallingCode)){
												  growl.warning("Please enter valid country calling code",{
														title : 'warning'
														});
														return false;
											  } 
					        			 }
										var URI = BASE_URL + ROOT_URL
												+ ADDCOUNTRY;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Country added Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope
																		.$broadcast('show-errors-reset');
																$scope.init();
															} else {
																growl
																		.error(
																				'Country already exits...!!',
																				{
																					title : 'Error!'
																				});
															}

															$scope
																	.initCountryMasterList(
																			$scope.organizationId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);

														});
									}
								}

							}

							// Function For get single country
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

									countryId : id,
									organizationId : $scope.organizationId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLECOUNTRY;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													// $scope.speciality =[];
													if (response.data.status == "success") {
														$scope.country.countryId = response.data.listObject[0].countryId;
														$scope.country.countryCode = response.data.listObject[0].countryCode;
														$scope.country.countryName = response.data.listObject[0].countryName;
														$scope.country.countryInitial = response.data.listObject[0].countryInitial;
														$scope.country.countryCallingCode = response.data.listObject[0].countryCallingCode;
														$scope.country.contactDigit = response.data.listObject[0].contactDigit;

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

								type = (type == "A") ? "A" : "I";

								var data = {
									countryId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId : $scope.organizationId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATECOUNTRYSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Country Updated Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.initCountryMasterList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
													} else {
														growl.warning( response.data.message,
																{
																	title : 'Warning!'
																});
														$scope.initCountryMasterList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
													}
												});
							}

							// Function For update country
							$scope.updateCountry = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
									countryCode : $scope.country.countryCode,
									countryName : $scope.country.countryName,
									countryInitial : $scope.country.countryInitial,
									countryCallingCode : $scope.country.countryCallingCode,
									countryId : $scope.country.countryId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									contactDigit : $scope.country.contactDigit,
									organizationId : $scope.organizationId
								}
								if ($scope.countryform.$valid) {
									
									var regEx = /^[+-]?\d+$/; // regex for validating calling country code
									 if($scope.country.countryCallingCode!=null && $scope.country.countryCallingCode!=undefined && $scope.country.countryCallingCode!=''){
										  if(!regEx.test($scope.country.countryCallingCode)){
											  growl.warning("Please enter valid country calling code",{
													title : 'warning'
													});
													return false;
										  } 
				        			 }
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ UPDATECOUNTRY;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Country updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope
																	.$broadcast('show-errors-reset');
															$scope.init();
														} else {
															growl
																	.error(
																			'Country already exits...!!',
																			{
																				title : 'Error!'
																			});
														}

														$scope
																.initCountryMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);

													});
								}

							}
						} ]);
