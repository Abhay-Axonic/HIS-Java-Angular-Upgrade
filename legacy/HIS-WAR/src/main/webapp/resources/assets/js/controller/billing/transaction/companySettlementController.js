(function(){
	'use strict';
	
	function companySettlementController($scope, $rootScope, BillingGenericService, growl, $cookies, $state,$timeout, paymentCollFactory){
		var self = this;
		
		function init(){
			$rootScope.loginpage = true;
			self.model = {
				fromDateOptions: {
		          formatYear: 'yyyy',
		          showWeeks: false,
		          maxDate: new Date()
		        },
		        toDateOptions: {
		          formatYear: 'yyyy',
		          showWeeks: false,
		          maxDate: new Date()
		        }};
			
			$scope.depositeList = {
				    depositeIds: []
				  };
			
			$scope.consumedAmt = parseFloat(0);
			
			var cookieObject = {}; var cookieObjectAppConfig = {};
			
			[cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}
			
			$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
			$scope.mpesaRequired = cookieObject.mpesaRequired;
			self.model.unitId = cookieObject.unitId;
			self.model.orgId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.searchObj = {fromDate:new Date(),toDate:new Date(),orgId:self.model.orgId,unitId:self.model.unitId,companyId:""};
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.getCompaniesByCompanyType = getCompaniesByCompanyType;
			self.model.getInsuranceByCompanyId = getInsuranceByCompanyId;
			self.model.getCompanyInvoiceListByFilter = getCompanyInvoiceListByFilter;
			self.model.getCompanyInvoiceListByFilterNew = getCompanyInvoiceListByFilterNew;
			self.model.getCompanyInvoiceDetailsByInvoiceId = getCompanyInvoiceDetailsByInvoiceId;
			self.model.calculateSummaryAndValidateForTDS = calculateSummaryAndValidateForTDS;
			self.model.calculateSummaryAndValidateForOD = calculateSummaryAndValidateForOD;
			self.model.calculateSummaryAndValidateForDA = calculateSummaryAndValidateForDA;
			self.model.calculateSummaryAndValidateForSDS = calculateSummaryAndValidateForSDS;
			self.model.calculateSummaryAndValidateForRecive = calculateSummaryAndValidateForRecive;
			self.model.calculateSummary = calculateSummary;
			self.model.SelectAllItems = SelectAllItems;
			self.model.settleCompanyInvoice = settleCompanyInvoice;
			self.model.getCompanyInvoiceServiceDetails = getCompanyInvoiceServiceDetails;
			self.model.saveServiceInvoiceDetails = saveServiceInvoiceDetails;
			self.model.clrServiceInvoiceDetails = clrServiceInvoiceDetails;
			self.model.saveServiceInvoiceDetailsOutSide = saveServiceInvoiceDetailsOutSide;
			self.model.getTransactionTypes = getTransactionTypes;
			self.model.getHospitalBankList = getHospitalBankList;
			self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(0),tds:$rootScope.parseFloatNum(0),otherDeduction:$rootScope.parseFloatNum(0),
												disallowedAmount:$rootScope.parseFloatNum(0),securityDepositSettlement:$rootScope.parseFloatNum(0),total:$rootScope.parseFloatNum(0)};
			self.model.invoiceDetails = [];
			self.model.companyIvoiceServiceDetails = [];
			self.model.validateReceiveAmount = validateReceiveAmount;
			self.model.validateDisallAmount = validateDisallAmount;
			self.model.validateTDSAmount = validateTDSAmount;
			self.model.validateODAmount = validateODAmount;
			
			self.model.recvAndDisallAmountDetails =[];
			self.model.currentInvoiceIndex = null;
			$scope.btnSettleDisabled = false;
			$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
			self.model.recvAndDisallAmountDetailsFinal = [];
			
			$scope.settleFlag = false;
			
			// added for payment collection
			$scope.dueAmount=parseFloat(0).toFixed(2);
			$scope.listPaymentDetails = [];
			$scope.companySettlement = true;
			$scope.paymentModes = {1: "Cash", 2: "Cheque", 3: "Card", 4: "Debit Card", 5 : "Mpesa" , 8:"E-Transfer"};
			$scope.paymentDetails={"paymentRecieptDetailsId":"","paymentModeId":2,"paymentModeName":"Cash","amount":"","bankId":"","bankName":"",
									"accountNumber":"","checkNumber":"","cardNo":"","cardHolderName":"","dateOfTransaction":new Date().getTime(),
									"expiryDate":"","paymentVoucherNo":"","cvv":"","unitId":self.model.unitId,"orgnisationId":self.model.orgId,"status":"A",
									"createdDate":new Date().getTime(),"createdBy":$scope.userId,"updatedDate":new Date().getTime(),"updatedBy":self.model.userId,
									"chequeDate":"","cardTypeId":"","isCancle":"I",'hospitalAccountId'     : '',
									'transactionNo'         : ""};
		
			$scope.getBankList();
			$scope.getCardTypeList();
			self.model.getTransactionTypes();
			self.model.getHospitalBankList();
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
			
			$scope.chequedDateOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : moment(new Date()).subtract(90, 'days').toDate(),
				};
			$scope.datepicker = {opened : false};
			$scope.companySettlement = true;
			$scope.setPaymentModeId(2);//default selected cheque
			$scope.page = "companyInvoiceSettlement";
			$scope.disableInsurance = true;
			$scope.transferToActiveDeposit = "N";
			$scope.hospitalBankListFlag = true;
			$scope.checkedAllArr = [];
			$scope.checkedAllIndex = -1;
		}
		
		$scope.cardExpiryOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};
		
		var OpenFromDate = function () {
	      self.model.fromDateOpened = true;
	    }

	    var OpenToDate = function () {
	      self.model.toDateOpened = true;
	    }
	    
	    
	    
	  //ADDED LATER GANESH
		var getHospitalBankList = function()
		{
			var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+self.model.orgId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		       {
				if (response.data.status == "success")
				{
					$scope.hospitalBankList = response.data.listObject;
				}
			  });
		}

		$scope.hidediv = false;
		
	    var getCompaniesByCompanyType = function(companyTypeId){
	    	$scope.hidediv = false;
	    	
           if(companyTypeId == 2 && $scope.unitConfigurationObject.invoiceCreationInsurance == 'N'){
        	   $scope.hidediv = true;
	    	}
	    	if(companyTypeId == 3 && $scope.unitConfigurationObject.invoiceCreationCorporate == 'N'){
	    		$scope.hidediv = true;
	    	}

	    	
	    	if(companyTypeId != ""){
		    	
	    		if(companyTypeId==3){
		    		$scope.disableInsurance=true;
		    		self.model.searchObj.associateCompanyId = '';
	    		}
		    	else{
		    		$scope.disableInsurance=false;
		    	}
	    		
	    		var param = {organizationId:self.model.orgId,unitId:self.model.unitId,companyTypeId:companyTypeId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList";
			    $rootScope.startLoader();
			    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 self.model.companies = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
	    	}else{
	    		self.model.searchObj.associateCompanyId = '';
	    		self.model.searchObj.companyId = '';
	    	}
	    }
	    
	    var getInsuranceByCompanyId = function(companyId){
	    	if(companyId != ""){
		    	var param = {organizationId:self.model.orgId,unitId:self.model.unitId,companyId:companyId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster";
			    $rootScope.startLoader();
			    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 self.model.associateCompanies = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
	    	}
	    }
	    
	    var getCompanyInvoiceListByFilter = function()
	    {
	    	
	    	
	    	if(self.model.searchObj.companyTypeId==undefined || self.model.searchObj.companyTypeId=='')
            {
                growl.warning("Please select company type...!!",{
                            title : 'Warning'
                        });
                        return false;
                    
            }
	    	if(self.model.searchObj.companyTypeId==3)
	    	{
	    		self.model.searchObj.associateCompanyId = '';
				 if(self.model.searchObj.companyId == '' || typeof(self.model.searchObj.companyId) == "undefined"){
						growl.warning("Please select company...!!",{
							title : 'Warning'
						});
						return false;
					}
			}else if(self.model.searchObj.companyTypeId==2){
				if( self.model.searchObj.companyId == '' || typeof(self.model.searchObj.companyId) == "undefined"){
						growl.warning("Please select company and insurance company...!!",{
							title : 'Warning'
						});
						return false;
					}
				if( self.model.searchObj.associateCompanyId == '' || typeof(self.model.searchObj.associateCompanyId) == "undefined"){
					growl.warning("Please select insurance company...!!",{
						title : 'Warning'
					});
					return false;
				}
			}
	    			var obj = angular.copy(self.model.searchObj);
	    	    	obj.fromDate = $rootScope.getFormattedDate(obj.fromDate,false);
	    	    	obj.toDate = $rootScope.getFormattedDate(obj.toDate,false);
	    	    	obj.multidelete("companyTypeId");
	    	    	var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/pending";
	    	    	$rootScope.startLoader();
	    		    BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	    				if(response.data.status == "success"){
	    					self.model.invoiceDetails = [];
	    					self.model.invoices = response.data.listObject;
	    					self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(0),tds:$rootScope.parseFloatNum(0),otherDeduction:$rootScope.parseFloatNum(0),
	    							disallowedAmount:$rootScope.parseFloatNum(0),securityDepositSettlement:$rootScope.parseFloatNum(0),total:$rootScope.parseFloatNum(0)};

                            for(var i=0; i<self.model.invoices.length; i++){
                                $scope.checkedAllArr[i] = true;
                            }
	    				}
	    				$rootScope.stopLoader();
	    			});
	    		    
	    		    
	    		    
	    		    
	    		    var URI = BASE_URL + ROOT_URL + "/api/billing/companyReceipts/companyReceiptsByCompany";
	    		    
	    		    var data = {
	    		    		
	    		    		companyId : self.model.searchObj.companyId,
	    			        insuranceCompanyId : self.model.searchObj.associateCompanyId
	    		    }
	    		    
	    		    BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							 $scope.companyAdvanced = [];
							 $scope.companyAdvanced = response.data.listObject;
							 console.log("ss",$scope.companyAdvanced)
							
						}
					});
	    		    
	    		
	    	
	    }
	    
	    var getCompanyInvoiceDetailsByInvoiceId = function(id,flag){
	    	if(flag){
	    		//get invoice details
	    		self.model.tdsAmountGlobal = 0.00;
		    	var URI = BASE_URL + ROOT_URL + "/api/billing/invoicedtls/invoice/"+id;
		    	$rootScope.startLoader();
			    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 self.model.invoiceDetails = self.model.invoiceDetails.concat(response.data.listObject);
						 self.model.invoiceDetailsOld = [];
						 self.model.invoiceDetailsOld = self.model.invoiceDetails.concat(response.data.listObject);
						 angular.forEach(self.model.invoiceDetailsOld,function(value,key){
							 angular.forEach(value.tCompanyInvoiceServiceDetailsDtoOld,function(value1,key1){
								 value1.otherDeductionOld = 0;
								 value1.otherDeductionOld = value1.otherDeduction;
							 });
						 });
						 var flgSet = false;
					    	var v = 1;
					    	self.model.invoiceDetails.forEach(function(value,index){
					    		if(value.isSelected){
					        		flgSet = true;
					    		}
					    		 if(v == self.model.invoiceDetails.length){
					    			if(flgSet == false){
					    				$scope.settleFlag = false;
					    			}else{
					    				$scope.settleFlag = true;
					    			}
					    		}
					    		v++;
					    	});
					}
					$rootScope.stopLoader();
				});
	    	}else{
	    		//remove invoice details
	    		$rootScope.startLoader();
	    		for(let i = (self.model.invoiceDetails.length - 1) ; i >= 0; i--){
	    			if(self.model.invoiceDetails[i].companyInvoiceId == id)
	    				self.model.invoiceDetails.splice(i,1);
	    			var flgSet = false;
	    	    	var v = 1;
	    	    	self.model.invoiceDetails.forEach(function(value,index){
	    	    		if(value.isSelected){
	    	        		flgSet = true;
	    	    		}
	    	    		 if(v == self.model.invoiceDetails.length){
	    	    			if(flgSet == false){
	    	    				$scope.settleFlag = false;
	    	    			}else{
	    	    				$scope.settleFlag = true;
	    	    			}
	    	    			 $scope.calculateSummaryForChange();
	    	    		}
	    	    		v++;
	    	    	});
	    		}
	    		//self.model.calculateSummary();
	    		$rootScope.stopLoader();
	    	}
	    	
	    }
	    
	    var getCompanyInvoiceListByFilterNew = function()
	    {
	    	
	    	
	    	if(self.model.searchObj.companyTypeId==undefined || self.model.searchObj.companyTypeId=='')
            {
                growl.warning("Please select company type...!!",{
                            title : 'Warning'
                        });
                        return false;
                    
            }
	    	if(self.model.searchObj.companyTypeId==3)
	    	{
	    		self.model.searchObj.associateCompanyId = '';
				 if(self.model.searchObj.companyId == '' || typeof(self.model.searchObj.companyId) == "undefined"){
						growl.warning("Please select company...!!",{
							title : 'Warning'
						});
						return false;
					}
			}else if(self.model.searchObj.companyTypeId==2){
				if( self.model.searchObj.companyId == '' || typeof(self.model.searchObj.companyId) == "undefined"){
						growl.warning("Please select company and insurance company...!!",{
							title : 'Warning'
						});
						return false;
					}
				if( self.model.searchObj.associateCompanyId == '' || typeof(self.model.searchObj.associateCompanyId) == "undefined"){
					growl.warning("Please select insurance company...!!",{
						title : 'Warning'
					});
					return false;
				}
			}
	    			var obj = angular.copy(self.model.searchObj);
	    	    	obj.fromDate = $rootScope.getFormattedDate(obj.fromDate,false);
	    	    	obj.toDate = $rootScope.getFormattedDate(obj.toDate,false);
	    	    	obj.multidelete("companyTypeId");
	    	    	var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/pending";
	    	    	$rootScope.startLoader();
	    		    BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	    				if(response.data.status == "success"){
	    					self.model.invoiceDetails = [];
	    					self.model.invoices = response.data.listObject;
	    					angular.forEach(self.model.invoices,function(inv,key){
	  	    		    	  inv.isSelected = true;
	  	    		        });
	    					self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(0),tds:$rootScope.parseFloatNum(0),otherDeduction:$rootScope.parseFloatNum(0),
	    							disallowedAmount:$rootScope.parseFloatNum(0),securityDepositSettlement:$rootScope.parseFloatNum(0),total:$rootScope.parseFloatNum(0)};

                            for(var i=0; i<self.model.invoices.length; i++){
                                $scope.checkedAllArr[i] = true;
                            }
                            angular.forEach(self.model.invoices,function(inv,key){
      	    		    	  getCompanyInvoiceDetailsByInvoiceId(inv.companyInvoiceId,inv.isSelected);
      	    		        });
	    				}
	    				$rootScope.stopLoader();
	    			});
	    		    
	    		    var URI = BASE_URL + ROOT_URL + "/api/billing/companyReceipts/companyReceiptsByCompany";
	    		    
	    		    var data = {
	    		    		
	    		    		companyId : self.model.searchObj.companyId,
	    			        insuranceCompanyId : self.model.searchObj.associateCompanyId
	    		    }
	    		    
	    		    BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							 $scope.companyAdvanced = [];
							 $scope.companyAdvanced = response.data.listObject;
							
						}
					});
	    		    
	    		  
	    		
	    	
	    }
	    

	    // Old-17-4-2020
	    /*var calculateSummaryAndValidate = function(invdtl,key){
	    	var balanceAmount = $rootScope.parseFloatNum(parseFloat(invdtl.outstanding));
	    	var totalAmount = parseFloat(parseFloat($rootScope.parseFloatNum(invdtl.receivedAmount)) + parseFloat($rootScope.parseFloatNum(invdtl.tdsAmount)) + parseFloat($rootScope.parseFloatNum(invdtl.otherDeduction)) 
	    							+ parseFloat($rootScope.parseFloatNum(invdtl.disallowedAmount)) + parseFloat($rootScope.parseFloatNum(invdtl.securityDepositSettlement)));
	    	
	    	if(totalAmount > balanceAmount){
	    		growl.error("Total should be less than balance amount",{
	    			title : ERROR_MSG
	    		});
	    		invdtl.total = $rootScope.parseFloatNum(totalAmount - parseFloat(invdtl[key]));
	    		invdtl[key] = parseFloat(0);
	    	}else{
	    		invdtl.total = $rootScope.parseFloatNum(totalAmount);
	    	}
	    	self.model.calculateSummary();
	    	
	    }*/
	    
	    $scope.calculateSummaryForChangeSave = function(){
	    	//calculate summary
	    	
	    	var receivedAmount = 0,tds = 0,otherDeduction = 0,disallowedAmount = 0,total = 0;
	    	
	    	
	    	self.model.invoiceDetails.forEach(function(value,index){
	    		if(value.isSelected){
	    			
		    		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(value.receivedAmount));
		    		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(value.tdsAmount));
		    		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(value.otherDeduction));
		    		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(value.disallowedAmount));
		    		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(value.total));
		    		
		    		var len = value.tCompanyInvoiceServiceDetailsDto.length;
		    		
		    		
		    		//var tmpTds =  parseFloat(value.tdsAmount)/len;
		    		//var tmpOD =  parseFloat(value.otherDeduction)/len;
		    		var tmpDA = parseFloat(value.disallowedAmount)/len;
