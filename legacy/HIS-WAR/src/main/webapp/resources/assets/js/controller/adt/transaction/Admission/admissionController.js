'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:admissionController
 * @description #admissionController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'admissionController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'GenericServiceFileUpload',
						'growl',
						'$state',
						'$stateParams',
						'BillingGenericService',
						'GenericServiceParamHeader',
						'reportService',
						'$location',
						'loyaltiCardCallFactory',
						'$interval',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,GenericServiceFileUpload, growl,
								$state, $stateParams,BillingGenericService,GenericServiceParamHeader,reportService,$location,loyaltiCardCallFactory,$interval) {


							/* init() function for form object create. */
							$scope.init = function() {
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.mpesaRequired = cookieObject.mpesaRequired;
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
								$scope.dateTimeZone= JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								var backDateAdmissionDays;
								if (cookieObjectAppConfigSecond != undefined) {
								backDateAdmissionDays = cookieObjectAppConfigSecond.backDateAdmissionDays;
								}
								$scope.backDateAdmissionDaysScope = backDateAdmissionDays;
								if(backDateAdmissionDays!= '' && backDateAdmissionDays!= null && backDateAdmissionDays >1){
									$scope.backDateAdmissionDaysScope = backDateAdmissionDays;
								}
								//ganesh
								$scope.depositAmount =0;
								$scope.paidDeposit = 0;
								$scope.balanceDeposit = 0;
								$scope.resend =false;
								$scope.send =false;
							    $scope.flagForSale=false;
							     $scope.flagForPay=false;
							     $scope.flagForBharatQr=false;
								$scope.filepreview = "";
								$('#admission_check').prop('checked', false);
								$scope.admissionCheck=false;
								 /**PatientImages*/
							    $scope.patobj={
							    		patientImage1:"",
							    		filepreviewMLC : ""
							    }
								$scope.fromTimeDoa = moment().format("HH:mm");
								
								$scope.depositStatus = false;
								
								$scope.kinFlag = true;


								console.log($scope.dateTimeZone);
								// for datePicker
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate: new Date()
								};
			
								$scope.dateOptionsFutureDate = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate: new Date()
									};
								
								$scope.dateOptionsDoaPdd = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate: new Date()
									};
								
								
								if(cookieObjectAppConfig.isBackDatedAdmission=='Y'){
									
									var date = new Date();
									$scope.dateOptionsDoa = {
											formatYear : 'yyyy',
											showWeeks : false,
											maxDate : new Date(),
									        minDate : date.setDate((new Date()).getDate() - $scope.backDateAdmissionDaysScope),
												
										};
										}else{
											var date = new Date();
									$scope.dateOptionsDoa = {
											formatYear : 'yyyy',
											showWeeks : false,
											maxDate : new Date(),
									        minDate : new Date(),
										}
										}
								$scope.dateOptionsTrancs = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
								
								$scope.loadingMsg="";
								$scope.dateOptionsPaymentEntitlement = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
								$scope.dateOptionsPaymentEntitlementStart = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
									};
								$scope.todaysDate=moment(new Date()).format('DD/MM/YYYY');
								// for datePicker for kin details
								$scope.dateOptionsKinDetails = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
								$scope.openDatePickerExpiry = function(index) {
									// alert("in");
									// $scope.expiry.opened = true;
									for (var i = $scope.kinDetailsList.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.expiry.opened[i] = true;
										} else {
											$scope.expiry.opened[i] = false;
										}
									}
								};
								
								$scope.expiry = {
									opened : [ false ],

								};
								
								  // object for Diagnosis List added by sneha
							     $scope.dataListDiagnosis = [ {
										diagnosisId : '',
										diagnosisType:'1',
										type : '1',
										remark : '',
										diagnosisNameText:'',
										isDiagnosisName : 'N',
										diagnosisCode:'',
										diagnosisName :null
									}];
							    
							    $scope.referenceNumber = "";
								$scope.disablePDD=false;
								$scope.enableDisabledApply=false;	
								$scope.enableDisabledValidated=false;
								$scope.validatTextFieldFlag = true;
								$scope.setDayCareDischargeDate=function(isDayCare){
									$scope.bedCategoryList =[];
									 if(isDayCare=='Y'){
										 $scope.datepickerPdd=new Date();
										 $scope.disablePDD=true;
										angular.forEach($scope.allBedCategoryList,function(value,index){
											var flag=false;
											angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
												//for day Care
												if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==3){
													flag=true;
												}
											});
											if(flag==true){
												$scope.bedCategoryList.push(value);
											}
										});
										
										 $scope.patientDetails.patientDischargeDate= moment($scope.datepickerPdd).format('DD/MM/YYYY');
									}else{
										//$scope.datepickerPdd="";
										$scope.disablePDD=false;
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
										var a=moment($scope.datepickerPdd).format('DD/MM/YYYY');
										var b=moment($scope.datepicker).format('DD/MM/YYYY');
										if(a==b){
										 var new_date = moment($scope.datepicker).add(5, 'days');
										 $scope.datepickerPdd = new Date(new_date);
										}
										 $scope.patientDetails.patientDischargeDate =  moment( $scope.datepickerPdd).format('DD/MM/YYYY');
									}
									 
									 $scope.paymentEntitleMentList=
											[{
												'paymentEntitlementName':"Self",
												'paymentEntitlementId':"1",
												'companyName':"Self",
												'tariffName':"Self Tariff",
												'contractName':"",
												'insuranceName':"",
												'insuranceNumber':"",
												'startDate':"",
												'endDate':"",
												'priority':"1",
												'companyId':$scope.selfPayeeId,
												'tariffId':$scope.defaultSelfTariffId,
												'contractId':null,
												'typeId' : 1,
												'insuredAmount' : '',
												'billingBedCategoryId':null
											}];
									 
									 $scope.paymentEntitleMentCorporate=
											[{
												'companyId':"",
												'contractId':"",
												//'statrDate':"",
												//'endDate':"",
											}];
										
										$scope.paymentEntitleMentInsurance=
											[{
												'companyId':"",
												'associateCompanyId':"",
												'contractId':"",
												//'statrDate':"",
												//'endDate':"",
												'insuranceNumber':"",
												'insuredAmt':""
											}];



										
										if($scope.patientDetails.patientId > 0){
											var URI = BASE_URL + ROOT_URL + "/api/opd/lastSponsors/"+$scope.patientDetails.patientId+"/3/"+$scope.unitId;
											BillingGenericService
													.serviceAction(METHOD_GET, URI, {},
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																console.log(response);
																if (response.data.status == "success") {
																	$scope.forCheckingPatientClassAndEntitlement = response.data.listObject;
																	if( response.data.object > 0){
																		$scope.admissionRequestOrAdmissionObj.patientClassId = response.data.object;
																	}
																	if($scope.forCheckingPatientClassAndEntitlement.length > 0){
																		$scope.paymentEntitleMentList = [];
																		 $scope.priorityList = [];
																		 var i = 1;
																		 angular.forEach($scope.forCheckingPatientClassAndEntitlement,function(value,key)
																					{
																				  		var newArr = {};
																				  		      newArr.paymentEntitlementId =value.paymentEntitlementId;
																				  	          newArr.contractName = value.contractName;
																				  		      newArr.insuranceName = value.insuranceDesc;
																				  		      newArr.insuranceNumber = value.insuranceNumber;
																				  		      newArr.startDate = value.validityFrom;
																				  		      newArr.endDate = value.validityTo;
																				  		      newArr.priority = value.priorityId;
																				  		      newArr.companyId = value.companyId;
																				  		      newArr.insuranceId = value.insuranceId;
																				  		      newArr.contractId = value.contractId;
																				  		      newArr.insuredAmount = value.insuredAmt;
																				  		      newArr.claimNumber = value.claimNumber;
																					  		  newArr.patientSponsorDetailsId = value.patientSponsorDetailsId;
																				  		if(value.paymentEntitlementId ==1){
																				  			  newArr.startDate = null;
																				  			  newArr.endDate = null;
																				  			  newArr.paymentEntitlementName = "Self";
																				  			  newArr.companyName = "Self";
																				  			  newArr.tariffName = "Self Tariff";
																				  			  newArr.tariffId = $scope.defaultSelfTariffId;
																				  			  newArr.companyId = $scope.selfPayeeId;
																				  		}else{
																				  			  newArr.paymentEntitlementName = value.paymentEntitlementDesc;
																				  			  newArr.companyName = value.companyDesc;
																				  			  newArr.tariffName = value.tariffDesc;
																				  			  newArr.tariffId = value.tariffId;
																				  		}
																				  		
																				  		if(value.priorityId == 1){
																				  			$scope.appointmentMasterObj.paymentEntitlementTypeId = value.paymentEntitlementId;
																				  		}
																				  		
																				  		$scope.paymentEntitleMentList.push(newArr);
																				  		
																				  	
																				  		var prioObj = {
																								  id:i,
																								  value:i,
																							  }
																							  $scope.priorityList.push(prioObj);
																				  		i++;
																					});
																	}
																}

															});
														}
										if($scope.admissionRequestOrAdmissionObj.floorId != ""){
											$scope.admissionRequestOrAdmissionObj.floorId = "";
											$scope.admissionRequestOrAdmissionObj.wardId = "";
											$scope.admissionRequestOrAdmissionObj.wardName = "";
											$scope.admissionRequestOrAdmissionObj.roomId = "";
											$scope.admissionRequestOrAdmissionObj.roomName = "";
											$scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId = "";
											$scope.admissionRequestOrAdmissionObj.currentBillingBedDesc  = "";
											$scope.admissionRequestOrAdmissionObj.bedId = "";
											$scope.admissionRequestOrAdmissionObj.bedNumber = "";
											$scope.admissionRequestOrAdmissionObj.bedCategoryId= "";
											$scope.admissionRequestOrAdmissionObj.bedCategoryDesc = "";
											$scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId = "";
											$scope.admissionRequestOrAdmissionObj.changeBillingBedDesc = "";
											$scope.patientDetails.bedNumber =  "";
										}
										
										
									/*	//deposit COde
										$scope.getDepositMatrixList($scope.itemSelecteds.billingBedCategoryId);
										$scope.commonChangeDeposit();*/
									 
								}
								
								$scope.visitPassObj=function(){

						        	$scope.visiterPassMaster={
						        			"visitorPassTypeId":0,
						        			"passNumber":""
						        	}
						        	$scope.listVisiterPassMaster=[];
									var visiterPassMaster = angular.copy($scope.visiterPassMaster);
						        	$scope.listVisiterPassMaster.push(visiterPassMaster);
						        	
								}
								$scope.visitPassObj();
								 $scope.addVisitorPass = function(index) 
								 {
									 $scope.visiterPassMaster={
							        			"visitorPassTypeId":0,
							        			"passNumber":""
							        	}
							        	$scope.listVisiterPassMaster.push($scope.visiterPassMaster);
									};
									
									$scope.removeVisitorPass = function(index){
										if(index != 0){
											$scope.listVisiterPassMaster.splice(index,1);
										}
									}
									$scope.visitorPassTypeList = [];
								$scope.getVisitorPassList=function(){
									
									if($scope.admissionRequestOrAdmissionObj.bedCategoryId != null && $scope.admissionRequestOrAdmissionObj.bedCategoryId != "" ){
										if($scope.visitorPassTypeList.length == 0){
											var URI = BASE_URL + ROOT_URL + "/api/unit/getVisitorPassMapperListByBedCategoryNVisitType";
											var commonObj={
													organisationId : $scope.organizationId,
													unitId : $scope.unitId,
													bedCategoryId  :$scope.admissionRequestOrAdmissionObj.bedCategoryId,
													visitTypeId : $scope.admissionRequestOrAdmissionObj.isDayCare=='Y' ? 3 : 2
											}
											GenericService
													.serviceAction("POST", URI, commonObj)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	$scope.visitorPassTypeList = response.data.listObject;

																}
															}); 
										}
									}
								}
								
								$scope.openDatePickerPdd = function() {
									$scope.datepickerPdd.opened = true;
								};

								$scope.datepickerPdd = {
									opened : false
								};
								
								
								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								$scope.openDatePickerForETransfer = function() {
									$scope.datepickerEtransfer.opened = true;
								};

								$scope.datepickerEtransfer = {
									opened : false
								};

                                  var currentDateNew = new Date().toLocaleString('en-US', { timeZone: $scope.dateTimeZone.timeZone});
                                    $scope.datepicker = new Date(currentDateNew);
								/* $scope.datepicker = new Date( $scope.admissionObj.oldDoa);
									$scope.datepickerPdd*/
								
	
								$scope.btnDisableEnable = true;
								/*$scope.openDatePickerContractStartDate = function() {
									$scope.datepickerCons.opened = true;
								};

								$scope.datepickerCons = {
									opened : false
								};
								*/
								
								$scope.openDatePickerContractStartDate = function(index) {

									for (var i = $scope.paymentEntitleMentCorporate.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerCons.opened[i] = true;
										} else {
											$scope.datepickerCons.opened[i] = false;
										}
									}
								};

								$scope.datepickerCons = {
									opened : [ false ],

								};
								
								
								/*$scope.openDatePickerContractEndDate = function() {
									$scope.datepickerConE.opened = true;
								};

								$scope.datepickerConE = {
									opened : false
								};*/
								
								
								$scope.openDatePickerContractEndDate = function(index)
								{

									for (var i = $scope.paymentEntitleMentCorporate.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerConE.opened[i] = true;
										} else {
											$scope.datepickerConE.opened[i] = false;
										}
									}
								};

								$scope.datepickerConE = {
									opened : [ false ],

								};
								
								
								/*$scope.openDatePickerInsuranceStartDate = function() {
									$scope.datepickerInss.opened = true;
								};

								$scope.datepickerInss = {
									opened : false
								};*/
								
								$scope.openDatePickerInsuranceStartDate = function(index) {

									for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerInss.opened[i] = true;
										} else {
											$scope.datepickerInss.opened[i] = false;
										}
									}
								};

								$scope.datepickerInss = {
									opened : [ false ],

								};
								
								/*$scope.openDatePickerInsuranceEndDate = function() {
									$scope.datepickerInsE.opened = true;
								};

								$scope.datepickerInsE = {
									opened : false
								};*/
								
								//Patient Search DOB Start
								$scope.openDatePickerSearch = function() {
									$scope.datepicker2.opened = true;
								};
								
								$scope.datepicker2 = {
										opened : false
									};
								//Patient Search DOB End
								
								
								$scope.openDatePickerInsuranceEndDate = function(index) {

									for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerInsE.opened[i] = true;
										} else {
											$scope.datepickerInsE.opened[i] = false;
										}
									}
								};

								$scope.datepickerInsE = {
									opened : [ false ],

								};
								
								
								 //var new_date = moment($scope.datepicker).add(5, 'days');
								var currentDateNewPdd = new Date().toLocaleString('en-US', { timeZone: $scope.dateTimeZone.timeZone});
								 $scope.datepickerPdd = new Date(currentDateNewPdd);
								  $scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
								  $scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
								  $scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
								  $scope.razorMobileNo = '';
								  $scope.razorAmount = '';
								  $scope.fileObj = {};
								 //addded by dinesh for getting default config start
									$scope.unitCountryId = cookieObject.unitCountryId;
									$scope.unitStateId = cookieObject.unitStateId;
									$scope.unitDistrictId = cookieObject.unitDistrictId;
									$scope.unitCityId = cookieObject.unitCityId;
									$scope.unitAreaId = cookieObject.unitAreaId;
									$scope.storeId=cookieObject.storeId;
									
									 //addded by dinesh for getting default config end
								$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.employeeId = cookieObject.employeeId;
								$scope.sexMasterList=[];
								$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
						  		$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
								$scope.pinpadAmounFlag = false;
								$scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
								$scope.isBackDatedAdmission = cookieObjectAppConfig.isBackDatedAdmission;
								$scope.isPharmacologicalValidationSkipIp = cookieObjectAppConfig.isPharmacologicalValidationSkipIp;
								$scope.fieldsdisabled = false;
								$('#admission_check').prop('checked', false);
								$scope.admissionCheck=false;
								$scope.hitMpesaApi = false;
								if($scope.corEmpVerificationFlag){
									$scope.enableDisabledApply=false;
								}else{
									$scope.enableDisabledValidated=false;
									$scope.enableDisabledApply=false;
								}
								
								$scope.selectedPatient = 'N';
								
								$scope.getBedPopUpFlag = false;
								$scope.expiryDateDisabled = true;
								$scope.diableDropDown=false;
								$scope.patientList = [];
								$scope.kinsList =[];
								

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								$scope.patientSearchObj = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"identificationNo":"",
										"patientType"	  :"1",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId
								}
								
								$scope.initReferralObj = function(){
							    	$scope.referral = {
											"referralTypeId"     	: "",
											"referralName"     		: "",
											"address"       		: "",
											"countryId"      	    :$scope.unitCountryId!=undefined && $scope.unitCountryId !=null ? $scope.unitCountryId : '',
											"stateId" 			    :  $scope.unitStateId!=undefined && $scope.unitStateId !=null ? $scope.unitStateId : '',
											"districtId" 		:  $scope.unitDistrictId!=undefined && $scope.unitDistrictId !=null ? $scope.unitDistrictId : '',
											"cityId" 			: $scope.unitCityId!=undefined && $scope.unitCityId !=null ? $scope.unitCityId : '',
											"areaId" 			: $scope.unitAreaId!=undefined && $scope.unitAreaId !=null ? $scope.unitAreaId : '',
											"email" 			: "",
											"contact" 			: "",
											"contactPerson"		: "",
											"unitId"            :$scope.unitId,
											"organizationId"    :$scope.organizationId
									}
							    			
							    	$scope.diableDropDown=false;
							    	
							    }
								$scope.initReferralObj();
								
								$scope.bedsearch = {
									floorId : 0,
									nursingStationId : 0,
									bedCategoryId : 0,
									bedStatusId : 0,
									wardId : 0,
									isVirtual : 'N',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,

								}
								$scope.admissionRequestOrAdmissionObj ={
										admissionNoteId : '',
										floorId : '',
										wardId :'',
										wardName :'',
										roomId : '',
										roomName : '',
										reservedBedCategoryId :'',
									    reservedBedCategoryDesc :'',
									    currentBillingBedCategoryId :'',
									    currentBillingBedDesc :'',
										pdd :'',
										doa : '',
										mealPrefId : '',
										attendentMealPrefId : '',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										patientId :'',
										tPatientId :'',
										specialityId :'',
										visitTypeId :2,
										bedId : '',
										bedNumber : '',
										paymentEntitlementId :'',
										doctorId : '',
										patientCategoryId : '',
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										kinArray : '',
										createdDate :'',
										updatedDate : '',
										admissionTypeId: '', //For OP
										bedCategoryWaitingListId:'',
										referralTypeId : '',
										referralId : '',
										reasonId :'',
										admissionNoteNumber : '',
										bedCategoryId :'',
									    bedCategoryDesc :'',
									    changeBillingBedCategoryId :'',
									    changeBillingBedDesc :'',
									    remark : '',
									    refferalRemark : '',
									    kinDetailsId : '',
									    listKinDetailsDto	:$scope.kinDetailsList,
									    treatingDoctorSpecialityId : "",
									    treatingDoctorId : "",
									    patientClassId : "",
									    isDayCare:'N',
									   //For MLC
									    	accompaniedBy : '',
											firNo        :'',
											policeBuckleNo :'',
											policeStationName:'',
											mlcContactNo:'',
											mlcRelationId:'',
											mlcRelationId:'',
											mlcRemark:'',
											modeOfArrival:'',
											priorityId : '',
											isMedicoLegal : 'N',
									    //end MLC
								}
								
								//for paymententitlement
								$scope.paymentEntitleMentList=
									[{
										'paymentEntitlementName':"Self",
										'paymentEntitlementId':"1",
										'companyName':"Self",
										'tariffName':"Self Tariff",
										'contractName':"",
										'insuranceName':"",
										'insuranceNumber':"",
										'startDate':"",
										'endDate':"",
										'priority':"1",
										'companyId':$scope.selfPayeeId,
										'tariffId':$scope.defaultSelfTariffId,
										'contractId':null,
										'typeId' : 1,
										'insuredAmount' : '',
										'billingBedCategoryId':null
									}];
								
								$scope.paymentEntitleMentCorporate=
									[{
										'companyId':"",
										'contractId':"",
										//'statrDate':"",
										//'endDate':"",
									}];
								
								$scope.paymentEntitleMentInsurance=
									[{
										'companyId':"",
										'associateCompanyId':"",
										'contractId':"",
										//'statrDate':"",
										//'endDate':"",
										'insuranceNumber':"",
										'insuredAmt':""
									}];
								//End Paymententitlement
								
								
								$scope.pendingAdmissionRequest = {};
								$scope.itemSelecteds = {};
								
								$scope.appointmentMasterObj = {
										paymentEntitlementTypeId : 1
								};
								
								$scope.initCoEmp=function(){
									$scope.corEmpObj={
											typeOfCoEmp:'3',
//											employeeNumber:"",
											note:"",
											employeeName:"",
											relation:"",
											medicalCardNo:""
												
									}
								}
								$scope.initCoEmp();
								$scope.initEmpObj=function(){
									$scope.empObj={
											employeeNumber:""
												
									}
								}
								$scope.initEmpObj();
								
								$scope.patientDetails = {};
								 $scope.patientDetails.patientDischargeDate =  moment( $scope.datepickerPdd).format('DD/MM/YYYY');
								 $scope.patientDetails.dateOfAdmission = moment(new Date()).format('DD/MM/YYYY');
								 
								 var dateDoa = moment( new Date(), 'DD/MM/YYYY');
									var datePdd = moment( $scope.datepickerPdd, 'DD/MM/YYYY');
									
									$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
									
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
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
										});

								/*var bedObj = {
									bedCategoryId : $scope.admissionNoteData.bedCategoryId,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDLIST;
								GenericService
										.serviceAction("POST", URI, bedObj)
										.then(
												function(response) {
													$scope.activeBedList = [];

													if (response.data.status == "success") {
														$scope.activeBedList = response.data.listObject;

													}
												});*/
						
						
						
					        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
					    GenericService
					        .serviceAction(METHOD_GET, URI,
					                NOTIFICATION_MSG_STATUS_FALSE)
					        .then(
					                function(response) {
					                    console.log(response);
					                    if (response.data.status == "success"){
					                        
					                        $scope.patientTypeList = response.data.listObject;
					                        console.log("list",$scope.patientTypeList);
					                    }
					                    
					                });
					        
					    
								var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
								BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									$scope.sexMasterList = response.data.listObject;
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

							/*	var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDCATEGORYLISTBYUNIT;
										//+"/adt/bedCategory/availableBeds"
*/								
								var URI = BASE_URL + ROOT_URL +"/adt/bedCategory/availableBeds";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.bedCategoryList=[];
														$scope.allBedCategoryList=response.data.listObject;
														$scope.dayCareIpBillingBedCateList=response.data.object.dayCareIpBedCategoryList;
                                                        if($scope.admissionRequestOrAdmissionObj.isDayCare=='Y'){
                                                            angular.forEach($scope.allBedCategoryList,function(value,index){
                                                                var flag=false;
                                                                angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
                                                                    //for day Care
                                                                	if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==3){
                                                                        flag=true;
                                                                    }
                                                                });
                                                                if(flag==true){
                                                                    $scope.bedCategoryList.push(value);
                                                                }
                                                            });
                                                         }else{
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
                                                         }
												}
													
												});
								
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEMEALPREFERENCELIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.mealPreferenceList = [];

													if (response.data.status == "success") {
														$scope.mealPreferenceList = response.data.listObject;

													}
												});

								var data = {};
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDSTATUSLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.bedStatusList = [];

													if (response.data.status == "success") {
														$scope.bedStatusList = response.data.listObject;

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
													}
												});
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPREFIXLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.prefixList = [];

													if (response.data.status == "success") {
														$scope.prefixList = response.data.listObject;

													}
												});
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVERELATIONLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.relationList = [];

													if (response.data.status == "success") {
														$scope.relationList = response.data.listObject;

													}
												});
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEIDENTIFICATION;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.identificationList = [];

													if (response.data.status == "success") {
														$scope.identificationList = response.data.listObject;
														

													}
												});
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.countryList = [];

													if (response.data.status == "success") {
														$scope.countryList = response.data.listObject;
														console
																.log($scope.countryList);

													}
												});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEADMISSIONTYPELIST+"/"+$scope.organizationId;
						GenericService
								.serviceAction("GET", URI, {})
								.then(
										function(response) {
											$scope.admissionTypeList = [];

											if (response.data.status == "success") {
												$scope.admissionTypeList = response.data.listObject;
												console.log("$scope.admissionTypeList",$scope.admissionTypeList);

											}
										});
						
						var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
						var obj={organizationId : $scope.organizationId};
							BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
									$scope.referralTypeList = [];
									if (response.data.status == "success") {
										$scope.referralTypeList = response.data.listObject;
									}
							});
							
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEREASONLIST;
					
					BillingGenericService
							.serviceAction(METHOD_POST, URI,
									commonObj,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.reasonList = response.data.listObject;
									});
					
					var URI = BASE_URL + ROOT_URL
					+ GETACTIVEMEALPREFERENCELIST;
			GenericService
					.serviceAction("POST", URI, commonObj)
					.then(
							function(response) {
								$scope.mealPreferenceList = [];

								if (response.data.status == "success") {
									$scope.mealPreferenceList = response.data.listObject;

								}
							});
			
			var URI = BASE_URL + ROOT_URL
			+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
	GenericService
			.serviceAction("GET", URI, {})
			.then(
					function(response) {
						$scope.billingBedList = [];

						if (response.data.status == "success") {
							$scope.billingBedList = response.data.listObject;
							console.log("billingBedList",$scope.billingBedList);

						}
					});
	
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVETARIFFMASTERLIST;
					GenericService
						.serviceAction("POST", URI, commonObj)
						.then(
								function(response) {
									$scope.tariffList = [];
									if (response.data.status == "success") {
										$scope.tariffList = response.data.listObject;
										console.log("ActiveTariffMasterList",$scope.tariffList);
									}
								});
					
					$scope.priorityList = [{
						id:1,
						value:1
					}]

							}

							$scope.init();
							$scope.finalOtReservationList = [];
						    $scope.loadMoreData = function()
						    {
						        $scope.offset+=200;
						        $scope.loadMoreFlag=true;
						        $scope.searchPatient($scope.patientSearchObj);
						    }
						    $scope.offset=0;
						    $scope.loadMoreFlag=false;
						    $scope.noOfRecordsPerPage=200;
						    $scope.patientList = [];
							$scope.searchPatient = function(patientsearchObj){
								
								if(patientsearchObj.patientId=="" && patientsearchObj.uhIdNumber == "" && patientsearchObj.patientName == "" && patientsearchObj.genderId == "" && patientsearchObj.mobileNo == ""  && patientsearchObj.identificationTypeId == "" && patientsearchObj.identificationNo == "" && patientsearchObj.birthDate=="")
								{
									growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"});
									return false;
								}
								if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 2)
								{
									growl.warning("Please enter atleast 2 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									return false;
								}	
							
								
								if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
									patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
								} 
								
								$rootScope.startLoader();
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
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
								BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.listObject==null){
										growl.warning(response.data.message,{
											title : ERROR_MSG
										});
										$scope.patientSearchObj.birthDate="";
										return false;
									}
									
									if(Object.keys(response.data.listObject).length  == 0){
										growl.warning("No Result Found!",{
											title : ERROR_MSG
										});
										$scope.patientSearchObj.birthDate="";
										return false;
									}else if(patientsearchObj.patientId!=null && patientsearchObj.patientId>0 && (response.data.listObject).length  == 1){
										$scope.patientList.push(response.data.listObject[0]);
										$scope.getPatientDetailsByIndex(0);
									}else{
										 $scope.patientList = [];
										angular.forEach(response.data.listObject, function (value, key) {
										if(value.registrationTypeId != 5 && value.registrationTypeId !=2)
											{
												$scope.patientList.push(value);
											}
									 	});
										$scope.patientSearchObj.birthDate="";
									}
									
								});
								
							}
							// check the corporate verification is required or not
							$scope.corEmpVerificationFlag=false;
							$scope.getCoEmpVerificationReq = function(){
								try{
									var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
									var obj = {
												organizationId : $scope.organizationId,
												unitId         :$scope.unitId
										    }
									BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.unitConfigurationObject = response.data.listObject[0];
											if($scope.unitConfigurationObject.isCorporateEmpVerificationReq=='Y'){
												$scope.corEmpVerificationFlag=true;
												$scope.enableDisabledApply=false;	
												$scope.enableDisabledValidated=false;

											}else{
												$scope.corEmpVerificationFlag=false;
												$scope.enableDisabledApply=true;	
												$scope.enableDisabledValidated=false;

											}
										}
									});
								}catch (e) {
									console.log("Exception",e.message);
								}
							}
							// check the corporate verification is required or not
							$scope.getCoEmpVerificationReq();
							
							$rootScope.getDefaultRegistrationValidationDetails();
							
							//kin details
							$scope.getKinDetailsByPatientId = function(){
								var URI = BASE_URL + ROOT_URL + KIN_DETAILS + PATIENT + SLASH + $scope.patientDetails.patientId  + UNIT + SLASH +  $scope.unitId  + ORG + SLASH + $scope.organizationId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success") {
										$scope.kinsList = response.data.listObject;
									}
								});
							}
							
							
							$scope.selectedPatientId = "";
							$scope.getPatientDetailsByIndex = function(index){
								$("#patientSelect_"+index).prop("checked",true);
								
								if($scope.patientList[index].isDeath =='Y')
								{
							 		growl.warning("Patient is marked as death",{
										title : WARNING_MSG
									});
							 		$scope.init();
									$scope.initKinDetails();
									$scope.activeBedList=[];
									$scope.medicationDetails = [];
									$scope.diagnosisMedicationDetailList = [];
									$scope.addedServiceOrdersForDisplay = [];
									$scope.dataListDiagnosis = [];
									$scope.dataListDiagnosis = [{
										diagnosisId : '',
										diagnosisType:'1',
										type : '1',
										remark : '',
										diagnosisNameText:'',
										isDiagnosisName : 'N',
										diagnosisCode:'',
										diagnosisName :null
									}];
									$scope.selectedOrderSet = [];
									$scope.selectedOrderSet1 = [];
									$scope.setSecondTabLevel(1);
									$scope.disableDayCareChk=false;
									$scope.initDepositMaster();
									$scope.setSecondTabLevel(1);
							 		return false;
								}
								if($scope.patientList[index].admissionId){
									var message = "";
									if($scope.unitId==$scope.patientList[index].admissionUnitId)
					            		message = "Patient is already admitted in IP...!!";
					            	else
					            		message = "Patient is already admitted in another unit...!!";
									
									growl.warning(message,{
										title : WARNING_MSG
									});
									$scope.patientList = [];
									return false;
								}
								
								if($scope.patientList[index].registrationTypeId == 4){
								 growl.warning("Please complete registration conversion process...!!",{

										title : WARNING_MSG
									});
								 	$scope.patientList = [];
									return false;
								}
								
								var currentDate = moment(new Date()).format('YYYY-MM-DD');
								if($scope.patientList[index].identificationExpiryDate !=null && currentDate > $scope.patientList[index].identificationExpiryDate)
								{
									growl.warning("Patient's ID number has expired. Kindly update patient's ID number..!!",{
										title : WARNING_MSG
									});
								}
								
								$scope.abscondPatientFlag="";
								if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
									$scope.initReferralObj();
									$scope.patientDetails = {};
									console.log("$scope.patientList",$scope.patientList[index]);
									///$rootScope.startLoader();
									$scope.patientDetails.age=($scope.patientList[index].age);
									$scope.patientDetails.patientId=($scope.patientList[index].patientId);
									$scope.patientDetails.patientName=($scope.patientList[index].patientName);
									$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
									$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
									$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
									$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
									$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
									$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
									$scope.countryCallingCode =($scope.patientList[index].countryCallingCode);
									/*var code=($scope.patientList[index].countryCallingCode).split('+');
									$scope.countryCallingCode=code[1];*/
	                                $scope.patientDetails.patientDischargeDate =  moment( $scope.datepickerPdd).format('DD/MM/YYYY');
	                                $scope.patientDetails.dateOfAdmission = moment(new Date()).format('DD/MM/YYYY');
	                                if($scope.patientList[index].mobileNo!=null){
	                                	  $scope.mobileNumber = parseInt($scope.patientList[index].mobileNo);
	                                }
	                                var dateDoa = moment( new Date(), 'DD/MM/YYYY');
									var datePdd = moment( $scope.datepickerPdd, 'DD/MM/YYYY');
									
									$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
	                                
	                                
	                                $scope.selectedPatientId = ($scope.patientList[index].patientId);
									
									$scope.selectedPatient = 'Y';
									
									/*$scope.patientDetails.uhIdNumber = $scope.admissionObj.uhidnumber;
									$scope.patientDetails.patientName = $scope.admissionObj.patientFullName;
									$scope.patientDetails.genderCode = $scope.admissionObj.genderCode;
									$scope.patientDetails.genderId = $scope.admissionObj.genderId;
									$scope.patientDetails.age = $scope.admissionObj.age;
								    $scope.patientDetails.birthDate = $scope.admissionObj.dob;
								    $scope.patientDetails.patientId = $scope.admissionObj.patientId;*/
								    
									
									$scope.appointmentMasterObj.patientId=$scope.patientList[index].patientId;
									
									
								    $scope.admissionRequestOrAdmissionObj.patientId = $scope.patientList[index].patientId;
								    $scope.admissionRequestOrAdmissionObj.tPatientId = $scope.patientList[index].tPatientId;
								    $scope.admissionRequestOrAdmissionObj.patientCategoryId = $scope.patientList[index].patientCategoryId;
								    
								    $scope.appointmentMasterObj.paymentEntitlementTypeId = 1;
									
									$scope.getKinDetailsByPatientId();
									$scope.kinDetailsList[0].patientId=$scope.patientDetails.patientId;
									
									
									if($scope.patientDetails.patientId > 0){
										var URI = BASE_URL + ROOT_URL + "/api/opd/lastSponsors/"+$scope.patientDetails.patientId+"/2/"+$scope.unitId;
										BillingGenericService
												.serviceAction(METHOD_GET, URI, {},
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															if (response.data.status == "success") {
																$scope.forCheckingPatientClassAndEntitlement = [];
																$scope.forCheckingPatientClassAndEntitlement = response.data.listObject;
																if( response.data.object > 0){
																	$scope.admissionRequestOrAdmissionObj.patientClassId = response.data.object;
																}
																
																if($scope.forCheckingPatientClassAndEntitlement!=null && Object.keys($scope.forCheckingPatientClassAndEntitlement).length > 0){
																	$scope.paymentEntitleMentList = [];
																	 $scope.priorityList = [];
																	 var i = 1;
																	 angular.forEach($scope.forCheckingPatientClassAndEntitlement,function(value,key)
																				{
																			  		var newArr = {};
																			  		newArr.paymentEntitlementId =value.paymentEntitlementId;
																			  		
																			  	   newArr.contractName = value.contractName;
																			  		newArr.insuranceName = value.insuranceDesc;
																			  		      newArr.insuranceNumber = value.insuranceNumber;
																			  		      newArr.startDate = value.validityFrom;
																			  		      newArr.endDate = value.validityTo;
																			  		      newArr.priority = value.priorityId;
																			  		      newArr.companyId = value.companyId;
																			  		      newArr.insuranceId = value.insuranceId;
																			  		      newArr.contractId = value.contractId;
																			  		      newArr.insuredAmount = value.insuredAmt;
																			  		      newArr.claimNumber = value.claimNumber;
																				  		  newArr.patientSponsorDetailsId = value.patientSponsorDetailsId;
																			  		if(value.paymentEntitlementId ==1){
																			  			  newArr.paymentEntitlementName = "Self";
																			  			  newArr.companyName = "Self";
																			  			  newArr.tariffName = "Self Tariff";
																			  			  newArr.tariffId = $scope.defaultSelfTariffId;
																			  			  newArr.companyId = $scope.selfPayeeId;
																			  		}else{
																			  			  newArr.paymentEntitlementName = value.paymentEntitlementDesc;
																			  			  newArr.companyName = value.companyDesc;
																			  			  newArr.tariffName = value.tariffDesc;
																			  			  newArr.tariffId = value.tariffId;
																			  		}
																			  		
																			  		if(value.priorityId == 1){
																			  			$scope.appointmentMasterObj.paymentEntitlementTypeId = value.paymentEntitlementId;
																			  		}
																			  		
																			  		$scope.paymentEntitleMentList.push(newArr);
																			  		
																			  	
																			  		var prioObj = {
																							  id:i,
																							  value:i,
																						  }
																						  $scope.priorityList.push(prioObj);
																			  		i++;
																				});
																}
															}

														});
													}
									
								    //$rootScope.stopLoader();
								 }else{
									    $scope.abscondIndex=index;
										$scope.abscondPatientFlag='Y';
										angular.element('#abscondedPatientPopUp').modal('show');
								 }
								loyaltiCardCallFactory.getLoyaltyCardList($scope.patientList[index].patientId,$scope);
								$scope.getOTReservationList($scope.patientList[index].patientId);

								var dataward = {};
								
								var URI = BASE_URL + ROOT_URL
								+ "/api/adt/getWardForDirectTransfer/"+$scope.patientDetails.genderId+"/"+2+"/"+$scope.unitId;
								GenericService
								.serviceAction("GET", URI, dataward)
								.then(
										function(response) {
											$scope.wardList = [];

											if (response.data.status == "success") {
												$scope.wardList = response.data.listObject;

											}
										});
							}
							
							$scope.creatingEncounterForAbscondedPatient=function(){
								var index=$scope.abscondIndex;
								$scope.initReferralObj();
								$scope.patientDetails = {};
								console.log("$scope.patientList",$scope.patientList[index]);
								///$rootScope.startLoader();
								$scope.patientDetails.age=($scope.patientList[index].age);
								$scope.patientDetails.patientId=($scope.patientList[index].patientId);
								$scope.patientDetails.patientName=($scope.patientList[index].patientName);
								$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
								$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
								$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
								$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
								$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
								
								$scope.selectedPatient = 'Y';
								$scope.appointmentMasterObj.patientId=$scope.patientList[index].patientId;								
								
							    $scope.admissionRequestOrAdmissionObj.patientId = $scope.patientList[index].patientId;
							    $scope.admissionRequestOrAdmissionObj.tPatientId = $scope.patientList[index].tPatientId;
							    $scope.admissionRequestOrAdmissionObj.patientCategoryId = $scope.patientList[index].patientCategoryId;
							    $scope.patientDetails.patientDischargeDate =  moment( $scope.datepickerPdd).format('DD/MM/YYYY');
                                $scope.patientDetails.dateOfAdmission = moment(new Date()).format('DD/MM/YYYY');
                                if($scope.patientList[index].mobileNo!=null){
                                      $scope.mobileNumber = parseInt($scope.patientList[index].mobileNo);
                                }
                                var dateDoa = moment( new Date(), 'DD/MM/YYYY');
                                var datePdd = moment( $scope.datepickerPdd, 'DD/MM/YYYY');

                                $scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
							    
							    $scope.appointmentMasterObj.paymentEntitlementTypeId = 1;
								
								$scope.getKinDetailsByPatientId();
								$scope.kinDetailsList[0].patientId=$scope.patientDetails.patientId;
							}
							$scope.selectItem = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.pendingAdmissionRequest) {
									$scope.itemSelecteds = item;
								}
							}
							
							$scope.getPddForHeader = function()
							{
								 $scope.patientDetails.patientDischargeDate =  moment( $scope.datepickerPdd).format('DD/MM/YYYY');
							}
							
								$scope.checkDatePdd = function(){
								 if(!angular.isDate($scope.datepickerPdd)){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
									 var new_date = moment($scope.datepicker).add(5, 'days');
									 $scope.datepickerPdd = new Date(new_date);
										return false;
								 }else if($scope.datepicker > $scope.datepickerPdd)
									 {
									 growl
										.warning(
												'DOA should be less then PDD',
												{
													title : 'Warning!'
												});
									 var new_date = moment($scope.datepicker).add(5, 'days');
									 $scope.datepickerPdd = new Date(new_date);
										return false;
									 }
							}
	
							$scope.selectBed = function()
							{
								console.log($scope.itemSelecteds);
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select bed.',
													{
														title : 'Warning!'
													});
								}else
									{
									angular.element('#bedStatusModalForDayCare').modal('hide');
									angular.element('#bedStatusModal').modal('hide');
									$scope.admissionRequestOrAdmissionObj.floorId = $scope.itemSelecteds.floorId;
									$scope.admissionRequestOrAdmissionObj.wardId = $scope.itemSelecteds.wardId;
									$scope.admissionRequestOrAdmissionObj.wardName = $scope.itemSelecteds.wardName;
									$scope.admissionRequestOrAdmissionObj.roomId = $scope.itemSelecteds.roomId;
									$scope.admissionRequestOrAdmissionObj.roomName = $scope.itemSelecteds.roomDesc;
									$scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId = $scope.itemSelecteds.billingBedCategoryId;
									$scope.admissionRequestOrAdmissionObj.currentBillingBedDesc  = $scope.itemSelecteds.billingBedDesc;
									$scope.admissionRequestOrAdmissionObj.bedId = $scope.itemSelecteds.bedId;
									$scope.admissionRequestOrAdmissionObj.bedNumber = $scope.itemSelecteds.bedNumber;
									$scope.admissionRequestOrAdmissionObj.bedCategoryId= $scope.itemSelecteds.bedCategoryId;
									$scope.admissionRequestOrAdmissionObj.bedCategoryDesc = $scope.itemSelecteds.bedCategoryDesc;
									$scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId = $scope.itemSelecteds.billingBedCategoryId;
									$scope.admissionRequestOrAdmissionObj.changeBillingBedDesc = $scope.itemSelecteds.billingBedDesc;
									$scope.patientDetails.bedNumber =  $scope.itemSelecteds.bedNumber;
									
									//deposit COde
									$scope.getDepositMatrixList($scope.itemSelecteds.billingBedCategoryId);
									$scope.commonChangeDeposit();
									}
							}
							
							 var isPriorityZeroExists;
								function checkPriorityZeroOrNot(arr) {
									return arr
											.some(function(arr) {
												if(parseInt(arr.priority) == 0 || arr.priority == "")
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
							$scope.getActiveBedList = function()
							{
								
								//$scope.paymentEntitleMentList..
								$scope.activeBedList = [];
								$scope.bedsearch = {
										floorId : 0,
										nursingStationId : 0,
										bedCategoryId : 0,
										bedStatusId : 0,
										wardId : 0,
										isVirtual : 'N',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,

									};
								
								isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
								  if (isPriorityZeroExists) {
									
									  growl.warning("Please select priority of all payment entitlement list.",{	
										title : "WARNING"
									});
									// $scope.tab = 3;
									return;
									
								  }
								  else{
									  $scope.getBedPopUpFlag = true;
								
								var paymentEntitlementObj = $.grep($scope.paymentEntitleMentList, function (doc) {
						            return doc.priority == 1 && doc.paymentEntitlementId != 1;
						        })[0];
								
								console.log("paymentEntitlementObj",paymentEntitlementObj);
								$scope.payEntObj = paymentEntitlementObj;
								$rootScope.startLoader();
								$scope.loadingMsg="";
								//if(paymentEntitlementObj ==undefined || paymentEntitlementObj == null )
								//{
								if($scope.admissionRequestOrAdmissionObj.reservedBedCategoryId>0)
								{
									$scope.bedsearch.bedCategoryId= $scope.admissionRequestOrAdmissionObj.reservedBedCategoryId;
								var bedObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									}
									if($scope.admissionRequestOrAdmissionObj.isDayCare=='Y')
									{
										bedObj.isDayCare='Y';
										bedObj.bedCategoryId=$scope.bedCategoryList[0].bedCategoryId;
										$scope.bedsearch.bedCategoryId=$scope.bedCategoryList[0].bedCategoryId;
									}else{
										bedObj.bedCategoryId=$scope.admissionRequestOrAdmissionObj.reservedBedCategoryId;
									}
									var URI = BASE_URL + ROOT_URL
											+ GETACTIVEBEDLIST;
									GenericService
											.serviceAction("POST", URI, bedObj)
											.then(
													function(response) {
														$scope.activeBedList = [];

														if (response.data.status == "success") {
															$scope.activeBedList = response.data.listObject;															
															if($scope.activeBedList.length>0){
																$scope.loadingMsg="";
															}else{
																$scope.loadingMsg="";
															}

														}
													});
								}
								/*else
									{
									  //$scope.getBedPopUpFlag = false;
									}*/
								/* }
								else
									{
									$scope.bedsearch.bedCategoryId=  paymentEntitlementObj.billingBedCategoryId;
									
									
									var bedObj = {
											billingBedCategoryId : paymentEntitlementObj.billingBedCategoryId,
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										}
									
									if($scope.admissionRequestOrAdmissionObj.isDayCare=='Y'){
										bedObj.isDayCare='Y';
										bedObj.bedCategoryId=$scope.bedCategoryList[0].bedCategoryId;
										$scope.bedsearch.bedCategoryId=$scope.bedCategoryList[0].bedCategoryId;
										}
									
										var URI = BASE_URL + ROOT_URL
												+ GETACTIVEBEDLIST;
										GenericService
												.serviceAction("POST", URI, bedObj)
												.then(
														function(response) {
															$scope.activeBedList = [];

															if (response.data.status == "success") {
																$scope.activeBedList = response.data.listObject;

															}
														});
									}*/
							  }
								  //$scope.getBedPopUpFlag = false;
								  $rootScope.stopLoader();
							}
							
					/*		$scope.getReferralMasterList = function(referralTypeId){
								var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByReferralTypeId";
								var obj=
								      { 
										 organizationId:$scope.organizationId,
										 referralTypeId:referralTypeId
								      };
									BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
											$scope.referralMasterList = [];
											if (response.data.status == "success") {
												$scope.referralMasterList = response.data.listObject;
											}
									});
								};*/
								
							
							
					        $scope.searchByreferralId = function(searchKeyword,index){
					        	
								var data={
					        			organizationId: $scope.organizationId,
					        			referralTypeId:$scope.admissionRequestOrAdmissionObj.referralTypeId,
					        			desc: searchKeyword
					        	};
								var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByReferralTypeId/forTypeSearch" 
								
					    		return GenericService.serviceAction("POST",URI,data).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.referralMasterList = [];
					    				$scope.referralMasterList = response.data.listObject;
					    			}
					    			return $scope.referralMasterList;
					    		});
					    	}
					        
					        
					    	$scope.selectReferralId = function(info, model, label,ind) {
								$scope.admissionRequestOrAdmissionObj.referralId = info.referralId;
					    	}

								$scope.getAdmittedDocName = function(docId)
								{
									if(docId > 0)
										{
											var docName = $.grep($scope.doctorListTreating, function (doc) {
									            return doc.doctorId == docId;
									        })[0];
											$scope.patientDetails.doctorFullName =docName.firstName+' '+docName.lastName; 
										}
								}
							$scope.getDoctorBySpeciality = function(id) {

							   if($scope.admissionRequestOrAdmissionObj.isTreatingDoctor=='Y')
                                  {
                                   $scope.admissionRequestOrAdmissionObj.specialityId=$scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId;
                                   $scope.admissionRequestOrAdmissionObj.doctorId=$scope.admissionRequestOrAdmissionObj.treatingDoctorId;
                                   }
                                 const value=id;
                                 console.log(id);
								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorList = [];
													// $rootScope.startLoader();
													//if (response.data.status == "success")
														$scope.doctorList = response.data.listObject
														console.log($scope.doctorList);
												});

							}


                            $scope.checkBox=function(id){
                                           	var pddConfig = $.grep($scope.specialityList, function (mppr) {
                                                 return mppr.specialityId == id;
                                             })[0].pddDays;

                                            var new_date = moment($scope.datepicker).add(pddConfig, 'days');
                                            $scope.datepickerPdd = new Date(new_date);

                                            $scope.patientDetails.patientDischargeDate = moment($scope.datepickerPdd).format('DD/MM/YYYY');


                                            var dateDoa = moment( $scope.datepicker, 'DD/MM/YYYY');
                                            var datePdd = moment( $scope.datepickerPdd, 'DD/MM/YYYY');

                                            $scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd)

                                          if($scope.admissionRequestOrAdmissionObj.isTreatingDoctor=='Y')
                                            {
                                             $scope.admissionCheck=true;
                                             $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId=$scope.admissionRequestOrAdmissionObj.specialityId;
                                             $scope.admissionRequestOrAdmissionObj.treatingDoctorId=$scope.admissionRequestOrAdmissionObj.doctorId;
                                             }
                                             else if($scope.admissionRequestOrAdmissionObj.isTreatingDoctor=='N'){
                                                     $scope.admissionCheck=false;
                                                     $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId="";
                                                     $scope.admissionRequestOrAdmissionObj.treatingDoctorId="";
                                             }
                                          }

							$scope.getDoctorBySpecialityForTreating = function(id) {

								
								var pddConfig = $.grep($scope.specialityList, function (mppr) {
			                         return mppr.specialityId == id;
			                     })[0].pddDays;
								
								var new_date = moment($scope.datepicker).add(pddConfig, 'days');
								$scope.datepickerPdd = new Date(new_date);
								
								$scope.patientDetails.patientDischargeDate = moment($scope.datepickerPdd).format('DD/MM/YYYY');
								
								
								var dateDoa = moment( $scope.datepicker, 'DD/MM/YYYY');
								var datePdd = moment( $scope.datepickerPdd, 'DD/MM/YYYY');
								
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
								
								
								
								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorListTreating = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.doctorListTreating = response.data.listObject;
												});

							}
							$scope.treatingDoctor=function(){
                                if($scope.admissionRequestOrAdmissionObj.isTreatingDoctor=='Y')
                                {
                                 $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId=$scope.admissionRequestOrAdmissionObj.specialityId;
                                 $scope.admissionRequestOrAdmissionObj.treatingDoctorId=$scope.admissionRequestOrAdmissionObj.doctorId;
                                 }

							}

							
							$scope.getDoctorBySpecialityForTreatingRedirect = function(id) {
								
								
								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorListTreating = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.doctorListTreating = response.data.listObject;
												});

							}
							
							
							$scope.disableDayCareChk=false;
							console.log("admissionObj", $stateParams.admissionObj);
							 $scope.admissionObj=$stateParams.admissionObj;
							if ($scope.admissionObj.fromPendingAdmissions==1 && $scope.admissionObj.patientId != undefined || $scope.admissionObj.tPatientId != null) 
							{
								console.log("$scope.admissionObj",$scope.admissionObj);
								if($scope.admissionObj.visitTypeId==3){
									$scope.admissionRequestOrAdmissionObj.isDayCare='Y';
								}
								$scope.isDirectAdmission='N';
								$scope.patientDetails = {};
								$scope.initReferralObj();
								$scope.appointmentMasterObj = {};
								
								$scope.fieldsdisabled = true;
								$("#admission_check").attr("disabled", true);
								$scope.admissionCheck=true;
								$scope.btnDisableEnable = false;
								
								$scope.patientDetails.uhIdNumber = $scope.admissionObj.uhidnumber;
								if($scope.admissionObj.patientFullName != null && $scope.admissionObj.patientFullName != undefined && $scope.admissionObj.patientFullName != '' )
									{
									 $scope.patientDetails.patientName = $scope.admissionObj.patientFullName;
									}
								else
									{
									  $scope.patientDetails.patientName = $scope.admissionObj.firstName+' '+$scope.admissionObj.middleName+' '+$scope.admissionObj.lastName;
									}
								
								$scope.patientDetails.genderCode = $scope.admissionObj.genderCode;
								$scope.patientDetails.genderId = $scope.admissionObj.genderId;
								$scope.patientDetails.age = $scope.admissionObj.age;
							    $scope.patientDetails.birthDate = $scope.admissionObj.dob;
							    $scope.patientDetails.patientId = $scope.admissionObj.patientId;
							    if($scope.admissionObj.frombed == 1 && $scope.admissionObj.frombed != undefined){
							    	 $scope.patientDetails.dateOfAdmission = moment(new Date($scope.admissionObj.oldDoa)).format('DD-MM-YYYY');
							    }else{                                    
							    	$scope.patientDetails.dateOfAdmission =  moment(new Date($scope.admissionObj.oldDoa)).format('DD-MM-YYYY');
							    }
							    $scope.patientDetails.patientDischargeDate =  moment(new Date($scope.admissionObj.oldPdd)).format('DD-MM-YYYY');
							    
							    var dateDoa = moment($scope.admissionObj.datepicker, 'DD/MM/YYYY');
								var datePdd = moment( $scope.admissionObj.datepickerPdd, 'DD/MM/YYYY');
								
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
								
							    $scope.patientDetails.doctorFullName = $scope.admissionObj.doctorFullName;
							    
							    $scope.admissionRequestOrAdmissionObj.admissionNoteId  = $scope.admissionObj.admissionNoteId;
							    $scope.admissionRequestOrAdmissionObj.reservedBedCategoryId = $scope.admissionObj.bedCategoryId;
							    $scope.admissionRequestOrAdmissionObj.reservedBillingBedCategoryId = $scope.admissionObj.billingBedCategoryId;
							    $scope.admissionRequestOrAdmissionObj.reservedBedCategoryDesc = $scope.admissionObj.bedCategoryDesc;
							    $scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId = '';
							    $scope.admissionRequestOrAdmissionObj.currentBillingBedDesc = '';
							    $scope.admissionRequestOrAdmissionObj.pdd =$scope.admissionObj.oldPdd;
							    $scope.admissionRequestOrAdmissionObj.doa = $scope.admissionObj.oldDoa;
							    $scope.admissionRequestOrAdmissionObj.patientId = $scope.admissionObj.patientId;
							    $scope.admissionRequestOrAdmissionObj.tPatientId = $scope.admissionObj.tPatientId;
							    $scope.admissionRequestOrAdmissionObj.specialityId = $scope.admissionObj.doctorSpecialityId;
							    $scope.admissionRequestOrAdmissionObj.visitTypeId = $scope.admissionObj.visitTypeId;
							    $scope.admissionRequestOrAdmissionObj.doctorId = $scope.admissionObj.requestToDoctorId;
							    $scope.admissionRequestOrAdmissionObj.patientCategoryId = $scope.admissionObj.patientCategoryId;
							    $scope.admissionRequestOrAdmissionObj.bedCategoryWaitingListId= $scope.admissionObj.bedCategoryWaitingListId;
							    $scope.admissionRequestOrAdmissionObj.admissionTypeId = $scope.admissionObj.admissionTypeId; 
							    $scope.admissionRequestOrAdmissionObj.reasonId = $scope.admissionObj.reasonId; 
							    $scope.admissionRequestOrAdmissionObj.admissionNoteNumber =$scope.admissionObj.admissionNoteNumber;
							    
							    //start for Treating
							    $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId = $scope.admissionObj.treatingDoctorSpecialityId;
							    $scope.admissionRequestOrAdmissionObj.treatingDoctorId = $scope.admissionObj.treatingDoctorId;
							    //end for Treating	
							    	
							    $scope.appointmentMasterObj.paymentEntitlementTypeId = 1;
							    
							    $scope.admissionRequestOrAdmissionObj.bedCategoryId  = '';
							    $scope.admissionRequestOrAdmissionObj.bedCategoryDesc = '';
							    $scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId ='';
							    $scope.admissionRequestOrAdmissionObj.changeBillingBedDesc = '';
							    
							   // $scope.datepicker = new Date( $scope.admissionObj.oldDoa);
								//$scope.datepickerPdd = new Date($scope.admissionObj.oldPdd);
								
								
								//var ad = $rootScope.getFormatedDateeShase($scope.admissionObj.oldDoa);
								//var dd = $rootScope.getFormatedDateeShase($scope.admissionObj.oldPdd);
								
								$scope.datepicker = new Date($scope.admissionObj.oldDoa);
								$scope.datepickerPdd = new Date($scope.admissionObj.oldPdd);
								
								console.log($scope.admissionObj.oldDoa);
								console.log($scope.admissionObj.oldPdd);
							    
							    $scope.getDoctorBySpeciality($scope.admissionObj.doctorSpecialityId);

							    $scope.getDoctorBySpecialityForTreatingRedirect($scope.admissionObj.treatingDoctorSpecialityId);

							    
							    $scope.getKinDetailsByPatientId();
							    

								if($scope.patientDetails.patientId > 0){
									var URI = BASE_URL + ROOT_URL + "/api/opd/lastSponsors/"+$scope.patientDetails.patientId+"/"+$scope.admissionObj.visitTypeId+"/"+$scope.unitId;
									BillingGenericService
											.serviceAction(METHOD_GET, URI, {},
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														if (response.data.status == "success") {
															$scope.forCheckingPatientClassAndEntitlement = [];
															$scope.forCheckingPatientClassAndEntitlement = response.data.listObject;
															if( response.data.object > 0){
																$scope.admissionRequestOrAdmissionObj.patientClassId = response.data.object;
															}
															if(Object.keys($scope.forCheckingPatientClassAndEntitlement).length > 0){
																$scope.paymentEntitleMentList = [];
																 $scope.priorityList = [];
																 var i = 1;
																 angular.forEach($scope.forCheckingPatientClassAndEntitlement,function(value,key)
																			{
																		  		var newArr = {};
																		  		newArr.paymentEntitlementId =value.paymentEntitlementId;
																		  		
																		  	   newArr.contractName = value.contractName;
																		  		newArr.insuranceName = value.insuranceDesc;
																		  		      newArr.insuranceNumber = value.insuranceNumber;
																		  		      newArr.startDate = value.validityFrom;
																		  		      newArr.endDate = value.validityTo;
																		  		      newArr.priority = value.priorityId;
																		  		      newArr.companyId = value.companyId;
																		  		      newArr.insuranceId = value.insuranceId;
																		  		      newArr.contractId = value.contractId;
																		  		      newArr.insuredAmount = value.insuredAmt;
																		  		if(value.paymentEntitlementId ==1){
																		  			  newArr.paymentEntitlementName = "Self";
																		  			  newArr.companyName = "Self";
																		  			  newArr.tariffName = "Self Tariff";
																		  			  newArr.tariffId = $scope.defaultSelfTariffId;
																		  			  newArr.companyId = $scope.selfPayeeId;
																		  		}else{
																		  			  newArr.paymentEntitlementName = value.paymentEntitlementDesc;
																		  			  newArr.companyName = value.companyDesc;
																		  			  newArr.tariffName = value.tariffDesc;
																		  			  newArr.tariffId = value.tariffId;
																		  		}
																		  		
																		  		if(value.priorityId == 1){
																		  			$scope.appointmentMasterObj.paymentEntitlementTypeId = value.paymentEntitlementId;
																		  		}
																		  		$scope.paymentEntitleMentList.push(newArr);
																		  		
																		  	
																		  		var prioObj = {
																						  id:i,
																						  value:i,
																					  }
																					  $scope.priorityList.push(prioObj);
																		  		i++;
																			});
															}
														}

													});
												}
							    
									
							}else if($scope.admissionObj.fromNewRegistration==1 && $scope.admissionObj.patientId != undefined){
								var patiObjSearch = {};
								patiObjSearch.patientId = $scope.admissionObj.patientId
								patiObjSearch.organizationId = $scope.organizationId;
								patiObjSearch.patientName = $scope.admissionObj.patientName;
								$scope.searchPatient(patiObjSearch);
								$scope.isDirectAdmission='N';
								//$scope.patientDetails = {};
								$scope.initReferralObj();
								$scope.appointmentMasterObj = {};
								
								$scope.fieldsdisabled = false;
								$scope.btnDisableEnable = false;
								$('#admission_check').prop('checked', false);
								$scope.admissionCheck=false;
							    $scope.admissionRequestOrAdmissionObj.patientId = $scope.admissionObj.patientId;
							    $scope.admissionRequestOrAdmissionObj.visitTypeId = 1;
							    
							}
							
							
							
							$scope.initKinDetails = function(){
								  /* Add Next Of Kin Details */
							    $scope.kinDetailsList = [];
							    $scope.kinDetails = {
										prefixId   		: '',
										kinName  		: '',
										relationId 		: '',
										mobileNo 		: '',
										phoneNo 		: '',
										expiry 			: '',
										identificationId: '',
										identificationNo: '',
										isGuarantor 	: 'N',
										address 		: '',
										countryId 		: $scope.unitCountryId!=undefined && $scope.unitCountryId !=null ? $scope.unitCountryId : '',
										stateId 		:  $scope.unitStateId!=undefined && $scope.unitStateId !=null ? $scope.unitStateId : '',
										districtId 		:  $scope.unitDistrictId!=undefined && $scope.unitDistrictId !=null ? $scope.unitDistrictId : '',
										cityId 			: $scope.unitCityId!=undefined && $scope.unitCityId !=null ? $scope.unitCityId : '',
										areaId 			: $scope.unitAreaId!=undefined && $scope.unitAreaId !=null ? $scope.unitAreaId : '',
										patientId       : '',
										unitId			: $scope.unitId,
										organizationId  : $scope.organizationId,
										createdBy		: $scope.userId,
										updatedBy		: $scope.userId,
										createdDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										isExpiry		:'N'
									};
						
								$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
							}
							$scope.initKinDetails();
						  
						    $scope.addNewKinDetails = function(){
						    	$scope.kinDetails.patientId = $scope.patientDetails.patientId;
						    	$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
						    };
						    $scope.removeKinDetails = function(indexToBeDeleted){
						    		
						    	$scope.kinDetailsListCopy = angular.copy($scope.kinDetailsList);
						    	$scope.kinDetailsList = [];
						    	$scope.kinDetailsListCopy.splice(indexToBeDeleted, 1);
						    	$scope.kinDetailsList = angular.copy($scope.kinDetailsListCopy);
						    };
						    
							$scope.setKinId = function(kinId){
								$scope.admissionRequestOrAdmissionObj.kinDetailsId = kinId;
							}
							
							
							$scope.getIdentificationTypeList = function(){
								var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
								BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										$scope.identificationTypeList = response.data.listObject;
								});
							}
							$scope.getIdentificationTypeList();						    //start paymententitlement
						    $scope.getDetailsEntitlement = function(type)
							{
								/*if(type == '2')
								{
									
								}*/
								if(type == '2')
									{
									
									var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:type};
								    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
								    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											 $scope.companyMasterInsuranceList = response.data.listObject;
										}
									});
									}
								if(type == '3')
									{
									     var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:type};
										    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
										    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success"){
													 $scope.companyMasterList = response.data.listObject;
													 console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
												}
											});
										  
									}
							}
						    
						    $scope.getContractByCompanyId = function(companyId)
							{
						    	$scope.showNameDetails=false;
						    	console.log("getContractByCompanyId ",companyId);
								/*if(companyId!=undefined && companyId!="" && companyId!=null && !isNaN(companyId)){
										$scope.corporateCompanyId=companyId;
										$scope.enableDisabledValidated=true;
										$scope.enableDisabledApply=false;
										$scope.showHidevalidate=true;
								}else{
									$scope.corporateCompanyId="";
									$scope.enableDisabledValidated=false;
									$scope.enableDisabledApply=true;
								}*/
								var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
								if(companyId!=undefined && companyId!="" && companyId!=null && !isNaN(companyId)){
									$scope.corporateCompanyId=companyId;
									if($scope.corEmpVerificationFlag){
										$scope.enableDisabledValidated=true;
										$scope.enableDisabledApply=false;
									}else{
										$scope.enableDisabledValidated=false;
										$scope.enableDisabledApply=true;
									}
								}else{
									$scope.corporateCompanyId="";
									$scope.enableDisabledValidated=false;
								}
								$rootScope.startLoader();
								var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:2};
								if($scope.admissionRequestOrAdmissionObj.isDayCare == 'Y')
									{
									  param.visitTypeId = 3;
									}
								 $scope.contractList = [];
								 $scope.paymentEntitleMentCorporate[0].contractId = '';
								  $scope.paymentEntitleMentCorporate[0].datepickerCons ='';
								  $scope.paymentEntitleMentCorporate[0].datepickerConE ='';
								var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
							    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										 $scope.contractList = response.data.listObject;
										 //getting insurance document name start
										 if(companyId!=null && companyId!="" && companyId!=undefined){
												var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterListById/unit/"+$scope.unitId+"/org/"+$scope.organizationId+"/company/"+companyId;
												GenericService.serviceAction("GET", URI, "")
														.then(function(response) {
																	if (response.data.status == "success") {
																		$scope.CompanyObjDoc=response.data.listObject[0];											
																	} else {
																		alert("Error!!");
																	}
																});
											 }
										//getting insurance document name End
									}$rootScope.stopLoader();
								});
							}
							
							$scope.getInsuranceContract = function(insuranceItem)
							{
									var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
									var companyId = insuranceItem.companyId;
									var asscoiatedCompanyId = insuranceItem.associateCompanyId;
									var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,associateCompanyId:asscoiatedCompanyId,visitTypeId:2};
									if($scope.admissionRequestOrAdmissionObj.isDayCare == 'Y')
									{
									  param.visitTypeId = 3;
									}
									$scope.assoContractList = [];
									$scope.paymentEntitleMentInsurance[0].contractId = '';
								    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
								    $rootScope.startLoader();
								    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											 $scope.assoContractList = response.data.listObject;
											 //getting insurance document name start
											 if(asscoiatedCompanyId!=undefined && asscoiatedCompanyId!=null && asscoiatedCompanyId != ""){
												 var URI = BASE_URL + ROOT_URL+ "/api/unit/billing/getInsuranceMasterById/"+asscoiatedCompanyId+"/"+ $scope.organizationId+"/"+$scope.unitId;
													GenericService.serviceAction("GET", URI, "").then(function(response) {
														if (response.data.status == "success") {
															$scope.insuranceObjDoc=response.data.listObject[0];													
														} else {
															alert("Error!!");
														}
													});
											 }
											//getting insurance document name End
										}$rootScope.stopLoader();
									});
							}
							//checking expire datepicker is show or not
							$scope.disableEnableExpiry = function(isExpiryVal,idx)
							{
								 var splittedStringArray = isExpiryVal.split("_");
								if(splittedStringArray[1] == 'Y')
								{
									$scope.kinDetailsList[idx].isExpiry='Y';
//									$scope.kinDetails.expiry =new Date(moment(new Date()).format('MM/DD/YYYY'))
									//$scope.expiryDateDisabled = false;
								}
								else
									{
									 //$scope.expiryDateDisabled = true;
									
									$scope.kinDetailsList[idx].isExpiry='N';
									}
							}
							
							$scope.getContractDetails = function(item,idx,contId)
							{
								//alert(idx);
								var contractObj = null;
								contractObj = $.grep($scope.contractList, function (doc) {
						            return doc.contractId == contId;
						        })[0];
								console.log("contractObj",contractObj);
								if(contractObj != undefined || contractObj != null)
									{
									 //alert("Some Values");
									  $scope.paymentEntitleMentCorporate[0].datepickerCons =contractObj.validityFrom;
									  $scope.paymentEntitleMentCorporate[0].datepickerConE = contractObj.validityTo;
									
									  
									  
									}
								
								
							}
							
							/*$scope.getContractDetails = function(id,idx)
							{
								//alert(idx);
								var contractObj = null;
								contractObj = $.grep($scope.contractList, function (doc) {
						            return doc.contractId == id;
						        })[0];
								var insuranceObj = null;
								if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
									{
										contractObj = $.grep($scope.contractList, function (doc) {
								            return doc.contractId == id;
								        })[0];
									}
								if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
								{
									insuranceObj = $.grep($scope.assoContractList, function (doc) {
							            return doc.contractId == id;
							        })[0];
								}
								
								console.log("contractObj",contractObj);
								if(contractObj != undefined || contractObj != null)
									{
									 //alert("Some Values");
									  $scope.paymentEntitleMentCorporate[0].statrDate = contractObj.validityFrom;
									  $scope.paymentEntitleMentCorporate[0].endDate = contractObj.validityTo;
									  var payEntList = angular.copy($scope.paymentEntitleMentList);
									  $scope.paymentEntitleMentList = [];
									  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
									  {
									  var newArr = {};
									  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
									  newArr.paymentEntitlementId="3";
									  newArr.companyName = contractObj.assCompanyName;
									  newArr.companyId = contractObj.companyId;
									  newArr.tariffName = contractObj.tariffName;
									  newArr.contractName = contractObj.contractName;
									  newArr.insuranceName = "";
									  newArr.insuranceNumber = "";
									  newArr.startDate = contractObj.validityFrom;
									  newArr.endDate = contractObj.validityTo;
									  newArr.priority = 1;
									  newArr.tariffId = contractObj.tariffId;
									  newArr.contractId = contractObj.contractId;
									  
									  $scope.paymentEntitleMentList.push(newArr);
									  
									  }
									  angular.forEach(payEntList, function (value, key) {
										 
										  if(value.paymentEntitlementId == "1")
											  {
											  var newArr = {};
											  newArr.paymentEntitlementName=value.paymentEntitlementName;
											  newArr.paymentEntitlementId="1";
											  newArr.companyName = value.companyName;
											  newArr.companyId = value.companyId;
											  newArr.tariffName = value.tariffName;
											  newArr.contractName = value.contractName;
											  newArr.insuranceName = "";
											  newArr.insuranceNumber = "";
											  newArr.startDate = value.startDate;
											  newArr.endDate = value.endDate;
											  newArr.priority = 2;
											  newArr.tariffId = value.tariffId;
											  newArr.contractId = value.contractId;
											  $scope.paymentEntitleMentList.push(newArr);
											  }
										  
										  
									  });
									  
									}
								
								
							}*/
							
						/*	$scope.applyContractDetails = function(id)
							{
								//alert(idx);
								var contractObj = null;
								contractObj = $.grep($scope.contractList, function (doc) {
						            return doc.contractId == $scope.paymentEntitleMentCorporate[0].contractId;
						        })[0];
								
								var tarrifName = $.grep($scope.tariffList, function (doc) {
						            return doc.tariffId == $scope.paymentEntitleMentCorporate[0].tariffId;
						        })[0].tariffDesc;
								
								var insuranceObj = null;
								if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
									{
										contractObj = $.grep($scope.contractList, function (doc) {
								            return doc.contractId == id;
								        })[0];
									}
								if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
								{
									insuranceObj = $.grep($scope.assoContractList, function (doc) {
							            return doc.contractId == id;
							        })[0];
								}
								
								console.log("contractObj",contractObj);
								if(contractObj != undefined || contractObj != null)
									{
									 //alert("Some Values");
									  //$scope.paymentEntitleMentCorporate[0].statrDate = contractObj.validityFrom;
									  //$scope.paymentEntitleMentCorporate[0].endDate = contractObj.validityTo;
									  var payEntList = angular.copy($scope.paymentEntitleMentList);
									  $scope.paymentEntitleMentList = [];
									  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
									  {
									  var newArr = {};
									  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
									  newArr.paymentEntitlementId="3";
									  newArr.companyName = contractObj.assCompanyName;
									  newArr.companyId = contractObj.companyId;
									  newArr.tariffName = tarrifName;
									  newArr.contractName = contractObj.contractName;
									  newArr.insuranceName = "";
									  newArr.insuranceNumber = "";
									  newArr.startDate = moment($scope.paymentEntitleMentCorporate[0].datepickerCons).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
									  newArr.endDate = moment($scope.paymentEntitleMentCorporate[0].datepickerConE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityTo;datepickerConE
									  newArr.priority = 1;
									  newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
									  newArr.contractId = contractObj.contractId;
									  newArr.insuredAmount = '';
									  newArr.typeId = 3;
									  
									  $scope.paymentEntitleMentList.push(newArr);
									  
									  }
									  angular.forEach(payEntList, function (value, key) {
										 
										  if(value.paymentEntitlementId == "1")
											  {
											  var newArr = {};
											  newArr.paymentEntitlementName=value.paymentEntitlementName;
											  newArr.paymentEntitlementId="1";
											  newArr.companyName = value.companyName;
											  newArr.companyId = value.companyId;
											  newArr.tariffName = value.tariffName;
											  newArr.contractName = value.contractName;
											  newArr.insuranceName = "";
											  newArr.insuranceNumber = "";
											  newArr.startDate = value.startDate;
											  newArr.endDate = value.endDate;
											  newArr.priority = 2;
											  newArr.tariffId = value.tariffId;
											  newArr.contractId = value.contractId;
											  newArr.typeId = 1;
											  newArr.insuredAmount = '';
											  $scope.paymentEntitleMentList.push(newArr);
											  }
										  
										  
									  });
									  
									}
								
								
							}
							
							$scope.applyInsuranceDetails = function()
							{
								var insuranceObj = null;
								insuranceObj = $.grep($scope.assoContractList, function (doc) {
						            return doc.contractId == $scope.paymentEntitleMentInsurance[0].contractId;
						        })[0];
								var tarrifName = $.grep($scope.tariffList, function (doc) {
						            return doc.tariffId == $scope.paymentEntitleMentInsurance[0].tariffId;
						        })[0].tariffDesc;
								
								var associatedName = $.grep($scope., function (doc) {
						            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
						        })[0].name;
								
								console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
							   //console.log('$scope.datepickerInss',$scope.datepickerInss);
							   //console.log('$scope.datepickerInsE',$scope.datepickerInsE);
								
								if(insuranceObj != undefined || insuranceObj != null)
									{
									 //alert("Some Values");
									 //$scope.paymentEntitleMentCorporate[0].statrDate = contractObj.validityFrom;
									  //$scope.paymentEntitleMentCorporate[0].endDate = contractObj.validityTo;
									  var payEntList = angular.copy($scope.paymentEntitleMentList);
									  $scope.paymentEntitleMentList = [];
									  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
									  {
									  var newArr = {};
									  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
									  newArr.paymentEntitlementId="2";
									  newArr.companyName = insuranceObj.assCompanyName;
									  newArr.companyId = insuranceObj.companyId;
									  newArr.tariffName = tarrifName;
									  newArr.contractName = insuranceObj.contractName;
									  newArr.insuranceName = associatedName;
									  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
									  newArr.startDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
									  newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;datepickerInsE
									  newArr.priority = 1;
									  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
									  newArr.contractId = insuranceObj.contractId;
									  newArr.typeId = 2;
									  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
									  
									  $scope.paymentEntitleMentList.push(newArr);
									  
									  }
									  angular.forEach(payEntList, function (value, key) {
										 
										  if(value.paymentEntitlementId == "1")
											  {
											  var newArr = {};
											  newArr.paymentEntitlementName=value.paymentEntitlementName;
											  newArr.paymentEntitlementId="1";
											  newArr.companyName = value.companyName;
											  newArr.companyId = value.companyId;
											  newArr.tariffName = value.tariffName;
											  newArr.contractName = value.contractName;
											  newArr.insuranceName = "";
											  newArr.insuranceNumber = "";
											  newArr.startDate = value.startDate;
											  newArr.endDate = value.endDate;
											  newArr.priority = 2;
											  newArr.tariffId = value.tariffId;
											  newArr.contractId = value.contractId;
											  newArr.typeId = 1;
											  newArr.insuredAmount = '';
											  $scope.paymentEntitleMentList.push(newArr);
											  }
										  
										  
									  });
									  
									}
							}*/
							
							$scope.checkDuplicatePriority = function(item,idx,priorityVal)
							{
								
								var newDataList = [];
								var le = $scope.paymentEntitleMentList.length;
								var i = 0;
								angular.forEach($scope.paymentEntitleMentList, function(v) {
									if (idx != i) {
										/*if(v.priority > 1){
										v.priority = v.priority - 1;
										}*/
										newDataList.push(v);
									}
									i++;
								});
								
								//$scope.paymentEntitleMentList = newDataList;
								
								var found = newDataList.find(ar=>ar.priority==priorityVal);
								  if(found == undefined)
									  {
									  	//return false;
									  }
								  else
									  {
									  $scope.paymentEntitleMentList[idx].priority = 0;
									  growl.warning("You are aleady assign "+priorityVal+" priority",{
											title : "WARNING"
										});
										return;
									  }
							}
							
							
							 var isContractAlreadyExists;
								function checkContractAlreadyExistsOrNot(arr, val) {
									/*return arr
											.some(function(arr) {
												if(parseInt(arr.paymentEntitlementId) == val)
													{
														return true;
													}
												else
													{
														return false;
													}
											});*/
									  var found = arr.find(ar=>ar.paymentEntitlementId==val);
									  if(found == undefined)
										  {
										  	return false;
										  }
									  else
										  {
										  	return true;
										  }
									  
								}
								
							$scope.applyContractDetails = function(id)
							{
								//alert(idx);
								
								 var isEmpty = !(Object.values($scope.paymentEntitleMentCorporate[0]).every(x =>(x != null &&  x != '')));
								 if(isEmpty)
									 {
									 	
									 if($scope.paymentEntitleMentCorporate[0].companyId == null || $scope.paymentEntitleMentCorporate[0].companyId == '' || $scope.paymentEntitleMentCorporate[0].companyId == undefined){
										 growl.warning("Please select company.",{
												title : "WARNING"
											});
											return; 
									 }
									 									 
									 if($scope.paymentEntitleMentCorporate[0].contractId == null || $scope.paymentEntitleMentCorporate[0].contractId == '' || $scope.paymentEntitleMentCorporate[0].contractId == undefined){
										 growl.warning("Please select contract.",{
												title : "WARNING"
											});
											return; 
									 }
									 
									 
									 
									   /*growl.warning("Please select all fields.",{
											title : "WARNING"
										});
										return;*/
									 }
								 else{
							
								isContractAlreadyExists=checkContractAlreadyExistsOrNot($scope.paymentEntitleMentList,3);
									  //var found = $scope.array.find(ar=>ar==char);
								if (isContractAlreadyExists) {
									growl.warning("Can't add two contract.",{
										title : "WARNING"
									});
									return;
								}
								else
									{
								var contractObj = null;
								contractObj = $.grep($scope.contractList, function (doc) {
						            return doc.contractId == $scope.paymentEntitleMentCorporate[0].contractId;
						        })[0];
								
						/*		var tarrifName = $.grep($scope.tariffList, function (doc) {
						            return doc.tariffId == $scope.paymentEntitleMentCorporate[0].tariffId;
						        })[0].tariffDesc;*/
								
								
								console.log("contractObj",contractObj);
								if(contractObj != undefined || contractObj != null)
								{
									 $scope.getBedPopUpFlag = false;
								 // var payEntList = angular.copy($scope.paymentEntitleMentList);
								  //$scope.paymentEntitleMentList = [];
								  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
								  {
									  
									  var prioObj = {
											  id:$scope.priorityList.length + 1,
											  value:$scope.priorityList.length + 1,
										  }
										  $scope.priorityList.push(prioObj);
									  
								  var newArr = {};
								  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
								  newArr.paymentEntitlementId="3";
								  newArr.companyName = contractObj.assCompanyName;
								  newArr.companyId = contractObj.companyId;
								  //newArr.tariffName = tarrifName;
								  newArr.contractName = contractObj.contractName;
								  if($scope.employeeNumberManual != null || $scope.employeeNumberManual != undefined || $scope.employeeNumberManual != '')
									  newArr.employeeNoManual = $scope.employeeNumberManual;
								  
								  if($scope.employeeName != null && $scope.employeeName != undefined && $scope.employeeName != ''){
									  newArr.employeeName = $scope.employeeName;
								  }
								  
								  if($scope.relation != null && $scope.relation != undefined && $scope.relation != ''){
									  newArr.relation = $scope.relation;
								  }
								  
								  if($scope.medicalCardNo != null && $scope.medicalCardNo != undefined && $scope.medicalCardNo != ''){
										newArr.medicalCardNo = $scope.medicalCardNo;
								  }
								  
								  newArr.insuranceName = "";
								  newArr.insuranceNumber = "";
								  //newArr.startDate = moment($scope.paymentEntitleMentCorporate[0].datepickerCons).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
								  //newArr.endDate = moment($scope.paymentEntitleMentCorporate[0].datepickerConE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityTo;datepickerConE
								  newArr.startDate = $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+" 00:00:00";
								  newArr.endDate =$rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+" 00:00:00";
								  newArr.priority = "";
								 // newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
								  newArr.contractId = contractObj.contractId;
								  newArr.insuredAmount = '';
								  newArr.typeId = 3;
								  newArr.billingBedCategoryId = contractObj.billingBedCategoryId;
								  newArr.claimNumber = $scope.paymentEntitleMentCorporate[0].claimNumber;
								  if($scope.CompanyObjDoc.companyId!=undefined && $scope.CompanyObjDoc.companyId!=null && $scope.CompanyObjDoc.companyId!="" && $scope.paymentEntitleMentCorporate[0].companyId==$scope.CompanyObjDoc.companyId){
									  newArr.reportPath=$scope.CompanyObjDoc.reportPath;
								  }

								  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
									if (!isInsuranceExists) {
										$scope.enableDisabledApply=false;
										var lenCon = $scope.paymentEntitleMentList.length;
										if($scope.paymentEntitleMentList.length > 0)
										{
											angular.forEach($scope.paymentEntitleMentList, function (value, key) {
												 
												  if(value.paymentEntitlementId == "1")
													  {
													   value.priority = lenCon + 1;
													  }
												  
												  if(lenCon == 1)
												  {
												  	newArr.priority = "1";
												  }
											  if(lenCon >= 2)
												  {
												  angular.forEach($scope.paymentEntitleMentList, function (value1, key1) {
													  if(value1.paymentEntitlementId != 1)
													  {
													  value1.priority =  "";
													  }
												  });
												  	
												  }
												  
												  
											  });
										}
										
										$scope.paymentEntitleMentList.push(newArr);
										$scope.paymentEntitleMentCorporate=
											[{
												'companyId':"",
												'contractId':"",
												//'statrDate':"",
												//'endDate':""
											}];
										$scope.employeeNumberManual= '';
										$scope.employeeName= '';
										$scope.relation= '';
										$scope.medicalCardNo= '';
										
									}
									else
										{
											growl.warning("Already added in the list",{
												title : "WARNING"
											});
											return;
										}
								  
								  
								  
								  
								  }
								 /* angular.forEach(payEntList, function (value, key) {
									 
									  if(value.paymentEntitlementId == "1")
										  {
										  var newArr = {};
										  newArr.paymentEntitlementName=value.paymentEntitlementName;
										  newArr.paymentEntitlementId="1";
										  newArr.companyName = value.companyName;
										  newArr.companyId = value.companyId;
										  newArr.tariffName = value.tariffName;
										  newArr.contractName = value.contractName;
										  newArr.insuranceName = "";
										  newArr.insuranceNumber = "";
										  newArr.startDate = value.startDate;
										  newArr.endDate = value.endDate;
										  newArr.priority = 2;
										  newArr.tariffId = value.tariffId;
										  newArr.contractId = value.contractId;
										  newArr.typeId = 1;
										  newArr.insuredAmount = '';
										  $scope.paymentEntitleMentList.push(newArr);
										  }savePatientAdmission
									  
									  
								  });*/
								  
										}
									}
								 }		
								/*if(contractObj != undefined || contractObj != null)
									{
									  var payEntList = angular.copy($scope.paymentEntitleMentList);
									  $scope.paymentEntitleMentList = [];
									  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
									  {
									  var newArr = {};
									  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
									  newArr.paymentEntitlementId="3";
									  newArr.companyName = contractObj.assCompanyName;
									  newArr.companyId = contractObj.companyId;
									  newArr.tariffName = tarrifName;
									  newArr.contractName = contractObj.contractName;
									  newArr.insuranceName = "";
									  newArr.insuranceNumber = "";
									  newArr.startDate = moment($scope.paymentEntitleMentCorporate[0].datepickerCons).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
									  newArr.endDate = moment($scope.paymentEntitleMentCorporate[0].datepickerConE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityTo;datepickerConE
									  newArr.priority = 1;
									  newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
									  newArr.contractId = contractObj.contractId;
									  newArr.insuredAmount = '';
									  newArr.typeId = 3;
									  
									  $scope.paymentEntitleMentList.push(newArr);
									  
									  }
									  angular.forEach(payEntList, function (value, key) {
										 
										  if(value.paymentEntitlementId == "1")
											  {
											  var newArr = {};
											  newArr.paymentEntitlementName=value.paymentEntitlementName;
											  newArr.paymentEntitlementId="1";
											  newArr.companyName = value.companyName;
											  newArr.companyId = value.companyId;
											  newArr.tariffName = value.tariffName;
											  newArr.contractName = value.contractName;
											  newArr.insuranceName = "";
											  newArr.insuranceNumber = "";
											  newArr.startDate = value.startDate;
											  newArr.endDate = value.endDate;
											  newArr.priority = 2;
											  newArr.tariffId = value.tariffId;
											  newArr.contractId = value.contractId;
											  newArr.typeId = 1;
											  newArr.insuredAmount = '';
											  $scope.paymentEntitleMentList.push(newArr);
											  }
										  
										  
									  });
									  
									}*/
								
								
							}
							
							 var isInsuranceExists;
								function checkInsuranceExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												if(val.paymentEntitlementId == parseInt(arr.paymentEntitlementId) && val.contractId == parseInt(arr.contractId))
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
								 var insuranceCount;
									function checkInsuranceCount(arr) {
										var countInsurance = 0;
										angular.forEach($scope.paymentEntitleMentList, function (value, key) {
											 
											  if(value.paymentEntitlementId == "2")
												  {
												   countInsurance++;
												  }
											  
											  
										  });
										return countInsurance;
									}
									
							
							$scope.applyInsuranceDetails = function()
							{
								
									 //var isEmpty = !(Object.values($scope.paymentEntitleMentInsurance[0]).every(x =>(x != null &&  x != '')));
								var objTmp = angular.copy($scope.paymentEntitleMentInsurance[0]);
								 objTmp.multidelete("insuranceNumber","datepickerInss","datepickerInsE","insuredAmt");
									 var isEmpty = !(Object.values(objTmp).every(x =>(x != null &&  x != '')));
									 if(isEmpty)
										 {
										 if($scope.paymentEntitleMentInsurance[0].companyId == null || $scope.paymentEntitleMentInsurance[0].companyId == '' || $scope.paymentEntitleMentInsurance[0].companyId == undefined){
											 growl.warning("Please select company.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].associateCompanyId == null || $scope.paymentEntitleMentInsurance[0].associateCompanyId == '' || $scope.paymentEntitleMentInsurance[0].associateCompanyId == undefined){
											 growl.warning("Please select associate company.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].contractId == null || $scope.paymentEntitleMentInsurance[0].contractId == '' || $scope.paymentEntitleMentInsurance[0].contractId == undefined){
											 growl.warning("Please select contract.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 
										 if($scope.paymentEntitleMentInsurance[0].insuranceNumber == null || $scope.paymentEntitleMentInsurance[0].insuranceNumber == '' || $scope.paymentEntitleMentInsurance[0].insuranceNumber == undefined){
											 growl.warning("Please enter insurance number.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].datepickerInss == null || $scope.paymentEntitleMentInsurance[0].datepickerInss == '' || $scope.paymentEntitleMentInsurance[0].datepickerInss == undefined){
											 growl.warning("Please select start date.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].datepickerInsE == null || $scope.paymentEntitleMentInsurance[0].datepickerInsE == '' || $scope.paymentEntitleMentInsurance[0].datepickerInsE == undefined){
											 growl.warning("Please select end date.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].insuredAmt == null || $scope.paymentEntitleMentInsurance[0].insuredAmt == '' || $scope.paymentEntitleMentInsurance[0].insuredAmt == undefined){
											 growl.warning("Please enter amount.",{
													title : "WARNING"
												});
												return; 
										 }
										   /*growl.warning("Please select all fields.",{
												title : "WARNING"
											});
											return;*/
										 }
									 else{
										 
								insuranceCount=checkInsuranceCount($scope.paymentEntitleMentList)
								if (insuranceCount >= 2) {
									growl.warning("You can't add greater than two insurance.",{
										title : "WARNING"
									});
									return;
								}
								else
									{
										
								var insuranceObj = null;
								insuranceObj = $.grep($scope.assoContractList, function (doc) {
						            return doc.contractId == $scope.paymentEntitleMentInsurance[0].contractId;
						        })[0];
							/*	var tarrifName = $.grep($scope.tariffList, function (doc) {
						            return doc.tariffId == $scope.paymentEntitleMentInsurance[0].tariffId;
						        })[0].tariffDesc;*/
								
								var associatedName = $.grep($scope.associateCompanyMasterList, function (doc) 
									{
						            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
						        })[0].name;
								
								var insuranceId = $.grep($scope.associateCompanyMasterList, function (doc) 
										{
							            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
							        })[0].id;
								
								console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
								
								if(insuranceObj != undefined || insuranceObj != null)
								{
									 $scope.getBedPopUpFlag = false;
								  //var payEntList = angular.copy($scope.paymentEntitleMentList);
								 // $scope.paymentEntitleMentList = [];
								  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
								  {
									  var isAfter = true;
									  /*var sInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');
									  var eInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');
									  
									  var isAfter = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).isAfter($scope.paymentEntitleMentInsurance[0].datepickerInss);*/
									   /* alert(isAfter);
									    //$('.result2').text('Date is After: ' + isAfter);
									    return false;*/
									    
									  if(!isAfter)
										  {
											  growl.warning("Start date should be less then end date..!!",{
													title : "WARNING"
												});
												return false;
										  }else{
											  
											  var prioObj = {
													  id:$scope.priorityList.length + 1,
													  value:$scope.priorityList.length + 1,
												  }
												  $scope.priorityList.push(prioObj);
								  var newArr = {};
								  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
								  newArr.paymentEntitlementId="2";
								  newArr.companyName = insuranceObj.assCompanyName;
								  newArr.companyId = insuranceObj.companyId;
								 // newArr.tariffName = tarrifName;
								  newArr.contractName = insuranceObj.contractName;
								  newArr.insuranceName = associatedName;
								  newArr.insuranceId = insuranceId;
								  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
								  newArr.employeeNoManual = '';
								 // newArr.startDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
								  newArr.startDate = $scope.paymentEntitleMentInsurance[0].datepickerInss != null ?moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY'): null;//contractObj.validityFrom;
                                  newArr.endDate = $scope.paymentEntitleMentInsurance[0].datepickerInsE != null ? moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY'): null;//contractObj.validityFrom;datepickerInsE/ newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;datepickerInsE								  newArr.priority = "";
								  newArr.priority = "";
								 // newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
								  newArr.contractId = insuranceObj.contractId;
								  newArr.typeId = 2;
								  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
								  newArr.billingBedCategoryId = insuranceObj.billingBedCategoryId;
								  newArr.claimNumber = $scope.paymentEntitleMentInsurance[0].claimNumber;
								  if($scope.insuranceObjDoc.insuranceId!=undefined && $scope.insuranceObjDoc.insuranceId!=null  && $scope.paymentEntitleMentInsurance[0].associateCompanyId==$scope.insuranceObjDoc.insuranceId){
									  newArr.reportPath=$scope.insuranceObjDoc.reportPath;
								  }
								  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
									if (!isInsuranceExists) {
										var lenIns = $scope.paymentEntitleMentList.length;
										
										if($scope.paymentEntitleMentList.length > 0)
										{
											angular.forEach($scope.paymentEntitleMentList, function (value, key) {
												 
												  if(value.paymentEntitlementId == "1")
													  {
													   value.priority = lenIns + 1;
													  }
												  if(lenIns == 1)
													  {
													  	newArr.priority = "1";
													  }
												  if(lenIns >= 2)
													  {
													  angular.forEach($scope.paymentEntitleMentList, function (value1, key1) {
														  if(value1.paymentEntitlementId != 1)
														  {
														  value1.priority =  "";
														  }
													  });
													  	
													  }
												  
												  
											  });
										}
										
										$scope.paymentEntitleMentList.push(newArr);
										$scope.paymentEntitleMentInsurance=
											[{
												'companyId':"",
												'associateCompanyId':"",
												'contractId':"",
												//'statrDate':"",
												//'endDate':"",
												'insuranceNumber':"",
												'insuredAmt':""
											}];
									}
									else
										{
											growl.warning("Already added in the list",{
												title : "WARNING"
											});
											return;
										}
								  
								  }
								  
								  
								  }
								  /*angular.forEach(payEntList, function (value, key) {
									 
									  if(value.paymentEntitlementId == "1")
										  {
										  var newArr = {};
										  newArr.paymentEntitlementName=value.paymentEntitlementName;
										  newArr.paymentEntitlementId="1";
										  newArr.companyName = value.companyName;
										  newArr.companyId = value.companyId;
										  newArr.tariffName = value.tariffName;
										  newArr.contractName = value.contractName;
										  newArr.insuranceName = "";
										  newArr.insuranceNumber = "";
										  newArr.startDate = value.startDate;
										  newArr.endDate = value.endDate;
										  newArr.priority = 2;
										  newArr.tariffId = value.tariffId;
										  newArr.contractId = value.contractId;
										  newArr.typeId = 1;
										  newArr.insuredAmount = '';
										  $scope.paymentEntitleMentList.push(newArr);
										  }
									  
									  
								  });*/
								  
										}
									}
							}
								/*if(insuranceObj != undefined || insuranceObj != null)
									{
									  var payEntList = angular.copy($scope.paymentEntitleMentList);
									  $scope.paymentEntitleMentList = [];
									  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
									  {
									  var newArr = {};
									  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
									  newArr.paymentEntitlementId="2";
									  newArr.companyName = insuranceObj.assCompanyName;
									  newArr.companyId = insuranceObj.companyId;
									  newArr.tariffName = tarrifName;
									  newArr.contractName = insuranceObj.contractName;
									  newArr.insuranceName = associatedName;
									  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
									  newArr.startDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
									  newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;datepickerInsE
									  newArr.priority = 1;
									  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
									  newArr.contractId = insuranceObj.contractId;
									  newArr.typeId = 2;
									  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
									  
									  $scope.paymentEntitleMentList.push(newArr);
									  
									  }
									  angular.forEach(payEntList, function (value, key) {
										 
										  if(value.paymentEntitlementId == "1")
											  {
											  var newArr = {};
											  newArr.paymentEntitlementName=value.paymentEntitlementName;
											  newArr.paymentEntitlementId="1";
											  newArr.companyName = value.companyName;
											  newArr.companyId = value.companyId;
											  newArr.tariffName = value.tariffName;
											  newArr.contractName = value.contractName;
											  newArr.insuranceName = "";
											  newArr.insuranceNumber = "";
											  newArr.startDate = value.startDate;
											  newArr.endDate = value.endDate;
											  newArr.priority = 2;
											  newArr.tariffId = value.tariffId;
											  newArr.contractId = value.contractId;
											  newArr.typeId = 1;
											  newArr.insuredAmount = '';
											  $scope.paymentEntitleMentList.push(newArr);
											  }
										  
										  
									  });
									  
									}*/
							}
							
							$scope.addContracts = function()
							{
								var data = {
										'companyId':"",
										'contractId':"",
										//'statrDate':"",
										//'endDate':""
									};
								
								$scope.paymentEntitleMentCorporate.push(data);
								
							}
							
							$scope.removeContract = function(index)
							{
								console.log("removeIndex",index);
								var newDataList = [];
								var le = $scope.paymentEntitleMentList.length;
								var i = 1;
								if(le != i){
								angular.forEach($scope.paymentEntitleMentList, function(v) {
									if (index != i) {
										/*if(v.priority > 1){
										v.priority = v.priority - 1;
										}*/
										newDataList.push(v);
										
										if(le == 2)
										{
											if(v.paymentEntitlementId == "1")
												{
													v.priority = le - 1;
												}else
													{
													 v.priority = 1;
													}
										}
									if(le > 2)
									{
										
										if(v.priority != undefined && v.priority != 1 && v.priority !="")
										{
										 //v.priority = "";
										 v.priority = v.priority - 1;
										}
										
										/*if(v.paymentEntitlementId == "1")
											{
												v.priority = le - 1;
											}else
												{
												v.priority = "";
												}*/
									}
									
									}
									i++;
									if($scope.corEmpVerificationFlag){
										$scope.enableDisabledValidated=false;
										$scope.enableDisabledApply=true;	
									}else{
										$scope.enableDisabledValidated=false;
										$scope.enableDisabledApply=true;
									}
								});
								
								var len = $scope.priorityList.length;
								 $scope.priorityList.splice(len-1,1);
								 
								$scope.paymentEntitleMentList = newDataList;
								
								//$scope.paymentEntitleMentCorporate =[];
								//$scope.addContracts();
								// for removing document
								$scope.allDocumentFinalListTemp=[];
								$scope.allDocumentFinalListTemp=$scope.allDocumentFinalList;
									if($scope.allDocumentFinalListTemp!=undefined && $scope.allDocumentFinalListTemp.length>0){
										$scope.allDocumentFinalList=[];
										angular.forEach(newDataList, function(v) {											
										
												angular.forEach($scope.allDocumentFinalListTemp, function(v1) {												
													if(v.paymentEntitlementId==v1.paymentEntitlementId){
														$scope.allDocumentFinalList.push(v1);
													}
													
												});
											
											
										});
									}
									console.log("final list after removing",$scope.allDocumentFinalList);
								}
							}
							
							$scope.addInsurances = function()
							{
								var data = {
										'companyId':"",
										'associateCompanyId':"",
										'contractId':"",
										//'statrDate':"",
										//'endDate':"",
										'insuranceNumber':"",
										'insuredAmt':""
									};
								$scope.paymentEntitleMentInsurance.push(data);
							}
							
							$scope.getAssociatedCompanyMasterListByCompanyId = function(companyId){
								 var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
								 $scope.associateCompanyMasterList = [];
								 $scope.paymentEntitleMentInsurance[0].associateCompanyId = '';
								    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
								    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											 $scope.associateCompanyMasterList = response.data.listObject;
											  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
										}
									});
							}
							
							$scope.getCompanyMasterList = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + COMPANY_MASTER + UNIT + SLASH + $scope.unitId + ORG + SLASH + $scope.organizationId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.companyMasterList = [];
										$scope.associatedCompanyList = [];
										if (response.data.status == "success") {
											$scope.companyMasterList = response.data.listObject;
										}
									});
							}
							$scope.getCompanyMasterList();
						    //End paymententitlemwnt
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveBedAllocation();
							}

							// for get state by country ID
							$scope.getStateByCountry = function(id) {
								var data = {
									countryId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETSTATELISTBYCOUNTRYID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.stateList = [];

													if (response.data.status == "success") {
														$scope.stateList = response.data.listObject;
														console
																.log($scope.stateList);

													}
												});

							}

							$scope.getDistrictByState = function(id) {
								var data = {
									stateId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDISTRICTLISTBYSTATEID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.districtList = [];

													if (response.data.status == "success") {
														$scope.districtList = response.data.listObject;
														console
																.log($scope.districtList);

													}
												});

							}

							$scope.getCityByDistrict = function(id) {

								var data = {
									districtId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETCITYLISTBYDISTRICTID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.cityList = [];

													if (response.data.status == "success") {
														$scope.cityList = response.data.listObject;
														console
																.log($scope.cityList);

													}
												});

							}
							$scope.getAreaByCity = function(id) {

								var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETAREALISTBYCITY;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.areaList = [];

													if (response.data.status == "success") {
														$scope.areaList = response.data.listObject;
													}
												});

							}
							//added by dinesh start 10-09-19
							if($scope.unitCountryId > 0)
							{
							
							  $scope.getStateByCountry($scope.unitCountryId);
							  $scope.getDistrictByState($scope.unitStateId);
							  $scope.getCityByDistrict($scope.unitDistrictId);
							  $scope.getAreaByCity($scope.unitCityId);
							}
							 
							//added by dinesh end 10-09-19
							 
							$scope.getBedById = function(commonType) {

								console.log($scope.bedsearch);
                                 
								var commonData = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								switch (commonType) {
								case 'floor':
									
									//alert($scope.patientDetails.genderId);
									commonData.floorId = $scope.bedsearch.floorId;
									commonData.genderId = $scope.patientDetails.genderId;
									if($scope.patientDetails.genderId > 0)
										{
										
										if($scope.bedsearch.floorId != "" && $scope.bedsearch.floorId > 0){
									var URI = BASE_URL + ROOT_URL
											+ GETACTIVEWARDLISTBYFLOORID;
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
										}else{
											var data = {};
											
											var URI = BASE_URL + ROOT_URL
											+ "/api/adt/getWardForDirectTransfer/"+$scope.patientDetails.genderId+"/"+2+"/"+$scope.unitId;
											GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.wardList = [];

														if (response.data.status == "success") {
															$scope.wardList = response.data.listObject;

														}
													});
												
											
										}
										}
									else
										{
											growl.warning("Please Select Patient",{
												title : WARNING_MSG
											});
											return;
										}
									break;
								default:

								}

							}

							$scope.dayCareWardList = [];
							$scope.getWardListForDayCare = function(commonType) {
								var commonData = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

									var URI = BASE_URL + ROOT_URL
											+ GETACTIVEWARDLIST;
									GenericService
											.serviceAction("POST", URI,
													commonData)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.dayCareWardList=response.data.listObject;
														}
													});

							}
							$scope.getWardListForDayCare();
							
							
							$scope.setReferralObject = function()
							{
//									
								if($scope.referral.referralTypeId=="" || $scope.referral.referralTypeId==undefined){
									growl.warning("Referral Type is required",
											{
												title: 'Warning!'
											});
									return false;
								}else if($scope.referral.referralName=="" || $scope.referral.referralName== undefined){
									growl.warning("Referral Name is required",
											{
												title: 'Warning!'
											});
									return false;
								}/*else if($scope.referral.address=="" || $scope.referral.address==undefined){
									growl.warning("Address is required",
											{
												title: 'Warning!'
											});
									return false;
									
								}else if($scope.referral.email=="" || $scope.referral.email==undefined){
									
									growl.warning("Email is required",
											{
												title: 'Warning!'
											});
									return false;
								}else if($scope.referral.contact=="" || $scope.referral.contact==undefined){
									growl.warning("Contact Number is required",
											{
												title: 'Warning!'
											});
									return false;
								}*/
								
								/*if($scope.referral.contact!="" && $scope.referral.contact!=undefined){
									var mobileNo=$scope.referral.contact;
									if(mobileNo.length==parseInt($rootScope.registrationFieldObj.mobileDigit)){
					        			
									}else{
										growl.error("Please enter valid contact number",{
											title : ERROR_MSG
											});
											return false;
									}
								}
								
								
								if($scope.referral.email!=""){
									var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
					           		 var email = $scope.referral.email;
					           		    if (!filter.test(email)) {
					           		     $scope.tab=1;
				        				    growl.warning(EMAIL_MSG,{
												title : 'Warning!'
											});
				        				    $('#email').focus();
				        				    return false;
					           		    }
								}*/
								$('#addReferralModal').modal('hide');
									$scope.referralData = {
											"referralTypeId"      : $scope.referral.referralTypeId,
											"id"      : $scope.referral.referralTypeId,
											"desc"     : $scope.referral.referralName,
											"referralAddress"        : $scope.referral.address,
											"countryId"        : $scope.referral.countryId,
											"stateId"       : $scope.referral.stateId,
											"districtId":$scope.referral.districtId,
											"cityId"	  :$scope.referral.cityId,
											"areaId" : $scope.referral.areaId,
											"referralEmail" : $scope.referral.email,
											"referralContact" : $scope.referral.contact,
											"referralContactPerson": $scope.referral.contactPerson,
											//"unitId"          :$scope.unitId,
											"organizationId"  :$scope.organizationId,
											"createdBy"  :$scope.userId,
											"updatedBy"  :$scope.userId,
											"createdDate"  :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											"updatedDate"  :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											"status"  :'A',
									}
									
									
									$scope.referralData.referralTypeDesc = $.grep($scope.referralTypeList, function (disCat) {
							            return disCat.id == $scope.referral.referralTypeId;
							        })[0].desc;
									$scope.diableDropDown=true;
									$scope.enableText=false;
									$scope.admissionRequestOrAdmissionObj.referralDesc = $scope.referral.referralName;
									console.log("referralData",$scope.referralData);
//								}
							}
							
							//get OT ReservationList By Patient ID
							$scope.otReservationList = [];
							$scope.getOTReservationList = function(patientId){
								$rootScope.startLoader();
								$scope.otReservationList = [];
								 var URI = BASE_URL + ROOT_URL + "/api/otims/OTReservation/getOTReservationListByPatientId";
						         var param = {
						      		   patientId:patientId
						         };
							   BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										if(response.data.listObject.length>0){
											angular.forEach(response.data.listObject,function(value,index){
												value.isSelected = false;
												value.reasonId="";
												value.reasonNote="";
											});
											$scope.otReservationList = response.data.listObject;
										}
										
										$rootScope.stopLoader();
										}else if(response.data.status == "error"){
										$rootScope.stopLoader();
									}
									
								});
							}

							
							
