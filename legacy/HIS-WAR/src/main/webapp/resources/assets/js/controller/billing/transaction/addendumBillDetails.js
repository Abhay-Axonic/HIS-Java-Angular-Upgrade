angular.module("myApp").controller("addendumBillDtlsController", [ '$scope','$rootScope','$cookies','$state','BillingGenericService','GenericService','$stateParams','$timeout','growl','paymentCollFactory','GenericServiceParamHeader','dataService',function($scope,$rootScope,$cookies,$state,BillingGenericService,GenericService,$stateParams,$timeout,growl,paymentCollFactory,GenericServiceParamHeader,dataService)
	{
	
	var addService = 0;
	var init = 1;
	var quantity = 2;
	var unitPrice = 3;
	var discountPer = 4;
	var discount = 5;
	var discountCat = 5;
	
	
	$scope.init = function() 
	{
	 $scope.isSplitBillFlag  =false;
	 $scope.isSplitBillApplied = false;
	 
	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	if(cookieObjectAppConfig == undefined){
		$state.go('login');
		return;
	}
	else
		{
			console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			
		}
	$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
	$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
	$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
	
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	
	
	/**================================COUNTER CODE START======================================*/
	$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
	$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
	if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
		{
		     var flag = $scope.getCounterDetails();
		     if(flag)
		    	 {
		    	 //$rootScope.counterMstObj = $cookies.getObject('counterMstObj');
		    	 //$rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
		    	 }
		     else
		     {
		    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
		     }
		}else{
			$rootScope.counterId = 0;
		}
	/**================================COUNTER CODE END======================================*/
	
	$scope.visitTypeId=1;
	$rootScope.loginpage = true;
	$scope.currentDate = new Date();
	$scope.serviceMasterIdCheck = {};
	$scope.itemSelecteds = {};
	$scope.autoRenderedServicesFinalList = [];
	$scope.removeDiscountList = [];
	$scope.patientList  = [];
	$scope.removeServicesList = [];
	
	$scope.finalBill = "N";
	$scope.roundOffAmt = parseFloat(0).toFixed(2);
	$scope.billRemark = '';
	
	$scope.transferToActiveDeposit = "N";
	
	//start for request discount amount DD
	$scope.requestDiscountDiv = false;
	$scope.requestApporivedDiscountDiv = false;
	$scope.discountDisabledFlagForRequest = false;
	//end for discount amount DD
	 
	 	$scope.totalAmountSummary = "0.00";
		$scope.totalConcessionSummary = "0.00";
		$scope.netPayableSummary = "0.00";
		$scope.totalDiscountSummary = "0.00";
		$scope.totalGstTaxSummary = "0.00";
		$scope.totalCreditPayble = "0.00";
		$scope.totalSelfPayble = "0.00";
		
		
		//for datepicker 1
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
			//	"orderDetailsDiscountDto":"",
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
		
		//for userList DD
		
		var URI = BASE_URL + ROOT_URL
		+ "/billing/getUsersDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/userId/"+$scope.userId;
BillingGenericService.serviceAction(METHOD_GET, URI,"",
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						$scope.userDiscountObject ={};
						$scope.userDiscountObject = response.data.object;
							 
						}
						
				});
		//for UserList DD
		
		var URI = BASE_URL + ROOT_URL
		+ GETACTIVESPECIALITY;
BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					console.log(response);
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						$scope.specialityMasterList = response.data.listObject;
							 
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
+ GETACTIVEDOCTORLIST;
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
			"discountGivenBy":"",
			"discountCategoryId" : "",
			"discountAmountBillWise" : "",
			"discountPercentageBillWise" : "",
			"approvedAmountBillWise":"",
			"requestUserId":"",
			"requestAmountBillWise" : 0,
	}
	

	//yogesh
	$scope.searchPackageList = [];
	$scope.selectedPackageList = [];
	$scope.packageDetailsList = [];
	$scope.initPackageSearchObj();
	$scope.getSexMasterList();
	$scope.search="";
	$scope.forServicePayeeId  = 1;
	$scope.servicesListPopup = [];
	/*First Level*/
	$scope.firstTabLevel = 1 ;
	$scope.getPackageTypeList($scope.organizationId,$scope.unitId);
	$scope.addendumBill = dataService.addendumBill;
	$scope.billId = dataService.addendumBill.billingMasterId;
	
	$scope.invoiceDetails = [];
	$scope.creditNoteDetails = [];
	
	$scope.billSummary = {newBillAmt:0,oldBillAmt:0,amountReceived:0,oldBillDue:0,creditNoteAmt:0,cnPaidAmt:0,cnDueAmt:0,difference:0};
	
	$scope.getInvoiceDetails();
	$scope.getCreditNoteDetails();
	//get service details
	$scope.getBillServiceDetails($scope.billId);
	}
	
	
	$scope.addPaymentDetails = function(){
		paymentCollFactory.addPaymentDetails($scope);
	}
	$scope.removePaymentDetails = function(){
		paymentCollFactory.removePaymentDetails($scope);
	}
	
	 $scope.checkAll = function()
	 {
		 $scope.servicesSelectedList.services = $scope.autoRenderedServicesFinalList.map(function(item) { return item.serviceMasterId; });
		  
	 };
		  
			
