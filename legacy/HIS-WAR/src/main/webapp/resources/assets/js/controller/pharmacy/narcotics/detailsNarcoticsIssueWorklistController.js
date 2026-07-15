(function () {
    "use strict";

    function detailsNarcoticsIssueWorklistController($scope, $rootScope, $http, $window,$cookies, CONSTANTS, GenericService,GenericServiceParamHeader, $filter, growl, CurrencyService, StatusService, commonDetailService,BillingGenericService,pharamcyContractService, $state,stopTransactionService, $timeout, GenericServiceFileUpload, reportService) {
        var self = this;
        var discountLimit = 100;

        var cookieObject = {};
        var cookieObjectAppConfig = {};
        var cookieObjectStore = {};

    [cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();

    var cookieObject = $cookies.getObject('cookieObject');
    if (cookieObject == undefined) {
      $state.go('login');
      return;
    }

    var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
    if (cookieObjectAppConfig == undefined) 
    {
      $state.go('login');
      return;
    }
    $scope.removeItemFlag = false;
    $scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
    $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
    $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
    $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
    $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
    $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
		//$scope.pinpadAmounFlag = false;
    
    var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
    $scope.storeName = cookieObjectStore.storeName;
    if(cookieObjectStore==undefined){
      $rootScope.storePopup();
      angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
    }
    $scope.storeId = cookieObject.storeId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isBatchValid: [],
                SelectedItems : [],
                isUOMValid: [],
                isQuantityValid: [],
                discountCategoryList: [{
                    "discountCategory": "Select Discount Category"
                }],
                narcoticsIssueWorklistDetails : {
                    items : []
                }

            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            $scope.saveFlag = true;
            $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
            self.model.GetNarcoticsIssueWorklistDetails = GetNarcoticsIssueWorklistDetails;
            self.model.BatchChange = BatchChange;
            self.model.UomChange = UomChange;
            self.model.SubstituteItems = SubstituteItems;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectSubstituteItem = SelectSubstituteItem;
            self.model.Add = Add;
            self.model.AddSubstituteItem = AddSubstituteItem;
            self.model.RemoveSubstituteItem = RemoveSubstituteItem;
            self.model.RestorePreviousItem = RestorePreviousItem;
            self.model.ApplyDiscount = ApplyDiscount;
            self.model.AddItem = AddItem;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;
            self.model.AllocateQuantity = AllocateQuantity;
            self.model.ValidateBill = ValidateBill;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.ConvertQtyOrdered = ConvertQtyOrdered;
            self.model.SaveBill = SaveBill;
            self.model.SavePopup = SavePopup;
            self.model.SetDecimal = SetDecimal;
            self.model.ApplyDiscountValidation = ApplyDiscountValidation;
            self.model.NavigateToNarcoticsIssueWorklist = NavigateToNarcoticsIssueWorklist;
            self.model.ValidateSaveButton = ValidateSaveButton;
            self.model.OnKeyPressQuantity = OnKeyPressQuantity;
            self.model.ValidateMaxOrderQuantity = ValidateMaxOrderQuantity;
            self.model.ValidateBatch = ValidateBatch;
            self.model.ValidateUOM = ValidateUOM;
            self.model.ValidateQuantity = ValidateQuantity;
           // self.model.hideItem= hideItem;
            self.model.ValidateUniqueBatchAndUom = ValidateUniqueBatchAndUom;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.narcoticId = commonDetailService.getDataId();
            self.model.ActualSaveBill = ActualSaveBill;
            self.model.Calculate = Calculate;
            self.model.onChangeDiscount = onChangeDiscount;
            self.model.BarcodeScan = BarcodeScan;
            self.model.ConfirmationPopup = ConfirmationPopup;
            self.model.ClearBarcode = ClearBarcode;
            self.model.SetFocus = SetFocus;
            self.model.PrintNarcoticsIssueWorklistReport = PrintNarcoticsIssueWorklistReport;
            self.model.getAllPatientDiagnosisForIcon = self.model.getAllPatientDiagnosisForIcon;
            self.model.calculateAtcop = calculateAtcop;
            self.model.SearchItems = SearchItems;
            self.model.AddItemToBill = AddItemToBill;
            GetNarcoticIssueForPrint(commonDetailService.getDataId());
            SetFocus();
             $scope.entitlementObj={
          		  "paymentEntitlementTypeId":1
            }	
             GetNarcoticsIssueWorklistDetails(commonDetailService.getDataId());
            // $scope.admissionId = pharamcyContractService.billObj.admissionId;
        }

        $scope.popupStore = function () {
			var popup = angular.element('#storePopUp');
			popup.modal('show');
		}

        var SearchItems = function (search) {
            if (search.length < 1) return;
        
            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
        
                var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId+"/" + true + "/" + search + "/" + $cookies.getObject('cookieObject').storeId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                .then(function (response) {
                    $rootScope.stopLoader();
                    if (!!response.data && response.data.length > 0) {
                    self.model.items = response.data;
                    return $filter('filter')(self.model.items, {
                        $: search
                    });
                    } else {
                    NoItemFound(search);
                    return self.model.items;
                    }
                }, function (err) {
                    NoItemFound(search);
                    $rootScope.stopLoader();
                    return self.model.items;
                });
            } else {
                if (!!self.model.items && self.model.items.length > 0 &&
                self.model.items[0].itemFound != undefined && !self.model.items[0].itemFound) {
                return NoItemFound(search);
                } else {
                return $filter('filter')(self.model.items, {
                    $: search
                });
                }
            }
        }
        
        var AddItemToBill = function (bill, item, model, label) {
        	if (!!item.id) {
        		
        		 ActualAddItemToBill(bill, item, model, label);
        		
        		/*
           	 var stopData = {
    	                "itemIds": [item.id],
    	                "storeId": $scope.storeId 	      		 
    	         };
           	 stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                    var StopTransactionData = responseData;  
                    if(StopTransactionData.length > 0 ){    
                             var startdate=  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                                var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                             	  growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");
                             	  $rootScope.stopLoader();                
                             	 ActualAddItemToBill(bill, item, model, label);
                     }
                    else{
                   	 ActualAddItemToBill(bill, item, model, label);
                    }
           	 });        	 
           */}
        } 
        
        var ActualAddItemToBill = function (bill, item, model, label) {
            if (!!item.id) {
                var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$scope.storeId+'/' + item.id + '/' + $scope.entitlementObj.paymentEntitlementTypeId;
        
                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {})
                .then(function (response) {
                    $scope.data = response.data;
                    
                    //addded by minesh
                    var clubData  = clubBatches(response.data.batch);
                    response.data.batch = clubData;
                    //end added by minesh
                    
                    angular.forEach(response.data.batch, function (value, key) {
                    if(!!this.markUpDownPer){
                        var markUpDown = (value.sellingPrice * (this.markUpDownPer / 100));
        
                        // if (this.markUpDown == 1) {
                        // value.sellingPrice = value.sellingPrice + markUpDown;
                        // } else if (this.markUpDown == 0) {
                        // value.sellingPrice = value.sellingPrice - markUpDown;
                        // }
                    }
                    }, response.data);
        
                    // response.data.batch.unshift({
                    // batchDetails: "Select Batch"
                    // });

                    $timeout( function(){
                        var item = {
                        itemDisabled : false,
                        disabled : false,
                        isConsumed:false,		
                        itemId: response.data.itemId,
                        itemCode: response.data.itemCode,
                        markUpDown: response.data.markUpDown,
                        itemName: response.data.itemName,
                        markUpDownPer: response.data.markUpDownPer,
                        formulationType: response.data.formulationType,
                        stock: response.data.currentStock,
                        genericId: response.data.genericId,
                        genericName: response.data.genericName,
                        manufacturerId: response.data.manufacturerId,
                        manufacturerName: response.data.manufacturerName,
                        strength: response.data.strength,
                        batch: response.data.batch,
                        productCategoryId:response.data.productCategoryId,
                        assetCategoryId:response.data.assetCategoryId,
                        selectedBatch: response.data.batch[0],
                        uom: [{
                            uomDetails: "Select UOM",
                        }],
                        taxId: response.data.taxId,
                        tax: response.data.taxName,
                        taxPercentage: response.data.taxPercent,
                        isOTC: true,
                        isShow : bill.items.length >=1 ? true: false,
                        isFromAssignPicker : $scope.assignPickerFlag!=undefined ? $scope.assignPickerFlag:false,
                        patientClassPercentage : $scope.patientClassPercentage,
                        saleDetailPayeeMapperDtoList:[],
                        SaleDetailPackageMapperList:[],
                        isThreeUOMApplicable: response.data.isThreeUOMApplicable,
                        onlySubButtonDisable:true
                        };
        
                        item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(response.data.batch);
                        if(item.selectedBatch!=undefined)
                        	{
                        	   //item.selectedUom = item.uom[0];
                        	   item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                   return obj.opUom;
                                   });
                        	   
                        	  
                        	}
                        else{
                        	item.selectedUom = item.uom[0];
                        }
                        item.checkNewItem=true;
                        bill.items.push(item);
                        if(item.selectedBatch!=undefined)
                    	{
                          BatchChange(bill.items.length-1,self.model.narcoticsIssueWorklistDetails, item.selectedBatch,null);
                    	}
                        if(bill.items.length > 1){
                            $scope.flagRemove = true;
                        }
                        else{
                            $scope.flagRemove = false;
                        }
                   },2000);
                    self.model.search = '';
                    $rootScope.stopLoader();
                }, function (err) {
                    growl.error('Error while fetching the item details');
                    $rootScope.stopLoader();
                });
            }
        }
        
        /*var calculateAtcop = function () {
            angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function (d){
                if($scope.atCop == true){
                    d.rate = d.selectedBatch.actualLendingPrice;
                    Calculate(d, self.model.narcoticsIssueWorklistDetails);
                }
                else{
                    d.rate = d.selectedBatch.rate;
                    Calculate(d, self.model.narcoticsIssueWorklistDetails);
                }
            });
        }*/
        var calculateAtcop = function (atCop,flag) {
        	$scope.atCopSelected=atCop;
        	$scope.atCopToSave=$scope.atCopSelected;
        	$scope.globalFlag=flag;
        	var count=0;
            angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function (d){
            	count++;
            	if(d.disabled==false)	
            		{
            		$scope.atCop=$scope.atCopSelected;
            		if($scope.globalFlag==1)
            			{
            			if($scope.atCop == true){
                            d.rate = d.selectedBatch.actualLendingPrice;
                            $scope.calculateAtCopCalculation(d, self.model.narcoticsIssueWorklistDetails);
                            CalculateAmount(d);
                            $scope.atCopToSave=true;
                        }
                        else{
                            d.rate = d.selectedBatch.rate;
                            $scope.calculateAtCopCalculation(d, self.model.narcoticsIssueWorklistDetails);
                            CalculateAmount(d);
                            $scope.atCopToSave=false;
                        }
            			if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
            				angular.forEach(d.selectedItems, function (sub){
                				if($scope.atCop == true){
                					sub.rate = sub.selectedBatch.actualLendingPrice;
                					$scope.calculateAtCopCalculation(sub, d.selectedItems);
                                    CalculateAmount(sub);
                                    $scope.atCopToSave=true;
                                }
                                else{
                                	sub.rate = sub.selectedBatch.rate;
                                	$scope.calculateAtCopCalculation(sub, d.selectedItems);
                                    CalculateAmount(sub);
                                    $scope.atCopToSave=false;
                                }
                			});
            			}
            			}
            		}
            	else
            		{
            		if(d.disabled==true)
                	{
                	$scope.atCop=d.atCop;
                	if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
                		$scope.atCop=$scope.atCopSelected;
        				angular.forEach(d.selectedItems, function (sub){
            				if($scope.atCop == true){
            					sub.rate = sub.selectedBatch.actualLendingPrice;
            					$scope.calculateAtCopCalculation(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=true;
                            }
                            else{
                            	sub.rate = (sub.selectedBatch.rate != undefined && sub.selectedBatch.rate != null )? sub.selectedBatch.rate : sub.selectedBatch.sellingPrice;
                            	$scope.calculateAtCopCalculation(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=false;
                            }
            			});
        			}
                	}
                	else
                		{
                		$scope.atCop=$scope.atCopSelected;
                		}
                    if($scope.atCop == true){
                        d.rate = d.selectedBatch.actualLendingPrice;
                        $scope.calculateAtCopCalculation(d, self.model.narcoticsIssueWorklistDetails);
                        CalculateAmount(d);
                        $scope.atCopToSave=true;
                    }
                    else{
                        d.rate = d.selectedBatch.rate;
                        $scope.calculateAtCopCalculation(d, self.model.narcoticsIssueWorklistDetails);
                        CalculateAmount(d);
                        $scope.atCopToSave=false;
                    }
                    if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
        				angular.forEach(d.selectedItems, function (sub){
            				if($scope.atCop == true){
            					sub.rate = sub.selectedBatch.actualLendingPrice;
            					$scope.calculateAtCopCalculation(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=true;
                            }
                            else{
                            	sub.rate = sub.selectedBatch.rate;
                            	$scope.calculateAtCopCalculation(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=false;
                            }
            			});
        			}
            		}
            	
            });
            if(count==self.model.narcoticsIssueWorklistDetails.items.length && $scope.globalFlag!=1)
            	{
            	$scope.atCop=false;
            	}
        }
        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var ClearBarcode = function () {
            self.model.barcode = '';
        }

        var ConfirmationPopup = function () {
            var popup = angular.element('#confirmationPopUp');
            popup.modal('show');
        }

        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }

        $scope.handleDoubleClick=false;
        var BarcodeScan = function (event, barcode) {
            $scope.handleDoubleClick=false;
        	if(self.model.barcode=="")
    		{
    		growl.error("Barcode Number Should Not Be Blank...")
    		return false;
    		}
            var enterCode = 13;
            if (!barcode && event.which === enterCode) {
                barcode = self.model.barcode;
            }
            var data = {
                "unitId": $scope.unitId,
                "items":[]
            };
            if (!!barcode) {
    	        var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API+"/prePrintedBarcode/getBarcodes";
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {        
     	            $rootScope.stopLoader();
     	            var flag=false;
     	            angular.forEach(response.data, function (value, key) {
     	                if(response.data[0].isManualBarcodeRequired==true && value.prePrintedBarcode==self.model.barcode )
     		            {
     		                flag=true;
                             self.model.barcode="";
                            AllocateBatch(self.model.narcoticsIssueWorklistDetails, value.itemId, value.batchId);
     		            }
     	            });
     	            if(flag==false)
     		        {
                        if (!!barcode) {
                            var itemId = barcode.split(' ')[0];
                            var batchId = barcode.split(' ')[1];

                            if (!!itemId && !!batchId) {
                	            var barcodeCnt = 0;
                	            angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function (item, key) {
                	                if((!item.disabled && item.disabled !=undefined) || (!item.itemDisabled && item.itemDisabled !=undefined)){
             		                    if(item.itemId == itemId && item.selectedBatch.batchId == batchId){
             			                    barcodeCnt++;
//             			                    AddItem(key, self.model.narcoticsIssueWorklistDetails);
             		                    }
             		                    else if(item.selectedItems!=undefined && item.selectedItems.length > 0){
           			                        angular.forEach(item.selectedItems, function (item1, key1) {
           			                            if(item1.itemId == itemId){
           				                            barcodeCnt++;
//           				                            AddItem(key1, self.model.narcoticsIssueWorklistDetails, key);
           			                            }
           			                        });
             		                    }
                                        else{
                                            AllocateBatch(self.model.narcoticsIssueWorklistDetails, itemId, batchId);
                                        }
                	                }
                                    else{
                                        AllocateBatch(self.model.narcoticsIssueWorklistDetails, itemId, batchId);
                                    }
             	                });
                            }
                            event.stopPropagation();
                            event.preventDefault();
                            SetFocus();
                        }
                    }
                });
            }
        } 
        
         var AllocateBatch = function (bill, itemId, batchId) {
            if(itemId != undefined && batchId != undefined){
                var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_BATCH_API + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId + '/'+$cookies.getObject('cookieObject').storeId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + bill.id;
                var data = {
                    "itemId": itemId,
                    "batchId": batchId
                };
                $rootScope.startLoader();
                GenericService.serviceAction("PUT", URI, data).then(function (response) {
                    if (!!response.data) {
                        var item = bill.items.find(function (obj) {
                            return obj.allocationId == response.data.allocationId;
                        });
                        // if (!!item) {
                        // item.quantity = parseInt(item.quantity) + 1;
                        // CalculateQuantity(item, item.selectedUom, item.uom);
                        // CalculateAmount(item);
                        // } 
                        // else {
                            angular.forEach(response.data.batch, function (value, key) {
                            }, response.data);
                            var batch = response.data.batch.find(function (obj) {
                                return obj.batchId == batchId;
                            });
                            item = {
                                // allocationId: response.data.allocationId,
                                itemDisabled:false,
                                disabled:false,
                                itemId: response.data.itemId,
                                itemCode: response.data.itemCode,
                                itemName: response.data.itemName,
                                markUpDown: response.data.markUpDown,
                                markUpDownPer: response.data.markUpDownPer,
                                genericId: response.data.genericId,
                                genericName: response.data.genericName,
                                manufacturerId: response.data.manufacturerId,
                                manufacturerName: response.data.manufacturerName,
                                formulationType: response.data.formulationType,
                                stock: response.data.currentStock,
                                strength: response.data.strength,
                                batch: [{
                                batchDetails: "Select Batch"
                                }].concat(response.data.batch),
                                uom: [{
                                uomDetails: "Select UOM",
                                }],
                                taxId: response.data.taxId,
                                taxName: response.data.taxName,
                                taxPercent: response.data.taxPercent,
                                taxPercentage: response.data.taxPercent,
                                isOTC: response.data.isOTC,
                                isShow : bill.items.length >=1 ? true: false,
                                isThreeUOMApplicable: response.data.isThreeUOMApplicable,
                                barcodeItem: true
                            };
                            if (!!batch) {
                                item.selectedBatch = batch;
                                item.uom = item.uom.concat(batch.uom);
                                item.selectedUom = batch.uom.find(function (obj) {
                                    return obj.opUom;
                                });
                                item.quantity = 1;
                            } else {
                                //item.selectedBatch = item.batch[0];
                                item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                                if(item.selectedBatch!=undefined && item.selectedBatch!=null)
                            	{
                            item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                return obj.opUom;
                              });
                            	}
                            }
                            CalculateQuantity(item, item.selectedUom, item.uom);
                            CalculateRate(item, item.selectedUom, item.uom);
                            Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            CalculateAmount(item);
                            if($scope.handleDoubleClick==false)
                      	  {
                            bill.items.push(item);
                            $scope.handleDoubleClick=true;
                            }
                            var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API + $scope.storeId+'/'+itemId+'/'+ batchId+'/'+item.selectedUom.uomTypeId;
                            GenericService.serviceAction("GET", URI, {}).then(
                            function (response) 
                            {
                                item.stock=response.data.availableqty;
                                //Ganesh
                                item.quantity = commonDetailService.getDefaultQuantity(item,item.quantityOrdered,response.data.availableqty);
                                item.newStock =response.data.availableqty;
                                $scope.handleDoubleClick=false;
                                $rootScope.stopLoader();
                            },
                            function (err) 
                            {
                                item.stock = 0;
                                item.newStock = 0;
                                $rootScope.stopLoader();
                            });
                            self.model.barcode = "";
                        // }
                        $rootScope.stopLoader();
                    }
                },
                function (ex) {
                    growl.error(ex.data.message);
                    $rootScope.stopLoader();
                });
            }
        }
        
        var  clubBatches = function(batches){
        	// added by minesh for total of availableQuantity from all batch and get single Unique Batch
            var batchNo = [];
            var uniqueBatches = [];
        	
        		if (!!batches) {
        			batches.forEach(function (batch) {
        			var index = batchNo.indexOf(batch.batchId);                    			 
                	if(index === -1)
                	{
                		batchNo.push(batch.batchNo);
                		uniqueBatches.push(batch);
                	}else{
                			uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
        					//batch.availableStockQuantity = uniqueBatches[index].availableStockQuantity ;
                		}
        		});
        		}                	
        		return uniqueBatches;
        	// added by minesh for total of availableQuantity from all  batch and get single Unique Batch
        }
        
        
        $scope.parentIssueDetailsId = 0;
        $scope.isFromAssignPicker = false;
        var SubstituteItems = function (item,index) {
        	//self.model.parentIndex=parentIndex;
            self.model.SelectedItems=[];
        	item.substititeFlag=true;
            self.model.parIndx = index;
            self.model.formulationTypeId = item.formulationTypeId;
            self.model.instructionDesc = item.instructionDesc;
            self.model.duration = item.duration;
            self.model.frequencyDesc = item.frequencyDesc;
        	self.model.selectedItemId = item.itemId;
            self.model.selectedGenricId = item.genericId;
            self.model.genericId = item.genericId;
            self.model.itemName = item.itemName;
            self.model.genericName = item.genericName;
            self.model.dose = item.dose;
            $scope.parentIssueDetailsId =item.id!=null ? item.id:item.parentIssueDetailsId;
            $scope.isFromAssignPicker = item.isFromAssignPicker;
            if(item.selectedUom == null || item.selectedUom.conversion == undefined ){
            	 
            	 self.model.selectedQuantity = item.quantityOrdered;
            }else{
            	  var mulQty = item.selectedUom.conversion * item.quantity;
                  var  newOrderQty =  item.quantityOrdered - mulQty;
                  self.model.selectedQuantity = newOrderQty;
            }
          //  self.model.selectedQuantity = item.quantityOrdered;
            self.model.substituteNewQty = item.newQty;
            self.model.taxPercentage = item.taxPercentage;
            self.model.issuedQuantity = item.quantityIssued;
            self.model.productCategoryId = item.productCategoryId;
            self.model.pendingIssueQuantity = item.pendingIssueQuantity;
            self.model.prescriptionDetailId = item.id!=null ? item.id:item.parentIssueDetailsId;
            self.model.frequency = item.frequency;
            self.model.dose = item.dose+' '+item.doseUnit ;
            self.model.startDate = item.startDate;
            self.model.stopDate = item.stopDate;
            self.model.substituteDetails = [];
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            self.model.pickedQuantity =item.pickedQuantity!=undefined ? item.pickedQuantity :0;
//            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic;
            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic+ '/' +  $scope.storeId+'/'+ self.model.formulationTypeId;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.substituteDetails = response.data;

                    if (!self.model.substituteDetails || self.model.substituteDetails == []) {
                        return;
                    } else {
                        self.model.substituteDetails.forEach(function (item) {
                            self.model.selectAllRows = false;
                            item.isSelected = false;
                            item.selectSuttt = false;
                            item.batch.unshift({
                                "batchDetails": "Select Batch"
                                // "batchType": "Select Batch"
                            });
                            item.uom = [{
                                "uomDetails": "Select UOM"
                            }],

                            //item.selectedBatch = item.batch[0];
                            item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                             if(item.selectedBatch!=undefined)
                            	 {
                            	 item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                     ipUom: true
                                  }, true)[0];
                            	 }
                            item.selectedUom = item.selectedUom!=undefined ? item.selectedUom : item.uom[0];
                            
                            
                        });
                        self.model.substituteDetailsData = self.model.substituteDetails;
                    self.model.substituteDetails = [];
                    angular.forEach(self.model.substituteDetailsData, function(d){
                        if(d.batch.length != 0){
                            self.model.substituteDetails.push(d);
                        }
                    })
                    }
                    $rootScope.stopLoaderPopup();
                });

        }
        
        var AddSubstituteItem = function (index, bill) {
            bill.splice(index + 1, 0, CopySubstituteItem(index, bill));
        }
        //For Remove 
        var RemoveSubstituteItem = function (index, bill) {
          //  bill.splice(index, 1);
          	angular.forEach(bill,function(value,indx){
        		if(index ==indx){
        			if(value.isSelected == false || value.isSelected == undefined){
            			bill.splice(index, 1);
            		}else{
            			growl.warning("Please de-select the item",
    							{
    								title : 'Success!'
    							});
            		}
        		}
        		
        	});
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItems = [];
                self.model.substituteDetails.forEach(function (item) {

                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    self.model.SelectedItems.push(item);

                });
            } else {
                self.model.SelectedItems = [];
                self.model.substituteDetails.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var isSelected = function (item) {
            return item.isSelected;
        }
       
        var SelectSubstituteItem = function (item) {
        	
            var itemIds = self.model.SelectedItems;

            if (item.isSelected) {
                itemIds.push(item);
                item.selectSuttt=true;
            } else {
            	item.selectSuttt=false;
                var index = itemIds.indexOf(item);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.substituteDetails.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;

            self.model.substituteDetails.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        
        var CopySubstituteItem = function (index, bill) {
            var item = bill[index];
            return {
	             	itemId: item.itemId,
	             	genericId: item.genericId,
	                itemCode: item.itemCode,
	                itemName: item.itemName,
	                genericName: item.genericName,
	                // manufacturerName: item.manufacturerName,
	                batch: item.batch,
	                selectedBatch: null,
	                uom: item.uom,
	                selectedUom: null,
	                stockAvailable: item.stockAvailable,
	                rackNo: item.rackNo,
	                picker: item.picker,
	                disabled: item.disabled,
	                issuedQty: item.issuedQuantity,
	                pendingIssueQty: item.pendingIssueQuantity,
	                isShow: true
	                
            }
        }
        
        var validateSubSelectedBatch=function(listSub){
      	  var isValid=true;
      	  var count=0;
      	  angular.forEach(listSub, function(val , key){
      		  if(val.selectedBatch==undefined){
                    growl.error("Please select batch.");
                    $("#substituteModal").show();
      			  isValid=false;
      			  count++;
      		  }else{
      		  	
      			 // var popup = angular.element('#substituteModal');
      	          //popup.modal('hide');
      			  isValid=true;
      		  }
      	  });
      	  if(isValid=true && (count==0)){
      	  	 var popup = angular.element('#substituteModal');
      	          popup.modal('hide');
      	  }
      	  return isValid; 
        }
        
        var Add = function () {
            if (!self.model.SelectedItems || self.model.SelectedItems == undefined || self.model.SelectedItems == '') {
                growl.error('Please select items')
                return false;
            } else {
            	  if(validateSubSelectedBatch(self.model.SelectedItems)){
            		self.model.SelectedItems.forEach(function (selectedItem) {
                    // selectedItem['itemName'] = self.model.itemName;
                    // selectedItem['genericName'] = self.model.genericName;
                    selectedItem['dose'] = self.model.dose;
                    selectedItem['quantityOrdered'] = self.model.selectedQuantity;
                    selectedItem['substituteNewQty'] = self.model.substituteNewQty;
                    selectedItem['newQty'] = self.model.selectedQuantity;
                    selectedItem['id'] = self.model.prescriptionDetailId;
                    selectedItem['substituteBtn'] = true;
                    selectedItem['parentItemId'] = self.model.selectedItemId;
                    selectedItem['consumedItemId'] = self.model.selectedItemId;
                    selectedItem['selectedUom'] = selectedItem.selectedUom;
                    selectedItem['frequency'] = self.model.frequency;
                    selectedItem['pendingIssueQuantity'] = self.model.pendingIssueQuantity;
                    selectedItem['quantityIssued'] = self.model.issuedQuantity;
                    selectedItem['issuedQty'] = self.model.issuedQuantity;
                    selectedItem['pendingIssueQty'] = self.model.pendingIssueQuantity;
                   // selectedItem['taxPercentage'] =  self.model.taxPercentage;
                    selectedItem['taxPercentage'] =  selectedItem.taxPercentage;
                    selectedItem['taxId'] = selectedItem.selectedBatch.taxId;
                    selectedItem['finalOrderedQty'] = self.model.selectedQuantity;
                    selectedItem['parentIssueDetailsId'] =  $scope.parentIssueDetailsId!=0 ? $scope.parentIssueDetailsId : null;
                    selectedItem['quantityIssued'] = self.model.issuedQuantity ;
                    selectedItem['isFromAssignPicker'] = $scope.isFromAssignPicker;
                    selectedItem['saleDetailPayeeMapperDtoList'] = [];
                    selectedItem['SaleDetailPackageMapperList'] = [];
                    selectedItem['patientClassPercentage'] = 0;
                    selectedItem['markUpDown'] = selectedItem.markUpDown;
                    selectedItem['markUpDownPer'] = selectedItem.markUpDownPer;
                    selectedItem['itemId'] = selectedItem.itemId;
                    selectedItem['productCategoryId'] = self.model.productCategoryId;
                    selectedItem['pickedQuantity'] = self.model.pickedQuantity;
                    selectedItem['instructionDesc'] = self.model.instructionDesc
                    selectedItem['duration'] = self.model.duration;
                    selectedItem['frequencyDesc'] = self.model.frequencyDesc;


                    // selectedItem.selectedUom = [{
                    //     uomDetails: "Select UOM"
                    // }];
                    // selectedItem.selectedUom = selectedItem.selectedUom[0];
                });

                self.model.narcoticsIssueWorklistDetails.items.forEach(function (list) {
//                	if(!list.itemDisabled){
                    self.model.SelectedItems.forEach(function (item) {

                        item.uom = [{
                            uomDetails: "Select UOM",
                        }];
                        if(item.selectedBatch != undefined){
                            if (!!item.selectedBatch.batchId) {
                                item.stockAvailable=item.selectedBatch.availableStockQuantity;
                                item.stock=item.selectedBatch.availableStockQuantity;
                                item.substituteNewQty = item.selectedBatch.availableStockQuantity;
                                item.uom = item.selectedBatch.uom;
                                item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                    opUom: true
                                }, true)[0];
                            } else {
                                item.selectedUom = item.uom[0];
                            }
                        }
                        if(item.batch) {
                            if(item.batch.length > 0) {
                                self.model.IsEnbaledSave = true;
                            }
                        }
                        // if (!item.selectedUom) item.selectedUom = "";
                        
                        //for markup and markdown and concession
                        var URI = CONSTANTS.GLOBAL.DETAILS_API_IP + '/' + $scope.unitId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + $scope.admissionId + '/' + $scope.storeId;
                        $rootScope.startLoader();
                        GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $timeout( function(){
                                item.markUpDown =response.data.markUpDown;
                                item.markUpDownPer =response.data.markUpDownPer;
                                item.patientClassPercentage =response.data.patientClassPercentage;
                                item.consumedItemId = list.itemId;
                                item.consumedPcategroyId =list.productCategoryId;
                                angular.forEach(response.data.batch, function(d){
                                   if(item.selectedBatch.batchId == d.batchId){
                                      item.selectedBatch.actualLendingPrice = d.actualLendingPrice;
                                      item.selectedBatch.rate = d.rate;
                                    }
                                });
                                angular.forEach(item.batch, function(b){
                                    if(b.batchId != undefined && b.batchId != null){
                                        angular.forEach(response.data.batch, function(b1){
                                            if(b.batchId == b1.batchId){
                                                b.actualLendingPrice = b1.actualLendingPrice;
                                                b.rate = b1.rate;
                                            }
                                        });
                                    }
                                });
                                if(item.isThreeUOMApplicable == false){
                                    item.uomListData = item.uom;
                                    item.uom = [];
                                    angular.forEach(item.uomListData, function(u){
                                        if(u.uomTypeId != 2){
                                            item.uom.push(u);
                                        }
                                    })
                                }
                                ConvertQtyOrdered(item, item.uom, item.selectedUom);
                                $scope.calculateAtCopCalculation(item, self.model.narcoticsIssueWorklistDetails);
                                $rootScope.stopLoader();
                            },2000);
                        }, function (err) {
                            growl.error('Error while fetching the item details');
                            $rootScope.stopLoader();
                        });
                    });
                   
                    if (self.model.selectedGenricId == list.genericId) {
                    	if(list.substititeFlag){
                    	    if(list.quantity == null || list.quantity == 0){
                                list['disabled'] = true;
                                list.quantity = null;
                    	    }
                            if (!list['selectedItems']){
                                angular.forEach(self.model.SelectedItems, function(subData){
                                    if(subData.isThreeUOMApplicable == false){
                                        subData.uomListData = subData.uom;
                                        subData.uom = [];
                                        angular.forEach(subData.uomListData, function(u){
                                            if(u.uomTypeId != 2){
                                                subData.uom.push(u);
                                            }
                                        })
                                    }
                                })
                                list['selectedItems'] = self.model.SelectedItems;
                            }
                            else {
                                angular.forEach(self.model.SelectedItems, function(subData){
                                    if(subData.isThreeUOMApplicable == false){
                                        subData.uomListData = subData.uom;
                                        subData.uom = [];
                                        angular.forEach(subData.uomListData, function(u){
                                            if(u.uomTypeId != 2){
                                                subData.uom.push(u);
                                            }
                                        })
                                    }
                                })
                                list['selectedItems'] = list['selectedItems'].concat(self.model.SelectedItems);
                            }
                            // list.selectedUom = list.selectedUom[0];
                            list.substititeFlag=false;
                        }
                    }
//                }
                });
                self.model.narcoticsIssueWorklistDetails.items[self.model.parIndx].isShow = false;
                self.model.SelectedItems = [];
            }
        }
            /*var popup = angular.element('#substituteModal');
            popup.modal('hide');*/
        }
        
        var GetCalculateOrederQty = function(item){
         	 var issuedQty = 0;
         	 var found =  item.batch.find(function (x) {
               return x.isSelected;
           });
         	 
         	var uomTypeId = item.uomTypeId !=undefined ?item.uomTypeId : (found != undefined ? found.uomTypeId : undefined);
         	
        	if(uomTypeId == undefined){
        		  return issuedQty;
         	}else{
        	
         	  if(uomTypeId == 3)
               {
         		issuedQty  = parseInt(item.quantityIssued);
                     
               }else if(uomTypeId == 2)
                   	{
              	
                          var stripQty = found.uom[1].conversion * parseInt(item.quantityIssued);
                          issuedQty  = stripQty;
                      
                   	}else if(uomTypeId== 1)
                   		 {
  		                      var boxQty = found.uom[1].conversion *  found.uom[2].conversion * parseInt(item.quantityIssued);
  		                   issuedQty  = boxQty;
                   		 }  
         	  
         	  return issuedQty;
         	}
         }
       
       var  uomConversion = function(item,batchObj,selectedUomId,parentItemQty){
       	if(parentItemQty !=undefined){
       		var quantityOrdered = item.quantityOrdered - parentItemQty;
       	}else{
       		var quantityOrdered = item.quantityOrdered ;
       	}
        var pickedQty = item.pickedQuantity!=undefined ?item.pickedQuantity : 0;
        
            if (batchObj) { 
                if (selectedUomId == 1) {
               	 item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                 item.newPickedQty = Math.ceil(pickedQty / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
               	 item.newStock = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                } else if (selectedUomId == 2) {
                    item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                    item.newPickedQty = Math.ceil(pickedQty / ( batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                    item.newStock = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                } else if (selectedUomId == 3) {
                    item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[2].conversion));
                    item.newPickedQty = Math.ceil(pickedQty / (batchObj.uom[2].conversion));
                    item.newStock = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[2].conversion));
                }
            }
       }
       
        
        var GetNarcoticsIssueWorklistDetails = function (issueId) {
            var URI = CONSTANTS.PHARMACY.NARCOTICS.NARCOTICS_ISSUE_WORKLIST.DETAIL_API  + $scope.unitId + '/'+$scope.storeId + '/' + issueId;

            // self.model.worklistDetails.discountCatgeory = self.model.discountCategoryList[0];
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	self.model.statusId = response.data.statusId;
                    $scope.admissionId = response.data.admissionId;
                    self.model.remark = response.data.remark;
                    $scope.getSponsorDetails($scope.admissionId);
                    $scope.idpatient = response.data.patientId;
                    self.model.narcoticsIssueWorklistDetails = response.data;
                    self.model.narcoticsIssueWorklistDetails.discountCatgeory = self.model.discountCategoryList[0];
                    self.model.discountCategoryList = self.model.discountCategoryList.concat(response.data.discountCategory);
                    $rootScope.allergydata(response.data);
                    getAllPatientDiagnosisForIcon(response.data.patientId);
                    self.model.narcoticsIssueWorklistDetails['discountType'] = '1';
                    if(response.data.statusId == 4)
                    {
                 	   $scope.saveFlag = true;
                    }

                    if (self.model.narcoticsIssueWorklistDetails.items) {
                        $scope.orderQtyTotalSum=0;

                        self.model.narcoticsIssueWorklistDetails.items.forEach(function (item, index) {
                        	
                            if(item.startDate && item.stopDate){
                                var startDate = moment(item.startDate).format('YYYY-MM-DD');
                                var stopDate = moment(item.stopDate).format('YYYY-MM-DD');
                                item.duration = moment(stopDate).diff(moment(startDate), 'days');
                                item.duration = item.duration + 1;
                                if(item.selectedItems){
                                	 if(item.selectedItems.length != 0){
										angular.forEach(item.selectedItems, function(select){
											select.duration = item.duration;
											select.instructionDesc = item.instructionDesc;
											select.frequencyDesc = item.frequencyDesc;
										});
									}
                                }
                            }
                        	$scope.assignPickerFlag=item.isFromAssignPicker;
                            $scope.patientClassPercentage = item.patientClassPercentage;
                            
                         // added by minesh
                            var clubData  = clubBatches(item.batch);
                            item.batch = clubData;    
                         // added by minesh

                            
                            if (item.status == 'Completed' ||  item.status == 'Partially Completed' || item.statusId == 9) {
                            	  item.itemDisabled = true;
                              	  item.disabled = true;
                            }
                            else if(item.status == 'Pending'){
                            	item.itemDisabled = false;
                                item.disabled = false;
                            	 var priQty = 0;
                                 if(item.selectedItems !=undefined){
	                                   item.selectedItems.forEach(function (item2) {
	                                   priQty += GetCalculateOrederQty(item2);
                                 });
                                 }

//                                 item.qtyPrescribed = item.qtyPrescribed - priQty
                                 
                                 if(item.isFromAssignPicker == true){

                                	 item.pickedQuantity = item.pickedQuantity - priQty;
                                	 item.quantityOrdered = item.quantityOrdered - priQty;
                                    
                          	 	}else{
                          	 		item.quantityOrdered = item.quantityOrdered - priQty;
                          	 		if(item.quantityOrdered == 0){
                          	 			item.itemDisabled = true;
                          	            item.disabled = true;
                          	 		}
                          	 	}
                                
                                 $scope.saveFlag = false; 
                                 
                            }else{
                            	  $scope.saveFlag = false; 
                            }
                            
                            if(self.model.narcoticsIssueWorklistDetails.items.length==1 && self.model.narcoticsIssueWorklistDetails.statusId == 2  ){
                                item.isShow = false;
                           }else{
                                item.isShow = true;
                           }
                                
                            var isBatchFount = item.batch.find(function (x) {
                                return !!x.isSelected;
                            });

                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }  
                            item.finalOrderedQty = item.quantityOrdered;
                            if (!!isBatchFount) {
                                item.selectedBatch = item.batch.find(function (x) {
                                    return !!x.isSelected;
                                });
                                item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                	
                                    return item.selectedBatch.uomTypeId==obj.uomTypeId;
                                });
                                item.uom = item.selectedBatch.uom;
                                item.stock = item.selectedBatch.availableStockQuantity;
//                                item.itemtotalAmount = item.totalAmount;
//                                item.itemDiscount = item.discountAmount;
                                if(item.totalAmount != undefined){
                            		item.itemtotalAmount = item.totalAmount;
                            		item.itemDiscount = item.discountAmount;
                            		item.discountPerItem = (100*(item.discountAmount/item.totalAmount));
                            		if(item.discountPerItem){
                                		item.discountPerItem = item.discountPerItem.toFixed(2);
                                	}
                            	}else{
                            		 item.itemtotalAmount = 0;
                                	 item.itemDiscount = 0;
                                	 item.taxAmount= 0;
                                	 item.netAmount= 0;
                                	 item.roundOff=0;
                            	}
                              //  AllocateQuantity(index, self.model.narcoticsIssueWorklistDetails);
                                ConvertQtyOrdered(item, item.uom, item.selectedUom);
                                CalculateRate(item, item.selectedUom, item.uom);
                               // Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            }
                            else{
                                item.batch.unshift({
                                    "batchDetails": "Select Batch"
                                });
                                item.uom = [{
                                        "uomDetails": "Select UOM"
                                    }],
                                    //item.selectedBatch = item.batch[0];
                                item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                                item.selectedUom = item.uom[0];
                                
                               
                                if(item.selectedBatch!=undefined){
                                    item.quantity = commonDetailService.getDefaultQuantity(item,item.quantityOrdered,item.selectedBatch.availableStockQuantity);
                                    if(item.quantity!=null && item.quantity!=undefined)
                               	  {
	                               	// AllocateQuantity(index, self.model.narcoticsIssueWorklistDetails, null);
	                               	ValidateQuantity(item); 
	                               	Calculate(item, self.model.narcoticsIssueWorklistDetails);
                               	  }
                                }
                                    else
                                    item.quantity = 0;
                                
                            		 item.itemtotalAmount = 0;
                                	 item.itemDiscount = 0;
                                	 item.taxAmount= 0;
                                	 item.netAmount= 0;
                                	 item.roundOff=0;
                            	
                            } 
                            angular.forEach(item.selectedItems, function (item1, index) {
                            	
                          	  // added by minesh
                              var clubData  = clubBatches(item1.batch);
                              item1.batch = clubData;    
                           // added by minesh
                           //   item1.quantityIssued=item.quantityIssued;
                              item1.quantityOrdered=item.quantityOrdered;
                              item1.finalOrderedQty = item.quantityOrdered;
                              item1.taxPercentage=item.taxPercentage;
//                              item1.itemtotalAmount = item1.totalAmount;
//                              item1.itemDiscount = item1.discountAmount;
                              if(item1.totalAmount != undefined){
                            	  item1.itemtotalAmount = item1.totalAmount;
                            	  item1.itemDiscount = item1.discountAmount;
                            	  item1.discountPerItem = (100*(item1.discountAmount/item1.totalAmount));
                            	  if(item1.discountPerItem){
                              		item1.discountPerItem = item1.discountPerItem.toFixed(2);
                              	}
                          	}else{
                          		item1.itemtotalAmount = 0;
                          		item1.itemDiscount = 0;
                          		item1.taxAmount= 0;
                          		item1.netAmount= 0;
                          		item1.roundOff=0;
                          	}
                              item1.substituteBtn=true;
                              if (item1.status == 'Completed') {
                                  item1.itemDisabled = true;
                              }
                              var isBatchFount = item1.batch.find(function (x) {
                                  return !!x.isSelected;
                              });

                              if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                  $scope.isValidated = true;
                              }  

                              if (!!isBatchFount) {
                              	item1.selectedBatch = item1.batch.find(function (x) {
                                      return !!x.isSelected;
                                  });
                                  item1.selectedUom = item1.selectedBatch.uom.find(function (obj) {
                                      //return obj.opUom;
                                	  return item1.selectedBatch.uomTypeId==obj.uomTypeId;
                                  });
                                  item1.stock = item1.selectedBatch.availableStockQuantity;
                                  item1.uom = item1.selectedBatch.uom;
                                //  AllocateQuantity(index, item.selectedItems,0);
                                  ConvertQtyOrdered(item1, item1.uom, item1.selectedUom);
                                  CalculateRate(item1, item1.selectedUom, item1.uom);
                                  //Calculate(item1, item.selectedItems);
                              }
                              else{
                              	item1.batch.unshift({
                                      "batchDetails": "Select Batch"
                                  });
                              	item1.uom = [{
                                          "uomDetails": "Select UOM"
                                      }],
                                //item1.selectedBatch = item1.batch[0];
                              	item1.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item1.batch);
                              	item1.selectedUom = item.uom[0];
                              	
                              	 if(self.model.narcoticsIssueWorklistDetails.statusId ==undefined && self.model.narcoticsIssueWorklistDetails.statusId ==null && self.model.narcoticsIssueWorklistDetails.statusId ==2){
                               	  ConvertQtyOrdered(item1, item1.uom, item1.selectedUom);
                                 }else{
                                 	
                                	 var found =  item1.batch.find(function (x) {
                                         return x.isSelected;
                                     });
                                   	
                                   var	parentItemQty = GetCalculateOrederQty(item1);
                                	
                                  	if(found == undefined){
                                  		if(item1.batch[0].uom.length>0){
                                  			uomConversion(item1, item1.batch[0],3,parentItemQty);
                                  		}
                                  	}else{
                                  		var selectedUomTypeId =  found.uomTypeId;
                                  		uomConversion(item1, found,selectedUomTypeId,parentItemQty);
                                  	}	
                                	
                                }
                              } 
                          });
                          
                        });
                        
                        
                        var tempList = [];
                        if(self.model.narcoticsIssueWorklistDetails.statusId == 18){
                        	
                        	   self.model.narcoticsIssueWorklistDetails.items.forEach(function (item) {
                        		   
                        		   if(item.status != 'Pending'){                      		   
                        		   var flag = false;
                        		   if(item.statusId == 9){
                        			   flag = true;
                        		   }else{
                        			   
                        			   if(tempList.length >0){
                            			   tempList.forEach(function (tempItem) {
                            					if(item.itemId == tempItem.itemId ){
                            						flag = true;
                            					}
                            			   })
                            			   
                                           if(tempList.length==1){
                                               self.model.narcoticsIssueWorklistDetails.items[0].isShow = false;
                                           }
                            		   }
                        		   }
                        		 
                               	var totalQty = 0;
                               	
                                var found =  item.batch.find(function (x) {
                                    return x.isSelected;
                                });
                              	 
                              	var uomTypeId = item.uomTypeId !=undefined ?item.uomTypeId : (found !=undefined ?found.uomTypeId : undefined );
                              	if(uomTypeId == undefined){
                              		item.itemDisabled = true;
                              	}
                               	
                               	if(flag == false){
                               		
    		                           	  self.model.narcoticsIssueWorklistDetails.items.forEach(function (item1) {
    		                                	if(item.itemId == item1.itemId ){
    		                                		totalQty += GetCalculateOrederQty(item1);
    		                                		
    		                                		if(item1.selectedItems !=undefined){
        		                                		item1.selectedItems.forEach(function (item2) {
        		                                     		totalQty += GetCalculateOrederQty(item2);
        		                                	  });
        		                                	}
    		                                	}
    		                                	
    		                             });
    		                  		   
    		                           var assignQty = 0;
     		                  		   var newOrderQty = 0;
     		                  		   var newRow = false;
     		                           	 	if(item.isFromAssignPicker == true){

                                                   assignQty = item.pickedQuantity - totalQty;
                                                   newOrderQty = item.quantityOrdered;
                                                  if(item.pickedQuantity > totalQty){
                                                        	newRow = true;
                                                    }
                                                  
     		                           	 	}else{
                                                   newOrderQty = item.quantityOrdered - totalQty;

                                                    if(item.quantityOrdered > totalQty){
                                                         newRow = true;
                                                    }
     		                           	 	}
     		                  		   if(newRow){
     		                  		   
     		                  			   var newArr = {};
    		                  			   
    		                  			 newArr.id = null,
    		                  			 newArr.itemCode = item.itemCode,
    		                  			 newArr.itemName = item.itemName,
    		                  			 newArr.itemId = item.itemId,
    		                  			 newArr.formulationTypeId= item.formulationTypeId,
    		                  			 newArr.isNarcotic= item.isNarcotic,
    		                  			 newArr.patientClassPercentage = item.patientClassPercentage,
    		                  			 newArr.genericName = item.genericName,
    		                  			 newArr.genericId =  item.genericId,
    		                  			 newArr.manufacturerName = item.manufacturerName,
    		                  			 newArr.interventionFlag = item.interventionFlag,
    		                  			 newArr.rackNo = item.rackNo,
    		                  			 newArr.quantityOrdered = newOrderQty,
    		                  			 newArr.quantity = 0,
    		                  			 newArr.priorityId = item.priorityId,
    		                  			 newArr.storeId = item.storeId,
    		                  			 newArr.batch = item.batch,
    		                  			 newArr.markUpDown= item.markUpDown,
    		                  			 newArr.markUpDownPer= item.markUpDownPer,
    		                  			 newArr.markupStatus = item.markupStatus,
    		                  			 newArr.addedItem = item.addedItem,
    		                  			 newArr.substituteNewQty = item.substituteNewQty,
    		                  			 newArr.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch),
    		                  			 newArr.uom = item.uom,
    		                  			 newArr.selectedUom = null,
    		                  			 newArr.taxId = item.taxId,
    		                  			 newArr.tax = item.tax,
    		                  			 newArr.issuedQty = item.issuedQty,
    		                  			 newArr.quantityIssued = 0 ,
    		                  			 newArr.taxPercentage = item.taxPercentage,
    		                  			 newArr.itemDiscount = item.itemDiscount,
    		                  			 newArr.newStock = item.stockAvailable,
    		                  			 newArr.availableStockQuantity = item.stockAvailable,
    		                  			 newArr.productCategoryId = item.productCategoryId,
    		                  			 newArr.substituteBtn = false ,
    		                  			 newArr.isShow = true,
	    		                  			newArr.dose  = item.dose,
	    		                  			newArr.doseUnit  = item.doseUnit,
    		                  			// newArr.parentIssueDetailsId = item.id == null ? item.parentIssueDetailsId : item.id,
    		                  			 newArr.finalOrderedQty  = item.finalOrderedQty,
    		                  			 newArr.isFromAssignPicker = item.isFromAssignPicker,
    		                  			 newArr.parentIssueDetailsId = item.id,
    		                  			 newArr.prescriptionDetailId = item.prescriptionDetailId,
    		                  			 newArr.pickedQuantity = assignQty,
    		                  			 newArr.instructionDesc = item.instructionDesc;
                                        newArr.duration = item.duration;
                                        newArr.frequencyDesc = item.frequencyDesc;
                                        newArr.finalOrderedQty = newOrderQty;
                                        newArr.itemDisabled = false;
                                        newArr.disabled = false;
                                        newArr.isThreeUOMApplicable = item.isThreeUOMApplicable;
                                        if(newArr.selectedBatch!=undefined)
                                   	      {
                                        	newArr.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                            ipUom: true
                                            }, true)[0];
                                   	      }
                                        newArr.selectedUom = newArr.selectedUom!=undefined ? newArr.selectedUom : newArr.uom[0];
                                        tempList.push(newArr);
    		                  		   }
                               	}
                        	   }
                        		   
                               });
                        	   if(tempList.length >0){
                        		   $scope.saveFlag = false; 
                    			   tempList.forEach(function (tempItem) {
                    				   self.model.narcoticsIssueWorklistDetails.items.push(tempItem);
                    			   })
                    		   }
                            } if(self.model.narcoticsIssueWorklistDetails.statusId == 2){
                            	$scope.saveFlag = false; 
                            }
                    }
                    angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function(val1){
                        if(!val1.itemDisabled && !val1.disabled){
                            $scope.orderQtyTotalSum = $scope.orderQtyTotalSum + val1.quantityOrdered;	
                        }
                    });
                    $rootScope.stopLoader();
                });
        }

        $scope.getDetailsEntitlement = function () {
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }  
        }

        var BatchChange = function (index, bill, batch, parentIndex) {
         
        	self.model.parentIndex=parentIndex;
           
            var item = null;

            //Assign Item and UOM
            if (parentIndex == null) {
                if(bill.items[index].isThreeUOMApplicable == false){
                    if(batch.uom[2] == undefined || batch.uom[2] == null){
                        bill.items[index].uomListData = batch.uomListData;
                        bill.items[index].selectedBatch.uomListData = batch.uomListData;
                    }
                    else{
                        bill.items[index].uomListData = batch.uom;
                        bill.items[index].selectedBatch.uomListData = batch.uom;
                    }
                    bill.items[index].uom = [];
                    bill.items[index].selectedBatch.uom = [];
                    angular.forEach(batch.uomListData, function(u){
                        if(u.uomTypeId != 2){
                            bill.items[index].selectedBatch.uom.push(u);
                            bill.items[index].uom.push(u);
                        }
                    })
                    item = bill.items[index];
                }
                else{
                    bill.items[index].uom = batch.uom;
                    item = bill.items[index];
                }
            } else {
                if(bill.items[parentIndex].selectedItems[index].isThreeUOMApplicable == false){
                    bill.items[parentIndex].selectedItems[index].uomListData = batch.uom;
                    bill.items[parentIndex].selectedItems[index].uom = [];
                    angular.forEach(batch.uom, function(u){
                        if(u.uomTypeId != 2){
                            bill.items[parentIndex].selectedItems[index].uom.push(u);
                        }
                    })
                    item = bill.items[parentIndex].selectedItems[index];
                }
                else{
                    bill.items[parentIndex].selectedItems[index].uom = batch.uom;
                    item = bill.items[parentIndex].selectedItems[index];
                }
            }

            if (!!batch.batchId) {
                //Select Default UOM
                item.selectedUom = $filter('filter')(item.uom, {
                    opUom: true
                }, true)[0];

                //"Select UOM" option from UOM dropdown
                if (!item.selectedUom) {
                    item.selectedUom = item.uom[0];
                }
                
                //Caclulate amount including discount
                // Calculate(item, bill);
                $scope.calculateAtCopCalculation(item, bill);
               // CalculateAmount(item);

                //Convert Ordered Quantity
                ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);

                if(item.quantity !=undefined && item.quantity != 0 ){
                	// AllocateQuantity(index, bill, parentIndex);
                	$scope.calculateAtCopCalculation(item, bill);
                	}
                //Allocate quantity
                //AllocateQuantity(index, bill, parentIndex);               

                //Get current stock for the selected batch
                var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                $rootScope.startLoader();
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        item.stock= response.data.availableqty;
                        item.newStock= response.data.availableqty;
                        //Ganesh
                        item.quantity = commonDetailService.getDefaultQuantity(item,item.quantityOrdered,response.data.availableqty);
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                        if(item.quantity == 0 || item.rate == undefined){
                            item.rate = 0;
                        }
                        if($scope.atCop == true){
                          item.rate = item.selectedBatch.actualLendingPrice;
                          $scope.calculateAtCopCalculation(item, bill);
                        }
                        else{
                            if(item.rate){
                                item.rate = item.rate;
                            }
                            else{
                                item.rate = item.selectedBatch.rate;
                                $scope.calculateAtCopCalculation(item, bill);
                            }
                        }
                        // item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                        //     uomTypeId: item.selectedBatch.uomTypeId
                        // }, true)[0];
                        if(item.selectedBatch != undefined && item.selectedUom != undefined && item.selectedBatch.uom != undefined){
                            item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                return obj.uomTypeId == item.selectedBatch.uomTypeId;
                            });   
                            if(item.selectedUom == undefined){
                            	item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
									uomTypeId: item.selectedBatch.uomTypeId
								}, true)[0];
                            }  
                        }
                        ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom)
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    }
                );

            } else{
                item.selectedUom = item.uom[0];
                item.quantity = null;
                item.rate = 0;
            }
        }

        var ConvertQtyOrdered = function (item, uom, uomList) {
            var newUOM = [];
            if(item.isThreeUOMApplicable == false){
                if(item.uomListData == undefined || item.uomListData == null){
                    item.uomListData = item.uom;
                }
                if(item.uomListData[2] == undefined || item.uomListData[2] == null){
                    item.uoms.find(function (value) {
                        if (!!value.uomTypeId) {
                            newUOM.push(value);
                        }
                    });
                }
                else{
                    item.uomListData.find(function (value) {
                        if (!!value.uomTypeId) {
                            newUOM.push(value);
                        }
                    });
                }
            }
            else{
                uom.find(function (value) {
                    if (!!value.uomTypeId) {
                        newUOM.push(value);
                    }
                });
            }

            if(item.quantityOrdered == undefined || item.quantityOrdered == null || item.quantityOrdered == ""){
                item.quantityOrdered = 0;
            }
            var quantityOrdered = item.quantityOrdered;
            var pickedQty = item.pickedQuantity!=undefined ?item.pickedQuantity : 0;
//            var stockAvailable = item.stock || 0;
            if(item.selectedBatch != null){
            	  var stockAvailable = item.stock!=undefined ? item.stock:item.selectedBatch.availableStockQuantity!=undefined?item.selectedBatch.availableStockQuantity:0;
            }
          

           // item.quantity = 0;

           if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
            item.quantity = 0;
           }
            
            if (uomList != null && !!uomList.uomTypeId ) {
                if(item.isThreeUOMApplicable == false){
                    if(self.model.narcoticsIssueWorklistDetails.statusId == 18 || self.model.narcoticsIssueWorklistDetails.statusId == 4){
                        if ( uomList.uomTypeId == 1 ) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantityIssued +"*"+ newUOM[0].conversion);
                            item.receivedQtyCalculation = (item.quantityIssued * newUOM[0].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        } else if ( uomList.uomTypeId == 2) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
                        } else if ( uomList.uomTypeId == 3) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantityIssued);
                            item.receivedQtyCalculation = (item.quantityIssued);
                            item.uomUnitData = newUOM[2].uomUnit;
                        }
                    }
                    else{
                        if ( uomList.uomTypeId == 1 ) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantity +"*"+ newUOM[0].conversion);
                            item.receivedQtyCalculation = (item.quantity * newUOM[0].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        } else if ( uomList.uomTypeId == 2) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
                        } else if ( uomList.uomTypeId == 3) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = newUOM[2].uomUnit;
                        }
                    }
                   
                }
                else{
                    if(self.model.narcoticsIssueWorklistDetails.statusId == 18 || self.model.narcoticsIssueWorklistDetails.statusId == 4){
                        if ( uomList.uomTypeId == 1 ) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantityIssued +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                            item.receivedQtyCalculation = (item.quantityIssued * newUOM[0].conversion * newUOM[1].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        } else if ( uomList.uomTypeId == 2) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantityIssued +"*"+ newUOM[1].conversion);
                            item.receivedQtyCalculation = (item.quantityIssued * newUOM[1].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        } else if ( uomList.uomTypeId == 3) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantityIssued);
                            item.receivedQtyCalculation = (item.quantityIssued);
                            item.uomUnitData = newUOM[2].uomUnit;
                        }
                    }
                    else{
                        if ( uomList.uomTypeId == 1 ) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * newUOM[0].conversion * newUOM[1].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        } else if ( uomList.uomTypeId == 2) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[1].conversion * newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[1].conversion * newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantity +"*"+ newUOM[1].conversion);
                            item.receivedQtyCalculation = (item.quantity * newUOM[1].conversion);
                            item.uomUnitData = newUOM[2].uomUnit;
                        } else if ( uomList.uomTypeId == 3) {
                            item.newQty = Math.ceil(quantityOrdered / (newUOM[2].conversion));
                            item.newPickedQty = Math.ceil(pickedQty / ( newUOM[2].conversion));
                            item.newStock = Math.ceil(stockAvailable / (newUOM[2].conversion));
                            item.receivedQtyCon = (item.quantity);
                            item.receivedQtyCalculation = (item.quantity);
                            item.uomUnitData = newUOM[2].uomUnit;
                        }
                    }
                }
            }else{
                item.stock = 0;
                item.newStock = 0;
            }
            if(item.onlySubButtonDisable)
            {
                item.newQty =undefined;
                item.quantityOrdered=undefined;
            }
            if(item.barcodeItem == true)
            {
            	item.newQty =undefined;
            	item.quantityOrdered=undefined;
            }
        }

        $scope.calculateReceivedQty = function (item) {
            if(item.quantity == undefined || item.quantity == null || item.quantity == ""){
                item.quantity = 0;
            }
            if(item.isThreeUOMApplicable == false){
                if(item.uomListData[2] == undefined || item.uomListData[2] == null){
                    if(item.selectedUom.uomTypeId == 1){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uoms[0].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uoms[0].conversion);
                        item.uomUnitData = item.uoms[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                    
                    }
                    else{
                        item.receivedQtyCon = (item.quantity);
                        item.receivedQtyCalculation = (item.quantity);
                        item.uomUnitData = item.uoms[2].uomUnit;
                    }
                }
                else{
                    if(item.selectedUom.uomTypeId == 1){
                        item.receivedQtyCon = (item.quantity +"*"+ item.uomListData[0].conversion);
                        item.receivedQtyCalculation = (item.quantity * item.uomListData[0].conversion);
                        item.uomUnitData = item.uomListData[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                    
                    }
                    else{
                        item.receivedQtyCon = (item.quantity);
                        item.receivedQtyCalculation = (item.quantity);
                        item.uomUnitData = item.uomListData[2].uomUnit;
                    }
                }
            }
            else{
                if(item.selectedUom.uomTypeId == 1){
                    item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
                else if(item.selectedUom.uomTypeId == 2){
                    item.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
                else{
                    item.receivedQtyCon = (item.quantity);
                    item.receivedQtyCalculation = (item.quantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                }
            }
        }

            var UomChange = function (index, bill, uom , parentIndex) {
                var item = null;
                if (parentIndex == null) {
                    item = bill.items[index];
                } else {
                    item = bill.items[parentIndex].selectedItems[index];
                }
            $scope.selectedUom=uom;
          if (!!uom) {
                item.quantity = 0;
               Calculate(item, bill);
    
             //  CalculateStock(item, item.uom, item.selectedUom);
               //Convert Ordered Quantity
               ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
               if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                    $scope.isValidated = true;
                }
                else{
                    $scope.isValidated = false;
                }
                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                    $scope.isValidated = true;
                }
          } else {
            item.rate = 0;
          }
    
          //AllocateQuantity(index);
        
        }
        var CalculateStock = function (item, uom, uomList) {
            if (!!uomList.uomTypeId) {
              var stockAvailable = item.stock || 0;
      
              uomList.uomDetails = uomList.uomDetails.split(' ')[0];
              if (uomList.uomDetails == "P") {
                item.newStock = parseInt(stockAvailable / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
               // item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
              //  item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
              } else if (uomList.uomDetails == "S") {
                item.newStock = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion));
               // item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion * uom[3].conversion));
              //  item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion * uom[3].conversion));
              } else if (uomList.uomDetails == "D") {
                item.newStock = parseInt(stockAvailable / (uom[2].conversion));
               // item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[3].conversion));
               // item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[3].conversion));
              }
            } else {
              item.stock = 0;
              item.newStock = 0;
             // item.newMaxOrderQuantity = 0;
             // item.newMaxStoreQuantity = 0;
            }
          }
        $scope.calculateAtCopCalculation=function(item, bill){

            // if(item.quantity != 0){
        	if($scope.globalFlag==1)
    		{
    		$scope.atCop=$scope.atCopSelected;
    		if(item.disabled==false)
    			{
    			 if($scope.atCop == true){
                     item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
                     item.rate = item.selectedBatch.actualLendingPrice;
                     item.mrp = item.rate;
                 }
                 else{
                     if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                         item.selectedBatch.sellingPrice = item.selectedBatch.rate;
                         item.rate = item.selectedBatch.rate;
                     }
                     else{
                         item.selectedBatch.sellingPrice = item.rate;
                         item.rate = item.rate;
                     }
                 }
    			}
    		}
        	else
        		{
        		if(item.disabled==true)
            	{
            	$scope.atCop=item.atCop;
            	}
            	else
            		{
            		$scope.atCop=$scope.atCopSelected;
            		}
                    if($scope.atCop == true){
                        item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
                        item.rate = item.selectedBatch.actualLendingPrice;
                        item.mrp = item.rate;
                    }
                    else{
                        if(item.selectedBatch.rate != undefined || item.selectedBatch.rate != null){
                            item.selectedBatch.sellingPrice = item.selectedBatch.rate;
                            item.rate = item.selectedBatch.rate;
                        }
                        else{
                            item.selectedBatch.sellingPrice = item.rate;
                            item.rate = item.rate;
                        }
                    }
        		}
                var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
                item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
                self.model.beforeCalculation = item;
                // if($scope.atCop == true){
                //     item.mrp = item.rate;
                // }
                var data = {
                		"mrp": item.mrp,
                        "isTaxInc": $scope.taxInclusive,
                        // "markup": !!item.markUpDown ? item.markUpDownPer : null,
                        // "markdown": !item.markUpDown ? item.markUpDownPer : null,
                        "markup": item.markUpDown,
                        "markUpDownPer" : item.markUpDownPer,
                        "discount": item.discountPerItem ? item.discountPerItem : 0,
                        "isDiscPercent": (bill.discountType != undefined && bill.discountType == 1 )? true : false,
                        "tax": item.taxPercentage,
                        "qty": item.quantity,
                        // "qty": item.quantity ? item.quantity : item.qtyPrescribed,
                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                        "patientClassPercentage": item.patientClassPercentage,
                }
                if(!data.discount){
                    data.discount = 0;
                }
                var response = commonDetailService.calculateAmount(data);
                item.rate = response.rate;
                item.taxAmount = response.taxAmount;
                item.responseTaxAmount =  response.taxAmount;
                item.unitDiscountAmount = response.unitDiscountAmount;
                item.itemDiscount =  response.discountAmount;
                item.discountAmount = response.discountAmount;
                item.itemtotalAmount = response.totalAmount
                item.totalAmount = response.totalAmount;
                item.netAmount = response.netAmount;
//                item.selfPayable = response.netAmount;
//                item.creditPayable = response.creditPayable;
                item.packageConsumptionAmt = response.packageConsumptionAmt;
                item.patientClassAmount = response.patientClassAmount;
                item.unitPatientClassAmount = response.unitPatientClassAmount;
                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                    $scope.isValidated = true;
                }
                else{
                    $scope.isValidated = false;
                }
                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                    $scope.isValidated = true;
                }
            // }
            if(item.selectedItems != undefined && item.selectedItems != null && item.selectedItems.length > 0){
                angular.forEach(item.selectedItems, function(s){
                	$scope.afterSaveUOM;
                	if($scope.globalFlag==1)
            		{
            		$scope.atCop=$scope.atCopSelected;
            		if(s.disabled==false || s.statusId!=4)
            			{
            			angular.forEach(s.batch, function(a){
                            if($scope.atCop == true){
                            	if(a.isSelected)
                            		{
                            		a.sellingPrice = a.actualLendingPrice;
                                    a.rate = a.actualLendingPrice;
                                    $scope.afterSaveUOM=a.uom;
                            		}
                            	 if(s.selectedBatch!=undefined)
                            		{
                            		 s.selectedBatch.sellingPrice = s.selectedBatch.actualLendingPrice;
                                     s.rate = s.selectedBatch.actualLendingPrice;
                            		}
                            }
                            else{
                            	if(a.isSelected)
                        		{
                        		a.sellingPrice = a.rate;
                                a.rate = a.rate;
                                $scope.afterSaveUOM=a.uom;
                        		}
                            	if(s.selectedBatch!=undefined)
                        		{
                            		s.selectedBatch.sellingPrice = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                                    s.rate = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                        		}
                            }
                        });
            			}
            		}
                	else
                		{
                	if(s.disabled==true || s.statusId==4)
                	{
                	$scope.atCop=s.atCop;
                	}
                	else
                		{
                		$scope.atCop=$scope.atCopSelected;
                		}
                    angular.forEach(s.batch, function(a){
                        if($scope.atCop == true){
                        	if(a.isSelected)
                        		{
                        		a.sellingPrice = a.actualLendingPrice;
                                a.rate = a.actualLendingPrice;
                                $scope.afterSaveUOM=a.uom;
                        		}
                        	 if(s.selectedBatch!=undefined)
                        		{
                        		 s.selectedBatch.sellingPrice = s.selectedBatch.actualLendingPrice;
                                 s.rate = s.selectedBatch.actualLendingPrice;
                        		}
                        }
                        else{
                        	if(a.isSelected)
                    		{
                    		a.sellingPrice = a.rate;
                            a.rate = a.rate;
                            $scope.afterSaveUOM=a.uom;
                    		}
                        	if(s.selectedBatch!=undefined)
                    		{
                        		s.selectedBatch.sellingPrice = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                                s.rate = s.selectedBatch.rate!=undefined ? s.selectedBatch.rate : s.selectedBatch.sellingPrice;
                    		}
                        }
                    });
                		}
                    var sellingPrice = !!s.selectedBatch ? commonDetailService.fixedDecimal(s.selectedBatch.sellingPrice!=undefined ||s.selectedBatch.sellingPrice!=null ? s.selectedBatch.sellingPrice :s.selectedBatch.rate) : 0;
                    s.mrp = commonDetailService.calculateRate(s.selectedBatch!=undefined ?s.selectedBatch.uom : $scope.afterSaveUOM, s.selectedUom!=undefined ? s.selectedUom.uomTypeId :s.uomTypeId, sellingPrice!=undefined || sellingPrice!=0 ? sellingPrice : s.rate);
                    self.model.beforeCalculation = s;
                    var data = {
                    		 "mrp": s.mrp!=null || s.mrp!=undefined || !isNaN(s.mrp) ? s.mrp : s.selectedBatch!=undefined ? s.selectedBatch.rate:s.rate,
                                     "isTaxInc": $scope.taxInclusive,
                                     "markup": s.markUpDown,
                                     "markUpDownPer" : s.markUpDownPer,
                                     "discount": ((bill.discount == 0 || bill.discount == undefined) || (s.discountPerItem== 0 || bill.discountPerItem == undefined))  ? s.discountPerItem : bill.discount,
                                     "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                     "tax": s.taxPercentage,
                                     "qty": s.quantity ? s.quantity : s.newDispQty,
                                     "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                     "patientClassPercentage": s.patientClassPercentage,
                    }
                    
                    if(!data.discount){
                        data.discount = 0;
                    }
                    var response = commonDetailService.calculateAmount(data);
                    s.rate = response.rate;
                    s.taxAmount = response.taxAmount;
                    s.responseTaxAmount =  response.taxAmount;
                    s.unitDiscountAmount = response.unitDiscountAmount;
                    s.itemDiscount =  response.discountAmount;
                    s.discountAmount = response.discountAmount;
                    s.itemtotalAmount = response.totalAmount
                    s.totalAmount = response.totalAmount;
                    s.netAmount = response.netAmount;
//                    s.selfPayable = response.netAmount;
//                    s.creditPayable = response.creditPayable;
                    s.packageConsumptionAmt = response.packageConsumptionAmt;
                    s.patientClassAmount = response.patientClassAmount;
                    s.unitPatientClassAmount = response.unitPatientClassAmount;
                    if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                        $scope.isValidated = true;
                    }
                    else{
                        $scope.isValidated = false;
                    }
                    if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                        $scope.isValidated = true;
                    }    
                });
            }
            CalculateAmount(item);
        
        }
        var Calculate = function(item, bill){
        	// if(item.discountPerItem>100)
    		// {
    		//  growl.error("Discount should be less than or equal to 100%");
    		//  item.discountPerItem=0;
    		//  return false;
    		// }
            if($scope.atCop == true){
                item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
                item.rate = item.selectedBatch.actualLendingPrice;
                item.mrp = item.rate;
            }
            var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
            item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
            self.model.beforeCalculation = item;
            // if($scope.atCop == true){
            //     item.mrp = item.rate;
            // }
            var data = {
                "mrp": item.mrp,
                "isTaxInc": $scope.taxInclusive,
                // "markup": !!item.markUpDown ? item.markUpDownPer : null,
                // "markdown": !item.markUpDown ? item.markUpDownPer : null,
                "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,
                "discount":  item.discountPerItem ? item.discountPerItem : 0,
                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                "tax": item.taxPercentage,
                "qty": item.quantity != undefined ? item.quantity : item.newQty,
                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                "patientClassPercentage": item.patientClassPercentage,
            }
  
            var response = commonDetailService.calculateAmount(data);
  
            item.rate = response.rate;
            item.taxAmount = response.taxAmount;
            item.responseTaxAmount =  response.taxAmount;
            item.unitDiscountAmount = response.unitDiscountAmount;
            item.itemDiscount =  response.discountAmount;
            item.itemtotalAmount = response.totalAmount;
            item.netAmount = response.netAmount;
            item.selfPayable = response.netAmount;
            item.creditPayable = response.creditPayable;
            item.packageConsumptionAmt = response.packageConsumptionAmt;
            item.patientClassAmount = response.patientClassAmount;
            $scope.patientClassAmount = response.patientClassAmount;
            item.unitPatientClassAmount = response.unitPatientClassAmount;
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }
            else{
                $scope.isValidated = false;
            }
            if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                $scope.isValidated = true;
            }
        }

        var onChangeDiscount = function(item, method){
            if(validateDiscount(item,method)){
                if(method =='linewise'){
                    angular.forEach(item.items, function (value, key) {
                        if(value.discountPerItem){
                            if(value.discountPerItem > 100){
                                growl.error("Discount should be less than or equal to 100%. ")
                                value.discountPerItem = 0;
                                value.patientClassPercentage = 0;
                            }else{
                                item.discount = 0;
                                item.discountType = 1;
                            }
    
                            if(value.discountPerItem != ""){
                                value.patientClassPercentage = 0;
                                value.patientClassAmount = 0;
                            }
                            else{
                                value.patientClassPercentage =  $scope.patientClassPercentage;
                                value.patientClassAmount = $scope.patientClassAmount;
                            }
                        }
                        if(value.selectedItems){
                            angular.forEach(value.selectedItems, function(value1){
                                if(value1.discountPerItem > 100){
                                    growl.error("Discount should be less than or equal to 100%. ")
                                    value1.discountPerItem = 0;
                                    value1.patientClassPercentage = 0;
                                }else{
                                    item.discount = 0;
                                    item.discountType = 1;
                                }
        
                                if(value1.discountPerItem != ""){
                                    value1.patientClassPercentage = 0;
                                    value1.patientClassAmount = 0;
                                }
                                else{
                                    value1.patientClassPercentage =  $scope.patientClassPercentage;
                                    value1.patientClassAmount = $scope.patientClassAmount;
                                }
                            });
                        }
                    }); 
                }
                else{
                    angular.forEach(item.items, function (value, key) {
                        // bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
                  
                        // value.discountPerItem = item.discount;
            	 
                    });
                }
            }
            else{
                growl.error("Discount should be less than or equal to 100%.")
                    angular.forEach(item.items, function (value, key) {
            	        value.discountPerItem = 0;
                    })
                }
            }

      var validateDiscount = function(item, method){
        var regexp = /^(\d{0,3}(\.\d{0,2})?)$/;
        var isValid = true;
        if(method =='linewise'){
            angular.forEach(item.items, function (value, key) {
                if(value.discountPerItem){
                    if(regexp.test(Number(value.discountPerItem))){
                        isValid = true;
                    }
                    else{
                        isValid = false;
                        value.discountPerItem = 0;
                    }
                }
                if(value.selectedItems){
                    angular.forEach(value.selectedItems, function(value1){
                        if(regexp.test(Number(value1.discountPerItem))){
                            isValid = true;
                        }
                        else{
                            isValid = false;
                            value1.discountPerItem = 0;
                        }
                    });
                }
            }); 
            }
            else{
                // if(regexp.test(Number(item.discount))){
                //     isValid = true;
                //  }
                //  else{
                //     isValid = false;
                //     item.discount = 0;
                //  }
                if(self.model.narcoticsIssueWorklistDetails.discountType == 1){
                    if(item.discount > 100){
                        isValid = false;
                        item.discount = 0;
                        growl.error("Please enter valid discount");
                    }
                    else{
                        isValid = true;
                    }
                }
                else{
                    isValid = true;
                }
               
            }
            return isValid;
      }
        var CalculateRate = function (item, uom, uomList) {
            if (!!uom) {
                var sellingPrice = !!item.selectedBatch ? item.selectedBatch.sellingPrice : 0;

                var conversion = 1;
                var flag = false;

                angular.forEach(uomList, function (value, key) {
                    if (value.uomTypeId == uom.uomTypeId || flag) {
                        conversion = conversion * value.conversion;
                        flag = true;
                    }
                });

                item.rate = conversion * sellingPrice;
            } else {
                item.rate = 0;
            }
        }

        var OnKeyPressQuantity = function (item) {
            if (!item.quantityIssued) {
                item.totalAmount = 0;
                item.itemDiscount = 0;
                item.taxAmount = 0;
                item.netAmount = 0;
            }
        }

        var SetDecimal = function () {
            if (isNaN(self.model.narcoticsIssueWorklistDetails.discount) || !self.model.narcoticsIssueWorklistDetails.discount) {
                self.model.narcoticsIssueWorklistDetails.discount = 0;
            } else {
                self.model.narcoticsIssueWorklistDetails.discount = parseFloat(self.model.narcoticsIssueWorklistDetails.discount).toFixed(2);
            }
        }
        
        var ApplyDiscount = function (bill) {
            if(bill.discountType == '2' && bill.netAmountData == undefined){
                bill.netAmountData = bill.netAmount;
            }
            if(ApplyDiscountValidation(bill) && CheckQuantityForDiscount(bill)){
                // var discount = parseFloat(bill.discount);
                var item = bill.items;
                var discount = parseFloat(bill.discount);
                if (!isNaN(discount)) {
                    if (bill.discountType == '2') {
                    	//FOR RESET DISCOUNT
                    	var billAmount = 0;
                    	angular.forEach(bill.items, function (value, key) {
                            if(!value.itemDisabled){
                                if(!value.disabled){
                                    var data = {
                                        "mrp": value.mrp,
                                        "isTaxInc": $scope.taxInclusive,
                                        // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                        // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                        "markup": value.markUpDown,
                                        "markUpDownPer" : value.markUpDownPer,
                                        "discount":  0,
                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                        "tax": value.taxPercentage,
                                        "qty": value.quantity,
                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                        "discountPerItem":(discount*100)/bill.totalAmount,
                                        "patientClassPercentage": value.patientClassPercentage
                                    }
                                    var response = commonDetailService.calculateDisAmount(data);
                                    value.rate = response.rate;
                                    value.taxAmount = response.taxAmount;
                                    value.responseTaxAmount =  response.taxAmount;
                                    value.unitDiscountAmount = response.unitDiscountAmount;
                                    value.itemDiscount =  response.discountAmount;
                                    value.discountAmount = 0;
                                    value.itemtotalAmount = response.totalAmount
                                    value.totalAmount = response.totalAmount;
                                    value.netAmount = response.netAmount;
                                    value.selfPayable = response.netAmount;
                                    value.discountPerItem= 0;
                                    value.itemDiscount  =  0;
                                    value.patientClassAmount = response.patientClassAmount;
                                    value.creditPayable = response.creditPayable;
                                    value.packageConsumptionAmt = response.packageConsumptionAmt;
                                    
                                    billAmount = billAmount+parseFloat(value.netAmount);
                                    if(value.selectedItems){
                                        angular.forEach(value.selectedItems, function(svalue){
                                        	if(!svalue.disabled && !svalue.itemDisabled){
                                            	var data = {
                                                    "mrp": svalue.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    "markup": svalue.markUpDown,
                                                    "markUpDownPer" : svalue.markUpDownPer,
                                                    "discount": 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": svalue.taxPercentage,
                                                    "qty": svalue.quantity,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":(discount*100)/bill.totalAmount,
                                                    "patientClassPercentage": svalue.patientClassPercentage
                                                }
                                    
                                                var res = commonDetailService.calculateDisAmount(data);
                                                svalue.rate = res.rate;
                                                svalue.taxAmount = res.taxAmount;
                                                svalue.responseTaxAmount =  res.taxAmount;
                                                svalue.unitDiscountAmount = res.unitDiscountAmount;
                                                svalue.totalAmount = res.totalAmount;
                                                svalue.itemtotalAmount = res.totalAmount;
                                                svalue.netAmount = res.netAmount;
                                                svalue.itemDiscount =  res.discountAmount;
                                                svalue.discountPerItem = 0;
                                                svalue.discountAmount =   0;
                                                svalue.selfPayable = res.netAmount;
                                                svalue.itemDiscount  =  0;
                                                svalue.patientClassAmount = res.patientClassAmount;
                                                svalue.creditPayable = res.creditPayable;
                	                            svalue.packageConsumptionAmt = res.packageConsumptionAmt;
                	                            
                	                            billAmount = billAmount+parseFloat(svalue.netAmount);
                                            }
                                        });
                                    }
                                }
                                else
                                {
                                    angular.forEach(value.selectedItems, function (value, key) {
                                        if(!value.disabled && !value.itemDisabled){
                                            var data = {
                                                "mrp": value.mrp,
                                                "isTaxInc": $scope.taxInclusive,
                                                //  "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                                //  "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                                "markup": value.markUpDown,
                                                "markUpDownPer" : value.markUpDownPer,
                                                "discount":  0,
                                                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                "tax": value.taxPercentage,
                                                "qty": value.quantity,
                                                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                "discountPerItem":(discount*100)/bill.totalAmount,
                                                "patientClassPercentage": value.patientClassPercentage
                                            }
                         
                                            var response = commonDetailService.calculateDisAmount(data); 
                                            value.rate = response.rate;
                                            value.taxAmount = response.taxAmount;
                                            value.responseTaxAmount =  response.taxAmount;
                                            value.unitDiscountAmount = response.unitDiscountAmount;
                                            value.itemDiscount =  0;
                                            value.itemtotalAmount = response.totalAmount
                                            value.totalAmount = response.totalAmount;
                                            value.netAmount = response.netAmount;
                                            value.discountPerItem =0;
                                            value.discountAmount = 0;
                                            value.selfPayable = response.netAmount;
                                            value.patientClassAmount = response.patientClassAmount;
                                            value.creditPayable = response.creditPayable;
                                            value.packageConsumptionAmt = response.packageConsumptionAmt;
                                        }
                                    });
                                    value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
                                    value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                    billAmount = billAmount+parseFloat(value.netAmount);
                                }
                                if(bill.netAmountData == undefined){
                                    bill.netAmountData = bill.netAmount;
                                }
                                bill.netAmount = billAmount;
                            }
                        });
                    	//END
                    	
                        if (discount <= bill.totalAmount) {
                            var itemDiscount = discount / bill.totalAmount;

                            angular.forEach(bill.items, function (value, key) {
                                if(!value.itemDisabled){
                                    if (value.itemtotalAmount > itemDiscount) {
                                        value.itemDiscount = value.itemtotalAmount * itemDiscount;
                                    } else {
                                        value.itemDiscount = 0;
                                    }
                                    if(!value.disabled){
                                        var data = {
                                            "mrp": value.mrp,
                                            "isTaxInc": $scope.taxInclusive,
                                            // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                            // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                            "markup": value.markUpDown,
                                                    "markUpDownPer" : value.markUpDownPer,
                                            "discount": bill.discount ? bill.discount : 0,
                                            "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                            "tax": value.taxPercentage,
                                            "qty": value.quantity,
                                            "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                            "discountPerItem":(discount*100)/bill.totalAmount,
                                            "patientClassPercentage": value.patientClassPercentage
                                        }
                        
                                        var response = commonDetailService.calculateDisAmount(data);
                                        value.rate = response.rate;
                                        value.taxAmount = response.taxAmount;
                                        value.responseTaxAmount =  response.taxAmount;
                                        value.unitDiscountAmount = response.unitDiscountAmount;
                                        value.itemDiscount =  response.discountAmount;
                                        value.discountPerItem=  response.discountPerItem;
                                        value.discountAmount = value.itemtotalAmount * value.discountPerItem * 0.01;
                                        value.itemtotalAmount = response.totalAmount
                                        value.totalAmount = response.totalAmount;
                                        value.netAmount = response.netAmount;
                                        value.selfPayable = response.netAmount;
                                        value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem * 0.01;
                                        value.patientClassAmount = response.patientClassAmount;
                                        value.creditPayable = response.creditPayable;
                                        value.packageConsumptionAmt = response.packageConsumptionAmt;
                                        if(value.selectedItems){
                                            angular.forEach(value.selectedItems, function(svalue){
                                            	if(!svalue.disabled && !svalue.itemDisabled){
                                                	var data = {
                                                        "mrp": svalue.mrp,
                                                        "isTaxInc": $scope.taxInclusive,
                                                        // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                        // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                        "markup": svalue.markUpDown,
                                                        "markUpDownPer" : svalue.markUpDownPer,
                                                        "discount": bill.discount ? bill.discount : 0,
                                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                        "tax": svalue.taxPercentage,
                                                        "qty": svalue.quantity,
                                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                        "discountPerItem":(discount*100)/bill.totalAmount,
                                                        "patientClassPercentage": svalue.patientClassPercentage
                                                    }
                                        
                                                    var res = commonDetailService.calculateDisAmount(data);
                                                    svalue.rate = res.rate;
                                                    svalue.taxAmount = res.taxAmount;
                                                    svalue.responseTaxAmount =  res.taxAmount;
                                                    svalue.unitDiscountAmount = res.unitDiscountAmount;
                                                    svalue.totalAmount = res.totalAmount;
                                                    svalue.itemtotalAmount = res.totalAmount;
                                                    svalue.netAmount = res.netAmount;
                                                    svalue.itemDiscount =  res.discountAmount;
                                                    svalue.discountPerItem =  response.discountPerItem;
                                                    svalue.discountAmount =   svalue.itemtotalAmount * svalue.discountPerItem * 0.01;
                                                    svalue.selfPayable = res.netAmount;
                                                    svalue.itemDiscount  =  svalue.itemtotalAmount * svalue.discountPerItem * 0.01;
                                                    svalue.patientClassAmount = res.patientClassAmount;
                                                    svalue.creditPayable = res.creditPayable;
                                                    svalue.packageConsumptionAmt = res.packageConsumptionAmt;
                                                }
                                            });
                                            value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
                                            value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                        }
                                    }
                                    else{
                                        angular.forEach(value.selectedItems, function (value, key) {
                                    	    if(!value.disabled && !value.itemDisabled){
                                                var data = {
                                                    "mrp": value.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    "markup": value.markUpDown,
                                                    "markUpDownPer" : value.markUpDownPer,
                                                    "discount": bill.discount ? bill.discount : 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": value.taxPercentage,
                                                    "qty": value.quantity,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":(discount*100)/bill.totalAmount,
                                                    "patientClassPercentage": value.patientClassPercentage
                                                }
                                
                                                var response = commonDetailService.calculateDisAmount(data);
                                
                                                value.rate = response.rate;
                                                value.taxAmount = response.taxAmount;
                                                value.responseTaxAmount =  response.taxAmount;
                                                value.unitDiscountAmount = response.unitDiscountAmount;
                                                value.totalAmount = response.totalAmount;
                                                value.itemtotalAmount = response.totalAmount
                                                value.netAmount = response.netAmount;
                                                value.itemDiscount =  response.discountAmount;
                                                value.discountPerItem = ((discount / bill.totalAmount) * 100).toFixed(2);
                                                value.discountAmount = (value.itemtotalAmount)*value.discountPerItem* 0.01;
                                                value.selfPayable = response.netAmount;
                                                value.patientClassAmount = response.patientClassAmount;
                                                value.creditPayable = response.creditPayable;
                                                value.packageConsumptionAmt = response.packageConsumptionAmt;
                                            }
                                        });
                                        value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
                                        value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                    }
                                
                                    self.model.isDiscountAppliedValid = false;
                                    self.model.isDiscountApplied = true;
                                    self.model.isAppliedDiscountValid = false; 
                                    var unitDiscountAmount = itemDiscount / value.quantity;
                                    bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
                                    CalculateAmount(value);
                                    angular.forEach(value.selectedItems, function (subvalue, subkey) {
                                	if(!subvalue.disabled && !subvalue.itemDisabled){ 
                                    	if (subvalue.itemtotalAmount > itemDiscount) {
                                            subvalue.itemDiscount = subvalue.itemtotalAmount * itemDiscount;
                                        } else {
                                            subvalue.itemDiscount = 0;
                                        }

                                        var unitDiscountAmount = itemDiscount / subvalue.quantity;
                                    
                                        bill.items[key].selectedItems[subkey]['unitDiscountAmount'] = unitDiscountAmount;
                                	}
                                    });
                                }
                            });
                        } 
                        else {
                            SetDiscountFlags();
                            // growl.error('Please enter discount amount less than Total Amount');
                        }
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                    } 
                    else if (bill.discountType == '1') {
                    	
                    	//FOR RESET DISCOUNT
                    	var billAmount = 0;
                    	angular.forEach(bill.items, function (value, key) {
                            if(!value.itemDisabled){
                                if(!value.disabled){
                                    var data = {
                                        "mrp": value.mrp,
                                        "isTaxInc": $scope.taxInclusive,
                                        // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                        // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                        "markup": value.markUpDown,
                                                    "markUpDownPer" : value.markUpDownPer,
                                        "discount":  0,
                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                        "tax": value.taxPercentage,
                                        "qty": value.quantity,
                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                        "discountPerItem":discount,
                                        "patientClassPercentage": value.patientClassPercentage
                                    }
                                    var response = commonDetailService.calculateDisAmount(data);
                                    value.rate = response.rate;
                                    value.taxAmount = response.taxAmount;
                                    value.responseTaxAmount =  response.taxAmount;
                                    value.unitDiscountAmount = response.unitDiscountAmount;
                                    value.itemDiscount =  response.discountAmount;
                                    value.discountAmount = 0;
                                    value.itemtotalAmount = response.totalAmount
                                    value.totalAmount = response.totalAmount;
                                    value.netAmount = response.netAmount;
                                    value.selfPayable = response.netAmount;
                                    value.discountPerItem= 0;
                                    value.itemDiscount  =  0;
                                    value.patientClassAmount = response.patientClassAmount;
                                    value.creditPayable = response.creditPayable;
                                    value.packageConsumptionAmt = response.packageConsumptionAmt;
                                    
                                    billAmount = billAmount+parseFloat(value.netAmount);
                                    if(value.selectedItems){
                                        angular.forEach(value.selectedItems, function(svalue){
                                        	if(!svalue.disabled && !svalue.itemDisabled){                                        	var data = {
                                                    "mrp": svalue.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    "markup": svalue.markUpDown,
                                                    "markUpDownPer" : svalue.markUpDownPer,
                                                    "discount": 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": svalue.taxPercentage,
                                                    "qty": svalue.quantity,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                     "discountPerItem":discount,
                                                     "patientClassPercentage": svalue.patientClassPercentage
                                                }
                                    
                                                var res = commonDetailService.calculateDisAmount(data);
                                                svalue.rate = res.rate;
                                                svalue.taxAmount = res.taxAmount;
                                                svalue.responseTaxAmount =  res.taxAmount;
                                                svalue.unitDiscountAmount = res.unitDiscountAmount;
                                                svalue.totalAmount = res.totalAmount;
                                                svalue.itemtotalAmount = res.totalAmount;
                                                svalue.netAmount = res.netAmount;
                                                svalue.itemDiscount =  res.discountAmount;
                                                svalue.discountPerItem = 0;
                                                svalue.discountAmount =   0;
                                                svalue.selfPayable = res.netAmount;
                                                svalue.itemDiscount  =  0;
                                                svalue.patientClassAmount = res.patientClassAmount;
                                                svalue.creditPayable = res.creditPayable;
                	                            svalue.packageConsumptionAmt = res.packageConsumptionAmt;
                	                            
                	                            billAmount = billAmount+parseFloat(svalue.netAmount);
                                            }
                                        });
                                    }
                                }else{

                                    angular.forEach(value.selectedItems, function (value, key) {
                                	    if(!value.disabled && !value.itemDisabled){
                                            var data = {
                                                "mrp": value.mrp,
                                                "isTaxInc": $scope.taxInclusive,
                                                //  "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                                //  "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                                "markup": value.markUpDown,
                                                "markUpDownPer" : value.markUpDownPer,
                                                "discount":  0,
                                                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                "tax": value.taxPercentage,
                                                "qty": value.quantity,
                                                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                "discountPerItem":discount,
                                                "patientClassPercentage": value.patientClassPercentage
                                            }
                         
                                            var response = commonDetailService.calculateDisAmount(data);
                         
                                            value.rate = response.rate;
                                            value.taxAmount = response.taxAmount;
                                            value.responseTaxAmount =  response.taxAmount;
                                            value.unitDiscountAmount = response.unitDiscountAmount;
                                            value.itemDiscount =  0;
                                            value.itemtotalAmount = response.totalAmount
                                            value.totalAmount = response.totalAmount;
                                            value.netAmount = response.netAmount;
                                            value.discountPerItem =0;
                                            value.discountAmount = 0;
                                            value.selfPayable = response.netAmount;
                                            value.patientClassAmount = response.patientClassAmount;
                                            value.creditPayable = response.creditPayable;
                                            value.packageConsumptionAmt = response.packageConsumptionAmt;
                                        }
                                    });
                                    value.discountPerItem= self.model.narcoticsIssueWorklistDetails.discount;
                                    value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                 
                                    billAmount = billAmount+parseFloat(value.netAmount);
                                }
                                bill.netAmount = billAmount;
                            }
                        });
                    	//END
                    	
                        if (self.model.narcoticsIssueWorklistDetails.discount <= discountLimit) {
                            angular.forEach(bill.items, function (value, key) {
                                if(!value.itemDisabled){
                                    value.itemDiscount = value.itemtotalAmount * discount * 0.01;

                                    var unitDiscountAmount = value.itemDiscount / value.quantity;
                                    bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;  
                                    if(!value.disabled){
                                        var data = {
                                            "mrp": value.mrp,
                                            "isTaxInc": $scope.taxInclusive,
                                            // "markup": !!value.markUpDown ? value.markUpDownPer : null,
                                            // "markdown": !value.markUpDown ? value.markUpDownPer : null,
                                            "markup": value.markUpDown,
                                                    "markUpDownPer" : value.markUpDownPer,
                                            "discount": bill.discount ? bill.discount : 0,
                                            "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                            "tax": value.taxPercentage,
                                            "qty": value.quantity,
                                            "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                            "discountPerItem":discount,
                                            "patientClassPercentage": value.patientClassPercentage
                                        }
                        
                                        var response = commonDetailService.calculateDisAmount(data);
                        
                                        value.rate = response.rate;
                                        value.taxAmount = response.taxAmount;
                                        value.responseTaxAmount =  response.taxAmount;
                                        value.unitDiscountAmount = response.unitDiscountAmount;
                                        value.itemDiscount =  response.discountAmount;
                                        value.itemtotalAmount = response.totalAmount
                                        value.totalAmount = response.totalAmount;
                                        value.netAmount = response.netAmount;
                                        value.selfPayable = response.netAmount;
                                        // var discountPer = (discount / bill.totalAmount)*100
                                        value.discountPerItem = discount;
                                        value.discountAmount = (value.totalAmount/100)*value.discountPerItem;
                                        value.itemDiscount  =  (value.itemtotalAmount *  discount) / 100;
                                        value.patientClassAmount = response.patientClassAmount;
                                        if(value.selectedItems){
                                   	
                                            angular.forEach(value.selectedItems, function(svalue){
                                    	        if(!svalue.disabled && !svalue.itemDisabled){
                                       	            var data = {
                                                        "mrp": svalue.mrp,
                                                        "isTaxInc": $scope.taxInclusive,
                                                        // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                        // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                        "markup": svalue.markUpDown,
                                                        "markUpDownPer" : svalue.markUpDownPer,
                                                        "discount": bill.discount ? bill.discount : 0,
                                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                        "tax": svalue.taxPercentage,
                                                        "qty": svalue.quantity,
                                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                        "discountPerItem":discount,
                                                        "patientClassPercentage": svalue.patientClassPercentage
                                                    }
                                       	            var response = commonDetailService.calculateDisAmount(data);
                        
                                                    svalue.rate = response.rate;
                                                    svalue.taxAmount = response.taxAmount;
                                                    svalue.responseTaxAmount =  response.taxAmount;
                                                    svalue.unitDiscountAmount = response.unitDiscountAmount;
                                                    svalue.itemDiscount =  response.discountAmount;
                                                    svalue.itemtotalAmount = response.totalAmount
                                                    svalue.totalAmount = response.totalAmount;
                                                    svalue.netAmount = response.netAmount;
                                                    svalue.selfPayable = response.netAmount;
                                                    svalue.discountPerItem = discount;
                                                    svalue.discountAmount = (svalue.totalAmount/100)*svalue.discountPerItem;
                                                    svalue.itemDiscount  =  (svalue.itemtotalAmount *  discount) / 100;
                                                    svalue.patientClassAmount = response.patientClassAmount;
                                                }
                                            });
                                        }
                                    }
                                    else{
                                        angular.forEach(value.selectedItems, function (svalue) {
                                    	    if(!svalue.disabled && !svalue.itemDisabled){
                                    	        var data = {
                                                    "mrp": svalue.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    // "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    // "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                    "markup": svalue.markUpDown,
                                                    "markUpDownPer" : svalue.markUpDownPer,
                                                    "discount": bill.discount ? bill.discount : 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": svalue.taxPercentage,
                                                    "qty": svalue.quantity,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":discount,
                                                    "patientClassPercentage": svalue.patientClassPercentage
                                                }
                            
                                                var response = commonDetailService.calculateDisAmount(data);
                            
                                                svalue.rate = response.rate;
                                                svalue.taxAmount = response.taxAmount;
                                                svalue.responseTaxAmount =  response.taxAmount;
                                                svalue.unitDiscountAmount = response.unitDiscountAmount;
                                                svalue.itemDiscount =  response.discountAmount;
                                                svalue.itemtotalAmount = response.totalAmount
                                                svalue.totalAmount = response.totalAmount;
                                                svalue.netAmount = response.netAmount;
                                                svalue.selfPayable = response.netAmount;
                                                svalue.discountPerItem = discount;
                                                svalue.discountAmount = (svalue.totalAmount/100)*svalue.discountPerItem;
                                                svalue.itemDiscount  =  (svalue.itemtotalAmount *  discount) / 100;
                                                svalue.patientClassAmount = response.patientClassAmount;
                                            }
                                        });
                                    }
                             
                                    angular.forEach(value.selectedItems, function (subvalue, subkey) {
                                	    if(!subvalue.disabled && !subvalue.itemDisabled){ 
                                	        subvalue.itemDiscount = subvalue.itemtotalAmount * value.discountPerItem / 100;

                                            var unitDiscountAmount = subvalue.itemDiscount / subvalue.quantity;
                                            bill.items[key].selectedItems[subkey]['unitDiscountAmount'] = unitDiscountAmount;
                                            //  self.model.itemsAfterCalculation.itemtotalAmount = self.model.itemsAfterCalculation.itemtotalAmount - unitDiscountAmount;
                                	    }
                                	});
                                }
                            });
                            self.model.isDiscountAppliedValid = false;
                            self.model.isDiscountApplied = true;
                            self.model.isAppliedDiscountValid = false
                        } else {
                            SetDiscountFlags();
                            growl.error('Please enter discount amount less than ' + discountLimit + '%');
                        }
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                            $scope.isValidated = true;
                        }
                    }
                }
            }
        }
        
        var CalculateAmount = function (item) {
            if ($scope.taxInclusive == true) {
                item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
                item.netAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (item.itemDiscount)));
                item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
            }
            else {
                item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
                item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (item.itemDiscount)) * (item.taxPercentage / 100));
                item.netAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (item.itemDiscount) + item.taxAmount));
                item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
            }
            if(!item.isConsumed)
       	   {
       	     item.selfPayable = item.netAmount;
       	   }
        }

        var SetDiscountFlags = function () {
            self.model.isDiscountAppliedValid = true;
            self.model.isDiscountApplied = false;
            self.model.isAppliedDiscountValid = true
        }

        /*var CheckQuantityForDiscount = function (bill) {
            var isValid = true;
            bill.items.some(function (item) {
                if (!item.quantity) {
                    growl.error('Please enter quantity');
                    isValid = false;
                    SetDiscountFlags();
                } else if ((parseFloat(bill.discount) > bill.totalAmount) && (bill.discountType == '2')) {
                    growl.error('Please enter discount amount less than Total Amount');
                    isValid = false;
                    SetDiscountFlags();
                }
            })
            return isValid
        }*/

        var CheckQuantityForDiscount = function (bill) {
            var isValid = true;
            bill.items.some(function (item) {
               /* if (!item.quantity) {
//                    growl.error('Please enter quantity');
//                    isValid = false;
                    SetDiscountFlags();
                }
                else */
                if($scope.taxInclusive == true){
                    if ((parseFloat(bill.discount) > bill.netAmountData) && (bill.discountType == '2')) {
                        growl.error('Please enter discount amount less than Total Amount');
                        isValid = false;
                        SetDiscountFlags();
                    }
                }
                else{
                    if ((parseFloat(bill.discount) > bill.totalAmount) && (bill.discountType == '2')) {
                        growl.error('Please enter discount amount less than Total Amount');
                        isValid = false;
                        SetDiscountFlags();
                    }
                }
            })
            return isValid;
        }
        
        var ApplyDiscountValidation = function (bill) {
        	 var isValid = true;
//            if (CheckQuantityForDiscount(bill)) {
        	 
                self.model.discountTypeFlag = false
                if(self.model.narcoticsIssueWorklistDetails.discountCatgeory.discountCategory!="Select Discount Category"){
                if (!self.model.narcoticsIssueWorklistDetails.discount) {
                    if (bill.discountType == '1') {
                        growl.error('Please enter % discount');
                    }
                    if (bill.discountType == '2') {
                        growl.error('Please enter discount amount');
                    }
                    SetDiscountFlags();
                }
            }
                return isValid;
        }

        
        var RestorePreviousItem = function (bill) {

            bill.items.forEach(function (item) {
                if (item.selectedItems) {
                    if (item.selectedItems.length == 0) {
                        item['disabled'] = false;
                    }
                }
            });
        }
        
        var CalculateOrederOrIssuedQty = function(item){
          	 var issuedQty = 0;
          	 var quantity = item.quantity == undefined ? item.quantity : 0;
          	  if(item.selectedUom.uomTypeId == 3)
                {
          		issuedQty  = parseInt(item.quantity);
                      
                }else if(item.selectedUom.uomTypeId == 2)
                    	{
                           var stripQty = item.uom[1].conversion * parseInt(item.quantity);
                           issuedQty  = stripQty;
                       
                    	}else if(item.selectedUom.uomTypeId == 1)
                    		 {
   		                      var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(item.quantity);
   		                      issuedQty  = boxQty;
                    		 }  
          	  
          	  return issuedQty;
          }
        
        var AddItem = function (index, bill, parentIndex) { 
            
//            bill.items.splice(index + 1, 0, CopyItem(index, bill));
            console.log("data",bill);
//            var item = bill.items[index];
            if (parentIndex == null) {
            	
            	if(bill.items[index].quantity==undefined || bill.items[index].quantity== null ||bill.items[index].quantity=="" || bill.items[index].quantity==0){
            		  growl.error('Please Enter Dispensed Quantity')
                      return false;
              	}
                bill.items.splice(index + 1, 0, CopyItem(index, bill));
                bill.items[index]['disableInputField']=true;
                bill.items[index+1].id = null;
                bill.items[index+1].parentIssueDetailsId =  bill.items[index].id == null ? bill.items[index].parentIssueDetailsId :  bill.items[index].id;
            } else {
            //	var item =bill.items[parentIndex].selectedItems[index];
                bill.items[parentIndex].selectedItems.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
                bill.items[parentIndex]['disableInputField']=true;
            }
//             ValidateUniqueBatchAndUom(item);
          
        	  if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }
            else{
                $scope.isValidated = false;
            }
            if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                $scope.isValidated = true;
            }
            
        }
            
        var CopyItem = function (index, bill, parentIndex) {
        	var item = null;
            var totalIssuedQty = 0;
            var finalOrdQty = 0;
            if (parentIndex == undefined) {
                item = bill.items[index];
	  	      	  angular.forEach( self.model.narcoticsIssueWorklistDetails.items, function (value, index1) {
	  	            	
	  	                	if(bill.items[index].itemId == value.itemId && value.disabled != true ){
	  	                		
	  	                		totalIssuedQty += CalculateOrederOrIssuedQty(value);
	  	                	}
	  	                	
	  	            });  
	  	      	 finalOrdQty = bill.items[index].finalOrderedQty;
            } else {
		                item = bill.items[parentIndex].selectedItems[index];
		                finalOrdQty = bill.items[parentIndex].finalOrderedQty;
		              if(bill.items[parentIndex].selectedBatch!=undefined && bill.items[parentIndex].selectedBatch.batchId!=undefined){
		                totalIssuedQty += CalculateOrederOrIssuedQty(bill.items[parentIndex]);
		              }
	                	if(bill.items[parentIndex].selectedItems != null && bill.items[parentIndex].selectedItems.length>0){
	                    		
	                		angular.forEach(bill.items[parentIndex].selectedItems, function (selectValue, selectIndex) {
	                			
	                			totalIssuedQty += CalculateOrederOrIssuedQty(selectValue);
	               		 	});
	                  }
            }
            
      	 
//	      var newOrderQty = finalOrdQty - totalIssuedQty;
            if(finalOrdQty!=undefined){
                if(isNaN(totalIssuedQty)){
                    totalIssuedQty = 0;
                }
                var newOrderQty = finalOrdQty - totalIssuedQty;
            }else{
                var newOrderQty = undefined;
            }
            
            return {
                id: item.id,
                itemCode: item.itemCode,
                itemName: item.itemName,
                itemId: item.itemId,
                genericName: item.genericName,
                genericId:  item.genericId,
                formulationTypeId: item.formulationTypeId,
                manufacturerName: item.manufacturerName,
                interventionFlag: item.interventionFlag,
                rackNo: item.rackNo,
                quantityOrdered: newOrderQty,
                newQty: newOrderQty ,
                stock: item.stockAvailable,
                quantity: 0,
                duration: item.duration,
                frequencyDesc: item.frequencyDesc,
                storeId: item.storeId,
                batch: item.batch,
                markUpDown: item.markUpDown,
                markUpDownPer: item.markUpDownPer,
                markupStatus: item.markupStatus,
                addedItem: item.addedItem,
                substituteNewQty: item.substituteNewQty,
                selectedBatch: null,
                isNarcotic:item.isNarcotic,
                uom: item.uom,
                selectedUom: null,
                taxId: item.taxId,
                pickedQuantity : item.pickedQuantity!=undefined ? item.pickedQuantity :0,
                tax: item.tax,
                issuedQty: item.issuedQty,
                quantityIssued: item.quantityIssued,
                taxPercentage: item.taxPercentage,
                itemDiscount: item.itemDiscount,
                newStock: item.stockAvailable,
                stockAvailable: item.stockAvailable,
                availableStockQuantity: item.stockAvailable,
                productCategoryId: item.productCategoryId,
                substituteBtn: ((parentIndex == undefined && parentIndex == null )? false : true),
                isShow: true,
                prescriptionDetailId : item.prescriptionDetailId,
//                parentIssueDetailsId: item.id == null ? item.parentIssueDetailsId : item.id,
                finalOrderedQty : item.finalOrderedQty,
                isFromAssignPicker : item.isFromAssignPicker,
                substituteBtn:item.substituteBtn,
                dose : item.dose,
                doseUnit : item.doseUnit,
                parentIssueDetailsId:((item.parentIssueDetailsId == undefined && item.parentIssueDetailsId == null )? null : item.parentIssueDetailsId),
                parentItemId : ((item.parentItemId == undefined && item.parentItemId == null )? null : item.parentItemId),
                instructionDesc : item.instructionDesc,
                itemDisabled : false,
                disabled : false
            }
        }
        
        //confirmation for remove
        var ConfirmRemove = function (index, narcoticsIssueWorklistDetails, parentindex) {
            var popup = angular.element('#removeItemPopup');
            popup.modal('show');
            self.model.itemForRemove = index;
            self.model.narcoticsWorklistDetails = narcoticsIssueWorklistDetails;
            self.model.parentIndexNew = parentindex;
        }
        
        var RemoveItem = function (index, bill)
        
        {
        	
        	  var item = bill.items[index];
              var parentIndex = self.model.parentIndexNew;
        	
//            if (!!item.allocationId) {
              if (parentIndex == undefined &&  parentIndex == null && (item.allocationIds!=undefined || item.allocationIds!=null)) {
              //  var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API  + $scope.unitId + "/"+$scope.organizationId +"/" +$scope.storeId + "/"+ bill.id + "/" + item.allocationIds;
            	  var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API  + $scope.unitId + "/"+$scope.organizationId +"/" +$scope.storeId +"/"+$scope.userId  + "/"+ bill.id + "/" + item.allocationIds;

                $rootScope.startLoader();
                GenericService.serviceAction("DELETE", URI, {}).then(
                    function (response) {
                        if (!!response.data) {
                        /*	 if (parentIndex == null) {
                          		  bill.items.splice(index, 1);
                                } else {
                                    bill.items[parentIndex].selectedItems.splice(index, 1);
                                    if(bill.items[parentIndex].selectedItems.length === 0){
                                        bill.items[parentIndex]['disabled'] = false;
                                        bill.items[parentIndex]['itemDisabled'] = false;
                                    }
                                }*/
                        }
                        $rootScope.stopLoader();
                        // growl.success(response.data.message);
                        ValidateUniqueBatchAndUom(item);
                    },
                    function (ex) {
                        $rootScope.stopLoader();
                        growl.error('Error while deleting allocated quantity');
                    });
                    CalculateItemSum(bill.items);
            } else {
            	
            	 /*if (parentIndex == null) {
           		  bill.items.splice(index, 1);
                 } else {
                     bill.items[parentIndex].selectedItems.splice(index, 1);
                     if(bill.items[parentIndex].selectedItems.length === 0){
                         bill.items[parentIndex]['disabled'] = false;
                         bill.items[parentIndex]['itemDisabled'] = false;
                     }
                 }*/
            }
        	
        	  var item = null;
              if (parentIndex == undefined &&  parentIndex == null) {
            	  
            	  $scope.removeItemFlag = true;
                  item = bill.items[index];
                  if(item.quantityOrdered == undefined){
                  	$scope.removeItemFlag = false;
                 }
                  var itemCount = 0;
                  angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function (value, index) {
                    	
                    	if(value.itemId == item.itemId){
                    		itemCount +=1;
                    	}
                });  
                  if(itemCount>2){
                  	if((itemCount - 1) > index){
                  		
                  		for(var i = index;i<itemCount - 1;i++){
                  			
                  			var removeItemPickedQty = CalculateOrederOrIssuedQty(item) ;
                      		bill.items[i+1].quantityOrdered  += removeItemPickedQty ;
                      		ConvertQtyOrdered(bill.items[i+1], bill.items[i+1].uom, bill.items[i+1].selectedUom,1);
                  		}
                  		
                  	}
                  }
                  
              } else {
                  item = bill.items[parentIndex].selectedItems[index];
                  if(bill.items[parentIndex].selectedItems.length>1   ){
                  	
                  	if((bill.items[parentIndex].selectedItems.length - 1) > index){
                  		
                  		for(var i = index;i<bill.items[parentIndex].selectedItems.length - 1;i++){
                  			var removeItemPickedQty = CalculateOrederOrIssuedQty(bill.items[parentIndex].selectedItems[index]) ;
                  			bill.items[parentIndex].selectedItems[i+1].quantityOrdered += removeItemPickedQty ;
                      		ConvertQtyOrdered(bill.items[parentIndex].selectedItems[i+1], bill.items[parentIndex].selectedItems[i+1].uom, bill.items[parentIndex].selectedItems[i+1].selectedUom,1);
                  		}
                  	}
                  }
                  
              }

              if (parentIndex == undefined && parentIndex == null ) {
                 /* bill.items.splice(index, 1);
                  if(bill.items.length >1){
                      bill.items[index-1]['disabled'] = false;
                      bill.items[index-1]['itemDisabled'] = false;
                      }else{
                      	 bill.items[0]['disabled'] = false;
                           bill.items[0]['itemDisabled'] = false;
                      }
                  if(bill.items.length==1){
                      bill.items[index-1].isShow = false;
                  }else{
                          bill.items[index-1].isShow = true;
                  }*/
            	  bill.items.splice(index, 1);
            	  bill.items[index]['disabled'] = false;
                  bill.items[index]['itemDisabled'] = false;
              } else {
              	 bill.items[parentIndex].selectedItems.splice(index, 1);
              		if(bill.items[parentIndex].selectedItems.length==0){
                          
	              			 if(bill.items[parentIndex].disabled!=undefined){
	                   bill.items[parentIndex]['disabled'] = false;
	                   bill.items[parentIndex]['itemDisabled'] = false;
	                  }
              		}
              		else{
        		        var isParentSubDisabled=false;
                        angular.forEach(bill.items[parentIndex].selectedItems, function (value, index) {

                                   if(value.itemDisabled!=undefined || !value.itemDisabled){
                                   	isParentSubDisabled=true;
                                   }
                             });
                        if(isParentSubDisabled && bill.items[parentIndex].selectedItems.length < 1){
                                     bill.items[parentIndex]['disabled'] = false;
                                     bill.items[parentIndex]['itemDisabled'] = false;
                            }
                  	}
              		
              		if(bill.items.length==1){
                		bill.items[parentIndex].isShow = false;
                	}else{
                			bill.items[parentIndex].isShow = true;
                	}
              }
        	
        	
        }
        
