angular.module("myApp").controller("addNewMiscellaneousExpenseController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','$timeout','growl','paymentCollFactory','reportService','GenericService','$location','loyaltiCardCallFactory','$stateParams','GenericServiceFileUpload',"$interval",
	function($scope,$rootScope,BillingGenericService,$cookies,$state,$timeout,growl,paymentCollFactory,reportService,GenericService,$location,loyaltiCardCallFactory,$stateParams,GenericServiceFileUpload,$interval){
	try{
		
		
		
		
		$scope.init = function(){
			
			$rootScope.loginpage = true;
			$scope.page = "addNewMiscellaneous";
			
			$scope.dateOptions = {
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
			$scope.unitId = cookieObject.unitId;
			$scope.userId = cookieObject.userId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.mpesaRequired = cookieObject.mpesaRequired;
			$scope.userId =$scope.userId;
			$scope.billDate = new Date();
			$scope.skipMpesaValidation = false;
			$scope.hitMpesaApi = false;
			$scope.receivedAmt = parseFloat(0);
			$scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
			$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
			$scope.fileObj = {};
			$scope.fileObj.file = null;
			$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
			$scope.transactionId="";
			$scope.mobileNo="";
			$scope.mpesaAmount="";
			$scope.mobileNumber="";
			$scope.validateMpesaAmount="";
			
			$scope.mpesaObj={
					transactionId:"",
					mobileNo:"",
					mpesaAmount:"",
					mobileNumber:"",
					validateMpesaAmount:""
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
					if(typeof($scope.paymentDetails.credAmount) == "undefined" || $scope.paymentDetails.credAmount == "" || $scope.paymentDetails.credAmount == parseFloat(0)  || $scope.paymentDetails.credAmount < parseFloat(0)){
						growl.warning("Please enter  amount",{
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
					
					
				case 5:
					
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
					
					
					var paymentDetails = { paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaObj.mpesaAmount).toFixed(2), createdBy:$scope.userId, cardNo:$scope.mpesaObj.transactionId,transactionNo:$scope.mpesaObj.transactionId,skipMpesaValidation:$scope.skipMpesaValidation};
					$scope.listPaymentDetails.push(paymentDetails);
					$scope.clearMpesaFields();
					
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
				
			}else if($scope.paymentDetails.paymentModeId == 5){
				
				exAmount = $scope.mpesaObj.mpesaAmount;
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
		
		$scope.settlementClick = function(){
			
			/*if($scope.encounterDetailsList.length > 0){*/
			if($scope.billType == '' || $scope.billType == undefined || $scope.billType == null || $scope.billType == 0){
				 growl.warning("Please select bill type..!!",{
						title : WARNING_MSG
					});
					return false;
			}else if($scope.billDate == '' || $scope.billDate == undefined || $scope.billDate == null){
				growl.warning("Please select bill date..!!",{
					title : WARNING_MSG
				});
				return false;
			}
			else if($scope.billNumber == '' || $scope.billNumber == undefined || $scope.billNumber == null){
				growl.warning("Please enter bill number..!!",{
					title : WARNING_MSG
				});
				return false;
			}
			else if($scope.name == '' || $scope.name == undefined || $scope.name == null){
				growl.warning("Please enter name..!!",{
					title : WARNING_MSG
				});
				return false;
			}
			else if($scope.accountHead == '' || $scope.accountHead == undefined || $scope.accountHead == null || $scope.accountHead == 0){
				growl.warning("Please select ledger..!!",{
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
			  documentTypeId :63,
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
							var promise = $scope.uploadEncounterDoc(value,$scope.newMiscellaneousObj.expenseId);
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
												$state.go('miscellaneousExpense');	
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
								    $state.go('miscellaneousExpense');	
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
			        fd.append('documentTypeId',33);
			        fd.append('createdBy',Obj.createdBy );
			        fd.append('updatedBy', Obj.updatedBy);
			        fd.append('unitId', Obj.unitId);
			        fd.append('organisationId', Obj.organisationId);
			        fd.append('expenseId',expenseId);
			        fd.append('grnId',0);
					fd.append('poId',0);
			        fd.append('file',Obj.file );
			        fd.append('incidentReportingMapperId',0);
			        fd.append('companyExpenseId',0);
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
		        "expenseId":$scope.expenseId,
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
		
			$scope.uploadEncounterDoc1 = function(Obj)
			{
				 var fd = new FormData();
			        fd.append('organizationId', $scope.organizationId);
			        fd.append('billId',Obj);
			        fd.append('isDeposite',false);
			        fd.append('isPharmacy',false);
			        fd.append('isMiscellaneous',true);
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
			
		$scope.saveMiscellaneous = function(){
			var collectedAmount = parseFloat(0).toFixed();
			angular.forEach($scope.listPaymentDetails,function(value,key){
				if(value.paymentModeId == 1){
					collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
				}
			});
			
			if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
				$('#uploadpanCardDetailsMissle').modal('show');
					return false;
			}
			$('#uploadpanCardDetailsMissle').modal('hide');
			
			
			var data ={
					"billNumber":($scope.billNumber == undefined || $scope.billNumber == '')?null:$scope.billNumber,
					"billDateTime": moment($scope.billDate).format('YYYY-MM-DD HH:mm:ss'),
					"amount": $scope.amount,
					"billType":$scope.billType,
					"name": ($scope.name == undefined || $scope.name == '')?null:$scope.name,
					"accountHead":($scope.accountHead == undefined || $scope.accountHead == '')?null:$scope.accountHead,
					"unitId":$scope.unitId,
					"orgId": $scope.organizationId,
					"createdBy": $scope.userId,
					"updatedBy":$scope.userId,
					"miscellaneousExpenseRecieptDetailsDtosList":$scope.listPaymentDetails,
					"remark":$scope.remark
				}
			if($scope.type == 2){
				data.expenseId = $scope.expenseId;
				data.receivedAmount = $scope.receivedAmt;
			}
			console.log(data);
			$rootScope.startLoaderPopup();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/expense/save";
			BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
			{
				if(response.data.status == "success"){
					angular.element('#collectPayment').modal('hide');
					$(".modal-backdrop").hide();
					$state.go('miscellaneousExpense');
					$rootScope.stopLoaderPopup();
					
					if(response.data.object > 0)
					{
						if($scope.fileObj.file != null){
							$scope.uploadEncounterDoc1(response.data.object);
						}
						
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
																$state.go('miscellaneousExpense');	
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
												    $state.go('miscellaneousExpense');	
												 }
										 }
												
										});
						  }else{
							    angular.element('#collectPayment').modal('hide');
								$(".modal-backdrop").hide();
								$state.go('miscellaneousExpense');	
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
					mpesaTxcode:$scope.mpesaObj.transactionId,
					mpesaSendermobile:$scope.mpesaObj.mobileNo,
					mpesaAmount:$scope.mpesaObj.mpesaAmount
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
	
		
		$scope.checkoutRequestId = null;
		$scope.mpesaPaymentRequest = function(){
			
			if($scope.mpesaObj.validateMpesaAmount == undefined || $scope.mpesaObj.validateMpesaAmount == null){
				
				growl.warning("Please Enter Amount",{
					title : "warning!!!"
				});
				
			}else if($scope.mpesaObj.validateMpesaAmount > $scope.dueAmount){
				
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
						phoneNumber:parseInt($scope.countryCallingCode+""+$scope.mpesaObj.mobileNumber),
						partyA:$scope.mpesaObj.mobileNumber,
						amount:$scope.mpesaObj.validateMpesaAmount
				};
			
				if(($scope.mpesaObj.mobileNumber != undefined && $scope.mpesaObj.mobileNumber != null && $scope.mpesaObj.mobileNumber != "") ||
						($scope.countryCallingCode != undefined && $scope.countryCallingCode != null && $scope.countryCallingCode != "")){
					$rootScope.startLoaderPopup();
					
					var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
					BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						
						if (response.data.status == "success")
							{
							$scope.checkoutRequestId = response.data.object.checkoutRequestId;
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
						checkoutRequestId:$scope.checkoutRequestId
						
				};
				$scope.skipMpesaValidation = true;
				$rootScope.startLoaderPopup();
				var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
				BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					
					if (response.data.status == "success")
						{
						$scope.mpesaObj.mpesaAmount = response.data.object.amount;
						$scope.mpesaObj.transactionId = response.data.object.transactionId;
						$scope.mpesaObj.mobileNo = response.data.object.phoneNumber;
						if($scope.mpesaObj.transactionId != null ){
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
						
						growl.error(response.data.message != null ? response.data.message :"Please wait",{
							title : ERROR_MSG
						});
						$rootScope.stopLoaderPopup();
					}
					
				});
				
			}

		$scope.cancelPaymentReqModal = function(){
			angular.element('#paymentReqModal').modal('hide');
		}
		
		$scope.clearMpesaFields = function(){
			$scope.skipMpesaValidation = false;
			$scope.mpesaObj.mobileNo = null;
			$scope.mpesaObj.mpesaAmount = '';
			$scope.mpesaObj.transactionId = null;
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
			$scope.enableDocSaveBitton = true;
			$scope.reqStatus =  $scope.newMiscellaneousObj.reqStatus;
			$scope.billType = $scope.newMiscellaneousObj.billType;
			$scope.billDate = new Date($scope.newMiscellaneousObj.billDateTime);
			$scope.billNumber = $scope.newMiscellaneousObj.billNumber;
			$scope.name = $scope.newMiscellaneousObj.name;
			$scope.accountHead = $scope.newMiscellaneousObj.accountHead;
			$scope.amount = $scope.newMiscellaneousObj.amount;
			$scope.name = $scope.newMiscellaneousObj.name;
			$scope.expenseId = $scope.newMiscellaneousObj.expenseId;
			$scope.remark = $scope.newMiscellaneousObj.remark;
			
			$rootScope.startLoaderPopup();
			
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/expense/details/"+$scope.newMiscellaneousObj.expenseId;
			
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
			$scope.miscellaneousExpenseRecieptDetailsDtosList = item.miscellaneousExpenseRecieptDetailsDtosList;
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
					expenseRecieptId : $scope.cancelReceiptObj.expenseRecieptId,
					recieptCancelBy : $scope.userId,
					createdBy : $scope.userId,
					expenseId : $scope.cancelReceiptObj.expenseId
			}
			
			$rootScope.startLoaderPopup();
			
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/expense/receipt/cancel";
			
			BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
					{
						if(response.data.status == "success"){
							if($scope.newMiscellaneousObj.expenseId > 0){
								
								var URI = BASE_URL + ROOT_URL + API + BILLING + "/expense/details/"+$scope.newMiscellaneousObj.expenseId;
								
								BillingGenericService.serviceAction(METHOD_GET,URI, {}, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								{
									if(response.data.status == "success"){
										
										$scope.billType = $scope.newMiscellaneousObj.billType;
										$scope.billDate = new Date($scope.newMiscellaneousObj.billDateTime);
										$scope.billNumber = $scope.newMiscellaneousObj.billNumber;
										$scope.name = $scope.newMiscellaneousObj.name;
										$scope.accountHead = $scope.newMiscellaneousObj.accountHead;
										$scope.amount = $scope.newMiscellaneousObj.amount;
										$scope.name = $scope.newMiscellaneousObj.name;
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
}]);