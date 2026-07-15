/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"ambulanceMasterController",
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
								$scope.ambulanceMasterDataList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.ambulanceMasterListCount;
		
								$rootScope.loginpage = true;
								$scope.ambulanceMaster = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
		
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								//var data = {};
								
								var URI = BASE_URL + ROOT_URL + GETAMBULANCETYPEACTIVELIST;
								var data = {
									organizationId : $scope.organizationId
								}
								console.log("URI", URI);
								// For Get Ambulance Type List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.ambulanceMasterList = [];
													if (response.data.status == "success") {
														$scope.ambulanceMasterList = response.data.listObject;
													}
												});
								
								
								var URI = BASE_URL + ROOT_URL + GETSERVICEPROVIDERACTIVELIST;
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
														$scope.serviceProviderList = response.data.listObject;
													}
												});
								
								var URI = BASE_URL + ROOT_URL + GETAMBULANCEFACILITIESACTIVELIST;
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
														$scope.ambulanceFacilitiesList = response.data.listObject;
													}
												});
					}
					$scope.init();
					
					/* code for setting Label */
					$(".selectedPageName").text("Ambulance Master");
					
					$scope.getNameByAmbType = function(id) {
						var data = {
								resourceAllocationMasterId : id,
								organizationId : $scope.organizationId
							}
						var URI = BASE_URL + ROOT_URL + GETSINGLERESOURCEALLOCATION;
						console.log("URI", URI);
						GenericService.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceMaster.vehicalNo = response.data.listObject[0].vehicalNo;
												if(response.data.listObject[0].serviceProvider == "In House"){
													$scope.inhouseVehicle = true;
												}else{
													$scope.inhouseVehicle = false;
												}
											}
										});

					}
					
					
					$scope.getAmbulanceMasterDataList = function(orgId,
							offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETAMBULANCEMASTERDATALIST;
						var data = {
							organizationId : $scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						console.log("URI", URI);
						// For Get Ambulance Type List Using Offset
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.ambulanceMasterDataList = response.data.listObject;

											}
										});
					}
					
					
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
						var URI = BASE_URL + ROOT_URL + GETAMBULANCEMASTERDATALIST;
						var URI1 = BASE_URL + ROOT_URL
								+ COUNTAMBULANCETYPEMASTER;

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
												$scope.ambulanceMasterDataList = response.data.listObject;
											}
										});

						// For Count Ambulance List
						GenericService
								.serviceAction("POST", URI1, data1)
								.then(
										function(response) {
											if (response.data.status == "success") {
												console.log(response.data);
												$scope.ambulanceMasterListCount = response.data.object;
												$scope.setPage(1,false);

											}
										});
					}
					
					$scope.initAmbulanceMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage);
					
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						$scope.saveAmbulanceMaster();
					}
					
					// Function For Save Ambulance Master Data
					$scope.saveAmbulanceMaster = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
						
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							serviceProvider : $scope.ambulanceMaster.serviceProvider,
							ambulanceType : $scope.ambulanceMaster.ambulanceType,
							ambulanceVehicalNo: $scope.ambulanceMaster.vehicalNo,
							availableFacilities: $scope.ambulanceMaster.availableFacilities,
							inhouse: ($scope.ambulanceMaster.inhouseVehicle != undefined ? $scope.ambulanceMaster.inhouseVehicle: 'N'),
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
								

								var URI = BASE_URL + ROOT_URL
										+ SAVEAMBULANCEMASTERDATA; // ADDAMBULANCETYPE
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {
													console.log("saved record ",JSON.stringify(response.data));
													if (response.data.status == "success") {
														growl
																.success(
																		'Ambulance Data Added Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Ambulance Vehicle No. is already exist.!!',
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
					
					$scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages) {
							return;
						}
						$scope.pager = PagerService.GetPager(
								$scope.ambulanceMasterListCount, page,
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

							ambulanceMasterId : id,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ GETSINGLEAMBULANCEMASTERDATA;
						console.log("url ",URI);
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {		
												console.log("single reord for updation ",JSON.stringify(response.data));
												$scope.ambulanceMaster.ambulanceMasterId = response.data.listObject[0].ambulanceMasterId;
												$scope.ambulanceMaster.ambulanceType = response.data.listObject[0].ambulanceType;
												$scope.ambulanceMaster.serviceProvider = response.data.listObject[0].serviceProvider;
												$scope.ambulanceMaster.vehicalNo = response.data.listObject[0].ambulanceVehicalNo;
												$scope.ambulanceMaster.availableFacilities = response.data.listObject[0].availableFacilities;
												$scope.ambulanceMaster.inhouseVehicle = response.data.listObject[0].inhouse;
											
											} else {
												window.alert("Error!!");
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
							ambulanceMasterId : id,
							status : type,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ UPDATEAMBULANCEMASTERDATASTATUS;
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
					$scope.updateAmbulanceMaster = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambulanceMasterId : $scope.ambulanceMaster.ambulanceMasterId, 
							ambulanceType : $scope.ambulanceMaster.ambulanceType,
							serviceProvider : $scope.ambulanceMaster.serviceProvider,
							ambulanceVehicalNo: $scope.ambulanceMaster.vehicalNo,
							availableFacilities: $scope.ambulanceMaster.availableFacilities,
							inhouse: $scope.ambulanceMaster.inhouseVehicle,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						if ($scope.ambulanceform.$valid) {
							console.log(data);
							var URI = BASE_URL + ROOT_URL
									+ UPDATEAMBULANCEMASTERDATA;
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
																	'Ambulance Type already exist...!!',
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