/*
        var RemoveItem = function   (index, bill, parentIndex) {
           
          
                var item = null;
                if (parentIndex == null) {
                    item = bill.items[index];
                } else {
                    item = bill.items[parentIndex].selectedItems[index];
                }
      
            
            
        }*/

        var ValidateMaxOrderQuantity = function (item,selecteditem, uom, uomList) {
            var isValid = true;
           if(!!item){
            item.isParentQuantityValid = false;
            var totalLeaseQuantity = 0;

            // CalculateQuantity(item, uom, uomList);

            var items = self.model.narcoticsIssueWorklistDetails.items.filter(function (detail) {
                return detail.itemId == item.itemId;
            });

            items.forEach(function (detail) {
                totalLeaseQuantity = totalLeaseQuantity + detail.leaseQuantity + detail.quantityIssued;
            });

            if (!item.quantityDispensed) {
                if (totalLeaseQuantity > item.quantityOrdered) {
                    item.isParentQuantityValid = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the ordered quantity');
                    item.quantity = 0;
                }
            } else {
                if (totalLeaseQuantity > (item.quantityOrdered - item.quantityDispensed)) {
                    item.isParentQuantityValid = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the ordered quantity');
                    item.quantity = 0;
                }
            }
        }
           
           if(!!selecteditem){
        	   selecteditem.isParentQuantityValid = false;
               var totalLeaseQuantity = 0;

               // CalculateQuantity(item, uom, uomList);

        	     var items = self.model.narcoticsIssueWorklistDetails.items.filter(function (detail) {
                 	if(!detail.itemDisabled){
                       return detail.itemId == selecteditem.itemId;
                 	}
               });

        	     items.forEach(function (detail) {
                   totalLeaseQuantity = totalLeaseQuantity + detail.leaseQuantity + detail.quantityIssued;
               });

               if (!selecteditem.quantityDispensed) {
                   if (totalLeaseQuantity > selecteditem.quantityOrdered) {
                	   selecteditem.isParentQuantityValid = true;
                       isValid = false;
                       growl.error('Entered quantity is exceeding the ordered quantity');
                       selecteditem.quantity = 0;
                   }
               } else {
                   if (totalLeaseQuantity > (selecteditem.quantityOrdered - selecteditem.quantityDispensed)) {
                	   selecteditem.isParentQuantityValid = true;
                       isValid = false;
                       growl.error('Entered quantity is exceeding the ordered quantity');
                       selecteditem.quantity = 0;
                   }
               }
           }

            return isValid;
        }

      /*  var ValidateUniqueBatchAndUom = function (item) {
            var isValid = true;

            // item['isBatchValid'] = false;
            // item['isUOMValid'] = false;
            if (item.substituteBtn == true) {
            	  var parentItem = self.model.narcoticsIssueWorklistDetails.items.filter(function (parentItem) {
                      return parentItem.genericId == item.genericId;
                  })
                  
                  $scope.itemsSelectedData1=[];
	            var itemsSelectedData= parentItem.filter(function (detail1) {
	             	if(detail1.selectedItems != undefined){
	             		 var itemsSelectedData1 = null;
	             	$scope.itemsSelectedData1 = detail1.selectedItems.filter(function (detail) {
	                      return  detail.itemId == item.itemId
	             	 });
	            	}
	            });
	            
           	 var items = $scope.itemsSelectedData1.filter(function (detail) {
                    return detail.itemId == item.itemId && 
                    item.selectedBatch && 
                    item.selectedUom && item.selectedUom.uomTypeId > 0
                    && item.selectedBatch.batchId > 0 &&
                    detail.selectedBatch.batchId == item.selectedBatch.batchId
                    && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
           	 });
                 
            
	            if (items.length > 1) {
	                isValid = false;
	                angular.forEach(items, function (value) {
	                    value.isBatchValid = true;
	                    value.isUOMValid = true;
	                });
	                // item['isBatchValid'] = true;
	                // item['isUOMValid'] = true;
	                growl.error('There should not be same batch and same UOM for same item');
	            } else {
	            	
	            	$scope.similarItems=[];
	                var similarItemsData = self.model.narcoticsIssueWorklistDetails.items.filter(function (detail1) {
		             	if(detail1.selectedItems != undefined){
		             		 var similarItemsData1 = null;
		             	$scope.similarItemsData1 = detail1.selectedItems.filter(function (detail) {
		                      return  detail.itemId == item.itemId
		             	 });
		            	}
		            
	                });
	                
	                var similarItems = $scope.similarItemsData1.filter(function (detail) {
                        return detail.itemId == item.itemId
                    });
	                angular.forEach(similarItems, function (value) {
	                    value.isBatchValid = false;
	                    value.isUOMValid = false;
	                });
	            }
            }else{

                var items = self.model.narcoticsIssueWorklistDetails.items.filter(function (detail) {
                	if(!detail.itemDisabled){
                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom &&
                    item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId 
                    == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                	}
                });

                if (items.length > 1) {
                    isValid = false;
                    angular.forEach(items, function (value) {
                        value.isBatchValid = true;
                        value.isUOMValid = true;
                    });
                    // item['isBatchValid'] = true;
                    // item['isUOMValid'] = true;
                    growl.error('There should not be same batch and same UOM for same item');
                } else {
                    var similarItems = self.model.narcoticsIssueWorklistDetails.items.filter(function (detail) {
                        return detail.itemId == item.itemId
                    });
                    angular.forEach(similarItems, function (value) {
                        value.isBatchValid = false;
                        value.isUOMValid = false;
                    });
                }
                
            }
            return isValid;
        }
*/
        
        var ValidateUniqueBatchAndUom = function (item) {
            var isValid = true;
            if (item.substituteBtn == true) {
                item['isBatchValid'] = false;
                item['isUOMValid'] = false;

                var parentItem = self.model.narcoticsIssueWorklistDetails.items.filter(function (parentItem) {
                    return parentItem.genericId == item.genericId;
                })

                var items = parentItem.filter(function (detail1) {
                	if(detail1.selectedItems != undefined){
                		self.model.test = [];
                    	self.model.test = angular.copy(detail1.selectedItems);
                		/*
                	detail1.selectedItems.filter(function (detail) {
                	
                    return detail.itemId == 
                    	item.itemId && item.selectedBatch && 
                    	item.selectedUom && 
                    	item.selectedUom.uomTypeId > 0 &&
                    	item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == 
                    		item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                	 });
                	*/}
                });
                var items = self.model.test.filter(function (detail) {
                	  return detail.itemId == 
                      	item.itemId && item.selectedBatch && 
                      	item.selectedUom && 
                      	item.selectedUom.uomTypeId > 0 &&
                      	item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == 
                      		item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                  });
                if (items.length > 1) {
                    isValid = false;
                    item['isBatchValid'] = true;
                    item['isUOMValid'] = true;
                    growl.error('There should not be same batch and same UOM for same item');
                }
            }
            else {
                item['isParentBatchValid'] = false;
                item['isParentUOMValid'] = false;

                console.log("self.model.narcoticsIssueWorklistDetails.items",self.model.narcoticsIssueWorklistDetails.items);
                var items = self.model.narcoticsIssueWorklistDetails.items.filter(function (detail) {
                	
                	 if (!detail.disabled && !detail.itemDisabled && detail.selectedBatch!=null) {
                		
                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
                    && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
                    == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;}
                });
                if (items.length > 1) {
                    isValid = false;
                    item['isParentBatchValid'] = true;
                    item['isParentUOMValid'] = true;
                    growl.error('There should not be same batch and same UOM for same item');
                }
            }
            return isValid;
        }
        
        
        
        var CalculateQuantity = function (item, uom, uomList) {
            if (!!uom) {
                var pUom = $filter('filter')(item.uom, {
                    uomTypeId: 1
                }, true)[0];

                var sUom = $filter('filter')(item.uom, {
                    uomTypeId: 2
                }, true)[0];

                var dUom = $filter('filter')(item.uom, {
                    uomTypeId: 3
                }, true)[0];

                if (uom.uomTypeId == 1) {
                    item.leaseQuantity = pUom.conversion * sUom.conversion * dUom.conversion * item.quantity;
                } else if (uom.uomTypeId == 2) {
                    item.leaseQuantity = sUom.conversion * dUom.conversion * item.quantity;
                } else if (uom.uomTypeId == 3) {
                    item.leaseQuantity = dUom.conversion * item.quantity;
                }
            } else {
                item.leaseQuantity = 0;
            }
        }

        var AllocateQuantity = function (index,bill,parentIndex) {
        	
        	 var item = null;
            if (!!bill) {
            	if(parentIndex == undefined){
                           item = bill.items[index];
                } else {
                    item = bill.items[parentIndex].selectedItems[index];
                }

                CalculateQuantity(item, item.selectedUom, item.uom);

//                if (CanAllocate(item) && (parseInt(item.quantity) <= item.stock) && (item.newQty >= parseInt(item.quantity))) {
                if (CanAllocate(item) && (parseInt(item.quantity) <= (item.stock!=undefined ? item.stock: (item.availableStockQuantity!=undefined) ? item.availableStockQuantity: item.stock)) && (item.newQty==undefined ? true:item.newQty >= parseInt(item.quantity))) {

                    var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+$scope.storeId + "/" +$scope.userId+"/"+item.itemId+"/" +bill.id + "/" + (!!item.allocationIds ? item.allocationIds : "");
                    var data = {
                        "batchId": item.selectedBatch.batchId,
                        "quantity": item.leaseQuantity
                    };

                    $rootScope.startLoader();
                    GenericService.serviceAction("PUT", URI, data).then(
                        function (response) {
                            if (!!response.data) {
                                item.allocationId = response.data.id;
                                item.allocationIds = response.data.ids;
                                
                                item.selectedBatch.availableStockQuantity = response.data.object;
                                item.stock = response.data.object;
                                item.newStock = response.data.object;
                                angular.forEach(item.batch, function (value4, index4) {
                                  	
                                  	if(value4.batchId == item.selectedBatch.batchId){
                                  		value4.availableStockQuantity = response.data.object;
                                  	}
                                });
                                
                                ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                                
                                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                    $scope.isValidated = true;
                                }
                                else{
                                    $scope.isValidated = false;
                                }
                                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                                    $scope.isValidated = true;
                                }
                                StorePreviousValue(item);
                            }
                            $rootScope.stopLoader();
                        },
                        function (ex) {

                        	growl.warning('Please enter quantity less than or equal to ordered quantity.');
                            // RestorePreviousValue(item);
                            item.itemtotalAmount = 0.0 ;
                            item.totalAmount = 0.0;
                            item.discountAmount = 0.0;
                            item.netAmount = 0.0;
                            item.quantity = 0;
                            $rootScope.stopLoader();
                        });
                } else {
                	   growl.warning('Please enter quantity less than or equal to ordered quantity.');
                	   item.itemtotalAmount = 0.0 ;
                       item.totalAmount = 0.0;
                       item.discountAmount = 0.0;
                       item.netAmount = 0.0;
                       item.quantity = 0;
                       $rootScope.stopLoader();
                       StorePreviousValue(item);
                }
            }
        }

        var CanAllocate = function (item) {
            if (!!item.selectedBatch && !!item.selectedUom &&
                (!!item.quantity) && !(item.prevQuantity == item.quantity && item.quantity == '')) {
                return true;
            }

            return false;
        }

        var StorePreviousValue = function (item) {
            item.prevBatch = item.selectedBatch;
            item.prevUom = item.selectedUom;
            item.prevQuantity = item.quantity;

           // CalculateRate(item, item.selectedUom, item.uom);
        }

        var RestorePreviousValue = function (item) {
            item.selectedBatch = item.prevBatch;

            if (!!item.selectedBatch) {
                item.selectedUom = item.prevUom;
            } else {
                item.uom = [{
                    uomType: "Select UOM"
                }];
                item.selectedUom = item.uom[0];
                item.rate = 0;
            }

            item.quantity = item.prevQuantity;

           // CalculateRate(item, item.selectedUom, item.uom);
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        var ChangeItemStatus = function (item) {
        	
            var qty;
            if (!!item.substituteNewQty) {
                qty = item.substituteNewQty;
            } else {
                qty = item.newQty;
            }
            if (qty > parseInt(item.quantity)) {
                item.statusId = SetStatus('Partially Completed');
            } else if (qty == parseInt(item.quantity)) {
                item.statusId = SetStatus('Completed');
            }
        }
        $scope.saveBtnDisabled=false;
        
        var SaveBill = function (bill) {
        	
        	ActualSaveBill(bill);
        	/*   
>>>>>>> Stashed changes
        		self.model.stopItemIds = [];
                $scope.saveBtnDisabled=true;
           	  angular.forEach(bill.items, function (item, key)  {
                    if (!item.disabled && !item.itemDisabled) 
                       {
                         self.model.stopItemIds.push(item.itemId);
                       }
                 });        	 
       		 var stopData = {
      	                "itemIds": self.model.stopItemIds,
      	                "storeId": $scope.storeId   	      		 
      	              };      	    
          	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                   var StopTransactionData = responseData;  
                   if(StopTransactionData.length > 0 ){    
                            var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                            var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");                                         
                            $rootScope.stopLoader();                     
                            var stopMessage = "Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ";                             
                      	    growl.warning(stopMessage, {onclose: function() {
                      		  var popup = angular.element('#confirmStop');
                                popup.modal('show');    
                          },}); 
                    }
                   else{
                	   ActualSaveBill(bill);
                   }
          	  });        	
        */}

        var ActualSaveBill = function (bill) {
            $scope.saveBtnDisabled=true;
            if (bill.visitType == 1) {
                bill.saleTypeId = 2;
            } else {
                bill.saleTypeId = 1;
            }

            $scope.totalSelfPayable = 0.00;
            $scope.totalCreditPayable = 0.00;
            var statusFinalFlag =  false;
            if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
            {
                var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
                if(found==undefined)
                {
                    growl.error('Please Apply Payment Entitlement.');
                    return false;
                }
            }

            if (ValidateBill(bill)) {
                var URI = CONSTANTS.PHARMACY.NARCOTICS.NARCOTICS_ISSUE_WORKLIST.SAVE_API;
                var data = {
                    // "screenId": 1,
                    "id": !!bill.id ? bill.id : undefined,
                    "prescriptionId": !!bill.prescriptionId ? bill.prescriptionId : undefined,
                    "storeId": !!bill.storeId ? bill.storeId : undefined,
                    "visitTypeId": !!bill.visitType ? bill.visitType : undefined,
                    "admissionId": !!bill.admissionId ? bill.admissionId : undefined,
                    "encounterId": !!bill.encounterId ? bill.encounterId : undefined,
                    // "remark":  !!bill.remark ? bill.remark : undefined,
                    "orderId": !!bill.orderId ? bill.orderId : undefined,
                    "patientId": !!bill.patientId ? bill.patientId : undefined,
                    "doctorId": !!bill.doctorId ? bill.doctorId : undefined,
                    "discountTypeId": !!bill.discountType ? bill.discountType : undefined,
                    "discountCategoryId": !!bill.discountCatgeory.id ? bill.discountCatgeory.id : undefined,
                    "discount": !!bill.discount ? bill.discount : 0,
                    "totalAmount": !!bill.totalAmount ? bill.totalAmount : undefined,
                    "totalDiscountAmount": !!bill.totalDiscountAmount ? bill.totalDiscountAmount : undefined,
                    "netAmount": !!bill.netAmount ? parseFloat(bill.netAmount).toFixed(2) : 0,
                    "roundOff": !!bill.roundOff ? bill.roundOff : 0,
                    "taxAmount": !!bill.totalTaxAmount ? bill.totalTaxAmount : 0,
                    "saleTypeId": !!bill.saleTypeId ? bill.saleTypeId : undefined,
                    "orgId": self.model.orgId,
                    "unitId": self.model.unitId,
                    "createdBy": self.model.userId,
                    "totalSelfPayable":$scope.totalSelfPayable,
                	"totalCreditPayable":$scope.totalCreditPayable,
                    // "statusId": SetStatus('Pending'),
                	"consume" :false,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "remark": self.model.remark,
                    "storeId":$scope.storeId
                }
                
                $scope.totalOrderedQty=0;
                var validateQty = function(item){
               	  var statusCount = 0;
                     
             		if(item.itemId!=null){
             					var totalSame = 0;
             					
             						
             						if(item.isFromAssignPicker){
                                       if (item.pickedQuantity >  parseInt(item.quantity)) {
             							statusCount = 1
                                     } 
             						}else{
             							
             							angular.forEach(bill.items, function (item1, key1) 
                                                {
          	              							if(item.id == item1.id)
          	                                		{
          	              								if(item1.quantity != undefined && item1.quantity != null){

           	              									totalSame += CalculateOrederOrIssuedQty(item1);
           	              								}
          	              								
          	              								angular.forEach(item1.selectedItems, function (item2, key2) 
          		                                                {
          	              											if(!item2.itemDisabled ){
          					              								totalSame += CalculateOrederOrIssuedQty(item2);
          	              											}
          		                                                });
          	                                		}
          	              							
          		                                	if((item.id == item1.parentIssueDetailsId)  && (item1.parentIssueDetailsId != undefined))
          		                                		{
          		                                			totalSame +=  CalculateOrederOrIssuedQty(item1);
          		                                			
          		                                			angular.forEach(item1.selectedItems, function (item2, key2) 
          			                                                {
          						              								totalSame += CalculateOrederOrIssuedQty(item2);
          			                                                });
          		                                		}
                                                }); 
                       						
             							
             						   if (item.quantityOrdered > totalSame) {
             							statusCount = 1
             						   } 
             						   
             						  if(item.checkNewItem==undefined){
//             							  	$scope.totalOrderedQty+=parseInt(item.quantity);
             							 var issuedQty =0;
             							 if(item.isThreeUOMApplicable==true)
             								 {
                							if(item.selectedUom !=undefined && item.selectedUom.uomTypeId !=undefined)
 										 {
 										  if(item.selectedUom.uomTypeId == 3)
 											 {
 											issuedQty  = parseInt(item.quantity);

 											 }else if(item.selectedUom.uomTypeId == 2)
 													{
 														var stripQty = item.uom[1].conversion * parseInt(item.quantity);
 														issuedQty  = stripQty;

 													}else if(item.selectedUom.uomTypeId == 1)
 														 {
 															  var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(item.quantity);
 															  issuedQty  = boxQty;
 														 }  
 										}
             						  }
             							 else
             								 {
             								if(item.selectedUom !=undefined && item.selectedUom.uomTypeId !=undefined)
    										 {
    										  if(item.selectedUom.uomTypeId == 3)
    											 {
    											issuedQty  = parseInt(item.quantity);

    											 }else if(item.selectedUom.uomTypeId == 1)
    														 {
    															  var boxQty = item.uom[0].conversion *  item.uom[1].conversion * parseInt(item.quantity);
    															  issuedQty  = boxQty;
    														 }  
    										}
             								 }
                							$scope.totalOrderedQty+=parseInt(issuedQty);
                 						   }
                    						
                                       

             						}
             				}
             		if (statusCount>0) {
							return true;
                     } else if (statusCount == 0) {
                   		return false;
                     }
             	
               }
                angular.forEach(bill.items, function (item, key) {
//                	if(item.quantity){
                        if (!item.disabled && !item.itemDisabled  && item.selectedBatch!=null) {
                        	if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                            var result = validateQty(item);
                            var status;
                            if (result) {
                                status = 'Partially Completed';
                                statusFinalFlag =  true;
                            } else if (!result) {
                                status = 'Completed';
                            }
                            var billItem = {
                                "itemId": item.itemId,
                                "statusId": SetStatus(status),
                                "id": item.id,
                                "prescriptionDetailId": item.prescriptionDetailId,
                                "orderDetailId": item.orderDetailId,
                                "batchId": item.selectedBatch.batchId,
                                "uomTypeId": item.selectedUom.uomTypeId,
                                "uomUnitId": item.selectedUom.uomUnitId,
                                "rate": item.rate,
                                "quantity": Number(item.quantity),
                                "leaseRate": item.selectedBatch.sellingPrice,
                                "leaseQuantity": item.leaseQuantity,
                                "totalAmount": item.itemtotalAmount,
                                "discountAmount": item.itemDiscount,
                                "unitDiscountAmount": item.unitDiscountAmount,
                                "netAmount": item.netAmount,
                                "taxId": item.taxId,
                                "taxPercentage": item.taxPercentage,
                                "taxAmount": item.taxAmount,
                                "creditPayable": item.creditPayable ? parseFloat(item.creditPayable).toFixed(2) : 0,
                                "selfPayable": item.selfPayable ? parseFloat(item.selfPayable).toFixed(2) : 0,
                                "productCategoryId":item.productCategoryId,
                                "payeeId":item.payeeId,
                                "packageConsumptionAmt" : item.packageConsumptionAmt,
                                "saleDetailPayeeMapperDtoList":item.saleDetailPayeeMapperDtoList,
                                "saleDetailPackageMapperDtoList": item.saleDetailPackageMapperDtoList,
                                "consessionPercentage": item.patientClassPercentage,
                                "consessionAmount": item.patientClassAmount,
                                "unitConsessionAmount": item.unitPatientClassAmount ? item.unitPatientClassAmount.toFixed(2) : 0,
                                // "parentIssueDetailsId" : item.parentIssueDetailsId != undefined ? item.parentIssueDetailsId : null,
                                "isFromAssignPicker" : item.isFromAssignPicker,
                                "consumedItemId" : item.consumedItemId,
                                "consumedPcategroyId" : item.consumedPcategroyId,
                                "batchAllocationId":item.allocationId
                            }
                            var selfpayable = item.selfPayable ? parseFloat(item.selfPayable) : 0.00;
                            var creditpayable = item.creditPayable ? parseFloat(item.creditPayable) : 0.00;
                            $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable) + selfpayable;
                            $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable) + creditpayable;
                            data.items.push(billItem);
                        }
                        }
                        if (item.selectedItems && !item.itemDisabled) {
                            
                            angular.forEach(item.selectedItems, function (item, key) {
                                if(!item.itemDisabled  && item.selectedBatch!=null){
                               if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                                var result = validateQty(item);
                                var status;
                                if (result) {
                                    status = 'Partially Completed';
                                    statusFinalFlag =  true;
                                } else if (!result) {
                                    status = 'Completed';
                                }
                                
                                var billSubItem = {
                                		 "batchAllocationId":item.allocationId,
                                        "itemId": item.itemId,
                                        "statusId": 4,
                                        "id": item.id,
                                        "prescriptionDetailId": item.isFromAssignPicker == true ?item.prescriptionDetailId:null,
                                        "orderDetailId": item.orderDetailId,
                                        "batchId": item.selectedBatch.batchId,
                                        "uomTypeId": item.selectedUom.uomTypeId,
                                        "uomUnitId": item.selectedUom.uomUnitId,
                                        "rate": item.rate,
                                        "quantity": Number(item.quantity),
                                        "leaseRate": item.selectedBatch.sellingPrice,
                                        "leaseQuantity": item.leaseQuantity,
                                        "totalAmount": item.itemtotalAmount,
                                        "discountAmount": item.itemDiscount,
                                        "unitDiscountAmount": item.unitDiscountAmount ? parseFloat(item.unitDiscountAmount).toFixed(2) : 0.00,
                                        "netAmount": item.netAmount ? parseFloat(item.netAmount).toFixed(2) : 0,
                                        "taxId": item.taxId,
                                        "taxPercentage": item.taxPercentage ? parseFloat(item.taxPercentage).toFixed(2) : 0,
                                        "taxAmount": item.taxAmount ? parseFloat(item.taxAmount).toFixed(2) : 0,
                                        "creditPayable": item.creditPayable ? parseFloat(item.creditPayable).toFixed(2) : 0,
                                        "selfPayable": item.selfPayable ? parseFloat(item.selfPayable).toFixed(2) : 0,
                                        "productCategoryId":item.productCategoryId,
                                        "payeeId":item.payeeId,
                                        "packageConsumptionAmt" : item.packageConsumptionAmt,
                                        "saleDetailPayeeMapperDtoList":item.saleDetailPayeeMapperDtoList,
                                        "saleDetailPackageMapperDtoList": item.saleDetailPackageMapperDtoList,
                                        "consessionPercentage": item.patientClassPercentage,
                                        "consessionAmount": item.patientClassAmount,
                                        "unitConsessionAmount": item.unitPatientClassAmount ? item.unitPatientClassAmount.toFixed(2) : 0,
                                        "parentIssueDetailsId" : item.parentIssueDetailsId != undefined ? item.parentIssueDetailsId : null,
                                        "isFromAssignPicker" : item.isFromAssignPicker,		
                                        "consumedItemId" : item.consumedItemId,
                                        "consumedPcategroyId" : item.consumedPcategroyId,
                                    "parentItemId": item.parentItemId
                                }                           
                                var selfpayable = item.selfPayable ? parseFloat(item.selfPayable) : 0.00;
                                var creditpayable = item.creditPayable ? parseFloat(item.creditPayable) : 0.00;
                                $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable) + selfpayable;
                                $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable) + creditpayable;    
                                data.items.push(billSubItem);
    //                        	}
                                }
                            }
                            });
                        }