//		    		$scope.checkAllOrder = true;
		    		var checkDisAllAmou = 0;
		    		value.tCompanyInvoiceServiceDetailsDto.forEach(function(c,index){
		    			checkDisAllAmou += parseFloat(c.disallowedAmount);
		    		});
		    		var otrduTmp = parseFloat(0);
		    		var otrTDSTmp = parseFloat(0);
		    		var otrDISTmp = parseFloat(0);
		    		var v = 1;
		    		var rlTmp = value.tCompanyInvoiceServiceDetailsDto.length;
		    		var tempDisAllAmt=0;
		    		value.tCompanyInvoiceServiceDetailsDto.forEach(function(value1,index){
		    			if(value1.isSelected){
		    			if(value1.creditPayableAmount > 0  && parseFloat(value.receivedAmount) > 0){

		    				var disAllowAmount=parseFloat((value1.creditPayableAmount*value.disallowedAmount)/value.total).toFixed(2);
			    			value1.isSelected = true;
			    			
			    			
			    			if(parseFloat(value1.tdsAmount) == '' && parseFloat(value1.tdsAmount) == 0 && value.popUpDetailsSaved == false){
			    				
			    				if(parseFloat(value.tdsPercentage) == undefined){
				    				value.tdsPercentage = 0;
				    			}
				    			if(parseFloat(value.tdsPercentage) == 0){
				    			  value.tdsPercentage = ((parseFloat(value.tdsAmount)/parseFloat(value.receivedAmount)) * 100).toFixed(2);
				    			}
				    			
					    			if(parseFloat(value.tdsAmount) != 0){
					    				var tejd = (value1.creditPayableAmount * (value.tdsPercentage/100));
										var tdsAnsos = value1.creditPayableAmount - tejd.toFixed(4);
										value1.tdsAmounttest = value1.creditPayableAmount - tdsAnsos;
										value1.tdsAmount = value1.tdsAmounttest.toFixed(2);
										var tmpTds = value1.tdsAmount;
					    		}else{
					    			value1.tdsAmount = 0.00;
					    			var tmpTds = value1.tdsAmount;
					    		}
				    		}
							//value1.tdsAmount = parseFloat(tmpTds).toFixed(2);
	     					//value1.disallowedAmount = parseFloat(tmpDA).toFixed(2);
			    			/*if((parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false ) || (checkDisAllAmou != disallowedAmount )){
							tempDisAllAmt = parseFloat(tempDisAllAmt)+parseFloat(disAllowAmount);
							if(tempDisAllAmt>value.disallowedAmount){
								var diffDisAmt=parseFloat(tempDisAllAmt-value.disallowedAmount);
								value1.disallowedAmount = disAllowAmount-diffDisAmt;
								otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
							}else{
								value1.disallowedAmount = disAllowAmount;
								otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
							}
							
			    			}*/
			    			
			    			  if(parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false){
				    				
				    				if(parseFloat(value.daPercentage) == undefined){
					    				value.daPercentage = 0;
					    			}
					    			if(parseFloat(value.daPercentage) == 0){
					    			  value.daPercentage = ((parseFloat( value.disallowedAmount)/parseFloat(value.receivedAmount))* 100).toFixed(2);
					    			}
					    			
					    			if(parseFloat(value1.disallowedAmount) != 0){
						    				var teDAD = (value1.creditPayableAmount * (value.daPercentage/100));
											var tdsAnDAD = value1.creditPayableAmount - teDAD.toFixed(4);
											value1.tdsAmountDAD= value1.creditPayableAmount - tdsAnDAD;
											value1.disallowedAmount = value1.tdsAmountDAD.toFixed(2);
											var tmpDA = value1.disallowedAmount;
					    			}
						    		
			    			  }
							value1.receivedAmount = parseFloat(0);
							
							if(parseFloat(value1.otherDeduction) == '' && parseFloat(value1.otherDeduction) == 0 && value.popUpDetailsSaved == false){
							if(value.odPercentage == undefined){
			    				value.odPercentage = 0;
			    			}
			    			if(parseFloat(value1.otherDeduction) != 0){
			    				var tejdod = (value1.creditPayableAmount * (value.odPercentage/100));
								var tdsAnsosod = value1.creditPayableAmount - tejdod.toFixed(4);
								value1.tdsAmounttestod = value1.creditPayableAmount - tdsAnsosod;
								value1.otherDeduction = value1.tdsAmounttestod.toFixed(2);
								var tmpOD = value1.otherDeduction;
			    			}
			    			
							}
//							value1.otherDeduction = parseFloat(tmpOD).toFixed(2);
							otrduTmp = parseFloat(otrduTmp)+parseFloat(value1.otherDeduction);
							otrTDSTmp = parseFloat(otrTDSTmp)+parseFloat(value1.tdsAmount);
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
							
							
							var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction)+parseFloat(value1.dueAmount));
								value1.receivedAmount = parseFloat(recAmt).toFixed(2);
							
							
							//value1.dueAmount = 0;
							
//							value.dueAmount  = parseFloat(value.dueAmount) - parseFloat(value.receivedAmount);
							if(v == rlTmp){
								if(parseFloat(otrduTmp).toFixed(4) != parseFloat(value.otherDeduction).toFixed(4)){
									var difTMp = parseFloat(otrduTmp) - parseFloat(value.otherDeduction);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction = (parseFloat(tmpOD) - parseFloat(difTMp)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMp)).toFixed(2);
								}
								
								if(parseFloat(otrTDSTmp).toFixed(4) != parseFloat(value.tdsAmount).toFixed(4)){
									var difTMpTDS = parseFloat(otrTDSTmp) - parseFloat(value.tdsAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount = (parseFloat(tmpTds) - parseFloat(difTMpTDS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpTDS)).toFixed(2);
								}
								
								if(parseFloat(otrDISTmp).toFixed(4) != parseFloat(value.disallowedAmount).toFixed(4)){
									var difTMpDIS = parseFloat(otrDISTmp) - parseFloat(value.disallowedAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount = (parseFloat(tmpDA) - parseFloat(difTMpDIS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpDIS)).toFixed(2);
								}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount=0
									}
							}
		    				
		    			}else if(value1.creditPayableAmount < 0  && parseFloat(value.receivedAmount) > 0){
		    				
		    				var disAllowAmount=parseFloat((value1.creditPayableAmount*value.disallowedAmount)/value.total).toFixed(2);
			    			value1.isSelected = true;
			    			
			    			
			    			if(parseFloat(value1.tdsAmount) == '' && parseFloat(value1.tdsAmount) == 0 && value.popUpDetailsSaved == false){
			    				
			    				if(parseFloat(value.tdsPercentage) == undefined){
				    				value.tdsPercentage = 0;
				    			}
				    			if(parseFloat(value.tdsPercentage) == 0){
				    			  value.tdsPercentage = ((parseFloat(value.tdsAmount)/parseFloat(value.receivedAmount)) * 100).toFixed(2);
				    			}
				    			
					    			if(parseFloat(value.tdsAmount) != 0){
					    				var tejd = (value1.creditPayableAmount * (value.tdsPercentage/100));
										var tdsAnsos = value1.creditPayableAmount - tejd.toFixed(4);
										value1.tdsAmounttest = value1.creditPayableAmount - tdsAnsos;
										value1.tdsAmount = value1.tdsAmounttest.toFixed(2);
										var tmpTds = value1.tdsAmount;
					    		}else{
					    			value1.tdsAmount = 0.00;
					    			var tmpTds = value1.tdsAmount;
					    		}
				    		}
							//value1.tdsAmount = parseFloat(tmpTds).toFixed(2);
	     					//value1.disallowedAmount = parseFloat(tmpDA).toFixed(2);
//			    			if((parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false ) || (checkDisAllAmou != disallowedAmount )){
//							tempDisAllAmt = parseFloat(tempDisAllAmt)+parseFloat(disAllowAmount);
//							if(tempDisAllAmt>value.disallowedAmount){
//								var diffDisAmt=parseFloat(tempDisAllAmt-value.disallowedAmount);
//								value1.disallowedAmount = disAllowAmount-diffDisAmt;
//								otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
//							}else{
//								value1.disallowedAmount = disAllowAmount;
//								otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
//							}
//							
//			    			}
			    			
			    			  if(parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false){
				    				
				    				if(parseFloat(value.daPercentage) == undefined){
					    				value.daPercentage = 0;
					    			}
					    			if(parseFloat(value.daPercentage) == 0){
					    			  value.daPercentage = ((parseFloat( value.disallowedAmount)/parseFloat(value.receivedAmount))* 100).toFixed(2);
					    			}
					    			if(parseFloat(value1.disallowedAmount) != 0){
						    				var teDAD = (value1.creditPayableAmount * (value.daPercentage/100));
											var tdsAnDAD = value1.creditPayableAmount - teDAD.toFixed(4);
											value1.tdsAmountDAD= value1.creditPayableAmount - tdsAnDAD;
											value1.disallowedAmount = value1.tdsAmountDAD.toFixed(2);
											var tmpDA = value1.disallowedAmount;
					    			}
						    		
			    			  }
			    			  
							value1.receivedAmount = parseFloat(0);
							
							if(parseFloat(value1.otherDeduction) == '' && parseFloat(value1.otherDeduction) == 0 && value.popUpDetailsSaved == false){
							if(value.odPercentage == undefined){
			    				value.odPercentage = 0;
			    			}
			    			if(parseFloat(value1.otherDeduction) != 0){
			    				var tejdod = (value1.creditPayableAmount * (value.odPercentage/100));
								var tdsAnsosod = value1.creditPayableAmount - tejdod.toFixed(4);
								value1.tdsAmounttestod = value1.creditPayableAmount - tdsAnsosod;
								value1.otherDeduction = value1.tdsAmounttestod.toFixed(2);
								var tmpOD = value1.otherDeduction;
			    			}
			    			
							}
			    			
//							value1.otherDeduction = parseFloat(tmpOD).toFixed(2);
							otrduTmp = parseFloat(otrduTmp)+parseFloat(value1.otherDeduction);
							otrTDSTmp = parseFloat(otrTDSTmp)+parseFloat(value1.tdsAmount);
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
							
							
							var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction)+parseFloat(value1.dueAmount));
								value1.receivedAmount = parseFloat(recAmt).toFixed(2);
							
							
							//value1.dueAmount = 0;
							
//							value.dueAmount  = parseFloat(value.dueAmount) - parseFloat(value.receivedAmount);
							if(v == rlTmp){
								if(parseFloat(otrduTmp).toFixed(4) != parseFloat(value.otherDeduction).toFixed(4)){
									var difTMp = parseFloat(otrduTmp) - parseFloat(value.otherDeduction);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction = (parseFloat(tmpOD) - parseFloat(difTMp)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMp)).toFixed(2);
								}
								
								if(parseFloat(otrTDSTmp).toFixed(4) != parseFloat(value.tdsAmount).toFixed(4)){
									var difTMpTDS = parseFloat(otrTDSTmp) - parseFloat(value.tdsAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount = (parseFloat(tmpTds) - parseFloat(difTMpTDS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpTDS)).toFixed(2);
								}
								
								if(parseFloat(otrDISTmp).toFixed(4) != parseFloat(value.disallowedAmount).toFixed(4)){
									var difTMpDIS = parseFloat(otrDISTmp) - parseFloat(value.disallowedAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount = (parseFloat(tmpDA) - parseFloat(difTMpDIS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpDIS)).toFixed(2);
								}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount=0
									}
							}
		    				
		    			}
		    			
						v++;
						}
		    		});
	    		}
	    		
	    		
	    	});
	    	
	    	
	    	console.log("3",self.model.invoiceDetails);
	    	
	    	self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(receivedAmount),tds:$rootScope.parseFloatNum(tds),total:$rootScope.parseFloatNum(total),
	    									otherDeduction:$rootScope.parseFloatNum(otherDeduction),disallowedAmount:$rootScope.parseFloatNum(disallowedAmount)};
	    	self.model.tdsAmountGlobal = self.model.invoiceDetailsSummary.tds;
	    	
	    	//added for payment collection
	    	$scope.dueAmount = $rootScope.parseFloatNum(total);
	    	$rootScope.setFirstTabLevel(2);
	    }
	    
	    $scope.calculateSummaryForChange = function(){
	    	//calculate summary
	    	
	    	var receivedAmount = 0,tds = 0,otherDeduction = 0,disallowedAmount = 0,total = 0;
	    	
	    	
	    	self.model.invoiceDetails.forEach(function(value,index){
	    		if(value.isSelected){
	    			
		    		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(value.receivedAmount));
		    		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(value.tdsAmount));
		    		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(value.otherDeduction));
		    		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(value.disallowedAmount));
		    		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(value.total));
		    		
		    		var len = value.tCompanyInvoiceServiceDetailsDto.length;
		    		
		    		var checkDisAllAmou = 0;
		    		value.tCompanyInvoiceServiceDetailsDto.forEach(function(c,index){
		    			checkDisAllAmou += parseFloat(c.disallowedAmount);
		    		});
		    		
