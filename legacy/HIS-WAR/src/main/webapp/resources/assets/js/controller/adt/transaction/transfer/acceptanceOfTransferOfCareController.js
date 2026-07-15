'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:acceptanceOfTransferOfCareController
 * @description #acceptanceOfTransferOfCareController Controller of the myApp
 */

angular
		.module('myApp')
		.controller(
				'acceptanceOfTransferOfCareController',
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
						'$stateParams',
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams, PagerService) {

							/**Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.listCount;
							
							/* init() function for form object create. */
							$scope.init = function() {
								
								$rootScope.patientFullName = '';
								$rootScope.uhidNum = '';
								$rootScope.bedNumber = '';
								$rootScope.doctorFullName = '';
								$rootScope.wardName ='';
								$scope.treatingDoctorId = '';
								$scope.admissionId = '';
								
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
								$scope.userId = cookieObject.organizationId;
								$scope.doctorId = cookieObject.doctorId;
								$rootScope.itemSelectedsAccept = {};

								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								
								/*if ($cookies.get('bedManagmentCookies') != null) {
									$scope.bedManagmentCookiesObj = JSON
											.parse($cookies
													.get('bedManagmentCookies'));
									console.log($scope.bedManagmentCookiesObj);
									$scope.patientFullName = $scope.bedManagmentCookiesObj.patientFullName;
									$scope.uhidNum = $scope.bedManagmentCookiesObj.uhidNum;
									$scope.bedNumber = $scope.bedManagmentCookiesObj.bedNumber;
									$scope.doctorFullName = $scope.bedManagmentCookiesObj.doctorFullName;
									$scope.wardName = $scope.bedManagmentCookiesObj.wardName;
									
									var dataCookies = {
										admissionId : $scope.bedManagmentCookiesObj.admissionId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
									};

									var URI = BASE_URL + ROOT_URL
											+ GETOTLISTAGAINTPATIENT;
									GenericService
											.serviceAction("POST", URI,
													dataCookies)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$scope.patientSurgeryList = [];
															$scope.patientSurgeryList = response.data.listObject;
														}
													});

									var URI = BASE_URL
											+ ROOT_URL
											+ GETOTTRANSFERLISTAGAINTPATIENT;
									GenericService
											.serviceAction("POST", URI,
													dataCookies)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$scope.patientSurgeryTransferList = [];
															$scope.patientSurgeryTransferList = response.data.listObject;
														}
													});

								} else {
									$scope.bedManagmentCookiesObj = null;
								}*/

								$rootScope.loginpage = true;

								$scope.patientSurgeryOrder = {}
								$scope.itemSelecteds = {};

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var data = {};
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId : 4, // for tansfer of care
										doctorId : $scope.doctorId
									}
								
								
								/*	var URI = BASE_URL + ROOT_URL
											+ GETTRANSFEROFCAREREQUESTTODOCTORLIST;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.transferOfCareListForDoctor = [];

														if (response.data.status == "success") 
														{
															if($scope.bedManagmentCookiesObj!=null)
																{
																	angular.forEach(response.data.listObject.size,function(value,key) {
																		if(value.admissionId == $scope.bedManagmentCookiesObj.admissionId )
																		{
																			$scope.transferOfCareListForDoctor.push(value);
																		}
																	});
																}else{
																	$scope.transferOfCareListForDoctor = response.data.listObject;
																//}

															if(response.data.listObject.length > 0)
																{
																	$scope.transferOfCareListForDoctor = response.data.listObject;
																}else{
																	$scope.transferOfCareListForDoctor =[];
																	growl
																	.warning(
																			'No records found...!!',
																			{
																				title : 'Warning!'
																			});
																}
														}
													});*/
									
									var reasonCommonObj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
//											transferTypeId : 4, // for tansfer of care
//											doctorId : $scope.doctorId
										}	
									
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEREASONLIST;
							GenericService
									.serviceAction("POST", URI, reasonCommonObj)
									.then(
											function(response) {
												$scope.reasonList = [];

												if (response.data.status == "success") {
													$scope.reasonList = response.data.listObject;

												}
											});
							

							}

							$scope.init();
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId : 4, // for tansfer of care
										doctorId : $scope.doctorId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage
									}
								
								
									var URI = BASE_URL + ROOT_URL
											+ GETTRANSFEROFCAREREQUESTTODOCTORLIST;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.transferOfCareListForDoctor = [];

														if (response.data.status == "success") 
														{
															/*if($scope.bedManagmentCookiesObj!=null)
																{
																	angular.forEach(response.data.listObject.size,function(value,key) {
																		if(value.admissionId == $scope.bedManagmentCookiesObj.admissionId )
																		{
																			$scope.transferOfCareListForDoctor.push(value);
																		}
																	});
																}else{*/
															$scope.transferOfCareListForDoctor = response.data.listObject;
															$scope.transferOfCareListCount = response.data.object;
																//}

															if(response.data.listObject.length > 0)
																{
																	$scope.transferOfCareListForDoctor = response.data.listObject;
																}else{
																	$scope.transferOfCareListForDoctor =[];
																	growl
																	.warning(
																			'No records found...!!',
																			{
																				title : 'Warning!'
																			});
																}
															$scope.setPage(1,false);
														}
														$rootScope.stopLoader();
													});
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId : 4, // for tansfer of care
										doctorId : $scope.doctorId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage
									}
								
								
									var URI = BASE_URL + ROOT_URL
											+ GETTRANSFEROFCAREREQUESTTODOCTORLIST;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.transferOfCareListForDoctor = [];

														if (response.data.status == "success") 
														{
															/*if($scope.bedManagmentCookiesObj!=null)
																{
																	angular.forEach(response.data.listObject.size,function(value,key) {
																		if(value.admissionId == $scope.bedManagmentCookiesObj.admissionId )
																		{
																			$scope.transferOfCareListForDoctor.push(value);
																		}
																	});
																}else{*/
																	$scope.transferOfCareListForDoctor = response.data.listObject;
																//}

															if(response.data.listObject.length > 0)
																{
																	$scope.transferOfCareListForDoctor = response.data.listObject;
																	$scope.transferOfCareListCount = response.data.object;

																}else{
																	$scope.transferOfCareListForDoctor =[];
																	growl
																	.warning(
																			'No records found...!!',
																			{
																				title : 'Warning!'
																			});
																}
														}
														$rootScope.stopLoader();
													});
								
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.transferOfCareListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							$scope.initAcceptReject = function() {
								
								$rootScope.patientFullName = '';
								$rootScope.uhidNum = '';
								$rootScope.bedNumber = '';
								$rootScope.doctorFullName = '';
								$rootScope.wardName ='';
								$scope.treatingDoctorId = '';
								$scope.admissionId = '';
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.organizationId;
								$scope.doctorId = cookieObject.doctorId;
								$rootScope.itemSelectedsAccept = {};

								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								

								$rootScope.loginpage = true;

								$scope.patientSurgeryOrder = {}
								$scope.itemSelecteds = {};

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var data = {};
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										transferTypeId : 4, // for tansfer of care
										doctorId : $scope.doctorId
									}
								
								
									var URI = BASE_URL + ROOT_URL
											+ GETTRANSFEROFCAREREQUESTTODOCTORLIST;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.transferOfCareListForDoctor = [];

														if (response.data.status == "success") 
														{
															
																	$scope.transferOfCareListForDoctor = response.data.listObject;

															if(response.data.listObject.length > 0)
																{
																	$scope.transferOfCareListForDoctor = response.data.listObject;
																}else{
																	$scope.transferOfCareListForDoctor =[];
																	/*growl
																	.warning(
																			'No records found...!!',
																			{
																				title : 'Warning!'
																			});*/
																}
														}
													});
									
									var reasonCommonObj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
