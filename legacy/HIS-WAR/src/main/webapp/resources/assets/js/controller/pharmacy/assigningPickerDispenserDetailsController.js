(function () {
    "use strict";

    function assigningPickerDispenserDetailsController($scope, $rootScope, $http,  CONSTANTS, GenericService, $window, $filter, $state, growl, CurrencyService, StatusService, assigningPickerDispenserService, reportService,stopTransactionService,$cookies,commonDetailService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;


            self.model = {
                assigningPickerDispenserDetails: {
                    items: []
                },
                SelectedItems: [],
                EnterQuantity: 0,
                IsEnbaledSave: false
            };

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            $scope.saveFlag = true;
            self.model.AssigningPickerDispenserDetails = AssigningPickerDispenserDetails;
            self.model.BatchChange = BatchChange;
            self.model.AddItem = AddItem;
            self.model.RemoveItem = RemoveItem;
            self.model.SubstituteItems = SubstituteItems;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectSubstituteItem = SelectSubstituteItem;
            self.model.Add = Add;
            self.model.RestorePreviousItem = RestorePreviousItem;
            self.model.AddSubstituteItem = AddSubstituteItem;
            self.model.RemoveSubstituteItem = RemoveSubstituteItem;
            self.model.SavePopup = SavePopup;
            self.model.SaveBill = SaveBill;
            self.model.ClearFields = ClearFields;
            self.model.selectedQuantity = 0;
            self.model.BarcodeScan = BarcodeScan;
            self.model.SetFocus = SetFocus;
            self.model.AddBatchToDetail = AddBatchToDetail;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.ConvertQtyOrdered = ConvertQtyOrdered;
            self.model.ValidateSaveButton = ValidateSaveButton;
            self.model.ValidateUniqueBatchAndUom = ValidateUniqueBatchAndUom;
            self.model.ValidateBatch = ValidateBatch;
            self.model.ValidateUOM = ValidateUOM;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.smartIssueAssignPicker = smartIssueAssignPicker;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.printAssigningPickerDispenserDetailsReport= printAssigningPickerDispenserDetailsReport;
            self.model.ClearBill = ClearBill;
            self.model.validateUOMAndBatch = validateUOMAndBatch;
            self.model.ActualSaveBill = ActualSaveBill;	
            // SetFocus();
            AssigningPickerDispenserDetails(assigningPickerDispenserService.getAssigningPickerDispenserId());
            
            var cookieObject = $cookies.getObject('cookieObject');
            $scope.storeId = cookieObject.storeId;
        }

        var  clubBatches = function(batches){
        	// added by minesh for total of availableQuantity from all batch and get single Unique Batch
            var batchNo = [];
            var uniqueBatches = [];
        	
        		if (!!batches) {
        			batches.forEach(function (batch) {
        			var index = batchNo.indexOf(batch.batchId+" | "+batch.expiry);                    			 
                	if(index === -1)
                	{
                		batchNo.push(batch.batchNo+" | "+batch.expiry);
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
       	 var pickedQty = 0;
       	 var found =  item.batch.find(function (x) {
             return x.isSelected;
         });
         if(found != undefined){
        	 var uomTypeId = item.uomTypeId !=undefined ?item.uomTypeId : found.uomTypeId;
	       	  if(uomTypeId == 3)
	             {
	       		  pickedQty  = parseInt(item.pendingIssueQty);
	                   
	             }else if(uomTypeId == 2)
	                 	{
	            	
	                        var stripQty = found.uom[1].conversion * parseInt(item.pendingIssueQty);
	                        pickedQty  = stripQty;
	                    
	                 	}else if(uomTypeId== 1)
	                 		 {
			                      var boxQty = found.uom[1].conversion *  item.uom[2].conversion * parseInt(item.pendingIssueQty);
			                      pickedQty  = boxQty;
	                 		 }  
         }
       	  return pickedQty;
       }
        
        
        var GetCalculateOrederQtySelectedItem = function(item){
          	 var pickedQty = 0;
          	 var found =  item.batch.find(function (x) {
                return x.isSelected;
            });
            if(found != undefined){
           	 var uomTypeId = item.uomTypeId !=undefined ?item.uomTypeId : found.uomTypeId;
   	       	  if(uomTypeId == 3)
   	             {
   	       		  pickedQty  = parseInt(item.issuedQuantity);
   	                   
   	             }else if(uomTypeId == 2)
   	                 	{
   	            	
   	                        var stripQty = found.uom[1].conversion * parseInt(item.issuedQuantity);
   	                        pickedQty  = stripQty;
   	                    
   	                 	}else if(uomTypeId== 1)
   	                 		 {
   			                      var boxQty = found.uom[1].conversion *  item.uom[2].conversion * parseInt(item.issuedQuantity);
   			                      pickedQty  = boxQty;
   	                 		 }  
            }
          	  return pickedQty;
          }
        
        var  uomConversion = function(item,batchObj,selectedUomId,parentItemQty){
        	if(parentItemQty !=undefined){
        		var quantityOrdered = item.quantityOrdered - parentItemQty;
        	}else{
        		var quantityOrdered = item.quantityOrdered ;
        	}
            
             if (batchObj) { 
                 if (selectedUomId == 1) {
                     item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                     item.stockAvailable = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[0].conversion * batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                 } else if (selectedUomId == 2) {
                     item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                     item.stockAvailable = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[1].conversion * batchObj.uom[2].conversion));
                 } else if (selectedUomId == 3) {
                     item.newQty = Math.ceil(quantityOrdered / (batchObj.uom[2].conversion));
                     item.stockAvailable = Math.ceil(batchObj.availableStockQuantity / (batchObj.uom[2].conversion));
                 }
             }
        }
        var AssigningPickerDispenserDetails = function (itemId) {
        	 self.model.printId = itemId;
            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.DETAIL_API  + $scope.unitId + "/"+$scope.organizationId + "/"+$scope.storeId+ "/"+$scope.userId+"/" + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.assigningPickerDispenserDetails = response.data;
                    console.log("Saved Data",self.model.assigningPickerDispenserDetails );
                    if (self.model.assigningPickerDispenserDetails.items) {
                        self.model.assigningPickerDispenserDetails.items.forEach(function (item) {
                        	item.issuedQuantity = item.quantity == undefined ? 0 : item.quantity ;
                            item.issuedQty = item.issuedQuantity;
                            item.pendingIssueQty = item.issuedQty;
                            item.finalOrderedQty = item.quantityOrdered;
                            if(self.model.assigningPickerDispenserDetails.items.length==1 && self.model.assigningPickerDispenserDetails.statusId == 2  ){
                            	 item.isShow = false;
                            }else{
                            	 item.isShow = true;
                            }
                            	 
                          //added by minesh
                       	 	var clubData  = clubBatches(item.batch);
                            item.batch = clubData;
                          //added end by minesh
                          if(item.quantityOrdered == (item.pendingIssueQuantity + item.issuedQuantity)){
                                item.status = 'Completed';
                                item.statusId = 4;
                                $scope.saveFlag = false;
                            }
                            if (item.status == 'Completed' || item.statusId == 9 ) {
                            	item.itemDisabled = true;
                            	item.disabled = true;

                            }
                            if(item.batch) {
                                if(item.batch.length > 0) {
                                    self.model.IsEnbaledSave = true;
                                }
                            }
                            if(item.status == 'Partially Completed'){
                                item.pendingIssueQuantity = 0;
                            }
                            else{
                                item.selectedBatch = item.batch.find(function (x) {
                                    return !!x.isSelected;
                                });
                                if(!item.selectedBatch)
                                	{
                                	 item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                                	}
                                
                                if (!!item.selectedBatch) {
                                    item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                        return obj.uomTypeId == item.selectedBatch.uomTypeId;
                                    });
                                    item.uom = item.selectedBatch.uom;
                                    item.stockAvailable = item.selectedBatch.availableStockQuantity;
                                } else {
                                    item.stockAvailable = 0;
                                    item.issuedQuantity=0;
                                    item.pendingIssueQuantity=0;
                                }
                            }
                           
                            item.quantity = 0;
                            
                            if(self.model.assigningPickerDispenserDetails.statusId ==undefined && self.model.assigningPickerDispenserDetails.statusId ==null && self.model.assigningPickerDispenserDetails.statusId ==2){
                                 	 ConvertQtyOrdered(item, item.uom, item.selectedUom);
                            }else{
                            	
                            	 var found =  item.batch.find(function (x) {
                                     return x.isSelected;
                                 });
                               	 
                            	 
                                   	if(found == undefined){
                                   		if(item.batch.length>0){
                                   		if(item.batch[0].uom.length>0){
                               				uomConversion(item, item.batch[0],3);
                                   			}
                                   		}	
                                   	}else{
                                   		var selectedUomTypeId = found.uomTypeId;
                                   		uomConversion(item, found,selectedUomTypeId);
                                   	}
                            }
                           
                            
                            
                            angular.forEach(item.selectedItems, function (item1, index) {
                            	item1.issuedQuantity = item1.issuedQuantity == undefined ? 0 : item1.issuedQuantity ;
                            	item1.issuedQty = item1.issuedQuantity== undefined ? 0 : item1.issuedQuantity;
                            	item1.pendingIssueQty = item1.pendingIssueQuantity;
                            	
                            //added by minesh
                         	 	var clubData  = clubBatches(item1.batch);
                         	 	item1.batch = clubData;
                            //added end by minesh
                         	 	 item1.substituteBtn=true;
                              if(item1.quantityOrdered == (item1.pendingIssueQuantity + item1.issuedQuantity)){
                            	  item1.status = 'Completed';
                            	  item1.statusId = 4;
                              }
                              if (item1.status == 'Completed') {
                            	  item1.itemDisabled = true;
                              }
                              if(item1.batch) {
                                  if(item1.batch.length > 0) {
                                      self.model.IsEnbaledSave = true;
                                  }
                              }
                              if(item1.status == 'Partially Completed'){
                            	  item1.pendingIssueQuantity = 0;
                              }
                              else{
                            	  item1.selectedBatch = item1.batch.find(function (x) {
                                      return !!x.isSelected;
                                  });
                                  if (item1.selectedBatch) {
                                	  item1.selectedUom = item1.selectedBatch.uom.find(function (obj) {
                                          return obj.uomTypeId == item1.selectedBatch.uomTypeId;
                                      });
                                	  item1.uom = item1.selectedBatch.uom;
                                	  item1.stockAvailable = item1.selectedBatch.availableStockQuantity;
                                  } else {
                                	  item1.stockAvailable = 0;
                                  }
                              }
                             
                              item1.quantity = 0;
                              if(self.model.assigningPickerDispenserDetails.statusId ==undefined && self.model.assigningPickerDispenserDetails.statusId ==null && self.model.assigningPickerDispenserDetails.statusId ==2){
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
                              });
                        });
                    }
                    
                    var tempList = [];
                    if(self.model.assigningPickerDispenserDetails.statusId == 18){
                    	
                    	   self.model.assigningPickerDispenserDetails.items.forEach(function (item) {
                    		   var flag = false;
                    		   if(item.statusId == 9|| item.statusId == 2){
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
                           	if(flag == false){
                           		
		                           	  self.model.assigningPickerDispenserDetails.items.forEach(function (item1) {
		                                	if(item.itemId == item1.itemId ){
		                                		totalQty += GetCalculateOrederQty(item1);
		                                		
		                                		if(item1.selectedItems !=undefined){
			                                		item1.selectedItems.forEach(function (item2) {
			                                     		totalQty += GetCalculateOrederQtySelectedItem(item2);
			                                	  });
			                                	}
		                                	}
		                                	
		                             });
		                  		   
		                  		   if(item.quantityOrdered > totalQty){
		                  			   var newOrderQty = item.quantityOrdered - totalQty;
		                  			   var newArr = {};
		                  			 newArr.id = item.id;
		                  			newArr.itemCode= item.itemCode;
		                  			newArr.itemName= item.itemName;
		                  			newArr.isNarcotic= item.isNarcotic;
		                  			newArr.itemId= item.itemId;
		                  			newArr.formulationTypeId= item.formulationTypeId,
		                  			newArr.genericName= item.genericName;
		                  			newArr.genericId= item.genericId;
		                  			newArr.quantityOrdered= newOrderQty;
		                  			newArr.batch= item.batch;
		                  			newArr.selectedBatch= null;
		                  			newArr.storeId = item.storeId,
		                  			newArr.uom= [];
		                  			newArr.selectedUom= null;
		                  			newArr.stockAvailable=0;
		                  			newArr.rackNo= item.rackNo;
		                  			newArr.picker= null;
		                  			newArr.substituteBtn= true;
		                  			newArr.isShow= true;
		                  			newArr.frequency= item.frequency;
		                  			newArr.dose= item.dose;
		                  			newArr.startDate =item.startDate;
		                  			newArr.stopDate = item.stopDate;
		                  			newArr.quantity= 0;
		                  			newArr.prescriptionDetailId= item.prescriptionDetailId;
		                  			newArr.issuedQuantity= 0;
		                  			newArr.pendingIssueQuantity= 0;
		                  			newArr.issuedQty=0;
		                  			newArr.pendingIssueQty= 0;
		                  			newArr.finalOrderedQty = newOrderQty;
		                  			newArr.instructionDesc = item.instructionDesc;
		                  			tempList.push(newArr);
		                  			   
		                  		   }
                           	}
                    		 
                    		   
                           });
                    	   if(tempList.length >0){
                			   tempList.forEach(function (tempItem) {
                				   $scope.saveFlag = false;
                				   self.model.assigningPickerDispenserDetails.items.push(tempItem);
                			   })
                			   if(tempList.length==1){
                				   self.model.assigningPickerDispenserDetails.items[0].isShow = false;
                			   }
                		   }
                        }
                    	if(self.model.assigningPickerDispenserDetails.statusId == 2){
                    		$scope.saveFlag = false; 
                    	}
                        $rootScope.stopLoader();
           			   });
        }

        var BatchChange = function (index, bill, batch, parentIndex) {
            var item = null;
            if (parentIndex == null) {
                bill.items[index].uom = batch.uom;
                bill.items[index].stockAvailable = batch.availableStockQuantity;
                bill.items[index].issuedQuantity = bill.items[index].issuedQuantity;
                bill.items[index].pendingIssueQuantity = bill.items[index].pendingIssueQuantity;
                item = bill.items[index];
            } else {
                bill.items[parentIndex].selectedItems[index].uom = batch.uom;
                bill.items[parentIndex].selectedItems[index].stockAvailable = batch.availableStockQuantity;
                bill.items[parentIndex].selectedItems[index].substituteNewQty = batch.availableStockQuantity;
                bill.items[parentIndex].selectedItems[index].issuedQuantity = bill.items[parentIndex].issuedQuantity;
                bill.items[parentIndex].selectedItems[index].pendingIssueQuantity = bill.items[parentIndex].pendingIssueQuantity;
                item = bill.items[parentIndex].selectedItems[index];
            }

            item.uom = [{
                uomDetails: "Select UOM",
            }];

            if (!!batch.batchId) {
                item.uom = item.uom.concat(batch.uom);

                item.selectedUom = $filter('filter')(item.uom, {
                    opUom: true
                }, true)[0];

            } else {
                item.uom = [{
                    uomDetails: "Select UOM",
                }];
                item.selectedUom = item.uom[0];
            }
            item.issuedQty = item.issuedQuantity;
            item.pendingIssueQty = item.pendingIssueQuantity;
            ConvertQtyOrdered(item, item.uom, item.selectedUom);
        }

        var ConvertQtyOrdered = function (item, uom, uomList,flag) {
            var newUOM = [];
            if (uom) {
                uom.find(function (value) {
                    if (!!value.uomTypeId) {
                        newUOM.push(value);
                    }
                });
            }
            var quantityOrdered = item.quantityOrdered;
            if(flag == undefined){
            	 item.quantity = 0;
            }
           
            if (uomList) { 
                var uomDetails = uomList.uomDetails.split(' ')[0];
                if (uomDetails == "P") {
                    item.newQty = Math.ceil(quantityOrdered / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                    item.issuedQuantity = Math.ceil(item.issuedQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                    item.pendingIssueQuantity = Math.ceil(item.pendingIssueQty / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                    item.stockAvailable = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
                } else if (uomDetails == "S") {
                    item.newQty = Math.ceil(quantityOrdered / (newUOM[1].conversion * newUOM[2].conversion));
                    item.issuedQuantity = Math.ceil(item.issuedQty / (newUOM[1].conversion * newUOM[2].conversion));
                    item.pendingIssueQuantity = Math.ceil(item.pendingIssueQty / (newUOM[1].conversion * newUOM[2].conversion));
                    item.stockAvailable = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
                } else if (uomDetails == "D") {
                    item.newQty = Math.ceil(quantityOrdered / (newUOM[2].conversion));
                    item.issuedQuantity = Math.ceil(item.issuedQty / (newUOM[2].conversion));
                    item.pendingIssueQuantity = Math.ceil(item.pendingIssueQty / (newUOM[2].conversion));
                    item.stockAvailable = Math.ceil(item.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
                }
            }
        }

        var ValidateUniqueBatchAndUom = function (item) {
            var isValid = true;

            if (item.substituteBtn == true) {
                item['isBatchValid'] = false;
                item['isUOMValid'] = false;

                var parentItem = self.model.assigningPickerDispenserDetails.items.filter(function (parentItem) {
                    return parentItem.genericId == item.genericId;
                })

                var items = parentItem.filter(function (detail1) {
                	if(detail1.selectedItems != undefined){
                	detail1.selectedItems.filter(function (detail) {
                	
                    return detail.itemId == 
                    	item.itemId && item.selectedBatch && 
                    	item.selectedUom && 
                    	item.selectedUom.uomTypeId > 0 &&
                    	item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == 
                    		item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
                	 });
                	}
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

                console.log("self.model.assigningPickerDispenserDetails.items",self.model.assigningPickerDispenserDetails.items);
                var items = self.model.assigningPickerDispenserDetails.items.filter(function (detail) {
                	
                	 if (!detail.disabled && !detail.itemDisabled) {
                		
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

        var AddItem = function (index, bill, parentIndex) { 
            //bill.items.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
        	
            if (parentIndex == undefined) {
            	
            	if(bill.items[index].quantity==undefined || bill.items[index].quantity== null ||bill.items[index].quantity=="" || bill.items[index].quantity==0){
          		  growl.error('Please Enter Picked Quantity')
                    return false;
            	}
            	
                bill.items.splice(index + 1, 0, CopyItem(index, bill));
            } else {
                bill.items[parentIndex].selectedItems.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
                //item = bill.items[parentIndex].selectedItems[index];
            }
        }
     
        
        var CopyItem = function (index, bill, parentIndex) {
            // var item = bill.items[index];
            var item = null;
            var totalPickQty = 0;
            var finalOrdQty = 0;
            if (parentIndex == undefined) {
                item = bill.items[index];
	  	      	  angular.forEach(self.model.assigningPickerDispenserDetails.items, function (value, index1) {
	  	            	
	  	                	if(bill.items[index].itemId == value.itemId){
	  	                		
	  	                		totalPickQty += CalculateOrederOrPickedQty(value);
	  	                	}
	  	                	
	  	            });  
	  	      	 finalOrdQty = bill.items[index].finalOrderedQty;
            } else {
		                item = bill.items[parentIndex].selectedItems[index];
		                finalOrdQty = bill.items[parentIndex].finalOrderedQty;
		                if(bill.items[parentIndex].selectedBatch!=undefined){
		                totalPickQty += CalculateOrederOrPickedQty(bill.items[parentIndex]);
		                }
	                	if(bill.items[parentIndex].selectedItems != null && bill.items[parentIndex].selectedItems.length>0){
	                    		
	                		angular.forEach(bill.items[parentIndex].selectedItems, function (selectValue, selectIndex) {
	                			
	                			totalPickQty += CalculateOrederOrPickedQty(selectValue);
	               		 	});
	                  }
            }
            
      	 
	      var newOrderQty = finalOrdQty - totalPickQty;
      	
            return {
                id: item.id,
                itemCode: item.itemCode,
                itemName: item.itemName,
                itemId: item.itemId,
                formulationTypeId: item.formulationTypeId,
                genericName: item.genericName,
                genericId: item.genericId,
                quantityOrdered: newOrderQty,
                // newQty: item.newQty,
                batch: item.batch,
                selectedBatch: null,
                storeId: item.storeId,
                uom: item.uom,
                selectedUom: null,
                stockAvailable: item.stockAvailable,
                rackNo: item.rackNo,
                picker: item.picker,
                substituteBtn: (parentIndex == null ? false : true),
                isShow: true,
                frequency: item.frequency,
                dose : item.dose,
                doseUnit : item.doseUnit,
                isNarcotic:item.isNarcotic,
                startDate: item.startDate,
                stopDate: item.stopDate,
                quantity: 0,
                prescriptionDetailId: item.prescriptionDetailId,
                issuedQuantity: item.issuedQuantity,
                pendingIssueQuantity: item.pendingIssueQuantity,
                issuedQty: item.issuedQuantity,
                pendingIssueQty: item.pendingIssueQuantity,
                finalOrderedQty : item.finalOrderedQty,
                parentItemId : ((item.parentItemId == undefined && item.parentItemId == null )? null : item.parentItemId) ,
                disabled : false,
                itemDisabled : false,
                instructionDesc : item.instructionDesc
            }
        }

        var RemoveItem = function (index, bill, parentIndex) {
            var item = null;
            if (parentIndex == undefined &&  parentIndex == null) {
                item = bill.items[index];
                var itemCount = 0;
                angular.forEach(self.model.assigningPickerDispenserDetails.items, function (value, index) {
                  	
                  	if(value.itemId == item.itemId){
                  		itemCount +=1;
                  	}
              });  
                if(itemCount>2){
                	if((itemCount - 1) > index){
                		
                		for(var i = index;i<itemCount - 1;i++){
                			
                			var removeItemPickedQty = CalculateOrederOrPickedQty(item) ;
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
                			var removeItemPickedQty = CalculateOrederOrPickedQty(bill.items[parentIndex].selectedItems[index]) ;
                    	//	bill.items[i+1].quantityOrdered  += removeItemPickedQty ;
                			bill.items[parentIndex].selectedItems[i+1].quantityOrdered += removeItemPickedQty ;
                    		ConvertQtyOrdered(bill.items[parentIndex].selectedItems[i+1], bill.items[parentIndex].selectedItems[i+1].uom, bill.items[parentIndex].selectedItems[i+1].selectedUom,1);
                		}
                	}
                }
                
            }
            //bill.items.splice(index, 1);
            if (parentIndex == undefined && parentIndex == null) {
                bill.items.splice(index, 1);
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
            	}
            } else {
            	 bill.items[parentIndex].selectedItems.splice(index, 1);
            	if(bill.items[parentIndex].selectedItems.length==0){
            		bill.items[parentIndex]['disabled'] = false;
                    bill.items[parentIndex]['itemDisabled'] = false;	
            	}
            	if(bill.items.length==1){
            		bill.items[parentIndex].isShow = false;
            	}else{
            			bill.items[parentIndex].isShow = true;
            	}
               /* bill.items[parentIndex].selectedItems.splice(index, 1);
                if(bill.items[parentIndex].selectedItems.length === 0){
                    bill.items[parentIndex]['disabled'] = false;
                    bill.items[parentIndex]['itemDisabled'] = false;
                }*/
            }
        }

        var SubstituteItems = function (item,index) {
        	item.substititeFlag=true;
        	self.model.parIndx = index;
        	   self.model.formulationTypeId = item.formulationTypeId;
        	 self.model.selectedItemId = item.itemId;
            self.model.itemName = item.itemName;
            self.model.selectedGenricId = item.genericId;
            self.model.genericName = item.genericName;
            self.model.dose = item.dose;
            self.model.instructionDesc = item.instructionDesc;
            if(item.selectedUom == undefined){
            	 self.model.selectedQuantity = item.quantityOrdered;
            }else{
            	  var mulQty = item.selectedUom.conversion * item.quantity;
                  var  newOrderQty =  item.quantityOrdered - mulQty;
                  self.model.selectedQuantity = newOrderQty;
            }
          
            self.model.substituteNewQty = item.newQty;
            self.model.issuedQuantity = item.issuedQuantity;
            self.model.pendingIssueQuantity = item.pendingIssueQuantity;
            self.model.prescriptionDetailId = item.id;
            self.model.frequency = item.frequency;
            self.model.dose = item.dose+' '+item.doseUnit ;
            self.model.startDate = item.startDate;
            self.model.stopDate = item.stopDate;
            self.model.substituteDetails = [];
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic+ '/' +  $scope.storeId+'/'+ self.model.formulationTypeId;
//            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic;
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
                            item.selectedUom = item.uom[0];
                           
                        });
                    }
                    $rootScope.stopLoaderPopup();
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

        var Add = function () {
            if (!self.model.SelectedItems || self.model.SelectedItems == undefined || self.model.SelectedItems == '') {
                growl.error('Please select items')
                return false;
            } else {

                self.model.SelectedItems.forEach(function (selectedItem) {
                    // selectedItem['itemName'] = self.model.itemName;
                    // selectedItem['genericName'] = self.model.genericName;
                    selectedItem['dose'] = self.model.dose;
                    selectedItem['quantityOrdered'] = self.model.selectedQuantity;
                    selectedItem['substituteNewQty'] = self.model.substituteNewQty;
                    selectedItem['id'] = self.model.prescriptionDetailId;
                    selectedItem['substituteBtn'] = true;
                    selectedItem['parentItemId'] = self.model.selectedItemId;
                    selectedItem['selectedUom'] = selectedItem.selectedUom;
                    selectedItem['frequency'] = self.model.frequency;
                    selectedItem['pendingIssueQuantity'] = self.model.pendingIssueQuantity;
                    selectedItem['issuedQuantity'] = self.model.issuedQuantity;
                    selectedItem['issuedQty'] = self.model.issuedQuantity;
                    selectedItem['finalOrderedQty'] = self.model.selectedQuantity;
                    selectedItem['pendingIssueQty'] =  self.model.pendingIssueQuantity;
                    selectedItem['disabled'] = false;
                    selectedItem['itemDisabled'] =  false;
                    selectedItem['instructionDesc'] = self.model.instructionDesc;
                    // selectedItem['stopDate'] = self.model.stopDate;
                    // selectedItem['startDate'] = self.model.startDate;

                   
                    // selectedItem.selectedUom = [{
                    //     uomDetails: "Select UOM"
                    // }];
                    // selectedItem.selectedUom = selectedItem.selectedUom[0];
                });

                self.model.assigningPickerDispenserDetails.items.forEach(function (list) {
                	if(!list.itemDisabled){
                    self.model.SelectedItems.forEach(function (item) {

                        item.uom = [{
                            uomDetails: "Select UOM",
                        }];
                        if (!!item.selectedBatch.batchId) {
                        	item.stockAvailable=item.selectedBatch.availableStockQuantity;
                            item.substituteNewQty = item.selectedBatch.availableStockQuantity;
                            item.uom = item.selectedBatch.uom;
                            item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                opUom: true
                            }, true)[0];
                        } else {
                            item.selectedUom = item.uom[0];
                        }
                        if(item.batch) {
                            if(item.batch.length > 0) {
                                self.model.IsEnbaledSave = true;
                            }
                        }
                        // if (!item.selectedUom) item.selectedUom = "";
                    });
                   
                    if (self.model.selectedGenricId == list.genericId) {
                    	if(list.substititeFlag){
                    	if(list.quantity == null || list.quantity == 0){
                        list['disabled'] = true;
                        list.quantity = null;
                    	}
                        if (!list['selectedItems'])
                            list['selectedItems'] = self.model.SelectedItems;
                        else {
                            list['selectedItems'] = list['selectedItems'].concat(self.model.SelectedItems);
                        }
                        // list.selectedUom = list.selectedUom[0];
                        list.substititeFlag=false;
                    }
                    }
                }
                });
                self.model.assigningPickerDispenserDetails.items[self.model.parIndx].isShow = false;
                self.model.SelectedItems = [];
            }
            var popup = angular.element('#substituteModal');
            popup.modal('hide');
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

        var AddSubstituteItem = function (index, bill) {
            bill.splice(index + 1, 0, CopySubstituteItem(index, bill));
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
	                selectedBatch: commonDetailService.getDefaultBatchFromConfig(item.batch),
	                uom: item.uom,
	                selectedUom: item.uom[0],
	                stockAvailable: item.stockAvailable,
	                rackNo: item.rackNo,
	                picker: item.picker,
	                disabled: item.disabled,
	                issuedQty: item.issuedQuantity,
	                pendingIssueQty: item.pendingIssueQuantity
            }
        }

        var RemoveSubstituteItem = function (index, bill) {
         //   bill.splice(index, 1);
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

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var SavePopup = function () {
            var popup = angular.element('#confirmSave');
            popup.modal('show');
        }

        // self.model.ChangeItemStatus = ChangeItemStatus;
        var ChangeItemStatus = function (item) {
            var qty;
            if (!!item.substituteNewQty) {
                qty = item.quantityOrdered;
            } else {
                qty = item.newQty;
            }
            if (qty > parseInt(item.quantity)) {
                item.statusId = SetStatus('Partially Completed');
            } else if (qty == parseInt(item.quantity)) {
                item.statusId = SetStatus('Completed');
            }
        }

        var ValidateSaveButton = function () {
            var isValid = false;

            self.model.assigningPickerDispenserDetails.items.find(function (item) {
                if (!item.itemDisabled) {
                    isValid = true;
                }
            });
            return isValid;
        }

        var SaveBill = function (bill) {
        	
        	ActualSaveBill(bill);
        	/*
        	 self.model.stopItemIds = [];
        	 angular.forEach(bill.items, function (item, key) {
                 if(item.batch) {
                     if(item.batch.length > 0) {
                        if (!item.disabled && !item.itemDisabled) {
                	 	self.model.stopItemIds.push(item.itemId);
                    }
                  }
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
                         var stopMessage = "Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ";
                         self.model.stopItemIds = [];
                         $rootScope.stopLoader();
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
            // var SalesWorklistBill = [];

            if (ValidateBill(bill)) {
                var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SAVE_API;

                var data = {
                    "id": !!bill.id ? bill.id : undefined,
                    "prescriptionId": !!bill.prescriptionId ? bill.prescriptionId : undefined,
                    "pickerId": !!bill.pickerId ? bill.pickerId : undefined,
                    "doctorId": !!bill.doctorId ? bill.doctorId : undefined,
                    "visitType": !!bill.visitType ? bill.visitType : undefined,
                    "admissionId": !!bill.admissionId ? bill.admissionId : undefined,
                    "statusId": SetStatus('Partially Completed'),
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                }

                angular.forEach(bill.items, function (item, key) {
                    if(item.batch) {
                        if(item.batch.length > 0) {
                            if (!item.disabled && !item.itemDisabled) {
                                ChangeItemStatus(item);
                                var billItem = {
                                    "statusId": SetStatus('Partially Completed'),
                                    "id": item.id,
                                    "itemId": item.itemId,
                                    "prescriptionDetailId": item.prescriptionDetailId,
                                    "batchId": item.selectedBatch.batchId,
                                    "uomTypeId": item.selectedUom.uomTypeId,
                                    "uomUnitId": item.selectedUom.uomUnitId,
                                    "quanityIssued": Number(item.quantity),
                                    "isNarcotics":item.isNarcotic
                                }
                                data.items.push(billItem);
                                if (item.statusId == SetStatus('Partially Completed')) {
                                    data.statusId = SetStatus('Partially Completed');
                                }
                            }

                        }
                                angular.forEach(item.selectedItems, function (item, key) {
                                	if (item.itemDisabled!= true) {
	                                    var billSubItem = {
	                                        "statusId": 4,
	                                        "id": item.id,
	                                        "itemId": item.itemId,
	                                        "prescriptionDetailId": null,
	                                        "batchId": item.selectedBatch.batchId,
	                                        "uomTypeId": item.selectedUom.uomTypeId,
	                                        "uomUnitId": item.selectedUom.uomUnitId,
	                                        "quanityIssued": Number(item.quantity),
	                                        "parentItemId" : item.parentItemId
	                                    }                           

                                    data.items.push(billSubItem);
                                    
                                	}
                                });
                        
                    }
                });
                
      
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        NavigateToAssigningPickerDispenser();
                        ClearFields(bill);
                        growl.success('Assigning picker dispenser successfully saved');
                        $rootScope.stopLoader();
                        NavigateToAssigningPickerDispenser();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving assigning picker dispenser');
                    });
            }
        }

        var NavigateToAssigningPickerDispenser = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('assigningPickerDispenser');
            }, 1000);
        }

        var ClearFields = function (bill) {

            bill.Id = "1" + (new Date()).getTime();
            bill.uhid = null;
            bill.patientDetail = null;
            bill.dob = null;
            bill.pdd = null;
            bill.doa = null;
            bill.wardName = null;
            bill.bedNo = null;
            bill.prescriptionDate = null;
            bill.doctorDetail = null;
            bill.prescriptionId = null;
            bill.pickerId = null;

            bill.items = [];
        }

        var ValidateBatch = function (parentItem, item) {
            var isValid = true;
            if (!!parentItem) {
                if(item) {
                    if(item.batch) {
                        if(item.batch.length > 0) {
                            if (!parentItem.selectedBatch) {
                                parentItem['isParentBatchValid'] = true;
                                growl.error("Please Select Batch");
                                isValid = false;
                            }
                        }
                    }
                }
            }
            if (!!item) {
                if(item.batch) {
                    if(item.batch.length > 0) {
                        if (!item.selectedBatch) {
                            item['isBatchValid'] = true;
                            growl.error("Please Select Batch");
                            isValid = false;
                        }
                    }
                }                
            }
            return isValid;
        }

        var ValidateUOM = function (parentItem, item) {
            var isValid = true;

            if (!!parentItem) {
                if (!parentItem.selectedUom.uomUnitId) {
                    parentItem['isParentUOMValid'] = true;
                    growl.error("Please Select UOM");
                    isValid = false;
                }
            }
            if (!!item) {
                if (!item.selectedUom.uomUnitId) {
                    item['isUOMValid'] = true;
                    growl.error("Please Select UOM");
                    isValid = false;
                }
            }
            return isValid;
        }

      var CalculateOrederOrPickedQty = function(item){
        	 var pickedQty = 0;
        	 var quantity  = item.quantity == undefined ? 0 : item.quantity ; 
        	  if(item.selectedUom.uomTypeId == 3)
              {
        		  pickedQty  = parseInt(quantity);
                    
              }else if(item.selectedUom.uomTypeId == 2)
                  	{
                         var stripQty = item.uom[1].conversion * parseInt(quantity);
                         pickedQty  = stripQty;
                     
                  	}else if(item.selectedUom.uomTypeId == 1)
                  		 {
		                      var boxQty = item.uom[1].conversion *  item.uom[2].conversion * parseInt(quantity);
		                      pickedQty  = boxQty;
                  		 }  
        	  
        	  return pickedQty;
        }
        
  
        var ValidateQuantity = function (parentItem,parentIndex) {
            var isValid = true; var totalQuantitys = 0
            if (!!parentItem) {
              
            	  if (!parentItem.quantity || parentItem.quantity == 0 ) {
                      parentItem.isParentQuantityValid = true;
                      growl.error("Please enter quantity");
                      isValid = false;
                      return false;
                  }
              
                else if (parseInt(parentItem.quantity) > parentItem.stockAvailable) {
                    parentItem.isParentQuantityValid = true;
                    growl.error("Entered Quantity is exceeding stock available quantity");
                    isValid = false;
                    parentItem.quantity = 0;
                    return false;
                
                }
                else {
                    parentItem.quantity = parseInt(parentItem.quantity);
                    isValid = true;
                }
                
                $scope.PickedQty = 0;
                if(parentItem.selectedUom.uomTypeId == 3)
                {
                	 $scope.PickedQty  = parentItem.quantity;
                      if (parseInt(parentItem.quantity) > parentItem.newQty) {
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                isValid = false;
                      } else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                      }  
                } else  if(parentItem.selectedUom.uomTypeId == 2)
                      {
	                	 var stripQty = parentItem.uom[1].conversion * parseInt(parentItem.quantity);
	                     $scope.PickedQty  = stripQty;
	                   /*  if (parseInt(parentItem.quantity) == parentItem.newQty) {
	                    	 
	                    	 if (parentItem.quantityOrdered  == stripQty) {
	                    		 parentItem.quantity = parseInt(parentItem.quantity);
		                         isValid = true;
	                         }else {
	                        	  parentItem.isParentQuantityValid = true;
	                              growl.error("order Qty should not less than Strip Qty");
	                              parentItem.quantity = "0";
	                              isValid = false;
	                          }   
	                     } else*/ if(parseInt(parentItem.quantity) > parentItem.newQty){
	                    	  parentItem.isParentQuantityValid = true;
                              growl.error("Please enter quantity less than or equal to ordered quantity");
                              parentItem.quantity = "0";
                              isValid = false;
	                     }else{
	                    	 parentItem.quantity = parseInt(parentItem.quantity);
	                            isValid = true;
	                     }
	                    
                 }  else  if (parentItem.selectedUom.uomTypeId == 1)
                   {
                	  var boxQty = parentItem.uom[1].conversion * parentItem.uom[2].conversion * parseInt(parentItem.quantity);
                      $scope.PickedQty  =  boxQty;
                      /*if (parseInt(parentItem.quantity) == parentItem.newQty) {
	                    	 
	                    	 if (parentItem.quantityOrdered  == boxQty) {
	                    		 parentItem.quantity = parseInt(parentItem.quantity);
		                         isValid = true;
	                         }else {
	                        	  parentItem.isParentQuantityValid = true;
	                              growl.error("order Qty should not less than Box Qty");
	                              parentItem.quantity = "0";
	                              isValid = false;
	                          }   
	                     } else*/ if (parseInt(parentItem.quantity) > parentItem.newQty) {
                                parentItem.isParentQuantityValid = true;
                                growl.error("Please enter quantity less than or equal to ordered quantity");
                                parentItem.quantity = "0";
                                isValid = false;
                         }else {
                            parentItem.quantity = parseInt(parentItem.quantity);
                            isValid = true;
                          }   
                    }  
              if( isValid == true){
            	  var finalOrdQty = 0;
            	  var totalPickQty = 0;
            	  
            	  if (parentIndex == undefined) {
            	
		            	  angular.forEach(self.model.assigningPickerDispenserDetails.items, function (value, index) {
		                  	
			                  	if(value.itemId == parentItem.itemId){
			                  		
			                  		totalPickQty += CalculateOrederOrPickedQty(value);
			                  		
			                  		if(value.selectedItems != null && value.selectedItems.length>0){
			                      		
				                  		angular.forEach(value.selectedItems, function (selectValue, selectIndex)  {
				                  			
				                  			totalPickQty += CalculateOrederOrPickedQty(selectValue);
				                  			
				                 		 });
				                    }
			                  	}
			                  	
			                  
		                  });  
		            	  finalOrdQty = parentItem.finalOrderedQty;
            	     } else {
            	    	 
	            	    	 if(self.model.assigningPickerDispenserDetails.items[parentIndex].disabled == undefined){
	            	    		 totalPickQty += CalculateOrederOrPickedQty(self.model.assigningPickerDispenserDetails.items[parentIndex]);
	            	    	 }
         	                	
         	                	finalOrdQty = self.model.assigningPickerDispenserDetails.items[parentIndex].finalOrderedQty;
         	                	if(self.model.assigningPickerDispenserDetails.items[parentIndex].selectedItems != null && self.model.assigningPickerDispenserDetails.items[parentIndex].selectedItems.length>0){
         	                    		
         	                		angular.forEach(self.model.assigningPickerDispenserDetails.items[parentIndex].selectedItems, function (selectValue, selectIndex) {
         	                			
         	                			totalPickQty += CalculateOrederOrPickedQty(selectValue);
         	               		 	});
         	                  }
                     }
            	  
            	 
            	  if (finalOrdQty < totalPickQty) {
	       	              parentItem.isParentQuantityValid = true;
	                      growl.warning("  warning !! enter quantity is greater than ordered quantity ");
	                     // parentItem.quantity = "0";
            	  	}else {
            	  		
	                  parentItem.quantity = parseInt(parentItem.quantity);
            	  	}
              }
               
                
                
            }
       /*     if (!!item) {
                if (!item.quantity) {
                    item.isQuantityValid = true;
                    growl.error("Please enter quantity");
                    isValid = false;
                }
                else if (parseInt(item.quantity) > item.quantityOrdered) {
                    item.isQuantityValid = true;
                    growl.error("Please enter quantity less than or equal to ordered quantity");
                    isValid = false;
                }
                else if (!item.quantity > item.stockAvailable) {
                    item.isQuantityValid = true;
                    growl.error("Entered Quantity is exceeding stock available quantity");
                    isValid = false;
                }
            }*/
            return isValid;
        }

        var ValidateBill = function (bill) {

            var isValid = true;

            if (!bill.items || bill.items.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                bill.items.forEach(function (billitem) {

                    var totalQtyOfSelectedItems = 0;

                    if(billitem.batch) {
                        if(billitem.batch.length > 0) {
                            if (!billitem.disabled && !billitem.itemDisabled) {
                                billitem['isParentItemValid'] = false;
                                billitem['isParentBatchValid'] = false;
                                billitem['isParentUOMValid'] = false;
                                billitem['isParentQuantityValid'] = false;

                                if (!billitem.selectedBatch || !billitem.selectedUom || !billitem.quantity) {

                                    billitem['isParentItemValid'] = true;
                                    isValid = false;
                                }

                                if(billitem.batch) {
                                    // if(billitem.batch.length > 0) {
                                    //     if (!billitem.selectedBatch) {
                                    //         billitem['isParentBatchValid'] = true;
                                    //         isValid = false;
                                    //         growl.error('Select Batch')
                                    //     }
                                    // }
                                    if(billitem.selectedBatch) {
                                        isValid = true;
                                        billitem['isParentBatchValid'] = false;
                                    }
                                    if(billitem.batch.length < 0){
                                        billitem['isParentBatchValid'] = true;
                                                isValid = false;
                                                growl.error('Select Batch')
                                    }
                                    if (!billitem.selectedUom) {
                                        billitem['isParentUOMValid'] = true;
                                        isValid = false;
                                        growl.error('Select UOM')
    
                                    }
                                    if (!ValidateQuantity(billitem, null)) {
                                        isValid = false;
                                    }
                                }                        

                             
                            }

                            if (!ValidateUniqueBatchAndUom(billitem)) {
                                isValid = false;
                            }

                            if (billitem.selectedItems && !billitem.itemDisabled) {
                                billitem.selectedItems.forEach(function (item) {

                                    item['isItemValid'] = false;
                                    item['isBatchValid'] = false;
                                    item['isUOMValid'] = false;
                                    item['isQuantityValid'] = false;

                                    if (!item.selectedBatch || !item.selectedUom || !item.quantity) {
                                        item['isItemValid'] = true;
                                        isValid = false;
                                    }
                                    if (!item.selectedBatch) {
                                        item['isBatchValid'] = true;
                                        isValid = false;
                                        growl.error('Select Batch')
                                    }
                                    if (!item.selectedUom) {
                                        item['isUOMValid'] = true;
                                        isValid = false;
                                        growl.error('Select UOM')
                                    }

                                    totalQtyOfSelectedItems = totalQtyOfSelectedItems + parseInt(item.quantity);
                                    if (!item.quantity) {
                                        // totalQtyOfSelectedItems = totalQtyOfSelectedItems + parseInt(item.quantity);
                                        item['isQuantityValid'] = true;
                                        isValid = false;
                                        growl.error('Enter valid quantity')
                                    }
                                });

                                if (self.model.selectedQuantity < totalQtyOfSelectedItems) {
                                    billitem.selectedItems.forEach(function (item) {
                                        item['isQuantityValid'] = true;
                                        isValid = false;
                                        growl.error('Enter valid quantity')
                                    });
                                } else {
                                    billitem.selectedItems.forEach(function (item) {
                                        item['isQuantityValid'] = false;
                                    });
                                }
                                billitem.selectedItems.forEach(function (item) {
                                    if (!ValidateUniqueBatchAndUom(item)) {
                                        isValid = true;
                                    }
                                });

                            }
                        }
                    }

                });
            }

            return isValid;
        }

        var ClearBill = function (bill) {
            self.model.barcode = null;
        }

        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }

        var BarcodeScan = function (event, barcode) {
            var enterCode = 13;
            if (!barcode && event.which === enterCode) {
                barcode = self.model.barcode;
            }

            if (!!barcode) {
                var itemId = barcode.split(' ')[0];
                var batchId = barcode.split(' ')[1];

                if (!!itemId && !!batchId) {
                	
                	angular.forEach(self.model.assigningPickerDispenserDetails.items, function (item, key) {
             		   if(item.itemId==itemId){
             			   AddItem(key, self.model.assigningPickerDispenserDetails);
             		   }
             		   else{
             			   growl.warning("Selected barcode is not matching with prescription line item.");
             		   }
             	   });
                	
                	/*
                	var stopData = {
        	                "itemIds": [itemId],
        	                "storeId": $scope.storeId	      		 
        	         };
              	 	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                       var StopTransactionData = responseData;  
                       if(StopTransactionData.length > 0 ){    
                                var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                                var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                                growl.error("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");
                                $rootScope.stopLoader();                     
                                return false;
                        }
                       else{
                    	   angular.forEach(self.model.assigningPickerDispenserDetails.items, function (item, key) {
                    		   if(item.itemId==itemId){
                    			   AddItem(key, self.model.assigningPickerDispenserDetails);
                    		   }
                    		   else{
                    			   growl.warning("Selected barcode is not matching with prescription line item.");
                    		   }
                    	   });
                       }
              	 	});
                  
                */}

                event.stopPropagation();
                event.preventDefault();

                SetFocus();
            }
        }

         var AllocateBatch = function (bill, itemId, batchId) {
            var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_BATCH_API + '1' + '/' + itemId;
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
                if (!!item) {
                item.quantity = parseInt(item.quantity) + 1;
                // CalculateQuantity(item, item.selectedUom, item.uom);
                // CalculateAmount(item);
                } else {
                angular.forEach(response.data.batch, function (value, key) {
                }, response.data);
                var batch = response.data.batch.find(function (obj) {
                    return obj.batchId == batchId;
                });
                item = {
                    allocationId: response.data.allocationId,
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
                    isOTC: response.data.isOTC
                };
                if (!!batch) {
                    item.selectedBatch = batch;
                    item.uom = item.uom.concat(batch.uom);
                    item.selectedUom = batch.uom.find(function (obj) {
                    return obj.opUom;
                    });
                    item.quantity = 1;
                } else {
                    item.selectedBatch = item.batch[0];
                    item.selectedUom = item.uom[0];
                }
                // CalculateQuantity(item, item.selectedUom, item.uom);
                // CalculateRate(item, item.selectedUom, item.uom);
                // CalculateAmount(item);
                bill.items.push(item);
                var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API + $scope.storeId+'/'+itemId+'/'+ batchId+'/'+item.selectedUom.uomTypeId;
              GenericService.serviceAction("GET", URI, {}).then(
                function (response) 
                {
                  item.stockAvailable=response.data.availableqty;
                //   item.newStock =response.data.availableqty;
                  $rootScope.stopLoader();
                },
                function (err) 
                {
                  item.stock = 0;
                  item.newStock = 0;
                  $rootScope.stopLoader();
                });
                }
                $rootScope.stopLoader();
                }
                },
            function (ex) {
            growl.error(ex.data.message);
            $rootScope.stopLoader();
            });
        }

        var AddBatchToDetail = function (worklist, itemId, batchId) {
            var itemId = itemId;
            var batchId = batchId;
            angular.forEach(self.model.assigningPickerDispenserDetails.items, function (item, key) {

                let temp = item.batch.find(function (batch) {
                    if (batch.batchId == batchId) {
                        item.selectedBatch = batch;
                        item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                            return obj.opUom;
                        });
                        // CalculateRate(item, item.selectedUom, item.uom)
                        item.quantity = item.quantity + 1;
                    }
                });

            });
            var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.AVAILABLE_QTY_API  + $scope.unitId + "/"+ $scope.storeId+'/'+itemId+'/'+ batchId+'/'+item.selectedUom.uomTypeId;
              GenericService.serviceAction("GET", URI, {}).then(
                function (response) 
                {
                  item.stock=response.data.availableqty;
                  item.newStock =response.data.availableqty;
                  $rootScope.stopLoader();
                },
                function (err) 
                {
                  item.stock = 0;
                  item.newStock = 0;
                  $rootScope.stopLoader();
                });
        }
        var ConfirmRemove = function (index, assigningPickerDispenserDetails, parentindex) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
            self.model.itemForRemove = index;
            self.model.pickerDetail = assigningPickerDispenserDetails;
            self.model.parentIndexNew = parentindex;
        }
        var smartIssueAssignPicker = function () {

            var URI = CONSTANTS.GLOBAL.SMART_ISSUE;

            var data = {
                "storeId": 1,
                "items": []
            };
            angular.forEach(self.model.assigningPickerDispenserDetails.items, function (item, key) {
                var storeItem = {
                    "itemId": item.itemId,
                    "issueQuantity": item.quantityOrdered
                }
                data.items.push(storeItem);
            });
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.smartIssueData = response.data;
                    self.model.smartIssueData.forEach(function (smart) {
                        angular.forEach(self.model.assigningPickerDispenserDetails.items, function (item, key) {
                            if (smart.itemId == item.itemId) {
                                item.batch.forEach(function (batch) {
                                    smart.batch.forEach(function (smartBatch) {
                                        if (batch.batchId == smartBatch.batchId) {
                                            batch.quantity = smartBatch.issueQuantity;
                                            item.selectedBatch = batch;
                                            batchOnChange(batch.selectedBatch, batch, item);
                                        }
                                    });

                                });
                            }
                        });
                    });
                    $rootScope.stopLoader();
                    growl.success(response.data.message);


                },

                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var batchOnChange = function (selectedBatch, batch, item) {

            var conversionPurchase;
            var conversionStock;
            var conversionDespense;
            item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                return obj.opUom;
            });
            ConvertQtyOrdered(item, item.selectedBatch.uom, item.selectedUom);
            // selectedBatch.uom.forEach(function (uom) {
            //     if (uom.uomTypeId == 1) {
            //         conversionPurchase = uom.conversion;
            //     }
            //     else if (uom.uomTypeId == 2) {
            //         conversionStock = uom.conversion
            //     }
            //     else {
            //         conversionDespense = uom.conversion
            //     }
            // });

            // if (storeUom.uomTypeId == 1) {
            //     batch.selectedBatch.displayQuantity = Math.round((((selectedBatch.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
            // }
            // else if (storeUom.uomTypeId == 2) {
            //     batch.selectedBatch.displayQuantity = Math.round((selectedBatch.availableStockQuantity / conversionStock) / conversionDespense);
            // }
            // else {
            //     batch.selectedBatch.displayQuantity = Math.round(selectedBatch.availableStockQuantity / conversionDespense);
            // }

            // batch.selectedBatch.uomType = storeUom.uomType;
            // batch.selectedBatch.uomTypeId = storeUom.uomTypeId;
            // batch.selectedBatch.uomUnit = storeUom.uomUnit;
            // batch.selectedBatch.uomUnitId = storeUom.uomUnitId;
            // batch.selectedBatch.displayQuantity = Math.round(selectedBatch.availableStockQuantity / storeUom.conversion);
        }

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

      
      /***Chetana**/
      var printAssigningPickerDispenserDetailsReport = function () {
          var data = {
              "appName": "HIS",
              "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
              "reportDesignFilePath": "get_pending_picker.rpt",
              "reportFormat": "PDF",
              "exportFileName": "AssigningPickerDispenser",
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
     /*****/ 
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assigningPickerDispenserDetails", {
            url: "/assigningPickerDispenserDetails",
            templateUrl: 'views/PharmacyInventory/InPatient/assigning-picker-dispenser-details.html',
            controller: "AssigningPickerDispenserDetails.Controller",
            controllerAs: "vm",
            title: 'Pending List for Assigning Picker'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AssigningPickerDispenserDetails.Controller", assigningPickerDispenserDetailsController)
})();