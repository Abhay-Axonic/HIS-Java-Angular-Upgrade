'use strict';

/**
 * @Author
 * @name myApp.controller:roomMasterController
 * @description #roomMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nursingStationMasterController',
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
								$scope.nursingMasterMasterObj = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup		
								
								 // For Get store List 
								  // var URI =  BASE_URL + ROOT_URL+"/api/global/user/stores";
								var URI =  BASE_URL + ROOT_URL+"/api/unit/main/stores/"+ $scope.unitId + '/'+$scope.organizationId;
									  	var data = {	  			
									  	}
								GenericService.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.storeList = [];													
													$scope.storeList = response.data;
													console.log("store list",$scope.storeList);
																				
												});

							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initNursingList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initNursingList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETNURSINGSTATIONMASTERLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ GETNURSINGSTATIONMASTERCOUNT;

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
													$scope.nursingStationList = [];

													if (response.data.status == "success") {
														$scope.nursingStationList = response.data.listObject;
														console
																.log(
																		"nursingStationList",
																		$scope.nursingStationList);
													}
												});

								// For Count billingBedCatList 
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

							$scope.getNursingList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETNURSINGSTATIONMASTERLIST;
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
													$scope.nursingStationList = [];

													if (response.data.status == "success") {
														$scope.nursingStationList = response.data.listObject;														
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
									$scope.getNursingList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initNursingList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveNursingStation();
							}

							// Function For  saveNursingStation
							$scope.saveNursingStation = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								 $scope.$broadcast('show-errors-check-validity');
								var data = {
									nursingStationCode : $scope.nursingMasterMasterObj.nursingStationCode,
									nursingStationDesc : $scope.nursingMasterMasterObj.nursingStationDesc,
									storeId : $scope.nursingMasterMasterObj.storeId,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									unitId:$scope.unitId,
									organizationId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								}
								 if ($scope.nursingStationForm.$valid) {

								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									console.log("save", data);

									var URI = BASE_URL + ROOT_URL
											+ SAVENURSINGSTATIONMASTER;
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
														} else {
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
														}

														$scope
																.initNursingList(
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
										+ GETNURSINGSTATIONMASTERBYID + "/"
										+ id + "/" + $scope.organizationId+"/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
													   $scope.nursingMasterMasterObj.nursingStationCode = response.data.listObject[0].nursingStationCode;
														$scope.nursingMasterMasterObj.nursingStationDesc = response.data.listObject[0].nursingStationDesc;
														$scope.nursingMasterMasterObj.nursingStationId = response.data.listObject[0].nursingStationId;
														$scope.nursingMasterMasterObj.storeId = response.data.listObject[0].storeId.toString();
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

								type = (type == "A") ? "A" : "I";

								var data = {
									nursingStationId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ UPDATENURSINGSTATIONMASTERSTATUS;
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

							// Function For updateNursingStation
							$scope.updateNursingStation = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								var data = {
										nursingStationCode : $scope.nursingMasterMasterObj.nursingStationCode,
										nursingStationDesc : $scope.nursingMasterMasterObj.nursingStationDesc,
										nursingStationId : $scope.nursingMasterMasterObj.nursingStationId,	
										storeId : $scope.nursingMasterMasterObj.storeId,	
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									updatedDate : $scope.currentDate
								}							
								if ($scope.nursingStationForm.$valid) {
									var URI = BASE_URL + ROOT_URL
											+ UPDATENURSINGSTATIONMASTER;
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
														} else {
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
														}
														$scope
																.initNursingList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}

							}
						} ]);
