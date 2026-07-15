'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:cleanedBedAcceptController
 * @description #cleanedBedAcceptController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'cleanedBedAcceptController',
				[
						'$scope',
						'$http',
						'$filter',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'PagerService',
						'growl',
						'$state',
						'$stateParams',
						function($scope, $http, $filter, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, PagerService, growl,
								$state, $stateParams) {

                                var self = this;
							/* init() function for form object create. */
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

                                $scope.pagination= {
                                  currentPage: 1,
                                  totalPages: 0,
                                  pageSize: '10',
                                  search: '',
                                  setTotalPages: PagerService.setTotalPages,
                                  setPage: PagerService.setPage, // need to rename
                                }

								$scope.bedsearch = {
									floorId : 0,
									nursingStationId : 0,
									bedCategoryId : 0,
									wardId : 0,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,

								}

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
							    $scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								// end for popup
								
								$scope.bedHousekeeping = {}
								$scope.itemSelecteds = {};
								var data ={};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

								/*var URI = BASE_URL + ROOT_URL
										+ GETLISTOFVACANTBEDSFORNURSING;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.housekeepingBedList = [];

													if (response.data.status == "success") {
														$scope.housekeepingList = response.data.listObject;
														
														angular.forEach( $scope.housekeepingList, function( value, index) {
																	var newArr = {};
																	newArr.housekeepingId = value.housekeepingId;
																	newArr.bedId = value.bedId;
																	newArr.priorityId = value.priorityId;
																	newArr.assignedPersonId = value.assignedPersonId.toString();
																	newArr.floorName = value.floorName;
																	newArr.nursingStationDesc = value.nursingStationDesc;
																	newArr.bedCategoryDesc = value.bedCategoryDesc;
																	newArr.bedNumber = value.bedNumber;
																	newArr.priorityDesc = value.priorityDesc;
																	newArr.note = value.note;
																	newArr.housekeepingStatusDesc = value.housekeepingStatusDesc;
																	newArr.userName = value.userName;
																	$scope.housekeepingBedList.push(newArr);
																});
														
														
													}
												});*/
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEREASONLIST;
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.reasonList = [];

											if (response.data.status == "success") {
												$scope.reasonList = response.data.listObject;

											}
										});
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEFLOORLIST;
				GenericService
						.serviceAction("POST", URI, commonObj)
						.then(
								function(response) {
									$scope.activeFloorList = [];

									if (response.data.status == "success") {
										$scope.activeFloorList = response.data.listObject;

									}
								});

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEBEDCATEGORYLISTBYUNIT;
				GenericService
						.serviceAction("POST", URI, commonObj)
						.then(
								function(response) {
									$scope.bedCategoryList = [];

									if (response.data.status == "success") {
										$scope.bedCategoryList = response.data.listObject;

									}
								});
				
				
				// getting discharge patient for house keeping start
				var common = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId,
					};
				console.log(common);
				$scope.listOfAdmittedPatientListForHouseKeeping=[];
				var URI = BASE_URL + ROOT_URL+ GETDISCHARGEPATIENTLISTFORNURSING;
				GenericService.serviceAction("POST", URI,common).then(function(response) {
									if (response.data.status == "success") {
										$scope.admittedPatientList = response.data.listObject;
										angular.forEach($scope.admittedPatientList,function(value,index) {

															var newArr = {};
															newArr.specialityName = value.specialityName;
															newArr.dFirstName = value.dFirstName;
															newArr.pFirstName = value.pFirstName;
															newArr.reasonDesc = value.reasonDesc;
															newArr.dob = value.dob;
															newArr.datepicker = value.doa;
															newArr.datepickerPdd = value.pdd;
															newArr.genderCode = value.genderCode;
															newArr.uhidnumber = value.uhidnumber;
															newArr.patientId = value.patientId;
															newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
															newArr.bedNumber = value.bedNumber;
															newArr.admissionId = value.admissionId;
															newArr.prefixCode = value.prefixCode;
															newArr.identificationNo = value.identificationNo;
															newArr.dischargeId = value.dischargeId;
															newArr.futureDischargeDate = value.futureDischargeDate;
															newArr.dischargeStatusId = value.dischargeStatusId;
															newArr.dischargeTypeId = value.dischargeTypeId;
															newArr.dischargeTypeName = value.dischargeTypeName;
															newArr.dischargeDesc = value.dischargeDesc;
															newArr.doa = value.doa;
															newArr.pdd = value.pdd;
															newArr.age = value.ageString;
															newArr.wardName = value.wardName;
															newArr.bedId = value.bedId;
															newArr.floorName = value.floorName;
															newArr.dischargeSummaryId = value.dischargeSummaryId;
															newArr.genderId=value.genderId;
															if(value.visitTypeId==2){
																newArr.visitTypeId=value.visitTypeId;
															}
															$scope.listOfAdmittedPatientListForHouseKeeping
																	.push(newArr);

														});
										}
									console.log($scope.listOfAdmittedPatientListForHouseKeeping);
								});

				// getting discharge patient for house keeping end

			

							}

							$scope.init();


							// Methode to Search the product
                            $scope.searchProduct = function (housekeepingBedList, pagination) {
                                        var data = housekeepingBedList;
                                        var searchQuery = pagination.search.toLowerCase();

                                        if (searchQuery) {
                                            pagination.filteredData = $filter('filter')(data, function (item) {
                                                return (
                                					(item.floorName != undefined ? item.floorName.toLowerCase().includes(searchQuery) : "")||
                                                    (item.bedNumber != undefined ? item.bedNumber.toLowerCase().includes(searchQuery) : "") ||
                                					(item.userName != undefined ? item.userName.toLowerCase().includes(searchQuery) : "")
                                                );
                                            });
                                            $scope.housekeepingBedList = [];
                                            $scope.housekeepingBedList = pagination.filteredData;
                                        } else {
                                            $scope.housekeepingBedList = data;
                                            pagination.filteredData = data;
                                        }

                                        $scope.setTotalPages(pagination);
                                    }


                            $scope.setTotalPages = function (pagination) {
                                        pagination.currentPage = 1;
                                        pagination.totalPages = Math.ceil(pagination.filteredData.length / parseInt(pagination.pageSize, 10));
                                       return $scope.setPagedData(pagination);
                                }

                            $scope.setPagedData = function (pagination) {
                                      var limit = parseInt(pagination.pageSize, 10);
                                      var offset = (pagination.currentPage - 1) * limit;
                                      pagination.data = pagination.filteredData.slice(offset, offset + limit);
                                }

							
							//For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.gethousekeepingBedListData( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.gethousekeepingBedListData= function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
									};

									var URI = BASE_URL + ROOT_URL
											+ GETLISTOFVACANTBEDSFORNURSING;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.housekeepingBedList = [];

														if (response.data.status == "success") {
															$scope.housekeepingList = response.data.listObject;
															
															angular.forEach( $scope.housekeepingList, function( value, index) {
																		var newArr = {};
																		newArr.housekeepingId = value.housekeepingId;
																		newArr.bedId = value.bedId;
																		newArr.priorityId = value.priorityId;
																		newArr.assignedPersonId = value.assignedPersonId.toString();
																		newArr.floorName = value.floorName;
																		newArr.nursingStationDesc = value.nursingStationDesc;
																		newArr.bedCategoryDesc = value.bedCategoryDesc;
																		newArr.bedNumber = value.bedNumber;
																		newArr.priorityDesc = value.priorityDesc;
																		newArr.note = value.note;
																		newArr.housekeepingStatusDesc = value.housekeepingStatusDesc;
																		newArr.userName = value.userName;
																		$scope.housekeepingBedList.push(newArr);
																	});
															
															$scope.commonListCount = response.data.object;
									            			$scope.pager = {};
									            			$scope.page;
									            			$scope.setPage(1,false);

//									            			if(!!response && !!response.data.listObject && response.data.listObject.length > 0){
//                                                                 searchProduct($scope.housekeepingList, self.model.pagination);
//                                                            }
														}
													});
							}

							$scope.gethousekeepingBedListDataMaster = function(orgId, offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
									};

									var URI = BASE_URL + ROOT_URL
											+ GETLISTOFVACANTBEDSFORNURSING;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.housekeepingBedList = [];

														if (response.data.status == "success") {
															$scope.housekeepingList = response.data.listObject;
															
															angular.forEach( $scope.housekeepingList, function( value, index) {
																		var newArr = {};
																		newArr.housekeepingId = value.housekeepingId;
																		newArr.bedId = value.bedId;
																		newArr.priorityId = value.priorityId;
																		newArr.assignedPersonId = value.assignedPersonId.toString();
																		newArr.floorName = value.floorName;
																		newArr.nursingStationDesc = value.nursingStationDesc;
																		newArr.bedCategoryDesc = value.bedCategoryDesc;
																		newArr.bedNumber = value.bedNumber;
																		newArr.priorityDesc = value.priorityDesc;
																		newArr.note = value.note;
																		newArr.housekeepingStatusDesc = value.housekeepingStatusDesc;
																		newArr.userName = value.userName;
																		$scope.housekeepingBedList.push(newArr);
																	});

														}
													});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager( $scope.commonListCount, page, $scope.noOfRecordsPerPage);
								if (flag) {
									$scope.gethousekeepingBedListDataMaster($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.gethousekeepingBedListData($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End
							
							
							
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.updateHouseKeepingRequest();
							}
							
							$scope.selectItem = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.bedHousekeeping) {
									$scope.itemSelecteds = item;
								}
								console.log($scope.itemSelecteds);
							}
							
							
							$scope.getBedById = function(commonType) {

								console.log($scope.bedsearch);

								var commonData = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								switch (commonType) {
								case 'floor':

									commonData.floorId = $scope.bedsearch.floorId;

									var URI = BASE_URL + ROOT_URL
											+ GETWARDLISTBYFLOORIDORGUNITID;
									GenericService
											.serviceAction("POST", URI,
													commonData)
											.then(
													function(response) {
														$scope.wardList = [];

														if (response.data.status == "success") {
															$scope.wardList = response.data.listObject;

														}
													});
									break;
								case 'ward':

									commonData.wardId = $scope.bedsearch.wardId;

									var URI = BASE_URL
											+ ROOT_URL
											+ GETACTIVENURSINGSTATIONLISTBYWARDID;
									GenericService
											.serviceAction("POST", URI,
													commonData)
											.then(
													function(response) {
														$scope.nursingStationList = [];

														if (response.data.status == "success") {
															$scope.nursingStationList = response.data.listObject;

														}
													});
									break;
								default:

								}

							}

							$scope.bedSearchByMultipleCriteria = function() {

								// $scope.bedsearch.isVirtual =
								// ($scope.bedsearch.isVirtual == true) ? "Y":
								// "N";
								console.log($scope.bedsearch);
								$scope.bedsearch.offset= $scope.offset;
								$scope.bedsearch.noOfRecordsPerPage= $scope.noOfRecordsPerPage;
								
								// return false;
								var URI = BASE_URL + ROOT_URL
										+ SEARCHBEDBYMULTIPLECRITERIAFORNURSING;
								GenericService
										.serviceAction("POST", URI,
												$scope.bedsearch)
										.then(
												function(response) {
													$scope.housekeepingBedList = [];

													if (response.data.status == "success") {
	                                                    $scope.housekeepingList = response.data.listObject;
														
														angular
														.forEach( $scope.housekeepingList, function( value, index) {
																	var newArr = {};
																	newArr.housekeepingId = value.housekeepingId;
																	newArr.bedId = value.bedId;
																	newArr.priorityId = value.priorityId;
																	newArr.assignedPersonId = value.assignedPersonId.toString();
																	newArr.floorName = value.floorName;
																	newArr.nursingStationDesc = value.nursingStationDesc;
																	newArr.bedCategoryDesc = value.bedCategoryDesc;
																	newArr.bedNumber = value.bedNumber;
																	newArr.priorityDesc = value.priorityDesc;
																	newArr.note = value.note;
																	newArr.housekeepingStatusDesc = value.housekeepingStatusDesc;
																	newArr.userName = value.userName;
																	$scope.housekeepingBedList.push(newArr);
																});
														$scope.commonListCount = response.data.object;
								            			$scope.pager = {};
								            			$scope.page;
								            			$scope.setPage(1,false);
													}
												});

							}

							// Toggle selection for a given Trans Req by id
							$scope.toggleSelectionAccept = function(item) {
								$scope.itemSelecteds = [];
								$scope.itemSelecteds = item;
								$('#noteRejectDrpDN').val('');
								$('#rejectReasonIdDrpDN').val('');
								$('#noteTransfer').val('');
								

							};
							
							$scope.saveAcceptReject = function()
							{
								if($('#rejectReasonIdDrpDN').val() == '' || $('#rejectReasonIdDrpDN').val() == null || $('#rejectReasonIdDrpDN').val() ==undefined)
									{
										growl.warning('Please select reason...!!',
																	{
																		title : 'Warning!'
																	});
										return false;
									}else{
										angular.element('#accepatReject').modal('hide');	
										$rootScope.startLoader();
								console.log($scope.itemSelecteds);
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');


								var data = {
									updatedDate : $scope.currentDate,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									//note : $('#noteReject').val(),
									//rejectionReasonId : $('#rejectReasonId').val(),
									note :$('#noteRejectDrpDN').val(),
									rejectionReasonId : $('#rejectReasonIdDrpDN').val(),
									bedId :$scope.itemSelecteds.bedId,
									housekeepingId : $scope.itemSelecteds.housekeepingId
								}
								
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
								+ HOUSEKEEPINGREJECTREQUESTFORNURSING;
						GenericService
								.serviceAction("POST", URI,
										data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												growl
														.success(
																'Cleaned Bed rejected Successfully...!!',
																{
																	title : 'Success!'
																});
												
												
												$scope.init();
												$scope.gethousekeepingBedListData($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
											} else {
												$rootScope.stopPopup();
												growl
														.error(
																'Something went wrong...!!',
																{
																	title : 'Error!'
																});
											}

										});
								
									}
								
							}
							
							
							
							
							$scope.saveAcceptance = function()
							{
								if($('#noteTransfer').val() == '' || $('#noteTransfer').val() == null || $('#noteTransfer').val() ==undefined)
								{
									growl.warning('Please add note...!!',
																{
																	title : 'Warning!'
																});
									return false;
								}else{
								$rootScope.startLoaderPopup();
								console.log($scope.itemSelecteds);
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');


								var data = {
									updatedDate : $scope.currentDate,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									note : $('#noteTransfer').val(),
									bedId :$scope.itemSelecteds.bedId,
									housekeepingId : $scope.itemSelecteds.housekeepingId
								}
							
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
								+ HOUSEKEEPINGACCEPTREQUESTFORNURSING;
						GenericService
								.serviceAction("POST", URI,
										data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoaderPopup();
												growl.success('Cleaned Bed Accepted Successfully !!',
																{
																	title : 'Success!'
																});
												angular.element('#commonNoteAcc').modal('hide');
												$scope.init();
												$scope.gethousekeepingBedListData($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
											} else {
												$rootScope.stopLoaderPopup();
												growl
														.error(
																'Something went wrong...!!',
																{
																	title : 'Error!'
																});
											}

										});
							}
								
							}
							
							

						} ]);
