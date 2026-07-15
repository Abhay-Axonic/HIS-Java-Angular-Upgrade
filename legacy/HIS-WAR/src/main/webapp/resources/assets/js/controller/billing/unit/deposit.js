//directive to allow only decimal
angular.module("myApp").directive('ngDecimal', function(){
    return {
        restrict: 'A',
        link: function($scope, $element, $attributes){
            var limit = $attributes.ngDecimal;
            function caret(node) {
                if(node.selectionStart) {
                    return node.selectionStart;
                }
                else if(!document.selection) {
                    return 0;
                }
                // node.focus();
                var c		= "\001";
                var sel	= document.selection.createRange();
                var txt	= sel.text;
                var dul	= sel.duplicate();
                var len	= 0;
                try{ dul.moveToElementText(node); }catch(e) { return 0; }
                sel.text	= txt + c;
                len		= (dul.text.indexOf(c));
                sel.moveStart('character',-1);
                sel.text	= "";
                return len;
            }
            $element.bind('keypress', function(event){
            	var charCode = (event.which) ? event.which : event.keyCode;
            	var elem=document.getElementById($element.attr("id"));
            	if (charCode == 45){
                    var caretPosition=caret(elem);
                    if(caretPosition==0){
                    	if($element.val().charAt(0)!="-" ){
                    		if($element.val() <=limit){
                                $element.val("-"+$element.val());
                            }
                        }
                        if($element.val().indexOf("-")!=-1){
                        	event.preventDefault();
                            return false;
                        }
                    }
                    else{
                    	event.preventDefault();
                    }
                }
                if (charCode == 46){
                    if($element.val().length>limit-1){
                    	event.preventDefault();
                    	return false;
                    }
                    if ($element.val().indexOf('.') !=-1){
                    	event.preventDefault();
                        return false;
                    }
                    return true;
                }
                if (charCode != 45 && charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)){
                	event.preventDefault();
                    return false;
                }
                if($element.val().length>limit-1){
                	event.preventDefault();
                	return false;
                }
                return true;
            });
        }
    };
});

angular.module("myApp").controller("depositController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','reportService','$http','GenericServiceParamHeader','$location','loyaltiCardCallFactory','GenericServiceFileUpload','PagerService','$interval','$window', 'cancelReasonService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,reportService,$http,GenericServiceParamHeader,$location,loyaltiCardCallFactory,GenericServiceFileUpload,PagerService,$interval,$window, cancelReasonService){
	try{
	
	$rootScope.loginpage = true;
	
	// alert($location.host());
	/** Counter Code */
	
	
	
	
	/**
	 * ================================COUNTER CODE
	 * START======================================
	 */
	$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
	$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
	$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
	if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
		{
		     var flag = $scope.getCounterDetails();
		     if(flag){
		    	 if($rootScope.counterMstObj == undefined){
		    		 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
		    	}else{
		    		 $rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
		    		 $rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
		    	}
		     }
		     else
		     {
		    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
		     }
		}
	/**
	 * ================================COUNTER CODE
	 * END======================================
	 */
	
	$scope.dateOptionsTrancs = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
		};
	
	$scope.dateOptions = {
			formatYear : 'yyyy',
			showWeeks : false
		};
	
	$scope.dateOptionsFrom = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : moment().toDate()
		};
	
	$scope.dateOptionsSearch = {
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : new Date()
		};
	
	$scope.dateOptionsToDate = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
		};
	
	$scope.dateOptionsDob = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate: new Date()
		};
	$scope.checkDateOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate:moment().subtract(3, 'months').toDate()
		};
	
	$scope.CardDateOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate:new Date()
		};
	
	$scope.chequedDateOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : moment(new Date()).subtract(90, 'days').toDate(),
		};
	$scope.openDatePicker = function() {
		$scope.datepicker.opened = true;
	};
	
	  $scope.validatTextFieldFlag = true;
	$scope.fromDate =moment().toDate();
	$scope.toDate =moment().toDate();
	
	$scope.file = null;
	// for cheque
	$scope.openDatePickerCheque = function() {
		$scope.datepickercheque.opened = true;
	};
	$scope.datepickercheque = {
			opened : false
		};
	// ------

	$scope.datepicker = {
		opened : false
	};
	
	$scope.openDatePickerPatiSearch = function() {
		$scope.datepicker1.opened = true;
	};

	$scope.datepicker1 = {
			opened : false
		};
	
	$scope.openDatePickerFromDate = function() {
		$scope.datepickerFrom.opened = true;
	};

	$scope.datepickerFrom = {
			opened : false
		};
	
	$scope.openDatePickerToDate = function() {
		$scope.datepickerTo.opened = true;
	};

	$scope.datepickerTo = {
			opened : false
		};
	$scope.btnDisabled = false;
	$scope.skipMpesaValidation = false;
	$scope.pinpadAmounFlag = false;
	$scope.hitMpesaApi = false;
	$scope.razorMobileNo = '';
	$scope.razorAmount = '';
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
	/* $scope.fileURL = "C:/pinpadtallernumber.txt"; */