//							$scope.loadingMsg="";
							$scope.bedSearchByMultipleCriteria = function() {

								// $scope.bedsearch.isVirtual =
								// ($scope.bedsearch.isVirtual == true) ? "Y":
								// "N";
								if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
									growl.warning(
											'Please select patient!!!.',
											{
												title : 'Warning!'
											});
										return;
								}else{
								$scope.loadingMsg="Loading...";
								$scope.activeBedList = [];
								$scope.bedsearch.isDayCare=$scope.admissionRequestOrAdmissionObj.isDayCare;
								console.log($scope.bedsearch);
								$scope.bedsearch.genderId = $scope.patientDetails.genderId;
								$scope.bedsearch.floorId= $scope.bedsearch.floorId=="" ? 0 : $scope.bedsearch.floorId;
								$scope.bedsearch.wardId= $scope.bedsearch.wardId=="" ? 0 : $scope.bedsearch.wardId;
								$scope.bedsearch.bedStatusId= ($scope.bedsearch.bedStatusId=="" || $scope.bedsearch.bedStatusId==0) ? 3 : $scope.bedsearch.bedStatusId;
 								 $scope.bedsearch.bedCategoryId= (isNaN($scope.bedsearch.bedCategoryId) || $scope.bedsearch.bedCategoryId==null) ? 0 : $scope.bedsearch.bedCategoryId;
								// return false;
								var URI = BASE_URL + ROOT_URL
										+ GETBEDLISTBYMULTIPLECRITERIA;
								GenericService
										.serviceAction("POST", URI,
												$scope.bedsearch)
										.then(
												function(response) {
													

													if (response.data.status == "success") {
														$scope.activeBedList = response.data.listObject;
														if($scope.activeBedList.length>0){
															$scope.loadingMsg="";
														}else{
															$scope.loadingMsg="No records found...!!";
														}

													}
												});
						        	}
							}
							

							$scope.getMLCPriorityList=function(){

								var URI = BASE_URL + ROOT_URL+ "/adt/getActivePriorityList";
								GenericService.serviceAction("GET", URI, {})
								.then(function(response) {
									$scope.priorityListForMLC = [];
									$scope.priorityListForMLC = response.data.listObject;
								});
							}

							$scope.getMLCPriorityList();

							
							$scope.updateAbscondPatient=function(patientId){
								// update abscond patient as normal patient start
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

							$scope.saveBedAllocationWithOTreservation = function(){
								
								 if($scope.otReservationList.length>0){
									 $("#otReservationDetails").modal("show"); 
								 }else{
								   $scope.treatingDoctor();
									 $scope.saveBedAllocation();
								 }
								
							}
					
							$scope.saveBedAllocation = function() {
								
//								$scope.$broadcast('show-errors-check-validity');
								
							var flag=false;
							 if($scope.admissionRequestOrAdmissionObj.patientId == '' || $scope.admissionRequestOrAdmissionObj.patientId == 0){
								//$scope.tab=1;
								growl.warning("Please Select Patient",{
									title : WARNING_MSG
								});
								return;
							}
							else if($scope.admissionRequestOrAdmissionObj.specialityId == '' || $scope.admissionRequestOrAdmissionObj.specialityId == 0){
									//$scope.tab=1;
									growl.warning("Please Select Admitted Doctor Specialty",{
										title : WARNING_MSG
									});
									return;
								}
								else if($scope.admissionRequestOrAdmissionObj.doctorId == '' || $scope.admissionRequestOrAdmissionObj.doctorId == 0){
									//$scope.tab=1;
									growl.warning("Please Select Admitted Doctor",{
										title : WARNING_MSG
									});
									return;
								}
								else if($scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId == '' || $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId == 0){
									//$scope.tab=1;
									growl.warning("Please Select Treating Doctor Specialty",{
										title : WARNING_MSG
									});
									return;
								}
								else if($scope.admissionRequestOrAdmissionObj.treatingDoctorId == '' || $scope.admissionRequestOrAdmissionObj.treatingDoctorId == 0){
									//$scope.tab=1;
									growl.warning("Please Select Treating Doctor",{
										title : WARNING_MSG
									});
									return;
								}
								else if($scope.admissionRequestOrAdmissionObj.admissionTypeId == '' || $scope.admissionRequestOrAdmissionObj.admissionTypeId == 0 || $scope.admissionRequestOrAdmissionObj.admissionTypeId == null)
								{
																	
									growl.warning("Please Select Admission Type",{
										title : WARNING_MSG
									});
									return;
								}
								else if($scope.admissionRequestOrAdmissionObj.reasonId == '' || $scope.admissionRequestOrAdmissionObj.reasonId == 0 || $scope.admissionRequestOrAdmissionObj.reasonId == null)
								{
																	
									growl.warning("Please Select Reason",{
										title : WARNING_MSG
									});
									return;
								}
								else if($scope.isBackDatedAdmission == 'Y' && ($scope.fromTimeDoa == '' || $scope.fromTimeDoa == null || $scope.fromTimeDoa == undefined)){
									growl.warning("Please add date of admission time.",{
										title : WARNING_MSG
									});
									return;
								}
								else if($scope.admissionRequestOrAdmissionObj.bedId == '' || $scope.admissionRequestOrAdmissionObj.bedId == 0 || $scope.admissionRequestOrAdmissionObj.bedId == null)
								{
																	
									growl.warning("Please Select Bed",{
										title : WARNING_MSG
									});
									return;
								}else if(($scope.admissionRequestOrAdmissionObj.referralTypeId !=null 
										   && $scope.admissionRequestOrAdmissionObj.referralTypeId !=""
											   && $scope.admissionRequestOrAdmissionObj.referralTypeId !=undefined)&&
											  ($scope.admissionRequestOrAdmissionObj.referralDesc == null 
												  || $scope.admissionRequestOrAdmissionObj.referralDesc == ""
												   || $scope.admissionRequestOrAdmissionObj.referralDesc == undefined)){
										
										   if($scope.admissionRequestOrAdmissionObj.referralDesc == null 
												   || $scope.admissionRequestOrAdmissionObj.referralDesc == ""
												   || $scope.admissionRequestOrAdmissionObj.referralDesc == undefined){
											   
												growl.warning("Please Select Referral By",{
													title : ERROR_MSG
												});
												return;
												}
											   
								 	}							
								else if($scope.admissionRequestOrAdmissionObj.kinDetailsId == "" && $scope.kinFlag == true){
									
									/*growl.warning("Please Add Kin",{
										title : WARNING_MSG
									});
									return;*/
									
//									if (!$scope.admissionForm.nextOfKinForm.$valid) {
//                                        $('#prefixId11').focus();
//										growl.warning("Please Add Kin",{
//											title : WARNING_MSG
//										});
//										return false;
//									}
									var kinDetailsListTemp = [];
									angular.forEach($scope.kinDetailsList, function(
											value, index) 
											{
										     var datakin = {
										    		 prefixId : value.prefixId,
										    		 kinName : value.kinName,
										    		 relationId:value.relationId,
										    		 mobileNo:value.mobileNo,
										    		 phoneNo:value.phoneNo,
										    		 address:value.address,
										    		 countryId :value.countryId,
										    		 stateId:value.stateId,
										    		 districtId:value.districtId,
										    		 cityId:value.cityId,
										    		 areaId:value.areaId,
										    		 postCode:value.postCode,
										    		 identificationId:value.identificationId,
										    		 identificationNo:value.identificationNo
										     }
										     kinDetailsListTemp.push(datakin);
									});
									var i=1;
									var kinFlag=false;
									 if($scope.kinDetailsList.length >0){
				        				 var i=1;
				        				 angular.forEach(kinDetailsListTemp,function(value,key){

				        					 if($scope.registrationFieldObj.kinprefix== "Y"){
													if(value.prefixId == undefined || value.prefixId == null || value.prefixId ==""){
														growl.warning("Please Add Prefix For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.kinName== "Y"){
													if(value.kinName == undefined || value.kinName == null || value.kinName ==""){
														growl.warning("Please Add Name For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												if($scope.registrationFieldObj.relationId== "Y"){
													if(value.relationId == undefined || value.relationId == null || value.relationId ==""){
														growl.warning("Please Add Relation For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												if ($scope.registrationFieldObj.kinMobileNo === "Y") {
												    angular.forEach($scope.kinDetailsList, function (value, index) {
												        var mobileNo = value.mobileNo;

												        if (mobileNo === undefined || mobileNo === null || mobileNo === "") {
												            growl.warning("Please Add Mobile No For Next Of KIN");
												            flagKin = false;
												            return false; 
												        } else if (mobileNo.length !== $scope.registrationFieldObj.mobileDigit) {
												            growl.warning("Mobile No For Next Of KIN should be " + $scope.registrationFieldObj.mobileDigit + " digits long");
												            flagKin = false;
												            return false; 
												        }
												    });
												}

												 if($scope.registrationFieldObj.phoneNo== "Y"){
													if(value.phoneNo == undefined || value.phoneNo == null || value.phoneNo ==""){
														growl.warning("Please Add Phone No For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.kinAddress== "Y"){
													if(value.address == undefined || value.address == null || value.address ==""){
														growl.warning("Please Add Address For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.countryId== "Y"){
													if(value.countryId == undefined || value.countryId == null || value.countryId ==""){
														growl.warning("Please Add Country For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.stateId== "Y"){
													if(value.stateId == undefined || value.stateId == null || value.stateId ==""){
														growl.warning("Please Add State For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.districtId== "Y"){
													if(value.districtId == undefined || value.districtId == null || value.districtId==""){
														growl.warning("Please Add District For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.cityId== "Y"){
													if(value.cityId == undefined || value.cityId == null || value.cityId ==""){
														growl.warning("Please Add City For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.areaId== "Y"){
													if(value.areaId == undefined || value.areaId == null || value.areaId ==""){
														growl.warning("Please Add Area For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.postCode== "Y"){
													if(value.postCode == undefined || value.postCode == null ||value.postCode ==""){
														growl.warning("Please Add Post Code For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.identificationId== "Y"){
													if(value.identificationId == undefined || value.identificationId == null ||value.identificationId ==""){
														growl.warning("Please Add Identification Type For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
												 if($scope.registrationFieldObj.identificationNo== "Y"){
													if(value.identificationNo == undefined || value.identificationNo == null || value.identificationNo ==""){
														growl.warning("Please Add Identification No For Next Of KIN",{
														});
														kinFlag=false;
														return false;
													}
												}
				        					 if((i==kinDetailsListTemp.length) && kinFlag==true)
												{
				        						 return false;
												}else{
													$scope.kinFlag = false;
												}
				        					 if(i==kinDetailsListTemp.length){
												 $scope.saveBedAllocation();
											}
				        					 i++;
				        				 });
				        			 }
									/*angular.forEach(kinDetailsListTemp, function(
											value, index) 
											{
										     var isEmpty = !(Object.values(value).some(x =>(x != null &&  x != '')));
										     if(kinDetailsListTemp.length >0)
											 {
										    	 if($scope.registrationFieldObj.relationId== "Y"){
														if(value.relationId == undefined || value.relationId == null || value.relationId ==""){
															growl.warning("Please Add Relation For Next Of KIN",{
															});
															return false;
														}
														
													}
													 if($scope.registrationFieldObj.kinMobileNo== "Y"){
														if(value.mobileNo == undefined || value.mobileNo == null || value.mobileNo ==""){
															growl.warning("Please Add Mobile No For Next Of KIN",{
															});
															return false;
														}
														
													}
											 }
										   else
										    	 {
										    	 	$scope.kinFlag = false;
										    	 	
										    	 }
										     if(t == kinDetailsListTemp.length){
										    	 $scope.saveBedAllocation();
										     }
										     t++;
									});*/
									
								}

								else{
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								
								var dateOfPdd = '';
								var dateOfAdmission = '';
								var admTime = moment(new Date()).format("HH:mm:ss");

								console.log($scope.kinDetailsList);
								angular.forEach($scope.kinDetailsList, function(
										value, index) {
//									console.log("value.expiry",value.expiry);
//									value.expiry = moment(value.expiry).format('DD-MM-YYYY HH:mm:ss');
//									value.expiry = null;
								  var splittedStringArray =value.identificationId.split("_");
								  
								  if((splittedStringArray[0] != null && splittedStringArray[0] > 0) && splittedStringArray[1] != null)
								  {
									  value.identificationId=splittedStringArray[0];
								   var identificationIdTemp=splittedStringArray[1];
								   if(identificationIdTemp=='' || identificationIdTemp==undefined){
									   splittedStringArray[1]='N';
								   }
								   if(splittedStringArray[1]=='N'){
									   value.expiry=null;
								   }else{
									   if(value.expiry==null || value.expiry==""){
										   flag=true;
									   }
									   value.expiry= moment(value.expiry).format('DD-MM-YYYY HH:mm:ss');
								   }
								}else{
									 value.expiry=null;
								}
									
								});
								if(flag){
									growl.error(
											'Please select expiry date',
											{
												title : 'Error!'
											});
									return;
								}
//								return false;
								
								if($scope.isBackDatedAdmission == 'Y'){
									admTime = $scope.fromTimeDoa + ':00';
								}
								if($scope.selectedPatient == 'N')
									{
									if($scope.admissionRequestOrAdmissionObj.isDayCare=='Y'){
										var dateOfAdm  =  moment($scope.datepicker).format('DD-MM-YYYY');
										var dischargeDate = moment($scope.datepickerPdd).format('DD-MM-YYYY');
										  dateOfPdd = dischargeDate+' '+admTime;
										  dateOfAdmission =dateOfAdm+' '+admTime;		
									}else{
									    var dischargeDate = $rootScope
										.getChangedFormatedDateShalseTwo($scope.admissionRequestOrAdmissionObj.pdd);
										var dateOfAdm = $rootScope
										.getChangedFormatedDateShalseTwo($scope.admissionRequestOrAdmissionObj.doa);
										dateOfPdd = dischargeDate+' '+admTime;
										  dateOfAdmission =dateOfAdm+' '+admTime;
									}
								}else{
									var dateOfAdm  =  moment($scope.datepicker).format('DD-MM-YYYY');
									var dischargeDate = moment($scope.datepickerPdd).format('DD-MM-YYYY');
									  dateOfPdd = dischargeDate+' '+admTime;
									  dateOfAdmission =dateOfAdm+' '+admTime;
									}
								
								
								$scope.finalOtReservationList = [];
								angular.forEach($scope.otReservationList ,function(value,index){
									if(value.isSelected){
										$scope.finalOtReservationList.push(value);
									}
								});
								
								if($scope.admissionRequestOrAdmissionObj.isMedicoLegal == 'Y'){
									
									if($scope.admissionRequestOrAdmissionObj.accompaniedName == '' || $scope.admissionRequestOrAdmissionObj.accompaniedName == null || $scope.admissionRequestOrAdmissionObj.accompaniedName == undefined){
										growl.warning(
												'Please enter Accompanied by...!!',
												{
													title : 'Warning!'
												});
										return false;
									}else if($scope.admissionRequestOrAdmissionObj.policeStationName == '' || $scope.admissionRequestOrAdmissionObj.policeStationName == null || $scope.admissionRequestOrAdmissionObj.policeStationName == undefined){
										growl.warning(
												'Please enter Police Station Name...!!',
												{
													title : 'Warning!'
												});
										return false;
									}else if($scope.admissionRequestOrAdmissionObj.policeBuckleNo == '' || $scope.admissionRequestOrAdmissionObj.policeBuckleNo == null || $scope.admissionRequestOrAdmissionObj.policeBuckleNo == undefined){
										growl.warning(
												'Please enter Police Buckle No...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								}
								if($scope.patobj.patientImage1!=undefined &&  !angular.equals({}, $scope.patobj.patientImage1)  && $scope.patobj.patientImage1!="")
								   {
									     var ext = $scope.patobj.patientImage1.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
												growl.warning('You can only upload *.jpg, *.jpeg, *.png',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
											}
								   }
								$rootScope.startLoader();
								var data = {
									admissionNoteId : $scope.admissionRequestOrAdmissionObj.admissionNoteId,
									floorId : $scope.admissionRequestOrAdmissionObj.floorId,
									wardId : $scope.admissionRequestOrAdmissionObj.wardId,
									roomId : $scope.admissionRequestOrAdmissionObj.roomId,
									bedCategoryId : $scope.admissionRequestOrAdmissionObj.bedCategoryId,
									billingBedCategoryId : $scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId,
									pdd : dateOfPdd,
									doa : dateOfAdmission,
									mealPrefId : $scope.admissionRequestOrAdmissionObj.mealPrefId,
									attendentMealPrefId : $scope.admissionRequestOrAdmissionObj.attendentMealPrefId,
									//new added
									reservedBedCategoryId : $scope.admissionRequestOrAdmissionObj.reservedBedCategoryId,
									bedsBillingBedCategoryId : $scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId,
									
									referralTypeId : $scope.admissionRequestOrAdmissionObj.referralTypeId,
									referralId : $scope.admissionRequestOrAdmissionObj.referralId,
									referralRemark :  $scope.admissionRequestOrAdmissionObj.refferalRemark,
									
									//end new added
									//For MLC
									accompaniedBy : $scope.admissionRequestOrAdmissionObj.accompaniedName,
									firNo        :$scope.admissionRequestOrAdmissionObj.firNo,
									policeBuckleNo :$scope.admissionRequestOrAdmissionObj.policeBuckleNo,
									policeStationName:$scope.admissionRequestOrAdmissionObj.policeStationName,
									mlcContactNo:$scope.admissionRequestOrAdmissionObj.mlcContactNo,
									mlcRelationId:$scope.admissionRequestOrAdmissionObj.mlcRelationId,
									mlcRelationId:($scope.admissionRequestOrAdmissionObj.mlcRelationId != "0" &&  $scope.admissionRequestOrAdmissionObj.mlcRelationId != null)?  $scope.admissionRequestOrAdmissionObj.mlcRelationId : null,
									priorityId : ($scope.admissionRequestOrAdmissionObj.priorityId != "0" &&  $scope.admissionRequestOrAdmissionObj.priorityId != null)?  $scope.admissionRequestOrAdmissionObj.priorityId : null,
									isMedicoLegal : $scope.admissionRequestOrAdmissionObj.isMedicoLegal,
									mlcRemark : $scope.admissionRequestOrAdmissionObj.mlcRemark,
									modeOfArrival : $scope.admissionRequestOrAdmissionObj.modeOfArrival,
									rremark : $scope.admissionRequestOrAdmissionObj.rremark,
									remark : $scope.admissionRequestOrAdmissionObj.rremark,
									//end MLC
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									patientId : $scope.admissionRequestOrAdmissionObj.patientId,
									tPatientId : $scope.admissionRequestOrAdmissionObj.tPatientId,
									specialityId : $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId != null ? $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
									//visitTypeId :2,
									visitTypeId :$scope.admissionRequestOrAdmissionObj.isDayCare=='Y' ? 3 : 2,
									bedId : $scope.admissionRequestOrAdmissionObj.bedId,
									//paymentEntitlementId : $scope.admissionNoteData.paymentEntitlementId,
									doctorId : $scope.admissionRequestOrAdmissionObj.treatingDoctorId != null ? $scope.admissionRequestOrAdmissionObj.treatingDoctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
									patientCategoryId : $scope.admissionRequestOrAdmissionObj.patientCategoryId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									kinArray : $scope.kinDetailsList,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									admissionTypeId: $scope.admissionRequestOrAdmissionObj.admissionTypeId, //For OP
									bedCategoryWaitingListId:$scope.admissionRequestOrAdmissionObj.bedCategoryWaitingListId,
									kinDetailsId : $scope.admissionRequestOrAdmissionObj.kinDetailsId,
									defaultSelfTariffId :$scope.defaultSelfTariffId,
									reqDto:$scope.depositReq,  //depositCollection
									admittingSpecialityId:$scope.admissionRequestOrAdmissionObj.specialityId,
									admittingDoctorId:$scope.admissionRequestOrAdmissionObj.doctorId,
									reasonId:$scope.admissionRequestOrAdmissionObj.reasonId,
									patientClassId:$scope.admissionRequestOrAdmissionObj.patientClassId,
									bedNumber :$scope.admissionRequestOrAdmissionObj.bedNumber,
									wardName : $scope.admissionRequestOrAdmissionObj.wardName,
									payeeId:$scope.selfPayeeId,
									otReservationList : $scope.finalOtReservationList,
									defaultDiscountOwnership : $scope.defaultDiscountOwnership,
									referenceNumber : $scope.referenceNumber
									
								};

								if($scope.dataListDiagnosis[0].diagnosisId!=null && $scope.dataListDiagnosis[0].diagnosisId!="" && $scope.dataListDiagnosis[0].diagnosisId!=undefined)  
								{
									//****** order set started******
									
									 data.diagnosisList =  $scope.dataListDiagnosis;
//									 console.log("diagnosis list",$scope.dataListDiagnosis);
									 var checkMedQty=false;
									if($scope.medicationDetails.length>0 && $scope.medicationDetails[0].itemId!=null && $scope.medicationDetails[0].itemId!="" && $scope.medicationDetails[0].itemId!=undefined)
									{
										$scope.diagnosisMedicationDetailList = [];
										var medicationDetails = $scope.medicationDetails;
			                           for (var i=0 ; i< medicationDetails.length; i++)
			                           {
											var medicationStartDate = moment(medicationDetails[i].medicationStartDate).format('YYYY-MM-DD');
											var instructionId = medicationDetails[i].instructionId
											var medicationDetailsObj = {};
											medicationDetailsObj["itemId"] = medicationDetails[i].itemId;
											medicationDetailsObj["isMedicationStop"] = 'N';
											medicationDetailsObj["itemName"] = medicationDetails[i].medicationName;
											medicationDetailsObj["storeId"] = $scope.storeId; 
											medicationDetailsObj["genericName"] = medicationDetails[i].genericName;
											medicationDetailsObj["uomUnitId"] = medicationDetails[i].uomTypeId;
											medicationDetailsObj["orderQty"] = medicationDetails[i].quantity;
											medicationDetailsObj["strength"] = medicationDetails[i].strength;
											medicationDetailsObj["prescriptionDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											medicationDetailsObj["authorisedBy"] = $scope.admissionRequestOrAdmissionObj.treatingDoctorId != null ? $scope.admissionRequestOrAdmissionObj.treatingDoctorId : $scope.admissionRequestOrAdmissionObj.doctorId;
											medicationDetailsObj["authorisedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											medicationDetailsObj["dose"] = medicationDetails[i].dose;
											medicationDetailsObj["doseUnitId"] = medicationDetails[i].doseUnitId;
											medicationDetailsObj["frequencyId"] = medicationDetails[i].frequencyId;
											medicationDetailsObj["routeId"] = medicationDetails[i].roaId;
											medicationDetailsObj["roaDesc"] = medicationDetails[i].roa_details;
											medicationDetailsObj["startDate"] = medicationStartDate+" 00:00:00";
											medicationDetailsObj["stopDate"] = medicationDetails[i].medicationEndDate+" 00:00:00";
											medicationDetailsObj["orderSetId"] =medicationDetails[i].orderSetId; 
											if(medicationDetails[i].isInstruction == 'Y'){
												medicationDetails[i].instructionId = '';
											}
											if(medicationDetails[i].instructionId != ''){
												medicationDetailsObj["instructionId"] = medicationDetails[i].instructionId;
												medicationDetailsObj["instructionDesc"] = null;
											}else{
												medicationDetailsObj["instructionDesc"] = medicationDetails[i].isInstructionText;
											}
											medicationDetailsObj["statflag"] = medicationDetails[i].isStat!=null?medicationDetails[i].isStat:false;		
											medicationDetailsObj["isNonFormulary"] =  medicationDetails[i].nonFormularyAdvice;
											
											if(medicationDetails[i].prescribedDosageUnitList.length>0 && medicationDetails[i].formulationCounterIndex!="" && medicationDetails[i].formulationCounterIndex!=null
													&& medicationDetails[i].formulationCounterIndex!=undefined)
											{
												 var list=$.grep(medicationDetails[i].prescribedDosageUnitList, function (obj) {
											        return medicationDetails[i].formulationCounterIndex === obj.counterIndex;
											    });
												 if(list.length>0){
													 if(list[0].formulationFactorId==1){
														 medicationDetailsObj.prescribedDose=medicationDetails[i].prescribeDose;
														 medicationDetailsObj.prescribedDoseUnitId=medicationDetails[i].prescribedDoseUnitId;
														 medicationDetailsObj.factorId=list[0].formulationFactorId;
														 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
													 }else{
														 medicationDetailsObj.factorId=list[0].formulationFactorId;
														 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
													 }
												 }
											}
											//medication list
											$scope.diagnosisMedicationDetailList.push(medicationDetailsObj);
										}
			                           
			                           angular.forEach($scope.diagnosisMedicationDetailList,function(value,key){
											if(value.orderQty==0 || isNaN(value.orderQty))
											{
												checkMedQty=true;
											}
										});
										if(checkMedQty==true){
											growl.warning("Please enter quantity of medication.",{
												title : WARNING_MSG
												});
											$rootScope.stopLoader();
												return false;
										}
			                           
			                           data.diagnosisMedicationDetailList = $scope.diagnosisMedicationDetailList;
									}
									if($scope.addedServiceOrdersForDisplay.length>0 &&  $scope.addedServiceOrdersForDisplay[0].serviceMasterId!=null && $scope.addedServiceOrdersForDisplay[0].serviceMasterId!="" && $scope.addedServiceOrdersForDisplay[0].serviceMasterId!=undefined)
									{
										$scope.finalOrderServiceList = [];
										angular.forEach($scope.addedServiceOrdersForDisplay, function(value, key) 
										{
											var newArr = {};
												newArr.serviceId = value.serviceMasterId;
												newArr.orderSetId = value.orderSetId;
												newArr.isInvestigationOrder = 'Y';
											$scope.finalOrderServiceList.push(newArr);
										});
										 //service
										data.diagnosisServiceList = $scope.finalOrderServiceList;
									}	
		                           //diagnosis list
		                           data.diagnosisList = $scope.dataListDiagnosis;
		                           data.isPharmacologicalValidationSkipIp= $scope.isPharmacologicalValidationSkipIp;
			                       //****** order set end******
								}
								
								var checkpass = null;
								if($scope.listVisiterPassMaster.length > 0){
									checkpass = false;
									angular.forEach($scope.listVisiterPassMaster, function (value, key) {
										
										if(value.passNumber != "" && value.visitorPassTypeId == 0){
											checkpass = true;
										}
										
									});
									
								}
								
								if(checkpass != null){
									
									 if (checkpass == true) {
											
										  growl.warning("Please Select Visitor Pass Type first.",{	
											title : "WARNING"
										});
										  $rootScope.stopLoader();
										return false;
										
									  }else{
										  $scope.newListVisiterPassMaster = [];
                                          angular.forEach($scope.listVisiterPassMaster, function (value, key) {
                                                if(value.passNumber != "" && value.visitorPassTypeId != 0){
                                                      $scope.newListVisiterPassMaster.push(value);
                                                }

                                            });

                                              data.listVisiterPassMaster = $scope.newListVisiterPassMaster;
									  }
									
								}
								
								
								
									
								   if($scope.admissionRequestOrAdmissionObj.patientClassId == '' || $scope.admissionRequestOrAdmissionObj.patientClassId == null || $scope.admissionRequestOrAdmissionObj.patientClassId == undefined)
							          {
							             data.patientClassId = null;
							          }
								console.log(data);
								//return false;
								
								var paymentEntlmId = "";
								isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
								  if (isPriorityZeroExists) {
									
									  growl.warning("Please select priority of all payment entitlement list.",{	
										title : "WARNING"
									});
									  $rootScope.stopLoader();
									// $scope.tab = 3;
									return;
									
								  }
								  else {
								 
								console.log('$scope.paymentEntitleMentList',$scope.paymentEntitleMentList);
								var sponserList = [];
								angular.forEach($scope.paymentEntitleMentList, function (value, key) {
									var newArr = {};
									newArr.organizationId = $scope.organizationId ;
									newArr.unitId = $scope.unitId;
									//newArr.visitTypeId = 2;
									newArr.visitTypeId = $scope.admissionRequestOrAdmissionObj.isDayCare=='Y' ? 3 : 2;
									//newArr.encounterId =$scope.appointmentMasterObj.encounterId;....patientSponsorDetailsDtosList
									newArr.paymentEntitlementId =value.paymentEntitlementId;
									newArr.payeeId = value.companyId;
									newArr.priorityId = value.priority;
									newArr.createdBy =$scope.userId;
									newArr.createdDate = $scope.currentDate;
									newArr.updatedBy = $scope.userId;
									newArr.updatedDate = $scope.currentDate;
									newArr.status = 'A';
									newArr.tariffId =value.tariffId;
									newArr.contractId =value.contractId;
									newArr.insuranceNumber = value.insuranceNumber;
									newArr.insuredAmount = value.insuredAmount;
									newArr.insuranceId = value.insuranceId;
                                    newArr.validityTo =  (value.endDate!=undefined && value.endDate!="" ) ? moment(moment(value.endDate,'DD-MM-YYYY').toDate()).format('DD-MM-YYYY HH:mm:ss') : value.endDate;//value.endDate;
                                    newArr.validityFrom = (value.startDate!=undefined && value.startDate!="" ) ? moment(moment(value.startDate,'DD-MM-YYYY').toDate()).format('DD-MM-YYYY HH:mm:ss') : value.startDate;//value.startDate;                                    
									newArr.employeeNoManual = value.employeeNoManual;
									newArr.employeeName = value.employeeName;
									newArr.relation = value.relation;
									newArr.medicalCardNo = value.medicalCardNo;
									newArr.claimNumber = value.claimNumber;
									if(value.paymentEntitlementId == 1){
										  newArr.validityTo =  null;
		                                  newArr.validityFrom = null;
									}
									if(value.priority == 1)
										{
											data.companyId = value.companyId;
											data.contractId = value.contractId;
											data.paymentEntitlementId = value.paymentEntitlementId;
											paymentEntlmId =  value.paymentEntitlementId;
										}
									sponserList.push(newArr);
								});
								data.patientSponsorDetailsDtosList = sponserList;
								data.paymentEntitlementId = paymentEntlmId;
								if($scope.diableDropDown== true)
									data.referralMasterDto = $scope.referralData;
								console.log("data",data);
								
//								return false;
							/*	$rootScope.startLoader();*/
								var URI = BASE_URL + ROOT_URL
										+ ADDBEDALLOCATION;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Patient Admitted Successfully..!!',
																		{
																			title : 'Success!'
																		});
														
														 if($scope.fileObj.file != null){
																$scope.uploadPancard($scope.admissionRequestOrAdmissionObj.patientId);
															}
														
														
														if($scope.patobj.patientImage1!=undefined &&  !angular.equals({}, $scope.patobj.patientImage1) && $scope.patobj.patientImage1!="")
														{
															$scope.admissionList=response.data.object;
//														    $scope.uploadPatientImage($scope.patobj.patientImage1,response.data.object.patientId,$scope.organizationId,$scope.unitId);
															$scope.uploadPatientMLCImage();
															
														   }
														//upload
														
														
														$scope.updateAbscondPatient($scope.admissionRequestOrAdmissionObj.patientId);
														$rootScope.stopLoader();
														$scope.init();
														$scope.initKinDetails();
														$scope.activeBedList=[];
														$scope.medicationDetails = [];
														$scope.diagnosisMedicationDetailList = [];
														$scope.addedServiceOrdersForDisplay = [];
														$scope.dataListDiagnosis = [];
														$scope.dataListDiagnosis = [{
															diagnosisId : '',
															diagnosisType:'1',
															type : '1',
															remark : '',
															diagnosisNameText:'',
															isDiagnosisName : 'N',
															diagnosisCode:'',
															diagnosisName :null
														}];
														$scope.selectedOrderSet = [];
														$scope.selectedOrderSet1 = [];
														$scope.setSecondTabLevel(1);
														$scope.disableDayCareChk=false;
														$scope.initDepositMaster();
														$scope.setSecondTabLevel(1);
														//document upload against admission start 
														if($scope.allDocumentFinalList.length>0){
															 $scope.count=0;
															 angular.forEach($scope.allDocumentFinalList, function(value, key) 
																		{  									
																 			value.admissionId=response.data.object.admissionId;
																			var promise = $scope.uploadEncounterContractDoc(value);
																			promise.then(function(response) 
																					{
																					  if(response.data.status == "success")
																						  {
																						    $scope.count++;
																						    if($scope.finalDocUploadList.length==$scope.count)
																							 {
																							  
																							   $rootScope.stopLoader();
																							   $scope.encounterDetailsContractList = [];
																							   $scope.finalDocUploadList=[];
																							   $scope.allDocumentFinalList=[];
//																							   $scope.getPatientDocumentList();
																							   $rootScope.stopLoader();
																							 }
																					      }
																					  else 
																						{																						
																							 $rootScope.stopLoader();
																						}
																				    });
																				
																		});
														}
														
														// document upload against admission end
														
														/*
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
												                  "paramValue": response.data.object.reqDto.depositMasterDto.depositMasterId
												                }
												                 ]
												      };
														reportService.generate(REPORT_API_URI1,data);*/
														
														
														var data =  {
												                 "appName": "HIS",
												                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
												                 "reportDesignFilePath": "AdmissionPaper.rpt",
												                 "reportFormat": "PDF",
												                 "exportFileName": "AdmissionPaper",
												                 "reportParams": [
												                   {
												                  "paramName": "@admission_id",
												                  "paramType": "Integer",
												                  "paramValue": response.data.object.admissionId
												                }
												                 ]
												      };
														reportService.generate(REPORT_API_URI1,data);
														
														if($scope.isPharmacologicalValidationSkipIp==true && response.data.object.diagnosisMedicationDetailList!=null){
															
															var data = {
																	"organizationId" : $scope.organizationId,
																	"unitId" : $scope.unitId,
																	"visitTypeId" : response.data.object.visitTypeId,
																	"employeeId" : $scope.employeeId,
																	"doctorId" : response.data.object.doctorId,
																	"prescriptionDate" :  moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
																	"authorisedBy" : response.data.object.doctorId,
																	"authorisedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
																	"addedBy" : $scope.userId,
																	"addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
																	"admissionId" : response.data.object.admissionId,
																	"patientId" : response.data.object.patientId,
//																	"priorityId" : 1,
																	"priorityId" : 2,
																	"isDischarge" : false,
																	 "deptId" : response.data.object.specialityId,
																	 "isPharmacologicalValidationSkipIp": $scope.isPharmacologicalValidationSkipIp
																};
															data.prescriptionDetailsDtosList =response.data.object.diagnosisMedicationDetailList;
															
															var URI = BASE_URL + ROOT_URL
															+ "/api/pharmacy/ip/pharmacology/validation/savePresWithouthPharmaValidation";
															
															GenericServiceParamHeader
															.serviceAction(METHOD_POST, URI,
																	data,
																	NOTIFICATION_MSG_STATUS_TRUE)
															.then(
																	function(response) {
																		if (response.data.status == "success") {
																			
																			$rootScope.stopLoader();
																		} else {
																			$rootScope.stopLoader();
																		}
																	});
														}else{
															$rootScope.stopLoader();
														}
														$rootScope.stopLoader();
													} else if (response.data.code==409) {
														growl
																.warning(
																		response.data.message,
																		{
																			title : 'Warning!'
																		});
														//$scope.initKinDetails();
														$rootScope.stopLoader();
													}else{
														growl
														.error(
																response.data.message,
																{
																	title : 'Error!'
																});
												//$scope.initKinDetails();
												$rootScope.stopLoader();
													}

												});
								
								}
							}

//							}
							}
							
							
							/***===================DEPOSIT MATRIX CODE ************/
							
							//for cheque
							$scope.openDatePickerCheque = function() {
								$scope.datepickercheque.opened = true;
							};
							$scope.datepickercheque = {
									opened : false
								};
							//------
							
							$scope.openDatePickerCredit = function() {
								$scope.datepickerCr.opened = true;
							};
							$scope.datepickerCr = {
									opened : false
								};
							
							$scope.openDatePickerDebit = function() {
								$scope.datepickerDr.opened = true;
							};
							$scope.datepickerDr = {
									opened : false
								};	
							
							$scope.getDepositAgainstAdmission = function()
							{	
								$scope.depositSearchList = [];
								
								if($scope.admissionObj.admissionNoteId!=null)
									{
									$rootScope.startLoader();
									$scope.depositSearch.orgId = $scope.organizationId;
									$scope.depositSearch.unitId = $scope.unitId;
									$scope.depositSearch.admissionNoteId = $scope.admissionObj.admissionNoteId;
									var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/search";
									BillingGenericService.serviceAction(METHOD_POST,URI,$scope.depositSearch,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
											$scope.depositReq = {depositMasterDto:$scope.deposit,listPaymentDepositDetailsDto:$scope.listPaymentDetails};
//											$scope.depositSearchList = response.data.listObject;
											
											$scope.depositSearchList = $scope.depositReq.listPaymentDepositDetailsDto;
											$('#depositDetailsModal').modal('show');
										}
										else if (response.data.status == "error"){
											$rootScope.stopLoader();
										}
									  });
									}
								else{
									$('#depositDetailsModal').modal('hide');
								}
							}
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							$scope.isSetSecondTabLevel = function(tabNum) 
							{
								return $scope.secondTabLevel === tabNum;
							};
							
							$scope.setSecondTabLevel = function(newTab) {
								$scope.secondTabLevel = newTab;
							}
							$scope.addBtnId=false;
							$scope.pinpadAmounFlag =false;
							$scope.setPaymentModeId = function(value)
							{
								if(value==3 && $scope.isPinPadRequired =='Y'){
									$scope.addBtnId=true;
									 //$scope.pinpadAmounFlag =true;
									$scope.objPosMachine.deviceName= $cookies.getObject('deviceName');
								}else if(value==6){
									$scope.addBtnId=true;
								}
								else{
									$scope.addBtnId=false;
									 $scope.pinpadAmounFlag =false;
								}
								$scope.paymentDetails.paymentModeId = value;
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
								$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
								$scope.eTransferObj={};
							}
							
							$scope.initDepositObj = function(){
								$scope.deposit = angular.copy({depositTypeId:"2",visitTypeId:"",depositAmount:"",remarks:"",receivedFrom:"",identificationNo:"",patientId:"",encounterId:"",admNoteId:"",counterId :$rootScope.counterId})
							}
							$scope.initDepositSearchObj = function(){
								$scope.depositSearch = angular.copy({fromDate:"",toDate:"",patientName:"",uhidNumber:"",visitTypeId:"",depositTypeId:"",admissionNoteId:"",admissionId:""});
							}
							
						
							$scope.initDepositMaster = function()
							{
								$scope.deposit = {};
								$scope.initDepositObj();
								$scope.paymentDetails = {};
								$scope.initPaymentDetailsObj();
								$scope.initDepositSearchObj();
								$scope.listPaymentDetails = [];
								$scope.depositReq = {};
								$scope.getBankList();
								$scope.getHospitalBankList();
								$scope.getCardTypesList();
							}
							$scope.initDepositMaster();
							
							

							$scope.addPaymentDetails = function(){
									
									//check amount collected is greater tha deposit amount
									var depositAmount = $scope.dueAmount != "" && typeof($scope.dueAmount) != "undefined" ? parseFloat($scope.dueAmount) : parseFloat(0) ;
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
										cashLimit = cashLimit +  parseFloat(tempAmount);
										if(($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null) && cashLimit > $scope.cashAmountLimit){
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
										
										if($scope.transactionId == undefined || $scope.transactionId == null || $scope.transactionId == ""){
											growl.warning("Please Enter Transaction Id",{
												title : WARNING_MSG
											});
											return false;
										}
										if($scope.mobileNo == undefined || $scope.mobileNo == null || $scope.mobileNo == ""){
											growl.warning("Please Enter Mobile No",{
												title : WARNING_MSG
											});
											return false;
										}
										
										tempAmount = $scope.mpesaAmount != '' && typeof($scope.mpesaAmount) != 'undefined' ? parseFloat($scope.mpesaAmount) : parseFloat(0);
										
										if(parseFloat($scope.mpesaAmount) > depositAmount){
											growl.warning("Entered amount should be equal to or less than deposit amount",{
												title : WARNING_MSG
											});
											return;
										}
										var invalidMpesa = false;
										angular.forEach($scope.listPaymentDetails,function(value,key){
											if($scope.transactionId == value.cardNo)
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
											
										
									case 12 :
										
										tempAmount = $scope.razorPaidAmount != null && $scope.razorPaidAmount != '' && typeof($scope.razorPaidAmount) != 'undefined' ? parseFloat($scope.razorPaidAmount) : parseFloat(0);

										if(parseFloat($scope.razorAmount) > depositAmount){
											growl.warning("Entered amount should be equal to or less than deposit amount",{
												title : WARNING_MSG
											});
											return;
										}
										break;
											break;

									}
									
									if(tempAmount == 0){
										growl.warning("Amount cannot be zero",{
											title : WARNING_MSG
										});
										return;
									}
									
									/*angular.forEach($scope.listPaymentDetails,function(value,key){
										collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
									});
									collectedAmount = collectedAmount + tempAmount;
									if(collectedAmount > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									*/
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
											var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:$scope.paymentDetails.transactionNo,skipMpesaValidation:$scope.pinpadAmounFlag,requestedUniqueId:$scope.paymentDetails.requestedUniqueId,deviceName : $scope.objPosMachine.deviceName, creditDebitCardType:$scope.paymentDetails.creditDebitCardType, transactionTypeName:$scope.paymentDetails.transactionTypeName};
											$scope.listPaymentDetails.push(paymentDetails);
											break;
										case 4 :
											var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.debExpiryDate,false);
											var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.debAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.debCardHolderName,cardTypeId:$scope.paymentDetails.debCardTypeId,cardNo:$scope.paymentDetails.debCardNumber,expiryDate:expiryDate};
											$scope.listPaymentDetails.push(paymentDetails);
											break;
											
										case 5:				
											var paymentDetails = { paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaAmount).toFixed(2), createdBy:$scope.userId, cardNo:$scope.transactionId,transactionNo:$scope.transactionId,skipMpesaValidation : $scope.skipMpesaValidation };
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
											 $scope.validatTextFieldFlag = true;
											break;
										
										case 12:				
											var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.razorPaidAmount).toFixed(2), createdBy:$scope.userId,transactionNo:$scope.razorUniqueId,paymentGatewayProviderId:$scope.payGatewayProviderId};
											$scope.listPaymentDetails.push(paymentDetails);
											break;
									}
									$scope.initPaymentDetailsObj();
									$scope.setFirstTabLevel(1);
							        $scope.setPaymentModeId(1);
									$scope.calculateDueAmount();
									$scope.calculateTotalAmount();
									
								}
								
							
							$scope.initClockPicker = function() {
                                     $("#page-content-wrapper").scroll(function() {
                                       var topPos = $("input[id$='time']").offset().top;
                                       var leftPos = $("input[id$='time']").offset().left;
                                       var botPos = $("input[id$='time']").offset().bottom;
                                       console.log(topPos);
                                       console.log(leftPos);
                                       $(".popover").css("top", topPos + 35);
                                       $(".popover").css("bottom", botPos + 35);
                                       $(".popover").css("left", leftPos);
                                     });

								$('.clockpicker').clockpicker({
									afterDone: function() {
										var todayDate = new Date();
										todayDate = moment(todayDate).format('DD/MM/YYYY');
										 if (todayDate == moment( $scope.datepicker).format('DD/MM/YYYY')){
											 var tmp  = moment( $scope.datepicker).format('MM/DD/YYYY')
											 var ftmp = tmp+' '+$scope.fromTimeDoa;
											 var ytmp = moment(new Date(ftmp),'hh:mm');
											 var time = moment(new Date(),'hh:mm');
											 	if(ytmp.isBefore(time)){

											 	}else{
											 		growl.warning("Admission time should be equal or less then current time.",{title : "WARNING!"
													});
											 		$scope.fromTimeDoa = moment(new Date()).format('hh:mm');
													return false
											 	}
										 }
			                        }
								});
							}
							
							$scope.clearReferral = function(){
								$scope.admissionRequestOrAdmissionObj.referralTypeId = '';
								$scope.admissionRequestOrAdmissionObj.referralDesc = '';
								$scope.admissionRequestOrAdmissionObj.refferalRemark = '';
								$scope.admissionRequestOrAdmissionObj.referralId = '';
							}
							
							$scope.commonChangeDeposit = function(){
								
								var bedCategoryId = null;
								if($scope.admissionRequestOrAdmissionObj.reservedBillingBedCategoryId>0)
									{
									   bedCategoryId = $scope.admissionRequestOrAdmissionObj.reservedBillingBedCategoryId;
									}
								 if($scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId>0)
									{
									bedCategoryId = $scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId;
									}
								 if($scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId>0)
									{
									  bedCategoryId = $scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId;
									}
						    	 if($scope.appointmentMasterObj.paymentEntitlementTypeId=="")
									   {
										   $scope.depositStatus = true;
									   }
									   else if($scope.admissionRequestOrAdmissionObj.admissionTypeId=="")
									    {
										   $scope.depositStatus = true;
								   } 
									   else if(bedCategoryId=="" || bedCategoryId == null)
									    {
									   $scope.depositStatus = true;
									    }	
										 else
									   {
											  $scope.depositStatus = false;
									   }
							}
								$scope.getFormattedDate = function(date,isTime){
									var tempDate = new Date(date);
									var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
									var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
									var year = tempDate.getFullYear();
									if(isTime)
										return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+tempDate.getSeconds();
									else
										return date + "-" + month + "-" + year;
								}
								$scope.calculateDueAmount = function(){
									//calculate due amount
									var collectedAmount = parseFloat(0).toFixed();
									angular.forEach($scope.listPaymentDetails,function(value,key){
										collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
									});
									$scope.dueAmount = $scope.deposit.depositAmount != '' && typeof($scope.deposit.depositAmount) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
									var paid = $scope.paidDeposit == undefined || $scope.paidDeposit== null ||$scope.paidDeposit =="" ? 0: $scope.paidDeposit;
									var balance = parseInt($scope.dueAmount);
									$scope.dueAmount = paid + balance;
									$scope.paymentDetails.cashAmount =$scope.dueAmount;
									$scope.paymentDetails.chequeAmount = $scope.dueAmount;
									$scope.paymentDetails.debAmount = $scope.dueAmount;
									$scope.paymentDetails.credAmount = $scope.dueAmount;
									$scope.eTransferObj.transferAmount = $scope.dueAmount;
									$scope.razorAmount =  $scope.dueAmount;
								}
								
								$scope.calculateDueAmountOnCollect = function(){
									//calculate due amount
								/*	var collectedAmount = parseFloat(0).toFixed();
									angular.forEach($scope.listPaymentDetails,function(value,key){
										collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
									});*/
									//$scope.dueAmount = $scope.deposit.depositAmount != '' && typeof($scope.deposit.depositAmount) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
									$scope.dueAmount = $scope.balanceDeposit;
									$scope.paymentDetails.cashAmount =$scope.dueAmount;
									$scope.paymentDetails.chequeAmount = $scope.dueAmount;
									$scope.paymentDetails.debAmount = $scope.dueAmount;
									$scope.paymentDetails.credAmount = $scope.dueAmount;
									$scope.eTransferObj.transferAmount = $scope.dueAmount;
									$scope.razorAmount =  $scope.dueAmount;
								}
								$scope.calculateTotalAmount = function(){
									//calculate due amount
									var tatalAmount = parseFloat(0).toFixed();
									angular.forEach($scope.listPaymentDetails,function(value,key){
										tatalAmount = parseFloat(tatalAmount) + parseFloat(value.amount);
									});
									$scope.tatalAmount = tatalAmount;
								//	$scope.deposit.depositAmount  = $scope.tatalAmount;
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
							
								
						$scope.collectPayment = function()
						  {
								$scope.skipMpesaValidation = false;
						        $scope.setFirstTabLevel(1);
								$scope.send=false;
								$scope.resend=false;	
								$scope.paymentDetails.otp ="";
								//$scope.calculateTotalDueRefAmount();
								$('#collectPaymentModalAdmission').modal('show');
								$scope.deposit.depositAmount = 	$scope.balanceDeposit;
								$scope.deposit.depositPending = 	$scope.balanceDeposit;
								$scope.paymentDetails.cashAmount = 	$scope.balanceDeposit;
								 $scope.validatTextFieldFlag = true;
								$scope.listPaymentDetails = [];
								$scope.calculateDueAmountOnCollect();
								$scope.calculateTotalAmount();
						  }
							 $scope.getDepositMatrixList = function(bedCategoryId)
								{
								    $scope.setPaymentModeId(1);
								    $scope.listPaymentDetails = [];
									$scope.depositReq = null;
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/billing/unit/depositMatrixConfig";
									
									var param = 
									    {
											  patientTypeId : $scope.admissionRequestOrAdmissionObj.patientCategoryId,
											  paymentEntitlementId : $scope.appointmentMasterObj.paymentEntitlementTypeId,
											  bedCategoryId :bedCategoryId,
											  organsationId : $scope.organizationId,
											  unitId : $scope.unitId,
											  sergicalOrMedical : $scope.admissionRequestOrAdmissionObj.admissionTypeId,
											  againstDeposit : 2//for Admission
										};
									console.log('param',JSON.stringify(param));
									return GenericService.serviceAction(METHOD_POST,URI,param).then(function(response) {
										if (response.data.status == "success")
										{
											var depositMatrixList = response.data.listObject;
											if(depositMatrixList==null || depositMatrixList.length==0)
												{

												 $rootScope.stopLoader();
												 /*growl.error("Please add deposit in Deposit Matrix.",{title : "WARNING!"
													});;*/
													return false

												}
											else{
												
												 if(angular.equals({}, $stateParams.admissionObj)||$stateParams.admissionObj.depositAmount==0)
													{
													$scope.depositAmount = depositMatrixList[0].reservationDeposit;
													$scope.paidDeposit = 0.00;
													$scope.balanceDeposit = parseFloat($scope.depositAmount) - parseFloat($scope.paidDeposit);
													$rootScope.stopLoader();
													}
												 else if(parseFloat($stateParams.admissionObj.depositAmount==undefined?0:$stateParams.admissionObj.depositAmount)<depositMatrixList[0].reservationDeposit)
													 {
														$scope.depositAmount = depositMatrixList[0].reservationDeposit;
														$scope.paidDeposit = parseFloat($stateParams.admissionObj.depositCollected);
														$scope.balanceDeposit =(parseFloat($scope.depositAmount)-parseFloat($scope.paidDeposit));
													 }
												
											}
											
											
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											  $rootScope.stopLoader();
											}
										return response.data.listObject;
									});
								}
							
							 $scope.actualdepositAmount = "";
							$scope.getDepositMatrix = function()
							{
								var bedCategoryId = null;
								if($scope.admissionRequestOrAdmissionObj.reservedBillingBedCategoryId>0)
									{
									   bedCategoryId = $scope.admissionRequestOrAdmissionObj.reservedBillingBedCategoryId;
									}
								 if($scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId>0)
									{
									bedCategoryId = $scope.admissionRequestOrAdmissionObj.currentBillingBedCategoryId;
									}
								 if($scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId>0)
									{
									  bedCategoryId = $scope.admissionRequestOrAdmissionObj.changeBillingBedCategoryId;
									}
								$scope.tab = 4;
						    	 if(angular.equals({}, $stateParams.admissionObj)||$stateParams.admissionObj.depositAmount==0)
						    		 {
					
									   if($scope.admissionRequestOrAdmissionObj.patientCategoryId=="")
										   {
										   growl.error("Please Select Patient..",{title : "WARNING!"
											});
										   $scope.depositStatus = true;
											return false;
										   }
									   else if($scope.appointmentMasterObj.paymentEntitlementTypeId=="")
									   {
										   growl.error("Please Select Payment EntitleMent..",{title : "WARNING!"
											});
										   $scope.depositStatus = true;
											return false;
									   }
									   else if($scope.admissionRequestOrAdmissionObj.admissionTypeId=="")
									    {
									     growl.error("Please Select Admission Type.",{title : "WARNING!"
										});
									    $scope.depositStatus = true;
										return false;	
								   } 
									   else if(bedCategoryId=="" || bedCategoryId == null)
									    {
									     growl.error("Please Select Billing Bed Category.",{title : "WARNING!"
										});
									   $scope.depositStatus = true;
										return false;
									    }	
										 else
									   {
											 
										if($scope.paidDeposit > 0){
											return false; // HIS-13653 resolve this issue
										}
										   $scope.depositAmount =0.00;
										   $scope.paidDeposit = 0.00;
										   $scope.balanceDeposit = 0.00;
										   var promise = $scope.getDepositMatrixList(bedCategoryId);
										   promise.then(function(depositMatrixList) 
													{
													  if(depositMatrixList != null && depositMatrixList!=false && (depositMatrixList.length==0))
														  {
														     if(depositMatrixList[0].reservationDeposit==null)
														    	 {
														    	 $rootScope.stopLoader();
														    	 $scope.depositStatus = true;
														    	 /* growl.error("Deposit Matrix Amount Not Found.",{title : "WARNING!"
																	});*/
														    	  return false;

														    	 }
														     else
														        {
														    		  $scope.depositPending = depositMatrixList[0].reservationDeposit;
															    	  $scope.depositAmount = depositMatrixList[0].reservationDeposit;
																	  $scope.paidDeposit = 0;
																	  $scope.balanceDeposit =(parseFloat($scope.depositAmount)-parseFloat($scope.paidDeposit));
																	  $scope.depositStatus = false;
																	  $scope.actualdepositAmount  = $scope.depositAmount;
														        }
														  }
													  else{
														  $scope.depositStatus = true;
														  $scope.actualdepositAmount  = $scope.depositAmount;
														  $rootScope.stopLoader();
														  /*growl.error("Deposit Matrix Amount Not Found.",{title : "WARNING!"

															});*/
												    	   return false;
													    }
													});
									   }
						    		 }
						    	  else
						    	  {
						    		  if($scope.depositStatus==false)
						    		  {
						    			  $scope.depositAmount =parseFloat($stateParams.admissionObj.depositAmount);
										  $scope.paidDeposit = parseFloat($stateParams.admissionObj.depositCollected);
										  $scope.balanceDeposit =($scope.depositAmount)-parseFloat($scope.paidDeposit);
										  $scope.actualdepositAmount  = $scope.depositAmount;
						    		  }
									  $rootScope.stopLoader();
						    	  }
									  
									
							}
							
							$scope.tempBalanceDeposit = 0;
							$scope.changeMatrixDepositeAmount = function(){
						    	  
								var tempBalanceDeposit =(parseFloat($scope.depositAmount)-(($scope.paidDeposit!= undefined && $scope.paidDeposit!= null)?parseFloat($scope.paidDeposit):0));
								if(tempBalanceDeposit <0){
									 $scope.depositAmount = $scope.tempBalanceDeposit;
									 
									 growl.warning("If you want to change admission deposit please clear the paid amount",{
											title : " Balance amount going in minus !!!"
										});
										return;
									 
								}else{
									$scope.balanceDeposit =(parseFloat($scope.depositAmount)-(($scope.paidDeposit!= undefined && $scope.paidDeposit!= null)?parseFloat($scope.paidDeposit):0));
									 
									 $scope.tempBalanceDeposit = $scope.depositAmount;
								}
								 
								}
							$scope.clearPayment = function(){
						    	  
								 $scope.depositAmount = $scope.actualdepositAmount; 
								 $scope.paidDeposit = 0;
								 $scope.balanceDeposit = $scope.depositAmount;
								 $scope.listPaymentDetails = [];
							}
							
							
							$scope.uploadPancard = function(Obj)
							{
								 var fd = new FormData();
							        fd.append('organizationId', $scope.organizationId);
							        fd.append('billId',Obj);
							        fd.append('isDeposite',true);
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
							
						$scope.addDeposit = function(){
							var collectedAmount = parseFloat(0).toFixed();
							angular.forEach($scope.listPaymentDetails,function(value,key){
								if(value.paymentModeId == 1){
									collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
								}
								
							});
							
							if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
								$('#uploadpanCardDetailsAdmission').modal('show');
									return false;
							}
							$('#uploadpanCardDetailsAdmission').modal('hide');
					    	
							
							$scope.depositStatus = true;
								if(Number($scope.tatalAmount) == 0){
									growl.error("Please clear Due Amount.",{
										title : ERROR_MSG
									});
									return;
								}
								$rootScope.startLoader();
								$scope.deposit.createdBy = $scope.userId;
								$scope.deposit.orgId = $scope.organizationId;
								$scope.deposit.unitId = $scope.unitId; 
								$scope.deposit.counterId = $rootScope.counterId; 
								$scope.deposit.admNoteId = $stateParams.admissionObj.admissionNoteId;
								$scope.deposit.patientId =  $scope.admissionRequestOrAdmissionObj.patientId;
								$scope.deposit.visitTypeId = $scope.admissionRequestOrAdmissionObj.isDayCare=='Y' ? 3 : 2;
								$scope.paidDeposit = parseFloat($scope.tatalAmount);
								/*$scope.paidDepositnew = $scope.paidDeposit;
								if($scope.paidDeposit == 0){
									$scope.paidDeposit = parseFloat($scope.tatalAmount);
								}else{
									$scope.paidDeposit = $scope.paidDepositnew + parseFloat($scope.tatalAmount);
								}*/
								
								$scope.balanceDeposit = ($scope.depositAmount) - parseFloat($scope.paidDeposit);
								
								$scope.deposit.depositAmount = 	$scope.paidDeposit;
								$scope.deposit.depositPending = $scope.balanceDeposit;
								
								$scope.depositReq = {depositMasterDto:$scope.deposit,listPaymentDepositDetailsDto:$scope.listPaymentDetails};
								console.log("$scope.depositReq",JSON.stringify($scope.depositReq));
								$rootScope.stopLoader();
								$('#collectPaymentModalAdmission').modal('hide');
								  //$scope.paidDeposit = parseFloat($scope.paidDeposit)+parseFloat($scope.deposit.depositAmount);
							
								growl.success("Deposit Added Successfully.",{
									title : SUCCESS_MSG
								});
								
						}
						//document upload functionality for payment entitle ment start
						
						$scope.getDocumentType = function(){
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
							BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.documentTypeList = [];
									if (response.data.status == "success") {
										$scope.documentTypeList = response.data.listObject;
										 angular.forEach($scope.documentTypeList, function(value, key) 
										 {  
								 			$scope.corporateDocTypeId=value.documentTypeId;
										 });
									}
								});
						}
						$scope.getDocumentType();
						 $scope.getPatientDocumentList = function(obj)
					     {
					      
					      if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
								growl.error(
										'Please select atleast one patient!!!.',
										{
											title : 'Error!'
										});
									$scope.encounterDetailsContractList =[];
									return;
							}
					      $rootScope.startLoader();
					      var data =
					        {
					        "patientId":$scope.patientDetails.patientId,
					        "unitId":$scope.unitId,
					        "organisationId":$scope.organizationId,
					        "userId":$scope.userId,
					      //  "visitTypeId":2,
					        "visitTypeId":$scope.admissionRequestOrAdmissionObj.isDayCare=='Y' ? 3 : 2,
					        "admissionId":0,
					       }
					      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
					      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					        $scope.patientDocumentList = [];
					        if (response.data.status == "success") 
					        {
//					        	$scope.patientDocumentList = response.data.listObject;  
					            $scope.patientAllDocumentList = response.data.listObject;
					            $scope.patientDocumentList=[];
					            angular.forEach($scope.patientAllDocumentList, function(value, key) 
										{  
					            if(obj.paymentEntitlementId==1){
					            	if(value.documentTypeId !=30 && value.documentTypeId!=19){				            				
					         			
					         			var data={
					         					documentTypeId:value.documentTypeId,
					         					documentName:value.documentName,
					         					documentPath:value.documentPath,
					         					createdDate:value.createdDate
					         			}
					         			$scope.patientDocumentList.push(data);
					     			}
					            }else if(obj.paymentEntitlementId==2){
					            	if(value.documentTypeId==30){				            				
					         			
					         			var data={
					         					documentTypeId:value.documentTypeId,
					         					documentName:value.documentName,
					         					documentPath:value.documentPath,
					         					createdDate:value.createdDate
					         			}
					         			$scope.patientDocumentList.push(data);
					     			}
					            }else if(obj.paymentEntitlementId==3){
					            		if(value.documentTypeId==19){				            				
					         			
					         			var data={
					         					documentTypeId:value.documentTypeId,
					         					documentName:value.documentName,
					         					documentPath:value.documentPath,
					         					createdDate:value.createdDate
					         			}
					         			$scope.patientDocumentList.push(data);
					     			}
					            }else{
					            	
					            }	
					 			
						});    
					            $rootScope.stopLoader();
					        }
					        else if(response.data.status == "error")
					         {
					        	$rootScope.stopLoader();
					         }
					       });
					     }
						
						 $scope.addImageDetailsForContract = function(obj)
							{ 
								var encounterDetails = angular.copy($scope.encounterDetailsContract);
								encounterDetails.patientId = $scope.patientDetails.patientId;
								//insurance
								if( obj.paymentEntitlementId==2){
									encounterDetails.documentTypeId = '30';	
									encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
									
								}else if(obj.paymentEntitlementId==3){
									encounterDetails.documentTypeId = '19';	
									encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
								}else{
									encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
								}
								
								$scope.encounterDetailsContractList.push(encounterDetails);
							};

						  $scope.removeImageDetailsContract = function(deleteIndex)
							{ 
							   $scope.encounterDetailsContractList.splice(deleteIndex, 1);
						    }
						  $scope.openDocument = function(obj)
							{
								
							  $('#fileId').val(undefined);
								$scope.encounterDetailsContract = {
										  encounterDocId : "",
										  encounterId : 0,
										  admissionId:0,
										  //visitTypeId:1,
										  visitTypeId:$scope.admissionRequestOrAdmissionObj.isDayCare=='Y' ? 3 : 2,
										  patientId : "",
										  documentPath : "",
										  documentTypeId : '',
										  documentTypeName : '',
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  organisationId : $scope.organizationId,
										  file : {}
										}
							
									if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
										growl.error(
												'Please select atleast one patient!!!.',
												{
													title : 'Error!'
												});
											$scope.encounterDetailsContractList =[];
											return;
									}
								
								/*$scope.encounterDetailsContractList =[];
								var encounterDetails = angular.copy($scope.encounterDetailsContract);
								encounterDetails.patientId = $scope.patientDetails.patientId;	
								$scope.encounterDetailsContractList.push(encounterDetails);
								 $scope.getPatientDocumentList();
								 $('#documentUploadModalForAdmission').modal('show');*/
								//1-self,2-insurance,3-corporate 
								$scope.paymentEntId=undefined;
								if(obj.paymentEntitlementId==1){			
									
									$scope.encounterDetailsContractList =[];
									var encounterDetails = angular.copy($scope.encounterDetailsContract);
									encounterDetails.patientId = $scope.patientDetails.patientId;
									encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
									$scope.encounterDetailsContractList.push(encounterDetails);	
									$scope.getPatientDocumentList();
									$scope.paymentEntId=obj.paymentEntitlementId;
									$('#documentUploadModalForAdmission').modal('show');
								}else if(obj.paymentEntitlementId==2){
									
									$scope.encounterDetailsContractList =[];
									var encounterDetails = angular.copy($scope.encounterDetailsContract);
									encounterDetails.documentTypeId = '30';	
									encounterDetails.patientId = $scope.patientDetails.patientId;
									encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
									$scope.encounterDetailsContractList.push(encounterDetails);
									 $scope.getPatientDocumentList(encounterDetails);
									 $scope.paymentEntId=obj.paymentEntitlementId;
									 $('#documentUploadModalForAdmission').modal('show');
									
								}else if(obj.paymentEntitlementId==3){
									$scope.encounterDetailsContractList =[];
									var encounterDetails = angular.copy($scope.encounterDetailsContract);
									encounterDetails.documentTypeId = '19';	
									encounterDetails.patientId = $scope.patientDetails.patientId;	
									encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
									$scope.encounterDetailsContractList.push(encounterDetails);
									 $scope.getPatientDocumentList(encounterDetails);		
									 $scope.paymentEntId=obj.paymentEntitlementId;
									$('#documentUploadModalForAdmission').modal('show');
								}else{
									
								}
									
								
							}
						  $scope.uploadEncounterContractDoc = function(Obj)
							{
								 var fd = new FormData();
							        fd.append('encounterId', Obj.encounterId);
							        fd.append('visitTypeId', Obj.visitTypeId);
							        fd.append('admissionId', Obj.admissionId);
							        fd.append('patientId',Obj.patientId );
							        fd.append('documentTypeId', Obj.documentTypeId);
							        fd.append('createdBy',Obj.createdBy );
							        fd.append('updatedBy', Obj.updatedBy);
							        fd.append('unitId', Obj.unitId);
							        fd.append('grnId',0);
									fd.append('poId',0);
							        fd.append('organisationId', Obj.organisationId);
							        fd.append('file',Obj.file );
							        fd.append('expenseId', 0);
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
						  $scope.documentName = function(id)
						  {
							
							  var documentTypeName = $.grep($scope.documentTypeList, function (doc) {
					  	           return doc.documentTypeId == id;
					  	       })[0].documentTypeName;
							  return documentTypeName;
						  }
						  $scope.allDocumentFinalList=[];
						  $scope.uploadContractDoc = function()
						    {
							  $scope.finalDocUploadList=[];
							  var count=0;
							 angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
								  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
										 growl.warning("Please select document type .",
																	{
																		title : 'warning!'
																	});
														   $rootScope.stopLoader();
										return false;
									}
								  if (angular.equals(value.file, {}) || value.file==undefined) {
											growl.warning('Please select file.',
															{
																title : 'warning!'
															});
											 $rootScope.stopLoader();
											return false;
										} 
									 var ext = value.file.name.split(".").pop().toLowerCase();
										if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
											growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											return false;
										}
										$scope.finalDocUploadList.push(value);
										 console.log("final list",$scope.finalDoc);
							});
							 
			
							 if($scope.encounterDetailsContractList.length==$scope.finalDocUploadList.length){
								   $('#documentUploadModalForAdmission').modal('hide');
								   angular.forEach($scope.encounterDetailsContractList, function(value, key) { 
									   value.documentTypeName= $scope.documentName(value.documentTypeId);
									   $scope.allDocumentFinalList.push(value);
								   });
								   console.log("final doc list",$scope.allDocumentFinalList);
							 }
							
						    }
							 $scope.removeDoc=function(index){
								 $scope.allDocumentFinalList.splice(index, 1);
							 }
							 
							/* $scope.uploadContractDoc = function()
							    {
								 angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
									  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
											 growl.error("Please select document type .",
																		{
																			title : 'Errror!'
																		});
															   $rootScope.stopLoader();
											return false;
										}
//									  if (value.file == undefined || value.file == "" || value.file == null ) {
									  if (angular.equals(value.file, {}) || value.file==undefined) {
												growl.error('Please select file.',
																{
																	title : 'Error!'
																});
												 $rootScope.stopLoader();
												return false;
											} 
										 var ext = value.file.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
											}
								});
								 $rootScope.startLoader();
								  $scope.count = 0;
								 angular.forEach($scope.encounterDetailsContractList, function(value, key) 
									{  
									 if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
										
											$rootScope.stopLoader();
										return false;
									}
								  if (value.file == undefined || value.file == "" || value.file == null ) {
									 if (angular.equals(value.file, {}) || value.file==undefined) {		
											 $rootScope.stopLoader();
											return false;
										} 
									 var ext = value.file.name.split(".").pop().toLowerCase();
										if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
											
											$rootScope.stopLoader();
											return false;
										}
										var promise = $scope.uploadEncounterContractDoc(value);
										promise.then(function(response) 
												{
												  if(response.data.status == "success")
													  {
													    $scope.count++;
													    if($scope.encounterDetailsContractList.length==$scope.count)
														 {
														   growl.success("Document Uploaded Successfully.",
																	{
																		title : 'Success!'
																	});
														   $rootScope.stopLoader();
														   $('#documentUploadModalForAdmission').modal('hide');
														   $scope.encounterDetailsContractList = [];
														   $scope.getPatientDocumentList();
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
								
							    }*/

								$scope.openImage = function(path)
					               { 
										$rootScope.startLoader();
										var href = $rootScope.currentHrefDoccument();
										$.fancybox.open({
											width: 600, // or whatever
											height: 320,
											href:href+path,
											type: 'iframe'
										});
										$rootScope.stopLoader();
//										$timeout(function() {
//											$rootScope.stopLoader();
//										}, 5000);

					         }
								
							 $scope.openInsuranaceImage = function(documentPath)
				             { 
								if(documentPath!==""){
									$rootScope.startLoader();
									var imageUrl = documentPath;
									var href = $rootScope.currentHrefDoccument();
									$.fancybox.open({
										width: 600, // or whatever
										height: 320,
										href:href+imageUrl,
										type: 'iframe'
									});
									$rootScope.stopLoader();
//									$timeout(function() {
//										$rootScope.stopLoader();
//									}, 5000);
								}else{
									growl.error("No image found",
											{
												title : 'Error!'
											});
								}									
				         }	
								
						  //document upload functionality for payment entitle ment end
				    ///Pinpad  
							 
							 
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
								

								$scope.objPosMachine={
										isSale:false, 
										isSMSpay : false,
										isBharatQr:false, 
										posMachineDeviceId:"",
										deviceName:"",
										machineConfigDetails:"",
										mobileNumberForPOS:""
								};
								
								$scope.getFlagSale=function(){
									if($scope.objPosMachine.isSale =="isSale"){
										$scope.objPosMachine.isSale = true;
										$scope.flagForSale=true;
										 $scope.flagForSMSpay=false;
										 $scope.flagForBharatQr=false;
										 $scope.objPosMachine.isBharatQr ="";
										 $scope.objPosMachine.isSMSpay=""
									}
									
								}
							 
							 $scope.getFlagBharatQr=function(){
								
									if($scope.objPosMachine.isBharatQr =="isBharatQr"){
										$scope.objPosMachine.isBharatQr = true;
										$scope.flagForBharatQr=true;
										 $scope.flagForSale=false;
										 $scope.flagForSMSpay=false;
										 $scope.objPosMachine.isSale ="";
										 $scope.objPosMachine.isSMSpay="";
									}
								}
							 $scope.getFlagSMSpay=function(){
									
									if($scope.objPosMachine.isSMSpay =="isSMSpay"){
										$scope.objPosMachine.isSMSpay = true;
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
//											{

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
														  "customerMobileNo":$scope.patientDetails.mobileNumber,
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
													  "customerMobileNo":$scope.patientDetails.mobileNumber,
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
													  "customerMobileNo":$scope.patientDetails.mobileNumber,
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
												patientId:$scope.patientDetails.patientId,
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
								 
								$scope.checkValidatePinPad = function()
								{
									if(($scope.registeredPinPadDevice != undefined || $scope.registeredPinPadDevice != null || $scope.registeredPinPadDevice != "") 
											&& $scope.registeredPinPadDevice == "POS Machine"){
										$scope.getResposeList=[];
										$scope.getGeneratedResponse=[];
										$rootScope.startLoaderPopup();
										var data = {
												patientId : $scope.patientDetails.patientId,
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												transactionAmount:$scope.paymentDetails.credAmount,
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
//														$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
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
											patientId: $scope.patientDetails.patientId,
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
							             
							             
//												/$scope.ipAddress+
												var urlprotocol = $location.protocol();
												//var finalUrl = "http://"+$scope.ipAddress+":9991/api/sale";
												var finalUrl = "http://127.0.0.1:9991/api/sale";
												var URI = finalUrl;
												BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													/*if (response.data.status == "success"){
														console.log("ree",response);
														console.log("reew",response.data);
														console.log("reer",response.data.rspCode);*/
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
																		$rootScope.stopLoader();
																	}
																	
																});
																 $scope.pinpadAmounFlag = false;
																growl.success(response.data.message,{
																	title : "SUCCESS!"
																});
																
															
															}
														else
															{
															 $scope.pinpadAmounFlag = false;
																growl.error(response.data.message,{
																	title : ERROR_MSG
																});
																return false;
															}
													 }else{
														 
														 $scope.pinpadAmounFlag = false;
														 growl.warning("Pin Pad Not Configure",{
																title : "Warning"
															});
															$rootScope.stopLoader();
															return false;
													 }
												});
												
												$rootScope.stopLoader();
											}
										else {
											 $scope.pinpadAmounFlag = false;
											growl.error("Someting went wrong..!!",{
												title : ERROR_MSG
											});
											$rootScope.stopLoader();
										}
										
									});
								}
								}
								
								//*********pinpad
								
/* *************************************Mpesa Integration start****************************************** */
								
								
								$scope.checkmpesaDetails=function(){ 
									$rootScope.startLoaderPopup();
									var data ={
											organizationId :$scope.organizationId,
											unitId : $scope.unitId,
											createdBy : $scope.userId,
											createdDate : moment(new Date()).format("DD-MM-YYYY"),
											updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
											updatedBy:$scope.userId,
											mpesaTxcode:$scope.transactionId,
											uhid: $scope.patientDetails.uhidNumber,
											mpesaSendermobile:$scope.mobileNo,
											mpesaAmount:$scope.mpesaAmount
									};
								
									if($scope.transactionId!=null && $scope.mobileNo!=null){
										 $scope.skipMpesaValidation = true;
 
									var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
									BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										if (response.data.status == "success")
											{
											
												$scope.mpesaAmount = response.data.object.mpesaAmount;
												$scope.transactionId = response.data.object.mpesaTxcode;
												$scope.addPaymentDetails();
												$rootScope.stopLoaderPopup();
											}
										else if(response.data.status == "error"){
											
											growl.error(response.data.message,{
												title : ERROR_MSG
											});
											$rootScope.stopLoaderPopup();
											$scope.skipMpesaValidation = false;
										}
										
									});
									}else{
										growl.error("Enter both transaction id and mobile number",{
											title : ERROR_MSG
										});
										$scope.skipMpesaValidation = false;
										$rootScope.stopLoaderPopup();
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
										
									}else if(($scope.mobileNumber == undefined || $scope.mobileNumber == null || $scope.mobileNumber == "") ||
											($scope.countryCallingCode == undefined || $scope.countryCallingCode == null || $scope.countryCallingCode == "") ){
										
										growl.warning("Please enter mobile code and mobile number. ",{
											title : "warning!!!"
										});
										$rootScope.stopLoaderPopup();
										return false;
										
									}else{
									$scope.skipMpesaValidation = true;
									var data ={
											organizationId :$scope.organizationId,
											unitId : $scope.unitId,
											createdBy : $scope.userId,
											createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											uhidNumber: $scope.patientDetails.uhidNumber,
											phoneNumber:parseInt($scope.countryCallingCode+""+$scope.mobileNumber),
											partyA:$scope.mobileNumber,
											amount:$scope.validateMpesaAmount
									};
								
									if($scope.mobileNumber!=null){
										$rootScope.startLoaderPopup();
									var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
									BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										if (response.data.status == "success")
											{
											
											growl.success(response.data.message,{
												title : SUCCESS_MSG
											});
											$scope.hitMpesaApi = true;
											
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
										$rootScope.stopLoaderPopup();
									});
									}else{
										growl.error("Enter mobile number",{
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
											uhidNumber: $scope.patientDetails.uhidNumber
											
									};
									$scope.skipMpesaValidation = true;
									$rootScope.startLoader();
									var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
									BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										
										if (response.data.status == "success")
											{
											$scope.mpesaAmount = response.data.object.amount;
											$scope.transactionId = response.data.object.transactionId;
											$scope.mobileNo = response.data.object.phoneNumber;
											if($scope.transactionId != null ){
												$scope.hitMpesaApi = false;
												$scope.addPaymentDetails();
												$scope.skipMpesaValidation = false;
												}
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
								
							$interval(refreshPage,1000000);
							function refreshPage()
							{
								if($scope.hitMpesaApi == true){
									$scope.validateSTKPushPayment();
								}

							}
															
							/************************************* Mpesa Integration END ******************************************/	
							/************************** E-transfer START ******************************************/
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
							 /************************** E-transfer END ******************************************/
							 
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
													    $scope.resend =false;
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
								
							
								/***********************************************Loylty card Tab END *****************************************/
								// kin details update and remove start
								$scope.getKinDetailsById = function(kinDetailsId){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId;
									BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success") {
													$scope.kinDetailsList[0]=(angular.copy(response.data.object));
													$scope.kinDetails.prefixId	= $scope.kinDetailsList[0].prefixId;
													
												  if($scope.kinDetailsList[0].identificationId!=null && $scope.kinDetailsList[0].identificationId!='' && $scope.kinDetailsList[0].isExpiry!=null && $scope.kinDetailsList[0].isExpiry!=undefined){
														$scope.kinDetailsList[0].identificationId = $scope.kinDetailsList[0].identificationId+'_'+$scope.kinDetailsList[0].isExpiry;
													}else{
														$scope.kinDetailsList[0].identificationId='';
													}
											
													$scope.getStateByCountry($scope.kinDetailsList[0].countryId);
													$scope.getDistrictByState($scope.kinDetailsList[0].stateId);
													$scope.getCityByDistrict($scope.kinDetailsList[0].districtId);
													$scope.getAreaByCity($scope.kinDetailsList[0].cityId);
													console.log($scope.kinDetailsList);
												}
											});
								}
								$scope.updateKinDetailsStatus = function(kinDetailsId){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId + STATUS + SLASH + 'I';
									BillingGenericService.serviceAction(METHOD_PUT, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success") {
											$scope.admissionRequestOrAdmissionObj.kinDetailsId="";
											$scope.getKinDetailsByPatientId();
										}
									});
								}
								// kin details update and remove end
								
								$scope.clearCoEmpDetails=function(){
									
									 if($scope.paymentEntitleMentCorporate[0].companyId == null || $scope.paymentEntitleMentCorporate[0].companyId == '' || $scope.paymentEntitleMentCorporate[0].companyId == undefined){
										 growl.warning("Please select company.",{
												title : "WARNING"
											});
											return; 
									 }
									 else if($scope.paymentEntitleMentCorporate[0].contractId == null || $scope.paymentEntitleMentCorporate[0].contractId == '' || $scope.paymentEntitleMentCorporate[0].contractId == undefined){
										 growl.warning("Please select contract.",{
												title : "WARNING"
											});
											return; 
									 }
									 else{
										 $scope.corEmpDependantList=[];
											$scope.showNameDetails=false;
											$scope.showHideAcceptRejectBtn=false;
											$scope.showHideOkBtn=false;
											$scope.initCoEmp();
											$scope.initEmpObj();
											$scope.getTypeOfCoEmp(3);
											angular.element('#validationEmployeeModel').modal('show');
									 }
									 
									
								}
								
								$scope.empObj.employeeNumber="";
								$scope.validateCorporateEmp=function(){
									$scope.name="";
									if($scope.corEmpObj.typeOfCoEmp==undefined || $scope.corEmpObj.typeOfCoEmp=="" || $scope.corEmpObj.typeOfCoEmp==null){
										growl.warning("Please type of verification",{
											title : WARNING_MSG
										});
										return;
									}
										if($scope.patientDetails.patientId==undefined || $scope.patientDetails.patientId=="" || $scope.patientDetails.patientId==null){
											growl.warning("Please select patient",{
												title : WARNING_MSG
											});
											return;
										}
										if($scope.corporateCompanyId==undefined || $scope.corporateCompanyId=="" || $scope.corporateCompanyId==null){
											growl.warning("Please select company name",{
												title : WARNING_MSG
											});
											return;
										}
									//for staff or employee 
										$scope.corEmpListObj=[];
										$scope.corEmpDependantList = [];
										$scope.displayMsg="";
									if($scope.corEmpObj.typeOfCoEmp=='1'){
										if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null){
											growl.warning("Please enter employee number",{
												title : WARNING_MSG
											});
											return;
										}
										var data={
												typeOfEmp:$scope.corEmpObj.typeOfCoEmp,
												employeeNumber:$scope.empObj.employeeNumber,
												organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												firstName:$scope.patientDetails.firstName,
												middleName:$scope.patientDetails.middleName,
												lastName:$scope.patientDetails.lastName,
												corporateId:$scope.corporateCompanyId
//												genderId:$scope.patientDetails.genderId,
//												birthDate:$scope.patientDetails.birthDate
										    }
										var URI = BASE_URL + ROOT_URL + "/api/global/verifyCorporateEmployee";
										BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success") {
												
													if(response.data.object!=null && response.data.listObject!=null){
														
														$scope.corEmpObj = response.data.object;
														$scope.corEmpListObj=response.data.listObject[0];
														
														$scope.name=$scope.corEmpListObj.firstName+" "+$scope.corEmpListObj.middleName+" "+$scope.corEmpListObj.lastName;
														if($scope.corEmpObj==1){
															
																$scope.color="green";
																$scope.enableDisabledValidated=false;											
																$scope.showHideAcceptRejectBtn=true;
																
															}else if($scope.corEmpObj==2){
																
																$scope.color="red";		
																$scope.showHideAcceptRejectBtn=true;
															}else{
															}
														$scope.showHideOkBtn=false;
													}else{
														$scope.color="red";	
														$scope.name="record not found.."
														$scope.showHideAcceptRejectBtn=false;
														$scope.showHideOkBtn=true;
													}
													$scope.showNameDetails=true;
													 console.log("$scope.corEmplist",$scope.corEmpObj);
//													 $scope.initCoEmp();
													 angular.element('#validationEmployeeModel').modal('show');
												}
											});
									
									}else if($scope.corEmpObj.typeOfCoEmp=='2'){
										if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null){
											growl.warning("Please enter employee number",{
												title : WARNING_MSG
											});
											return;
										}
										var data={
												typeOfEmp:$scope.corEmpObj.typeOfCoEmp,
												employeeNumber:$scope.empObj.employeeNumber,
												organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												corporateId:$scope.corporateCompanyId
//												
										    }
										var URI = BASE_URL + ROOT_URL + "/api/global/verifyCorporateEmployee";
										BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												$scope.corEmpDependantList = [];
												if (response.data.status == "success") {
												
													if(response.data.listObject!=null && response.data.listObject.length>0){
														$scope.corEmpDependantList = response.data.listObject;
														$scope.showNameDetails=true;
														angular.forEach($scope.corEmpDependantList, function (value, key) {
															if(value.employeeNumber != null && value.employeeNumber != undefined && value.employeeNumber != ""){
																$scope.name = value.empName;
																return;
															}
														});
//														angular.element('#validationEmployeeModel').modal('hide');
														
													}else{
														  $scope.color="red";
														  $scope.showNameDetails=true;
														  $scope.name="record not found.."
														  $scope.showHideAcceptRejectBtn=false;
														  $scope.showHideOkBtn=true;
													}
													
												}
