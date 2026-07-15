(function () {
	"use strict";
	function detailsCorporateStatementController($scope,$rootScope,BillingGenericService,GenericService,GenericServiceParamHeader,$cookies,$state,growl,servicesFactory,itemFactory,$timeout,reportService,PaginationService){
	try{
$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		 if(cookieObjectAppConfig == undefined){
			 $state.go('login');
		  return;
		 }
		 else
		  {
		   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
		  }
		 $scope.isConsolidatedBill=cookieObjectAppConfig.isConsolidatedBill;
		 
		 /**DatePicker Code  */
		    var corporateStatmentObject = JSON.parse(sessionStorage.getItem('corporateStatmentObject'));
		    if (corporateStatmentObject != undefined) {
		    $scope.companyInvoiceId = corporateStatmentObject.companyInvoiceId;
		    $scope.invoiceDate = corporateStatmentObject.invoiceDate;
			 $scope.invoiceNumber = corporateStatmentObject.invoiceNumber;
			 $scope.companyName = corporateStatmentObject.companyName;
			 }
			$scope.fromDate = moment().startOf('day').toDate();
		    $scope.toDate = moment().startOf('day').toDate();
		    $scope.referenceDate =  moment($scope.invoiceDate, "DD-MM-YYYY").toDate();
		    
		    var labConfig = JSON.parse(localStorage.getItem('labConfig'));
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$scope.labConfig =labConfig;
			$scope.labSpecialityId = labConfig==undefined ? 0 : labConfig.labSpecialityId;
			$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
			$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
			$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
			$scope.isSplitBillUnitFlag = cookieObjectAppConfig.isServiceWiseSplitBill;
			$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
			$scope.consultationSpecialityId = cookieObjectAppConfig.consultationSpecialityId;
			$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
			$scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
			$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
			$scope.isRenderedServiceRateEditable = cookieObjectAppConfig.isRenderedServiceRateEditable;
			$scope.isSchemeDiscount = cookieObjectAppConfig.isSchemeDiscount;
			$scope.isGstVisible = cookieObjectAppConfig.isGstVisible;
		    $scope.defaultDiscountOwnership = cookieObjectAppConfig.defaultDiscountOwnership;
		    $scope.isSmartCardRequired = cookieObjectAppConfig.isSmartCardRequired;
		    $scope.isPackageApplicableDateEditable = cookieObjectAppConfig.isPackageApplicableDateEditable;
			$scope.unitId = cookieObject.unitId;
			$scope.isRenderedServiceCancelled = cookieObject.isRenderedServiceCancelled;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.isUpdateIpdChargesRemark = cookieObject.isUpdateIpdChargesRemark;
			
		    $scope.completed='N';
			$scope.pending = 'N';
			$scope.outsourced = 'N';
			$scope.isDisable=false;
			$scope.dateOptions =
			{
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate : "",
			};

			$scope.open = function() {
				$scope.popup.opened = true;
			};

			$scope.popup =
			{
				opened : false
			};
			
			$scope.popup1 =
			{
				opened : false
			};
			$scope.popup2 =
			{
				opened : false
			};
			$scope.popup3 =
			{
				opened : false
			};
			$scope.popup4 =
			{
				opened : false
			};
			$scope.popup5 =
			{
				opened : false
			};
			$scope.popup6 =
			{
				opened : false
			};
			$scope.popup7 =
			{
				opened : false
			};
			
			$scope.open1 = function() 
			{
				$scope.popup1.opened = true;
			};
			$scope.open2 = function() 
			{
				$scope.popup2.opened = true;
			};
			$scope.open3 = function() 
			{
				$scope.popup3.opened = true;
			};
			$scope.open4 = function() 
			{
				$scope.popup4.opened = true;
			};
			$scope.open5 = function() 
			{
				$scope.popup5.opened = true;
			};
			$scope.open6 = function() 
			{
				$scope.popup6.opened = true;
			};
			$scope.open7 = function() 
			{
				$scope.popup7.opened = true;
			};

			 $scope.pagination = {
	                 currentPage: 1,
	                 totalPages: 0,
	                 pageSize: '10',
	                 search: '',
	                 searchData: PaginationService.searchData,
	                 setTotalPages: PaginationService.setTotalPages,
	                 getPagesList: PaginationService.getPagesList,
	                 setPage: PaginationService.setPage,
	                 setFirstPage: PaginationService.setFirstPage,
	                 setPreviousPage: PaginationService.setPreviousPage,
	                 setNextPage: PaginationService.setNextPage,
	                 setLastPage: PaginationService.setLastPage,
	                 sortBy: PaginationService.sortBy
	               }
             $scope.request = {
					"firstName":"",
					 "fatherName":"",
					 "investigation":"",
					 "doneOnDate":new Date(),
					 "consultant":"",
					 "orgName":"",
					 "disease":"",
					 "hospitalName":"QUEENS NRI HOSPITAL",
					 "hospitalTreatment":"",
					 "visitfromDate":"",
					 "visittoDate":"",
					 "treatfromDate":"",
					 "consDate":"",
					 "employeeName":"",
					 "medicalCardNo":"",
					 "authorizationLetterNo":"",
					 "relation":"",
					 "consAmount":0,
					 "labAmount":0,
					 "pharmacyAmount":0,
					 "address1":"",
					 "address1":"",
			 }
			 
			    $scope.checkListObjId = [];
				$scope.selectedcheckListId=[];
				$scope.checkOpVisit = [];
				$scope.selectedReportlistOP = [];
				$scope.checkIPVisit = [];
				$scope.selectedReportlistIP = [];
				$scope.checkInvesVisit = [];
				$scope.selectedReportlistInvest = [];
				$scope.checkInvesDoccument = [];
				$scope.selectedReportlistDoccument = [];
				
				
			 
			$scope.init = function(){
				 $scope.getrelationList();
				$scope.getCompanyInvoiceDetailsByInvoiceId();
			}
			 $scope.getrelationList = function(){
				 var data = {};
					var URI = BASE_URL + ROOT_URL
					+ GETACTIVERELATIONLIST;
			GenericService
					.serviceAction("GET", URI, data)
					.then(
							function(response) {
								$scope.relationList = [];

								if (response.data.status == "success") {
									$scope.relationList = response.data.listObject;

								}
							});
			 }
			 
			  var isGroupExistsCap;
			  function checkGroupIdExistsOrNotCap(arr, val) {
					return arr.some(function(arr) {
							if(val.patientId === arr.patientId)
									{
										return true;
									}
								else
									{
										return false;
									}
							});
				}
			 
			 $scope.getCompanyInvoiceDetailsByInvoiceId = function(){
				 $scope.invoiceDetailsTemporaryList = [];
				 $scope.invoiceDetails = [];
			    	var URI = BASE_URL + ROOT_URL + "/api/billing/invoicedtls/invoice/"+$scope.companyInvoiceId;
			    	$rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							 $scope.invoiceDetailsTemporaryList = response.data.listObject;
							 angular.forEach($scope.invoiceDetailsTemporaryList ,function(value,key){
								 
									if($scope.invoiceDetails.length==0){
										$scope.invoiceDetails.push(value);
									}else{
										
										isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.invoiceDetails,value)
										  if (!isGroupExistsCap) {
											$scope.invoiceDetails.push(value);
										  }
									}
							 });
							 console.log("$scope.invoiceDetails",$scope.invoiceDetails);
							 console.log("$scope.invoiceDetailsTemporaryList",$scope.invoiceDetailsTemporaryList);
						}
						$rootScope.stopLoader();
					});
		    }
			 
			 
		      
		        $scope.deatilsCompanyInvoiceList = function() {
		        	$scope.checkList = [];
					  var param = {
							  "organizationId":$scope.organizationId,
							  "unitId":$scope.unitId,
							  };
					    var URI = BASE_URL + ROOT_URL + "/adt/getCorporateStatementChecklistMasterListAll"
					    
					    BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if(response.data.status == "success"){
								angular.forEach(response.data.listObject,function(value,key){
									
									var found = value.visitTypeMasterDtosList.find(ar=>ar.id==$scope.visitTypeId);
									   if(found != undefined)
										   {
										   $scope.checkListObjId[value.corporateStatementChecklistId] = true;
											var temp = {
												     corporateStatementChecklistId:value.corporateStatementChecklistId,
												  };
										   $scope.selectedcheckListId.push(temp);
										   $scope.checkList.push(value);
										   }else{
											   $scope.checkListObjId[value.corporateStatementChecklistId] = false;
												var temp = {
													     corporateStatementChecklistId:value.corporateStatementChecklistId
													  };
											   $scope.checkList.push(value);
										   }
								});
							}
						});
			
					  }
		        
			 $scope.getEmployeeNameOP = function(data){
				 if(data.encounterId != null){
				 var URL = BASE_URL + ROOT_URL +"/api/opd/sponsoredDetails/admission/0/encounter/"+ data.encounterId;
					BillingGenericService.serviceAction(METHOD_GET,URL, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							
							angular.forEach(response.data.listObject,function(value){
								$scope.request.employeeName = value.employeeName;
								$scope.request.medicalCardNo = value.medicalCardNo;
								$scope.request.relation = value.relation;
								
							});
							 
						}
					});
				 }
			 }
			 
			 $scope.getEmployeeName = function(data){
				 if(data.admissionId != null){
					var URI = BASE_URL + ROOT_URL + "/api/opd/sponsoredDetails/admission/"+ data.admissionId+"/encounter/0";
					BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							angular.forEach(response.data.listObject,function(value){
								$scope.request.employeeName = value.employeeName;
								$scope.request.medicalCardNo = value.medicalCardNo;
								$scope.request.relation = value.relation;
								
							});							 
						}
					});
				 }
			 }
				$scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				}
				
				
				
				
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
							"loyaltyCardAmt"        :'',
							"cardBalAmount"         :'',
							"loyaltyCardNo"         :'',
							'otp'                   :'',
							'hospitalAccountId'     : '',
							'transactionNo'         : ""
					}
				};
				$scope.initPaymentDetails();
				
				
				$scope.request.consAmount = 0;
				$scope.request.labAmount = 0;
				$scope.request.pharmacyAmount = 0;
				
				$scope.billDetails = function(bill){
					 var promise={};
					 promise=$scope.getBillReferenceData(bill.billId);
					 promise.then(function(response) {
						 $scope.billRefData=response;
							$scope.patientEmail = bill.email;
							$scope.patientName = bill.patientName;
							if(bill.mobileNo!= null){
								$scope.mobileNumber = parseInt(bill.mobileNo);
								$scope.razorPayObject.razorMobileNo = $scope.mobileNumber;
							}
							
							$scope.excessAmount = parseFloat(0).toFixed(2);	
							$scope.transferToActiveDeposit = 'N'
							$scope.amountPaid = bill.amountPaid;
							$scope.selectedUhid = bill.uhidNumber;
							$scope.selectedPatientId = bill.patientId;
							$scope.rountOffString = '';
							console.log("BILL",bill);
							$scope.validatTextFieldFlag = true;
							$scope.billServicesList=[];
							$scope.paymentReceiptsDetailsList=[];
							$scope.listPaymentDetails = [];
							$scope.payeeList = [];
							$scope.totalAmtQuaBase = 0;
							$scope.totalAmtConcession = 0;
							$scope.totalAmtDiscount = 0;
							$scope.totalAmtGst = 0;
							$scope.totalNetPayableAmt = 0;
							$scope.payeeDueAmtFinal = 0;
							$scope.payeeDueAmtStatic = 0;
							$scope.patientIdForReport = bill.patientId;
							$scope.visitTypeId = bill.visitTypeId;
							$scope.referalDocId =  $scope.billRefData.referalDocId;
							$scope.consumedAmt = parseFloat(0);
							$scope.payeeId = $scope.selfPayeeIdCookies;
							$scope.isPaidAppointment = $scope.billRefData.isPaidAppointment!=null ? $scope.billRefData.isPaidAppointment : 'N';
							$scope.billDisabled = (bill.billStatusId == 3 || ( $scope.billRefData.intermediateSelfDue!=null &&   $scope.billRefData.intermediateSelfDue>0)) ? true : false;
							$scope.isInterimOrFinalBill = bill.isInterimOrFinalBill;
							$scope.isNursingAssessmentRequired = bill.isNursingAssessmentRequired;
							$scope.disallowedAmount = bill.disallowedAmount;
							$scope.disallowedAgainstInvoiceDtlsId = bill.disallowedAgainstInvoiceDtlsId;
							$scope.doctorId = bill.doctorId;
							$scope.totalAmount = bill.netAmount; 
							$scope.dueAmount = bill.outstanding;
							$scope.oldDueAmount = bill.outstanding;
							$scope.billDueAmount = bill.outstanding;
							$scope.totalSelfPayable = bill.totalSelfPayable;
							$scope.totalCreditPayable=bill.totalCreditPayable;
							$scope.currentBill = bill;
							$scope.admissionId =bill.visitTypeId != 1 ? $scope.billRefData.visitId : null;
							$scope.isFinancialClearanceFlag = $scope.billRefData.isFinancialClearance;
							//this.$parent.isFinancialClearence = bill.isFinancialClearance;
							
							//Vivek added
							$scope.encounterId =$scope.billRefData.visitId;
							$scope.appointmentId = $scope.billRefData.appointmentId;
							//End Vivek Added
							
							$scope.pendingCountY = 0;
							$scope.pendingCountN = 0;
							
							//get payer details
							$scope.mpesaObject={};
							var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+bill.billId;
							BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success")
								{
									$scope.payeeList = response.data.listObject;
									$scope.billServicesList = response.data.object.listObject;
									$scope.paymentReceiptsDetailsList=response.data.object.object;
									var totalAmtQuaBase = parseFloat(0);
									var totalAmtConcession = parseFloat(0);
									var totalAmtDiscount = parseFloat(0);
									var totalAmtGst = parseFloat(0);
									var totalNetPayableAmt = parseFloat(0);
									var discountRoundOff = parseFloat(0);
									var totalSelfPayable = parseFloat(0);
									var totalCreditPayable = parseFloat(0);
									var totalPackageConsumptionAmt = parseFloat(0);
									
									angular.forEach($scope.billServicesList,function(value,key)
											{
										angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
												{
												  if(value1.isPackageService=='N')
													  {
													  
													  if(value.packageConsumptionAmt == 0){
														  totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
													  }else{
														  totalAmtQuaBase = totalAmtQuaBase + parseFloat(value1.serviceAmount);
													  }
													    
														totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
														totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
														totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
														totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
													    totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
														totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
														discountRoundOff = parseFloat(value.roundOff);
														totalPackageConsumptionAmt = totalPackageConsumptionAmt + parseFloat(value.packageConsumptionAmt);
													  }
												});
										
									});
									
									$scope.totalAmtQuaBase =totalAmtQuaBase;
									$scope.totalAmtConcession = totalAmtConcession;
									$scope.totalAmtDiscount =totalAmtDiscount+Number(discountRoundOff);
									$scope.rountOffString = Number(discountRoundOff)!=null ? '(RoundOff : '+discountRoundOff+')' :'';
									$scope.totalAmtGst = (totalAmtGst).toFixed(2);
									$scope.totalNetPayableAmt = totalNetPayableAmt-(Number(discountRoundOff));
									$scope.totalPackageConsumptionAmt = totalPackageConsumptionAmt;
									
									angular.forEach($scope.payeeList,function(value1,key)
									{
										if(value1.payeeId == $scope.selfPayeeIdCookies)

											{
											$scope.payeeDueAmtFinal = Number(parseFloat(value1.dueAmount).toFixed(2));
											$scope.payeeDueAmtStatic = Number(parseFloat(value1.dueAmount).toFixed(2));
											}
										
									});
									
									angular.forEach($scope.payeeList,function(value,key){
										
										if(value.payeeId==$scope.payeeId && (value.billAmount==0 || value.dueAmount==0))
											$scope.billDisabled = true;
									})
									
									
									
									//get available active deposits by patient
									var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+bill.patientId+"/"+$scope.billRefData.visitId+"/"+bill.visitTypeId+"/0/false";
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
												
													activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
													$scope.availableDeposits[i].depositeFlag = 'N';
													activeDepositPatient = activeDepositPatient + parseFloat(value.availableDeposit);
												i++;
												
											});
											$scope.activeDeposit = activeDeposit;
											$scope.useActiveDepo = true;
											
										}
									});
									
									console.log("billdetais",$scope.billServicesList);
									 angular.forEach($scope.billServicesList,function(value1,key1)
												{
														 if( $scope.consultationSpecialityId == value1.specialityId && $scope.pharmacyServiceId != value1.serviceId){
																$scope.request.consAmount += value1.netAmt
															}
															if($scope.consultationSpecialityId != value1.specialityId && $scope.pharmacyServiceId != value1.serviceId){
																$scope.request.labAmount += value1.netAmt
															}
															if( $scope.pharmacyServiceId == value1.serviceId){
																$scope.request.pharmacyAmount += value1.netAmt
															}
												
												});
						/*		 angular.forEach($scope.billServicesList,function(value1,key1)
												{
										 angular.forEach(value1.orderDetailsPackageMapperDtosList,function(value,key)
													{
													 if(value.isPackageService=='N'){
														        $scope.request.consAmount = 0;
														        $scope.request.labAmount = 0;
														        $scope.request.pharmacyAmount = 0;
													 }
												});
												});*/
								/* angular.forEach($scope.billServicesList,function(value1,key1)
												{
										 angular.forEach(value1.orderDetailsPackageMapperDtosList,function(value,key)
													{
													 if(value.isPackageService=='N'){
																$scope.request.labAmount += value1.netAmt;
													 }
												});
												});*/
									
								}
							});
							 
							
					 });
					
				}
				
			
				$scope.checkReport=function(labObj,idx,printFlag)
				{
					if(labObj.sampleStatusId==8)
					{
						return true;
				   }
					else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
						{
		    	         return true;
						}
				 else
				 {
				     return false;
				  }
			 }
				
				 $scope.getPastVisitData = function()
			        {
					  $scope.opVisitList = [];
					  $scope.ipVisitList = [];
			         $rootScope.startLoader();
			         var data =
			           {
			           "patientId":$scope.patientId,
			           "organizationId":$scope.organizationId,
			          }
			         var URI = BASE_URL + ROOT_URL + "/api/global/getAllPatientVisits";
			         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			           $scope.patientPastVisitList = [];
			           if (response.data.status == "success") 
			           {
			            $scope.opVisitList = response.data.object.encounterList;
			            angular.forEach( $scope.opVisitList ,function(value,key){
			            	value.reportType = 1;
			            });
			            console.log("opd list",$scope.opVisitList);
			            $scope.ipVisitList = response.data.object.admissionList;
			            angular.forEach( $scope.ipVisitList ,function(value,key){
			            	value.reportType = 2;
			            });
			            console.log("ip list",$scope.ipVisitList);
			            $rootScope.stopLoader();
			           }
			           else if(response.data.status == "error")
			            {
			            $rootScope.stopLoader();
			            }
			          });
			         
			         var data1={};
						var paramObj={};
						var headers = {
								'patientId' : $scope.patientId,
								'flag' : true,
								'Content-Type' : 'application/json',
								'unitId':$scope.unitId
							};
			    		var URI2 = BASE_URL + ROOT_URL + "/api/opd/getInvestigationsListForCoversheet" ;
			    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI2,data1,NOTIFICATION_MSG_STATUS_FALSE, paramObj,
								headers)
			    		.then(
			    			function(response) {
			    				if (response.data.status == "success") {
			    					$scope.currentEncounterOrderLists = response.data.listObject;
			    					$scope.currentEncounterOrderLists1 = [];
			    					 angular.forEach( $scope.currentEncounterOrderLists ,function(value,index){
			 			            	value.reportType = 3;
			 			            	if(value.isPanel=='N'){
			 			            		var test = $scope.checkReport(value,index,1);
			 			            		value.checkReport = test;
			 			            		$scope.currentEncounterOrderLists1.push(value);
			 			            	}
			 			            });
			    				}
			    				else if(response.data.status == "error")
			    					{
			    					   $rootScope.stopLoader();
			    					}
			    			});
			    		
			    		var data2 =
				        {
				        "patientId":$scope.patientId,
				        "unitId":$scope.unitId,
				        "organisationId":$scope.organizationId,
				        "userId":undefined,
				        "visitTypeId":undefined,
				        "admissionId":undefined,
				       }
				      var URI3 = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
				      BillingGenericService.serviceAction(METHOD_POST, URI3, data2, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
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
			    		
			        }
				 $scope.checkSavedData = function(patientId){
					 
					 var data = {
								billId:patientId,
								invoiceNumber:$scope.invoiceNumber
					          }
						var URI = BASE_URL + ROOT_URL+ "/unit/CompanyStatment/checkAlreadyExit";
				
						GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
									if(response.data.status == "success"){
										if(response.data.listObject.length > 0){
											$scope.corporateStatementId = response.data.listObject[0].corporateStatementId;
										}
									   }
										});
				 }
			 $scope.savedData = [];
			 $scope.selectedInvoiceDetails = function(patientId){
				 $scope.checkSavedData(patientId);
				
					 $scope.request = {
								"firstName":"",
								 "fatherName":"",
								 "investigation":"",
								 "doneOnDate":new Date(),
								 "consultant":"",
								 "orgName":"",
								 "disease":"",
								 "hospitalName":"QUEENS NRI HOSPITAL",
								 "hospitalTreatment":"",
								 "visitfromDate":"",
								 "visittoDate":"",
								 "treatfromDate":"",
								 "consDate":"",
								 "employeeName":"",
								 "relation":"",
								 "consAmount":0,
								 "labAmount":0,
								 "pharmacyAmount":0,
								 "address1":"",
								 "address1":"",
						 }
					
                   
					 angular.forEach( $scope.invoiceDetailsTemporaryList,function(value,index){
						 if(value.patientId == patientId){
							$rootScope.startLoader();
							$scope.request.firstName = value.patientName;
							$scope.request.fatherName = value.fatherName;
							$scope.patientId = value.patientId;
							$scope.visitTypeId = value.visitTypeId;
							$scope.request.consultant = value.doctorName;
							$scope.request.orgName = $scope.companyName;
							value.admissionId = value.id;
							if(value.encounterId != null){
								$scope.request.visitfromDate = moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.request.visittoDate = moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.request.treatfromDate =moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.request.treattoDate =moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.request.consDate = moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.getEmployeeNameOP(value); 
								value.visitTypeId = 1;
								$scope.visitTypeId = 1;
							}else{
								$scope.request.visitfromDate = moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.request.visittoDate = moment(value.pdd, "DD/MM/YYYY").toDate();
								$scope.request.treatfromDate =moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.request.treattoDate =moment(value.pdd, "DD/MM/YYYY").toDate();
								$scope.request.consDate = moment(value.visitDate, "DD/MM/YYYY").toDate();
								$scope.getEmployeeName(value); 
								$scope.visitTypeId = value.visitTypeId;
							}
							$scope.request.address1 = value.address;
//							$scope.billDetails(value);
							 angular.forEach(value.tCompanyInvoiceServiceDetailsDto,function(value1,key1)
										{
												 if( $scope.consultationSpecialityId == value1.specialityId && $scope.pharmacyServiceId != value1.serviceId){
														$scope.request.consAmount += value1.creditPayableAmount;
													}
													/*if($scope.consultationSpecialityId != value1.specialityId && $scope.pharmacyServiceId != value1.serviceId){
														$scope.request.labAmount +=  parseFloat(value1.creditPayableAmount);
													}*/
												    if($scope.labSpecialityId == value1.specialityId || $scope.radiologySpecialityId == value1.specialityId || 
												    		value1.isProcedure == 'Y' || value1.procedureTypeId == 1 || value1.isBedSide == 'Y'){
														$scope.request.labAmount +=  parseFloat(value1.creditPayableAmount);
													}
													if( $scope.pharmacyServiceId == value1.serviceId || value1.saleId != 0){
														$scope.request.pharmacyAmount += value1.creditPayableAmount;
													}
										
										});
						}
					});
					 
					 if($scope.request.labAmount){
							$scope.request.labAmount = $scope.request.labAmount.toFixed(2);
						}
					 $timeout( function(){
	                   	  $rootScope.stopLoader();
					
					 angular.forEach($scope.invoiceDetailsTemporaryList,function(value,key){
						 if(value.patientId == patientId){
						 $scope.billIds = value.billId;
						 $scope.billId = patientId;
							$rootScope.startLoader();
							$scope.saveBtnFlag = false;
							$scope.updateScope = true;
							$scope.updateBtnFlag = true;
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/unit/CompanyStatment/getCorporateStatementMasterById/" +S+ patientId +S+ $scope.organizationId +S+ $scope.unitId;
							console.log("EDIT",URI);
							GenericService.serviceAction("GET",URI).then(function(response){
								if(response.data.status == "success"){
									$rootScope.stopLoader();
									
									$scope.savedData = response.data.listObject;
									if(response.data.listObject.length > 0){
										$scope.request.consAmount = 0;
										$scope.request.labAmount = 0;
										$scope.request.pharmacyAmount = 0;
										$scope.patientId = value.patientId;
										$scope.visitTypeId = value.visitTypeId;
										$scope.request = response.data.listObject[0];
										$scope.request.corporateStatementId = response.data.listObject[0].corporateStatementId;
										$scope.request.visitfromDate =  moment(response.data.listObject[0].visitfromDate, "DD-MM-YYYY").toDate();
										$scope.request.visittoDate =  moment(response.data.listObject[0].visittoDate, "DD-MM-YYYY").toDate();
										$scope.request.treatfromDate =  moment(response.data.listObject[0].treatfromDate, "DD-MM-YYYY").toDate();
										$scope.request.treattoDate =  moment(response.data.listObject[0].treattoDate, "DD-MM-YYYY").toDate();
										$scope.request.consDate =  moment(response.data.listObject[0].consDate, "DD-MM-YYYY").toDate();
										$scope.request.doneOnDate =  moment(response.data.listObject[0].doneOnDate, "DD-MM-YYYY").toDate();
									
									}
								
								
								}
							});
						 }
					 });
					 },5000);
					 $scope.deatilsCompanyInvoiceList();
					 $scope.getPastVisitData();
					 
						
				            
		    }
			 
			 $scope.init();
			 
			 
			 $scope.save = function(){
				 
					$scope.currentDate = new Date();
					$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
				
					var data = {
						visitTypeId:$scope.visitTypeId,
						invoiceNumber:$scope.invoiceNumber,
						billId:parseInt($scope.patientId),
						referenceDate:moment($scope.referenceDate).format('DD-MM-YYYY HH:mm:ss'),
						firstName:$scope.request.firstName,
						fatherName:$scope.request.fatherName,
						hospitalName:$scope.request.hospitalName,
						investigation:$scope.request.investigation,
						doneOnDate:moment($scope.request.doneOnDate).format('DD-MM-YYYY HH:mm:ss'),
						consultant:$scope.request.consultant,
						orgName:$scope.request.orgName,
						disease:$scope.request.disease,
						hospitalTreatment:$scope.request.hospitalTreatment,
						visitfromDate:moment($scope.request.visitfromDate).format('DD-MM-YYYY HH:mm:ss'),
						visittoDate :moment($scope.request.visittoDate).format('DD-MM-YYYY HH:mm:ss'),
						treatfromDate : moment($scope.request.treatfromDate).format('DD-MM-YYYY HH:mm:ss'),
						treattoDate : moment($scope.request.treattoDate).format('DD-MM-YYYY HH:mm:ss'),
						consDate : moment($scope.request.consDate).format('DD-MM-YYYY HH:mm:ss'),
						employeeName : $scope.request.employeeName,
						relation : $scope.request.relation,
						consAmount: parseInt($scope.request.consAmount),
						labAmount:parseInt($scope.request.labAmount),
						pharmacyAmount:parseInt($scope.request.pharmacyAmount),
						address1:$scope.request.address1,
						address2:$scope.request.address2,
						medicalCardNo:$scope.request.medicalCardNo,
						authorizationLetterNo:$scope.request.authorizationLetterNo,
						letterType:$scope.letterType,
						status : 'A',
						unitId:$scope.unitId,
						organizationId : $scope.organizationId,
						createdDate : $scope.currentDate,
						updatedDate : $scope.currentDate,
						createdBy : $scope.userId,
						updatedBy : $scope.userId,
					}
			
					console.log("data to save",data);
					if($scope.corporateStatementId == undefined){
						var URI = BASE_URL + ROOT_URL
						+ "/unit/CompanyStatment/saveCorporateStatementMasterDetails";
					}else{
						data.corporateStatementId = $scope.corporateStatementId;
						var URI = BASE_URL + ROOT_URL
						+ "/unit/CompanyStatment/updateCorporateStatementMasterDetails";
					}
				
						GenericService.serviceAction("POST", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												
												$scope.corporateStatementId = response.data.object;
												
									
												growl.success(response.data.message,
																{
																	title : 'Success!'
																});
												 
												 if($scope.selectedcheckListId.length > 0){
													 //save checklist against bill
													 
													 angular.forEach($scope.selectedcheckListId,function(value,key){
														 value.corporateStatementId = $scope.corporateStatementId;
													 })
													var datata={
														status : 'A',
														unitId:$scope.unitId,
														organizationId : $scope.organizationId,
														createdDate : $scope.currentDate,
														updatedDate : $scope.currentDate,
														createdBy : $scope.userId,
														updatedBy : $scope.userId,
														listOfDetailsChecklistDto:$scope.selectedcheckListId,
													}
													 var URI2 = BASE_URL + ROOT_URL
														+ "/unit/saveCorporateStatementDetailsChecklist";
														GenericService.serviceAction("POST", URI2, datata)
																.then(
																		function(response) {
																			if (response.data.status == "success") {
																				if($scope.letterType == 1){
																					var rptName = "CoveringLetter.rpt";
																				}else if($scope.letterType == 2){
																					var rptName = "Essentiality_certificate.rpt";
																				}else if($scope.letterType == 3){
																					var rptName = "Emergency_certificate.rpt";
																				}else if($scope.letterType == 4){
																					var rptName = "CertificateC.rpt";
																				}else if($scope.letterType == 5){
																					var rptName = "CertificateB.rpt";
																				}
																					  var data =  {
																					                 "appName": "HIS",
																					                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																					                 "reportDesignFilePath": rptName ,
																					                 "reportFormat": "PDF",
																					                 "exportFileName": "Coversheet",
																					                 "reportParams": [
																					                	 {
																							                  "paramName": "billing_master_id",
																							                  "paramType": "Integer",
																							                  "paramValue": $scope.billId
																						                },
																						               
																						               
																			                 ]
																			      };
																					  
																					  if($scope.letterType != undefined){
																						  reportService.generate(REPORT_API_URI1,data);
																					  }
																					    
																					   $scope.coveringLetter = ""
																					   $scope.essentialityCertificate = ""
																					   $scope.emergencyCertificate = ""
																					   $scope.certificateA = ""
																					   $scope.certificateB = ""
																						
																			}
																		});
												 }
												 
											} else {
												growl.error(response.data.message,
																{
																	title : 'Error!'
																});
											}
										});
				}
			 
			 
			 
			  
				$scope.printOldEncounter=function(info){
					  var data =  {
					                 "appName": "HIS",
					                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					                 "reportDesignFilePath": "NewCoverSheet.rpt",
					                 "reportFormat": "PDF",
					                 "exportFileName": "Coversheet",
					                 "reportParams": [
					                	 {
							                  "paramName": "Patientid",
							                  "paramType": "Integer",
							                  "paramValue":  $scope.patientId
						                },
						                {
							                  "paramName": "Unitid",
							                  "paramType": "Integer",
							                  "paramValue": $scope.unitId
						                },
						                {
								             "paramName": "Encounter_id",
								             "paramType": "Integer",
								             "paramValue": info.encounterId
									      }
			                 ]
			      };
					    reportService.generate(REPORT_API_URI1,data);
				};
			   
				
				$scope.printPastAdmissionReport = function(info) {

					var data = {
						"appName" : "HIS",
						"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
						"reportDesignFilePath" : "NewCoverSheetIPD.rpt",
						"reportFormat" : "PDF",
						"exportFileName" : "CompleteConsultation",
						"reportParams" : [
								{
									"paramName" : "Patientid",
									"paramType" : "Integer",
									"paramValue" : $scope.patientId
								},
								{
									"paramName" : "Unitid",
									"paramType" : "Integer",
									"paramValue" : $scope.unitId
								},
								{
									"paramName" : "admission_id",
									"paramType" : "Integer",
									"paramValue" :info.admissionId
								}

						]
					};

					reportService.generate(REPORT_API_URI1, data);
				}
				
				
				$scope.rePrintInvestigation=function(labObj,idx,printFlag)
				{
					if (labConfig == undefined) {
						return;
					}
					if(labObj.sampleStatusId==8)
					{
					if(labConfig.biochemistryId==labObj.subDeptId||labConfig.clinicalPathologyId==labObj.subDeptId||labConfig.hematologyId==labObj.subDeptId)
					{
					var data =  
					{
				            "appName": "HIS",
				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				            "reportDesignFilePath": "pathologyReport.rpt",
				            "reportFormat": "PDF",
				            "exportFileName": "pathologyReport",
				            "reportParams": [
				              {
					             "paramName": "labtesresid",
					             "paramType": "Integer",
					             "paramValue": labObj.labTestResId
				              },{
						             "paramName": "isOriginal",
						             "paramType": "Integer",
						             "paramValue": printFlag
					           }
				            ]
				      };
					  reportService.generate(REPORT_API_URI1,data);
					}
				else if(labConfig.microbiologyId==labObj.subDeptId)
					{
					var data =  
					{
				            "appName": "HIS",
				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				            "reportDesignFilePath": "pathologyReport_Sensitivity.rpt",
				            "reportFormat": "PDF",
				            "exportFileName": "pathologyReport",
				            "reportParams": [
				              {
					             "paramName": "lab_sample_dtls_id",
					             "paramType": "Integer",
					             "paramValue": labObj.labSampleDtlsId
				              },{
						             "paramName": "isOriginal",
						             "paramType": "Integer",
						             "paramValue": printFlag
					           }
				            ]
				      };
					   reportService.generate(REPORT_API_URI1,data);
					}
				else if(labConfig.histopathologyId==labObj.subDeptId||labConfig.cystopathologyId==labObj.subDeptId)
				{
				 var data =  
				   {
			            "appName": "HIS",
			            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			            "reportDesignFilePath": "GetHistopathologyReport.rpt",
			            "reportFormat": "PDF",
			            "exportFileName": "pathologyReport",
			            "reportParams": [
			              {
				             "paramName": "labtesresid",
				             "paramType": "Integer",
				             "paramValue": labObj.labSampleDtlsId
			              },{
					             "paramName": "isOriginal",
					             "paramType": "Integer",
					             "paramValue": printFlag
				           }
			            ]
			       };
				      reportService.generate(REPORT_API_URI1,data);
				  }else if(labConfig.serologyId==labObj.subDeptId){
						var data =  
						{
					            "appName": "HIS",
					            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					            "reportDesignFilePath": "SerologyReport.rpt",
					            "reportFormat": "PDF",
					            "exportFileName": "pathologyReport",
					            "reportParams": [
					              {
						             "paramName": "lab_sample_dtls_id",
						             "paramType": "Integer",
						             "paramValue": labObj.labSampleDtlsId
					              },
					              {
							             "paramName": "isOriginal",
							             "paramType": "Integer",
							             "paramValue": printFlag
						           }
					            ]
					      };
						reportService.generate(REPORT_API_URI1,data);
				}else{
					  var data =  
						{
					            "appName": "HIS",
					            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					            "reportDesignFilePath": "pathologyReport.rpt",
					            "reportFormat": "PDF",
					            "exportFileName": "pathologyReport",
					            "reportParams": [
					              {
						             "paramName": "labtesresid",
						             "paramType": "Integer",
						             "paramValue": labObj.labTestResId
					              },{
							             "paramName": "isOriginal",
							             "paramType": "Integer",
							             "paramValue": printFlag
						           }
					            ]
					      };
						  reportService.generate(REPORT_API_URI1,data);
				  }	
				}
					
					else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
						{
		    			var data =  {
		    		            "appName": "HIS",
		    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		    		            "reportDesignFilePath": "RadiologyReport.rpt",
		    		            "reportFormat": "PDF",
		    		            "exportFileName": "RadiologyReport",
		    		            "reportParams": [
		    		              {
		    			             "paramName": "template_transaction_id",
		    			             "paramType": "Integer",
		    			             "paramValue": labObj.templateTransactionId
		    		              },{
						        		"paramName": "isOriginal",
						        		"paramType": "Integer",
						        		"paramValue": 1
						        		},{
						        		"paramName": "isHeader",
						        		"paramType": "Integer",
						        		"paramValue": 0
						        		}
		    		            ]
		    		    };
		    	           reportService.generate(REPORT_API_URI1,data);
						}
				 else
				 {
				  growl.warning("Final Report not released!!",
						{
							title : 'Warning!'
						});
				  }
			 }
				
				
				  $scope.printAllReports = function (){
				    	if($scope.selectedReportlistOP.length > 0){
				    		angular.forEach($scope.selectedReportlistOP,function(value,key){
				    			angular.forEach($scope.opVisitList,function(value1,index1){
				    				if(value.index == index1){
				    					$scope.printOldEncounter(value1);
				    				}
				    			});
				    		});
				    	}
				    	if($scope.selectedReportlistIP.length > 0){
				    		angular.forEach($scope.selectedReportlistIP,function(value,key){
				    			angular.forEach($scope.ipVisitList,function(value1,index1){
				    				if(value.index == index1){
				    					$scope.printPastAdmissionReport(value1);
				    				}
				    			});
				    		});
				    	}
				    	if($scope.selectedReportlistInvest.length > 0){
				    		angular.forEach($scope.selectedReportlistInvest,function(value,index){
				    			angular.forEach($scope.currentEncounterOrderLists1,function(value1,index1){
				    				if(value.index == index1){
				    					$scope.rePrintInvestigation(value1,index,1);
				    				}
				    			});
				    			
				    		});
				    	}
				    	
				    }
				  
				  $scope.selectedReportlistOPfun = function(info,idx){
						var flag = false;
						angular.forEach($scope.selectedReportlistOP,function(value,index){
						 if(index == idx){
							 
							 $scope.selectedReportlistOP.splice(index,1);
							 $scope.checkOpVisit[index] = false;
							 flag = true;
						 }
						})
						if(!flag){
							var temp = {
									     index:idx
									};
							$scope.selectedReportlistOP.push(temp);
							}
						
						 console.log("$scope.selectedReportlistOP",$scope.selectedReportlistOP);
						}
				  
				  $scope.selectedReportlistIPfun = function(info,idx){
						var flag = false;
						angular.forEach($scope.selectedReportlistIP,function(value,index){
						 if(index == idx){
							 
							 $scope.selectedReportlistIP.splice(index,1);
							 $scope.checkIpVisit[index] = false;
							 flag = true;
						 }
						})
						if(!flag){
							var temp = {
									     index:idx
									};
							$scope.selectedReportlistIP.push(temp);
							}
						
						 console.log("$scope.selectedReportlistIP",$scope.selectedReportlistIP);
						}
				  
				  $scope.selectedReportlistInvestfun = function(info,idx){
						var flag = false;
						angular.forEach($scope.selectedReportlistInvest,function(value,index){
						 if(index == idx){
							 
							 $scope.selectedReportlistInvest.splice(index,1);
							 $scope.checkIpVisit[index] = false;
							 flag = true;
						 }
						})
						if(!flag){
							var temp = {
									     index:idx
									};
							$scope.selectedReportlistInvest.push(temp);
							}
						
						 console.log("$scope.selectedReportlistIP",$scope.selectedReportlistIP);
						}
				  
			 $scope.selectchecklist = function(info){
					var flag = false;
					angular.forEach($scope.selectedcheckListId,function(value,index){
					 if(value.corporateStatementChecklistId == info.corporateStatementChecklistId){
						 
						 $scope.selectedcheckListId.splice(index,1);
						 $scope.checkListObjId[value.corporateStatementChecklistId] = false;
						 flag = true;
					 }
					})
					if(!flag){
						var temp = {
								     corporateStatementChecklistId:info.corporateStatementChecklistId
								};
						$scope.selectedcheckListId.push(temp);
						}
					
					 console.log("$scope.selectedcheckListId",$scope.selectedcheckListId);
					}
			 
			 $scope.NavigateToDetail = function (item) {
				    angular.element('#patientReports').modal('hide');
					angular.element('#exampleModal').modal('hide');
		            $state.go('corporateStatement');
//		            $state.reload('corporateStatement');
		        }
			 
			 $scope.creditBillingReportCall = function () {
				var data =  
				{
			            "appName": "HIS",
			            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			            "reportDesignFilePath": "OPCorporateBill.rpt",
			            "reportFormat": "PDF",
			            "exportFileName": "pathologyReport",
			            "reportParams": [
			              {
				             "paramName": "patient_id",
				             "paramType": "Integer",
				             "paramValue": $scope.patientId
			              }
			            ]
			      };
				   reportService.generate(REPORT_API_URI1,data);
			 }
	}catch(e){
		console.log(e.message);
	}
}
function config($stateProvider) {
	$stateProvider.state('detailsCorporateStatement', {
		url: '/detailsCorporateStatement',
		templateUrl: 'views/master/UnitMaster/billingMaster/corporate-statement-details.html',
		controller: "detailsCorporateStatementController",
		title: 'Corporate Statement',
	})
}

angular
.module("myApp")
.config(config)
.controller("detailsCorporateStatementController", detailsCorporateStatementController);
})();	