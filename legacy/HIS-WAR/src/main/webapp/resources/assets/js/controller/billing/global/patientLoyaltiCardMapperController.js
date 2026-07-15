/**
 * 
 */

angular.module('myApp')
		.controller("patientLoyaltiCardMapperController",
		['$scope','$rootScope','$state','$cookies','GenericService','BillingGenericService','PagerService','promiseFactory','$sessionStorage','growl','reportService','$location','GenericServiceFileUpload',
		function($scope,$rootScope,$state, $cookies, GenericService,BillingGenericService,PagerService,promiseFactory,$sessionStorage,growl,reportService,$location,GenericServiceFileUpload){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			$scope.mobileNumber ="";
			$scope.validateMpesaAmount ="";
			$scope.fileObj = {};
			$scope.fileObj.file = null;
			
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$scope.patientCategoryId = cookieObjectAppConfig.patientCategoryId;
			$scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
			$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
			$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
			$scope.pinpadAmounFlag = false;
			
			 $scope.dateOptionsTrancs = {
			  			formatYear : 'yyyy',
			  			showWeeks : false,
			  			minDate : new Date()
			  		};
			
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
			
			$scope.dateOptions1 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date()
				};
			$scope.chequedDateOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : moment(new Date()).subtract(90, 'days').toDate(),
				};
			
			$scope.dateOptionsSearch = {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date()
				};
			
			
			$scope.openDatePicker = function() {
				$scope.datepicker.opened = true;
			};
			
			
			//for cheque
			$scope.openDatePickerCheque = function() {
				$scope.datepickercheque.opened = true;
			};
			$scope.datepickercheque = {
					opened : false
				};
			//------

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
			$scope.mpesaRequired = cookieObject.mpesaRequired;
			$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
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
			
			$scope.getLoyaltyCardList = function(patientId){
				
				$rootScope.startLoader();
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
						$scope.loyaltyCardList = response.data.listObject;
						$rootScope.stopLoader();
					}
					else if(response.data.status == "error")
						{ 
						  $rootScope.stopLoader();
						}
				});
			}
			
			$scope.initPaymentDetailsObj = function(){
				$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:""});
			}
	      $scope.initPaientLoyaltiCardMapper = function(){
	    	    $scope.patientId=null;
	    	    $scope.depoAmt  = 0;
	    	    $scope.availableActiveDeposit = 0;
				$scope.minStartAmount = 0;
				$scope.firstNo = "";
				$scope.secondNo = "";
				$scope.thirdNo = "";
				$scope.fourthNo = ""; 
				$scope.patientLoyaltiCardMapper =
				{
					  "patientLoyaltyCardMpprId" : "",
					  "loyaltyCardId" : "",
					  "patientId" : "",
					  "patientCategoryId":$scope.patientCategoryId,
					  "cardConfigurationId" : "",
					  "organisationId" : $scope.organizationId,
					  "unitId" : $scope.unitId,
					  "status" : "A",
					  "createdDate" : "",
					  "createdBy" : $scope.userId,
					  "updatedBy" : $scope.userId,
					  "updatedDate" : "",
					  "isDeleted" : "N",
					  "isUserDefined":"Y",
					   "cardNo":""
					}
				
			};
			$scope.initPaientLoyaltiCardMapper();
			
			$scope.initDepositObj = function(){
				$scope.deposit = angular.copy({depositTypeId:"",visitTypeId:"",depositAmount:"",remarks:"",receivedFrom:"",identificationNo:"",patientId:"",encounterId:"",counterId :$rootScope.counterId})
			}
			
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
						$scope.patientList = response.data.listObject;
						patSearchObj = {};
						
						$scope.initPaymentDetailsObj();
						$scope.listPaymentDetails = [];
						$scope.initDepositObj();
						$scope.patientDetailsList = [];
						$scope.patientDetails = {};
						$scope.patientSearchObj.birthDate="";
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
					else{
						$rootScope.stopLoader();
					}
					
				});
			}
			
			$scope.getCardDetails = function(patientLoyaltyCardMpprId)
			{
				var data = {
						"patientLoyaltyCardMpprId" : patientLoyaltyCardMpprId,
						  "organisationId" : $scope.organizationId,
						  "unitId" : $scope.unitId,
						  "status" : "A",
						  "isDeleted" : "N",
						  "isUserDefined":"Y"
				}
		   var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getLoyaltiCard";
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.loyaltiCardObj = response.data.object;
					if($scope.loyaltiCardObj!=null)
						{ 
						console.log("$scope.loyaltiCardObj",$scope.loyaltiCardObj);
						$scope.availableActiveDeposit = parseFloat($scope.loyaltiCardObj.availableDeposit);
						$scope.minStartAmount = parseFloat($scope.loyaltiCardObj.minStartAmount);
						$scope.patientLoyaltiCardMapper.cardConfigurationId= $scope.loyaltiCardObj.cardConfigurationId;
						if(response.data.object.cardNo!=null){
						$scope.cardNo = response.data.object.cardNo.toString();
						$scope.firstNo = $scope.cardNo.substring(0, 4);
						$scope.secondNo = $scope.cardNo.substring(4, 8);
						$scope.thirdNo = $scope.cardNo.substring(8, 12);
						$scope.fourthNo = $scope.cardNo.substring(12, 16); 
						}
						else{
							$scope.firstNo = "";
							$scope.secondNo = "";
							$scope.thirdNo = "";
							$scope.fourthNo = ""; 
						}
					}
					$rootScope.stopLoader();						
				}
				else if (response.data.status == "error")
				{
					$rootScope.stopLoader();	
				}
			});
		}
			$scope.getPatientDetailsById = function(patientId){
				$scope.patientDetailsList = [];
				$scope.patientDetails = {}
				var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.patientDetailsList = response.data.listObject;
						$scope.patientDetails = response.data.listObject[0];
						$scope.patientDetails.patientName = $scope.patientDetails.prefixCode+" "+$scope.patientDetails.firstName+" "+$scope.patientDetails.middleName+" "+$scope.patientDetails.lastName;
						$scope.patientDetails.age = $scope.patientDetails.ageString;
						$scope.patientDetails.patientId = patientId;
						$scope.patientId = patientId;
						$scope.deposit.patientId = patientId;
						$scope.patientLoyaltiCardMapper.patientId = patientId;
						$scope.getCardDetails($scope.patientDetails.patientLoyaltyCardMpprId);
						$scope.getLoyaltyCardList(patientId);
						$rootScope.allergydata($scope.patientDetails);
					}
				});
			}
			
			
		$scope.checkUserDefinedCardNumber = function()
		{
			var cardNumber = $scope.firstNo+""+$scope.secondNo+""+$scope.thirdNo+""+$scope.fourthNo;
			if(cardNumber.length==16)
				{
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/unit/checkLoyaltyCard/"+$scope.organizationId+"/"+$scope.unitId+"/"+cardNumber;
				BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{ 
						$rootScope.stopLoader();
					}
					else if(response.data.status == "error")
						{
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
						$scope.firstNo = "";
						$scope.secondNo = "";
						$scope.thirdNo = "";
						$scope.fourthNo = "";
						$rootScope.stopLoader();
						}
					
					
				});
			}else{
				
				return false;
			}
		}
			
			$scope.getLoyaltiCardNumber = function(patientId)
			{
				if(patientId==null)
					{
					growl.error("Please select patient.",{
						title : ERROR_MSG
					});
					return false;
					}
				
				
				
				$rootScope.startLoader();
				
				var URI = BASE_URL + ROOT_URL + "/api/unit/loyaltyCardNumber/"+$scope.organizationId+"/"+$scope.unitId;
				BillingGenericService.serviceAction(METHOD_GET,URI,$scope.patientLoyaltiCardMapper,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.loyaltiCardObj = response.data.object;
						if($scope.loyaltiCardObj!=null)
							{ 
							$scope.cardNo = response.data.object.toString();
							$scope.firstNo = $scope.cardNo.substring(0, 4);
							$scope.secondNo = $scope.cardNo.substring(4, 8);
							$scope.thirdNo = $scope.cardNo.substring(8, 12);
							$scope.fourthNo = $scope.cardNo.substring(12, 16); 
							$scope.patientLoyaltiCardMapper.cardNo = response.data.object;
							}
						else{
							growl.error(response.data.message,{
								title : ERROR_MSG
							});
						}
					
						
					}else{
						growl.error(response.data.message,{
							title : ERROR_MSG
						});
						$scope.patientLoyaltiCardMapper.isUserDefined = 'Y';
					}
				});
				
			}
			
			
			$scope.getLoyalyCardNo = function(isUserDefined)
			{
				
				if(isUserDefined=='N')
					{
					  $scope.getLoyaltiCardNumber($scope.patientId);
					 
					}
				else{
					$scope.firstNo = "";
					$scope.secondNo = "";
					$scope.thirdNo = "";
					$scope.fourthNo = ""; 
				}
			}
			
			$scope.firstTabLevel = 1 ;	
			
			$scope.addBtnId=false;
			//$scope.pinpadAmounFlag =false;
			$scope.setFirstTabLevel= function(newTab)
			{
				if(newTab==3 && $scope.isPinPadRequired =='Y'){
					$scope.addBtnId=true;
					// $scope.pinpadAmounFlag =true;
				}else if(newTab==6){
					$scope.addBtnId=true;
				}
				else{
					$scope.addBtnId=false;
					 $scope.pinpadAmounFlag =false;
				}
				$scope.paymentDetails.paymentModeId = newTab;
				$scope.firstTabLevel = newTab;
			};
			$scope.isSetFirstTabLevel = function(tabNum)
			{
				return $scope.firstTabLevel === tabNum;
			};
			
			
			
		/*===============active deposit ******/
			
			$scope.addPaymentDetails = function(){
				
				//check amount collected is greater tha deposit amount
				var depositAmount = $scope.deposit.depositAmount != "" && typeof($scope.deposit.depositAmount) != "undefined" ? parseFloat($scope.deposit.depositAmount) : parseFloat(0) ;
				var collectedAmount = 0;
				var tempAmount = 0;
				
				switch($scope.paymentDetails.paymentModeId){
				case 1 :
					tempAmount = $scope.paymentDetails.cashAmount != '' && typeof($scope.paymentDetails.cashAmount) != 'undefined' ? parseFloat($scope.paymentDetails.cashAmount) : parseFloat(0);
					if(parseFloat($scope.paymentDetails.cashAmount) > depositAmount){
						growl.warning("Collected amount cannot greater than deposit amount",{
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
				/*	if($scope.paymentDetails.credExpiryDate==null||$scope.paymentDetails.credExpiryDate==undefined||$scope.paymentDetails.credExpiryDate==''){
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
						growl.warning("Collected amount cannot greater than deposit amount",{
							title : WARNING_MSG
						});
						return;
					}
					break;
					
				case 5 :
					tempAmount = $scope.mpesaObject.mpesaAmount != '' && typeof($scope.mpesaObject.mpesaAmount) != 'undefined' ? parseFloat($scope.mpesaObject.mpesaAmount) : parseFloat(0);
					
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
						growl.warning("Collected amount cannot greater than deposit amount",{
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
					
					if($scope.eTransferObj.transactionobj.id == "" || $scope.eTransferObj.transactionobj == undefined ){
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
						if($scope.eTransferObj.transactionNo == value.transactionNo)
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
				if(collectedAmount > depositAmount){
					growl.warning("Collected amount cannot greater than deposit amount",{
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
						var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:$scope.paymentDetails.transactionNo,skipMpesaValidation:$scope.pinpadAmounFlag!=undefined ? $scope.pinpadAmounFlag : null};
						$scope.listPaymentDetails.push(paymentDetails);
						$scope.clearCardFields();
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
						
				}
				
				$scope.eTransferObj={};
				$scope.initPaymentDetailsObj();
				$scope.calculateDueAmount();
			}
			
			$scope.calculateDueAmount = function(){
				//calculate due amount
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
			}
			
			//remove payment details
			$scope.removePaymentDetails = function(index){
				$scope.listPaymentDetails.splice(index,1);
				$scope.calculateDueAmount();
			}
			
			$scope.parseFloatNum = function(number){
				return (number != '' && typeof(number) != 'undefined' && !isNaN(number)) ? parseFloat(number).toFixed(2) : parseFloat(0).toFixed(2);
			}
			
			
			$scope.uploadEncounterDoc = function(Obj)
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
				if($scope.dueAmount > 0){
					growl.error("Please clear the due amount",{
						title : ERROR_MSG
					});
					return;
				}
				
				var collectedAmount = parseFloat(0).toFixed();
				angular.forEach($scope.listPaymentDetails,function(value,key){
					if(value.paymentModeId == 1){
						collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
					}
					
				});
				
				if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
					$('#uploadpanCardDetailsLoyalty').modal('show');
						return false;
				}
				$('#uploadpanCardDetailsLoyalty').modal('hide');
				
				$('#collectPaymentModalLoyaltiCard').modal('hide');
				$rootScope.startLoader();
				$scope.deposit.createdBy = $scope.userId;
				$scope.deposit.orgId = $scope.organizationId;
				$scope.deposit.unitId = $scope.unitId; 
				$scope.deposit.counterId = $rootScope.counterId; 
				$scope.deposit.depositTypeId = 4;
			
				
				var param = {depositMasterDto:$scope.deposit,listPaymentDepositDetailsDto:$scope.listPaymentDetails};
				var URI = BASE_URL + ROOT_URL + "/api/billing/unit/recharge/card";
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if (response.data.status == "success"){
						
						$rootScope.stopLoader();
						if($scope.fileObj.file != null){
							$scope.uploadEncounterDoc(response.data.object);
						}
						$scope.initPaymentDetailsObj();
						$scope.listPaymentDetails = [];
						$scope.initDepositObj();
						$scope.patientDetailsList = [];
						
						$scope.deposit.patientId = $scope.patientDetails.patientId;
						//$scope.getLoyaltyCardList($scope.patientDetails.patientId);
						$scope.getPatientDetailsById($scope.patientDetails.patientId);
						$scope.patientDetails = {};
						$scope.mpesaObject={};
						
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
					return date + "-" + month + "-" + year + " 00:00:00";
			}
			
			

			$scope.init = function(){
				
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
				$scope.getHospitalBankList();//ADDED LATER GANESH
				$scope.getCardTypesList();
				$scope.dueAmount = parseFloat(0).toFixed(2);
				$scope.visitNoList = [];
			}
			$scope.init();
		/***==========================================**/	
			
	
		$scope.showPaymentCollection = function(loyaltyCard)
			{
			if(loyaltyCard.rechargeAmount<$scope.minStartAmount){
				growl.error("Deposit amount can not be less than minimum start amount",
						{
							title : 'Error!'
						});
				//loyaltyCard.rechargeAmount=0;
				return false;
			}
			    if(loyaltyCard.rechargeAmount>0)
			    	{
			    	$scope.skipMpesaValidation = false;
			    	$scope.pinpadAmounFlag = false;
			    	 $scope.setFirstTabLevel(1);
					 $scope.setPaymentModeId(1);
			    	 $scope.listPaymentDetails = [];
					  $scope.paymentDetails.cashAmount = parseFloat(loyaltyCard.rechargeAmount).toFixed(2);
					  $scope.dueAmount = parseFloat(loyaltyCard.rechargeAmount).toFixed(2);
					  $scope.deposit.depositAmount= parseFloat(loyaltyCard.rechargeAmount).toFixed(2);
						$scope.deposit.patientLoyaltyCardMpprId = loyaltyCard.patientLoyaltyCardMpprId;
						$scope.calculateDueAmount();
					  $('#collectPaymentModalLoyaltiCard').modal('show');
			    	}
			    else{
			    	growl.error("Please enter deposit Amount.",
							{
								title : 'Error!'
							});
					  
					  return false;
			    }
					 
			}
		
		
		$scope.changeDepositAmt = function()
		{
		   var tempAmt = 	$scope.minStartAmount - $scope.availableActiveDeposit
			if($scope.depoAmt<tempAmt)
				{
				growl.error("minimum deposit amount is "+parseFloat(tempAmt).toFixed(2),
						{
							title : 'Error!'
						});
				  
				  return false;
				}
			else{
				 $scope.paymentDetails.cashAmount = parseFloat($scope.depoAmt).toFixed(2);
				 $scope.dueAmount = parseFloat($scope.depoAmt).toFixed(2);
			   }
		}
			
			$scope.saveCardDetail = function(){
				if($scope.patientDetails.patientLoyaltyCardMpprId==0)
				{
					try{
						
						$scope.patientLoyaltiCardMapper.cardNo = $scope.firstNo+""+$scope.secondNo+""+$scope.thirdNo+""+$scope.fourthNo;
						var cardNo = $scope.patientLoyaltiCardMapper.cardNo;
						var len = ('' + cardNo).length;
						if(len == 16){
							
							$rootScope.startLoader();
							var date = new Date();
							$scope.$broadcast('show-errors-check-validity');
							var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/patientLoyaltyCard";
							console.log("Save-URI-",URI);
							console.log("$scope.patientLoyaltiCardMapper",$scope.patientLoyaltiCardMapper);
							GenericService.serviceAction("POST",URI,$scope.patientLoyaltiCardMapper).then(function(response){
								
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									growl.success(response.data.message,
											{
												title : 'Success!'
											});								 
									 $scope.getLoyaltyCardList($scope.patientLoyaltiCardMapper.patientId);
									 $scope.getPatientDetailsById($scope.patientLoyaltiCardMapper.patientId);
									 $scope.initPaientLoyaltiCardMapper();
									
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							});
						}else{
							
							growl.warning("Loyalty card number should be 16 digit",
									{
										title : 'warning!!!!'
									});
						}
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
			}
			
			/**AutoTab ***/
			$("#ssn").bind("keypress", autotab);
			function autotab(e){
			    var element = e.target;
			    if(element.value.length == element.maxLength)
			    {
			         while((element = element.nextSibling))
			         {
			            if(element.tabIndex >= 0)
			            {
			                element.focus();
			                break;
			            }
			         }
			    }
			    }
			
			/**Crud Operations End**/
			
			$scope.printLoyaltyCard=function(loyatyCardObj)
			{
			var data = {
		         "appName": "HIS",
		         "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		         "reportDesignFilePath": "LoyaltyCard.rpt",
		         "reportFormat": "PDF",
		         "exportFileName": "LoyaltyCard",
		         "reportParams": [
		           {
			             "paramName": "@loyalty_card_id",
			             "paramType": "Integer",
			             "paramValue": loyatyCardObj.loyaltyCardId
		           }
		         ]
		     }
			reportService.generate(REPORT_API_URI1,data);
			}
			
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
						uhid: $scope.patientDetails.uhidNumber,
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
			
			$scope.mpesaPaymentRequest = function(){
				
				
				if($scope.validateMpesaAmount >  $scope.dueAmount){
					
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
								phoneNumber:$scope.mobileNumber,
								partyA:parseInt($scope.countryCallingCode+""+$scope.mobileNumber),
								amount:$scope.validateMpesaAmount
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
				
			console.log("$scope.patientDetails.uhidNumber",$scope.patientDetails.uhidNumber);
				var data ={
						organizationId :$scope.organizationId,
						unitId : $scope.unitId,
						uhidNumber: $scope.patientDetails.uhidNumber
						
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
	             
	             
//						/$scope.ipAddress+
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
							}else{
								 
								 $scope.pinpadAmounFlag = false;
								 growl.warning("Pin Pad Not Configure",{
										title : "Warning"
									});
								 $rootScope.stopLoaderPopup();
									return false;
							 }
						});
						
						$rootScope.stopLoaderPopup();
						}else {
							 $scope.pinpadAmounFlag = false;
								growl.error("Someting went wrong..!!",{
									title : ERROR_MSG
								});
								$rootScope.stopLoaderPopup();
							}
			});
			$rootScope.stopLoaderPopup();
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
			$scope.paymentDetails = angular.copy({paymentModeId:$scope.paymentDetails.paymentModeId,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
			$scope.calculateDueAmount();
		}
		
		$scope.paymentModes = {
				1: "Cash",
				2: "Cheque",
				3: "Credit Card",
				4: "E - Transfer",
				5: "Loyalty Card"	
		}
		$scope.setPaymentModeId=function(paymentModeId){
			if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
				$scope.addBtnId=true;
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
}]);