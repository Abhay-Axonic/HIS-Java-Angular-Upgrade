

angular.module("myApp").controller("creditNoteRefundController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','reportService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,reportService){
	try{
	
	$rootScope.loginpage = true;
	$scope.dateOptionsTrancs = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
		};
	
	$scope.dateOptions = {
			formatYear : 'yyyy',
			showWeeks : false
		};
	$scope.chequedDateOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : moment(new Date()).subtract(90, 'days').toDate(),
		};
	
	$scope.CardDateOptions = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate:new Date()
		};
	$scope.openDatePicker = function() {
		$scope.datepicker.opened = true;
	};

	$scope.datepicker = {
		opened : false
	};
	
	$scope.openDatePickerPatiSearch = function() {
		$scope.datepicker1.opened = true;
	};

	$scope.datepicker1 = {
			opened : false
		};
	
	
	
	$scope.openFromDate = function() {
		$scope.fromDatePicker.opened = true;
	};

	$scope.fromDatePicker = {
		opened : false
	};
	
	$scope.openToDate = function() {
		$scope.toDatePicker.opened = true;
	};

	$scope.toDatePicker = {
			opened : false
		};
	
	
	$scope.fromDate =moment().toDate();
	$scope.toDate = moment().toDate();
	
	$scope.searchKeyword="";
	$scope.searchKeywordForCr = "";
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
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
	/**================================COUNTER CODE END======================================*/

	
	$scope.refundCNSearchObj = 
	    {
			  "fromDate" 	: "",
			  "toDate" 		: "",
			  "patientName" : "",
			  "uhidNumber"  : "",
			  "visitTypeId" : "",
			  "depositTypeId" : "",
			  "orgId" 		: $scope.organizationId,
			  "unitId" 		: $scope.unitId,
			  "refundCnNo"  : "",
			  "refundCrNo"  : "",
			  "paymentRefundVoucherNo" :""	 
				  
		}
	
	
	
	
	//For Get Active Gender List
	$scope.getSexMasterList = function() {
		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.sexMasterList = response.data.listObject;
		});
	}
	
