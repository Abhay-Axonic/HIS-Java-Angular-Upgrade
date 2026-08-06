
angular
		.module('myApp')
		.controller(
				"workOrderCompletionController",
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
							"CONSTANTS",
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,GenericServiceParamHeader,
								$stateParams,CONSTANTS,$modalInstance) {

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
							
							
							var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
							if(cookieObjectStore==undefined){
								$rootScope.storePopup();
								angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
							}
							$scope.storeName = cookieObjectStore != null ? cookieObjectStore.storeName : ''; // store null -> empty header (approved tolerance)
							$scope.storeId = cookieObjectStore != null ? cookieObjectStore.storeId : undefined;
							
							
							
							$scope.workOrderItemDetails=[];
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							 $scope.workOrderItemDetailsList=[];
							 $scope.finalDocUploadList=[];
							console.info($scope.orgId)
								var data = {};
							//$(".selectedPageName").text("Machine Master");
							//Date For From Date on Search
							
							 $scope.searchNewWOItemList= [];
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
										var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationRequestList/completionSearch";
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											if(response.data.status == "success"){
												$rootScope.stopLoader();
												if(response.data.listObject!=null && response.data.listObject.length >0)
											        $scope.searchNewWOItemList = response.data.listObject;
												if($scope.searchNewWOItemList.length > 0){
													angular.forEach($scope.searchNewWOItemList,function(value, index){
														value.workorderRequestStatusId=value.approvalStatusId
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
							
							
							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.StoreWorkOrderAppovalListObj = {
									"workorderRequestId":'',	
									"storeId" : '',
									"vendorId" : '',
									"vendorName" : '',
									"contractTypeId":'',
									'billTypeId':'',
									'fromDate':new Date(),
									'toDate':new Date(),
									'workorderRequestNumber':"",
									"status" : "A",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"totalAmount":'',
									"totalDiscountAmount":'',
									"totalTaxAmount":'',
									"otherChargeAmount":'',
									"totalNetAmount":'',
									"requestDetailsDto":[],
									"requestTaxDetailsDto":[],
									"requestDocumentDto":[]
									
									
								}
								
								$scope.StoreWorkOrderAppovalListObj.storeId=$scope.storeId;
								$scope.getListNewWOList();
								
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
									 maxDate : new Date(),
									
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
						
						$scope.getStoreList=[];
						 $scope.getStorelist = function(){
							var URI = BASE_URL + ROOT_URL + API + "/procurements/WOCreation/dropdowns"+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
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
						      if (search.length < 1) return;
						      $scope.vendorNames=[];
						      var URI = BASE_URL + ROOT_URL + API + "/global/vendor/search/"+ search + "/"+$scope.unitId;
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
							
							
							
							
							$scope.getOrderlist=[];
							 $scope.getOrderlist = function(){
								var URI = BASE_URL + ROOT_URL + API + "/procurements/workOrderRequest/dropdowns"+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
								BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.status == 200){
										$scope.getOrderlist = response.data.contractType;
									}
									console.log("getOrderlist",$scope.getOrderlist);
								});
							}
							 $scope.getOrderlist ();
						 
						 
							 
							 $scope.clearWorkorderListObj = function(){
								 $scope.initCommon();
								}
							 
							 
							 
							 $scope. getProductCategoryList = function (id) {
						          
						            var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id+ '/'+$scope.organizationId ;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                	$scope.productCategoriesList = response.data;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
						        }
							 
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
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
								
								
								
								$scope.getNewWOElementForEdit=function(workorderRequestId){
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
									
									
								}
								$scope.getNewWOElementForApprovalDetailEdit=function(workorderRequestId,workorderRequestStatusId,workOrderCompletionId){
									var data = {
											    "workorderRequestId" : workorderRequestId,
											    "workOrderCompletionId":workOrderCompletionId
											};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationApprovalDetailsbyId/edit";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											console.log("complete object",response.data.object);
											if(response.data.object!=null){
												response.data.object.isFromEdit=true;
												if(response.data.message=="updatedata"){
													if(workorderRequestStatusId!=5){
														$state.go('workOrderCompletionDetails',
																{
															      resultworkOrApprovalderDetailsUpdateCommonObj: response.data.object
																});	
													}
													if(workorderRequestStatusId==5){
														$state.go('workOrderCompletionDetails',
																{
															      resultworkOrApprovalderDetailsReadOnlyCommonObj: response.data.object
																});	
													}
													
												}
												
												if(response.data.message=="savedata"){
													$state.go('workOrderCompletionDetails',
															{
														      resultworkOrApprovalderDetailsSaveCommonObj: response.data.object
															});	
												}
												
												
											}
											
											
										}
										else
											{
											$rootScope.stopLoader();
											}
									});
								
								
							}
								$scope.getCompletedDetails=function(workorderRequestId){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
									var data = {
											    "workorderRequestId" : workorderRequestId,
											    "storeId":$scope.storeId ,
												"unitId":$scope.unitId ,
												"orgId":$scope.orgId 
											};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/getCreationApprovalDetailsbyId/getWOCompletedTransaction";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$('#completion_details').modal('show');
											$scope.completedTransactionList=response.data.listObject;
										}
									});
								
								
							}
								$scope.getNewWOElementForApprovalEdit=function(workorderRequestId,workorderRequestStatusId,workOrderCompletionId){
									if(workorderRequestStatusId==10)
										{
										growl.error("Please Save Pending Approval");
										return false;
										}
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
												response.data.object.isFromEdit=false;
												if(response.data.message=="updatedata"){
													if(workorderRequestStatusId!=5){
														$state.go('workOrderCompletionDetails',
																{
															      resultworkOrApprovalderDetailsUpdateCommonObj: response.data.object
																});	
													}
													if(workorderRequestStatusId==5){
														$state.go('workOrderCompletionDetails',
																{
															      resultworkOrApprovalderDetailsReadOnlyCommonObj: response.data.object
																});	
													}
													
												}
												
												if(response.data.message=="savedata"){
													$state.go('workOrderCompletionDetails',
															{
														      resultworkOrApprovalderDetailsSaveCommonObj: response.data.object
															});	
												}
												
												
											}
											
											
										}
									});
								
								
							}
								
								
								$scope.cancelWOElement=function(workorderRequestId){
									var data = {
											    "workorderRequestId" : workorderRequestId,
											    "approvedBy":$scope.userId
											};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/requestChangeToCancel";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.getListNewWOList();
										}
									});
								
								
							}
								
								
								$scope.showRejectionPopup=function(info){
									if(info.isReject==true){
										$scope.rejectionObj={
												workorderRequestId :'',
												cancelReasonId:'',
												rejectNote:"",
												createdBy : $scope.userId
										};
										
										$scope.rejectionObj.workorderRequestId=info.workorderRequestId;
										$("#Reject_modal").modal("show");
										
									}
									 
									 
									
								}
								
								$scope.CancelRejection=function(){
									angular.forEach($scope.searchNewWOItemList,function(value, index){
										if(value.workorderRequestId==$scope.rejectionObj.workorderRequestId){
											value.isReject=false
										}
									})
									$scope.rejectionObj={};
									
									
								}
								
								$scope.rejectWOElement=function(){
									if($scope.rejectionObj.cancelReasonId==undefined || $scope.rejectionObj.cancelReasonId==''){
										 growl.warning("Please select Reason",
													{
														title : 'warning!'
													});
										 return;
									}
									if($scope.rejectionObj.rejectNote==undefined || $scope.rejectionObj.rejectNote==""){
										 growl.warning("Please enter Reason Note",
													{
														title : 'warning!'
													});
										 return;
									}
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/requestChangeToReject";
									GenericService.serviceAction("POST", URI, $scope.rejectionObj).then(function(response) {
										if(response.data.status == "success"){
											
											$rootScope.stopLoader();
											$scope.getListNewWOList();
										}
									});
									$scope.rejectionObj={};
									$("#Reject_modal").modal("hide");
								
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
								
								
							    $scope.printData = function(CompletionId)
							    {
								    var data = {
									    "appName": "HIS",
									    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									    "reportDesignFilePath": "get_WorkOrderCompletionDetails.rpt",
									    "reportFormat": "PDF",
									    "exportFileName": "Certificate",
									    "reportParams": [
											    {
											    "paramName": "id",
											    "paramType": "Integer",
											    "paramValue": CompletionId,
					
											    }
										    ]
								                };
								    reportService.generate(REPORT_API_URI1,data);
							    }
							    
							    
								 
								$scope.changestatusToApproval=function(){
									$scope.finalChangeToPendinglist=[];
									$scope.SelectedRequestIds = [];	
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
												$scope.SelectedRequestIds.push(value.workorderRequestId);
												
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
												data.orgId= $scope.orgId;
												data.unitId= $scope.unitId;
												data.createdBy=$scope.userId;
												var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/requestChangeToApproved/completionSvaeSubmit";
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
										
										//  $rootScope.startLoader();
								        //   var URI = CONSTANTS.PROCUREMENT.WORK_ORDER.WORK_ORDER_CREATION.APPROVE_API;

								        //   var data = {
								        //     "woCreationIds":  $scope.SelectedRequestIds,
								        //     "statusId": 8,
								        //     "approvalStatusId": 5,
								        //     "unitId":$scope.unitId,
								        //     "organizationId":$scope.organizationId,
								        //     "userId":$scope.userId
								        //   };
								        //   $rootScope.startLoader();
								        //   GenericService.serviceAction("PATCH", URI, data).then(
								        //     function (response) {
								        //     	$scope.SelectedRequestIds = [];	
								        //       $scope.getListNewWOList();
								        //       self.model.selectAllRows = false;
								        //       $rootScope.stopLoader();
								        //       growl.success(response.data.message);
								        //     },
								        //     function (err) {
								        //     	$scope.SelectedRequestIds = [];	
								        //     	 $rootScope.stopLoader();
								        //       growl.error(err.data.message);
								        //     });
								        
										
										
										
									}
										
									
								}
                                
                          }
				
						
 ]);