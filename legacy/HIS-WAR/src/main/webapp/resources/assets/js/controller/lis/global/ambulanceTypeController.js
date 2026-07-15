/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"ambulanceTypeController",
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
								$scope.ambulanceTypeList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.ambulanceTypeListCount;
		
								$rootScope.loginpage = true;
								$scope.ambulance = {}
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
					$(".selectedPageName").text("Ambulance Type Master");
					
					// For Paginations Start
					$scope.setNoOfRecords = function() {
						$scope.initAmbulanceMasterList(
								$scope.organizationId, $scope.offset,
								$scope.noOfRecordsPerPage);
					};
					$scope.initAmbulanceMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCETYPELIST;
//						var URI1 = BASE_URL + ROOT_URL
//								+ COUNTAMBULANCETYPEMASTER;

						var data = {
							organizationId : orgId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						var data1 = {
							organizationId : $scope.organizationId
						}

						// For Get Ambulance List Using Offset
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceTypeList = response.data.listObject;
												$scope.ambulanceTypeListCount = response.data.object;
												$scope.setPage(1,false);
												console.log("ambulanceTypeList",$scope.ambulanceTypeList);
											}
										});

						// For Count Ambulance List
//						GenericService
//								.serviceAction("POST", URI1, data1)
//								.then(
//										function(response) {
//											if (response.data.status == "success") {
//												console.log(response.data);
//												$scope.ambulanceTypeListCount = response.data.object;
//												$scope.setPage(1,false);
//
//											}
//										});
					}
					
					$scope.initAmbulanceMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage);
					
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						$scope.saveAmbulanceType();
					}
					
					// Function For Save Ambulance Type
					$scope.saveAmbulanceType = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
						
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambulanceType : $scope.ambulance.ambulanceType,
							ambulanceTypeId : $scope.ambulance.ambulanceTypeId,
							status : 'A',
							createdBy : $scope.userId,
							updatedBy : $scope.userId,
							organizationId : $scope.organizationId,
							createdDate : $scope.currentDate,
							updatedDate : $scope.currentDate
						}
					
						if ($scope.ambulanceform.$valid) {

							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ ADDAMBULANCETYPE;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Ambulance Type Added Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Ambulance Type already exits...!!',
																		{
																			title : 'Error!'
																		});
													}

													$scope.initAmbulanceMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage);

												});
							}
						}

					}
					
					$scope.getAmbulanceMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCETYPELIST;
						var data = {
							organizationId : $scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						console.log("URI", URI);
						// For Get Ambulance Type List Using Offset
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceTypeList = response.data.listObject;

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
								$scope.ambulanceTypeListCount, page,
								$scope.noOfRecordsPerPage);
						if (flag) {
							$scope.getAmbulanceMasterList(
									$scope.organizationId,
									$scope.pager.startIndex,
									$scope.pager.pageSize);
						}
					}

					
					// Function For get single Ambulance Type
					$scope.showUpdateBtn = function(id) {

						$scope.saveBtnFlag = false;
						$scope.updateBtnFlag = true;

						var data = {
							ambulanceTypeMasterId : id,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ GETSINGLEAMBULANCETYPE;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.ambulance.ambulanceTypeId = response.data.listObject[0].ambulanceTypeId;
												$scope.ambulance.ambulanceType = response.data.listObject[0].ambulanceType;
												$scope.ambulance.ambulanceTypeMasterId = response.data.listObject[0].ambulanceTypeMasterId;
											} else {
												/*alert("Error!!");*/
											}
										});

					};

					// Function For update Ambulance status
					$scope.updateStatus = function(id, type) {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');

						type = (type == "A") ? "A" : "I";

						var data = {
							ambulanceTypeMasterId : id,
							status : type,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ UPDATEAMBULANCETYPESTATUS;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												growl
														.success(
																'Ambulance Type Updated Successfully...!!',
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

					// Function For update Ambulance Type
					$scope.updateAmbulanceType = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambulanceTypeMasterId : $scope.ambulance.ambulanceTypeMasterId, 
							ambulanceType : $scope.ambulance.ambulanceType,
							ambulanceTypeId : $scope.ambulance.ambulanceTypeId,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						if ($scope.ambulanceform.$valid) {
							console.log(data);
							var URI = BASE_URL + ROOT_URL
									+ UPDATEAMBULANCETYPE;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													growl.success('Ambulance Type Updated Successfully...!!',
																	{
																		title : 'Success!'
																	});
													$scope
															.$broadcast('show-errors-reset');
													$scope.init();
												} else {
													growl
															.error(
																	'Ambulance Type already exits...!!',
																	{
																		title : 'Error!'
																	});
												}

												$scope
														.initAmbulanceMasterList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage);

											});
						}

					}
					
				} ]);