/*	$scope.getIdentificationTypeList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
				$scope.identificationTypeList = response.data.listObject;
		});
	}*/
	
	$scope.getBankList = function(){
		var URI = BASE_URL + ROOT_URL + "/api/billing/bankMaster";
		var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
		BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
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
		$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:"",loyaltyCardAmt:""});
	}
	
	
	$scope.initPaymentRefundObj = function()
	{
		$scope.paymentRefundObj =angular.copy({paymentRefundVoucherId : '',
			  paymentRefundVoucherNo : '',
			  dateOfRefund : '',
			  isCnOrDeposit : '',
			  tRefundCnId : '',
			  creditNoteId:'',
			  tPharmacyReturnId:'',
			  amountRefunded : 0.0,
			  patientId : '',
			  receivedFrom : '',
			  identificationNo : '',
			  nameOfPerson : '',
			  status : 'A',
			  unitId : $scope.unitId,
			  orgnisationId : $scope.organizationId,
			  createdDate : '',
			  createdBy : $scope.userId,
			  updatedDate : '',
			  counterId :$rootScope.counterId,
			  cancelOrDiscount:""
	  })
	};
	
	

	
	
	$scope.init = function(){
		/*$scope.identificationTypeList = [];*/
		$scope.sexMasterList = [];
		$scope.getSexMasterList();
	/*	$scope.getIdentificationTypeList();*/
		$scope.initPaymentRefundObj();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {};
		$scope.billing = true;
		$scope.deposit = {};
		$scope.paymentDetails = {};
		$scope.initPaymentDetailsObj();
		$scope.listPaymentDetails = [];
		$scope.getBankList();
		$scope.getHospitalBankList();//ADDED LATER GANESH
		$scope.getCardTypesList();
		$scope.dueAmount = parseFloat(0).toFixed(2);
		$scope.depositSearch = {};
		$scope.visitNoList = [];
	}
	
	
	
	$scope.init();
	$scope.setPaymentModeId = function(id)
	{
		$scope.paymentDetails.paymentModeId = id;
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
	
	$scope.cashTotalAmount = 0;
	$scope.calculateDueAmount = function(){
		//calculate due amount
		var collectedAmount = parseFloat(0).toFixed();
		angular.forEach($scope.listPaymentDetails,function(value,key){
			collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
		});
		/*$scope.dueAmount = $scope.paymentRefundObj.amountRefunded != '' && typeof($scope.paymentRefundObj.amountRefunded) != 'undefined' ? parseFloat(parseFloat($scope.paymentRefundObj.amountRefunded) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
	*/	
		$scope.dueAmount = $scope.cashTotalAmount - parseFloat(collectedAmount);
		$scope.paymentRefundObj.amountRefunded = collectedAmount;
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
	//	$scope.deposit.depositAmount  = $scope.tatalAmount;
	}
	
	//remove payment details
	$scope.removePaymentDetails = function(index){
		$scope.listPaymentDetails.splice(index,1);
		$scope.calculateDueAmount();
	}
	
	$scope.parseFloatNum = function(number){
		return (number != '' && typeof(number) != 'undefined' && !isNaN(number)) ? parseFloat(number).toFixed(2) : parseFloat(0).toFixed(2);
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
	
	//search deposit
	$scope.depositTypeId="";
	$scope.getDeposit = function(depositTypeId)
	{
		$scope.depositTypeId = depositTypeId;
		if($scope.patientDetails.patientId==null && ($scope.depositTypeId==""||$scope.depositTypeId==undefined))
			{
			growl.error("Please Select Deposit Type.",{
				title : ERROR_MSG
			});
			return;
			}
		else{
			var data = {};
			var patientId =$scope.patientDetails.patientId;
				var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/refund/"+$scope.organizationId+"/"+$scope.unitId+"/"+patientId+"/"+depositTypeId;
				console.log("URI",URI);
				BillingGenericService.serviceAction(METHOD_GET,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.depositList = response.data.listObject;
						$scope.totalDepositAmount = 0.00;
						$scope.totalConsumetedAmount = 0.00;
						$scope.totalRefundAmount = 0.00;
						$scope.totalBalanceAmount = 0.00;
						
						angular.forEach($scope.depositList,function(value,key){
							$scope.totalDepositAmount = parseFloat($scope.totalDepositAmount) + parseFloat(value.depositAmount);
							$scope.totalConsumetedAmount = parseFloat($scope.totalConsumetedAmount) + parseFloat(value.consumedDeposit);
							$scope.totalRefundAmount = parseFloat($scope.totalRefundAmount) + parseFloat(value.refundAmount);
							$scope.totalBalanceAmount = parseFloat($scope.totalBalanceAmount) + parseFloat(value.availableDeposit);
						});
					}
					$rootScope.stopLoader();
				});
		}
		
	}
	
  $scope.cnMasterObj = {};
  $scope.showColletPaymentPopup = function(cnMasterObj)
  {
	  $scope.cnMasterObj = cnMasterObj;
	  $scope.cnMasterObj.crDueAmount =  parseFloat($scope.cnMasterObj.crDueAmount).toFixed(2);
	  $scope.paymentRefundObj.amountRefunded = parseFloat($scope.cnMasterObj.crDueAmount).toFixed(2);
	  $scope.paymentDetails.cashAmount = parseFloat($scope.cnMasterObj.crDueAmount).toFixed(2);
	  $scope.paymentDetails.chequeAmount = $scope.paymentDetails.cashAmount;
	  $scope.paymentDetails.debAmount = $scope.paymentDetails.cashAmount;
	  $scope.paymentDetails.credAmount = $scope.paymentDetails.cashAmount;
	  $scope.cashTotalAmount = parseFloat($scope.cnMasterObj.crDueAmount).toFixed(2);
	  $scope.paymentRefundObj.creditNoteId = $scope.cnMasterObj.creditNoteId;
	  $scope.paymentRefundObj.patientId = $scope.cnMasterObj.patientId;
	  $scope.paymentRefundObj.cancelOrDiscount = $scope.cnMasterObj.cancelOrDiscount;
	  $scope.listPaymentDetails = [];
	  $('#creditNote_collectRefundModal').modal('show');  
	  $scope.calculateTotalAmount();
	  $scope.calculateDueAmount();
	  
  }
  

		
	
	
	