/*
 * $http.get("file:///C:/pinpadtallernumber.txt").success(function (response) {
 * alert(response); });
 */
	
        $scope.reasons = [{
            "reason":"Select Reason"
        }];

        cancelReasonService.GetReason().then(function (reason) {
                $scope.reasons = $scope.reasons.concat(reason);
        });
	
	// For Get Active Gender List
	$scope.getSexMasterList = function() {
		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.sexMasterList = response.data.listObject;
		});
	}
	
	$scope.getIdentificationTypeList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
				$scope.identificationTypeList = response.data.listObject;
		});
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
	
	// ADDED LATER GANESH
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
		$scope.paymentDetails = angular.copy({paymentModeId:$scope.paymentDetails.paymentModeId,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
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
	}
	
	$scope.initDepositObj = function(){
		$scope.deposit = angular.copy({depositTypeId:"",visitTypeId:"",depositAmount:"",remarks:"",receivedFrom:"",identificationNo:"",patientId:"",encounterId:"",counterId :$rootScope.counterId,otReservationId:""})
	}
	
	$scope.initDepositSearchObj = function(){
		$scope.depositSearch = angular.copy({fromDate:"",toDate:"",patientName:"",uhidNumber:"",visitTypeId:"",depositTypeId:"",noOfRecordsPerPage:"",offset:""});
	}
	
	$scope.initDepositSearchDtls = function(){
		$scope.depositSearchDtls = angular.copy(
		  {totalDepositAmount:parseFloat(0).toFixed(2)
			,totalConsumedDepositAmount:parseFloat(0).toFixed(2),
			totalBalanceAmount:parseFloat(0).toFixed(2)
			,totalRefundAmount:parseFloat(0).toFixed(2)});
	}
	

	$scope.init = function()
	{
		$scope.resend =false;
		$scope.send =false;
		$scope.identificationTypeList = [];
		$scope.sexMasterList = [];
		$scope.getSexMasterList();
		$scope.getIdentificationTypeList();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {}
		$scope.billing = true;
		$scope.deposit = {};
		$scope.initDepositObj();
		$scope.paymentDetails = {};
		$scope.initPaymentDetailsObj();
		$scope.listPaymentDetails = [];
		$scope.getBankList();
		$scope.getHospitalBankList();// ADDED LATER GANESH
		$scope.getCardTypesList();
		$scope.dueAmount = parseFloat(0).toFixed(2);
		$scope.depositSearch = {};
		$scope.validatTextFieldFlag = true;
		$scope.initDepositSearchObj();
		$scope.depositSearchList = [];
		$scope.depositSearchList1 = [];
		$scope.depositSearchDtls = {};
		$scope.visitNoList = [];
		
		$scope.cardDisabledAllField = true;
	}
	
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	$scope.mpesaRequired = cookieObject.mpesaRequired;
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
	$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
	$scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
	$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
	$scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
	$scope.depositTypeWiseRequired =  cookieObjectAppConfig.depositTypeWiseRequired;
	$scope.dateFormat = cookieObjectAppConfig.dateFormat;
	$scope.pinpadAmounFlag = false;

	var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
	if (cookieObjectAppConfigSecond != undefined) {
	    $scope.maxInputAmount = cookieObjectAppConfigSecond.maxInputAmount;
	}

	$scope.init();
	

	$scope.patientSearchObj = {
		      "uhIdNumber"      : "",
		      "patientName"     : "",
		      "genderId"        : "",
		      "mobileNo"        : "",
		      "birthDate"       : "",
		      "identificationNo":"",
		      "patientType"   :"1",
		      "unitId"          :$scope.unitId,
		      "organizationId"  :$scope.organizationId
		    }
    $scope.loadMoreData = function()
    {
        $scope.offset+=200;
        $scope.loadMoreFlag=true;
        $scope.searchPatient($scope.patientSearchObj);
    }
    $scope.offset=0;
    $scope.loadMoreFlag=false;
    $scope.noOfRecordsPerPage=10;
    $scope.flageForDepositSearchList1 = true;
    $scope.patientList = [];
	$scope.searchPatient = function()
	{
		$rootScope.startLoader();
		if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			$rootScope.stopLoader();
			return false;
		}	
        if($scope.loadMoreFlag==true)
        {
        	$scope.patientSearchObj.offset=$scope.offset;
        $scope.loadMoreFlag=false;
        }
    else
        {
    	$scope.patientSearchObj.offset=0;
        $scope.loadMoreFlag=false;
        $scope.patientList = [];
        }
        $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
		var patSearchObj = angular.copy($scope.patientSearchObj);
		if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
			var dt = new Date($scope.patientSearchObj.birthDate);
			patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
		} 
		BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.searchPatientList = [];
			if (response.data.status == "success")
				{
				angular.forEach(response.data.listObject,function(value,index)
						{
						    if(value.registrationTypeId != 5)
					            $scope.patientList.push(value);
						})
				
				patSearchObj = {};
				
				$scope.initPaymentDetailsObj();
				$scope.listPaymentDetails = [];
				$scope.initDepositObj();
				$scope.patientDetailsList = [];
				$scope.patientDetails = {};
				
				$rootScope.stopLoader();
				}
			else if(response.data.listObject==null){
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$scope.patientSearchObj.birthDate="";
					$scope.patientList=[];
					return false;
				}
			else if(response.data.status == "error"){
				$rootScope.stopLoader();
			}
			$scope.patientSearchObj.birthDate="";
		});
	}
	
		$scope.patientIdForLoyalty = 0;
		$scope.initialDepositAgainst=false;
		$scope.oldPatientId;
		$scope.getPatientDetailsById = function(patientId){
		$scope.initialDepositAgainst=false;
		if ($scope.oldPatientId != patientId)
			{
			$scope.oldPatientId = patientId;
			$scope.initialDepositAgainst=true;
			}else{
				$scope.oldPatientId = $scope.oldPatientId;
				$scope.initialDepositAgainst=true;
			}
		if($scope.initialDepositAgainst==true){
			$scope.changeVisit(2);
		}
		$rootScope.startLoader();
		$scope.patientIdForLoyalty = patientId;
		$scope.patientDetailsList = [];
		$scope.patientDetails = {}
		$scope.visitNoList = [];
		$scope.deposit = {};
		$scope.deposit.depositTypeId = 2;
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
				$scope.patientDetailsList = response.data.listObject;
				angular.forEach($scope.patientDetailsList,function(value,index)
						{
						value.dob =moment(value.dob,'DD/MM/YYYY').format('DD-MMM-YYYY');
						});
				$scope.patientDetails = response.data.listObject[0];
				
				if($scope.patientDetails.mobileNumber!= null){
					$scope.mobileNumber = parseInt($scope.patientDetails.mobileNumber);
					$scope.countryCallingCode = parseInt($scope.patientDetails.countryCallingCode);
					$scope.razorMobileNo =  parseInt($scope.patientDetails.mobileNumber);
					$scope.objPosMachine.mobileNumberForPOS=$scope.patientDetails.mobileNumber;
				}
				
				$scope.patientDetails.patientName = $scope.patientDetails.prefixCode+" "+$scope.patientDetails.firstName+" "+$scope.patientDetails.middleName+" "+$scope.patientDetails.lastName;
				$scope.patientDetails.age = $scope.patientDetails.ageString;
				$scope.deposit.patientId = $scope.patientDetails.patientId;
				 $scope.validatTextFieldFlag = true;
				loyaltiCardCallFactory.getLoyaltyCardList(patientId,$scope);
				if($scope.deposit.depositTypeId == 2){
					$scope.deposit.depositTypeId = 2;

				}
				$rootScope.stopLoader();
			}
			else if(response.data.status == "error"){
				$rootScope.stopLoader();
			}
		});
	}
	
	
	$scope.showPaymentCollection = function()
	{
		$scope.btnDisabled = false;
		 $scope.setPaymentModeId(1);
		 $scope.setFirstTabLevel(1);
		if($scope.deposit.patientId==null||$scope.deposit.patientId=="")
			{
			growl.warning("Please select patient..!!",{
				title : WARNING_MSG
			});
			    return false;
			}
		if($scope.deposit.depositTypeId==null||$scope.deposit.depositTypeId=="")
			{
			growl.warning("Please select deposit against..!!",{
				title : WARNING_MSG
			});
			    return false;
			}
		
		if($scope.deposit.depositTypeId==2)
		 {
			  if($scope.deposit.visitTypeId==''||$scope.deposit.visitTypeId==null)
				  {
				  growl.warning("Please select visit type..!!",{
						title : WARNING_MSG
					   });
					    return false;
				  }
			  if($scope.deposit.encounterId == undefined || $scope.deposit.encounterId==''||$scope.deposit.encounterId==null)
				  {
				  growl.warning("Please select visit number..!!",{
						title : WARNING_MSG
					   });
					    return false;
				  }
		 
		}
		
		if($scope.deposit.depositTypeId == 5)
		 {
			  if($scope.deposit.otReservationId == undefined ||$scope.deposit.otReservationId == null || $scope.deposit.otReservationId == "")
				  {
				  growl.warning("Please select Reservation No..!!",{
						title : WARNING_MSG
					   });
					    return false;
				  }
		}
		
		 if($scope.deposit.depositAmount==""||$scope.deposit.depositAmount==0||$scope.deposit.depositAmount==null)
		 {
		  growl.warning("Please enter deposit amount..!!",{
			title : WARNING_MSG
		   });
		    return false;
		 }
		else{
	      	  $('#collectPaymentModal').modal('show');
	    	 $scope.listPaymentDetails = [];
	    	 $scope.calculateDueAmount();
	    	 $scope.calculateTotalAmount();
		    }
		 $scope.patientSearchObj.identificationTypeId = "";
		 $scope.patientSearchObj.identificationNo = "";
		 $scope.patientSearchObj.birthDate = "";
		 $scope.patientSearchObj.mobileNo = "";
		 $scope.patientSearchObj.genderId = "";
		 $scope.patientSearchObj.patientName = "";
		 $scope.patientSearchObj.uhIdNumber = "";
	}
	
	$scope.getDepositDetails = function(depositMasterId)
	{
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/details/"+$scope.organizationId+"/"+$scope.userId+"/"+depositMasterId;
		BillingGenericService.serviceAction(METHOD_GET,URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			$scope.depositDetailsList = [];
			console.log("response",response);
			if (response.data.status == "success")
				{
				 $scope.depositDetailsList = response.data.listObject;
				 $('#payment_mode_Modal_deposit').modal('show');
				  $rootScope.stopLoader();
				}
			else{
				 $rootScope.stopLoader();
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
	 
	$scope.getDepositConsumption = function(depositMasterId)
	{
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/consumption/"+depositMasterId;
		BillingGenericService.serviceAction(METHOD_GET,URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			$scope.depositConsumptionList = [];
			console.log("response",response);
			if (response.data.status == "success")
				{
				 $scope.depositConsumptionList = response.data.listObject;
				 angular.forEach($scope.depositConsumptionList,function(value,key){
                     value.visitDate = moment(value.visitDate,'DD-MM-YYYY').format($scope.dateFormat.toUpperCase());
                     if(value.visitDate=='Invalid date')
                    	 {
                    	 value.visitDate='';
                    	 }else
                    		 {
                    		 value.visitDate=value.visitDate;
                    		 }
                      value.billDate = moment(value.billDate,'DD-MM-YYYY').format($scope.dateFormat.toUpperCase());
                      if(value.billDate=='Invalid date')
                 	 {
                 	 value.billDate='';
                 	 }else
                 		 {
                 		 value.billDate=value.billDate;
                 		 }
                     });
				 if($scope.depositConsumptionList!=null && $scope.depositConsumptionList.length>0)
					 {
					 $('#consumedLinkModal').modal('show');
					 }
				 
				  $rootScope.stopLoader();
				}
			else if (response.data.status == "error"){
				 $rootScope.stopLoader();
			   }
		});
	}
		
	$scope.firstTabLevel = 1 ;	
	
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.paymentDetails.paymentModeId = newTab;
		$scope.firstTabLevel = newTab;
	};
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	$scope.addBtnId=false;
	$scope.pinpadAmounFlag =false;
	$scope.setPaymentModeId = function(value)
	{
		if(value==3 && $scope.isPinPadRequired =='Y'){
			$scope.addBtnId=true;
			// $scope.pinpadAmounFlag =true;
		}else if(value==6){
			$scope.addBtnId=true;
		}
		else{
			$scope.addBtnId=false;
			 $scope.pinpadAmounFlag =false;
		}
		$scope.paymentDetails.paymentModeId = value;
	}
	
	$scope.addPaymentDetails = function(){
		
		// check amount collected is greater tha deposit amount
		var depositAmount = $scope.deposit.depositAmount != "" && typeof($scope.deposit.depositAmount) != "undefined" ? parseFloat($scope.deposit.depositAmount) : parseFloat(0) ;
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
			
			/*if($scope.paymentDetails.credCardHolderName==null||$scope.paymentDetails.credCardHolderName==''){
				growl.warning("Please enter card holder name.",{
					title : WARNING_MSG
				});
				return;
			}*/
			/*if(($scope.paymentDetails.credCardTypeId==null||$scope.paymentDetails.credCardTypeId=='') && $scope.flagForBharatQr == false){
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
			if($scope.paymentDetails.credExpiryDate==null||$scope.paymentDetails.credExpiryDate==undefined||$scope.paymentDetails.credExpiryDate==''){
				$scope.paymentDetails.credExpiryDate=null;
			}
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
			if($scope.mobileNo == null){
				growl.warning("Please Enter Mobile Number !!!",{
					title : WARNING_MSG
				});
				return false;
			}
			
			if($scope.transactionId == null){
				growl.warning("Please Enter Transaction Id !!",{
					title : WARNING_MSG
				});
				return false;
			}
			
			if(parseFloat($scope.mpesaAmount) > depositAmount){
				growl.warning("Entered amount should be equal to or less than deposit amount",{
					title : WARNING_MSG
				});
				return false;
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
			/*if($scope.eTransferObj.transBankId == "" || $scope.eTransferObj.transBankId == "-"){
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
			}*/
			if($scope.eTransferObj.transTransactionDate == ""){
				$scope.eTransferObj.transTransactionDate = null;
			}
			var invalidETransfer = false;
			angular.forEach($scope.listPaymentDetails,function(value,key){
				if(value.paymentModeId == 8 && $scope.eTransferObj.transactionNo == value.transactionNo && $scope.eTransferObj.transactionNo != "")
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
				// Get Bank Name
				angular.forEach($scope.bankList,function(value,key){
					if($scope.paymentDetails.bankId == value.bankId)
						bankName = value.bankName;
				})
				var chequeDate = $scope.getFormattedDate($scope.paymentDetails.chequeDate,false);
				var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.chequeAmount).toFixed(2),createdBy:$scope.userId, bankId:$scope.paymentDetails.bankId, chequeNumber:$scope.paymentDetails.chequeNumber, chequeDate:chequeDate, bankName:bankName,hospitalAccountId:$scope.paymentDetails.hospitalAccountId};
				$scope.listPaymentDetails.push(paymentDetails);
				break;
			case 3 :
				var expiryDate =  $scope.paymentDetails.credExpiryDate!=null ? $scope.getFormattedDate($scope.paymentDetails.credExpiryDate,false) : null;
				var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:$scope.paymentDetails.transactionNo,skipMpesaValidation:$scope.pinpadAmounFlag,requestedUniqueId:$scope.paymentDetails.creditDebitCardType,deviceName : $scope.objPosMachine.deviceName, creditDebitCardType:$scope.paymentDetails.creditDebitCardType, transactionTypeName:$scope.paymentDetails.transactionTypeName, transactionDetails:$scope.paymentDetails.transactionDetails};
				$scope.listPaymentDetails.push(paymentDetails);
				break;
			case 4 :
				var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.debExpiryDate,false);
				var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.debAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.debCardHolderName,cardTypeId:$scope.paymentDetails.debCardTypeId,cardNo:$scope.paymentDetails.debCardNumber,expiryDate:expiryDate};
				$scope.listPaymentDetails.push(paymentDetails);
				break;
			case 5:				
				var paymentDetails = { paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaAmount).toFixed(2), createdBy:$scope.userId, cardNo:$scope.transactionId,transactionNo:$scope.transactionId,skipMpesaValidation:$scope.skipMpesaValidation};
				$scope.listPaymentDetails.push(paymentDetails);
				$scope.clearMpesaFields();
				break;
				
			case 8 : 
				var bankName = "";
				// Get Bank Name
				angular.forEach($scope.bankList,function(value,key){
					if($scope.eTransferObj.transBankId == value.bankId)
						bankName = value.bankName;
				})
				var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.eTransferObj.transferAmount).toFixed(2), transactionType:$scope.eTransferObj.transactionobj.name, transactionTypeId:$scope.eTransferObj.transactionobj.id,transTransactionDate:  $scope.eTransferObj.transTransactionDate!=null ? moment($scope.eTransferObj.transTransactionDate).format('DD-MM-YYYY HH:mm:ss') : null,bankId:$scope.eTransferObj.transBankId,transactionNo:$scope.eTransferObj.transactionNo,createdBy:$scope.userId,bankName:bankName};
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
				var paymentDetails = { paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.razorPaidAmount).toFixed(2), createdBy:$scope.userId,transactionNo:$scope.razorUniqueId,paymentGatewayProviderId:$scope.payGatewayProviderId};
				$scope.listPaymentDetails.push(paymentDetails);
				break;
		}
	/*	$scope.setFirstTabLevel(1);
        $scope.setPaymentModeId(1);*/
		$scope.initPaymentDetailsObj();
		$scope.calculateDueAmount();
		$scope.calculateTotalAmount();
		$scope.pinpadAmounFlag = false;
		$scope.razorPaidAmount == null;
		
	}
	
	
	$scope.calculateTotalAmount = function(){
		var tatalAmount = parseFloat(0).toFixed();
		angular.forEach($scope.listPaymentDetails,function(value,key){
			tatalAmount = parseFloat(tatalAmount) + parseFloat(value.amount);
		});
		$scope.tatalAmount = tatalAmount;
	}
	$scope.calculateDueAmount = function(){
		// calculate due amount
		var collectedAmount = parseFloat(0).toFixed();
		angular.forEach($scope.listPaymentDetails,function(value,key){
			collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
		});
		$scope.dueAmount = $scope.deposit.depositAmount != '' && typeof($scope.deposit.depositAmount) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
		$scope.paymentDetails.cashAmount =$scope.dueAmount;
		$scope.paymentDetails.chequeAmount = $scope.dueAmount;
		$scope.paymentDetails.debAmount = $scope.dueAmount;
		$scope.paymentDetails.credAmount = $scope.dueAmount;
		$scope.eTransferObj.transferAmount = $scope.dueAmount;
		$scope.paymentDetails.loyaltyCardAmt = $scope.dueAmount;
		$scope.razorAmount = $scope.dueAmount;
		$scope.mpesaAmount = "";
	}
	
	// remove payment details
	$scope.removePaymentDetails = function(index){
		
		var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
		if(tempObj!=null && (tempObj.cardNo == $scope.paymentDetails.loyaltyCardNo))
			{
			 $scope.paymentDetails.cardBalAmount = $scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
			 $scope.send=false;
			 $scope.resend=false;
			 $scope.validatTextFieldFlag = true;
			}
		$scope.listPaymentDetails.splice(index,1);
		$scope.calculateDueAmount();
		$scope.calculateTotalAmount();
	}
	
	$scope.parseFloatNum = function(number){
		return (number != '' && typeof(number) != 'undefined' && !isNaN(number)) ? parseFloat(number).toFixed(2) : parseFloat(0).toFixed(2);
	}
	
	
	$scope.uploadPatientImage = function(inputFile,depositId)
    {
    	    var file = inputFile;
	        var fd = new FormData();
	        fd.append('file', file );
			var URI1 = BASE_URL + ROOT_URL + "/api/billing/unit/signature/image/"+$scope.organizationId+"/"+$scope.unitId+"/"+depositId;
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
	
	
	
	$scope.uploadEncounterDoc = function(Obj)
	{
		 var fd = new FormData();
	        fd.append('organizationId', Obj.orgId);
	        fd.append('billId',Obj.depositMasterId );
	        fd.append('isDeposite',true);
	        fd.append('isPharmacy',false);
	        fd.append('isMiscellaneous',false);
	        fd.append('unitId', Obj.unitId);
	        fd.append('documentName',$scope.file.name);
	        fd.append('file',$scope.file );
	        var URI1 = BASE_URL + ROOT_URL + "/billing/PanCard/uploadDoc";
	        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
			{	
	        	$scope.file = null;
	        	angular.element("input[type='file']").val(null);
				return response;
				
			});
	}
	
	$scope.saveDeposit = function(){
		
		$scope.btnDisabled = true;
		if($scope.dueAmount > 0){
			growl.error("Please clear the due amount",{
				title : ERROR_MSG
			});
			return;
			$scope.btnDisabled = false;
		}	
		var collectedAmount = parseFloat(0).toFixed();
		angular.forEach($scope.listPaymentDetails,function(value,key){
			if(value.paymentModeId == 1){
				collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
			}
			
		});
		
		if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation){
			$('#uploadpanCardDetails').modal('show');
		}else{
			$scope.addDeposit();
		}
	}
	
	$scope.saveBtnDisabledDoc = true;

	$scope.addDeposit = function(){
		
		$scope.btnDisabled = true;
		$scope.saveBtnDisabledDoc = false;
		if($scope.dueAmount > 0){
			growl.error("Please clear the due amount",{
				title : ERROR_MSG
			});
			return;
			$scope.btnDisabled = false;
			$scope.saveBtnDisabledDoc = true;
		}	
		
		// Special check handling for MPesa if not valid trx then it will retrun
		// from here
// if($scope.transactionId!=null)
// {
// var data = {
// organizationId : $scope.organizationId,
// unitId : $scope.unitId,
// updatedBy : $scope.userId,
// updatedDate :moment(new Date()).format("YYYY-MM-DD HH:mm:ss"),
// mpesaTxcode : $scope.transactionId,
// mpesaAmount:$scope.mpesaAmount
// };
// var URI = BASE_URL + ROOT_URL + "/Billing/updateByNumber";
// var hasError = false;
// BillingGenericService.serviceAction(METHOD_POST,URI,data).then(function(response)
// {
// if(response.data.status == "error"){
// growl.error(response.data.message,{
// title : ERROR_MSG
// });
// $rootScope.stopLoader();
// hasError = true;
// }
// });
// if(hasError == true)
// return;
// }
		$('#uploadpanCardDetails').modal('hide');
		$('#collectPaymentModal').modal('hide');
		$rootScope.startLoader();
		$scope.deposit.createdBy = $scope.userId;
		$scope.deposit.orgId = $scope.organizationId;
		$scope.deposit.unitId = $scope.unitId; 
		$scope.deposit.signetureImage = $scope.binaryValueDataForSing,
		$scope.deposit.counterId = $rootScope.counterId; 
		if($scope.deposit.depositTypeId == 1)
			$scope.deposit.payeeId = 1;
		var param = {depositMasterDto:$scope.deposit,listPaymentDepositDetailsDto:$scope.listPaymentDetails};
		console.log("PARAM",param);
		
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit";
		BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
		if (response.data.status == "success"){
			$scope.btnDisabled = false;
			$scope.saveBtnDisabledDoc = true;		
				if($scope.file != null){
					$scope.uploadEncounterDoc(response.data.resObject);
				}
				
				$scope.initPaymentDetailsObj();
				$scope.listPaymentDetails = [];
				$scope.initDepositObj();
				$scope.patientDetailsList = [];
				$scope.deposit = {};
				$scope.deposit.patientId = $scope.patientDetails.patientId;
				$scope.patientDetails = {};
				$scope.patientDetails.patientId = $scope.deposit.patientId ;
				$scope.visitNoList = [];
				$scope.validatTextFieldFlag = true;
				$scope.skipMpesaValidation = false;
				$scope.deposit.depositTypeId = 1;
				$scope.deposit = {};
				$scope.patientList = [];
				$scope.searchDeposit();
				$rootScope.stopLoader();
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
				
			} else if(response.data.status == "error")
				{
					$scope.btnDisabled = false;
				  $rootScope.stopLoader();
				}
			
		});				
	}
		
	
	$scope.checkDate= function()
	{
		if($scope.patientSearchObj.birthDate == undefined || $scope.patientSearchObj.birthDate == null || $scope.patientSearchObj.birthDate == ''){
			   growl.warning("Date is invalid",{
					title : "warning"
				});
				$scope.patientSearchObj.birthDate = new Date();
			
			   return false;
		}else{
		if($scope.patientSearchObj.birthDate > new Date())
			{
						   growl.warning("DOB should not be greater than today's date",{
								title : "warning"
							});
							$scope.patientSearchObj.birthDate = new Date();
						
						   return false;
			}
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
	
	// search deposit
	$scope.searchDeposit = function()
	{
		$rootScope.startLoader();
		$scope.depositSearch.fromDate = ($scope.fromDate!=undefined && $scope.fromDate!="")?moment($scope.fromDate).format("YYYY-MM-DD"):moment(moment().toDate()).format('YYYY-MM-DD');
		$scope.depositSearch.toDate  =($scope.toDate!=undefined && $scope.toDate!="")?moment($scope.toDate).format("YYYY-MM-DD"):moment(moment().toDate()).format('YYYY-MM-DD');
		$scope.depositSearch.orgId = $scope.organizationId;
		$scope.depositSearch.unitId = $scope.unitId;
		$scope.depositSearch.noOfRecordsPerPage = $scope.noOfRecordsPerPage;
		$scope.depositSearch.offset = $scope.offset;
		$scope.depositSearch.fromDeposit = true;
		var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/search";
		BillingGenericService.serviceAction(METHOD_POST,URI,$scope.depositSearch,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.initDepositSearchDtls();
				console.log("$scope.depositSearchList  \n",response.data.listObject);
				$scope.commonListCount = response.data.object;
				$scope.depositSearchList = response.data.listObject ;
				$scope.depositSearchList1 = $scope.depositSearchList;
				angular.forEach($scope.depositSearchList,function(value,key){
				    value.cancellationBtn=false;
					$scope.depositSearchDtls.totalDepositAmount = parseFloat($scope.depositSearchDtls.totalDepositAmount) + parseFloat(value.depositAmount);
					$scope.depositSearchDtls.totalConsumedDepositAmount = parseFloat($scope.depositSearchDtls.totalConsumedDepositAmount) + parseFloat(value.consumedDeposit);
					$scope.depositSearchDtls.totalBalanceAmount = parseFloat($scope.depositSearchDtls.totalBalanceAmount) + parseFloat(value.availableDeposit);
					$scope.depositSearchDtls.totalRefundAmount = parseFloat($scope.depositSearchDtls.totalRefundAmount) + parseFloat(value.refundAmount);
					if(value.status !== 'A' || ((parseInt(value.availableDeposit) != parseInt(value.depositAmount)) && parseInt(value.consumedDeposit) != 0)){
                        value.cancellationBtn=true;
					}
					/*if(value.availableDeposit != undefined && value.availableDeposit !=null &&  value.availableDeposit == 0 && value.consumedDeposit>0){
						value.consumedDeposit = value.consumedDeposit - value.refundAmount;
					}*/
                   if($scope.flageForDepositSearchList1){
					        $scope.setPage(1,false);
					}
          
					$rootScope.stopLoader();
				});
			}
			else if (response.data.status == "error"){
				$rootScope.stopLoader();
			}
			
		});
	}
	$scope.searchDepositForPageSize = function(tempInt){
		$scope.noOfRecordsPerPage = tempInt != null ? tempInt : 10;
		$scope.offset = 0;
		$scope.flageForDepositSearchList1 = true;
		$scope.searchDeposit();
	}
	$scope.searchDepositForPageIndex = function(tempInt){
		$scope.offset=tempInt;
		$scope.flageForDepositSearchList1 = false;
		$scope.searchDeposit();
	}
	$scope.pager = {};
    $scope.page;
    $scope.setPage = function(page, flag)
    {
        $scope.pager.totalPages = undefined;
        if (page < 1 || page > $scope.pager.totalPages)
        {
            return;
        }
        
        $scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
        
        if (flag) {
        	if($scope.depositSearchAll == undefined || $scope.depositSearchAll == null || $scope.depositSearchAll == ""){
             $scope.searchDepositForPageIndex($scope.pager.startIndex);
        	}else{
        		$scope.searchOverAllInDeposit();
        	}
        }
    }
	
	$scope.searchDeposit();

	
	
	$scope.otReservationList = [];
	$scope.getReservationDetailsByPatientId = function(patinetId){
		$scope.deposit.encounterId = '';
	
		var param = {patientId:patinetId,unitId:$scope.unitId};
		
		var URI = BASE_URL + ROOT_URL + "/api/otims/OTReservation/getOTReservationListByPatientId/forDeposite";
		BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			
			$scope.otReservationList = [];
			if (response.data.status == "success")
			{
				$scope.otReservationList  = response.data.listObject;
			}
			//console.log("$scope.otReservationList".$scope.otReservationList);
		});
	}
	
	$scope.changeVisit = function(depositTypeId){
		
		if(depositTypeId == 1){
			$scope.deposit.visitTypeId = '';
			$scope.deposit.encounterId = '';
		}
		if(depositTypeId == 5){
			if($scope.deposit.patientId!=undefined && $scope.deposit.patientId!=null){
			$scope.getReservationDetailsByPatientId($scope.deposit.patientId);
			}
		}
	}
	
	// get encounters for deposit
	$scope.getEncounters = function(){
		 $scope.deposit.encounterId = '';
		if($scope.deposit.visitTypeId != null && $scope.deposit.visitTypeId != '' && typeof($scope.deposit.visitTypeId) != "undefined" && $scope.deposit.patientId != '' && $scope.deposit.patientId != null && typeof($scope.deposit.patientId) != 'undefined'){
		var param = {patientId:$scope.deposit.patientId,orgId:$scope.organizationId,unitId:$scope.unitId,visitTypeId:$scope.deposit.visitTypeId};
		var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/encounters";
		BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			
			$scope.visitNoList = [];
			if (response.data.status == "success")
			{
				$scope.visitNoList = response.data.listObject;
				
				if($scope.visitNoList !=null && $scope.visitNoList.length ==1){
					
					$scope.deposit.encounterId = $scope.visitNoList[0].encounterId;
					
				}else{
					
					$scope.deposit.encounterId = '';
				}
			}
		});
		}
	}
	/** *************Deposit Slip Report**************Chetana******** */
	$scope.rePrintDepositSlip=function(depositObj)
	{
		
			var data =  {
		            "appName": "HIS",
		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		            "reportDesignFilePath": "DepositSlip_listview.rpt",
		            "reportFormat": "PDF",
		            "exportFileName": "DepositSlip",
		            "reportParams": [
		              {
			             "paramName": "@Deposit_Master_ID",
			             "paramType": "Integer",
			             "paramValue": depositObj.depositMasterId
		              }
		            ]
		 };
		
		
	reportService.generate(REPORT_API_URI1,data);
	
}


	/** ***********Deposit Slip Report END************************ */
	
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
	           				patSearchObj = {};
	           				$scope.initPaymentDetailsObj();
	           				$scope.listPaymentDetails = [];
	           				$scope.initDepositObj();
	           				$scope.patientDetailsList = [];
	           				$scope.patientDetails = {};
	           				
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
	/*
	 * *************************************Mpesa Integration
	 * start******************************************
	 */
	
	$scope.closeDigitaslSig = function()
	{
		 angular.element('#digitalSignaturePopup').modal('hide');
	}
	
	
	$scope.checkmpesaDetails=function(){ 
		$rootScope.startLoaderPopup();
		$scope.skipMpesaValidation = true;
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
				$scope.mpesaAmount = '';
				$rootScope.stopLoaderPopup();
				$scope.skipMpesaValidation = false;
			}
			$rootScope.stopLoaderPopup();
		});
		}else{
			
			$scope.skipMpesaValidation = false;
			$rootScope.stopLoaderPopup();
			
			growl.error("Enter both transaction id and mobile number",{
				title : ERROR_MSG
			});
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
					uhidNumber: $scope.patientDetails.uhidNumber,
					phoneNumber:parseInt($scope.countryCallingCode+""+$scope.mobileNumber),
					partyA:$scope.mobileNumber,
					amount:$scope.validateMpesaAmount
			};
