/**
 * @author Vivek
 * Controller for Encounter Creation
 */
angular.module("myApp").controller("EncounterController", [ '$scope','$rootScope','BillingGenericService','GenericService','GenericServiceParamHeader','GenericServiceFileUpload','$sessionStorage','PagerService','promiseFactory','$cookies','$state','promiseFactoryWithObject','growl','$stateParams','$http','reportService','paymentCollFactory','$location','loyaltiCardCallFactory','servicesFactory','$interval','$window','$timeout',
	function($scope,$rootScope,BillingGenericService,GenericService,GenericServiceParamHeader,GenericServiceFileUpload,$sessionStorage,PagerService,promiseFactory,$cookies,$state,promiseFactoryWithObject,growl,$stateParams,$http,reportService,paymentCollFactory,$location,loyaltiCardCallFactory,servicesFactory,$interval,$window,$timeout){
	
	
	
	$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	
	
	$scope.focusdata1 = function(event,item){
	        var idfocus_name = 'focus_name';
	        document.getElementById(idfocus_name).focus();
	     
	}
	
	$scope.focusdata2 = function(event,item){
		
		if($scope.patientObj.mobileNumber.length == 10){
			  var idcode_focus = 'code_focus';
		        document.getElementById(idcode_focus).focus();
		}
	  
	}

	$scope.checkFocus = function(data){
                 document.getElementById("genderId").focus();
          }
	
	
$scope.focusdata3 = function(event,item){
		
		if($scope.ageDay.length == 2){
			  var idmobile_no = 'mobile_no';
		        document.getElementById(idmobile_no).focus();
		}
	  
	}

//	$scope.focusdata3 = function(event,item){
//		 var idmobile_no = 'mobile_no';
//	        document.getElementById(idmobile_no).focus(); 
//	 }
	
	$scope.focusdata4 = function(event,item){
		 var iddoctor_name = 'doctor_name';
	        document.getElementById(iddoctor_name).focus(); 
	 }
	
	
	$scope.focusdata5 = function(event,item){
		 var idtime_slot = 'time_slot';
	        document.getElementById(idtime_slot).focus(); 
	        
	        
	 }
	
	
	
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
	 if(cookieObjectAppConfig == undefined){
		 $state.go('applicationConfiguration');
	  return;
	 }
	 else
	  {
	   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
	   
	  }
	 $scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
	 $scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
	 $scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
	 $scope.emailValidationRequired = cookieObjectAppConfig.emailValidationRequired;
	 $scope.mobileValidationRequired = cookieObjectAppConfig.mobileValidationRequired;
	 if (cookieObjectAppConfigSecond != undefined) {
	 $scope.exceededBillAmountSettlement=cookieObjectAppConfigSecond.exceededBillAmountSettlement;
	 }
	$scope.fileObj = {};
	$scope.fileObj.file = null;
	$scope.patientRegistrationform="";
	
	$scope.storeEmailId = null;
	$scope.emailOtp = null;
    $scope.entredOpt = "";
	$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
	$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
	$scope.radiologySpecialtyId = cookieObjectAppConfig.radiologySpecialityId;
	$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
	$scope.casualtySpecialityId = cookieObjectAppConfig.casualtySpecialityId;
	$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
	//alert($scope.isAppointmentRequired);
	$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
	$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
	$scope.pinpadAmounFlag = false;
	$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
	$scope.skipMpesaValidation = false;
	$scope.isFreeVisitReq = cookieObjectAppConfig.isFreeVisitReq;
	$scope.isHospitalVisitReq = cookieObjectAppConfig.isHospitalVisitReq;
	$scope.isReviewAppReq = cookieObjectAppConfig.isReviewAppReq;
	$scope.isLabReq = cookieObjectAppConfig.isLabReq;
	$scope.isEHCReq =  cookieObjectAppConfig.isEHCReq;
	$scope.isRadiologyReq =  cookieObjectAppConfig.isRadiologyReq;
	$scope.isDocConsultReq =  cookieObjectAppConfig.isDocConsultReq;
	$scope.unitCountryId = cookieObject.unitCountryId;
	$scope.minAge = cookieObjectAppConfig.minAge;
	$scope.dateFormat = cookieObjectAppConfig.dateFormat;
	$scope.billSettlement = cookieObject.billSettlement;
	$scope.maxAge = cookieObjectAppConfig.maxAge;
	$scope.defaultPatientCategoryId=cookieObjectAppConfig.defaultPatientCategoryId;
	$scope.hitMpesaApi = false;
	$scope.isTeleconsultationAppointment=0;
	$scope.diableSettle = false;
	$rootScope.getIdentificationTypeList();
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
	$scope.encounterTypeList = [];
	$scope.DepositObj = {};
	$scope.DepositObj.userEnterDepositAmount = 0;
	
    $scope.validatTextFieldFlag = true;
    $scope.search='';
    $scope.patientSerachHideAndShow = true;
	var labConfig = JSON.parse(localStorage.getItem('labConfig'));
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	$scope.labSpecialityId = labConfig==undefined ? 0 : labConfig.labSpecialityId;

	 //addded by dinesh for getting default config start
	$scope.unitCountryId = cookieObject.unitCountryId;
	$scope.unitStateId = cookieObject.unitStateId;
	$scope.unitDistrictId = cookieObject.unitDistrictId;
	$scope.unitCityId = cookieObject.unitCityId;
	$scope.unitAreaId = cookieObject.unitAreaId;
	$scope.unitZipCode = cookieObject.unitZipCode;
	
    $scope.excessAmount = parseFloat(0).toFixed(2);   
    $scope.isReviFlg = true;
    
    $scope.tabColl = 1;
    
    $scope.cashSettlementFlag = 'N';
    
    $scope.areaNotExist = 'N';
    $scope.areaNotExistPermanent = 'N';
    
    $scope.sameAsLocal = 'Y';
    $scope.numberToDisplay = 5;
	
	 //addded by dinesh for getting default config end
	
	$scope.dateOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
		};
	

	$scope.dateOptionsDOB = {
            formatYear : 'yyyy',
            showWeeks : false,
            maxDate : new Date()
        };

	$scope.dateOptionsSearch = {
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : new Date()
		};
	

	
	$scope.dateOptionsTrancs = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
		};
	
	$scope.cardExpiryOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date(),
		};
	
	$scope.openDatePicker = function() {
	    $scope.datepicker=new Date();
		$scope.datepicker.opened = true;
	};
	

	$scope.datepicker = {
		opened : false
	};
	
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
	
	$scope.dateOptionsExpiry1 = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
		};
		$scope.openDatePickerExpiry1 = function() {
			$scope.datepickerExpiry1.opened = true;
		};

		$scope.datepickerExpiry1 = {
			opened : false
		};
		$scope
		
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
	
	//for New/Registerd
	$scope.openDatePickerSearch = function() {
		$scope.datepickerSearch.opened = true;
	};

	$scope.datepickerSearch = {
		opened : false
	};
	
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
$scope.openDatePickerContractEndDate = function(index) {

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
	$scope.alreadyPatient = 0;
	$scope.settleBillFlag = "N";
	$scope.billTabHide = false;
	$scope.transferToActiveDeposit = "N";
	$scope.page = "billList";
	
	$scope.billDisabled = false;
	
	$scope.finalOtReservationList = [];
    $scope.priorityList = [{
        id:1,
        value:1
    }]

	
	/*$scope.insuApplyBtn = true;
	$scope.insuUpdateBtn = false;
	
	$scope.corpApplyBtn = true;
	$scope.corpUpdateBtn = false;*/
	
    
	/**================================COUNTER CODE START======================================*/
	$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
	$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
	if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
		{
		     var flag = $scope.getCounterDetails();
		     if(flag)
		    	 {
		    	 //$rootScope.counterMstObj = $cookies.getObject('counterMstObj');
		    	 //$rootScope.counterId= $rootScope.counterId!=""?$rootScope.counterId:"";
		    	 }
		     else
		     {
		    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
		     }
		}else{
			$rootScope.counterId = 0;
		}
	/**================================COUNTER CODE END======================================*/
	$scope.diableDropDown=false;
	$scope.enableDisabled  = false;
	$scope.hideShowByType = true;
	$scope.hideShowByTypeForEHC = true;
	$scope.hideShowByTypeForPackage = false;
	$rootScope.loginpage = true;
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.sexMasterList=[];
	$scope.doctorList=[];
	$scope.associatedCompanyList = [];
	$scope.userId = cookieObject.userId;
	$scope.mpesaRequired = cookieObject.mpesaRequired;
	$scope.nextOfKinId = "";
	$scope.slotId = '';
	$scope.EHCpackageId = 0;
	$scope.packageBasePrice = 0;
	$scope.encounterDetailsContractList = [];
	$scope.patientObj = {

			prefixId : '',
			firstName : '',
			middleName : '',
			lastName : '',
			genderId : '',
			countryCallingCode : '',
			email : '',
			nationalityId : '',
			raceId : '',
			maritalStatusId : '',
			mobileNumber : '',
			dob : '',
			identificationTypeId : '',
			identificationNumber : '',
			isVip : '',
			organizationId : $scope.organizationId,
			unitId : $scope.unitId,
			updatedBy : $scope.userId,
			createdBy : $scope.userId,
			status : 'A',
			patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
			aliseName:'',
			identificationExpiryDate:'',
			occupationId:'',
			registrationTypeId:1,
			previousId:'',
			patientId:0,
			patientDetailsId:'',
			whatsappCallingCode : '',
			whatsappNo:'',
			skypeId:'',
			address : '',
			countryId : '',
			stateId : '',
			districtId : '',
			cityId :'',
			areaId : '',
			zipCode : '',
			phoneCode : '',
			phoneNumber : '',
			permanentAddress:'',
			permanentCountryId:'',
			permanentStateId:'',
			permanentDistrictId:'',
			permanentCityId:'',
			permanentAreaId:'',
			permanentZipCode:'',
			permanentPhoneCode:'',
			permanentPhoneNumber:'',
			fileSequenceNumber:'',
			bloodGroup:'',
			isAlreadyExist:'N'
		};
	 $scope.patientAdmittedFlag = false;
		$scope.ageDay = 0;
		$scope.ageMonth = 0;
		$scope.ageYear = 0;
	$scope.registrationTypeId = 0;
	$scope.enableDisabledApply=false;	
	$scope.enableDisabledValidated=false;
	$scope.deptWiseAppointment = "N";
	$scope.documentUploadPopUp  = true;
	$scope.paymentEntitlementShowFlag  = false;
	$scope.selectedOrderDetailIdsList = [];
	$scope.isRedirectAppointment=false;
	$scope.initCoEmp=function(){
		$scope.corEmpObj={
				typeOfCoEmp:'3',
//				employeeNumber:"",
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
	$rootScope.getDefaultRegistrationValidationDetails();
	
	//get calling code by using country id start
	$scope.getCallingCode=function(data){
		var URI = BASE_URL + ROOT_URL
		+ GETSINGLECOUNTRY;
		GenericService.serviceAction("POST", URI, data).then(
				function(response) {
					if (response.data.status == "success" && response.data.listObject.length >0) {
						$scope.countryCallingCode = response.data.listObject[0].countryCallingCode;
						
						$scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
						$scope.patientObj.whatsappCallingCode = response.data.listObject[0].countryCallingCode;
						
						//if(type=="PA"){
							$scope.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
						//}else{
							$scope.phoneCode = response.data.listObject[0].countryCallingCode;
						//}
					} else {
						//alert("Error!!");
					}
				});
	}
	
	$scope.getNationalityByContryId=function(data){
		data.organizationId = $scope.organizationId;
		var URI = BASE_URL + ROOT_URL
		+ "/adt/getNationalityByCountryId";
		GenericService.serviceAction("POST", URI, data).then(
				function(response) {
					if (response.data.status == "success" && response.data.listObject.length > 0) {
						console.log("nationality List ",response.data.listObject[0]);
						$scope.patientObj.nationalityId = response.data.listObject[0].nationalityId;

					} else {
//						alert("Error!!");
					}
				});
	}
	
	
	// get state by country ID
	/*$scope.getStateByCountry = function(id) {
		var data = {
			countryId : id
		};
		var dataorg = {
				countryId : id,
				organizationId :$scope.organizationId,
			};
		$scope.getCallingCode(dataorg);
		$scope.getNationalityByContryId(data);
		var URI = BASE_URL + ROOT_URL + GETSTATELISTBYCOUNTRYID;
		BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							$scope.stateList = [];
							if (response.data.status == "success") {
								$scope.stateList = response.data.listObject;
							}
		});
	}*/
	// for get state by country ID
	$scope.getStateByCountry = function(id,type) {
		$scope.startLoader();
		var data = {
			countryId : id
		};								
		
		var dataorg = {
				countryId : id,
				organizationId : $scope.organizationId
			};	
		
		$scope.getCallingCode(dataorg,type);
	
		$scope.getNationalityByContryId(data);
		var URI = BASE_URL + ROOT_URL
				+ GETSTATELISTBYCOUNTRYID;
		BillingGenericService
		.serviceAction(METHOD_POST, URI, data,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(
				function(response) {
					//$scope.stateList = [];
					if (response.data.status == "success"){
						if(type == 'P')
							{
							$scope.stateList = [];
							$scope.stateList = response.data.listObject;
							$scope.patientObj.stateId="";
							$scope.patientObj.districtId="";
							$scope.patientObj.cityId="";
							$scope.patientObj.areaId="";
							$scope.patientObj.zipCode="";
							/*if($scope.sameAsLocal == 'Y'){
								$scope.patientObj.permanentCountryId = id;
							}*/
							}
						else if(type == 'PA')
							{
							$scope.permanentStateList = [];
							$scope.permanentStateList = response.data.listObject;
							
							$scope.patientObj.permanentStateId="";
							$scope.patientObj.permanentDistrictId="";
							$scope.patientObj.permanentCityId="";
							$scope.patientObj.permanentAreaId="";
							$scope.patientObj.permanentZipCode="";
							}
						else if(type == 'L')
							{
							$scope.stateList = [];
							$scope.stateList = response.data.listObject;
							
							}else if(type == 'LTOP')
							{
								$scope.permanentStateList = [];
								$scope.permanentStateList = response.data.listObject;
								
							}else
							{
							$scope.CompanyStateList = [];
							$scope.CompanyStateList = response.data.listObject;
							}
					}
					$scope.stopLoader();
				});

	}

	$scope.getDistrictByState = function(id,type) {
		$scope.startLoader();
		var data = {
			stateId : id
		};
		var URI = BASE_URL + ROOT_URL
				+ GETDISTRICTLISTBYSTATEID;
		BillingGenericService
		.serviceAction(METHOD_POST, URI, data,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(
				function(response) {
					
					if (response.data.status == "success")
					{
						//$scope.districtList = response.data.listObject;
						if(type == 'P')
						{
						$scope.districtList = [];
						$scope.districtList = response.data.listObject;
						$scope.patientObj.districtId="";
						$scope.patientObj.cityId="";
						$scope.patientObj.areaId="";
						$scope.patientObj.zipCode="";
						}
						else if(type == 'PA')
						{
						$scope.permanentDistrictList = [];
						$scope.permanentDistrictList = response.data.listObject;
						
						$scope.patientObj.permanentDistrictId="";
						$scope.patientObj.permanentCityId="";
						$scope.patientObj.permanentAreaId="";
						$scope.patientObj.permanentZipCode="";
						}else if(type == 'L')
						{
							$scope.districtList = [];
							$scope.districtList = response.data.listObject;
							
						}
					else  if(type == 'LTOP')
					{
						$scope.permanentDistrictList = [];
						$scope.permanentDistrictList = response.data.listObject;
						
					}else
						{
						$scope.companyDistrictList = [];
						$scope.companyDistrictList = response.data.listObject;
						}
					}
					$scope.stopLoader();
				});
		
	}

	$scope.getCityByDistrict = function(id,type) {
		$scope.startLoader();
		var data = {
			districtId : id
		};
		var URI = BASE_URL + ROOT_URL
				+ GETCITYLISTBYDISTRICTID;
		BillingGenericService
		.serviceAction(METHOD_POST, URI, data,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(
				function(response) {
					
					if (response.data.status == "success")
						//$scope.cityList = response.data.listObject;
					{
						//$scope.districtList = response.data.listObject;
						if(type == 'P')
						{
						 $scope.cityList = [];
						 $scope.cityList = response.data.listObject;
						 $scope.patientObj.cityId="";
						$scope.patientObj.areaId="";
						$scope.patientObj.zipCode="";
						}
						else if(type == 'PA')
						{
						$scope.permanentCityList = [];
						$scope.permanentCityList = response.data.listObject;
						
						$scope.patientObj.permanentCityId="";
						$scope.patientObj.permanentAreaId="";
						$scope.patientObj.permanentZipCode="";
						}else if(type == 'L')
						{
							 $scope.cityList = [];
							 $scope.cityList = response.data.listObject;
							
						}else if(type == 'LTOP')
						{
							$scope.permanentCityList = [];
							$scope.permanentCityList = response.data.listObject;
						}	
					else
						{
						$scope.CompanyCityList = [];
						$scope.CompanyCityList = response.data.listObject;
						}
					}
					$scope.stopLoader();
				});
	}
	$scope.getAreaByCity = function(id,type) {
		$scope.startLoader();
		 $scope.idd = id;
		 
/*		var data = {
			cityId : id
		};
		var URI = BASE_URL + ROOT_URL
				+ GETAREALISTBYCITY;*/
		 var data = {
					offset:0,
					noOfRecordsPerPage:10,
				    cityId : id
			};
			var URI = BASE_URL + ROOT_URL
					+ "/adt/getAreaByCityIdPagination";
			
			
		BillingGenericService
		.serviceAction(METHOD_POST, URI, data,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(
				function(response) {
					
					if (response.data.status == "success")
						//$scope.areaList = response.data.listObject;
					{
						if(type == 'P')
						{
							$scope.areaList = [];
							$scope.logEvents = [];
							 $scope.logEvents = response.data.listObject;
						console.log($scope.areaList)
						$scope.patientObj.areaId="";
						$scope.patientObj.zipCode="";
						}
						else if(type == 'PA')
						{
						$scope.permanentAreaList = [];
						$scope.permanentAreaList = response.data.listObject;
						
						$scope.patientObj.permanentAreaId="";
						$scope.patientObj.permanentZipCode="";
						}else if(type == 'L')
						{
							$scope.areaList = [];
							$scope.logEvents = [];
							$scope.logEvents = response.data.listObject;
							console.log($scope.areaList)
						}else if(type == 'LTOP')
						{
							$scope.permanentAreaList = [];
							$scope.permanentAreaList = response.data.listObject;
						}	
					else
						{
						$scope.CompanyAreaList = [];
						$scope.CompanyAreaList = response.data.listObject;
						}
					}
					$scope.stopLoader();
				});
	}
	
	
	$scope.initAreaMasterList = function(offset, noOfRecordsPerPage)
	{
		$rootScope.startLoader();
		offset = offset != null ? offset : 0;
		noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
						var data = {
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
							    cityId : $scope.idd
						};
						var URI = BASE_URL + ROOT_URL
								+  "/adt/getAreaByCityIdPagination";
						BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									$scope.areaList = [];
									if (response.data.status == "success")
									{
										 $scope.logEvents = [];
										 $scope.logEvents = response.data.listObject;
										
									}
								});

	}	
    
	

	 $scope.loadMore = function()
     {
  	   $scope.numberToDisplay += 5;
         $scope.initAreaMasterList($scope.offset,$scope.numberToDisplay);
     }
	
	
	 $scope.applySize = function()
     {
  	   $('#selRegsPer').focus(function () {
  	 	    $('#selRegsPer').attr("size", "4");
  	 	});
  		
  	 	$('#selRegsPer').change(function () {
  	 	    $('#selRegsPer').attr("size", "1");
  	 	    $("#selRegsPer").blur();
	 	});	
  	 	
  	 	}
	 
	//------------ Get Speciality Master List ------------//
	$scope.getSpecialityMasterList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
			var obj = {
						organizationId : $scope.organizationId,
						unitId         :$scope.unitId
				    }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success"){
					$scope.specialityMasterList = response.data.listObject;
					$scope.specialityMasterListCopy = response.data.listObject;
					console.log('splList',$scope.specialityMasterList);
				}
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getSpecialityMasterList();
	
	if($scope.unitCountryId > 0)
	{
	$scope.countryId  = $scope.unitCountryId;
	  $scope.getStateByCountry($scope.countryId);
	}
	
	
	$scope.getPatientRegistrationDefaultValue = function(){
		
		$scope.patientObj = {

				prefixId : '',
				firstName : '',
				middleName : '',
				lastName : '',
				genderId : '',
				countryCallingCode :$scope.patientObj.countryCallingCode,
				email : '',
				nationalityId : $scope.patientObj.nationalityId,
				raceId : $scope.patientObj.raceId,
				maritalStatusId : '',
				mobileNumber : '',
				dob : '',
				identificationTypeId : '',
				identificationNumber : '',
				isVip : '',
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				updatedBy : $scope.userId,
				createdBy : $scope.userId,
				status : 'A',
				patientCategoryId:$scope.defaultPatientCategoryId,
				aliseName:'',
				identificationExpiryDate:'',
				occupationId:'',
				registrationTypeId:1,
				previousId:'',
				patientId:0,
				patientDetailsId:'',
				whatsappCallingCode : $scope.patientObj.countryCallingCode,
				whatsappNo: '',
				skypeId:'',
				address : '',
				countryId : '',
				stateId : '',
				districtId : '',
				cityId :'',
				areaId : '',
				zipCode : '',
				phoneCode : '',
				phoneNumber : '',
				permanentAddress:'',
				permanentCountryId:'',
				permanentStateId:'',
				permanentDistrictId:'',
				permanentCityId:'',
				permanentAreaId:'',
				permanentZipCode:'',
				permanentPhoneCode:'',
				permanentPhoneNumber:'',
				fileSequenceNumber:'',
				bloodGroup:'',
				isAlreadyExist:'N'
			};
		
		var data = {};
		var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
			var commonObj1 = {
				organizationId : $scope.organizationId
			};

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
						+ GETACTIVEPREFIXLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.prefixList = [];
									if (response.data.status == "success")
										angular.forEach(response.data.listObject, function(prefix) {
											if(prefix.isShowPatientRegistration == true){
												$scope.prefixList.push(prefix);
											}
										});
								});

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEGENDERLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.genderList = [];
									if (response.data.status == "success")
										$scope.genderList = response.data.listObject;
								});
				
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVEOCCUPATIONLIST;
		BillingGenericService
				.serviceAction(METHOD_GET, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							$scope.occupationList = [];
							if (response.data.status == "success")
								$scope.occupationList = response.data.listObject;
						});
		
				var URI = BASE_URL + ROOT_URL
						+ GETACTIVENATIONALITYLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, "",
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.nationalityList = [];
									if (response.data.status == "success")
										$scope.nationalityList = response.data.listObject;
									console.log("$scope.nationalityList",$scope.nationalityList);
								});

				var URI = BASE_URL + ROOT_URL + GETRACELIST;

				BillingGenericService
						.serviceAction(METHOD_POST, URI, commonObj1,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.raceList = [];
									if (response.data.status == "success")
										$scope.raceList = response.data.listObject;
								});
				
				

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEMARITALSTATUSLIST;

				BillingGenericService
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.maritalStatusList = [];
									if (response.data.status == "success")
										$scope.maritalStatusList = response.data.listObject;
								});
				

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

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVECOUNTRYLIST+$scope.organizationId;								
				BillingGenericService
				.serviceAction(METHOD_GET, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							$scope.countryList = [];
							if (response.data.status == "success")
								$scope.countryList = response.data.listObject;
						});
				//getting default race start
				var URI = BASE_URL + ROOT_URL
				+ "/adt/getDefaultRaceList/"+$scope.organizationId;
		BillingGenericService.serviceAction(METHOD_GET, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							$scope.defaultRaceList = [];
							if (response.data.status == "success" && response.data.listObject.length > 0)
							{
								$scope.patientObj.raceId = response.data.listObject[0].raceId;
								console.log("defaultRaceList=",response.data.listObject[0]);
							}
								
						});
		
		
		$scope.registrationFieldObj = {};
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
								email:"N",
								sourceId:"N"
						}
					}
				
				

			} else if (response.data.status == 'error') {
				growl.error(response.data.message,
					{
						title: 'Error!'
					});
			}
		});
	}
	$scope.getPatientRegistrationDefaultValue();	
	
	$scope.selectGender = function(id)
	{
		if(id > 0){
		var genId = $.grep($scope.prefixList, function (doc) {
            return doc.prefixId == id;
        })[0].id;
		
		$scope.patientObj.genderId = genId;
		}
		else
			{
			$scope.patientObj.prefixId = '';
			$scope.patientObj.genderId = '';
			}
		
	}
	
	/*$scope.getActiveEncounterTypeList = function()
    {
        var URI = BASE_URL + ROOT_URL +"/adt/getActiveEncounterTypeList";
    GenericService
        .serviceAction(METHOD_GET, URI,
                NOTIFICATION_MSG_STATUS_FALSE)
        .then(
                function(response) {
                    console.log(response);
                    if (response.data.status == "success"){
                        
                        $scope.encounterTypeList = response.data.listObject;
                        console.log("list",$scope.patientTypeList);
                    }
                    
                });
        
    }*/
	
	
	if($scope.unitCountryId > 0)
	{
	  $scope.patientObj.countryId  = $scope.unitCountryId;
	  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
	  $scope.patientObj.stateId  =  $scope.unitStateId;
	  $scope.patientObj.districtId = $scope.unitDistrictId;
	  $scope.patientObj.cityId = $scope.unitCityId;
	  $scope.patientObj.areaId = $scope.unitAreaId;
	  $scope.patientObj.zipCode = $scope.unitZipCode;
	  $scope.getStateByCountry($scope.patientObj.countryId,'L');
	  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
	  $scope.getDistrictByState($scope.patientObj.stateId,'L');
	  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
	  $scope.getAreaByCity($scope.patientObj.cityId,'L');
      $scope.selectFlag = false;
	}
	
	$scope.getDataByPinCode = function(){
		
		if($scope.patientObj.zipCode != null && $scope.patientObj.zipCode != undefined && $scope.patientObj.zipCode!="" && $scope.areaNotExist=='N' ){
			$rootScope.startLoader();
			var zipC = $scope.patientObj.zipCode;
			
		var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
		BillingGenericService
				.serviceAction(METHOD_GET,
						URI, "",
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							if (response.data.status == "success"){
								
								if(response.data.object.countryId != '' ){
									$scope.patientObj.countryId = response.data.object.countryId;
								}
								
								if(response.data.object.stateId != ''){
									$scope.getStateByCountry(response.data.object.countryId,'L');
									$scope.patientObj.stateId = response.data.object.stateId;
								}
								
								if(response.data.object.districtId != ''){
									$scope.getDistrictByState(response.data.object.stateId,'L');
									$scope.patientObj.districtId = response.data.object.districtId;
								}
								
								if(response.data.object.cityId != '')
									$scope.getCityByDistrict(response.data.object.districtId,'L');
									$scope.patientObj.cityId = response.data.object.cityId;
								
								if(response.data.object.areaId != ''){
									$scope.getAreaByCity(response.data.object.cityId,'L');
									$scope.patientObj.areaId = response.data.object.areaId;
								}
								var checkAreaName = false;
								angular.forEach($scope.areaList,function(value,key){
									
									if(value.areaId==$scope.patientObj.areaId)
									{
										checkAreaName = true;
										$scope.areaNew  = value.areaName;
									}
								});
								
								if(checkAreaName == false){
									$scope.areaNew = "";
								}
								
								$scope.selectArea = false;
								$scope.flgArea = true;
								 setTimeout(function(){
                                      $scope.selectArea = true;
                                    },500);
								$rootScope.stopLoader();
							}else{
								$scope.areaNew='';
								$scope.patientObj.areaId='';
								$rootScope.stopLoader();
							}
						});
		}
		
	}
	
	$scope.changePermAswell = function(){
		$scope.areaNotExistPermanent = $scope.areaNotExist;
	}
	$scope.appendPermArea = function(){
		$scope.patientObj.permanentArea = $scope.patientObj.area;
	}
	
	$scope.getDataByPinCodePermanent = function(){
		
		if($scope.patientObj.permanentZipCode != null && $scope.patientObj.permanentZipCode != undefined && $scope.patientObj.permanentZipCode != "" && $scope.areaNotExistPermanent=='N'){
			$rootScope.startLoader();
			var zipC = $scope.patientObj.permanentZipCode;
			
		var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
		BillingGenericService
				.serviceAction(METHOD_GET,
						URI, "",
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							if (response.data.status == "success"){
								
								if(response.data.object.countryId != '' ){
									$scope.patientObj.permanentCountryId = response.data.object.countryId;
								}
								
								if(response.data.object.stateId != ''){
									$scope.getStateByCountry(response.data.object.countryId,'LTOP');
									$scope.patientObj.permanentStateId = response.data.object.stateId;
								}
								
								if(response.data.object.districtId != ''){
									$scope.getDistrictByState(response.data.object.stateId,'LTOP');
									$scope.patientObj.permanentDistrictId = response.data.object.districtId;
								}
								
								if(response.data.object.cityId != '')
									$scope.getCityByDistrict(response.data.object.districtId,'LTOP');
									$scope.patientObj.permanentCityId = response.data.object.cityId;
								
								if(response.data.object.areaId != ''){
									$scope.getAreaByCity(response.data.object.cityId,'LTOP');
									$scope.patientObj.permanentAreaId = response.data.object.areaId;
								}
								var checkArea = false;
								angular.forEach($scope.permanentAreaList,function(value,key){
									
									if(value.areaId==$scope.patientObj.permanentAreaId)
									{
										var checkArea = true;
										$scope.areaNew1  = value.areaName;
									}
								});
								
								if(checkArea == false){
									$scope.areaNew1 = "";
								}
								$scope.selectArea = false;
								$scope.flgArea1 = true;
								 setTimeout(function(){
                                      $scope.selectArea = true;
                                    },500);	
								$rootScope.stopLoader();
							}else{
								$scope.areaNew1='';
								$scope.patientObj.permanentAreaId='';
								$rootScope.stopLoader();
							}
						});
		}
		
	}
	
	
	$scope.getActivePatientTypeList = function()
    {
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
        
    }
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
	$scope.getCoEmpVerificationReq();
	
    $scope.getActivePatientTypeList();
	
    $scope.initReferralObj = function(){
    	//alert();
    	$scope.referral = {
				"referralTypeId"      : "",
				"referralName"    	  : "",
				"address"             : "",
				"countryId"           :$scope.unitCountryId!=undefined && $scope.unitCountryId !=null ? $scope.unitCountryId : '',
				"stateId" 		      :  $scope.unitStateId!=undefined && $scope.unitStateId !=null ? $scope.unitStateId : '',
				"districtId" 		:  $scope.unitDistrictId!=undefined && $scope.unitDistrictId !=null ? $scope.unitDistrictId : '',
				"cityId" 			: $scope.unitCityId!=undefined && $scope.unitCityId !=null ? $scope.unitCityId : '',
				"areaId" 			: $scope.unitAreaId!=undefined && $scope.unitAreaId !=null ? $scope.unitAreaId : '',
				"email"  			: "",
				"contact" 			: "",
				"contactPerson"		: "",
				"unitId"            :$scope.unitId,
				"organizationId"    :$scope.organizationId
		}
    	$scope.diableDropDown=false;
    	
    }
    $scope.initReferralObj();
	$scope.initPatientSearchObj = function(){
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
		/*$scope.patientDetails = {
				"patientName" : "",
				"genderCode"  : "",
				"age"         : "", 
				"uhIdNumber"  : ""
		}*/
		$scope.patientDetails = {
				"patientId"	  : "",
				"patientName" : "",
				"genderCode"  : "",
				"age"         : "", 
				"uhIdNumber"  : "",
				"birthDate"	  : "",
				"encounterId" : ""
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
				'companyId':$scope.selfPayeeIdCookies,
				'payeeId':$scope.selfPayeeIdCookies,
				'tariffId':$scope.defaultSelfTariffId,
				'contractId':null,
				'insuredAmount' : ''
			}];
		
		$scope.paymentEntitleMentCorporate=
			[{
				'companyId':"",
				'contractId':"",
				//'statrDate':"",
				//'endDate':""
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
		
		$scope.initReferralObj();
		$scope.selectedOrderDetailIdsList = [];
		
		$scope.storeEmailId = null;
		 $scope.emailOtp = null;
        $scope.entredOpt = "";
		
	}
	$scope.initPatientSearchObj();
	
	
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
	$scope.initPaymentDetails();
	
	$scope.paymentModes = {
			1: "Cash",
			2: "Cheque",
			3: "Credit Card",
			4: "E - Transfer",
			5: "Loyalty Card"	
	}
	$scope.addBtnId=false;
	$scope.pinpadAmounFlag =false;
	$scope.setPaymentModeId=function(paymentModeId){
		if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
			$scope.addBtnId=true;
			 //$scope.pinpadAmounFlag =true;
			$scope.objPosMachine.deviceName= $cookies.getObject('deviceName');
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
	
	$scope.setBankName=function(bankId){
		angular.forEach($scope.bankList, function (value, key) {
			if(value.bankId == bankId){
				$scope.paymentDetails.bankName = value.bankName;
				return;
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
	$scope.getHospitalBankList();
	
	
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
	$scope.appointmentMasterObj = {};
	$scope.appointmentMasterObj.systemGeneratedVisitTypeId=null;
	 $scope.appointmentMasterObj.userDefinedVisitTypeId=null;
    // $scope.appointmentMasterObj.nursingStationId=$cookies.getObject('nursingStationId');
	 /*if($scope.encounterTypeList.length==1)
			$scope.appointmentMasterObj.encounterTypeId = $scope.encounterTypeList[0].toString();*/
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
				postCode 		: '',
				patientId       : '',
				unitId			: $scope.unitId,
				organizationId  : $scope.organizationId,
				createdBy		: $scope.userId,
				updatedBy		: $scope.userId,
//				expiryDateDisabled 		: true,
				createdDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				updatedDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
			};
		
		$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
	}
	$scope.initKinDetails();
  
    $scope.addNewKinDetails = function(){
    	$scope.kinDetails.patientId = $scope.appointmentMasterObj.patientId;
    	$scope.validateNexK=false;
    	angular.forEach($scope.kinDetailsList,function(value,index){
    		if(value.prefixId == "" || value.kinName == "" 
    			|| value.relationId == "" || value.mobileNo == "")
    		{
    			$scope.validateNexK=true;
    			growl.error("Please enter details ",{
					title : ERROR_MSG
					});
    			return false;
    		}
    	});
    	
    	if(!$scope.validateNexK){
    		$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
    	}
    	
    };
    $scope.removeKinDetails = function(indexToBeDeleted){	
		  $scope.kinDetailsList.splice(indexToBeDeleted, 1);
    };
    
    
    $scope.getReviewappoinemtnByPatient = function(flag)
	{
		$rootScope.startLoader();
		  if($scope.patientDetails==undefined||$scope.patientDetails.patientId==null||$scope.patientDetails.patientId=='')
			  {
				growl.warning("Please select Patient.",{
					title : ERROR_MSG
				});
				return false;
			  }
			var URI = BASE_URL + ROOT_URL+"/api/opd/reviewApplicable/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId;
			 BillingGenericService.serviceAction(METHOD_GET, URI,{},
				        NOTIFICATION_MSG_STATUS_FALSE)
				      .then(function(response) {
				         console.log(response);
				         if (response.data.status == "success")
				          {
				        		$rootScope.stopLoader();
				        		if(response.data.listObject!=null && response.data.listObject.length>0)
				        			{
				        			   $scope.reviewServiesList = response.data.listObject;
				        			   $rootScope.stopLoader();
				        			   if($scope.reviewServiesList!=null && $scope.reviewServiesList.length>0 && flag)
				        				   {
				        				    $('#ReviewAppointmentId').modal('show');
				        				   }
				        			}
				        		else{
				        			if(!$scope.isReviFlg)
				        				{
				        				$('#noReviewAppointmentId').modal('show');
				        				$scope.appointmentMasterObj.encounterTypeId = '';
				        				}
				        			 $rootScope.stopLoader();
				        		}
				          }
				         else if(response.data.status == "error")
				         {
				        	 if(!$scope.isReviFlg)
	        				{
				        	 $('#noReviewAppointmentId').modal('show');
				        	 $scope.appointmentMasterObj.encounterTypeId = '';
	        				}
		        			 $rootScope.stopLoader();
				          }
				        });
	}
    
	$scope.initAppointmentMasterObj = function(){
		/*$scope.appointmentMasterObj = {
				"appointmentMediaTypeId":"",
				"patientId" 			:"1",
				"doctorId"				:"",
				"opVisitType"			:"",
				"appointmentDate"		:1515474059174,
				"timeSlot"				:"",
				"appointmentTypeId"		:"",
				"categoryTypeId"		:"",
				"appointmentStatusId"	:"",
				"registrationDate"		:"",
				"departmentId"			:"",
				"isMlc"					:"I",
				"sourceId"				:"",
				"isReferal"				:"I",
				"remark"				:"",
				"kinDetailsId"			:"",
				"status"				:"A",
				"createdBy"				:$scope.unitId,
				"createdDate"			:new Date().getTime(),
				"updatedBy"				:$scope.userId,
				"updatedDate"			:new Date().getTime(),
				"unitId"				:$scope.userId,
				"organizationId"		:$scope.organizationId,
				"listKinDetailsDto"		:$scope.kinDetailsList
		}*/
		$scope.commonObj = {
				"unitId"				:$scope.unitId,
				"organizationId"		:$scope.organizationId,
				
		}
		
		$scope.appointmentMasterObj = {
				 "encounterId"			:"",
				 "patientId"			:"",
				 "visitTypeId"			:"",
				 "doctorId"				:"",
				 "sourceId"				:"",
				 "encounterDate"		:new Date().getTime(),
				 "encounterTime"		:"",
				 "isReferal"			:"A",
				 "deptId"				:"",
				 "subDeptId"			:"",
				 "remark"				:"",
				 "kinDetailsId"			:"",
				 "paymentEntitlementTypeId":"1",
				 "encounterNumber"		:"",
				 "createdDate"			:new Date().getTime(),
				 "createdBy"			:$scope.userId,
				 "updatedDate"			:new Date().getTime(),
				 "updatedBy"			:$scope.userId,
				 "status"				:"A",
				 "unitId"				:$scope.unitId,
				 "organizationId"		:$scope.organizationId,
				 "listKinDetailsDto"	:$scope.kinDetailsList,
				 "isScheduledAppointment":"I",
				 "modalityId":"",
				 "specialityId":"",
				 //"subSpecialityId":"",
				 "systemGeneratedVisitTypeId":"",
				 "userDefinedVisitTypeId":"",
				 "reasonIdForChangingVisitType":"",
                 "nursingStationId":JSON.parse(sessionStorage.getItem('nursingStationId')),
				 "remarkForChangingVisitType":"",
				 "encounterTypeId": 1,
				 "clinicId":"",
				 "referralTypeId":"",
				 "referralId":"",
				 "referralRemark":"",
				 "defaultSelfTariffId" : $scope.defaultSelfTariffId,
				 "isAppointmentRequired" : $scope.isAppointmentRequired,
				 "currentDate" :  moment(new Date()).format('YYYY-MM-DD'),
				 "counterId":$rootScope.counterId,
			}
		
		$scope.selfFlag = "N";
		$scope.paymentEntitlementShowFlag = false;
		if($scope.registrationTypeId != 2 && $scope.patientAdmittedFlag == false && $scope.isDocConsultReq=='Y'){
			$scope.encounterTypeList.push(1);
		}
		if($scope.registrationTypeId != 2 && $scope.patientAdmittedFlag == false && $scope.isEHCReq=='Y'){
			$scope.encounterTypeList.push(3);
		}
		if($scope.patientAdmittedFlag == false && $scope.isRadiologyReq=='Y'){
			$scope.encounterTypeList.push(2);
		}
		if($scope.patientAdmittedFlag == false && $scope.isLabReq=='Y'){
			$scope.encounterTypeList.push(4);
		}
		if($scope.registrationTypeId != 2 && $scope.patientAdmittedFlag == false && $scope.isReviewAppReq=='Y' && $scope.appointmentMasterObj.patientId > 0){
			$scope.encounterTypeList.push(6);
		}
		if($scope.isHospitalVisitReq=='Y'){
			$scope.encounterTypeList.push(7);	
		}
		if($scope.patientAdmittedFlag == false && $scope.isFreeVisitReq=='Y'){
			$scope.encounterTypeList.push(9);
		}
	    console.log("encounterTypeListTeststs",$scope.encounterTypeList);
	    if($scope.encounterTypeList.length == 1){
	    	 $scope.appointmentMasterObj.encounterTypeId = $scope.encounterTypeList[0];
	    	 
	    	 if($scope.appointmentMasterObj.encounterTypeId == 7){

					$scope.hideShowByTypeForEHC = false;
					$scope.hideShowByTypeForPackage = false;
					$scope.enableDisabled  = false;
					$scope.hideShowByType = false;
					$scope.paymentEntitlementShowFlag=true;
			
	    	 }
	    }
	   
	}
	$scope.initAppointmentMasterObj();
	
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

  /*   $scope.encounterSearchObject = {
             nursingStationId: $cookies.getObject('nursingStationId'),
         };*/
	
	$scope.getClinicByDoctorId = function(doctorId,type,check)
	{
		var data = {};
		var paramObj = {
				'doctor_id' : doctorId
		};
		var headers={
				'organization_id': $scope.organizationId,
				'unit_id':$scope.unitId,
				'Content-Type':'application/json'
		};
		
		
		if(doctorId > 0)
			{
				 // return false;
			    var URI = BASE_URL + ROOT_URL
				+ GETCLINICMASTERLISTBYDOCTORID;
	
				GenericServiceParamHeader
				.serviceAction(METHOD_GET, URI,
						data,
						NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
				.then(
						function(response) {
							console.log(response);
							// $rootScope.startLoader();
							$scope.clinicList = [];
							if (response.data.status == "success")
								
								$scope.clinicList =response.data.listObject;
								console.log("Clnic List",$scope.clinicList);
						});
				if($scope.hideShowByTypeForEHC == true && check  == 1 && ($scope.appointmentMasterObj.encounterTypeId == 1 || $scope.appointmentMasterObj.encounterTypeId == 2 || $scope.appointmentMasterObj.encounterTypeId == 9 ||$scope.appointmentMasterObj.encounterTypeId == 6) && $scope.isAppointmentRequired == 'Y'){
                    $scope.getAppointmentPopup();
                }
			}
	  
		if($scope.deptWiseAppointment =='N')
			$scope.appointmentMasterObj.userDefinedVisitTypeId ==null;
			
		//console.log("ff",$scope.encounterDetailsObj);
		//console.log("ff2",$scope.encounterDetailsObj.patientId);
		if($scope.encounterDetailsObj.patientId == undefined || type == 1)
		{
			if($scope.patientDetails.patientId > 0)
				{
				 var  testData = {
					    	
			    		 unitId :$scope.unitId,
						 organizationId  :$scope.organizationId,
			    		 patientId : $scope.patientDetails.patientId  ,
			    		 doctorId : doctorId ,
			    		 specialityId : $scope.appointmentMasterObj.deptId,
			    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
				    		 
			     };
				 $scope.appointmentMasterObj.specialityId  = $scope.appointmentMasterObj.deptId;
				 console.log("SpecilaityId:",$scope.appointmentMasterObj.specialityId);
				 
			    // testData.currentDate = moment(new Date()).format('YYYY-MM-DD');
			     console.log("new direct obj",testData);
			     //return false;
			     if($scope.appointmentMasterObj.encounterTypeId==6)
		    	 {
			    	 $scope.appointmentMasterObj.systemGeneratedVisitTypeId=($scope.appointmentMasterObj.encounterTypeId).toString();
					 $scope.appointmentMasterObj.userDefinedVisitTypeId=($scope.appointmentMasterObj.encounterTypeId).toString();
			    	
		    	 }else{
		    		 var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
				     BillingGenericService.serviceAction(METHOD_POST,URI, testData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				     
				    $scope.appointmentMasterObj.systemGeneratedVisitTypeId=response.data.toString();
				    $scope.appointmentMasterObj.userDefinedVisitTypeId=response.data.toString();
				    if(type == 1){
				    	$scope.getBillDetails();
				    }
				    
				     });
		    	 }
			    
			     
				}
			else
				{
					/*growl.warning("Please Select Patient",{
						title : WARNING_MSG
					});
					return;*/
				  $scope.appointmentMasterObj.systemGeneratedVisitTypeId="1";
				  $scope.appointmentMasterObj.userDefinedVisitTypeId="1";
				  
				  $scope.getBillDetailsForNewPatient();
				}
		}
		if( $scope.selfFlag == "Y")
		{
			if($scope.patientDetails.patientId > 0)
				{
				 var  testData = {
					    	
			    		 unitId :$scope.unitId,
						 organizationId  :$scope.organizationId,
			    		 patientId : $scope.patientDetails.patientId,
			    		 doctorId : doctorId ,
			    		 specialityId : $scope.appointmentMasterObj.deptId,
			    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
				    		 
			     };
				 $scope.appointmentMasterObj.specialityId  = $scope.appointmentMasterObj.deptId;
				 console.log("SpecilaityId:",$scope.appointmentMasterObj.specialityId);
				 
			    // testData.currentDate = moment(new Date()).format('YYYY-MM-DD');
			     console.log("new direct obj",testData);
			     //return false;
			     var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
			     BillingGenericService.serviceAction(METHOD_POST,URI, testData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			     
			    $scope.appointmentMasterObj.systemGeneratedVisitTypeId=response.data.toString();
			    $scope.appointmentMasterObj.userDefinedVisitTypeId=response.data.toString();
			    $scope.getBillDetails();
			     });
			     
				}
			else
				{
				/*	growl.warning("Please Select Patient",{
						title : WARNING_MSG
					});
					return;*/
				 $scope.appointmentMasterObj.systemGeneratedVisitTypeId="1";
				  $scope.appointmentMasterObj.userDefinedVisitTypeId="1";
				  
				  $scope.getBillDetailsForNewPatient();
				}
		}
		
		if($scope.deptWiseAppointment =='N')
		{
			$scope.specialityname = '';
			$scope.fromTime = '';
			$scope.toTime = '';
			$scope.appointmentDate = '';
			$scope.slotId = '';
			$scope.appointmentMasterObj.encounterTime = "";
		}
		$scope.doctorName ='';
		$scope.doctorId = '';
		
		$scope.bookedpatientLength = '';
		if($scope.encounterDetailsObj.appointmentId == null || $scope.encounterDetailsObj.appointmentId == '' || $scope.encounterDetailsObj.appointmentId == undefined)
			$scope.appointmentMasterObj.encounterTime = '';
		
	}
	
	$scope.getSubSpecialityBySpeciality = function(id) {
		
		var selectedSplObject = $.grep($scope.specialityMasterList, function (splList) {
            return splList.specialityId == id;
        });
		
		$scope.isNursingAssessmentRequiredForSpeciality = selectedSplObject.isNursingAssessmentRequired;
		console.log("$scope.isNursingAssessmentRequiredForSpeciality",$scope.isNursingAssessmentRequiredForSpecialityForSpeciality);
		console.log("selectedSplObject",selectedSplObject);
		$scope.specialityName = selectedSplObject!=undefined?selectedSplObject[0].specialityName:null;
		
		var data = {
			organizationId : $scope.organizationId,
			unitId : $scope.unitId,
			specialityId : id
		}
		console.log(data);
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

							}

						});
	}
	
	$scope.getModalityBySubSpeciality = function(id) {
		//alert('assa');
		var array = id.split('_');
		
		var data = {
			organizationId : $scope.organizationId,
			unitId : $scope.unitId,
			specialityId : array[0],
			subSpecialityId :array[1]
		}
		console.log(data);
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
							}

						});
	}
	
	
	
	
	
	$scope.enableDisableDoctor = function(encounterTypeId)
	{
		 $scope.appointmentMasterObj.systemGeneratedVisitTypeId=null;
		 $scope.appointmentMasterObj.userDefinedVisitTypeId=null;
		 $scope.appointmentMasterObj.doctorId = null;
		 $scope.appointmentMasterObj.deptId = null;
		 $scope.appointmentMasterObj.subDeptId = null;
		 $scope.appointmentMasterObj.patientClassId = '';
		 $scope.paymentEntitlementShowFlag = (encounterTypeId==6)?false:true;
		 $scope.appointmentMasterObj.modalityId = null;
		
		if(encounterTypeId == 1 || encounterTypeId == 9)
			{
			   $scope.hideShowByTypeForEHC = true;
			   $scope.hideShowByTypeForPackage = false;
			   $scope.enableDisabled  = false;
			   $scope.hideShowByType = true;
				/*var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
				var obj = {
							organizationId : $scope.organizationId,
							unitId         :$scope.unitId
					    }
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					console.log(response);
					if(response.data.status == "success"){
						$scope.specialityMasterList = response.data.listObject;
						console.log('splList',$scope.specialityMasterList);
					}
				});*/
				$scope.specialityMasterList = $scope.specialityMasterListCopy;
				
			}
		else if(encounterTypeId == 2){
			    $scope.hideShowByTypeForEHC = true;
				$scope.enableDisabled  = true;
				$scope.hideShowByType = false;
				
				var radioSpecialityObj = $.grep($scope.specialityMasterListCopy, function (doc) {
		            return doc.specialityId == $scope.radiologySpecialtyId;
		        });
				console.log("radioSpecialityObj",radioSpecialityObj);
				$scope.specialityMasterList = radioSpecialityObj;
				console.log("$scope.specialityMasterList",$scope.specialityMasterList);
			}
		else if(encounterTypeId == 3)
		{
//			$rootScope.startLoader();
			if($scope.appointmentMasterObj.appointmentId==null||$scope.appointmentMasterObj.appointmentId==undefined){
				$rootScope.startLoader();
				$scope.appointmentMasterObj.userDefinedVisitTypeId='5';
				$scope.hideShowByTypeForEHC = false;
				$scope.hideShowByTypeForPackage = true;
				this.hideShowByTypeForPackage = true;
				$scope.enableDisabled  = false;
				$scope.hideShowByType = false;
				this.search='';
				$scope.EHCpackageId = undefined;
				$scope.appointmentMasterObj.serviceId = undefined;
				var checkEHCSlotObj={
						
						 unitId :$scope.unitId,
						 organizationId  :$scope.organizationId,
			    		 slotTypeId : 3,
			    		 appointmentDate :moment(new Date()).format('YYYY-MM-DD'),
				}
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/isEHCSlotAvailable";
				BillingGenericService.serviceAction(METHOD_POST,URI,checkEHCSlotObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if (response.data.status == "success") 
					{
						$scope.slotId = response.data.object;
						$rootScope.stopLoader();
					}
					else if(response.data.status == "error")
						{
						$rootScope.stopLoader();
						$scope.slotId = null;
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
					}
				});
			}
		}
		else if(encounterTypeId == 4){
			$scope.hideShowByTypeForEHC = true;
			$scope.hideShowByTypeForPackage = false;
			$scope.hideShowByType = false;
			
			
			var labSpecialityObj = $.grep($scope.specialityMasterListCopy, function (doc) {
	            return doc.specialityId == $scope.labSpecialityId;
	        });
			console.log("labSpecialityObj",labSpecialityObj);
			$scope.specialityMasterList = labSpecialityObj;
			console.log("$scope.specialityMasterList",$scope.specialityMasterList);
			
			
		}
		else if(encounterTypeId == 5){
			$scope.hideShowByTypeForEHC = false;
			$scope.hideShowByTypeForPackage = false;
			$scope.hideShowByType = false;
		}
		 if(encounterTypeId == 6)
		{
			 $scope.hideShowByTypeForEHC = true;
			 $scope.hideShowByTypeForPackage = false;
			 $scope.hideShowByType = true;
			 $scope.getReviewappoinemtnByPatient(true);
			 $scope.isReviFlg = false;
			 $scope.enableDisabled  = false;
			 $scope.hideShowByType = true;
			 
		}else{
			 $scope.isReviFlg = true;
		}
		
		 if(encounterTypeId == 7) {
				$scope.hideShowByTypeForEHC = false;
				$scope.hideShowByTypeForPackage = false;
				$scope.enableDisabled  = false;
				$scope.hideShowByType = false;
				$scope.paymentEntitlementShowFlag=true;
		}
		 
		 if(encounterTypeId != 6 && $scope.appointmentMasterObj.patientId > 0){
				var URI = BASE_URL + ROOT_URL + "/api/opd/lastSponsors/"+$scope.appointmentMasterObj.patientId+"/1/"+$scope.unitId;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, {},
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									if (response.data.status == "success") {
										$scope.forCheckingPatientClassAndEntitlement = response.data.listObject;
										if( response.data.object > 0){
											$scope.appointmentMasterObj.patientClassId = response.data.object;
										}
										if($scope.forCheckingPatientClassAndEntitlement!=null && $scope.forCheckingPatientClassAndEntitlement.length > 0){
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
													  		newArr.tariffId = value.tariffId;
													  		newArr.contractId = value.contractId;
													  		newArr.payeeId = value.payeeId;
													  		newArr.insuredAmount = value.insuredAmt;
													  		newArr.insuranceId = value.insuranceId;
													  		newArr.claimNumber = value.claimNumber;
													  		newArr.patientSponsorDetailsId = value.patientSponsorDetailsId;
													  		if(value.paymentEntitlementId ==1){
													  			newArr.paymentEntitlementName = "Self";
													  			newArr.companyName = "Self";
													  			newArr.tariffName = "Self Tariff";
													  		    newArr.tariffId = $scope.defaultSelfTariffId;
													  		    newArr.companyId = $scope.selfPayeeIdCookies;
													  		}else{
													  			newArr.paymentEntitlementName = value.paymentEntitlementDesc;
													  			newArr.companyName = value.companyDesc;
													  			newArr.tariffName = value.tariffDesc;
													  			newArr.tariffId = value.tariffId;
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
		 if($scope.otReservationList.length>0){
			 $("#otReservationDetails").modal("show"); 
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
	}
	
	$scope.hideShowAlise = function(vipVal)
	{
		if(vipVal == 'Y')
			$scope.aliseFlag = true;
		else
			$scope.aliseFlag = false;
			
	}
	
	$scope.sameAsLocalCopy = function(vipVal)
	{
		if(vipVal == 'Y')
			{
			
			/*if($scope.patientObj.address =="" || $scope.patientObj.address ==null
				||  $scope.patientObj.countryId =="" ||  $scope.patientObj.countryId ==null 
				||  $scope.patientObj.stateId ==null ||  $scope.patientObj.stateId =="" 
				|| $scope.patientObj.districtId =="" || $scope.patientObj.districtId == null
				|| $scope.patientObj.cityId ==""  ||$scope.patientObj.cityId ==null
				|| $scope.patientObj.areaId == "" || $scope.patientObj.areaId == null){
				//|| $scope.patientObj.phoneCode =="" || $scope.patientObj.phoneNumber ==""){
				$scope.sameAsLocal='N';
				 growl.warning("Please Select All Required fields",{
						title : 'Warning !'
						});
						return false;
			}*/
			
			if($scope.patientObj.countryId != '' )
			$scope.getStateByCountry($scope.patientObj.countryId,'LTOP');
			
			if($scope.patientObj.stateId != '' )
			$scope.getDistrictByState($scope.patientObj.stateId,'LTOP');
			
			if($scope.patientObj.districtId != '' )
			$scope.getCityByDistrict($scope.patientObj.districtId,'LTOP');
			
			if($scope.patientObj.cityId != '' )
			$scope.getAreaByCity($scope.patientObj.cityId,'LTOP');
			
			$scope.patientObj.permanentAddress = $scope.patientObj.address;
			$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
			$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
			$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
			$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
			$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
			$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
			$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
			$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
			$scope.selectFlag = false;
            $scope.addressFlag =true;
			}
		else
			{
			/*$scope.patientObj.permanentAddress ='';
			$scope.patientObj.permanentCountryId = '';
			$scope.patientObj.permanentStateId ='';
			$scope.patientObj.permanentDistrictId ='';
			$scope.patientObj.permanentCityId ='';
			$scope.patientObj.permanentAreaId ='';
			$scope.patientObj.permanentZipCode ='';
			$scope.patientObj.permanentPhoneCode ='';
			$scope.patientObj.permanentPhoneNumber ='';*/
            $scope.addressFlag =false;
			}
	}
	$scope.setReferralObject = function()
	{
		
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
		console.log("referralData",$scope.referralData);
		$scope.appointmentMasterObj.referralDesc = $scope.referral.referralName;
		$scope.appointmentMasterObj.referralMasterDto = $scope.referralData;
	}
	
	
	  $scope.copyMobileToWhatsappNo = function () {
	    	$scope.patientObj.whatsappNo = $scope.patientObj.mobileNumber;
      }
	    $scope.copyCountryCodeToWhatsappCode = function(){
	    	
	    	$scope.patientObj.whatsappCallingCode = $scope.patientObj.countryCallingCode;
	    }
	
	$scope.getLastEncounterDetails=function(showAlert){
		
		var sdate = new Date();
	 	sdate.setDate(sdate.getDate() - 1);
	 	$scope.yesterday=sdate;
	 	
		
		$scope.encounterDetailsObjLast={
				 unitId          :$scope.unitId,
				 organizationId  :$scope.organizationId,
	    		 patientId : $scope.patientDetails.patientId,
	    		 toDate:moment($scope.yesterday).format('YYYY-MM-DD')
		}
		
		if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId==''){
				growl.warning("Please select patient !!",{
					title : ERROR_MSG
				});
				return false;
		}
		
		var URI = BASE_URL + ROOT_URL + GETENCOUNTERDETAILS;
		BillingGenericService.serviceAction(METHOD_POST,URI,$scope.encounterDetailsObjLast, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.encounterList = response.data.listObject;
			
			if(response.data.listObject.length>0){
				$scope.totalOutstanding = 0;
				angular.forEach(response.data.listObject,function(value,index)
				{
					value.encounterDateString=moment(value.encounterDateString,"YYYY-MM-DD").format($scope.dateFormat.toUpperCase())
					value.encounterDateString2=moment(value.encounterDateString,"DD/MM/YYYY'").format($scope.dateFormat.toUpperCase())
					
					if(value.selfDueAmount > 0)
						{
							$scope.totalOutstanding = $scope.totalOutstanding+value.selfDueAmount;
						}
				});
				
				if(showAlert=='Y' && $scope.totalOutstanding>0)
				{

					
					angular.element('#outStandingPopup').modal('show');
					return false;
				}
				
			}
			
		});
		
		
	}
	
	$scope.closeDigitaslSig = function()
	{
		 angular.element('#digitalSignaturePopup').modal('hide');
	}
	$scope.reprintEncounter=function(encounterId){
	    var data =  {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "OPDCaseSheet.rpt",
                "reportFormat": "PDF",
                "exportFileName": "OPDCoversheet",
                "reportParams": [
			                {
			                  "paramName": "@EncounterId",
			                  "paramType": "Integer",
			                  "paramValue":  encounterId
			                }
			               
        ]
};
console.log('data',data);
reportService.generate(REPORT_API_URI1,data);
	}
	
	$scope.getSexMasterList = function(){
		var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.sexMasterList = response.data.listObject;
		});
	}
	$scope.getSexMasterList();
	
	$scope.getTariffMaster = function(){
		
		var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
		
		
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
	}
	$scope.getTariffMaster();
	$scope.loadMoreData = function()
	{
		$scope.offset+=200;
		$scope.loadMoreFlag=true;
		$rootScope.searchPatient($scope.patientSearchObj);
	}
	$scope.offset=0;
	$scope.loadMoreFlag=false;
	$scope.noOfRecordsPerPage=200;
	$scope.patientList = [];
	$rootScope.searchPatient = function(patientsearchObj){
		
		if($scope.patientSearchObj.uhIdNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNo == '' && $('#datepickerSearch').val() == '' && ( patientsearchObj.identificationTypeId == "" || patientsearchObj.identificationTypeId == undefined ) && $scope.patientSearchObj.identificationNo == '')
		{
			growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
			});
			return false;
		}
			
		else{
				//alert("else AlERT");
			}
		if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			return false;
		}	
		$rootScope.startLoader();
		
	    if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
            patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
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
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
		BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			
			if(response.data.listObject.length == 0){
				growl.warning("No Result Found!",{
					title : ERROR_MSG
				});
			}
			else{
				$scope.patientList = [];
			angular.forEach(response.data.listObject,function(value,key)
					{
				  		if(value.registrationTypeId != 5 && value.registrationTypeId != 4)
				  			{
				  			$scope.patientList.push(value);
				  			}
				  		
					});
			}
			
		});
		console.log("patientList=",$scope.patientList)
	}
	
	

	
	$scope.checkDateValid= function()
	{
		if($scope.datepickerSearch == undefined || $scope.datepickerSearch == null || $scope.datepickerSearch == ''){
			
			   return false;
		}else{
		if($scope.datepickerSearch > new Date())
			{
						   growl.warning("DOB should not be greater than today's date",{
								title : "warning"
							});
							$scope.datepickerSearch = new Date();
						
						   return false;
			}
	}
	
	}
	
	
	
	$scope.datediff = function(dateFrom, dateTo)
	{
	    var from = {
	        d: dateFrom.getDate(),
	        m: dateFrom.getMonth(),
	        y: dateFrom.getFullYear()
	    };

	    var to = {
	        d: dateTo.getDate(),
	        m: dateTo.getMonth(),
	        y: dateTo.getFullYear()
	    };

	    var daysFebruary = to.y % 4 != 0 || (to.y % 100 == 0 && to.y % 400 != 0)? 28 : 29;
	    var daysInMonths = [31, daysFebruary, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

	    if (to.d < from.d) {
	        to.d   += daysInMonths[parseInt(to.m+1)];
	        from.m += 1;
	    }
	    if (to.m < from.m) {
	        to.m   += 12;
	        from.y += 1;
	    }


        if(to.d == from.d && to.m == from.m && to.y== from.y && $scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker.opened == undefined){
            return {
                   //days:   1,
                   days:   0,
                   months: to.m - from.m,
                   years:  to.y - from.y
               };
       }else{
            return {
                   days:   to.d - from.d,
                   months: to.m - from.m,
                   years:  to.y - from.y
               };
       }
	}

	
		$scope.newAge="";
		$scope.calDOBFlag = 0;
		$scope.calculateAge= function(flag)
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
					var nDt = moment(new Date()).format('MM/DD/YYYY');
					
					
					var array = dt.split('/');
					var array1 = nDt.split('/');
				   
					var curday = array1[1];
					var curmon =array1[0];
					var curyear =array1[2];
					
					var calday =array[1];
					var calmon = array[0];
					var calyear = array[2];
					
						var curd = new Date(curyear,curmon-1,curday);
						var cald = new Date(calyear,calmon-1,calday);
						
				                    var diff =  Date.UTC(curyear,curmon-1,curday,0,0,0) - Date.UTC(calyear,calmon-1,calday,0,0,0);
				    
				                    var dife = $scope.datediff(cald,curd);
				                  if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 1){
				                    $scope.ageYear = dife.years;
							        if(flag==true){
                                            $scope.ageMonth = 0;
                                            $scope.ageDay = 0;
                                        }
                                        else {
                                            $scope.ageMonth = dife.months;
                                            $scope.ageDay = dife.days;
                                        }
							        if($scope.ageYear!=undefined && $scope.ageYear!=null && $scope.ageYear!="" && $scope.ageYear!=0
			                    			 && $scope.ageMonth!=undefined && $scope.ageMonth!=null && $scope.ageMonth!="" && $scope.ageMonth!=0
			                    			 && $scope.ageDay!=undefined && $scope.ageDay!=null && $scope.ageDay!="" && $scope.ageDay!=0){
			                    		 $scope.calDOBFlag = 1;
			                    	 }
								  }
								  else if ($scope.calDOBFlag == 2 && flag==true){
                                       $scope.ageYear = dife.years;
                                       $scope.ageMonth = 0;
                                         $scope.ageDay = 0;
                                    }
                                        else if($scope.calDOBFlag == 2){
                                       $scope.ageYear = dife.years;
                                       $scope.ageMonth = dife.months;
                                         $scope.ageDay = dife.days;
                                        }

				}
		}
		
		}
		
		$scope.myFunctDay = function(day) {
		if(  $scope.ageYear==undefined ||  $scope.ageYear==null || $scope.ageYear==0  ){
                     $scope.ageYear="";
                     //$scope.datepicker="";
                     $scope.ageDay = 0;
                     $scope.ageMonth = "";
                         growl.warning("It is advisable to enter actual DOB",{
                                       title : "warning"
                                       });
                                       return false;
                        }
			if(day>31){
				$scope.ageDay = 0;
				growl.warning("Day should be less then 31 ",{
					title : "warning"
					});
			}else{
				var cntOfYears = 0;
				var cntOfMonths = 0;
				var cntOfDays = 0;
				
				if($scope.ageDay == undefined || $scope.ageDay == null || $scope.ageDay == ""){
					$scope.ageDay = 0;
				}else{
					cntOfDays = $scope.ageDay;
				}
				

				if( $scope.ageMonth == undefined || $scope.ageMonth == null ||  $scope.ageMonth == ""){
					$scope.ageMonth = 0;
				}else{
					cntOfMonths = $scope.ageMonth;
				}
				
				if($scope.ageYear == undefined || $scope.ageYear == null ||  $scope.ageYear == ""){
					$scope.ageYear = 0;
				}else{
					cntOfYears = $scope.ageYear;
				}

			}
			  
		}
		$scope.myFunctMonth = function(month) {
		if( $scope.ageYear==undefined ||  $scope.ageYear==null || $scope.ageYear==0  ){
                 $scope.ageMonth = 0;
                     growl.warning("It is advisable to enter actual DOB",{
                                   title : "warning"
                                   });
                                   return false;
                    }
			if(month>12){
				$scope.ageMonth = 0;
				growl.warning("Month should be less then 12 ",{
					title : "warning"
					});
			}else{
				var cntOfYears = 0;
				var cntOfMonths = 0;
				var cntOfDays = 0;
				
				if($scope.ageDay == undefined || $scope.ageDay == null ||  $scope.ageDay == ""){
					$scope.ageDay = 0;
				}else{
					cntOfDays = $scope.ageDay;
				}
				

				if($scope.ageMonth == undefined || $scope.ageMonth == null || $scope.ageMonth == ""){
					$scope.ageMonth = 0;
				}else{
					cntOfMonths = $scope.ageMonth;
				}
				
				if($scope.ageYear == undefined || $scope.ageYear == null ||  $scope.ageYear == ""){
					$scope.ageYear = 0;
				}else{
					cntOfYears = $scope.ageYear;
				}
				
				//$scope.subtractCurrDateFromDOB(cntOfYears,cntOfMonths,cntOfDays);
				if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
					$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
					if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
						&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
						$scope.calDOBFlag = 2;
					}
				}
			}
			  
		}
		$scope.myFunctYear = function(age) {
		            if(age==undefined || age ==""){
                            $scope.ageYear="";
                            $scope.ageMonth="";
                            $scope.datepicker = "";
                            //return false;
                        }

			if(age>=$scope.minAge && age <= $scope.maxAge){
				             if($('#datepicker').val()!=""){
                                    var dt = moment($scope.datepicker).format('MM/DD/YYYY');
                                    var newAgeE = $rootScope.getAge(dt);
                                    var tArr = dt.split("/");
                                    var m = tArr[0];
                                    var d=tArr[1];
                                   if(newAgeE != age)
                                        {
                                         var currentYear = (new Date()).getFullYear();
                                         var finalYear = currentYear - age;
                                         $scope.datepicker = new Date("01/01/"+finalYear);
                                            }
                                        }
                                            else{
                                    var currentYear = (new Date()).getFullYear();
                                      var finalYear = currentYear - age;
                                     $scope.datepicker = new Date("01/01/"+finalYear);
                                        }
				var cntOfYears = 0;
				var cntOfMonths = 0;
				var cntOfDays = 0;
				
				if($scope.ageDay == undefined || $scope.ageDay == null || $scope.ageDay == ""){
					$scope.ageDay = 0;
				}else{
					cntOfDays = $scope.ageDay;
				}
				

				if($scope.ageMonth == undefined ||  $scope.ageMonth == null || $scope.ageMonth == "" ){
					$scope.ageMonth = 0;
				}else{
					cntOfMonths = $scope.ageMonth;
				}
				
				if($scope.ageYear == undefined || $scope.ageYear == null ||  $scope.ageYear == ""){
					$scope.ageYear = 0;
				}else{
					cntOfYears = $scope.ageYear;
				}
				

				if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
					//$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
					if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
						&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
						$scope.calDOBFlag = 2;
					}
				}
			}

			  
		}

                    $scope.birthAge = function (age){
                            if(age != '' && age<=10 ){
                                     $scope.ageYear=0;
                                     $scope.datepicker= new Date();
                                      growl.warning("It is advisable to enter actual DOB",{
                                                    title : "warning"
                                                    });
                                                    return false;
                                     }
                            }
	//Thumb search
	
	$scope.thumbSearch = function()
	{
		 try {
			    $rootScope.startLoader();
	           	var quality = 60; //(1 to 100) (recommanded minimum 55)
	               var timeout = 10; // seconds (minimum=10(recommanded), maximum=60, unlimited=0 )
	               var res = CaptureFinger(quality, timeout);	
	               if (res.httpStaus) 
	               {
	            	   if (res.data.ErrorCode == "0") 
	                   {
	                       $scope.thumbImage = res.data.BitmapData;
	                       var URI = BASE_URL + ROOT_URL + "/api/global/searchByFingerPrint";
	                       
	                       var param = 
	                       {
	                    		   organizationId:$scope.organizationId,
	                    		   unitId:$scope.unitId,
	                    		   thumbImage:res.data.BitmapData
	                    		  }; 
	                       }
	            	   BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
	           			$scope.searchPatientList = [];
	           			if (response.data.status == "success")
	           				{
	           				$scope.patientList = [];
	           				$scope.patientListTemp = [];
	           				$scope.patientListTemp = response.data.listObject;
	        				if($scope.patientListTemp.length == 0){
	        					//alert();
	        					growl.warning("No Result Found!",{
	        						title : ERROR_MSG
	        					});
	        				}  else{
	        					angular.forEach($scope.patientListTemp,function(value,key)
	        							{
	        						  		if(value.registrationTypeId != 5 && value.registrationTypeId != 4)
	        						  			{
	        						  			$scope.patientList.push(value);
	        						  			}
	        						  		
	        							});
	        				}      				
	           				
	           				$rootScope.stopLoader();
	           				}
	           			else if(response.data.status == "error"){
	           				$rootScope.stopLoader();
	           			}
	           			
	           		});
	                   
	               }
	               else 
	               {
	                   alert(res.err);
	                   $rootScope.stopLoader();
	               }
	           }
	           catch (e) {
	               alert(e);
	               $rootScope.stopLoader();
	           }
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

		$scope.clearFields = function()
		{
			$scope.patientList = [];
			$scope.appointmentMasterObj = {};
			$scope.patientAdmittedFlag = false;
			$scope.appointmentMasterObj.patientId = '';
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
			$scope.patientDetails = {
					"patientId"	  : "",
					"patientName" : "",
					"genderCode"  : "",
					"age"         : "", 
					"uhIdNumber"  : "",
					"birthDate"	  : "",
					"encounterId" : "",
					"createdDate" :""
			}
			$scope.patientAdmittedFlag = false;
			$scope.initPatientSearchObj();
			$scope.initAppointmentMasterObj();
			$scope.initKinDetails();
			$scope.serviceOrderList = [];
			$scope.totalServiceAmt = 0;
			$scope.patientList=[];
			$scope.encounterList=[];
			$scope.secondTabLevel = 1 ;
			
			$scope.patientObj = {

					prefixId : '',
					firstName : '',
					middleName : '',
					lastName : '',
					genderId : '',
					countryCallingCode : '',
					email : '',
					nationalityId : '',
					raceId : '',
					maritalStatusId : '',
					mobileNumber : '',
					dob : '',
					identificationTypeId : '',
					identificationNumber : '',
					isVip : '',
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					updatedBy : $scope.userId,
					createdBy : $scope.userId,
					status : 'A',
					patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
					aliseName:'',
					identificationExpiryDate:'',
					occupationId:'',
					registrationTypeId:1,
					previousId:'',
					patientId:0,
					patientDetailsId:'',
					whatsappCallingCode : '',
					whatsappNo:'',
					skypeId:'',
					address : '',
					countryId : '',
					stateId : '',
					districtId : '',
					cityId :'',
					areaId : '',
					zipCode : '',
					phoneCode : '',
					phoneNumber : '',
					permanentAddress:'',
					permanentCountryId:'',
					permanentStateId:'',
					permanentDistrictId:'',
					permanentCityId:'',
					permanentAreaId:'',
					permanentZipCode:'',
					permanentPhoneCode:'',
					permanentPhoneNumber:'',
					fileSequenceNumber:'',
					bloodGroup:'',
					isAlreadyExist:'N'
				};
			 $scope.patientAdmittedFlag = false;
				$scope.ageDay = 0;
				$scope.ageMonth = 0;
				$scope.ageYear = 0;
				
				if($scope.unitCountryId > 0)
				{
				  $scope.patientObj.countryId  = $scope.unitCountryId;
				  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
				  $scope.patientObj.stateId  =  $scope.unitStateId;
				  $scope.patientObj.districtId = $scope.unitDistrictId;
				  $scope.patientObj.cityId = $scope.unitCityId;
				  $scope.patientObj.areaId = $scope.unitAreaId;
				  $scope.patientObj.zipCode = $scope.unitZipCode;
				  $scope.getStateByCountry($scope.patientObj.countryId,'L');
				  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
				  $scope.getDistrictByState($scope.patientObj.stateId,'L');
				  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
				  $scope.getAreaByCity($scope.patientObj.cityId,'L');
                  $scope.selectFlag = false;
				}
				$scope.calDOBFlag = 0;
		}
	
		$scope.getAppointmentDetailsByPatientId = function(index,patientId){
			var data = {
					patientId : patientId,
					unitId : $scope.unitId
			};
			var URI = BASE_URL + ROOT_URL + "/api/opd/getAppointMentDetailsByPatientId";
			BillingGenericService.serviceAction("POST", URI, data).then(function(response) {
						$scope.countryList = [];
						if (response.data.status == "success") {
							if(response.data.object!=null){
								$scope.appointmentId=response.data.object.appointmentId;
								$scope.patientList[index].appointmentId = response.data.object.appointmentId;
								$scope.patientList[index].appointmentStatusId = response.data.object.appointmentStatusId;
							}
								
							if($scope.patientList[index].appointmentId != null && $scope.patientList[index].appointmentId >0){
								$scope.alreadyPatient = 1;
								var URI = BASE_URL + ROOT_URL + "/api/opd/searchInProcessAppointmentWeb/"+$scope.patientList[index].appointmentId;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, {},
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													$scope.alreadyAppointment = {};
													// $rootScope.startLoader();
													if (response.data.status == "success") {
														$scope.alreadyAppointment = response.data.listObject[0];
														$scope.appointmentMasterObj.encounterTypeId=response.data.listObject[0].appointmentTypeId;
														$scope.appointmentMasterObj.doctorId = response.data.listObject[0].doctorId;
														$scope.checkAppointment();
													}
												});
									}
							
							
						}
					});
		}
		
	$scope.getPatientDetailsByIndex = function(index){
		
		
		 $scope.otpvalidated = false;
		 $scope.otpvalidatedMobile = false;
		 $scope.initAppointmentMasterObj();
		 $scope.isTeleconsultationAppointment=0;
		 $scope.appointmentMasterObj.contractId = null;
	     $scope.appointmentMasterObj.companyId = null;
	     $scope.fileObj = {};
	 	$scope.fileObj.file = null;
	 	if($scope.patientList[index].isDeath =='Y')
		{
	 		growl.warning("Patient is marked as dead",{
				title : WARNING_MSG
			});
	 		$scope.patientAdmittedFlag = false;
			$scope.initPatientSearchObj();
			$scope.initAppointmentMasterObj();
			$scope.initKinDetails();
			$scope.serviceOrderList = [];
			$scope.totalServiceAmt = 0;
			$scope.patientList=[];
			$scope.encounterList=[];
			$scope.secondTabLevel = 1 ;
			
			$scope.patientObj = {

					prefixId : '',
					firstName : '',
					middleName : '',
					lastName : '',
					genderId : '',
					countryCallingCode : '',
					email : '',
					nationalityId : '',
					raceId : '',
					maritalStatusId : '',
					mobileNumber : '',
					dob : '',
					identificationTypeId : '',
					identificationNumber : '',
					isVip : '',
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					updatedBy : $scope.userId,
					createdBy : $scope.userId,
					status : 'A',
					patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
					aliseName:'',
					identificationExpiryDate:'',
					occupationId:'',
					registrationTypeId:1,
					previousId:'',
					patientId:0,
					patientDetailsId:'',
					whatsappCallingCode : '',
					whatsappNo:'',
					skypeId:'',
					address : '',
					countryId : '',
					stateId : '',
					districtId : '',
					cityId :'',
					areaId : '',
					zipCode : '',
					phoneCode : '',
					phoneNumber : '',
					permanentAddress:'',
					permanentCountryId:'',
					permanentStateId:'',
					permanentDistrictId:'',
					permanentCityId:'',
					permanentAreaId:'',
					permanentZipCode:'',
					permanentPhoneCode:'',
					permanentPhoneNumber:'',
					fileSequenceNumber:'',
					bloodGroup:'',
					createdDate:'',
					isAlreadyExist:'N'
				};

				 $scope.patientAdmittedFlag = false;

			
			 $scope.patientAdmittedFlag = false;

				$scope.ageDay = 0;
				$scope.ageMonth = 0;
				$scope.ageYear = 0;
				
				if($scope.unitCountryId > 0)
				{
				  $scope.patientObj.countryId  = $scope.unitCountryId;
				  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
				  $scope.patientObj.stateId  =  $scope.unitStateId;
				  $scope.patientObj.districtId = $scope.unitDistrictId;
				  $scope.patientObj.cityId = $scope.unitCityId;
				  $scope.patientObj.areaId = $scope.unitAreaId;
				  $scope.patientObj.zipCode = $scope.unitZipCode;
				  $scope.getStateByCountry($scope.patientObj.countryId,'L');
				  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
				  $scope.getDistrictByState($scope.patientObj.stateId,'L');
				  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
				  $scope.getAreaByCity($scope.patientObj.cityId,'L');
				  
				  $scope.selectFlag = false;
				}
				
	 	 return false;
		}
		if($scope.patientList[index].admissionId > 0)
			{
			



			if($scope.patientList.length > 0)
				{
			    	$scope.visitTypeToCheckOPORIP = parseInt($scope.patientList[index].visitTypeId);
				}

				 switch (parseInt($scope.patientList[index].visitTypeId)) 
				 {
		            case 2:
			            	var message = "";
			            	if($scope.unitId==$scope.patientList[index].admissionUnitId)
			            		message = "Patient is already admitted in IP...!!";
			            	else
			            		message = "Patient is already admitted in another unit...!!";
			            	
		            		growl.warning(message,{
	    						title : WARNING_MSG
	    					});
			            	//$scope.patientList = [];
			            	//return false;
		                break;
		            case 3:
		            	
		            	if($scope.unitId==$scope.patientList[index].admissionUnitId)
		            		message = "Patient is already admitted in Day Care...!!";
		            	else
		            		message = "Patient is already admitted in another unit...!!";
		            	
		            	growl.warning(message,{
    						title : WARNING_MSG
    					});
		            	//$scope.patientList = [];
		            	//return false;
		                break;
		            case 4:
		            	
		            	if($scope.unitId==$scope.patientList[index].admissionUnitId)
		            		message = "Patient is already admitted in ER...!!";
		            	else
		            		message = "Patient is already admitted in another unit...!!";
		            	
		            	growl.warning(message,{
    						title : WARNING_MSG
    					});
		            	//$scope.patientList = [];
		            	//return false;
		                break;
		            default:
	
		        }
				 
				 $scope.patientAdmittedFlag = true;
			}else{
				 $scope.patientAdmittedFlag = false;
			}
	/*}*/
	/*else{*/ //ELSE OPEN
		var currentDate = moment(new Date()).format('YYYY-MM-DD');
		if($scope.patientList[index].identificationExpiryDate !=null && currentDate > $scope.patientList[index].identificationExpiryDate)
		{
			growl.warning("Patient's ID number has expired. Kindly update patient's ID number..!!",{
				title : WARNING_MSG
			});
		}
		
		$scope.appointmentMasterObj.encounterTypeId = $scope.encounterTypeList.length==1 ? $scope.encounterTypeList[0] : "";
		$scope.isRedirectAppointment=false;
		$scope.EHCpackageId = '';
		$scope.search = '';
		$scope.reviewServiesList =[];
		$scope.serviceOrderList = [];
		$scope.totalServiceAmt = 0;
		$scope.selectedOrderDetailIdsList = [];
		//$scope.tab = 1;
		$scope.secondTabLevel = 1 ;
		$scope.abscondPatientFlag="";
		$scope.encounterList = [];
		if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N')
		{
			$rootScope.startLoader();
			$scope.initReferralObj();
			$scope.encounterDetailsObj = {};
			$scope.appointmentMasterObj.appointmentId = null;
			//$scope.appointmentMasterObj.isCyclicAppointment=$scope.patientList[index].isCyclicAppointment;
			//$scope.appointmentId=$scope.patientList[index].appointmentId;
			$scope.appointmentMasterObj.deptId = null;
			$scope.appointmentMasterObj.doctorId = null;
			$scope.appointmentMasterObj.patientClassId = '';		
			$scope.slotId = null;
			$scope.appointmentMasterObj.encounterTime = "";
			$scope.appointmentMasterObj.systemGeneratedVisitTypeId=null;
			$scope.appointmentMasterObj.userDefinedVisitTypeId=null;
			$scope.patientDetails.age=($scope.patientList[index].age);
			$scope.patientDetails.patientId=($scope.patientList[index].patientId);
			$scope.patientDetails.patientName=($scope.patientList[index].patientName);
			$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
			$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
			$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
			$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
			$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
			$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
			$scope.patientDetails.videoCallUrl = ($scope.patientList[index].videoCallUrl);
			$scope.patientDetails.email = ($scope.patientList[index].email);
			$scope.patientDetails.patientTypeId=($scope.patientList[index].patientType);
			$scope.patientDetails.firstName=($scope.patientList[index].firstName);
			$scope.patientDetails.lastName=($scope.patientList[index].lastName);
			$scope.patientDetails.middleName=($scope.patientList[index].middleName);
			$scope.patientDetails.mobileNumber=($scope.patientList[index].mobileNo);
			$scope.patientDetails.createdDate=($scope.patientList[index].createdDate);
//			$scope.countryCallingCode=($scope.patientList[index].countryCallingCode);
			$scope.patientDetails.fileSequenceNumber=($scope.patientList[index].fileSequenceNumber);
			$scope.patientDetails.bloodGroup=($scope.patientList[index].bloodGroup);
			$scope.objPosMachine.mobileNumberForPOS = $scope.patientList[index].mobileNo;
			if($scope.patientDetails.mobileNumber!= null){
				$scope.mobileNumber = parseInt($scope.patientDetails.mobileNumber);
				$scope.razorPayObject.razorMobileNo = $scope.patientDetails.mobileNumber;
				 $scope.countryCallingCode = parseInt($scope.patientList[index].countryCallingCode);
			}
			
			$scope.patientDetails.registrationTypeId = ($scope.patientList[index].registrationTypeId);
			
			$scope.appointmentMasterObj.patientId=$scope.patientList[index].patientId;
			$scope.appointmentMasterObj.patientCategoryId=$scope.patientList[index].patientCategoryId;
			
			$scope.getKinDetailsByPatientId();
			$scope.kinDetailsList[0].patientId=$scope.appointmentMasterObj.patientId;
			$scope.getLastEncounterDetails('Y');
			$scope.registrationTypeId = $scope.patientList[index].registrationTypeId;
			//alert($scope.registrationTypeId);
			// added for pop up to show or not 
			$scope.documentUploadPopUp  = true;
			$scope.getAppointmentDetailsByPatientId(index,$scope.patientList[index].patientId);
			var data ={
					orgId:$scope.organizationId,
					unitId : $scope.unitId,
					age : 32,
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
								}
	
							});
	
			if($scope.corEmpVerificationFlag){
				$scope.enableDisabledApply=false;
			}else{
				$scope.enableDisabledValidated=false;
				$scope.enableDisabledApply=false;
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
					'companyId':$scope.selfPayeeIdCookies,
					'payeeId' : $scope.selfPayeeIdCookies,
					'tariffId':$scope.defaultSelfTariffId,
					'contractId':null,
					'insuredAmount' : ''
				}];
			
			$scope.isReviFlg = true;
			$scope.selectedOrderDetailIdsList = [];
			$scope.getReviewappoinemtnByPatient(true);
			 $scope.patientSerachHideAndShow = false;
			$(".patienttSearchResult").toggle();
			$rootScope.stopLoader();
			}else
			{
				$scope.appointmentMasterObj.patientId = $scope.patientList[index].patientId;
				$scope.abscondIndex=index;
				$scope.abscondPatientFlag='Y';
				angular.element('#abscondedPatientPopUp').modal('show');
			}
		
		$scope.getOTReservationList($scope.patientList[index].patientId);
		
		if($scope.appointmentMasterObj.encounterTypeId!=null && $scope.appointmentMasterObj.encounterTypeId!="" && $scope.appointmentMasterObj.encounterTypeId>0)
			$scope.enableDisableDoctor($scope.appointmentMasterObj.encounterTypeId);
		
	}
	$scope.cancelAbscondedPatient=function(){
		$rootScope.stopLoader();
		$scope.initPatientSearchObj();
		$scope.initAppointmentMasterObj();
		$scope.initKinDetails();
	}
	
	
	$scope.creatingEncounterForAbscondedPatient=function(){
		var index=$scope.abscondIndex;
		
		$rootScope.startLoader();
		$scope.initReferralObj();
		$scope.encounterDetailsObj = {};
		$scope.appointmentMasterObj.appointmentId = null;
		$scope.appointmentMasterObj.deptId = null;
		$scope.appointmentMasterObj.doctorId = null;
		$scope.appointmentMasterObj.patientClassId = '';		
		$scope.slotId = null;
		$scope.appointmentMasterObj.encounterTime = "";
		$scope.appointmentMasterObj.systemGeneratedVisitTypeId=null;
		$scope.appointmentMasterObj.userDefinedVisitTypeId=null;
		$scope.patientDetails.age=($scope.patientList[index].age);
		$scope.patientDetails.patientId=($scope.patientList[index].patientId);
		$scope.patientDetails.patientName=($scope.patientList[index].patientName);
		$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
		$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
		$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
		$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
		$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
		$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
		
		$scope.patientDetails.firstName=($scope.patientList[index].firstName);
		$scope.patientDetails.mobileNumber=($scope.patientList[index].mobileNo);
		$scope.patientDetails.registrationTypeId = ($scope.patientList[index].registrationTypeId);
		
		$scope.appointmentMasterObj.patientId=$scope.patientList[index].patientId;
		$scope.appointmentMasterObj.patientCategoryId=$scope.patientList[index].patientCategoryId;
		
		$scope.getKinDetailsByPatientId();
		$scope.kinDetailsList[0].patientId=$scope.appointmentMasterObj.patientId;
		
		$scope.registrationTypeId = $scope.patientList[index].registrationTypeId;
		// added for pop up to show or not 
		$scope.documentUploadPopUp  = true;
		
		var data ={
				orgId:$scope.organizationId,
				unitId : $scope.unitId,
				age : 32,
				sexId : $scope.patientDetails.genderId
		}
		if($scope.patientList[index].appointmentId != null && $scope.patientList[index].appointmentId >0 ){
			$scope.alreadyPatient = 1;
			var URI = BASE_URL + ROOT_URL + "/api/opd/searchInProcessAppointmentWeb/"+$scope.patientList[index].appointmentId;
			BillingGenericService
					.serviceAction(METHOD_GET, URI, {},
							NOTIFICATION_MSG_STATUS_TRUE)
					.then(
							function(response) {
								$scope.alreadyAppointment = {};
								// $rootScope.startLoader();
								if (response.data.status == "success") {
									$scope.alreadyAppointment = response.data.listObject[0];
									$scope.checkAppointment();
								}
							});
				}
		
		var URI = BASE_URL + ROOT_URL + "/api/packages/ehcPackages";
		BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							$scope.packageEHCList = [];
							if (response.data.status == "success") {
								$scope.packageEHCList = response.data.listObject;
							}

						});		
		
		if($scope.corEmpVerificationFlag){
//			$scope.enableDisabledValidated=true;
			$scope.enableDisabledApply=false;
		}else{
			$scope.enableDisabledValidated=false;
			$scope.enableDisabledApply=false;
		}
		$scope.getReviewappoinemtnByPatient(true);
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
				'companyId':$scope.selfPayeeIdCookies,
				'payeeId' : $scope.selfPayeeIdCookies,
				'tariffId':$scope.defaultSelfTariffId,
				'contractId':null,
				'insuredAmount' : ''
			}];
		 $scope.patientSerachHideAndShow = false;
		$(".patienttSearchResult").toggle();
		$rootScope.stopLoader();
	}
	
	//Idetification List
	$scope.getIdentificationList = function(){
		var URI = BASE_URL + ROOT_URL+ GETACTIVEIDENTIFICATION;
		GenericService.serviceAction("GET", URI, "").then(function(response) {
			$scope.identificationList = [];
			if (response.data.status == "success") {
				$scope.identificationList = response.data.listObject;

			}
		});
	}
	$scope.getIdentificationList();
	// Country List
	$scope.getCountryList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVECOUNTRYLIST+$scope.organizationId;
		BillingGenericService.serviceAction("GET", URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.countryList = [];
					if (response.data.status == "success") {
						$scope.countryList = response.data.listObject;
					}
				});
	}
	$scope.getCountryList();
	
	
	
	$scope.getVisitTypeMasterList = function(){
		try{
			var URI = BASE_URL + ROOT_URL + VISIT_TYPE_MASTER + UNIT + SLASH + $scope.unitId + ORG + SLASH + $scope.organizationId;
			BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				console.log(response);
				if(response.data.status == "success")
					$scope.visitTypeMasterList = response.data.listObject;
			});
		}catch (e) {
			console.log("Exception",e.message);
		}
	}
	$scope.getVisitTypeMasterList();

	$scope.getSourceMasterList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEPATIENTSOURCELIST+"/"+$scope.organizationId;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.sourceMasterList = [];
					if (response.data.status == "success") {
						$scope.sourceMasterList = response.data.listObject;
					}
				});
	}
	$scope.getSourceMasterList();
	
	$scope.getReferralList = function(){
	var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
	var obj={organizationId : $scope.organizationId};
		BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
				$scope.referralTypeList = [];
				if (response.data.status == "success") {
					$scope.referralTypeList = response.data.listObject;
				}
		});
	};
	$scope.getReferralList();
	