//												$scope.initCoEmp();
											});
									}
									
									$scope.getEmpDependantDetailsByIndex=function(index){
										$scope.showNameDetails=false;
										     if($scope.patientDetails.firstName==$scope.corEmpDependantList[index].firstName) {
//										    	 $scope.name=$scope.corEmpDependantList[index].firstName+" "+$scope.corEmpDependantList[index].middleName+" "+$scope.corEmpDependantList[index].lastName;
										    	 if($scope.patientDetails.genderId==$scope.corEmpDependantList[index].genderId){
										    		       
//										    		        $scope.color="green";
															$scope.enableDisabledValidated=false;										
															$scope.showHideAcceptRejectBtn=true;
														
										    	 }else{
//										    		    $scope.color="red";
														$scope.enableDisabledValidated=false;										
														$scope.showHideAcceptRejectBtn=true;
													}
										     }else{
//										    	   $scope.color="red";
													$scope.enableDisabledValidated=false;										
													$scope.showHideAcceptRejectBtn=true;
												}
										 	$scope.showNameDetails=true;
//										     $scope.initCoEmp();
									}
									
								}
									$scope.verifyCorEmp=function(type){
										if(type=='A'){
											$scope.enableDisabledApply=true;	
											$scope.enableDisabledValidated=false;
										}else{
											$scope.enableDisabledApply=false;	
											$scope.enableDisabledValidated=true;
										}
										$scope.initCoEmp();
										$scope.initEmpObj();
									}
									
									$scope.closePopupValidate = function()
									{
										angular.element('#validationEmployeeModel').modal('hide');
									}
									
									$scope.saveMannualDetails=function(){
										if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null)
											{
												growl.warning("Please enter employee number.",{
													title : WARNING_MSG
												});
												return;
											
											}
										/*else if($scope.corEmpObj.note==undefined || $scope.corEmpObj.note=="" || $scope.corEmpObj.note==null){
											growl.warning("Please enter note",{
												title : WARNING_MSG
											});
											return;
										}*/
										if($scope.corEmpObj.employeeName==undefined || $scope.corEmpObj.employeeName=="" || $scope.corEmpObj.employeeName==null){
											growl.warning("Please enter Employee Name",{
												title : WARNING_MSG
											});
											return;
										}
										
										if($scope.corEmpObj.relation==undefined || $scope.corEmpObj.relation=="" || $scope.corEmpObj.relation==null){
											growl.warning("Please enter Relation",{
												title : WARNING_MSG
											});
											return;
										}
										$scope.enableDisabledApply=true;	
										$scope.enableDisabledValidated=false;
										angular.element('#validationEmployeeModel').modal('hide');
										$scope.employeeNumberManual = $scope.empObj.employeeNumber;
										$scope.employeeName = $scope.corEmpObj.employeeName;
										$scope.relation = $scope.corEmpObj.relation;
										$scope.medicalCardNo = $scope.corEmpObj.medicalCardNo;
										$scope.initEmpObj();
									}
									
									$scope.getTypeOfCoEmp=function(id){
										console.log("getTypeOfCoEmp id ",id);
										$scope.empObj.employeeNumber = '';
										$scope.corEmpObj.employeeName = '';
										$scope.corEmpObj.relation = '';
										$scope.corEmpObj.medicalCardNo = '';
										$scope.corEmpObj.note = '';
										  if(id=="1"){
											  $scope.corEmpObj.typeOfCoEmp="1";
											  $scope.dependTblShowHode=false;
											  $scope.noteShowHide=false;
											  $scope.showHidevalidate=true;
											  $scope.showHideSaveBtn=false; 
											  $scope.showNameDetails=false;
											  $scope.showHideOkBtn=false;
										  }
										  if(id=="2"){
											  $scope.corEmpObj.typeOfCoEmp="2";
											  $scope.dependTblShowHode=true;
											  $scope.showHidevalidate=true;
											  $scope.noteShowHide=false;
											  $scope.showHideSaveBtn=false; 
											  $scope.showNameDetails=false;
											  $scope.showHideOkBtn=false;
										  }
										  if(id=="3"){
											  $scope.corEmpObj.typeOfCoEmp="3";
											  $scope.dependTblShowHode=false;
											  $scope.noteShowHide=true; 
											  $scope.showHidevalidate=false; 
											  $scope.showHideSaveBtn=true; 
											  $scope.showNameDetails=false;
											  $scope.showHideAcceptRejectBtn=false;
											  $scope.showHideOkBtn=false;
										  }
									}
									$scope.getTypeOfCoEmp(3);
									/**************************************Ot Reservation Cancel START*****************************/
									
									$scope.currentIdx = '';
									$scope.getIndex = function(indx){
										
											$scope.currentIdx = indx;
											$("#confirmCancellation").modal("show");
											
									}
									$scope.patientId ="";
									$scope.cancelOTReservation = function(){
										try {
										
											$scope.patientId = $scope.otReservationList[$scope.currentIdx].patientId;
											
											if($scope.otReservationList[$scope.currentIdx].reasonId == ""){
												$scope.stoptLoader();
												growl.warning("Please Select Reason ", {
													title : 'warning!'
												});
												return false;
											}else if($scope.otReservationList[$scope.currentIdx].reasonNote == ""){
												growl.warning("Please Enter Reason Note", {
													title : 'warning!'
												});
												return false;
											}
											
											var data = {
													"proReqId":$scope.otReservationList[$scope.currentIdx].proReqId,
													"reasonId":$scope.otReservationList[$scope.currentIdx].reasonId,
													"reasonNote":$scope.otReservationList[$scope.currentIdx].reasonNote
												};
											$scope.startLoader();
											var URI = BASE_URL + ROOT_URL+"/api/otims/OTReservation/cancelOTReservation";
											console.log("URI", URI);
											GenericService
													.serviceAction("POST", URI, data)
													.then(
															function(response) {
																if (response.data.status == "success")
																{
																	$scope.stopLoader();
																		growl.success("Reservation Cancelled Successfully.",
																				{
																					title : 'Success!'
																				});
																		$("#otReservationDetails").modal("hide");
																		$scope.getOTReservationList($scope.patientId);
																	} else if(response.data.status == 'error')
																	{
																		$scope.stopLoader();
																		growl.error(response.data.message,
																				{
																					title : 'Error!'
																				});
																}
															});
											
										} catch (e) {
											console.log(e.message)
											$scope.stopLoader();
										}

									}
									
									$scope.cancelledSelectedItem = function(flag){
										
										angular.forEach($scope.otReservationList ,function(value,index){
											value.isSelected = false;
											
										});
										
										if(flag == 1){
											$scope.saveBedAllocation();
										}
										
									}
									
									$scope.checkForValidReservation = function(info){
										if(info.isSelected){
										if(info.scheduleDate<moment(new Date()).format('DD/MM/YYYY')){
											growl.warning("Procedure has been Expired,Please Reschedule Again",{
												title : WARNING_MSG
												});
										}
										}
									}
									/*********************************Ot Reservation cancel END*******************************/
									
									
									
									
								/*************************************Diagnosis Code Start***********************************/
									
									// to show a default empty row in medication
									// detail
									$scope.emptyMedicationRow = {
											itemId:"",
											favorite_status:false,
											medicationStartDate:new Date(),
											roaId:'',
											dose:'',
											doseUnitId:'',
											durationCount:'',
											durationUnit:'Day',
											instructionId:'',
											medicationEndDate:'',
											frequencyId:'',
											quantity:'',
											uomTypeId:'',
											conversions:'',
											strength :'',
											nonFormularyAdvice:'N',
											medicationName:'',
											itemName:'',
											genericName:'',
											isStat:false,
											isGeneric:'N',
											isInstruction:'N',
											checkFlagValidation:false,
											prescribedDosageUnitList:[]
											
										};
									
									$scope.medicationDetails = [$scope.emptyMedicationRow];
									
									$scope.addedServiceOrdersForDisplay = [{
										"serviceMasterId":""
									}];
									
									var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+$scope.organizationId;
									GenericService.serviceAction("GET", URI, {}).then(
									function(response) {
									$scope.doseList = [];
									$scope.doseList = response.data;

									});
									
									 // for Medication Advice
	                                $scope.openDatePickerMedicationAdvice = function(index) {

	                                	
	                                	
	                                	angular.forEach( $scope.medicationDetails,function(value,idx){
	                                		if(idx==index){
	                                			 $scope.datepickerMedicationAdvice.opened[idx] = true;
	                                		}else{
	                                			 $scope.datepickerMedicationAdvice.opened[idx] = false;
	                                		}
	                                	});
	                                 
	                                };

	                                $scope.datepickerMedicationAdvice = {
	                                    opened : [ false ],

	                                };
	                                $scope.dateOptionsMedicationAdvice = {
	                                		formatYear : 'yyyy',
	        								showWeeks : false,
	        								minDate:new Date(), //max 
	        							};
	                                
	                                
	                                $scope.openDatePickerMedicationAdviceEndDate = function(index) {

	                                    for (var i = $scope.medicationDetails.length - 1; i >= 0; i--) {
	                                        if (i === index) {

	                                            $scope.datepickerMedicationAdviceEndDate.opened[i] = true;
	                                        } else {
	                                            $scope.datepickerMedicationAdviceEndDate.opened[i] = false;
	                                        }
	                                    }
	                                };

	                                $scope.datepickerMedicationAdviceEndDate = {
	                                    opened : [ false ],

	                                };
	                                // End Travel
	                                $scope.dateOptionsMedicationAdvicesEndDate = {
	                                		formatYear : 'yyyy',
	        								showWeeks : false,
	        								minDate:new Date(), //max 
	        							};
	        							
	                                
	                                $scope.onStartDateChange = function(medication) {
	    								$scope.calculateEndDate(medication)
	    								if(medication.medicationStartDate  < $scope.currentDate){
	    									alert("date should not be leass than todays date");
	    								}
	    							}
	    							
	    							$scope.calculateEndDate = function(medication) {
	    								var currentMedication = medication
	    								var medicationStartDate = currentMedication.medicationStartDate
	    							
	    								var indexOfCurrentMedication = $scope.medicationDetails.indexOf(currentMedication)
	    								var durationCount = parseInt(currentMedication["durationCount"])
	    								var durationUnit = currentMedication["durationUnit"]
	    							
	    								    if(durationUnit == "Day") {
	    									var durationCount = parseInt(currentMedication["durationCount"]);
	    									}
	    								    else if(durationUnit == "Week") {
	    										var durationCount = parseInt(currentMedication["durationCount"])
	    										var durationCount = durationCount* 7;
	    									}
	    									else if(durationUnit == "Month") {
	    										var durationCount = parseInt(currentMedication["durationCount"])
	    										var durationCount = durationCount* 30;
	    									}
	    									else if(durationUnit == "Year") {
	    										var durationCount = parseInt(currentMedication["durationCount"])
	    										var months = durationCount * 12
	    										var durationCount = months* 30;
	    									}
	    								
	    								if(durationCount && durationUnit && medicationStartDate) {
	    									if(durationUnit == "Day") {
	    										var endDate = moment(medicationStartDate).add(durationCount-1, 'days').format('YYYY-MM-DD');
	    									}
	    									else if(durationUnit == "Week") {
	    										var days = durationCount * 7
	    										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
	    									}
	    									else if(durationUnit == "Month") {
	    										var days = durationCount * 30
	    										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
	    									}
	    									else if(durationUnit == "Year") {
	    										var days = durationCount * 365;
	    										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
	    									}
	    									currentMedication["medicationEndDate"] = endDate
	    									currentMedication["medicationEndDateFormatted"] = moment(endDate).toDate();
	    									
	    									$scope.medicationDetails[indexOfCurrentMedication] = currentMedication
	    									$scope.model=currentMedication.searchItemFre;
	    									$scope.label=currentMedication.searchItemFre;
	    									$scope.idx=indexOfCurrentMedication;
	    									//$scope.addFrequencyItemList($scope.storeItemOrder,$scope.model,$scope.label,$scope.idx);
	    									$scope.calculateFre($scope.idx);
	    								}else{
	    									$scope.medicationDetails[indexOfCurrentMedication].quantity="";
	    								}
	    							}
	    							
	    							$scope.addFrequencyItemList = function($item, $model, $label,idx){
	    					    		console.log($item);
	    					    		if($item != undefined){
	    					    		$scope.storeItemOrder=$item;
	    					    		$scope.freQua = 0;
	    					    		$scope.medicationDetails[idx].frequencyId = $item.frequency_id;
	    					    		$scope.freQua = $item.noOfAdministrationTime;
	    					    		}else{
	    					    			$scope.freQua = 0;
	    					    			$item={};
	    					    			$scope.medicationDetails[idx].frequencyId = $scope.medicationDetails[idx].frequencyId;
	    					        		$scope.freQua =$scope.medicationDetails[idx].noOfAdministrationTime;
	    					        		$item.noOfAdministrationTime =$scope.medicationDetails[idx].noOfAdministrationTime;
	    					    		}
	    					    		var dur = 0;
	    					    		if($scope.medicationDetails[idx].durationCount == '')
	    					    			{
	    					    			dur = 0;
	    					    			}
	    					    		else
	    					    			{
	    					    				
	    					    				 if($scope.medicationDetails[idx].durationUnit == "Day")
	    						    				{
	    					    					 dur = $scope.medicationDetails[idx].durationCount;
	    						    				 }
	    					    				 else if($scope.medicationDetails[idx].durationUnit == "Week")
	    							    			{
	    							    			  var  caldur = $scope.medicationDetails[idx].durationCount;
	    							    			    dur = caldur * 7;
	    							    			}
	    						    			else if($scope.medicationDetails[idx].durationUnit == "Month")
	    							    			{
	    							    			  var  caldur = $scope.medicationDetails[idx].durationCount;
	    							    			    dur = caldur * 30;
	    							    			}
	    								    		else if($scope.medicationDetails[idx].durationUnit == "Year")
	    							    			{
	    								    				
	    								    			var  caldur = $scope.medicationDetails[idx].durationCount;
	    							    				dur = caldur * 365;
	    							    		    }
	    					    			}
	    					    		
	    					    		$scope.medicationDetails[idx].quantity = $item.noOfAdministrationTime * dur;
	    					    		$scope.calculateFre(idx);
	    					    	}
	    					        
	    					        $scope.calculateFre = function(idx)
	    					        {
	    								var list=$.grep($scope.medicationDetails[idx].prescribedDosageUnitList, function (obj) {
	    							        return $scope.medicationDetails[idx].formulationCounterIndex === obj.counterIndex;
	    							    });
	    								var totalDuration="";
	    								if($scope.medicationDetails[idx].durationUnit=="Day"){
	    									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*1;
	    								}else if($scope.medicationDetails[idx].durationUnit=="Week"){
	    									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*7;
	    								}else if($scope.medicationDetails[idx].durationUnit=="Month"){
	    									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*30;
	    								}else{
	    									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*30*12;
	    								}
	    								if(list.length>0){	
	    									var presDoseObj=list[0];
	    									if(list.length>0  && presDoseObj.formulationFactorId==2){
	    										$scope.medicationDetails[idx].prescriptionDoseUnit=presDoseObj.formulationDosage;
	    									}else{
	    										$scope.medicationDetails[idx].prescriptionDoseUnit=presDoseObj.formulationDosageFromUnit;
	    									}
	    									$scope.medicationDetails[idx].prescribedDoseUnitId=presDoseObj.formulationDosageFromUnitId;
	    								}
	    								if(list.length>0  && $scope.medicationDetails[idx].prescribeDose!="" && $scope.medicationDetails[idx].prescribeDose!=undefined && $scope.medicationDetails[idx].prescribeDose!=null 
	    										&& $scope.medicationDetails[idx].formulationCounterIndex!="" && $scope.medicationDetails[idx].formulationCounterIndex!=undefined && $scope.medicationDetails[idx].formulationCounterIndex!=null){
	    									//for conversion factor
	    										if($scope.medicationDetails[idx].prescribedDoseUnitId==$scope.medicationDetails[idx].doseUnitId && $scope.medicationDetails[idx].dose!=0){
	    											$scope.medicationDetails[idx].quantity =Math.ceil(($scope.freQua* totalDuration*$scope.medicationDetails[idx].prescribeDose)/$scope.medicationDetails[idx].dose);
	    										}else{
	    											if(list.length>0 && $scope.medicationDetails[idx].dose!=0){	
	    											if(presDoseObj.formulationDosageToUnitId==$scope.medicationDetails[idx].doseUnitId){
	    												var convValue=Math.round(($scope.medicationDetails[idx].prescribeDose*presDoseObj.formulationDosageToConversionValue)/presDoseObj.formulationDosageFromConversionValue);
	    												 $scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua* totalDuration*convValue)/$scope.medicationDetails[idx].dose);
	    											}else{
	    											  $scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua* totalDuration*$scope.medicationDetails[idx].prescribeDose)/$scope.medicationDetails[idx].dose);
	    											}
	    										  }else{
	    											  $scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua
	    														* totalDuration)); 
	    										  }
	    											
	    										}
	    								}else{
	    									//using frequency factor 
	    									if(list.length>0  && presDoseObj.formulationFactorId==2){
	    										var splitString=(presDoseObj.formulationDosage).split(" ");
	    										var convValue=0,i;
	    										for(i=0;i<splitString.length;i++){
	    											var b=parseFloat(splitString[i]);
	    											if(Number.isNaN(b)==false){
	    												convValue=convValue+(math.number(math.fraction(splitString[i])));
	    											}
	    										}
	    										 $scope.medicationDetails[idx].quantity = Math.ceil($scope.freQua
	    													* totalDuration*(convValue==0?1:convValue)); 
	    									}else{
	    										$scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua * totalDuration));
	    									}
	    								}
	    							
	    					        }
	    					        
	    							$scope.onDurationCountChange = function(medication,idx) {
	    								$scope.calculateEndDate(medication);
	    								$scope.medicationExist=false;
	    								var AA={};
	    								var AA=medication;
	    								//$scope.calculateEndDate(medication)
	    								$scope.ListCheckDuplicate = [];
	    								$scope.ListCheckDuplicate.push(AA);
	    								console.log("entered data",$scope.ListCheckDuplicate);
	    								
	    								angular.forEach($scope.ListCheckDuplicate,function(value1,index){
	    									
	    										if($scope.medicationDetails.length>0){
	    		                                    $scope.medicationStartDateEnter = moment(value1.medicationStartDate).format('YYYY-MM-DD');
	    		                                    angular.forEach($scope.medicationDetails,function(value,index){
	    		                                        if(idx != index){
	    		                                        if ( value1.searchItem == value.searchItem
	    		                                                && moment(value1.medicationStartDate).format('YYYY-MM-DD') == moment(value.medicationStartDate).format('YYYY-MM-DD')
	    		//                                                && $scope.medicationObj.medicationEndDate == value.medicationEndDate
	    		                                                && value1.durationCount == value.durationCount
	    		                                                && value1.searchItemFre == value.searchItemFre
	    		                                                && value1.doseUnitId == value.doseUnitId
	    		                                                && value1.roaId == value.roaId
	    		                                                && value1.quantity == value.quantity) {
	    		                                            
	    		                                            $scope.medicationExist =true;
	    		                                        }
	    		                                       }
	    		                                    });
	    		                                }
	    							   });	
	    								 if($scope.medicationExist){
	    									 
	    	                                    growl.warning( "Medication Already Added",
	    	                                            {
	    	                                                title : 'Warning!'
	    	                                            });
	    	                                    $scope.medicationDetails[idx].durationCount="";
	    	                                    $scope.medicationDetails[idx].durationUnit="Day";
	    	                                    $scope.medicationDetails[idx].medicationStartDate=new Date();
	    	                                    $scope.medicationDetails[idx].medicationEndDateFormatted="";
	    	                                    $scope.medicationDetails[idx].quantity="";
	    	                                   $rootScope.stopLoader();
	    	                                  return false;
	    	                                 }
	    								 
	    							}
	    							
	    							$scope.searchMedicationFreItems = function(searchKeyword,ind){

							        	$scope.itemList=[];
							        	var unitId = $scope.unitId
										var orgId = $scope.organizationId
										
										var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequencyList" + "?unitId=" + 
											unitId + "&orgId=" + orgId + "&searchPar=" + searchKeyword
							    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							    			if(response.data.status == "success"){
							    				$scope.itemList = response.data.listObject;
							    			}
							    			if($scope.itemList.length > 0)
							    				{
							    				return $scope.itemList;
							    				}else{
							    					$scope.medicationDetails[ind].searchItemFre='';
							    					$scope.medicationDetails[ind].frequencyId='';
							    				}
							    			
							    		});
							    	}
	    							$scope.onDurationUnitChange = function(medication,idx) {
	    								$scope.medicationExist=false;
	    								var AA={};
	    								var AA=medication;
	    								$scope.calculateEndDate(medication)
	    								$scope.ListCheckDuplicate = [];
	    								$scope.ListCheckDuplicate.push(AA);
	    								console.log("entered data",$scope.ListCheckDuplicate);
	    								
	    								angular.forEach($scope.ListCheckDuplicate,function(value1,index){
	    									
	    										if($scope.medicationDetails.length>0){
	    		                                    $scope.medicationStartDateEnter = moment(value1.medicationStartDate).format('YYYY-MM-DD');
	    		                                    angular.forEach($scope.medicationDetails,function(value,index){
	    		                                        if(idx != index){
	    		                                        if ( value1.searchItem == value.searchItem
	    		                                                && moment(value1.medicationStartDate).format('YYYY-MM-DD') == moment(value.medicationStartDate).format('YYYY-MM-DD')
	    		//                                                && $scope.medicationObj.medicationEndDate == value.medicationEndDate
	    		                                                && value1.durationCount == value.durationCount
	    		                                                && value1.searchItemFre == value.searchItemFre
	    		                                                && value1.doseUnitId == value.doseUnitId
	    		                                                && value1.roaId == value.roaId
	    		                                                && value1.quantity == value.quantity) {
	    		                                            
	    		                                            $scope.medicationExist =true;
	    		                                        }
	    		                                        }
	    		                                    });
	    		                                }
	    							   });	.2
	    								 if($scope.medicationExist){
	    	                                    growl.warning( "Medication Already Added",
	    	                                            {
	    	                                                title : 'Warning!'
	    	                                            });
	    	                                    $scope.medicationDetails[idx].durationCount="";
	    	                                    $scope.medicationDetails[idx].durationUnit="Day";
	    	                                    $scope.medicationDetails[idx].medicationStartDate=new Date();
	    	                                    $scope.medicationDetails[idx].medicationEndDateFormatted="";
	    	                                    $scope.medicationDetails[idx].quantity="";
	    	                                   $rootScope.stopLoader();
	    	                                   $rootScope.stopLoader();
	    	                                  return false;
	    	                                 }
	    							}
									var commonObj = {
						    				isMedication : 'Y',
											organizationId : $scope.organizationId
										}

									var URI = BASE_URL + ROOT_URL
											+ GETMEDICATIONINSTRUCTIONS;
								
									BillingGenericService.serviceAction(METHOD_POST, URI,
													commonObj,
													NOTIFICATION_MSG_STATUS_FALSE).then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.medicationInstructions =[];
															$scope.medicationInstructions = response.data.listObject;
													});
									
									 $scope.removeTempServicesByIndex = function(index){
								    		$scope.addedServiceOrdersForDisplay.splice(index, 1);
								    	}
								        
									 
									 $scope.removeTempMedicationByIndex = function(index){
								    		$scope.medicationDetails.splice(index, 1);
								    	}
								        
								
									$scope.flagDigByGen=false;
									$scope.searchGroupList = function(keyword,index) {
										  $scope.DiagnosisByGenderList=[];
												
												var data = {
														organizationId : $scope.organizationId,
														diagnosisName : keyword,
														};
												
												console.log(data);
												$scope.varOr = "|";
												var URI = BASE_URL + ROOT_URL
														+ "/api/opd/getActiveEmergencyDiagnosis";
												
												return GenericService.serviceAction("POST", URI,data)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		 $scope.DiagnosisByGenderList = response.data.listObject;
																	if($scope.DiagnosisByGenderList.length == 0){
																		
																		$scope.flagDigByGen=true;
																		var newArr = {};
																		newArr.diagnosisName="Diagnosis not applicable";
																		newArr.diagnosisId='';
																		$scope.DiagnosisByGenderList.push(newArr);
																		$scope.varOr="";
																		return response.data.listObject= $scope.DiagnosisByGenderList;
																	} else{
																		$scope.flagDigByGen=false;
																	}
																		return response.data.listObject;
																		
																});

											}
									
									// Selected TypeHead on Diagnosis
									$scope.selectDignosisIdForDiagnosis = function(info, model,
											label,ind) {
										console.log(info);
										
										$scope.dataListDiagnosis[ind].diagnosisId = info.diagnosisId;
										$scope.diagonisIdforTemplate = $scope.dataListDiagnosis[ind].diagnosisId;
										$scope.dataListDiagnosis[ind].selectedOrderSet = [];
									}
									
								     $scope.allReadyExistDia=function(index){
											
											//saved data check
											$scope.isDiagnosisExists= true;
											angular.forEach($scope.dataListDiagnosis, function(value , key) {
											if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
												var chkStatus= $scope.patientDiagnosisList.filter(function(item)
												{ 
													return (item.diagnosisId==value.diagnosisId && 
															item.type==value.type && item.diagnosisType==value.diagnosisType &&
															item.admissionId == $scope.patientDetails.admissionId);
				    									})
			    	    								if(chkStatus.length>0){
			    	    									growl.warning("Diagnosis already exists.",
			    			    	    							{
			    			    	    							title : 'Warning!'
			    			    	    							});
			    	    								        	$scope.isDiagnosisExists= false;
			    	    								        	$scope.dataListDiagnosis[index].type='';
			    			    	    							$rootScope.stopLoader();
			    			    	    							return false;
			    	    								}
				    								}
											});
											
											
											
											//addeded data check
											
											$scope.list=[];
											$scope.list2=[];
											angular.forEach($scope.dataListDiagnosis, function(value , index1) {
												if(index1 == index){
													var newArr ={};
													newArr.diagnosisId=value.diagnosisId;
													newArr.type=value.type;
													$scope.list.push(newArr);
												}
											});
											angular.forEach($scope.dataListDiagnosis, function(value , index2) {
												if(index2 != index){
													var newArr ={};
													newArr.diagnosisId=value.diagnosisId;
													newArr.type=value.type;
													$scope.list2.push(newArr);
												}
											});
											
											angular.forEach($scope.list, function(value , key) {
												if($scope.dataListDiagnosis.length > 1){
												if($scope.dataListDiagnosis!=undefined && $scope.dataListDiagnosis.length>0){
													var chkStatus= $scope.list2.filter(function(item)
													{ 
														return (item.diagnosisId==value.diagnosisId && 
																item.type==value.type && item.diagnosisType==value.diagnosisType );
					    									})
				    	    								if(chkStatus.length>0){
				    	    									growl.warning("Diagnosis already added.",
				    			    	    							{
				    			    	    							title : 'Warning!'
				    			    	    							});
				    	    								        	$scope.isDiagnosisExists= false;
				    	    								        	$scope.dataListDiagnosis[index].type='';
				    			    	    							$rootScope.stopLoader();
				    			    	    							return false;
				    	    								}
					    								}
											     }
												});
											
											//once dai added as final dont again it as provisionl
											angular.forEach($scope.list, function(value , key) {
												if($scope.dataListDiagnosis.length > 1){
												if($scope.dataListDiagnosis!=undefined && $scope.dataListDiagnosis.length>0){
													var chkStatus= $scope.list2.filter(function(item)
													{ 
														return (item.diagnosisId==value.diagnosisId && 
																item.type==2 && item.diagnosisType == value.diagnosisType);
					    									})
				    	    								if(chkStatus.length>0){
				    	    									growl.warning("Diagnosis is already added as Final diagnosis.",
				    			    	    							{
				    			    	    							title : 'Warning!'
				    			    	    							});
				    	    								        	$scope.isDiagnosisExists= false;
				    	    								        	$scope.dataListDiagnosis[index].type='';
				    			    	    							$rootScope.stopLoader();
				    			    	    							return false;
				    	    								}
					    								}
											     }
												});
											//once dai save as final dont again it as provisionl
											if($scope.isDiagnosisExists){
											angular.forEach($scope.dataListDiagnosis, function(value , key) {
												if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
													var chkStatus= $scope.patientDiagnosisList.filter(function(item)
													{ 
														return (item.diagnosisId==value.diagnosisId && 
																item.type == 2 && item.diagnosisType==value.diagnosisType && 
																item.admissionId == $scope.patientDetails.admissionId);
					    									})
				    	    								if(chkStatus.length>0){
				    	    									growl.warning("Diagnosis is already saved as Final diagnosis.",
				    			    	    							{
				    			    	    							title : 'Warning!'
				    			    	    							});
				    	    									$scope.dataListDiagnosis[index].type='';
				    	    									$scope.isDiagnosisExists= false;
				    			    	    							$rootScope.stopLoader();
				    			    	    							return false;
				    	    								}
					    								}
												});
											}
										}
								     
										$scope.checkDiagnosisTypeExist=function(index){
											$scope.diaSec=index;
											//sneha
											$scope.Diaflag=false;
											$scope.DiaflagSave=true;
											angular.forEach($scope.dataListDiagnosis, function(value , index1) {
												if(index != index1){
												if(value.diagnosisType == 1){
													$scope.Diaflag=true;
												}
												}
											 });
											
											angular.forEach($scope.dataListDiagnosis, function(value , index1) {
											
												if(index == index1){
												if(value.diagnosisId == ""){
													$scope.DiaflagSave=false;
												}
												}
											 });
											
											
												angular.forEach($scope.dataListDiagnosis, function(value , key) 
												{
													if($scope.DiaflagSave){
													if($scope.Diaflag){
													if(value.diagnosisType == 1){
														$('#confirmDiagnosisTypePopup').modal();
													}
													}
													}
													
												});
											
										}
										
										$scope.setDiagTypeSecondary=function(){
											
											angular.forEach($scope.dataListDiagnosis,function(value,index){
												if($scope.diaSec == index){
													value.diagnosisType= 2;
												}
											});
											
										}
										
										//For multiple Diagnosis type save validtion
										$scope.checkDiagTypePrimary=function(){
											var counter=0;
											angular.forEach($scope.dataListDiagnosis, function(value , index1) {
												if($scope.diaSec != index1){
												value.diagnosisType=2;	
												}
											 });
											
										}
										//---------//
										
										$scope.checkDiagnosisTypeNotExist=function(index){
											//chetana
											var counter=0;
											if($scope.patientDiagnosisList.length > 0){
											angular.forEach($scope.patientDiagnosisList, function(value , key) 
											{
												if(value.diagnosisType == 1){
													counter++;
												}
												
											});
											}
											if(counter <= 0){
												growl.warning("Atleast one diagnosis should be primary.!!",
				    	    							{
				    	    							title : 'Warning!'
				    	    							});
											        	//$scope.isDiagnosisExists= false;
											        	$scope.dataListDiagnosis[index].diagnosisType='';
				    	    							$rootScope.stopLoader();
				    	    							return false;
											}
										}
							       	$scope.getDiagonosis = function(templateTypeId,obj,index) {
										$scope.icpcSystemId = 0;
										$scope.icpcId = 0;
										$scope.tempTemplateIndex=index;
										$scope.diagonisIdforTemplate=obj.diagnosisId;
										if ($scope.diagonisIdforTemplate == 0 ||$scope.diagonisIdforTemplate== 0 ) 
										{
											growl.warning(
													"Please select ICD 10 first...!!",
													{
														title : 'Warning!'
													});
											return;
										}
										$rootScope.startLoader();
										var data = {
											orgId : $scope.organizationId,
											unitId : $scope.unitId,
											doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
							        		specialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
							        		templateTypeId : templateTypeId,
							        		patientId : $scope.patientDetails.patientId,
							        		visitTypeId: $scope.patientDetails.visitTypeId,
							        		admissionId : $scope.patientDetails.admissionId,
											diagnosisId : $scope.diagonisIdforTemplate,
											mIcpcSystemId : $scope.icpcSystemId,
											mIcpcId :  $scope.diagonisIdforTemplate
										}

										angular.forEach($scope.templateListForSave,function(value,index){
											if(index==$scope.tempTemplateIndex){
		                            		value.diagonisIdforTemplate=$scope.diagonisIdforTemplate;
		                            		value.icpcSystemId=0;
		                            		value.icpcId=0;
											}
										});
										
										console.log('data', JSON.stringify(data));
										var URI = BASE_URL + ROOT_URL
												+ "/api/global/templates/diagnosis";
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															if (response.data.status == 'success') {
																$scope.templateList = response.data.listObject;
																if($scope.templateList.length==0){
																	growl.warning(
																			"No template mapped with selected ICD10 code..!",
																			{
																				title : 'Warning!'
																			});
																	return false;
																}
																$('#addtemplateModal')
																		.modal('show');
																$rootScope.stopLoader();
															} else if (response.data.status == 'error') {
																$rootScope.stopLoader();
																growl.warning(
																				"Template is already filled..!",
																				{
																					title : 'Warning!'
																				});
															}

														});
									}
							       	
							     // for added diagnosis
									$scope.selectedOrderSet= [];
									
									$scope.callOrderset=function(index){
										
										$scope.diagnosisIdForOrderSet=0;
										$scope.diagnosisIdForOrderSet =  $scope.dataListDiagnosis[index].diagnosisId;
										$scope.ordersetFlag=true;
										$scope.getOrderset(index);
									}
									
									$scope.addDiagnosis = function(index) 
									 {
										 
										 console.log("index add",index);
											var data = {
													diagnosisId : '',
													type :'',
													remark:'',
													diagnosisName:null
											};
											var len = $scope.dataListDiagnosis.length;
											
											
											angular.forEach($scope.dataListDiagnosis,function(value,index1){
												if(index == index1){
													if(value.diagnosisId == "" || value.type == "")
													{
														growl.warning("Please Enter Diagnosis.",
						    	    							{
						    	    							title : 'Warning!'
						    	    							});
														return false;
													}
													else{
														$scope.dataListDiagnosis.push(data);
													}
												}
											});
											
											//for diagnosis template
											var tempObj={};
				                        	 tempObj.templateObject={};
				                        	 tempObj.diagonisIdforTemplate=0;
				                        	 tempObj.icpcSystemId=0;
				                        	 tempObj.icpcId=0;
				                        	 $scope.templateListForSave.push(tempObj);
										};
										
										// for remove diagnosis
										$scope.removeDiagnosis = function(index) {
											console.log("removeIndex",index);
											var newDataList = [];
											var le = $scope.dataListDiagnosis.length;
											var i = 1;
											if(le != i){
											angular.forEach($scope.dataListDiagnosis, function(v) {
												if (index != i) {
													newDataList.push(v);
												}
												i++;
											});
											
											$scope.dataListDiagnosis = newDataList;
											}
											
											//for remove Diagnosis template
											var newTempList = [];
											var tempLen = $scope.templateListForSave.length;
											var j = 1;
											if (tempLen != j) {
												angular.forEach($scope.templateListForSave,
														function(v) {
															if (index != j) {
																newTempList.push(v);
															}
															j++;
														});
												$scope.templateListForSave = newTempList;
											}
										};
										
										$scope.orderSetList = [];
								       	$scope.diagnosisList = [];
								       	$scope.selectedOrderSet = [];
								       	$scope.orderSetOrderList = [];
