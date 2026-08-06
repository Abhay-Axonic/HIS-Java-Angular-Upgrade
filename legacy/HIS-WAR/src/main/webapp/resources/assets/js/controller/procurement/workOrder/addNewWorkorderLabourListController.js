
angular
		.module('myApp')
		.controller(
				"addNewWorkorderLabourListController",
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
							'cancelReasonService',
							'$stateParams',
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,GenericServiceParamHeader,
								cancelReasonService,$stateParams,$modalInstance) {

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
			$scope.storeName = 'All'; // labour-list intentional default: ALL stores (unchanged behavior — only the parse was converted)
			$scope.storeId = 0;
							
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
										data.storeId= $scope.StoreWorkOrderListObj.storeId;
										
										if($scope.StoreWorkOrderListObj.toDate!=undefined && $scope.StoreWorkOrderListObj.toDate!="" )
										{
											data.toDate=moment( $scope.StoreWorkOrderListObj.toDate ).format('YYYY-MM-DD');
										}
										
										if($scope.StoreWorkOrderListObj.fromDate!=undefined && $scope.StoreWorkOrderListObj.fromDate!="" )
										{
											data.fromDate=moment( $scope.StoreWorkOrderListObj.fromDate ).format('YYYY-MM-DD');
										}
										if($scope.StoreWorkOrderListObj.workorderRequestNumber!=undefined && $scope.StoreWorkOrderListObj.workorderRequestNumber!="" )
										{
											data.workorderRequestNumber=$scope.StoreWorkOrderListObj.workorderRequestNumber ;
										}
										
										if($scope.StoreWorkOrderListObj.billTypeId!=undefined && $scope.StoreWorkOrderListObj.billTypeId!='' )
										{
											data.billTypeId=$scope.StoreWorkOrderListObj.billTypeId ;
										}
										
										if($scope.StoreWorkOrderListObj.vendorId!=undefined && $scope.StoreWorkOrderListObj.vendorId!='' && $scope.StoreWorkOrderListObj.vendorId!=0 )
										{
											data.vendorId=$scope.StoreWorkOrderListObj.vendorId ;
										}
										if($scope.StoreWorkOrderListObj.workorderRequestStatusId!=undefined && $scope.StoreWorkOrderListObj.workorderRequestStatusId!='' && $scope.StoreWorkOrderListObj.workorderRequestStatusId!=0 )
										{
											data.workorderRequestStatusId=$scope.StoreWorkOrderListObj.workorderRequestStatusId ;
										}
										
										if($scope.StoreWorkOrderListObj.storeId==undefined){
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
								$scope.StoreWorkOrderListObj = {
									"workorderRequestId":'',	
									"storeId" : 0,
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
								
								$scope.StoreWorkOrderListObj.storeId=$scope.storeId;
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
									 maxDate : new Date()
									
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
//						$scope.StoreWorkOrderListObj.toDate= new Date();
//						$scope.StoreWorkOrderListObj.fromDate= new Date();
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

						$scope.getStoreLists = [{
          					"store": "All",
							"id": 0
        				}],
						
						$scope.getStoreList=[];
						 $scope.getStorelist = function(){
							var URI = BASE_URL + ROOT_URL + API + "/procurements/WOCreation/dropdowns/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
							BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.status == 200){
									$scope.getStoreList = $scope.getStoreLists.concat(response.data.store);
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
								
								$scope.StoreWorkOrderListObj.vendorName=$item.vendorName;
								$scope.StoreWorkOrderListObj.vendorId= $item.id;
								console.log("StoreWorkOrder",$scope.StoreWorkOrderListObj);
								
								
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
								var URI = BASE_URL + ROOT_URL + API + "/procurements/workOrderRequest/dropdowns/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
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
						          
						            var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id +"/"+$scope.orgId ;
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
								
								
								$scope.cancelWOElement=function(){
									if($scope.ValidetReason()){
										var data = {
											"workorderRequestId" : $scope.id,
											"approvedBy":$scope.userId
										};
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/procurements/workOrder/requestChangeToCancel";
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											if(response.data.status == "success"){
												$rootScope.stopLoader();
												$scope.ModalDismiss('cancelWorkOrder');
												$scope.getListNewWOList();
											}
										});
									}
								}

								$scope.ValidetReason = function () {
									var isValid = true;

									if (!$scope.cancelReason) {
										$scope.isReasonValid = true;
										isValid = false;
									}
								
									return isValid;
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
								$('.modal-backdrop').remove()
							    $(document.body).removeClass("modal-open");
								cancelReasonService.GetReason().then(function(reason) {
									$scope.reasons = reason;
								});

								$scope.OpenPopup = function (action, popupName, data) {
									$scope.action = action;
									var popup = angular.element('#' + popupName);
									$scope.id = data.workorderRequestId;
									popup.modal('show');
								}

								$scope.ModalDismiss = function (modal) {
									var popup = angular.element('#' + modal);
									popup.modal('hide');
									$scope.rejectNote = undefined;
									$scope.cancelReason = undefined;
								}
                                
								 /***Chetana**/
							    $scope.printWorkOrderCreationReport = function (workOrder) {
							        var data = {
							            "appName": "HIS",
							            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							            "reportDesignFilePath": "get_WorkOrderCreationDetails.rpt",
							            "reportFormat": "PDF",
							            "exportFileName": "WorkOrderCreation",
							            "reportParams": [
							                {
							                    "paramName": "id",
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