$scope.addPaymentDetails = function()
{
		//check amount collected is greater tha deposit amount
		var refundAmount = $scope.cnMasterObj.crDueAmount != "" && typeof($scope.cnMasterObj.crDueAmount) != "undefined" ? parseFloat($scope.cnMasterObj.crDueAmount) : parseFloat(0) ;
		//var refundAmount = $scope.cnMasterObj.selfPayable != "" && typeof($scope.cnMasterObj.selfPayable) != "undefined" ? parseFloat($scope.cnMasterObj.selfPayable) : parseFloat(0) ;
		$scope.paymentRefundObj.amountRefunded = $scope.cnMasterObj.crDueAmount;
		var collectedAmount = 0;
		var tempAmount = 0;
		
		switch($scope.paymentDetails.paymentModeId)
		{
		case 1 :
			tempAmount = $scope.paymentDetails.cashAmount != '' && typeof($scope.paymentDetails.cashAmount) != 'undefined' ? parseFloat($scope.paymentDetails.cashAmount) : parseFloat(0);
			if(parseFloat($scope.paymentDetails.cashAmount) > parseFloat(refundAmount)){
				growl.warning("Entered amount should be equal to or less than Refund amount",{
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
			if(parseFloat($scope.paymentDetails.chequeAmount) > refundAmount){
				growl.warning("Entered amount should be equal to or less than Refund amount",{
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
			if(parseFloat($scope.paymentDetails.credAmount) > refundAmount){
				growl.warning("Entered amount should be equal to or less than Refund amount",{
					title : WARNING_MSG
				});
				return;
			}
			break;
		
		case 4 :
			tempAmount = $scope.paymentDetails.debAmount != '' && typeof($scope.paymentDetails.debAmount) != 'undefined' ? parseFloat($scope.paymentDetails.debAmount) : parseFloat(0);
			if(parseFloat($scope.paymentDetails.debAmount) > refundAmount){
				growl.warning("Entered amount should be equal to or less than Refund amount",{
					title : WARNING_MSG
				});
				return;
			}
			break;
		case 6 :
			
			tempAmount = $scope.paymentDetails.loyaltyCardAmt != '' && typeof($scope.paymentDetails.loyaltyCardAmt) != 'undefined' ? parseFloat($scope.paymentDetails.loyaltyCardAmt) : parseFloat(0);
			if(typeof($scope.paymentDetails.loyaltyCardAmt) == "undefined" || $scope.paymentDetails.loyaltyCardAmt == "" || $scope.paymentDetails.loyaltyCardNo == ""){
				growl.error("Please fill all details",{
					title : ERROR_MSG
				});
				return;
			}
				break;
				
		case 7:
			
			tempAmount = $scope.paymentDetails.loyaltyCardAmt != '' && typeof($scope.paymentDetails.loyaltyCardAmt) != 'undefined' ? parseFloat($scope.paymentDetails.loyaltyCardAmt) : parseFloat(0);
			if(typeof($scope.paymentDetails.loyaltyCardAmt) == "undefined" || $scope.paymentDetails.loyaltyCardAmt == ""){
				growl.error("Please insert an amount",{
					title : ERROR_MSG
				});
				return;
			}
				break;
				
		case 8:
			
			tempAmount = $scope.eTransferObj.transferAmount != '' && typeof($scope.eTransferObj.transferAmount) != 'undefined' ? parseFloat($scope.eTransferObj.transferAmount) : parseFloat(0);
			
			if(parseFloat($scope.eTransferObj.transferAmount) > refundAmount){
				growl.warning("Entered amount should be equal to or less than Refund amount",{
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
		}
		
		if(tempAmount == 0)
		{
			growl.warning("Amount cannot be zero",{
				title : WARNING_MSG
			});
			return;
		}
		
		angular.forEach($scope.listPaymentDetails,function(value,key)
		{
			collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
		});
		collectedAmount = collectedAmount + tempAmount;
		if(collectedAmount > refundAmount)
		{
			growl.warning("Entered amount should be equal to or less than Refund amount",{
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
			case 6:				
				var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.loyaltyCardAmt).toFixed(2),cardNo:$scope.paymentDetails.loyaltyCardNo,createdBy:$scope.userId};
				$scope.listPaymentDetails.push(paymentDetails);
				break;
				
			case 7:				
				var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.loyaltyCardAmt).toFixed(2),createdBy:$scope.userId};
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
		}
		$scope.initPaymentDetailsObj();
		$scope.setFirstTabLevel(1);
        $scope.setPaymentModeId(1);
		$scope.calculateDueAmount();
		$scope.calculateTotalAmount();
		$scope.firstName = "";
		$scope.patientLoyaltyCardFlag = true;
		
	}


$scope.searchCNMaster = function(){
	
	$rootScope.startLoader();
	$scope.refundCNSearchObj.fromDate = ($scope.fromDate!=""&&$scope.fromDate!=undefined)?moment($scope.fromDate).format('YYYY-MM-DD'):moment().format('YYYY-MM-DD');
	$scope.refundCNSearchObj.toDate =($scope.toDate!=""&&$scope.toDate!=undefined)?moment($scope.toDate).format('YYYY-MM-DD'):moment().format('YYYY-MM-DD');
	$scope.refundCNSearchObj.orgId = $scope.organizationId;
	$scope.refundCNSearchObj.unitId = $scope.unitId;
	
	console.log($scope.refundCNSearchObj.fromDate);
	console.log($scope.refundCNSearchObj.toDate);
	$scope.totalCnRefundAmount = 0.00;
	$scope.totalCnDueAmount = 0.00;
	var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote/refund";
	BillingGenericService.serviceAction(METHOD_POST,URI,$scope.refundCNSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		if (response.data.status == "success")
		{
			$scope.refundCrMasterList = response.data.listObject;
			angular.forEach($scope.refundCrMasterList,function(value,key){
				$scope.totalCnRefundAmount = parseFloat($scope.totalCnRefundAmount) + parseFloat(value.crAmount);
				$scope.totalCnDueAmount = parseFloat($scope.totalCnDueAmount) + parseFloat(value.crDueAmount);
			});
			$rootScope.stopLoader();
		}
		else if(response.data.status == "error")
			{
			$rootScope.stopLoader();
			}
		
	});
}
$scope.searchCNMaster();


$scope.checkOutstanding = function(refundCnObj){
	/*$rootScope.startLoader();
	var URI = BASE_URL + ROOT_URL + "/api/billing/patient/outstanding/"+refundCnObj.patientId;
	BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		if (response.data.status == "success")
		{
		   $scope.showColletPaymentPopup(refundCnObj);
		   $rootScope.stopLoader();
		}
		else if(response.data.status == "error")
			{
			  growl.warning(response.data.message,{title : WARNING_MSG});
			  $rootScope.stopLoader();
			}
	});*/
	$scope.showColletPaymentPopup(refundCnObj);
}

$scope.saveBtnDis = true;
$scope.addRefund = function()
{
	$scope.saveBtnDis = false;
	if($scope.paymentRefundObj.amountRefunded == 0){
		growl.error("Amount Should not be 0",{
			title : ERROR_MSG
		});
		$scope.saveBtnDis = true;
		return;
	}
	if(Number($scope.tatalAmount) == 0){
		growl.error("Amount Should not be 0..!!",{
			title : ERROR_MSG
		});
		$scope.saveBtnDis = true;
		return;
	}
	
	$('#creditNote_collectRefundModal').modal('hide');
	$rootScope.startLoader();
	$scope.paymentRefundObj.createdBy = $scope.userId;
	$scope.paymentRefundObj.orgnisationId = $scope.organizationId;
	$scope.paymentRefundObj.unitId = $scope.unitId; 
	$scope.paymentRefundObj.isCnOrDeposit = 1;
	$scope.paymentRefundObj.counterId = $rootScope.counterId;
	var billPayeeId = $scope.cnMasterObj.billPayeeId;
	var billMasterId = $scope.cnMasterObj.billMasterId; 
	var param = {paymentRefundMasterDto:$scope.paymentRefundObj,listPaymentRefundDetails:$scope.listPaymentDetails};
	console.log('param',JSON.stringify(param));
	
				
				var URI = BASE_URL + ROOT_URL + "/api/billing/refund/cr/"+billMasterId+"/"+billPayeeId;
				console.log("URI",URI);
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if (response.data.status == "success"){
						$scope.initPaymentDetailsObj();
						$scope.listPaymentDetails = [];
						$scope.patientDetailsList = [];
						$scope.patientDetails = {};
						$scope.searchCNMaster();
						$rootScope.stopLoader();
						$scope.saveBtnDis = true;
						//ForReport Print Refund
						var data =  {
				                 "appName": "HIS",
				                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				                 "reportDesignFilePath": "Refund_Receipt_CN.rpt",
				                 "reportFormat": "PDF",
				                 "exportFileName": "RefundReceipt",
				                 "reportParams": [
				                   {
				                  "paramName": "@payment_refund_id",
				                  "paramType": "Integer",
				                  "paramValue": response.data.object
				                }
				                 ]
				      };
						reportService.generate(REPORT_API_URI1,data);
					}
					else if(response.data.status == "error")
					{
					$rootScope.stopLoader();
					}
				});
}



/**===================================================================*/


//Start For Claim
$scope.billDetailsForClaim = function(bill){
	
	console.log("BILL",bill);
	$scope.claimBill = bill;
	$scope.billServicesListClaim=[];
	$scope.totalAmtQuaBase = 0;
	$scope.totalAmtConcession = 0;
	$scope.totalAmtDiscount = 0;
	$scope.totalAmtGst = 0;
	$scope.totalNetPayableAmt = 0;
	$scope.totalSelfPayable = 0;
	$scope.totalCreditPayable = 0;
	$scope.totalPackageConsumptionAmt = 0;
	
	$scope.billId = bill.billMasterId;
	
	
	
	var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+bill.billMasterId;
	BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		if (response.data.status == "success"){
			//$scope.payeeList = response.data.listObject;
			$scope.billServicesListClaim = response.data.object.listObject;
			//$scope.paymentReceiptsDetailsList=response.data.object.object;
			var totalAmtQuaBase = parseFloat(0);
			var totalAmtConcession = parseFloat(0);
			var totalAmtDiscount = parseFloat(0);
			var totalAmtGst = parseFloat(0);
			var totalNetPayableAmt = parseFloat(0);
			var totalSelfPayableAmtClaim = parseFloat(0);
			var totalCreditPayableAmtClaim = parseFloat(0);
			angular.forEach($scope.billServicesListClaim,function(value,key){
				
				totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
				totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
				totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
				totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
				totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
				totalSelfPayableAmtClaim = totalSelfPayableAmtClaim + parseFloat(value.selfPayable);
				totalCreditPayableAmtClaim = totalCreditPayableAmtClaim + parseFloat(value.creditPayable);
			
			});
			
			$scope.totalAmtQuaBase =totalAmtQuaBase;
			$scope.totalAmtConcession = totalAmtConcession;
			$scope.totalAmtDiscount =totalAmtDiscount;
			$scope.totalAmtGst = (totalAmtGst).toFixed(2);
			$scope.totalNetPayableAmt = totalNetPayableAmt;
			$scope.totalCreditPayableAmtClaim = totalCreditPayableAmtClaim;
			$scope.totalSelfPayableAmtClaim = totalSelfPayableAmtClaim;
		}
	});
			
		}


$scope.refundListSearch = function(){
	$rootScope.startLoader();
	$scope.refundCNSearchObj.fromDate = ($scope.fromDate!=""&&$scope.fromDate!=undefined)?moment($scope.fromDate).format('YYYY-MM-DD'):moment().format('YYYY-MM-DD');
	$scope.refundCNSearchObj.toDate =($scope.toDate!=""&&$scope.toDate!=undefined)?moment($scope.toDate).format('YYYY-MM-DD'):moment().format('YYYY-MM-DD');
	$scope.refundCNSearchObj.orgId = $scope.organizationId;
	$scope.refundCNSearchObj.unitId = $scope.unitId;
	console.log($scope.refundCNSearchObj.fromDate);
	console.log($scope.refundCNSearchObj.toDate);
	var URI = BASE_URL + ROOT_URL + "/api/billing/refund/cr/search";
	BillingGenericService.serviceAction(METHOD_POST,URI,$scope.refundCNSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		if (response.data.status == "success"){
			$scope.refundList = response.data.listObject;
		}
		$rootScope.stopLoader();
	});
}
$scope.refundListSearch();
	
/***************Credit Note Report**************Chetana*********/
$scope.rePrintCreditNote=function(cnMasterObj)
{
	
		var data =  {
	            "appName": "HIS",
	            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
	            "reportDesignFilePath": "CreditNotes.rpt",
	            "reportFormat": "PDF",
	            "exportFileName": "CreditNote",
	            "reportParams": [
	              {
		             "paramName": "@credit_note_id",
		             "paramType": "Integer",
		             "paramValue": cnMasterObj.creditNoteId
	              }
	            ]
	 };
	
	
reportService.generate(REPORT_API_URI1,data);

}



$scope.cancelCr = function(refundCnObj){
    $scope.creditNoteId = refundCnObj.creditNoteId;
    $('#cancelCreditNote').modal('show');
}

$scope.saveBtnDis = true;
$scope.cancelCreditNote = function(){
	$scope.saveBtnDis = false;
	$rootScope.startLoader();
	var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote/cancel/"+$scope.creditNoteId;
	BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
		if (response.data.status == "success")
		{
		 growl.success(response.data.message,{
        			title : SUCCESS_MSG
        		});
			$rootScope.stopLoader();
			$scope.saveBtnDis = true;
			$scope.searchCNMaster();
		}
		else if(response.data.status == "error")
			{
			growl.error(response.data.message,{
                    			title : ERROR_MSG
                    		});
			$rootScope.stopLoader();
			}
	});
}