/*	$scope.getReferralMasterList = function(referralTypeId){
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
        			referralTypeId:$scope.appointmentMasterObj.referralTypeId,
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
			console.log(info);
			$scope.appointmentMasterObj.referralId = info.referralId;
    	}

	
	
	
	
	//get District by State 
	/*$scope.getDistrictByState = function(id) {
		var data = {
			stateId : id
		};
		var URI = BASE_URL + ROOT_URL + GETDISTRICTLISTBYSTATEID;
		GenericService.serviceAction("POST", URI, data).then(function(response) {	
			$scope.districtList = [];
			if (response.data.status == "success") {
				$scope.districtList = response.data.listObject;
			}
		});

	}
	
	//get City By District
	$scope.getCityByDistrict = function(id) {
		var data = {
			districtId : id
		};
		var URI = BASE_URL + ROOT_URL + GETCITYLISTBYDISTRICTID;
		GenericService.serviceAction("POST", URI, data).then(function(response) {
				$scope.cityList = [];
				if (response.data.status == "success") {
					$scope.cityList = response.data.listObject;
				}
			});

	}
	
	//get Area By City
	$scope.getAreaByCity = function(id) {
		var data = {
			cityId : id
		};
		var URI = BASE_URL + ROOT_URL + GETAREALISTBYCITY;
		GenericService.serviceAction("POST", URI, data).then(function(response) {
			$scope.areaList = [];
			if (response.data.status == "success") {
				$scope.areaList = response.data.listObject;
			}
		});
	}*/
	
	//Prefix list
	$scope.getPrefixList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEPREFIXLIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(
				function(response) {
					$scope.prefixList = [];
					if (response.data.status == "success") {
						angular.forEach(response.data.listObject, function(prefix) {
							if(prefix.isShowPatientRegistration == true){
								$scope.prefixList.push(prefix);
							}
						});
					}
				});
	}
	$scope.getPrefixList();
	
	//get Reason List
	$scope.getReasonList = function(){
		
	var URI = BASE_URL + ROOT_URL
			+ GETACTIVEREASONLIST;

	BillingGenericService.serviceAction(METHOD_POST, URI,$scope.commonObj,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log("ReasonList",response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.reasonList = response.data.listObject;
							
						});
	}
	$scope.getReasonList();
	
