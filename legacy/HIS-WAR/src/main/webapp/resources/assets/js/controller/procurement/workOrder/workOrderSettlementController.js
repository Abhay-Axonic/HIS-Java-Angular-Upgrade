

angular
		.module('myApp')
		.controller(
				"workOrderSettlementController",
				[	    	'$scope',
							'$http',
							'$localStorage',
							'$sessionStorage',
							'$cookies',
							'$rootScope',
							'GenericService',
							'PagerService',
							'promiseFactory',
							'BillingGenericService',
							'GenericServiceFileUpload',
							'reportService',
							'growl',
							'$state',
							'GenericServiceParamHeader',
							'$stateParams',
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,GenericServiceParamHeader,
								$stateParams,$modalInstance) {

							$rootScope.loginpage = true;
						    $scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							//$scope.setPaymentModeId(1);
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							

							$scope.visitTypeList = [ {
								id : 1,
								label : "OP"
							}, {
								id : 2,
								label : "IP"
							}, {
								id : 3,
								label : "Day Care"
							}, {
								id : 4,
								label : "ER"
							} ];
							$scope.selectedVisitType = [];
							$scope.patientList =[];
							$scope.patientDetails = {};
							 
							/** * Check user is Loggedin */
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.storeName = cookieObject.storeName;
							$scope.storeId = cookieObject.storeId;
							console.info($scope.orgId)
								var data = {};
							
							
							 $scope.searchNewWOItemList= [];
								$scope.getListNewWOList = function () {
									 $scope.searchNewWOItemList= [];
										var data = {};
										data.orgId= $scope.orgId;
										data.unitId= $scope.unitId;
										data.storeId= $scope.woSettlementSerach.storeId;
										
										if($scope.woSettlementSerach.toDate!=undefined && $scope.woSettlementSerach.toDate!="" )
										{
											data.toDate=moment( $scope.woSettlementSerach.toDate ).format('YYYY-MM-DD');
										}
										
										if($scope.woSettlementSerach.fromDate!=undefined && $scope.woSettlementSerach.fromDate!="" )
										{
											data.fromDate=moment( $scope.woSettlementSerach.fromDate ).format('YYYY-MM-DD');
										}
										/*if($scope.woSettlementSerach.workorderRequestNumber!=undefined && $scope.woSettlementSerach.workorderRequestNumber!="" )
										{
											data.workorderRequestNumber=$scope.woSettlementSerach.workorderRequestNumber ;
										}*/
										
										if($scope.woSettlementSerach.billTypeId!=undefined && $scope.woSettlementSerach.billTypeId!='' )
										{
											data.billTypeId=$scope.woSettlementSerach.billTypeId ;
										}
										
										if($scope.woSettlementSerach.vendorId!=undefined && $scope.woSettlementSerach.vendorId!='' && $scope.woSettlementSerach.vendorId!=0 )
										{
											data.vendorId=$scope.woSettlementSerach.vendorId ;
										}
										if($scope.woSettlementSerach.paymentStatusId!=undefined && $scope.woSettlementSerach.paymentStatusId!='' && $scope.woSettlementSerach.paymentStatusId!=0 )
										{
											data.paymentStatusId=$scope.woSettlementSerach.paymentStatusId ;
										}
										if($scope.woSettlementSerach.vendorName==undefined || $scope.woSettlementSerach.vendorName==null || $scope.woSettlementSerach.vendorName=="")
										{
											data.vendorId=null ;
										}
										if($scope.woSettlementSerach.storeId==undefined || $scope.woSettlementSerach.storeId==''){
									    	  growl.warning("Please select store",
														{
															title : 'warning!'
														});
												return;
									      }
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getRequestApprovalList";
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											if(response.data.status == "success"){
												$rootScope.stopLoader();
												if(response.data.listObject!=null && response.data.listObject.length >0)
											        $scope.searchNewWOItemList = response.data.listObject;
												if($scope.searchNewWOItemList.length > 0){
													angular.forEach($scope.searchNewWOItemList,function(value, index){
														value.selectedToPendingstatus=false;
														value.isReject=false;
													});
												}
												// else{
												// 	 growl.warning("No Record found..",
												// 				{
												// 					title : 'warning!'
												// 				});
												// }
											}
										});
								}
							
								
								
								$scope.searchSettledList=function(){
									
									var data={};
									data.orgId= $scope.orgId;
									data.unitId= $scope.unitId;
									data.storeId= $scope.SettledWorkOrderSearch.storeId;
									if($scope.SettledWorkOrderSearch.toDate!=undefined && $scope.SettledWorkOrderSearch.toDate!="" )
									{
										data.toDate=moment( $scope.SettledWorkOrderSearch.toDate ).format('YYYY-MM-DD');
									}
									if($scope.SettledWorkOrderSearch.fromDate!=undefined && $scope.SettledWorkOrderSearch.fromDate!="" )
									{
										data.fromDate=moment( $scope.SettledWorkOrderSearch.fromDate ).format('YYYY-MM-DD');
									}
									if($scope.SettledWorkOrderSearch.vendorName==undefined || $scope.SettledWorkOrderSearch.vendorName==null || $scope.SettledWorkOrderSearch.vendorName==""){
										$scope.SettledWorkOrderSearch.vendorId='';
									}
									if($scope.SettledWorkOrderSearch.vendorId!=undefined && $scope.SettledWorkOrderSearch.vendorId!='' && $scope.SettledWorkOrderSearch.vendorId!=0 )
									{
										data.vendorId=$scope.SettledWorkOrderSearch.vendorId ;
									}
									
									if($scope.SettledWorkOrderSearch.workorderSettlementNumber!=undefined && $scope.SettledWorkOrderSearch.workorderSettlementNumber!="" )
									{
										data.workorderSettlementNumber=$scope.SettledWorkOrderSearch.workorderSettlementNumber ;
									}
									
									
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getRequestSettledList";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											if(response.data.listObject!=null && response.data.listObject.length >0)
											{    $scope.requestSettledList=[];
												$scope.requestSettledList = response.data.listObject;
											}
											// else{
											// 	 growl.warning("No Record found..",
											// 				{
											// 					title : 'warning!'
											// 				});
											// }
										}else{
											$rootScope.stopLoader();
										}
										
									});
									
									
								}
							//$(".selectedPageName").text("Machine Master");
							//Date For From Date on Search
							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.selectedtrustFundTypeId="";
								$scope.woSettlementSerach = {
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"toDate" :new Date(),
									"fromDate" :new Date(),
									"storeId" : $scope.storeId,
									"vendorId" : '',
									"vendorName" : '',
									"contractTypeId":'',
									"billTypeId":'',
									"paymentStatusId":''
									
								}
								
								$scope.SettledWorkOrderSearch = {
										"orgId" : $scope.orgId,
										"unitId" : $scope.unitId,
										"toDate" :new Date(),
										"fromDate" :new Date(),
										"storeId" : $scope.storeId,
										"vendorId" : '',
										"vendorName" : '',
										"workorderSettlementNumber":'',
										"contractTypeId":'',
										"billTypeId":'',
										"paymentStatusId":''
										
									}
								//for settlement wo use
								$scope.trustlistObj={};
								$scope.trustlistObj.trustId=1;
								
								$scope.getListNewWOList () ;
								$scope.searchSettledList();
							};
							$scope.initCommon();
							
							
							$scope.openDatePickerFromDate = function() {
								$scope.datepickerFromDate.opened = true;
							};

							$scope.datepickerFromDate = {
								opened : false
							};
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date(),
									minDate : moment(new Date()).subtract(180, 'days').toDate()
								};
							$scope.dateOptions1 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
									minDate : moment(new Date()).subtract(180, 'days').toDate()
								};
							
							$scope.dateOptions3 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : moment(new Date()).subtract(180, 'days').toDate()
								
								};
							
							
							$scope.dateOptions4 = {
									formatYear : 'yyyy',
									showWeeks : false,
									
									
								};
							
							
							$scope.dateOptions5 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : moment(new Date()).subtract(180, 'days').toDate()
								
								};
							
							
							$scope.dateOptions6 = {
									formatYear : 'yyyy',
									showWeeks : false,
									
									
								};
							
							$scope.openDatePicker = function() { 
								$scope.datepicker.opened = true;
							};
							$scope.openDatePickerSearch = function() {
								$scope.datepicker2.opened = true;
							};
							
							$scope.datepicker2 = {
									opened : false
								};
							
							
							$scope.openDatePickerSearch5 = function() {
								$scope.openDatePickerSearch5.opened = true;
							}
							
							$scope.openDatePickerSearch6 = function() {
								$scope.openDatePickerSearch6.opened = true;
							}
							
							//Date For To Date on Search
							var visitList=[];
							angular.forEach($scope.paymentModeId,
									function(value,index) {
								     visitList.push(value.id);
									});
							
							$scope.checkedcobjectlist=function(workOrderApprovalObj){
								if(workOrderApprovalObj.selectedToPendingstatus==true){
									angular.forEach($scope.searchNewWOItemList, function(value, key) {
										
										if(value.vendorId !=workOrderApprovalObj.vendorId ){
											value.isReject=true;
										}
									});
								}
								
								if(workOrderApprovalObj.selectedToPendingstatus==false){
									var selectedList=[];
									angular.forEach($scope.searchNewWOItemList, function(value, key) {
										
										if(value.selectedToPendingstatus==true){
											selectedList.push(value);
										}
									})
								
									angular.forEach($scope.searchNewWOItemList, function(value, key) {
										if(selectedList.length == 0){
											if(value.vendorId !=workOrderApprovalObj.vendorId ){
												value.isReject=false;
											}
										}
										
									});
								}
								
								
							}
							
							
							
							$scope.SettledWOBillDetailsList=function(infoObject){
							var data={};
							data.workorderSettlementId=infoObject.workorderSettlementId;
								$rootScope.startLoaderPopup();
								 $scope.getRequestListDetails=[];
								var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getRequestSettledListDetails";
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									if(response.data.status == "success"){
										$rootScope.stopLoaderPopup();
										if(response.data.listObject!=null && response.data.listObject.length >0)
										{   $("#allocated_Request_Details_modal").modal("show");
											$scope.getRequestListDetails = response.data.listObject;
										}
										// else{
										// 	 growl.warning("No Record found..",
										// 				{
										// 					title : 'warning!'
										// 				});
										// }
									}else{
										$rootScope.stopLoaderPopup();
									}
									
								});
								
								
							}
							
							
							$scope.setAmountForSettle =function(){
								
								$scope.selectedFinalList=[];
								var payableamount=parseFloat(0);
								angular.forEach($scope.searchNewWOItemList, function(value, key) {
									
									if(value.selectedToPendingstatus==true){
										payableamount= parseFloat (parseFloat(payableamount)+ parseFloat(value.balanceAmount)).toFixed(2);
										$scope.selectedFinalList.push(value);
									}
								});
								
								if($scope.selectedFinalList.length==0){
									growl.warning("Please select at least one WorkOrder Request",
											{
												title : 'warning!'
											});
									return;
								}
								
								
								$scope.listPaymentDetails=[];
								$scope.finalAmount=0;
								$scope.fundAmount=parseFloat(payableamount);
								$scope.calculateTotalDueRefAmount();
								$scope.initPaymentDetailsObj();
								$("#fundCollectPayment").modal();
								$scope.setFirstTabLevel(1);
								$scope.setPaymentModeId(1);
								 
								$scope.paymentDetails.cashAmount=parseFloat($scope.fundAmount);
								$scope.paymentDetails.chequeAmount=parseFloat($scope.fundAmount);
								$scope.paymentDetails.credAmount=parseFloat($scope.fundAmount);
								$scope.paymentDetails.transferAmount=parseFloat($scope.fundAmount);
								
								
							}
							
							$scope.getNewWOElementForApprovalEdit=function(workorderRequestId,workorderRequestStatusId,workOrderCompletionId){
								var data = {
										    "workorderRequestId" : workorderRequestId,
										    "workOrderCompletionId":workOrderCompletionId
										};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationApprovalDetailsbyId/settlementDetails";
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									if(response.data.status == "success"){
										$rootScope.stopLoader();
										console.log("complete object",response.data.object);
										if(response.data.object!=null){
											if(response.data.message=="updatedata"){
												if(response.data.object.paymentStatusId==1)
													{
													response.data.object.paymentStatus="Pending";
													}
												if(workorderRequestStatusId!=5){
													$state.go('deatilsWorkOrderSettlement',
															{
														      resultworkOrApprovalderDetailsUpdateCommonObj: response.data.object
															});	
												}
												if(workorderRequestStatusId==5){
													$state.go('deatilsWorkOrderSettlement',
															{
														      resultworkOrApprovalderDetailsReadOnlyCommonObj: response.data.object
															});	
												}
												
											}
											
											if(response.data.message=="savedata"){
												$state.go('deatilsWorkOrderSettlement',
														{
													      resultworkOrApprovalderDetailsSaveCommonObj: response.data.object
														});	
											}
											
											
										}
										
										
									}
								});
							
							
						}
				
						
		
						// to and from date picker
		/*				$scope.woSettlementSerach.toDate= new Date();
						$scope.woSettlementSerach.fromDate= new Date();*/
						$scope.openDatePicker1 = function() { 
							$scope.datepicker1.opened = true;
						};
						$scope.datepicker1 = {
								opened : false
							};
						$scope.openDatePicker2 = function() { 
							$scope.datepicker2.opened = true;
						};
						$scope.datepicker2 = {
								opened : false
							};
						
						$scope.openDatePicker3 = function() { 
							$scope.datepicker3.opened = true;
						};
						$scope.datepicker3 = {
								opened : false
							};
						var trustId="";
						$scope.getTrustDetails = function(id) {
							  $scope.getTrustInflowDetailsByTrustId(id);
							  $scope.trustObj.trustId=id;
								
								
						}
						
						
						$scope.collectionList = function(toDate,fromDate,trustId) {
							if(toDate==undefined || fromDate ==undefined ){
								$scope.trustObj.toDate= new Date();
								$scope.trustObj.fromDate= new Date();
							}
							    var dateTo= moment(toDate).format('YYYY-MM-DD');
								var dateFrom=  moment(fromDate).format('YYYY-MM-DD');
					
							var data={
								toDate:dateTo,
								fromDate:dateFrom,
								orgId:$scope.orgId,
								unitId:$scope.unitId,
								trustId:trustId			
							};			
				
				        var URI = BASE_URL + ROOT_URL +"/api/billing/getCollectionList/"+$scope.orgId +"/"+$scope.unitId;
				    GenericService
				        .serviceAction(METHOD_POST, URI,
				                data)
				        .then(
				                function(response) {
				                    if (response.data.status == "success"){
				                        $scope.CollectList = response.data.listObject;
				                        console.log("list",$scope.CollectList);
				                    }
				                   
				                });

					
						}
