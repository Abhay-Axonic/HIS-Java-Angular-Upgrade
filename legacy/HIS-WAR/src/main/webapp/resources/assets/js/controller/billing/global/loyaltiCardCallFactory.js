angular.module("myApp").factory('loyaltiCardCallFactory',['$rootScope','BillingGenericService','growl','paymentCollFactory',
	function($rootScope,BillingGenericService,growl,paymentCollFactory){
	var loyFactory = {};

	loyFactory.initPaymentDetailsObj = function(scope){
		
		scope.paymentDetails = angular.copy({cardBalAmount:"",loyaltyCardNo:"",loyaltyCardAmt:""});
		
		scope.loyaltyCardObj = angular.copy({loyaltyCardBalance:"",loyaltyCardNo:""});
	}
	
	/*** loyalty Card****========================================================================*/
	loyFactory.getLoyaltyCardList = function(patientId,scope){
		var data = {
				"patientId" : patientId,
				  "organisationId" : scope.organizationId,
				  "unitId" : scope.unitId,
				  "status" : "A",
				  "isDeleted" : "N",
				  "isUserDefined":"Y"
		}
		var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getLoyaltiCardDetailsByPatientId";
		BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
				scope.send =false;
				scope.resend =false;
				if(response.data.listObject!=null && response.data.listObject.length>0){
					scope.loyaltyCardObj = response.data.listObject[0];					
					scope.paymentDetails.cardBalAmount =  scope.loyaltyCardObj.cardAmount;
					scope.paymentDetails.loyaltyCardNo = scope.loyaltyCardObj.cardNo;
				}else{
					scope.paymentDetails.cardBalAmount = "";
					scope.paymentDetails.loyaltyCardNo = "";
				}
				
			}
		});
		
		console.log("factory.loyaltyCardObj",scope.loyaltyCardObj);
		console.log("scope.paymentDetails",scope.paymentDetails);
		console.log("scope.scope",scope);
	}
	
	loyFactory.checkAmt = function(amt,scope)
	{
		if(isNaN(Number(parseFloat(amt))))
			{
			 scope.paymentDetails.loyaltyCardAmt= 0.00;
			}
		else{
			
			  if(Number(scope.paymentDetails.loyaltyCardAmt)>Number(scope.paymentDetails.cardBalAmount))
				  {
				  scope.paymentDetails.loyaltyCardAmt = 0;
				  growl.error("Amount should be less than card Amount.",
							{
								title : 'Error!'
							});
					return false;
				  }
			  if(Number(scope.paymentDetails.loyaltyCardAmt)>Number(scope.dueAmount))
			  {
			  scope.paymentDetails.loyaltyCardAmt = 0;
			  growl.error("Amount should be less than due Amount.",
						{
							title : 'Error!'
						});
				return false;
			  }
			  else{
				  scope.paymentDetails.loyaltyCardAmt= amt;
			  }
			
		   }
	}

	loyFactory.getLoyaltyCardByNo = function(cardNo,scope)
	{
		if(cardNo!="" && cardNo.toString().length==16)
			{
			var data = {
					"patientId" : '',
					  "organisationId" : scope.organizationId,
					  "unitId" : scope.unitId,
					  "status" : "A",
					  "isDeleted" : "N",
					  "isUserDefined":"Y",
					  "cardNo":cardNo
			}
			var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getPatientDetailsByLoyaltiCardNo";
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				{
					if(response.data.listObject!=null && response.data.listObject.length>0)
					{
						scope.send =false;
						scope.resend =false;
						var cardAmtToSub = 0;
						scope.loyaltyCardObj = response.data.listObject[0];
					    scope.paymentDetails.loyaltyCardNo = scope.loyaltyCardObj.cardNo;
					    if(scope.listPaymentDetails!=null && scope.listPaymentDetails.length>0)
					    	{
					    	   angular.forEach(scope.listPaymentDetails,function(value,key)
					    	    {
					    		   if(value.paymentModeId==6 && value.cardNumber==cardNo)
					    			   {
					    			     cardAmtToSub = cardAmtToSub + parseFloat(value.amount); 
					    			   }
									
								});
					    	}
					     scope.paymentDetails.cardBalAmount =  scope.loyaltyCardObj.cardAmount-cardAmtToSub;
					}
				}
			});
			}else{
				 scope.paymentDetails.cardBalAmount =  parseFloat();
					 growl.error("Please enter valid card number",
								{
									title : 'Error!'
								});
			}

	}

	loyFactory.generateOtp = function(flag,scope)
	{
		if(scope.paymentDetails.cardBalAmount==0||scope.paymentDetails.cardBalAmount=='')
			{
			growl.error("Please Recharge Your card.",
					{
						title : 'Error!'
					});
			return false;
               	}
		if(scope.paymentDetails.loyaltyCardAmt==0||scope.paymentDetails.loyaltyCardAmt==''||scope.paymentDetails.loyaltyCardAmt==undefined||
				scope.paymentDetails.loyaltyCardAmt==null)
			{
			growl.error("Amount Should be greater than zero.",
					{
						title : 'Error!'
					});
			    return false;
			 }
		
		if(Number(scope.dueAmount)==0)
			{
			growl.error("Due Amount Should be greater than 0...",
					{
						title : 'Error!'
					});
			    return false;
			 }
		if(Number(scope.paymentDetails.loyaltyCardAmt) > scope.dueAmount)
		{
			growl.error("Due Amount Should be greater than entered amount...",
					{
						title : 'Error!'
					});
			    return false;
		 }
		if(parseFloat(scope.paymentDetails.loyaltyCardAmt)>parseFloat(scope.paymentDetails.cardBalAmount))
			{
			growl.error("Insufficient fund in your account.",
					{
						title : 'Error!'
					});
			    return false;
			 }
		if(scope.loyaltyCardObj.mobileNumber==''||scope.loyaltyCardObj.mobileNumber==undefined||
				scope.loyaltyCardObj.mobileNumber==null)
			{
			growl.error("Mobile number is not Found..",
					{
						title : 'Error!'
					});
			    return false;
			 }
		
		
			if(scope.paymentDetails.loyaltyCardNo!="" && scope.paymentDetails.loyaltyCardNo.toString().length==16)
				{
				
				scope.send =true;
				var data = {
						  "patientId" : '',
						  "organisationId" : scope.organizationId,
						  "unitId" : scope.unitId,
						  "status" : "A",
						  "isDeleted" : "N",
						  "isUserDefined":"Y",
						  "cardNo":scope.paymentDetails.loyaltyCardNo,
						  "mobileNumber":scope.loyaltyCardObj.mobileNumber,
						  "patientLoyaltyCardMpprId":scope.loyaltyCardObj.patientLoyaltyCardMpprId,
						  "resendOTP" :flag,
						  "email":scope.loyaltyCardObj.email,
						  "firstName":scope.loyaltyCardObj.firstName,
						  "consumeAmount":scope.paymentDetails.loyaltyCardAmt,
						  "createdBy" : scope.userId,
						  "createdDate" : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						  "updatedBy" : scope.userId,
						  "updatedDate" :moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
						
				}
				var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/generateOTP";
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						scope.loyaltyCardObj.authKey =  response.data.object;
						scope.validatTextFieldFlag = false;
							growl.success(response.data.message,
									{
								
										title : 'Success!'
									});
						} else if(response.data.status == 'error')
						{
							scope.send =false;
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
					}
				});
				}

	}
	
	loyFactory.sendOTP = function(flag,scope)
	{
		
		if(Number(scope.paymentDetails.cardBalAmount)==0||scope.paymentDetails.cardBalAmount=='')
			{
			growl.error("Card Amount Should be Greater than 0..",
					{
						title : 'Error!'
					});
			  return false;
			}
		
		if(flag=='N')
			{
			 //scope.resend = (flag=='N')?true:false;
			 loyFactory.generateOtp(flag,scope);
			}
		else if(flag=='Y')
			{
			loyFactory.generateOtp(flag,scope);
			}
	}
	
	loyFactory.validateOtp = function(scope)
	{
		
		if(scope.paymentDetails.otp==0||scope.paymentDetails.otp==''||scope.paymentDetails.otp==undefined||
				scope.paymentDetails.otp==null)
			{
			growl.error("Please enter valid OTP",
					{
						title : 'Error!'
					});
			    return false;
			 }
		else{
	
			var data = {
					  "patientId" : '',
					  "organisationId" : scope.organizationId,
					  "unitId" : scope.unitId,
					  "status" : "A",
					  "isDeleted" : "N",
					  "consumeAmount":scope.paymentDetails.loyaltyCardAmt,
					  "isUserDefined":"Y",
					  "otp": scope.paymentDetails.otp,
					  "cardNo":scope.paymentDetails.loyaltyCardNo,
					  "patientLoyaltyCardMpprId":scope.loyaltyCardObj.patientLoyaltyCardMpprId,
					  "authKey":scope.loyaltyCardObj.authKey
			}
			var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/validateOTP";
			BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				{
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
							scope.isBillList = 0;
							scope.loyaltyCardObj.authKey = 0;
							var paymentObj = angular.copy(scope.paymentDetails);
							paymentCollFactory.addPaymentDetails(scope);
							scope.paymentDetails.loyaltyCardNo = scope.loyaltyCardObj.cardNo;
							scope.paymentDetails.cardBalAmount = paymentObj.cardBalAmount - parseFloat(paymentObj.loyaltyCardAmt);
							scope.send =false;
							scope.resend =false;
							scope.validatTextFieldFlag = true;
						
					} else if(response.data.status == 'error')
					{
					
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
				}
	
			});
		}
	}
	

	/*====================================================================================*/
	
	return loyFactory;
}]);