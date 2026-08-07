angular
		.module('myApp')
		.controller(
				"addNewWorkorderLabourController",
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
							
							$scope.getStoreList=[];
							 $scope.getStorelist = function(){
								var URI = BASE_URL + ROOT_URL + API + "/procurements/WOCreation/dropdowns/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
								BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.status == 200){
										$scope.getStoreList = response.data.store;
									}
									console.log("getStorelist",$scope.getStoreList);
								});
							}
							 
							 $scope.getDocumentType = function(){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
									BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.documentTypeList = [];
											if (response.data.status == "success") {
												$scope.documentTypeList = response.data.listObject;
												 /*angular.forEach($scope.documentTypeList, function(value, key) 
												 {  
										 			$scope.corporateDocTypeId=value.documentTypeId;
												 });*/
											}
										});
								}
//								$scope.getDocumentType();
							 $scope.getRequestTypes = function(){ 
								 var URI=BASE_URL + ROOT_URL +"/api/procurements/WOCreation/dropdowns/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
							 GenericService.serviceAction("GET", URI, {}).then(
								        function (response) {
								        	  $scope.requestTypeslist=[];
								          $scope.requestTypeslist = response.data.requestType;
								          

								          $rootScope.stopLoader();
								        },
								        function (err) {
								          $rootScope.stopLoader();
								        });
							 
							 }
							 $scope.getRequestTypes();
							 
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
							
							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.StoreWorkOrder = {
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
									"roundoff":0,
									"requestDetailsDto":[],
									"requestTaxDetailsDto":[],
									"requestDocumentDto":[],
									"isReverseTax":false
									
								}
								
								
								/* var workOrderItemDetail = {
										  itemId : '',
										  itemName : "",
										  itemQuantity : "",
										  itemRate : '',
										  itemMrp:'',
										  itemDiscPercentage:'',
										  itemDiscAmount:'',
										  itemTax:'', 
										  itemNetAmount:'',
										  itemRemark:"",
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  orgId : $scope.orgId
										}
								 
								 $scope.workOrderItemDetailsList.push(workOrderItemDetail);*/
								$scope.StoreWorkOrder.storeId=$scope.storeId ;
								$scope.getStorelist();
								$scope.btnText="Save";
								$scope.Readonlybtn=false;
								$scope.getDocumentType();
//								 $scope.getOrderlist ();
								
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
									maxDate : new Date()
								};
							$scope.dateOptions1 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
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
							
						
//		$scope.taxpercObj ={};
						// to and from date picker
//						$scope.StoreWorkOrder.toDate= new Date();
//						$scope.StoreWorkOrder.fromDate= new Date();
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
								
								$scope.StoreWorkOrder.vendorName=$item.vendorName;
								$scope.StoreWorkOrder.vendorId= $item.id;
								console.log("StoreWorkOrder",$scope.StoreWorkOrder);
								
								
							 }
						 
							
							
							
							
							
						 
							 $scope.addnewItem= function(){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
								 
								 if($scope.StoreWorkOrder.storeId==undefined  || $scope.StoreWorkOrder.storeId==''){
									 growl.warning('Please select store.',
												{
													title : 'warning!'
												});
								     return false;
								 }
								 
								 
								 var workOrderItemDetail = {
										  itemId : '',
										  storeId: $scope.StoreWorkOrder.storeId,
										  itemName : "",
										  itemQuantity : "",
										  itemRate : '',
										  itemMrp:'',
										  itemDiscPercentage:0,
										  itemDiscAmount:0,
										  itemTaxId:'', 
										  itemTaxAmount:0,
										  itemNetAmount:0,
										  itemRemark:"",
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  orgId : $scope.orgId,
										  taxpercentObj : {}
										}
								 
								 $scope.workOrderItemDetailsList.push(workOrderItemDetail);
								 
							 }
							 
							 $scope.removenewItem=function (index){
								 $scope.workOrderItemDetailsList.splice(index, 1);
								 $scope.calculateFinalAmount();
							 }
						 
							 $scope.WorkorderuploadDocumentOBJList=[];
							 $scope.WorkorderuploadDocument=function(){
								 
								 if($scope.finalDocUploadList.length>0){
									 $scope.WorkorderuploadDocumentOBJList=$scope.finalDocUploadList;
								 }else{
									 $scope.WorkorderuploadDocumentOBJList=[];
									 var WorkorderuploadDocumentOBJ = {
											 workorderRequestId : '',
											  documentPath : "",
											  documentTypeId : '',
											  documentTypeName : '',
											  status : 'A',
											  createdDate : "",
											  createdBy : $scope.userId,
											  updatedDate : "",
											  updatedBy : $scope.userId,
											  unitId : $scope.unitId,
											  orgId : $scope.orgId,
											  file : {}
											};
									 
									 $scope.WorkorderuploadDocumentOBJList.push(WorkorderuploadDocumentOBJ);
							
								 }
								 
								 
								 		 
								 
							 }
						 
							 
							 $scope.addDocforworkorder=function(){
								 var WorkorderuploadDocumentOBJ = {
										 workorderRequestId : '',
										  documentPath : "",
										  documentTypeId : '',
										  documentTypeName : '',
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  orgId : $scope.orgId,
										  file : {}	
										};
								 
								 $scope.WorkorderuploadDocumentOBJList.push(WorkorderuploadDocumentOBJ);
								 
								 
							 }
							 $scope.removeDocforworkorder=function(index){
								 $scope.WorkorderuploadDocumentOBJList.splice(index, 1);
								 
								
							 }
						 
							 
							 $scope.uploadWorkorderuploadDocument = function()
							    {
								  $scope.finalDocUploadList=[];
								  var count=0;
								      angular.forEach($scope.WorkorderuploadDocumentOBJList, function(value, key) {  
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
												return false;
											} 
										 var ext = value.file.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
														{
															title : 'warning!'
														});
												return false;
											}
											$scope.finalDocUploadList.push(value);
