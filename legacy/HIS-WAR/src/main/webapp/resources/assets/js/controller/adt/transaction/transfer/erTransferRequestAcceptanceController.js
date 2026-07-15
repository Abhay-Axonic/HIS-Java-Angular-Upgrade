/**
 *.js
 */
'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller: transferRequestAcceptanceController
 * @description # transferRequestAcceptanceController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'erTransferRequestAcceptanceController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,PagerService , growl,
								$state) {

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

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								$scope.admittedPatient = {};
								$scope.itemSelecteds = {};
								
								$scope.beditemSelecteds = {};
								$scope.bedTr = {};
								$scope.rejectTransferPopUpFlag = false;

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
						

			/*					var URI = BASE_URL + ROOT_URL
										+ GETTRANSFERREQUESTACCEPTANCELIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.listOfTransferRequestAcceptanceList = [];
													$scope.admittedPatientList = [];
													
													if (response.data.status == "success") {
														$scope.admittedPatientList = response.data.listObject;
														angular
																.forEach(
																		$scope.admittedPatientList,
																		function(
																				value,
																				index) {

																			var newArr = {};
																			newArr.patientFullName = value.patientName;
																			newArr.dob = value.dob;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.transferId = value.transferId;
																			newArr.hierarchyId = value.hierarchyId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.bedCategoryId = value.fromBedCategoryId;
																			newArr.bedCategoryDesc = value.fromBedCategoryDesc;
																			newArr.doctorId  = value.doctorId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc; 
																			newArr.wardId = value.wardId;
																			newArr.wardName = value.wardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.remark = value.note;
																			newArr.toBedCategoryId = value.toBedCategoryId;
																			newArr.doctorFullName = value.doctorName;
																			newArr.transferRequestId = value.transferRequestId;
																			newArr.doctorNote = value.doctorNote;
																			newArr.transferTypeId = value.transferTypeId;
																			newArr.toBedId = value.toBedId;
																			newArr.fromWardId = value.fromWardId;
																			newArr.genderId = value.genderId;
																			$scope.listOfTransferRequestAcceptanceList
																					.push(newArr);

																		});

													}
												});*/

							}

							$scope.init();
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initBedReservetionList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initBedReservetionList = function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								var URI = BASE_URL + ROOT_URL
								+ GETTRANSFERREQUESTACCEPTANCELIST;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.listOfTransferRequestAcceptanceList = [];
											$scope.admittedPatientList = [];
											
											if (response.data.status == "success") {
												$scope.admittedPatientList = response.data.listObject;
												angular.forEach( $scope.admittedPatientList, function( value, index) {

																	var newArr = {};
																	newArr.patientFullName = value.patientName;
																	newArr.dob = value.dob;
																	newArr.genderCode = value.genderCode;
																	newArr.uhidnumber = value.uhidnumber;
																	newArr.patientId = value.patientId;
																	newArr.tPatientId = value.tPatientId;
																	newArr.bedNumber = value.bedNumber;
																	newArr.admissionId = value.admissionId;
																	newArr.transferId = value.transferId;
																	newArr.hierarchyId = value.hierarchyId;
																	newArr.prefixCode = value.prefixCode;
																	newArr.bedCategoryId = value.fromBedCategoryId;
																	newArr.billingBedCategoryId = value.fromBillingBedCategoryId;
																	newArr.fromRoomId = value.fromRoomId;
																	newArr.bedCategoryDesc = value.fromBedCategoryDesc;
																	newArr.doctorId  = value.doctorId;
																	newArr.admissionDetailsId = value.admissionDetailsId;
																	newArr.transferStatusDesc = value.transferStatusDesc; 
																	newArr.wardId = value.wardId;
																	newArr.wardName = value.wardName;
																	newArr.transferStatusId = value.transferStatusId;
																	newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																	newArr.reasonDesc = value.reasonDesc;
																	newArr.remark = value.note;
																	newArr.toBedCategoryId = value.toBedCategoryId;
																	newArr.doctorFullName = value.doctorName;
																	newArr.transferRequestId = value.transferRequestId;
																	newArr.doctorNote = value.doctorNote;
																	newArr.transferTypeId = value.transferTypeId;
																	newArr.toBedId = value.toBedId;
																	newArr.fromWardId = value.fromWardId;
																	newArr.fromBedId = value.fromBedId;
																	newArr.genderId = value.genderId;
																	newArr.createdDate=value.createdDate;
																	newArr.imagePath=value.imagePath;
																	newArr.age=value.ageStr;
																	newArr.prefixDesc=value.prefixDesc;
																	$scope.listOfTransferRequestAcceptanceList.push(newArr);

																});
												$scope.commonListCount = response.data.object;
												$scope.setPage(1,false);
												console.log("commonListCount",response.data.object);
											}
										});
								
							}

							$scope.getBedReservetionList = function(orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var data = {
										organizationId : orgId,
										unitId : $scope.unitId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
									}
									var URI = BASE_URL + ROOT_URL
									+ GETTRANSFERREQUESTACCEPTANCELIST;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.listOfTransferRequestAcceptanceList = [];
												$scope.admittedPatientList = [];
												
												if (response.data.status == "success") {
													$scope.admittedPatientList = response.data.listObject;
													angular.forEach( $scope.admittedPatientList, function( value, index) {

																		var newArr = {};
																		newArr.patientFullName = value.patientName;
																		newArr.dob = value.dob;
																		newArr.genderCode = value.genderCode;
																		newArr.uhidnumber = value.uhidnumber;
																		newArr.patientId = value.patientId;
																		newArr.tPatientId = value.tPatientId;
																		newArr.bedNumber = value.bedNumber;
																		newArr.admissionId = value.admissionId;
																		newArr.transferId = value.transferId;
																		newArr.hierarchyId = value.hierarchyId;
																		newArr.prefixCode = value.prefixCode;
																		newArr.bedCategoryId = value.fromBedCategoryId;
																		newArr.billingBedCategoryId = value.fromBillingBedCategoryId;
																		newArr.fromRoomId = value.fromRoomId;
																		newArr.bedCategoryDesc = value.fromBedCategoryDesc;
																		newArr.doctorId  = value.doctorId;
																		newArr.admissionDetailsId = value.admissionDetailsId;
																		newArr.transferStatusDesc = value.transferStatusDesc; 
																		newArr.wardId = value.wardId;
																		newArr.wardName = value.wardName;
																		newArr.transferStatusId = value.transferStatusId;
																		newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																		newArr.reasonDesc = value.reasonDesc;
																		newArr.remark = value.note;
																		newArr.toBedCategoryId = value.toBedCategoryId;
																		newArr.doctorFullName = value.doctorName;
																		newArr.transferRequestId = value.transferRequestId;
																		newArr.doctorNote = value.doctorNote;
																		newArr.transferTypeId = value.transferTypeId;
																		newArr.toBedId = value.toBedId;
																		newArr.fromWardId = value.fromWardId;
																		newArr.fromBedId = value.fromBedId;
																		newArr.genderId = value.genderId;
																		newArr.createdDate=value.createdDate;
																		newArr.imagePath=value.imagePath;
																		newArr.age=value.ageStr;
																		newArr.prefixDesc=value.prefixDesc;
																		$scope.listOfTransferRequestAcceptanceList.push(newArr);

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
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getBedReservetionList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}
							
							$scope.initBedReservetionList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End
							
							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								var transferId = $scope.itemSelecteds.transferId;
								$scope.acceptRequest(transferId);
							}
							
							
							$scope.selectItemBed = function(item) {
								console.log(item);
								$scope.beditemSelecteds = [];
								// If checkbox is checked
								if ($scope.bedTr) {
									$scope.beditemSelecteds = item;
								}
							}

							$scope.selectedBedObject = function() {
								console.log($scope.beditemSelecteds);
								angular.element('#bedAllocationForTransfer').modal(
										'hide');
							}
							
							$scope.transferIdForCheck = '';
							$scope.toSelectedBedId = 0;
							$scope.bedAllocastionTransfer = function(bedCategoryId,transferId,transferTypeId,fromWardId,toBedId,genderId)
							{
								//alert(toBedId);
								//return false;
								$scope.patGenderId = genderId;
								if(toBedId != null)
									{
									$scope.toSelectedBedId = toBedId.toString();
									}
								
								
								var data = {
										organizationId:$scope.organizationId,
										unitId : $scope.unitId,
										bedCategoryId :bedCategoryId,
										transferTypeId : transferTypeId,
										wardId : fromWardId
								}
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEBEDLIST;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.activeBedList = [];

											if (response.data.status == "success") {
												$scope.activeBedList = response.data.listObject;
												angular
												.forEach(
														$scope.activeBedList,
														function(
																value,
																index) {
															
														if(value.bedId == toBedId)
															{
															var data = {bedId:value.bedId,
															wardId : value.wardId,
															roomId : value.roomId,
															billingBedCategoryId:value.billingBedCategoryId}
															$scope.beditemSelecteds = data;
															}
															console.log($scope.beditemSelecteds);
															
														});

											}
										});
								
								
							}
							
							

							$scope.selectPatient = function(item) {
								
								$scope.transferIdForCheck = item.transferId;
								$scope.rejectTransferPopUpFlag = true;
								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;

								}
							}

							$scope.acceptRequest = function(transferId) {
								if($scope.transferIdForCheck == transferId)
									{
										
										console.log($scope.itemSelecteds);
										if (Object.keys($scope.beditemSelecteds).length === 0) {
											growl
													.error(
															'Please Allocate bed before accept...!!',
															{
																title : 'Error!'
															});
										} else {
											if ($scope.popUpFlag) {
												$scope.showModal = !$scope.showModal;

											} else {
												$rootScope.startLoader();
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate)
														.format('DD-MM-YYYY HH:mm:ss');
												
												var data = {
														organizationId:$scope.organizationId,
														unitId : $scope.unitId,
														toBedCategoryId :$scope.itemSelecteds.toBedCategoryId,
														toBedId:$scope.beditemSelecteds.bedId,
														toWardId : $scope.beditemSelecteds.wardId,
														toRoomId :  $scope.beditemSelecteds.roomId,
														toBillingBedCategoryId: $scope.beditemSelecteds.billingBedCategoryId,
														updatedBy:$scope.userId,
														updatedDate : $scope.currentDate,
														transferId : $scope.itemSelecteds.transferId,
														transferRequestId : $scope.itemSelecteds.transferRequestId
												}
												console.log(data);
												
												var URI = BASE_URL + ROOT_URL
												+ ACCEPTTRANSFERREQUESTFORADT;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Transfer Request accepted...!!',
																				{
																					title : 'Success!'
																				});
																$scope.initBedReservetionList(
																		$scope.organizationId, $scope.offset,
																		$scope.noOfRecordsPerPage);
																$scope.init();
															} else {
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
															}
															$rootScope.stopLoader();

														});
										
										
											}
										}
									}
								else
									{
									growl
									.error(
											'Please select correct patient for accept...!!',
											{
												title : 'Error!'
											});
									return false;
									}
								
							}
							
							$scope.rejectRequest = function(transferId)
							{
								if($scope.transferIdForCheck == transferId)
								{
									
									
								}
							else
								{
								growl
								.error(
										'Please select correct patient for Reject...!!',
										{
											title : 'Error!'
										});
								return false;
								}
							}
							
							$scope.saveAcceptReject = function()
							{
								//alert($scope.rejectReasonId);
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select patient...!!',
													{
														title : 'Error!'
													});
								}else
									{
									angular.element('#accepatReject').modal('hide');
									$rootScope.startLoader();
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
									var data = {
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											updatedBy:$scope.userId,
											updatedDate : $scope.currentDate,
											transferId : $scope.itemSelecteds.transferId,
											transferRequestId : $scope.itemSelecteds.transferRequestId,
											adtRejectReason :$('#noteRejectDrpDN').val(),
											adtReasonId : $('#rejectReasonIdDrpDN').val(),
									}
									console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL
									+ REJECTTRANSFERREQUESTFORADT;
							GenericService
									.serviceAction("POST", URI,
											data)
									.then(
											function(response) {

												if (response.data.status == "success") {
													growl
															.success(
																	'Transfer Request accepted...!!',
																	{
																		title : 'Success!'
																	});
													$scope.initBedReservetionList($scope.organizationId, $scope.offset,$scope.noOfRecordsPerPage);
													$scope.init();
												} else {
													growl
															.error(
																	'Something went wrong...!!',
																	{
																		title : 'Error!'
																	});
												}
												$rootScope.stopLoader();

											});
									}
							}

						} ]);
