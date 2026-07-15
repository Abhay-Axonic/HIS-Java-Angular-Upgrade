
angular
		.module('myApp')
		.controller(
				"workOrderCompletionDetailsController",
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
							$scope.btnText="";
							$scope.readOnlyApproved=false;
							

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
							
							
							var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
							if(cookieObjectStore==undefined){
								$rootScope.storePopup();
								angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
							}
							$scope.storeName = cookieObjectStore.storeName;
							$scope.storeId = cookieObjectStore.storeId;
							
							
							$scope.workOrderItemDetails=[];
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							 $scope.workOrderItemDetailsList=[];
							 $scope.workOrderCreationStagedListData=[];
							 $scope.finalDocUploadList=[];
							console.info($scope.orgId)
								var data = {};
							//$(".selectedPageName").text("Machine Master");
							//Date For From Date on Search
							
							/* $scope.searchNewWOItemList= [];
								$scope.getListNewWOList = function () {
									 $scope.searchNewWOItemList= [];
										var data = {};
										data.orgId= $scope.orgId;
										data.unitId= $scope.unitId;
										data.storeId= $scope.StoreWorkOrderAppovalListObj.storeId;
										
										if($scope.StoreWorkOrderAppovalListObj.toDate!=undefined && $scope.StoreWorkOrderAppovalListObj.toDate!="" )
										{
											data.toDate=moment( $scope.StoreWorkOrderAppovalListObj.toDate ).format('YYYY-MM-DD');
										}
										
										if($scope.StoreWorkOrderAppovalListObj.fromDate!=undefined && $scope.StoreWorkOrderAppovalListObj.fromDate!="" )
										{
											data.fromDate=moment( $scope.StoreWorkOrderAppovalListObj.fromDate ).format('YYYY-MM-DD');
										}
										if($scope.StoreWorkOrderAppovalListObj.workorderRequestNumber!=undefined && $scope.StoreWorkOrderAppovalListObj.workorderRequestNumber!="" )
										{
											data.workorderRequestNumber=$scope.StoreWorkOrderAppovalListObj.workorderRequestNumber ;
										}
										
										if($scope.StoreWorkOrderAppovalListObj.billTypeId!=undefined && $scope.StoreWorkOrderAppovalListObj.billTypeId!='' )
										{
											data.billTypeId=$scope.StoreWorkOrderAppovalListObj.billTypeId ;
										}
										
										if($scope.StoreWorkOrderAppovalListObj.vendorId!=undefined && $scope.StoreWorkOrderAppovalListObj.vendorId!='' && $scope.StoreWorkOrderAppovalListObj.vendorId!=0 )
										{
											data.vendorId=$scope.StoreWorkOrderAppovalListObj.vendorId ;
										}
										if($scope.StoreWorkOrderAppovalListObj.workorderRequestStatusId!=undefined && $scope.StoreWorkOrderAppovalListObj.workorderRequestStatusId!='' && $scope.StoreWorkOrderAppovalListObj.workorderRequestStatusId!=0 )
										{
											data.workorderRequestStatusId=$scope.StoreWorkOrderAppovalListObj.workorderRequestStatusId ;
										}
										
										if($scope.StoreWorkOrderAppovalListObj.storeId==undefined || $scope.StoreWorkOrderAppovalListObj.storeId==''){
									    	  growl.warning("Please select store",
														{
															title : 'warning!'
														});
												return;
									      }
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationRequestList";
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
												}else{
													 growl.warning("No Record found..",
																{
																	title : 'warning!'
																});
												}
											}
										});
								}*/
							
							
							$scope.initCommon = function() {
								  $scope.itemRejectionObject={};
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.StoreWorkOrder = {
										"workorderRequestApprovalId":'',
										"workorderRequestId":'',	
										"storeId" : '',
										"vendorId" : '',
										"vendorName" : '',
										"contractTypeId":'',
										'billTypeId':'',
										'deliveryDate':"",
										'guaranteeWarranty':"",
										'paymentTerms':"",
										"status" : "A",
										"createdBy" : $scope.userId,
										"createdDate" : "",
										"updatedBy" : $scope.userId,
										"updatedDate" : "",
										"orgId" : $scope.orgId,
										"unitId" : $scope.unitId,
										"totalAmount":0,
										"againstTypeId":'',
										"totalDiscountAmount":0,
										"totalTaxAmount":0,
										"otherChargeAmount":0,
										"totalNetAmount":0,
										"requestApprovalDetailsDto":[],
										"requestTaxDetailsDto":[],
										"requestDocumentDto":[],
										"approvalRemark":""
										
										
									}
								
								$scope.StoreWorkOrder.storeId=$scope.storeId;
//								$scope.getListNewWOList();
								
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
									showWeeks : false
								
								};
							$scope.dateOptions1 = {
									formatYear : 'yyyy',
									showWeeks : false
									
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
							
							
							//Date For To Date on Search
							var visitList=[];
							angular.forEach($scope.paymentModeId,
									function(value,index) {
								     visitList.push(value.id);
									});
							
						
		                      $scope.taxpercObj ={};
						// to and from date picker
//						$scope.StoreWorkOrderAppovalListObj.toDate= new Date();
//						$scope.StoreWorkOrderAppovalListObj.fromDate= new Date();
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
						$scope.openDatePicker4 = function() { 
							$scope.datepicker4.opened = true;
						};
						$scope.datepicker4 = {
								opened : false
							};
						$scope.creditDays=0;
						$scope.assignDueValue = function() { 
					        $scope.common.dueDate=new Date($scope.common.invoiceDate.getTime() + ($scope.creditDays)*24*60*60*1000);
						}
						/*var trustId="";
						$scope.getTrustDetails = function(id) {
							  $scope.getTrustInflowDetailsByTrustId(id);
							  $scope.StoreWorkOrder.trustId=id;
								
								
						}*/
						
				/*		$scope.getStoreList=[];
						 $scope.getStorelist = function(){
							var URI = BASE_URL + ROOT_URL + API + "/procurements/WOCreation/dropdowns";
							BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.status == 200){
									$scope.getStoreList = response.data.store;
									
									
									
								}
								console.log("getStorelist",$scope.getStoreList);
							});
						}
						 $scope.getStorelist ();*/
						 
						 
						 
						 
						 
						 $scope.prevSearch='';
						 
					     $scope.SearchVendorNames = function(search){
						      if (search.length < 1) return;
						      $scope.vendorNames=[];
						      var URI = BASE_URL + ROOT_URL + API + "/global/vendor/search/"+search;
						      return GenericServiceParamHeader.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.status == 200){
										return response.data;
									}
								
								});
						      
						      
						    }
						 
						 
							$scope.selectVendorTemp = function($item, $model, $label){
														/*newdata.diagnosisId = $item.diagnosisId;
														newdata.diagnosisName = $item.diagnosisName;
														newdata.code = $item.code;
														$scope.addDiagnosisList.push(newdata);
														$scope.searchDiagnosis="";*/
								
								$scope.StoreWorkOrderAppovalListObj.vendorName=$item.vendorName;
								$scope.StoreWorkOrderAppovalListObj.vendorId= $item.id;
								console.log("StoreWorkOrderAppovalListObj",$scope.StoreWorkOrderAppovalListObj);
								
								
							 }
						 
						/*	$scope.getDocumentType = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") {
											$scope.documentTypeList = response.data.listObject;
											 angular.forEach($scope.documentTypeList, function(value, key) 
											 {  
									 			$scope.corporateDocTypeId=value.documentTypeId;
											 });
										}
									});
							}
							$scope.getDocumentType();*/
							
							
							
							
							/*$scope.getOrderlist=[];
							 $scope.getOrderlist = function(){
								var URI = BASE_URL + ROOT_URL + API + "/procurements/workOrderRequest/dropdowns";
								BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.status == 200){
										$scope.getOrderlist = response.data.contractType;
									}
									console.log("getOrderlist",$scope.getOrderlist);
								});
							}
							 $scope.getOrderlist ();
						 */
							 $scope.getTaxlist=function(){
								 $scope.selectedTaxList=[];
								 var URI = BASE_URL + ROOT_URL + "/api/globals/tax/org/"+$scope.organizationId;
									GenericService.serviceAction("GET", URI, {}).then(function(response) {
										if(response.status == 200){
										    $scope.selectedTaxList=response.data;
										    
										}
									});
							 }
							 $scope.getTaxlist();
							 
							 $scope.getAllOtherChargesList =function (){
									$scope.getAllOtherChargesList=[];
									var URI = BASE_URL+ROOT_URL+"/api/globals/other/charges/"+$scope.organizationId;
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                	if(response.data!=null && response.data.length!=0){
						                		$scope.getAllOtherChargesList = response.data;
						                		$scope.getAllOtherChargesDropDownList=[];
						                		angular.forEach($scope.getAllOtherChargesList,function(value, index){
						                			if(value.status==true){
						                				$scope.getAllOtherChargesDropDownList.push(value);
						                			}
						                		});
						                		
						                		
						                	}
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
									
									
								}
								$scope.getAllOtherChargesList();
							 
							 $scope.clearWorkorderListObj = function(){
								 $scope.initCommon();
								}
							 
							 
							 
							/* $scope. getProductCategoryList = function (id) {
						          
						            var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                	$scope.productCategoriesList = response.data;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
						        }*/
							 
							 $scope.getRejectionReasonList = function () {
						          
						            var URI = BASE_URL+ROOT_URL+"/api/global/reject/reasons"+ '/' + $scope.unitId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                	$scope.rejectionReasonList=[];
						                	$scope.rejectionReasonList = response.data;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
						        }
							 
							 $scope.getRejectionReasonList();
							 
							 
							 $scope.searchNewItem = function () {
								 $scope.searchNewItemList= [];
								 $scope.searchNewItemids=[];
									var data = {
											 	"assetTypeId": 2,
										        "assetCategoryId": $scope.assetObject.assetCategoryId,
										        "productCategoryId":$scope.assetObject.productCategoryId,
										        "storeId": $scope.assetObject.storeId
											};
									
									var URI = BASE_URL + ROOT_URL + "/api/global/items";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$scope.searchNewItemList = response.data;
										angular.forEach($scope.searchNewItemList,function(value, index){
											
											value.isSelected = false;
										})
										console.log("$scope.searchNewItemList",$scope.searchNewItemList);
									});
							}
							 
							 
							
							 
							 $scope.selectedTax="";
							 $scope.setTaxWorkorderItem =function(taxPer,workOrderItemDetail,selectedindex){
								 
								 workOrderItemDetail.itemTax=parseFloat (taxPer.taxPercentage );
								 workOrderItemDetail.itemTaxId=taxPer.id;
								 $scope.calculate(workOrderItemDetail,selectedindex,'TAX')
								}
							 
							 
							 $scope.searchWorkorderItem = function(searchtext){
							      if (searchtext.length < 3) return;
							      if($scope.StoreWorkOrder.storeId==undefined || $scope.StoreWorkOrder.storeId==''){
							    	  growl.warning("Please select store",
												{
													title : 'warning!'
												});
										return;
							      }
							      var data={
							    		  assetTypeId:2,
							    		  storeId: $scope.StoreWorkOrder.storeId,
							    		  itemName:searchtext,
							    	      itemCode:searchtext,
							    	      isConsignment : false // added by minesh for resolved HIS-8808 Sub Module: Consignment - Op Balance item not display
							      };
							      var URI = BASE_URL + ROOT_URL + API + "/global/items/getlistofitems";
							      return GenericServiceParamHeader.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.status == 200){
											return response.data;
										}
									
									});
							      
							    }							 

							 
								$scope.selectSearchItemName = function($item, $model, $label,elementindex){
								
									angular.forEach($scope.workOrderItemDetailsList,function(value, index){
										 if(elementindex== index){
											 value.itemId=$item.itemId;
											 value.itemName=$item.itemName;
										 }
									 })
								 }
								
								
								
								/*$scope.getNewWOElementForEdit=function(workorderRequestId){
										var data = {
												    "workorderRequestId" : workorderRequestId
												    
												};
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationRequestDetailsbyId";
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											if(response.data.status == "success"){
												$rootScope.stopLoader();
												console.log("complete object",response.data.object);
												if(response.data.object!=null){
													$state.go('addNewWorkOrderCreation',
															{
																resultworkOrderDetailsCommonObj: response.data.object
															});	
													
												}
												
												
											}
										});
									
									
								}*/
								$scope.totalTaxAmount=parseFloat(0);
								$scope.totalDiscAmount=parseFloat(0);
								$scope.totalNetAmount=parseFloat(0);
								$scope.roundoff=parseFloat(0);
								$scope.totalAmount=parseFloat(0);
								
								$scope.calculateFinalAmount =function(){
									$scope.totalTaxAmount=parseFloat(0);
									$scope.totalDiscAmount=parseFloat(0);
									$scope.totalNetAmount=parseFloat(0);
									$scope.roundoff=parseFloat(0);
									$scope.totalAmount=parseFloat(0);
									var percentagePaidForOtherAmount=0;
									angular.forEach($scope.workOrderItemDetailsList,function(value, index){
										var percentagePaid=0;
										var totalPercentage=0;
										angular.forEach(value.workOrderCreationStagedListData,function(value1, index){
											percentagePaid=percentagePaid+(parseFloat(value1.percentageAmountPaid));
											percentagePaid=percentagePaid.toFixed(2);
											percentagePaid=parseFloat(percentagePaid);
											totalPercentage=totalPercentage+parseFloat(value1.percentage);
										});
										if(percentagePaid==totalPercentage)
											{
											value.status="Completed";
											value.isSelected=false;
											}
									});
									angular.forEach($scope.workOrderItemDetailsList,function(value, index){
										value.isMultiStages=false;
										var percentagePay=0;
										var percentagePaid=0;
										value.itemMrp = parseFloat(
												parseFloat(value.approvedQty)
														* parseFloat(value.itemRate))
												.toFixed(2);
										if($scope.common.workOrderCompletion==false)
										{
										if(value.status=="Completed")
											{
											return false;
											}
										}else
											{
											value.isSelected=true;
											}
										if(value.isFromPopUpSave==undefined || value.isFromPopUpSave==false)
											{
										if(value.workOrderCreationStagedListData.length>1 && $scope.common.workOrderCompletion==false)
											{
											value.isMultiStages=true;
											if($scope.common.isFromEdit==false)
												{
												return false;
												}
											}
										else if(value.workOrderCreationStagedListData.length>1 && $scope.common.workOrderCompletion==true)
											{
											value.isMultiStages=true;
											}
											}
										if(value.workOrderCreationStagedListData.length==1)
										{
										value.isMultiStages=false;
										}
										angular.forEach(value.workOrderCreationStagedListData,function(value1, index){
											if(value1.isSelected==true)
												{
												percentagePay=percentagePay+parseFloat(value1.percentageAmountPay!=undefined && value1.percentageAmountPay!=null ? value1.percentageAmountPay:0);
												}
											else if($scope.common.isFromEdit==true)
   										 {		
												percentagePay=percentagePay+parseFloat(value1.percentageAmountPay!=undefined && value1.percentageAmountPay!=null ? value1.percentageAmountPay:0);
												if(value1.percentageAmountPay!=undefined && value1.percentageAmountPay!=null &&value1.percentageAmountPay>0 && value.workOrderCreationStagedListData.length>1)
													{
													value1.isSelected=true;
													}
   										 }
											percentagePaid=percentagePaid+parseFloat(value1.percentageAmountPaid!=undefined && value1.percentageAmountPaid!=null ? value1.percentageAmountPaid:0);
										})
										percentagePaidForOtherAmount=percentagePaidForOtherAmount+percentagePay;
										if(value.isFromPopUpSave==true)
											{
											percentagePay=percentagePay;
											}
										else if(percentagePay==0 && value.workOrderCreationStagedListData.length==1)
											{
											percentagePay=100-percentagePaid;
											}
										if(value.isSelected==true){
											
										
										var discamt=parseFloat(0);
										 var taxamt=parseFloat(0);
										if( value.itemDiscAmount!=undefined && value.itemDiscAmount!='' && value.itemDiscAmount!=0){
											discamt=parseFloat(discamt+value.itemDiscAmount);
										}
										if($scope.common.workOrderCompletion==false)
										{
										discamt=(discamt*percentagePay)/100;
										}
										if( value.itemTaxAmount!=undefined && value.itemTaxAmount!='' && value.itemTaxAmount!=0  ){
											value.itemTaxAmount = parseFloat(
													(parseFloat(value.itemMrp) * parseFloat(value.itemTax))
															/ parseFloat(100))
													.toFixed(2);
											taxamt=parseFloat(taxamt+value.itemTaxAmount);
										}
										if($scope.common.workOrderCompletion==false)
										{
										taxamt=(taxamt*percentagePay)/100;
										value.itemTaxAmount=taxamt;
										}
										if($scope.common.workOrderCompletion==false)
										{
//											if(percentagePay>0)
//												{
												value.itemNetAmount= parseFloat((parseFloat((value.itemMrp*percentagePay)/100)+ parseFloat( taxamt))-parseFloat(discamt) );
//												}
//											else
//												{
//												value.itemNetAmount= parseFloat((parseFloat(value.itemMrp )+ parseFloat( taxamt))-parseFloat(discamt) );
//												}
										}
										else
											{
											value.itemNetAmount= parseFloat((parseFloat(value.itemMrp )+ parseFloat( taxamt))-parseFloat(discamt) );
											}
										$scope.totalTaxAmount=parseFloat($scope.totalTaxAmount + taxamt);
										$scope.totalDiscAmount=parseFloat($scope.totalDiscAmount+discamt);
										
											$scope.totalNetAmount=parseFloat($scope.totalNetAmount+ value.itemNetAmount);
										
										}
										value.itemMrp=(value.itemMrp*percentagePay)/100;
									 })
									 percentagePaidForOtherAmount=percentagePaidForOtherAmount/$scope.workOrderItemDetailsList.length;
									 if($scope.otherChargeAmount==undefined){
										 $scope.otherChargeAmount=0;
									 }
									 if($scope.otherChargeAmount!=0){
										 if($scope.common.workOrderCompletion==false)
											{
										 $scope.otherChargeAmount=$scope.otherChargeAmount;
											}
										 $scope.totalAmount= parseFloat($scope.totalNetAmount)+ parseFloat($scope.totalDiscAmount) - parseFloat($scope.totalTaxAmount);
										 $scope.totalNetAmount=parseFloat($scope.totalNetAmount + $scope.otherChargeAmount).toFixed(2);
										           
										 var roundoff1=parseFloat($scope.totalNetAmount).toFixed(2);
										 var roundoff2=parseFloat($scope.totalNetAmount).toFixed();
										 var roundoffvalue=parseFloat(roundoff2 - roundoff2);
										 if(roundoffvalue < 0.50){
											 $scope.totalNetAmount =parseFloat(roundoff2);
										        $scope.roundoff = parseInt(roundoff2) - roundoff1;
										      }
										      if(roundoffvalue >= 0.50){
										    	  $scope.totalNetAmount = math.round(roundoff1);
										        $scope.roundoff =  $scope.totalNetAmount - roundoff1;
										      }
									 }
									 if($scope.otherChargeAmount==0){
										 $scope.totalAmount= parseFloat($scope.totalNetAmount)+ parseFloat($scope.totalDiscAmount) - parseFloat($scope.totalTaxAmount);
										 var roundoff1=parseFloat($scope.totalNetAmount).toFixed(2);
										 var roundoff2=parseFloat($scope.totalNetAmount).toFixed();
										 var roundoffvalue=parseFloat(roundoff2 - roundoff2);
										 if(roundoffvalue < 0.50){
											 $scope.totalNetAmount =parseFloat(roundoff2);
										        $scope.roundoff = parseInt(roundoff2) - roundoff1;
										      }
										      if(roundoffvalue >= 0.50){
										    	  $scope.totalNetAmount = math.round(roundoff1);
										          $scope.roundoff =  $scope.totalNetAmount - roundoff1;
										      }
									 }
									
								}
								
								
								$scope.getNewWOElementForApprovalEdit=function(workorderRequestId){
									var data = {
											    "workorderRequestId" : workorderRequestId
											    
											};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationApprovalDetailsbyId";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											console.log("complete object",response.data.object);
											if(response.data.object!=null){
												$state.go('deatilsWorkorderApproval',
														{
															resultworkOrApprovalderDetailsCommonObj: response.data.object
														});	
												
											}
											
											
										}
									});
								
								
							}
								
								$scope.AddOtherchargeSave =function(){
									 $scope.otherChargeAmount=parseFloat(0);
									 var countTypeId=0;
									 var countotheramount=0; 
									 $scope.AddOtherSavechargesList=[];
									 angular.forEach($scope.AddOtherchargesList,function(value, index){
										 if(value.otherchargeTypeId!=undefined && value.otherchargeTypeId!=''  ){
											 if(value.otherChargeAmount!=undefined && value.otherChargeAmount!=''  && value.otherChargeAmount!=0 ){
												 $scope.AddOtherSavechargesList.push(value);
												 $scope.otherChargeAmount=parseFloat($scope.otherChargeAmount)+parseFloat(value.otherChargeAmount);
											 }else{
												 countotheramount++;
											 }
											 
										 }else{
											 countTypeId++;
										 }
									 })
									 
									if(countTypeId>0){
										growl.warning("Please Select Valid Charge Type .",
												{
													title : 'warning!'
												});
										return;
									} 
									 
									 if(countotheramount>0){
											growl.warning("Please enter Valid Charge Amount .",
													{
														title : 'warning!'
													});
											return;
										} 
									
//										 $("#qtyaccepatReject").modal('hide');
										 $scope.calculateFinalAmount();
										 
									
								}
								
								
								$scope.showRejectionPopup=function(workOrderItemDetail,index){
									if(workOrderItemDetail.isRejectedQty==true){
										if(workOrderItemDetail.itemRejectionObject.workorderRequestDetialsId==undefined){
											$scope.itemRejectionObject={};
//											$("#qtyaccepatReject").modal("show");
											 $scope.itemRejectionObject.workorderRequestDetialsId=workOrderItemDetail.workorderRequestDetialsId;
											 $scope.itemRejectionObject.cancelReasonId='';
											 $scope.itemRejectionObject.rejectedQty=workOrderItemDetail.rejectedQty;
											 $scope.itemRejectionObject.itemQuantity=workOrderItemDetail.itemQuantity;
											 $scope.itemRejectionObject.rejectNote="";
										}else{
											$scope.itemRejectionObject={};
//											$("#qtyaccepatReject").modal("show");
											 $scope.itemRejectionObject.workorderRequestDetialsId=workOrderItemDetail.workorderRequestDetialsId;
											 $scope.itemRejectionObject.cancelReasonId=workOrderItemDetail.itemRejectionObject.cancelReasonId;
											 $scope.itemRejectionObject.rejectedQty=workOrderItemDetail.itemRejectionObject.rejectedQty;
											 $scope.itemRejectionObject.itemQuantity=workOrderItemDetail.itemRejectionObject.itemQuantity;
											 $scope.itemRejectionObject.rejectNote=workOrderItemDetail.itemRejectionObject.rejectNote;
											
										} 
									}
									
									if(workOrderItemDetail.isRejectedQty==false){
										workOrderItemDetail.approvedQty=workOrderItemDetail.itemQuantity;
										workOrderItemDetail.rejectedQty=0;
										workOrderItemDetail.itemRejectionObject={};
										$scope.cancelRejectionPopup();
										$scope.calculate(workOrderItemDetail,index,'');
									}
									
								}
								
								
								$scope.openDatePicker2 = function() { 
									$scope.datepicker2.opened = true;
								};
								$scope.datepicker2 = {
										opened : false
									};
                      $scope.calculate=function(workOrderItemDetail,index,blurpoint){
								                    	if (workOrderItemDetail.isSelected == true) {
									if (workOrderItemDetail.approvedQty == 0) {
										// workOrderItemDetail.isSelected=false;
										$scope.selectedRow(workOrderItemDetail,index)
									} else {

										if (workOrderItemDetail.approvedQty != undefined
												&& workOrderItemDetail.approvedQty != ""
												&& workOrderItemDetail.itemRate != undefined
												&& workOrderItemDetail.itemRate != "") {
											if (workOrderItemDetail.approvedQty <= workOrderItemDetail.itemQuantity) {

												workOrderItemDetail.rejectedQty = workOrderItemDetail.itemQuantity
														- workOrderItemDetail.approvedQty;
												if (workOrderItemDetail.rejectedQty > 0) {
													workOrderItemDetail.isRejectedQty = true;
													if (workOrderItemDetail.itemRejectionObject.workorderRequestDetialsId == undefined) {
														$scope.itemRejectionObject = {};
//														$("#qtyaccepatReject")
//																.modal("show");
														$scope.itemRejectionObject.workorderRequestDetialsId = workOrderItemDetail.workorderRequestDetialsId;
														$scope.itemRejectionObject.cancelReasonId = '';
														$scope.itemRejectionObject.rejectedQty = workOrderItemDetail.rejectedQty;
														$scope.itemRejectionObject.itemQuantity = workOrderItemDetail.itemQuantity;
														$scope.itemRejectionObject.rejectNote = "";
													} else {
														$scope.itemRejectionObject = {};
														// $("#qtyaccepatReject")
														// 		.modal("show");
														$scope.itemRejectionObject.workorderRequestDetialsId = workOrderItemDetail.workorderRequestDetialsId;
														$scope.itemRejectionObject.cancelReasonId = workOrderItemDetail.itemRejectionObject.cancelReasonId;
														$scope.itemRejectionObject.rejectedQty = workOrderItemDetail.rejectedQty;
														$scope.itemRejectionObject.itemQuantity = workOrderItemDetail.itemQuantity;
														$scope.itemRejectionObject.rejectNote = workOrderItemDetail.itemRejectionObject.rejectNote;

													}

												}
												if (workOrderItemDetail.rejectedQty == 0) {
													workOrderItemDetail.isRejectedQty = false;
												}

											} else {
												growl
														.warning(
																"Please enter Valid Approved Quantity .",
																{
																	title : 'warning!'
																});
												workOrderItemDetail.approvedQty = workOrderItemDetail.itemQuantity;
												workOrderItemDetail.rejectedQty = 0;
												workOrderItemDetail.isRejectedQty = false;
											}

											workOrderItemDetail.itemMrp = parseFloat(
													parseFloat(workOrderItemDetail.approvedQty)
															* parseFloat(workOrderItemDetail.itemRate))
													.toFixed(2);

											/*
											 * change dynamically tax and
											 * percentage also
											 * if(workOrderItemDetail.itemDiscAmount!=undefined &&
											 * workOrderItemDetail.itemDiscAmount!="" &&
											 * blurpoint=='DA' ){
											 * 
											 * if(parseFloat(workOrderItemDetail.itemDiscAmount) >
											 * workOrderItemDetail.itemMrp ||
											 * parseFloat(workOrderItemDetail.itemDiscAmount
											 * )==0){ growl.warning("Please
											 * Enter Valid Discount Amount .", {
											 * title : 'warning!' });
											 * workOrderItemDetail.itemDiscPercentage=0;
											 * workOrderItemDetail.itemDiscAmount=0;
											 * }else{
											 * workOrderItemDetail.itemDiscPercentage=
											 * parseFloat(
											 * parseFloat(workOrderItemDetail.itemDiscAmount)
											 * /(parseFloat(workOrderItemDetail.itemMrp) )*
											 * parseFloat(100)); }
											 *  }
											 * 
											 * if(workOrderItemDetail.itemDiscPercentage!=undefined &&
											 * workOrderItemDetail.itemDiscPercentage!="" &&
											 * blurpoint=='DP'){
											 * if(parseFloat(workOrderItemDetail.itemDiscPercentage) >
											 * 100 ||
											 * parseFloat(workOrderItemDetail.itemDiscPercentage
											 * )==0){ growl.warning("Please
											 * Enter Valid Discount Percentage
											 * .", { title : 'warning!' });
											 * workOrderItemDetail.itemDiscPercentage=0;
											 * workOrderItemDetail.itemDiscAmount=0;
											 * 
											 * }else{
											 * workOrderItemDetail.itemDiscAmount=
											 * parseFloat(
											 * parseFloat(workOrderItemDetail.itemMrp)
											 * *(parseFloat(workOrderItemDetail.itemDiscPercentage) )/
											 * parseFloat(100)); }
											 *  }
											 * 
											 * if(workOrderItemDetail.itemTax!=undefined &&
											 * workOrderItemDetail.itemTax!="" &&
											 * blurpoint=='TAX'){
											 * 
											 * if(workOrderItemDetail.itemDiscAmount==0){
											 * workOrderItemDetail.itemTaxAmount =
											 * parseFloat((parseFloat(workOrderItemDetail.itemMrp) *
											 * parseFloat(workOrderItemDetail.itemTax)
											 * )/parseFloat(100)); }else{
											 * 
											 * workOrderItemDetail.itemTaxAmount =
											 * parseFloat((parseFloat(workOrderItemDetail.itemMrp -
											 * workOrderItemDetail.itemDiscAmount) *
											 * parseFloat(workOrderItemDetail.itemTax)
											 * )/parseFloat(100)); }
											 *  }
											 */

											/*
											 * if(workOrderItemDetail.itemDiscAmount!=undefined &&
											 * workOrderItemDetail.itemDiscAmount!="" ){
											 * 
											 * if(parseFloat(workOrderItemDetail.itemDiscAmount) >
											 * workOrderItemDetail.itemMrp ||
											 * parseFloat(workOrderItemDetail.itemDiscAmount
											 * )==0){ growl.warning("Please
											 * Enter Valid Discount Amount .", {
											 * title : 'warning!' });
											 * workOrderItemDetail.itemDiscPercentage=0;
											 * workOrderItemDetail.itemDiscAmount=0;
											 * }else{
											 * workOrderItemDetail.itemDiscPercentage=
											 * parseFloat(
											 * parseFloat(workOrderItemDetail.itemDiscAmount)
											 * /(parseFloat(workOrderItemDetail.itemMrp) )*
											 * parseFloat(100)); }
											 *  }
											 */

											if (workOrderItemDetail.itemDiscPercentage != undefined
													&& workOrderItemDetail.itemDiscPercentage != "") {
												if (parseFloat(workOrderItemDetail.itemDiscPercentage) > 100
														|| parseFloat(workOrderItemDetail.itemDiscPercentage) == 0) {
													growl
															.warning(
																	"Please Enter Valid Discount Percentage  .",
																	{
																		title : 'warning!'
																	});
													workOrderItemDetail.itemDiscPercentage = 0;
													workOrderItemDetail.itemDiscAmount = 0;

												} else {
													workOrderItemDetail.itemDiscAmount = parseFloat(
															parseFloat(workOrderItemDetail.itemMrp)
																	* (parseFloat(workOrderItemDetail.itemDiscPercentage))
																	/ parseFloat(100))
															.toFixed(2);
												}

											}

											if (workOrderItemDetail.itemTax != undefined
													&& workOrderItemDetail.itemTax != "") {
												// for tax already given
												if (workOrderItemDetail.itemDiscAmount == 0) {
													workOrderItemDetail.itemTaxAmount = parseFloat(
															(parseFloat(workOrderItemDetail.itemMrp) * parseFloat(workOrderItemDetail.itemTax))
																	/ parseFloat(100))
															.toFixed(2);
												} else {

													workOrderItemDetail.itemTaxAmount = parseFloat(
															(parseFloat(workOrderItemDetail.itemMrp
																	- workOrderItemDetail.itemDiscAmount) * parseFloat(workOrderItemDetail.itemTax))
																	/ parseFloat(100))
															.toFixed(2);
												}

											}

											if (workOrderItemDetail.itemTax == 0
													&& blurpoint == 'TAX') {

												workOrderItemDetail.itemTaxAmount = parseFloat(0);
											}

										}

										$scope.calculateFinalAmount();
									}

								}

							}
                      $scope.validateOtherCharges = function()
                      {
                    	  if($scope.otherChargeAmount>$scope.initialOtherCharges)
                    		  {
                    		  growl.error("Please Enter Valid Other Charges");
                    		  $scope.otherChargeAmount=parseFloat($scope.initialOtherCharges);
                    		  $scope.calculateFinalAmount();
                    		  return false;
                    		  }
                    	  $scope.otherChargeAmount=parseFloat($scope.otherChargeAmount);
                    	  $scope.calculateFinalAmount();
                      }
                      $scope.isAtleastOneSelected
                      $scope.calculateAmount =function(item)
                      {
                    	  if(!item.isSelected)
                    		  {
                    		  growl.error("Please Select Stage");
                    		  item.amountPay=null;
                    			return false;
                    		  }
                    	  else{
							  $scope.isAtleastOneSelected=1;
						  }
                      	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                      		modal.percentageAmountPay=(parseFloat(modal.amountPay!=undefined && modal.amountPay!=null && modal.amountPay!="" ? modal.amountPay:0)*100)/modal.amount;
                      		modal.percentageAmountPay=modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null && modal.percentageAmountPay!="" ? modal.percentageAmountPay:0;
                      		modal.percentageAmountPay=parseFloat(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null && modal.percentageAmountPay!="" ? modal.percentageAmountPay:0);
                      		$scope.calculatePercentage(modal);
                      		if(modal.isSelected==true)
                      			{
                      			$scope.isAtleastOneSelected=1;
                      			}
                      		
                      	});
                      	}
                      
                      $scope.calculatePercentage =function(item)
                      {
                    	  if(!item.isSelected && ($scope.isAtleastOneSelected==undefined || $scope.isAtleastOneSelected==null))
                    		  {
                    		  growl.error("Please Select Stage");
                    			return false;
                    		  }
                      	var percentageSum=0;
                      	var totalAmountPaid=0;
                      	var totalPer=0;
                      	var growlCount=0;
                      	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                      		
                      		if(modal.isSelected==true && item.stagedWorkorderId==modal.stagedWorkorderId){
                      		totalAmountPaid=totalAmountPaid+parseFloat(modal.percentageAmountPaid!=undefined && modal.percentageAmountPaid!=null ? modal.percentageAmountPaid:0);
                      		percentageSum=percentageSum+parseFloat(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null && modal.percentageAmountPay!="" ? modal.percentageAmountPay:0);
                      		totalPer=totalPer+modal.percentage;
                      		modal.amountPay=((modal.amount)*parseFloat(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null && modal.percentageAmountPay!="" ? modal.percentageAmountPay:0))/100;
                      		modal.amountPay=modal.amountPay.toFixed(2);
                      		modal.amountPay=parseFloat(modal.amountPay);
                      		if((modal.percentage-parseFloat(modal.percentageAmountPaid!=undefined && modal.percentageAmountPaid!=null ? modal.percentageAmountPaid :0))<parseFloat(modal.percentageAmountPay))
                      			{
                      			modal.percentageAmountPay=modal.percentage-(modal.percentageAmountPaid!=undefined && modal.percentageAmountPaid!=null ? modal.percentageAmountPaid:0)
                      			modal.amountPay=((modal.amount)*parseFloat(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null && modal.percentageAmountPay!="" ? modal.percentageAmountPay:0))/100;
                      			modal.amountPay=modal.amountPay.toFixed(2);
                          		modal.amountPay=parseFloat(modal.amountPay);
                      			growl.error("Percentage Should Not Be Greater Than Total Percentage ");
                          		return false;
                      			}
                      		 modal.percentageAmountPayNew = parseFloat(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null && modal.percentageAmountPay!="" ? modal.percentageAmountPay:0);
                      		modal.percentageAmountPay = parseFloat(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null && modal.percentageAmountPay!="" ? modal.percentageAmountPay:0).toFixed(2);
                      	}
                      	});
                      	if((totalAmountPaid+percentageSum)>100)
                      		{
                      		growl.error("Sum of percentage can not be greater than 100% ");
                      		return false;
                      		}
                      }
                      $scope.selectItem=function(item)
                      {
                    	  if(item.isSelected)
                    		  {
                    		  item.percentageAmountPay=parseFloat(item.percentage)-parseFloat(item.percentageAmountPaid!=undefined && item.percentageAmountPaid!=null ? item.percentageAmountPaid:0);
                    		  item.percentageAmountPay=item.percentageAmountPay.toFixed(2);
                    		  item.percentageAmountPay=parseFloat(item.percentageAmountPay);
                    		  var percentageToGetAmount=0;
                    		  $scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].workOrderCreationStagedListData.forEach(function (modal, index) {
                    			  percentageToGetAmount=percentageToGetAmount+modal.percentage;
                    			  });
                    		  if(item.amount==undefined || item.amount==null)
                    			  {
                    			  var tAmount=($scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].itemMrp*percentageToGetAmount)/100;
                            	  var tTax=($scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].itemTaxAmount*percentageToGetAmount)/100;
                            	  item.amount=tAmount+tTax;
                            	  if(item.percentageAmountPay!=undefined && item.percentageAmountPay!=null && item.percentageAmountPay>0)
                            		  {
                            		  if(item.amountPay==undefined || item.amountPay==null || item.amountPay==0)
                            		  {
                            			  item.amountPay=(item.amount*item.percentageAmountPay)/100;
                            		  }
                            		  }
                            	  else
                				  {
                            		  item.amountPay=null;
                				  }
                    			  }
                    		  else
                    			  {
                    			  var tAmount=($scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].itemMrp*percentageToGetAmount)/100;
                            	  var tTax=($scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].itemTaxAmount*percentageToGetAmount)/100;
                            	  item.amount=tAmount+tTax;
                    			  if(item.percentageAmountPay!=undefined && item.percentageAmountPay!=null && item.percentageAmountPay>0)
                        		  {
                    				  if(item.amountPay==undefined || item.amountPay==null || item.amountPay==0)
                            		  {
                    					  item.amountPay=(item.amount*parseFloat(item.percentageAmountPay))/100;
                            		  }
                        		  }
                    			  else
                    				  {
                    				  item.amountPay=null;
                    				  }
                    			  }
                    		  if(item.percentageAmountPaid!=undefined && item.percentageAmountPaid!=null && item.percentageAmountPaid>0)
                    		  {
                    			  item.amountPaid=(item.amount*item.percentageAmountPaid)/100;
                    		  }
                    		  else
                    			  {
                    			  item.amountPaid=null;
                    			  }
                    		  item.percentageAmountPayNew = parseFloat(item.percentageAmountPay);
                    		  }
                    	  else
                    		  {
                    		  item.percentageAmountPay=0;
                    		  $scope.workOrderItemDetailsList.forEach(function (modal, index) {
                    			  modal.workOrderCreationStagedListData.forEach(function (modal1, index) {
                    			  if(modal1.stagedWorkorderId==item.stagedWorkorderId)
                    				  {
                    				  modal1.percentageAmountPay=item.percentageAmountPay;
                    				  modal1.isSelected=item.isSelected;
                    				  }
                    			  });
                    		  })
                    		  item.amountPay=null;
                    		  }
                    	  item.amountPay=item.amountPay.toFixed(2);
                    	  item.amountPay=parseFloat(item.amountPay);
                      }
                      $scope.closeStagedPopUp =function()
                      {
                    	  $scope.workOrderItemDetailsList.forEach(function (modal, index) {
                    		  $scope.calculate(modal,index,"");
                    		  if(modal.isMultiStages == true){
								  modal.workOrderCreationStagedListData.forEach(function(modalData) {
									  delete modalData.amount;
									  delete modalData.amountMinus;
									  delete modalData.amountPaid;
								  });
							  }
                    	  });
                      	if($scope.initailStagedData.length>0)
                      		{
                      		$scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].workOrderCreationStagedListData=$scope.initailStagedData;
                      		}
                      	else
                      		{
                      		$scope.workOrderCreationStagedListData=[];
                      		}
                      }
                      $scope.addStage = function (date) {
                      	var percentageSum=0;
                      	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                      		percentageSum=percentageSum+parseFloat(modal.percentage);
                      	});
                      	if(percentageSum>=100)
                      		{
                      		growl.error("You can not add new item as total percentage can not be greater than 100% ");
                      		return false;
                      		}
                      	$scope.workOrderCreationStagedListData.push({ stagedWorkorderId:null,stageName:'', percentage: (parseInt(100)-percentageSum).toFixed(2) });
                        }
                      $scope.lessStage = function (item) {
                          var index = $scope.workOrderCreationStagedListData.indexOf(item);
                          $scope.workOrderCreationStagedListData.splice(index, 1);
                          var percentageSum=0;
                      	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                      		percentageSum=percentageSum+parseFloat(modal.percentage);
                      	});
                      	var diff=100-percentageSum;
                      	$scope.workOrderCreationStagedListData[$scope.workOrderCreationStagedListData.length-1].percentage=parseFloat($scope.workOrderCreationStagedListData[$scope.workOrderCreationStagedListData.length-1].percentage)+diff;
                        }
                     $scope.stagedWorkOrderInfoOnClick = function (workOrder, index) {
                          $scope.WorkOrderListIndex = index;
                          $scope.initailStagedData=workOrder.workOrderCreationStagedListData;
                          if(workOrder.workOrderCreationStagedListData!=undefined && workOrder.workOrderCreationStagedListData!=null && workOrder.workOrderCreationStagedListData.length>0)
                          	{
                        	  workOrder.itemMrp = parseFloat(
    									parseFloat(workOrder.approvedQty)
    											* parseFloat(workOrder.itemRate))
    									.toFixed(2);
                            workOrder.itemTaxAmount = parseFloat(
  									(parseFloat(workOrder.itemMrp) * parseFloat(workOrder.itemTax!=undefined && workOrder.itemTax!=null && workOrder.itemTax!="" ? workOrder.itemTax:0 ))
  											/ parseFloat(100))
  									.toFixed(2);
                          	$scope.workOrderCreationStagedListData=[];
                          	$scope.generatedAmount=0;
                          	$scope.balanceAmount=0;
                          	var generatedPercentage=0;
                          	var balancePercentage=0;
                          	var percentageToGetAmount=0;
                          	angular.forEach(workOrder.workOrderCreationStagedListData, function (list) {
                      		  percentageToGetAmount=percentageToGetAmount+list.percentage;
                      	  });
                          angular.forEach(workOrder.workOrderCreationStagedListData, function (list) {
                        	  if($scope.common.isFromEdit==false && (workOrder.isFromPopUpSave==false || workOrder.isFromPopUpSave==undefined ))
								{
                        		  list.percentageAmountPay=0;
								}
                        	  if(parseFloat(list.percentage)==parseFloat(list.percentageAmountPaid))
                        		  {
                        		  list.percentageAmountPay=0;
                        		  }
                        	  generatedPercentage=generatedPercentage+list.percentageAmountPaid;
                        	  balancePercentage=balancePercentage+list.percentage;
                        	  $scope.initialPercentageToPay=0;
                        	  if(workOrder.workOrderCreationStagedListData.length>1)
                        		  {
                        		  $scope.isSelected=false;
                        		  }
                        	  else
                        		  {
                        		  $scope.isSelected=true;
                        		  $scope.initialPercentageToPay=list.percentage-(list.percentageAmountPaid!=undefined && list.percentageAmountPaid!=null ? list.percentageAmountPaid:0);
                        		  }
//                        	  if(list.percentageAmountPaid<100 && (list.percentageAmountPay!=undefined && list.percentageAmountPay!=null && list.percentageAmountPay>0) && $scope.common.isFromEdit==true)
//                        		  {
//                        		  list.percentageAmountPay=list.percentage-(list.percentageAmountPaid!=undefined && list.percentageAmountPaid!=null ? list.percentageAmountPaid:0)
//                        		  }
                        	  if(list.percentageAmountPay>0)
                        		  {
                        		  list.isSelected=true;
                        		  }
                        	  if(list.isSelected==true || $scope.isSelected==true)
                    		  {
                    		  if(list.amount==undefined || list.amount==null)
                    			  {
                    			  var tAmount=(workOrder.itemMrp*percentageToGetAmount)/100;
                            	  var tTax=(workOrder.itemTaxAmount*percentageToGetAmount)/100;
                            	  list.amount=tAmount+tTax;
                            	  list.amountMinus=(list.amount*list.percentage)/100;
                            	  if(list.percentageAmountPay!=undefined && list.percentageAmountPay!=null && list.percentageAmountPay>0)
                            		  {
                            		  if(list.amountPay==undefined || list.amountPay==null)
                            		  {
                            		  list.amountPay=(list.amount*list.percentageAmountPay)/100;
                            		  }
                            		  }
                            	  else
                				  {
                				  list.amountPay=null;
                				  }
                    			  }
                    		  else
                    			  {
                    			  var tAmount=(workOrder.itemMrp*percentageToGetAmount)/100;
                            	  var tTax=(workOrder.itemTaxAmount*percentageToGetAmount)/100;
                            	  list.amount=tAmount+tTax;
                    			  if(list.percentageAmountPay!=undefined && list.percentageAmountPay!=null && list.percentageAmountPay>0)
                        		  {
                    				  if(list.amountPay==undefined || list.amountPay==null)
                            		  {
                    			  list.amountPay=(list.amount*parseFloat(list.percentageAmountPay))/100;
                            		  }
                        		  }
                    			  else
                    				  {
                    				  list.amountPay=null;
                    				  }
                    			  }
                    		  if(list.percentageAmountPaid!=undefined && list.percentageAmountPaid!=null && list.percentageAmountPaid>0)
                    		  {
                    			  list.amountPaid=(list.amount*list.percentageAmountPaid)/100;
                    		  }
                    		  else
                    			  {
                    			  list.amountPaid=null;
                    			  }
                    		  }
                        	  else
                        		  {
                        		  var tAmount=(workOrder.itemMrp*percentageToGetAmount)/100;
                            	  var tTax=(workOrder.itemTaxAmount*percentageToGetAmount)/100;
                        	      list.amount=tAmount+tTax;
                            	  list.amountMinus=(list.amount*list.percentage)/100;
                        		  if(list.percentageAmountPaid!=undefined && list.percentageAmountPaid!=null && list.percentageAmountPaid>0)
                        		  {
                        			  list.amountPaid=(list.amount*list.percentageAmountPaid)/100;
                        		  }
                        		  else
                        			  {
                        			  list.amountPaid=null;
                        			  }
                        		  }
                        	  if(list.isSelected==true)
                        		  {
                        		  list.percentageAmountPayNew=list.percentageAmountPay;
                        		  list.percentageAmountPay=parseFloat(list.percentageAmountPay);
                        		  list.percentageAmountPay=list.percentageAmountPay.toFixed(2);
                        		  }
                        	  if(workOrder.workOrderCreationStagedListData.length==1)
                        		  {
                        		  if(list.isSelected==false)
                        			  {
                        			  list.isSelected=true;
                        			  }
                        		  if($scope.isSelected==false)
                    			  {
                        			  $scope.isSelected=true;
                    			  }
                        		  }
                          	$scope.workOrderCreationStagedListData.push({
                          		stagedWorkorderId:list.stagedWorkorderId,
                          		workorderRequestDetailsId:list.workorderRequestDetailsId,
                          		workOrderCompletionStagedDetailId:list.workOrderCompletionStagedDetailId,
                          		workorderRequestDetailsId:list.workorderRequestDetailsId,
                          		stageName:list.stageName,
                          		percentage: list.percentage,
                          		percentageAmountPaid: list.percentageAmountPaid,
                          		percentageAmountPay: list.percentageAmountPay!=undefined && list.percentageAmountPay!=null && list.percentageAmountPay>0 ? list.percentageAmountPay :($scope.initialPercentageToPay!=undefined && $scope.initialPercentageToPay!=null ? $scope.initialPercentageToPay:0),
                          		isSelected:list.isSelected!=undefined && list.isSelected!=null ? list.isSelected : $scope.isSelected,
                          		amount:list.amount,
                          		amountPaid:list.amountPaid,
                          		amountPay:list.amountPay,
                          		amountMinus:list.amountMinus,
                          		percentageAmountPayNew:list.isSelected==true ? list.percentageAmountPayNew:null,
                          		amountPay:list.amountPay!=undefined && list.amountPay!=null ? list.amountPay :(workOrder.workOrderCreationStagedListData.length==1 && list.amount!=undefined && list.amount!=null ? list.amount-(list.amountPaid!=undefined && list.amountPaid!=null ? list.amountPaid:0):0)
                              });
                            });
                          var genTax=(parseInt(workOrder.itemTaxAmount)*generatedPercentage)/100;
                          var balTax=(parseInt(workOrder.itemTaxAmount)*(balancePercentage-generatedPercentage))/100;
                          $scope.generatedAmount=((parseInt(workOrder.itemMrp)*(generatedPercentage))/100)+genTax;
                          $scope.balanceAmount=((parseInt(workOrder.itemMrp)*(balancePercentage-generatedPercentage))/100)+balTax;
                              }
                          else
                          	{
                          	var percentageSum=0;
                          	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                          		percentageSum=percentageSum+parseFloat(modal.percentage);
                          	});
                          	if(percentageSum<100)
                          		{
                          	$scope.workOrderCreationStagedListData.push({
                          		stagedWorkorderId:null,
                          		workorderRequestDetailsId:null,
                          		workOrderCompletionStagedDetailId:null,
                          		workorderRequestDetailsId:null,
                          		stageName: "",
                          		percentage: 100-percentageSum,
                          		percentageAmountPaid: 0,
                          		percentageAmountPay: 0,
                              });
                          		}
                          	}
                          
                      }
                     $scope.stagedWorkOrderModalOnClick = function (item) {
                  	   $scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].workOrderCreationStagedListData = [];
                  	      var modal_list = $scope.workOrderCreationStagedListData;
                  	      var stageNameCount=0;
                  	      var selectionCount=0;
                  	      var atleastStageSelectionCount=0;
                  	      modal_list.forEach(function (modal, index) {
                  	    	  if(modal.percentageAmountPaid!=modal.percentage)
                  	    		  {
		                  	        if(modal.percentageAmountPay==undefined || modal.percentageAmountPay==null || modal.percentageAmountPay==0)
		                  	        {
		                  	        	stageNameCount++;
		                  	        }
                  	    		  }
                  	      if(modal.isSelected)
                	        {
                  	    	selectionCount++;
                  	    	atleastStageSelectionCount++;
                	        }
                  	      });
                  	      if(selectionCount==0)
                  	    	  {
                  	    	growl.error("Please Select Atleast One Stage");
          	        	  return false;
                  	    	  }
                  	      if(stageNameCount>0)
                  	    	  {
                  	    	  if(atleastStageSelectionCount==0)
                  	    		  {
                  	    	  growl.error("Please enter all Required fields");
                	        	  return false;
                  	    	  }
                  	    	  }
                        	var percentageSum=0;
                          	var totalAmountPaid=0;
                          	var totalPer=0;
                          	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                          		totalAmountPaid=totalAmountPaid+parseFloat(modal.percentageAmountPaid!=undefined && modal.percentageAmountPaid!=null ? modal.percentageAmountPaid:0);
                          		percentageSum=percentageSum+parseFloat(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null ? modal.percentageAmountPay:0);
                          		totalPer=totalPer+modal.percentage;
                          	});
                          	if((totalAmountPaid+percentageSum).toFixed(2)>100)
                          		{
                          		growl.error("Sum of percentage can not be greater than 100% ");
                          		return false;
                          		}
                  	      modal_list.forEach(function (modal, index) {
                  	    	  $scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].workOrderCreationStagedListData.push({
                  	        	stagedWorkorderId:modal.stagedWorkorderId!=null ? modal.stagedWorkorderId :null ,
                  	        			stageName: modal.stageName,
                  	        			workorderRequestDetailsId:modal.workorderRequestDetailsId,
                  	        			workOrderCompletionStagedDetailId:modal.workOrderCompletionStagedDetailId,
                  	        			workorderRequestDetailsId:modal.workorderRequestDetailsId,
                  	        			percentage: modal.percentage,
                  	        			percentageAmountPaid: modal.percentageAmountPaid,
                                  		percentageAmountPay: modal.isSelected==true ? (modal.percentageAmountPayNew!=undefined && modal.percentageAmountPayNew!=null ? modal.percentageAmountPayNew:(modal.percentageAmountPay!=undefined && modal.percentageAmountPay!=null ? modal.percentageAmountPay:null)):null,
                                  		isSelected:modal.isSelected
                  	        });
                  	      });
                  	    $scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].isFromPopUpSave=true;
                  	    $scope.calculate($scope.workOrderItemDetailsList[$scope.WorkOrderListIndex],$scope.WorkOrderListIndex,"");
                  	      $scope.workOrderCreationStagedListData=[];
                  	      $('#staged_Workorder_Creation').modal('hide');
                  	    }
					$scope.SelectAllItems = function () {
						if ($scope.selectAllRows) {
							$scope.workOrderItemDetailsList.forEach(function (item) {
								item.isSelected = true;
							});
						} else {
							$scope.workOrderItemDetailsList.forEach(function (item) {
								item.isSelected = false;
							});
						}
					}
                      
                      
                      $scope.selectedRow=function(workOrderItemDetail,selectedindex){
                    	 
                    	 if( workOrderItemDetail.isSelected==true){
                    		 workOrderItemDetail.approvedQty=workOrderItemDetail.itemQuantity;
                    		 workOrderItemDetail.rejectedQty=0;
                    		 $scope.calculate(workOrderItemDetail,selectedindex,"");
                    	 }
                    	 
                       if( workOrderItemDetail.isSelected==false){
                    		    workOrderItemDetail.approvedQty=0 ; 
								 workOrderItemDetail.rejectedQty=0;
								 workOrderItemDetail.itemMrp=0; 
								 workOrderItemDetail.itemDiscAmount=0;
								 workOrderItemDetail.itemTaxAmount=0 ;
							     workOrderItemDetail.itemNetAmount=0 ;
							     workOrderItemDetail.approvalDetailRemark="";
							     workOrderItemDetail.isRejectedQty=false;
							     $scope.calculateFinalAmount();
								 
                    	 }

							if ($scope.workOrderItemDetailsList.every($scope.isSelected)) {
								$scope.selectAllRows = true;
							} else {
								$scope.selectAllRows = false;
							}
                      }

					$scope.isSelected = function (item) {
						return item.isSelected;
					}
                      
                      
                      $scope.saveApprovalData=function(){
                    	  $scope.workOrderItemDetailsFinalList=[];
                    	  var selectedCount=0;
                    	  var actualCount=0;
                    	  if($scope.common.invoiceDate==undefined || $scope.common.invoiceDate==null || $scope.common.invioceNumber==undefined || $scope.common.invioceNumber==null)
                   		 {
                   		angular.forEach($scope.workOrderItemDetailsList,function(value, index){
                   			if(value.workOrderCreationStagedListData.length>1)
                   				{
                   				value.isMultiStages=true;
                   				}
                   			else
                   				{
                   				value.isMultiStages=false;
                   				}
                   		});
                   		growl.error("Please Enter Valid Invoice Date And Invoice Number");
                   		 return false;
                   		 }
                    	  var totalPaid=0;
                    	  var totalPercentage=0;
                    	  angular.forEach($scope.workOrderItemDetailsList,function(value, index){
                      		 if(value.isSelected==true){
                      			if(value.status!="Completed")
                      				{
                      				
                      			$scope.workOrderItemDetailsFinalList.push(value);
                      				}
                      		 }
                      		 var stagedDataCount=0;
                      		angular.forEach(value.workOrderCreationStagedListData,function(value1, index){
                      			totalPercentage=totalPercentage+value1.percentage;
                      			totalPaid=totalPaid+(value1.percentageAmountPaid!=undefined && value1.percentageAmountPaid!=null ? value1.percentageAmountPaid:0)
                      			+(value1.percentageAmountPay!=undefined && value1.percentageAmountPay!=null ? value1.percentageAmountPay:0)
                      			if(value1.isSelected==true)
                      				{
                      				stagedDataCount++;
                      				}
                      			if(value.status!="Completed")
                  				{
                      			actualCount++;
                  				}
                      			if(value.isFromPopUpSave==false || value.isFromPopUpSave==undefined || value.isFromPopUpSave==null)
                      				{
                      			if(value.workOrderCreationStagedListData.length==1 && (value1.percentageAmountPay==undefined || value1.percentageAmountPay==null || value1.percentageAmountPay==0))
                     			 {
                      				value1.isSelected=true;
                     			value1.percentageAmountPay=parseFloat(value1.percentage!=undefined && value1.percentage!=null ? value1.percentage:0)-parseFloat(value1.percentageAmountPaid!=undefined && value1.percentageAmountPaid!=null ? value1.percentageAmountPaid:0)
                     			 }
                      		}
                      			if(value.workOrderCreationStagedListData.length==1)
                  				{
                  				value1.isSelected=true;
                  				}
	                      			if(value1.isSelected!=undefined && value1.isSelected!=null && value1.isSelected==true && (value.status=='A' || value.status==undefined || value.status==null))
	                  				{
                      				selectedCount++;
                      				}
                      				delete value1.isSelected;
                      				
                      		});
                      		if(stagedDataCount==0)
                      			{
//                      			value.workOrderCreationStagedListData=[];
                      			}
                      		delete value.isFromPopUpSave;
                      		delete value.isMultiStages;
                      		delete value.isFromPopUpSave;
                      		value.workOrderCompletionStagedListData=value.workOrderCreationStagedListData
                      	 })
                      	 if(selectedCount<=0)
                      		 {
                      		angular.forEach($scope.workOrderItemDetailsList,function(value, index){
                      		angular.forEach(value.workOrderCreationStagedListData,function(value1, index){
                      			if(value.workOrderCreationStagedListData.length>1)
                      				{
                      				value.isMultiStages=true;
                      				}
                      			else
                      				{
                      				value.isMultiStages=false;
                      				}
                      		 });
                      		 });
                      		 growl.error("Please select Stages");
                      		 return false;
                      		 }
                      	 if($scope.workOrderItemDetailsFinalList.length==0){
                      		growl.warning("Please select At least One Item Details.",
									{
										title : 'warning!'
									});
                      		
                      		return;
                      	 }
                      	  $scope.StoreWorkOrder.completionStatusId=10;
                      	  $scope.StoreWorkOrder.invoiceDate=$scope.common.invoiceDate;
                      	  $scope.StoreWorkOrder.dueDate=$scope.common.dueDate;
                      	  $scope.StoreWorkOrder.invioceNumber=$scope.common.invioceNumber;
                      	  $scope.StoreWorkOrder.isReverseTax=$scope.common.isReverseTax;
                      	  $scope.StoreWorkOrder.workOrderCompletionId=$scope.workOrderCompletionId;
                    	  $scope.StoreWorkOrder.requestApprovalDetailsDto=$scope.workOrderItemDetailsFinalList;
                    	  $scope.StoreWorkOrder.totalAmount=parseFloat($scope.totalNetAmount-  $scope.otherChargeAmount);
                     	  $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.otherChargeAmount);
                     	  $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.totalTaxAmount);
                     	  $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.totalDiscAmount);
                     	  $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.totalNetAmount);
                     	  
                     	 
                     	 
                     	  console.log("$scope.StoreWorkOrder",$scope.StoreWorkOrder);
                     	 $rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/saveCreationApprovalDetails/completionStages";
							GenericService.serviceAction("POST", URI, $scope.StoreWorkOrder).then(function(response) {
								if(response.data.status == "success"){
									$rootScope.stopLoader();
									growl.success(
											"saved successfully...",
											{
												title : 'Success!'
											});
//									/deatilsWorkorderApproval
									$state.go('workOrderCompletion',
											{}
											);	
								}else{
									$rootScope.stopLoader();
									growl.error(
											response.data.message,
											{
												title : 'Error!'
											});
									$state.go('workOrderCompletion',
											{}
											);	
								}
							});
                     	 
                    	  
                      }
                      
                      
                      
                      
                      
                     
								
