'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:discountRequestApprovalController
 * @description #discountRequestApprovalController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'discountRequestApprovalController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceParamHeader',
						'GenericServiceFileUpload',
						'growl',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl) {

							/*alert("encounterQueueManagementController");*/

							var date = new Date();
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : date.setDate((new Date()).getDate() - 90),
									//minDate : new Date()
								};
							
							$scope.dateOptionsTo = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							
							$scope.openDatePicker = function() { 
								$scope.datepicker.opened = true;
							};
							$scope.openDatePickerTo = function() {
								$scope.datepickerTo.opened = true;
							};
							
							$scope.datepicker = {
									opened : false
								};
								$scope.datepickerTo = {
										opened : false
									};
								$scope.financial = {fromDate:new Date(),toDate:new Date(),visitTypeId:"",patientId:"",organizationId:$scope.organizationId,unitId:$scope.unitId,requestBy:"",requestSentTo:$scope.userId, requestType:16};
							
							/* init() function for form object create. */
							$scope.init = function() {


								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								$scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
								$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
								$scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};

								//For Get Active Reason List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});
								
								     
								     $scope.requestSearchObject = {
								    		 'doctorId':0,
								    		 'specialityId':0,
								    		 'patientId':0,
								    		 'specialityId':0,
								    		 'statusId':0,
								    		 'clinicId':0,
								    		 'nursingStationId':0,
								    		 'requestType' : 16
								     }
								     	
										
										
							}

							$scope.init();
							
							$scope.clearFields  = function(){
								$scope.patientName = "";
								$scope.financial = {fromDate:new Date(),toDate:new Date(),visitTypeId:"",patientId:"",organizationId:$scope.organizationId,unitId:$scope.unitId,requestBy:"",requestType:16};
							}
							$scope.searchFinancialList = function(){
								console.log("dd",$scope.financial);
								$scope.financial = {fromDate:$scope.financial.fromDate,toDate:$scope.financial.toDate,visitTypeId:"",patientId:"",organizationId:$scope.organizationId,unitId:$scope.unitId,requestBy:"",requestSentTo:$scope.userId, requestType:16};
								var  financialObj = angular.copy($scope.financial);
							/*	return false;
								var requestDiscObj={
										'organizationId': $scope.organizationId,
										'unitId':$scope.unitId,
										'requestSentTo':$scope.userId
								};*/
								
								
							   // return false;
								
								if($scope.financial.fromDate != '' && $scope.financial.toDate != '' && typeof($scope.financial.fromDate) != "undefined" && typeof($scope.financial.toDate) != "undefined"){
									financialObj.fromDate = $rootScope.getFormattedDate($scope.financial.fromDate,false);
									financialObj.toDate = $rootScope.getFormattedDate($scope.financial.toDate,false);
								}
								
								
							    var URI = BASE_URL + ROOT_URL
								+ "/api/billing/requestedDiscount/user";
						
								GenericService
								.serviceAction("POST", URI, financialObj)
								.then(
										function(response) {
											$scope.requestDiscUsersList = [];

											if (response.data.status == "success") {
												$scope.requestDiscUsersList = response.data.listObject;

											}
										});
							}
							$scope.searchFinancialList();
							$scope.usersList = [];
							$scope.getEmployeeList = function(){
								
								try{
									
									var data = {
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									};
									var URI = BASE_URL + ROOT_URL
									+ GETUSERSLIST;
							GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.usersList = [];
												if (response.data.status == "success") {
													$scope.usersList = response.data.listObject;
													console.log("$scope.usersList",$scope.usersList);
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							$scope.getEmployeeList();
							
							$scope.firstTabLevel = 1 ;
							
							$scope.setFirstTabLevel= function(newTab)
							{
								$scope.searchOrderFilter = '';
								$scope.firstTabLevel = newTab;
							};
							
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							
							
							$scope.displayDetails = function(item,idx)
							{
								//$scope.displayDetailsList = item.requestDiscountDetailsDtosList;
								$scope.displayDetailsList = [];
								var totalDisPercentage =(parseFloat(item.approvedDiscAmt)/parseFloat(item.netAmount)*100).toFixed(2);
					    		//alert(totalDisPercentage);
								
								var i = 0;
						    	angular.forEach(item.requestDiscountDetailsDtosList, function (value, key) {
						    		var newArr = [];
						    		newArr.orderDetailsId = value.orderDetailsId;
						    		newArr.orderId = value.orderId;
						    		newArr.reqDiscDetailsId = value.reqDiscDetailsId;
						    		newArr.serviceId = value.serviceId;
						    		newArr.reqDisAmt = value.reqDisAmt;
						    		newArr.netAmount = value.netAmount;
						    		newArr.serviceName = value.serviceName;
						    		
						    		//var calDicAmt = ((value.netAmount)*totalDisPercentage/100).toFixed(2);	
						    		/*newArr.approvedDiscAmt = reqDisAm;
						    		newArr.finalAmount = parseFloat(value.netAmount) - parseFloat(calDicAmt);*/
						    		
						    		 newArr.approvedDiscAmt = value.reqDisAm;
	                                 newArr.finalAmount = parseFloat(value.netAmount) - parseFloat(value.netAmount)

						    		
						    		$scope.displayDetailsList.push(newArr);
						    		
						    		i++;
						    	});
						    	$scope.requestDiscUsersList[idx].requestDiscountDetailsDtosList = $scope.displayDetailsList;
							}
							
							$scope.checkAmt = function(info,idx)
							{
								if(info.approvedDiscAmt > info.reqDisAmt )
									{
										growl.warning("Approved Amount should be less then request amount!!",{
											title : ERROR_MSG
										});
										$scope.requestDiscUsersList[idx].approvedDiscAmt = info.reqDisAmt; 
							    		return false;
									}
								else if(info.approvedDiscAmt < info.reqDisAmt )
									{
									var totalDisPercentage =parseFloat(info.approvedDiscAmt)/parseFloat(info.netAmount)*100;
										angular.forEach(info.requestDiscountDetailsDtosList, function (value, key) {
											var calDicAmt = ((value.netAmount)*totalDisPercentage/100).toFixed(2);	
								    		value.approvedDiscAmt = calDicAmt;
								    		value.reqDisAmt = calDicAmt;
										});
									}
								
							}
							
							$scope.acceptReq = function(info)
							{
								console.log(info);
								
								$rootScope.startLoader();
								var data = {
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										reqDiscId :info.reqDiscId,
										//approvedDiscAmt: info.approvedDiscAmt,
										remark: info.remark,
										approvedDiscAmt: info.reqDisAmt
								}
								var discServList = [];
								//$scope.displayDetails(info,idx);
								var totalDisPercentage =(parseFloat(info.approvedDiscAmt)/parseFloat(info.netAmount)*100).toFixed(2);
								angular.forEach(info.requestDiscountDetailsDtosList, function (value, key) {
									var newArr = {};
									
									//var calDicAmt = ((value.netAmount)*totalDisPercentage/100).toFixed(2);	
						    		//newArr.approvedDiscAmt = calDicAmt;
									newArr.approvedDiscAmt = value.reqDisAmt
									newArr.reqDiscDetailsId=value.reqDiscDetailsId,
									//newArr.approvedDiscAmt=value.approvedDiscAmt,
									newArr.updatedBy = $scope.userId,
									newArr.updatedDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
									discServList.push(newArr);
								});
								data.requestDiscountDetailsDtosList = discServList;
								
								  var URI = BASE_URL + ROOT_URL + "/api/billing/approveRequest";
							
									GenericService
									.serviceAction("POST", URI, data)
									.then(function(response) {
												$rootScope.stopLoader();
												if (response.data.status == "success") {
													growl.success("Approved !!!!",{
														title : 'success'
													});
													$scope.init();
													$scope.searchFinancialList();
												}
											});
							
							}
							
							$scope.acceptReqForFinancial = function(item)
							{
								//alert("hhh");
								//return false;
								console.log(item);
								
								$rootScope.startLoader();
								var data = {
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										reqDiscId :item.reqDiscId,
										billId :item.billId,
										admissionId: item.admissionId,
										encounterId: item.encounterId,
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/approveRequestForFinancialClearance";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Approved !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
											}
										});
								
								
							}
							
							$scope.rejetReq = function(item)
							{
								//alert("F");
								console.log(item);
								
								$rootScope.startLoader();
								var data = {
										reqDiscId :item.reqDiscId,
										billId :item.billId,
										remark : item.remark
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Cancelled !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
											}
										});
								
							}
							 $scope.forceCancelPackage=function(list){
								 angular.forEach(list,function(data){
						        		var data={
						        				packageConsumptionMasterId:data.packageConsumptionMasterId,
						        				orderDetailId:data.orderDetailId,
						        				toDate : moment().format('YYYY-MM-DD HH:mm:ss'),
						        				unitId : $scope.unitId
						        		}
					        		$rootScope.startLoader();
					        		var URI = BASE_URL + ROOT_URL + "/api/packages/forceCancelPackage";
					        		BillingGenericService.serviceAction("POST", URI, data).then(function(response){
					        			if(response.data.status == "success")
					        				{
					        					growl.success("Package closed",{
					        						title : WARNING_MSG
					        					});
					        					$rootScope.stopLoader();
					        				}else{
					        					$rootScope.stopLoader();
					        				}
					        				
					        		});
								 });
					        	}
							 
							$scope.checkPackageConsumedOrNot =function(item)
							{ 
								
								var id =null;
								if(item.visitTypeId == 1){
									id = item.encounterId;
								}else{
									id =item.admissionId;
								}
								if(item.visitTypeId != null && id != null){
                                    if(item.patientId === undefined || item.patientId === null || item.patientId ===''){
                                        item.patientId = 0;
                                    }
								var URI = BASE_URL+ ROOT_URL
		                        + "/api/packages/getPackageListForIPDPackageOrder/"+item.patientId+"/"+ id
		                        + "/" + $scope.organizationId + "/"+ $scope.unitId+"/"+item.visitTypeId;
		                    $rootScope.startLoader();
		                GenericService.serviceAction("GET", URI).then(function(response) {
		                                    $rootScope.stopLoader();
		                                    $scope.listPackageOrder = [];
		                                    $scope.listPackageOrder = response.data.listObject !=null ? response.data.listObject : []  ;
		                                    if($scope.listPackageOrder.length > 0){
		                                        $scope.creditNoteDetailsList = [];
		        								$rootScope.startLoader();
		        								var data = {
		        										creditNoteId: item.creditNoteId,
		        								}
		        								
		        								var URI = BASE_URL + ROOT_URL + "/api/billing/getCreditNoteDetailsByCreditNoteId";
		        								
		        								GenericService
		        								.serviceAction("POST", URI, data)
		        								.then(function(response) {
		        											$rootScope.stopLoader();
		        											if (response.data.status == "success") {
		        												 $scope.creditNoteDetailsList = response.data.listObject !=null ? response.data.listObject : [];
		        												 $scope.sortedListOrderDetailsIds = [];
		        												 if($scope.listPackageOrder.length > 0){
		        													 angular.forEach($scope.listPackageOrder,function(pacakage){
		        														 if(pacakage.packageTypeId == 5){
		        															 angular.forEach($scope.creditNoteDetailsList,function(creditNote){
		        																 if(pacakage.orderDetailId == creditNote.orderDetailsId){
		        																	 $scope.sortedListOrderDetailsIds.push(pacakage);
		        																 }
		        																 
		        															 });	 
		        														 }
		        														
		        													 });
		        													 if($scope.listPackageOrder.length > 0){
		        													 $scope.forceCancelPackage($scope.sortedListOrderDetailsIds);
		        													 }
		        												 }
		        											}
		        										});
		                                    }
		                                
		                                    $rootScope.stopLoader();
		                                });
							}
		                
								
								
							}
							
							$scope.acceptReqForCn =function(item)
							{ 
								$rootScope.startLoader();
								var data = {
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										reqDiscId :item.reqDiscId,
										creditNoteId: item.creditNoteId,
										tRefundCnId : item.tRefundCnId,
										creditNoteRemark : item.creditNoteRemark,
										refundRemark : item.refundRemark,
										selfPayeeId : $scope.selfPayeeId 
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/approveCnRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Approved !!!!",{
													title : 'success'
												});
												
												$scope.checkPackageConsumedOrNot(item);
												$scope.init();
												$scope.searchFinancialList();
											}
										});
								
							}


							$scope.cancelCreditNote = function(creditNoteId){
							   if(creditNoteId!=null && creditNoteId>0){
							     var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote/cancel/"+creditNoteId;
                                     BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                                     if (response.data.status == "success")
                                        {}
                                     else if(response.data.status == "error")
                                    	{}
                                   });
							   }
                            }

							$scope.rejetReqCn =function(item)
							{
								$rootScope.startLoader();
								var data = {
										reqDiscId :item.reqDiscId,
										creditNoteId :item.creditNoteId,
										tRefundCnId : item.tRefundCnId,
										depositId : item.depositId,
										creditNoteRemark : item.creditNoteRemark,
										refundAmount : item.refundAmount
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Rejected !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
												//$scope.cancelCreditNote(item.creditNoteId);
											}
										});
							}

							
							$scope.rejetReqDepositRefund =function(item)
							{
								$rootScope.startLoader();
								var data = {
										reqDiscId :item.reqDiscId,
										expenseId :item.expenseId,
										expenseRemark: item.expenseRemark,
										companyReciptRemark: item.companyReciptRemark,
										companyReceiptsExpenseId: item.companyReceiptsExpenseId,
										
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Cancelled !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
											}
										});
							}
							
							
							$scope.rejetReqCompanyReceipts =function(item)
							{
								$rootScope.startLoader();
								var data = {
										reqDiscId :item.reqDiscId,
										companyReceiptsExpenseId :item.companyReceiptsExpenseId
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Cancelled !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
											}
										});
							}
							
							$scope.acceptRequestCompanyReceipts =function(item)
							{ 
								$rootScope.startLoader();
								var data = {
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										reqDiscId :item.reqDiscId,
										companyReceiptsExpenseId: item.companyReceiptsExpenseId,
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/approveCompanyReceiptsExpenseRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Approved !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
											}
										});
								
							}
							
							
							
							$scope.acceptRequestRefund =function(item)
							{ 
								$rootScope.startLoader();
								var data = {
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										reqDiscId :item.reqDiscId,
										expenseId: item.expenseId,
										expenseRemark: item.expenseRemark,
										expenseId: item.expenseId,
										companyReciptRemark: item.companyReciptRemark,
										companyReceiptsExpenseId: item.companyReceiptsExpenseId,
										
										
								}
								var data1={		
										 reqDiscId : item.reqDiscId,
										 fromPatientId : item.fromPatientId,
										 toPatientId   : item.toPatientId,
										 requestSentBy 	 : item.requestSentBy,
										 requestSentTo	 : item.requestSentTo,
							         	}	
//								console.log("data1",data1);
//								return false;
								
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/approveExpenseRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Approved !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
											}
										});
								
								
								
                                 var URI1 = BASE_URL + ROOT_URL + "/api/billing/getUHIDMergeReuestAcceptData";
								
								GenericService
								.serviceAction("POST", URI1, data1)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Approved !!!!",{
													title : 'success'
												});
												$scope.init();
												$scope.searchFinancialList();
											}
										});
								
							}
							
							
							
							$scope.nursingStationChange = function()
							{
								$scope.encounterSearchObject.doctorId = '0';
								$scope.encounterSearchObject.clinicId = '0';
							}
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function()
							{
								$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.organizationId = $scope.organizationId;
                                $scope.encounterSearchObject.unitId = $scope.unitId;
								
								console.log($scope.encounterSearchObject);
								
								var paramObj = {};
								var headers={
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETSEARCHEDLISTENCOUNTERMASTER;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success")
												
												$scope.allEncounterList =response.data.listObject;
										});
						
							}
							
							
							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								console.log(data);
								//$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
													//$rootScope.stopLoader();
												});

							}
							
							$scope.selectPatientId = function(info, model,
									label) {
								
								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;
								$scope.financial.patientId = info.patientId;
							}
						
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							$scope.getAppointmentDetails = function(item)
							{
								$scope.appointmentDate = item.appointmentDate;
								$scope.appointmentFromTimeString = item.fromTimeString;
								$scope.appointmentRemark = item.remark;
							}
							
						} ]);