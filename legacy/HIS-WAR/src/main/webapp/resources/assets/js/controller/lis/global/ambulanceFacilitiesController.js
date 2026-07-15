/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"ambulanceFacilitiesController",
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
								$scope.ambulanceFacilitiesList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.ambulanceFacilitiesListCount;
								
								$rootScope.loginpage = true;
								$scope.ambulanceFacilities = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
		
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								var data = {};
								
					}
					$scope.init();
					
					/* code for setting Label */
					$(".selectedPageName").text("Ambulance Facilities Master");
					
					// For Paginations Start
					$scope.setNoOfRecords = function() {
						$scope.initAmbulanceFacilitiesMasterList(
								$scope.organizationId, $scope.offset,
								$scope.noOfRecordsPerPage);
					};
					$scope.initAmbulanceFacilitiesMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCEFACILITIESLIST;
						

						var data = {
							organizationId : orgId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						var data1 = {
							organizationId : $scope.organizationId
						}

						// For Get Ambulance Facilities List Using Offset
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceFacilitiesList = response.data.listObject;
												$scope.ambulanceFacilitiesListCount = response.data.object;
												$scope.setPage(1,false);
												console.log("ambulanceFacilitiesList",$scope.ambulanceFacilitiesList);
											}
										});

						
					}
					
					
					$scope.initAmbulanceFacilitiesMasterList1 = function(orgId,
							offset, noOfRecordsPerPage) {
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCEFACILITIESLIST;
						

						var data = {
							organizationId : orgId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						var data1 = {
							organizationId : $scope.organizationId
						}

						// For Get Ambulance Facilities List Using Offset
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceFacilitiesList = response.data.listObject;
												$scope.ambulanceFacilitiesListCount = response.data.object;
												console.log("ambulanceFacilitiesList",$scope.ambulanceFacilitiesList);
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
								$scope.ambulanceFacilitiesListCount, page,
								$scope.noOfRecordsPerPage);
						if (flag) {
							$scope.initAmbulanceFacilitiesMasterList1(
									$scope.organizationId,
									$scope.pager.startIndex,
									$scope.pager.pageSize);
						}
					}

					$scope.initAmbulanceFacilitiesMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage);
					
					
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						$scope.saveAmbulanceFacilities();
					}
					
					// Function For Save Ambulance Facilities
					$scope.saveAmbulanceFacilities = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
						
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambFacilities : $scope.ambulanceFacilities.ambFacilities,
							status : 'A',
							createdBy : $scope.userId,
							updatedBy : $scope.userId,
							organizationId : $scope.organizationId,
							createdDate : $scope.currentDate,
							updatedDate : $scope.currentDate
						}
					
						if ($scope.ambulanceFacilitiesForm.$valid) {

							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ ADDAMBULANCEFACILITIES;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Ambulance Facilities Added Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Ambulance Facilities already exits...!!',
																		{
																			title : 'Error!'
																		});
													}

													$scope.initAmbulanceFacilitiesMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage);

												});
							}
						}

					}
					
					$scope.getAmbulanceFacilitiesMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCEFACILITIESLIST;
						var data = {
							organizationId : $scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						console.log("URI", URI);
						// For Get Ambulance Facilities List Using Offset
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceFacilitiesList = response.data.listObject;

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
								$scope.ambulanceFacilitiesListCount, page,
								$scope.noOfRecordsPerPage);
						if (flag) {
							$scope.getAmbulanceFacilitiesMasterList(
									$scope.organizationId,
									$scope.pager.startIndex,
									$scope.pager.pageSize);
						}
					}

					
					// Function For get single Ambulance Facilities
					$scope.showUpdateBtn = function(id) {

						$scope.saveBtnFlag = false;
						$scope.updateBtnFlag = true;

						var data = {

							ambulanceFacilitiesMasterId : id,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ GETSINGLEAMBULANCEFACILITIES;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceFacilities.ambulanceFacilitiesMasterId = response.data.listObject[0].ambulanceFacilitiesMasterId;
												$scope.ambulanceFacilities.ambFacilities = response.data.listObject[0].ambFacilities;
											} else {
												alert("Error!!");
											}
										});

					};

					// Function For update Ambulance Facilities status
					$scope.updateStatus = function(id, type) {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');

						type = (type == "A") ? "A" : "I";

						var data = {
							ambulanceFacilitiesMasterId : id,
							status : type,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ UPDATEAMBULANCEFACILITIESSTATUS;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												growl
														.success(
																'Ambulance Facilities Updated Successfully...!!',
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

					// Function For update Ambulance Facilities
					$scope.updateAmbulanceFacilities = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambulanceFacilitiesMasterId : $scope.ambulanceFacilities.ambulanceFacilitiesMasterId, 
							ambFacilities : $scope.ambulanceFacilities.ambFacilities,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						if ($scope.ambulanceFacilitiesForm.$valid) {
							console.log(data);
							var URI = BASE_URL + ROOT_URL
									+ UPDATEAMBULANCEFACILITIES;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													growl.success('Ambulance Facilities Updated Successfully...!!',
																	{
																		title : 'Success!'
																	});
													$scope
															.$broadcast('show-errors-reset');
													$scope.init();
												} else {
													growl
															.error(
																	'Ambulance Facilities already exits...!!',
																	{
																		title : 'Error!'
																	});
												}

												$scope
														.initAmbulanceFacilitiesMasterList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage);

											});
						}

					}
					
				} ]);