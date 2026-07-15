'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:appointmentSchedulingController
 * @description #appointmentSchedulingController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'appointmentSchedulingController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'growl',
						'reportService',
						'$stateParams',
						'paymentCollFactory',
						'loyaltiCardCallFactory',
						'$window',
						'$location',
						'GenericServiceFileUpload',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,growl,reportService,$stateParams,paymentCollFactory,loyaltiCardCallFactory,$window,$location,GenericServiceFileUpload) {
							
							/*if ($cookies.get('cookieObjectReschedule') == null && $cookies.get('cookieObjectReschedule') == undefined) {
								$state.go('appointmentScheduling.year');
								$scope.setThirdTabLevel(1);
							}*/
							    

							
							
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							
							$scope.dateOptionsDaySlot = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							
							$scope.dateOptionsDob = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								$scope.openDt = function() {
									$scope.popup.opened = true;
								};

								$scope.popup =
								{
									opened : false
								};
								
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};
								
							/* init() function for form object create. */
							$scope.init = function() {
								
								//for New/Registerd
								$scope.openDatePickerDOB1 = function() {
									$scope.datepickerDOB1.opened = true;
								};

								$scope.datepickerDOB1 = {
									opened : false
								};
								
								/*** Check user is Loggedin */
								var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
								var cookieObject,cookieObjectAppConfig;
								[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.billSettlement = cookieObject.billSettlement;
								$scope.mpesaRequired = cookieObject.mpesaRequired;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$scope.isKiosk = cookieObjectAppConfig.isKiosk;
								$scope.hemodialysisSpecialityId=cookieObjectAppConfig.hemodialysisSpecialityId;
								$scope.radiologySpecialtyId = cookieObjectAppConfig.radiologySpecialityId;
							    $scope.defaultPatientCategoryId=cookieObjectAppConfig.defaultPatientCategoryId;
							    $scope.emailValidationRequired = cookieObjectAppConfig.emailValidationRequired;
							    $scope.mobileValidationRequired = cookieObjectAppConfig.mobileValidationRequired;
							    $scope.remarkForChangingVisitType = null;
								$scope.reasonIdForChangingVisitType = null;
							    $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								if (cookieObjectAppConfigSecond != undefined) {
								$scope.couponSystemApplicable = cookieObjectAppConfigSecond.couponSystemApplicable;
								}
								$scope.openScheduling = true;
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.systemGeneratedVisitTypeId=null;
						    	$scope.userDefinedVisitTypeId=null;
								$scope.fromCollectScreen = false;
								$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
								$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
								$scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
								$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
								$scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
								$scope.exceededBillAmountSettlement=cookieObjectAppConfigSecond.exceededBillAmountSettlement;
								$scope.fileObj = {};
								$scope.fileObj.file = null;
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
								$scope.btnDisabled = false;
							    $scope.flagForSale=false;
							    $scope.flagForPay=false;
							    $scope.flagForBharatQr=false;
							    $scope.objPosMachine={
										isSale:false, 
										isSMSpay : false,
										isBharatQr:false, 
										posMachineDeviceId:"",
										deviceName:"",
										machineConfigDetails:"",
										mobileNumberForPOS:""
								};
								$scope.isReshedule = 'N';

								$scope.regiType = "2";

								$scope.fieldsDisabled = false;

								$scope.selectType = "doctor";
								$scope.selectedSlotPeriod = "Y";

								$scope.selectedSlotId = "1";
								
								$scope.allowedOverBookingSlots = 0;
								$scope.slotStatusId = 4;
								$scope.priorityId = 1;
								
								$scope.deptWiseAppointmentFlag = true;

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};
								$scope.DepositObj = {};
								$scope.DepositObj.userEnterDepositAmount = 0;
								//Get Active Prefix List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPREFIXLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.prefixList = [];
													if (response.data.status == "success")
														$scope.prefixList = response.data.listObject;
												});

								
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
								
								//Get Active Speciality List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVESPECIALITY;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.specialityList = response.data.listObject;
													    $scope.specialityMasterListCopy = response.data.listObject;
												});

								//Get Active Modality Type List
								/*var URI = BASE_URL + ROOT_URL
										+ GETACTIVEMODALITYTYPELIST;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.modalityTypeList = response.data.listObject;
												});*/

								//Object For Patient Search
								$scope.patientSearchObj = {
									"uhidNumber" : "",
									"patientName" : "",
									"genderId" : "",
									"mobileNumber" : "",
									"dob" : "",
									"identificationNumber" : "",
									"identificationTypeId" : "",
									"unitId" : $scope.unitId,
									"organizationId" : $scope.organizationId,
									"registrationTypeIdList" : [1,2,3,4,6]
								}

								//Object For Patient Details
								$scope.patientDetails = {
									/* Vivek Comment "patientName" : "",
									"genderCode" : "",
									"age" : "",
									"uhIdNumber" : "",
									"prefixId" : "",
									"genderId" : "",
									"birthDate" : "",
									"mobikeNumber" : "",
									"firstName" : "",
									"lastName" : "",
									"middleName" : "",
									"email" : "",
									"remark" : "",
									"patientId" : ""*/
								}
								
								$scope.dayList =[{id:"1",name:"Monday"},{id:"2",name:"Tuesday"},{id:"3",name:"Wednesday"},{id:"4",name:"Thursday"},{id:"5",name:"Friday"},{id:"6",name:"Saturday"},{id:"7",name:"Sunday"}];
								
								
								//getting default field configuration start
								//code for registration field configuration start
							
									
											var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+$scope.organizationId+"/"+$scope.unitId;
													
											GenericService.serviceAction("GET", URI).then(function (response) {
												if (response.data.status == 'success') {
													
													$scope.fieldConfigList=response.data.listObject;
													if($scope.fieldConfigList.length>0)
														{
														 $scope.registrationFieldObj=$scope.fieldConfigList[0];
														 console.log("registrationFieldObj",$scope.registrationFieldObj);
														}else{
															$scope.registrationFieldObj={
																	prefix: "Y",
																	firstName: "Y",
																	middleName: "N",
																	lastName: "Y",
																	birthDate: "Y",
																	sex: "Y",
																	callingCode: "Y",
																	mobileNo: "Y",
																	mobileDigit:"10",
																	nationality:"Y",
																	identificationDoc:"Y",
																	race:"Y",
																	maritalStatus:"Y",
																	occupation:"Y",
																	patientCategory:"Y",
															}
														}												
				
													

												} else if (response.data.status == 'error') {
													growl.error(response.data.message,
														{
															title: 'Error!'
														});
												}
											});
										
								//getting default field configuration start
											var data={};
											var URI = BASE_URL + ROOT_URL
											+ GETACTIVEIDENTIFICATION;
									BillingGenericService
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.identificationList = [];
												if (response.data.status == "success")
													$scope.identificationList = response.data.listObject;
											});
									
                               $scope.isTeleconsultationAppointment=0;
                               
                               $scope.otpvalidated = false;
                               $scope.emailOtp = null;
                               $scope.entredOpt = "";
			             	   $scope.storeEmailId = null;
			             	  $scope.otpvalidatedMobile = false;
							}

							$scope.init();
							
							//*************Payment collection start**********************
							
							$scope.paymentModes = {
									1: "Cash",
									2: "Cheque",
									3: "Credit Card",
									4: "E - Transfer",
									5: "Loyalty Card"	
							}
							
							$scope.initPaymentDetails=function(){
								$scope.paymentDetails={
										"paymentRecieptDetailsId":"",
										"paymentModeId"			:1,
										"paymentModeName"		:"Cash",
										"amount"				:"",
										"bankId"				:"",
										"bankName"				:"",
										"accountNumber"			:"",
										"checkNumber"			:"",
										"cardNo"				:"",
										"cardHolderName"		:"",
										"dateOfTransaction"		:new Date().getTime(),
										"expiryDate"			:"",
										"paymentVoucherNo"		:"",
										"cvv"					:"",
										"unitId"				:$scope.unitId,
										"orgnisationId"			:$scope.organizationId,
										"status"				:"A",
										"createdDate"			:new Date().getTime(),
										"createdBy"				:$scope.userId,
										"updatedDate"			:new Date().getTime(),
										"updatedBy"				:$scope.userId,
										"chequeDate"			:"",
										"cardTypeId"			:"",
										"isCancle"				:"I",
										"loyaltyCardAmt"        :'',
										"cardBalAmount"         :'',
										"loyaltyCardNo"         :'',
										'otp'                   :'',
										'hospitalAccountId'     : '',
										'transactionNo'         : ""
										
										
								}
								//$scope.initReferralObj();
								
							}
							
							$scope.getBankList = function(){
								var obj={
										"unitId":$scope.unitId,
										"orgnisationId" : $scope.organizationId
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + API + BILLING + "/bankMaster";
								BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.bankList = response.data.listObject;
									}
								});
							}
							$scope.getBankList();
							
							$scope.getCardTypeList = function(){
								var obj={
										"unitId":$scope.unitId,
										"orgnisationId" : $scope.organizationId
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + API + BILLING + "/cardTypeMaster";
								BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.cardTypeList = response.data.listObject;
									}
								});
							}
							$scope.getCardTypeList();
							
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
							$scope.getHospitalBankList();
							
							$scope.setBankName=function(bankId){
								angular.forEach($scope.bankList, function (value, key) {
									if(value.bankId == bankId){
										$scope.paymentDetails.bankName = value.bankName;
										return;
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
							 
				//*************Payment collection**********************			 
							/*  $scope.getAssetList = function() {
									var data = {};
									
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getAsset";								
							BillingGenericService
							.serviceAction(METHOD_GET, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.assetList = [];
										if (response.data.status == "success")
											$scope.assetList = response.data.listObject;
										console.log($scope.countryList)
									});
								}
						      $scope.getAssetList();*/
							
							$scope.getBedList = function() {
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
								};
								console.log(data);
								var URI = BASE_URL + ROOT_URL
								+ "/api/adt/getDialysisChair";								
								GenericService
								.serviceAction("POST", URI, data)
						.then(
								function(response) {
									console.log(response);
									$scope.chairList = [];
									if (response.data.status == "success")
										$scope.chairList = response.data.listObject;
									console.log($scope.chairList)
								});
							}
					      $scope.getBedList();
						
							//Function For Get Doctor Lisy By Multiple Speciality 
							$scope.getDoctorBySpeciality = function(id) {

								var speciality = [];
								speciality.push(id);
								
												var spcObj = $.grep($scope.specialityList, function (disCat) {
										            return disCat.specialityId == id;
										        })[0];
												$scope.deptWiseAppointment = spcObj.deptWiseAppointment!=null ? spcObj.deptWiseAppointment : 'N';
												$scope.isNursingAssessmentRequired = spcObj.isNursingAssessmentRequired!=null ? spcObj.isNursingAssessmentRequired : 'N';
												if(spcObj.deptWiseAppointment == 'Y')
													{
													    $scope.commonListDoctorModality = [];
														$scope.deptWiseAppointmentFlag = false;
														var newArr = {};
														newArr.specialityName = spcObj.specialityName;
														newArr.doctorId = null;
														newArr.specialityId = spcObj.specialityId;
														newArr.doctorName  = null;
														newArr.allowedOverBookingSlots = spcObj.allowedOverbookingSlots;
														$scope.commonListDoctorModality.push(newArr);
													}
												else
													{
													$scope.deptWiseAppointmentFlag = true;
												$rootScope.startLoader();
												
												var data = {
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													specialityArray : speciality
												}
												console.log(data);
												var URI = BASE_URL + ROOT_URL
														+ GETDOCTORSLISTAGANISTSPECIALITIES;
												BillingGenericService
														.serviceAction(METHOD_POST, URI, data,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	$scope.doctorList = [];
																	$scope.commonListDoctorModality = [];

																	// $rootScope.startLoader();
																	if (response.data.status == "success") {
																		$scope.doctorList = response.data.listObject;
																		$scope.commonListDoctorModality = response.data.listObject;
																		

																		/*if ($cookies.get('cookieObjectReschedule') == null && $cookies.get('cookieObjectReschedule') == undefined) {
																			$scope.setThirdTabLevel(1);
																			$state.go('appointmentScheduling.year');
																		}*/
																		if (angular.equals({},$stateParams.appointmentObjectReschedule)) {
																			$state.go('appointmentScheduling.year');
                                                                            $scope.setThirdTabLevel(1);
																		}
																	
																		$scope.yearSlotList=[];
																		$scope.doctorId = '';
																		$scope.doctorName = '';
																		$scope.specialityName = '';
																		$scope.doctorId = '';
																		$scope.allowedOverBookingSlots = 0;
																		$scope.specialityId = '';
																		
																		$rootScope.stopLoader();
																	}

																});

													}

							}
							
					
					
							//Function For Get Doctor Lisy By Multiple Speciality 
							$scope.getDoctorBySpecialityReschedule = function(id) {

								var speciality = [];
								speciality.push(id);
								
								var dataS = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVESPECIALITY;
						BillingGenericService
								.serviceAction(METHOD_POST, URI,
										dataS,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success"){
												$scope.specialityList = response.data.listObject;
												var spcObj = $.grep($scope.specialityList, function (disCat) {
										            return disCat.specialityId == id;
										        })[0];
												
												if(spcObj.deptWiseAppointment == 'Y')
													{
													    $scope.commonListDoctorModality = [];
														$scope.deptWiseAppointmentFlag = false;
														var newArr = {};
														newArr.specialityName = spcObj.specialityName;
														newArr.doctorId = null;
														newArr.specialityId = spcObj.specialityId;
														newArr.doctorName  = null;
														newArr.allowedOverBookingSlots  =spcObj.allowedOverbookingSlots!=null ? spcObj.allowedOverbookingSlots : 0;
														$scope.commonListDoctorModality.push(newArr);
													}
												else
													{
													$scope.deptWiseAppointmentFlag = true;
												$rootScope.startLoader();
												
												var data = {
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													specialityArray : speciality
												}
												console.log(data);
												var URI = BASE_URL + ROOT_URL
														+ GETDOCTORSLISTAGANISTSPECIALITIES;
												BillingGenericService
														.serviceAction(METHOD_POST, URI, data,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	$scope.doctorList = [];
																	$scope.commonListDoctorModality = [];

																	// $rootScope.startLoader();
																	if (response.data.status == "success") {
																		$scope.doctorList = response.data.listObject;
																		$scope.commonListDoctorModality = response.data.listObject;
																		

																	/*	if ($cookies.get('cookieObjectReschedule') == null && $cookies.get('cookieObjectReschedule') == undefined) {
																			$scope.setThirdTabLevel(1);
																			$state.go('appointmentScheduling.year');
																		}*/
																		$scope.setThirdTabLevel(4);
																		$scope.yearSlotList=[];
																		
																		$scope.doctorName = '';
																		$scope.specialityName = '';
																		//$scope.doctorId = '';
																		$scope.allowedOverBookingSlots = 0;
																		//$scope.specialityId = '';
																		
																		$rootScope.stopLoader();
																	}

																});

													}
											}
										});
						
								
								

							}

							//For Get Active Gender List
							$scope.getSexMasterList = function() {
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEGENDERLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.sexMasterList = response.data.listObject;
												});
							}
							$scope.getSexMasterList();

                            $scope.loadMoreData = function()
                            {
                                $scope.offset+=200;
                                $scope.loadMoreFlag=true;
                                $rootScope.searchPatient($scope.patientSearchObj);
                            }
                            $scope.offset=0;
                            $scope.loadMoreFlag=false;
                            $scope.noOfRecordsPerPage=200;
							//Function For Search Patient
							$rootScope.searchPatient = function(patientsearchObj) {
								//alert();
								if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
								{
									growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
									});
									return false;
								}
									
								else{
										//alert("else AlERT");
									}

									  if($scope.loadMoreFlag==true)
                                    	    	{
                                    	    	patientsearchObj.offset=$scope.offset;
                                    	    	$scope.loadMoreFlag=false;
                                    	    	}
                                    	    else
                                    	    	{
                                    	    	patientsearchObj.offset=0;
                                    	    	$scope.loadMoreFlag=false;
                                    	    	$scope.patientList = [];
                                    	    	}
                                    	    patientsearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;

								patientsearchObj.dob =	$('#datepickerSearch').val()!="" && $('#datepickerSearch').val()!=undefined ? $rootScope.getChangedFormatedDateShalse($('#datepickerSearch').val()) : "";
								var URI = BASE_URL + ROOT_URL
										+ GETTEMPORYPATIENTFORPREREGISTRATION;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												patientsearchObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.patientList=[];
													$scope.patientListTemp=[];
													
													$scope.patientListTemp = response.data.listObject;
													
													if($scope.patientListTemp.length>0){
														if($scope.selectedSlotId == '1'||$scope.selectedSlotId == '3' )
														{
															angular.forEach($scope.patientListTemp, function (value, key) {
																if(value.registrationTypeId!=2 ){
																	$scope.patientList.push(value);
																}
															});
															
														}else{
															$scope.patientList=$scope.patientListTemp;
														}
															
													}
													console.log("PatientList",$scope.patientList);
													if($scope.patientList.length == 0){
														growl.warning("No Result Found!",{
															title : ERROR_MSG
														});
														$scope.patientSearchObj.dob = '';
													}
												});
							}

							
							$scope.checkAppointementRequestAlreadyTheirOrNot= function()
							{
								
								var data = {
										fromDate : moment(new Date()).format('YYYY-MM-DD'),
										toDate : moment(new Date()).format('YYYY-MM-DD'),
										appointmentTypeId : 1,
										isAppointmentRequired :'Y',
										doctorId:$scope.doctorId,
										specialityId:$scope.specialityId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										patientId:$scope.patdhId ,
										subSpecialityId:0,
										modalityId:0,
										visitTypeId:0,
									}
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + SEARCHAPPOINTMENTSBYCRITERIA;
								        BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) 
												{
													console.log(response);
													$scope.appointmentListNew = [];
													if (response.data.status == "success")
														{
														$rootScope.stopLoader();
														$scope.appointmentListNew = response.data.listObject;
														 angular.forEach($scope.appointmentListNew,function(value,index) {
															 value.property = value.appointmentDate +" "+value.fromTimeString;
														 });
														 
														 if($scope.appointmentListNew[0].appointmentId > 0)
															{
																if( $scope.appointmentListNew[0].appointmentStatusId != 2)
																{
												            	growl.warning(" Appointment are already booked for the patient on "+ $scope.appointmentListNew[0].property +" with "+$scope.appointmentListNew[0].doctorName+". Please Cancel !",{
										    						title : ERROR_MSG
										    					});
																}
															}
														}   
														else if (response.data.status == "error") 
														{
															$rootScope.stopLoader();
														}
													
												});
									}
								        
							//Function For Selected Patient assign to patient Details Object
							$scope.getPatientDetailsByIndex = function(index) 
							{
								
								$scope.patdhId = $scope.patientList[index].patientId;
								$scope.checkAppointementRequestAlreadyTheirOrNot();
								$scope.otpvalidated = false;
                                if($scope.patientList[index].admissionId > 0)
                                {
                                     switch (parseInt($scope.patientList[index].visitTypeId)) 
                                     {
                                        case 2:
                                                var message = "";
                                                if($scope.patientList[index].visitTypeId==2)
                                                    message = "Patient is already admitted in IP...!!";
                                                else
                                                    message = "Patient is already admitted in another unit...!!";
                                                
                                                growl.warning(message,{
                                                    title : WARNING_MSG
                                                });
                                                return false;
                                                //$scope.patientList = [];
                                                //return false;
                                            break;
                                        case 3:
                                            
                                            if($scope.patientList[index].visitTypeId==3)
                                                message = "Patient is already admitted in Day Care...!!";
                                            else
                                                message = "Patient is already admitted in another unit...!!";
                                            
                                            growl.warning(message,{
                                                title : WARNING_MSG
                                            });
                                            //$scope.patientList = [];
                                            return false;
                                            break;
                                        case 4:
                                            
                                            if($scope.patientList[index].visitTypeId==4)
                                                message = "Patient is already admitted in ER...!!";
                                            else
                                                message = "Patient is already admitted in another unit...!!";
                                            
                                            growl.warning(message,{
                                                title : WARNING_MSG
                                            });
                                            //$scope.patientList = [];
                                            return false;
                                            break;
                                        default:
                        
                                    }
                                }

								if($scope.patientList[index].appointmentId > 0)
								{
					            	growl.warning("This patient already has an active appointment.",{
			    						title : ERROR_MSG
			    					});
					            	return false;
								}
								else{
									
							$(".patienttSearchResult").hide();
							var data = {
									specialityId : $scope.specialityId,
									doctorId :  $scope.doctorId,
									patientId : $scope.patientList[index].patientId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotId : $scope.slotId,
									appointmentTypeId : $scope.selectedSlotId,
									appointmentDate : $scope.appointmentDate2
									
								};
							
							var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/check";
							BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
							
												if (response.data.code == 409) {
													
													growl.warning(response.data.message,{
														title : WARNING_MSG
													});
													angular.element('#patientSearchForRegistration').modal('hide');
													$scope.btnDisabled = false;
													$scope.newPatientChange();
													$timeout(function() {
																$state.go('appointmentScheduling.year',{}, {reload: true});
															}, 1000);
												}
												else if(response.data.status="success")
												{
													$scope.abscondPatientFlag="";
													if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
														console.log($scope.patientList[index]);
														//return false;
														$scope.patientDetails.prefixId = ($scope.patientList[index].prefixId.toString());
														$scope.patientDetails.firstName = ($scope.patientList[index].firstName);
														$scope.patientDetails.middleName = ($scope.patientList[index].middleName);
														$scope.patientDetails.lastName = ($scope.patientList[index].lastName);
														$scope.patientDetails.age = ($scope.patientList[index].ageString);
														$scope.patientDetails.birthDate = ($scope.patientList[index].dob);
														$scope.patientDetails.genderId = ($scope.patientList[index].genderId.toString());
														$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
														$scope.patientDetails.mobileNumber = ($scope.patientList[index].mobileNumber);
														$scope.mobileNumber = ($scope.patientList[index].mobileNumber);
														$scope.patientDetails.email = ($scope.patientList[index].email);
														$scope.patientDetails.bloodGroup = ($scope.patientList[index].bloodGroup);
														$scope.countryCallingCode = ($scope.patientList[index].countryCallingCode);
														$scope.patientDetails.patientId = ($scope.patientList[index].patientId);
														$scope.patientDetails.patientCategoryId = ($scope.patientList[index].patientCategoryId);
														$scope.objPosMachine.mobileNumberForPOS = ($scope.patientList[index].mobileNumber);
														$scope.registrationTypeId = ($scope.patientList[index].registrationTypeId);
														$scope.regiType = "2";
														
														//$scope.datepickerDOB = new Date($rootScope.getFormatedDate($scope.patientList[index].dob));
														//$scope.datepickerDOB = new Date('05/20/1987');
														$scope.datepickerDOB = new Date($scope.patientList[index].dob);
														$scope.fieldsDisabled = true;
														
														var  data = {
														    	
													    		 unitId:$scope.unitId,
																 organizationId  :$scope.organizationId,
													    		 patientId : $scope.patientDetails.patientId,
													    		 specialityId : $scope.specialityId,
													    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
													    		 
													     };
														if($scope.patientAppoObj.deptWiseAppointment!=null ? $scope.patientAppoObj.deptWiseAppointment : $scope.deptWiseAppointment != null ? $scope.deptWiseAppointment : 'N'=="N")
															data.doctorId = $scope.doctorId
														else	
															data.doctorId = 0;
														
														 var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
													     BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
													     {
													    	 $scope.systemGeneratedVisitTypeId=response.data.toString();
													    	 $scope.userDefinedVisitTypeId=response.data.toString();
													     });
														
														
														if($scope.selectedSlotId == 3)
															{
																var data ={
																		orgId:$scope.organizationId,
																		unitId : $scope.unitId,
																		age : $scope.patientList[index].ageString,
																		sexId : $scope.patientList[index].genderId
																}
																
																var URI = BASE_URL + ROOT_URL + "/api/packages/ehcPackages";
																BillingGenericService
																		.serviceAction(METHOD_POST, URI, data,
																				NOTIFICATION_MSG_STATUS_FALSE)
																		.then(
																				function(response) {
																					console.log(response);
																					$scope.packageEHCList = [];
																					// $rootScope.startLoader();
																					if (response.data.status == "success") {
																						$scope.packageEHCList = response.data.listObject;
																						console.log($scope.packageEHCList);
																					}
						
																				});
																
																
															}
													}else{
														    $scope.abscondIndex=index;
															$scope.abscondPatientFlag='Y';
															angular.element('#abscondedPatientPopUp').modal('show');
													 }
													$scope.patientAppoObj.patientId=$scope.patientList[index].patientId;
													$scope.patientAppoObj.doctorId= $scope.doctorId;
													$scope.patientAppoObj.specialityId=$scope.specialityId;
													$scope.patientAppoObj.patientCategoryId=$scope.patientList[index].patientCategoryId;
													$scope.patientAppoObj.isDeptWise='N';
													
													
													
													
													//$scope.getBillAmountForAppointment();
												}else{
													growl.warning(response.data.message,{
														title : WARNING_MSG
													});
													return false;
												}
												
											});
								
							}//Else Close

							}
							$scope.creatingEncounterForAbscondedPatient=function(){
								 var index=$scope.abscondIndex;
								$scope.patientDetails.prefixId = ($scope.patientList[index].prefixId
										.toString());
								$scope.patientDetails.firstName = ($scope.patientList[index].firstName);
								$scope.patientDetails.middleName = ($scope.patientList[index].middleName);
								$scope.patientDetails.lastName = ($scope.patientList[index].lastName);
								$scope.patientDetails.age = ($scope.patientList[index].ageString);
								$scope.patientDetails.birthDate = ($scope.patientList[index].dob);
								$scope.patientDetails.genderId = ($scope.patientList[index].genderId
										.toString());
								$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.mobileNumber = ($scope.patientList[index].mobileNumber);
								$scope.patientDetails.email = ($scope.patientList[index].email);
								$scope.patientDetails.patientId = ($scope.patientList[index].patientId);
								$scope.regiType = "2";
								$scope.patientDetails.bloodGroup = ($scope.patientList[index].bloodGroup);
								
								//$scope.datepickerDOB = new Date($rootScope.getFormatedDate($scope.patientList[index].dob));
								//$scope.datepickerDOB = new Date('05/20/1987');
								$scope.datepickerDOB = new Date($scope.patientList[index].dob);
								$scope.fieldsDisabled = true;
								
								if($scope.selectedSlotId == 3)
									{
										var data ={
												orgId:$scope.organizationId,
												unitId : $scope.unitId,
												age : $scope.patientList[index].ageString,
												sexId : $scope.patientList[index].genderId
										}
										
										var URI = BASE_URL + ROOT_URL + "/api/packages/ehcPackages";
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															$scope.packageEHCList = [];
															// $rootScope.startLoader();
															if (response.data.status == "success") {
																$scope.packageEHCList = response.data.listObject;
																console.log($scope.packageEHCList);
															}

														});
										
										
									}
							}

                                   // age calculation 125
									$scope.newAge="";
                            		$scope.calDOBFlag = 0;
                            		$scope.ageFunction= function(flag)
                            		{
                            			if ($scope.patientDetails.age && ($scope.patientDetails.age < 0 || $scope.patientDetails.age > 125)) {
                                              $scope.patientDetails.age = null;
                                                }

                            		}



							
							$scope.calculateAge= function()
							{
								if($scope.datepicker == undefined || $scope.datepicker == null || $scope.datepicker == ''){
                                            										 $scope.age = "";
                                            										 $scope.datepicker=""
                                            										   return false;
                                            									}else{
								if($scope.datepicker > new Date())
									{
											       $scope.age = "";
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepicker = new Date();
												
												   return false;
									}else{
										
										var dt = moment($scope.datepicker).format('MM/DD/YYYY');
										$scope.newAge = $rootScope.getAge(dt);
										if($scope.newAge != undefined){
											if($scope.newAge>=$scope.minAge && $scope.newAge <= $scope.maxAge){
												$scope.age=$scope.newAge;	
											}else{
												$scope.age = "";
												growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
													title : "warning"
													});
											}
										}
										
									}
							}
							
							}
							
							
							$scope.calculateAge= function()
							{
								$('#searchServiceId').val('');
								if($scope.datepickerDOB == undefined || $scope.datepickerDOB == null || $scope.datepickerDOB == ''){
									$scope.patientDetails.age  = "";
									   growl.warning("Date is invalid",{
											title : "warning"
										});
									   $scope.datepickerDOB = new Date();
									
									   return false;
								}else{
									if($scope.datepickerDOB > new Date())
									{
										$scope.patientDetails.age = "";
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepickerDOB = new Date();
												
												   return false;
									}else{
								var dt = moment($scope.datepickerDOB).format('MM/DD/YYYY');
								$scope.patientDetails.age = $rootScope.getAge(dt);
								
								if($scope.selectedSlotId == '3')
									{
									var data ={
											orgId:$scope.organizationId,
											unitId : $scope.unitId,
											age : $scope.patientDetails.age,
											sexId : $scope.patientDetails.genderId
									}
									
									var URI = BASE_URL + ROOT_URL + "/api/packages/ehcPackages";
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														$scope.packageEHCList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success") {
															$scope.packageEHCList = response.data.listObject;
															console.log($scope.packageEHCList);
														}

													});
									}
								}
							}
								
							}

							//Function For Get Date Wise Slots
							$scope.getSlotsOfDate = function() {
								$rootScope.startLoader();
							//	var dt = $('#datepicker').val();
								//alert(dt);
								$scope.ehcCheckDate = moment(new Date()).format('YYYY-MM-DD');
								//var array = dt.split('/');
								var newDate = "";
								var newDate1 = "";
								//newDate1 = array[2] + '-' + array[1] + '-'+ array[0];
								
								newDate =  moment(this.datepicker).format('YYYY-MM-DD');
								//alert($scope.selectedSlotId);
								
								if($scope.specialityId == '' || $scope.specialityId == null)
									{
										/*if ($cookies.get('cookieObjectReschedule') != null
												&& $cookies.get('cookieObjectReschedule') != undefined) {
											if($scope.rescheduleCookiesObj.selectedSlotId == '1' || $scope.selectedSlotId == '2')
												{
												  $scope.specialityId = $scope.rescheduleCookiesObj.specialityId;
												  if($scope.rescheduleCookiesObj.selectedSlotId == '1'){
													  $scope.doctorId = $scope.rescheduleCookiesObj.doctorId;
												  }
												
												}
										}*/
									
									if (!angular.equals({},$stateParams.appointmentObjectReschedule)) {
										if($scope.rescheduleCookiesObj.selectedSlotId == '1' || $scope.selectedSlotId == '2')
											{
											  $scope.specialityId = $scope.rescheduleCookiesObj.specialityId;
											  if($scope.rescheduleCookiesObj.selectedSlotId == '1'){
												  $scope.doctorId = $scope.rescheduleCookiesObj.doctorId;
											  }
											
											}
									}
									}

								if ($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == true) {
									var data = {
										specialityId : $scope.specialityId,
										doctorId : $scope.doctorId,
										appointmentDate : newDate,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										slotTypeId : $scope.selectedSlotId
									};
								} else if ($scope.selectedSlotId == '2'){
									var subSpecIdTempId = '';
									var tempSubDe =""+$scope.subSpecialityId;
									/*if($scope.subSpecialityId.includes("_"))
										{
											var arrayRe = $scope.subSpecialityId.split('_');
											subSpecIdTempId  = arrayRe[1];
										}
									else
										{
										  subSpecIdTempId = $scope.subSpecialityId; 
										}*/
									
									
									if (tempSubDe.indexOf('_') > -1) {
										var arrayRe = tempSubDe.split('_');
										subSpecIdTempId  = arrayRe[1];
									} else {
										subSpecIdTempId = $scope.subSpecialityId; 
									}
									
									var data = {
										subSpecialityId : subSpecIdTempId,
										specialityId : $scope.specialityId,
										modalityId : $scope.modalityId,
										appointmentDate : newDate,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										slotTypeId : $scope.selectedSlotId,
										visitTypeId : 1
									};
								}
								else if($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == false)
									{
									var data = {
											specialityId : $scope.specialityId,
											appointmentDate : newDate,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : 4
										};
									}
								else if($scope.selectedSlotId == 8 ){
									var data={};
									data.slotTypeId = 8;
									data.bedId = $scope.bedId;
									data.specialityId = $scope.hemodialysisSpecialityId;
									data.organizationId = $scope.organizationId;
									data.unitId = $scope.unitId;
									data.appointmentDate=newDate;
								}
								else
									{
									var data = {
											appointmentDate : newDate,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.selectedSlotId
										};
									}

								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success") {
														$scope.slotDetailsList = response.data.listObject;
														console.log($scope.slotDetailsList);
														    $timeout(function(){
                                                                if (!angular.equals({},$stateParams.appointmentObjectReschedule)) {
                                                                    $state.go('appointmentScheduling.day');
                                                                    $scope.setThirdTabLevel(4);
                                                                }
														    },1000)

													    if($scope.slotDetailsList.length == 0 && $scope.thirdTabLevel==4){
															   growl.warning("Slots not available",{
																	title : WARNING_MSG
																});
																return;
														  }
														$rootScope.stopLoader();
													}
													else
														{
														$rootScope.stopLoader();
														}

												});

							}
							
							
							$scope.redirectDayfromMonth = function(date,setTab){
								
								
								$rootScope.startLoader();
								if(setTab == 1){
									$scope.setThirdTabLevel(4);
								}else{
									$scope.setSecondTabLevel(4);
								}
								
								var arrayTmp = date.split('-');
								var nd = arrayTmp[1]+'/'+arrayTmp[2]+'/'+arrayTmp[0];
								$scope.datepicker  = new Date(nd);
									$scope.ehcCheckDate = date;
									var newDate = "";
									var newDate1 = "";
									newDate =  date;
									
									if($scope.specialityId == '' || $scope.specialityId == null)
										{
										if (!angular.equals({},$stateParams.appointmentObjectReschedule)) {
											if($scope.rescheduleCookiesObj.selectedSlotId == '1' || $scope.selectedSlotId == '2')
												{
												  $scope.specialityId = $scope.rescheduleCookiesObj.specialityId;
												  if($scope.rescheduleCookiesObj.selectedSlotId == '1'){
													  $scope.doctorId = $scope.rescheduleCookiesObj.doctorId;
												  }
												
												}
										}
										}

									if ($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == true) {
										var data = {
											specialityId : $scope.specialityId,
											doctorId : $scope.doctorId,
											appointmentDate : newDate,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.selectedSlotId
										};
									} else if ($scope.selectedSlotId == '2'){
										var subSpecIdTempId = '';
										var tempSubDe =""+$scope.subSpecialityId;
										if (tempSubDe.indexOf('_') > -1) {
											var arrayRe = tempSubDe.split('_');
											subSpecIdTempId  = arrayRe[1];
										} else {
											subSpecIdTempId = $scope.subSpecialityId; 
										}
										
										var data = {
											subSpecialityId : subSpecIdTempId,
											specialityId : $scope.specialityId,
											modalityId : $scope.modalityId,
											appointmentDate : newDate,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.selectedSlotId
										};
									}
									else if($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == false)
										{
										var data = {
												specialityId : $scope.specialityId,
												appointmentDate : newDate,
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												slotTypeId : 4
											};
										}
									else if($scope.selectedSlotId == 8 ){
										var data={};
										data.slotTypeId = 8;
										data.bedId = $scope.bedId;
										data.specialityId = $scope.hemodialysisSpecialityId;
										data.organizationId = $scope.organizationId;
										data.unitId = $scope.unitId;
										data.appointmentDate=newDate;
									}
									else
										{
										var data = {
												appointmentDate : newDate,
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												slotTypeId : $scope.selectedSlotId
											};
										}

									console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														$scope.slotDetailsList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success") {
															$scope.slotDetailsList = response.data.listObject;
															console
																	.log($scope.slotDetailsList);
															$rootScope.stopLoader();
														}
														else
															{
															$rootScope.stopLoader();
															}

													});
							}
							
							/*$scope.Test = function()
							{
								angular
								.element(
										'#patientSearchForRegistration')
								.modal('show');
							}*/
								
							$scope.getOverBookSlotCheckPopup = function(item)
							{
								//alert($scope.allowedOverBookingSlots);
								if($scope.openScheduling!=null && $scope.openScheduling == false)
									{
										return false;
									}
								 var endTime = moment(item.originalFromTime, 'hh:mm');
							     var startTempTime = moment().format("HH:mm");
							     var startTime = moment(startTempTime, 'hh:mm');
							     if(endTime.isBefore(startTime) && moment(item.appointmentDate2).isSame(moment().format('YYYY-MM-DD'))
										  &&(item.isBlockedUnblocked == '2' && item.slotPatientDetailsListsSize >= '0'))
									 {
							    	 	return false;
									 }
								
								if(item.allowedOverBookingSlots > 0)
									{
										var data = {
												
												/*appointmentDate : item.appointmentDate2,
												doctorId:item.doctorId,
												specialityId : item.specialityId,
												organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												slotTypeId : $scope.selectedSlotId*/
												slotId : item.slotId
										}
										
										//0- Over booking Not Allowed
										//1- Over booking Allowed
										var URI = BASE_URL + ROOT_URL + ISOVERBOOKINGALLOWEDORNOT;
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															/*if (response.data.listObject[0] == "0") {
																growl.error("Over Booking Not Allowed",{
																	title : ERROR_MSG
																});
																return false;
															}
															else
																{
																if (response.data.listObject[1] == $scope.allowedOverBookingSlots) {
																	growl.error("Over Booking Slots Full",{
																		title : ERROR_MSG
																	});
																	return false;
																}else*/
															if(response.data.object==true)
																{
																
																	$scope.doctorName = item.doctorName;
																	$scope.specialityname = item.specialityName;
																	$scope.fromTime = item.fromTimeString2;
																	$scope.toTime = item.toTimeString2;
																	$scope.appointmentDate = item.appointmentDate;
																	$scope.appointmentDate2 = item.appointmentDate2;
																	$scope.slotId = item.slotId;
																	$scope.slotStatusId = 6;
																	$scope.priorityId  = 2;
																	$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
																	angular
																	.element('#patientSearchForRegistration').modal('show');
																}else{
																	$scope.slotStatusId = 4;
																}

														});
										
									}
								
							}
							
							
							
							$scope.getOverBookSlotCheckPopupForHemodialysis = function(item)
							{
								//alert($scope.allowedOverBookingSlots);
								if($scope.openScheduling!=null && $scope.openScheduling == false)
									{
										return false;
									}
								 
								if(item.allowedOverBookingSlots > 0)
									{
										var data = {
												slotId : item.slotId
										}
										
										//0- Over booking Not Allowed
										//1- Over booking Allowed
										var URI = BASE_URL + ROOT_URL + ISOVERBOOKINGALLOWEDORNOT;
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															if(response.data.object==true)
																{
																	$scope.appointmentDate = item.appointmentDate;
																	$scope.slotId = item.slotId;
																	$scope.slotStatusId = 6;
																	$scope.priorityId  = 2;
																	$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
																	$scope.setSecondTabLevel(111);
																	angular
																	.element('#patientSearchForRegistration').modal('show');
																}else{
																	$scope.slotStatusId = 4;
																}

														});
									}
								
							}
							
							/*  $scope.$watch('datepicker',function(){
							      console.log($scope.datepicker);
							    })*/

							//Function For Set Value On Popup
							$scope.getSlotDetailsPopup = function(item) 
							{
								$(".patienttSearchResult").show();
								$scope.doctorName = item.doctorName;
								//$scope.specialityname = item.specialityName;
								$scope.fromTime = item.fromTimeString2;
								$scope.toTime = item.toTimeString2;
								$scope.appointmentDate = item.appointmentDate;
								$scope.appointmentDate2 = item.appointmentDate2;
								$scope.slotId = item.slotId;
								$scope.slotStatusId = 4;
								$scope.priorityId  = 1;
								$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
								$scope.slotTypeIdTemp = item.slotTypeId;
								if (!angular.equals({},$stateParams.appointmentObjectReschedule)) {
									$scope.regiType = "2"
									$scope.isTeleconsultationAppointment = $stateParams.appointmentObjectReschedule.isTeleconsultationAppointment!=null ? $stateParams.appointmentObjectReschedule.isTeleconsultationAppointment : 0;
								}else{
									$scope.regiType = "2";
									$scope.isTeleconsultationAppointment = 0;
									$scope.systemGeneratedVisitTypeId=null;
							    	$scope.userDefinedVisitTypeId=null;
								}
								$scope.fieldsDisabled=true;
								$scope.setSecondTabLevel(111);
								$scope.patientAppoObj={};
									
							}
							
							
							$scope.newPatientChange = function()
							{
								
								$scope.openDatePickerDOB1 = function() {
									$scope.datepickerDOB1.opened = true;
								};
								$scope.datepickerDOB1 = {
										opened : false
									};
								$scope.dateOptionsDob = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
									};

								
								//$scope.$broadcast('show-errors-check-validity');
								$(".patienttSearchResult").hide();
								$(".help-block").show();
								$scope.patientDetails.prefixId = "";
								$scope.patientDetails.firstName = "";
								$scope.patientDetails.middleName = "";
								$scope.patientDetails.lastName = "";
								$scope.patientDetails.age = "";
								$scope.patientDetails.birthDate = "";
								$scope.patientDetails.genderId = "";
								$scope.patientDetails.mobileNumber = "";
								$scope.patientDetails.email = "";
								$scope.patientDetails.remark = "";
								$scope.patientDetails.patientId = 0;
								$scope.patientDetails.bloodGroup = "";
								//$scope.datepickerDOB = new Date();
								
								$scope.patientList = [];
								
								$scope.regiType = "1";
								$scope.patientSearchObj = {
										"uhidNumber" : "",
										"patientName" : "",
										"genderId" : "",
										"mobileNumber" : "",
										"dob" : "",
										"identificationNumber" : "",
										"identificationTypeId" : "",
										"unitId" : $scope.unitId,
										"organizationId" : $scope.organizationId,
										"registrationTypeIdList" : [1,2,3,4,6]
									}
								
								$scope.fieldsDisabled = false;
								
								$scope.patientAppoObj.patientId=null;
								$scope.systemGeneratedVisitTypeId=(1).toString();
						    	$scope.userDefinedVisitTypeId=(1).toString();
									
							}
							$scope.registerPatientChange = function()
							{
								 $scope.otpvalidated = false;
								$scope.openDatePickerDOB = function() {
									$scope.datepickerDOB.opened = true;
								};
								$scope.datepickerDOB = {
										opened : false
									};
								$scope.dateOptionsDob = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
									};
								
								$(".patienttSearchResult").show();
								$(".help-block").hide();
								$scope.patientDetails.prefixId = "";
								$scope.patientDetails.firstName = "";
								$scope.patientDetails.middleName = "";
								$scope.patientDetails.lastName = "";
								$scope.patientDetails.age = "";
								$scope.patientDetails.birthDate = "";
								$scope.patientDetails.genderId = "";
								$scope.patientDetails.mobileNumber = "";
								$scope.patientDetails.email = "";
								$scope.patientDetails.remark = "";
								$scope.patientDetails.patientId = 0;
								$scope.patientDetails.bloodGroup ="";
								$scope.systemGeneratedVisitTypeId=null;
						    	$scope.userDefinedVisitTypeId=null;
								//$scope.datepickerDOB = new Date();
								
								$scope.regiType = "2";
								$scope.fieldsDisabled = true;
							}

							//Function For Select Slot
							$scope.getDoctorModalityList = function(type) {

								$scope.commonListDoctorModality = [];
								if (type == 'doctor') {
									$scope.specialityList = $scope.specialityMasterListCopy;
									$scope.selectedSlotId = "1";
								} else {
									
									var radioSpecialityObj = $.grep($scope.specialityMasterListCopy, function (doc) {
							            return doc.specialityId == $scope.radiologySpecialtyId;
							        });
									console.log("radioSpecialityObj",radioSpecialityObj);
									$scope.specialityList = radioSpecialityObj;
									console.log("$scope.specialityList",$scope.specialityList);
									$scope.spName = radioSpecialityObj[0].specialityName;
									
									$scope.selectedSlotId = "2";
								}
								
								
								$scope.doctorName = '';
								$scope.specialityName = '';
								$scope.modalityId = '';
								$scope.subSpecialityId = '';
								$scope.specialityId = '';
								$scope.allowedOverBookingSlots = 0;
								$scope.doctorId = '';
								$scope.specDoctorId = '';
								$scope.setThirdTabLevel(1);
								//$state.go('appointmentScheduling.year');
								$scope.yearSlotList=[];
								$scope.slotDetailsList = [];
								$scope.slotListMonth = [];
								$scope.weekArray = [];
								$scope.slotsListWeek = [];
								$scope.fromDateForWeek = "";
								$scope.subSpecialityList = [];
								$scope.modalityMaster = [];
								$scope.doctorList = [];
								$scope.newPatientChange();
							}

							//Function For Get Modality List by Multiple Modality Type Id
							$scope.getSubSpecialityBySpeciality = function(id) {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : id
								}
								console.log(data);
								//$scope.subSpecialityId ="";
								//alert('vdfv');
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ GETSUBSPECIALITYBYSPECIALITYID;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.subSpecialityList = [];

													if (response.data.status == "success") {
														$scope.subSpecialityList = response.data.listObject;
														$scope.commonListDoctorModality = [];
														$scope.yearSlotList = [];
														
														$scope.doctorName = '';
														$scope.specialityName = '';
														//$scope.modalityId = '';
														//$scope.subSpecialityId ='';
														$scope.allowedOverBookingSlots = 0;
														$scope.doctorId = '';
														//alert($scope.subSpecialityId);
														$scope.setThirdTabLevel(1);
														//$scope.setThirdTabLevel(1);
														//$state.go('appointmentScheduling.year');
														$rootScope.stopLoader();
													}
													
												});
							}
							
							
							$scope.getSubSpecialityBySpecialityReschedule = function(id) {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : id
								}
								console.log(data);
								//$scope.subSpecialityId ="";
								//alert('vdfv');
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ GETSUBSPECIALITYBYSPECIALITYID;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.subSpecialityList = [];

													if (response.data.status == "success") {
														$scope.subSpecialityList = response.data.listObject;
														//$scope.commonListDoctorModality = [];
														$scope.yearSlotList = [];
														
														$scope.doctorName = '';
														$scope.specialityName = '';
														//$scope.modalityId = '';
														//$scope.subSpecialityId ='';
														$scope.allowedOverBookingSlots = 0;
														$scope.doctorId = '';
														//alert($scope.subSpecialityId);
														$scope.setThirdTabLevel(4);
														//$scope.setThirdTabLevel(1);
														//$state.go('appointmentScheduling.year');
														$rootScope.stopLoader();
													}
													
												});
							}
							
							
							//Function For Get Modality List by Multiple Modality Type Id
							$scope.getModalityBySubSpeciality = function(id) {
								//alert('assa');
								var array = id.split('_');
								
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : array[0],
									subSpecialityId :array[1]
								}
								
								$scope.specialityId = array[0];
								$scope.subSpecialityId = array[1];
								console.log(data);
								$rootScope.startLoader();
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ GETMODALITYBYSUBSPECIALITYID;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.modalityMaster = [];
													$scope.commonListDoctorModality = [];


													if (response.data.status == "success") {
														$scope.modalityMaster = response.data.listObject;
														$scope.commonListDoctorModality =response.data.listObject;
														
														$scope.yearSlotList = [];
														$scope.setThirdTabLevel(1);
														//$state.go('appointmentScheduling.year');
														/*$scope.doctorName = '';
														$scope.specialityName = '';
														$scope.modalityId = '';
														$scope.subSpecialityId = '';
														$scope.specialityId = '';
														$scope.allowedOverBookingSlots = 0;
														$scope.doctorId = '';*/
														$rootScope.stopLoader();
													}

												});
							}
							
							$scope.getModalityBySubSpecialityReschedule = function(id) {
								//alert('assa');
								var array = id.split('_');
								
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : array[0],
									subSpecialityId :array[1]
								}
								
								$scope.specialityId = array[0];
								$scope.subSpecialityId = array[1];
								console.log(data);
								$rootScope.startLoader();
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ GETMODALITYBYSUBSPECIALITYID;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.modalityMaster = [];
													$scope.commonListDoctorModality = [];


													if (response.data.status == "success") {
														$scope.modalityMaster = response.data.listObject;
														$scope.commonListDoctorModality =response.data.listObject;
														
														$scope.yearSlotList = [];
														$scope.setThirdTabLevel(4);
														//$state.go('appointmentScheduling.year');
														/*$scope.doctorName = '';
														$scope.specialityName = '';
														$scope.modalityId = '';
														$scope.subSpecialityId = '';
														$scope.specialityId = '';
														$scope.allowedOverBookingSlots = 0;
														$scope.doctorId = '';*/
														$rootScope.stopLoader();
													}

												});
							}
							

							//Function For Get Slot By Doctor/Modality
							$scope.getSlotsUsingDoctorId = function(
									specDoctorId) {
								
								$scope.datepicker = new Date();
								var array = specDoctorId.split('_');
								if ($scope.selectedSlotId == 1) {
									$scope.getSlots(array[0], array[1],
											array[2], array[3],array[4]);
									
									
									//$scope.allowedOverBookingSlots = array[4];
								} else {
									$scope.getSlotsForModality(array[0],
											array[1], array[2],array[3], array[4]);
								}
							}

							//Function For Get Slot By Doctor
							$scope.getSlots = function(docId, specialityId,
									docName, specialityName,allowedOverBookingSlots) 
							{
								 $scope.datepicker = new Date();
								//alert(docId +'_'+ specialityId + '_'+docName+'_'+specialityName+'_'+$scope.selectedSlotId);
								$(".tabular").removeClass("activeTabular");
								$("#tabular" + docId).addClass("activeTabular");
								$scope.doctorName = docName;
								$scope.specialityName = specialityName;
								$scope.doctorId = docId;
								$scope.specDoctorId = docId +'_'+ specialityId + '_'+docName+'_'+specialityName+'_'+allowedOverBookingSlots;
								$('#specDoctorId').val($scope.specDoctorId);
								$scope.allowedOverBookingSlots = allowedOverBookingSlots;
								$scope.specialityId = specialityId;
								$scope.getSlotsOfDate();
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									
									doctorId : docId,
									specialityId : specialityId,
									appointmentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
								}
								if($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == false)
									{
									 data.slotTypeId = 4;	
									}
								else
									{
									 data.slotTypeId = $scope.selectedSlotId;
									}
								console.log(data);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.yearSlotList = [];

													if (response.data.status == "success") {
														$scope.yearSlotList = response.data.listObject;
														console.log($scope.yearSlotList);
														$scope.setThirdTabLevel(1);
														$state.go('appointmentScheduling.year');
														$rootScope.stopLoader();
													}

												});

								if ($scope.selectedSlotPeriod == "D") {
									$scope.getSlotsOfDate();
								}

							}
							$scope.getNephologyAsset= function(){
								
							}

							//Function For Get Slot By Modality
							$scope.getSlotsForModality = function(modId,subSpecId,specId,modName, subSpecialityName) {
							    $scope.datepicker = new Date();
								$(".tabular").removeClass("activeTabular");
								$("#tabular" + modId).addClass("activeTabular");
								$scope.doctorName = modName;
								$scope.specialityName = subSpecialityName;
								$scope.modalityId = modId;
								$scope.subSpecialityId = subSpecId;
								$scope.specialityId = specId;
								$scope.specDoctorId = modId+'_'+subSpecId+'_'+specId+'_'+modName+'_'+subSpecialityName;
								$('#specDoctorId').val($scope.specDoctorId);
								$scope.getSlotsOfDate();
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : $scope.selectedSlotId,
									modalityId : modId,
									subSpecialityId :$scope.subSpecialityId,
									specialityId :$scope.specialityId,
									appointmentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
								}
								console.log(data);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.yearSlotList = [];

													if (response.data.status == "success") {
														$scope.yearSlotList = response.data.listObject;
														console
																.log($scope.yearSlotList);
														$rootScope.stopLoader();
													}

												});

							}
