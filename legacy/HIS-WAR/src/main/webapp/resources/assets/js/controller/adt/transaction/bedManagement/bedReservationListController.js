'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:bedReservationListController
 * @description # bedReservationListController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'bedReservationListController',
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
						'PagerService',
						'reportService',
						'$location',
						'loyaltiCardCallFactory',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,BillingGenericService,growl,
								$state, PagerService,reportService,$location,loyaltiCardCallFactory) {

							/* init() function for form object create. */
							moment.suppressDeprecationWarnings = true
							$scope.init = function() {

								// for datePicker
								$scope.resend =false;
								$scope.send =false;
								$scope.datepickerForNewDateOfAdmission = {
									opened : false
								};

								$scope.openDatePickerForNewDOA = function() {
									$scope.datepickerForNewDateOfAdmission.opened = true;
								};
								
								$scope.datepickerForNewPDD = {
										opened : false
									};

									$scope.openDatePickerForNewPDD = function() {
										$scope.datepickerForNewPDD.opened = true;
									};

							/*	$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};*/
									
									$scope.dateOptionsTrancs = {
											formatYear : 'yyyy',
											showWeeks : false,
											minDate : new Date()
										};
									
									$scope.CardDateOptions = {
											formatYear : 'yyyy',
											showWeeks : false,
											minDate:new Date()
										};	
								$scope.dateOptionsForNewDateOfAdmission={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()	
								};
								$scope.dateOptionsForNewPDD={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()	
								};
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.mpesaRequired = cookieObject.mpesaRequired;
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
								$scope.pinpadAmounFlag = false;
								$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
								$rootScope.loginpage = true;
								$scope.reserveAdmissionRequest = {}
								$scope.itemSelecteds = {};
								$scope.unReservePopUpFlag = false;
								$scope.unreserveFlag = false;
								$scope.assignContractPopUpFlag = false;
								$scope.insurancePopUpFlag = false;
								$scope.depositeMatrixPopUpFlag = false;
								$scope.btnValue = "Unreserve";
								$scope.cancelFlag = true;
								$scope.canres = "cancel";
								
								

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}

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

								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETBEDRESERVEDLIST; GenericService
								 * .serviceAction("POST", URI, commonObj) .then(
								 * function(response) { $scope.bedReservedList =
								 * [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.bedReservetionList =
								 * response.data.listObject; angular .forEach(
								 * $scope.bedReservetionList, function( value,
								 * index) { var newArr = {};
								 * newArr.admissionNoteId =
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
								 * newArr.dob = value.dob; newArr.datepicker =
								 * value.doa; newArr.datepickerPdd = value.pdd;
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
								 * $scope.bedReservedList .push(newArr); });
								 *  } });
								 */

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

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initBedReservetionList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initBedReservetionList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETBEDRESERVEDLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTBEDRESERVETIONLIST;

								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
								}

								// For Get Bed Reservation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.bedReservedList = [];

													if (response.data.status == "success") {
														$scope.bedReservetionList = response.data.listObject;
														angular
																.forEach(
																		$scope.bedReservetionList,
																		function(
																				value,
																				index) {
																			var newArr = {};
																			newArr.admissionNoteId = value.admissionNoteId;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.specialityName = value.specialityName;
																			newArr.dFirstName = value.dFirstName;
																			newArr.dMiddleName = value.dMiddleName;
																			newArr.dFirstName = value.dFirstName;
																			newArr.dLastName = value.dLastName;
																			newArr.pFirstName = value.pFirstName;
																			newArr.pMiddleName = value.pMiddleName;
																			newArr.pLastName = value.pLastName;
																			newArr.visitTypeName = value.visitTypeName;
																			newArr.isFlexiableDate = value.isFlexiableDate;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.paymentEntitlementId = value.paymentEntitlementId
																					.toString();
																			
																			//newArr.dob = value.dob;
																			newArr.dob = moment(value.dob, 'DD/MM/YYYY').format('MM/DD/YYYY');
																			//newArr.dob = value.dob;
																			newArr.datepicker = value.oldDoa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.bedCategoryDesc = value.bedCategoryDesc;
																			newArr.genderCode = value.genderCode;
																			//newArr.patientCategoryId = value.patientCategoryId.toString();
																			newArr.bedCategoryId = value.bedCategoryId
																					.toString();
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.genderId = value.id;
																			newArr.doctorSpecialityId = value.doctorSpecialityId;
																			newArr.requestToDoctorId = value.requestToDoctorId;
																			newArr.visitTypeId = value.visitTypeId;
																			newArr.patientId = value.patientId;
																			newArr.patientCategoryDesc = value.patientCategory;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
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
																			newArr.tPatientId = value.tPatientId;
																			
																			//newArr.oldDoa = $rootScope.getFormatedDateeShase(value.doa);
																			newArr.oldDoa = value.doa;
																			newArr.oldBedCategoryId = value.bedCategoryId;
																			newArr.bedCategoryWaitingListId = value.bedCategoryWaitingListId;
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
																			newArr.oldPdd = $rootScope.getFormatedDateeShase(value.pdd);
																			newArr.reasonId = value.reasonId;
																			newArr.treatingDoctorSpecialityId =value.treatingDoctorSpecialityId;
																			newArr.treatingDoctorId = value.treatingDoctorId;
																			
																			$scope.bedReservedList
																					.push(newArr);
																		});

													}
												});

								// For Count Bed Reservation List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);

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
								var URI = BASE_URL + ROOT_URL
										+ GETBEDRESERVEDLIST;
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Bed Reservation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.bedReservedList = [];

													if (response.data.status == "success") {
														$scope.bedReservetionList = response.data.listObject;
														angular
																.forEach(
																		$scope.bedReservetionList,
																		function(
																				value,
																				index) {
																			var newArr = {};
																			newArr.admissionNoteId = value.admissionNoteId;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.specialityName = value.specialityName;
																			newArr.dFirstName = value.dFirstName;
																			newArr.dMiddleName = value.dMiddleName;
																			newArr.dFirstName = value.dFirstName;
																			newArr.dLastName = value.dLastName;
																			newArr.pFirstName = value.pFirstName;
																			newArr.pMiddleName = value.pMiddleName;
																			newArr.pLastName = value.pLastName;
																			newArr.visitTypeName = value.visitTypeName;
																			newArr.isFlexiableDate = value.isFlexiableDate;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.paymentEntitlementId = value.paymentEntitlementId.toString();
																			newArr.dob = value.dob;
																			newArr.datepicker = value.oldDoa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.bedCategoryDesc = value.bedCategoryDesc;
																			newArr.genderCode = value.genderCode;
																			newArr.patientCategoryId = value.patientCategoryId
																					.toString();
																			newArr.bedCategoryId = value.bedCategoryId
																					.toString();
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.genderId = value.id;
																			newArr.doctorSpecialityId = value.doctorSpecialityId;
																			newArr.requestToDoctorId = value.requestToDoctorId;
																			newArr.visitTypeId = value.visitTypeId;
																			newArr.patientId = value.patientId;
																			newArr.patientCategoryDesc = value.patientCategory;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
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
																			newArr.tPatientId = value.tPatientId;
																			
																			newArr.oldDoa = $rootScope.getFormatedDateeShase(value.doa);
																			newArr.oldBedCategoryId = value.bedCategoryId;
																			newArr.oldPdd = $rootScope.getFormatedDateeShase(value.pdd);
																			
																			newArr.bedCategoryWaitingListId = value.bedCategoryWaitingListId;
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
																			newArr.reasonId = value.reasonId;
																			newArr.treatingDoctorSpecialityId =value.treatingDoctorSpecialityId;
																			newArr.treatingDoctorId = value.treatingDoctorId;
																			
																			$scope.bedReservedList
																					.push(newArr);
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
							$scope.selectedPetientId="";
							$scope.selectPatientReserve = function(item) {
								console.log(item);
								$scope.selectedUhid = item.uhidnumber;
								$scope.selectedPetientId = item.patientId;
								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.reserveAdmissionRequest) {
									$scope.itemSelecteds = item;
									$scope.unReservePopUpFlag = true;

									$scope.assignContractPopUpFlag = true;
									$scope.insurancePopUpFlag = true;
									$scope.depositeMatrixPopUpFlag = true;
								}
								console.log($scope.itemSelecteds);
								loyaltiCardCallFactory.getLoyaltyCardList(item.patientId,$scope);
							}

							$scope.bedAllocation = function() {
								// alert($scope.itemSelecteds.datepicker);
								var todayDate = new Date();
								//todayDate = $rootScope.getFormatDate(todayDate);
								todayDate = moment(new Date()).format('DD-MM-YYYY');
								/*alert(todayDate);
								alert($rootScope
										.getChangedFormatedDateShalse($scope.itemSelecteds.datepicker));*/
								
								
								// var myObj = $scope.itemSelecteds;
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {
									if (todayDate == $rootScope
											.getChangedFormatedDateShalse($scope.itemSelecteds.datepicker)) {

										/*sessionStorage
												.setItem(
														"admissionNoteObj",
														JSON
																.stringify($scope.itemSelecteds));
										sessionStorage.setItem(
												"bedAllocationFrom",
												"bedReserve");
										$state.go('bedAllocation');*/
										if($scope.itemSelecteds.registrationTypeId == 5)
										{
											 $state.go("patientRegistration", {
												 patientRegistrationObjForAdmission:$scope.itemSelecteds,
												});
										}
										else {
											//$scope.itemSelecteds.oldDoa = moment(moment($scope.itemSelecteds.oldDoa,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
											//$scope.itemSelecteds.oldPdd = moment(moment($scope.itemSelecteds.oldPdd,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
											
											$scope.itemSelecteds.fromPendingAdmissions = 1;
											$scope.itemSelecteds.frombed = 1;
											$scope.itemSelecteds.patientFullName = $scope.itemSelecteds.pFirstName+' '+$scope.itemSelecteds.pLastName;
											$scope.itemSelecteds.doctorFullName = $scope.itemSelecteds.dFirstName+' '+$scope.itemSelecteds.dLastName;
											 var datePartsad = $scope.itemSelecteds.oldDoa.split('/');
			                                 var dateObjectad =  datePartsad[1] + "/" + datePartsad[0] + "/" + datePartsad[2];  
											 $scope.itemSelecteds.oldDoa = dateObjectad;
										$state.go("admission", {
											admissionObj: $scope.itemSelecteds,
										});
									}
									} else {
										growl
												.warning(
														'Patient Date of Admission and Today Date Should be Same...!!',
														{
															title : 'Warning!'
														});
									}
								}
							}

							$scope.unreservetion = function() {
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} 
								else
									{
								console.log($scope.itemSelecteds);
								var age = $rootScope
										.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName = $scope.itemSelecteds.pFirstName
										+ " "
										+ $scope.itemSelecteds.pMiddleName
										+ " "
										+ $scope.itemSelecteds.pLastName
										+ " "
										+ age
										+ "/"
										+ $scope.itemSelecteds.genderCode;
								$scope.genderCode = $scope.itemSelecteds.genderCode;
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.doctorFullName = 'Dr. '
										+ $scope.itemSelecteds.dFirstName + " "
										+ $scope.itemSelecteds.dMiddleName
										+ " " + $scope.itemSelecteds.dLastName;
								$scope.patientCategoryDesc = $scope.itemSelecteds.patientCategoryDesc;
								$scope.paymentEntitlementDesc = $scope.itemSelecteds.paymentEntitlementDesc;
								$scope.bedCategoryId = $scope.itemSelecteds.bedCategoryId;
								$scope.paymentEntitlementId = $scope.itemSelecteds.paymentEntitlementId;
								$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
								var dateTo = $scope.itemSelecteds.datepickerPdd;
								//$scope.patientDischargeDate = moment(dateTo).format('DD/MM/YYYY');
								$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
								$scope.isFlexiableDate = $scope.itemSelecteds.isFlexiableDate;
								$scope.doaActual = $scope.dateOfAdmission; 
	                            $scope.pddActual = $scope.patientDischargeDate;
	                            $scope.admissionNumber = $scope.itemSelecteds.admissionNumber;
	                            $scope.specialityName= $scope.itemSelecteds.specialityName;

								$scope.unReserveModel = {
									admissionReasonId : 0,
									cancelReasonId : 0,
									unreserveReasonId : 0,
									paymentEntitlementId : $scope.itemSelecteds.paymentEntitlementId,
									patientCategoryId : $scope.itemSelecteds.patientCategoryId,
									admissionNoteId : $scope.itemSelecteds.admissionNoteId,
									doa : $scope.dateOfAdmission + ' 00:00:00',
									isFlexiableDateUN : $scope.isFlexiableDate,
									datepickerForNewDateOfAdmission : new Date()
								};
								
							}
							}
							
							$scope.depositeMatrix = function() {
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {
								console.log($scope.itemSelecteds);
								var age = $rootScope
										.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName = $scope.itemSelecteds.pFirstName
										+ " "
										+ $scope.itemSelecteds.pMiddleName
										+ " "
										+ $scope.itemSelecteds.pLastName
										+ " "
										+ age
										+ "/"
										+ $scope.itemSelecteds.genderCode;
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.doctorFullName = 'Dr. '
										+ $scope.itemSelecteds.dFirstName + " "
										+ $scope.itemSelecteds.dMiddleName
										+ " " + $scope.itemSelecteds.dLastName;
								$scope.patientCategoryDesc = $scope.itemSelecteds.patientCategoryDesc;
								$scope.paymentEntitlementDesc = $scope.itemSelecteds.paymentEntitlementDesc;
								$scope.bedCategoryId = $scope.itemSelecteds.bedCategoryId;
								$scope.paymentEntitlementId = $scope.itemSelecteds.paymentEntitlementId;
								$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
								$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
								$scope.isFlexiableDate = $scope.itemSelecteds.isFlexiableDate;

								$scope.unReserveModel = {
									admissionReasonId : 0,
									cancelReasonId : 0,
									unreserveReasonId : 0,
									paymentEntitlementId : $scope.itemSelecteds.paymentEntitlementId,
									patientCategoryId : $scope.itemSelecteds.patientCategoryId,
									admissionNoteId : $scope.itemSelecteds.admissionNoteId,
									doa : $scope.dateOfAdmission + ' 00:00:00',
									isFlexiableDateUN : $scope.isFlexiableDate,
									datepickerForNewDateOfAdmission : '',
									datepickerForNewPDD : ''
								};
							}

							}
							

							$scope.insurance = function() {
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {
								console.log($scope.itemSelecteds);
								var age = $rootScope
										.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName = $scope.itemSelecteds.pFirstName
										+ " "
										+ $scope.itemSelecteds.pMiddleName
										+ " "
										+ $scope.itemSelecteds.pLastName
										+ " "
										+ age
										+ "/"
										+ $scope.itemSelecteds.genderCode;
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.doctorFullName = 'Dr. '
										+ $scope.itemSelecteds.dFirstName + " "
										+ $scope.itemSelecteds.dMiddleName
										+ " " + $scope.itemSelecteds.dLastName;
								$scope.patientCategoryDesc = $scope.itemSelecteds.patientCategoryDesc;
								$scope.paymentEntitlementDesc = $scope.itemSelecteds.paymentEntitlementDesc;
								$scope.bedCategoryId = $scope.itemSelecteds.bedCategoryId;
								$scope.paymentEntitlementId = $scope.itemSelecteds.paymentEntitlementId;
								$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
								$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
								$scope.isFlexiableDate = $scope.itemSelecteds.isFlexiableDate;

								$scope.unReserveModel = {
									admissionReasonId : 0,
									cancelReasonId : 0,
									unreserveReasonId : 0,
									paymentEntitlementId : $scope.itemSelecteds.paymentEntitlementId,
									patientCategoryId : $scope.itemSelecteds.patientCategoryId,
									admissionNoteId : $scope.itemSelecteds.admissionNoteId,
									doa : $scope.dateOfAdmission + ' 00:00:00',
									isFlexiableDateUN : $scope.isFlexiableDate,
									datepickerForNewDateOfAdmission : ''
								};
							}

							}

							$scope.assignContract = function() {
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {
								console.log($scope.itemSelecteds);
								var age = $rootScope
										.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName = $scope.itemSelecteds.pFirstName
										+ " "
										+ $scope.itemSelecteds.pMiddleName
										+ " "
										+ $scope.itemSelecteds.pLastName
										+ " "
										+ age
										+ "/"
										+ $scope.itemSelecteds.genderCode;
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.doctorFullName = 'Dr. '
										+ $scope.itemSelecteds.dFirstName + " "
										+ $scope.itemSelecteds.dMiddleName
										+ " " + $scope.itemSelecteds.dLastName;
								$scope.patientCategoryDesc = $scope.itemSelecteds.patientCategoryDesc;
								$scope.paymentEntitlementDesc = $scope.itemSelecteds.paymentEntitlementDesc;
								$scope.bedCategoryId = $scope.itemSelecteds.bedCategoryId;
								$scope.paymentEntitlementId = $scope.itemSelecteds.paymentEntitlementId;
								$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
								$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
								$scope.isFlexiableDate = $scope.itemSelecteds.isFlexiableDate;

								$scope.unReserveModel = {
									admissionReasonId : 0,
									cancelReasonId : 0,
									unreserveReasonId : 0,
									paymentEntitlementId : $scope.itemSelecteds.paymentEntitlementId,
									patientCategoryId : $scope.itemSelecteds.patientCategoryId,
									admissionNoteId : $scope.itemSelecteds.admissionNoteId,
									doa : $scope.dateOfAdmission + ' 00:00:00',
									isFlexiableDateUN : $scope.isFlexiableDate,
									datepickerForNewDateOfAdmission : ''
								};
							}

							}

							$scope.hideShowUnreserveDiv = function(type) {
								if (type == 'cancel') {
									$scope.unreserveFlag = false;
									$scope.cancelFlag = true;
									$scope.btnValue = "Unreserve";
								} else {
									$scope.unreserveFlag = true;
									$scope.cancelFlag = false;
									$scope.btnValue = "Reshedule";
								}
							}
							$scope.saveUnreserve = function()
							{
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								var data = {
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									admissionNoteId : $scope.unReserveModel.admissionNoteId,
									doa : $rootScope
											.getChangedFormatedDateShalse($scope.dateOfAdmission)
											+ ' 00:00:00',
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate
								};
								if ($scope.canres == 'cancel') {

									if($scope.unReserveModel.cancelReasonId==''||$scope.unReserveModel.cancelReasonId==null)
										{
										growl
										.warning(
												'Please select reason for cancellation..!!',
												{
													title : 'Warning!'
												});
										return false;
										}
								
									data.isCancelReservation = 'Y';
									data.reasonForCancelation = $scope.unReserveModel.cancelReasonId;

								} else {
									
									/*console.log("NDOA",document
											.getElementById('datepickerForNewDateOfAdmission').value);
									console.log("NPDD",document
											.getElementById('datepickerForNewPDD').value);*/
									
									var newdoa = "";
									var newpdd = "";
									newdoa = document.getElementById('datepickerForNewDateOfAdmission').value;
								    newpdd = document.getElementById('datepickerForNewPDD').value;
								    
								    
								    if($scope.unReserveModel.admissionReasonId==''||$scope.unReserveModel.admissionReasonId==null)
									{
									growl
									.warning(
											'Please select reason for admission..!!',
											{
												title : 'Warning!'
											});
									return false;
									}
								    if($scope.unReserveModel.unreserveReasonId==''||$scope.unReserveModel.unreserveReasonId==null)
									{
									growl
									.warning(
											'Please select reason for unreserve bed..!!',
											{
												title : 'Warning!'
											});
									return false;
									}
							
									
									if(newdoa != "" && newpdd != ""){
									var newDoaDate = $rootScope
											.getChangedFormatedDateShalse(document
													.getElementById('datepickerForNewDateOfAdmission').value);
									var newPddDate = $rootScope
									.getChangedFormatedDateShalse(document
											.getElementById('datepickerForNewPDD').value);
									
									data.doa = $rootScope
											.getChangedFormatedDateShalse($scope.dateOfAdmission)
											+ ' 00:00:00';
									data.bedCategoryId = $scope.bedCategoryId;
									data.reasonForAdmission = $scope.unReserveModel.admissionReasonId;
									data.reasonForUnreserve = $scope.unReserveModel.unreserveReasonId;
									data.isFlexible = $scope.unReserveModel.isFlexiableDateUN;
									data.newDoa = newDoaDate + ' 00:00:00';
									data.newPdd = newPddDate + ' 00:00:00';
											data.paymentEntitlementId = $scope.unReserveModel.paymentEntitlementId;
											data.patientCategoryId = $scope.unReserveModel.patientCategoryId;
									}
									else{
										growl
										.warning(
												'DOA and PDD are mandatory..!!',
												{
													title : 'Warning!'
												});
										return false;
									}

								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ UNRESERVERESHEDULEPATIENT;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.reasonList = [];
													if (response.data.status == "success") {
														if($scope.btnValue == "Reshedule"){
															growl
															.success(
																	'Reshedule Successfully...!!',
																	{
																		title : 'Success!'
																	});
														}else{
															growl
															.success(
																	'Unreserve Successfully...!!',
																	{
																		title : 'Success!'
																	});
														}
														
														$scope.init();
														angular.element(
																'#unreserve')
																.modal('hide');
														// $state.go('admittedPatientList');
													} else {
														growl
																.error(
																		'Something wrongs!!!!',
																		{
																			title : 'Error!'
																		});
													}
													$scope
															.initBedReservetionList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage);
												});

							}
