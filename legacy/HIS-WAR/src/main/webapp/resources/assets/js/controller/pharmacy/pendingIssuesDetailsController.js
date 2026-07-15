(function () {
    "use strict";

    function pendingIssuesDetailsController($scope, $rootScope, $http, $window,  CONSTANTS, GenericService,GenericServiceFileUpload, $filter, growl, CurrencyService, StatusService, commonDetailService, $state, $cookies,pharamcyContractService,BillingGenericService, reportService,stopTransactionService,$timeout) {
        var self = this;
        var discountLimit = 100;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        $scope.cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
        $scope.storeName = $scope.cookieObjectStore.storeName;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isBatchValid: [],
                isUOMValid: [],
                SelectedItems: [],
                isQuantityValid: [],
                discountCategoryList: [{
                    "discountCategory": "Select Discount Category"
                }],
                pendingIssuesDetails: {
                    items: []
                },
                statusId: null
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            $scope.saveFlag = true;
            $scope.discountDisabled = true;
            self.model.GetPendingIssuesDetails = GetPendingIssuesDetails;
            self.model.BatchChange = BatchChange;
            self.model.UomChange = UomChange;
            self.model.ApplyDiscount = ApplyDiscount;
            self.model.AddItem = AddItem;
            self.model.RemoveItem = RemoveItem;
            self.model.AllocateQuantity = AllocateQuantity;
            self.model.ValidateBill = ValidateBill;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.ConvertQtyOrdered = ConvertQtyOrdered;
            self.model.SaveBill = SaveBill;
            self.model.SavePopup = SavePopup;
            self.model.SetDecimal = SetDecimal;
            self.model.ApplyDiscountValidation = ApplyDiscountValidation;
            self.model.NavigateToPendingIssues = NavigateToPendingIssues;
            self.model.ValidateSaveButton = ValidateSaveButton;
            self.model.OnKeyPressQuantity = OnKeyPressQuantity;
            self.model.ValidateMaxOrderQuantity = ValidateMaxOrderQuantity;
            self.model.ValidateBatch = ValidateBatch;
            self.model.ValidateUOM = ValidateUOM;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateUniqueBatchAndUom = ValidateUniqueBatchAndUom;
            self.model.PrintPendingIssuesDetailsReport = PrintPendingIssuesDetailsReport;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            //for substitute
            self.model.SubstituteItems = SubstituteItems;
            self.model.Add = Add;
            self.model.AddSubstituteItem = AddSubstituteItem;
            self.model.RemoveSubstituteItem = RemoveSubstituteItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            self.model.SelectSubstituteItem = SelectSubstituteItem;
            self.model.AddItemForSubstitute = AddItemForSubstitute;
            self.model.validateUOMAndBatch = validateUOMAndBatch;
            
            var cookieObject = $cookies.getObject('cookieObject');
            self.model.unitId = cookieObject.unitId;
            self.model.orgId = cookieObject.organizationId;
            self.model.userId = cookieObject.userId;
            self.model.storeId = cookieObject.storeId;
            $scope.storeId = cookieObject.storeId;
            $scope.autoBatch = cookieObject.autoBatch;
            $scope.autoPrescribedQty = cookieObject.autoPrescribedQty;
            $scope.atCopRequired = cookieObject.atCopRequired;
            self.model.ActualSaveBill  = ActualSaveBill;
            
            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
            if (cookieObjectAppConfig == undefined) {
                $state.go('login');
                return;
            }
            $scope.removeItemFlag = false;
            $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
            $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
            $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
            $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
            $scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
            $scope.isConsolidatedBill=cookieObjectAppConfig.isConsolidatedBill;

            self.model.BarcodeScan = BarcodeScan;
            self.model.SetFocus = SetFocus;
            self.model.AddBatchToDetail = AddBatchToDetail;
            self.model.ClearBarcode = ClearBarcode;
            self.model.ConfirmRemove = ConfirmRemove;

            self.model.SearchItems = SearchItems;
            self.model.Calculate = Calculate;
            self.model.onChangeDiscount = onChangeDiscount;
            self.model.calculateAtcop = calculateAtcop;
            self.model.AddItemToBill = AddItemToBill;
            GetPendingIssuesDetails(commonDetailService.getDataId());
            GetPendingIssuesDetailsForPrint(commonDetailService.getDataId());
            
            
            $scope.entitlementObj={
          		  "paymentEntitlementTypeId":1
            }	
            $scope.admissionId = pharamcyContractService.billObj.admissionId;
            $scope.isFinalDischarge=pharamcyContractService.billObj.isFinalDischarge;
            $scope.prescriptionId = pharamcyContractService.billObj.prescriptionId;
            commonDetailService.setDataId(null);
            $scope.showNonFormularyItemList();
            SetFocus();
        }

        $scope.popupStore = function () {
			var popup = angular.element('#storePopUp');
			popup.modal('show');
		}

        var SearchItems = function (search) {
            if (search.length < 1) return;
            $scope.isNarcotic=false;
            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
        
                var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId+"/" + true + "/" + $scope.isNarcotic + "/" + search + "/" + $cookies.getObject('cookieObject').storeId;
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
        
        var NoItemFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "No item found with item code or name " + search
            };
            self.model.items = [item];
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
                */
            }
        } 
        
        var ActualAddItemToBill = function (bill, item, model, label) {
        	$scope.itemSelected = {};
        	$scope.itemSelected = item;
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
                        }
                    }, response.data);
        
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
                            isLasa: response.data.isLasa,
                            isShow : bill.items.length >=1 ? true: false,
                            isFromAssignPicker : $scope.assignPickerFlag!=undefined ? $scope.assignPickerFlag:false,
                            patientClassPercentage : $scope.patientClassPercentage,
                            saleDetailPayeeMapperDtoList:[],
                            SaleDetailPackageMapperList:[],
                            storageUnitName: response.data.storageUnitName,
                            onlySubButtonDisable:true,
                            isThreeUOMApplicable: response.data.isThreeUOMApplicable
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
                        if(item.isThreeUOMApplicable == false){
                            item.selectedBatch.uomListData = item.selectedBatch.uom;
                            item.selectedBatch.uom = [];
                            angular.forEach(item.selectedBatch.uomListData, function(d){
                                if(d.uomTypeId != 2){
                                    item.selectedBatch.uom.push(d);
                                }
                            });
                        }
                        
                        if($scope.NonFormularyAdded == true){
                        	item.prescriptionDetailsId = $scope.itemSelected.prescriptionDetailsId;
                        	item.prescribedDose = $scope.itemSelected.prescribedDose;
                        	item.units = $scope.itemSelected.strengthDesc;
                        	item.duration = $scope.itemSelected.duration;
                        	item.durationUnit = $scope.itemSelected.durationUnit;
                        	item.frequencyDesc = $scope.itemSelected.frequencyDesc;
                        	item.qtyPrescribed = $scope.itemSelected.qtyPrescribed;
                        	item.instructionDesc = $scope.itemSelected.instructionDesc;
                        }
                        
                        bill.items.push(item);

                        if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                            growl.warning("Item is LASA drug..!",{
                                title : WARNING_MSG
                            });
                        }

                        ValidateUniqueBatchAndUom(item);
                        if(item.selectedBatch!=undefined)
                    	{
                            BatchChange(bill.items.length-1,self.model.pendingIssuesDetails, item.selectedBatch,null);
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
        
        $scope.atCopToSave=false;
        var calculateAtcop = function (atCop,flag) {
        	$scope.atCopSelected=atCop;
        	$scope.atCopToSave=$scope.atCopSelected;
        	$scope.globalFlag=flag;
        	var count=0;
            angular.forEach(self.model.pendingIssuesDetails.items, function (d){
            	count++;
            	if(d.selectedBatch!=undefined && d.selectedBatch!=null && d.selectedBatch.length!=0)
                {
            	if(d.disabled==false)	
                {
            		$scope.atCop=$scope.atCopSelected;
            		
            		if($scope.globalFlag==1)
                    {
            			if($scope.atCop == true){
                            d.rate = d.selectedBatch.actualLendingPrice;
                            Calculate(d, self.model.pendingIssuesDetails);
                            CalculateAmount(d);
                            $scope.atCopToSave=true;
                        }
                        else{
                            d.rate = d.selectedBatch.rate;
                            Calculate(d, self.model.pendingIssuesDetails);
                            CalculateAmount(d);
                            $scope.atCopToSave=false;
                        }
            			if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
            				angular.forEach(d.selectedItems, function (sub){
                				if($scope.atCop == true){
                					sub.rate = sub.selectedBatch.actualLendingPrice;
                					Calculate(sub, self.model.pendingIssuesDetails);
                                    CalculateAmount(sub);
                                    $scope.atCopToSave=true;
                                }
                                else{
                                	sub.rate = sub.selectedBatch.rate;
                                	Calculate(sub, self.model.pendingIssuesDetails);
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
                		    $scope.atCop = $scope.atCopToSave;
        				    angular.forEach(d.selectedItems, function (sub){
            				    if($scope.atCop == true){
            					    sub.rate = sub.selectedBatch.actualLendingPrice;
            					    Calculate(sub, d.selectedItems);
                                    CalculateAmount(sub);
                                    $scope.atCopToSave=true;
                                }
                                else{
                            	    sub.rate = (sub.selectedBatch.rate != undefined && sub.selectedBatch.rate != null)? sub.selectedBatch.rate :sub.selectedBatch.sellingPrice;
                            	    Calculate(sub, d.selectedItems);
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
                        Calculate(d, self.model.pendingIssuesDetails);
                        CalculateAmount(d);
                        $scope.atCopToSave=true;
                    }
                    else{
                        d.rate = d.selectedBatch.rate;
                        Calculate(d, self.model.pendingIssuesDetails);
                        CalculateAmount(d);
                        $scope.atCopToSave=false;
                    }
                    if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
        				angular.forEach(d.selectedItems, function (sub){
            				if($scope.atCop == true){
            					sub.rate = sub.selectedBatch.actualLendingPrice;
            					Calculate(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=true;
                            }
                            else{
                            	sub.rate = sub.selectedBatch.rate;
                            	Calculate(sub, d.selectedItems);
                                CalculateAmount(sub);
                                $scope.atCopToSave=false;
                            }
            			});
        			}
                }
            }
            	else
            		{
            		if(d.selectedItems != undefined && d.selectedItems != null && d.selectedItems.length > 0){
        				angular.forEach(d.selectedItems, function (sub){
            				if($scope.atCop == true){
            					sub.rate = sub.selectedBatch.actualLendingPrice;
            					Calculate(sub, self.model.pendingIssuesDetails);
                                CalculateAmount(sub);
                                $scope.atCopToSave=true;
                            }
                            else{
                            	sub.rate = sub.selectedBatch.rate;
                            	Calculate(sub, self.model.pendingIssuesDetails);
                                CalculateAmount(sub);
                                $scope.atCopToSave=false;
                            }
            			});
        			}
            		}
            });
            if(count==self.model.pendingIssuesDetails.items.length && $scope.globalFlag!=1)
            {
            	$scope.atCop=false;
            }
        }

        var ClearBarcode = function () {
            self.model.barcode = '';
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
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
        
        var GetCalculateOrederQty = function(item){
            var issuedQty = 0;
            var found =  item.batch.find(function (x) {
                return x.isSelected;
            });
          	 if(found==undefined || found==null)
          		 {
          		found =  item.zeroStockBatchList.find(function (x) {
                    return x.isSelected;
                });
          		 }
          	var uomTypeId = item.uomTypeId !=undefined ?item.uomTypeId : (found !=undefined ?found.uomTypeId : undefined );
          	if(uomTypeId == undefined){
          		return issuedQty;
          	}
            if(uomTypeId == 3)
            {
          		issuedQty  = parseInt(item.issuedQuantity);          
            }
            else if(uomTypeId == 2)
            {   	
                var stripQty = found.uom[1].conversion * parseInt(item.issuedQuantity);
                issuedQty  = stripQty;       
            }
            else if(uomTypeId== 1)
            {
                if(found.uom[0] == undefined || found.uom[0] == null){
                    var boxQty = found.uom[1].conversion *  found.uom[2].conversion * parseInt(item.issuedQuantity);
                    issuedQty  = boxQty;
                }
                else{
                    var boxQty = found.uom[0].conversion *  found.uom[1].conversion * parseInt(item.issuedQuantity);
                    issuedQty  = boxQty;
                }
                
            }    	  
            return issuedQty;
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
                    item.stockAvailable = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                } else if (selectedUomId == 2) {
                    item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                    item.newPickedQty = Math.ceil(pickedQty / ( batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                    item.stockAvailable = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                } else if (selectedUomId == 3) {
                    item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[2].conversion));
                    item.newPickedQty = Math.ceil(pickedQty / (batchObj.uom[2].conversion));
                    item.stockAvailable = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[2].conversion));
                }
            }
        }
        
     	  $scope.showNonFormularyItemList = function(){
  			try{
  				$rootScope.startLoader();
  				$scope.savePriscritpionId = $scope.prescriptionId;
  			    var URI = BASE_URL + ROOT_URL + "/api/opd/getNonFormularyItemForWorklistPharmacy/" +$scope.prescriptionId +"/"+ $scope.unitId+"/"+ $scope.organizationId ;
  				GenericService.serviceAction("GET",URI).then(function(response){
  					if(response.data.status == "success"){
  						$scope.nonFormularyItemList = [];
  						$scope.nonFormularyItemList = response.data.listObject;
  						if($scope.nonFormularyItemList != null && $scope.nonFormularyItemList.length > 0 ){
  							$("#nooformularyModal").show();
  						}
  						$rootScope.stopLoader();
  					}else if(response.data.status == 'error'){
  						$rootScope.stopLoader();
  						growl.warning(response.data.message,
  								{
  									title : 'Error!'
  								});
  					}
  				});
  				
  			}catch(e){
  				console.log("Exception-",e);
  			}
  		}
     	 
          $scope.removeTempServicesByIndex = function(index)
          {
        	  
        	  $scope.removeIdPrescriptionId = $scope.servicesListPopup[index].prescriptionDetailsId;
          	angular.forEach($scope.nonFormularyItemList, function (value, key) {
      			if($scope.servicesListPopup[index].prescriptionDetailsId!=undefined && $scope.servicesListPopup[index].prescriptionDetailsId!= null && $scope.servicesListPopup[index].prescriptionDetailsId==value.prescriptionDetailsid){
      					value.status = 'A';
      				}
      			});
          	
      		$scope.servicesListPopup.splice(index, 1);
      		
      		angular.forEach(self.model.pendingIssuesDetails.items, function (item1, subindex) {
      			if(item1.prescriptionDetailsId ==  $scope.removeIdPrescriptionId ){
      				self.model.pendingIssuesDetails.items.splice(subindex, 1);
      			}
      		});
      	}
          $scope.NonFormularyAdded = false;
          $scope.addTempServiesToMedicationMasterList = function(index)
          {
        	  $scope.NonFormularyAdded = true;
        	  angular.forEach($scope.servicesListPopup,function(b,j){
        			if($scope.autoPrescribedQty == true){
        				$scope.calculateReceivedQty1(b);
                		Calculate(b, self.model.pendingIssuesDetails);
                    }
        		  if(self.model.pendingIssuesDetails.items.length == 0){
        			  self.model.pendingIssuesDetails.items.push(b);
                  }else{
                 	 var found =  self.model.pendingIssuesDetails.items.find(ar=>ar.prescriptionDetailsId == b.prescriptionDetailsId);
						   if(found == undefined){
							   b.substituteBtn = true;
							   self.model.pendingIssuesDetails.items.push(b);
							   $scope.orderQtyTotalSum =  parseFloat($scope.orderQtyTotalSum) + parseFloat(b.qtyPrescribed);
						   }
                  }
        	  });
        	  
        	 console.log("final list",self.model.worklistDetails);
          	$("#nooformularyModal").hide();
      	}
          
		   	$scope.closePopupsj = function(){
		   		$("#nooformularyModal").hide();
		   	}
   	  $scope.deleteNonFormulary = function(){
			try{
				
				angular.forEach($scope.servicesListPopup,function(v,k){
					
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
						    updatedBy:$scope.userId,
							updatedDate:$scope.dt,
							prescriptionId:$scope.prescriptionId,
							organizationId:$scope.organizationId,
							justificationStop:"deleted from peding issue",
							prescriptionDetailsid:v.prescriptionDetailsId,
							
					};
					
					var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationDelete";
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
						
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							
						}
					});
				});
				
				
			}catch(e){
				console.log("Exception-",e);
			}
		}
   	  
   	 $scope.saveDataInPrescription = function(bill){
   		
   			var data = {
  				  "organizationId": $scope.organizationId,
  				  "unitId": $scope.unitId,
  				  "employeeId" : $scope.userId,
  				  "visitTypeId":self.model.visitTypeId,
  				  "doctorId": self.model.visitTypeId != 4 ? self.model.doctorId: 0, 
  				  "prescriptionDate":moment(new Date()).format('YYYY-MM-DD  HH:mm:ss'),
  				  "authorisedBy": self.model.doctorId,
  				  "authorisedDate":moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
  				  "addedBy":self.model.userId,
  				  "addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
  				  "admissionId":$scope.admissionId,
  				  "prescriptionId":$scope.savePriscritpionId ,
  				  "issueId":self.model.issueId,
  				  "patientId": $scope.idpatient,
  				  "priorityId":2,
  				  "deptId" : self.model.specialityId,
  				  "isFromOP" :true,
  				  "medicationType":1
  		    };
   		
   		    $scope.medicationDetailList = [];
   		
   			angular.forEach($scope.servicesListPopup,function(value, key) {
					var medicationDetailsObj = {};
					medicationDetailsObj.itemId = value.itemId;
					medicationDetailsObj.oldPrescriptionId = value.prescriptionDetailsId;
					medicationDetailsObj.storeId = $scope.storeId;
					medicationDetailsObj.uomUnitId = "";
					medicationDetailsObj.isMedicationStop = "N";
					medicationDetailsObj.orderQty =value.orderQty;
					medicationDetailsObj.priorityId =2;
					medicationDetailsObj.prescriptionDate =  moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
					medicationDetailsObj.authorisedDate =  moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
					medicationDetailsObj.startDate =  value.startDate+ " 00:00:00";
					medicationDetailsObj.stopDate =  value.stopDate+ " 00:00:00";
					medicationDetailsObj.authorisedBy =  $scope.userId;
					medicationDetailsObj.dose =  value.prescribedDose;
					var arr = value.details.split("|"); 
					medicationDetailsObj.itemName =  arr[1];
					medicationDetailsObj.genericName =  value.genericName;
					medicationDetailsObj.doseUnitId =  value.doseUnitId;
					medicationDetailsObj.frequencyId =  value.frequencyId;
					medicationDetailsObj.routeId =  value.roa_id;
					medicationDetailsObj.isNonFormulary = "N";
					medicationDetailsObj.statflag = false;
					medicationDetailsObj.duration = value.duration;
					medicationDetailsObj.durationUnit = value.durationUnit;
					medicationDetailsObj.instructionDesc = value.instructionDesc;
					medicationDetailsObj.isDischarge = false;
					medicationDetailsObj.prescribedDose = value.prescribedDose;
					medicationDetailsObj.prescribedDoseUnitId = value.prescribedDoseUnitId;
					medicationDetailsObj.factorId =value.factorId;
					medicationDetailsObj.instructionDesc =value.instructionDesc;
				$scope.medicationDetailList.push(medicationDetailsObj);
			});
   			if ($scope.medicationDetailList.length > 0) {
				data.prescriptionDetailsDtosList = $scope.medicationDetailList;
				console.log("medicationDetailList", data);
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/ip/pharmacology/validation/savePresWithouthPharmaValidationFromPendingIssue";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == 'success')
					{
						$scope.medicationObsdhb = [];
						$scope.medicationObsdhb = response.data.object.prescriptionDetailsDtosList;
						$rootScope.stopLoader();
						$scope.deleteNonFormulary();
						angular.forEach($scope.medicationObsdhb,function(b, key) {
						angular.forEach(bill.items,function(value, key) {
							if(value.oldPrescriptionId ==  b.oldPrescriptionId ){
								value.prescriptionDetailsId = b.prescriptionDetailsid;
								value.prescriptionDetailId = b.prescriptionDetailsid;
								value.id = b.issueDetailsId;
							}
						});
						});
						ActualSaveBill(bill);
						$scope.NonFormularyAdded = false;
					} else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						$scope.deleteNonFormulary();
						
					}
				});
				
			}
   	 }
      // chetana
      $scope.item={};
  	$scope.mod="";
  	$scope.labl="";
  	$scope.servicesListPopup =[];
      $scope.addServiceToTempServiceList = function($item, $model, $label,info,freeTextserviceName)
      {
    	  
    	  //check duplicate 
    	  var checekkdsk = false;
     	  angular.forEach($scope.servicesListPopup,function(b,j){
             var found =  $scope.servicesListPopup.find(ar=>ar.itemId == $item.id);
			    if(found != undefined){
			    	checekkdsk = true;
			    }
    	  });
    	  
     	  angular.forEach(self.model.pendingIssuesDetails.items,function(b,j){
              var found =  self.model.pendingIssuesDetails.items.find(ar=>ar.itemId == $item.id);
 			    if(found != undefined){
 			    	checekkdsk = true;
 			    }
     	  });
     	  
     	  if(checekkdsk == true){
     		 growl.error("Medication Already Exist");
     		 return false;
     	  }
    	$rootScope.startLoaderPopup();
      	$scope.prescriptionDetailsid = info.prescriptionDetailsid;
      	$scope.freeTextserviceName = freeTextserviceName;
    	$scope.item={};
  		$scope.mod="";
  		$scope.labl="";
  		var nrsdcnsdnc = {};
  		nrsdcnsdnc.assetCategoryId = $item.assetCategoryId;
  		nrsdcnsdnc.details = $item.details;
  		nrsdcnsdnc.searchItem =freeTextserviceName;
  		nrsdcnsdnc.formulationTypeId = $item.formulationTypeId;
  		nrsdcnsdnc.id = $item.id;
  		nrsdcnsdnc.isThreeUOMApplicable = $item.isThreeUOMApplicable;
  		nrsdcnsdnc.markUpDown = $item.markUpDown;
  		nrsdcnsdnc.markUpDownPer = $item.markUpDownPer;
  		nrsdcnsdnc.markupStatus = $item.markupStatus;
  		nrsdcnsdnc.patientClassPercentage = $item.patientClassPercentage;
  		nrsdcnsdnc.productCategoryId = $item.productCategoryId;
  		nrsdcnsdnc.prescriptionDetailsId = info.prescriptionDetailsid;
  		nrsdcnsdnc.oldPrescriptionId = info.prescriptionDetailsid;
  		nrsdcnsdnc.prescribedDose = info.prescribeDose;
  		nrsdcnsdnc.units = info.strengthDesc;
  		nrsdcnsdnc.duration = info.durationCount;
  		nrsdcnsdnc.durationUnit = info.durationUnit;
  		nrsdcnsdnc.frequencyDesc = info.searchItemFre;
  		nrsdcnsdnc.qtyPrescribed = info.quantity;
  		nrsdcnsdnc.orderQty = info.quantity;
  		nrsdcnsdnc.quantityOrdered = info.quantity;
  		nrsdcnsdnc.instructionDesc = info.instructionText;
  		nrsdcnsdnc.itemId = $item.id;
  		nrsdcnsdnc.roa_id = info.roa_id;
  		nrsdcnsdnc.frequencyId = info.frequencyId;
  		nrsdcnsdnc.startDate = info.startDate;
  		nrsdcnsdnc.stopDate = info.stopDate;
  		nrsdcnsdnc.onlySubButtonDisable =false;
  		nrsdcnsdnc.barcodeItem=false;
  		$scope.admissionId = self.model.pendingIssuesDetails.admissionId;
      	$timeout( function(){

        	$scope.itemSelected = {};
        	$scope.itemSelected = info;
            if (!!$item.id) {
                var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId + '/'+$scope.storeId+'/' + $item.id + '/' + $scope.entitlementObj.paymentEntitlementTypeId;
        
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
                        }
                    }, response.data);
        
                    $timeout( function(){
                    	nrsdcnsdnc.itemDisabled = false;
                    	nrsdcnsdnc.disabled = false;
                    	nrsdcnsdnc.isConsumed=false;		
                    	nrsdcnsdnc.itemId= response.data.itemId;
                    	nrsdcnsdnc.itemCode= response.data.itemCode;
                    	nrsdcnsdnc.markUpDown= response.data.markUpDown;
                    	nrsdcnsdnc.itemName= response.data.itemName;
                    	nrsdcnsdnc.markUpDownPer= response.data.markUpDownPer;
                    	nrsdcnsdnc.formulationType= response.data.formulationType;
                    	nrsdcnsdnc.stock= response.data.currentStock;
                    	nrsdcnsdnc.genericId= response.data.genericId;
                    	nrsdcnsdnc.genericName= response.data.genericName;
                    	nrsdcnsdnc.manufacturerId= response.data.manufacturerId;
                    	nrsdcnsdnc.manufacturerName= response.data.manufacturerName;
                    	nrsdcnsdnc.strength= response.data.strength;
                    	nrsdcnsdnc.batch= response.data.batch;
                    	nrsdcnsdnc.productCategoryId=response.data.productCategoryId;
                    	nrsdcnsdnc.assetCategoryId=response.data.assetCategoryId;
                    	nrsdcnsdnc.selectedBatch=response.data.batch[0];
                    	nrsdcnsdnc.uom=[{
                                uomDetails: "Select UOM",
                            }];
                        nrsdcnsdnc.taxId=response.data.taxId;
                        nrsdcnsdnc.tax= response.data.taxName;
                        nrsdcnsdnc.taxPercentage= response.data.taxPercent;
                        nrsdcnsdnc.isOTC= true;
                        nrsdcnsdnc.isShow= self.model.pendingIssuesDetails.items.length >=1 ? true: false;
                        nrsdcnsdnc.isFromAssignPicker = $scope.assignPickerFlag!=undefined ? $scope.assignPickerFlag:false;
                        nrsdcnsdnc.patientClassPercentage = $scope.patientClassPercentage;
                        nrsdcnsdnc.saleDetailPayeeMapperDtoList=[];
                        nrsdcnsdnc.SaleDetailPackageMapperList=[];
                        nrsdcnsdnc.storageUnitName= response.data.storageUnitName;
                      //  nrsdcnsdnc.onlySubButtonDisable=true;
                        nrsdcnsdnc.isThreeUOMApplicable=response.data.isThreeUOMApplicable;
        
                        nrsdcnsdnc.selectedBatch = commonDetailService.getDefaultBatchFromConfig(response.data.batch);
                        nrsdcnsdnc.finalOrderedQty=nrsdcnsdnc.qtyPrescribed;
                        if(nrsdcnsdnc.selectedBatch!=undefined)
                        {
                            //item.selectedUom = item.uom[0];
                        	nrsdcnsdnc.selectedUom = nrsdcnsdnc.selectedBatch.uom.find(function (obj) {
                                return obj.opUom;
                            });
                        }
                        else{
                        	nrsdcnsdnc.selectedUom = nrsdcnsdnc.uom[0];
                        }
                        nrsdcnsdnc.checkNewItem=true;
                        if(nrsdcnsdnc.isThreeUOMApplicable == false){
                        	nrsdcnsdnc.selectedBatch.uomListData = nrsdcnsdnc.selectedBatch.uom;
                        	nrsdcnsdnc.selectedBatch.uom = [];
                            angular.forEach(nrsdcnsdnc.selectedBatch.uomListData, function(d){
                                if(d.uomTypeId != 2){
                                	nrsdcnsdnc.selectedBatch.uom.push(d);
                                }
                            });
                        }
                        
                        if($scope.NonFormularyAdded == true){
                        	if($scope.itemSelected.prescriptionDetailsId != undefined && $scope.itemSelected.prescriptionDetailsId !=null){
                        		nrsdcnsdnc.prescriptionDetailsId = $scope.itemSelected.prescriptionDetailsId;
                        		nrsdcnsdnc.oldPrescriptionId = $scope.itemSelected.prescriptionDetailsId;
                        	}else{
                        		nrsdcnsdnc.prescriptionDetailsId = $scope.itemSelected.prescriptionDetailsid;
                        		nrsdcnsdnc.oldPrescriptionId = $scope.itemSelected.prescriptionDetailsid;
                        	}
                        	
                        	nrsdcnsdnc.prescribedDose = $scope.itemSelected.prescribedDose;
                        	nrsdcnsdnc.units = $scope.itemSelected.strengthDesc;
                        	nrsdcnsdnc.duration = $scope.itemSelected.duration!=undefined && $scope.itemSelected.duration!=null ? $scope.itemSelected.duration :info.durationCount;
                        	nrsdcnsdnc.durationUnit = $scope.itemSelected.durationUnit!=undefined && $scope.itemSelected.durationUnit!=null ? $scope.itemSelected.durationUnit : info.durationUnit ;
                        	nrsdcnsdnc.frequencyDesc = $scope.itemSelected.frequencyDesc!=undefined && $scope.itemSelected.frequencyDesc!=null ? $scope.itemSelected.frequencyDesc: info.searchItemFre ;
                        	nrsdcnsdnc.qtyPrescribed = $scope.itemSelected.qtyPrescribed;
                        	nrsdcnsdnc.instructionDesc = $scope.itemSelected.instructionDesc!=undefined && $scope.itemSelected.instructionDesc!=null ? $scope.itemSelected.instructionDesc : info.instructionText ;
                        }
                        
                        if(nrsdcnsdnc.selectedBatch!=undefined)
                    	{
                            
                            
                                var item = null;

                                //Assign Item and UOM
                                    if(nrsdcnsdnc.isThreeUOMApplicable == false){
                                        if(nrsdcnsdnc.selectedBatch.uom[2] == undefined || nrsdcnsdnc.selectedBatch.uom[2] == null){
                                        	nrsdcnsdnc.uomListData = nrsdcnsdnc.selectedBatch.uomListData;
                                        	nrsdcnsdnc.selectedBatch.uomListData = nrsdcnsdnc.selectedBatch.uomListData;
                                        }
                                        else{
                                        	nrsdcnsdnc.uomListData = nrsdcnsdnc.selectedBatch.uom;
                                        	nrsdcnsdnc.selectedBatch.uomListData = nrsdcnsdnc.selectedBatch.uom;
                                        }
                                        nrsdcnsdnc.uom = [];
                                        nrsdcnsdnc.selectedBatch.uom = [];
                                        angular.forEach(nrsdcnsdnc.selectedBatch.uomListData, function(u){
                                            if(u.uomTypeId != 2){
                                            	nrsdcnsdnc.selectedBatch.uom.push(u);
                                            	nrsdcnsdnc.uom.push(u);
                                            }
                                        })
                                    }
                                    else{
                                    	nrsdcnsdnc.uom = nrsdcnsdnc.selectedBatch.uom;
                                    }
                              

                                if (!!nrsdcnsdnc.selectedBatch.batchId) {
                                    //Select Default UOM
                                	nrsdcnsdnc.selectedUom = $filter('filter')(nrsdcnsdnc.uom, {
                                        opUom: true
                                    }, true)[0];

                                    //"Select UOM" option from UOM dropdown
                                    if (!nrsdcnsdnc.selectedUom) {
                                    	nrsdcnsdnc.selectedUom = nrsdcnsdnc.uom[0];
                                    }
                                    
                                    //Caclulate amount including discount
                                    Calculate(nrsdcnsdnc, self.model.pendingIssuesDetails);
                                    // CalculateAmount(item);

                                    if(nrsdcnsdnc.selectedBatch.availableStockQuantity != undefined || nrsdcnsdnc.selectedBatch.availableStockQuantity != null){
                                    	nrsdcnsdnc.availableStockQuantity = nrsdcnsdnc.selectedBatch.availableStockQuantity;
                                        //Convert Ordered Quantity
                                        if(nrsdcnsdnc.selectedUom!=undefined && nrsdcnsdnc.selectedUom!=null)
                                    	{
                                        ConvertQtyOrdered(nrsdcnsdnc, nrsdcnsdnc.selectedBatch.uom, nrsdcnsdnc.selectedUom);
                                    	}
                                        //Allocate quantity
                                        if(nrsdcnsdnc.quantityIssued !=undefined && nrsdcnsdnc.quantityIssued != 0 ){
                                            // AllocateQuantity(index, bill, parentIndex);
                                            self.model.ValidateQuantity(nrsdcnsdnc);
                                        }
                                        var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId + "/" + nrsdcnsdnc.itemId + "/" + nrsdcnsdnc.selectedBatch.batchId + "/" + nrsdcnsdnc.selectedUom.uomTypeId;
                                        $rootScope.startLoader();
                                        GenericService.serviceAction("GET", URI, {}).then(
                                            function (response) {
                                            	nrsdcnsdnc.stock= response.data.availableqty;
                                            //Ganesh
                                            	nrsdcnsdnc.quantityIssued = commonDetailService.getDefaultQuantity(nrsdcnsdnc,nrsdcnsdnc.quantityOrdered,response.data.availableqty);
                                                $scope.calculateReceivedQty(nrsdcnsdnc);
                                                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                                    $scope.isValidated = true;
                                                }
                                                else{
                                                    $scope.isValidated = false;
                                                }

                                                if($scope.activePackageList!=null&& $scope.activePackageList.length>0){
                                                    $scope.isValidated = true;
                                                }
                                                if(nrsdcnsdnc.quantity == 0 || nrsdcnsdnc.rate == undefined){
                                                	nrsdcnsdnc.rate = 0;
                                                }
                                                if($scope.atCop == true){
                                                	nrsdcnsdnc.rate = nrsdcnsdnc.selectedBatch.actualLendingPrice;
                                                    Calculate(nrsdcnsdnc, self.model.pendingIssuesDetails);
                                                }
                                                else{
                                                    if(nrsdcnsdnc.rate){
                                                    	nrsdcnsdnc.rate = nrsdcnsdnc.rate;
                                                    }
                                                    else{
                                                    	nrsdcnsdnc.rate = nrsdcnsdnc.selectedBatch.rate;
                                                        Calculate(nrsdcnsdnc, self.model.pendingIssuesDetails);
                                                    }
                                                }
                                                $rootScope.stopLoader();
                                            },
                                            function (err) {
                                                $rootScope.stopLoader();
                                            }
                                        );
                                    }
                                    else{
                                        //Get current stock for the selected batch
                                        var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId + "/" + nrsdcnsdnc.itemId + "/" + nrsdcnsdnc.selectedBatch.batchId + "/" + nrsdcnsdnc.selectedUom.uomTypeId;
                                        $rootScope.startLoader();
                                        GenericService.serviceAction("GET", URI, {}).then(
                                            function (response) {
                                            	nrsdcnsdnc.stock= response.data.availableqty;
                                            //Ganesh
                                            	nrsdcnsdnc.quantityIssued = commonDetailService.getDefaultQuantity(nrsdcnsdnc,nrsdcnsdnc.quantityOrdered,response.data.availableqty);
                                                $scope.calculateReceivedQty(nrsdcnsdnc);
                                                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                                    $scope.isValidated = true;
                                                }
                                                else{
                                                    $scope.isValidated = false;
                                                }
                                                $scope.activePackageListWithoutForceClose=[];
                                				angular.forEach($scope.activePackageList, function (item, key)  {
                                					if(item.isForceClose==1)
                                						{
                                						$scope.activePackageListWithoutForceClose.push(item);
                                						}
                                				});
                                                if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                                                    $scope.isValidated = true;
                                                }
                                                if(nrsdcnsdnc.quantity == 0 || nrsdcnsdnc.rate == undefined){
                                                	nrsdcnsdnc.rate = 0;
                                                }
                                                if($scope.atCop == true){
                                                	nrsdcnsdnc.rate = nrsdcnsdnc.selectedBatch.actualLendingPrice;
                                                    Calculate(nrsdcnsdnc, self.model.pendingIssuesDetails);
                                                }
                                                else{
                                                    if(nrsdcnsdnc.rate){
                                                    	nrsdcnsdnc.rate = nrsdcnsdnc.rate;
                                                    }
                                                    else{
                                                    	nrsdcnsdnc.rate = nrsdcnsdnc.selectedBatch.rate;
                                                        Calculate(nrsdcnsdnc, self.model.pendingIssuesDetails);
                                                    }
                                                }
                                                nrsdcnsdnc.selectedBatch.availableStockQuantity = response.data.availableqty;
                                                //Convert Ordered Quantity
                                                if(nrsdcnsdnc.selectedUom!=undefined && nrsdcnsdnc.selectedUom!=null)
                                            	{
                                                ConvertQtyOrdered(nrsdcnsdnc, nrsdcnsdnc.selectedBatch.uom, nrsdcnsdnc.selectedUom);
                                            	}
                                                //Allocate quantity
                                                if(nrsdcnsdnc.quantityIssued !=undefined && nrsdcnsdnc.quantityIssued != 0 ){
                                                    // AllocateQuantity(index, bill, parentIndex);
                                                    self.model.ValidateQuantity(nrsdcnsdnc);
                                                }
                                                $rootScope.stopLoader();
                                            },
                                            function (err) {
                                                $rootScope.stopLoader();
                                            }
                                        );
                                    }
                                } 
                                else{
                                	nrsdcnsdnc.selectedUom = nrsdcnsdnc.uom[0];
                                	nrsdcnsdnc.quantity = null;
                                	nrsdcnsdnc.rate = 0;
                                }
                            
                    	}
                        if(self.model.pendingIssuesDetails.items.length > 1){
                            $scope.flagRemove = true;
                        }
                        else{
                            $scope.flagRemove = false;
                        }