//			data.phoneNumber=parseInt($scope.countryCallingCode+""+$scope.mobileNumber);
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
					
				});
			}else{
				
				$scope.skipMpesaValidation = false;
				$rootScope.stopLoaderPopup();
				growl.error("Please enter mobile code and mobile number. ",{
					title : ERROR_MSG
				});
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
		$rootScope.startLoaderPopup();
		var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
		BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$rootScope.stopLoaderPopup();
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
				$rootScope.stopLoaderPopup();
				}
			else if(response.data.status == "error"){
				$rootScope.stopLoaderPopup();
				growl.error(response.data.message != null ? response.data.message :"Please wait",{
					title : ERROR_MSG
				});
			
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
	
	$scope.getSignBinaryData = function()
	{
		// $("#id_description_iframe").contents().find("body").html()
		
		 var binaryValueData = $('iframe[name=Demo]').contents().find('#finalDataBinary').val();
		 $scope.binaryValueDataForSing = binaryValueData;
		 angular.element('#digitalSignaturePopup').modal('hide');
		$scope.binaryValueDataForSing = "data:image/jpeg;base64,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inaBSh0qtkmaVTyBeGyjsKqmxjcTyz8MkXt+lxDpVBbskLVRZWLwHZCNQMQfgxwuS2osMyj2cT8qNVfx/pLuhnFnVY57nh7HNH+ggQj9dF8SopTm3UPUE8fA606XOcAks4rhIeZE5AcpQzQNIGDrYaFnh9hrvDTEQEcg07tbYuBjlTM3w45rlf3S3wmxT1qJOtiIwXqf6DfNf+nHmv+tSwRAIe5j+7ztILe2uNcE0GFICP4i1Y3JE8Vf+CwIrwdBZwSM7KsmpA4T7zB78AQO8k7TlYvVO+84K3f6Nj8jxLtA7AYN9G0yIaeCu7pr/OQ9EzkaZnwvDO2K3J5CGKwtJejTIL9PBHqXGBCaPWK2Ofjz3CZZS1Fz7/gOkKWAcSfqn2h8ouvJbGCSy4qdanxWONsueC3fFNoVm8HEW4+ODAIvEgMPq43qZRtDuC1Jt9rynuP1Q18NVfdnClGHnbQ3ZviISG76B9N8g8YppH6QBkYaHnWBmUxgvPoGjW1F3BPuT+OOxzuCxwSj/vwQfal1wy8PPJFPDJHeyDOunn1bTPCsACajcfMQA8tQblcGcgb3l4Z+IyQ/Dh1taD4yhcFjhU1Dkz4FavqAocHgsJpkcveQEMaI/WFWnbTS+1qIlSjGi/1k4yOuEPeA7xj3hfvz9yG2ntgWeUuIGgL3DjFhojCNUfHjwTtMeiFbIEyWD/Ui8izP2URHNgg5sBQVoeoYEYdEIrI6aMTAwj4fKzAa/38ghIv8RiE5jIeHWHUNGYnPbJ/V21zdsKxMWGkwMLIX/LjQH4XIy2bVzvtEz+vGwGPcGxd+9Xm3ID4796AJnCMglMI43CueNZNgDHuzt8793bph5x7QyOdFISaMTIqG+J6RjUDGgYhsAAwprjQGWwbfPoNIM1jjTiXobStbBQR0EdiFISIIkkA+BmBWnnh/uB9MeCjOgkueieTWDTsemka8M+2UNyZEbGcQ2Mg7yGSJrBBc8nh45hRA13htuLYHhrhuvDV4IHjWYz1nVtZ4iUREfsCtQgxGBKQwUJLzLqeBsWEbp9lvJ5q72WtnoGc7hgnS1o16lzDweGcw8gjvBveL+4a3A0NJ+1D2bKnHjts4C48b7nxiGxBeADx0eASIgcHQ4mY+l8eEyR0lh+fUPEdERAqaFTtGnVU7Rp1ULYx6s8+OQW8MioZ9/uIZMbFgUvagkIiIbAT2fttGnb1hGr9Qh4B66MRwNCtR9kWnCBLckjDA3GOaB7FtRDOfucZjiIjIzCGGoNlfJS0Lo9LsrVPcBrcwLmT21jE+WzbsTG5wiVNkiZRAioQYtCUiIrOCwCniFDDuOeWNHHfS3gieIyoety5tP9upb+c28qx22SLgmgheY9VL5T1a2d41ZItSERGRgBRL2o1SROfGIVb0iKA6SuoSOU/WQFv0gmfFf1HoqSH2dXHtM2Hgd0m/ZAJBDr6FWkREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREZBVccMH/B+nxFdKODKtWAAAAAElFTkSuQmCC";
		$scope.file =$scope.convertBase64ToImage($scope.binaryValueDataForSing,moment().toDate().getYear()+".jpeg");
		
	}
	
	
    $scope.convertBase64ToImage = function (dataurl, filename) {
	    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
	        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	    while(n--){
	        u8arr[n] = bstr.charCodeAt(n);
	    }
	    return new File([u8arr], filename, {type:mime});
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
//				requestedUniqueId :$scope.requestedUniqueId
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
//						$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
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
	}else{// alert($location.host());
		// var IAddress = $location.host();
		// alert("IP ADRRESS");
		// alert(IAddress);
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
							// "amount":100,
							// tellerId: "50002",
							tranKey: $scope.uniqueNumber
					}
					
				/*
				 * $http.post('http://localhost:9991/api/sale',dataForPinPad)
				 * .then(function(response) { alert(); });
				 */
					
					/*
					 * return $http({ headers: { "Accept":
					 * "application/json;odata=verbose", "content-type":
					 * "application/json;odata=verbose" }, method: "POST", url:
					 * "http://localhost:9991/api/sale", data: { __metadata: {
					 * "type": "SP.Data.SongsListItem" }, "amount": "100",
					 * "tellerId": "50002", "tranKey": "201903282" }
					 * 
					 * 
					 * }).then(saveContact) .catch(function(message) {
					 * console.log("addQuestion() error:" + message); });
					 * 
					 * function saveContact(data, status, headers, config) {
					 * alert("Item added successfully"); }
					 */
					
					/*
					 * var URI = "http://localhost:9991/api/sale"; var
					 * headerObjDia = { 'Content-Type':'application/json', };
					 * var data={};
					 * 
					 * GenericServiceParamHeader .serviceAction(METHOD_POST,
					 * URI, dataForPinPad,
					 * NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia) .then(
					 * function(response) { console.log(response); alert(); });
					 */
             
             
// /$scope.ipAddress+
					var urlprotocol = $location.protocol();
					// var finalUrl =
					// "http://"+$scope.ipAddress+":9991/api/sale";
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
	
	/*
	 * *************************************Mpesa Integration
	 * END******************************************
	 */
	
	/**
	 * *********************************************Loylty card Tab START
	 * ****************************************
	 */
	
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
		$scope.resend = (flag=='N')?true:false;
		$scope.send =false;
	}
	
	$scope.clearLoylty = function(){
		
		$scope.send = false;
		$scope.validatTextFieldFlag = true;
		$scope.paymentDetails.otp ="";
		$scope.calculateDueAmount();
		$scope.calculateTotalAmount();
		loyaltiCardCallFactory.getLoyaltyCardList($scope.patientIdForLoyalty,$scope);
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
	
	$scope.searchOverAllInDeposit = function(){		
		if($scope.depositSearchList1.length > 0 && $scope.depositSearchAll != '' && $scope.depositSearchAll != undefined){
			$scope.depositSearchList = [];
			angular.forEach($scope.depositSearchList1,function(val){
			    val.cancellationBtn = false;
				if((val.depositNo.toLowerCase()).includes($scope.depositSearchAll.toLowerCase()) 
						|| (val.depositDate.toLowerCase()).includes($scope.depositSearchAll.toLowerCase()) 
						|| (val.patientName.toLowerCase()).includes($scope.depositSearchAll.toLowerCase())
						|| (val.uhidNumber.toLowerCase()).includes($scope.depositSearchAll.toLowerCase())
						
				){
                    if(val.status !== 'A' || ((parseInt(val.availableDeposit) != parseInt(val.depositAmount)) && parseInt(val.consumedDeposit) != 0)){
                        val.cancellationBtn=true;
                    }
					$scope.depositSearchList.push(val);
				}
			});
		}
	}
	
	$scope.cancelPaymentReqModal = function(){
		angular.element('#paymentReqModal').modal('hide');
	}
	
	$scope.clearMpesaFields = function(){
		$scope.skipMpesaValidation = false;
		$scope.mobileNo = null;
		$scope.mpesaAmount = '';
		$scope.transactionId = null;
	}
	
	$scope.clearCardFields =  function(){
		$scope.pinpadAmounFlag = false;
		$scope.flagForSale=false;
		 $scope.flagForSMSpay=false;
		 $scope.flagForBharatQr=false;
		 $scope.objPosMachine.deviceName = "";
		 $scope.objPosMachine.isSale = false;
		 $scope.objPosMachine.isBharatQr = false;
		 $scope.objPosMachine.isSMSpay = false;
		$scope.paymentDetails = angular.copy({paymentModeId:$scope.paymentDetails.paymentModeId,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
		$scope.calculateDueAmount();
		$scope.calculateTotalAmount();
	}
	
	
	/**
	 * *********************************************Loylty card Tab END
	 * ****************************************
	 */
	
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
						$scope.calculateDueAmount();
						$scope.calculateTotalAmount();
						
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
	
	$scope.getPandCard = function(billId){

		var URI = BASE_URL + ROOT_URL + "/billing/PanCard/url/"+billId+"/"+1; //1 for Deposit screen
		BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if (response.data.status == "success")
			{
				if(response.data.object != null ){
					var imageUrl = response.data.object.docUrl;
					$window.open(imageUrl, '_blank');
				}else{
					growl.warning("pancard is not uploaded",
							{
								title : 'Warning!'
							});
				}
				
			} 

		});
	
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
						$scope.calculateDueAmount();
						$scope.calculateTotalAmount();
						
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

    $scope.validateDepositAccess = function(){
        var URI = BASE_URL + ROOT_URL + "/api/billing/unit/validateDepositAccess/"+$scope.userId+"/"+$scope.unitId+"/"+$scope.organizationId;
        BillingGenericService.serviceAction("GET", URI, {}).then(
        function (response) {
             if(response.data.status == 'success'){
                $scope.depositAccess = response.data.object.depositAccess;
                $rootScope.stopLoader();
             }
             else{
                    console.log(response.data.message)
                    $rootScope.stopLoader();
             }
        });

    }
    $scope.validateDepositAccess();

    $scope.reasonSelect = "";
    $scope.cancellationDeposit={};

    $scope.openCancellationPopup = function(deposit){
        $scope.reasonSelect = "";
        $scope.reasonNote = "";
        $scope.cancellationDeposit = deposit;
        $('#CancellationModalForDepositReceipt').modal('show');
    }

	$scope.cancelDepositReceipt = function(){

	    $('#CancellationModalForDepositReceipt').modal('hide');

        if($scope.ValidateRejection() && $scope.validateCancellation()){

            var data = {
                    "depositMasterId": $scope.cancellationDeposit.depositMasterId,
                    "orgId" : $scope.organizationId,
                    "unitId" : $scope.unitId,
                    "cancelReasonId" : parseInt($scope.reasonSelect),
                    "cancelReasonNote" : $scope.reasonNote,
                    "updatedBy" : $scope.userId
            }

            $rootScope.stopLoader();
            var URI = BASE_URL + ROOT_URL + "/api/billing/unit/cancelDepositReceipt";
            BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                if (response.data.status == "success")
                {
                    $scope.reasonSelect = "";
                    $scope.reasonNote = "";
                    $scope.cancellationDeposit = {};
                    growl.success("Deposit Receipt Cancelled Successfully..!",
                    {
                        title : 'Success!'
                    });

                    $scope.searchDeposit();
                    $rootScope.stopLoader();
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

     $scope.getClassForColor = function(info)
     {
           if(info.status == 'I'){
            return 'td-bg-red';
           }
     }

    $scope.ValidateRejection = function () {
        var isValid = true;
        if (!$scope.reasonSelect) {
          isValid = false;
          growl.error('Please Select Reason to Cancel');
          return false;
        }
        return isValid;
    }

    $scope.validateCancellation = function(){
          var validate = true;
          var obj = $scope.cancellationDeposit;
         if((parseInt(obj.availableDeposit) == parseInt(obj.depositAmount)) && parseInt(obj.consumedDeposit) == 0){
            return validate;
         }
         else{
                validate = false;
                growl.error('This Deposit Receipt cannot be cancelled.!');
                return false;
         }
    }

    $scope.checkMaxInoutAmount = function(){
        var depositAmount = $scope.deposit.depositAmount;
        if(depositAmount !== undefined && depositAmount !== null && depositAmount !== ''){
            if($scope.maxInputAmount !== undefined && $scope.maxInputAmount !== null && $scope.maxInputAmount !== ''){
                if(depositAmount > $scope.maxInputAmount){
                    $scope.deposit.depositAmount = '';
                    growl.warning
                    ("You can enter max amount up to " + $scope.maxInputAmount + "...!",
                        {title : "WARNING!"}
                    );
                }
            }
        }
    }
		
	}catch(e){
		console.log(e.message);
	}
}]);
