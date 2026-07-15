/**
 * @author Vivek Controller to handle OPD Billing
 */

angular.module("myApp").controller("newOPDBillingController", [ '$scope','$rootScope','$cookies','$state','BillingGenericService','GenericService','$stateParams','$timeout','$http','growl','paymentCollFactory','GenericServiceParamHeader','reportService','servicesFactory','loyaltiCardCallFactory','$location','GenericServiceFileUpload','$interval','$window',function($scope,$rootScope,$cookies,$state,BillingGenericService,GenericService,$stateParams,$timeout,$http,growl,paymentCollFactory,GenericServiceParamHeader,reportService,servicesFactory,loyaltiCardCallFactory,$location,GenericServiceFileUpload,$interval,$window)
	{
	
	var addService = 0;
	var init = 1;
	var quantity = 2;
	var unitPrice = 3;
	var discount = 4;
	var discount = 5;
	var discountCat = 5;
	
	$scope.financialRequestSendTo = "";
	$scope.popUpFlaginv = true;
	
	$scope.isNewOpdBill = 'Y';
	
	$scope.init = function() 
	{
	 $scope.isBillWiseDiscountApplied = false;
	 $scope.isSplitBillFlag  =false;
	 $scope.isSplitBillApplied = false;
	 $scope.serviceViseSplit = false;
	 $scope.service = null;
     $scope.getDisRiqFlag = true;
     $scope.needToUpdatePharmacyService = false;
     $scope.applyAuth = false;
     $scope.preBillId = "";
     $scope.updatePayee = false;
     $scope.cashSettlement = 'N';
     $scope.homeCollection = 'N';
     $scope.groupWiseDisFlag = true;
     $scope.enableDisabledValidated=false;
     $scope.capExist = false;
     $scope.cancelService = false;
     $scope.isAddendumBill = 'N';
     $scope.packaged = {};
     $scope.packaged.withPackageDetail = 'N';
     $scope.packaged.withRatePackageDetail = 'N';
     $scope.fileObj = {};
     $scope.fileObj.file = null;
     $scope.cashCollectedAmount = 0;
     $scope.actualServiceCount =0;
     $scope.flagForSale=false;
     $scope.flagForPay=false;
     $scope.flagForBharatQr=false;
     $scope.isBillManipulationAllowed = false;
     $scope.objPosMachine={
 			isSale:false, 
 			isSMSpay : false,
 			isBharatQr:false, 
 			posMachineDeviceId:"",
 			deviceName:"",
 			machineConfigDetails:"",
 			mobileNumberForPOS:""
 	};
     $scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
	if (cookieObjectAppConfigSecond != undefined) {
	$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
	$scope.ambulanceChargesServiceId =  cookieObjectAppConfigSecond.ambulanceChargesServiceId;
	}
	$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
	$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
	$scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
	$scope.serviceWiseDoctorListReq =  cookieObjectAppConfig.serviceWiseDoctorListReq;
	$scope.exceededBillAmountSettlement=cookieObjectAppConfigSecond.exceededBillAmountSettlement;
    $scope.runTimeRateChangeUsingTarrif=cookieObjectAppConfigSecond.runTimeRateChangeUsingTarrif;
    $scope.specialityId=cookieObjectAppConfig.consultationSpecialityId;
    $scope.consultationSpecialityId = cookieObjectAppConfig.consultationSpecialityId;
    $scope.pharmacyServiceId=cookieObjectAppConfig.pharmacyServiceId;
	$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
	$scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
	
	 $scope.physiotherapySpecialityId=cookieObjectAppConfig.physiotherapySpecialityId;
	 
	 $scope.hemodialysisSpecialityId=cookieObjectAppConfig.hemodialysisSpecialityId;
	 
	if(cookieObjectAppConfig == undefined){
		$state.go('applicationConfiguration');
		return;
	}
	else
		{
			// console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			
		}
	 $scope.searchAdvised ={};
    $scope.labConfig  =    $scope.checkLabConfig();
    $scope.isConsolidatedBill = cookieObjectAppConfig.isConsolidatedBill;
	$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
	$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
	$scope.isSplitBillUnitFlag = cookieObjectAppConfig.isServiceWiseSplitBill;
	$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
	$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
	$scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
	$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
	$scope.isSchemeDiscount = cookieObjectAppConfig.isSchemeDiscount;
	$scope.isGstVisible = cookieObjectAppConfig.isGstVisible;
	$scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	$scope.billSettlement = cookieObject.billSettlement;
	$scope.mpesaRequired = cookieObject.mpesaRequired;
	$scope.isRenderedServiceCancelled = cookieObject.isRenderedServiceCancelled;
	$scope.serviceRateEditable = cookieObject.serviceRateEditable;
	$scope.isSmartCardRequired = cookieObjectAppConfig.isSmartCardRequired;
	$scope.isPackageApplicableDateEditable = cookieObjectAppConfig.isPackageApplicableDateEditable;
	$scope.isForceUpdateServicePackage = cookieObject.isForceUpdateServicePackage;
	$scope.neuronPlus = cookieObjectAppConfig.neuronPlus;
	$scope.couponSystemApplicable = cookieObjectAppConfigSecond.couponSystemApplicable;
	$scope.transferToActiveDeposit = "N";
	$scope.page = "billList";
    $scope.hitMpesaApi = false;
	$scope.billDisabled = false;
	$scope.forceAddOrRemove = false;
	$scope.isPrebarcodeGenerantion =  $scope.labConfig.isPrebarcodeGenerantion;
	$scope.dateFormat=cookieObjectAppConfig.dateFormat;
	$scope.changePayeeRights = cookieObject.changePayeeRights;
	// for update Payee
	$scope.paymentEntitlementTypeIdPopup = "1";
	console.log("55555555555555555555",$scope.dateFormat);
	
    var cookieObjectLabConfig = JSON.parse(localStorage.getItem('labConfig'));
    if (cookieObjectLabConfig) {
        $scope.outsourceLabId = cookieObjectLabConfig.outsourceLabId;
    }
	
	$scope.paymentEntitleMentCorporate=
		[{
			'companyId':"",
			'contractId':"",
			// 'statrDate':"",
			// 'endDate':""
		}];
	
	$scope.paymentEntitleMentInsurance=
		[{
			'companyId':"",
			'associateCompanyId':"",
			'contractId':"",
			// 'statrDate':"",
			// 'endDate':"",
			'insuranceNumber':"",
			'insuredAmt':""
		}];
	
	
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
	// For Update Payee
	/**
	 * ================================COUNTER CODE
	 * START======================================
	 */
	$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
	$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
	if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
		{
		     var flag = $scope.getCounterDetails();
		     if(flag)
		    	 {
		    	 	$rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
		    	 	$rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
		    	 }
		     else
		     {
		    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
		     }
		}else{
			$rootScope.counterId = 0;
		}
	/**
	 * ================================COUNTER CODE
	 * END======================================
	 */
	
	
	$scope.visitTypeId=1;
	$rootScope.loginpage = true;
	$scope.currentDate = new Date();
	$scope.serviceMasterIdCheck = {};
	$scope.itemSelecteds = {};
	$scope.autoRenderedServicesFinalList = [];
	$scope.autoRenderedServicesForSaveAndUpdate=[];
	$scope.removeDiscountList = [];
	$scope.patientList  = [];
	$scope.removeServicesList = [];
	$scope.packageResultObj = {};
	$scope.skipMpesaValidation = false;
	$scope.pinpadAmounFlag = false;
	$scope.finalBill = "Y";
	$scope.isPatientClass = "N";
	$scope.smart=true;
	$scope.roundOffAmt = parseFloat(0).toFixed(2);
	$scope.billRemark = '';
	$scope.DepositObj = {};
	$scope.DepositObj.userEnterDepositAmount = 0;
	$scope.transferToActiveDeposit = "N";
	$scope.discountRadio = 1;
	
	$scope.saveBtnDisable = false;
	
	// start for request discount amount DD
	$scope.requestDiscountDiv = false;
	$scope.requestApporivedDiscountDiv = false;
	$scope.discountDisabledFlagForRequest = false;
	// end for discount amount DD
	 
	 	$scope.totalAmountSummary = "0.00";
		$scope.totalConcessionSummary = "0.00";
		$scope.netPayableSummary = "0.00";
		$scope.totalDiscountSummary = "0.00";
		$scope.totalGstTaxSummary = "0.00";
		$scope.totalCreditPayble = "0.00";
		$scope.totalSelfPayble = "0.00";
		$scope.totalSelfPaybleTemp = "0.00";
		$scope.totalPackagePaybleTemp = "0.00";
		$scope.AutoRenderednetTotalDiscount = "0.00";
		
		// for datepicker 1
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
	
	var commonObj = {
			unitId : $scope.unitId,
			organizationId : $scope.organizationId
		};
	
	// for popup
	$scope.showModal = false;
	$scope.buttonClicked = "";
	$scope.popUpFlag = true;
	// end for popup
	
	$scope.btnDisabled = false;
	
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
			"encounterId" : ""
	}
		
		$scope.encounterDetails = {
				encounterId:""
		}
		
		$scope.billingMasterObj = {
				"billingMasterId"   :"",
				"encounterId"		:$scope.patientDetails.encounterId,
				"visitTypeId"		:$scope.visitTypeId,
				"patientId"			:$scope.patientDetails.patientId,
				"billNumber"		:"",
				"billDateTime"		:new Date().getTime(),
				"netAmount"			:"",
				"discountAmount"	:"",
				"grossAmount"		:"",
				"concessionAmount"	:"",
				"gst"				:"",
				"totalBillAmount"	:"",
				"outstanding"		:"",
				"roundOff"			:"",	
				"isCreditBill"		:"",
				"unitId"			:$scope.unitId,
				"organisationId"	:$scope.organizationId,
				"status"			:"A",
				"createdDate"		:new Date().getTime(),
				"createdBy"			:$scope.userId,
				"updatedDate"		:new Date().getTime(),
				"updatedBy"			:$scope.userId,
				"bedTypeId":"1",
				"patientTypeId":"1",
				"paymentTypeId":"1",
				"isDiscountInPercentage":'I',
				"isDiscountApplicable":"I",
				"isDrNote":"N",
		        "drNoteAgainstBillId":"",
				"listBillingDetailsDto":[],
				"listAutoRenderedServiesBillingDetails":[],
			// "orderDetailsDiscountDto":"",
				"orderMasterDto"		:{
						"orderId"		:"",
						"orgId"			:$scope.organizationId,
						"orgUnitId"		:$scope.unitId,
						"orderNo"		:"",
						"orderDate"		:new Date().getTime(),
						"visitTypeId"	:"1",
						"visitAdmId"	:$scope.patientDetails.encounterId,
						"ordDoctorId"	:"",
						"patientId"		:$scope.patientDetails.patientId,
						"priorityId"	:"2",
						"patientType"	:"",
						"wardId"		:"",
						"drCrId"		:"",
						"ordRemarks"	:"",
						"createdBy"		:"1",
						"createdDate"	:new Date().getTime(),
						"updatedBy"		:"1",
						"updatedDate"	:new Date().getTime(),
						"orderStatus"	:"A",
						"orderStatusId"	:"1",
						"listOrderDetailsMasterDto":[]
					}
				
		}
		
		// for userList DD
		
		var URI = BASE_URL + ROOT_URL
		+ "/billing/getUsersDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/userId/"+$scope.userId;
		BillingGenericService.serviceAction(METHOD_GET, URI,"",
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					// console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						$scope.userDiscountObject ={};
						$scope.userDiscountObject = response.data.object;
						if($scope.userDiscountObject != null){
				    		
							if($scope.userDiscountObject.discountType == 1){
								$scope.userDiscountObject.fromDiscountPer = $scope.userDiscountObject.fromDiscountAmount;
								$scope.userDiscountObject.toDiscountPer = $scope.userDiscountObject.toDiscountAmount;
							}
				    	}
							 
						}
						
				});
		// for UserList DD

$scope.corEmpVerificationFlag=false;
var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
var obj = {
			organizationId :$scope.organizationId,
			unitId :$scope.unitId
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



var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+$scope.organizationId+"/"+$scope.unitId;
BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
   {
	if (response.data.status == "success")
	{
		$scope.hospitalBankList = response.data.listObject;
	}
  });


var objBankL={
		"unitId":$scope.unitId,
		"orgnisationId" : $scope.organizationId
}
$rootScope.startLoader();
var URI = BASE_URL + ROOT_URL + API + BILLING + "/bankMaster";
BillingGenericService.serviceAction(METHOD_POST,URI, objBankL, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	if(response.data.status == "success"){
		$scope.bankList = response.data.listObject;
	}
});

var URI = BASE_URL + ROOT_URL + API + BILLING + "/cardTypeMaster";
BillingGenericService.serviceAction(METHOD_POST,URI, objBankL, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	if(response.data.status == "success"){
		$scope.cardTypeList = response.data.listObject;
	}
});
$scope.transactionTypesList = [];
var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+$scope.organizationId+"/"+$scope.unitId;
BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	if(response.data.status == "success"){
		$scope.transactionTypesList = response.data.listObject;
	}
	
	// console.log("transactionTypesList",$scope.transactionTypesList);
});



var URI = BASE_URL + ROOT_URL
+ GETACTIVEREASONLIST;

BillingGenericService
.serviceAction(METHOD_POST, URI,
		commonObj,
		NOTIFICATION_MSG_STATUS_FALSE)
.then(
		function(response) {
			// console.log(response);
			// $rootScope.startLoader();
			if (response.data.status == "success")
				$scope.reasonList = response.data.listObject;
		});
// var URI = BASE_URL + ROOT_URL + GETACTIVEDOCTORLIST;
// var URI = BASE_URL + ROOT_URL + "/adt/getActiveDoctorList";


var URI = BASE_URL + ROOT_URL + "/adt/getDoctorListForOPDBilling";// for
																	// HIS-15355

GenericService
.serviceAction("POST", URI, commonObj)
.then(
		function(response) {
			$scope.doctorsList = [];

			if (response.data.status == "success") {
				$scope.doctorsList = response.data.listObject;
				
			}
		});


var URI = BASE_URL + ROOT_URL + "/api/billing/discountCategories/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	$scope.discountCategoryList = response.data.listObject;
});

	$scope.servicesSelectedList = {
	    services: []
	  };
	
	$scope.groupWiseServicesSelectedList = {
			services: []
	}
	
	
	$scope.billWiseDiscountObj = {
			"discountGivenBy":$scope.userId,
			"discountCategoryId" : "",
			"discountAmountBillWise" : "",
			"discountPercentageBillWise" : "",
			"approvedAmountBillWise":"",
			"requestUserId":"",
			"requestAmountBillWise" : 0,
			"remark" :""
	}
	
	$scope.finanacial = {
			"financialRequestSendTo":""
	}
	$scope.isFinancialClearence = 'N';
	

	// yogesh
	$scope.searchPackageList = [];
	$scope.selectedPackageList = [];
	$scope.packageDetailsList = [];
	$scope.initPackageSearchObj();
	$scope.getSexMasterList();
	$scope.getIdentificationList();
	$scope.search='';
	$scope.forServicePayeeId  = $scope.selfPayeeId;
	$scope.servicesListPopup = [];
	/* First Level */
	$scope.firstTabLevel = 1 ;
	$scope.getPackageTypeList($scope.organizationId,$scope.unitId);
	
	var commonObjSpe = {
			unitId : $scope.unitId,
			organizationId : $scope.organizationId
		};
	var URI = BASE_URL + ROOT_URL
	+ GETACTIVESPECIALITY;
		BillingGenericService.serviceAction(METHOD_POST, URI,commonObjSpe,
			NOTIFICATION_MSG_STATUS_FALSE)
	.then(function(response) {
				// console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
					{
					$scope.specialityMasterList = response.data.listObject;
						 
					}
					
			});
		
		 var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
		    GenericService
		        .serviceAction(METHOD_GET, URI,
		                NOTIFICATION_MSG_STATUS_FALSE)
		        .then(
		                function(response) {
		                    // console.log(response);
		                    if (response.data.status == "success"){
		                        
		                        $scope.patientTypeList = response.data.listObject;
		                        // console.log("list",$scope.patientTypeList);
		                    }
		                    
		                });
		    
		    // list for packages
		    $scope.listPackageOrder = [];
		    $scope.getReferralMasterList = function(referralTypeId){
				var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByOrgId";
				var obj=
				      { 
						 organizationId:$scope.organizationId,
				      };
					BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
							$scope.referralMasterList = [];
							if (response.data.status == "success") {
								$scope.referralMasterList = response.data.listObject;
							}
					});
				};
			$scope.getReferralMasterList();
	
	}
	
	
	
	// chetana
	$scope.serviceCheckFlag=false;
	$scope.checkServiceRenderOrNot = function()
	{
		$scope.serviceCheckFlag=true;
		if($scope.isFromAddServiceToTempServiceList==true){
			$scope.addServiceToTempServiceList($scope.item,$scope.mod, $scope.labl,$scope.freeTextOrderedServicesId,$scope.freeTextserviceName);
		}else{
			 $scope.addServiceFromSearch($scope.item,$scope.mod, $scope.labl, true);
		}
		
	}
	
	$scope.serviceCheckFlagForParameter=false;
	$scope.checkServiceRenderForParameter = function()
	{
		$scope.serviceCheckFlagForParameter=true;
		if($scope.isFromAddServiceToTempServiceList==true){
			$scope.addServiceToTempServiceList($scope.item,$scope.mod, $scope.labl,$scope.freeTextOrderedServicesId,$scope.freeTextserviceName);
		}else{
			 $scope.addServiceFromSearch($scope.item,$scope.mod, $scope.labl, true);
		}
		
	}
	
	$scope.getSpecialityList=function(){
		var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
		var URI = BASE_URL + ROOT_URL
		+ GETACTIVESPECIALITY;
			BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					// console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						$scope.specialityMasterList = response.data.listObject;
							 
						}
						
				});
			
			
	}
	// $scope.getSpecialityList();
	/*
	 * $scope.addPaymentDetails = function(){
	 * paymentCollFactory.addPaymentDetails($scope); }
	 */
	
	// Check isActiveSelected
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
		
		var parentChild = 0;
		$scope.addExAmount = function(){
			$scope.isBillList = 0;
			if( parentChild == 0 ){
				$scope.transferToActiveDeposit = 'Y';
				$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
			// this.$parent.$parent.$parent.transferToActiveDeposit = 'Y';
			// $scope.transferToActiveDepositFact =
			// this.$parent.$parent.$parent.transferToActiveDeposit;
			}else{
				
				$scope.transferToActiveDeposit = 'Y';
				$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
				
			// this.$parent.$parent.transferToActiveDeposit = 'Y';
			// $scope.transferToActiveDepositFact =
			// this.$parent.$parent.transferToActiveDeposit;
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
			cashLimit = cashLimit + $scope.cashCollectedAmount;
		}
		
		if(($scope.cashAmountLimit != undefined && $scope.cashAmountLimit !=null ) && cashLimit >  $scope.cashAmountLimit){
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
		} if(parseFloat(exAmount) <= parseFloat($scope.payeeDueAmtFinal))
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
	
	
	$scope.removePaymentDetails = function(){
		paymentCollFactory.removePaymentDetails($scope);
	}
	
	 $scope.checkAll = function()
	 {
	    	if($scope.checkAllFlag == true)
	    		{
	    		  $scope.servicesSelectedList.services = $scope.autoRenderedServicesFinalList.map(function(item) { return item.serviceMasterId; });
	    	}
	    	else
	    		{
	    		$scope.servicesSelectedList.services = [];
	    		}
	    	
	 };
	 
	 $scope.getActivePatientTypeList = function()
	    {
	        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
	    GenericService
	        .serviceAction(METHOD_GET, URI,
	                NOTIFICATION_MSG_STATUS_FALSE)
	        .then(
	                function(response) {
	                    // console.log(response);
	                    if (response.data.status == "success"){
	                        
	                        $scope.patientTypeList = response.data.listObject;
	                        // console.log("list",$scope.patientTypeList);
	                    }
	                    
	                });
	        
	    }
			
	 $scope.groupWiseCheckAll= function(chkFlg) {
			$scope.disPerPopupForGroup = '';
			$scope.disAmtPopupForGroup = '';
			$scope.disCategoryIdForGroup = '';
			$scope.disPerPopupForGroupFull = 0;
			if(chkFlg == 'Y'){
			$scope.groupWiseServicesSelectedList.services = $scope.groupWiseDiscountServicesList.map(function(item) { return item.orderDetailsId; });
			}else{
				$scope.groupWiseDisFlag = true;
				$scope.groupWiseServicesSelectedList.services = [];
			}
	 };
	
	
	$scope.getSexMasterList = function(){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.sexMasterList = response.data.listObject;
		});
	}
	
	
	$scope.selectItem = function(item) {

		$scope.itemSelecteds = [];
		// If checkbox is checked
		if ($scope.serviceMasterIdCheck) {
			$scope.itemSelecteds = item;
		}
		// console.log(item);
	}
	
	
	$scope.wipeOutServices = function(){
    	if($scope.servicesListPopup.length>0){
    	$scope.servicesListPopup = [];
		 growl.warning('Added Services are wiped out..!!',{
											title : 'Warning!'
										});
    	}
    }
    
    $scope.wipeOutPackages = function(){
    	if($scope.selectedPackageList.length>0){
    		$scope.selectedPackageList = [];
		 growl.warning('Added Packages are wiped out..!!',{
											title : 'Warning!'
										});
    	}
    }
	
	// Idetification List
	$scope.getIdentificationList = function(){
		var URI = BASE_URL + ROOT_URL+ GETACTIVEIDENTIFICATION;
		GenericService.serviceAction("GET", URI, "").then(function(response) {
			$scope.identificationList = [];
			if (response.data.status == "success") {
				$scope.identificationTypeList = response.data.listObject;
				

			}
		});
	}
	
	$scope.getIdentificationTypeList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
				$scope.identificationTypeList = response.data.listObject;
		});
	}
	$scope.getIdentificationTypeList();	
	
	
	
	$scope.setBankName=function(bankId){
		angular.forEach($scope.bankList, function (value, key) {
			if(value.bankId == bankId){
				$scope.paymentDetails.bankName = value.bankName;
				return;
			}
		});
		
	}
	
	$scope.loseSessionForBillEdit = function()
	{
		$scope.newOpdBillObject = "";
		$scope.preBillId = "";
		$scope.isAddendumBill = 'N';
		$rootScope.searchPatient($scope.patientSearchObj);
	}
	$scope.keepSessionForBillEdit = function()
	{
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
	}
	
	$rootScope.searchPatient = function(patientsearchObj){
		
		$scope.requestStatusObject = {};
        $scope.getDisRiqFlag = true;
    	$scope.requestApporivedDiscountDiv = false;
		$scope.billWiseDiscountObj.discountPercentageBillWise = "";
		$scope.billWiseDiscountObj.discountAmountBillWise ="";
		$scope.billWiseDiscountObj.discountCategoryId ="";
		
		$scope.billWiseDiscountObj.requestAmountBillWise = "";
		$scope.billWiseDiscountObj.approvedAmountBillWise = "";
		$scope.requestDiscountDiv = false;
		
		if($scope.preBillId!=undefined && $scope.preBillId>0)
		{
			angular.element('#billEditSessionLost').modal('show');
			return false;
		}
		
		
		if($scope.patientSearchObj.uhIdNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNo == '' && $('#birthDate').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNo == '')
		{
			
		}
		if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			return false;
		}	
		if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
			patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
		}
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByForOPBilling";
		BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.patientList = response.data.listObject;
//			angular.forEach($scope.patientList,function(value,index)
//					{
//
//					value.birthDate =moment(value.birthDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
//					});
			 if(response.data.listObject==null){
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$scope.patientSearchObj.birthDate="";
					$scope.patientList=[];
					return false;
				}
			 
			 $scope.patientSearchObj.birthDate="";
			$scope.patientEncounterList = [];
			
			$scope.encounterDetails = {
					encounterId : "",
					encounterDateString : "",
					referralDesc : "",
					specialityName : "",
					doctorName : "",
					patientCategory : "",
					paymentEntitlementDesc : "",
					companyDesc : "",
					paymentEntitlementTypeId : "",
					payeeId : "",
					patientId : "",
					doctorId : "",
			}
			$scope.autoRenderedServicesFinalList = [];
			$scope.autoRenderedServicesForSaveAndUpdate=[];
			$scope.patientDetails ={};
			
			$scope.totalAmountSummary = "0.00";
			$scope.totalConcessionSummary = "0.00";
			$scope.netPayableSummary = "0.00";
			$scope.totalDiscountSummary = "0.00";
			$scope.totalGstTaxSummary = "0.00";
			
			$scope.totalCreditPayble = "0.00";
			$scope.totalSelfPayble = "0.00";
			
		});
	}

	$scope.getFreeTextOrders = function(encounterId)
	{
		var data1={
				encounterId:encounterId,
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/getFreeTextOrderServices";
		BillingGenericService .serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.freeTextServiceList = [];
							$scope.servicesListPopup = [];
							if (response.data.status == "success") {
								
								angular.forEach(response.data.listObject,function(value,index){
									value.search="";
									value.status='A';
								});
								
								$scope.freeTextServiceList = response.data.listObject;
								if(response.data.listObject.length > 0){
									 angular.element('#freeTextServicesModal').modal('show');
								}
							}

						});
	}
	

	$scope.getFutureOrders = function()
	{
		// SNEHA
		var data1={
				patientId:$scope.patientDetails.patientId,
				organisationId:$scope.organizationId,
				unitId : $scope.unitId,
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientServicesForFutureDate";
		BillingGenericService .serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							// console.log(response);
							$scope.futureServiceList = [];
							// $rootScope.startLoader();
							if (response.data.status == "success") {
								angular.forEach(response.data.listObject,function(value,index){
									
									if(moment(new Date()).format('YYYY-MM-DD') == value.orderSchDate){
										value.checkOption='Y';
									}else{
										value.checkOption='N';
									}
									
									
								});
								$scope.futureServiceList = response.data.listObject;
								if(response.data.listObject.length > 0){
									 angular.element('#futureServicesModal').modal('show');
								}
								// console.log("Future service
								// List",$scope.futureServiceList);
							}

						});
	}
	
	
	$scope.saveFutureServicesEncounter=function()
	{
		
		var listServiceMasterDto=[];
		
		angular.forEach($scope.futureServiceList,function(value,index)
		{
			if(value.checkOption=='Y')
			{
				listServiceMasterDto.push(value); 
			}
		  });
		 if(Object.keys(listServiceMasterDto).length >0)
		 {
			 angular.element('#futureServicesModal').modal('hide');
			 
			 var encounterDetailsFuture = angular.copy($scope.encounterDetailsFuture);
			 
			 $scope.encounterDetailsFuture.defaultSelfTariffId = $scope.defaultSelfTariffId;
			 $scope.encounterDetailsFuture.billingBedCategoryId =$scope.opdBillingCatId;
			 $scope.encounterDetailsFuture.listServiceMasterDto = listServiceMasterDto;
			 $scope.encounterDetailsFuture.organizationId  = $scope.organizationId;
			 $scope.encounterDetailsFuture.unitId  = $scope.unitId;
			 $scope.encounterDetailsFuture.visitTypeId = 1;
			 $scope.encounterDetailsFuture.doctorId = $scope.encounterDetails.doctorId;
			 $scope.encounterDetailsFuture.referralId = $scope.encounterDetails.referralId;
			 $scope.encounterDetailsFuture.paymentEntitlementId = $scope.encounterDetails.paymentEntitlementTypeId;
			 var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/future/services";
				BillingGenericService.serviceAction(METHOD_POST,URI, $scope.encounterDetailsFuture, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						
						$scope.getAutoRenderedServicesByPatient(encounterDetailsFuture);
				
					}
				});
				
		 }else{
			 growl.warning("Please select at least one serivce..!!",{
					title : WARNING_MSG
				});
				return;
		 }
		
// //console.log("accepted
// Services",$scope.appointmentMasterObj.listServiceMasterDto);
	}
	
	$scope.selectAllPendingServices=function(selectAllPending){
		if(selectAllPending == 'Y'){
			angular.forEach($scope.futureServiceList,function(value,index){
				value.checkOption='Y';
			});
			}else{
				angular.forEach($scope.futureServiceList,function(value,index){
					value.checkOption='N';
				});
			}
		// console.log("checkAll",$scope.pendingServicesList);
	}
	
	$scope.checkAllTrue = function() {
		var futureServiceLength = $scope.futureServiceList.length;
		$scope.servicesCount=0;
		angular.forEach($scope.futureServiceList,function(value,index){
			if(value.checkOption == 'Y'){
				$scope.servicesCount++;
			}
		});
		if($scope.servicesCount == futureServiceLength){
			$scope.selectAllPending = 'Y';
		}else{
			$scope.selectAllPending = 'N';
		}
	};
	
	$scope.getPatientDetailsByIndex = function(index)
	{
		
		$scope.lineLevelItem = false;
		$("#patientSelectRadio_"+index).prop("checked",true);
		$scope.getSpecialityByDoctorMain();
		$rootScope.startLoader();
		$scope.forceAddOrRemove = false;
		$scope.requestStatusObject = {};
		$scope.fileObj = {};
	 	$scope.fileObj.file = null;
        $scope.getDisRiqFlag = true;
    	$scope.requestApporivedDiscountDiv = false;
		$scope.billWiseDiscountObj.discountPercentageBillWise = "";
		$scope.billWiseDiscountObj.discountAmountBillWise ="";
		$scope.billWiseDiscountObj.discountCategoryId ="";
		$scope.billWiseDiscountObj.requestAmountBillWise = "";
		$scope.billWiseDiscountObj.approvedAmountBillWise = "";
		$scope.requestDiscountDiv = false;
		$scope.autoRenderedServicesFinalList = [];
		$scope.autoRenderedServicesForSaveAndUpdate=[];
		$scope.patientDetails.age=($scope.patientList[index].age);
		$scope.patientDetails.patientName=($scope.patientList[index].patientName);
		$scope.patientDetails.firstName=($scope.patientList[index].firstName)+" "+($scope.patientList[index].middleName)+" "+($scope.patientList[index].lastName);
		$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
		$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
		$scope.selectedUhid = $scope.patientDetails.uhIdNumber;
		$scope.patientDetails.patientId=($scope.patientList[index].patientId);
		$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
		$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
		$scope.patientDetails.patientTypeId = ($scope.patientList[index].patientCategoryId);
		$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
		$scope.patientDetails.address = ($scope.patientList[index].address);
		$scope.patientDetails.cityName = ($scope.patientList[index].cityName);
		$scope.patientDetails.prefixId = ($scope.patientList[index].prefixId);
		
		 $scope.countryCallingCode = ($scope.patientList[index].countryCallingCode);
		$scope.getNewDoctorList();
		if($scope.patientList[index].mobileNo!=null){
			$scope.mobileNumber = parseInt($scope.patientList[index].mobileNo);
			$scope.objPosMachine.mobileNumberForPOS=$scope.patientList[index].mobileNo;
			$scope.razorPayObject.razorMobileNo = $scope.patientList[index].mobileNo;
		}
		$scope.patientDetails.visitTypeId = 1;
		var data = {};
		if($scope.preBillId > 0 && $scope.preBillId != undefined){
			var URI = BASE_URL + ROOT_URL + "/api/opd/canelledBills/encounterDetails/"+$scope.encounterDetails.encounterId;
		}else{
			var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientList[index].patientId;
		}
		$scope.getTariffList($scope.unitId,$scope.organizationId);
		BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
			{
				$scope.patientEncounterList = [];
				$scope.patientEncounterList = response.data.listObject;
				if($scope.patientEncounterList.length == 0){
					growl.warning("No Result Found!",{
						title : ERROR_MSG
					});
				}
				if($scope.patientEncounterList.length == 1)
					{
						
					 $scope.encounterDetails.encounterId = $scope.patientEncounterList[0].encounterId;
					 $scope.encounterDetails.encounterNumber = $scope.patientEncounterList[0].encounterNumber;
					 $scope.patientDetails.email = $scope.patientEncounterList[0].email;
					 $scope.getEncounterDetails($scope.patientEncounterList[0].encounterId);
					}else{
						 $scope.encounterDetails.encounterId = $scope.patientEncounterList[0].encounterId;
						 $scope.encounterDetails.encounterNumber = $scope.patientEncounterList[0].encounterNumber;
						 $scope.patientDetails.email = $scope.patientEncounterList[0].email;
						 $scope.getEncounterDetails($scope.patientEncounterList[0].encounterId);
					}
			}
			
		});
		
		if($scope.corEmpVerificationFlag){
			$scope.enableDisabledApply=false;
		}else{
			$scope.enableDisabledValidated=false;
			$scope.enableDisabledApply=false;
		}
	
			$(".patienttSearchResult").toggle();
		
			$rootScope.stopLoader();
	}
	
	
	$scope.getAuthrizationDetails = function()

	{
		$timeout(function () {

	        var idsearchServiceId = 'searchServiceId';

	        document.getElementById(idsearchServiceId).focus();  

	   }, 1000);
		
		if($scope.patientDetails.patientId==null)
			{
			growl.warning("Please select Patient..",{
				title : ERROR_MSG
				
			});
			return false;
			}
		if($scope.encounterDetails.payeeId==null)
			{
			growl.warning("Please select payer.",{
				title : ERROR_MSG
			});
			return false;
			}
		$rootScope.startLoader();
		 $scope.addNewRequestList = [];
	    var param = {"organisationId":$scope.organizationId,"unitId":$scope.unitId,"patientId":$scope.patientDetails.patientId, 
	    		"isSplitStatus": "N","authStatusId": 1,"encounterId":$scope.encounterDetails.encounterId};
	    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListByPayeeId"
	    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
	  if (response.data.status == "success"){
	    $scope.addNewRequestList = response.data.listObject;
	    $scope.newRequestCalculates();
	  }
	     $rootScope.stopLoader();
	 });
	}
	
	
	$scope.contractId = 0;
	$scope.AddServiceBtn = true;
	$scope.getencounterNumber = function(info)
	{
		$scope.encounterNumber = info.encounterNumber;
	}
	// $scope.autoRenderedServicesList = [];
	$("#savedisablebtn").addClass("disabled");
	$scope.getEncounterDetails = function(encounterId)
	{
		$scope.patientClassdDescription = '';
		$scope.requestApporivedDiscountDiv = false;
		$scope.billWiseDiscountObj.discountPercentageBillWise = "";
		$scope.billWiseDiscountObj.discountAmountBillWise ="";
		$scope.autoRenderedServicesFinalList = [];
		$scope.autoRenderedServicesForSaveAndUpdate=[];
		$scope.patientSponsorDetailsDtosListSplit = [];
		var data = {};
		
		var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/"+encounterId;
		BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				
				if(response.data.object!=null)
					{
						// alert();
						$scope.AddServiceBtn = false;
						$("#savedisablebtn").removeClass("disabled");
					}
				
				$scope.encounterDetailsFuture = response.data.object;
				$scope.encounterDetails = {
						encounterId : response.data.object.encounterId.toString(),
						encounterDateString : moment(response.data.object.encounterDateString,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()),
						encounterDateString2 : moment(response.data.object.encounterDateString2,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()),
						referralId : response.data.object.referralId,
						referralDesc : response.data.object.referralDesc,
						specialityName : response.data.object.specialityName,
						doctorName : response.data.object.doctorName,
						patientCategory : response.data.object.patientCategory,
						patientId : response.data.object.patientId,
						doctorId : response.data.object.doctorId,
						modalityId : response.data.object.modalityId,
						subSpecialityName : response.data.object.subSpecialityName,
						modalityDesc : response.data.object.modalityDesc,
						specialityId : response.data.object.specialityId,
						patientClassId : response.data.object.patientClassId,
						isTariffRateApplicable : response.data.object.isTariffRateApplicable,
						
				  }
				if(response.data.object.advisedByName != null){
					$scope.searchAdvised.advisedByName = response.data.object.advisedByName;
				}
				if(response.data.object.advisedById != null){
					$scope.searchAdvised.advisedById = response.data.object.advisedById;
				}
				
				$scope.getCashCollectedAmount($scope.encounterDetails.encounterId);
				$scope.isTariffRateApplicablePopup = $scope.encounterDetails.isTariffRateApplicable;
				$scope.getFutureOrders();
				$scope.getFreeTextOrders($scope.encounterDetails.encounterId);
				$scope.patientClassIdPopup = ($scope.encounterDetails.patientClassId > 0 ? $scope.encounterDetails.patientClassId : '');
				patientClassCancelReasonIdPopup = '';
				if($scope.encounterDetails.patientClassId > 0){
					
					$scope.patientClassdDescription = $.grep($scope.patientTypeList, function (mppr) {
                         return mppr.patientTypeId == $scope.encounterDetails.patientClassId;
                     })[0].description;
				}
				$scope.capExist = false;
				$scope.patientSponsorDetailsDtosListSplit = response.data.object.patientSponsorDetailsDtosList;
				angular.forEach(response.data.object.patientSponsorDetailsDtosList,function(value,index)
				 {
					
					if(value.capExist==true)
						$scope.capExist = true;
					
					if(value.priorityId == '1')
					{
						$scope.encounterDetails.paymentEntitlementDesc  = value.paymentEntitlementDesc;
						$scope.encounterDetails.companyDesc = value.companyDesc;
						$scope.encounterDetails.contractDesc = value.contractName;
						$scope.encounterDetails.validityFrom = value.validityFrom;
						$scope.encounterDetails.validityTo = value.validityTo;
						$scope.encounterDetails.payeeId = value.payeeId;
						$scope.encounterDetails.paymentEntitlementTypeId = value.paymentEntitlementId;
						
						if($scope.encounterDetails.payeeId != $scope.selfPayeeId)
							{
								$scope.contractId = value.contractId;
							}
						else
							{
								$scope.contractId = 0;
							}
					}
				});
				
				
				$scope.getAutoRenderedServicesByPatient(response.data.object);
				$scope.getAuthrizationDetails();
				$scope.getMedicationIssueListByEncounterId();
		
			}
		});
		
		$scope.getActivePackagesList();// yogesh
		$scope.packageDetailsList = [];
	}
	
	
	$scope.applyPatientClassPopup = function(){
		
		
		if(($('#patientClassIdPopup').val() == '' || $('#patientClassIdPopup').val() == null || $('#patientClassIdPopup').val() == undefined) && ($('#patientClassCancelReasonIdPopup').val() == '' || $('#patientClassCancelReasonIdPopup').val() == null || $('#patientClassCancelReasonIdPopup').val() == undefined))
			{
				

					growl.warning("Please select patient class and reason.",{
						title : WARNING_MSG
					});
					return false;
			
			}
		else if($('#patientClassIdPopup').val() == '' || $('#patientClassIdPopup').val() == null || $('#patientClassIdPopup').val() == undefined){
			
			growl.warning("Please select patient class.",{
				title : WARNING_MSG
			});
			return false;
		}else if($('#patientClassCancelReasonIdPopup').val() == '' || $('#patientClassCancelReasonIdPopup').val() == null || $('#patientClassCancelReasonIdPopup').val() == undefined){
			
			growl.warning("Please select reason.",{
				title : WARNING_MSG
			});
			return false;
			
		}else{
			$rootScope.startLoader();
			$scope.patientClassIdPopup = $('#patientClassIdPopup').val();
			$scope.patientClassCancelReasonIdPopup = $('#patientClassCancelReasonIdPopup').val();
			 $scope.encounterDetails.patientClassId =$scope.patientClassIdPopup;
		
			var obj={
					visitId:$scope.encounterDetails.encounterId,
					visitTypeId : $scope.visitTypeId,
					reasonIdForChangePatientClass : $scope.patientClassCancelReasonIdPopup,
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					patientClassId:$scope.encounterDetails.patientClassId
			}
			URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
					{	
						if(response.data.status == "success")
						{
							$rootScope.stopLoader();
							
							$scope.needToUpdatePharmacyService = true;
							
							angular.element('#change_patient_class').modal('hide');
							var i = 1;
				    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
								{
				    					
				    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(0);
				   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(0);
				   						    $scope.autoRenderedServicesFinalList[key].discount = service.discount;
				   						    
					    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
					    			       
					    			        $scope.autoRenderedServicesFinalList[key].discountPercentage = service.discountPercentage;
					    			        var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].discount);
					    			        
					    			    	var txpre = 0;
											if(service.taxPercentage != null || service.taxPercentage > 0){
												$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
												txpre = service.taxPercentage;
											}else{
													$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
													}
											$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
											$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
											$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
											$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
				    					
									    
										
										if(i == $scope.autoRenderedServicesFinalList.length){
											// $scope.saveOrderPackage();
											if($scope.requestStatusObject == null){
												$scope.saveOrderPackage();
												$scope.requestDiscountDiv = false;
												$scope.requestApporivedDiscountDiv = false;
												$scope.discountDisabledFlagForRequest = false;
												$scope.requestAppUser = 0;
												$scope.approvedAmtApp = 0;
												$scope.requestAmtApp =0;
												$scope.statusApp = "";
												$scope.discTypeDesc = "";
												$scope.discountRadio = 2; 
												
												$scope.billWiseDiscountObj = {
														// "discountGivenBy":"",
														"discountGivenBy":$scope.userId,
														"discountCategoryId" : "",
														"discountAmountBillWise" : "",
														"discountPercentageBillWise" : "",
														"approvedAmountBillWise":"",
														"requestUserId":"",
														"requestAmountBillWise" : 0,
												}
												 $scope.requestStatusObject  = null;
											}else{
														$scope.getCancelDiscountReqForDiscount();
														$scope.saveOrderPackage();
												
											}
										}
										i++;
								});
				    		growl.success(response.data.message);
						}
					});
			
		}
	}
	
	$scope.cancelPatientClassPopup = function(){
		
		$rootScope.startLoaderPopup();
		$scope.patientClassIdPopup = null;
		$scope.patientClassCancelReasonIdPopup = null;
		 $scope.encounterDetails.patientClassId =null;
	
		var obj={
				visitId:$scope.encounterDetails.encounterId,
				visitTypeId : $scope.visitTypeId,
				reasonIdForChangePatientClass : $scope.patientClassCancelReasonIdPopup,
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				patientClassId:null
		}
		var URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response)
				{	
					if(response.data.status == "success")
					{
						$rootScope.stopLoaderPopup();
						$scope.encounterDetails.patientClassId = null;
						angular.element('#change_patient_class').modal('hide');
						angular.element('#confirmCancelPatientClass').modal('hide');
						$scope.needToUpdatePharmacyService = true;
						var i = 1;
			    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
							{
			    					
	    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(0);
	   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(0);
	   						    $scope.autoRenderedServicesFinalList[key].discount = service.discount;
	   						    
		    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
		    			       
		    			        $scope.autoRenderedServicesFinalList[key].discountPercentage = service.discountPercentage;
		    			        var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[key].concession) + parseFloat($scope.autoRenderedServicesFinalList[key].discount));
		    			        
		    			    	var txpre = 0;
								if(service.taxPercentage != null || service.taxPercentage > 0){
									$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
									txpre = service.taxPercentage;
								}else{
										$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
										}
								$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
								$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
								$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
								$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
	    					
						    
							
							if(i == $scope.autoRenderedServicesFinalList.length){
								// $scope.saveOrderPackage();
								if($scope.requestStatusObject == null){
									$scope.saveOrderPackage();
									$scope.requestDiscountDiv = false;
									$scope.requestApporivedDiscountDiv = false;
									$scope.discountDisabledFlagForRequest = false;
									$scope.requestAppUser = 0;
									$scope.approvedAmtApp = 0;
									$scope.requestAmtApp =0;
									$scope.statusApp = "";
									$scope.discountRadio = 2; 
									$scope.discTypeDesc = "";
									
									$scope.billWiseDiscountObj = {
											// "discountGivenBy":"",
											"discountGivenBy":$scope.userId,
											"discountCategoryId" : "",
											"discountAmountBillWise" : "",
											"discountPercentageBillWise" : "",
											"approvedAmountBillWise":"",
											"requestUserId":"",
											"requestAmountBillWise" : 0,
									}
									 $scope.requestStatusObject  = null;
								}else{
									$scope.getCancelDiscountReqForDiscount();
									$scope.saveOrderPackage();
									
								}
							}
							i++;
						});

					} else {
                                     // Handle failure scenario
                                     $('#change_patient_class').modal('hide');
                                     $('#confirmCancelPatientClass').modal('hide');
                                     $rootScope.stopLoaderPopup(); // Stop loader in case of failure
                                 }
                                 })
                                 .catch(function (error) {
                                             console.error("Error in API call:", error);
                                             $('#change_patient_class').modal('hide');
                                             $('#confirmCancelPatientClass').modal('hide');
                                             $rootScope.stopLoaderPopup();
				});
	}
	
	$scope.cancelPatientClassClosePopup = function(){
		angular.element('#confirmCancelPatientClass').modal('hide');
	}
	
	$scope.closePopup = function()
	{
		$scope.setFirstTabLevel(1);
		// $scope.resetToActualValuesAfterClosePopup();
	}
	
	$scope.checkPackageAmountConsumption = function(){
		
				var pacAmount = 0;
				var allpackSerivceAmount = 0;
				angular.forEach($scope.listPackageOrder, function (value, key) {
					pacAmount = value.packagePrice;
					
					if( value.packageTypeId == 4 ){
						
						var param = {
								encounterId:$scope.encounterDetails.encounterId,
								pharmacyServiceId:$scope.pharmacyServiceId,
								packageId :  value.packageId 
							
						};//visit type IP default
						var URI = BASE_URL+ ROOT_URL
						+ "/api/billing/getAllInclusivePackageConsumeAmount";

						GenericService.serviceAction("POST", URI,param).then(function(response) {
								
							$rootScope.stopLoader();
									
									if(response.data.object != null){
										
										var amount = response.data.object.packageAmount;
										
										if( amount != null && amount > pacAmount  ){
											
											var sub = amount - pacAmount;
											$scope.subAmount =  parseFloat(sub).toFixed(2);
						                    growl.warning('Pharmacy bill amount getting exceeded with '+$scope.subAmount+' amount than package amount ',{
						                        title : 'Warning!'
						                    });
										}
									
									}
									
								});
						$rootScope.stopLoader();
					}
					
				
		    	});
				
					
				
				
				
	}
	
	$scope.getListPackageOrder = function() {
        if($scope.patientDetails !== undefined && $scope.patientDetails !== null){
            if($scope.patientDetails.patientId === undefined || $scope.patientDetails.patientId === null || $scope.patientDetails.patientId ===''){
                $scope.patientDetails.patientId = 0;
            }
        }
        else{
            $scope.patientDetails.patientId = 0;
        }
		var URI = BASE_URL+ ROOT_URL
				+ "/api/packages/getPackageListForIPDPackageOrder/"+$scope.patientDetails.patientId+"/"+ $scope.encounterDetails.encounterId
				+ "/" + $scope.organizationId + "/"+ $scope.unitId+"/1";
			$rootScope.startLoader();
		GenericService.serviceAction("GET", URI).then(function(response) {
							$rootScope.stopLoader();
							$scope.listPackageOrder = [];
// $scope.listPackageOrder = response.data.listObject;
							$scope.listPackageOrder = response.data.listObject !=null ? response.data.listObject : []  ;
							$rootScope.stopLoader();
							// console.log("ListPackageOrderresult",$scope.listPackageOrder);
							//$scope.checkPackageAmountConsumption();
						});
	}
    $scope.ServiceCount=0;
	$scope.getAutoRenderedServicesByPatient=function(encounterObj){
		
		$rootScope.startLoader();
		$rootScope.startLoaderPopup();
		$scope.autoRenderedServicesBillingDetails=[];
		$scope.setFirstTabLevel(1);
		var URI = null;
		
		if($scope.isPreOrPostBilling=='Y')
			URI = BASE_URL + ROOT_URL + "/api/billing/service/preBilling/orderedServices";
		else
			URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
		
		var obj={
				"visitTypeId":1,
				"encounterId":encounterObj.encounterId,
				"orgId":$scope.organizationId,
				"unitId":$scope.unitId,
		}
		// console.log("AUTORENDER",obj);
		// return false;
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
		{	
			if(response.data.status == "success")
			{
				var tempAutoRendServList = [];
				$scope.ServiceCount=0;
				angular.forEach(response.data.listObject,function(value,index){
					if(value.discount==undefined || value.discount==null || value.discount==0)
						{
						value.discountCategoryId=null;
						}
					value.packageMasterId = value.packageId;
					//ServiceCount
//					if(value.isPackageService == 'N')
//					{
						$scope.ServiceCount=$scope.ServiceCount + value.quantity;
//					}
                    if($scope.tariffList.length>0)
                    {
                        value.tariffList=[];
                        angular.forEach($scope.tariffList, function (value1, key) {
                            value.tariffList.push(value1);
                        });
                    }
					tempAutoRendServList.push(value);
					if(value.homeCollection==true)
						$scope.homeCollection = 'Y';
					
//					 $scope.autoRenderedServicesFinalList[index].doctorForServiceList=value.doctorList;
				});
				
				$scope.homeCollection = $scope.homeCollection!=null ? $scope.homeCollection : 'N';
				
				$scope.autoRenderedServicesList = tempAutoRendServList;
				// console.log("$scope.autoRenderedServicesList",$scope.autoRenderedServicesList);
				var indexT = response.data.listObject.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
				
			    if (indexT >= 0) {
			    	$scope.isPatientClass = "Y";
			    }
			   
				  $scope.calculationTest($scope.autoRenderedServicesList,init);
				  
				  if($scope.applyAuth==true)
						 $scope.showSplitBill();
				  $scope.applyAuth = false;
				  // Ganesh Package
				  angular.element('#addServicePopup').modal('hide');
				  angular.element('#serviceCancellationModal').modal('hide');
				  angular.element('#packageForceAdd').modal('hide');
				  $rootScope.stopLoader();
				  $rootScope.stopLoaderPopup();
			}
			else if(response.data.status == "error")
				{
				 $rootScope.stopLoader();
				 $rootScope.stopLoaderPopup();
				}
		});
		$scope.getListPackageOrder();
	}
	
	$scope.closeSplitBill = function()
	{
		$scope.applyAuth==false;
		$scope.autoRenderedServicesFinalList = $scope.serviceListOLD;
	}
	
	
	 $scope.createBillingDetailsObj = function(item){
	    	var obj = {
	    		"billingDetailsId"	:"",
	    		"billingMasterId"	:"",
	    		"serviceId"			:item.serviceMasterId,
	    		"rate"				:"",
	    		"quantity"			:item.quantity,
	    		"concession"		:item.concession,
	    		"concessionAmount"	:"",
	    		"grossAmount"		:(item.basePrice * item.quantity) - item.concession,
	    		"coPayPercentage"	:"",
	    		"unitId"			:$scope.unitId,
	    		"organizationId"	:$scope.organizationId,
	    		"status"			:"A",
	    		"createdBy"			:$scope.userId,
	    		"updatedBy"			:$scope.userId,
	    		"createdDate"		:new Date().getTime(),
	    		"updatedDate"		:new Date().getTime(),
	    		"serviceMasterId"	:item.serviceMasterId,
		    	"serviceStandardName":item.serviceStandardName,
		    	"serviceStandardCode":item.serviceStandardCode,
		    	"specialityId"		:item.specialityId,
		    	"specialityName"	:item.specialityName,
		    	"subSpecialityId"	:item.subSpecialityId,
		    	"subSpecialityName"	:item.subSpecialityName,
		    	"basePrice"			:item.basePrice,
		    	"discount"			:(item.discount == undefined || item.discount == '') ? '0' : item.discount,
		    	"discountPercentage":item.discountPercentage, // direct
																// discount
																// percentage
		    	"discountAmount"	:item.discountAmount, // direct discount
															// amount
		    	"orderDetailsId"	:item.orderDetailsId
	    	}
	    	return obj;
	    }
	
	
	/*
	 * $scope.searchServices = function(searchKeyword){ $scope.servicesList=[];
	 * searchKeyword=searchKeyword+"---"+$scope.patientDetails.genderId; var
	 * serData = { organizationId : $scope.organizationId, unitId :
	 * $scope.unitId, searchKeyword : searchKeyword, defaultSelfTariffId :
	 * $scope.defaultSelfTariffId, paymentEntitlementTypeId :
	 * $scope.encounterDetails.paymentEntitlementTypeId, visitTypeId : 1 ,
	 * billingBedCategoryId : $scope.opdBillingCatId, patientCategoryId :
	 * $scope.patientDetails.patientTypeId, encounterId :
	 * $scope.encounterDetails.encounterId }
	 * 
	 * 
	 * var URI = BASE_URL + ROOT_URL + "/api/unit/tariff/serviceMaster"; return
	 * BillingGenericService.serviceAction(METHOD_POST,URI, serData,
	 * NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	 * if(response.data.status == "success"){ $scope.servicesList =
	 * response.data.listObject; } return $scope.servicesList; }); }
	 */
	 
	 $scope.searchServices = function(searchKeyword){
			$scope.servicesList=[];
			$scope.selectedServices=[];
			searchKeyword=searchKeyword+"---"+$scope.patientDetails.genderId;
			var serData = {
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					searchKeyword : searchKeyword
			}
			// var URI = BASE_URL + ROOT_URL +
			// "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
			var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
			return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.servicesList = response.data.listObject;
				}
				return $scope.servicesList;
			});
		}


	
	
	
    var isExists;
    function checkExistsOrNot(arr, val) {
     return arr
       .some(function(arr) {
        return (val.serviceMasterId === arr.serviceMasterId)
       });
    }
    
	   $scope.checkServiceWiseDoctorsList = function(info, idx){
		   
		   var docListObj={
				   serviceId : info.serviceMasterId,
				   serviceDepartmentId : info.serviceSpecialityId,
			   		unitId : $scope.unitId,
			   		organizationId : $scope.organizationId,
			   		billingBedCategoryId : $scope.opdBillingCatId,
			   		visitTypeId : $scope.visitTypeId,
			   		patientTypeId : $scope.patientDetails.patientTypeId,
			   		paymentEntitlementId:$scope.encounterDetails.paymentEntitlementTypeId
			   }
			   var URI = BASE_URL + ROOT_URL + "/adt/getDoctorShareWiseDocList";// for
			   																	// HIS-15355

			   GenericService
			   .serviceAction("POST", URI, docListObj)
			   .then(
			   		function(response) {
			   		
			   			$scope.doctorsListNew = [];
			   			if (response.data.status == "success") {
			   				$scope.doctorsListNew = response.data.listObject;
			   				angular.forEach($scope.autoRenderedServicesFinalList, function(value, key){
			   				   if(key == idx){
			   					value.docList = $scope.doctorsListNew;
			   				   }
			   			   });
			   			}
			   		});
	   }
	   
      $scope.getNewDoctorList=function(){
    	  var obj={
    			  unitId : $scope.unitId,
			   		organizationId : $scope.organizationId,
    	  }
		    	  var URI = BASE_URL + ROOT_URL + "/adt/getDoctorListForOPDBilling";// for
					// HIS-15355
		
		GenericService
		.serviceAction("POST", URI, obj)
		.then(
		function(response) {
		$scope.doctorsList = [];
		
		if (response.data.status == "success") {
		$scope.doctorsList = response.data.listObject;
		
		}
		});
      }
   
      $scope.closePop = function(){
		   $('#packageAmountexceededOPD').modal('hide');
	   }
	   $scope.subAmount = 0;
    $scope.calculationTest = function(autoRenderedServicesList,typeId)
    {
    	// console.log("AUTO",autoRenderedServicesList);
    	$scope.AutoRenderedTotalAmountSummary = 0;
		$scope.AutoRenderedTotalConcessionSummary = 0;
		$scope.AutoRenderednetPayableSummary = 0;
		$scope.AutoRenderednetTotalDiscountSummary=0;
		$scope.AutoRenderednetTotalTax=0;
		$scope.AutoRenderednetTotalDiscount=0;
		$scope.AutoRenderednetTotalCreditPayble=0;
		$scope.AutoRenderednetTotalSelfPayble=0;
		$scope.AutoRenderednetTotalPackagePayble=0;
		$scope.totalSelfPaybleTemp = 0;
		$scope.totalPackagePaybleTemp = 0;
		$scope.roundOffAmt = 0;
		$scope.totalDisServiceWise = 0;
		$scope.autoRenderedServicesFinalList = [];
        var mainCount = 1;
        var serviceCount = autoRenderedServicesList.length;

        if(autoRenderedServicesList.length == 0){
            $scope.getDisRiqFlag = false;
            $scope.requestStatusObject = null;
       }
        
       
       
		angular.forEach(autoRenderedServicesList, function (value, key) 
				{
				var newArr = {};
				angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
					{
							
				    	    // console.log(value.basePrice, "#",
							// value.quantity);
							if(value1.serviceMasterId != null){
								$scope.checkServiceWiseDoctorsList(value1,key);
							}
					 		
							value1.serviceAmount = parseFloat(value1.serviceAmount).toFixed(2);
							newArr.orderDetailsId = value.orderDetailsId > 0 ? value.orderDetailsId : null;
							newArr.tariffId = value.tariffId > 0 ? value.tariffId : $scope.defaultSelfTariffId;
							newArr.isInvestigationOrder = (value.isInvestigationOrder=='Y')?'Y':'N';
							newArr.orderId = value.orderId > 0 ? value.orderId : null;
							newArr.orderDate = value.orderDate != null ? value.orderDate : '';
							newArr.orderNo = value.orderNo != null ? value.orderNo : '';
							newArr.bedCategoryId = value.bedCategoryId;
							newArr.isFromServiceSerach = value.isFromServiceSerach!= null ? value.isFromServiceSerach : 'N';
							if(value.isDoctorRequired == null || value.isDoctorRequired == undefined)
							{
							   newArr.isDoctorRequired  = 'N';
							}
							else
							{
							  newArr.isDoctorRequired  = value.isDoctorRequired;
							}
                            newArr.docSharePer = value.docSharePer !=null ? parseFloat(value.docSharePer).toFixed(2) : 0;
                            newArr.hospitalShare = value.hospitalShare !=null ? parseFloat(value.hospitalShare).toFixed(2) : 0;
                            newArr.refDocSharePer = value.refDocSharePer !=null ? parseFloat(value.refDocSharePer).toFixed(2) : 0;
							newArr.discountOwnership = value.discountOwnership > 0 ? value.discountOwnership : 0;
							newArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
							newArr.isConsultationService = value.isConsultationService;
							newArr.ordRemarks = value.ordRemarks;
							newArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
							newArr.dummyServiceAmt = value.dummyServiceAmt;
							newArr.priorityId  = value.priorityId;
							newArr.isOutsourced = value.isOutsourced;
						    newArr.specialityId = value.specialityId;
							newArr.taxId = value.taxId;
							newArr.ordCancelled = value.ordCancelled != null ? value.ordCancelled : 'N';
							newArr.ordCancelReasonId = value.ordCancelReasonId != null ? value.ordCancelReasonId  : '';
							newArr.ordCancelDatetime = value.ordCancelDatetime != null ? value.ordCancelDatetime  : '';
							newArr.ordCancelRemark = value.ordCancelRemark != null ? value.ordCancelRemark  : '';
							newArr.patientClassPercentage = value.patientClassPercentage != null ? value.patientClassPercentage  : null;
			        		newArr.markupStatus =value.markupStatus != null ? value.markupStatus  : false;
							newArr.status  = value.status;
							newArr.serviceRendered = value.serviceRendered != null ? value.serviceRendered  : '0';
							newArr.oldOrdDtlId = value.oldOrdDtlId != null ? value.oldOrdDtlId  : null;
							newArr.isRateEditable = (value.isRateEditable == 'Y')?'Y':'N';	
							newArr.isRateEditable = ((value.packageConsumptionAmt>0 && newArr.isRateEditable=='Y') || value.serviceMasterId==$scope.pharmacyServiceId )?'N':value.isRateEditable;	
							// newArr.isRateEditable = value.isPackage ==
							// true?'N':newArr.isRateEditable;
							newArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
							newArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
							newArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
							newArr.doctorSpecialityId=value.doctorSpecialityId; 
							// console.log("docSpe",value.doctorSpecialityId );
							// console.log("spe",value.specialityId);
							newArr.isRefDoctorShare = value.isRefDoctorShare != null ? value.isRefDoctorShare  : null;
							newArr.contractId = ((value.contractId != null || value.contractId > 0) ? value.contractId  : null);
							newArr.doctorId = value.doctorId==null?0:value.doctorId;
						    newArr.serviceSpecialityId = value.serviceSpecialityId;
							newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
							newArr.isAutoRender =  value.isAutoRender;
							newArr.serviceRendered = value.serviceRendered;
							newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
							newArr.discountDetailsList = value.discountDetailsList;
							newArr.serviceMasterId = value.serviceMasterId;
							newArr.serviceStandardCode = value.serviceStandardCode;
							newArr.serviceStandardName = value.serviceStandardName;
							newArr.orderDateString = value.orderDateString;
							newArr.orderDateTimeString = value.orderDateTimeString;
							newArr.orderDateTime  = value.orderDateTime;
							newArr.totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
							newArr.isDiscount = value.isDiscountApplicable;
							newArr.medallServiceId = value.medallServiceId != null ? value.medallServiceId : '';
						    newArr.oldNetPay = value.oldNetPay !== undefined && !isNaN(value.oldNetPay) ? value.oldNetPay : newArr.totalAmt;

							newArr.isDiscountApplicable = (value.packageConsumptionAmt != 0) ? 'N':value.isDiscountApplicable;// isDiscountApplicable
                            if(value.serviceMasterId == $scope.surchargeServiceId)
                            {
                                newArr.isDiscountApplicable = 'N';
                            }
							if(value.packageConsumptionAmt>0 || value.serviceMasterId==$scope.pharmacyServiceId)
							{
								// newArr.isDiscountApplicable = 'N';
								newArr.isRateEditable = 'N';
								newArr.isQuantityEditable = 'N';
							}else{
								// newArr.isDiscountApplicable =
								// newArr.isDiscountApplicable;
								newArr.isRateEditable = newArr.isRateEditable;
								newArr.isQuantityEditable = value.isQuantityEditable;
							}
							newArr.isTariffRateApplicable = value.isTariffRateApplicable;// 12-10-2019
							newArr.minRateEditable = value.minRateEditable;
							newArr.maxRateEditable = value.maxRateEditable;
							newArr.specialityName = value.specialityName;
							newArr.doctorName = value.doctorName;
							newArr.discountDisabledFlag = 'N';
							newArr.isPackage = value.isPackage;
							newArr.packageMasterId = value.packageMasterId;
							newArr.packageTypeId = value.packageTypeId;
							newArr.isPackageService = value.isPackageService;
							newArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
							newArr.isCapService = value.isCapService;
			    			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
			    			newArr.doctorSpecialityId !=undefined && newArr.doctorSpecialityId >0 ? newArr.docSpecialityList=$scope.specialityMasterList : newArr.docSpecialityList = [];
			    			
			    			if(value.doctorSpecialityList == undefined && newArr.doctorSpecialityId >0){
			    				var specialityLsits=[];
								angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
										{
											if(service.doctorId==value.doctorId)
												{
												specialityLsits.push(service);
												}
										});
								newArr.doctorSpecialityList=[];
			    			} 
			    			
							
			    			newArr.doctorSpecialityList = (value.doctorSpecialityList != undefined ? value.doctorSpecialityList : specialityLsits);
			    			newArr.docList=value.docList != undefined ? value.docList:$scope.doctorsList;
			    			newArr.isFromAddservice = (value.isFromAddservice != null || value.isFromAddservice != '' || value.isFromAddservice != undefined ? value.isFromAddservice :'N') ;
			    			if(value.subSpecialityId==undefined||value.subSpecialityId==null)
			    				{
			    				  newArr.subSpecialityId = value.serviceRenderingDeptId;
			    				}
			    			else{
			    			     newArr.subSpecialityId = value.subSpecialityId;
			    			    }
			    			newArr.packageId = value.packageId;
							if(value.specialityName == null)
							{
								newArr.specialityName = "";
								newArr.doctorName = "";
							}
							newArr.quantity = value.quantity;
							newArr.basePrice = parseFloat(value.basePrice).toFixed(2);
							newArr.basePriceRateChange = parseFloat(value.basePrice).toFixed(2);
							if(parseFloat(value.packageConsumptionAmt) > 0 && value1.isPackageService == 'N'){
								newArr.basePriceRateChange = parseFloat(value1.serviceAmount).toFixed(2);
							}
							newArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ? parseFloat(value.coPayPer).toFixed(2) : "0";
							newArr.basePriceOld = value.basePrice;
							newArr.oldBasePrice = value.oldBasePrice;
							newArr.concession = parseFloat(value.concession).toFixed(2);
							newArr.patientClassPercentage = parseFloat(value.patientClassPercentage).toFixed(2);
							newArr.patientClassPercentageChange = parseFloat(value.patientClassPercentage).toFixed(2);
							newArr.discountCategoryId = value.discountCategoryId != null ? value.discountCategoryId.toString()  : null;
							newArr.discountPercentage = value.discountPercentage;
							newArr.discount = value.discount;
							newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
							
							newArr.packageApplicableDate = value.packageApplicableDate;
							
							var dis = parseFloat(value.discount).toFixed(2);
							var disPer =value.discountPercentage;
							if(value.discount == null || value.discount == 0 || value.discount == '')
								{
									newArr.discountPercentage = 0;
									newArr.discount = 0;
									dis = 0;
									disPer = 0;
									newArr.discountDisabledFlag = 'Y';
								}
								
							if(value.discount != 0 && (value.discountPercentage == null || value.discountPercentage == 0 || value.discountPercentage == '')){
								newArr.discountPercentage = isNaN(((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2);
								}
							
								if(value.discountDetailsList == null)
								{
									value.discountDetailsList = [];
									newArr.discountDetailsList = [];
								}
							
							newArr.taxName = value.taxName;
							var txpre = 0;
							if(value.taxPercentage != null || value.taxPercentage > 0){
								newArr.taxPercentage = value.taxPercentage;
								txpre = value.taxPercentage;
							}
							else
								{
								 newArr.taxPercentage = 0;
								}
							
							
							var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
							 totalAmt  = parseFloat(totalAmt) - value.packageConsumptionAmt;
							// var concession = (value.packageConsumptionAmt==0
							// ?parseFloat(value.concession).toFixed(2):
							// parseFloat(0));
							var concession = (parseFloat(value.concession).toFixed(2));
		                    if(newArr.isDiscountApplicable == 'Y' && value1.isPackageService == 'N' && value.status == 'A')
		                    	{
		                    		$scope.totalDisServiceWise  = parseFloat($scope.totalDisServiceWise) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)));
		                    	}
		                    
							var withConDic =parseFloat(totalAmt - (parseFloat(concession) + parseFloat(dis))).toFixed(2);
							newArr.taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							var gstAmt =  parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							newArr.taxAmtOld = value.taxAmtOld;
							newArr.selfPayableOldC = value.selfPayableOldC;
							newArr.creditPayableOldC = value.creditPayableOldC;
							newArr.netPay =   parseFloat(value.netPay).toFixed(2);
							newArr.ordTotalAmount =   parseFloat(value.totalAmt).toFixed(2);
							var selfPayableV =   "0";
							var creditPayableV =   "0";
							
								if(value.payeeId == $scope.selfPayeeId || value.payeeId == null )
									{
									      newArr.selfPayable =parseFloat(value.selfPayable).toFixed(2);;
									      newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);;
									      selfPayableV =  parseFloat(newArr.selfPayable).toFixed(2);
									      creditPayableV =  parseFloat(newArr.creditPayable).toFixed(2);
									     }
								
								else
									{
									newArr.payeeId= value.payeeId;
									    if(value.selfPayable>0)
										{
									    	
									    	if(parseInt(dis) > 0 )
									    		{
									    		newArr.selfPayable =   parseFloat(value.selfPayable).toFixed(2);
									            selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
									    		}
									    	else
									    		{
									    		 newArr.selfPayable =parseFloat(value.selfPayableOldC).toFixed(2);
												 selfPayableV =   parseFloat(value.selfPayableOldC).toFixed(2);
									    		}
									    
										
										newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);
										creditPayableV=parseFloat(value.creditPayable).toFixed(2);
										}
									else{
										newArr.selfPayable = (value.selfPayable==null||value.selfPayable==undefined||value.selfPayable=="")?0:value.selfPayable;
										selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
									   
									    newArr.creditPayable  = value.creditPayable;
									    creditPayableV = parseFloat(value.creditPayable).toFixed(2);
									    
									   }
									
									}
							
							
					
								
										if($scope.surchargeServiceId !=  value.serviceMasterId){
											   if(Object.keys(value.orderDetailsPayeeMapperDtosList).length > 0)
										    	 {
										    		angular.forEach(value.orderDetailsPayeeMapperDtosList, function (value1, key) {
														
										    			 if(value1.payeeId == $scope.selfPayeeId)
													       {
										    				 value1.payeeAmount =  newArr.selfPayable;
										    				 value1.selfPayable =  newArr.selfPayable;
										    				 value1.creditPayable = 0;
										    			
													       }
										    			 else{
										    				 if($scope.isSplitBillApplied)
										    					 {
										    					  value1.creditPayable =  value1.creditPayable;
											    				  value1.selfPayable =  value1.selfPayable;
										    					 }
										    				 else{
										    				     value1.creditPayable =  value1.payeeAmount;
										    				     value1.selfPayable =  0;
										    				 }
										    			 }
														
													});
										    	   }
												}
							newArr.saleId = value.saleId;
						    newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
						    newArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
						    newArr.isPeriodicityService = value.periodicityServiceOrderId!=null ? 'Y' : 'N' ;
						   
						    // package force add
						    newArr.forceAddRemoved = value.forceAddRemoved;
						    newArr.isConsumed = value.isConsumed;
						    newArr.oldDiscount = value.discount;
						    var newPatAmt = parseFloat(parseFloat(newArr.netPay)).toFixed(2);
							if(value1.isPackageService=='N' && value.status=='A')

								{
									// $scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)
									// + parseFloat(value.quantity) *
									// ($scope.pharmacyServiceId ==
									// value.serviceMasterId ?
									// parseFloat(value.basePrice)
									// :parseFloat(value1.serviceAmount))).toFixed(2);
									
									if(	$scope.ambulanceChargesServiceId != undefined && $scope.ambulanceChargesServiceId != null  && $scope.ambulanceChargesServiceId  == value1.serviceId){
										$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.netPay)).toFixed(2);
									}else{
										$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
									}
									$scope.AutoRenderedTotalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary) + (parseFloat(value.concession))).toFixed(2);
									$scope.AutoRenderednetPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary) + parseFloat(newPatAmt)).toFixed(2);
									$scope.AutoRenderednetTotalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscountSummary) + parseFloat(0)).toFixed(2);
									// $scope.AutoRenderednetTotalTax =
									// (parseFloat($scope.AutoRenderednetTotalTax)
									// +
									// (value.packageConsumptionAmt==0?parseFloat(gstAmt):0)).toFixed(2);
									$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) +  (parseFloat(gstAmt))).toFixed(2);
									$scope.AutoRenderednetTotalDiscount = (parseFloat($scope.AutoRenderednetTotalDiscount) + parseFloat(dis)).toFixed(2);
									$scope.AutoRenderednetTotalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble) + parseFloat(creditPayableV)).toFixed(2);
									$scope.AutoRenderednetTotalSelfPayble= (parseFloat($scope.AutoRenderednetTotalSelfPayble)+ parseFloat(selfPayableV)).toFixed(2);
									$scope.AutoRenderednetTotalPackagePayble= (parseFloat($scope.AutoRenderednetTotalPackagePayble)+ parseFloat(value.packageConsumptionAmt)).toFixed(2);
								}
							newArr.createdBy = value.createdBy;
							newArr.createdDate = value.createdDate;
							newArr.medallServiceId = value.medallServiceId != null ? value.medallServiceId : '';

							newArr.discountRateChange = parseFloat(newArr.discount).toFixed(2);
							newArr.discountPercentageRateChange = parseFloat(newArr.discountPercentage).toFixed(2);
							newArr.conccesionPriceRateChange = parseFloat(newArr.concession).toFixed(2);
							newArr.patClsPerPriceRateChange = parseFloat(newArr.patientClassPercentage).toFixed(2);
							newArr.userName = value.userName;
							newArr.createdBy = value.createdBy;
							newArr.hospitalShareNew=parseFloat(newArr.netPay-(parseFloat(newArr.docSharePer)+parseFloat(newArr.refDocSharePer)+parseFloat(newArr.taxAmt))).toFixed(2);
							newArr.doctorForServiceList=value.doctorList != null ? value.doctorList :value.doctorForServiceList;
					 });// /end service loop
					newArr.patientCouponTemplateMapperId = value.patientCouponTemplateMapperId;
					newArr.tariffList=value.tariffList;
					newArr.oldTariffId=newArr.tariffId;
                    newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
				// chk blank object by dinesh start
                	if(!angular.equals({}, newArr) ) {
                		 $scope.autoRenderedServicesFinalList.push(newArr);
                     }
// $scope.autoRenderedServicesFinalList.push(newArr); // before
                	// chk blank object by dinesh end
			       // Start Code For Reject Discount
                   if($scope.getDisRiqFlag == true && autoRenderedServicesList.length == mainCount)
                   {
                       $scope.getDisRiqFlag = false;
                       $scope.requestStatusObject = {};
                       var data1 = {
                               encounterId : $scope.encounterDetails.encounterId,
                               requestType : 1
                       }
                       var URI1 = BASE_URL + ROOT_URL + "/api/billing/getDiscReqDtlsForOPBilling";
               		BillingGenericService.serviceAction(METHOD_POST,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
               			if(response.data.status == "success"){
               				if(response.data.listObject.length > 0){
               				$scope.requestStatusObject = response.data.listObject[0];
               				if($scope.requestStatusObject.reqStatus == 2)
               					{
               					$scope.requestApporivedDiscountDiv = true;
               					$scope.discountDisabledFlagForRequest = true;
               					$scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
               					$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
               					$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
               					$scope.requestAmtRemark =$scope.requestStatusObject.remark;
               					$scope.statusApp = "Approved.";
               					$scope.discTypeDesc = $scope.requestStatusObject.discTypeDesc;
               					$scope.discountRadio = 2;
               						
               					var totalDisPercentage =((parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100).toFixed(2);
               		    		if(totalDisPercentage > 0){
               		    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
               		    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
               		    		// 12-10-2019
               		    		$scope.billWiseDiscountObj.discountPercentageBillWiseOld = (parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100;
               		    		$scope.billWiseDiscountObj.discountCategoryId =  $scope.requestStatusObject.discType;
               		    	  if($scope.billWiseDiscountObj.discountAmountBillWise > 0 && $scope.totalDiscountSummary > 0){
               				     $scope.roundOffAmt = parseFloat($scope.totalDiscountSummary-$scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
               				   }
               		    		// 12-10-2019
               		    		}
               		    		}
               				else if($scope.requestStatusObject.reqStatus == 1)
               					{
               					    $scope.discountDisabledFlagForRequest = true;
               					    $scope.requestApporivedDiscountDiv = true;
               					    $scope.requestAppUser = '-';
               						$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
               						$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
               						$scope.requestAmtRemark =$scope.requestStatusObject.remark;
               						$scope.statusApp = "Pending.";
               						$scope.discountRadio = 2;
               						$scope.discTypeDesc = $scope.requestStatusObject.discTypeDesc;
               						$scope.finalBill = 'N';
               					}
               				else if($scope.requestStatusObject.reqStatus == 3)
               					{
               					  // $scope.requestStatusObject = null;
                                $scope.requestStatusObject = null;
                                var reject =0;
                               /*
								 * angular.forEach($scope.autoRenderedServicesFinalList,
								 * function (value, key) { if(value.status ==
								 * 'A'){
								 * 
								 * if(parseFloat(value.discount) > 0){
								 * 
								 * $scope.autoRenderedServicesFinalList[reject].concession =
								 * parseFloat(0);
								 * $scope.autoRenderedServicesFinalList[reject].patientClassPercentage =
								 * parseFloat(0);
								 * 
								 * var totalAmt = (parseFloat(value.quantity) *
								 * parseFloat(value.basePrice)).toFixed(2);
								 * $scope.autoRenderedServicesFinalList[reject].discountPercentage =
								 * 0;
								 * $scope.autoRenderedServicesFinalList[reject].discount =
								 * parseFloat(0).toFixed(2); var withConDic
								 * =parseFloat(totalAmt) -
								 * parseFloat($scope.autoRenderedServicesFinalList[reject].discount);
								 * 
								 * var txpre = 0; if(value.taxPercentage != null ||
								 * value.taxPercentage > 0){
								 * $scope.autoRenderedServicesFinalList[reject].taxPercentage =
								 * value.taxPercentage; txpre =
								 * value.taxPercentage; }else{
								 * $scope.autoRenderedServicesFinalList[reject].taxPercentage =
								 * 0; }
								 * $scope.autoRenderedServicesFinalList[reject].taxAmt =
								 * parseFloat((parseFloat(withConDic) *
								 * txpre)/100).toFixed(2);
								 * $scope.autoRenderedServicesFinalList[reject].netPay =
								 * (parseFloat(withConDic) +
								 * parseFloat($scope.autoRenderedServicesFinalList[reject].taxAmt)).toFixed(2);
								 * $scope.autoRenderedServicesFinalList[reject].selfPayable =
								 * $scope.autoRenderedServicesFinalList[reject].netPay;
								 * $scope.autoRenderedServicesFinalList[reject].creditPayable
								 * =parseFloat(0);
								 *  } } if(reject + 1 ==
								 * $scope.autoRenderedServicesFinalList.length){
								 * $scope.finalCancelDiscount(); }
								 * 
								 * reject++; });
								 */
                                	$scope.finalCancelDiscount();
               					}
               				else{
               				 $scope.requestStatusObject = null;
               			   $timeout(function () {
 								
								  var discountPresent =false;
								  angular.forEach($scope.autoRenderedServicesList,function(value,index){
								    	if(value.discount != undefined && value.discount != '' && value.discount != '.' ){
								    		discountPresent =true;
									     }
								  });
								  if(discountPresent == true ){
									  var lengthofSaved  = $scope.autoRenderedServicesList.length;
									  var objectForDiscount  = {};
									  objectForDiscount  = $scope.autoRenderedServicesList[lengthofSaved-1];
									  $scope.getDiscountCal(objectForDiscount,lengthofSaved-1);
								  }
								  $rootScope.stopLoader();
							}, 1000);
               				}
               				
               				// New added For Disc Start
               				$scope.totalSumDisAmountTemp = parseFloat(0);
               				if($scope.requestStatusObject != null){
                   			var i = 1;
                   			angular.forEach($scope.requestStatusObject.requestDiscountDetailsDtosList,function(value,index)
               						 {
               					        
               					        	   $scope.totalSumDisAmountTemp =$scope.totalSumDisAmountTemp + value.netAmount;
               					        	
               					       
               							i++;	
               						 });
               				}
                   			// New added For Disc End
               				}
               				
               				else{
               					$scope.requestStatusObject = null;
               					
               					
		               				   $timeout(function () {
		      								
		  								  var discountPresent =false;
		  								  angular.forEach($scope.autoRenderedServicesList,function(value,index){
		  								    	if(value.discount != undefined && value.discount != '' && value.discount != '.' && value.pharmacyDiscountOverride != 0){
		  								    		discountPresent =true;
		  									     }
		  								  });
		  								  if(discountPresent == true ){
		  									  var lengthofSaved  = $scope.autoRenderedServicesList.length;
		  									  var objectForDiscount  = {};
		  									  objectForDiscount  = $scope.autoRenderedServicesList[lengthofSaved-1];
		  									  $scope.getDiscountCalOnloadForWarningMsg(objectForDiscount,lengthofSaved-1);
		  								  }
		  								  $rootScope.stopLoader();
		  							}, 1000);
               					}
               				}
               		});
                   }
			       // End Code For Reject Discount
                   mainCount++;
				});// end package loop
		
		  
			$scope.totalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)).toFixed(2);
			$scope.totalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary)).toFixed(2);
			$scope.netPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary)).toFixed(2);
			$scope.totalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscount)).toFixed(2);
			$scope.totalGstTaxSummary=(parseFloat($scope.AutoRenderednetTotalTax)).toFixed(2);
			

			if($scope.updatePayee){
				$scope.billWiseDiscountObj.discountAmountBillWise = $scope.AutoRenderednetTotalDiscount;
			}
			$scope.updatePayee=false;
			
			var amt =   isNaN(Number(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)))?0:$scope.billWiseDiscountObj.discountAmountBillWise;
			if(amt>0 && $scope.AutoRenderednetTotalDiscount>0 && $scope.isBillWiseDiscountApplied == true){
			  $scope.roundOffAmt = parseFloat(amt-$scope.AutoRenderednetTotalDiscount).toFixed(2);
			}	
			
			$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);
			$scope.totalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
			$scope.totalSelfPaybleTemp=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
			$scope.totalPackagePaybleTemp=(parseFloat($scope.AutoRenderednetTotalPackagePayble)).toFixed(2);
			
            if(serviceCount == $scope.actualServiceCount && $scope.roundOffAmt!=0)
            {
                if($scope.totalSelfPayble!=0)
                {
                    $scope.totalSelfPayble = parseFloat(Number(parseFloat($scope.totalSelfPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                    $scope.totalSelfPaybleTemp = parseFloat(Number(parseFloat($scope.totalSelfPaybleTemp).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                }else{
                    $scope.totalCreditPayble = parseFloat(Number(parseFloat($scope.totalCreditPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                }
                $scope.netPayableSummary = parseFloat(Number(parseFloat($scope.netPayableSummary).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
            }else{
                $scope.roundOffAmt=0;
            }
            
            $scope.actualServiceCount = $scope.autoRenderedServicesFinalList.length;
			
			$timeout(function () {
				
				 if($scope.listPackageOrder.length != undefined && $scope.listPackageOrder.length != null && $scope.listPackageOrder.length >0 ){
			        	$scope.checkPackageAmountConsumption();
			        }
				
			}, 6000);
			
			
		    return 1;
    }
    

    $scope.getQytCal = function(itemObj,idx)
    {
    	
    	$scope.isFinancialClearence ='N';
    	$scope.financialRequestSendTo = "";

    	// console.log("OBJ",itemObj);
    	if(itemObj.quantity == '' || itemObj.quantity == undefined || itemObj.quantity == 0 || itemObj.quantity == '.')
		{
    		if(itemObj.quantity == 0)
			{
    			growl.warning("Quantity cannot be zero..!!",{
    				title : WARNING_MSG
    			});
			}
    		itemObj.quantity = 1;
		}
    	
		if($scope.requestStatusObject != null)
		{
			  $scope.getCancelDiscountReqForDiscount();
			
		}
		itemObj.quantity = itemObj.quantity.replace(".", "");
		//ServiceCount
		$scope.ServiceCount=0;
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,index){
//			if(value.isPackageService == 'N')
//			{
				$scope.ServiceCount=$scope.ServiceCount + parseInt(value.quantity);
//			}
			});
		
		
		if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
			
			 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
					title : ERROR_MSG
				});
			
			var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
	    	var indexList = [];
	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
				var j = 0;
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
						{
							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
							{
		    					var totalAmt = 0;
		    					var dis = 0;
		    					var gstAmt = 0;
		    					var withConDic = 0;
		    	    		
		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
		    						$scope.roundOffAmt = 0.00;
		    						
		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
		    						
		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
		
		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
		    			    		indexList.push(j);
							}	
	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
	    			{
	    	            $scope.addOrUpdateServices(null,indexList);
	    			}
					
	    		j++;
				
			});
	    
			
		
			
			
		}else{
			

			$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
			$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
			
			$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
			
			$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);
			
			var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			 $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);
	      
	        var withConDic =parseFloat(totalAmt) - (parseFloat(itemObj.concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
	        
	    	var txpre = 0;
			if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
				$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
				txpre = itemObj.taxPercentage;
			}else{
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
					}
			$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
			$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
			
	        	$scope.autoRenderedServicesForSaveAndUpdate=[];
	        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
	        	$scope.addOrUpdateServices(idx,null);
	      
			$scope.billWiseDiscountObj = 
			{
				"discountGivenBy":$scope.userId,
				"discountCategoryId" : "",
				"discountAmountBillWise" : "",
				"discountPercentageBillWise" : "",
				"approvedAmountBillWise":"",
				"requestUserId":"",
				"requestAmountBillWise" : 0,
			}
			$scope.requestDiscountDiv = false;
			
		}
		
     }
    
    $scope.getBasePriceCal = function(itemObj,idx)
    {
    	
    	$scope.isFinancialClearence ='N';
    	$scope.financialRequestSendTo = "";
    	// console.log("OBJ",itemObj);
    	if(itemObj.basePrice == '' || itemObj.basePrice == undefined)
		{
		  itemObj.basePrice = 0;
		}
    	
    	if(itemObj.basePrice == '.' )
		{
		  itemObj.basePrice =  itemObj.basePriceOld;
		}
    	
    	if(itemObj.maxRateEditable!= null && itemObj.maxRateEditable < itemObj.basePrice){
    		 growl.warning(" You can edit rate max up to "+itemObj.maxRateEditable,{
					title : ERROR_MSG
				});
    		 itemObj.basePrice =  itemObj.basePriceOld;
    		 return false;
    	}
    	
    	if(itemObj.minRateEditable!= null && itemObj.minRateEditable > itemObj.basePrice){
   		 growl.warning(" You can edit rate min up to "+itemObj.minRateEditable,{
					title : ERROR_MSG
				});
   		 itemObj.basePrice =  itemObj.basePriceOld;
   		 return false;
   	}
    	
    	if(parseFloat(itemObj.basePrice) == parseFloat(itemObj.basePriceRateChange))
    	{
          $rootScope.stopLoaderPopup();
    	}
    	if(parseFloat(itemObj.basePrice) != parseFloat(itemObj.basePriceRateChange))
    	{
    		if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
    			
    			 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
						title : ERROR_MSG
					});
    			
    			var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;

    	    	var indexList = [];
    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    				var j = 0;
    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
    						{
    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
    							{
			    					var totalAmt = 0;
			    					var dis = 0;
			    					var gstAmt = 0;
			    					var withConDic = 0;
			    	    		
			    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
			    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
			    						$scope.autoRenderedServicesFinalList[j].discount = 0;
			    						$scope.roundOffAmt = 0.00;
			    						
			    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
			    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    						
			    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
			    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
			
			    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
			    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
			    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
			    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
			    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
			    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
			    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
			    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
			    			    		indexList.push(j);
    							}	
    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    	    			{
    	    	            $scope.addOrUpdateServices(null,indexList);
    	    			}
    					
    	    		j++;
    				
    			});
    	    
    			
    		}else{
    			
	    			if($scope.requestStatusObject != null)
					{
						  $scope.getCancelDiscountReqForDiscount();
						
					}
					
					$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
		    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
		    		
		    		$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
					$scope.autoRenderedServicesFinalList[idx].concession = ((parseFloat(totalAmt))*(isNaN(itemObj.patientClassPercentage) ? 0: itemObj.patientClassPercentage)*0.01).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);
					$scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)*0.01).toFixed(2);
	                
	                var withConDic =parseFloat(totalAmt) - (parseFloat(itemObj.concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
			        
			    	var txpre = 0;
					if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
						$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
						txpre = itemObj.taxPercentage;
					}else{
							$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
							}
					$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
					$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
					
			        	$scope.autoRenderedServicesForSaveAndUpdate=[];
			        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
			        	$scope.addOrUpdateServices(idx,null);
	
					$scope.billWiseDiscountObj = {
							// "discountGivenBy":"",
							"discountGivenBy":$scope.userId,
							"discountCategoryId" : "",
							"discountAmountBillWise" : "",
							"discountPercentageBillWise" : "",
							"approvedAmountBillWise":"",
							"requestUserId":"",
							"requestAmountBillWise" : 0,
					}
					$scope.requestDiscountDiv = false;
    		
    		}
				
    	}
    	else
    		{
    		$rootScope.stopLoader();
    		}
   }
    
  
    $scope.rateEditCancelDiscount = function()
    {
    	$rootScope.startLoaderPopup();
		var data = {
				reqDiscId :$scope.requestStatusObject.reqDiscId,
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
		
		GenericService
		.serviceAction("POST", URI, data)
		.then(function(response) {
					$rootScope.stopLoaderPopup();
					if (response.data.status == "success") {
						
						 var i =0;
				    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				   			if(value.status == 'A'){
							   		$scope.autoRenderedServicesFinalList[i].discountCategoryId = "";
							   		$scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
							   		$scope.autoRenderedServicesFinalList[i].discount = 0;
							   		// for remove discount
				   			 }
				   			i++;
				   			});
				    	   
				    	   $scope.calculationTest($scope.autoRenderedServicesFinalList,discountPer);
				    	   
						growl.success("Cancelled !!!!",{
							title : 'success'
						});
						
						$scope.requestApporivedDiscountDiv = false;
						$scope.discountDisabledFlagForRequest = false;
						$scope.requestAppUser = 0;
						$scope.approvedAmtApp = 0;
						$scope.requestAmtApp =0;
						$scope.statusApp = "";
						$scope.discTypeDesc = "";
						$scope.discountRadio = 2; 
						
						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						 $scope.requestStatusObject  = null;
						// $scope.totalDiscountSummary = parseFloat(0)
			    		angular.element('#discountRatePopup').modal('hide');
					}
				});
    }
    
    $scope.rateEditCancelDiscountLine = function()
    {
			$rootScope.startLoaderPopup();
			$scope.autoRenderedServicesFinalList[$scope.serviceAutoRenInd].basePrice = $scope.serviceAutoRenOldBasePrice;
   		    var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
       	    $scope.calculationTest(autoRenderedServicesList,discount);
       	    $rootScope.stopLoaderPopup();
			angular.element('#discountRatePopup').modal('hide');
    }
    
    
 $scope.getConcessionCal = function(itemObj,idx){
    	
	 if(parseFloat(itemObj.patientClassPercentage) != parseFloat(itemObj.patClsPerPriceRateChange))
 	{
	 
    	if(itemObj.patientClassPercentage == '' || itemObj.patientClassPercentage == undefined || itemObj.patientClassPercentage == '.' ){
    		itemObj.patientClassPercentage = 0;
    		itemObj.concession = 0;
    		$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = 0;
    		$scope.autoRenderedServicesFinalList[idx].concession = 0;
    	}
    	if(parseFloat(itemObj.patientClassPercentage) > 100)
		{
    		if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
    			
    			 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
						title : ERROR_MSG
					});
    			
    			var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
    	    	var indexList = [];
    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    				var j = 0;
    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
    						{
    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
    							{
    		    					var totalAmt = 0;
    		    					var dis = 0;
    		    					var gstAmt = 0;
    		    					var withConDic = 0;
    		    					$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(0);
    		    	    			$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(0);
    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
    		    						$scope.roundOffAmt = 0.00;
    		    						
    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    		    						
    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    		
    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
    		    			    		indexList.push(j);
    							}	
    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    	    			{
    	    	            $scope.addOrUpdateServices(null,indexList);
    	    			}
    					
    	    		j++;
    				
    			});
    	    
    		}else{
    			
    			$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(0);
    			$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(0);
        		
        		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount);
        		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);
        		
        		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
    	        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
    	        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
    	        
    	    	var txpre = 0;
    			if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
    				$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
    				txpre = itemObj.taxPercentage;
    			}else{
    					$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
    					}
    			$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
    			$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
    			$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
    			$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
    			$scope.autoRenderedServicesForSaveAndUpdate=[];
            	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
            	$scope.addOrUpdateServices(idx,null);
    		}
    		
			
			// flagDis = false;
			growl.warning("You can't give concession more than 100 %.",{
				title : ERROR_MSG
			});
			return false;
    		
		}
    	else
    	{
    		var i = 1;
    		var discountAmtTmp = parseFloat(0);
    		
    					if(parseFloat(itemObj.patientClassPercentage) >= 0)
    					{
    						
    						if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
    							
    							 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
 									title : ERROR_MSG
 								});
    							
    							var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;

    			    	    	var indexList = [];
    			    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    			    				var j = 0;
    			    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
    			    						{
    			    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
    			    							{
    			    		    					var totalAmt = 0;
    			    		    					var dis = 0;
    			    		    					var gstAmt = 0;
    			    		    					var withConDic = 0;
    			       	   						 
    			            						$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
    			            						var conAmt =  ((parseFloat(totalAmt)*parseFloat(itemObj.patientClassPercentage))/100).toFixed(2);
    			            							$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(conAmt);
    			    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
    			    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
    			    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
    			    		    						
    			    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
    			    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    			    		    						
    			    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
    			    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    			    		
    			    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
    			    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
    			    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
    			    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
    			    		    			    		indexList.push(j);
    			    							}	
    			    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    			    	    			{
    			    	    	            $scope.addOrUpdateServices(null,indexList);
    			    	    			}
    			    					
    			    	    		j++;
    			    				
    			    			});
    			    	    
    						}else{
    							
    							
    							var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	   						 
        						$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
        						var conAmt =  ((parseFloat(totalAmt)*parseFloat(itemObj.patientClassPercentage))/100).toFixed(2);
        						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(conAmt);
        						
        						
    	    			        $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);
    	    			        discountAmtTmp = discountAmtTmp + parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
    	    			        $scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage).toFixed(2);
    	    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
    	    			        
    	    			    	var txpre = 0;
    							if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
    								$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
    								txpre = itemObj.taxPercentage;
    							}else{
    									$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
    									}
    							$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
    							$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
    							$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
    							$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
    							
    							if($scope.requestStatusObject == null){
						        	$scope.autoRenderedServicesForSaveAndUpdate=[];
						        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
						        	$scope.addOrUpdateServices(idx,null);
    							}else{
									if($scope.requestStatusObject.reqStatus == 1){
											$scope.getCancelDiscountReqForDiscount();
									        	$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
									}
									else{
										if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
									        	$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
										}else{
											$scope.getCancelDiscountReqForDiscount();
									        	$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
										}
									}
								}
    						}
    						
    					}
							
		}
    	
    	$scope.billWiseDiscountObj = {
				// "discountGivenBy":"",
				"discountGivenBy":$scope.userId,
				"discountCategoryId" : "",
				"discountAmountBillWise" : "",
				"discountPercentageBillWise" : "",
				"approvedAmountBillWise":"",
				"requestUserId":"",
				"requestAmountBillWise" : 0,
		}
		$scope.requestDiscountDiv = false;
    	// patientClassPercentageChange
 		}
    }
 
 
    $scope.getDiscountPerCal = function(itemObj,idx){
    	
    	if(parseFloat(itemObj.discountPercentage) != parseFloat(itemObj.discountPercentageRateChange))
    	{
    		itemObj.discountCategoryId=$scope.discountCategoryList[0].id;
	    	if(itemObj.discountPercentage == '' || itemObj.discountPercentage == undefined || itemObj.discountPercentage == '.' ){
	    		itemObj.discountPercentage = 0;
	    		itemObj.discount = 0;
	    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
	    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
	    	}
	    	if(parseFloat(itemObj.discountPercentage) > 100)
			{
	    		
	    		if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){

	    			 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
							title : ERROR_MSG
						});
	    			
	    			var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
	    	    	var indexList = [];
	    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
	    				var j = 0;
	    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
	    						{
	    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
	    							{
	    		    					var totalAmt = 0;
	    		    					var dis = 0;
	    		    					var gstAmt = 0;
	    		    					var withConDic = 0;
	    		    					$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
	    		    					$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
	    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
	    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
	    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
	    		    						$scope.roundOffAmt = 0.00;
	    		    						
	    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
	    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	    		    						
	    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
	    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
	    		
	    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
	    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
	    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
	    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
	    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
	    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
	    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
	    		    			    		indexList.push(j);
	    							}	
	    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
	    	    			{
	    	    	            $scope.addOrUpdateServices(null,indexList);
	    	    			}
	    					
	    	    		j++;
	    				
	    			});
	    	    
	    		
	    		}else{
	    			
	    			$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
					$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
		    		
		    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
		    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
		    		
		    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
			        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
			        
			    	var txpre = 0;
					if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
						$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
						txpre = itemObj.taxPercentage;
					}else{
							$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
							}
					$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
					$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
					$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
					
			        // $scope.saveOrderPackage();
					$scope.autoRenderedServicesForSaveAndUpdate=[];
		        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
		        	$scope.addOrUpdateServices(idx,null);
	    			
	    		}
				
				growl.warning("You can't give discount more than 100 %.",{
					title : ERROR_MSG
				});
				
				$scope.billWiseDiscountObj = {
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				}
				$scope.requestDiscountDiv = false;
				return false;
				
			}else{
	    		$scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				}
				$scope.requestDiscountDiv = false;
	    		
	    		var i = 1;
	    		var discountAmtTmp = parseFloat($scope.totalDiscountSummary) - parseFloat(itemObj.oldDiscount) ;
	    		
	    					if(parseFloat(itemObj.discountPercentage) >= 0)
	    					{
	    						
	    						if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){

	    							 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
	    									title : ERROR_MSG
	    								});
	    							
	    			    			var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
	    			    	    	var indexList = [];
	    			    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
	    			    				var j = 0;
	    			    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
	    			    						{
	    			    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
	    			    							{
	    			    		    					var totalAmt = 0;
	    			    		    					var dis = 0;
	    			    		    					var gstAmt = 0;
	    			    		    					var withConDic = 0;
	    			    		    					$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
	    			    		    					$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
	    			    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
	    			    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
	    			    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
	    			    		    						$scope.roundOffAmt = 0.00;
	    			    		    						
	    			    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
	    			    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	    			    		    						
	    			    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
	    			    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
	    			    		
	    			    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
	    			    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
	    			    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
	    			    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    			    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
	    			    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
	    			    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
	    			    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
	    			    		    			    		indexList.push(j);
	    			    							}	
	    			    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
	    			    	    			{
	    			    	    	            $scope.addOrUpdateServices(null,indexList);
	    			    	    			}
	    			    					
	    			    	    		j++;
	    			    				
	    			    			});
	    							
	    						}else{
	    							
	    							$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
		   						    $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
		   						    
			    			        var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
			    			       
			    			        $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);
			    			        discountAmtTmp = discountAmtTmp + parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
			    			        $scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage).toFixed(2);
			    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
			    			        
			    			    	var txpre = 0;
									if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
										$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
										txpre = itemObj.taxPercentage;
									}else{
											$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
											}
									$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
									$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
									$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
									$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
									
										if($scope.requestStatusObject == null){
								        	$scope.autoRenderedServicesForSaveAndUpdate=[];
								        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
								        	$scope.addOrUpdateServices(idx,null);
										}else{
										if($scope.requestStatusObject.reqStatus == 1){
												$scope.getCancelDiscountReqForDiscount();
										        	$scope.autoRenderedServicesForSaveAndUpdate=[];
										        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
										        	$scope.addOrUpdateServices(idx,null);
										}
										else{
											if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
												$scope.autoRenderedServicesForSaveAndUpdate=[];
									        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
									        	$scope.addOrUpdateServices(idx,null);
											}else{
												$scope.getCancelDiscountReqForDiscount();
										        	$scope.autoRenderedServicesForSaveAndUpdate=[];
										        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
										        	$scope.addOrUpdateServices(idx,null);
											}
										}
									}
	    						}
	    						
	    					}
								
			}
    	
    }
    	if(itemObj.discount==undefined || itemObj.discount==null || itemObj.discount==0)
    		{
    		itemObj.discountCategoryId=null;
    		}
    	
    	   $scope.billWiseDiscountObj.requestAmountBillWise =0;
    	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
    		   
    		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
    		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
    	   });
    	   
    	   if($scope.userDiscountObject != null){
	    		
				if($scope.userDiscountObject.discountType == 1){
					
					var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
					var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
					$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
					$scope.userDiscountObject.toDiscountAmount = toDisAmt;
				}
	    	}
    	   
    	   $scope.funDiscountAmountBillWise();
    	   var checkrightsAmount = false;
    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
    		   checkrightsAmount = true;
    	   }
    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
    		   checkrightsAmount = true;
    	   }
    	   
    	   if(checkrightsAmount == true){
	    		var URI = BASE_URL + ROOT_URL
	    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
	    BillingGenericService.serviceAction(METHOD_GET, URI,"",
	    				NOTIFICATION_MSG_STATUS_FALSE)
	    		.then(function(response) {
	    					//console.log(response);
	    					// $rootScope.startLoader();
	    					if (response.data.status == "success")
	    						{
	    						  $scope.userDiscountList =[];
	    						  $scope.userDiscountList = response.data.listObject;
	    						}
	    						
	    				});
	            $scope.lineLevelItem = true;
			   $scope.requestDiscountDiv = true;
			   $scope.isBillWiseDiscountApplied = true;
			   $scope.finalBill = 'N';
			   growl.warning("Please send discount request to user",{
					title : ERROR_MSG
				});
			   
	    	}else
	    		{
	    		 $scope.requestDiscountDiv = false;
	    		 $scope.billWiseDiscountObj = {
							"discountGivenBy":$scope.userId,
							"discountCategoryId" : "",
							"discountAmountBillWise" : "",
							"discountPercentageBillWise" : "",
							"approvedAmountBillWise":"",
							"requestUserId":"",
							"requestAmountBillWise" : 0,
					}
					$scope.requestDiscountDiv = false;
			    	$scope.isFinancialClearence = 'N';
	    		}
    }
    
    $scope.lineLevelItem = false;
    
    $scope.getDiscountCalOnloadForWarningMsg = function(itemObj,idx)
    {
    	 $scope.billWiseDiscountObj = {
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			$scope.requestDiscountDiv = false;
	    	$scope.isFinancialClearence = 'N';
	    	   $scope.billWiseDiscountObj.requestAmountBillWise =0;
	    	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
	    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
	    		   
	    		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
	    		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
	    	   });
	    	   
	    	   $scope.billWiseDiscountObj.discountAmountBillWise  = $scope.billWiseDiscountObj.discountAmountBillWise.toFixed();
	    	   $scope.billWiseDiscountObj.requestAmountBillWise  = $scope.billWiseDiscountObj.requestAmountBillWise.toFixed();
	    	   
	    	   if($scope.userDiscountObject != null){
		    		
					if($scope.userDiscountObject.discountType == 1){
						
						var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
						var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
						$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
						$scope.userDiscountObject.toDiscountAmount = toDisAmt;
					}
		    	}
	    	   
	    	   $scope.funDiscountAmountBillWise();
	    	   var checkrightsAmount = false;
	    	  if($scope.userDiscountObject != null){
	    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
	    		   checkrightsAmount = true;
	    	   }
	    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
	    		   checkrightsAmount = true;
	    	   }
	    	  }else{
	    		  checkrightsAmount = true;
	    	  }
	    	   
	    	   if(checkrightsAmount == true){
		    		var URI = BASE_URL + ROOT_URL
		    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
		    BillingGenericService.serviceAction(METHOD_GET, URI,"",
		    				NOTIFICATION_MSG_STATUS_FALSE)
		    		.then(function(response) {
		    					//console.log(response);
		    					// $rootScope.startLoader();
		    					if (response.data.status == "success")
		    						{
		    						  $scope.userDiscountList =[];
		    						  $scope.userDiscountList = response.data.listObject;
		    						}
		    						
		    				});
		            $scope.lineLevelItem = true;
				   $scope.requestDiscountDiv = true;
				   $scope.isBillWiseDiscountApplied = true;
				   $scope.finalBill = 'N';
				   growl.warning("Please send discount request to user",{
						title : ERROR_MSG
					});
				   
		    	}else
		    		{
		    		 $scope.requestDiscountDiv = false;
		    		 $scope.billWiseDiscountObj = {
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						$scope.requestDiscountDiv = false;
				    	$scope.isFinancialClearence = 'N';
		    		}
	    	   
    	if(parseFloat(itemObj.discount) != parseFloat(itemObj.discountRateChange))
    	{
    		itemObj.discountCategoryId=$scope.discountCategoryList[0].id;
    	if(itemObj.discount == '' || itemObj.discount == undefined || itemObj.discount == '.'){
    		itemObj.discount = 0;
    		itemObj.discountPercentage = 0;
    		$scope.autoRenderedServicesFinalList[idx].discount = 0;
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = 0;
    	}
    	var totalServiceAmount = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	if(parseFloat(itemObj.discount) > parseFloat(totalServiceAmount))
		{
    		if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
    			
    			growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
						title : ERROR_MSG
					});
    			
    			var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
    	    	var indexList = [];
    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    				var j = 0;
    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
    						{
    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
    							{
    		    					var totalAmt = 0;
    		    					var dis = 0;
    		    					var gstAmt = 0;
    		    					var withConDic = 0;
    		    					$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
    		    					$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
    		    						$scope.roundOffAmt = 0.00;
    		    						
    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    		    						
    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    		
    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
    		    			    		indexList.push(j);
    							}	
    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    	    			{
    	    	        //    $scope.addOrUpdateServices(null,indexList);
    	    			}
    					
    	    		j++;
    				
    			});
    	    
    		
    		}else{
    			
    			$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
				$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
	    		
	    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
	    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
	    		
	    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
		        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
		        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
		        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
		        
		    	var txpre = 0;
				if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
					txpre = itemObj.taxPercentage;
				}else{
						$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
						}
				$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
				$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
				$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
				
		        // $scope.saveOrderPackage();
				$scope.autoRenderedServicesForSaveAndUpdate=[];
	        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
	        //	$scope.addOrUpdateServices(idx,null);
    			
    		}
			
			growl.warning("You can't give discount more then service amount..",{
				title : ERROR_MSG
			});
			
			$scope.billWiseDiscountObj = {
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			$scope.requestDiscountDiv = false;
			return false;
		}
    	else
    	{
    		
    		var i = 1;
    		var discountAmtTmp = parseFloat($scope.totalDiscountSummary) - parseFloat(itemObj.oldDiscount);
    		
    					if(parseFloat(itemObj.discount) >= 0)
    					{
    						if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
    							
    							 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
    									title : ERROR_MSG
    								});
    							
    							var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
    			    	    	var indexList = [];
    			    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    			    				var j = 0;
    			    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
    			    						{
    			    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
    			    							{
    			    		    					var totalAmt = 0;
    			    		    					var dis = 0;
    			    		    					var gstAmt = 0;
    			    		    					var withConDic = 0;
    			    		    					$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
    			    		    					$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
    			    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
    			    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
    			    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
    			    		    						$scope.roundOffAmt = 0.00;
    			    		    						
    			    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
    			    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    			    		    						
    			    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
    			    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    			    		
    			    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
    			    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
    			    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
    			    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
    			    		    			    		indexList.push(j);
    			    							}	
    			    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    			    	    			{
    			    	    	          //  $scope.addOrUpdateServices(null,indexList);
    			    	    			}
    			    					
    			    	    		j++;
    			    				
    			    			});
    							
    							
    						}else{
    							
    							discountAmtTmp = discountAmtTmp + parseFloat(itemObj.discount);
        						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
       						    $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
       						    
    	    			        var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	    			        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/((parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice)  - parseFloat(itemObj.concession) )) ))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice) - parseFloat(itemObj.concession) )))*100;
    	    			        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
    	    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
    	    			        
    	    			    	var txpre = 0;
    							if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
    								$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
    								txpre = itemObj.taxPercentage;
    							}else{
    									$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
    									}
    							$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
    							$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
    							/*$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
    							$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);*/
    							
    							if($scope.requestStatusObject == null)
    							{
    								$scope.autoRenderedServicesForSaveAndUpdate=[];
    					        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    					        	//$scope.addOrUpdateServices(idx,null);
    							}else{
    								if($scope.requestStatusObject.reqStatus == 1){
    										$scope.getCancelDiscountReqForDiscount();
    										$scope.autoRenderedServicesForSaveAndUpdate=[];
    							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    							        	//$scope.addOrUpdateServices(idx,null);
    								}
    								else{
    									if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
    										$scope.autoRenderedServicesForSaveAndUpdate=[];
    							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    							        	//$scope.addOrUpdateServices(idx,null);
    									}else{
    										//$scope.getCancelDiscountReqForDiscount();
    										$scope.autoRenderedServicesForSaveAndUpdate=[];
    							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    							        	//$scope.addOrUpdateServices(idx,null);
    									}
    								}
    							}
    							
    						}
    						
    						
    					
    					}
				}
    	}
    	if(itemObj.discountPercentage==undefined || itemObj.discountPercentage==null || itemObj.discountPercentage==0)
    		{
    		itemObj.discountCategoryId=null;
    		}
    }
    
    
    $scope.getDiscountCal = function(itemObj,idx)
    {
    	 $scope.billWiseDiscountObj = {
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			$scope.requestDiscountDiv = false;
	    	$scope.isFinancialClearence = 'N';
	    	   $scope.billWiseDiscountObj.requestAmountBillWise =0;
	    	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
	    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
	    		   
	    		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
	    		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
	    	   });
	    	   
	    	   $scope.billWiseDiscountObj.discountAmountBillWise  = $scope.billWiseDiscountObj.discountAmountBillWise.toFixed();
	    	   $scope.billWiseDiscountObj.requestAmountBillWise  = $scope.billWiseDiscountObj.requestAmountBillWise.toFixed();
	    	   
	    	   if($scope.userDiscountObject != null){
		    		
					if($scope.userDiscountObject.discountType == 1){
						
						var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
						var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
						$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
						$scope.userDiscountObject.toDiscountAmount = toDisAmt;
					}
		    	}
	    	   
	    	   $scope.funDiscountAmountBillWise();
	    	   var checkrightsAmount = false;
	    	  if($scope.userDiscountObject != null){
	    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
	    		   checkrightsAmount = true;
	    	   }
	    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
	    		   checkrightsAmount = true;
	    	   }
	    	  }else{
	    		  checkrightsAmount = true;
	    	  }
	    	   
	    	   if(checkrightsAmount == true){
		    		var URI = BASE_URL + ROOT_URL
		    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
		    BillingGenericService.serviceAction(METHOD_GET, URI,"",
		    				NOTIFICATION_MSG_STATUS_FALSE)
		    		.then(function(response) {
		    					//console.log(response);
		    					// $rootScope.startLoader();
		    					if (response.data.status == "success")
		    						{
		    						  $scope.userDiscountList =[];
		    						  $scope.userDiscountList = response.data.listObject;
		    						}
		    						
		    				});
		            $scope.lineLevelItem = true;
				   $scope.requestDiscountDiv = true;
				   $scope.isBillWiseDiscountApplied = true;
				   $scope.finalBill = 'N';
				   growl.warning("Please send discount request to user",{
						title : ERROR_MSG
					});
				   
		    	}else
		    		{
		    		 $scope.requestDiscountDiv = false;
		    		 $scope.billWiseDiscountObj = {
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						$scope.requestDiscountDiv = false;
				    	$scope.isFinancialClearence = 'N';
		    		}
	    	   
    	if(parseFloat(itemObj.discount) != parseFloat(itemObj.discountRateChange))
    	{
    		itemObj.discountCategoryId=$scope.discountCategoryList[0].id;
    	if(itemObj.discount == '' || itemObj.discount == undefined || itemObj.discount == '.'){
    		itemObj.discount = 0;
    		itemObj.discountPercentage = 0;
    		$scope.autoRenderedServicesFinalList[idx].discount = 0;
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = 0;
    	}
    	var totalServiceAmount = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	if(parseFloat(itemObj.discount) > parseFloat(totalServiceAmount))
		{
    		if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
    			
    			growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
						title : ERROR_MSG
					});
    			
    			var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
    	    	var indexList = [];
    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    				var j = 0;
    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
    						{
    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
    							{
    		    					var totalAmt = 0;
    		    					var dis = 0;
    		    					var gstAmt = 0;
    		    					var withConDic = 0;
    		    					$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
    		    					$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
    		    						$scope.roundOffAmt = 0.00;
    		    						
    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    		    						
    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    		
    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
    		    			    		indexList.push(j);
    							}	
    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    	    			{
    	    	            $scope.addOrUpdateServices(null,indexList);
    	    			}
    					
    	    		j++;
    				
    			});
    	    
    		
    		}else{
    			
    			$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
				$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
	    		
	    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
	    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
	    		
	    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
		        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
		        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
		        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
		        
		    	var txpre = 0;
				if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
					txpre = itemObj.taxPercentage;
				}else{
						$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
						}
				$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
				$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
				$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
				
		        // $scope.saveOrderPackage();
				$scope.autoRenderedServicesForSaveAndUpdate=[];
	        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
	        	$scope.addOrUpdateServices(idx,null);
    			
    		}
			
			growl.warning("You can't give discount more then service amount..",{
				title : ERROR_MSG
			});
			
			$scope.billWiseDiscountObj = {
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			$scope.requestDiscountDiv = false;
			return false;
		}
    	else
    	{
    		
    		var i = 1;
    		var discountAmtTmp = parseFloat($scope.totalDiscountSummary) - parseFloat(itemObj.oldDiscount);
    		
    					if(parseFloat(itemObj.discount) >= 0)
    					{
    						if($scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId != null){
    							
    							 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
    									title : ERROR_MSG
    								});
    							
    							var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;
    			    	    	var indexList = [];
    			    	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    			    				var j = 0;
    			    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
    			    						{
    			    							if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
    			    							{
    			    		    					var totalAmt = 0;
    			    		    					var dis = 0;
    			    		    					var gstAmt = 0;
    			    		    					var withConDic = 0;
    			    		    					$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
    			    		    					$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
    			    		    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
    			    		    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
    			    		    						$scope.autoRenderedServicesFinalList[j].discount = 0;
    			    		    						$scope.roundOffAmt = 0.00;
    			    		    						
    			    		    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
    			    		    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    			    		    						
    			    		    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
    			    		    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    			    		
    			    		    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    			    		    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
    			    		    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
    			    		    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
    			    		    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
    			    		    			    		indexList.push(j);
    			    							}	
    			    	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    			    	    			{
    			    	    	            $scope.addOrUpdateServices(null,indexList);
    			    	    			}
    			    					
    			    	    		j++;
    			    				
    			    			});
    							
    							
    						}else{
    							
    							discountAmtTmp = discountAmtTmp + parseFloat(itemObj.discount);
        						$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
       						    $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
       						    
    	    			        var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	    			        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/((parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice)  - parseFloat(itemObj.concession) )) ))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * (parseFloat(itemObj.basePrice) - parseFloat(itemObj.concession) )))*100;
    	    			        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
    	    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
    	    			        
    	    			    	var txpre = 0;
    							if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
    								$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
    								txpre = itemObj.taxPercentage;
    							}else{
    									$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
    									}
    							$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
    							$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
    							$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
    							$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
    							
    							if($scope.requestStatusObject == null)
    							{
    								$scope.autoRenderedServicesForSaveAndUpdate=[];
    					        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    					        	$scope.addOrUpdateServices(idx,null);
    							}else{
    								if($scope.requestStatusObject.reqStatus == 1){
    										$scope.getCancelDiscountReqForDiscount();
    										$scope.autoRenderedServicesForSaveAndUpdate=[];
    							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    							        	$scope.addOrUpdateServices(idx,null);
    								}
    								else{
    									if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
    										$scope.autoRenderedServicesForSaveAndUpdate=[];
    							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    							        	$scope.addOrUpdateServices(idx,null);
    									}else{
    										$scope.getCancelDiscountReqForDiscount();
    										$scope.autoRenderedServicesForSaveAndUpdate=[];
    							        	$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[idx]);
    							        	$scope.addOrUpdateServices(idx,null);
    									}
    								}
    							}
    							
    						}
    						
    						
    					
    					}
				}
    	}
    	if(itemObj.discountPercentage==undefined || itemObj.discountPercentage==null || itemObj.discountPercentage==0)
    		{
    		itemObj.discountCategoryId=null;
    		}
    }
    
    $scope.getDiscountListForInfo  = function(itemObj,idx)
    {
    	// console.log(itemObj);
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
		
    	$scope.serciveNamePop = itemObj.serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup))*itemObj.discountPercentage/100).toFixed(2);
    	$scope.discountDetailsArrayListPopup = itemObj.discountDetailsList;
    	$scope.objectIndex = idx;
    }
    $scope.getDiscountList  = function(itemObj,idx)
    {
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
		
    	$scope.serciveNamePop = itemObj.serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup))*itemObj.discountPercentage/100).toFixed(2);
    	$scope.discountDetailsArrayListPopup = itemObj.discountDetailsList;
    	$scope.objectIndex = idx;
    }
    $scope.getDiscountListPopup  = function(itemObj,idx)
    {
    	$scope.serciveNamePop = itemObj[idx].serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj[idx].quantity) * parseFloat(itemObj[idx].basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj[idx].quantity) * parseFloat(itemObj[idx].basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup))*itemObj[idx].discountPercentage/100).toFixed(2);
    	$scope.discountDetailsArrayListPopup = itemObj[idx].discountDetailsList;
    	$scope.objectIndex = idx;
    }
    
    $scope.getDisPerPopup = function(disPer)
    {
    	var idx  = $scope.objectIndex;
    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
    	var calDicAmt = ((parseFloat(totalAmtPopup))*disPer/100).toFixed(2);	
		$scope.disAmtPopup = calDicAmt;
		
    }
    
    $scope.getDisAmtPopup = function(disAmt)
    {
    	var idx  = $scope.objectIndex;
    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
    	var disPer = ((parseFloat(disAmt)/(totalAmtPopup))*100).toFixed(2);
		$scope.disPerPopup = disPer;
		
    }
    
    $scope.addDiscountInDiscountList = function()
    {
    	if($scope.disPerPopup == '' || $scope.disAmtPopup == '')
    	{
    		growl.warning("Please Enter Discount",{
				title : ERROR_MSG
			});
    	}
    	if($scope.disPerPopup !== ''  && $scope.disCategoryId == '')
    	{
    		growl.warning("Please Select Discount Category",{
				title : ERROR_MSG
			});
    	}
    	if($scope.disAmtPopup !== ''  && $scope.disCategoryId == '')
    	{
    		growl.warning("Please Select Discount Category",{
				title : ERROR_MSG
			});
    	}
    	var idx  = $scope.objectIndex;
    	// console.log(idx);
    	// console.log("hhh",$scope.autoRenderedServicesFinalList[idx]);
    	
    	var disCatName = $.grep($scope.discountCategoryList, function (disCat) {
            return disCat.id == $scope.disCategoryId;
        })[0].type;
    	
    	var popupData = {
    			"discountPercentage":$scope.disPerPopup,
    			"discountAmount":(parseFloat($scope.disAmtPopup)).toFixed(2),
    			"discountCategory":disCatName,
    			"discountCategoryId":$scope.disCategoryId,
    			"createdBy" : $scope.userId,
				"createdDate" :new Date().getTime()
    			
    	}
    	
    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.push(popupData);
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	$scope.calculationTest(autoRenderedServicesList,init);
    	
    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);
    	
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
    }
    
    
    $scope.getSubSpecialityListById = function(spId)
    {
    	
    	$scope.serviceSubSpecialityIdForGroup = '';
    	 var specialityIDS = [];
    	 specialityIDS.push(spId);
			
         var data = {
       		  organizationId:$scope.organizationId,
       		  unitId:$scope.unitId,
       		  specialityIdList:specialityIDS
         }
         // console.log(data);
         var URI = BASE_URL + ROOT_URL+ "/adt/subspeciality/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/specialityId/"+spId;
	BillingGenericService
			.serviceAction("GET", URI,
					"",
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(
					function(response) {
						// console.log(response);
						// $rootScope.startLoader();
						if (response.data.status == "success")
							{
							$scope.subSpecialityMaterList = response.data.listObject;
							}
							
					});
    }
    $scope.getservicesPop = function()
    {
    	$scope.servicetab =1;
    	$scope.servicesListPopup = [];
    	$scope.selectedPackageList = [];
        $scope.selectedServices=[];
    	$scope.totalAmountSummaryPopup = parseFloat(0);
    }
    
    
    $scope.checkServiceMappedWithTest = function(serviceId,specialityId)
	{
		     $rootScope.startLoaderPopup();
		     var labSpecialityId = 0;
		     if(specialityId==$scope.labConfig.labSpecialityId)
				{
		    	   labSpecialityId = specialityId;
				}
			var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/"+labSpecialityId;
			var data = {serviceMasterId:serviceId,orgId:$scope.organizationId,unitId:$scope.unitId};
		    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
			{
				if(response.data.status == "success")
				{
					$rootScope.stopLoaderPopup();
					
					if(response.data.object == 0)
					{
					   growl
						.warning('Service is not mapped with test..!!',{
									title : 'Warning!'
								});
					   response.data.object = 1;
				     }
					 return response.data.object;
				}
				else if(response.data.status == "error"){
					  $rootScope.stopLoaderPopup();
					  return response.data.object;
				   }
			 });
	}
    
    
    $scope.checkServiceWithTestParameter = function(serviceId,serviceIdList)
	{
		     $rootScope.startLoaderPopup();
 
			var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/testParameter";
			var data = {
						serviceMasterId:serviceId,
						orgId:$scope.organizationId,
						unitId:$scope.unitId,
						serviceIds:serviceIdList
					};
			
		    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
			{
				if(response.data.status == "success")
				{
					$rootScope.stopLoaderPopup();
					
					if($scope.serviceCheckFlagForParameter == true){
						return response.data.object = 0;
					}else{
						 return response.data.object;
					}
					
					
				}
				else if(response.data.status == "error"){
					  $rootScope.stopLoaderPopup();
					  return response.data.object;
				   }
			 });
	
	}
    
    // chetana
    $scope.item={};
	$scope.mod="";
	$scope.labl="";
    $scope.addServiceToTempServiceList = function($item, $model, $label,freeTextOrderedServicesId,freeTextserviceName)
    {
        if($item.preRequisite == 'Y' && $scope.serviceCheckFlag==false){
            $scope.preRequisiteDetails = $item.preRequisiteDetails;
            angular.element('#preRequisitePopup').modal('show');
            
            
            $timeout(function () {
                angular.element('#preRequisitePopup').modal('hide');
               }, 10000);
            
        }

    	$scope.freeTextOrderedServicesId = freeTextOrderedServicesId;
    	$scope.freeTextserviceName = freeTextserviceName;
    	$scope.isFromAddServiceToTempServiceList=true;
    	$scope.item={};
		$scope.mod="";
		$scope.labl="";
    	var serviceObj = $scope.autoRenderedServicesFinalList.find(obj => {return obj.serviceMasterId == $item.serviceMasterId})
      	if(serviceObj != undefined && $scope.serviceCheckFlag==false){
      		   
      		 $scope.item=$item;
 			$scope.mod=$model;
 			$scope.labl=$label;
 			angular.element('#pendingServiceAdd').modal('show');
      	         return false;
      	} 
    	
    	var serviceObj1 = $scope.servicesListPopup.find(obj => {return obj.serviceMasterId == $item.serviceMasterId})
      	if(serviceObj1 != undefined && $scope.serviceCheckFlag==false){
      		   
      	  $scope.item=$item;
			$scope.mod=$model;
			$scope.labl=$label;
			angular.element('#pendingServiceAdd').modal('show');
      	         return false;
      	} 
    	
    	  var serviceMasterIds = [];
          angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
          		serviceMasterIds.push(value.serviceMasterId);
  			});	
          
          angular.forEach($scope.servicesListPopup, function (value, key) {
        		serviceMasterIds.push(value.serviceMasterId);
			});	
    	
    	  var promise = $scope.checkServiceWithTestParameter($item.serviceMasterId,serviceMasterIds);
          
          promise.then(function(obj1) 
  				{
      	             if(obj1==0)
      	             {	
    	
					    	var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
							promise.then(function(obj) 
									{
					    	             if(obj>0)
					    	             {
					    	            	 if($scope.serviceSpecialityId==undefined){
					    	            			angular.forEach($scope.specialityMasterList, function (value, key) {
					    	            				if($item.specialityId==value.specialityId){
					    	            					$scope.serviceSpecialityId=$item.specialityId.toString();
					    	            					$scope.getSubSpecialityListById($item.specialityId);
					    	            					}
					    	            				});		
					    	            			}
					    	            			if($scope.serviceSubSpecialityId==undefined){
					    	            				angular.forEach($scope.subSpecialityMaterList, function (value, key) {
					    	            					if($item.subSpecialityId==value.subSpecialityMasterId){
					    	            						$scope.serviceSubSpecialityId=$item.subSpecialityId.toString();
					    	            						}
					    	            					});
					    	            			}
					    	            			$scope.forServicePayeeId  = $scope.selfPayeeId;
					    	            			$scope.search = '';
					    	            			$('#searchServiceId').val('');
					    	            			
					    	            			if(parseInt($scope.contractId) == 0)
					    	            			{
					    	            				$scope.contractId = null;
					    	            			}
					    	            			var obj = {
					    	            					"encounterId" :$scope.encounterDetails.encounterId,
					    	            					"contractId" :$scope.contractId,
					    	            					"serviceId" :$item.serviceMasterId,
					    	            					"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
					    	            					"organizationId":$scope.organizationId,
					    	            					"unitId":$scope.unitId,
					    	            					"visitTypeId":1,
					    	            					"billingBedCategoryId":$scope.opdBillingCatId,
					    	            					"patientCategoryId":$scope.patientDetails.patientTypeId,
					    	            					"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    	            				    "selfPayeeId" :$scope.selfPayeeId,
					    	            				    "patientClassId":$scope.encounterDetails.patientClassId,
					    	            				    "quantity":1,
					    	            				    "patientId":$scope.patientDetails.patientId,
					    	            				    "tariffId" :$scope.defaultSelfTariffId,
					    	            				    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
					    	            				    "taxPercentage":$item.taxPercentage,
					    	            				};
					    	            			
					    	            			// console.log('Base prise',obj)
					    	            			$rootScope.startLoaderPopup();
					    	            			var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
					    	            			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    	            				if(response.data.status=="success")
					    	            				{
					    	            					
					    	            					if(response.data.object != "undefined" && response.data.object != null)
					    	            					{
					    	            						if(response.data.object.doesNotExist==null)
						            							{
					    	            							$scope.forServicePayeeId = response.data.object.payeeId;
					        	            						$item.basePrice=response.data.object.serviceRate;
					        	            						$item.oldBasePrice =response.data.object.serviceRate;
					        	            						$item.dummyServiceAmt=response.data.object.serviceRate;
					        	            						$item.coPayPer= response.data.object.coShare !== undefined && !isNaN(response.data.object.coShare) ? response.data.object.coShare : "0";
					        	            						$item.quantity=1;
					        	            						$item.coShare = response.data.object.coShare;
					        	            						$item.selfPayable = response.data.object.selfPayable;
					        	            						$item.creditPayable = response.data.object.creaditPayable;
					        	            						$item.netPay = response.data.object.netAmount;
					        	            						$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
					        	            						$item.contractId = response.data.object.contractId;
					        	            						$item.patientClassPercentage = response.data.object.patientClassPercentage;
					        	            						$item.markupStatus = response.data.object.markupStatus;
					        	            						$item.minRateEditable = response.data.object.minRateEditable;
					        	            						$item.maxRateEditable = response.data.object.maxRateEditable;
					        	            						$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
					        	            						$item.isRateEditable=response.data.object.isRateEditable;
					        	            						$item.isQuantityEditable=response.data.object.isQuantityEditable;
					        	            						$item.tariffId=response.data.object.tariffId;
					        	            						$item.ordTotalAmount = response.data.object.ordTotalAmount;
					        	            						$item.concession = response.data.object.concession;
					        	            						$item.isDiscountApplicable = response.data.object.isDiscountApplicable;
					        	            						$item.patientClassPercentage = response.data.object.patientClassPercentage;
					        	            						$item.taxAmt = response.data.object.taxAmt;
					        	            						$item.taxPercentage = response.data.object.taxPercentage;
					        	            						$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
					        	            						$item.bedCategoryId=response.data.object.billingBedCategoryId;
					        	            						$item.isAutoRender = (response.data.object.isAutoRender == 'Y' ? 'Y':'N');
					        	            						$item.serviceRendered = (response.data.object.isAutoRender == 'Y'?1:0);
					        	            						$item.freeTextOrderedServicesId = $scope.freeTextOrderedServicesId;
					        	            						$item.freeTextserviceName = $scope.freeTextserviceName;
					        	            						$scope.servicePopupCalculate($item);
					        	            						
					        	            						angular.forEach($scope.freeTextServiceList, function (value, key) {
					        	            							if(freeTextOrderedServicesId==value.freeTextOrderedServicesId){
					        	            									value.status = 'I';
					        	            								}
					        	            							});
					        	            						$scope.freeTextserviceName = "";
					        	            						$scope.freeTextOrderedServicesId= null;
					        	            						$rootScope.stopLoaderPopup();
					        	            						$scope.serviceCheckFlag = false;
					        	            						$scope.serviceCheckFlagForParameter=false;
						            							}else{
						            								$rootScope.stopLoaderPopup();
					    	            							growl.warning("Service is not mapped with the tariff..!!",{
					    	            								title : WARNING_MSG
					    	            							});
					    	            							$scope.serviceCheckFlag = false;
					    	            							$scope.serviceCheckFlagForParameter=false;
					    	            							return false;
						            							}
					    	            						
					    	            					}
					    	            					else
					    	            						{
					    	            							$rootScope.stopLoaderPopup();
					    	            							growl.warning("Please add base price of this service and then add..!!",{
					    	            								title : ERROR_MSG
					    	            							});
					    	            							$scope.serviceCheckFlag = false;
					    	            							$scope.serviceCheckFlagForParameter=false;
					    	            							return false;
					    	            						}
					    	            					$scope.isFromAddServiceToTempServiceList=false;
					    	            					$scope.serviceCheckFlag=false;
					    	            					$scope.serviceCheckFlagForParameter=false;
					    	            					// concession
					    	            					// console.log("FINAL ITEM",$item);
					    	            				 }
					
					            						$scope.serviceSpecialityId=undefined;
					            						$scope.serviceSubSpecialityId=undefined;
					    	            			});
					    	            			
					    	            			
							    	           }
									       
									});
		
      	           }else{
  	            	 
   	            	$scope.item=$item;
   	      			$scope.mod=$model;
   	      			$scope.labl=$label;
   	      			angular.element('#checkParameter').modal('show');
   	        	    return false;
   	            	 
   	             }
      		       
  		      });
	}
    
    // chetana
    $scope.item={};
	$scope.mod="";
	$scope.labl="";
	$scope.getTariffList = function(unitId, orgId)
    {
		var obj = {
				"organizationId":orgId,
				"unitId":unitId
			};
	var URI = BASE_URL + ROOT_URL
	+ GETTARIFFMASTERLISTOPDBILLING;
	BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
		if(response.data.status=="success")
		{
			$scope.tariffList=response.data.listObject;
		}
	});
    }
	$scope.changeTariffRate=function($item,tariffId,serviceIdx)
	{
		$scope.getNewTariffRate($item,tariffId,serviceIdx)
	}
	$scope.getNewTariffRate = function($item,tariffId,serviceIdx)
    {
    	            			var obj = {
    	            					"encounterId" :$scope.encounterDetails.encounterId,
    	            					"contractId" :$scope.contractId,
    	            					"serviceId" :$item.serviceMasterId,
    	            					"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
    	            					"organizationId":$scope.organizationId,
    	            					"unitId":$scope.unitId,
    	            					"visitTypeId":1,
    	            					"billingBedCategoryId":$scope.opdBillingCatId,
    	            					"patientCategoryId":$scope.patientDetails.patientTypeId,
    	            					"defaultSelfTariffId":$scope.defaultSelfTariffId,
    	            				    "selfPayeeId" :$scope.selfPayeeId,
    	            				    "patientClassId":$scope.encounterDetails.patientClassId,
    	            				    "quantity":1,
    	            				    "patientId":$scope.patientDetails.patientId,
    	            				    "tariffId" :tariffId!=undefined && tariffId!=null ?  tariffId:$scope.defaultSelfTariffId,
    	            				    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
    	            				    "taxPercentage":$item.taxPercentage,
    	            				};
    	            			
    	            			// console.log('Base prise',obj)
    	            			$rootScope.startLoaderPopup();
    	            			var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobalAfterTariffChange";
    	            			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    	            				if(response.data.status=="success")
    	            				{
    	            					if(response.data.object != "undefined" && response.data.object != null)
    	            						{
    	            						var oldService=$item;
    	            						if(response.data.object.doesNotExist==null)
	            							{
    	            							console.log("defaultSelfTariffId",$scope.defaultSelectedTariffId)
    	            							if(response.data.object.serviceRate==null)
    	            								{
    	            								growl.warning("Selected Tariff Is Not Mapped With Service");
    	            								oldService.tariffId=oldService.oldTariffId;
    	            								$scope.getNewTariffRate($item,oldService.oldTariffId,serviceIdx);
    	            								return false;
    	            								}
    	            							else
    	            								{
    	            								oldService.oldTariffId=oldService.tariffId
    	            								oldService.basePrice=response.data.object.serviceRate;
        	            							$scope.getBasePriceCal(oldService,serviceIdx)
    	            								}
//    	            							$rootScope.stopLoaderPopup();
        	            					}else{
	            								$rootScope.stopLoaderPopup();
    	            							growl.warning("Service is not mapped with the tariff..!!",{
    	            								title : WARNING_MSG
    	            							});
    	            							return false;
	            							}
	            						}
    	            						
    	            				 }
    	            				else
    	            					{
    	            					  $rootScope.stopLoaderPopup();
    	            					}

    	            						$scope.serviceSpecialityId=undefined;
    	            						$scope.serviceSubSpecialityId=undefined;
    	            					});
    	            			
		       
	}
    $scope.addServiceFromSearch = function($item, $model, $label,flag,tariffId)
    {
    	
    	
    	if($item.preRequisite == 'Y' && $scope.serviceCheckFlag==false){
    		$scope.preRequisiteDetails = $item.preRequisiteDetails;
    		angular.element('#preRequisitePopup').modal('show');
    		
    		
    		$timeout(function () {
    			angular.element('#preRequisitePopup').modal('hide');
   		    }, 10000);
    		
    	}
    	$scope.isFromAddServiceFromSearch=true;
    	$scope.isFinancialClearence ='N';
    	$scope.financialRequestSendTo = "";
    	$scope.item={};
		$scope.mod="";
		$scope.labl="";
        var serviceObj = $scope.autoRenderedServicesFinalList.find(obj => {return obj.serviceMasterId == $item.serviceMasterId})
    	if(serviceObj != undefined && $scope.serviceCheckFlag==false){
    		   
    		  $scope.item=$item;
    		  if($scope.item.packageTypeId==5)
    			  {
    			  return false;
    			  }
  			$scope.mod=$model;
  			$scope.labl=$label;
  			angular.element('#pendingServiceAdd').modal('show');
    	         return false;
    	}
    	
        var serviceMasterIds = [];
        angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
        		serviceMasterIds.push(value.serviceMasterId);
			});	
        
        var promise = $scope.checkServiceWithTestParameter($item.serviceMasterId,serviceMasterIds);
        
        promise.then(function(obj1) 
				{
    	             if(obj1==0)
    	             {
        
				    	$scope.servicesListPopup = [];
				    	var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
						promise.then(function(obj) 
								{
				    	             if(obj>0)
				    	             {
				    	            	 if($scope.serviceSpecialityId==undefined){
				    	            			angular.forEach($scope.specialityMasterList, function (value, key) {
				    	            				if($item.specialityId==value.specialityId){
				    	            					$scope.serviceSpecialityId=$item.specialityId.toString();
				    	            					$scope.getSubSpecialityListById($item.specialityId);
				    	            					}
				    	            				});		
				    	            			}
				    	            			if($scope.serviceSubSpecialityId==undefined){
				    	            				angular.forEach($scope.subSpecialityMaterList, function (value, key) {
				    	            					if($item.subSpecialityId==value.subSpecialityMasterId){
				    	            						$scope.serviceSubSpecialityId=$item.subSpecialityId.toString();
				    	            						}
				    	            					});
				    	            			}
				    	            			$scope.forServicePayeeId  = $scope.selfPayeeId;
				    	            			$scope.search = '';
				    	            			$('#searchServiceId').val('');
				    	            			
				    	            			if(parseInt($scope.contractId) == 0)
				    	            			{
				    	            				$scope.contractId = null;
				    	            			}
				    	            			var obj = {
				    	            					"encounterId" :$scope.encounterDetails.encounterId,
				    	            					"contractId" :$scope.contractId,
				    	            					"serviceId" :$item.serviceMasterId,
				    	            					"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
				    	            					"organizationId":$scope.organizationId,
				    	            					"unitId":$scope.unitId,
				    	            					"visitTypeId":1,
				    	            					"billingBedCategoryId":$scope.opdBillingCatId,
				    	            					"patientCategoryId":$scope.patientDetails.patientTypeId,
				    	            					"defaultSelfTariffId":$scope.defaultSelfTariffId,
				    	            				    "selfPayeeId" :$scope.selfPayeeId,
				    	            				    "patientClassId":$scope.encounterDetails.patientClassId,
				    	            				    "quantity":1,
				    	            				    "patientId":$scope.patientDetails.patientId,
				    	            				    "tariffId" :$scope.defaultSelfTariffId,
				    	            				    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
				    	            				    "taxPercentage":$item.taxPercentage,
				    	            				};
				    	            			
				    	            			$rootScope.startLoaderPopup();
				    	            			var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
				    	            			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				    	            				if(response.data.status=="success")
				    	            				{
				    	            					if(response.data.object != "undefined" && response.data.object != null)
				    	            						{
				
				    	            						if(response.data.object.doesNotExist==null)
					            							{
				    	            							$scope.forServicePayeeId = response.data.object.payeeId;
				        	            						var item = response.data.object;
				        	            						item.quantity=1;
				        	            						item.isAutoRender = (item.isAutoRender == 'Y' ? 'Y':'N');
				        	            						item.serviceRendered = (item.isAutoRender == 'Y'?1:0);
				        	            						
				        	            						item.basePrice=response.data.object.serviceRate;
				        	            						item.oldBasePrice =response.data.object.serviceRate;
				        	            						item.dummyServiceAmt=response.data.object.serviceRate;
				        	            						item.coPayPer= response.data.object.coShare !== undefined && !isNaN(response.data.object.coShare) ? response.data.object.coShare : "0";
				        	            						item.coShare = response.data.object.coShare;
				        	            						item.selfPayable = response.data.object.selfPayable;
				        	            						item.creditPayable = response.data.object.creaditPayable;
				        	            						item.netPay = response.data.object.netAmount;
				        	            						item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
				        	            						item.contractId = response.data.object.contractId;
				        	            						item.patientClassPercentage = response.data.object.patientClassPercentage;
				        	            						item.markupStatus = response.data.object.markupStatus;
				        	            						item.minRateEditable = response.data.object.minRateEditable;
				        	            						item.maxRateEditable = response.data.object.maxRateEditable;
				        	            						item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
				        	            						item.isRateEditable=response.data.object.isRateEditable;
				        	            						item.isQuantityEditable=response.data.object.isQuantityEditable;
				        	            						item.tariffId=response.data.object.tariffId;
				        	            						item.ordTotalAmount = response.data.object.ordTotalAmount;
				        	            						item.concession = response.data.object.concession;
				        	            						item.isDiscountApplicable = response.data.object.isDiscountApplicable;
				        	            						item.patientClassPercentage = response.data.object.patientClassPercentage;
				        	            						item.medallServiceId = response.data.object.medallServiceId;
				        	            						item.taxAmt = response.data.object.taxAmt;
				        	            						item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
				        	            						item.bedCategoryId=response.data.object.billingBedCategoryId;
				        	            						item.isAutoRender = (response.data.object.isAutoRender == 'Y' ? 'Y':'N');
				        	            						item.serviceRendered = (response.data.object.isAutoRender == 'Y'?1:0);
				        	            						// for test
				        	            						var serviceId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
				        	            			    		
				        	            						var newArr = {}; 	
				        	            			    		newArr.dummyServiceAmt = item.dummyServiceAmt;
				        	            			        	newArr.serviceSpecialityId = item.specialityId;
				        	            			    		newArr.serviceSubSpecialityId = item.subSpecialityId;
				        	            			    		newArr.subSpecialityId = item.subSpecialityId;
				        	            			        	newArr.taxId = item.taxId;
				        	            			    		newArr.serviceMasterId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
				        	            			    		newArr.serviceStandardCode = typeof(item.serviceStandardCode) != "undefined" ? item.serviceStandardCode  : item.serviceCode ;
				        	            			    		newArr.serviceStandardCode = typeof(item.isPackageService) != "undefined" && item.isPackageService == "Y" ? (newArr.serviceStandardCode + " (P)") : newArr.serviceStandardCode;
				        	            			    		newArr.serviceStandardName = typeof(item.serviceStandardName) != "undefined" ? item.serviceStandardName : item.serviceName;
				        	            			    		newArr.isDiscount = typeof(item.isDiscount) != "undefined" ? item.isDiscount : 'N';
				        	            			    		newArr.isDiscountApplicable = typeof(item.isDiscountApplicable) != "undefined" ? item.isDiscountApplicable : 'N';
				        	            			    		newArr.isQuantityEditable = typeof(item.isQuantityEditable) != "undefined" ? item.isQuantityEditable : 'N';
				        	            			    		newArr.isRateEditable = typeof(item.isRateEditable) != "undefined" ? item.isRateEditable : 'N';
				        	            			    		newArr.coPayPer = item.coPayPer !== undefined && !isNaN(item.coPayPer) ? item.coPayPer : "0";
				        	            			    		newArr.minRateEditable = typeof(item.minRateEditable) != "undefined" ? item.minRateEditable : 0;
				        	            			    		newArr.maxRateEditable = typeof(item.maxRateEditable) != "undefined" ? item.maxRateEditable : 0;
				        	            			    		newArr.specialityName = item.specialityName;
				        	            			    		newArr.subSpecialityName = item.subSpecialityName;
				        	            			    		newArr.quantity = 1;
				        	            			    		newArr.basePrice = typeof(item.basePrice) != "undefined" ? item.basePrice : 0;
				        	            			    		newArr.basePriceOld = typeof(item.basePrice) != "undefined" ? item.basePrice: 0;
				        	            			    		newArr.oldBasePrice =item.oldBasePrice;
				        	            			    		newArr.totalAmt = parseFloat(newArr.quantity) * parseFloat(newArr.basePrice).toFixed(2);
				        	            			    		newArr.concession = typeof(item.concession) != "undefined" ? parseFloat(item.concession).toFixed(2) : parseFloat(0).toFixed(2);
				        	            			    		newArr.patientClassPercentage = typeof(item.patientClassPercentage) != "undefined" ? parseFloat(item.patientClassPercentage).toFixed(2) : parseFloat(0).toFixed(2);
				        	            			    		newArr.discountPercentage = 0;
				        	            			    		newArr.discount =0;
				        	            			    		newArr.doctorId = ($item.doctorId != undefined && $item.doctorId != null ) ? $item.doctorId : '';
				        	            			    		newArr.specialityId = '';
				        	            			    		newArr.doctorSpecialityId = ($item.doctorSpecialityId != undefined && $item.doctorSpecialityId != null ) ? $item.doctorSpecialityId : ''; 
				        	            			    		newArr.isDoctorRequired = typeof(item.isDocReq) != "undefined" ?item.isDocReq:'N'; 
				        	            			    		newArr.discountDetailsList = [];
				        	            			    		newArr.taxName = typeof(item.taxName) != "undefined" ? item.taxName : "-";
				        	            			    		newArr.taxAmt = typeof(item.taxAmt) != "undefined" ? item.taxAmt : 0;
				        	            			    		newArr.taxId = typeof(item.taxId) != "undefined" ? item.taxId : null;
				        	            			    		newArr.taxPercentage = typeof(item.taxPercentage) != "undefined" ? item.taxPercentage : 0;
				        	            			    		newArr.selfPayable =  item.selfPayable;
				        	            			    		newArr.creditPayable =    item.creditPayable;
				        	            			    		newArr.netPay =    item.netPay;
				        	            			    		newArr.ordTotalAmount = item.ordTotalAmount;
				        	            			    		newArr.patientClassPercentage = typeof(item.patientClassPercentage) != "undefined" ? item.patientClassPercentage : 0;
				        	            			    		newArr.isAutoRender =  item.isAutoRender;
				        	            			    		newArr.serviceRendered = item.serviceRendered;
				        	            			    		newArr.serviceRenderingDeptId = item.subSpecialityId;
				        	            			    		newArr.createdBy  = $scope.userId;
				        	            			    		newArr.createdDate = new Date().getTime();
				        	            			    		newArr.apportionedPrice = (typeof(item.apportionedPrice) != "undefined" && item.apportionedPrice != null) ? item.apportionedPrice : null;
				        	            			    		newArr.isPackageService = typeof(item.isPackageService) != "undefined" ? item.isPackageService : "";
				        	            			    		newArr.packageId = (typeof(item.packageId) != "undefined" && item.packageId != null) ? item.packageId : item.packageMasterId;
				        	            			    		newArr.packageTypeId = (typeof(item.packageTypeId) != "undefined" && item.packageTypeId != null) ? item.packageTypeId : null; 
				        	            			    		newArr.packageEitherOrGroupId = (typeof(item.packageEitherOrGroupId) != "undefined" && item.packageEitherOrGroupId != null) ? item.packageEitherOrGroupId : null; 
				        	            			    		newArr.packageCapDetailsId = item.packageCapDetailsId;
				        	            			    		newArr.isCapService = item.isCapService;
				        	            			    		newArr.packageConsumptionAmt = item.packageConsumptionAmt;
				        	            			    		newArr.status = 'A';
				        	            			    		newArr.payeeId = $scope.forServicePayeeId;
				        	            			    		newArr.isRefDoctorShare = item.isRefReq;
				        	            			    		newArr.packageEitherOrGroupId = item.packageEitherOrGroupId;
				        	            			    		newArr.coShare = item.coShare;
				        	            			    		newArr.contractId = item.contractId;
				        	            			    		newArr.isTariffRateApplicable = item.isTariffRateApplicable; // 12-10-2019
				        	            			    		newArr.markupStatus = item.markupStatus;
				        	            			    		newArr.orderDetailsPayeeMapperDtosList = item.orderDetailsPayeeMapperDtosList;
				        	            			    		newArr.orderDetailsPackageMapperDtosList = item.orderDetailsPackageMapperDtosList;
				        	            			    		newArr.tariffId = item.tariffId;
				        	            			    		newArr.isFromAddservice = 'N';
				        	            			    		newArr.isFromServiceSerach = 'Y';
				        	            			    		newArr.medallServiceId = item.medallServiceId;
				        	            			    		newArr.bedCategoryId = item.bedCategoryId;
				        	            			    		newArr.discountOwnership = $scope.defaultDiscountOwnership;
				        	            			    		if($scope.tariffList.length>0)
				        	            						{
				        	            							newArr.tariffList=[];
				        	            							angular.forEach($scope.tariffList, function (value, key) {
				        	            								newArr.tariffList.push(value);
				        	            							});
				        	            						}
				        	            			    		if($scope.selectedServices!=undefined && $scope.selectedServices!=null && $scope.selectedServices.length>0)
				    	            			    			{
				    	            			    			$scope.servicesListPopup=[];
				    	            			    			}
				        	            			    		$scope.servicesListPopup.push(newArr);
				        	            			    		
				        	            			    		if($scope.servicesListPopup.length > 0){
				        	            			    		 $scope.addTempServiesToServiceMasterList();
				        	            			    		}
				        	            			    		$scope.serviceCheckFlag=false;
				        	            			    		$scope.serviceCheckFlagForParameter=false;
				        	            			    		
				        	            					}else{
					            								$rootScope.stopLoaderPopup();
				    	            							growl.warning("Service is not mapped with the tariff..!!",{
				    	            								title : WARNING_MSG
				    	            							});
				    	            							$scope.serviceCheckFlag==false
				    	            							$scope.serviceCheckFlagForParameter=false;
				    	            							return false;
					            							}
					            						}
				    	            						
				    	            				 }
				    	            				else
				    	            					{
				    	            					  $rootScope.stopLoaderPopup();
				    	            					}
				    	            						$scope.serviceCheckFlag==false
				    	            						$scope.serviceSpecialityId=undefined;
				    	            						$scope.serviceSubSpecialityId=undefined;
				    	            						$scope.serviceCheckFlagForParameter=false;
				    	            					});
				    	            			
				    	           }
						       
								});
    	             }else{
    	            	 
    	            	$scope.item=$item;
    	      			$scope.mod=$model;
    	      			$scope.labl=$label;
    	      			angular.element('#checkParameter').modal('show');
    	        	    return false;
    	            	 
    	             }
    
				});
	}
    
    
    
    
    $scope.servicePopupCalculate = function(item)
    {
    	var serviceId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
    		var newArr = {}; 	
    		newArr.dummyServiceAmt = item.dummyServiceAmt;
        	// newArr.specialityId = item.specialityId;
        	newArr.serviceSpecialityId = item.specialityId;
    		newArr.serviceSubSpecialityId = item.subSpecialityId;
    		newArr.subSpecialityId = item.subSpecialityId;
        	newArr.taxId = item.taxId;
    		newArr.serviceMasterId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
    		newArr.serviceStandardCode = typeof(item.serviceStandardCode) != "undefined" ? item.serviceStandardCode  : item.serviceCode ;
    		newArr.serviceStandardCode = typeof(item.isPackageService) != "undefined" && item.isPackageService == "Y" ? (newArr.serviceStandardCode + " (P)") : newArr.serviceStandardCode;
    		newArr.serviceStandardName = typeof(item.serviceStandardName) != "undefined" ? item.serviceStandardName : item.serviceName;
    		newArr.isDiscount = typeof(item.isDiscount) != "undefined" ? item.isDiscount : 'N';
    		newArr.isDiscountApplicable = typeof(item.isDiscountApplicable) != "undefined" ? item.isDiscountApplicable : 'N';
    		newArr.isQuantityEditable = typeof(item.isQuantityEditable) != "undefined" ? item.isQuantityEditable : 'N';
    		newArr.isRateEditable = typeof(item.isRateEditable) != "undefined" ? item.isRateEditable : 'N';
    		newArr.coPayPer = item.coPayPer !== undefined && !isNaN(item.coPayPer) ? item.coPayPer : "0";
    		newArr.minRateEditable = typeof(item.minRateEditable) != "undefined" ? item.minRateEditable : 0;
    		newArr.maxRateEditable = typeof(item.maxRateEditable) != "undefined" ? item.maxRateEditable : 0;
    		newArr.specialityName = item.specialityName;
    		newArr.subSpecialityName = item.subSpecialityName;
    		newArr.quantity = 1;
    		newArr.basePrice = typeof(item.basePrice) != "undefined" ? item.basePrice : 0;
    		newArr.basePriceOld = typeof(item.basePrice) != "undefined" ? item.basePrice: 0;
    		newArr.oldBasePrice =item.oldBasePrice;
    		newArr.totalAmt = parseFloat(newArr.quantity) * parseFloat(newArr.basePrice).toFixed(2);
    		newArr.concession = typeof(item.concession) != "undefined" ? parseFloat(item.concession).toFixed(2) : parseFloat(0).toFixed(2);
    		newArr.patientClassPercentage = typeof(item.patientClassPercentage) != "undefined" ? parseFloat(item.patientClassPercentage).toFixed(2) : parseFloat(0).toFixed(2);
    		newArr.discountPercentage = 0;
    		newArr.discount =0;
    		newArr.doctorId = '';
    		newArr.specialityId = '';
    		newArr.doctorSpecialityId = '';
    		newArr.isDoctorRequired = typeof(item.isDoctorRequired) != "undefined" ?item.isDoctorRequired:'N'; 
    		newArr.discountDetailsList = [];
    		newArr.taxName = typeof(item.taxName) != "undefined" ? item.taxName : "-";
    		newArr.taxAmt = typeof(item.taxAmt) != "undefined" ? item.taxAmt : 0;
    		newArr.taxId = typeof(item.taxId) != "undefined" ? item.taxId : null;
    		newArr.taxPercentage = typeof(item.taxPercentage) != "undefined" ? item.taxPercentage : 0;
    		newArr.selfPayable =  item.selfPayable;
    		newArr.creditPayable =    item.creditPayable;
    		newArr.netPay =    item.netPay;
    		newArr.ordTotalAmount = item.ordTotalAmount;
    		newArr.patientClassPercentage = typeof(item.patientClassPercentage) != "undefined" ? item.patientClassPercentage : 0;
    		/*
			 * if($scope.isPreOrPostBilling == 'N') { newArr.serviceRendered =
			 * 1; } else { newArr.serviceRendered = "0"; }
			 */
    		newArr.isAutoRender =  item.isAutoRender;
    		newArr.serviceRendered = item.serviceRendered;
    		newArr.serviceRenderingDeptId = item.subSpecialityId;
    		newArr.createdBy  = item.createdBy;
    		newArr.createdDate = new Date().getTime();
    		newArr.apportionedPrice = (typeof(item.apportionedPrice) != "undefined" && item.apportionedPrice != null) ? item.apportionedPrice : null;
    		newArr.isPackageService = typeof(item.isPackageService) != "undefined" ? item.isPackageService : "";
    		newArr.packageId = (typeof(item.packageId) != "undefined" && item.packageId != null) ? item.packageId : item.packageMasterId;
    		newArr.packageTypeId = (typeof(item.packageTypeId) != "undefined" && item.packageTypeId != null) ? item.packageTypeId : null; 
    		newArr.packageEitherOrGroupId = (typeof(item.packageEitherOrGroupId) != "undefined" && item.packageEitherOrGroupId != null) ? item.packageEitherOrGroupId : null; 
    		newArr.packageCapDetailsId = item.packageCapDetailsId;
    		newArr.isCapService = item.isCapService;
    		newArr.packageConsumptionAmt = item.packageConsumptionAmt;
    		newArr.status = 'A';
    		newArr.payeeId = $scope.forServicePayeeId;
    		newArr.isRefDoctorShare = item.isRefDoctorShare;
    		newArr.packageEitherOrGroupId = item.packageEitherOrGroupId;
    		newArr.coShare = item.coShare;
    		newArr.contractId = item.contractId;
    		newArr.isTariffRateApplicable = item.isTariffRateApplicable; // 12-10-2019
    		newArr.markupStatus = item.markupStatus;
    		newArr.orderDetailsPayeeMapperDtosList = item.orderDetailsPayeeMapperDtosList;
    		newArr.orderDetailsPackageMapperDtosList = item.orderDetailsPackageMapperDtosList;
    		newArr.tariffId = item.tariffId;
    		newArr.isFromAddservice = 'N';
    		newArr.bedCategoryId = item.bedCategoryId;
    		newArr.freeTextOrderedServicesId = item.freeTextOrderedServicesId;
    		newArr.freeTextserviceName = item.freeTextserviceName;
    		newArr.medallServiceId = item.medallServiceId;
    		$scope.servicesListPopup.push(newArr);
    		
    		$scope.servicesListPopup = $scope.servicesListPopup.reverse();
    		
    	    $scope.servicePopupCalculateFinal($scope.servicesListPopup); 
    }
    
    $scope.servicePopupCalculateFinal = function(itemObj)
    {
    	$scope.AutoRenderedTotalAmountSummaryPopUp = 0;
    	angular.forEach($scope.servicesListPopup, function (value, key) {
		$scope.AutoRenderedTotalAmountSummaryPopUp=(parseFloat($scope.AutoRenderedTotalAmountSummaryPopUp) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		});
		$scope.totalAmountSummaryPopup = $scope.AutoRenderedTotalAmountSummaryPopUp;
    }
    

    $scope.removeAllServices = function(){
    	$scope.servicesListPopup = [];
    }
    
    $scope.removeTempServicesByIndex = function(index)
    {
    	angular.forEach($scope.freeTextServiceList, function (value, key) {
			if($scope.servicesListPopup[index].freeTextOrderedServicesId!=undefined && $scope.servicesListPopup[index].freeTextOrderedServicesId!= null && $scope.servicesListPopup[index].freeTextOrderedServicesId==value.freeTextOrderedServicesId){
					value.status = 'A';
				}
			});
    	
    	$scope.addCapAmout($scope.servicesListPopup[index],$scope.autoRenderedServicesFinalList);
    	$scope.totalAmountSummaryPopup = parseFloat($scope.totalAmountSummaryPopup - ($scope.servicesListPopup[index].basePrice * $scope.servicesListPopup[index].quantity)).toFixed(2);
		$scope.servicesListPopup.splice(index, 1);
	}
    
    $scope.getQuantityCalPopup = function(itemObj,idx)
    {
    	if(itemObj.quantity == '' || itemObj.quantity ==null || itemObj.quantity == undefined || itemObj.quantity==0)
    	{
    		$scope.servicesListPopup[idx].quantity = parseFloat(1);
    		growl.warning("Quantity cannot be zero or empty..!!",
    	    		{
    					title : WARNING_MSG
    				});
    	    		return false;
    	   		
    	    		
    	}else{
    	
    			$scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
    			var autoRenderedServicesList = $scope.servicesListPopup;
       	 		$scope.servicePopupCalculateFinal(autoRenderedServicesList);
    	}
    }
    
  
    $scope.getBasePriceCalPopup = function(itemObj,idx)
    {
    			if(itemObj.basePrice == '' || itemObj.basePrice == null || itemObj.basePrice == undefined){
    				itemObj.basePrice = 0;
    			}
	    		/*
				 * var basepriceNew = parseFloat(itemObj.basePrice); var
				 * basePriceOld = parseFloat(itemObj.basePriceOld).toFixed(2);
				 * var minRate = itemObj.minRateEditable; var maxRate =
				 * itemObj.maxRateEditable; var minRatePerAmount =
				 * ((basePriceOld*minRate)/100).toFixed(2); var maxRatePerAmount =
				 * ((basePriceOld*maxRate)/100).toFixed(2); var minPerDiff =
				 * parseFloat(basePriceOld) - parseFloat(minRatePerAmount); var
				 * maxPerAdd = parseFloat(basePriceOld) +
				 * parseFloat(maxRatePerAmount); if(minPerDiff >basepriceNew) {
				 * growl.warning("Unit price should be Minimum Rs."+minPerDiff, {
				 * title : ERROR_MSG }); $scope.servicesListPopup[idx].basePrice =
				 * basePriceOld; return false; }
				 * 
				 * if(basepriceNew > maxPerAdd) { growl.warning("Unit price
				 * should be Maximun Rs."+maxPerAdd, { title : ERROR_MSG });
				 * $scope.servicesListPopup[idx].basePrice = basePriceOld;
				 * return false; }
				 */
	        	
    		  	if(itemObj.maxRateEditable!= null && itemObj.maxRateEditable < itemObj.basePrice){
    	    		 growl.warning(" You can edit rate max up to "+itemObj.maxRateEditable,{
    						title : ERROR_MSG
    					});
    	    		 itemObj.basePrice =  itemObj.basePriceOld;
    	    	}
    	    	
    	    	if(itemObj.minRateEditable!= null && itemObj.minRateEditable > itemObj.basePrice){
    	   		 growl.warning(" You can edit rate min up to "+itemObj.minRateEditable,{
    						title : ERROR_MSG
    					});
    	   		 itemObj.basePrice =  itemObj.basePriceOld;
    	   	}
    			
	        	$scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
    			var autoRenderedServicesList = $scope.servicesListPopup;
       	 		$scope.servicePopupCalculateFinal(autoRenderedServicesList);
    		
    }
    $scope.selectedServices=[];
    $scope.addTempServiesToServiceMasterList = function(){
    	
    	$scope.serviceSelectedList = [];
    	$scope.isMultiVisitPackageSelected=false;
    	$scope.isFinancialClearence ='N';
    	$scope.financialRequestSendTo = "";
		angular.forEach($scope.SelectedPackageListToAddPackageServices, function (packageList, key) {
			if(packageList.packageTypeId==5)
				{
				$scope.isMultiVisitPackageSelected=true;
				angular.forEach(packageList.packageServiceList, function (serviceList, key) {
					if(serviceList.isServiceSelected=="Y")
						{
						$scope.serviceSelectedList.push(serviceList.serviceMasterId);
						$scope.selectedServices.push(serviceList);
//						$scope.checkServiceMappedWithTest(serviceList.serviceMasterId,serviceList.serviceSpecialityId);
						}
					serviceList.isServiceSelected="N"
				});
				}
		});
        if($scope.isMultiVisitPackageSelected==true && $scope.serviceSelectedList.length==0)
        {
            $scope.SelectedPackageListToAddPackageServices=[];
        }
    	// For Surgecharge start
    	 angular.element('#addServicePopup').modal('hide');
    	 
    	 var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
		    if (indexT >= 0) {
		    	if($scope.autoRenderedServicesFinalList[indexT].orderDetailsId > 0){
		    		
		    		var data = {
		    				orderDetailsId : $scope.autoRenderedServicesFinalList[indexT].orderDetailsId,
		    				ordCancelReasonId : null,
		    				userId : $scope.userId
		    		}
		    		
		    		var URI = BASE_URL + ROOT_URL + "/api/billing/bill/service/cancel";
			    	
			    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			    		if(response.data.status == "success"){
                             growl.success(response.data.message,
                             {
                                 title : 'success'
                             });
			    		}else{
                             growl.error(response.data.message,
                             {
                                 title : 'error'
                             });
			    		}
			    	});
		    	
		    }else{
		    	$scope.autoRenderedServicesFinalList.splice(indexT, 1);
		    }
	}
    	// For Surgecharge end
    	
    	// console.log("$scope.autoRenderedServicesFinalList",$scope.autoRenderedServicesFinalList);
    		$scope.disLis = {};
    		$scope.servicesListPopup = $scope.servicesListPopup.reverse();
    		var indexList = [];
    		angular.forEach($scope.servicesListPopup, function (value, key) 
    		{
				//ServiceCount
    			if($scope.autoRenderedServicesFinalList != null){
                    $scope.ServiceCount = $scope.ServiceCount + parseInt(value.quantity);
                }
    			
    			if(value.serviceSpecialityId==$scope.labConfig.labSpecialityId || value.serviceSpecialityId==$scope.radiologySpecialityId || value.serviceSpecialityId==$scope.physiotherapySpecialityId || value.serviceSpecialityId==$scope.hemodialysisSpecialityId)	
    			{
    				for(var i=0;i<value.quantity;i++)
					{
		    			var docName = ''; 
		    			if(value.doctorId > 0){
		    				
		    				if($scope.doctorsList.length>0){
								docName = $.grep($scope.doctorsList, function (doc) {
			    	            return doc.doctorId == value.doctorId;
			    	        })[0].doctorName;
							}else if($scope.doctorsListNewPack.length>0){
								docName = $.grep($scope.doctorsListNewPack, function (doc) {
			    	            return doc.doctorId == value.doctorId;
			    	        })[0].doctorName;
							}
		    				
		    			}
		    			
		    			var temp = {
								serviceMasterId : $scope.surchargeServiceId
						}
		    			
		    			var newArr = {};
		    			newArr.priorityId  = '2';
		    			newArr.isOutsourced ='N';
		    			newArr.serviceSpecialityId = value.serviceSpecialityId;
		    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
		    			newArr.doctorId = value.doctorId;
		    			newArr.doctorSpecialityId = value.doctorSpecialityId;
		    			newArr.specialityId = value.doctorSpecialityId;
		    			newArr.bedCategoryId = value.bedCategoryId;
		    			newArr.isFromServiceSerach = value.isFromServiceSerach;
		    			newArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ? value.coPayPer : "0";
		    			newArr.isAutoRender =  value.isAutoRender;
		    			newArr.serviceRendered = value.serviceRendered;
		    			newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
		    			newArr.serviceMasterId = value.serviceMasterId;
		    			newArr.serviceStandardCode = value.serviceStandardCode;
		    			newArr.serviceStandardName = value.serviceStandardName;
		    			newArr.isDiscount = value.isDiscount;
		    			newArr.isDiscountApplicable = value.isDiscountApplicable;
		    			newArr.isTariffRateApplicable = value.isTariffRateApplicable;// 12-10-2019
		    			newArr.isQuantityEditable = value.isQuantityEditable;
		    			newArr.isRateEditable = value.isRateEditable;
		    			newArr.minRateEditable = value.minRateEditable;
		    			newArr.maxRateEditable = value.maxRateEditable;
		    			newArr.specialityName = value.specialityName;
		    			newArr.doctorName =docName;
		    			newArr.quantity = 1;
		    			newArr.basePrice = value.basePrice;
		    			newArr.basePriceOld = value.basePrice;
		    			newArr.oldBasePrice =value.oldBasePrice;
						newArr.medallServiceId = value.medallServiceId != null ? value.medallServiceId : '';
		    			newArr.discountDisabledFlag = 'Y';
		    			newArr.totalAmt = parseFloat(1) * parseFloat(value.basePrice).toFixed(2);
		    			newArr.concession = parseFloat(value.concession);
		    			newArr.patientClassPercentage = parseFloat(value.patientClassPercentage);
		    			newArr.taxAmt = parseFloat(value.taxAmt); 
		    			newArr.netPay = parseFloat(value.netPay); 
		    			newArr.ordTotalAmount = parseFloat(value.ordTotalAmount); 
		    			newArr.selfPayable = parseFloat(value.selfPayable); 
		    			newArr.selfPayableOldC =parseFloat(value.selfPayable); 
		    			newArr.creditPayable = parseFloat(value.creditPayable)
		    			newArr.creditPayableOldC =  parseFloat(value.creditPayable);
		    			newArr.discountPercentage = 0;
		    			newArr.discount = 0;
		    			newArr.packageConsumptionAmt = 0;
		    			newArr.taxId = value.taxId;
		    			newArr.taxName = value.taxName;
		    			newArr.taxPercentage = value.taxPercentage;
		    			newArr.discountDetailsList = [];
		    			newArr.oldNetPay = "0";
		    			newArr.createdBy  = value.createdBy;
		    			newArr.createdDate = new Date().getTime();
		    			newArr.isPackageService = value.isPackageService;
		    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
		    			newArr.isCapService = value.isCapService;
		    			newArr.subSpecialityId = value.subSpecialityId;
		    			newArr.packageId = value.packageId;
		    			newArr.packageTypeId = value.packageTypeId;
		    			newArr.ordCancelled = "N";
		    			newArr.status = "A";
		    			newArr.freeTextOrderedServicesId = value.freeTextOrderedServicesId
		    			/*
						 * newArr.ordCancelReasonId = '';
						 * newArr.ordCancelDatetime = new Date().getTime();
						 * newArr.ordCancelRemark = '';
						 */
		    			newArr.payeeId  = value.payeeId;
		    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
		    			newArr.coShare = value.coShare;
		    			newArr.contractId = value.contractId;
		    			newArr.isRefDoctorShare = value.isRefDoctorShare;
		    			newArr.patientClassPercentage = value.patientClassPercentage;
		        		newArr.markupStatus =value.markupStatus;
		        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
		        		newArr.isFromAddservice = 'Y';
		        		newArr.discountOwnership = $scope.defaultDiscountOwnership;
		        		// for pacakge
		        		newArr.orderDetailsPackageMapperDtosList = [{"id":"",
		    				"orderDetailsId":"",
		    				"serviceId":value.serviceMasterId,
		    				"packageId":'',
		    				"payeeId":"",
		    				"serviceAmount":value.basePrice,	
		    				"packageAmount":0,
		    				"packageCode":'',
		    			    "packageName":'',
		    				"isPackageService":"N",
		    				"isCapService":"N",
		    				"saleDetailsId":"",
		    				"itemId":"",
		    				"status":"A",
		    				"unitId":$scope.unitId,
		    				"organizationId": $scope.organizationId}];
		        		if($scope.tariffList.length>0)
		        			          {
		        			newArr.tariffList=[];
		        			angular.forEach($scope.tariffList, function (value1, key) {
		        				newArr.tariffList.push(value1);
		        			   });
		        	               }
		        		newArr.tariffId = value.tariffId;
		        		newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N'; 
		        		newArr.dummyServiceAmt = value.dummyServiceAmt;
		        		newArr.tariffList=value.tariffList;
		        		newArr.medallServiceId = value.medallServiceId;
                        newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
		    			$scope.autoRenderedServicesFinalList.push(newArr);
		    			
		    			var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(newArr);
		    			indexList.push(serviceIndex);
		    			if($scope.selectedServices!=undefined && $scope.selectedServices!=null && $scope.selectedServices.length>0)
		    			{
		    			$scope.autoRenderedServicesForSaveAndUpdate=[];
		    			}
		    			$scope.autoRenderedServicesForSaveAndUpdate.push(newArr);
		    		}
    			}else{
    				var docName = ''; 
	    			if(value.doctorId > 0){
	    	    	docName = $.grep($scope.doctorsList, function (doc) {
	    	            return doc.doctorId == value.doctorId;
	    	        })[0].doctorName;
	    			}
	    			
	    			var temp = {
							serviceMasterId : $scope.surchargeServiceId
					}
	    			
	    			var newArr = {};
	    			newArr.priorityId  = '2';
	    			newArr.isOutsourced ='N';
	    			newArr.serviceSpecialityId = value.serviceSpecialityId;
	    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
	    			newArr.doctorId = value.doctorId;
	    			newArr.doctorSpecialityId = value.doctorSpecialityId;
	    			newArr.specialityId = value.doctorSpecialityId;
	    			newArr.bedCategoryId = value.bedCategoryId;
	    			newArr.isFromServiceSerach = value.isFromServiceSerach;
	    			newArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ? value.coPayPer : "0";
	    			newArr.isAutoRender =  value.isAutoRender;
	    			newArr.serviceRendered = value.serviceRendered;
	    			newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
	    			newArr.serviceMasterId = value.serviceMasterId;
	    			newArr.serviceStandardCode = value.serviceStandardCode;
	    			newArr.serviceStandardName = value.serviceStandardName;
	    			newArr.isDiscount = value.isDiscount;
	    			newArr.isDiscountApplicable = value.isDiscountApplicable;
	    			newArr.isTariffRateApplicable = value.isTariffRateApplicable;// 12-10-2019
	    			newArr.isQuantityEditable = value.isQuantityEditable;
	    			newArr.isRateEditable = value.isRateEditable;
	    			newArr.minRateEditable = value.minRateEditable;
	    			newArr.maxRateEditable = value.maxRateEditable;
	    			newArr.specialityName = value.specialityName;
	    			newArr.doctorName =docName;
	    			newArr.quantity = value.quantity;
	    			newArr.basePrice = value.basePrice;
	    			newArr.basePriceOld = value.basePrice;
	    			newArr.oldBasePrice =value.oldBasePrice;
	    			newArr.discountDisabledFlag = 'Y';
	    			newArr.totalAmt = parseFloat(value.quantity) * parseFloat(value.basePrice).toFixed(2);
	    			newArr.concession = parseFloat(value.concession);
	    			newArr.patientClassPercentage = parseFloat(value.patientClassPercentage);
	    			newArr.taxAmt = parseFloat(value.quantity) * parseFloat(value.taxAmt); 
	    			newArr.netPay = parseFloat(value.quantity) * parseFloat(value.netPay); 
	    			newArr.ordTotalAmount =parseFloat(value.quantity) * parseFloat(value.ordTotalAmount); 
	    			newArr.selfPayable = parseFloat(value.quantity) * parseFloat(value.selfPayable); 
	    			newArr.selfPayableOldC =parseFloat(value.quantity) * parseFloat(value.selfPayable); 
	    			newArr.creditPayable = parseFloat(value.quantity) * parseFloat(value.creditPayable)
	    			newArr.creditPayableOldC =  parseFloat(value.quantity) * parseFloat(value.creditPayable);
	    			newArr.discountPercentage = 0;
	    			newArr.discount = 0;
	    			newArr.packageConsumptionAmt = 0;
	    			newArr.taxId = value.taxId;
	    			newArr.taxName = value.taxName;
	    			newArr.taxPercentage = value.taxPercentage;
	    			newArr.discountDetailsList = [];
	    			newArr.oldNetPay = "0";
	    			newArr.createdBy  = value.createdBy;
	    			newArr.createdDate = new Date().getTime();
	    			newArr.isPackageService = value.isPackageService;
	    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
	    			newArr.isCapService = value.isCapService;
	    			newArr.subSpecialityId = value.subSpecialityId;
	    			newArr.packageId = value.packageId;
	    			newArr.packageTypeId = value.packageTypeId;
	    			newArr.ordCancelled = "N";
	    			newArr.status = "A";
	    			newArr.freeTextOrderedServicesId = value.freeTextOrderedServicesId
	    			/*
					 * newArr.ordCancelReasonId = ''; newArr.ordCancelDatetime =
					 * new Date().getTime(); newArr.ordCancelRemark = '';
					 */
	    			newArr.payeeId  = value.payeeId;
	    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
	    			newArr.coShare = value.coShare;
	    			newArr.contractId = value.contractId;
	    			newArr.isRefDoctorShare = value.isRefDoctorShare;
	    			newArr.patientClassPercentage = value.patientClassPercentage;
	        		newArr.markupStatus =value.markupStatus;
	        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
	        		newArr.isFromAddservice = 'Y';
	        		newArr.discountOwnership = $scope.defaultDiscountOwnership;
	        		newArr.medallServiceId = value.medallServiceId;
	        		// for pacakge
	        		newArr.orderDetailsPackageMapperDtosList = [{"id":"",
	    				"orderDetailsId":"",
	    				"serviceId":value.serviceMasterId,
	    				"packageId":'',
	    				"payeeId":"",
	    				"serviceAmount":value.basePrice,	
	    				"packageAmount":0,
	    				"packageCode":'',
	    			    "packageName":'',
	    				"isPackageService":"N",
	    				"isCapService":"N",
	    				"saleDetailsId":"",
	    				"itemId":"",
	    				"status":"A",
	    				"unitId":$scope.unitId,
	    				"organizationId": $scope.organizationId}];
	        		
	        		newArr.tariffId = value.tariffId;
	        		newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N'; 
	        		newArr.dummyServiceAmt = value.dummyServiceAmt;
	        		newArr.tariffList=value.tariffList;
                    newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
	    			$scope.autoRenderedServicesFinalList.push(newArr);

	    			var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(newArr);
	    			indexList.push(serviceIndex);
	    			if($scope.selectedServices!=undefined && $scope.selectedServices!=null && $scope.selectedServices.length>0)
	    			{
	    			$scope.autoRenderedServicesForSaveAndUpdate=[];
	    			}
	    			$scope.autoRenderedServicesForSaveAndUpdate.push(newArr);
    			}
					
					
			
		});
    		
               angular.forEach($scope.selectedPackageList, function (value, key) 
            {
            	   
            	var newArr = {};
   				newArr.priorityId  = '2';
   				newArr.isOutsourced ='N';
   				newArr.doctorSpecialityId = value.doctorSpecialityId;
    			newArr.specialityId = value.doctorSpecialityId;
   				newArr.serviceSpecialityId = value.serviceSpecialityId;
   				newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
   				newArr.bedCategoryId = value.bedCategoryId;
   				newArr.serviceRendered = "0";
   				newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
   				newArr.serviceMasterId = value.serviceMasterId;
   				newArr.serviceStandardCode = value.serviceStandardCode;
   				newArr.serviceStandardName = value.serviceStandardName;
   				newArr.isDiscount = value.isDiscount;
   				newArr.isDiscountApplicable = value.isDiscountApplicable;
   				newArr.isQuantityEditable = value.isQuantityEditable;
   				newArr.isRateEditable = value.isRateEditable;
   				newArr.minRateEditable = value.minRateEditable;
   				newArr.maxRateEditable = value.maxRateEditable;
   				newArr.specialityName = value.specialityName;
   				newArr.quantity =1;
   				newArr.basePrice = value.basePrice;
   				newArr.basePriceOld = value.basePrice;
   				newArr.discountDisabledFlag = 'Y';
   				newArr.totalAmt = parseFloat(1) * parseFloat(value.basePrice).toFixed(2);
   				newArr.concession = parseFloat(value.concession);
   				newArr.patientClassPercentage = parseFloat(value.patientClassPercentage);
   				newArr.discountPercentage = value.discountPercentage;
   				newArr.discount = value.discount;
   				var dis =value.discount;
   				var disPer =value.discountPercentage;
   				newArr.taxId = value.taxId;
   				newArr.taxName = value.taxName;
   				newArr.taxPercentage = value.taxPercentage;
   				newArr.taxAmt = value.taxAmt;
   				newArr.discountDetailsList = [];
   				newArr.selfPayable = value.selfPayable;
   				newArr.creditPayable =   value.creditPayable;
   				newArr.netPay = parseFloat(value.netPay); 
   				newArr.ordTotalAmount = parseFloat(value.ordTotalAmount); 
   				newArr.oldNetPay = "0";
   				newArr.createdBy  = value.createdBy;
   				newArr.createdDate = new Date().getTime();
   				newArr.isPackage = true;
   				newArr.packageMasterId = value.packageMasterId;
   				newArr.packageTypeId = value.packageTypeId;
   				newArr.packageId = value.packageId;
   				newArr.isPackageService = value.isPackageService;
   				newArr.isCapService = value.isCapService;
       			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
       			newArr.subSpecialityId = value.subSpecialityId;
       			newArr.serviceRenderingDeptId = value.subSpecialityId;
       			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
       			newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
       			newArr.orderDetailsPackageMapperDtosList =(value.orderDetailsPackageMapperDtosList==null|| value.orderDetailsPackageMapperDtosList==undefined)?[]:value.orderDetailsPackageMapperDtosList;
       			newArr.isFromAddservice = 'Y';
       			newArr.tariffId = value.tariffId;
       			newArr.dummyServiceAmt = value.dummyServiceAmt;
       			newArr.selfPayableOldC = value.selfPayable;
       			newArr.creditPayableOldC =  value.creditPayable;
                newArr.payeeId  = value.payeeId;
                newArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ? value.coPayPer : "0";
                newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N';
                newArr.discountOwnership = value.discountOwnership;
                newArr.packageApplicableDate = moment(new Date()).format('DD-MM-YYYY');
                newArr.tariffList=[];
				newArr.medallServiceId = value.medallServiceId != null ? value.medallServiceId : '';
                newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;
   				$scope.autoRenderedServicesFinalList.push(newArr);
    	    	if(typeof(value.packageTypeId) != "undefined" && value.packageTypeId == "1"){
    						angular.forEach(value.packageServiceList,function(val,i)
    								{
    							var docName = ''; 
    			    			if(val.doctorId > 0){
    			    	    	docName = $.grep($scope.doctorsList, function (doc) {
    			                 	            return doc.doctorId == val.doctorId;
    			    	        })[0].doctorName;
    			    			}
    			    			val.doctorName = docName;
    			    			$scope.autoRenderedServicesFinalList.push(val);
    						})
    	    	 }
    		});
               
            /*
			 * $scope.billWiseDiscountObj.requestAmountBillWise = 0;
			 * $scope.requestDiscountDiv = false;
			 * 
			 * if($scope.billWiseDiscountObj.discountPercentageBillWise > 0){
			 * $scope.funDiscountPercentageBillWise(); }
			 */
               
               $scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				}
			 $scope.requestDiscountDiv = false;
               
				
               
               
    		if($scope.selectedPackageList !=null && $scope.selectedPackageList.length>0)
    			{
    			   $rootScope.startLoaderPopup();
    			   $scope.calculationTest($scope.autoRenderedServicesFinalList,addService);
    			   $scope.saveOrderPackage();
    			   $scope.selectedPackageList = [];
    			}
    		else 
    		   {
    			 angular.element('#addServicePopup').modal('hide');
    			 var listServicepopup = $scope.autoRenderedServicesFinalList;
    	    	 $scope.calculationTest(listServicepopup,addService);
    	    	 if(indexList.length > 0)
    	    		 $scope.addOrUpdateServices(null,indexList);
    		   }
     }
    
    $scope.getRemoveDiscountPopup = function(item,idxDis)
    {
    	$scope.objectIndexDiscount = idxDis;
    	$scope.discountObjectForRemove = item;
    }
    
    
    $scope.removeDiscount = function()
    {
    	var idx  = $scope.objectIndex;
    	var discIdx = $scope.objectIndexDiscount;
    	var item = $scope.discountObjectForRemove;
    	
    	// return false;
    	if(item.orderDetailsDiscountId > 0)
    		{
    			var disArr = {};
    			disArr.orderDetailsDiscountId  = item.orderDetailsDiscountId;
    			disArr.orderDetailsId = item.orderDetailsId;
    			disArr.discountCategoryId = item.discountCategoryId;
    			disArr.discountPercentage = item.discountPercentage;
    			disArr.discountAmount = item.discountAmount;
    			disArr.isCancel = 'Y';
    			disArr.cancelBy  = $scope.userId;
    			disArr.cancelReasonId  = $scope.discountReasonId;
    			disArr.remark = $scope.discountRemark;
    			disArr.createdBy  = item.createdBy;
    			disArr.createdDate  = item.createdDate;
    			disArr.updatedBy  = $scope.userId;
    			disArr.updatedDate  = new Date().getTime();
    			disArr.cancelledDate = new Date().getTime();
    			disArr.orgId  = $scope.organizationId;
    			disArr.unitId  = $scope.unitId;
    			disArr.discountDate = new Date().getTime();
    			disArr.status = "I";
    			disArr.isConsessionDiscount = "N";
    			$scope.removeDiscountList.push(disArr);
    		}
    	
    	
    	var currDisAmt = $scope.autoRenderedServicesFinalList[idx].discount;
    	var currDisPer = $scope.autoRenderedServicesFinalList[idx].discountPercentage;
    	
    	var finalDisAmt = (parseFloat(currDisAmt) - parseFloat(item.discountAmount)).toFixed(2);
    	var finalDisPer = (parseFloat(currDisPer) - parseFloat(item.discountPercentage)).toFixed(2);
    	
    	$scope.autoRenderedServicesFinalList[idx].discount = finalDisAmt;
    	$scope.autoRenderedServicesFinalList[idx].discountPercentage = finalDisPer;
    	
    	var serviceIndex = $scope.autoRenderedServicesFinalList[idx].discountDetailsList.indexOf(item);
    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.splice(serviceIndex, 1);
    	
    	if($scope.autoRenderedServicesFinalList[idx].discountDetailsList.length == 0)
    		{
    		$scope.autoRenderedServicesFinalList[idx].discountCategoryId = '';
    		}
    	
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	
    	$scope.calculationTest(autoRenderedServicesList,init);
    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);
    	$scope.discountReasonId = '';
    	$scope.discountRemark = '';
    	
    }
    
    $scope.addCapAmout = function(info,list)
	{
    	
    	if((info.contractId>0 && info.creditPayable>0)||info.orderDetailsPackageMapperDtosList.find(pkg => pkg.isPackageService=='Y')||info.orderDetailsId>0)
    	 {
    		var data = {
    				encounterId : $scope.encounterDetails.encounterId,
    				specialityId : info.serviceSpecialityId,
    				contractId : info.contractId,
    				orderDetailsId : info.orderDetailsId,
    				orderDetailsPayeeMapperDtosList : info.orderDetailsPayeeMapperDtosList,
    				orderDetailsPackageMapperDtosList : info.orderDetailsPackageMapperDtosList,
    				serviceId:info.serviceMasterId,
    				createdBy : $scope.userId,
    				quantity : info.quantity,
    				ordCancelReasonId : $scope.servicesCancelReasonId,
	    			ordCancelRemark : $scope.servicesCancelRemark, 
	    			unitId : $scope.unitId
			}
			
			if(info.saleId!=null && info.saleId>0)
				data.saleId = info.saleId;
    		// console.log('AddCapAmt',data);
    		$rootScope.startLoader();
    		var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
    		BillingGenericService
    				.serviceAction(METHOD_POST,
    						URI, data,
    						NOTIFICATION_MSG_STATUS_FALSE)
    				.then(
    						function(response) {
    							//ServiceCount
    							if($scope.autoRenderedServicesFinalList != null){
				                    $scope.ServiceCount = $scope.ServiceCount - info.quantity;
				                }
    							$rootScope.stopLoaderPopup();
    							if (response.data.status == "success")
    							{
    								$scope.servicesCancelReasonId = null;
    				            	$scope.servicesCancelRemark = '';	
    				            	$scope.autoRenderedServicesFinalList=[];
					            	$scope.autoRenderedServicesFinalList = list;
					            	$scope.resetTotalAmounts();
    				            	/*
									 * $scope.getAutoRenderedServicesByPatient({
									 * "visitTypeId":1,
									 * "encounterId":$scope.encounterDetails.encounterId
									 * });
									 */

                                      growl.success(response.data.message,
                                      {
                                          title : 'success'
                                      });
    							}else{
    								$scope.servicesCancelReasonId = null;
    				            	$scope.servicesCancelRemark = '';	
    				            	$scope.autoRenderedServicesFinalList=[];
					            	$scope.autoRenderedServicesFinalList = list;
					            	$scope.resetTotalAmounts();

                                     growl.error(response.data.message,
                                     {
                                         title : 'error'
                                     });
    							}
    						});
    	 }

	}
    
    $scope.cancelPopup = false;
    $scope.cancelSevices = function()
    {
    	$rootScope.startLoaderPopup();
        $scope.cancelService = true;
        $scope.cancelPopup = true;
        angular.element('#serviceCancellationModal').modal('hide');

    	if($scope.servicesCancelReasonId > 0)
    	{
	    	
    		var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf($scope.serciveObjectForRemove);
    		var idx = serviceIndex;
	    	var item = $scope.serciveObjectForRemove;
	    	var itemForNagetive = angular.copy($scope.serciveObjectForRemove);
	    	angular.element('#serviceCancellationModal').modal('hide');
	    	
	    	if(item.isPackage) 
		   	{
		   		growl.warning(" Services with updated rates will be removed from the package. Please update the rate accordingly. ",{
					title : ERROR_MSG
				});
	   		
		   	}
	    	
	    	if(item.orderDetailsId > 0)
	    	{
	    		
	    		itemForNagetive.basePrice = parseFloat(-($scope.serciveObjectForRemove.basePrice));
	        	itemForNagetive.concession = parseFloat(-($scope.serciveObjectForRemove.concession));
	        	itemForNagetive.orderDetailsId = 0;
	        	itemForNagetive.orderId = 0;
	        	itemForNagetive.discountDetailsList = [];
	        	itemForNagetive.discountPercentage = parseFloat(0);
	        	itemForNagetive.discount = parseFloat(0);
	        	itemForNagetive.ordCancelled  = 'Y';
	        	itemForNagetive.oldOrdDtlId = item.orderDetailsId;
	        	itemForNagetive.status = "I";
	        	itemForNagetive.isPackage = item.isPackage;
	        	itemForNagetive.ordCancelReasonId  = $scope.servicesCancelReasonId;
	        	itemForNagetive.ordCancelDatetime  = new Date().getTime();
	        	itemForNagetive.ordCancelRemark  = $scope.servicesCancelRemark ;
	        	itemForNagetive.isPackageService = item.isPackageService;
	        	itemForNagetive.serviceRenderingDeptId = item.serviceRenderingDeptId;
	        	itemForNagetive.isCapService = item.isCapService;
	        	itemForNagetive.creditPayable = item.creditPayable*-1;
	        	itemForNagetive.selfPayable = item.selfPayable*-1;
	        	// console.log("AFTERRRR",itemForNagetive);
	    		
	    			if(item.discountDetailsList.length > 0){
	    				angular.forEach(item.discountDetailsList, function (value, key) {
	    					if(value.orderDetailsDiscountId > 0)
	    		    		{
	    		    			var disArr = {};
	    		    			disArr.orderDetailsDiscountId  = value.orderDetailsDiscountId;
	    		    			disArr.orderDetailsId = value.orderDetailsId;
	    		    			disArr.discountCategoryId = value.discountCategoryId;
	    		    			disArr.discountPercentage = value.discountPercentage;
	    		    			disArr.discountAmount = value.discountAmount;
	    		    			disArr.isCancel = 'Y';
	    		    			disArr.cancelBy  = $scope.userId;
	    		    			disArr.cancelReasonId  = "";
	    		    			disArr.remark ="";
	    		    			disArr.createdBy  = value.createdBy;
	    		    			disArr.createdDate  = value.createdDate;
	    		    			disArr.updatedBy  = $scope.userId;
	    		    			disArr.updatedDate  = new Date().getTime();
	    		    			disArr.cancelledDate = new Date().getTime();
	    		    			disArr.orgId  = $scope.organizationId;
	    		    			disArr.unitId  = $scope.unitId;
	    		    			disArr.discountDate = new Date().getTime();
	    		    			disArr.status = "I";
	    		    			disArr.isConsessionDiscount = "N";
	    		    			$scope.removeDiscountList.push(disArr);
	    		    		}
	    	    			
	    	    			
	    	    		});
	    			}
	    	
	    			itemForNagetive.orderDetailsPayeeMapperDtosList = [];
	    			
	    			$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
	    			$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
	    			$scope.autoRenderedServicesFinalList[idx].ordCancelled = 'Y';
	    			$scope.autoRenderedServicesFinalList[idx].discountDetailsList = [];
	    			$scope.autoRenderedServicesFinalList[idx].ordCancelReasonId  = $scope.servicesCancelReasonId;
	    			$scope.autoRenderedServicesFinalList[idx].ordCancelDatetime  = new Date().getTime();
	    			$scope.autoRenderedServicesFinalList[idx].ordCancelRemark  = $scope.servicesCancelRemark ;
	    			$scope.autoRenderedServicesFinalList[idx].status  = 'I' ;
	    			$scope.autoRenderedServicesFinalList[idx].oldOrdDtlId = null;
	        		
	    			var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
	            	$scope.removeServicesList = [];
	        		
	    		}else{
	    		      var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(item);
	    		      $scope.autoRenderedServicesFinalList.splice(serviceIndex, 1);
	    		      var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
	    		      $scope.calculationTest(autoRenderedServicesList,init);
	    		      return false;
			      }
	    	
	    			// $scope.addCapAmout(item);
	    	
	    	if($scope.requestStatusObject != null){
				$scope.getCancelDiscountReqForDiscount();
				$scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				}
			 $scope.requestDiscountDiv = false;
			 $scope.lineItemDiscountAmt = parseFloat(0);
			 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
			 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
				}
			 
		   	if(item.isPackage) 
		   	{
	   	        $rootScope.startLoaderPopup();
	   	       
		   	    if($scope.requestStatusObject != null)
		  		{
					$scope.getCancelDiscountReqForDiscount();
		  		}	
				$scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				 }
				 $scope.requestDiscountDiv = false;
				 $scope.lineItemDiscountAmt = parseFloat(0);
				 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
				 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
	   	       
	   	       
	   		     $scope.saveOrderPackage();
	   		   
		   
			}else{
			//	var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(item);
			    $scope.autoRenderedServicesFinalList.splice(serviceIndex, 1);
			    
			    if(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)>0 && $scope.isBillWiseDiscountApplied)
				 {
			   		if($scope.discountRadio == 1)
			   			$scope.funDiscountPercentageBillWise();
			   		else
			   			$scope.funDiscountAmountBillWise();
					
				     $scope.applyDiscount();
				 }
			    
			    
				$scope.addCapAmout(item,$scope.autoRenderedServicesFinalList);
			}
		   
	        $scope.isFinancialClearence ='N';
	        $scope.financialRequestSendTo = "";

	    }else{
		    	growl.warning("Please select reason..!!",{
					title : ERROR_MSG
				});
		    	$rootScope.stopLoaderPopup();
				return false;
	    }
        $scope.isFinancialClearence ='N';
        $scope.financialRequestSendTo = "";

    }
    
   
    
    $scope.getRemoveServicePopup = function(item,idx)
    {
    	$scope.cancelPopup = false;
    	$scope.objectIndexService = idx;
    	$scope.serciveObjectForRemove = item;
    	
    	if(item.patientCouponTemplateMapperId != null){
    		angular.element('#couponServiceCancel').modal('show');
    	}else{
    		
    		$scope.servicesCancelReasonId = null;
    	    $scope.servicesCancelRemark = '';
        	if(item.serviceMasterId == $scope.surchargeServiceId)
        		{
        			$scope.isPatientClass = "N";
        		}
        	 angular.element('#serviceCancellationModal').modal('show');
    	}
    }
    
    
    $scope.couponServiceCancelPopup = function(){
    	
    	
    	var idx = $scope.objectIndexService;
    	var itemObj = $scope.serciveObjectForRemove ;
		
		 growl.warning("Coupon is applied to this service's. You need to reapply/rescan the said coupon",{
				title : ERROR_MSG
			});
		
		var patientCouponTemplateMapperId = $scope.autoRenderedServicesFinalList[idx].patientCouponTemplateMapperId;

		var indexList = [];
		$scope.autoRenderedServicesForSaveAndUpdate=[];
			var j = 0;
			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
					{
						if($scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId != null && $scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId == patientCouponTemplateMapperId)
						{
	    					var totalAmt = 0;
	    					var dis = 0;
	    					var gstAmt = 0;
	    					var withConDic = 0;
	    	    		
	    	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
	    						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
	    						$scope.autoRenderedServicesFinalList[j].discount = 0;
	    						$scope.roundOffAmt = 0.00;
	    						
	    						$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
	    						var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	    						
	    						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
	    						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
	
	    						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
	    						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
	    						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
	    						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
	    			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
	    			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
	    			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
	    			    		indexList.push(j);
						}	
   			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
   			{
   	            $scope.addOrUpdateServices(null,indexList);
   			}
				
   		j++;
			
		});
    	
    	$timeout(function () {

			 angular.element('#serviceCancellationModal').modal('show');

		}, 4000);
    	
    	
    }
    
    
    
    $scope.funDiscountPercentageBillWise = function()
    {
    	$scope.totalSumDisAmount = parseFloat(0);
		var i = 1;
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,index)
				 {
			        if(value.status == 'A' && value.isDiscountApplicable == 'Y')
			        	{
	    					
			        	var totalAmt = ((parseFloat(value.quantity) * parseFloat(value.basePrice))- parseFloat(value.concession)).toFixed(2);
	    					$scope.totalSumDisAmount =parseFloat($scope.totalSumDisAmount) + parseFloat(totalAmt);
	    					
			        	  
			        	}
			         if(i == $scope.autoRenderedServicesFinalList.length)
			        	{
			        		var totalDisAmount =(((parseFloat($scope.totalSumDisAmount))*(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)/100))).toFixed(2);
				    		if(totalDisAmount > 0){
				    		$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise).toFixed(2);
				    		$scope.billWiseDiscountObj.discountAmountBillWise = totalDisAmount;
				    		
				    		if($scope.totalSumDisAmount > 0){
				    			if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
								growl.warning("You can give discount on only upto "+$scope.totalSumDisAmount,{
									title : WARNING_MSG
								});
								$scope.billWiseDiscountObj.discountAmountBillWise=0;
								$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						
				    			}
								}else{
										growl.warning("You have no service for discount ",{
											title : WARNING_MSG
										});
										$scope.billWiseDiscountObj.discountAmountBillWise=0;
										$scope.billWiseDiscountObj.discountPercentageBillWise=0;
								}
				    		
				    		 }
				    		else
				    			{
					    			$scope.billWiseDiscountObj.discountPercentageBillWise =0;
						    		$scope.billWiseDiscountObj.discountAmountBillWise =0;
						    		
						    		if($scope.totalSumDisAmount > 0){
						    			if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
										growl.warning("You can give discount only upto "+$scope.totalSumDisAmount,{
											title : WARNING_MSG
										});
										$scope.billWiseDiscountObj.discountAmountBillWise=0;
										$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						    			}
										}else{
												growl.warning("You have no service for discount ",{
													title : WARNING_MSG
												});
												$scope.billWiseDiscountObj.discountAmountBillWise=0;
												$scope.billWiseDiscountObj.discountPercentageBillWise=0;
										}
				    			}
			        	}
					i++;	
				 });
    }
    
    
    $scope.getCancelDiscountReqForDiscount = function()
	{
		
		
		$rootScope.startLoaderPopup();
		var data = {
				reqDiscId :$scope.requestStatusObject.reqDiscId,
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
		
		GenericService
		.serviceAction("POST", URI, data)
		.then(function(response) {
					$rootScope.stopLoaderPopup();
					if (response.data.status == "success") {
					
						$scope.requestApporivedDiscountDiv = false;
						$scope.discountDisabledFlagForRequest = false;
						$scope.requestAppUser = 0;
						$scope.approvedAmtApp = 0;
						$scope.requestAmtApp =0;
						$scope.statusApp = "";
						$scope.discountRadio = 2; 
						$scope.discTypeDesc = "";
						$scope.finalBill = 'Y';
						
						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						 $scope.requestStatusObject  = null;
			    		angular.element('#discountCancellationModalReq').modal('hide');
					}
				});
		
	}
    
    $scope.getCancelDiscountReq = function()
	{
		
		var indexList =[];
		$rootScope.startLoaderPopup();
		var data = {
				reqDiscId :$scope.requestStatusObject.reqDiscId,
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
		
		GenericService
		.serviceAction("POST", URI, data)
		.then(function(response) {
					$rootScope.stopLoaderPopup();
					if (response.data.status == "success") {
						
						 var i =0;
				    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				   			if(value.status == 'A'){
				   				
				   				if(parseFloat(value.discount) > 0){
				   					
						   				$scope.autoRenderedServicesFinalList[i].concession = parseFloat(0);
			   						    $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(0);
			   						    
				    			        var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
				    			        $scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
				    			        $scope.autoRenderedServicesFinalList[i].discount = parseFloat(0).toFixed(2);
				    			        var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
				    			        
				    			    	var txpre = 0;
										if(value.taxPercentage != null || value.taxPercentage > 0){
											$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
											txpre = value.taxPercentage;
										}else{
												$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
												}
										$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
										$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
										$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
										$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
										
										$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);	
							    		indexList.push(i);
				   					}
				   				}
				   			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length > 0){
				   				// $scope.saveOrderPackage();
			    	            $scope.addOrUpdateServices(null,indexList);
				   			}
				    	   		
				   			i++;
				   			});
				    	   
				    	   
						growl.success("Cancelled !!!!",{
							title : 'success'
						});
						
						$scope.requestApporivedDiscountDiv = false;
						$scope.discountDisabledFlagForRequest = false;
						$scope.requestAppUser = 0;
						$scope.approvedAmtApp = 0;
						$scope.requestAmtApp =0;
						$scope.statusApp = "";
						$scope.discTypeDesc = "";
						$scope.discountRadio = 2; 
						$scope.finalBill = 'Y';
						
						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						 $scope.requestStatusObject  = null;
			    		angular.element('#discountCancellationModalReq').modal('hide');
					}
				});
		
	}
    
    $scope.funDiscountAmountBillWise = function()
    {
    	$scope.totalSumDisAmount = parseFloat(0);
		var i = 1;
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,index)
			 {
		        if(value.status == 'A' && value.isDiscountApplicable == 'Y')
		        	{
		        	
		        	                var totalAmt = ((parseFloat(value.quantity) * parseFloat(value.basePrice))- parseFloat(value.concession)).toFixed(2);
    						 		$scope.totalSumDisAmount =parseFloat($scope.totalSumDisAmount) + parseFloat(totalAmt);
    						
		        	
		        	}
		         if(i == $scope.autoRenderedServicesFinalList.length)
		        	{
		        	 var totalDisPercentage =((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/(parseFloat($scope.totalSumDisAmount)))*100).toFixed(2);
		        	/* if($scope.billWiseDiscountObj.discountPercentageBillWise!=undefined && $scope.billWiseDiscountObj.discountPercentageBillWise!=null && $scope.billWiseDiscountObj.discountPercentageBillWise!='')
		        		 {
		        	 var totalDisAmount =(((parseFloat($scope.totalSumDisAmount))*(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)/100))).toFixed(2);
			    		$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise).toFixed(2);
			    		$scope.billWiseDiscountObj.discountAmountBillWise = totalDisAmount;
			    		totalDisPercentage=$scope.billWiseDiscountObj.discountPercentageBillWise;
		        		 }*/
			    		if(totalDisPercentage >= 0){
			    		$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat(totalDisPercentage).toFixed(2); 
			    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
			    		if(Number($scope.billWiseDiscountObj.discountPercentageBillWise)>100)
						{
						if($scope.totalSumDisAmount > 0){
							if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
						growl.warning("You can give discount only upto "+$scope.totalSumDisAmount,{
							title : WARNING_MSG
						});
						$scope.billWiseDiscountObj.discountAmountBillWise=0;
						$scope.billWiseDiscountObj.discountPercentageBillWise=0;
					
							}
						}else{
								growl.warning("You have no service for discount ",{
									title : WARNING_MSG
								});
								$scope.billWiseDiscountObj.discountAmountBillWise=0;
								$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						}
			    		return false;
						}
			    		}else{
						
							if($scope.totalSumDisAmount > 0){
								if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
							growl.warning("You can give discount only upto "+$scope.totalSumDisAmount,{
								title : WARNING_MSG
							});
							$scope.billWiseDiscountObj.discountAmountBillWise=0;
							$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						
								}
							}else{
									growl.warning("You have no service for discount ",{
										title : WARNING_MSG
									});
									$scope.billWiseDiscountObj.discountAmountBillWise=0;
									$scope.billWiseDiscountObj.discountPercentageBillWise=0;
							}
				    		return false;
			    		}
		        	}
				i++;	
			 });
    }
    
    $scope.getApplyDiscountPopup = function()
    {
    	if($scope.billWiseDiscountObj.discountPercentageBillWise === '')
    		{
	    		growl.warning("Discount Percentage is required.",{
					title : ERROR_MSG
				});
	    		return false;
    			
    		}

    	else if($scope.billWiseDiscountObj.discountAmountBillWise === '')
    		{
	    		growl.warning("Discount Amount is required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.billWiseDiscountObj.discountAmountBillWise >= 0 && $scope.billWiseDiscountObj.discountPercentageBillWise >= 0 && $scope.billWiseDiscountObj.discountCategoryId === '')
    		{
	    		growl.warning("Discount Category is required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	
    	else
    		{
    		
	    		var checkCoupon = false;
	    		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
						{
							if(value.patientCouponTemplateMapperId != null )
							{
								checkCoupon = true;
							}
						});
	    		
	    		if(checkCoupon == true){
	    			angular.element('#coupnDiscountPopup').modal('show');
	    		}else{
	    			if($scope.billWiseDiscountObj.discountAmountBillWise >= 0 && $scope.billWiseDiscountObj.discountPercentageBillWise >= 0 && $scope.billWiseDiscountObj.discountCategoryId > 0)
		    		{
		    			// if($scope.totalSumDisAmount > 0)
		    			angular.element('#applyDiscountPopup').modal('show');
		    		}
	    		}
	    		
    		}
    }
    
    
    $scope.cancelCoupnByDiscount = function(){
    	
    	if($scope.billWiseDiscountObj.discountAmountBillWise >= 0 && $scope.billWiseDiscountObj.discountPercentageBillWise >= 0 && $scope.billWiseDiscountObj.discountCategoryId > 0)
		{
    	
    		$scope.finalCancelCoupon();
    		$timeout(function () {

    			 angular.element('#applyDiscountPopup').modal('show');

    		}, 6000);
    		 
		}
    }
    
    
    $scope.applyDiscount  = function()
    {
    	
    	// if(parseFloat($scope.totalDisServiceWise) >
		// parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)){
		    	$scope.listOfOldDisAmt = [];
		    	var indexList =[];
		    	$scope.isBillWiseDiscountApplied = true;
		    	$scope.autoRenderedServicesForSaveAndUpdate=[];
		    	if($scope.userDiscountObject != null){
		    		
					if($scope.userDiscountObject.discountType == 1){
						
						var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
						var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
						$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
						$scope.userDiscountObject.toDiscountAmount = toDisAmt;
					}
		    	}
		    	
		    	if(($scope.requestStatusObject == null) && ($scope.userDiscountObject == null || parseFloat($scope.userDiscountObject.fromDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise && parseFloat($scope.userDiscountObject.toDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise) )
		    	{
		    	   $scope.billWiseDiscountObj.requestAmountBillWise = $scope.billWiseDiscountObj.discountAmountBillWise;
		    	   var i =0;
		    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
		    			   {
		   			if(value.status == 'A' && value.isDiscountApplicable == 'Y' )
		   			{
		                var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		                $scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
    		    		if($scope.discountRadio == 1)
    					{
    		    			$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
	    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
    		    			
    		    			
    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    		    			$scope.disAmtCal = (((parseFloat(totalAmt) - parseFloat(value.concession) ) *parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
    		    			// $scope.autoRenderedServicesFinalList[i].discount
							// = $scope.disAmtCal;

    		    			$scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN($scope.billWiseDiscountObj.discountPercentageBillWise) ? 0: $scope.billWiseDiscountObj.discountPercentageBillWise)/100).toFixed(2);

    		    			  var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
	  	    		  	        
	  	    		  	    	var txpre = 0;
	  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
	  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
	  	    		  				txpre = value.taxPercentage;
	  	    		  			}else{
	  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
	  	    		  					}
	  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
	  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
    						}
    						else{
    						
    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
	    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
	    		    		
    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    						// $scope.disAmtCal = parseFloat(0);
    						var disPerTmp = parseFloat(0);
    						disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise-$scope.AutoRenderedTotalConcessionSummary))*100;
    						// $scope.disAmtCal = ((( parseFloat(totalAmt) -
							// parseFloat(value.concession) ) *
							// parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise);
    						// $scope.autoRenderedServicesFinalList[i].discountPercentage
							// =
							// ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
    						// $scope.autoRenderedServicesFinalList[i].discount
							// = ($scope.disAmtCal).toFixed(2);
    						
    						// $scope.autoRenderedServicesFinalList[i].discount
							// = (parseFloat(totalAmt) * disPerTmp)/100;
    						disPerTmp =  parseFloat(disPerTmp).toFixed(2);
    						 $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	

    						$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
    						  var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
	  	    		  	        
	  	    		  	    	var txpre = 0;
	  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
	  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
	  	    		  				txpre = value.taxPercentage;
	  	    		  			}else{
	  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
	  	    		  					}
	  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
	  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
	  	    		  			$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  null;
    					}
    		    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
    		    		indexList.push(i);
		   			 }
		   			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length > 0){
		   				
	    	            $scope.addOrUpdateServices(null,indexList);
		   				// $scope.saveOrderPackage();
		   			}
		   			i++;
		   			});
		    	   
		    	   if($scope.billWiseDiscountObj.requestAmountBillWise > 0){
		    		var URI = BASE_URL + ROOT_URL
		    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
		    BillingGenericService.serviceAction(METHOD_GET, URI,"",
		    				NOTIFICATION_MSG_STATUS_FALSE)
		    		.then(function(response) {
		    					// console.log(response);
		    					// $rootScope.startLoader();
		    					if (response.data.status == "success")
		    						{
		    						  $scope.userDiscountList =[];
		    						  $scope.userDiscountList = response.data.listObject;
		    						}
		    						
		    				});
		    
				   $scope.requestDiscountDiv = true;
				   $scope.isBillWiseDiscountApplied = true;
				   $scope.finalBill = 'N';
				   growl.warning("Please send discount request to user",{
						title : ERROR_MSG
					});
		    	}else
		    		{
		    		 $scope.requestDiscountDiv = false;
		    		}
		    		return false;
				}
		    	else{
		    		
		    		if($scope.requestStatusObject != null){
		    			if($scope.requestStatusObject.reqStatus  == 2 && parseFloat($scope.requestStatusObject.approvedDiscAmt) >= parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))
			    		{
		    				// for DD
			    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
			    		$scope.billWiseDiscountObj.requestUserId = "";
			    		$scope.requestDiscountDiv = false;
			    		// For DD
				    	var i = 0;
				    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				    			if(value.status == 'A' && value.isDiscountApplicable == 'Y')
				    			{
		    					$scope.requestDiscountDiv = false;
								$scope.billWiseDiscountObj.requestAmountBillWise = 0;
								$scope.billWiseDiscountObj.requestUserId = '';
								
		    					$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
		    		    		if($scope.discountRadio == 1)
		    					{
		    		    			$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
			    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
			    		    		
			    		    		
		    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
		    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    		    			$scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
		    		    			// $scope.autoRenderedServicesFinalList[i].discount
									// = $scope.disAmtCal;

		    		    			$scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN($scope.billWiseDiscountObj.discountPercentageBillWise) ? 0: $scope.billWiseDiscountObj.discountPercentageBillWise)/100).toFixed(2);

		    		    			  var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].discount) + parseFloat($scope.autoRenderedServicesFinalList[i].concession));
			  	    		  	        
			  	    		  	    	var txpre = 0;
			  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
			  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
			  	    		  				txpre = value.taxPercentage;
			  	    		  			}else{
			  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
			  	    		  					}
			  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
			  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
			  	    		  			$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  null;
		    					}
		    					else{
		    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
			    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
			    		    		
		    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    						// $scope.disAmtCal =
									// (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
		    						// $scope.autoRenderedServicesFinalList[i].discountPercentage
									// =
									// ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
		    						// $scope.autoRenderedServicesFinalList[i].discount
									// = $scope.disAmtCal;

		    						var disPerTmp = parseFloat(0);
		    						disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise))*100;
		    						// $scope.autoRenderedServicesFinalList[i].discount
									// = (parseFloat(totalAmt) * disPerTmp)/100;
		    						$scope.autoRenderedServicesFinalList[i].discountPercentage = parseFloat(disPerTmp).toFixed(2);

		    						 $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	

		    						  var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].discount) + parseFloat($scope.autoRenderedServicesFinalList[i].concession));
			  	    		  	        
			  	    		  	    	var txpre = 0;
			  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
			  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
			  	    		  				txpre = value.taxPercentage;
			  	    		  			}else{
			  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
			  	    		  					}
			  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
			  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
			  	    		  			$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  null;
		    					}
		    		    		// $scope.autoRenderedServicesFinalList[i].concession
								// = parseFloat(0);
		    		    		// $scope.autoRenderedServicesFinalList[i].patientClassPercentage
								// = parseFloat(0);
		    		    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
		    		    		indexList.push(i);
					    	   }
				    			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length > 0)
				    			{
				    	            $scope.addOrUpdateServices(null,indexList);
				    			}
					    		i++;
		    		
				    		});
			    		}
		    			else{
		    				
		    				  growl.warning("You have approved only upto "+parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2),{
		  						title : ERROR_MSG
		  					});
		    				  return false;
		    			}
		    	}else{
		    		// for DD
		    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
		    		$scope.billWiseDiscountObj.requestUserId = "";
		    		$scope.requestDiscountDiv = false;
		    		// For DD
			    	var i = 0;
			    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    			if(value.status == 'A' && value.isDiscountApplicable == 'Y')
			    			{
	    					$scope.requestDiscountDiv = false;
							$scope.billWiseDiscountObj.requestAmountBillWise = 0;
							$scope.billWiseDiscountObj.requestUserId = '';
							
	    					$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
	    		    		if($scope.discountRadio == 1)
	    					{
	    		    			$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
		    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
		    		    		
	    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
	    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    		    			$scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
	    		    			// $scope.autoRenderedServicesFinalList[i].discount
								// = $scope.disAmtCal;
	    		    			$scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN($scope.billWiseDiscountObj.discountPercentageBillWise) ? 0: $scope.billWiseDiscountObj.discountPercentageBillWise)/100).toFixed(2);
	    		    			
	    		    			  var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
	  	    		  	        
	  	    		  	    	var txpre = 0;
	  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
	  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
	  	    		  				txpre = value.taxPercentage;
	  	    		  			}else{
	  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
	  	    		  					}
	  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
	  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
	  	    		  			$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  null;
	  	    		  			
	    					}
	    					else{
	    						
	    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
		    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
		    		    		
	    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    						// $scope.disAmtCal =
								// (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
	    						// $scope.autoRenderedServicesFinalList[i].discountPercentage
								// =
								// ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
	    						// $scope.autoRenderedServicesFinalList[i].discount
								// = $scope.disAmtCal;
	    						var disPerTmp = parseFloat(0);
	    						if($scope.cancelService)
	    							disPerTmp = $scope.billWiseDiscountObj.discountPercentageBillWise;
	    						else
	    							disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise-$scope.AutoRenderedTotalConcessionSummary))*100;
	    						// $scope.autoRenderedServicesFinalList[i].discount
								// = (parseFloat(totalAmt) * disPerTmp)/100;
	    						$scope.autoRenderedServicesFinalList[i].discountPercentage = parseFloat(disPerTmp).toFixed(2);
	    						
	    						  var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
		  	    		  	        
		  	    		  	    	var txpre = 0;
		  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
		  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
		  	    		  				txpre = value.taxPercentage;
		  	    		  			}else{
		  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
		  	    		  					}
		  	    		  		    $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	
		  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
		  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
		  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
		  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
		  	    		  			$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  null;
	    						}
	    		  			
	    		    			$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
	    		    			indexList.push(i);
				    	     }
				    			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length > 0){
				    				$scope.addOrUpdateServices(null,indexList);
				    			}
				    		i++;
	    		
			    		});
		    	}
		       }
    	
    }
    
    
    $scope.specialityMasterListForGroup = [];
    // Check Product Wise Category
	  var isGroupExists;
		function checkGroupExistsOrNot(arr, val) {
			return arr
					.some(function(arr) {
						return (val.specialityId === arr.specialityId);
					});
		}
		
    
    $scope.groupWiseDiscountPopup = function()
    {
    	$scope.groupWiseDisFlag = true;
    	$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
		$scope.disPerPopupForGroupFull = 0;
    	// console.log($scope.autoRenderedServicesFinalList);
    	$scope.groupWiseDiscountServicesList = [];
    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
    		
    		if(value.isDiscountApplicable == 'Y'){
    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
    		if($scope.specialityMasterListForGroup.length > 0)
    			{
			    	isGroupExists=checkGroupExistsOrNot($scope.specialityMasterListForGroup,temp)
					if (!isGroupExists) {
						$scope.specialityMasterListForGroup.push(temp);
					}
    	       }
    		else
    			{
    			   $scope.specialityMasterListForGroup.push(temp);
    			}
    		var newArrPop = angular.copy(value);
    		$scope.groupWiseDiscountServicesList.push(newArrPop);
    	}
    		
    	});
    	
    	$scope.groupWiseServicesSelectedList.services = [];
    	$scope.chkFlgGroup = 'N';
    	
    }
    
    $scope.serachGroupWiseServicesClear = function()
    {
    	$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
    	$scope.serviceSpecialityIdForGroup = '';
    	$scope.serviceSubSpecialityIdForGroup = '';
    	$scope.disPerPopupForGroupFull = 0;
    	$scope.chkFlgGroup = 'N';
    	$scope.groupWiseServicesSelectedList.services = [];
    	$scope.groupWiseDiscountServicesList  = [];
    	
    	
    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
    		
    		if(value.isDiscountApplicable == 'Y'){
    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
    		if($scope.specialityMasterListForGroup.length > 0)
    			{
			    	isGroupExists=checkGroupExistsOrNot($scope.specialityMasterListForGroup,temp)
					if (!isGroupExists) {
						$scope.specialityMasterListForGroup.push(temp);
					}
    	       }
    		else
    			{
    			   $scope.specialityMasterListForGroup.push(temp);
    			}
    		var newArrPop = angular.copy(value);
    		$scope.groupWiseDiscountServicesList.push(newArrPop);
    	}
    		
    	});
    	
    }
    
    $scope.serachGroupWiseServices = function()
    {
    	$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
		$scope.disPerPopupForGroupFull = 0;
    	$scope.groupWiseServicesSelectedList.services = [];
    	
    	
    	$scope.groupWiseDiscountServicesList = [];
    	var v = 1;
    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
    		
    		if(value.isDiscountApplicable == 'Y'){
    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
    		if($scope.specialityMasterListForGroup.length > 0)
    			{
			    	isGroupExists=checkGroupExistsOrNot($scope.specialityMasterListForGroup,temp)
					if (!isGroupExists) {
						$scope.specialityMasterListForGroup.push(temp);
					}
    	       }
    		else
    			{
    			   $scope.specialityMasterListForGroup.push(temp);
    			}
    		var newArrPop = angular.copy(value);
    		$scope.groupWiseDiscountServicesList.push(newArrPop);
    	}
    		if(v == $scope.autoRenderedServicesFinalList.length){
    			if($scope.serviceSpecialityIdForGroup > 0 && $scope.serviceSubSpecialityIdForGroup > 0)
        		{
        			$scope.groupWiseDiscountServicesList = [];
        			var i = 0;
        			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
            			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup && value.serviceSubSpecialityId == $scope.serviceSubSpecialityIdForGroup && value.isDiscountApplicable == 'Y')
            				{
            				var newArrPop = angular.copy($scope.autoRenderedServicesFinalList[i]);
            	    		$scope.groupWiseDiscountServicesList.push(newArrPop);
            				}
        				
        	    		i++;
        	    	});
        			
        			
        			
        		}
        	else if($scope.serviceSpecialityIdForGroup > 0)
        		{
    	    		$scope.groupWiseDiscountServicesList = [];
    				var i = 0;
    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
    	    			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup && value.isDiscountApplicable == 'Y')
    	    				{
    	    				var newArrPop = angular.copy($scope.autoRenderedServicesFinalList[i]);
            	    		$scope.groupWiseDiscountServicesList.push(newArrPop);
    	    				}
    					
    		    		i++;
    		    	});
        		}
    		}
    		v++;
    	});
    }
    
    
  // Check Service in inclusion //12-10-2019 start
	  var isContractExists;
		function checkContractIdExistsOrNot(arr, val) {
			return arr
					.some(function(arr) {
						if(val === arr.contractId)
							{
								return true;
							}
						else
							{
								return false;
							}
					});
		}
		// 12-10-2019 end
    $scope.getDisPerPopupForGroup = function(disPer)
    {
    	
    	if(disPer == "" || disPer == null || disPer == undefined){
    		disPer = 0;
    	}
    	var len = $scope.groupWiseServicesSelectedList.services.length;
    	if(len == 0)
		{
			growl.warning("Please select at least one service!",{
				title : ERROR_MSG
			});
			$scope.disPerPopupForGroup = '';
			$scope.disPerPopupForGroupFull = 0;
    		return false;
		}
    	else if(parseFloat(disPer) > 100){
    		growl.warning("Percentage should be less then or equal to 100 %.",{
				title : ERROR_MSG
			});
			$scope.disPerPopupForGroup = '';
			$scope.disAmtPopupForGroup  = '';
			$scope.disCategoryIdForGroup = '';
			$scope.disPerPopupForGroupFull = 0;
    		return false;
    	}
    	else
    		{
    		$scope.totalAmtPopupAllServices = 0;
    		for(var i = 0;i < len;i++)
    		{
    			// alert($scope.groupWiseServicesSelectedList.services[i]);
	    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
	    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
	    				{
	    				    var finalTotalAmt = parseFloat(value.totalAmt) - parseFloat(value.concession);
	    				    $scope.totalAmtPopupAllServices =(parseFloat($scope.totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
	    				}
					
		    	});
    		}
    	}
    	
    	if($scope.totalAmtPopupAllServices>0){
	    	var calDicAmt = ($scope.totalAmtPopupAllServices*disPer/100).toFixed(2);	
			$scope.disAmtPopupForGroup = calDicAmt;
			$scope.disPerPopupForGroup = disPer;
			$scope.disPerPopupForGroupFull = disPer;
    	}else{
    		$scope.disAmtPopupForGroup = 0;
			$scope.disPerPopupForGroup = 0;
    	}
    }
    
    $scope.getDisAmtPopupForGroup = function(disAmt)
    {
    	if(disAmt == "" || disAmt == null || disAmt == undefined){
    		disAmt = 0;
    	}
    	var len = $scope.groupWiseServicesSelectedList.services.length;
    	if(len == 0)
		{
			growl.warning("Please select at least one service!",{
				title : ERROR_MSG
			});
			$scope.disAmtPopupForGroup  = '';
    		return false;
		}
    	else
    		{
    		
    		$scope.totalAmtPopupAllServices = 0;
    		for(var i = 0;i < len;i++)
    		{
    			// alert($scope.groupWiseServicesSelectedList.services[i]);
	    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
	    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
	    				{
	    					var finalTotalAmt = parseFloat(value.totalAmt) - parseFloat(value.concession);
	    					$scope.totalAmtPopupAllServices =(parseFloat($scope.totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
	    				}
					
		    	});
	    		if(i+1 == len){
	    			if(parseFloat(disAmt) > parseFloat($scope.totalAmtPopupAllServices)){
	    				growl.warning("Amount should be less than or equal to total amount..",{
	    					title : ERROR_MSG
	    				});
	    				$scope.disAmtPopupForGroup  = '';
	    				$scope.disPerPopupForGroup = '';
	    				$scope.disCategoryIdForGroup = '';
	    				$scope.disPerPopupForGroupFull = 0;
	    	    		return false;
	    			}
	    		}
    		}
    		if(disAmt>0 && $scope.totalAmtPopupAllServices>0){
    			var disPer = ((parseFloat(disAmt)/$scope.totalAmtPopupAllServices)*100).toFixed(2);
        		$scope.disPerPopupForGroupFull = (parseFloat(disAmt)/$scope.totalAmtPopupAllServices)*100;
        		$scope.disPerPopupForGroup = disPer;
        		$scope.disAmtPopupForGroup = disAmt;
    		}else{
    			$scope.disPerPopupForGroup = 0;
        		$scope.disAmtPopupForGroup = 0;
    		}
    		
    		}
    	
		
    }
    
    $scope.clearDiscount = function()
    {
    	$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
		$scope.disPerPopupForGroupFull = 0;
		
		if(!$scope.checked){
			$scope.chkFlgGroup = 'N';
		}
		if($scope.groupWiseServicesSelectedList.services.length == 0){
			 $scope.groupWiseDisFlag  = true;
		}
		
    }
    
    $scope.addDiscountInDiscountListForGroup = function()
    {
    	if($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '' )
    		{
	    		growl.warning("All fields are required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '')
    		{
	    		growl.warning("All fields are required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '')
		{
    		growl.warning("All fields are required.",{
				title : ERROR_MSG
			});
    		return false;
		}
    	else
    		{
    		var len = $scope.groupWiseServicesSelectedList.services.length;
    			if(len == 0)
    				{
	    				growl.warning("Please select at least one service!",{
	    					title : ERROR_MSG
	    				});
	    	    		return false;
    				}
    			else
    				{
    				$scope.groupWiseDisFlag = false;
    				for(var i = 0;i < len;i++)
    	    		{
    					var j = 0;
	    				angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
	    					var totalAmtPopupForGroup =0;
	    					var concessionPopupForGroup =0;
	    					var finalTotalAmtForGroup = 0;
	    					var calDicAmtForPopup = 0;
	    	    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
	    	    				{
	    	    					
	    	    					// test
	    	    				
	    	    				$scope.groupWiseDiscountServicesList[j].concession =value.concession;
	    	    				$scope.groupWiseDiscountServicesList[j].patientClassPercentage = value.patientClassPercentage;
	    						
	    	    				    var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    	    				    $scope.groupWiseDiscountServicesList[j].discountCategoryId = $scope.disCategoryIdForGroup;
	    	    				    
	    	    				    
	    	    				  
	    	    				    $scope.groupWiseDiscountServicesList[j].discountPercentageFull = $scope.disPerPopupForGroupFull;
	        		    			var disAmtCal = (((parseFloat(totalAmt) - parseFloat(value.concession)) *parseFloat($scope.disPerPopupForGroupFull))/100).toFixed(2);
	        		    			$scope.groupWiseDiscountServicesList[j].discount = disAmtCal;
	        		    			if(disAmtCal>0){
	        		    				  $scope.groupWiseDiscountServicesList[j].discountPercentage = $scope.disPerPopupForGroup;
	        		    			}else{
	        		    				  $scope.groupWiseDiscountServicesList[j].discountPercentage = 0;
	        		    			}
	        		    			
	        		    			 var withConDic =parseFloat(totalAmt) - ( parseFloat(value.concession) + parseFloat(disAmtCal));
		    	    			        
		    	    			    	var txpre = 0;
		    							if(value.taxPercentage != null || value.taxPercentage > 0){
		    								$scope.groupWiseDiscountServicesList[j].taxPercentage = value.taxPercentage;
		    								txpre = value.taxPercentage;
		    							}else{
		    								  $scope.groupWiseDiscountServicesList[j].taxPercentage = 0;
		    									}
		    						$scope.groupWiseDiscountServicesList[j].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	    	    					$scope.groupWiseDiscountServicesList[j].netPay = parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(disAmtCal)) + parseFloat(value.taxAmt))).toFixed(2);
	    	    					$scope.groupWiseDiscountServicesList[j].selfPayable = $scope.groupWiseDiscountServicesList[j].netPay;
	    	    					$scope.groupWiseDiscountServicesList[j].creditPayable =parseFloat(0);
	    							
	    							
	    	    					
	    	    					// test
	    	    				}
	    	    			
	    	    			j++;
	    		    	});
	    				
    				}
    			}
    		}
    	
    	
    }
    
    $scope.getPayeeCreditDetails = function(itemList)
    {
    	$scope.payeeCreditDetailslList = [];
    	$scope.payeeCreditDetailslList = itemList;
    }
    
    
    var isItemExists;
	function checkItemIdExistsOrNot(arr, val) {
		return arr
				.some(function(arr) {
					if(val === arr.payeeId )
						{
							return true;
						}
					else
						{
							return false;
						}
				});
	}
	
    
    $scope.initForPayeeList = function(itemList,idx){
    	if(itemList.length != $scope.patientSponsorDetailsDtosListSplit.length)
		{
    		
    		if(itemList.length == 1){
    			
    			var tmpItemList = angular.copy($scope.serviceList[idx].orderDetailsPayeeMapperDtosList);
    			$scope.serviceList[idx].orderDetailsPayeeMapperDtosList = [];
				  
    			var cooount = 1;
				angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
						 {
					
							if(value.priorityId == '1')
							{
								var newTmpArr = {};
								
								
								newTmpArr.associateCompanyDesc = value.insuranceDesc;
								newTmpArr.associateCompanyId = value.insuranceId;
								newTmpArr.coShare =  null;
								newTmpArr.contractId = value.contractId;
								newTmpArr.creditPayable = parseFloat(0);
								newTmpArr.id = null;
								newTmpArr.isInvoiceGenerated = "N";
								newTmpArr.isRateEditable = null;
								newTmpArr.isTariffRateApplicable= "N";
								newTmpArr.orderDetailsId = value.orderDetailsId;
								newTmpArr.organizationId = $scope.organizationId;
								newTmpArr.payeeAmount =  parseFloat(0);
								newTmpArr.payeeDesc =  value.companyDesc;
								newTmpArr.payeeId = value.payeeId;
								newTmpArr.selfPayable =  0;
								newTmpArr.serviceId = value.serviceMasterId;
								newTmpArr.spiltBillAmt = parseFloat(0);
								newTmpArr.status =  'A';
								newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
								newTmpArr.unitId = $scope.unitId;
								$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
								
								// $scope.payeeCreditDetailslListSplit.push(newTmpArr);
								
							}
							else{
								if(value.payeeId != $scope.selfPayeeId){
									var newTmpArr = {};
									newTmpArr.associateCompanyDesc = value.insuranceDesc;
									newTmpArr.associateCompanyId = value.insuranceId;
									newTmpArr.coShare =  null;
									newTmpArr.contractId = value.contractId;
									newTmpArr.creditPayable = parseFloat(0);
									newTmpArr.id = null;
									newTmpArr.isInvoiceGenerated = "N";
									newTmpArr.isRateEditable = null;
									newTmpArr.isTariffRateApplicable= "N";
									newTmpArr.orderDetailsId = value.orderDetailsId;
									newTmpArr.organizationId = $scope.organizationId;
									newTmpArr.payeeAmount =  parseFloat(0);
									newTmpArr.payeeDesc =  value.companyDesc;
									newTmpArr.payeeId = value.payeeId;
									newTmpArr.selfPayable =  0;
									newTmpArr.serviceId = value.serviceMasterId;
									newTmpArr.spiltBillAmt = parseFloat(0);
									newTmpArr.status =  'A';
									newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
									newTmpArr.unitId = $scope.unitId;
									$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
								}
							}
							
							if(cooount == $scope.patientSponsorDetailsDtosListSplit.length){
								
								angular.forEach(tmpItemList,function(value,index)
										 {
									$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
										 });
								$scope.calculateSplitBillTotal($scope.serviceList);
							}
							
							cooount++;
							
						});
    		}
    		
    		if(itemList.length > 1){
    			var cooount = 1;
    			angular.forEach($scope.patientSponsorDetailsDtosListSplit, function (value, key) {
    				
    				if(value.priorityId == 1){
    					
    					
    					isItemExists=checkItemIdExistsOrNot(itemList,value.payeeId)
						  if (!isItemExists) {
							  
							  var tmpItemList = angular.copy($scope.serviceList[idx].orderDetailsPayeeMapperDtosList);
							  $scope.serviceList[idx].orderDetailsPayeeMapperDtosList = [];
							  
							  var newTmpArr = {};
								
								
								newTmpArr.associateCompanyDesc = value.insuranceDesc;
								newTmpArr.associateCompanyId = value.insuranceId;
								newTmpArr.coShare =  null;
								newTmpArr.contractId = value.contractId;
								newTmpArr.creditPayable = parseFloat(0);
								newTmpArr.id = null;
								newTmpArr.isInvoiceGenerated = "N";
								newTmpArr.isRateEditable = null;
								newTmpArr.isTariffRateApplicable= "N";
								newTmpArr.orderDetailsId = value.orderDetailsId;
								newTmpArr.organizationId = $scope.organizationId;
								newTmpArr.payeeAmount =  parseFloat(0);
								newTmpArr.payeeDesc =  value.companyDesc;
								newTmpArr.payeeId = value.payeeId;
								newTmpArr.selfPayable =  0;
								newTmpArr.serviceId = value.serviceMasterId;
								newTmpArr.spiltBillAmt = parseFloat(0);
								newTmpArr.status =  'A';
								newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
								newTmpArr.unitId = $scope.unitId;
								$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
								angular.forEach(tmpItemList,function(value,index)
										 {
									$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
										 });
							  // $scope.selectedItemList.push(value);
						  }
    					
    					
    					
    				}else{
    					isItemExists=checkItemIdExistsOrNot(itemList,value.payeeId)
						  if (!isItemExists) {
							  var newTmpArr = {};
								
								
								newTmpArr.associateCompanyDesc = value.insuranceDesc;
								newTmpArr.associateCompanyId = value.insuranceId;
								newTmpArr.coShare =  null;
								newTmpArr.contractId = value.contractId;
								newTmpArr.creditPayable = parseFloat(0);
								newTmpArr.id = null;
								newTmpArr.isInvoiceGenerated = "N";
								newTmpArr.isRateEditable = null;
								newTmpArr.isTariffRateApplicable= "N";
								newTmpArr.orderDetailsId = value.orderDetailsId;
								newTmpArr.organizationId = $scope.organizationId;
								newTmpArr.payeeAmount =  parseFloat(0);
								newTmpArr.payeeDesc =  value.companyDesc;
								newTmpArr.payeeId = value.payeeId;
								newTmpArr.selfPayable =  0;
								newTmpArr.serviceId = value.serviceMasterId;
								newTmpArr.spiltBillAmt = parseFloat(0);
								newTmpArr.status =  'A';
								newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
								newTmpArr.unitId = $scope.unitId;
								$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
						  }
    				}
        			
        			// $scope.encounterDetails.payeeId
    				if(cooount == $scope.patientSponsorDetailsDtosListSplit.length){
						$scope.calculateSplitBillTotal($scope.serviceList);
					}
					
					cooount++;
        		
        		});
    		}
    	
		}
    	
    }
    
    $scope.getPayeeCreditDetailsSplit = function(itemList,idx)
    {
    	$scope.payeeeIdxService = idx;
    	$scope.payeeCreditDetailslListSplit = [];
    	// $scope.payeeCreditDetailslListSplit = itemList;
    	// $scope.payeeCreditDetailslListSplit = [];
    	angular.forEach(itemList, function (value, key) {
    		value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
    		$scope.payeeCreditDetailslListSplit.push(value);
    	});
    }
    
    
    $scope.saveGroupDiscountToFinalList = function()
    {
    	var indexList = [];
    	$scope.autoRenderedServicesForSaveAndUpdate=[];  
    	var len = $scope.groupWiseServicesSelectedList.services.length;
			if(len == 0)
				{
					growl.warning("Please select at least one service!",{
						title : ERROR_MSG
					});
		    		return false;
				}
			else if($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '' )
			{
	    		growl.warning("All fields are required.",{
					title : ERROR_MSG
				});
	    		return false;
			}
			else if($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '')
				{
		    		growl.warning("All fields are required.",{
						title : ERROR_MSG
					});
		    		return false;
				}
			else if($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '')
			{
				growl.warning("All fields are required.",{
					title : ERROR_MSG
				});
				return false;
			}else
			{
			    	for(var i = 0;i < len;i++)
					{
						angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
						
			    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
			    				{
			    				angular.forEach($scope.autoRenderedServicesFinalList, function (mainVal, mainKey) {
			    					if(value.orderDetailsId == mainVal.orderDetailsId){
			    						$scope.autoRenderedServicesFinalList[mainKey].concession =value.concession;
			    						$scope.autoRenderedServicesFinalList[mainKey].patientClassPercentage = value.patientClassPercentage;
			    						$scope.autoRenderedServicesFinalList[mainKey].discount = value.discount;
			    						$scope.autoRenderedServicesFinalList[mainKey].discountPercentage = value.discountPercentageFull;
			    						$scope.autoRenderedServicesFinalList[mainKey].discountCategoryId = value.discountCategoryId;
			    						$scope.autoRenderedServicesFinalList[mainKey].totalAmt = value.totalAmt;
			    						$scope.autoRenderedServicesFinalList[mainKey].netPay =value.netPay;
			    						$scope.autoRenderedServicesFinalList[mainKey].discountDetailsList = [];
			    						
			    						$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[mainKey]);
			    						indexList.push(mainKey);
			    					}
			    				});
			    				}
				    	});
						if(i == len -1){
							if(indexList.length > 0)
								$scope.addOrUpdateServices(null,indexList);
		    	            
							if($scope.requestStatusObject != null)
							{
								$scope.getCancelDiscountReqForDiscount();
							}
							 $scope.billWiseDiscountObj = {
										// "discountGivenBy":"",
										"discountGivenBy":$scope.userId,
										"discountCategoryId" : "",
										"discountAmountBillWise" : "",
										"discountPercentageBillWise" : "",
										"approvedAmountBillWise":"",
										"requestUserId":"",
										"requestAmountBillWise" : 0,
								}
							 $scope.requestDiscountDiv = false;
							 $scope.lineItemDiscountAmt = parseFloat(0);
							 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
							 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
							 
						}
			
			}
    	angular.element('#groupWiseDiscountModal').modal('hide');
    	
    	  $scope.billWiseDiscountObj.requestAmountBillWise =0;
   	   $scope.billWiseDiscountObj.discountAmountBillWise =0;
   	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
   		   
   		   $scope.billWiseDiscountObj.discountAmountBillWise  += parseFloat(value.discount);
   		   $scope.billWiseDiscountObj.requestAmountBillWise   += parseFloat(value.discount);
   	   });
   	   $scope.billWiseDiscountObj.discountAmountBillWise  = $scope.billWiseDiscountObj.discountAmountBillWise.toFixed();
   	   $scope.billWiseDiscountObj.requestAmountBillWise  = $scope.billWiseDiscountObj.requestAmountBillWise.toFixed();
   	   $scope.billWiseDiscountObj.discountCategoryId  =  $scope.disCategoryIdForGroup;
   	   
   	   $scope.funDiscountAmountBillWise();
   	   var checkrightsAmount = false;
   	   if($scope.userDiscountObject != null){
	    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) < parseFloat($scope.userDiscountObject.fromDiscountAmount))){
	    		   checkrightsAmount = true;
	    	   }
	    	   if((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise) > parseFloat($scope.userDiscountObject.toDiscountAmount))){
	    		   checkrightsAmount = true;
	    	   }
	    	  }else{
	    		  checkrightsAmount = true;
	    	  }
   	   if(checkrightsAmount == true){
	    		var URI = BASE_URL + ROOT_URL
	    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
	    BillingGenericService.serviceAction(METHOD_GET, URI,"",
	    				NOTIFICATION_MSG_STATUS_FALSE)
	    		.then(function(response) {
	    					//console.log(response);
	    					// $rootScope.startLoader();
	    					if (response.data.status == "success")
	    						{
	    						  $scope.userDiscountList =[];
	    						  $scope.userDiscountList = response.data.listObject;
	    						}
	    						
	    				});
	            $scope.lineLevelItem = true;
			   $scope.requestDiscountDiv = true;
			   $scope.isBillWiseDiscountApplied = true;
			   $scope.finalBill = 'N';
			   growl.warning("Please send discount request to user",{
					title : ERROR_MSG
				});
			   
	    	}else
	    		{
	    		 $scope.requestDiscountDiv = false;
	    		 $scope.billWiseDiscountObj = {
							"discountGivenBy":$scope.userId,
							"discountCategoryId" : "",
							"discountAmountBillWise" : "",
							"discountPercentageBillWise" : "",
							"approvedAmountBillWise":"",
							"requestUserId":"",
							"requestAmountBillWise" : 0,
					}
					$scope.requestDiscountDiv = false;
			    	$scope.isFinancialClearence = 'N';
	    		}
    	
    	}	
    }
    
    
    
    
    $scope.cancelDiscountForServices = function()
    {
    	
		var checkCoupon = false;
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
				{
					if(value.patientCouponTemplateMapperId != null )
					{
						checkCoupon = true;
					}
				});
		
		if(checkCoupon == true){
			
			growl.warning("Coupon discount will be get cancel after applying/cancel total discount",{
				title : WARNING_MSG
			});
			
			$scope.finalCancelCoupon();
			
			$timeout(function () {

				var i = 1;
		    	var flag = false;
		    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		    		if(parseFloat(value.discount) > 0){
		    			flag = true;
		    		}
		    		if(i == $scope.autoRenderedServicesFinalList.length && flag == true){
		    			angular.element('#cancelDiscountPopup').modal('show');
		    		}
		    		i++;
		    	});

			}, 7000);
			
		}else{
			
			var i = 1;
	    	var flag = false;
	    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
	    		if(parseFloat(value.discount) > 0){
	    			flag = true;
	    		}
	    		if(i == $scope.autoRenderedServicesFinalList.length && flag == true){
	    			angular.element('#cancelDiscountPopup').modal('show');
	    		}
	    		i++;
	    	});
		}
    	
    }
    
    
    $scope.finalCancelDiscount = function()
    {
    	var cancelDiscountObjectList = [];
    	var cancelRemoveDiscountList = [];
    	var indexList = [];
    	$scope.autoRenderedServicesForSaveAndUpdate=[];
    	$scope.lineLevelItem = false;
			var j = 0;
			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
		{
			if($scope.autoRenderedServicesFinalList[j].discountPercentage > 0)
			{
				var totalAmt = 0;
				var dis = 0;
				var gstAmt = 0;
				var withConDic = 0;
    		
    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
					$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
					$scope.autoRenderedServicesFinalList[j].discount = 0;
					$scope.roundOffAmt = 0.00;
					
					totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					
					withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
					gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);

					$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
					$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
					$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
					$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
		    	
		    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
		    		indexList.push(j);
				}	
    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
    			{
    				if($scope.requestStatusObject != null)
    				{
						$scope.getCancelDiscountReqForDiscount();
					}
    	            $scope.addOrUpdateServices(null,indexList);
    				
    				 $scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
								
						}
    				 $scope.requestDiscountDiv = false;
					 $scope.lineItemDiscountAmt = parseFloat(0);
					 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
					 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
    			}
				
    		j++;
			
		});
    }
    
    
// for skip invoice creation
	  $scope.saveCompanyInvoiceCreation = function(){
		  
					var totalAmount = 0;
				    var dueAmount = 0;
				    var crNetAmount = 0;
					angular.forEach($scope.companyInvoiceList,function(value,index){
						if (value.companyInvoiceSelected == true) {
								totalAmount = parseFloat(totalAmount) + (isNaN(value.billAmount) ? 0 : parseFloat(value.billAmount));
								dueAmount = parseFloat(dueAmount) + (isNaN(value.billAmount) ? 0 : parseFloat(value.billAmount));
								crNetAmount = parseFloat(crNetAmount) + (isNaN(value.crNetAmount) ? 0 : parseFloat(value.crNetAmount));
								$scope.billNumber = value.billNumber;
								
						}
						
					});
					  $scope.mCompanyInvoiceMasterDto={
							  "companyInvoiceId":"",
							  "invoiceNumber":"1",
							  "paymentEntitlementId":$scope.paymentEntitlementIdInvoice,
							  "payeeId":$scope.encounterDetails.payeeId,
							  "associateCompanyId":$scope.associateCompanyIdInvoice,
							  "totalAmount":totalAmount+crNetAmount,
							  "dueAmount":dueAmount,
							  "crNetAmount":crNetAmount,
							  "unitId":$scope.unitId,
							  "orgId":$scope.organizationId,
							  "createdBy":$scope.userId,
							  "updatedBy":"",
							  "invoiceDate":"",
							  "createdDate":"",
							  "updatedDate":"",
							  "billNumber":$scope.billNumber,
							  "invoiceCreationInsurance":$scope.unitConfigurationObject.invoiceCreationInsurance,
							  "invoiceCreationCorporate":$scope.unitConfigurationObject.invoiceCreationCorporate,
							
							}
					
					var listTCompanyInvoiceDetailsDto = [];
					angular.forEach($scope.companyInvoiceList,function(value,index){
						if (value.companyInvoiceSelected == true) 
						{
							var temp ={
									   "companyInvoiceDetailId":"",
									   "companyInvoiceId":"",
									   "billPayeeId":value.payeeId,
									   "billId":(value.pharamcy==false)?value.billId:'',
									   "tSaleId":(value.pharamcy==true)?value.billId:'',
									   "payableAmount":value.billAmount+value.crNetAmount,//amount from bill payee details
									   "dueAmount":value.billAmount,//amount from bill payee details
									   "billAmount":value.netAmount,//amount from bill master
									   "crNetAmount":value.crNetAmount,
									   "unitId":$scope.unitId,
									   "orgId":$scope.organizationId,
									   "createdBy":$scope.userId,
									   "updatedBy":"",
									   "createdDate":"",
									   "updatedDate":"",
									   "billPayeeDetailsId":value.billPayeeDetailsId,
									   "associateCompanyId":value.associateCompanyIdInvoice
									}
							listTCompanyInvoiceDetailsDto.push(temp);
						}
						
					});
				
				
						$rootScope.startLoader();
						var param = {
						        mCompanyInvoiceMasterDto:$scope.mCompanyInvoiceMasterDto,
						        listTCompanyInvoiceDetailsDto:listTCompanyInvoiceDetailsDto,
								orgId:$scope.organizationId,
								unitId:$scope.unitId,
								isConsolidatedBill : $scope.isConsolidatedBill 
						    }
						 console.log("param",JSON.stringify(param));
						
						var URI = BASE_URL + ROOT_URL + "/api/billing/saveCompanyInvoiceCreationList";
						BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								//alert("invoice created");
							}else if (response.data.status == "error"){
							}
							$rootScope.stopLoader();
						});
	  }					    
	
	$scope.getBillForCompanyInvoice = function() {
		
		angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
				 {
					if(value.priorityId == '1')
					{
						$scope.associateCompanyDescInvoice = value.insuranceDesc;
						$scope.associateCompanyIdInvoice = value.insuranceId;
						$scope.paymentEntitlementIdInvoice = value.paymentEntitlementId;
						
					}
				 });
		var skipInvoive =false;
		if(($scope.paymentEntitlementIdInvoice == 2)){
			if(($scope.unitConfigurationObject.invoiceCreationInsurance == 'N')){
				skipInvoive =true;
			}
		}
		
		if(($scope.paymentEntitlementIdInvoice == 3)){
			if(($scope.unitConfigurationObject.invoiceCreationCorporate == 'N')){
				skipInvoive =true;
			}
		}
		if(skipInvoive == true){
				
		$scope.companyInvoiceList = [];
		  var param = {
				  "orgId":$scope.organizationId,
				  "orgUnitId":$scope.unitId,
				  "payeeId":$scope.encounterDetails.payeeId,
				  "associateCompanyId":$scope.associateCompanyIdInvoice,
				  "visitTypeId" :$scope.visitTypeId,
				  "billId" :$scope.billIdInvoice
			};
				  
		    var URI = BASE_URL + ROOT_URL + "/api/billing/companyInvoiceCreationListByBillId"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.companyInvoiceList = response.data.listObject;
					 angular.forEach($scope.companyInvoiceList,function(v,k){
						 v.companyInvoiceSelected =true;
					 });
					 $scope.saveCompanyInvoiceCreation();
				}
				$rootScope.stopLoader();
			});
		}
	  }
	// function for popup ok button call
	$scope.PopupOkBtn = function() {
		$scope.popUpFlag = false;
		$scope.saveBillings();
		$(".patienttSearchResult").toggle();
	}
	$scope.lineItemDiscountAmt = parseFloat(0); // 12-10-2019
	
	
	
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
	
	
	
$scope.saveOpdBill = function(){
		if(parseFloat($scope.totalSelfPayble) < 0){
			growl.error("SelfPayble amount should be greater than or equal to ZERO",{
				title : ERROR_MSG
			});
			return;
		}
		
		
		if($scope.isFinancialClearence=='Y' && $scope.financialFlag == 'N'){
			if($scope.finanacial.financialRequestSendTo == undefined || $scope.finanacial.financialRequestSendTo == null || $scope.finanacial.financialRequestSendTo == ""){
				growl.warning("Please select user to send financial clearance  request",{
					title : WARNING_MSG
				});
				return false;
			}
		}
		
		if($scope.finalBill == 'Y' && $scope.cashSettlement == 'Y'){
			if($scope.visitTypeId == 1){
				if($scope.billSettlement  == false){
					growl.warning("user do not have bill settlements rights",
							{
								title : 'Error!'
							});
				    
					return false;
				}
			}
			$scope.cashCollectedAmount = $scope.cashCollectedAmount +  parseFloat($scope.totalSelfPayble);
			if( ($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null ) && $scope.cashCollectedAmount > $scope.cashAmountLimit ){
				growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
					title : WARNING_MSG
				});
				return;
			}
			
			
			if($scope.cashAmountValidation != null && parseFloat($scope.totalSelfPayble) >= $scope.cashAmountValidation){
				
				$('#uploadpanCardDetailsOPD').modal('show');
			}else{
				$scope.saveBillings();
			}
			
		}else{
			$scope.saveBillings();
		}
		
	}

$scope.addOpdBill = function(){
	
	if($scope.fileObj.file == null){
		growl.warning("Please select file ",{
			title : WARNING_MSG
		});
		return false;
	}else{
		$('#uploadpanCardDetailsOPD').modal('hide');
		if($scope.finalBill == 'Y' && $scope.cashSettlement == 'Y'){
			
			$scope.saveBillings();
			
		}else if($scope.finalBill == 'Y' && $scope.cashSettlement == 'N'){
			
			$scope.settleBill();
		}
	}
	
}
	

	$scope.saveBillings = function()
	{
		$scope.skipMpesaValidation = false;
		$scope.pinpadAmounFlag = false;
		if(Object.keys($scope.autoRenderedServicesFinalList).length == 0)
			{	
				growl.warning(" No service to save..Please add service..!!",{
					title : ERROR_MSG
				});
				return false;
				
			}
		else{
			if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && ($scope.requestStatusObject != null || $scope.requestDiscountDiv == true) ){
			  if($scope.billWiseDiscountObj.requestUserId == "")
				{
					growl.warning("Please select request user for discount!",{
						title : ERROR_MSG
					});
					return false;
				}
			else
				{
				// $scope.saveBillngNew();
				$scope.saveBtnDisable = false;
				angular.element('#saveConfirmPopup').modal('show');
				}
			
		}else{
			
			if($scope.userDiscountObject == null || $scope.userDiscountObject == undefined)
			{
				if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined){
						if($scope.requestStatusObject.reqStatus == 1) // Already
																		// Pending
							{
							$scope.finalBill = 'N';
							 growl.warning("Discount request is pending..!!",{
									title : WARNING_MSG
								});
					    		return false;
							
							}else if($scope.requestStatusObject.reqStatus == 2){
								
								if($scope.totalDiscountSummary > $scope.requestStatusObject.approvedDiscAmt)
									{
										 growl.warning("Discount approved amount is "+$scope.requestStatusObject.approvedDiscAmt+".",{
												title : WARNING_MSG
											});
								    		return false;
									}
								else
									{
									 // $scope.saveBillngNew();
									$scope.saveBtnDisable = false;
									// angular.element('#saveConfirmPopup').modal('show');
									if($scope.finalBill == 'Y' && $scope.cashSettlement == 'N')
									{
										$scope.saveBillngNew();
									}else{
										angular.element('#saveConfirmPopup').modal('show');
									}
									
									}
						
					}
					// BR-PENDING-billing-save-button-hang fix: requestStatusObject is left as a
					// non-null placeholder object ({}) whenever no discount-approval request was ever
					// raised for this bill (reqStatus is neither 1/Pending nor 2/Approved), so this
					// if/else-if chain previously fell through doing nothing -- no save, no popup, no
					// warning -- leaving the Save button looking clickable but inert. Same fallback as
					// the requestStatusObject==null branch just below.
					else
						{
						$scope.saveBtnDisable = false;
						if($scope.finalBill == 'Y' && $scope.cashSettlement == 'N')
							{
								$scope.saveBillngNew();
							}else{
								angular.element('#saveConfirmPopup').modal('show');
							}
						}
			}
				else{
					if(($scope.userDiscountObject == null || $scope.userDiscountObject == undefined) &&  ($scope.totalDiscountSummary == 0 ||$scope.totalDiscountSummary == 0.0 || $scope.totalDiscountSummary == '' || $scope.totalDiscountSummary == null))
							{
						         // $scope.saveBillngNew();
								$scope.saveBtnDisable = false;
								if($scope.finalBill == 'Y' && $scope.cashSettlement == 'N')
								{
									$scope.saveBillngNew();
								}else{
									angular.element('#saveConfirmPopup').modal('show');
								}
							}
					else{
							if($scope.totalDiscountSummary > 0 && ($scope.billWiseDiscountObj.requestUserId == null || $scope.billWiseDiscountObj.requestUserId == '' || $scope.billWiseDiscountObj.requestUserId == undefined)){
								$scope.finalBill = 'N';
								$scope.listOfOldDisAmt = [];
								 $scope.isBillWiseDiscountApplied = true;
								 $scope.lineItemDiscountAmt = $scope.totalDiscountSummary;
								// $scope.billWiseDiscountObj.discountAmountBillWise
								// = $scope.lineItemDiscountAmt;
								 $scope.billWiseDiscountObj.discountAmountBillWise =0;
								 $scope.billWiseDiscountObj.discountPercentageBillWise =0;
								 
								 var disPerForUser = (parseFloat($scope.totalDiscountSummary)/parseFloat($scope.totalDisServiceWise) *100).toFixed(2);
							    	
								 
						    		var URI = BASE_URL + ROOT_URL
						    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.totalDiscountSummary+"/per/"+disPerForUser;
						    BillingGenericService.serviceAction(METHOD_GET, URI,"",
						    				NOTIFICATION_MSG_STATUS_FALSE)
						    		.then(function(response) {
						    					// console.log(response);
						    					// $rootScope.startLoader();
						    					if (response.data.status == "success")
						    						{
						    						  $scope.userDiscountList =[];
						    						  $scope.userDiscountList = response.data.listObject;
						    						}
						    						
						    				});
						    
						           $scope.finalBill = 'N';
								   $scope.requestDiscountDiv = true;
								   $scope.billWiseDiscountObj.requestAmountBillWise =  $scope.lineItemDiscountAmt;
								   growl.warning("Please send discount request to user",{
										title : WARNING_MSG
									});
						    		return false;
							
							}else{
								// $scope.saveBillngNew();
								$scope.saveBtnDisable = false;
								angular.element('#saveConfirmPopup').modal('show');
							}
					    		
				  }
				}
		    		
			}
			else if($scope.userDiscountObject != null || $scope.userDiscountObject != undefined)
				{	
						// if(approvedAmtApp)

		    	if($scope.userDiscountObject != null){
		    		
					if($scope.userDiscountObject.discountType == 1){
						var fromDisAmt = (parseFloat($scope.totalDisServiceWise) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
						var toDisAmt = (parseFloat($scope.totalDisServiceWise) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
						$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
						$scope.userDiscountObject.toDiscountAmount = toDisAmt;
					}
		    	}
		    	
						if(($scope.userDiscountObject.toDiscountAmount >= $scope.totalDiscountSummary) && ($scope.listOfOldDisAmt == undefined || Object.keys($scope.listOfOldDisAmt).length == 0))
							{
								 if($scope.billWiseDiscountObj.requestAmountBillWise == 0)
									 {
										 $scope.listOfOldDisAmt = [];
										 $scope.isBillWiseDiscountApplied = false;
										 $scope.requestDiscountDiv = false;
										 $scope.lineItemDiscountAmt = parseFloat(0);
										 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
										 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
										 $scope.discountRadio = 2;
										 // $scope.saveBillngNew();
										 $scope.saveBtnDisable = false;
										 // angular.element('#saveConfirmPopup').modal('show');
										 if($scope.finalBill == 'Y' && $scope.cashSettlement == 'N' && $scope.isFinancialClearence == 'N')
											{
												$scope.saveBillngNew();
											}else{
												angular.element('#saveConfirmPopup').modal('show');
											}
									 }
								 else{
									 $scope.discountRadio = 2;
									// $scope.saveBillngNew();
									 $scope.saveBtnDisable = false;
									 angular.element('#saveConfirmPopup').modal('show');
								 }
						    		
							  
							}
						else if(($scope.userDiscountObject.toDiscountAmount >= $scope.totalDiscountSummary) && $scope.listOfOldDisAmt.length > 0)
							{
							   // $scope.saveBillngNew();
							$scope.saveBtnDisable = false;
							angular.element('#saveConfirmPopup').modal('show');
							}
						else
							{
								if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined)
								{
										
									
									if($scope.requestStatusObject.reqStatus == 1) // Already
																					// Pending
									{
									
									 growl.warning("Discount request is pending..!!",{
											title : WARNING_MSG
										});
							    		return false;
									
									}else if($scope.requestStatusObject.reqStatus == 2){
										
										if($scope.totalDiscountSummary > $scope.requestStatusObject.approvedDiscAmt)
											{
										    	   
												 growl.warning("Discount approved amount is "+$scope.requestStatusObject.approvedDiscAmt+".",{
														title : WARNING_MSG
													});
										    		return false;
											}
										else if($scope.totalDiscountSummary <= $scope.requestStatusObject.approvedDiscAmt)
											{
											   // $scope.saveBillngNew();
											$scope.saveBtnDisable = false;
											angular.element('#saveConfirmPopup').modal('show');
											}
								
							}
								
								}else
									{
									
									var checkCouponOrNot = false;
									var pharmacyServiceCounter=false;
									angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
											{
												if(value.patientCouponTemplateMapperId != null){
													checkCouponOrNot = true;
												}
												if($scope.pharmacyServiceId!=value.serviceMasterId && value.discountPercentage!=undefined && value.discountPercentage!=null
														&& value.discountPercentage!='' && value.discountPercentage!=0 )
													{
													pharmacyServiceCounter=true;
													}
											});
									
									if(pharmacyServiceCounter==true)
										{
									if(checkCouponOrNot == false && $scope.totalDiscountSummary > 0 && $scope.isFinancialClearence =='N' && ($scope.billWiseDiscountObj.requestUserId == null || $scope.billWiseDiscountObj.requestUserId == '' || $scope.billWiseDiscountObj.requestUserId == undefined)){
										$scope.listOfOldDisAmt = [];
										 $scope.isBillWiseDiscountApplied = true;
										 $scope.lineItemDiscountAmt = $scope.totalDiscountSummary;
										 $scope.billWiseDiscountObj.discountAmountBillWise = $scope.lineItemDiscountAmt;
										 $scope.billWiseDiscountObj.discountPercentageBillWise =  (parseFloat($scope.lineItemDiscountAmt)/(parseFloat($scope.totalDisServiceWise))*100).toFixed(2)
										// $scope.funDiscountPercentageBillWise();
										 $scope.discountRadio = 2;
										    $scope.finalBill = 'N';
								    		var URI = BASE_URL + ROOT_URL
								    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
								    BillingGenericService.serviceAction(METHOD_GET, URI,"",
								    				NOTIFICATION_MSG_STATUS_FALSE)
								    		.then(function(response) {
								    					// console.log(response);
								    					// $rootScope.startLoader();
								    					if (response.data.status == "success")
								    						{
								    						  $scope.userDiscountList =[];
								    						  $scope.userDiscountList = response.data.listObject;
								    						}
								    						
								    				});
								    if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined){
										   $scope.requestDiscountDiv = true;
										   $scope.billWiseDiscountObj.requestAmountBillWise =  $scope.lineItemDiscountAmt;
										   growl.warning("Please send discount request to user",{
												title : WARNING_MSG
											});
								    		return false;
										}else{
											// $scope.saveBillngNew();
											$scope.saveBtnDisable = false;
											angular.element('#saveConfirmPopup').modal('show');
										}
									}
									else
										{
										$scope.saveBtnDisable = false;
										angular.element('#saveConfirmPopup').modal('show');
										}
									}
									else
										{
										$scope.saveBtnDisable = false;
										angular.element('#saveConfirmPopup').modal('show');
										}
							}
				}}
			else
				{
				// $scope.saveBillngNew();
				$scope.saveBtnDisable = false;
				angular.element('#saveConfirmPopup').modal('show');
				}
			
		}// for DD
	  }
	}
	
	$scope.finalBillClick = function(val){
		
		if(val == 'N'){
			$scope.cashSettlement = 'N';
		}
	}
	
	// For Collect Payment Start
	
	
	
	$scope.addBtnDisabled =false;
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
					//consumedAmt = (parseFloat(consumedAmt)+parseFloat($scope.dueAmount)).toFixed(2);
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
	
	
	$scope.paymentModes = {
			1: "Cash",
			2: "Cheque",
			3: "Credit Card",
			4: "E - Transfer",
			5: "Loyalty Card"	
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
	
	
	$scope.checkAmt = function(amt)
	{
		if(isNaN(Number(parseFloat(amt))))
			{
			 $scope.paymentDetails.loyaltyCardAmt= 0.00;
			}
		else{
			 $scope.paymentDetails.loyaltyCardAmt= amt;
		}
	}
	
	$scope.getLoyaltyCardByNo = function(cardNo)
	{
		if(cardNo!="" && cardNo.toString().length==16)
			{
			var data = {
					"patientId" : '',
					  "organisationId" : $scope.organizationId,
					  "unitId" : $scope.unitId,
					  "status" : "A",
					  "isDeleted" : "N",
					  "isUserDefined":"Y",
					  "cardNo":cardNo
			}
			var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getPatientDetailsByLoyaltiCardNo";
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				{
					$scope.send =false;
					$scope.resend =false;
					if(response.data.listObject!=null && response.data.listObject.length>0)
					{
						var cardAmtToSub = 0;
				    	$scope.loyaltyCardObj = response.data.listObject[0];
					    $scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
					    if($scope.listPaymentDetails!=null && $scope.listPaymentDetails.length>0)
					    	{
					    	   angular.forEach($scope.listPaymentDetails,function(value,key)
					    	    {
					    		   if(value.paymentModeId==6 && value.cardNumber==cardNo)
					    			   {
					    			     cardAmtToSub = cardAmtToSub + parseFloat(value.amount); 
					    			   }
									
								});
					    	}
					      var cardBalAmountTmp = $scope.loyaltyCardObj.cardAmount-cardAmtToSub;
					     $scope.paymentDetails.cardBalAmount = parseFloat(cardBalAmountTmp).toFixed(2); 
					}
				}
			});
			}

	}
	
/*
 * $socpe.count ; if(flag) { $socpe.count=0; $socpe.count=+1; } else if(!flag) {
 * $socpe.count=-1; }
 */
	$scope.generateOtp = function(flag)
	{
		if($scope.paymentDetails.cardBalAmount==0||$scope.paymentDetails.cardBalAmount=='')
			{
			growl.warning("Please Recharge Your card...!!",
					{
						title : 'Warning!'
					});
			return false;
               	}
		if($scope.paymentDetails.loyaltyCardAmt==0||$scope.paymentDetails.loyaltyCardAmt==''||$scope.paymentDetails.loyaltyCardAmt==undefined||
				$scope.paymentDetails.loyaltyCardAmt==null)
			{
			growl.warning("Amount Should be greater than zero...!!",
					{
						title : 'Warning!'
					});
			    return false;
			 }
		if(parseFloat($scope.paymentDetails.loyaltyCardAmt)>parseFloat($scope.paymentDetails.cardBalAmount))
			{
			growl.warning("Insufficient fund in your account...!!",
					{
						title : 'Warning!'
					});
			    return false;
			 }
		if($scope.loyaltyCardObj.mobileNumber==''||$scope.loyaltyCardObj.mobileNumber==undefined||
				$scope.loyaltyCardObj.mobileNumber==null)
			{
			growl.warning("Mobile number not Found...!!",
					{
						title : 'Warning!'
					});
			    return false;
			 }
		
			$rootScope.startLoader();
			if($scope.paymentDetails.loyaltyCardNo!="" && $scope.paymentDetails.loyaltyCardNo.toString().length==16)
				{
				var data = {
						  "patientId" : '',
						  "organisationId" : $scope.organizationId,
						  "unitId" : $scope.unitId,
						  "status" : "A",
						  "isDeleted" : "N",
						  "isUserDefined":"Y",
						  "cardNo":$scope.paymentDetails.loyaltyCardNo,
						  "mobileNumber":$scope.loyaltyCardObj.mobileNumber,
						  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
						  "resendOTP" :flag,
						  "email":$scope.loyaltyCardObj.email,
						  "firstName":$scope.loyaltyCardObj.firstName,
						  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt
				}
				var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/generateOTP";
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
	
	$scope.sendOTP = function(flag)
	{
		loyaltiCardCallFactory.sendOTP(flag,$scope);
	}
	$scope.reSendOTP = function(flag)
	{
		loyaltiCardCallFactory.sendOTP(flag,$scope);
		// $scope.resend = (flag=='N')?true:false;
		// $scope.send =false;
	}
	
	
	$scope.clearLoylty = function(){
		
		$scope.send = false;
		$scope.validatTextFieldFlag = true;
		$scope.paymentDetails.otp ="";
		$scope.calculateTotalDueRefAmount();
		$scope.getLoyaltyCardList($scope.patientDetails.patientId);
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
					  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt,
					  "isUserDefined":"Y",
					  "otp": $scope.paymentDetails.otp,
					  "cardNo":$scope.paymentDetails.loyaltyCardNo,
					  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
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
							$scope.isBillList = 0;
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
	

	$scope.loyaltyCardObj ={
			
			"loyaltyCardNo" :"",
			"loyaltyCardBalance" :"",
			"authKey":0
	}
	
	
	/*
	 * *************************************Mpesa Integration
	 * start******************************************
	 */
	$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
	$scope.validateMpesaDetails = function(mpesaObject){
		$rootScope.startLoaderPopup();
		// console.log("MPesa Object:",mpesaObject);
		
		$scope.skipMpesaValidation = true;
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
			growl.error("Enter both transaction id and mobile number",{
				title : ERROR_MSG
			});
			$rootScope.stopLoaderPopup();
		}
	}
	
	/*
	 * $scope.mobileNumber =""; $scope.validateMpesaAmount ="";
	 */
	
	$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
		
		
		if(validateMpesaAmount == undefined || validateMpesaAmount == null){
			
			growl.warning("Please Enter Amount",{
				title : "warning!!!"
			});
			
		}else if(validateMpesaAmount > $scope.payeeDueAmtFinal){
			
			growl.warning("Amount should be equel or less than due amount...!!",{
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
					phoneNumber:parseInt( $scope.countryCallingCode+""+mobileNumber),
					partyA:mobileNumber,
					amount:validateMpesaAmount
			};
		
			if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
					( $scope.countryCallingCode != undefined &&  $scope.countryCallingCode != null &&  $scope.countryCallingCode != "")){
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
				uhidNumber: $scope.selectedUhid
				
		};
		$scope.skipMpesaValidation = true;
		$rootScope.startLoader();
		var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
		BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			
			if (response.data.status == "success")
				{
				$scope.mpesaObject.mpesaAmount = response.data.object.amount;
				$scope.mpesaObject.transactionId = response.data.object.transactionId;
				$scope.mpesaObject.resTransactionId = response.data.object.transactionId;
				$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
                if($scope.mpesaObject.transactionId != null ){
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

	/*
	 * *************************************Mpesa Integration
	 * END******************************************
	 */
	
	
	
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
	
	$scope.medallPrifixList =[];
	$scope.medallUnitObj ={};
	$scope.medallDoctorList =[];
	$scope.getMedalDdetails = function(){
		var data = {
				  "organizationId" : $scope.organizationId,
				  "unitId" : $scope.unitId,
		}
		var URI = BASE_URL + ROOT_URL + "/adt/getPrefixMasterAndMedallPrefixMaster";
		BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
				$scope.medallPrifixList =response.data.listObject;
				$scope.medallUnitObj =response.data.object;
				$scope.medallDoctorList =response.data.resObject;
				
			}
		});
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
			$scope.razorPayObject.razorAmount = $scope.dueAmount;
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
			$scope.razorPayObject.razorAmount = $scope.dueAmount;
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
	
	
	$scope.getBillDetails = function(){
		 
		// for Bill start
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
		$scope.patientIdForReport = $scope.encounterDetails.patientId;
		$scope.visitTypeId = 1;
		
		$scope.consumedAmt = parseFloat(0);
		
		$scope.payeeId = $scope.selfPayeeIdCookies;
		
		$scope.billDisabled = false;
		// $scope.billId = bill.billingMasterId;
		$scope.totalAmount = $scope.totalSelfPayble; 
		$scope.dueAmount = $scope.totalSelfPayble;
		$scope.oldDueAmount = $scope.totalSelfPayble;
		$scope.billDueAmount =$scope.totalSelfPayble;
		$scope.currentBill = {};
		$scope.currentBill.amountPaid = parseFloat(0);
		// $scope.admissionId = bill.visitTypeId == 1 ? bill.visitId : null;
		$scope.isFinancialClearance = false;
		$('#isFinancialClearance').prop('checked',false);
		
		$scope.initPaymentDetails();
		$scope.validatTextFieldFlag = true;
		$scope.getLoyaltyCardList($scope.encounterDetails.patientId);
		$scope.payeeDueAmtFinal = $scope.totalSelfPayble;
		$scope.payeeDueAmtStatic = $scope.totalSelfPayble;
						
		// get available active deposits by patient
		var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.encounterDetails.patientId+"/"+0+"/1/1/false";
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
				
				$scope.consumedAmt=0;
				$scope.availableDeposits =[]; 
				angular.forEach(response.data.listObject , function(value,key){
					
					if(value.depositTypeId == 2){
						if(parseInt($scope.encounterDetails.encounterId) == value.encounterId){
							$scope.availableDeposits.push(value);
						}
					}else{
						$scope.availableDeposits.push(value);
					}
						
				});
//				$scope.availableDeposits = response.data.listObject;
				var activeDeposit = parseFloat(0);
				var activeDepositPatient = parseFloat(0);
				var billAmt = parseFloat($scope.payeeDueAmtStatic);
				var i = 0
				angular.forEach($scope.availableDeposits,function(value,key)
				{
					$scope.availableDeposits[i].oldConsumedDeposit = value.consumedDeposit;
					$scope.availableDeposits[i].oldAvailableDeposit =  value.availableDeposit;
				/*
				 * if(value.depositTypeId == '2' ) { activeDeposit =
				 * activeDeposit + parseFloat(value.availableDeposit);
				 * 
				 * if(billAmt >0){
				 * 
				 * if(billAmt >= value.availableDeposit) { value.consumedDeposit =
				 * value.consumedDeposit+value.availableDeposit;
				 * $scope.availableDeposits[i].depositeFlag = 'Y';
				 * $scope.transferToActiveDepositeFlag = true; $scope.dueAmount =
				 * ($scope.dueAmount - value.availableDeposit).toFixed(2);
				 * billAmt = billAmt-value.availableDeposit;
				 * $scope.availableDeposits[i].availableDeposit = parseFloat(0); }
				 * else if(billAmt < value.availableDeposit) {
				 * 
				 * value.availableDeposit = parseFloat(value.availableDeposit)-
				 * parseFloat(billAmt); value.consumedDeposit =
				 * value.consumedDeposit + parseFloat(billAmt);
				 * $scope.availableDeposits[i].depositeFlag = 'Y';
				 * $scope.dueAmount = ($scope.dueAmount - billAmt).toFixed(2);
				 * $scope.payeeDueAmtFinal = 0; billAmt = 0; //$scope.dueAmount =
				 * ($scope.dueAmount -$scope.consumedAmt).toFixed(2); } }else{
				 * $scope.availableDeposits[i].depositeFlag = 'N'; }
				 *  } else {
				 */
						activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
						$scope.availableDeposits[i].depositeFlag = 'N';
						activeDepositPatient = activeDepositPatient + parseFloat(value.availableDeposit);
						/* } */
					i++;
					
				});
				$scope.activeDeposit = activeDeposit;
				$scope.useActiveDepo = true;
				
			}
			$scope.calculateTotalDueRefAmount();
		});
	  // for Bill End
		
		
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
		
		
	}
	
	
	/*
	 * paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
	 * paymentDepositDetailsList : $scope.finalAvailableDeposits,
	 * totalDepositConsumption : $scope.consumedAmt,
	 */
	 $scope.collectPayment = function(){
		 
		 $scope.getBillDetails();
		 $scope.send=false;
		 $scope.resend=false;
		 $scope.setFirstTabLevel(1);
		 $scope.setPaymentModeId(1);
		 $scope.validatTextFieldFlag = true;
		 $scope.getLoyaltyCardList($scope.encounterDetails.patientId);
		 $scope.transferToActiveDeposit = 'N';
		 $scope.excessAmount = parseFloat(0).toFixed(2);
		 $scope.listPaymentDetails = [];
		$scope.calculateTotalDueRefAmount();
		 $scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
		 angular.element('#collectPayment').modal('show');
	 }
	 
	 
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
			var collectedAmount = parseFloat(0).toFixed();
			$scope.receivedFrom=$scope.listPaymentDetails.amountReceivedFrom;
			angular.forEach($scope.listPaymentDetails,function(value,key){
				value.receivedFrom=$scope.listPaymentDetails.amountReceivedFrom!=undefined ? $scope.listPaymentDetails.amountReceivedFrom :'';
				if(value.paymentModeId == 1){
					collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
				}
				
			});
			
			if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
				$('#uploadpanCardDetailsOPD').modal('show');
					return false;
			}
			$scope.billDisabled = true;
			 angular.element('#collectPayment').modal('hide');
			 $rootScope.startLoader();
			// console.log("LISt CHECK",$scope.availableDeposits);
			
			
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
					/*
					 * newArr.billId = $scope.billId; newArr.billPayeeDetailsId =
					 * billPayeeDetailsId;
					 */
					newArr.consumedAmount = (parseFloat($scope.availableDeposits[i].consumedDeposit).toFixed(2) - $scope.availableDeposits[i].oldConsumedDeposit); 
					newArr.createdBy = $scope.userId;
					$scope.finalAvailableDeposits.push(newArr);
					// $scope.finalAvailableDeposits.push($scope.availableDeposits[i]);
					}
				i++;
				
			});
		
			$scope.finalOtReservationList = [];
			angular.forEach($scope.otReservationList ,function(value,index){
				if(value.isSelected){
					$scope.finalOtReservationList.push(value);
				}
			});
			
			
			$scope.settleobj.paymentReceiptDetailsDtosList = $scope.listPaymentDetails;
			$scope.settleobj.paymentDepositDetailsList = $scope.finalAvailableDeposits
			$scope.settleobj.totalDepositConsumption = $scope.consumedAmt;
			$scope.settleobj.isTransferToActiveDeposit = 'N';
			$scope.settleobj.advisedByName = $scope.searchAdvised.advisedByName;
			$scope.settleobj.advisedById = $scope.searchAdvised.advisedById;
			$scope.settleobj.receivedFrom=$scope.receivedFrom;
			
			// console.log("param",$scope.settleobj);
			
			var URI = BASE_URL + ROOT_URL + SAVEBILL;
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.settleobj , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				// //console.log(response)
				if(response.data.status == 'success')
				{
					// alert("Success SET Billed");
					$rootScope.stopLoader();
					$scope.billIdInvoice = response.data.object;
					$scope.getBillForCompanyInvoice();
					
					$scope.btnDisabled = false;
					if($scope.isPrebarcodeGenerantion != undefined && $scope.isPrebarcodeGenerantion != null && $scope.isPrebarcodeGenerantion == 'Y'){
						$scope.getWorkListDetails();
					}
					var id=response.data.object;
					var recieptObj=response.data.resObject;
					if($scope.fileObj.file != null){
						$scope.uploadEncounterDoc(response.data.object);
					}
					$scope.billId=id;
					 $scope.ServiceCount=0;
					if(recieptObj !=null)
					{
						$scope.recieptId=recieptObj.recieptId;
						$scope.depositeConsumptionId = recieptObj.depositConsumptionId
					}
					$scope.preBillId = "";
				    console.log("$scope.autoRenderedServicesFinalList",$scope.autoRenderedServicesFinalList);

					if($scope.outsourceLabId == 1){		
						
						$scope.ordersIdListNeuron = [];
					angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
						 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
							{
								var saveNuroPlus = {};
								saveNuroPlus.branchName = "NphMain";
								saveNuroPlus.orderId = value.orderId.toString();
								if($scope.encounterNumber != undefined){
									saveNuroPlus.IPOPNo = $scope.encounterNumber;
								 }else{
									saveNuroPlus.IPOPNo = $scope.patientEncounterList[0].encounterNumber;
								 }
								saveNuroPlus.UHID =  $scope.patientDetails.uhIdNumber;
								var orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
								var orderSchDate = new Date().getTime();
								saveNuroPlus.orderTs =value.orderDateString+"T"+value.orderDateTime;
									//"2022-02-15T13:24:00";
								saveNuroPlus.ipoptype = "OPD";
								saveNuroPlus.salutation =  $scope.patientDetails.genderId.toString();;
								saveNuroPlus.patientName =  $scope.patientDetails.patientName;
								saveNuroPlus.gender = $scope.patientDetails.genderCode;
							     var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
							     var Bx = moment(Birth).format('YYYY-MM-DD');
								saveNuroPlus.birthDate = Bx+"T00:00:00";
								saveNuroPlus.ward ="";
								saveNuroPlus.patMobileNo = $scope.mobileNumber.toString();
								saveNuroPlus.patEmailId = $scope.patientDetails.email;
								saveNuroPlus.patAddress = $scope.patientDetails.address;
								saveNuroPlus.patCity = $scope.patientDetails.cityName;
								saveNuroPlus.refDrCode = value.doctorName;
								saveNuroPlus.refDrName = value.doctorName;
								saveNuroPlus.feesPaid = false;
								saveNuroPlus.orderTests=[];
								if($scope.ordersIdListNeuron.length==0){
								$scope.ordersIdListNeuron.push(saveNuroPlus);
							}else{
								isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.ordersIdListNeuron,saveNuroPlus)
								  if (!isGroupExistsCap) {
									$scope.ordersIdListNeuron.push(saveNuroPlus);
								  }
							}
					     }
					});
					
					angular.forEach($scope.ordersIdListNeuron, function (value, key) {
					    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
						    if(value.orderId == value1.orderId){
						    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
						    		var newAer = {};
						    		newAer.testRecordId = value1.serviceMasterId.toString();
						    		newAer.testName = value1.serviceStandardName;
						    		newAer.testCode = value1.serviceStandardCode;
						    		value.orderTests.push(newAer);
								}
						    }
						});
					});
					console.log("nuronlist",$scope.ordersIdListNeuron);
					angular.forEach($scope.ordersIdListNeuron, function (value, key) {
						var data ={};
						data=value;
						var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
						var config ={
								  "Access-Control-Allow-Origin":'*',
								  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
								  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
								  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
								  'accept' :'application/json, text/javascript, *',
								  'content-type' :'application/json',
								  'scheme' :'https',
						};

						$http.post(url, data, config).then(function (response) {

						// This function handles success
//							alert("success");
							console.log(response);
							if(response.data.statusCode==200){
								var masse=response.data.message;
								var arr = masse.split(' ');
								var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
								var data ={
										"orgId" : $scope.organizationId,
										 "orgUnitId" :$scope.unitId,
										"orderId":arr[3],
    									"neuronPlusUrl":response.data.reportUrl,
    									
									}
									GenericService.serviceAction("POST", URI, data).then(function (response) {
										if (response.data.status == "success") 
										{
							            }
									});
							}

						}, function (response) {
//							alert("error");
						// this function handles error

						});

					});
					}
					if($scope.outsourceLabId == 2){	
						
						$scope.ordersIdListMedall= [];
						
						
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
						 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
							{
							 if($scope.patientDetails.genderId == 1){
								 $scope.patientDetails.genderId="0"
							 }else if($scope.patientDetails.genderId == 2){
								 $scope.patientDetails.genderId="1"
							 }else{
								 $scope.patientDetails.genderId="2"
							 }
							 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");
							
							 var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
						     var Bx = moment(Birth).format('YYYY-MM-DD');
							 var medallObj =
							 {
								    "name": $scope.patientDetails.firstName,
								    "gender": $scope.patientDetails.genderId,
								    "mobile":  $scope.mobileNumber.toString(),
								    "pincode":  $scope.patientDetails.zipCode != null ? $scope.patientDetails.zipCode :600020,
								    "address": $scope.patientDetails.address != null ? $scope.patientDetails.address:"",
								    "state": $scope.patientDetails.stateName!= null ? $scope.patientDetails.stateName:"",
								    "district": $scope.patientDetails.districtName != null ? $scope.patientDetails.districtName : "",
								    "passport": null,
								    "srfid": null,
								  //  "referalid": null,
								    "relationid": "1",
								  //  "salutationid": $scope.patientDetails.prefixId != null ?$scope.patientDetails.prefixId:"",
								    "nationality": "Indian",
								    "dob": Bx,
								    "centerid": centerid,
								    "appointmentid": null,
								    "externalid":  $scope.patientDetails.uhIdNumber,
								    "emailid": $scope.patientDetails.email != null ? $scope.patientDetails.email:"",
								    "orderid":value.orderId,
								    "sourceid":"10019471",
								    "hospitaltype":"OP",
								    "studyinfo": []
								}
							 
							 
							 var prefixId = "2513";
							 
								angular.forEach($scope.medallPrifixList, function (val, inx) {
									
									if($scope.patientDetails.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
										prefixId = val.medallSalutationIdPrefixId;
									}
									
								});
								
								medallObj.salutationid = prefixId.toString();
								
								if(value.doctorId != undefined && value.doctorId != null && value.doctorId != 0 ){
									
									var check = true;
									angular.forEach($scope.medallDoctorList, function (val, inx) {
										
										if(value.doctorId == val.doctorId && val.medallReferalId != null){
											medallObj.referalid = val.medallReferalId;
											check = false;
										}
										
									});
									
									if(check == true){
										medallObj.referalid = '5001195';
									}
								}else if( $scope.encounterDetails.referralId != undefined &&  $scope.encounterDetails.referralId != null &&  $scope.encounterDetails.referralId != 0 ){
									
									var check = true;
									angular.forEach($scope.referralMasterList, function (val, inx) {
										
										if( $scope.encounterDetails.referralId == val.referralId && val.medallReferralId != null){
											medallObj.referalid = val.medallReferralId;
											check = false;
										}
										
									});
									
									if(check == true){
										medallObj.referalid = '5001195';
									}
								}else{
									medallObj.referalid = '5001195';
								}
								
							 	if($scope.ordersIdListMedall.length==0){
								 $scope.ordersIdListMedall.push(medallObj);
								}
					     }
					});
					$scope.checkAutoRenderedServicesForMedall = [];
					angular.forEach($scope.ordersIdListMedall, function (value, key) {
					    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
						    if(value.orderid == value1.orderId){
						    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
						    		$scope.checkAutoRenderedServicesForMedall.push(value1);
								}
						    }
						});
					});
					
					angular.forEach($scope.ordersIdListMedall, function (value, key) {
					    angular.forEach($scope.checkAutoRenderedServicesForMedall, function (value1, key1) {
						    if(value.orderid == value1.orderId){
						    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
						    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
							    			var newAer = {};
								    		newAer.billingamount="0";
								    		newAer.billingid="0";
								    		newAer.billingtype="";
								    		newAer.corpbillingid=(value1.medallServiceId).toString();
								    		newAer.discount="0";
								    		newAer.paidamount="0";
								    		newAer.topaid="0";
								    		newAer.extraamount="0";
								    		value.studyinfo.push(newAer);
							    		}
									}
						    }
						});
					});
					
					if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
						if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
									
									angular.forEach($scope.ordersIdListMedall, function (value, key) {
									var data ={};
									data=value;
									var url = 'https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register';//'https://medcrpvmdev02.medallcorp.in/ExternalSource/api/register';
									config ={
											  "Access-Control-Allow-Origin":'*',
											  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
											  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
											  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
											  'accept' :'application/json, text/javascript, *',
											  'content-type' :'application/json',
											  'scheme' :'https',
											  headers: {
											      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace with your actual credentials
											    }
									};
			
									$http.post(url, data, config).then(function (response) {
			
									// This function handles success
			//							alert("success");
										console.log(response);
										if(response.data.status=="success"){
											growl.success("success");
											
											var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
											var data ={
													encounterId:$scope.encounterDetails.encounterId,
													patientId:$scope.encounterDetails.patientId,
													requestedPayload:JSON.stringify($scope.ordersIdListMedall),
													returnedResponse:JSON.stringify(response.data),
													status:'A',
													organizationId:$scope.organizationId,
													unitId:$scope.unitId,
													addedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													addedBy:$scope.userId,
													updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													updatedBy:$scope.userId
												}
												GenericService.serviceAction("POST", URI, data).then(function (response) {
													if (response.data.status == "success") 
													{
										            }
												});
										}else{
											growl.warning(response.data.status);
										}
									}, function (response) {
			
									});
			
								});
						}else{
							growl.warning("Service is not mapped with Medall.");
						}
					}else{
						growl.warning("Service is not mapped with Medall.");
					}
				}
					// console.log("$scope.billIdddd", $scope.billId);
					$("#opdBill").show();
				}else if (response.data.code==409)
				{
					$rootScope.stopLoader();
					$scope.btnDisabled = false;
					if(response.data.object==true) // if mismatch in bill found
					{
						$scope.getAutoRenderedServicesByPatient({
    						"visitTypeId":1,
    						"encounterId":$scope.encounterDetails.encounterId
						});
					}else{
						$scope.patientEncounterList = [];
						$scope.init();
					}
				}
				
			});
		}
	 
	 // For Collect Payment End
	 
	 
	 
	 // sample Collection On OpBilling then payment has completed
	 
	 /** Code for printing Barcode */
							$scope.printBarCode = function()
					{
					
							var printContents = $window.document.getElementById('barcodePrintingId').innerHTML;
							var popupWin = $window.open('', '_blank', 'width=300,height=300');
							popupWin.document.open();
							popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
							popupWin.document.close();
					}
					
					$scope.options =  {
							width: 2,
							height: 60,
							displayValue: false,
							font: 'monospace',
							textAlign: 'left',
							textPosition: 'bottom',
							fontSize: 15,
							backgroundColor: '#ffffff',
							lineColor: '#000000',
							margin: 0
					};
					
					$scope.barcode = {
							type: "code128",
							code: '',
							itemCode: '',
							batchCode: "123456",
							uhid : '',
							patientDetails:'',
							generationDateTime:''
					}

		$scope.initLabSampleMaster = function() {
			$scope.listSampleDtls = [];
			$scope.labSampleMaster = {
				"labSampleId" : "",
				"orgId" : $scope.organizationId,
				"orgUnitId" : $scope.unitId,
				"sampleGenDatetime" : "",
				"orderId" : "",
				"visitTypeId" : "",
				"admissionId" : "",
				"encounterId" : "",
				"patientId" : "",
				"doctorId" : "",
				"createdBy" : $scope.userId,
				"createdDate" : "",
				"updatedBy" : "",
				"updatedDate" : "",
				"uhid" : "",
				"patientDetails" : "",
				"doctorDetails" : "",
				"priorityName" : "",
				"colorCode" : "",
				"isOrderComplete" : "",
				"genderId" :"",
				"visitType":"",
				"listLabSampleDetailsMaster" : []
			}
		}
		$scope.initLabSampleMaster();
	 
	 
	 $scope.saveSampleCollectionList = [];
	 $scope.orderIdList = [];
		$scope.getWorkListDetails = function() {
			$rootScope.startLoader();
			try {
				
				var URI = BASE_URL + ROOT_URL+"/LIS/transaction/getPhlebotomyWorklistOnNewOPBill";
					GenericService.serviceAction("POST", URI, $scope.orderIdList).then(
								function(response) 
								{
									if(response.data.status=='success')
										{
										
										$rootScope.stopLoader();
										$scope.phlebotomyWorkListDetails=[];
										$scope.phlebotomyWorkListDetails = response.data.listObject;
										$scope.orderIdList = [];
										$scope.sampleList = [];
										$scope.orderIds = [];
										angular.forEach( $scope.phlebotomyWorkListDetails, function(value, key) {
													
											value.performTestId  = value.testId;
											value.tempOutSource = value.isOutsourced;
											if(value.assignUnit == null){
												value.assignUnit  = value.orgUnitId;
											}
											
											if(value.sampleCollection == "Y"){
                                            	$scope.sampleList.push(value);
                                            	
                                            	if($scope.orderIds.length==0){
                                            		var newobj ={};
                                            		newobj.orderId = value.orderId;
                                            		$scope.orderIds.push(newobj);
                                            	}else{
                                            		var found = $scope.orderIds.find(ar=>(ar.orderId == value.orderId));
                                            		if(found == undefined){
                                            			var newobj ={};
                                                		newobj.orderId = value.orderId;
                                                		$scope.orderIds.push(newobj);
                                            		}
                                            	}
                                            }
														
										 });
										
										var worklistDetails;
										angular.forEach( $scope.orderIds, function(value, key) {
											
											$scope.labSampleMaster.orderId= value.orderId;
											$scope.labSampleMaster.visitTypeId=1;
											$scope.labSampleMaster.encounterId=$scope.encounterDetails.encounterId;
											$scope.labSampleMaster.patientId=$scope.encounterDetails.patientId;
											$scope.labSampleMaster.doctorId=($scope.encounterDetails.doctorId == undefined || $scope.encounterDetails.doctorId == null) ? null : $scope.encounterDetails.doctorId;
											$scope.labSampleMaster.createdBy = $scope.userId;
											$scope.labSampleMaster.createdDate=new Date().getTime();;
											$scope.labSampleMaster.updatedBy=$scope.userId;
											$scope.labSampleMaster.updatedDate=new Date().getTime();;
											$scope.labSampleMaster.uhid=$scope.patientDetails.uhIdNumber;
											$scope.labSampleMaster.genderId=$scope.patientDetails.genderId;
											$scope.labSampleMaster.orgId =  $scope.organizationId;
											$scope.labSampleMaster.orgUnitId = $scope.unitId;
											
											$scope.listSampleDtls = [];
											
											angular.forEach($scope.sampleList, function(value, key) {
												worklistDetails = angular.copy(value);
												worklistDetails.sampleStatusId = SAMPLE_COLLECTED;
												worklistDetails.patientId = $scope.labSampleMaster.patientId;
												worklistDetails.doctorId = $scope.labSampleMaster.doctorId;
												worklistDetails.priorityId = $scope.labSampleMaster.priorityId;
												worklistDetails.sampleCollectionBy = $scope.userId;
												worklistDetails.sampleGenBy  = $scope.userId;
												worklistDetails.createdBy  = $scope.userId;
												worklistDetails.createdDate  = new Date().getTime();
												worklistDetails.updatedDate  = new Date().getTime();
												worklistDetails.updatedBy  = $scope.userId;
												worklistDetails.performTestId  = value.performTestId;
												worklistDetails.assignUnit  = value.assignUnit;
												worklistDetails.collectionTime = null;
												$scope.listSampleDtls.push(worklistDetails);
											});
											
											$scope.labSampleMaster.collectionTime = null;
											$scope.labSampleMaster.createdBy = $scope.userId;
											$scope.labSampleMaster.listLabSampleDetailsMaster = $scope.listSampleDtls;
											$scope.saveSampleCollectionList.push($scope.labSampleMaster);
										});
										
										
										if($scope.saveSampleCollectionList.length>0){
											
											var URI = BASE_URL + ROOT_URL+ "/LIS/transaction/sampleCollectionOfOPDBilling";

											try {
												GenericService.serviceAction("POST", URI,$scope.saveSampleCollectionList).then(
																function(response) {
																	if (response.data.status == 'success') {
																		$rootScope.stopLoader();
																		growl.success(response.data.message,
																						{
																							title : 'Success!'
																						});
																		
																		$scope.barcodeList = [];
																		var labSampleIds = [];
																		angular.forEach(response.data.listObject,function(value, key) 
																				{
																					var barcode  = angular.copy($scope.barcode);
																					barcode.code =value.sampleId;
																					barcode.itemCode =value.patientId;
																					barcode.sampleName =value.sampleName;
																					barcode.uhid =value.uhid;
																					barcode.patientDetails =value.patientDetails;
																					barcode.generationDateTime =value.generationDateTime;
																					$scope.barcodeList.push(barcode);																		
																					labSampleIds.push(value.labSampleDtlsId);
																				});
																		
																			// $('#sampleBarCode').modal('show');
																		
																		if(labSampleIds!=undefined && labSampleIds!=null && labSampleIds.length>0){
																			var data =  
																			{
																		            "appName": "HIS",
																		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																		            "reportDesignFilePath": "PathologybarcodePhebo.rpt",
																		            "reportFormat": "PDF",
																		            "exportFileName": "pathologyReport",
																		            "reportParams": [
																		              {
																			             "paramName": "lab_sample_Detils_id",
																			             "paramType": "String",
																			             "paramValue": labSampleIds.toString()
																		              }
																		            ]
																		      };
																			  reportService.generate(REPORT_API_URI1,data);
																		}
																		
																	} else if (response.data.status == 'error') 
																	{	
																		$rootScope.stopLoader();
																		if(response.data.code==409)
																			{
																				growl.warning(response.data.message,
																						{
																							title : 'Warning!'
																						});
																			
																			}else{
																				growl.error(response.data.message,
																						{
																							title : 'Error!'
																						});
																			}
																		
																		
																		
																	}
																});
											} catch (e) {
												// console.log("Exception",
												// e.message);
											}
										
											
										}

										
										}
									else if(response.data.status=='error')
										{
										  $rootScope.stopLoader();
										  $scope.orderIdList = [];
										}
									
									
								});
			} catch (e) {
				// console.log(e.message)
			}
		}

	 
	 
		 var isGroupExistsCap;
		  function checkGroupIdExistsOrNotCap(arr, val) {
				return arr.some(function(arr) {
						if(val.orderId === arr.orderId)
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
		  
		  
	
	$scope.saveBillngNew = function()
	{
		$scope.saveBtnDisable = true;
		angular.element('#saveConfirmPopup').modal('hide');
		var discountDetailForLine = [];
		$scope.btnDisabled = false;
		
		
		var data={
			
				patientId :$scope.encounterDetails.patientId,
				encounterId :$scope.encounterDetails.encounterId,
				createdBy :$scope.userId,
				createdDate :new Date().getTime(),
				updatedBy :$scope.userId,
				updatedDate :new Date().getTime(),
				orderDate :new Date().getTime(),
				organisationId :$scope.organizationId,
				unitId :$scope.unitId,
				isFinalBill : $scope.finalBill,
				ordDoctorId :$scope.encounterDetails.doctorId,
				visitTypeId : '1',
				billDateTime : new Date().getTime(),
				netAmount :  $scope.netPayableSummary !== undefined && !isNaN($scope.netPayableSummary) ? parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2) : "0",
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				isFinancialClearance : $scope.isFinancialClearence,
				financialRequestSendTo:$scope.finanacial.financialRequestSendTo,
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : $scope.removeDiscountList,
				tariffId : $scope.defaultSelfTariffId,
				paymentEntitlementId : $scope.encounterDetails.paymentEntitlementTypeId,
				visitTypeId : $scope.visitTypeId,
				patientTypeId : $scope.patientDetails.patientTypeId,
				billingBedCategoryId : $scope.opdBillingCatId,// default op
				isInterimBill :'N',
				isDrNote:"N",
				counterId:$rootScope.counterId,
				encounterSpecialityId:$scope.encounterDetails.specialityId,
				lineLevelDiscountOrderList : discountDetailForLine,
				homeCollection : $scope.homeCollection=='Y' ? true : false,
				saveFromSplitBill :	$scope.saveFromSplitBill,
				isAddendumBill : $scope.isAddendumBill,
				payeeId : $scope.encounterDetails.payeeId,
				advisedByName : $scope.searchAdvised.advisedByName,
				advisedById : $scope.searchAdvised.advisedById,
				
		};
		
		if(parseFloat($scope.totalSelfPayble) > 0){
			data.cashSettlement = $scope.cashSettlement;
		}else{
			data.cashSettlement ='N';
		}
		
		data.againstBillId = null;
		if($scope.preBillId > 0 && $scope.preBillId != undefined){
			data.againstBillId = $scope.preBillId;
		}
		
		if($scope.isBillWiseDiscountApplied == true)
			{
				data.isDiscountApplied = 'Y';
			}
		
		if($scope.requestStatusObject !=null )
			{
				data.reqDiscId = $scope.requestStatusObject.reqDiscId;
			}
		
		if($scope.billWiseDiscountObj.requestAmountBillWise > 0 ){
			data.reqDisAmt = $scope.billWiseDiscountObj.requestAmountBillWise - (parseFloat($scope.roundOffAmt));
			data.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
			data.remark = $scope.billWiseDiscountObj.remark;
			data.discType = $scope.billWiseDiscountObj.discountCategoryId;
		}
		
		var billPayeeList =[];
		if($scope.totalSelfPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.selfPayeeId,
						billAmount : $scope.totalSelfPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		if($scope.totalCreditPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.encounterDetails.payeeId,
						billAmount : $scope.totalCreditPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		
		data.billPayeeDetailsDtosList = billPayeeList;
		
		// console.log('$scope.autoRenderedServicesFinalList',$scope.autoRenderedServicesFinalList);

		// return false;
		
		var saveOrderDetailsObj = [];
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			var saveArr = {};
			if(value.orderDetailsId > 0){
				saveArr.orderDetailsId = value.orderDetailsId;
			}
			saveArr.discountOwnership = value.discountOwnership;
			saveArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
			saveArr.isConsultationService = value.isConsultationService;
			saveArr.ordRemarks = value.ordRemarks;
			saveArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
			saveArr.isDocReq = value.isDoctorRequired;
			saveArr.markupStatus = value.markupStatus;
			saveArr.patientClassPercentage = value.patientClassPercentage;
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			// saveArr.orderDate = new Date().getTime();
			saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.serviceAmt = value.basePrice !== undefined && !isNaN(value.basePrice) ? value.basePrice : "0";
			saveArr.dummyServiceAmt = value.dummyServiceAmt;
			saveArr.ordConcession = value.concession !== undefined && !isNaN(value.concession) ? value.concession : "0";
			saveArr.ordDiscount = value.discount !== undefined && !isNaN(value.discount) ? value.discount : "0";
			saveArr.netAmount =  value.netPay !== undefined && !isNaN(value.netPay) ? parseFloat(parseFloat(value.netPay)).toFixed(2) : "0";
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.taxId = value.taxId;
			saveArr.taxPer  = value.taxPercentage !== undefined && !isNaN(value.taxPercentage) ? value.taxPercentage : "0";
			saveArr.taxAmount = value.taxAmt !== undefined && !isNaN(value.taxAmt) ? value.taxAmt : "0"; value.taxAmt;
			saveArr.selfPayable = value.selfPayable;
			saveArr.creditPayable = value.creditPayable;
			saveArr.payeeId = value.payeeId;
			saveArr.ordTotalAmt =value.totalAmt;
			saveArr.tariffId = value.tariffId;
			saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
			saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
			saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			saveArr.status = value.status;
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = value.createdBy != null ? value.createdBy : $scope.userId;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay = value.oldNetPay !== undefined && !isNaN(value.oldNetPay) ? parseFloat(value.oldNetPay).toFixed(2) : "0";
			saveArr.ordCancelled = value.ordCancelled;
			saveArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ?  parseFloat(value.coPayPer).toFixed(2) : "0";
			saveArr.ordCancelReasonId  = value.ordCancelReasonId;
			saveArr.ordCancelDatetime  = value.ordCancelDatetime;
			saveArr.ordCancelRemark  = value.ordCancelRemark;
			saveArr.oldOrdDtlId = value.oldOrdDtlId;
			saveArr.discountCategoryId = value.discountCategoryId;
			// Ganesh
			saveArr.oldCreditPayable = value.creditPayableOldC;
			saveArr.oldSelfPayable = value.selfPayableOldC;
			saveArr.isInvestigationOrder = value.isInvestigationOrder;
			saveArr.isDiscount = value.isDiscount;
            saveArr.isDiscountApplicable = value.isDiscountApplicable;
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
			saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
			saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
			saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
			saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
			saveArr.isCapService = value.isCapService;
			saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
			saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
			saveArr.subDepartmentId = value.subSpecialityId;
			saveArr.isRefDoctorShare = value.isRefDoctorShare;
			saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
			
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.serviceRenderingDeptId = value.subSpecialityId; 
			saveArr.isPeriodicityService = value.isPeriodicityService;
			saveArr.encounterId = $scope.encounterDetails.encounterId;
			saveArr.contractId = value.contractId;
			saveArr.discountDetailsList = [];
			saveArr.serviceAmt = value.basePrice !== undefined && !isNaN(value.basePrice) ? value.basePrice : "0";
			saveArr.discountDetailsList = [];
			saveArr.bedCategoryId = value.bedCategoryId;
			saveArr.debitNoteService = false;
			
		    // package force add
			saveArr.forceAddRemoved = value.forceAddRemoved;
			saveArr.isConsumed = value.isConsumed;
			var ornew = {};
			ornew.orderId =  value.orderId;
			ornew.orgId = $scope.organizationId;
			ornew.orgUnitId = $scope.unitId;
			ornew.deptId = $scope.labConfig.labSpecialityId;
			if($scope.orderIdList.length == 0){
				$scope.orderIdList.push(ornew);
			}else{
				var findcount  = 0;
				angular.forEach($scope.orderIdList, function (val, index) {
					if(val.orderId == value.orderId){
						findcount = 1;
					}
				});
				if(findcount == 0){
					$scope.orderIdList.push(ornew);
				}
			}
			
				if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && value.isDiscountApplicable == 'Y')
					{
            				saveArr.reqDisAmt  = value.discount;
					}
				saveArr.saleId = value.saleId;
				saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
				saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
				
			saveOrderDetailsObj.push(saveArr);
		});
		data.orderDetailsMasterDtosList = saveOrderDetailsObj;
		data.cancelOrderDetailsList = $scope.removeServicesList;
		data.referralId = $scope.encounterDetails.referralId;
		
		$scope.settleobj = null;
		
		// console.log("finalSaveData",data);
		   if($scope.finalBill == 'Y' && parseFloat($scope.totalSelfPayble) == 0)
			$scope.cashSettlement = 'N';
		
			if($scope.finalBill == 'Y' && $scope.cashSettlement == 'N' && parseFloat($scope.totalSelfPayble) > 0 &&  $scope.isFinancialClearence == 'N')
			{
				$scope.settleobj = data;
				$scope.collectPayment();
			}else if($scope.finalBill == 'Y' && $scope.cashSettlement == 'N' && parseFloat($scope.totalSelfPayble) == 0){
				$rootScope.startLoader();
				$scope.popUpFlag = true;
				// console.log("finalSaveData",data);
				// console.log(JSON.stringify(data));
				// return false;
				var URI = BASE_URL + ROOT_URL + SAVEBILL;
				BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
					// //console.log(response)
					if(response.data.status == 'success')
					{
						// alert("Success SET Billed");
						
						$scope.billIdInvoice = response.data.object;
						$scope.getBillForCompanyInvoice();
						$rootScope.stopLoader();
						$scope.btnDisabled = false;
						var id=response.data.object;
						$scope.billId=id;
						 $scope.ServiceCount=0;
						if($scope.fileObj.file != null){
							$scope.uploadEncounterDoc(response.data.object);
						}
						if($scope.isPrebarcodeGenerantion != undefined && $scope.isPrebarcodeGenerantion != null && $scope.isPrebarcodeGenerantion == 'Y'){
							$scope.getWorkListDetails();
						}
						
					    console.log("$scope.autoRenderedServicesFinalList",$scope.autoRenderedServicesFinalList);
						if($scope.outsourceLabId == 1){		
						
							$scope.ordersIdListNeuron = [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
									var saveNuroPlus = {};
									saveNuroPlus.branchName = "NphMain";
									saveNuroPlus.orderId = value.orderId.toString();
									if($scope.encounterNumber != undefined){
										saveNuroPlus.IPOPNo = $scope.encounterNumber;
									 }else{
										saveNuroPlus.IPOPNo = $scope.patientEncounterList[0].encounterNumber;
									 }
									saveNuroPlus.UHID =  $scope.patientDetails.uhIdNumber;
									var orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
									var orderSchDate = new Date().getTime();
									saveNuroPlus.orderTs =value.orderDateString+"T"+value.orderDateTime;
										//"2022-02-15T13:24:00";
									saveNuroPlus.ipoptype = "OPD";
									saveNuroPlus.salutation =  $scope.patientDetails.genderId.toString();;
									saveNuroPlus.patientName =  $scope.patientDetails.patientName;
									saveNuroPlus.gender = $scope.patientDetails.genderCode;
								     var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
								     var Bx = moment(Birth).format('YYYY-MM-DD');
									saveNuroPlus.birthDate = Bx+"T00:00:00";
									saveNuroPlus.ward ="";
									saveNuroPlus.patMobileNo = $scope.mobileNumber.toString();
									saveNuroPlus.patEmailId = $scope.patientDetails.email;
									saveNuroPlus.patAddress = $scope.patientDetails.address;
									saveNuroPlus.patCity = $scope.patientDetails.cityName;
									saveNuroPlus.refDrCode = value.doctorName;
									saveNuroPlus.refDrName = value.doctorName;
									saveNuroPlus.feesPaid = false;
									saveNuroPlus.orderTests=[];
									if($scope.ordersIdListNeuron.length==0){
									$scope.ordersIdListNeuron.push(saveNuroPlus);
								}else{
									isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.ordersIdListNeuron,saveNuroPlus)
									  if (!isGroupExistsCap) {
										$scope.ordersIdListNeuron.push(saveNuroPlus);
									  }
								}
						     }
						});
						
						angular.forEach($scope.ordersIdListNeuron, function (value, key) {
						    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
							    if(value.orderId == value1.orderId){
							    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
									{
							    		var newAer = {};
							    		newAer.testRecordId = value1.serviceMasterId.toString();
							    		newAer.testName = value1.serviceStandardName;
							    		newAer.testCode = value1.serviceStandardCode;
							    		value.orderTests.push(newAer);
									}
							    }
							});
						});
						console.log("nuronlist",$scope.ordersIdListNeuron);
						angular.forEach($scope.ordersIdListNeuron, function (value, key) {
							var data ={};
							data=value;
							var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
							config ={
									  "Access-Control-Allow-Origin":'*',
									  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
									  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
									  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
									  'accept' :'application/json, text/javascript, *',
									  'content-type' :'application/json',
									  'scheme' :'https',
							};

							$http.post(url, data, config).then(function (response) {

							// This function handles success
//								alert("success");
								console.log(response);
								if(response.data.statusCode==200){
									var masse=response.data.message;
									var arr = masse.split(' ');
									var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
									var data ={
											"orgId" : $scope.organizationId,
											 "orgUnitId" :$scope.unitId,
											"orderId":arr[3],
	    									"neuronPlusUrl":response.data.reportUrl,
	    									
										}
										GenericService.serviceAction("POST", URI, data).then(function (response) {
											if (response.data.status == "success") 
											{
								            }
										});
								}
							}, function (response) {
//								alert("error");
							// this function handles error

							});

						});
						}
						if($scope.outsourceLabId == 2){	
							
							$scope.ordersIdListMedall= [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
								 if($scope.patientDetails.genderId == 1){
									 $scope.patientDetails.genderId="0"
								 }else if($scope.patientDetails.genderId == 2){
									 $scope.patientDetails.genderId="1"
								 }else{
									 $scope.patientDetails.genderId="2"
								 }
								 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");
									
								/* if($scope.unitId == 129){
									 centerid="1478";
								 }else if($scope.unitId == 135){
									 centerid="1479";
								 }else if($scope.unitId == 139){
									 centerid="1480";
								 }else{
									 centerid="2";
								 }*/
								 var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
							     var Bx = moment(Birth).format('YYYY-MM-DD');
								 var medallObj =
								 {
									    "name": $scope.patientDetails.firstName,
									    "gender": $scope.patientDetails.genderId,
									    "mobile":  $scope.mobileNumber.toString(),
									    "pincode":  $scope.patientDetails.zipCode != null ? $scope.patientDetails.zipCode :600020,
									    "address": $scope.patientDetails.address != null ? $scope.patientDetails.address:"",
									    "state": $scope.patientDetails.stateName!= null ? $scope.patientDetails.stateName:"",
									    "district": $scope.patientDetails.districtName != null ? $scope.patientDetails.districtName : "",
									    "passport": null,
									    "srfid": null,
									   // "referalid": null,
									    "relationid": "1",
									   // "salutationid": $scope.patientDetails.prefixId != null ?$scope.patientDetails.prefixId:"",
									    "nationality": "Indian",
									    "dob": Bx,
									    "centerid": centerid,
									    "appointmentid": null,
									    "externalid":  $scope.patientDetails.uhIdNumber,
									    "emailid": $scope.patientDetails.email != null ? $scope.patientDetails.email:"",
									    "orderid":value.orderId,
									    "sourceid":"10019471",
									    "hospitaltype":"OP",
									    "studyinfo": []
									}
								 
								 var prefixId = "2513";
								 
									angular.forEach($scope.medallPrifixList, function (val, inx) {
										
										if($scope.patientDetails.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
											prefixId = val.medallSalutationIdPrefixId;
										}
										
									});
									
									medallObj.salutationid = prefixId.toString();
									
									if(value.doctorId != undefined && value.doctorId != null && value.doctorId != 0 ){
										
										var check = true;
										angular.forEach($scope.medallDoctorList, function (val, inx) {
											
											if(value.doctorId == val.doctorId && val.medallReferalId != null){
												medallObj.referalid = val.medallReferalId;
												check = false;
											}
											
										});
										
										if(check == true){
											medallObj.referalid = '5001195';
										}
									}else if( $scope.encounterDetails.referralId != undefined &&  $scope.encounterDetails.referralId != null &&  $scope.encounterDetails.referralId != 0 ){
										
										var check = true;
										angular.forEach($scope.referralMasterList, function (val, inx) {
											
											if( $scope.encounterDetails.referralId == val.referralId && val.medallReferralId != null){
												medallObj.referalid = val.medallReferralId;
												check = false;
											}
											
										});
										
										if(check == true){
											medallObj.referalid = '5001195';
										}
									}else{
										medallObj.referalid = '5001195';
									}
									
								 
								 
								 	if($scope.ordersIdListMedall.length==0){
									 $scope.ordersIdListMedall.push(medallObj);
									}
						     }
						});
						$scope.checkAutoRenderedServicesForMedall = [];
						angular.forEach($scope.ordersIdListMedall, function (value, key) {
						    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
							    if(value.orderid == value1.orderId){
							    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
									{
							    		$scope.checkAutoRenderedServicesForMedall.push(value1);
									}
							    }
							});
						});
						
						angular.forEach($scope.ordersIdListMedall, function (value, key) {
						    angular.forEach($scope.checkAutoRenderedServicesForMedall, function (value1, key1) {
							    if(value.orderid == value1.orderId){
							    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
									{
							    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
								    			var newAer = {};
									    		newAer.billingamount="0";
									    		newAer.billingid="0";
									    		newAer.billingtype="";
									    		newAer.corpbillingid=(value1.medallServiceId).toString();
									    		newAer.discount="0";
									    		newAer.paidamount="0";
									    		newAer.topaid="0";
									    		newAer.extraamount="0";
									    		value.studyinfo.push(newAer);
								    		}
										}
							    }
							});
						});
						if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
							if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
								
										angular.forEach($scope.ordersIdListMedall, function (value, key) {
											var data ={};
											data=value;
											var url = 'https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register';
											config ={
													  "Access-Control-Allow-Origin":'*',
													  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
													  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
													  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
													  'accept' :'application/json, text/javascript, *',
													  'content-type' :'application/json',
													  'scheme' :'https',
													  headers: {
													      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace with your actual credentials
													    }
											};
				
											$http.post(url, data, config).then(function (response) {
				
											// This function handles success
				//								alert("success");
												console.log(response);
												if(response.data.status=="success"){
													growl.success("success");
													
													var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
													var data ={
															encounterId:$scope.encounterDetails.encounterId,
															patientId:$scope.encounterDetails.patientId,
															requestedPayload:JSON.stringify($scope.ordersIdListMedall),
															returnedResponse:JSON.stringify(response.data),
															status:'A',
															organizationId:$scope.organizationId,
															unitId:$scope.unitId,
															addedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
															addedBy:$scope.userId,
															updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
															updatedBy:$scope.userId
														}
														GenericService.serviceAction("POST", URI, data).then(function (response) {
															if (response.data.status == "success") 
															{
												            }
														});
												}else{
													growl.warning(response.data.status);
												}
											}, function (response) {
				//								alert("error");
											// this function handles error
				
											});
				
										});
							}else{
								growl.warning("Service is not mapped with Medall.");
							}
						}else{
							growl.warning("Service is not mapped with Medall.");
						}
					}
						
						// var recieptObj=response.data.resObject;
						$scope.billId=id;
						 $scope.ServiceCount=0;
						// $scope.recieptId=recieptObj.recieptId;
						// $scope.depositeConsumptionId =
						// recieptObj.depositConsumptionId
						$scope.preBillId = "";
						// console.log("$scope.billIdddd", $scope.billId);
						if($scope.finalBill=='Y')
						{
							$("#opdBill").show();
						}
					}else if (response.data.code==409)
					{
						$rootScope.stopLoader();
						$scope.btnDisabled = false;
						if(response.data.object==true) // if mismatch in bill
														// found
						{
							$scope.getAutoRenderedServicesByPatient({
	    						"visitTypeId":1,
	    						"encounterId":$scope.encounterDetails.encounterId
							});
						}else{
							$scope.patientEncounterList = [];
							$scope.init();
						}
					}
					
				});
			}
			else if($scope.finalBill == 'Y' && $scope.cashSettlement == 'Y'){
				$rootScope.startLoader();
				$scope.popUpFlag = true;
				// console.log("finalSaveData",data);
				// console.log(JSON.stringify(data));
				// return false;
				var URI = BASE_URL + ROOT_URL + SAVEBILL;
				
				// nitin
				
				BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
					// //console.log(response)
					if(response.data.status == 'success')
					{
						// alert("Success SET Billed");
						$scope.billIdInvoice = response.data.object;
						$scope.getBillForCompanyInvoice();
						$rootScope.stopLoader();
						$scope.btnDisabled = false;
						var id=response.data.object;
						$scope.billId=id;
						 $scope.ServiceCount=0;
						if($scope.fileObj.file != null){
							$scope.uploadEncounterDoc(response.data.object);
						}
						if($scope.outsourceLabId == 1){		
							
							$scope.ordersIdListNeuron = [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
									var saveNuroPlus = {};
									saveNuroPlus.branchName = "NphMain";
									saveNuroPlus.orderId = value.orderId.toString();
									if($scope.encounterNumber != undefined){
										saveNuroPlus.IPOPNo = $scope.encounterNumber;
									 }else{
										saveNuroPlus.IPOPNo = $scope.patientEncounterList[0].encounterNumber;
									 }
									saveNuroPlus.UHID =  $scope.patientDetails.uhIdNumber;
									var orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
									var orderSchDate = new Date().getTime();
									saveNuroPlus.orderTs =value.orderDateString+"T"+value.orderDateTime;
										//"2022-02-15T13:24:00";
									saveNuroPlus.ipoptype = "OPD";
									saveNuroPlus.salutation =  $scope.patientDetails.genderId.toString();;
									saveNuroPlus.patientName =  $scope.patientDetails.patientName;
									saveNuroPlus.gender = $scope.patientDetails.genderCode;
								     var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
								     var Bx = moment(Birth).format('YYYY-MM-DD');
									saveNuroPlus.birthDate = Bx+"T00:00:00";
									saveNuroPlus.ward ="";
									saveNuroPlus.patMobileNo = $scope.mobileNumber.toString();
									saveNuroPlus.patEmailId = $scope.patientDetails.email;
									saveNuroPlus.patAddress = $scope.patientDetails.address;
									saveNuroPlus.patCity = $scope.patientDetails.cityName;
									saveNuroPlus.refDrCode = value.doctorName;
									saveNuroPlus.refDrName = value.doctorName;
									saveNuroPlus.feesPaid = false;
									saveNuroPlus.orderTests=[];
									if($scope.ordersIdListNeuron.length==0){
									$scope.ordersIdListNeuron.push(saveNuroPlus);
								}else{
									isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.ordersIdListNeuron,saveNuroPlus)
									  if (!isGroupExistsCap) {
										$scope.ordersIdListNeuron.push(saveNuroPlus);
									  }
								}
						     }
						});
						
						angular.forEach($scope.ordersIdListNeuron, function (value, key) {
						    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
							    if(value.orderId == value1.orderId){
							    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
									{
							    		var newAer = {};
							    		newAer.testRecordId = value1.serviceMasterId.toString();
							    		newAer.testName = value1.serviceStandardName;
							    		newAer.testCode = value1.serviceStandardCode;
							    		value.orderTests.push(newAer);
									}
							    }
							});
						});
						console.log("nuronlist",$scope.ordersIdListNeuron);
						angular.forEach($scope.ordersIdListNeuron, function (value, key) {
							var data ={};
							data=value;
							var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
							var config ={
									  "Access-Control-Allow-Origin":'*',
									  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
									  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
									  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
									  'accept' :'application/json, text/javascript, *',
									  'content-type' :'application/json',
									  'scheme' :'https',
							};

							$http.post(url, data, config).then(function (response) {

							// This function handles success
//								alert("success");
								console.log(response);
								if(response.data.statusCode==200){
									var masse=response.data.message;
									var arr = masse.split(' ');
									var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
									var data ={
											"orgId" : $scope.organizationId,
											 "orgUnitId" :$scope.unitId,
											"orderId":arr[3],
	    									"neuronPlusUrl":response.data.reportUrl,
	    									
										}
										GenericService.serviceAction("POST", URI, data).then(function (response) {
											if (response.data.status == "success") 
											{
								            }
										});
								}

							}, function (response) {
//								alert("error");
							// this function handles error

							});

						});
						}
						if($scope.outsourceLabId == 2){	
							
							$scope.ordersIdListMedall= [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
								 if($scope.patientDetails.genderId == 1){
									 $scope.patientDetails.genderId="0"
								 }else if($scope.patientDetails.genderId == 2){
									 $scope.patientDetails.genderId="1"
								 }else{
									 $scope.patientDetails.genderId="2"
								 }
								 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");
									
								/* var centerid="";
								 if($scope.unitId == 129){
									 centerid="1478";
								 }else if($scope.unitId == 135){
									 centerid="1479";
								 }else if($scope.unitId == 139){
									 centerid="1480";
								 }else{
									 centerid="2";
								 }*/
								 var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
							     var Bx = moment(Birth).format('YYYY-MM-DD');
								 var medallObj =
								 {
									    "name": $scope.patientDetails.firstName,
									    "gender": $scope.patientDetails.genderId,
									    "mobile":  $scope.mobileNumber.toString(),
									    "pincode":  $scope.patientDetails.zipCode != null ? $scope.patientDetails.zipCode :600020,
									    "address": $scope.patientDetails.address != null ? $scope.patientDetails.address:"",
									    "state": $scope.patientDetails.stateName!= null ? $scope.patientDetails.stateName:"",
									    "district": $scope.patientDetails.districtName != null ? $scope.patientDetails.districtName : "",
									    "passport": null,
									    "srfid": null,
									   // "referalid": null,
									    "relationid": "1",
									    //"salutationid": $scope.patientDetails.prefixId != null ?$scope.patientDetails.prefixId:"",
									    "nationality": "Indian",
									    "dob": Bx,
									    "centerid": centerid,
									    "appointmentid": null,
									    "externalid":  $scope.patientDetails.uhIdNumber,
									    "emailid": $scope.patientDetails.email != null ? $scope.patientDetails.email:"",
									    "orderid":value.orderId,
									    "sourceid":"10019471",
									    "hospitaltype":"OP",
									    "studyinfo": []
									}
								 
								 var prefixId = "2513";
								 
									angular.forEach($scope.medallPrifixList, function (val, inx) {
										
										if($scope.patientDetails.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
											prefixId = val.medallSalutationIdPrefixId;
										}
										
									});
									
									medallObj.salutationid = prefixId.toString();
									
									if(value.doctorId != undefined && value.doctorId != null && value.doctorId != 0 ){
										
										var check = true;
										angular.forEach($scope.medallDoctorList, function (val, inx) {
											
											if(value.doctorId == val.doctorId && val.medallReferalId != null){
												medallObj.referalid = val.medallReferalId;
												check = false;
											}
											
										});
										
										if(check == true){
											medallObj.referalid = '5001195';
										}
									}else if( $scope.encounterDetails.referralId != undefined &&  $scope.encounterDetails.referralId != null &&  $scope.encounterDetails.referralId != 0 ){
										
										var check = true;
										angular.forEach($scope.referralMasterList, function (val, inx) {
											
											if( $scope.encounterDetails.referralId == val.referralId && val.medallReferralId != null){
												medallObj.referalid = val.medallReferralId;
												check = false;
											}
											
										});
										
										if(check == true){
											medallObj.referalid = '5001195';
										}
									}else{
										medallObj.referalid = '5001195';
									}
									
									
								 	if($scope.ordersIdListMedall.length==0){
									 $scope.ordersIdListMedall.push(medallObj);
									}
						     }
						});
						var count=0;
						$scope.checkAutoRenderedServicesForMedall = [];
						angular.forEach($scope.ordersIdListMedall, function (value, key) {
						    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
							    if(value.orderid == value1.orderId){
							    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
									{
							    		$scope.checkAutoRenderedServicesForMedall.push(value1);
									}
							    }
							});
						});
						
						angular.forEach($scope.ordersIdListMedall, function (value, key) {
						    angular.forEach($scope.checkAutoRenderedServicesForMedall, function (value1, key1) {
							    if(value.orderid == value1.orderId){
							    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
									{
							    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
							    			count++;
								    			var newAer = {};
									    		newAer.billingamount="0";
									    		newAer.billingid="0";
									    		newAer.billingtype="";
									    		newAer.corpbillingid=(value1.medallServiceId).toString();
									    		newAer.discount="0";
									    		newAer.paidamount="0";
									    		newAer.topaid="0";
									    		newAer.extraamount="0";
									    		value.studyinfo.push(newAer);
								    		}
										}
							    }
							});
						});
						if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
							if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
								
									angular.forEach($scope.ordersIdListMedall, function (value, key) {
										var data ={};
										data=value;
										var url = 'https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register';
										config ={
												  "Access-Control-Allow-Origin":'*',
												  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
												  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
												  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
												  'accept' :'application/json, text/javascript, *',
												  'content-type' :'application/json',
												  'scheme' :'https',
												  headers: {
												      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace with your actual credentials
												    }
										};
			
										$http.post(url, data, config).then(function (response) {
			
										// This function handles success
			//								alert("success");
											console.log(response);
											if(response.data.status=="success"){
												growl.success("success");
												
												var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
												var data ={
														encounterId:$scope.encounterDetails.encounterId,
														patientId:$scope.encounterDetails.patientId,
														requestedPayload:JSON.stringify($scope.ordersIdListMedall),
														returnedResponse:JSON.stringify(response.data),
														status:'A',
														organizationId:$scope.organizationId,
														unitId:$scope.unitId,
														addedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
														addedBy:$scope.userId,
														updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
														updatedBy:$scope.userId
													}
													GenericService.serviceAction("POST", URI, data).then(function (response) {
														if (response.data.status == "success") 
														{
											            }
													});
											}else{
												growl.warning(response.data.status);
											}
										}, function (response) {
			
										});
			
									});
							}else{
								growl.warning("Service is not mapped with Medall.");
							}
						}else{
							growl.warning("Service is not mapped with Medall.");
						}
					}
						var recieptObj=response.data.resObject;
						$scope.billId=id;
						 $scope.ServiceCount=0;
						$scope.recieptId=recieptObj.recieptId;
						$scope.depositeConsumptionId = recieptObj.depositConsumptionId
						$scope.preBillId = "";
						if($scope.isPrebarcodeGenerantion != undefined && $scope.isPrebarcodeGenerantion != null && $scope.isPrebarcodeGenerantion == 'Y'){
							$scope.getWorkListDetails();
						}
						// console.log("$scope.billIdddd", $scope.billId);
						if($scope.finalBill=='Y')
						{
							$("#opdBill").show();
						}								else{}
					}else if (response.data.code==409)
					{
						$rootScope.stopLoader();
						$scope.btnDisabled = false;
						if(response.data.object==true) // if mismatch in bill
														// found
						{
							$scope.getAutoRenderedServicesByPatient({
	    						"visitTypeId":1,
	    						"encounterId":$scope.encounterDetails.encounterId
							});
						}else{
							$scope.patientEncounterList = [];
							$scope.init();
						}
					}
					
				});
				
			}
			else
				{
				/*
				 * if ($scope.popUpFlag) { $scope.showModal = !$scope.showModal;
				 *  } else{
				 */
					// alert("Direct");
					$rootScope.final=data;
					$rootScope.startLoader();
					// console.log("finalSaveData",data);
					// return false;
					var URI = BASE_URL + ROOT_URL + SAVEBILL;
					BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
						// console.log(response)
						if(response.data.status == 'success'){
							// alert("Success SET");
							$scope.billIdInvoice = response.data.object;
							$scope.getBillForCompanyInvoice();
							$rootScope.stopLoader();
							var id=response.data.object;
							$scope.billId=id;
							 $scope.ServiceCount=0;
							if($scope.fileObj.file != null){
								$scope.uploadEncounterDoc(response.data.object);
							}
							$scope.btnDisabled = false;
							$scope.patientEncounterList = [];
							$scope.init();
							if($scope.finalBill=='Y' && $scope.billId!= null)
							{
								$("#opdBill").show();
							}	
							
						}else if (response.data.code==409)
						{
							$rootScope.stopLoader();
							$scope.btnDisabled = false;
							if(response.data.object==true) // if mismatch in
															// bill found
							{
								$scope.getAutoRenderedServicesByPatient({
		    						"visitTypeId":1,
		    						"encounterId":$scope.encounterDetails.encounterId
								});
							}else{
								$scope.patientEncounterList = [];
								$scope.init();
							}
						}
						
					});
					
				/* } */
					
				}
	}
	
	
	$scope.saveBillForCollectPopup = function(){
		

		$scope.saveBtnDisable = true;
		$scope.isNewOpdBill = 'N';
		angular.element('#collectPayment').modal('hide');
		var discountDetailForLine = [];
		$scope.btnDisabled = false;
		
		
		var data={
			
				patientId :$scope.encounterDetails.patientId,
				encounterId :$scope.encounterDetails.encounterId,
				createdBy :$scope.userId,
				createdDate :new Date().getTime(),
				updatedBy :$scope.userId,
				updatedDate :new Date().getTime(),
				orderDate :new Date().getTime(),
				organisationId :$scope.organizationId,
				unitId :$scope.unitId,
				isFinalBill : $scope.finalBill,
				ordDoctorId :$scope.encounterDetails.doctorId,
				visitTypeId : '1',
				billDateTime : new Date().getTime(),
				netAmount : $scope.netPayableSummary !== undefined && !isNaN($scope.netPayableSummary) ? parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2) : "0",
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				isFinancialClearance : $scope.isFinancialClearence,
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : $scope.removeDiscountList,
				tariffId : $scope.defaultSelfTariffId,
				paymentEntitlementId : $scope.encounterDetails.paymentEntitlementTypeId,
				visitTypeId : $scope.visitTypeId,
				patientTypeId : $scope.patientDetails.patientTypeId,
				billingBedCategoryId : $scope.opdBillingCatId,// default op
				isInterimBill :'N',
				isDrNote:"N",
				counterId:$rootScope.counterId,
				encounterSpecialityId:$scope.encounterDetails.specialityId,
				lineLevelDiscountOrderList : discountDetailForLine,
				homeCollection : $scope.homeCollection=='Y' ? true : false,
				saveFromSplitBill :	$scope.saveFromSplitBill,
				isAddendumBill : $scope.isAddendumBill,
				payeeId : $scope.encounterDetails.payeeId,
				advisedByName : $scope.searchAdvised.advisedByName,
				advisedById : $scope.searchAdvised.advisedById
		};
		
		if(parseFloat($scope.totalSelfPayble) > 0){
			data.cashSettlement = $scope.cashSettlement;
		}else{
			data.cashSettlement ='N';
		}
		
		data.againstBillId = null;
		if($scope.preBillId > 0 && $scope.preBillId != undefined){
			data.againstBillId = $scope.preBillId;
		}
		
		if($scope.isBillWiseDiscountApplied == true)
			{
				data.isDiscountApplied = 'Y';
			}
		
		if($scope.requestStatusObject !=null )
			{
				data.reqDiscId = $scope.requestStatusObject.reqDiscId;
			}
		
		if($scope.billWiseDiscountObj.requestAmountBillWise > 0 ){
			data.reqDisAmt = $scope.billWiseDiscountObj.requestAmountBillWise;
			data.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
			data.discType = $scope.billWiseDiscountObj.discountCategoryId;
		}
		
		var billPayeeList =[];
		if($scope.totalSelfPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.selfPayeeId,
						billAmount : $scope.totalSelfPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		if($scope.totalCreditPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.encounterDetails.payeeId,
						billAmount : $scope.totalCreditPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		
		data.billPayeeDetailsDtosList = billPayeeList;
		
		// console.log('$scope.autoRenderedServicesFinalList',$scope.autoRenderedServicesFinalList);
		// return false;
		
		var saveOrderDetailsObj = [];
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			var saveArr = {};
			if(value.orderDetailsId > 0){
				saveArr.orderDetailsId = value.orderDetailsId;
			}
			saveArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
			saveArr.isConsultationService = value.isConsultationService;
			saveArr.ordRemarks = value.ordRemarks;
			saveArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
			saveArr.isDocReq = value.isDoctorRequired;
			saveArr.markupStatus = value.markupStatus;
			saveArr.patientClassPercentage = value.patientClassPercentage;
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			// saveArr.orderDate = new Date().getTime();
			saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.serviceAmt = value.basePrice !== undefined && !isNaN(value.basePrice) ? value.basePrice : "0"
			saveArr.dummyServiceAmt = value.dummyServiceAmt;
			saveArr.ordConcession =  value.concession !== undefined && !isNaN(value.concession) ? value.concession : "0";
			saveArr.ordDiscount = value.discount !== undefined && !isNaN(value.discount) ? value.discount : "0";
			saveArr.netAmount = value.netPay !== undefined && !isNaN(value.netPay) ? value.netPay : "0";
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.taxId = value.taxId;
			saveArr.taxPer  = value.taxPercentage !== undefined && !isNaN(value.taxPercentage) ? value.taxPercentage : "0";
			saveArr.taxAmount = value.taxAmt !== undefined && !isNaN(value.taxAmt) ? value.taxAmt : "0"; value.taxAmt;
			saveArr.selfPayable = value.selfPayable;
			saveArr.creditPayable = value.creditPayable;
			saveArr.payeeId = value.payeeId;
			saveArr.ordTotalAmt =value.totalAmt;
			saveArr.tariffId = value.tariffId;
			saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
			saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
			saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			saveArr.status = value.status;
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = value.createdBy != null ? value.createdBy : $scope.userId;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay =  value.oldNetPay !== undefined && !isNaN(value.oldNetPay) ? parseFloat(value.oldNetPay).toFixed(2) : "0";
			saveArr.ordCancelled = value.ordCancelled;
			saveArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ? parseFloat(value.coPayPer).toFixed(2) : "0";
			saveArr.ordCancelReasonId  = value.ordCancelReasonId;
			saveArr.ordCancelDatetime  = value.ordCancelDatetime;
			saveArr.ordCancelRemark  = value.ordCancelRemark;
			saveArr.oldOrdDtlId = value.oldOrdDtlId;
			saveArr.discountCategoryId = value.discountCategoryId;
			// Ganesh
			saveArr.oldCreditPayable = value.creditPayableOldC;
			saveArr.oldSelfPayable = value.selfPayableOldC;
			saveArr.isInvestigationOrder = value.isInvestigationOrder;
			saveArr.isDiscount = value.isDiscount;
            saveArr.isDiscountApplicable = value.isDiscountApplicable;
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
			saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
			saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
			saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
			saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
			saveArr.isCapService = value.isCapService;
			saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
			saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
			saveArr.subDepartmentId = value.subSpecialityId;
			saveArr.isRefDoctorShare = value.isRefDoctorShare;
			saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
			
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.serviceRenderingDeptId = value.subSpecialityId; 
			saveArr.isPeriodicityService = value.isPeriodicityService;
			saveArr.encounterId = $scope.encounterDetails.encounterId;
			saveArr.contractId = value.contractId;
			saveArr.discountDetailsList = [];
			saveArr.serviceAmt = value.basePrice !== undefined && !isNaN(value.basePrice) ? value.basePrice : "0"
			saveArr.discountDetailsList = [];
			saveArr.bedCategoryId = value.bedCategoryId;
			saveArr.debitNoteService = false;
			
			var ornew = {};
			ornew.orderId =  value.orderId;
			ornew.orgId = $scope.organizationId;
			ornew.orgUnitId = $scope.unitId;
			ornew.deptId = $scope.labConfig.labSpecialityId;
			if($scope.orderIdList.length == 0){
				$scope.orderIdList.push(ornew);
			}else{
				var findcount  = 0;
				angular.forEach($scope.orderIdList, function (val, index) {
					if(val.orderId == value.orderId){
						findcount = 1;
					}
				});
				if(findcount == 0){
					$scope.orderIdList.push(ornew);
				}
			}
			
			
		    // package force add
			saveArr.forceAddRemoved = value.forceAddRemoved;
			saveArr.isConsumed = value.isConsumed;
			
				if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && value.isDiscountApplicable == 'Y')
					{
            				saveArr.reqDisAmt  = value.discount;
					}
				saveArr.saleId = value.saleId;
				saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
				saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
				
			saveOrderDetailsObj.push(saveArr);
		});
		data.orderDetailsMasterDtosList = saveOrderDetailsObj;
		data.cancelOrderDetailsList = $scope.removeServicesList;
		data.referralId = $scope.encounterDetails.referralId;
		
		$scope.settleobj = null;
		
		// console.log("finalSaveData",data);
		
				$rootScope.startLoader();
				$scope.popUpFlag = true;
				// console.log("finalSaveData",data);
				// console.log(JSON.stringify(data));
				// return false;
				var URI = BASE_URL + ROOT_URL + SAVEBILL;
				// nitin
				
				BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
					// //console.log(response)
					if(response.data.status == 'success')
					{
						// alert("Success SET Billed");
						$scope.billIdInvoice = response.data.object;
						$scope.getBillForCompanyInvoice();
						$rootScope.stopLoader();
						$scope.isNewOpdBill = 'Y';
						$scope.btnDisabled = false;
						var id=response.data.object;
						$scope.billId=id;
						 $scope.ServiceCount=0;
						if($scope.fileObj.file != null){
							$scope.uploadEncounterDoc(response.data.object);
						}
						
						if($scope.isPrebarcodeGenerantion != undefined && $scope.isPrebarcodeGenerantion != null && $scope.isPrebarcodeGenerantion == 'Y' && response.data.resObject!= null){
							$scope.getWorkListDetails();
						}
				if($scope.outsourceLabId == 1){		
							
							$scope.ordersIdListNeuron = [];
						angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
							 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
								{
									var saveNuroPlus = {};
									saveNuroPlus.branchName = "NphMain";
									saveNuroPlus.orderId = value.orderId.toString();
									if($scope.encounterNumber != undefined){
										saveNuroPlus.IPOPNo = $scope.encounterNumber;
									 }else{
										saveNuroPlus.IPOPNo = $scope.patientEncounterList[0].encounterNumber;
									 }
									saveNuroPlus.UHID =  $scope.patientDetails.uhIdNumber;
									var orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
									var orderSchDate = new Date().getTime();
									saveNuroPlus.orderTs =value.orderDateString+"T"+value.orderDateTime;
										//"2022-02-15T13:24:00";
									saveNuroPlus.ipoptype = "OPD";
									saveNuroPlus.salutation =  $scope.patientDetails.genderId.toString();;
									saveNuroPlus.patientName =  $scope.patientDetails.patientName;
									saveNuroPlus.gender = $scope.patientDetails.genderCode;
								     var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
								     var Bx = moment(Birth).format('YYYY-MM-DD');
									saveNuroPlus.birthDate = Bx+"T00:00:00";
									saveNuroPlus.ward ="";
									saveNuroPlus.patMobileNo = $scope.mobileNumber.toString();
									saveNuroPlus.patEmailId = $scope.patientDetails.email;
									saveNuroPlus.patAddress = $scope.patientDetails.address;
									saveNuroPlus.patCity = $scope.patientDetails.cityName;
									saveNuroPlus.refDrCode = value.doctorName;
									saveNuroPlus.refDrName = value.doctorName;
									saveNuroPlus.feesPaid = false;
									saveNuroPlus.orderTests=[];
									if($scope.ordersIdListNeuron.length==0){
									$scope.ordersIdListNeuron.push(saveNuroPlus);
								}else{
									isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.ordersIdListNeuron,saveNuroPlus)
									  if (!isGroupExistsCap) {
										$scope.ordersIdListNeuron.push(saveNuroPlus);
									  }
								}
						     }
						});
						
						angular.forEach($scope.ordersIdListNeuron, function (value, key) {
						    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
							    if(value.orderId == value1.orderId){
							    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
									{
							    		var newAer = {};
							    		newAer.testRecordId = value1.serviceMasterId.toString();
							    		newAer.testName = value1.serviceStandardName;
							    		newAer.testCode = value1.serviceStandardCode;
							    		value.orderTests.push(newAer);
									}
							    }
							});
						});
						console.log("nuronlist",$scope.ordersIdListNeuron);
						angular.forEach($scope.ordersIdListNeuron, function (value, key) {
							var data ={};
							data=value;
							var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
							var config ={
									  "Access-Control-Allow-Origin":'*',
									  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
									  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
									  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
									  'accept' :'application/json, text/javascript, *',
									  'content-type' :'application/json',
									  'scheme' :'https',
							};

							$http.post(url, data, config).then(function (response) {

							// This function handles success
//								alert("success");
								console.log(response);
								if(response.data.statusCode==200){
									var masse=response.data.message;
									var arr = masse.split(' ');
									var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
									var data ={
											"orgId" : $scope.organizationId,
											 "orgUnitId" :$scope.unitId,
											"orderId":arr[3],
	    									"neuronPlusUrl":response.data.reportUrl,
	    									
										}
										GenericService.serviceAction("POST", URI, data).then(function (response) {
											if (response.data.status == "success") 
											{
								            }
										});
								}

							}, function (response) {
//								alert("error");
							// this function handles error

							});

						});
						}
				if($scope.outsourceLabId == 2){	
					
					$scope.ordersIdListMedall= [];
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					 if(value.serviceSpecialityId == $scope.labConfig.labSpecialityId)
						{
						 if($scope.patientDetails.genderId == 1){
							 $scope.patientDetails.genderId="0"
						 }else if($scope.patientDetails.genderId == 2){
							 $scope.patientDetails.genderId="1"
						 }else{
							 $scope.patientDetails.genderId="2"
						 }
						 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");
							
						 var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
					     var Bx = moment(Birth).format('YYYY-MM-DD');
						 var medallObj =
						 {
							    "name": $scope.patientDetails.firstName,
							    "gender": $scope.patientDetails.genderId,
							    "mobile":  $scope.mobileNumber.toString(),
							    "pincode":  $scope.patientDetails.zipCode != null ? $scope.patientDetails.zipCode :600020,
							    "address": $scope.patientDetails.address != null ? $scope.patientDetails.address:"",
							    "state": $scope.patientDetails.stateName!= null ? $scope.patientDetails.stateName:"",
							    "district": $scope.patientDetails.districtName != null ? $scope.patientDetails.districtName : "",
							    "passport": null,
							    "srfid": null,
							    //"referalid": null,
							    "relationid": "1",
							   // "salutationid": $scope.patientDetails.prefixId != null ?$scope.patientDetails.prefixId:"",
							    "nationality": "Indian",
							    "dob": Bx,
							    "centerid": centerid,
							    "appointmentid": null,
							    "externalid":  $scope.patientDetails.uhIdNumber,
							    "emailid": $scope.patientDetails.email != null ? $scope.patientDetails.email:"",
							    "orderid":value.orderId,
							    "sourceid":"10019471",
							    "hospitaltype":"OP",
							    "studyinfo": []
							}
						 
						 var prefixId = "2513";
						 
							angular.forEach($scope.medallPrifixList, function (val, inx) {
								
								if($scope.patientDetails.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
									prefixId = val.medallSalutationIdPrefixId;
								}
								
							});
							
							medallObj.salutationid = prefixId.toString();
							
							if(value.doctorId != undefined && value.doctorId != null && value.doctorId != 0 ){
								
								var check = true;
								angular.forEach($scope.medallDoctorList, function (val, inx) {
									
									if(value.doctorId == val.doctorId && val.medallReferalId != null){
										medallObj.referalid = val.medallReferalId;
										check = false;
									}
									
								});
								
								if(check == true){
									medallObj.referalid = '5001195';
								}
							}else if( $scope.encounterDetails.referralId != undefined &&  $scope.encounterDetails.referralId != null &&  $scope.encounterDetails.referralId != 0 ){
								
								var check = true;
								angular.forEach($scope.referralMasterList, function (val, inx) {
									
									if( $scope.encounterDetails.referralId == val.referralId && val.medallReferralId != null){
										medallObj.referalid = val.medallReferralId;
										check = false;
									}
									
								});
								
								if(check == true){
									medallObj.referalid = '5001195';
								}
							}else{
								medallObj.referalid = '5001195';
							}
							
						 
						 	if($scope.ordersIdListMedall.length==0){
							 $scope.ordersIdListMedall.push(medallObj);
							}
				     }
				});
				var count=0;
				$scope.checkAutoRenderedServicesForMedall = [];
				angular.forEach($scope.ordersIdListMedall, function (value, key) {
				    angular.forEach($scope.autoRenderedServicesFinalList, function (value1, key1) {
					    if(value.orderid == value1.orderId){
					    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
							{
					    		$scope.checkAutoRenderedServicesForMedall.push(value1);
							}
					    }
					});
				});
				
				angular.forEach($scope.ordersIdListMedall, function (value, key) {
				    angular.forEach($scope.checkAutoRenderedServicesForMedall, function (value1, key1) {
					    if(value.orderid == value1.orderId){
					    	if(value1.serviceSpecialityId == $scope.labConfig.labSpecialityId)
							{
					    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
					    			count++;
						    			var newAer = {};
							    		newAer.billingamount="0";
							    		newAer.billingid="0";
							    		newAer.billingtype="";
							    		newAer.corpbillingid=(value1.medallServiceId).toString();
							    		newAer.discount="0";
							    		newAer.paidamount="0";
							    		newAer.topaid="0";
							    		newAer.extraamount="0";
							    		value.studyinfo.push(newAer);
						    		}
								}
					    }
					});
				});
				if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
					if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
						
								angular.forEach($scope.ordersIdListMedall, function (value, key) {
									var data ={};
									data=value;
									var url = 'https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register';
									config ={
											  "Access-Control-Allow-Origin":'*',
											  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
											  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
											  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
											  'accept' :'application/json, text/javascript, *',
											  'content-type' :'application/json',
											  'scheme' :'https',
											  headers: {
											      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace with your actual credentials
											    }
									};
				
									$http.post(url, data, config).then(function (response) {
				
									// This function handles success
				//						alert("success");
										console.log(response);
										if(response.data.status=="success"){
											growl.success("success");
											
											var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
											var data ={
													encounterId:$scope.encounterDetails.encounterId,
													patientId:$scope.encounterDetails.patientId,
													requestedPayload:JSON.stringify($scope.ordersIdListMedall),
													returnedResponse:JSON.stringify(response.data),
													status:'A',
													organizationId:$scope.organizationId,
													unitId:$scope.unitId,
													addedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													addedBy:$scope.userId,
													updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													updatedBy:$scope.userId
												}
												GenericService.serviceAction("POST", URI, data).then(function (response) {
													if (response.data.status == "success") 
													{
										            }
												});
										}else{
											growl.warning(response.data.status);
										}
									}, function (response) {
				
									});
				
								});
					}else{
						growl.warning("Service is not mapped with Medall.");
					}
				}else{
					growl.warning("Service is not mapped with Medall.");
				}
			}
						// var recieptObj=response.data.resObject;
						$scope.billId=id;
						 $scope.ServiceCount=0;
						// $scope.recieptId=recieptObj.recieptId;
						// $scope.depositeConsumptionId =
						// recieptObj.depositConsumptionId
						$scope.preBillId = "";
						// console.log("$scope.billIdddd", $scope.billId);
						if($scope.finalBill=='Y')
						{
							$("#opdBill").show();
						}								else{}
					}else if (response.data.code==409)
					{
						$rootScope.stopLoader();
						$scope.btnDisabled = false;
						if(response.data.object==true) // if mismatch in bill
														// found
						{
							$scope.getAutoRenderedServicesByPatient({
	    						"visitTypeId":1,
	    						"encounterId":$scope.encounterDetails.encounterId
							});
						}else{
							$scope.patientEncounterList = [];
							$scope.init();
						}
					}else 
					{
						$rootScope.stopLoader();
						angular.element('#collectPayment').modal('show');
						$scope.isNewOpdBill = 'Y';
						$scope.btnDisabled = false;
						$scope.patientEncounterList = [];
						$scope.init();
					}
					
				});
			
			
	}
	/** **************************************** */
	$scope.printFinalBill=function()
	{
		$("#opdBill").hide();
		if($scope.recieptId==undefined || $scope.recieptId == 0)
		{
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/printLog/"+0+"/"+$scope.userId+"/"+$scope.billId;
			BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			{
				if (response.data.status == "success")
				{
					var data =  {
			                "appName": "HIS",
			                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			                "reportDesignFilePath": "opdbillreceipt.rpt",
			                "reportFormat": "PDF",
			                "exportFileName": "PatientBill",
			                "reportParams": [
			                  {
			                 "paramName": "@billid",
			                 "paramType": "Integer",
			                 "paramValue": $scope.billId
			               },{
				                  "paramName": "@isDuplicate",
				                  "paramType": "Integer",
				                  "paramValue": 0
				             }
			             ]
					};
					reportService.generate(REPORT_API_URI1,data);
				}
				
			});
			
			if($scope.packaged.withPackageDetail == 'Y'){
				var data5 =  {
		                "appName": "HIS",
		                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		                "reportDesignFilePath": "opdbillreceipt.rpt",
		                "reportFormat": "PDF",
		                "exportFileName": "PatientBill",
		                "reportParams": [
		                  {
		                 "paramName": "@billid",
		                 "paramType": "Integer",
		                 "paramValue": $scope.billId
		               },{
			                  "paramName": "@isDuplicate",
			                  "paramType": "Integer",
			                  "paramValue": 0
			             }
		             ]
				};
				 reportService.generate(REPORT_API_URI1,data5);
			}
			if($scope.packaged.withRatePackageDetail == 'Y'){
				var data6 =  {
		                "appName": "HIS",
		                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		                "reportDesignFilePath": "opdbillreceipt_PackageRate.rpt",
		                "reportFormat": "PDF",
		                "exportFileName": "PatientBill",
		                "reportParams": [
		                  {
		                 "paramName": "@billid",
		                 "paramType": "Integer",
		                 "paramValue": $scope.billId
		               }
		             ]
				};
				 reportService.generate(REPORT_API_URI1,data6);
			}
		}
		if($scope.recieptId!=undefined && $scope.recieptId > 0)
		{
			
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/0/"+$scope.userId+"/"+$scope.recieptId;
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
									                  "paramValue": $scope.billId
								                },
								                {
									                  "paramName": "@PaymentID",
									                  "paramType": "Integer",
									                  "paramValue": $scope.recieptId
								                },
								                {
									                  "paramName": "@dep_consumption_id",
									                  "paramType": "Integer",
									                  "paramValue": $scope.depositeConsumptionId > 0 ?  $scope.depositeConsumptionId : 0
								                }
								              ]
					  };
					reportService.generate(REPORT_API_URI1,data);
					
					
				}
			});
			
			
		}
		
	
		if($scope.cashSettlement=='N')
		{
			$scope.splitBill();
		}else{
			$scope.btnDisabled = false;
			$scope.patientEncounterList = [];
			$scope.init();
		}
	}
	
	// Change Payee
	$scope.getPayeeListForUpdatePayee = function(){
		if($scope.patientDetails.patientId==null || $scope.patientDetails.patientId=='' || $scope.patientDetails.patientId==undefined)
		{
		growl.warning("Please select Patient..",{
			title : WARNING_MSG
			
		});
		return false;
		}else{
			$scope.patientSponsorDetailsDtosListPopup = [];
			 $scope.priorityList = [];
			 var i = 1;
			 
			angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
					 {
						var newArr = {}	;
						
						newArr.paymentEntitlementId = value.paymentEntitlementId;
						newArr.companyName = value.paymentEntitlementDesc;
						newArr.contractName =value.contractName;
						newArr.medicalCardNo =value.medicalCardNo;
						newArr.employeeName =value.employeeName;
						if(value.validityFrom != null){
							newArr.startDate =  moment(value.validityFrom,'DD-MM-YYYY').format($scope.dateFormat.toUpperCase());
						}else{
							newArr.startDate = null;
						}
						
						if(value.validityTo != null){
					    newArr.endDate =  moment(value.validityTo,'DD-MM-YYYY').format($scope.dateFormat.toUpperCase());
						}else{
							newArr.endDate = null;
						}
						
					    newArr.priority = value.priorityId;
					    newArr.payeeId =value.payeeId;
					    newArr.contractId =value.contractId;
					    newArr.claimNumber =value.claimNumber;
					    newArr.patientSponsorDetailsId =value.patientSponsorDetailsId;
					    
					    	if(value.paymentEntitlementId == 1){
					    		newArr.paymentEntitlementName = "Self";
					  			newArr.companyName = "Self";
					  			newArr.tariffName = "Self Tariff";
					  		    newArr.tariffId = $scope.defaultSelfTariffId;
					  		    newArr.companyId = $scope.selfPayeeIdCookies;
					    	}else if(value.paymentEntitlementId == 2){
					    		newArr.paymentEntitlementName = "Insurance";
					    		newArr.companyName = value.companyDesc;
					  		    newArr.tariffId = value.tariffId;
					  		    newArr.companyId = value.payeeId;
					  		    newArr.insuranceName =	value.insuranceDesc;
								newArr.insuranceNumber = value.insuranceNumber;
								newArr.insuredAmount = value.insuredAmount;
                                newArr.insuranceId =    value.insuranceId;

					  		  
					    	}else{
					    		newArr.paymentEntitlementName = "Corporate";
					    		newArr.companyName = value.companyDesc;
					  		    newArr.tariffId = value.tariffId;
					  		    newArr.companyId = value.payeeId;
					  		    newArr.insuranceName = "";
							    newArr.insuranceNumber = "";
							    newArr.insuredAmount = '';
					    	}
					    $scope.patientSponsorDetailsDtosListPopup.push(newArr);	
					    var prioObj = {
								  id:i,
								  value:i,
							  }
							  $scope.priorityList.push(prioObj);
					    i++;
					 });
			/*
			 * $scope.paymentEntitleMentList= [{
			 * 'paymentEntitlementName':"Self", 'paymentEntitlementId':"1",
			 * 'companyName':"Self", 'tariffName':"Self Tariff",
			 * 'contractName':"", 'insuranceName':"", 'insuranceNumber':"",
			 * 'startDate':"", 'endDate':"", 'priority':"1",
			 * 'companyId':$scope.selfPayeeIdCookies,
			 * 'payeeId':$scope.selfPayeeIdCookies,
			 * 'tariffId':$scope.defaultSelfTariffId, 'contractId':null,
			 * 'insuredAmount' : '' }];
			 */
			
			angular.element('#paymentEntitlementModalForChange').modal('show');
		}
	}
	
	$scope.getDetailsEntitlement = function(type)
	{
		/*
		 * if(type == '2') {
		 *  }
		 */
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
							 // console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
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
		var companyTypeId = $scope.paymentEntitlementTypeIdPopup;
		
		var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:1/* ,billingBedCategoryId:$scope.opdBillingCatId */};
	    
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
		var companyTypeId = $scope.paymentEntitlementTypeIdPopup;
		var companyId = insuranceItem.companyId;
			var asscoiatedCompanyId = insuranceItem.associateCompanyId;
				
			
			var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,associateCompanyId:asscoiatedCompanyId,visitTypeId:1/* ,billingBedCategoryId:$scope.opdBillingCatId */};
			$scope.assoContractList = [];
			$scope.paymentEntitleMentInsurance[0].contractId = '';
			
			var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.assoContractList = response.data.listObject;
					 
					 // getting insurance document name start
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
					// getting insurance document name End
				}
				$rootScope.stopLoader();
			});
	}
	
	$scope.getContractDetails = function(item,idx,contId)
	{
		// alert(idx);
		var contractObj = null;
		contractObj = $.grep($scope.contractList, function (doc) {
            return doc.contractId == contId;
        })[0];
		// console.log("contractObj",contractObj);
		if(contractObj != undefined || contractObj != null)
			{
			  $scope.paymentEntitleMentCorporate[0].datepickerCons =contractObj.validityFrom;
			  $scope.paymentEntitleMentCorporate[0].datepickerConE = contractObj.validityTo;
			}
	}
	
	$scope.checkDuplicatePriority = function(item,idx,priorityVal)
	{
		
		var newDataList = [];
		var le = $scope.patientSponsorDetailsDtosListPopup.length;
		var i = 0;
		angular.forEach($scope.patientSponsorDetailsDtosListPopup, function(v) {
			if (idx != i) {
				newDataList.push(v);
			}
			i++;
		});
		
		
		var found = newDataList.find(ar=>ar.priority==priorityVal);
		  if(found == undefined)
			  {
			  	// return false;
			  }
		  else
			  {
			  $scope.patientSponsorDetailsDtosListPopup[idx].priority = 0;
			  growl.warning("You have aleady assigned "+priorityVal+" priority",{
					title : "WARNING"
				});
				return;
			  }
	}
	
	
	 var isContractAlreadyExists;
		function checkContractAlreadyExistsOrNot(arr, val) {
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
			 }
		 else{
	
		isContractAlreadyExists=checkContractAlreadyExistsOrNot($scope.patientSponsorDetailsDtosListPopup,3);
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
		
		
		
		// console.log("contractObj",contractObj);
		if(contractObj != undefined || contractObj != null)
		{
		  if($scope.paymentEntitlementTypeIdPopup == '3')
		  {
			  
		/*
		 * var prioObj = { id:$scope.priorityList.length + 1,
		 * value:$scope.priorityList.length + 1, }
		 * $scope.priorityList.push(prioObj);
		 */
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
		  newArr.payeeId = contractObj.companyId;
		  newArr.contractName = contractObj.contractName;
		  newArr.insuranceName = "";
		  newArr.insuranceNumber = "";
// newArr.startDate =
// $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+"
// 00:00:00";
// newArr.endDate
// =$rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+"
// 00:00:00";
		  //newArr.startDate = $scope.paymentEntitleMentCorporate[0].datepickerCons != null ?moment($scope.paymentEntitleMentCorporate[0].datepickerCons,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()+' HH:mm:ss'): moment(new Date()).format($scope.dateFormat.toUpperCase()+' HH:mm:ss');// contractObj.validityFrom;
		 // newArr.endDate = $scope.paymentEntitleMentCorporate[0].datepickerConE != null ? moment($scope.paymentEntitleMentCorporate[0].datepickerConE,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()+' HH:mm:ss'): moment(new Date()).format($scope.dateFormat.toUpperCase()+' HH:mm:ss');// contractObj.validityFrom;datepickerInsE
		 
		  newArr.startDate = $scope.paymentEntitleMentCorporate[0].datepickerCons != null ?moment($scope.paymentEntitleMentCorporate[0].datepickerCons,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()+' HH:mm:ss'): moment(new Date()).format($scope.dateFormat.toUpperCase()+' HH:mm:ss');// contractObj.validityFrom;
		  newArr.endDate = $scope.paymentEntitleMentCorporate[0].datepickerConE != null ? moment($scope.paymentEntitleMentCorporate[0].datepickerConE,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()+' HH:mm:ss'): moment(new Date()).format($scope.dateFormat.toUpperCase()+' HH:mm:ss');// contractObj.validityFrom;datepickerInsE

		  
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

		  isInsuranceExists=checkInsuranceExistsOrNot($scope.patientSponsorDetailsDtosListPopup,newArr)
			if (!isInsuranceExists) {
				$scope.enableDisabledApply=false;	
				  var prioObj = {
						  id:$scope.priorityList.length + 1,
						  value:$scope.priorityList.length + 1,
					  }
					  $scope.priorityList.push(prioObj);
				var lenCon = $scope.patientSponsorDetailsDtosListPopup.length;
				if($scope.patientSponsorDetailsDtosListPopup.length > 0)
				{
					angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
						 
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
						  angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value1, key1) {
							  if(value1.paymentEntitlementId != 1)
							  {
							  value1.priority =  "";
							  }
						  });
						  	
						  }
						  
						  
					  });
				}
				
				$scope.patientSponsorDetailsDtosListPopup.push(newArr);
				
				if($scope.patientSponsorDetailsDtosListPopup.length > 1)
				{
					$scope.billTabHide = true;
				}
				$scope.paymentEntitleMentCorporate=
					[{
						'companyId':"",
						'contractId':"",
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
				}
			}
		 }		
		
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
				angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
					 
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
				 
				 }
			 else{
				 
		insuranceCount=checkInsuranceCount($scope.patientSponsorDetailsDtosListPopup)
		if (insuranceCount >= 2) {
			growl.warning("You can't add more than two insurance.",{
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
		var associatedName = $.grep($scope.associateCompanyMasterList, function (doc) {
            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
        })[0].name;
		
		var insuranceId = $.grep($scope.associateCompanyMasterList, function (doc) 
				{
	            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
	        })[0].id;
		
		// console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
		
		if(insuranceObj != undefined || insuranceObj != null)
		{
		  if($scope.paymentEntitlementTypeIdPopup == '2')
		  {
			  var isAfter = true;
			    
			  if(!isAfter)
				  {
					  growl.warning("Start date should be less than end date..!!",{
							title : "WARNING"
						});
						return false;
				  }else{
					  
					 /*
						 * var prioObj = { id:$scope.priorityList.length + 1,
						 * value:$scope.priorityList.length + 1, }
						 * $scope.priorityList.push(prioObj);
						 */
		  var newArr = {};
		  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
		  newArr.paymentEntitlementId="2";
		  newArr.companyName = insuranceObj.assCompanyName;
		  newArr.companyId = insuranceObj.companyId;
		  newArr.payeeId = insuranceObj.companyId;
		// newArr.tariffName = tarrifName;
		  newArr.employeeNoManual = '';
		  newArr.contractName = insuranceObj.contractName;
		  newArr.insuranceName = associatedName;
		  newArr.insuranceId = insuranceId;
		  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
		  newArr.startDate = $scope.paymentEntitleMentInsurance[0].datepickerInss != null ?moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format($scope.dateFormat.toUpperCase()+' HH:mm:ss'): moment(new Date()).format($scope.dateFormat.toUpperCase()+' HH:mm:ss');// contractObj.validityFrom;
		  newArr.endDate = $scope.paymentEntitleMentInsurance[0].datepickerInsE != null ? moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format($scope.dateFormat.toUpperCase()+' HH:mm:ss'): moment(new Date()).format($scope.dateFormat.toUpperCase()+' HH:mm:ss');// contractObj.validityFrom;datepickerInsE
		  newArr.priority = "";
		  newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
		  newArr.contractId = insuranceObj.contractId;
		  newArr.typeId = 2;
		  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
		  newArr.claimNumber = $scope.paymentEntitleMentInsurance[0].claimNumber;
		  if($scope.insuranceObjDoc.insuranceId!=undefined && $scope.insuranceObjDoc.insuranceId!=null  && $scope.paymentEntitleMentInsurance[0].associateCompanyId==$scope.insuranceObjDoc.insuranceId){
			  newArr.reportPath=$scope.insuranceObjDoc.reportPath;
		  }
		  
		  isInsuranceExists=checkInsuranceExistsOrNot($scope.patientSponsorDetailsDtosListPopup,newArr)
			if (!isInsuranceExists) {
				 var prioObj = {
						  id:$scope.priorityList.length + 1,
						  value:$scope.priorityList.length + 1,
					  }
					  $scope.priorityList.push(prioObj);
				  
				var lenIns = $scope.patientSponsorDetailsDtosListPopup.length;
				if($scope.patientSponsorDetailsDtosListPopup.length > 0)
					{
						angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
							 
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
								  angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value1, key1) {
									  if(value1.paymentEntitlementId != 1)
									  {
									  value1.priority =  "";
									  }
								  });
								  	
								  }
							  
							  
						  });
					}
				
				$scope.patientSponsorDetailsDtosListPopup.push(newArr);
				
				if($scope.patientSponsorDetailsDtosListPopup.length > 1)
					{
						$scope.billTabHide = true;
					}
				$scope.paymentEntitleMentInsurance=
					[{
						'companyId':"",
						'associateCompanyId':"",
						'contractId':"",
						// 'statrDate':"",
						// 'endDate':"",
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
		
		  
				}
			}
	}
		
	}
	
	
	
	$scope.addContracts = function()
	{
		var data = {
				'companyId':"",
				'contractId':"",
				// 'statrDate':"",
				// 'endDate':""
			};
		
		$scope.paymentEntitleMentCorporate.push(data);
		
	}
	
	$scope.removeContract = function(index)
	{
		// console.log("removeIndex",index);
		var newDataList = [];
		var tmpprio;
		
		var le = $scope.patientSponsorDetailsDtosListPopup.length;
		
		var tmpObj = angular.copy($scope.patientSponsorDetailsDtosListPopup);
		var i = 1;
		if(le != i){
			
		angular.forEach(tmpObj, function(value,key) {
			
			if (index != i) {
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
						value.priority =value.priority - 1;
					}
					}
				}
			}else{
				tmpprio = value.priority;
				$scope.patientSponsorDetailsDtosListPopup.splice(index - 1,1);
			}
			
			if(i == le)
				{
					if(newDataList.length == 1){
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
		 
		$scope.patientSponsorDetailsDtosListPopup = newDataList;
		if($scope.patientSponsorDetailsDtosListPopup.length == 1)
			{
			$scope.patientSponsorDetailsDtosListPopup[0].priority = "1";
			}
		if($scope.patientSponsorDetailsDtosListPopup.length == 1)
			{
			  $scope.billTabHide = false;
			}
		else
			{
			  $scope.billTabHide = true;
			}
		
		
		}
		
		$scope.enableDisabledApply=true;	
		
	}
	
	$scope.addInsurances = function()
	{
		var data = {
				'companyId':"",
				'associateCompanyId':"",
				'contractId':"",
				// 'statrDate':"",
				// 'endDate':"",
				'insuranceNumber':"",
				'insuredAmt':""
			};
		$scope.paymentEntitleMentInsurance.push(data);
	}
/*
 * $scope.getCompanyMasterList = function(){ $rootScope.startLoader(); var URI =
 * BASE_URL + ROOT_URL + COMPANY_MASTER + UNIT + SLASH + $scope.unitId + ORG +
 * SLASH + $scope.organizationId;
 * BillingGenericService.serviceAction(METHOD_GET, URI, "",
 * NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
 * $scope.companyMasterList = []; $scope.associatedCompanyList = []; if
 * (response.data.status == "success") { $scope.companyMasterList =
 * response.data.listObject; } }); } $scope.getCompanyMasterList();
 */
	$scope.getAssociatedCompanyMasterListByCompanyId = function(companyId){
		$rootScope.startLoader();
		 var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
		 $scope.associateCompanyMasterList = [];
		 $scope.paymentEntitleMentInsurance[0].associateCompanyId = '';
		 
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.associateCompanyMasterList = response.data.listObject;
					  // console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
				}
				$rootScope.stopLoader();
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
// if (value.file == undefined || value.file == "" || value.file == null ) {
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
		 if($scope.patientDetails.patientId > 0){
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
								   // $scope.getPatientDocumentList();
								   
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
// $scope.patientDocumentList = response.data.listObject;
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
         					createdDate:value.createdDate,
         					bucketFlag:value.bucketFlag
         			}
         			$scope.patientDocumentList.push(data);
     			}
            }else if(obj.paymentEntitlementId==2){
            	if(value.documentTypeId==30){				            				
         			
         			var data={
         					documentTypeId:value.documentTypeId,
         					documentName:value.documentName,
         					documentPath:value.documentPath,
         					createdDate:value.createdDate,
         					bucketFlag:value.bucketFlag
         			}
         			$scope.patientDocumentList.push(data);
     			}
            }else if(obj.paymentEntitlementId==3){
            		if(value.documentTypeId==19){				            				
         			
         			var data={
         					documentTypeId:value.documentTypeId,
         					documentName:value.documentName,
         					documentPath:value.documentPath,
         					createdDate:value.createdDate,
         					bucketFlag:value.bucketFlag
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
					/*
					 * growl.warning( 'Please select atleast one patient!!!.', {
					 * title : 'warning!' });
					 * $scope.encounterDetailsContractList =[]; return;
					 */
					
					// $scope.tab=1;
					$scope.secondTabLevel = 1 ;
					/*
					 * growl.warning("Please Select Patient",{ title :
					 * WARNING_MSG }); return;
					 */
					
					$scope.$broadcast('show-errors-check-validity');
					// $scope.secondTabLevel=1;
			        if($scope.patientRegistrationform.$valid){
			        	// alert("valid");
			        	
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
						/* if($scope.patientRegistrationform2.$valid){ */
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
			        		 // test start
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
			        		
			        		 // test end
			        			 
			        }else{
			        	growl.warning(" Please select patient or add patient details..!! ",{
							title : WARNING_MSG
						});
			        	return false;
			        }
			        
				}
			
			/*
			 * $scope.encounterDetailsContractList =[]; var encounterDetails =
			 * angular.copy($scope.encounterDetailsContract);
			 * encounterDetails.patientId = $scope.patientDetails.patientId;
			 * $scope.encounterDetailsContractList.push(encounterDetails);
			 * $scope.getPatientDocumentList();
			 */
				 
				 
				 
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
	 
	 var isPriorityZeroExists;
		function checkPriorityZeroOrNot(arr) {
			return arr
					.some(function(arr) {
						if(parseInt(arr.priority) == 0 || arr.priority == "" || arr.priority == -1)
							{
								return true;
							}
						else
							{
								return false;
							}
					});
		}
		
	  
	 $scope.savePatintAndSponsor = function(){
		 
		 isPriorityZeroExists=checkPriorityZeroOrNot($scope.patientSponsorDetailsDtosListPopup);
		  if (isPriorityZeroExists) {
			
			  growl.warning("Please assign priority for payment entitlements.",{	
				title : "WARNING"
			});
			return false;
			
		  }
		  else{
			 // $rootScope.startLoader();
			  var data = {
					  patientCategoryId:$scope.patientDetails.patientTypeId,
					  visitTypeId:1,
					  selfPayeeId:$scope.selfPayeeId,
					  defaultSelfTariffId:$scope.defaultSelfTariffId,
					  admissionId:null,
					  encounterId :$scope.encounterDetails.encounterId,
					  billingBedCategoryId:$scope.opdBillingCatId,
					  isPreBilling:$scope.isPreOrPostBilling,
					  patientId:$scope.patientDetails.patientId,
					  orgId: $scope.organizationId,
					  unitId:  $scope.unitId,
					  referralId: $scope.encounterDetails.referralId,
					  patientClassId:  $scope.encounterDetails.patientClassId,
					  pharmacyServiceId : $scope.pharmacyServiceId
			  };
				// console.log('$scope.paymentEntitleMentList',$scope.patientSponsorDetailsDtosListPopup);
				var sponserList = [];
				angular.forEach($scope.patientSponsorDetailsDtosListPopup, function (value, key) {
					var newArr = {};
					newArr.organizationId = $scope.organizationId ;
					newArr.unitId = $scope.unitId;
					newArr.visitTypeId = 1;
					newArr.paymentEntitlementId =value.paymentEntitlementId;
					newArr.payeeId = value.payeeId;
					newArr.priorityId = value.priority;
					newArr.createdBy =$scope.userId;
					newArr.updatedBy = $scope.userId;
					// createdDate : moment(new Date()).format("DD-MM-YYYY"),
					// updatedDate : moment(new Date()).format("DD-MM-YYYY"),
					newArr.status = 'A';
					newArr.tariffId =value.tariffId;
					newArr.contractId =value.contractId;
					newArr.insuranceNumber = value.insuranceNumber;
					newArr.insuredAmount = value.insuredAmount;
					newArr.insuranceId = value.insuranceId;
					newArr.applicableDate = moment($scope.encounterDetails.encounterDateString2,$scope.dateFormat.toUpperCase()+" HH:mm:ss").format("DD-MM-YYYY HH:mm:ss");
					if(value.paymentEntitlementId != 1){


						 var dff =  moment(value.startDate,$scope.dateFormat.toUpperCase()+" HH:mm:ss").format("DD-MM-YYYY HH:mm:ss");
						 var dff1 =  moment(value.endDate,$scope.dateFormat.toUpperCase()+" HH:mm:ss").format("DD-MM-YYYY HH:mm:ss");
						newArr.validityTo = (dff1 != null || dff1 != undefined) ? dff1 : null;
	                    newArr.validityFrom = (dff != null || dff != undefined) ? dff : null; 
					}else{
					
							newArr.validityTo = null;
							newArr.validityFrom = null;
					}
					
					if(value.priority == 1)
						{
							data.contractId = value.contractId;
							data.paymentEntitlementId = value.paymentEntitlementId;
						}
					newArr.employeeNoManual = value.employeeNoManual;
					newArr.employeeName = value.employeeName;
					newArr.relation = value.relation;
					newArr.medicalCardNo = value.medicalCardNo;
                    newArr.claimNumber =value.claimNumber;
					sponserList.push(newArr);
				});
				data.patientSponsorDetailsDtoList = sponserList;
				
				console.log("data",data);
				// return false;
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
						+ "/api/billing/updatePayee";
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									angular.element('#paymentEntitlementModalForChange').modal('hide');
									if (response.data.status == "success") {
										
										var data1 = {};
										angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
											value.tariffId=$scope.defaultSelfTariffId;
										})
										var URI1 = BASE_URL + ROOT_URL + "/api/opd/encounter/"+$scope.encounterDetails.encounterId;
										BillingGenericService.serviceAction(METHOD_GET,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status == "success"){
												$scope.patientSponsorDetailsDtosListSplit = [];
												$scope.patientSponsorDetailsDtosListSplit = response.data.object.patientSponsorDetailsDtosList;
												var co = 1;
												angular.forEach(response.data.object.patientSponsorDetailsDtosList,function(value,index)
												 {
													
													if(value.capExist==true)
														$scope.capExist = true;
													
													if(value.priorityId == '1')
													{
														$scope.encounterDetails.paymentEntitlementDesc  = value.paymentEntitlementDesc;
														$scope.encounterDetails.companyDesc = value.companyDesc;
														$scope.encounterDetails.contractDesc = value.contractName;
														$scope.encounterDetails.validityFrom = value.validityFrom;
														$scope.encounterDetails.validityTo = value.validityTo;
														$scope.encounterDetails.payeeId = value.payeeId;
														$scope.encounterDetails.paymentEntitlementTypeId = value.paymentEntitlementId;
														
														if($scope.encounterDetails.payeeId != $scope.selfPayeeId)
															{
																$scope.contractId = value.contractId;
															}
														else
															{
																$scope.contractId = 0;
															}
													}
													if(co == $scope.patientSponsorDetailsDtosListSplit.length){
														$rootScope.stopLoader();
														$scope.needToUpdatePharmacyService = true;
														$scope.updatePayee = true;
														$scope.saveOrderPackage();
													}
													co++
												});
												
											}
											
										});
										
										// $scope.init();
										
									} else
										{
										  $rootScope.stopLoader();
										}

								});
		  }
	 }
	 
	// CHange Payee
	
	$scope.splitBill=function(){
		if($scope.isSplitBillFlag)
			{
				var preAuthReqIdList = [];
				angular.forEach($scope.addNewRequestList,function(value,index)
						{
					preAuthReqIdList.push(value.preauthRequestId);
				});
				var splitBillData = 
				{ "preAuthReqIds":preAuthReqIdList,
				  "encounterId": $scope.encounterDetails.encounterId
				}
				
				var URI = BASE_URL + ROOT_URL + "/api/ipd/updateIsSplitStatusByPreAuthorizationId";
				BillingGenericService.serviceAction(METHOD_POST,URI, splitBillData , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
					if(response.data.status == 'success')
					{
						
					}
				});
				  $state.go('billList');
			}
			else
			{
				$state.go('billList');
			}
	};
	/** ************************************************** */
	$scope.getSpecialityByDoctor = function(docId,idx)
		{
		var specDocObj = {
		    			unitId:$scope.unitId,
		    			organizationId:$scope.organizationId,
		    			doctorId:docId
		    }
		var specialityLsit=[];
		angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
				{
					if(service.doctorId==docId)
						{
						specialityLsit.push(service);
						}
				});
		if(specialityLsit.length == 0){
        	$scope.servicesListPopup[idx].doctorId ="";
               growl.warning("Selected Doctor Department and Sub Department not mapped in the unit masters !! ",{
                    title : "WARNING"
                });
                return false; 
        }
	 $scope.servicesListPopup[idx].docSpecialityList = specialityLsit;
		$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
         return docList.doctorId == docId;
        })[0];


	 if($scope.servicesListPopup[idx].doctorSpecialityId==undefined || $scope.servicesListPopup[idx].doctorSpecialityId==null ||$scope.servicesListPopup[idx].specialityId=="")

     {
		 if(specialityLsit.length == 1){
				$scope.servicesListPopup[idx].doctorSpecialityId =specialityLsit[0].specialityId;
			}else{
				$scope.servicesListPopup[idx].doctorSpecialityId =specialityLsit[0].specialityId;
			}
     }
//		    var URI = BASE_URL + ROOT_URL
//			+ GETSPECIALITYLISTBYDOCTORID;
//	BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
//					NOTIFICATION_MSG_STATUS_FALSE)
//			.then(function(response) {
//						// console.log(response);
//						
//						// $rootScope.startLoader();
//						if (response.data.status == "success")
//							{
//							// $scope.docSpecialityList =
//							// response.data.listObject;
//							$scope.servicesListPopup[idx].docSpecialityList = response.data.listObject;
//							$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
//					            return docList.doctorId == docId;
//					           })[0];
//							
//							if(response.data.listObject.length == 1){
//								$scope.servicesListPopup[idx].doctorSpecialityId =response.data.listObject[0].specialityId;
//							}else{
//								$scope.servicesListPopup[idx].doctorSpecialityId =response.data.listObject[0].specialityId;
//							}
//								/*
//								 * if($scope.docSplObj!=null &&
//								 * $scope.docSplObj!=undefined ){
//								 * $scope.servicesListPopup[idx].doctorSpecialityId =
//								 * $scope.docSplObj.specialityId; }
//								 */
//							}
//					});
	 }
	$scope.getInitiallySpecialityList=[];
	$scope.getSpecialityByDoctorMain = function(docId,idx)
	{
//		if(docId > 0){
			var specDocObj = {
	    			unitId:$scope.unitId,
	    			organizationId:$scope.organizationId,
	    			doctorId:docId
	    }
	    var URI = BASE_URL + ROOT_URL
		+ GETSPECIALITYLISTBYDOCTORID;
		BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					// console.log(response);
					
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						$scope.getInitiallySpecialityList=response.data.listObject;
//						$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
//						if($scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == '' || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == null || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == undefined)
//							{
//							$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
//							$scope.autoRenderedServicesFinalList[idx].specialityName = response.data.listObject[0].specialityName;
//							}
						}
				});
//	}
	
 }
	$scope.getSpecialityByDoctorMainChange = function(docId,idx,flag)
	{
//		$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = null;
		if(docId!=undefined && docId!=null && docId!=0)
			{
		var specDocObj = {
	    			unitId:$scope.unitId,
	    			organizationId:$scope.organizationId,
	    			doctorId:docId
	    }
		var specialityLsit=[];
		angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
				{
					if(service.doctorId==docId)
						{
						specialityLsit.push(service);
						}
				});
		
		if(specialityLsit.length == 0){
			$scope.autoRenderedServicesFinalList[idx].doctorId ="";
			   growl.warning("Selected Doctor Department and Subdepartment not mapped in the unit masters !! ",{
                    title : "WARNING"
                });
                return false; 
		}
		
		$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList=[];
		$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = specialityLsit;
		 if(specialityLsit!=null && specialityLsit.length > 0 && flag == true)
         {
			$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
			$scope.autoRenderedServicesFinalList[idx].specialityName = specialityLsit[0].specialityName;
         }
		 
         if($scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==undefined || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==null && flag == false){
        	 $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
 			 $scope.autoRenderedServicesFinalList[idx].specialityName = specialityLsit[0].specialityName;
         }
		 
		$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx);
//	    var URI = BASE_URL + ROOT_URL
//		+ GETSPECIALITYLISTBYDOCTORID;
//		BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
//				NOTIFICATION_MSG_STATUS_FALSE)
//		.then(function(response) {
//					if (response.data.status == "success")
//						{
//							$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
//							$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
//							$scope.autoRenderedServicesFinalList[idx].specialityName = response.data.listObject[0].specialityName;
//							$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx);
//						}
//				});
	}
	}
	
	// Ganesh
	$scope.getSpecialityByDoctorService = function(docId,idx)
	{
		var specDocObj = {
	    			unitId:$scope.unitId,
	    			organizationId:$scope.organizationId,
	    			doctorId:docId
	    }
		var specialityLsit=[];
		angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
				{
					if(service.doctorId==docId)
						{
						specialityLsit.push(service);
						}
				});
		
		 if(specialityLsit.length == 0){
             $scope.autoRenderedServicesFinalList[idx].doctorId ="";
                growl.warning("Selected Doctor Department and Sub Department not mapped in the unit masters !! ",{
                     title : "WARNING"
                 });
                 return false; 
         }
		 
	 $scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = specialityLsit;
	 $scope.autoRenderedServicesFinalList[idx].docSpecialityList=[];
	 $scope.autoRenderedServicesFinalList[idx].docSpecialityList=specialityLsit;
	 if($scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==undefined || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==null || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId==0)
     {
		$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
     }
     else if(specialityLsit.length==1 && $scope.autoRenderedServicesFinalList[idx].doctorId>0)
 	{
 	$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId =specialityLsit[0].specialityId;
 	}
	 $scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx);
//	    var URI = BASE_URL + ROOT_URL
//		+ GETSPECIALITYLISTBYDOCTORID;
//		BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
//				NOTIFICATION_MSG_STATUS_FALSE)
//		.then(function(response) {
//					// console.log(response);
//					if (response.data.status == "success")
//						{
//							$scope.autoRenderedServicesFinalList[idx].docSpecialityList = response.data.listObject;
//							$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
//							$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
//							$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx);
//						}
//				});
 }
	
	
	$scope.getSpecialityByDoctorForPackage = function(packageIndex,docId,idx)
	{
		var specDocObj = {
	    			unitId:$scope.unitId,
	    			organizationId:$scope.organizationId,
	    			doctorId:docId
	    }
		var specialityLsit=[];
		angular.forEach($scope.getInitiallySpecialityList, function (service, key) 
				{
					if(service.doctorId==docId)
						{
						specialityLsit.push(service);
						}
				});
		
		 if(specialityLsit.length == 0){
			 $scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorId ="";
                growl.warning("Selected Doctor Department and Sub Department not mapped in the unit masters !! ",{
                     title : "WARNING"
                 });
                 return false; 
         }
		 
		$scope.selectedPackageList[packageIndex].packageServiceList[idx].docSpecialityList = specialityLsit;
	 if($scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId==undefined || $scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId==null || $scope.selectedPackageList[packageIndex].serviceSpecialityId==undefined )
          {
     		  if(specialityLsit.length == 1){
     				$scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
     			}else{
     				$scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
     			}
     		// $scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId = specialityLsit[0].specialityId;
          }
//	    var URI = BASE_URL + ROOT_URL
//		+ GETSPECIALITYLISTBYDOCTORID;
//		BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
//				NOTIFICATION_MSG_STATUS_FALSE)
//		.then(function(response) {
//					// console.log(response);
//					
//					// $rootScope.startLoader();
//					if (response.data.status == "success")
//						{
//							$scope.selectedPackageList[packageIndex].packageServiceList[idx].docSpecialityList = response.data.listObject;
//							$scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
//						}
//					
//				});
	}
	$scope.getDoctorBySpeciality = function (specialityId,idx,packageIndex) {

		var data = {
			specialityId: specialityId,
			unitId: $scope.unitId,  
			organizationId: $scope.organizationId
		};
		var URI = BASE_URL + ROOT_URL+ GETDOCTORBYSPECIALITYID;

		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
			.then(
				function (response) {
					if (response.data.status == "success")
						{
						
						 if(packageIndex!=null && idx!=null)
							 {
							 $scope.selectedPackageList[packageIndex].packageServiceList[idx].docList =  response.data.listObject
							 }else{
								 if($scope.autoRenderedServicesFinalList[idx].docList.length == 0)
									 	$scope.autoRenderedServicesFinalList[idx].docList = response.data.listObject;
							 }
						}
					else if (response.data.status == "error")
					{
						$rootScope.stopLoader();
					}
				});

	}
	$scope.getDoctorBySpecialityList = function (specialityId) {

		var data = {
			specialityId: specialityId,
			unitId: $scope.unitId,  
			organizationId: $scope.organizationId
		};
		var URI = BASE_URL + ROOT_URL+ GETDOCTORBYSPECIALITYID;

		return BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
			.then(
				function (response) {
					if (response.data.status == "success")
						{
							 return $scope.docLists =  response.data.listObject
							 
						}
					
				});

	}
	
/** *=========================Payer tab=========================** */
	
	

	$scope.corEmpVerificationFlag=false;
	/*
	 * $scope.getCoEmpVerificationReq = function(){ try{ var URI = BASE_URL +
	 * ROOT_URL +
	 * "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
	 * var obj = { organizationId :$scope.organizationId, unitId :$scope.unitId }
	 * BillingGenericService.serviceAction(METHOD_POST,URI, obj,
	 * NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	 * if(response.data.status == "success"){ $scope.unitConfigurationObject =
	 * response.data.listObject[0];
	 * if($scope.unitConfigurationObject.isCorporateEmpVerificationReq=='Y'){
	 * $scope.corEmpVerificationFlag=true; $scope.enableDisabledApply=false;
	 * $scope.enableDisabledValidated=false;
	 * 
	 * }else{ $scope.corEmpVerificationFlag=false;
	 * $scope.enableDisabledApply=true; $scope.enableDisabledValidated=false;
	 *  } } }); }catch (e) { //console.log("Exception",e.message); } }
	 * $scope.getCoEmpVerificationReq();
	 */
	
	$scope.initCoEmp=function(){
		$scope.corEmpObj={
				typeOfCoEmp:'3',
// employeeNumber:"",
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
	
	
	$scope.clearCoEmpDetails=function(){
		/*
		 * $scope.corEmpDependantList=[]; $scope.showNameDetails=false;
		 * $scope.showHideAcceptRejectBtn=false; $scope.showHideOkBtn=false;
		 */
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
	$scope.empNoShowHide=true; // for empNo text field hide show
	$scope.noteShowHide=false; // for note text area hide show
	$scope.dependTblShowHode=false; // for table hide show
	$scope.showHidevalidate=true; // for validate buttton
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
		// for staff or employee
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
// genderId:$scope.patientDetails.genderId,
// birthDate:$scope.patientDetails.birthDate
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
						 // console.log("$scope.corEmplist",$scope.corEmpObj);
// $scope.initCoEmp();
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
// angular.element('#validationEmployeeModel').modal('hide');
						}else{
							  $scope.color="red";
							  $scope.showNameDetails=true;
							  $scope.name="record not found.."
							  $scope.showHideAcceptRejectBtn=false;
							  $scope.showHideOkBtn=true;
						}
						
						
					}
// $scope.initCoEmp();
				});
		}
		
		$scope.getEmpDependantDetailsByIndex=function(index){
			$scope.showNameDetails=false;
			     if($scope.patientDetails.firstName==$scope.corEmpDependantList[index].firstName) {
// $scope.name=$scope.corEmpDependantList[index].firstName+"
// "+$scope.corEmpDependantList[index].middleName+"
// "+$scope.corEmpDependantList[index].lastName;
			    	 if($scope.patientDetails.genderId==$scope.corEmpDependantList[index].genderId){
			    		       
// $scope.color="green";
								$scope.enableDisabledValidated=false;										
								$scope.showHideAcceptRejectBtn=true;
							
			    	 }else{
// $scope.color="red";
							$scope.enableDisabledValidated=false;										
							$scope.showHideAcceptRejectBtn=true;
						}
			     }else{
// $scope.color="red";
						$scope.enableDisabledValidated=false;										
						$scope.showHideAcceptRejectBtn=true;
					}
			 	$scope.showNameDetails=true;
// $scope.initCoEmp();
			
		}
		
		
	}
	
	$scope.getMedicationIssueListByEncounterId = function()
	{
		$scope.medSummary=0;
		$scope.medicationIssueList=[];
		var param = {encounterId:$scope.encounterDetails.encounterId,unitId:$scope.unitId};
		var URI =  BASE_URL + ROOT_URL +"/api/pharmacy/ip/pending/issues/get/drug/items";
		BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.status == "200")
			{
				$scope.medicationIssueList = response.data;
				
				angular.forEach($scope.medicationIssueList,function(v,k){
					$scope.medSummary = $scope.medSummary + parseFloat(v.netAmount);
		        });
			}
		});	
	}
	
	
	$scope.getPharmacyDetails = function(orderId)
	{
		$scope.medicationIssueListByOrderId = [];
		$scope.medTotalAmt=0;
		$scope.medTotalConcession=0;
		$scope.medTotalDiscount=0;
		$scope.medNetAmount=0;
		$scope.medTaxAmount=0;
		$scope.medTotalSelf=0;
		$scope.medTotalCredit=0;
		$scope.medTotalPackageAmt=0;
		angular.forEach($scope.medicationIssueList, function (value, key) 
		{
			if(value.orderId == orderId)
			{
				$scope.medTotalAmt = $scope.medTotalAmt + value.totalAmount;
				$scope.medTotalConcession = $scope.medTotalConcession + value.ordConcession;
				$scope.medTotalDiscount=$scope.medTotalDiscount + value.ordDiscount;
				$scope.medNetAmount=$scope.medNetAmount + value.netAmount;
				$scope.medTaxAmount=0;
				$scope.medTotalSelf=$scope.medTotalSelf + value.selfPayable;
				$scope.medTotalCredit=$scope.medTotalCredit + value.creditPayable;
				$scope.medTotalPackageAmt = $scope.medTotalPackageAmt+ value.packageConsumptionAmt;
				$scope.medicationIssueListByOrderId.push(value);
			}
			
		});
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
	/*
	 * else if($scope.corEmpObj.note==undefined || $scope.corEmpObj.note=="" ||
	 * $scope.corEmpObj.note==null){ growl.warning("Please enter note",{ title :
	 * WARNING_MSG }); return; }
	 */
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
	/*
	 * $scope.corEmpDependantList=[]; $scope.showNameDetails=false;
	 * $scope.showHideAcceptRejectBtn=false; $scope.showHideOkBtn=false;
	 */
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
	$scope.newRequestCalculates = function()
	{
		var totalSummary=0;
		var totalAuthSummary=0;
		angular.forEach($scope.addNewRequestList,function(value,index){
			totalSummary = parseFloat(totalSummary) + (isNaN(value.requestAmount) ? 0 : parseFloat(value.requestAmount));
			totalAuthSummary = parseFloat(totalAuthSummary) + (isNaN(value.authorizationAmount) ? 0 : parseFloat(value.authorizationAmount));
		});
		$scope.summary = $rootScope.parseFloatNum(totalSummary);
		$scope.authsummary = $rootScope.parseFloatNum(totalAuthSummary);
		$scope.authsummarySplitBill = $scope.authsummary;
  }
 
	
	$scope.calculateSplitBillTotal = function(inputList)
	{
		$scope.totalAmountSummarySplitBill = 0;
		$scope.totalCreditPaybleSplitBill =0 ;
		$scope.totalSelfPaybleSplitBill = 0;
		$scope.totalNetAmt = 0;
		$scope.totalCreditPaybleSplitBillOld =0 ;
		$scope.totalSelfPaybleSplitBillOld = 0;
		$scope.totalDiscountSplit = 0;
		$scope.totalConsessionSplit = 0;
		$scope.totalGstSplit = 0;

		angular.forEach(inputList,function(value,index)
		{
			angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
			{
				if(value1.isPackageService=='N' && value.status=='A')
				{
					value.selfPayable = (isNaN(value.selfPayable)||value.selfPayable==undefined)?0:value.selfPayable;
					value.creditPayable = (isNaN(value.creditPayable)||value.creditPayable==undefined)?0:value.creditPayable;
					$scope.totalAmountSummarySplitBill = $scope.totalAmountSummarySplitBill+parseFloat(value.totalAmt).toFixed(2);
					$scope.totalCreditPaybleSplitBill =(parseFloat($scope.totalCreditPaybleSplitBill)+parseFloat(value.creditPayable)).toFixed(2);
					$scope.totalSelfPaybleSplitBill = (parseFloat($scope.totalSelfPaybleSplitBill)+parseFloat(value.selfPayable)).toFixed(2);
		            $scope.totalCreditPaybleSplitBillOld =(parseFloat($scope.totalCreditPaybleSplitBillOld) + parseFloat(value.creditPayable)).toFixed(2);
		            $scope.totalSelfPaybleSplitBillOld = (parseFloat($scope.totalSelfPaybleSplitBillOld) +parseFloat(value.selfPayable)).toFixed(2);
		            $scope.totalNetAmt =(parseFloat($scope.totalNetAmt)+parseFloat(value.netPay)).toFixed(2);
		            $scope.totalDiscountSplit = (parseFloat($scope.totalDiscountSplit)+ parseFloat(value.discount)).toFixed(2);
		            $scope.totalConsessionSplit = (parseFloat($scope.totalConsessionSplit)+ parseFloat(value.concession)).toFixed(2);
		            $scope.totalGstSplit = (parseFloat($scope.totalGstSplit)+ parseFloat(value.taxAmt)).toFixed(2);
				}
				
			});
			
						
		});
		$scope.totalAmountSummarySplitBill = parseFloat($scope.totalAmountSummarySplitBill).toFixed(2);
		$scope.totalCreditPaybleSplitBill =parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2);
		$scope.totalSelfPaybleSplitBill = parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2);
		this.$parent.totalCreditPaybleSplitBill =parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2);
		this.$parent.totalSelfPaybleSplitBill = parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2);
		$scope.totalCreditPaybleSplitBillOld =parseFloat($scope.totalCreditPaybleSplitBillOld).toFixed(2);
		$scope.totalSelfPaybleSplitBillOld = parseFloat($scope.totalSelfPaybleSplitBillOld).toFixed(2);
		$scope.totalNetAmt = parseFloat($scope.totalNetAmt).toFixed(2);
        
        /*if($scope.resetvalue == true){
       	 var checkAmount2 = parseFloat(Number(parseFloat($scope.totalAmountSummarySplitBill).toFixed(2))-Number(parseFloat($scope.totalConsessionSplit).toFixed(2))-Number(parseFloat($scope.totalDiscountSplit).toFixed(2))).toFixed(2);
            var checkAmount = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))).toFixed(2);
           
        if(parseFloat(checkAmount2) > parseFloat(checkAmount)){
            var amounjj = 0 ;
            amounjj = parseFloat(Number(parseFloat(checkAmount2).toFixed(2))-Number(parseFloat(checkAmount).toFixed(2))).toFixed(2);
               if($scope.roundOffAmt!=0 && $scope.totalSelfPaybleSplitBill!=0)
               {
                   $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
               }else{
                   $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
               }
       }else{
            var amounjj = 0 ;
            amounjj = parseFloat(Number(parseFloat(checkAmount).toFixed(2))-Number(parseFloat(checkAmount2).toFixed(2))).toFixed(2);
               if($scope.roundOffAmt!=0 && $scope.totalSelfPaybleSplitBill!=0)
               {
                   $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
               }else{
                   $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
               }
       }
        $scope.resetvalue = false;
      }
		
		if($scope.serviceList != undefined  && $scope.selfSlitBillAmount == true && $scope.serviceList.length == $scope.co ){
           var checkAmount2 = parseFloat(Number(parseFloat($scope.totalAmountSummarySplitBill).toFixed(2))-Number(parseFloat($scope.totalConsessionSplit).toFixed(2))-Number(parseFloat($scope.totalDiscountSplit).toFixed(2))).toFixed(2);
            var checkAmount = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))).toFixed(2);
               
            if(parseFloat(checkAmount2) > parseFloat(checkAmount)){
                var amounjj = 0 ;
                amounjj = parseFloat(Number(parseFloat(checkAmount2).toFixed(2))-Number(parseFloat(checkAmount).toFixed(2))).toFixed(2);
                       $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
           }else{
                var amounjj = 0 ;
                amounjj = parseFloat(Number(parseFloat(checkAmount).toFixed(2))-Number(parseFloat(checkAmount2).toFixed(2))).toFixed(2);
                       $scope.totalCreditPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
           }
            $scope.selfSlitBillAmount =false;
			}
			
			if($scope.serviceList != undefined &&  $scope.creditSlitBillAmount == true && $scope.serviceList.length == $scope.co){
               var checkAmount2 = parseFloat(Number(parseFloat($scope.totalAmountSummarySplitBill).toFixed(2))-Number(parseFloat($scope.totalConsessionSplit).toFixed(2))-Number(parseFloat($scope.totalDiscountSplit).toFixed(2))).toFixed(2);
                var checkAmount = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+Number(parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2))).toFixed(2);
                   
                if(parseFloat(checkAmount2) > parseFloat(checkAmount)){
                    var amounjj = 0 ;
                    amounjj = parseFloat(Number(parseFloat(checkAmount2).toFixed(2))-Number(parseFloat(checkAmount).toFixed(2))).toFixed(2);
                           $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))+parseFloat(amounjj)).toFixed(2);
               }else{
                    var amounjj = 0 ;
                    amounjj = parseFloat(Number(parseFloat(checkAmount).toFixed(2))-Number(parseFloat(checkAmount2).toFixed(2))).toFixed(2);
                           $scope.totalSelfPaybleSplitBill = parseFloat(Number(parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2))-parseFloat(amounjj)).toFixed(2);
               }
                
                $scope.creditSlitBillAmount = false;
				}*/
		
		return inputList;
	}

	$scope.showSplitBill = function()
	{
		$scope.serviceList = [];
		$scope.getAuthrizationDetails();
		$scope.serviceList = angular.copy($scope.autoRenderedServicesFinalList);
		$scope.serviceListOLD = angular.copy($scope.autoRenderedServicesFinalList);
		$scope.calculateSplitBillTotal($scope.serviceList);
		$('#splitBillModal').modal('show');
	}
	

	/*
	 * $scope.calculateSplitBillTotal = function(inputList) {
	 * $scope.totalAmountSummarySplitBill = 0; $scope.totalCreditPaybleSplitBill
	 * =0 ; $scope.totalSelfPaybleSplitBill = 0;
	 * 
	 * angular.forEach(inputList,function(value,index) { value.selfPayable =
	 * (isNaN(value.selfPayable)||value.selfPayable==undefined)?0:value.selfPayable;
	 * value.creditPayable =
	 * (isNaN(value.creditPayable)||value.creditPayable==undefined)?0:value.creditPayable;
	 * $scope.totalAmountSummarySplitBill =
	 * $scope.totalAmountSummarySplitBill+parseFloat(value.totalAmt);
	 * $scope.totalCreditPaybleSplitBill
	 * =$scope.totalCreditPaybleSplitBill+parseFloat(value.creditPayable);
	 * $scope.totalSelfPaybleSplitBill =
	 * $scope.totalSelfPaybleSplitBill+parseFloat(value.selfPayable); });
	 * $scope.totalAmountSummarySplitBill =
	 * parseFloat($scope.totalAmountSummarySplitBill).toFixed(2);
	 * $scope.totalCreditPaybleSplitBill
	 * =parseFloat($scope.totalCreditPaybleSplitBill).toFixed(2);
	 * $scope.totalSelfPaybleSplitBill =
	 * parseFloat($scope.totalSelfPaybleSplitBill).toFixed(2); return inputList; };
	 */
  
	
	$scope.applySplitBill = function()
	{
		$scope.applyAuth = true;
		$scope.saveOrderPackage();
	}
	
	$scope.getSmartCardBalance = function(){
		
		$rootScope.startLoader();
		$scope.smartCardBalanceAmount = 0;
				$scope.patientDetailsListForClaim = response.data.listObject;

				var data = {
						uhidNumber : $scope.patientDetails.uhIdNumber,
						unitId:$scope.unitId,
						organizationId:$scope.organizationId,
				}
				// console.log("data",data);
				var URL = BASE_URL + ROOT_URL+"/api/billing/getSmartCardBalance";
				GenericService.serviceAction("POST", URL,data).then(function(response) {
					if(response.data.status == "success"){
						
						// console.log(response.data.object)
						$scope.smartCardBalanceAmount = response.data.object;
						$('#getSmartCardBalancePopup').modal('hide');
						   $rootScope.stopLoader();
						}else if(response.data.status == "error")
						{
							$rootScope.stopLoader();
							growl.error("Error with getting balance.",{
							title : ERROR_MSG
						});
					}
				});
				
		
		
	}
	// Clear Doctor Selection
	$scope.clearDoctor = function(item,$index){
		// console.log(item);
		// return false;
		item=$scope.autoRenderedServicesFinalList[$index];
		var data = {
		    "visitTypeId" : 1,
		    "billingBedCategoryId" : $scope.opdBillingCatId,
		    "patientTypeId" : $scope.patientDetails.patientTypeId,
		    "paymentEntitlementTypeId" :  $scope.encounterDetails.paymentEntitlementTypeId,
		    "referralId" : $scope.encounterDetails.referralId,
		    listOrderDetailsMasterDto : []
		};
		var newArr = {
				ordDiscount: item.discount !== undefined && !isNaN(item.discount) ? item.discount : "0",
				ordConcession : item.concession !== undefined && !isNaN(item.concession) ? item.concession : "0",
				ordDocId : null,
				ordDocSplId : null,
				encounterId : $scope.encounterDetails.encounterId,
				orderDetailsId : item.orderDetailsId,
				orgId : $scope.organizationId,
				orgUnitId : $scope.unitId,
				serviceId : item.serviceMasterId,
				departmentId : item.serviceSpecialityId,
				ordTotalAmt : item.ordTotalAmount,
				tariffId : item.tariffId,
				isDocReq : item.isDoctorRequired,
				isRefDoctorShare:item.isRefDoctorShare ,
				discountOwnership: parseInt(item.discountOwnership),
				orderDate : item.orderDate,
				
		};
		var orderDetailsList = [];
		orderDetailsList.push(newArr);
		data.listOrderDetailsMasterDto = orderDetailsList;
		
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/order/docShare/clearDoctor";
    	if(item.doctorId>0)
    		{
    		$rootScope.startLoader();
    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    		if(response.data.status == "success"){
    			item.doctorSpecialityId=null;
    			item.doctorId=null;
    			 $scope.calculateShareAmount(item,$index);
    			growl.success("Doctor cancelled successfully");
    		}else{
    			$rootScope.stopLoader();
    		}
    	});
    		}
	}

    $scope.clearDoctorBtn= function(item,$index){	
    	
    	if(item.doctorId>0)
			{
		        
				item.doctorSpecialityId=null;
				item.doctorId=null;
				growl.success("Doctor cancelled successfully");
		
		
			}
    
    }

    $scope.clearDoctorService=function(item,$index){
    	
    	if(item.doctorId>0)
		{
	        
			item.doctorSpecialityId=null;
			item.doctorId=null;
			growl.success("Doctor cancelled successfully");
	
	
		}
    	
    }

// Share Development start

	$scope.calculateShareAmount = function(item,$index){
		// console.log(item);
		// return false;
		var data = {
		    "visitTypeId" : 1,
		    "billingBedCategoryId" : $scope.opdBillingCatId,
		    "patientTypeId" : $scope.patientDetails.patientTypeId,
		    "paymentEntitlementTypeId" :  $scope.encounterDetails.paymentEntitlementTypeId,
		    "referralId" : $scope.encounterDetails.referralId,
		    listOrderDetailsMasterDto : []
		};
		var newArr = {
				ordDiscount:item.discount !== undefined && !isNaN(item.discount) ? item.discount : "0",
				ordConcession : item.concession !== undefined && !isNaN(item.concession) ? item.concession : "0",
				ordDocId : item.doctorId,
				ordDocSplId : item.doctorSpecialityId,
				encounterId : $scope.encounterDetails.encounterId,
				orderDetailsId : item.orderDetailsId,
				orgId : $scope.organizationId,
				orgUnitId : $scope.unitId,
				serviceId : item.serviceMasterId,
				departmentId : item.serviceSpecialityId,
				ordTotalAmt : item.ordTotalAmount,
				tariffId : item.tariffId,
				isDocReq : item.isDoctorRequired,
				isRefDoctorShare:item.isRefDoctorShare ,
				discountOwnership: parseInt(item.discountOwnership),
				orderDate : item.orderDate,
				
		};
		var orderDetailsList = [];
		orderDetailsList.push(newArr);
		data.listOrderDetailsMasterDto = orderDetailsList;
		
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/billing/order/docShare";
    	
    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    		if(response.data.status == "success"){
    			$scope.autoRenderedServicesFinalList[$index].docSharePer = response.data.object.listOrderDetailsMasterDto[0].docSharePer!=null ? parseFloat(response.data.object.listOrderDetailsMasterDto[0].docSharePer).toFixed(2) : 0;
    			$scope.autoRenderedServicesFinalList[$index].hospitalShare = response.data.object.listOrderDetailsMasterDto[0].hospitalShare!=null ?parseFloat(response.data.object.listOrderDetailsMasterDto[0].hospitalShare).toFixed(2):0;
    			$scope.autoRenderedServicesFinalList[$index].hospitalShareNew=parseFloat($scope.autoRenderedServicesFinalList[$index].hospitalShare).toFixed(2);
    			$scope.autoRenderedServicesFinalList[$index].refDocSharePer = response.data.object.listOrderDetailsMasterDto[0].refDocSharePer!=null ? parseFloat(response.data.object.listOrderDetailsMasterDto[0].refDocSharePer).toFixed(2) : 0;
    			$scope.autoRenderedServicesFinalList[$index].discountOwnership = (response.data.object.listOrderDetailsMasterDto[0].discountOwnership>0 && ($scope.autoRenderedServicesFinalList[$index].concession != 0 || $scope.autoRenderedServicesFinalList[$index].discount != 0)) > 0 ? response.data.object.listOrderDetailsMasterDto[0].discountOwnership : $scope.defaultDiscountOwnership;
    			// $rootScope.stopLoader();
    		}else{
    			$rootScope.stopLoader();
    		}
    	});
	}
	
// SHare Development end
	
	
	$scope.resetToActualValuesAfterClosePopup = function()
	{
		$scope.authsummarySplitBill = angular.copy($scope.authsummary);
		$scope.serviceList = angular.copy($scope.serviceListOLD);
		$scope.isSplitBillFlag =false;
		$scope.isSplitBillApplied = false;
		$scope.autoRenderedServicesFinalList = $scope.serviceListOLD;
	}
	
	$scope.resetvalue = false;
	$scope.resetSplitBill = function()
	{
		
		$scope.authsummarySplitBill = angular.copy($scope.authsummary);
		$scope.serviceList = angular.copy($scope.serviceListOLD);
		$scope.resetvalue = true;
		$scope.calculateSplitBillTotal($scope.serviceList);
		$scope.isSplitBillFlag =false;
		$scope.isSplitBillApplied = false;
	}
	$scope.saveSplitBill = function()
	{
		$scope.autoRenderedServicesFinalList = $scope.serviceList;
		$scope.saveFromSplitBill = true;
		// console.log($scope.serviceList);
		// return false;
		var res = $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
		if(res == 1){
			$scope.saveBillngNew();
		}
		// $scope.isSplitBillFlag =true;
		// $scope.isSplitBillApplied = true;
	}
	
	$scope.changeSelfAmount =function(item,idx)
	{
		
		if(item.netPay>0)
			{
			   if(parseFloat(item.selfPayable)>item.netPay)
				   {
				   $scope.autoRenderedServicesFinalList[idx].creditPayable = $scope.autoRenderedServicesFinalList[idx].oldCreditPayable;
				   $scope.autoRenderedServicesFinalList[idx].selfPayable =$scope.autoRenderedServicesFinalList[idx].oldSelfPayable;
				   }
			   else{
				   $scope.serviceViseSplit = true;
				   $scope.autoRenderedServicesFinalList[idx].creditPayable = (parseFloat(item.netPay) - parseFloat(item.selfPayable)).toFixed(2);
				   $scope.autoRenderedServicesFinalList[idx].selfPayable = parseFloat(item.selfPayable).toFixed(2);
			      }
			   
			}
		  $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
		  // console.log("Self",$scope.autoRenderedServicesFinalList);
	}
	
	
	
$scope.changeCreditAmount =function(item,idx){
	if(item.netPay>0)
	{
	   if(parseFloat(item.creditPayable)>item.netPay)
		   {
		   $scope.autoRenderedServicesFinalList[idx].creditPayable = $scope.autoRenderedServicesFinalList[idx].oldCreditPayable;
		   $scope.autoRenderedServicesFinalList[idx].selfPayable =$scope.autoRenderedServicesFinalList[idx].oldSelfPayable;
		   }
	   else{
		   $scope.serviceViseSplit = true;
		   $scope.autoRenderedServicesFinalList[idx].selfPayable =(parseFloat(item.netPay) - parseFloat(item.creditPayable)).toFixed(2);
		   $scope.autoRenderedServicesFinalList[idx].creditPayable = parseFloat(item.creditPayable).toFixed(2);
		   
	      }
	}
  $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
  // console.log("credit",$scope.autoRenderedServicesFinalList);
}
	
	/** *=========================Payer tab=========================** */
	
	
			
		$scope.getPatientClassService = function()
		{
				// alert($scope.isPatientClass);
			
				if($scope.isPatientClass == 'Y' && $scope.encounterDetails.patientClassId > 0 )
					{
							var data = {
									organizationId:$scope.organizationId,
									unitId : $scope.unitId,
									patientCategoryId : $scope.patientDetails.patientTypeId,
									patientClassId : $scope.encounterDetails.patientClassId,
									tariffId : $scope.defaultSelfTariffId,
									serviceIds : [],
									saleIds : [],
									encounterId : $scope.encounterDetails.encounterId,
									visitTypeId : 1,
									pharmacyServiceId:$scope.pharmacyServiceId,
									paymentEntitlementTypeId:$scope.encounterDetails.paymentEntitlementTypeId
							}
							
					angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
						  data.serviceIds.push(value.serviceMasterId);
						    if(value.saleId!=null)
						    data.saleIds.push(value.saleId);
						});
					    $rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/billing/surcharge/service/details";
				    	
				    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				    		$rootScope.stopLoader();
				    		if(response.data.status == "success"){
				    			
				    			var newArr = {};
				    			newArr.priorityId  = '2';
				    			newArr.isOutsourced ='N';
				    			newArr.specialityId = null;
				    			newArr.serviceSpecialityId =response.data.listObject[0].specialityId;
				    			newArr.serviceSubSpecialityId = response.data.listObject[0].subSpecialityId;
				    			newArr.doctorId = null;
				    			newArr.doctorSpecialityId = null;
    			    			newArr.serviceRendered = 0;
				    			newArr.serviceRenderingDeptId = null;
				    			newArr.serviceMasterId = response.data.listObject[0].serviceMasterId;
				    			newArr.serviceStandardCode = response.data.listObject[0].serviceStandardCode;
				    			newArr.serviceStandardName = response.data.listObject[0].serviceStandardName;
				    			newArr.isDiscount = "N";
				    			newArr.isDiscountApplicable = 'N';
				    			newArr.isQuantityEditable ='N';
				    			newArr.isRateEditable = 'N';
				    			newArr.minRateEditable = response.data.listObject[0].minRateEditable;
				    			newArr.maxRateEditable = response.data.listObject[0].maxRateEditable;
				    			newArr.specialityName = response.data.listObject[0].specialityName;
				    			newArr.doctorName ='';
				    			newArr.quantity = 1;
				    			newArr.discountDisabledFlag = 'N';
				    			newArr.concession = parseFloat(0);
				    			newArr.discountPercentage = parseFloat(0);
				    			newArr.discount = parseFloat(0);
				    			newArr.taxId = response.data.listObject[0].taxId;
				    			newArr.taxName = response.data.listObject[0].taxName;
				    			newArr.taxPercentage = response.data.listObject[0].taxPercentage;
				    			newArr.discountDetailsList = [];
				    			newArr.creditPayable =parseFloat(0);
				    			newArr.creditPayableOldC = parseFloat(0);
				    			newArr.oldNetPay = "0";
				    			newArr.createdBy  = $scope.userId;
				    			newArr.createdDate = new Date().getTime();
				    			newArr.isPackageService = "N";
				    			newArr.isCapService = "N";
				    			newArr.subSpecialityId = response.data.listObject[0].subSpecialityId;
				    			newArr.ordCancelled = "N";
				    			newArr.status = "A";
				    			/*
								 * newArr.ordCancelReasonId = '';
								 * newArr.ordCancelDatetime = new
								 * Date().getTime(); newArr.ordCancelRemark =
								 * '';
								 */
				    			newArr.payeeId  = $scope.selfPayeeId;
				    			newArr.coShare =0;
				    			newArr.coPayPer =0;
				    			newArr.contractId = 0;
				    			newArr.isRefDoctorShare = "N";
				    			var patientClassTotal = parseFloat(0);
				    			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
				    			{
				    				var stopFlag = true;
				    				angular.forEach(value.orderDetailsPackageMapperDtosList, function (pkgValue, key) 
							    			{
						    					if(pkgValue.isPackageService=='N')
				    					    	   {
								    				   angular.forEach(response.data.listObject[0].discountDetailsList, function (value1, key1) {
								    					if(value.serviceMasterId == value1.serviceId && stopFlag == true)
								    					{
								    						stopFlag = false;
								    					    var totalAmt = (parseFloat(value.quantity) * parseFloat(pkgValue.serviceAmount)).toFixed(2);
								    					    var calDicAmt = (parseFloat(totalAmt)*value1.discountPercentage/100).toFixed(2);	
								    					    patientClassTotal = parseFloat(patientClassTotal) + parseFloat(calDicAmt);
								    					 }
								    				  });
				    					    	   }
							                   });
				    			  });
				    			newArr.basePrice = patientClassTotal;
				    			newArr.basePriceOld = patientClassTotal;
				    			newArr.selfPayable = patientClassTotal;
				    		    newArr.selfPayableOldC = patientClassTotal;
				    		    newArr.totalAmt =  patientClassTotal;
				    		    newArr.netPay  = patientClassTotal;
				    		    newArr.packageConsumptionAmt  = parseFloat(0);
				    		    newArr.concession = parseFloat(0);
								newArr.patientClassPercentage = parseFloat(0);
								newArr.medallServiceId = response.data.listObject[0].medallServiceId != null ? response.data.listObject[0].medallServiceId : '';
	                            newArr.pharmacyDiscountOverride = value.pharmacyDiscountOverride;

				    			var dataMappr = [{
			    					contractId: null,
			    					creditPayable: 0,
			    					organizationId: $scope.organizationId,
			    					payeeAmount: patientClassTotal,
			    					payeeId:$scope.selfPayeeId,
			    					selfPayable: patientClassTotal,
			    					serviceId: response.data.listObject[0].serviceMasterId,
			    					status: 'A',
			    					unitId: $scope.unitId
			    			}];
			    			// ganesh
			    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
			    			
			    			// for Package
			    			var packageMppr = [{"id":"",
			    				"orderDetailsId":"",
			    				"serviceId":response.data.listObject[0].serviceMasterId,
			    				"packageId":"",
			    				"payeeId":"",
			    				"serviceAmount":patientClassTotal,	
			    				"packageAmount":0,
			    				"packageCode":"",
			    			    "packageName":"",
			    				"isPackageService":"N",
			    				"isCapService":"N",
			    				"saleDetailsId":"",
			    				"itemId":"",
			    				"status":"A",
			    				"unitId":$scope.unitId,
			    				"organizationId": $scope.organizationId}];
			    			
			    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
			    			
			    			
				    		    if(parseFloat(patientClassTotal) > 0 ){
				    		      $scope.autoRenderedServicesFinalList.push(newArr);
				    		      $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
				    		    }
				    		    
				    		    
				    		}
				    	});
					}
				else
					{
						var temp = {
								serviceMasterId : $scope.surchargeServiceId
						}
						
						var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
						
						
						    if (indexT >= 0) {
						    	if($scope.autoRenderedServicesFinalList[indexT].orderDetailsId > 0){
						    		
						    		var data = {
						    				orderDetailsId : $scope.autoRenderedServicesFinalList[indexT].orderDetailsId,
						    				ordCancelReasonId : null,
						    				userId : $scope.userId
						    		}
						    		
						    		var URI = BASE_URL + ROOT_URL + "/api/billing/bill/service/cancel";
							    	
							    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							    		if(response.data.status == "success"){
							    			$scope.autoRenderedServicesFinalList.splice(indexT, 1);
									    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
											$rootScope.stopLoader();
											$scope.saveOrderPackage();

                                             growl.success(response.data.message,
                                             {
                                                 title : 'success'
                                             });

							    		}else{
							    			$rootScope.stopLoader();
                                             growl.error(response.data.message,
                                             {
                                                 title : 'error'
                                             });
							    		}
							    	});
						    	
						    }else{
						    	$scope.autoRenderedServicesFinalList.splice(indexT, 1);
						    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
								$rootScope.stopLoader();
						    }
					}
				
					}
	    	
		}

    
	 /** ***** START TAB FOR Payment Mode ****** */
	
	
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.firstTabLevel = newTab;
		if(newTab==3)
		{
			$scope.getAuthrizationDetails();
		}
		if(newTab==1)
		{
			/*
			 * $scope.getAutoRenderedServicesByPatient({ "visitTypeId":1,
			 * "encounterId":$scope.encounterDetails.encounterId });
			 */
		}
		/*
		 * if(newTab==5) { if($scope.autoRenderedServicesFinalList != null){
		 * $scope.ServiceCount = $scope.autoRenderedServicesFinalList.length; } }
		 */
	};
	
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	 /** ***** END TAB FOR Payment Mode ****** */
	
	/** Yogesh Start * */
	
	$scope.initPackageSearchObj = function(){
		$scope.packageSearchObj = {packageTypeId : "0", serviceCode:"",serviceName:""}
	}
	
	$scope.getPackageTypeList = function(orgId,unitId) {
		var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
		BillingGenericService.serviceAction("GET", URI, null).then(function(response){
			$scope.packageTypeList = response.data.listObject;
		});
	}
	
	
	$scope.searchPackages = function(packageSearchKeyword)
	{
		    var packageSearchObj = {};
			packageSearchObj.organisationId = $scope.organizationId;
			packageSearchObj.unitId = $scope.unitId;
			packageSearchObj.visitTypeId = 1;// default op
			packageSearchObj.bedBillingCategoryId = $scope.opdBillingCatId;// default
																			// op
			packageSearchObj.genderId = $scope.patientDetails.genderId;
			packageSearchObj.paymentEntitlementId = $scope.encounterDetails.paymentEntitlementTypeId;
			packageSearchObj.patientTypeId = $scope.patientDetails.patientTypeId;
			packageSearchObj.serviceCode = packageSearchKeyword;
			packageSearchObj.serviceName = packageSearchKeyword;
			return servicesFactory.searchPackageForBilling(packageSearchObj,$scope);
	}
	
	
	
	$scope.selectPackageService = function($item, $model, $label){
		
		var checkFlag = false;
		angular.forEach($scope.listPackageOrder,function(value,index){
			
			if($item.packageMasterId == value.packageId && value.isForceClose == 1){
				checkFlag = true;
				
			}
			
		});
		
		if(checkFlag){
			growl.warning("Mentioned package already added.",{
					title : WARNING_MSG
				});
		}
		
		if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1"){
			var birthDateArr=$scope.patientDetails.birthDate.split("-");
			var age=0;
			if(birthDateArr.length>1){
				 age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);
			}else{
				  birthDateArr=$scope.patientDetails.birthDate.split("/");
				 age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);
			}			
			
			if(!($item.minAge <= age && $item.maxAge >= age)){
				$scope.packageSearchObj.serviceName='';
				growl.warning("Package is not yet  configure for this age.",{
					title : WARNING_MSG
				});
				return;
			}
			
		}
		
		$scope.packageResultObj =  $item;
		$scope.forServicePayeeId  = $scope.selfPayeeId;
		
		if(parseInt($scope.contractId) == 0)
		{
			$scope.contractId = null;
		}
		$scope.packageResultObj.encounterId = $scope.encounterDetails.encounterId;
		$scope.packageResultObj.contractId = $scope.contractId;
		$scope.packageResultObj.visitTypeId = 1;
		$scope.packageResultObj.billingBedCategoryId = $scope.opdBillingCatId;
		$scope.packageResultObj.patientCategoryId = $scope.patientDetails.patientTypeId;
		$scope.packageResultObj.defaultSelfTariffId = $scope.defaultSelfTariffId;
	    $scope.packageResultObj.selfPayeeId = $scope.selfPayeeId;
	    $scope.packageResultObj.organizationId = $scope.organizationId;
	    $scope.packageResultObj.unitId = $scope.unitId;
	    $scope.packageResultObj.patientClassId = $scope.encounterDetails.patientClassId;
	    $scope.packageResultObj.updateCap = 'N';
	    var packageIds = [];
		angular.forEach($scope.selectedPackageList,function(value,index){
			packageIds.push(value.packageMasterId);
		});
		if(packageIds.indexOf($item.packageMasterId) == -1)
		{
			// return false;
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/global/consumption/package";
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageResultObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status=="success"){
					// console.log("res",response);
					// var basePriceObj=response.data.object;
					if(response.data.object != "undefined" && response.data.object != null)
						{
						$scope.forServicePayeeId = response.data.object.payeeId;
						$item.basePrice=response.data.object.serviceRate;
						$item.quantity=1;
						$item.coPayPer = response.data.object.coShare==null?0:response.data.object.coShare;
						$item.selfPayable = response.data.object.selfPayable;
						$item.creditPayable = response.data.object.creaditPayable;
						$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
						$item.contractId = response.data.object.contractId;
						$item.patientClassPercentage = isNaN(parseFloat(response.data.object.patientClassPercentage)) ? parseFloat(0).toFixed(2) : (parseFloat(response.data.object.patientClassPercentage));
						$item.markupStatus = response.data.object.markupStatus;
						$item.minRateEditable = response.data.object.minRateEditable;
						$item.maxRateEditable = response.data.object.maxRateEditable;
						$item.netPay = response.data.object.finalPrice;
						$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
						$item.bedCategoryId=response.data.object.billingBedCategoryId;
						$item.isPackage = true;
						$item.packAppDate=new Date();
						// for package
						$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
						
						$item.payeeId=response.data.object.payeeId;
						
						$scope.preRequisiteDetails = [];
						var packageServiceList = [];
						if((typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1")||(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "5")){
		    	    		var packageIds = [];
		    	    		packageIds.push($item.packageMasterId);
		    	    		// get ehc package services
		    	    		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
		    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
		    					if(response.data.status == "success")
		    					{
		    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
		    						$scope.preRequisiteDetails = [];
		    						angular.forEach(ehcPackageServiceList,function(val,i){
		    							var newArr = {};
		    			    			newArr.priorityId  = '2';
		    			    			newArr.isOutsourced ='N';
		    			    			newArr.specialityId = val.specialityId;
		    			    			newArr.serviceSpecialityId = val.specialityId;
		    			    			newArr.serviceSubSpecialityId = val.subSpecialityId;
		    			    			/*
										 * if($scope.isPreOrPostBilling == 'N') {
										 * newArr.serviceRendered = 1; } else {
										 * newArr.serviceRendered = "0"; }
										 */
		    			    			newArr.serviceRendered = "0";
		    			    			newArr.serviceMasterId = val.serviceId;
		    			    			newArr.serviceStandardCode = val.serviceCode;
		    			    			newArr.serviceStandardName = val.serviceName;
		    			    			newArr.isDoctorRequired = val.isDocReq;
		    			    			newArr.isRefDoctorShare = val.isRefDoctorShare;
		    			    			newArr.isDiscountApplicable = "N";
		    			    			newArr.isDiscount = "N";
		    			    			newArr.isQuantityEditable = "N";
		    			    			newArr.isRateEditable = "N";
		    			    			newArr.minRateEditable = 0;
		    			    			newArr.maxRateEditable = 0;
		    			    			newArr.specialityName = val.specialityName;
		    			    			newArr.quantity = 1;
		    			    			newArr.basePrice =  val.servicePrice;
		    			    			newArr.serviceAmt =  val.servicePrice !== undefined && !isNaN(val.servicePrice) ? val.servicePrice : "0";
		    			    			newArr.totalAmt = val.servicePrice;
		    			    			newArr.netPay = 0;
		    			    			newArr.packageConsumptionAmt = val.servicePrice;
		    			    			newArr.basePriceOld = 0;
		    			    			newArr.discountDisabledFlag = 'N';
		    			    			newArr.concession = 0;
		    			    			newArr.patientClassPercentage = parseFloat(0);
		    			    			newArr.discountPercentage = 0;
		    			    			newArr.discount = 0;
		    			    			newArr.coPayPer = 0;
		    			    			var dis =0;
		    			    			var disPer =0;
		    			    			newArr.taxPercentage = 0;
		    			    			newArr.discountDetailsList = [];
		    			    			newArr.selfPayable =  "0";
		    			    			newArr.creditPayable =   "0";
		    			    			newArr.oldNetPay = "0";
		    			    			newArr.createdBy  = $scope.userId;
		    			    			newArr.createdDate = new Date().getTime();
		    			    			newArr.packageMasterId = $item.packageMasterId;
		    			    			newArr.packageTypeId = $item.packageTypeId;
		    			    			newArr.packageId = $item.packageMasterId;
		    			    			newArr.isPackageService = 'Y';
		    			    			newArr.apportionedPrice = val.apportionedPrice;
		    			    			
		    			    			newArr.subSpecialityId = val.subSpecialityId; 
		    			    			newArr.ordCancelled = 'N';
		    			    			newArr.isDocReq=val.isDocReq;
		    			    			
		    			    			
		    			    			if(val.isDocReq == 'Y')
		    			    				{
		    			    				 newArr.doctorId = null;
		    			    	    		 newArr.doctorSpecialityId = null;
		    			    				}
		    			    			var dataMappr = [{
		    			    					contractId: null,
		    			    					creditPayable: 0,
		    			    					organizationId: $scope.organizationId,
		    			    					payeeAmount: 0,
		    			    					payeeId : $scope.selfPayeeId,
		    			    					selfPayable: 0,
		    			    					serviceId: val.serviceId,
		    			    					status: 'A',
		    			    					unitId: $scope.unitId
		    			    			 }];
		    			    			if(val.preRequisite == 'Y')
	    			    				{
	    			    				  $scope.preRequisiteDetails.push(val); 
	    			    				}
		    			    			if(val.consultationSpecialityId>0)
		    			    			{
		    			    				newArr.docSpecialityList=$scope.specialityMasterList;
		    			    				newArr.doctorSpecialityId=val.consultationSpecialityId;
		    			    				newArr.docList=$scope.getDoctorBySpecialityList(val.consultationSpecialityId);
		    			    			}
		    			    			else{
		    			    				newArr.docList=$scope.doctorsList;
		    			    			}
		    			    			
		    			    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
		    			    			newArr.isServiceSelected="N";
		    			    			// for Package
		    			    			var packageMppr = [{"id":"",
		    			    				"orderDetailsId":"",
		    			    				"serviceId":val.serviceId,
		    			    				"packageId":$item.packageMasterId,
		    			    				"payeeId":"",
		    			    				"serviceAmount":val.servicePrice,	
		    			    				"packageAmount":val.servicePrice,
		    			    				"packageCode":$item.serviceStandardCode,
		    			    			    "packageName":$item.serviceStandardName,
		    			    				"isPackageService":"Y",
		    			    				"isCapService":"N",
		    			    				"saleDetailsId":"",
		    			    				"itemId":"",
		    			    				"status":"A",
		    			    				"unitId":$scope.unitId,
		    			    				"organizationId": $scope.organizationId}];
		    			    			
		    			    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
		    			    			  newArr.bedCategoryId = $item.bedCategoryId,
		    			    			  packageServiceList.push(newArr);
		    						})
		    						
		    						$item.packageServiceList = packageServiceList;
				    				$scope.selectedPackageList.push($item);
				    				$scope.SelectedPackageListToAddPackageServices=$scope.selectedPackageList;
		    					}
		    				});
		    				$timeout(function () {
		    				if($scope.preRequisiteDetails != null && $scope.preRequisiteDetails.length > 0){
		    					angular.element('#preRequisitePopupTabular').modal('show');
		    		    		$timeout(function () {
		    		    			angular.element('#preRequisitePopupTabular').modal('hide');
		    		   		    }, 10000);
		    				}
		    				}, 2000);
		    	    	}
						else
							{
								$scope.selectedPackageList.push($item);
							}
					    	$scope.initPackageSearchObj();
						}
					else
						{
						growl.warning("Please add base price of this service and then add..!!",{
							title : ERROR_MSG
						});
						return false
						}
					// concession
					// console.log("FINAL ITEM",$item);
				}
			});
			
			
		}else{
			growl.error("Package Already Added",{
				title : ERROR_MSG
			});
		}
	}
	
	
	$scope.removeSelectedPackage = function(index){
		$scope.selectedPackageList.splice(index,1);
	}
	
	
	
	$scope.addOrUpdateServices = function(index,indexList)
	{
		var finalIndexList = [];
		var containsPharmacyService = false;
		if(index!=undefined)
			finalIndexList.push(index);
		
		if(indexList!=undefined)
			finalIndexList = indexList;
		$rootScope.startLoaderPopup();
		$scope.isPatientClass = 'N';
		var newOrder =0;
		var data={
				patientId :$scope.encounterDetails.patientId,
				encounterId :$scope.encounterDetails.encounterId,
				createdBy :$scope.userId,
				createdDate :new Date().getTime(),
				updatedBy :$scope.userId,
				updatedDate :new Date().getTime(),
				orderDate :new Date().getTime(),
				organisationId :$scope.organizationId,
				orgId :$scope.organizationId,
				unitId :$scope.unitId,
				isFinalBill : $scope.finalBill,
				ordDoctorId :$scope.encounterDetails.doctorId,
				visitTypeId : '1',
				billDateTime : new Date().getTime(),
				netAmount : $scope.netPayableSummary !== undefined && !isNaN($scope.netPayableSummary) ? parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2) : "0",
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				isFinancialClearance : 'N',
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : $scope.removeDiscountList,
				tariffId : $scope.defaultSelfTariffId,
				paymentEntitlementId : $scope.encounterDetails.paymentEntitlementTypeId,
				visitTypeId : $scope.visitTypeId,
				patientTypeId : $scope.patientDetails.patientTypeId,
				billingBedCategoryId : $scope.opdBillingCatId,// default op
				isInterimBill :'N',
				isDrNote:"N",
				contractId:$scope.contractId,
				counterId:$rootScope.counterId,
				encounterSpecialityId:$scope.encounterDetails.specialityId,
				lineLevelDiscountOrderList : [],
				patientClassId : $scope.encounterDetails.patientClassId,
				surchargeServiceId : ($scope.surchargeServiceId != null && $scope.surchargeServiceId > 0?$scope.surchargeServiceId : null),
				pharmacyServiceId:$scope.pharmacyServiceId,
				referralId : $scope.encounterDetails.referralId,
				selfPayeeId : $scope.selfPayeeId,
				homeCollection : $scope.homeCollection=='Y' ? true : false,
				applyAuth : $scope.applyAuth!=null ? $scope.applyAuth : false,
				needToUpdatePharmacyService : $scope.needToUpdatePharmacyService!=null ? $scope.needToUpdatePharmacyService : false,
				updatePayee : $scope.updatePayee!=null ? $scope.updatePayee : false,
				isPreBilling :$scope.isPreOrPostBilling,
				capExist : $scope.capExist,
				packageExist : ($scope.listPackageOrder.length>0)? true : false
		   };
		
		var billPayeeList =[];
		if($scope.totalSelfPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.selfPayeeId,
						billAmount : $scope.totalSelfPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		if($scope.totalCreditPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.encounterDetails.payeeId,
						billAmount : $scope.totalCreditPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		
		data.billPayeeDetailsDtosList = billPayeeList;
		var saveOrderDetailsObj = [];
		angular.forEach($scope.autoRenderedServicesForSaveAndUpdate, function (value, key) {
			
			if(value.serviceMasterId==$scope.pharmacyServiceId)
				containsPharmacyService = true;
			
			var saveArr = {};
			if(value.orderDetailsId!=null && value.orderDetailsId > 0)
			{
				newOrder ++;
				saveArr.orderDetailsId = value.orderDetailsId;
			}
			saveArr.discountOwnership = value.discountOwnership;
			saveArr.isFromServiceSerach = value.isFromServiceSerach;
			saveArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
			if(value.isOrderVisibleDepartment == undefined || value.isOrderVisibleDepartment == null || value.isOrderVisibleDepartment == ''){
				saveArr.isOrderVisibleDepartment = 'A';
               }
			saveArr.isConsultationService = value.isConsultationService;
			saveArr.ordRemarks = value.ordRemarks;
			saveArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
			saveArr.markupStatus = value.markupStatus;
			saveArr.patientClassPercentage = value.patientClassPercentage;
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			// saveArr.orderDate = new Date().getTime();
			saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.payeeId = value.payeeId;
			if(value.tariffId != undefined && value.tariffId != null){
				saveArr.tariffId = value.tariffId;
			}else{
				saveArr.tariffId = $scope.defaultSelfTariffId;
			}
			
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			
			if($scope.surchargeServiceId == value.serviceMasterId){
				saveArr.status = 'I';
			}
			else{
				saveArr.status = value.status;
			}
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = value.createdBy;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay = value.oldNetPay !== undefined && !isNaN(value.oldNetPay) ? parseFloat(value.oldNetPay).toFixed(2) : "0";
			saveArr.ordCancelled = value.ordCancelled;
			saveArr.ordCancelReasonId  = value.ordCancelReasonId;
			saveArr.ordCancelDatetime  = value.ordCancelDatetime;
			saveArr.ordCancelRemark  = value.ordCancelRemark;
			saveArr.oldOrdDtlId = value.oldOrdDtlId;
			saveArr.serviceAmt = value.basePrice !== undefined && !isNaN(value.basePrice) ? value.basePrice : "0";
			saveArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ? parseFloat(value.coPayPer).toFixed(2) : "0";
			saveArr.ordTotalAmt = parseFloat(saveArr.serviceAmt) * parseFloat(value.quantity);
			saveArr.taxPer  = value.taxPercentage !== undefined && !isNaN(value.taxPercentage) ? value.taxPercentage : "0";
			saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
			saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
			saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
			var withConDic =parseFloat(parseFloat(saveArr.ordTotalAmt) - parseFloat(value.concession) - parseFloat(value.discount)).toFixed(2);
			saveArr.taxAmount = withConDic !== undefined && !isNaN(withConDic) ? parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2) : "0";
			saveArr.netAmount =   withConDic !== undefined && !isNaN(withConDic) ? parseFloat(parseFloat(withConDic) +  parseFloat(saveArr.taxAmount)).toFixed(2) : "0";
			saveArr.dummyServiceAmt = value.dummyServiceAmt;
			saveArr.ordConcession = value.concession !== undefined && !isNaN(value.concession) ? value.concession : "0";
			saveArr.ordDiscount = value.discount !== undefined && !isNaN(value.discount) ? value.discount : "0";
			saveArr.ordDiscountPer = value.discountPercentage !== undefined && !isNaN(value.discountPercentage) ? value.discountPercentage : "0";
			saveArr.isFromAddservice = value.isFromAddservice;
			saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
			// saveArr.selfPayable = (saveArr.isPackage ||
			// value.packageTypeId==1 ) ? value.selfPayable : saveArr.netAmount;
			saveArr.selfPayable =   value.packageTypeId==1 &&  !saveArr.isPackage ?  0 : saveArr.netAmount;
			// saveArr.creditPayable = saveArr.isPackage?value.creditPayable:0;
			saveArr.creditPayable = 0;
			saveArr.oldCreditPayable = 0;
			saveArr.oldSelfPayable = 0;
			saveArr.taxId = value.taxId;
			saveArr.isInvestigationOrder = value.isInvestigationOrder;
            saveArr.isDiscountApplicable = value.isDiscountApplicable;
            saveArr.isDiscount = value.isDiscount;
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
			saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
			saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
			saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
			saveArr.isCapService = value.isCapService;
			saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
			saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
			saveArr.subDepartmentId = value.serviceSubSpecialityId;
			saveArr.isRefDoctorShare = value.isRefDoctorShare;
			saveArr.isDocReq =  value.isDocReq!=null &&  value.isDocReq!=undefined ? value.isDocReq :value.isDoctorRequired;
			saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.serviceRenderingDeptId = value.subSpecialityId; 
			saveArr.encounterId = $scope.encounterDetails.encounterId;
			saveArr.contractId = value.contractId;
			saveArr.bedCategoryId = value.bedCategoryId;
			saveArr.discountCategoryId = value.discountCategoryId;
			saveArr.discountDetailsList = [];
			saveArr.debitNoteService = false;
			saveArr.patientCouponTemplateMapperId = value.patientCouponTemplateMapperId != undefined ? value.patientCouponTemplateMapperId :null;
			  // package force add
			saveArr.forceAddRemoved = value.forceAddRemoved;
			saveArr.isConsumed = value.isConsumed;			
			saveArr.isLabService = $scope.labConfig.labSpecialityId==saveArr.departmentId ? 'Y' : 'N';
			if(value.discountDetailsList == null)
			{
			 value.discountDetailsList = [];
			}
		    saveArr.saleId = value.saleId;
			saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
			saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
			saveArr.freeTextOrderedServicesId = value.freeTextOrderedServicesId
			saveOrderDetailsObj.push(saveArr);
			    
			    if(saveArr.isPackage && saveArr.packageTypeId==1)
		    	{
			    	angular.forEach($scope.autoRenderedServicesFinalList, function (ehcPackageList, packKey) 
	    			{
			    		var ehcPackageListObj = {};
			    		angular.forEach(ehcPackageList.orderDetailsPackageMapperDtosList, function (pkgValue, key) 
		    			{
	    					if(value.packageId==pkgValue.packageId)
	    					{	
	    						ehcPackageListObj.orderDetailsId = ehcPackageList.orderDetailsId;
	    						
	    						ehcPackageListObj.ordTotalAmt = parseFloat(ehcPackageList.basePrice) * parseFloat(ehcPackageList.quantity);
	    						if(ehcPackageList.status == 'A' && ehcPackageList.isDiscount == 'Y')
	    						{
	    							ehcPackageListObj.patientClassPercentage = value.patientClassPercentage;
		    						ehcPackageListObj.ordDiscountPer = value.discountPercentage !== undefined && !isNaN(value.discountPercentage) ? value.discountPercentage : "0";
	    							ehcPackageListObj.ordConcession = value.patientClassPercentage != 0 ? parseFloat(ehcPackageListObj.ordTotalAmt * value.patientClassPercentage*0.01).toFixed(2) : 0;
	    							ehcPackageListObj.ordDiscount = value.discountPercentage != 0 ? parseFloat(((ehcPackageListObj.ordTotalAmt-ehcPackageListObj.ordConcession) * value.discountPercentage*0.01)).toFixed(2) : 0;
	    						}else{
	    							ehcPackageListObj.ordConcession = 0.00;
	    							ehcPackageListObj.ordDiscount =0.00;
	    							ehcPackageListObj.patientClassPercentage = 0.00;
		    						ehcPackageListObj.ordDiscountPer = 0.00;
	    						}
                                var totalPackageAmt = ehcPackageListObj.ordTotalAmt - (ehcPackageListObj.ordConcession + ehcPackageListObj.ordDiscount);
                                ehcPackageListObj.packageConsumptionAmt = totalPackageAmt;
                                pkgValue.packageAmount = totalPackageAmt;
	    						finalIndexList.push(packKey);
	    						
	    						ehcPackageListObj.discountOwnership = ehcPackageList.discountOwnership;
	    						ehcPackageListObj.isFromServiceSerach = ehcPackageList.isFromServiceSerach;
	    						ehcPackageListObj.isOrderVisibleDepartment = ehcPackageList.isOrderVisibleDepartment;
	    						ehcPackageListObj.isConsultationService = ehcPackageList.isConsultationService;
	    						ehcPackageListObj.ordRemarks = ehcPackageList.ordRemarks;
	    						ehcPackageListObj.nursingServiceOrderFlag =ehcPackageList.nursingServiceOrderFlag;
	    						ehcPackageListObj.markupStatus = ehcPackageList.markupStatus;
	    						ehcPackageListObj.orgId = $scope.organizationId;
	    						ehcPackageListObj.orgUnitId  = $scope.unitId;
	    						ehcPackageListObj.orderId = ehcPackageList.orderId;
	    						ehcPackageListObj.orderQty = ehcPackageList.quantity;
	    						ehcPackageListObj.serviceId = ehcPackageList.serviceMasterId;
	    						ehcPackageListObj.priorityId = ehcPackageList.priorityId;
	    						ehcPackageListObj.isOutsourced = ehcPackageList.isOutsourced;
	    						ehcPackageListObj.orderDate =   (ehcPackageList.orderDate !=null && ehcPackageList.orderDate!=undefined && ehcPackageList.orderDate!='')?ehcPackageList.orderDate:new Date().getTime();
	    						ehcPackageListObj.orderSchDate = new Date().getTime();
	    						ehcPackageListObj.payeeId = ehcPackageList.payeeId;
	    						ehcPackageListObj.tariffId = ehcPackageList.tariffId;
	    						ehcPackageListObj.billingClassId = '1';
	    						ehcPackageListObj.ordDocId = ehcPackageList.doctorId;
	    						ehcPackageListObj.serviceRendered = ehcPackageList.serviceRendered;
	    						ehcPackageListObj.serviceRenderingDeptId = ehcPackageList.serviceRenderingDeptId;
	    						ehcPackageListObj.ordRenderDatetime = new Date().getTime();
	    						ehcPackageListObj.serviceIsBilled = '0';
	    						if($scope.surchargeServiceId == ehcPackageList.serviceMasterId){
	    							ehcPackageListObj.status = 'I';
	    						}
	    						else{
	    							ehcPackageListObj.status = ehcPackageList.status;
	    						}
	    						ehcPackageListObj.updatedBy = $scope.userId;
	    						ehcPackageListObj.updatedDate =new Date().getTime();
	    						ehcPackageListObj.createdBy  = ehcPackageList.createdBy;
	    						ehcPackageListObj.createdDate  = ehcPackageList.createdDate;
	    						ehcPackageListObj.oldNetPay =   isNaN(ehcPackageList.oldNetPay )|| (ehcPackageList.oldNetPay<0 ) ? 0 : parseFloat(ehcPackageList.oldNetPay).toFixed(2);
	    						ehcPackageListObj.ordCancelled = ehcPackageList.ordCancelled;
	    						ehcPackageListObj.ordCancelReasonId  = ehcPackageList.ordCancelReasonId;
	    						ehcPackageListObj.ordCancelDatetime  = ehcPackageList.ordCancelDatetime;
	    						ehcPackageListObj.ordCancelRemark  = ehcPackageList.ordCancelRemark;
	    						ehcPackageListObj.oldOrdDtlId = ehcPackageList.oldOrdDtlId;
	    						ehcPackageListObj.serviceAmt =   ehcPackageList.basePrice !== undefined && !isNaN(ehcPackageList.basePrice) ? ehcPackageList.basePrice : "0";
	    						ehcPackageListObj.coPayPer = ehcPackageList.coPayPer !== undefined && !isNaN(ehcPackageList.coPayPer) ? parseFloat(ehcPackageList.coPayPer).toFixed(2) : "0";
	    						ehcPackageListObj.taxPer  = ehcPackageList.taxPercentage !== undefined && !isNaN(ehcPackageList.taxPercentage) ? ehcPackageList.taxPercentage : "0";
	    						ehcPackageListObj.procedureOrderMstId = ehcPackageList.procedureOrderMstId > 0 ? ehcPackageList.procedureOrderMstId  : null;
	    						ehcPackageListObj.isProcedureService = ehcPackageList.isProcedureService!=null ? ehcPackageList.isProcedureService  : 'N';
	    						ehcPackageListObj.isPostedWhileDischarge = ehcPackageList.isPostedWhileDischarge!=null ? ehcPackageList.isPostedWhileDischarge : 'N';
	    						ehcPackageListObj.taxAmount = 0.00;
	    						ehcPackageListObj.netAmount =   0.00;
	    						ehcPackageListObj.dummyServiceAmt = ehcPackageList.dummyServiceAmt;
	    						ehcPackageListObj.isFromAddservice = ehcPackageList.isFromAddservice;
	    						ehcPackageListObj.isPackage = typeof(ehcPackageList.isPackage) != "undefined" ? ehcPackageList.isPackage : false;
	    						// ehcPackageListObj.selfPayable = (ehcPackageListObj.isPackage ||
	    						// ehcPackageList.packageTypeId==1 ) ? ehcPackageList.selfPayable : ehcPackageListObj.netAmount;
	    						ehcPackageListObj.selfPayable =   ehcPackageList.packageTypeId==1 &&  !ehcPackageListObj.isPackage ?  0 : ehcPackageListObj.netAmount;
	    						// ehcPackageListObj.creditPayable = ehcPackageListObj.isPackage?ehcPackageList.creditPayable:0;
	    						ehcPackageListObj.creditPayable = 0;
	    						ehcPackageListObj.oldCreditPayable = 0;
	    						ehcPackageListObj.oldSelfPayable = 0;
	    						ehcPackageListObj.taxId = ehcPackageList.taxId;
	    						ehcPackageListObj.isInvestigationOrder = ehcPackageList.isInvestigationOrder;
	    			            ehcPackageListObj.isDiscountApplicable = ehcPackageList.isDiscountApplicable;
	    			            ehcPackageListObj.isDiscount = ehcPackageList.isDiscount;
	    						ehcPackageListObj.orderEmergencyFlag = 'N';
	    						ehcPackageListObj.isDrug = 'N';
	    						ehcPackageListObj.packageId = typeof(ehcPackageList.packageMasterId) != "undefined" && ehcPackageList.packageMasterId != null ? ehcPackageList.packageMasterId : ehcPackageList.packageId;
	    						ehcPackageListObj.packageTypeId = typeof(ehcPackageList.packageTypeId) != "undefined" ? ehcPackageList.packageTypeId : null;
	    						ehcPackageListObj.isPackageService = typeof(ehcPackageList.isPackageService) != "undefined" ? ehcPackageList.isPackageService : 'N';
	    						ehcPackageListObj.apportionedPrice = typeof(ehcPackageList.apportionedPrice) != "undefined" ? ehcPackageList.apportionedPrice : null;
	    						ehcPackageListObj.isCapService = ehcPackageList.isCapService;
	    						ehcPackageListObj.departmentId = ehcPackageList.serviceSpecialityId!=null? ehcPackageList.serviceSpecialityId:ehcPackageList.specialityId;
	    						ehcPackageListObj.subDepartmentId = ehcPackageList.serviceSubSpecialityId;
	    						ehcPackageListObj.isRefDoctorShare = ehcPackageList.isRefDoctorShare;
	    						ehcPackageListObj.isDocReq =  ehcPackageList.isDocReq!=null &&  ehcPackageList.isDocReq!=undefined ? ehcPackageList.isDocReq :ehcPackageList.isDoctorRequired;
	    						ehcPackageListObj.packageEitherOrGroupId = ehcPackageList.packageEitherOrGroupId;
	    						ehcPackageListObj.ordDocSplId = ehcPackageList.doctorSpecialityId;
	    						ehcPackageListObj.serviceRenderingDeptId = ehcPackageList.subSpecialityId; 
	    						ehcPackageListObj.encounterId = $scope.encounterDetails.encounterId;
	    						ehcPackageListObj.contractId = ehcPackageList.contractId;
	    						ehcPackageListObj.bedCategoryId = ehcPackageList.bedCategoryId;
	    						ehcPackageListObj.discountCategoryId = ehcPackageList.discountCategoryId;
	    						ehcPackageListObj.discountDetailsList = [];
	    						ehcPackageListObj.debitNoteService = false;
	    						  // package force add
	    						ehcPackageListObj.forceAddRemoved = ehcPackageList.forceAddRemoved;
	    						ehcPackageListObj.isConsumed = ehcPackageList.isConsumed;			
	    						ehcPackageListObj.isLabService = $scope.labConfig.labSpecialityId==ehcPackageListObj.departmentId ? 'Y' : 'N';
	    						if(ehcPackageList.discountDetailsList == null)
	    						{
	    						 ehcPackageList.discountDetailsList = [];
	    						}
	    					    ehcPackageListObj.saleId = ehcPackageList.saleId;
	    						ehcPackageListObj.orderDetailsPayeeMapperDtosList  = ehcPackageList.orderDetailsPayeeMapperDtosList;
	    						ehcPackageListObj.orderDetailsPackageMapperDtosList  = ehcPackageList.orderDetailsPackageMapperDtosList;
	    						
	    						
	    						saveOrderDetailsObj.push(ehcPackageListObj);
	    					}
		                 });
	    				 
	    			 });
		    	}
			    
			    
		    });
        var uniqueServiceArray=[];
        saveOrderDetailsObj.filter(function(item){
            if(item.orderDetailsId!=undefined && item.orderDetailsId!=null && item.orderDetailsId>0)
                {
              var i = uniqueServiceArray.findIndex(x => (x.orderDetailsId == item.orderDetailsId));
              if(i <= -1){
                  uniqueServiceArray.push(item);
              }
                }
            else
                {
                uniqueServiceArray.push(item);
                }
            });
        saveOrderDetailsObj=uniqueServiceArray;
        var uniqueArray = finalIndexList.filter(function(item, pos, self) {
            return self.indexOf(item) == pos;
        });
        
        uniqueArray.sort(function(a, b) {
            return a - b;
          });
        
        finalIndexList=uniqueArray;
           data.orderDetailsMasterDtosList = saveOrderDetailsObj;
           
           var URI = null;
           if($scope.forceAddOrRemove==false)
        	   URI = BASE_URL + ROOT_URL +'/api/billing/update/service';
           else
        	   URI = BASE_URL + ROOT_URL +'/api/billing/forceAddOrRemove';
			
           BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == 'success' && response.data.listObject.length > 0)
				{
					 if($scope.selectedServices!=undefined && $scope.selectedServices!=null && $scope.selectedServices.length>0)
				        {
					angular.forEach(response.data.listObject, function (value1, key) 
							{
						$scope.autoRenderedServicesFinalList.push(value1);
							});
				        }
					if(finalIndexList!=undefined && finalIndexList!=null)
					{
						$scope.reassignValuesAndReCalculate(response.data.listObject,finalIndexList);
					}else{
						$scope.getAutoRenderedServicesByPatient({
    						"visitTypeId":1,
    						"encounterId":$scope.encounterDetails.encounterId
						});
					}
					 $scope.isPatientClass = 'N';
                     $scope.needToUpdatePharmacyService = false;
                     $scope.applyAuth = false;
                     $scope.updatePayee = false;
                     $scope.autoRenderedServicesForSaveAndUpdate=[];
   				  	 $scope.setFirstTabLevel(1);
					 $rootScope.stopLoaderPopup();
					 if(index != null){
					 if( $scope.autoRenderedServicesFinalList[index].discount==undefined || $scope.autoRenderedServicesFinalList[index].discount==null || $scope.autoRenderedServicesFinalList[index].discount==0)
						 {
						 $scope.autoRenderedServicesFinalList[index].discountCategoryId=null;
						 }
					 }
					 if(containsPharmacyService){
							$scope.getMedicationIssueListByEncounterId();
                     }

                     growl.success(response.data.message,
                     {
                         title : 'success'
                     });
					 
				}
				else if(response.data.status == 'error')
				{
					$scope.getAutoRenderedServicesByPatient({
						"visitTypeId":1,
						"encounterId":$scope.encounterDetails.encounterId
					}); 
					
					$scope.isPatientClass = 'N';
					$rootScope.stopLoaderPopup();

                    growl.error(response.data.message,
                    {
                        title : 'error'
                    });

				}else{
					$rootScope.stopLoaderPopup();
				}
			});
	}
	
	
	$scope.reassignValuesAndReCalculate = function(list,finalIndexList)
	{
		var count = 0;
		angular.forEach(list, function (value, key) 
		{
	        var resArr=[];
	        if($scope.selectedServices!=undefined && $scope.selectedServices!=null && $scope.selectedServices.length>0)
	        {
	        $scope.autoRenderedServicesFinalList.filter(function(item){
	              var i = resArr.findIndex(x => (x.serviceMasterId == item.serviceMasterId));
	              if(i <= -1){
	                    resArr.push(item);
	              }
	            });
	        $scope.autoRenderedServicesFinalList=resArr;
	         }
			var index = finalIndexList[count];
			angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
				{
						value1.serviceAmount = parseFloat(value1.serviceAmount).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].orderDetailsId = value.orderDetailsId > 0 ? value.orderDetailsId : null;
						$scope.autoRenderedServicesFinalList[index].tariffId = value.tariffId > 0 ? value.tariffId : $scope.defaultSelfTariffId;
						$scope.autoRenderedServicesFinalList[index].isInvestigationOrder = (value.isInvestigationOrder=='Y')?'Y':'N';
						$scope.autoRenderedServicesFinalList[index].orderId = value.orderId > 0 ? value.orderId : null;
						$scope.autoRenderedServicesFinalList[index].orderDate = value.orderDate != null ? value.orderDate : '';
						$scope.autoRenderedServicesFinalList[index].orderNo = value.orderNo != null ? value.orderNo : '';
						$scope.autoRenderedServicesFinalList[index].medallServiceId = value.medallServiceId != null ? value.medallServiceId : '';
						$scope.autoRenderedServicesFinalList[index].bedCategoryId = value.bedCategoryId;
						$scope.autoRenderedServicesFinalList[index].isFromServiceSerach = value.isFromServiceSerach!= null ? value.isFromServiceSerach : 'N';
						if(value.isDoctorRequired == null || value.isDoctorRequired == undefined)
						{
						   $scope.autoRenderedServicesFinalList[index].isDoctorRequired  = 'N';
						}
						else
						{
						  $scope.autoRenderedServicesFinalList[index].isDoctorRequired  = value.isDoctorRequired;
						}
						$scope.autoRenderedServicesFinalList[index].docSharePer = value.docSharePer !=null ? parseFloat(value.docSharePer).toFixed(2) : 0;
						$scope.autoRenderedServicesFinalList[index].hospitalShare = value.hospitalShare !=null ? parseFloat(value.hospitalShare).toFixed(2) : 0;
						$scope.autoRenderedServicesFinalList[index].refDocSharePer = value.refDocSharePer !=null ? parseFloat(value.refDocSharePer).toFixed(2) : 0;
						$scope.autoRenderedServicesFinalList[index].discountOwnership = value.discountOwnership > 0 && (value.concession!=0 || value.discount!=0)? value.discountOwnership : $scope.defaultDiscountOwnership;
						$scope.autoRenderedServicesFinalList[index].isOrderVisibleDepartment = value.isOrderVisibleDepartment;
						$scope.autoRenderedServicesFinalList[index].isConsultationService = value.isConsultationService;
						$scope.autoRenderedServicesFinalList[index].ordRemarks = value.ordRemarks;
						$scope.autoRenderedServicesFinalList[index].nursingServiceOrderFlag =value.nursingServiceOrderFlag;
						$scope.autoRenderedServicesFinalList[index].dummyServiceAmt = value.dummyServiceAmt;
						$scope.autoRenderedServicesFinalList[index].priorityId  = value.priorityId;
						$scope.autoRenderedServicesFinalList[index].isOutsourced = value.isOutsourced;
					    $scope.autoRenderedServicesFinalList[index].specialityId = value.specialityId;
						$scope.autoRenderedServicesFinalList[index].taxId = value.taxId;
						$scope.autoRenderedServicesFinalList[index].ordCancelled = value.ordCancelled != null ? value.ordCancelled : 'N';
						$scope.autoRenderedServicesFinalList[index].ordCancelReasonId = value.ordCancelReasonId != null ? value.ordCancelReasonId  : '';
						$scope.autoRenderedServicesFinalList[index].ordCancelDatetime = value.ordCancelDatetime != null ? value.ordCancelDatetime  : '';
						$scope.autoRenderedServicesFinalList[index].ordCancelRemark = value.ordCancelRemark != null ? value.ordCancelRemark  : '';
						$scope.autoRenderedServicesFinalList[index].patientClassPercentage = value.patientClassPercentage != null ? value.patientClassPercentage  : null;
		        		$scope.autoRenderedServicesFinalList[index].markupStatus =value.markupStatus != null ? value.markupStatus  : false;
						$scope.autoRenderedServicesFinalList[index].status  = value.status;
						$scope.autoRenderedServicesFinalList[index].serviceRendered = value.serviceRendered != null ? value.serviceRendered  : '0';
						$scope.autoRenderedServicesFinalList[index].oldOrdDtlId = value.oldOrdDtlId != null ? value.oldOrdDtlId  : null;
						$scope.autoRenderedServicesFinalList[index].isRateEditable = (value.isRateEditable == 'Y')?'Y':'N';	
						$scope.autoRenderedServicesFinalList[index].isRateEditable = ((value.packageConsumptionAmt>0 && $scope.autoRenderedServicesFinalList[index].isRateEditable=='Y') || value.serviceMasterId==$scope.pharmacyServiceId )?'N':value.isRateEditable;	
						// $scope.autoRenderedServicesFinalList[index].isRateEditable
						// = value.isPackage ==
						// true?'N':$scope.autoRenderedServicesFinalList[index].isRateEditable;
						$scope.autoRenderedServicesFinalList[index].procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
						$scope.autoRenderedServicesFinalList[index].isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
						$scope.autoRenderedServicesFinalList[index].isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
						$scope.autoRenderedServicesFinalList[index].doctorSpecialityId=value.doctorSpecialityId; 
						// console.log("docSpe",value.doctorSpecialityId );
						// console.log("spe",value.specialityId);
						$scope.autoRenderedServicesFinalList[index].isRefDoctorShare = value.isRefDoctorShare != null ? value.isRefDoctorShare  : null;
						$scope.autoRenderedServicesFinalList[index].contractId = ((value.contractId != null || value.contractId > 0) ? value.contractId  : null);
						$scope.autoRenderedServicesFinalList[index].doctorId = value.doctorId==null?0:value.doctorId;
					    $scope.autoRenderedServicesFinalList[index].serviceSpecialityId = value.serviceSpecialityId;
						$scope.autoRenderedServicesFinalList[index].serviceSubSpecialityId = value.serviceSubSpecialityId;
						$scope.autoRenderedServicesFinalList[index].isAutoRender =  value.isAutoRender;
						$scope.autoRenderedServicesFinalList[index].serviceRendered = value.serviceRendered;
						$scope.autoRenderedServicesFinalList[index].serviceRenderingDeptId = value.serviceRenderingDeptId;
						$scope.autoRenderedServicesFinalList[index].oldNetPay = value.oldNetPay !== undefined && !isNaN(value.oldNetPay) ? value.oldNetPay : "0";
						$scope.autoRenderedServicesFinalList[index].discountDetailsList = value.discountDetailsList; 
						$scope.autoRenderedServicesFinalList[index].serviceMasterId = value.serviceMasterId;
						$scope.autoRenderedServicesFinalList[index].serviceStandardCode = value.serviceStandardCode;
						$scope.autoRenderedServicesFinalList[index].serviceStandardName = value.serviceStandardName;
                        $scope.autoRenderedServicesFinalList[index].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].isDiscountApplicable = (value.packageConsumptionAmt != 0) ? 'N':value.isDiscountApplicable; // isDiscountApplicable
						$scope.autoRenderedServicesFinalList[index].isDiscount = value.isDiscountApplicable;
						if(value.serviceMasterId == $scope.surchargeServiceId){
                            $scope.autoRenderedServicesFinalList[index].isDiscountApplicable = 'N';
                        }
						if(value.packageConsumptionAmt!=0 || value.serviceMasterId==$scope.pharmacyServiceId){
							// $scope.autoRenderedServicesFinalList[index].isDiscountApplicable
							// = 'N';
							$scope.autoRenderedServicesFinalList[index].isRateEditable = 'N';
							$scope.autoRenderedServicesFinalList[index].isQuantityEditable = 'N';
						}else{
							// $scope.autoRenderedServicesFinalList[index].isDiscountApplicable
							// =
							// $scope.autoRenderedServicesFinalList[index].isDiscountApplicable;
							$scope.autoRenderedServicesFinalList[index].isRateEditable = $scope.autoRenderedServicesFinalList[index].isRateEditable;
							$scope.autoRenderedServicesFinalList[index].isQuantityEditable = value.isQuantityEditable;
						}
						
						$scope.autoRenderedServicesFinalList[index].isTariffRateApplicable = value.isTariffRateApplicable;// 12-10-2019
						$scope.autoRenderedServicesFinalList[index].minRateEditable = value.minRateEditable;
						$scope.autoRenderedServicesFinalList[index].maxRateEditable = value.maxRateEditable;
						$scope.autoRenderedServicesFinalList[index].specialityName = value.specialityName;
						$scope.autoRenderedServicesFinalList[index].doctorName = value.doctorName;
						$scope.autoRenderedServicesFinalList[index].discountDisabledFlag = 'N';
						$scope.autoRenderedServicesFinalList[index].isPackage = value.isPackage;
						$scope.autoRenderedServicesFinalList[index].packageMasterId = value.packageMasterId;
						$scope.autoRenderedServicesFinalList[index].packageTypeId = value.packageTypeId;
						$scope.autoRenderedServicesFinalList[index].isPackageService = value.isPackageService;
						$scope.autoRenderedServicesFinalList[index].apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
						$scope.autoRenderedServicesFinalList[index].isCapService = value.isCapService;
		    			$scope.autoRenderedServicesFinalList[index].packageConsumptionAmt = value.packageConsumptionAmt;
		    			$scope.autoRenderedServicesFinalList[index].doctorSpecialityId !=undefined && $scope.autoRenderedServicesFinalList[index].doctorSpecialityId >0 ? $scope.autoRenderedServicesFinalList[index].docSpecialityList=$scope.specialityMasterList : $scope.autoRenderedServicesFinalList[index].docSpecialityList = [];
		    			$scope.autoRenderedServicesFinalList[index].doctorSpecialityList = (value.doctorSpecialityList != undefined ? value.doctorSpecialityList : []);
		    			$scope.autoRenderedServicesFinalList[index].docList=value.docList != null ? value.docList:$scope.doctorsList;
		    			$scope.autoRenderedServicesFinalList[index].isFromAddservice = (value.isFromAddservice != null || value.isFromAddservice != '' || value.isFromAddservice != undefined ? value.isFromAddservice :'N') ;
		    			if(value.subSpecialityId==undefined||value.subSpecialityId==null)
		    				{
		    				  $scope.autoRenderedServicesFinalList[index].subSpecialityId = value.serviceRenderingDeptId;
		    				}
		    			else{
		    			     $scope.autoRenderedServicesFinalList[index].subSpecialityId = value.subSpecialityId;
		    			    }
		    			$scope.autoRenderedServicesFinalList[index].packageId = value.packageId;
						if(value.specialityName == null)
						{
							$scope.autoRenderedServicesFinalList[index].specialityName = "";
							$scope.autoRenderedServicesFinalList[index].doctorName = "";
						}
						$scope.autoRenderedServicesFinalList[index].quantity = value.quantity;
						$scope.autoRenderedServicesFinalList[index].basePrice = parseFloat(value.basePrice).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].basePriceRateChange = parseFloat(value.basePrice).toFixed(2);
						if(parseFloat(value.packageConsumptionAmt) > 0 && value1.isPackageService == 'N'){
							$scope.autoRenderedServicesFinalList[index].basePriceRateChange = parseFloat(value1.serviceAmount).toFixed(2);
						}
						$scope.autoRenderedServicesFinalList[index].coPayPer =  value.coPayPer !== undefined && !isNaN(value.coPayPer) ? parseFloat(value.coPayPer).toFixed(2) : "0";
						$scope.autoRenderedServicesFinalList[index].basePriceOld = value.basePrice;
						$scope.autoRenderedServicesFinalList[index].oldBasePrice = value.oldBasePrice;
						$scope.autoRenderedServicesFinalList[index].concession = parseFloat(value.concession).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].patientClassPercentage = parseFloat(value.patientClassPercentage).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].patientClassPercentageChange = parseFloat(value.patientClassPercentage).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].discountCategoryId = value.discountCategoryId != null ? value.discountCategoryId.toString()  : null;
						$scope.autoRenderedServicesFinalList[index].discountPercentage = value.discountPercentage;
						$scope.autoRenderedServicesFinalList[index].discount = value.discount;
						$scope.autoRenderedServicesFinalList[index].orderDateString = value.orderDateString;
						$scope.autoRenderedServicesFinalList[index].orderDateTimeString = value.orderDateTimeString;
						$scope.autoRenderedServicesFinalList[index].orderDateTime = value.orderDateTime;
						$scope.autoRenderedServicesFinalList[index].packageEitherOrGroupId = value.packageEitherOrGroupId;
						$scope.autoRenderedServicesFinalList[index].doctorForServiceList = value.doctorList != null ? value.doctorList :value.doctorForServiceList;

						var dis = parseFloat(value.discount).toFixed(2);
						var disPer =value.discountPercentage;
						if(value.discount == null || value.discount == 0 || value.discount == '')
							{
								$scope.autoRenderedServicesFinalList[index].discountPercentage = 0;
								$scope.autoRenderedServicesFinalList[index].discount = 0;
								dis = 0;
								disPer = 0;
								$scope.autoRenderedServicesFinalList[index].discountDisabledFlag = 'Y';
							}
							
						if(value.discount > 0 && (value.discountPercentage == null || value.discountPercentage == 0 || value.discountPercentage == '')){
							$scope.autoRenderedServicesFinalList[index].discountPercentage = isNaN(((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - parseFloat(value.concession)))*100).toFixed(2);
							}
						
							if(value.discountDetailsList == null)
							{
								value.discountDetailsList = [];
								$scope.autoRenderedServicesFinalList[index].discountDetailsList = [];
							}
						
						$scope.autoRenderedServicesFinalList[index].taxName = value.taxName;
						var txpre = 0;
						if(value.taxPercentage != null || value.taxPercentage > 0){
							$scope.autoRenderedServicesFinalList[index].taxPercentage = value.taxPercentage;
							txpre = value.taxPercentage;
						}
						else
							{
							 $scope.autoRenderedServicesFinalList[index].taxPercentage = 0;
							}
						
						
						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
						 totalAmt  = parseFloat(totalAmt) - value.packageConsumptionAmt;
						var concession = (parseFloat(value.concession).toFixed(2));
						var withConDic =parseFloat(totalAmt - (parseFloat(concession) + parseFloat(dis))).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
						var gstAmt =  parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].taxAmtOld = value.taxAmtOld;
						$scope.autoRenderedServicesFinalList[index].selfPayableOldC = value.selfPayableOldC;
						$scope.autoRenderedServicesFinalList[index].creditPayableOldC = value.creditPayableOldC;
						$scope.autoRenderedServicesFinalList[index].netPay =   parseFloat(value.netPay).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].ordTotalAmount =   parseFloat(value.totalAmt).toFixed(2);
						var selfPayableV =   "0";
						var creditPayableV =   "0";
						
							if(value.payeeId == $scope.selfPayeeId || value.payeeId == null )
							{
							      $scope.autoRenderedServicesFinalList[index].selfPayable =parseFloat(value.selfPayable).toFixed(2);;
							      $scope.autoRenderedServicesFinalList[index].creditPayable =parseFloat(value.creditPayable).toFixed(2);;
							      selfPayableV =  parseFloat($scope.autoRenderedServicesFinalList[index].selfPayable).toFixed(2);
							      creditPayableV =  parseFloat($scope.autoRenderedServicesFinalList[index].creditPayable).toFixed(2);
							}else{
								
								$scope.autoRenderedServicesFinalList[index].payeeId= value.payeeId;
								    if(value.selfPayable>0)
									{
								    	
								    	if(parseInt(dis) > 0 )
								    		{
								    		$scope.autoRenderedServicesFinalList[index].selfPayable =   parseFloat(value.selfPayable).toFixed(2);
								            selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
								    		}
								    	else
								    		{
								    		 $scope.autoRenderedServicesFinalList[index].selfPayable =parseFloat(value.selfPayableOldC).toFixed(2);
											 selfPayableV =   parseFloat(value.selfPayableOldC).toFixed(2);
								    		}
								    
									
									$scope.autoRenderedServicesFinalList[index].creditPayable =parseFloat(value.creditPayable).toFixed(2);
									creditPayableV=parseFloat(value.creditPayable).toFixed(2);
									}
								else{
									$scope.autoRenderedServicesFinalList[index].selfPayable = (value.selfPayable==null||value.selfPayable==undefined||value.selfPayable=="")?0:value.selfPayable;
									selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
								   
								    $scope.autoRenderedServicesFinalList[index].creditPayable  = value.creditPayable;
								    creditPayableV = parseFloat(value.creditPayable).toFixed(2);
								    
								   }
								
								}
						$scope.autoRenderedServicesFinalList[index].saleId = value.saleId;
					    $scope.autoRenderedServicesFinalList[index].orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
					    $scope.autoRenderedServicesFinalList[index].orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
					    $scope.autoRenderedServicesFinalList[index].isPeriodicityService = value.periodicityServiceOrderId!=null ? 'Y' : 'N' ;
					    $scope.autoRenderedServicesFinalList[index].forceAddRemoved = value.forceAddRemoved;
					    $scope.autoRenderedServicesFinalList[index].isConsumed = value.isConsumed;
						$scope.autoRenderedServicesFinalList[index].createdBy = value.createdBy;
						$scope.autoRenderedServicesFinalList[index].createdDate = value.createdDate;
						$scope.autoRenderedServicesFinalList[index].discountRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].discount).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].discountPercentageRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].discountPercentage).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].conccesionPriceRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].concession).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].patClsPerPriceRateChange = parseFloat($scope.autoRenderedServicesFinalList[index].patientClassPercentage).toFixed(2);
						$scope.autoRenderedServicesFinalList[index].userName = value.userName;
						$scope.autoRenderedServicesFinalList[index].createdBy = value.createdBy;
						var tempTariffList=[];
						if($scope.autoRenderedServicesFinalList[index].tariffList==undefined)
							{
						        if($scope.tariffList.length>0)
			  			          {
					  			angular.forEach($scope.tariffList, function (value1, key) {
					  				tempTariffList.push(value1);
					  			   });
			  			          }
						        $scope.autoRenderedServicesFinalList[index].tariffList=tempTariffList;
				            }
						$scope.autoRenderedServicesFinalList[index].hospitalShareNew=parseFloat(value.netPay-(parseFloat(gstAmt!=null && gstAmt!=undefined ?gstAmt:0)
								+parseFloat(value.docSharePer!=null?value.docSharePer:0)
								+parseFloat(value.refDocSharePer!=null?value.refDocSharePer:0))).toFixed(2);
						
				 });// /end service loop
			count++;
			});
		if(count==list.length)
			$scope.resetTotalAmounts();
			// $scope.calculationTest($scope.autoRenderedServicesFinalList,init);
		
	}
	
	$scope.resetTotalAmounts = function()
	{
		
		
		var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
		if(indexT > 0)
		{
			var indexObj = $scope.autoRenderedServicesFinalList[indexT];
			$scope.autoRenderedServicesFinalList.splice(indexT, 1);
			
			if(indexObj.orderDetailsId!=undefined && indexObj.orderDetailsId!=null && indexObj.orderDetailsId>0)
				$scope.addCapAmout(indexObj,$scope.autoRenderedServicesFinalList);
		}
		
		$scope.AutoRenderedTotalAmountSummary=0.00;
		$scope.AutoRenderedTotalConcessionSummary=0.00;
		$scope.AutoRenderednetPayableSummary=0.00;
		$scope.AutoRenderednetTotalDiscountSummary=0.00;
		$scope.AutoRenderednetTotalTax = 0.00;
		$scope.AutoRenderednetTotalDiscount = 0.00;
		$scope.AutoRenderednetTotalCreditPayble=0.00;
		$scope.AutoRenderednetTotalSelfPayble= 0.00;
		$scope.AutoRenderednetTotalPackagePayble= 0.00;
		$scope.totalDisServiceWise =0.00;
		$scope.totalAmtApplicableForDiscount =0.00;
		
		console.log("$scope.autoRenderedServicesFinalList before Loop:"+$scope.autoRenderedServicesFinalList);
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
				{
					angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
						{
							if(value1.isPackageService=='N' && value.status=='A')
							{
								if(	$scope.ambulanceChargesServiceId != undefined && $scope.ambulanceChargesServiceId != null  && $scope.ambulanceChargesServiceId  == value1.serviceId){
									$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.netPay)).toFixed(2);
								}else{
									$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
								}
								
								$scope.AutoRenderedTotalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary) + (parseFloat(value.concession))).toFixed(2);
								$scope.AutoRenderednetPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary) + parseFloat(value.netPay)).toFixed(2);
								$scope.AutoRenderednetTotalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscountSummary) + parseFloat(0)).toFixed(2);
								$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) +  (parseFloat(value.taxAmt))).toFixed(2);
								$scope.AutoRenderednetTotalDiscount = (parseFloat($scope.AutoRenderednetTotalDiscount) + parseFloat(value.discount)).toFixed(2);
								$scope.AutoRenderednetTotalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble) + parseFloat(value.creditPayable)).toFixed(2);
								$scope.AutoRenderednetTotalSelfPayble= (parseFloat($scope.AutoRenderednetTotalSelfPayble)+ parseFloat(value.selfPayable)).toFixed(2);
								$scope.AutoRenderednetTotalPackagePayble= (parseFloat($scope.AutoRenderednetTotalPackagePayble)+ parseFloat(value.packageConsumptionAmt)).toFixed(2);
							}
							if(value.isDiscountApplicable == 'Y' && value.status == 'A')
	                    	{
	                    		$scope.totalDisServiceWise  = parseFloat($scope.totalDisServiceWise) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)));
	                    		$scope.totalAmtApplicableForDiscount = parseFloat($scope.totalAmtApplicableForDiscount) + ((parseFloat(value.quantity) * parseFloat(value.basePrice)));
	                    	}
							
						});
				});
		
		$scope.totalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)).toFixed(2);
		$scope.totalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary)).toFixed(2);
		$scope.netPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary)).toFixed(2);
		$scope.totalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscount)).toFixed(2);
		$scope.totalGstTaxSummary=(parseFloat($scope.AutoRenderednetTotalTax)).toFixed(2);
		
		if($scope.billWiseDiscountObj !=null && $scope.billWiseDiscountObj.discountPercentageBillWise >0 && ($scope.cancelService == true || $scope.forceAddOrRemove == true))
			$scope.billWiseDiscountObj.discountAmountBillWise = (($scope.totalAmtApplicableForDiscount - $scope.totalConcessionSummary)*($scope.billWiseDiscountObj.discountPercentageBillWise * 0.01));
		else if ($scope.cancelService == true || $scope.forceAddOrRemove == true)
			$scope.billWiseDiscountObj.discountAmountBillWise = $scope.totalDiscountSummary;
		
		$scope.billWiseDiscountObj.discountAmountBillWise = isNaN(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)) ? parseFloat(0) : (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)).toFixed(2); 
		var amt =   isNaN(Number(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)))?0:$scope.billWiseDiscountObj.discountAmountBillWise;
		if(amt>0 && $scope.AutoRenderednetTotalDiscount>0 && $scope.isBillWiseDiscountApplied == true)
		{
			$scope.roundOffAmt = parseFloat(amt-$scope.AutoRenderednetTotalDiscount).toFixed(2);
		}
		
		$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);
		$scope.totalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
		$scope.totalSelfPaybleTemp=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
		$scope.totalPackagePaybleTemp=(parseFloat($scope.AutoRenderednetTotalPackagePayble)).toFixed(2);
		
		if($scope.roundOffAmt!=0)
        {
            if($scope.totalSelfPayble!=0)
            {
                $scope.totalSelfPayble = parseFloat(Number(parseFloat($scope.totalSelfPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
                $scope.totalSelfPaybleTemp = parseFloat(Number(parseFloat($scope.totalSelfPaybleTemp).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
            }else{
                $scope.totalCreditPayble = parseFloat(Number(parseFloat($scope.totalCreditPayble).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
            }
            $scope.netPayableSummary = parseFloat(Number(parseFloat($scope.netPayableSummary).toFixed(2))-parseFloat($scope.roundOffAmt)).toFixed(2);
        }
		
		$scope.forceAddOrRemove = false;
        $scope.cancelService = false;
	}
	
	
	
	
	$scope.saveOrderPackage = function()
	{
		$rootScope.startLoaderPopup();
		 $scope.isPatientClass = 'N';
		var data={
				patientId :$scope.encounterDetails.patientId,
				encounterId :$scope.encounterDetails.encounterId,
				createdBy :$scope.userId,
				createdDate :new Date().getTime(),
				updatedBy :$scope.userId,
				updatedDate :new Date().getTime(),
				orderDate :new Date().getTime(),
				organisationId :$scope.organizationId,
				unitId :$scope.unitId,
				isFinalBill : $scope.finalBill,
				ordDoctorId :$scope.encounterDetails.doctorId,
				visitTypeId : '1',
				billDateTime : new Date().getTime(),
				netAmount :  $scope.netPayableSummary !== undefined && !isNaN($scope.netPayableSummary) ? parseFloat(parseFloat($scope.netPayableSummary)).toFixed(2) : "0",
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				isFinancialClearance : 'N',
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : $scope.removeDiscountList,
                tariffId : $scope.defaultSelfTariffId,
				paymentEntitlementId : $scope.encounterDetails.paymentEntitlementTypeId,
				visitTypeId : $scope.visitTypeId,
				patientTypeId : $scope.patientDetails.patientTypeId,
				billingBedCategoryId : $scope.opdBillingCatId,// default op
				isInterimBill :'N',
				isDrNote:"N",
				contractId:$scope.contractId,
				counterId:$rootScope.counterId,
				encounterSpecialityId:$scope.encounterDetails.specialityId,
				lineLevelDiscountOrderList : [],
				patientClassId : $scope.encounterDetails.patientClassId,
				surchargeServiceId : ($scope.surchargeServiceId != null && $scope.surchargeServiceId > 0?$scope.surchargeServiceId : null),
				pharmacyServiceId:$scope.pharmacyServiceId,
				referralId : $scope.encounterDetails.referralId,
				selfPayeeId : $scope.selfPayeeId,
				homeCollection : $scope.homeCollection=='Y' ? true : false,
				applyAuth : $scope.applyAuth!=null ? $scope.applyAuth : false,
				needToUpdatePharmacyService : $scope.needToUpdatePharmacyService!=null ? $scope.needToUpdatePharmacyService : false,
				updatePayee : $scope.updatePayee!=null ? $scope.updatePayee : false,
				isPreBilling :$scope.isPreOrPostBilling 		
		   };
		
		if($scope.updatePayee){
			data.discountAmount="0.0";
			// $scope.totalDiscountSummary=0.0;
            if($scope.requestStatusObject != null){
                $scope.getCancelDiscountReqForDiscount();
                }
            $scope.billWiseDiscountObj = {
                    // "discountGivenBy":"",
                    "discountGivenBy":$scope.userId,
                    "discountCategoryId" : "",
                    "discountAmountBillWise" : "",
                    "discountPercentageBillWise" : "",
                    "approvedAmountBillWise":"",
                    "requestUserId":"",
                    "requestAmountBillWise" : 0,
            }
            $scope.requestDiscountDiv = false;

            
		}
		var billPayeeList =[];
		if($scope.totalSelfPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.selfPayeeId,
						billAmount : $scope.totalSelfPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		if($scope.totalCreditPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.encounterDetails.payeeId,
						billAmount : $scope.totalCreditPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		
		data.billPayeeDetailsDtosList = billPayeeList;
		var saveOrderDetailsObj = [];
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			/* if($scope.surchargeServiceId != value.serviceMasterId){ */
			var saveArr = {};
			if(value.orderDetailsId > 0){
				saveArr.orderDetailsId = value.orderDetailsId;
			}
			saveArr.discountOwnership = value.discountOwnership;
			saveArr.isFromServiceSerach = value.isFromServiceSerach;
			saveArr.isOrderVisibleDepartment = value.isOrderVisibleDepartment;
			saveArr.isConsultationService = value.isConsultationService;
			saveArr.ordRemarks = value.ordRemarks;
			saveArr.nursingServiceOrderFlag =value.nursingServiceOrderFlag;
			saveArr.markupStatus = value.markupStatus;
			saveArr.patientClassPercentage = value.patientClassPercentage;
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			// saveArr.orderDate = new Date().getTime();
			saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.payeeId = value.payeeId;
			saveArr.tariffId = value.tariffId;
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			
			if($scope.surchargeServiceId == value.serviceMasterId){
				saveArr.status = 'I';
			}
			else{
				saveArr.status = value.status;
			}
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = value.createdBy;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay =  value.oldNetPay !== undefined && !isNaN(value.oldNetPay) ? parseFloat(value.oldNetPay).toFixed(2) : "0";
			saveArr.ordCancelled = value.ordCancelled;
			saveArr.ordCancelReasonId  = value.ordCancelReasonId;
			saveArr.ordCancelDatetime  = value.ordCancelDatetime;
			saveArr.ordCancelRemark  = value.ordCancelRemark;
			saveArr.oldOrdDtlId = value.oldOrdDtlId;
			saveArr.serviceAmt = value.basePrice !== undefined && !isNaN(value.basePrice) ? value.basePrice : "0";
			saveArr.coPayPer = value.coPayPer !== undefined && !isNaN(value.coPayPer) ? parseFloat(value.coPayPer).toFixed(2) : "0";
			saveArr.ordTotalAmt = parseFloat(saveArr.serviceAmt) * parseFloat(value.quantity);
			saveArr.taxPer  = value.taxPercentage !== undefined && !isNaN(value.taxPercentage) ? value.taxPercentage : "0";
			saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
			saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
			saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
			var withConDic =parseFloat(parseFloat(saveArr.ordTotalAmt) - parseFloat(value.concession) - parseFloat(value.discount)).toFixed(2);
			saveArr.taxAmount = withConDic !== undefined && !isNaN(withConDic) ? parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2) : "0";
			saveArr.netAmount =    withConDic !== undefined && !isNaN(withConDic) ? parseFloat(parseFloat(withConDic) +  parseFloat(saveArr.taxAmount)).toFixed(2) : "0";
			saveArr.dummyServiceAmt = value.dummyServiceAmt;
			saveArr.ordConcession = value.concession !== undefined && !isNaN(value.concession) ? value.concession : "0";
			saveArr.ordDiscount = value.discount !== undefined && !isNaN(value.discount) ? value.discount : "0";
			saveArr.ordDiscountPer = value.discountPercentage !== undefined && !isNaN(value.discountPercentage) ? value.discountPercentage : "0";
			saveArr.isFromAddservice = value.isFromAddservice;
			saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
			// saveArr.selfPayable = (saveArr.isPackage ||
			// value.packageTypeId==1 ) ? value.selfPayable : saveArr.netAmount;
			saveArr.selfPayable =   value.packageTypeId==1 &&  !saveArr.isPackage ?  0 : saveArr.netAmount;
			// saveArr.creditPayable = saveArr.isPackage?value.creditPayable:0;
			saveArr.creditPayable = 0;
			saveArr.oldCreditPayable = 0;
			saveArr.oldSelfPayable = 0;
			saveArr.taxId = value.taxId;
			saveArr.isInvestigationOrder = value.isInvestigationOrder;
            saveArr.isDiscountApplicable = value.isDiscountApplicable;
            saveArr.isDiscount = value.isDiscount;
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
			saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
			saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
			saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
			saveArr.isCapService = value.isCapService;
			saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
			saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
			saveArr.subDepartmentId = value.serviceSubSpecialityId;
			saveArr.isRefDoctorShare = value.isRefDoctorShare;
			saveArr.isDocReq =  value.isDocReq!=null &&  value.isDocReq!=undefined ? value.isDocReq :value.isDoctorRequired;
			saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.serviceRenderingDeptId = value.subSpecialityId; 
			saveArr.encounterId = $scope.encounterDetails.encounterId;
			saveArr.contractId = value.contractId;
			saveArr.bedCategoryId = value.bedCategoryId;
			saveArr.discountCategoryId = value.discountCategoryId;
			saveArr.discountDetailsList = [];
			saveArr.debitNoteService = false;
			  // package force add
			saveArr.forceAddRemoved = value.forceAddRemoved;
			saveArr.isConsumed = value.isConsumed;			
			saveArr.isLabService = $scope.labConfig.labSpecialityId==saveArr.departmentId ? 'Y' : 'N';
			saveArr.packageApplicableDate = value.packageApplicableDate;
			if(value.discountDetailsList == null)
				{
				 value.discountDetailsList = [];
				}
			    saveArr.saleId = value.saleId;
				saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
				saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
                if($scope.updatePayee){
                    saveArr.ordDiscount = parseFloat(0);
                    saveArr.ordDiscountPer =  parseFloat(0);
                }
			    saveOrderDetailsObj.push(saveArr);
		/* } */
		    });
		               data.orderDetailsMasterDtosList = saveOrderDetailsObj;
						var URI = BASE_URL + ROOT_URL +'/api/billing/upDatePackage';
						BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
							if(response.data.status == 'success')
							{        
								
								
								$scope.getAutoRenderedServicesByPatient({
		    						"visitTypeId":1,
		    						"encounterId":$scope.encounterDetails.encounterId
								});
								
								var checkIn = false;
								$timeout(function () {
									
									if($scope.selectedServices!=undefined && $scope.selectedServices!=null && $scope.selectedServices.length>0)
									{
										 checkIn = true;
										angular.forEach($scope.selectedServices, function (service, key) {
											
												$scope.addServiceFromSearch(service, "", "","Y",undefined);
										});
									
									}
									
									
								}, 1500);
								
								
								 $scope.isPatientClass = 'N';
                                 $scope.needToUpdatePharmacyService = false;
                                 $scope.getMedicationIssueListByEncounterId(); 
                                 
                                 if(checkIn == true){
                                	 
                                	 $timeout(function () {
											$scope.getAutoRenderedServicesByPatient({
					    						"visitTypeId":1,
					    						"encounterId":$scope.encounterDetails.encounterId
											});
										
										}, 4000);
                                 }
										
										
                                 
                                 if($scope.updatePayee)
                                 {
                                 growl.success(
											'Payee Updated Successfully..!!',
											{
												title : 'Success!'
											});
                                 }
//                                 $scope.applyAuth = false;
                                // $scope.updatePayee = false;
								 $rootScope.stopLoaderPopup();
							}else if (response.data.code==409)
							{
								$rootScope.stopLoader();
								$scope.btnDisabled = false;
								$scope.getMedicationIssueListByEncounterId(); 
								if(response.data.object==true) // if mismatch in bill
																// found
								{
									$scope.getAutoRenderedServicesByPatient({
			    						"visitTypeId":1,
			    						"encounterId":$scope.encounterDetails.encounterId
									});
								}else{
									$scope.patientEncounterList = [];
									$scope.init();
								}
							}else if(response.data.status == 'error')
							{
								 $scope.isPatientClass = 'N';
								$rootScope.stopLoaderPopup();
							}
						});
	}
	
	
	
	$scope.getPackages = function(){
		$scope.setSecondTabLevel(21);
		var packageIds = [];
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,key){
			if(value.isPackage && value.isPackageService != 'Y' && packageIds.indexOf(value.packageMasterId) == -1 && $scope.activePackageIds.indexOf(value.packageMasterId) == -1)
				packageIds.push(value.packageMasterId==undefined ?value.packageId:value.packageMasterId);
		});
		// get package details
		if(packageIds.length > 0){ 
			var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
			BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				if(response.data.status == "success")
					if($scope.packageDetailsList!=undefined && $scope.packageDetailsList.length>0 && response.data.listObject!=null)
			    	   {
			    	   angular.forEach(response.data.listObject,function(value,key){
			    		   var found = $scope.packageDetailsList.find(ar=>ar.mPackageMasterDto.packageMasterId==value.mPackageMasterDto.packageMasterId);
			    		   if(found==undefined)
						   {
			    			   $scope.packageDetailsList.push(response.data.listObject.mPackageMasterDto);
						   }
						});
			    	   }
			       else{
			    	     $scope.packageDetailsList = response.data.listObject;
			          }
			});
		}
	}
	$scope.getActivePackagesList = function()
	{
		$scope.activePackageIds = [];
		$scope.activePackagesList = [];
		if($scope.patientDetails !== undefined && $scope.patientDetails !== null){
            if($scope.patientDetails.patientId === undefined || $scope.patientDetails.patientId === null || $scope.patientDetails.patientId ===''){
                $scope.patientDetails.patientId = 0;
            }
        }
        else{
            $scope.patientDetails.patientId = 0;
        }
		var URI =BASE_URL + ROOT_URL + "/api/packages/patientId/"+$scope.patientDetails.patientId+"/encounterId/"+$scope.encounterDetails.encounterId+"/visitTypeId/1";
		var header = {organisationId: $scope.organizationId,unitId:$scope.unitId,packageId:0};
		GenericServiceParamHeader.serviceAction("GET", URI, null,NOTIFICATION_MSG_STATUS_FALSE,null,header).then(function(response){
			if(response.data.status == "success"){
				$scope.activePackagesList = response.data.listObject;
			$scope.activePackagesList.forEach(function(value,index){
				$scope.activePackageIds.push(value.packageId);
			})
		}
		});
	}

	
	// package transaction view start
	$scope.getAllPackageList=function(){
		/*angular.forEach($scope.listPackageOrder,function(value,key){
			value.orderDate = moment(value.orderDate,'DD-MM-YYYY').format('DD-MMM-YYYY');
			});*/
		  $('#commonPackageDetailsPopup').modal('show');
	}
	
	$scope.forceCancelPackage=function(obj){
		var data={
				packageConsumptionMasterId:obj.packageConsumptionMasterId,
				orderDetailId:obj.orderDetailId,
				toDate : moment().format('YYYY-MM-DD HH:mm:ss'),
				unitId : $scope.unitId
		}
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/forceCancelPackage";
		BillingGenericService.serviceAction("POST", URI, data).then(function(response){
			if(response.data.status == "success")
				{
					growl.success("Package closed",{
						title : WARNING_MSG
					});
					$scope.getListPackageOrder();
					$rootScope.stopLoader();
				}else{
					$rootScope.stopLoader();
				}
				
		});
	}
	
	$scope.getServiceIndex = function(index)
	{
		$scope.serviceIdxPkg = index;
	}
	$scope.addToPackage = function(packageObj)
	{
		
		
		var checkdis = false;
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			
			if((value.discountPercentage != undefined && value.discountPercentage > 0 ) || (value.discount != undefined && value.discount >0) )
			{
				checkdis = true;
			}
		});
		
		if(checkdis == true){
			
			growl.warning(" Applied discount will get cleared ",{
				title : "Warning"
			});
			
			$scope.finalCancelDiscount();
		}
		
		$timeout(function () {

			 $scope.forceAddOrRemove = true;
			 $scope.servicesForForceAddOrRemove =[];
			 $rootScope.startLoaderPopup();
			 var packageMppr = [];
					 packageMppr = [{"id":"",
						"orderDetailsId":"",
						"serviceId":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].serviceId,
						"packageId":packageObj.packageId,
						"payeeId":"",
						"serviceAmount":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].basePrice,	
						"packageAmount":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].basePrice * $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].quantity,
						"packageCode":"",
					    "packageName":"",
						"isPackageService":"Y",
						"isCapService":"N",
						"saleDetailsId":"",
						"itemId":"",
						"status":"A",
						"unitId":$scope.unitId,
						"organizationId": $scope.organizationId}];

			 $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].orderDetailsPackageMapperDtosList = packageMppr;
			 $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].forceAddRemoved =1;
			 $scope.autoRenderedServicesForSaveAndUpdate =[];
			 $scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg]);
			 
			 $scope.addOrUpdateServices($scope.serviceIdxPkg,null);
			 angular.element('#packageForceAdd').modal('hide');
			 
			 //ServiceCount
			/* angular.forEach($scope.autoRenderedServicesForSaveAndUpdate,function(value,index){
					if(value.isPackageService == 'N')
					{
						$scope.ServiceCount=$scope.ServiceCount - parseInt(value.quantity);
					}
					});*/

	   }, 1500);
		
		
	}
	
	$scope.removeFromPackage = function(serviceIdx)
	{
		
		
		var checkdis = false;
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			
			if((value.discountPercentage != undefined && value.discountPercentage > 0 ) || (value.discount != undefined && value.discount >0) )
			{
				checkdis = true;
			}
		});
		
		if(checkdis == true){
			
			growl.warning(" Applied discount will get cleared ",{
				title : "Warning"
			});
			
			$scope.finalCancelDiscount();
		}
		
		$timeout(function () {
		
		 $scope.forceAddOrRemove = true; 
		 $scope.autoRenderedServicesFinalList[serviceIdx].forceAddRemoved =-1;
		 $scope.autoRenderedServicesFinalList[serviceIdx].isPackageService='N';
		 $scope.autoRenderedServicesForSaveAndUpdate =[];
		 $scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[serviceIdx]);
		 $scope.addOrUpdateServices(serviceIdx,null);
		 
		//ServiceCount
		/* angular.forEach($scope.autoRenderedServicesForSaveAndUpdate,function(value,index){
				if(value.isPackageService == 'N')
				{
					$scope.ServiceCount=$scope.ServiceCount ;
				}
				});*/
		 
		}, 1500);
	}
	
	$scope.getPackagesDetails = function(packageId,consumptionMasterId)
	{
		 
		var data={};
		data[consumptionMasterId]=packageId;   
	
	// get package details
		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package";
		BillingGenericService.serviceAction("POST", URI, data).then(function(response){
			if(response.data.status == "success")
				{
				
				   // console.log(response.data.listObject);
				   $scope.packageTypeId = response.data.listObject[0].mPackageMasterDto.packageTypeId;
				   $scope.listPackage = [];
				   $scope.listPackage = response.data.listObject[0];
				   if($scope.packageTypeId==1 || $scope.packageTypeId==5){
					   $scope.setFirstTabLevel(101);
                   // added by sneha getUsersListHavingFinancialClearanceRights
	    	    		var packageIds = [];
	    	    		packageIds.push(packageId);
	    	    		// get ehc package services
	    	    		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
	    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
	    					if(response.data.status == "success")
	    					{
	    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
	    						console.log("list",$scope.listPackage);
	    						
	    						$scope.consulationserviceIdlIST  =[];
	    						
			                       angular.forEach(ehcPackageServiceList,function(val,i){
		    								if(val.consultationSpecialityId>0)
			    			    			{
		    									$scope.consulationserviceIdlIST.push(val);
				    			    				   
			    			    			}
		    						 });
			                       
			                       angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(val,i){
			                    	   val.pushed = false;
			                       });
			                       
			                       var count = 0;
			                       angular.forEach($scope.consulationserviceIdlIST,function(val1,i1){
		    							
			    						angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(val,i){
													if(val1.packageId == val.packageId && val1.serviceId == val.serviceId ){
														
														if(count == 0){
															 if(val1.consultationSpecialityId>0)
								    			    			{
									    			    			if(val1.consultationSpecialityId>0 && val.pushed == false)
									    			    			{
									    			    				val.consultationSpecialityId =val1.consultationSpecialityId;
									    			    				val.pushed = true;
																		count++;
									    			    				   
									    			    			}
								    			    			  }
														}else{
															var found = $scope.listPackage.listTPackageServicesDetailsDto.find(ar=>ar.consultationSpecialityId == val1.consultationSpecialityId && ar.serviceId == val1.serviceId);
															   if(found==undefined)
																   {
																   if(val1.consultationSpecialityId>0)
									    			    			{
										    			    			if(val1.consultationSpecialityId>0 && val.pushed == false)
										    			    			{
										    			    				val.consultationSpecialityId =val1.consultationSpecialityId;
										    			    				val.pushed = true;
										    			    				   
										    			    			}
									    			    			  }
								    							   }
														}
														
														}
			    						
			    						    });
			    						});
			                       
			                       angular.forEach($scope.listPackage.listTPackageServicesDetailsDto,function(val,i){
	                                   angular.forEach($scope.specialityMasterList,function(spec,ispec){
	                                       if(val.consultationSpecialityId == spec.specialityId){
	                                           val.specialityName=spec.specialityName;

	                                          }
	                                       });  
		    						    });
	    						
	    						console.log("list3",$scope.listPackage.listTPackageServicesDetailsDto);
	    						$scope.packageDetailsResponse=$scope.listPackage;
	    					}
	    				});
	    				 
	  				     $('#packageServiceDetailsPopup').modal('show');
				   }
				   else if($scope.packageTypeId==2){
					   $scope.setFirstTabLevel(401);
					   $scope.packageDetailsResponse=response.data.listObject[0];
					   $('#packageServiceDetailsPopup').modal('show');
				   }
				   else if($scope.packageTypeId==3){
					   $scope.setFirstTabLevel(201);
					   $scope.packageDetailsResponse=response.data.listObject[0];
					   $('#packageServiceDetailsPopup').modal('show');
				   }else if($scope.packageTypeId==4){
					   $scope.setFirstTabLevel(301);
					   $scope.packageDetailsResponse=response.data.listObject[0];
					   $('#packageServiceDetailsPopup').modal('show');
				   }
				   /*$scope.packageDetailsResponse=response.data.listObject[0];
				   $('#packageServiceDetailsPopup').modal('show');*/
			    
				}
				
		});
	}
	$scope.eoGroupServiceDtls=[];
	$scope.getGroupServiceDetails=function(list){
		
		$scope.eoGroupServiceDtls=list;
		 $('#qtyDetailsModal').modal('show');
		
	}
	$scope.setPackageOrderTabActive=function(){
		$scope.setFirstTabLevel(2);
		$scope.setSecondTabLevel(22);
	}

	// package transaction view END
	// getPackageDetails by serviceConsumptionDetailsId Start
	$scope.getServiceConsumptionDetailsId=function(id){
		$scope.serviceConsumptionDetailsList=[];
		var URI = BASE_URL + ROOT_URL+ "/api/packages/getPackageLog/"+id;
		BillingGenericService.serviceAction(METHOD_POST, URI,"",NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					if (response.data.status == "success")
						$scope.serviceConsumptionDetailsList = response.data.object;
					    // console.log("$scope.serviceConsumptionDetailsList",$scope.serviceConsumptionDetailsList);
				});
	}
	
	$scope.currentActivePackageServices = function(activePack){
		$scope.currentActivePackage = angular.copy(activePack);
	}
	
	$scope.addCurrentPackageServices = function(){
			angular.forEach($scope.currentActivePackage.listPackageConsumptionServiceDetailDto,function(value,index){
				if(value.isSelected){
					var obj = {
							basePrice:0,
							concession:0,
							discount:0,
							isDiscountApplicable:"N",
							isQuantityEditable:"N",
							isRateEditable:"N",
							isTaxApplicable:"N",
							maxRateEditable:0,
							minRateEditable:0,
							packageMasterId:$scope.currentActivePackage.packageId,
							serviceMasterId:value.serviceId,
							serviceStandardCode:value.serviceCode + " (P)",
							serviceStandardName:value.serviceStandardName,
							quantity:1,
							specialityId:value.specialityId,
							specialityName:value.specialityName,
							subSpecialityId:value.subSpecialityId,
							subSpecialityName:value.subSpecialityName,
							taxPercentage:0,
							discountPercentage : 0,
							isPackageService:'Y',
							apportionedPrice : value.apportionedPrice,
							packageId : $scope.currentActivePackage.packageId,
							packageTypeId : $scope.currentActivePackage.packageTypeId
							};
					$scope.servicesListPopup.push(obj);
				}
			});
	}
	
	$scope.init();
	$scope.getMedalDdetails();
	
	if (angular.equals({},$stateParams.newOpdBillObject)) {
		// $state.go('progressNote');
		// return;
	} else {
		$rootScope.startLoader();
		// console.log('$stateParams.newOpdBillObject',$stateParams.newOpdBillObject);
		$scope.newOpdBillObject = $stateParams.newOpdBillObject;
		if($scope.newOpdBillObject.billingMasterId != undefined && $scope.newOpdBillObject.billingMasterId > 0){
			$scope.preBillId = $scope.newOpdBillObject.billingMasterId;
		}
		if($scope.newOpdBillObject.billEditType == 1){
			$scope.isAddendumBill = 'Y';
		}else{
			$scope.isAddendumBill = 'N';
		}
		
		$scope.patientList = [];
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/"+$scope.newOpdBillObject.patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$rootScope.stopLoader();
			var newArr = {};
			newArr.age = response.data.object.age;
		    newArr.patientName = response.data.object.patientName;
		    newArr.firstName = response.data.object.fFirstName;
		    newArr.middleName=(response.data.object.middleName != null ?response.data.object.middleName :'' );
		    newArr.lastName= (response.data.object.lastName != null ?response.data.object.lastName :'' );
		    newArr.prefixId = response.data.object.prefixId;
			newArr.genderCode = response.data.object.genderCode;
			newArr.uhIdNumber = response.data.object.uhidnumber;
			newArr.patientId = response.data.object.patientId;
		    newArr.birthDate = response.data.object.dob;
		    newArr.genderId = response.data.object.genderId;
		    newArr.genderName = response.data.object.genderName;
		    newArr.mobileNo = response.data.object.mobileNumber;
		    newArr.cityName = response.data.object.cityName;
		    newArr.identificationNo = response.data.object.identificationNumber;
		    newArr.identificationName = response.data.object.identificationName;
			newArr.patientProfile = null;
			newArr.countryCallingCode = parseInt($scope.newOpdBillObject.countryCallingCode);
			newArr.patientCategoryId =	response.data.object.patientCategoryId;
			$scope.patientList.push(newArr);
			$scope.encounterDetails.encounterId = $scope.newOpdBillObject.encounterId;
			$scope.getPatientDetailsByIndex('0');
			$scope.getEncounterDetails($scope.newOpdBillObject.encounterId);
			$(".patienttSearchResult").toggle();
			
		});
		
	}
	
	/** Yogesh End * */
	
	$scope.getAuthorizationSelfUpdate = function(item,idx){
		
		if(item.selfPayable == '' || item.selfPayable == null || item.selfPayable == undefined){
			item.selfPayable = parseFloat(0);
		}
		
		
		
		if(parseFloat(item.selfPayable)  == parseFloat(item.selfPayableOldC)){
			return false;
		}
		
		if(parseFloat(item.selfPayable) > parseFloat(item.netPay)){
			growl.warning("You can't add more than net amount.",{
				title : ERROR_MSG
			});
			item.selfPayable = parseFloat(item.selfPayableOldC);
			return false;
		}
		
		if(parseFloat(item.selfPayable) == parseFloat(item.netPay)){
			
			item.creditPayable = parseFloat(0);
			item.selfPayableOldC = parseFloat(item.selfPayable);
			// item.orderDetailsPayeeMapperDtosList = [];
			angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value,index)
					 {
						if(value.payeeId != $scope.selfPayeeId){
							// item.orderDetailsPayeeMapperDtosList.splice(index,
							// 1);
							value.creditPayable = parseFloat(0);
							value.payeeAmount = parseFloat(0);
							value.spiltBillAmt = parseFloat(0);
						}
					 });
			
			$scope.calculateSplitBillTotal($scope.serviceList);
			return false;
			
		}
		
		
		if(item.orderDetailsPayeeMapperDtosList.length == 1){
			
			var creditAmount = parseFloat(item.netPay) - parseFloat(item.selfPayable);
			item.creditPayable = parseFloat(creditAmount);
			item.selfPayableOldC = parseFloat(item.selfPayable);
			if(parseFloat(creditAmount) > 0){
				
				$scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].selfPayable = (parseFloat($scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].selfPayable) - parseFloat(creditAmount)).toFixed(2);
				$scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].payeeAmount = (parseFloat($scope.serviceList[idx].orderDetailsPayeeMapperDtosList[0].payeeAmount) - parseFloat(creditAmount)).toFixed(2);
				var tmpListPayy = [];
				tmpListPayy = angular.copy($scope.serviceList[idx].orderDetailsPayeeMapperDtosList);
				$scope.serviceList[idx].orderDetailsPayeeMapperDtosList = [];
				var cooount = 1;
				angular.forEach($scope.patientSponsorDetailsDtosListSplit,function(value,index)
						 {
					
							if(value.priorityId == '1')
							{
								var newTmpArr = {};
								
								
								newTmpArr.associateCompanyDesc = value.insuranceDesc;
								newTmpArr.associateCompanyId = value.insuranceId;
								newTmpArr.coShare =  null;
								newTmpArr.contractId = value.contractId;
								newTmpArr.creditPayable = parseFloat(creditAmount);
								newTmpArr.id = null;
								newTmpArr.isInvoiceGenerated = "N";
								newTmpArr.isRateEditable = null;
								newTmpArr.isTariffRateApplicable= "N";
								newTmpArr.orderDetailsId = value.orderDetailsId;
								newTmpArr.organizationId = $scope.organizationId;
								newTmpArr.payeeAmount =  parseFloat(creditAmount);
								newTmpArr.payeeDesc =  value.companyDesc;
								newTmpArr.payeeId = value.payeeId;
								newTmpArr.selfPayable =  0;
								newTmpArr.serviceId = value.serviceMasterId;
								newTmpArr.spiltBillAmt = parseFloat(creditAmount);
								newTmpArr.status =  'A';
								newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
								newTmpArr.unitId = $scope.unitId;
								$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
								
								// $scope.payeeCreditDetailslListSplit.push(newTmpArr);
								
							}
							else{
								if(value.payeeId != $scope.selfPayeeId){
									var newTmpArr = {};
									newTmpArr.associateCompanyDesc = value.insuranceDesc;
									newTmpArr.associateCompanyId = value.insuranceId;
									newTmpArr.coShare =  null;
									newTmpArr.contractId = value.contractId;
									newTmpArr.creditPayable = parseFloat(0);
									newTmpArr.id = null;
									newTmpArr.isInvoiceGenerated = "N";
									newTmpArr.isRateEditable = null;
									newTmpArr.isTariffRateApplicable= "N";
									newTmpArr.orderDetailsId = value.orderDetailsId;
									newTmpArr.organizationId = $scope.organizationId;
									newTmpArr.payeeAmount =  parseFloat(0);
									newTmpArr.payeeDesc =  value.companyDesc;
									newTmpArr.payeeId = value.payeeId;
									newTmpArr.selfPayable =  0;
									newTmpArr.serviceId = value.serviceMasterId;
									newTmpArr.spiltBillAmt = parseFloat(0);
									newTmpArr.status =  'A';
									newTmpArr.orderDetailsId =  $scope.serviceList[idx].orderDetailsId;
									newTmpArr.unitId = $scope.unitId;
									$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(newTmpArr);
								}
							}
							
							if(cooount == $scope.patientSponsorDetailsDtosListSplit.length){
								angular.forEach(tmpListPayy,function(value,index)
										 {
											$scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
										 });
								$scope.calculateSplitBillTotal($scope.serviceList);
							}
							
							cooount++;
							
						});
				
			}
		}
		else{
			
			// alert(item.orderDetailsPayeeMapperDtosList.length);
			// //console.log("dd",item.orderDetailsPayeeMapperDtosList);
			
			var creditAmount = parseFloat(item.netPay) - parseFloat(item.selfPayable);
			item.creditPayable = parseFloat(creditAmount);
			
			if(parseFloat(item.selfPayable) > parseFloat(item.selfPayableOldC)){
				var tmpSelf = parseFloat(item.selfPayable) - parseFloat(item.selfPayableOldC);
				if(parseFloat(item.orderDetailsPayeeMapperDtosList[0].creditPayable) >= parseFloat(tmpSelf)){
					item.orderDetailsPayeeMapperDtosList[0].creditPayable = parseFloat(item.orderDetailsPayeeMapperDtosList[0].creditPayable) - parseFloat(tmpSelf);
					item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = parseFloat(item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt) - parseFloat(tmpSelf);
					item.orderDetailsPayeeMapperDtosList[0].payeeAmount = parseFloat(item.orderDetailsPayeeMapperDtosList[0].payeeAmount) - parseFloat(tmpSelf);
					item.selfPayableOldC = item.selfPayable;
					
					angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value1,index)
							 {
								// $scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
								if(value1.payeeId == $scope.selfPayeeId){
									
									value1.creditPayable = parseFloat(0);
									value1.spiltBillAmt =  parseFloat(0);
									value1.selfPayable = (parseFloat(value1.selfPayable) +parseFloat(tmpSelf)).toFixed(2);
									value1.payeeAmount = (parseFloat(value1.payeeAmount) +parseFloat(tmpSelf)).toFixed(2);
									
								}
								
							 });
					
					$scope.calculateSplitBillTotal($scope.serviceList);
				}else{
					
					if(item.orderDetailsPayeeMapperDtosList.length == 2){
						
						item.orderDetailsPayeeMapperDtosList[0].creditPayable = parseFloat(0);
						item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = parseFloat(0);
						item.orderDetailsPayeeMapperDtosList[0].payeeAmount =parseFloat(0);
						item.selfPayableOldC = item.selfPayable;
						
						angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value1,index)
								 {
									// $scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
									if(value1.payeeId == $scope.selfPayeeId){
										
										value1.creditPayable = parseFloat(0);
										value1.spiltBillAmt =  parseFloat(0);
										value1.selfPayable = (parseFloat(value1.selfPayable) +parseFloat(tmpSelf)).toFixed(2);
										value1.payeeAmount = (parseFloat(value1.payeeAmount) +parseFloat(tmpSelf)).toFixed(2);
										
									}
									
								 });
						
						$scope.calculateSplitBillTotal($scope.serviceList);
					}
					
					if(item.orderDetailsPayeeMapperDtosList.length > 2){
						var tmpSelfTmp = parseFloat(tmpSelf);
						var count = 1;
						angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value,index)
								 {
									// $scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
									if(value.payeeId != $scope.selfPayeeId){
										if(parseFloat(tmpSelfTmp) > 0 && parseFloat(value.creditPayable) > 0){
											if(parseFloat(tmpSelfTmp) > parseFloat(value.creditPayable)){
												tmpSelfTmp = parseFloat(tmpSelfTmp) - parseFloat(value.creditPayable);
												value.creditPayable = parseFloat(0);
												value.spiltBillAmt =   parseFloat(0);
												value.payeeAmount = parseFloat(0);
												
											}else{
											
												// value.selfPayable =
												// parseFloat(value.selfPayable)
												// + parseFloat(tmpSelfTmp);
												// value.payeeAmount =
												// parseFloat(value.payeeAmount)+
												// parseFloat(tmpSelfTmp);
												value.creditPayable = (parseFloat(value.creditPayable) - parseFloat(tmpSelfTmp)).toFixed(2);
												value.spiltBillAmt =  (parseFloat(value.spiltBillAmt) - parseFloat(tmpSelfTmp)).toFixed(2);
												value.payeeAmount =(parseFloat(value.payeeAmount) - parseFloat(tmpSelfTmp)).toFixed(2);
												value.selfPayable = parseFloat(0);
												tmpSelfTmp = parseFloat(0);
												
											}
										
										}
										
									}
									if(count == item.orderDetailsPayeeMapperDtosList.length){
										item.selfPayableOldC = item.selfPayable;
										$scope.calculateSplitBillTotal($scope.serviceList);
									}
									count++;
								 });
						
					}
				}
				
				 
			}
			if(parseFloat(item.selfPayable) < parseFloat(item.selfPayableOldC)){
				var tmpSelf = parseFloat(item.selfPayableOldC) - parseFloat(item.selfPayable);
					item.orderDetailsPayeeMapperDtosList[0].creditPayable = parseFloat(item.orderDetailsPayeeMapperDtosList[0].creditPayable) + parseFloat(tmpSelf);
					item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = parseFloat(item.orderDetailsPayeeMapperDtosList[0].spiltBillAmt)+ parseFloat(tmpSelf);
					item.orderDetailsPayeeMapperDtosList[0].payeeAmount = parseFloat(item.orderDetailsPayeeMapperDtosList[0].payeeAmount) + parseFloat(tmpSelf);
					item.selfPayableOldC = item.selfPayable;
					angular.forEach(item.orderDetailsPayeeMapperDtosList,function(value1,index)
							 {
								// $scope.serviceList[idx].orderDetailsPayeeMapperDtosList.push(value);
								if(value1.payeeId == $scope.selfPayeeId){
									
									value1.creditPayable = parseFloat(0);
									value1.spiltBillAmt =  parseFloat(0);
									value1.selfPayable = parseFloat(value1.selfPayable) -parseFloat(tmpSelf);
									value1.payeeAmount = parseFloat(value1.payeeAmount) -parseFloat(tmpSelf);
									
								}
								
							 });
					
					$scope.calculateSplitBillTotal($scope.serviceList);
				 
			}
			
			// item.orderDetailsPayeeMapperDtosList
			
		}
		
	}
	
	
    $scope.getAuthorizationCreditUpdate = function(item){
		// serviceList
	}
    
    
    $scope.selfSlitBillAmount =false;
	$scope.getAuthorizationSelfUpdateTotal = function(selfVal){
		$scope.selfSlitBillAmount =true;
        $scope.newSelfVal = selfVal;
		if(parseFloat(selfVal) > parseFloat($scope.totalSelfPaybleSplitBillOld)){
				var diffSelf = parseFloat(selfVal) - parseFloat($scope.totalSelfPaybleSplitBillOld);
				if(parseFloat(diffSelf) > parseFloat($scope.totalCreditPaybleSplitBill)){
					growl.warning("You can't add more than net amount.("+parseFloat($scope.totalNetAmt)+").",{
						title : WARNING_MSG
					});
					$scope.calculateSplitBillTotal($scope.serviceList);
					//return false;
				}else{
					
					$scope.co = 1;
					angular.forEach($scope.serviceList,function(value,index)
							 {
								 if(parseFloat(value.netPay) > parseFloat(value.selfPayable)){
						 if(parseFloat(value.netPay) > parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0){
							     // value.selfPayable =  parseFloat(value.selfPayable) + parseFloat(diffSelf);
											 if( parseFloat(value.creditPayable) > parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
												 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(diffSelf)).toFixed(2);
												 value.creditPayable =  (parseFloat(value.creditPayable) - parseFloat(diffSelf)).toFixed(2);
												 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
												 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
												 var diffSelfTmp = parseFloat(diffSelf);
												 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
														 {
													 		if(value1.payeeId == $scope.selfPayeeId){
													 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(diffSelfTmp)).toFixed(2); 
													 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(diffSelfTmp)).toFixed(2);
													 		}else{
													 			if(parseFloat(value1.creditPayable) > parseFloat(diffSelf)){
													 				value1.creditPayable = (parseFloat(value1.creditPayable) - parseFloat(diffSelf)).toFixed(2);
														 			value1.payeeAmount = (parseFloat(value1.payeeAmount) -  parseFloat(diffSelf)).toFixed(2);
														 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) - parseFloat(diffSelf)).toFixed(2);
														 			diffSelf = parseFloat(0);
													 			}else{
													 				value1.creditPayable = parseFloat(0);
														 			value1.payeeAmount = parseFloat(0);
														 			value1.spiltBillAmt = parseFloat(0);
														 			diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
													 			}
													 		}
													 
														 });
											 }
											 
											 
                                                 if( parseFloat(value.creditPayable) <= parseFloat(diffSelf) && parseFloat(diffSelf) > 0){

												 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
												 diffSelf = parseFloat(diffSelf) - parseFloat(value.creditPayable);
												// value.creditPayable =  parseFloat(0);
												 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
												 var countList = 1;
												 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
														 {
													 		if(value1.payeeId == $scope.selfPayeeId){
													 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(value.creditPayable)).toFixed(2);
													 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
													 		}else{
													 			
													 				value1.creditPayable = parseFloat(0);
														 			value1.payeeAmount = parseFloat(0);
														 			value1.spiltBillAmt = parseFloat(0);
														 			value1.selfPayable = parseFloat(0);
													 			
													 		}
													 		if(value.orderDetailsPayeeMapperDtosList.length == countList){
													 			value.creditPayable =  parseFloat(0);
													 			 value.creditPayableOldC = parseFloat(value.creditPayable);
													 		}
													 		countList ++;
													 
														 });
											 }
										 
									 }
						 if(parseFloat(value.netPay) < parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0){
						     // value.selfPayable =
								// parseFloat(value.selfPayable) +
								// parseFloat(diffSelf);
										 if( parseFloat(value.creditPayable) > parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
											 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(diffSelf)).toFixed(2);
											 value.creditPayable =  (parseFloat(value.creditPayable) - parseFloat(diffSelf)).toFixed(2);
											 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
											 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
											 var diffSelfTmp = parseFloat(diffSelf);
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId == $scope.selfPayeeId){
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(diffSelfTmp)).toFixed(2); 
												 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(diffSelfTmp)).toFixed(2);
												 		}else{
												 			if(parseFloat(value1.creditPayable) > parseFloat(diffSelf)){
												 				value1.creditPayable = (parseFloat(value1.creditPayable) - parseFloat(diffSelf)).toFixed(2);
													 			value1.payeeAmount = (parseFloat(value1.payeeAmount) -  parseFloat(diffSelf)).toFixed(2);
													 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) - parseFloat(diffSelf)).toFixed(2);
													 			diffSelf = parseFloat(0);
												 			}else{
												 				value1.creditPayable = parseFloat(0);
													 			value1.payeeAmount = parseFloat(0);
													 			value1.spiltBillAmt = parseFloat(0);
													 			diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
												 			}
												 		}
												 
													 });
										 }
										 
										 
                                         if( parseFloat(value.creditPayable) <= parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
											 value.selfPayable =  (parseFloat(value.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
											 diffSelf = parseFloat(diffSelf) - parseFloat(value.creditPayable);
											// value.creditPayable =
											// parseFloat(0);
											 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
											 var countList = 1;
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId == $scope.selfPayeeId){
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(value.creditPayable)).toFixed(2);
												 			value1.selfPayable = (parseFloat(value1.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
												 		}else{
												 			
												 				value1.creditPayable = parseFloat(0);
													 			value1.payeeAmount = parseFloat(0);
													 			value1.spiltBillAmt = parseFloat(0);
													 			value1.selfPayable = parseFloat(0);
												 			
												 		}
												 		if(value.orderDetailsPayeeMapperDtosList.length == countList){
												 			value.creditPayable =  parseFloat(0);
												 			 value.creditPayableOldC = parseFloat(value.creditPayable);
												 		}
												 		countList ++;
												 
													 });
										 }
									 
								 }
								 }
								 
								 
								 if($scope.serviceList.length == $scope.co){
									 $scope.calculateSplitBillTotal($scope.serviceList);
								 }
								 $scope.co ++;
							 });
				}
		}
		
		if(parseFloat($scope.totalSelfPaybleSplitBillOld) > parseFloat(selfVal)){
			var diffSelf =  parseFloat($scope.totalSelfPaybleSplitBillOld) - parseFloat(selfVal);
			$scope.co = 1;
			angular.forEach($scope.serviceList,function(value,index)
					 {
							if(value.orderDetailsPayeeMapperDtosList.length > 1){
                                if(parseFloat(diffSelf) >= parseFloat(value.selfPayable) && parseFloat(diffSelf) > 0){
									 diffSelf = parseFloat(diffSelf) - parseFloat(value.selfPayable);
									 value.creditPayable = (parseFloat(value.creditPayable) + parseFloat(value.selfPayable)).toFixed(2);
									 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
									 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
									 var coChild = 1;
									 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
											 {
										 		if(value1.payeeId != $scope.selfPayeeId){
										 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(value.selfPayable)).toFixed(2);
										 			value1.payeeAmount = (parseFloat(value1.payeeAmount) +  parseFloat(value.selfPayable)).toFixed(2);
										 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(value.selfPayable)).toFixed(2);
										 			value.selfPayable = parseFloat(0);
										 			 value.selfPayableOldC = parseFloat(value.selfPayable);
										 		}else{
										 			value1.payeeAmount = parseFloat(0);
										 			value1.selfPayable = parseFloat(0);
										 			value1.spiltBillAmt = parseFloat(0);
										 			value1.creditPayable = parseFloat(0);
										 		}
										 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
										 			{
										 				value.selfPayable = parseFloat(0);
										 				 value.selfPayableOldC = parseFloat(value.selfPayable);
										 			}
										 		coChild++;
										 
											 });

								 }
								 
                                 if(parseFloat(diffSelf) == 0 && parseFloat($scope.newSelfVal) != parseFloat($scope.totalSelfPaybleSplitBill)){
									 
	                    	         value.creditPayable = (parseFloat(value.creditPayable) + parseFloat(value.selfPayable));
									 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
									 value.selfPayable = 0.00;
									 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
									 value.selfPayable = parseFloat(diffSelf);
									 
									 var coChild = 1;
									 var diffSelfTmp1 =parseFloat(diffSelf); 
									 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
											 {
										 		if(value1.payeeId != $scope.selfPayeeId){
										 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(diffSelf)).toFixed(2);
										 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(diffSelf)).toFixed(2);
										 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(diffSelf)).toFixed(2);
										 			diffSelf = parseFloat(diffSelf) - parseFloat(diffSelf);
										 		}else{
										 			value1.payeeAmount = (parseFloat(value1.payeeAmount) - parseFloat(diffSelfTmp1)).toFixed(2);
										 			value1.selfPayable = (parseFloat(value1.selfPayable)- parseFloat(diffSelfTmp1)).toFixed(2);
										 			value1.creditPayable = parseFloat(0);
										 		}
										 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
										 			{
										 			   diffSelf = parseFloat(0);
										 			}
										 		coChild++;
										 
											 });
									 
								 }
								 
								 if(parseFloat(diffSelf) < parseFloat(value.selfPayable)){
									// diffSelf = parseFloat(0);
									 value.creditPayable = (parseFloat(value.creditPayable) + parseFloat(diffSelf)).toFixed(2);
									 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
									 value.selfPayable = (parseFloat(value.selfPayable) - parseFloat(diffSelf)).toFixed(2);
									 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
									 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
									 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
									 var coChild = 1;
									 var diffSelfTmp1 =parseFloat(diffSelf); 
									 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
											 {
										 		if(value1.payeeId != $scope.selfPayeeId){
										 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(diffSelf)).toFixed(2);
										 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(diffSelf)).toFixed(2);
										 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(diffSelf)).toFixed(2);
										 			diffSelf = parseFloat(diffSelf) - parseFloat(diffSelf);
										 		}else{
										 			value1.payeeAmount = (parseFloat(value1.payeeAmount) - parseFloat(diffSelfTmp1)).toFixed(2);
										 			value1.selfPayable = (parseFloat(value1.selfPayable)- parseFloat(diffSelfTmp1)).toFixed(2);
										 			value1.creditPayable = parseFloat(0);
										 		}
										 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
										 			{
										 			   diffSelf = parseFloat(0);
										 			}
										 		coChild++;
										 
											 });

								 }
							}
						
//						 if($scope.serviceList.length == co){
						
							 $scope.calculateSplitBillTotal($scope.serviceList);
//						 }
							 $scope.co++;
					 });
			
		}
	}
	
	
	$scope.creditSlitBillAmount = false;
    $scope.getAuthorizationCreditUpdateTotal = function(creditVal){
    	$scope.creditSlitBillAmount = true;
    	if(parseFloat(creditVal) > parseFloat($scope.totalCreditPaybleSplitBillOld)){
    		var diffSelf = parseFloat(creditVal) - parseFloat($scope.totalCreditPaybleSplitBillOld);
    		
			if(parseFloat(diffSelf) > parseFloat($scope.totalSelfPaybleSplitBill)){
				growl.warning("You can't add more than net amount.("+parseFloat($scope.totalNetAmt)+").",{
					title : WARNING_MSG
				});
				$scope.calculateSplitBillTotal($scope.serviceList);
				//return false;
			}else{
				$scope.co = 1;
				
				angular.forEach($scope.serviceList,function(value,index)
						 {
					 if(parseFloat(value.netPay) > parseFloat(value.creditPayable)){
						 if(parseFloat(value.netPay) >= parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0){
											 if( parseFloat(value.selfPayable) >= parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
												 var tmpDiffSelf = parseFloat(diffSelf);
												 value.creditPayable =  parseFloat(value.creditPayable) + parseFloat(diffSelf);
												 value.selfPayable =  parseFloat(value.selfPayable) - parseFloat(diffSelf);
												 value.creditPayable =  parseFloat(value.creditPayable).toFixed(2);
												 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
												 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
												 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
												 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
														 {
													 		if(value1.payeeId != $scope.selfPayeeId){
													 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(diffSelf)).toFixed(2); 
													 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(diffSelf)).toFixed(2);
													 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(diffSelf)).toFixed(2);
													 			diffSelf = parseFloat(0);
													 		}else{
													 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  - parseFloat(tmpDiffSelf)).toFixed(2);
													 			value1.selfPayable = (parseFloat(value1.selfPayable) - parseFloat(tmpDiffSelf)).toFixed(2);
													 			//tmpDiffSelf
													 		}
													 
														 });
											 }
											 
											 
											 if( parseFloat(value.selfPayable) < parseFloat(diffSelf) && parseFloat(diffSelf) > 0){
												 value.creditPayable =  parseFloat(value.creditPayable) + parseFloat(value.selfPayable);
												 diffSelf = parseFloat(diffSelf) - parseFloat(value.selfPayable);
												 value.creditPayable = parseFloat(value.creditPayable).toFixed(2)
												 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
												 
												// value.creditPayable =  parseFloat(0);
												 
												 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
														 {
													 		if(value1.payeeId == $scope.selfPayeeId){
													 			value1.payeeAmount = parseFloat(0); 
													 			value1.selfPayable = parseFloat(0);
													 		}
														 });
												 
												 value.orderDetailsPayeeMapperDtosList[0].creditPayable = (parseFloat( value.orderDetailsPayeeMapperDtosList[0].creditPayable)+parseFloat(value.selfPayable)).toFixed(2);
												 value.orderDetailsPayeeMapperDtosList[0].payeeAmount = (parseFloat( value.orderDetailsPayeeMapperDtosList[0].payeeAmount)+parseFloat(value.selfPayable)).toFixed(2);
												 value.orderDetailsPayeeMapperDtosList[0].spiltBillAmt = (parseFloat( value.orderDetailsPayeeMapperDtosList[0].spiltBillAmt)+parseFloat(value.selfPayable)).toFixed(2);
												 value.selfPayable =  parseFloat(0);
												 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
											
											 }
										 
									 }else{
										 if(parseFloat(value.netPay) <= parseFloat(diffSelf) && value.orderDetailsPayeeMapperDtosList.length > 1 && parseFloat(diffSelf) > 0 && parseFloat(value.selfPayable) > 0){
											 value.creditPayable = parseFloat(value.creditPayable) + parseFloat(value.selfPayable);
											 value.creditPayable = parseFloat(value.creditPayable).toFixed(2)
											 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
											 diffSelf = parseFloat(diffSelf) - parseFloat(value.selfPayable);
											 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
													 {
												 		if(value1.payeeId != $scope.selfPayeeId && parseFloat(value.selfPayable) > 0){
												 			value1.payeeAmount = (parseFloat(value1.payeeAmount)  + parseFloat(value.selfPayable)).toFixed(2);
												 			value1.creditPayable = (parseFloat(value1.creditPayable) + parseFloat(value.selfPayable)).toFixed(2);
												 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) + parseFloat(value.selfPayable)).toFixed(2);
												 			value.selfPayable =  parseFloat(0);
												 			value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
												 		}else{
												 			value1.payeeAmount = parseFloat(0); 
												 			value1.selfPayable = parseFloat(0);
												 			//tmpDiffSelf
												 		}
												 
													 });
										 }
										 }
								 }
								 if($scope.serviceList.length == $scope.co){
									 $scope.calculateSplitBillTotal($scope.serviceList);
								 }
								 $scope.co++;
						 });
				
			}
    	}
    	
    	if(parseFloat($scope.totalCreditPaybleSplitBillOld) > parseFloat(creditVal)){
            $scope.newCreditVal = creditVal;
			var diffSelf =  parseFloat($scope.totalCreditPaybleSplitBillOld) - parseFloat(creditVal);
			$scope.co = 1;
			angular.forEach($scope.serviceList,function(value,index)
					 {
							if(value.orderDetailsPayeeMapperDtosList.length > 1){
								 if(parseFloat(diffSelf) >= parseFloat(value.creditPayable)  && parseFloat(diffSelf) > 0){
									 diffSelf = parseFloat(diffSelf) - parseFloat(value.creditPayable);
									 value.selfPayable = parseFloat(value.selfPayable) + parseFloat(value.creditPayable);
									 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
									 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
									 var coChild = 1;
									 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
											 {
										 		if(value1.payeeId != $scope.selfPayeeId){
										 			value1.creditPayable =  parseFloat(0);;
										 			value1.payeeAmount =  parseFloat(0);;
										 			value1.spiltBillAmt =  parseFloat(0);;
										 		}else{
										 			value1.selfPayable =(parseFloat(value.selfPayable) + parseFloat(value.creditPayable)).toFixed(2);
										 			value1.payeeAmount = (parseFloat(value.payeeAmount) + parseFloat(value.creditPayable)).toFixed(2);
										 		}
										 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
										 			{
										 				value.creditPayable = parseFloat(0);
										 				 value.creditPayableOldC = parseFloat(0);
										 			}
										 		coChild++;
										 
											 });

								 }
                                 if(parseFloat(diffSelf) == 0 && parseFloat($scope.newCreditVal) != parseFloat($scope.totalCreditPaybleSplitBill)){
									 
									 value.selfPayable = (parseFloat(value.selfPayable) + parseFloat(value.creditPayable));
									 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
									 value.creditPayable = 0.00;
									 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
									 value.creditPayable = parseFloat(diffSelf);
									 
									 
									 var coChild = 1;
									 var tmpdd = parseFloat(diffSelf);
									 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
											 {
										 		if(value1.payeeId != $scope.selfPayeeId){
										 			if(parseFloat(value1.creditPayable) >= parseFloat(diffSelf)){
										 				
										 				value1.creditPayable = 0;
											 			value1.payeeAmount =0;
											 			value1.spiltBillAmt = 0;
											 			diffSelf = parseFloat(0);
										 			}else{
										 				diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
										 				value1.creditPayable = parseFloat(0);
											 			value1.payeeAmount = parseFloat(0);
											 			value1.spiltBillAmt = parseFloat(0);
											 			
										 			}
										 		}else{
										 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(tmpdd)).toFixed(2);
										 			value1.selfPayable = (parseFloat(value1.selfPayable)+ parseFloat(tmpdd)).toFixed(2);
										 			value1.creditPayable = parseFloat(0);
										 		}
										 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
										 			{
										 			   diffSelf = parseFloat(0);
										 			}
										 		coChild++;
										 
											 });
									 
								 }
								 
								 if(parseFloat(diffSelf) < parseFloat(value.creditPayable)){
									 
									// diffSelf = parseFloat(0);
									 value.selfPayable = parseFloat(value.selfPayable) + parseFloat(diffSelf);
									 value.selfPayable = parseFloat(value.selfPayable).toFixed(2);
									 value.creditPayable = parseFloat(value.creditPayable) - parseFloat(diffSelf);
									 value.creditPayable = parseFloat(value.creditPayable).toFixed(2);
									 
									 value.selfPayableOldC = parseFloat(value.selfPayable).toFixed(2);
									 value.creditPayableOldC = parseFloat(value.creditPayable).toFixed(2);
									 
									 
									 var coChild = 1;
									 var tmpdd = parseFloat(diffSelf);
									 angular.forEach(value.orderDetailsPayeeMapperDtosList,function(value1,index1)
											 {
										 		if(value1.payeeId != $scope.selfPayeeId){
										 			if(parseFloat(value1.creditPayable) >= parseFloat(diffSelf)){
										 				
										 				value1.creditPayable = (parseFloat(value1.creditPayable) - parseFloat(diffSelf)).toFixed(2);
											 			value1.payeeAmount = (parseFloat(value1.payeeAmount) - parseFloat(diffSelf)).toFixed(2);
											 			value1.spiltBillAmt = (parseFloat(value1.spiltBillAmt) - parseFloat(diffSelf)).toFixed(2);
											 			diffSelf = parseFloat(0);
										 			}else{
										 				diffSelf = parseFloat(diffSelf) - parseFloat(value1.creditPayable);
										 				value1.creditPayable = parseFloat(0);
											 			value1.payeeAmount = parseFloat(0);
											 			value1.spiltBillAmt = parseFloat(0);
											 			
										 			}
										 		}else{
										 			value1.payeeAmount = (parseFloat(value1.payeeAmount) + parseFloat(tmpdd)).toFixed(2);
										 			value1.selfPayable = (parseFloat(value1.selfPayable)+ parseFloat(tmpdd)).toFixed(2);
										 			value1.creditPayable = parseFloat(0);
										 		}
										 		if(value.orderDetailsPayeeMapperDtosList.length == coChild)
										 			{
										 			   diffSelf = parseFloat(0);
										 			}
										 		coChild++;
										 
											 });

								 }
							}
						
//						 if($scope.serviceList.length == co){
						
							 $scope.calculateSplitBillTotal($scope.serviceList);
//						 }
					    $scope.co++;
					 });
			
		}
    	
	}
    
    
    $scope.getCreditAmountForPayee = function(item,idx){
    	
    	if(item.creditPayable == '' || item.creditPayable == null || item.creditPayable == undefined){
			item.creditPayable = parseFloat(0);
		}
    	// $scope.payeeeIdxService
    	// serviceList
    	
    	
    	// $scope.payeeCreditDetailslListSplit
    	var totalCreditAmt = parseFloat(0);
    	angular.forEach($scope.payeeCreditDetailslListSplit,function(value,index)
				 {
    					totalCreditAmt = parseFloat(totalCreditAmt) + parseFloat(value.creditPayable);
				 });
    	
    	var totalNetTmp = parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) + parseFloat(totalCreditAmt);
    	
		/*
		 * if(parseFloat(totalNetTmp) >
		 * parseFloat($scope.serviceList[$scope.payeeeIdxService].netPay)){
		 * 
		 * growl.warning("You can't add more then net amount.",{ title :
		 * ERROR_MSG }); item.creditPayable = parseFloat(item.spiltBillAmt);
		 * return false; }else{ item.spiltBillAmt = parseFloat(totalCreditAmt);
		 * var selfAmountTmp =
		 * parseFloat($scope.serviceList[$scope.payeeeIdxService].netPay) -
		 * parseFloat(totalCreditAmt);
		 * $scope.serviceList[$scope.payeeeIdxService].selfPayable =
		 * parseFloat(selfAmountTmp);
		 * $scope.serviceList[$scope.payeeeIdxService].creditPayable =
		 * parseFloat(totalCreditAmt);
		 * $scope.calculateSplitBillTotal($scope.serviceList); }
		 */
    	
    	
    	if(parseFloat(item.creditPayable) > parseFloat(item.spiltBillAmt)){
    		var tmpCreDiff = parseFloat(item.creditPayable) - parseFloat(item.spiltBillAmt);
    		if(parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) >= parseFloat(tmpCreDiff)){
    			$scope.serviceList[$scope.payeeeIdxService].selfPayable  = (parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) - parseFloat(tmpCreDiff)).toFixed(2);
    			$scope.serviceList[$scope.payeeeIdxService].selfPayableOldC  =  $scope.serviceList[$scope.payeeeIdxService].selfPayable;
    			item.spiltBillAmt = parseFloat(item.creditPayable);
    			item.payeeAmount = parseFloat(item.creditPayable);
    			 $scope.serviceList[$scope.payeeeIdxService].creditPayable = parseFloat($scope.serviceList[$scope.payeeeIdxService].creditPayable) + parseFloat(tmpCreDiff);
    			 
                 angular.forEach($scope.serviceList[$scope.payeeeIdxService].orderDetailsPayeeMapperDtosList,function(value,index)
                         {
                            if(value.payeeId == $scope.selfPayeeId){
                                // item.orderDetailsPayeeMapperDtosList.splice(index,
								// 1);
                                value.creditPayable = parseFloat(0);
                                value.spiltBillAmt = parseFloat(0);
                                value.selfPayable = (parseFloat(value.selfPayable) - parseFloat(tmpCreDiff)).toFixed(2);
                                value.payeeAmount = parseFloat(value.selfPayable);
                            }
                         });

    			 
    			 $scope.calculateSplitBillTotal($scope.serviceList);
    		}else{
    			growl.warning("You can add more upto "+parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable),{
					title : ERROR_MSG
				});
				item.creditPayable = parseFloat(item.spiltBillAmt);
				return false;
    		}
    	}else{
    		var tmpCreDiff = parseFloat(item.spiltBillAmt) - parseFloat(item.creditPayable);
    		$scope.serviceList[$scope.payeeeIdxService].selfPayable  = (parseFloat($scope.serviceList[$scope.payeeeIdxService].selfPayable) + parseFloat(tmpCreDiff)).toFixed(2);
    		$scope.serviceList[$scope.payeeeIdxService].creditPayable = (parseFloat($scope.serviceList[$scope.payeeeIdxService].creditPayable) - parseFloat(tmpCreDiff)).toFixed(2);
    		$scope.serviceList[$scope.payeeeIdxService].selfPayableOldC  =  $scope.serviceList[$scope.payeeeIdxService].selfPayable;
			 item.spiltBillAmt = parseFloat(item.creditPayable);
			 
             item.payeeAmount = parseFloat(item.creditPayable);
             angular.forEach($scope.serviceList[$scope.payeeeIdxService].orderDetailsPayeeMapperDtosList,function(value,index)
                     {
                        if(value.payeeId == $scope.selfPayeeId){
                            // item.orderDetailsPayeeMapperDtosList.splice(index,
							// 1);
                            value.creditPayable = parseFloat(0);
                            value.spiltBillAmt = parseFloat(0);
                            value.selfPayable = (parseFloat(value.selfPayable) +  parseFloat(tmpCreDiff)).toFixed(2);
                            value.payeeAmount = parseFloat(value.selfPayable);
                        }
                     });


             
			 $scope.calculateSplitBillTotal($scope.serviceList);
    	}
    	
    }
    
	$scope.ServiceCount=0;
	$scope.searchOrderServiceByName = function(searchKeyword){
		var param = {encounterId:$scope.encounterDetails.encounterId,visitTypeId:$scope.visitTypeId,searchKeyword:searchKeyword};// visit
																																	// type
																																	// IP
																																	// default
		var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
		BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				$scope.autoRenderedServicesList = [];
				$scope.autoRenderedServicesList = response.data.listObject;
				//ServiceCount
				if($scope.autoRenderedServicesList != null){
					$scope.ServiceCount = $scope.autoRenderedServicesList.length+1;
				}
				
				var indexT = response.data.listObject.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
				
			
			    if (indexT >= 0) {
			    	$scope.isPatientClass = "Y";
			    }
			    
				$scope.calculationTest($scope.autoRenderedServicesList,init);
				$scope.loadingServiceFlag = false;
				  angular.element('#addServicePopup').modal('hide');
				  angular.element('#serviceCancellationModal').modal('hide');
				  angular.element('#packageForceAdd').modal('hide');
				  $rootScope.stopLoader();
				  $rootScope.stopLoaderPopup();
			}
			else if(response.data.status == "error")
			{
				 $rootScope.stopLoader();
				 $rootScope.stopLoaderPopup();
				}
		});	
		$scope.getListPackageOrder();
	}
	
	
	$scope.thumbSearch = function()
	{
		 try {
			    $rootScope.startLoader();
	           	var quality = 60; // (1 to 100) (recommanded minimum 55)
	               var timeout = 10; // seconds (minimum=10(recommanded),
										// maximum=60, unlimited=0 )
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
		           				$scope.patientList = response.data.listObject;
		           				
		           				$scope.patientEncounterList = [];
		           				
		           				$scope.encounterDetails = {
		           						encounterId : "",
		           						encounterDateString : "",
		           						referralDesc : "",
		           						specialityName : "",
		           						doctorName : "",
		           						patientCategory : "",
		           						paymentEntitlementDesc : "",
		           						companyDesc : "",
		           						paymentEntitlementTypeId : "",
		           						payeeId : "",
		           						patientId : "",
		           						doctorId : "",
		           				}
		           				$scope.autoRenderedServicesFinalList = [];
		           				$scope.autoRenderedServicesForSaveAndUpdate=[];
		           				$scope.patientDetails ={};
		           				
		           				$scope.totalAmountSummary = "0.00";
		           				$scope.totalConcessionSummary = "0.00";
		           				$scope.netPayableSummary = "0.00";
		           				$scope.totalDiscountSummary = "0.00";
		           				$scope.totalGstTaxSummary = "0.00";
		           				
		           				$scope.totalCreditPayble = "0.00";
		           				$scope.totalSelfPayble = "0.00";
	           				
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
//				{

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
//					requestedUniqueId :$scope.requestedUniqueId
			}
			
			
			var URI = BASE_URL + ROOT_URL + "/api/updatePOSMachineDetails";
			BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				
				if (response.data.status == "success")
					{
						console.log("saved Card Details");

						if(response.data.object != null && response.data.object.listObject.length > 0){
							$scope.getResposeList=response.data.object.listObject;
							$scope.getGeneratedResponse = JSON.parse($scope.getResposeList[0].generatedReponse);
							//$scope.paymentDetails.credCardHolderName=$scope.getGeneratedResponse.cardHolderName;
//							$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
$scope.paymentDetails.credCardHolderName=$scope.getGeneratedResponse.cardHolderName != undefined ? $scope.getGeneratedResponse.cardHolderName : "upi" ;
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
								else{
									$scope.paymentDetails.credCardTypeId= 1;
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
									// console.log("resString",$scope.resString);
									
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
												// console.log("saved Card
												// Details");
											    $scope.addPaymentDetails();
											}
										else if(response.data.status == "error"){
											
											growl.error("Something wrong with save data of card..",{
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
						 	} else{
							 
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
	
	
	  $scope.getUsersForFinancialClearanceRights =function() {
		  var URI = BASE_URL + ROOT_URL +
		  "/billing/getUsersListHavingFinancialClearanceRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.netPayableSummary;
		  BillingGenericService.serviceAction(METHOD_GET,URI, {} ,
		  NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
		  //console.log(response)
			  if(response.data.status == 'success'){
		          $scope.getUsersForFinancialClearanceList = response.data.listObject;
		    }
		  
		    });
	  }
	 
	
	 $scope.checkFinancialClearance = function() 
	 {
		if($scope.isFinancialClearence=='Y')
		{
			if(($scope.userDiscountObject != null &&  parseFloat($scope.userDiscountObject.fromFinancialClearanceAmount) <= $scope.netPayableSummary && parseFloat($scope.userDiscountObject.toFinancialClearanceAmount) >=  $scope.netPayableSummary) )
	    	{
				 $scope.financialFlag = 'Y';
				 $scope.cashSettlement = 'N';
	    	}else{
	    		$scope.financialFlag = 'N';
	    		$scope.cashSettlement = 'N';
//	    		$scope.isFinancialClearence ='N'
	    		if($scope.userDiscountObject != null){
    			growl.warning("User have rights for approving financial clearance from "+$scope.userDiscountObject.fromFinancialClearanceAmount+" to "+$scope.userDiscountObject.toFinancialClearanceAmount+"..!!",{
					title : WARNING_MSG
				});
	    		}
	    	}
		    
			  $scope.getUsersForFinancialClearanceRights(); 
			  $scope.finalBill ='Y';
			 
		}
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
							// paymentCollFactory.addPaymentDetails($scope);
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
					  "name":$scope.patientDetails.patientName,
					  "patientId": $scope.patientDetails.patientId, 
					  "createdBy": $scope.userId, 
					  "updatedBy": $scope.userId
					  
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
	


	$scope.addImageDetailsForContract = function(obj)
	{
	var encounterDetails = angular.copy($scope.encounterDetailsContract);
	encounterDetails.patientId = $scope.patientDetails.patientId;
	// insurance
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
	
	
	$scope.getCashCollectedAmount = function(encounterId){

		$rootScope.startLoader();
			var data = {
					  "encounterId" : encounterId
			}
			var URI = BASE_URL + ROOT_URL + "/api/billing/cashPaidAmount";
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				{
						$rootScope.stopLoader();
						$scope.cashCollectedAmount = response.data.object;
				} else if(response.data.status == 'error')
				{
					$rootScope.stopLoader();
					$scope.cashCollectedAmount = 0;
				}
	
			});
		
	}
	
	
	// Search Surgen with Typehead
	$scope.searchGroupSurgen = function(keyword) {

		 if(keyword != undefined){
			    if(keyword!="" && keyword.length < 3)
			    {
				    growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
				    return false;
			    }
		 }
		var data = {};
		var paramObj = {
			doctor_name : keyword
		};
		var headerObj = {
			unit_id : $scope.unitId,
			organization_id : $scope.organizationId
		}
		console.log(data);

		var URI = BASE_URL + ROOT_URL
				+ GETDOCTORMASTERLISTAUTOFILLSERACH;

		return GenericServiceParamHeader
				.serviceAction(METHOD_GET, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE,
						paramObj, headerObj)
				.then(
						function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success") {
				               
				                var distinctResults = [];
				                var seenNames = {};

				                response.data.listObject.forEach(function(item) {
				                    var doctorName = item.doctorName;

				                    
				                    if (!seenNames[doctorName]) {
				                        seenNames[doctorName] = true;
				                        distinctResults.push(item);
				                    }
				                });

				                return distinctResults;
				            }
				        });

	}
	
	$scope.selectDoctor = function($item, $model, $label, idx) {
		$scope.searchAdvised.advisedByName = $item.doctorName;
		$scope.searchAdvised.advisedById = $item.doctorId;
	}
	
   
	 /*  $scope.getSpecialityIdWiseDoctorList = function(id, idx) {


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
								
								// $rootScope.startLoader();
								if (response.data.status == "success"){
									$scope.doctorList = response.data.listObject;
									$scope.autoRenderedServicesFinalList[idx].doctorForServiceList = response.data.listObject;
//									if($scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == '' || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == null || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == undefined)
//									{
									$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = id;
									angular.forEach($scope.specialityMasterList, function(value, key){
										if(value.specialityId == id){
											$scope.specialityName=value.specialityName;
										}
									});
									$scope.autoRenderedServicesFinalList[idx].specialityName = $scope.specialityName;
//									}
								}
								
							});

		}*/

	
	   
	   $scope.getDoctorMainChangeForDocShare = function(docId,idx, docSpecialityId)
		{
		   
			$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = null;
			var specDocObj = {
		    			unitId:$scope.unitId,
		    			organizationId:$scope.organizationId,
		    			doctorId:docId
		    }
		    var URI = BASE_URL + ROOT_URL
			+ GETSPECIALITYLISTBYDOCTORID;
			BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
						if (response.data.status == "success")
							{
								$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
								$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = docSpecialityId;
								angular.forEach($scope.specialityMasterList, function(value, key){
									if(value.specialityId == docSpecialityId){
										$scope.specialityName=value.specialityName;
									}
								});
								$scope.autoRenderedServicesFinalList[idx].specialityName = $scope.specialityName;
								$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx);
							}
					});
		
		}
	
	   $scope.couponCode = '';
	   $scope.validateBtn = true;
	   $scope.openCouponPopup = function(){
		   $scope.validateBtn = false;
		   $scope.couponCode = '';
		   angular.element('#couponPopoup').modal('show');
	   } 
	  
	   $scope.closeCouponPopup = function(){
		   angular.element('#couponPopoup').modal('hide');
	   }
	   
	   
	  
	   
	   $scope.applyCoupon = function()
		{
		   
		   $scope.validateBtn = true;
		   var checkDiscount = false;
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
					{
						if(value.discountPercentage > 0 && value.patientCouponTemplateMapperId == null)
						{
							checkDiscount = true;
						}
					});
				
		   if(checkDiscount == true){
			   
			   $scope.validateBtn = false;
			   angular.element('#couponPopoup').modal('hide');
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
		   
		   if($scope.autoRenderedServicesFinalList == undefined || $scope.autoRenderedServicesFinalList == null ||  $scope.autoRenderedServicesFinalList.length == 0)
			{
			   $scope.validateBtn = false;
			growl.warning("No service's added ",
					{
						title : 'Warning!'
					});
			    return false;
			 }
		   
		   angular.element('#couponPopoup').modal('hide');
			var specDocObj = {
		    			unitId:$scope.unitId,
		    			couponCode:$scope.couponCode,
		    			encounterId :$scope.encounterDetails.encounterId
		    }
		    var URI = BASE_URL + ROOT_URL+ "/api/billing/Patientcoupon/counsumCouponTemplate";
			BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
						$scope.validateBtn = false;
						if (response.data.status == "success")
							{
				    		// For DD
							
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
					    	
					    	
					    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					    		 continueLoop = false;
					    		if(value.orderDetailsPackageMapperDtosList[0].packageId == null && value.status == 'A' && (checkCouponId == 0 && value.serviceMasterId == couponData.serviceId && serviceCheck == true && value.netPay > 0))
					    			{
					    			serviceCheck = false;
					    			continueLoop = true;
					    			}else if(value.orderDetailsPackageMapperDtosList[0].packageId == null  && value.status == 'A' && (checkCouponId == 1 && value.serviceSubSpecialityId == couponData.subSpecialityId)){
					    				continueLoop = true;
					    			}else if(value.orderDetailsPackageMapperDtosList[0].packageId == null  && value.status == 'A' && (checkCouponId == 2 && value.serviceSpecialityId == couponData.specialityId)){
					    				continueLoop = true;
					    			}
					    		
					    		if(continueLoop == true){
					    			
					    			
					    			if(couponData.percentage !=0){
					    				$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
			    						
			    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
				    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
				    		    		
			    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
			    						var disPerTmp = parseFloat(0);
			    						if($scope.cancelService)
			    							disPerTmp = couponData.percentage;
			    						else
			    							disPerTmp =  couponData.percentage;
			    						$scope.autoRenderedServicesFinalList[i].discountPercentage = couponData.percentage ;
				  	    		  	        
				  	    		  	    	var txpre = 0;
				  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
				  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
				  	    		  				txpre = value.taxPercentage;
				  	    		  			}else{
				  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
				  	    		  					}
				  	    		  		    $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	
				  	    		  		    var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat( $scope.autoRenderedServicesFinalList[i].discount));
				  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
				  	    		  			$scope.autoRenderedServicesFinalList[i].hospitalShare = parseFloat(withConDic);
				  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
				  	    		  			$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  couponData.patientCouponTemplateMapperId;
						    	     
					    				
					    			}else{
			    						
			    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
				    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
				    		    		
			    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
			    						var disPerTmp = parseFloat(0);
			    						
			    						  var withConDic = parseFloat(0);
			    						  
			    						if(couponData.amount >= (parseFloat(totalAmt) - parseFloat(value.concession))){
			    							
			    							couponData.amount =  parseFloat(couponData.amount) - (parseFloat(totalAmt) - parseFloat(value.concession))
			    							$scope.autoRenderedServicesFinalList[i].discountPercentage = 100;
			    							$scope.autoRenderedServicesFinalList[i].discount  = (parseFloat(totalAmt) - parseFloat(value.concession));
			    							$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  couponData.patientCouponTemplateMapperId;
			    							
			    						}else{
			    							
			    							if(couponData.amount > 0){
			    								
			    								disPerTmp = (parseFloat(couponData.amount)/parseFloat(parseFloat(totalAmt) - parseFloat(value.concession)))*100;
				    							$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
				    							$scope.autoRenderedServicesFinalList[i].discount  = parseFloat(couponData.amount);
				    							$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId =  couponData.patientCouponTemplateMapperId;
			    							}else{
			    								$scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
				    							$scope.autoRenderedServicesFinalList[i].discount  = 0;
				    							$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId = null;
			    							}
			    							couponData.amount = 0;
			    						}
			    						
			    						var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
			    						
			    						/*if($scope.cancelService)
			    							disPerTmp = 0;
			    						else
			    							disPerTmp = (parseFloat(couponData.amount)/parseFloat(totalDisServiceWise-$scope.AutoRenderedTotalConcessionSummary))*100;
			    					//	$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
			    						
			    						//$scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	
			    						 // var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
				  	    		  	        */
				  	    		  	    	var txpre = 0;
				  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
				  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
				  	    		  				txpre = value.taxPercentage;
				  	    		  			}else{
				  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
				  	    		  					}
				  	    		  		 
				  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
				  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
				  	    		  			$scope.autoRenderedServicesFinalList[i].hospitalShare = parseFloat(withConDic);
				  	    		  			
			    						}
			  	    		  			
			  	    		  		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
		    		    			indexList.push(i);
					    	     }else{/*

					    				$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
			    						
			    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
				    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
				    		    		
			    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
			    						var disPerTmp = parseFloat(0);
			    						if($scope.cancelService)
			    							disPerTmp =  ($scope.autoRenderedServicesFinalList[i].discountPercentage != null && $scope.autoRenderedServicesFinalList[i].discountPercentage !=0) ? $scope.autoRenderedServicesFinalList[i].discountPercentage : 0; 
			    						else
			    							disPerTmp = ($scope.autoRenderedServicesFinalList[i].discountPercentage != null && $scope.autoRenderedServicesFinalList[i].discountPercentage !=0) ? $scope.autoRenderedServicesFinalList[i].discountPercentage : 0;
			    						$scope.autoRenderedServicesFinalList[i].discountPercentage = ($scope.autoRenderedServicesFinalList[i].discountPercentage != null && $scope.autoRenderedServicesFinalList[i].discountPercentage !=0) ? $scope.autoRenderedServicesFinalList[i].discountPercentage : 0;
			    						
				  	    		  	        
				  	    		  	    	var txpre = 0;
				  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
				  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
				  	    		  				txpre = value.taxPercentage;
				  	    		  			}else{
				  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
				  	    		  					}
				  	    		  		    $scope.autoRenderedServicesFinalList[i].discount =((parseFloat(totalAmt) - parseFloat(value.concession))*(isNaN(disPerTmp) ? 0: disPerTmp)/100).toFixed(2);	
				  	    		  		    var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat( $scope.autoRenderedServicesFinalList[i].discount));
				  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
				  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
				  	    		  			$scope.autoRenderedServicesFinalList[i].hospitalShare = parseFloat(withConDic);
				  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
				  	    		  			$scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId = ( $scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId !=undefined && $scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId !=null) ? $scope.autoRenderedServicesFinalList[i].patientCouponTemplateMapperId : null;
						    	     
				  	    		  		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[i]);
			    		    			indexList.push(i);
						    	     
					    	     */}
					    			if(i+1 == $scope.autoRenderedServicesFinalList.length && indexList.length > 0){
					    				$scope.addOrUpdateServices(null,indexList);
					    			}
					    			
					    			i++;
					    		});
				    	
							
							}else{
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
					});
		
		}
	   
	   
		   $scope.checkServiceWiseDoctorsListPop = function(info, idx){

			   var docListObj={
					   serviceId : info.serviceMasterId,
					   serviceDepartmentId : info.serviceSpecialityId,
				   		unitId : $scope.unitId,
				   		organizationId : $scope.organizationId,
				   		billingBedCategoryId : $scope.opdBillingCatId,
				   		visitTypeId : $scope.visitTypeId,
				   		patientTypeId : $scope.patientDetails.patientTypeId,
				   		paymentEntitlementId:$scope.encounterDetails.paymentEntitlementTypeId
				   }
				   var URI = BASE_URL + ROOT_URL + "/adt/getDoctorShareWiseDocList"// for
   				   																	// HIS-15355

   				   GenericService
   				   .serviceAction("POST", URI, docListObj)
   				   .then(
   				   		function(response) {
//   				   			$scope.doctorsList = [];
   				   			$scope.doctorsListNewPop = [];
   				   			if (response.data.status == "success") {
   				   				$scope.doctorsListNewPop = response.data.listObject;
   				   				angular.forEach($scope.servicesListPopup, function(value, key){
   				   				   if(key == idx){
   				   					value.docList = $scope.doctorsListNewPop;
   				   				   }
   				   			   });
   				   			}
   				   		});
   		   }
   		  // $scope.checkServiceWiseDoctorsListPop();

   		  $scope.checkServiceWiseDoctorsListPack = function(info, idx,parentIndex){

		   			  var docListObj={
		   				   serviceId : info.serviceMasterId,
		   				   serviceDepartmentId : info.serviceSpecialityId,
		   			   		unitId : $scope.unitId,
		   			   		organizationId : $scope.organizationId,
		   			   		billingBedCategoryId : $scope.opdBillingCatId,
		   			   		visitTypeId : $scope.visitTypeId,
		   			   		patientTypeId : $scope.patientDetails.patientTypeId,
		   			   		paymentEntitlementId:$scope.encounterDetails.paymentEntitlementTypeId
		   			   }
		   			   var URI = BASE_URL + ROOT_URL + "/adt/getDoctorShareWiseDocList";// for
                     				   																	// HIS-15355

                     				   GenericService
                     				   .serviceAction("POST", URI, docListObj)
                     				   .then(
                     				   		function(response) {
                     				   			$scope.doctorsList = [];
                     				   			$scope.doctorsListNewPack = [];
                     				   			if (response.data.status == "success") {
                     				   				$scope.doctorsListNewPack = response.data.listObject;
                     				   				angular.forEach($scope.selectedPackageList[parentIndex].packageServiceList, function(value, key){
                     				   				   if(key == idx){
                     				   					value.docList = $scope.doctorsListNewPack;
                     				   				   }
                     				   			   });
                     				   			}
                     				   		});
                     		   }
   		  
	    $scope.finalCancelCoupon = function()
	    {
	    	var indexList = [];
	    	$scope.autoRenderedServicesForSaveAndUpdate=[];
				var j = 0;
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) 
			{
				if($scope.autoRenderedServicesFinalList[j].discountPercentage > 0 && value.patientCouponTemplateMapperId != null)
				{
					var totalAmt = 0;
					var dis = 0;
					var gstAmt = 0;
					var withConDic = 0;
	    		
	    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
						$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
						$scope.autoRenderedServicesFinalList[j].discount = 0;
						$scope.roundOffAmt = 0.00;
						
						totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
						
						withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
						gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);

						$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
						$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
						$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
						$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
						$scope.autoRenderedServicesFinalList[j].hospitalShare = parseFloat(withConDic);
			    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
			    		$scope.autoRenderedServicesFinalList[j].patientCouponTemplateMapperId =  null;
			    		$scope.autoRenderedServicesForSaveAndUpdate.push($scope.autoRenderedServicesFinalList[j]);
			    		indexList.push(j);
					}	
	    			if(j+1 == $scope.autoRenderedServicesFinalList.length && indexList.length>0)
	    			{
	    	            $scope.addOrUpdateServices(null,indexList);
	    			}
					
	    		j++;
				
			});
	    }

	    
	    $scope.uncheckFincialClearance = function()
	    {
	    	$scope.isFinancialClearence = 'N';
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
}]);
	
