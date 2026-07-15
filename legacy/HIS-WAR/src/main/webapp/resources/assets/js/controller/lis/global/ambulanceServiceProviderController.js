/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"ambulanceServiceProviderController",
		[ '$scope',
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
								$scope.serviceProviderList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.serviceProviderListCount;
								
								$rootScope.loginpage = true;
								$scope.serviceProvider = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false
									};

									$scope.openDatePicker1 = function() {
										$scope.datepicker1.opened = true;
									};

									$scope.datepicker1 = {
										opened : false
									};
									
									$scope.openDatePicker2 = function() {
										$scope.datepicker2.opened = true;
									};

									$scope.datepicker2 = {
										opened : false
									};
									
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
								
								var URI = BASE_URL + ROOT_URL + GETAMBULANCETARRIFACTIVELIST;
								var data = {
									organizationId : $scope.organizationId
								}
								console.log("URI", URI);
								// For Get Ambulance Type List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.ambulanceTypeList = [];
													if (response.data.status == "success") {
														$scope.ambulanceTypeList = response.data.listObject;
													}
												});
								var URI = BASE_URL + ROOT_URL + GETSTATELIST;
								GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.stateList = [];

											if (response.data.status == "success") {
												$scope.stateList = response.data.listObject;

											}
										});
								var URI = BASE_URL + ROOT_URL + GETDISTRICTLIST;
								// For Get District List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.districtList = [];

													if (response.data.status == "success") {
														$scope.districtList = response.data.listObject;

													}
												});
								var URI = BASE_URL + ROOT_URL + GETCITYLIST;
								GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.cityList = [];

											if (response.data.status == "success") {
												$scope.cityList = response.data.listObject;

											}
										});
								
					}
					$scope.init();
					
					$scope.getStateByCountry = function(id) {
						var data = {
							countryId : id
						};
						var URI = BASE_URL + ROOT_URL
								+ GETSTATELISTBYCOUNTRYID;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.stateList = [];

											if (response.data.status == "success") {
												$scope.stateList = response.data.listObject;
												console.log($scope.stateList);

											}
										});

					}

					$scope.getDistrictByState = function(id) {
						var data = {
							stateId : id
						};
						var URI = BASE_URL + ROOT_URL
								+ GETDISTRICTLISTBYSTATEID;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.districtList = [];

											if (response.data.status == "success") {
												$scope.districtList = response.data.listObject;
												console
														.log($scope.districtList);

											}
										});

					}

					$scope.getCityByDistrict = function(id) {

						var data = {
							districtId : id
						};
						var URI = BASE_URL + ROOT_URL
								+ GETCITYLISTBYDISTRICTID;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.cityList = [];

											if (response.data.status == "success") {
												$scope.cityList = response.data.listObject;
												console
														.log($scope.cityList);

											}
										});

					}

					$scope.getAreaByCity = function(id) {

						var data = {
							cityId : id
						};
						var URI = BASE_URL + ROOT_URL
								+ GETAREALISTBYCITY;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.areaList = [];

											if (response.data.status == "success") {
												$scope.areaList = response.data.listObject;
												console
														.log($scope.areaList);

											}
										});

					}
					
					$scope.getNameByAmbType = function(id) {

						var data = {
								ambulanceTarrifMasterId : id,
								organizationId : $scope.organizationId
							}
						var URI = BASE_URL + ROOT_URL + GETSINGLEAMBULANCETARRIF;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.serviceProvider.serviceProviderPersonName = response.data.listObject[0].ambulanceOutSrcVender;
												$scope.serviceProvider.serviceProviderFirstDistance = response.data.listObject[0].ambulanceFirstDist;
												$scope.serviceProvider.serviceProviderFirstDistanceRate = response.data.listObject[0].ambulanceFirstDistRate;
												$scope.serviceProvider.serviceProviderAdditionalMileage = response.data.listObject[0].ambulanceAddMileage;
												$scope.serviceProvider.serviceProviderAdditionalMileageRate = response.data.listObject[0].ambulanceAddMileageRate;
											}
										});

					}
					
					/* code for setting Label */
					$(".selectedPageName").text("Service Provider Master");
					
					// For Paginations Start
					$scope.setNoOfRecords = function() {
						$scope.initServiceProviderMasterList(
								$scope.organizationId, $scope.offset,
								$scope.noOfRecordsPerPage);
					};
					$scope.initServiceProviderMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + GETSERVICEPROVIDERLIST;
						

						var data = {
							organizationId : orgId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						var data1 = {
							organizationId : $scope.organizationId
						}

						// For Get Service Provider List Using Offset
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.serviceProviderList = response.data.listObject;
												console.log("serviceProviderList",$scope.serviceProviderList);
											}
										});

						
					}
					
					$scope.initServiceProviderMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage);
					
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						$scope.saveServiceProvider();
					}
					
					// Function For Save Service Provider
					$scope.saveServiceProvider = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
						
						$scope.$broadcast('show-errors-check-validity');
						var data = {
								serviceProviderName : $scope.serviceProvider.serviceProviderName,
								serviceProviderAddress : $scope.serviceProvider.serviceProviderAddress,
								serviceProviderCountry : $scope.serviceProvider.serviceProviderCountry,
								serviceProviderState : $scope.serviceProvider.serviceProviderState,
								serviceProviderDistrict : $scope.serviceProvider.serviceProviderDistrict,
								serviceProviderCity : $scope.serviceProvider.serviceProviderCity,
								serviceProviderArea : $scope.serviceProvider.serviceProviderArea,
								serviceProviderZipCode : $scope.serviceProvider.serviceProviderZipCode,
								serviceProviderAmbulanceType : $scope.serviceProvider.serviceProviderAmbulanceType,
								serviceProviderPersonName : $scope.serviceProvider.serviceProviderPersonName,
								serviceProviderPhoneNo : $scope.serviceProvider.serviceProviderPhoneNo,
								serviceProviderCellNo : $scope.serviceProvider.serviceProviderCellNo,
								serviceProviderEmailAddress : $scope.serviceProvider.serviceProviderEmailAddress,
								serviceProviderContractFromDate : moment($scope.serviceProvider.serviceProviderContractFromDate).format('DD-MM-YYYY HH:mm:ss'),
								serviceProviderContractToDate : moment($scope.serviceProvider.serviceProviderContractToDate).format('DD-MM-YYYY HH:mm:ss'),
								serviceProviderFirstDistance : $scope.serviceProvider.serviceProviderFirstDistance,
								serviceProviderFirstDistanceRate : $scope.serviceProvider.serviceProviderFirstDistanceRate,
								serviceProviderAdditionalMileage : $scope.serviceProvider.serviceProviderAdditionalMileage,
								serviceProviderAdditionalMileageRate : $scope.serviceProvider.serviceProviderAdditionalMileageRate,
								status : 'A',
								createdBy : $scope.userId,
								updatedBy : $scope.userId,
								organizationId : $scope.organizationId,
								createdDate : $scope.currentDate,
								updatedDate : $scope.currentDate
						}
					
						if ($scope.serviceProviderForm.$valid) {

							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ ADDSERVICEPROVIDER;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Service Provider Added Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Service Provider already exits...!!',
																		{
																			title : 'Error!'
																		});
													}

													$scope.initServiceProviderMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage);

												});
							}
						}

					}
					
					$scope.getServiceProviderMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETSERVICEPROVIDERLIST;
						var data = {
							organizationId : $scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						console.log("URI", URI);
						// For Get Service Provider List Using Offset
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.serviceProviderList = response.data.listObject;

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
								$scope.serviceProviderListCount, page,
								$scope.noOfRecordsPerPage);
						if (flag) {
							$scope.getServiceProviderMasterList(
									$scope.organizationId,
									$scope.pager.startIndex,
									$scope.pager.pageSize);
						}
					}

					
					// Function For get single Service Provider
					$scope.showUpdateBtn = function(id) {

						$scope.saveBtnFlag = false;
						$scope.updateBtnFlag = true;

						var data = {

							serviceProviderMasterId : id,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ GETSINGLESERVICEPROVIDER;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.serviceProvider.serviceProviderMasterId = response.data.listObject[0].serviceProviderMasterId;
												$scope.serviceProvider.serviceProviderName = response.data.listObject[0].serviceProviderName;
												$scope.serviceProvider.serviceProviderAddress = response.data.listObject[0].serviceProviderAddress;
												$scope.serviceProvider.serviceProviderCountry = response.data.listObject[0].serviceProviderCountry;
												$scope.serviceProvider.serviceProviderState = response.data.listObject[0].serviceProviderState;
												$scope.serviceProvider.serviceProviderDistrict = response.data.listObject[0].serviceProviderDistrict;
												$scope.serviceProvider.serviceProviderCity = response.data.listObject[0].serviceProviderCity;
												$scope.serviceProvider.serviceProviderArea = response.data.listObject[0].serviceProviderArea;
												$scope.serviceProvider.serviceProviderZipCode = response.data.listObject[0].serviceProviderZipCode;
												$scope.serviceProvider.serviceProviderAmbulanceType = response.data.listObject[0].serviceProviderAmbulanceType;
												$scope.serviceProvider.serviceProviderPersonName = response.data.listObject[0].serviceProviderPersonName;
												$scope.serviceProvider.serviceProviderPhoneNo = response.data.listObject[0].serviceProviderPhoneNo;
												$scope.serviceProvider.serviceProviderCellNo = response.data.listObject[0].serviceProviderCellNo;
												$scope.serviceProvider.serviceProviderEmailAddress = response.data.listObject[0].serviceProviderEmailAddress;
												$scope.serviceProvider.serviceProviderContractFromDate = response.data.listObject[0].serviceProviderContractFromDate;
												$scope.serviceProvider.serviceProviderContractToDate = response.data.listObject[0].serviceProviderContractToDate;
												$scope.serviceProvider.serviceProviderFirstDistance = response.data.listObject[0].serviceProviderFirstDistance;
												$scope.serviceProvider.serviceProviderFirstDistanceRate = response.data.listObject[0].serviceProviderFirstDistanceRate;
												$scope.serviceProvider.serviceProviderAdditionalMileage = response.data.listObject[0].serviceProviderAdditionalMileage;
												$scope.serviceProvider.serviceProviderAdditionalMileageRate = response.data.listObject[0].serviceProviderAdditionalMileageRate;
											} else {
												alert("Error!!");
											}
										});

					};

					// Function For update Service Provider status
					$scope.updateStatus = function(id, type) {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');

						type = (type == "A") ? "A" : "I";

						var data = {
							serviceProviderMasterId : id,
							status : type,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ UPDATESERVICEPROVIDERSTATUS;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												growl
														.success(
																'Service Provider Updated Successfully...!!',
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

					// Function For update Service Provider
					$scope.updateServiceProvider = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							serviceProviderMasterId : $scope.serviceProvider.serviceProviderMasterId, 
							serviceProviderName : $scope.serviceProvider.serviceProviderName,
							serviceProviderAddress : $scope.serviceProvider.serviceProviderAddress,
							serviceProviderCountry : $scope.serviceProvider.serviceProviderCountry,
							serviceProviderState : $scope.serviceProvider.serviceProviderState,
							serviceProviderDistrict : $scope.serviceProvider.serviceProviderDistrict,
							serviceProviderCity : $scope.serviceProvider.serviceProviderCity,
							serviceProviderArea : $scope.serviceProvider.serviceProviderArea,
							serviceProviderZipCode : $scope.serviceProvider.serviceProviderZipCode,
							serviceProviderAmbulanceType : $scope.serviceProvider.serviceProviderAmbulanceType,
							serviceProviderPersonName : $scope.serviceProvider.serviceProviderPersonName,
							serviceProviderPhoneNo : $scope.serviceProvider.serviceProviderPhoneNo,
							serviceProviderCellNo : $scope.serviceProvider.serviceProviderCellNo,
							serviceProviderEmailAddress : $scope.serviceProvider.serviceProviderEmailAddress,
							serviceProviderContractFromDate : moment($scope.serviceProvider.serviceProviderContractFromDate).format('DD-MM-YYYY HH:mm:ss'),
							serviceProviderContractToDate : moment($scope.serviceProvider.serviceProviderContractToDate).format('DD-MM-YYYY HH:mm:ss'),
							serviceProviderFirstDistance : $scope.serviceProvider.serviceProviderFirstDistance,
							serviceProviderFirstDistanceRate : $scope.serviceProvider.serviceProviderFirstDistanceRate,
							serviceProviderAdditionalMileage : $scope.serviceProvider.serviceProviderAdditionalMileage,
							serviceProviderAdditionalMileageRate : $scope.serviceProvider.serviceProviderAdditionalMileageRate,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						if ($scope.serviceProviderForm.$valid) {
							console.log(data);
							var URI = BASE_URL + ROOT_URL
									+ UPDATESERVICEPROVIDER;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													growl.success('Service Provider Updated Successfully...!!',
																	{
																		title : 'Success!'
																	});
													$scope
															.$broadcast('show-errors-reset');
													$scope.init();
												} else {
													growl
															.error(
																	'Service Provider already exits...!!',
																	{
																		title : 'Error!'
																	});
												}

												$scope
														.initServiceProviderMasterList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage);

											});
						}

					}
					
				} ]);