//relation list
	$scope.getRelationshipList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVERELATIONLIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.relationList = [];
					if (response.data.status == "success") {
						$scope.relationList = response.data.listObject;
					}
				});
	}
	$scope.getRelationshipList();
	
	
	//http://localhost:8080/HIS-WAR/adt/getDoctorListAganistSpeciality
	$scope.getDoctorBySpeciality = function(id,type) {
		
		$scope.serviceOrderList = [];
		if(type == 1){
		$scope.appointmentMasterObj.doctorId = '';
		$scope.appointmentMasterObj.encounterTime = '';
		$scope.slotId = '';
		}
		var selectedSplObject = $.grep($scope.specialityMasterList, function (splList) {
            return splList.specialityId == id;
        });
		
		$scope.deptWiseAppointment = selectedSplObject[0].deptWiseAppointment;
		$scope.isNursingAssessmentRequired = selectedSplObject[0].isNursingAssessmentRequired;
		
		
		if($scope.deptWiseAppointment=="Y"){
			
			$scope.allowedOverBookingSlots = selectedSplObject[0].allowedOverbookingSlots;
			
			if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == null)
				{
					
					/*growl.warning("Please Select patient",{
						title : WARNING_MSG
					});
					return;*/

					$scope.$broadcast('show-errors-check-validity');
					// $scope.secondTabLevel=1;
			        if($scope.patientRegistrationform.$valid){
			        	//alert("valid");
			        	
			        	var contryCode=$scope.patientObj.countryCallingCode;
			        	 // validating country code
						 var regEx = /^[+-]?\d+$/;
						      if(!regEx.test(contryCode)){
						    	  growl.warning("Please enter valid country calling code",{
										title : 'Warning !'
										});
										return false;
						      } 
			        	
			        	var mobileNo=$scope.patientObj.mobileNumber;
			        	if(contryCode==91){
			        		if(mobileNo.length==$scope.registrationFieldObj.mobileDigit){
			        			
								}else{
									growl.warning("Please enter valid mobile number",{
										title :'Warning !'
										});
										$('#mobileNo').focus();
										return false;
			        		}
			        	}else{
				        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
				        			
								}else{
									growl.warning("Please enter valid mobile number",{
										title : 'Warning !'
										});
										$('#mobileNo').focus();
										return false;
			        		}
			        	}


			        	if($scope.patientObj.patientCategoryId==null || $scope.patientObj.patientCategoryId==undefined||$scope.patientObj.patientCategoryId==""){
			        		growl.warning("Please select patient category",{
										title : 'Warning !'
										});
										
										return false;
			        	}
						/*if($scope.patientRegistrationform2.$valid){*/
			        		 if($scope.patientObj.phoneCode !='' && $scope.patientObj.phoneCode!=undefined && $scope.patientObj.phoneCode!=null){
			        			 var phoneCode=$scope.patientObj.phoneCode;
					        	 // validating country code
								 var regEx = /^[+-]?\d+$/;
								      if(!regEx.test(phoneCode)){
								    	  growl.warning("Please enter valid country calling code",{
												title : 'Warning !'
												});
												return false;
								      } 
			        		 }
			        		 //test start
			        		 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
		       			 }
			        		 if($scope.patientObj.permanentZipCode !='' && $scope.patientObj.permanentZipCode!=undefined && $scope.patientObj.permanentZipCode!=null){
		       			 }
			        		 if($scope.patientObj.permanentPhoneCode !='' && $scope.patientObj.permanentPhoneCode!=undefined && $scope.patientObj.permanentPhoneCode!=null){
			        			 var permanentPhoneCode=$scope.patientObj.permanentPhoneCode;
					        	 // validating country code
								 var regEx = /^[+-]?\d+$/;
								      if(!regEx.test(permanentPhoneCode)){
								    	  growl.warning("Please enter valid country calling code",{
												title : 'Warning !'
												});
												return false;
								      } 
			        		 }
			        		 if($scope.patientObj.email == null){
			        			 $scope.patientObj.email = "";
			        		 }
			        		 if($scope.patientObj.email != "")
			           			{
				           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				           		 var email = $scope.patientObj.email;
				           		    if (!filter.test(email)) {

			        				    growl.error(EMAIL_MSG,{
											title : ERROR_MSG

										});
			        				    $('#email').focus();
			        				    return false;
				           		    }
				           		    else{
				           		    	if($scope.patientObj.phoneNumber == undefined)
				           		    			$scope.patientObj.phoneNumber='';
				           		    	if($scope.patientObj.phoneNumber == null)
				           		    			$scope.patientObj.phoneNumber='';
				           		     if($scope.patientObj.phoneNumber != '')
						        		{
						        		}
				           		     else
				           		    	 {
				           		    	 }
				           		    }
			           			}
			        		 else
			        			 {
			        			 if($scope.patientObj.phoneNumber == undefined)
			        				 $scope.patientObj.phoneNumber='';
			        			 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
			        			 }
			        			 }
			        		
			        		 //test end
			        			 
			        }else{
			        	$scope.appointmentMasterObj.deptId = "";
			        	growl.warning(" Please select patient or add patient details..!! ",{
							title : WARNING_MSG
						});
			        	return false;
			        }

					$scope.appointmentMasterObj.doctorId = null;
					var  data = {
					    	
				    		 unitId          :$scope.unitId,
							 organizationId  :$scope.organizationId,
				    		 patientId : $scope.appointmentMasterObj.patientId,
				    		 specialityId : id,
				    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
				    		 
				     };
				     
					if($scope.appointmentMasterObj.encounterTypeId==6)
					{
						
						 $scope.appointmentMasterObj.systemGeneratedVisitTypeId=($scope.appointmentMasterObj.encounterTypeId).toString();
						 $scope.appointmentMasterObj.userDefinedVisitTypeId=($scope.appointmentMasterObj.encounterTypeId).toString();
						
					}else{
						var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
					     BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					     
					    $scope.appointmentMasterObj.systemGeneratedVisitTypeId=response.data.toString();
					    $scope.appointmentMasterObj.userDefinedVisitTypeId=response.data.toString();
					    $scope.getBillDetails(); 
					     });
					}
					
				  
			        
				}
			else{
			$scope.appointmentMasterObj.doctorId = null;
			var  data = {
			    	
		    		 unitId          :$scope.unitId,
					 organizationId  :$scope.organizationId,
		    		 patientId : $scope.appointmentMasterObj.patientId,
		    		 specialityId : id,
		    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
		    		 
		     };
		     
			if($scope.appointmentMasterObj.encounterTypeId==6)
			{
				
				 $scope.appointmentMasterObj.systemGeneratedVisitTypeId=($scope.appointmentMasterObj.encounterTypeId).toString();
				 $scope.appointmentMasterObj.userDefinedVisitTypeId=($scope.appointmentMasterObj.encounterTypeId).toString();
				
			}else{
				var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
			     BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			     
			    $scope.appointmentMasterObj.systemGeneratedVisitTypeId=response.data.toString();
			    $scope.appointmentMasterObj.userDefinedVisitTypeId=response.data.toString();
			    $scope.getBillDetails(); 
			     });
			}
			
		  }
			
		}
		
		
		
		var data = {
			specialityId : id,
			unitId : $scope.unitId,
			organizationId : $scope.organizationId
		};
		
		
		var URI = null;
		
		if($scope.isAppointmentRequired=='N' || $scope.deptWiseAppointment=="Y")
			URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
		else
			URI = BASE_URL + ROOT_URL + "/api/global/appoitments/doctors";
		
		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							console.log(response);
							$scope.doctorList = [];
							// $rootScope.startLoader();
							if (response.data.status == "success"){
								$scope.doctorList = response.data.listObject;
							console.log('DoctorList',$scope.doctorList);
							if(type==3){
								
								 $scope.getBillDetails(); 
							}
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
	//kin details
	$scope.getKinDetailsByPatientId = function(){
		console.log("$scope.appointmentMasterObj.patientId",$scope.appointmentMasterObj);
		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + PATIENT + SLASH + $scope.appointmentMasterObj.patientId  + UNIT + SLASH +  $scope.unitId  + ORG + SLASH + $scope.organizationId;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success") {
				$scope.kinsList = response.data.listObject;
			}
		});
	}
	
	
	$scope.getReferralTypeDesc=function(referral){
		
		$scope.referralDesc=referral.desc;
		
	}
	
	$scope.getAppointmentPopup = function()
	{
		$scope.slotDetailsList = [];
		if($scope.isAppointmentRequired == 'Y'){
			
			$scope.slotCheckTime = moment(new Date()).format("HH:mm");
			
			var data = {
					appointmentDate :moment(new Date()).format('YYYY-MM-DD'),
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
				};
			
		if($scope.appointmentMasterObj.encounterTypeId == 1||$scope.appointmentMasterObj.encounterTypeId ==6 || $scope.appointmentMasterObj.encounterTypeId ==9)
			{
			if($scope.deptWiseAppointment == "N"){
					if($scope.appointmentMasterObj.deptId > 0 && $scope.appointmentMasterObj.doctorId > 0)
					{
						
			
						angular.element('#time_slot_modal').modal('show');
						
								data.specialityId = $scope.appointmentMasterObj.deptId;
								data.doctorId = $scope.appointmentMasterObj.doctorId;
								data.slotTypeId = 1;
			
						console.log(data);
						//return false;
						}
						else{
				
							growl.warning("Please Select Department and Doctor",{
								title : WARNING_MSG
							});
							return;
						}
			}
			else
				{
					if($scope.appointmentMasterObj.deptId > 0)
					{
						
			
						angular.element('#time_slot_modal').modal('show');
						
								data.specialityId = $scope.appointmentMasterObj.deptId;
								//data.doctorId = $scope.appointmentMasterObj.doctorId;
								data.slotTypeId = 4;
			
						console.log(data);
						//return false;
						}
						else{
				
							growl.warning("Please Select Department",{
								title : WARNING_MSG
							});
							return;
						}
				}
			}
		else if($scope.appointmentMasterObj.encounterTypeId == 2)
			{
			if($scope.appointmentMasterObj.deptId > 0 && $scope.appointmentMasterObj.subDeptId != '' && $scope.appointmentMasterObj.modalityId > 0)
			{
				
						var array = $scope.appointmentMasterObj.subDeptId.split('_');
				        angular.element('#time_slot_modal').modal('show');
						data.specialityId = $scope.appointmentMasterObj.deptId;
						data.subSpecialityId = array[1];
						data.modalityId = $scope.appointmentMasterObj.modalityId;
						data.visitTypeId= 1;
						data.slotTypeId = 2;
	
				console.log(data);
				//return false;
					
				}
				else{
		
					growl.warning("Please Select Department, Sub Department and Modality..!!",{
						title : WARNING_MSG
					});
					return;
				}
			}
		
		var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
		BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success") {
							 
								  $scope.slotDetailsList = response.data.listObject;
									console.log($scope.slotDetailsList);
						       if($scope.slotDetailsList.length ==  0){
								   growl.warning("Slots not available",{
										title : WARNING_MSG
									});
									return;
							  }
							}

						});
		
		}
	}
	
	//Function For Set Value On Popup
	$scope.getSlotDetailsPopup = function(item) {
		
		//$scope.regiType = "1";
		$rootScope.startLoader();
		var data = {
				specialityId : $scope.appointmentMasterObj.deptId,
				doctorId : $scope.appointmentMasterObj.doctorId,
				patientId : $scope.appointmentMasterObj.patientId,
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				slotId : item.slotId,
				appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
				appointmentDate : item.appointmentDate2,
				
			};

		console.log(data);

		if($scope.encounterDetailsObj.appointmentId > 0){

			$scope.doctorName = item.doctorName;
			$scope.doctorId = item.doctorId;
			$scope.specialityname = item.specialityName;
			$scope.fromTime = item.fromTimeString2;
			$scope.toTime = item.toTimeString2;
			$scope.appointmentDate = item.appointmentDate;
			$scope.slotId = item.slotId;
			
			if(item.slotStatusId==4 || item.slotStatusId==6)
			{
				$scope.slotStatusId = 6;
			}else
				$scope.slotStatusId = 4;
			
			$scope.priorityId  = 1;
			$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
			//$scope.appointmentMasterObj.encounterTime = item.fromTimeString;
			$scope.appointmentMasterObj.encounterTime = item.originalFromTime;
			//$scope.appointmentMasterObj.appoitmentDate = item.appointmentDate;
			 $scope.encounterDetailsObj.appoitmentDate = item.appointmentDate;
			$rootScope.stopLoader();
			
	}else{
		
		if( $scope.appointmentMasterObj.patientId > 0){
		var URI = BASE_URL + ROOT_URL + "/api/opd/appointment/check";
		BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							$scope.slotDetailsList = [];
							// $rootScope.startLoader();
							if (response.data.code == 409) {
								
								growl.warning(response.data.message,{
									title : WARNING_MSG
								});
								
								$scope.initPatientSearchObj();
								$scope.initAppointmentMasterObj();
								$scope.initKinDetails();
								$scope.serviceOrderList = [];
								$scope.totalServiceAmt = 0;
								$scope.secondTabLevel = 1 ;
								$rootScope.stopLoader();
								return false;
								
							}else{
								$scope.doctorName = item.doctorName;
								$scope.doctorId = item.doctorId;
								$scope.specialityname = item.specialityName;
								$scope.fromTime = item.fromTimeString2;
								$scope.toTime = item.toTimeString2;
								$scope.appointmentDate = item.appointmentDate;
								$scope.slotId = item.slotId;
								
								if(item.slotStatusId==4 || item.slotStatusId==6)
								{
									$scope.slotStatusId = 6;
								}else
									$scope.slotStatusId = 4;
								
								$scope.priorityId  = 1;
								$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
								//$scope.appointmentMasterObj.encounterTime = item.fromTimeString;
								$scope.appointmentMasterObj.encounterTime = item.originalFromTime;
								//$scope.appointmentMasterObj.appoitmentDate = item.appointmentDate;
								 $scope.encounterDetailsObj.appoitmentDate = item.appointmentDate;
								$rootScope.stopLoader();
							}

						});
	}else{
		$scope.doctorName = item.doctorName;
		$scope.doctorId = item.doctorId;
		$scope.specialityname = item.specialityName;
		$scope.fromTime = item.fromTimeString2;
		$scope.toTime = item.toTimeString2;
		$scope.appointmentDate = item.appointmentDate;
		$scope.slotId = item.slotId;
		
		if(item.slotStatusId==4 || item.slotStatusId==6)
		{
			$scope.slotStatusId = 6;
		}else
			$scope.slotStatusId = 4;
		
		$scope.priorityId  = 1;
		$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
		//$scope.appointmentMasterObj.encounterTime = item.fromTimeString;
		$scope.appointmentMasterObj.encounterTime = item.originalFromTime;
		//$scope.appointmentMasterObj.appoitmentDate = item.appointmentDate;
		 $scope.encounterDetailsObj.appoitmentDate = item.appointmentDate;
		$rootScope.stopLoader();
	}
	
	}
	}
	
	
	$scope.checkDateIsBefore = function(slobObj)
	{
	     var endTime = moment(slobObj.originalFromTime, 'hh:mm');
	     var startTempTime = moment().format("HH:mm");
	     var startTime = moment(startTempTime, 'hh:mm');
	     
		if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > 0 && slobObj.slotPatientDetailsListsSize < slobObj.allowedOverBookingSlots)
			{
				//return 'bg-red1 cursor-pointer day-slot-div';
				return 'bg-orange cursor-pointer day-slot-div';
			}
		/*if(slobObj.isBlockedUnblocked == '2' && slobObj.allowedOverBookingSlots>0 && slobObj.slotPatientDetailsListsSize == slobObj.allowedOverBookingSlots)
			{
				//return 'bg-orange cursor-pointer day-slot-div';
				return 'bg-red1 cursor-pointer day-slot-div';
			}*/
		if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize == '0')
		{
			return 'bg-green cursor-pointer day-slot-div';
		}
		
	}
	
	 $scope.setPostCode = function(areaId,flag){
			
		 if(flag == 0 ){
			 
			 angular.forEach($scope.areaList,function(value,key){
					
					if(value.areaId==areaId)
					{
						$scope.patientObj.zipCode = value.postCode;
					}
				});
		 }else{
			 angular.forEach($scope.permanentAreaList,function(value1,key){
					
					if(value1.areaId==areaId)
					{
						$scope.patientObj.permanentZipCode = value1.postCode;
					}
				});
			 
		 }
		 $scope.selectFlag = true;
		 $scope.selectFlag1 = true;
         $scope.flgHideShow = false;
         $scope.testFlgPer =false;
         setTimeout(function(){
             //alert()
              $scope.selectFlag = false;
              $scope.selectFlag1 = false;
            },500);	
			
		}
	
	////Function For Set Value on assign patient on appointement
	$scope.getBookSlotDetails = function(item) {
		$scope.patientName = item.patientName;
		$scope.mobileNumber = item.mobileNumber;
		$scope.email = item.email;
		$scope.address = item.address;
		
		$scope.bookedPatientList = item.slotPatientDetailsList;
		console.log($scope.bookedPatientList);
	}
	
	$scope.getSlotsOfDate = function() {
		/*var dt = $('#datepicker').val();
		//alert(dt);
		//	alert(moment($('#datepicker').val()).format('DD-MM-YYYY'));
		var array = dt.split('/');
		var newDate = "";
		newDate = array[2] + '-' + array[1] + '-'
				+ array[0];*/

		//alert($scope.selectedSlotId);
			var data = {
				specialityId : $scope.specialityId,
				doctorId : $scope.doctorId,
				appointmentDate :moment(new Date()).format('YYYY-MM-DD'),
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				slotTypeId : $scope.selectedSlotId
			};

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
							}

						});

	}
	$scope.patientIdForReport = "";
	//Start For New Tab Bill
	$scope.getBillDetails = function()
	{
		
		if($scope.appointmentMasterObj.patientId > 0){
			var sponserList = [];
			angular.forEach($scope.paymentEntitleMentList, function (value, key) {
				
				var newArr = {};
				newArr.organizationId = $scope.organizationId ;
				newArr.unitId = $scope.unitId;
				newArr.visitTypeId = 1;
				newArr.paymentEntitlementId =value.paymentEntitlementId;
				newArr.payeeId = value.companyId;
				newArr.priorityId = 1;
				newArr.createdBy =$scope.userId;
				newArr.createdDate = $scope.currentDate;
				newArr.updatedBy = $scope.userId;
				newArr.updatedDate = $scope.currentDate;
				newArr.status = 'A';
				newArr.tariffId =value.tariffId;
				newArr.contractId =value.contractId;
				newArr.insuranceNumber = value.insuranceNumber;
				newArr.insuredAmount = value.insuredAmount;
				newArr.validityTo = value.endDate;
				newArr.validityFrom = value.startDate;
				newArr.claimNumber = value.claimNumber;
				if(value.companyId != $scope.selfPayeeIdCookies && value.priority == 1)
			    {
			     $scope.appointmentMasterObj.contractId = value.contractId;
			     $scope.appointmentMasterObj.companyId = value.companyId;
			     return false;
			    }
				
				sponserList.push(newArr);
			});
			$scope.appointmentMasterObj.patientSponsorDetailsDtosList = sponserList;
			
			if($scope.appointmentMasterObj.patientSponsorDetailsDtosList.length==1)
			{
				if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == 0){
					//$scope.tab=1;
					growl.warning("Please Select Patient",{
						title : WARNING_MSG
					});
					return;
				}
				if($scope.appointmentMasterObj.encounterTypeId == '' || $scope.appointmentMasterObj.encounterTypeId == 0 ){
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Encounter Type",{
						title : ERROR_MSG
					});
					return;
				}
				if(($scope.appointmentMasterObj.deptId == '' || $scope.appointmentMasterObj.deptId == 0) && $scope.appointmentMasterObj.encounterTypeId != 3 && $scope.appointmentMasterObj.encounterTypeId != 5){
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Department",{
						title : ERROR_MSG
					});
					return;
				}
				
				if($scope.appointmentMasterObj.encounterTypeId == 1 &&  $scope.deptWiseAppointment == "N")
					{
					
						if($scope.appointmentMasterObj.doctorId == '' || $scope.appointmentMasterObj.doctorId == undefined ){
							//$scope.tab=1;
							$scope.secondTabLevel = 1 ;
							growl.warning("Please Select Doctor",{
								title : ERROR_MSG
							});
							return;
						}
					}
				else if ($scope.appointmentMasterObj.encounterTypeId == 2)
					{
					$scope.appointmentMasterObj.userDefinedVisitTypeId="4";
						if($scope.appointmentMasterObj.subDeptId == '' ){
							//$scope.tab=1;
							$scope.secondTabLevel = 1 ;
							growl.warning("Please Select Sub Department",{
								title : ERROR_MSG
							});
							return;
						}
						if($scope.appointmentMasterObj.modalityId == '' || $scope.appointmentMasterObj.modalityId == 0){
							//$scope.tab=1;
							$scope.secondTabLevel = 1 ;
							growl.warning("Please Select Modality",{
								title : ERROR_MSG
							});
							return;
						}
					}
				else if($scope.appointmentMasterObj.encounterTypeId == 4)
					{
						if($scope.appointmentMasterObj.deptId == '' ){
							//$scope.tab=1;
							$scope.secondTabLevel = 1 ;
							growl.warning("Please Select Department",{
								title : ERROR_MSG
							});
							return;
						}
						if($scope.appointmentMasterObj.subDeptId == '' ){
							//$scope.tab=1;
							$scope.secondTabLevel = 1 ;
							growl.warning("Please Select Sub Department",{
								title : ERROR_MSG
							});
							return;
						}
					}
				
				
				//var array = $scope.appointmentMasterObj.subDeptId.split('_');
				
				//----------------------------------!@#$%^&*(
				var objForGettingDoctorServiceId = {
						doctorId 		: $scope.appointmentMasterObj.doctorId,
						specialityId 	: $scope.appointmentMasterObj.deptId,
						unitId 			: $scope.unitId,
						organizationId 	: $scope.organizationId
					};
				$scope.currentDate = new Date();
				$scope.currentDate = moment(
						$scope.currentDate).format(
						'DD-MM-YYYY HH:mm:ss');
				$scope.currentDateAppDate = moment(
						new Date()).format(
						'YYYY-MM-DD HH:mm:ss');
				//appointmentObject
				if($scope.appointmentMasterObj.appointmentId > 0)
					{
					$scope.appointmentMasterObj.appointmentObject  = null;
					$scope.appointmentMasterObj.unitTokenNumber =  $scope.encounterDetailsObj.tokenNumber;
					}
				else{
					$scope.appointmentMasterObj.unitTokenNumber =  null;
					$scope.appointmentMasterObj.currentDate =  moment(new Date()).format('YYYY-MM-DD');
					if($scope.isAppointmentRequired == 'Y' && $scope.appointmentMasterObj.encounterTypeId != 4)
						{
							/*if(this.slotId == '' && $scope.appointmentMasterObj.encounterTypeId != 5)
								{
								//$scope.tab=1;
								$scope.secondTabLevel = 1 ;
								growl.warning(" Please select the time slot ",{
									title : WARNING_MSG
								});
								return;
								}
							else
								{*/
									var dataAppointmentObj = {
											patientId : $scope.appointmentMasterObj.patientId,
											remark : $scope.appointmentMasterObj.remark,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											createdDate : $scope.currentDate,
											updatedDate : $scope.currentDate,
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											status : 'A',
											appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
											priorityId : $scope.priorityId,
											firstName : $scope.patientDetails.firstName,
										    mobileNumber :$scope.patientDetails.mobileNumber,
										    generateToken : 'Y',
										    appointmentDate : $scope.currentDateAppDate,
										    isDirectEncounter : 'Y',
											ehcPackageId : $scope.EHCpackageId
											//allowedOverBookingSlots : $scope.allowedOverBookingSlots,
										}
									
									if($scope.appointmentMasterObj.encounterTypeId != 5)
										{
										   dataAppointmentObj.slotId = $scope.slotId;
										   dataAppointmentObj.appointmentDate2 = $scope.appointmentDate;
										   dataAppointmentObj.fromTimeString = $scope.fromTime;
										   dataAppointmentObj.slotStatusId = $scope.slotStatusId;
										   dataAppointmentObj.doctorName = $scope.doctorName;
										   dataAppointmentObj.isFromEncounter = 'Y';
										   dataAppointmentObj.doctorId = $scope.doctorId;
										}
									else
										{
										  dataAppointmentObj.slotId = null;
										  dataAppointmentObj.appointmentDate2 = null;
										  dataAppointmentObj.fromTimeString =null;
										  dataAppointmentObj.slotStatusId =null;
										  dataAppointmentObj.doctorName = null;
										  dataAppointmentObj.isFromEncounter = null;
										}
									if($scope.appointmentMasterObj.encounterTypeId == 3)
									{
										
										if($scope.EHCpackageId == 0 || $scope.EHCpackageId == null)
										{
											//$scope.tab=1;
											$scope.secondTabLevel = 1 ;
											growl.warning("Please Select Package",{
												title : ERROR_MSG
											});
											return;
										}else
											{
											 dataAppointmentObj.ehcPackageId = $scope.EHCpackageId;
											}
									}
									$scope.appointmentMasterObj.appointmentObject  = dataAppointmentObj;
								//}
						}
					
				}
				
				if($scope.appointmentMasterObj.encounterTypeId == 4)
				{
					$scope.appointmentMasterObj.appointmentObject  = null;
				}
				/*if($scope.appointmentMasterObj.kinDetailsId == "" && $scope.kinDetailsList[0].kinName == ""){
					$scope.tab=2;
					//$state.go('encounter.nextOfKin');
					
					growl.warning("Please add kin",{
						title : WARNING_MSG
					});
					return;
				}*/
				
				if($scope.appointmentMasterObj.encounterTypeId == 5)
				{
					$scope.appointmentMasterObj.specialityId = $scope.casualtySpecialityId;
					$scope.appointmentMasterObj.deptId = $scope.casualtySpecialityId;//need to add casualty speciality Id from unitApplicationConfig
				}
				else
					{
					 $scope.appointmentMasterObj.specialityId = $scope.appointmentMasterObj.deptId;
					}
				
				//var array = $scope.appointmentMasterObj.subDeptId.split('_');
				if($scope.appointmentMasterObj.encounterTypeId == 2 || $scope.appointmentMasterObj.encounterTypeId == 4){
				var array = $scope.appointmentMasterObj.subDeptId.split('_');
				$scope.appointmentMasterObj.subDeptId = array[1];
				//$scope.appointmentMasterObj.subSpecialityId = $scope.appointmentMasterObj.subDeptId;
				}
				
			      if($scope.appointmentMasterObj.patientClassId == '' || $scope.appointmentMasterObj.patientClassId == null || $scope.appointmentMasterObj.patientClassId == undefined)
		          {
		          $scope.appointmentMasterObj.patientClassId = null;
		          }
				
				isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
				  if (isPriorityZeroExists) {
					
					  growl.warning("Please select priority of all paymententitlement list.",{	
						title : "WARNING"
					});
					 //$scope.tab = 3;
					  //$scope.secondTabLevel = 3 ;
					return;
					
				  }
				  else{
				
					  $scope.kinDetailsFlag =false;
			        	angular.forEach($scope.kinDetailsList, function( value, index){
							if(value.kinName != ''){
						        	var contryCode=$scope.countryCallingCode;
						        	var mobileNo=value.mobileNo;
						        	if(mobileNo!=''){
						        		if(mobileNo.length==$scope.kinDetails.mobileDigit){
						        			
											}else{
												growl.error("Please enter valid mobile number",{
													title : ERROR_MSG
													});
													$('#mobileNo').focus();
													$scope.setSecondTabLevel(2);
													$scope.kinDetailsFlag =true;
													return false;
											}
						        	}else {
							        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
							        			
											}else{
												growl.error("Please enter valid mobile number",{
													title : ERROR_MSG
													});
													$('#mobileNo').focus();
													$scope.setSecondTabLevel(2);
													$scope.kinDetailsFlag =true;
													return false;
						        		}
						        	}
							}
						});
			        	
			        	if($scope.kinDetailsFlag){
							return false;
						}
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/bill/details/encounter";
				
				if($scope.selfFlag == "Y")
					{
					 $scope.appointmentMasterObj.slotId =$scope.slotId;
					}
				
				//$scope.appointmentMasterObj.encounterTime =  moment(new Date()).format("HH:mm");
				
				
				$scope.appointmentMasterObj.deptWiseAppointment = $scope.deptWiseAppointment;
				$scope.appointmentMasterObj.referralMasterDto = $scope.referralData;
				console.log('Encounter obj',$scope.appointmentMasterObj);
				$rootScope.startLoader();
				BillingGenericService.serviceAction(METHOD_POST, URI, $scope.appointmentMasterObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					var count=0;
					if (response.data.status == "success") {
								console.log("response.data.object",response.data.object);
								//$scope.tab=4;
								//$scope.secondTabLevel = 4 ;
								$scope.docName = "-";
								if($scope.appointmentMasterObj.doctorId > 0)
									{
									   $scope.docDtls = $.grep($scope.doctorList, function (disCat) {
							            return disCat.doctorId == $scope.appointmentMasterObj.doctorId;
							           })[0];
									   
									   $scope.docName = "Dr. "+$scope.docDtls.firstName+" "+$scope.docDtls.lastName;


									var aiDoctorFlag = (
										$scope.doctorList.filter(function(item){
											return item.doctorId ==
												$scope.appointmentMasterObj.doctorId;
										})[0] || {}
									).aiDoctor;
		
									console.log(aiDoctorFlag);

								if(aiDoctorFlag == 'Y'){

									if(!$scope.isTeleconsultationAppointment || !$scope.isTeleconsultationAppointment==0){
										$scope.isTeleconsultationAppointment = true;					
									}
								}										
									else{
										 if($scope.isTeleconsultationAppointment || $scope.isTeleconsultationAppointment==1){
												$scope.isTeleconsultationAppointment = false;									
											}

										}
									}							
								
								
								$scope.serviceOrderList =  [];
								$scope.totalServiceAmt = parseFloat(0);
								
								$scope.orderDetailsList = response.data.object.listOrderDetailsMasterDto;
								
								if(Object.keys(response.data.object.listOrderDetailsMasterDto).length > 0 ){
								  angular.forEach(response.data.object.listOrderDetailsMasterDto,function(value,key)
										{
									  		var newArr = {};
									  		newArr.serviceAmt = parseFloat(value.serviceAmt).toFixed(2);
									  		newArr.ordConcession = parseFloat(value.ordConcession).toFixed(2);
									  		newArr.taxAmount = parseFloat(value.taxAmount).toFixed(2);
									  		newArr.netAmount = parseFloat(value.netAmount).toFixed(2);
									  		newArr.specialityName = value.specialityName;
									  		newArr.serviceName = value.serviceName;
									  		newArr.doctorName = $scope.docName;
									  		$scope.serviceOrderList.push(newArr);
									  		$scope.totalServiceAmt = (parseFloat($scope.totalServiceAmt)+parseFloat(value.netAmount)).toFixed(2);
									  		
										});
								}
								else
									{
										 growl.warning("Consultation charges not configured..!!",{	
												title : "WARNING"
											});
											return;
									}
								  
								  
								  //for Bill start

									
									$scope.billServicesList=[];
									$scope.listPaymentDetails = [];
									$scope.payeeList = [];
									$scope.totalAmtQuaBase = 0;
									$scope.totalAmtConcession = 0;
									$scope.totalAmtDiscount = 0;
									$scope.totalAmtGst = 0;
									$scope.totalNetPayableAmt = 0;
									$scope.payeeDueAmtFinal = 0;
									$scope.payeeDueAmtStatic = 0;
									$scope.patientIdForReport = $scope.appointmentMasterObj.patientId;
									$scope.visitTypeId = 1;
									
									$scope.consumedAmt = parseFloat(0);
									
									$scope.payeeId = $scope.selfPayeeIdCookies;
									
									$scope.billDisabled = false;
									//$scope.billId = bill.billingMasterId;
									$scope.totalAmount = $scope.totalServiceAmt; 
									$scope.dueAmount = $scope.totalServiceAmt;
									$scope.oldDueAmount = $scope.totalServiceAmt;
									$scope.billDueAmount = $scope.totalServiceAmt;
									$scope.currentBill = {};
									$scope.currentBill.amountPaid = parseFloat(0);
									//$scope.admissionId = bill.visitTypeId == 1 ? bill.visitId : null;
									$scope.isFinancialClearance = false;
									$('#isFinancialClearance').prop('checked',false);
									
									$scope.initPaymentDetails();
									$scope.validatTextFieldFlag = true;
									$scope.getLoyaltyCardList($scope.appointmentMasterObj.patientId);
									$scope.payeeDueAmtFinal = $scope.totalServiceAmt;
									$scope.payeeDueAmtStatic = $scope.totalServiceAmt;
													
											
									
									
									//get available active deposits by patient
									var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.appointmentMasterObj.patientId+"/"+0+"/1/1/false";
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
							$rootScope.stopLoader();
						});
				  }
			}
		}else{
			$scope.getBillDetailsForNewPatient();
		}
		
		
		
	}
	
	
	
	$scope.getBillDetailsForNewPatient = function()
	{
		
		var sponserList = [];
		angular.forEach($scope.paymentEntitleMentList, function (value, key) {
			
			var newArr = {};
			newArr.organizationId = $scope.organizationId ;
			newArr.unitId = $scope.unitId;
			newArr.visitTypeId = 1;
			newArr.paymentEntitlementId =value.paymentEntitlementId;
			newArr.payeeId = value.companyId;
			newArr.priorityId = 1;
			newArr.createdBy =$scope.userId;
			newArr.createdDate = $scope.currentDate;
			newArr.updatedBy = $scope.userId;
			newArr.updatedDate = $scope.currentDate;
			newArr.status = 'A';
			newArr.tariffId =value.tariffId;
			newArr.contractId =value.contractId;
			newArr.insuranceNumber = value.insuranceNumber;
			newArr.insuredAmount = value.insuredAmount;
			newArr.validityTo = value.endDate;
			newArr.validityFrom = value.startDate;
			newArr.claimNumber = value.claimNumber;
			if(value.companyId != $scope.selfPayeeIdCookies && value.priority == 1)
		    {
		     $scope.appointmentMasterObj.contractId = value.contractId;
		     $scope.appointmentMasterObj.companyId = value.companyId;
		     return false;
		    }
			
			sponserList.push(newArr);
		});
		$scope.appointmentMasterObj.patientSponsorDetailsDtosList = sponserList;
		$scope.appointmentMasterObj.systemGeneratedVisitTypeId=$scope.appointmentMasterObj.userDefinedVisitTypeId;
		if($scope.appointmentMasterObj.patientSponsorDetailsDtosList.length==1)
		{
			/*if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == 0){
				//$scope.tab=1;
				growl.warning("Please Select Patient",{
					title : WARNING_MSG
				});
				return;
			}*/
			if($scope.appointmentMasterObj.encounterTypeId == '' || $scope.appointmentMasterObj.encounterTypeId == 0 ){
				//$scope.tab=1;
				$scope.secondTabLevel = 1 ;
				growl.warning("Please Select Encounter Type",{
					title : ERROR_MSG
				});
				return;
			}
			if(($scope.appointmentMasterObj.deptId == '' || $scope.appointmentMasterObj.deptId == 0) && $scope.appointmentMasterObj.encounterTypeId != 3 && $scope.appointmentMasterObj.encounterTypeId != 5){
				//$scope.tab=1;
				$scope.secondTabLevel = 1 ;
				growl.warning("Please Select Department",{
					title : ERROR_MSG
				});
				return;
			}
			
			if($scope.appointmentMasterObj.encounterTypeId == 1 &&  $scope.deptWiseAppointment == "N")
				{
				
					if($scope.appointmentMasterObj.doctorId == '' || $scope.appointmentMasterObj.doctorId == undefined ){
						//$scope.tab=1;
						$scope.secondTabLevel = 1 ;
						growl.warning("Please Select Doctor",{
							title : ERROR_MSG
						});
						return;
					}
				}
			else if ($scope.appointmentMasterObj.encounterTypeId == 2)
				{
				$scope.appointmentMasterObj.userDefinedVisitTypeId="4";
					if($scope.appointmentMasterObj.subDeptId == '' ){
						//$scope.tab=1;
						$scope.secondTabLevel = 1 ;
						growl.warning("Please Select Sub Department",{
							title : ERROR_MSG
						});
						return;
					}
					if($scope.appointmentMasterObj.modalityId == '' || $scope.appointmentMasterObj.modalityId == 0){
						//$scope.tab=1;
						$scope.secondTabLevel = 1 ;
						growl.warning("Please Select Modality",{
							title : ERROR_MSG
						});
						return;
					}
				}
			else if($scope.appointmentMasterObj.encounterTypeId == 4)
				{
					if($scope.appointmentMasterObj.deptId == '' ){
						//$scope.tab=1;
						$scope.secondTabLevel = 1 ;
						growl.warning("Please Select Department",{
							title : ERROR_MSG
						});
						return;
					}
					if($scope.appointmentMasterObj.subDeptId == '' ){
						//$scope.tab=1;
						$scope.secondTabLevel = 1 ;
						growl.warning("Please Select Sub Department",{
							title : ERROR_MSG
						});
						return;
					}
				}
			
			
			//var array = $scope.appointmentMasterObj.subDeptId.split('_');
			
			//----------------------------------!@#$%^&*(
			var objForGettingDoctorServiceId = {
					doctorId 		: $scope.appointmentMasterObj.doctorId,
					specialityId 	: $scope.appointmentMasterObj.deptId,
					unitId 			: $scope.unitId,
					organizationId 	: $scope.organizationId
				};
			$scope.currentDate = new Date();
			$scope.currentDate = moment(
					$scope.currentDate).format(
					'DD-MM-YYYY HH:mm:ss');
			$scope.currentDateAppDate = moment(
					new Date()).format(
					'YYYY-MM-DD HH:mm:ss');
			//appointmentObject
			if($scope.appointmentMasterObj.appointmentId > 0)
				{
				$scope.appointmentMasterObj.appointmentObject  = null;
				$scope.appointmentMasterObj.unitTokenNumber =  $scope.encounterDetailsObj.tokenNumber;
				}
			else{
				$scope.appointmentMasterObj.unitTokenNumber =  null;
				$scope.appointmentMasterObj.currentDate =  moment(new Date()).format('YYYY-MM-DD');
				if($scope.isAppointmentRequired == 'Y' && $scope.appointmentMasterObj.encounterTypeId != 4)
					{
						/*if(this.slotId == '' && $scope.appointmentMasterObj.encounterTypeId != 5)
							{
							//$scope.tab=1;
							$scope.secondTabLevel = 1 ;
							growl.warning(" Please select the time slot ",{
								title : WARNING_MSG
							});
							return;
							}
						else
							{*/
								var dataAppointmentObj = {
										remark : $scope.appointmentMasterObj.remark,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										createdDate : $scope.currentDate,
										updatedDate : $scope.currentDate,
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										status : 'A',
										appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
										priorityId : $scope.priorityId,
										firstName : $scope.patientDetails.firstName,
									    mobileNumber :$scope.patientDetails.mobileNumber,
									    generateToken : 'Y',
									    appointmentDate : $scope.currentDateAppDate,
									    isDirectEncounter : 'Y',
										ehcPackageId : $scope.EHCpackageId
										//allowedOverBookingSlots : $scope.allowedOverBookingSlots,
									}
								
								if($scope.appointmentMasterObj.encounterTypeId != 5)
									{
									   dataAppointmentObj.slotId = $scope.slotId;
									   dataAppointmentObj.appointmentDate2 = $scope.appointmentDate;
									   dataAppointmentObj.fromTimeString = $scope.fromTime;
									   dataAppointmentObj.slotStatusId = $scope.slotStatusId;
									   dataAppointmentObj.doctorName = $scope.doctorName;
									   dataAppointmentObj.isFromEncounter = 'Y';
									   dataAppointmentObj.doctorId = $scope.doctorId;
									}
								else
									{
									  dataAppointmentObj.slotId = null;
									  dataAppointmentObj.appointmentDate2 = null;
									  dataAppointmentObj.fromTimeString =null;
									  dataAppointmentObj.slotStatusId =null;
									  dataAppointmentObj.doctorName = null;
									  dataAppointmentObj.isFromEncounter = null;
									}
								if($scope.appointmentMasterObj.encounterTypeId == 3)
								{
									
									if($scope.EHCpackageId == 0 || $scope.EHCpackageId == null)
									{
										//$scope.tab=1;
										$scope.secondTabLevel = 1 ;
										growl.warning("Please Select Package",{
											title : ERROR_MSG
										});
										return;
									}else
										{
										 dataAppointmentObj.ehcPackageId = $scope.EHCpackageId;
										}
								}
								$scope.appointmentMasterObj.appointmentObject  = dataAppointmentObj;
							//}
					}
				
			}
			
			if($scope.appointmentMasterObj.encounterTypeId == 4)
			{
				$scope.appointmentMasterObj.appointmentObject  = null;
			}
			/*if($scope.appointmentMasterObj.kinDetailsId == "" && $scope.kinDetailsList[0].kinName == ""){
				$scope.tab=2;
				//$state.go('encounter.nextOfKin');
				
				growl.warning("Please add kin",{
					title : WARNING_MSG
				});
				return;
			}*/
			
			if($scope.appointmentMasterObj.encounterTypeId == 5)
			{
				$scope.appointmentMasterObj.specialityId = $scope.casualtySpecialityId;
				$scope.appointmentMasterObj.deptId = $scope.casualtySpecialityId;//need to add casualty speciality Id from unitApplicationConfig
			}
			else
				{
				 $scope.appointmentMasterObj.specialityId = $scope.appointmentMasterObj.deptId;
				}
			
			//var array = $scope.appointmentMasterObj.subDeptId.split('_');
			if($scope.appointmentMasterObj.encounterTypeId == 2 || $scope.appointmentMasterObj.encounterTypeId == 4){
			var array = $scope.appointmentMasterObj.subDeptId.split('_');
			$scope.appointmentMasterObj.subDeptId = array[1];
			//$scope.appointmentMasterObj.subSpecialityId = $scope.appointmentMasterObj.subDeptId;
			}
			
		      if($scope.appointmentMasterObj.patientClassId == '' || $scope.appointmentMasterObj.patientClassId == null || $scope.appointmentMasterObj.patientClassId == undefined)
	          {
	          $scope.appointmentMasterObj.patientClassId = null;
	          }
			
			isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
			  if (isPriorityZeroExists) {
				
				  growl.warning("Please select priority of all paymententitlement list.",{	
					title : "WARNING"
				});
				 //$scope.tab = 3;
				  //$scope.secondTabLevel = 3 ;
				return;
				
			  }
			  else{
			
				  $scope.kinDetailsFlag =false;
		        	angular.forEach($scope.kinDetailsList, function( value, index){
						if(value.kinName != ''){
					        	var contryCode=$scope.countryCallingCode;
					        	var mobileNo=value.mobileNo;
					        	if(contryCode==91){
					        		if(mobileNo.length==10){
					        			
										}else{
											growl.error("Please enter valid mobile number",{
												title : ERROR_MSG
												});
												$('#mobileNo').focus();
												$scope.setSecondTabLevel(2);
												$scope.kinDetailsFlag =true;
												return false;
					        		}
					        	}else{
						        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
						        			
										}else{
											growl.error("Please enter valid mobile number",{
												title : ERROR_MSG
												});
												$('#mobileNo').focus();
												$scope.setSecondTabLevel(2);
												$scope.kinDetailsFlag =true;
												return false;
					        		}
					        	}
						}
					});
		        	
		        	if($scope.kinDetailsFlag){
						return false;
					}
			
			var URI = BASE_URL + ROOT_URL + "/api/opd/bill/details/encounter";
			
			if($scope.selfFlag == "Y")
				{
				 $scope.appointmentMasterObj.slotId =$scope.slotId;
				}
			
			//$scope.appointmentMasterObj.encounterTime =  moment(new Date()).format("HH:mm");
			
			$scope.appointmentMasterObj.organizationId = $scope.organizationId;
			$scope.appointmentMasterObj.unitId = $scope.unitId;
			$scope.appointmentMasterObj.deptWiseAppointment = $scope.deptWiseAppointment;
			$scope.appointmentMasterObj.referralMasterDto = $scope.referralData;
			$scope.appointmentMasterObj.patientCategoryId = $scope.patientObj.patientCategoryId;
			console.log('Encounter obj',$scope.appointmentMasterObj);
			$rootScope.startLoader();
			BillingGenericService.serviceAction(METHOD_POST, URI, $scope.appointmentMasterObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success") {
							console.log("response.data.object",response.data.object);
							//$scope.tab=4;
							//$scope.secondTabLevel = 4 ;
							$scope.docName = "-";
							if($scope.appointmentMasterObj.doctorId > 0)
								{
								   $scope.docDtls = $.grep($scope.doctorList, function (disCat) {
						            return disCat.doctorId == $scope.appointmentMasterObj.doctorId;
						           })[0];
								   
								   $scope.docName = "Dr. "+$scope.docDtls.firstName+" "+$scope.docDtls.lastName;

								   	var aiDoctorFlag = (
										$scope.doctorList.filter(function(item){
											return item.doctorId ==
												$scope.appointmentMasterObj.doctorId;
										})[0] || {}
									).aiDoctor;

									console.log(aiDoctorFlag);

									if(aiDoctorFlag == 'Y'){

									if(!$scope.isTeleconsultationAppointment || !$scope.isTeleconsultationAppointment==0){
										$scope.isTeleconsultationAppointment = true;
											}
										}
										else{
											if($scope.isTeleconsultationAppointment || $scope.isTeleconsultationAppointment==1){
												$scope.isTeleconsultationAppointment = false;
											}
										}
								}
							
							
							
							$scope.serviceOrderList =  [];
							$scope.totalServiceAmt = parseFloat(0);
							
							$scope.orderDetailsList = response.data.object.listOrderDetailsMasterDto;
							
							if(Object.keys(response.data.object.listOrderDetailsMasterDto).length > 0 ){
							  angular.forEach(response.data.object.listOrderDetailsMasterDto,function(value,key)
									{
								  		var newArr = {};
								  		newArr.serviceAmt = parseFloat(value.serviceAmt).toFixed(2);
								  		newArr.ordConcession = parseFloat(value.ordConcession).toFixed(2);
								  		newArr.taxAmount = parseFloat(value.taxAmount).toFixed(2);
								  		newArr.netAmount = parseFloat(value.netAmount).toFixed(2);
								  		newArr.specialityName = value.specialityName;
								  		newArr.serviceName = value.serviceName;
								  		newArr.doctorName = $scope.docName;
								  		$scope.serviceOrderList.push(newArr);
								  		$scope.totalServiceAmt = (parseFloat($scope.totalServiceAmt)+parseFloat(value.netAmount)).toFixed(2);
								  		
									});
							}
							else
								{
									 growl.warning("Consultation charges not configured..!!",{	
											title : "WARNING"
										});
										return;
								}
							  
							  
							  //for Bill start

								
								$scope.billServicesList=[];
								
								$scope.listPaymentDetails = [];
								$scope.payeeList = [];
								$scope.totalAmtQuaBase = 0;
								$scope.totalAmtConcession = 0;
								$scope.totalAmtDiscount = 0;
								$scope.totalAmtGst = 0;
								$scope.totalNetPayableAmt = 0;
								$scope.payeeDueAmtFinal = 0;
								$scope.payeeDueAmtStatic = 0;
								$scope.patientIdForReport = $scope.appointmentMasterObj.patientId;
								$scope.visitTypeId = 1;
								
								$scope.consumedAmt = parseFloat(0);
								
								$scope.payeeId = $scope.selfPayeeIdCookies;
								
								$scope.billDisabled = false;
								//$scope.billId = bill.billingMasterId;
								$scope.totalAmount = $scope.totalServiceAmt; 
								$scope.dueAmount = $scope.totalServiceAmt;
								$scope.oldDueAmount = $scope.totalServiceAmt;
								$scope.billDueAmount = $scope.totalServiceAmt;
								$scope.currentBill = {};
								$scope.currentBill.amountPaid = parseFloat(0);
								//$scope.admissionId = bill.visitTypeId == 1 ? bill.visitId : null;
								$scope.isFinancialClearance = false;
								$('#isFinancialClearance').prop('checked',false);
								
								$scope.initPaymentDetails();
								$scope.validatTextFieldFlag = true;
								$scope.getLoyaltyCardList($scope.appointmentMasterObj.patientId);
								$scope.payeeDueAmtFinal = $scope.totalServiceAmt;
								$scope.payeeDueAmtStatic = $scope.totalServiceAmt;
												
										
								
								if($scope.appointmentMasterObj.patientId>0)
								{
									//get available active deposits by patient
									var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.appointmentMasterObj.patientId+"/"+0+"/1/1/false";
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
											/*	if(value.depositTypeId != '1')
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
								else{
									$scope.consumedAmt = 0;
									$scope.availableDeposits =[];
									var activeDeposit = parseFloat(0);
									var activeDepositPatient = parseFloat(0);
									var billAmt = parseFloat($scope.payeeDueAmtStatic);
									$scope.calculateTotalDueRefAmount();
								}
								
							
							
						}
						$rootScope.stopLoader();
					});
			  }
		}
		
		
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
				$scope.availableDeposits[idx].remark = $scope.DepositObj.remark;
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
				$scope.DepositObj.remark = ""; 
				$scope.addBtnDisabled = true;
			}
		if(item.depositeFlag == 'N')
		{
			$scope.availableDeposits[idx].remark =$scope.DepositObj.remark;
					if($scope.availableDeposits[idx].availableDeposit > 0)
						{
						$scope.availableDeposits[idx].availableDeposit =parseFloat(item.oldAvailableDeposit).toFixed(2);
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
					$scope.DepositObj.remark = "";	
					
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
		
	$scope.addPaymentDetailsOldEncounter = function(){
		$scope.isBillList = 0;
		var exAmount = '';
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
					$("#confirmSave").modal("show");
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
		}if($scope.paymentDetails.paymentModeId == 12){
			
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
	
	$scope.checkValue = function(value){
		console.log(value);
		$scope.isTeleconsultationAppointment = (value == false?0:1);
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
		
		/*
		if(parseInt($scope.totalAmount) >=totalListamount){
			$scope.transferToActiveDeposit = 'N';
			//this.$parent.$parent.transferToActiveDeposit = 'N';
			//this.$parent.$parent.$parent.transferToActiveDeposit = 'N';
		}*/
	
	}
	
	
	$scope.removePaymentDetailsOldEncounter = function(index){
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
			$scope.transferToActiveDeposit = 'N';
			$scope.excessAmount = parseFloat(0).toFixed(2);
		}else{
			var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
			$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
		}
	}
	
	
	$scope.checkValidationChangeVisitType = function()
	{
		if($scope.appointmentMasterObj.reasonIdForChangingVisitType == '' || $scope.appointmentMasterObj.reasonIdForChangingVisitType == null || $scope.appointmentMasterObj.reasonIdForChangingVisitType == undefined)
			{
				growl.warning("Please select reason",{
					title : WARNING_MSG
				});
				return;
			}
		else
			{
				angular.element('#changeVisitTypeModal').modal('hide');
				if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == 0)
				{
					$scope.getBillDetailsForNewPatient();
				}else{
					$scope.getBillDetails();	
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
	
	
	$scope.settlementClickOld=function()
	{
		
		
		if($scope.payeeId > 0)
			{
			     $scope.setFirstTabLevel(1);
				 $scope.transferToActiveDeposit = 'N'
				 $scope.excessAmount = parseFloat(0).toFixed(2);
				 $scope.send=false;
				 $scope.resend=false;
				 $scope.validatTextFieldFlag = true;
				 $scope.setPaymentModeId(1);
				 $scope.calculateTotalDueRefAmount();
				 $scope.getLoyaltyCardList($scope.patientDetails.patientId);
				 $scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
					angular.element('#collectPaymentOldEncounter').modal('show');
			}
		else
			{
				growl.warning("Please select payer..!!",{
					title : WARNING_MSG
				});
				return false;
			}
	}
	$scope.setFirstTabLevelColl = function(val){
		$scope.tabColl = val;
		this.tabColl = val;
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
		
//		$scope.setFirstTabLevel(1);
		
		if($scope.isAppointmentRequired=='Y' && ($scope.slotId==undefined || $scope.slotId==null || $scope.slotId==""))
		{
			growl.warning(" Please select the time slot ",{
				title : WARNING_MSG
			});
			return;
			
		}else{
			
			if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == 0){
				//$scope.tab=1;
				$scope.secondTabLevel = 1 ;
				/*growl.warning("Please Select Patient",{
					title : WARNING_MSG
				});
				return;*/
				if($scope.registrationFieldObj.sourceId== "Y"){
					if($scope.appointmentMasterObj.sourceId == undefined || $scope.appointmentMasterObj.sourceId == null || $scope.appointmentMasterObj.sourceId ==""){
						$scope.secondTabLevel = 1 ;
						growl.warning("Please select source",{
							title : ERROR_MSG
						});
						return false;
					}
				}
				$scope.$broadcast('show-errors-check-validity');
				// $scope.secondTabLevel=1;
		        if($scope.patientRegistrationform.$valid){
		        	//alert("valid");
		        	
		        	var contryCode=$scope.patientObj.countryCallingCode;
		        	 // validating country code
					 var regEx = /^[+-]?\d+$/;
					      if(!regEx.test(contryCode)){
					    	  growl.warning("Please enter valid country calling code",{
									title : 'Warning !'
									});
									return false;
					      } 
		        	
		        	var mobileNo=$scope.patientObj.mobileNumber;
		        	if(contryCode==91){
		        		if(mobileNo.length==10){
		        			
							}else{
								growl.warning("Please enter valid mobile number",{
									title :'Warning !'
									});
									$('#mobileNo').focus();
									return false;
		        		}
		        	}else{
			        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
			        			
							}else{
								growl.warning("Please enter valid mobile number",{
									title : 'Warning !'
									});
									$('#mobileNo').focus();
									return false;
		        		}
		        	}


		        	if($scope.patientObj.patientCategoryId==null || $scope.patientObj.patientCategoryId==undefined||$scope.patientObj.patientCategoryId==""){
		        		growl.warning("Please select patient category",{
									title : 'Warning !'
									});
									
									return false;
		        	}
					/*if($scope.patientRegistrationform2.$valid){*/
		        		 if($scope.patientObj.phoneCode !='' && $scope.patientObj.phoneCode!=undefined && $scope.patientObj.phoneCode!=null){
		        			 var phoneCode=$scope.patientObj.phoneCode;
				        	 // validating country code
							 var regEx = /^[+-]?\d+$/;
							      if(!regEx.test(phoneCode)){
							    	  growl.warning("Please enter valid country calling code",{
											title : 'Warning !'
											});
											return false;
							      } 
		        		 }
		        		 //test start
		        		 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
	       			 }
		        		 if($scope.patientObj.permanentZipCode !='' && $scope.patientObj.permanentZipCode!=undefined && $scope.patientObj.permanentZipCode!=null){
	       			 }
		        		 if($scope.patientObj.permanentPhoneCode !='' && $scope.patientObj.permanentPhoneCode!=undefined && $scope.patientObj.permanentPhoneCode!=null){
		        			 var permanentPhoneCode=$scope.patientObj.permanentPhoneCode;
				        	 // validating country code
							 var regEx = /^[+-]?\d+$/;
							      if(!regEx.test(permanentPhoneCode)){
							    	  growl.warning("Please enter valid country calling code",{
											title : 'Warning !'
											});
											return false;
							      } 
		        		 }
		        		 if($scope.patientObj.email == null){
		        			 $scope.patientObj.email = "";
		        		 }
		        		 if($scope.patientObj.email != "")
		           			{
			           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			           		 var email = $scope.patientObj.email;
			           		    if (!filter.test(email)) {

		        				    growl.error(EMAIL_MSG,{
										title : ERROR_MSG

									});
		        				    $('#email').focus();
		        				    return false;
			           		    }
			           		    else{
			           		    	if($scope.patientObj.phoneNumber == undefined)
			           		    			$scope.patientObj.phoneNumber='';
			           		    	if($scope.patientObj.phoneNumber == null)
			           		    			$scope.patientObj.phoneNumber='';
			           		     if($scope.patientObj.phoneNumber != '')
					        		{
					        		}
			           		     else
			           		    	 {
			           		    	 }
			           		    }
		           			}
		        		 else
		        			 {
		        			 if($scope.patientObj.phoneNumber == undefined)
		        				 $scope.patientObj.phoneNumber='';
		        			 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
		        			 }
		        			 }
		        		
		        		 //test end
		        			 
		        }else{
		        	growl.warning(" Please select patient or add patient details..!! ",{
						title : WARNING_MSG
					});
		        	return false;
		        }
			}
			
		 angular.element('#collectPayment').modal('show');
		 if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == 0){
			 $scope.getBillDetailsForNewPatient();
		 }else{
			 $scope.getBillDetails();
		 }
		 
		 $scope.send=false;
		 $scope.resend=false;
		 $scope.setFirstTabLevel(1);
		 $scope.setPaymentModeId(1);
		 $scope.validatTextFieldFlag = true;
		 $scope.getLoyaltyCardList($scope.patientIdForReport);
		 $scope.transferToActiveDeposit = 'N';
		 $scope.excessAmount = parseFloat(0).toFixed(2);
		 $scope.listPaymentDetails = [];
		 $scope.calculateTotalDueRefAmount();
		 $scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
		}
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
			$scope.razorPayObject.razorAmount =  $scope.dueAmount;
			  $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
			}
		else
			{
			$scope.dueAmount = parseFloat(0.00).toFixed(2);
			$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
			$scope.paymentDetails.chequeAmount = $scope.dueAmount;
			$scope.paymentDetails.debAmount = $scope.dueAmount;
			$scope.paymentDetails.credAmount = $scope.dueAmount;
			$scope.eTransferObj.transferAmount = $scope.dueAmount;
			$scope.razorPayObject.razorAmount =  $scope.dueAmount;
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
	/*$scope.calculateTotalDueRefAmount = function()
	{
		var amount = 0;
		var dueAmount = $rootScope.parseFloatNum($scope.dueAmount);
	
		
		var payeeDueAmtFinal = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
		
		angular.forEach($scope.listPaymentDetails,function(value,key){
			amount = amount + parseFloat(value.amount); 
		});
		var tempDueAmt = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
		
		var temppayeeDueAmtFinal = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
		$scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;
		
		$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
		
		console.log("tt1", parseInt(amount) + parseInt($scope.consumedAmt));
		console.log("tt2",(parseInt($scope.payeeDueAmtStatic) - (parseInt(amount) + parseInt($scope.consumedAmt))));
		var df = (parseInt($scope.payeeDueAmtStatic) - (parseInt(amount) + parseInt($scope.consumedAmt)));
		if(df > 0)
			{
			$scope.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
			$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
			}
		else
			{
			$scope.dueAmount = parseInt($scope.billDueAmount) - parseInt($scope.payeeDueAmtStatic);
			$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
			}
		
		$scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
		$scope.amountCollected = amount;
		
			  
	}*/
	
	
	//for old encounter settlement
	
	$scope.billDetails = function(bill){
		
		
		$scope.setFirstTabLevel(1);
		$scope.excessAmount = parseFloat(0).toFixed(2);	
		$scope.transferToActiveDeposit = 'N'
		$scope.amountPaid = bill.amountPaid;
		$scope.selectedUhid = bill.uhidNumber;
		$scope.selectedPatientId = bill.patientId;
		console.log("BILL",bill);
		$scope.validatTextFieldFlag = true;
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
		$scope.patientIdForReport = bill.patientId;
		$scope.visitTypeId = bill.visitTypeId;
		$scope.referalDocId = bill.referalDocId;
		$scope.consumedAmt = parseFloat(0);
		$scope.payeeId = $scope.selfPayeeIdCookies;
		
		$scope.billDisabled = (bill.billStatusId == 3 || (bill.intermediateSelfDue!=null &&  bill.intermediateSelfDue>0)) ? true : false;
		$scope.billId = bill.billingMasterId;
		$scope.isInterimOrFinalBill =2;
		$scope.isNursingAssessmentRequired = bill.isNursingAssessmentRequired;
		$scope.disallowedAmount = bill.disallowedAmount;
		$scope.disallowedAgainstInvoiceDtlsId = bill.disallowedAgainstInvoiceDtlsId;
		$scope.doctorId = bill.doctorId;
		$scope.totalAmount = bill.totalBillAmount; 
		$scope.dueAmount = bill.outstanding;
		$scope.oldDueAmount = bill.outstanding;
		$scope.billDueAmount = bill.outstanding;
		$scope.totalSelfPayable = bill.selfPayable;
		$scope.totalCreditPayable=bill.creditPayable;
		$scope.currentBill = bill;
		$scope.admissionId = bill.visitTypeId != 1 ? bill.visitId : null;
		$scope.isFinancialClearance = false;
		$('#isFinancialClearance').prop('checked',false);
		//Vivek added
		$scope.encounterId = bill.visitId;
		$scope.appointmentId = bill.appointmentId;
		//End Vivek Added
		if($scope.visitTypeId == 1 && $scope.isPreOrPostBilling == 'N'){
			$scope.getPendingServicesByEncounterId();
		}
		
		//get payer details
		$scope.initPaymentDetails();
		$scope.mpesaObject={};
		$scope.getLoyaltyCardList($scope.patientDetails.patientId);
		var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+bill.billingMasterId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
				$scope.payeeList = response.data.listObject;
				$scope.billServicesList = response.data.object.listObject;
				angular.forEach($scope.billServicesList,function(value,index)
						{
							value.orderDateString=moment(value.orderDateString,"DD/MM/YYYY").format($scope.dateFormat.toUpperCase())
							
						});
				$scope.paymentReceiptsDetailsList=response.data.object.object;
				angular.forEach($scope.paymentReceiptsDetailsList,function(value,index)
						{
							value.dateOfReciept=moment(value.dateOfReciept,"DD/MM/YYYY").format($scope.dateFormat.toUpperCase())
							
						});
				var totalAmtQuaBase = parseFloat(0);
				var totalAmtConcession = parseFloat(0);
				var totalAmtDiscount = parseFloat(0);
				var totalAmtGst = parseFloat(0);
				var totalNetPayableAmt = parseFloat(0);
				var discountRoundOff = parseFloat(0);
				var totalSelfPayable = parseFloat(0);
				var totalCreditPayable = parseFloat(0);
				var totalPackageConsumptionAmt = parseFloat(0);
				
				
				angular.forEach($scope.billServicesList,function(value,key)
						{
					angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
							{
							  if(value1.isPackageService=='N')
								  {
								    totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
									totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
									totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
									totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
									totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
								    totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
									totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
									discountRoundOff = parseFloat(value.roundOff);
								  }
							});
					// totalPackageConsumptionAmt = totalPackageConsumptionAmt + parseFloat(value.packageConsumptionAmt);
				});
				
				$scope.totalAmtQuaBase =totalAmtQuaBase;
				$scope.totalAmtConcession = totalAmtConcession;
				$scope.totalAmtDiscount =totalAmtDiscount+Number(discountRoundOff);
				$scope.totalAmtGst = (totalAmtGst).toFixed(2);
				$scope.totalNetPayableAmt = totalNetPayableAmt+(Number(discountRoundOff));
				$scope.totalPackageConsumptionAmt = totalPackageConsumptionAmt;
				
				angular.forEach($scope.payeeList,function(value1,key)
				{
					if(value1.payeeId == $scope.selfPayeeIdCookies)
						{
						$scope.payeeDueAmtFinal = Number(parseFloat(value1.dueAmount).toFixed(2));
						$scope.payeeDueAmtStatic = Number(parseFloat(value1.dueAmount).toFixed(2));
						}
					
				});
				
				angular.forEach($scope.payeeList,function(value,key){
					
					if(value.payeeId==$scope.payeeId && (value.billAmount==0 || value.dueAmount==0))
						$scope.billDisabled = true;
				})
				
				
				
				//get available active deposits by patient
				var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.patientDetails.patientId+"/"+bill.encounterId+"/1/"+0+"/false";
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
						/*	if(value.depositTypeId != '1')
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
			}
		});
		
	}
	
	//for old encounter settlement
	
	
	$scope.settleBillOldEncounter = function(){
		$('#collectPaymentOldEncounter').modal('hide');
		$('#billListDetailsOldEncounter').modal('hide');
		$rootScope.startLoader();
		$scope.billDisabled = true;
		console.log("LISt CHECK",$scope.availableDeposits);
		
		var billPayeeDetailsId = "";
		var payeeDueAmount = 0;
		//Get Bank Name
		angular.forEach($scope.payeeList,function(value,key){
			if(value.payeeId == $scope.selfPayeeIdCookies)
				{
				billPayeeDetailsId = value.billPayeeDetailsId;
			    payeeDueAmount = value.dueAmount;
				}
		})
		
		$scope.finalAvailableDeposits = [];
		var i = 0;
		angular.forEach($scope.availableDeposits,function(value,key){
			if(value.depositeFlag == 'Y')
				{
				//availableDeposit : A
				//depositAmount : A
				//depositMasterId : A 
				//organizationId : A 
				//unitId:A
				//depConsumptionDate: currentDate
				//billId : V
				//billPayeeDetailsId : V
				//consumedAmount : consumedDeposit
				var newArr = {};
				newArr.availableDeposit = $scope.availableDeposits[i].availableDeposit;
				newArr.depositAmount = $scope.availableDeposits[i].depositAmount;
				newArr.depositMasterId =$scope.availableDeposits[i].depositMasterId;
				newArr.organizationId  =  $scope.organizationId;
				newArr.unitId = $scope.unitId;
				newArr.depConsumptionDate =moment(new Date()).format('DD-MM-YYYY HH:MM:SS'); 
				newArr.billId = $scope.billId;	
				newArr.billPayeeDetailsId = billPayeeDetailsId;
				newArr.consumedAmount = (parseFloat($scope.availableDeposits[i].consumedDeposit).toFixed(2) - $scope.availableDeposits[i].oldConsumedDeposit); 
				newArr.createdBy = $scope.userId;
				$scope.finalAvailableDeposits.push(newArr);
				//$scope.finalAvailableDeposits.push($scope.availableDeposits[i]);
				}
			i++;
			
		});
		console.log("$scope.finalAvailableDeposits",$scope.finalAvailableDeposits);
		console.log("$scope.listPaymentDetails",$scope.listPaymentDetails);
		
		
		//return false;
		var param = {
				
				totalSelfPayable : $scope.totalSelfPayable,
				totalCreditPayable : $scope.totalCreditPayable,
				doctorId : $scope.doctorId,
				isInterimOrFinalBill : $scope.isInterimOrFinalBill,
				billingMasterId : $scope.billId,
				billPayeeDetailsId : billPayeeDetailsId,
				dateOfReciept : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				payeeId : $scope.payeeId,
				//listBillingPaymentDetailsReqDto : $scope.listPaymentDetails,
				paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
				orgnizationId : $scope.organizationId,
				unitId : $scope.unitId,
				createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
				updatedBy:$scope.userId,
				createdBy:$scope.userId,
				receiptType:1,
				receivedFrom:$scope.receivedFrom,
				identificationNumber : $scope.identificationNumber,
				remark : "",
				amount : (parseFloat($scope.amountCollected) + parseFloat($scope.consumedAmt)).toFixed(2) ,
				//consumedAmount : $scope.consumedAmt,
				//totalCollection : $scope.amountCollected,
				totalDepositConsumption : $scope.consumedAmt,
				isPreOrPostBilling:$scope.isPreOrPostBilling,
				isFinancialClearance : $scope.isFinancialClearance ? 'Y' : 'N',
				appointmentId:$scope.appointmentId,
				encounterId : $scope.encounterId,
				counterId : $rootScope.counterId,
				paymentDepositDetailsList : $scope.finalAvailableDeposits,
				payeeDueAmount :parseFloat(payeeDueAmount).toFixed(2),
				admissionId : $scope.admissionId,
				signetureImage:$scope.binaryValueDataForSing,
				isTransferToActiveDeposit : this.transferToActiveDeposit,
				patientId : $scope.patientDetails.patientId,
				visitTypeId : 1,
				referalDocId : $scope.referalDocId,
				isNursingAssessmentRequired : $scope.isNursingAssessmentRequired,
				disallowedAmount : $scope.disallowedAmount,
				disallowedAgainstInvoiceDtlsId : $scope.disallowedAgainstInvoiceDtlsId,
				outstanding : $scope.oldDueAmount
		}
		
		if($scope.payeeId == '' || typeof($scope.payeeId) == "undefined"){
			$scope.billDisabled = false;
			growl.warning("Please select at least one payee..!!",{
				title : 'Warning!'
			});
			return;
		}
		
		if(($scope.finalAvailableDeposits.length == 0)&&($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')){
			$scope.billDisabled = false;
			growl.warning("Please select at least one payment mode..!!",{
				title : 'Warning!'
			});
			return;
		}
		
		if(($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)){
			$scope.billDisabled = false;
			growl.warning("Amount can't be zero..!!",{
				title : 'Warning!'
			});
			return;
		}
		
		console.log("param",param);
		//return false;
		
		var URI = BASE_URL + ROOT_URL + API + BILLING + "/payments";
		BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
		{
			if(response.data.status == "success"){
				//collectPayment
				//billListDetails
				$scope.getLastEncounterDetails('N');
				$scope.billDisabled = false;
				$scope.validatTextFieldFlag = true;
				$scope.excessAmount = parseFloat(0).toFixed(2);
				$scope.transferToActiveDeposit = 'N'
				this.transferToActiveDeposit = "N";
				$scope.billDetails($scope.currentBill);
				$scope.calculateTotalDueRefAmount();
				$scope.listPaymentDetails = [];
				$scope.mpesaObject={};
				$scope.searchBills($scope.billSearch);
				$scope.receiptId=response.data.object.recieptId;
				$scope.depositConsumptionId=response.data.object.depositConsumptionId;
				$scope.processDocShare();
				console.log(response);
				if(response.data.object != null)
				{
						  
					var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/0/"+$scope.userId+"/"+response.data.object.recieptId;
					BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					{
						$rootScope.stopLoader();
						if (response.data.status == "success")
						{
							var data =  
							  {
									  
					                 "appName": "HIS",
					                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					                 "reportDesignFilePath": "OPDPaymentReceipt.rpt",
					                 "reportFormat": "PDF",
					                 "exportFileName": "PaymentReciept",
					                 "reportParams": [
									                 	{
											                  "paramName": "@BillID",
											                  "paramType": "Integer",
											                  "paramValue": $scope.currentBill.billingMasterId
										                },
										                {
											                  "paramName": "@PaymentID",
											                  "paramType": "Integer",
											                  "paramValue": response.data.object.recieptId
										                },
										                {
											                  "paramName": "@dep_consumption_id",
											                  "paramType": "Integer",
											                  "paramValue": (response.data.object.depositConsumptionId!=null ? response.data.object.depositConsumptionId :0)
										                }
										              ]
							  };
							reportService.generate(REPORT_API_URI1,data);
							
						}
					});
					
					
					}
		}else{
			
				$rootScope.stopLoader();
				$scope.getLastEncounterDetails('N');
				$scope.initAppointmentMasterObj();
				//$scope.searchBills($scope.billSearch);
				
		}
		});
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
	
	$scope.changeCashMode = function(cashSettlementFlag){
		if($scope.patientRegistrationform.$valid || $scope.patientDetails.patientId){
		$scope.cashSettlementFlag = cashSettlementFlag;
		}else{
			growl.warning(" Please select patient or add patient details..!! ",{
				title : WARNING_MSG
			});
        	return false;
		}
		}
	// NEW FOR VIVEK
	$scope.settleBill = function(){
		if($scope.visitTypeId == 1){
			if($scope.billSettlement  == false){
				growl.warning("user do not have bill settlements rights",
						{
							title : 'Error!'
						});
			    
				return false;
			}
		}
		if($scope.registrationFieldObj.sourceId== "Y"){
			if($scope.appointmentMasterObj.sourceId == undefined || $scope.appointmentMasterObj.sourceId == null || $scope.appointmentMasterObj.sourceId ==""){
				$scope.secondTabLevel = 1 ;
				growl.warning("Please select source",{
					title : ERROR_MSG
				});
				return false;
			}
		}
		if($scope.patientRegistrationform.$valid || $scope.patientDetails.patientId){
		if($scope.appointmentMasterObj.referralTypeId !=null 
				   && $scope.appointmentMasterObj.referralTypeId !=""
				   && $scope.appointmentMasterObj.referralTypeId !=undefined){
			
			   if($scope.appointmentMasterObj.referralDesc == null 
					   || $scope.appointmentMasterObj.referralDesc == ""
					   || $scope.appointmentMasterObj.referralDesc == undefined){
				   
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Referral By",{
						title : ERROR_MSG
					});
					return;
					}
				   
	 	}
		$scope.diableSettle = true;
		if($scope.cashSettlementFlag == 'N')
		{
			var collectedAmount = parseFloat(0).toFixed();
			angular.forEach($scope.listPaymentDetails,function(value,key){
				if(value.paymentModeId == 1){
					collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
				}
				
			});
			
			if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
				$('#uploadpanCardDetailsEncouter').modal('show');
					return false;
			}
			$('#uploadpanCardDetailsEncouter').modal('hide');
	    	
		}else{
			
			if($scope.orderDetailsList != null  && $scope.orderDetailsList.length >0){
				
				var collectedAmount = parseFloat(0).toFixed();
				angular.forEach($scope.orderDetailsList,function(value,key){
						collectedAmount = parseFloat(collectedAmount) + parseFloat(value.selfPayable);
				});
				
				if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
					$('#uploadpanCardDetailsEncouter').modal('show');
					return false;
				}
				$('#uploadpanCardDetailsEncouter').modal('hide');
			}
			
	    	
		}
		}else{
			growl.warning(" Please select patient or add patient details..!! ",{
				title : WARNING_MSG
			});
		    $scope.cashSettlementFlag = 'N';
        	return false;
		}
		
    	
		
		$rootScope.startLoader();
		$scope.billDisabled = true;
		angular.element('#collectPayment').modal('hide');
//		$('#collectPayment').hide('hide');
		$(".modal-backdrop").hide();
		
		
		console.log("LISt CHECK",$scope.availableDeposits);
		
		
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
	
		$scope.finalOtReservationList = [];
		angular.forEach($scope.otReservationList ,function(value,index){
			if(value.isSelected){
				$scope.finalOtReservationList.push(value);
			}
		});
		
		if(($scope.appointmentMasterObj.appointmentObject == null || $scope.appointmentMasterObj.appointmentObject == undefined) && !($scope.appointmentMasterObj.appointmentId > 0) ){
			$scope.appointmentMasterObj.appointmentObject = {};
			
			var appointmentObj = {
					patientId : $scope.appointmentMasterObj.patientId,
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					createdDate : $scope.currentDate,
					updatedDate : $scope.currentDate,
					createdBy : $scope.userId,
					updatedBy : $scope.userId,
					status : 'A',
					appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
					//priorityId : $scope.priorityId,
					firstName : $scope.patientDetails.firstName,
				    mobileNumber :$scope.patientDetails.mobileNumber,
				    generateToken : 'N',
				    appointmentDate : $scope.currentDateAppDate,
				    isDirectEncounter : 'Y',
					ehcPackageId : $scope.EHCpackageId,
					packageBasePrice : $scope.packageBasePrice,
					slotId : $scope.slotId,
					slotStatusId : $scope.slotStatusId,
					appointmentId : $scope.appointmentMasterObj.appointmentId,
					// isTeleconsultationAppointment : $scope.isTeleconsultationAppointment
					isTeleconsultationAppointment:
						($scope.isTeleconsultationAppointment == true || 
						$scope.isTeleconsultationAppointment == 1) ? 1 : 0
			}
			$scope.appointmentMasterObj.appointmentObject = appointmentObj;
				
		}
		else{
			if(($scope.appointmentMasterObj.appointmentObject !=undefined || $scope.appointmentMasterObj.appointmentObject!=null) 
						&& ($scope.appointmentMasterObj.appointmentObject.slotId=="" ||$scope.appointmentMasterObj.appointmentObject.slotId==undefined || $scope.appointmentMasterObj.appointmentObject.slotId==null))
				{
						$scope.appointmentMasterObj.appointmentObject.slotId = $scope.slotId;
						$scope.appointmentMasterObj.appointmentObject.slotStatusId = $scope.slotStatusId;
					
				}else{
					$scope.appointmentMasterObj.slotId =$scope.slotId;
				}
		}
		
		if($scope.isAppointmentRequired=='Y' && ($scope.slotId==undefined || $scope.slotId==null || $scope.slotId==""))
		{
			$scope.diableSettle = false;
			growl.warning(" Please select the time slot ",{
				title : WARNING_MSG
			});
			$rootScope.stopLoader();
			return;
			
		}
		
		

		if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == null || $scope.appointmentMasterObj.patientId == 0){
			var flag=false;
			$scope.currentDate = new Date();
			$scope.currentDate = moment($scope.currentDate)
					.format('DD-MM-YYYY HH:mm:ss');
			
			$scope.patientObj.createdDate = $scope.currentDate;
			$scope.patientObj.updatedDate = $scope.currentDate;
			$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
			$scope.patientObj.middleName =  ($scope.patientObj.middleName == null || $scope.patientObj.middleName.trim() == "" || $scope.patientObj.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.middleName));
			$scope.patientObj.lastName =   ($scope.patientObj.lastName == null || $scope.patientObj.lastName.trim() == "" || $scope.patientObj.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.lastName));
			$scope.patientObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
	
			
			
			var ConDate = $('#datepickerExpiry').val();
			
			if($scope.expiryDateDisabled==false && (ConDate != '' && ConDate != undefined)){
				var newExDate = new Date($rootScope.getChangedFormatedDateShalseTwo(ConDate));
				$scope.patientObj.identificationExpiryDate = moment(newExDate).format('DD-MM-YYYY HH:mm:ss');
			}
			
//			 var splittedStringArray = $scope.patientObj.identificationTypeId.split("_");
//			 $scope.patientObj.identificationTypeId = splittedStringArray[0]; 
			if($scope.patientObj.identificationTypeId==undefined || $scope.patientObj.identificationTypeId=="")
				$scope.patientObj.identificationTypeId=null;
			
			
			
			if($scope.patientObj.isVip=='N')
				$scope.patientObj.aliseName='';
			
			if($scope.sameAsLocal == 'Y'){
				$scope.patientObj.permanentAddress = $scope.patientObj.address;
				$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
				$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
				$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
				$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
				$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
				$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
				$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
				$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
				}
			
			if($scope.patientObj.identificationTypeId !=null)
			{
				 $scope.patientObjCopy = angular.copy($scope.patientObj);
		      var splittedStringArray = $scope.patientObjCopy.identificationTypeId.split("_");
		      $scope.patientObjCopy.identificationTypeId = splittedStringArray[0];
		      if(splittedStringArray[1]=='Y'){
			      if($scope.patientObjCopy.identificationExpiryDate==undefined || $scope.patientObjCopy.identificationExpiryDate==null || $scope.patientObjCopy.identificationExpiryDate==""){
					   flag=true;
				   }
		      }
		     
			}
			else
			{
				$scope.patientObj.identificationTypeId = null;
				$scope.patientObjCopy = angular.copy($scope.patientObj);
			}
			if(flag){
				$scope.diableSettle = false;
				growl.error(
						'Please select expiry date',
						{
							title : 'Error!'
						});
				return;
			}
			
			 console.log($scope.patientObj);
			 $scope.appointmentMasterObj.patientRegistrationDto = $scope.patientObjCopy;
		}
		if($scope.appointmentMasterObj!=null && $scope.appointmentMasterObj.appointmentObject!=null )
			$scope.appointmentMasterObj.appointmentObject.isTeleconsultationAppointment = $scope.isTeleconsultationAppointment;
		var param = {
				encounterMasterDto : $scope.appointmentMasterObj,
				paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
				orderDetailsList:$scope.orderDetailsList,
				isNursingAssessmentRequired : $scope.isNursingAssessmentRequired,
				transferToActiveDeposit :$scope.transferToActiveDepositFact,
				paymentDepositDetailsList : $scope.finalAvailableDeposits,
				totalDepositConsumption : $scope.consumedAmt,
				otReservationList : $scope.finalOtReservationList,
				cashSettlement : this.cashSettlementFlag
		}
		
		
		
		//param.paymentCollectionDto.listBillingPaymentDetailsReqDto =$scope.listPaymentDetails; 
		console.log("param",param);
		
		
		if(this.cashSettlementFlag == 'N')
		{
			if(($scope.finalAvailableDeposits.length == 0)&&($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')){
				$scope.billDisabled = false;
				growl.warning("Please select at least one payment",{
					title : WARNING_MSG
				});
				$rootScope.stopLoader();
				return;
			}
			
			if(($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)){
				$scope.billDisabled = false;
				growl.error("Amount can't be zero",{
					title : ERROR_MSG
				});
				$rootScope.stopLoader();
				return;
			}
		}
		
		console.log("param",param);
		//return false;
		
		var URI = BASE_URL + ROOT_URL + API + BILLING + "/encounter/collect/payment";
		BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
			if (response.data.status == "success")
			{
				 if($scope.fileObj.file != null){
						$scope.uploadEncounterDoc(response.data.object.billingMasterId != undefined ? response.data.object.billingMasterId : response.data.resObject.billingMasterId);
					}
				
				if($scope.encounterDetailsContractList != undefined && $scope.encounterDetailsContractList.length > 0){
					$scope.count = 0;
					angular.forEach($scope.encounterDetailsContractList, function(value, key) 
							{  
						       value.patientId = response.data.object.patientId;
								var promise = $scope.uploadEncounterContractDoc(value);
								promise.then(function(response) 
										{
										  if(response.data.status == "success")
											  {
											    $scope.count++;
											    if($scope.encounterDetailsContractList.length==$scope.count)
												 {
												   $scope.encounterDetailsContractList = [];
												   
												 }
										      }
										  else 
											{
												growl.error('Something went wrong.',
														{
															title : 'Error!'
														});
											}
									    });
									
							});
				}
				//upload patient images
				 if($scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
					{
					  $scope.uploadPatientImage($scope.patientImage1,response.data.object.object.patientId,$scope.organizationId,$scope.unitId);
					}
					if($scope.snapshotData!=undefined && $scope.snapshotData!="")
					{
					  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
					  $scope.uploadPatientImage(file,response.data.object.object.patientId,$scope.organizationId,$scope.unitId);
					}

				 $scope.patientAdmittedFlag = false;
				 $scope.diableSettle = false;
//				$('#collectPayment').hide('hide');
				angular.element('#collectPayment').modal('hide');
				$(".modal-backdrop").hide();
				$rootScope.stopLoader();
				$scope.mpesaObject={};
				$scope.validatTextFieldFlag = true;
				$scope.excessAmount = parseFloat(0).toFixed(2);
				$scope.transferToActiveDeposit = 'N'
				this.transferToActiveDeposit = "N";
				
				if($scope.isTeleconsultationAppointment == 1){
					if($scope.patientDetails.videoCallUrl == null  || $scope.patientDetails.videoCallUrl == undefined ){
						if($scope.patientObj.email != ''  && $scope.patientObj.email != null){
							$scope.email = $scope.patientObj.email;
						}
						if($scope.patientDetails.email != '' && $scope.patientDetails.email != null){
							$scope.email = $scope.patientDetails.email;
						}
					$scope.date = moment(new Date()).format('DD-MM-YYYY');
					var data = {
							"patientId" : response.data.object.object.patientId,
							"organizationId" :$scope.organizationId,
							"unitId" :$scope.unitId,
							"doctorId" :response.data.object.object.doctorId,
							"email" : $scope.email,
							"dateTime" : moment(new Date()).format('DD/MM/YYYY') +" "+$scope.fromTime
					};
							var URI = BASE_URL + ROOT_URL
							+ "/api/global/sendPatientSmsFromCoversheet";
							 GenericService.serviceAction("POST", URI, data).then(function(response) {
									if (response.data.status == "success") {
										growl.success("Message sent successfully !!",
											{
												title : 'Success!'
											});
									}
									var URL = response.data.object;

								});
					}

			}
				/*var data =  {
		                 "appName": "HIS",
		                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		                 "reportDesignFilePath": "opdbillreceipt.rpt",
		                 "reportFormat": "PDF",
		                 "exportFileName": "PatientBill",
		                 "reportParams": [
		                   {
		                  "paramName": "@billid",
		                  "paramType": "Integer",
		                  "paramValue": response.data.resObject.billingMasterId
		                }
		                 ]
		      };
				 reportService.generate(REPORT_API_URI1,data);*/
				var data =  {
						
		                 "appName": "HIS",
		                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		                 "reportDesignFilePath": "OPDCaseSheet_OPDBill_Receipt.rpt",
		                 "reportFormat": "PDF",
		                 "exportFileName": "PatientBill",
		                 "reportParams": [
		                   {
		                  "paramName": "@EncounterID",
		                  "paramType": "Integer",
		                  "paramValue": response.data.object.object.encounterId
		                }]
		                 
		      };
				 reportService.generate(REPORT_API_URI1,data);
				//$state.go('billList');
				 	$state.reload();
				 	$state.go("encounter.encounterDetails", {
						appointmentObj: {},
					});
					$scope.initPatientSearchObj();
					$scope.initAppointmentMasterObj();
					$scope.initKinDetails();
					$scope.serviceOrderList = [];
					$scope.totalServiceAmt = 0;
					$scope.patientList = [];
					$scope.secondTabLevel = 1 ;
					$scope.encounterList=[];
					$scope.kinsList=[];
					$scope.datepicker = "";
					$scope.patientObj = {

							prefixId : '',
							firstName : '',
							middleName : '',
							lastName : '',
							genderId : '',
							countryCallingCode : '',
							email : '',
							nationalityId : '',
							raceId : '',
							maritalStatusId : '',
							mobileNumber : '',
							dob : '',
							identificationTypeId : '',
							identificationNumber : '',
							isVip : '',
							organizationId : $scope.organizationId,
							unitId : $scope.unitId,
							updatedBy : $scope.userId,
							createdBy : $scope.userId,
							status : 'A',
							patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
							aliseName:'',
							identificationExpiryDate:'',
							occupationId:'',
							registrationTypeId:1,
							previousId:'',
							patientId:0,
							patientDetailsId:'',
							whatsappCallingCode : '',
							whatsappNo:'',
							skypeId:'',
							address : '',
							countryId : '',
							stateId : '',
							districtId : '',
							cityId :'',
							areaId : '',
							zipCode : '',
							phoneCode : '',
							phoneNumber : '',
							permanentAddress:'',
							permanentCountryId:'',
							permanentStateId:'',
							permanentDistrictId:'',
							permanentCityId:'',
							permanentAreaId:'',
							permanentZipCode:'',
							permanentPhoneCode:'',
							permanentPhoneNumber:'',
							fileSequenceNumber:'',
							bloodGroup:'',
							isAlreadyExist:'N'
						};
					 $scope.patientAdmittedFlag = false;
						$scope.ageDay = 0;
						$scope.ageMonth = 0;
						$scope.ageYear = 0;
						
						if($scope.unitCountryId > 0)
						{
						  $scope.patientObj.countryId  = $scope.unitCountryId;
						  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
						  $scope.patientObj.stateId  =  $scope.unitStateId;
						  $scope.patientObj.districtId = $scope.unitDistrictId;
						  $scope.patientObj.cityId = $scope.unitCityId;
						  $scope.patientObj.areaId = $scope.unitAreaId;
						  $scope.patientObj.zipCode = $scope.unitZipCode;
						  $scope.getStateByCountry($scope.patientObj.countryId,'L');
						  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
						  $scope.getDistrictByState($scope.patientObj.stateId,'L');
						  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
						  $scope.getAreaByCity($scope.patientObj.cityId,'L');
						  $scope.selectFlag = false;
						}
						
						
			}else{
				$scope.encounterDetailsContractList = [];
				$scope.initPatientSearchObj();
				$scope.initAppointmentMasterObj();
				$scope.initKinDetails();
				$scope.serviceOrderList = [];
				$scope.totalServiceAmt = 0;
				$scope.patientList=[];
				$scope.secondTabLevel = 1 ;
				
				$scope.patientObj = {

						prefixId : '',
						firstName : '',
						middleName : '',
						lastName : '',
						genderId : '',
						countryCallingCode : '',
						email : '',
						nationalityId : '',
						raceId : '',
						maritalStatusId : '',
						mobileNumber : '',
						dob : '',
						identificationTypeId : '',
						identificationNumber : '',
						isVip : '',
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						updatedBy : $scope.userId,
						createdBy : $scope.userId,
						status : 'A',
						patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
						aliseName:'',
						identificationExpiryDate:'',
						occupationId:'',
						registrationTypeId:1,
						previousId:'',
						patientId:0,
						patientDetailsId:'',
						whatsappCallingCode : '',
						whatsappNo:'',
						skypeId:'',
						address : '',
						countryId : '',
						stateId : '',
						districtId : '',
						cityId :'',
						areaId : '',
						zipCode : '',
						phoneCode : '',
						phoneNumber : '',
						permanentAddress:'',
						permanentCountryId:'',
						permanentStateId:'',
						permanentDistrictId:'',
						permanentCityId:'',
						permanentAreaId:'',
						permanentZipCode:'',
						permanentPhoneCode:'',
						permanentPhoneNumber:'',
						fileSequenceNumber:'',
						bloodGroup:'',
						isAlreadyExist:'N'
					};
				 $scope.patientAdmittedFlag = false;
					$scope.ageDay = 0;
					$scope.ageMonth = 0;
					$scope.ageYear = 0;
					
					if($scope.unitCountryId > 0)
					{
					  $scope.patientObj.countryId  = $scope.unitCountryId;
					  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
					  $scope.patientObj.stateId  =  $scope.unitStateId;
					  $scope.patientObj.districtId = $scope.unitDistrictId;
					  $scope.patientObj.cityId = $scope.unitCityId;
					  $scope.patientObj.areaId = $scope.unitAreaId;
					  $scope.patientObj.zipCode = $scope.unitZipCode;
					  $scope.getStateByCountry($scope.patientObj.countryId,'L');
					  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
					  $scope.getDistrictByState($scope.patientObj.stateId,'L');
					  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
					  $scope.getAreaByCity($scope.patientObj.cityId,'L');
					  $scope.selectFlag = false;
					  $state.reload();
					 	$state.go("encounter.encounterDetails", {
							appointmentObj: {},
						});
					}
			}
		});
	}
	
	//settle cyclic bill
	$scope.settleCyclicBill=function(){
		$scope.appointmentMasterObj.appointmentId=$scope.appointmentId;
		$rootScope.startLoader();
		var param = {
				encounterMasterDto : $scope.appointmentMasterObj,
				paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
				orderDetailsList:$scope.orderDetailsList,
				isNursingAssessmentRequired : $scope.isNursingAssessmentRequired,
				transferToActiveDeposit :$scope.transferToActiveDepositFact,
				paymentDepositDetailsList : $scope.finalAvailableDeposits,
				totalDepositConsumption : $scope.consumedAmt
		}
		//return false;
		//param.paymentCollectionDto.listBillingPaymentDetailsReqDto =$scope.listPaymentDetails; 
		console.log("param",param);
		var URI = BASE_URL + ROOT_URL + API + BILLING + "/encounter/collect/payment";
		BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
			if (response.data.status == "success")
			{
				
				$rootScope.stopLoader();
/*				var data =  {
		                 "appName": "HIS",
		                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		                 "reportDesignFilePath": "opdbillreceipt.rpt",
		                 "reportFormat": "PDF",
		                 "exportFileName": "PatientBill",
		                 "reportParams": [
		                   {
		                  "paramName": "@billid",
		                  "paramType": "Integer",
		                  "paramValue": response.data.object
		                }
		                 ]
		      };
				 reportService.generate(REPORT_API_URI1,data);*/
				var data =  {
						
		                 "appName": "HIS",
		                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		                 "reportDesignFilePath": "OPDCaseSheet_OPDBill_Receipt.rpt",
		                 "reportFormat": "PDF",
		                 "exportFileName": "PatientBill",
		                 "reportParams": [
		                   {
		                  "paramName": "@EncounterID",
		                  "paramType": "Integer",
		                  "paramValue": response.data.object.object.encounterId
		                }]
		                 
		      };
				 reportService.generate(REPORT_API_URI1,data);
				//$state.go('billList');
					$scope.initPatientSearchObj();
					$scope.initAppointmentMasterObj();
					$scope.initKinDetails();
					$scope.serviceOrderList = [];
					$scope.totalServiceAmt = 0;
					$scope.patientList = [];
					$scope.secondTabLevel = 1 ;
					$scope.encounterList=[];
			}
		});
	
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
		
		$scope.secondTabLevel = 1 ;
	$scope.saveFutureServicesEncounter=function()
	{
		$scope.appointmentMasterObj.listServiceMasterDto=[];
		
		angular.forEach($scope.futureServiceList,function(value,index)
		{
			if(value.checkOption=='Y')
			{
				$scope.appointmentMasterObj.listServiceMasterDto.push(value); 
			}
		  });
		 if(Object.keys($scope.appointmentMasterObj.listServiceMasterDto).length >0)
		 {
			 $("#futureServicesModal").hide();
				$scope.saveAppointmentMaster();
		 }else{
			 growl.warning("Please select at least one serivce..!!",{
					title : WARNING_MSG
				});
				return;
		 }
		
		console.log("accepted Services",$scope.appointmentMasterObj.listServiceMasterDto);
	}
	
	$scope.cancelFutureServicesEncounter=function(){
		$("#futureServicesModal").hide();
		$scope.appointmentMasterObj.listServiceMasterDto = [];
		$scope.saveAppointmentMaster();
	}

	$scope.futureServiceList = [];
	$scope.saveFutureServices=function()
	{
		$scope.saveAppointmentMaster();
	}
	
	
	//Check duplicate speciality
	  var isSpecialityExists;
		function checkSpecialityIdExistsOrNot(arr, val) {
			return arr
					.some(function(arr) {
						if(val.specialityId === arr.specialityId)
							{
								return true;
							}
						else
							{
								return false;
							}
					});
		}
		
	$scope.reviewSetObjectNew = function(){
		
		if($scope.selectedOrderDetailIdsList.length==0)
		{
			growl.warning("Please select service.",{
				title : WARNING_MSG
			});
			return;
		}
		else{
				angular.element('#reviewAppointmentServicesId').modal('hide');
				$scope.reviewSpecialityList = [];
				$scope.reviewSpecialityList = angular.copy($scope.specialityMasterList);
				$scope.specialityMasterList = [];
				var le = 1;
				angular.forEach($scope.selectedOrderDetailIdsList, function(value, key) {

					angular.forEach($scope.reviewSpecialityList, function(value1, key) {
						 if(value1.specialityId == value.reviewAgainstSplId){
							 var newArr = {specialityId : value.reviewAgainstSplId}
							 isSpecialityExists=checkSpecialityIdExistsOrNot($scope.specialityMasterList,newArr)
							  if (!isSpecialityExists) {
								  $scope.specialityMasterList.push(value1);
							  }
							 
						 }
					 });
					if(le == $scope.selectedOrderDetailIdsList.length){
						if($scope.specialityMasterList.length == 1){
							$scope.appointmentMasterObj.deptId = $scope.specialityMasterList[0].specialityId;
							$scope.getDoctorBySpeciality($scope.appointmentMasterObj.deptId,1);
						}
					}
					le++;
				 });
				
				$scope.appointmentMasterObj.encounterTypeId = 6;
		}
	}
	
    $scope.reviewAppointmentNotRequired = function()
    {
        $scope.appointmentMasterObj.encounterTypeId = 1;
    }

    
	$scope.initReviewAppointment = function()
	{
		$scope.checkAllFuture = 'N';
		this.$parent.$parent.checkAllFuture = 'N';
		angular.element('#reviewAppointmentServicesId').modal('show');
		/*$scope.selectedOrderDetailIdsList = [];
		$scope.encounterList = [];
		$scope.initReferralObj();
		$scope.encounterDetailsObj = {};
		$scope.appointmentMasterObj.encounterTypeId = 6;*/
		
	}
	
	$scope.reviewAppointmentNotRequired = function()
	{
		$scope.appointmentMasterObj.encounterTypeId = 1;
	}
	