//		    		if($scope.cheackedArr[index] == false){
//		    			$scope.checkAllOrder = true;
//		    		}

		    		var otrduTmp = parseFloat(0);
		    		var otrTDSTmp = parseFloat(0);
		    		var otrDISTmp = parseFloat(0);
		    		var v = 1;
		    		var rlTmp = value.tCompanyInvoiceServiceDetailsDto.length;
		    		var tempDisAllAmt=0;
		    		value.tCompanyInvoiceServiceDetailsDto.forEach(function(value1,index){
		    			if(value1.isSelected){
		    			if(value1.creditPayableAmount > 0 && parseFloat(value.receivedAmount) > 0){
		    				
		    				var disAllowAmount=parseFloat((value1.creditPayableAmount*value.disallowedAmount)/value.total).toFixed(2);
			    			value1.isSelected = true;
			    			
			    			
			    			if(parseFloat(value1.tdsAmount) == '' && parseFloat(value1.tdsAmount) == 0 && value.popUpDetailsSaved == false){
			    				
			    				if(parseFloat(value.tdsPercentage) == undefined){
				    				value.tdsPercentage = 0;
				    			}
				    			if(parseFloat(value.tdsPercentage) == 0){
				    			  value.tdsPercentage = ((parseFloat(value.tdsAmount)/parseFloat(value.receivedAmount)) * 100).toFixed(2);
				    			}
				    			
					    			if(parseFloat(value.tdsAmount) != 0){
					    				var tejd = (value1.creditPayableAmount * (value.tdsPercentage/100));
										var tdsAnsos = value1.creditPayableAmount - tejd.toFixed(4);
										value1.tdsAmounttest = value1.creditPayableAmount - tdsAnsos;
										value1.tdsAmount = value1.tdsAmounttest.toFixed(2);
										var tmpTds = value1.tdsAmount;
					    		}else{
					    			value1.tdsAmount = 0.00;
					    			var tmpTds = value1.tdsAmount;
					    		}
				    		}
			    			
		                      if( value.popUpDetailsSaved == false || parseFloat(value1.disallowedAmount) == 0 ){

			    				
			    				if(value.daPercentage == undefined){
				    				value.daPercentage = 0;
				    			}
				    			if(parseFloat(value.daPercentage) == 0){
				    			  value.daPercentage = ((parseFloat( value.disallowedAmount)/parseFloat(value.receivedAmount))* 100).toFixed(2);
				    			}
				    			
					    	 
					    				var teDAD = (value1.receivedAmount * (value.daPercentage/100));
										var tdsAnDAD = value1.receivedAmount - teDAD;
										value1.tdsAmountDAD= value1.receivedAmount  - tdsAnDAD;
										value1.disallowedAmount = value1.tdsAmountDAD.toFixed(2);
										var tmpDA = value1.disallowedAmount;
					    		
                      }
			    			
							//value1.receivedAmount = parseFloat(0);
							
                            if((value.popUpDetailsSaved == false) || parseFloat(value1.otherDeduction) == 0 ){

							if(value.odPercentage == undefined){
			    				value.odPercentage = 0;
			    			}
			    				var tejdod = (value1.receivedAmount * (value.odPercentage/100));
								var tdsAnsosod = value1.receivedAmount - tejdod.toFixed(4);
								value1.tdsAmounttestod = value1.receivedAmount - tdsAnsosod;
								value1.otherDeduction = value1.tdsAmounttestod.toFixed(2);
								var tmpOD = value1.otherDeduction;
			    			}
                        	value1.receivedAmount = parseFloat(0);
							otrduTmp = parseFloat(otrduTmp)+parseFloat(value1.otherDeduction);
							otrTDSTmp = parseFloat(otrTDSTmp)+parseFloat(value1.tdsAmount);
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
						/*	
							var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
							value1.receivedAmount = parseFloat(recAmt).toFixed(2);
							value1.dueAmount = 0;*/
							if(value.receivedAmountNew != undefined){
								 var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
									
		                            var recivedAmountCheck= parseFloat((value.receivedAmount * value1.creditPayableAmount)/value.outstanding).toFixed(1);
									
									if(parseFloat(recAmt).toFixed(1) != parseFloat(recivedAmountCheck)){
										value1.receivedAmount = parseFloat(0);
										value1.dueAmount = parseFloat(recAmt) - parseFloat(recivedAmountCheck);
										value1.receivedAmount = parseFloat(recivedAmountCheck).toFixed(2);
									}else{
										value1.receivedAmount = parseFloat(0);
										var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
										value1.receivedAmount = parseFloat(recAmt).toFixed(2);
										value1.dueAmount = 0;
									}
							}else{
								var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
								value1.receivedAmount = parseFloat(recAmt).toFixed(2);
								value1.dueAmount = 0;
							}						
                           
							
							
							//value1.dueAmount = 0;
							
							
							if(v == rlTmp){
								if(parseFloat(otrduTmp).toFixed(4) != parseFloat(value.otherDeduction).toFixed(4)){
									var difTMp = parseFloat(otrduTmp) - parseFloat(value.otherDeduction);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction = (parseFloat(tmpOD) - parseFloat(difTMp)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMp)).toFixed(2);
								}
								
								if(parseFloat(otrTDSTmp).toFixed(4) != parseFloat(value.tdsAmount).toFixed(4)){
									var difTMpTDS = parseFloat(otrTDSTmp) - parseFloat(value.tdsAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount = (parseFloat(tmpTds) - parseFloat(difTMpTDS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpTDS)).toFixed(2);
								}
								
								if(parseFloat(otrDISTmp).toFixed(4) != parseFloat(value.disallowedAmount).toFixed(4)){
									var difTMpDIS = parseFloat(otrDISTmp) - parseFloat(value.disallowedAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount = (parseFloat(tmpDA) - parseFloat(difTMpDIS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpDIS)).toFixed(2);
								}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount=0
									}
							}
		    				
                        }else if(value1.creditPayableAmount < 0  && parseFloat(value.receivedAmount) > 0){
		    				
		    				var disAllowAmount=parseFloat((value1.creditPayableAmount*value.disallowedAmount)/value.total).toFixed(2);
			    			value1.isSelected = true;
			    			
			    			
			    			if(parseFloat(value1.tdsAmount) == '' && parseFloat(value1.tdsAmount) == 0 && value.popUpDetailsSaved == false){
			    				
			    				if(parseFloat(value.tdsPercentage) == undefined){
				    				value.tdsPercentage = 0;
				    			}
				    			if(parseFloat(value.tdsPercentage) == 0){
				    			  value.tdsPercentage = ((parseFloat(value.tdsAmount)/parseFloat(value.receivedAmount)) * 100).toFixed(2);
				    			}
				    			
					    			if(parseFloat(value.tdsAmount) != 0){
					    				var tejd = (value1.creditPayableAmount * (value.tdsPercentage/100));
										var tdsAnsos = value1.creditPayableAmount - tejd.toFixed(4);
										value1.tdsAmounttest = value1.creditPayableAmount - tdsAnsos;
										value1.tdsAmount = value1.tdsAmounttest.toFixed(2);
										var tmpTds = value1.tdsAmount;
					    		}else{
					    			value1.tdsAmount = 0.00;
					    			var tmpTds = value1.tdsAmount;
					    		}
				    		}
                            if( value.popUpDetailsSaved == false || parseFloat(value1.disallowedAmount) == 0){

				    				
				    				if(value.daPercentage == undefined){
					    				value.daPercentage = 0;
					    			}
					    			if(parseFloat(value.daPercentage) == 0){
					    			  value.daPercentage = ((parseFloat( value.disallowedAmount)/parseFloat(value.receivedAmount))* 100).toFixed(2);
					    			}
					    			
						    				var teDAD = (value1.creditPayableAmount * (value.daPercentage/100));
											var tdsAnDAD = value1.creditPayableAmount - teDAD;
											value1.tdsAmountDAD= value1.creditPayableAmount - tdsAnDAD;
											value1.disallowedAmount = value1.tdsAmountDAD.toFixed(2);
											var tmpDA = value1.disallowedAmount;
											
						    		
					    		}
			    			 
			    			 
							value1.receivedAmount = parseFloat(0);
							
                            if(value.popUpDetailsSaved == false  || parseFloat(value1.otherDeduction) == 0){

							if(value.odPercentage == undefined){
			    				value.odPercentage = 0;
			    			}
			    				var tejdod = (value1.creditPayableAmount * (value.odPercentage/100));
								var tdsAnsosod = value1.creditPayableAmount - tejdod.toFixed(4);
								value1.tdsAmounttestod = value1.creditPayableAmount - tdsAnsosod;
								value1.otherDeduction = value1.tdsAmounttestod.toFixed(2);
								var tmpOD = value1.otherDeduction;
			    			}
			    			
//							}
			    			
							otrduTmp = parseFloat(otrduTmp)+parseFloat(value1.otherDeduction);
							otrTDSTmp = parseFloat(otrTDSTmp)+parseFloat(value1.tdsAmount);
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
							
							if(value.receivedAmountNew != undefined){
								 var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
									
		                            var recivedAmountCheck= parseFloat((value.receivedAmount * value1.creditPayableAmount)/value.outstanding).toFixed(1);
									
									if(parseFloat(recAmt).toFixed(1) != parseFloat(recivedAmountCheck)){
										
										value1.dueAmount = parseFloat(recAmt) - parseFloat(recivedAmountCheck);
										value1.receivedAmount = parseFloat(recivedAmountCheck).toFixed(2);
									}else{
										var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
										value1.receivedAmount = parseFloat(recAmt).toFixed(2);
										value1.dueAmount = 0;
									}
							}else{
								var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
								value1.receivedAmount = parseFloat(recAmt).toFixed(2);
								value1.dueAmount = 0;
							}		
							/*var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
							value1.receivedAmount = parseFloat(recAmt).toFixed(2);
							value1.dueAmount = 0;*/
							
							if(v == rlTmp){
								if(parseFloat(otrduTmp).toFixed(4) != parseFloat(value.otherDeduction).toFixed(4)){
									var difTMp = parseFloat(otrduTmp) - parseFloat(value.otherDeduction);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction = (parseFloat(tmpOD) - parseFloat(difTMp)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMp)).toFixed(2);
								}
								
								if(parseFloat(otrTDSTmp).toFixed(4) != parseFloat(value.tdsAmount).toFixed(4)){
									var difTMpTDS = parseFloat(otrTDSTmp) - parseFloat(value.tdsAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount = (parseFloat(tmpTds) - parseFloat(difTMpTDS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpTDS)).toFixed(2);
								}
								
								if(parseFloat(otrDISTmp).toFixed(4) != parseFloat(value.disallowedAmount).toFixed(4)){
									var difTMpDIS = parseFloat(otrDISTmp) - parseFloat(value.disallowedAmount);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount = (parseFloat(tmpDA) - parseFloat(difTMpDIS)).toFixed(2);
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpDIS)).toFixed(2);
								}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount=0
									}
								if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount))
									{
									value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount=0
									}
							}
		    				
                        }
						v++;
						}
		    		});
	    		}
	    		
	    		
	    	});
	    	
	    	
	    	
	    	
	    	self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(receivedAmount),tds:$rootScope.parseFloatNum(tds),total:$rootScope.parseFloatNum(total),
	    									otherDeduction:$rootScope.parseFloatNum(otherDeduction),disallowedAmount:$rootScope.parseFloatNum(disallowedAmount)};
	    	self.model.tdsAmountGlobal = self.model.invoiceDetailsSummary.tds;
	    	
	    	//added for payment collection
	    	$scope.dueAmount = $rootScope.parseFloatNum(total);
	    	$rootScope.setFirstTabLevel(2);
	    }
	    
	    
	    
	    var calculateSummaryAndValidateForTDS = function(invdtl,type,key){
	    	
	    	if(invdtl.tdsAmount == '' || invdtl.tdsAmount == undefined || invdtl.tdsAmount == null){
	    		invdtl.tdsAmount = 0;
	    	}
	    	
	    	var tdsPercentage = (parseFloat(invdtl.tdsAmount)/parseFloat(invdtl.receivedAmount)) * 100;
	    	
	    	console.log(tdsPercentage);
	    	
	    	var otherDeductionAmount = parseFloat(invdtl.otherDeduction) + parseFloat($rootScope.parseFloatNum(invdtl.securityDepositSettlement)) + parseFloat(invdtl.disallowedAmount);
	    	
	    	if(parseFloat(invdtl.outstanding) > parseFloat(otherDeductionAmount))
	    		{
	    			var diff = parseFloat(invdtl.outstanding) - parseFloat(otherDeductionAmount);
	    			diff =  parseFloat(diff).toFixed(2);
	    			if(parseFloat(invdtl.tdsAmount) > parseFloat(diff)){
	    				growl.error("You can apply tds on "+parseFloat(diff).toFixed(2),{
	    	    			title : ERROR_MSG
	    	    		});
	    				
	    				self.model.invoiceDetails[key].tdsAmount = 0;
	    				self.model.invoiceDetails[key].tdsPercentage = tdsPercentage;
	    				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
	    				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
	    				$scope.calculateSummaryForChange();
	    				
	    				saveServiceInvoiceDetailsOutSide(key);
	    				return false;
	    			}else{
	    				var diff = parseFloat(diff) - parseFloat(invdtl.tdsAmount);
	    				self.model.invoiceDetails[key].tdsPercentage = parseFloat(tdsPercentage).toFixed(2); 
	    				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
	    				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
	    				 //$scope.calculateSummaryForChange();
	    				$scope.calculateSummaryForChange();
	    				
	    				saveServiceInvoiceDetailsOutSide(key);
	    			}
	    			
	    		}
	    	
	    }
	    
	    
var calculateSummaryAndValidateForOD = function(invdtl,type,key){
	
	 if(parseFloat(invdtl.otherDeduction) == parseFloat(self.model.invoiceDetailsSummary.receivedAmount)){
			growl.error("Other Deduction Amount Should Not Be Equals To Recived Amount");
			invdtl.otherDeduction = 0.00;
			
			return false;
		}
		
	if( parseFloat(invdtl.otherDeduction) > parseFloat(self.model.invoiceDetailsSummary.receivedAmount)){
			growl.error("Other Deduction amount should not be greater than Received amount");
			  invdtl.otherDeduction=0;
			/*angular.forEach(self.model.invoiceDetails,function(value,index){
				otherDeduction += parseFloat(value.otherDeduction);
			});*/
			invdtl.otherDeduction = otherDeduction;
			
			return false;
		}
	
	if(invdtl.otherDeduction == '' || invdtl.otherDeduction == undefined || invdtl.otherDeduction == null){
		invdtl.otherDeduction = 0;
		angular.forEach(self.model.invoiceDetails[key].tCompanyInvoiceServiceDetailsDto,function(v,k){
			v.otherDeduction=0.00;
		});
	}
	
	if(invdtl.receivedAmountNew != undefined  && parseFloat(invdtl.receivedAmountNew) !=  parseFloat(invdtl.total)){
		invdtl.receivedAmountNew = undefined;
	}
	
	
         var odPercentage = (parseFloat(invdtl.otherDeduction)/parseFloat(invdtl.receivedAmount)) * 100;
    
          console.log(odPercentage);

    
	    	var otherDeductionAmount = parseFloat(invdtl.tdsAmount) + parseFloat($rootScope.parseFloatNum(invdtl.securityDepositSettlement)) + parseFloat(invdtl.disallowedAmount);
	    	
	    	if(parseFloat(invdtl.outstanding) > parseFloat(otherDeductionAmount))
	    		{
	    		 if(parseFloat(invdtl.receivedAmount) == parseFloat(invdtl.outstanding)){
	    			   var diff = parseFloat(invdtl.outstanding) - parseFloat(otherDeductionAmount);
	    		   }else{
	    			   if(invdtl.receivedAmountNew != undefined){
	    				   var diff = parseFloat(invdtl.receivedAmountNew) - parseFloat(otherDeductionAmount);
	    			   }else{
	    				   var diff = parseFloat(invdtl.outstanding) - parseFloat(otherDeductionAmount);
	    			   }
	    			   
	    		   }
	    			if(parseFloat(invdtl.otherDeduction) > parseFloat(diff).toFixed(2)){
	    				growl.error("You can apply other deduction on "+parseFloat(diff).toFixed(2),{
	    	    			title : ERROR_MSG
	    	    		});
	    				
	    				self.model.invoiceDetails[key].otherDeduction = 0;
	    				self.model.invoiceDetails[key].odPercentage = odPercentage;
	    				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
	    				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
	    				$scope.calculateSummaryForChange();
	    				saveServiceInvoiceDetailsOutSide(key);
	    				
	    				return false;
	    			}else{
	    				var diffT= parseFloat(diff) - parseFloat(invdtl.otherDeduction);
	    				self.model.invoiceDetails[key].odPercentage = odPercentage;
	    				self.model.invoiceDetails[key].receivedAmount = parseFloat(diffT).toFixed(2);
	    				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
	    				$scope.calculateSummaryForChange();
	    				saveServiceInvoiceDetailsOutSide(key);
	    			}
	    			
	    		}
	    	
	    }

var calculateSummaryAndValidateForDA = function(invdtl,type,key){
	
	 if(parseFloat(invdtl.disallowedAmount) == parseFloat(self.model.invoiceDetailsSummary.receivedAmount)){
			growl.error("Disallowed  Amount Should Not Be Equals To Recived Amount");
			invdtl.disallowedAmount = 0.00;
			
			return false;
		}
		
	if( parseFloat(invdtl.disallowedAmount) > parseFloat(self.model.invoiceDetailsSummary.receivedAmount)){
			growl.error("Disallowed  Amount Should Not Be Equals To Recived Amount");
			 var otherDeduction = 0.00;
			/*angular.forEach(self.model.invoiceDetails,function(value,index){
				otherDeduction += parseFloat(value.otherDeduction);
			});*/
			invdtl.disallowedAmount = otherDeduction;
			
			return false;
		}
	
	if(invdtl.disallowedAmount == '' || invdtl.disallowedAmount == undefined || invdtl.disallowedAmount == null){
		invdtl.disallowedAmount = 0;
	}
	
	if(invdtl.receivedAmountNew != undefined  && parseFloat(invdtl.receivedAmountNew) !=  parseFloat(invdtl.total)){
		invdtl.receivedAmountNew = undefined;
	}
	 var daPercentage = ((parseFloat( invdtl.disallowedAmount)/parseFloat(invdtl.receivedAmount)) * 100).toFixed(2);
	    
     console.log(daPercentage);
	
	var otherDeductionAmount =parseFloat(invdtl.tdsAmount) + parseFloat($rootScope.parseFloatNum(invdtl.securityDepositSettlement)) + parseFloat(invdtl.otherDeduction);
	
	if(parseFloat(invdtl.outstanding) > parseFloat(otherDeductionAmount))
		{
		 if(parseFloat(invdtl.receivedAmount) == parseFloat(invdtl.outstanding)){
			   var diff = parseFloat(invdtl.outstanding) - parseFloat(otherDeductionAmount);
		   }else{
			   
			   if(invdtl.receivedAmountNew != undefined){
				   var diff = parseFloat(invdtl.receivedAmountNew) - parseFloat(otherDeductionAmount);
			   }else{
				   var diff = parseFloat(invdtl.outstanding) - parseFloat(otherDeductionAmount);
			   }
		   }
			if(parseFloat(invdtl.disallowedAmount) > parseFloat(diff).toFixed(2)){
				growl.error("You can apply disallowed amt on "+parseFloat(diff).toFixed(2),{
	    			title : ERROR_MSG
	    		});
				
				self.model.invoiceDetails[key].disallowedAmount = 0;
				self.model.invoiceDetails[key].daPercentage = daPercentage;
				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
				$scope.calculateSummaryForChange();
				saveServiceInvoiceDetailsOutSide(key);
				
				
				return false;
			}else{
				var diff = parseFloat(diff) - parseFloat(invdtl.disallowedAmount);
				self.model.invoiceDetails[key].daPercentage = daPercentage;
				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
				$scope.calculateSummaryForChange();
				saveServiceInvoiceDetailsOutSide(key);
			}
			
		}
	
}


$scope.calculateTotalDueRefAmount = function()
{
	var amount = 0;
	var dueAmount = $rootScope.parseFloatNum($scope.dueAmount);
	$scope.dueAmountInitially=dueAmount;
	$scope.consumedAmt = 0;
	//var payeeDueAmtFinal = $rootScope.parseFloatNum(self.model.invoiceDetailsSummary.receivedAmount);
	
	angular.forEach($scope.listPaymentDetails,function(value,key){
		amount = amount + parseFloat(value.amount); 
	});
	
	if($scope.companyAdvanced!=null &&$scope.companyAdvanced.length>0)
	{
		angular.forEach($scope.companyAdvanced,function(value,key)
				{
					if(value.depositeFlag=='Y')
						{
						  $scope.consumedAmt = $scope.consumedAmt + parseFloat(value.consumedAmount- value.oldConsumedAmount); 
						}
				});
	}
	var tempDueAmt = parseFloat(self.model.invoiceDetailsSummary.receivedAmount) - parseFloat(amount) - parseFloat($scope.consumedAmt);
	var temppayeeDueAmtFinal = self.model.invoiceDetailsSummary.receivedAmount - amount - $scope.consumedAmt;
	//$scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;
	//$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
	var df = (parseFloat(self.model.invoiceDetailsSummary.receivedAmount) - (parseFloat(amount) + parseFloat($scope.consumedAmt)));
	if(df > 0)
		{
		$scope.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
	    $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
	    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
		}
	else
		{
		$scope.dueAmount = parseFloat(0.00).toFixed(2);
	    $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
	    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
		}
	$scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
	$scope.amountCollected = amount;
	 $scope.consumedAmt =parseFloat($scope.consumedAmt).toFixed(2);
	if(parseFloat($scope.consumedAmt) > 0)
		{
		  $scope.depositeSettleBtnFlag = false;
		}
	else
		{
		$scope.depositeSettleBtnFlag = true;
		}
}