//							************************************************save*******************************************
								
								  console.log("$stateParams.resultCommonObj",$stateParams.resultworkOrApprovalderDetailsSaveCommonObj);
								  if($stateParams.resultworkOrApprovalderDetailsSaveCommonObj.workorderRequestId!=undefined){
								  $scope.common = $stateParams.resultworkOrApprovalderDetailsSaveCommonObj;
	    							console.log(	$scope.common);
	    							if($scope.common.workorderRequestId!=undefined){
	    								$scope.btnText="Save";
	    								 
	    								$scope.StoreWorkOrder.workorderRequestId=$scope.common.workorderRequestId;
	    								$scope.StoreWorkOrder.workorderRequestNumber=$scope.common.workorderRequestNumber;
	    								$scope.StoreWorkOrder.billTypeId=$scope.common.billTypeId;
	    								$scope.StoreWorkOrder.storeId=$scope.common.storeId;
	    								$scope.StoreWorkOrder.vendorId=$scope.common.vendorId;
	    								$scope.StoreWorkOrder.vendorName=$scope.common.vendorName;
	    								$scope.StoreWorkOrder.againstTypeId=$scope.common.againstTypeId;
	    								
//	    								$scope.StoreWorkOrder.deliveryDate= moment($scope.common.deliveryDate).format('DD-MM-YYYY HH:mm:ss');
	    								$scope.StoreWorkOrder.contractTypeId=$scope.common.contractTypeId;
	    								
	    							 $scope.initialOtherCharges=$scope.common.otherChargeAmount;
	    							 if($scope.common.isFromEdit==true)
	    								 {
	    								 $scope.otherChargeAmount=$scope.common.otherChargeAmountForEdit;
	    								 }
	    							 else
	    								 {
	    								 $scope.otherChargeAmount=$scope.common.otherChargeAmount;
	    								 }
	    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
	                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
	                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
	                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
	                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
	                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
	                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
	    								
	    								
	                                 $scope.creditDays=$scope.common.creditDays;
	                                 $scope.workOrderItemDetailsList=[];
	    								if($scope.common.requestApprovalDetailsDto.length>0){
	    									 $scope.workOrderItemDetailsList=[];
	    									angular.forEach($scope.common.requestApprovalDetailsDto,function(value, index){
	    										var workOrderItemDetail = {};
	    									workOrderItemDetail.isSelected=false;	
	    									 workOrderItemDetail.workorderRequestDetialsId=value.workorderRequestDetialsId;
	       									 workOrderItemDetail.itemId=  value.itemId == null ?  ''  : value.itemId  ;
	       									 workOrderItemDetail.storeId=  $scope.StoreWorkOrder.storeId;
	       									 workOrderItemDetail.itemName= value.itemName;
	       									 workOrderItemDetail.serviceName= value.serviceName;
	       									 workOrderItemDetail.itemQuantity=value.itemQuantity == null ?  ''  : value.itemQuantity  ; 
	       									 workOrderItemDetail.approvedQty=0 ; 
	       									 workOrderItemDetail.rejectedQty=0;
	       									 workOrderItemDetail.itemRate=parseFloat( value.itemRate == null ?  ''  : value.itemRate  ); 
	       									 workOrderItemDetail.itemMrp=0; 
	       									 workOrderItemDetail.itemDiscPercentage=value.itemDiscPercentage == null ?  ''  : value.itemDiscPercentage  ; 
	       									 workOrderItemDetail.itemDiscAmount=0  ;
	       									 workOrderItemDetail.itemTaxId=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
	       									 taxpercentDetailsObj={};
	       									 taxpercentDetailsObj.id=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
	       									 taxpercentDetailsObj.taxPercentage= value.itemTax == null ?  ''  : value.itemTax  ;
	       									 workOrderItemDetail.taxpercentObj=taxpercentDetailsObj;
	       									 workOrderItemDetail.itemTax= value.itemTax == null ?  ''  : value.itemTax  ;
	       									 workOrderItemDetail.itemTaxAmount=0 ;
	       									 workOrderItemDetail.itemNetAmount=0 ;
	       									 workOrderItemDetail.itemRemark=value.itemRemark == null ?  ""  : value.itemRemark  ;
	       									 workOrderItemDetail.status='A' ;
	       									 workOrderItemDetail.createdDate="";
	       									 workOrderItemDetail.createdBy = $scope.userId;
	       									 workOrderItemDetail.updatedDate = "";
	       									 workOrderItemDetail.updatedBy = $scope.userId;
	       									 workOrderItemDetail.unitId = $scope.unitId;
	       									 workOrderItemDetail.orgId = $scope.orgId;
	       									 workOrderItemDetail.approvalDetailRemark="";
	       									 workOrderItemDetail.isRejectedQty=false;
	       									 workOrderItemDetail.itemRejectionObject={};
	       									workOrderItemDetail.workOrderCreationStagedListData=value.workOrderCreationStagedListData;
	       									 $scope.workOrderItemDetailsList.push(workOrderItemDetail);
	    									})
	    									$scope.calculateFinalAmount();
	    									if($scope.common.requestTaxDetailsDto!=null){
	    										
	    										if($scope.common.requestTaxDetailsDto.length>0){
	        										$scope.AddOtherchargesList=[];
	        										angular.forEach($scope.common.requestTaxDetailsDto,function(value, index){
	        											var AddOtherchargesObj = {};
	        											
	            										AddOtherchargesObj.workorderRequestId=value.workorderRequestId;
	            										AddOtherchargesObj.otherchargeTypeId=value.otherchargeTypeId;
	            										AddOtherchargesObj.otherChargeAmount=parseFloat(value.otherChargeAmount);
	            										AddOtherchargesObj.status='A';
	            										AddOtherchargesObj.createdDate="";
	            										AddOtherchargesObj.createdBy=$scope.userId;
	            										AddOtherchargesObj.updatedDate="";
	            										AddOtherchargesObj.updatedBy=$scope.userId;
	            										AddOtherchargesObj.unitId = $scope.unitId;
	            										AddOtherchargesObj.orgId = $scope.orgId;
	            										$scope.AddOtherchargesList.push(AddOtherchargesObj);
	            										
	        										  });
//	        										$scope.AddOtherchargeSave();
	        									}
	    										
	    										
	    									}
	    									console.log("final object data=",	$scope.StoreWorkOrder);
	    									console.log("final details data=",	$scope.workOrderItemDetailsList);
	    									$scope.documentlist=[];
	    									if($scope.common.requestDocumentDto!=null){
	    										$scope.documentlist=[];
	    										$scope.documentlist=$scope.common.requestDocumentDto;
	    									}
	    									
	    										
	    									}
	    								}
								  }