//								       	$scope.ordersetFlag=false;
//								       	$scope.ordersetButton=false;
								       	$scope.addbutDis=false;
								       	$scope.getOrderset = function(index) 
								    	{
								       		
								       		if($scope.diagnosisIdForOrderSet==0)
								       			{
								       			growl.warning("Please select Diagnosis first..!",{
								    				title : 'Warning!'
								    			 });
								       			return;
								       			}
	
								       		
								       		$scope.orderSetIndex = index;
								    		$rootScope.startLoader();
								        	var data={
								        		  ordersetMasterId:[]	,
								        		  orgId : $scope.organizationId,
								        		  unitId : $scope.unitId,
								        		  doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
								        		  specialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
								        		  diagnosisId:$scope.diagnosisIdForOrderSet
								        		}
								        
								        	
								        	console.log('data',JSON.stringify(data));
								    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET;
								    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								    				{
								    			   if(response.data.status=='success')
								    			      {
								    				    $scope.orderSetList = response.data.listObject;
								    				    console.log("yes order set ",$scope.orderSetList);
								    				
								    				    if($scope.orderSetList!=null && $scope.orderSetList.length>0)
														{
															$scope.counts=0;
															$scope.countcheck =$scope.orderSetList.length;
															$scope.selectedlist = $scope.selectedOrderSet;
															angular.forEach($scope.selectedlist,function(value,index){
																
																angular.forEach($scope.orderSetList,function(value1,index1){
																	if(value == value1.ordersetMasterId)
																		{
																		$scope.counts++;
																		}
																	
																});
															});
															
															if($scope.addSelectedOrderSet == true){
																if($scope.counts == $scope.countcheck){
																	$scope.addbutDis=true;
																}else{
																	$scope.addbutDis=false;
																}
															}
															$( '#addOrdersetModal').modal('show');
														}
								    				    else{
								    				    	growl.warning(
																	"No order set mapped with this diagnosis , Please select diagnsis which have order set ..!",
																	{
																		title : 'Warning!'
																	});
								    				    	// object for Diagnosis List
															$scope.dataListDiagnosis = [ {
																diagnosisId : '',
																diagnosisType:'',
																type : '',
																remark : '',
																diagnosisName :null
															}];
								    				    	$rootScope.stopLoader();
															return;
								    				    }
								    				   	$rootScope.stopLoader();
								    			      }
								    			   else if(response.data.status=='error'){
								    				   $rootScope.stopLoader();
								    				   growl.warning("Something went wrong...",{
										    				title : 'Warning!'
										    			 });
								    			   }
								    				
								    		});
								    	}
								       	
								       	$scope.totalOrderSetids = [];
								        $scope.selectedOrderSet = [];
										$scope.fromOrderSetServices = false;
										$scope.addSelectedOrderSet = false;
										$scope.addSelectedOrderSettoAdd = function(index)
										{
											if ($scope.selectedOrderSet.length == 0) {
												growl.warning("Please select Order set..!", {
													title : 'Warning!'
												});
												return;
											}
											else{ 
												$scope.addSelectedOrderSet = true;
												 $scope.dataListDiagnosis[index].selectedOrderSet = $scope.selectedOrderSet;
											    }
											$scope.selectedOrderSet1=[];
											var dataDiagnosisList = $scope.dataListDiagnosis;
											for (var i = 0; i < dataDiagnosisList.length; i++) 
											{
												if($scope.dataListDiagnosis[i].selectedOrderSet!=null && $scope.dataListDiagnosis[i].selectedOrderSet.length>0)
													{
													var data = {
															ordersetMasterId : $scope.dataListDiagnosis[i].selectedOrderSet,
															orgId : $scope.organizationId,
															unitId : $scope.unitId,
															doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
															specialityId :  $scope.admissionRequestOrAdmissionObj.specialityId,
															diagnosisId : $scope.dataListDiagnosis[i].diagnosisId
														}
													$scope.selectedOrderSet1.push(data);
													}
											}
											
											if($scope.selectedOrderSet1.length>0)
												{
												$scope.addOrderSetOrders($scope.selectedOrderSet1);
												}
										}
										
										 var isServiceExistsForNotSelected;
											function checkServiceIdExistsForNotSelectedTemeporaryAdd(arr, val) {
												if(arr != null && arr.length >0){
												return arr.some(function(arr) {
															if(arr.serviceMasterId === val && arr.serviceRendered == 0 && arr.status == 'A')
																{
																	return true;
																}
															else
																{
																	return false;
																}
														});
												}else{
													 return false;
													}
											}
										
								       	$scope.addOrderSetOrders = function(orderSetObjList)
								       	{
								       		$scope.dublicateSerivceList = [];
								       		if($scope.selectedOrderSet.lengh==0)
							       			{
							       			growl.warning("Please select Order set..!",{
							    				title : 'Warning!'
							    			 });
							       			return;
							       			}
							    		$rootScope.startLoader();
							        	var data={
							        		  ordersetMasterId:$scope.selectedOrderSet1	,
							        		  orgId : $scope.organizationId,
							        		  unitId : $scope.unitId,
							        		  doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
							        		  specialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
							        		  diagnosisId:$scope.diagnosisIdForOrderSet
							        		}
							        	$scope.addedServiceOrdersForDisplay=[];
							        	
							    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET_ORDERS;
							    		console.log('dataget',JSON.stringify(data));
							    		console.log('URI',URI);
							    		BillingGenericService.serviceAction(METHOD_POST, URI, orderSetObjList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							    				{
							    			   if(response.data.status=='success')
							    			      {
							    				    $scope.orderSetOrderList = response.data.listObject;
							    				    console.log('$scope.orderSetOrderLis',$scope.orderSetOrderList);
							    				    angular.forEach($scope.orderSetOrderList, function(value, key) 
							    				    		{
							    				    	
							    				    	  angular.forEach(value.ordersetInvestigations, function(value, key) 
									    				    		{
							    				    		 
														        	var newArr = {};
														        	newArr.serviceId = value.serviceId;
																	newArr.specialityId = value.specialityId;
																	newArr.selectedForFuture = 'N';
																	newArr.specialityName = value.specialityName;
																	newArr.orderSetId = value.ordersetMasterId;
																	newArr.subSpecialityId = value.subSpecilaityId;
																	newArr.serviceStandardName = value.serviceStandardName;
																	newArr.selectedForFuture = $scope.selectedForFuture;
																	
																	newArr.specialityId = value.specialityId;
																	newArr.serviceSpecialityId = value.specialityId;
																	newArr.subSpecialityId = value.subSpecialityId;
																	
																	newArr.serviceMasterId = value.serviceId;
																	newArr.specialityName = value.specialityName;
																	newArr.serviceRenderingDeptId = value.subSpecialityId;
																	newArr.serviceSubSpecialityId = value.subSpecialityId;
																	
																	newArr.isAppointable = value.isAppointable;
																	newArr.createdBy = $scope.userId;
																	newArr.createdDate = new Date()
																			.getTime();
																	newArr.selfPayable = "0";
																	newArr.creditPayable = "0";
																	newArr.serviceRendered = "0";
																	newArr.doctorId = $scope.admissionRequestOrAdmissionObj.doctorId;
																	newArr.discountPercentage = 0;
																	newArr.discount = 0;
																	newArr.quantity = 1;
																	newArr.basePrice = 0;
																	newArr.basePriceOld = 0;
																	newArr.totalAmt = 0;
																	newArr.concession = 0;
																	newArr.payeeId = $scope.selfPayeeId;
																	newArr.apportionedPrice = null;
																	newArr.packageId = null;
																	newArr.packageTypeId = null;
																	newArr.packageEitherOrGroupId = null;
																	newArr.packageCapDetailsId = null;
																	newArr.isCapService = null;
																	newArr.status = 'A';
																	newArr.isRefDoctorShare = value.isRefDoctorShare;
																	newArr.packageEitherOrGroupId = null;
																	newArr.isPackageService = 'N';
																	newArr.discountDetailsList = [];
																	$scope.fromOrderSetServices = true;
																	newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
																	$scope.addedServiceOrdersForDisplay.push(newArr);
												
									    				    	    });
							    				    	  $scope.medicationObj={};
							    				    	  
							    				    	 angular.forEach(value.ordersetMedications, function(value, index1) 
									    				    		{
							    				    		  if($scope.medicationDetails!=null && $scope.medicationDetails.length==1 &&$scope.medicationDetails[0].itemId=='' )
						    				    			  {
						    				    			     $scope.medicationDetails = [];
						    				    			  }
									    				    		});
							    				    	  angular.forEach(value.ordersetMedications, function(value, index) 
									    				    {
							    				    		  if($scope.medicationDetails!=null)
							    				    			  {
							    				    			    $scope.medicationObj.itemId = value.itemMasterId;
							    				    			    $scope.medicationObj.doseUnitId = value.doseUnitId;
							    				    			    $scope.medicationObj.dose = value.dose;
							    				    			    $scope.medicationObj.roaId = value.routeId;
							    				    			    $scope.medicationObj.frequencyId = value.frequencyId;
							    				    			    $scope.medicationObj.formulationTypeId= value.formulationTypeId;
							    				    			    $scope.medicationObj.medicationStartDate = moment(new Date()).format('YYYY-MM-DD');
																	$scope.medicationObj.itemName = value.itemName;
																	$scope.medicationObj.roaDesc =value.roaDesc;
																	$scope.medicationObj.strengthDesc =value.strengthDesc;
																	$scope.medicationObj.frequencyDesc =value.frequencyDesc;
																	$scope.medicationObj.priorityId =1;
																	$scope.medicationObj.genericName = value.genericName;
																	$scope.medicationObj.searchGeneric = value.genericName;
																	$scope.medicationObj.genericId=value.genericId;
																	$scope.medicationObj.searchItem = value.itemName;
																	$scope.medicationObj.medicationName = value.itemName;
															        $scope.medicationObj.searchItemFre = value.frequencyDesc;
															        $scope.medicationObj.instructionId = value.instructionId;
															        $scope.medicationObj.orderSetId = value.ordersetMasterId;
																	  var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
																		$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" +
																		$scope.medicationObj.formulationTypeId+ "&genericId="
				                                                         + $scope.medicationObj.genericId+ "&patientId=" +
				                                                         $scope.patientDetails.patientId;
																	
																		GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
																		if(response.data.status == "success")
																		{
																			$scope.medicationDetails[index].roaList=[];
																			var idx = $scope.medicationDetails.findIndex(item => item.itemId == value.itemMasterId);
																			$scope.medicationDetails[index].roaList = response.data.listObject;
																		}});
																		

																    $scope.medicationObj.instructionDesc = value.instructionDesc;
																	$scope.medicationObj.medicationStartDate = new Date();
																	$scope.medicationObj.nonFormularyAdvice='N' ;
																	$scope.medicationObj.isGeneric='N' ;
																	$scope.medicationObj.isInstruction='N' ;
																	
																	$scope.medicationObj.frequencyId=value.frequencyId;
																	$scope.medicationObj.frequencyDesc=value.frequencyDesc;
																	$scope.medicationObj.noOfAdministrationTime=value.noOfAdministrationTime;
																	$scope.medicationObj.durationUnit='Day';
														    		$scope.freQua = 0;
														    		$scope.freQua = value.noOfAdministrationTime;
														    		var dur = 0;
														    		$scope.medicationObj.quantity = value.noOfAdministrationTime * dur;
														    		var checkFlagValidation = true;
																    $scope.medicationObj.checkFlagValidation = checkFlagValidation;
																    $scope.medicationObj.selected = 'Y';
																    
																	var found = $scope.medicationDetails.find(ar=>ar.itemId==value.itemMasterId);
																	   if(found==undefined)
																		   {
																		      $scope.medicationDetails.push($scope.medicationObj);
																		      console.log("testOrderSet" ,$scope.medicationDetails);
																		   }
																	   
																	   
																	$scope.medicationObj={};
							    				    			  }
							    				    		
									    				    	 });
							    				    	  
							    				  		
							    				    	  angular.forEach($scope.medicationDetails,function(value,idx){
							    				    		//get formuation dosage list
							    				    	$scope.prescribedDosageUnitListorderswet=[];
							    				  			var dataobj = {
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	formulationTypeId: value.formulationTypeId
															};
															value.prescribeDose="";
															var URIForPresc = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
															GenericService.serviceAction("POST", URIForPresc, dataobj).then(function(response) {
																		$rootScope.stopLoader();
																		$scope.counterFormulation=1;
																		$scope.medicationDetails[idx].formulationCounterIndex="";
																		$scope.prescribedDosageUnitListorderswet=[];
																		angular.forEach(response.data.listObject,function(value,index){
																			var unitObj={};
																			unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																			unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																			unitObj.formulationDosageToUnit=value.toConversionUnit;
																			unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																			unitObj.counterIndex=$scope.counterFormulation++;
																			unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																			unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																			unitObj.formulationFactorId=value.factorId;
																			unitObj.formulationDosage=value.dosageForm;
																			unitObj.formulationDosageMappId=value.formulationDosageMappId;
																			$scope.prescribedDosageUnitListorderswet.push(unitObj);
																		});
																		
																		angular.forEach(response.data.listObject,function(value,index){
																			var unitObj={};
																			if(value.factorId==1){
																				unitObj.formulationDosageFromUnit=value.toConversionUnit;
																				unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																				unitObj.formulationDosageToUnit=value.fromConversionUnit;
																				unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																				unitObj.counterIndex=$scope.counterFormulation++;
																				unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																				unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																				unitObj.formulationFactorId=value.factorId;
																				unitObj.formulationDosage=value.dosageForm;
																				unitObj.formulationDosageMappId=value.formulationDosageMappId;
																				$scope.prescribedDosageUnitListorderswet.push(unitObj);
																			}
																		});
																		value.prescribedDosageUnitList=$scope.prescribedDosageUnitListorderswet;
																		var list=[];
																		if(value.prescribedDosageUnitList.length>0 && value.prescribedDosageUnitList[0].formulationFactorId==1){
																			 list=$.grep(value.prescribedDosageUnitList, function (obj) {
																		        return value.doseUnitId === obj.formulationDosageFromUnitId;
																		    });
																			if(list.length>0){	
																				value.formulationCounterIndex=list[0].counterIndex;
																			}
																		}
															});
							    				    		  
							    				    	  })
							    				    	  
							    				    	    });
							    					
							    				    $('#addOrdersetModal').modal('hide');
							    				   	$rootScope.stopLoader();
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    				   growl.warning("Something went wrong...",{
									    				title : 'Warning!'
									    			 });
							    			   }
							    				
							    		});
								       	}
								       	
								       	
							       	/*************************************Diagnosis Code End***********************************/
								    /*MLC upload code*/
								       	
									    /***============================================Code Image Upload==============================================*/
									    /**PatientImages*/
									    $scope.patientImage1;
									    $scope.patientImage2;
									    $scope.patientImage3;
									    
									    
									    $scope.convertBase64ToImage = function (dataurl, filename) {
										    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
										        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
										    while(n--){
										        u8arr[n] = bstr.charCodeAt(n);
										    }
										    return new File([u8arr], filename, {type:mime});
										}
									    
									    $scope.uploadPatientImage = function(inputFile,patientId,orgId,unitId)
									    {
									    	    var file = inputFile;
										        var fd = new FormData();
										        fd.append('file', file );
												var URI1 = BASE_URL + ROOT_URL + PATIENT_IMAGE_UPLOAD+orgId+"/"+unitId+"/"+patientId;
												GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
												{	
													if (response.data.status == 'success')
													{
														$rootScope.stopLoader();
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
													}
												});
									    }
									    
									   
									    /***============================================Code Image Upload==============================================*/
									    
								       	$scope.cancelPatientImg = function(info){
											
											if($scope.patientDetails.patientId > 0){
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL+"/api/global/patient/deletePatientImage/"+$scope.patientDetails.patientId+"/"+$scope.organizationId;
													BillingGenericService
															.serviceAction(METHOD_GET,
																	URI, "",
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(
																	function(response) {
																		if (response.data.status == "success"){
																			$scope.patobj.filepreviewMLC = "";
																			$scope.patobj.patientImage1 = null;
																			angular.element("input[type='file']").val(null);
																			$rootScope.stopLoader();
																		}else{
																			$rootScope.stopLoader();
																		}
																	});
											}else{
												$scope.patobj.filepreviewMLC = "";
												$scope.patobj.patientImage1 = null;
												angular.element("input[type='file']").val(null);
											}
										}
								       	
								        $scope.uploadPatientMLCImage = function()
									    {
										  $rootScope.startLoader();
												var promise = $scope.uploadEncounterDoc($scope.admissionList);
												promise.then(function(response) 
														{
														  if(response.data.status == "success")
															  {
//																   growl.success("Document Uploaded Successfully.",
//																			{
//																				title : 'Success!'
//																			});
																   $rootScope.stopLoader();
																 }
													    });
													
										
									    }
										$scope.uploadEncounterDoc = function(Obj)
										{
											 var fd = new FormData();
										        fd.append('encounterId', 0);
										        fd.append('visitTypeId', Obj.visitTypeId);
										        fd.append('admissionId', Obj.admissionId);
										        fd.append('patientId',Obj.patientId );
										        fd.append('documentTypeId', 35);
										        fd.append('createdBy',Obj.createdBy );
										        fd.append('updatedBy', Obj.updatedBy);
										        fd.append('unitId', Obj.unitId);
										        fd.append('organisationId', Obj.organizationId);
										        fd.append('uploadDoccumentDate', moment(Obj.createdDate).format('DD-MM-YYYY HH:mm:ss'));
										        fd.append('file',$scope.patobj.patientImage1);
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
							
										$scope.cancelPaymentReqModal = function(){
											angular.element('#paymentReqModal').modal('hide');
										}
										
										$scope.clearMpesaFields = function(){
											$scope.skipMpesaValidation = false;
											$scope.mpesaAmount = '';
											$scope.transactionId = null;
											$scope.mobileNo = null;
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
											$scope.calculateDueAmount();
											$scope.calculateTotalAmount();
										}
										
										

										$scope.sendPaymentLink = function(){
											
											if($scope.razorMobileNo == undefined || $scope.razorMobileNo == null || $scope.razorMobileNo =='')
												{
												growl.error("Please enter mobile Number",
														{
															title : 'Error!'
														});
												    return false;
												 }
											else if($scope.razorAmount == undefined || $scope.razorAmount == null || $scope.razorAmount =='')
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
														  "mobileNo":$scope.razorMobileNo,
														  "razorAmount":$scope.razorAmount,
														  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",//"https://0c61-2409-4042-e0d-8cbe-b4eb-d586-7c08-28ee.ngrok.io/HIS-WAR/getRazerPay",
														  "email":$scope.patientDetails.email,
														  "name":$scope.patientDetails.patientName,
														  
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
										
										
										$scope.razorUniqueId = null;
										$scope.razorPaidAmount = null;
										$scope.validateRazerPay = function(){
											

											if($scope.razorMobileNo == undefined || $scope.razorMobileNo == null || $scope.razorMobileNo =='')
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
														  "mobileNo":$scope.razorMobileNo,
														  
												}
												var URI = BASE_URL + ROOT_URL + "/validateRazorPayPaymentLink";
												BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													if (response.data.status == "success")
													{
															$rootScope.stopLoader();
															$scope.razorPaidAmount = response.data.object.razorAmount;
															$scope.razorUniqueId = response.data.object.uniqueId;
															$scope.addPaymentDetails();
															
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
										
												if($scope.razorMobileNo == undefined || $scope.razorMobileNo == null || $scope.razorMobileNo =='')
												{
												growl.error("Please enter mobile Number",
														{
															title : 'Error!'
														});
												    return false;
												 }
												else if($scope.razorAmount == undefined || $scope.razorAmount == null || $scope.razorAmount =='')
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
														  "mobileNo":$scope.razorMobileNo,
														  "razorAmount":$scope.razorAmount,
														  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",
														  "email":$scope.patientDetails.email,
														  "name":$scope.patientDetails.patientName,
														  
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
											
											if($scope.razorMobileNo == undefined || $scope.razorMobileNo == null || $scope.razorMobileNo =='')
												{
												growl.error("Please enter mobile Number",
														{
															title : 'Error!'
														});
												    return false;
												 }
											else if($scope.razorAmount == undefined || $scope.razorAmount == null || $scope.razorAmount =='')
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
														  "mobileNo":$scope.razorMobileNo,
														  "requestAmount":$scope.razorAmount,
														  "emailId":$scope.patientDetails.email,
														  "name":$scope.patientDetails.patientName,
														  "patientId": $scope.patientDetails.patientId, 
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
										
										
										$scope.PayByRazorPaymentCCAvenue = function(){
											
											if($scope.razorMobileNo == undefined || $scope.razorMobileNo == null || $scope.razorMobileNo =='')
												{
												growl.error("Please enter mobile Number",
														{
															title : 'Error!'
														});
												    return false;
												 }
											else if($scope.razorAmount == undefined || $scope.razorAmount == null || $scope.razorAmount =='')
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
														  "mobileNo":$scope.razorMobileNo,
														  "requestAmount":$scope.razorAmount,
														  "emailId":$scope.patientDetails.email,
														  "name":$scope.patientDetails.patientName,
														  "patientId": $scope.patientDetails.patientId, 
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
										
										
									$scope.validateRazerPayCCAvenue = function(){
											
											if($scope.razorMobileNo == undefined || $scope.razorMobileNo == null || $scope.razorMobileNo =='')
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
														  "mobileNo":$scope.razorMobileNo,
														  "patientId": $scope.patientDetails.patientId, 
														  
												}
												var URI = BASE_URL + ROOT_URL + "/validateCCAvenuePayment";
												BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													if (response.data.status == "success")
													{
															$rootScope.stopLoader();
															$scope.razorPaidAmount = response.data.object.paidAmount;
															$scope.razorUniqueId = response.data.object.orderNo;
															$scope.addPaymentDetails();
															
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

										
									$scope.updateClaimNumber=function(info){
										
										$rootScope.startLoaderPopup();
										if(info.patientSponsorDetailsId == null){
											growl.warning("The record first you have to save then update !!!",
													{
														title : 'Warning!'
													});
											$rootScope.stopLoaderPopup();
											return false;
										}
										var data ={
												
												claimNumber:info.claimNumber,
												patientSponsorDetailsId:info.patientSponsorDetailsId,
										};
									if(info.claimNumber != null && info.claimNumber !="" && info.claimNumber != undefined){
										var URI = BASE_URL + ROOT_URL + "/api/opd/updatePatientSponserDetailsClaimNumber";
										GenericService.serviceAction("POST",URI,data).then(function(response){
											if (response.data.status == 'success')
											{
												$rootScope.stopLoaderPopup();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoaderPopup();
												growl.warning(response.data.message,
														{
															title : 'Warning!'
														});
											}
										});
										
									}
									else{
										$rootScope.stopLoaderPopup();
										 growl.warning(" Please enter claim number. ",
													{
														title : 'warning!'
													});
									}
										
									}

										
						} ]);