/**===========Deposit Collection On Reservation========================*/

							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};
							
							$scope.collectPayment = function(admisionObj)
							{
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
									return false;
								}
								else{
								$scope.skipMpesaValidation = false;
								$('#collectPaymentModalBedReserVation').modal('show');
								$scope.tatalAmount = admisionObj.depositPending;
								$scope.deposit.depositPending = admisionObj.depositPending;
								$scope.paymentDetails.cashAmount = admisionObj.depositPending;
								$scope.listPaymentDetails = [];
								$scope.calculateDueAmount();
								$scope.calculateTotalAmount();
								
								}
							}
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							$scope.addBtnId=false;
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
								$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
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
									
									break;
								
								case 3 :
									
									/*if($scope.paymentDetails.credCardHolderName==null||$scope.paymentDetails.credCardHolderName==''){
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
									
									if($scope.mpesaObject.transactionId == undefined || $scope.mpesaObject.transactionId == null || $scope.mpesaObject.transactionId == ""){
										growl.warning("Please Enter Transaction Id !!",{
											title : WARNING_MSG
										});
										return false;
									}
									if($scope.mpesaObject.mobileNo == undefined || $scope.mpesaObject.mobileNo == null || $scope.mpesaObject.mobileNo == ""){
										growl.warning("Please Enter Mobile Number !!",{
											title : WARNING_MSG
										});
										return false;
									}
									tempAmount = $scope.mpesaObject.mpesaAmount != '' && typeof($scope.mpesaObject.mpesaAmount) != 'undefined' ? parseFloat($scope.mpesaObject.mpesaAmount) : parseFloat(0);
									if($scope.skipMpesaValidation){
										if($scope.mpesaObject.resTransactionId!=$scope.mpesaObject.transactionId){
											growl.warning("Invalid Transaction Id",{
												title : WARNING_MSG
											});
											$scope.mpesaObject.mpesaAmount=null;
											return false;	
										}
									}
																		
									if($scope.mpesaObject.mpesaAmount==null){
										growl.warning("Amount Can not be zero",{
											title : WARNING_MSG
										});
										return false;
									}
									
									if(parseFloat($scope.mpesaObject.mpesaAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return false;
									}
									
									var invalidMpesa = false;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										if($scope.mpesaObject.transactionId == value.cardNo)
										{
											growl.warning("Collected MPesa is already used in current tranasction",{
												title : WARNING_MSG
											});
											invalidMpesa = true;
										}
									});
									if(invalidMpesa == true)
										return false;
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
										growl.warning("Please insert an amount",{
											title : 'Warning!'
										});
										return;
									}
									
										if(parseFloat($scope.paymentDetails.loyaltyCardAmt) > parseFloat($scope.paymentDetails.cardBalAmount))
										{
											growl.warning("Entered Amount Should be less than Card Amount.",{
												title : 'Warning!'
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
								
								switch($scope.paymentDetails.paymentModeId)
								{
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
									case 5 :
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:$scope.mpesaObject.transactionId,createdBy:$scope.userId,transactionNo:$scope.mpesaObject.transactionId,skipMpesaValidation : $scope.skipMpesaValidation};
										$scope.listPaymentDetails.push(paymentDetails);
										$scope.clearMpesaFields();
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
										break;
								}
								$scope.eTransferObj={};
								$scope.initPaymentDetailsObj();
								$scope.setFirstTabLevel(1);
						        $scope.setPaymentModeId(1);
								$scope.calculateDueAmount();
								$scope.calculateTotalAmount();
								
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
							
							$scope.addDeposit = function(){
								if($scope.tatalAmount ==0){
									growl.warning("Amount Should Not be 0",{
										title : 'Warning!'
									});
									return;
								}
								$('#collectPaymentModalBedReserVation').modal('hide');
								$rootScope.startLoader();
								$scope.deposit.bedReservation =true//identify Deposit
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
									if (response.data.status == "success")
									{
										$scope.initPaymentDetailsObj();
										$scope.listPaymentDetails = [];
										$scope.initDepositObj();
										$scope.patientDetailsList = [];
										$scope.patientDetails = {};
										$scope.mpesaObject={};
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
								                  "paramValue": response.data.object
								                }
								                 ]
								      };
										reportService.generate(REPORT_API_URI1,data);
										$scope.initBedReservetionList($scope.organizationId, $scope.offset,$scope.noOfRecordsPerPage);
										$rootScope.stopLoader();
									}
									else if(response.data.status == "error")
										{
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
							

							/* *************************************Mpesa Integration start****************************************** */
							$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
							$scope.skipMpesaValidation = false;
							$scope.validateMpesaDetails = function(mpesaObject){
								console.log("MPesa Object:",mpesaObject);
								$rootScope.startLoaderPopup();
								$scope.skipMpesaValidation = true;
								var data ={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										createdBy : $scope.userId,
										createdDate : moment(new Date()).format("DD-MM-YYYY"),
										updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
										updatedBy:$scope.userId,
										mpesaTxcode:$scope.mpesaObject.transactionId,
										uhid: $scope.uhidNumber,
										mpesaSendermobile:$scope.mpesaObject.mobileNo,
										mpesaAmount:$scope.mpesaObject.mpesaAmount
								};
							
								if($scope.mpesaObject.transactionId!=null && $scope.mpesaObject.mobileNo!=null){
								var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
								BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									
									if (response.data.status == "success")
										{
										
											$scope.mpesaObject.mpesaAmount = response.data.object.mpesaAmount;
											$scope.mpesaObject.resTransactionId = response.data.object.mpesaTxcode;
											$scope.addPaymentDetails();
											$rootScope.stopLoaderPopup();
										}
									else if(response.data.status == "error"){
										
										growl.error(response.data.message,{
											title : ERROR_MSG
										});
										$scope.mpesaObject.mpesaAmount=null;
										$rootScope.stopLoaderPopup();
										$scope.skipMpesaValidation = false;
									}
									$rootScope.stopLoaderPopup();
								});
								}else{
									$scope.skipMpesaValidation = false;
									growl.warning("Enter both transaction id and mobile number",{
										title : 'Warning!'
									});
									$rootScope.stopLoaderPopup();
								}
							}
							
							$scope.checkDatePdd = function(){
								 if(!angular.isDate($scope.unReserveModel.datepickerForNewPDD)){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
									 var new_date = moment($scope.unReserveModel.datepickerForNewDateOfAdmission).add(5, 'days');
									 $scope.unReserveModel.datepickerForNewPDD = new Date(new_date);
										return false;
								 }else if($scope.unReserveModel.datepickerForNewDateOfAdmission > $scope.unReserveModel.datepickerForNewPDD)
									 {
									 growl
										.warning(
												'DOA should be less then PDD',
												{
													title : 'Warning!'
												});
									 var new_date = moment($scope.unReserveModel.datepickerForNewDateOfAdmission).add(5, 'days');
									 $scope.unReserveModel.datepickerForNewPDD = new Date(new_date);
										return false;
									 }
							}
							
							$scope.checkDateDoa = function(){
								 if(!angular.isDate($scope.unReserveModel.datepickerForNewDateOfAdmission)){
									 growl.warning("Doa is invalid",{
											title : WARNING_MSG
										});
									 $scope.unReserveModel.datepickerForNewDateOfAdmission = new Date();
										return false;
								 }else if( $scope.unReserveModel.datepickerForNewDateOfAdmission < $scope.unReserveModel.datepickerForNewPDD)
									 {
									 growl
										.warning(
												'DOA should be less then PDD',
												{
													title : 'Warning!'
												});
									 $scope.unReserveModel.datepickerForNewDateOfAdmission = new Date();
										return false;
									 }
							}
							
							$scope.mpesaPaymentRequest = function(){
								
								if($scope.validateMpesaAmount == undefined || $scope.validateMpesaAmount == null){
									
									growl.warning("Please Enter Amount",{
										title : "warning!!!"
									});
									
								}else if($scope.validateMpesaAmount > $scope.dueAmount){
									
									growl.warning("Mpesa amount is should be equel or less than due amount ",{
										title : "warning!!!"
									});
									
								}else{
									$scope.skipMpesaValidation = true;
										var data ={
												organizationId :$scope.organizationId,
												unitId : $scope.unitId,
												createdBy : $scope.userId,
												createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												updatedBy:$scope.userId,
												uhidNumber: $scope.selectedUhid,
												phoneNumber:parseInt($scope.countryCallingCode+""+$scope.mobileNumber),
												partyA:$scope.mobileNumber,
												amount:$scope.validateMpesaAmount
										};
									
										if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
												(countryCallingCode != undefined && countryCallingCode != null && countryCallingCode != "")){
											$rootScope.startLoaderPopup();
										var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
										BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											
											if (response.data.status == "success")
												{
												
												growl.success(response.data.message,{
													title : SUCCESS_MSG
												});
												$rootScope.stopLoaderPopup();
												angular.element('#paymentReqModal').modal('hide');
												}
											else if(response.data.status == "error"){
												
												growl.error(response.data.message != null ? response.data.message : "There is problem in Mpesa payment gatway",{
													title : ERROR_MSG
												});
												$rootScope.stopLoaderPopup();
												$scope.skipMpesaValidation = false;
											}
											
										});
										}else{
											growl.warning("Please enter mobile code and mobile number.",{
												title : 'Warning!'
											});
											$scope.skipMpesaValidation = false;
											$rootScope.stopLoaderPopup();
										}
								}
							}
							
						$scope.validateSTKPushPayment = function(){
								console.log("$scope.selectedUhid,",$scope.selectedUhid);
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
										$scope.skipMpesaValidation = false;
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
							
						
						$scope.checkValidatePinPad = function()
						{
							//alert($location.host());
							//var IAddress = $location.host();
							//alert("IP ADRRESS");
							//alert(IAddress);
							var data ={
									organizationId :$scope.organizationId,
									unitId : $scope.unitId,
									createdBy : $scope.userId,
									createdDate : moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
									tillId:'50002',
									patientId: $scope.selectedPetientId ,
									//ip : IAddress
							};
							$scope.pinpadAmounFlag = true;
							$rootScope.startLoader();
							var URI9 = BASE_URL + ROOT_URL + "/Billing/savePinPanDetaild";
							BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								
								if (response.data.status == "success")
									{
									
										$scope.uniqueNumber = response.data.object.uniqueNumber;
										$scope.pinpadTransectionId = response.data.object.pinpadTransectionId;
										$scope.ipAddress = response.data.object.ipAddress;
										var dataForPinPad = {
											    amount: parseFloat($scope.dueAmount) * 100,
												//"amount":100,
												//tellerId: "50002",
												tranKey: $scope.uniqueNumber
										}
										
									/*	
										$http.post('http://localhost:9991/api/sale',dataForPinPad)
											    .then(function(response) {
											            alert();
											    });*/
										
										/* return $http({
								                headers: {
								                    "Accept": "application/json;odata=verbose",
								                    "content-type": "application/json;odata=verbose"
								                },
								                method: "POST",
								                url: "http://localhost:9991/api/sale",
								                data: {
								                    __metadata: {
								                        "type": "SP.Data.SongsListItem"
								                    },
								                    "amount": "100",
								                    "tellerId": "50002",
								                    "tranKey": "201903282"
								                }


								            }).then(saveContact)
								            .catch(function(message) {
								                console.log("addQuestion() error:" + message);
								            });
										
										 function saveContact(data, status, headers, config) {
									            alert("Item added successfully");
									        }*/
										
										/*var URI = "http://localhost:9991/api/sale";
										var headerObjDia = {
												'Content-Type':'application/json',
								     };
									 var data={};
					               
					             GenericServiceParamHeader
					                 .serviceAction(METHOD_POST, URI,
					                		 dataForPinPad,
					                         NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
					                 .then(
					                         function(response) {
					                             console.log(response);
					                             alert();
					                         });*/
					             
					             
//										/$scope.ipAddress+
										var urlprotocol = $location.protocol();
										//var finalUrl = "http://"+$scope.ipAddress+":9991/api/sale";
										var finalUrl = "http://127.0.0.1:9991/api/sale";
										var URI = finalUrl;
										BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											/*if (response.data.status == "success"){
												console.log("ree",response);
												console.log("reew",response.data);
												console.log("reer",response.data.rspCode);*/
												
												if(response.data.rspCode == '100' || response.data.rspCode == '003' || response.data.rspCode == '000')
													{
														$scope.paymentDetails.credCardHolderName = response.data.cardholderName;
														$scope.paymentDetails.credAmount = parseFloat(response.data.amount)/100;
														$scope.schema = response.data.scheme;
														var pan = response.data.pan;
														var lastFour = pan.substr(pan.length - 4);
														$scope.paymentDetails.credCardNumber = lastFour;
														
														
														var cardTypeId = $.grep($scope.cardTypeList, function (card) {
										    	            return card.pinpadScheme == $scope.schema;
										    	        })[0].cardTypeId;
														$scope.paymentDetails.credCardTypeId = cardTypeId;
														
														$scope.resString = JSON.stringify(response);
														console.log("resString",$scope.resString); 
														
														var dataUPDATE = {
																response : $scope.resString,
																pinpadTransectionId : $scope.pinpadTransectionId,
																patientId : $scope.selectedPetientId ,
																organizationId : $scope.organizationId,
																unitId : $scope.unitId,
																createdDate : moment(new Date()).format("DD-MM-YYYY"),
																createdBy: $scope.userId,
																status:'A'
														}
														
														
														var URIUPDATE = BASE_URL + ROOT_URL + "/Billing/updatePinpadDetails";
														BillingGenericService.serviceAction(METHOD_POST,URIUPDATE, dataUPDATE,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
															
															if (response.data.status == "success")
																{
																	console.log("saved Card Details");
																    $scope.addPaymentDetails();
																}
															else if(response.data.status == "error"){
																
																growl.error("Somting went wrong.",{
																	title : ERROR_MSG
																});
																$rootScope.stopLoader();
															}
															
														});
														
														growl.success(response.data.message,{
															title : "SUCCESS!"
														});
														
													
													}
												else
													{
														
														growl.error(response.data.message,{
															title : ERROR_MSG
														});
														return false;
													}
											/*}*/
										});
										
										$rootScope.stopLoader();
									}
								else {
									
									growl.error("Someting went wrong..!!",{
										title : ERROR_MSG
									});
									$rootScope.stopLoader();
								}
								
							});
						}

						/***********************************************Loylty card Tab START *****************************************/
						
						$scope.loyaltyCardObj ={
								
								"loyaltyCardNo" :"",
								"loyaltyCardBalance" :"",
								"authKey":0
						}
						
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
						}
						
						
						$scope.clearLoylty = function(){
							
							$scope.send = false;
							$scope.validatTextFieldFlag = true;
							$scope.paymentDetails.otp ="";
							$scope.calculateTotalDueRefAmount();
							$scope.getLoyaltyCardList($scope.selectedPatientId);
						}
						
						$scope.validateOtp = function()
						{
							
							if($scope.paymentDetails.otp==0||$scope.paymentDetails.otp==''||$scope.paymentDetails.otp==undefined||
									$scope.paymentDetails.otp==null)
								{
								growl.warning("Please enter valid OTP",
										{
											title : 'Warning!'
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
											    $scope.resend =false;
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
						
						
						/***********************************************Loylty card Tab END *****************************************/
						
						$scope.cancelPaymentReqModal = function(){
							angular.element('#paymentReqModal').modal('hide');
						}
						
						$scope.clearMpesaFields = function(){
							$scope.skipMpesaValidation = false;
							$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
						}
						
						} ]);
