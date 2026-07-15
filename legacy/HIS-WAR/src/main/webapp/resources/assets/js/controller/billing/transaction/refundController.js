	

angular.module("myApp").controller("refundController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','reportService',
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
	$scope.dateOptionsCard = {
			formatYear : 'yyyy',
			showWeeks : false,
			minDate : new Date()
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
	
	$scope.dateOptionsSearch = {
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : new Date()
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
	
	
	
	
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	$scope.refundCnMasterListDup = [];
	$scope.searchKeyword = "";
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	$scope.depositTypeWiseRequired =  cookieObjectAppConfig.depositTypeWiseRequired;
	
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
	
	
	$scope.fromDate = moment().toDate();
	$scope.toDate = moment().toDate();
	/**================================COUNTER CODE END======================================*/
	
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
			  "paymentRefundVoucherNo" :""	  
		}
	
	
	$scope.depositAgainst = function(depositTypeId)
    	{
    	   if(depositTypeId == 1){
    	      return 'Regular';
    	   }
    	   if(depositTypeId == 2){
    	     return 'Encounter';
    	   }
    	   if(depositTypeId == 5){
           	  return 'OT Reservation';
           }
           if(depositTypeId == 3)
           {
              return 'Bed Reservation';
           }
            if(depositTypeId == 6)
            {
               return 'Pharmacy Deposit';
            }
           return "-";
    	}
	
	
	$scope.checkDate= function()
	{
		if($scope.patientSearchObj.birthDate == undefined || $scope.patientSearchObj.birthDate == null || $scope.patientSearchObj.birthDate == ''){
			   growl.warning("Date is invalid",{
					title : "warning"
				});
				$scope.patientSearchObj.birthDate = new Date();
			
			   return false;
		}
	
	}
	
	//For Get Active Gender List
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
	
	
	
	$scope.initDepositSearchObj = function(){
		$scope.depositSearch = angular.copy({fromDate:"",toDate:"",patientName:"",uhidNumber:"",visitTypeId:"",depositTypeId:""});
	}
	
	$scope.initDepositSearchDtls = function(){
		$scope.depositSearchDtls = angular.copy({totalDepositAmount:parseFloat(0).toFixed(2),
			totalConsumedDepositAmount:parseFloat(0).toFixed(2),
			totalBalanceAmount:parseFloat(0).toFixed(2),
			totalCnRefundAmount:parseFloat(0).toFixed(2),
			totalCnBalanceAmount:parseFloat(0).toFixed(2),
			});
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
			  counterId :$rootScope.counterId
	  })
	};
	
	
	$scope.initRefundCNMasterDto = function()
	{
		$scope.refundCNMasterDto = angular.copy(
				{
						  tRefundCnId : "",
						  refundCnNo : "",
						  refundCnDate : "",
						  payeeId : "",
						  depositId : "",
						  refundAgainstId : "",
						  visitTypeId : "",
						  encounterId : "",
						  admissionId : "",
						  refundAmount : "",
						  refundedAmount : "",
						  balanceAmount : "",
						  accountHolderName : "",
						  remark:'',
						  bankId : "",
						  branchId : "",
						  ifscCode : "",
						  accountNo : "",
						  bankRequestRefundAmount : "",
						  organizationId : $scope.organizationId,
						  unitId : $scope.unitId,
						  createdDate : "",
						  createdBy : $scope.userId,
						  updatedDate : "",
						  updatedBy : $scope.userId,
						  counterId :$rootScope.counterId,
						  isApproved:"N",
						  requestSentTo:"",
						  patientId:"",
				});
	}
	
	$scope.initAmountObj = function()
	{
		$scope.amountObj = {
				"availableDeposit":"",
				"refundedAmount":"",
				"remark":""
		};
		$scope.totalDepositAmount = 0.00;
		$scope.totalConsumetedAmount = 0.00;
		$scope.totalRefundAmount = 0.00;
		$scope.totalBalanceAmount = 0.00;
	};
	
	$scope.init = function(){
		$scope.totalDepositAmount = 0.00;
		$scope.totalConsumetedAmount = 0.00;
		$scope.totalRefundAmount = 0.00;
		$scope.totalBalanceAmount = 0.00;
	    $scope.identificationTypeList = [];
		$scope.sexMasterList = [];
		$scope.getSexMasterList();
		$scope.getIdentificationTypeList();
		$scope.initRefundCNMasterDto();
		$scope.initPaymentRefundObj();
		$scope.patientDetailsList = [];
		$scope.initDepositSearchDtls;
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
		$scope.initDepositSearchObj();
		$scope.depositSearchList = [];
		$scope.depositSearchDtls = {};
		$scope.visitNoList = [];
		$scope.initAmountObj();
	}
	
	
	
	$scope.init();
	


	

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
	$scope.searchPatient = function(patientSearchObj)
	{
		
		$rootScope.startLoader();
		$scope.patientSearchObj.unitId = cookieObject.unitId;
		$scope.patientSearchObj.organizationId = cookieObject.organizationId;
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
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
		var patSearchObj = angular.copy($scope.patientSearchObj);
		if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
			var dt = new Date($scope.patientSearchObj.birthDate);
			patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
		} 
		BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.searchPatientList = [];
			console.log("response",response);
			if (response.data.status == "success")
				{
				angular.forEach(response.data.listObject,function(value,index)
						{
					$scope.patientList.push(value);
						})
				patSearchObj = {};
				
				$scope.initPaymentDetailsObj();
				$scope.initRefundCNMasterDto();
				$scope.listPaymentDetails = [];
				$scope.patientDetailsList = [];
				$scope.patientDetails = {};
				$scope.initAmountObj();
				}
			else if(response.data.status == "error")
			{
			   $rootScope.stopLoader();
			}
			
		});
		
	}
	
	$scope.getPatientDetailsById = function(patientId)
	{
		$rootScope.startLoader();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {}
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.patientDetailsList = response.data.listObject;
				$scope.patientDetails = response.data.listObject[0];
				$scope.initPaymentRefundObj();
				$scope.initDepositSearchDtls();
				$scope.initRefundCNMasterDto(); 
				$scope.initAmountObj();
				$scope.init();
				var index = $scope.patientList.findIndex(x => x.patientId ==patientId);
				$scope.patientDetails.age=($scope.patientList[index].age);
				$scope.patientDetails.patientName=($scope.patientList[index].patientName);
				$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
				$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
				$scope.patientDetails.patientId=($scope.patientList[index].patientId);
				$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
				$scope.deposit.patientId = $scope.patientDetails.patientId;
				$scope.refundCNMasterDto.patientId= $scope.patientDetails.patientId;
				$scope.depositList = [];
				$scope.depositTypeId="";
				$rootScope.stopLoader();
			}
			else if(response.data.status == "error")
			{
			  $rootScope.stopLoader();
			}
		});
		
	}
		
	$scope.getDepositDetails = function(depositMasterId)
	{
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/details/"+$scope.organizationId+"/"+$scope.unitId+"/"+depositMasterId;
		BillingGenericService.serviceAction(METHOD_GET,URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			$scope.depositDetailsList = [];
			console.log("response",response);
			if (response.data.status == "success")
				{
				 $scope.depositDetailsList = response.data.listObject;
			     $('#payment_mode_Modal').modal('show');
				 $rootScope.stopLoader();
				}
			else if(response.data.status == "error")
				{
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
	

	$scope.setPaymentModeId = function(value)
	{
		$scope.paymentDetails.paymentModeId = value;
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
	
	$scope.calculateDueAmount = function(){
		//calculate due amount
		
		$scope.paymentRefundObj.amountRefunded = 0;
		var collectedAmount = parseFloat(0).toFixed();
		angular.forEach($scope.listPaymentDetails,function(value,key){
			collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
		});
		$scope.paymentRefundObj.amountRefunded = collectedAmount;
		$scope.dueAmount = $scope.refundCNMasterDto.refundAmount != '' && typeof($scope.refundCNMasterDto.refundAmount) != 'undefined' ? parseFloat(parseFloat($scope.refundCNMasterDto.refundAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
		$scope.paymentDetails.cashAmount = $scope.dueAmount;
		$scope.paymentDetails.chequeAmount = $scope.dueAmount;
		$scope.paymentDetails.debAmount = $scope.dueAmount;
		$scope.paymentDetails.credAmount = $scope.dueAmount;
		$scope.paymentDetails.loyaltyCardAmt =  $scope.dueAmount;
		$scope.eTransferObj.transferAmount = $scope.dueAmount;
	}
	
	//remove payment details
	$scope.removePaymentDetails = function(index){
		$scope.listPaymentDetails.splice(index,1);
		$scope.calculateDueAmount();
		$scope.calculateTotalAmount();
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
			return date + "-" + month + "-" + year + " 00:00:00";
	}
	
	//search deposit
	$scope.depositTypeId="";
	$scope.getDeposit = function(depositTypeId)
	{
		
		$scope.depositTypeId = depositTypeId;
		if(($scope.depositTypeId==""||$scope.depositTypeId==undefined))
			{
			growl.error("Please Select Deposit Type.",{
				title : ERROR_MSG
			});
			return false;
			}
		if($scope.patientDetails.patientId==null)
			{
			growl.error("Please Select Patient.",{
				title : ERROR_MSG
			});
			return false;
			}
		else{
			$rootScope.startLoader();
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
		                $scope.initAmountObj();
		                $scope.refundCNMasterDto.refundAmount ='';
						angular.forEach($scope.depositList,function(value,key){
							$scope.totalDepositAmount = parseFloat($scope.totalDepositAmount) + parseFloat(value.depositAmount);
							$scope.totalConsumetedAmount = parseFloat($scope.totalConsumetedAmount) + parseFloat(value.consumedDeposit);
							$scope.totalRefundAmount = parseFloat($scope.totalRefundAmount) + parseFloat(value.refundAmount);
							$scope.totalBalanceAmount = parseFloat($scope.totalBalanceAmount) + parseFloat(value.availableDeposit);
							
							
						});
						$scope.totalDepositAmount =parseFloat($scope.totalDepositAmount).toFixed(2);
						$scope.totalConsumetedAmount =parseFloat($scope.totalConsumetedAmount).toFixed(2);
						$scope.totalRefundAmount=parseFloat($scope.totalRefundAmount).toFixed(2);
						$scope.totalBalanceAmount =parseFloat($scope.totalBalanceAmount).toFixed(2);
						$rootScope.stopLoader();
					}
					else if (response.data.status == "error")
					{
						$rootScope.stopLoader();
					}
					
				});
		}
		
	}
	


	
	
	/*$scope.checkRefundRights = function()
	{
		$scope.refundCNMasterDto.isApproved='Y';
	     $scope.showCollectPaymentModal();
		var getRefundRightByUser = function()
		{
			var URI = BASE_URL + ROOT_URL+"/billing/refund/rights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/user/"+$scope.userId;
			console.log('URI',URI);
		    return BillingGenericService.serviceAction(METHOD_GET, URI,"",
		        NOTIFICATION_MSG_STATUS_FALSE)
		      .then(function(response) {
		         console.log(response);
		         // $rootScope.startLoader();
		         if (response.data.status == "success")
		          {
		           return response.data.object;
		          }
		          
		        });
		}
		var promise = getRefundRightByUser();
		promise.then(function(obj) 
				{
					      if(obj!=null  && obj.isRefundApproval!=null && obj.isRefundApproval=='Y')
					    	  {
					    	     $scope.refundCNMasterDto.isApproved='Y';
					    	     $scope.showCollectPaymentModal();
					    	  }
					      else{
					    	  $rootScope.startLoader();  
					    	  var URI = BASE_URL + ROOT_URL	
						      + "/billing/users/refund/rights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
						       BillingGenericService.serviceAction(METHOD_GET, URI,"",
						        NOTIFICATION_MSG_STATUS_FALSE)
						      .then(function(response) {
						         console.log(response);
						          $rootScope.startLoader();
						         if (response.data.status == "success")
						          {
						        	 $scope.userList = response.data.listObject;
						        	 if($scope.userList==null)
					        		 {
					        		 growl.warning("Please Assign User for Credit Note Approval.",{
					     				title : WARNING_MSG
					     			  });
					        		  return false;
					        		 }
						        	 else{
						        	   $('#refundRequest').modal('show');
						        	   $scope.refundCNMasterDto.isApproved='N';
						        	   $rootScope.stopLoader();
						        	 }
						          }
						         else if(esponse.data.status == "error")
						        	 {
						        	   $rootScope.stopLoader();
						        	   $('#refundRequest').modal('hide');
						        	 }
						          
						        });
					          }
			    });
	}*/
	
	
	
		
			
	$scope.selectDeposit = function(deposit)
	{
         $scope.initAmountObj();
         $scope.refundCNMasterDto.refundAmount ='';
		$scope.refundCNMasterDto.payeeId =  deposit.payeeId==null?"":deposit.payeeId;
		$scope.refundCNMasterDto.depositId =   deposit.depositMasterId==null?"":deposit.depositMasterId;
		$scope.refundCNMasterDto.refundAgainstId  = deposit.depositTypeId==null?"":deposit.depositTypeId;
		$scope.refundCNMasterDto.visitTypeId = deposit.visitTypeId==null?"":deposit.visitTypeId;
		$scope.refundCNMasterDto.encounterId = deposit.encounterId==null?"":deposit.encounterId;
		$scope.refundCNMasterDto.admissionId = deposit.admissionId==null?"":deposit.admissionId;
		$scope.amountObj.availableDeposit = parseFloat(deposit.availableDeposit).toFixed(2);
		
	}
	
	//forRefundForVoucher
	$scope.saveRefundCreditNote = function()
	 {  
		$scope.refundCNMasterDto.balanceAmount = $scope.refundCNMasterDto.refundAmount;
		$scope.refundCNMasterDto.bankRequestRefundAmount = 0.00;
		$scope.refundCNMasterDto.refundedAmount = 0.00;
		$scope.refundCNMasterDto.counterId = $rootScope.counterId; 
		console.log("$scope.refundCNMasterDto",JSON.stringify($scope.refundCNMasterDto));
		
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/refund/creditnote";
		return BillingGenericService.serviceAction(METHOD_POST,URI,$scope.refundCNMasterDto,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			 return response;
		});
		
	}
	
$scope.addPaymentDetails = function()
{
	
		//check amount collected is greater tha deposit amount
		var refundAmount = $scope.refundCNMasterDto.refundAmount != "" && typeof($scope.refundCNMasterDto.refundAmount) != "undefined" ? parseFloat($scope.refundCNMasterDto.refundAmount) : parseFloat(0) ;
		//$scope.paymentRefundObj.amountRefunded = $scope.refundCNMasterDto.refundAmount;
		var collectedAmount = 0;
		var tempAmount = 0;
		
		switch($scope.paymentDetails.paymentModeId){
		case 1 :
			tempAmount = $scope.paymentDetails.cashAmount != '' && typeof($scope.paymentDetails.cashAmount) != 'undefined' ? parseFloat($scope.paymentDetails.cashAmount) : parseFloat(0);
			if(parseFloat($scope.paymentDetails.cashAmount) > refundAmount){
				growl.warning("Collected amount cannot greater than Refund amount",{
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
				growl.warning("Collected amount cannot greater than Refund amount",{
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
				growl.warning("Collected amount cannot greater than Refund amount",{
					title : WARNING_MSG
				});
				return;
			}
			break;
		
		case 4 :
			tempAmount = $scope.paymentDetails.debAmount != '' && typeof($scope.paymentDetails.debAmount) != 'undefined' ? parseFloat($scope.paymentDetails.debAmount) : parseFloat(0);
			if(parseFloat($scope.paymentDetails.debAmount) > refundAmount){
				growl.warning("Collected amount cannot greater than Refund amount",{
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
		if(collectedAmount > refundAmount){
			growl.warning("Collected amount cannot greater than Refund amount",{
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
		$scope.calculateTotalAmount();
		$scope.calculateDueAmount();
		$scope.firstName = "";
		$scope.patientLoyaltyCardFlag = true;
		
	}



$scope.showCollectPaymentModal = function()
{
	
	var foundDeposit = false;
	angular.forEach($scope.depositList,function(value,key)
	{
		if(value.availableDeposit >0){
			foundDeposit = true;
		}
	});

	if($scope.amountObj.availableDeposit ==undefined || $scope.amountObj.availableDeposit ==null || $scope.amountObj.availableDeposit =="" || $scope.amountObj.availableDeposit <= 0.00|| foundDeposit == false)
	{
		growl.warning("Please select deposit to refund",{
			title : WARNING_MSG
	});

	return false;
	}
	
	
	if(($scope.amountObj.availableDeposit!="")&&($scope.amountObj.availableDeposit>=0.00)&&($scope.refundCNMasterDto.refundAmount<=$scope.amountObj.availableDeposit))
		{
		  $scope.paymentDetails.cashAmount = $scope.refundCNMasterDto.refundAmount;
		  $scope.paymentRefundObj.patientId = $scope.patientDetails.patientId;
		  if( $scope.paymentDetails.cashAmount > 0){
			  	 $('#save_collectPaymentModal').modal('show');
				$scope.listPaymentDetails = [];
				$scope.calculateDueAmount();
				$scope.calculateTotalAmount();
			  }else{
			  	
			  	$('#save_collectPaymentModal').modal('hide');
				growl.warning("Refund Amount should be greater than 0.",{
				title : WARNING_MSG
				});
			  }
		  
		}
	else{
		$('#save_collectPaymentModal').modal('hide');
		growl.warning("Refund Amount Could not be greater than balance amount.",{
			title : WARNING_MSG
		});
	}
}


$scope.closePaymentDetailsPoup = function()
{
	if(($scope.amountObj.availableDeposit!="")&&($scope.amountObj.availableDeposit>=0.00)&&($scope.refundCNMasterDto.refundAmount<=$scope.amountObj.availableDeposit))
		{
			$('#save_collectPaymentModal').modal('hide');
			$('#saveVoucher').modal('show');
		}
		else{
			$('#save_collectPaymentModal').modal('hide');
		}	

}


$scope.checkRefundAmount = function()
{
	$scope.amountObj.availableDeposit = isNaN(Number(parseFloat($scope.amountObj.availableDeposit)))?0:Number(parseFloat($scope.amountObj.availableDeposit));
	$scope.refundCNMasterDto.refundAmount = isNaN(Number(parseFloat($scope.refundCNMasterDto.refundAmount)))?0:Number(parseFloat($scope.refundCNMasterDto.refundAmount));
	
	var foundDeposit = false;
	angular.forEach($scope.depositList,function(value,key)
	{
		if(value.availableDeposit >0){
			foundDeposit = true;
		}
	});

	if($scope.amountObj.availableDeposit ==undefined || $scope.amountObj.availableDeposit ==null || $scope.amountObj.availableDeposit =="" || $scope.amountObj.availableDeposit <= 0.00|| foundDeposit == false)
	{
		growl.warning("Please select deposit to refund",{
			title : WARNING_MSG
	});

	return false;
	}

	
	if(($scope.amountObj.availableDeposit!="")&&($scope.amountObj.availableDeposit>=0.00)&&($scope.refundCNMasterDto.refundAmount<=$scope.amountObj.availableDeposit))
	{
	
	}
	else{
		growl.warning("Refund Amount Could not be greater than balance amount.",{
			title : WARNING_MSG
		});
	}	
}

$scope.saveOnlyVoucher = function(flag)
{
	
	if(isNaN(Number($scope.refundCNMasterDto.refundAmount)))
		{
		 growl.error("Please enter Valid Amount..!!",{
				title : ERROR_MSG
			});
			return;
		}
	if(Number($scope.refundCNMasterDto.refundAmount)==0 )
	{
	 growl.error("Refund amount should not be 0..!!",{
			title : ERROR_MSG
		});
		return;
	}
	else if(Number($scope.refundCNMasterDto.refundAmount)>Number($scope.amountObj.availableDeposit))
	{
		growl.error("Refund Amount Should be less than Available deposit..!!",{
			title : ERROR_MSG
		});
		return;
	}
	if(($scope.refundCNMasterDto.requestSentTo==null||$scope.refundCNMasterDto.requestSentTo=='')&&!flag)
		{
		growl.error("Please select user.",{
			title : ERROR_MSG
		});
		return;
		}
	else{
		$rootScope.startLoader();
		var promise = $scope.saveRefundCreditNote();
	promise.then(function(response) 
			{
			  if(response.data.status == "success")
				  {
				    $scope.initPaymentDetailsObj();
					$scope.initRefundCNMasterDto();
					$scope.listPaymentDetails = [];
					$scope.patientDetailsList = [];
					$scope.patientDetails = {};
					$scope.depositList = [];
					$scope.depositTypeId = "";
					$scope.initAmountObj();
					$('#saveVoucher').modal('hide');
					growl.success('Voucher Added Succesfully.',
							{
								title : 'Success!'
							});
					$rootScope.stopLoader();
				  }
			  else{
				  $rootScope.stopLoader();
				  growl.error("Something went wrong..",{
						title : ERROR_MSG
					});
					return;
			   }
		    });
	
	}
  
}



$scope.onlyVoucher=false;
$scope.addRefund = function()
{

	if(Number($scope.tatalAmount) == 0){
		growl.error("Amount Should not be 0..!!",{
			title : ERROR_MSG
		});
		return;
	}
	
	$('#save_collectPaymentModal').modal('hide');
	if($scope.paymentRefundObj.amountRefunded == 0)
	{
		growl.error("Amount Should be greater than zero.",{
			title : ERROR_MSG
		});
		return;
	}
	/*if(parseFloat($scope.dueAmount) == 0)
	{
		growl.error("Amount Should be greater than zero.",{
			title : ERROR_MSG
		});
		return;
	}*/
	$rootScope.startLoader();
	$scope.paymentRefundObj.createdBy = $scope.userId;
	$scope.paymentRefundObj.orgnisationId = $scope.organizationId;
	$scope.paymentRefundObj.unitId = $scope.unitId; 
	$scope.paymentRefundObj.isCnOrDeposit = 0;
	$scope.paymentRefundObj.counterId = $rootScope.counterId; 
	var promise = $scope.saveRefundCreditNote();
	
	promise.then(function(response) 
		{
		  if(response.data.status == "success")
			  {
			  var depositId = response.data.object.depositId;
			  var tRefundCnId = response.data.object.tRefundCnId; 
				var param = {paymentRefundMasterDto:$scope.paymentRefundObj,listPaymentRefundDetails:$scope.listPaymentDetails};
				var URI = BASE_URL + ROOT_URL + "/api/billing/refund/"+depositId+"/"+tRefundCnId;
				console.log("URI",URI);
				console.log('param',JSON.stringify(param));
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if (response.data.status == "success")
					{
						//$scope.getDeposit($scope.depositTypeId);
						$scope.initPaymentDetailsObj();
						$scope.initRefundCNMasterDto();
						$scope.listPaymentDetails = [];
						$scope.patientDetailsList = [];
						$scope.patientList = [];
						$scope.depositList = [];
						$scope.patientDetails = {};
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
						$('#depoId').prop('checked', false);
						$scope.initAmountObj();
						$scope.depositTypeId = "";
						$rootScope.stopLoader();
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
		  else{
			  $rootScope.stopLoader();
			  growl.error("Something went wrong..",{
					title : ERROR_MSG
				});
				return;
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
	                         $scope.patientList = response.data.listObject;
	         					patSearchObj = {};
	         				$scope.initPaymentDetailsObj();
	         				$scope.initRefundCNMasterDto();
	         				$scope.listPaymentDetails = [];
	         				$scope.patientDetailsList = [];
	         				$scope.patientDetails = {};
	         				$scope.initAmountObj();
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

/*======================================================================** loyalty Card****========================================================================*/
$scope.firstName = "";
$scope.loyaltyCarNo="";
$scope.patientLoyaltyCardFlag= true;
$scope.getLoyaltyCardList = function(){
	$scope.startLoader();
	if($scope.paymentDetails.paymentModeId == 6){
		$scope.resend =false;
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
	$scope.resend =false;
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


$scope.fromDate;
$scope.toDate;
$scope.searchCNMaster = function(){
	
	$rootScope.startLoader();
	$scope.refundCNSearchObj.fromDate = ($scope.fromDate!=""&&$scope.fromDate!=undefined)?moment($scope.fromDate).format('YYYY-MM-DD'):"";
	$scope.refundCNSearchObj.toDate =($scope.toDate!=""&&$scope.toDate!=undefined)?moment($scope.toDate).format('YYYY-MM-DD'):"";
	$scope.refundCNSearchObj.orgId = $scope.organizationId;
	$scope.refundCNSearchObj.unitId = $scope.unitId;
	$scope.totalCnRefundAmount = 0.00;
	$scope.totalCnBalanceAmount = 0.00;
	var URI = BASE_URL + ROOT_URL + "/api/billing/refund/creditnote/search";
	BillingGenericService.serviceAction(METHOD_POST,URI,$scope.refundCNSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		if (response.data.status == "success")
		{
			$scope.initDepositSearchDtls();
			$rootScope.stopLoader();
			$scope.refundCnMasterList = response.data.listObject;
			$scope.refundCnMasterListDup = response.data.listObject;
			angular.forEach($scope.refundCnMasterList,function(value,key){
				$scope.totalCnRefundAmount = parseFloat($scope.totalCnRefundAmount) + parseFloat(value.refundAmount);
				$scope.totalCnBalanceAmount = parseFloat($scope.totalCnBalanceAmount) + parseFloat(value.balanceAmount);
			});
		}
		else if(response.data.status == "error")
			{
			   $rootScope.stopLoader();
			}
	});
}
$scope.searchCNMaster();
	
	$scope.searchOverAllInRefund = function(){		
		if($scope.refundCnMasterListDup.length > 0 && $scope.refundSearchAll != '' && $scope.refundSearchAll != undefined){
			$scope.refundCnMasterList = [];
			angular.forEach($scope.refundCnMasterListDup,function(val){
				if((val.refundCnNo.toLowerCase()).includes($scope.refundSearchAll.toLowerCase()) 
						|| (val.patientName.toLowerCase()).includes($scope.refundSearchAll.toLowerCase()) 
						|| (val.uhidNumber.toLowerCase()).includes($scope.refundSearchAll.toLowerCase()) 
						|| (val.mobileNo.toLowerCase()).includes($scope.refundSearchAll.toLowerCase())){
					$scope.refundCnMasterList.push(val);
				}
			});
		}
	}

	}catch(e){
		console.log(e.message);
	}


/* refund aproval against deposit*/
	$scope.checkRefundRights = function()
    	{

    	$scope.checkRefundAmount();
    	if(isNaN(Number($scope.refundCNMasterDto.refundAmount)))
        		{
        		 growl.error("Please enter Valid Amount..!!",{
        				title : ERROR_MSG
        			});
        			return;
        		}
    		var getRefundRightByUser = function()
    		{
    			var URI = BASE_URL + ROOT_URL+"/billing/deposit/refund/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/user/"+$scope.userId+"/amount/"+$scope.refundCNMasterDto.refundAmount;
    			console.log('URI',URI);
    		    return BillingGenericService.serviceAction(METHOD_GET, URI,"",
    		        NOTIFICATION_MSG_STATUS_FALSE)
    		      .then(function(response) {
    		         console.log(response);
    		         // $rootScope.startLoader();
    		         if (response.data.status == "success")
    		          {
    		           return response.data.object;
    		          }

    		        });
    		}
    		var promise = getRefundRightByUser();
    		promise.then(function(obj)
    				{
    					      if(obj!=null  && obj.isRefundApproval!=null && obj.isRefundApproval=='Y')
    					    	  {
    					    	     $scope.refundCNMasterDto.isApproved='Y';
    					    	     $scope.showCollectPaymentModal();
    					    	  }
    					      else{
    					    	  $rootScope.startLoader();
    					    	  var URI = BASE_URL + ROOT_URL
    						      + "/billing/users/refund/rights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amount/"+$scope.refundCNMasterDto.refundAmount;;
    						       BillingGenericService.serviceAction(METHOD_GET, URI,"",
    						        NOTIFICATION_MSG_STATUS_FALSE)
    						      .then(function(response) {
    						         console.log(response);
    						          $rootScope.startLoader();
    						         if (response.data.status == "success")
    						          {
    						        	 $scope.userList = response.data.listObject;
    						        	 if($scope.userList!=null && $scope.userList.length > 0)
    					        		 {
    					        		    $('#refundRequest').modal('show');
                                            $scope.refundCNMasterDto.isApproved='N';
                                            $rootScope.stopLoader();
    					        		 }
    						        	 else{
    						        	  $rootScope.stopLoader();
    						        	 growl.warning("Please Assign User for Deposit refund Approval.",{
                                             					     				title : WARNING_MSG
                                             					     			  });
                                          return false;
    						        	 }
    						          }
    						         else if(esponse.data.status == "error")
    						        	 {
    						        	   $rootScope.stopLoader();
    						        	   $('#refundRequest').modal('hide');
    						        	 }

    						        });
    					          }
    			    });
    	}
}]);