/*	$scope.initReviewAppointmentTest = function()
	{
		$scope.selectedOrderDetailIdsList = [];
		$scope.encounterList = [];
		$scope.initReferralObj();
		$scope.encounterDetailsObj = {};
		//$scope.appointmentMasterObj.encounterTypeId = 6;
		
		
		
	}*/
	
	
	$scope.checkAllFutureApp = function(flag){
		if(flag == 'Y')
			{
			  $scope.selectedOrderDetailIdsList = [];
			  var i = 0;
			  angular.forEach($scope.reviewServiesList, function(value, key) {
				  $scope.reviewServiesList[i].flg = 'Y';
				  $("#rev_"+key).prop("checked", true);
				  $scope.selectedOrderDetailIdsList.push(value);
				  i++;
				 });
			}else{
				var i=0;
				$scope.selectedOrderDetailIdsList = [];
				/*var reviewServiesListTemp = angular.copy($scope.reviewServiesList);
				$scope.reviewServiesList = [];
				$scope.reviewServiesList = reviewServiesListTemp;*/
				 angular.forEach($scope.reviewServiesList, function(value, key) {
					 // $scope.reviewServiesList[i].flg = 'N';
					 $("#rev_"+key).prop("checked", false);
					  i++;
					 });
			}
	}
	
	$scope.checkSigleClick = function(flg,idx){
		if(!flg){
			//$scope.checkAllFuture = 'N';
			console.log($scope.selectedOrderDetailIdsList);
			var tmpList = angular.copy($scope.selectedOrderDetailIdsList);
			
			var ordDetIdTMp = 0;
			var i = 1;
			 angular.forEach($scope.reviewServiesList, function(value, key) {
				if(key == idx){
					ordDetIdTMp = value.orderDetailsId
				}
				if(i == $scope.reviewServiesList.length){
					$scope.selectedOrderDetailIdsList = [];
					 angular.forEach(tmpList, function(value, key) {
							if(value.orderDetailsId != ordDetIdTMp){
								$scope.selectedOrderDetailIdsList.push(value);
							}
							 });
				}
				i++;
				 });
			 
		
			 
			$("#rev_"+idx).prop("checked", false);
			this.$parent.$parent.checkAllFuture = 'N';
		}else{
			
			var i = 1;
			 angular.forEach($scope.reviewServiesList, function(value, key) {
				if(key == idx){
					$scope.selectedOrderDetailIdsList.push(value);
				}
				i++;
				 });
			 
		}
	}
	
	
	$scope.clearDatatSD = function(){

	$scope.encounterDetailsContractList = [];
	$scope.initPatientSearchObj();
	$scope.initAppointmentMasterObj();
	$scope.initKinDetails();
	$scope.serviceOrderList = [];
	$scope.totalServiceAmt = 0;
	$scope.patientList=[];
	$scope.secondTabLevel = 1 ;
	$scope.expiryDateDisabled=true;
	$scope.aliseFlag = false;
	
	var checkbox = document.getElementById('sameAsLocalCheckbox');
	if (checkbox) {
	    checkbox.checked = false;
	}
	
	
	var checkbox1 = document.getElementById('tele');
	if (checkbox1) {
	    checkbox1.checked = false;
	}
	
	$scope.patientObj = {

			prefixId : '',
			firstName : '',
			middleName : '',
			lastName : '',
			genderId : '',
			countryCallingCode : '',
			email : '',
			nationalityId : '',
			raceId : '',
			maritalStatusId : '',
			mobileNumber : '',
			dob : '',
			identificationTypeId : '',
			identificationNumber : '',
			isVip : '',
			organizationId : $scope.organizationId,
			unitId : $scope.unitId,
			updatedBy : $scope.userId,
			createdBy : $scope.userId,
			status : 'A',
			patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
			aliseName:'',
			identificationExpiryDate:'',
			occupationId:'',
			registrationTypeId:1,
			previousId:'',
			patientId:0,
			patientDetailsId:'',
			whatsappCallingCode : '',
			whatsappNo:'',
			skypeId:'',
			address : '',
			countryId : '',
			stateId : '',
			districtId : '',
			cityId :'',
			areaId : '',
			zipCode : '',
			phoneCode : '',
			phoneNumber : '',
			permanentAddress:'',
			permanentCountryId:'',
			permanentStateId:'',
			permanentDistrictId:'',
			permanentCityId:'',
			permanentAreaId:'',
			permanentZipCode:'',
			permanentPhoneCode:'',
			permanentPhoneNumber:'',
			fileSequenceNumber:'',
			bloodGroup:'',
			isAlreadyExist:'N'
		};
	 $scope.patientAdmittedFlag = false;
		$scope.ageDay = 0;
		$scope.ageMonth = 0;
		$scope.ageYear = 0;
		
		if($scope.unitCountryId > 0)
		{
		  $scope.patientObj.countryId  = $scope.unitCountryId;
		  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
		  $scope.patientObj.stateId  =  $scope.unitStateId;
		  $scope.patientObj.districtId = $scope.unitDistrictId;
		  $scope.patientObj.cityId = $scope.unitCityId;
		  $scope.patientObj.areaId = $scope.unitAreaId;
		  $scope.patientObj.zipCode = $scope.unitZipCode;
		  $scope.getStateByCountry($scope.patientObj.countryId,'L');
		  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
		  $scope.getDistrictByState($scope.patientObj.stateId,'L');
		  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
		  $scope.getAreaByCity($scope.patientObj.cityId,'L');
		  $scope.selectFlag = false;
		}
		 $scope.filepreview = "";
		$scope.datepicker="";
        $scope.areaNotExist = 'N';
	    angular.element('#datepickerExpiry').scope().datepickerExpiry = "";
	    $('#pieChartContainer').html(''); //remove canvas from container
        $('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>');
        $scope.patientImage1 = null;
        angular.element("input[type='file']").val(null);

	}
    $scope.flagKin =true;
	$scope.saveAppointmentMaster = function()
	{
		if($scope.registrationFieldObj.sourceId== "Y"){
			if($scope.appointmentMasterObj.sourceId == undefined || $scope.appointmentMasterObj.sourceId == null || $scope.appointmentMasterObj.sourceId ==""){
				$scope.secondTabLevel = 1 ;
				growl.warning("Please select source",{
					title : ERROR_MSG
				});
				return false;
			}
		}
		if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == 0){
			//$scope.tab=1;
			$scope.secondTabLevel = 1 ;
			/*growl.warning("Please Select Patient",{
				title : WARNING_MSG
			});
			return;*/
			$scope.$broadcast('show-errors-check-validity');
			// $scope.secondTabLevel=1;
	        if($scope.patientRegistrationform.$valid){
	        	//alert("valid");
	        	
	        	var contryCode=$scope.patientObj.countryCallingCode;
	        	 // validating country code
				 var regEx = /^[+-]?\d+$/;
				      if(!regEx.test(contryCode)){
				    	  growl.warning("Please enter valid country calling code",{
								title : 'Warning !'
								});
								return false;
				      } 
	        	
	        	var mobileNo=$scope.patientObj.mobileNumber;
//	        	if(contryCode==91){
//	        		if(mobileNo.length==10){
//
//						}else{
//							growl.warning("Please enter valid mobile number",{
//								title :'Warning !'
//								});
//								$('#mobileNo').focus();
//								return false;
//	        		}
//	        	}else{
		        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
		        			
						}else{
							growl.warning("Please enter valid mobile number",{
								title : 'Warning !'
								});
								$('#mobileNo').focus();
								return false;
	        		}
//	        	}


	        	if($scope.patientObj.patientCategoryId==null || $scope.patientObj.patientCategoryId==undefined||$scope.patientObj.patientCategoryId==""){
	        		growl.warning("Please select patient category",{
								title : 'Warning !'
								});
								
								return false;
	        	}
				/*if($scope.patientRegistrationform2.$valid){*/
	        		 if($scope.patientObj.phoneCode !='' && $scope.patientObj.phoneCode!=undefined && $scope.patientObj.phoneCode!=null){
	        			 var phoneCode=$scope.patientObj.phoneCode;
			        	 // validating country code
						 var regEx = /^[+-]?\d+$/;
						      if(!regEx.test(phoneCode)){
						    	  growl.warning("Please enter valid country calling code",{
										title : 'Warning !'
										});
										return false;
						      } 
	        		 }
	        		 //test start
	        		 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
       			 }
	        		 if($scope.patientObj.permanentZipCode !='' && $scope.patientObj.permanentZipCode!=undefined && $scope.patientObj.permanentZipCode!=null){
       			 }
	        		 if($scope.patientObj.permanentPhoneCode !='' && $scope.patientObj.permanentPhoneCode!=undefined && $scope.patientObj.permanentPhoneCode!=null){
	        			 var permanentPhoneCode=$scope.patientObj.permanentPhoneCode;
			        	 // validating country code
						 var regEx = /^[+-]?\d+$/;
						      if(!regEx.test(permanentPhoneCode)){
						    	  growl.warning("Please enter valid country calling code",{
										title : 'Warning !'
										});
										return false;
						      } 
	        		 }
	        		 if($scope.patientObj.email == null){
	        			 $scope.patientObj.email = "";
	        		 }
	        	 if($scope.patientObj.email != "")
	           			{
		           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		           		 var email = $scope.patientObj.email;
		           		    if (!filter.test(email)) {

	        				    growl.error(EMAIL_MSG,{
									title : ERROR_MSG

								});
	        				    $('#email').focus();
	        				    return false;
		           		    }
		           		    else{
		           		    	if($scope.patientObj.phoneNumber == undefined)
		           		    			$scope.patientObj.phoneNumber='';
		           		    	if($scope.patientObj.phoneNumber == null)
		           		    			$scope.patientObj.phoneNumber='';
		           		     if($scope.patientObj.phoneNumber != '')
				        		{
				        		}
		           		     else
		           		    	 {
		           		    	 }
		           		    }
	           			}
	        		 else
	        			 {
	        			 if($scope.patientObj.phoneNumber == undefined)
	        				 $scope.patientObj.phoneNumber='';
	        			 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
	        			 }
	        			 }
	        		
	        		 //test end
	        }else{
	        	growl.warning(" Please select patient or add patient details..!! ",{
					title : WARNING_MSG
				});
	        	return false;
	        }
		}
		
		if($scope.appointmentMasterObj.referralTypeId !=null 
				   && $scope.appointmentMasterObj.referralTypeId !=""
				   && $scope.appointmentMasterObj.referralTypeId !=undefined){
			
			   if($scope.appointmentMasterObj.referralDesc == null 
					   || $scope.appointmentMasterObj.referralDesc == ""
					   || $scope.appointmentMasterObj.referralDesc == undefined){
				   
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Referral By",{
						title : ERROR_MSG
					});
					return;
					}
				   
	 	}
		if($scope.appointmentMasterObj.encounterTypeId == '' || $scope.appointmentMasterObj.encounterTypeId == 0 ){
			//$scope.tab=1;
			$scope.secondTabLevel = 1 ;
			growl.warning("Please select visit type",{
				title : ERROR_MSG
			});
			return;
		}
		if(($scope.appointmentMasterObj.deptId == '' || $scope.appointmentMasterObj.deptId == 0) && $scope.appointmentMasterObj.encounterTypeId != 3 && $scope.appointmentMasterObj.encounterTypeId != 5 && $scope.appointmentMasterObj.encounterTypeId!=7){
			//$scope.tab=1;
			$scope.secondTabLevel = 1 ;
			growl.warning("Please Select Department",{
				title : ERROR_MSG
			});
			return;
		}
		
		if($scope.appointmentMasterObj.encounterTypeId == 1 &&  $scope.deptWiseAppointment == "N")
			{
				if($scope.appointmentMasterObj.doctorId == ''  || $scope.appointmentMasterObj.doctorId == undefined){
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Doctor",{
						title : ERROR_MSG
					});
					return;
				}
			}
		else if ($scope.appointmentMasterObj.encounterTypeId == 2)
			{
			
			$scope.appointmentMasterObj.userDefinedVisitTypeId="4";
				if($scope.appointmentMasterObj.subDeptId == '' ){
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Sub Department",{
						title : ERROR_MSG
					});
					return;
				}
				if($scope.appointmentMasterObj.modalityId == '' || $scope.appointmentMasterObj.modalityId == 0){
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Modality",{
						title : ERROR_MSG
					});
					return;
				}
			}
		else if($scope.appointmentMasterObj.encounterTypeId == 4)
			{
				if($scope.appointmentMasterObj.deptId == '' ||  $scope.appointmentMasterObj.deptId == null || $scope.appointmentMasterObj.deptId == undefined){
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					growl.warning("Please Select Department",{
						title : ERROR_MSG
					});
					return;
				}
				
			}
		
		else if($scope.appointmentMasterObj.encounterTypeId == 6)
		{
			if($scope.appointmentMasterObj.deptId == '' ){
				//$scope.tab=1;
				$scope.secondTabLevel = 1 ;
				growl.warning("Please Select Department",{
					title : ERROR_MSG
				});
				return;
			}
			/*if($scope.appointmentMasterObj.subDeptId == '' ){
				//$scope.tab=1;
				$scope.secondTabLevel = 1 ;
				growl.warning("Please Select Sub Department",{
					title : ERROR_MSG
				});
				return;
			}*/
			if($scope.selectedOrderDetailIdsList.length==0)
				{
				growl.warning("Please select service.",{
					title : WARNING_MSG
				});
				return;
				}
			else
			  {
				var encounterIds = [];
				var orderDetailsIds = [];
				angular.forEach($scope.selectedOrderDetailIdsList, function(value, key) {
					encounterIds.push(value.encounterId);	
				 });
					if(new Set(encounterIds).size==1)
					{
						var iterator = new Set(encounterIds).values();
						var encounterId =iterator.next().value;
				        $scope.appointmentMasterObj.reviewAgainstAncId = encounterId;
				        angular.forEach($scope.selectedOrderDetailIdsList, function(value, key) {
				        	if(value.encounterId = encounterId)
				        		{
				        		  orderDetailsIds.push(value.orderDetailsId);
				        		}
						 });
				        
				        $scope.appointmentMasterObj.orderDetailsIds = orderDetailsIds;
					}
					else{
						growl.warning("Please Select Unique encounter.",{
							title : ERROR_MSG
						});
						return;
					}
			  }
			
		}

		
		
		
		//var array = $scope.appointmentMasterObj.subDeptId.split('_');
		
		//----------------------------------!@#$%^&*(
		var objForGettingDoctorServiceId = {
				doctorId 		: $scope.appointmentMasterObj.doctorId,
				specialityId 	: $scope.appointmentMasterObj.deptId,
				unitId 			: $scope.unitId,
				organizationId 	: $scope.organizationId
			};
		$scope.currentDate = new Date();
		$scope.currentDate = moment(
				$scope.currentDate).format(
				'DD-MM-YYYY HH:mm:ss');
		$scope.currentDateAppDate = moment(
				new Date()).format(
				'YYYY-MM-DD HH:mm:ss');
		
		//appointmentObject	
		if($scope.appointmentMasterObj.appointmentId > 0)
			{			
			
			
			/*if($scope.appointmentMasterObj.encounterTypeId == 1){
				 $scope.deptWiseAppointment='N';
			}*/
			
			if(($scope.slotId == '' || $scope.slotId == null) && $scope.appointmentMasterObj.encounterTypeId != 5)
			{
			if($scope.appointmentMasterObj.encounterTypeId == 3)
				{
					//$scope.tab=1;
				$scope.secondTabLevel = 1 ;
					growl.warning("No slot available for EHC.",{
						title : WARNING_MSG
					});
					return;
				}
			else if($scope.appointmentMasterObj.encounterTypeId == 1 || $scope.appointmentMasterObj.encounterTypeId == 2 || $scope.appointmentMasterObj.encounterTypeId == 6 || $scope.appointmentMasterObj.encounterTypeId == 9)
				{
					//$scope.tab=1;
				if($scope.isAppointmentRequired == 'Y'){
				$scope.secondTabLevel = 1 ;
					growl.warning(" Please select the time slot ",{
						title : WARNING_MSG
					});
					return;
				}
				}
			
			}
			else if($scope.appointmentMasterObj.encounterTypeId == 3 && ($scope.EHCpackageId == 0 || $scope.EHCpackageId == null))
			{
				$scope.tab=1;
				growl.warning("Please Select Package",{
					title : ERROR_MSG
				});
				return;
			}
			else 
				{
				$scope.appointmentMasterObj.appointmentObject  = null;
				$scope.appointmentMasterObj.unitTokenNumber =  $scope.encounterDetailsObj.tokenNumber;
				$scope.appointmentMasterObj.ehcPackageId = $scope.EHCpackageId;
				$scope.appointmentMasterObj.packageBasePrice = $scope.packageBasePrice;
				}
			
			}
		else{
			$scope.appointmentMasterObj.unitTokenNumber =  null;
			$scope.appointmentMasterObj.currentDate =  moment(new Date()).format('YYYY-MM-DD');
			if($scope.isAppointmentRequired == 'Y' && $scope.appointmentMasterObj.encounterTypeId != 4)
				{
					if((this.slotId == '' || this.slotId == null) && $scope.appointmentMasterObj.encounterTypeId != 5)
						{
						if($scope.appointmentMasterObj.encounterTypeId == 3)
							{
								//$scope.tab=1;
							$scope.secondTabLevel = 1 ;
								growl.warning("No slot available for EHC.",{
									title : WARNING_MSG
								});
								return;
							}
						else if($scope.appointmentMasterObj.encounterTypeId == 1 || $scope.appointmentMasterObj.encounterTypeId == 2 || $scope.appointmentMasterObj.encounterTypeId == 6 || $scope.appointmentMasterObj.encounterTypeId == 9)
							{
								//$scope.tab=1;
							    $scope.secondTabLevel = 1 ;
								 growl.warning(" Please select the time slot ",{
									title : WARNING_MSG
								 });
								return;
							}
						
						}
					else
						{
						
						$scope.appointmentMasterObj.ehcPackageId = $scope.EHCpackageId;
						$scope.appointmentMasterObj.packageBasePrice = $scope.packageBasePrice;
						
							var dataAppointmentObj = {
									patientId : $scope.appointmentMasterObj.patientId,
									remark : $scope.appointmentMasterObj.remark,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									status : 'A',
									appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
									priorityId : $scope.priorityId,
									firstName : $scope.patientDetails.firstName,
								    mobileNumber :$scope.patientDetails.mobileNumber,
								    generateToken : 'Y',
								    appointmentDate : $scope.currentDateAppDate,
								    isDirectEncounter : 'Y',
									ehcPackageId : $scope.EHCpackageId,
									packageBasePrice : $scope.packageBasePrice,
									slotStatusId : $scope.slotStatusId !=null ? $scope.slotStatusId : null,
 									//allowedOverBookingSlots : $scope.allowedOverBookingSlots,
											// isTeleconsultationAppointment : $scope.isTeleconsultationAppointment
											isTeleconsultationAppointment:
												($scope.isTeleconsultationAppointment == true || 
												$scope.isTeleconsultationAppointment == 1) ? 1 : 0
								}
							//alert($scope.isTeleconsultationAppointment);
							//return false;
							if($scope.appointmentMasterObj.encounterTypeId != 5)
								{
								   dataAppointmentObj.slotId = $scope.slotId;
								   dataAppointmentObj.appointmentDate2 = $scope.appointmentDate;
								   dataAppointmentObj.fromTimeString = $scope.fromTime;
								   dataAppointmentObj.slotStatusId = $scope.slotStatusId;
								   dataAppointmentObj.doctorName = $scope.doctorName;
								   dataAppointmentObj.isFromEncounter = 'Y';
								}
							else
								{
								  dataAppointmentObj.slotId = null;
								  dataAppointmentObj.appointmentDate2 = null;
								  dataAppointmentObj.fromTimeString =null;
								  dataAppointmentObj.slotStatusId =null;
								  dataAppointmentObj.doctorName = null;
								  dataAppointmentObj.isFromEncounter = 'Y';
								}
							if($scope.appointmentMasterObj.encounterTypeId == 3)
							{
								
								if($scope.EHCpackageId == 0 || $scope.EHCpackageId == null)
								{
									$scope.tab=1;
									growl.warning("Please Select Package",{
										title : ERROR_MSG
									});
									return;
								}else{
									 dataAppointmentObj.ehcPackageId = $scope.EHCpackageId;
									 dataAppointmentObj.packageBasePrice = $scope.packageBasePrice;
									 $scope.appointmentMasterObj.ehcPackageId = $scope.EHCpackageId;
									 $scope.appointmentMasterObj.packageBasePrice = $scope.packageBasePrice;
								}
								//dataAppointmentObj.ehcPackageId = null;
							}
							$scope.appointmentMasterObj.appointmentObject  = dataAppointmentObj;
						}
				}
			else{
				
				var dataAppointmentObj = {
						patientId : $scope.appointmentMasterObj.patientId,
						remark : $scope.appointmentMasterObj.remark,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						createdDate : $scope.currentDate,
						updatedDate : $scope.currentDate,
						createdBy : $scope.userId,
						updatedBy : $scope.userId,
						status : 'A',
						appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
						priorityId : $scope.priorityId,
						firstName : $scope.patientDetails.firstName,
					    mobileNumber :$scope.patientDetails.mobileNumber,
					    generateToken : 'Y',
					    appointmentDate : $scope.currentDateAppDate,
					    isDirectEncounter : 'Y',
						ehcPackageId : $scope.EHCpackageId,
						packageBasePrice : $scope.packageBasePrice,
						slotStatusId : $scope.slotStatusId !=null ? $scope.slotStatusId : null,
							//allowedOverBookingSlots : $scope.allowedOverBookingSlots,
								// isTeleconsultationAppointment : $scope.isTeleconsultationAppointment
								isTeleconsultationAppointment:
								($scope.isTeleconsultationAppointment == true || 
								$scope.isTeleconsultationAppointment == 1) ? 1 : 0
					}
				
				
				if($scope.appointmentMasterObj.encounterTypeId != 5)
				{
				   dataAppointmentObj.slotId = null,
				   dataAppointmentObj.appointmentDate2 = null;
				   dataAppointmentObj.fromTimeString = null;
				   dataAppointmentObj.doctorName = $scope.doctorName;
				   dataAppointmentObj.isFromEncounter = 'Y';
				}
			else
				{
				  dataAppointmentObj.slotId = null;
				  dataAppointmentObj.appointmentDate2 = null;
				  dataAppointmentObj.fromTimeString =null;
				  dataAppointmentObj.slotStatusId =null;
				  dataAppointmentObj.doctorName = null;
				  dataAppointmentObj.isFromEncounter = 'Y';
				}
			if($scope.appointmentMasterObj.encounterTypeId == 3)
			{
				
				if($scope.EHCpackageId == 0 || $scope.EHCpackageId == null)
				{
					$scope.tab=1;
					growl.warning("Please Select Package",{
						title : ERROR_MSG
					});
					return;
				}else
					{
					 dataAppointmentObj.ehcPackageId = $scope.EHCpackageId;
					 dataAppointmentObj.packageBasePrice = $scope.packageBasePrice;
					 $scope.appointmentMasterObj.ehcPackageId = $scope.EHCpackageId;
					 $scope.appointmentMasterObj.packageBasePrice = $scope.packageBasePrice;
					}
				//dataAppointmentObj.ehcPackageId = null;
			}
			$scope.appointmentMasterObj.appointmentObject  = dataAppointmentObj;
			
				
			}
			
		}
		
		/*if($scope.appointmentMasterObj.encounterTypeId == 7){
			$scope.appointmentMasterObj.appointmentId=$scope.appointmentId;
		}
		*/
		if($scope.appointmentMasterObj.encounterTypeId == 4)
		{
//			$scope.appointmentMasterObj.appointmentObject  = null;
		}
		if($scope.appointmentMasterObj.kinDetailsId == "" && $scope.flagKin == true){
			$scope.tab=2;
			$scope.secondTabLevel = 2 ;
			 var flagKin=false;
			 var checkKinPri = false;
			 var checkKinName = false;
			 var checkKinMobile=false;
			 var checkKinRelation=false;
			 var checkKinPhone=false;
			 var checkKinAddress=false;
			 var checkKinContry=false;
			 var checkKinState=false;
			 var checkKinDistrict=false;
			 var checkKinCity=false;
			 var checkKinArea=false;
			 var checkKinPost=false;
			 var checkKinIdenti=false;
			 var checkKinIdentiNo=false;
			 if($scope.kinDetailsList.length >0){
				 var i=1;
				 angular.forEach($scope.kinDetailsList,function(value,key){

						if($scope.registrationFieldObj.kinprefix== "Y"){
							if(value.prefixId == undefined || value.prefixId == null || value.prefixId ==""){
								checkKinPri = true;
							}
						}
						 if($scope.registrationFieldObj.kinName== "Y"){
							if(value.kinName == undefined || value.kinName == null || value.kinName ==""){
								checkKinName = true;
							}
						}
						 if($scope.registrationFieldObj.relationId== "Y"){
								if(value.relationId == undefined || value.relationId == null || value.relationId ==""){
									checkKinRelation = true;
								}
							}
						 if($scope.registrationFieldObj.kinMobileNo== "Y"){
								if(value.mobileNo == undefined || value.mobileNo == null || value.mobileNo ==""){
									checkKinMobile = true;
								}
							}
						
						 if($scope.registrationFieldObj.phoneNo== "Y"){
								if(value.phoneNo == undefined || value.phoneNo == null || value.phoneNo ==""){
									checkKinPhone=true;
								}
							}
						 if($scope.registrationFieldObj.kinAddress== "Y"){
								if(value.address == undefined || value.address == null || value.address ==""){
									checkKinAddress=true;
								}
							}
						 if($scope.registrationFieldObj.countryId== "Y"){
								if(value.countryId == undefined || value.countryId == null || value.countryId ==""){
									checkKinContry=true;
								}
							}
						 if($scope.registrationFieldObj.stateId== "Y"){
								if(value.stateId == undefined || value.stateId == null || value.stateId ==""){
									checkKinState=true;
								}
							}
						 if($scope.registrationFieldObj.districtId== "Y"){
								if(value.districtId == undefined || value.districtId == null || value.districtId==""){
									checkKinDistrict=true;
								}
							}
							 if($scope.registrationFieldObj.cityId== "Y"){
								if(value.cityId == undefined || value.cityId == null || value.cityId ==""){
									checkKinCity=true;
								}
							}
							 if($scope.registrationFieldObj.areaId== "Y"){
								if(value.areaId == undefined || value.areaId == null || value.areaId ==""){
									checkKinArea=true;
								}
							}
							 if($scope.registrationFieldObj.postCode== "Y"){
								if(value.postCode == undefined || value.postCode == null ||value.postCode ==""){
									checkKinPost=true;
								}
							}
							 if($scope.registrationFieldObj.identificationId== "Y"){
								if(value.identificationId == undefined || value.identificationId == null ||value.identificationId ==""){
									checkKinIdenti=true;
								}
							}
							 if($scope.registrationFieldObj.identificationNo== "Y"){
								if(value.identificationNo == undefined || value.identificationNo == null || value.identificationNo ==""){
									checkKinIdentiNo=true;
								}
							}
				 });
				 
if($scope.visitTypeToCheckOPORIP != undefined  && $scope.visitTypeToCheckOPORIP != null && $scope.visitTypeToCheckOPORIP != "" && $scope.visitTypeToCheckOPORIP!= 1 )
				 {
//				 if(checkKinPri == true){
//					 growl.warning("Please Add Prefix For Next Of KIN",{
//						});
//						checkKin = true;
//						return false;
//				 }
//				 if(checkKinName == true){
//					 growl.warning("Please Add Name For Next Of KIN",{
//						});
//						checkKin = true;
//						return false;
//				 }
//				 if(checkKinRelation == true){
//					 growl.warning("Please Add Relation For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
				 if(checkKinMobile == true){
					 growl.warning("Please Add Mobile No For Next Of KIN",{
						});
						checkKin = true;
						return false;
				 }
//				 if(checkKinPhone == true){
//					 growl.warning("Please Add Phone No For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinAddress == true){
//					 growl.warning("Please Add Address For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinContry == true){
//					 growl.warning("Please Add Country For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinState == true){
//					 growl.warning("Please Add State For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinDistrict == true){
//					 growl.warning("Please Add District For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinCity == true){
//					 growl.warning("Please Add City For Next Of KIN",{
//						}); 
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinArea == true){
//					 growl.warning("Please Add Area For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinPost == true){
//					 growl.warning("Please Add Post Code For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinIdenti == true){
//					 growl.warning("Please Add Identification Type For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
//				 if(checkKinIdentiNo == true){
//					 growl.warning("Please Add Identification No For Next Of KIN",{
//						});
//					 checkKin = true;
//					 return false;
//				 }
			  }
			 }
			 else{
				 flagKin==false;
    		 }
		}

		
		
		if($scope.appointmentMasterObj.encounterTypeId == 5)
		{
			$scope.appointmentMasterObj.specialityId = $scope.casualtySpecialityId;
			$scope.appointmentMasterObj.deptId = $scope.casualtySpecialityId;//need to add casualty speciality Id from unitApplicationConfig
		}
		else
			{
			 $scope.appointmentMasterObj.specialityId = $scope.appointmentMasterObj.deptId;
			}
		
		//var array = $scope.appointmentMasterObj.subDeptId.split('_');
		if($scope.appointmentMasterObj.encounterTypeId == 2){
		var array = $scope.appointmentMasterObj.subDeptId.split('_');
		$scope.appointmentMasterObj.subDeptId = array[1];
		//$scope.appointmentMasterObj.subSpecialityId = $scope.appointmentMasterObj.subDeptId;
		}
		
	      if($scope.appointmentMasterObj.patientClassId == '' || $scope.appointmentMasterObj.patientClassId == null || $scope.appointmentMasterObj.patientClassId == undefined)
          {
          $scope.appointmentMasterObj.patientClassId = null;
          }
		
		isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
		  if (isPriorityZeroExists) {
			
			  growl.warning("Please select priority of all payment entitlement list..!!",{	
				title : "WARNING"
			});
			 //$scope.tab = 3;
			  $scope.secondTabLevel =1;
			  $scope.setSecondTabLevel($scope.secondTabLevel);
			return;
			
		  }
		  else{
		 $scope.kinDetailsFlag =false;

//				if(value.kinName != ''){
//					if(value.mobileNo !=""){
//						var contryCode=$scope.countryCallingCode;
//			        	var mobileNo=value.mobileNo;
//			        	if(contryCode==91){
//			        		if(mobileNo.length==10){
//			        			
//								}else{
//									growl.error("Please enter valid mobile number",{
//										title : ERROR_MSG
//										});
//										$('#mobileNo').focus();
//										$scope.setSecondTabLevel(2);
//										$scope.kinDetailsFlag =true;
//										return false;
//			        		}
//			        	}else{
//				        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
//				        			
//								}else{
//									growl.error("Please enter valid mobile number",{
//										title : ERROR_MSG
//										});
//										$('#mobileNo').focus();
//										$scope.setSecondTabLevel(2);
//										$scope.kinDetailsFlag =true;
//										return false;
//			        		}
//			        	}	
//					}
//			        	
//				}
				if ($scope.registrationFieldObj.kinMobileNo == 'Y') {
				    var invalidNo = false;

				    angular.forEach($scope.kinDetailsList, function (value, index) {
				        var mobileNoNextOfKin = value.mobileNo;

				        if (mobileNoNextOfKin != null && mobileNoNextOfKin != undefined) {
				            if (mobileNoNextOfKin.length == $scope.registrationFieldObj.mobileDigit) {
				                // Exit the loop early if an invalid number is found
				            }else{
								 invalidNo = true;
				                return false;
							}
				        }
				    });

				    if (invalidNo) {
				        growl.warning("Please enter Kin details", {
				            title: 'Warning!'
				        });
				        return false;
				    }
				}
		var URI = BASE_URL + ROOT_URL + "/api/opd/encounter";
		
		var sponserList = [];
		angular.forEach($scope.paymentEntitleMentList, function (value, key) {
			
			var newArr = {};
			newArr.organizationId = $scope.organizationId ;
			newArr.unitId = $scope.unitId;
			newArr.visitTypeId = 1;
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
//			newArr.validityTo = value.endDate;
//			newArr.validityFrom = value.startDate;
			
			if(value.paymentEntitlementId != 1){

				 var dff =  moment(value.startDate,$scope.dateFormat.toUpperCase()+" HH:mm:ss").format("DD-MM-YYYY HH:mm:ss");
				 var dff1 =  moment(value.endDate,$scope.dateFormat.toUpperCase()+" HH:mm:ss").format("DD-MM-YYYY HH:mm:ss");
//				 var dff = value.startDate;
//				 var dff1 =  value.endDate;
				 
				newArr.validityTo = (dff1 != null || dff1 != undefined) ? dff1 : null;
               newArr.validityFrom = (dff != null || dff != undefined) ? dff : null; 
			}else{
				
					newArr.validityTo = null;
					newArr.validityFrom = null;
			}
			
			newArr.employeeNoManual = value.employeeNoManual;
			newArr.employeeName = value.employeeName;
			newArr.relation = value.relation;
			newArr.medicalCardNo = value.medicalCardNo;
			newArr.claimNumber = value.claimNumber;
			
			
			if(value.companyId != $scope.selfPayeeIdCookies && value.priority == 1)
		    {
		     $scope.appointmentMasterObj.contractId = value.contractId;
		     $scope.appointmentMasterObj.companyId = value.companyId;
		    }
//			for adding note of corporate employee verification
			  if($scope.corEmpVerificationFlag && $scope.corEmpObj.typeOfCoEmp==3){
				  newArr.corporateEmpNote=value.corporateEmpNote;
			  }
			
			sponserList.push(newArr);
		});
		$scope.appointmentMasterObj.patientSponsorDetailsDtosList = sponserList;
		
		/*if($scope.selfFlag == "Y")
			{
			 $scope.appointmentMasterObj.slotId =$scope.slotId;
			}*/
	
		 $scope.appointmentMasterObj.slotId =$scope.slotId;
		
		 //for Hospital Visit
		 if($scope.appointmentMasterObj.encounterTypeId == 7){
			 var appointmentObj = {
						patientId : $scope.appointmentMasterObj.patientId,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						createdDate : $scope.currentDate,
						updatedDate : $scope.currentDate,
						createdBy : $scope.userId,
						updatedBy : $scope.userId,
						status : 'A',
						appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
						//priorityId : $scope.priorityId,
						firstName : $scope.patientDetails.firstName,
					    mobileNumber :$scope.patientDetails.mobileNumber,
					    generateToken : 'N',
					    appointmentDate : $scope.currentDateAppDate,
					    isDirectEncounter : 'Y',
						ehcPackageId : $scope.EHCpackageId,
						packageBasePrice : $scope.packageBasePrice,
					}
			 if($scope.appointmentMasterObj.appointmentId!=null){
				 $scope.appointmentMasterObj.appointmentObject  = null;
			 }else{
				 $scope.appointmentMasterObj.appointmentObject  = appointmentObj;
			 }
			
		 }
		
		 
		if($scope.diableDropDown== true && $scope.referralData!=null)
			$scope.appointmentMasterObj.referralMasterDto = $scope.referralData;
		
		
		$scope.appointmentMasterObj.deptWiseAppointment = $scope.deptWiseAppointment;
		$scope.appointmentMasterObj.isNursingAssessmentRequiredForSpeciality = $scope.isNursingAssessmentRequired;
		if($scope.encounterDetailsObj!=null){
			if($scope.encounterDetailsObj.appoitmentDate != undefined || $scope.encounterDetailsObj.appoitmentDate!= null || $scope.encounterDetailsObj.appoitmentDate != "")
			{
				$scope.appointmentMasterObj.appoitmentDate =$scope.encounterDetailsObj.appoitmentDate;
			}else{
				$scope.appointmentMasterObj.appoitmentDate = $scope.encounterDetailsObj.appointmentDate;
			}
			
			$scope.appointmentMasterObj.appoitmentTime = $scope.encounterDetailsObj.fromTimeString;
		}
		
		//$scope.appointmentMasterObj.encounterTime =  moment(new Date()).format("HH:mm");
		$scope.appointmentMasterObj.specialityName = $scope.specialityName;
		if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == null || $scope.appointmentMasterObj.patientId == 0){
			var flag=false;
			$scope.currentDate = new Date();
			$scope.currentDate = moment($scope.currentDate)
					.format('DD-MM-YYYY HH:mm:ss');
			
			$scope.patientObj.createdDate = $scope.currentDate;
			$scope.patientObj.updatedDate = $scope.currentDate;
			$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
			$scope.patientObj.middleName =  ($scope.patientObj.middleName == null || $scope.patientObj.middleName.trim() == "" || $scope.patientObj.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.middleName));
			$scope.patientObj.lastName =   ($scope.patientObj.lastName == null || $scope.patientObj.lastName.trim() == "" || $scope.patientObj.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.lastName));
			$scope.patientObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
	
			
			
			var ConDate = $('#datepickerExpiry').val();
			
			if($scope.expiryDateDisabled==false && (ConDate != '' && ConDate != undefined)){
				var newExDate = new Date($rootScope.getChangedFormatedDateShalseTwo(ConDate));
				$scope.patientObj.identificationExpiryDate = moment(newExDate).format('DD-MM-YYYY HH:mm:ss');
			}
			