$scope.groupWiseCheckAll= function(chkFlg) {
			$scope.disPerPopupForGroup = '';
			$scope.disAmtPopupForGroup = '';
			$scope.disCategoryIdForGroup = '';
			if(chkFlg == 'Y')
			$scope.groupWiseServicesSelectedList.services = $scope.groupWiseDiscountServicesList.map(function(item) { return item.serviceMasterId; });
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
		console.log(item);
	}
	
	
	$rootScope.searchPatient = function(patientsearchObj){
		if($scope.patientSearchObj.uhIdNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNo == '' && $('#birthDate').val() == '' && $scope.patientSearchObj.identificationNo == '')
		{
			growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
			});
			return false;
		}
			
		else{
				//alert("else AlERT");
			}
		if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			return false;
		}	
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
		BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
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
	
	$scope.getPatientDetailsByIndex = function(index){
		$rootScope.startLoader();
		$scope.patientDetails.age=($scope.patientList[index].age);
		$scope.patientDetails.patientName=($scope.patientList[index].patientName);
		$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
		$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
		$scope.patientDetails.patientId=($scope.patientList[index].patientId);
		$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
		$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
		$scope.patientDetails.patientTypeId = ($scope.patientList[index].patientType);
		$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
		var data = {};
		var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.patientList[index].patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				console.log("Encouner List",response.data.listObject);
				$scope.patientEncounterList = response.data.listObject;
				if($scope.patientEncounterList.length == 0){
					growl.warning("No Result Found!",{
						title : ERROR_MSG
					});
				}
			}
		});
	
			$(".patienttSearchResult").toggle();
		
			$rootScope.stopLoader();
	}
	$scope.contractId = 0;
	$scope.AddServiceBtn = true;
	//$scope.autoRenderedServicesList = [];
	$("#savedisablebtn").addClass("disabled");
	$scope.getEncounterDetails = function(encounterId)
	{
		$scope.requestApporivedDiscountDiv = false;
		$scope.billWiseDiscountObj.discountPercentageBillWise = "";
		$scope.billWiseDiscountObj.discountAmountBillWise ="";
		var data = {};
		var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/"+encounterId;
		BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				
				if(response.data.object!=null)
					{
						//alert();
						$scope.AddServiceBtn = false;
						$("#savedisablebtn").removeClass("disabled");
					}
				$scope.encounterDetails = {
						encounterId : response.data.object.encounterId.toString(),
						encounterDateString : response.data.object.encounterDateString,
						referralDesc : response.data.object.referralDesc,
						specialityName : response.data.object.specialityName,
						doctorName : response.data.object.doctorName,
						patientCategory : response.data.object.patientCategory,
						//paymentEntitlementDesc : response.data.object.paymentEntitlementDesc,
						//companyDesc : response.data.object.companyDesc,
						//paymentEntitlementTypeId : response.data.object.paymentEntitlementTypeId,
						//payeeId : response.data.object.payeeId,
						patientId : response.data.object.patientId,
						doctorId : response.data.object.doctorId,
						modalityId : response.data.object.modalityId,
						subSpecialityName : response.data.object.subSpecialityName,
						modalityDesc : response.data.object.modalityDesc,
						patientTypeId : response.data.object.patientSponsorDetailsDtosList[0].patientTypeId,
						genderId : response.data.object.patientSponsorDetailsDtosList[0].genderId
						
				}
				
				$scope.patientDetails = $scope.patientDetails = {
						patientId	  : $scope.addendumBill.patientId,
						patientName : $scope.addendumBill.patientName,
						genderCode  : "",
						age         : "", 
						uhIdNumber  : "",
						birthDate	  : "",
						encounterId : $scope.encounterDetails.encounterId,
						patientTypeId : $scope.encounterDetails.patientTypeId,
						genderId : $scope.encounterDetails.genderId
				}
				
				angular.forEach(response.data.object.patientSponsorDetailsDtosList,function(value,index){
					if(value.priorityId == '1')
					{
						$scope.encounterDetails.paymentEntitlementDesc  = value.paymentEntitlementDesc;
						$scope.encounterDetails.companyDesc = value.companyDesc;
						$scope.encounterDetails.contractDesc = value.contractName;
						$scope.encounterDetails.validityFrom = value.validityFrom;
						$scope.encounterDetails.validityTo = value.validityTo;
						$scope.encounterDetails.payeeId = value.payeeId;
						$scope.encounterDetails.paymentEntitlementTypeId = value.paymentEntitlementId;
						
						if($scope.encounterDetails.payeeId > 1)
							{
								$scope.contractId = value.contractId;
							}
						else
							{
								$scope.contractId = 0;
							}
					}
				});
				
				
				//$scope.getAutoRenderedServicesByPatient(response.data.object);
				$scope.getActivePackagesList();//yogesh
				$scope.packageDetailsList = [];
			}
		});
		
		$scope.requestStatusObject = {};
		var data1 = {
				encounterId : encounterId
		}
		var URI1 = BASE_URL + ROOT_URL + "/api/billing/getDiscReqDtlsForOPBilling/";
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
					$scope.statusApp = "Approved.";
						
					var totalDisPercentage =((parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100).toFixed(2);
		    		if(totalDisPercentage > 0){
		    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
		    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
					
		    		}
		    		}
				else if($scope.requestStatusObject.reqStatus == 1)
					{
					    $scope.discountDisabledFlagForRequest = true;
					    $scope.requestApporivedDiscountDiv = true;
					    $scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
						$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
						$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
						$scope.statusApp = "Pendding.";
					}
				
				}
				
				else{
					$scope.requestStatusObject = null;
					}
				
				}
		});
		
	}
	
	
	$scope.getAutoRenderedServicesByPatient=function(encounterObj){
		
		$rootScope.startLoader();
		$scope.autoRenderedServicesBillingDetails=[];
		//var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/autoRendered";
		//var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/renderedServices";
		var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
		var obj={
				"visitTypeId":1,
				"encounterId":encounterObj.encounterId
		}
		console.log("AUTORENDER",obj);
		//return false;
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
		{	
			if(response.data.status == "success")
			{
				var tempAutoRendServList = [];
				angular.forEach(response.data.listObject,function(value,index){
					if(value.isPackageService == 'Y'){
						value.apportionedPrice = value.basePrice;
						value.basePrice = 0;
						value.netPay = 0;
						value.totalAmt = 0;
						value.selfPayable = 0;
					}
					value.packageMasterId = value.packageId;
					tempAutoRendServList.push(value);
				});
				$scope.autoRenderedServicesList = tempAutoRendServList;
				console.log("$scope.autoRenderedServicesList",$scope.autoRenderedServicesList);
				//return false;
				  $scope.calculationTest($scope.autoRenderedServicesList,init);
				  $rootScope.stopLoader();
			}
			else
				{
				 $rootScope.stopLoader();
				}
		});
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
		    	"discountPercentage":item.discountPercentage, // direct discount percentage
		    	"discountAmount"	:item.discountAmount, // direct discount amount
		    	"orderDetailsId"	:item.orderDetailsId
	    	}
	    	return obj;
	    }
	
	
	$scope.searchServices = function(searchKeyword){
		$scope.servicesList=[];
		var serData = {
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				searchKeyword : searchKeyword
		}
		var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
		//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
		return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
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
   
    $scope.calculationTest = function(autoRenderedServicesList,typeId)
    {
    	console.log("AUTO",autoRenderedServicesList);
    	$scope.AutoRenderedTotalAmountSummary = 0;
		$scope.AutoRenderedTotalConcessionSummary = 0;
		$scope.AutoRenderednetPayableSummary = 0;
		$scope.AutoRenderednetTotalDiscountSummary=0;
		$scope.AutoRenderednetTotalTax=0;
		$scope.AutoRenderednetTotalDiscount=0;
		$scope.AutoRenderednetTotalCreditPayble=0;
		$scope.AutoRenderednetTotalSelfPayble=0;
		//-----calculate amounts
		//console.log(autoRenderedServicesList);
		//return false;
		
		$scope.autoRenderedServicesFinalList = [];
		
		angular.forEach(autoRenderedServicesList, function (value, key) {
					console.log(value.basePrice, "#", value.quantity);
					var newArr = {};
					
					
					
					/*if(value.orderDetailsId > 0)
						{
						newArr.orderDetailsId = value.orderDetailsId;
						}
					else
						{
						newArr.orderDetailsId = null;
						}*/
					
					newArr.orderDetailsId = value.orderDetailsId > 0 ? value.orderDetailsId : null;
					
					
					/*if(value.orderId > 0)
					{
					newArr.orderId = value.orderId;
					}
				else
					{
					newArr.orderId = null;
					}*/
					newArr.orderId = value.orderId > 0 ? value.orderId : null;
					
					if(value.orderDate != null)
						{
							newArr.orderDate  = value.orderDate;
						}
					
					/*if(value.orderNo != null)
					{
						newArr.orderNo  = value.orderNo;
					}
					else
						{
							newArr.orderNo  = '';
						}*/
					
					newArr.orderNo = value.orderNo != null ? value.orderNo : '';
					
					newArr.priorityId  = value.priorityId;
					newArr.isOutsourced = value.isOutsourced;
					newArr.specialityId = value.specialityId;
					newArr.taxId = value.taxId;
					
					/*if(value.ordCancelled != null)
					{
						newArr.ordCancelled  = value.ordCancelled;
					}
					else
						{
							newArr.ordCancelled  = 'N';
						}*/
					
					newArr.ordCancelled = value.ordCancelled != null ? value.ordCancelled : 'N';
					
					/*if(value.ordCancelReasonId != null)
					{
						newArr.ordCancelReasonId  = value.ordCancelReasonId;
					}
					else
						{
							newArr.ordCancelReasonId  = '';
						}*/
					
					newArr.ordCancelReasonId = value.ordCancelReasonId != null ? value.ordCancelReasonId  : '';
					
					/*if(value.ordCancelDatetime != null)
					{
						newArr.ordCancelDatetime  = value.ordCancelDatetime;
					}
					else
						{
							newArr.ordCancelDatetime  = '';
						}*/
					newArr.ordCancelDatetime = value.ordCancelDatetime != null ? value.ordCancelDatetime  : '';
					
					
					/*if(value.ordCancelRemark != null)
					{
						newArr.ordCancelRemark  = value.ordCancelRemark;
					}
					else
						{
							newArr.ordCancelRemark  = '';
						}*/
					newArr.ordCancelRemark = value.ordCancelRemark != null ? value.ordCancelRemark  : '';
					
					newArr.status  = value.status;
					/*if(value.serviceRendered != null)
					{
						newArr.serviceRendered  = value.serviceRendered;
					}
					else
						{
							newArr.serviceRendered  = '0';
						}*/
					newArr.serviceRendered = value.serviceRendered != null ? value.serviceRendered  : '0';
					
					
					/*if(value.oldOrdDtlId != null)
					{
						newArr.oldOrdDtlId  = value.oldOrdDtlId;
					}
					else
						{
							newArr.oldOrdDtlId  = null;
						}*/
					newArr.oldOrdDtlId = value.oldOrdDtlId != null ? value.oldOrdDtlId  : null;
					
					/*if(value.doctorSpecialityId != null || value.doctorSpecialityId > 0)
					{
						newArr.doctorSpecialityId  = value.doctorSpecialityId;
					}
					else
						{
							newArr.doctorSpecialityId  = null;
						}*/
					
					newArr.doctorSpecialityId = ((value.doctorSpecialityId != null || value.doctorSpecialityId > 0) ? value.doctorSpecialityId  : null);
					/*if(value.isRefDoctorShare != null)
					{
						newArr.isRefDoctorShare  = value.isRefDoctorShare;
					}
					else
						{
							newArr.isRefDoctorShare  = null;
						}*/
					
					newArr.isRefDoctorShare = value.isRefDoctorShare != null ? value.isRefDoctorShare  : null;
					
					newArr.doctorId = value.doctorId;
					newArr.serviceSpecialityId = value.serviceSpecialityId;
					newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
					newArr.serviceRendered = value.serviceRendered;
					newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
					newArr.oldNetPay = value.oldNetPay;
					newArr.discountDetailsList = value.discountDetailsList; 
					newArr.serviceMasterId = value.serviceMasterId;
					newArr.serviceStandardCode = value.serviceStandardCode;
					newArr.serviceStandardName = value.serviceStandardName;
					//newArr.isDiscountApplicable = value.isDiscountApplicable;
					newArr.isDiscountApplicable = value.isDiscountApplicable; //isDiscountApplicable
					newArr.isQuantityEditable = value.isQuantityEditable;
					newArr.isRateEditable = value.isRateEditable;
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
	    			newArr.packagePayable = value.packagePayable;
	    			newArr.subSpecialityId = value.subSpecialityId;
	    			newArr.packageId = value.packageId;
					if(value.specialityName == null)
					{
						newArr.specialityName = "";
						newArr.doctorName = "";
					}
					newArr.quantity = value.quantity;
					newArr.basePrice = parseFloat(value.basePrice).toFixed(2);
					newArr.basePriceOld = value.basePrice;
					newArr.totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					newArr.concession = parseFloat(value.concession).toFixed(2);
					newArr.discountCategoryId = '';
					newArr.discountPercentage = value.discountPercentage;
					newArr.discount = value.discount;
					var dis = parseFloat(value.discount).toFixed(2);
					var disPer =value.discountPercentage;
					//alert($scope.discountDetailsList.length);
					if(value.discount == null || value.discount == 0 )
						{
							newArr.discountPercentage = 0;
							newArr.discount = 0;
							dis = 0;
							disPer = 0;
							newArr.discountDisabledFlag = 'Y';
							
						}
					
						if(value.discountDetailsList == null)
						{
							value.discountDetailsList = [];
							newArr.discountDetailsList = [];
						}
					
					if(Object.keys(value.discountDetailsList).length > 1)
						{
								newArr.discountCategoryId = '0';
								var tempDiscPer = 0;
								var tempDiscAmt = 0;
								angular.forEach(value.discountDetailsList, function (value, key) {
										
									tempDiscPer += parseFloat(value.discountPercentage);
									tempDiscAmt += parseFloat(value.discountAmount);
									
								});
								dis = tempDiscAmt;
								disPer = tempDiscPer;
								newArr.discountPercentage =disPer.toFixed(2);
								newArr.discount = dis.toFixed(2);
								newArr.discountDisabledFlag = 'N';
								
						}
					else
						{
							if(Object.keys(value.discountDetailsList).length == 0)
								{
									//newArr.discountPercentage = disPer;parseFloat(value.quantity) * parseFloat(value.basePrice))
									newArr.discount = dis;
									newArr.discountPercentage = isNaN(((parseFloat(dis)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - (parseFloat(value.concession))))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(dis)/(parseFloat(value.quantity) * parseFloat(value.basePrice) - (parseFloat(value.concession))))*100).toFixed(2);
									//dis = dis;
									//disPer = disPer;
									if(value.discountCategoryId != null)
										{
											newArr.discountCategoryId = value.discountCategoryId.toString();
										}
									
									newArr.discountDisabledFlag = 'Y';
								}
							else /*if(typeId == '1')*/  //just test changes
								{
									//alert("gg");
									newArr.discountPercentage = value.discountDetailsList[0].discountPercentage;
									newArr.discount = value.discountDetailsList[0].discountAmount;
									dis = value.discountDetailsList[0].discountAmount;
									disPer = value.discountDetailsList[0].discountPercentage;
									newArr.discountCategoryId = typeof(value.discountDetailsList[0].discountCategoryId) != "undefined" && value.discountDetailsList[0].discountCategoryId != null ? value.discountDetailsList[0].discountCategoryId.toString() : null;
									newArr.discountDisabledFlag = 'Y';
								}
							//newArr.discountCategoryId = $scope.discountDetailsList[0].discountCategoryId;
						}
					newArr.taxName = value.taxName;
					//newArr.taxPercentage = value.taxPercentage;
					var txpre = 0;
					//alert(value.taxPercentage);
					if(value.taxPercentage != null || value.taxPercentage > 0){
						newArr.taxPercentage = value.taxPercentage;
						txpre = value.taxPercentage;
					}
					else
						{
						 newArr.taxPercentage = 0;
						}
					
					
					var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					var concession = parseFloat(value.concession).toFixed(2);
					
					

					if(typeId == '4' || typeId == '2' || typeId == '3' ) // for discount percentage
						{
							var calDicAmt = ((parseFloat(totalAmt) - parseFloat(concession))*value.discountPercentage/100).toFixed(2);	
							dis = calDicAmt;
							newArr.discount  = calDicAmt;
							newArr.discountPercentage  = parseFloat(value.discountPercentage).toFixed(2);//new added
							if(value.discountDetailsList.length == 0)
							{
								newArr.discountDisabledFlag = 'Y';
							}
							else
								{
								newArr.discountDisabledFlag = 'N';
								}
							
						}
					if(typeId == '5')// for discount amount
						{
							disPer = ((parseFloat(dis)/(totalAmt - (parseFloat(concession))))*100).toFixed(2);
							newArr.discountPercentage =disPer;
							//newArr.discountDisabledFlag = 'Y';
							if(value.discountDetailsList.length == 0)
							{
								newArr.discountDisabledFlag = 'Y';
							}
							else
								{
								newArr.discountDisabledFlag = 'N';
								}
						}
					
					
					var withConDic =parseFloat(totalAmt - (parseFloat(concession) + parseFloat(dis))).toFixed(2);
					
			
					
					newArr.taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					
					var gstAmt =  parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					newArr.netPay =   parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
					//newArr.selfPayable =   parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
					//newArr.creditPayable =   parseFloat(0).toFixed(2);
					
					var selfPayableV =   "0";
					var creditPayableV =   "0";
					if(value.selfPayable==undefined ||value.selfPayable==null && $scope.isSplitBillFlag == true)
						{
							 newArr.selfPayable =   value.selfPayable;
							 newArr.creditPayable = value.creditPayable;
							 selfPayableV =  isNaN(value.selfPayable) ? 0 : value.selfPayable;
							 creditPayableV  =isNaN(value.creditPayable) ? 0: value.creditPayable;
						}
					else{
						var selfPayableV =   "0";
						var creditPayableV =   "0";
						
						if(value.payeeId == 1 || value.payeeId == null )
							{
							newArr.payeeId = 1;
							 newArr.selfPayable =   parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
							 selfPayableV =   parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
							}
						else
							{
							newArr.payeeId= value.payeeId;
							newArr.creditPayable = parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
							creditPayableV  = parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
							}
					}
					
					
					
					//var selfPayableV =   parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
					//var creditPayableV =   "0";
					var newPatAmt = parseFloat(((parseFloat(totalAmt) -(parseFloat(value.concession) + parseFloat(dis))) + parseFloat(gstAmt))).toFixed(2);
					$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					$scope.AutoRenderedTotalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary) + parseFloat(value.concession)).toFixed(2);
					$scope.AutoRenderednetPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary) + parseFloat(newPatAmt)).toFixed(2);
					$scope.AutoRenderednetTotalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscountSummary) + parseFloat(0)).toFixed(2);
					$scope.autoRenderedServicesBillingDetails.push($scope.createBillingDetailsObj(value));
					$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) + parseFloat(gstAmt)).toFixed(2);
					$scope.AutoRenderednetTotalDiscount = (parseFloat($scope.AutoRenderednetTotalDiscount) + parseFloat(dis)).toFixed(2);
					$scope.AutoRenderednetTotalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble) + parseFloat(creditPayableV)).toFixed(2);
					$scope.AutoRenderednetTotalSelfPayble= (parseFloat($scope.AutoRenderednetTotalSelfPayble)+ parseFloat(selfPayableV)).toFixed(2);
					newArr.createdBy = value.userId;
					newArr.createdDate = value.createdDate;
					$scope.autoRenderedServicesFinalList.push(newArr);
			
		});// end of loop
		$scope.totalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)).toFixed(2);
		$scope.totalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary)).toFixed(2);
		$scope.netPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary)).toFixed(2);
		$scope.totalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscount)).toFixed(2);
		$scope.totalGstTaxSummary=(parseFloat($scope.AutoRenderednetTotalTax)).toFixed(2);
		
		$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);
		$scope.totalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
		$scope.totalCreditPaybleSplitBill =$scope.totalCreditPayble ;
		$scope.totalSelfPaybleSplitBill = $scope.totalSelfPayble;
		
		//calculate bill summary 
		$scope.billSummary.newBillAmt = $scope.netPayableSummary;
		$scope.billSummary.oldBillAmt = $scope.addendumBill.netAmount;
		$scope.billSummary.amountReceived = $scope.addendumBill.amountPaid;
		
		//credit note amounts
		let totalCrAmt = 0;totalCnPaidAmt = 0;
		$scope.creditNoteDetails.forEach(function(val){
			totalCrAmt = parseFloat(totalCrAmt) + parseFloat($rootScope.parseFloatNum(val.creditNoteAmount));
			totalCnPaidAmt = parseFloat(totalCnPaidAmt) + parseFloat($rootScope.parseFloatNum(val.amountPaid));
		});
		$scope.billSummary.creditNoteAmt = totalCrAmt;
		$scope.billSummary.cnPaidAmt = totalCnPaidAmt;
		$scope.billSummary.cnDueAmt = parseFloat(totalCrAmt) - parseFloat(totalCnPaidAmt);
		$scope.billSummary.difference = parseFloat($scope.billSummary.newBillAmt) - parseFloat($scope.billSummary.amountReceived) - (parseFloat(totalCrAmt) - parseFloat(totalCnPaidAmt));
		
		return 1;
    }
    $scope.testAdd = function()
    {
    	
    	$scope.discountDetailsList = [/*{
			"discountPercentage":"10",
			"discountAmount":"78",
			"discountCategoryDesc":"Doctor Discount",
			"discountCategoryId":"1"
		},
		{
			"discountPercentage":"5",
			"discountAmount":"39",
			"discountCategoryDesc":"Hospital Discount",
			"discountCategoryId":"2"
		}*/];
    	
    	var newArr = {};
		newArr.serviceMasterId = '123';
		newArr.serviceStandardCode = 'Test';
		newArr.serviceStandardName = 'Test';
		newArr.isDiscountApplicable = 'Y';
		newArr.isQuantityEditable = 'Y';
		newArr.isRateEditable = 'Y';
		newArr.minRateEditable = '10';
		newArr.maxRateEditable = '25';
		newArr.specialityName = 'Speciality';
		newArr.doctorName = 'Dr. Prasun Rao';
		newArr.quantity = '1';
		newArr.basePrice = '500';
		newArr.basePriceOld = '500';
		newArr.discountDisabledFlag = 'Y';
		newArr.totalAmt = parseFloat(1) * parseFloat(500).toFixed(2);
		newArr.concession = parseFloat(0).toFixed(2);
		newArr.discountPercentage = 0;
		newArr.discount =0;
		var dis = 0;
		var disPer =0;
		
		newArr.taxName = 'CGST';
		newArr.taxPercentage = 6;
		newArr.discountDetailsList = [];
		newArr.selfPayable =  "0";
		newArr.creditPayable =   "0";
		
		var totalAmt = parseFloat(1) * parseFloat(500).toFixed(2);
		var concession = parseFloat(0).toFixed(2);
		var withConDic =((totalAmt - (concession + dis)));
		
		newArr.taxAmt = parseFloat((withConDic * 6)/100).toFixed(2);
		var gstAmt = (withConDic * 6)/100;
		newArr.netPay =   parseFloat((totalAmt -(parseFloat(0).toFixed(2) + dis) + gstAmt)).toFixed(2);
		$scope.autoRenderedServicesFinalList.push(newArr);
		$scope.calculationTest($scope.autoRenderedServicesFinalList,addService);
    }
    
    $scope.getQuantityCal = function(itemObj,idx)
    {
    	//alert(idx);
    	 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	 $scope.calculationTest(autoRenderedServicesList,quantity);
    }
    
    $scope.getBasePriceCal = function(itemObj,idx)
    {
    	//alert(idx);
    	console.log("OBJ",itemObj);
    	var basepriceNew = parseFloat(itemObj.basePrice);
    	var basePriceOld = parseFloat(itemObj.basePriceOld).toFixed(2);
    	var minRate = itemObj.minRateEditable;
    	var maxRate = itemObj.maxRateEditable;
    	
    	/*var diffRate = basePriceOld - basepriceNew;
    	alert(diffRate);*/
    	var minRatePerAmount = ((basePriceOld*minRate)/100).toFixed(2);
    	var maxRatePerAmount = ((basePriceOld*maxRate)/100).toFixed(2);
    	
    	var minPerDiff = parseFloat(basePriceOld) - parseFloat(minRatePerAmount);
    	var maxPerAdd = parseFloat(basePriceOld) + parseFloat(maxRatePerAmount);
    	
    	//alert(minRatePerAmount);
    	//alert(maxRatePerAmount);
    	
    	//alert("minPerDiff"+minPerDiff);
    	//alert("maxPerAdd"+maxPerAdd);
    	
    	if(minPerDiff >basepriceNew)
    	{
    		growl.warning("Unit price should be Minimum RS."+minPerDiff,{
				title : ERROR_MSG
			});
    		$scope.autoRenderedServicesFinalList[idx].basePrice = basePriceOld;
    		return false;
    	}
    	
    	if(basepriceNew > maxPerAdd)
    	{
    		growl.warning("Unit price should be Maximun RS."+maxPerAdd,{
				title : ERROR_MSG
			});
    		$scope.autoRenderedServicesFinalList[idx].basePrice = basePriceOld;
    		return false;	
    	}
    	
    	
    	 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	 $scope.calculationTest(autoRenderedServicesList,unitPrice);
    }
    
    $scope.getDiscountPerCal = function(itemObj,idx)
    {
    	 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	 $scope.calculationTest(autoRenderedServicesList,discountPer);
    }
    
    $scope.getDiscountCal = function(itemObj,idx)
    {
    	 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	 $scope.calculationTest(autoRenderedServicesList,discount);
    }
    
    $scope.getDiscountListForInfo  = function(itemObj,idx)
    {
    	console.log(itemObj);
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
		
    	$scope.serciveNamePop = itemObj.serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj.concession))*itemObj.discountPercentage/100).toFixed(2);
    	
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
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj.concession))*itemObj.discountPercentage/100).toFixed(2);
    	
    	$scope.discountDetailsArrayListPopup = itemObj.discountDetailsList;
    	
    	$scope.objectIndex = idx;
    	//alert($scope.objectIndex);
    	//alert();
    }
    $scope.getDiscountListPopup  = function(itemObj,idx)
    {
    	console.log("dsdsd",itemObj);
    	
    	$scope.serciveNamePop = itemObj[idx].serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj[idx].quantity) * parseFloat(itemObj[idx].basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj[idx].quantity) * parseFloat(itemObj[idx].basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj[idx].concession))*itemObj[idx].discountPercentage/100).toFixed(2);
    	$scope.discountDetailsArrayListPopup = itemObj[idx].discountDetailsList;
    	$scope.objectIndex = idx;
    	//alert("idc"+$scope.objectIndex);
    }
    
    $scope.getDisPerPopup = function(disPer)
    {
    	var idx  = $scope.objectIndex;
    	//alert(idx);
    	//alert($scope.autoRenderedServicesFinalList[idx].totalAmt);
    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
    	var calDicAmt = ((parseFloat(totalAmtPopup) - parseFloat(concessionPopup))*disPer/100).toFixed(2);	
		$scope.disAmtPopup = calDicAmt;
		
    }
    
    $scope.getDisAmtPopup = function(disAmt)
    {
    	var idx  = $scope.objectIndex;
    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
		var disPer = ((parseFloat(disAmt)/(totalAmtPopup - (parseFloat(concessionPopup))))*100).toFixed(2);
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
    	console.log(idx);
    	console.log("hhh",$scope.autoRenderedServicesFinalList[idx]);
    	//alert($scope.disPerPopup);
    	
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
    	//$scope.getDiscountList();
    	console.log("ff",$scope.autoRenderedServicesFinalList);
    	$scope.calculationTest(autoRenderedServicesList,init);
    	
    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);
    	
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
    }
    
    
    $scope.getSubSpecialityListById = function(spId)
    {
    	 var specialityIDS = [];
    	 specialityIDS.push(spId);
			
         var data = {
       		  organizationId:$scope.organizationId,
       		  unitId:$scope.unitId,
       		  specialityIdList:specialityIDS
         }
         console.log(data);
         var URI = BASE_URL + ROOT_URL
			+ GETSUBSPECIALITYBYSPECIALITYARRAY;
	BillingGenericService
			.serviceAction(METHOD_POST, URI,
					data,
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(
					function(response) {
						console.log(response);
						// $rootScope.startLoader();
						if (response.data.status == "success")
							{
							$scope.subSpecialityMaterList = response.data.listObject;
							}
							
					});
    }
    $scope.getservicesPop = function()
    {
    	$scope.servicesListPopup = [];
    }
    
    $scope.addServiceToTempServiceList = function($item, $model, $label){
		console.log($item);
		$scope.forServicePayeeId  = 1;
		$scope.search = '';
		$('#searchServiceId').val('');
		if($scope.encounterDetails.payeeId == 1){
				var obj={
							"unitId":$scope.unitId,
							"organizationId":$scope.organizationId,
							"visitTypeId":1,// by default its op
							"billingBedCategoryId":1, //By default its OPD ---> Id==1
							"patientTypeId":$scope.patientDetails.patientTypeId, //need to add after patient registration 
							//"paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
							"paymentEntitlementId":1,
							"tariffId":$scope.defaultSelfTariffId,
							"orderDate":moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
							"serviceList":[$item.serviceMasterId]
				
						}
				console.log('Base prise',obj)
				//return false;
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/unit/getBasePriceOfServicesFromTariffMasterByServiceList";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status=="success"){
						var basePriceObj=response.data.object;
						if(typeof(basePriceObj) != "undefined" && basePriceObj != null)
							{
							$item.basePrice=basePriceObj.rate;
							$item.quantity=1;
							$scope.servicePopupCalculate($item);
							}
						else
							{
							growl.warning("Please add base price of this service and then add..!!",{
								title : ERROR_MSG
							});
							return false
							}
						//concession
						console.log("FINAL ITEM",$item);
						
						
					}
				});
    }
    else
    	{
    		var obj = {
    				
    				"unitId":$scope.unitId,
					"organizationId":$scope.organizationId,
					//"visitTypeId":1,// by default its op
					//"billingBedCategoryId":1, //By default its OPD ---> Id==1
					"patientTypeId":$scope.patientDetails.patientTypeId, //need to add after patient registration 
					"orderDate":moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
    				"contractId":$scope.contractId,
    		        "serviceId":$item.serviceMasterId
    		        }
    		$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getServiceRateByCompanyId";
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status=="success"){
					if(response.data.listObject.length > 0)
					{
						$scope.forServicePayeeId = $scope.encounterDetails.payeeId;
					var basePriceObj=response.data.listObject[0].finalPrice;
					if(typeof(basePriceObj) != "undefined" && basePriceObj != null)
						{
						$item.basePrice=basePriceObj;
						$item.quantity=1;
						$scope.servicePopupCalculate($item);
						}
					else
						{
						growl.warning("Please add base price of this service and then add..!!",{
							title : ERROR_MSG
						});
						return false
						}
				}
					else
						{
						var obj={
								"unitId":$scope.unitId,
								"organizationId":$scope.organizationId,
								"visitTypeId":1,// by default its op
								"billingBedCategoryId":1, //By default its OPD ---> Id==1
								"patientTypeId":$scope.patientDetails.patientTypeId, //need to add after patient registration 
								//"paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
								"paymentEntitlementId":1,
								"tariffId":$scope.defaultSelfTariffId,
								"orderDate":moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
								"serviceList":[$item.serviceMasterId]
					
							}
					console.log('Base prise',obj)
					//return false;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/unit/getBasePriceOfServicesFromTariffMasterByServiceList";
					BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status=="success"){
							var basePriceObj=response.data.object;
							if(typeof(basePriceObj) != "undefined" && basePriceObj != null)
								{
								$item.basePrice=basePriceObj.rate;
								$item.quantity=1;
								$scope.servicePopupCalculate($item);
								}
							else
								{
								growl.warning("Please add base price of this service and then add..!!",{
									title : ERROR_MSG
								});
								return false
								}
							//concession
							console.log("FINAL ITEM",$item);
							
							
						}
					});
						}
					console.log("FINAL ITEM",$item);
				}
			});
    	}
			
		
		console.log($scope.tempServiceDetailsList);
		$scope.search = "";
	}
    
    
    $scope.servicePopupCalculate = function(item)
    {
    	//validate service in current added package
    	var serviceId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
    	var setPackageService = function(val){
    		if(val.isAvailableInCurrentPackage)
    			item = val;
    		
    		var newArr = {};
        	newArr.specialityId = item.specialityId;
        	newArr.serviceSpecialityId = item.specialityId;
    		newArr.serviceSubSpecialityId = item.subSpecialityId;
    		newArr.subSpecialityId = item.subSpecialityId;
        	newArr.taxId = item.taxId;
    		newArr.serviceMasterId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
    		newArr.serviceStandardCode = typeof(item.serviceStandardCode) != "undefined" ? item.serviceStandardCode  : item.serviceCode ;
    		newArr.serviceStandardCode = typeof(item.isPackageService) != "undefined" && item.isPackageService == "Y" ? (newArr.serviceStandardCode + " (P)") : newArr.serviceStandardCode;
    		newArr.serviceStandardName = typeof(item.serviceStandardName) != "undefined" ? item.serviceStandardName : item.serviceName;
    		newArr.isDiscountApplicable = typeof(item.isDiscountApplicable) != "undefined" ? item.isDiscountApplicable : 'N';
    		newArr.isQuantityEditable = typeof(item.packageConsumptionMasterId) != "undefined" ? 'N' : 'Y';
    		newArr.isRateEditable = typeof(item.isRateEditable) != "undefined" ? item.isRateEditable : 'N';
    		newArr.minRateEditable = typeof(item.minRateEditable) != "undefined" ? item.minRateEditable : 0;
    		newArr.maxRateEditable = typeof(item.maxRateEditable) != "undefined" ? item.maxRateEditable : 0;
    		newArr.specialityName = item.specialityName;
    		newArr.subSpecialityName = item.subSpecialityName;
    		newArr.quantity = typeof(item.quantity) != "undefined" ? item.quantity : 1;
    		newArr.basePrice = typeof(item.basePrice) != "undefined" ? item.basePrice : 0;
    		newArr.basePriceOld = typeof(item.basePrice) != "undefined" ? item.basePrice: 0;
    		newArr.totalAmt = parseFloat(newArr.quantity) * parseFloat(newArr.basePrice).toFixed(2);
    		newArr.concession = typeof(item.concession) != "undefined" ? parseFloat(item.concession).toFixed(2) : parseFloat(0).toFixed(2);
    		newArr.discountPercentage = 0;
    		newArr.discount =0;
    		newArr.doctorId = '';
    		newArr.doctorSpecialityId = '';
    		newArr.isDoctorRequired = item.isDoctorRequired;
    		newArr.discountDetailsList = [];
    		newArr.taxName = typeof(item.taxName) != "undefined" ? item.taxName : "-";
    		newArr.taxId = typeof(item.taxId) != "undefined" ? item.taxId : null;
    		newArr.taxPercentage = typeof(item.taxPercentage) != "undefined" ? item.taxPercentage : 0;
    		newArr.selfPayable =  "0";
    		newArr.creditPayable =   "0";
    		newArr.serviceRendered = "0";
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
    		newArr.packagePayable = item.packagePayable;
    		newArr.status = 'A';
    		newArr.payeeId = $scope.forServicePayeeId;
    		newArr.isRefDoctorShare = item.isRefDoctorShare;
    		$scope.servicesListPopup.push(newArr);
    		
    	    $scope.servicePopupCalculateFinal($scope.servicesListPopup); 
    	}
    	//check wether service available in package
    	var URI = BASE_URL + ROOT_URL + "/api/packages/service/exists";
    	var param = {patientId:$scope.patientDetails.patientId,encounterId:$scope.encounterDetails.encounterId,visitType:1,serviceId:item.serviceMasterId,organisationId:$scope.organizationId,unitId:$scope.unitId,
    				tariffId:$scope.defaultSelfTariffId,paymentEntitlementId:$scope.encounterDetails.paymentEntitlementTypeId,patientTypeId:$scope.patientDetails.patientTypeId,billingBedCategoryId:1};
    	BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    		if(response.data.status == "success"){
    			if(response.data.object.packageTypeId != 5 || response.data.object.packageTypeId != "5" )
    				item = response.data.object;
    			//item.isPackageService = "Y";
    		}
    		isValidPackageService(item, setPackageService);
    	});
    }
    
    $scope.servicePopupCalculateFinal = function(itemObj)
    {
    	$scope.AutoRenderedTotalAmountSummaryPopUp = 0;
    	angular.forEach($scope.servicesListPopup, function (value, key) {
		$scope.AutoRenderedTotalAmountSummaryPopUp=(parseFloat($scope.AutoRenderedTotalAmountSummaryPopUp) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		});
		$scope.totalAmountSummaryPopup = $scope.AutoRenderedTotalAmountSummaryPopUp;
    }
    
    $scope.removeTempServicesByIndex = function(index){
		//var serviceIndex = $scope.servicesListPopup.indexOf(index);
		$scope.servicesListPopup.splice(index, 1);
	}
    
    $scope.getQuantityCalPopup = function(itemObj,idx)
    {
    	//alert(idx);
    	$scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
    	 var autoRenderedServicesList = $scope.servicesListPopup;
    	 $scope.servicePopupCalculateFinal(autoRenderedServicesList);
    }
    
    $scope.getBasePriceCalPopup = function(itemObj,idx)
    {
    	//alert(idx);
    	
    	var basepriceNew = parseFloat(itemObj.basePrice);
    	var basePriceOld = parseFloat(itemObj.basePriceOld).toFixed(2);
    	var minRate = itemObj.minRateEditable;
    	var maxRate = itemObj.maxRateEditable;
    	var minRatePerAmount = ((basePriceOld*minRate)/100).toFixed(2);
    	var maxRatePerAmount = ((basePriceOld*maxRate)/100).toFixed(2);
    	var minPerDiff = parseFloat(basePriceOld) - parseFloat(minRatePerAmount);
    	var maxPerAdd = parseFloat(basePriceOld) + parseFloat(maxRatePerAmount);
    	if(minPerDiff >basepriceNew)
    	{
    		growl.warning("Unit price should be Minimum RS."+minPerDiff,{
				title : ERROR_MSG
			});
    		$scope.servicesListPopup[idx].basePrice = basePriceOld;
    		return false;
    	}
    	
    	if(basepriceNew > maxPerAdd)
    	{
    		growl.warning("Unit price should be Maximun RS."+maxPerAdd,{
				title : ERROR_MSG
			});
    		$scope.servicesListPopup[idx].basePrice = basePriceOld;
    		return false;	
    	}
    	
    	 $scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
    	 var autoRenderedServicesList = $scope.servicesListPopup;
    	 $scope.servicePopupCalculateFinal(autoRenderedServicesList);
    }
    
    $scope.addTempServiesToServiceMasterList = function(){
    	console.log("$scope.autoRenderedServicesFinalList",$scope.autoRenderedServicesFinalList);
    		$scope.disLis = {};
    		angular.forEach($scope.servicesListPopup, function (value, key) {
			
    			var docName = ''; 
    			if(value.doctorId > 0){
    	    	docName = $.grep($scope.doctorsList, function (doc) {
    	            return doc.doctorId == value.doctorId;
    	        })[0].doctorName;
    			}
    	    	
    			var newArr = {};
    			newArr.priorityId  = '2';
    			newArr.isOutsourced ='N';
    			newArr.specialityId = value.specialityId;
    			newArr.serviceSpecialityId = value.serviceSpecialityId;
    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
    			newArr.doctorId = value.doctorId;
    			newArr.doctorSpecialityId = value.doctorSpecialityId;
    			newArr.serviceRendered = "0";
    			newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
    			newArr.serviceMasterId = value.serviceMasterId;
    			newArr.serviceStandardCode = value.serviceStandardCode;
    			newArr.serviceStandardName = value.serviceStandardName;
    			newArr.isDiscountApplicable = value.isDiscountApplicable;
    			newArr.isQuantityEditable = value.isQuantityEditable;
    			newArr.isRateEditable = value.isRateEditable;
    			newArr.minRateEditable = value.minRateEditable;
    			newArr.maxRateEditable = value.maxRateEditable;
    			newArr.specialityName = value.specialityName;
    			newArr.doctorName =docName;
    			newArr.quantity = value.quantity;
    			newArr.basePrice = value.basePrice;
    			newArr.basePriceOld = value.basePrice;
    			newArr.discountDisabledFlag = 'Y';
    			newArr.totalAmt = parseFloat(value.quantity) * parseFloat(value.basePrice).toFixed(2);
    			newArr.concession = parseFloat(value.concession);
    			newArr.discountPercentage = value.discountPercentage;
    			newArr.discount = value.discount;
    			var dis =value.discount;;
    			var disPer =value.discountPercentage;
    			newArr.taxId = value.taxId;
    			newArr.taxName = value.taxName;
    			newArr.taxPercentage = value.taxPercentage;
    			newArr.discountDetailsList = [];
    			newArr.selfPayable =  "0";
    			newArr.creditPayable =   "0";
    			newArr.oldNetPay = "0";
    			newArr.createdBy  = $scope.userId;
    			newArr.createdDate = new Date().getTime();
    			newArr.isPackageService = value.isPackageService;
    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
    			newArr.isCapService = value.isCapService;
    			newArr.packagePayable = value.packagePayable;
    			newArr.subSpecialityId = value.subSpecialityId;
    			newArr.packageId = value.packageId;
    			newArr.packageTypeId = value.packageTypeId;
    			newArr.ordCancelled = "N";
    			newArr.status = "A";
    			newArr.ordCancelReasonId  = '';
    			newArr.ordCancelDatetime  = new Date().getTime();
    			newArr.ordCancelRemark  = '';
    			newArr.payeeId  = value.payeeId;

    			
    			var totalAmt = parseFloat(1) * parseFloat(500).toFixed(2);
    			var concession = parseFloat(0).toFixed(2);
    			var withConDic =((totalAmt - (concession + dis)));
    			
    			newArr.taxAmt = parseFloat((withConDic * 6)/100).toFixed(2);
    			var gstAmt = (withConDic * 6)/100;
    			newArr.netPay =   parseFloat((totalAmt -(parseFloat(0).toFixed(2) + dis) + gstAmt)).toFixed(2);
    			
    			newArr.isRefDoctorShare = value.isRefDoctorShare;
    			
    			$scope.autoRenderedServicesFinalList.push(newArr);
			
		});
    		
    		//yogesh start 
    		angular.forEach($scope.selectedPackageList, function (value, key) {
    			
    			//get services if package type is EHC
    	    	if(typeof(value.packageTypeId) != "undefined" && value.packageTypeId == "1"){
    	    		var packageIds = [];
    	    		packageIds.push(value.packageMasterId);
    	    		//get ehc package services 
    	    		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
    					if(response.data.status == "success"){
    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
    						angular.forEach(ehcPackageServiceList,function(val,i){
    							var newArr = {};
    			    			newArr.priorityId  = '2';
    			    			newArr.isOutsourced ='N';
    			    			newArr.specialityId = val.specialityId;
    			    			newArr.serviceSpecialityId = val.specialityId;
    			    			newArr.serviceSubSpecialityId = val.subSpecialityId;
    			    			newArr.serviceRendered = "0";
    			    			newArr.serviceMasterId = val.serviceId;
    			    			newArr.serviceStandardCode = val.serviceCode;
    			    			newArr.serviceStandardName = val.serviceName;
    			    			newArr.isDiscountApplicable = "N";
    			    			newArr.isQuantityEditable = "N";
    			    			newArr.isRateEditable = "N";
    			    			newArr.minRateEditable = 0;
    			    			newArr.maxRateEditable = 0;
    			    			newArr.specialityName = val.specialityName;
    			    			newArr.quantity = 1;//val.numberToBeUse;
    			    			newArr.basePrice = 0;//val.apportionedPrice;
    			    			newArr.basePriceOld = 0;//val.apportionedPrice;
    			    			newArr.discountDisabledFlag = 'N';
    			    			newArr.totalAmt = parseFloat(val.numberToBeUse) * parseFloat(0).toFixed(2);
    			    			newArr.concession = 0;
    			    			newArr.discountPercentage = 0;
    			    			newArr.discount = 0;
    			    			var dis =0;
    			    			var disPer =0;
    			    			newArr.taxPercentage = 0;
    			    			newArr.discountDetailsList = [];
    			    			newArr.selfPayable =  "0";
    			    			newArr.creditPayable =   "0";
    			    			newArr.oldNetPay = "0";
    			    			newArr.createdBy  = $scope.userId;
    			    			newArr.createdDate = new Date().getTime();
    			    			newArr.packageMasterId = value.packageMasterId;
    			    			newArr.packageTypeId = value.packageTypeId;
    			    			newArr.packageId = value.packageMasterId;
    			    			newArr.isPackageService = 'Y';
    			    			newArr.apportionedPrice = val.apportionedPrice;
    			    			
    			    			var totalAmt = 0//parseFloat(1) * parseFloat(500).toFixed(2);
    			    			var concession = 0//parseFloat(0).toFixed(2);
    			    			var withConDic =((totalAmt - (concession + dis)));
    			    			
    			    			newArr.taxAmt = parseFloat((withConDic * 6)/100).toFixed(2);
    			    			var gstAmt = (withConDic * 6)/100;
    			    			newArr.netPay =   parseFloat((totalAmt -(parseFloat(0).toFixed(2) + dis) + gstAmt)).toFixed(2);
    			    			
    			    			$scope.autoRenderedServicesFinalList.push(newArr);
    						})
    					}
    				});
    	    	}
    			
	    		var newArr = {};
				newArr.priorityId  = '2';
				newArr.isOutsourced ='N';
				newArr.specialityId = value.specialityId;
				newArr.serviceSpecialityId = value.serviceSpecialityId;
				newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
				newArr.serviceRendered = "0";
				newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
				newArr.serviceMasterId = value.serviceMasterId;
				newArr.serviceStandardCode = value.serviceStandardCode;
				newArr.serviceStandardName = value.serviceStandardName;
				newArr.isDiscountApplicable = value.isDiscountApplicable;
				newArr.isQuantityEditable = value.isQuantityEditable;
				newArr.isRateEditable = value.isRateEditable;
				newArr.minRateEditable = value.minRateEditable;
				newArr.maxRateEditable = value.maxRateEditable;
				newArr.specialityName = value.specialityName;
				newArr.quantity = value.quantity;
				newArr.basePrice = value.basePrice;
				newArr.basePriceOld = value.basePrice;
				newArr.discountDisabledFlag = 'Y';
				newArr.totalAmt = parseFloat(value.quantity) * parseFloat(value.basePrice).toFixed(2);
				newArr.concession = parseFloat(value.concession);
				newArr.discountPercentage = value.discountPercentage;
				newArr.discount = value.discount;
				var dis =value.discount;;
				var disPer =value.discountPercentage;
				newArr.taxId = value.taxId;
				newArr.taxName = value.taxName;
				newArr.taxPercentage = value.taxPercentage;
				newArr.discountDetailsList = [];
				newArr.selfPayable =  "0";
				newArr.creditPayable =   "0";
				newArr.oldNetPay = "0";
				newArr.createdBy  = $scope.userId;
				newArr.createdDate = new Date().getTime();
				newArr.isPackage = true;
				newArr.packageMasterId = value.packageMasterId;
				newArr.packageTypeId = value.packageTypeId;
				newArr.packageId = value.packageId;
				newArr.isPackageService = value.isPackageService;
				newArr.isCapService = value.isCapService;
    			newArr.packagePayable = value.packagePayable;
    			newArr.subSpecialityId = value.subSpecialityId;
    			
				var totalAmt = parseFloat(1) * parseFloat(500).toFixed(2);
				var concession = parseFloat(0).toFixed(2);
				var withConDic =((totalAmt - (concession + dis)));
				
				newArr.taxAmt = parseFloat((withConDic * 6)/100).toFixed(2);
				var gstAmt = (withConDic * 6)/100;
				newArr.netPay =   parseFloat((totalAmt -(parseFloat(0).toFixed(2) + dis) + gstAmt)).toFixed(2);
				
				$scope.autoRenderedServicesFinalList.push(newArr);
    		});
    		$scope.selectedPackageList = [];
    		// yogesh end
    		
    		angular.element('#addServicePopup').modal('hide');
    		console.log("ddfdfd",$scope.autoRenderedServicesFinalList);
    		var listServicepopup = $scope.autoRenderedServicesFinalList;
    		$scope.calculationTest(listServicepopup,addService);
    }
    
    $scope.getRemoveDiscountPopup = function(item,idxDis)
    {
    	//var idx  = $scope.objectIndex;
    	$scope.objectIndexDiscount = idxDis;
    	$scope.discountObjectForRemove = item;
    	//alert(idx);
    	
    	/*if(item.orderDetailsDiscountId > 0)
    		{
    			var disArr = {};
    			disArr.
    		}*/
    	
    	/*var currDisAmt = $scope.autoRenderedServicesFinalList[idx].discount;
    	var currDisPer = $scope.autoRenderedServicesFinalList[idx].discountPercentage;
    	
    	var finalDisAmt = (parseFloat(currDisAmt) - parseFloat(item.discountAmount)).toFixed(2);
    	var finalDisPer = (parseFloat(currDisPer) - parseFloat(item.discountPercentage)).toFixed(2);
    	
    	$scope.autoRenderedServicesFinalList[idx].discount = finalDisAmt;
    	$scope.autoRenderedServicesFinalList[idx].discountPercentage = finalDisPer;
    	
    	var serviceIndex = $scope.autoRenderedServicesFinalList[idx].discountDetailsList.indexOf(idx);
    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.splice(serviceIndex, 1);
    	
    	
    	
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	
    	$scope.calculationTest(autoRenderedServicesList,init);
    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);*/
    	
    }
    
    
    $scope.removeDiscount = function()
    {
    	var idx  = $scope.objectIndex;
    	var discIdx = $scope.objectIndexDiscount;
    	var item = $scope.discountObjectForRemove;
    	
    	//return false;
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
    
    $scope.cancelSevices = function()
    {
    	var idx  = $scope.objectIndexService;
    	//alert(idx);
    	var item = $scope.serciveObjectForRemove;
    	var itemForNagetive = angular.copy($scope.serciveObjectForRemove);
    	
    		//return false;
    	
    	//return false;
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
        	itemForNagetive.isCapService = item.isCapService;
        	console.log("AFTERRRR",itemForNagetive);
    		
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
    	

    			$scope.autoRenderedServicesFinalList.push(itemForNagetive);
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
            	
        		console.log("AFTER",autoRenderedServicesList);
        		//return false;
            	$scope.calculationTest(autoRenderedServicesList,init);

            	$scope.removeServicesList = [];
            	
    			console.log("RE DIS LISt",$scope.removeDiscountList)
    			console.log("RE SER LISt",$scope.removeServicesList);
            	//test code
            	
            	$scope.servicesCancelReasonId = '';
            	$scope.servicesCancelRemark = '';
        		
    		}else
    	      {
    		      var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(item);
    		      
    		      $scope.autoRenderedServicesFinalList.splice(serviceIndex, 1);
    		      
    		      var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    		         
    		      console.log("AFTER",autoRenderedServicesList);
    		      
    		         $scope.calculationTest(autoRenderedServicesList,init);
		      }
    	
    }
    
    $scope.getRemoveServicePopup = function(item,idx)
    {
    	//console.log("ITEM",item);
    	$scope.objectIndexService = idx;
    	$scope.serciveObjectForRemove = item;
    	
    	
    }
    
    $scope.funDiscountPercentageBillWise = function()
    {
    	//alert($scope.billWiseDiscountObj.discountPercentageBillWise);
    	if($scope.autoRenderedServicesFinalList.length > 0){
    		var totalDisAmount =(((parseFloat($scope.totalAmountSummary) - parseFloat($scope.totalConcessionSummary))*(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)/100))).toFixed(2);
    		if(totalDisAmount > 0){
    		$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise).toFixed(2);
    		$scope.billWiseDiscountObj.discountAmountBillWise = totalDisAmount;
    		}
    	}
    }
    
    $scope.funDiscountAmountBillWise = function()
    {
    	//alert($scope.billWiseDiscountObj.discountAmountBillWise);
    	if($scope.autoRenderedServicesFinalList.length > 0){
    		var totalDisPercentage =((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/(parseFloat($scope.totalAmountSummary)-parseFloat($scope.totalConcessionSummary)))*100).toFixed(2);
    		if(totalDisPercentage > 0){
    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
    		}
    	}
    	
    }
    
    $scope.getApplyDiscountPopup = function()
    {
    	if($scope.billWiseDiscountObj.discountPercentageBillWise == '')
    		{
	    		growl.warning("Discount Percentage is required.",{
					title : ERROR_MSG
				});
	    		return false;
    			
    		}

    	else if($scope.billWiseDiscountObj.discountAmountBillWise == '')
    		{
	    		growl.warning("Discount Amount is required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.billWiseDiscountObj.discountCategoryId == '')
    		{
	    		growl.warning("Discount Category is required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.billWiseDiscountObj.discountGivenBy == '')
    		{
	    		growl.warning("discount Giver By is required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else
    		{
    			angular.element('#applyDiscountPopup').modal('show');
    		}
    }
    
    $scope.applyDiscount  = function()
    {
    	//$scope.billWiseDiscountObj.discountAmountBillWise > parseFloat(100)
    	
    	if(($scope.userDiscountObject == null) || (parseFloat($scope.userDiscountObject.fromDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise && parseFloat($scope.userDiscountObject.toDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise) && $scope.requestStatusObject == null )
		{
    	   $scope.billWiseDiscountObj.requestAmountBillWise = $scope.billWiseDiscountObj.discountAmountBillWise;
    	   var i =0;
    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
   			if(value.status == 'A'){
			   		$scope.autoRenderedServicesFinalList[i].discountCategoryId = "";
			   		$scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
			   		//for remove discount
   			 }
   			i++;
   			});
    	   
    		var URI = BASE_URL + ROOT_URL
    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise;
    BillingGenericService.serviceAction(METHOD_GET, URI,"",
    				NOTIFICATION_MSG_STATUS_FALSE)
    		.then(function(response) {
    					console.log(response);
    					// $rootScope.startLoader();
    					if (response.data.status == "success")
    						{
    						  $scope.userDiscountList =[];
    						  $scope.userDiscountList = response.data.listObject;
    						}
    						
    				});
    
		   $scope.requestDiscountDiv = true;
		   growl.warning("Please sent discount request to user",{
				title : ERROR_MSG
			});
    		return false;
		}
    	else{
    		
    		//for DD
    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
    		$scope.billWiseDiscountObj.requestUserId = "";
    		$scope.requestDiscountDiv = false;
    		//For DD
    	var i = 0;
    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
    			if(value.status == 'A'){
    				if($scope.requestStatusObject != null){
    				angular.forEach($scope.requestStatusObject.requestDiscountDetailsDtosList, function (value1, key) {
    					if(value1.orderDetailsId == value.orderDetailsId)
    						{
    						  $scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
        		    		  $scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
    						}
    					
    					
    				});
    				}
    				else
    				{
    					$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
    		    		$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
    				}
    				
    		
    		//for remove discount
    		if($scope.autoRenderedServicesFinalList[i].discountDetailsList.length > 0){
				angular.forEach($scope.autoRenderedServicesFinalList[i].discountDetailsList, function (value, key) {
					if(value.orderDetailsDiscountId > 0 || value.status == 'A')
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
    		//end remove discount
    		$scope.autoRenderedServicesFinalList[i].discountDetailsList = [];
    	}
    		i++;
    	});
    	console.log("gg",$scope.removeDiscountList);
    	//return false;
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	
		
    	$scope.calculationTest(autoRenderedServicesList,discountPer);
    }
    }
    
    
    $scope.groupWiseDiscountPopup = function()
    {
    	//$scope.groupWiseDiscountServicesList  = $scope.autoRenderedServicesFinalList;
    	$scope.groupWiseServicesSelectedList.services = [];
    	$scope.chkFlgGroup = 'N';
    	$scope.groupWiseDiscountServicesList = angular.copy($scope.autoRenderedServicesFinalList);
    }
    
    $scope.serachGroupWiseServicesClear = function()
    {
    	$scope.serviceSpecialityIdForGroup = '';
    	$scope.serviceSubSpecialityIdForGroup = '';
    	$scope.chkFlgGroup = 'N';
    	$scope.groupWiseServicesSelectedList.services = [];
    	//$scope.groupWiseDiscountServicesList  = $scope.autoRenderedServicesFinalList;
    	$scope.groupWiseDiscountServicesList = angular.copy($scope.autoRenderedServicesFinalList);
    }
    
    $scope.serachGroupWiseServices = function()
    {
    	if($scope.serviceSpecialityIdForGroup > 0 && $scope.serviceSubSpecialityIdForGroup > 0)
    		{
    			$scope.groupWiseDiscountServicesList = [];
    			//alert("Both Grater");
    			var i = 0;
    			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
        			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup && value.serviceSubSpecialityId == $scope.serviceSubSpecialityIdForGroup)
        				{
        					$scope.groupWiseDiscountServicesList.push($scope.autoRenderedServicesFinalList[i]);
        				}
    				
    	    		i++;
    	    	});
    			
    			
    			
    		}
    	else if($scope.serviceSpecialityIdForGroup > 0)
    		{
	    		$scope.groupWiseDiscountServicesList = [];
				//alert("Both Grater");
				var i = 0;
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
	    			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup)
	    				{
	    					$scope.groupWiseDiscountServicesList.push($scope.autoRenderedServicesFinalList[i]);
	    				}
					
		    		i++;
		    	});
    		}
    	
    	
    	
    }
    
    $scope.getDisPerPopupForGroup = function(disPer)
    {
    	var len = $scope.groupWiseServicesSelectedList.services.length;
    	if(len == 0)
		{
			growl.warning("Please select at least one service!",{
				title : ERROR_MSG
			});
			$scope.disPerPopupForGroup = '';
    		return false;
		}
    	else
    		{
    		var totalAmtPopupAllServices = 0;
    		for(var i = 0;i < len;i++)
    		{
    			//alert($scope.groupWiseServicesSelectedList.services[i]);
	    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
	    			if(value.serviceMasterId == $scope.groupWiseServicesSelectedList.services[i])
	    				{
	    					var totalAmtPopup = value.totalAmt;
	    					var concessionPopup = value.concession;
	    					var finalTotalAmt = totalAmtPopup - parseFloat(concessionPopup);
	    					totalAmtPopupAllServices =(parseFloat(totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
	    				}
					
		    	});
    		}
    	}
    		
    	var calDicAmt = (totalAmtPopupAllServices*disPer/100).toFixed(2);	
		$scope.disAmtPopupForGroup = calDicAmt;
    }
    
    $scope.getDisAmtPopupForGroup = function(disAmt)
    {
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
    		
    		var totalAmtPopupAllServices = 0;
    		for(var i = 0;i < len;i++)
    		{
    			//alert($scope.groupWiseServicesSelectedList.services[i]);
	    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
	    			if(value.serviceMasterId == $scope.groupWiseServicesSelectedList.services[i])
	    				{
	    					var totalAmtPopup = value.totalAmt;
	    					var concessionPopup = value.concession;
	    					var finalTotalAmt = totalAmtPopup - parseFloat(concessionPopup);
	    					totalAmtPopupAllServices =(parseFloat(totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
	    				}
					
		    	});
    		}
    		//alert("totalAmtPopupAllServices"+totalAmtPopupAllServices);
    		
    		var disPer = ((parseFloat(disAmt)/totalAmtPopupAllServices)*100).toFixed(2);
    		$scope.disPerPopupForGroup = disPer;
    		}
    	
		
    }
    
    $scope.clearDiscount = function()
    {
    	$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
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
    				
    				for(var i = 0;i < len;i++)
    	    		{
    					var j = 0;
	    				angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
	    					var totalAmtPopupForGroup =0;
	    					var concessionPopupForGroup =0;
	    					var finalTotalAmtForGroup = 0;
	    					var calDicAmtForPopup = 0;
	    	    			if(value.serviceMasterId == $scope.groupWiseServicesSelectedList.services[i])
	    	    				{
	    	    					
	    	    				totalAmtPopupForGroup = value.totalAmt;
	    	    				concessionPopupForGroup = value.concession;
	    	    				finalTotalAmtForGroup = totalAmtPopupForGroup - parseFloat(concessionPopupForGroup);
	    	    					
	    	    					$scope.groupWiseDiscountServicesList[j].discountPercentage = parseFloat($scope.disPerPopupForGroup).toFixed(2);
	    	    					$scope.groupWiseDiscountServicesList[j].discountCategoryId = $scope.disCategoryIdForGroup;
	    	    					
	    	    					calDicAmtForPopup = (finalTotalAmtForGroup*$scope.disPerPopupForGroup/100).toFixed(2);	
	    	    					$scope.groupWiseDiscountServicesList[j].discount = calDicAmtForPopup;
	    	    					$scope.groupWiseDiscountServicesList[j].netPay = parseFloat((totalAmtPopupForGroup -(parseFloat(value.concession) + parseFloat(calDicAmtForPopup)) + parseFloat(value.taxAmt))).toFixed(2);
	    	    					
	    	    				}
	    	    			j++;
	    		    	});
	    				
    				}
    				console.log('scs',$scope.groupWiseDiscountServicesList);
    				console.log("ff",$scope.autoRenderedServicesFinalList);
    			}
    		}
    	
    	
    }
    
    $scope.saveGroupDiscountToFinalList = function()
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
			
    	for(var i = 0;i < len;i++)
		{
			var j = 0;
			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				var totalAmtPopupForGroup =0;
				var concessionPopupForGroup =0;
				var finalTotalAmtForGroup = 0;
				var calDicAmtForPopup = 0;
    			if(value.serviceMasterId == $scope.groupWiseServicesSelectedList.services[i])
    				{
    					
    				totalAmtPopupForGroup = value.totalAmt;
    				concessionPopupForGroup = value.concession;
    				finalTotalAmtForGroup = totalAmtPopupForGroup - parseFloat(concessionPopupForGroup);
    					
    					$scope.autoRenderedServicesFinalList[j].discountPercentage = parseFloat($scope.disPerPopupForGroup).toFixed(2);
    					$scope.autoRenderedServicesFinalList[j].discountCategoryId = $scope.disCategoryIdForGroup;
    					
    					calDicAmtForPopup = (finalTotalAmtForGroup*$scope.disPerPopupForGroup/100).toFixed(2);	
    					$scope.autoRenderedServicesFinalList[j].discount = calDicAmtForPopup;
    					//for remove discount
    		    		if($scope.autoRenderedServicesFinalList[j].discountDetailsList.length > 0){
    						angular.forEach($scope.autoRenderedServicesFinalList[j].discountDetailsList, function (value, key) {
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
    		    		//end remove discount  
    					$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
    					
    				}
    			j++;
	    	});
			
			}
    	
    		var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    		$scope.calculationTest(autoRenderedServicesList,discountPer);
    	}	
    }
    
    
    $scope.cancelDiscountForServices = function()
    {
    	if($scope.discountDisabledFlagForRequest == false){
    	console.log($scope.servicesSelectedList.services);
    	
    	var len = $scope.servicesSelectedList.services.length;
		if(len == 0)
			{
				growl.warning("Please select at least one service!",{
					title : ERROR_MSG
				});
	    		return false;
			}
		else
			{
				angular.element('#cancelDiscountPopup').modal('show');
			}
    	}
    	
    }
    
    $scope.finalCancelDiscount = function()
    {
    	
    	var len = $scope.servicesSelectedList.services.length;
    	
    	var cancelDiscountObjectList = [];
    	var cancelRemoveDiscountList = [];
    	for(var i = 0;i < len;i++)
		{
			var j = 0;
			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				
				var totalAmt = 0;
				var dis = 0;
				var gstAmt = 0;
				var withConDic = 0;
    			if(value.serviceMasterId == $scope.servicesSelectedList.services[i] && value.discount > 0)
    				{
    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
					$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
					$scope.autoRenderedServicesFinalList[j].discount = 0;
					
					totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
					withConDic =parseFloat(totalAmt - (parseFloat(value.concession) + parseFloat(dis))).toFixed(2);

					$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
					
					$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);

					$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);

					$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					
					
					
					//for remove discount
		    		if($scope.autoRenderedServicesFinalList[j].discountDetailsList.length > 0){
						angular.forEach($scope.autoRenderedServicesFinalList[j].discountDetailsList, function (value1, key) {
							if(value1.orderDetailsDiscountId > 0)
				    		{
				    			var disArr = {};
				    			disArr.orderDetailsDiscountId  = value1.orderDetailsDiscountId;
				    			disArr.orderDetailsId = value1.orderDetailsId;
				    			disArr.discountCategoryId = value1.discountCategoryId;
				    			disArr.discountPercentage = value1.discountPercentage;
				    			disArr.discountAmount = value1.discountAmount;
				    			disArr.isCancel = 'Y';
				    			disArr.cancelBy  = $scope.userId;
				    			disArr.cancelReasonId  = "";
				    			disArr.remark ="";
				    			disArr.createdBy  = value1.createdBy;
				    			disArr.createdDate  = value1.createdDate;
				    			disArr.updatedBy  = $scope.userId;
				    			disArr.updatedDate  = new Date().getTime();
				    			disArr.cancelledDate = new Date().getTime();
				    			disArr.orgId  = $scope.organizationId;
				    			disArr.unitId  = $scope.unitId;
				    			disArr.discountDate = new Date().getTime();
				    			disArr.status = "I";
				    			disArr.isConsessionDiscount = "N";
				    			cancelRemoveDiscountList.push(disArr);
				    		}
			    			
			    			
			    		});
					}
		    		//end remove discount  
		    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
		    		if(value.orderDetailsId > 0)
		    		{
		    			cancelDiscountObjectList.push($scope.autoRenderedServicesFinalList[j]);
		    		}
    			}
    			j++;
	    	});
			
		}//for End
    	
    	if(cancelDiscountObjectList.length > 0){
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
		var reVal = $scope.calculationTest(autoRenderedServicesList,discountPer);
    	//return false;
    	//for SaveCancel
    	if(reVal == 1)
    	{
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
				isFinalBill : 'N',
				ordDoctorId :$scope.encounterDetails.doctorId,
				visitTypeId : '1',
				billDateTime : new Date().getTime(),
				netAmount : $scope.netPayableSummary,
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : cancelRemoveDiscountList
				
		};
    	
    	var saveOrderDetailsObj = [];
		angular.forEach(cancelDiscountObjectList, function (value, key) {
			var saveArr = {};
			
			saveArr.orderDetailsId = value.orderDetailsId;
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			saveArr.orderDate = new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.serviceAmt = value.basePrice;
			saveArr.ordConcession = value.concession;
			saveArr.ordDiscount = value.discount;
			saveArr.netAmount = value.netPay; //P
			saveArr.ordDocSplId = value.specialityId;
			saveArr.taxId = value.taxId;
			saveArr.taxPer  = value.taxPercentage;
			saveArr.taxAmount = value.taxAmt; //P
			saveArr.selfPayable = value.selfPayable; //P
			saveArr.creditPayable = value.creditPayable;
			saveArr.payeeId = $scope.encounterDetails.payeeId;
			saveArr.ordTotalAmt =value.totalAmt; //P
			saveArr.tariffId = $scope.defaultSelfTariffId;
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			saveArr.status = 'A';
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = $scope.userId;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2); //P
			saveArr.ordCancelled = 'N';
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.discountDetailsList = [];
			saveOrderDetailsObj.push(saveArr);
		});
		data.orderDetailsMasterDtosList = saveOrderDetailsObj;
		data.cancelOrderDetailsList = [];
    	console.log("CANCEL DATA",data);
    	
    	var URI = BASE_URL + ROOT_URL + SAVEBILL;
		BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			console.log(response)
			if(response.data.status == 'success'){
				//alert("Success SET");
				
				growl.success("Discount Canceled Successfully!!",{
					title : SUCCESS_MSG
				});
				$scope.servicesSelectedList = {
					    services: []
					  };
				//$scope.init();
			}
			
		});
		
    }
   }
    	//for End SaveCnacel
    	
    }
	// function for popup ok button call
	$scope.PopupOkBtn = function() {
		$scope.popUpFlag = false;
		$scope.saveBillings();
	}
	
	$scope.saveBillings = function(){
		//alert($scope.finalBill);
		console.log("SAVE ORDER DETAILS",$scope.autoRenderedServicesFinalList);
		
		console.log("tttt",$scope.removeDiscountList);
		//return false;
		
		if($scope.billWiseDiscountObj.requestAmountBillWise > 0){
			if($scope.billWiseDiscountObj.requestUserId == "")
				{
					growl.warning("Please select request user for discount!",{
						title : ERROR_MSG
					});
					return false;
				}
			else
				{
				$scope.saveBillngNew();
				}
			
		}else{
			
			//$scope.AutoRenderednetTotalDiscount
			/*if($scope.AutoRenderednetTotalDiscount > 0)
				{
					if(($scope.userDiscountObject == null) || (parseFloat($scope.userDiscountObject.fromDiscountAmount) < $scope.AutoRenderednetTotalDiscount && parseFloat($scope.userDiscountObject.toDiscountAmount) < $scope.AutoRenderednetTotalDiscount))
						{
							 $scope.billWiseDiscountObj.requestAmountBillWise = $scope.AutoRenderednetTotalDiscount;
					    	   
					    	   
					    	   
					    		var URI = BASE_URL + ROOT_URL
					    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise;
					            BillingGenericService.serviceAction(METHOD_GET, URI,"",
					    				NOTIFICATION_MSG_STATUS_FALSE)
					    		.then(function(response) {
					    					console.log(response);
					    					// $rootScope.startLoader();
					    					if (response.data.status == "success")
					    						{
					    						  $scope.userDiscountList =[];
					    						  $scope.userDiscountList = response.data.listObject;
					    						}
					    						
					    				});
					    
							   $scope.requestDiscountDiv = true;
							   growl.warning("Please sent discount request to user",{
									title : ERROR_MSG
								});
					    		return false;
						}
					else
						{
						  $scope.saveBillngNew();
						}
			}
			else
				{
				  $scope.saveBillngNew();
				}*/
			$scope.saveBillngNew();
		}//for DD
		
	}
	
	$scope.saveBillngNew = function()
	{
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
				netAmount : $scope.netPayableSummary,
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : $scope.removeDiscountList,
				tariffId : $scope.defaultSelfTariffId,
				paymentEntitlementId : $scope.encounterDetails.paymentEntitlementTypeId,
				visitTypeId : $scope.visitTypeId,
				patientTypeId : $scope.patientDetails.patientTypeId,
				billingBedCategoryId : 1,//default op 
				isInterimBill :'N',
				isDrNote:"N",
				counterId:$rootScope.counterId,
				billingMasterId : $scope.addendumBill.billingMasterId,
				difference : $scope.billSummary.difference,
				isFinalBill : 'Y',
				billNumber : $scope.addendumBill.billNumber
		};
		
		if($scope.requestStatusObject !=null && $scope.requestStatusObject.reqStatus == 2)
			{
				data.reqDiscId = $scope.requestStatusObject.reqDiscId;
			}
		
		//for DD
		if($scope.billWiseDiscountObj.requestAmountBillWise > 0){
			data.reqDisAmt = $scope.billWiseDiscountObj.requestAmountBillWise;
			data.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
			data.discType = 1;
		}
		//FOR DD
		var billPayeeList =[];
		if($scope.totalSelfPayble > 0)
			{
				var billPayeeArr ={
						payeeId : 1,
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
		
		console.log('$scope.autoRenderedServicesFinalList',$scope.autoRenderedServicesFinalList);
		//return false;
		
		var saveOrderDetailsObj = [];
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			var saveArr = {};
			if(value.orderDetailsId > 0){
				saveArr.orderDetailsId = value.orderDetailsId;
			}
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			saveArr.orderDate = new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.serviceAmt = value.basePrice;
			saveArr.ordConcession = value.concession;
			saveArr.ordDiscount = value.discount;
			saveArr.netAmount = value.netPay;
			saveArr.ordDocSplId = value.specialityId;
			saveArr.taxId = value.taxId;
			saveArr.taxPer  = value.taxPercentage;
			saveArr.taxAmount = value.taxAmt;
			saveArr.selfPayable = value.selfPayable;
			saveArr.creditPayable = value.creditPayable;
			//saveArr.payeeId = $scope.encounterDetails.payeeId;
			saveArr.payeeId = value.payeeId;
			saveArr.ordTotalAmt =value.totalAmt;
			saveArr.tariffId = $scope.defaultSelfTariffId;
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			saveArr.status = value.status;
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = value.createdBy;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
			saveArr.ordCancelled = value.ordCancelled;
			
			saveArr.ordCancelReasonId  = value.ordCancelReasonId;
			saveArr.ordCancelDatetime  = value.ordCancelDatetime;
			saveArr.ordCancelRemark  = value.ordCancelRemark;
			saveArr.oldOrdDtlId = value.oldOrdDtlId;
			
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
			saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
			saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
			saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
			saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
			saveArr.isCapService = value.isCapService;
			saveArr.packageConsumptionAmt = value.packagePayable;
			saveArr.departmentId = value.specialityId;
			saveArr.subDepartmentId = value.subSpecialityId;
			saveArr.isRefDoctorShare = value.isRefDoctorShare;
			
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.serviceRenderingDeptId = value.subSpecialityId; 
			
			saveArr.encounterId = $scope.encounterDetails.encounterId;
			saveArr.discountDetailsList = [];
			saveArr.serviceAmt = value.isCapService == 'Y' ? value.packagePayable : value.basePrice;
			saveArr.discountDetailsList = [];
			
			if(value.discountDetailsList == null)
				{
				value.discountDetailsList = [];
				}
			
			if(Object.keys(value.discountDetailsList).length > 0)
				{
				angular.forEach(value.discountDetailsList, function (value1, key1) {
					var discounntArr = {};
					if(value1.orderDetailsDiscountId > 0)
						{
							discounntArr.orderDetailsDiscountId = value1.orderDetailsDiscountId;
							discounntArr.orderDetailsId  = value1.orderDetailsId;
						}
					discounntArr.discountCategoryId = value1.discountCategoryId;
					discounntArr.discountPercentage = value1.discountPercentage;
					discounntArr.discountAmount  = value1.discountAmount;
					discounntArr.discountDate = new Date().getTime();
					discounntArr.status = 'A';
					discounntArr.updatedBy = $scope.userId;
					discounntArr.updatedDate =new Date().getTime();
					discounntArr.createdBy  = value1.createdBy;
					discounntArr.createdDate  = value1.createdDate;
					saveArr.discountDetailsList.push(discounntArr);
				});
				
				}
			else
				{
					if(value.discount > 0)
						{
							var discounntArr = {};
							
							discounntArr.discountCategoryId = value.discountCategoryId;
							discounntArr.discountPercentage = value.discountPercentage;
							discounntArr.discountAmount  = value.discount;
							discounntArr.discountDate = new Date().getTime();
							discounntArr.status = 'A';
							discounntArr.updatedBy = $scope.userId;
							discounntArr.updatedDate =new Date().getTime();
							discounntArr.createdDate = new Date().getTime();
							discounntArr.createdBy = $scope.userId;
							saveArr.discountDetailsList.push(discounntArr);
					
						}
				}
			
			//for DD
				if($scope.billWiseDiscountObj.requestAmountBillWise > 0)
					{
					  var calDicAmt = ((parseFloat(value.totalAmt) - parseFloat(value.concession))*$scope.billWiseDiscountObj.discountPercentageBillWise/100).toFixed(2);	
					  saveArr.reqDisAmt  = calDicAmt;
					}
			//For DD
			saveOrderDetailsObj.push(saveArr);
		});
		data.orderDetailsMasterDtosList = saveOrderDetailsObj;
		data.cancelOrderDetailsList = $scope.removeServicesList;
		
		
		console.log("finalSaveData",data);
		//return false;
		
			if($scope.finalBill == 'Y')
			{
				if ($scope.popUpFlag) {
					$scope.showModal = !$scope.showModal;

				}
				else{
					$rootScope.startLoader();
						$scope.popUpFlag = true;
						console.log("finalSaveData",data);
						console.log(JSON.stringify(data));
						//return false;
						var URI = BASE_URL + ROOT_URL + "/api/billing/saveAddendumBill";
						BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
							//console.log(response)
							if(response.data.status == 'success'){
								//alert("Success SET Billed");
								$rootScope.stopLoader();
								$scope.btnDisabled = false;
								dataService.addendumBill = {};
								$state.go('addendumBill');
								
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
									  $state.go('addendumBill');
									}
								else
								{
									$state.go('addendumBill');
								}
								
								//angular.element('#collectPayment').modal('show');
							}
							
						});
						
						
				}
			}
			else
				{
				if ($scope.popUpFlag) {
					$scope.showModal = !$scope.showModal;

				}
				else{
					//alert("Direct");
					
					$rootScope.startLoader();
					console.log("finalSaveData",data);
					//return false;
					var URI = BASE_URL + ROOT_URL + "/api/billing/saveAddendumBill";
					BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
						console.log(response)
						if(response.data.status == 'success'){
							//alert("Success SET");
							$rootScope.stopLoader();
							$scope.btnDisabled = false;
							$state.go('addendumBill');
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
						 }
							$scope.init();
						}
						
					});
					
				}
					
				}
	}
	
	$scope.getSpecialityByDoctor = function(docId,idx)
		{
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
						console.log(response);
						
						// $rootScope.startLoader();
						if (response.data.status == "success")
							{
							//$scope.docSpecialityList = response.data.listObject;
							$scope.servicesListPopup[idx].docSpecialityList = response.data.listObject;
							}
							
					});
	}
	
	