$scope.depositeSub = function(item,idx)
{
	//payment details
	var totalCollected = 0;
	var totalCollectedTmp = 0;
	if($scope.listPaymentDetails.length > 0){
	var i = 1;
		angular.forEach($scope.listPaymentDetails,function(value,key){
			totalCollectedTmp = parseFloat(totalCollectedTmp) + parseFloat(value.amount);
		if($scope.listPaymentDetails.length == i){
			totalCollected = parseFloat(totalCollectedTmp) + parseFloat($scope.consumedAmt);
		}
		i++;
	});
	}else{
		totalCollected =  parseFloat($scope.consumedAmt);
	}
/*	if($rootScope.parseFloatNum(totalCollected) != $rootScope.parseFloatNum(self.model.invoiceDetailsSummary.receivedAmount)){
		growl.error("Collected amount total amount differs please recheck" ,{
			title : ERROR_MSG
		});
		$rootScope.stopLoader();
		$scope.btnSettleDisabled = false;
		return;
	}*/
	
	if((parseFloat(self.model.invoiceDetailsSummary.receivedAmount) >= parseFloat(totalCollected)) || item.depositeFlag == 'N'){
	var consumedAmt = 0;
	$scope.dueAmount = parseFloat(self.model.invoiceDetailsSummary.receivedAmount) - parseFloat(totalCollected);
	if(item.depositeFlag == 'Y')
		{
			if($scope.dueAmount == 0)
			{
				$scope.companyAdvanced[idx].depositeFlag = 'N';
			}
			else if($scope.dueAmount >= item.balanceAmount)
				{
					consumedAmt = (parseFloat(consumedAmt)+parseFloat(item.balanceAmount)).toFixed(2);
					$scope.companyAdvanced[idx].consumedAmount = parseFloat(item.consumedAmount)+parseFloat(item.balanceAmount);
					$scope.companyAdvanced[idx].balanceAmount = parseFloat(0);
					$scope.dueAmount = (parseFloat($scope.dueAmount)- parseFloat(consumedAmt)).toFixed(2);
				}
			else if($scope.dueAmount <= item.balanceAmount)
				{
					consumedAmt = (parseFloat(consumedAmt)+parseFloat($scope.dueAmount)).toFixed(2);
					$scope.companyAdvanced[idx].balanceAmount = parseFloat(item.balanceAmount)- parseFloat($scope.dueAmount);
					$scope.companyAdvanced[idx].consumedAmount =item.consumedAmount + parseFloat($scope.dueAmount);
					$scope.dueAmount = (parseFloat($scope.dueAmount)- parseFloat($scope.dueAmount)).toFixed(2);
				}
			 
		}
	if(item.depositeFlag == 'N')
	{
				if($scope.companyAdvanced[idx].balanceAmount > 0)
					{
					$scope.companyAdvanced[idx].balanceAmount = parseFloat(item.oldBalanceAmount);
					$scope.companyAdvanced[idx].consumedAmount = parseFloat(item.oldConsumedAmount);
					$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(item.oldBalanceAmount-item.balanceAmount)).toFixed(2);
					}
				else{
				$scope.companyAdvanced[idx].balanceAmount = $scope.companyAdvanced[idx].balanceAmount + parseFloat(item.consumedAmount-item.oldConsumedAmount);
				$scope.companyAdvanced[idx].consumedAmount = parseFloat(item.oldConsumedAmount);
				$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat($scope.companyAdvanced[idx].balanceAmount)).toFixed(2);
				}
				
				
				/*var totalListamount = 0;
				angular.forEach($scope.listPaymentDetails,function(value,key)
						{
					totalListamount = totalListamount+parseInt(value.amount);
						});

				if(parseInt($scope.totalAmount) >=totalListamount){
					
					$scope.excessAmount = parseFloat(0).toFixed(2);
				}else{
					var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
					$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
				}*/
				
	}
	
	/*var avalForFlag = false;
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
	*/
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

var calculateSummaryAndValidateForRecive = function(invdtl,type,key){
	
	
	if(invdtl.receivedAmount == '' || invdtl.receivedAmount == undefined || invdtl.receivedAmount == null){
		invdtl.receivedAmount = 0;
	}
	
	var checkAmt = parseFloat(invdtl.outstanding) - (parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction));
	
	if(parseFloat(checkAmt) >= parseFloat(invdtl.receivedAmount)){
		invdtl.receivedAmountNew = parseFloat(invdtl.receivedAmount).toFixed(2);
	}
	if(parseFloat(checkAmt) >= parseFloat(invdtl.receivedAmount)){
		
		self.model.invoiceDetails[key].receivedAmount = parseFloat(invdtl.receivedAmount).toFixed(2);
		self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
		$scope.calculateSummaryForChange();
		saveServiceInvoiceDetailsOutSide(key);
	}
	else
		{
		self.model.invoiceDetails[key].receivedAmount = parseFloat(0);
		self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
		//self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].dueAmount = parseFloat(checkAmt);
		   growl.error("You can modify upto "+parseFloat(checkAmt).toFixed(2) ,{
				title : ERROR_MSG
			});
		   $scope.calculateSummaryForChange();
			saveServiceInvoiceDetailsOutSide(key);
			return false;
			
			
		}
	
	
}

$scope.changeTDSPer = function(tdsPer){
	

	
	if(tdsPer == undefined || tdsPer == '' || tdsPer == null){
		tdsPer = 0;
	}
	
	if(parseFloat(tdsPer) > 100){
		growl.error("TDS percentage should be equal or less than 100%. ",{
			title : ERROR_MSG
		});
		$scope.tdsPercentage = 0;
		return false;
	}else{
		if(parseFloat(tdsPer) == 100){
			$scope.disAllowedPop = 0;
		}
	}
}

$scope.changeDisallowedAmt = function(dissAll){
	
	if($scope.tdsPercentage == undefined || $scope.tdsPercentage == '' || $scope.tdsPercentage == null){
		$scope.tdsPercentage = 0;
	}
	
	if(dissAll == undefined || dissAll == '' || dissAll == null){
		dissAll = 0;
	}
	
	if($scope.tdsPercentage == 0){
		if(parseFloat(dissAll) > parseFloat($scope.totalPopupAmount)){
					
					growl.error("You can apply disallowed on "+parseFloat($scope.totalPopupAmount).toFixed(2),{
		    			title : ERROR_MSG
		    		});
					$scope.disAllowedPop = 0;
					return false;
				}
	}else{
		
		var tmpTds = ((parseFloat($scope.totalPopupAmount))*$scope.tdsPercentage/100).toFixed(2);
		
		var diffTmp = parseFloat($scope.totalPopupAmount) - parseFloat(tmpTds);
		
		if(parseFloat(dissAll) > parseFloat(diffTmp)){
			
			growl.error("You can apply disallowed on "+parseFloat(diffTmp).toFixed(2),{
    			title : ERROR_MSG
    		});
			$scope.disAllowedPop = 0;
			return false;
		}
		
	}
}


var calculateSummaryAndValidateForSDS = function(invdtl,type,key){
	
	var otherDeductionAmount =parseFloat(invdtl.tdsAmount) + parseFloat($rootScope.parseFloatNum(invdtl.disallowedAmount)) + parseFloat(invdtl.otherDeduction);
	
	if(parseFloat(invdtl.total) > parseFloat(otherDeductionAmount))
		{
			var diff = parseFloat(invdtl.total) - parseFloat(otherDeductionAmount);
			if(parseFloat(invdtl.securityDepositSettlement) > parseFloat(diff)){
				growl.error("You can apply on "+parseFloat(diff).toFixed(2),{
	    			title : ERROR_MSG
	    		});
				return false;
			}else{
				var diff = parseFloat(diff) - parseFloat(invdtl.securityDepositSettlement);
				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
				 $scope.calculateSummaryForChange();
			}
			
		}
	
}
		
	    //Old - 16-4-2020 SSG
	    /*var calculateSummary = function(){
	    	//calculate summary
	    	var receivedAmount = 0,tds = 0,otherDeduction = 0,disallowedAmount = 0,total = 0;
	    	self.model.invoiceDetails.forEach(function(value,index){
	    		if(value.isSelected){
		    		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(value.receivedAmount));
		    		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(value.tdsAmount));
		    		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(value.otherDeduction));
		    		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(value.disallowedAmount));
		    		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(value.total));
	    		}
	    	});
	    	self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(receivedAmount),tds:$rootScope.parseFloatNum(tds),total:$rootScope.parseFloatNum(total),
	    									otherDeduction:$rootScope.parseFloatNum(otherDeduction),disallowedAmount:$rootScope.parseFloatNum(disallowedAmount)};
	    	
	    	//added for payment collection
	    	$scope.dueAmount = $rootScope.parseFloatNum(total);
	    	$rootScope.setFirstTabLevel(2);
	    }*/
	    
/*var calculateSummary = function(){
	//calculate summary
	
	var receivedAmount = 0,tds = 0,otherDeduction = 0,disallowedAmount = 0,total = 0;
	
	self.model.invoiceDetails.forEach(function(value,index){
		if(value.isSelected){
			value.receivedAmount = value.outstanding;
			value.total = value.outstanding;
			value.tdsAmount = 0.0;
			value.otherDeduction = 0.0;
			value.disallowedAmount = 0.0;
    		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(value.receivedAmount));
    		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(value.tdsAmount));
    		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(value.otherDeduction));
    		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(value.disallowedAmount));
    		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(value.total));
		}
	});
	
	self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(receivedAmount),tds:$rootScope.parseFloatNum(tds),total:$rootScope.parseFloatNum(total),
									otherDeduction:$rootScope.parseFloatNum(otherDeduction),disallowedAmount:$rootScope.parseFloatNum(disallowedAmount)};
	
	//added for payment collection
	$scope.dueAmount = $rootScope.parseFloatNum(total);
	$rootScope.setFirstTabLevel(2);
}*/

var SelectAllItems = function() {
	$rootScope.startLoader();
	 self.model.invoiceDetails = [];
	 self.model.invoiceDetailsOld = [];
	self.model.invoices.forEach(function(invdtl ,index) {
		if(invdtl.isSelected == true){
 		//get invoice details
 		self.model.tdsAmountGlobal = 0.00;
	    	var URI = BASE_URL + ROOT_URL + "/api/billing/invoicedtls/invoice/"+invdtl.companyInvoiceId;
	    	$rootScope.startLoader();
		    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					
					 self.model.invoiceDetails = self.model.invoiceDetails.concat(response.data.listObject);
					 self.model.invoiceDetailsOld = self.model.invoiceDetails.concat(response.data.listObject);
					 angular.forEach(self.model.invoiceDetailsOld,function(value,key){
						 angular.forEach(value.tCompanyInvoiceServiceDetailsDtoOld,function(value1,key1){
							 value1.otherDeductionOld = 0;
							 value1.otherDeductionOld = value1.otherDeduction;
						 });
					 });
					 
					 if (self.model.selectAllRows) {
							self.model.SelectedRequestIds = [];
							self.model.invoiceDetails.forEach(function(invdtl ,index) {
									invdtl.isSelected = true;
									invdtl.popUpDetailsSaved = false; 
									self.model.SelectedRequestIds.push(invdtl.id);
									$scope.settleFlag = true;
									calculateSummary(invdtl,index);
									
							});
						} else {
							self.model.SelectedRequestIds = [];
							self.model.invoiceDetails.forEach(function(invdtl,index) {
								invdtl.isSelected = false;
								invdtl.popUpDetailsSaved = false; 
								calculateSummary(invdtl,index);
							});
							$scope.settleFlag = false;
						}
					 
					 var flgSet = false;
				    	var v = 1;
				    	self.model.invoiceDetails.forEach(function(value,index){
				    		if(value.isSelected){
				        		flgSet = true;
				    		}
				    		 if(v == self.model.invoiceDetails.length){
				    			if(flgSet == false){
				    				$scope.settleFlag = false;
				    			}else{
				    				$scope.settleFlag = true;
				    			}
				    		}
				    		v++;
				    	});
				}
				$timeout( function(){
				$rootScope.stopLoader();
				},1000);
			});
 	
		}
	});
	$timeout( function(){
		if (self.model.selectAllRows) {
			self.model.SelectedRequestIds = [];
			self.model.invoiceDetails.forEach(function(invdtl ,index) {
					invdtl.isSelected = true;
					invdtl.popUpDetailsSaved = false; 
					self.model.SelectedRequestIds.push(invdtl.id);
					$scope.settleFlag = true;
					calculateSummary(invdtl,index);
					
			});
		} else {
			self.model.SelectedRequestIds = [];
			self.model.invoiceDetails.forEach(function(invdtl,index) {
				invdtl.isSelected = false;
				invdtl.popUpDetailsSaved = false; 
				calculateSummary(invdtl,index);
			});
			$scope.settleFlag = false;
		}
  },1000);
		
	
}
	
		
	    var calculateSummary = function(item,idx){
	    	
	    	if(item.isSelected == false ){
	    		self.model.selectAllRows = false;
	    	}
	    	//calculate summary
	     
	    	
	    	if(item.isSelected){
	    		self.model.currentInvoiceIndexOS = idx;
	    		self.model.invoiceDetails[idx].popUpDetailsSaved = false; 
    			self.model.invoiceDetails[idx].receivedAmount = parseFloat(self.model.invoiceDetails[idx].outstanding).toFixed(2); 
    			self.model.invoiceDetails[idx].total = parseFloat(self.model.invoiceDetails[idx].outstanding).toFixed(2);
    			self.model.invoiceDetails[idx].tdsAmount = 0.0;
    			self.model.invoiceDetails[idx].otherDeduction = 0.0;
    			self.model.invoiceDetails[idx].disallowedAmount = 0.0;
    			$scope.checkAllOrder = true;
    			$scope.totalPopupAmount  = 0;
    			self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
    				elem.isSelected = true;
    				$scope.totalPopupAmount = parseFloat($scope.totalPopupAmount)+parseFloat(elem.creditPayableAmount);
    			});
    			
    			
    			$scope.calculateSummaryForChange();
				
				saveServiceInvoiceDetailsOutSide(idx);
    			