$scope.reprintRefund = function(refundObj)
{
	var data =  {
            "appName": "HIS",
            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
            "reportDesignFilePath": "Refund_Receipt_CN.rpt",
            "reportFormat": "PDF",
            "exportFileName": "RefundReceipt",
            "reportParams": [
              {
             "paramName": "@payment_refund_id",
             "paramType": "Integer",
             "paramValue": refundObj.paymentRefundVoucherId
           }
            ]
 };
	reportService.generate(REPORT_API_URI1,data);
}
/*************Credit Note Report END*************************/
	$scope.getRedundDetails = function(paymentRefundVoucherId)
	{
		$rootScope.startLoader();
		$('#refundListCr_payment_mode_Modal').modal('show');
		var URI = BASE_URL + ROOT_URL + "/api/billing/refund/cr/details/"+$scope.organizationId+"/"+$scope.unitId+"/"+paymentRefundVoucherId;
		BillingGenericService.serviceAction(METHOD_GET,URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			$scope.refundDetailsList = [];
			console.log("response",response);
			if (response.data.status == "success")
				{
				 $scope.refundDetailsList = response.data.listObject;
				}
			$rootScope.stopLoader();	
		});
	}

	/*$scope.getBankDetails = function(tRefundCnId)
	{
		$('#bankDetailsModal_refundList').modal('show');
		var URI = BASE_URL + ROOT_URL + "/api/billing/refund/bankDetails/"+$scope.organizationId+"/"+$scope.unitId+"/"+tRefundCnId;
		BillingGenericService.serviceAction(METHOD_GET,URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			$scope.bankDetailsList = [];
			console.log("response",response);
			if (response.data.status == "success")
				{
				 $scope.bankDetailsList = response.data.listObject;
				}
		});
	}*/

	
	/*======================================================================** loyalty Card****========================================================================*/
	$scope.firstName = "";
	$scope.loyaltyCarNo="";
	$scope.patientLoyaltyCardFlag= true;
	$scope.getLoyaltyCardList = function(){
		$scope.startLoader();
		if($scope.paymentDetails.paymentModeId == 6){
			var data = {
					  "patientId" : $scope.paymentRefundObj.patientId,
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
					$scope.send = false;
					$scope.resend = false;
					$scope.stopLoader();
					if(response.data.listObject!=null && response.data.listObject.length>0){
						$scope.loyaltyCardObj = response.data.listObject[0];					
						$scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount;
						$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
						$scope.loyaltyCarNo = $scope.loyaltyCardObj.cardNo;
						$scope.firstName = $scope.loyaltyCardObj.firstName;
						$scope.patientLoyaltyCardFlag = false;
					}
					
				}
				$scope.stopLoader();
			});
		
		}else{
			$scope.paymentDetails.cardBalAmount = "";
			$scope.paymentDetails.loyaltyCardNo = "";
			$rootScope.stopLoader();
		}
	}
	$scope.getLoyaltyCardNumber = function(number){
		
		$scope.paymentDetails.paymentModeId = number;
		$scope.getLoyaltyCardList();
	}

	$scope.getLoyaltyCardByNo = function(cardNo)
	{
		$scope.patientLoyaltyCardFlag = true;
		$scope.startLoader();
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
					$scope.stopLoader();
					if(response.data.listObject!=null && response.data.listObject.length>0)
					{
						$scope.send =false;
						$scope.resend =false;
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
					    	   $scope.firstName = $scope.loyaltyCardObj.firstName;
					    	}
					    	$scope.firstName = $scope.loyaltyCardObj.firstName;
					    	$scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount-cardAmtToSub;
					    	$("#loyaltyCardSave").modal("show");
					}else{
						$scope.stopLoader();
						$scope.firstName = "";
						growl.warning("This loyalty card does not Exist",{
							title : WARNING_MSG
						});
					}
				}
				$scope.stopLoader();
			});
			}else{
				$scope.stopLoader();
				$scope.firstName = "";
			}
	}
	
	$scope.loyaltyCardObj ={
			
			"loyaltyCardNo" :"",
			"loyaltyCardBalance" :"",
			"authKey":0
	}
	
	$scope.currentLoyIndex = "";
	$scope.addloyaltyCardPayment = function(){
		
		var loyaltyCardNoFlag = false;
		angular.forEach($scope.listPaymentDetails,function(value,indx){
			
			if(value.cardNo == $scope.paymentDetails.loyaltyCardNo){
				$scope.currentLoyIndex = indx;
				loyaltyCardNoFlag = true;
			}
		});
		
		if(loyaltyCardNoFlag == true){
			
			$("#sameLoyaltyCard").modal("show");
			
		}else{
			
			$scope.addPaymentDetails();
		}
		
	}
	
	
	$scope.changeModeofLoyalty = function(diffNum){
		
		if(diffNum == 1){
			$scope.patientLoyaltyCardFlag = false;
		}
	}
	$scope.sumAmountLoyalty = function(){
		
		$scope.sumLoyaltyAmount = parseFloat($scope.listPaymentDetails[$scope.currentLoyIndex].amount) + parseFloat($scope.paymentDetails.loyaltyCardAmt);
		$scope.listPaymentDetails[$scope.currentLoyIndex].amount = $scope.sumLoyaltyAmount;
		$scope.firstName = "";
		$scope.initPaymentDetailsObj();
		$scope.calculateDueAmount();
		$scope.calculateTotalAmount();
		$scope.patientLoyaltyCardFlag = true;
	}
	
	
	/**=====================================================** loyalty Card****====================================================*/
	
	
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
	 
	
	}catch(e){
		console.log(e.message);
	}
}]);