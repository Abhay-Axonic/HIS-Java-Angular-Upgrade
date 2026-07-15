
angular
		.module('myApp')
		.controller(
				"addNewWorkorderApprovalDetailsController",
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
									
									angular.forEach($scope.workOrderItemDetailsList,function(value, index){
										if(value.isSelected==true){
											
										
										var discamt=parseFloat(0);
										 var taxamt=parseFloat(0);
										if( value.itemDiscAmount!=undefined && value.itemDiscAmount!='' && value.itemDiscAmount!=0){
											discamt=parseFloat(discamt+value.itemDiscAmount);
										}
										if( value.itemTaxAmount!=undefined && value.itemTaxAmount!='' && value.itemTaxAmount!=0  ){
											taxamt=parseFloat(taxamt+value.itemTaxAmount);
										}
										
										value.itemNetAmount= parseFloat((parseFloat(value.itemMrp )+ parseFloat( taxamt))-parseFloat(discamt) );
										
										$scope.totalTaxAmount=parseFloat($scope.totalTaxAmount + taxamt);
										$scope.totalDiscAmount=parseFloat($scope.totalDiscAmount+discamt);
										
											$scope.totalNetAmount=parseFloat($scope.totalNetAmount+ value.itemNetAmount);
										
										}
									 })
									 if($scope.otherChargeAmount==undefined){
										 $scope.otherChargeAmount=0;
									 }
									 if($scope.otherChargeAmount!=0){
										 
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
									
										 $("#qtyaccepatReject").modal('hide');
										 $scope.calculateFinalAmount();
										 
									
								}
								
								
								$scope.showRejectionPopup=function(workOrderItemDetail,index){
									if(workOrderItemDetail.isRejectedQty==true){
										if(workOrderItemDetail.itemRejectionObject.workorderRequestDetialsId==undefined){
											$scope.itemRejectionObject={};
											$("#qtyaccepatReject").modal("show");
											 $scope.itemRejectionObject.workorderRequestDetialsId=workOrderItemDetail.workorderRequestDetialsId;
											 $scope.itemRejectionObject.cancelReasonId='';
											 $scope.itemRejectionObject.rejectedQty=workOrderItemDetail.rejectedQty;
											 $scope.itemRejectionObject.itemQuantity=workOrderItemDetail.itemQuantity;
											 $scope.itemRejectionObject.rejectNote="";
										}else{
											$scope.itemRejectionObject={};
											$("#qtyaccepatReject").modal("show");
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
														$("#qtyaccepatReject")
																.modal("show");
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
								                    	else
								                    		{
								                    		$scope.calculateFinalAmount();
								                    		}

							}
                      $scope.calculatePercentage =function()
                      {
                      	var percentageSum=0;
                      	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                      		percentageSum=percentageSum+parseFloat(modal.percentage);
                      	});
                      	if(percentageSum>100)
                      		{
                      		growl.error("Sum of percentage can not be greater than 100% ");
                      		return false;
                      		}
                      }
                      $scope.closeStagedPopUp =function()
                      {
                      	if($scope.initailStagedData.length>0)
                      		{
                      		$scope.workOrderCreationStagedListData=$scope.initailStagedData;
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
                          	$scope.workOrderCreationStagedListData=[];
                          angular.forEach(workOrder.workOrderCreationStagedListData, function (list) {
                          	$scope.workOrderCreationStagedListData.push({
                          		stagedWorkorderId:list.stagedWorkorderId,
                          		stageName:list.stageName,
                          		percentage: list.percentage,
                              });
                            });
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
                          		stageName: "",
                          		percentage: 100-percentageSum,
                              });
                          		}
                          	}
                          
                      }
                     $scope.stagedWorkOrderModalOnClick = function () {
                  	   $scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].workOrderCreationStagedListData = [];
                  	      var modal_list = $scope.workOrderCreationStagedListData;
                  	      var stageNameCount=0;
                  	      modal_list.forEach(function (modal, index) {
                  	        if(modal.stageName=="" || modal.percentage==0 || modal.percentage=='')
                  	        {
                  	        	stageNameCount++;
                  	        }
                  	      });
                  	      if(stageNameCount>0)
                  	    	  {
                  	    	  growl.error("Please enter all Required fields");
                	        	  return false;
                  	    	  }
                  	      var percentageSum=0;
                        	$scope.workOrderCreationStagedListData.forEach(function (modal, index) {
                        		percentageSum=percentageSum+parseFloat(modal.percentage);
                        	});
                        	if(percentageSum<100)
                        		{
                        		growl.error("Sum of percentage must be 100% ");
                        		return false;
                        		}
                        	if(percentageSum>100)
                    		{
                        		growl.error("Sum of percentage can not be greater than 100% ");
                    		return false;
                    		}
                  	      modal_list.forEach(function (modal, index) {
                  	    	  $scope.workOrderItemDetailsList[$scope.WorkOrderListIndex].workOrderCreationStagedListData.push({
                  	        	stagedWorkorderId:modal.stagedWorkorderId!=null ? modal.stagedWorkorderId :null ,
                  	        			stageName: modal.stageName,
                  	        			percentage: modal.percentage,
                  	        });
                  	      });
                  	      $scope.workOrderCreationStagedListData=[];
                  	      $('#staged_Workorder_Creation').modal('hide');
                  	    }
					$scope.SelectAllItems = function () {
						if ($scope.selectAllRows) {
							$scope.workOrderItemDetailsList.forEach(function (item,index) {
								item.approvedQty=item.itemQuantity;
								item.isSelected = true;
								$scope.calculate(item,index,"");
							});
						} else {
							$scope.workOrderItemDetailsList.forEach(function (item,index) {
								item.isSelected = false;
								item.approvedQty=0 ; 
								item.rejectedQty=0;
								item.itemMrp=0; 
								item.itemDiscAmount=0;
								item.itemTaxAmount=0 ;
								item.itemNetAmount=0 ;
								item.approvalDetailRemark="";
								item.isRejectedQty=false;
							     $scope.calculateFinalAmount();
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
                    	  angular.forEach($scope.workOrderItemDetailsList,function(value, index){
                      		 if(value.isSelected==true){
                      			$scope.workOrderItemDetailsFinalList.push(value);
                      		 }
                      		 
                      	 })
                      	 if($scope.workOrderItemDetailsFinalList.length==0){
                      		growl.warning("Please select At least One Item Details.",
									{
										title : 'warning!'
									});
                      		
                      		return;
                      	 }
                    	  $scope.StoreWorkOrder.isReverseTax=$scope.common.isReverseTax;
                    	  $scope.StoreWorkOrder.requestApprovalDetailsDto=$scope.workOrderItemDetailsList;
                    	  $scope.StoreWorkOrder.totalAmount=parseFloat($scope.totalNetAmount-  $scope.otherChargeAmount);
                     	  $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.otherChargeAmount);
                     	  $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.totalTaxAmount);
                     	  $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.totalDiscAmount);
                     	  $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.totalNetAmount);
                     	  
                     	 
                     	 
                     	  console.log("$scope.StoreWorkOrder",$scope.StoreWorkOrder);
                     	 $rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/saveCreationApprovalDetails";
							GenericService.serviceAction("POST", URI, $scope.StoreWorkOrder).then(function(response) {
								if(response.data.status == "success"){
									$rootScope.stopLoader();
									growl.success(
											"saved successfully...",
											{
												title : 'Success!'
											});
//									/deatilsWorkorderApproval
									$state.go('workOrderApproval',
											{}
											);	
								}else{
									$rootScope.stopLoader();
									growl.error(
											response.data.message,
											{
												title : 'Error!'
											});
									$state.go('workOrderApproval',
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
	    								
	    								
	    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
	                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
	                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
	                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
	                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
	                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
	                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
	    								
	    								
	    								
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
	        										$scope.AddOtherchargeSave();
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
		    								
		    								
		    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
		                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
		                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
		                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
		                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
		                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
		                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
		                                 $scope.StoreWorkOrder.approvalRemark=	$scope.common.approvalRemark==null? '': $scope.common.approvalRemark;
		    								
		    								
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
		        										$scope.AddOtherchargeSave();
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
		    								
		    								
		    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
		                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
		                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
		                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
		                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
		                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
		                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
		                                 $scope.StoreWorkOrder.approvalRemark=	$scope.common.approvalRemark==null? '': $scope.common.approvalRemark;
		    								
		    								
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
		        										$scope.AddOtherchargeSave();
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
	    		                    	  $("#qtyaccepatReject").modal("hide");
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
	    		                    	  
	    		                    	  $("#qtyaccepatReject").modal("hide");
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