/***=========================Payer tab=========================***/
	
	$scope.newRequestCalculates = function(){
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
	$scope.getAuthrizationDetails = function()
	{
		
		
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
	    var param = {"payeeId":$scope.encounterDetails.payeeId,"organisationId":$scope.organizationId,"unitId":$scope.unitId,"patientId":$scope.patientDetails.patientId, 
	    		"isSplitStatus": "N","authStatusId": 1};
	    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListByPayeeId"
	    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
	  if (response.data.status == "success"){
	    $scope.addNewRequestList = response.data.listObject;
	    $scope.newRequestCalculates();
	  }
	     $rootScope.stopLoader();
	 });
	}

	$scope.showSplitBill = function()
	{
		$scope.serviceList = [];
		$scope.getAuthrizationDetails();
		$scope.totalCreditPaybleSplitBill =$scope.totalCreditPayble ;
		$scope.totalSelfPaybleSplitBill = $scope.totalSelfPayble;
		
		$scope.serviceList = angular.copy($scope.autoRenderedServicesFinalList);
		$scope.serviceListOLD = angular.copy($scope.autoRenderedServicesFinalList);
		$('#splitBillModal').modal('show');
	}
	
	
  
	
	$scope.applySplitBill = function()
	{
		
		if(!$scope.isSplitBillApplied)
        {
		$scope.isSplitBillApplied = true;
		$scope.authsummarySplitBill = parseFloat($scope.authsummarySplitBill);
		if($scope.authsummarySplitBill>0.00)
			{

			$scope.amountTobeDevided = 0.00;
			//$scope.amountTobeDevided = $scope.totalCreditPaybleSplitBill -1000; 
			
			/*$scope.authsummarySplitBill*/
			
			
			angular.forEach($scope.serviceList,function(value,index)
			 {
				value.creditPayable = parseFloat(value.creditPayable);
				 value.selfPayable = parseFloat( value.selfPayable);
				if(value.selfPayable==undefined || isNaN(value.selfPayable))
					{
					 value.selfPayable=parseFloat(0);
					}
				if(value.creditPayable>0 && $scope.authsummarySplitBill>0.00)
				{
					
				  if($scope.authsummarySplitBill > value.creditPayable)
					  {
					    $scope.authsummarySplitBill  = ($scope.authsummarySplitBill - value.creditPayable);
					  }
				  else{
					  
					  var amount = 0.00;
					  amount = value.creditPayable - $scope.authsummarySplitBill;
					  value.creditPayable = $scope.authsummarySplitBill;
					  value.selfPayable = value.selfPayable + parseFloat(amount);
					  $scope.authsummarySplitBill  =0.00;
				  }
				}
				  else
				  {
					  value.selfPayable = value.selfPayable + value.creditPayable;
					  value.creditPayable = 0.00;
					  
				  }
					
			 });
			}
		else
			{
			angular.forEach($scope.serviceList,function(value,index)
					 {
				        value.selfPayable = value.selfPayable + value.creditPayable;
				        value.creditPayable = 0.00;
							
					 });
			}
		    $scope.calculationTest($scope.serviceList,1);
         }
		
	}
	
	$scope.resetSplitBill = function()
	{
		$scope.authsummarySplitBill = $scope.authsummary;
		$scope.serviceList = angular.copy($scope.serviceListOLD);
		$scope.calculationTest($scope.serviceListOLD,1);
		$scope.showSplitBill();
		$scope.isSplitBillFlag =false;
		$scope.isSplitBillApplied = false;
	}
	$scope.saveSplitBill = function()
	{
		$scope.autoRenderedServicesFinalList = $scope.serviceList;
		$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
		$scope.isSplitBillFlag =true;
		$scope.isSplitBillApplied = true;
		
	}
	/***=========================Payer tab=========================***/
	
	
    
	 /******* START TAB FOR Payment Mode *******/
	
	
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.firstTabLevel = newTab;
		if(newTab==3)
			{
			$scope.getAuthrizationDetails();
			}
	};
	
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	 /******* END TAB FOR Payment Mode *******/
	
	/** Yogesh Start **/
	
	$scope.initPackageSearchObj = function(){
		$scope.packageSearchObj = {packageTypeId : "1", serviceCode:"",serviceName:""}
	}
	
	$scope.getPackageTypeList = function(orgId,unitId) {
		var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
		BillingGenericService.serviceAction("GET", URI, null).then(function(response){
			$scope.packageTypeList = response.data.listObject;
		});
	}
	
	
	$scope.searchPackages = function(packageSearchObj){
			packageSearchObj.organisationId = $scope.organizationId;
			packageSearchObj.unitId = $scope.unitId;
			packageSearchObj.visitTypeId = 1;//default op
			packageSearchObj.bedBillingCategoryId = $scope.opdBillingCatId;//default op
			packageSearchObj.genderId = $scope.patientDetails.genderId;
			var URI = BASE_URL + ROOT_URL + "/api/packages/bill/services/search";
			return BillingGenericService.serviceAction("POST", URI, packageSearchObj).then(function(response){
				if(response.data.status == "success")
					return response.data.listObject;
			});
	}
	
	$scope.selectPackageService = function($item, $model, $label){
		var packageIds = [];
		angular.forEach($scope.selectedPackageList,function(value,index){
			packageIds.push(value.packageMasterId);
		});
		if(packageIds.indexOf($item.packageMasterId) == -1){
			$scope.selectedPackageList.push($item);
			$scope.initPackageSearchObj();
		}else{
			growl.error("Package Already Added",{
				title : ERROR_MSG
			});
		}
	}
	
	$scope.removeSelectedPackage = function(index){
		$scope.selectedPackageList.splice(index,1);
	}
	
	$scope.getPackages = function(){
		var packageIds = [];
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,key){
			if(value.isPackage && value.isPackageService != 'Y' && packageIds.indexOf(value.packageMasterId) == -1 && $scope.activePackageIds.indexOf(value.packageMasterId) == -1)
				packageIds.push(value.packageMasterId);
		});
		//get package details
		if(packageIds.length > 0){
			var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
			BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				if(response.data.status == "success")
					$scope.packageDetailsList = response.data.listObject;
			});
		}
	}
	$scope.getActivePackagesList = function(){
		$scope.activePackageIds = [];
		if($scope.patientDetails !== undefined && $scope.patientDetails !== null){
            if($scope.patientDetails.patientId === undefined || $scope.patientDetails.patientId === null || $scope.patientDetails.patientId ===''){
                $scope.patientDetails.patientId = 0;
            }
        }
        else{
            $scope.patientDetails.patientId = 0;
        }
		var URI =BASE_URL + ROOT_URL + "/api/packages/patientId/"+$scope.patientDetails.patientId+"/encounterId/"+$scope.encounterDetails.encounterId+"/visitTypeId/1";
		var header = {organisationId: $scope.organizationId,unitId:$scope.unitId};
		GenericServiceParamHeader.serviceAction("GET", URI, null,NOTIFICATION_MSG_STATUS_FALSE,null,header).then(function(response){
			if(response.data.status == "success"){
				$scope.activePackagesList = response.data.listObject;
			$scope.activePackagesList.forEach(function(value,index){
				$scope.activePackageIds.push(value.packageId);
			})
		}
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
	
	var isValidPackageService = function(item, setPackageService){
		var serviceId = typeof(item.serviceMasterId) != "undefined" && item.serviceMasterId != null ? item.serviceMasterId : item.serviceId;
		var packageIds = [];
		var flag = false;
		var serviceFoundFlag = false;
		$scope.autoRenderedServicesFinalList.forEach(function(value,key){
			if(value.isPackage && value.isPackageService != 'Y')
				packageIds.push(value.packageMasterId);
			else if(typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null)
				packageIds.push(value.packageMasterId);
		});
		
		$scope.activePackagesList.forEach(function(value,index){
				packageIds.push(value.packageId);
		});
		
		packageIds = Array.from(new Set(packageIds));
		if(packageIds.length > 0){
		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
		BillingGenericService.serviceAction("GET", URI, null).then(function(response){
			if(response.data.status == "success"){
				var packages = response.data.listObject;
				packages.forEach(function(value,index){
					if(serviceFoundFlag)
						return;
					switch(value.mPackageMasterDto.packageTypeId){
					case 1://EHC package
						break;
					case 2://Either or Package
						var services = value.listTPackageServicesDetailsDto;
						var breakEo = false;
						services.forEach(function(serv,i){
							if(breakEo)
								return;
							if(serv.serviceId == serviceId && (serv.packageEitherOrGroupId != "" && serv.packageEitherOrGroupId != null && typeof(serv.packageEitherOrGroupId) != "undefined")){
								//get eo group details
								var listTPackageEitherorGroupDetailsDto = value.listTPackageEitherorGroupDetailsDto;
								//check services in either or
								listTPackageEitherorGroupDetailsDto.forEach(function(eoGroup){
									if(eoGroup.packageGroupDetailsId == serv.packageEitherOrGroupId){
										var noOfAvailableQty = eoGroup.numberServiceUsable;
										var count = 0;
										$scope.autoRenderedServicesFinalList.forEach(function(addedServ){
											//check no of services consumed
											if(addedServ.isPackageService == "Y" && addedServ.packageId == value.mPackageMasterDto.packageMasterId && eoGroup.packageGroupDetailsId == addedServ.packageEitherOrGroupId){
												count++;
											}
										});
										$scope.servicesListPopup.forEach(function(addedServ){
											//check no of services consumed
											if(addedServ.isPackageService == "Y" && addedServ.packageId == value.mPackageMasterDto.packageMasterId && eoGroup.packageGroupDetailsId == addedServ.packageEitherOrGroupId){
												count++;
											}
										})
										if(count < noOfAvailableQty){
											serv.packageId = value.mPackageMasterDto.packageMasterId;
											serv.isPackageService = "Y";
											serv.packageTypeId = 2;//Either or Package
											serv.isAvailableInCurrentPackage = true;
											serv.packagePayable = serv.apportionedPrice;
											serviceFoundFlag = true;
											breakEo = true;
											setPackageService(serv);
											return;
										}else if(count == noOfAvailableQty){
											serviceFoundFlag = true;
											breakEo = true;
											setPackageService({});
										}else{
											breakEo = true;
											setPackageService({});
										}
									}
								});
								
							}else if(serv.serviceId == serviceId && (serv.packageEitherOrGroupId == "" || serv.packageEitherOrGroupId == null || typeof(serv.packageEitherOrGroupId) == "undefined")){
								//check independently in service
								var noOfAvailableQty = serv.numberToBeUse;
								var count = 0;
								$scope.autoRenderedServicesFinalList.forEach(function(addedServ){
									//check no of services consumed
									if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageId == value.mPackageMasterDto.packageMasterId){
										count++;
									}
								});
								$scope.servicesListPopup.forEach(function(addedServ){
									//check no of services consumed
									if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageId == value.mPackageMasterDto.packageMasterId){
										count++;
									}
								});
								
								if(count < noOfAvailableQty){
									serv.packageId = value.mPackageMasterDto.packageMasterId;
									serv.isPackageService = "Y";
									serv.packageTypeId = 2;//Either or Package
									serv.isAvailableInCurrentPackage = true;
									serv.packagePayable = serv.apportionedPrice;
									serviceFoundFlag = true;
									breakEo = true;
									setPackageService(serv);
									return;
								}else if(count == noOfAvailableQty){
									serviceFoundFlag = true;
									breakEo = true;
									setPackageService({});
								}else{
									breakEo = true;
									setPackageService({});
								}
							}
						});
						break;
					case 3:// package with cap
						var count = 0;
						var services = value.listTPackageServicesDetailsDto;
						var breakCapFlag = false;
						services.forEach(function(serv,i){
							if(breakCapFlag)
								return;
							if(serv.serviceId == serviceId){
								var noOfAvailableQty = serv.numberToBeUse;
								count = 0;
								$scope.autoRenderedServicesFinalList.forEach(function(addedServ){
									if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageMasterId == value.mPackageMasterDto.packageMasterId){
										count++;
									}
								});
								$scope.servicesListPopup.forEach(function(addedServ){
									//check no of services consumed
									if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageId == value.mPackageMasterDto.packageMasterId){
										count++;
									}
								});
								
								if(count < noOfAvailableQty){
									serv.packageId = value.mPackageMasterDto.packageMasterId;
									serv.isPackageService = "Y";
									serv.packageTypeId = 3;//package with cap
									serv.packagePayable = serv.apportionedPrice;
									serv.isAvailableInCurrentPackage = true;
									serviceFoundFlag = true;
									breakCapFlag=true;
									setPackageService(serv);
									return;
								}else if(count >= noOfAvailableQty){
									serviceFoundFlag = true;
									breakCapFlag=true;
									setPackageService({});
								}else{
									setPackageService({});
								}
							}
						});
						
						//check inclusion and exclusions
						var incExcServices = value.listTPackageIncExcDetailsDto;
						if(incExcServices != null && incExcServices != ""){
							incExcServices.forEach(function(incExcSrv){
								if(incExcSrv.serviceId == serviceId && incExcSrv.packageId == value.mPackageMasterDto.packageMasterId && incExcSrv.isInclusiveService == 1){
									//inclusion service
									incExcSrv.packageId = value.mPackageMasterDto.packageMasterId;
									incExcSrv.isPackageService = "Y";
									incExcSrv.packageTypeId = 3;
									incExcSrv.isAvailableInCurrentPackage = true;
									serviceFoundFlag = true;
									breakCapFlag=true;
									setPackageService(incExcSrv);
									return;
								}else if(incExcSrv.serviceId == serviceId && incExcSrv.packageId == value.mPackageMasterDto.packageMasterId && incExcSrv.isInclusiveService == 2){
									//exclusion service
									serviceFoundFlag = true;
									breakCapFlag=true;
									setPackageService({});
								}
							});
						}
						
						//check in cap
						if(count == 0 && !serviceFoundFlag && !breakCapFlag){
							var capDetailsList = value.listTPackageGroupWiseCapDetailsDto;
							//get consumed cap amounts
							var consumedCapDetails = {};
							capDetailsList.forEach(function(cap,i){
								$scope.autoRenderedServicesFinalList.forEach(function(addedServ){
									if(addedServ.isCapService == 'Y' && addedServ.packageCapDetailsId == cap.packageCapDetailsId){
										if(cap.departmentId != null && cap.subDepartmentId == null && (item.specialityId == cap.departmentId))
											consumedCapDetails[addedServ.packageCapDetailsId] = $rootScope.parseFloatNum(cap.departmentCapAmount) - $rootScope.parseFloatNum(addedServ.apportionedPrice);
										else if(cap.departmentId != null && cap.subDepartmentId != null && (item.specialityId == cap.departmentId && item.subSpecialityId == cap.subDepartmentId))
											consumedCapDetails[addedServ.packageCapDetailsId] = $rootScope.parseFloatNum(cap.subDepartmentCapAmount) -  $rootScope.parseFloatNum(addedServ.apportionedPrice);
									}
								});
								$scope.servicesListPopup.forEach(function(addedServ){
									if(addedServ.isCapService == 'Y' && addedServ.packageCapDetailsId == cap.packageCapDetailsId){
										if(cap.departmentId != null && cap.subDepartmentId == null && (item.specialityId == cap.departmentId))
											consumedCapDetails[addedServ.packageCapDetailsId] = $rootScope.parseFloatNum(cap.departmentCapAmount) - $rootScope.parseFloatNum(addedServ.apportionedPrice);
										else if(cap.departmentId != null && cap.subDepartmentId != null && (item.specialityId == cap.departmentId && item.subSpecialityId == cap.subDepartmentId))
											consumedCapDetails[addedServ.packageCapDetailsId] = $rootScope.parseFloatNum(cap.subDepartmentCapAmount) -  $rootScope.parseFloatNum(addedServ.apportionedPrice);
									}
								});
							});
							
							//---------- check in cap ---------
								capDetailsList.forEach(function(cap,i){
									if(cap.isServiceItem == 1){
										if(breakCapFlag)
											return;
										//check cap is group or sub group wise
										if(cap.departmentId != null && cap.subDepartmentId == null && (item.specialityId == cap.departmentId)){
											//check service amount fits in department amount
											if(item.basePrice <= cap.departmentCapAmount ){
												if(Object.keys(consumedCapDetails).length > 0 && item.basePrice >= consumedCapDetails[cap.packageCapDetailsId]){
													serviceFoundFlag = true;breakCapFlag=true;
													setPackageService(item);
													return;
												}else{
													item.packageId = value.mPackageMasterDto.packageMasterId;
													item.isPackageService = "Y";
													item.packageTypeId = 3;//package with cap
													item.isAvailableInCurrentPackage = true;
													item.isCapService = 'Y';
													item.apportionedPrice = item.basePrice;
													item.packagePayable =  item.basePrice;
													item.basePrice = 0;
													item.packageCapDetailsId = cap.packageCapDetailsId;
													serviceFoundFlag = true;
													breakCapFlag=true;
													setPackageService(item);
													return;
												}
											}
											
										}else if(cap.departmentId != null && cap.subDepartmentId != null && (item.specialityId == cap.departmentId && item.subSpecialityId == cap.subDepartmentId)){
											//check service amount fits in subdepartment amount
											if(item.basePrice <= cap.subDepartmentCapAmount ){
												if(Object.keys(consumedCapDetails).length > 0 && item.basePrice >= consumedCapDetails[cap.packageCapDetailsId]){
													serviceFoundFlag = true;breakCapFlag=true;
													setPackageService(item);
													return;
												}else{
													item.packageId = value.mPackageMasterDto.packageMasterId;
													item.isPackageService = "Y";
													item.packageTypeId = 3;//package with cap
													item.isAvailableInCurrentPackage = true;
													item.isCapService = 'Y';
													item.apportionedPrice = item.basePrice;
													item.packagePayable =  item.basePrice;
													item.basePrice = 0;
													item.packageCapDetailsId = cap.packageCapDetailsId;
													serviceFoundFlag = true;
													breakCapFlag=true;
													setPackageService(item);
													return;
												}
											}
										}
									}
								});
						}
						
						break;
					case 4://All inclusive package
						var breakAllInc = false;
						if(breakAllInc)
							return;
						var services = value.listTPackageIncExcDetailsDto;
						var exclusionServIds = [];
						services.forEach(function(serv,i){
							exclusionServIds.push(serv.serviceId);
						});
						
						if(exclusionServIds.indexOf(serviceId) == -1){
							item.packageId = value.mPackageMasterDto.packageMasterId;
							item.isPackageService = "Y";
							item.packageTypeId = 4;//All inclusive package
							item.apportionedPrice = item.basePrice;
							item.packagePayable =  item.basePrice;
							item.basePrice = 0;
							item.isAvailableInCurrentPackage = true;
							serviceFoundFlag = true;
							breakAllInc = true;
							setPackageService(item);
							return;
						}
						
						if(!serviceFoundFlag){
							breakAllInc = true;
							serviceFoundFlag = true;
							setPackageService({});
							return;
						}
						break;
					case 5://multi encounter package
						//check wether service available in package
						
						var count = 0;
						var services = value.listTPackageServicesDetailsDto;
						var breakMultiFlag = false;
						
						//check in database
				    	var URI = BASE_URL + ROOT_URL + "/api/packages/service/exists";
				    	var param = {patientId:$scope.patientDetails.patientId,encounterId:$scope.encounterDetails.encounterId,visitType:1,serviceId:serviceId,organisationId:$scope.organizationId,unitId:$scope.unitId,
				    				tariffId:$scope.defaultSelfTariffId,paymentEntitlementId:$scope.encounterDetails.paymentEntitlementTypeId,patientTypeId:$scope.patientDetails.patientTypeId,billingBedCategoryId:1};
				    	serviceFoundFlag = true;//avoid adding service before response comes
				    	BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				    		if(response.data.status == "success"){
				    			var serv = response.data.object;
				    			var noOfAvailableQty = 0;
				    			services.forEach(function(serv,i){
									if(serv.serviceId == serviceId)
										noOfAvailableQty = serv.numberToBeUse;
									});		
				    			count = 0;
								$scope.autoRenderedServicesFinalList.forEach(function(addedServ){
									if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageId == value.mPackageMasterDto.packageMasterId){
										count++;
									}
								});
								$scope.servicesListPopup.forEach(function(addedServ){
									//check no of services consumed
									if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageId == value.mPackageMasterDto.packageMasterId){
										count++;
									}
								});
								
								//check qty from active packages
								$scope.activePackagesList.forEach(function(activePack){
									activePack.listPackageConsumptionServiceDetailDto.forEach(function(service){
										if(serviceId == service.serviceId || serviceId == service.serviceId.toString()){
											count = count + (service.packageQuantity - service.balanceQuantity);
										}
									});
								});
				    			
								if(count < noOfAvailableQty){
					    			item.packageId = value.mPackageMasterDto.packageMasterId;
					    			item.isPackageService = "Y";
					    			item.packageTypeId = 5;//multi encounter package
					    			item.packagePayable = serv.apportionedPrice;
					    			item.isAvailableInCurrentPackage = true;
					    			item.basePrice = 0;
									serviceFoundFlag = true;
									breakMultiFlag=true;
									setPackageService(item);
									return;
								}else if(count >= noOfAvailableQty){
									serviceFoundFlag = true;
									breakMultiFlag=true;
									setPackageService({});
								}else{
									setPackageService({});
								}
								
				    		}else{
				    			//check locally
				    			serviceFoundFlag = false;
				    			services.forEach(function(serv,i){
									if(breakMultiFlag)
										return;
									if(serv.serviceId == serviceId){
										var noOfAvailableQty = serv.numberToBeUse;
										count = 0;
										$scope.autoRenderedServicesFinalList.forEach(function(addedServ){
											if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageId == value.mPackageMasterDto.packageMasterId){
												count++;
											}
										});
										$scope.servicesListPopup.forEach(function(addedServ){
											//check no of services consumed
											if(addedServ.isPackageService == "Y" && addedServ.serviceMasterId == serviceId && addedServ.packageId == value.mPackageMasterDto.packageMasterId){
												count++;
											}
										});
										
										//check qty from active packages
										$scope.activePackagesList.forEach(function(activePack){
											activePack.listPackageConsumptionServiceDetailDto.forEach(function(service){
												if(serviceId == service.serviceId || serviceId == service.serviceId.toString()){
													count = count + (service.packageQuantity - service.balanceQuantity);
												}
											});
										});
										
										if(count < noOfAvailableQty){
											serv.packageId = value.mPackageMasterDto.packageMasterId;
											serv.isPackageService = "Y";
											serv.packageTypeId = 5;//package with cap
											serv.packagePayable = serv.apportionedPrice;
											serv.isAvailableInCurrentPackage = true;
											serv.basePrice = 0;
											serviceFoundFlag = true;
											breakMultiFlag=true;
											setPackageService(serv);
											return;
										}else if(count >= noOfAvailableQty){
											serviceFoundFlag = true;
											breakMultiFlag=true;
											setPackageService({});
										}else{
											setPackageService({});
										}
									}
								});
				    			
				    			if(!serviceFoundFlag){
				    				serviceFoundFlag = true;
									setPackageService({});
									return;
								}
				    			
				    		}
				    	});
					}
				});
				if(!serviceFoundFlag){
					setPackageService({});
					return;
				}
			}
		});
		}else{
			setPackageService({});
		}
		
		
	}
	
	$scope.getInvoiceDetails = function(){
		if($scope.billId != "" && typeof($scope.billId) != "undefined"){
			var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/paymentRecieptByBillId";
			var param = {orgId:$scope.organizationId,orgUnitId:$scope.unitId,billId:$scope.billId};
			BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
					$scope.invoiceDetails = response.data.listObject;
			});
		}
	}
	
	$scope.getCreditNoteDetails = function(){
		if($scope.billId != "" && typeof($scope.billId) != "undefined"){
			var URI = BASE_URL + ROOT_URL + "/api/billing/creditnotes/addendum/"+$scope.billId;
			var param = {orgId:$scope.organizationId,orgUnitId:$scope.unitId,billId:$scope.billId};
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
					$scope.creditNoteDetails = response.data.listObject;
			});
		}
	}
	
	$scope.getBillServiceDetails = function(billId){
		if(billId != "" && typeof(billId) != "undefined"){
			$rootScope.startLoader();
			$scope.autoRenderedServicesBillingDetails=[];
			var URI = BASE_URL + ROOT_URL + "/api/billing/addendum/services/"+billId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					var tempAutoRendServList = [];
					angular.forEach(response.data.listObject,function(value,index){
						if(value.isPackageService == 'Y'){
							value.apportionedPrice = value.basePrice;
							value.basePrice = 0;
							value.netPay = 0;
							value.totalAmt = 0;
							value.selfPayable = 0;
						}
						value.packageMasterId = value.packageId;
						tempAutoRendServList.push(value);
					});
					$scope.autoRenderedServicesList = tempAutoRendServList;
					$scope.calculationTest($scope.autoRenderedServicesList,init);
					
					//get encounter details
					$scope.getEncounterDetails($scope.addendumBill.visitId);
					 $rootScope.stopLoader();
				}else{
				  $rootScope.stopLoader();
				}
			});
		}
	}
	$scope.init();
	
	/** Yogesh End **/
}]);
	