/*	    		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(self.model.invoiceDetails[idx].receivedAmount));
	    		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(self.model.invoiceDetails[idx].tdsAmount));
	    		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(self.model.invoiceDetails[idx].otherDeduction));
	    		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(self.model.invoiceDetails[idx].disallowedAmount));
	    		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(self.model.invoiceDetails[idx].total));
*/    		}else{
    			self.model.invoiceDetails[idx].receivedAmount = 0.0;
    			self.model.invoiceDetails[idx].total = 0.0;
    			self.model.invoiceDetails[idx].tdsAmount = 0.0;
    			self.model.invoiceDetails[idx].otherDeduction = 0.0;
    			self.model.invoiceDetails[idx].disallowedAmount = 0.0;
    			self.model.invoiceDetails[idx].popUpDetailsSaved = false; 
    			$scope.checkAllOrder = false;
    			
    			angular.forEach(self.model.invoiceDetailsOld,function(value,key){
					 angular.forEach(value.tCompanyInvoiceServiceDetailsDtoOld,function(value1,key1){
						 value1.otherDeduction = value1.otherDeductionOld;
					 });
				 });
    			
    			self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto = self.model.invoiceDetailsOld[idx].tCompanyInvoiceServiceDetailsDtoOld;
    			self.model.currentInvoiceIndexOS = null;
    			
    			//companyInvoiceDetailId
    			//self.model.recvAndDisallAmountDetailsFinal
    			
    			self.model.recvAndDisallAmountDetailsFinal.forEach(function(value,index){
        			if(value.companyInvoiceDetailId == self.model.invoiceDetails[idx].invoiceDetailId){
        				self.model.recvAndDisallAmountDetailsFinal.splice(index,1);
        			}
        		});
    			$scope.totalPopupAmount  = 0;
    			
    		}
	    	var receivedAmount = 0,tds = 0,otherDeduction = 0,disallowedAmount = 0,total = 0;
	    	
	    	var flgSet = false;
	    	var v = 1;
	    	self.model.invoiceDetails.forEach(function(value,index){
	    		if(value.isSelected){
	    		/*	value.receivedAmount = value.outstanding;
	    			value.total = value.outstanding;
	    			value.tdsAmount = 0.0;
	    			value.otherDeduction = 0.0;
	    			value.disallowedAmount = 0.0;*/
	        		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(value.receivedAmount));
	        		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(value.tdsAmount));
	        		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(value.otherDeduction));
	        		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(value.disallowedAmount));
	        		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(value.total));
	        		flgSet = true;
	    		}
	    		 if(v == self.model.invoiceDetails.length){
	    			if(flgSet == false){
	    				$scope.settleFlag = false;
	    			}else{
	    				$scope.settleFlag = true;
	    			}
	    		}
	    		v++;
	    	});
	    	
	    	self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(receivedAmount),tds:$rootScope.parseFloatNum(tds),total:$rootScope.parseFloatNum(total),
	    									otherDeduction:$rootScope.parseFloatNum(otherDeduction),disallowedAmount:$rootScope.parseFloatNum(disallowedAmount)};
	    	self.model.tdsAmountGlobal = self.model.invoiceDetailsSummary.tds;
	    	//added for payment collection
	    	$scope.dueAmount = $rootScope.parseFloatNum(total);
	    	$rootScope.setFirstTabLevel(2);
	    	var countCheck = 0;
	    	self.model.invoiceDetails.forEach(function(invdtl,index) {
				if(invdtl.isSelected ==  true){
					countCheck++;
				}
			});
	    	
	    	if(self.model.invoiceDetails.length ==countCheck ){
	    		self.model.selectAllRows = true;
	    	}
	    	
	    }
        $scope.saveBtnDisabled=false;
	    var settleCompanyInvoice = function()
	    {
            $scope.saveBtnDisabled=true;
	    	$rootScope.startLoader();
	    	$scope.btnSettleDisabled = true;
	    	//$('#collectPaymentModal').modal('hide');
	    	var receiptDetails = [];
    		var receipt = {
	    				paymentEntitlementId:self.model.searchObj.companyTypeId, 
	    				payeeId:self.model.searchObj.companyId,
	    				associateCompanyId:self.model.searchObj.associateCompanyId,
	    				receiptAmount:self.model.invoiceDetailsSummary.receivedAmount,
	    				tdsAmount:self.model.invoiceDetailsSummary.tds,
	    				otherDeduction:self.model.invoiceDetailsSummary.otherDeduction,
	    				disallowedAmount:self.model.invoiceDetailsSummary.disallowedAmount,
	    				netReceiptAmount:self.model.invoiceDetailsSummary.receivedAmount,
	    				createdBy:self.model.userId,
	    				updatedBy:self.model.userId, 
	    				orgId:self.model.orgId, 
	    				unitId:self.model.unitId
    				};
    		
    		//receipt details
    		
    		self.model.recvAndDisallAmountDetailsFinal = [];
    		self.model.invoiceDetails.forEach(function(value,index){
    			if(value.isSelected){
	    			var temp = 
	    			   {netReceiptAmount:parseFloat(value.receivedAmount).toFixed(2),
	    					tdsAmount:parseFloat(value.tdsAmount).toFixed(2),
	    					otherDeduction:parseFloat(value.otherDeduction).toFixed(2),
	    					disallowedAmount:parseFloat(value.disallowedAmount).toFixed(2),
	    					companyInvoiceDetailId:value.companyInvoiceDetailId,
	    					totalAmount:parseFloat(value.total).toFixed(2),
	    					createdBy:self.model.userId,
	    					updatedBy:self.model.userId, 
	    					orgId:self.model.orgId, 
	    					unitId:self.model.unitId,
	    					billId:value.billId,
	    					saleId:value.saleId,
	    					outstandingAmt: value.saleId!=null && value.saleId>0 ? parseFloat(value.outstanding).toFixed(2):parseFloat(value.billOutStanging).toFixed(2)
	    					};
	    			if(value.otherDeduction == undefined || value.otherDeduction == null)
	    				{
	    				  temp.otherDeduction = parseFloat(0);
	    				}
	    			
	    			receiptDetails.push(temp);
	    			
	    			
	    			
	    			
	    			value.tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
	    				if(elem.isSelected){
	    					
	    					var amountObj = {
	    							receivedAmount:0,
	    							disallowedAmount:0,
	    							otherDeduction:0,
	    							serviceId:0,
	    							invoiceDetailId:0,
	    							compInvSerDetlsId:0,
	    							dueAmount:0
	    					};
	    					
	    				var tempObj=angular.copy(amountObj);
	    				tempObj.receivedAmount = parseFloat(elem.receivedAmount);
	    				tempObj.disallowedAmount = parseFloat(elem.disallowedAmount);
	    				tempObj.otherDeduction = parseFloat(elem.otherDeduction);
	    				tempObj.serviceId = elem.serviceId;
	    				tempObj.itemId = elem.itemId;
	    				tempObj.invoiceDetailId = elem.invoiceDetailId;
	    				tempObj.compInvSerDetlsId = elem.compInvSerDetlsId;
	    				tempObj.dueAmount = elem.dueAmount;
	    				tempObj.tdsAmount = elem.tdsAmount;
	    				tempObj.saleId = elem.saleId;
	    				tempObj.orderDetailsId = elem.orderDetailsId;
	    				tempObj.tSaleDetailsId = elem.tSaleDetailsId;
	    				tempObj.billingMasterId = elem.billingMasterId;
	    				
	    				self.model.recvAndDisallAmountDetailsFinal.push(tempObj);
	    					
	    				}
	    			});
	    			
	    			
    			}
    		});
    		receipt.receiptDetails = receiptDetails;
    		
    		//calculate collected amount to update due amount of company invoice
    		var invoiceIds = [];
    		var companyRecptRecAmount = {};
    		self.model.invoiceDetails.forEach((value) => {
    			if(value.isSelected && invoiceIds.indexOf(value.companyInvoiceId) == -1)
    				invoiceIds.push(value.companyInvoiceId);
    		});
    		
    		invoiceIds.forEach((id) => {
    			var temp = {}, total = 0;
    			var invDtls = self.model.invoiceDetails.filter(obj => {return obj.companyInvoiceId == id && obj.isSelected == true})
    			invDtls.forEach(function(value,index){
    				total = parseFloat(total) + (parseFloat(value.receivedAmount)+parseFloat(value.tdsAmount)+parseFloat(value.disallowedAmount)+parseFloat(value.otherDeduction));
    			});
    			companyRecptRecAmount[id] = total;
    		});
    		
    		//payment details
    		var totalCollected = 0;
    		$scope.listPaymentDetails.forEach((elem) => {
    			totalCollected = parseFloat(totalCollected) + parseFloat(elem.amount);
    			elem.createdBy = 1;
			});
    		var tmpTo = parseFloat(totalCollected) + parseFloat($scope.consumedAmt);
    		if( parseFloat(tmpTo) != parseFloat(self.model.invoiceDetailsSummary.receivedAmount)){
                $scope.saveBtnDisabled=false;
    			growl.warning("Collected amount total amount differs please recheck" ,{
					title : WARNING_MSG
				});
    			$rootScope.stopLoader();
    			$scope.btnSettleDisabled = false;
    			return;
    		}
    		

			$scope.finalAvailableDeposits = [];
			var i = 0;
			angular.forEach($scope.companyAdvanced,function(value,key){
				if(value.depositeFlag == 'Y')
					{
					var newArr = {};
					newArr.availableDeposit = $scope.companyAdvanced[i].balanceAmount;
					//newArr.amount = $scope.companyAdvanced[i].amount;
					newArr.companyReceiptsExpenseId =$scope.companyAdvanced[i].companyReceiptsExpenseId;
					newArr.organizationId  =  $scope.organizationId;
					newArr.unitId = $scope.unitId;
					newArr.consumedAmount =(parseFloat($scope.companyAdvanced[i].consumedAmount).toFixed(2) - $scope.companyAdvanced[i].oldConsumedAmount);
					newArr.createdBy = $scope.userId;
					$scope.finalAvailableDeposits.push(newArr);
					//$scope.finalAvailableDeposits.push($scope.availableDeposits[i]);
					}
				i++;
				
			});
			//console.log("$scope.finalAvailableDeposits",$scope.finalAvailableDeposits);
		//	console.log("$scope.listPaymentDetails",$scope.listPaymentDetails);
			
			
    		
    		var param = {
    						companyReceipt:receipt ,
    						companyRecptRecAmount:companyRecptRecAmount, 
    						paymentDetails:$scope.listPaymentDetails,
    						recvAndDisallAmountDetails:self.model.recvAndDisallAmountDetailsFinal,
    						selfPayeeId : $scope.selfPayeeId,
    						paymentDepositDetailsList : $scope.finalAvailableDeposits,
    		};
    		console.log("param",param);
    		//return false;
    		$rootScope.startLoaderPopup();
    		var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/settle";
    		BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if(response.data.status == "success")
				{
					angular.element('#collectPaymentModal').modal('hide');
					$rootScope.stopLoaderPopup();
					init();
				}
				else if(response.data.status == "error")
					{
					 $rootScope.stopLoaderPopup();
					}
				
			});
    		
	    }
	    
	    $scope.getTotalAmount = function()
	    {
	    	$scope.saveBtnDisabled=false;
	    	 var URI = BASE_URL + ROOT_URL + "/api/billing/companyReceipts/companyReceiptsByCompany";
 		    var data = {
 		    		
 		    		companyId : self.model.searchObj.companyId,
 			        insuranceCompanyId : self.model.searchObj.associateCompanyId
 		    }
 		    
 		    BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 $scope.companyAdvanced = [];
						 $scope.companyAdvanced = response.data.listObject;
						 console.log("ss",$scope.companyAdvanced)
						
					}
				});
	    	if($scope.companyAdvanced.length > 0){
	    		angular.forEach($scope.companyAdvanced,function(value,key){
		    		value.depositeFlag = 'N';
		    	});	
	    	}
	    	$scope.consumedAmt = parseFloat(0);
	    	$scope.paymentDetails.chequeAmount = self.model.invoiceDetailsSummary.receivedAmount;
	    	$scope.mpesaObject.mpesaAmount = self.model.invoiceDetailsSummary.receivedAmount;
	    	$scope.eTransferObj.transferAmount = self.model.invoiceDetailsSummary.receivedAmount;
	    	$scope.listPaymentDetails = [];
	    	$scope.btnSettleDisabled = false;
	    	angular.element('#collectPaymentModal').modal('show');
	    }
	    
	    $scope.addPaymentDetails = function(){
	    	//$scope.isBillList = 1;
	    	if( $scope.paymentDetails.paymentModeId == 5 && $scope.mpesaRigths == true){
	    		if( $scope.mpesaObject.transactionId == null){
				growl.warning("Please Enter Transaction Id !!",{
				title : WARNING_MSG
				});
				return false;
	    		}
			}
	    	
			paymentCollFactory.addPaymentDetails($scope);
			
			$scope.mpesaObject.mpesaAmount = '';
	    	$scope.eTransferObj.transferAmount = '';
	    	$scope.calculateTotalDueRefAmount();
	    	
		}
		$scope.removePaymentDetails = function(){
			paymentCollFactory.removePaymentDetails($scope);
			$scope.calculateTotalDueRefAmount();
		}
	    
		$scope.setPaymentModeId=function(paymentModeId){
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
		
		$scope.openDatePicker = function() {
			$scope.datepicker.opened = true;
		};
		$scope.openDatePickerTo = function() {
			$scope.datepickerTo.opened = true;
		};
		
		$scope.validCollectedAmount = function(index){
			var total = $rootScope.parseFloatNum(self.model.invoiceDetailsSummary.receivedAmount);
			var collected = 0;
			$scope.listPaymentDetails.forEach((elem) => {collected = parseFloat(collected) + parseFloat(elem.amount)});
			if(collected > total){
				let due = parseFloat(total) - (parseFloat(collected) - parseFloat($scope.listPaymentDetails[index].amount));
				growl.error("Collected amount cannot be greater than total amount due amount is " + $rootScope.parseFloatNum(due),{
					title : ERROR_MSG
				});
				$scope.listPaymentDetails.splice(index,1);
				return;
			}
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
		
		
		var getTransactionTypes = function(){
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+self.model.orgId+"/"+self.model.unitId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
				//	$scope.transactionTypes = response.data.listObject;
					$scope.transactionTypesList = response.data.listObject;
				}
			});
		}
		
		//Old - 16-4-2020
		/*var getCompanyInvoiceServiceDetails = function(invoiceDtlId,index){
			$rootScope.startLoader();
			self.model.currentInvoiceIndex = index;
			var URI = BASE_URL + ROOT_URL + "/api/billing/getCompanyInvServDetails?invoiceDetailId="+invoiceDtlId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					self.model.companyIvoiceServiceDetails = response.data.listObject;
					self.model.companyIvoiceServiceDetails.forEach((elem) => {
						elem.tdsAmount = parseFloat(0);
						elem.receivedAmount = parseFloat(0);
						elem.disallowedAmount = parseFloat(0);
						$scope.tdsPercentage = parseFloat(0);
						elem.isSelected = false;
						$scope.checkAllOrder = false;
					});
				}
				$rootScope.stopLoader();
			});
			
		}*/
		
		
		var getCompanyInvoiceServiceDetails = function(invoiceObj,index){
			
			$scope.aplysTDSHhs = true;
			$rootScope.startLoader();
			$scope.tdsPercentage = 0;
			$scope.disAllowedPop = 0;
			$scope.checkedAllIndex = index;
//			$scope.checkAllOrder = false;
			self.model.currentInvoiceIndex = index;
			saveServiceInvoiceDetailsOutSide(index);
			/*self.model.companyIvoiceServiceDetails = [];
			self.model.companyIvoiceServiceDetails = invoiceObj.tCompanyInvoiceServiceDetailsDto;*/
			$rootScope.stopLoader();
			
		}
		//OLd - 17-4-2020
		/*$scope.checkAll = function()
		{
			if($scope.checkAllOrder){
			self.model.companyIvoiceServiceDetails.forEach((elem) => {
				elem.isSelected = true;
			});
			}
			else
				{
					self.model.companyIvoiceServiceDetails.forEach((elem) => {
						elem.isSelected = false;
						elem.tdsAmount = parseFloat(0);
						elem.disallowedAmount = parseFloat(0);
						elem.receivedAmount = parseFloat(0);
						 //self.model.companyIvoiceServiceDetails[index].dueAmount = self.model.companyIvoiceServiceDetails[index].creditPayableAmount;
						elem.companyIvoiceServiceDetails = elem.creditPayableAmount;
					});
				}
		}*/

		$scope.totalPopupAmount  = 0;
		$scope.checkAll = function()
		{
			$scope.tdsPercentage = 0;
			$scope.disAllowedPop = 0;
			
			if($scope.checkAllOrder){
				$scope.totalPopupAmount  = 0;
			self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
				elem.isSelected = true;
				elem.receivedAmount = elem.creditPayableAmount; 
	            elem.tdsAmount = parseFloat(0);
	            elem.disallowedAmount = parseFloat(0);
	            elem.otherDeduction = parseFloat(0);
	            elem.dueAmount = parseFloat(0);
				$scope.totalPopupAmount = parseFloat($scope.totalPopupAmount)+parseFloat(elem.creditPayableAmount);
			});
			}
			else
				{
				$scope.totalPopupAmount  = 0;
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
						elem.isSelected = false;
						elem.tdsAmount = parseFloat(0);
						elem.disallowedAmount = parseFloat(0);
						elem.receivedAmount = parseFloat(0);
						elem.otherDeduction = parseFloat(0);
						elem.dueAmount = elem.creditPayableAmount;
						 //self.model.companyIvoiceServiceDetails[index].dueAmount = self.model.companyIvoiceServiceDetails[index].creditPayableAmount;
						elem.companyIvoiceServiceDetails = elem.creditPayableAmount;
					});
				}
		}
		
        		$scope.checkAll = function()
        		{
        			$scope.tdsPercentage = 0;
        			$scope.disAllowedPop = 0;
        			var norArr = self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto;
                    var i=0;

        			if($scope.checkedAllArr[self.model.currentInvoiceIndex]){
        				$scope.totalPopupAmount  = 0;
        				$scope.checkedAllArr[self.model.currentInvoiceIndex] = true;
        			self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
        				elem.isSelected = true;
        				elem.tdsAmount = norArr[i].tdsAmount;
                        elem.disallowedAmount = norArr[i].disallowedAmount;
                        elem.receivedAmount = norArr[i].creditPayableAmount;
                        elem.otherDeduction = norArr[i].otherDeduction;
                        elem.dueAmount = parseFloat(0);
        				$scope.totalPopupAmount = parseFloat($scope.totalPopupAmount)+parseFloat(elem.creditPayableAmount);
        				i++;
        			});
        			}
        			else
        				{
        				$scope.totalPopupAmount  = 0;
        				$scope.checkedAllArr[self.model.currentInvoiceIndex] = false;
        				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
        						elem.isSelected = false;
        						elem.tdsAmount = parseFloat(0);
        						elem.disallowedAmount = parseFloat(0);
        						elem.receivedAmount = parseFloat(0);
        						elem.otherDeduction = parseFloat(0);
        						elem.dueAmount = elem.creditPayableAmount;
        						 //self.model.companyIvoiceServiceDetails[index].dueAmount = self.model.companyIvoiceServiceDetails[index].creditPayableAmount;
        						elem.companyIvoiceServiceDetails = elem.creditPayableAmount;
        					});
        				}
        		}

		$scope.getUncheck = function(isSelected,index)
		{
			
			var norArr = self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto;
            			$scope.tdsPercentage = 0;
            			$scope.disAllowedPop = 0;
            			if(!isSelected)
            				{
            					$scope.totalPopupAmount = parseFloat($scope.totalPopupAmount)-parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].creditPayableAmount);
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].dueAmount = self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].creditPayableAmount;
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].tdsAmount = parseFloat(0);
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].disallowedAmount = parseFloat(0);
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(0);
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].otherDeduction = parseFloat(0);
            				}else{
            					$scope.totalPopupAmount = parseFloat($scope.totalPopupAmount)+parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].creditPayableAmount);
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].dueAmount = parseFloat(0);
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].tdsAmount = norArr[index].tdsAmount;
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].disallowedAmount = norArr[index].disallowedAmount;
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = norArr[index].creditPayableAmount;
            					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].otherDeduction = norArr[index].otherDeduction;
            				}


            var flag = false;
            for(var i=0; i<self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.length; i++){
                if(!self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[i].isSelected){
                    flag = true;
                    $scope.checkAllOrder = false;
                    $scope.checkedAllArr[self.model.currentInvoiceIndex] = false;
                    break;
                }
            }

            if(!flag){
                $scope.checkAllOrder = true;
                $scope.checkedAllArr[self.model.currentInvoiceIndex] = true;
            }


		}
		
		//OLD- 17-4-2020
		/*var saveServiceInvoiceDetails = function(){
			$rootScope.startLoader();
			var totalRec = 0;
			var totalDisall = 0;
			var totalTDS = 0;
			var amountObj = {
					receivedAmount:0,
					disallowedAmount:0,
					serviceId:0,
					invoiceDetailId:0,
					compInvSerDetlsId:0,
					dueAmount:0
			};
			self.model.recvAndDisallAmountDetails = [];
			if(self.model.companyIvoiceServiceDetails.length > 0){
				var bmId = self.model.companyIvoiceServiceDetails[0].billingMasterId;
				var testRecSerDet = [];
				testRecSerDet = self.model.recvAndDisallAmountDetailsFinal;
				self.model.recvAndDisallAmountDetailsFinal = [];
				var i=0;
				angular.forEach(testRecSerDet, function (value, key) {
					if(value.billingMasterId != bmId)
						{
						 self.model.recvAndDisallAmountDetailsFinal.push(value);
						
						}
					i++;
				});
			}
			self.model.companyIvoiceServiceDetails.forEach((elem) => {
				if(elem.isSelected){
					
				totalRec = parseFloat(totalRec) + ((elem.receivedAmount == null || elem.receivedAmount == "" || isNaN(elem.receivedAmount)) ? 0 : parseFloat(elem.receivedAmount));
				totalDisall = parseFloat(totalDisall) + (elem.disallowedAmount == null ||elem.disallowedAmount == "" || (isNaN(elem.disallowedAmount)) ? 0 : parseFloat(elem.disallowedAmount));
				totalTDS = parseFloat(totalTDS) + (elem.tdsAmount == null ||elem.tdsAmount == "" || (isNaN(elem.tdsAmount)) ? 0 : parseFloat(elem.tdsAmount));
				var tempObj=angular.copy(amountObj);
				tempObj.receivedAmount = parseFloat(elem.receivedAmount);
				tempObj.disallowedAmount = parseFloat(elem.disallowedAmount);
				tempObj.serviceId = elem.serviceId;
				tempObj.itemId = elem.itemId;
				tempObj.invoiceDetailId = elem.invoiceDetailId;
				tempObj.compInvSerDetlsId = elem.compInvSerDetlsId;
				tempObj.dueAmount = elem.dueAmount;
				tempObj.tdsAmount = elem.tdsAmount;
				tempObj.saleId = elem.saleId;
				tempObj.orderDetailsId = elem.orderDetailsId;
				tempObj.tSaleDetailsId = elem.tSaleDetailsId;
				tempObj.billingMasterId = elem.billingMasterId;
				self.model.recvAndDisallAmountDetails.push(tempObj);
				console.log("scsdcdsc",tempObj);
				
				self.model.recvAndDisallAmountDetailsFinal.push(tempObj);
					
				}
			});
			var invoice = self.model.invoiceDetails[self.model.currentInvoiceIndex];
			invoice.receivedAmount = $rootScope.parseFloatNum(totalRec);
			invoice.disallowedAmount = $rootScope.parseFloatNum(totalDisall);
			invoice.tdsAmount = $rootScope.parseFloatNum(totalTDS);
			self.model.currentInvoiceIndex = null;
			self.model.calculateSummaryAndValidate(invoice,'disallowedAmount');
			self.model.calculateSummaryAndValidate(invoice,'receivedAmount');
			$rootScope.stopLoader();
		}*/
		
