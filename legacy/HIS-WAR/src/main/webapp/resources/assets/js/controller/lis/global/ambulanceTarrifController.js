/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"ambulanceTarrifController",
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
								$scope.ambulanceTarrifList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.ambulanceTarrifListCount;
		
								$rootScope.loginpage = true;
								$scope.ambulanceTarrif = {};
								$scope.ambulanceTarrif.ambulanceAddMileage= 1;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
		
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								var data = {};
								
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
					}
					$scope.init();
					
					/* code for setting Label */
					$(".selectedPageName").text("Ambulance Tariff Master");
					
					// For Paginations Start
					$scope.setNoOfRecords = function() {
						$scope.initAmbulanceTarrifMasterList(
								$scope.organizationId, $scope.offset,
								$scope.noOfRecordsPerPage);
					};
					$scope.initAmbulanceTarrifMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCETARRIFLIST;
						/*var URI1 = BASE_URL + ROOT_URL
								+ COUNTAMBULANCETARRIFMASTER;*/

						var data = {
							organizationId : orgId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						var data1 = {
							organizationId : $scope.organizationId
						}

						// For Get Ambulance Tarrif List Using Offset
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceTarrifList = response.data.listObject;
												
												$scope.ambulanceTarrifListCount = response.data.object;
												$scope.setPage(1,false);
												
												console.log("ambulanceTarrifList",$scope.ambulanceTarrifList);
											}
										});

						// For Count Ambulance List
						/*GenericService
								.serviceAction("POST", URI1, data1)
								.then(
										function(response) {
											if (response.data.status == "success") {
												console.log(response.data);
												$scope.ambulanceTarrifListCount = response.data.object;
												$scope.setPage(1,false);

											}
										});*/
					}
					
					$scope.initAmbulanceTarrifMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage);
					
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						$scope.saveAmbulanceTarrif();
					}
					
					// Function For Save Ambulance Tarrif
					$scope.saveAmbulanceTarrif = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
						
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambulanceType : $scope.ambulanceTarrif.ambulanceType,
							ambulanceProvider : $scope.ambulanceTarrif.ambulanceProvider,
							ambulanceOutSrcVender : $scope.ambulanceTarrif.ambulanceOutSrcVender,
							ambulanceFirstDist : $scope.ambulanceTarrif.ambulanceFirstDist,
							ambulanceFirstDistRate : $scope.ambulanceTarrif.ambulanceFirstDistRate,
							ambulanceAddMileage : $scope.ambulanceTarrif.ambulanceAddMileage,
							ambulanceAddMileageRate : $scope.ambulanceTarrif.ambulanceAddMileageRate,
							status : 'A',
							createdBy : $scope.userId,
							updatedBy : $scope.userId,
							organizationId : $scope.organizationId,
							createdDate : $scope.currentDate,
							updatedDate : $scope.currentDate
						}
					
						if ($scope.ambulanceTarrifForm.$valid) {

							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ ADDAMBULANCETARRIF;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Ambulance Tariff Added Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Ambulance Tariff Already Exist...!!',
																		{
																			title : 'Error!'
																		});
													}

													$scope.initAmbulanceTarrifMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage);

												});
							}
						}

					}
					
					$scope.getAmbulanceTarrifMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCETARRIFLIST;
						var data = {
							organizationId : $scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						console.log("URI", URI);
						// For Get Ambulance Tarrif List Using Offset
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceTarrifList = response.data.listObject;

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
								$scope.ambulanceTarrifListCount, page,
								$scope.noOfRecordsPerPage);
						if (flag) {
							$scope.getAmbulanceTarrifMasterList(
									$scope.organizationId,
									$scope.pager.startIndex,
									$scope.pager.pageSize);
						}
					}

					
					// Function For get single Ambulance Tarrif
					$scope.showUpdateBtn = function(id) {

						$scope.saveBtnFlag = false;
						$scope.updateBtnFlag = true;

						var data = {

							ambulanceTarrifMasterId : id,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ GETSINGLEAMBULANCETARRIF;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceTarrif.ambulanceTarrifMasterId = response.data.listObject[0].ambulanceTarrifMasterId;
												$scope.ambulanceTarrif.ambulanceType = response.data.listObject[0].ambulanceType;
												$scope.ambulanceTarrif.ambulanceProvider = response.data.listObject[0].ambulanceProvider;
												$scope.ambulanceTarrif.ambulanceOutSrcVender = response.data.listObject[0].ambulanceOutSrcVender;
												$scope.ambulanceTarrif.ambulanceFirstDist = response.data.listObject[0].ambulanceFirstDist;
												$scope.ambulanceTarrif.ambulanceFirstDistRate = response.data.listObject[0].ambulanceFirstDistRate;
												$scope.ambulanceTarrif.ambulanceAddMileage = response.data.listObject[0].ambulanceAddMileage;
												$scope.ambulanceTarrif.ambulanceAddMileageRate = response.data.listObject[0].ambulanceAddMileageRate;
											} else {
												alert("Error!!");
											}
										});

					};

					// Function For update Ambulance Tarrif status
					$scope.updateStatus = function(id, type) {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');

						type = (type == "A") ? "A" : "I";

						var data = {
							ambulanceTarrifMasterId : id,
							status : type,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ UPDATEAMBULANCETARRIFSTATUS;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												growl
														.success(
																'Ambulance Tariff Updated Successfully...!!',
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

					// Function For update Ambulance Tarrif
					$scope.updateAmbulanceTarrif = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambulanceTarrifMasterId : $scope.ambulanceTarrif.ambulanceTarrifMasterId, 
							ambulanceType : $scope.ambulanceTarrif.ambulanceType,
							ambulanceProvider : $scope.ambulanceTarrif.ambulanceProvider,
							ambulanceOutSrcVender : $scope.ambulanceTarrif.ambulanceOutSrcVender,
							ambulanceFirstDist : $scope.ambulanceTarrif.ambulanceFirstDist,
							ambulanceFirstDistRate : $scope.ambulanceTarrif.ambulanceFirstDistRate,
							ambulanceAddMileage : $scope.ambulanceTarrif.ambulanceAddMileage,
							ambulanceAddMileageRate : $scope.ambulanceTarrif.ambulanceAddMileageRate,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						if ($scope.ambulanceTarrifForm.$valid) {
							console.log(data);
							var URI = BASE_URL + ROOT_URL
									+ UPDATEAMBULANCETARRIF;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													growl.success('Ambulance Tariff Updated Successfully...!!',
																	{
																		title : 'Success!'
																	});
													$scope
															.$broadcast('show-errors-reset');
													$scope.init();
												} else {
													growl
															.error(
																	'Ambulance Tariff already exits...!!',
																	{
																		title : 'Error!'
																	});
												}

												$scope
														.initAmbulanceTarrifMasterList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage);

											});
						}

					}
					
				} ]);