//			 var splittedStringArray = $scope.patientObj.identificationTypeId.split("_");
//			 $scope.patientObj.identificationTypeId = splittedStringArray[0]; 
			if($scope.patientObj.identificationTypeId==undefined || $scope.patientObj.identificationTypeId=="")
				$scope.patientObj.identificationTypeId=null;
			
			
		
			if($scope.patientObj.isVip=='N')
				$scope.patientObj.aliseName='';
			
			if($scope.sameAsLocal == 'Y'){
				$scope.patientObj.permanentAddress = $scope.patientObj.address;
				$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
				$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
				$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
				$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
				$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
				$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
				$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
				$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
				}
				
			if($scope.patientObj.identificationTypeId !=null)
			{
				 $scope.patientObjCopy = angular.copy($scope.patientObj);
		      var splittedStringArray = $scope.patientObjCopy.identificationTypeId.split("_");
		      $scope.patientObjCopy.identificationTypeId = splittedStringArray[0];
		      if(splittedStringArray[1]=='Y'){
			      if($scope.patientObjCopy.identificationExpiryDate==undefined || $scope.patientObjCopy.identificationExpiryDate==null || $scope.patientObjCopy.identificationExpiryDate==""){
					   flag=true;
				   }
		      }
		     
			}
			else
			{
				$scope.patientObj.identificationTypeId = null;
				$scope.patientObj.videoCallUrl = $scope.patientDetails.videoCallUrl;
				$scope.patientObj.isAlreadyExist = $scope.patientObj.isAlreadyExist;
				$scope.patientObjCopy = angular.copy($scope.patientObj);
			}
			if(flag){
				growl.error(
						'Please select expiry date',
						{
							title : 'Error!'
						});
				return;
			}
			
			 $scope.patientObj.videoCallUrl = $scope.patientDetails.videoCallUrl;
			 console.log($scope.patientObj);
			 $scope.patientObj.isAlreadyExist = $scope.patientObj.isAlreadyExist;
			 $scope.appointmentMasterObj.patientRegistrationDto = $scope.patientObjCopy;
		}
		
		console.log('Encounter obj',$scope.appointmentMasterObj);
		var appointmentMasterObjCopy = angular.copy($scope.appointmentMasterObj);
		console.log(appointmentMasterObjCopy);
		//return false;
		if($scope.appointmentMasterObj.encounterTypeId == 2){
		$scope.appointmentMasterObj.subDeptId = $scope.appointmentMasterObj.deptId+'_'+ $scope.appointmentMasterObj.subDeptId;
		}
		$scope.finalOtReservationList = [];
		angular.forEach($scope.otReservationList ,function(value,index){
			if(value.isSelected){
				$scope.finalOtReservationList.push(value);
			}
		});
		appointmentMasterObjCopy.otReservationList = $scope.finalOtReservationList;
		//return false;
		$rootScope.startLoader();
		BillingGenericService.serviceAction(METHOD_POST, URI,appointmentMasterObjCopy, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
		           $scope.patientExists=response.data.message;
					if (response.data.status == "success") {
						if($scope.encounterDetailsContractList != undefined && $scope.encounterDetailsContractList.length  > 0){
						$scope.count = 0;
						angular.forEach($scope.encounterDetailsContractList, function(value, key) 
								{  
							      value.patientId = response.data.object.patientId;
									var promise = $scope.uploadEncounterContractDoc(value);
									promise.then(function(response) 
											{
											  if(response.data.status == "success")
												  {
												    $scope.count++;
												    if($scope.encounterDetailsContractList.length==$scope.count)
													 {
													   $scope.encounterDetailsContractList = [];
													   
													 }
											      }
											  else 
												{
													growl.error('Something went wrong.',
															{
																title : 'Error!'
															});
												}
										    });
										
								});
					}
						
						//upload patient images
						 if($scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
							{
							  $scope.uploadPatientImage($scope.patientImage1,response.data.object.patientId,$scope.organizationId,$scope.unitId);
							}
//							if($scope.snapshotData!=undefined && $scope.snapshotData!="")
//							{
//							  var file =$scope.convertBase64ToImage($scope.snapshotDatamoment().toDate().getTime()+".jpeg");
//							  $scope.uploadPatientImage(file,response.data.object.patientId,$scope.organizationId,$scope.unitId);
//							}
                                     if($scope.snapshotData!=undefined && $scope.snapshotData!="")
                                        {
                                          var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getTime()+".jpeg");
                                          $scope.uploadPatientImage(file,response.data.object.patientId,$scope.organizationId,$scope.unitId);
                                        }


						
						// update abscond patient as normal patient start
						if($scope.abscondPatientFlag!=undefined  && $scope.abscondPatientFlag!="" && $scope.abscondPatientFlag!=null && $scope.abscondPatientFlag=='Y'){
						var URI = BASE_URL + ROOT_URL+ "/api/global/updatePatientAsAbscound";					
						var obj={
								patientId:response.data.object.patientId
						}		
						console.log('abscond obj',obj);
//						return false;
						GenericService.serviceAction("POST", URI, obj).then(
								function(response) {	
									if (response.data.status == "success") {
										$scope.abscondPatientFlag="";
									}
								});
						}
						if($scope.isTeleconsultationAppointment == 1){
							
							if($scope.patientDetails.videoCallUrl == null  || $scope.patientDetails.videoCallUrl == undefined ){
								$scope.date = moment(new Date()).format('DD-MM-YYYY');
								if($scope.patientObj.email != ''  && $scope.patientObj.email != null){
									$scope.email = $scope.patientObj.email;
								}
								if($scope.patientDetails.email != '' && $scope.patientDetails.email != null){
									$scope.email = $scope.patientDetails.email;
								}
							$scope.date = moment(new Date()).format('DD-MM-YYYY');
							var data = {
									"patientId" : response.data.object.patientId,
									"organizationId" :$scope.organizationId,
									"unitId" :$scope.unitId,
									"doctorId" :response.data.object.doctorId,
									"email" : $scope.email,
									"dateTime" : moment(new Date()).format('DD/MM/YYYY') +" "+$scope.fromTime
							};
										var URI = BASE_URL + ROOT_URL
										+ "/api/global/sendPatientSmsFromCoversheet";
										 GenericService.serviceAction("POST", URI, data).then(function(response) {
												if (response.data.status == "success") {
													growl.success("Message sent successfully !!",
														{
															title : 'Success!'
														});
												}
												var URL = response.data.object;

											});
							}

						}
						// update abscond patient as normal patient end
						// hide/ show apply button for corporate
						if($scope.settleBillFlag == "N"){
						var encounterObj={
								appointmentId : $scope.appointmentMasterObj.appointmentId,
								specialityId : $scope.appointmentMasterObj.deptId,
								patientId : response.data.object.patientId,
								encounterId :response.data.object.encounterId,
								visitTypeId:response.data.object.visitTypeId,
								doctorId:response.data.object.doctorId,
								doctorName:$scope.encounterDetailsObj.doctorName,
								specialityName: $scope.encounterDetailsObj.specialityName,
								paymentEntitlementTypeId:$scope.appointmentMasterObj.paymentEntitlementTypeId,
								referralId : $scope.appointmentMasterObj.referralId,
								patientCategoryId : $scope.appointmentMasterObj.patientCategoryId,
								referralDesc : $scope.referralDesc,
								counterId : $rootScope.counterId,
								countryCallingCode : $scope.countryCallingCode
								
						}
						console.log('response.data.object.encounterId',response.data.object.encounterId);
						
						if($scope.appointmentMasterObj.encounterTypeId==1 || $scope.appointmentMasterObj.encounterTypeId==3 || $scope.appointmentMasterObj.encounterTypeId==9)
							{
							     if($scope.appointmentMasterObj.contractId==null){
									var data =  {
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "OPDCaseSheet.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "OPDCoversheet",
							                 "reportParams": [
										                {
										                  "paramName": "@EncounterId",
										                  "paramType": "Integer",
										                  "paramValue":  response.data.object.encounterId
										                }
										                ]
									};
									
							     }else{
							    	 //casepaper for insurance patient
										var data =  {
								                 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "OPDCaseSheet_ForcompanyPatient.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "OPDCoversheet",
								                 "reportParams": [
											                {
											                  "paramName": "@EncounterID",
											                  "paramType": "Integer",
											                  "paramValue":  response.data.object.encounterId
											                }
											                ]
										};
										
							     }
									if($scope.appointmentMasterObj.encounterTypeId!=6 )
									{
									  reportService.generate(REPORT_API_URI1,data);
									}
							}
						    
					    
						 if($scope.isPreOrPostBilling == 'Y' && $scope.appointmentMasterObj.encounterTypeId!=6 )
							 { 	 	
								 $state.go("newOPDBilling",{
										newOpdBillObject :encounterObj
									});
							 }
					       
						$scope.patientAdmittedFlag = false;
						$scope.initPatientSearchObj();
						$scope.initAppointmentMasterObj();
						$scope.initKinDetails();
						$scope.serviceOrderList = [];
						$scope.totalServiceAmt = 0;
						$scope.patientList=[];
						$scope.encounterList=[];
						$scope.secondTabLevel = 1 ;
						
						$scope.patientObj = {

								prefixId : '',
								firstName : '',
								middleName : '',
								lastName : '',
								genderId : '',
								countryCallingCode : '',
								email : '',
								nationalityId : '',
								raceId : '',
								maritalStatusId : '',
								mobileNumber : '',
								dob : '',
								identificationTypeId : '',
								identificationNumber : '',
								isVip : '',
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								updatedBy : $scope.userId,
								createdBy : $scope.userId,
								status : 'A',
								patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
								aliseName:'',
								identificationExpiryDate:'',
								occupationId:'',
								registrationTypeId:1,
								previousId:'',
								patientId:0,
								patientDetailsId:'',
								whatsappCallingCode : '',
								whatsappNo:'',
								skypeId:'',
								address : '',
								countryId : '',
								stateId : '',
								districtId : '',
								cityId :'',
								areaId : '',
								zipCode : '',
								phoneCode : '',
								phoneNumber : '',
								permanentAddress:'',
								permanentCountryId:'',
								permanentStateId:'',
								permanentDistrictId:'',
								permanentCityId:'',
								permanentAreaId:'',
								permanentZipCode:'',
								permanentPhoneCode:'',
								permanentPhoneNumber:'',
								fileSequenceNumber:'',
								bloodGroup:'',
								isAlreadyExist:'N'
							};
						 $scope.patientAdmittedFlag = false;
							$scope.ageDay = 0;
							$scope.ageMonth = 0;
							$scope.ageYear = 0;
							
							if($scope.unitCountryId > 0)
							{
							  $scope.patientObj.countryId  = $scope.unitCountryId;
							  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
							  $scope.patientObj.stateId  =  $scope.unitStateId;
							  $scope.patientObj.districtId = $scope.unitDistrictId;
							  $scope.patientObj.cityId = $scope.unitCityId;
							  $scope.patientObj.areaId = $scope.unitAreaId;
							  $scope.patientObj.zipCode = $scope.unitZipCode;
							  $scope.getStateByCountry($scope.patientObj.countryId,'L');
							  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
							  $scope.getDistrictByState($scope.patientObj.stateId,'L');
							  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
							  $scope.getAreaByCity($scope.patientObj.cityId,'L');
							  $scope.selectFlag = false;
							}
							
						/*if($scope.appointmentMasterObj.encounterTypeId==7){
							growl.success("Encounter generated successfully",
									{
										title : 'Success!'
									});
						}*/
					}
					}else if(response.data.code == 501){
						//Added by kaustubh
								if($scope.appointmentMasterObj.appointmentId > 0)
									{
								// update status to pending 
									//alert('SET');
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/updateAppointmentStatus";
									
									var statusChangeObj={
											appointmentId : $scope.appointmentMasterObj.appointmentId,
											appointmentStatusId: 1, // If encounter fails by any case change the status to pending 
											updatedBy : $scope.userId,
											updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
									}		
									console.log('statusChangeObj',statusChangeObj);
									//return false;
							GenericService.serviceAction("POST", URI, statusChangeObj)
									.then(
											function(response) {
												$scope.reScheduleObjList = [];
		
												if (response.data.status == "success") {
													
													
		
												}
											});
							
							if($scope.appointmentMasterObj.encounterTypeId == 2){
							$scope.appointmentMasterObj.subDeptId =array[0]+'_'+array[1];
							}
								
							}
					}else if(response.data.code == 409)	{
							$scope.patientObj.isAlreadyExist ='Y';
							angular.element('#patientAlradyExistPopUp').modal('show'); 
					}
				
				});
		  }
		
		
		
	}
	
	$scope.getKinDetailsById = function(kinDetailsId){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success") {
						$scope.kinDetailsList[0]=(angular.copy(response.data.object));
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
				$scope.getKinDetailsByPatientId();
			}
		});
	}
	
	$scope.setKinId = function(kinId){
		$scope.appointmentMasterObj.kinDetailsId = kinId;
	}
	
	$scope.getDetailsEntitlement = function(type)
	{
		/*if(type == '2')
		{
			
		}*/
		if(type == '2')
			{
			
			var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:type};
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.companyMasterInsuranceList = response.data.listObject;
				}
			});
			}
		if(type == '3')
			{
			     var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:type};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterList = response.data.listObject;
							 console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
						}
					});
				  
			}
	}
	$scope.corporateCompanyId="";
	$scope.getContractByCompanyId = function(companyId)
	{
		$rootScope.startLoader();
		$scope.showNameDetails=false;	
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
		var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
		
		var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:1/*,billingBedCategoryId:$scope.opdBillingCatId*/};
	    
		 $scope.contractList = [];
		 $scope.paymentEntitleMentCorporate[0].contractId = '';
		  $scope.paymentEntitleMentCorporate[0].datepickerCons ='';
		  $scope.paymentEntitleMentCorporate[0].datepickerConE ='';
		  
		var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
	    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				 $scope.contractList = response.data.listObject;
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
			}
			$rootScope.stopLoader();
		});
	}
	
	$scope.getInsuranceContract = function(insuranceItem)
	{
		
		$rootScope.startLoader();
		var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
		var companyId = insuranceItem.companyId;
			var asscoiatedCompanyId = insuranceItem.associateCompanyId;
				
			
			var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,associateCompanyId:asscoiatedCompanyId,visitTypeId:1/*,billingBedCategoryId:$scope.opdBillingCatId*/};
			$scope.assoContractList = [];
			$scope.paymentEntitleMentInsurance[0].contractId = '';
			
			var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
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
				}
				$rootScope.stopLoader();
			});
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
		
	/*	var tarrifName = $.grep($scope.tariffList, function (doc) {
            return doc.tariffId == $scope.paymentEntitleMentCorporate[0].tariffId;
        })[0].tariffDesc;*/
		
		
		console.log("contractObj",contractObj);
		if(contractObj != undefined || contractObj != null)
		{
		 // var payEntList = angular.copy($scope.paymentEntitleMentList);
		  //$scope.paymentEntitleMentList = [];
		  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
		  {
		/*	  var sInsDt =  $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+" 00:00:00";
			  var eInsDt =  $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+" 00:00:00";
			  if(sInsDt > eInsDt)
				  {
					  growl.warning("Start date should be less then end date..!!		",{
							title : "WARNING"
						});
						return false;
				  }else{*/
			  
			  var prioObj = {
					  id:$scope.priorityList.length + 1,
					  value:$scope.priorityList.length + 1,
				  }
				  $scope.priorityList.push(prioObj);
		  var newArr = {};
		  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
		  newArr.paymentEntitlementId="3";
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
			
		  newArr.companyName = contractObj.assCompanyName;
		  newArr.companyId = contractObj.companyId;
		 // newArr.tariffName = tarrifName;
		  newArr.contractName = contractObj.contractName;
		  newArr.insuranceName = "";
		  newArr.insuranceNumber = "";
		 // newArr.startDate = moment($scope.paymentEntitleMentCorporate[0].datepickerCons).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
		  //newArr.endDate = moment($scope.paymentEntitleMentCorporate[0].datepickerConE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityTo;datepickerConE
		  newArr.startDate = $scope.paymentEntitleMentCorporate[0].datepickerCons != null ?moment($scope.paymentEntitleMentCorporate[0].datepickerCons,"DD/MM/YYYY").format($scope.dateFormat.toUpperCase()): moment(new Date()).format($scope.dateFormat.toUpperCase());//contractObj.validityFrom;
		  newArr.endDate = $scope.paymentEntitleMentCorporate[0].datepickerConE != null ?moment($scope.paymentEntitleMentCorporate[0].datepickerConE,"DD/MM/YYYY").format($scope.dateFormat.toUpperCase()): moment(new Date()).format($scope.dateFormat.toUpperCase());//contractObj.validityTo;
//		 
		  newArr.priority = "";
		  newArr.tariffId = $scope.paymentEntitleMentCorporate[0].tariffId;
		  newArr.contractId = contractObj.contractId;
		  newArr.insuredAmount = '';
		  newArr.typeId = 3;
		  newArr.claimNumber = $scope.paymentEntitleMentCorporate[0].claimNumber;
		  if($scope.corEmpVerificationFlag && $scope.corEmpObj.typeOfCoEmp==3){
			  newArr.corporateEmpNote=$scope.corEmpObj.note!=undefined || $scope.corEmpObj.note!="" || $scope.corEmpObj.note!=null ? $scope.corEmpObj.note : "";
		  }
		  
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
				
				if($scope.paymentEntitleMentList.length > 1)
				{
					$scope.billTabHide = true;
				}
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
				  }
			  
			  
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
				 
				 
				/* if($scope.paymentEntitleMentInsurance[0].insuranceNumber == null || $scope.paymentEntitleMentInsurance[0].insuranceNumber == '' || $scope.paymentEntitleMentInsurance[0].insuranceNumber == undefined){
					 growl.warning("Please enter insurance number.",{
							title : "WARNING"
						});
						return; 
				 }*/
				 
			/*	 if($scope.paymentEntitleMentInsurance[0].datepickerInss == null || $scope.paymentEntitleMentInsurance[0].datepickerInss == '' || $scope.paymentEntitleMentInsurance[0].datepickerInss == undefined){
					 growl.warning("Please select start date.",{
							title : "WARNING"
						});
						return; 
				 }
				 */
				 /*if($scope.paymentEntitleMentInsurance[0].datepickerInsE == null || $scope.paymentEntitleMentInsurance[0].datepickerInsE == '' || $scope.paymentEntitleMentInsurance[0].datepickerInsE == undefined){
					 growl.warning("Please select end date.",{
							title : "WARNING"
						});
						return; 
				 }*/
				 
				 /*if($scope.paymentEntitleMentInsurance[0].insuredAmt == null || $scope.paymentEntitleMentInsurance[0].insuredAmt == '' || $scope.paymentEntitleMentInsurance[0].insuredAmt == undefined){
					 growl.warning("Please enter amount.",{
							title : "WARNING"
						});
						return; 
				 }*/
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
		
		var associatedName = $.grep($scope.associateCompanyMasterList, function (doc) {
            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
        })[0].name;
		
		var insuranceId = $.grep($scope.associateCompanyMasterList, function (doc) 
				{
	            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
	        })[0].id;
		
		console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
		
		if(insuranceObj != undefined || insuranceObj != null)
		{
		  //var payEntList = angular.copy($scope.paymentEntitleMentList);
		 // $scope.paymentEntitleMentList = [];
		  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
		  {
			  /*if(($scope.paymentEntitleMentInsurance[0].datepickerInssc != null || $scope.paymentEntitleMentInsurance[0].datepickerInss != "") && ($scope.paymentEntitleMentInsurance[0].datepickerInsE == null || $scope.paymentEntitleMentInsurance[0].datepickerInsE == '')){
				  growl.warning("Please select end date.",{
						title : "WARNING"
					});
					return false;
			  }
			  
			  if(($scope.paymentEntitleMentInsurance[0].datepickerInssc == null || $scope.paymentEntitleMentInsurance[0].datepickerInss == "") && ($scope.paymentEntitleMentInsurance[0].datepickerInsE != null || $scope.paymentEntitleMentInsurance[0].datepickerInsE != '')){
				  growl.warning("Please select start date.",{
						title : "WARNING"
					});
					return false;
			  }*/
			  var isAfter = true;
			/*  if(($scope.paymentEntitleMentInsurance[0].datepickerInssc != null || $scope.paymentEntitleMentInsurance[0].datepickerInss != "") && ($scope.paymentEntitleMentInsurance[0].datepickerInsE != null || $scope.paymentEntitleMentInsurance[0].datepickerInsE != '')){
				  var sInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');
				  var eInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');
				  
				isAfter = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).isAfter($scope.paymentEntitleMentInsurance[0].datepickerInss);
			  }else{
				  isAfter = true;
				  
			  }*/
			 
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
		//  newArr.tariffName = tarrifName;
		  newArr.employeeNoManual = '';
		  newArr.contractName = insuranceObj.contractName;
		  newArr.insuranceName = associatedName;
		  newArr.insuranceId = insuranceId;
		  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
		  newArr.startDate = $scope.paymentEntitleMentInsurance[0].datepickerInss != null ?moment($scope.paymentEntitleMentInsurance[0].datepickerInss,"DD/MM/YYYY").format($scope.dateFormat.toUpperCase()): moment(new Date()).format($scope.dateFormat.toUpperCase());//contractObj.validityFrom;
		  newArr.endDate = $scope.paymentEntitleMentInsurance[0].datepickerInsE != null ? moment($scope.paymentEntitleMentInsurance[0].datepickerInsE,"DD/MM/YYYY").format($scope.dateFormat.toUpperCase()): moment(new Date()).format($scope.dateFormat.toUpperCase());//contractObj.validityFrom;datepickerInsE
		  newArr.priority = "";
		  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
		  newArr.contractId = insuranceObj.contractId;
		  newArr.typeId = 2;
		  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
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
				
				if($scope.paymentEntitleMentList.length > 1)
					{
						$scope.billTabHide = true;
					}
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
		var tmpprio;
		
		var le = $scope.paymentEntitleMentList.length;
		
		var tmpObj = angular.copy($scope.paymentEntitleMentList);
		var i = 1;
		if(le != i){
			
		angular.forEach(tmpObj, function(value,key) {
			
			if (index != i) {
				/*if(v.priority > 1){
				v.priority = v.priority - 1;
				}*/
				newDataList.push(value);
				
				if(le == 2)
					{
						if(value.paymentEntitlementId == "1")
							{
							value.priority = le - 1;
							}else
								{
								value.priority = 1;
								}
					}
				if(le > 2)
				{
					if(tmpprio != 3){
					if(value.priority != 1)
					{
					 //v.priority = "";
						value.priority =value.priority - 1;
					}
					}
					
					/*if(v.paymentEntitlementId == "1")
						{
							v.priority = le - 1;
						}else
							{
							v.priority = "";
							}*/
				}
			}else{
				tmpprio = value.priority;
				$scope.paymentEntitleMentList.splice(index - 1,1);
			}
			
			if(i == le)
				{
					if(newDataList.length == 1){
						

					     $scope.appointmentMasterObj.contractId = null;
					     $scope.appointmentMasterObj.companyId = null;
						$scope.getBillDetails();
					}
				}
			
			if($scope.corEmpVerificationFlag){
				$scope.enableDisabledValidated=false;
				$scope.enableDisabledApply=true;	
			}else{
				$scope.enableDisabledValidated=false;
				$scope.enableDisabledApply=true;
			}
			i++;
		});
		
		var len = $scope.priorityList.length;
		 $scope.priorityList.splice(len-1,1);
		 
		$scope.paymentEntitleMentList = newDataList;
		if($scope.paymentEntitleMentList.length == 1)
			{
			$scope.paymentEntitleMentList[0].priority = "1";
			}
		if($scope.paymentEntitleMentList.length == 1)
			{
			  $scope.billTabHide = false;
			}
		else
			{
			  $scope.billTabHide = true;
			}
		
		
		//$scope.paymentEntitleMentCorporate =[];
		//$scope.addContracts();
		}
		
		$scope.enableDisabledApply=true;	
		
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
	
	
	$scope.secondTabLevel = 1 ;
	$scope.setSecondTabLevel = function(newTab){
		$scope.secondTabLevel = newTab;
	};
	$scope.isSetSecondTabLevel = function(tabNum){
		return $scope.secondTabLevel === tabNum;
	};
	
	/*$scope.editInsurance = function(item,idx)
	{
		$scope.getDetailsEntitlement(item.paymentEntitlementId);
		if(item.paymentEntitlementId == 2)
			{
			 $scope.appointmentMasterObj.paymentEntitlementTypeId = "2";
			 $scope.insuApplyBtn = false;
			 $scope.insuUpdateBtn = true;
			 
			 $scope.paymentEntitleMentInsurance=
					[{
						'tariffId' : item.tariffId,
						'companyId':item.companyId,
						'associateCompanyId':item.associateCompanyId,
						'contractId':item.contractId,
						//'statrDate':"",
						//'endDate':"",
						'insuranceNumber':item.insuranceNumber,
						'insuredAmt':item.insuredAmount,
						'datepickerInsE':new Date(item.startDate),
						'datepickerInss':new Date(item.endDate)
					}];
			 $scope.getAssociatedCompanyMasterListByCompanyId(item.companyId);
			 $scope.getInsuranceContract($scope.paymentEntitleMentInsurance[0]);
			 
			}
		if(item.paymentEntitlementId == 3)
		{
		 $scope.appointmentMasterObj.paymentEntitlementTypeId = "3";
			$scope.corpApplyBtn = false;
			$scope.corpUpdateBtn = true;
		}
	}*/
	/*$scope.getReferralType = function(){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
		var obj={organizationId : $scope.organizationId};
		BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.referralTypeList = [];
			if (response.data.status == "success") {
				$scope.referralTypeList = response.data.listObject;
			}
		});
	}
	$scope.getReferralType();*/
	
	$scope.searchPackages = function(packageSearchKeyword){
		
		/*if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == 0){
			$scope.secondTabLevel = 1 ;
			growl.warning("Please Select Patient",{
				title : WARNING_MSG
			});
			return;
		}*/	
		if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined){
			
			$scope.secondTabLevel = 1 ;
			
			$scope.$broadcast('show-errors-check-validity');
			// $scope.secondTabLevel=1;
	        if($scope.patientRegistrationform.$valid){
	        	
	        	var contryCode=$scope.patientObj.countryCallingCode;
	        	 // validating country code
				 var regEx = /^[+-]?\d+$/;
				      if(!regEx.test(contryCode)){
				    	  growl.warning("Please enter valid country calling code",{
								title : 'Warning !'
								});
								return false;
				      } 
	        	
	        	var mobileNo=$scope.patientObj.mobileNumber;
	        	if(contryCode==91){
	        		if(mobileNo.length==10){
	        			
						}else{
							growl.warning("Please enter valid mobile number",{
								title :'Warning !'
								});
								$('#mobileNo').focus();
								return false;
	        		}
	        	}else{
		        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
		        			
						}else{
							growl.warning("Please enter valid mobile number",{
								title : 'Warning !'
								});
								$('#mobileNo').focus();
								return false;
	        		}
	        	}


	        	  	
	        	
	        	
	        	if($scope.patientObj.patientCategoryId==null || $scope.patientObj.patientCategoryId==undefined||$scope.patientObj.patientCategoryId==""){
	        		growl.warning("Please select patient category",{
								title : 'Warning !'
								});
								
								return false;
	        	}
				/*if($scope.patientRegistrationform2.$valid){*/
	        		 if($scope.patientObj.phoneCode !='' && $scope.patientObj.phoneCode!=undefined && $scope.patientObj.phoneCode!=null){
	        			 var phoneCode=$scope.patientObj.phoneCode;
			        	 // validating country code
						 var regEx = /^[+-]?\d+$/;
						      if(!regEx.test(phoneCode)){
						    	  growl.warning("Please enter valid country calling code",{
										title : 'Warning !'
										});
										return false;
						      } 
	        		 }
	        		 //test start
	        		 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
       			 }
	        		 if($scope.patientObj.permanentZipCode !='' && $scope.patientObj.permanentZipCode!=undefined && $scope.patientObj.permanentZipCode!=null){
       			 }
	        		 if($scope.patientObj.permanentPhoneCode !='' && $scope.patientObj.permanentPhoneCode!=undefined && $scope.patientObj.permanentPhoneCode!=null){
	        			 var permanentPhoneCode=$scope.patientObj.permanentPhoneCode;
			        	 // validating country code
						 var regEx = /^[+-]?\d+$/;
						      if(!regEx.test(permanentPhoneCode)){
						    	  growl.warning("Please enter valid country calling code",{
										title : 'Warning !'
										});
										return false;
						      } 
	        		 }
	        		 if($scope.patientObj.email == null){
	        			 $scope.patientObj.email = "";
	        		 }
	        		 if($scope.patientObj.email != "")
	           			{
		           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		           		 var email = $scope.patientObj.email;
		           		    if (!filter.test(email)) {

	        				    growl.error(EMAIL_MSG,{
									title : ERROR_MSG

								});
	        				    $('#email').focus();
	        				    return false;
		           		    }
		           		    else{
		           		    	if($scope.patientObj.phoneNumber == undefined)
		           		    			$scope.patientObj.phoneNumber='';
		           		    	if($scope.patientObj.phoneNumber == null)
		           		    			$scope.patientObj.phoneNumber='';
		           		     if($scope.patientObj.phoneNumber != '')
				        		{
				        		}
		           		     else
		           		    	 {
		           		    	 }
		           		    }
	           			}
	        		 else
	        			 {
	        			 if($scope.patientObj.phoneNumber == undefined)
	        				 $scope.patientObj.phoneNumber='';
	        			 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
	        			 }
	        			 }
	        		
	        		 //test end
	        			 
	        }else{
	        	growl.warning(" Please select patient or add patient details..!! ",{
					title : WARNING_MSG
				});
	        	return false;
	        }
	        
		}
		var packageSearchObj = {};
			packageSearchObj.serviceName = packageSearchKeyword;
			packageSearchObj.organisationId = $scope.organizationId;
			packageSearchObj.unitId = $scope.unitId;
			packageSearchObj.packageTypeId = 1;
			packageSearchObj.visitTypeId = 1;//default op
			packageSearchObj.bedBillingCategoryId = $scope.opdBillingCatId;//default op
			packageSearchObj.paymentEntitlementId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
			packageSearchObj.patientTypeId=$scope.patientObj.patientCategoryId;
			packageSearchObj.genderId = $scope.patientObj.genderId;
			return servicesFactory.searchPackageForBilling(packageSearchObj,$scope);
			/*var URI = BASE_URL + ROOT_URL + "/api/packages/bill/services/search";
			return BillingGenericService.serviceAction("POST", URI, packageSearchObj).then(function(response){
				if(response.data.status == "success")
					return $scope.searchPackageList = response.data.listObject;
			});*/
		
	}
	
	$scope.selectPackageService = function($item, $model, $label){
		
		if($scope.patientDetails.patientId > 0){
		var birthDateArr=$scope.patientDetails.birthDate.split("/");
		var age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);		
		var age=0;
		if(birthDateArr.length>1){
			 age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);
		}else{
			  birthDateArr=$scope.patientDetails.birthDate.split("-");
			 age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);
		}	
		
		if(!($item.minAge <= age && $item.maxAge >= age)){
			this.search='';
			growl.warning("Package is not yet  configure for this age.",{
				title : WARNING_MSG
			});
			return;
		}
		
		$scope.EHCpackageId = $item.packageMasterId;
		$scope.appointmentMasterObj.serviceId = $item.serviceMasterId;
		$scope.packageBasePrice =  $item.basePrice;
		}
		else{
			var birthDateArr=moment($scope.datepicker).format('MM/DD/YYYY');
			var age= $rootScope.getAge(birthDateArr);		
			
			if(!($item.minAge <= age && $item.maxAge >= age)){
				this.search='';
				growl.warning("Package is not yet  configure for this age.",{
					title : WARNING_MSG
				});
				return;
			}
			
			$scope.EHCpackageId = $item.packageMasterId;
			$scope.appointmentMasterObj.serviceId = $item.serviceMasterId;
			$scope.packageBasePrice =  $item.basePrice;
		}
	}
	
	//======================= Payment Entitlement ==========================//
	$scope.getDocumentType = function(){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.documentTypeList = [];
				if (response.data.status == "success") {
					$scope.documentTypeList = response.data.listObject;
					console.log("document type list=",$scope.documentTypeList);
				}
			});
	}
	$scope.getDocumentType();
	
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
	
	$scope.getAssociatedCompanyMasterListByCompanyId = function(companyId){
		$rootScope.startLoader();
		 var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
		 $scope.associateCompanyMasterList = [];
		 $scope.paymentEntitleMentInsurance[0].associateCompanyId = '';
		 
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.associateCompanyMasterList = response.data.listObject;
					  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
				}
				$rootScope.stopLoader();
			});
		    
	}
	
	//-------------- function to check create Encounter based on appointment -------------//
	
	/*$scope.getFutureOrders = function()
	{
		//SNEHA
		var data1={
				patientId:$scope.encounterDetailsObj.patientId,
				organisationId:$scope.organizationId,
				unitId : $scope.unitId,
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientServicesForFutureDate";
		BillingGenericService .serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							$scope.futureServiceList = [];
							// $rootScope.startLoader();
							if (response.data.status == "success") {
								angular.forEach(response.data.listObject,function(value,index){
									value.checkOption='N';
								});
								$scope.futureServiceList = response.data.listObject;
								console.log("Future service List",$scope.futureServiceList);
							}

						});
	}*/
	
	$scope.checkAppointment=function(){
		var appointmentObj = null;
		
		 //$scope.oldSlotId = null;
		 
		if($scope.alreadyPatient == 1)
			{
			$scope.encounterDetailsObj=$scope.alreadyAppointment;
			appointmentObj=$scope.alreadyAppointment;
			
			}
		else
			{
			
			 console.log("stateObj", $stateParams.appointmentObj);
			 appointmentObj=$stateParams.appointmentObj;
			 console.log(appointmentObj);
			 $scope.encounterDetailsObj=appointmentObj; 
			 $scope.patientDetails.videoCallUrl = $scope.encounterDetailsObj.videoCallUrl;
			 if($scope.encounterDetailsObj.fromNewRegistration > 0)
				 {
				    $scope.isRedirectAppointment=false;
				 }else{
					 
					 $scope.isRedirectAppointment=true;
				 }
			 if($scope.encounterDetailsObj.fromNewRegistration == undefined){
				 $scope.isRedirectAppointment=false;
			 }
			 $scope.isReviFlg = true;
			}
		
		
					if(appointmentObj.patientId != undefined && appointmentObj.appointmentTypeId != 6)
					{
						var URI = BASE_URL + ROOT_URL + "/api/opd/lastSponsors/"+appointmentObj.patientId+"/1/"+$scope.unitId;
						BillingGenericService
								.serviceAction(METHOD_GET, URI, {},
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											if (response.data.status == "success") 
											{
												$scope.forCheckingPatientClassAndEntitlement = response.data.listObject;
												if( response.data.object > 0)
												{
													$scope.appointmentMasterObj.patientClassId = response.data.object;
												}
												if($scope.forCheckingPatientClassAndEntitlement!=null && $scope.forCheckingPatientClassAndEntitlement.length > 0)
												{
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
													  		  newArr.claimNumber = value.claimNumber;
														  		newArr.patientSponsorDetailsId = value.patientSponsorDetailsId;
//													  		      newArr.startDate = value.validityFrom;
													  		    newArr.startDate=(value.validityFrom!= null ? moment(value.validityFrom,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()):moment(new Date()).format($scope.dateFormat.toUpperCase()));
//													  		      newArr.endDate = value.validityTo;
													  		  newArr.endDate=(value.validityTo!= null ? moment(value.validityTo,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()):moment(new Date()).format($scope.dateFormat.toUpperCase()));
													  		      newArr.priority = value.priorityId;
													  		      newArr.companyId = value.companyId;
													  		      newArr.insuranceId = value.insuranceId;
													  		      newArr.contractId = value.contractId;
													  		      newArr.insuredAmount = value.insuredAmt;
													  		if(value.paymentEntitlementId ==1){
													  			  newArr.startDate = null;
													  			  newArr.endDate = null;
													  			  newArr.paymentEntitlementName = "Self";
													  			  newArr.companyName = "Self";
													  			  newArr.tariffName = "Self Tariff";
													  			  newArr.tariffId = $scope.defaultSelfTariffId;
													  			  newArr.companyId = $scope.selfPayeeIdCookies;
													  		}else{
													  			  newArr.paymentEntitlementName = value.paymentEntitlementDesc;
													  			  newArr.companyName = value.companyDesc;
													  			  newArr.tariffName = value.tariffDesc;
													  			  newArr.tariffId = value.tariffId;
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
		
		
				$scope.paymentEntitlementShowFlag = (appointmentObj.appointmentTypeId==6)?false:true;
				if(appointmentObj.patientId != undefined && appointmentObj.appointmentTypeId == 1)
				{
					$rootScope.startLoader();
					 $scope.appointmentMasterObj.patientId=appointmentObj.patientId;
					//start1
					 $scope.paymentEntitlementShowFlag = (appointmentObj.appointmentTypeId==6)?false:true;
					if(appointmentObj.slotId > 0) //For New Appointment changes
					{
						     var  testData = {
						    	
						    		 unitId          :$scope.unitId,
									 organizationId  :$scope.organizationId,
						    		 patientId : appointmentObj.patientId ,
						    		 doctorId : appointmentObj.doctorId ,
						    		 specialityId : appointmentObj.specialityId,
						    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
		 				    		 
						     };
						     var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
						     BillingGenericService.serviceAction(METHOD_POST,URI, testData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						     
						   
						    $scope.appointmentMasterObj.systemGeneratedVisitTypeId=response.data.toString();
						    $scope.appointmentMasterObj.userDefinedVisitTypeId=response.data.toString();
						   // $scope.getBillDetails();
						     });
						     
						     $scope.getClinicByDoctorId(appointmentObj.doctorId,2,2);
						     
						     $scope.slotId = appointmentObj.slotId;
					}
				     //end1
				     
					if(appointmentObj.appointmentTypeId == 2 && appointmentObj.slotId > 0)
						{
							$scope.enableDisabled  = true;
							$scope.appointmentMasterObj.modalityId=appointmentObj.modalityId;
						}
					
					var URI = BASE_URL + ROOT_URL + "/api/global/patient/"+appointmentObj.patientId;
					BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						$scope.patientDetails.patientId = response.data.object.patientId,
						$scope.patientDetails.patientName = response.data.object.patientName,
						$scope.patientDetails.genderCode = response.data.object.genderCode,
						$scope.patientDetails.age = response.data.object.age, 
						$scope.patientDetails.uhIdNumber = response.data.object.uhidnumber,
						$scope.patientDetails.birthDate = response.data.object.dob,
						$scope.patientObj.email = response.data.object.email,
//						$scope.patientDetails.videoCallUrl = response.data.object.videoCallUrl,
						$scope.patientDetails.registrationTypeId = response.data.object.registrationTypeId,
						$scope.appointmentMasterObj.patientId=$scope.patientDetails.patientId;
						$scope.appointmentMasterObj.patientCategoryId = response.data.object.patientCategoryId;
						$scope.appointmentMasterObj.paymentEntitlementTypeId = 1;//selt
						//$scope.patientDetails.uhIdNumber=appointmentObj.uhidNumber;
						
						$scope.registrationTypeId = response.data.object.registrationTypeId;
						
						
						if(appointmentObj.slotId > 0) //For New Appointment changes
							{
								$scope.patientDetails.doctorName=appointmentObj.modalityDesc;
								$scope.appointmentMasterObj.specialityId = appointmentObj.specialityId;
								if(appointmentObj.appointmentTypeId == 1)
									{
									$scope.appointmentMasterObj.deptId=appointmentObj.specialityId;
									$scope.getDoctorBySpeciality($scope.appointmentMasterObj.deptId,3);
									$scope.appointmentMasterObj.encounterTypeId = "1";
									}
								else
									{
									$scope.appointmentMasterObj.deptId=appointmentObj.modalitySpecialityId;
									}
								
								$scope.appointmentMasterObj.doctorId=appointmentObj.doctorId;
								$scope.appointmentMasterObj.encounterTime=appointmentObj.fromTimeString;
								$scope.appointmentMasterObj.appointmentId=appointmentObj.appointmentId;
								$scope.appointmentMasterObj.isScheduledAppointment='A';
								
								
							}
						else
							{
								
								$scope.appointmentMasterObj.appointmentId=appointmentObj.appointmentId;
							    $scope.appointmentMasterObj.encounterTypeId = "1";
							   	$scope.selfFlag = "Y";
							}
						
						$scope.getKinDetailsByPatientId();
						$scope.kinDetailsList[0].patientId=appointmentObj.patientId;
						
						
					});
					//$scope.getFutureOrders();
				}
				if(appointmentObj.patientId != undefined && appointmentObj.appointmentTypeId == 2)
				{
				//	alert('ffg');
					 $scope.appointmentMasterObj.patientId=appointmentObj.patientId;
					 $scope.appointmentMasterObj.systemGeneratedVisitTypeId="4";
					 $scope.appointmentMasterObj.userDefinedVisitTypeId="4";
					 $scope.enableDisabled  = true;
					 $scope.hideShowByType = false;
					 /*$scope.appointmentMasterObj.modalityId=appointmentObj.modalityId;
					 $scope.appointmentMasterObj.deptId=appointmentObj.modalitySpecialityId;
					 kencounterTime=appointmentObj.fromTimeString;
					 $scope.appointmentMasterObj.appointmentId=appointmentObj.appointmentId;
					 $scope.appointmentMasterObj.isScheduledAppointment='A';*/
					 $scope.appointmentMasterObj.encounterTypeId = "2";
					 
					 var URI = BASE_URL + ROOT_URL + "/api/global/patient/"+appointmentObj.patientId;
						BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							$scope.patientDetails.patientId = response.data.object.patientId,
							$scope.patientDetails.patientName = response.data.object.patientName,
							$scope.patientDetails.genderCode = response.data.object.genderCode,
							$scope.patientDetails.age = response.data.object.age, 
							$scope.patientDetails.uhIdNumber = response.data.object.UHIDNumber,
							$scope.patientDetails.birthDate = response.data.object.dob,
							$scope.appointmentMasterObj.patientId=$scope.patientDetails.patientId;
							$scope.appointmentMasterObj.patientCategoryId = appointmentObj.patientCategoryId;
							$scope.patientDetails.doctorName=appointmentObj.doctorName;
							$scope.patientDetails.uhIdNumber=appointmentObj.uhidNumber;
							
							$scope.registrationTypeId = response.data.object.registrationTypeId;

							$scope.appointmentMasterObj.specialityId = appointmentObj.modalitySpecialityId;
							
							$scope.appointmentMasterObj.deptId=appointmentObj.modalitySpecialityId;
							$scope.getSubSpecialityBySpeciality(appointmentObj.modalitySpecialityId);
							$scope.appointmentMasterObj.subDeptId = appointmentObj.modalitySpecialityId +'_'+appointmentObj.modalitySubSpecialityId;
							//alert($scope.appointmentMasterObj.subDeptId);
							$scope.getModalityBySubSpeciality($scope.appointmentMasterObj.subDeptId);
							$scope.appointmentMasterObj.modalityId = appointmentObj.modalityId;
							//alert("$scope.appointmentMasterObj.modalityId"+$scope.appointmentMasterObj.modalityId);
							
							//$scope.appointmentMasterObj.deptId=appointmentObj.modalitySpecialityId;
							
							$scope.getKinDetailsByPatientId();
							$scope.kinDetailsList[0].patientId=appointmentObj.patientId;
							$scope.appointmentMasterObj.doctorId=appointmentObj.doctorId;
							$scope.appointmentMasterObj.encounterTime=appointmentObj.fromTimeString;
							$scope.appointmentMasterObj.appointmentId=appointmentObj.appointmentId;
							$scope.appointmentMasterObj.isScheduledAppointment='A';
							
						});
						 $scope.slotId = appointmentObj.slotId;
						//$scope.getFutureOrders();
							
				}
				if(appointmentObj.patientId != undefined && appointmentObj.appointmentTypeId == 3)
				{
					
					 $rootScope.startLoader();
					 $scope.appointmentMasterObj.patientId=appointmentObj.patientId;
					 $scope.appointmentMasterObj.paymentEntitlementId="1";
					 $scope.appointmentMasterObj.encounterTypeId = '3';
					 $scope.appointmentMasterObj.systemGeneratedVisitTypeId="5";
					 $scope.appointmentMasterObj.userDefinedVisitTypeId="5";
					 $scope.hideShowByTypeForEHC = false;
					 $scope.hideShowByTypeForPackage = true;
					 this.hideShowByTypeForPackage = true;					
					 $scope.hideShowByType = false;
					 var URI = BASE_URL + ROOT_URL + "/api/global/patient/"+appointmentObj.patientId;
						BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							$scope.patientDetails.patientId = response.data.object.patientId,
							$scope.patientDetails.patientName = response.data.object.patientName,
							$scope.patientDetails.genderCode = response.data.object.genderCode,
							$scope.patientDetails.age = response.data.object.age, 
							$scope.patientDetails.uhIdNumber = response.data.object.UHIDNumber,
							$scope.patientDetails.birthDate = response.data.object.dob,
							$scope.appointmentMasterObj.patientId=$scope.patientDetails.patientId;
							$scope.appointmentMasterObj.patientCategoryId = appointmentObj.patientCategoryId;
							//$scope.patientDetails.doctorName=appointmentObj.doctorName;
							$scope.registrationTypeId = response.data.object.registrationTypeId;
							$scope.patientDetails.uhIdNumber=appointmentObj.uhidNumber;
							$scope.getKinDetailsByPatientId();
							$scope.kinDetailsList[0].patientId=appointmentObj.patientId;
							$scope.appointmentMasterObj.appointmentId=appointmentObj.appointmentId;
							 $scope.slotId = appointmentObj.slotId;
							 if(appointmentObj.serviceId!=undefined && appointmentObj.serviceId>0 ){
								 var data={
										 patientTypeId:response.data.object.patientCategoryId,
										 serviceId:appointmentObj.serviceId,
										 visitTypeId : 1,//default op
										 billingBedCategoryId : $scope.opdBillingCatId,//default op
									     paymentEntitlementId : $scope.appointmentMasterObj.paymentEntitlementTypeId,
									     unitId          :$scope.unitId,
									     orgId  :$scope.organizationId,
								 }
								 var URI = BASE_URL + ROOT_URL + "/api/packages/getPackageRate";
									BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if (response.data.status == "success") {
											$scope.packageResponse=response.data.listObject[0];
											$scope.EHCpackageId = $scope.packageResponse.packageMasterId;
											$scope.appointmentMasterObj.serviceId = $scope.packageResponse.serviceMasterId;
											$scope.packageBasePrice =  $scope.packageResponse.packagePrice;
											$scope.search=$scope.packageResponse.serviceStandardName;
											console.log("pkg response",$scope.packageResponse);
										}
									});
							 }
							
							 
							//$scope.appointmentMasterObj.isScheduledAppointment='A';
							
							/*var data ={
									orgId:$scope.organizationId,
									unitId : $scope.unitId,
									age : 32,
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

											});*/
							
							
							
							
						});
						
						//$scope.getFutureOrders();
						BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
							{
							if(response.data.status=="success")
								{
									$scope.patientDetails.patientId = response.data.object.patientId,
									$scope.patientDetails.patientName = response.data.object.patientName,
									$scope.patientDetails.genderCode = response.data.object.genderCode,
									$scope.patientDetails.age = response.data.object.age, 
									$scope.patientDetails.uhIdNumber = response.data.object.UHIDNumber,
									$scope.patientDetails.birthDate = response.data.object.dob,
									$scope.appointmentMasterObj.patientId=$scope.patientDetails.patientId;
									$scope.appointmentMasterObj.patientCategoryId = appointmentObj.patientCategoryId;
									$scope.registrationTypeId = response.data.object.registrationTypeId;
									$scope.patientDetails.uhIdNumber=appointmentObj.uhidNumber;
									$scope.getKinDetailsByPatientId();
									$scope.kinDetailsList[0].patientId=appointmentObj.patientId;
									$scope.appointmentMasterObj.appointmentId=appointmentObj.appointmentId;
									$scope.slotId=appointmentObj.slotId;
									$scope.enableDisableDoctor($scope.appointmentMasterObj.encounterTypeId);
									$rootScope.stopLoader();
								}
							else if(response.data.status=="error")
								{
								  $rootScope.stopLoader();
								}
						   });
						//$scope.getFutureOrders();
					 
				}
				
				//for Hemodialysis
				if(appointmentObj.patientId != undefined && appointmentObj.appointmentTypeId ==8)
				{

					     $rootScope.startLoader();
					     $scope.appointmentMasterObj.patientId=appointmentObj.patientId;
						 $scope.appointmentMasterObj.systemGeneratedVisitTypeId="8";
						 $scope.appointmentMasterObj.userDefinedVisitTypeId="8";
						 $scope.enableDisabled  = true;
						 $scope.hideShowByType = false;
						 $scope.appointmentMasterObj.encounterTypeId ="7";
						 
						 var URI = BASE_URL + ROOT_URL + "/api/global/patient/"+appointmentObj.patientId;
							BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								$scope.patientDetails.patientId = response.data.object.patientId,
								$scope.patientDetails.patientName = response.data.object.patientName,
								$scope.patientDetails.genderCode = response.data.object.genderCode,
								$scope.patientDetails.age = response.data.object.age, 
								$scope.patientDetails.uhIdNumber = response.data.object.UHIDNumber,
								$scope.patientDetails.birthDate = response.data.object.dob,
								$scope.appointmentMasterObj.patientId=$scope.patientDetails.patientId;
								$scope.appointmentMasterObj.patientCategoryId = appointmentObj.patientCategoryId;
								$scope.patientDetails.uhIdNumber=appointmentObj.uhidNumber;
								
								$scope.registrationTypeId = response.data.object.registrationTypeId;

								$scope.getKinDetailsByPatientId();
								$scope.kinDetailsList[0].patientId=appointmentObj.patientId;
								//$scope.appointmentMasterObj.encounterTime=appointmentObj.fromTimeString;
								$scope.appointmentMasterObj.appointmentId=appointmentObj.appointmentId;
								$scope.appointmentMasterObj.isScheduledAppointment='A';
								$scope.enableDisableDoctor($scope.appointmentMasterObj.encounterTypeId);
								$rootScope.stopLoader();
								
							});
							 $scope.slotId = appointmentObj.slotId;
							//$scope.getFutureOrders();
				}
				
				
				
				
				if(appointmentObj.slotId != null && appointmentObj.slotId > 0)
				{
					 $scope.appointmentMasterObj.oldSlotId = appointmentObj.slotId; 
					 $scope.appointmentMasterObj.slotId = appointmentObj.slotId;
				}
				
				
			
				
				
	}
		
	$scope.checkAppointment();
	
	$scope.showPatientSearch = function(){
		$scope.patientSerachHideAndShow = true;
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
      
      if($scope.patientDetails.patientId > 0){

    	  $rootScope.startLoader();
       var data =
        {
        "patientId":$scope.patientDetails.patientId,
        "unitId":$scope.unitId,
        "organisationId":$scope.organizationId,
        "userId":$scope.userId,
        "visitTypeId":1,
        "admissionId":0,
       }
      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
        $scope.patientDocumentList = [];
        if (response.data.status == "success") 
        {
//        	$scope.patientDocumentList = response.data.listObject;  
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
					  visitTypeId:1,
					  patientId : "",
					  documentPath : "",
					  documentTypeId : '',
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
					/*growl.warning(
							'Please select atleast one patient!!!.',
							{
								title : 'warning!'
							});
						$scope.encounterDetailsContractList =[];
						return;*/
					
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					/*growl.warning("Please Select Patient",{
						title : WARNING_MSG
					});
					return;*/
					
					$scope.$broadcast('show-errors-check-validity');
					// $scope.secondTabLevel=1;
			        if($scope.patientRegistrationform.$valid){
			        	//alert("valid");
			        	
			        	var contryCode=$scope.patientObj.countryCallingCode;
			        	 // validating country code
						 var regEx = /^[+-]?\d+$/;
						      if(!regEx.test(contryCode)){
						    	  growl.warning("Please enter valid country calling code",{
										title : 'Warning !'
										});
										return false;
						      } 
			        	
			        	var mobileNo=$scope.patientObj.mobileNumber;
			        	if(contryCode==91){
			        		if(mobileNo.length==10){
			        			
								}else{
									growl.warning("Please enter valid mobile number",{
										title :'Warning !'
										});
										$('#mobileNo').focus();
										return false;
			        		}
			        	}else{
				        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
				        			
								}else{
									growl.warning("Please enter valid mobile number",{
										title : 'Warning !'
										});
										$('#mobileNo').focus();
										return false;
			        		}
			        	}


			        	if($scope.patientObj.patientCategoryId==null || $scope.patientObj.patientCategoryId==undefined||$scope.patientObj.patientCategoryId==""){
			        		growl.warning("Please select patient category",{
										title : 'Warning !'
										});
										
										return false;
			        	}
						/*if($scope.patientRegistrationform2.$valid){*/
			        		 if($scope.patientObj.phoneCode !='' && $scope.patientObj.phoneCode!=undefined && $scope.patientObj.phoneCode!=null){
			        			 var phoneCode=$scope.patientObj.phoneCode;
					        	 // validating country code
								 var regEx = /^[+-]?\d+$/;
								      if(!regEx.test(phoneCode)){
								    	  growl.warning("Please enter valid country calling code",{
												title : 'Warning !'
												});
												return false;
								      } 
			        		 }
			        		 //test start
			        		 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
		       			 }
			        		 if($scope.patientObj.permanentZipCode !='' && $scope.patientObj.permanentZipCode!=undefined && $scope.patientObj.permanentZipCode!=null){
		       			 }
			        		 if($scope.patientObj.permanentPhoneCode !='' && $scope.patientObj.permanentPhoneCode!=undefined && $scope.patientObj.permanentPhoneCode!=null){
			        			 var permanentPhoneCode=$scope.patientObj.permanentPhoneCode;
					        	 // validating country code
								 var regEx = /^[+-]?\d+$/;
								      if(!regEx.test(permanentPhoneCode)){
								    	  growl.warning("Please enter valid country calling code",{
												title : 'Warning !'
												});
												return false;
								      } 
			        		 }
			        		 if($scope.patientObj.email == null){
			        			 $scope.patientObj.email = "";
			        		 }
			        		 if($scope.patientObj.email != "")
			           			{
				           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				           		 var email = $scope.patientObj.email;
				           		    if (!filter.test(email)) {

			        				    growl.error(EMAIL_MSG,{
											title : ERROR_MSG

										});
			        				    $('#email').focus();
			        				    return false;
				           		    }
				           		    else{
				           		    	if($scope.patientObj.phoneNumber == undefined)
				           		    			$scope.patientObj.phoneNumber='';
				           		    	if($scope.patientObj.phoneNumber == null)
				           		    			$scope.patientObj.phoneNumber='';
				           		     if($scope.patientObj.phoneNumber != '')
						        		{
						        		}
				           		     else
				           		    	 {
				           		    	 }
				           		    }
			           			}
			        		 else
			        			 {
			        			 if($scope.patientObj.phoneNumber == undefined)
			        				 $scope.patientObj.phoneNumber='';
			        			 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
			        			 }
			        			 }
			        		
			        		 //test end
			        			 
			        }else{
			        	growl.warning(" Please select patient or add patient details..!! ",{
							title : WARNING_MSG
						});
			        	return false;
			        }
			        
				}
			
			/*	$scope.encounterDetailsContractList =[];
				var encounterDetails = angular.copy($scope.encounterDetailsContract);
				encounterDetails.patientId = $scope.patientDetails.patientId;	
				$scope.encounterDetailsContractList.push(encounterDetails);
				 $scope.getPatientDocumentList();*/
				 
				 
				 
				 if(obj.paymentEntitlementId==1){			
						
					 $scope.encounterDetailsContractList =[];
						var encounterDetails = angular.copy($scope.encounterDetailsContract);
						encounterDetails.patientId = $scope.patientDetails.patientId;
						encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
						$scope.encounterDetailsContractList.push(encounterDetails);	
						$scope.getPatientDocumentList(encounterDetails);
						$('#documentUploadModalForEncounter').modal('show');
					}else if(obj.paymentEntitlementId==2){
						
						$scope.encounterDetailsContractList =[];
						var encounterDetails = angular.copy($scope.encounterDetailsContract);
						encounterDetails.documentTypeId = '30';	
						encounterDetails.patientId = $scope.patientDetails.patientId;
						encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
						$scope.encounterDetailsContractList.push(encounterDetails);
						 $scope.getPatientDocumentList(encounterDetails);
						 $('#documentUploadModalForEncounter').modal('show');
						
					}else if(obj.paymentEntitlementId==3){
						$scope.encounterDetailsContractList =[];
						var encounterDetails = angular.copy($scope.encounterDetailsContract);
						encounterDetails.documentTypeId = '19';	
						encounterDetails.patientId = $scope.patientDetails.patientId;	
						encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
						$scope.encounterDetailsContractList.push(encounterDetails);
						 $scope.getPatientDocumentList(encounterDetails);		
						$('#documentUploadModalForEncounter').modal('show');
					}else{
						
					}
			
		}
	  
	  
	  $scope.rePrintOPDBillReceipt=function(index){

		  var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/1/"+$scope.userId+"/"+ $scope.paymentReceiptsDetailsList[index].receiptId;
			BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			{
				$rootScope.stopLoader();
				if (response.data.status == "success")
				{
					var data =  {
							  
			                 "appName": "HIS",
			                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			                 "reportDesignFilePath": "OPDPaymentReceipt.rpt",
			                 "reportFormat": "PDF",
			                 "exportFileName": "PaymentReciept",
			                 "reportParams": [
			                	 {
					                  "paramName": "@BillID",
					                  "paramType": "Integer",
					                  "paramValue": $scope.paymentReceiptsDetailsList[index].billingMasterId
				                },
				                {
					                  "paramName": "@PaymentID",
					                  "paramType": "Integer",
					                  "paramValue": $scope.paymentReceiptsDetailsList[index].receiptId
				                },
				                {
					                  "paramName": "@dep_consumption_id",
					                  "paramType": "Integer",
					                  "paramValue": ($scope.paymentReceiptsDetailsList[index].depositConsumptionId!=null ? $scope.paymentReceiptsDetailsList[index].depositConsumptionId : 0 )
				                }
	                 ]
					};
					 reportService.generate(REPORT_API_URI1,data);
				}
			});
		  
					  
		 
		   
		}
	  
	  $scope.uploadEncounterContractDoc = function(Obj)
		{
			 var fd = new FormData();
		        fd.append('encounterId', Obj.admissionId);
		        fd.append('visitTypeId', Obj.visitTypeId);
		        fd.append('admissionId', Obj.admissionId);
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
		 $scope.uploadContractDoc = function()
		    {
			  $scope.countFrValidation = 0;
			 angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
				  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
						 growl.warning("Please select document type .",
													{
														title : 'warning!'
													});
										   $rootScope.stopLoader();
						return false;
					}
//				  if (value.file == undefined || value.file == "" || value.file == null ) {
				  if (angular.equals(value.file, {}) || value.file==undefined || value.file==null) {
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
						$scope.countFrValidation++;
			});
			 if($scope.encounterDetailsContractList.length!=$scope.countFrValidation){
				 return false;
			 }
			 $rootScope.startLoader();
			 if($scope.appointmentMasterObj.patientId > 0){
			  $scope.count = 0;
			 angular.forEach($scope.encounterDetailsContractList, function(value, key) 
				{  
				 $('#documentUploadModalForEncounter').modal('hide');
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
		    }else{
		    	 $('#documentUploadModalForEncounter').modal('hide');
		    	 $rootScope.stopLoader();
		    }
			
		    }

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
         }
			//document upload functionality for payment entitle ment end
			
			
			/* *************************************Mpesa Integration start****************************************** */
			$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
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
						uhid: $scope.patientDetails.uhIdNumber,
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
					
				});
				}else{
					$scope.skipMpesaValidation = false;
					$rootScope.stopLoaderPopup();
					growl.error("Enter both transaction id and mobile number",{
						title : ERROR_MSG
					});
				}
			}
			
			$scope.checkoutRequestId= null;
			$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
				
				
				if(validateMpesaAmount == undefined || validateMpesaAmount == null){
					
					growl.warning("Please Enter Amount",{
						title : "warning!!!"
					});
					
				}else if(validateMpesaAmount > $scope.payeeDueAmtFinal){
					
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
							phoneNumber:parseInt($scope.countryCallingCode+""+mobileNumber),
							partyA:mobileNumber,
							amount:validateMpesaAmount
					};
				
					if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
							($scope.countryCallingCode != undefined && $scope.countryCallingCode != null && $scope.countryCallingCode != "")){
					$rootScope.startLoaderPopup();
					var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
					BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						
						if (response.data.status == "success")
							{
							$scope.checkoutRequestId = response.data.object.checkoutRequestId;
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
						uhidNumber: $scope.patientDetails.uhIdNumber,
						checkoutRequestId:$scope.checkoutRequestId
						
				};
				$scope.skipMpesaValidation = true;
				$rootScope.startLoaderPopup();
				var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
				BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					
					if (response.data.status == "success")
						{
						$scope.mpesaObject.mpesaAmount = response.data.object.amount;
						$scope.mpesaObject.transactionId = response.data.object.transactionId;
						$scope.mpesaObject.resTransactionId = response.data.object.transactionId;
						$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
					
						growl.success(response.data.message,{
							title : SUCCESS_MSG
						});
						if($scope.mpesaObject.transactionId != null ){
							$scope.hitMpesaApi = false;
							$scope.addPaymentDetails();
							$scope.skipMpesaValidation = false;
						}
						$rootScope.stopLoaderPopup();
						}
					else if(response.data.status == "error"){
						
						growl.error(response.data.message != null ? response.data.message :"Please wait",{
							title : ERROR_MSG
						});
						$rootScope.stopLoaderPopup();
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
			/* *************************************Mpesa Integration END****************************************** */
			
			
			 $scope.openInsuranaceImage = function(documentPath)
             { 
//				$rootScope.startLoader();
				if(documentPath!==""){
					var imageUrl = documentPath;
					var href = $rootScope.currentHrefDoccument();
					$.fancybox.open({
						width: 600, // or whatever
						height: 320,
						href:href+imageUrl,
						type: 'iframe'
					});
				}else{
					growl.error("No image found",
							{
								title : 'Error!'
							});
				}
				
//        $rootScope.stopLoader();
       }
			 // code start here for corporate employee verification
			 
			$scope.empNoShowHide=true; // for empNo text field hide show
			$scope.noteShowHide=false; // for note text area hide show
			$scope.dependTblShowHode=false; // for table hide show
			$scope.showHidevalidate=true; //for validate buttton 	
			$scope.showHideSaveBtn=false; // for mannual co-emp verification
			$scope.showHideAcceptRejectBtn=false;
			$scope.showNameDetails=false;
			$scope.showHideOkBtn=false;
			$scope.getTypeOfCoEmp=function(id){
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
				  }else if(id=="2"){
					  $scope.corEmpObj.typeOfCoEmp="2";
					  $scope.dependTblShowHode=true;
					  $scope.showHidevalidate=true;
					  $scope.noteShowHide=false;
					  
					  $scope.showHideSaveBtn=false; 
					  $scope.showNameDetails=false;
					  $scope.showHideOkBtn=false;
				  }else if(id=="3"){
					  $scope.corEmpObj.typeOfCoEmp="3";
					  $scope.dependTblShowHode=false;
					  $scope.noteShowHide=true; 
					  $scope.showHidevalidate=false; 
					  $scope.showHideSaveBtn=true; 
					  $scope.showNameDetails=false;
					  $scope.showHideAcceptRejectBtn=false;
					  $scope.showHideOkBtn=false;
				  }else{
					  
				  }
	  }
			
			$scope.getTypeOfCoEmp(3);
			$scope.name="";
			$scope.displayMsg="";
			$scope.empObj.employeeNumber="";
			$scope.validateCorporateEmp=function(){
				$scope.name="";
				if($scope.corEmpObj.typeOfCoEmp==undefined || $scope.corEmpObj.typeOfCoEmp=="" || $scope.corEmpObj.typeOfCoEmp==null){
					growl.warning("Please type of verification",{
						title : WARNING_MSG
					});
					return;
				}
					/*if($scope.patientDetails.patientId==undefined || $scope.patientDetails.patientId=="" || $scope.patientDetails.patientId==null){
						growl.warning("Please select patient",{
							title : WARNING_MSG
						});
						return;
					}*/
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
//							genderId:$scope.patientDetails.genderId,
//							birthDate:$scope.patientDetails.birthDate
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
											$scope.enableDisabledApply=true;	
											$scope.enableDisabledValidated=false;											
											$scope.showHideAcceptRejectBtn=true;
											
										}else if($scope.corEmpObj==2){
											
											$scope.color="red";		
											$scope.showHideAcceptRejectBtn=true;
											$scope.enableDisabledApply=true;	
											$scope.enableDisabledValidated=false;
										}else{
										}
									$scope.showHideOkBtn=false;
								}else{
									$scope.color="red";	
									$scope.name="record not found.."
									$scope.showHideAcceptRejectBtn=false;
									$scope.showHideOkBtn=true;
									$scope.showNameDetails=true;
									$scope.enableDisabledApply=false;	
									return false;
								}
								$scope.showNameDetails=true;
								 console.log("$scope.corEmplist",$scope.corEmpObj);