//											transferTypeId : 4, // for tansfer of care
//											doctorId : $scope.doctorId
										}	
									
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEREASONLIST;
							GenericService
									.serviceAction("POST", URI, reasonCommonObj)
									.then(
											function(response) {
												$scope.reasonList = [];

												if (response.data.status == "success") {
													$scope.reasonList = response.data.listObject;

												}
											});
							

							}
							
							$scope.getDoctorBySpeciality = function(id) {

								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorList = [];
													if (response.data.status == "success") {
														$scope.doctorList = response.data.listObject;
													}
												});

							}
							

							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ GETADMITTEDPATIENTBYKEWWORD;
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										// alert("ff");
										// console.log(response.data.listObject);
										return response.data.listObject;

									}
								});
								// return data="[{name:'test'}]";
							}

							$scope.selectPatientId = function(info, model,
									label) {
								console.log(info);
							/*	var data = {
									admissionId : info.admissionId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								};*/

								/*if (info.t_patient_id > 0) {
									$rootScope.patientFullName = info.patientName
											+ ' ' + info.dob + '/'
											+ info.genderName;
								} else {
									var age = $rootScope.getAge(info.dob);
									$rootScope.patientFullName = info.patientName
											+ ' ' + age + '/' + info.genderName;
								}

								$rootScope.uhidNum = info.uhidnumber;
								$rootScope.bedNumber = info.bedNumber;
								$rootScope.doctorFullName = info.dFirstName;
								$rootScope.wardName = info.wardName;
								$scope.treatingDoctorId = info.doctorId;
								$scope.admissionId = info.admissionId;*/
								var data={
										admissionId:info.admissionId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										transferTypeId:4
								}
									var URI = BASE_URL + ROOT_URL
											+ SEARCHCONSULTORDERREQUESTBYADMISSIONID;
									GenericService
											.serviceAction("POST", URI,data)
											.then(
													function(response) {
														$scope.transferOfCareListForDoctor = [];

														if (response.data.status == "success") {
															$scope.transferOfCareListForDoctor = response.data.listObject;

														}
													});
								
							}

						


							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveTransferOfCareRequest();
							}

							$scope.saveTransferOfCareRequest = function() {

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {

										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');
										var data = {
											admissionId : $scope.admissionId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											treatingDoctorId : $scope.treatingDoctorId,
											specialityId : $scope.specialityId,
											requestDoctorId : $scope.requestDoctorId,
											reasonId : $scope.reasonId,
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											createdDate : $scope.currentDate,
											updatedDate : $scope.currentDate,
											status : 'A',
											note : $scope.note,
											transferTypeId:2
										};
										console.log(data);
//										return false;
										var URI = BASE_URL + ROOT_URL
												+ SAVEOTTRANSFERREQUEST;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'OT Request Sent Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.init();
																$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
							
							$scope.getAdmissionDataAccept = function(item)
							{
								//alert("sc");
								console.log(item);
								$rootScope.itemSelectedsAccept = [];
								$rootScope.itemSelectedsAccept = item;
								console.log($rootScope.itemSelectedsAccept);
								
							}
							
							$scope.saveAcceptance = function()
							{
								console.log($rootScope.itemSelectedsAccept);
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
							
								var tansferNote = $('#noteTransfer').val();
								if(tansferNote==""||tansferNote==null){
									growl.warning(" Please fill note  ..!!",{
										title : "WARNING!"
											});
									   return false;
								}
									
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										transferTypeId:$scope.itemSelectedsAccept.transferTypeId,
										updatedBy:$scope.userId,
										updatedDate:$scope.currentDate,
										transferOfCareId:$scope.itemSelectedsAccept.transferOfCareId,
										remark :tansferNote,
										admissionId:$scope.itemSelectedsAccept.admissionId,
										toSpecialityId : $scope.itemSelectedsAccept.toSpecialityId,
										authorizedBy : $scope.userId,
										transferStatusId : 11,
										requestTo : $scope.itemSelectedsAccept.requestTo
										
								};
								
								console.log(data);
								
								var URI = BASE_URL + ROOT_URL
										+ SAVEACCEPTTRANSFEROFCAREREQUEST;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Transfer of Care request accepted successfully...!!',
																		{
																			title : 'Success!'
																		});
														//commonNote
														angular.element(
														'#commonNote')
														.modal('hide');
														$scope.noteTransfer="";
														$scope.initAcceptReject();
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
							
							$scope.saveAcceptReject = function()
							{
								
								$scope.rejectReasonId = $('#rejectReasonIdDrpDN').val();
								if($scope.rejectReasonId == null|| $scope.rejectReasonId=='')
								{
									  growl.warning(" Please Select Reason ..!!",{title : "WARNING!"});
									   return false;
								}
								console.log($scope.rejectReasonId);
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
							
								var data = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												transferTypeId:$scope.itemSelectedsAccept.transferTypeId,
												updatedBy:$scope.userId,
												updatedDate:$scope.currentDate,
												transferOfCareId:$scope.itemSelectedsAccept.transferOfCareId,
												authorizedBy : $scope.userId,
												transferStatusId : 5,
												remark :$('#noteRejectDrpDN').val(),
												rejectReasonId : $('#rejectReasonIdDrpDN').val(),
												
										
								};
								console.log(data);
								
								var URI = BASE_URL + ROOT_URL
										+ REJECTTRANSFEROFCAREREQUEST;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Patient has been rejected successfully...!!',
																		{
																			title : 'Success!'
																		});
														angular.element(
														'#accepatReject')
														.modal('hide');
														$scope.initAcceptReject();
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
							$scope.displayNote=function(note)
							{
								$scope.noteDoctor = note;
								
							}
						
							
							/*$scope.acceptRequest = function(transferId) {
							
											if ($scope.popUpFlag) {
												$scope.showModal = !$scope.showModal;

											} else {
												
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate)
														.format('DD-MM-YYYY HH:mm:ss');
												
										
										
											}
										
								
								
							}*/

						} ]);

