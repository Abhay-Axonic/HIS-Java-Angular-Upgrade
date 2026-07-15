/**
 * @author Ganesh Chaudhari
 */


angular.module("myApp").controller("debitNoteListController", [ '$scope','$rootScope','BillingGenericService','GenericService','$cookies','$state','growl','paymentCollFactory','reportService','$location','loyaltiCardCallFactory',
	function($scope,$rootScope,BillingGenericService,GenericService,$cookies,$state,growl,paymentCollFactory,reportService,$location,loyaltiCardCallFactory){
	try{
		$rootScope.loginpage = true;
		$scope.page = "billList";
		
		$scope.dateOptionsTrancs = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		$scope.dateOption = {
				formatYear : 'yyyy',
				showWeeks : false
			};
		
		$scope.dateOptions = {
				formatYear : 'yyyy',
				maxDate : new Date(),
				showWeeks : false
			};
		
		$scope.dateOptionDebitTo = {
				formatYear : 'yyyy',
				maxDate : new Date(),
				showWeeks : false
			};
		
		
		$scope.chequedDateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : moment(new Date()).subtract(90, 'days').toDate(),
			};
		$scope.cardExpiryOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};
		$scope.openDatePicker = function() {
			$scope.datepicker.opened = true;
		};
		$scope.openDatePickerTo = function() {
			$scope.datepickerTo.opened = true;
		};
		
		$scope.openDatePickerPatiSearch = function() {
			$scope.datepicker1.opened = true;
		};

		$scope.datepicker1 = {
				opened : false
			};
		
		$scope.datepicker = {
			opened : false
		};
		$scope.datepickerTo = {
				opened : false
			};
		$scope.mpesaRequired = cookieObject.mpesaRequired;
		$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
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
		$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
		$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
		$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
		
		$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
		$scope.pinpadAmounFlag = false;
		$scope.excessAmount = parseFloat(0).toFixed(2);
		$scope.unitId = cookieObject.unitId;
		$scope.userId = cookieObject.userId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId =$scope.userId;
	    $scope.validatTextFieldFlag = true;
		
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
		
		$scope.appointmentId = 0;
		$scope.encounterId = 0;
		
		//vivek added
		$scope.payeeId = '';
		$scope.consumedAmt = parseFloat(0);
		$scope.receivedFrom = "";
		$scope.identificationNumber = "";
		
		$scope.depositeList = {
			    depositeIds: [1]
			  };
		
		$scope.depositeDisabledFlag = false;
		$scope.depositeSettleBtnFlag = true;
		//vivek added
		
		$scope.patientSearchObj = {
				"uhIdNumber"      : "",
				"patientName"     : "",
				"genderId"        : "",
				"mobileNo"        : "",
				"birthDate"       : "",
				"identificationNo":"",
				"patientType"	  :"1",
				"unitId"          :$scope.unitId,
				"organizationId"  :$scope.organizationId1
		}
		
	     $scope.patientDetails = {
				"patientName" : "",
				"genderCode" : "",
				"age" : "",
				"uhIdNumber" : "",
				"dob" : "",
				"doctorFullName" : "",
				"dateOfAdmission" : "",
				"patientDischargeDate" : "",
				"wardName" : "",
				"bedNumber" : "",
				"imagePath":"",
				"patientId":"",
				"visitTypeId":"",
				"admissionId":"",
				"genderId":"",
			}
		
		
		//static List	
		$scope.getSexMasterList = function() {
    		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    			$scope.sexMasterList = response.data.listObject;
    		});
    	}
    	
    	$scope.getSexMasterList();
    	
    	$scope.getIdentificationTypeList = function(){
    		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
    				{
    			if (response.data.status == "success")
    				$scope.identificationTypeList = response.data.listObject;
    		});
    	}
    	$scope.getIdentificationTypeList();
		
		$scope.searchBills = function(billSearch){
			
			$rootScope.startLoader();
			var billSearchObj = angular.copy(billSearch);
			billSearchObj.patientId = $scope.billSearch.patientId;
			billSearchObj.billStatusId = billSearch.billStatusId == '-' ? null : billSearch.billStatusId;
			if($scope.billSearch.isOtcReg)
				billSearchObj.isOtcReg = 'Y';
			else
				billSearchObj.isOtcReg = 'N';
			
			if($scope.billSearch.fromDate != '' && $scope.billSearch.toDate != '' && typeof($scope.billSearch.fromDate) != "undefined" && typeof($scope.billSearch.toDate) != "undefined"){
				billSearchObj.fromDate = $rootScope.getFormattedDate($scope.billSearch.fromDate,false);
				billSearchObj.toDate = $rootScope.getFormattedDate($scope.billSearch.toDate,false);
			}
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/debitNote/filter";
			BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.billList = response.data.listObject;
					$rootScope.stopLoader();
				}
				else if (response.data.status == "error"){
					$rootScope.stopLoader();
				}
				
			});
		
	}
		
		$scope.initBillSearchObj = function(){
				//$scope.billSearch = {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
			$scope.billSearch = {fromDate:new Date(),toDate:new Date(),visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
		}
		
		$scope.init = function(){
			
			$scope.billing = true;
			$scope.billSearch = {};
			$scope.initBillSearchObj();
			$scope.billList = [];
			$scope.payeeList = [];
			$scope.paymentDetailsList=[];
			$scope.listPaymentDetails = [];
			$scope.availableDeposits = [];
			$scope.activeDeposit = 0;
			$scope.billDisabled = false;
			$scope.isFinancialClearance = false;
			$scope.transferToActiveDepositeFlag = false;
			$scope.resend =false;
			
			//check wether redirected from ipd charges
			if(typeof($rootScope.fromWhere) != "undefined" && $rootScope.fromWhere != ""){
				var temp = $rootScope.fromWhere.split("#");
				if(temp[0] == "ipdCharge"){
					//commented by ganesh
					//$scope.getPatientDetailsById(temp[2]);
					//$scope.billSearch.patientId = temp[2];
					$scope.searchBills($scope.billSearch);
				}
			}else{
				$scope.searchBills($scope.billSearch);
			}
		}
		$scope.initTotalAmounts=function(){
			$scope.totalPaymentAmount=parseFloat(0).toFixed(2);
			$scope.dueAmount=parseFloat(0).toFixed(2);
			$scope.refundAmount=parseFloat(0).toFixed(2);
			$scope.totalAmountSummary=parseFloat(0).toFixed(2);
			$scope.totalConcessionSummary=parseFloat(0).toFixed(2);
			$scope.netPayableSummary=parseFloat(0).toFixed(2);
			$scope.totalPayingAmount=parseFloat(0).toFixed(2);
		}
		
		$scope.patientSearchObj = {
				"uhIdNumber"      : "",
				"patientName"     : "",
				"genderId"        : "",
				"mobileNo"        : "",
				"birthDate"       : "",
				"identificationNo":"",
				"unitId"          :$scope.unitId,
				"organizationId"  :$scope.organizationId,
		}
		
		$scope.firstTabLevel = 1 ;
		
		$scope.transferToActiveDeposit = "N";
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		
		$rootScope.searchPatient = function(patientsearchObj){
			if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' )
			{
				//alert();
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
			var patSearchObj = angular.copy($scope.patientSearchObj);
			patSearchObj.patientType = 1;
		/*	if($scope.patientSearchObj.dob != "" && typeof($scope.patientSearchObj.dob) != "undefined"){
				var dt = new Date($scope.patientSearchObj.dob);
//				patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
				patSearchObj.birthDate = dt.getFullYear() + "-" +(dt.getMonth() + 1)+ "-" +dt.getDate();
			} */
			if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
				patSearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
			} 
			BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.searchPatientList = [];
				if (response.data.status == "success")
					$scope.patientList = response.data.listObject;
				
				 if(response.data.listObject==null){
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$scope.patientSearchObj.birthDate="";
						$scope.patientList=[];
						return false;
					}
		           
				if($scope.patientList.length == 0){
					//alert();
					growl.warning("No Result Found!",{
						title : ERROR_MSG
					});
				}
				patSearchObj = {};
				 $scope.patientSearchObj.birthDate="";
			});
		}
		
		$scope.getPatientDetailsById = function(patientId){
			$rootScope.startLoader();
			$scope.patientDetailsList = [];
			$scope.patientDetails = {}
			var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.patientDetailsList = response.data.listObject;
					$scope.patientDetails = response.data.listObject[0];
					$scope.patientDetails.patientName = $scope.patientDetails.firstName + " " + $scope.patientDetails.lastName;
					$scope.patientDetails.genderCode = $scope.patientDetails.genderName.charAt(0).toUpperCase();
					$scope.patientDetails.age = $scope.patientDetails.ageString
					$scope.patientDetails.mobileNo = $scope.patientDetails.mobileNumber;
					$scope.countryCallingCode = $scope.patientDetails.countryCallingCode;
					$scope.billSearch.patientId = $scope.patientDetails.patientId; 
					$scope.billing = $scope.patientDetails.visitTypeId==2?false:true;
					$('.patienttSearchResult').toggle();
				}
			});
			
		
			$rootScope.stopLoader();
		}
		
		$scope.loyaltyCardObj ={
				
				"loyaltyCardNo" :"",
				"loyaltyCardBalance" :"",
				"authKey":0
		}
		
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
			loyaltiCardCallFactory.getLoyaltyCardList($scope.selectedPatientId,$scope);
		}
		
		$scope.validateOtp = function()
		{
			
			loyaltiCardCallFactory.validateOtp($scope); 
			
		}
		$scope.isBillList = 0;
		$scope.resend =false;
		$scope.send =false;
		$scope.selectedUhid = "";
		$scope.selectedPatientId = "";
		$scope.amountPaid ="";
		$scope.amountPaidflag = true;
		$scope.billDetails = function(bill){
			
			console.log("BILL",bill);
			//alert("bill.outstanding"+bill.outstanding);
			//$scope.billDisabled = true;
			//$scope.payeeId = "";
			$scope.excessAmount = parseFloat(0).toFixed(2);	
			$scope.transferToActiveDeposit = 'N'
			$scope.validatTextFieldFlag = true;
			$scope.amountPaid = bill.amountPaid;
			$scope.rountOffString = '';
			loyaltiCardCallFactory.getLoyaltyCardList(bill.patientId,$scope);
			$scope.billServicesList=[];
			$scope.payeeList = [];
			$scope.listPaymentDetails = [];
			$scope.totalAmtQuaBase = 0;
			$scope.totalAmtConcession = 0;
			$scope.totalAmtDiscount = 0;
			$scope.totalAmtGst = 0;
			$scope.totalNetPayableAmt = 0;
			$scope.payeeDueAmtFinal = 0;
			$scope.payeeDueAmtStatic = 0;
			$scope.totalSelfPayable = 0;
			$scope.totalCreditPayable = 0;
			$scope.consumedAmt = parseFloat(0);
			$scope.visitTypeId = bill.visitTypeId;
			$scope.referalDocId = bill.referalDocId;
			$scope.payeeId = $scope.selfPayeeIdCookies;
			$scope.selectedUhid = bill.uhidNumber;
			$scope.selectedPatientId =bill.patientId;
			$scope.billDisabled = bill.billStatusId == 3 ? true : false;
			$scope.billId = bill.billingMasterId;
			$scope.isInterimOrFinalBill = bill.isInterimOrFinalBill;
			$scope.doctorId = bill.doctorId;
			$scope.totalAmount = bill.netAmount; 
			$scope.dueAmount = bill.outstanding;
			$scope.oldDueAmount = bill.outstanding;
			$scope.billDueAmount = bill.outstanding;
			$scope.totalSelfPayable = bill.totalSelfPayable;
			$scope.totalCreditPayable=bill.totalCreditPayable
			$scope.currentBill = bill;
			$scope.admissionId = bill.visitTypeId == 2 ? bill.visitId : null;
			$scope.isFinancialClearance = false;
			
			$scope.disallowedAmount = bill.disallowedAmount;
			$scope.disallowedAgainstInvoiceDtlsId = bill.disallowedAgainstInvoiceDtlsId;
			
			$('#isFinancialClearance').prop('checked',false);
			//Vivek added
			$scope.disallowedAmount = bill.disallowedAmount;
            $scope.disallowedAgainstInvoiceDtlsId = bill.disallowedAgainstInvoiceDtlsId;
			$scope.encounterId = bill.visitId;
			$scope.appointmentId = bill.appointmentId;
			$scope.patientId = bill.patientId;
			//End Vivek Added
			//get payer details
			var URI = BASE_URL + ROOT_URL + "/api/billing/debitNoteDetails/"+bill.billingMasterId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.payeeList = response.data.listObject;
					$scope.billServicesList = response.data.object;
					var totalAmtQuaBase = parseFloat(0);
					var totalAmtConcession = parseFloat(0);
					var totalAmtDiscount = parseFloat(0);
					var totalAmtGst = parseFloat(0);
					var totalNetPayableAmt = parseFloat(0);
					var discountRoundOff = parseFloat(0);
					var totalSelfPayable = parseFloat(0);
					var totalCreditPayable = parseFloat(0);
					
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
				  	      });
					
					
					$scope.totalAmtQuaBase =totalAmtQuaBase;
					$scope.totalAmtConcession = totalAmtConcession;
					$scope.totalAmtDiscount =totalAmtDiscount+Number(discountRoundOff);
					$scope.rountOffString = Number(discountRoundOff)>0 ? '(RoundOff : '+discountRoundOff+')' :'';
					$scope.totalAmtGst = (totalAmtGst).toFixed(2);
					$scope.totalNetPayableAmt = totalNetPayableAmt+(Number(discountRoundOff));
					
					angular.forEach($scope.payeeList,function(value1,key){
						if(value1.payeeId == $scope.selfPayeeIdCookies)
							{
							$scope.payeeDueAmtFinal =  Number(parseFloat(value1.dueAmount).toFixed(2));
							$scope.payeeDueAmtStatic = Number(parseFloat(value1.dueAmount).toFixed(2));
							}
					});
					angular.forEach($scope.payeeList,function(value,key){
						
						if(value.payeeId==$scope.payeeId && (value.billAmount==0 || value.dueAmount==0))
							$scope.billDisabled = true;
					})
					//get available active deposits by patient
					var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+bill.patientId+"/"+bill.visitId+"/"+bill.visitTypeId+"/0/false";
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
				}
			});
			
		}
		
		
		$scope.clearLoylty = function(){
			
			$scope.send = false;
			$scope.validatTextFieldFlag = true;
			$scope.paymentDetails.otp ="";
			$scope.calculateTotalDueRefAmount();
			loyaltiCardCallFactory.getLoyaltyCardList($scope.selectedPatientId,$scope);
		}
		
		//-----------------payment Collection----------------//
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
					'hospitalAccountId'     : '',
					'transactionNo'         : ""
					
			}
		}
		$scope.initPaymentDetails();
		
		$scope.paymentModes = {
				1: "Cash",
				2: "Cheque",
				3: "Credit Card",
				4: "Debit Card"
		}
		$scope.addBtnId=false;
		$scope.setPaymentModeId=function(paymentModeId){
			if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
				$scope.addBtnId=true;
				 $scope.pinpadAmounFlag =true;
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
		

		//Ganesh Start
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
			var tempDueAmt = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
			var temppayeeDueAmtFinal = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
			$scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;
			$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
			var df = (parseFloat($scope.payeeDueAmtStatic) - (parseFloat(amount) + parseFloat($scope.consumedAmt)));
			if(df > 0)
				{
				$scope.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
				$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
				  $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);  
				}
			else
				{
					$scope.dueAmount = parseFloat(0.00).toFixed(2);
					$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
					$scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
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
		
		$scope.changePayee = function(payeeId){
			$scope.listPaymentDetails = [];
			//alert("bill.outstandingChabge"+$scope.dueAmount);
			
			$scope.calculateTotalDueRefAmount();
			$scope.payeeId = payeeId;
			if(payeeId >$scope.selfPayeeIdCookies)
				{
				  $scope.billDisabled = true;
				}
			else
				{
				  $scope.billDisabled = false;
				}
		}
		
		// NEW FOR VIVEK
		$scope.settleBill = function(){
			
			$scope.billDisabled = true;
			
			$('#collectPayment').modal('hide');
			$('#debitNoteListView').modal('hide');
			$rootScope.startLoader();
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
					dateOfReciept : moment(new Date()).format('DD-MM-YYYY HH:MM:SS'),
					payeeId : $scope.payeeId,
					//listBillingPaymentDetailsReqDto : $scope.listPaymentDetails,
					paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
					orgnizationId : $scope.organizationId,
					unitId : $scope.unitId,
					createdDate : moment(new Date()).format('DD-MM-YYYY HH:MM:SS'),
					updatedDate : moment(new Date()).format('DD-MM-YYYY HH:MM:SS'),
					updatedBy:$scope.userId,
					createdBy:$scope.userId,
					receiptType:1,
					receivedFrom:$scope.receivedFrom,
					identificationNumber : $scope.identificationNumber,
					remark : "",
					amount : (parseFloat($scope.amountCollected) + parseFloat($scope.consumedAmt)).toFixed(2) ,
					//consumedAmount : $scope.consumedAmt,
					//totalCollection : $scope.amountCollected,
					isTransferToActiveDeposit : this.transferToActiveDeposit,
					totalDepositConsumption : $scope.consumedAmt,
					isPreOrPostBilling:$scope.isPreOrPostBilling,
					isFinancialClearance : $scope.isFinancialClearance ? 'Y' : 'N',
					appointmentId:$scope.appointmentId,
					encounterId : $scope.encounterId,
					counterId : $rootScope.counterId,
					paymentDepositDetailsList : $scope.finalAvailableDeposits,
					payeeDueAmount :payeeDueAmount,
					admissionId : $scope.admissionId,
					visitTypeId : $scope.visitTypeId,
					patientId : $scope.patientId,
					referalDocId : $scope.referalDocId,
					outstanding : $scope.oldDueAmount,
					disallowedAmount : $scope.disallowedAmount,
					disallowedAgainstInvoiceDtlsId : $scope.disallowedAgainstInvoiceDtlsId,
					isNursingAssessmentRequired : 'Y'
			}
			if($scope.payeeId == '' || typeof($scope.payeeId) == "undefined"){
				$scope.billDisabled = false;
				growl.error("Please select a payee",{
					title : ERROR_MSG
				});
				return;
			}
			
			if(($scope.finalAvailableDeposits.length == 0)&&($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')){
				$scope.billDisabled = false;
				growl.error("Please select at least one payment",{
					title : ERROR_MSG
				});
				return;
			}
			
			if(($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)){
				$scope.billDisabled = false;
				growl.error("Amount can't be zero",{
					title : ERROR_MSG
				});
				return;
			}
			
			console.log("param",param);
			//return false;
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/payments";
			BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if(response.data.status == "success"){
					
					$scope.resend =false;
					//collectPayment
					//billListDetails
					$scope.excessAmount = parseFloat(0).toFixed(2);
					$scope.billDisabled = false;
					$scope.validatTextFieldFlag = true;
					$scope.transferToActiveDeposit = "N";
					this.transferToActiveDeposit = "N";
					angular.element('#collectPayment').modal('hide');
					angular.element('#debitNoteListView').modal('hide');
					$scope.billDetails($scope.currentBill);
					$scope.calculateTotalDueRefAmount();
					$scope.listPaymentDetails = [];
					$scope.searchBills($scope.billSearch);
					$scope.mpesaObject={};
					console.log(response);
					$rootScope.startLoader();
					if(response.data.object != null)
					{
						var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/0/"+$scope.userId+"/"+response.data.object.recieptId;
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
										                  "paramName": "@BillId",
										                  "paramType": "Integer",
										                  "paramValue": $scope.currentBill.billingMasterId
									                },
									                {
										                  "paramName": "@PaymentId",
										                  "paramType": "Integer",
										                  "paramValue": response.data.object.recieptId
									                },
									                {
										                  "paramName": "@dep_consumption_id",
										                  "paramType": "Integer",
										                  "paramValue": response.data.object.depositConsumptionId
									                }
				                 ]
								};
								reportService.generate(REPORT_API_URI1,data);
								
							}
						});
					    
					}

				}
				else if(response.data.status == "error")
					{
					  $rootScope.stopLoader();
					}
			});
			
		}
		
		
		
		$scope.closeDigitaslSig = function()
		{
			 angular.element('#digitalSignaturePopup').modal('hide');
		}
		
		/*$scope.checkAmount = function(amount)
		{
			
			if(this.transferToActiveDeposit == 'N'){
			if(parseFloat(amount) > parseFloat($scope.payeeDueAmtFinal))
				{
				growl.error("Entered amount should not be greater than self due amount...!!",{
					title : ERROR_MSG
				});
				$scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
				return;
					
				}
			}
		}*/
		
		// OLD FOR YOGESH
		/*$scope.settleBill = function(){
			//alert('SET');
			var param = {
					billingMasterId : $scope.billId,
					payeeId : $scope.payeeId,
					listBillingPaymentDetailsReqDto : $scope.listPaymentDetails,
					orgId : $scope.organizationId,
					unitId : $scope.unitId, 
					appointmentId:$scope.appointmentId,
					updatedBy:$scope.userId,
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					isPreOrPostBilling:$scope.isPreOrPostBilling,
					encounterId : $scope.encounterId,
					isFinancialClearance : $scope.isFinancialClearance ? 'Y' : 'N',
					admissionId : $scope.admissionId,
			}
			
			if($scope.payeeId == '' || typeof($scope.payeeId) == "undefined"){
				growl.error("Please select a payee",{
					title : ERROR_MSG
				});
				return;
			}
			
			if($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined'){
				growl.error("Please select at least one payment",{
					title : ERROR_MSG
				});
				return;
			}
			
			if($scope.amountCollected == '' || $scope.amountCollected <= 0){
				growl.error("Amount can't be zero",{
					title : ERROR_MSG
				});
				return;
			}
			console.log('settle obj',param);
			//return false;
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/payments";
			BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if(response.data.status == "success"){
					//collectPayment
					//billListDetails
					angular.element('#collectPayment').modal('hide');
					angular.element('#billListDetails').modal('hide');
					$scope.billDetails($scope.currentBill);
					$scope.calculateTotalDueRefAmount();
					$scope.listPaymentDetails = [];
					$scope.searchBills($scope.billSearch);
				}
			});
			$rootScope.stopLoader();
		}*/

		
		/*Vivek start*/
		
		$scope.settlementClick=function()
		{
				$scope.skipMpesaValidation = false;
				$scope.transferToActiveDeposit = 'N'
				$scope.transferToActiveDeposit = 'N'
				 $scope.excessAmount = parseFloat(0).toFixed(2);
				 $scope.send=false;
				 $scope.resend=false;
				 $scope.validatTextFieldFlag = true;				 
				// $scope.getLoyaltyCardList($scope.selectedPatientId);
				 $scope.calculateTotalDueRefAmount();
			if($scope.payeeId > 0)
				{
				$scope.setPaymentModeId(1);
				$scope.setFirstTabLevel(1);
				$scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
					angular.element('#collectPayment').modal('show');
					
					/*angular.forEach($scope.payeeList,function(value,key){
						if(value.payeeId == '1')
							{
						      $scope.payeeDueAmtFinal = value.dueAmount;
							}
					})*/
					
				}
			else
				{
					growl.warning("Please select payer!!",{
						title : WARNING_MSG
					});
					return false;
				}
		}
		
		//Ganesh Start
		$scope.depositeSub = function(item,idx)
		{
			
			if($scope.payeeDueAmtFinal > 0 || item.depositeFlag == 'N'){
			var consumedAmt = 0;
			if(item.depositeFlag == 'Y')
				{
					if($scope.dueAmount == 0)
					{
						$scope.availableDeposits[idx].depositeFlag = 'N';
					}
					else if($scope.dueAmount >= item.availableDeposit)
						{
							consumedAmt = (parseFloat(consumedAmt)+parseFloat(item.availableDeposit)).toFixed(2);
							$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.consumedDeposit)+parseFloat(item.availableDeposit);
							$scope.availableDeposits[idx].availableDeposit = parseFloat(0);
							$scope.dueAmount = (parseFloat($scope.dueAmount)- parseFloat(consumedAmt)).toFixed(2);
						}
					else if($scope.dueAmount <= item.availableDeposit)
						{
							consumedAmt = (parseFloat(consumedAmt)+parseFloat($scope.dueAmount)).toFixed(2);
							$scope.availableDeposits[idx].availableDeposit = parseFloat(item.availableDeposit)- parseFloat($scope.dueAmount);
							$scope.availableDeposits[idx].consumedDeposit =item.consumedDeposit + parseFloat($scope.dueAmount);
							$scope.dueAmount = (parseFloat($scope.dueAmount)- parseFloat($scope.dueAmount)).toFixed(2);
						}
					 
				}
			if(item.depositeFlag == 'N')
			{
						if($scope.availableDeposits[idx].availableDeposit > 0)
							{
							$scope.availableDeposits[idx].availableDeposit = parseFloat(item.oldAvailableDeposit);
							$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
							$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(item.oldAvailableDeposit-item.availableDeposit)).toFixed(2);
							}
						else{
						$scope.availableDeposits[idx].availableDeposit = $scope.availableDeposits[idx].availableDeposit + parseFloat(item.consumedDeposit-item.oldConsumedDeposit);
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
		/***************Debit Note Report**************Chetana*********/
		$scope.rePrintDebitNote=function(cnMasterObj)
		{
			
				var data =  {
			            "appName": "HIS",
			            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			            "reportDesignFilePath": "OPD_DebitNote.rpt",
			            "reportFormat": "PDF",
			            "exportFileName": "DebitNote",
			            "reportParams": [
			              {
				             "paramName": "@billing_master_id",
				             "paramType": "Integer",
				             "paramValue": cnMasterObj.billingMasterId
			              }
			            ]
			 };
			
			
		reportService.generate(REPORT_API_URI1,data);

		}

		/*************Debit Note Report END*************************/
		/*Vivek end*/
		
		$scope.validFclearance = function(flag){
			var amount = 0;
			$scope.listPaymentDetails.forEach((elem) => {amount = parseFloat(amount) + parseFloat(elem.amount)});
			
			if(($scope.currentBill.amountPaid > 0 || amount > 0) && flag ){
				$scope.isFinancialClearance = true;
				$('#isFinancialClearance').prop('checked',true);
			}else if(($scope.currentBill.amountPaid <= 0 || amount <=0) && flag){
				$scope.isFinancialClearance = false;
				$('#isFinancialClearance').prop('checked',false);
				growl.error("Please pay some amount to get financial clearance",{
					title : ERROR_MSG
				});
			}else{
				$scope.isFinancialClearance = false;
				$('#isFinancialClearance').prop('checked',false);
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
		
		$scope.addPaymentDetails = function(){
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
		var parentChild = 0;
		$scope.addExAmount = function(){
			$scope.isBillList = 0;
			if( parentChild == 0 ){
				$scope.transferToActiveDeposit = 'Y';
				$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
			}else{
				
				$scope.transferToActiveDeposit = 'Y';
				$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
			}
			
			paymentCollFactory.addPaymentDetails($scope);
			$("#confirmSave").modal("hide");
			parentChild += 1;
		}
		
		$scope.changeExAmount = function(){
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
			
		/*	
			if($scope.paymentDetails.cashAmount >=0){
			
				$scope.transferToActiveDeposit = 'N';
				//	this.$parent.$parent.transferToActiveDeposit = 'N';
				//	this.$parent.$parent.$parent.transferToActiveDeposit = 'N';
			}*/
		}
		
		/* *************************************Mpesa Integration start****************************************** */
		$scope.skipMpesaValidation = false;
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
					$scope.skipMpesaValidation = false;
					$rootScope.stopLoaderPopup();
				}
				$rootScope.stopLoaderPopup();
			});
			}else{
				growl.error("Enter both transaction id and mobile number",{
					title : ERROR_MSG
				});
				$rootScope.stopLoaderPopup();
				$scope.skipMpesaValidation = false;
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
							uhidNumber: $scope.selectedUhid,
							phoneNumber:parseInt(countryCallingCode+""+mobileNumber),
							partyA:mobileNumber,
							amount:validateMpesaAmount
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
							
							growl.error(response.data.message,{
								title : ERROR_MSG
							});
							$rootScope.stopLoaderPopup();
							$scope.skipMpesaValidation = false;
						}
						
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
				patientId: $scope.selectedPatientId,
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
             
             
//					/$scope.ipAddress+
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
											patientId : $scope.selectedPatientId,
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
	
	$scope.depositeEnableDisabled = function(){
		if($scope.transferToActiveDeposit == 'Y')
			{
			  $scope.depositeDisabledFlag = true;
			}
		else
			{
			  $scope.depositeDisabledFlag = false;
			}
	}
	
	$scope.cancelExAmount = function(){
		$('#confirmSave').modal('hide');
		$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
		$scope.excessAmount = parseFloat(0.0).toFixed(2);
	}
	
	$scope.cancelPaymentReqModal = function(){
		angular.element('#paymentReqModal').modal('hide');
	}
	
	$scope.clearMpesaFields = function(){
		$scope.skipMpesaValidation = false;
		$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
	}
	
		$scope.init();
		$scope.initTotalAmounts();
		
	}catch(e){
		console.log(e.message);
	}
}]);