//
							$scope.getSlotsForNephrology = function(bedId) {
								if(bedId > 0)
								//alert(modId +' '+ modTypeId+ ' '+ $scope.selectedSlotId);
								$scope.datepicker = new Date();
								$(".tabular").removeClass("activeTabular");
								$("#tabular" + bedId).addClass("activeTabular");
								$scope.specialityId = $scope.hemodialysisSpecialityId;
								$scope.bedId=bedId;
								$scope.getSlotsOfDate();
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : 8,
									bedId :$scope.bedId,
									specialityId :$scope.specialityId,
									appointmentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
								}
								console.log(data);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.yearSlotList = [];

													if (response.data.status == "success") {
														$scope.yearSlotList = response.data.listObject;
														console.log($scope.yearSlotList);
														$rootScope.stopLoader();
													}

												});

							}
							/*$scope.test = function()
							{
								//alert("xcd");
							}*/

							/******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
							/*First Level*/
							$scope.firstTabLevel = 1;

							$scope.setFirstTabLevel = function(newTab) {
								if($scope.fromCollectScreen == false){
								$scope.newPatientChange();
								if (newTab == 1) {
									/*$("#case-sheet").css("background-color",
											"#5F9EA0");
									$("#complaints").css("background-color",
											"#5ab7c8");*/
									$scope.slotTypeId = 1;
									$scope.yearSlotList = [];
									$scope.slotListMonth = [];
									$scope.slotsListWeek = [];
									$scope.slotDetailsList = [];
									$scope.slotDetailsList = [];
									$scope.commonListDoctorModality = [];
									$scope.weekArray = [];
									$scope.specialityName = '';
									$scope.fromDateForWeek = '';
									$scope.subSpecialityList = [];
									$scope.modalityMaster = [];
									$scope.doctorList = [];
									$scope.doctorName = '';
									$scope.specialityId = '';
									$scope.specDoctorId = '';
									$scope.subSpecialityId = '';
									
									$scope.setThirdTabLevel(1);
									$scope.selectedSlotId = 1;
								} else if (newTab == 2) {

									/*$("#case-sheet").css("background-color",
											"#5ab7c8");
									$("#complaints").css("background-color",
											"#5F9EA0");*/
									//alert("dv");
									$scope.secondTabLevel = 1;
									$scope.datepicker = new Date();
									$scope.slotTypeId = 2;
									 $scope.selectedSlotId = 3;
									var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.selectedSlotId,
											appointmentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
										}
										console.log(data);
										var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTS;
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															$scope.yearSlotList = [];

															if (response.data.status == "success") {
																$scope.yearSlotList = response.data.listObject;
																console
																		.log($scope.yearSlotList);
															}

														});
									
										$scope.slotListMonth = [];
										$scope.slotsListWeek = [];
										$scope.slotDetailsList = [];
										$scope.commonListDoctorModality = [];
										$scope.weekArray = [];
										$scope.specialityName = "";
										$scope.fromDateForWeek = "";
										$scope.subSpecialityList = [];
										$scope.modalityMaster = [];
										$scope.doctorList = [];
										$scope.doctorName = "";
										$scope.specialityId = null;
										$scope.specDoctorId = "";
										$scope.subSpecialityId = "";
										$scope.getSlotsOfDate();
									//$scope.getECHSlots(3);
								}
								if (newTab == 4) {
									/*$("#case-sheet").css("background-color",
											"#5F9EA0");
									$("#complaints").css("background-color",
											"#5ab7c8");*/
									
									$scope.slotTypeId = 8;
									$scope.yearSlotList = [];
									$scope.slotListMonth = [];
									$scope.slotsListWeek = [];
									$scope.slotDetailsList = [];
									$scope.slotDetailsList = [];
									$scope.commonListDoctorModality = [];
									$scope.weekArray = [];
									$scope.specialityName = '';
									$scope.fromDateForWeek = '';
									$scope.subSpecialityList = [];
									$scope.modalityMaster = [];
									$scope.doctorList = [];
									$scope.doctorName = '';
									$scope.specialityId = '';
									$scope.specDoctorId = '';
									$scope.subSpecialityId = '';
									
									$scope.setThirdTabLevel(1);
									//$scope.selectedSlotId = 4;
									$scope.selectedSlotId = 8;
								}
							}
								$scope.firstTabLevel = newTab;
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
							};

							//Second Level
							$scope.secondTabLevel = 1;

							$scope.setSecondTabLevel = function(newTab) {
								$scope.secondTabLevel = newTab;
								//alert('Day!!!!');
								$scope.dayDate = moment(new Date()).format();
								
								
							};

							$scope.leftArrow = function() {
								//alert("left arrow");
								$scope.dayDate = moment($scope.dayDate).add(
										'days', -1).format();
							}

							$scope.rightArrow = function() {
								//alert("right arrow");
								$scope.dayDate = moment($scope.dayDate).add(
										'days', 1).format();
							}

							$scope.isSetSecondTabLevel = function(tabNum) {
								return $scope.secondTabLevel === tabNum;
							};

							//Third Level
							$scope.thirdTabLevel = 1;

							$scope.setThirdTabLevel = function(newTab) {
								$scope.thirdTabLevel = newTab;
								//$scope.getSlotsOfDate();
							};

							$scope.isSetThirdTabLevel = function(tabNum) {
								return $scope.thirdTabLevel === tabNum;
							};

							//FORTH Level
							$scope.forthTabLevel = 1;

							$scope.setForthTabLevel = function(newTab) {
								$scope.forthTabLevel = newTab;
							};

							$scope.isSetForthTabLevel = function(tabNum) {
								return $scope.forthTabLevel === tabNum;
							};
							/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							/*$scope.getECHSlots(selecteSlotId)
							{

								
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										slotTypeId :selecteSlotId
									}
									console.log(data);
									var URI = BASE_URL + ROOT_URL + GETYEARLYSLOTSFORECH;
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														$scope.yearSlotListECH = [];

														if (response.data.status == "success") {
															$scope.yearSlotListECH = response.data.listObject;
															console
																	.log($scope.yearSlotListECH);
														}

													});
									
							}*/
							// for getting gender by prefix start
							$scope.selectGender = function(id)
							{
								
								var genId = $.grep($scope.prefixList, function (doc) {
				    	            return doc.prefixId == id;
				    	        })[0].id;
								
								$scope.patientDetails.genderId = genId.toString();
								
							}
							// for getting gender by prefix end
							//for getting DOB by age start
							$scope.myFunct = function(age) {
								
								 if($('#datepickerDOB').val()!=""){
									 
									 var dt = moment($scope.datepickerDOB).format('MM/DD/YYYY');
										var newAgeE = $rootScope.getAge(dt);
										  var tArr = dt.split("/");
                                          var m = tArr[0];
                                          var d=tArr[1];
										if(newAgeE != age)
										{
										var currentYear = (new Date()).getFullYear();
									    var finalYear = currentYear - age;		
									    var mon=$scope.ageMonth
                                        var dat=$scope.ageDay
                                        $scope.datepickerDOB = new Date(m +'/' +d +'/'  +finalYear);
									    $('#searchServiceId').val('');
										}
									}else{
										var currentYear = (new Date()).getFullYear();
									    var finalYear = currentYear - age;
									    $scope.datepickerDOB = new Date(m +'/' +d +'/'  +finalYear);
									    $('#searchServiceId').val('');
									}
								/*var currentYear = (new Date()).getFullYear();
							    var finalYear = currentYear - age;
							    //alert(finalYear);
							    $scope.datepickerDOB = new Date("01/01/"+finalYear);*/
							}
							//for getting DOB by age end
							$scope.updateAbscondPatient=function(patientId){
								// update abscond patient as normal patient start
								if(patientId==0 || patientId==undefined || patientId==null){
									return false;
								}
								if($scope.abscondPatientFlag!=undefined  && $scope.abscondPatientFlag!="" && $scope.abscondPatientFlag!=null && $scope.abscondPatientFlag=='Y'){
								var URI = BASE_URL + ROOT_URL+ "/api/global/updatePatientAsAbscound";					
								var obj={
										patientId:patientId
								}		
								console.log('abscond obj',obj);
//								return false;
								GenericService.serviceAction("POST", URI, obj).then(
										function(response) {	
											if (response.data.status == "success") {
												$scope.abscondPatientFlag="";
											}
										});
								}
								// update abscond patient as normal patient end
							}

							//Save Appointment Schduling
							$scope.saveAppointmentSchedule = function() {
								$scope.$broadcast('show-errors-check-validity');
								
								if($scope.patientDetails.prefixId=='' || $scope.patientDetails.prefixId==undefined){
									growl.warning("Prefix is required",{
										title : "warning!!!"
									});
									$scope.setSecondTabLevel(111);
									return false;
								}else if($scope.patientDetails.firstName=='' || $scope.patientDetails.firstName==undefined){
									growl.warning("First name is required",{
										title : "warning!!!"
									});
									$scope.setSecondTabLevel(111);
									return false;
								}else if($scope.patientDetails.genderId=='' ||$scope.patientDetails.genderId==undefined){
									growl.warning("Gender is required",{
										title : "warning!!!"
									});
									$scope.setSecondTabLevel(111);
									return false;
								}else if($scope.patientDetails.mobileNumber=='' ||$scope.patientDetails.mobileNumber==undefined){
									growl.warning("Mobile number is required",{
										title : "warning!!!"
									});
									$scope.setSecondTabLevel(111);
									return false;
								}
								else if($scope.datepickerDOB=='' ||$scope.datepickerDOB==undefined){
									growl.warning("Date of Birth  is required",{
										title : "warning!!!"
									});
									$scope.setSecondTabLevel(111);
									return false;
								}
								
//								if ($scope.modalityForm.$valid){
//									
//								}else{
//						        	growl.warning(" Please add required field",{
//										title : WARNING_MSG
//									});
//						        	return false;
//						        }
	                         		 if( $scope.otpvalidated == false ){
	                         			 
	                         		 }else{
	                         			growl.warning("Please validate email Id ",{
											title : "warning!!!"
										});
	                         			$scope.setSecondTabLevel(111);
										return false;
	                         		 }
								
                                  if( $scope.otpvalidatedMobile == false){
	                         			 
	                         		 }else{
	                         			growl.warning("Please validate Mobile Number ",{
											title : "warning!!!"
										});
	                         			$scope.setSecondTabLevel(111);
										return false;
	                         		 }
//								if (!$scope.modalityForm.$valid)
//								{ 
//									//$scope.showModal = !$scope.showModal;
//								} else{
									
								
								if($scope.patientDetails.mobileNumber == '' || $scope.patientDetails.mobileNumber==undefined){
//					        		$scope.tab=2;
		        				    growl.error(PHONE_NUMBER_MSG,{
										title : ERROR_MSG
									});
		        				    $('#mobileNumber').focus();
		        				    return false;
					        	}
								if($scope.patientDetails.mobileNumber != ''){
					        		
					        		 var val = $scope.patientDetails.mobileNumber;
					        		 if($scope.patientDetails.mobileNumber.length==parseInt($scope.registrationFieldObj.mobileDigit)){
						        			
										}else{
											growl.warning("Please enter valid mobile number.",{
												title : "warning"
												});
												$('#mobileNumber').focus();
												return false;
					        		}
				        		
					        	}
								if($scope.patientDetails.email != "" &&  $scope.patientDetails.email != undefined){
									 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
					           		 var email = $scope.patientDetails.email;
					           		    if (!filter.test(email)) {
				        				    growl.warning("Please enter valid email Id.",{
												title : "warning"
											});
				        				    return false;
					           		    }
								}
//								return false;
//								if ($scope.popUpFlag) {
//									$scope.showModal = !$scope.showModal;
//
//								} else {
									$rootScope.startLoaderPopup();
									$scope.btnDisabled = true;
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');

									
									angular.forEach($scope.specialityList,function(value,key){
										if($scope.specialityId == value.specialityId){
											$scope.spName = value.specialityName
										}
									})
									var data = {
										slotId : $scope.slotId,
										reasonForAppointmentId : $scope.patientDetails.reasonForAppointmentId,
										remark : $scope.patientDetails.remark,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										createdDate : $scope.currentDate,
										updatedDate : $scope.currentDate,
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										status : 'A',
										appointmentTypeId : $scope.selectedSlotId,
										slotStatusId : $scope.slotStatusId,
										priorityId : $scope.priorityId,
										allowedOverBookingSlots : $scope.allowedOverBookingSlots,
										firstName : $scope.patientDetails.firstName,
									    mobileNumber :$scope.patientDetails.mobileNumber,
									    doctorName :$scope.doctorName,
									    fromTimeString : $scope.fromTime,
									    appointmentDate2 : $scope.appointmentDate,
									    doctorId : $scope.doctorId,
									    specialityId : ($scope.patientAppoObj.specialityId==undefined) ? $scope.specialityId : $scope.patientAppoObj.specialityId,//ganesh
									    isTeleconsultationAppointment:$scope.isTeleconsultationAppointment,
									    specialityName: $scope.spName 
									}
									
									if($scope.isKiosk == 'N')							
									{
										data.generateToken = 'Y';
										//data.appointmentDate = $scope.appointmentDate;
									}
									/*else{
										data.appointmentDate = $scope.appointmentDate;
									}*/
									
									if($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == false)
									{
										data.appointmentTypeId =1;
									}
								
									
									if($scope.selectedSlotId == '3')
										{
											data.ehcPackageId = $scope.patientDetails.ehcPackageId;
											data.serviceId =$scope.patientDetails.serviceMasterId;
										}
								
									if($scope.selectedSlotId == '4')
									{
										data.appointmentTypeId = 4;
									}
								
									if($scope.selectedSlotId == 8)
									{
										data.appointmentTypeId = 8;
									}
									
									if($scope.regiType == '2' && $scope.patientDetails.patientId == 0)
										{
											growl.error("Please Select Patient!",{
												title : ERROR_MSG
											});
											return false;
										}
									else 
										{
											data.patientId = $scope.patientDetails.patientId;
											
										}
									
									if($scope.regiType == '1')
										{
											data.prefixId = $scope.patientDetails.prefixId;
											data.firstName = $scope.patientDetails.firstName;
											data.middleName = $scope.patientDetails.middleName;
											data.lastName =$scope.patientDetails.lastName;
											data.dob =moment($scope.datepickerDOB).format('DD-MM-YYYY HH:mm:ss');
											data.genderId =$scope.patientDetails.genderId;
											data.mobileNumber =$scope.patientDetails.mobileNumber;
											data.email =$scope.patientDetails.email;
											data.bloodGroup =$scope.patientDetails.bloodGroup;
											data.registrationTypeId = 5;
											data.patientId = 0;
										}

									if ($scope.isReshedule == 'N') {
										var URI = BASE_URL + ROOT_URL
												+ SCHEDULEAPPOINTMENT;
									} else {

										data.previousSlotId = $scope.previousSlotId;
										data.appointmentId = $scope.appointmentId;
										data.rescheduleReasonId = $scope.rescheduleReasonId;
										data.rescheduledBy = $scope.reschedulledBy;
										data.orderDetailsId =$scope.rescheduleCookiesObj.orderDetailsId;

										var URI = BASE_URL + ROOT_URL
												+ RESCHEDULEAPPOINTMENT;
									}

									console.log(data);
//									return false;
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														console.log(response);
														//$rootScope.startLoader();
														if (response.data.status == "success") {
															$scope.updateAbscondPatient($scope.patientDetails.patientId);
															$scope.rescheduleCookiesObj = {};
															$stateParams.appointmentObjectReschedule = {};
															angular
																	.element(
																			'#patientSearchForRegistration')
																	.modal(
																			'hide');
															$scope.newPatientChange();
															$rootScope.stopLoaderPopup();

															//alert(item.date);
															// $cookies.putObject('cookieObjectReschedule',null);
															//$cookies.remove("cookieObjectReschedule");
															
															if(response.data.object!=null){
															var data =  {
													                 "appName": "HIS",
													                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
													                 "reportDesignFilePath": "OPDAppointmentSlip.rpt",
													                 "reportFormat": "PDF",
													                 "exportFileName": "appointmentSleep",
													                 "reportParams": [
													                   {
													                  "paramName": "@appointment_id",
													                  "paramType": "Integer",
													                  "paramValue": response.data.object.appointmentId
													                }
													                 ]
													      };
															reportService.generate(REPORT_API_URI1,data);
															}
															$timeout(
																	function() {
																		$state.go('appointmentScheduling',{appointmentObjectReschedule:{}}, {reload: true});
																	}, 1000);
															
															
														}
														else
															{
																angular
																.element(
																		'#patientSearchForRegistration')
																.modal(
																		'hide');
																$rootScope.stopLoader();
																$scope.btnDisabled = false;
															    }
														   $scope.newPatientChange();
														   $rootScope.stopLoaderPopup();

													});