//                    }
                });
             
                var billStatus='Completed';
                /*if (statusFinalFlag == true)
                {
                    billStatus ='Partially Completed';
                }*/
                
                if($scope.orderQtyTotalSum <= $scope.totalOrderedQty){
                	statusFinalFlag = false;
                }else{
                	statusFinalFlag = true;
                }
                
                if($scope.removeItemFlag){
              	   billStatus ='Partially Completed';
                 }else{
                
                              if (statusFinalFlag == true)
                              {
                                  billStatus ='Partially Completed';
                              }
                              
                 }
                data.statusId=SetStatus(billStatus);
                data.totalSelfPayable = parseFloat($scope.totalSelfPayable).toFixed(2);
                data.totalCreditPayable =  parseFloat($scope.totalCreditPayable).toFixed(2);
                $rootScope.startLoader();
                console.log("savedData",data);
                
                
                var URI1 = BASE_URL + ROOT_URL + "/api/pharmacy/ip/pending/issues/pendingIssueStatus/1";
                
                GenericService.serviceAction(METHOD_POST,URI1, data).then(function(response) {
    			if (response.data.object != null && response.data.object == 4)
    				{
	                    ClearFields(bill);
	                    $scope.orderQtyTotalSum=0;
	                    growl.error(response.data.message);
	                    NavigateToNarcoticsIssueWorklist();
	                    $rootScope.stopLoader();
    				}
    				
    			else {
    				
    			
                GenericService.serviceAction("POST",URI, data).then(
                    function (response) {
                        ClearFields(bill);
                        $scope.orderQtyTotalSum=0;
                        growl.success('Narcotics issues bill Successfully saved');
                        NavigateToNarcoticsIssueWorklist();
                        $rootScope.stopLoader();
                        PrintNarcoticsIssueWorklistReport();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        if(err.data.summary != undefined && err.data.summary != null && err.data.summary == "Stock is not available"){
                            angular.forEach(bill.items, function(b){
                                b.flag = false;
                                if(b.selectedItems){
                                    angular.forEach(b.selectedItems, function(i){
                                        i.flag = false;
                                    });
                                }
                            });
                            angular.forEach(err.data.errors, function(e){
                                angular.forEach(bill.items, function(d){
                                    if(d.selectedItems){
                                        angular.forEach(d.selectedItems, function(s){
                                            if(s.flag == false){
                                                s.summary = '';    
                                                if(s.selectedBatch.batchNo == e.batchNo){
                                                    s.flag = true;
                                                    s.summary = "Enough stock not available";
                                                    s.newStock = e.currentStock;
                                                }
                                            }
                                        });
                                    }

                                    
                                    if(d.flag == false){
                                        d.summary = '';
                                        if(d.selectedBatch.batchNo == e.batchNo){
                                            d.summary = "Enough stock not available";
                                            d.newStock = e.currentStock;
                                            d.flag = true;
                                            var popup = angular.element('#confirmSave');
                                            popup.modal('hide');
                                        }	
                                    }
                                });
                            });
                            var popup = angular.element('#confirmSave');
                            popup.modal('hide');
                        }
                        else{
                            if(err.data.summary != undefined && err.data.summary != null){
                                growl.error('Stock not available for the some of the items.');
                            }
                            if(err.data!= undefined && err.data!= null){
                            	
                            	$rootScope.stopLoader();
                            	ClearFields(bill);
         	                    $scope.orderQtyTotalSum=0;
         	                    growl.error(err.data);
         	                    NavigateToNarcoticsIssueWorklist();
         	                    
	                        }else{
	                            growl.error('Error while saving pending issues bill.');
	                        }
                        }
                    });
                
    				}
                
                });
            }
        }

        var ClearFields = function (bill) {

            bill.Id = "1" + (new Date()).getTime();
            bill.uhid = null;
            bill.patientDetail = null;
            bill.wardName = null;
            bill.pdd = null;
            bill.doa = null;
            bill.bedNo = null;
            bill.prescriptionDate = null;
            bill.doctorDetail = null;
            bill.discountTypeId = null;
            bill.discountCatgeory = self.model.discountCategoryList[0];
            bill.discount = null;
            bill.totalAmount = null;
            bill.totalDiscountAmount = null;
            bill.taxAmount = null;
            bill.netAmount = null;
            bill.remark = self.model.remark;
            bill.items = [];
        }

        var ValidateBatch = function (parentItem,item) {
            var isValid = true;
            
            if(!!parentItem){
            	if(parentItem.selectedBatch!= null){
		            if (!parentItem.selectedBatch.batchId) {
		            	parentItem['isBatchValid'] = true;
		                growl.error("Please Select Batch");
		                isValid = false;
		            } else {
		                ValidateUniqueBatchAndUom(parentItem)
		            }
            	}
            }
            if(!!item){
            	if(item.selectedBatch!=null){
                if (!item.selectedBatch.batchId) {
                    item['isBatchValid'] = true;
                    growl.error("Please Select Batch");
                    isValid = false;
                } else {
                    ValidateUniqueBatchAndUom(item)
                }
            	}
                }
            return isValid;

        }

        var ValidateUOM = function (item) {
            var isValid = true;
            if(item.selectedUom.uomUnitId!=undefined){
            if (!item.selectedUom.uomUnitId) {
                item['isUOMValid'] = true;
                growl.error("Please Select UOM");
                isValid = false;
            }
            }
            return isValid;
        }

        var CalculateOrederOrIssuedQty = function(item){
       	    var issuedQty = 0;
       	    if(item.selectedUom !=undefined && item.selectedUom.uomTypeId !=undefined)
            {
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData[2] == undefined || item.uomListData[2] == null){
                        if(item.selectedUom.uomTypeId == 3)
                        {
                            issuedQty  = parseInt(item.quantity!=undefined ? item.quantity :0);
                        }
                        else if(item.selectedUom.uomTypeId == 2)
                        {
                            var stripQty = item.uoms[1].conversion * parseInt(item.quantity);
                            issuedQty  = stripQty;    
                        }
                        else if(item.selectedUom.uomTypeId == 1)
                        {
                            var boxQty = item.uoms[1].conversion *  item.uoms[2].conversion * parseInt(item.quantity);
                            issuedQty  = boxQty;
                        }
                    }
                    else{
                        if(item.selectedUom.uomTypeId == 3)
                        {
                            issuedQty  = parseInt(item.quantity!=undefined ? item.quantity :0);
                        }
                        else if(item.selectedUom.uomTypeId == 2)
                        {
                            var stripQty = item.uomListData[1].conversion * parseInt(item.quantity);
                            issuedQty  = stripQty;    
                        }
                        else if(item.selectedUom.uomTypeId == 1)
                        {
                            var boxQty = item.uomListData[1].conversion *  item.uomListData[2].conversion * parseInt(item.quantity);
                            issuedQty  = boxQty;
                        }
                    }
                }   
                else{
                    if(item.selectedUom.uomTypeId == 3)
                    {
                        issuedQty  = parseInt(item.quantity!=undefined ? item.quantity :0);
                    }
                    else if(item.selectedUom.uomTypeId == 2)
                    {
                        var stripQty = item.uom[1].conversion * parseInt(item.quantity);
                        issuedQty  = stripQty;    
                    }
                    else if(item.selectedUom.uomTypeId == 1)
                    {
                        var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(item.quantity);
                        issuedQty  = boxQty;
                    }
                }  
            }
       	    return issuedQty;
        }

        $scope.batchExpiryMsgShow = function(item){
            var date = moment(new Date()).format("MM");
            var year = moment(new Date()).format("YYYY");
            var dateCal = parseInt(date) + 1;
            var expDate = moment(item.selectedBatch.expiry).format("MM");
            var expYear = moment(item.selectedBatch.expiry).format("YYYY");
            if((dateCal == expDate || date == expDate) && year == expYear){
                growl.warning("Batch No. " + item.selectedBatch.batchNo + " is near expiry");
            }  
        }

        $scope.substituteButtonFlag = false;  
        var ValidateQuantity = function (parentItem, parentIndex) {
            var isValid = true;
            var count = 0;
            $scope.substituteButtonFlag = false;
            if(!!parentItem){
                angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function(d){
                    if(d.quantity){
                        count++;
                    }
                    d["substituteButtonFlag1"]=false;
                    if(d.newQty==parseInt(d.quantity)){
                    	d["substituteButtonFlag1"]=true;
//                    	$scope.substituteButtonFlag = true;
                    }
                });
                // if(count == 0){
                //     if (!parentItem.quantity) {
                //         parentItem['isQuantityValid'] = true;
                //         growl.error("Please enter quantity");
                //         isValid = false;
                //         return false;
                //     }
                    
                //     if (parentItem.quantity == 0) {
                //         parentItem['isQuantityValid'] = true;
                //         growl.error("Please enter quantity greater than zero");
                //         isValid = false;
                //         return false;
                //     }
                // }
               
                if (parentItem.newQty < parentItem.quantity) {
                    parentItem['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the max orderd quantity');
                    return false;
                }
            
            
            //new code start
            $scope.quantityIssuedNew = 0;            
            if(parentItem.isFromAssignPicker){
                if(parentItem.isThreeUOMApplicable == false){
                    if(parentItem.selectedUom.uomTypeId == 3)
                    {
                        $scope.quantityIssuedNew  = parentItem.quantity;
                        if (parseInt(parentItem.quantity) > parentItem.newPickedQty) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to Picked quantity");
                            parentItem.quantity = "0";
                            Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        } 
                        else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }  
                    } 
                    else  if(parentItem.selectedUom.uomTypeId == 2)
                    {
                        var stripQty = parentItem.uomListData[1].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  = stripQty;      
                        if(parseInt(parentItem.quantity) > parentItem.newPickedQty){
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to Picked quantity");
                            parentItem.quantity = "0";
                            Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        }else{
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }
                    }  
                    else  if (parentItem.selectedUom.uomTypeId == 1)
                    {
                        var boxQty = parentItem.uomListData[1].conversion * parentItem.uomListData[2].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  =  boxQty;
                        if (parseInt(parentItem.quantity) > parentItem.newPickedQty) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to Picked quantity");
                            parentItem.quantity = "0";
                            Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        }else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }   
                    }  
                }
                else{
                    if(parentItem.selectedUom.uomTypeId == 3)
                    {
                        $scope.quantityIssuedNew  = parentItem.quantity;
                        if (parseInt(parentItem.quantity) > parentItem.newPickedQty) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to Picked quantity");
                            parentItem.quantity = "0";
                            Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        } else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }  
                    } 
                    else  if(parentItem.selectedUom.uomTypeId == 2)
                    {
                        var stripQty = parentItem.uom[1].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  = stripQty;      
                        if(parseInt(parentItem.quantity) > parentItem.newPickedQty){
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to Picked quantity");
                            parentItem.quantity = "0";
                            Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        }else{
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }
                    }  
                    else  if (parentItem.selectedUom.uomTypeId == 1)
                    {
                        var boxQty = parentItem.uom[1].conversion * parentItem.uom[2].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  =  boxQty;
                       
                        if (parseInt(parentItem.quantity) > parentItem.newPickedQty) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to Picked quantity");
                            parentItem.quantity = "0";
                            Calculate(item, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        }else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }   
                    }  
                } 
            }else if(!parentItem.isFromAssignPicker){
                if(parentItem.isThreeUOMApplicable == false){
                    if(parentItem.selectedUom.uomTypeId == 3)
                    {
                        $scope.quantityIssuedNew  = parentItem.quantity;
                        if (parseInt(parentItem.quantity) > parentItem.quantityOrdered) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to ordered quantity");
                            parentItem.quantity = "0";
                            Calculate(parentItem, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                            return false;
                        } else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }  
                    } 
                    else  if(parentItem.selectedUom.uomTypeId == 2)
                    {
                        var stripQty = parentItem.uomListData[1].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  = stripQty;      
                        if(parseInt(parentItem.quantity) > parentItem.quantityOrdered){
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to ordered quantity");
                            parentItem.quantity = "0";
                            Calculate(parentItem, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                            return false;
                        }else{
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }
                    }  
                    else  if (parentItem.selectedUom.uomTypeId == 1)
                    {
                        var boxQty = parentItem.uomListData[1].conversion * parentItem.uomListData[2].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  =  boxQty;
                        if (parseInt(parentItem.quantity) > parentItem.quantityOrdered) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to ordered quantity");
                            parentItem.quantity = "0";
                            Calculate(parentItem, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        }else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }   
                    }
                }
                else{
                    if(parentItem.selectedUom.uomTypeId == 3)
                    {
                        $scope.quantityIssuedNew  = parentItem.quantity;
                        if (parseInt(parentItem.quantity) > parentItem.quantityOrdered) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to ordered quantity");
                            parentItem.quantity = "0";
                            Calculate(parentItem, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                            return false;
                        } else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }  
                    } 
                    else  if(parentItem.selectedUom.uomTypeId == 2)
                    {
                        var stripQty = parentItem.uom[1].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  = stripQty;      
                        if(parseInt(parentItem.quantity) > parentItem.quantityOrdered){
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to ordered quantity");
                            parentItem.quantity = "0";
                            Calculate(parentItem, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                            return false;
                        }else{
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }
                    }  
                    else  if (parentItem.selectedUom.uomTypeId == 1)
                    {
                        var boxQty = parentItem.uom[1].conversion * parentItem.uom[2].conversion * parseInt(parentItem.quantity);
                        $scope.quantityIssuedNew  =  boxQty;
                        if (parseInt(parentItem.quantity) > parentItem.quantityOrdered) {
                            parentItem.isParentQuantityValid = true;
                            growl.error("Please enter quantity less than or equal to ordered quantity");
                            parentItem.quantity = "0";
                            Calculate(parentItem, self.model.narcoticsIssueWorklistDetails);
                            isValid = false;
                        }else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                        }   
                    }
                } 
            }
          
            if( isValid == true){
          	  var finalOrdQty = 0;
          	  var totalIssuedQty = 0;
          	  
          	  if (parentIndex == undefined) {
          	
		            	  angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function (value, index) {
		                  	
			                  	if(value.itemId == parentItem.itemId){
			                  		
			                  		totalIssuedQty += CalculateOrederOrIssuedQty(value);
			                  		
			                  		if(value.selectedItems != null && value.selectedItems.length>0){
			                      		
				                  		angular.forEach(value.selectedItems, function (selectValue, selectIndex)  {
				                  			
				                  			totalIssuedQty += CalculateOrederOrIssuedQty(selectValue);
				                  			
				                 		 });
				                    }
			                  	}
			                  	
			                  
		                  });  
		            	  finalOrdQty = parentItem.finalOrderedQty;
          	     } else {
          	    	 
	            	    	 if(self.model.narcoticsIssueWorklistDetails.items[parentIndex].disabled == undefined){
	            	    		 totalIssuedQty += CalculateOrederOrIssuedQty(self.model.narcoticsIssueWorklistDetails.items[parentIndex]);
	            	    	 }
       	                	
       	                	finalOrdQty = self.model.narcoticsIssueWorklistDetails.items[parentIndex].finalOrderedQty;
       	                	if(self.model.narcoticsIssueWorklistDetails.items[parentIndex].selectedItems != null && self.model.narcoticsIssueWorklistDetails.items[parentIndex].selectedItems.length>0){
       	                    		
       	                		angular.forEach(self.model.narcoticsIssueWorklistDetails.items[parentIndex].selectedItems, function (selectValue, selectIndex) {
       	                			
       	                			totalIssuedQty += CalculateOrederOrIssuedQty(selectValue);
       	               		 	});
       	                  }
                   }
          	  
          	 
          	  if (finalOrdQty < totalIssuedQty) {
	       	              parentItem.isParentQuantityValid = true;
	                    //   growl.warning("  warning !! enter quantity is greater than ordered quantity ");
	                     // parentItem.quantity = "0";
	                      if(parentItem.substituteBtn !=undefined && parentItem.substituteBtn == true){
    	                    	parentItem.quantity = "0";
    	                      }
          	  	}else {
          	  		
//	                  parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
          	  	if(parentItem.quantityIssued!=undefined){
      	  			parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
      	  		}else{
      	  			parentItem.quantityIssued=0;
      	  		}
          	  	}
            }
            
            
            
        }
            
         /*   if(!!item){ 
            if (!item.quantity) {
                item['isQuantityValid'] = true;
                growl.error("Please enter quantity");
                isValid = false;
            }
            if (item.quantity == 0) {
                item['isQuantityValid'] = true;
                growl.error("Please enter quantity greater than zero");
                isValid = false;
            }
            if (item.newQty < item.quantity) {
                item['isQuantityValid'] = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max orderd quantity');
            }
            }*/
            return isValid;
        }
