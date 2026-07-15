/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"resourceAllocationController",
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
								$scope.resourceAllocationList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.resourceAllocationListCount;
								
								$rootScope.loginpage = true;
								$scope.resourceAllocation = {}
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
					
					
					
					
					/**List And Pagination Start**/
					$scope.setNoOfRecords = function() {
						$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
					};
					$scope.getDonorScreeningList = [];
					$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
					{
						$rootScope.startLoader();
						
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
						var data1 = {
							      	organizationId:organizationId,
									offset:offset,
									noOfRecordsPerPage:noOfRecordsPerPage,
//									unitId:unitId
									};
						
						var data2 = {
								      organizationId:organizationId,
//								      unitId:unitId
									};
						
						var URI1 = BASE_URL + ROOT_URL + GETRESOURCEALLOCATIONLIST; 
						var URI2 = BASE_URL + ROOT_URL + "/adt/getResourceAllocationListCount";
							
						
						GenericService.serviceAction("POST",URI1,data1).then(function(response){
							if(response.data.status == "success"){
							$rootScope.stopLoader();					
							$scope.resourceAllocationList = response.data.listObject;
							console.log("commonList",$scope.commonList);
							}
						});
						
						GenericService.serviceAction("POST",URI2,data2).then(function(response){
							if(response.data.status == "success"){					
								$rootScope.stopLoader();
								$scope.commonListCount = response.data.object;
								$scope.setPage(1,false);
								console.log("commonListCount",response.data.object);
							}
						});
						
					}		
					
					$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
						$rootScope.startLoader();
						console.log("offset", offset);
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						
						var data = {
								organizationId:organizationId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
//								unitId:unitId
								};
						
						var URI = BASE_URL + ROOT_URL + GETRESOURCEALLOCATIONLIST;
						console.log("URI", URI);
						GenericService.serviceAction("POST", URI,data).then(function(response) {
							$rootScope.stopLoader();
							$scope.resourceAllocationList = response.data.listObject;
							console.log("commonList",$scope.commonList);
						});
					}

					$scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages)
						{
							return;
						}
						$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
						if (flag)
						{
							$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
						}
					}

					$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
					
					/**List And Pagination End**/
					
					
					/* code for setting Label */
					$(".selectedPageName").text("Resource Allocation Master");
					
					// For Paginations Start
			/*		$scope.setNoOfRecords = function() {
						$scope.initResourceAllocationMasterList(
								$scope.organizationId, $scope.offset,
								$scope.noOfRecordsPerPage);
					};
					$scope.initResourceAllocationMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETRESOURCEALLOCATIONLIST;
						

						var data = {
							organizationId : orgId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						var data1 = {
							organizationId : $scope.organizationId
						}

						// For Get Resource Allocation List Using Offset
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.resourceAllocationList = response.data.listObject;
												console.log("resourceAllocationList",$scope.resourceAllocationList);
											}
										});

						
					}
					
					$scope.initResourceAllocationMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage);*/
					
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						$scope.saveResourceAllocation();
					}
					
					// Function For Save Resource Allocation
					$scope.saveResourceAllocation = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
						
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							ambulanceType : $scope.resourceAllocation.ambulanceType,
							ambulanceProvider : $scope.resourceAllocation.ambulanceProvider,
							vehicalNo : $scope.resourceAllocation.vehicalNo,
							resourceName : $scope.resourceAllocation.resourceName,
							mobileNo : $scope.resourceAllocation.mobileNo,
							status : 'A',
							createdBy : $scope.userId,
							updatedBy : $scope.userId,
							organizationId : $scope.organizationId,
							createdDate : $scope.currentDate,
							updatedDate : $scope.currentDate
						}
					
						if ($scope.resourceAllocationForm.$valid) {

							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ ADDRESOURCEALLOCATION;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Resource Allocation Added Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$scope.init();
													} else {
														growl
																.error(
																		'Resource Allocation already exits...!!',
																		{
																			title : 'Error!'
																		});
													}

													$scope.initMasterList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage);

												});
							}
						}

					}
					
					/*$scope.getResourceAllocationMasterList = function(orgId,
							offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
								: 10;
						var URI = BASE_URL + ROOT_URL + GETRESOURCEALLOCATIONLIST;
						var data = {
							organizationId : $scope.organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage
						}

						console.log("URI", URI);
						// For Get Resource Allocation List Using Offset
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.resourceAllocationList = response.data.listObject;

											}
										});
					}*/
					
	/*				
					$scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages) {
							return;
						}
						$scope.pager = PagerService.GetPager(
								$scope.resourceAllocationListCount, page,
								$scope.noOfRecordsPerPage);
						if (flag) {
							$scope.getResourceAllocationMasterList(
									$scope.organizationId,
									$scope.pager.startIndex,
									$scope.pager.pageSize);
						}
					}
*/
					
					// Function For get single Resource Allocation
					$scope.showUpdateBtn = function(id) {

						$scope.saveBtnFlag = false;
						$scope.updateBtnFlag = true;

						var data = {

							resourceAllocationMasterId : id,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ GETSINGLERESOURCEALLOCATION;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.resourceAllocation.resourceAllocationMasterId = response.data.listObject[0].resourceAllocationMasterId;
												$scope.resourceAllocation.ambulanceType = response.data.listObject[0].ambulanceType;
												$scope.resourceAllocation.ambulanceProvider = response.data.listObject[0].ambulanceProvider;
												$scope.resourceAllocation.vehicalNo = response.data.listObject[0].vehicalNo;
												$scope.resourceAllocation.resourceName = response.data.listObject[0].resourceName;
												$scope.resourceAllocation.mobileNo = response.data.listObject[0].mobileNo;
											} else {
												alert("Error!!");
											}
										});

					};

					// Function For update Resource Allocation status
					$scope.updateStatus = function(id, type) {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');

						type = (type == "A") ? "A" : "I";

						var data = {
							resourceAllocationMasterId : id,
							status : type,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						console.log(data);
						var URI = BASE_URL + ROOT_URL
								+ UPDATERESOURCEALLOCATIONSTATUS;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												growl
														.success(
																'Resource Allocation Updated Successfully...!!',
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

					// Function For update Resource Allocation
					$scope.updateResourceAllocation = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data = {
							resourceAllocationMasterId : $scope.resourceAllocation.resourceAllocationMasterId, 
							ambulanceType : $scope.resourceAllocation.ambulanceType,
							ambulanceProvider : $scope.resourceAllocation.ambulanceProvider,
							vehicalNo : $scope.resourceAllocation.vehicalNo,
							resourceName : $scope.resourceAllocation.resourceName,
							mobileNo : $scope.resourceAllocation.mobileNo,
							updatedBy : $scope.userId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						if ($scope.resourceAllocationForm.$valid) {
							console.log(data);
							var URI = BASE_URL + ROOT_URL
									+ UPDATERESOURCEALLOCATION;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													growl.success('Resource Allocation Updated Successfully...!!',
																	{
																		title : 'Success!'
																	});
													$scope
															.$broadcast('show-errors-reset');
													$scope.init();
												} else {
													growl
															.error(
																	'Resource Allocation already exits...!!',
																	{
																		title : 'Error!'
																	});
												}

												$scope.initMasterList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage);

											});
						}

					}
					
				} ]);