var saveServiceInvoiceDetailsOutSide = function(idx){
			
			$rootScope.startLoader();
			var totalRec = 0;
			var totalDisall = 0;
			var totalOtherDeduction = 0;
			var totalTDS = 0;
			var totalAmt = 0;
			
			
			var amountObj = {
					receivedAmount:0,
					disallowedAmount:0,
					otherDeduction:0,
					serviceId:0,
					invoiceDetailId:0,
					compInvSerDetlsId:0,
					dueAmount:0
			};
			
			
			self.model.recvAndDisallAmountDetails = [];
			
			if(self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto.length > 0){
				var bmId = self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto[0].billingMasterId;
				var testRecSerDet = [];
				testRecSerDet = self.model.recvAndDisallAmountDetailsFinal;
				self.model.recvAndDisallAmountDetailsFinal = [];
				var i=0;
				angular.forEach(testRecSerDet, function (value, key) {
					if(value.billingMasterId != bmId)
						{
						 self.model.recvAndDisallAmountDetailsFinal.push(value);
						
						}
					i++;
				});
			}
			self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
				if(elem.isSelected){
					
				totalRec = parseFloat(totalRec) + ((elem.receivedAmount == null || elem.receivedAmount == "" || isNaN(elem.receivedAmount)) ? 0 : parseFloat(elem.receivedAmount));
				totalDisall = parseFloat(totalDisall) + (elem.disallowedAmount == null ||elem.disallowedAmount == "" || (isNaN(elem.disallowedAmount)) ? 0 : parseFloat(elem.disallowedAmount));
				totalTDS = parseFloat(totalTDS) + (elem.tdsAmount == null ||elem.tdsAmount == "" || (isNaN(elem.tdsAmount)) ? 0 : parseFloat(elem.tdsAmount));
				totalOtherDeduction = parseFloat(totalOtherDeduction) + (elem.otherDeduction == null ||elem.otherDeduction == "" || (isNaN(elem.otherDeduction)) ? 0 : parseFloat(elem.otherDeduction));
				//totalAmt = parseFloat(totalRec)+ parseFloat(totalDisall) + parseFloat(totalTDS) + parseFloat(totalOtherDeduction);
				var tempObj=angular.copy(amountObj);
				tempObj.receivedAmount = parseFloat(elem.receivedAmount);
				tempObj.disallowedAmount = parseFloat(elem.disallowedAmount);
				tempObj.otherDeduction = parseFloat(elem.otherDeduction);
				tempObj.serviceId = elem.serviceId;
				tempObj.itemId = elem.itemId;
				tempObj.invoiceDetailId = elem.invoiceDetailId;
				tempObj.compInvSerDetlsId = elem.compInvSerDetlsId;
				tempObj.dueAmount = elem.dueAmount;
				tempObj.tdsAmount = elem.tdsAmount;
				tempObj.saleId = elem.saleId;
				tempObj.orderDetailsId = elem.orderDetailsId;
				tempObj.tSaleDetailsId = elem.tSaleDetailsId;
				tempObj.billingMasterId = elem.billingMasterId;
				self.model.recvAndDisallAmountDetails.push(tempObj);
				console.log("scsdcdsc",tempObj);
				
				self.model.recvAndDisallAmountDetailsFinal.push(tempObj);
					
				}
			});
			
			
			$scope.totalPopupAmount  = 0;
			self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
				//elem.isSelected = true;
				if(elem.isSelected){
				$scope.totalPopupAmount = parseFloat($scope.totalPopupAmount)+parseFloat(elem.creditPayableAmount);
				}
			});

			var invoice = self.model.invoiceDetails[idx];
			//invoice.receivedAmount = $rootScope.parseFloatNum(totalRec);
			//invoice.disallowedAmount = $rootScope.parseFloatNum(totalDisall);
			//invoice.tdsAmount = $rootScope.parseFloatNum(totalTDS);
			//invoice.otherDeduction = $rootScope.parseFloatNum(totalOtherDeduction);
			//invoice.total = $rootScope.parseFloatNum(totalAmt);
			$scope.calculateSummaryForChange();
			$rootScope.stopLoader();
		}
		

        var clrServiceInvoiceDetails = function(){
        	if($scope.aplysTDSHhs == false){
        		$scope.tdsPercentage = 0.00;
        		$scope.disAllowedPop = 0.00;
        		$scope.applyTDS();
        	}
        }
		var saveServiceInvoiceDetails = function(){
			$scope.aplysTDSHhs = true;
			self.model.invoiceDetails[self.model.currentInvoiceIndex].popUpDetailsSaved = true; 
			$rootScope.startLoader();
			var totalRec = 0;
			var totalDisall = 0;
			var totalOtherDeduction = 0;
			var totalTDS = 0;
			var totalAmt = 0;
			
			
			var amountObj = {
					receivedAmount:0,
					disallowedAmount:0,
					otherDeduction:0,
					serviceId:0,
					invoiceDetailId:0,
					compInvSerDetlsId:0,
					dueAmount:0
			};
			
			
			self.model.recvAndDisallAmountDetails = [];
			
			if(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.length > 0){
				var bmId = self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[0].billingMasterId;
				var testRecSerDet = [];
				testRecSerDet = self.model.recvAndDisallAmountDetailsFinal;
				self.model.recvAndDisallAmountDetailsFinal = [];
				var i=0;
				angular.forEach(testRecSerDet, function (value, key) {
					if(value.billingMasterId != bmId)
						{
						 self.model.recvAndDisallAmountDetailsFinal.push(value);
						
						}
					i++;
				});
			}
			self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
				if(elem.isSelected){
					
				totalRec = parseFloat(totalRec) + ((elem.receivedAmount == null || elem.receivedAmount == "" || isNaN(elem.receivedAmount)) ? 0 : parseFloat(elem.receivedAmount));
				totalDisall = parseFloat(totalDisall) + (elem.disallowedAmount == null ||elem.disallowedAmount == "" || (isNaN(elem.disallowedAmount)) ? 0 : parseFloat(elem.disallowedAmount));
				totalTDS = parseFloat(totalTDS) + (elem.tdsAmount == null ||elem.tdsAmount == "" || (isNaN(elem.tdsAmount)) ? 0 : parseFloat(elem.tdsAmount));
				totalOtherDeduction = parseFloat(totalOtherDeduction) + (elem.otherDeduction == null ||elem.otherDeduction == "" || (isNaN(elem.otherDeduction)) ? 0 : parseFloat(elem.otherDeduction));
				totalAmt = parseFloat(totalRec)+ parseFloat(totalDisall) + parseFloat(totalTDS) + parseFloat(totalOtherDeduction);
				var tempObj=angular.copy(amountObj);
				tempObj.receivedAmount = parseFloat(elem.receivedAmount);
				tempObj.disallowedAmount = parseFloat(elem.disallowedAmount);
				tempObj.otherDeduction = parseFloat(elem.otherDeduction);
				tempObj.serviceId = elem.serviceId;
				tempObj.itemId = elem.itemId;
				tempObj.invoiceDetailId = elem.invoiceDetailId;
				tempObj.compInvSerDetlsId = elem.compInvSerDetlsId;
				tempObj.dueAmount = elem.dueAmount;
				tempObj.tdsAmount = elem.tdsAmount;
				tempObj.saleId = elem.saleId;
				tempObj.orderDetailsId = elem.orderDetailsId;
				tempObj.tSaleDetailsId = elem.tSaleDetailsId;
				tempObj.billingMasterId = elem.billingMasterId;
				self.model.recvAndDisallAmountDetails.push(tempObj);
				console.log("scsdcdsc",tempObj);
				
				self.model.recvAndDisallAmountDetailsFinal.push(tempObj);
					
				}
			});
			var invoice = self.model.invoiceDetails[self.model.currentInvoiceIndex];
			invoice.receivedAmount = $rootScope.parseFloatNum(totalRec);
			invoice.disallowedAmount = $rootScope.parseFloatNum(totalDisall);
			invoice.tdsAmount = $rootScope.parseFloatNum(totalTDS);
			invoice.otherDeduction = $rootScope.parseFloatNum(totalOtherDeduction);
			//invoice.total = invoice.billOutStanging; //added HIS-27273 Transaction Details Not Showing On Settled Invoices After Applying Amount Wise Discount From IPD Charges  ( Check Attached Video )  commeted by confirming vishal kale;
			invoice.total = $rootScope.parseFloatNum(totalAmt);
			self.model.currentInvoiceIndex = null;
			$scope.calculateSummaryForChangeSave();
			$rootScope.stopLoader();
		}
		
		var validateReceiveAmount = function(item,index){
			
			if(item.receivedAmount == '' || item.receivedAmount == undefined || item.receivedAmount == null){
				item.receivedAmount = parseFloat(0);
			}
			
			
			var checkAmt = parseFloat(item.creditPayableAmount) - (parseFloat(item.tdsAmount) + parseFloat(item.disallowedAmount) + parseFloat(item.otherDeduction));
			
			if(parseFloat(checkAmt) >= parseFloat(item.receivedAmount)){
				
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].dueAmount = parseFloat(checkAmt) - parseFloat(item.receivedAmount);
			}
			else
				{
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(0);
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].dueAmount = parseFloat(checkAmt);
				  
				   growl.error("You can modify upto "+parseFloat(checkAmt).toFixed(2) ,{
						title : ERROR_MSG
					});
					return;
					
				}
			
		}
		
var validateDisallAmount = function(item,index){
	
	if(item.disallowedAmount == '' || item.disallowedAmount == undefined || item.disallowedAmount == null){
		item.disallowedAmount = parseFloat(0);
	}
			
var otherDeductionAmount =parseFloat(item.tdsAmount) + parseFloat($rootScope.parseFloatNum(item.otherDeduction));
	
	if(parseFloat(item.creditPayableAmount) > parseFloat(otherDeductionAmount))
		{
			var diff = parseFloat(item.creditPayableAmount) - parseFloat(otherDeductionAmount) - parseFloat(item.dueAmount);
			if(parseFloat(item.disallowedAmount) > parseFloat(diff).toFixed(2)){
				growl.error("You can apply disallowed amt on "+parseFloat(diff).toFixed(2),{
	    			title : ERROR_MSG
	    		});
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].disallowedAmount = 0;
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(diff).toFixed(2);
				return false;
			}else{
				var diff = parseFloat(diff) - parseFloat(item.disallowedAmount);
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(diff).toFixed(2);
				 //$scope.calculateSummaryForChange();
			}
			
		}else{
			self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].disallowedAmount = 0;
		}
			
		}