//New code add all button disabled
        /*var hideItem =function(item,index){
        	$scope.result = 0;
        	angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function(value){
        		$scope.substituteButtonFlag1= false;
                if(item.itemId==value.itemId){
                    $scope.result += value.quantity;
                    
                    if(value.finalOrderedQty == parseInt($scope.result)){
                		$scope.substituteButtonFlag1= true;
                    }
                    if(value.selectedItems != null && value.selectedItems.length > 0){
                    	angular.forEach(value.selectedItems, function(item){
                    		$scope.substituteButtonFlag1= false;
                    		$scope.result += item.quantity;
                    		 if(value.finalOrderedQty == parseInt($scope.result)){
                         		$scope.substituteButtonFlag1= true;
                         		
                             }
                    		
                    	});
                    	
                    }

                }
            });
        	
        }*/
        var ValidateBill = function (bill) {

            var isValid = true;
            var count = 0;

            if (self.model.narcoticsIssueWorklistDetails.discount) {
                if (!bill.discountCatgeory.id) {
                    isValid = false;
                    self.model.isDiscountCategoryValid = true;
                }

                if (!self.model.isDiscountApplied) {
                    isValid = false;
                    self.model.isDiscountAppliedValid = true;
                    growl.error('Please apply discount on the bill items');
                }

                if (self.model.discountTypeFlag == true) {
                    isValid = false;
                    self.model.isDiscountAppliedValid = true;
                    growl.error('Please apply discount on the bill items');
                }

                if (self.model.isAppliedDiscountValid == true) {
                    isValid = false;
                    growl.error('Please apply discount on the bill items');
                }
            }

            if (self.model.narcoticsIssueWorklistDetails.discountCatgeory.id) {
              var found = self.model.narcoticsIssueWorklistDetails.items.find(item => item.discountPerItem == 0)
                if (!self.model.narcoticsIssueWorklistDetails.discount && found!=undefined) {
               // if (!self.model.narcoticsIssueWorklistDetails.discount) {
                    isValid = false;
                    self.model.isDiscountAmountValid = true;
                    growl.error('Please enter discount amount');
                }
            }


            if (!bill.items || bill.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                bill.items.forEach(function (billitem) {
                	   if (!billitem.disabled && !billitem.itemDisabled) {
                		//    if(billitem.quantity !=undefined && billitem.quantity !=null && billitem.quantity > 0 && billitem.quantity !=""){
                    // billitem['isItemValid'] = false;
                    billitem['isBatchValid'] = false;
                    billitem['isUOMValid'] = false;
                    billitem['isQuantityValid'] = false;

                    // if (!billitem.selectedBatch.batchId || !billitem.selectedUom.uomUnitId || !billitem.quantity) {

                    //     billitem['isItemValid'] = true;
                    //     isValid = false;
                    // }
                    if(billitem.quantity){
                        if(billitem.selectedBatch != null){
                            if (!billitem.selectedBatch.batchId) {
                                billitem['isBatchValid'] = true;
                                isValid = false;
                            }
                        }
                    

                        if (!billitem.selectedUom.uomUnitId) {
                            billitem['isUOMValid'] = true;
                            isValid = false;
                        }
                    }

                    angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function(d){
                        if(d.quantity){
                            count++;
                        }
                    });
                    // if(count == 0){
                    //     if (!billitem.quantity) {
                    //         billitem['isQuantityValid'] = true;
                    //         isValid = false;
                    //         growl.error('Please enter quantity')
                    //     }
                        
                    //     if (billitem.quantity == 0) {
                    //     	billitem['isQuantityValid'] = true;
                    //         growl.error("Please enter quantity greater than zero");
                    //         isValid = false;
                    //     }
                    // }

                  

                    if (billitem.newQty < parseInt(billitem.quantity)) {
                        billitem['isQuantityValid'] = true;
                        isValid = false;
                        growl.error('Entered quantity is exceeding the max orderd quantity');
                    }

                    if (!ValidateUniqueBatchAndUom(billitem)) {
                        isValid = false;
                    }
                    if (!ValidateBatch(billitem)) {
                        isValid = false;
                    }
                    if (!ValidateUOM(billitem)) {
                        isValid = false;
                    }
                    if (!ValidateQuantity(billitem)) {
                        isValid = false;
                    }
                // }
                    
                }
                	   
                	   if (billitem.selectedItems && !billitem.itemDisabled) {
                    	   
                    	   billitem.selectedItems.forEach(function (item) {
                    		//    if(billitem.quantity !=undefined && billitem.quantity !=null && billitem.quantity > 0 && billitem.quantity !=""){
                           // billitem['isItemValid'] = false;
                    		   item['isBatchValid'] = false;
                    		   item['isUOMValid'] = false;
                    		   item['isQuantityValid'] = false;

                           // if (!billitem.selectedBatch.batchId || !billitem.selectedUom.uomUnitId || !billitem.quantityIssued) {

                           //     billitem['isItemValid'] = true;
                           //     isValid = false;
                           // }

                           if (!item.selectedBatch.batchId) {
                        	   item['isBatchValid'] = true;
                               isValid = false;
                           }

                           if (!item.selectedUom) {
                        	   item['isUOMValid'] = true;
                               isValid = false;
                           }

                            if (!item.disabled && !item.itemDisabled){
                                if(item.quantity){
                                    count++; 
                                }	
                            }
                            
                            // if(count == 0){
                            //     if (item.quantity == undefined || String(item.quantity) <= 0) {
                            //         item['isQuantityValid'] = true;
                            //         isValid = false;
                            //         growl.error('Please enter quantity')
                            //     }
                                
                            //     if (item.quantityIssued < 0) 
                            //     {
                            //  	   item['isQuantityValid'] = true;
                            //         growl.error("Please enter quantity greater than zero");
                            //         isValid = false;
                            //     }
                            // }
                           

                          

                           if (item.newQty < item.quantityIssued) {

                        	   item['isQuantityValid'] = true;
                               isValid = false;
                               growl.error('Entered quantity is exceeding the max orderd quantity');
                           }

                           if (!ValidateUniqueBatchAndUom(item)) {
                               isValid = false;
                           }
                          /* if (!ValidateBatch(item)) {
                               isValid = false;
                           }
                           if (!ValidateUOM(item)) {
                               isValid = false;
                           }
                           if (!ValidateQuantity(item)) {
                               isValid = false;
                           }
                           if (!ValidateMaxOrderQuantity(item)) {
                               isValid = false;
                           }*/
                    	//    }
                       });
                       }
 
                });
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }

        var ValidateSaveButton = function () {
            var isValid = false;

            self.model.narcoticsIssueWorklistDetails.items.find(function (item) {
                if (!item.itemDisabled) {
                    isValid = true;
                }
            });
            return isValid;
        }

    var CalculateItemSum = function (itemList) 
    {
        $scope.totalSelfPayable = parseFloat(0);
        $scope.totalCreditPayable = parseFloat(0);
        angular.forEach(itemList, function (item, key) 
        {
            if ($scope.taxInclusive == true) 
            {
            	if(!item.itemDisabled && !item.disabled){
              item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
            //   item.netAmount = (((item.totalAmount) - (isNaN(item.discount)?0:item.discount)));
              item.netAmount = item.netAmount;
              item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
              item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
              item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
              
              $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(item.selfPayable);
              $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(item.creditPayable);
              
              if (item.selectedItems && !item.itemDisabled) {
                  angular.forEach(item.selectedItems, function (sitem, key) {
                	  if(!sitem.disabled){ 
                	  sitem.totalAmount = ((sitem.rate ? sitem.rate : 0) * (sitem.quantity ? sitem.quantity : 0));
                      sitem.netAmount = sitem.netAmount;
                      sitem.packageConsumptionAmt = (isNaN(sitem.packageConsumptionAmt)?0:sitem.packageConsumptionAmt);
                      sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
                      sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                      
                      $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(sitem.selfPayable);
                      $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(sitem.creditPayable);
                	  }
                	  });
              }
            }else if(item.selectedItems && !item.disabled){
            	 
            	 angular.forEach(item.selectedItems, function (sitem, key) 
                	{
            		 if(!sitem.disabled){
            			 sitem.totalAmount = ((sitem.rate ? item.rate : 0) * (sitem.quantity ? sitem.quantity : 0));
                         sitem.netAmount = sitem.netAmount;
                         sitem.packageConsumptionAmt = (isNaN(sitem.packageConsumptionAmt)?0:sitem.packageConsumptionAmt);
                         sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
                         sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                         
                         $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(sitem.selfPayable);
                         $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(sitem.creditPayable);
                  }
                	 
                  });
             }
            }
            else 
             {
            	if(!item.itemDisabled && !item.disabled){
              item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantity ? item.quantity : 0));
              item.taxAmount = (((item.totalAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)) * ((item.taxPercent == undefined ? (item.taxPercentage == undefined ? 0 : item.taxPercentage ) : item.taxPercent) / 100));
              item.netAmount = item.netAmount;
              item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
              item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
              
              $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
              $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
              
              if (item.selectedItems && !item.itemDisabled) {
                  angular.forEach(item.selectedItems, function (sitem, key) {
                	  if(!sitem.disabled){
                		  sitem.totalAmount = ((sitem.rate ? sitem.rate : 0) * (sitem.quantity ? sitem.quantity : 0));
                		  sitem.netAmount = sitem.netAmount;
                		  sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
                		  sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                      
                      $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(sitem.selfPayable);
                      $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(sitem.creditPayable);
                	  }
                	  });
              }
            }else if(item.selectedItems && !item.disabled){
           	 
           	 angular.forEach(item.selectedItems, function (sitem, key) 
               	{
           		 if(!sitem.disabled){
           			sitem.totalAmount = ((sitem.rate ? sitem.rate : 0) * (sitem.quantity ? sitem.quantity : 0));
           			sitem.netAmount = sitem.netAmount;
           			sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
           			sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                    
                    $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(sitem.selfPayable);
                    $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(sitem.creditPayable);
                }
               	 
                 });
            }
             }
            
        });
        	
        $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable).toFixed(2);
        $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable).toFixed(2);
    }

    //get sponsor details by encounter or admissionId start
    $scope.getSponsorDetails=function(admissionId){
        $scope.activePackageList =[];
        $rootScope.startLoader();
        var param = {
            admissionId:admissionId,
        };
        console.log("Id data",param);
        var URI = BASE_URL + ROOT_URL + "/global/consumption/getActivePaymentEntitlement"
        BillingGenericService.serviceAction(METHOD_POST,URI,param,"").then(function(response) {
            if (response.data.status == "success")
            {
                $scope.paymentEntitleMentList=response.data.listObject;
                $scope.paymentEntitleMentListDummy =angular.copy($scope.paymentEntitleMentList);
                if($scope.paymentEntitleMentList!=null && $scope.paymentEntitleMentList.length>0)
                {
                    $scope.entitlementObj.paymentEntitlementTypeId = $scope.paymentEntitleMentList[0].paymentEntitlementId;
                    $scope.isContractChanged = $scope.paymentEntitleMentList[0].isContractChanged;
                    $scope.isPayeeUpdated = $scope.paymentEntitleMentList[0].isPayeeUpdated;
                    $scope.paymentEntitlementTypeIdDummy =angular.copy($scope.entitlementObj.paymentEntitlementTypeId);
                    //$scope.isDiscountApplicable = $scope.paymentEntitleMentList[0].isTariffRateApplicable;
                    $scope.isDiscountApplicable ='Y';
                    if(($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3) && $scope.isConsumed == false){
                        $scope.isValidated = true;
                    }
                    else{
                        $scope.isValidated = false;
                    }
                }
                else{
                    $scope.entitlementObj.paymentEntitlementTypeId = $scope.selfPayeeId;
                    $scope.isContractChanged ='N';
                    $scope.isPayeeUpdated ='N';
                    $scope.isDiscountApplicable = 'Y';
                    $scope.paymentEntitlementTypeIdDummy =angular.copy($scope.entitlementObj.paymentEntitlementTypeId);
                    if(($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3) && $scope.isConsumed == false){
                        $scope.isValidated = true;
                    }
                    else{
                        $scope.isValidated = false;
                    }
                }
                
                //for package
                $scope.activePackageList = response.data.object;
                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                    $scope.isValidated = true;
                }
                $rootScope.stopLoader();
            }
            else if(response.data.status == "error")
            {
                    $rootScope.stopLoader();
            }    
        });
    }
    
    $scope.saveAndValidateContract = function(bill)
    {
    	
    	ActulsaveAndValidateContract(bill);
    	
    	/*
    	 self.model.stopItemIds = [];
    	 angular.forEach(bill.items, function (item, key)  {
             if (!item.disabled && !item.itemDisabled) 
                {
                  self.model.stopItemIds.push(item.itemId);
                }
          });        	 
		var stopData = {
	                "itemIds": self.model.stopItemIds,
	                "storeId": $scope.storeId   	      		 
	              };      	    
   	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
            var StopTransactionData = responseData;  
            if(StopTransactionData.length > 0 ){    
                     var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                     var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                     self.model.stopItemIds = [];                 
                     $rootScope.stopLoader();                     
                     var stopMessage = "Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ";                             
               	    growl.warning(stopMessage, {onclose: function() {
               		  var popup = angular.element('#confirmStopValidate');
                         popup.modal('show');    
                   },}); 
             }
            else{
            	ActulsaveAndValidateContract(bill);
            }
   	  });
    */}
    

    var ActulsaveAndValidateContract = function(bill)
    {
     if(ValidateBill(bill)){
        $rootScope.startLoader();	 
        var data = {
            "contractId" : '',
            "organizationId" : $scope.organizationId,
            "unitId" : $scope.unitId,
            "visitTypeId" : bill.visitType,
            "paymentEntitlementId" : $scope.entitlementObj.paymentEntitlementTypeId,
            "encounterId" : '',
            "admissionId" : $scope.admissionId,
            "billingBedCategoryId" : '',
            "patientCategoryId" : '',
            "defaultSelfTariffId" : '',
            "payeeId" : '',
            "selfPayeeId" : $scope.selfPayeeId,
            "patientClassId" : '',
            // "patientClassPercentage" : '',
            "patientId" :bill.patientId,
            "tariffId" : $scope.defaultSelfTariffId,
            "taxInclusive" : $scope.taxInclusive,
            "doctorId" : bill.doctorId,
            "consume" :false,
            "itemList" : []
        }

        angular.forEach(bill.items, function (item, key) {
            if (!item.disabled && !item.itemDisabled  && item.selectedBatch!=null) {
                if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                    var billItem =  {
                        "itemId" : item.itemId,
                        "batchId" : item.selectedBatch.batchId,
                        "rate" : item.rate,
                        "quantity" : Number(item.quantity),
                        "selfQuantity" : '',
                        "packageConsumptionAmt" : 0,
                        "totalAmount" : item.itemtotalAmount,
                        "discountAmount" : item.itemDiscount,
                        "unitDiscountAmount" : item.unitDiscountAmount,
                        "netAmount" : item.netAmount.toFixed(2),
                        "taxId" : item.taxId,
                        "taxPercentage" : item.taxPercentage,
                        "taxAmount" : item.taxAmount,
                        "productCategoryId" : item.productCategoryId,
                        "payeeId" : !! $scope.selfPayeeId ? $scope.selfPayeeId : item.payeeId,
                        "contractId" : '',
                        "creditPayable" : !!item.creditPayable ? item.creditPayable : 0,
                        "selfPayable" : !!item.selfPayable ? item.selfPayable : item.netAmount.toFixed(2),
                        "index" : key,
                        "childIndex" : '',
                        "consessionPercentage": item.patientClassPercentage,
                        "consessionAmount": item.patientClassAmount,
                        "unitConsessionAmount": item.unitPatientClassAmount,
                        "saleDetailPayeeMapperDtoList" : !!item.saleDetailPayeeMapperDtoList ? item.saleDetailPayeeMapperDtoList : [],
                        "saleDetailPackageMapperDtoList" : !!item.saleDetailPackageMapperDtoList ? item.saleDetailPackageMapperDtoList : [],
                        "isConsumed" : false,
                        "consumedItemId" : item.consumedItemId,
                        "consumedPcategroyId" : item.consumedPcategroyId,
                    }
                    //Ganesh
                    $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                    $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                    data.itemList.push(billItem);
                }
            }
            if (item.selectedItems && !item.itemDisabled) {
                if (item.selectedItems && !item.itemDisabled) {
                    angular.forEach(item.selectedItems, function (item, ckey) {
                    	if(!item.itemDisabled  && item.selectedBatch!=null){	
                            if(item.quantity !=undefined && item.quantity !=null && item.quantity > 0 && item.quantity !=""){
                                var billSubItem = {
                                    "itemId" : item.itemId,
                                    "batchId" : item.selectedBatch.batchId,
                                    "rate" : item.rate,
                                    "quantity" : Number(item.quantity),
                                    "selfQuantity" : '',
                                    "packageConsumptionAmt" : 0,
                                    "totalAmount" : item.itemtotalAmount,
                                    "discountAmount" : item.itemDiscount,
                                    "unitDiscountAmount" : item.unitDiscountAmount,
                                    "netAmount" : item.netAmount.toFixed(2),
                                    "taxId" : item.taxId,
                                    "taxPercentage" : item.taxPercentage,
                                    "taxAmount" : item.taxAmount,
                                    "productCategoryId" : item.productCategoryId,
                                    "payeeId" : !! $scope.selfPayeeId ? $scope.selfPayeeId : item.payeeId,
                                    "contractId" : '',
                                    "creditPayable" : !!item.creditPayable ? item.creditPayable : 0,
                                    "selfPayable" : !!item.selfPayable ? item.selfPayable : item.netAmount.toFixed(2),
                                    "index" : key,
                                    "childIndex" : ckey,
                                    "consessionPercentage": item.patientClassPercentage,
                                    "consessionAmount": item.patientClassAmount,
                                    "unitConsessionAmount": item.unitPatientClassAmount,
                                    "saleDetailPayeeMapperDtoList" : !!item.saleDetailPayeeMapperDtoList ? item.saleDetailPayeeMapperDtoList : [],
                                    "saleDetailPackageMapperDtoList" : !!item.saleDetailPackageMapperDtoList ? item.saleDetailPackageMapperDtoList : [],
                                    "isConsumed" : false,
                                    "consumedItemId" : item.consumedItemId,
                                    "consumedPcategroyId" : item.consumedPcategroyId,
                                }                           
                                $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                                $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
                                data.itemList.push(billSubItem);
                            }
                        }
                    });
                }
            }
        });
        $scope.currentDate = new Date();
        $scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
        var sponserList = [];

        var URI = BASE_URL + ROOT_URL + "/api/pharmacy/sales/walkins/consumeItemPackageContract"
        BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
            if (response.data.status == "success")
            {
                if(response.data.listObject.length>0)
                {			
                    angular.forEach(bill.items, function (value, key) 
                    {
                        angular.forEach(response.data.listObject, function (consumedItems, consumedKey) 
                        {
                        	if (!value.disabled && !value.itemDisabled && value.selectedBatch!=null) 
                            {
                            if(consumedItems.batchId==value.selectedBatch.batchId && consumedItems.itemId==value.itemId
                            		&& key==consumedItems.index)   
                            {
                                
                                    value.creditPayable = Number(parseFloat(consumedItems.creditPayable).toFixed(2));
                                    value.selfPayable = Number(parseFloat(consumedItems.selfPayable).toFixed(2));
                                    value.netAmount =  Number(parseFloat(consumedItems.netAmount).toFixed(2));
                                    value.isConsumed = false;
                                    value.contractId  = consumedItems.contractId;
                                    value.payeeId = consumedItems.payeeId;
                                    value.packageConsumptionAmt = consumedItems.packageConsumptionAmt;
                                    value.saleDetailPayeeMapperDtoList =consumedItems.saleDetailPayeeMapperDtoList;
                                    value.saleDetailPackageMapperDtoList =consumedItems.saleDetailPackageMapperDtoList;
                                    $scope.isConsumed = value.isConsumed;
                                    if(value.isConsumed == false){
                                    	 $scope.isValidated=false;
                                    }
                                }
                            }
                        });
                    });
                    
                    angular.forEach(bill.items, function (value1, key1) 
                            {
                    	 if (value1.selectedItems && !value1.itemDisabled) 
				          {
		                   	  angular.forEach(value1.selectedItems, function (value, cIndex) 
		                                 {
		                                angular.forEach(response.data.listObject, function (consumedItems, consumedKey) 
		                                {
		                                    if(consumedItems.batchId==value.selectedBatch.batchId && consumedItems.itemId==value.itemId
		                                    		&& key1==consumedItems.index && cIndex==consumedItems.childIndex)   
		                                    {
		                                        if (!value.disabled && !value.itemDisabled) 
		                                        {
		                                            value.creditPayable = Number(parseFloat(consumedItems.creditPayable).toFixed(2));
		                                            value.selfPayable = Number(parseFloat(consumedItems.selfPayable).toFixed(2));
		                                            value.netAmount =  Number(parseFloat(consumedItems.netAmount).toFixed(2));
		                                            value.isConsumed = false;
		                                            value.contractId  = consumedItems.contractId;
		                                            value.payeeId = consumedItems.payeeId;
		                                            value.packageConsumptionAmt = consumedItems.packageConsumptionAmt;
		                                            value.saleDetailPayeeMapperDtoList =consumedItems.saleDetailPayeeMapperDtoList;
		                                            value.saleDetailPackageMapperDtoList =consumedItems.saleDetailPackageMapperDtoList;
		                                            $scope.isConsumed = value.isConsumed;
		                                            if(value.isConsumed == false){
		                                            	 $scope.isValidated=false;
		                                            }
		                                        }
		                                    }
		                                });
		                                
		                            });
                            }
                            });
                    
                    
                    CalculateItemSum(bill.items);
                }
                if(response.data.object!=null)
                {
                    self.model.patientId = response.data.object.patientId;
                }
                // $scope.getSponsorDetails($scope.admissionId);
                $rootScope.stopLoader();
            }
            else if(response.data.status == "error")
            {
                    $rootScope.stopLoader();
            }
        });
    }
    }

    var groupBy = function(xs, key) {
        return xs.reduce(function(rv, x) {
        (rv[x[key]] = rv[x[key]] || []).push(x);
        return rv;
        }, {});
    };

    $scope.getPayeeCreditDetails = function(itemList)
    {
        var data ={
            payeeDesc: '',
            itemId: '',
            contractId: '',
            payeeId: '',
            payeeAmount: 0
        }
        $scope.payeeCreditDetailslList = [];
        var list =groupBy(itemList, 'contractId');
        angular.forEach(list,function(contract,key) 
        {
            var payeeAmount=0;
            var obj = angular.copy(data);
            angular.forEach(contract,function(payee,key) 
                    {
                        payeeAmount=payeeAmount+payee.payeeAmount;
                        obj.payeeId =payee.payeeId;
                        obj.contractId =payee.contractId;
                        obj.payeeDesc =payee.payeeDesc;
                    });
            obj.payeeAmount = payeeAmount;
            $scope.payeeCreditDetailslList.push(obj);
        });
    }
        
    $scope.getPackageDetails = function(itemList)
    {
        $scope.packageDetailsList = [];
        var data ={
            packageName: '',
            packageCode: '',
            packageConsumptionMasterId: '',
            packageId: '',
            packageAmount: 0,
            isPackageItem:'N'
        }
        $scope.payeeCreditDetailslList = [];
        var list =groupBy(itemList, 'packageConsumptionMasterId');
        angular.forEach(list,function(packageObj,key) 
        {
            var packageAmount=0;
            var obj = angular.copy(data);
            angular.forEach(packageObj,function(pkg,key) 
                    {
                        packageAmount=packageAmount+pkg.packageAmount;
                        obj.packageId =pkg.packageId;
                        obj.packageConsumptionMasterId =pkg.packageConsumptionMasterId;
                        obj.packageName =pkg.packageName;
                        obj.packageCode =pkg.packageCode;
                        obj.isPackageItem =pkg.isPackageItem;
                    });
            obj.packageAmount = packageAmount;
            $scope.packageDetailsList.push(obj);
        });
    }

        var NavigateToNarcoticsIssueWorklist = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('narcoticsIssueWorklist',{
                    obj: self.model.narcoticId
                });
            }, 1000);
        }

         //Method to get the list of Generic 
    var GetGenericList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.genericList = response.data;
            GetGenericMaster();
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }
      GetGenericList();
  
      var genericMasterList = [];
      var GetGenericMaster = function () {
        var arraylist = self.model.genericList.filter(x => x.status == true);
        for (let generic of arraylist){
          genericMasterList.push(generic);
        }       
        $rootScope.stopLoader();
      } 

      var GetStrengthUnitList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
              self.model.strengthUnitList = response.data;
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            },
            function errorCallback(response) {
              console.log("Error");
              console.log("Strength Units : ",self.model.strengthUnitList);
              $rootScope.stopLoader();
            });
      }
      GetStrengthUnitList();
  
      var ViewCombinationDetails = function(item) {
        self.model.genericCombinationList = [];
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            for(let combination of response.data) {
              let combinationName = genericMasterList.filter(x => x.id == combination.subGenericId);
              combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
              combination.code = item.code;
              combination.genericName = item.name;
            }
            self.model.strengthUnitList.forEach(function (strength){
                response.data.forEach(function (data){
                  if(data.strength == strength.id){
                    $scope.strength = strength.unit;
                    var obj = {
                      strength: "",
                      code:"",
                      genericId: "",
                      genericName: "",
                      id:"",
                      name:"",
                      strengthUnit:"",
                      subGenericId: "",
                    }
                    obj.strength = $scope.strength;
                    obj.code = data.code;
                    obj.genericId = data.genericId;
                    obj.genericName = data.genericName;
                    obj.id = data.id;
                    obj.name = data.name;
                    obj.strengthUnit = data.strengthUnit;
                    obj.subGenericId = data.subGenericId;
                    self.model.genericCombinationList.push(obj);
                  }
                });
              });
            var popup = angular.element("#combinationDetails");
            popup.modal("show");
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }
      
    //chetana
      
      $scope.openDocument = function(obj)
		{ 
		    $('#fileId').val(undefined);
			$scope.encounterDetailsContract = {
              encounterDocId : "",
              encounterId : 0,
              admissionId:0,
              visitTypeId:1,
              patientId : "",
              documentPath : "",
              documentTypeId : '',
              status : 'A',
              createdDate : "",
              createdBy : $scope.userId,
              updatedDate : "",
              updatedBy : $scope.userId,
              unitId : $scope.unitId,
              organisationId : $scope.organizationId,
              file : {}
          }
		
          if($scope.idpatient == null || $scope.idpatient =='' || $scope.idpatient ==undefined){
              growl.warning(
                  'Please select atleast one patient!!!.',
              {
                  title : 'warning!'
              });
              $scope.encounterDetailsContractList =[];
              return;
          }
				 
          if($scope.entitlementObj.paymentEntitlementTypeId == 1){						
              $scope.encounterDetailsContractList =[];
              var encounterDetails = angular.copy($scope.encounterDetailsContract);
              encounterDetails.patientId = $scope.idpatient;
              encounterDetails.encounterId = $scope.encounterId;
              encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
              $scope.encounterDetailsContractList.push(encounterDetails);	
              $scope.getPatientDocumentList(encounterDetails);
              $('#documentUploadModalForEncounter').modal('show');
          }else if($scope.entitlementObj.paymentEntitlementTypeId == 2){
              $scope.encounterDetailsContractList =[];
              var encounterDetails = angular.copy($scope.encounterDetailsContract);
              encounterDetails.documentTypeId = '30';	
              encounterDetails.patientId = $scope.idpatient;
              encounterDetails.encounterId = $scope.encounterId;
              encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
              $scope.encounterDetailsContractList.push(encounterDetails);
              $scope.getPatientDocumentList(encounterDetails);
              $('#documentUploadModalForEncounter').modal('show');
          }else if($scope.entitlementObj.paymentEntitlementTypeId == 3){
              $scope.encounterDetailsContractList =[];
              var encounterDetails = angular.copy($scope.encounterDetailsContract);
              encounterDetails.documentTypeId = '19';	
              encounterDetails.patientId = $scope.idpatient;
              encounterDetails.encounterId = $scope.encounterId;
              encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
              $scope.encounterDetailsContractList.push(encounterDetails);
              $scope.getPatientDocumentList(encounterDetails);		
              $('#documentUploadModalForEncounter').modal('show');
          }else{
						
          }			
		}
      
      $scope.getPatientDocumentList = function(obj)
      {
          if($scope.idpatient == null || $scope.idpatient == '' || $scope.idpatient == undefined){
			    growl.warning(
					'Please select atleast one patient!!!.',
					{
						title : 'warning!'
					});
				$scope.encounterDetailsContractList =[];
				return;
		    }
          $rootScope.startLoader();
          var data =
          {
              "patientId": $scope.idpatient,
              "unitId":$scope.unitId,
              "organisationId":$scope.organizationId,
              "userId":$scope.userId,
              "visitTypeId":1,
              "encounterId": $scope.encounterId,
          }
          var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
          BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
              $scope.patientDocumentList = [];
              if (response.data.status == "success") 
              {

                  $scope.patientAllDocumentList = response.data.listObject;
                  $scope.patientDocumentList=[];
                  angular.forEach($scope.patientAllDocumentList, function(value, key) 
                  {  
                      if($scope.entitlementObj.paymentEntitlementTypeId == 1){
                          if(value.documentTypeId !=30 && value.documentTypeId!=19){				            				
                              var data={
                                  documentTypeId:value.documentTypeId,
                                  documentName:value.documentName,
                                  documentPath:value.documentPath,
                                  createdDate:value.createdDate,
                                  bucketFlag:value.bucketFlag
                              }
                              $scope.patientDocumentList.push(data);
                          }
                      }else if($scope.entitlementObj.paymentEntitlementTypeId == 2){
                          if(value.documentTypeId==30){				            				
                              var data={
                                  documentTypeId:value.documentTypeId,
                                  documentName:value.documentName,
                                  documentPath:value.documentPath,
                                  createdDate:value.createdDate,
                                  bucketFlag:value.bucketFlag
                              }
                              $scope.patientDocumentList.push(data);
                          }
                      }else if($scope.entitlementObj.paymentEntitlementTypeId == 3){
                          if(value.documentTypeId==19){				            				
                              var data={
                                  documentTypeId:value.documentTypeId,
                                  documentName:value.documentName,
                                  documentPath:value.documentPath,
                                  createdDate:value.createdDate,
                                  bucketFlag:value.bucketFlag
                              }
                              $scope.patientDocumentList.push(data);
                          }
                      }else{
                  
                      }	
                  });
                  $rootScope.stopLoader();
              }
              else if(response.data.status == "error")
              {
                  $rootScope.stopLoader();
              }
          });
      }
      
      var getAllPatientDiagnosisForIcon= function(patientId){
          self.model.patientDiagnosisListIcon =[];
          var cookieObject = $cookies.getObject('cookieObject');
           var headerObjDia = {
                       'unit_id' : cookieObject.unitId,
                      'organization_id' : cookieObject.organizationId,
                      'patient_id' : patientId,
                     'Content-Type':'application/json',
           };
           var data={};
           var URI = CONSTANTS.ROOT_URL
           + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
           GenericServiceParamHeader
           .serviceAction(METHOD_GET, URI,
              data,
              NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
           .then(
                   function(response) {
                       console.log(response);
                       // $rootScope.startLoader();
                       if (response.data.status == "success")
                       self.model.patientDiagnosisListIcon = response.data.listObject; 
                       console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
                           self.model.comorbidityFlag = false;
                           self.model.notifiableFlag = false;
                          
                              if(self.model.patientDiagnosisListIcon.length > 0)
                                  {
                                      self.model.complaintDelete=false;
                                      angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
                                          if(value.isNotifiable == 'Y')
                                          {
                                              self.model.notifiableFlag = true;
                                          }
                                          else if(value.isComorbidity == 'Y')
                                          {
                                              self.model.comorbidityFlag = true;
                                          }
                                      });
                                  }
                       
                   });
          }
      
      $scope.getDocumentType = function(){
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
      $scope.getDocumentType();

      $scope.documentName = function(id)
      {
      
          var documentTypeName = $.grep($scope.documentTypeList, function (doc) {
              return doc.documentTypeId == id;
          })[0].documentTypeName;
          return documentTypeName;
      }

      $scope.uploadContractDoc = function()
      {
          $scope.countFrValidation = 0;
          angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
              if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
                  growl.warning("Please select document type .",
                  {
                      title : 'warning!'
                  });
                  $rootScope.stopLoader();
                  return false;
              }
              if (angular.equals(value.file, {}) || value.file==undefined || value.file==null) {
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
              $scope.countFrValidation++;
			});
          if($scope.encounterDetailsContractList.length!=$scope.countFrValidation){
              return false;
          }
          $rootScope.startLoader();
          $scope.count = 0;
          angular.forEach($scope.encounterDetailsContractList, function(value, key) 
          {  
              $('#documentUploadModalForOP').modal('hide');
              var promise = $scope.uploadEncounterContractDoc(value);
              promise.then(function(response) 
              {
                  if(response.data.status == "success")
                  {
                      $scope.count++;
                      if($scope.encounterDetailsContractList.length==$scope.count)
                      {
                          growl.success("Document Uploaded Successfully.",
                          {
                              title : 'Success!'
                          });
                          $rootScope.stopLoader();
                          $('#documentUploadModalForEncounter').modal('hide');
                          $scope.encounterDetailsContractList = [];
                          $scope.getPatientDocumentList();
                      }
                  }
                  else 
                  {
                      growl.error('Something went wrong.',
                      {
                          title : 'Error!'
                      });
                      $rootScope.stopLoader();
                  }
              });
          });
      }

      $scope.uploadEncounterContractDoc = function(Obj)
		{
          var fd = new FormData();
          fd.append('encounterId', 0);
          fd.append('visitTypeId', Obj.visitTypeId);
          fd.append('admissionId', $scope.admissionId);
          fd.append('patientId',$scope.idpatient);
          fd.append('documentTypeId', Obj.documentTypeId);
          fd.append('createdBy',Obj.createdBy );
          fd.append('updatedBy', Obj.updatedBy);
          fd.append('unitId', Obj.unitId);
          fd.append('organisationId', Obj.organisationId);
          fd.append('file',Obj.file);
          fd.append('expenseId', 0);
          fd.append('grnId',0);
          fd.append('poId',0);
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
      
        $scope.formulationPopup = function (item) {
            $scope.formulationList = [];
            var obj = {
                "manufacturerName" : ''
            }
            obj.manufacturerName = item.manufacturerName;
            $scope.formulationList.push(obj);
            $('#formulationType').modal('show');
        }
      
      var GetNarcoticIssueForPrint= function (printId) {
      	self.model.printId=printId;
      }
      
      var PrintNarcoticsIssueWorklistReport=function(){
      	
	      var data = {
	        "appName": "HIS",
	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
	        "reportDesignFilePath": "prescrptioncasesheet_narcotics.rpt",
	        "reportFormat": "PDF",
	        "exportFileName": "NarcoticIssuesDetails",
	        "reportParams": [
	          {
	            "paramName": "@id",
	            "paramType": "Integer",
	            "paramValue": self.model.printId
	          }
	        ]
	      };
	      reportService.generate(REPORT_API_URI1, data);
        }

        $scope.clearDiscount = function() {
            angular.forEach(self.model.narcoticsIssueWorklistDetails.items, function(data){
              data.discountPerItem = 0;
              self.model.narcoticsIssueWorklistDetails.discount = 0;
              self.model.narcoticsIssueWorklistDetails.discountCatgeory = self.model.discountCategoryList[0];
              Calculate(data, self.model.narcoticsIssueWorklistDetails);
            });
        }

        $scope.subFlagChange = function()
        {
            self.model.narcoticsIssueWorklistDetails.items.forEach(function (item) {
                item.substititeFlag=false;
            });
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsNarcoticsIssueWorklist", {
            url: "/detailsNarcoticsIssueWorklist",
            templateUrl: 'views/PharmacyInventory/NarcoticsStore/details-narcotics-issue-worklist.html',
            controller: "DetailsNarcoticsIssueWorklist.Controller",
            controllerAs: "vm",
            title: 'Issue Worklist'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsNarcoticsIssueWorklist.Controller", detailsNarcoticsIssueWorklistController)
})();