//                    	$scope.collectionList(); 
						
						$scope.getStoreList=[];
						 $scope.getStorelist = function(){
							var URI = BASE_URL + ROOT_URL + API + "/procurements/WOCreation/dropdowns" + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
							BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.status == 200){
									$scope.getStoreList = response.data.store;
									
									
									
								}
								console.log("getStorelist",$scope.getStoreList);
							});
						}
						 $scope.getStorelist ();
						 
						 
                        $scope.prevSearch='';
						 
					     $scope.SearchVendorNames = function(search){
						      if (search.length < 3) return;
						      $scope.vendorNames=[];
						      var URI = BASE_URL + ROOT_URL + API + "/global/vendor/search/"+ search + "/"+$scope.unitId;
						      return GenericServiceParamHeader.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.status == 200){
										return response.data;
									}
								
								});
						      
						      
						    }
					     
						 $scope.clearWorkorderListObj = function(){
							 $scope.initCommon();
							}
						 
							$scope.selectVendorTemp = function($item, $model, $label){
								
								$scope.woSettlementSerach.vendorName=$item.vendorName;
								$scope.woSettlementSerach.vendorId= $item.id;
								console.log("woSettlementSerach",$scope.woSettlementSerach);
								
								
							 }
                               $scope.selectVendorTemp2 = function($item, $model, $label){
//                            	   SettledWorkOrderSearch.vendorName
								$scope.SettledWorkOrderSearch.vendorName=$item.vendorName;
								$scope.SettledWorkOrderSearch.vendorId= $item.id;
								console.log("SettledWorkOrderSearch",$scope.SettledWorkOrderSearch);
								
								
							 }
							
						//-----------------payment collection Start---------------//
						$scope.getBankList = function(){
							var obj={
									"unitId":$scope.unitId,
									"orgnisationId" :  $scope.orgId
							}
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + API + BILLING + "/bankMaster";
							BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									$scope.bankList = response.data.listObject;
									console.log($scope.bankList)
								}
							});
						}
						
						$scope.getBankList();
						$scope.getHospitalBankList = function()
						{
							var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+$scope.orgId+"/"+$scope.unitId;
							BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
						       {
								if (response.data.status == "success")
								{
									$scope.hospitalBankList = response.data.listObject;
								}
							  });
						}
						$scope.getHospitalBankList();
						
						$scope.getCardTypesList = function(){
							var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
							var param = {
									orgnisationId:$scope.orgId,
									unitId:$scope.unitId
									};
							BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success"){
									$scope.cardTypeList = response.data.listObject;
								}
							});
						}
						$scope.getCardTypesList();
						
						$scope.transactionTypesList=[];
						 $scope.getTransactionTypes = function(){
							var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+$scope.orgId+"/"+$scope.unitId;
							BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									$scope.transactionTypesList = response.data.listObject;
								}
								
								console.log("transactionTypesList",$scope.transactionTypesList);
							});
						}
						 $scope.getTransactionTypes ();
						 
						$scope.initPaymentDetails=function(){
							$scope.paymentDetails={
								    "paymentModeId"			:[],
								    "bankId"				:"",
									"accountNumber"			:"",
									"chequeNumber"			:"",
									"cardNo"				:"",
									"cardHolderName"		:"",
									"dateOfTransaction"		:new Date().getTime(),
									"expiryDate"			:"",
									"unitId"				:$scope.unitId,
									"orgId"			        :$scope.organizationId,
									"status"				:"A",
									"createdDate"			:new Date().getTime(),
									"createdBy"				:$scope.userId,
									"updatedDate"			:new Date().getTime(),
									"updatedBy"				:$scope.userId,
									"chequeDate"			:"",
									"cardTypeId"			:"",
									"isCancle"				:"I",
								    "otp"	                 :''
									
							}
							
							$scope.patientSearchObj = {
									"uhIdNumber"      : "",
									"patientName"     : "",
									"genderId"        : "",
									"mobileNo"        : "",
									"birthDate"       : "",
									"identificationNo":"",
									"patientType"	  :"1",
									"unitId"          :$scope.unitId,
									"organizationId"  :$scope.organizationId
							}
							
						};
						$scope.initPaymentDetails();
						
						$scope.paymentModes = {
								1: "Cash",
								2: "Cheque",
								3: "Credit Card",
								8: "E - Transfer",
								5: "Loyalty Card"	
						}
						
						$scope.setPaymentModeId=function(paymentModeId){
							console.log(paymentModeId)
							$scope.paymentDetails.paymentModeId=paymentModeId;
							$scope.firstTabLevel = paymentModeId;
							$scope.isSetFirstTabLevel(paymentModeId);
							
							//$scope.paymentDetails.paymentModeName=$scope.paymentModes[paymentModeId];
						}
						
						$scope.firstTabLevel = 1 ;
						
						$scope.isSetFirstTabLevel = function(tabNum)
						{
							return $scope.firstTabLevel === tabNum;
						};
						
						
						$scope.setFirstTabLevel= function(newTab)
						{
							$scope.paymentDetails.paymentModeId = newTab;
							$scope.firstTabLevel = newTab;
						};
						
						$scope.setBankName=function(bankId){
							angular.forEach($scope.bankList, function (value, key) {
								if(value.bankId == bankId){
									$scope.paymentDetails.bankName = value.bankName;
									return;
								}
							});
							
						}
						$scope.initPaymentDetailsObj = function(){
							$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
							$scope.eTransferObj={};
						}
						
						/*var payId=[];
						angular.forEach($scope.paymentModeId,
								function(value,index) {
							    payId.push(value.id);
								});*/
						//billing fatory
						      $scope.listPaymentDetails = [];
							$scope.addPaymentDetails = function(scope){
								var amount=0;
								angular.forEach($scope.listPaymentDetails,function(value,key){
									console.log(value)
									console.log(value.amount)
									amount = amount + parseFloat(value.amount); 
									console.log(amount)
								});
								
							switch($scope.paymentDetails.paymentModeId){
									case 1 :
										console.log($scope.paymentDetails.cashAmount)
										if(typeof($scope.paymentDetails.cashAmount) == "undefined" || $scope.paymentDetails.cashAmount == "" || $scope.paymentDetails.cashAmount == parseFloat(0) || $scope.paymentDetails.cashAmount < parseFloat(0)){
											growl.error("Please insert an amount",{
												title : ERROR_MSG
											});
											return;
										}
										
										if($scope.paymentDetails.cashAmount!= null)
											{
											var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.cashAmount;
											   if($scope.paymentDetails.cashAmount<$scope.fundAmount)
												   {
												   $scope.paymentDetails.cashAmount=$scope.fundAmount;
												   growl.error("Cash amount should be euqal to due amount ",{
														title : ERROR_MSG
													});
												   return false;
												   }
											if(dueamt<0){
												   growl.error("Please insert valid amount",{
														title : ERROR_MSG
													});
												    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
												   return;
											   }
											 var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2)};
											   $scope.listPaymentDetails.push(paymentDetails);
											   console.log($scope.listPaymentDetails)
											   
											    
											}
										break;
									case 2 :
										if(typeof($scope.paymentDetails.chequeAmount) == "undefined" || $scope.paymentDetails.chequeAmount == "" || $scope.paymentDetails.bankId == 0 || $scope.paymentDetails.chequeNumber == "" || typeof($scope.paymentDetails.chequeNumber) == "undefined" || typeof($scope.paymentDetails.chequeDate) == "undefined" || $scope.paymentDetails.chequeDate == "" || $scope.paymentDetails.chequeAmount == parseFloat(0) || $scope.paymentDetails.chequeAmount < parseFloat(0)){
											growl.error("Please select bank, chequeno, cheque date and amount",{
												title : ERROR_MSG
											});
											return;
										}
										
										var bankName = "";
										//Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.paymentDetails.bankId == value.bankId)
												bankName = value.bankName;
										})
										
										if($scope.paymentDetails.chequeAmount!=null){
											
											var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.chequeAmount;
											if($scope.paymentDetails.chequeAmount<$scope.fundAmount)
											   {
												$scope.paymentDetails.chequeAmount=$scope.fundAmount;
											   growl.error("Check amount should be euqal to due amount ",{
													title : ERROR_MSG
												});
											   return false;
											   }
											if(dueamt<0){
												   growl.error("Please insert valid amount",{
														title : ERROR_MSG
													});
												   $scope.paymentDetails=[];
												    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
												   return;
											   }
											var chequeDate = $rootScope.getFormattedDate($scope.paymentDetails.chequeDate,false);
											
											var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.chequeAmount).toFixed(2),createdBy:$scope.userId, bankId:$scope.paymentDetails.bankId, chequeNumber:$scope.paymentDetails.chequeNumber, chequeDate:chequeDate,bankName:bankName};//, bankName:bankName
												$scope.listPaymentDetails.push(paymentDetails);
										}
										break;
									
									case 3 :
										if(typeof($scope.paymentDetails.credAmount) == "undefined" || $scope.paymentDetails.credAmount == "" ||$scope.paymentDetails.credAmount =="" ||$scope.paymentDetails.credCardNumber==undefined ||$scope.paymentDetails.credCardNumber==""){
											growl.error("Please select card number and amount",{
												title : ERROR_MSG
											});
											return;
										}
										if($scope.paymentDetails.credAmount!=null){
											var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.credAmount;
											if($scope.paymentDetails.credAmount<$scope.fundAmount)
											   {
												$scope.paymentDetails.credAmount=$scope.fundAmount;
											   growl.error("Card amount should be euqal to due amount ",{
													title : ERROR_MSG
												});
											   return false;
											   }
											if(dueamt<0){
												   growl.error("Please insert valid amount",{
														title : ERROR_MSG
													});
												   $scope.paymentDetails=[];
												    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.paymentModeId=3;
												   return;
											   }
											
										var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.credExpiryDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate};
										$scope.listPaymentDetails.push(paymentDetails);
										$scope.paymentDetails.paymentModeId=3;
										}
										break;
										
									case 8 : 
										if(typeof($scope.paymentDetails.transactionobj==undefined||$scope.paymentDetails.transactionobj=="" ||$scope.paymentDetails.transferAmount) == "undefined" || $scope.paymentDetails.transferAmount == "" ||$scope.paymentDetails.transactionobj.id==0||$scope.paymentDetails.transactionNo ==0 || $scope.paymentDetails.transactionNo == undefined || $scope.paymentDetails.transBankId == ""|| $scope.paymentDetails.transBankId==0 ||$scope.paymentDetails.transactionNo==0||$scope.paymentDetails.transactionNo=="" || typeof($scope.paymentDetails.transTransactionDate) == "undefined" || $scope.paymentDetails.transTransactionDate == "" ){
											growl.error("Please select Name, card type,card number expiry date and amount",{
												title : ERROR_MSG
											});
											return;
										}
										
										var bankName = "";
										// Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.paymentDetails.transBankId == value.bankId)
												bankName = value.bankName;
										})
										var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.transferAmount;
										if($scope.paymentDetails.transferAmount<$scope.fundAmount)
										   {
											scope.paymentDetails.transferAmount=$scope.fundAmount;
										   growl.error("Transfer amount should be euqal to due amount ",{
												title : ERROR_MSG
											});
										   return false;
										   }
										if(dueamt<0){
											   growl.error("Please insert valid amount",{
													title : ERROR_MSG
												});
											   $scope.paymentDetails=[];
											    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
												$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
												$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
												$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
											   return;
										   }	
									 var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.transferAmount).toFixed(2), transactionType:$scope.paymentDetails.transactionobj.name, transactionTypeId:$scope.paymentDetails.transactionobj.id,transTransactionDate:moment($scope.paymentDetails.transTransactionDate).format('DD-MM-YYYY HH:mm:ss'),bankId:$scope.paymentDetails.transBankId,transactionNo:$scope.paymentDetails.transactionNo,createdBy:$scope.userId,bankName:bankName};
										   $scope.listPaymentDetails.push(paymentDetails);
										   console.log(paymentDetails)
										break;
										
									case 5:
										if($scope.mpesaObject.resTransactionId!=$scope.mpesaObject.transactionId){
											growl.warning("Invalid Transaction Id",{
												title : WARNING_MSG
											});
											$scope.mpesaObject.mpesaAmount=null;
											return;	
										}
										if($scope.mpesaObject.mpesaAmount == 0 || $scope.mpesaObject.mpesaAmount == null){
											growl.warning("Amount cannot be zero",{
												title : WARNING_MSG
											});
											return;
										}
										var invalidMpesa = false;
										angular.forEach($scope.listPaymentDetails,function(value,key){
											if($scope.mpesaObject.resTransactionId == value.cardNo)
											{
												growl.warning("Collected MPesa is already used in current tranasction",{
													title : WARNING_MSG
												});
												invalidMpesa = true;
											}
										});
										if(invalidMpesa == true)
											return;
										
										if($scope.isBillList == 0 || $scope.isBillList != undefined){
											if($scope.transferToActiveDepositFact == undefined || $scope.transferToActiveDepositFact == 'N'){
										if(parseFloat($scope.mpesaObject.mpesaAmount) <= parseFloat($scope.payeeDueAmtFinal))
										{
											var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:$scope.mpesaObject.resTransactionId,transactionNo:$scope.mpesaObject.resTransactionId,createdBy:$scope.userId};
											   $scope.listPaymentDetails.push(paymentDetails);
										 }
										else {
											growl.error("Entered Amount Should not be greater than Self Due Amount",{
												title : ERROR_MSG
											});
											return 
										}
										}
											else
												{
												var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:$scope.mpesaObject.transactionId,transactionNo:$scope.mpesaObject.transactionId,createdBy:$scope.userId};
												   $scope.listPaymentDetails.push(paymentDetails);
												}
										}
										$scope.mpesaObject={};
										break;
										
										
								}
								//$scope.initPaymentDetailsObj(angular.element(document.getElementById("uiView")).$scope());
								
								//calculate due amount by page
								switch($scope.page){
								case "billList":
									$scope.calculateTotalDueRefAmount();
									break;
								case "companyInvoiceSettlement":
									$scope.validCollectedAmount($scope.listPaymentDetails.length-1);
									break;
								}
								$scope.calculateTotalDueRefAmount();
								$scope.initPaymentDetailsObj();
								 $scope.setFirstTabLevel(1);
								$scope.setPaymentModeId(1);
								$scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
								
							}
							
							
							
							 // calculate ammount
							$scope.totalAmount;
							$scope.calculateTotalDueRefAmount = function()
							{
								$scope.totalAmount = 0;
								angular.forEach($scope.listPaymentDetails,function(value,key){
								
									$scope.totalAmount = $scope.totalAmount + parseFloat(value.amount); 
									$scope.finalAmount=$rootScope.parseFloatNum($scope.totalAmount);
								});
								
								console.log("final Amount ---"+$rootScope.parseFloatNum($scope.totalAmount))
								
									  
							};
							$scope.removePaymentDetails = function(paymentObj, index){
								$scope.totalAmount=$scope.totalAmount - parseFloat(paymentObj.amount);
								$scope.finalAmount=$rootScope.parseFloatNum($scope.totalAmount);
							    var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
								if(tempObj!=null && (tempObj.cardNumber == $scope.paymentDetails.loyaltyCardNo))
									{
									 $scope.paymentDetails.cardBalAmount = $scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
									}
								$scope.listPaymentDetails.splice(index,1);
								
								$scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
							}
							
							
							
							$scope.collectAmount=function(id){
									var finalList=[];
									var vendorId=0;
                                    angular.forEach($scope.searchNewWOItemList, function(value, key) {
									if(value.selectedToPendingstatus==true){
										finalList.push(value);
										vendorId=value.vendorId;
										
									}
								    });
                                    angular.forEach($scope.listPaymentDetails, function(value, key) {
                                    	if(value.paymentModeId==8){
                                    		      
                                    		value.transTransactionDate =moment(value.transTransactionDate).format('DD-MM-YYYY HH:mm:ss');
                                    	}
                                    	
                                    	
                                    });
                                    
                                    
//                                    moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                                    
                                    
                                    if(finalList.length==0){
                                    	growl.warning("Please select at least one WorkOrder Request",
    											{
    												title : 'warning!'
    											});
    									return;
                                    }
                                    
                                    
								var data={};
								data.workOrderRequestList=finalList;
								data.settlementDetailsDto = $scope.listPaymentDetails;
								data.settlementAmount=$scope.fundAmount;
								data.vendorId=vendorId;
								data.orgId= $scope.orgId;
								data.unitId = $scope.unitId;
								data.createdBy=  $scope.userId ;
								console.log(data);
								$rootScope.startLoaderPopup();
								var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/saveWorkOrderSettlement";
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									if(response.data.status == "success"){
										$rootScope.stopLoaderPopup();
										growl.success(
												"saved successfully...",
												{
													title : 'Success!'
												});
										$scope.getListNewWOList();
										$scope.searchSettledList();
										$("#fundCollectPayment").modal("hide");
										
										}else{
											$rootScope.stopLoaderPopup();
											growl.error(
													response.data.message,
													{
														title : 'Error!'
													});
											$scope.getListNewWOList();
											$scope.searchSettledList();
											$("#fundCollectPayment").modal("hide");
										}
								});
								
							}
							
							 
						
						
							
							$scope.temdiagnolist=[];
			     	
			     	
			    	$scope.showFieldList = function(id){
						try{
							$scope.fieldMasterList=[];
							$scope.saveBtnFlag = false;
							$scope.updateScope = true;
							$scope.updateBtnFlag = true;
							var data={};
							var URI = BASE_URL + ROOT_URL + "/api/billing/getVisitType" +"/"+ id +"/"+ $scope.orgId +"/"+$scope.unitId;
							
							GenericService.serviceAction("GET",URI,data).then(function(response){
								
								if(response.data.status == "success"){
		 							$scope.fieldMasterList= response.data.listObject;
								}else if(response.data.status == 'error'){
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							});
							
						}catch(e){
							console.log("Exception-",e);
						}
					}
			    	
			    	
			   
			    	
			    	
			    	
			    	
			    	
					
					
					
					$scope.commonVitualTrustAcceptanceList=[];
					$scope.getFundApprovalRequestDetails =function(){
						
//						data-toggle="modal" data-target="#approve_Request_Details_modal"
						if($scope.trustObj.trustId==undefined || $scope.trustObj.trustId==""  ) 
						{
							growl.warning("Please select Trust",
									{
										title : 'warning!'
									});
							return;
						}else{
							$scope.fundAmount=0;
							$scope.finalCheckedAmount=0;
							$("#approve_Request_Details_modal").modal("show");
							$rootScope.startLoaderPopup();
							var data = {};
							
							if($scope.selectedPatientId != undefined && $scope.selectedPatientId != ""){
								data={  trustId : $scope.trustObj.trustId,
								        orgId : $scope.orgId ,
							            unitId:$scope.unitId,
							            patientId:$scope.selectedPatientId
							        };
								
							}else{
								data={  trustId : $scope.trustObj.trustId,
								        orgId : $scope.orgId ,
							            unitId:$scope.unitId
							        };
							}
							
								
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getAllPendingVirtualAcceptance";
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
					                    console.log(response);
					                    if (response.data.status == "success"){
					                    	$rootScope.stopLoaderPopup();
					                    	$scope.commonVitualTrustAcceptanceList=[];
					                    	
					                    	$scope.commonVitualTrustAcceptanceList=response.data.listObject;
					                    	if($scope.commonVitualTrustAcceptanceList!=null){
					                    		angular.forEach($scope.commonVitualTrustAcceptanceList, function(value, key) { 
											   		value.sanctionApprovedflagforCheck=false;
											   		value.virtualAcceptancePaidAmmount="";
											   	 });
					                    	}
					                    	console.log($scope.commonVitualTrustAcceptanceList);
					                    }
					                });
							        
						}
						
					}