//                        nrsdcnsdnc.onlySubButtonDisable =true;
                        $scope.servicesListPopup.push(nrsdcnsdnc);
                        $rootScope.stopLoaderPopup();
                    },2000);
                    self.model.search = '';
                   
                    $rootScope.stopLoader();
                }, function (err) {
                    growl.error('Error while fetching the item details');
                    $rootScope.stopLoader();
                });
            }
        
        },2000);
      	
    	angular.forEach($scope.nonFormularyItemList, function (value, key) {
			if($scope.prescriptionDetailsid==value.prescriptionDetailsid){
					value.status = 'I';
				}
			});
  		       
  	}
      $scope.validateBatchAndUomFlag=false;
        var GetPendingIssuesDetails = function (issueId) {
        	self.model.issueId = issueId;
            var URI = CONSTANTS.PHARMACY.IP.PENDING_ISSUES.DETAIL_API  + $scope.unitId + '/'+$scope.organizationId+ '/'+ $cookies.getObject('cookieObject').storeId + '/' + issueId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.statusId = response.data.statusId;
                    self.model.remark = response.data.remark;
                    response.data.storeId = self.model.storeId;
                	$scope.getSponsorDetails($scope.admissionId);
                    self.model.pendingIssuesDetails = response.data;
                    $scope.idpatient = response.data.patientId;
                    $scope.encounterId = response.data.encounterId;
                    $scope.admissionId = response.data.admissionId;
                    $scope.getDischargeStatusId($scope.admissionId);
                    self.model.doctorId = response.data.doctorId;
                    self.model.visitTypeId = response.data.visitType;
                    self.model.specialityId = response.data.specialityId;
                    self.model.pendingIssuesDetails.discountCatgeory = self.model.discountCategoryList[0];
                    self.model.discountCategoryList = self.model.discountCategoryList.concat(response.data.discountCategory);
                    var finalOrderedQty=0;
                    $scope.orderListCount = 0;
                    self.model.pendingIssuesDetails.items.forEach(function (item, index) {
                    	item.originalBatchList=item.batch; 
                        finalOrderedQty =item.quantityOrdered;
                    	if(self.model.pendingIssuesDetails.statusId==4 || self.model.pendingIssuesDetails.statusId==8 )
                        {
                            item.statusId=4;
                    		item.status="Completed";
                        }
                    });
//                    $scope.FinalPrescribedQuantity=finalOrderedQty;
                    self.model.pendingIssuesDetails['discountType'] = '1';
                    if (self.model.pendingIssuesDetails.items) {
                    	$scope.orderQtyTotalSum=0;
                        self.model.pendingIssuesDetails.items.forEach(function (item, index) {
                            if(item.startDate && item.stopDate){
                                var startDate = moment(item.startDate).format('YYYY-MM-DD');
                                var stopDate = moment(item.stopDate).format('YYYY-MM-DD');
                                item.duration = moment(stopDate).diff(moment(startDate), 'days');
								if(item.durationUnit != undefined && item.durationUnit != null && item.durationUnit == 'Day'){
									item.duration = item.duration + 1;
								}
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
                            item.issuedQty = item.issuedQuantity;
                            if (item.status == 'Completed' ||  item.status == 'Partially Completed' || item.statusId == 9) {
                          	  item.itemDisabled = true;
                          	  item.disabled = true;
                            }else if(item.status == 'Pending'){
                            	item.itemDisabled = false;
                                item.disabled = false;
                                var priQty = 0;
                                if(item.selectedItems !=undefined){
                                    item.selectedItems.forEach(function (item2) {
                                        priQty += GetCalculateOrederQty(item2);
                                    });
                                }
//                                if(priQty>item.quantityOrdered || priQty>item.pickedQuantity)
//                         	   {
//                         	   priQty=0;
//                         	   }
                                //item.qtyPrescribed = item.qtyPrescribed - priQty
                                
                                if(item.isFromAssignPicker == true){
                                    item.pickedQuantity = item.pickedQuantity - priQty;
                                    item.quantityOrdered = item.quantityOrdered - priQty;
                          	 	}else{
                                    if(item.quantityOrdered < priQty){
                                        item.quantityOrdered = 0;
                                    }
                                    else{
                                        item.quantityOrdered = item.quantityOrdered - priQty;
                                    }
                          	 		if(item.quantityOrdered == 0){
                          	 			item.itemDisabled = true;
                          	            item.disabled = true;
                          	 		}
                          	 	}
                                $scope.saveFlag = false; 
                            }
                            else{
                                $scope.saveFlag = false; 
                            }
                            
                            if(self.model.pendingIssuesDetails.items.length==1 && self.model.pendingIssuesDetails.statusId == 2  ){
                                item.isShow = false;
                            }else{
                                item.isShow = true;
                            }
                            
                            if(item.disabled != true && item.statusId != 4 && item.statusId != 9){
                        		$scope.orderListCount++;
                        	}
                            
                            //Ganesh
                            item.isConsumed=false;
                            item.productCategoryId=item.productCategoryId;
                            item.itemId= item.itemId;
                            item.saleDetailPayeeMapperDtoList=[];
                            //item.itemtotalAmount = item.totalAmount;
                            if(item.totalAmount != undefined){
                        		item.itemtotalAmount = item.totalAmount;
                        		item.itemDiscount = item.discountAmount;
                        		item.discountPerItem = (100*(item.discountAmount/item.totalAmount)).toFixed(2);
                        	}else{
                                item.itemtotalAmount = 0;
                                item.itemDiscount = 0;
                                item.taxAmount= 0;
                                item.netAmount= 0;
                                item.roundOff=0;
                        	}
                            //item.itemDiscount = item.discountAmount;
                            angular.forEach(self.model.pendingIssuesDetails.items, function (item, key) {
                                item.batch.forEach(function(value){
                                	if(value.isSelected){
                                		item.availableStockQuantity = value.availableStockQuantity
                                	}
                                	// if (!!item.markUpDownPer) {
                                    //     var markUpDown = (value.sellingPrice * (item.markUpDownPer / 100));
    
                                    //     if (item.markUpDown == 1) {
                                    //         value.sellingPrice = value.sellingPrice + markUpDown;
                                    //     } else if (value.markUpDown == 0) {
                                    //         value.sellingPrice = value.sellingPrice - markUpDown;
                                    //     }
                                    // }
                                })                                
                            }, self.model.pendingIssuesDetails);      

                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }                  

                            item.finalOrderedQty = item.quantityOrdered;
                            
                            var isBatchFount = item.batch.find(function (x) {
                                return !!x.isSelected;
                            });

                            //added by minesh
                      	  	var clubData  = clubBatches(item.batch);
                            item.batch = clubData;
                            item.batchlistData = item.batch;
                            item.batch = [];
                            item.zeroStockBatchList=[];
                            angular.forEach(item.batchlistData, function(d){
                                if(d.availableStockQuantity != 0){
                                    item.batch.push(d);
                                }
                                if(d.availableStockQuantity == 0)
                                	{
                                	item.zeroStockBatchList.push(d);
                                	}
                            })
                            //added by minesh
                            
                            if (!!isBatchFount) {
                            	if(item.batch.length != 0 && item.disabled == false){
                            		item.selectedBatch = item.batch.find(function (x) {
                                        return !!x.isSelected;
                                    });
                            	}
                            	else{
                            		item.selectedBatch = item.zeroStockBatchList.find(function (x) {
                                        return !!x.isSelected;
                                    });
                            	}
                                
                                if(item.selectedBatch!=undefined && item.selectedBatch!=null)
                                	{
                                item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                    //  return obj.opUom;
                                   return obj.uomTypeId === item.selectedBatch.uomTypeId
                                });
                                item.uom = item.selectedBatch.uom;
                                item.quantityIssued = item.selectedBatch.issueQuantity;
                                // AllocateQuantity(index, self.model.pendingIssuesDetails);
                                ConvertQtyOrdered(item, item.uom, item.selectedUom);
                                	}
                                // CalculateRate(item, item.selectedUom, item.uom);
                                ValidateSaveButton();
                                if(item.itemtotalAmount!=0 && item.itemDiscount!=0){
                                	item.discountPerItem = ((item.itemDiscount/item.itemtotalAmount) * 100).toFixed(2);
                                }
                                //CalculateAmount(item);
                            } else {
                                item.batch.unshift({
                                    "batchDetails": "Select Batch"
                                });

                                item.uom = [{
                                    "uomDetails": "Select UOM"
                                }],

                                //item.selectedBatch = item.batch[0];
                                item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                                if(item.selectedBatch != undefined){
                                	if(item.selectedBatch.uom==undefined || item.selectedBatch.uom==null || item.selectedBatch.uom.length==0)
                                		{
                                		item.selectedBatch.uom=item.zeroStockBatchList[0].uom;
                                		}
                                    item.selectedUom = item.selectedBatch!=undefined ?item.selectedBatch.uom[0] :item.uom[0];
                                    BatchChange(index, self.model.pendingIssuesDetails, item.selectedBatch, null);
                                	
                                    item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                        ipUom: true
                                    }, true)[0];
                                    item.quantityIssued = commonDetailService.getDefaultQuantity(item,item.quantityOrdered,item.selectedBatch.availableStockQuantity);
	                           	    if(item.quantityIssued!=null && item.quantityIssued!=undefined)
	                        	    {
		                        	    // AllocateQuantity(index, self.model.pendingIssuesDetails);
	                           	    	$scope.calculateReceivedQty(item);
//                                        ValidateQuantity(item);
                                        Calculate(item, self.model.pendingIssuesDetails);
                                    }
                                }
                                else{
                            	  item.quantityIssued = 0;
                                }
                            }
                            angular.forEach(item.selectedItems, function (item1, index) {
                            	item1.issuedQty = item1.issuedQuantity;
                                if (item1.status == 'Completed' ||  item1.status == 'Partially Completed' ) {
                                    item1.itemDisabled = true;
                                }
                                item1.finalOrderedQty = item1.quantityOrdered;
                                  
                                //Ganesh
                                item1.substituteBtn=true;
                                item1.isConsumed=false;
                                //item1.netAmount=0;
                                item1.productCategoryId=item1.productCategoryId;
                                item1.itemId= item1.itemId;
                                item1.saleDetailPayeeMapperDtoList=[];
                                //item1.itemtotalAmount = item1.totalAmount;
                                //item1.itemDiscount = item1.discountAmount;
                                if(item1.totalAmount != undefined){
                                    item1.itemtotalAmount = item1.totalAmount;
                                    item1.itemDiscount = item1.discountAmount;
                                    item1.discountPerItem = (100*(item1.discountAmount/item1.totalAmount)).toFixed(2);
                              	}else{
                              		item1.itemtotalAmount = 0;
                              		item1.itemDiscount = 0;
                              		item1.taxAmount= 0;
                              		item1.netAmount= 0;
                              		item1.roundOff=0;
                              	}
                                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                    $scope.isValidated = true;
                                }                  

                                var isBatchFount = item1.batch.find(function (x) {
                                    return !!x.isSelected;
                                });

                                //added by minesh
                                var clubData  = clubBatches(item1.batch);
                                item1.batch = clubData;
                                //added by minesh
                                  
                                if (!!isBatchFount) {
                                    item1.selectedBatch = item1.batch.find(function (x) {
                                        return !!x.isSelected;
                                    });
                                    item1.selectedUom = item1.selectedBatch.uom.find(function (obj) {
                                    //  return obj.opUom;
                                        return obj.uomTypeId === item1.selectedBatch.uomTypeId
                                    });
                                    
                                    if(item1.selectedBatch!=undefined && item1.selectedBatch!=null)
               				     	 {
                                    	item1.taxId=item1.selectedBatch.taxId;
                                    	item1.taxPercentage=item1.selectedBatch.taxPercentage; 
               					    }
                                    
                                    item1.uom = item1.selectedBatch.uom;
                                    item1.quantityIssued = item1.selectedBatch.issueQuantity

                                    item1.availableStockQuantity =  item1.selectedBatch.availableStockQuantity
                                	  
                                    // AllocateQuantity(index, self.model.pendingIssuesDetails);
                                    if(item1.selectedUom!=undefined && item1.selectedUom!=null)
                                	{
                                    ConvertQtyOrdered(item1, item1.uom, item1.selectedUom);
                                	}
                                    // CalculateRate(item1, item1.selectedUom, item1.uom);
                                    Calculate(item1,self.model.pendingIssuesDetails);
                                    ValidateSaveButton();
                                    //CalculateAmount(item);
                                } else {
                                    item1.batch.unshift({
                                        "batchDetails": "Select Batch"
                                    });

                                    item1.uom = [{
                                        "uomDetails": "Select UOM"
                                    }],

                                    item1.selectedBatch = item1.batch[0];
                                    item1.selectedUom = item1.uom[0];
                                    //item1.quantityIssued = 0;
                                    if(self.model.pendingIssuesDetails.statusId ==undefined && self.model.pendingIssuesDetails.statusId ==null && self.model.pendingIssuesDetails.statusId ==2){
                                        ConvertQtyOrdered(item1, item1.uom, item1.selectedUom);
                                    }else{
                                        var found =  item1.batch.find(function (x) {
                                            return x.isSelected;
                                        });
                                        var	parentItemQty = GetCalculateOrederQty(item);
                                       	if(found == undefined){
                                       		if(item1.batch[0].uom.length>0){
                                       			uomConversion(item1, item1.batch[0],3,parentItemQty);
                                       		}	
                                       	}else{
                                       		var selectedUomTypeId =  found.uomTypeId;
                                       		uomConversion(item1, found,selectedUomTypeId,parentItemQty);
                                       	}       
                                    }
                                    Calculate(item1,self.model.pendingIssuesDetails);
                                }
                            });
                        });
                       
                        var tempList = [];
                        if(self.model.pendingIssuesDetails.statusId == 18){
                            self.model.pendingIssuesDetails.items.forEach(function (item) {
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
                                    self.model.pendingIssuesDetails.items.forEach(function (item1) {
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
                                        newArr.itemName= item.itemName,
                                        newArr.itemId = item.itemId,
                                        newArr.isNarcotic= item.isNarcotic,
                                        newArr.productCategoryId = item.productCategoryId,
                                        newArr.genericName = item.genericName,
                                        newArr.genericId = item.genericId,
                                        newArr.manufacturerName = item.manufacturerName,
                                        newArr.quantityOrdered = newOrderQty,
                                        newArr.stockAvailable = 0,
                                        newArr.quantityIssued = 0,
                                        newArr.formulationTypeId= item.formulationTypeId,
                                        newArr.patientClassPercentage = item.patientClassPercentage,
		                                //newArr.patientClassAmount = item.patientClassAmount,
                                        newArr.storeId = item.storeId,
                                        newArr.uom =  item.uom,
                                        newArr.selectedUom = null,
                                        newArr.priorityId = item.priorityId,
                                        newArr.markUpDown = item.markUpDown,
                                        newArr.markUpDownPer = item.markUpDownPer,
                                        newArr.markupStatus = item.markupStatus,
                                        newArr.taxId = item.taxId,
                                        newArr.pickedQuantity = assignQty,
                                        newArr.tax = item.tax != undefined ? item.tax : 0.0,
                                        newArr.taxPercentage = item.taxPercentage,
                                        newArr.rate = item.rate,
                                        newArr.saleDetailPayeeMapperDtoList = [],
                                        newArr.issuedQuantity = 0,
                                        newArr.prescriptionDetailId = item.prescriptionDetailId,
                                        newArr.isShow = true,
                                        newArr.dose  = item.dose,
                                        newArr.storageUnitName = item.storageUnitName,
                                        newArr.doseUnit  = item.doseUnit,
                                        newArr.interventionFlag  = item.interventionFlag,
                                        newArr.issuedQty = item.issuedQuantity,
                                        newArr.parentDetailId = item.id,
                                        // newArr.parentIssueDetailsId = item.id,
                                        newArr.finalOrderedQty = newOrderQty;
                                        newArr.isFromAssignPicker = item.isFromAssignPicker;
                                        newArr.instructionDesc = item.instructionDesc;
                                        newArr.duration = item.duration;
                                        newArr.frequencyDesc = item.frequencyDesc;
                                        newArr.itemDisabled = false;
                                        newArr.disabled = false;
                                        newArr.isThreeUOMApplicable = item.isThreeUOMApplicable;
                                        if(item.batch.length == 0 && item.disabled == true){
                                        	newArr.batchdata = item.zeroStockBatchList;
                                        }
                                        else{
                                        	newArr.batchdata = item.batch;
                                        }
                                        newArr.batch = [];
                                        newArr.zeroStockBatchList=[];
                                        angular.forEach(newArr.batchdata, function(d){
                                            if(d.availableStockQuantity != 0){
                                                newArr.batch.push(d);
                                            }
                                            if(d.availableStockQuantity == 0)
                                        	{
                                        	newArr.zeroStockBatchList.push(d);
                                        	}
                                        })
                                        if(newArr.batch.length != 0 && newArr.disabled == false){
                                        	newArr.selectedBatch = commonDetailService.getDefaultBatchFromConfig(newArr.batch);
                                        }
                                        else{
                                        	newArr.selectedBatch = commonDetailService.getDefaultBatchFromConfig(newArr.zeroStockBatchList);
                                        }
                                        if(item.selectedBatch!=undefined && item.selectedBatch!=null && newArr.selectedBatch) {
                                            newArr.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                                ipUom: true
                                            }, true)[0];
                                            if(newArr.selectedUom!=undefined){
                                                ConvertQtyOrdered(newArr,newArr.uom,newArr.selectedUom);
                                            }
                                        }
                                        tempList.push(newArr);		    		                  			   
                                    }
                                }
                            }		
                        });
                        if(tempList.length >0){		 
                            tempList.forEach(function (tempItem) {
                                $scope.saveFlag = false;
                                if(tempItem.isThreeUOMApplicable == false){
                                    if(tempItem.selectedBatch == null || tempItem.selectedBatch == undefined){
                                        tempItem.uomListData = tempItem.uom;
                                        tempItem.uom = [];
                                        angular.forEach(tempItem.uomListData, function(u){
                                            if(u.uomTypeId != 2){
                                                tempItem.uom.push(u);
                                            }
                                        })
                                    }
                                    else{
                                        tempItem.selectedBatch.uomListData = tempItem.selectedBatch.uom;
                                        tempItem.selectedBatch.uom = [];
                                        angular.forEach(tempItem.selectedBatch.uomListData, function(u){
                                            if(u.uomTypeId != 2){
                                                tempItem.selectedBatch.uom.push(u);
                                            }
                                        })
                                    }
                                }
                                self.model.pendingIssuesDetails.items.push(tempItem);				
                            })
                            if(tempList.length==1){
                                self.model.pendingIssuesDetails.items[0].isShow = false;
                            }
                            self.model.pendingIssuesDetails.items.forEach(function (item) {
                            	var rate = 0;
                                if((item.selectedBatch!=undefined && item.selectedBatch!=null )&& (item.selectedBatch.uom==undefined || item.selectedBatch.uom==null || item.selectedBatch.uom.length==0))
                                {
                                	var counter =0;
                            		item.batch.forEach(function (batchData) {
                            			if(counter==0)
                            				{
                            			if(batchData.uom!=undefined && batchData.uom!=null && batchData.uom.length!=0 )
                            				{
                            				counter++;
                            				item.selectedBatch.uom=batchData.uom;
                            				}
                            				}
                            		});
                                }
                            	if(item.selectedBatch!=undefined && item.selectedBatch!=null)
                            		{
                            		if(item.selectedUom==undefined || item.selectedUom==null)
                            		{
                            			item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                            ipUom: true
                                        }, true)[0];
                            		}
                            		rate = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, item.selectedBatch.rate);
                            		}
                            	else
                            		{
                            		rate=item.selectedBatch!=undefined && item.selectedBatch!=null ? item.selectedBatch.rate :item.rate;
                            		}
                            	if((item.newQty == undefined || item.newQty == null || item.newQty == "") && item.disabled==false ){
                            		if($scope.autoPrescribedQty == true){
                            			if(item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.availableStockQuantity<=item.quantityOrdered)
                            				{
                            				item.quantityIssued=item.selectedBatch.availableStockQuantity;
                            				}
                            			else
                            				{
                            				
                            				if(item.issuedQuantity!=undefined && item.issuedQuantity!=null && item.issuedQuantity>0)
											{
												item.quantityIssued = item.quantityOrdered-(item.issuedQuantity!=undefined && item.issuedQuantity!=null ? item.issuedQuantity:0);
											}
                            				
                            			//	item.quantityIssued = item.quantityOrdered-(item.issuedQuantity!=undefined && item.issuedQuantity!=null ? item.issuedQuantity:0);
                            				}
                                    }
                            	}
                            	if($scope.autoPrescribedQty == true){
                            	if((item.quantityIssued==undefined || item.quantityIssued==null || item.quantityIssued==0) && (item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.availableStockQuantity>0) )
                            		{
                            		if(item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.availableStockQuantity<=item.newQty)
                    				{
                    				item.quantityIssued=item.selectedBatch.availableStockQuantity;
                    				}
                    			else
                    				{
                            		item.quantityIssued=(item.newQty!=undefined && item.newQty!=null && item.newQty>0 ? item.newQty:0)
                    				}
                            		}
                            	}
                                var data = {
                                    "mrp": rate,
                                    "isTaxInc": $scope.taxInclusive,
                                    "markup": item.markUpDown,
                                    "markUpDownPer" : item.markUpDownPer,
                                    "discount": item.discountPerItem ? item.discountPerItem : 0,
                                    "isDiscPercent": !!self.model.pendingIssuesDetails.discountType && self.model.pendingIssuesDetails.discountType == 1 ? true : false,
                                    "tax": item.taxPercentage,
                                    "qty": (item.quantityIssued!=undefined && item.quantityIssued!=null && item.quantityIssued>0 ? item.quantityIssued : (item.issuedQuantity!=undefined && item.issuedQuantity!=null && item.issuedQuantity>0 ? item.issuedQuantity:0)),
                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                    "patientClassPercentage": item.patientClassPercentage,
                                }
                                var response = commonDetailService.calculateAmount(data);
                                if(self.model.statusId == 18 && item.disabled == true){
                                	item.rate = item.rate;
                                }
                                else{
                                	 item.rate = response.rate;
                                	 item.itemtotalAmount = response.totalAmount;
                                     item.netAmount = response.netAmount;
                                     item.taxAmount = response.taxAmount;
                                }
                                if(item.selectedBatch!=undefined && item.selectedBatch!=null)
           				     	 {
                           	      item.taxId=item.selectedBatch.taxId;
                                  item.taxPercentage=item.selectedBatch.taxPercentage; 
           					    }
                                item.responseTaxAmount =  response.taxAmount;
                                item.unitDiscountAmount = response.unitDiscountAmount;
                                item.itemDiscount =  response.discountAmount;
