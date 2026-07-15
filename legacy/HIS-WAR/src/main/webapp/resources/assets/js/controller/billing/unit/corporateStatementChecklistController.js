
/**
 * @Author Sneha Dalave
 * @name myApp.controller:corporateStatementChecklistController
 * @description #corporateStatementChecklistController Controller of the myApp
 */
(function () {
	"use strict";
						function corporateStatementChecklistController($scope, $http, $localStorage, $sessionStorage,
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
								$scope.corporateMasterObj = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.isPeriodicBilling = 'N';
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.visitTypeList = [
									{
										id:3,
										label:"Day Care"
									},
									{
										id:4,
										label:"ER"
									},
									{
										id:2,
										label:"IP"
									},
									{
										id:1,
										label:"OP"
									}
								];
								$scope.selectedVisitType=[];
								

								var data = {};
								
								var commonobj = {
										organizationId : $scope.organizationId
								}						

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEHIERARCHYLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.hierarchyList = [];

													if (response.data.status == "success") {
														$scope.hierarchyList = response.data.listObject;
														console.log("hierarchyList",$scope.hierarchyList);

													}
												});

							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initCorporateStatmentCheckListList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initCorporateStatmentCheckListList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 15;
								var URI = BASE_URL + ROOT_URL
										+ "/adt/getCorporateStatementChecklistMasterList";
								var URI1 = BASE_URL + ROOT_URL
										+ "/adt/getCorporateStatementChecklistMasterCount";

								var data = {
									organizationId : orgId,
									unitId:unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								console.log("data", data);

								var data1 = {
									organizationId : $scope.organizationId,unitId:unitId
								}

								// For Get billingBedCatList Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.corporateStatementChecklistMasterList = [];

													if (response.data.status == "success") {
														$scope.corporateStatementChecklistMasterList = response.data.listObject;
														console.log( "corporateStatementChecklistMasterList", $scope.corporateStatementChecklistMasterList);
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

							$scope.getBillingBedList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 15;
								var URI = BASE_URL + ROOT_URL
										+ GETBILLINGBEDCATEGORYLIST;
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
													$scope.billingBedCatList = [];

													if (response.data.status == "success") {
														$scope.billingBedCatList = response.data.listObject;														
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

							$scope.initCorporateStatmentCheckListList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveCorporateStatCheck();
							}

							// Function For  saveCorporateStatCheck
							$scope.saveCorporateStatCheck = function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								 $scope.$broadcast('show-errors-check-validity');
								var data = {
									checkListCode : $scope.corporateMasterObj.checkListCode,
									checkListDesc : $scope.corporateMasterObj.checkListDesc,
									status : 'A',
//									createdBy : $scope.userId,
//									updatedBy : $scope.userId,
									unitId:$scope.unitId,
									organizationId : $scope.organizationId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									visitTypeMasterDtosList : $scope.selectedVisitType
								}

								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									console.log("save", data);
									console.log("xx",$scope.selectedVisitType);
									//return false;
									var URI = BASE_URL + ROOT_URL
											+ "/adt/saveCorporateStatementChecklistMaster";
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.init();
															growl.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
														} else {
															growl.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
														}

														$scope.initCorporateStatmentCheckListList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});
								}
							}
							//$scope.addedvisitList = [];
							$scope.visitDetails = function(item)
							{
								console.log(item);
								//return false;
								$scope.addedvisitList = [];
								$scope.addedvisitList = item.visitTypeMasterDtosList;
							}

							// Function For get single billingBed entry
							$scope.showUpdateBtn = function(item) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								$scope.corporateMasterObj.checkListCode = item.checkListCode;
								$scope.corporateMasterObj.checkListDesc = item.checkListDesc;
								$scope.corporateMasterObj.corporateStatementChecklistId = item.corporateStatementChecklistId,
								$scope.selectedVisitType = [];
								$scope.oldSelectVisitList =[];
								angular .forEach(item.visitTypeMasterDtosList, function( value, index) {
									var newAr = {};
									newAr.id = value.id;
									$scope.selectedVisitType.push(newAr);
									$scope.oldSelectVisitList.push(newAr);
								});
								//$scope.selectedVisitType
								//$scope.oldSelectVisitList=item.visitTypeMasterDtosList;
							};

							// Function For update billingBed status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
									corporateStatementChecklistId: id,
									status : type,
//									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ "/adt/updateCorporateStatementChecklistMasterStatus";
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

							// Function For update billingBed
							$scope.updateCorporStatementChecklist= function() {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								var data = {
									checkListCode : $scope.corporateMasterObj.checkListCode,
									checkListDesc : $scope.corporateMasterObj.checkListDesc,
									corporateStatementChecklistId : $scope.corporateMasterObj.corporateStatementChecklistId,
//									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId:$scope.organizationId,
									unitId:$scope.unitId,
								}
								
								console.log($scope.selectedVisitType);
								console.log($scope.oldSelectVisitList);
								
								
								$scope.newlyAddedVisitTypeList = [];
								$scope.removedVisitTypeList = [];
								
								
								angular .forEach($scope.selectedVisitType, function( value, index) {
									if($scope.oldSelectVisitList.indexOf(value) !== -1) {
										  //alert('already exists!');
										}
									else
										{
										var newAr = {
												id:value.id
										}
										$scope.newlyAddedVisitTypeList.push(newAr);
										}
									});
								
								angular .forEach($scope.oldSelectVisitList, function( value, index) {
									if($scope.selectedVisitType.indexOf(value) !== -1) {
										  //alert('already exists!');
										}
									else
										{
										var newAr = {
												id:value.id
										}
										$scope.removedVisitTypeList.push(newAr);
										}
									});
								
								console.log("newlyAddedVisitTypeList",$scope.newlyAddedVisitTypeList);
								console.log("removedVisitTypeList",$scope.removedVisitTypeList);
								data.newlyAddedVisitTypeList = $scope.newlyAddedVisitTypeList;
								data.removedVisitTypeList = $scope.removedVisitTypeList;
								//return false;
									var URI = BASE_URL + ROOT_URL
											+ "/adt/updateCorporateStatementChecklistMaster";
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
																.initCorporateStatmentCheckListList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage,$scope.unitId);

													});

							}
						} 
						function config($stateProvider) {
							$stateProvider.state('corporateStatementChecklist', {
								url: '/corporateStatementChecklist',
								templateUrl: 'views/master/UnitMaster/billingMaster/corporate-statement-checklist.html',
								controller: "corporateStatementChecklistController",
								title: 'Corporate Statement Checklist',
							})
						}
					
					angular
						.module("myApp")
						.config(config)
						.controller("corporateStatementChecklistController", corporateStatementChecklistController);
					})();