$scope.finalCheckedAmount=0;
$scope.getcheckedFundApprovalDetails =function(index){
	$scope.finalCheckedAmount=0;
	
	var zeroenterd=0;
	var envalidamount=0;
	$scope.fundAmount=0;
	    angular.forEach($scope.commonVitualTrustAcceptanceList, function(value, key) { 
   		     if(value.sanctionApprovedflagforCheck==true){
   		    	 
   		    	if(key==index && (value.virtualAcceptancePaidAmmount==undefined ||  value.virtualAcceptancePaidAmmount=="") ) {
   		    		value.virtualAcceptancePaidAmmount=parseFloat ( value.requestApprovalAmount - value.virtualPaidAmount);
   		    		$scope.finalCheckedAmount= parseFloat ($scope.finalCheckedAmount + value.virtualAcceptancePaidAmmount) ;
   		    	}else if(key==index && (value.virtualAcceptancePaidAmmount!=undefined ||  value.virtualAcceptancePaidAmmount!=""))
   		    	{
   		    		if(value.virtualAcceptancePaidAmmount==0 || value.virtualAcceptancePaidAmmount=="" ){
   		    			zeroenterd++;
   		    			value.virtualAcceptancePaidAmmount="";
   		    		}
   		    		  if(value.virtualAcceptancePaidAmmount> 0 && (parseFloat( value.virtualAcceptancePaidAmmount) >  parseFloat ( value.requestApprovalAmount - value.virtualPaidAmount)) )
   		    		  {
   		    			envalidamount++; 
   		    			value.virtualAcceptancePaidAmmount="";
   		    	      }else if(value.virtualAcceptancePaidAmmount!=""){
   		    	    	value.virtualAcceptancePaidAmmount=value.virtualAcceptancePaidAmmount;
   	   		    		$scope.finalCheckedAmount= parseFloat ($scope.finalCheckedAmount) + parseFloat(value.virtualAcceptancePaidAmmount);
   		    	     }
   		    		
   		    	}
   		    	
   		    	if(key!=index && (value.virtualAcceptancePaidAmmount!=undefined ||  value.virtualAcceptancePaidAmmount!=""))
   		    	{
   		    		$scope.finalCheckedAmount=parseFloat($scope.finalCheckedAmount + value.virtualAcceptancePaidAmmount);
   		    	}
   		    	
		    	
   		      }else{
   		    	value.virtualAcceptancePaidAmmount="";
   		      }
   		     
   		       
   	     });
	    if($scope.finalCheckedAmount > 0){
	    	$scope.fundAmount=$scope.finalCheckedAmount;
	    }
	    
	    if(zeroenterd>0){
	    	growl.warning("Entered amount  should not be  zero or empty.",
					{
						title : 'warning!'
					});
	    	
	    }
        if(envalidamount>0){
        	growl.warning("please enter valid amount ",
					{
						title : 'warning!'
					});
	    	
	    }
	    
						
}
					
					$scope.fundAmount='';
					$scope.setFundAmmount =function(){
					//	data-toggle="modal" data-target="#fundCollectPayment"
						$scope.commonVitualTrustAcceptanceCheckedList=[];
						if( $scope.commonVitualTrustAcceptanceList!= null && $scope.commonVitualTrustAcceptanceList.length>0 ){
							
							angular.forEach($scope.commonVitualTrustAcceptanceList, function(value, key) { 
					   		     if(value.sanctionApprovedflagforCheck==true){
					   		    	$scope.commonVitualTrustAcceptanceCheckedList.push(value);
					   		      }
					   	     });
						}
					
						
						if($scope.trustObj.trustId==undefined || $scope.trustObj.trustId==""  ) 
						{
							growl.warning("Please select Trust",
									{
										title : 'warning!'
									});
							return;
						}else if( $scope.selectedtrustFundTypeId==undefined || $scope.selectedtrustFundTypeId==""  ){
							growl.warning("Please select Fund Type",
									{
										title : 'warning!'
									});
							return;
						}else if( $scope.selectedtrustFundTypeId ==2 &&  $scope.selectedPatientId==""  ){
							growl.warning("Please Select Patient",
									{
										title : 'warning!'
									});
							return;
						}else if($scope.selectedtrustFundTypeId == 3 && $scope.addDiagnosisList.length==0){
							growl.warning("Please Select Diagnosis",
									{
										title : 'warning!'
									});
							return;
						}else if($scope.selectedtrustFundTypeId == 4 && $scope.commonVitualTrustAcceptanceCheckedList.length==0){
							growl.warning("Please Select Virtual accepted Request",
									{
										title : 'warning!'
									});
							return;
						}else if( $scope.fundAmount==undefined || $scope.fundAmount==""  ){
							growl.warning("Please enter Amount",
									{
										title : 'warning!'
									});
							return;
						}else{
							$scope.listPaymentDetails=[];
							$scope.finalAmount=0;
							$scope.calculateTotalDueRefAmount();
							$scope.initPaymentDetailsObj();
							$("#fundCollectPayment").modal();
							$scope.setFirstTabLevel(1);
							$scope.setPaymentModeId(1);
							 
							$scope.paymentDetails.cashAmount=parseFloat($scope.fundAmount);
							$scope.paymentDetails.chequeAmount=parseFloat($scope.fundAmount);
							$scope.paymentDetails.credAmount=parseFloat($scope.fundAmount);
							$scope.paymentDetails.transferAmount=parseFloat($scope.fundAmount);
							
						}
						
						
					}
					
					$scope.getPatientDetailsPopUp=function(info){
						$scope.patientDetails="";
						$scope.uhidNumber="";
						$scope.visitDetails="";
						$scope.billDetails="";
						$scope.patientDetails=info.patientDetails;
						$scope.uhidNumber=info.uhidNumber;
						$scope.visitDetails=info.visitDetails;
						$scope.billDetails=info.billDetails;
					}
					
					  /***Chetana**/
				    $scope.printWorkOrderSettlementReport = function (workOrder) {
				        var data = {
				            "appName": "HIS",
				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				            "reportDesignFilePath": "get_workorder_Settlement.rpt",
				            "reportFormat": "PDF",
				            "exportFileName": "WorkOrderSettlement",
				            "reportParams": [
				                {
				                    "paramName": "Id",
				                    "paramType": "Integer",
				                    "paramValue": workOrder.workorderRequestId
				                }
				            ]
				        };
				        reportService.generate(REPORT_API_URI1, data);
				    }
				   /*****/ 
				    
				    /***Chetana**/
				    $scope.printWorkOrderReport = function (workOrder) {
				        var data = {
				            "appName": "HIS",
				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				            "reportDesignFilePath": "get_workorder_Settlement.rpt",
				            "reportFormat": "PDF",
				            "exportFileName": "WorkOrderSettlement",
				            "reportParams": [
				                {
				                    "paramName": "Id",
				                    "paramType": "Integer",
				                    "paramValue": workOrder.workorderRequestId
				                }
				            ]
				        };
				        reportService.generate(REPORT_API_URI1, data);
				    }
				   /*****/ 
				}
						
 ]);