//	    		****************************************************save end***************************************		
//	    		****************************************************update start***********************************			
								  $scope.approvalStatus='';
	    							console.log("$stateParams.resultCommonObj",$stateParams.resultworkOrApprovalderDetailsUpdateCommonObj);
	    							  if($stateParams.resultworkOrApprovalderDetailsUpdateCommonObj
	    									  .workorderRequestId!=undefined){
	    								$scope.common = $stateParams.resultworkOrApprovalderDetailsUpdateCommonObj;
		    							console.log(	$scope.common);
		    							if($scope.common.workorderRequestId!=undefined){
		    								$scope.btnText="Update";
		    								if($scope.common.approvalStatus!=undefined && $scope.common.approvalStatus!=null && $scope.common.approvalStatus!="" )
		    								      $scope.approvalStatus=$scope.common.approvalStatus;
		    								$scope.StoreWorkOrder.workorderRequestApprovalId=$scope.common.workorderRequestApprovalId;
		    								$scope.StoreWorkOrder.workorderRequestId=$scope.common.workorderRequestId;
		    								$scope.StoreWorkOrder.workorderRequestNumber=$scope.common.workorderRequestNumber;
		    								$scope.StoreWorkOrder.billTypeId=$scope.common.billTypeId;
		    								$scope.StoreWorkOrder.storeId=$scope.common.storeId;
		    								$scope.StoreWorkOrder.vendorId=$scope.common.vendorId;
		    								$scope.StoreWorkOrder.vendorName=$scope.common.vendorName;
		    								$scope.StoreWorkOrder.againstTypeId=$scope.common.againstTypeId;
		    								
//		    								$scope.StoreWorkOrder.deliveryDate= moment($scope.common.deliveryDate).format('DD-MM-YYYY HH:mm:ss');
		    								$scope.StoreWorkOrder.contractTypeId=$scope.common.contractTypeId;
		    							 $scope.initialOtherCharges=$scope.common.otherChargeAmount;
		    							 if($scope.common.isFromEdit==true)
	    								 {
	    								 $scope.otherChargeAmount=$scope.common.otherChargeAmountForEdit;
	    								 }
	    							 else
	    								 {
	    								 $scope.otherChargeAmount=$scope.common.otherChargeAmount;
	    								 }
		    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
		                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
		                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
		                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
		                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
		                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
		                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
		                                 $scope.StoreWorkOrder.approvalRemark=	$scope.common.approvalRemark==null? '': $scope.common.approvalRemark;
		    								
		                                 $scope.creditDays=$scope.common.creditDays;
		                                 $scope.workOrderItemDetailsList=[];
		    								if($scope.common.requestApprovalDetailsDto.length>0){
		    									 $scope.workOrderItemDetailsList=[];
		    							angular.forEach($scope.common.requestApprovalDetailsDto,function(value, index){
		    									 var workOrderItemDetail = {};
		    									 if(value.isSelected==null){
		    										 value.isSelected=false;
		    									 }
		    									 $scope.common.workOrderCompletion=value.workOrderCompletion;
		    									 $scope.workOrderCompletionId=value.workOrderCompletionId;
		    									 $scope.common.invoiceDate=value.invoiceDate;
		    									 $scope.common.dueDate=value.dueDate;
		    									 $scope.common.invioceNumber=value.invoiceNumber;
		    									 if($scope.common.isFromEdit==true)
		    										 {
		    										 $scope.common.workOrderCompletion=false;
		    									 value.workOrderCreationStagedListData=value.workOrderCompletionStagedListData
		    										 }
//		    									 $scope.common.isReverseTax=value.isReverseTax;
		    									 workOrderItemDetail.isSelected=value.isSelected;	
		    									 workOrderItemDetail.workorderRequestDetialsId=value.workorderRequestDetialsId;
		       									 workOrderItemDetail.itemId=  value.itemId == null ?  ''  : value.itemId  ;
		       									 workOrderItemDetail.storeId=  $scope.StoreWorkOrder.storeId;
		       									 workOrderItemDetail.itemName= value.itemName;
		       									 workOrderItemDetail.serviceName= value.serviceName;
		       									 workOrderItemDetail.itemQuantity=value.itemQuantity == null ?  ''  : value.itemQuantity  ; 
		       									 workOrderItemDetail.approvedQty=value.approvedQty == null ?  ''  : value.approvedQty  ; 
		       									 workOrderItemDetail.rejectedQty=value.rejectedQty==null ? 0 : value.rejectedQty;
		       									 if(value.isSelected==false){
			       										 workOrderItemDetail.approvedQty=0 ; 
				       									 workOrderItemDetail.rejectedQty=0;
			       								}
		       									 
		       									 workOrderItemDetail.itemRate=parseFloat( value.itemRate == null ?  ''  : value.itemRate  ); 
		       									
		       									 
		       									 workOrderItemDetail.itemMrp=parseFloat(value.itemMrp == null ?  ''  : value.itemMrp ) ; 
		       									 if(value.isSelected==false){
			       										workOrderItemDetail.itemMrp=0 ; 
				       									
			       									 }
		       									 workOrderItemDetail.itemDiscPercentage=value.itemDiscPercentage == null ?  ''  : value.itemDiscPercentage  ; 
		       									 workOrderItemDetail.itemDiscAmount=parseFloat(value.itemDiscAmount == null ?  ''  : value.itemDiscAmount)  ;
		       									 if(value.isSelected==false){
			       										workOrderItemDetail.itemDiscAmount=0 ; 
			       									 }
		       									 workOrderItemDetail.itemTaxId=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
		       									 taxpercentDetailsObj={};
		       									 taxpercentDetailsObj.id=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
		       									 taxpercentDetailsObj.taxPercentage= value.itemTax == null ?  ''  : value.itemTax  ;
		       									 workOrderItemDetail.taxpercentObj=taxpercentDetailsObj;
		       									 workOrderItemDetail.itemTax= value.itemTax == null ?  ''  : value.itemTax  ;
		       									 workOrderItemDetail.itemTaxAmount=parseFloat(value.itemTaxAmount == null ?  ''  : value.itemTaxAmount ) ;
		       									 workOrderItemDetail.itemNetAmount=parseFloat(value.itemNetAmount == null ?  ''  : value.itemNetAmount ) ;
		       									 if(value.isSelected==false){
		       										 workOrderItemDetail.itemTaxAmount=0 ;
			       									 workOrderItemDetail.itemNetAmount=0 ;
			       									
			       									 }
		       									 workOrderItemDetail.itemRemark=value.itemRemark == null ?  ""  : value.itemRemark  ;
		       									 workOrderItemDetail.status='A' ;
		       									 workOrderItemDetail.createdDate="";
		       									 workOrderItemDetail.createdBy = $scope.userId;
		       									 workOrderItemDetail.updatedDate = "";
		       									 workOrderItemDetail.updatedBy = $scope.userId;
		       									 workOrderItemDetail.unitId = $scope.unitId;
		       									 workOrderItemDetail.orgId = $scope.orgId;
		       									 workOrderItemDetail.approvalDetailRemark=value.approvalDetailRemark;
		       									 workOrderItemDetail.isRejectedQty=value.isRejectedQty;
		       									 workOrderItemDetail.itemRejectionObject={};
		                                         workOrderItemDetail.workOrderCreationStagedListData=value.workOrderCreationStagedListData;
		                                         workOrderItemDetail.workorderRequestApprovalDetialsId=value.workorderRequestApprovalDetialsId;
		       									 $scope.workOrderItemDetailsList.push(workOrderItemDetail);
		    									})
		    									$scope.calculateFinalAmount();
		    									if($scope.common.requestTaxDetailsDto!=null){
		    										
		    										if($scope.common.requestTaxDetailsDto.length>0){
		        										$scope.AddOtherchargesList=[];
		        										angular.forEach($scope.common.requestTaxDetailsDto,function(value, index){
		        											var AddOtherchargesObj = {};
		        											
		            										AddOtherchargesObj.workorderRequestId=value.workorderRequestId;
		            										AddOtherchargesObj.otherchargeTypeId=value.otherchargeTypeId;
		            										AddOtherchargesObj.otherChargeAmount=parseFloat(value.otherChargeAmount);
		            										AddOtherchargesObj.status='A';
		            										AddOtherchargesObj.createdDate="";
		            										AddOtherchargesObj.createdBy=$scope.userId;
		            										AddOtherchargesObj.updatedDate="";
		            										AddOtherchargesObj.updatedBy=$scope.userId;
		            										AddOtherchargesObj.unitId = $scope.unitId;
		            										AddOtherchargesObj.orgId = $scope.orgId;
		            										$scope.AddOtherchargesList.push(AddOtherchargesObj);
		            										
		        										  });
//		        										$scope.AddOtherchargeSave();
		        									}
		    										
		    										
		    									}
		    									console.log("final object data=",	$scope.StoreWorkOrder);
		    									console.log("final details data=",	$scope.workOrderItemDetailsList);
		    									$scope.documentlist=[];
		    									if($scope.common.requestDocumentDto!=null){
		    										$scope.documentlist=[];
		    										$scope.documentlist=$scope.common.requestDocumentDto;
		    									}
		    									
		    										
		    									}
		    								}
	    							}