//OLD - 17-4-2020
/*var validateTDSAmount = function(dueAmt,tdsAmt,index){
	
	var tmp = parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount) + parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].tdsAmount);
	if(parseFloat(disallAmt) <= tmp){
		//var recAmt = parseFloat(self.model.companyIvoiceServiceDetails[index].receivedAmount) - parseFloat(self.model.companyIvoiceServiceDetails[index].disallowedAmount);
		var recAmt = parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].creditPayableAmount) - parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].tdsAmount) - parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].disallowedAmount);
		self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(recAmt).toFixed(2);
		
		var dueAmt = parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].creditPayableAmount) -  parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].tdsAmount) - parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].disallowedAmount) - parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount);
		self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].dueAmount = parseFloat(dueAmt);
	}
	else
		{
		
		self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].disallowedAmount = parseFloat(0);
		growl.warning("Disallowed amount should not greater than received amount." ,{
			title : WARNING_MSGx
		});
		return;

		}
	
}*/
	var validateTDSAmount = function(item,index){
		
		if(item.tdsAmount == '' || item.tdsAmount == undefined || item.tdsAmount == null){
			item.tdsAmount = parseFloat(0);
		}
		
		
		var otherDeductionAmount =parseFloat(item.otherDeduction) + parseFloat($rootScope.parseFloatNum(item.disallowedAmount));
		
		if(parseFloat(item.creditPayableAmount) > parseFloat(otherDeductionAmount))
		{
			var diff = parseFloat(item.creditPayableAmount) - parseFloat(otherDeductionAmount) - parseFloat(item.dueAmount);
			if(parseFloat(item.tdsAmount) > parseFloat(diff).toFixed(2)){
				growl.error("You can apply other deduction amt on "+parseFloat(diff).toFixed(2),{
	    			title : ERROR_MSG
	    		});
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].tdsAmount = 0;
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(diff).toFixed(2);
				return false;
			}else{
				var diff = parseFloat(diff) - parseFloat(item.tdsAmount);
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(diff).toFixed(2);
				 //$scope.calculateSummaryForChange();
			}
			
		}else{
			self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].tdsAmount = 0;
		}	
	}

	var validateODAmount = function(item,index){
		
		if(item.otherDeduction == '' || item.otherDeduction == undefined || item.otherDeduction == null)
		{
			item.otherDeduction = parseFloat(0);
		}
		
		var otherDeductionAmount = parseFloat(item.tdsAmount) + parseFloat($rootScope.parseFloatNum(item.disallowedAmount));
			
			if(parseFloat(item.creditPayableAmount) > parseFloat(otherDeductionAmount))
				{
					var diff = parseFloat(item.creditPayableAmount) - parseFloat(otherDeductionAmount) - parseFloat(item.dueAmount);
					if(parseFloat(item.otherDeduction) > parseFloat(diff).toFixed(2)){
						growl.error("You can apply other deduction amt on "+parseFloat(diff).toFixed(2),{
			    			title : ERROR_MSG
			    		});
						self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].otherDeduction = 0;
						self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(diff).toFixed(2);
						return false;
					}else{
						var diff = parseFloat(diff) - parseFloat(item.otherDeduction);
						self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].receivedAmount = parseFloat(diff).toFixed(2);
						 //$scope.calculateSummaryForChange();
					}
					
				}else{
					self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[index].otherDeduction = 0;
				}
		
	}
        //Old -16-4-2020
		/*$scope.applyTDS = function()
		{
			
			//console.log(self.model.companyIvoiceServiceDetails);
			
			self.model.companyIvoiceServiceDetails.forEach((elem) => {
				if(elem.isSelected){
				elem.tdsAmount = parseFloat(0);
				elem.dueAmount = elem.creditPayableAmount;
				elem.tdsAmount = ((parseFloat(elem.creditPayableAmount))*$scope.tdsPercentage/100).toFixed(2);
				var recAmt = parseFloat(elem.creditPayableAmount) - parseFloat(elem.tdsAmount) - parseFloat(elem.disallowedAmount);
				elem.receivedAmount = parseFloat(recAmt).toFixed(2);
				
				elem.dueAmount = parseFloat(elem.dueAmount) - parseFloat(elem.tdsAmount) -  parseFloat(elem.disallowedAmount);
				elem.dueAmount  = parseFloat(elem.dueAmount) - parseFloat(elem.receivedAmount);
				}
			});
			
		}*/

	$scope.aplysTDSHhs = false;
	$scope.applyTDS = function()
	{
		
		$scope.aplysTDSHhs = false;
		if($scope.disAllowedPop == '' || $scope.disAllowedPop == undefined ||  $scope.disAllowedPop == null){
			$scope.disAllowedPop = 0;
		}
		if($scope.tdsPercentage == '' || $scope.tdsPercentage == undefined ||  $scope.tdsPercentage == null){
			$scope.tdsPercentage = 0;
		}
		
		if($scope.checkAllOrder){
			
			var len = self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.length;
			var tmpdisAl = 0;
			var totleCreAmt = 0;
			
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
							totleCreAmt = parseFloat(totleCreAmt) + parseFloat(elem.creditPayableAmount);
					});
			var afterDisAllowed = 0;
			

			self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
				var singPer =  0;
				singPer = (parseFloat($scope.disAllowedPop)/parseFloat(totleCreAmt))*100;
				elem.tdsAmount = parseFloat(0);
				elem.otherDeduction = parseFloat(0);
				elem.disallowedAmount = ((parseFloat(elem.creditPayableAmount))* singPer/100).toFixed(2);
				elem.dueAmount = elem.creditPayableAmount;
				elem.tdsAmount = ((parseFloat(elem.creditPayableAmount)) * parseFloat($scope.tdsPercentage)/100).toFixed(2);
				var recAmt = parseFloat(elem.creditPayableAmount) - (parseFloat(elem.tdsAmount) + parseFloat(elem.disallowedAmount) + parseFloat(elem.otherDeduction));
				elem.receivedAmount = parseFloat(recAmt);
				elem.dueAmount = parseFloat(elem.dueAmount) - (parseFloat(elem.tdsAmount) + parseFloat(elem.disallowedAmount) + parseFloat(elem.otherDeduction));
				elem.dueAmount  = parseFloat(elem.dueAmount) - parseFloat(elem.receivedAmount);
				elem.receivedAmount = parseFloat(elem.receivedAmount).toFixed(2);
				afterDisAllowed += parseFloat(elem.disallowedAmount);
			});
			
			
			
			
			if(parseFloat(afterDisAllowed).toFixed(4) != parseFloat($scope.disAllowedPop).toFixed(4)){
				var difTMpDIS = parseFloat(afterDisAllowed) - parseFloat($scope.disAllowedPop);
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[len -1].disallowedAmount = (parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[len -1].disallowedAmount) - parseFloat(difTMpDIS)).toFixed(2);
				self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[len -1].receivedAmount = (parseFloat(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto[len -1].receivedAmount) + parseFloat(difTMpDIS)).toFixed(2);
			}
			
		}else{
			var flag = false;
			var i = 1;
			var len = 0;
			self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
				if(elem.isSelected){
					flag = true;
					len ++;
				}
				if(self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.length == i){
					
					if(flag){
						
						var tmpdisAl = 0;
						if($scope.disAllowedPop > 0){
							tmpdisAl = (parseFloat($scope.disAllowedPop)/len).toFixed();
						}
						
						
						self.model.invoiceDetails[self.model.currentInvoiceIndex].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
							if(elem.isSelected){
								elem.tdsAmount = parseFloat(0);
								elem.otherDeduction = parseFloat(0);
								elem.disallowedAmount = parseFloat(tmpdisAl);
								elem.dueAmount = elem.creditPayableAmount;
								elem.tdsAmount = ((parseFloat(elem.creditPayableAmount))*$scope.tdsPercentage/100).toFixed(2);
								var recAmt = parseFloat(elem.creditPayableAmount) - (parseFloat(elem.tdsAmount) + parseFloat(elem.disallowedAmount) + parseFloat(elem.otherDeduction));
								elem.receivedAmount = parseFloat(recAmt).toFixed(2);
								
								elem.dueAmount = parseFloat(elem.dueAmount) - (parseFloat(elem.tdsAmount) + parseFloat(elem.disallowedAmount) + parseFloat(elem.otherDeduction));
								elem.dueAmount  = parseFloat(elem.dueAmount) - parseFloat(elem.receivedAmount);
							}
						});
						
					}else{
						growl.warning("Please check at least one service.",{
							title : ERROR_MSG
						});
						return false;
					}
				}
				i++;
			});
		}
		
		//console.log(self.model.companyIvoiceServiceDetails);
		
		self.model.companyIvoiceServiceDetails.forEach((elem) => {
			if(elem.isSelected){
			elem.tdsAmount = parseFloat(0);
			elem.dueAmount = elem.creditPayableAmount;
			elem.tdsAmount = ((parseFloat(elem.creditPayableAmount))*$scope.tdsPercentage/100).toFixed(2);
			var recAmt = parseFloat(elem.creditPayableAmount) - parseFloat(elem.tdsAmount) - parseFloat(elem.disallowedAmount);
			elem.receivedAmount = parseFloat(recAmt).toFixed(2);
			
			elem.dueAmount = parseFloat(elem.dueAmount) - parseFloat(elem.tdsAmount) -  parseFloat(elem.disallowedAmount);
			elem.dueAmount  = parseFloat(elem.dueAmount) - parseFloat(elem.receivedAmount);
			}
		});
		
	}
	
	
	
	$scope.calculateSummaryForChangeGlobalTds = function(){
    	//calculate summary
    	
    	var receivedAmount = 0,tds = 0,otherDeduction = 0,disallowedAmount = 0,total = 0;
    	
    	
    	self.model.invoiceDetails.forEach(function(value,index){
    		if(value.isSelected){
    			
	    		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(value.receivedAmount));
	    		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(value.tdsAmount));
	    		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(value.otherDeduction));
	    		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(value.disallowedAmount));
	    		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(value.total));
	    		
	    		var len = value.tCompanyInvoiceServiceDetailsDto.length;
	    		
	    		
	    		//var tmpTds =  parseFloat(value.tdsAmount)/len;
	    		//var tmpOD =  parseFloat(value.otherDeduction)/len;
	    		var tmpDA = parseFloat(value.disallowedAmount)/len;
	    		$scope.checkAllOrder = true;
	    		var checkDisAllAmou = 0;
	    		value.tCompanyInvoiceServiceDetailsDto.forEach(function(c,index){
	    			checkDisAllAmou += parseFloat(c.disallowedAmount);
	    		});
	    		var otrduTmp = parseFloat(0);
	    		var otrTDSTmp = parseFloat(0);
	    		var otrDISTmp = parseFloat(0);
	    		var v = 1;
	    		var rlTmp = value.tCompanyInvoiceServiceDetailsDto.length;
	    		var tempDisAllAmt=0;
	    		value.tCompanyInvoiceServiceDetailsDto.forEach(function(value1,index){
	    			
	    			if(value1.creditPayableAmount > 0 && parseFloat(value.receivedAmount) > 0){
	    				
	    				var disAllowAmount=parseFloat((value1.creditPayableAmount*value.disallowedAmount)/value.total).toFixed(2);
		    			value1.isSelected = true;
		    			
		    			
//		    			if(parseFloat(value1.tdsAmount) == '' && parseFloat(value1.tdsAmount) == 0 && value.popUpDetailsSaved == false){
		    				
		    				if(parseFloat(value.tdsPercentage) == undefined){
			    				value.tdsPercentage = 0;
			    			}
			    			if(parseFloat(value.tdsPercentage) == 0){
			    			  value.tdsPercentage = ((parseFloat(value.tdsAmount)/parseFloat(value.receivedAmount)) * 100).toFixed(2);
			    			}
			    			
				    			if(parseFloat(value.tdsAmount) != 0){
				    				var tejd = (value1.creditPayableAmount * (value.tdsPercentage/100));
									var tdsAnsos = value1.creditPayableAmount - tejd.toFixed(4);
									value1.tdsAmounttest = value1.creditPayableAmount - tdsAnsos;
									value1.tdsAmount = value1.tdsAmounttest.toFixed(2);
									var tmpTds = value1.tdsAmount;
				    		}else{
				    			value1.tdsAmount = 0.00;
				    			var tmpTds = value1.tdsAmount;
				    		}
//			    		}
						//value1.tdsAmount = parseFloat(tmpTds).toFixed(2);
     					//value1.disallowedAmount = parseFloat(tmpDA).toFixed(2);
//		    			if(parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false){
						/*tempDisAllAmt = parseFloat(tempDisAllAmt)+parseFloat(disAllowAmount);
						if(tempDisAllAmt>value.disallowedAmount){
							var diffDisAmt=parseFloat(tempDisAllAmt-value.disallowedAmount);
							value1.disallowedAmount = disAllowAmount-diffDisAmt;
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
						}else{
							value1.disallowedAmount = disAllowAmount;
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
						}*/
						
//		    			}
				    			
	    			 if(parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false){
		    				
		    				if(value.daPercentage == undefined){
			    				value.daPercentage = 0;
			    			}
			    			if(parseFloat(value.daPercentage) == 0){
			    			  value.daPercentage = ((parseFloat( value.disallowedAmount)/parseFloat(value.receivedAmount))* 100).toFixed(2);
			    			}
			    			
				    				var teDAD = (value1.creditPayableAmount * (value.daPercentage/100));
									var tdsAnDAD = value1.creditPayableAmount - teDAD;
									value1.tdsAmountDAD= value1.creditPayableAmount - tdsAnDAD;
									value1.disallowedAmount = value1.tdsAmountDAD.toFixed(2);
									var tmpDA = value1.disallowedAmount;
									
				    		
			    		}
				    			 
						value1.receivedAmount = parseFloat(0);
						
//						if(parseFloat(value1.otherDeduction) == '' && parseFloat(value1.otherDeduction) == 0 && value.popUpDetailsSaved == false){
						if(value.odPercentage == undefined){
		    				value.odPercentage = 0;
		    			}
		    			if(parseFloat(value1.otherDeduction) != 0){
		    				var tejdod = (value1.creditPayableAmount * (value.odPercentage/100));
							var tdsAnsosod = value1.creditPayableAmount - tejdod;
							value1.tdsAmounttestod = value1.creditPayableAmount - tdsAnsosod;
							value1.otherDeduction = value1.tdsAmounttestod.toFixed(2);
							var tmpOD = value1.otherDeduction;
		    			}
		    			
//						}
//						value1.otherDeduction = parseFloat(tmpOD).toFixed(2);
		    			otrduTmp = parseFloat(otrduTmp)+parseFloat(value1.otherDeduction);
						otrTDSTmp = parseFloat(otrTDSTmp)+parseFloat(value1.tdsAmount);
						otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
						
						
						/*var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
						value1.receivedAmount = parseFloat(recAmt).toFixed(2);
						value1.dueAmount = 0;*/
						
						if(value.receivedAmountNew != undefined){
							 var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
								
	                            var recivedAmountCheck= parseFloat((value.receivedAmount * value1.creditPayableAmount)/value.outstanding).toFixed(1);
								
								if(parseFloat(recAmt).toFixed(1) != parseFloat(recivedAmountCheck)){
									
									value1.dueAmount = parseFloat(recAmt) - parseFloat(recivedAmountCheck);
									value1.receivedAmount = parseFloat(recivedAmountCheck).toFixed(2);
								}else{
									var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
									value1.receivedAmount = parseFloat(recAmt).toFixed(2);
									value1.dueAmount = 0;
								}
						}else{
							var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
							value1.receivedAmount = parseFloat(recAmt).toFixed(2);
							value1.dueAmount = 0;
						}		
						
						//value.dueAmount  = parseFloat(value.dueAmount) - parseFloat(value.receivedAmount);
						if(v == rlTmp){
							if(parseFloat(otrduTmp).toFixed(4) != parseFloat(value.otherDeduction).toFixed(4)){
								var difTMp = parseFloat(otrduTmp) - parseFloat(value.otherDeduction);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction = (parseFloat(tmpOD) - parseFloat(difTMp)).toFixed(2);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMp)).toFixed(2);
							}
							
							if(parseFloat(otrTDSTmp).toFixed(4) != parseFloat(value.tdsAmount).toFixed(4)){
								var difTMpTDS = parseFloat(otrTDSTmp) - parseFloat(value.tdsAmount);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount = (parseFloat(tmpTds) - parseFloat(difTMpTDS)).toFixed(2);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpTDS)).toFixed(2);
							}
							
							if(parseFloat(otrDISTmp).toFixed(4) != parseFloat(value.disallowedAmount).toFixed(4)){
								var difTMpDIS = parseFloat(otrDISTmp) - parseFloat(value.disallowedAmount);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount = (parseFloat(tmpDA) - parseFloat(difTMpDIS)).toFixed(2);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpDIS)).toFixed(2);
							}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction=0
								}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount=0
								}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount=0
								}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount=0
								}
						}
	    				
                    }else if(value1.creditPayableAmount < 0  && parseFloat(value.receivedAmount) > 0){
	    				
	    				var disAllowAmount=parseFloat((value1.creditPayableAmount*value.disallowedAmount)/value.total).toFixed(2);
		    			value1.isSelected = true;
		    			
		    			
//		    			if(parseFloat(value1.tdsAmount) == '' && parseFloat(value1.tdsAmount) == 0 && value.popUpDetailsSaved == false){
		    				
		    				if(parseFloat(value.tdsPercentage) == undefined){
			    				value.tdsPercentage = 0;
			    			}
			    			if(parseFloat(value.tdsPercentage) == 0){
			    			  value.tdsPercentage = ((parseFloat(value.tdsAmount)/parseFloat(value.receivedAmount)) * 100).toFixed(2);
			    			}
			    			
				    			if(parseFloat(value.tdsAmount) != 0){
				    				var tejd = (value1.creditPayableAmount * (value.tdsPercentage/100));
									var tdsAnsos = value1.creditPayableAmount - tejd.toFixed(4);
									value1.tdsAmounttest = value1.creditPayableAmount - tdsAnsos;
									value1.tdsAmount = value1.tdsAmounttest.toFixed(2);
									var tmpTds = value1.tdsAmount;
				    		}else{
				    			value1.tdsAmount = 0.00;
				    			var tmpTds = value1.tdsAmount;
				    		}
//			    		}
						//value1.tdsAmount = parseFloat(tmpTds).toFixed(2);
     					//value1.disallowedAmount = parseFloat(tmpDA).toFixed(2);
//		    			if((parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false ) || (checkDisAllAmou != disallowedAmount )){
						/*tempDisAllAmt = parseFloat(tempDisAllAmt)+parseFloat(disAllowAmount);
						if(tempDisAllAmt>value.disallowedAmount){
							var diffDisAmt=parseFloat(tempDisAllAmt-value.disallowedAmount);
							value1.disallowedAmount = disAllowAmount-diffDisAmt;
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
						}else{
							value1.disallowedAmount = disAllowAmount;
							otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
						}*/
						
//		    			}
				    			
//	    			 if(parseFloat(value1.disallowedAmount) == '' && parseFloat(value1.disallowedAmount) == 0 && value.popUpDetailsSaved == false){
		    				
		    				if(value.daPercentage == undefined){
			    				value.daPercentage = 0;
			    			}
			    			if(parseFloat(value.daPercentage) == 0){
			    			  value.daPercentage = ((parseFloat( value.disallowedAmount)/parseFloat(value.receivedAmount))* 100).toFixed(2);
			    			}
			    			if(parseFloat(value1.disallowedAmount) != 0){
				    				var teDAD = (value1.creditPayableAmount * (value.daPercentage/100));
									var tdsAnDAD = value1.creditPayableAmount - teDAD;
									value1.tdsAmountDAD= value1.creditPayableAmount - tdsAnDAD;
									value1.disallowedAmount = value1.tdsAmountDAD.toFixed(2);
									var tmpDA = value1.disallowedAmount;
			    			}
				    		
//			    		}
						value1.receivedAmount = parseFloat(0);
						
//						if(parseFloat(value1.otherDeduction) == '' && parseFloat(value1.otherDeduction) == 0 && value.popUpDetailsSaved == false){
						if(value.odPercentage == undefined){
		    				value.odPercentage = 0;
		    			}
		    			if(parseFloat(value1.otherDeduction) != 0){
		    				var tejdod = (value1.creditPayableAmount * (value.odPercentage/100));
							var tdsAnsosod = value1.creditPayableAmount - tejdod;
							value1.tdsAmounttestod = value1.creditPayableAmount - tdsAnsosod;
							value1.otherDeduction = value1.tdsAmounttestod.toFixed(2);
							var tmpOD = value1.otherDeduction;
		    			}
		    			
//						}
		    			
//						value1.otherDeduction = parseFloat(tmpOD).toFixed(2);
		    			otrduTmp = parseFloat(otrduTmp)+parseFloat(value1.otherDeduction);
						otrTDSTmp = parseFloat(otrTDSTmp)+parseFloat(value1.tdsAmount);
						otrDISTmp = parseFloat(otrDISTmp)+parseFloat(value1.disallowedAmount);
						
						
						/*var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
						value1.receivedAmount = parseFloat(recAmt).toFixed(2);
						value1.dueAmount = 0;*/
						
						if(value.receivedAmountNew != undefined){
							 var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
								
	                            var recivedAmountCheck= parseFloat((value.receivedAmount * value1.creditPayableAmount)/value.outstanding).toFixed(1);
								
								if(parseFloat(recAmt).toFixed(1) != parseFloat(recivedAmountCheck)){
									
									value1.dueAmount = parseFloat(recAmt) - parseFloat(recivedAmountCheck);
									value1.receivedAmount = parseFloat(recivedAmountCheck).toFixed(2);
								}else{
									var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
									value1.receivedAmount = parseFloat(recAmt).toFixed(2);
									value1.dueAmount = 0;
								}
						}else{
							var recAmt = parseFloat(value1.creditPayableAmount) - (parseFloat(value1.tdsAmount) + parseFloat(value1.disallowedAmount) + parseFloat(value1.otherDeduction));
							value1.receivedAmount = parseFloat(recAmt).toFixed(2);
							value1.dueAmount = 0;
						}		
							
						//value.dueAmount  = parseFloat(value.dueAmount) - parseFloat(value.receivedAmount);
						if(v == rlTmp){
							if(parseFloat(otrduTmp).toFixed(4) != parseFloat(value.otherDeduction).toFixed(4)){
								var difTMp = parseFloat(otrduTmp) - parseFloat(value.otherDeduction);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction = (parseFloat(tmpOD) - parseFloat(difTMp)).toFixed(2);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMp)).toFixed(2);
							}
							
							if(parseFloat(otrTDSTmp).toFixed(4) != parseFloat(value.tdsAmount).toFixed(4)){
								var difTMpTDS = parseFloat(otrTDSTmp) - parseFloat(value.tdsAmount);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount = (parseFloat(tmpTds) - parseFloat(difTMpTDS)).toFixed(2);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpTDS)).toFixed(2);
							}
							
							if(parseFloat(otrDISTmp).toFixed(4) != parseFloat(value.disallowedAmount).toFixed(4)){
								var difTMpDIS = parseFloat(otrDISTmp) - parseFloat(value.disallowedAmount);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount = (parseFloat(tmpDA) - parseFloat(difTMpDIS)).toFixed(2);
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount = (parseFloat(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount) + parseFloat(difTMpDIS)).toFixed(2);
							}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].otherDeduction=0
								}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].receivedAmount=0
								}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].tdsAmount=0
								}
							if(isNaN(value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount))
								{
								value.tCompanyInvoiceServiceDetailsDto[rlTmp -1].disallowedAmount=0
								}
						}
	    				
                    }
					v++;
	    		});
    		}
    		
    		
    	});
    	
    	
    	
    	
    	self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(receivedAmount),tds:$rootScope.parseFloatNum(tds),total:$rootScope.parseFloatNum(total),
    									otherDeduction:$rootScope.parseFloatNum(otherDeduction),disallowedAmount:$rootScope.parseFloatNum(disallowedAmount)};
    	self.model.tdsAmountGlobal = self.model.invoiceDetailsSummary.tds;
    	
    	//added for payment collection
    	$scope.dueAmount = $rootScope.parseFloatNum(total);
    	$rootScope.setFirstTabLevel(2);
    }
	
	
	$scope.saveServiceInvoiceDetailsOutSideTds = function(idx){
		
		$rootScope.startLoader();
		var totalRec = 0;
		var totalDisall = 0;
		var totalOtherDeduction = 0;
		var totalTDS = 0;
		var totalAmt = 0;
		
		
		var amountObj = {
				receivedAmount:0,
				disallowedAmount:0,
				otherDeduction:0,
				serviceId:0,
				invoiceDetailId:0,
				compInvSerDetlsId:0,
				dueAmount:0
		};
		
		
		self.model.recvAndDisallAmountDetails = [];
		
		if(self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto.length > 0){
			var bmId = self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto[0].billingMasterId;
			var testRecSerDet = [];
			testRecSerDet = self.model.recvAndDisallAmountDetailsFinal;
			self.model.recvAndDisallAmountDetailsFinal = [];
			var i=0;
			angular.forEach(testRecSerDet, function (value, key) {
				if(value.billingMasterId != bmId)
					{
					 self.model.recvAndDisallAmountDetailsFinal.push(value);
					
					}
				i++;
			});
		}
		self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
			if(elem.isSelected){
				
			totalRec = parseFloat(totalRec) + ((elem.receivedAmount == null || elem.receivedAmount == "" || isNaN(elem.receivedAmount)) ? 0 : parseFloat(elem.receivedAmount));
			totalDisall = parseFloat(totalDisall) + (elem.disallowedAmount == null ||elem.disallowedAmount == "" || (isNaN(elem.disallowedAmount)) ? 0 : parseFloat(elem.disallowedAmount));
			totalTDS = parseFloat(totalTDS) + (elem.tdsAmount == null ||elem.tdsAmount == "" || (isNaN(elem.tdsAmount)) ? 0 : parseFloat(elem.tdsAmount));
			totalOtherDeduction = parseFloat(totalOtherDeduction) + (elem.otherDeduction == null ||elem.otherDeduction == "" || (isNaN(elem.otherDeduction)) ? 0 : parseFloat(elem.otherDeduction));
			//totalAmt = parseFloat(totalRec)+ parseFloat(totalDisall) + parseFloat(totalTDS) + parseFloat(totalOtherDeduction);
			var tempObj=angular.copy(amountObj);
			tempObj.receivedAmount = parseFloat(elem.receivedAmount);
			tempObj.disallowedAmount = parseFloat(elem.disallowedAmount);
			tempObj.otherDeduction = parseFloat(elem.otherDeduction);
			tempObj.serviceId = elem.serviceId;
			tempObj.itemId = elem.itemId;
			tempObj.invoiceDetailId = elem.invoiceDetailId;
			tempObj.compInvSerDetlsId = elem.compInvSerDetlsId;
			tempObj.dueAmount = elem.dueAmount;
			tempObj.tdsAmount = elem.tdsAmount;
			tempObj.saleId = elem.saleId;
			tempObj.orderDetailsId = elem.orderDetailsId;
			tempObj.tSaleDetailsId = elem.tSaleDetailsId;
			tempObj.billingMasterId = elem.billingMasterId;
			self.model.recvAndDisallAmountDetails.push(tempObj);
			console.log("scsdcdsc",tempObj);
			
			self.model.recvAndDisallAmountDetailsFinal.push(tempObj);
				
			}
		});
		
		
		$scope.totalPopupAmount  = 0;
		self.model.invoiceDetails[idx].tCompanyInvoiceServiceDetailsDto.forEach((elem) => {
			elem.isSelected = true;
			$scope.totalPopupAmount = parseFloat($scope.totalPopupAmount)+parseFloat(elem.creditPayableAmount);
		});
		
		var invoice = self.model.invoiceDetails[idx];
		invoice.disallowedAmount = $rootScope.parseFloatNum(totalDisall);
		$scope.calculateSummaryForChangeGlobalTds();
		$rootScope.stopLoader();
	}
	
	$scope.changeTDSPerAmount = function(tdsAmountGlobal){
		if(parseFloat(tdsAmountGlobal) == parseFloat(self.model.invoiceDetailsSummary.receivedAmount)){
			growl.error("TDS Amount Should Not Be Equals To Recived Amount");
			self.model.tdsAmountGlobal = 0.00;
			tdsAmountGlobal = 0.00;
			return false;
		}
		if( parseFloat(tdsAmountGlobal) > parseFloat(self.model.invoiceDetailsSummary.receivedAmount)){
			growl.error("TDS Amount Should Not Be Equals To Recived Amount");
			 var oldTds = 0;
			angular.forEach(self.model.invoiceDetails,function(value,index){
				oldTds += value.tdsAmount;
			});
			self.model.tdsAmountGlobal =oldTds;
			tdsAmountGlobal = oldTds;
			return false;
		}
		if(tdsAmountGlobal == ''){
			self.model.tdsAmountGlobal = 0.00;
			tdsAmountGlobal = 0.00;
		}
		var tdsPercentageGlobal = (self.model.tdsAmountGlobal/parseFloat(self.model.invoiceDetailsSummary.receivedAmount)) * 100;
		
		angular.forEach(self.model.invoiceDetails,function(invdtl,key){
			if(invdtl.isSelected){
				invdtl.popUpDetailsSaved = false; 
				var glo = (invdtl.receivedAmount * (tdsPercentageGlobal/100));
				var glo1 = invdtl.receivedAmount - glo.toFixed(4);
				invdtl.glo2 = invdtl.receivedAmount - glo1;
				invdtl.tdsAmount = invdtl.glo2.toFixed(2);
				    	if(invdtl.tdsAmount == '' || invdtl.tdsAmount == undefined || invdtl.tdsAmount == null){
				    		invdtl.tdsAmount = 0;
				    	}
				    	
				    	var tdsPercentage = (parseFloat(invdtl.tdsAmount)/parseFloat(invdtl.receivedAmount)) * 100;
				    	
				    	console.log(tdsPercentage);
				    	
				    	var otherDeductionAmount = parseFloat(invdtl.otherDeduction) + parseFloat($rootScope.parseFloatNum(invdtl.securityDepositSettlement)) + parseFloat(invdtl.disallowedAmount);
				    	
				    	if(parseFloat(invdtl.outstanding) > parseFloat(otherDeductionAmount))
				    		{
				    		   if(parseFloat(invdtl.receivedAmount) == parseFloat(invdtl.outstanding)){
				    			   var diff = parseFloat(invdtl.outstanding) - parseFloat(otherDeductionAmount);
				    		   }else{
				    			   if(invdtl.receivedAmountNew != undefined){
				    				   var diff = parseFloat(invdtl.receivedAmountNew) - parseFloat(otherDeductionAmount);
				    			   }else{
				    				   var diff = parseFloat(invdtl.outstanding) - parseFloat(otherDeductionAmount);
				    			   }
				    		   }
				    			
				    			diff =  parseFloat(diff).toFixed(2);
				    			if(parseFloat(invdtl.tdsAmount) > parseFloat(diff)){
				    				growl.error("You can apply tds on "+parseFloat(diff).toFixed(2),{
				    	    			title : ERROR_MSG
				    	    		});
				    				
				    				self.model.invoiceDetails[key].tdsAmount = 0;
				    				self.model.invoiceDetails[key].tdsPercentage = tdsPercentage;
				    				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
				    				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
				    				$scope.calculateSummaryForChangeGlobalTds();
				    				
				    				$scope.saveServiceInvoiceDetailsOutSideTds(key);
				    				return false;
				    			}else{
				    				var diff = parseFloat(diff) - parseFloat(invdtl.tdsAmount);
				    				self.model.invoiceDetails[key].tdsPercentage = parseFloat(tdsPercentage).toFixed(2); 
				    				self.model.invoiceDetails[key].receivedAmount = parseFloat(diff).toFixed(2);
				    				self.model.invoiceDetails[key].total = parseFloat(invdtl.receivedAmount) + parseFloat(invdtl.tdsAmount) + parseFloat(invdtl.disallowedAmount) + parseFloat(invdtl.otherDeduction);
				    				 //$scope.calculateSummaryForChange();
                                    $scope.calculateSummaryForChangeGlobalTds();
				    				$scope.saveServiceInvoiceDetailsOutSideTds(key); 
				    			}
				    			
				    		}
				    	
				  
			}
		});
		
		
	}
		/* *************************************Mpesa Integration start****************************************** */
		$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
		$scope.countryCallingCode="";
		$scope.validateMpesaDetails = function(mpesaObject){
			console.log("MPesa Object:",mpesaObject);
			
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
			$rootScope.startLoader();
			var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
			BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				
				if (response.data.status == "success")
					{
					
						$scope.mpesaObject.mpesaAmount = response.data.object.mpesaAmount;
						$scope.mpesaObject.resTransactionId = response.data.object.mpesaTxcode;
						$scope.addPaymentDetails();
						$rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					
					growl.error(response.data.message,{
						title : ERROR_MSG
					});
					$scope.mpesaObject.mpesaAmount=null;
					$rootScope.stopLoader();
				}
				
			});
			}else{
				growl.error("Enter both transaction id and mobile number",{
					title : ERROR_MSG
				});
			}
		}
		
			$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
			
				if(validateMpesaAmount > $scope.dueAmount){
					
					growl.warning("Mpesa amount is should be equel or less than due amount ",{
						title : "warning!!!"
					});
					
				}else{
				
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
						$rootScope.startLoader();
						var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
						BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							
							if (response.data.status == "success")
								{
								
								growl.success(response.data.message,{
									title : SUCCESS_MSG
								});
								$rootScope.stopLoader();
								}
							else if(response.data.status == "error"){
								
								growl.error(response.data.message,{
									title : ERROR_MSG
								});
								$rootScope.stopLoader();
							}
							
						});
						}else{
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
		
	$scope.cancelPaymentReqModal = function(){
		angular.element('#paymentReqModal').modal('hide');
	}
	
		/* *************************************Mpesa Integration END****************************************** */
		
		init();
		
	    $scope.unitApplicationData = function(tdsPer){
			var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
			var obj = {
			            organizationId :$scope.organizationId,
			            unitId :$scope.unitId
			        }
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			    if(response.data.status == "success"){
			    	 $scope.unitConfigurationObject ={};
			         $scope.unitConfigurationObject = response.data.listObject[0];
			    }
			});
	    } 
	    $scope.unitApplicationData();
		$scope.cancelExAmount = function(){
			$('#confirmSave').modal('hide');
			$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
		}
		
		
		$scope.billDetailsAgainstInvoiceList = [];
		$scope.getBillDetailsAgainstInvoice = function(id){
	    		//get invoice details
		    	var URI = BASE_URL + ROOT_URL + "/api/billing/invoicedtls/invoice/"+id;
		    	$rootScope.startLoader();
			    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.billDetailsAgainstInvoiceList = [];
						 $scope.billDetailsAgainstInvoiceList = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
	    	
	    }
		
		$scope.initRequestList = function(data) 
		{
			$scope.addNewRequestList  =[];
			$scope.addNewRequestListNew = [];
			$rootScope.startLoaderPopup();
		    var param = {
		    		"payeeId":data.billPayeeId,
		    		"organisationId":$scope.organizationId,
		    		"unitId":$scope.unitId,
		    		"patientId":data.patientId, 
		    		};
		    if(data.admissionId != null)
		    	param.admissionId = data.admissionId;
		    else
		    	param.encounterId = data.encounterId;
		    
		    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListByPayeeId"
		   return BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.addNewRequestList = response.data.listObject;
					 angular.forEach($scope.addNewRequestList,function (req){
						 if(req.authorizationStatus == 1){
							 req.claimNumber = data.claimNumber;
							 $scope.addNewRequestListNew.push(req);
						 }
					 });
					 $rootScope.stopLoaderPopup();
					 $rootScope.stopLoader();
					return true;
				}
				
			});
		}  
		
		$scope.getPreAuthRequest = function(info,index){
			$rootScope.startLoaderPopup();
			$scope.selectedObject ={};
			$scope.selectedObject =info;
			if(info.admissionId != null){
				var URI = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/"+info.admissionId+"/encounter/0";
				BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 $scope.patientSponsorDetailsDtosListSplit = [];
						 $scope.patientSponsorDetailsDtosListSplit = response.data.listObject;
						 angular.forEach($scope.patientSponsorDetailsDtosListSplit,function (v,k){
							 if($scope.selectedObject.billPayeeId == v.payeeId){
								 $scope.selectedObject.claimNumber = v.claimNumber;
							 }
						 });
						 $rootScope.stopLoaderPopup();
						 $scope.initRequestList($scope.selectedObject);
					}
				});
			}else if(info.encounterId != null){
				var URL = BASE_URL + ROOT_URL +"/api/opd/sponsoredDetails/admission/0/encounter/"+info.encounterId;
				BillingGenericService.serviceAction(METHOD_GET,URL, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 $scope.patientSponsorDetailsDtosListSplit = [];
						 $scope.patientSponsorDetailsDtosListSplit = response.data.listObject;
						 angular.forEach($scope.patientSponsorDetailsDtosListSplit,function (v,k){
							 if($scope.selectedObject.billPayeeId == v.payeeId){
								 $scope.selectedObject.claimNumber = v.claimNumber;
							 }
						 });
						 $rootScope.stopLoaderPopup();
						 $scope.initRequestList($scope.selectedObject);
					}
				});
			}
	
		}
	}
	
	function config($stateProvider){
		$stateProvider.state('companySettlement', {
			 url: '/companySettlement',
			 title:'Company Settlement',
			 templateUrl: 'views/master/UnitMaster/billingMaster/company-settlement.html',
			 controller: "CompanySettlement.Controller",
			 controllerAs : "vm"
		});
	}
	
	angular.module("myApp").config(config).controller("CompanySettlement.Controller",companySettlementController);
})();