//								 $scope.initCoEmp();
								 angular.element('#validationEmployeeModel').modal('hide');
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
//									angular.element('#validationEmployeeModel').modal('hide');
								}else{
									  $scope.color="red";
									  $scope.showNameDetails=true;
									  $scope.name="record not found.."
									  $scope.showHideAcceptRejectBtn=false;
									  $scope.showHideOkBtn=true;
								}
								
								
							}
//							$scope.initCoEmp();
						});
				}
				
				$scope.getEmpDependantDetailsByIndex=function(index){
					$scope.showNameDetails=false;
					     if($scope.patientDetails.firstName==$scope.corEmpDependantList[index].firstName) {
//					    	 $scope.name=$scope.corEmpDependantList[index].firstName+" "+$scope.corEmpDependantList[index].middleName+" "+$scope.corEmpDependantList[index].lastName;
					    	 if($scope.patientDetails.genderId==$scope.corEmpDependantList[index].genderId){
					    		       
//					    		        $scope.color="green";
										$scope.enableDisabledValidated=false;										
										$scope.showHideAcceptRejectBtn=true;
									
					    	 }else{
//					    		    $scope.color="red";
									$scope.enableDisabledValidated=false;										
									$scope.showHideAcceptRejectBtn=true;
								}
					     }else{
//					    	   $scope.color="red";
								$scope.enableDisabledValidated=false;										
								$scope.showHideAcceptRejectBtn=true;
							}
					 	$scope.showNameDetails=true;
//					     $scope.initCoEmp();
					
				}
				
				
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
			/*else 
				if($scope.corEmpObj.note==undefined || $scope.corEmpObj.note=="" || $scope.corEmpObj.note==null){
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
			$scope.clearCoEmpDetails=function(){
			/*	$scope.corEmpDependantList=[];
				$scope.showNameDetails=false;
				 $scope.showHideAcceptRejectBtn=false;
				  $scope.showHideOkBtn=false;*/
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
			
			/* *************************************Mpesa Integration END****************************************** */
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
//						{

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
									  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != "" ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
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
								  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != "" ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
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
								  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != "" ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
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
							patientId:($scope.patientDetails.patientId == undefined || $scope.patientDetails.patientId == null || $scope.patientDetails.patientId == "") ? 0 :$scope.patientDetails.patientId ,
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
							patientId:($scope.patientDetails.patientId == undefined || $scope.patientDetails.patientId == null || $scope.patientDetails.patientId == "") ? 0 :$scope.patientDetails.patientId ,
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
//									$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
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
						patientId: $scope.patientDetails.patientId ,
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
		             
		             
//							/$scope.ipAddress+
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
													patientId : $scope.patientDetails.patientId ,
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
			
			
			$scope.clearReferral = function(){
				$scope.appointmentMasterObj.referralTypeId = '';
				$scope.appointmentMasterObj.referralDesc = '';
				$scope.appointmentMasterObj.referralRemark = '';
				$scope.appointmentMasterObj.referralId = '';
			}
			
			/* *************************************Loyalty Card START****************************************** */
			
			
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
				$scope.resend =false;
				loyaltiCardCallFactory.getLoyaltyCardByNo(cardNo,$scope);
				
			}
			
		/*	$scope.sendOTP = function(flag)
			{
				
				loyaltiCardCallFactory.sendOTP(flag,$scope);
				$scope.resend = (flag=='N')?true:false;
				
			}
			*/
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
			
			
			$scope.changeDateOnEnd = function(){
				
				if($scope.paymentEntitleMentInsurance[0].datepickerInsE != undefined || $scope.paymentEntitleMentInsurance[0].datepickerInsE != null || $scope.paymentEntitleMentInsurance[0].datepickerInsE != ''){
					  
					if($scope.paymentEntitleMentInsurance[0].datepickerInsE < $scope.paymentEntitleMentInsurance[0].datepickerInss)
					{
								   growl.warning("End should not be greater than start date",{
										title : "warning"
									});
								   $scope.paymentEntitleMentInsurance[0].datepickerInsE = new Date();
								
								   return false;
					}
			}
				
			}
			
			$scope.changeDateOnStart = function(){
				
				if($scope.paymentEntitleMentInsurance[0].datepickerInss != undefined || $scope.paymentEntitleMentInsurance[0].datepickerInss != null || $scope.paymentEntitleMentInsurance[0].datepickerInss != ''){
					
					if(moment($scope.paymentEntitleMentInsurance[0].datepickerInss).isAfter(moment(new Date()))){
						   growl.warning("Start date should be equal or less than today..!!",{
								title : "warning"
							});
						   $scope.paymentEntitleMentInsurance[0].datepickerInss = new Date();
						   return false;
								
					}else{
						//$scope.paymentEntitleMentInsurance[0].datepickerInsE = new Date();
					}
					
				}
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
			
			
			$scope.disableEnableExpiryPatReg = function(isExpiryVal)
			{
				 var splittedStringArray = isExpiryVal.split("_");
				if(splittedStringArray[1] == 'Y')
				{
					$scope.expiryDateDisabled = false;
//					$scope.datepickerExpiry=null;
				}
				else
					{
					 $scope.expiryDateDisabled = true;
					}
				$scope.patientObj.identificationNumber = '';
			}
			
			$scope.disableEnableExpiry = function(id,index)
			{
				if(id!=undefined && id!=null && id!=""){
					var isExpiryRequired = $.grep($scope.identificationList, function (doc) {
	    	            return doc.identificationId == id;
	    	        })[0].isExpiryRequired;
					console.log("isExpiryRequired",isExpiryRequired);
					if(isExpiryRequired=='Y'){
						angular.forEach($scope.kinDetailsList,function(value,idx){
							if(index == idx){
								value.expiryDateDisabled = false;
							}
						});
					}else{
						angular.forEach($scope.kinDetailsList,function(value,idx){
							if(index == idx){
							value.expiryDateDisabled = true;
							}
						});
					}
				}
				
			}
			/* *************************************Loyalty Card END****************************************** */
			
			/**************************************Ot Reservation Cancel START*****************************/
			
			$scope.currentIdx = '';
			$scope.getIndex = function(indx){
					$scope.currentIdx = indx;
					$("#confirmCancellation").modal("show");
			}
			
			$scope.patientId = "";
			$scope.cancelOTReservation = function(){
				try {
					
					$scope.patientId = $scope.otReservationList[$scope.currentIdx].patientId;
					
					if($scope.otReservationList[$scope.currentIdx].reasonId == ""){
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
			
			$scope.cancelledSelectedItem = function(){
				
				angular.forEach($scope.otReservationList ,function(value,index){
					value.isSelected = false;
					
				});
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
			
			if (angular.equals({},
					$stateParams.appointmentObj)) {
				$scope.patientSerachHideAndShow = true;
			}else{
				$scope.patientSerachHideAndShow = false;
				$(".patienttSearchResult").toggle();
			}
			
			
			$scope.cancelPaymentReqModal = function(){
				angular.element('#paymentReqModal').modal('hide');
			}
			
			$scope.clearMpesaFields = function(){
				$scope.skipMpesaValidation = false;
				$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
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
			
			/*$scope.validationForReservation = function(){
				var found = $scope.otReservationList.find(arr=>!arr.isSelected);
				if(found!=undefined){
					growl.warning("Please Select Atleast one Record for save",{
						title : WARNING_MSG
						});
					return false;
				}
			}*/
			/*********************************Ot Reservation cancel END*******************************/
			
			/**************Snapshot START*************************************************************/
			//for star camera
            $scope.filepreview = "";
            $scope.setSecondTabLevel(1);
			  var _video = null,
		        patData = null;

		    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};
			$scope.totalDisplayed = 2;
		    // Setup a channel to receive a video property
		    // with a reference to the video element
		    // See the HTML binding in main.html
		    $scope.channel = {};

		    $scope.webcamError = false;
		    $scope.onError = function (err) {
		        $scope.$apply(
		            function() {
		                $scope.webcamError = err;
		            }
		        );
		    };

		    $scope.onSuccess = function () {
		        // The video element contains the captured camera data
		        _video = $scope.channel.video;
		        _video.width = 117;
		        _video.height = 100;
		        $scope.$apply(function() {
		        	//alert(_video.width);
		            $scope.patOpts.w = _video.width;
		            $scope.patOpts.h = _video.height;
		            //$scope.showDemos = true;
		        });
		    };

		    $scope.onStream = function (stream) {
		        // You could do something manually with the stream.
		    };

			$scope.makeSnapshot = function(type) {
		        if (_video) {
		        	
		            var patCanvas = document.querySelector('#snapshot'+type);
		            if (!patCanvas) return;
		            //alert(_video.width);
		            patCanvas.width = _video.width;
		            patCanvas.height = _video.height;
		            var ctxPat = patCanvas.getContext('2d');

		            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
		            ctxPat.putImageData(idata, 0, 0);

		            sendSnapshotToServer(patCanvas.toDataURL());

		            patData = idata;
		        }
		    };
		    
		    /**
		     * Redirect the browser to the URL given.
		     * Used to download the image by passing a dataURL string
		     */
		    $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
		        window.location.href = dataURL;
		    };
		    
		    var getVideoData = function getVideoData(x, y, w, h) {
		        var hiddenCanvas = document.createElement('canvas');
		        hiddenCanvas.width = _video.width;
		        hiddenCanvas.height = _video.height;
		        var ctx = hiddenCanvas.getContext('2d');
		        ctx.drawImage(_video, 0, 0, _video.width, _video.height);
		        return ctx.getImageData(x, y, w, h);
		    };

		    /**
		     * This function could be used to send the image data
		     * to a backend server that expects base64 encoded images.
		     *
		     * In this example, we simply store it in the scope for display.
		     */
		    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
		        $scope.snapshotData = imgBase64;
		    };
			
			//for end camera
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
			  $scope.cancelPatientImg = function(){
						$scope.filepreview = "";
						$('#pieChartContainer').html(''); //remove canvas from container
						$('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>'); 
						$scope.patientImage1 = null;
						angular.element("input[type='file']").val(null);
				}
				
			  
			  /********************************snapshot END***************************************************************/
			  $scope.openEmailPopup = function(){
				  
				  if($scope.patientObj.email == undefined){
             		 $scope.patientObj.email ="";
             	 }
             	 var filter = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/; 
	           		 var email = $scope.patientObj.email;
             	 if($scope.patientObj.email == 'filter')
         			{
         			}
             	else{
             		 if (!(email.match(filter))) {
		           		    growl.error("Please enter valid email id.",{
          					title : ERROR_MSG
          					});
          					return false;;
		           		    }
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
                 		 toMailId : $scope.patientObj.email,
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
             		 $scope.storeEmailId = $scope.patientObj.email;
             	}else{
             		$scope.otpvalidated = true;
             			growl.warning("Please enter valid OTP",{title : "WARNING!"
						});
						return false;
             	}
              }
              
              $scope.checkEmail = function(){
             	 
             	// if($scope.registrationFieldObj.email == "Y"){
             		 
             		 if($scope.storeEmailId != null  && $scope.storeEmailId == $scope.patientObj.email ){
             			 $scope.otpvalidated = false;
             		 }else{
             			if($scope.patientObj.email != null && $scope.patientObj.email != "" && $scope.emailValidationRequired == 'Y'){
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
									  "email":$scope.patientDetails.email!= undefined ? $scope.patientDetails.email :  $scope.patientObj.email ,
									  "name":($scope.patientDetails.patientName  != ""  && $scope.patientDetails.patientName != undefined) ? $scope.patientDetails.patientName :  $scope.patientObj.firstName ,
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
								  "email":$scope.patientDetails.email!= undefined ? $scope.patientDetails.email :  $scope.patientObj.email ,
								  "name":($scope.patientDetails.patientName  != ""  && $scope.patientDetails.patientName != undefined) ? $scope.patientDetails.patientName :  $scope.patientObj.firstName ,
								  
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
									  "emailId":$scope.patientDetails.email!= undefined ? $scope.patientDetails.email :  $scope.patientObj.email ,
									  "name":($scope.patientDetails.patientName  != ""  && $scope.patientDetails.patientName != undefined) ? $scope.patientDetails.patientName :  $scope.patientObj.firstName ,
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
									  "patientId": $scope.patientDetails.patientId, 
									  
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
								  "emailId":$scope.patientDetails.email!= undefined ? $scope.patientDetails.email :  $scope.patientObj.email ,
								  "name":($scope.patientDetails.patientName  != ""  && $scope.patientDetails.patientName != undefined) ? $scope.patientDetails.patientName :  $scope.patientObj.firstName ,
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
				
				 $scope.openMobilePopup = function(){
                	 if($scope.patientObj.mobileNumber.length ==$scope.registrationFieldObj.mobileDigit){
                		 $('#sendOtpOnMobile').modal('show');
         			}else{
         				growl.error(" Please enter valid mobile number",{
         					title : ERROR_MSG
         					});
         					return false;
         			}
                  }
                   
                   $scope.clearMobile = function(){
                     	
                  	 $('#sendOtpOnMobile').modal('hide');
                  		 $scope.entredOptMobile = null;
                   }
                   
                   $scope.sendMobileOtp = function(){
                  	 
                  	 $rootScope.startLoader();
                  	 
                       var data ={
                      		 mobileNumber : $scope.patientObj.mobileNumber,
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
                   $scope.flgHideShow = false;
                   $scope.checkMobileNumber = function(){
           			 if( $scope.patientObj.mobileNumber != null && $scope.patientObj.mobileNumber != "" && $scope.mobileValidationRequired == 'Y'){
								$scope.otpvalidatedMobile = true;
							}else{
								$scope.otpvalidatedMobile = false;
							}
                    }
             /*****************************************************zipcode***************************************************************/
                   $scope.onClickDropdownmyFun = function(){
   					$scope.flgArea =false;
                       if($scope.selectFlag == false || $scope.selectFlag== undefined){
                           $scope.flgHideShow = true;
                           $scope.selectFlag = true;
                       }
                   }
                   $scope.selectFlag1=true;
                   $scope.onClickDropdownmy = function(){
                           if($scope.selectFlag1 == false || $scope.selectFlag1== undefined){
                               $scope.testFlg = true;
                               $scope.selectFlag1 = true;
                               $scope.flgArea1 =false;
                           }
                       }
                   $scope.getNewAreaInput = function(){
						if($scope.selectArea == false){
                          $scope.flgArea = true;
                          $scope.selectArea = true;
                      }
					}
                  $scope.getNewAreaInput1 = function(){
						if($scope.selectArea == false){
                          $scope.flgArea1 = true;
                          $scope.selectArea = true;
                      }
					}
   				$scope.myFunctionForTogglePer = function(flag) {
   						
   						if ($scope.tempFlgPer == true) {
   							$scope.testFlgPer = false;
   							$scope.tempFlgPer = false;
   							return;
   						}
   						if (flag == true) {
   							$scope.testFlgPer = true;
   							$scope.tempFlgPer=true;
   						}

   					}
   				
   				$scope.setFlagFalse=function(flag){
   					if(flag==false){
   						$scope.testFlg = false;
                           $scope.selectFlag = true;
                           $scope.storeEmailId = null;
   					}
   					
   				}
   				$scope.setFlag=function(flag){
   					if(flag==false){
   						$scope.testFlg = false;
                           $scope.selectFlag1 = true;
                           $scope.storeEmailId = null;
   					}
   					
   				}
   			 $scope.applySize = function()
		       {
		    	   $('#selRegsPer').focus(function () {
		    	 	    $('#selRegsPer').attr("size", "4");
		    	 	});
		    		
		    	 	$('#selRegsPer').change(function () {
		    	 	    $('#selRegsPer').attr("size", "1");
		    	 	    $("#selRegsPer").blur();
	    	 	});						       
		    	 	}

		    $scope.newPatientRegistration= function(flag){
		      if (flag === "PatientVisit-Screen") {
                    $state.go('patientRegistration', {
                        screenType: flag
                    });
                }
		    }


		   // patient searching auto fill data start

		   	$rootScope.searchPatientVisitData = function(patientsearchObj){

           		if($scope.patientSearchObj.uhIdNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNo == '' && $('#datepickerSearch').val() == '' && ( patientsearchObj.identificationTypeId == "" || patientsearchObj.identificationTypeId == undefined ) && $scope.patientSearchObj.identificationNo == '')
           		{
           			growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
           			});
           			return false;
           		}

           		else{
           				//alert("else AlERT");
           			}
           		if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
           		{
           			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
           			$scope.patientList = [];
           			return false;
           		}
           		$rootScope.startLoader();

           	    if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
                       patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
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
           		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
           		BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){

           			if(response.data.listObject.length == 0){
           				growl.warning("No Result Found!",{
           					title : ERROR_MSG
           				});
           			}
           			else{
           				$scope.patientList = [];
           			angular.forEach(response.data.listObject,function(value,key)
           					{
           				  		if(value.registrationTypeId != 5 && value.registrationTypeId != 4)
           				  			{
           				  			$scope.patientList.push(value);
           				  			}

           					});
           			}

           			if($scope.patientList.length > 0){
                        $scope.getPatientDetailsByIndex(0);
                        $(".patienttSearchResult").css("display","none");
                    }


           		});
           		console.log("patientList=",$scope.patientList)
           	}
		   // patient searching auto fill data end

		      if ($stateParams.patientVisitDataScreen) {
                $scope.patientSearchObj = {
                          "uhIdNumber"      : $stateParams.patientVisitDataScreen.uhidNumber,
                          "patientName"     : "",
                          "genderId"        : "",
                          "mobileNo"        : "",
                          "birthDate"       : "",
                          "identificationNo":"",
                          "patientType" : 1,
                          "unitId"          :$scope.unitId,
                          "organizationId"  :$scope.organizationId
                              }
                  $rootScope.searchPatientVisitData($scope.patientSearchObj);
                  }


                  
}]);