//									}
//								}
							}
							
							$scope.confirmPopForCollect = function(){
								
								//alert("ss"+$scope.selectedSlotId);
								//alert("at"+$scope.appointmentTypeId);
								if($scope.selectedSlotId == 1 && $scope.selectType=='doctor'){
								angular.element('#confirmPopCollectPay').modal('show');
								}
								else{
									$scope.saveAppointmentSchedule();
								}
							}
							
							$scope.getMonthWiseSlot = function(monthDate,typeFlag) 
							{
								$scope.slotListMonth = [];
								$scope.setThirdTabLevel(2);
								
								if(typeFlag == 'M')
									{
									 var copyright = new Date();
									 var year = copyright.getFullYear();
									 var month = copyright.getMonth()+1;
									
									}
								else
									{
										var arr = monthDate.split('-');
										 var year = arr[1];
										 var month = arr[0];
									}
								
								var data = {
									"organizationId" : $scope.organizationId,
									"unitId" : $scope.unitId,
									"slotTypeId" : $scope.selectedSlotId,
									"month" : month,
									"year" : year,

								}
								
								if($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == false)
								{
									data.slotTypeId = 4;
									data.specialityId = $scope.specialityId;
									data.bedId = $scope.bedId;
								}
							else 
								{
							       //	data.slotTypeId = $scope.selectedSlotId;
							       
										data.slotTypeId = $scope.selectedSlotId;
									
									
								}
								
								if ($scope.selectedSlotId == 1 && $scope.deptWiseAppointmentFlag == true) {
									
									data.doctorId = $scope.doctorId;
									data.specialityId = $scope.specialityId;
									data.bedId = $scope.bedId;
									
								}
						
								else if($scope.selectedSlotId == 8){
									data.slotTypeId = 8;
									data.bedId = $scope.bedId;
									data.specialityId = $scope.hemodialysisSpecialityId;
									$scope.secondTabLevel = 2;
								}
								else if($scope.selectedSlotId == 2)
								{
											data.specialityId = $scope.specialityId;
											data.subSpecialityId = $scope.subSpecialityId;
											data.modalityId = $scope.modalityId;
											data.bedId = $scope.bedId;
								}
								else
									{
									$scope.secondTabLevel = 2;
									}
								
								//console.log($scope.dayList);
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL + GETMONTHLYSLOTS;

								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													//$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success") {
														$scope.slotListMonth = response.data.listObject;
														console.log($scope.slotListMonth);
														$scope.weekArray = [];
														for(var i=1;i <= response.data.listObject.length;i++)
															{
																var weekArr ={};
																weekArr.id = i;
																weekArr.name = "w"+i;
																$scope.weekArray.push(weekArr);
															}
														console.log('week');
														console.log($scope.weekArray);
														
														//$scope.firstDate = response.data.listObject[0][0];
														//$scope.lastDate = response.data.listObject[response.data.listObject.length][0];
														//alert(response.data.listObject[response.data.listObject.length][0]);
														//alert("d"+response.data.listObject[0][0]);
														//console.log("first:"+console.log(response.data.listObject[0][0]));
														//console.log("last:"+console.log(response.data.listObject[response.data.listObject.length][0]));

														 var now = new Date(year,month,1);
														 $scope.lastDate  = new Date(now.getFullYear(), now.getMonth(), 0);
														 $scope.firstDate = new Date(year,month-1,1);
													}

												});

							}
							$scope.checkDateIsBefore = function(slobObj)
							{
							     var endTime = moment(slobObj.originalFromTime, 'hh:mm');
							     var startTempTime = moment().format("HH:mm");
							     var startTime = moment(startTempTime, 'hh:mm');
							     
								 if(moment(slobObj.appointmentDate2).isBefore(moment().format('YYYY-MM-DD'))		 
									&&
										 (slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize >= '0'))
								 {
									 return 'bg-grey cursor-pointer day-slot-div';
								 }
								 
								  if(endTime.isBefore(startTime) && moment(slobObj.appointmentDate2).isSame(moment().format('YYYY-MM-DD'))
										  &&(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize >= '0'))
									 {
									   return 'bg-grey cursor-pointer day-slot-div';
									 }
								 else{
								
									
									if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize == '0')
									{
										return 'bg-green cursor-pointer day-slot-div';
									}else if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > '0' && slobObj.isVip=='Y')
									{
										//return 'bg-orange cursor-pointer day-slot-div';
										return 'bg-red1 cursor-pointer day-slot-div';
									}
									else if(slobObj.isBlockedUnblocked == '2' && slobObj.allowedOverBookingSlots == '0' && slobObj.slotPatientDetailsListsSize > '0')
									{
										//return 'bg-orange cursor-pointer day-slot-div';
										return 'bg-red1 cursor-pointer day-slot-div';
									}else if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > '0' && slobObj.slotPatientDetailsListsSize < slobObj.allowedOverBookingSlots)
									{
										//return 'bg-red1 cursor-pointer day-slot-div';
										return 'bg-orange cursor-pointer day-slot-div';
									}else if(slobObj.isBlockedUnblocked == '2' && slobObj.allowedOverBookingSlots > '0' && slobObj.slotPatientDetailsListsSize == slobObj.allowedOverBookingSlots)
									{
										//return 'bg-orange cursor-pointer day-slot-div';
										return 'bg-red1 cursor-pointer day-slot-div';
									}else if(slobObj.isBlockedUnblocked == '1' && slobObj.slotPatientDetailsListsSize == '1')
									{
										return 'bg-grey cursor-pointer day-slot-div';
									}else if(slobObj.isBlockedUnblocked == '1' && slobObj.slotPatientDetailsListsSize == '0')
									{
										return 'bg-grey cursor-pointer day-slot-div';
									}
								}
								
							}
							
							//$scope.ehcFlagColor = true;
							$scope.checkDateIsBeforeEHC = function(slobObj)
							{
							  
							   
							   var alignFillDate = slobObj.appointmentDate2;
							   var pickUpDate = moment(new Date()).format('YYYY-MM-DD');
							   
							   if (pickUpDate < alignFillDate) {
								    return 'false';
								  }
							   else if (pickUpDate > alignFillDate){
								   return 'true';
							   }
							   else{
								   return 'false';
							   }
							  
								 
								
							}
							
							$scope.getWeekWiseSlots = function(weekId)
							{
								//$scope.slotDetailsList =  [];
								$scope.setThirdTabLevel(3);
								$scope.currentTime = moment().format('HH:mm');
									
								$scope.wkId = weekId;
								
								console.log("ff",$scope.slotListMonth[weekId-1]);
								//alert($scope.slotListMonth[weekId-1].length);
								//alert($scope.slotListMonth[weekId-1][0]);
								//alert($scope.slotListMonth[weekId-1][$scope.slotListMonth[weekId-1].length-1]);
								var arrFrom =$scope.slotListMonth[weekId-1][0].split('_');
								$scope.fromDateForWeek = arrFrom[1];
								
								var arrTo =$scope.slotListMonth[weekId-1][$scope.slotListMonth[weekId-1].length-1].split('_');
								$scope.toDateForWeek = arrTo[1];
								
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										
										slotTypeId:$scope.selectedSlotId,
										fromDate:$scope.fromDateForWeek,
										toDate:$scope.toDateForWeek
								}
								
								if($scope.selectedSlotId == '1' && $scope.deptWiseAppointmentFlag == false)
								{
									data.slotTypeId = 4;
									data.specialityId = $scope.specialityId;
								}
							else
								{
										data.slotTypeId = $scope.selectedSlotId;
									
								}
								
								
								if($scope.selectedSlotId == 1 && $scope.deptWiseAppointmentFlag == true)
									{
										data.doctorId=$scope.doctorId;
										data.specialityId=$scope.specialityId;
									
									}
								else if($scope.selectedSlotId == 2 )
									{
									
										data.specialityId = $scope.specialityId;
										data.subSpecialityId = $scope.subSpecialityId;
										data.modalityId = $scope.modalityId;
									
									}
								else if($scope.selectedSlotId == 8 ){
									data.slotTypeId = 8;
									data.bedId = $scope.bedId;
									data.specialityId = $scope.hemodialysisSpecialityId;
									$scope.secondTabLevel = 3;
								}
								else
								{
								$scope.secondTabLevel = 3;
								}
							
								
								console.log(data);
								//return false;
								
								var URI = BASE_URL+ ROOT_URL+ GETWEEKLYSLOTS;
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											//$scope.slotDetailsList = [];
											// $rootScope.startLoader();
											if (response.data.status == "success") {
												$scope.slotsListWeek = response.data.listObject;
												console.log($scope.slotsListWeek);
											}

										});
							
							}
							
							$scope.changeClass = function(colorFlag,weekId,co,appoitDate)
							{
								console.log(colorFlag,weekId,co);
								 var pickUpDate = moment(new Date()).format('YYYY-MM-DD');
								if(colorFlag == 'Y' && pickUpDate <= appoitDate )
									{
									$('#color'+co+weekId).addClass("bg-green");
									}else
										{
										$('#color'+co+weekId).addClass("bg-grey");
										}
								
								
								 
							}
							
							$scope.changeClassTemp = function(colorFlag,weekId,co,appoitDate)
							{
								/*console.log(colorFlag,weekId,co);
								 var pickUpDate = moment(new Date()).format('YYYY-MM-DD');
								if(colorFlag == 'Y' && pickUpDate <= appoitDate )
									{
										return "bg-green";
									}else
										{
										return "bg-grey";
										}*/
								console.log(colorFlag,weekId,co);
								 var pickUpDate = moment(new Date()).format('YYYY-MM-DD');
								if(colorFlag == 'Y' && pickUpDate <= appoitDate )
									{
									$('#color'+co+weekId).addClass("bg-green");
									}else
										{
										$('#color'+co+weekId).addClass("bg-grey");
										}
								
								 
							}

							////Function For Set Value on assign patient on appointement
							$scope.getBookSlotDetails = function(item) 
							{
								$scope.openScheduling = false;
								
								$scope.patientName = item.patientName;
								$scope.mobileNumber = item.mobileNumber;
								$scope.email = item.email;
								$scope.address = item.address;
								
								$scope.bookedPatientList = item.slotPatientDetailsList;
								console.log($scope.bookedPatientList);
							}
							
							
							$scope.closeDetailsPopUp = function() 
							{
								$scope.openScheduling = true;
							}
							
							
							//for get cookie object from appointment list
							if (!angular.equals({},$stateParams.appointmentObjectReschedule)) {
								//alert('sc');
								$scope.rescheduleCookiesObj = $stateParams.appointmentObjectReschedule;
								console.log("ssssssssssssssssssssss",$scope.rescheduleCookiesObj);

								$scope.selectType = $scope.rescheduleCookiesObj.selectType;
								$scope.selectedSlotPeriod = "D";
								$scope.selectedSlotId = $scope.rescheduleCookiesObj.selectedSlotId;
								$scope.isPaidAppointment = $scope.rescheduleCookiesObj.isPaidAppointment;

								if ($scope.selectedSlotId == 1) {

									$scope.specialityId = $scope.rescheduleCookiesObj.specialityId
											.toString();

									$scope.getDoctorBySpecialityReschedule($scope.specialityId);

									$scope.specDoctorId = $scope.rescheduleCookiesObj.doctorId
											+ '_'
											+ $scope.rescheduleCookiesObj.specialityId
											+ '_'
											+ $scope.rescheduleCookiesObj.doctorName
											+ '_'
											+ $scope.rescheduleCookiesObj.specialityName + '_'+$scope.rescheduleCookiesObj.allowedOverBookingSlots;
									$scope.doctorId = $scope.rescheduleCookiesObj.doctorId;

									$timeout(
											function() {
												$(".tabular").removeClass(
														"activeTabular");
												$("#tabular"+ $scope.doctorId)
														.addClass(
																"activeTabular");
												$('#datepicker')
														.val(
																$scope.rescheduleCookiesObj.rescheduleAppointmentDate);
												//$scope.datepicker = new Date($scope.rescheduleCookiesObj.rescheduleAppointmentDate);
												var array = $scope.rescheduleCookiesObj.rescheduleAppointmentDate.split('/');
												var alignFillDate = "";
												alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
												$scope.datepicker = new Date(alignFillDate);
												if($scope.rescheduleCookiesObj.deptWiseAppointment=='Y'){
												    $scope.deptWiseAppointmentFlag = false;
												}
												$scope.getSlotsOfDate();
											}, 500);
									
									var  data = {
									    	
								    		 unitId:$scope.unitId,
											 organizationId  :$scope.organizationId,
								    		 patientId : $scope.patientDetails.patientId,
								    		 specialityId : $scope.specialityId,
								    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
								    		 
								     };
									if($scope.rescheduleCookiesObj.deptWiseAppointment!=null ? $scope.rescheduleCookiesObj.deptWiseAppointment : $scope.deptWiseAppointment != null ? $scope.deptWiseAppointment : 'N'=="N")
										data.doctorId = $scope.doctorId
									else	
										data.doctorId = 0;
									
									 var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
								     BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								     {
								    	 $scope.systemGeneratedVisitTypeId=response.data.toString();
								    	 $scope.userDefinedVisitTypeId=response.data.toString();
								     });
									
									

								} else  if ($scope.selectedSlotId == 2)  {
									
									//$scope.modalityTypeId = $scope.rescheduleCookiesObj.modalityTypeId.toString();

									//$scope.getModalityByModalityType($scope.modalityTypeId);
									
									
									$scope.specialityId = $scope.rescheduleCookiesObj.specialityId.toString();
									
									$scope.getSubSpecialityBySpecialityReschedule($scope.specialityId);
									
									
									
									
									var subSpe =  $scope.rescheduleCookiesObj.specialityId+'_'+ $scope.rescheduleCookiesObj.modalitySubSpecialityId;
									
									$scope.subSpecialityId =subSpe;
									
									$scope.getModalityBySubSpecialityReschedule(subSpe);

									$scope.specDoctorId = $scope.rescheduleCookiesObj.modalityId
											+ '_'
											+ $scope.rescheduleCookiesObj.modalitySubSpecialityId
											+ '_'
											+ $scope.rescheduleCookiesObj.specialityId
											+ '_'
											+ $scope.rescheduleCookiesObj.modalityDesc+'_'+$scope.rescheduleCookiesObj.subSpecialityName;
									
									//info.modalityId+'_'+info.subSpecialityId+'_'+info.specialityId+'_'+info.modalityDesc+'_'+info.subSpecialityName
									
									
									$scope.modalityId = $scope.rescheduleCookiesObj.modalityId;

									$timeout(
											function() {
												$(".tabular").removeClass(
														"activeTabular");
												$("#tabular"+ $scope.rescheduleCookiesObj.modalityId)
														.addClass(
																"activeTabular");
												$('#datepicker')
														.val(
																$scope.rescheduleCookiesObj.rescheduleAppointmentDate);
												//$scope.datepicker = new Date($scope.rescheduleCookiesObj.rescheduleAppointmentDate);
												var array = $scope.rescheduleCookiesObj.rescheduleAppointmentDate.split('/');
												var alignFillDate = "";
												alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
												$scope.datepicker = new Date(alignFillDate);
												$scope.getSlotsOfDate();
												$scope.setThirdTabLevel(4);
//												/$scope.specialityId = array[0];
												$scope.subSpecialityId = $scope.specialityId+'_'+$scope.subSpecialityId;
											}, 500);

								}
								else if ($scope.selectedSlotId == 3) 
									{
									$scope.firstTabLevel = 2;
									$scope.secondTabLevel = 4;
									$timeout(
											function() {
												$('#datepicker')
														.val(
																$scope.rescheduleCookiesObj.rescheduleAppointmentDate);
												//$scope.datepicker = new Date($scope.rescheduleCookiesObj.rescheduleAppointmentDate);
												var array = $scope.rescheduleCookiesObj.rescheduleAppointmentDate.split('/');
												var alignFillDate = "";
												alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
												$scope.datepicker = new Date(alignFillDate);
												$scope.getSlotsOfDate();
											}, 500);
										// for getting selected ehc pkg when we reschedule the appointment
										 if( $scope.rescheduleCookiesObj.serviceId!=undefined &&  $scope.rescheduleCookiesObj.serviceId>0 ){
											 var data={
													 patientTypeId: $scope.rescheduleCookiesObj.patientCategoryId !=null && $scope.rescheduleCookiesObj.patientCategoryId !=undefined ?  $scope.rescheduleCookiesObj.patientCategoryId : $scope.defaultPatientCategoryId!=null && $scope.defaultPatientCategoryId!=undefined ? $scope.defaultPatientCategoryId : 1,
													 serviceId:$scope.rescheduleCookiesObj.serviceId,
													 visitTypeId : 1,//default op
													 billingBedCategoryId : $scope.opdBillingCatId,//default op
												     paymentEntitlementId : 1,
												     unitId          :$scope.unitId,
												     orgId  :$scope.organizationId,
											 }
											 var URI = BASE_URL + ROOT_URL + "/api/packages/getPackageRate";
												BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
													if (response.data.status == "success") {
														$scope.packageResponse=response.data.listObject[0];													
														$scope.patientDetails.ehcPackageId = $scope.packageResponse.packageMasterId;
														$scope.patientDetails.serviceMasterId =  $scope.packageResponse.serviceMasterId;
														$scope.search=$scope.packageResponse.serviceStandardName;												
														console.log("pkg response",$scope.packageResponse);
													}
												});
										 }
									}else if($scope.selectedSlotId == 8){
										$scope.firstTabLevel = 4;
										$scope.secondTabLevel = 4;
										$scope.hemodialysisSpecialityId = $scope.rescheduleCookiesObj.specialityId.toString();
										$scope.bedId=$scope.rescheduleCookiesObj.bedId;
										$timeout(
												function() {
													$(".tabular").removeClass("activeTabular");
													$("#tabular" + $scope.bedId).addClass("activeTabular");
													$('#datepicker')
															.val(
																	$scope.rescheduleCookiesObj.rescheduleAppointmentDate);
													//$scope.datepicker = new Date($scope.rescheduleCookiesObj.rescheduleAppointmentDate);
													var array = $scope.rescheduleCookiesObj.rescheduleAppointmentDate.split('/');
													var alignFillDate = "";
													alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
													$scope.datepicker = new Date(alignFillDate);
													$scope.getSlotsOfDate();
												}, 500);
									}

								$scope.doctorName = $scope.rescheduleCookiesObj.doctorName;
								$scope.specialityName = $scope.rescheduleCookiesObj.specialityName;

								$scope.patientDetails.prefixId = ($scope.rescheduleCookiesObj.prefixId
										.toString());
								$scope.patientDetails.firstName = ($scope.rescheduleCookiesObj.firstName);
								$scope.patientDetails.middleName = ($scope.rescheduleCookiesObj.middleName);
								$scope.patientDetails.lastName = ($scope.rescheduleCookiesObj.lastName);
								$scope.patientDetails.age = ($scope.rescheduleCookiesObj.age);
								$scope.patientDetails.bloodGroup = ($scope.rescheduleCookiesObj.bloodGroup);
								$scope.patientDetails.dob = $scope.rescheduleCookiesObj.dob;
								$scope.datepickerDOB = new Date($scope.rescheduleCookiesObj.dob);
								$scope.patientDetails.genderId = ($scope.rescheduleCookiesObj.genderId
										.toString());
								$scope.patientDetails.uhIdNumber = ($scope.rescheduleCookiesObj.uhIdNumber);
								$scope.patientDetails.mobileNumber = ($scope.rescheduleCookiesObj.mobileNumber);
								$scope.patientDetails.email = ($scope.rescheduleCookiesObj.email);
								$scope.patientDetails.patientId = ($scope.rescheduleCookiesObj.patientId);
								$scope.patientDetails.remark = $scope.rescheduleCookiesObj.remark;
								$scope.regiType = "2";
								$scope.fieldsDisabled = true;

								$scope.fieldsDisabled = true;
                               console.log($scope.rescheduleCookiesObj);
								$scope.isReshedule = 'Y';

								$scope.previousSlotId = $scope.rescheduleCookiesObj.slotId;
								$scope.appointmentId = $scope.rescheduleCookiesObj.appointmentId;
								$scope.rescheduleReasonId = $scope.rescheduleCookiesObj.rescheduleReasonId;
								$scope.reschedulledBy = $scope.rescheduleCookiesObj.reschedulledBy;
								
								
		//RESCHEDULEAPPOINTMENT						
								
								$timeout(
										function() {
								if($scope.selectedSlotId == 2){
									var radioSpecialityObj = $.grep($scope.specialityMasterListCopy, function (doc) {
							            return doc.specialityId == $scope.radiologySpecialtyId;
							        });
									console.log("radioSpecialityObj",radioSpecialityObj);
									$scope.specialityList = radioSpecialityObj;
									console.log("$scope.specialityList",$scope.specialityList);
								}
										}, 1500);

							}
							
							
							$scope.closeAppPopup = function(){
								if (angular.equals({},$stateParams.appointmentObjectReschedule)) {
									$scope.patientDetails.prefixId = "";
									$scope.patientDetails.firstName = "";
									$scope.patientDetails.middleName = "";
									$scope.patientDetails.lastName = "";
									$scope.patientDetails.age = "";
									$scope.patientDetails.birthDate = "";
									$scope.patientDetails.genderId = "";
									$scope.patientDetails.mobileNumber = "";
									$scope.patientDetails.email = "";
									$scope.patientDetails.remark = "";
									$scope.patientDetails.bloodGroup = "";
									$scope.patientDetails.patientId = 0;
									
									//$scope.datepickerDOB = new Date();
									
									$scope.patientList = [];
									
									$scope.regiType = "1";
									$scope.patientSearchObj = {
											"uhidNumber" : "",
											"patientName" : "",
											"genderId" : "",
											"mobileNumber" : "",
											"dob" : "",
											"identificationNumber" : "",
											"identificationTypeId" : "",
											"unitId" : $scope.unitId,
											"organizationId" : $scope.organizationId,
											"registrationTypeIdList" : [1,2,3,4,6]
										}
									
									$scope.fieldsDisabled = false;
								}
								$scope.discountAmount = parseFloat(0);
	  					    	$scope.patientCouponTemplateMapperId = null;
							}
							
							$scope.searchPackages = function(packageSearchKeyword){
								/*if (!$scope.modalityForm.$valid)
								{ 
									growl.warning("Please fill patient details.",{
										title : WARNING_MSG
									});
									return;
								}*/
									
									if($scope.patientDetails.prefixId=='' || $scope.patientDetails.prefixId==undefined){
										growl.warning("Prefix is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}else if($scope.patientDetails.firstName=='' || $scope.patientDetails.firstName==undefined){
										growl.warning("First name is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}else if($scope.patientDetails.genderId=='' ||$scope.patientDetails.genderId==undefined){
										growl.warning("Gender is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}else if($scope.patientDetails.mobileNumber=='' ||$scope.patientDetails.mobileNumber==undefined){
										growl.warning("Mobile number is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}
									else if($scope.datepickerDOB=='' ||$scope.datepickerDOB==undefined){
										growl.warning("Date of Birth  is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}
								var packageSearchObj = {};
									packageSearchObj.serviceCode = packageSearchKeyword;
//									packageSearchObj.serviceName = packageSearchKeyword;
									packageSearchObj.organisationId = $scope.organizationId;
									packageSearchObj.unitId = $scope.unitId;
									packageSearchObj.packageTypeId = 1;
									packageSearchObj.visitTypeId = 1;//default op
									packageSearchObj.paymentEntitlementId=1; //default op
									packageSearchObj.genderId = $scope.patientDetails.genderId;
									packageSearchObj.patientTypeId = $scope.patientDetails.patientCategoryId !=null ? $scope.patientDetails.patientCategoryId : $scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : 1;
									packageSearchObj.bedBillingCategoryId = $scope.opdBillingCatId;//default op
									var URI = BASE_URL + ROOT_URL + "/api/packages/bill/services/search";
									return BillingGenericService.serviceAction("POST", URI, packageSearchObj).then(function(response){
										if(response.data.status == "success")
											return $scope.searchPackageList = response.data.listObject;
									});
								
							}
							
							$scope.selectPackageService = function($item, $model, $label){
								 
								
								if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1"){
									var birthDateArr=moment($scope.datepickerDOB).format('DD/MM/YYYY').split("/");
									var age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);
									if(!($item.minAge <= age && $item.maxAge >= age)){
										this.search='';
										growl.warning("Package is not yet  configure for this age.",{
											title : WARNING_MSG
										});
										return;
									}
								}
							
								$scope.patientDetails.ehcPackageId = $item.packageMasterId;
								$scope.patientDetails.serviceMasterId = $item.serviceMasterId;
							}
							
							//Vivek Arrow Slots Start
							$scope.leftArrowWeeklySlots = function(arrowType)
							{
								//alert("WEEKID"+$scope.wkId);
							
								//alert("MONTH LENGTH"+$scope.slotListMonth.length);
								//var weekId = $scope.wkId;
								var minLength = 1;
								var maxLength = $scope.slotListMonth.length;
								if(arrowType == 'L')
											{
												if(minLength == $scope.wkId)
												{
													growl.warning("This is first week!!",{
														title : ERROR_MSG
													});
													return false;
												}else{
													$scope.wkId = $scope.wkId - 1;
													//alert($scope.wkId);
												console.log($scope.slotListMonth[$scope.wkId-1]);
												var arrFrom =$scope.slotListMonth[$scope.wkId-1][0].split('_');
												$scope.fromDateForWeek = arrFrom[1];
												
												var arrTo =$scope.slotListMonth[$scope.wkId -1][$scope.slotListMonth[$scope.wkId -1].length-1].split('_');
												$scope.toDateForWeek = arrTo[1];
												}
											}
										else{
												if(maxLength == $scope.wkId)
												{
													growl.warning("This is Last week!!",{
														title : ERROR_MSG
													});
													return false;
												}
												else
													{
													$scope.wkId = $scope.wkId + 1;
												console.log($scope.slotListMonth[$scope.wkId -1 ]);
												var arrFrom =$scope.slotListMonth[$scope.wkId -1][0].split('_');
												$scope.fromDateForWeek = arrFrom[1];
												
												var arrTo =$scope.slotListMonth[$scope.wkId -1][$scope.slotListMonth[$scope.wkId -1].length-1].split('_');
												$scope.toDateForWeek = arrTo[1];
													}
										}
								
								$rootScope.startLoader();
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										
										slotTypeId:$scope.selectedSlotId,
										fromDate:$scope.fromDateForWeek,
										toDate:$scope.toDateForWeek
								}
								
								if($scope.selectedSlotId == 1)
									{
										data.doctorId=$scope.doctorId;
										data.specialityId=$scope.specialityId;
									}
								else if($scope.selectedSlotId == 2)
									{
										data.specialityId=$scope.specialityId;
										data.subSpecialityId=$scope.subSpecialityId;
										data.modalityId=$scope.modalityId;
									}
								else
								{
								$scope.secondTabLevel = 3;
								}
							
								
								console.log(data);
								//return false;
								
								var URI = BASE_URL+ ROOT_URL+ GETWEEKLYSLOTS;
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											$scope.slotDetailsList = [];
											// $rootScope.startLoader();
											if (response.data.status == "success") {
												$scope.slotsListWeek = response.data.listObject;
												console.log($scope.slotsListWeek);
												$rootScope.stopLoader();
											}

										});
									
									
							
							}
							
							
							$scope.leftArrowWeeklySlotsFor = function(arrowType)
							{
								/*alert($scope.doctorId);
								var indexList = 0;
								var listLength = $scope.commonListDoctorModality.length;
								angular .forEach($scope.commonListDoctorModality, function( value, index) {
									 if($scope.doctorId ==  value.doctorId)
										 {
										 	indexList = index;
										 }
								});
								if(arrowType == 'L')
								{
									if(indexList == listLength - 1)
										{
											
										}
									else{
										console.log("just Check",$scope.commonListDoctorModality[listLength - 1])
									}
								}
								else
									{
									
									}*/
							}
							//Vivek Arrow Slots End
							
							$scope.patientAppoObj={
									patientId:undefined
							};
							$scope.patientAppoObjPayment={};
							$scope.patientIdForReport = "";
							$scope.getBillAmountForAppointment = function()
							{
								$scope.serviceOrderList = [];
								if($scope.rescheduleCookiesObj!=undefined && $scope.rescheduleCookiesObj!=null)
									{
										$scope.patientAppoObj = $scope.rescheduleCookiesObj;
									}
								
	                         		 if( $scope.otpvalidated == false ){
	                         		 }else{
	                         			growl.warning("Please validate email Id ",{
											title : "warning!!!"
										});
	                         			$scope.setSecondTabLevel(111);
										return false;
	                         		 }
											
	                         		 
	                         		if( $scope.otpvalidatedMobile == false){
	                        			 
	                        		 }else{
	                        			growl.warning("Please validate Mobile Number ",{
	           							title : "warning!!!"
	           						});
	                        			$scope.setSecondTabLevel(111);
	           						return false;
	                        		 }
								
								var promise={};
								$scope.patientAppoObjPayment={};
								if($scope.regiType==1){
									if($scope.patientDetails.prefixId=='' || $scope.patientDetails.prefixId==undefined){
										growl.warning("Prefix is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}else if($scope.patientDetails.firstName=='' || $scope.patientDetails.firstName==undefined){
										growl.warning("First name is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}else if($scope.patientDetails.genderId=='' ||$scope.patientDetails.genderId==undefined){
										growl.warning("Gender is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}else if($scope.patientDetails.mobileNumber=='' ||$scope.patientDetails.mobileNumber==undefined){
										growl.warning("Mobile number is required",{
											title : "warning!!!"
										});
										$scope.setSecondTabLevel(111);
										return false;
									}
									else{
										var patientData={
												registrationTypeId:5,
												genderId :$scope.patientDetails.genderId,
												mobileNumber:$scope.patientDetails.mobileNumber,
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												dob :moment($scope.datepickerDOB).format('DD-MM-YYYY HH:mm:ss')
										};
										 var URI = BASE_URL + ROOT_URL
									 		+ "/api/global/patient/unique";

									 	BillingGenericService
									 		.serviceAction(METHOD_POST,
									 				URI, patientData,
									 				NOTIFICATION_MSG_STATUS_FALSE)
									 		.then(
									 				function(response) {
									 					console
									 							.log(response);
									 					//$rootScope.startLoader();
									 					if (response.data.status == "success")
									 					{
									 						if(response.data.code==409)
									 							{
									 							growl.warning(response.data.message,{
																	title : "warning!!!"
																});
																return false;
											 				}else{
											 								$scope.getServiceRates();
									 						}
									 					}
									 				});
									}

								}
								//if(($scope.patientAppoObj.patientId!=undefined && $scope.patientAppoObj.patientId > 0)||($scope.rescheduleCookiesObj.patientId!=undefined && $scope.rescheduleCookiesObj.patientId > 0))
								if(($scope.patientAppoObj.patientId!=undefined && $scope.patientAppoObj.patientId > 0))
								{
									$scope.getServiceRates();
								}
								
							}

							//Get deposit amount
						$scope.availableDeposits=[];
						$scope.getActiveDepositAmount=function(){
											
							//get available active deposits by patient
							var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.patientAppoObj.patientId+"/"+0+"/1/0/false";
							BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success")
								{
									
									$scope.consumedAmt=0;
									$scope.availableDeposits = response.data.listObject;
									var activeDeposit = parseFloat(0);
									var activeDepositPatient = parseFloat(0);
									var billAmt = parseFloat($scope.payeeDueAmtStatic);
									var i = 0
									angular.forEach($scope.availableDeposits,function(value,key)
									{
										$scope.availableDeposits[i].oldConsumedDeposit = value.consumedDeposit;
										$scope.availableDeposits[i].oldAvailableDeposit =  value.availableDeposit;
										/*if(value.depositTypeId != '1')
									     {
										    activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
										    
										 if(billAmt >0){ 
										   
										   if(billAmt >= value.availableDeposit)
												{
											        value.consumedDeposit = value.consumedDeposit+value.availableDeposit;
													$scope.availableDeposits[i].depositeFlag = 'Y';
													$scope.transferToActiveDepositeFlag = true;
													$scope.dueAmount = ($scope.dueAmount - value.availableDeposit).toFixed(2);
													billAmt = billAmt-value.availableDeposit;
													$scope.availableDeposits[i].availableDeposit = parseFloat(0);
												}							
											else if(billAmt < value.availableDeposit)
												{
													
													value.availableDeposit = parseFloat(value.availableDeposit)- parseFloat(billAmt);
													value.consumedDeposit = value.consumedDeposit + parseFloat(billAmt);
													$scope.availableDeposits[i].depositeFlag = 'Y';
													$scope.dueAmount = ($scope.dueAmount - billAmt).toFixed(2);
													$scope.payeeDueAmtFinal = 0;
													billAmt = 0;
													//$scope.dueAmount = ($scope.dueAmount -$scope.consumedAmt).toFixed(2);
												}
										    }else{
										    	$scope.availableDeposits[i].depositeFlag = 'N';
										    }
										}
										else
										{*/
											activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
											$scope.availableDeposits[i].depositeFlag = 'N';
											activeDepositPatient = activeDepositPatient + parseFloat(value.availableDeposit);
											/*}*/
										i++;
										
									});
									$scope.activeDeposit = activeDeposit;
									$scope.useActiveDepo = true;
									
								}
								$scope.calculateTotalDueRefAmount();
							});
						  //for Bill End
						}
						
						
						//Ganesh Start
						$scope.checkAciveDepo  = function(useActiveDepo)
						{
							 $scope.activeDeposit = 0;
							if(useActiveDepo == true)
								{
								   
									var i = 0;
									angular.forEach($scope.availableDeposits,function(value,key)
									{
										if(value.depositTypeId == '1')
									   {
										   $scope.activeDeposit = $scope.activeDeposit + parseFloat(value.availableDeposit);
										   $scope.availableDeposits[i].depositeFlag = 'N';
										   if(value.depositeFlag == 'N')
										   {
											   value.consumedDeposit = value.oldConsumedDeposit;
											   value.availableDeposit = value.oldAvailableDeposit;
										   }
										}else{
											$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.depositAmount);
										}
										
										i++;
									});
								}
							else
								{
								var i = 0;
								var consumedAmt= 0;
									angular.forEach($scope.availableDeposits,function(value,key)
									{
										if(value.depositTypeId == '1')
										{
											if(value.depositeFlag == 'Y')
											{
												consumedAmt = consumedAmt - (value.consumedDeposit);
											}
											$scope.availableDeposits[i].availableDeposit = $scope.availableDeposits[i].oldAvailableDeposit;
											$scope.availableDeposits[i].consumedDeposit =$scope.availableDeposits[i].oldConsumedDeposit
											$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.oldAvailableDeposit);
											$scope.availableDeposits[i].depositeFlag = 'N';
										
										}else{
												$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.oldAvailableDeposit);
										}
										$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(consumedAmt)).toFixed(2);
										i++;
									});
								}
							$scope.calculateTotalDueRefAmount();
						}
						/*Ganesh end*/
						
							
                        //get service rates
							$scope.getServiceRates=function(){
								$scope.patientIdForReport = $scope.patientAppoObj.patientId==undefined ? null:$scope.patientAppoObj.patientId;
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								
								
								var data = {
								        unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										patientId : ($scope.patientAppoObj.patientId!=undefined ? $scope.patientAppoObj.patientId : null),
										doctorId : $scope.patientAppoObj.doctorId==undefined ? $scope.doctorId :$scope.patientAppoObj.doctorId ,
										specialityId : $scope.patientAppoObj.specialityId==undefined ? $scope.specialityId : $scope.patientAppoObj.specialityId ,
										currentDate :  moment(new Date()).format('YYYY-MM-DD'),
										patientCategoryId : $scope.patientAppoObj.patientCategoryId!=undefined ? $scope.patientAppoObj.patientCategoryId:$scope.defaultPatientCategoryId,
										deptWiseAppointment : $scope.patientAppoObj.deptWiseAppointment!=null ? $scope.patientAppoObj.deptWiseAppointment : $scope.deptWiseAppointment != null ? $scope.deptWiseAppointment : 'N',
										userDefinedVisitTypeId	: $scope.userDefinedVisitTypeId
												
								}
								//$scope.initPaymentDetails();
								var URI = BASE_URL + ROOT_URL + "/api/opd/billAmount/appointment"
								BillingGenericService
										.serviceAction(METHOD_POST,
												URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response1) {
													console.log(response1);
													if (response1.data.status == "success")
													{
														$scope.serviceOrderList = [];
														$scope.serviceOrderList = response1.data.object.listOrderDetailsMasterDto;
														
														$scope.patientAppoObjPayment.totalBillAmount = parseFloat(0);
														$scope.patientAppoObjPayment.systemDefiedVisitType = $scope.userDefinedVisitTypeId;
														 
														angular.forEach($scope.serviceOrderList,function(value,key)
																{
															      $scope.patientAppoObjPayment.totalBillAmount = parseFloat($scope.patientAppoObjPayment.totalBillAmount) + parseFloat(value.selfPayable);
															      $scope.patientAppoObjPayment.dueAmount =parseFloat($scope.patientAppoObjPayment.totalBillAmount);
															      $scope.patientAppoObjPayment.payeeDueAmtFinal=parseFloat($scope.patientAppoObjPayment.totalBillAmount);
															      $scope.patientAppoObjPayment.payeeDueAmtStatic = Number(parseFloat($scope.patientAppoObjPayment.totalBillAmount).toFixed(2));
																});
														
														 $scope.patientAppoObjPayment.paymentDetailsObj=$scope.paymentDetails;
														 $scope.patientAppoObjPayment.loyaltyCardObj= $scope.loyaltyCardObj;
														
															$scope.billServicesList=[];
															$scope.paymentReceiptsDetailsList=[];
															$scope.listPaymentDetails = [];
															$scope.payeeList = [];
															$scope.totalAmtQuaBase = 0;
															$scope.totalAmtConcession = 0;
															$scope.totalAmtDiscount = 0;
															$scope.totalAmtGst = 0;
															$scope.totalNetPayableAmt = 0;
															$scope.payeeDueAmtFinal = 0;
															$scope.payeeDueAmtStatic = 0;
															$scope.visitTypeId = 1;
															
															$scope.consumedAmt = parseFloat(0);
															
															$scope.payeeId = $scope.selfPayeeIdCookies;
															
															$scope.billDisabled = false;
															//$scope.billId = bill.billingMasterId;
															 $scope.payeeDueAmtFinal = $scope.patientAppoObjPayment.totalBillAmount;
															 $scope.payeeDueAmtStatic = $scope.patientAppoObjPayment.totalBillAmount;
															$scope.totalAmount = $scope.patientAppoObjPayment.serviceAmount; 
															$scope.dueAmount =$scope.patientAppoObjPayment.totalBillAmount;
															$scope.oldDueAmount = $scope.patientAppoObjPayment.totalBillAmount;
															$scope.billDueAmount = $scope.patientAppoObjPayment.serviceAmount;
															$scope.currentBill = {};
															$scope.currentBill.amountPaid = parseFloat(0);
															//$scope.admissionId = bill.visitTypeId == 1 ? bill.visitId : null;
															$scope.isFinancialClearance = false;
															$('#isFinancialClearance').prop('checked',false);
															
															$scope.initPaymentDetails();
															$scope.validatTextFieldFlag = true;
															$scope.availableDeposits=[];
															if($scope.patientAppoObj.patientId>0 && $scope.patientAppoObj.patientId!=undefined){
																$scope.getActiveDepositAmount();
																$scope.getLoyaltyCardList($scope.patientAppoObj.patientId);
															}else{
																$scope.availableDeposits=[];
															}
															
														$rootScope.stopLoader();
													}else{
														angular.element('#consultationConfigNotExistPre').modal('show');
														return false;
													}
												});
													
						
							}
									
							//Check isActiveSelected
							  var isActiveSelected;
								function checkActiveSelected(arr) {
									return arr
											.some(function(arr) {
												if(arr.depositeFlag === 'Y')
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
					//Deposit
						        $scope.addBtnDisabled = false;
								$scope.depositeSub = function(item,idx)
								{
									 $("#userEnterDeposit").modal("hide");
									if($scope.payeeDueAmtFinal > 0 || item.depositeFlag == 'N'){
									var consumedAmt = 0;
									if(item.depositeFlag == 'Y')
										{
										
										 if(item.availableDeposit == 0 || item.availableDeposit == '00' || item.availableDeposit < 0 || parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat(item.availableDeposit)){
												growl.warning("Available deposit amount shuold be greater than user enter amount.",{
													title : WARNING_MSG
												});
												return false;
											}
										 $scope.availableDeposits[idx].remark =$scope.DepositObj.remark;
											if($scope.dueAmount == 0)
											{
												$scope.availableDeposits[idx].depositeFlag = 'N';
											}
											else if($scope.dueAmount >= item.availableDeposit)
											{
												$scope.availableDeposits[idx].consumedDeposit  = parseFloat(parseFloat($scope.availableDeposits[idx].consumedDeposit)  + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2)// parseFloat(userEnterDepositAmount);
												$scope.availableDeposits[idx].availableDeposit = ($scope.availableDeposits[idx].availableDeposit) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
												$scope.dueAmount = (parseFloat($scope.dueAmount)) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
											}
										else if($scope.dueAmount <= item.availableDeposit)
											{
												$scope.availableDeposits[idx].availableDeposit = parseFloat(item.availableDeposit)- parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
												$scope.availableDeposits[idx].consumedDeposit =parseFloat(parseFloat(item.consumedDeposit) + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2);
												$scope.dueAmount = (parseFloat($scope.dueAmount))- parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
											}
											$scope.DepositObj.remark="";
											$scope.addBtnDisabled = true;
										}
									if(item.depositeFlag == 'N')
									{
										 $scope.availableDeposits[idx].remark =$scope.DepositObj.remark;
												if($scope.availableDeposits[idx].availableDeposit > 0)
													{
													$scope.availableDeposits[idx].availableDeposit = parseFloat(item.oldAvailableDeposit).toFixed(2);
													$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
													$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(item.oldAvailableDeposit-item.availableDeposit)).toFixed(2);
													}
												else{
													$scope.availableDeposits[idx].availableDeposit =  parseFloat($scope.availableDeposits[idx].availableDeposit + parseFloat(item.consumedDeposit-item.oldConsumedDeposit)).toFixed(2);
													$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
													$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat($scope.availableDeposits[idx].availableDeposit)).toFixed(2);
												}
												
												
												var totalListamount = 0;
												angular.forEach($scope.listPaymentDetails,function(value,key)
														{
													totalListamount = totalListamount+parseInt(value.amount);
														});

												if(parseInt($scope.totalAmount) >=totalListamount){
													
													$scope.excessAmount = parseFloat(0).toFixed(2);
												}else{
													var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
													$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
												}
												$scope.DepositObj.remark="";	
												
									}
									
									var avalForFlag = false;
									var i = 1;
									angular.forEach($scope.availableDeposits,function(value,key){
										if(value.depositeFlag == 'Y')
											{
											avalForFlag = true;
											}else
												{
													if($scope.transferToActiveDepositeFlag == false)
														{
														avalForFlag == false;
														}
												}
										if(i == $scope.availableDeposits.length)
											{
											$scope.transferToActiveDepositeFlag  = avalForFlag;
											}
										i++;
										
									})
									
									$scope.calculateTotalDueRefAmount();
									
								}
									else
									{
										growl.warning("Due amount is zero(0).",{
											title : WARNING_MSG
										});
										$scope.availableDeposits[idx].depositeFlag = 'N';
										return false;
										$scope.addBtnDisabled = false;
									}
									
								}
								
								
								$scope.depositeObject = undefined;
								$scope.depositeIndex = undefined;
								$scope.depositeSubPopup = function(item,idx){
									$scope.DepositObj.userEnterDepositAmount = 0;
									if(item.depositeFlag == 'Y' && $scope.dueAmount > 0){
										$scope.addBtnDisabled = false;
										if($scope.dueAmount > item.availableDeposit){
											$scope.DepositObj.userEnterDepositAmount = item.availableDeposit;
										}else{
											$scope.DepositObj.userEnterDepositAmount = parseFloat($scope.dueAmount).toFixed(2);
										}
										
										$scope.depositeObject = item;
										$scope.depositeIndex = idx;
										 $("#userEnterDeposit").modal("show");
										 
									}else{
										$scope.depositeSub(item,idx);
										 $("#userEnterDeposit").modal("hide");
									}
									
								}
								
								$scope.closeDepositPop = function(){
									$scope.availableDeposits[$scope.depositeIndex].depositeFlag = 'N';
									 $("#userEnterDeposit").modal("hide");
								}
								
								$scope.checkEnterDepositeAmount = function(){
									
									if(parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat($scope.depositeObject.availableDeposit)){
										growl.warning("Enter amount should not be greater available deposit ",{
											title : WARNING_MSG
										});
										$scope.DepositObj.userEnterDepositAmount =0;
										return;
									}
									
									if(parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat($scope.dueAmount)){
										growl.warning("Enter amount should not be greater due amount",{
											title : WARNING_MSG
										});
										$scope.DepositObj.userEnterDepositAmount =0;
										return;
										
									}
									
								}
								
								
						//add payment function
		             $scope.addPaymentDetails = function(){	
							$scope.isBillList = 0;
							var exAmount = '';
							
							var cashLimit = 0;
							angular.forEach($scope.listPaymentDetails,function(value,key){
								
								if(value.paymentModeId == 1){
									cashLimit = parseFloat(cashLimit) + parseFloat(value.amount);
								}
							});

							if($scope.paymentDetails.paymentModeId == 1){
								cashLimit = cashLimit + parseFloat($scope.paymentDetails.cashAmount);
							}
							
							if(($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null) && cashLimit > $scope.cashAmountLimit){
								growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
									title : WARNING_MSG
								});
								return;
							}
							
							var trancastionId = false;
							angular.forEach($scope.listPaymentDetails,function(value,key){
								
								if(value.paymentModeId == 12 && value.transactionNo == $scope.razorPayObject.razorUniqueId){
									trancastionId =true;
								}
							});
							
							if(trancastionId){
								growl.warning("Trancastion id should not be same",{
									title : WARNING_MSG
								});
								return;
							}

							if($scope.paymentDetails.paymentModeId == 1){
								
								exAmount = $scope.paymentDetails.cashAmount;
								
							}else if($scope.paymentDetails.paymentModeId == 2){
								
								exAmount =  $scope.paymentDetails.chequeAmount;
								
							}else if($scope.paymentDetails.paymentModeId == 3){
								
								exAmount = $scope.paymentDetails.credAmount;
								
							}else if($scope.paymentDetails.paymentModeId == 4){
								
								exAmount = $scope.paymentDetails.debAmount;
								
							}else if($scope.paymentDetails.paymentModeId == 5){
								
								exAmount = $scope.mpesaObject.mpesaAmount;
								
							}else if($scope.paymentDetails.paymentModeId == 6){
								
								exAmount = $scope.paymentDetails.loyaltyCardAmt;
								
							}else if($scope.paymentDetails.paymentModeId == 8){
								
								exAmount = $scope.eTransferObj.transferAmount;
							}else if($scope.paymentDetails.paymentModeId == 12){
								
								exAmount = $scope.razorPayObject.razorPaidAmount;
							}
							if(parseFloat(exAmount) <= parseFloat($scope.payeeDueAmtFinal))
							{
									$scope.transferToActiveDepositFact = this.transferToActiveDeposit;
									paymentCollFactory.addPaymentDetails($scope);
							}else{
								
								isActiveSelected=checkActiveSelected($scope.availableDeposits)
								  if (!isActiveSelected) {
									  if(exAmount == undefined || exAmount == '' || exAmount == 0 || exAmount == null){
										  growl.warning("Amount should be greater than ZERO(0)..!! ",{
												title : WARNING_MSG
											});
											return false;
									  }
									  else{
										  var fixAmount = parseFloat(exAmount).toFixed(2) - parseFloat($scope.payeeDueAmtFinal).toFixed(2); 
										  $scope.excessAmount = parseFloat(fixAmount).toFixed(2);
										  if(fixAmount>0)
											{
											  if($scope.exceededBillAmountSettlement==true)
												  {
												  $("#confirmSave").modal("show");
												  }
											  else
												  {
												    growl.warning("Entered amount is greater than current amount");
												    $scope.excessAmount = parseFloat(0.0).toFixed(2);
												    $scope.paymentDetails.chequeAmount= parseFloat($scope.dueAmount).toFixed(2);
													$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
													$scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
													$scope.paymentDetails.loyaltyCardAmt = parseFloat($scope.dueAmount).toFixed(2);
													$scope.mpesaObject.mpesaAmount = parseFloat($scope.dueAmount).toFixed(2);
													$scope.eTransferObj.transferAmount = parseFloat($scope.dueAmount).toFixed(2);
													$scope.razorPayObject.razorAmount = parseFloat($scope.dueAmount).toFixed(2);
												  }
											}
									  }
								  }
								  else
									  {
									         $scope.paymentDetails.cashAmount = $scope.dueAmount;
											  growl.warning("You can select either transfer to active deposit or consume from deposit..!!",{
													title : WARNING_MSG
												});
												return false;
												
									  }
							}
							
						}
							

		       	var parentChild = 0;
		       	$scope.addExAmount = function(){
		       		$scope.isBillList = 0;
		       		if( parentChild == 0 ){
		       			$scope.transferToActiveDeposit = 'Y';
		       			$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
		       		//	this.$parent.$parent.$parent.transferToActiveDeposit = 'Y';
		       		//	$scope.transferToActiveDepositFact = this.$parent.$parent.$parent.transferToActiveDeposit;
		       		}else{
		       			
		       			$scope.transferToActiveDeposit = 'Y';
		       			$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
		       			
		       		//	this.$parent.$parent.transferToActiveDeposit = 'Y';
		       		//	$scope.transferToActiveDepositFact = this.$parent.$parent.transferToActiveDeposit;
		       		}
		       		$scope.changeExAmount = function(){
		       			$scope.excessAmount = parseFloat(0.0).toFixed(2);
		       		}
		       		paymentCollFactory.addPaymentDetails($scope);
		       		$("#confirmSave").modal("hide");
		       		parentChild += 1;
		       	}
		       	
		       	$scope.cancelExAmount = function(){
		       		$('#confirmSave').modal('hide');
		       		$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
		       		$scope.excessAmount = parseFloat(0.0).toFixed(2);
		       	}
		       	
		       	$scope.removePaymentDetails = function(index){
		       		paymentCollFactory.removePaymentDetails($scope,index);
		       		var amount = 0;
		       		$scope.listPaymentDetails.forEach((elem) => {amount = parseFloat(amount) + parseFloat(elem.amount)});
		       		if(amount <= 0 && $scope.currentBill.amountPaid <= 0){
		       			$scope.isFinancialClearance = false;
		       			$('#isFinancialClearance').prop('checked',false);
		       		}
		       		
		       		
		       		var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
		       		if(tempObj!=null && (tempObj.cardNo == $scope.paymentDetails.loyaltyCardNo))
		       			{
		       				 $scope.paymentDetails.cardBalAmount = $scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
		       				 $scope.send=false;
		       				 $scope.resend=false;
		       			}
		       		
		       		
		       		var totalListamount = 0;
		       		angular.forEach($scope.listPaymentDetails,function(value,key)
		       				{
		       			totalListamount = totalListamount+parseInt(value.amount);
		       				});

		       		if(parseInt($scope.totalAmount) >=totalListamount){
		       			
		       			$scope.excessAmount = parseFloat(0).toFixed(2);
		       		}else{
		       			var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
		       			$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
		       		}
		       		
		       	}
		       	 
		       	$scope.settlementClick=function()
		    	{
                     setTimeout(function(){
                    	 $('#setCash').trigger('click')
                     }, 300);
		       		$scope.flagForSMSpay = false;
		    		$scope.flagForBharatQr = false;
		    		$scope.flagForSale = false;
		    		$scope.smsPay = {};
		    		$scope.bharatQr = {};
		    		$scope.sale = {};
		    		$scope.objPosMachine.deviceName = "";
		    		$scope.objPosMachine.isSale = false;
		    		$scope.objPosMachine.isBharatQr = false;
		    		$scope.objPosMachine.isSMSpay = false;
//		    		$scope.setFirstTabLevel(1);
		       		$scope.skipMpesaValidation = false;
		       		$scope.pinpadAmounFlag = false;
		       		$scope.fromCollectScreen = true;
		    		 $scope.send=false;
		    		 $scope.resend=false;
		    		 //$scope.setFirstTabLevel(1);
		    		 $scope.setPaymentModeId(1);
		    		 $scope.validatTextFieldFlag = true;
		    		 if($scope.patientAppoObj.patientId!=undefined){
		    		  $scope.getLoyaltyCardList($scope.patientIdForReport);
		    		 }
		    		 $scope.transferToActiveDeposit = 'Y';
		    		 $scope.excessAmount = parseFloat(0).toFixed(2);
		    		 $scope.listPaymentDetails = [];
		    		 $scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
		    		 $scope.totalAmtShow  = parseFloat(0);
		    		 $scope.totalAmtShow = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
		    		 $scope.calculateTotalDueRefAmount();
		    	}
		       	
		    	$scope.calculateTotalDueRefAmount = function()
		    	{
		    		var amount = 0;
		    		var dueAmount = $rootScope.parseFloatNum($scope.dueAmount);
		    		$scope.consumedAmt = 0;
		    		var payeeDueAmtFinal = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
		    		
		    		angular.forEach($scope.listPaymentDetails,function(value,key){
		    			amount = amount + parseFloat(value.amount); 
		    		});
		    		
		    		if($scope.availableDeposits!=null &&$scope.availableDeposits.length>0)
		    		{
		    			angular.forEach($scope.availableDeposits,function(value,key)
		    					{
		    						if(value.depositeFlag=='Y')
		    							{
		    							  $scope.consumedAmt = $scope.consumedAmt + parseFloat(value.consumedDeposit- value.oldConsumedDeposit); 
		    							}
		    					});
		    		}
		    		$scope.totalAmount = parseFloat($scope.totalAmtShow).toFixed(2);
		    		var tempDueAmt = parseFloat($scope.payeeDueAmtStatic) - parseFloat(amount) - parseFloat($scope.consumedAmt);
		    		var temppayeeDueAmtFinal = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
		    		$scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;
		    		$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
		    		var df = (parseFloat($scope.payeeDueAmtStatic) - (parseFloat(amount) + parseFloat($scope.consumedAmt)));
		    		if(df > 0)
		    			{
		    			$scope.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
		    			$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
		    			$scope.paymentDetails.chequeAmount = $scope.dueAmount;
		    			$scope.paymentDetails.debAmount = $scope.dueAmount;
		    			$scope.paymentDetails.credAmount = $scope.dueAmount;
		    			$scope.eTransferObj.transferAmount = $scope.dueAmount;
		    			$scope.razorPayObject.razorAmount =  parseFloat($scope.dueAmount).toFixed(2);
		    			$scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
		    			}
		    		else
		    			{
		    			$scope.dueAmount = parseInt($scope.billDueAmount) - parseInt($scope.payeeDueAmtStatic);
		    			$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
		    			$scope.paymentDetails.chequeAmount = $scope.dueAmount;
		    			$scope.paymentDetails.debAmount = $scope.dueAmount;
		    			$scope.paymentDetails.credAmount = $scope.dueAmount;
		    			$scope.eTransferObj.transferAmount = $scope.dueAmount;
		    			$scope.razorPayObject.razorAmount =  parseFloat($scope.dueAmount).toFixed(2);
		    			  $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
		    			}
		    		
		    		$scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
		    		$scope.amountCollected = amount;
		    		
		    		if($scope.consumedAmt > 0)
		    			{
		    			  $scope.depositeSettleBtnFlag = false;
		    			}
		    		else
		    			{
		    			$scope.depositeSettleBtnFlag = true;
		    			}
		    	}
		    	
		    	$scope.addBtnId=false;
		    	$scope.setPaymentModeId=function(paymentModeId){
		    		if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
		    			$scope.addBtnId=true;
		    			// $scope.pinpadAmounFlag =true;
		    		}else if(paymentModeId==6){
		    			$scope.addBtnId=true;
		    		}
		    		else{
		    			$scope.addBtnId=false;
		    			 $scope.pinpadAmounFlag =false;
		    		}
		    		$scope.paymentDetails.paymentModeId=paymentModeId;
		    		$scope.paymentDetails.paymentModeName=$scope.paymentModes[paymentModeId];
		    	}
		    	
		    	/* *************************************Mpesa Integration start****************************************** */
				$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
				$scope.skipMpesaValidation = false;
				$scope.validateMpesaDetails = function(mpesaObject){
					$rootScope.startLoaderPopup();
					console.log("MPesa Object:",mpesaObject);
					$scope.skipMpesaValidation = true;
					var data ={
							organizationId :$scope.organizationId,
							unitId : $scope.unitId,
							createdBy : $scope.userId,
							createdDate : moment(new Date()).format("DD-MM-YYYY"),
							updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
							updatedBy:$scope.userId,
							mpesaTxcode:$scope.mpesaObject.transactionId,
							uhid: $scope.patientDetails.uhIdNumber,
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
							$scope.skipMpesaValidation = false;
							$rootScope.stopLoaderPopup();
						}
						$rootScope.stopLoaderPopup();
					});
					}else{
						$scope.skipMpesaValidation = false;
						growl.error("Enter both transaction id and mobile number",{
							title : ERROR_MSG
						});
						$rootScope.stopLoaderPopup();
					}
				}
		    	$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
					if(validateMpesaAmount > $scope.payeeDueAmtFinal){
						
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
								uhidNumber: $scope.patientDetails.uhIdNumber,
								phoneNumber:parseInt($scope.countryCallingCode+""+$scope.mobileNumber),
								partyA:mobileNumber,
								amount:validateMpesaAmount
						};
					
						if(($scope.mobileNumber != undefined && $scope.mobileNumber != null && $scope.mobileNumber != "") ||
								($scope.countryCallingCode != undefined && $scope.countryCallingCode != null && $scope.countryCallingCode != "")){
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
								
								growl.error(response.data.message,{
									title : ERROR_MSG
								});
								$rootScope.stopLoaderPopup();
								$scope.skipMpesaValidation = false;
							}
							$rootScope.stopLoaderPopup();
						});
						}else{
							growl.error("Please enter mobile code and mobile number. ",{
								title : ERROR_MSG
							});
							$scope.skipMpesaValidation = false;
							$rootScope.stopLoaderPopup();
						}
					}
				}
				
				$scope.validateSTKPushPayment = function(){
					
					var data ={
							organizationId :$scope.organizationId,
							unitId : $scope.unitId,
							uhidNumber: $scope.patientDetails.uhIdNumber
							
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
							$scope.skipMpesaValidation = false;
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
				
				//***************************MPesa Integration end*************
				
	//**********************Loyalty card Functions start****************
				$scope.loyaltyCardObj ={
						
						"loyaltyCardNo" :"",
						"loyaltyCardBalance" :"",
						"authKey":0
				}
				
				$scope.isBillList = 0;
				$scope.page='billList';
				$scope.checkAmt = function(amt)
				{
					loyaltiCardCallFactory.checkAmt(amt,$scope);
					
				}
				
				$scope.getLoyaltyCardByNo = function(cardNo)
				{
					$scope.resend =false;
					loyaltiCardCallFactory.getLoyaltyCardByNo(cardNo,$scope);
					
				}
				
				$scope.sendOTP = function(flag)
				{
					loyaltiCardCallFactory.sendOTP(flag,$scope);
					//$scope.send = (flag=='Y')?true:false;
					
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
					$scope.calculateTotalDueRefAmount();
					$scope.getLoyaltyCardList($scope.patientIdForReport);
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
										  paymentCollFactory.addPaymentDetails($scope);
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
				
				$scope.disableEnableExpiry = function(id)
				{
					if(id!=undefined && id!=null && id!=""){
						var isExpiryRequired = $.grep($scope.identificationList, function (doc) {
		    	            return doc.identificationId == id;
		    	        })[0].isExpiryRequired;
						console.log("isExpiryRequired",isExpiryRequired);
						if(isExpiryRequired=='Y'){
							$scope.expiryDateDisabled = false;
						}else{
							$scope.expiryDateDisabled = true;
						}
					}
					
				}
				
				$scope.getLoyaltyCardList = function(patientId){
					$scope.resend =false;
					var data = {
							"patientId" : patientId,
							  "organisationId" : $scope.organizationId,
							  "unitId" : $scope.unitId,
							  "status" : "A",
							  "isDeleted" : "N",
							  "isUserDefined":"Y"
					}
					var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getLoyaltiCardDetailsByPatientId";
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
							if(response.data.listObject!=null && response.data.listObject.length>0){
								$scope.loyaltyCardObj = response.data.listObject[0];					
								$scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount;
								$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
							}else{
								$scope.paymentDetails.cardBalAmount = "";
								$scope.paymentDetails.loyaltyCardNo = "";
							}
							
						}
					});
				}
   //***********Loyalty card Functions end************	

				$scope.closePopup = function(){
					$scope.fromCollectScreen = false;
				}
				
				$scope.uploadEncounterDoc = function(Obj)
				{
					 var fd = new FormData();
				        fd.append('organizationId', $scope.organizationId);
				        fd.append('billId',Obj);
				        fd.append('isDeposite',false);
				        fd.append('isPharmacy',false);
				        fd.append('isMiscellaneous',false);
				        fd.append('unitId', $scope.unitId);
				        fd.append('documentName',$scope.fileObj.file.name);
				        fd.append('file',$scope.fileObj.file );
				        var URI1 = BASE_URL + ROOT_URL + "/billing/PanCard/uploadDoc";
				        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
						{	
				        	$scope.fileObj = {};
							$scope.fileObj.file = null;
							angular.element("input[type='file']").val(null);
							return response;
							
						});
				}	
				
				
				$scope.settleBill = function(){
					
					if($scope.billSettlement  == false){
						growl.warning("user do not have bill settlements rights",
								{
									title : 'Error!'
								});
					    
						return false;
					}
					$scope.finalAvailableDeposits = [];
					var i = 0;
					angular.forEach($scope.availableDeposits,function(value,key){
						if(value.depositeFlag == 'Y')
							{
							
							var newArr = {};
							newArr.availableDeposit = $scope.availableDeposits[i].availableDeposit;
							newArr.depositAmount = $scope.availableDeposits[i].depositAmount;
							newArr.depositMasterId =$scope.availableDeposits[i].depositMasterId;
							newArr.remark =$scope.availableDeposits[i].remark;
							newArr.organizationId  =  $scope.organizationId;
							newArr.unitId = $scope.unitId;
							newArr.depConsumptionDate =moment(new Date()).format('DD-MM-YYYY HH:MM:SS'); 
							/*newArr.billId = $scope.billId;	
							newArr.billPayeeDetailsId = billPayeeDetailsId;*/
							newArr.consumedAmount = (parseFloat($scope.availableDeposits[i].consumedDeposit).toFixed(2) - $scope.availableDeposits[i].oldConsumedDeposit); 
							newArr.createdBy = $scope.userId;
							$scope.finalAvailableDeposits.push(newArr);
							//$scope.finalAvailableDeposits.push($scope.availableDeposits[i]);
							}
						i++;
						
					});
				
					
					if(($scope.finalAvailableDeposits.length == 0)&&($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')){
						$scope.billDisabled = false;
						growl.warning("Please select at least one payment",{
							title : WARNING_MSG
						});
						return;
					}
					
					if($scope.patientCouponTemplateMapperId == null ){
						if(($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)){
							$scope.billDisabled = false;
							growl.error("Amount can't be zero",{
								title : ERROR_MSG
							});
							return;
						}
					}
					
						
				var amount=0;
				angular.forEach($scope.listPaymentDetails,function(value,key){
					amount = amount + parseFloat(value.amount); 
				});
				
				/*if(amount!=$scope.patientAppoObjPayment.totalBillAmount){
					angular.element('#saveNormalAppoitment').modal('show');
					return false;
				}*/
				
				
				var collectedAmount = parseFloat(0).toFixed();
				angular.forEach($scope.listPaymentDetails,function(value,key){
					if(value.paymentModeId == 1){
						collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
					}
					
				});
				
				if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
					$('#uploadpanCardDetailsAppointment').modal('show');
						return false;
				}
				$('#uploadpanCardDetailsAppointment').modal('hide');
				
				
				$scope.billDisabled = true;
				$('#collectPayment').hide('hide');
				$('#patientSearchForRegistration').hide('hide');
				$scope.fromCollectScreen = false;
				$(".modal-backdrop").hide();
//				if($scope.patientAppoObjPayment.totalBillAmount == $scope.mpesaAmount)
					//Appointment Object
					$scope.currentDate = new Date();
					$scope.currentDate = moment(
							$scope.currentDate).format(
							'DD-MM-YYYY HH:mm:ss');
					
					//$scope.appointmentDate=$rootScope.getChangedFormatedDateShalseTwo($scope.appointmentDate);
					$scope.appointmentDate=moment($scope.appointmentDate2).format('DD/MM/YYYY');
					var dataAppointmentObj = {
							patientId : $scope.patientAppoObj.patientId,
							remark : "",
							organizationId : $scope.organizationId,
							unitId : $scope.unitId,
							createdDate : $scope.currentDate,
							updatedDate : $scope.currentDate,
							createdBy : $scope.userId,
							updatedBy : $scope.userId,
							status : 'A',
							appointmentTypeId : "1",
							priorityId :"1",
							slotId :$scope.slotId,
					        appointmentDate2 :$scope.appointmentDate,
					        fromTimeString : $scope.fromTime,
					        appointmentDate : $scope.appointmentDate2,
//					        slotStatusId : 4,
					        slotStatusId: $scope.slotStatusId!=null && $scope.slotStatusId!=undefined ? $scope.slotStatusId : 4,
					        isFromEncounter: 'N',
					        isTeleconsultationAppointment : $scope.isTeleconsultationAppointment,
					        reasonForAppointmentId : $scope.patientDetails.reasonForAppointmentId,
							remark : $scope.patientDetails.remark
						}
					
					
					var isFromKiosk = null;
					$scope.currentDate = new Date();
					$scope.currentDate = moment($scope.currentDate)
							.format('DD-MM-YYYY HH:mm:ss');
					$rootScope.startLoader();
					var data = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId,
							doctorId : $scope.patientAppoObj.doctorId==undefined ? $scope.doctorId :$scope.patientAppoObj.doctorId,
							encounterDate : new Date().getTime(),
							isReferal : 'N',
							deptId : $scope.patientAppoObj.specialityId==undefined ? $scope.specialityId : $scope.patientAppoObj.specialityId,
							paymentEntitlementTypeId : 1, //self
							createdBy : $scope.userId,
							createdDate : new Date().getTime(),
							updatedDate : new Date().getTime(),
							updatedBy : $scope.userId,
							status : 'A',
							defaultSelfTariffId : $scope.defaultSelfTariffId,
							isScheduledAppointment:'A',
							specialityId :$scope.patientAppoObj.specialityId==undefined ? $scope.specialityId : $scope.patientAppoObj.specialityId,
							encounterTypeId: 1,
							isAppointmentRequired : 'Y',
							//counterId : 1, ask vishal
							currentDate :  moment(new Date()).format('YYYY-MM-DD'),
							patientCategoryId : $scope.patientAppoObj.patientCategoryId!=undefined ? $scope.patientAppoObj.patientCategoryId:$scope.defaultPatientCategoryId,
							totalBillAmt : $scope.patientAppoObjPayment.totalBillAmount,
							paidAmount : amount,
							discountAmount : $scope.discountAmount,
//							paidAmount : $scope.mpesaAmount,
							mpesaTxcode : $scope.mpesaTxcode,
							systemGeneratedVisitTypeId:$scope.systemGeneratedVisitTypeId ,
							userDefinedVisitTypeId:$scope.userDefinedVisitTypeId ,
							patientCouponTemplateMapperId:$scope.patientCouponTemplateMapperId,
							listKinDetailsDto : [],
							patientSponsorDetailsDtosList : [{
								unitId : $scope.unitId,
								organizationId : $scope.organizationId,
								visitTypeId : 1,
								paymentEntitlementId : 1,
								payeeId : $scope.selfPayeeId ,
								priorityId : 1,
								createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								createdBy : $scope.userId,
								updatedBy : $scope.userId,
								status : 'A',
								tariffId : $scope.defaultSelfTariffId,
							}],
							taxAmt : $scope.patientAppoObjPayment.taxAmount,
							serviceAmt : $scope.patientAppoObjPayment.serviceAmount,
							billingBedCategoryId : $scope.opdBillingCatId,	 				
							payeeId : $scope.selfPayeeId,
							isPreOrPostBilling : $scope.isPreOrPostBilling,
							deptWiseAppointment :$scope.patientAppoObj.deptWiseAppointment!=null ? $scope.patientAppoObj.deptWiseAppointment : $scope.deptWiseAppointment != null ? $scope.deptWiseAppointment : 'N',
							paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
							isTransferToActiveDeposit : $scope.patientAppoObjPayment.totalBillAmount < amount ? 'Y' : 'N',
							isNursingAssessmentRequiredForSpeciality : $scope.isNursingAssessmentRequired,
							isReshedule	: $scope.isReshedule,
							isFromAppointmentScheduling : 'Y',
							remarkForChangingVisitType : $scope.remarkForChangingVisitType,
							reasonIdForChangingVisitType : $scope.reasonIdForChangingVisitType
						   	
					}
					if($scope.isReshedule == 'Y')
					{
						data.rescheduleAppointmentId = $scope.rescheduleCookiesObj.appointmentId;
						data.rescheduleReasonId = $scope.rescheduleCookiesObj.rescheduleReasonId;
						data.reschedulledBy = $scope.rescheduleCookiesObj.reschedulledBy;
						data.previousSlotId = $scope.rescheduleCookiesObj.slotId;
					}
					
					if($scope.patientAppoObj.patientId!=undefined)
						{
							data.patientId = $scope.patientAppoObj.patientId;
						}else{
							data.patientId = 0,
							data.prefixId = $scope.patientDetails.prefixId,
							data.firstName = $scope.patientDetails.firstName, 
							data.middleName = $scope.patientDetails.middleName,
							data.lastName = $scope.patientDetails.lastName,
							data.genderId = $scope.patientDetails.genderId,
							//data.countryCallingCode = $scope.patientAppoObjPaymentPayment.countryCallingCode,
							data.registrationTypeId = 5,
							data.mobileNumber = $scope.patientDetails.mobileNumber,
							data.bloodGroup =$scope.patientDetails.bloodGroup;
							data.dob = moment($scope.datepickerDOB).format('DD-MM-YYYY HH:mm:ss'),
							data.patientCategoryId = $scope.defaultPatientCategoryId;
							
							if($scope.patientDetails.email != '' || $scope.patientDetails.email != null || $scope.patientDetails.email != undefined){
								data.email = $scope.patientDetails.email;
							}
							
							
						}
					$scope.patientAppoObjPayment.appointmentId=0;
					$scope.appoTime=moment($scope.fromTime, 'hh:mm A').format('HH:mm');
					if($scope.patientAppoObjPayment.appointmentId > 0)
						{
						  data.appointmentId = $scope.patientAppoObjPayment.appointmentId
						}
					else
						{
						   $scope.patientAppoObjPayment.isPaidAppointment = 'Y';
							//dataAppointmentObj.generateToken  = 'Y'
							$scope.slotCheckTimeAddOneHours = moment(new Date()).add(1, 'hours').format("HH:mm");
							if($scope.appointmentDate2==moment(new Date()).format('YYYY-MM-DD') && $scope.slotCheckTimeAddOneHours >= $scope.appoTime)
							{
								dataAppointmentObj.generateToken  = 'N';
								//dataAppointmentObj.arrivalTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
							}
							else{
								dataAppointmentObj.generateToken  = 'N';
							}
						
							data.appointmentObject = dataAppointmentObj;
						}
					if($scope.finalAvailableDeposits.length>0)
					{
						data.paymentDepositDetailsList=$scope.finalAvailableDeposits;
						data.totalDepositConsumption = $scope.consumedAmt;
					}
					var URI = null;
					
				/*	if(($scope.appointmentDate2==moment(new Date()).format('YYYY-MM-DD') && $scope.slotCheckTimeAddOneHours >= $scope.appoTime))
						URI = BASE_URL + ROOT_URL + "/api/billing/kiosk/saveAndPay";
					else*/
					
					//return false;
						URI = BASE_URL + ROOT_URL + "/api/billing/kiosk/saveAndPay/future";
					
					console.log('SavaAndPay',data);
					//return false;
					BillingGenericService
					.serviceAction(METHOD_POST,
							URI, data,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								console
										.log(response);
								//$rootScope.startLoader();
								if (response.data.status == "success")
								{
									growl.success("Appointment scheduled successfully !!!",
											{
										title : 'Success!'
											});	
									$scope.mpesaObject={};
									$scope.patientAppoObj={};
									
									if($scope.fileObj.file != null &&  response.data.resObject.billingMasterId != null){
										$scope.uploadEncounterDoc(response.data.resObject.billingMasterId);
									}
									$scope.patientCouponTemplateMapperId = null;
									$scope.validatTextFieldFlag = true;
									$scope.excessAmount = parseFloat(0).toFixed(2);
									$scope.transferToActiveDeposit = 'N';
									$scope.patientDetails={}; 
									$scope.patientAppoObjPayment={};
									$scope.patientAppoObj={};
									$scope.newPatientChange();
									$stateParams.appointmentObjectReschedule = {};
									$scope.rescheduleCookiesObj = {};
									//$scope.init();
									$scope.patientList = [];
									$rootScope.stopLoader();
									$scope.fromCollectScreen = false;
			                            $scope.slotTypeId = 1;
										$scope.yearSlotList = [];
										$scope.slotListMonth = [];
										$scope.slotsListWeek = [];
										$scope.slotDetailsList = [];
										$scope.slotDetailsList = [];
										$scope.commonListDoctorModality = [];
										$scope.weekArray = [];
										$scope.specialityName = '';
										$scope.fromDateForWeek = '';
										$scope.subSpecialityList = [];
										$scope.modalityMaster = [];
										$scope.doctorList = [];
										$scope.doctorName = '';
										$scope.specialityId ="";
										$scope.specDoctorId = '';
										$scope.subSpecialityId = '';
										 $scope.newPatientChange();
										 $stateParams.appointmentObjectReschedule = {};
											$scope.rescheduleCookiesObj = {};
											$timeout(function() {
														$state.go('appointmentScheduling.year',{appointmentObjectReschedule :{}}, {reload: true});
													}, 1000);
										$scope.setThirdTabLevel(1);
										$scope.setFirstTabLevel(1);
										$scope.selectedSlotId = 1;
									//$state.go('appointmentScheduling.year');									
								}else if (response.data.code == 409) {
									$scope.appointmentAlreadyExistMsg = response.data.message;
									angular.element('#alreadyExistAppointment').modal('show');
									$scope.setFirstTabLevel(1);
									$rootScope.stopLoader();
									return false;
									
								}else{
									$scope.appointmentAlreadyExistMsg = response.data.message;
									angular.element('#alreadyExistAppointment').modal('show');
									$scope.setFirstTabLevel(1);
									$rootScope.stopLoader();
									return false;
								}
							});
				
				}
		
				$scope.checkValidationChangeVisitType = function()
				{
					if($scope.reasonIdForChangingVisitType == '' || $scope.reasonIdForChangingVisitType == null || $scope.reasonIdForChangingVisitType == undefined)
						{
							growl.warning("Please select reason",{
								title : WARNING_MSG
							});
							return;
						}
					else
						{
							angular.element('#changeVisitTypeModal').modal('hide');
						}
				}
				
				$scope.printToken = function() 
				{									
					//start
					var printContents = $window.document.getElementById('qmsTokenPrintingId').innerHTML;
			        var popupWin = $window.open('', '_blank', 'width=400,height=400');
			        popupWin.document.open();
			        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
			        popupWin.document.close();
			        // end
					$scope.init();
					$('#tokenDisplayModal').hide('hide');
					$(".modal-backdrop").hide();
					/*document.location.reload(true);*/
					//$state.go('appointmentList');
					
				}	
				
				$scope.cancelPaymentReqModal = function(){
					angular.element('#paymentReqModal').modal('hide');
					}

					$scope.clearMpesaFields = function(){
					$scope.skipMpesaValidation = false;
					$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
					}
					
					 /******POS machine***********/

					$scope.getPOSMachineList=function(){
						$scope.pOSMachineList=[];
						 var data={
									   organizationId:$scope.organizationId,
					 			}
					           var URI = BASE_URL + ROOT_URL + "/api/getPOSMachineDeviceMasterList";
						 
							BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								
								$scope.otReservationList = [];
								if (response.data.status == "success")
								{
									$scope.pOSMachineList  = response.data.listObject;
								}
							});
					}
					$scope.getPOSMachineList();
				    

					$scope.selectedDevice=function(obj){
						
						if($scope.isPinPadRequired == 'Y' && $scope.registeredPinPadDevice == "POS Machine"){
						$scope.configDetail=[];
						angular.forEach($scope.pOSMachineList, function(value, key){
							if(value.deviceName == obj.deviceName.toString()){
								$scope.machineConfigDetails=value.machineConfigDetails;
								$scope.uploadLink=value.uploadLink;
							}
						});
							
						$scope.configDetail=JSON.parse($scope.machineConfigDetails);
						
						$scope.custUserId = $scope.configDetail.custUserId;
						$scope.deviceid = obj.deviceName;
						$scope.password = $scope.configDetail.password;
						}

					}
					
					$scope.getFlagSale=function(){
						if($scope.objPosMachine.isSale =="isSale"){
							$scope.objPosMachine.isSale =true;
							$scope.flagForSale=true;
							 $scope.flagForSMSpay=false;
							 $scope.flagForBharatQr=false;
							 $scope.objPosMachine.isBharatQr ="";
							 $scope.objPosMachine.isSMSpay=""
						}
						
					}
				 
				 $scope.getFlagBharatQr=function(){
					
						if($scope.objPosMachine.isBharatQr =="isBharatQr"){
							$scope.objPosMachine.isBharatQr =true;
							$scope.flagForBharatQr=true;
							 $scope.flagForSale=false;
							 $scope.flagForSMSpay=false;
							 $scope.objPosMachine.isSale ="";
							 $scope.objPosMachine.isSMSpay="";
						}
					}
				 $scope.getFlagSMSpay=function(){
						
						if($scope.objPosMachine.isSMSpay =="isSMSpay"){
							$scope.objPosMachine.isSMSpay =true;
							$scope.flagForBharatQr=false;
							 $scope.flagForSale=false;
							 $scope.flagForSMSpay=true;
							 $scope.objPosMachine.isSale ="";
							 $scope.objPosMachine.isBharatQr ="";
						}
					}
				 
					 $scope.sendRequestForPOSMachine=function(){

							if($scope.objPosMachine.deviceName != undefined && $scope.objPosMachine.deviceName !=null && $scope.objPosMachine.deviceName != "" ){
								$rootScope.startLoaderPopup();
								if($scope.flagForSale == true){
								$scope.sale=
//								{

								/*"deviceid":($scope.objPosMachine.deviceName).toString(),
								 "custUserId":$scope.custUserId,
								 "password":$scope.password,
								 "custMobileNo":$scope.patientDetails.mobileNumber,
								 "invoice":Date.now().toString(),
								 "amount":$scope.paymentDetails.credAmount,
								 "transactionType":"Sale"
								}*/
										{
											  "userName": $scope.custUserId,
											  "password": $scope.password,
											  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != null ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
											  "customerEmail": $scope.patientDetails.email,
											  "invoiceNo": Date.now().toString(),
											  "transactionAmount": $scope.paymentDetails.credAmount,
											  "transactionType": "1",  
											  "payerName": $scope.patientDetails.firstName,
											  "autoInitiateFlag": "N"
											}


								$scope.selectedJson=JSON.stringify($scope.sale);
								
							}
							if($scope.flagForBharatQr == true){
								$scope.bharatQr=
								{
										  "userName": $scope.custUserId,
										  "password": $scope.password,
										  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != null ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
										  "customerEmail": $scope.patientDetails.email,
										  "invoiceNo": Date.now().toString(),
										  "transactionAmount": $scope.paymentDetails.credAmount,
										  "transactionType": "24",  
										  "payerName": $scope.patientDetails.firstName,
										  "autoInitiateFlag": "N"
										}

								$scope.selectedJson=JSON.stringify($scope.bharatQr);
							}
							if($scope.flagForSMSpay == true){
								$scope.smsPay=
								{
										  "userName": $scope.custUserId,
										  "password": $scope.password,
										  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != null ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
										  "customerEmail": $scope.patientDetails.email,
										  "invoiceNo": Date.now().toString(),
										  "transactionAmount": $scope.paymentDetails.credAmount,
										  "transactionType": "19",  
										  "payerName": $scope.patientDetails.firstName,
										  "autoInitiateFlag": "N"
										}

								$scope.selectedJson=JSON.stringify($scope.smsPay);
							}
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							var data={
									patientId:$scope.patientIdForReport != null ? $scope.patientIdForReport :$scope.patientDetails.patientId,
									transactionAmount:parseInt($scope.paymentDetails.credAmount),
									organizationId:$scope.organizationId,
									unitId:$scope.unitId,
									createdBy:$scope.createdBy,
									updatedBy:$scope.updatedBy,
									status:'A',
									createdDate:$scope.dt,
									updatedDate:$scope.dt,
									posMachineDeviceId:$scope.deviceid,
									selectedPaymentModeJson:$scope.selectedJson,
								}
					        var URI = BASE_URL + ROOT_URL + "/api/savePOSMachineDeviceMaster";
						 
							BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {

								if(response.data.status != "success"){
									growl.error(response.data.message);
								}else{
									growl.success(response.data.message);
								}

								$rootScope.stopLoaderPopup();
							});
					 }else{
						 growl.warning("Please select device.");
						 $rootScope.stopLoader();
					 }
					 }
					 /*********************/
					 
					$scope.checkValidatePinPad = function()
					{
						if(($scope.registeredPinPadDevice != undefined || $scope.registeredPinPadDevice != null || $scope.registeredPinPadDevice != "") 
								&& $scope.registeredPinPadDevice == "POS Machine"){
							$scope.getResposeList=[];
							$scope.getGeneratedResponse=[];
							$rootScope.startLoaderPopup();
							var data = {
									patientId : $scope.patientIdForReport != null ? $scope.patientIdForReport :$scope.patientDetails.patientId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									transactionAmount:$scope.paymentDetails.credAmount,
//									requestedUniqueId :$scope.requestedUniqueId
							}
							
							
							var URI = BASE_URL + ROOT_URL + "/api/updatePOSMachineDetails";
							BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								
								if (response.data.status == "success")
									{
										console.log("saved Card Details");
										if(response.data.object != null && response.data.object.listObject.length > 0){
											$scope.getResposeList=response.data.object.listObject;
											$scope.getGeneratedResponse = JSON.parse($scope.getResposeList[0].generatedReponse);
											$scope.paymentDetails.credCardHolderName=$scope.getGeneratedResponse.cardHolderName;
//											$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
											$scope.paymentDetails.credAmount=$scope.getGeneratedResponse.transactionAmount;
											if($scope.flagForBharatQr == false){
												$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber.slice($scope.getGeneratedResponse.transactionCardNumber.length - 4);
											}else{
												$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber;
											}
											$scope.paymentDetails.transactionNo=$scope.getGeneratedResponse.transactionID;
											$scope.paymentDetails.credExpiryDate=moment($scope.getGeneratedResponse.expiryDate).toDate();
											$scope.paymentDetails.requestedUniqueId=$scope.getGeneratedResponse.billNumber/*$scope.getGeneratedResponse.invoiceNumber*/;
											$scope.paymentDetails.creditDebitCardType=$scope.getGeneratedResponse.creditDebitCardType;
											$scope.paymentDetails.transactionTypeName=$scope.getGeneratedResponse.transactionTypeName;
											$scope.paymentDetails.transactionDetails=($scope.paymentDetails.creditDebitCardType!= null && $scope.paymentDetails.creditDebitCardType!= "" ? $scope.paymentDetails.creditDebitCardType + ' || ':"")
											+ ($scope.paymentDetails.credCardNumber!= null && $scope.paymentDetails.credCardNumber!= "" ? $scope.paymentDetails.credCardNumber + ' || ':"")+ ($scope.paymentDetails.transactionNo!= null && $scope.paymentDetails.transactionNo!= "" ? $scope.paymentDetails.transactionNo + ' || ':"") + ($scope.paymentDetails.transactionTypeName!= null && $scope.paymentDetails.transactionTypeName!= "" ? $scope.paymentDetails.transactionTypeName:"")
											
											$scope.getGeneratedResponse.cardType=angular.lowercase($scope.getGeneratedResponse.cardType);
											angular.forEach($scope.cardTypeList,function(value, key){
												value.cardTypeName1=angular.lowercase(value.cardTypeName);
												if(value.cardTypeName1 == $scope.getGeneratedResponse.cardType){
													$scope.paymentDetails.credCardTypeId=value.cardTypeId;
												}
											});
											$scope.addPaymentDetails();
											$rootScope.stopLoaderPopup();
										}
										else {
											
											growl.error("Please wait user did not paid amount.",{
												title : ERROR_MSG
											});
											$rootScope.stopLoaderPopup();
										}

									}
								else if(response.data.status == "error"){
									
									growl.error("Please wait user did not paid amount.",{
										title : ERROR_MSG
									});
									$rootScope.stopLoader();
								}
								
							});
						}else{
						var data ={
								organizationId :$scope.organizationId,
								unitId : $scope.unitId,
								createdBy : $scope.userId,
								createdDate : moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
								tillId:'50002',
								patientId: $scope.patientDetails.patientId,
								// ip : IAddress
						};
						$scope.pinpadAmounFlag = true;
						$rootScope.startLoaderPopup();
						var URI9 = BASE_URL + ROOT_URL + "/Billing/savePinPanDetaild";
						BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							
							if (response.data.status == "success")
								{
								
									$scope.uniqueNumber = response.data.object.uniqueNumber;
									$scope.pinpadTransectionId = response.data.object.pinpadTransectionId;
									$scope.ipAddress = response.data.object.ipAddress;
									var dataForPinPad = {
										    amount: parseFloat($scope.dueAmount) * 100,
											tranKey: $scope.uniqueNumber
									}
									
							
									var urlprotocol = $location.protocol();
									var finalUrl = "http://127.0.0.1:9991/api/sale";
									var URI = finalUrl;
									BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										 if (response !=  undefined){
											if(response.data.rspCode == '100' || response.data.rspCode == '003' || response.data.rspCode == '000')
												{
													$scope.paymentDetails.credCardHolderName = response.data.cardholderName;
													$scope.paymentDetails.credAmount = parseFloat(response.data.amount)/100;
													$scope.schema = response.data.scheme;
													var pan = response.data.pan;
													var lastFour = pan.substr(pan.length - 4);
													$scope.paymentDetails.credCardNumber = lastFour;
													$scope.paymentDetails.transactionNo = response.data.retRefNr;
													
													
													var cardTypeId = $.grep($scope.cardTypeList, function (card) {
									    	            return card.pinpadScheme == $scope.schema;
									    	        })[0].cardTypeId;
													$scope.paymentDetails.credCardTypeId = cardTypeId;
													
													$scope.resString = JSON.stringify(response);
													console.log("resString",$scope.resString); 
													
													var dataUPDATE = {
															response : $scope.resString,
															pinpadTransectionId : $scope.pinpadTransectionId,
															patientId : $scope.patientDetails.patientId,
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
															createdDate : moment(new Date()).format("YYYY-MM-DD"),
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
															
															growl.error("Somting wrong with save data of card..",{
																title : ERROR_MSG
															});
															$rootScope.stopLoaderPopup();
														}
														
													});
													
													growl.success(response.data.message,{
														title : "SUCCESS!"
													});
													
													$rootScope.stopLoaderPopup();
												}
											else
												{
												    $scope.pinpadAmounFlag = false;
													growl.error(response.data.message,{
														title : ERROR_MSG
													});
													$rootScope.stopLoaderPopup();
													return false;
												}
										 	} else{
											 
											 $scope.pinpadAmounFlag = false;
											 growl.warning("Pin Pad Not Configure",{
													title : "Warning"
												});
											 $rootScope.stopLoaderPopup();
												return false;
										 }
									});
									
									$rootScope.stopLoaderPopup();
								}
							else {
								 $scope.pinpadAmounFlag = false;
								growl.error("Someting went wrong..!!",{
									title : ERROR_MSG
								});
								$rootScope.stopLoaderPopup();
							}
							
						});
					}
					}
					
					$scope.clearCardFields =  function(){
						$scope.pinpadAmounFlag = false;
						$scope.flagForSale=false;
						 $scope.flagForSMSpay=false;
						 $scope.flagForBharatQr=false;
						 $scope.objPosMachine.deviceName = undefined;
						 $scope.objPosMachine.isSale = false;
						 $scope.objPosMachine.isBharatQr = false;
						 $scope.objPosMachine.isSMSpay = false;
						$scope.paymentDetails = angular.copy({paymentModeId:$scope.paymentDetails.paymentModeId,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
						$scope.calculateTotalDueRefAmount();
					}
					
				
					  $scope.openEmailPopup = function(){
			              	
			             	 if($scope.emailOtp == null){
			             		 $('#sendOtpOnMail').modal('show');
			             		 
			             	}
			             	 
			              }
			              
			              $scope.clearEmail = function(){
			              	
			             	 $('#sendOtpOnMail').modal('hide');
			             		 $scope.emailOtp = null;
			              }
			              
			              
			              
			              
			              $scope.sendMailOtp = function(){
			             	 
			             	 $rootScope.startLoader();
			             	 
			                  var data ={
			                 		 toMailId : $scope.patientDetails.email,
			                          unitId : $scope.unitId,
			                          organizationId : $scope.organizationId
			                  }; 
			                      
			                  var URI = BASE_URL + ROOT_URL+"/autoMail/sendOTP";
			                  BillingGenericService
			                          .serviceAction(METHOD_POST,URI, data)
			                          .then(
			                                  function(response) {
			                                      if (response.data.status == "success"){
			                                     	 
			                                     	 growl.success("OTP send on entered email id.",
																{
																	title : 'Success!'
																});
			                                     	 
			                                     	 $scope.emailOtp  = response.data.object;
			                                          $rootScope.stopLoader();
			                                      }else{
			                                          $rootScope.stopLoader();
			                                      }
			                                  });
			                  }
			              
			              $scope.validateOtp1 = function(){
			               	
			             	 if($scope.emailOtp == $scope.entredOpt){
			             		 $('#sendOtpOnMail').modal('hide');
			             		 $scope.emailOtp = null;
			             		 $scope.otpvalidated = false;
			             		 $scope.storeEmailId = $scope.patientDetails.email;
			             	}else{
			             		$scope.otpvalidated = true;
			             			growl.warning("Please enter valid OTP",{title : "WARNING!"
									});
									return false;
			             	}
			              }
					
			              $scope.checkEmail = function(){
                         	 
                         	// if($scope.registrationFieldObj.email == "Y"){
                         		 
                         		 if($scope.storeEmailId != null  && $scope.storeEmailId == $scope.patientDetails.email ){
                         			 $scope.otpvalidated = false;
                         		 }else{
                         			 
                         			if($scope.patientDetails.email != null && $scope.patientDetails.email != "" &&  $scope.emailValidationRequired == 'Y'){
                         				$scope.otpvalidated = true;
                         			}else{
                         				$scope.otpvalidated = false;
                         			}
                         		 }
										
									//}
                         	 
                          }

					
					
					 $scope.razorPayObject={"razorMobileNo":null,"razorAmount":null,"razorPayRequired":true,"razorUniqueId":null,"razorPaidAmount":null};
					 $scope.sendPaymentLink = function(){
						 if($scope.exceededBillAmountSettlement==false)
						  {
						  if(parseFloat($scope.dueAmount)<parseFloat($scope.razorPayObject.razorAmount))
							  {
							  $scope.razorPayObject.razorAmount= parseFloat($scope.dueAmount).toFixed(2);
							  growl.warning("Entered amount is greater than current amount");
							  return false;
							  }
						  }
							if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
								{
								growl.error("Please enter mobile Number",
										{
											title : 'Error!'
										});
								    return false;
								 }
							else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
							{
								growl.error("Please enter amount",
										{
											title : 'Error!'
										});
								    return false;
								 }
							else{
							$rootScope.startLoader();
								var data = {
										  "organizationId" : $scope.organizationId,
										  "unitId" : $scope.unitId,
										  "mobileNo":$scope.razorPayObject.razorMobileNo,
										  "razorAmount":$scope.razorPayObject.razorAmount,
										  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",
										  "email":$scope.patientDetails.email,
										  "name":$scope.patientDetails.firstName,
										  
								}
								var URI = BASE_URL + ROOT_URL + "/razorPayPaymentLink";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											
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
						
						
						
						$scope.validateRazerPay = function(){
							

							if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
								{
								growl.error("Please enter mobile Number",
										{
											title : 'Error!'
										});
								    return false;
								 }
							else {
							$rootScope.startLoader();
								var data = {
										  "organizationId" : $scope.organizationId,
										  "unitId" : $scope.unitId,
										  "mobileNo":$scope.razorPayObject.razorMobileNo,
										  
								}
								var URI = BASE_URL + ROOT_URL + "/validateRazorPayPaymentLink";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
											$rootScope.stopLoader();
											$scope.razorPayObject.razorPaidAmount = response.data.object.razorAmount;
											$scope.razorPayObject.razorUniqueId = response.data.object.uniqueId;
											paymentCollFactory.addPaymentDetails($scope);
											$scope.calculateTotalDueRefAmount();
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
						
						
						
					$scope.PayByRazorPayment = function(){
						if($scope.exceededBillAmountSettlement==false)
						  {
						  if(parseFloat($scope.dueAmount)<parseFloat($scope.razorPayObject.razorAmount))
							  {
							  $scope.razorPayObject.razorAmount= parseFloat($scope.dueAmount).toFixed(2);
							  growl.warning("Entered amount is greater than current amount");
							  return false;
							  }
						  }
						if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
							{
							growl.error("Please enter mobile Number",
									{
										title : 'Error!'
									});
							    return false;
							 }
						else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
						{
							growl.error("Please enter amount",
									{
										title : 'Error!'
									});
							    return false;
							 }
						else{
						$rootScope.startLoader();
							var data = {
									  "organizationId" : $scope.organizationId,
									  "unitId" : $scope.unitId,
									  "mobileNo":$scope.razorPayObject.razorMobileNo,
									  "razorAmount":$scope.razorPayObject.razorAmount,
									  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",
									  "email":$scope.patientDetails.email,
									  "name":$scope.patientDetails.firstName,
									  
							}
							var URI = BASE_URL + ROOT_URL + "/razorPayPaymentLink";
							BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success")
								{
										$rootScope.stopLoader();
										$window.open(response.data.object.shortUrl, '_blank');
										
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
					
					
					
					 $scope.sendPaymentLinkCCAvenue = function(){
							
							if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
								{
								growl.error("Please enter mobile Number",
										{
											title : 'Error!'
										});
								    return false;
								 }
							else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
							{
								growl.error("Please enter amount",
										{
											title : 'Error!'
										});
								    return false;
								 }
							else{
							$rootScope.startLoader();
								var data = {
										  "organizationId" : $scope.organizationId,
										  "unitId" : $scope.unitId,
										  "mobileNo":$scope.razorPayObject.razorMobileNo,
										  "requestAmount":$scope.razorPayObject.razorAmount,
										  "emailId":$scope.patientDetails.email,
										  "name":$scope.patientDetails.firstName,
										  "patientId": $scope.patientDetails.PatientId, 
										  "createdBy": $scope.userId, 
										  "updatedBy": $scope.userId,
										  
								}
								var URI = BASE_URL + ROOT_URL + "/sendCCAvenueLink";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											
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
						
						
						
						$scope.validateRazerPayCCAvenue = function(){
							

							if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
								{
								growl.error("Please enter mobile Number",
										{
											title : 'Error!'
										});
								    return false;
								 }
							else {
							$rootScope.startLoader();
								var data = {
										  "organizationId" : $scope.organizationId,
										  "unitId" : $scope.unitId,
										  "mobileNo":$scope.razorPayObject.razorMobileNo,
										  "patientId": $scope.patientDetails.PatientId, 
										  
								}
								var URI = BASE_URL + ROOT_URL + "/validateCCAvenuePayment";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
											$rootScope.stopLoader();
											$scope.razorPayObject.razorPaidAmount = response.data.object.paidAmount;
											$scope.razorPayObject.razorUniqueId = response.data.object.orderNo;
											//paymentCollFactory.addPaymentDetails($scope);
											$scope.addPaymentDetails();
											$scope.calculateTotalDueRefAmount();
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
						
						
						
					$scope.PayByRazorPaymentCCAvenue = function(){
						
						if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
							{
							growl.error("Please enter mobile Number",
									{
										title : 'Error!'
									});
							    return false;
							 }
						else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
						{
							growl.error("Please enter amount",
									{
										title : 'Error!'
									});
							    return false;
							 }
						else{
						$rootScope.startLoader();
							var data = {
									  "organizationId" : $scope.organizationId,
									  "unitId" : $scope.unitId,
									  "mobileNo":$scope.razorPayObject.razorMobileNo,
									  "requestAmount":$scope.razorPayObject.razorAmount,
									  "emailId":$scope.patientDetails.email,
									  "name":$scope.patientDetails.firstName,
									  "patientId": $scope.patientDetails.PatientId, 
									  "createdBy": $scope.userId, 
									  "updatedBy": $scope.userId,
									  
							}
							var URI = BASE_URL + ROOT_URL + "/sendCCAvenueLink";
							BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success")
								{
										$rootScope.stopLoader();
										$window.open(response.data.object.payLink, '_blank');
										
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
		
					
					 $scope.openMobilePopup = function(){
		                 	
	              		 $('#sendOtpOnMobile').modal('show');
	              		 
	                    }
	                   
	                   $scope.clearMobile = function(){
	                     	
	                  	 $('#sendOtpOnMobile').modal('hide');
	                  		 $scope.entredOptMobile = null;
	                   }
	                   
	                   $scope.sendMobileOtp = function(){
	                  	 
	                  	 $rootScope.startLoader();
	                  	 
	                       var data ={
	                      		 mobileNumber : $scope.patientDetails.mobileNumber,
	                               unitId : $scope.unitId,
	                               organizationId : $scope.organizationId
	                       }; 
	                           
	                       var URI = BASE_URL + ROOT_URL+"/autoMail/sendOTPonMobile";
	                       BillingGenericService
	                               .serviceAction(METHOD_POST,URI, data)
	                               .then(
	                                       function(response) {
	                                           if (response.data.status == "success"){
	                                          	 
	                                          	 growl.success("OTP send on entered mobile number.",
															{
																title : 'Success!'
															});
	                                          	 
	                                          	 $scope.mobileOtp  = response.data.object;
	                                               $rootScope.stopLoader();
	                                           }else{
	                                               $rootScope.stopLoader();
	                                           }
	                                       });
	                       }
	                   
	                   $scope.validateOtpMobile = function(){
	                      	
	                  	 if($scope.mobileOtp == $scope.entredOptMobile){
	                  		 $('#sendOtpOnMobile').modal('hide');
	                  		 $scope.mobileOtp = null;
	                  		 $scope.otpvalidatedMobile = false;
	                  	}else{
	                  		$scope.otpvalidatedMobile = true;
	                  			growl.warning("Please enter valid OTP",{title : "WARNING!"
								});
								return false;
	                  	}
	                   }
	                   
	                   $scope.checkMobileNumber = function(){
	           			 if( $scope.patientDetails.mobileNumber != null && $scope.patientDetails.mobileNumber != "" && $scope.mobileValidationRequired == 'Y'){
									$scope.otpvalidatedMobile = true;
								}else{
									$scope.otpvalidatedMobile = false;
								}
	                    }
	                   
	                   $scope.couponCode = '';
	            	   $scope.validateBtn = true;
	            	   $scope.openCouponPopup = function(){
	            		   $scope.getBillAmountForAppointment();
	            		   $scope.validateBtn = false;
	            		   $scope.couponCode = '';
	            		   
	         			  $timeout( function(){
	         				 angular.element('#couponPopoupAppointment').modal('show');
	                     },2000);
	            		   
	            		  
	            	   } 
	            	  
	            	   $scope.closeCouponPopup = function(){
	            		   angular.element('#couponPopoupAppointment').modal('hide');
	            	   }
	            	   $scope.patientCouponTemplateMapperId = null;
	            	   $scope.applyCoupon = function(){
		           		   
		           		   $scope.validateBtn = true;
		           		   var checkDiscount = false;
		           				angular.forEach($scope.serviceOrderList, function (value, key) 
		           					{
		           						if(value.discountPercentage > 0 && value.patientCouponTemplateMapperId == null)
		           						{
		           							checkDiscount = true;
		           						}
		           					});
		           				
		           		   if(checkDiscount == true){
		           			   
		           			   $scope.validateBtn = false;
		           			   angular.element('#couponPopoupAppointment').modal('hide');
		           			   growl.warning("Please cancel applied discount",
		           						{
		           							title : 'Warning!'
		           						});
		           			   return false;
		           		   }
		           				
		           		   if( $scope.couponCode == undefined ||  $scope.couponCode == null ||  $scope.couponCode =='')
		           			{
		           			   $scope.validateBtn = false;
		           			growl.warning("Please enter coupon code",
		           					{
		           						title : 'Warning!'
		           					});
		           			    return false;
		           			 }
		           		   
		           		   if($scope.serviceOrderList == undefined || $scope.serviceOrderList == null ||  $scope.serviceOrderList.length == 0)
		           			{
		           			   $scope.validateBtn = false;
		           			growl.warning("No service's added ",
		           					{
		           						title : 'Warning!'
		           					});
		           			    return false;
		           			 }
		           		   
		           		   angular.element('#couponPopoupAppointment').modal('hide');
		           			var specDocObj = {
		           		    			unitId:$scope.unitId,
		           		    			couponCode:$scope.couponCode
		           		    }
		           		    var URI = BASE_URL + ROOT_URL+ "/api/billing/Patientcoupon/counsumCouponTemplate";
		           			BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
		           					NOTIFICATION_MSG_STATUS_FALSE)
		           			.then(function(response) {
		           						$scope.validateBtn = false;
		           						if (response.data.status == "success")
		           							{
		           							
		           							var couponData = response.data.object;
		           							var checkCouponId = 0;
		           							var serviceCheck = false;
		           							if(couponData.serviceId != null){
		           								checkCouponId = 0;
		           								serviceCheck = true;
		           							}else if(couponData.subSpecialityId != null){
		           								checkCouponId = 1;
		           							}else if(couponData.specialityId != null){
		           								checkCouponId = 2;
		           							}
		           							var indexList = [];
		           							var continueLoop = false;
		           					    	var i = 0;
		           					    	
		           					    	var totalBillAmount = parseFloat(0);
		           					    	$scope.discountAmount = parseFloat(0);
		           					    	$scope.patientCouponTemplateMapperId = null;
		           					    	angular.forEach($scope.serviceOrderList, function (value, key) {
		           					    		 continueLoop = false;
			           					    		if(value.orderDetailsPackageMapperDtosList[0].packageId == null && value.status == 'A' && (checkCouponId == 0 && value.serviceId == couponData.serviceId && serviceCheck == true && value.netAmount > 0))
			           					    			{
			           					    			serviceCheck = false;
			           					    			continueLoop = true;
			           					    			}else if(value.orderDetailsPackageMapperDtosList[0].packageId == null  && value.status == 'A' && (checkCouponId == 1 && value.subDepartmentId == couponData.subSpecialityId)){
			           					    				continueLoop = true;
			           					    			}else if(value.orderDetailsPackageMapperDtosList[0].packageId == null  && value.status == 'A' && (checkCouponId == 2 && value.specialityId == couponData.specialityId)){
			           					    				continueLoop = true;
			           					    			}
			           					    		
			           					    		if(continueLoop == true){
			           					    			
			           					    			
			           					    			if(couponData.percentage !=0){
			           			    						
			           			    						var totalAmt = (parseFloat(1) * parseFloat(value.netAmount)).toFixed(2);
			           			    						var disPerTmp = parseFloat(0);
			           			    						if($scope.cancelService)
			           			    							disPerTmp = couponData.percentage;
			           			    						else
			           			    							disPerTmp =  couponData.percentage;
			           			    						$scope.serviceOrderList[i].discountPercentage = couponData.percentage ;
			           				  	    		  	        
			           				  	    		  	    	
			           				  	    		  		    $scope.serviceOrderList[i].discount =(((parseFloat(totalAmt))*(isNaN(disPerTmp) ? 0: disPerTmp))/100).toFixed(2);	
			           				  	    		  		    var withConDic =parseFloat(totalAmt) - (parseFloat( $scope.serviceOrderList[i].discount));
			           				  	    		  		
			           				  	    		  			$scope.serviceOrderList[i].netAmount =  parseFloat(withConDic).toFixed(2);
			           				  	    		  			$scope.serviceOrderList[i].selfPayable = $scope.serviceOrderList[i].netAmount;
			           				  	    		  			$scope.serviceOrderList[i].hospitalShare = parseFloat(withConDic);
			           				  	    		  			$scope.serviceOrderList[i].creditPayable =parseFloat(0);
			           				  	    		  			$scope.serviceOrderList[i].patientCouponTemplateMapperId =  couponData.patientCouponTemplateMapperId;
			           						    	     
			           					    				
			           					    			}else{
			           			    					
			           			    						
			           				    		    		$scope.serviceOrderList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
			           				    		    		
			           			    						var totalAmt = (parseFloat(1) * parseFloat(value.netAmount)).toFixed(2);
			           			    						var disPerTmp = parseFloat(0);
			           			    						
			           			    						  var withConDic = parseFloat(0);
			           			    						  
			           			    						if(couponData.amount >= (parseFloat(totalAmt))){
			           			    							
			           			    							couponData.amount =  parseFloat(couponData.amount) - (parseFloat(totalAmt))
			           			    							$scope.serviceOrderList[i].discountPercentage = 100;
			           			    							$scope.serviceOrderList[i].discount  = (parseFloat(totalAmt));
			           			    							$scope.serviceOrderList[i].patientCouponTemplateMapperId =  couponData.patientCouponTemplateMapperId;
			           			    							
			           			    						}else{
			           			    							
			           			    							if(couponData.amount > 0){
			           			    								
			           			    								disPerTmp = (parseFloat(couponData.amount)/parseFloat(parseFloat(totalAmt)))*100;
			           				    							$scope.serviceOrderList[i].discountPercentage = disPerTmp;
			           				    							$scope.serviceOrderList[i].discount  = parseFloat(couponData.amount);
			           				    							$scope.serviceOrderList[i].patientCouponTemplateMapperId =  couponData.patientCouponTemplateMapperId;
			           			    							}else{
			           			    								$scope.serviceOrderList[i].discountPercentage = 0;
			           				    							$scope.serviceOrderList[i].discount  = 0;
			           				    							$scope.serviceOrderList[i].patientCouponTemplateMapperId = null;
			           			    							}
			           			    							couponData.amount = 0;
			           			    						}
			           			    						
			           			    						var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.serviceOrderList[i].discount));
			           			    						
			           			    						
			           				  	    		  			$scope.serviceOrderList[i].netAmount =   parseFloat(withConDic).toFixed(2);
			           				  	    		  			$scope.serviceOrderList[i].selfPayable = $scope.serviceOrderList[i].netAmount;
			           				  	    		  			$scope.serviceOrderList[i].creditPayable =parseFloat(0);
			           				  	    		  			$scope.serviceOrderList[i].hospitalShare = parseFloat(withConDic);
			           				  	    		  			
			           			    						}
			           					    	     }
			           					    		totalBillAmount = parseFloat(totalBillAmount) + parseFloat($scope.serviceOrderList[i].netAmount);
			           					    		$scope.discountAmount = parseFloat($scope.discountAmount) + parseFloat($scope.serviceOrderList[i].discount);
			           					    		$scope.patientCouponTemplateMapperId = $scope.serviceOrderList[i].patientCouponTemplateMapperId;
		           					    	});
										$scope.dueAmount = totalBillAmount;
										$scope.oldDueAmount = totalBillAmount;
										$scope.billDueAmount = totalBillAmount;
										$scope.payeeDueAmtFinal = 	totalBillAmount;
										$scope.payeeDueAmtStatic = totalBillAmount;
		           							}else{
		           								growl.error(response.data.message,
		           										{
		           											title : 'Error!'
		           										});
		           							}
		           					});
		           		
		           		}
	                   
	            	   $scope.cancelCoupon = function(){
	            		   $scope.discountAmount = parseFloat(0);
  					    	$scope.patientCouponTemplateMapperId = null;
  					    	$scope.getBillAmountForAppointment();
	            	   }
	                   
	            	   
	            	   
	            	   $scope.settlementedWithCoupon = function(){

	                       setTimeout(function(){
	                      	 $('#setCash').trigger('click')
	                       }, 300);
	  		       		$scope.flagForSMSpay = false;
	  		    		$scope.flagForBharatQr = false;
	  		    		$scope.flagForSale = false;
	  		    		$scope.smsPay = {};
	  		    		$scope.bharatQr = {};
	  		    		$scope.sale = {};
	  		    		$scope.objPosMachine.deviceName = "";
	  		    		$scope.objPosMachine.isSale = false;
	  		    		$scope.objPosMachine.isBharatQr = false;
	  		    		$scope.objPosMachine.isSMSpay = false;
	  		       		$scope.skipMpesaValidation = false;
	  		       		$scope.pinpadAmounFlag = false;
	  		       		$scope.fromCollectScreen = true;
	  		    		 $scope.send=false;
	  		    		 $scope.resend=false;
	  		    		 $scope.setPaymentModeId(1);
	  		    		 $scope.validatTextFieldFlag = true;
	  		    		 $scope.transferToActiveDeposit = 'Y';
	  		    		 $scope.excessAmount = parseFloat(0).toFixed(2);
	  		    		 $scope.listPaymentDetails = [];
	  		    		 $scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
	  		    		 $scope.totalAmtShow  = parseFloat(0);
	  		    		 $scope.totalAmtShow = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
		  		    		var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
		  		    		$scope.listPaymentDetails.push(paymentDetails);
		  		    		$scope.settleBill();   
	            	   }
	            	   
	                   
						} ]);