//                                item.selfPayable = response.netAmount;
//                                item.creditPayable = response.creditPayable;
                                item.packageConsumptionAmt = item.packageConsumptionAmt;
                                item.patientClassAmount = response.patientClassAmount;
                                $scope.patientClassAmount = response.patientClassAmount;
                                item.unitPatientClassAmount = response.unitPatientClassAmount;
                                if(item.issueDetailsId!=undefined && item.issueDetailsId!=null && item.issueDetailsId>0 )
								{
									item.batch=item.originalBatchList;
								}
                                if(item.itemtotalAmount == undefined || item.itemtotalAmount == null || item.itemtotalAmount == 0){
                                	if(item.newQty == undefined || item.newQty == null || item.newQty == ""){
                                		if($scope.autoPrescribedQty == true){
                                			if(item.selectedBatch!=undefined && item.selectedBatch!=null)
                            				{
        									item.quantityIssued = item.quantityOrdered;
                            				}
                                        }
                                		$scope.calculateReceivedQty(item);
                                		CalculateAmount(item);
                                	}
                                	if($scope.autoPrescribedQty == false){
                                		CalculateAmount(item);
                                	}
                                	CalculateAmount(item);
                                }
                            });
                        }
                    }
                    if(self.model.pendingIssuesDetails.statusId == 2){
                        $scope.saveFlag = false; 
                    }
                    if(self.model.pendingIssuesDetails.statusId == 4 || self.model.pendingIssuesDetails.statusId==8 ){
                    	angular.forEach(self.model.pendingIssuesDetails.items, function(item){
                    		if(item.statusId==4)
                    			{
                    			item.quantityIssued=0;
                    			}
                    	if(item.issueDetailsId!=undefined && item.issueDetailsId!=null && item.issueDetailsId>0 )
    					{
    						item.batch=item.originalBatchList;
    					} 
                    	});
                    }
                }
                angular.forEach(self.model.pendingIssuesDetails.items, function(val1){
                    if(!val1.itemDisabled && !val1.disabled){
                        $scope.orderQtyTotalSum = $scope.orderQtyTotalSum + val1.quantityOrdered;	
                    }
                });
                angular.forEach(self.model.pendingIssuesDetails.items, function(item){
                    if(!item.itemDisabled && !item.disabled){
                        if(self.model.pendingIssuesDetails.statusId!=4 && self.model.pendingIssuesDetails.statusId!=8)
                        {
                            const result = item.batch.filter(word => word.availableStockQuantity > 0);
                            item.batch=result;
                            angular.forEach(item.selectedItems, function(select){
                                if(!select.itemDisabled && !select.disabled){
                                    const result1 = select.batch.filter(word => word.availableStockQuantity > 0);
                                    select.batch=result1;
                                }
                            })
                        }
                        else
                        {
                            const result = item.batch.filter(word => word.availableStockQuantity > 0);
                            item.batch=result;
                            angular.forEach(item.selectedItems, function(select){
                                if(!select.itemDisabled && !select.disabled){
                                    const result1 = select.batch.filter(word => word.availableStockQuantity > 0);
                                    select.batch=result1;
                                }
                            })
                        }
                    }
                    if(item.selectedBatch==undefined || item.selectedBatch==null)
                    	{
                    	item.selectedBatch = item.batch.find(function (x) {
                            return !!x.isSelected;
                        });
                    	if(item.selectedBatch!=undefined && item.selectedBatch!=null)
	   				 	 {
	                   	     item.taxId=item.selectedBatch.taxId;
	                          item.taxPercentage=item.selectedBatch.taxPercentage; 
	   					 }
                    	}
                    if($scope.autoBatch==true && item.selectedBatch==undefined){
                    	item.batchSelected = item.batch;
                    	item.batch = [];
                    	angular.forEach(item.batchSelected, function(b){
                    		if(b.availableStockQuantity != 0){
                    			item.batch.push(b);
                    		}
                    	})
                        item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                    	
                    	if(item.selectedBatch!=undefined && item.selectedBatch!=null)
	   				 	 {
	                   	   item.taxId=item.selectedBatch.taxId;
	                          item.taxPercentage=item.selectedBatch.taxPercentage; 
	   					 }
                    }
                    if(item.issueDetailsId!=undefined && item.issueDetailsId!=null && item.issueDetailsId>0 ){
                        if(item.selectedBatch) {
                        	if(item.selectedUom==undefined || item.selectedUom==null)
                        		{
                            item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                            	uomTypeId: item.selectedBatch.uomTypeId
                            }, true)[0];
                            if(item.selectedUom!=undefined){
                                ConvertQtyOrdered(item,(item.uom!=undefined && item.uom!=null ? item.uom :item.selectedBatch.uom),item.selectedUom);
                            }
                        		}
                        }
                    }
                    if(!item.itemDisabled && !item.disabled){
                        if(item.selectedBatch) {
                            item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                ipUom: true
                            }, true)[0];
                            if(item.selectedUom!=undefined){
                                ConvertQtyOrdered(item,(item.uom!=undefined && item.uom!=null ? item.uom :item.selectedBatch.uom),item.selectedUom);
                            }
                        }
                    }
                    if(item.isThreeUOMApplicable == false){
                        if(item.selectedBatch != undefined && item.selectedBatch != null){
                            if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null){
                                item.selectedBatch.uomListData = item.selectedBatch.uom;
                                item.selectedBatch.uom = [];
                                angular.forEach(item.selectedBatch.uomListData, function(u){
                                    if(u.uomTypeId != 2){
                                        item.selectedBatch.uom.push(u);
                                    }
                                });
                            } 
                        }
                    }
                });
                self.model.pendingIssuesDetails.items.forEach(function (item, index) {
                	if(self.model.statusId == 18){
                		BatchChange(index, self.model.pendingIssuesDetails, item.selectedBatch, null);
                		if($scope.autoPrescribedQty == true){
                			if($scope.autoBatch == true && item.stockAvailable > item.newQty){
                				console.log( "item.itemtotalAmount1",item.itemtotalAmount);
                				if(item.disabled==true)
								{
									item.quantityIssued = 0;
								}
                				else{
                					item.quantityIssued = item.quantityOrdered;
                				}
                				$scope.calculateReceivedQty(item);
                				if(item.disabled==false)
                					{
                					CalculateAmount(item);
                					}
                				console.log( "item.itemtotalAmount2",item.itemtotalAmount);
        					}
        					else{
        						if(item.disabled==true)
								{
									item.quantityIssued = 0;
								}
								else{
									item.quantityIssued = item.stockAvailable;
								}
        						
        						$scope.calculateReceivedQty1(item);
								CalculateAmount(item);
        					}
                		}
                		else{
                			if(item.disabled == true || item.statusId == 4)
                    		{
                        		item.quantityIssued=0;
                    		}
                		}
                	}
                	if(item.statusId==4)
            		{
                		item.batch=item.originalBatchList;
                		item.quantityIssued=0;
            		}
                	if($scope.autoPrescribedQty == false){
                		if(item.disabled == true){
                			item.quantityIssued = 0;
                		}
                	}
                	if(item.selectedItems != undefined){
                		item.selectedItems.forEach(function (sub, index) {
                    		if(sub.statusId==4)
                    		{
                        		sub.quantityIssued=0;
                    		}
                    	});
                	}
                	if(item.quantityIssued>=item.newQty)
                		{
                		item.quantityIssuedFlag=true;
                		}
                	if(item.quantityIssued>item.stockAvailable)
                		{
                		item.quantityIssuedFlag=false;
                		}
                	console.log( "item.itemtotalAmount3",item.itemtotalAmount);
                	if($scope.validateBatchAndUomFlag==false)
                		{
                		ValidateUniqueBatchAndUom(item);
                		}
                	console.log( "item.itemtotalAmount4",item.itemtotalAmount);
                });
                $rootScope.stopLoader();
               console.log( "final list",self.model.pendingIssuesDetails.items);
            });
            
        }
        
        $scope.getDischargeStatusId = function (admissionId) {
        	var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+ admissionId;
    		BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
    			if(response.data.status == "success"){
    				if(response.data.object.dischargeStatusId == 1){
    					growl.warning("Patient discharge is marked...!!",{
    						  title : WARNING_MSG
    				    });
    				}
    			}
    		});
        }

        $scope.getDetailsEntitlement = function () {
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }  
        }
       
        var BatchChange = function (index, bill, batch, parentIndex) {
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
                	if(batch != undefined && batch.uom != undefined ){
                		 bill.items[index].uom = batch.uom;
                         item = bill.items[index];
                	}
                }
            } 
            else {
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

            if (batch != undefined && !!batch.batchId) {
                //Select Default UOM
            	 if (!item.selectedUom) {
                   item.selectedUom = $filter('filter')(item.uom, {
                    opUom: true
                   }, true)[0];
            	  }

                //"Select UOM" option from UOM dropdown
                if (!item.selectedUom) {
                    item.selectedUom = item.uom[0];
                }
                
                //Caclulate amount including discount
                item.taxId=item.selectedBatch.taxId;
                item.taxPercentage=item.selectedBatch.taxPercentage;
                Calculate(item, bill);
                // CalculateAmount(item);

                if(item.selectedBatch.availableStockQuantity != undefined || item.selectedBatch.availableStockQuantity != null){
                    item.availableStockQuantity = item.selectedBatch.availableStockQuantity;
                    //Convert Ordered Quantity
                    if(item.selectedUom!=undefined && item.selectedUom!=null)
                	{
                    ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                	}
                    //Allocate quantity
//                    if(item.quantityIssued !=undefined && item.quantityIssued != null ){
                        // AllocateQuantity(index, bill, parentIndex);
//                        self.model.ValidateQuantity(item);
//                    }
                    if(isNaN(item.newQty) || item.newQty==undefined || item.newQty==null)
                    	{
                    	item.newQty="";
                    	}
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                        function (response) {
                        	
                            //Select Default UOM
                       	 if (!item.selectedUom) {
                              item.selectedUom = $filter('filter')(item.uom, {
                               opUom: true
                              }, true)[0];
                       	  }

                           //"Select UOM" option from UOM dropdown
                           if (!item.selectedUom) {
                               item.selectedUom = item.uom[0];
                           }
                           
                           
                            item.stock= response.data.availableqty;
                        //Ganesh
                            if(item.statusId!=4)
                            	{
                            	if(item.issuedQuantity == undefined || item.issuedQuantity == null  ||item.issuedQuantity == 0){
                            	item.quantityIssued = commonDetailService.getDefaultQuantity(item,item.finalOrderedQty,response.data.availableqty!=undefined && response.data.availableqty!=null ? response.data.availableqty : item.selectedBatch.availableStockQuantity);
                            	}
                            	}
                            else
                            	{
                            	item.quantityIssued = 0;
                            	}
                            $scope.calculateReceivedQty(item);
                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
                            $scope.activePackageListWithoutForceClose=[];
            				angular.forEach($scope.activePackageList, function (item, key)  {
            					if(item.isForceClose==1)
            						{
            						$scope.activePackageListWithoutForceClose.push(item);
            						}
            				});
                            if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                                $scope.isValidated = true;
                            }
                            if(item.quantity == 0 || item.rate == undefined){
                                item.rate = 0;
                            }
                            if($scope.atCop == true){
                                item.rate = item.selectedBatch.actualLendingPrice;
                                Calculate(item, bill);
                            }
                            else{
                                if(item.rate){
                                    item.rate = item.rate;
                                }
                                else{
                                    item.rate = item.selectedBatch.rate;
                                    Calculate(item, self.model.pendingIssuesDetails);
                                }
                            }
                            if($scope.autoBatch==true)
                			{ 
                				Calculate(item, bill);
                			}
                            if(item.quantityIssued>=item.newQty)
                    		{
                            	item.quantityIssuedFlag=true;
                    		}
                            if(item.quantityIssued>item.stockAvailable)
                    		{
                            	item.quantityIssuedFlag=false;
                    		}
                            if(item.newQty>=item.stockAvailable)
                    		{
                            	item.quantityIssuedFlag=false;
                    		}
                            $rootScope.stopLoader();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                        }
                    );
                }
                else{
                    //Get current stock for the selected batch
                    var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId + "/" + item.itemId + "/" + item.selectedBatch.batchId + "/" + item.selectedUom.uomTypeId;
                    $rootScope.startLoader();
                    GenericService.serviceAction("GET", URI, {}).then(
                        function (response) {
                            item.stock= response.data.availableqty;
                        //Ganesh
                            item.quantityIssued = commonDetailService.getDefaultQuantity(item,item.quantityOrdered,response.data.availableqty);
                            $scope.calculateReceivedQty(item);
                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
                            $scope.activePackageListWithoutForceClose=[];
            				angular.forEach($scope.activePackageList, function (item, key)  {
            					if(item.isForceClose==1)
            						{
            						$scope.activePackageListWithoutForceClose.push(item);
            						}
            				});
                            if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                                $scope.isValidated = true;
                            }
                            if(item.quantity == 0 || item.rate == undefined){
                                item.rate = 0;
                            }
                            if($scope.atCop == true){
                                item.rate = item.selectedBatch.actualLendingPrice;
                                Calculate(item, bill);
                            }
                            else{
                                if(item.rate){
                                    item.rate = item.rate;
                                }
                                else{
                                    item.rate = item.selectedBatch.rate;
                                    Calculate(item, self.model.pendingIssuesDetails);
                                }
                            }
                            item.selectedBatch.availableStockQuantity = response.data.availableqty;
                            //Convert Ordered Quantity
                            if(item.selectedUom!=undefined && item.selectedUom!=null)
                        	{
                            ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                        	}
                            //Allocate quantity
                            if(item.quantityIssued !=undefined && item.quantityIssued != 0 ){
                                // AllocateQuantity(index, bill, parentIndex);
                                self.model.ValidateQuantity(item);
                            }
                            if($scope.autoBatch==true)
                			{ 
                				Calculate(item, bill);
                			}
                            if(item.quantityIssued>=item.newQty)
                    		{
                            	item.quantityIssuedFlag=true;
                    		}
                            if(item.quantityIssued>=item.stockAvailable)
                    		{
                            	item.quantityIssuedFlag=false;
                    		}
                            if(item.newQty>=item.stockAvailable)
                    		{
                            	item.quantityIssuedFlag=false;
                    		}
                            $rootScope.stopLoader();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                        }
                    );
                }
            } 
            else{
            	if(item!= undefined && item!= null){
            		 item.selectedUom = item.uom[0];
                     item.quantity = null;
                     item.rate = 0;
            	}
               
            }
        }

        var ConvertQtyOrdered = function (item, uom, uomList) {
            var newUOM = [];
            item.totalStock=0;
            angular.forEach(item.batch, function (batch, key) {
          	  item.totalStock+=batch.availableStockQuantity!=undefined && batch.availableStockQuantity!=null ? batch.availableStockQuantity:0;
            })
            if(item.isThreeUOMApplicable == false){
                if(item.uomListData == undefined || item.uomListData == null || item.uomListData.length == 0){
                    item.uomListData = item.uom!=undefined && item.uom!=null && item.uom.length!=0 ? item.uom : uom;
                }
                if(item.uomListData[2] == undefined){
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
            	if(uom!=undefined && uom!=null)
            		{
                if(uom[1] == undefined || uom[1] == null){
                	if(item.uoms != undefined && item.uoms != null && item.uoms.length != 0){
                		item.uoms.find(function (val){
                            if(!!val.uomTypeId){
                                newUOM.push(val);
                            }
                        })
                	}
                }
                else{
                    if(uom[2] == undefined || uom[2] == null){
                        uomListData.find(function (value) {
                            if (!!value.uomTypeId) {
                                newUOM.push(value);
                            }
                        });
                    }
                    else{
                        uom.find(function (value) {
                            if (!!value.uomTypeId) {
                                newUOM.push(value);
                            }
                        });
                    }
                }
            		}
            }
            if(item.quantityOrdered == undefined || item.quantityOrdered == null || item.quantityOrdered == ""){
                item.quantityOrdered = 0;
            }
            var quantityOrdered = item.finalOrderedQty;
            var pickedQty = item.pickedQuantity!=undefined ?item.pickedQuantity : 0;
            // item.quantityIssued = 0;
            if(newUOM!=undefined && newUOM!=null && newUOM.length!=0)
        	{
            if( uomList != null){
                var uomDetails = uomList.uomDetails.split(' ')[0];
                if(item.isThreeUOMApplicable == true)
                {
                if (uomDetails == "P") {
                    item.totalStock = Math.floor(item.totalStock / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                } else if (uomDetails == "S") {
                    item.totalStock = Math.floor(item.totalStock / (newUOM[1].conversion * newUOM[2].conversion));
                } else if (uomDetails == "D") {
                    item.totalStock = Math.floor(item.totalStock / (newUOM[2].conversion));
                }
                }
                else
                	{
                	if (uomDetails == "P") {
                        item.totalStock = Math.floor(item.totalStock / (newUOM[0].conversion * newUOM[1].conversion));
                    } else if (uomDetails == "D") {
                        item.totalStock = Math.floor(item.totalStock / (newUOM[1].conversion));
                    }
                	}
            }
        		}
            if(newUOM!=undefined && newUOM!=null && newUOM.length!=0)
        	{
            if( uomList != null){
                var uomDetails = uomList.uomDetails.split(' ')[0];
                if (uomDetails == "P") {
                    item.newQty = Math.ceil(quantityOrdered / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                    item.newPickedQty = Math.ceil(pickedQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                    //  item.issuedQuantity = Math.ceil(item.issuedQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                    item.stockAvailable = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                } else if (uomDetails == "S") {
                    item.newQty = Math.ceil(quantityOrdered / (newUOM[1].conversion * newUOM[2].conversion));
                    item.newPickedQty = Math.ceil(pickedQty / ( newUOM[1].conversion * newUOM[2].conversion));
                    //item.issuedQuantity = Math.ceil(item.issuedQuantity / (newUOM[1].conversion * newUOM[2].conversion));
                    item.stockAvailable = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
                } else if (uomDetails == "D") {
                    item.newQty = Math.ceil(quantityOrdered / (newUOM[2].conversion));
                    item.newPickedQty = Math.ceil(pickedQty / ( newUOM[2].conversion));
                    //item.issuedQuantity = Math.ceil(item.issuedQuantity / (newUOM[2].conversion));
                    item.stockAvailable = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
                }
            }
            if(item.checkNewItem==true)
            {
            	if( uomList != null){
                    var uomDetails = uomList.uomDetails.split(' ')[0];
                    if (uomDetails == "P") {
                        item.stockAvailable = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                    } else if (uomDetails == "S") {
                        item.stockAvailable = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
                    } else if (uomDetails == "D") {
                        item.stockAvailable = Math.floor(item.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
                    }
                }
            }
        		}

            if(item.quantityIssued == undefined || item.quantityIssued == null || item.quantityIssued == ""){
                item.quantityIssued = 0;
            }
            
            if($scope.autoPrescribedQty == true && item.statusId != 4 && item.itemDisabled != true && item.disabled != true){
            	if(isNaN(item.newQty) || item.newQty==undefined || item.newQty==null)
            		{
            		item.newQty=0;
            		}
            	if($scope.autoBatch == true && item.stockAvailable > item.newQty){
					item.quantityIssued = item.newQty;
				}
				else{
					item.quantityIssued = item.stockAvailable;
				}
            }
            
            if(newUOM!=undefined && newUOM!=null && newUOM.length!=0)
            	{
            if(item.isThreeUOMApplicable == false){
                if(item.statusId == 18 || item.statusId == 4){
                    if(item.selectedUom.uomTypeId == 1){
                        item.receivedQtyCon = (item.issuedQuantity +"*"+ newUOM[0].conversion);
                        item.receivedQtyCalculation = (item.issuedQuantity * newUOM[0].conversion);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){

                    }
                    else{
                        item.receivedQtyCon = (item.issuedQuantity);
                        item.receivedQtyCalculation = (item.issuedQuantity);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                }
                else{
                    if(item.selectedUom.uomTypeId == 1){
                        item.receivedQtyCon = (item.quantityIssued +"*"+ newUOM[0].conversion);
                        item.receivedQtyCalculation = (item.quantityIssued * newUOM[0].conversion);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){

                    }
                    else{
                        item.receivedQtyCon = (item.quantityIssued);
                        item.receivedQtyCalculation = (item.quantityIssued);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                }
            }
            else{
                if(item.statusId == 18 || item.statusId == 4){
                    if(item.selectedUom.uomTypeId == 1){
                        item.receivedQtyCon = (item.issuedQuantity +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                        item.receivedQtyCalculation = (item.issuedQuantity * newUOM[0].conversion * newUOM[1].conversion);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        item.receivedQtyCon = (item.issuedQuantity +"*"+ newUOM[1].conversion);
                        item.receivedQtyCalculation = (item.issuedQuantity * newUOM[1].conversion);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                    else{
                        item.receivedQtyCon = (item.issuedQuantity);
                        item.receivedQtyCalculation = (item.issuedQuantity);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                }
                else{
                    if(item.selectedUom.uomTypeId == 1){
                        item.receivedQtyCon = (item.quantityIssued +"*"+ newUOM[0].conversion+"*"+ newUOM[1].conversion);
                        item.receivedQtyCalculation = (item.quantityIssued * newUOM[0].conversion * newUOM[1].conversion);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        item.receivedQtyCon = (item.quantityIssued +"*"+ newUOM[1].conversion);
                        item.receivedQtyCalculation = (item.quantityIssued * newUOM[1].conversion);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                    else{
                        item.receivedQtyCon = (item.quantityIssued);
                        item.receivedQtyCalculation = (item.quantityIssued);
                        item.uomUnitData = newUOM[2].uomUnit;
                    }
                }
            }
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

        $scope.calculateReceivedQty1 = function (item) {
            if(item.quantityIssued == undefined || item.quantityIssued == null || item.quantityIssued == ""){
                item.quantityIssued = 0;
            }
            item.quantityIssued = parseInt(item.quantityIssued);
            if(item.uom==undefined || item.uom==null)
        	{
            	item.uom=item.selectedBatch.uom;
        	}
            item.quantityIssued1 = item.quantityIssued;
            if(item.isThreeUOMApplicable == false){
                if(item.uomListData[0].conversion == undefined || item.uomListData[0].conversion == null){
                	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != "")
                	{
                		if(item.selectedUom.uomTypeId == 1)
                        {
                            item.quantityIssued1=item.quantityIssued*(item.uomListdata[1].conversion*item.uomListdata[2].conversion*item.uomListdata[3].conversion);
                        }
                        if(item.selectedUom.uomTypeId == 2)
                        {
                            item.quantityIssued1=item.quantityIssued*(item.uomListdata[2].conversion*item.uomListdata[3].conversion);
                        }
                        if(item.selectedUom.uomTypeId == 3)
                        {
                            item.quantityIssued1=item.quantityIssued*(item.uomListdata[3].conversion);
                        }
                    if(item.selectedUom.uomTypeId == 1){
                    	item.quantityIssued = Math.floor(item.quantityIssued1 / item.uomListData[1].conversion);
                    	if(item.disabled == true){
                    		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uomListData[1].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uomListData[1].conversion);
                            item.uomUnitData = item.uomListData[3].uomUnit;
                    	}
                    	else{
                    		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uomListData[1].conversion);
                            item.receivedQtyCalculation = (item.quantityIssued * item.uomListData[1].conversion);
                            item.uomUnitData = item.uomListData[3].uomUnit;
                    	}
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                    
                    }
                    else{
                    	if(item.disabled == true){
                    		item.receivedQtyCon = (item.issuedQuantity);
                            item.receivedQtyCalculation = (item.issuedQuantity);
                            item.uomUnitData = item.uomListData[3].uomUnit;
                    	}
                    	else{
                    		item.receivedQtyCon = (item.quantityIssued);
                            item.receivedQtyCalculation = (item.quantityIssued);
                            item.uomUnitData = item.uomListData[3].uomUnit;
                    	}
                    }
                }
                }
                else{
                	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != "")
            		{
                		if(item.uomListData[2] == undefined){
                			if(item.selectedUom.uomTypeId == 1)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uoms[0].conversion*item.uoms[1].conversion*item.uoms[2].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 2)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uoms[1].conversion*item.uoms[2].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 3)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uoms[2].conversion);
                            }
                		}
                		else{
                			if(item.selectedUom.uomTypeId == 1)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uomListData[0].conversion*item.uomListData[1].conversion*item.uomListData[2].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 2)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uomListData[1].conversion*item.uomListData[2].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 3)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uomListData[2].conversion);
                            }
                		}
                    if(item.selectedUom.uomTypeId == 1){
                    	item.quantityIssued = Math.floor(item.quantityIssued1 / item.uomListData[0].conversion);
                    	if(item.disabled == true){
                    		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uomListData[0].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uomListData[0].conversion);
                            if(item.uomListData[2] == undefined){
                            	item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else{
                            	item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                    	}
                    	else{
                    		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uomListData[0].conversion);
                            item.receivedQtyCalculation = (item.quantityIssued * item.uomListData[0].conversion);
                            if(item.uomListData[2] == undefined){
                            	item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else{
                            	item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                    	}
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                    
                    }
                    else{
                    	if(item.disabled == true){
                    		item.receivedQtyCon = (item.issuedQuantity);
                            item.receivedQtyCalculation = (item.issuedQuantity);
                            if(item.uomListData[2] == undefined){
                            	item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else{
                            	item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                    	}
                    	else{
                    		item.receivedQtyCon = (item.quantityIssued);
                            item.receivedQtyCalculation = (item.quantityIssued);
                            if(item.uomListData[2] == undefined){
                            	item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else{
                            	item.uomUnitData = item.uomListData[2].uomUnit;
                            }
                    	}
                    }
                }
                }
            }
            else{
                if(item.uom[0].conversion == undefined || item.uom[0].conversion == null){
                    if(item.uom[1] == undefined || item.uom[1] == null){
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    		if(item.uoms==undefined || item.uoms==null || item.uoms.length==0)
                    			{
                    			var counter=0;
                            item.zeroStockBatchList.forEach(function(batch){
                                if(batch.uom!=undefined && batch.uom!=null && batch.uom.length!=0)
                                    {
                                	if(counter==0)
                                		{
                                		item.uoms=batch.uom;
                                		}
                                	counter++;
                                    }
                            });
                    			}
                    		if(item.selectedUom.uomTypeId == 1)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uoms[0].conversion*item.uoms[1].conversion*item.uoms[2].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 2)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uoms[1].conversion*item.uoms[2].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 3)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uoms[2].conversion);
                            }
                        if(item.selectedUom.uomTypeId == 1){
                        	item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uoms[0].conversion * item.uoms[1].conversion));
                        	if(item.disabled == true){
                        		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uoms[0].conversion+"*"+ item.uoms[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uoms[0].conversion * item.uoms[1].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uoms[0].conversion+"*"+ item.uoms[1].conversion);
                                item.receivedQtyCalculation = (item.quantityIssued * item.uoms[0].conversion * item.uoms[1].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                        	}
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                            item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uoms[1].conversion));
                            if(item.disabled == true){
                            	item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uoms[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uoms[1].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else{
                            	item.receivedQtyCon = (item.quantityIssued +"*"+ item.uoms[1].conversion);
                                item.receivedQtyCalculation = (item.quantityIssued * item.uoms[1].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                        }
                        else{
                        	 if(item.disabled == true){
                        		 item.receivedQtyCon = (item.issuedQuantity);
                                 item.receivedQtyCalculation = (item.issuedQuantity);
                                 item.uomUnitData = item.uoms[2].uomUnit;
                        	 }
                        	 else{
                        		 item.receivedQtyCon = (item.quantityIssued);
                                 item.receivedQtyCalculation = (item.quantityIssued);
                                 item.uomUnitData = item.uoms[2].uomUnit; 
                        	 }
                        }
                    	}	
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != "")
                    	{
                    		if(item.selectedUom.uomTypeId == 1)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uom[1].conversion*item.uom[2].conversion*item.uom[3].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 2)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uom[2].conversion*item.uom[3].conversion);
                            }
                            if(item.selectedUom.uomTypeId == 3)
                            {
                                item.quantityIssued1=item.quantityIssued*(item.uom[3].conversion);
                            }
                        if(item.selectedUom.uomTypeId == 1){
                            item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[1].conversion * item.uom[2].conversion));
                            if(item.disabled == true){
                            	item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                            	item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantityIssued * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        	item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[2].conversion));
                        	if(item.disabled == true){
                        		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.quantityIssued * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                        else{
                        	if(item.disabled == true){
                        		item.receivedQtyCon = (item.issuedQuantity);
                                item.receivedQtyCalculation = (item.issuedQuantity);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantityIssued);
                                item.receivedQtyCalculation = (item.quantityIssued);
                                item.uomUnitData = item.uom[3].uomUnit;
                        	}
                        }
                    }
                    }
                }
                else{
                	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                		if(item.selectedUom.uomTypeId == 1)
                        {
                            item.quantityIssued1=item.quantityIssued*(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion);
                        }
                        if(item.selectedUom.uomTypeId == 2)
                        {
                            item.quantityIssued1=item.quantityIssued*(item.uom[1].conversion*item.uom[2].conversion);
                        }
                        if(item.selectedUom.uomTypeId == 3)
                        {
                            item.quantityIssued1=item.quantityIssued*(item.uom[2].conversion);
                        }
                    if(item.selectedUom.uomTypeId == 1){
                    	item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[0].conversion * item.uom[1].conversion));
                    	if(item.disabled == true){
                    		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                    	}
                    	else{
                    		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.quantityIssued * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                    	}
                    }
                    else if(item.selectedUom.uomTypeId == 2){
                        item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[1].conversion));
                        if(item.disabled == true){
                        	item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                        else{
                        	item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.quantityIssued * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                    else{
                    	 if(item.disabled == true){
                    		 item.receivedQtyCon = (item.issuedQuantity);
                             item.receivedQtyCalculation = (item.issuedQuantity);
                             item.uomUnitData = item.uom[2].uomUnit;
                    	 }
                    	 else{
                    		 item.receivedQtyCon = (item.quantityIssued);
                             item.receivedQtyCalculation = (item.quantityIssued);
                             item.uomUnitData = item.uom[2].uomUnit; 
                    	 }
                    }
                	}	
                }
            }
        }

        $scope.calculateReceivedQty = function (item) {
        	if(self.model.statusId == 4){
        		if(item.issuedQuantity == undefined || item.issuedQuantity == null || item.issuedQuantity == ""){
                    item.issuedQuantity = 0;
                }
                if(item.uom==undefined || item.uom==null)
            	{
                	item.uom=item.selectedBatch.uom;
            	}
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData[0].conversion == undefined || item.uomListData[0].conversion == null){
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    	if(item.selectedUom.uomTypeId == 1){
                            item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uomListData[1].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uomListData[1].conversion);
                            item.uomUnitData = item.uomListData[3].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                            item.receivedQtyCon = (item.issuedQuantity);
                            item.receivedQtyCalculation = (item.issuedQuantity);
                            item.uomUnitData = item.uomListData[3].uomUnit;
                        }
                    }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uomListData[0].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uomListData[0].conversion);
                            item.uomUnitData = item.uomListData[2].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                            item.receivedQtyCon = (item.issuedQuantity);
                            item.receivedQtyCalculation = (item.issuedQuantity);
                            item.uomUnitData = item.uomListData[2].uomUnit;
                        }
                    	}
                    }
                }
                else{
                    if(item.uom[0].conversion == undefined || item.uom[0].conversion == null){
                        if(item.uom[1] == undefined || item.uom[1] == null){
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        	if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uoms[0].conversion+"*"+ item.uoms[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uoms[0].conversion * item.uoms[1].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                                item.quantityIssued = Math.floor(item.issuedQuantity / (item.uoms[1].conversion));
                                item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uoms[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uoms[1].conversion);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQuantity);
                                item.receivedQtyCalculation = (item.issuedQuantity);
                                item.uomUnitData = item.uoms[2].uomUnit;
                            }
                        }
                        }
                        else{
                        	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                            if(item.selectedUom.uomTypeId == 1){
                                item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uom[1].conversion * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else if(item.selectedUom.uomTypeId == 2){
                                item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[2].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uom[2].conversion);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issuedQuantity);
                                item.receivedQtyCalculation = (item.issuedQuantity);
                                item.uomUnitData = item.uom[3].uomUnit;
                            }
                        	}
                        }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                            item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                            item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.issuedQuantity * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.issuedQuantity);
                            item.receivedQtyCalculation = (item.issuedQuantity);
                            item.uomUnitData = item.uom[2].uomUnit;
                        }
                    }
                    }
                }
        	}
        	else{
        		if(item.quantityIssued == undefined || item.quantityIssued == null || item.quantityIssued == ""){
                    item.quantityIssued = 0;
                }
        		if(item.quantityIssued1 == undefined || item.quantityIssued1 == null || item.quantityIssued1 == ""){
                    item.quantityIssued1 = 0;
                }
                if(item.uom==undefined || item.uom==null)
            	{
            		item.uom=item.selectedBatch.uom;
            	}
                item.quantityIssued = parseInt(item.quantityIssued);
                if(item.disabled == true){
                	item.quantityIssued = item.quantityIssued;
                }
                else{
                	item.quantityIssued1 = item.quantityIssued;
                }
                if(item.isThreeUOMApplicable == false){
                    if(item.uomListData[0].conversion == undefined || item.uomListData[0].conversion == null){
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                    	if(item.selectedUom.uomTypeId == 1){
                        	if(item.finalOrderedQty>item.quantityIssued1)
							{
                        		item.quantityIssued = Math.floor(item.quantityIssued1 / item.uomListData[1].conversion);
							}
                        	else
                        		{
                        		item.quantityIssued = Math.ceil(item.quantityIssued1 / item.uomListData[1].conversion);
                        		}
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uomListData[1].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uomListData[1].conversion);
                                item.uomUnitData = item.uomListData[3].uomUnit;
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uomListData[1].conversion);
                                item.receivedQtyCalculation = (item.quantityIssued * item.uomListData[1].conversion);
                                item.uomUnitData = item.uomListData[3].uomUnit;
                        	}
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                        	if(item.statusId == 18 || item.statusId == 4){
                        		 item.receivedQtyCon = (item.issuedQuantity);
                                 item.receivedQtyCalculation = (item.issuedQuantity);
                                 item.uomUnitData = item.uomListData[3].uomUnit;
                        	}
                        	else{
                        		 item.receivedQtyCon = (item.quantityIssued);
                                 item.receivedQtyCalculation = (item.quantityIssued);
                                 item.uomUnitData = item.uomListData[3].uomUnit;
                        	}
                        }
                    }
                    }
                    else{
                    	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                        if(item.selectedUom.uomTypeId == 1){
                        	if(item.finalOrderedQty>item.quantityIssued1)
							{
                        		item.quantityIssued = Math.floor(item.quantityIssued1 / item.uomListData[0].conversion);
							}
                        	else
                        		{
                        		item.quantityIssued = Math.ceil(item.quantityIssued1 / item.uomListData[0].conversion);
                        		}
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.issuedQuantity * item.uomListData[0].conversion);
                                if(item.uomListData[2] == undefined){
                                	item.uomUnitData = item.uoms[2].uomUnit;
                                }
                                else{
                                	item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uomListData[0].conversion);
                                item.receivedQtyCalculation = (item.quantityIssued * item.uomListData[0].conversion);
                                if(item.uomListData[2] == undefined){
                                	item.uomUnitData = item.uoms[2].uomUnit;
                                }
                                else{
                                	item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                        	}
                        }
                        else if(item.selectedUom.uomTypeId == 2){
                        
                        }
                        else{
                        	if(item.statusId == 18 || item.statusId == 4){
                        		item.receivedQtyCon = (item.issuedQuantity);
                                item.receivedQtyCalculation = (item.issuedQuantity);
                                if(item.uomListData[2] == undefined){
                                	item.uomUnitData = item.uoms[2].uomUnit;
                                }
                                else{
                                	item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                        	}
                        	else{
                        		item.receivedQtyCon = (item.quantityIssued);
                                item.receivedQtyCalculation = (item.quantityIssued);
                                if(item.uomListData[2] == undefined){
                                	item.uomUnitData = item.uoms[2].uomUnit;
                                }
                                else{
                                	item.uomUnitData = item.uomListData[2].uomUnit;
                                }
                        	}
                        }
                    }
                    }
                }
                else{
                	
                	if(item.batch != null && item.batch.length > 0 && item.uom.length >0  && item.disabled != undefined && item.disabled == false){
                		 if(item.uom[0].conversion == undefined || item.uom[0].conversion == null){
                             if(item.uom[1] == undefined || item.uom[1] == null){
                             	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                             	if(item.selectedUom.uomTypeId == 1){
                                 	if(item.finalOrderedQty>item.quantityIssued1)
         							{
                                 		item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uoms[0].conversion * item.uoms[1].conversion));
         							}
                                 	else
                                 		{
                                 		item.quantityIssued = Math.ceil(item.quantityIssued1 / (item.uoms[0].conversion * item.uoms[1].conversion));
                                 		}
                                 	if(item.statusId == 18 || item.statusId == 4){
                                 		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uoms[0].conversion+"*"+ item.uoms[1].conversion);
                                         item.receivedQtyCalculation = (item.issuedQuantity * item.uoms[0].conversion * item.uoms[1].conversion);
                                         item.uomUnitData = item.uoms[2].uomUnit;
                                 	}
                                 	else{
                                 		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uoms[0].conversion+"*"+ item.uoms[1].conversion);
                                         item.receivedQtyCalculation = (item.quantityIssued * item.uoms[0].conversion * item.uoms[1].conversion);
                                         item.uomUnitData = item.uoms[2].uomUnit;
                                 	}
                                 }
                                 else if(item.selectedUom.uomTypeId == 2){
                                 	if(item.finalOrderedQty>item.quantityIssued1)
         							{
                                 		item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uoms[1].conversion));
         							}
                                 	else
                                 		{
                                 		item.quantityIssued = Math.ceil(item.quantityIssued1 / (item.uoms[1].conversion));
                                 		}
                                     if(item.statusId == 18 || item.statusId == 4){
                                     	 item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uoms[1].conversion);
                                          item.receivedQtyCalculation = (item.issuedQuantity * item.uoms[1].conversion);
                                          item.uomUnitData = item.uoms[2].uomUnit;
                                     }
                                     else{
                                     	 item.receivedQtyCon = (item.quantityIssued +"*"+ item.uoms[1].conversion);
                                          item.receivedQtyCalculation = (item.quantityIssued * item.uoms[1].conversion);
                                          item.uomUnitData = item.uoms[2].uomUnit;
                                     }
                                 }
                                 else{
                                 	 if(item.statusId == 18 || item.statusId == 4){
                                 		  item.receivedQtyCon = (item.issuedQuantity);
                                           item.receivedQtyCalculation = (item.issuedQuantity);
                                           item.uomUnitData = item.uoms[2].uomUnit; 
                                 	 }
                                 	 else{
                                 		  item.receivedQtyCon = (item.quantityIssued);
                                           item.receivedQtyCalculation = (item.quantityIssued);
                                           item.uomUnitData = item.uoms[2].uomUnit;
                                 	 }
                                 }
                             }
                             }
                             else{
                             	if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
                                 if(item.selectedUom.uomTypeId == 1){
                                 	if(item.finalOrderedQty>item.quantityIssued1)
         							{
                                 		item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[1].conversion * item.uom[2].conversion));
         							}
                                 	else
                                 		{
                                 		item.quantityIssued = Math.ceil(item.quantityIssued1 / (item.uom[1].conversion * item.uom[2].conversion));
                                 		}
                                     if(item.statusId == 18 || item.statusId == 4){
                                     	item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                         item.receivedQtyCalculation = (item.issuedQuantity * item.uom[1].conversion * item.uom[2].conversion);
                                         item.uomUnitData = item.uom[3].uomUnit;
                                     }
                                     else{
                                     	item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                                         item.receivedQtyCalculation = (item.quantityIssued * item.uom[1].conversion * item.uom[2].conversion);
                                         item.uomUnitData = item.uom[3].uomUnit;
                                     }
                                 }
                                 else if(item.selectedUom.uomTypeId == 2){
                                 	if(item.finalOrderedQty>item.quantityIssued1)
         							{
                                 		item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[2].conversion));
         							}
                                 	else
                                 		{
                                 		item.quantityIssued = Math.ceil(item.quantityIssued1 / (item.uom[2].conversion));
                                 		}
                                 	if(item.statusId == 18 || item.statusId == 4){
                                 		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[2].conversion);
                                         item.receivedQtyCalculation = (item.issuedQuantity * item.uom[2].conversion);
                                         item.uomUnitData = item.uom[3].uomUnit;
                                 	}
                                 	else{
                                 		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[2].conversion);
                                         item.receivedQtyCalculation = (item.quantityIssued * item.uom[2].conversion);
                                         item.uomUnitData = item.uom[3].uomUnit;
                                 	}
                                 }
                                 else{
                                 	if(item.statusId == 18 || item.statusId == 4){
                                 		item.receivedQtyCon = (item.issuedQuantity);
                                         item.receivedQtyCalculation = (item.issuedQuantity);
                                         item.uomUnitData = item.uom[3].uomUnit;
                                 	}
                                 	else{
                                 		item.receivedQtyCon = (item.quantityIssued);
                                         item.receivedQtyCalculation = (item.quantityIssued);
                                         item.uomUnitData = item.uom[3].uomUnit;
                                 	}
                                 }
                             }
                             }
                         }
                         else{
	                         if(item.selectedUom != undefined && item.selectedUom != null && item.selectedUom != ""){
	                             if(item.selectedUom.uomTypeId == 1){
	                             	if(item.finalOrderedQty>item.quantityIssued1)
	     							{
	                             		item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[0].conversion * item.uom[1].conversion));
	     							}
	                             	else
	                             		{
	                             		item.quantityIssued = Math.ceil(item.quantityIssued1 / (item.uom[0].conversion * item.uom[1].conversion));
	                             		}
	                             	if(item.statusId == 18 || item.statusId == 4){
	                             		item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
	                                     item.receivedQtyCalculation = (item.issuedQuantity * item.uom[0].conversion * item.uom[1].conversion);
	                                     item.uomUnitData = item.uom[2].uomUnit;
	                             	}
	                             	else{
	                             		item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
	                                     item.receivedQtyCalculation = (item.quantityIssued * item.uom[0].conversion * item.uom[1].conversion);
	                                     item.uomUnitData = item.uom[2].uomUnit;
	                             	}
	                             }
	                             else if(item.selectedUom.uomTypeId == 2){
	                             	if(item.finalOrderedQty>item.quantityIssued1)
	     							{
	                             		item.quantityIssued = Math.floor(item.quantityIssued1 / (item.uom[1].conversion));
	     							}
	                             	else
	                             		{
	                             		item.quantityIssued = Math.ceil(item.quantityIssued1 / (item.uom[1].conversion));
	                             		}
	                                 if(item.statusId == 18 || item.statusId == 4){
	                                 	item.receivedQtyCon = (item.issuedQuantity +"*"+ item.uom[1].conversion);
	                                     item.receivedQtyCalculation = (item.issuedQuantity * item.uom[1].conversion);
	                                     item.uomUnitData = item.uom[2].uomUnit;
	                                 }
	                                 else{
	                                 	item.receivedQtyCon = (item.quantityIssued +"*"+ item.uom[1].conversion);
	                                     item.receivedQtyCalculation = (item.quantityIssued * item.uom[1].conversion);
	                                     item.uomUnitData = item.uom[2].uomUnit;
	                                 }
	                             }
	                             else{
	                             	 if(item.statusId == 18 || item.statusId == 4){
	                             		 item.receivedQtyCon = (item.issuedQuantity);
	                                      item.receivedQtyCalculation = (item.issuedQuantity);
	                                      item.uomUnitData = item.uom[2].uomUnit; 
	                             	 }
	                             	 else{
	                             		 item.receivedQtyCon = (item.quantityIssued);
	                                      item.receivedQtyCalculation = (item.quantityIssued);
	                                      item.uomUnitData = item.uom[2].uomUnit;
	                             	 }
	                             }
	                         }
                         }
					}
                   
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

            if (!!uom) {
            	if($scope.autoPrescribedQty != true){
            		item.quantityIssued = 0;
            		//Caclulate amount including discount
                    Calculate(item, bill);

                    CalculateAmount(item);
                    
                    //Convert Ordered Quantity
                    if(item.selectedUom!=undefined && item.selectedUom!=null)
                	{
                    ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                	}
            	}
            	else{
            		//Convert Ordered Quantity
                    if(item.selectedUom!=undefined && item.selectedUom!=null)
                	{
                    ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                	}
                    
            		//Caclulate amount including discount
                    Calculate(item, bill);

                    CalculateAmount(item);
            	}
                
                if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                    $scope.isValidated = true;
                }
                else{
                    $scope.isValidated = false;
                }
                $scope.activePackageListWithoutForceClose=[];
				angular.forEach($scope.activePackageList, function (item, key)  {
					if(item.isForceClose==1)
						{
						$scope.activePackageListWithoutForceClose.push(item);
						}
				});
                if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                    $scope.isValidated = true;
                }
            } else {
                item.rate = "";
            }
            //AllocateQuantity(index, bill, parentIndex);
        }
        
        $scope.calculateAtCopCalculation=function(item, bill){

            // if(item.quantityIssued != 0){
        	if(item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.length!=0)
    		{
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
        }
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
                "qty": item.quantityIssued,
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
            //item.selfPayable = response.netAmount;
            //item.creditPayable = response.creditPayable;
            item.packageConsumptionAmt = item.packageConsumptionAmt;
            item.patientClassAmount = response.patientClassAmount;
            item.unitPatientClassAmount = response.unitPatientClassAmount;
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }
            else{
                $scope.isValidated = false;
            }
            $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
            if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                $scope.isValidated = true;
            }
            CalculateAmount(item);
        }
        $scope.clearDiscountOnQtyChange=function(){
        	var counter=0;
				if(self.model.pendingIssuesDetails.discountCatgeory.id!=undefined && self.model.pendingIssuesDetails.discountCatgeory.id!=null)
				{
					counter++;
				}
				if(counter>0)
					{
					angular.forEach(self.model.pendingIssuesDetails.items, function(value){
					self.model.pendingIssuesDetails.discount=0;
					if(value.disabled==false)
						{
					    value.itemDiscount = "0";
					    value.discountPerItem=0;
						}
					self.model.ApplyDiscount(self.model.pendingIssuesDetails);
					});
					}
				if(self.model.pendingIssuesDetails.discountCatgeory.id==undefined || self.model.pendingIssuesDetails.discountCatgeory.id==null)
					{
					self.model.pendingIssuesDetails.discountCatgeory=self.model.discountCategoryList[0];
					}
        };
        var Calculate = function(item, bill){
        	if(item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.length!=0)
        		{
        	if($scope.atCop == true){
                item.selectedBatch.sellingPrice = item.selectedBatch.actualLendingPrice;
                item.rate = item.selectedBatch.actualLendingPrice;
                item.mrp = item.rate;
            }
        	else{
    		  item.selectedBatch.sellingPrice = item.selectedBatch.rate;
              item.rate = item.selectedBatch.rate;
              item.mrp = item.rate;
        	}
            var sellingPrice = !!item.selectedBatch ? commonDetailService.fixedDecimal(item.selectedBatch.sellingPrice) : 0;
            if(item.selectedUom){
                item.mrp = commonDetailService.calculateRate(item.selectedBatch.uom, item.selectedUom.uomTypeId, sellingPrice);
            }
        }
            self.model.beforeCalculation = item;
            if(item.statusId==4 && (item.issuedQuantity==0))
            	{
            	item.quantityIssued=0;
            	}
               if(item.quantityIssued>item.stockAvailable && item.itemDisabled != true)
                {
                    item.quantityIssued=item.stockAvailable;
                }
            var data = {
                "mrp": item.mrp!=undefined && item.mrp!=null ? item.mrp : 0,
                "isTaxInc": $scope.taxInclusive,
                "markup": item.markUpDown,
                "markUpDownPer" : item.markUpDownPer,
                "discount": item.discountPerItem ? item.discountPerItem : 0,
                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                "tax": item.taxPercentage,
                "qty": item.quantityIssued!=undefined && item.quantityIssued!=null ? item.quantityIssued :0,
                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                "patientClassPercentage": item.patientClassPercentage,
            }
  
            var response = commonDetailService.calculateAmount(data);
            item.rate = response.rate;
            if(item.disabled != true){
            	item.taxAmount = response.taxAmount;
            }
            item.responseTaxAmount =  response.taxAmount;
            item.unitDiscountAmount = response.unitDiscountAmount;
            item.itemDiscount =  response.discountAmount;
            item.itemtotalAmount = response.totalAmount;
            item.netAmount = response.netAmount;
//            item.selfPayable = response.netAmount;
//            item.creditPayable = response.creditPayable;
            item.packageConsumptionAmt = item.packageConsumptionAmt;
            item.patientClassAmount = response.patientClassAmount;
            $scope.patientClassAmount = response.patientClassAmount;
            item.unitPatientClassAmount = response.unitPatientClassAmount;
            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }
            else{
                $scope.isValidated = false;
            }
            $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
            if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                $scope.isValidated = true;
            }
        	
            if(item.selectedItems != undefined && item.selectedItems != null && item.selectedItems.length > 0){
                angular.forEach(item.selectedItems, function(s){
                	$scope.afterSaveUOM;
                	if($scope.globalFlag==1)
            		{
            		$scope.atCop=$scope.atCopSelected;
            		if(item.selectedBatch!=undefined && item.selectedBatch!=null && item.selectedBatch.length!=0)
            		{
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
                    var sellingPrice = !!s.selectedBatch ? commonDetailService.fixedDecimal(s.selectedBatch.sellingPrice!=undefined ||s.selectedBatch.sellingPrice!=null ? s.selectedBatch.sellingPrice :s.selectedBatch.rate) : (s.selectedBatch==undefined || s.selectedBatch==null ? s.rate : 0);
                    s.mrp = commonDetailService.calculateRate(s.selectedBatch!=undefined ?s.selectedBatch.uom : $scope.afterSaveUOM, s.selectedUom!=undefined ? s.selectedUom.uomTypeId :s.uomTypeId, sellingPrice!=undefined || sellingPrice!=0 ? sellingPrice : s.rate);
                }
                    self.model.beforeCalculation = s;
                    if(s.discount==undefined || s.discount==null)
                	{
                    	if(isNaN(s.discountPerItem) || s.discountPerItem==undefined || s.discountPerItem==null)
                    	{
                    		s.discountPerItem=((s.discountAmount*100)/(s.totalAmount!=undefined && s.totalAmount!=null ? s.totalAmount :1)).toFixed(2);
                		}
                	}
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
                    if(data.qty==undefined || data.qty==null || data.qty==0)
                    {
                    data.qty=s.issuedQuantity!=undefined ? s.issuedQuantity :0;
                    }
                    if(data.qty==undefined || data.qty==null || data.qty==0)
                    {
                    data.qty=s.quantityIssued!=undefined ? s.quantityIssued :0;
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
                    //s.selfPayable = response.netAmount;
                    //s.creditPayable = response.creditPayable;
                    s.packageConsumptionAmt = s.packageConsumptionAmt;
                    s.patientClassAmount = response.patientClassAmount;
                    s.unitPatientClassAmount = response.unitPatientClassAmount;
                    if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                        $scope.isValidated = true;
                    }
                    else{
                        $scope.isValidated = false;
                    }
                    $scope.activePackageListWithoutForceClose=[];
        			angular.forEach($scope.activePackageList, function (item, key)  {
        				if(item.isForceClose==1)
        					{
        					$scope.activePackageListWithoutForceClose.push(item);
        					}
        			});
                    if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                        $scope.isValidated = true;
                    }    
                });
            }

            if($scope.autoPrescribedQty == true){
            	if((item.quantityIssued == undefined || item.quantityIssued == null || parseInt(item.quantityIssued) == 0) && (item.issuedQuantity == undefined || item.issuedQuantity == null || parseInt(item.issuedQuantity) == 0)){
                	if(item.newQty != undefined && item.newQty != null && item.newQty != 0){
                		item.quantityOrdered = item.newQty;
                    	item.newQty = undefined;
                	}
                }
            }
        	
            CalculateAmount(item);
        }
        
        var onChangeDiscount = function(item, method){
            if(validateDiscount(item,method)){
                if(method =='linewise'){
                    angular.forEach(item.items, function (value, key) {
                        if(value.discountPerItem){
                            if(value.discountPerItem > 100){
                                growl.error("Discount should be less than or equal to 100%")
                                value.discountPerItem = 0;
                                value.patientClassPercentage = 0;
                            }else{
                                item.discount = 0;
                                item.discountType = 1;
                                item.discountCatgeory = self.model.discountCategoryList[0];
                            }
                        }
                        if(value.selectedItems){
                            angular.forEach(value.selectedItems, function(value1){
                                if(value1.discountPerItem){
                                    if(value1.discountPerItem > 100){
                                        growl.error("Discount should be less than or equal to 100%")
                                        value1.discountPerItem = 0;
                                        value1.patientClassPercentage = 0;
                                    }else{
                                        item.discount = 0;
                                        item.discountType = 1;
                                        item.discountCatgeory = self.model.discountCategoryList[0];
                                    }
                                }
                            });
                        }
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
                        	if(value1.discountPerItem != undefined && value1.discountPerItem != null){
                        		if(regexp.test(Number(value1.discountPerItem))){
                                    isValid = true;
                                }
                                else{
                                    isValid = false;
                                    value1.discountPerItem = 0;
                                }
                        	}
                        	else{
                        		value1.discountPerItem = 0;
                        	}
                        });
                    }
                }); 
            }
            else{
                if(self.model.pendingIssuesDetails.discountType == 1){
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
            return isValid
        }

        var OnKeyPressQuantity = function (item) 
        {
            if (!item.quantityIssued) {
                item.totalAmount = 0;
                item.itemDiscount = 0;
                item.taxAmount = 0;
                item.netAmount = 0;
            }
        }

        var SetDecimal = function () {
            if (isNaN(self.model.pendingIssuesDetails.discount) || !self.model.pendingIssuesDetails.discount) 
            {
                self.model.pendingIssuesDetails.discount = 0;
            } else {
                self.model.pendingIssuesDetails.discount = parseFloat(self.model.pendingIssuesDetails.discount).toFixed(2);
            }
        }
  
        var ApplyDiscount = function (bill) {
            if(bill.discountType == '2' && bill.netAmountData == undefined){
                bill.netAmountData = bill.netAmount;
            }
            if(ApplyDiscountValidation(bill) && CheckQuantityForDiscount(bill)){
                var item = bill.items;
                var discount = parseFloat(bill.discount);
                var itemDiscount1 = (parseFloat(bill.discount) * bill.totalAmount)/100;
                var discountForPercentage=parseFloat(itemDiscount1)
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
                                        "markup": value.markUpDown,
                                        "markUpDownPer" : value.markUpDownPer,
                                        "discount":  0,
                                        "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                        "tax": value.taxPercentage,
                                        "qty": value.quantityIssued,
                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                        "discountPerItem":((discount*100)/bill.totalAmount).toFixed(2),
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
                                    value.packageConsumptionAmt = value.packageConsumptionAmt;
                                    
                                    billAmount = billAmount+parseFloat(value.netAmount);
                                    if(value.selectedItems){
                                        angular.forEach(value.selectedItems, function(svalue){
                                        	if(!svalue.disabled && !svalue.itemDisabled){
                                            	var data = {
                                                    "mrp": svalue.mrp,
                                                    "isTaxInc": $scope.taxInclusive,
                                                    "markup": svalue.markUpDown,
                                                    "markUpDownPer" : svalue.markUpDownPer,
                                                    "discount": 0,
                                                    "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                    "tax": svalue.taxPercentage,
                                                    "qty": svalue.quantityIssued,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":((discount*100)/bill.totalAmount).toFixed(2),
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
                	                            svalue.packageConsumptionAmt = svalue.packageConsumptionAmt;
                	                            
                	                            billAmount = billAmount+parseFloat(svalue.netAmount);
                                        	}
                                        });
                                    }
                                }
                                else{
                                    angular.forEach(value.selectedItems, function (svalue, key) { 
                                        if(!svalue.disabled && !svalue.itemDisabled){
                                            var data = {
                                                "mrp": svalue.mrp,
                                                "isTaxInc": $scope.taxInclusive,
                                                //  "markup": !!svalue.markUpDown ? svalue.markUpDownPer : null,
                                                //  "markdown": !svalue.markUpDown ? svalue.markUpDownPer : null,
                                                "markup": svalue.markUpDown,
                                                "markUpDownPer" : svalue.markUpDownPer,
                                                "discount":  0,
                                                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                                                "tax": svalue.taxPercentage,
                                                "qty": svalue.quantityIssued,
                                                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                "discountPerItem":((discount*100)/bill.totalAmount).toFixed(2),
                                                "patientClassPercentage": svalue.patientClassPercentage
                                            }
                         
                                            var response = commonDetailService.calculateDisAmount(data); 
                                            svalue.rate = response.rate;
                                            svalue.taxAmount = response.taxAmount;
                                            svalue.responseTaxAmount =  response.taxAmount;
                                            svalue.unitDiscountAmount = response.unitDiscountAmount;
                                            svalue.itemDiscount =  0;
                                            svalue.itemtotalAmount = response.totalAmount
                                            svalue.totalAmount = response.totalAmount;
                                            svalue.netAmount = response.netAmount;
                                            svalue.discountPerItem =0;
                                            svalue.discountAmount = 0;
                                            svalue.selfPayable = response.netAmount;
                                            svalue.patientClassAmount = response.patientClassAmount;
                                            svalue.creditPayable = response.creditPayable;
                                            svalue.packageConsumptionAmt = svalue.packageConsumptionAmt;
                                        }
                                    });
                                 
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
                                            "qty": value.quantityIssued,
                                            "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                            "discountPerItem":((discount*100)/bill.totalAmount).toFixed(2),
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
                                        // value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem * 0.01;
                                        value.patientClassAmount = response.patientClassAmount;
                                        value.creditPayable = response.creditPayable;
                                        value.packageConsumptionAmt = value.packageConsumptionAmt;
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
                                                        "qty": svalue.quantityIssued,
                                                        "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                        "discountPerItem":((discount*100)/bill.totalAmount).toFixed(2),
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
                                                    // svalue.itemDiscount  =  svalue.itemtotalAmount * svalue.discountPerItem * 0.01;
                                                    svalue.patientClassAmount = res.patientClassAmount;
                                                    svalue.creditPayable = res.creditPayable;
                	                                svalue.packageConsumptionAmt = svalue.packageConsumptionAmt;
                                                }
                                            });
                                            value.discountPerItem= ((discount / bill.totalAmount)*100).toFixed(2);
                                            value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                        }
                                    }
                                    else{
                                        angular.forEach(value.selectedItems, function (svalue, key) {
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
                                                    "qty": svalue.quantityIssued,
                                                    "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                                                    "discountPerItem":((discount*100)/bill.totalAmount).toFixed(2),
                                                    "patientClassPercentage": svalue.patientClassPercentage
                                                }
                            
                                                var response = commonDetailService.calculateDisAmount(data);
                                
                                                svalue.rate = response.rate;
                                                svalue.taxAmount = response.taxAmount;
                                                svalue.responseTaxAmount =  response.taxAmount;
                                                svalue.unitDiscountAmount = response.unitDiscountAmount;
                                                svalue.totalAmount = response.totalAmount;
                                                svalue.itemtotalAmount = response.totalAmount
                                                svalue.netAmount = response.netAmount;
                                                svalue.itemDiscount =  response.discountAmount;
                                                svalue.discountPerItem = ((discount / bill.totalAmount) * 100).toFixed(2);
                                                svalue.discountAmount = (value.itemtotalAmount)*value.discountPerItem* 0.01;
                                                svalue.selfPayable = response.netAmount;
                                                svalue.patientClassAmount = response.patientClassAmount;
                                                svalue.creditPayable = response.creditPayable;
                                                svalue.packageConsumptionAmt = svalue.packageConsumptionAmt;
                                            }
                                        });
                                        }
                                
                                    self.model.isDiscountAppliedValid = false;
                                    self.model.isDiscountApplied = true;
                                    self.model.isAppliedDiscountValid = false;
                                    value.itemDiscount = value.itemtotalAmount * value.discountPerItem / 100;
                                    var unitDiscountAmount = value.itemDiscount / value.quantityIssued;
                                    bill.items[key]['unitDiscountAmount'] = unitDiscountAmount;
                                    CalculateAmount(value);
                                    angular.forEach(value.selectedItems, function (subvalue, subkey) {
                                        if(!subvalue.disabled && !subvalue.itemDisabled){
                                            if (subvalue.itemtotalAmount > itemDiscount) {
                                                subvalue.itemDiscount = subvalue.itemtotalAmount * subvalue.discountPerItem / 100;
                                            } else {
                                                subvalue.itemDiscount = 0;
                                            }

                                            var unitDiscountAmount = subvalue.itemDiscount / subvalue.quantityIssued;
                                            
                                            bill.items[key].selectedItems[subkey]['unitDiscountAmount'] = unitDiscountAmount;
                                        }
                                    });
                                }
                            });
                        } else {
                            SetDiscountFlags();
                            // growl.error('Please enter discount amount less than Total Amount');
                        }
                        if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                            $scope.isValidated = true;
                        }
                        else{
                            $scope.isValidated = false;
                        }
                        $scope.activePackageListWithoutForceClose=[];
            			angular.forEach($scope.activePackageList, function (item, key)  {
            				if(item.isForceClose==1)
            					{
            					$scope.activePackageListWithoutForceClose.push(item);
            					}
            			});
                        if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                            $scope.isValidated = true;
                        }
                    } else if (bill.discountType == '1') {
                    	
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
                                        "qty": value.quantityIssued,
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
                                    value.packageConsumptionAmt = value.packageConsumptionAmt;
                                    
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
                                                    "qty": svalue.quantityIssued,
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
                                                svalue.packageConsumptionAmt = svalue.packageConsumptionAmt;
                                                        
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
                                                "qty": value.quantityIssued,
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
                                            value.packageConsumptionAmt = value.packageConsumptionAmt;
                                            value.discountPerItem= self.model.pendingIssuesDetails.discount;
                                            value.itemDiscount  =  value.itemtotalAmount * value.discountPerItem / 100;
                                        }
                                    });
                                    billAmount = billAmount+parseFloat(value.netAmount);
                                }
                                bill.netAmount = billAmount;
                            }
                        });
                        //END
                    	
                        if (self.model.pendingIssuesDetails.discount <= discountLimit) {
                        	var itemDiscount1 = discountForPercentage / bill.totalAmount;
                            angular.forEach(bill.items, function (value, key) {
                                if(!value.itemDisabled){ 
//                                    value.itemDiscount = value.itemtotalAmount * itemDiscount;

                                    var unitDiscountAmount = itemDiscount1 / value.quantityIssued;
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
                                            "qty": value.quantityIssued,
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
                                                        "qty": svalue.quantityIssued,
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
                                                    "qty": svalue.quantityIssued,
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
                                            subvalue.itemDiscount = subvalue.itemtotalAmount * subvalue.discountPerItem / 100;
                                            var unitDiscountAmount = subvalue.itemDiscount / subvalue.quantityIssued;
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
                        $scope.activePackageListWithoutForceClose=[];
            			angular.forEach($scope.activePackageList, function (item, key)  {
            				if(item.isForceClose==1)
            					{
            					$scope.activePackageListWithoutForceClose.push(item);
            					}
            			});
                        if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                            $scope.isValidated = true;
                        }
                    }
                }
            }
        }
        
        var SetDiscountFlags = function () {
            self.model.isDiscountAppliedValid = true;
            self.model.isDiscountApplied = false;
            self.model.isAppliedDiscountValid = true
        }

        var CheckQuantityForDiscount = function (bill) {
            var isValid = true;
            bill.items.some(function (item) {
                if($scope.taxInclusive == true){    
                    if ((parseFloat(bill.discount) > bill.netAmountData) && (parseFloat(bill.discount) > bill.netAmount) && (bill.discountType == '2')) {
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
            return isValid
        }

        var ApplyDiscountValidation = function (bill) {
            var isValid = true;
            if(self.model.pendingIssuesDetails.discountCatgeory.discountCategory=="Select Discount Category"){
                if(!self.model.pendingIssuesDetails.discountCatgeory.id){
                    growl.error('Please select Discount Catgeory');
                    isValid = false;
                }else{
                    self.model.discountTypeFlag = false
                    if (!self.model.pendingIssuesDetails.discount) {
                        if (bill.discountType == '1') {
                            growl.error('Please enter % discount');
                            isValid = false;
                        }
                        if (bill.discountType == '2') {
                            growl.error('Please enter discount amount');
                            isValid = false;
                        }
                        SetDiscountFlags();
                    }
                }
            }
            else
            	{
            	self.model.discountTypeFlag = false;
            	}
            
           return isValid
        }

        var AddItem = function (index, bill  , parentIndex) {
            if (parentIndex == undefined) {
            	if(bill.items[index].quantityIssued==undefined || bill.items[index].quantityIssued== null ||bill.items[index].quantityIssued=="" || bill.items[index].quantityIssued==0 || !bill.items[index].quantityIssued){
          		    growl.error('Please Enter Dispensed Quantity')
                    return false;
            	}
                bill.items.splice(index + 1, 0, CopyItem(index, bill));
                bill.items[index]['disableInputField']=true;
                bill.items[index+1].id = null;
                bill.items[index+1].parentIssueDetailsId =  bill.items[index].id == null ? bill.items[index].parentIssueDetailsId :  bill.items[index].id;
            } else {
                bill.items[parentIndex].selectedItems.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
                bill.items[parentIndex].selectedItems[index]['disableInputField']=true;
                bill.items[parentIndex].selectedItems[index+1]['isSelected']=true;
            }

            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                $scope.isValidated = true;
            }
            else{
                $scope.isValidated = false;
            }
            $scope.activePackageListWithoutForceClose=[];
			angular.forEach($scope.activePackageList, function (item, key)  {
				if(item.isForceClose==1)
					{
					$scope.activePackageListWithoutForceClose.push(item);
					}
			});
            if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                $scope.isValidated = true;
            }
        }
        $scope.convertIssuedQty = function(item)
        {
            if(item.selectedUom.uomTypeId==1)
            {
                item.quantityIssued1 = item.quantityIssued*(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion);
            }
            else if(item.selectedUom.uomTypeId==2)
            {
                item.quantityIssued1 = item.quantityIssued*(item.uom[1].conversion*item.uom[2].conversion);
            }
            return item.quantityIssued1;
        }
        var CopyItem = function (index, bill ,parentIndex) {
            var item = null;
            var totalIssuedQty = 0;
            var finalOrdQty = 0;
            if (parentIndex == undefined) {
                item = bill.items[index];
                totalIssuedQty = $scope.convertIssuedQty(item);
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
             
       	    if(finalOrdQty!=undefined){
   	    		var newOrderQty = finalOrdQty - totalIssuedQty;
       	    }else{
               var newOrderQty = undefined;
       	    }
 	     
            return {
                id: item.id,
                itemCode: item.itemCode,
                itemName: item.itemName,
                itemId: item.itemId,
                formulationTypeId: item.formulationTypeId,
                productCategoryId:item.productCategoryId,
                genericName: item.genericName,
                genericId: item.genericId,
                manufacturerName: item.manufacturerName,
                quantityOrdered: newOrderQty,
                newQty: newOrderQty ,
                stockAvailable: item.stockAvailable,
                quantityIssued: 0,
                batch: item.batch,
                isNarcotic:item.isNarcotic,
                selectedBatch: null,
                storeId: item.storeId,
                uom: item.uom,
                selectedUom: null,
                duration : item.duration,
                frequencyDesc : item.frequencyDesc,
                markUpDown: item.markUpDown,
                markUpDownPer: item.markUpDownPer,
                markupStatus: item.markupStatus,
                taxId: item.taxId,
                pickedQuantity : item.pickedQuantity!=undefined ? item.pickedQuantity :0,
                tax: item.tax,
                taxPercentage: item.taxPercentage,
                rate: item.rate,
                saleDetailPayeeMapperDtoList: [],
                issuedQuantity: item.issuedQuantity,
                prescriptionDetailId: item.prescriptionDetailId,
                isShow: true,
                substituteBtn:item.substituteBtn!=undefined?item.substituteBtn:parentIndex==undefined?false:true,
                //  newQty: item.newQty,
                isFromAssignPicker : item.isFromAssignPicker,
                interventionFlag: item.interventionFlag,
                issuedQty : item.issuedQuantity,
                parentIssueDetailsId:((item.parentIssueDetailsId == undefined && item.parentIssueDetailsId == null )? null : item.parentIssueDetailsId),
                parentItemId : ((item.parentItemId == undefined && item.parentItemId == null )? null : item.parentItemId) ,
                finalOrderedQty : item.finalOrderedQty,
                instructionDesc : item.instructionDesc,
                dose : item.dose,
                doseUnit : item.doseUnit,
                patientClassAmount : item.patientClassAmount,
                patientClassPercentage : item.patientClassPercentage,
                checkNewItem: item.checkNewItem,
                itemDisabled : false,
                disabled : false,
                storageUnitName: item.storageUnitName,
                isMultiBatch:true,
                isThreeUOMApplicable: item.isThreeUOMApplicable,
                onlySubButtonDisable: item.onlySubButtonDisable,
                barcodeItem: item.barcodeItem,
                uomListData: item.uomListData,
                finalOrderedQty:newOrderQty
            }
        }
        $scope.itemsRemoved=false;
        var RemoveItem = function (index, bill,parentIndex) {
        	
        	//sneha
        	if( $scope.nonFormularyItemList!= undefined && $scope.nonFormularyItemList != null && $scope.nonFormularyItemList.length > 0){
        		angular.forEach($scope.nonFormularyItemList, function (value, key) {
          			if(bill.items[index].prescriptionDetailsId!=undefined && bill.items[index].prescriptionDetailsId!= null && bill.items[index].prescriptionDetailsId==value.prescriptionDetailsid){
          					value.status = 'A';
          					$scope.servicesListPopup.splice(key, 1);
          					 $scope.orderQtyTotalSum =  parseFloat($scope.orderQtyTotalSum) - parseFloat(value.quantity);
          				}
          			});
        	}
        	
        	$scope.itemsRemoved=true;
            var item = bill.items[index];
            if(item != undefined && item.onlySubButtonDisable != undefined && item.onlySubButtonDisable != null)
            {
            	$scope.itemsRemoved=false;
            }
            var parentIndex = self.model.parentIndexNew;
            if(parentIndex!=undefined && parentIndex!=null)
            {
        if( bill.items[parentIndex].selectedItems[index-1]!=undefined && bill.items[parentIndex].selectedItems[index-1]!=null && bill.items[parentIndex].selectedItems[index-1].disableInputField!=undefined && bill.items[parentIndex].selectedItems[index-1].disableInputField!=null)
        {
            bill.items[parentIndex].selectedItems[index-1].disableInputField=false;
            bill.items[parentIndex].selectedItems[index-1].quantityIssuedFlag=false;
        }
            }
        else
            {
            if(bill.items[index-1]!=undefined && bill.items[index-1]!=null && bill.items[index-1].disableInputField!=undefined && bill.items[index-1].disableInputField!=null)
            {
            bill.items[index-1].disableInputField = false;
            bill.items[index-1].quantityIssuedFlag =false;
            }
            }
            if (parentIndex == undefined &&  parentIndex == null && (item.allocationIds!=undefined || item.allocationIds!=null)) {

                var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+ $cookies.getObject('cookieObject').storeId + "/" + bill.id + "/" + item.allocationIds;

                $rootScope.startLoader();
                GenericService.serviceAction("DELETE", URI, {}).then(
                function (response) {
                    if (!!response.data) {
                        
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
            	
            }
            
            var item = null;
            if (parentIndex == undefined &&  parentIndex == null) {
                $scope.removeItemFlag = true;
                item = bill.items[index];
                if(item.quantityOrdered == undefined){
                	$scope.removeItemFlag = false;
                }
                var itemCount = 0;
                angular.forEach(self.model.pendingIssuesDetails.items, function (value, index) {
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
                if(bill.items[parentIndex].selectedItems.length>1){	
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
                bill.items[index]['disabled'] = false;
                bill.items[index]['itemDisabled'] = false;
                bill.items.splice(index, 1);
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
            var count=0;
            angular.forEach(bill.items[parentIndex].selectedItems, function (value, index) {
                if(value.statusId==undefined || value.statusId==null ){
                	count++;
                }
            });
            if(count==0)
            	{
            	 bill.items[parentIndex]['disabled'] = false;
                 bill.items[parentIndex]['itemDisabled'] = false;
            	}
            if(bill.items[parentIndex].selectedItems.length==0)
        	  {
	              bill.items[parentIndex].quantityIssuedFlag =false;
	              bill.items[parentIndex].disableInputField=false;
        	  }else{
				  var countSub = 0 ;
				    angular.forEach(bill.items[parentIndex].selectedItems, function (value, index) {
		                if(value.itemDisabled==undefined || value.itemDisabled==false ){
		                	countSub++;
		                }
		            });
				  if(countSub == 0){
					   bill.items[parentIndex].quantityIssuedFlag =false;
		               bill.items[parentIndex].disableInputField=false;
				  }
			  }
        }
        
        var ValidateMaxOrderQuantity = function (item, uom, uomList) {
            var isValid = true;  var totalQuantitys = 0; var remainingQuantity = 0;

            item.isParentQuantityValid = false;
            var totalLeaseQuantity = 0;

            // CalculateQuantity(item, uom, uomList);
            var list = self.model.pendingIssuesDetails.items.filter(x=> x.prescriptionDetailId == item.prescriptionDetailId)
            if(list.length > 0){
                for(let lt of list){
                    if(lt.quantityIssued) {
                        totalQuantitys = totalQuantitys + Number(lt.quantityIssued)
                    }
                }
                remainingQuantity = item.quantityOrdered - item.issuedQuantity
                if(Number(totalQuantitys) > remainingQuantity){
                   /* item.isQuantityValid = true;
                    growl.error("Enter quantity is more than order quantity");
                    isValid = false;
                    item.quantityIssued = 0;*/
                } else {
                    item.isQuantityValid = false;
                }
            }
            var items = self.model.pendingIssuesDetails.items.filter(function (detail) {
                return detail.itemId == item.itemId;
            });

            items.forEach(function (detail) {
                totalLeaseQuantity = totalLeaseQuantity + detail.leaseQuantity;
            });

      /*      if (!item.quantityDispensed) {
                if (totalLeaseQuantity > item.qtyPrescribed) {
                    item.isParentQuantityValid = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the ordered quantity')
                }
            } else {
                if (totalLeaseQuantity > (item.qtyPrescribed - item.quantityDispensed)) {
                    item.isParentQuantityValid = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the ordered quantity')
                }
            }*/

            return isValid;
        }

        var CalculateAmount = function (item) {
            if ($scope.taxInclusive == true) {
            	if($scope.autoPrescribedQty == false)
        		{
            		item.itemtotalAmount = ((item.rate != undefined ?(item.rate.toFixed(2) ? item.rate.toFixed(2) : 0):0) * (parseInt(item.quantityIssued) ? parseInt(item.quantityIssued) : item.issuedQuantity ? item.issuedQuantity :0));
        		}
            	else
            	{
	            	if((item.quantityIssued == undefined || item.quantityIssued == null || item.quantityIssued == 0) && (item.issuedQuantity == undefined || item.issuedQuantity == null || item.issuedQuantity == 0)){
	            		item.itemtotalAmount = ((item.rate != undefined ?(item.rate.toFixed(2) ? item.rate.toFixed(2) : 0):0) * (parseInt(item.quantityIssued) ? parseInt(item.quantityIssued) : (item.disabled == true && item.newQty) ? item.newQty :0));
	            	}
	            	else{
	            		item.itemtotalAmount = ((item.rate != undefined ?(item.rate.toFixed(2) ? item.rate.toFixed(2) : 0):0) * (parseInt(item.quantityIssued) ? parseInt(item.quantityIssued) : item.issuedQuantity ? item.issuedQuantity :0));
	            	} 
                }
                item.netAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (item.itemDiscount)));
                
                if(item.netAmount > 0 && item.packageConsumptionAmt > 0){
                	item.netAmount = item.netAmount - item.packageConsumptionAmt;
                }
                item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
                item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
            }
            else {
            	if($scope.autoPrescribedQty == false)
            		{
            		item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : item.issuedQuantity ? item.issuedQuantity :0));
            		}
            	else
            		{
            		if((item.quantityIssued == undefined || item.quantityIssued == null || item.quantityIssued == 0) && (item.issuedQuantity == undefined || item.issuedQuantity == null || item.issuedQuantity == 0)){
                		item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : item.newQty ? item.newQty :0));
                	}
            		}
            	if((item.quantityIssued == undefined || item.quantityIssued == null || item.quantityIssued == 0) && (item.issuedQuantity == undefined || item.issuedQuantity == null || item.issuedQuantity == 0)){
            		item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : item.newQty ? item.newQty :0));
            	}
            	else{
            		item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : item.issuedQuantity ? item.issuedQuantity :0));
            	}
                item.taxAmount = (((item.itemtotalAmount) - (item.patientClassAmount) - (item.itemDiscount)) * (item.taxPercentage / 100)).toFixed(2);
                item.netAmount = (isNaN(item.netAmount)?0:item.netAmount);
                item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
                item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
            }
            if(!item.isConsumed)
       	    {
            	if(item.creditPayable==0)
                {
            		if(item.netAmount==0)
                    {
            			item.selfPayable = item.selfPayable;
                    }
            		else
            		{
                		item.selfPayable = item.netAmount;
            		}
            		}
            	else
                {
            		item.selfPayable = item.selfPayable;
                } 
       	    }
        }
        
        var CopySubstituteItem = function (index, bill) {
            var item = bill[index];
            return {
                itemId: item.itemId,
                genericId: item.genericId,
                itemCode: item.itemCode,
                itemName: item.itemName,
                narcoticFlag: item.narcoticFlag,
                genericName: item.genericName,
                manufacturerName: item.manufacturerName,
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
                isMultiBatch:true,
                uoms:item.uoms,
                storageUnitName:item.storageUnitName
            }
        }
        
        
        //Ganesh
        var CalculateItemSum = function (itemList) 
        {
            $scope.totalSelfPayable = parseFloat(0);
            $scope.totalCreditPayable = parseFloat(0);
            angular.forEach(itemList, function (item, key) 
            {
                if ($scope.taxInclusive == true) 
                {
                    if(!item.itemDisabled && !item.disabled){
                        item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : 0));
                        //   item.netAmount = (((item.totalAmount) - (isNaN(item.discount)?0:item.discount)));
//                        if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                        {
//                         item.amount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) / (1 + (item.taxPercent != undefined && item.taxPercent != null ? item.taxPercent : item.taxPercentage) / 100)).toFixed(2);
//                         item.taxAmount = ((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) - item.amount;
//                        }
                        item.netAmount = item.netAmount;
                        item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                        item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
                        item.packageConsumptionAmt = (isNaN(item.packageConsumptionAmt)?0:item.packageConsumptionAmt);
                        
                        $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(item.selfPayable);
                        $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(item.creditPayable);
              
                        if (item.selectedItems && !item.itemDisabled) {
                            angular.forEach(item.selectedItems, function (sitem, key) {
                	            if(!sitem.disabled){ 
                                    sitem.totalAmount = ((sitem.rate ? sitem.rate : 0) * (sitem.quantityIssued ? sitem.quantityIssued : 0));
//                                    if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                                    {
//                                     item.amount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) / (1 + (item.taxPercent != undefined && item.taxPercent != null ? item.taxPercent : item.taxPercentage) / 100)).toFixed(2);
//                                     item.taxAmount = ((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) - item.amount;
//                                    }
                                    sitem.netAmount = sitem.netAmount;
                                    sitem.packageConsumptionAmt = (isNaN(sitem.packageConsumptionAmt)?0:sitem.packageConsumptionAmt);
                                    sitem.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
                                    sitem.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                                    
                                    $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(sitem.selfPayable);
                                    $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(sitem.creditPayable);
                                }
                            });
                        }
                    } else if(item.selectedItems && !item.disabled){
             	        angular.forEach(item.selectedItems, function (sitem, key) 
                        {
             		        if(!sitem.disabled){
                                sitem.totalAmount = ((sitem.rate ? sitem.rate : 0) * (sitem.quantityIssued ? sitem.quantityIssued : 0));
//                                if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                                {
//                                 item.amount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) / (1 + (item.taxPercent != undefined && item.taxPercent != null ? item.taxPercent : item.taxPercentage) / 100)).toFixed(2);
//                                 item.taxAmount = ((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) - item.amount;
//                                }
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
                        item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : 0));
//                        if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                        {
//                         item.taxAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * ((item.taxPercent != undefined && item.taxPercent != null ? item.taxPercent : item.taxPercentage) / 100));
//                        }
                        item.netAmount = item.netAmount;
                        item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                        item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
                        
                        $scope.totalSelfPayable = $scope.totalSelfPayable+ item.selfPayable;
                        $scope.totalCreditPayable = $scope.totalCreditPayable+ item.creditPayable;
              
                        if (item.selectedItems && !item.itemDisabled) {
                            angular.forEach(item.selectedItems, function (sitem, key) {
                	            if(!sitem.disabled){
                                    item.totalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : 0));
//                                    if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                                    {
//                                     item.taxAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * ((item.taxPercent != undefined && item.taxPercent != null ? item.taxPercent : item.taxPercentage) / 100));
//                                    }
                                    item.netAmount = sitem.netAmount;
                                    item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                                    item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
                                    
                                    $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable)+ parseFloat(item.selfPayable);
                                    $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable)+ parseFloat(item.creditPayable);
                	            }
                            });
                        }
                    }else if(item.selectedItems && !item.disabled){
            	        angular.forEach(item.selectedItems, function (sitem, key) 
                	    {
            		        if(!sitem.disabled){
                                item.totalAmount = ((sitem.rate ? sitem.rate : 0) * (sitem.quantityIssued ? sitem.quantityIssued : 0));
//                                if(item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null && item.packageConsumptionAmt>0)
//                                {
//                                 item.taxAmount = (((item.totalAmount) - (item.patientClassAmount) - (isNaN(item.itemDiscount) ? 0 : item.itemDiscount)-(!isNaN(item.packageConsumptionAmt) && item.packageConsumptionAmt!=undefined && item.packageConsumptionAmt!=null ? item.packageConsumptionAmt:0)) * ((item.taxPercent != undefined && item.taxPercent != null ? item.taxPercent : item.taxPercentage) / 100));
//                                }
                                item.netAmount = sitem.netAmount;
                                item.selfPayable = (isNaN(sitem.selfPayable)?0:sitem.selfPayable);
                                item.creditPayable = (isNaN(sitem.creditPayable)?0:sitem.creditPayable);
                                
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
        
        var ValidateUniqueBatchAndUom = function (item) {
            var isValid = true;

            if (item.substituteBtn == true) {
                item['isBatchValid'] = false;
                item['isUOMValid'] = false;
                var count=0;
                var items = self.model.pendingIssuesDetails.items.filter(function (detail) {
                	if (!detail.disabled && !detail.itemDisabled && detail.selectedBatch!=null) {
                		if(detail.selectedBatch){
                if(detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
                        && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
                        == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId)
                            {
                            count++;
                            }
                        if ( detail.selectedItems!=undefined && detail.selectedItems!=null) {
                            detail.selectedItems.filter(function (detail1) {
                                if (!detail1.disabled && !detail1.itemDisabled && detail1.selectedBatch!=null) {
                                    if(detail1.selectedBatch){
                                        if( detail1.itemId == item.itemId && item.selectedBatch && item.selectedUom
                                        && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail1.selectedBatch.batchId
                                        == item.selectedBatch.batchId && detail1.selectedUom.uomTypeId == item.selectedUom.uomTypeId)
                                            {
                                            count++;
                                            }
                                    }
                                }
                            });
                        }
                }
            }
                	else
                    {
                                if ( detail.selectedItems!=undefined && detail.selectedItems!=null) {
                                    detail.selectedItems.filter(function (detail1) {
                                        if (!detail1.disabled && !detail1.itemDisabled && detail1.selectedBatch!=null) {
                                            if(detail1.selectedBatch){
                                                if( detail1.itemId == item.itemId && item.selectedBatch && item.selectedUom
                                                && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail1.selectedBatch.batchId
                                                == item.selectedBatch.batchId && detail1.selectedUom.uomTypeId == item.selectedUom.uomTypeId)
                                                    {
                                                    count++;
                                                    }
                                            }
                                        }
                                    });
                                }
                    }
                	});
                var parentItem = self.model.pendingIssuesDetails.items.filter(function (parentItem) {
                    return parentItem.genericId == item.genericId;
                });
                self.model.test = [];
                var items = parentItem.filter(function (detail1) {
                	if(detail1.selectedItems != undefined){
                		self.model.test = [];
                    	self.model.test = angular.copy(detail1.selectedItems);
                    }
                });
                var items = self.model.test.filter(function (detail) {
              	    return detail.itemId == 
                    	item.itemId && item.selectedBatch && 
                    	item.selectedUom && 
                    	item.selectedUom.uomTypeId > 0 &&
                    	item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == 
                        item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId && detail.itemDisabled!= true;
                });
                
                if (items.length > 1 || count>1) {
                    isValid = false;
                    item['isParentBatchValid'] = true;
                    item['isParentUOMValid'] = true;
                    $scope.validateBatchAndUomFlag=true;
                    growl.error('There should not be same batch and same UOM for same item');
                }
            }
            else {
                item['isParentBatchValid'] = false;
                item['isParentUOMValid'] = false;

                console.log("self.model.pendingIssuesDetails.items",self.model.pendingIssuesDetails.items);
                var count=0;
                var items = self.model.pendingIssuesDetails.items.filter(function (detail) {
                	
                    if (!detail.disabled && !detail.itemDisabled && detail.selectedBatch!=null) {
                		if(detail.selectedBatch){
                          if(detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
                          && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
                          == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId)
                              {
                              count++;
                              }
                          if ( detail.selectedItems!=undefined && detail.selectedItems!=null) {
                              detail.selectedItems.filter(function (detail1) {
                                  if (!detail1.disabled && !detail1.itemDisabled && detail1.selectedBatch!=null) {
                                      if(detail1.selectedBatch){
                                          if( detail1.itemId == item.itemId && item.selectedBatch && item.selectedUom
                                          && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail1.selectedBatch.batchId
                                          == item.selectedBatch.batchId && detail1.selectedUom.uomTypeId == item.selectedUom.uomTypeId)
                                              {
                                              count++;
                                              }
                                      }
                                  }
                              });
                          }
                            return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom
                            && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId
                            == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                        }
                    }
                    else
                    {
                                if ( detail.selectedItems!=undefined && detail.selectedItems!=null) {
                                    detail.selectedItems.filter(function (detail1) {
                                        if (!detail1.disabled && !detail1.itemDisabled && detail1.selectedBatch!=null) {
                                            if(detail1.selectedBatch){
                                                if( detail1.itemId == item.itemId && item.selectedBatch && item.selectedUom
                                                && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail1.selectedBatch.batchId
                                                == item.selectedBatch.batchId && detail1.selectedUom.uomTypeId == item.selectedUom.uomTypeId)
                                                    {
                                                    count++;
                                                    }
                                            }
                                        }
                                    });
                                }
                    }
                });
                if (items.length > 1 || count>1) {
                    isValid = false;
                    item['isParentBatchValid'] = true;
                    item['isParentUOMValid'] = true;
                    $scope.validateBatchAndUomFlag=true;
                    item['isBatchValid'] = true;
                    item['isUOMValid'] = true;
                    growl.error('There should not be same batch and same UOM for same item');
                }
            }
            return isValid;
        }

        var CalculateQuantity = function (item, uom, uomList) {
            if (!!uom) {
                var pUom = $filter('filter')(uomList, {
                    uomTypeId: 1
                }, true)[0];

                var sUom = $filter('filter')(uomList, {
                    uomTypeId: 2
                }, true)[0];

                var dUom = $filter('filter')(uomList, {
                    uomTypeId: 3
                }, true)[0];

                if (uom.uomTypeId == 1) {
                    item.leaseQuantity = pUom.conversion * sUom.conversion * dUom.conversion * item.quantityIssued;
                } else if (uom.uomTypeId == 2) {
                    item.leaseQuantity = sUom.conversion * dUom.conversion * item.quantityIssued;
                } else if (uom.uomTypeId == 3) {
                    item.leaseQuantity = dUom.conversion * item.quantityIssued;
                }
            } else {
                item.leaseQuantity = 0;
            }
        }

        var AllocateQuantity = function (index, bill,parentIndex) {
            // if (!!bill && ValidateQuantity (bill)) {
            if (!!bill) {
            	var item  ;
            	if(parentIndex == undefined){
            		 item = bill.items[index];
            		  bill.items[index].selfPayable = item.netAmount == undefined ? 0 : item.netAmount ;
                      bill.items[index].creditPayable = 0;
            	}else{
            		 item = bill.items[parentIndex].selectedItems[index];
            	}
                item.selfPayable= item.netAmount == undefined ? 0 : item.netAmount ;
                item.creditPayable= 0;
              
                if(item.selectedBatch.uom!=undefined && item.selectedUom!=undefined){
                	CalculateQuantity(item, item.selectedUom, item.selectedBatch.uom);
                }
                if (CanAllocate(item) && (parseInt(item.quantityIssued) <= (item.stockAvailable!=undefined ? item.stockAvailable: (item.availableStockQuantity!=undefined) ? item.availableStockQuantity: item.stock)) && (item.newQty==undefined ? true:item.newQty >= parseInt(item.quantityIssued))) {
                	var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+ $cookies.getObject('cookieObject').storeId + "/"+$scope.userId+ "/" +item.itemId+"/"+ bill.id + "/" + (!!item.allocationIds ? item.allocationIds : "");
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
                            item.availableStockQuantity = response.data.object;
                            item.stockAvailable = response.data.object;
                            angular.forEach(item.batch, function (value4, index4) {          	
                                if(value4.batchId == item.selectedBatch.batchId){
                                    value4.availableStockQuantity = response.data.object;
                                }
                            });
                            if(item.selectedUom!=undefined && item.selectedUom!=null)
                        	{
                            ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
                        	}
                            if($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3){
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
                            $scope.activePackageListWithoutForceClose=[];
                			angular.forEach($scope.activePackageList, function (item, key)  {
                				if(item.isForceClose==1)
                					{
                					$scope.activePackageListWithoutForceClose.push(item);
                					}
                			});
                            if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0){
                                $scope.isValidated = true;
                            }
                            StorePreviousValue(item);
                        }
                        $rootScope.stopLoader();
                    },

                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.summary ? err.data.summary : err.data.message);
                        RestorePreviousValue(item);
                        item.itemtotalAmount = 0.0 ;
                        item.totalAmount = 0.0;
                        item.discountAmount = 0.0;
                        item.netAmount = 0.0;
                        item.quantityIssued = 0;
                    });
                } else {
                    $rootScope.stopLoader();	 
                    if (parseInt(item.quantityIssued) > item.quantityOrdered && parseInt(item.quantityIssued) < item.stockAvailable) {
                        growl.warning('Please enter quantity less than or equal to ordered quantity.');
                    }
                    if (parseInt(item.quantityIssued) > item.stockAvailable) {
                        growl.warning('Please enter quantity less than or equal to ordered quantity.');
                        growl.warning('Please enter quantity less than or equals to available stock.');
                    }

                    item.itemtotalAmount = 0.0 ;
                    item.totalAmount = 0.0;
                    item.discountAmount = 0.0;
                    item.netAmount = 0.0;
                    item.quantityIssued = 0;
                    StorePreviousValue(item);
                }
            }
        }

        var CanAllocate = function (item) {
            if (!!item.selectedBatch && !!item.selectedUom &&
                (!!item.quantityIssued) && !(item.prevQuantity == item.quantityIssued && item.quantityIssued == '')) {
                return true;
            }

            return false;
        }

        var StorePreviousValue = function (item) {
            item.prevBatch = item.selectedBatch;
            item.prevUom = item.selectedUom;
            item.prevQuantity = item.quantityIssued;

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

            item.quantityIssued = item.prevQuantity;

            // CalculateRate(item, item.selectedUom, item.uom);
        }

        $scope.confirmMessage = "Are you sure you want to Save?";

        var SavePopup = function (issue) {
              if(issue.items !== undefined && issue.items !== null && issue.items.length>0){
                  var checkLasa = false;
                  angular.forEach(issue.items, function(item){
                        if(item.isLasa !== undefined && item.isLasa !== null && item.isLasa === true){
                           checkLasa = true;
                        }
                  });

                  if(checkLasa){
                       checkLasa = false;
                      $scope.confirmMessage = "List contains LASA drugs. Are you sure you want to Save?";
                  }
                  else{
                    $scope.confirmMessage = "Are you sure you want to Save?";
                  }
              }
         var counter=0;
         var newList=[];
         angular.forEach(self.model.pendingIssuesDetails.items, function(d){
          if(d.statusId!=4 && d.quantityIssued!=undefined && d.quantityIssued!=null && d.quantityIssued!=0) {
                  counter++;
                  }
                  angular.forEach(d.selectedItems, function(sub){
                   if(d.statusId!=4 && sub.statusId!=4  && sub.quantityIssued!=undefined && sub.quantityIssued!=null && sub.quantityIssued!=0) {
                                    counter++;
                                    }
                  })
         });
         console.log(counter)
         console.log(self.model.pendingIssuesDetails.items.quantityIssued)
         if(counter==0){
         growl.warning('Please Enter Qty Dispensed' ,{
         title : 'Warning!'
         });
         return false;
         }


         var popup = angular.element('#confirmSave');
         popup.modal('show');
        }
        var ChangeItemStatus = function (item) { 
            if (item.quantityOrdered > (parseInt(item.quantityIssued) + parseInt(item.issuedQuantity))) {
                item.statusId = SetStatus('Partially Completed');
            } else if (item.quantityOrdered == (parseInt(item.quantityIssued) + parseInt(item.issuedQuantity))) {
                item.statusId = SetStatus('Completed');
            }
        }

        $scope.saveBtnDisabled=false;
        var SaveBill = function (bill) 
        {
            $scope.saveBtnDisabled=true;
            $scope.btnF = true;
            if($scope.NonFormularyAdded == false || $scope.servicesListPopup.length == 0){
    		  ActualSaveBill(bill);    
    	 }else{
    		 $scope.saveDataInPrescription(bill);
    	 }
    	

    }    
        
        var ActualSaveBill = function (bill) 
        {
        	$scope.saveBtnDisabled=true;
            // if (bill.statusId != 4) {
        	
        	$scope.doctorId = bill.doctorId;
            $scope.btnF = true;
            $scope.totalSelfPayable = 0.00;
            $scope.totalCreditPayable = 0.00;
            var statusFinalFlag =  false;
            if($scope.entitlementObj.paymentEntitlementTypeId == '3'||$scope.entitlementObj.paymentEntitlementTypeId == '2')
            {
                var found = $scope.paymentEntitleMentList.find(ar=>(ar.paymentEntitlementId==2||ar.paymentEntitlementId==3));
                if(found==undefined)
                {
                    growl.error('Please Apply Payment Entitlement.');
                    $scope.btnF = false;
                    $scope.saveBtnDisabled = false;
                    return false;
                }
            }
            if (ValidateBill(bill)) 
            {
                var URI = CONSTANTS.PHARMACY.IP.PENDING_ISSUES.SAVE_API;
                var data = {
                    // "screenId": 1,
                    "id": !!bill.id ? bill.id : undefined,
                    "prescriptionId": !!bill.prescriptionId ? bill.prescriptionId : undefined,
                    "patientId": !!bill.patientId ? bill.patientId : undefined,
                    "doctorId": !!bill.doctorId ? bill.doctorId : undefined,
                    // "storeId": !!bill.storeId ? bill.storeId : undefined,
                    "visitTypeId": !!bill.visitType ? bill.visitType : undefined,
                    "encounterId": !!bill.encounterId ? bill.encounterId : undefined,
                    "admissionId": !!bill.admissionId ? bill.admissionId : undefined,
                    //"remark": !!bill.remark ? bill.remark : undefined,
                    "remark": self.model.remark,
                    // "statusId": SetStatus('Completed'),
                    "discountTypeId": !!bill.discountType ? bill.discountType : undefined,
                    "discountCategoryId": !!bill.discountCatgeory.id ? bill.discountCatgeory.id : undefined,
                    "discount": !!bill.discount ? parseFloat(bill.discount).toFixed(2) : 0,
                    "totalAmount": !!bill.totalAmount ? parseFloat(bill.totalAmount).toFixed(2) : 0,
                    "totalDiscountAmount": !!bill.totalDiscountAmount ? parseFloat(bill.totalDiscountAmount).toFixed(2) : 0,
                    "netAmount": !!bill.netAmount ? parseFloat(bill.netAmount).toFixed(2) : 0,
                    "roundOff": !!bill.roundOff ? parseFloat(bill.roundOff).toFixed(2) : 0,
                    "taxAmount": !!bill.totalTaxAmount ? parseFloat(bill.totalTaxAmount).toFixed(2) : 0,
                    "saleTypeId": 1,
                    "createdBy": self.model.userId,
                    // "orderId": !!bill.orderId ? bill.orderId : undefined,
                    "totalSelfPayable":$scope.totalSelfPayable,
                    // "totalSelfPayable": !!bill.netAmount ? parseFloat(bill.netAmount).toFixed(2) : 0,
                	"totalCreditPayable":$scope.totalCreditPayable,
                	"consume" :false,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$cookies.getObject('cookieObject').storeId,
                    "atCopToSave":$scope.atCopToSave,
                    "isConsolidatedFlag":false 
                }

                $scope.totalOrderedQty=0;
                var validateQty = function(item){
                    var statusCount = 0;
                    
               		if(item.itemId!=null){
                        var totalSame = 0;				
                        if(item.isFromAssignPicker){			
                            if (item.pickedQuantity > parseInt(item.quantityIssued)) {
                                statusCount = 1
                            }                      
                        }else{			
                            angular.forEach(bill.items, function (item1, key1) 
                            {
                                if (!item1.disabled && !item1.itemDisabled && item1.selectedBatch!=null) 
                                {
                                    if(item1.quantityIssued !=undefined && item1.quantityIssued !=null && item1.quantityIssued > 0 && item1.quantityIssued !=""){
                                        if(item1.barcodeItem != true){
                                            if(item.id == item1.id)
                                            {           							
                                                if(item1.quantityIssued != undefined && item1.quantityIssued != null ){
                                                    totalSame += CalculateOrederOrIssuedQty(item1);
                                                }
                                                                    
                                                angular.forEach(item1.selectedItems, function (item2, key2) 
                                                {
                                                    if(!item2.itemDisabled){
                                                        totalSame += CalculateOrederOrIssuedQty(item2);
                                                    }
                                                });

                                            }
                                                        
                                            if((item.id == item1.parentIssueDetailsId) && (item1.parentIssueDetailsId != undefined))
                                            {
                                                totalSame +=  CalculateOrederOrIssuedQty(item1);
                                                                        
                                                angular.forEach(item1.selectedItems, function (item2, key2) 
                                                {
                                                    if(!item2.itemDisabled){
                                                        totalSame += CalculateOrederOrIssuedQty(item2);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                                else{
                                    if(item1.barcodeItem != true){
                                        if(item.id == item1.id)
                                        {           							
                                            if(item1.quantityIssued != undefined && item1.quantityIssued != null ){
                                                totalSame += CalculateOrederOrIssuedQty(item1);
                                            }
                                                                
                                            angular.forEach(item1.selectedItems, function (item2, key2) 
                                            {
                                                if(!item2.itemDisabled){
                                                    totalSame += CalculateOrederOrIssuedQty(item2);
                                                }
                                            });

                                        }
                                                            
                                        if((item.id == item1.parentIssueDetailsId) && (item1.parentIssueDetailsId != undefined))
                                        {
                                            totalSame +=  CalculateOrederOrIssuedQty(item1);
                                                                    
                                            angular.forEach(item1.selectedItems, function (item2, key2) 
                                            {
                                                if(!item2.itemDisabled){
                                                    totalSame += CalculateOrederOrIssuedQty(item2);
                                                }
                                            });
                                        }
                                    }
                                }
                            }); 
               					
                            if(item.barcodeItem != true){
                                if (item.quantityOrdered > totalSame) {
                                    statusCount = 1;
                                }
                            }
                            else{
                                item.statusId = 4;
                            }
               						   
                            if(item.checkNewItem==undefined){
                                var issuedQty =0;
                                if(item.barcodeItem != true){
                                    if(item.selectedUom !=undefined && item.selectedUom.uomTypeId !=undefined)
                                    {
                                        if(item.isThreeUOMApplicable == false){
                                            if(item.selectedUom.uomTypeId == 3)
                                            {
                                                issuedQty  = parseInt(item.quantityIssued);
                                            }else if(item.selectedUom.uomTypeId == 2)
                                            {
                                                var stripQty = item.uomListData[1].conversion * parseInt(item.quantityIssued);
                                                issuedQty  = stripQty;
                                            }else if(item.selectedUom.uomTypeId == 1)
                                            {
                                                var boxQty = item.uomListData[0].conversion *  item.uomListData[1].conversion * parseInt(item.quantityIssued);
                                                issuedQty  = boxQty;
                                            }  
                                        }
                                        else{
                                        	if(item.uom!=undefined && item.uom!=null)
                                            {
                                            if(item.uom[1] == undefined || item.uom[1] == null){
                                                if(item.selectedUom.uomTypeId == 3)
                                                {
                                                    issuedQty  = parseInt(item.quantityIssued);
                                                }else if(item.selectedUom.uomTypeId == 2)
                                                {
                                                    var stripQty = item.uoms[1].conversion * parseInt(item.quantityIssued);
                                                    issuedQty  = stripQty;
                                                }else if(item.selectedUom.uomTypeId == 1)
                                                {
                                                    var boxQty = item.uoms[0].conversion *  item.uoms[1].conversion * parseInt(item.quantityIssued);
                                                    issuedQty  = boxQty;
                                                }  
                                            }
                                            else{
                                                if(item.selectedUom.uomTypeId == 3)
                                                {
                                                    issuedQty  = parseInt(item.quantityIssued);
                                                }else if(item.selectedUom.uomTypeId == 2)
                                                {
                                                    var stripQty = item.uom[1].conversion * parseInt(item.quantityIssued);
                                                    issuedQty  = stripQty;
                                                }else if(item.selectedUom.uomTypeId == 1)
                                                {
                                                    var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(item.quantityIssued);
                                                    issuedQty  = boxQty;
                                                }  
                                            }
                                            }
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
                		  
                angular.forEach(bill.items, function (item, key) 
                {
                    
                    if (!item.disabled && !item.itemDisabled && item.selectedBatch!=null) 
                    {
                        if(item.quantityIssued !=undefined && item.quantityIssued !=null && item.quantityIssued > 0 && item.quantityIssued !=""){
                            var result = validateQty(item);
                            var status;
                            if (result) {
                                status = 'Partially Completed';
                                statusFinalFlag =  true;
                            } else if (!result) {
                                status = 'Completed';
                            }
                            
                            var billItem = {
                                "statusId": SetStatus(status),
                                "id": item.id,
                                "itemId": item.itemId,
                                "prescriptionDetailId": item.prescriptionDetailId,
                                // "orderDetailId": item.orderDetailId,
                                "batchId": item.selectedBatch.batchId,
                                "uomTypeId": item.selectedUom.uomTypeId,
                                "uomUnitId": item.selectedUom.uomUnitId,
                                "rate": item.rate,
                                //  ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
                                "quantity": Number(item.quantityIssued),
                                "leaseRate": item.selectedBatch.sellingPrice,
                                // ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
                                "leaseQuantity": item.leaseQuantity,
                                "totalAmount": item.itemtotalAmount,
                                // ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
                                "discountAmount": item.itemDiscount ? parseFloat(item.itemDiscount).toFixed(2) : 0.00,
                                // ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
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
                                "unitConsessionAmount": item.unitPatientClassAmount.toFixed(2),
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
                    if (item.selectedItems && !item.itemDisabled ) 
                    {
                        $scope.parentDetailId = item.parentDetailId != undefined ? item.parentDetailId : item.id;
                        angular.forEach(item.selectedItems, function (item, key) {
                            if(!item.itemDisabled && item.selectedBatch!=null){
                                if(item.quantityIssued !=undefined && item.quantityIssued !=null && item.quantityIssued > 0 && item.quantityIssued !=""){
                                    item.subItem = true;
                                    var result = validateQty(item);
                                    var status;
                                    if (result) {
                                        status = 'Partially Completed';
                                        statusFinalFlag =  true;
                                    } else if (!result) {
                                        status = 'Completed';
                                    }
                                    
                                    var billSubItem = {
                                        //"statusId":  SetStatus(status),
                                        "statusId":  4,
                                        "id": item.id,
                                        "itemId": item.itemId,
                                        "prescriptionDetailId": item.isFromAssignPicker == true ?item.prescriptionDetailId:null,
                                        "batchId": item.selectedBatch.batchId,
                                        "uomTypeId": item.selectedUom.uomTypeId,
                                        "uomUnitId": item.selectedUom.uomUnitId,
                                        "quantity": Number(item.quantityIssued),
                                        //"statusId": SetStatus(status),
                                        "rate": item.rate,
                                        //  ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
                                        "leaseRate": item.selectedBatch.sellingPrice,
                                        // ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
                                        "leaseQuantity": item.leaseQuantity,
                                        "totalAmount": item.itemtotalAmount,
                                        // ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
                                        "discountAmount": item.itemDiscount ? parseFloat(item.itemDiscount).toFixed(2) : 0.00,
                                        // ? parseFloat(item.itemDiscount).toFixed(2) : 0.00
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
                                        "unitConsessionAmount": item.unitPatientClassAmount.toFixed(2),
                                        "parentIssueDetailsId" : $scope.parentDetailId != undefined ? $scope.parentDetailId : ($scope.parentIssueDetailsId!=undefined && $scope.parentIssueDetailsId!=null ? $scope.parentIssueDetailsId:null),
                                        "isFromAssignPicker" : item.isFromAssignPicker,	
                                        "consumedItemId" : item.consumedItemId,
                                        "consumedPcategroyId" : item.consumedPcategroyId,
                                        "parentItemId": item.parentItemId,
                                        "batchAllocationId":item.allocationId
                                    }                           
                                    var selfpayable = item.selfPayable ? parseFloat(item.selfPayable) : 0.00;
                                    var creditpayable = item.creditPayable ? parseFloat(item.creditPayable) : 0.00;
                                    $scope.totalSelfPayable = parseFloat($scope.totalSelfPayable) + selfpayable;
                                    $scope.totalCreditPayable = parseFloat($scope.totalCreditPayable) + creditpayable;
                                    data.items.push(billSubItem);
                                }
                            }
                        });
                    }   
                });
                var billStatus='Completed';
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
                console.log(data)
                var totalOrderedQty=0;
                var totalIssuedQty=0;
                var orderedQtyCount=0;
                var issuedQtyCount=0;
                angular.forEach(bill.items, function (item, key1) 
                {
                	var newUOM = [];
                    if(item.isThreeUOMApplicable == false){
                     if(item.uomListData!=undefined && item.uomListData!=null)
                           {
                        item.uomListData.find(function (value) {
                            if (!!value.uomTypeId) {
                                newUOM.push(value);
                            }
                        });
                            }
                    }
                    else{
                    	if(item.uom!=undefined && item.uom!=null)
                    		{
                        item.uom.find(function (value) {
                            if (!!value.uomTypeId) {
                                newUOM.push(value);
                            }
                        });
                    		}
                    }
             	    if(item.disabled!=undefined && item.disabled!=null && !item.disabled)
                    {
             	    	if(newUOM==undefined || newUOM==null || newUOM.length==0)
                		{
             	    		newUOM=item.selectedBatch!=undefined && item.selectedBatch!=null ? item.selectedBatch.uom:item.uom;
                		}
                	    if(item.selectedUom != null){
                            var uomDetails = item.selectedUom.uomDetails.split(' ')[0];
                      	    if (uomDetails == "P") {
                               item.finalQuantitySave = Math.ceil(item.quantityIssued * (((newUOM[0]!=undefined && newUOM[0]!=null) ? newUOM[0].conversion : 1) * ((newUOM[1]!=undefined && newUOM[1]!=null) ? newUOM[1].conversion : 1) * ((newUOM[2]!=undefined && newUOM[2]!=null) ? newUOM[2].conversion : 1)));
                            } else if (uomDetails == "S") {
                               item.finalQuantitySave = Math.ceil(item.quantityIssued * (((newUOM[1]!=undefined && newUOM[1]!=null) ? newUOM[1].conversion : 1 ) * ((newUOM[2]!=undefined && newUOM[2]!=null) ? newUOM[2].conversion :1 )));
                            } else if (uomDetails == "D") {
                               item.finalQuantitySave = Math.ceil(item.quantityIssued * ((newUOM[2]!=undefined && newUOM[2]!=null) ? newUOM[2].conversion : 1 ));
                            }
                        }
                	    if(!item.parentIssueDetailsId)
                    	{
                            if(!item.isMultiBatch)
                            {
                            	if(item.quantityOrdered!=undefined){
                            		totalOrderedQty=totalOrderedQty+(item.finalOrderedQty!=undefined && item.finalOrderedQty!=null ? item.finalOrderedQty : 0);
                                    orderedQtyCount++
                            	}
                                
                            }
                    	}
                        if(item.barcodeItem != true){
                            if(!item.onlySubButtonDisable || item.onlySubButtonDisable==undefined)
                            {
                            	if(item.finalQuantitySave>item.finalOrderedQty)
                            	{
                            		item.finalQuantitySave=item.finalOrderedQty;
                            	}
                                totalIssuedQty=totalIssuedQty+(item.finalQuantitySave!=undefined && item.finalQuantitySave!=null && item.finalQuantitySave!="" ?item.finalQuantitySave:0);
                                if(totalIssuedQty>0 && !item.isMultiBatch && item.finalQuantitySave!=undefined && item.finalQuantitySave!=null && item.finalQuantitySave!="")
                                	{
                                	 if(item.disabled!=undefined && item.disabled!=null && !item.disabled)
                                     {
                                	issuedQtyCount++;
                                     }
                                	}
                            }
                        }
                    }
                	angular.forEach(item.selectedItems, function (sub, key1) 
                    {
                		var newUOM = [];
                        if(sub.uom[1] == undefined || sub.uom[1] == null){
                            sub.uoms.find(function (value) {
                                if (!!value.uomTypeId) {
                                    newUOM.push(value);
                                }
                            });
                        } 
                        else{
                            if(sub.uom[2] == undefined || sub.uom[2] == null){
                            	if(sub.uomListData[2] == undefined){
                            		sub.uoms.find(function (value) {
                                        if (!!value.uomTypeId) {
                                            newUOM.push(value);
                                        }
                                    });
                            	}
                            	else{
                            		sub.uomListData.find(function (value) {
                                        if (!!value.uomTypeId) {
                                            newUOM.push(value);
                                        }
                                    });
                            	}
                            }
                            else{
                                sub.uom.find(function (value) {
                                    if (!!value.uomTypeId) {
                                        newUOM.push(value);
                                    }
                                });
                            }
                        }
                		if(item.disabled!=undefined && item.disabled!=null)
                        {
                			if(newUOM==undefined || newUOM==null || newUOM.length==0)
                    		{
                 	    		newUOM=sub.selectedBatch.uom;
                    		}
//                			if(!sub.isMultiBatch)
//                    		{
                				if(sub.isSelected)
                                {
                                    if(sub.selectedUom != null){
                                        var uomDetails = sub.selectedUom.uomDetails.split(' ')[0];
                                        if (uomDetails == "P") {
                                            sub.finalQuantitySave = Math.ceil(sub.quantityIssued * (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                                        } else if (uomDetails == "S") {
                                            sub.finalQuantitySave = Math.ceil(sub.quantityIssued * (newUOM[1].conversion * newUOM[2].conversion));
                                        } else if (uomDetails == "D") {
                                            sub.finalQuantitySave = Math.ceil(sub.quantityIssued * (newUOM[2].conversion));
                                        }
                                    }
                                    if(sub.finalQuantitySave>sub.finalOrderedQty)
                                    	{
                                    	sub.finalQuantitySave=sub.finalOrderedQty;
                                    	}
                                    totalIssuedQty=totalIssuedQty+(sub.finalQuantitySave!=undefined && sub.finalQuantitySave!=null && sub.finalQuantitySave!="" ?sub.finalQuantitySave:0);
                                    if(totalIssuedQty>0 && !sub.isMultiBatch && sub.finalQuantitySave!=undefined && sub.finalQuantitySave!=null && sub.finalQuantitySave!="")
                                    	{
                                        if(item.disabled!=undefined && item.disabled!=null && item.disabled)
                                        {
                                    	issuedQtyCount++;
                                        }
                                    	}
                                }
//                            }
                        }
                		if(item.disabled!=undefined && item.disabled!=null && item.disabled && !item.isMultiBatch)
                        {
                			if(!sub.isMultiBatch)
                    		{
                				if(sub.isSelected)
                                {
                        			totalOrderedQty=totalOrderedQty+(sub.finalOrderedQty!=undefined && sub.finalOrderedQty!=null ? sub.finalOrderedQty : 0);
                        			orderedQtyCount++;
                                }
                    		}
                        }
                    })
                })
                if(totalIssuedQty >= totalOrderedQty && issuedQtyCount >= $scope.orderListCount)
                {
                    billStatus ='Completed';
                }
                else
                {
                    billStatus ='Partially Completed';
                }
                data.statusId=SetStatus(billStatus);
                if(data.items.length==0)
                	{
                	growl.error('Please Enter Quantity');
                	$scope.saveBtnDisabled=false;
                    $scope.btnF = false;
                	return false;
                	}
                var URI1 = BASE_URL + ROOT_URL + "/api/pharmacy/ip/pending/issues/pendingIssueStatus/0";
                GenericService.serviceAction(METHOD_POST,URI1, data).then(function(response) {
    			    if (response.data.object != null && response.data.object == 4)
    				{
                        ClearFields(bill);
                        $scope.orderQtyTotalSum=0;
                        growl.error(response.data.message);
                        NavigateToPendingIssues();
                        $rootScope.stopLoader();
                        $scope.btnF = false;
    				}
    			    else {
		                $rootScope.startLoader();
		                GenericService.serviceAction("POST", URI, data).then(
		                function (response) {
		                    ClearFields(bill);
		                    $scope.orderQtyTotalSum=0;
		                    growl.success('Pending issue bill successfully saved');
		                    NavigateToPendingIssues();
		                    $rootScope.stopLoader();
		                    $scope.btnF = false;
		                    PrintPendingIssuesDetailsReport();
		                },
		                function (err) {
		                    $rootScope.stopLoader();
		                    $scope.btnF = false;
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
		                                            if(s.selectedBatch != undefined && s.selectedBatch != null && s.selectedBatch != "" && s.selectedBatch.batchNo == e.batchNo){
		                                                s.flag = true;
		                                                s.summary = "Enough stock not available";
		                                                s.stockAvailable = e.currentStock;
		                                                $scope.saveBtnDisabled=false;
		                                            }
		                                        }
		                                    });
		                                }
		              
		                                if(d.flag == false){
		                                    d.summary = '';
		                                    if(d.selectedBatch != undefined && d.selectedBatch != null && d.selectedBatch != "" && d.selectedBatch.batchNo == e.batchNo){
												d.summary = "Enough stock not available";
		                                        d.stockAvailable = e.currentStock;
		                                        $scope.saveBtnDisabled=false;
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
		                        else if(err.data!= undefined && err.data!= null){
		                        	 $rootScope.stopLoader();
		                        	ClearFields(bill);
		                            $scope.orderQtyTotalSum=0;
		                            growl.error(err.data);
		                            NavigateToPendingIssues();
		                            $scope.btnF = false;
		                        }else{
		                            growl.error('Error while saving pending issue bill.');
		                        }
		                    }
		                });
    				}
                });
            }
        }

        $scope.checkDuplicatePriority = function(item,idx,priorityVal)
        {
            var newDataList = [];
            var le = $scope.paymentEntitleMentList.length;
            var i = 0;
            angular.forEach($scope.paymentEntitleMentList, function(v) {
                if (idx != i) {
                    /*if(v.priority > 1){
                    v.priority = v.priority - 1;
                    }*/
                    newDataList.push(v);
                }
                i++;
            });
            
            //$scope.paymentEntitleMentList = newDataList;
            
            var found = newDataList.find(ar=>ar.priority==priorityVal);
            if(found == undefined)
            {
                //return false;
            }
            else
            {
                $scope.paymentEntitleMentList[idx].priority = 0;
                growl.warning("You are aleady assign "+priorityVal+" priority",{
                    title : "WARNING"
                });
                return;
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
            bill.remark = self.model.remark;
            bill.netAmount = null;
            bill.items = [];
        }

        var ValidateBatch = function (item) {
            var isValid = true;
            if(item.quantityIssued){
                if(item.selectedBatch != null){
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
            if(item.selectedUom !=null){
                if (!item.selectedUom) {
                    item['isUOMValid'] = true;
                    growl.error("Please Select UOM");
                    isValid = false;
                } else {
                    ValidateUniqueBatchAndUom(item)
                }
            }
            return isValid;
        }

        
        var CalculateOrederOrIssuedQty = function(item){
       	    var issuedQty = 0;
            if(!item.quantityIssued){
                item.quantityIssued = 0;
            }
       	    var quantityIssued = item.quantityIssued != undefined ? item.quantityIssued : 0;
       	    if(item.selectedUom !=undefined && item.selectedUom.uomTypeId !=undefined)
            {
                if(item.isThreeUOMApplicable == false){
                    if(item.selectedUom.uomTypeId == 3)
                    {
                        issuedQty  = parseInt(quantityIssued);   
                    }
                    else if(item.selectedUom.uomTypeId == 2)
                    {
                        var stripQty = item.uomListData[1].conversion * parseInt(quantityIssued);
                        issuedQty  = stripQty;    
                    }
                    else if(item.selectedUom.uomTypeId == 1)
                    {
                        if(item.uomListData[0] == undefined || item.uomListData[0] == null){
                            var boxQty = item.uomListData[1].conversion *  item.uomListData[2].conversion * parseInt(quantityIssued);
                            issuedQty  = boxQty;
                        }
                        else{
                            var boxQty = item.uomListData[0].conversion *  item.uomListData[1].conversion * parseInt(quantityIssued);
                            issuedQty  = boxQty;
                        }
                    }
                }
                else{
                	if(item.uom!=undefined && item.uom!=null)
                    {
                    if(item.uom[1] == undefined || item.uom[1] == null){
                        if(item.selectedUom.uomTypeId == 3)
                        {
                            issuedQty  = parseInt(quantityIssued);   
                        }
                        else if(item.selectedUom.uomTypeId == 2)
                        {
                            var stripQty = item.uoms[1].conversion * parseInt(quantityIssued);
                            issuedQty  = stripQty;    
                        }
                        else if(item.selectedUom.uomTypeId == 1)
                        {
                            var boxQty = item.uoms[0].conversion *  item.uoms[1].conversion * parseInt(quantityIssued);
                            issuedQty  = boxQty;
                        }
                    }   
                    else{
                        if(item.selectedUom.uomTypeId == 3)
                        {
                            issuedQty  = parseInt(quantityIssued);   
                        }
                        else if(item.selectedUom.uomTypeId == 2)
                        {
                            var stripQty = item.uom[1].conversion * parseInt(quantityIssued);
                            issuedQty  = stripQty;    
                        }
                        else if(item.selectedUom.uomTypeId == 1)
                        {
                            var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(quantityIssued);
                            issuedQty  = boxQty;
                        }
                    }
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
        var ValidateQuantity = function (parentItem , parentIndex) {	
            var isValid = true;     
            var count = 0;
            parentItem.substituteButtonFlag1=false;
            if(parseInt(parentItem.quantityIssued)>=parseInt((parentItem.newQty!=undefined && parentItem.newQty!=null ? parentItem.newQty :parentItem.quantityOrdered)))
            {
            	parentItem.quantityIssuedFlag =true;      
            }
            else
            {
            	parentItem.quantityIssuedFlag =false;      
            }
            if(parentItem.quantityIssued == NaN || parentItem.quantityIssued == 0 || parentItem.quantityIssued == undefined)
            	{
            	parentItem.creditPayable=0;
            	parentItem.selfPayable=0;
            	}
            $scope.substituteButtonFlag = false;
            if (!!parentItem) {
                angular.forEach(self.model.pendingIssuesDetails.items, function(d){
                    if(d.quantityIssued){
                        count++;
                    }
                    d["substituteButtonFlag1"]=false;
                    if(d.newQty==parseInt(d.quantityIssued)){
                    	d["substituteButtonFlag1"]=true;
                        //$scope.substituteButtonFlag = true;
                    }
                });
                if(count == 0){
                    if (!String(parentItem.quantityIssued)) {
                        parentItem['isQuantityValid'] = true;
                        growl.error("Please enter quantity");
                        parentItem.substituteButtonFlag1=false;
                        parentItem.disablePlusMinus=false;
                        isValid = false;
                        return false;
                    }
                    else if(parentItem.quantityIssued == NaN || parentItem.quantityIssued == 0 || parentItem.quantityIssued == undefined){
                    	if(parentItem.disabled != true){
                    		parentItem['isQuantityValid'] = true;
                            growl.error("Please enter quantity");
                            parentItem.substituteButtonFlag1=false;
                            parentItem.disablePlusMinus=false;
                            isValid = false;
                            return false;
                    	}
                    }
                    else if (parseInt(parentItem.quantityIssued) > parentItem.stockAvailable) {
                        parentItem.isParentQuantityValid = true;
                        parentItem.substituteButtonFlag1=false;
                        parentItem.quantityIssuedFlag =false;  
                        growl.error("Entered Quantity is exceeding stock available quantity");
                        isValid = false;
                        return false;
                    }
                   /* else if(parentItem.quantityIssued>parentItem.newQty)
                	{
                    	parentItem.substituteButtonFlag1=false;
                        growl.error("Entered Quantity is exceeding ordered quantity");
                        isValid = false;
                        parentItem.quantityIssued=0;
                        parentItem.itemtotalAmount = 0;
                        parentItem.itemDiscount = 0;
                        parentItem.taxAmount= 0;
                        parentItem.netAmount= 0;
                        parentItem.roundOff=0;
                	}*/
                    else {
                        parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                        isValid = true;
                    }
                }
                else if(parentItem.quantityIssued>parentItem.stockAvailable)
            	{
                	parentItem.substituteButtonFlag1=false;
                	parentItem.quantityIssuedFlag =false;  
            	growl.error("Entered Quantity is exceeding stock available quantity");
                isValid = false;
                parentItem.quantityIssued=0;
                parentItem.itemtotalAmount = 0;
                parentItem.itemDiscount = 0;
                parentItem.taxAmount= 0;
                parentItem.netAmount= 0;
                parentItem.roundOff=0;
            	}
                //old code start
                if (parentItem.quantityIssued < 0) {
                    parentItem['isQuantityValid'] = true;
                    growl.error("Please enter quantity greater than zero");
                    isValid = false;
                    return false;
                }

                /*   if (parentItem.newQty < parentItem.quantityIssued) {

            	    parentItem['isQuantityValid'] = true;
                    isValid = false;
                    growl.error('Entered quantity is exceeding the max orderd quantity');
                }
                */
                //old end 
      	        $scope.quantityIssuedNew = 0;            
                if(parentItem.isFromAssignPicker){
                	if(parentItem.uomListData==undefined || parentItem.uomListData==null || parentItem.uomListData.length==0)
            		{
            		parentItem.uomListData=parentItem.selectedBatch.uom;
            		}
                    if(parentItem.isThreeUOMApplicable == false){
                        if(parentItem.selectedUom.uomTypeId == 3)
                        {
                            $scope.quantityIssuedNew  = parentItem.quantityIssued;
                            if (parseInt(parentItem.quantityIssued) > parentItem.newPickedQty) {
                                parentItem.isParentQuantityValid = true;
                                parentItem.substituteButtonFlag1=false;
                                growl.error("Please enter quantity less than or equal to Picked quantity");
                                parentItem.quantityIssued = "0";
                                parentItem.itemtotalAmount = "0";
                                parentItem.itemDiscount = "0";
                                parentItem.taxAmount= "0";
                                parentItem.netAmount= "0";
                                parentItem.roundOff="0";
                                Calculate(parentItem, self.model.pendingIssuesDetails);
                                isValid = false;
                            } else {
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                            }  
                        } 
                        else  if(parentItem.selectedUom.uomTypeId == 2)
                        {
                            var stripQty = parentItem.uomListData[1].conversion * parseInt(parentItem.quantityIssued);
                            $scope.quantityIssuedNew  = stripQty;
                            
                            if(parseInt(parentItem.quantityIssued) > parentItem.newPickedQty){
                                parentItem.isParentQuantityValid = true;
                                parentItem.substituteButtonFlag1=false;
                                growl.error("Please enter quantity less than or equal to Picked quantity");
                                parentItem.quantityIssued = "0";
                                parentItem.itemtotalAmount = "0";
                                parentItem.itemDiscount = "0";
                                parentItem.taxAmount= "0";
                                parentItem.netAmount= "0";
                                parentItem.roundOff="0";
                                Calculate(parentItem, self.model.pendingIssuesDetails);
                                isValid = false;
                            }else{
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                            }      
                        }  
                        else  if (parentItem.selectedUom.uomTypeId == 1)
                        {
                            var boxQty = parentItem.uomListData[1].conversion * parentItem.uomListData[2].conversion * parseInt(parentItem.quantityIssued);
                            $scope.quantityIssuedNew  =  boxQty;
                            
                            if (parseInt(parentItem.quantityIssued) > parentItem.newPickedQty) {
                                parentItem.isParentQuantityValid = true;
                                parentItem.substituteButtonFlag1=false;
                                growl.error("Please enter quantity less than or equal to Picked quantity");
                                parentItem.quantityIssued = "0";
                                parentItem.itemtotalAmount = "0";
                                parentItem.itemDiscount = "0";
                                parentItem.taxAmount= "0";
                                parentItem.netAmount= "0";
                                parentItem.roundOff="0";
                                Calculate(parentItem, self.model.pendingIssuesDetails);
                                isValid = false;
                            }else {
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                            }   
                        }
                    }
                    else{
                        if(parentItem.selectedUom.uomTypeId == 3)
                        {
                            $scope.quantityIssuedNew  = parentItem.quantityIssued;
                            if (parseInt(parentItem.quantityIssued) > parentItem.newPickedQty) {
                                parentItem.isParentQuantityValid = true;
                                parentItem.substituteButtonFlag1=false;
                                growl.error("Please enter quantity less than or equal to Picked quantity");
                                parentItem.quantityIssued = "0";
                                parentItem.itemtotalAmount = "0";
                                parentItem.itemDiscount = "0";
                                parentItem.taxAmount= "0";
                                parentItem.netAmount= "0";
                                parentItem.roundOff="0";
                                Calculate(parentItem, self.model.pendingIssuesDetails);
                                isValid = false;
                            } else {
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                            }  
                        } 
                        else  if(parentItem.selectedUom.uomTypeId == 2)
                        {
                            var stripQty = parentItem.uom[1].conversion * parseInt(parentItem.quantityIssued);
                            $scope.quantityIssuedNew  = stripQty;
                            
                            if(parseInt(parentItem.quantityIssued) > parentItem.newPickedQty){
                                parentItem.isParentQuantityValid = true;
                                parentItem.substituteButtonFlag1=false;
                                growl.error("Please enter quantity less than or equal to Picked quantity");
                                parentItem.quantityIssued = "0";
                                parentItem.itemtotalAmount = "0";
                                parentItem.itemDiscount = "0";
                                parentItem.taxAmount= "0";
                                parentItem.netAmount= "0";
                                parentItem.roundOff="0";
                                Calculate(parentItem, self.model.pendingIssuesDetails);
                                isValid = false;
                            }else{
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                            }      
                        }  
                        else  if (parentItem.selectedUom.uomTypeId == 1)
                        {
                            var boxQty = parentItem.uom[1].conversion * parentItem.uom[2].conversion * parseInt(parentItem.quantityIssued);
                            $scope.quantityIssuedNew  =  boxQty;
                            
                            if (parseInt(parentItem.quantityIssued) > parentItem.newPickedQty) {
                                parentItem.isParentQuantityValid = true;
                                parentItem.substituteButtonFlag1=false;
                                growl.error("Please enter quantity less than or equal to Picked quantity");
                                parentItem.quantityIssued = "0";
                                parentItem.itemtotalAmount = "0";
                                parentItem.itemDiscount = "0";
                                parentItem.taxAmount= "0";
                                parentItem.netAmount= "0";
                                parentItem.roundOff="0";
                                Calculate(parentItem, self.model.pendingIssuesDetails);
                                isValid = false;
                            }else {
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                            }   
                        }
                    }
                }else if(!parentItem.isFromAssignPicker){
                	if(parentItem.uomListData==undefined || parentItem.uomListData==null || parentItem.uomListData.length==0)
            		{
            		parentItem.uomListData=parentItem.selectedBatch.uom;
            		}
                    if(parentItem.isThreeUOMApplicable == false){
                        if(parentItem.selectedUom){
                            if(parentItem.selectedUom.uomTypeId == 3)
                            {
                                $scope.quantityIssuedNew  = parentItem.quantityIssued;
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                                /*if (parseInt(parentItem.quantityIssued) > parentItem.quantityOrdered) {
                                    parentItem.isParentQuantityValid = true;
                                    parentItem.substituteButtonFlag1=false;
                                    growl.error("Please enter quantity less than or equal to ordered quantity");
                                    parentItem.quantityIssued = "0";
                                    parentItem.itemtotalAmount = "0";
                                    parentItem.itemDiscount = "0";
                                    parentItem.taxAmount= "0";
                                    parentItem.netAmount= "0";
                                    parentItem.roundOff="0";
                                    isValid = false;
                                    Calculate(parentItem, self.model.pendingIssuesDetails);
                                } else {
                                    parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                    isValid = true;
                                }  */
                            } 
                            else  if(parentItem.selectedUom.uomTypeId == 2)
                            {
                                var stripQty = parentItem.uomListData[1].conversion * parseInt(parentItem.quantityIssued);
                                $scope.quantityIssuedNew  = stripQty;
                               
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                                
                              /*  if(parseInt(parentItem.quantityIssued) > parentItem.quantityOrdered){
                                    parentItem.isParentQuantityValid = true;
                                    parentItem.substituteButtonFlag1=false;
                                    growl.error("Please enter quantity less than or equal to ordered quantity");
                                    parentItem.quantityIssued = "0";
                                    parentItem.itemtotalAmount = "0";
                                    parentItem.itemDiscount = "0";
                                    parentItem.taxAmount= "0";
                                    parentItem.netAmount= "0";
                                    parentItem.roundOff="0";
                                    Calculate(parentItem, self.model.pendingIssuesDetails);
                                    isValid = false;
                                }else{
                                    parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                    isValid = true;
                                }    */  
                            }  
                            else  if (parentItem.selectedUom.uomTypeId == 1)
                            {
                            	if(parentItem.uomListData[2] == undefined){
                            		var boxQty = parentItem.uoms[1].conversion * parentItem.uoms[2].conversion * parseInt(parentItem.quantityIssued);
                                    $scope.quantityIssuedNew  =  boxQty;
                            	}
                            	else{
                            		var boxQty = parentItem.uomListData[1].conversion * parentItem.uomListData[2].conversion * parseInt(parentItem.quantityIssued);
                                    $scope.quantityIssuedNew  =  boxQty;
                            	}
                            
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                               /* if (parseInt(parentItem.quantityIssued) > parentItem.quantityOrdered) {
                                    parentItem.isParentQuantityValid = true;
                                    parentItem.substituteButtonFlag1=false;
                                    growl.error("Please enter quantity less than or equal to ordered quantity");
                                    parentItem.quantityIssued = "0";
                                    parentItem.itemtotalAmount = "0";
                                    parentItem.itemDiscount = "0";
                                    parentItem.taxAmount= "0";
                                    parentItem.netAmount= "0";
                                    parentItem.roundOff="0";
                                    Calculate(parentItem, self.model.pendingIssuesDetails);
                                    isValid = false;
                                }else {
                                    parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                    isValid = true;
                                }   */
                            } 
                        }
                    }
                    else{
                    	if(parentItem.uom==undefined || parentItem.uom==null || parentItem.uom.length==0)
                    		{
                    		parentItem.uom=parentItem.selectedBatch.uom;
                    		}
                        if(parentItem.selectedUom){
                            if(parentItem.selectedUom.uomTypeId == 3)
                            {
                                $scope.quantityIssuedNew  = parentItem.quantityIssued;
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                              /*  if (parseInt(parentItem.quantityIssued) > parentItem.quantityOrdered) {
                                    parentItem.isParentQuantityValid = true;
                                    parentItem.substituteButtonFlag1=false;
                                    growl.error("Please enter quantity less than or equal to ordered quantity");
                                    parentItem.quantityIssued = "0";
                                    parentItem.itemtotalAmount = "0";
                                    parentItem.itemDiscount = "0";
                                    parentItem.taxAmount= "0";
                                    parentItem.netAmount= "0";
                                    parentItem.roundOff="0";
                                    isValid = false;
                                    Calculate(parentItem, self.model.pendingIssuesDetails);
                                } else {
                                    parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                    isValid = true;
                                }  */
                            } 
                            else  if(parentItem.selectedUom.uomTypeId == 2)
                            {
                                if(parentItem.uom[1] == undefined || parentItem.uom[1] == null){
                                    var stripQty = parentItem.uoms[1].conversion * parseInt(parentItem.quantityIssued);
                                    $scope.quantityIssuedNew  = stripQty;
                                }
                                else{
                                    var stripQty = parentItem.uom[1].conversion * parseInt(parentItem.quantityIssued);
                                    $scope.quantityIssuedNew  = stripQty;
                                }
                               
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                                /*if(parseInt(parentItem.quantityIssued) > parentItem.quantityOrdered){
                                    parentItem.isParentQuantityValid = true;
                                    parentItem.substituteButtonFlag1=false;
                                    growl.error("Please enter quantity less than or equal to ordered quantity");
                                    parentItem.quantityIssued = "0";
                                    parentItem.itemtotalAmount = "0";
                                    parentItem.itemDiscount = "0";
                                    parentItem.taxAmount= "0";
                                    parentItem.netAmount= "0";
                                    parentItem.roundOff="0";
                                    Calculate(parentItem, self.model.pendingIssuesDetails);
                                    isValid = false;
                                }else{
                                    parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                    isValid = true;
                                } */     
                            }  
                            else  if (parentItem.selectedUom.uomTypeId == 1)
                            {
                                if(parentItem.uom[1] == undefined || parentItem.uom[1] == null){
                                    var boxQty = parentItem.uoms[0].conversion * parentItem.uoms[1].conversion * parseInt(parentItem.quantityIssued);
                                    $scope.quantityIssuedNew  =  boxQty;
                                }
                                else{
                                    var boxQty = parentItem.uom[1].conversion * parentItem.uom[2].conversion * parseInt(parentItem.quantityIssued);
                                    $scope.quantityIssuedNew  =  boxQty;
                                }
                            
                                parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                isValid = true;
                              /*  if (parseInt(parentItem.quantityIssued) > parentItem.quantityOrdered) {
                                    parentItem.isParentQuantityValid = true;
                                    parentItem.substituteButtonFlag1=false;
                                    growl.error("Please enter quantity less than or equal to ordered quantity");
                                    parentItem.quantityIssued = "0";
                                    parentItem.itemtotalAmount = "0";
                                    parentItem.itemDiscount = "0";
                                    parentItem.taxAmount= "0";
                                    parentItem.netAmount= "0";
                                    parentItem.roundOff="0";
                                    Calculate(parentItem, self.model.pendingIssuesDetails);
                                    isValid = false;
                                }else {
                                    parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
                                    isValid = true;
                                } */  
                            } 
                        }
                    } 
                }
            
                if( isValid == true){
                    var finalOrdQty = 0;
          	        var totalIssuedQty = 0;
          	  
          	        if (parentIndex == undefined) {
                        angular.forEach(self.model.pendingIssuesDetails.items, function (value, index) {
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
                        if(self.model.pendingIssuesDetails.items[parentIndex].disabled == undefined){
                            totalIssuedQty += CalculateOrederOrIssuedQty(self.model.pendingIssuesDetails.items[parentIndex]);
                        }
       	                	
                        finalOrdQty = self.model.pendingIssuesDetails.items[parentIndex].finalOrderedQty;
                        if(self.model.pendingIssuesDetails.items[parentIndex].selectedItems != null && self.model.pendingIssuesDetails.items[parentIndex].selectedItems.length>0){                 		
                            angular.forEach(self.model.pendingIssuesDetails.items[parentIndex].selectedItems, function (selectValue, selectIndex) {		
                                totalIssuedQty += CalculateOrederOrIssuedQty(selectValue);
                            });
                        }
                    }
          	        $scope.totalQuantityIssued=0;
          	        $scope.subNewQty=0;
          	        $scope.subIssuedQty=0;
          	        $scope.mainNewQty=0;
        	        $scope.mainIssuedQty=0;
          	        angular.forEach(self.model.pendingIssuesDetails.items, function (value, index) {
          		        if(parentItem.parentItemId)
          			    {
          			        if(value.itemId == parentItem.parentItemId && value.issuedQuantity<1){
          			        	if(!value.itemDisabled)
          			        		{
                  		        $scope.totalQuantityIssued += parseInt(value.quantityIssued!=null ? value.quantityIssued :0);
                  		      $scope.mainIssuedQty=$scope.mainIssuedQty+parseInt(value.quantityIssued);
            			        if(!value.isMultiBatch)
            			        	{
            			        	$scope.mainNewQty=$scope.mainNewQty+value.newQty;
            			        	}
            			      /*  if($scope.mainIssuedQty>$scope.mainNewQty)
            			        	{
            			        	growl.warning("  warning !! enter quantity is greater than ordered quantity ");
            			        	value.quantityIssued = "0";
                			        return false;
            			        	}*/
          			        		}
                                if(value.selectedItems != null && value.selectedItems.length>0){
                      		        angular.forEach(value.selectedItems, function (selectValue, selectIndex)  {
                      			        if(!value.finalOrderedQty)
                      				    {
                      				        finalOrdQty = selectValue.finalOrderedQty;
                      				    }
                      			      if(!selectValue.itemDisabled)
            			        		{
                      			        $scope.totalQuantityIssued += parseInt(selectValue.quantityIssued);
                      			      $scope.subIssuedQty=$scope.subIssuedQty+parseInt(selectValue.quantityIssued);
                      			        if(!selectValue.isMultiBatch)
                      			        	{
                      			        	$scope.subNewQty=$scope.subNewQty+selectValue.newQty;
                      			        	}
                      			       /* if($scope.subIssuedQty>$scope.subNewQty)
                      			        	{
                      			        	growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                      			        	selectValue.quantityIssued = "0";
                          			        return false;
                      			        	}*/
            			        		}
                     		        });
                                }
                  	        }
          			    }
          		        else
          			    {
          			        if(value.itemId == parentItem.itemId && value.issuedQuantity<1){
          			        	if(!value.itemDisabled)
    			        		{
                  		        $scope.totalQuantityIssued += parseInt(value.quantityIssued!=null ? value.quantityIssued :0);
                  		      $scope.mainIssuedQty=$scope.mainIssuedQty+parseInt(value.quantityIssued);
          			        if(!value.isMultiBatch)
          			        	{
          			        	$scope.mainNewQty=$scope.mainNewQty+value.newQty;
          			        	}
          			       /* if($scope.mainIssuedQty>$scope.mainNewQty)
          			        	{
          			        	growl.warning("  warning !! enter quantity is greater than ordered quantity ");
          			        	value.quantityIssued = "0";
              			        return false;
          			        	}*/
    			        		}
                  		        if(value.selectedItems != null && value.selectedItems.length>0){
                      		        angular.forEach(value.selectedItems, function (selectValue, selectIndex)  {
                      			        if(!value.finalOrderedQty)
                  				        {
                  				            finalOrdQty = selectValue.finalOrderedQty;
                  				        }
                      			      if(!selectValue.itemDisabled)
          			        		{
                      			        $scope.totalQuantityIssued += parseInt(selectValue.quantityIssued);
                      			      $scope.subIssuedQty=$scope.subIssuedQty+parseInt(selectValue.quantityIssued);
                    			        if(!selectValue.isMultiBatch)
                    			        	{
                    			        	$scope.subNewQty=$scope.subNewQty+selectValue.newQty;
                    			        	}
                    			       /* if($scope.subIssuedQty>$scope.subNewQty)
                    			        	{
                    			        	growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                    			        	selectValue.quantityIssued = "0";
                        			        return false;
                    			        	}*/
          			        		}
                     		        });
                                }
                  	        }
          			    }
                    }); 
          	        angular.forEach(self.model.pendingIssuesDetails.items, function (value, index) {
                        if(!value.disabled && !value.itemDisabled){
                            if(parseInt(value.newStock)==parseInt(value.quantityIssued))
                            {
                                value.substituteButtonFlag1=true;
                            }
                            if(parseInt(value.newQty)<parseInt(value.quantityIssued))
                            {
                               /* value.substituteButtonFlag1=false;
                                growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                                value.quantityIssued = "0";
                                value.itemtotalAmount = "0";
                                value.itemDiscount = "0";
                                value.taxAmount= "0";
                                value.netAmount= "0";
                                value.roundOff="0";*/
                            }
                            if(value.checkNewItem==true)
                            {
                                /*if(parseInt(value.stockAvailable)<parseInt(value.quantityIssued))
                                {
                                    value.substituteButtonFlag1=false;
                                    growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                                    value.quantityIssued = "0";
                                    value.itemtotalAmount = "0";
                                    value.itemDiscount = "0";
                                    value.taxAmount= "0";
                                    value.netAmount= "0";
                                    value.roundOff="0";
                                }*/
                            }
                        }
          	        });
          	      parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
          	      /*  if (finalOrdQty < $scope.totalQuantityIssued) {
                        parentItem.isParentQuantityValid = true;
                        parentItem.substituteButtonFlag1=false;
                        growl.warning("  warning !! enter quantity is greater than ordered quantity ");
                        parentItem.quantityIssued = "0";
                        parentItem.itemtotalAmount = "0";
                        parentItem.itemDiscount = "0";
                        parentItem.taxAmount= "0";
                        parentItem.netAmount= "0";
                        parentItem.roundOff="0";
                        if(parentItem.substituteBtn !=undefined && parentItem.substituteBtn == true){
                            parentItem.quantityIssued = "0";
                            parentItem.itemtotalAmount = "0";
                            parentItem.itemDiscount = "0";
                            parentItem.taxAmount= "0";
                            parentItem.netAmount= "0";
                            parentItem.roundOff="0";
                        }
          	  	    }else {
                        parentItem.quantityIssued = parseInt(parentItem.quantityIssued);
          	  	    }*/
                }
            }
            parentItem.substituteButtonFlag1=false;
            return isValid;
        }
       
        var ValidateBill = function (bill) {

            var isValid = true;
            var count = 0;

            if (self.model.pendingIssuesDetails.discount) {
                if (!bill.discountCatgeory.id) {
                    isValid = false;
                    self.model.isDiscountCategoryValid = true;
                    $scope.btnF = false;
                }
                // if (!bill.items.quantityIssued || !String(bill.items.quantityIssued) <= 0) {
                //     growl.error('Please Enter Quantity');
                //     isValid = false;
                // }

                if (!self.model.isDiscountApplied) {
                    isValid = false;
                    self.model.isDiscountAppliedValid = true;
                    growl.error('Please apply discount on the bill items');
                    $scope.btnF = false;
                }

                if (self.model.discountTypeFlag == true) {
                    isValid = false;
                    self.model.isDiscountAppliedValid = true;
                    growl.error('Please apply discount on the bill items');
                    $scope.btnF = false;
                }

                if (self.model.isAppliedDiscountValid == true) {
                    isValid = false;
                    growl.error('Please apply discount on the bill items');
                    $scope.btnF = false;
                }
            }
          
            if (self.model.pendingIssuesDetails.discountCatgeory.id) {
                var found = self.model.pendingIssuesDetails.items.find(item => item.discountPerItem == 0)
                if (!self.model.pendingIssuesDetails.discount && found!=undefined) {
                    isValid = false;
                    self.model.isDiscountAmountValid = true;
                    growl.error('Please enter discount amount');
                    $scope.btnF = false;
                }
            }

            if (!bill.items || bill.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
                $scope.btnF = false;
            } else {
                bill.items.forEach(function (billitem) {
                    if (!billitem.disabled && !billitem.itemDisabled) {
                        // if(billitem.quantityIssued!=undefined && billitem.quantityIssued!=null && billitem.quantityIssued > 0 && billitem.quantityIssued!=""){
                        // billitem['isItemValid'] = false;
                        billitem['isBatchValid'] = false;
                        billitem['isUOMValid'] = false;
                        billitem['isQuantityValid'] = false;

                        if(billitem.quantityIssued){
                            if (billitem.selectedBatch && !billitem.selectedBatch.batchId) {
                                billitem['isBatchValid'] = true;
                                isValid = false;
                                $scope.btnF = false;
                            }

                            if (!billitem.selectedUom) {
                                billitem['isUOMValid'] = true;
                                isValid = false;
                                $scope.btnF = false;
                            }    
                        }
                        

                        angular.forEach(self.model.pendingIssuesDetails.items, function(d){
                        	if (!d.disabled && !d.itemDisabled){
								if(d.quantityIssued){
									count++; 
								}	
                        	}
                        });

                        if (billitem.quantityIssued < 0) 
                        {
                            billitem['isQuantityValid'] = true;
                            growl.error("Please enter quantity greater than zero");
                            isValid = false;
                            $scope.btnF = false;
                        }

                     /*   if (billitem.newQty < billitem.quantityIssued) {

                            billitem['isQuantityValid'] = true;
                            isValid = false;
                            growl.error('Entered quantity is exceeding the max orderd quantity');
                            $scope.btnF = false;
                        }*/

                        if (!ValidateUniqueBatchAndUom(billitem)) {
                            isValid = false;
                            $scope.btnF = false;
                        }
                        if (!ValidateBatch(billitem)) {
                            isValid = false;
                            $scope.btnF = false;
                        }
                        if (!ValidateUOM(billitem)) {
                            isValid = false;
                            $scope.btnF = false;
                        }
                    }
                	   
                    if (billitem.selectedItems && !billitem.itemDisabled) {
                    	   
                        billitem.selectedItems.forEach(function (item) {
                    		//    if(item.quantityIssued!=undefined && item.quantityIssued!=null && item.quantityIssued > 0 && item.quantityIssued!=""){
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
                               $scope.btnF = false;
                            }

                            if (!item.selectedUom) {
                        	   item['isUOMValid'] = true;
                               isValid = false;
                               $scope.btnF = false;
                            }

                            // angular.forEach(self.model.pendingIssuesDetails.items, function(d){
                            //     angular.forEach(d.selectedItems, function(s){
                            if (!item.disabled && !item.itemDisabled){
                                if(item.quantityIssued){
                                    count++; 
                                }	
                            }
                            // });
                            // });

                            if(count == 0){
                            	if(item.itemDisabled != true){
                            		if (item.quantityIssued == undefined || String(item.quantityIssued) <= 0) {
                                        item['isQuantityValid'] = true;
                                        isValid = false;
                                        growl.error('Please enter quantity');
                                        $scope.btnF = false;
                                    }
                            	}
                            }

                            if (item.quantityIssued < 0) 
                            {
                        	   item['isQuantityValid'] = true;
                               growl.error("Please enter quantity greater than zero");
                               isValid = false;
                               $scope.btnF = false;
                            }

                          /*  if (item.newQty < item.quantityIssued) {
                        	   item['isQuantityValid'] = true;
                               isValid = false;
                               growl.error('Entered quantity is exceeding the max orderd quantity');
                               $scope.btnF = false;
                            }*/

                            if (!ValidateUniqueBatchAndUom(item)) {
                               isValid = false;
                               $scope.btnF = false;
                            }
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
            $scope.isValid = false;

            self.model.pendingIssuesDetails.items.find(function (item) 
            {
                if($scope.paymentEntitleMentList!=null && $scope.paymentEntitleMentList.length>0){
                    $scope.isConsumed = true;
                }
            });
            return $scope.isValid;
        }

        var NavigateToPendingIssues = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('pendingIssues');
            }, 1000);
        }

        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }
        
        $scope.barcodeScanned= false;
        $scope.handleDoubleClick=false;
        var BarcodeScan = function (event, barcode) {
            $scope.handleDoubleClick=false;
            if(self.model.barcode == undefined || self.model.barcode.length == 0 || self.model.barcode == null)
    		{
            	growl.error("Barcode Number Should Not Be Blank...")
            	return false;
    		}
            if(!barcode) 
    		{
    	  		barcode = self.model.barcode;
			}
            var enterCode = 13;
            
           
            var data = {
            		
            	"priPrintedBarcodeNo":self.model.barcode,
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
                        if(response.data[0].isManualBarcodeRequired==true)
                        {
                        	if(value.prePrintedBarcode==self.model.barcode){
                        		flag=true;
                                self.model.barcode="";
                                $scope.barcodeScanned= true;
                               AllocateBatch(self.model.pendingIssuesDetails, value.batchId);  
            	        	  }
                        	else{
            	 	        		 flag=true;
            	 	        		self.model.barcode="";
            	  	 				growl.error( "Enter Valid Barcode..!",
            	  	 				{
            	  	 					title: "Error"
            	  	 				});
            	  	        	  }
                            
                        }
                        else {
             				flag=true;
             				self.model.barcode="";
             				growl.error( "Enter Valid Barcode..!",
             				{
             					title: "Error"
             				});
             			}
                    });
     	            if(flag==false)
     		        {
                        if (!!barcode) {
//                            var itemId = barcode.split(' ')[0];
                            var batchId = barcode;
                            $scope.barcodeScanned= true;
                            self.model.barcode="";
                            AllocateBatch(self.model.pendingIssuesDetails, batchId);
                            event.stopPropagation();
                            event.preventDefault();
                            SetFocus();
                        }
                    }
                });
            }
        }

        var AllocateBatch = function (bill, batchId) {
            if( batchId != undefined){
                var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_BATCH_API + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId + '/'+$cookies.getObject('cookieObject').storeId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + bill.id;
                var data = {
//                    "itemId": itemId,
                    "batchId": batchId,
                    "barcodeScanned": $scope.barcodeScanned
                };
                $rootScope.startLoader();
                GenericService.serviceAction("PUT", URI, data).then(function (response) {
                    if (!!response.data) {
                        var check = false;
                        angular.forEach(bill.items,function(value,key){
                              
                            if(value.selectedBatch != undefined && batchId == value.selectedBatch.batchId) {
                                      check = true;
                                  }
                                  
                            });
                        
                        if(check==true){
                            growl.error('There should not be the same batch for the same item.'); 
                            $rootScope.stopLoader();
                          return false;
                         }
                        var item = bill.items.find(function (obj) {
                            return obj.allocationId == response.data.allocationId;
                        });
                        // if (!!item) {
                        //     item.quantity = parseInt(item.quantity) + 1;
                        //     CalculateQuantity(item, item.selectedUom, item.uom);
                        //     CalculateAmount(item);
                        // } 
                        // else {
                            angular.forEach(response.data.batch, function (value, key) {
                            }, response.data);
                            var batch = response.data.batch.find(function (obj) {
                                return obj.batchId == batchId;
                            });
                            item = {
                                itemDisabled:false,
                                disabled:false,
                                // allocationId: response.data.allocationId,
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
                                productCategoryId: response.data.productCategoryId,
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
                                storageUnitName: response.data.storageUnitName,
                                isThreeUOMApplicable: response.data.isThreeUOMApplicable,
                                barcodeItem: true
                            };
                            if (!!batch) {
                                item.selectedBatch = batch;
                                item.uom = item.uom.concat(batch.uom);
                                item.selectedUom = batch.uom.find(function (obj) {
                                    return obj.opUom;
                                });
                                item.quantityIssued = 1;
                            } 
                            else {
                                //item.selectedBatch = item.batch[0];
                                item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                                if(item.selectedBatch!=undefined && item.selectedBatch!=null)
                            	{
                            item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                return obj.opUom;
                              });
                            	}
                            }
                            item.taxId=item.selectedBatch.taxId;
                            item.taxPercentage=item.selectedBatch.taxPercentage;
                            CalculateQuantity(item, item.selectedUom, item.uom);
                            // CalculateRate(item, item.selectedUom, item.uom);
                            Calculate(item, self.model.pendingIssuesDetails);
                            CalculateAmount(item);
                            if(item.isThreeUOMApplicable == false){
                                item.selectedBatch.uomListData = item.selectedBatch.uom;
                                item.selectedBatch.uom = [];
                                angular.forEach(item.selectedBatch.uomListData, function(d){
                                    if(d.uomTypeId != 2){
                                        item.selectedBatch.uom.push(d);
                                    }
                                });
                            }
                            if($scope.handleDoubleClick==false)
                      	  {
                            bill.items.push(item);
                            $scope.handleDoubleClick=true;
                            }
                            var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API + $scope.unitId +'/'+ $cookies.getObject('cookieObject').storeId +'/'+item.itemId+'/'+ batchId+'/'+item.selectedUom.uomTypeId;
                            GenericService.serviceAction("GET", URI, {}).then(
                            function (response) 
                            {
                                item.stockAvailable =response.data.availableqty;
                                item.selectedBatch.availableStockQuantity = response.data.availableqty;
                                if(item.selectedUom!=undefined && item.selectedUom!=null)
                            	{
                                ConvertQtyOrdered(item, item.uom, item.selectedUom);
                            	}
                                $scope.handleDoubleClick=false;
                                //Ganesh
                                //   item.quantityIssued = commonDetailService.getDefaultQuantity(item,item.quantityOrdered,response.data.availableqty);
                                $rootScope.stopLoader();
                            },
                            function (err)                   
                            {
                                item.stock = 0;
                                item.newStock = 0;
                                $rootScope.stopLoader();
                            });
                        // }
                        self.model.barcode = "";
                        $rootScope.stopLoader();
                    }
                },
                function (ex) {
                    growl.error(ex.data.message);
                    $rootScope.stopLoader();
                });
            }
        }

        var AddBatchToDetail = function (worklist, itemId, batchId) {
            var itemId = itemId;
            var batchId = batchId;
            angular.forEach(self.model.pendingIssuesDetails.items, function (item, key) {

                let temp = item.batch.find(function (batch) {
                    if (batch.batchId == batchId) {
                        item.selectedBatch = batch;
                        item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                            return obj.opUom;
                        });
                        // CalculateRate(item, item.selectedUom, item.uom)
                        item.quantityIssued = item.quantityIssued + 1;
                    }
                });

            });
            var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $cookies.getObject('cookieObject').storeId +'/'+itemId+'/'+ batchId+'/'+item.selectedUom.uomTypeId;
              GenericService.serviceAction("GET", URI, {}).then(
                function (response) 
                {
                  item.stock=response.data.availableqty;
                  item.newStock =response.data.availableqty;
                //Ganesh
                  item.quantityIssued = commonDetailService.getDefaultQuantity(item,response.data.availableqty);
                  $rootScope.stopLoader();
                },
                function (err) 
                {
                  item.stock = 0;
                  item.newStock = 0;
                  $rootScope.stopLoader();
                });
        }
        /*var ConfirmRemove = function (index, assigningPickerDispenserDetails) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
            self.model.itemForRemove = index;
            self.model.pickerDetail = assigningPickerDispenserDetails;
        }*/
        
        
        
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
                           // $scope.isDiscountApplicable = $scope.paymentEntitleMentList[0].isTariffRateApplicable;
                            $scope.isDiscountApplicable ='Y';
                            if(($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3)){//&& $scope.isConsumed == false
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
    					}
    					else{
    						//   $scope.initPaymentEntitlement();
    						  $scope.entitlementObj.paymentEntitlementTypeId = $scope.selfPayeeId;
                              $scope.isContractChanged ='N';
    						  $scope.isPayeeUpdated ='N';
    						  $scope.isDiscountApplicable = 'Y';
    						  $scope.paymentEntitlementTypeIdDummy =angular.copy($scope.entitlementObj.paymentEntitlementTypeId);
                            if(($scope.entitlementObj.paymentEntitlementTypeId == 2 || $scope.entitlementObj.paymentEntitlementTypeId == 3) ){ //&& $scope.isConsumed == false
                                $scope.isValidated = true;
                            }
                            else{
                                $scope.isValidated = false;
                            }
    					}
    					
    					//for package
    					$scope.activePackageList = response.data.object;
    					$scope.activePackageListWithoutForceClose=[];
    					angular.forEach($scope.activePackageList, function (item, key)  {
    						if(item.isForceClose==1)
    							{
    							$scope.activePackageListWithoutForceClose.push(item);
    							}
    					});
    					if($scope.activePackageListWithoutForceClose!=null&& $scope.activePackageListWithoutForceClose.length>0)
                        {
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
    		$scope.ActualsaveAndValidateContract(bill);
    		
    		/*
    		 self.model.stopItemIds = [];
        	 angular.forEach(bill.items, function (item, key)  {
                 if (!item.disabled && !item.itemDisabled) 
                    {
                      self.model.stopItemIds.push(item.itemId);
//                      if((item.quantityIssued<0||item.quantityIssued==0) && item.statusId != 4)
//                	  {
//                    	  growl.warning('Please enter quantity')
//                          return false;
//                	  }
                    }
              });        	 
    		var stopData = {
   	                "itemIds": self.model.stopItemIds,
   	                "storeId": $cookies.getObject('cookieObject').storeId   	      		 
   	              };      	    
       	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                var StopTransactionData = responseData;  
                if(StopTransactionData.length > 0 ){    
                         var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                         var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                         var stopMessage = "Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ";                             
                         growl.warning(stopMessage, {onclose: function() {
                         	var popup = angular.element('#confirmStopValidate');
                          	popup.modal('show');    
                         },});
                 }
                else{
                	$scope.ActualsaveAndValidateContract(bill);
                }
       	  });
 		*/}
    	
    	$scope.ActualsaveAndValidateContract = function(bill)
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
                    //  "patientClassPercentage" : '',
                    "patientId" :bill.patientId,
                    "tariffId" : $scope.defaultSelfTariffId,
                    "taxInclusive" : $scope.taxInclusive,
                    "doctorId" : bill.doctorId,
                    "consume" :false,
                    "itemList" : []
                }

                angular.forEach(bill.items, function (item, key) {
                    if (!item.disabled && !item.itemDisabled && item.selectedBatch!=null) {
                        if(item.quantityIssued !=undefined && item.quantityIssued !=null && item.quantityIssued > 0 && item.quantityIssued !=""){
                            var billItem =  {
                                "itemId" : item.itemId,
                                "batchId" : item.selectedBatch.batchId,
                                "rate" : item.rate,
                                "quantity" : Number(item.quantityIssued),
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
                                "payeeId" : (parseFloat(item.creditPayable)==0)?$scope.selfPayeeId:item.payeeId,
                                "contractId" : '',
                                "creditPayable" : item.creditPayable,
                                "selfPayable" : item.selfPayable,
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
                });
                
                angular.forEach(bill.items, function (item, key) {
                   // if (item.selectedItems && !item.itemDisabled) {
                    if (item.selectedItems && !item.itemDisabled) {
                        angular.forEach(item.selectedItems, function (item, ckey) {
                        	if(!item.itemDisabled  && item.selectedBatch!=null){
                                if(item.quantityIssued !=undefined && item.quantityIssued !=null && item.quantityIssued > 0 && item.quantityIssued !=""){
                                    var billSubItem = {
                                        "itemId" : item.itemId,
                                        "batchId" : item.selectedBatch.batchId,
                                        "rate" : item.rate,
                                        "quantity" : Number(item.quantityIssued),
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
                                        "payeeId" : (parseFloat(item.creditPayable)==0)?$scope.selfPayeeId:item.payeeId,
                                        "contractId" : '',
                                        "creditPayable" : item.creditPayable,
                                        "selfPayable" : item.selfPayable,
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
                    //}
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
                            		 if(consumedItems.batchId==value.selectedBatch.batchId && consumedItems.itemId==value.itemId && key==consumedItems.index)   
                            		 {
                                   
                                        value.creditPayable = Number(parseFloat(consumedItems.creditPayable).toFixed(2));
                                        value.selfPayable = Number(parseFloat(consumedItems.selfPayable).toFixed(2));
                                        value.netAmount =  Number(parseFloat(consumedItems.netAmount).toFixed(2));
                                        //value.isConsumed = consumedItems.isConsumed;
                                        value.isConsumed = false;
                                        value.contractId  = consumedItems.contractId;
                                        value.payeeId = consumedItems.payeeId;
                                        value.packageConsumptionAmt = consumedItems.packageConsumptionAmt;
                                        value.saleDetailPayeeMapperDtoList =consumedItems.saleDetailPayeeMapperDtoList;
                                        value.saleDetailPackageMapperDtoList =consumedItems.saleDetailPackageMapperDtoList;
                                        $scope.isConsumed = value.isConsumed;
                                        if(value.isConsumed == false){
                                            $scope.isValidated = false;
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
	                                                //value.isConsumed = consumedItems.isConsumed;
	                                                value.isConsumed = false;
	                                                value.contractId  = consumedItems.contractId;
	                                                value.payeeId = consumedItems.payeeId;
	                                                value.packageConsumptionAmt = consumedItems.packageConsumptionAmt;
	                                                value.saleDetailPayeeMapperDtoList =consumedItems.saleDetailPayeeMapperDtoList;
	                                                value.saleDetailPackageMapperDtoList =consumedItems.saleDetailPackageMapperDtoList;
	                                                $scope.isConsumed = value.isConsumed;
	                                                if(value.isConsumed == false){
	                                                    $scope.isValidated = false;
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
                    response.data.resObject =  response.data.resObject!=null ? Number(parseFloat(response.data.resObject).toFixed(2)):response.data.resObject;
                    if(response.data.resObject!=null)
                    {
                    	growl.warning('Pharmacy bill amount getting exceeded with '+response.data.resObject+' amount than package amount ',{
	                        title : 'Warning!'
	                    });
                    }
                    
                    //$scope.getSponsorDetails($scope.admissionId);
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
    
    //chetana
        
        var GetPendingIssuesDetailsForPrint= function (printId) {
        	self.model.printId=printId;
        }
        
        var PrintPendingIssuesDetailsReport=function(){
        	
	      var data = {
	        "appName": "HIS",
	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
	        "reportDesignFilePath": "get_getpendingIssuesDetails.rpt",
	        "reportFormat": "PDF",
	        "exportFileName": "PendingIssuesDetails",
	        "reportParams": [
	          {
	            "paramName": "id",
	            "paramType": "Integer",
	            "paramValue": self.model.printId
	          }
	        ]
	      };
	      reportService.generate(REPORT_API_URI1, data);
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
        
      //For substitute
      
      var isSelected = function (item) {
          return item.isSelected;
      }
      
      $scope.parentIssueDetailsId = 0;
      $scope.isFromAssignPicker = false;
      var SubstituteItems = function  (item,index) {
         // self.model.parentIndex=parentIndex;
    	  self.model.SelectedItems=[];
    	  item.substititeFlag=true;
          self.model.parIndx = index;
          self.model.formulationTypeId = item.formulationTypeId;
          self.model.instructionDesc = item.instructionDesc;
          self.model.duration = item.duration;
          self.model.frequencyDesc = item.frequencyDesc;
          self.model.selectedItemId = item.itemId;
          self.model.selectedGenricId = item.genericId;
          self.model.itemName = item.itemName;
          self.model.dose = item.dose;
          self.model.doseUnit = item.doseUnit!=undefined ? item.doseUnit : " ";
          self.model.genericName = item.genericName;
          self.model.productCategoryId = item.productCategoryId;
          $scope.parentIssueDetailsId = item.id!=undefined && item.id!=null ? item.id : item.parentIssueDetailsId;
          $scope.isFromAssignPicker = item.isFromAssignPicker;
          if(item.selectedUom == undefined || item.selectedUom.uomTypeId == undefined){
         	 self.model.selectedQuantity = item.quantityOrdered;
         }else{
             if(item.quantityIssued == null){
                item.quantityIssued = 0;
             }
         	  var mulQty = item.selectedUom.conversion * item.quantityIssued;
               var  newOrderQty =  item.finalOrderedQty - mulQty;
               self.model.selectedQuantity = newOrderQty;
         }
         // self.model.selectedQuantity = item.quantityOrdered;
          self.model.substituteNewQty = item.newQty;
          self.model.taxAmount = item.taxAmount;
          self.model.taxId = item.taxId;
          self.model.taxPercentage = item.taxPercentage;
          self.model.issuedQuantity = item.issuedQuantity;
          self.model.pendingIssueQuantity = item.pendingIssueQuantity;
          self.model.prescriptionDetailId = item.id!=undefined && item.id!=null ? item.id:item.parentIssueDetailsId;
          self.model.frequency = item.frequency;
          self.model.dose = item.dose+' '+ self.model.doseUnit ;
          self.model.startDate = item.startDate;
          self.model.stopDate = item.stopDate;
          self.model.substituteDetails = [];
          self.model.selectAllRows = false;
          self.model.isItemChecked = false;
          self.model.pickedQuantity =item.pickedQuantity!=undefined ? item.pickedQuantity :0;
          item.isNarcotic = false;
          item.substititeFlag=true;
          
//          var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic;
          var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic+ '/' +  $cookies.getObject('cookieObject').storeId +'/'+ self.model.formulationTypeId;
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
                          item.batch.unshift({
                              "batchDetails": "Select Batch"
                              // "batchType": "Select Batch"
                          });
                          item.uom = [{
                              "uomDetails": "Select UOM"
                          }],

                          //item.selectedBatch = item.batch[0];
                          $scope.resultData = item.batch.filter(word => word.availableStockQuantity > 0);
                          item.batch= $scope.resultData;
                          item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                          item.selectedUom = item.uom[0];
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
      
      //confirmation for remove
     /* var ConfirmRemove = function (index, assigningPickerDispenserDetails, parentindex) {
          var popup = angular.element('#itemRemovePopup');
          popup.modal('show');
          self.model.itemForRemove = index;
          self.model.pickerDetail = pendingIssuesDetails;
          self.model.parentIndexNew = parentindex;
      }*/
      
      //confirmation for remove
      var ConfirmRemove = function (index, pendingIssuesDetails, parentindex) {
          var popup = angular.element('#itemRemovePopup');
          popup.modal('show');
          self.model.itemForRemove = index;
          self.model.pickerDetail = pendingIssuesDetails;
          self.model.parentIndexNew = parentindex;
      }
      //For Add
      var AddItemForSubstitute = function (index, bill, parentIndex) { 
          //bill.items.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
          if (parentIndex == null) {
              bill.items.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
          } else {
              bill.items[parentIndex].selectedItems.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
              //item = bill.items[parentIndex].selectedItems[index];
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
//    	  	 var popup = angular.element('#substituteModal');
//    	          popup.modal('hide');
    	  }
    	  return isValid; 
      }
      $scope.subFlagChange = function()
      {
    	  self.model.pendingIssuesDetails.items.forEach(function (item) {
    		  item.substititeFlag=false;
    	  });
    	  $rootScope.stopLoader();
      }
      var Add = function () {
    	  $scope.clearDiscount();
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
                  selectedItem['newQty'] = self.model.selectedQuantity;
                  selectedItem['substituteNewQty'] = self.model.substituteNewQty;
                  selectedItem['id'] = self.model.prescriptionDetailId;
                  selectedItem['substituteBtn'] = true;
                  selectedItem['parentItemId'] = self.model.selectedItemId;
                  selectedItem['selectedUom'] = selectedItem.selectedUom;
                  selectedItem['selectedBatch'] = selectedItem.selectedBatch;
                  selectedItem['frequency'] = self.model.frequency;
                  selectedItem['productCategoryId'] = self.model.productCategoryId;
                  selectedItem['pendingIssueQuantity'] = self.model.pendingIssueQuantity;
                  selectedItem['issuedQuantity'] = self.model.issuedQuantity;
                  selectedItem['taxAmount'] = self.model.taxAmount;
                  selectedItem['taxId'] = selectedItem.selectedBatch.taxId!=undefined && selectedItem.selectedBatch.taxId!=null ? selectedItem.selectedBatch.taxId:selectedItem.taxId;
                  selectedItem['taxPercentage'] = selectedItem.selectedBatch.taxPercentage!=undefined && selectedItem.selectedBatch.taxPercentage!=null ? selectedItem.selectedBatch.taxPercentage:selectedItem.taxPercentage;
                  selectedItem['saleDetailPayeeMapperDtoList'] = [];
                  selectedItem['SaleDetailPackageMapperList'] = [];
                  selectedItem['patientClassPercentage'] = 0;
                  selectedItem['markUpDown'] = selectedItem.markUpDown;
                  selectedItem['markUpDownPer'] = selectedItem.markUpDownPer;
                  selectedItem['itemId'] = selectedItem.itemId;
                  selectedItem['finalOrderedQty'] = self.model.selectedQuantity;
                  selectedItem['isFromAssignPicker'] = $scope.isFromAssignPicker;
                  selectedItem['parentIssueDetailsId'] =  $scope.parentIssueDetailsId!=0 ? $scope.parentIssueDetailsId : null;
                  selectedItem['productCategoryId'] = selectedItem.productCategoryId;
                  selectedItem['pickedQuantity'] = self.model.pickedQuantity;
                  selectedItem['instructionDesc'] = self.model.instructionDesc
                  selectedItem['duration'] = self.model.duration;
                  selectedItem['frequencyDesc'] = self.model.frequencyDesc;
                  
                  // selectedItem.selectedUom = [{
                  //     uomDetails: "Select UOM"
                  // }];
                  // selectedItem.selectedUom = selectedItem.selectedUom[0];
                  
              });
              $rootScope.startLoader();
              var count=0;
              angular.forEach(self.model.pendingIssuesDetails.items,function(mainItem)
            		  {
            	  angular.forEach(self.model.SelectedItems,function(subItem)
            			  {
            		  		if(mainItem.itemId==subItem.itemId && mainItem.selectedBatch!=undefined && mainItem.selectedBatch!=null &&  mainItem.selectedBatch.batchId==subItem.selectedBatch.batchId)
            		  			{
            		  			count++
            		  			}
            			  });
            		  });
              if(count>0)
            	  {
            	  growl.error('There should not be same batch and same UOM for same item');
		  			  return false;
            	  }
              else
            	  {
            	  var popup = angular.element('#substituteModal');
    	          popup.modal('hide');
            	  }
              self.model.pendingIssuesDetails.items.forEach(function (list) {
            	  
            	  if(!list.itemDisabled){
                  self.model.SelectedItems.forEach(function (item) {
                      item.uom = [{
                          uomDetails: "Select UOM",
                      }];
                      if(item.selectedBatch != undefined){    
                        if (!!item.selectedBatch.batchId) {
                            item.stockAvailable=item.selectedBatch.availableStockQuantity;
                            item.substituteNewQty = item.selectedBatch.availableStockQuantity;
                            if($scope.autoPrescribedQty == true){
	                        	if($scope.autoBatch == true && item.stockAvailable > item.quantityOrdered){
									item.quantityIssued = item.quantityOrdered;
								}
								else{
									item.quantityIssued = item.stockAvailable;
								}
                            }
                            if(item.isThreeUOMApplicable == false){
                                item.uom = item.selectedBatch.uom;
                                if(item.selectedBatch.uomListData == undefined || item.selectedBatch.uomListData == null){
                                    item.selectedBatch.uomListData = item.uom;
                                }
                                if(item.uomListData == undefined || item.uomListData == null){
                                    item.uomListData = item.uom;
                                }
                                item.uom = [];
                                item.selectedBatch.uom = [];
                                angular.forEach(item.selectedBatch.uomListData, function(d){
                                    if(d.uomTypeId != 2){
                                        item.selectedBatch.uom.push(d);
                                        item.uom.push(d);
                                    }
                                });
                            }
                            item.rate = item.selectedBatch.sellingPrice;
                            item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                opUom: true
                            }, true)[0];
                        } else {
                            item.selectedUom = item.uom[0];
                            item.selectedBatch.batchNo = item.selectedBatch.batchDetails;
                        }
                      }
                      if(item.batch) {
                          if(item.batch.length > 0) {
                              self.model.IsEnbaledSave = true;
                          }
                      }
                      // if (!item.selectedUom) item.selectedUom = "";
                    //for markup and markdown and concession
                      var URI = CONSTANTS.GLOBAL.DETAILS_API_IP + '/' + $scope.unitId  + '/' + item.itemId + '/' + $scope.entitlementObj.paymentEntitlementTypeId + '/' + $scope.admissionId + '/' + $cookies.getObject('cookieObject').storeId;
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
                              if(item.selectedUom!=undefined && item.selectedUom!=null)
                              {
                                 ConvertQtyOrdered(item, item.uom, item.selectedUom);
                          	  }
                              Calculate(item, self.model.pendingIssuesDetails);
                          	  $rootScope.stopLoader();
                           },2000);
                      }, function (err) {
                          growl.error('Error while fetching the item details');
                          $rootScope.stopLoader();
                      });
                      if(item.selectedUom!=undefined && item.selectedUom!=null)
                  	  {
                         ConvertQtyOrdered(item, item.uom, item.selectedUom);
                         $scope.calculateReceivedQty(item);
                  	  }
                  });

                  if (self.model.selectedGenricId  == list.genericId) {
                    	if(list.substititeFlag){
                            if(list.quantityIssued == null || list.quantityIssued == 0){
                            list['disabled'] = true;
                            list.quantityIssued = null;
                            }
                        
                            // list.selectedUom = list.selectedUom[0];
                            
                            if (!list['selectedItems'])
                                list['selectedItems'] = self.model.SelectedItems;
                            else {
                                list['selectedItems'] = list['selectedItems'].concat(self.model.SelectedItems);
                            }
                            $scope.calculateAtCopCalculation(list, self.model.pendingIssuesDetails);
                            list.substititeFlag=false;
                            if(list.selectedItems!=undefined && list.selectedItems!=null && list.selectedItems.length>0 )
                            	{
                            	list.quantityIssuedFlag =true;
                            	list.disableInputField=true;
                            	}
                            
                            $rootScope.stopLoader();
                        }
                    }
              }
              });
console.log("self.model.pendingIssuesDetails",self.model.pendingIssuesDetails);
			self.model.pendingIssuesDetails.items[self.model.parIndx].isShow = false;
              self.model.SelectedItems = [];
          
          }
      }
          /*var popup = angular.element('#substituteModal');
          popup.modal('hide');*/
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
      
     
      
      //to select all items on substitute popup
      var SelectAllItems = function () {
    	    if (self.model.selectAllRows) {
    	        self.model.SelectedItems = []; 
    	        self.model.substituteDetails.forEach(function (item) {
    	            item.isSelected = true;
    	            self.model.SelectedItems.push(item);
    	            item.isMultiBatch = self.model.SelectedItems.length > 1; 
    	        });
    	        self.model.isItemChecked = true;
    	    } else {
    	        self.model.substituteDetails.forEach(function (item) {
    	            item.isSelected = false;
    	            item.isMultiBatch = false; 
    	        });
    	        self.model.SelectedItems = []; 
    	        self.model.isItemChecked = false; 
    	    }
    	};

    	var SelectSubstituteItem = function (item) {
    	    var itemIds = self.model.SelectedItems;

    	    if (item.isSelected) {
    	        if (!itemIds.includes(item)) {
    	            itemIds.push(item);
    	        }
    	        item.isMultiBatch = itemIds.length > 1; 
    	    } else {
    	        var index = itemIds.indexOf(item);
    	        if (index > -1) {
    	            itemIds.splice(index, 1);
    	        }
    	    }

    	    self.model.selectAllRows = self.model.substituteDetails.every(function (subItem) {
    	        return subItem.isSelected;
    	    });

    	    self.model.isItemChecked = itemIds.length > 0;
    	};

      
        var validateUOMAndBatch = function (item) {
            var isValid = true;

            if (item) {
                item['isBatchValid'] = false;

                var items = self.model.substituteDetails.filter(function (detail) {
                    return detail.itemCode == item.itemCode && item.selectedBatch && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == item.selectedBatch.batchId;
                });

                if (items.length > 1) {
                    isValid = false;
                    item['isBatchValid'] = true;
                    growl.error('There should not be same batch for same item');
                }
            }
            return isValid;
        }

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

        $scope.formulationPopup = function (item) {
            $scope.formulationList = [];
            var obj = {
                "manufacturerName" : ''
            }
            obj.manufacturerName = item.manufacturerName;
            $scope.formulationList.push(obj);
            $('#formulationType').modal('show');
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

        $scope.clearDiscount = function(flag) {
            angular.forEach(self.model.pendingIssuesDetails.items, function(data){
              data.discountPerItem = 0;
              angular.forEach(data.selectedItems, function(sub){
            	  sub.discountPerItem = 0;
            	  sub.discountAmount=0;
              });
              self.model.pendingIssuesDetails.discount = 0;
              self.model.pendingIssuesDetails.discountCatgeory = self.model.discountCategoryList[0];
              if(flag==true)
            	  {
            	  Calculate(data, self.model.pendingIssuesDetails);
            	  }
            });
        }
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("pendingIssuesDetails", {
            url: "/pendingIssuesDetails",
            templateUrl: 'views/PharmacyInventory/InPatient/pending-issues-details.html',
            controller: "PendingIssuesDetails.Controller",
            controllerAs: "vm",
            title: 'Pending Issuance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("PendingIssuesDetails.Controller", pendingIssuesDetailsController)
})();