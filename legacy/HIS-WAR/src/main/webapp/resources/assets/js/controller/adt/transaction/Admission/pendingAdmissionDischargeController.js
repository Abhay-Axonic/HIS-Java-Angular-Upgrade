'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:pendingAdmissionDischargeController
 * @description # pendingAdmissionDischargeController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'pendingAdmissionDischargeController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'growl',
						'$state',
						'$filter',
						'PagerService',
						'reportService',
						'GenericServiceFileUpload',
						'loyaltiCardCallFactory',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,BillingGenericService, growl,
								$state, $filter, PagerService,reportService,GenericServiceFileUpload,loyaltiCardCallFactory) {

							$scope.datepicker = [];
							$scope.datepickerPdd =[];	
							
							$scope.openDatePicker = function(index) {

										$scope.datepicker[index].opened = true;
										if(moment($scope.pendingAdmissionList[index].datepicker).isBefore($scope.pendingAdmissionList[index].datepickerPdd));
											{
												$scope.pendingAdmissionList[index].datepickerPdd = $scope.pendingAdmissionList[index].datepicker;
											}
							};

							$scope.openDatePickerPdd = function(index) {

										$scope.datepickerPdd[index].opened = true;
										$scope.dateOptions.minDate = $scope.pendingAdmissionList[index].datepicker;
										
							};
							
							$scope.openDatePickerDepo = function() {
								$scope.datepickerdepo.opened = true;
							};

							$scope.datepickerdepo = {
								opened : false
							};

							$scope.openDatePicker1 = function() {
								$scope.datepicker.opened = true;
							};
							
							

							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									
								};
							
							$scope.openDatePickerDeposit = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate:new Date()
								};
							
							$scope.dateOptionsTrancs = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							
							$scope.dateOptionsDoa = {
								formatYear : 'yyyy',
								showWeeks : false,
								minDate:new Date()
							};

							/* init() function for form object create. */
							    $scope.init = function() {

								$scope.reserverBedPopUpFlag = false;
								$scope.cancelAdmissionNoteId=null;
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.mpesaRequired = cookieObject.mpesaRequired;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								 $scope.send=false;
								 $scope.resend=false;
								 $scope.validatTextFieldFlag = true;
								$rootScope.loginpage = true;
								$scope.pendingAdmissionRequest = {}
								$scope.itemSelecteds = {};

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var data = {};

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}

								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETPENDINGADMISSIONREQUEST; GenericService
								 * .serviceAction("POST", URI, commonObj) .then(
								 * function(response) {
								 * $scope.pendingAdmissionList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.pendingLists =
								 * response.data.listObject; angular .forEach(
								 * $scope.pendingLists, function( value, index) {
								 * var newArr = {}; newArr.admissionNoteId =
								 * value.admissionNoteId; newArr.reasonDesc =
								 * value.reasonDesc; newArr.specialityName =
								 * value.specialityName; newArr.dFirstName =
								 * value.dFirstName; newArr.dMiddleName =
								 * value.dMiddleName; newArr.dFirstName =
								 * value.dFirstName; newArr.dLastName =
								 * value.dLastName; newArr.pFirstName =
								 * value.pFirstName; newArr.pMiddleName =
								 * value.pMiddleName; newArr.pLastName =
								 * value.pLastName; newArr.visitTypeName =
								 * value.visitTypeName; newArr.isFlexiableDate =
								 * value.isFlexiableDate; newArr.reasonDesc =
								 * value.reasonDesc; newArr.paymentEntitlementId =
								 * value.paymentEntitlementId .toString();
								 * newArr.dob = value.dob;
								 * newArr.datepickerNoFle = value.doa;
								 * newArr.datepickerPddNoFle = value.pdd;
								 * newArr.datepicker = new Date( value.doa);
								 * newArr.datepickerPdd = new Date( value.pdd);
								 * newArr.bedCategoryDesc =
								 * value.bedCategoryDesc; newArr.genderCode =
								 * value.genderCode; newArr.patientCategoryId =
								 * value.patientCategoryId .toString();
								 * newArr.bedCategoryId = value.bedCategoryId
								 * .toString(); newArr.uhidnumber =
								 * value.uhidnumber; newArr.genderId = value.id;
								 * newArr.doctorSpecialityId =
								 * value.doctorSpecialityId;
								 * newArr.requestToDoctorId =
								 * value.requestToDoctorId; newArr.visitTypeId =
								 * value.visitTypeId; newArr.patientId =
								 * value.patientId; newArr.patientCategoryDesc =
								 * value.patientCategory;
								 * newArr.paymentEntitlementDesc =
								 * value.paymentEntitlementDesc;
								 * $scope.pendingAdmissionList .push(newArr);
								 * }); console .log($scope.pendingLists);
								 *  } });
								 */

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPATIENTCATEGORY;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.patientCategoryList = [];

													if (response.data.status == "success") {
														$scope.patientCategoryList = response.data.listObject;
														console
																.log($scope.patientCategoryList);

													}
												});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEDOCUMENTTYPELIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.documentTypeList = [];

													if (response.data.status == "success") {
														$scope.documentTypeList = response.data.listObject;

													}
												});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPAYMENTENTITLEMENT;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.paymentEntitlementList = [];

													if (response.data.status == "success") {
														$scope.paymentEntitlementList = response.data.listObject;
														console
																.log($scope.paymentEntitlementList);

													}
												});

								/*var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDCATEGORYLISTBYUNIT;*/
								var URI = BASE_URL + ROOT_URL +"/adt/bedCategory/availableBeds";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {

													$scope.bedCategoryList=[];
													$scope.dayCareBedCategoryList=[]
													$scope.allBedCategoryList=response.data.listObject;
													$scope.dayCareIpBillingBedCateList=response.data.object.dayCareIpBedCategoryList;
                                                        angular.forEach($scope.allBedCategoryList,function(value,index){
                                                            var flag=false;
                                                            angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
                                                                //for day Care
                                                            	if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==3){
                                                                    flag=true;
                                                                }
                                                            });
                                                            if(flag==true){
                                                               $scope.dayCareBedCategoryList.push(value);
                                                            }
                                                        });
														angular.forEach($scope.allBedCategoryList,function(value,index){
															var flag=false;
															angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
																//for day Care
																if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==2){
																	flag=true;
																}
															});
															if(flag==true){
																$scope.bedCategoryList.push(value);
															}
														});
											
												});
								
							

							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initPendingAdmissionList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initPendingAdmissionList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETPENDINGADMISSIONREQUEST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTPENDINGADMISSIONLIST;

								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}

								// For Get Pending Admission Request List Using
								// Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.pendingAdmissionList = [];

													if (response.data.status == "success") {
														$scope.pendingLists = response.data.listObject;
														angular
																.forEach(
																		$scope.pendingLists,
																		function(
																				value,
																				index) {
																			var newArr = {};
																			newArr.admissionNoteId = value.admissionNoteId;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.specialityName = value.specialityName;
																			newArr.imagePath = value.imagePath;
																			newArr.doctorFullName = value.dFirstName;
																			newArr.patientFullName = value.pFirstName;
																			newArr.visitTypeName = value.visitTypeName;
																			newArr.isFlexiableDate = value.isFlexiableDate;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.paymentEntitlementId = value.paymentEntitlementId
																					.toString();
																			newArr.dob = value.dob;
																			newArr.datepickerNoFle = value.doa;
																			newArr.datepickerPddNoFle = value.pdd;
																			newArr.datepicker = new Date(
																					value.doa);
																			newArr.datepickerPdd = new Date(
																					value.pdd);
																			newArr.bedCategoryDesc = value.bedCategoryDesc;
																			newArr.genderCode = value.genderCode;
																			newArr.depositAmount = value.depositAmount;
																			newArr.depositCollected = value.depositCollected;
																			newArr.depositPending = value.depositPending;
																			newArr.depositStatus = value.depositStatus;
																			if(value.patientCategoryId == null)
																				{
																					newArr.patientCategoryId = '';
																				}
																			else
																				{
																					newArr.patientCategoryId = value.patientCategoryId.toString();
																				}
																			
																			newArr.patientCategory=value.patientCategory;
																			newArr.bedCategoryId = value.bedCategoryId
																					.toString();
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.genderId = value.id;
																			newArr.doctorSpecialityId = value.doctorSpecialityId;
																			newArr.requestToDoctorId = value.requestToDoctorId;
																			newArr.visitTypeId = value.visitTypeId;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.patientCategoryDesc = value.patientCategory;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.oldDoa = value.doa;
																			newArr.oldBedCategoryId = value.bedCategoryId;
																			newArr.bedCategoryWaitingListId = value.bedCategoryWaitingListId;
																			newArr.depositAmount = value.depositAmount;
																			newArr.depositCollected = value.depositCollected;
																			newArr.depositPending = value.depositPending;
																			newArr.depositStatus = value.depositStatus;
																			newArr.registrationTypeId = value.registrationTypeId;
																			newArr.prefixId = value.prefixId;
																			newArr.firstName = value.firstName;
																			newArr.middleName = value.middleName;
																			newArr.lastName = value.lastName;
																			newArr.mobileNumber = value.mobileNumber;
																			newArr.email = value.email;
																			
																			newArr.admissionTypeId = value.admissionTypeId;
																			newArr.age = value.age;
																			newArr.admissionNoteNumber = value.admissionNoteNumber;
																			newArr.billingBedCategoryId = value.billingBedCategoryId;
																			newArr.billingBedDesc = value.billingBedDesc;
																			newArr.oldPdd = value.pdd;
																			newArr.reasonId = value.reasonId;
																			newArr.treatingDoctorId = value.treatingDoctorId; 
																			newArr.treatingDoctorSpecialityId = value.treatingDoctorSpecialityId; 
																			newArr.isAdmissionReqFromCoversheet = value.isAdmissionReqFromCoversheet;
																			newArr.requestRejectionReason = value.requestRejectionReason; 
																			newArr.requestRejectionNote = value.requestRejectionNote;
																			newArr.sourceVisitTypeId = value.sourceVisitTypeId;
																			$scope.datepicker.push({opened:false});
																			$scope.datepickerPdd.push({opened:false});
																			newArr.bloodGroup = value.bloodGroup;
																			 
																			$scope.pendingAdmissionList
																					.push(newArr);
																		});
														console
																.log($scope.pendingLists);

													}
												});

								// For Count Pending Admission Request List
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

							$scope.getPendingAdmissionList = function(orgId,
									offset, noOfRecordsPerPage) 
								{
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETPENDINGADMISSIONREQUEST;
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Pending Admission Request List Using
								// Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.pendingAdmissionList = [];

													if (response.data.status == "success") {
														$scope.pendingLists = response.data.listObject;
														angular
																.forEach(
																		$scope.pendingLists,
																		function(
																				value,
																				index) {
																			var newArr = {};
																			newArr.admissionNoteId = value.admissionNoteId;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.specialityName = value.specialityName;
																			newArr.doctorFullName = value.dFirstName;
																			newArr.patientFullName = value.pFirstName;
																			newArr.visitTypeName = value.visitTypeName;
																			newArr.isFlexiableDate = value.isFlexiableDate;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.paymentEntitlementId = value.paymentEntitlementId
																					.toString();
																			newArr.dob = value.dob;
																			newArr.datepickerNoFle = value.doa;
																			newArr.datepickerPddNoFle = value.pdd;
																			newArr.datepicker = new Date(
																					value.doa);
																			newArr.datepickerPdd = new Date(
																					value.pdd);
																			newArr.bedCategoryDesc = value.bedCategoryDesc;
																			newArr.genderCode = value.genderCode;
																			newArr.depositAmount = value.depositAmount;
																			newArr.depositCollected = value.depositCollected;
																			newArr.depositPending = value.depositPending;
																			newArr.depositStatus = value.depositStatus;
																			if(value.patientCategoryId == null)
																			{
																			newArr.patientCategoryId = '';
																			}
																		else
																			{
																			newArr.patientCategoryId = value.patientCategoryId
																			.toString();
																			}
																			newArr.patientCategory=value.patientCategory;
																			newArr.bedCategoryId = value.bedCategoryId
																					.toString();
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.imagePath = value.imagePath;
																			newArr.genderId = value.id;
																			newArr.doctorSpecialityId = value.doctorSpecialityId;
																			newArr.requestToDoctorId = value.requestToDoctorId;
																			newArr.visitTypeId = value.visitTypeId;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.patientCategoryDesc = value.patientCategory;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.oldDoa = value.doa;
																			newArr.oldBedCategoryId = value.bedCategoryId;
																			newArr.bedCategoryWaitingListId = value.bedCategoryWaitingListId;
																			newArr.depositAmount = value.depositAmount;
																			newArr.depositCollected = value.depositCollected;
																			newArr.depositPending = value.depositPending;
																			newArr.depositStatus = value.depositStatus;
																			newArr.registrationTypeId = value.registrationTypeId;
																			newArr.prefixId = value.prefixId;
																			newArr.firstName = value.firstName;
																			newArr.middleName = value.middleName;
																			newArr.lastName = value.lastName;
																			newArr.mobileNumber = value.mobileNumber;
																			newArr.email = value.email;
																			newArr.admissionTypeId = value.admissionTypeId;
																			newArr.age = value.age;
																			newArr.admissionNoteNumber = value.admissionNoteNumber;
																			newArr.billingBedCategoryId = value.billingBedCategoryId;
																			newArr.billingBedDesc = value.billingBedDesc;
																			newArr.oldPdd = value.pdd;
																			newArr.reasonId = value.reasonId;
																			newArr.treatingDoctorId = value.treatingDoctorId;
																			newArr.treatingDoctorSpecialityId = value.treatingDoctorSpecialityId;
																			newArr.isAdmissionReqFromCoversheet = value.isAdmissionReqFromCoversheet;
																			newArr.requestRejectionReason = value.requestRejectionReason; 
																			newArr.requestRejectionNote = value.requestRejectionNote;
																			newArr.sourceVisitTypeId = value.sourceVisitTypeId;
																			newArr.bloodGroup = value.bloodGroup;
																			$scope.pendingAdmissionList
																					.push(newArr);
																		});
														console
																.log($scope.pendingLists);

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
									$scope.getPendingAdmissionList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initPendingAdmissionList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.updatePendingRequest();
							}
							$scope.isDayCare='N';
							$scope.selectItem = function(item) {
								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.pendingAdmissionRequest) {
									$scope.itemSelecteds = item;
									if($scope.itemSelecteds.visitTypeId != 4)
										{
											//$scope.reserverBedPopUpFlag = true;
										}
									
								}
								 if(item.visitTypeId==3){
									 $scope.isDayCare='Y';
								 }else{
									 $scope.isDayCare='N';
								 }
							}

							// Function for update Admission Request
							$scope.updatePendingRequest = function() {

								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								console.log($scope.itemSelecteds);
								// console.log($scope.itemSelecteds.pLastName);
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {
									var waitListNumber = 0;
									var dateOfPdd = moment(
											$scope.itemSelecteds.datepickerPdd)
											.format('DD-MM-YYYY HH:mm:ss');
									var dateOfAdmission = moment(
											$scope.itemSelecteds.datepicker)
											.format('DD-MM-YYYY HH:mm:ss');
									//console.log("dateOfPdd",dateOfPdd);
									//console.log("dateOfAdmission",dateOfAdmission);
									//&& dateOfPdd >= dateOfAdmission
									//if(dateOfAdmission > dateOfPdd )
									if($scope.itemSelecteds.datepicker > $scope.itemSelecteds.datepickerPdd)
										{
											growl
											.warning(
													'DOA should be less then PDD',
													{
														title : 'Warning!'
													});
											return false;
										}else
											{
											$rootScope.startLoader();
									if($scope.itemSelecteds.bedCategoryWaitingListId != null)
										{
										waitListNumber = $scope.itemSelecteds.bedCategoryWaitingListId;
										}
									var data = {

										admissionNoteId : $scope.itemSelecteds.admissionNoteId,
										bedCategoryId : $scope.itemSelecteds.bedCategoryId,
										paymentEntitlementId : $scope.itemSelecteds.paymentEntitlementId,
										patientCategoryId : $scope.itemSelecteds.patientCategoryId,
										doa : dateOfAdmission,
										pdd : dateOfPdd,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										createdDate : $scope.currentDate,
										organizationId:$scope.organizationId,
										unitId : $scope.unitId,
										oldDoa : moment($scope.itemSelecteds.oldDoa)
												.format('DD-MM-YYYY HH:mm:ss'),
									    oldBedCategoryId: $scope.itemSelecteds.oldBedCategoryId,
									    status : 'A',
									    bedCategoryWaitingListId :waitListNumber
									}
									console.log(data);
									var URI = BASE_URL + ROOT_URL
											+ UPDATEPENDINGADMISSION;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Admission updated Successfully...!!',
																			{
																				title : 'Success!'
																			});
															$scope.init();
															$rootScope.stopLoader();
														} else {
															growl
																	.error(
																			'Something went wrong...!!',
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														$scope
																.initPendingAdmissionList(
																		$scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);

													});
								}
							}

							}

							$scope.initList = function()
							{
								$scope.initPendingAdmissionList(
										$scope.organizationId,
										$scope.offset,
										$scope.noOfRecordsPerPage);
							}
							// Function for bed-allocation
							$scope.bedAllocation = function() {
								//console.log('$scope.itemSelecteds',$scope.itemSelecteds);
								
								console.log("OLDDAO",$scope.itemSelecteds.oldDoa);
								console.log("CURENCHA",moment(
										$scope.itemSelecteds.datepicker)
										.format('MM/DD/YYYY'));
								//return false;
								var todayDate = new Date();
								todayDate = moment(todayDate).format(
										'DD/MM/YYYY');
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								} else {
									
									var cuD = moment($scope.itemSelecteds.datepicker).format('MM/DD/YYYY')
									if($scope.itemSelecteds.oldDoa != cuD){
										growl
										.error(
												'If you want change date of admission, first change and save it and allocate bed..!',
												{
													title : 'Error!'
												});
										return false;
									}
									else if (todayDate == moment(
											$scope.itemSelecteds.datepicker)
											.format('DD/MM/YYYY')) {
										
										if($scope.itemSelecteds.registrationTypeId == 5 || $scope.itemSelecteds.registrationTypeId == 4)
										{
										      $scope.itemSelecteds.fromAdmission = true;
											 $state.go("patientRegistration", {
                                              patientRegistrationObj:$scope.itemSelecteds,
                                             	});
										}
										else {
										var URI = BASE_URL + ROOT_URL
										+ "/adt/isPatientAlreadyAdmitted/"+$scope.itemSelecteds.patientId+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, "")
										.then(
												function(response) {
												//console.log(response.data);
													//return false;
													if (response.data == true) {
														
														
														if($scope.itemSelecteds.visitTypeId == 4){
															growl
															.warning(
																	'Please initiate the discharge request for selected ER admission...!!',
																	{
																		title : 'Warning!'
																	});
															return false;
														}else if($scope.itemSelecteds.visitTypeId == 3){
															growl
															.warning(
																	'Please initiate the discharge request for selected Day Care admission...!!',
																	{
																		title : 'Warning!'
																	});
															return false;
														}else{
														growl
														.warning(
																'Patient Already Admitted...!!',
																{
																	title : 'Warning!'
																});
														return false;
														}
													} else {
														$scope.itemSelecteds.fromPendingAdmissions = 1;
														$state.go("admission", 
														{
															admissionObj: $scope.itemSelecteds,
														});
													}
												});
									    }
									} else {
										growl
												.error(
														'Patient Date of Admission and Today Date Should be Same!!',
														{
															title : 'Error!'
														});
									}
								}
							}

							// Function For bed reserve popup values
							$scope.selectedPatientId = "";
							$scope.bedReserved = function() {
								console.log($scope.itemSelecteds);
								$scope.selectedPatientId = $scope.itemSelecteds.patientId;
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								} else {
									
									////
									var URI = BASE_URL + ROOT_URL
									+ "/adt/isPatientAlreadyAdmitted/"+$scope.itemSelecteds.patientId+"/"+$scope.organizationId+"/"+$scope.unitId;
							GenericService
									.serviceAction("GET", URI, "")
									.then(
											function(response) {
											//console.log(response.data);
												//return false;
												if (response.data == true) {
													growl
													.warning(
															'Patient Already Admitted...!!',
															{
																title : 'Error!'
															});
													return false;
												} else {
													console.log($scope.itemSelecteds);
													var age = $rootScope
															.getAge($scope.itemSelecteds.dob);
													$scope.patientFullName = $scope.itemSelecteds.patientFullName
															+ " "
															+ age
															+ "/"
															+ $scope.itemSelecteds.genderCode;
													$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
													$scope.doctorFullName = 'Dr. '
															+ $scope.itemSelecteds.doctorFullName;
													$scope.patientCategoryDesc = $scope.itemSelecteds.patientCategoryDesc;
													$scope.paymentEntitlementDesc = $scope.itemSelecteds.paymentEntitlementDesc;
													$scope.bedCategoryId = $scope.itemSelecteds.bedCategoryId;
													$scope.paymentEntitlementId = $scope.itemSelecteds.paymentEntitlementId;
													$scope.dateOfAdmission = moment($scope.itemSelecteds.datepicker).format('DD/MM/YYYY');
													$scope.patientDischargeDate = moment($scope.itemSelecteds.datepickerPdd).format('DD/MM/YYYY');
													$scope.genderId = $scope.itemSelecteds.genderId;
													$scope.imagePath = $scope.itemSelecteds.imagePath;
													angular.element(
													'#reservedBed')
													.modal('show');
													$scope.openDocument();
												}
											});
									
									
									
									////
									
								}
							}
						
	                $scope.getDepositMatrixList = function()
							{
								$('.deposit-matrix-bed-reservation').show();
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing/unit/depositMatrixConfig";
								
								
								//var billingBedCategoryIdObj  = $scope.bedCategoryList.find( obj => obj.bedCategoryId ==$scope.bedCategoryId);
								
								if($scope.itemSelecteds.visitTypeId != 3)
									{
									var billingBedCategoryIdObj = $.grep($scope.bedCategoryList, function (obj) {
							   	           return obj.bedCategoryId== $scope.bedCategoryId;
							   	       })[0];
									}else{
										
										var billingBedCategoryIdObj = $.grep($scope.dayCareBedCategoryList, function (obj) {
								   	           return obj.bedCategoryId== $scope.bedCategoryId;
								   	       })[0];
									}
								
								
								var param = 
								    {
										  patientTypeId : $scope.itemSelecteds.patientCategoryId,
										  paymentEntitlementId : $scope.paymentEntitlementId,
										  bedCategoryId : billingBedCategoryIdObj.billingBedCategoryId,
										  organsationId : $scope.organizationId,
										  unitId : $scope.unitId,
										  sergicalOrMedical : 2,
										  againstDeposit : 1//for Reservation
									};
								console.log('param',JSON.stringify(param));
								return GenericService.serviceAction(METHOD_POST,URI,param).then(function(response) {
									if (response.data.status == "success"){
										$scope.depositMatrixList = response.data.listObject;
										$rootScope.stopLoader();
									}
									else if(response.data.status == "error")
										{
										  $rootScope.stopLoader();
										}
									return response.data.listObject;
								});
							}
	                
	                
	                $scope.checkFileUPloadValidaton = function()
	                {
	                	var flag = true;
	                	var count = 1;
	                	angular.forEach($scope.encounterDetailsList, function(value, key) 
								{
	                		            
										if(value.documentTypeId=='' && angular.equals({},value.file))
											{
											  if(count>1)
												  {
												  flag = false;
												  } 
											  else{
												  count++;
												  flag = true;
											    }
											  
											}
										else if(value.documentTypeId=='' && !angular.equals({},value.file))
											{
											  flag = false;
											}
										else if(value.documentTypeId!='' && angular.equals({},value.file))
											{
											 flag = false;
											}
								});
	                	return flag;
	                }

							// Function For Save bed reservetion
	            	    
							$scope.savebedReservation = function() {
								
								console.log(""+$scope.itemSelecteds.uhidnumber);
								$scope.todaydate = new Date();
								$scope.todayDate = moment(new Date())
										.format('MM/DD/YYYY');
								var data = {
										patientId:$scope.itemSelecteds.patientId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										createdDate :moment($scope.itemSelecteds.datepicker).format('DD/MM/YYYY')
									};								
								$scope.bedAllocatedList=[];
								//$scope.checkBedAllocation=function(data){
									var URI = BASE_URL + ROOT_URL
									+ "/adt/getcheckBedAllocation";
									GenericService.serviceAction("POST", URI, data).then(
											function(response) {
												$scope.bedAllocatedList = response.data.listObject;
												if (response.data.status == "success") {
													console.log("bedAllocatedList",$scope.bedAllocatedList);
													 if($scope.bedAllocatedList.length > 0)
													 {
													 growl.warning('This patient already has a reservation for selected date...!',
																{
																	title : 'Warning!'
																});
													 $rootScope.stopLoader();
													   return false;
													 }
												} else {
//													alert("Error!!");
												}
										
								//}
									
						 if($scope.bedAllocatedList.length == 0){
								$rootScope.startLoader();
								$scope.depositAmount = 0;
								$scope.depositPending = 0;
								$scope.depositCollected = 0;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								var dateOfPdd = moment(
										$scope.itemSelecteds.datepickerPdd)
										.format('DD-MM-YYYY HH:mm:ss');
								var dateOfAdmission = moment(
										$scope.itemSelecteds.datepicker)
										.format('DD-MM-YYYY HH:mm:ss');
								 //return false;
								
								 var flag = $scope.checkFileUPloadValidaton();
								 if(!flag)
									 {
									 growl.error('Please select valid Data on file Upload.',
												{
													title : 'Error!'
												});
									 $rootScope.stopLoader();
									   return false;
									 }
								if( $scope.itemSelecteds.depositStatus==1 ||  $scope.itemSelecteds.depositStatus==2||  $scope.itemSelecteds.depositStatus==3)
									{
									   var promise = $scope.getDepositMatrixList();
									   promise.then(function(depositMatrixList) 
												{
												  if(depositMatrixList != null && depositMatrixList.length > 0)
													  {
													    $scope.depositPending = depositMatrixList[0].reservationDeposit;
													    $scope.depositAmount = depositMatrixList[0].reservationDeposit;
						                                $scope.depositCollected = 0;
						                                var data = 
						                                {
						    									admissionNoteId : $scope.itemSelecteds.admissionNoteId,
						    									bedCategoryId : $scope.bedCategoryId,
						    									paymentEntitlementId : $scope.paymentEntitlementId,
						    									createdBy : $scope.userId,
						    									organizationId : $scope.organizationId,
						    									patientCategoryId : $scope.itemSelecteds.patientCategoryId,
						    									doa : dateOfAdmission,
						    									pdd : dateOfPdd,
						    									updatedBy : $scope.userId,
						    									updatedDate : $scope.currentDate,
						    									createdDate : $scope.currentDate,
						    									depositAmount : $scope.depositAmount,
						    									depositStatus :$scope.itemSelecteds.depositStatus,
						    									depositPending:$scope.depositPending,
						    									depositCollected:$scope.depositCollected
						    									
						    								};
						    								console.log(data);

						    								var URI = BASE_URL + ROOT_URL
						    										+ UPDATERESERVATIONADMISSION;
						    								GenericService
						    										.serviceAction("POST", URI, data)
						    										.then(
						    												function(response) {

						    													if (response.data.status == "success") 
						    													{
						    														
						    														if($scope.encounterDetailsList.length>0)
						    															{
						    															   if($scope.encounterDetailsList.length==1)
						    																   {
						    																      if($scope.encounterDetailsList[0].documentTypeId!=''||
						    																    		  $scope.encounterDetailsList[0].documentTypeId!=null )
						    																    	  {
						    																    	   	$scope.uploadDoc();
						    																    	  }
						    																   }
						    															   else{
						    																   $scope.uploadDoc();
						    															   }
						    															  
						    															}
						    														$rootScope.stopLoader();
						    														growl
						    																.success(
						    																		'Added successfully...!!',
						    																		{
						    																			title : 'Success!'
						    																		});
						    														$scope.init();
						    														angular.element(
						    																'#reservedBed')
						    																.modal('hide');
						    													} else {
						    														growl
						    																.error(
						    																		'Something went wrong...!!',
						    																		{
						    																			title : 'Error!'
						    																		});
						    													}
						    													$scope
						    															.initPendingAdmissionList(
						    																	$scope.organizationId,
						    																	$scope.offset,
						    																	$scope.noOfRecordsPerPage);

						    												});
													  }
												  else{
													  var data = 
						                                {
						    									admissionNoteId : $scope.itemSelecteds.admissionNoteId,
						    									bedCategoryId : $scope.bedCategoryId,
						    									paymentEntitlementId : $scope.paymentEntitlementId,
						    									createdBy : $scope.userId,
						    									organizationId : $scope.organizationId,
						    									patientCategoryId : $scope.itemSelecteds.patientCategoryId,
						    									doa : dateOfAdmission,
						    									pdd : dateOfPdd,
						    									updatedBy : $scope.userId,
						    									updatedDate : $scope.currentDate,
						    									createdDate : $scope.currentDate,
						    									depositAmount : 0,
						    									depositStatus : 3,
						    									depositPending: 0,
						    									depositCollected:0
						    									
						    								};
						    								console.log(data);

						    								var URI = BASE_URL + ROOT_URL
						    										+ UPDATERESERVATIONADMISSION;
						    								GenericService
						    										.serviceAction("POST", URI, data)
						    										.then(
						    												function(response) {

						    													if (response.data.status == "success") 
						    													{
						    														
						    														if($scope.encounterDetailsList.length>0)
						    															{
						    															   if($scope.encounterDetailsList.length==1)
						    																   {
						    																      if($scope.encounterDetailsList[0].documentTypeId!=''||
						    																    		  $scope.encounterDetailsList[0].documentTypeId!=null )
						    																    	  {
						    																    	   	$scope.uploadDoc();
						    																    	  }
						    																   }
						    															   else{
						    																   $scope.uploadDoc();
						    															   }
						    															  
						    															}
						    														$rootScope.stopLoader();
						    														growl
						    																.success(
						    																		'Added Successfully...!!',
						    																		{
						    																			title : 'Success!'
						    																		});
						    														$scope.init();
						    														angular.element(
						    																'#reservedBed')
						    																.modal('hide');
						    													} else {
						    														growl
						    																.error(
						    																		'Something went wrong...!!',
						    																		{
						    																			title : 'Error!'
						    																		});
						    													}
						    													$scope
						    															.initPendingAdmissionList(
						    																	$scope.organizationId,
						    																	$scope.offset,
						    																	$scope.noOfRecordsPerPage);

						    												});
												   }
											    });
									}
								else
									{
									  $rootScope.stopLoader();
									}
								  
                                
									 }
											});
							}

							$scope.dataList = [ {
								documentTypeId : '',
								documentPath : ''
							} ];

							$scope.add = function() {
								var data = {
									documentTypeId : '',
									documentPath : ''
								};
								$scope.dataList.push(data);
							};

							$scope.remove = function() {
								var newDataList = [];
								var le = $scope.dataList.length;
								var i = 1;
								angular.forEach($scope.dataList, function(v) {
									if (le != i) {
										newDataList.push(v);
									}
									i++;
								});
								$scope.dataList = newDataList;
							};

							// Function For Save Document
							$scope.saveDocuments = function() {

								console.log($scope.dataList);
								angular.forEach($scope.dataList, function(
										value, index) {

									var fd = new FormData();
									fd.append('file', value.documentPath);
									value.documentPath = fd;
								});
								var data = {
									admissionNoteId : $scope.itemSelecteds.admissionNoteId,
									createdBy : $scope.userId,
									organizationId : $scope.organizationId,
									updatedBy : $scope.userId,
									documentArray : $scope.dataList,
									unitId : $scope.unitId
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UPLOADPATIENTDOCUMENTS;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Document uploaded  Successfully...!!',
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
							
							$scope.checkDateDoa = function(index){
								
								console.log($scope.pendingAdmissionList[index].datepicker);
								//if($scope.pendingAdmissionList[index].datepicker == undefined)
									 if(!angular.isDate($scope.pendingAdmissionList[index].datepicker))
								{
									growl.warning("Doa is invalid",{
										title : WARNING_MSG
									});
									$scope.pendingAdmissionList[index].datepicker = new Date($scope.pendingAdmissionList[index].oldDoa);
									return false;
								}
							}
							$scope.checkDatePdd = function(index){
								
								console.log($scope.pendingAdmissionList[index].datepickerPdd);
								//if($scope.pendingAdmissionList[index].datepickerPdd == undefined)
								if(!angular.isDate($scope.pendingAdmissionList[index].datepickerPdd))
									{
										growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
										$scope.pendingAdmissionList[index].datepickerPdd = new Date($scope.pendingAdmissionList[index].oldPdd);
										return false;
									}
							}
							
							$scope.confirmationPopUp= function(info){
								$scope.cancelAdmissionNoteId = info.admissionNoteId;
								angular.element('#confirmationPopUp').modal('show');
							}
							
							$scope.getRejectionDetails= function(info){
								$scope.requestRejectionReason = "";
								$scope.requestRejectionNote = "";
								
								$scope.requestRejectionReason = info.requestRejectionReason;
								$scope.requestRejectionNote = info.requestRejectionNote;
								
								//angular.element('#rejectDetails').modal('show');
							}
							
							
							$scope.cancelRequest= function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/adt/cancelPendingAdmissionRequest";
								var param = { 
										organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												updatedBy : $scope.userId,
												admissionNoteId : $scope.cancelAdmissionNoteId,
											};
								console.log(param);
								BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success"){
										$scope.cancelAdmissionNoteId=null;
										$scope.initPendingAdmissionList(
												$scope.organizationId,
												$scope.offset,
												$scope.noOfRecordsPerPage);
									}
									$rootScope.stopLoader();
								});
								
							}
							
							
							/**===========Deposit Collection On Reservation========================*/
							
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							$scope.addBtnId=false;
							$scope.pinpadAmounFlag =false;
							$scope.setPaymentModeId = function(id)
							{
								if(id==3 && $scope.isPinPadRequired =='Y'){
									$scope.addBtnId=true;
									 $scope.pinpadAmounFlag =true;
								}else if(id==6){
									$scope.addBtnId=true;
								}
								else{
									$scope.addBtnId=false;
									 $scope.pinpadAmounFlag =false;
								}
								$scope.paymentDetails.paymentModeId = id;
							}
							
							$scope.bedCat = function(test){
								
								if(test == ""){
									growl.warning("Please Select Valid Bed Category.",{
										title : WARNING_MSG
									});
									return;
								}
							}
							
							$scope.collectPayment = function(depositMatrixObj)
							{
								$scope.setFirstTabLevel(1);
						        $scope.setPaymentModeId(1);
								$scope.send=false;
								$scope.resend=false;	
								$scope.paymentDetails.otp ="";
								$scope.validatTextFieldFlag = true;
								$('#collectPaymentModalReservation').modal('show');
								$scope.tatalAmount = 0;
								$scope.deposit.depositPending = depositMatrixObj.reservationDeposit;
								$scope.paymentDetails.cashAmount = depositMatrixObj.reservationDeposit;
								loyaltiCardCallFactory.getLoyaltyCardList($scope.selectedPatientId,$scope);
								$scope.calculateDueAmount();
								$scope.calculateTotalAmount();
							}
						
							
							
							$scope.getBankList = function(){
								var URI = BASE_URL + ROOT_URL + "/api/billing/bankMaster";
								var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
								BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.bankList = response.data.listObject;
									}
								});
							}
							
							//ADDED LATER GANESH
							$scope.getHospitalBankList = function()
							{
								var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							       {
									if (response.data.status == "success")
									{
										$scope.hospitalBankList = response.data.listObject;
									}
								  });
							}
							
							$scope.getCardTypesList = function(){
								var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
								var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
								BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.cardTypeList = response.data.listObject;
									}
								});
							}
							
							$scope.initPaymentDetailsObj = function(){
								$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:""});
							}
							
							$scope.initDepositObj = function(){
								$scope.deposit = angular.copy({depositTypeId:"",visitTypeId:"",depositAmount:"",depositPending:"",remarks:"",receivedFrom:"",identificationNo:"",patientId:"",encounterId:"",admNoteId:""})
							}
							
							$scope.initDepositSearchObj = function(){
								$scope.depositSearch = angular.copy({fromDate:"",toDate:"",patientName:"",uhidNumber:"",visitTypeId:"",depositTypeId:""});
							}
							
							$scope.initDepositSearchDtls = function(){
								$scope.depositSearchDtls = angular.copy({totalDepositAmount:parseFloat(0).toFixed(2),totalConsumedDepositAmount:parseFloat(0).toFixed(2),totalBalanceAmount:parseFloat(0).toFixed(2)});
							}
							
							
							
							$scope.addPaymentDetails = function(){
								
								//check amount collected is greater tha deposit amount
								var depositAmount = $scope.deposit.depositPending != "" && typeof($scope.deposit.depositPending) != "undefined" ? parseFloat($scope.deposit.depositPending) : parseFloat(0) ;
								var collectedAmount = 0;
								var tempAmount = 0;
								
								switch($scope.paymentDetails.paymentModeId){
								case 1 :
									tempAmount = $scope.paymentDetails.cashAmount != '' && typeof($scope.paymentDetails.cashAmount) != 'undefined' ? parseFloat($scope.paymentDetails.cashAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.cashAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									
									var cashLimit = 0;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										
										if(value.paymentModeId == 1){
											cashLimit = parseFloat(cashLimit) + parseFloat(value.amount);
										}
									});
									cashLimit = cashLimit + parseFloat(tempAmount);
									if( ($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null ) && cashLimit > $scope.cashAmountLimit ){
										growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
											title : WARNING_MSG
										});
										return;
									}
									
										break;
								case 2 :
									
									if($scope.paymentDetails.bankId==null||$scope.paymentDetails.bankId==''){
										growl.warning("Please select bank.",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.hospitalAccountId==null||$scope.paymentDetails.hospitalAccountId==''){
										growl.warning("Please select hospital account..",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.chequeNumber==null||$scope.paymentDetails.chequeNumber==''){
										growl.warning("Please enter cheque number..",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.chequeDate==null||$scope.paymentDetails.chequeDate==undefined||$scope.paymentDetails.chequeDate==''){
										growl.warning("Please enter cheque date..",{
											title : WARNING_MSG
										});
										return;
									}
									
									tempAmount = $scope.paymentDetails.chequeAmount != '' && typeof($scope.paymentDetails.chequeAmount) != 'undefined' ? parseFloat($scope.paymentDetails.chequeAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.chequeAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									break;
									

									var chequeNo = false;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										if(value.paymentModeId == 2 && value.chequeNumber == $scope.paymentDetails.chequeNumber){
											chequeNo = true;
										}
									});
									if(chequeNo){
										growl.warning("this cheque number already  added",{
											title : WARNING_MSG
										});
										return;
									}
								
								case 3 :
									
								/*	if($scope.paymentDetails.credCardHolderName==null||$scope.paymentDetails.credCardHolderName==''){
										growl.warning("Please enter card holder name.",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.credCardTypeId==null||$scope.paymentDetails.credCardTypeId==''){
										growl.warning("Please select card type..",{
											title : WARNING_MSG
										});
										return;
									}*/
									if($scope.paymentDetails.credCardNumber==null||$scope.paymentDetails.credCardNumber==''){
										growl.warning("Please enter card number..",{
											title : WARNING_MSG
										});
										return;
									}
									
									if(typeof($scope.paymentDetails.transactionNo) == "undefined" || $scope.paymentDetails.transactionNo == null || 
											$scope.paymentDetails.transactionNo == "" ){
											growl.error("Please enter transaction no",{
												title : ERROR_MSG
											});
											return;
										}
									/*if($scope.paymentDetails.credExpiryDate==null||$scope.paymentDetails.credExpiryDate==undefined||$scope.paymentDetails.credExpiryDate==''){
										growl.warning("Please enter expiry date..",{
											title : WARNING_MSG
										});
										return;
									}*/
									tempAmount = $scope.paymentDetails.credAmount != '' && typeof($scope.paymentDetails.credAmount) != 'undefined' ? parseFloat($scope.paymentDetails.credAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.credAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									break;
								
								case 4 :
									tempAmount = $scope.paymentDetails.debAmount != '' && typeof($scope.paymentDetails.debAmount) != 'undefined' ? parseFloat($scope.paymentDetails.debAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.debAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									break;
									
								case 5 :
									
									tempAmount = $scope.mpesaAmount != '' && typeof($scope.mpesaAmount) != 'undefined' ? parseFloat($scope.mpesaAmount) : parseFloat(0);
									
									if(parseFloat($scope.mpesaAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									var invalidMpesa = false;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										if($scope.mpesaTrxNo = value.cardNo)
										{
											growl.warning("Collected MPesa is already used in current tranasction",{
												title : WARNING_MSG
											});
											invalidMpesa = true;
										}
									});
									if(invalidMpesa == true)
										return;
									
									break;
									
								case 8:
									
									tempAmount = $scope.eTransferObj.transferAmount != '' && typeof($scope.eTransferObj.transferAmount) != 'undefined' ? parseFloat($scope.eTransferObj.transferAmount) : parseFloat(0);
									
									if(parseFloat($scope.eTransferObj.transferAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									
									if($scope.eTransferObj.transactionobj.id == ""){
										growl.warning("Please Select Transaction Type",{
											title : WARNING_MSG
										});
										return;	
									}
									if($scope.eTransferObj.transferAmount == 0 || $scope.eTransferObj.transferAmount == null){
										growl.warning("Amount cannot be zero",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.eTransferObj.transBankId == "" || $scope.eTransferObj.transBankId == "-"){
										growl.warning("Please Select Bank",{
											title : WARNING_MSG
										});
										return;	
									}
									if($scope.eTransferObj.transactionNo == "" || $scope.eTransferObj.transactionNo == undefined){
										growl.warning("Please Enter Transaction Number",{
											title : WARNING_MSG
										});
										return;	
									}
									if($scope.eTransferObj.transTransactionDate == ""){
										growl.warning("Please Select Transaction Date",{
											title : WARNING_MSG
										});
										return;	
									}
									var invalidETransfer = false;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										if(value.paymentModeId == 8 && $scope.eTransferObj.transactionNo == value.transactionNo)
										{
											growl.warning("Collected E-Transfer is already used in current tranasction",{
												title : WARNING_MSG
											});
											invalidETransfer = true;
										}
									});
									if(invalidETransfer == true)
										return;
									break;
									
								case 6 :
									
									tempAmount = $scope.paymentDetails.loyaltyCardAmt != '' && typeof($scope.paymentDetails.loyaltyCardAmt) != 'undefined' ? parseFloat($scope.paymentDetails.loyaltyCardAmt) : parseFloat(0);
									if(typeof($scope.paymentDetails.loyaltyCardAmt) == "undefined" || $scope.paymentDetails.loyaltyCardAmt == ""){
										growl.error("Please insert an amount",{
											title : ERROR_MSG
										});
										return;
									}
									
										if(parseFloat($scope.paymentDetails.loyaltyCardAmt) > parseFloat($scope.paymentDetails.cardBalAmount))
										{
											growl.error("Entered Amount Should be less than Card Amount.",{
												title : ERROR_MSG
											});
											return 
										 }
										break;
								}
								
								if(tempAmount == 0){
									growl.warning("Amount cannot be zero",{
										title : WARNING_MSG
									});
									return;
								}
								
								angular.forEach($scope.listPaymentDetails,function(value,key){
									collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
								});
								collectedAmount = collectedAmount + tempAmount;
								if(collectedAmount > depositAmount){
									growl.warning("Entered amount should be equal to or less than deposit amount",{
										title : WARNING_MSG
									});
									return;
								}
								
								switch($scope.paymentDetails.paymentModeId){
									case 1 :
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
									case 2 :
										var bankName = "";
										//Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.paymentDetails.bankId == value.bankId)
												bankName = value.bankName;
										})
										var chequeDate = $scope.getFormattedDate($scope.paymentDetails.chequeDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.chequeAmount).toFixed(2),createdBy:$scope.userId, bankId:$scope.paymentDetails.bankId, chequeNumber:$scope.paymentDetails.chequeNumber, chequeDate:chequeDate, bankName:bankName,hospitalAccountId:$scope.paymentDetails.hospitalAccountId};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
									case 3 :
										var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.credExpiryDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:$scope.paymentDetails.transactionNo};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
									case 4 :
										var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.debExpiryDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.debAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.debCardHolderName,cardTypeId:$scope.paymentDetails.debCardTypeId,cardNo:$scope.paymentDetails.debCardNumber,expiryDate:expiryDate};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
									case 5:				
										var paymentDetails = { paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaAmount).toFixed(2), createdBy:$scope.userId, cardNo:$scope.transactionId,transactionNo:$scope.transactionId};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
										
									case 8 : 
										var bankName = "";
										//Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.eTransferObj.transBankId == value.bankId)
												bankName = value.bankName;
										})
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.eTransferObj.transferAmount).toFixed(2), transactionType:$scope.eTransferObj.transactionobj.name, transactionTypeId:$scope.eTransferObj.transactionobj.id,transTransactionDate:moment($scope.eTransferObj.transTransactionDate).format('DD-MM-YYYY HH:mm:ss'),bankId:$scope.eTransferObj.transBankId,transactionNo:$scope.eTransferObj.transactionNo,createdBy:$scope.userId,bankName:bankName};
										   $scope.listPaymentDetails.push(paymentDetails);
										break;
										
									case 6:				
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.loyaltyCardAmt).toFixed(2),cardNo:$scope.paymentDetails.loyaltyCardNo,createdBy:$scope.userId};
										$scope.listPaymentDetails.push(paymentDetails);
										 $scope.send=false;
										 $scope.resend=false;
										 $scope.validatTextFieldFlag = true;
										break;
								}
								$scope.initPaymentDetailsObj();
								$scope.setFirstTabLevel(1);
						        $scope.setPaymentModeId(1);
								$scope.calculateTotalAmount();
								$scope.calculateDueAmount();
								
							}
							
							$scope.calculateDueAmount = function(){
								//calculate due amount
								var collectedAmount = parseFloat(0).toFixed();
								angular.forEach($scope.listPaymentDetails,function(value,key){
									collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
								});
								$scope.dueAmount = $scope.deposit.depositPending != '' && typeof($scope.deposit.depositPending) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositPending) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
								$scope.paymentDetails.cashAmount = $scope.dueAmount;
								$scope.paymentDetails.chequeAmount = $scope.dueAmount;
								$scope.paymentDetails.debAmount = $scope.dueAmount;
								$scope.paymentDetails.credAmount = $scope.dueAmount;
								$scope.eTransferObj.transferAmount = $scope.dueAmount;
								$scope.paymentDetails.loyaltyCardAmt =  $scope.dueAmount;
							}
							$scope.calculateTotalAmount = function(){
								//calculate due amount
								var tatalAmount = parseFloat(0).toFixed();
								angular.forEach($scope.listPaymentDetails,function(value,key){
									tatalAmount = parseFloat(tatalAmount) + parseFloat(value.amount);
								});
								$scope.tatalAmount = tatalAmount;
								$scope.deposit.depositAmount  = $scope.tatalAmount;
							}
							
							
							//remove payment details
							$scope.removePaymentDetails = function(index){
								var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
								if(tempObj!=null && (tempObj.cardNo == $scope.paymentDetails.loyaltyCardNo))
									{
									 $scope.paymentDetails.cardBalAmount = $scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
									 $scope.send=false;
									 $scope.resend=false;
									}
								$scope.listPaymentDetails.splice(index,1);
								$scope.calculateDueAmount();
								$scope.calculateTotalAmount();
							}
							
							$scope.parseFloatNum = function(number){
								return (number != '' && typeof(number) != 'undefined' && !isNaN(number)) ? parseFloat(number).toFixed(2) : parseFloat(0).toFixed(2);
							}
							$scope.disabledSettle = false;
							$scope.addDeposit = function()
							{
							if(parseFloat($scope.tatalAmount) == 0){
									growl.error("Amount should not be 0.",{
										title : ERROR_MSG
									});
									return;
								}
							
								if($scope.disabledSettle == true){
									return false;
								}
								$scope.disabledSettle = true;
								$rootScope.startLoader();
								$('#collectPaymentModalReservation').modal('hide');
								$scope.deposit.patientId = $scope.itemSelecteds.patientId;
								$scope.deposit.visitTypeId = $scope.itemSelecteds.visitTypeId;
								$scope.deposit.createdBy = $scope.userId;
								$scope.deposit.depositTypeId = 3//for reservation;
								$scope.deposit.admNoteId = $scope.itemSelecteds.admissionNoteId;
								$scope.deposit.orgId = $scope.organizationId;
								$scope.deposit.unitId = $scope.unitId; 
								if($scope.deposit.depositTypeId == 1)
									$scope.deposit.payeeId = 1;
								var param = {depositMasterDto:$scope.deposit,listPaymentDepositDetailsDto:$scope.listPaymentDetails};
								var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit";
								BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success"){
										$scope.itemSelecteds.depositStatus = response.data.listObject==null?1:response.data.listObject[0];
										var depositMasterId = response.data.object;
										$scope.send = false;
										$scope.disabledSettle = false;
										$scope.validatTextFieldFlag = true;
										$scope.initPaymentDetailsObj();
										$scope.listPaymentDetails = [];
										$scope.initDepositObj();
										$scope.patientDetailsList = [];
										$scope.patientDetails = {};
										$scope.dueAmount = parseFloat(0).toFixed(2);
										var data =  {
								                 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "DepositSlip.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "DepositSlip",
								                 "reportParams": [
								                   {
								                  "paramName": "@deposit_master_id",
								                  "paramType": "Integer",
								                  "paramValue": depositMasterId
								                }
								                 ]
								      };
										reportService.generate(REPORT_API_URI1,data);
										
									}
									else if (response.data.status == "error"){
										$scope.disabledSettle = false;
										$rootScope.stopLoader();
									}
									
								});
							}
							
							$scope.getFormattedDate = function(date,isTime){
								var tempDate = new Date(date);
								var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
								var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
								var year = tempDate.getFullYear();
								if(isTime)
									return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+getSeconds();
								else
									return date + "-" + month + "-" + year;
							}
							$scope.initDeposit = function()
							{
								/**Init BedReservation Deposit*/
								$scope.deposit = {};
								$scope.initDepositObj();
								$scope.paymentDetails = {};
								$scope.initPaymentDetailsObj();
								$scope.listPaymentDetails = [];
								$scope.getBankList();
								$scope.getHospitalBankList();//ADDED LATER GANESH
								$scope.getCardTypesList();
								$scope.dueAmount = parseFloat(0).toFixed(2);	
							}
							$scope.initDeposit();
							
							
 /**========================DOCUMENT UPLOAD =========================================*/
							
							
							
							
							$scope.getDocumentType = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") {
											$scope.documentTypeList = response.data.listObject;
										}
									});
							}
							$scope.getDocumentType();
							
							
							 $scope.getPatientDocumentList = function()
						        {
						         $rootScope.startLoader();
						         var data =
						           {
						           "patientId":$scope.itemSelecteds.patientId,
						           "unitId":$scope.unitId,
						           "organisationId":$scope.organizationId,
						           "userId":$scope.userId,
						           "visitTypeId":0,
						           "admissionId":null,
						           "encounterId":null,
						          }
						         var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
						         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						           $scope.patientDocumentList = [];
						           if (response.data.status == "success") 
						           {
						            $scope.patientDocumentList = response.data.listObject;
						            $rootScope.stopLoader();
						           }
						           else if(response.data.status == "error")
						            {
						            $rootScope.stopLoader();
						            }
						          });
						        }
							
							$scope.encounterDetailsList = [];
							$scope.encounterDetails = {
								  encounterDocId : "",
								  encounterId : "",
								  admissionId:"",
								  visitTypeId:'',
								  patientId : "",
								  documentPath : "",
								  documentTypeId : "",
								  status : 'A',
								  createdDate : "",
								  createdBy : $scope.userId,
								  updatedDate : "",
								  updatedBy : $scope.userId,
								  unitId : $scope.unitId,
								  organisationId : $scope.organizationId,
								  file : {}
								}
							
							$scope.openDocument = function()
							{
								
								$scope.encounterDetails = {
										  encounterDocId : "",
										  encounterId : "",
										  admissionId:"",
										  visitTypeId:$scope.itemSelecteds.visitTypeId,
										  patientId : "",
										  documentPath : "",
										  documentTypeId : "",
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  organisationId : $scope.organizationId,
										  file : {}
										}
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
									$scope.encounterDetailsList =[];
								}
								else
								{
									$scope.encounterDetailsList =[];
									if($scope.itemSelecteds.patientId > 0)
									{
									var age = $rootScope
									.getAge($scope.itemSelecteds.dob);
									$scope.patientFullName = $scope.itemSelecteds.patientFullName
									+ " "
									+age
									+ "/"
									+ $scope.itemSelecteds.genderCode;
									}
								else
									{
									$scope.patientFullName = $scope.itemSelecteds.patientFullName
									+ " "
									+ $scope.itemSelecteds.dob
									+ "/"
									+ $scope.itemSelecteds.genderCode;
									}
									
								
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.doctorFullName =$scope.itemSelecteds.doctorFullName;
								$scope.bedNumber = $scope.itemSelecteds.bedNumber;
								
								$scope.dateOfAdmission = moment($scope.itemSelecteds.datepicker).format('DD/MM/YYYY');
								$scope.patientDischargeDate = moment($scope.itemSelecteds.datepickerPdd).format('DD/MM/YYYY');
									
									$scope.encounterDetailsList =[];
									var encounterDetails = angular.copy($scope.encounterDetails);
									encounterDetails.patientId = $scope.itemSelecteds.patientId;
									encounterDetails.visitTypeId = $scope.itemSelecteds.visitTypeId;
									$scope.encounterDetailsList.push(encounterDetails);
									$scope.getPatientDocumentList();
								}
							}
							
							
							$scope.addImageDetails = function()
							{ 
								var encounterDetails = angular.copy($scope.encounterDetails);
								encounterDetails.patientId = $scope.itemSelecteds.patientId;
								encounterDetails.visitTypeId = $scope.itemSelecteds.visitTypeId;
								$scope.encounterDetailsList.push(encounterDetails);
							};

						  $scope.removeImageDetails = function(deleteIndex)
							{ 
							   $scope.encounterDetailsList.splice(deleteIndex, 1);
						    }		
							
						  $scope.costEstimation = function()
							{ 
							 
							  $state.go("costEstimation", 
									{
									 selectedPatient:$scope.itemSelecteds
									});
						    }
						  
						  
							$scope.uploadEncounterDoc = function(Obj)
							{
								$('#commonUploadDocuments').modal('hide');
								 var fd = new FormData();
							        fd.append('encounterId', 0);
							        fd.append('visitTypeId', Obj.visitTypeId);
							        fd.append('admissionId', 0);
							        fd.append('patientId',Obj.patientId );
							        fd.append('documentTypeId', Obj.documentTypeId);
							        fd.append('createdBy',Obj.createdBy );
							        fd.append('updatedBy', Obj.updatedBy);
							        fd.append('unitId', Obj.unitId);
							        fd.append('organisationId', Obj.organisationId);
							        fd.append('file',Obj.file );
							        fd.append('expenseId', 0);
							        fd.append('grnId',0);
									fd.append('poId',0);
							        fd.append('incidentReportingMapperId',0);
							        fd.append('companyExpenseId',0);
							        fd.append('createdDate',null);
									fd.append('fileEditedName',null);
									fd.append('opbId',0);
									fd.append('vendorId',0);
							        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
							        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										return response;
									});
							}
							 $scope.uploadDoc = function()
							    {
								 $rootScope.startLoader();
								  $scope.count = 0;
								 angular.forEach($scope.encounterDetailsList, function(value, key) 
									{
										var promise = $scope.uploadEncounterDoc(value);
										promise.then(function(response) 
												{
												  if(response.data.status == "success")
													  {
													    $scope.count++;
													    if($scope.encounterDetailsList.length==$scope.count)
														 {
														   growl.success("Document Uploaded Successfully.",
																	{
																		title : 'Success!'
																	});
														   $rootScope.stopLoader();
														 }
												      }
												  else 
													{
														growl.error('Something went wrong.',
																{
																	title : 'Error!'
																});
														 $rootScope.stopLoader();
													}
											    });
											
									});
								
							    }
								
							 
							 
								$scope.openImage = function(index)
		                              { 
										$rootScope.startLoader();
										var imageUrl = $scope.patientDocumentList[index].documentPath;
										var href = $rootScope.currentHrefDoccument();
										$.fancybox.open({
											width: 600, // or whatever
											height: 320,
											href:href+imageUrl,
											type: 'iframe'
										});
		                         $rootScope.stopLoader();
		                        }
	   /**========================DOCUMENT UPLOAD =========================================*/		
								 /* *************************************Mpesa Integration start****************************************** */
								$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
								$scope.validateMpesaDetails = function(mpesaObject){
									console.log("MPesa Object:",mpesaObject);
									
									var data ={
											organizationId :$scope.organizationId,
											unitId : $scope.unitId,
											createdBy : $scope.userId,
											createdDate : moment(new Date()).format("DD-MM-YYYY"),
											updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
											updatedBy:$scope.userId,
											mpesaTxcode:$scope.mpesaObject.transactionId,
											uhid: $scope.selectedUhid,
											mpesaSendermobile:$scope.mpesaObject.mobileNo,
											mpesaAmount:$scope.mpesaObject.mpesaAmount
									};
								
									if($scope.mpesaObject.transactionId!=null && $scope.mpesaObject.mobileNo!=null){
									$rootScope.startLoader();
									var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
									BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										if (response.data.status == "success")
											{
											
												$scope.mpesaObject.mpesaAmount = response.data.object.mpesaAmount;
												$scope.mpesaObject.resTransactionId = response.data.object.mpesaTxcode;
												$scope.addPaymentDetails();
												$rootScope.stopLoader();
											}
										else if(response.data.status == "error"){
											
											growl.error(response.data.message,{
												title : ERROR_MSG
											});
											$scope.mpesaObject.mpesaAmount=null;
											$rootScope.stopLoader();
										}
										
									});
									}else{
										growl.error("Enter both transaction id and mobile number",{
											title : ERROR_MSG
										});
									}
								}
								
								/*$scope.mobileNumber ="";
								$scope.validateMpesaAmount ="";*/
								
								$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
									
									var data ={
											organizationId :$scope.organizationId,
											unitId : $scope.unitId,
											createdBy : $scope.userId,
											createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											uhidNumber: $scope.selectedUhid,
											phoneNumber:parseInt(countryCallingCode+""+mobileNumber),
											partyA:mobileNumber,
											amount:validateMpesaAmount
									};
								
									if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
											(countryCallingCode != undefined && countryCallingCode != null && countryCallingCode != "")){
									$rootScope.startLoader();
									var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
									BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										if (response.data.status == "success")
											{
											
											growl.success(response.data.message,{
												title : SUCCESS_MSG
											});
											$rootScope.stopLoader();
											}
										else if(response.data.status == "error"){
											
											growl.error(response.data.message,{
												title : ERROR_MSG
											});
											$rootScope.stopLoader();
										}
										
									});
									}else{
										growl.error("Please enter mobile code and mobile number.",{
											title : ERROR_MSG
										});
									}
									
								}
								
							$scope.validateSTKPushPayment = function(){
									
									var data ={
											organizationId :$scope.organizationId,
											unitId : $scope.unitId,
											uhidNumber: $scope.selectedUhid
											
									};
									$rootScope.startLoader();
									var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
									BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										if (response.data.status == "success")
											{
											$scope.mpesaObject.mpesaAmount = response.data.object.amount;
											$scope.mpesaObject.transactionId = response.data.object.checkoutRequestId;
											$scope.mpesaObject.resTransactionId = response.data.object.checkoutRequestId;
											$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
											$scope.addPaymentDetails();
											growl.success(response.data.message,{
												title : SUCCESS_MSG
											});
											$rootScope.stopLoader();
											}
										else if(response.data.status == "error"){
											
											growl.error(response.data.message != null ? response.data.message :"Please wait",{
												title : ERROR_MSG
											});
											$rootScope.stopLoader();
										}
										
									});
									
								}
								
								/* *************************************Mpesa Integration END****************************************** */
							
							/***********************************************Loylty card Tab START *****************************************/
							
							
							$scope.isBillList = 0;
							
							
							$scope.checkAmt = function(amt)
							{
								loyaltiCardCallFactory.checkAmt(amt,$scope);
								
							}
							
							$scope.getLoyaltyCardByNo = function(cardNo)
							{
								loyaltiCardCallFactory.getLoyaltyCardByNo(cardNo,$scope);
								
							}
							
							$scope.sendOTP = function(flag)
							{
								loyaltiCardCallFactory.sendOTP(flag,$scope);
							}
							
							$scope.reSendOTP = function(flag)
							{
								loyaltiCardCallFactory.sendOTP(flag,$scope);
								//$scope.resend = (flag=='N')?true:false;
								//$scope.send =false;
							}
							
							$scope.clearLoylty = function(){
								
								$scope.send = false;
								$scope.validatTextFieldFlag = true;
								$scope.paymentDetails.otp ="";
								//$scope.calculateTotalDueRefAmount();
								$scope.calculateDueAmount();
								$scope.calculateTotalAmount();
								loyaltiCardCallFactory.getLoyaltyCardList($scope.selectedPatientId,$scope);
							}
							
							$scope.validateOtp = function()
							{
								
								if($scope.paymentDetails.otp==0||$scope.paymentDetails.otp==''||$scope.paymentDetails.otp==undefined||
										$scope.paymentDetails.otp==null)
									{
									growl.error("Please enter valid OTP",
											{
												title : 'Error!'
											});
									    return false;
									 }
								else{
								$rootScope.startLoader();
									var data = {
											  "patientId" : '',
											  "organisationId" : $scope.organizationId,
											  "unitId" : $scope.unitId,
											  "status" : "A",
											  "isDeleted" : "N",
											  "isUserDefined":"Y",
											  "otp": $scope.paymentDetails.otp,
											  "cardNo":$scope.paymentDetails.loyaltyCardNo,
											  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
											  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt,
											  "authKey":$scope.loyaltyCardObj.authKey
									}
									var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/validateOTP";
									BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
													$scope.loyaltyCardObj.authKey = 0;
													$scope.send=false;
													$scope.resend=false;
													$scope.validatTextFieldFlag = true;
													var paymentObj = angular.copy($scope.paymentDetails);
													$scope.addPaymentDetails();
													$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
													$scope.paymentDetails.cardBalAmount = paymentObj.cardBalAmount - parseFloat(paymentObj.loyaltyCardAmt);
												
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
										}
							
									});
								}
							}
							
							/*$scope.getClassForColor = function(info)
							{
								   if(info.isCriticalOrder==1){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
								   }else if(info.encounterTypeId==6){
									   return 'td-bg-green';
								   }
							}*/
							
							/***********************************************Loylty card Tab END *****************************************/
							/***********************************************E-Transfer Tab START *****************************************/
							$scope.eTransferObj={
									transBankId:"",
									transferAmount:"",
									transTransactionDate:"",
									transactionNo:"",
									transactionobj:{
											id:"",
											name:""
									}	
							}
							
							
							$scope.transactionTypesList=[];
							 $scope.getTransactionTypes = function(){
								var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.transactionTypesList = response.data.listObject;
									}
									
									console.log("transactionTypesList",$scope.transactionTypesList);
								});
							}
							 
							 $scope.getTransactionTypes();
							 $scope.getbedStatus = function()
								{
									var data = {
											organizationId:$scope.organizationId,
											unitId : $scope.unitId
									}
									
									var URI = BASE_URL + ROOT_URL
									+ "/api/adt/bedCategoryAvailabilityCount/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;

								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.bedStatusList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{
														  $scope.bedStatusList =  response.data.listObject;
														}
														
														
												});
							
								}
							 /***********************************************E-Transfer Tab END *****************************************/
							
						} ]);