//											 console.log("final list",$scope.finalDoc);
								});
								 
								 console.log("final doc list",$scope.finalDocUploadList);
								 if($scope.WorkorderuploadDocumentOBJList.length > $scope.finalDocUploadList.length){
									 return;
								 }else{
									$("#documentUploadModalForPOCreation").modal('hide');
								 }
							    }
						 
							 $scope.searchNewItemList= [];
							 $scope.removeAllDocforworkorder=function(){
								 $scope.WorkorderuploadDocumentOBJList=[];
								 $scope.finalDocUploadList=[];
							 }
							 	
							 
							 $scope.clearWorkorderObj=function(){
								 $scope.finalDocUploadList=[];
								 $scope.WorkorderuploadDocumentOBJList=[];
								 $scope.workOrderItemDetailsList=[];
								 $scope.AddOtherSavechargesList=[];
                             	 $scope.AddOtherchargesList=[];
                             	 
									$scope.totalTaxAmount=parseFloat(0);
									$scope.totalDiscAmount=parseFloat(0);
									$scope.totalNetAmount=parseFloat(0);
									$scope.btnText=="Save";
									
								     $scope.initCommon();
								     
							 }
							 
							 $scope.assetObject={};
							 $scope.AddAssets =function(){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
								 
								 if($scope.StoreWorkOrder.storeId==undefined || $scope.StoreWorkOrder.storeId==""  ){
									 growl.warning("Please select store",
												{
													title : 'warning!'
												});
										return;
								 }
								 if($scope.StoreWorkOrder.contractTypeId==undefined || $scope.StoreWorkOrder.contractTypeId==""  ){
									 growl.warning("Please select contract Type",
												{
													title : 'warning!'
												});
										return;
								 }
								 
								    var URI = BASE_URL + ROOT_URL + API + "/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId;
									BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.status == 200){
											$scope.assetCategoryList=[];
											$scope.assetCategoryList = response.data;
										}
										console.log("getOrderlist",$scope.getOrderlist);
									});
									$scope.searchNewItemList= [];
									$scope.itemName="";
									$scope.itemCode="";
								 $("#addNewItemWasteStore").modal('show');
								 $scope.assetObject = {
										  storeId:  $scope.StoreWorkOrder.storeId,
										  contractTypeId: $scope.StoreWorkOrder.contractTypeId,
										  assetCategoryId : '',
										  productCategoryId : '',
										  itemCode : "",
										  itemName : "",
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  orgId : $scope.orgId
										  
										};
								 
								 //global/assets/categories
								 
							 }
							
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
							 
							 
							 
							 $scope. getProductCategoryList = function (id) {
						          
						            var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id +"/"+$scope.organizationId;
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
                                 $scope.selectAllRows=false;
								 if($scope.itemName){
									 if($scope.itemName.length >= 3){		 
										$scope.searchNewItemList= [];
										$scope.searchNewItemids=[];
										var data = {
											"assetTypeId": 2,
											"assetCategoryId": $scope.assetObject.assetCategoryId,
											"productCategoryId":$scope.assetObject.productCategoryId,
											"storeId": $scope.assetObject.storeId,
											"itemName": $scope.itemName,
											"itemCode": $scope.itemCode
										};
											
										var URI = BASE_URL + ROOT_URL + "/api/global/items/unit" + "/" + $scope.unitId;
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											$scope.searchNewItemList = response.data;
											angular.forEach($scope.searchNewItemList,function(value, index){			
												value.isSelected = false;
											})
											console.log("$scope.searchNewItemList",$scope.searchNewItemList);
										});
									}
								}
								else if($scope.itemCode){
									if($scope.itemCode.length >= 3){
										$scope.searchNewItemList= [];
										$scope.searchNewItemids=[];
										var data = {
											"assetTypeId": 2,
											"assetCategoryId": $scope.assetObject.assetCategoryId,
											"productCategoryId":$scope.assetObject.productCategoryId,
											"storeId": $scope.assetObject.storeId,
											"itemName": $scope.itemName,
											"itemCode": $scope.itemCode
										};
											
										var URI = BASE_URL + ROOT_URL + "/api/global/items/unit" + "/" + $scope.unitId;
										GenericService.serviceAction("POST", URI, data).then(function(response) {
											$scope.searchNewItemList = response.data;
											angular.forEach($scope.searchNewItemList,function(value, index){			
												value.isSelected = false;
											})
											console.log("$scope.searchNewItemList",$scope.searchNewItemList);
										});
									}
								}
								else{
									$scope.searchNewItemList = [];	
								}
							}
							 $scope.selectAllItems=function()
							 {
								 if($scope.selectAllRows==true)
									 {
									 angular.forEach($scope.searchNewItemList,function(value, index){
										 value.isSelected=true;
										})
									 }
								 else
									 {
									 angular.forEach($scope.searchNewItemList,function(value, index){
										 value.isSelected=false;
										})
									 }
							 }
							 $scope.selectItem=function()
							 {
								 var count=0;
								 angular.forEach($scope.searchNewItemList,function(value, index){
									 if(value.isSelected==true)
										 {
										 count++;
										 }
									});
								 if(count!=$scope.searchNewItemList.length)
									 {
									 $scope.selectAllRows=false;
									 }
								 if(count==$scope.searchNewItemList.length)
								 {
								 $scope.selectAllRows=true;
								 }
							 }
							 $scope.addItems=function(){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
								 $scope.selectedItemList=[];
								 $scope.otherChargesList=[];
								 
								 console.log("$scope.searchNewItemList",$scope.searchNewItemList);
								 angular.forEach($scope.searchNewItemList,function(value, index){
										if(value.isSelected == true){
											$scope.selectedItemList.push(value);
										}
									})
									
									if($scope.selectedItemList.length==0){
										 growl.warning("Please select At least one Item  .",
													{
														title : 'warning!'
													});
						                  return false;
										
									}
								 if($scope.workOrderItemDetailsList.length>0){
									 var duplicatefound=0;
									 
									 angular.forEach($scope.selectedItemList,function(valueselected, indexselected){
									      angular.forEach($scope.workOrderItemDetailsList,function(value, index){
									    	      if(valueselected.itemId==value.itemId){
									    	    	  duplicatefound++;
									    	      }
										 
									      })
									 
									 })
									 if(duplicatefound>0){
										 growl.warning("Item Already Added",
													{
														title : 'warning!'
													});
										 
										 return false;
									 }
									 
								 }
								 
								 
								 
								 
								 angular.forEach($scope.selectedItemList,function(value, index){
									 var workOrderItemDetail = {
											  itemId :value.itemId ,
											  storeId:  $scope.StoreWorkOrder.storeId,
											  itemName : value.itemName,
											  itemQuantity : "",
											  itemRate : '',
											  itemMrp:'',
											  itemDiscPercentage:0,
											  itemDiscAmount:0,
											  itemTaxId:'',
											  itemTax:'',
											  itemTaxAmount:0,
											  itemNetAmount:0,
											  itemRemark:"",
											  status : 'A',
											  createdDate : "",
											  createdBy : $scope.userId,
											  updatedDate : "",
											  updatedBy : $scope.userId,
											  unitId : $scope.unitId,
											  orgId : $scope.orgId,
											  taxpercentObj:{}
											}
									        $scope.workOrderItemDetailsList.push(workOrderItemDetail);
										
									})
								    $scope.itemName="";
									$scope.itemCode="";
								 $("#addNewItemWasteStore").modal('hide');
							 }
							 
							 $scope.getcontractTypeList=function(contractTypeId){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
								 $scope.AddOtherSavechargesList=[];
                             	 $scope.AddOtherchargesList=[];
                             	 $scope.AddOtherchargeSave();
                             	 
                             	$scope.totalTaxAmount=parseFloat(0);
								$scope.totalDiscAmount=parseFloat(0);
								$scope.totalNetAmount=parseFloat(0);
								
								 if(contractTypeId==1){
									 $scope.workOrderItemDetailsList=[];
								 }
								 
								 if(contractTypeId==2){
									 $scope.workOrderItemDetailsList=[];
									 var workOrderItemDetail = {
											  itemId :'' ,
											  storeId:  $scope.StoreWorkOrder.storeId,
											  itemName : '',
											  itemQuantity : "",
											  itemRate : '',
											  itemMrp:'',
											  itemDiscPercentage:0,
											  itemDiscAmount:0,
											  itemTaxId:'',
											  itemTax:'',
											  itemTaxAmount:0,
											  itemNetAmount:0,
											  itemRemark:"",
											  status : 'A',
											  createdDate : "",
											  createdBy : $scope.userId,
											  updatedDate : "",
											  updatedBy : $scope.userId,
											  unitId : $scope.unitId,
											  orgId : $scope.orgId,
											  taxpercentObj:{}
											}
									        $scope.workOrderItemDetailsList.push(workOrderItemDetail);
									 
									 
								 }
								 
								 
							 }
							
							 
							 $scope.selectedTax="";
							 $scope.setTaxWorkorderItem =function(taxPer,workOrderItemDetail,selectedindex){
								 
								 workOrderItemDetail.itemTax=parseFloat (taxPer.taxPercentage );
								 workOrderItemDetail.itemTaxId=taxPer.id;
								 $scope.calculate(workOrderItemDetail,selectedindex,'TAX')
								}
							 
							 
							 $scope.searchWorkorderItem = function(searchtext){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
							      if (searchtext.length < 1) return;
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
							    	    //   itemCode:searchtext,
							    	    //   isConsignment : false // added by minesh for resolved HIS-8808 Sub Module: Consignment - Op Balance item not display
							      };
							      var URI = BASE_URL + ROOT_URL + API + "/global/items/unit/"+ $scope.unitId;
							      return GenericServiceParamHeader.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.status == 200){
											return response.data;
										}
									
									});
							      
							    }							 

							 $scope.workOrderCreationStagedListData=[];
								$scope.selectSearchItemName = function($item, $model, $label,elementindex){
									
									var itemfound=0;
									angular.forEach($scope.workOrderItemDetailsList,function(value, index){
										if(value.itemId!=undefined && value.itemId!='' ){
											if(value.itemId==$item.itemId){
												itemfound++;
											}
										}
									})
									if(itemfound>0){
										angular.forEach($scope.workOrderItemDetailsList,function(value, index){
											 if(elementindex== index){
												 value.itemId='';
												 value.itemName='';
											 }
										 })
										 growl.warning("Item Already Added",
													{
														title : 'warning!'
													});
									}else{
										angular.forEach($scope.workOrderItemDetailsList,function(value, index){
											if(elementindex== index){
												value.itemId=$item.itemId;
												value.itemName=$item.itemName;
											}
										})
									}
									if(event.which == 9){
										document.getElementById('qty').focus();
										event.preventDefault();
										$scope.focusInput=true;
									}
								 }
								$scope.closePopUp=function(flag)
								{
                                    $scope.totalNetAmount=parseFloat( parseFloat($scope.totalNetAmount) - parseFloat($scope.otherChargeAmount)).toFixed(2);
                                    $scope.otherChargeAmount=parseFloat(0);
                                    angular.forEach($scope.AddOtherchargesList,function(value, index){
										value.otherChargeAmount=value.otherChargeAmountOld;
									});
                                    $scope.AddOtherchargeSave(flag);
								}
								$scope.calculate=function(workOrderItemDetail,index,blurpoint){
									
									if(workOrderItemDetail.itemQuantity!=undefined && workOrderItemDetail.itemQuantity!="" && workOrderItemDetail.itemRate!=undefined && workOrderItemDetail.itemRate!="" )
									{
										workOrderItemDetail.itemMrp=parseFloat( parseFloat(workOrderItemDetail.itemQuantity) * parseFloat(workOrderItemDetail.itemRate)).toFixed(2);	;
									
										if(workOrderItemDetail.itemDiscAmount!=undefined &&  workOrderItemDetail.itemDiscAmount!=""  && blurpoint=='DA' ){
											
											if(parseFloat(workOrderItemDetail.itemDiscAmount) > workOrderItemDetail.itemMrp ){
												growl.warning("Please Enter Valid Discount Amount  .",
														{
															title : 'warning!'
														});
												workOrderItemDetail.itemDiscPercentage=0;
												workOrderItemDetail.itemDiscAmount=0;
											}else{
												if(workOrderItemDetail.itemDiscAmount==undefined || workOrderItemDetail.itemDiscAmount==null || workOrderItemDetail.itemDiscAmount=="")
												{
												workOrderItemDetail.itemDiscAmount=0;
												}
												workOrderItemDetail.itemDiscPercentage= parseFloat(  parseFloat(workOrderItemDetail.itemDiscAmount) /(parseFloat(workOrderItemDetail.itemMrp) )* parseFloat(100));
											}
											
										}
										
										if(workOrderItemDetail.itemDiscPercentage!=undefined &&  workOrderItemDetail.itemDiscPercentage!=""  && blurpoint=='DP'){
											if(parseFloat(workOrderItemDetail.itemDiscPercentage) > 100){
												growl.warning("Please Enter Valid Discount Percentage  .",
														{
															title : 'warning!'
														});
												workOrderItemDetail.itemDiscPercentage=0;
												workOrderItemDetail.itemDiscAmount=0;
												
											}else{
												if(workOrderItemDetail.itemDiscPercentage==undefined || workOrderItemDetail.itemDiscPercentage==null || workOrderItemDetail.itemDiscPercentage=="")
												{
												workOrderItemDetail.itemDiscPercentage=0;
												}
												workOrderItemDetail.itemDiscAmount= parseFloat(  parseFloat(workOrderItemDetail.itemMrp) *(parseFloat(workOrderItemDetail.itemDiscPercentage) )/ parseFloat(100)).toFixed(2);	
											}
											
										}
										
	                                       if(workOrderItemDetail.itemTax!=undefined &&  workOrderItemDetail.itemTax!=""  && blurpoint=='TAX'){
											
											if(workOrderItemDetail.itemDiscAmount==0){
												workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
											}else{
												
												workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp - workOrderItemDetail.itemDiscAmount) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
											}
											
										}
										
										if(workOrderItemDetail.itemDiscAmount!=undefined &&  workOrderItemDetail.itemDiscAmount!=""  ){
												
												if(parseFloat(workOrderItemDetail.itemDiscAmount) > workOrderItemDetail.itemMrp ){
													growl.warning("Please Enter Valid Discount Amount  .",
															{
																title : 'warning!'
															});
													workOrderItemDetail.itemDiscPercentage=0;
													workOrderItemDetail.itemDiscAmount=0;
												}else{
													if(workOrderItemDetail.itemDiscPercentage==undefined || workOrderItemDetail.itemDiscPercentage==null || workOrderItemDetail.itemDiscPercentage=="")
													{
													workOrderItemDetail.itemDiscAmount=0;
													}
													workOrderItemDetail.itemDiscPercentage= parseFloat(  parseFloat(workOrderItemDetail.itemDiscAmount) /(parseFloat(workOrderItemDetail.itemMrp) )* parseFloat(100)).toFixed(2);	
												}
												
											}
											
										
										if(workOrderItemDetail.itemDiscPercentage!=undefined &&  workOrderItemDetail.itemDiscPercentage!="" ){
											if(parseFloat(workOrderItemDetail.itemDiscPercentage) > 100 ||	parseFloat(workOrderItemDetail.itemDiscPercentage )<0){
												growl.warning("Please Enter Valid Discount Percentage  .",
														{
															title : 'warning!'
														});
												workOrderItemDetail.itemDiscPercentage=0;
												workOrderItemDetail.itemDiscAmount=0;
												
											}else{
												if(workOrderItemDetail.itemDiscAmount==undefined || workOrderItemDetail.itemDiscAmount==null || workOrderItemDetail.itemDiscAmount=="")
												{
												workOrderItemDetail.itemDiscPercentage=0;
												}
												workOrderItemDetail.itemDiscAmount= parseFloat(  parseFloat(workOrderItemDetail.itemMrp) *(parseFloat(workOrderItemDetail.itemDiscPercentage) )/ parseFloat(100)).toFixed(2);	
											}
											
										}
										
										
                                       
                                        if(workOrderItemDetail.itemTax!=undefined &&  workOrderItemDetail.itemTax!=""  ){
											//for tax already given
											if(workOrderItemDetail.itemDiscAmount==0){
												workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
											}else{
												
												workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp - workOrderItemDetail.itemDiscAmount) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
											}
											
										}
                                        
									
                                        if(workOrderItemDetail.itemTax==0 &&  blurpoint=='TAX'){
											
											workOrderItemDetail.itemTaxAmount =parseFloat(0);
										}
									}
									
									$scope.calculateFinalAmount();
									
								}	
								
								
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
										 var discamt=parseFloat(0);
										 var taxamt=parseFloat(0);
										if( value.itemDiscAmount!=undefined && value.itemDiscAmount!='' && value.itemDiscAmount!=0){
											discamt=parseFloat(parseFloat( discamt )+parseFloat(value.itemDiscAmount)).toFixed(2);
										}
										if( value.itemTaxAmount!=undefined && value.itemTaxAmount!='' && value.itemTaxAmount!=0  ){
											taxamt=parseFloat( parseFloat(taxamt)+parseFloat(value.itemTaxAmount)).toFixed(2);
										}
										if(value.itemMrp!=undefined && value.itemMrp!='')
										value.itemNetAmount= parseFloat((parseFloat(value.itemMrp )+ parseFloat( taxamt))-parseFloat(discamt) ).toFixed(2);
										
										$scope.totalTaxAmount= parseFloat( parseFloat($scope.totalTaxAmount) + parseFloat(taxamt)).toFixed(2);
										$scope.totalDiscAmount=parseFloat( parseFloat($scope.totalDiscAmount) +parseFloat(discamt)).toFixed(2);
											
											 $scope.totalNetAmount=parseFloat( parseFloat($scope.totalNetAmount)+ parseFloat(value.itemNetAmount)).toFixed(2);
										
											 
										
										
									 })
									 
									 if($scope.otherChargeAmount!=0){
										 $scope.totalAmount= parseFloat( parseFloat($scope.totalNetAmount)+ parseFloat($scope.totalDiscAmount) - parseFloat($scope.totalTaxAmount)).toFixed(2);
										 $scope.totalNetAmount=parseFloat( parseFloat($scope.totalNetAmount) + parseFloat($scope.otherChargeAmount)).toFixed(2);
										           
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
										 $scope.totalAmount=parseFloat( parseFloat($scope.totalNetAmount) + parseFloat($scope.totalDiscAmount) - parseFloat($scope.totalTaxAmount)).toFixed(2);
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
							 
							 
//								/api/globals/other/charges/
								
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
							 
								
								
								
								
								$scope.AddOthercharges =function(){
									
									if($scope.AddOtherSavechargesList.length>0){
										angular.forEach($scope.AddOtherchargesList,function(value, index){
											value.otherChargeAmountOld=value.otherChargeAmount!=undefined && value.otherChargeAmount!=null && value.otherChargeAmount!='' ? value.otherChargeAmount :0;
										});
										
									}else{
										$scope.AddOtherchargesList=[];
										 var AddOtherchargesObj = {
												  workorderRequestId : '',
												  otherchargeTypeId : '',
												  otherChargeAmount : '',
												  status : 'A',
												  createdDate : "",
												  createdBy : $scope.userId,
												  updatedDate : "",
												  updatedBy : $scope.userId,
												  unitId : $scope.unitId,
												  orgId : $scope.orgId,
												  otherChargeAmountOld:0
										 }
										 $scope.AddOtherchargesList.push(AddOtherchargesObj);
										
									}
									
									 $("#otherCharges_Modal").modal('show');
									
									
								}
								
								
								$scope.AddOtherChargeObj =function(){
									
									var AddOtherchargesObj = {
											  workorderRequestId : '',
											  otherchargeTypeId : '',
											  otherChargeAmount : '',
											  status : 'A',
											  createdDate : "",
											  createdBy : $scope.userId,
											  updatedDate : "",
											  updatedBy : $scope.userId,
											  unitId : $scope.unitId,
											  orgId : $scope.orgId
									 }
									 $scope.AddOtherchargesList.push(AddOtherchargesObj);
								}
								
								$scope.RemoveOtherChargeObj=function(row){
									 $scope.AddOtherchargesList.splice(row, 1);
									 $scope.getOtherchargeamountPopup();
								}
								
								
								 $scope.AddOtherSavechargesList=[];
								 $scope.otherChargeAmount=parseFloat(0);
								 
								$scope.AddOtherchargeSave =function(flag){
									$scope.isFromPopUpClose=flag;
									 $scope.otherChargeAmount=parseFloat(0);
									 var countTypeId=0;
									 var countotheramount=0; 
									 $scope.AddOtherSavechargesList=[];
									 angular.forEach($scope.AddOtherchargesList,function(value, index){
										 if(value.otherchargeTypeId!=undefined && value.otherchargeTypeId!=''  ){
											 if(value.otherChargeAmount!=undefined && value.otherChargeAmount!=''  && value.otherChargeAmount!=0 ){
												 $scope.AddOtherSavechargesList.push(value);
												 $scope.otherChargeAmount=parseFloat(parseFloat($scope.otherChargeAmount)+parseFloat(value.otherChargeAmount)).toFixed(2);
											 }else{
												 $scope.otherChargeAmount=parseFloat(parseFloat($scope.otherChargeAmount)+parseFloat(value.otherChargeAmount!=undefined && value.otherChargeAmount!=null && value.otherChargeAmount!='' ? value.otherChargeAmount :0)).toFixed(2);
												 countotheramount++;
												 $scope.otherChargeAmountpopup=$scope.otherChargeAmount;
												 $scope.AddOtherchargesList.splice(index, 1);
												 $scope.calculateFinalAmount();
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
									 
									 if(countotheramount>0 && $scope.isFromPopUpClose!=true){
											growl.warning("Please enter Valid Charge Amount .",
													{
														title : 'warning!'
													});
											return;
										} 
									
										 $("#otherCharges_Modal").modal('hide');
										 $scope.calculateFinalAmount();
										 
										 $scope.otherChargeAmountpopup=$scope.otherChargeAmount;
								}
								
								$scope.otherChargeAmountpopup=parseFloat(0);
								$scope.getOtherchargeamountPopup=function(){
									$scope.otherChargeAmountpopup=parseFloat(0);
									 var countTypeId=0;
									 var countotheramount=0; 
									 $scope.AddOtherSavechargesList=[];
									 angular.forEach($scope.AddOtherchargesList,function(value, index){
										 if(value.otherchargeTypeId!=undefined && value.otherchargeTypeId!=''  ){
											 if(value.otherChargeAmount!=undefined && value.otherChargeAmount!=''  && value.otherChargeAmount!=0 ){
												 $scope.otherChargeAmountpopup=parseFloat (parseFloat($scope.otherChargeAmountpopup)+parseFloat(value.otherChargeAmount)).toFixed(2);;
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
									 
									 if(countotheramount>0 && $scope.isFromPopUpClose!=true){
											growl.warning("Please enter Valid Charge Amount .",
													{
														title : 'warning!'
													});
											return;
										} 
									
								}
								
								
                                $scope.OtherchargeCancel =function(){
                                	$scope.AddOtherSavechargesList=[];
                                	$scope.AddOtherchargesList=[];
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
                                	diff=diff.toFixed(2);
                                	$scope.workOrderCreationStagedListData[$scope.workOrderCreationStagedListData.length-1].percentage=parseFloat($scope.workOrderCreationStagedListData[$scope.workOrderCreationStagedListData.length-1].percentage)+parseFloat(diff);
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
                                    		stageName: "-",
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
                                $scope.uploadEncounterDoc = function(Obj)
    							{
    								//$('#uploadModal').modal('hide');
    								 var fd = new FormData();
    							        fd.append('workorderRequestId', Obj.workorderRequestId);
    							        fd.append('documentTypeId', Obj.documentTypeId);
    							        fd.append('createdBy',Obj.createdBy );
    							        fd.append('updatedBy', Obj.updatedBy);
    							        fd.append('unitId', Obj.unitId);
    							        fd.append('orgId', Obj.orgId);
    							        fd.append('file',Obj.file );
    							        var URI1 = BASE_URL + ROOT_URL + "/api/procurements/workOrder/workorderRequestDocument";
    							        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
    									{	
    										return response;
    									});
    							}
									
							 
                                $scope.saveWorkOrderCreationRequest=function(){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
                                	
                                	console.log("saveObj", $scope.StoreWorkOrder);
                                	 if($scope.StoreWorkOrder.storeId==undefined || $scope.StoreWorkOrder.storeId==''){
   							    	  growl.warning("Please select store",
   												{
   													title : 'warning!'
   												});
   										return;
   							          }
                                	 
                                	 if($scope.StoreWorkOrder.vendorId==undefined || $scope.StoreWorkOrder.vendorId==''){
      							    	  growl.warning("Please select vendor",
      												{
      													title : 'warning!'
      												});
      										return;
      							          }
//                                	 
                                	 if($scope.StoreWorkOrder.deliveryDate==undefined || $scope.StoreWorkOrder.deliveryDate==""){
      							    	  growl.warning("Please select delivary date ",
      												{
      													title : 'warning!'
      												});
      										return;
      							          }
                                	 
                                	 if($scope.StoreWorkOrder.billTypeId==undefined || $scope.StoreWorkOrder.billTypeId==''){
    							    	  growl.warning("Please select Bill type",
    												{
    													title : 'warning!'
    												});
    										return;
    							          }
                                	 
                                	
                                	 
                                	 
                                	 if($scope.StoreWorkOrder.contractTypeId==undefined || $scope.StoreWorkOrder.contractTypeId==''){
    							    	  growl.warning("Please select Order Type",
    												{
    													title : 'warning!'
    												});
    										return;
    							          }
                                	 
                                	 if($scope.StoreWorkOrder.againstTypeId==undefined || $scope.StoreWorkOrder.againstTypeId==''){
   							    	  growl.warning("Please select Against Type",
   												{
   													title : 'warning!'
   												});
   										return;
   							          }
                                	
                                	if( $scope.workOrderItemDetailsList.length==0){
                                		 growl.warning("Please select At least one Item Details .",
													{
														title : 'warning!'
													});
						                  return false;
										
                                	}
                                	 //validation check detailslist
                                	 var itemnamevalidation=0;
                                	 var itemQuantityvalidation=0;
                                	 var itemMrpvalidation=0;
                                	 var itemRatevalidation=0;
                                	 
                                	 angular.forEach($scope.workOrderItemDetailsList,function(value, index){
                                		 if(value.workOrderCreationStagedListData==undefined || value.workOrderCreationStagedListData==null || value.workOrderCreationStagedListData.length==0)
                                			 {
                                			 value.workOrderCreationStagedListData=[];
                                			 var obj=
                                				 {
                                					stagedWorkorderId:null,
                                             		stageName: "-",
                                             		percentage: 100,
                                				 }
                                			 value.workOrderCreationStagedListData.push(obj);
                                			 }
                                		 
                                		 if(value.serviceName==null && (value.itemName==undefined || value.itemName=='')){
                                			 itemnamevalidation++;
                                		 }
                                		 if(value.itemQuantity==undefined || value.itemQuantity==''){
                                			 itemQuantityvalidation++;
                                		 }
                                		 if(value.itemRate==undefined || value.itemRate==''){
                                			 itemRatevalidation++;
                                		 }
                                		 
                                		 
                                		 if(value.itemMrp==undefined || value.itemMrp==''){
                                			 itemMrpvalidation++;
                                		 }
                                		 
                                	 })
                                	 
                                	 if(itemnamevalidation>0){
                                		 growl.warning("Please enter valid Item name .",
													{
														title : 'warning!'
													});
						                  return false;
                                	 }
                                	 
                                	 if(itemQuantityvalidation>0){
                                		 growl.warning("Please enter valid Item quantity .",
													{
														title : 'warning!'
													});
						                  return false;
                                	 }
                                	 
                                	 if(itemRatevalidation>0){
                                		 growl.warning("Please enter valid Item Rate .",
													{
														title : 'warning!'
													});
						                  return false;
                                	 }
                                	 
                                	 
                                	 
                                	 if(itemMrpvalidation>0){
                                		 
                                		 growl.warning("Please enter valid Item Quantity and Rate.",
													{
														title : 'warning!'
													});
						                  return false;
                                	 }
                                	
                                	
                                	 $scope.StoreWorkOrder.requestDetailsDto=$scope.workOrderItemDetailsList;
                                	 $scope.StoreWorkOrder.requestTaxDetailsDto= $scope.AddOtherSavechargesList;
                                	 $scope.StoreWorkOrder.requestDocumentDto=$scope.finalDocUploadList;
                                	 
                                	 
                                	 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.totalNetAmount-  $scope.otherChargeAmount);
                                	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.otherChargeAmount);
                                	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.totalTaxAmount);
                                	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.totalDiscAmount);
                                	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.totalNetAmount);
                                	 $scope.StoreWorkOrder.roundoff= $scope.roundoff;
                                	 $rootScope.startLoader();
                                	 $scope.StoreWorkOrder.deliveryDate=   moment($scope.StoreWorkOrder.deliveryDate).format('DD-MM-YYYY HH:mm:ss')
                                	console.log("finalsaveObj", $scope.StoreWorkOrder);
                                	
                                	var URI = BASE_URL + ROOT_URL + API + "/procurements/workOrder/saveCreationRequest";
                                	GenericService.serviceAction (METHOD_POST,URI, $scope.StoreWorkOrder
                                     ).then(function(response){
  										if(response.status == 200){
  											 $rootScope.stopLoader();
  										          if($scope.btnText=="Update"){
  										        	growl.success("updated successfully",
  															{
  																title : 'Success!'
  															});
  										          }else{
  										        	growl.success("saved successfully",
  															{
  																title : 'Success!'
  															});
  										          }	
  										        $state.go('workOrderCreation',
  														{}
  														);	  
  											      
  											   if($scope.finalDocUploadList.length>0) {
  												     angular.forEach($scope.finalDocUploadList,function(value, index){
  												    	value.workorderRequestId=response.data.object.workorderRequestId;
  												    	var uploadcount=0;
  												    	var promise = $scope.uploadEncounterDoc(value);
  												    	  promise.then(function(response) 
  																{
  																  if(response.data.status == "success")
  																	  {
  																	     uploadcount++;
  																	    if($scope.finalDocUploadList.length==uploadcount)
  																		 {
  																	    	
  																		   growl.success("Document Uploaded Successfully.",
  																					{
  																						title : 'Success!'
  																					});
  																		   
  																		    $scope.finalDocUploadList=[];
  																		 }
  																      }
  																  else 
  																	{  
  																		growl.error('Something went wrong.',
  																				{
  																					title : 'Error!'
  																				});
//  																		$scope.finalDocUploadList;
  																		 
  																	}
  															    });
  												    	 
  												     })
  												   
  										        $scope.clearWorkorderObj();
  												     
  												   $scope.otherChargeAmount=parseFloat(0);
  												   $scope.totalNetAmount=parseFloat(0);
  												   $scope.totalTaxAmount=parseFloat(0);
  												   $scope.totalDiscAmount=parseFloat(0);
  												 
  											   }else{
  												   $scope.otherChargeAmount=parseFloat(0);
												   $scope.totalNetAmount=parseFloat(0);
												   $scope.totalTaxAmount=parseFloat(0);
												   $scope.totalDiscAmount=parseFloat(0);

												   $scope.finalDocUploadList=[];
  												 $scope.clearWorkorderObj();
  											   }
  										}else{
  											    $scope.clearWorkorderObj();
  										}
  									
  									});
                                	
                                	
                                }
							 
                                
                           	 $scope.openImage = function(docobj)
  			               { 
                           		 if($scope.common.bucketFlag == 2){
                           			$rootScope.startLoaderPopup();
      								//var href = $rootScope.currentHrefDoccument();
      								$.fancybox.open({
      									width: 700, // or whatever
      									height: 420,
      									href:docobj.documentPath,
      									type: 'iframe'
      								});
                           		 }                           		 
                           		 else{
  								$rootScope.startLoaderPopup();
  								var href = $rootScope.currentHrefDoccument();
  								$.fancybox.open({
  									width: 700, // or whatever
  									height: 420,
  									href:href+docobj.documentPath,
  									type: 'iframe'
  								});
  			               }
  			          $rootScope.stopLoaderPopup();
  			         }
							 
                              
							 
                                console.log("$stateParams.resultCommonObj",$stateParams.resultworkOrderDetailsCommonObj);
    							$scope.common = $stateParams.resultworkOrderDetailsCommonObj;
    							console.log(	$scope.common);
    							if($scope.common.workorderRequestId!=undefined){
    								$scope.btnText="Update";
    								 
    								$scope.StoreWorkOrder.workorderRequestId=$scope.common.workorderRequestId;
    								$scope.StoreWorkOrder.workorderRequestNumber=$scope.common.workorderRequestNumber;
    								$scope.StoreWorkOrder.billTypeId=$scope.common.billTypeId;
    								$scope.StoreWorkOrder.storeId=$scope.common.storeId;
    								$scope.StoreWorkOrder.vendorId=$scope.common.vendorId;
    								$scope.StoreWorkOrder.vendorName=$scope.common.vendorName;
    								$scope.StoreWorkOrder.againstTypeId=$scope.common.againstTypeId;
    								$scope.StoreWorkOrder.isReverseTax = $scope.common.isReverseTax;
    								$scope.StoreWorkOrder.billId=$scope.common.billId;
    								$scope.StoreWorkOrder.encounterId = $scope.common.encounterId;
    								$scope.StoreWorkOrder.admissionId = $scope.common.admissionId;
    								$scope.StoreWorkOrder.deliveryDate= moment($scope.common.deliveryDate,'DD/MM/YYYY').toDate();
    								$scope.StoreWorkOrder.contractTypeId=$scope.common.contractTypeId;
    								
    								
    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
    								
    								
    								console.log("update data=",	$scope.StoreWorkOrder);
    								
    								if($scope.common.requestDetailsDto.length>0){
    									 $scope.workOrderItemDetailsList=[];
    									angular.forEach($scope.common.requestDetailsDto,function(value, index){
    										var workOrderItemDetail = {};
       									 
       									 workOrderItemDetail.itemId=  value.itemId == null ?  ''  : value.itemId  ;
       									 workOrderItemDetail.storeId=  $scope.StoreWorkOrder.storeId;
       									 workOrderItemDetail.itemName= value.itemName;
       									 workOrderItemDetail.serviceName= value.serviceName;
       									 workOrderItemDetail.itemQuantity=value.itemQuantity == null ?  ''  : value.itemQuantity  ; 
       									 workOrderItemDetail.itemRate=parseFloat( value.itemRate == null ?  ''  : value.itemRate  ); 
       									 workOrderItemDetail.itemMrp=parseFloat(value.itemMrp == null ?  ''  : value.itemMrp ) ; 
       									 workOrderItemDetail.itemDiscPercentage=value.itemDiscPercentage == null ?  ''  : value.itemDiscPercentage  ; 
       									 workOrderItemDetail.itemDiscAmount=parseFloat(value.itemDiscAmount == null ?  ''  : value.itemDiscAmount)  ;
       									 workOrderItemDetail.itemTaxId=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
//       									 $scope.taxpercObj.id=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
       									 taxpercentDetailsObj={};
       									 taxpercentDetailsObj.id=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
       									 taxpercentDetailsObj.taxPercentage= value.itemTax == null ?  ''  : value.itemTax  ;
       									 workOrderItemDetail.taxpercentObj=taxpercentDetailsObj;
       									 workOrderItemDetail.itemTax= value.itemTax == null ?  ''  : value.itemTax  ;
       									 workOrderItemDetail.itemTaxAmount=parseFloat(value.itemTaxAmount == null ?  ''  : value.itemTaxAmount ) ;
       									 workOrderItemDetail.itemNetAmount=parseFloat(value.itemNetAmount == null ?  ''  : value.itemNetAmount ) ;
       									 workOrderItemDetail.itemRemark=value.itemRemark == null ?  ""  : value.itemRemark  ;
       									 workOrderItemDetail.status='A' ;
       									 workOrderItemDetail.createdDate="";
       									 workOrderItemDetail.createdBy = $scope.userId;
       									 workOrderItemDetail.updatedDate = "";
       									 workOrderItemDetail.updatedBy = $scope.userId;
       									 workOrderItemDetail.unitId = $scope.unitId;
       									 workOrderItemDetail.orgId = $scope.orgId;
       									workOrderItemDetail.workOrderCreationStagedListData=value.workOrderCreationStagedListData;
       									 workOrderItemDetail.serviceName = value.serviceName;
       									 workOrderItemDetail.orderDetailsId = value.orderDetailsId;
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
            										$scope.getOtherchargeamountPopup();
        										  });
        										$scope.AddOtherchargeSave();
        									}
    										
    										
    									}
    									$scope.documentlist=[];
    									if($scope.common.requestDocumentDto!=null){
    										$scope.documentlist=[];
    										$scope.documentlist=$scope.common.requestDocumentDto;
    									}
    									
    								
    									
    										
    										
    									}
    								}
    								
    							
    							//read only
    							console.log("$stateParams.resultworkOrderReadOnlyDetailsCommonObj",$stateParams.resultworkOrderReadOnlyDetailsCommonObj);
    							$scope.common = $stateParams.resultworkOrderReadOnlyDetailsCommonObj;
    							console.log(	$scope.common);
    							if($scope.common.workorderRequestId!=undefined){
    								$scope.btnText="Save";
    								$scope.Readonlybtn=true;
    								$scope.StoreWorkOrder.workorderRequestId=$scope.common.workorderRequestId;
    								$scope.StoreWorkOrder.workorderRequestNumber=$scope.common.workorderRequestNumber;
    								$scope.StoreWorkOrder.billTypeId=$scope.common.billTypeId;
    								$scope.StoreWorkOrder.storeId=$scope.common.storeId;
    								$scope.StoreWorkOrder.vendorId=$scope.common.vendorId;
    								$scope.StoreWorkOrder.vendorName=$scope.common.vendorName;
    								
    								$scope.StoreWorkOrder.deliveryDate= moment($scope.common.deliveryDate,'DD/MM/YYYY').toDate();
    								$scope.StoreWorkOrder.contractTypeId=$scope.common.contractTypeId;
    								$scope.StoreWorkOrder.againstTypeId=$scope.common.againstTypeId;
    								$scope.StoreWorkOrder.isReverseTax = $scope.common.isReverseTax;
    								$scope.StoreWorkOrder.billId=$scope.common.billId;
    								$scope.StoreWorkOrder.encounterId = $scope.common.encounterId;
    								$scope.StoreWorkOrder.admissionId = $scope.common.admissionId;
    								
    							 $scope.StoreWorkOrder.totalAmount=parseFloat($scope.common.totalAmount ==null ? 0 : $scope.common.totalAmount);
                               	 $scope.StoreWorkOrder.otherChargeAmount=parseFloat( $scope.common.otherChargeAmount ==null ? 0 : $scope.common.otherChargeAmount);
                               	 $scope.StoreWorkOrder.totalTaxAmount=parseFloat($scope.common.totalTaxAmount ==null ? 0 : $scope.common.totalTaxAmount);
                               	 $scope.StoreWorkOrder.totalDiscountAmount=parseFloat($scope.common.totalDiscountAmount  ==null ? 0 : $scope.common.totalDiscountAmount );
                               	 $scope.StoreWorkOrder.totalNetAmount=parseFloat($scope.common.totalNetAmount  ==null ? 0 : $scope.common.totalNetAmount);
                                 $scope.StoreWorkOrder.paymentTerms= $scope.common.paymentTerms == null ?  ''  : $scope.common.paymentTerms;
                                 $scope.StoreWorkOrder.guaranteeWarranty =$scope.common.guaranteeWarranty == null ?  ''  : $scope.common.guaranteeWarranty;
    								
    								
    								
    								console.log("update data=",	$scope.StoreWorkOrder);
    								
    								if($scope.common.requestDetailsDto.length>0){
    									 $scope.workOrderItemDetailsList=[];
    									angular.forEach($scope.common.requestDetailsDto,function(value, index){
    										var workOrderItemDetail = {};
       									 
       									 workOrderItemDetail.itemId=  value.itemId == null ?  ''  : value.itemId  ;
       									 workOrderItemDetail.storeId=  $scope.StoreWorkOrder.storeId;
       									 workOrderItemDetail.itemName= value.itemName;
       									 workOrderItemDetail.serviceName= value.serviceName;
       									 workOrderItemDetail.itemQuantity=value.itemQuantity == null ?  ''  : value.itemQuantity  ; 
       									 workOrderItemDetail.itemRate=parseFloat( value.itemRate == null ?  ''  : value.itemRate  ); 
       									 workOrderItemDetail.itemMrp=parseFloat(value.itemMrp == null ?  ''  : value.itemMrp ) ; 
       									 workOrderItemDetail.itemDiscPercentage=value.itemDiscPercentage == null ?  ''  : value.itemDiscPercentage  ; 
       									 workOrderItemDetail.itemDiscAmount=parseFloat(value.itemDiscAmount == null ?  ''  : value.itemDiscAmount)  ;
       									 workOrderItemDetail.itemTaxId=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
//       									 $scope.taxpercObj.id=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
       									 taxpercentDetailsObj={};
       									 taxpercentDetailsObj.id=value.itemTaxId == null ?  ''  : value.itemTaxId  ;
       									 taxpercentDetailsObj.taxPercentage= value.itemTax == null ?  ''  : value.itemTax  ;
       									 workOrderItemDetail.taxpercentObj=taxpercentDetailsObj;
       									 workOrderItemDetail.itemTax= value.itemTax == null ?  ''  : value.itemTax  ;
       									 workOrderItemDetail.itemTaxAmount=parseFloat(value.itemTaxAmount == null ?  ''  : value.itemTaxAmount ) ;
       									 workOrderItemDetail.itemNetAmount=parseFloat(value.itemNetAmount == null ?  ''  : value.itemNetAmount ) ;
       									 workOrderItemDetail.itemRemark=value.itemRemark == null ?  ""  : value.itemRemark  ;
       									 workOrderItemDetail.status='A' ;
       									 workOrderItemDetail.createdDate="";
       									 workOrderItemDetail.createdBy = $scope.userId;
       									 workOrderItemDetail.updatedDate = "";
       									 workOrderItemDetail.updatedBy = $scope.userId;
       									 workOrderItemDetail.unitId = $scope.unitId;
       									 workOrderItemDetail.orgId = $scope.orgId;
       									 workOrderItemDetail.workOrderCreationStagedListData=value.workOrderCreationStagedListData;
       									 workOrderItemDetail.serviceName = value.serviceName;
       									 workOrderItemDetail.orderDetailsId = value.orderDetailsId;
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
        										$scope.getOtherchargeamountPopup();
    										  });
    										$scope.AddOtherchargeSave();
    									   }
    									}
    									
    									if($scope.common.requestDocumentDto!=null){
    										$scope.documentlist=[];
    										$scope.documentlist=$scope.common.requestDocumentDto;
    									}
    										
    										
    									}
    								}
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
							 
						
						/*
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
                    	$scope.collectionList(); 
						
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
						 $scope.getTransactionTypes ();*/
						 
			     	
			     	
			    	
			    	
			   
			    	
			    	
			    	
			    	
			    	
					
					$scope.getDiagnosisList = function(keyword) {

						var data = {};
						var paramObj = {
							diagnosisName : keyword
						};
						var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						}

						var URI = BASE_URL + ROOT_URL
								+ DIAGNOSISAUTOFILLSEARCH;

						return GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE,
										paramObj, headerObj)
								.then(
										function(response) {
											// $rootScope.startLoader();
											if (response.data.status == "success")
												return response.data.listObject;
										});

					     }
				 }
					 ]);