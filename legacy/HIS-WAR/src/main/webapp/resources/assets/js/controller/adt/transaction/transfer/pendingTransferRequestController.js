'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:pendingTransferRequestController
 * @description #pendingTransferRequestController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'pendingTransferRequestController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'PagerService',
						'growl',
						'$state',
						'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,PagerService, growl,
								$state,$stateParams) {

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
								$scope.doctorId = cookieObject.doctorId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
							    $scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$scope.admittedPatient = {};
								$scope.itemSelecteds = {};

								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								
								
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

							}

							$scope.init();
							
							
							
							//For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.getPendingtransferRequestList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getPendingtransferRequestList = function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var commonObjTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId : 1,
										doctorId : $scope.doctorId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
									};

								var URI = BASE_URL + ROOT_URL
										+ GETTRANSFERREQUESTLIST;
								GenericService
										.serviceAction("POST", URI, commonObjTransfer)
										.then(
												function(response) {
													$scope.listOfTransferRequestList = [];
													$scope.transferRequestList = [];

													if (response.data.status == "success") {
														$scope.transferRequestList = response.data.listObject;
														angular.forEach($scope.transferRequestList, function( value, index) {

																			if($scope.bedManagmentCookiesObj!=null)
																			{
																				if(value.admissionId == $scope.bedManagmentCookiesObj.admissionId)
																				{
																					var newArr = {};
																						newArr.patientFullName = value.patientName;
																						newArr.doctorFullName = value.doctorName;
																						newArr.dob = value.dob;
																						newArr.genderCode = value.genderCode;
																						newArr.uhidnumber = value.uhidnumber;
																						newArr.patientId = value.patientId;
																						newArr.tPatientId = value.tPatientId;
																						newArr.bedNumber = value.bedNumber;
																						newArr.transferId = value.transferId;
																						newArr.transferRequestId = value.transferRequestId;
																						newArr.hierarchyId = value.hierarchyId;
																						newArr.prefixCode = value.prefixCode;
																						newArr.bedCategoryId = value.bedCategoryId;
																						newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																						newArr.transferStatus = value.transferStatus;
																						newArr.wardDesc = value.wardName;
																						newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																						newArr.reasonDesc = value.reasonDesc;
																						newArr.floorDesc = value.floorName
																						newArr.remark = value.note;
																						newArr.transferTypeId = value.transferTypeId;
																					$scope.listOfTransferRequestList.push(newArr);
																				}
																				
																			}else{
																				var newArr = {};
																					newArr.patientFullName = value.patientName;
																					newArr.doctorFullName = value.doctorName;
																					newArr.dob = value.dob;
																					newArr.genderCode = value.genderCode;
																					newArr.uhidnumber = value.uhidnumber;
																					newArr.patientId = value.patientId;
																					newArr.tPatientId = value.tPatientId;
																					newArr.bedNumber = value.bedNumber;
																					newArr.transferId = value.transferId;
																					newArr.transferRequestId = value.transferRequestId;
																					newArr.hierarchyId = value.hierarchyId;
																					newArr.prefixCode = value.prefixCode;
																					newArr.bedCategoryId = value.bedCategoryId;
																					newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																					newArr.transferStatus = value.transferStatus;
																					newArr.wardDesc = value.wardName;
																					newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																					newArr.reasonDesc = value.reasonDesc;
																					newArr.floorDesc = value.floorName
																					newArr.remark = value.note;
																					newArr.transferTypeId = value.transferTypeId;
																				$scope.listOfTransferRequestList.push(newArr);
																			}
																			

																		});
														
														$scope.commonListCount = response.data.object;
								            			$scope.pager = {};
								            			$scope.page;
								            			$scope.setPage(1,false);
								            			
														if($scope.listOfTransferRequestList.length==0)
														{
															growl
															.warning(
																	'No records found...!!',
																	{
																		title : 'Warning!'
																	});
														}

													}
												});


							}

							$scope.getPendingtransferRequestListMaster = function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var commonObjTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId : 1,
										doctorId : $scope.doctorId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
									};

								var URI = BASE_URL + ROOT_URL
										+ GETTRANSFERREQUESTLIST;
								GenericService
										.serviceAction("POST", URI, commonObjTransfer)
										.then(
												function(response) {
													$scope.listOfTransferRequestList = [];
													$scope.transferRequestList = [];

													if (response.data.status == "success") {
														$scope.transferRequestList = response.data.listObject;
														angular.forEach($scope.transferRequestList, function( value, index) {

																			if($scope.bedManagmentCookiesObj!=null)
																			{
																				if(value.admissionId == $scope.bedManagmentCookiesObj.admissionId)
																				{
																					var newArr = {};
																						newArr.patientFullName = value.patientName;
																						newArr.doctorFullName = value.doctorName;
																						newArr.dob = value.dob;
																						newArr.genderCode = value.genderCode;
																						newArr.uhidnumber = value.uhidnumber;
																						newArr.patientId = value.patientId;
																						newArr.tPatientId = value.tPatientId;
																						newArr.bedNumber = value.bedNumber;
																						newArr.transferId = value.transferId;
																						newArr.transferRequestId = value.transferRequestId;
																						newArr.hierarchyId = value.hierarchyId;
																						newArr.prefixCode = value.prefixCode;
																						newArr.bedCategoryId = value.bedCategoryId;
																						newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																						newArr.transferStatus = value.transferStatus;
																						newArr.wardDesc = value.wardName;
																						newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																						newArr.reasonDesc = value.reasonDesc;
																						newArr.floorDesc = value.floorName
																						newArr.remark = value.note;
																						newArr.transferTypeId = value.transferTypeId;
																					$scope.listOfTransferRequestList.push(newArr);
																				}
																				
																			}else{
																				var newArr = {};
																					newArr.patientFullName = value.patientName;
																					newArr.doctorFullName = value.doctorName;
																					newArr.dob = value.dob;
																					newArr.genderCode = value.genderCode;
																					newArr.uhidnumber = value.uhidnumber;
																					newArr.patientId = value.patientId;
																					newArr.tPatientId = value.tPatientId;
																					newArr.bedNumber = value.bedNumber;
																					newArr.transferId = value.transferId;
																					newArr.transferRequestId = value.transferRequestId;
																					newArr.hierarchyId = value.hierarchyId;
																					newArr.prefixCode = value.prefixCode;
																					newArr.bedCategoryId = value.bedCategoryId;
																					newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																					newArr.transferStatus = value.transferStatus;
																					newArr.wardDesc = value.wardName;
																					newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																					newArr.reasonDesc = value.reasonDesc;
																					newArr.floorDesc = value.floorName
																					newArr.remark = value.note;
																					newArr.transferTypeId = value.transferTypeId;
																				$scope.listOfTransferRequestList.push(newArr);
																			}
																			

																		});
														
														if($scope.listOfTransferRequestList.length==0)
														{
															growl
															.warning(
																	'No records found...!!',
																	{
																		title : 'Warning!'
																	});
														}

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
									$scope.getPendingtransferRequestListMaster($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.getPendingtransferRequestList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End
							

							// function for popup ok button call
							/*$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveTransferRequestByDoctor();
							}*/

							/*$scope.selectPatient = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;

								}
							}

							$scope.showNotePopup = function(note) {
								$scope.note = note;
							}*/

							

							// Toggle selection for a given Trans Req by id
							$scope.toggleSelectionAccept = function(item) {
								$scope.itemSelecteds = [];
								$scope.itemSelecteds = item;

							};

							

							// Toggle selection reject for a given transfer Request  by id
							$scope.toggleSelectionReject = function(item) {
								$scope.itemSelecteds = [];
								$scope.itemSelecteds = item;
							};
							
							$scope.note = {};
							$scope.saveTransferRequestByDoctor = function() {
								if($scope.selectionReject.length == 0 && $scope.selection.length == 0)
								{
								growl
								.error(
										'You are not selected any accept or reject!!!',
										{
											title : 'Error!'
										});
								}
							else{
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								}
								else{
									
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');


								var data = {
									updatedDate : $scope.currentDate,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									acceptTransferReqList : finalAcceptList,
									rejectTransferReqList : finalRejectList
								}
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
								+ ACCEPTREJECTREQUESTBYDOCTOR;
						GenericService
								.serviceAction("POST", URI,
										data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												growl
														.success(
																'Transfer Request Sent Successfully...!!',
																{
																	title : 'Success!'
																});
												$scope.init();
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
							}
							}
							/*$scope.submitRequest = function() {

								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								} else {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {

										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');

										var data = {
											treatingDoctorId : $scope.itemSelecteds.doctorId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											admissionId : $scope.itemSelecteds.admissionId,
											transferId : $scope.itemSelecteds.transferId,
											fromWardId : $scope.itemSelecteds.wardId,
											fromBedCategoryId : $scope.itemSelecteds.bedCategoryId,
											reasonId : $scope.itemSelecteds.reasonId,
											note : $scope.itemSelecteds.remark,
											authorizedBy : $scope.userId,
											//transferType :
											createdBy : $scope.userId,
											createdDate : $scope.currentDate,
											updatedBy : $scope.userId,
											updatedDate : $scope.currentDate
										}

										console
												.log($scope.itemSelecteds.requiredBedCategory);
										var array = $scope.itemSelecteds.requiredBedCategory
												.split('_');
										if (array[1] >= $scope.itemSelecteds.hierarchyId) {
											console.log(array[0] + "__"
													+ array[1]);
											data.transferStatus = 'D';
										} else {
											console.log(array[0] + "__"
													+ array[1]);
											data.transferStatus = 'A';
										}
										console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ ADDTRANSFERREQUEST;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Transfer Request sent successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.init();
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
								}
							}*/
							
							$scope.saveAcceptReject = function()
							{
								/*alert($('#rejectReasonId').val());
								alert($('#noteReject').val());
								var transferRequestId = $('#commonId').val();
								
								$('#drnote_'+transferRequestId).val($('#noteReject').val());
								$('#drrId_'+transferRequestId).val($('#rejectReasonId').val());*/
								
								console.log($scope.itemSelecteds);
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');


								var data = {
									updatedDate : $scope.currentDate,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									transferRequestId :$scope.itemSelecteds.transferRequestId,
									doctorNote :$('#noteRejectDrpDN').val(),
									doctorReasonId : $('#rejectReasonIdDrpDN').val(),
									transferId : $scope.itemSelecteds.transferId
								}
								$rootScope.startLoader();
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
								+ REJECTTRANSFERREQUESTBYDOCTOR;
						GenericService
								.serviceAction("POST", URI,
										data)
								.then(
										function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl
														.success(
																'Rejected...!!',
																{
																	title : 'Success!'
																});
												$scope.init();
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
							
							
							
							
							$scope.saveAcceptance = function()
							{
								angular.element('#commonNote').modal('hide');
								console.log($scope.itemSelecteds);
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');


								var data = {
									updatedDate : $scope.currentDate,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									doctorNote : $('#noteTransfer').val(),
									transferRequestId :$scope.itemSelecteds.transferRequestId
								}
								if($scope.itemSelecteds.transferTypeId == 3)
								{
								data.transferStatusId = 10;
								}
							else 
								{
								data.transferStatusId = 4;
								}
							//alert($scope.itemSelecteds.transferTypeId);
								$rootScope.startLoader();
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
								+ ACCEPTTRANSFERREQUESTBYDOCTOR;
						GenericService
								.serviceAction("POST", URI,
										data)
								.then(
										function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl
														.success(
																'Transfer Request accepted...!!',
																{
																	title : 'Success!'
																});
												
												$scope.init();
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

						} ]);
