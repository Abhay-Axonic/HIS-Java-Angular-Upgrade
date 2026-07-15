angular.module("myApp").factory('pharmacyPaymentCollFactory', ['$rootScope', 'BillingGenericService', 'growl', function ($rootScope, BillingGenericService, growl) {
    /*var scope = angular.element(document.getElementById('uiView')).scope();*/
    var factory = {};

    factory.initPaymentDetailsObj = function (scope) {
        scope.paymentDetails = angular.copy({ paymentModeId: (scope.paymentDetails != "undefined" && scope.paymentDetails.paymentModeId != "undefined") ? scope.paymentDetails.paymentModeId : 1, cashAmount: "", bankId: "", chequeNumber: "", chequeDate: "", chequeAmount: "", credCardHolderName: "", credCardTypeId: "", credAmount: "", credCardNumber: "", credExpiryDate: "", debCardHolderName: "", debCardTypeId: "", debAmount: "", debCardNumber: "", debExpiryDate: "" ,cardBalAmount:scope.paymentDetails.cardBalAmount,loyaltyCardNo:scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
        /*scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:""});*/
    }

    factory.addPaymentDetails = function (scope) {
        switch (scope.paymentDetails.paymentModeId) {
    	case 1 :
			if(typeof(scope.paymentDetails.cashAmount) == "undefined" || scope.paymentDetails.cashAmount == "" || scope.paymentDetails.cashAmount == parseFloat(0) || scope.paymentDetails.cashAmount < parseFloat(0)){
				growl.error("Please insert an amount",{
					title : ERROR_MSG
				});
				return;
			}
			
			var cashLimit = 0;
			angular.forEach(scope.listPaymentDetails,function(value,key){
				
				if(value.paymentModeId == 1){
					cashLimit = parseFloat(cashLimit) + parseFloat(value.amount);
				}
			});
			cashLimit = cashLimit + parseFloat(scope.paymentDetails.cashAmount);
			if((scope.cashAmountLimit != undefined && scope.cashAmountLimit!= null ) && cashLimit >  scope.cashAmountLimit){
				growl.warning("System can not accept more than "+scope.cashAmountLimit +" in cash amount",{
					title : WARNING_MSG
				});
				return;
			}
			
			if(scope.isBillList == 0 || scope.isBillList != undefined){
				if(scope.transferToActiveDepositFact == undefined || scope.transferToActiveDepositFact == 'N'){
			if(parseFloat(scope.paymentDetails.cashAmount) <= parseFloat(scope.payeeDueAmtFinal))
			{
			  var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.cashAmount).toFixed(2),createdBy:scope.userId};
			   scope.listPaymentDetails.push(paymentDetails);
			 }
			else {
				growl.error("Entered amount should be equal to or less than self due amount",{
					title : ERROR_MSG
				});
				return 
			}
			}
				else
					{
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.cashAmount).toFixed(2),createdBy:scope.userId};
					   scope.listPaymentDetails.push(paymentDetails);
					}
			}
			else
				{
				 var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.cashAmount).toFixed(2),createdBy:scope.userId};
				   scope.listPaymentDetails.push(paymentDetails);
				}
			break;
		case 2 :
			/*if(typeof(scope.paymentDetails.chequeAmount) == "undefined" || scope.paymentDetails.chequeAmount == "" || scope.paymentDetails.bankId == 0 || scope.paymentDetails.chequeNumber == "" || typeof(scope.paymentDetails.chequeNumber) == "undefined" || typeof(scope.paymentDetails.chequeDate) == "undefined" || scope.paymentDetails.chequeDate == "" || scope.paymentDetails.chequeAmount == parseFloat(0) || scope.paymentDetails.chequeAmount < parseFloat(0) || scope.paymentDetails.hospitalAccountId == 0){
				growl.error("Please select bank,hospital account, chequeno, cheque date and amount",{
					title : ERROR_MSG
				});
				return;
			}*/
			
			if(typeof(scope.paymentDetails.chequeAmount) == "undefined" || scope.paymentDetails.chequeAmount == ""  || scope.paymentDetails.chequeAmount == parseFloat(0) || scope.paymentDetails.chequeAmount < parseFloat(0)){
				growl.error("Please enter amount",{
					title : ERROR_MSG
				});
				return;
			}
			
			var chequeNo = false;
			angular.forEach(scope.listPaymentDetails,function(value,key){
				if(value.paymentModeId == 2 && value.chequeNumber == scope.paymentDetails.chequeNumber){
					chequeNo = true;
				}
			});
			if(chequeNo){
				growl.warning("this cheque number already  added",{
					title : WARNING_MSG
				});
				return;
			}
			
			
			var bankName = "";
			//Get Bank Name
			angular.forEach(scope.bankList,function(value,key){
				if(scope.paymentDetails.bankId == value.bankId)
					bankName = value.bankName;
			})
			if(scope.isBillList == 0 || scope.isBillList != undefined){
				if(scope.transferToActiveDepositFact == undefined || scope.transferToActiveDepositFact == 'N'){
			if(parseFloat(scope.paymentDetails.chequeAmount) <= parseFloat(scope.payeeDueAmtFinal))
			{
				
				if(scope.paymentDetails.chequeDate !=undefined  && scope.paymentDetails.chequeDate != ""){
					var chequeDate = $rootScope.getFormattedDate(scope.paymentDetails.chequeDate,false);
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.chequeAmount).toFixed(2),createdBy:scope.userId, bankId:scope.paymentDetails.bankId, chequeNumber:scope.paymentDetails.chequeNumber, chequeDate:chequeDate,bankName:bankName,hospitalAccountId:scope.paymentDetails.hospitalAccountId};//, bankName:bankName
					scope.listPaymentDetails.push(paymentDetails);
				}else{
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.chequeAmount).toFixed(2),createdBy:scope.userId, bankId:scope.paymentDetails.bankId, chequeNumber:scope.paymentDetails.chequeNumber,bankName:bankName,hospitalAccountId:scope.paymentDetails.hospitalAccountId};//, bankName:bankName
					scope.listPaymentDetails.push(paymentDetails);
				}
				
				
			
			}
				else {
					growl.error("Entered amount should be equal to or less than self due amount",{
						title : ERROR_MSG
					});
					return 
				}
				
			}
			else
				{
				if(scope.paymentDetails.chequeDate !=undefined  && scope.paymentDetails.chequeDate != ""){
					var chequeDate = $rootScope.getFormattedDate(scope.paymentDetails.chequeDate,false);
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.chequeAmount).toFixed(2),createdBy:scope.userId, bankId:scope.paymentDetails.bankId, chequeNumber:scope.paymentDetails.chequeNumber, chequeDate:chequeDate,bankName:bankName,hospitalAccountId:scope.paymentDetails.hospitalAccountId};//, bankName:bankName
					scope.listPaymentDetails.push(paymentDetails);
				}else{
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.chequeAmount).toFixed(2),createdBy:scope.userId, bankId:scope.paymentDetails.bankId, chequeNumber:scope.paymentDetails.chequeNumber,bankName:bankName,hospitalAccountId:scope.paymentDetails.hospitalAccountId};//, bankName:bankName
					scope.listPaymentDetails.push(paymentDetails);
				}
				
				}
			}
			else
				{
				if(scope.paymentDetails.chequeDate !=undefined  && scope.paymentDetails.chequeDate != ""){
					var chequeDate = $rootScope.getFormattedDate(scope.paymentDetails.chequeDate,false);
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.chequeAmount).toFixed(2),createdBy:scope.userId, bankId:scope.paymentDetails.bankId, chequeNumber:scope.paymentDetails.chequeNumber, chequeDate:chequeDate,bankName:bankName,hospitalAccountId:scope.paymentDetails.hospitalAccountId};//, bankName:bankName
					scope.listPaymentDetails.push(paymentDetails);
				}else{
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.chequeAmount).toFixed(2),createdBy:scope.userId, bankId:scope.paymentDetails.bankId, chequeNumber:scope.paymentDetails.chequeNumber,bankName:bankName,hospitalAccountId:scope.paymentDetails.hospitalAccountId};//, bankName:bankName
					scope.listPaymentDetails.push(paymentDetails);
				}
				}
			break;
		case 3 :
			/*if(
				typeof(scope.paymentDetails.credCardHolderName) == "undefined" || 
				scope.paymentDetails.credCardHolderName == "" ){
				growl.error("Please enter card holder name",{
					title : ERROR_MSG
				});
				return;
			}
			
			if(typeof(scope.paymentDetails.credCardTypeId) == "undefined" ||
					scope.paymentDetails.credCardTypeId == "" ){
					growl.error("Please enter card type",{
						title : ERROR_MSG
					});
					return;
				}*/
			
			if(typeof(scope.paymentDetails.credCardNumber) == "undefined" || 
					scope.paymentDetails.credCardNumber == ""){
					growl.error("Please enter card number",{
						title : ERROR_MSG
					});
					return;
				}
			if(typeof(scope.paymentDetails.transactionNo) == "undefined" || scope.paymentDetails.transactionNo == null || 
					scope.paymentDetails.transactionNo == "" ){
					growl.error("Please enter transaction no",{
						title : ERROR_MSG
					});
					return;
				}
			/*if(
					typeof(scope.paymentDetails.credExpiryDate) == "undefined" || 
					scope.paymentDetails.credExpiryDate == "" ){
					growl.error("Please enter expiry date",{
						title : ERROR_MSG
					});
					return;
				}
			*/
			if(typeof(scope.paymentDetails.credAmount) == "undefined" || scope.paymentDetails.credAmount == ""
				|| scope.paymentDetails.credAmount == parseFloat(0)  || scope.paymentDetails.credAmount < parseFloat(0)){
				growl.error("Please enter card amount",{
					title : ERROR_MSG
				});
				return;
			}
			if(scope.isBillList == 0 || scope.isBillList != undefined){
				if(scope.transferToActiveDepositFact == undefined || scope.transferToActiveDepositFact == 'N'){
			if(parseFloat(scope.paymentDetails.credAmount) <= parseFloat(scope.payeeDueAmtFinal))
			{
				
				if(scope.paymentDetails.credExpiryDate !=undefined  && scope.paymentDetails.credExpiryDate != ""){
					var expiryDate =  $rootScope.getFormattedDate(scope.paymentDetails.credExpiryDate,false);
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.credAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.credCardHolderName,cardTypeId:scope.paymentDetails.credCardTypeId,cardNumber:scope.paymentDetails.credCardNumber,cardNo:scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:scope.paymentDetails.transactionNo,skipMpesaValidation:scope.pinpadAmounFlag!=undefined ? scope.pinpadAmounFlag : null,deviceName : scope.objPosMachine.deviceName};
					scope.listPaymentDetails.push(paymentDetails);
				}else{
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.credAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.credCardHolderName,cardTypeId:scope.paymentDetails.credCardTypeId,cardNumber:scope.paymentDetails.credCardNumber,cardNo:scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:scope.paymentDetails.transactionNo,skipMpesaValidation:scope.pinpadAmounFlag!=undefined ? scope.pinpadAmounFlag : null,deviceName : scope.objPosMachine.deviceName};
					scope.listPaymentDetails.push(paymentDetails);
					scope.clearCardFields();
				}
				
			}
			else {
				growl.error("Entered amount should be equal to or less than self due amount",{
					title : ERROR_MSG
				});
				return 
			}
			
			}
			else
				{
				if(scope.paymentDetails.credExpiryDate !=undefined  && scope.paymentDetails.credExpiryDate != ""){
					var expiryDate =  $rootScope.getFormattedDate(scope.paymentDetails.credExpiryDate,false);
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.credAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.credCardHolderName,cardTypeId:scope.paymentDetails.credCardTypeId,cardNumber:scope.paymentDetails.credCardNumber,cardNo:scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:scope.paymentDetails.transactionNo};
					scope.listPaymentDetails.push(paymentDetails);
				}else{
                    var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.credAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.credCardHolderName,cardTypeId:scope.paymentDetails.credCardTypeId,cardNumber:scope.paymentDetails.credCardNumber,cardNo:scope.paymentDetails.credCardNumber,transactionNo:scope.paymentDetails.transactionNo};
                    scope.listPaymentDetails.push(paymentDetails);
				}
				
				}
			}
			else
				{
				if(scope.paymentDetails.credExpiryDate !=undefined  && scope.paymentDetails.credExpiryDate != ""){
					var expiryDate =  $rootScope.getFormattedDate(scope.paymentDetails.credExpiryDate,false);
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.credAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.credCardHolderName,cardTypeId:scope.paymentDetails.credCardTypeId,cardNumber:scope.paymentDetails.credCardNumber,cardNo:scope.paymentDetails.credCardNumber,expiryDate:expiryDate};
					scope.listPaymentDetails.push(paymentDetails);
				}else{
                    var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.credAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.credCardHolderName,cardTypeId:scope.paymentDetails.credCardTypeId,cardNumber:scope.paymentDetails.credCardNumber,cardNo:scope.paymentDetails.credCardNumber};
                    scope.listPaymentDetails.push(paymentDetails);
				}
				
				}
			break;
		case 4 :
			if(typeof(scope.paymentDetails.debAmount) == "undefined" || scope.paymentDetails.debAmount == "" || typeof(scope.paymentDetails.debCardHolderName) == "undefined" || scope.paymentDetails.debCardHolderName == "" || typeof(scope.paymentDetails.debCardTypeId) == "undefined" || scope.paymentDetails.debCardTypeId == "" || typeof(scope.paymentDetails.debCardNumber) == "undefined" || scope.paymentDetails.debCardNumber == "" || typeof(scope.paymentDetails.debExpiryDate) == "undefined" || scope.paymentDetails.debExpiryDate == "" || scope.paymentDetails.debAmount == parseFloat(0) || scope.paymentDetails.debAmount < parseFloat(0)){
				growl.error("Please insert card holder name, card type, amount,expiry date and card number",{
					title : ERROR_MSG
				});
				return;
			}
			if(scope.isBillList == 0 || scope.isBillList != undefined){
				if(scope.transferToActiveDepositFact == undefined || scope.transferToActiveDepositFact == 'N'){
			if(parseFloat(scope.paymentDetails.debAmount) <= parseFloat(scope.payeeDueAmtFinal))
			{
			var expiryDate =  $rootScope.getFormattedDate(scope.paymentDetails.debExpiryDate,false);
			var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.debAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.debCardHolderName,cardTypeId:scope.paymentDetails.debCardTypeId,cardNumber:scope.paymentDetails.debCardNumber,cardNo:scope.paymentDetails.debCardNumber,expiryDate:expiryDate};
			scope.listPaymentDetails.push(paymentDetails);
			}
			else {
				growl.error("Entered amount should be equal to or less than self due amount",{
					title : ERROR_MSG
				});
				return 
			}
			}
			else
				{
				var expiryDate =  $rootScope.getFormattedDate(scope.paymentDetails.debExpiryDate,false);
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.debAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.debCardHolderName,cardTypeId:scope.paymentDetails.debCardTypeId,cardNumber:scope.paymentDetails.debCardNumber,cardNo:scope.paymentDetails.debCardNumber,expiryDate:expiryDate};
				scope.listPaymentDetails.push(paymentDetails);
				}
			}
			else
				{
				var expiryDate =  $rootScope.getFormattedDate(scope.paymentDetails.debExpiryDate,false);
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.debAmount).toFixed(2),createdBy:scope.userId, cardHolderName:scope.paymentDetails.debCardHolderName,cardTypeId:scope.paymentDetails.debCardTypeId,cardNumber:scope.paymentDetails.debCardNumber,cardNo:scope.paymentDetails.debCardNumber,expiryDate:expiryDate};
				scope.listPaymentDetails.push(paymentDetails);
				}
			break;
		case 7 :
			if(typeof(scope.paymentDetails.transferAmount) == "undefined" || scope.paymentDetails.transferAmount == "" || typeof(scope.paymentDetails.transactionNo) == "undefined" || scope.paymentDetails.transactionNo == "" || typeof(scope.paymentDetails.transBankId) == "undefined" || scope.paymentDetails.transBankId == "" || typeof(scope.paymentDetails.transactionTypeId) == "undefined" || scope.paymentDetails.transactionTypeId == ""){
				growl.error("Please insert transaction type, bank, transaction no and amount",{
					title : ERROR_MSG
				});
				return;
			}
			var bankName = "";
			//Get Bank Name
			angular.forEach(scope.bankList,function(value,key){
				if(scope.paymentDetails.transBankId == value.bankId)
					bankName = value.bankName;
			})
			var transactionType = "";
			scope.transactionTypes.forEach((elem) => {if(elem.id == scope.paymentDetails.transactionTypeId){transactionType = elem.name}})
			var transactionDate = $rootScope.getFormattedDate(scope.paymentDetails.transTransactionDate,false);
			var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.transferAmount).toFixed(2),createdBy:scope.userId, transactionTypeId:scope.paymentDetails.transactionTypeId,bankId:scope.paymentDetails.transBankId, transactionNo:scope.paymentDetails.transactionNo, transTransactionDate:transactionDate, transactionType:transactionType,bankName:bankName};
			scope.listPaymentDetails.push(paymentDetails);
			break;
		case 6 :
			if(typeof(scope.paymentDetails.loyaltyCardAmt) == "undefined" || scope.paymentDetails.loyaltyCardAmt == ""){
				growl.error("Please insert an amount",{
					title : ERROR_MSG
				});
				return;
			}
			if(scope.isBillList == 0 || scope.isBillList != undefined)
			{
				
				if(parseFloat(scope.paymentDetails.loyaltyCardAmt) > parseFloat(scope.paymentDetails.cardBalAmount))
				{
					growl.error("Entered Amount Should be less than Card Amount.",{
						title : ERROR_MSG
					});
					return 
				 }
				if(parseFloat(scope.paymentDetails.loyaltyCardAmt) <= parseFloat(scope.payeeDueAmtFinal))
			 {
			  var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.loyaltyCardAmt).toFixed(2),cardNumber:scope.paymentDetails.loyaltyCardNo,createdBy:scope.userId};
			  scope.listPaymentDetails.push(paymentDetails);
			 }
			else {
				growl.error("Entered amount should be equal to or less than self due amount",{
					title : ERROR_MSG
				});
				return 
			 }
			
			}
			else
				{
				 var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.paymentDetails.loyaltyCardAmt).toFixed(2),cardNumber:scope.paymentDetails.loyaltyCardNo,createdBy:scope.userId};
				  scope.listPaymentDetails.push(paymentDetails);
				}
			break;
			
		case 5:
			if(scope.mpesaObject.resTransactionId != undefined && scope.mpesaObject.resTransactionId != null){
				
				if(scope.mpesaObject.resTransactionId!=scope.mpesaObject.transactionId){
					growl.warning("Invalid Transaction Id",{
						title : WARNING_MSG
					});
					scope.mpesaObject.mpesaAmount=null;
					return false;	
				}
			}
			
			if(scope.mpesaObject.transactionId == null){
				growl.warning("Please Enter Transaction Id !!",{
					title : WARNING_MSG
				});
				return false;
			}
			
			if(scope.mpesaObject.mobileNo == null){
				growl.warning("Please Enter Mobile Number !!!",{
					title : WARNING_MSG
				});
				return false;
			}
			
			
			
			if(scope.mpesaObject.mpesaAmount == 0 || scope.mpesaObject.mpesaAmount == null){
				growl.warning("Amount cannot be zero",{
					title : WARNING_MSG
				});
				return false;	
			}
			var invalidMpesa = false;
			angular.forEach(scope.listPaymentDetails,function(value,key){
				if(scope.mpesaObject.transactionId == value.cardNo)
				{
					growl.warning("Collected MPesa is already used in current tranasction",{
						title : WARNING_MSG
					});
					invalidMpesa = true;
				}
			});
			if(invalidMpesa == true)
				return false;	
			
			if(scope.isBillList == 0 || scope.isBillList != undefined){
				if(scope.transferToActiveDepositFact == undefined || scope.transferToActiveDepositFact == 'N'){
			if(parseFloat(scope.mpesaObject.mpesaAmount) <= parseFloat(scope.payeeDueAmtFinal))
			{
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:scope.mpesaObject.transactionId,transactionNo:scope.mpesaObject.transactionId,createdBy:scope.userId,skipMpesaValidation:scope.skipMpesaValidation};
				   scope.listPaymentDetails.push(paymentDetails);
			 }
			else {
				growl.error("Entered amount should be equal to or less than self due amount",{
					title : ERROR_MSG
				});
				return false;	
			}
			}
				else
					{
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:scope.mpesaObject.transactionId,transactionNo:scope.mpesaObject.transactionId,createdBy:scope.userId,skipMpesaValidation:scope.skipMpesaValidation};
					   scope.listPaymentDetails.push(paymentDetails);
					}
			}else
			{
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:scope.mpesaObject.transactionId,transactionNo:scope.mpesaObject.transactionId,createdBy:scope.userId,skipMpesaValidation:scope.skipMpesaValidation};
				   scope.listPaymentDetails.push(paymentDetails);
				}
			scope.mpesaObject={};
			break;
			
		case 8:
			if(scope.eTransferObj.transactionobj.id == ""){
				growl.warning("Please Select Transaction Type",{
					title : WARNING_MSG
				});
				return;	
			}
			/*if(scope.eTransferObj.transferAmount == 0 || scope.eTransferObj.transferAmount == null){
				growl.warning("Amount cannot be zero",{
					title : WARNING_MSG
				});
				return;
			}
			if(scope.eTransferObj.transBankId == "" || scope.eTransferObj.transBankId == "-" || scope.eTransferObj.transBankId == undefined  || scope.eTransferObj.transBankId == null){
				growl.warning("Please Select Bank",{
					title : WARNING_MSG
				});
				return;	
			}
			if(scope.eTransferObj.transactionNo == "" || scope.eTransferObj.transactionNo == undefined){
				growl.warning("Please Enter Transaction Number",{
					title : WARNING_MSG
				});
				return;	
			}*/
			if(scope.eTransferObj.transTransactionDate == ""){
				growl.warning("Please Select Transaction Date",{
					title : WARNING_MSG
				});
				return;	
			}
			var invalidETransfer = false;
			angular.forEach(scope.listPaymentDetails,function(value,key){
				if(value.paymentModeId == 8 && scope.eTransferObj.transactionNo == value.transactionNo)
				{
					growl.warning("Collected E-Transfer is already used in current tranasction",{
						title : WARNING_MSG
					});
					invalidETransfer = true;
				}
			});
			if(invalidETransfer == true)
				return;
			
			var bankName = "";
			
			//Get Bank Name
			angular.forEach(scope.bankList,function(value,key){
				if(scope.eTransferObj.transBankId == value.bankId)
					bankName = value.bankName;
			})
			
			
			if(scope.isBillList == 0 || scope.isBillList != undefined){
				if(scope.transferToActiveDepositFact == undefined || scope.transferToActiveDepositFact == 'N'){
			if(parseFloat(scope.eTransferObj.transferAmount) <= parseFloat(scope.payeeDueAmtFinal))
			{
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.eTransferObj.transferAmount).toFixed(2), transactionType:scope.eTransferObj.transactionobj.name, transactionTypeId:scope.eTransferObj.transactionobj.id,transTransactionDate:moment(scope.eTransferObj.transTransactionDate).format('DD-MM-YYYY HH:mm:ss'),bankId:scope.eTransferObj.transBankId,transactionNo:scope.eTransferObj.transactionNo,createdBy:scope.userId,bankName:bankName};
				   scope.listPaymentDetails.push(paymentDetails);
			 }
			else {
				growl.error("Entered amount should be equal to or less than self due amount",{
					title : ERROR_MSG
				});
				return 
			}
			}
				else
					{
					var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.eTransferObj.transferAmount).toFixed(2), transactionType:scope.eTransferObj.transactionobj.name, transactionTypeId:scope.eTransferObj.transactionobj.id,transTransactionDate:moment(scope.eTransferObj.transTransactionDate).format('DD-MM-YYYY HH:mm:ss'),bankId:scope.eTransferObj.transBankId,transactionNo:scope.eTransferObj.transactionNo,createdBy:scope.userId,bankName:bankName};
					  scope.listPaymentDetails.push(paymentDetails);
					}
			}else
			{
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.eTransferObj.transferAmount).toFixed(2), transactionType:scope.eTransferObj.transactionobj.name, transactionTypeId:scope.eTransferObj.transactionobj.id,transTransactionDate:moment(scope.eTransferObj.transTransactionDate).format('DD-MM-YYYY HH:mm:ss'),bankId:scope.eTransferObj.transBankId,transactionNo:scope.eTransferObj.transactionNo,createdBy:scope.userId,bankName:bankName};
				  scope.listPaymentDetails.push(paymentDetails);
			}
			scope.eTransferObj={};
			break;
			
			
		case 12 :
			if(typeof(scope.razorPayObject.razorPaidAmount) == "undefined" || scope.razorPayObject.razorPaidAmount == "" || scope.razorPayObject.razorPaidAmount == parseFloat(0) || scope.razorPayObject.razorPaidAmount < parseFloat(0)){
				growl.warning("Please enter amount..!!",{
					title : WARNING_MSG
				});
				return;
			}
			
			var trancastionId = false;
			angular.forEach(scope.listPaymentDetails,function(value,key){
				
				if(value.paymentModeId == 12 && value.transactionNo == scope.razorPayObject.razorUniqueId){
					trancastionId =true;
				}
			});
			
			if(trancastionId){
				growl.warning("Trancastion id should not be same",{
					title : WARNING_MSG
				});
				return 
			}
			
			if(scope.isBillList == 0 || scope.isBillList != undefined){
				if(scope.transferToActiveDepositFact == undefined || scope.transferToActiveDepositFact == 'N'){
			if(parseFloat(scope.razorPayObject.razorAmount) <= parseFloat(scope.payeeDueAmtFinal))
			{
			var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.razorPayObject.razorPaidAmount).toFixed(2),createdBy:scope.userId,transactionNo:scope.razorPayObject.razorUniqueId,paymentGatewayProviderId:scope.payGatewayProviderId};
			scope.listPaymentDetails.push(paymentDetails);
			}
			else {
				growl.warning("Entered amount should be equal to or less than self due amount",{
					title : WARNING_MSG
				});
				return 
			}
			}
			else
				{
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.razorPayObject.razorPaidAmount).toFixed(2),createdBy:scope.userId,transactionNo:scope.razorPayObject.razorUniqueId,paymentGatewayProviderId:scope.payGatewayProviderId};
				scope.listPaymentDetails.push(paymentDetails);
				}
			}
			else
				{
				var paymentDetails = {paymentModeId:scope.paymentDetails.paymentModeId, amount:parseFloat(scope.razorPayObject.razorPaidAmount).toFixed(2),createdBy:scope.userId,transactionNo:scope.razorPayObject.razorUniqueId,paymentGatewayProviderId:scope.payGatewayProviderId};
				scope.listPaymentDetails.push(paymentDetails);
				}
			break;
			
			
        }
        factory.initPaymentDetailsObj(angular.element(document.getElementById("uiView")).scope());

        //calculate due amount by page
        switch (scope.page) {
            case "billList":
                scope.calculateTotalDueRefAmount();
                break;
            case "companyInvoiceSettlement":
                scope.validCollectedAmount(scope.listPaymentDetails.length - 1);
                break
        }

    }

    factory.removePaymentDetails = function (scope, index) {
    	
    	var tempObj = (index==undefined)?null:scope.listPaymentDetails[index];
		if(tempObj!=null && (tempObj.cardNumber == scope.paymentDetails.loyaltyCardNo))
			{
			 scope.paymentDetails.cardBalAmount = scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
			 scope.send=false;
			 scope.resend=false;
			}
        scope.listPaymentDetails.splice(index, 1);

        //calculate due amount by page
        switch (scope.page) {
            case "billList":
                scope.calculateTotalDueRefAmount();
                break;
        }
    }

    return factory;
}]);