//	    		*****************************************************update end **********************************************
//              *************************************************************ReadOnly start***********************************    							  
	    							  
	    							  console.log("$stateParams.resultCommonObj",$stateParams.resultworkOrApprovalderDetailsReadOnlyCommonObj);
	    							  console.log("$stateParams.resultCommonObj",$stateParams.resultworkOrApprovalderDetailsReadOnlyCommonObj);
	    							  if($stateParams.resultworkOrApprovalderDetailsReadOnlyCommonObj
	    									  .workorderRequestId!=undefined){
	    								$scope.common = $stateParams.resultworkOrApprovalderDetailsReadOnlyCommonObj;
		    							console.log(	$scope.common);
		    							if($scope.common.workorderRequestId!=undefined){
		    								$scope.btnText="Save";
		    								$scope.readOnlyApproved=true;
		    								if($scope.common.workorderRequestApprovalId!=undefined && $scope.common.workorderRequestApprovalId!=null)
		    								     $scope.StoreWorkOrder.workorderRequestApprovalId=$scope.common.workorderRequestApprovalId;
		    								$scope.StoreWorkOrder.workorderRequestId=$scope.common.workorderRequestId;
		    								$scope.StoreWorkOrder.workorderRequestNumber=$scope.common.workorderRequestNumber;
		    								$scope.StoreWorkOrder.billTypeId=$scope.common.billTypeId;
		    								$scope.StoreWorkOrder.storeId=$scope.common.storeId;
		    								$scope.StoreWorkOrder.vendorId=$scope.common.vendorId;
		    								$scope.StoreWorkOrder.vendorName=$scope.common.vendorName;
		    								$scope.StoreWorkOrder.againstTypeId=$scope.common.againstTypeId;
		    								
//		    								$scope.StoreWorkOrder.deliveryDate= moment($scope.common.deliveryDate).format('DD-MM-YYYY HH:mm:ss');
		    								$scope.StoreWorkOrder.contractTypeId=$scope.common.contractTypeId;
		    							 $scope.initialOtherCharges=$scope.common.otherChargeAmount;
		    							 if($scope.common.isFromEdit==true)
	    								 {
	    								 $scope.otherChargeAmount=$scope.common.otherChargeAmountForEdit;
	    								 }
	    							 else
	    								 {
	    								 $scope.otherChargeAmount=$scope.common.otherChargeAmount;
	    								 }
		    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
		                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
		                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
		                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
		                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
		                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
		                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
		                                 $scope.StoreWorkOrder.approvalRemark=	$scope.common.approvalRemark==null? '': $scope.common.approvalRemark;
		    								
		                                 $scope.creditDays=$scope.common.creditDays;
		                                 $scope.workOrderItemDetailsList=[];
		    								if($scope.common.requestApprovalDetailsDto.length>0){
		    									 $scope.workOrderItemDetailsList=[];
		    							angular.forEach($scope.common.requestApprovalDetailsDto,function(value, index){
		    									 var workOrderItemDetail = {};
		    									 if(value.isSelected==null){
		    										 value.isSelected=false;
		    									 }
		    									 workOrderItemDetail.isSelected=value.isSelected;	
		    									 workOrderItemDetail.workorderRequestDetialsId=value.workorderRequestDetialsId;
		       									 workOrderItemDetail.itemId=  value.itemId == null ?  ''  : value.itemId  ;
		       									 workOrderItemDetail.storeId=  $scope.StoreWorkOrder.storeId;
		       									 workOrderItemDetail.itemName= value.itemName;
		       									 workOrderItemDetail.serviceName= value.serviceName;
		       									 workOrderItemDetail.itemQuantity=value.itemQuantity == null ?  ''  : value.itemQuantity  ; 
		       									 workOrderItemDetail.approvedQty=value.approvedQty == null ?  ''  : value.approvedQty  ; 
		       									 workOrderItemDetail.rejectedQty=value.rejectedQty==null ? 0 : value.rejectedQty;
		       									 if(value.isSelected==false){
			       										 workOrderItemDetail.approvedQty=0 ; 
				       									 workOrderItemDetail.rejectedQty=0;
			       								}
		       									 
		       									 workOrderItemDetail.itemRate=parseFloat( value.itemRate == null ?  ''  : value.itemRate  ); 
		       									
		       									 
		       									 workOrderItemDetail.itemMrp=parseFloat(value.itemMrp == null ?  ''  : value.itemMrp ) ; 
		       									 if(value.isSelected==false){
			       										workOrderItemDetail.itemMrp=0 ; 
				       									
			       									 }
		       									 workOrderItemDetail.itemDiscPercentage=value.itemDiscPercentage == null ?  ''  : value.itemDiscPercentage  ; 
		       									 workOrderItemDetail.itemDiscAmount=parseFloat(value.itemDiscAmount == null ?  ''  : value.itemDiscAmount)  ;
		       									 if(value.isSelected==false){
			       										workOrderItemDetail.itemDiscAmount=0 ; 
			       									 }
		       									 workOrderItemDetail.itemTaxId=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
		       									 taxpercentDetailsObj={};
		       									 taxpercentDetailsObj.id=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
		       									 taxpercentDetailsObj.taxPercentage= value.itemTax == null ?  ''  : value.itemTax  ;
		       									 workOrderItemDetail.taxpercentObj=taxpercentDetailsObj;
		       									 workOrderItemDetail.itemTax= value.itemTax == null ?  ''  : value.itemTax  ;
		       									 workOrderItemDetail.itemTaxAmount=parseFloat(value.itemTaxAmount == null ?  ''  : value.itemTaxAmount ) ;
		       									 workOrderItemDetail.itemNetAmount=parseFloat(value.itemNetAmount == null ?  ''  : value.itemNetAmount ) ;
		       									 if(value.isSelected==false){
		       										 workOrderItemDetail.itemTaxAmount=0 ;
			       									 workOrderItemDetail.itemNetAmount=0 ;
			       									
			       									 }
		       									 workOrderItemDetail.itemRemark=value.itemRemark == null ?  ""  : value.itemRemark  ;
		       									 workOrderItemDetail.status='A' ;
		       									 workOrderItemDetail.createdDate="";
		       									 workOrderItemDetail.createdBy = $scope.userId;
		       									 workOrderItemDetail.updatedDate = "";
		       									 workOrderItemDetail.updatedBy = $scope.userId;
		       									 workOrderItemDetail.unitId = $scope.unitId;
		       									 workOrderItemDetail.orgId = $scope.orgId;
		       									 workOrderItemDetail.approvalDetailRemark=value.approvalDetailRemark;
		       									 workOrderItemDetail.isRejectedQty=value.isRejectedQty;
		       									 workOrderItemDetail.itemRejectionObject={};
		       									workOrderItemDetail.workOrderCreationStagedListData=value.workOrderCreationStagedListData;
		       									 $scope.workOrderItemDetailsList.push(workOrderItemDetail);
		    									})
		    									$scope.calculateFinalAmount();
		    									if($scope.common.requestTaxDetailsDto!=null){
		    										
		    										if($scope.common.requestTaxDetailsDto.length>0){
		        										$scope.AddOtherchargesList=[];
		        										angular.forEach($scope.common.requestTaxDetailsDto,function(value, index){
		        											var AddOtherchargesObj = {};
		        											
		            										AddOtherchargesObj.workorderRequestId=value.workorderRequestId;
		            										AddOtherchargesObj.otherchargeTypeId=value.otherchargeTypeId;
		            										AddOtherchargesObj.otherChargeAmount=parseFloat(value.otherChargeAmount);
		            										AddOtherchargesObj.status='A';
		            										AddOtherchargesObj.createdDate="";
		            										AddOtherchargesObj.createdBy=$scope.userId;
		            										AddOtherchargesObj.updatedDate="";
		            										AddOtherchargesObj.updatedBy=$scope.userId;
		            										AddOtherchargesObj.unitId = $scope.unitId;
		            										AddOtherchargesObj.orgId = $scope.orgId;
		            										$scope.AddOtherchargesList.push(AddOtherchargesObj);
		            										
		        										  });
//		        										$scope.AddOtherchargeSave();
		        									}
		    										
		    										
		    									}
		    									console.log("final object data=",	$scope.StoreWorkOrder);
		    									console.log("final details data=",	$scope.workOrderItemDetailsList);
		    									$scope.documentlist=[];
		    									if($scope.common.requestDocumentDto!=null){
		    										$scope.documentlist=[];
		    										$scope.documentlist=$scope.common.requestDocumentDto;
		    									}
		    									
		    										
		    									}
		    								}
	    							}
	    							  
	    							  
