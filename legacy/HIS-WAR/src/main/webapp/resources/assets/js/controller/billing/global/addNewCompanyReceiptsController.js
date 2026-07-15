angular.module("myApp").controller("addNewCompanyReceiptsController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','$timeout','growl','paymentCollFactory','reportService','GenericService','$location','loyaltiCardCallFactory','$stateParams','GenericServiceFileUpload',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,$timeout,growl,paymentCollFactory,reportService,GenericService,$location,loyaltiCardCallFactory,$stateParams,GenericServiceFileUpload){
	try{
		
		
		
		
		$scope.init = function(){
			
			$rootScope.loginpage = true;
			$scope.page = "addNewCompanyReceipts";
			
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					//minDate : new Date()
				};
			
			$scope.openDatePicker = function() { 
				$scope.datepicker.opened = true;
			};
			
			$scope.datepicker = {
				opened : false
			};
			var cookieObject = $cookies.getObject('cookieObject');
			if (cookieObject == undefined) {
				$state.go('login');
				return;
			}
			$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			 if(cookieObjectAppConfig == undefined){
			  $state.go('login');
			  return;
			 }
			 else
			  {
			   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			   
			  }
			$scope.unitId = cookieObject.unitId;
			$scope.userId = cookieObject.userId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId =$scope.userId;
			$scope.billDate = new Date();
			$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
			$scope.receivedAmt = parseFloat(0);
			
			$scope.mpesaObj = {
					transactionId:null,
					mobileNo:null,
					mpesaAmount:null,
					resTransactionId:null
			}
			
			$scope.detailsView = false;
			$scope.enableDocSaveBitton = false;
			// to get general ledger list
			var URI = BASE_URL + ROOT_URL
					+ GETGENERALLEDGERMASTERACTIVELIST
					+ "/" + $scope.organizationId;
			GenericService
					.serviceAction("POST", URI, {})
					.then(
							function(response) {
								$scope.accountHeadList = [];

								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.accountHeadList = response.data.listObject;
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
				
				console.log("transactionTypesList",$scope.transactionTypesList);
			});
			

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
			
			$scope.paymentDetails = {
					paymentModeId:1 ,
							cashAmount:"",
							bankId:"",
							chequeNumber:"",
							chequeDate:"",
							chequeAmount:"",
							credCardHolderName:"",
							credCardTypeId:"",
							credAmount:"",
							credCardNumber:"",
							credExpiryDate:"",
							cardBalAmount:"",
							}
			 $scope.listPaymentDetails = [];
			
		}
		$scope.init();
		
		$scope.initPaymentDetailsObj  = function(){
			$scope.paymentDetails = angular.copy({paymentModeId:($scope.paymentDetails != "undefined" && $scope.paymentDetails.paymentModeId != "undefined") ? $scope.paymentDetails.paymentModeId : 1 ,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
		}
		
		$scope.firstTabLevel = 1 ;
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		
		  $scope. getCompaniesByCompanyType = function(companyTypeId){
		    	
		    	
		    	if(companyTypeId != ""){
			    	
		    		if(companyTypeId==3){
			    		$scope.disableInsurance=true;
			    		$scope.insuranceCompanyId = null;
		    		}
			    	else{
			    		$scope.disableInsurance=false;
			    	}
		    		
		    		var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:companyTypeId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList";
				    $rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.companies = [];
							$scope.companies = response.data.listObject;
						}
						$rootScope.stopLoader();
					});
		    	}else{
		    		$scope.insuranceCompanyId = null;
		    	}
		    }
		
		   $scope.getInsuranceByCompanyId = function(companyId){
		    	if(companyId != ""){
			    	var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster";
				    $rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.associateCompanies = [];
							 $scope.associateCompanies = response.data.listObject;
						}
						$rootScope.stopLoader();
					});
		    	}
		    }
		    
		$scope.addPaymentDetailsList = function(){
			
			switch($scope.paymentDetails.paymentModeId){
				case 1 :
					if(typeof($scope.paymentDetails.cashAmount) == "undefined" || $scope.paymentDetails.cashAmount == "" || $scope.paymentDetails.cashAmount == parseFloat(0) || $scope.paymentDetails.cashAmount < parseFloat(0)){
						growl.warning("Please insert an amount",{
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
					cashLimit = cashLimit + parseFloat($scope.paymentDetails.cashAmount);
					if( ($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null ) && cashLimit > $scope.cashAmountLimit ){
						growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
							title : WARNING_MSG
						});
						return;
					}

					
						 var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
						   $scope.listPaymentDetails.push(paymentDetails);
					break;
				case 2 :
					if(typeof($scope.paymentDetails.chequeAmount) == "undefined" || $scope.paymentDetails.chequeAmount == "" || $scope.paymentDetails.bankId == 0 || $scope.paymentDetails.chequeNumber == "" || typeof($scope.paymentDetails.chequeNumber) == "undefined" || typeof($scope.paymentDetails.chequeDate) == "undefined" || $scope.paymentDetails.chequeDate == "" || $scope.paymentDetails.chequeAmount == parseFloat(0) || $scope.paymentDetails.chequeAmount < parseFloat(0) || $scope.paymentDetails.hospitalAccountId == 0){
						growl.warning("Please select bank,hospital account, chequeno, cheque date and amount",{
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
					
					
					var bankName = "";
					//Get Bank Name
					angular.forEach($scope.bankList,function(value,key){
						if($scope.paymentDetails.bankId == value.bankId)
							bankName = value.bankName;
					})
					
						var chequeDate = $rootScope.getFormattedDateMisc($scope.paymentDetails.chequeDate,false);
						var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.chequeAmount).toFixed(2),createdBy:$scope.userId, bankId:$scope.paymentDetails.bankId, chequeNumber:$scope.paymentDetails.chequeNumber, chequeDate:chequeDate,bankName:bankName,hospitalAccountId:$scope.paymentDetails.hospitalAccountId};//, bankName:bankName
						$scope.listPaymentDetails.push(paymentDetails);
					break;
				case 3 :
					if(typeof($scope.paymentDetails.credAmount) == "undefined" || $scope.paymentDetails.credAmount == ""  || $scope.paymentDetails.credAmount == parseFloat(0)  || $scope.paymentDetails.credAmount < parseFloat(0)){
						growl.warning("Please enter amount",{
							title : WARNING_MSG
						});
						return;
					}
					
					if($scope.paymentDetails.credExpiryDate==null||$scope.paymentDetails.credExpiryDate==undefined||$scope.paymentDetails.credExpiryDate==''){
						$scope.paymentDetails.credExpiryDate=null;
					}
					
						var expiryDate =  $scope.paymentDetails.credExpiryDate != null ? $rootScope.getFormattedDateMisc($scope.paymentDetails.credExpiryDate,false) : null;
						var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:$scope.paymentDetails.transactionNo};
						$scope.listPaymentDetails.push(paymentDetails);
					break;
					
				case 8:
					if($scope.eTransferObj.transactionobj.id == "" || $scope.eTransferObj.transactionobj.id == undefined){
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
					}
					if($scope.eTransferObj.transTransactionDate == ""){
						growl.warning("Please Select Transaction Date",{
							title : WARNING_MSG
						});
						return;	
					}*/
					if($scope.eTransferObj.transTransactionDate == ""){
						$scope.eTransferObj.transTransactionDate = null;
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
					
					var bankName = "";
					
					//Get Bank Name
					angular.forEach($scope.bankList,function(value,key){
						if($scope.eTransferObj.transBankId == value.bankId)
							bankName = value.bankName;
					})
					
					
						  var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.eTransferObj.transferAmount).toFixed(2), transactionType:$scope.eTransferObj.transactionobj.name, transactionTypeId:$scope.eTransferObj.transactionobj.id,transTransactionDate : $scope.eTransferObj.transTransactionDate!=null ? moment($scope.eTransferObj.transTransactionDate).format('YYYY-MM-DD HH:mm:ss') : null,bankId:$scope.eTransferObj.transBankId,transactionNo:$scope.eTransferObj.transactionNo,createdBy:$scope.userId,bankName:bankName};
						  $scope.listPaymentDetails.push(paymentDetails);
						  $scope.eTransferObj={};
					break;
			}
			$scope.initPaymentDetailsObj();
			//factory.initPaymentDetailsObj(angular.element(document.getElementById("uiView")).scope());
			$scope.paymentDetails.cashAmount = parseFloat($scope.amountPending);
			
		}

		
		$scope.removePaymentDetails = function(index){
			
			$scope.amountPending = parseFloat($scope.amountPending) + parseFloat($scope.listPaymentDetails[index].amount);
			
			$scope.listPaymentDetails.splice(index,1);
			
		}
		
		$scope.paymentModes = {
				1: "Cash",
				2: "Cheque",
				3: "Credit Card",
				4: "E - Transfer",
				5: "M-Pesa",
				6: "Loyalty Card",
				8: "E-Transfer"	
		}
		
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

		$scope.addPaymentDetails = function(){
			var exAmount = '';
			
			var cashLimit = 0;
			angular.forEach($scope.listPaymentDetails,function(value,key){
				
				if(value.paymentModeId == 1){
					cashLimit = parseFloat(cashLimit) + parseFloat(value.amount);
				}
			});
			
			if($scope.paymentDetails.paymentModeId == 1){
				cashLimit = cashLimit + parseFloat($scope.paymentDetails.cashAmount);
			}
			
			if( ($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null ) && cashLimit > $scope.cashAmountLimit ){
				growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
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
				
			}else if($scope.paymentDetails.paymentModeId == 8){
				
				exAmount = $scope.eTransferObj.transferAmount;
			}
			
			if(exAmount == undefined || exAmount == '' || exAmount == 0 || exAmount == null){
				  growl.warning("Amount should be greater than ZERO(0)..!! ",{
						title : WARNING_MSG
					});
					return false;
			  }else{
				  
			if(parseFloat(exAmount) <= parseFloat($scope.amountPending))
			{
					$scope.amountPending = parseFloat($scope.amountPending) - parseFloat(exAmount);
					$scope.addPaymentDetailsList();
			}else{
						  growl.warning("You can't exceed amount of "+parseFloat($scope.amountPending).toFixed(2),{
								title : WARNING_MSG
							});
							return false;
					  
			}
		}
			
		}
		
		
		$scope.clearMpesaFields = function(){
			$scope.skipMpesaValidation = false;
			$scope.mpesaObj.mobileNo = null;
			$scope.mpesaObj.mpesaAmount = '';
			$scope.mpesaObj.transactionId = null;
		}
		
		
		$scope.settlementClick = function(){
			
			/*if($scope.encounterDetailsList.length > 0){*/
			if($scope.companyTypeId == '' || $scope.companyTypeId == undefined || $scope.companyTypeId == null || $scope.companyTypeId == 0){
				 growl.warning("Please select company type..!!",{
						title : WARNING_MSG
					});
					return false;
			}
			else if($scope.companyId == '' || $scope.companyId == undefined || $scope.companyId == null || $scope.companyId == 0){
				 growl.warning("Please select company..!!",{
						title : WARNING_MSG
					});
					return false;
			}else if(($scope.companyTypeId == 2) && ($scope.insuranceCompanyId == '' || $scope.insuranceCompanyId == undefined || $scope.insuranceCompanyId == null || $scope.insuranceCompanyId == 0)){
				 growl.warning("Please select insurance company..!!",{
						title : WARNING_MSG
					});
					return false;
			}else if($scope.billDate == '' || $scope.billDate == undefined || $scope.billDate == null){
				growl.warning("Please select invoice date..!!",{
					title : WARNING_MSG
				});
				return false;
			}
			/*else if($scope.billNumber == '' || $scope.billNumber == undefined || $scope.billNumber == null){
				growl.warning("Please enter bill number..!!",{
					title : WARNING_MSG
				});
				return false;
			}*/
			else if($scope.name == '' || $scope.name == undefined || $scope.name == null){
				growl.warning("Please enter name..!!",{
					title : WARNING_MSG
				});
				return false;
			}
			else if($scope.amount == '' || $scope.amount == undefined || $scope.amount == null){
				growl.warning("Please enter total amount..!!",{
					title : WARNING_MSG
				});
				return false;
			}else{
				
				if(parseFloat($scope.receivedAmt) > parseFloat($scope.amount)){
					growl.warning("You Can't less amount of "+ parseFloat($scope.receivedAmt)+", If you want enter less amount first cancel the receipt..!!",{
						title : WARNING_MSG
					});
					return false;
				}else{
				$scope.amountPending = parseFloat($scope.amount) - parseFloat($scope.receivedAmt);
				$scope.paymentDetails.cashAmount = $scope.amountPending;
				$scope.firstTabLevel = 1 ;
				$scope.listPaymentDetails = [];
				angular.element('#collectPayment').modal('show');
				}
			}
		/*}else{
			 growl.warning("Please add at least one document..!!",{
					title : WARNING_MSG
				});
				return false;
		}
		* - Document Compulsion removed by kaustubh for NCH 
		*/
			
		}
		
		
		//For Doc Upload Start
		$scope.encounterDetailsList = [];
		$scope.encounterDetails = {
			  encounterDocId : "",
			  admissionId:"",
			  visitTypeId:2,
			  patientId : "",
			  documentPath : "",
			  documentTypeId :34,
			  status : 'A',
			  createdDate : "",
			  createdBy : $scope.userId,
			  updatedDate : "",
			  updatedBy : $scope.userId,
			  unitId : $scope.unitId,
			  organisationId : $scope.organizationId,
			  file : {}
			}
		
		$scope.showDocUPloadPopup   = function()
		{
			 $('#fileId').val(undefined);
			$scope.encounterDetailsList=[];
			if($scope.encounterDetailsList.length>0)
			{
			  $('#uploadDocForMiscelleneous').modal('show');
			}
		else{
			 $('#uploadDocForMiscelleneous').modal('show');
			var encounterDetails = angular.copy($scope.encounterDetails);
			$scope.encounterDetailsList.push(encounterDetails);
		}
			
		$scope.addImageDetails = function()
		{ 
			var encounterDetails = angular.copy($scope.encounterDetails);
			$scope.encounterDetailsList.push(encounterDetails);
		};

		  $scope.removeImageDetails = function(deleteIndex)
			{ 
			   $scope.encounterDetailsList.splice(deleteIndex, 1);
		    }
		  
			$scope.getPatientDocumentList();
			
				
		}
		
		
		
		 $scope.uploadPatientImage = function()
		    {
			 
			 $scope.finalDocUploadList=[];
			 var count=0;
			 angular.forEach($scope.encounterDetailsList, function(value, key) {  
				  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
						 growl.warning("Please select document type .",
													{
														title : 'warning!'
													});
										   $rootScope.stopLoader();
						return false;
					}
				  if (angular.equals(value.file, {}) || value.file==undefined) {
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
						$scope.finalDocUploadList.push(value);
						 console.log("final list",$scope.finalDoc);
			});
			 
			 if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length)
			 {
				   $rootScope.startLoader();	
				   $scope.count = 0;
				   var v = 0;
				   angular.forEach($scope.encounterDetailsList, function(value, key) 
				   {
						 v++;
						 if(!angular.equals({}, value.file))
						 {
							var promise = $scope.uploadEncounterDoc(value,$scope.newMiscellaneousObj.companyReceiptsExpenseId);
							promise.then(function(response) 
									{
									  if(response.data.status == "success")
									  {
										  $rootScope.stopLoader();  
										  	$scope.count++;
										    if($scope.encounterDetailsList.length==$scope.count)
											 {
										    	$scope.listPaymentDetails = [];
										    	$scope.finalDocUploadList=[];
												$scope.encounterDetailsList = [];
												$(".modal-backdrop").hide();
												$state.go('companyReceipts');	
											 }
									   }
									  else 
										{
											growl.error('Something went wrong.',
													{
														title : 'Error!'
													});
										}
								    });
						 }else{
							 if(v == $scope.encounterDetailsList.length)
								 {
								 	$scope.listPaymentDetails = [];
								 	$scope.finalDocUploadList=[];
								 	$scope.encounterDetailsList = [];
								    $(".modal-backdrop").hide();
								    $state.go('companyReceipts');	
								 }
							 $rootScope.stopLoader();
						 }
									
					});
			 }
			
		    }
		 
		 
		 $scope.uploadEncounterDoc = function(Obj,expenseId)
			{
				 var fd = new FormData();
			        fd.append('encounterId', 0);
			        fd.append('visitTypeId', 0);
			        fd.append('admissionId', 0);
			        fd.append('patientId',0);
			        fd.append('documentTypeId',34);
			        fd.append('createdBy',Obj.createdBy );
			        fd.append('updatedBy', Obj.updatedBy);
			        fd.append('unitId', Obj.unitId);
			        fd.append('organisationId', Obj.organisationId);
			        fd.append('companyExpenseId',expenseId);
			        fd.append('expenseId',0);
			        fd.append('grnId',0);
					fd.append('poId',0);
			        fd.append('file',Obj.file );
			        fd.append('incidentReportingMapperId',0);
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
		 
		 

			$scope.getPatientDocumentList = function(obj)
		     {
		      
		      $rootScope.startLoader();
		      var data =
		        {
		        "companyReceiptsExpenseId":$scope.expenseId,
		        "unitId":$scope.unitId,
		        "organisationId":$scope.organizationId,
		        "userId":undefined,
		        "visitTypeId":undefined,
		        "admissionId":undefined,
		       }
		      if($scope.expenseId != undefined){
		      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
		      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		        $scope.patientDocumentList = [];
		        if (response.data.status == "success") 
		        {
		        	$scope.patientDocumentList = response.data.listObject;  
		           
		            $rootScope.stopLoader();
		        }
		        else if(response.data.status == "error")
		         {
		        	$rootScope.stopLoader();
		         }
		       });
		     }else{
		    	 $rootScope.stopLoader();
		     }
		     }
		//For Doc Upload End
		
		$scope.saveMiscellaneous = function(){
			var data ={
					"billNumber":($scope.billNumber == undefined || $scope.billNumber == '')?null:$scope.billNumber,
					"billDateTime": moment($scope.billDate).format('YYYY-MM-DD HH:mm:ss'),
					"amount": $scope.amount,
					"companyTypeId":$scope.companyTypeId,
					"companyId":$scope.companyId,
					"insuranceCompanyId":$scope.insuranceCompanyId,
					"name": ($scope.name == undefined || $scope.name == '')?null:$scope.name,
					"unitId":$scope.unitId,
					"orgId": $scope.organizationId,
					"createdBy": $scope.userId,
					"updatedBy":$scope.userId,
					"companyRecieptsReceiptDetailsDto":$scope.listPaymentDetails,
					"remark":$scope.remark
				}
			if($scope.type == 2){
				data.companyReceiptsExpenseId = $scope.expenseId;
				data.receivedAmount = $scope.receivedAmt;
			}
			console.log(data);
			$rootScope.startLoaderPopup();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/companyReceipts/save";
			BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
			{
				if(response.data.status == "success"){
					$rootScope.stopLoaderPopup();
					
					if(response.data.object > 0)
					{
						  $scope.count = 0;
						  var v = 0;
						  if($scope.encounterDetailsList.length > 0)
						  {
							  angular.forEach($scope.encounterDetailsList, function(value, key) 
										{
										 v++;
										 if(!angular.equals({}, value.file)){
											var promise = $scope.uploadEncounterDoc(value,response.data.object);
											promise.then(function(response) 
													{
													  if(response.data.status == "success")
														  {
														    $scope.count++;
														    if($scope.encounterDetailsList.length==$scope.count)
															 {
														    	$scope.listPaymentDetails = [];
														    	  $scope.finalDocUploadList=[];
																   $scope.encounterDetailsList = [];
														    	angular.element('#collectPayment').modal('hide');
																$(".modal-backdrop").hide();
																$state.go('companyReceipts');	
															 }
													      }
													  else 
														{
															growl.error('Something went wrong.',
																	{
																		title : 'Error!'
																	});
														}
												    });
										 }else{
											 if(v == $scope.encounterDetailsList.length)
												 {
												 	$scope.listPaymentDetails = [];
												 	$scope.finalDocUploadList=[];
												 	$scope.encounterDetailsList = [];
												    angular.element('#collectPayment').modal('hide');
												    $(".modal-backdrop").hide();
												    $state.go('companyReceipts');	
												 }
										 }
												
										});
						  }else{
							    angular.element('#collectPayment').modal('hide');
								$(".modal-backdrop").hide();
								$state.go('companyReceipts');	
						  }
							 
					}else{
						angular.element('#collectPayment').modal('hide');
						$(".modal-backdrop").hide();
						$state.go('miscellaneousExpense');	
					}
				}else{
						$rootScope.stopLoaderPopup();
				}
			});
			
		}
		
		$scope.type=0;
		$scope.miscellaneousExpenseRecieptDetailsDtosList = [];
		$scope.miscellaneousExpenseRecieptDetailsDtosListMain = [];
		if (!angular.equals({},
				$stateParams.miscellaneousObj)) {
			$scope.newMiscellaneousObj = $stateParams.miscellaneousObj;
			
			$scope.type= $scope.newMiscellaneousObj.type;
			if($scope.newMiscellaneousObj.type == 1){
				$scope.detailsView = true;
			} 
			if($scope.newMiscellaneousObj.type == 2){
				$scope.receivedAmt = ($scope.newMiscellaneousObj.receivedAmount > 0)?$scope.newMiscellaneousObj.receivedAmount:parseFloat(0);
				
		        	$scope.encounterDetailsList = [];
		        	$scope.encounterDetailsList.push($scope.encounterDetails);
		           
				
			}
			console.log($scope.newMiscellaneousObj);
			//return false;
			$scope.enableDocSaveBitton = true;
			$scope.reqStatus =  $scope.newMiscellaneousObj.reqStatus;
			//$scope.billType = $scope.newMiscellaneousObj.billType;
			$scope.companyTypeId = $scope.newMiscellaneousObj.companyTypeId;
			$scope.getCompaniesByCompanyType($scope.companyTypeId);
		
			$scope.billDate = new Date($scope.newMiscellaneousObj.billDateTime);
			$scope.billNumber = $scope.newMiscellaneousObj.billNumber;
			$scope.name = $scope.newMiscellaneousObj.name;
			$scope.amount = $scope.newMiscellaneousObj.amount;
			$scope.name = $scope.newMiscellaneousObj.name;
			$scope.expenseId = $scope.newMiscellaneousObj.companyReceiptsExpenseId;
			$scope.remark = $scope.newMiscellaneousObj.remark;
			
			// $timeout( function(){
				 $scope.companyId = $scope.newMiscellaneousObj.companyId;
					$scope.getInsuranceByCompanyId($scope.companyId);
				// $timeout( function(){
					 $scope.insuranceCompanyId = $scope.newMiscellaneousObj.insuranceCompanyId;
						
	          // },2000);
					
           //},3000);
			 
			
			
			$rootScope.startLoaderPopup();
			
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/companyReceipts/details/"+$scope.newMiscellaneousObj.companyReceiptsExpenseId;
			
			BillingGenericService.serviceAction(METHOD_GET,URI, {}, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
			{
				if(response.data.status == "success"){
					
				
					$scope.miscellaneousExpenseRecieptDetailsDtosListMain = response.data.listObject;
					
					$rootScope.stopLoaderPopup();
			}else{
					$rootScope.stopLoaderPopup();
			}
			});
			
		}
		
		$scope.getDetailsPaymentMode = function(item){
			$scope.miscellaneousExpenseRecieptDetailsDtosList = item.companyRecieptsReceiptDetailsDtoList;
		}
		
		
		$scope.changeAmt = function(amt){
			if($scope.expenseId > 0){
				
			}else{
				$scope.listPaymentDetails = [];
			}
			
		}
		
		$scope.cancelClick = function(item){
			$scope.cancelReceiptObj = {};
			$scope.cancelReceiptObj = item;
		}
		
		$scope.cancelReceipt = function(){
			
			var data = {
					companyReceiptsExpenseRecieptId : $scope.cancelReceiptObj.companyReceiptsExpenseRecieptId,
					recieptCancelBy : $scope.userId,
					createdBy : $scope.userId,
					companyReceiptsExpenseId : $scope.cancelReceiptObj.companyReceiptsExpenseId
			}
			
			$rootScope.startLoaderPopup();
			
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/companyReceipts/receipt/cancel";
			
			BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
					{
						if(response.data.status == "success"){
							if($scope.newMiscellaneousObj.companyReceiptsExpenseId > 0){
								
								var URI = BASE_URL + ROOT_URL + API + BILLING + "/companyReceipts/details/"+$scope.newMiscellaneousObj.companyReceiptsExpenseId;
								
								BillingGenericService.serviceAction(METHOD_GET,URI, {}, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								{
									if(response.data.status == "success"){
										
										$scope.companyTypeId = $scope.newMiscellaneousObj.companyTypeId;
										$scope.getCompaniesByCompanyType($scope.companyTypeId);
									
										$scope.billDate = new Date($scope.newMiscellaneousObj.billDateTime);
										$scope.billNumber = $scope.newMiscellaneousObj.billNumber;
										$scope.name = $scope.newMiscellaneousObj.name;
										$scope.amount = $scope.newMiscellaneousObj.amount;
										$scope.name = $scope.newMiscellaneousObj.name;
										$scope.expenseId = $scope.newMiscellaneousObj.companyReceiptsExpenseId;
										$scope.remark = $scope.newMiscellaneousObj.remark;
										
										$scope.miscellaneousExpenseRecieptDetailsDtosListMain = response.data.listObject;
										
										if($scope.newMiscellaneousObj.type == 2){
											$scope.receivedAmt = parseFloat($scope.receivedAmt) - parseFloat($scope.cancelReceiptObj.amount);
											//$scope.amountPending = parseFloat($scope.amount) + parseFloat($scope.cancelReceiptObj.amount);
											//$scope.paymentDetails.cashAmount = $scope.amountPending + parseFloat($scope.cancelReceiptObj.amount);
										}
										
										
								}else{
								}
								});
							}
							$rootScope.stopLoaderPopup();
					}else{
							$rootScope.stopLoaderPopup();
					}
					});
		}
		
	}catch(e){
		console.log(e.message);
	}
}]);/**
 * 
 */