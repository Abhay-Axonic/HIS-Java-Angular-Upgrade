'use strict';


angular
		.module('myApp')
		.controller(
				'medicinesAddSubController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'growl',
						'$state',
						'$stateParams',
						'PagerService',
						'GenericServiceParamHeader','CONSTANTS','$timeout',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								$stateParams,PagerService,GenericServiceParamHeader,CONSTANTS,$timeout) {
					/* init() function for form object create. */
					$scope.init = function() {


						var cookieObject = $cookies.getObject('cookieObject');
						if (cookieObject == undefined) {
							$state.go('login');
							return;
						}
						$scope.unitId = cookieObject.unitId;
						$scope.organizationId = cookieObject.organizationId;
						$scope.userId = cookieObject.userId;
						$rootScope.loginpage = true;
						 var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
						 $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
					     $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
					     $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
					     $scope.dateFormat = cookieObjectAppConfig.dateFormat;
						$scope.dateOptions =
						{
							formatYear : 'yyyy',
							showWeeks : false,
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
						
						$scope.open1 = function() 
						{
							$scope.popup1.opened = true;
						};
						
						$scope.popup2 =
						{
							opened : false
						};
						
						$scope.open2 = function() 
						{
							$scope.popup2.opened = true;
						};
						
						
						
						  $scope.ordRenderDatetime = {
								                   
								  opened : [ false ],  
								
								 };
						
					
						             $scope.openRenderedDate = function(index) {    
							              for (var i = $scope.orderedMedicationList.length - 1; i >= 0; i--) {
						                     if (i === index) {                       
												  $scope.ordRenderDatetime.opened[i] = true;
						                     } else {
						                         $scope.ordRenderDatetime.opened[i] = false;
						                     }
						                 }
						             };


						
							$scope.bill = {};
							$scope.medicationSearch = '';
							$scope.newMedicationList = [];
							$scope.orderedMedicationList = [];
							$scope.fromDate = "";
							$scope.toDate = "";
							$scope.enterDate = "";
							$scope.enterQty = 0;
							$scope.testObj = {
						    		"itemName":"",
						    		"batchNo":""
						    };
							 $scope.dateList = [];
							 $scope.returnReasonId = "0";
				}
							
				$scope.init();
				
				

				var commonObj = {
						unitId :  $scope.unitId,
						organizationId : $scope.organizationId
					};
			$scope.reasonList=[];
			var URI = BASE_URL + ROOT_URL+ GETACTIVEREASONLIST;

	      BillingGenericService.serviceAction("POST", URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
						if (response.data.status == "success")
							angular.forEach(response.data.listObject , function(value){
								if(value.reasonTypeId == 18){
									$scope.reasonList.push(value);
								}
							})
					});
							
				$scope.virtualStoreId = null;
							/*First Level*/
							$scope.firstTabLevel = 1;

							$scope.setFirstTabLevel = function(newTab) {

								$scope.firstTabLevel = newTab;
								$scope.slotIds = [];
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
							};
							/*Second Level*/
							$scope.secondTabLevel = 1 ;
							
							$scope.setSecondTabLevel = function(newTab)
							{
								$scope.secondTabLevel = newTab;
								
										$scope.example14model = [];
										$scope.example14modelDoctor =[];
										$scope.doctorSlotList =[];
										$scope.slotIds = [];
										if(newTab == 1){
											$scope.isBlockedUnblocked = 2;
											$scope.isBlocked = 1;
										}
										else
											{
											$scope.isBlockedUnblocked = 1;
											$scope.isBlocked = 2;
											}
									
							};
							
							$scope.isSetSecondTabLevel = function(tabNum)
							{
								return $scope.secondTabLevel === tabNum;
							};
							
							
							
							
							  $scope.getorderedMedicationList=function(bill){
						    	 
								  var obj={
										  billingMasterId : bill.billingMasterId,
										  admissionId:bill.admissionId, 
								          encounterId: bill.encounterId  
										  
						    	  }
								    	  var URI = BASE_URL + ROOT_URL + "/api/global/getOrderedMedication";
								
								GenericService.serviceAction("POST", URI, obj).then(function(response) {
								$scope.orderedMedicationList = [];
								
										if (response.data.status == "success") {
											
											$scope.orderedMedicationList = response.data.listObject;
											var date = new Date();
											if(new Date($scope.bill.pdd) < new Date()){
												date = new Date($scope.bill.pdd);
											}
//											if($scope.bill.isFromIPDCharges==true)
//												{
//												date = new Date();
//												}
											 $scope.bill.pddNew=date*1;
											 var vd = new Date($scope.bill.visitDate);
											 $scope.bill.visitDateNew=vd*1;
											 angular.forEach($scope.orderedMedicationList, function (value, key) {
												 var date1 = new Date(value.expiryDateNew);
												 value.expiryDateNew=date1*1;
												 value.returnDate = date;
												 value.rate = Number(parseFloat(value.rate).toFixed(2));
											 });
										
										}
								});
						      }
							  
							  
							  $scope.getVirtualStore=function(){
							    	 
								  var obj={
										  unitId : $scope.unitId,
						    	  }
								    	  var URI = BASE_URL + ROOT_URL + "/api/global/getVirtualStoreId";
								
								GenericService.serviceAction("POST", URI, obj).then(function(response) {
								$scope.orderedMedicationList = [];
								
										if (response.data.status == "success") {
											$scope.virtualStoreId = response.data.listObject[0].virtualStoreId;
										
										}
								});
						      }
							  $scope.getVirtualStore();
							
							
							
							if(angular.equals({},$stateParams.obj))
 							{
								$state.go('cancelledBillList');
							} else {
								$scope.bill = $stateParams.obj;
								  $scope.getorderedMedicationList($scope.bill);
							}
							$scope.items =[];
							
							
							
							
							
							 var NoItemFound = function (search) {
						            var item = {
						                "itemFound": false,
						                "details": "No item found with item code or name " + search
						            };
						            $scope.items = [item];
						        }

							
						    $scope.searchItems = function (search) {
						    	
						    	if($scope.virtualStoreId == undefined || $scope.virtualStoreId == null || $scope.virtualStoreId  == 0){
						    		growl.warning("Virtual Store Is Not Configure...",{
										title : "warning"
									});
									return false;
						    	}
						    	
					            if ((!$scope.prevSearch || $scope.prevSearch != search) ) {
					               $scope.prevSearch = search;

					                var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId+ "/" + false + "/" + search + "/" + $scope.virtualStoreId;
					                $rootScope.startLoader();
					                return GenericService.serviceAction("GET", URI, {})
					                .then(function (response) {
					                    $rootScope.stopLoader();
					                    if (!!response.data && response.data.length > 0) {
					                    	$scope.items = response.data;
					                        return $scope.items ;
					                    } else {
					                        NoItemFound(search);
					                        return $scope.items;
					                    }
					                }, function (err) {
					                    NoItemFound(search);
					                    $rootScope.stopLoader();
					                    return $scope.items;
					                });
					            } else {
					                if (!!$scope.items && $scope.items.length > 0 &&
					                		$scope.items[0].itemFound != undefined && !$scope.items[0].itemFound) {
					                    return NoItemFound(search);
					                } else {
					                	return $scope.items ;
					                }
					            }
					        }

						    $scope.addItemToBill = function (item) {
					        	
					         	$scope.buttonDisabled = false;
					            var itemname = item.split('| ');
					            var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/" + $scope.unitId;                 
					            var data = {
					                "assetTypeId": 1,
					                "storeId": $scope.virtualStoreId,
					                "itemName": itemname[1]
					            };
					            $rootScope.startLoaderPopup();
					            GenericService.serviceAction("POST", URI, data).then(
					            function (response) {
					                $rootScope.stopLoaderPopup();
					                $scope.newMedicationList = [];
					                angular.forEach(response.data, function(item){
					                	var date1 = new Date(item.expiry);
					                    var obj ={
					                        expiryDate: '',
					                        avilableQuantity: '',
					                        assetCategory: '',
					                        assetCategoryId: '',
					                        batchId: '',
					                        batchNo: '',
					                        conversion: '',
					                        cop: '',
					                        genericName: '',
					                        isBatchRequired: '',
					                        itemCode: '',
					                        itemDetails: '',
					                        itemId: '',
					                        itemName: '',
					                        markupStatus: '',
					                        mrp: '',
					                        prodCatId: '',
					                        productCategory: '',
					                        productCategoryId: '',
					                        taxId: '',
					                        taxName: '',
					                        taxPercentage: '',
					                        uomResponses: [],
					                        uomType: '',
					                        uomTypeId: '',
					                        uomUnit: '',
					                        uomUnitId: '',
					                        expiryDateNew:''
					                    }
					                    obj.selected = false;
					                    obj.expiryDate = item.expiry;
					                    obj.avilableQuantity = item.availableQuantity;
					                    obj.assetCategory = item.assetCategory;
					                    obj.assetCategoryId = item.assetCategoryId;
					                    obj.batchId = item.batchId;
					                    obj.batchNo = item.batchNo;
					                    obj.conversion = item.conversion;
					                    obj.cop = item.cop;
					                    obj.genericName = item.genericName;
					                    obj.isBatchRequired = item.isBatchRequired;
					                    obj.itemCode = item.itemCode;
					                    obj.itemDetails = item.itemDetails;
					                    obj.itemId = item.itemId;
					                    obj.itemName = item.itemName;
					                    obj.markupStatus = item.markupStatus;
					                    obj.mrp = Number(parseFloat(item.mrp).toFixed(2));
					                    obj.prodCatId = item.prodCatId;
					                    obj.productCategory = item.productCategory;
					                    obj.productCategoryId = item.productCategoryId;
					                    obj.taxId = item.taxId;
					                    obj.taxName = item.taxName;
					                    obj.taxPercentage = item.taxPercentage;
					                    obj.uomResponses = item.uomResponses;
					                    obj.uomType = item.uomType;
					                    obj.uomTypeId = item.uomTypeId;
					                    obj.uomUnit = item.uomUnit;
					                    obj.uomUnitId = item.uomUnitId;
					                    obj.isThreeUOMApplicable = item.isThreeUOMApplicable;
					                    obj.expiryDateNew=date1*1;
					                    $scope.newMedicationList.push(obj);
					                });
					                $scope.medicationSearch = '';
					                var cleaned = [];
					                $scope.newMedicationList.forEach(function(itm) {
					                    if(itm.uploadedQuantity == undefined || itm.uploadedQuantity == null){
					                        itm.uploadedQuantity = 0;
					                    }
					                    if(itm.uomResponses != undefined && itm.uomResponses != null && itm.uomResponses.length != 0){
					                        itm.uomResponses.find(function(u){
					                            if(u.uomType == itm.uomType){
					                                itm.uomType = u.uomDetails;
					                            }
					                        })
					                        if(itm.isThreeUOMApplicable == false){
					                            if(itm.uomTypeId == 1){
					                                itm.receivedQtyCon = (itm.uploadedQuantity +"*"+ itm.uomResponses[0].conversion);
					                                itm.receivedQtyCalculation = (itm.uploadedQuantity * itm.uomResponses[0].conversion);
					                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
					                            }
					                            else if(itm.uomTypeId == 2){
					                                
					                            }
					                            else{
					                                itm.receivedQtyCon = (itm.uploadedQuantity);
					                                itm.receivedQtyCalculation = (itm.uploadedQuantity);
					                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
					                            }
					                        }
					                        else{
					                            if(itm.uomTypeId == 1){
					                                itm.receivedQtyCon = (itm.uploadedQuantity +"*"+ itm.uomResponses[0].conversion +"*"+ itm.uomResponses[1].conversion);
					                                itm.receivedQtyCalculation = (itm.uploadedQuantity * itm.uomResponses[0].conversion * itm.uomResponses[1].conversion);
					                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
					                            }
					                            else if(itm.uomTypeId == 2){
					                                itm.receivedQtyCon = (itm.uploadedQuantity +"*"+ itm.uomResponses[1].conversion);
					                                itm.receivedQtyCalculation = (itm.uploadedQuantity * itm.uomResponses[1].conversion);
					                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
					                            }
					                            else{
					                                itm.receivedQtyCon = (itm.uploadedQuantity);
					                                itm.receivedQtyCalculation = (itm.uploadedQuantity);
					                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
					                            }
					                        }
					                    }
					                    var unique = true;
					                    cleaned.forEach(function(itm2) {
					                        if (_.isEqual(itm.batchId, itm2.batchId)) unique = false;
					                    });
					                    if (unique)  cleaned.push(itm);
					                });
					                $scope.newMedicationList = cleaned;
					            },
					            function errorCallback(response) {
					                $rootScope.stopLoaderPopup();
					                growl.error('Error');
					            });
					        }
						    
						    $scope.selectRecord = function(item,index) {
						    	if(item.selected == true){
						    		
						    		angular.forEach($scope.newMedicationList,function(value,inx){
						    			
										 if(inx != index)
											{
											 	value.selected = false;
											}else{
												value.selected = true;
											}
										 
									});
						    		
						    		$scope.testObj = item;
						    	}else{
						    		$scope.testObj = {
								    		"itemName":"",
								    		"batchNo":""
								    };
						    	}
						    	
						    }
						    
						   
						    
						    $scope.getdates = function(){
						    	
						    	if($scope.testObj.itemId == undefined){
						    		growl.warning("Please select item ",{
										title : "warning"
									});
									return false;
						    	}
						    	
								if($scope.fromDate == undefined  || $scope.fromDate == null  || $scope.fromDate == "" || $scope.toDate == undefined  || $scope.toDate == null || $scope.toDate == ""){
									growl.warning("Please select from date and to date ",{
										title : "warning"
									});
									return false;
								}else{
									
									if($scope.fromDate > $scope.toDate){
										
										growl.warning(" To date should be greater than from date ",{
											title : "warning"
										});
										return false;
									}else if($scope.bill.isFromIPDCharges==undefined && ($scope.bill.visitDate > moment($scope.fromDate).format("YYYY-MM-DD") || $scope.bill.pdd < moment($scope.toDate).format("YYYY-MM-DD"))){
										growl.warning(" Date should be greater than or equal to admission date and less than or equal to discharge date",{
											title : "warning"
										});
										return false;
									}
									else if($scope.bill.isFromIPDCharges==true && ($scope.bill.visitDate > moment($scope.fromDate).format("YYYY-MM-DD") || moment($scope.toDate).format("YYYY-MM-DD") > moment(new Date()).format("YYYY-MM-DD"))){
										growl.warning(" Date should be greater than or equal to admission date and less than or equal to current date ",{
											title : "warning"
										});
										return false;
									}else{
										
										$scope.dateList = [];
									    var Difference_In_Time = $scope.toDate.getTime() - $scope.fromDate.getTime();
									      
									    var Difference_In_Days = (Difference_In_Time / (1000 * 3600 * 24) + 1);
									    
									    
									    for(var i = 0 ; i<Difference_In_Days;i++){
									    	var date = new Date($scope.fromDate);
									    	date.setDate((date).getDate() + i);
									    	
									    	var data = {};
									    	data.orderDate = moment(date).format("DD-MM-YYYY") ;
									    	data.orderDateNew=date*1;
									    	data.orderQty = $scope.enterQty;
									    	
									    	 $scope.dateList.push(data);
									    }
									}
									
								}
								// $scope.enterQty = 0;
						    }
						    
						
						    
						    
						    var ConvertQtyOrdered = function (item,quantity) {
						    	
						    	var mrp = 0;
						        if(item.uomTypeId != undefined && item.uomResponses != undefined){
						        	
					            if (item.uomTypeId == 1) {
					            	
					            	mrp = parseInt(item.mrp * (1 * (item.uomResponses[0].conversion * item.uomResponses[1].conversion * item.uomResponses[2].conversion)));
					            	
					            } else if (item.uomTypeId == 2) {
					            	
					                mrp = parseInt(item.mrp * (1 * (item.uomResponses[1].conversion * item.uomResponses[2].conversion)));
					                
					            } else if (item.uomTypeId == 3) {
					            	
					                mrp = parseInt(item.mrp * (1 * (item.uomResponses[2].conversion)));
					            }
					          
						      }
						      
						        return mrp;
						    }
						    
						    $scope.saveCount = 0;
						    $scope.saveItemAndPlaceOrder = function(){
						    	
						    	if($scope.saveCount == 0 ){
						    		
						    		if($scope.testObj.itemId == undefined){
							    		growl.warning("Please select item ",{
											title : "warning"
										});
										return false;
							    	}
						    		
						    		
						    		if($scope.dateList.length ==  0 ){
						    			
						    			growl.warning("Please select date range ",{
											title : "warning"
										});
										return false;
						    		}
						    		
							    
							    	var checkQty = false;
							    	 angular.forEach($scope.dateList, function (value, key) {
							    		 
							    		 if(value.orderQty == undefined || value.orderQty == null || value.orderQty == "" || value.orderQty == 0){
							    			 checkQty = true;
							    		 }
							    	 });
							    	
							    	 if(checkQty == true){
								    		growl.warning("Please enter order quantity to all rows ",{
												title : "warning"
											});
											return false;
								    	}
							    	
						    	}

						    	if($scope.dateList.length ==  $scope.saveCount ){
						    		
						    		 growl .success('Item Added Successfully...!!',
												{
													title : 'Success!'
												});
								          
								        $scope.medicationList = [];
								        $scope.testObj = {
									    		"itemName":"",
									    		"batchNo":""
									    };
								        $scope.dateList = [];
								        $scope.fromDate = "";
										$scope.toDate = "";
										$scope.enterDate = "";
										$scope.enterQty = "";
										$scope.newMedicationList = [];
										$scope.saveCount = 0;
						    		return false;
						    	}
						    	
						    	if($scope.dateList.length >  $scope.saveCount ){
						    		$rootScope.startLoader();
							    	  var data = {
							    		        "contractId" : '',
							    		        "organizationId" : $scope.organizationId,
							    		        "unitId" : $scope.unitId,
							    		        "visitTypeId" : $scope.bill.visitTypeId,
							    		        "paymentEntitlementId" : $scope.bill.paymentEntitlementId,
							    		        "encounterId" : $scope.bill.encounterId,
							    		        "admissionId" : $scope.bill.admissionId,
							    		        "billingBedCategoryId" : '',
							    		        "patientCategoryId" : '',
							    		        "defaultSelfTariffId" : '',
							    		        "payeeId" : '',
							    		        "selfPayeeId" : $scope.selfPayeeId,
							    		        "patientClassId" : '',
							    		        "patientId" : $scope.bill.patientId,
							    		        "tariffId" : $scope.defaultSelfTariffId,
							    		        "taxInclusive" : $scope.taxInclusive,
							    		        "walkInSales" : false,
							    		        "opSales" : true,
							    		        "doctorId" : $scope.bill.doctorId,
							    		        "consume" :false,
							    		        "itemList" : []
							    		      }
							    				       
							    	 
													
													var mrp =  ConvertQtyOrdered($scope.testObj, $scope.dateList[$scope.saveCount].orderQty);
													
							    		         var taxAmount = $scope.testObj.taxPercentage!= null ?  parseFloat(((parseFloat(parseFloat(mrp)))/100) * $scope.testObj.taxPercentage).toFixed(2) :0;
							    		          var billItem =  {
							    		            "itemId" : $scope.testObj.itemId,
							    		            "batchId" : $scope.testObj.batchId,
							    		            "rate" : Number(parseFloat(mrp).toFixed(2)),
							    		            "quantity" : Number($scope.dateList[$scope.saveCount].orderQty),
							    		            "selfQuantity" : '',
							    		            "packageConsumptionAmt" : 0,
							    		            "totalAmount" : parseFloat(parseFloat(mrp)).toFixed(2),
							    		            "discountAmount" : 0.00,
							    		            "unitDiscountAmount" :0.00,
							    		            "netAmount" :parseFloat((parseFloat(mrp)+ parseFloat(taxAmount))).toFixed(2),
							    		            "taxId" : $scope.testObj.taxId,
							    		            "taxPercentage" : $scope.testObj.taxPercentage!=null ? $scope.testObj.taxPercentage.toFixed(2) : 0.00,
							    		            "taxAmount" : taxAmount,
							    		            "productCategoryId" : $scope.testObj.productCategoryId,
							    		            "payeeId" : $scope.selfPayeeId,
							    		            "contractId" : '',
							    		            "creditPayable" : 0,
							    		            "selfPayable" : parseFloat(parseFloat(mrp)).toFixed(2),
							    		            "childIndex" : '',
							    		            "consessionPercentage": 0 ,
							    		            "consessionAmount": 0.00,
							    		            "unitConsessionAmount": 0.00,
							    		            "saleDetailPayeeMapperDtoList" :  [],
							    		            "saleDetailPackageMapperDtoList" :  [],
							    		            "isConsumed" : false
							    		          }
							    		          data.itemList.push(billItem);
							    				
							    	
							    	var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/consumeItemPackageContract";
									
									GenericService.serviceAction("POST", URI, data).then(function(response) {
									
											if (response.data.status == "success") {
												  $rootScope.stopLoader();
												if(response.data.listObject.length>0)
										          {
													$scope.medicationList = [];
										              angular.forEach(response.data.listObject, function (consumedItems, consumedKey) 
										              {
										                	consumedItems.orderDateString = $scope.dateList[$scope.saveCount].orderDate;
										                	 $scope.medicationList.push(consumedItems);
										               
										              });
										          
										          }
												 $scope.saveBill();
											}else{
												  $rootScope.stopLoader();
												growl.error('Something went wrong...!!',
														{
															title : 'Error!'
														});
											}
									});
						    	}
						    	
						    }
						    
						  
						    
						    $scope.saveBill = function(){

						    	
						        angular.forEach($scope.medicationList , function (item, key) 
						        		{
									          var billItem = {
									            "statusId": 4,
									            "batchId":  $scope.testObj.batchId,
									            "uomTypeId": $scope.testObj.uomTypeId,
									            "uomUnitId":  $scope.testObj.uomUnitId,
									            /*"rate": item.rate ? Number((item.rate).toFixed(2)) : 0.00,*/
									            "rate" : Number(parseFloat(item.rate).toFixed(2)),
									            "quantity": Number(item.quantity),
									            "leaseRate": $scope.testObj.mrp.toFixed(2),
									            "leaseQuantity": Number(item.quantity),
									            "totalAmount": parseFloat(item.netAmount).toFixed(2),
									            "netAmount":parseFloat(item.netAmount).toFixed(2),
									            "taxId": item.taxId,
									            "itemId": item.itemId,
									            "taxPercentage": item.taxPercentage,
									            "taxAmount":item.taxAmount,
									            "creditPayable": item.creditPayable ? Number((item.creditPayable).toFixed(2)) : 0.00,
									            "selfPayable": item.selfPayable ? Number((item.selfPayable).toFixed(2)) : 0.00,
									            "payeeId":item.payeeId,
									            "productCategoryId": item.productCategoryId,
									            "saleDetailPayeeMapperDtoList":item.saleDetailPayeeMapperDtoList,
									            "saleDetailPackageMapperDtoList": item.saleDetailPackageMapperDtoList,
								          }
									          
									          var data = {
											          "screenId": 1,
											          "patientId":$scope.bill.patientId,
											          "admissionId": $scope.bill.admissionId, 
											          "encounterId": $scope.bill.encounterId,	  
											          "visitTypeId": $scope.bill.visitTypeId,
											          "doctorId": $scope.bill.doctorId ,
											          "totalAmount":parseFloat(item.netAmount).toFixed(2),
											          "netAmount": parseFloat(item.netAmount).toFixed(2),
											          "roundOff": 0,
											          "taxAmount":parseFloat(item.taxAmount).toFixed(2),
											          "saleTypeId": 3,
											          "totalSelfPayable":parseFloat(item.selfPayable).toFixed(2),
											      	  "totalCreditPayable":parseFloat(item.creditPayable).toFixed(2),
											          "items": [],          
											          "paymentEntitlementId": $scope.bill.paymentEntitlementId,
											          "remark": "",
											          "isContractChanged":'N',
											          "isPayeeUpdated":'N',
											          "consume" :false,
											          "walkInSales":true,
											          "unitId":$scope.unitId,
											          "organizationId":$scope.organizationId,
											          "userId":$scope.userId,
											          "storeId":$scope.virtualStoreId,
											          "addToBill" : false,
											          "cashSettlement":  false,
											          "searchPatient":false,
											          "atCopToSave":false,
											          "isVirtualStore": true,
											          "orderDate":item.orderDateString,
											          "isDummyService":true
											        }
									         
								    	  
									          		data.items.push(billItem);
									        
									          $scope.actualSave(data);
						        });
						      
						        
						       
						      }
						    
						    $scope.actualSave = function(data){
						    	  $rootScope.startLoader();
						    	 var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.SAVE_API;
						    	 GenericService.serviceAction("POST", URI, data).then(
								          function (response) {
								            $rootScope.stopLoader();
								            $scope.saveCount = $scope.saveCount + 1;
								            $scope.saveItemAndPlaceOrder();
								          },
								          function (err) {
								            $rootScope.stopLoader();
								        	growl.error('Something went wrong...!!',
													{
														title : 'Error!'
													});
								          }); 
						    }
						    
						    
						    $scope.changeQty = function(){
						    	if(	$scope.enterDate != undefined && $scope.enterDate != "" && 	$scope.enterQty != undefined &&  $scope.enterQty != "" &&  $scope.enterQty != 0 ){
						    		 angular.forEach($scope.dateList , function (item, key) 
								        		{
						    			 			if(item.orderDate == moment($scope.enterDate).format("DD-MM-YYYY")){
						    			 				item.orderQty = $scope.enterQty;
						    			 			}
							    		 
								        		});
						    		 $scope.enterDate = "";
									 $scope.enterQty = 0;
						    		 
						    	}
						    }
						    
						    $scope.returnItem = {};
						    $scope.rejectPopup = function(item,index){
						    	
						    	if(item.returnQty != "" && item.returnQty > 0){
						    		
						    		var remainQty = item.orderQty - item.returnQuantity;
							    	
						    		if(remainQty>=item.returnQty){
						    			
						    			$scope.returnItem =item;
						    			
						    			// $("#accptRejInPatient").modal("show");
						    			
						    		}else{
						    			growl.warning(" Return quantity should be less than or equals to issued quantity ",{
											title : "Warning"
										});
						    			$scope.orderedMedicationList[index].returnQty = "";
										return false;	
						    		}
							    	
						    	}
						    	 
						    }
						    
						    $scope.clear = function(){
						    	 $scope.returnItem = {};
						    	 $scope.returnReasonId = "0";
						    }
						    
						    
						    $scope.checkreturnDate = function(index){
						    	
						    	
						    	 if($scope.orderedMedicationList[index].orderDateString > moment($scope.orderedMedicationList[index].returnDate).format("YYYY-MM-DD") || $scope.bill.pdd < moment($scope.orderedMedicationList[index].returnDate).format("YYYY-MM-DD")){
										growl.warning("Return date should be greater than or equal to Issue date",{
											title : "warning"
										});
										$scope.orderedMedicationList[index].returnDate = new Date($scope.bill.pdd);
										return false;
									}
						    	
						    }
						    
						    
						    $scope.returnItems = function(){
						    	
						    	var checkFirstValue = false;
						    	var checkDate = "";
						    	var tempList = [];
						    	var compareDate = false;
						    	
						    	 angular.forEach($scope.orderedMedicationList , function (item, key) 
							        		{
						    		 			if(checkFirstValue == false){
						    		 				
						    		 				if(item.returnQty != undefined && item.returnQty != null && item.returnQty != ""){
						    		 					
						    		 					checkFirstValue = true;
						    		 					tempList.push(item);
						    		 					checkDate = item.returnDate;
						    		 					
						    			 			}
						    		 				
						    		 			}else{
						    		 				
						    		 				if(item.returnQty != undefined && item.returnQty != null && item.returnQty != ""){
						    		 					
						    		 					tempList.push(item);
						    		 					
						    		 					if(moment(checkDate).format("DD-MM-YYYY")  != moment(item.returnDate).format("DD-MM-YYYY")){
						    		 						compareDate = true;
						    		 					}
						    			 			}
						    		 			}
						    		 			
						    		 
							        		});
						    	 
						    	 if(compareDate == true){
						    		 growl.warning("Please select same date for enter return items",{
											title : "warning"
										});
						    	 }else{
						    		 $scope.saveReturn(tempList);
						    	 }
						    	 
						    	
						    }
						    
						    $scope.saveReturn = function(tempList){
						    	
						    	
						    	var data ={
						    		  "patientId": $scope.bill.patientId,
						    		  "visitType": $scope.bill.visitTypeId,
						    		  "admissionId": $scope.bill.admissionId,
						    		  "wardId":   $scope.bill.wardId, 
						    		  "bedId": $scope.bill.bedId,
						    		  "storeId": $scope.virtualStoreId,
						    		  "statusId": 2,
						    		  "fromMedication": true,
						    		  "unitId": $scope.unitId,
						    		  "organizationId":$scope.organizationId,
						    		  "userId": $scope.userId,
						    		  "returnDate": moment(tempList[0].returnDate).format("DD-MM-YYYY") ,
						    		  "fromMedicineIssue":true,
						    		  "items": []
						    		}
						    	
						    	 angular.forEach(tempList , function (item, key){
						    		  var obj = {
							    		      "batchMapperId": item.batchMapperId,
							    		      "returnQuantity": item.returnQty,
							    		      "returnReasonId": '',
							    		      "id": item.issueId
							    		    }
							    	data.items.push(obj);
						    		 
						    	 }); 
						    		  
						    	
						    	var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_SAVE_API;
						    	
						    	$rootScope.startLoader();
								GenericService.serviceAction("POST", URI, data).then(
												function(response) {
													$rootScope.stopLoader();
													
													 $timeout( function(){
														 $scope.savePatientReturnAcceptance(response.data.id,tempList);
										            },1000);
													
													
												},
												function(err) {
													$rootScope.stopLoader();
													growl.error('Error while saving patient issue return');
												});
						    	
						    }
						    
						    
						    $scope.savePatientReturnAcceptance = function (id,tempList) {
					            	
					                var URI =  CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.MEDICATION_SAVE_API;

					                var data = {
					                    "id": id,
					                    "statusId": 4,
					                    "items": [],
					                	"patientId": $scope.bill.patientId,
					                	"storeId":$scope.virtualStoreId,
					                	"unitId":$scope.unitId,
					                    "organizationId":$scope.organizationId,
					                    "userId":$scope.userId,
					                    "issueId":tempList[0].issueId,
					                    "returnDate": moment(tempList[0].returnDate).format("DD-MM-YYYY") ,
					                    "fromVirtualStore":true,
					                    "isDummyService":true
					                }
					                
					                
					                angular.forEach(tempList , function (item, key){
					                	
					                	 var PatientData = {
						                            "statusId": 4,
						                            "acceptQuantity": Number(item.returnQty),
						                            "rejectedQuantity": 0,
						                            "batchId":item.batchId,
						                            "returnQuantity" :  Number(item.returnQty),
						                            "issueDetailBatchMapper": item.batchMapperId,
						                        }
						                  data.items.push(PatientData);
							    		 
							    	 });
					                
					                       
					                $rootScope.startLoader();
					                GenericService.serviceAction("POST", URI, data).then(

					                    function (response) {
					                    	
					                        $rootScope.stopLoader();
					                        growl.success('Patient return Successfully saved');
					                        $scope.getorderedMedicationList($scope.bill);
					                    },
					                    function (err) {
					                        $rootScope.stopLoader();
					                        growl.error(err.data.message);
					                    });
					        }
							
						    
						    $scope.removeDate = function(index){
						    	
						    	$scope.dateList.splice(index,1);
						    }
        
						}
						 ]);