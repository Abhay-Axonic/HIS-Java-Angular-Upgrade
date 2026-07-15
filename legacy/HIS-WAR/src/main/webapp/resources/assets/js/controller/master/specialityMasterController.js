'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:specialityMasterController
 * @description #specialityMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'specialityMasterController',
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

							// init function description
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
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;

								$scope.speciality = {
									isSurgicalCode : 'N',
									isClinicalSpeciality:'N',
									isRenderingSpeciality:'N',
									deptWiseAppointment:'N',
									isNursingAssessmentRequired:'N',
								}
								$scope.speciality.specialityId = 0;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};
								
								
								var URI1 = BASE_URL + ROOT_URL
								+ COUNTSPECIALITYMASTER;
								
								var data1 = {
										organizationId : $scope.organizationId
									}
								
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
								
								

							};
							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() 
							{
								$scope.initSpecialityMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initSpecialityMasterList = function(orgId,
									offset, noOfRecordsPerPage) 
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
//                                + GETSPECIALITYLIST;
										+ "/adt/getSpecialityListOfActiveInactive";
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTSPECIALITYMASTER;

								var data = {
									organizationId : orgId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId
								}

								// For Get Speciality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.specialityList = [];

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.specialityList = response.data.listObject;
														console.log("specialityList",$scope.specialityList);
													}
												});
								//to get general ledger list
								var URI = BASE_URL + ROOT_URL
								+ GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
								var data = {
										
									}
								GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.ledgerList = [];

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.ledgerList = response.data.listObject;
												console.log("ledgerList",$scope.ledgerList);
											}
										});

								// For Count Speciality List
								
							}

							$scope.getSpecialityMasterList = function(orgId,
									offset, noOfRecordsPerPage) 
									{
								$rootScope.startLoader();
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + "/adt/getSpecialityListOfActiveInactive";
								var data = {
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Speciality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.specialityList = [];

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.specialityList = response.data.listObject;
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
									$scope.getSpecialityMasterList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initSpecialityMasterList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveSpeciality();
							}

							// Save function for add speciality
							$scope.saveSpeciality = function() {
								
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								
								var data = {
									specialityCode : $scope.speciality.specialityCode,
									specialityName : $scope.speciality.specialityName,
									generalLedgerId : $scope.speciality.generalLedgerId,
									specialitySAC : $scope.speciality.specialitySAC,
									isSurgicalCode : $scope.speciality.isSurgicalCode,
									isClinicalSpeciality : $scope.speciality.isClinicalSpeciality,
									status : 'A',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									deptWiseAppointment : $scope.speciality.deptWiseAppointment,
									isRenderingSpeciality : $scope.speciality.isRenderingSpeciality,
									isKioskDept : $scope.speciality.isKioskDept,
//									storeId : $scope.speciality.storeId,
									isNursingAssessmentRequired :  $scope.speciality.isNursingAssessmentRequired,
									allowedOverbookingSlots : $scope.speciality.allowedOverbookingSlots
								}
//								console.log("specilityDetails",data); return false;
								if ($scope.Specialityform.$valid) {
									$rootScope.startLoader();
									console.log(data);
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL
												+ ADDSPECIALITY;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																$rootScope.stopLoader();
																growl.success(
																				'Department added successfully...!!',
																				{
																					title : 'Success!'
																				});
																// $scope.speciality
																// = {};
																$scope.$broadcast('show-errors-reset');
																$scope.init();
																
															} else {
																$rootScope.stopLoader();
																growl.error(
																				'Department already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
															$scope.initSpecialityMasterList(
																			$scope.organizationId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
														});
									}
								}

							};

							// function for get speciality object for update
							$scope.showUpdateBtn = function(id) {
								$rootScope.startLoader();
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								var data = {
									specialityId : id
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLESPECIALITY;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														console.log("list obj",response.data.listObject[0]);
														$scope.speciality.specialityId = response.data.listObject[0].specialityId;
														$scope.speciality.specialityName = response.data.listObject[0].specialityName;
														$scope.speciality.specialityCode = response.data.listObject[0].specialityCode;
														$scope.speciality.generalLedgerId = response.data.listObject[0].generalLedgerId.toString();
														$scope.speciality.generalLedgerName = response.data.listObject[0].generalLedgerName.toString();
														$scope.speciality.specialitySAC = response.data.listObject[0].specialitySAC;
														$scope.speciality.isSurgicalCode = response.data.listObject[0].isSurgicalCode;
														$scope.speciality.isClinicalSpeciality = response.data.listObject[0].isClinicalSpeciality;
														$scope.speciality.isRenderingSpeciality = response.data.listObject[0].isRenderingSpeciality;
														$scope.speciality.deptWiseAppointment = response.data.listObject[0].deptWiseAppointment;
														$scope.speciality.isKioskDept = response.data.listObject[0].isKioskDept;
														$scope.speciality.isNursingAssessmentRequired =  response.data.listObject[0].isNursingAssessmentRequired;
														$scope.speciality.allowedOverbookingSlots = response.data.listObject[0].allowedOverbookingSlots;
//														$scope.speciality.storeId =  response.data.listObject[0].storeId.toString();
														console.log("list obj",$scope.speciality);
													} else {
														$rootScope.stopLoader();
														alert("Error!!");
													}
												});

							};

							// function for update status speciality
							$scope.updateStatus = function(id, type,idx) {
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";
								var data = {
									specialityId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									organizationId : $scope.organizationId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPDATESPECIALITYSTATUS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Department Updated successfully...!!',
																		{
																			title : 'Success!'
																		});
														$rootScope.stopLoader();
													} else {
														
														if(response.data.code == '409'){
															
															var typeTmp = (type == "A") ? "I" : "A";
															growl.warning("Department is mapped with unit, you can't change status.",
																	{
																		title : 'Warning!'
																	});
															$scope.specialityList[idx].status = typeTmp;
														}else{
															growl
															.error(
																	'Something went wrong...!!',
																	{
																		title : 'Error!'
																	});
														}
														$rootScope.stopLoader();
													}
												});
							}

							// function for update speciality
							$scope.updateSpeciality = function() {
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.$broadcast('show-errors-check-validity');
								console.log($scope.speciality);
								var data = {
									specialityId : $scope.speciality.specialityId,
									specialityCode : $scope.speciality.specialityCode,
									specialityName : $scope.speciality.specialityName,
									specialitySAC : $scope.speciality.specialitySAC,
									generalLedgerId : $scope.speciality.generalLedgerId,
//									storeId : $scope.speciality.storeId,
									isSurgicalCode : $scope.speciality.isSurgicalCode,
									isRenderingSpeciality : $scope.speciality.isRenderingSpeciality,
									isClinicalSpeciality : $scope.speciality.isClinicalSpeciality,
									deptWiseAppointment : $scope.speciality.deptWiseAppointment,
									isKioskDept : $scope.speciality.isKioskDept,
									isNursingAssessmentRequired :  $scope.speciality.isNursingAssessmentRequired,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate,
									allowedOverbookingSlots : $scope.speciality.allowedOverbookingSlots
								}
								if ($scope.Specialityform.$valid) {
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ UPDATESPECIALITY;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Department updated successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope.saveBtnFlag = false;
															$scope.updateBtnFlag = true;
															$scope.init();
															$rootScope.stopLoader();

														} else {
															growl
																	.error(
																			'Department already exits...!!',
																			{
																				title : 'Error!'
																			});
														}
														$scope
																.initSpecialityMasterList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
													});
									$rootScope.stopLoader();
								}else{
									growl.error(
											'Fill all mandatory fields.',
											{
												title : 'Error!'
											});
									$rootScope.stopLoader();
								}
							}
							$scope.ledgerNameSearch = function(keyword)
							{
								$scope.speciality.generalLedgerId=null;
                                var URI = BASE_URL + ROOT_URL
                                + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
                                var data = {
                                        "generalLedgerName":keyword
                                    }
                                return GenericService
                                .serviceAction("POST", URI, data)
                                .then(
                                        function(response) {
                                            if (response.data.status == "success") {
                                                $rootScope.stopLoader();
                                                return response.data.listObject;
                                            }
                                        });
							}
							$scope.SelectLedgerName =function(ledger)
						    {
								$scope.speciality.generalLedgerId=ledger.generalLedgerId;
						    }
							$scope.clearText = function()
						    {
						    	if($scope.speciality.generalLedgerId==undefined || $scope.speciality.generalLedgerId==null || $scope.speciality.generalLedgerId==0)
						    		{
						    		$scope.speciality.generalLedgerName="";
						    		}
						    }
							/*$scope.getStoreList = [];*/

						/*	 $scope.storeList = function() {
								 var data={};
								 var URI = BASE_URL + ROOT_URL + "/api/global/user/stores";
							      GenericService.serviceAction("GET", URI, data)
									.then(
											function(response) {

												if (response.status == 200) {
													$rootScope.stopLoader();
													$scope.getStoreList = response.data;
													console.log("getStoreList",$scope.getStoreList);
												}
											});

							    
							    }
							 $scope.storeList();
*/
							$scope.serachDepartmentMaster = function(searchString){
								$scope.specialityList=[];
								$scope.loadingMsg="Loading...";
								if(searchString!=''){
									var data = {
										organizationId : $scope.organizationId,
										specialityName : searchString
									};
								var URI = BASE_URL + ROOT_URL + "/adt/departmentMasterUniversalSearch"
										
								console.log("URI", URI);
								GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
													$rootScope.stopLoader();
													$scope.specialityList = response.data.listObject;
													if($scope.specialityList.length==0)
														$scope.loadingMsg="No record found...";
									});
								}else{
									$scope.initSpecialityMasterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage);
								}
								$rootScope.stopLoader();
							}
						} ]);