//	    	    *************************************************************ReadOnly end**************************************    							  
	    							  
	    							 $scope.cancelRejectionPopup=function(){
	    		                    	  
	    		                    	  
	    		                    	  
	    		                    	  angular.forEach($scope.workOrderItemDetailsList,function(value, index){
	    		                    		 if( $scope.itemRejectionObject.workorderRequestDetialsId!=undefined){
	    		                    			 if(value.workorderRequestDetialsId==$scope.itemRejectionObject.workorderRequestDetialsId){
		    		                    			  value.itemRejectionObject={};
		    		                    			  value.approvedQty=value.itemQuantity;
		    		                    			  value.rejectedQty=0;
		    		                    			  value.isRejectedQty=false;
		    		                    			  $scope.calculate(value,index,'');
		    		                    		  }
	    		                    		 }
	    		                    		 
	    		                    	  }) 
	    		                    	  
	    		                    	  $scope.itemRejectionObject={};
//	    		                    	  $("#qtyaccepatReject").modal("hide");
	    		                      }	
	    		                      
	    		                      $scope.comfirmRejection=function(){
	    									if( $scope.itemRejectionObject.cancelReasonId==undefined || $scope.itemRejectionObject.cancelReasonId==''){
	    										growl.warning("Please select Valid Reason  .",
														{
															title : 'warning!'
														});
	    										return false;
	    									}
	    									if( $scope.itemRejectionObject.rejectedQty==undefined || $scope.itemRejectionObject.rejectedQty=="" || $scope.itemRejectionObject.rejectedQty==0 ||  $scope.itemRejectionObject.rejectedQty> $scope.itemRejectionObject.itemQuantity){
	    										growl.warning("Please enter Valid Reject Quantity  .",
														{
															title : 'warning!'
														});
	    										return false;
	    									}
	    									
	    									if( $scope.itemRejectionObject.rejectNote==undefined || $scope.itemRejectionObject.rejectNote==""){
	    										growl.warning("Please enter Valid Reject Note  .",
														{
															title : 'warning!'
														});
	    										return false;
	    									}
	    									
	    									
	    		                    	  angular.forEach($scope.workOrderItemDetailsList,function(value, index){
	    		                    		  if(value.workorderRequestDetialsId==$scope.itemRejectionObject.workorderRequestDetialsId){
	    		                    			  value.itemRejectionObject=$scope.itemRejectionObject;
	    		                    			  value.rejectedQty=$scope.itemRejectionObject.rejectedQty;
	    		                    			  value.approvedQty=value.itemQuantity - value.rejectedQty;
	    		                    			  $scope.calculate(value,index,'');
	    		                    		  }
	    		                    		  
	    		                    		  
	    		                    	  }) 
	    		                    	  
//	    		                    	  $("#qtyaccepatReject").modal("hide");
	    		                    	  $scope.itemRejectionObject={};
	    		                      }
	    								
								
								
								
								
								$scope.getNewWOElementForReadonly=function(workorderRequestId){
									var data = {
											    "workorderRequestId" : workorderRequestId
											    
											};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationRequestDetailsbyId";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											console.log("complete object",response.data.object);
											if(response.data.object!=null){
												$state.go('addNewWorkOrderCreation',
														{
													     resultworkOrderReadOnlyDetailsCommonObj: response.data.object
														});	
												
											}
											
											
										}
									});
								
								
							}
								
								$scope.changestatuToPending=function(){
									$scope.finalChangeToPendinglist=[];
									
									if($scope.searchNewWOItemList.length==0){
										 growl.warning("Please Select At Least one Work Order ",
													{
														title : 'warning!'
													});
						                  return false;
									}
									if($scope.searchNewWOItemList.length > 0){
										angular.forEach($scope.searchNewWOItemList,function(value, index){
											if(value.selectedToPendingstatus==true){
												$scope.finalChangeToPendinglist.push(value);
												
											}
										});
										if($scope.finalChangeToPendinglist.length==0){
											 growl.warning("Please Select At Least one Work Order ",
														{
															title : 'warning!'
														});
							                  return false;
										}else{
											 $rootScope.startLoader();
											var data={};
											 data.workOrderRequestList=$scope.finalChangeToPendinglist;
											var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/requestChangeToPending";
											GenericService.serviceAction("POST", URI, data).then(function(response) {
												if(response.data.status == "success"){
													 $rootScope.stopLoader();
													growl.success("updated successfully",
  															{
  																title : 'Success!'
  															});
													
													$scope.getListNewWOList();
												}else{
													$rootScope.stopLoader();
													$scope.getListNewWOList();
												}
											});
											
										}
										
										
										
									}
										
									
								}
                                
                                
									
							 
							 
							 
                              
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
						
						 
			     	
			     	
			    	
			    	
			   
			    	
			    	
			    	
			    	
			    	
					
					
					
					
					
					
					
					
					
					
 }
						
 ]);