(function () {
    "use strict";

    function materialPendingListDetailsController($scope, $rootScope, $http, $state, commonDetailService, StatusService, $cookies, CONSTANTS, GenericService, $filter, growl,stopTransactionService) {
        var self = this;
        var selectItemList = [];
        var masterId;
        // Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                requestData: [],
            };
            self.model.fromMaterialPending = fromMaterialPending;
            self.model.ApprovePending = ApprovePending;
            self.model.uomSelectEvent = uomSelectEvent;
            self.model.batchOnChange = batchOnChange;
            self.model.batchChange = batchChange;
            self.model.AddDuplicateRow = AddDuplicateRow;
            self.model.validatePending = validatePending;
            self.model.validateIssueQty = validateIssueQty;
            self.model.RemoveRowData = RemoveRowData;
            self.model.setStatus = setStatus;
            self.model.generateStatus = generateStatus;
            self.model.SetFocus = SetFocus;
            self.model.BarcodeScan = BarcodeScan;
            self.model.calculateIssueQty = calculateIssueQty;
            self.model.getListByUserName = getListByUserName;
            //for substitute
            self.model.SubstituteItems = SubstituteItems;
            self.model.Add = Add;
            self.model.AddSubstituteItem = AddSubstituteItem;
            self.model.RemoveSubstituteItem = RemoveSubstituteItem;
            self.model.SelectSubstituteItem = SelectSubstituteItem;
            self.model.isItemChecked = false;
            self.model.RemoveSubstituteItem = RemoveSubstituteItem;
            self.model.validateSubstituteIssueQty = validateSubstituteIssueQty;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;
            self.model.SelectAllItems = SelectAllItems;
            fromMaterialPending();
        }

        var ConfirmRemove = function (index, materialIssuePendingDetail, parentindex) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
            self.model.itemForRemove = index;
            self.model.pickerDetail = materialIssuePendingDetail;
            self.model.parentIndexNew = parentindex;
        }

        var RemoveItem = function (index, bill,parentIndex) {
            var item = bill.items[index];
            var parentIndex = self.model.parentIndexNew;
            
            if (parentIndex == undefined &&  parentIndex == null && (item.allocationIds!=undefined || item.allocationIds!=null)) {
                var URI = CONSTANTS.PHARMACY.OP.WALK_IN_SALES.ALLOCATE_API + $scope.unitId + "/"+$scope.organizationId +"/"+ $cookies.getObject('cookieObject').storeId + "/" + bill.id + "/" + item.allocationIds;
                $rootScope.startLoader();
                GenericService.serviceAction("DELETE", URI, {}).then(
                function (response) {
                    $rootScope.stopLoader();
                    ValidateUniqueBatchAndUom(item);
                },
                function (ex) {
                    $rootScope.stopLoader();
                    growl.error('Error while deleting allocated quantity');
                });  
                CalculateItemSum(bill.items);
            } 
            
            var item = null;
            if (parentIndex == undefined &&  parentIndex == null) {
                $scope.removeItemFlag = true;
                item = bill.items[index];
                if(item.quantityOrdered == undefined){
                	$scope.removeItemFlag = false;
                }
                var itemCount = 0;
                angular.forEach(self.model.materialIssuePendingDetail.items, function (value, index) {  	
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

        $scope.common = {
            userId:"",
            empCode:"",
            empName:"",    
        };

        var getListByUserName = function(name){	
            var URI = BASE_URL + ROOT_URL + "/api/global/searchEmployeeByName";
            var data = {
                organizationId: $scope.organizationId,
                unitId: $scope.unitId,
                firstName: name
            }
            $rootScope.startLoader();
            return GenericService.serviceAction("POST",URI,data).then(function(response){
                if (response.data.status == "success")
                    $rootScope.stopLoader();
                    console.log("getListByUserName", response.data.listObject);
                return response.data.listObject;
            });
        }

        $scope.selectUserId = function(info, model,label) {
            console.log(info);
            $scope.common.userId = info.userId;
            $scope.common.empName = info.empName + '-' + info.empCode;
        }
        
        var uomSelectEvent = function (batch) {
            batch.uom.forEach(function (uom) {
                if (uom.uomTypeId == batch.uomTypeId) batch.selectedUom = uom;
            });
        }
        $scope.checkItemBatch = function (item)
        {
        	batchOnChange((item.batchData!=undefined && item.batchData!=null ? item.batchData[0] :item.batch[0]),(item.batchListData2!=undefined && item.batchListData2!=null ? item.batchListData2[0] : item.batchListData[0]),item);
        }
        var batchOnChange = function (batches, item, itemData) {
            var conversionPurchase;
            var conversionStock;
            var conversionDespense;
            if(item.selectedBatch.uom==undefined || item.selectedBatch.uom==null)
            	{
            	item.selectedBatch.uom=itemData.uom;
            	}
            if(self.model.materialIssuePendingDetail != undefined){
            	self.model.materialIssuePendingDetail.items.forEach(function (data) {
                    angular.forEach(data.batchListData2, function(bdata){
                        if(data.batchListData2.length > 1){
                            if(item.selectedBatch != ''){
                                if(bdata.batchId == batches.batchId){
                                    growl.error("Please Select Different Batch");
                                    item.selectedBatch = '';
                                }
                            }
                        }
                    });
                    
                    if(data.selectedItems != undefined && data.selectedItems.length > 0){
                        data.selectedItems.forEach(function (dataSub) {
                            angular.forEach(dataSub.batchListData2, function(bdataSub){
                                if(bdataSub.batchListData2.length > 1){
                                    if(item.selectedBatch != ''){
                                        if(bdataSub.batchId == batches){
                                            growl.error("Please Select Different Batch");
                                            item.selectedBatch = '';
                                        }
                                    }
                                }
                            });
                        });
                    }
                    if(data.selectedItems != undefined && data.selectedItems.length > 0){
                        data.selectedItems.forEach(function (dataSub) {
                            angular.forEach(dataSub.batchListData, function(bdataSub){
                                if(dataSub.batchListData.length > 1){
                                    if(item.selectedBatch != ''){
                                        if(bdataSub.batchId == batches.batchId){
                                            growl.error("Please Select Different Batch");
                                            item.selectedBatch = '';
                                        }
                                    }
                                }
                            });
                        });
                    }
                });
            }

            var uom = batches.uom.find(function (obj) {
                return obj.storeUom;
            });
            
            item.selectedBatch.batchNo = batches.batchNo;
            item.selectedBatch.expiry = batches.expiry;
            batches.uom.forEach(function (uom) {
                if (uom.uomTypeId == 1) {
                    conversionPurchase = uom.conversion;
                }
                else if (uom.uomTypeId == 2) {
                    conversionStock = uom.conversion
                }
                else {
                    conversionDespense = uom.conversion
                }
            });

            if(batches.issueQuantity == undefined || batches.issueQuantity == null || batches.issueQuantity == ""){
                batches.issueQuantity = 0;
            }

            if (uom) {
            	item.selectedBatch.uomType = (item.uomType != undefined && item.uomType != null && item.uomType != "" ) ? item.uomType :uom.uomType;
                item.selectedBatch.uomTypeId = (item.uomTypeId != undefined && item.uomTypeId != null && item.uomTypeId != "" ) ? item.uomTypeId :uom.uomTypeId;
                item.selectedBatch.uomUnit = (item.uomUnit != undefined && item.uomUnit != null && item.uomUnit != "" ) ? item.uomUnit :uom.uomUnit;
                item.selectedBatch.uomUnitId = (item.uomUnitId != undefined && item.uomUnitId != null && item.uomUnitId != "" ) ? item.uomUnitId :uom.uomUnitId;

                if(batches.issueQuantity == undefined || batches.issueQuantity == null || batches.issueQuantity == 0){
                    batches.issueQuantity = item.issueQuantity;
                }
                if(itemData.isThreeUOMApplicable == false){
                    if (item.selectedBatch.uomTypeId == 1) {
                        item.selectedBatch.displayQuantity = parseInt((((batches.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                        item.selectedBatch.receivedQtyCon = (batches.issueQuantity +"*"+ conversionPurchase);
                        item.selectedBatch.receivedQtyCalculation = (batches.issueQuantity * conversionPurchase);
                        item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
                        item.selectedBatch.receivedQtyConSub = (item.issueQuantity +"*"+ conversionPurchase);
                        item.selectedBatch.receivedQtyCalculationSub = (item.issueQuantity * conversionPurchase);
                        item.selectedBatch.uomUnitDataSub = item.selectedBatch.uom[2].uomUnit;
                    }
                    else if (item.selectedBatch.uomTypeId == 2) {
                        item.selectedBatch.displayQuantity = parseInt((batches.availableStockQuantity / conversionStock) / conversionDespense);
                    }
                    else {
                        item.selectedBatch.displayQuantity = parseInt(batches.availableStockQuantity / conversionDespense);
                        item.selectedBatch.receivedQtyCon = (batches.issueQuantity);
                        item.selectedBatch.receivedQtyCalculation = (batches.issueQuantity);
                        item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
                        item.selectedBatch.receivedQtyConSub = (item.issueQuantity);
                        item.selectedBatch.receivedQtyCalculationSub = (item.issueQuantity);
                        item.selectedBatch.uomUnitDataSub = item.selectedBatch.uom[2].uomUnit;
                    }
                }
                else{
                    if (item.selectedBatch.uomTypeId == 1) {
                        item.selectedBatch.displayQuantity = parseInt((((batches.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                        item.selectedBatch.receivedQtyCon = (batches.issueQuantity +"*"+ conversionPurchase +"*"+ conversionStock);
                        item.selectedBatch.receivedQtyCalculation = (batches.issueQuantity * conversionPurchase * conversionStock);
                        item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
                        item.selectedBatch.receivedQtyConSub = (item.issueQuantity +"*"+ conversionPurchase +"*"+ conversionStock);
                        item.selectedBatch.receivedQtyCalculationSub = (item.issueQuantity * conversionPurchase * conversionStock);
                        item.selectedBatch.uomUnitDataSub = item.selectedBatch.uom[2].uomUnit;
                    }
                    else if (item.selectedBatch.uomTypeId == 2) {
                        item.selectedBatch.displayQuantity = parseInt((batches.availableStockQuantity / conversionStock) / conversionDespense);
                        item.selectedBatch.receivedQtyCon = (batches.issueQuantity +"*"+ conversionStock);
                        item.selectedBatch.receivedQtyCalculation = (batches.issueQuantity * conversionStock);
                        item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
                        item.selectedBatch.receivedQtyConSub = (item.issueQuantity +"*"+ conversionStock);
                        item.selectedBatch.receivedQtyCalculationSub = (item.issueQuantity * conversionStock);
                        item.selectedBatch.uomUnitDataSub = item.selectedBatch.uom[2].uomUnit;
                    }
                    else {
                        item.selectedBatch.displayQuantity = parseInt(batches.availableStockQuantity / conversionDespense);
                        item.selectedBatch.receivedQtyCon = (batches.issueQuantity);
                        item.selectedBatch.receivedQtyCalculation = (batches.issueQuantity);
                        item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
                        item.selectedBatch.receivedQtyConSub = (item.issueQuantity);
                        item.selectedBatch.receivedQtyCalculationSub = (item.issueQuantity);
                        item.selectedBatch.uomUnitDataSub = item.selectedBatch.uom[2].uomUnit;
                    }
                }
            }
        }
        
        var batchChange = function (batches,item, itemid, itemData) {
            var conversionPurchase;
            var conversionStock;
            var conversionDespense;

            $scope.selectedBatch = batches;
            self.model.materialIssuePendingDetail.items.forEach(function (data) {
                if(itemid == data.itemId){
                    angular.forEach(data.batchListData1, function(bdata){
                        if(data.batchListData1.length > 1){
                            if(bdata.batchId == batches.batchId){
                                growl.error("Please Select Different Batch");
                                item.selectedBatch = '';
                            }
                        }
                    });
                }
                // var uom = item.uom.find(function (obj) {
                //     return obj.storeUom;
                // });
                angular.forEach(data.batch, function(batchItem){
                    if(batchItem.batchId == batches.batchId){
                        item.batchNo = batchItem.batchNo;
                        item.expiry = batchItem.expiry;
                        
                        /* start code for resolved HIS-10584 Unable to save material return */
                        //batchId not set so, It was passed wrong batchId. Need to set batchId.
                        item.batchId = batchItem.batchId;
                        /* end code for resolved HIS-10584 Unable to save material return */
                    
                        item.selectedBatch.uom.forEach(function (uom) {
                            if (uom.uomTypeId == 1) {
                                conversionPurchase = uom.conversion;
                            }
                            else if (uom.uomTypeId == 2) {
                                conversionStock = uom.conversion
                            }
                            else {
                                conversionDespense = uom.conversion
                            }
                        });
                        angular.forEach(item.selectedBatch.uom, function(s){
                            if(s.uomTypeId == item.uomTypeId){
                                item.uomType = s.uomDetails;
                            }
                            if(s.uomUnitId == item.uomUnitId){
                                item.uomUnit = s.uomUnit;
                            }
                        });
                        
                        item.uomType = item.uomType;
                        item.uomTypeId = item.uomTypeId;
                        item.uomUnit = item.uomUnit;
                        item.uomUnitId = item.uomUnitId;

                        if(item.issueQuantity == undefined || item.issueQuantity == null || item.issueQuantity == ""){
                            item.issueQuantity = 0;
                        }

                        if(itemData.isThreeUOMApplicable == false){
                            if (item.uomTypeId == 1) {
                                item.displayQuantity = parseInt((((batchItem.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                                item.receivedQtyCon = (item.issueQuantity +"*"+ conversionPurchase);
                                item.receivedQtyCalculation = (item.issueQuantity * conversionPurchase);
                                item.uomUnitData = item.uomListdata[2].uomUnit;
                            }
                            else if (item.uomTypeId == 2) {
                                item.displayQuantity = parseInt((batchItem.availableStockQuantity / conversionStock) / conversionDespense);
                            }
                            else {
                                item.displayQuantity = parseInt(batchItem.availableStockQuantity / conversionDespense);
                                item.receivedQtyCon = (item.issueQuantity);
                                item.receivedQtyCalculation = (item.issueQuantity);
                                item.uomUnitData = item.uomListdata[2].uomUnit;
                            }
                        }
                        else{
                            if (item.uomTypeId == 1) {
                                item.displayQuantity = parseInt((((batchItem.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                                item.receivedQtyCon = (item.issueQuantity +"*"+ conversionPurchase +"*"+ conversionStock);
                                item.receivedQtyCalculation = (item.issueQuantity * conversionPurchase * conversionStock);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if (item.uomTypeId == 2) {
                                item.displayQuantity = parseInt((batchItem.availableStockQuantity / conversionStock) / conversionDespense);
                                item.receivedQtyCon = (item.issueQuantity +"*"+ conversionStock);
                                item.receivedQtyCalculation = (item.issueQuantity * conversionStock);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else {
                                item.displayQuantity = parseInt(batchItem.availableStockQuantity / conversionDespense);
                                item.receivedQtyCon = (item.issueQuantity);
                                item.receivedQtyCalculation = (item.issueQuantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                    }
                });
            });
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
                    AddBatchToDetail(self.model.materialPickerDetail, itemId, batchId);
                }

                event.stopPropagation();
                event.preventDefault();

                SetFocus();
            }
        }

        function fromMaterialPending() {
            var pendingId = commonDetailService.getDataId();
            $rootScope.startLoader();
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_PENDING_ISSUE.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + pendingId;
            var data = {};
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    self.model.materialIssuePendingDetail = response.data;
                    $scope.count = self.model.materialIssuePendingDetail.items.length;
                  	if (!!self.model.materialIssuePendingDetail.items) {
                  		var totalIssuedQTy=0;
                  		 $scope.totalQuantityIssued=0;
                  		$scope.totalIndentQuantity=0;
                    self.model.materialIssuePendingDetail.items.forEach(function (item) {
                    	if(item.indentQuantity == item.issueQuantity){
                    		item.subBtnDisabled = true;
                    		item.removeBtndisabled = true;
                    	}
                    	else{
                    		item.subBtnDisabled = false;
                    		item.removeBtndisabled = false;
                    	}
                        if(item.parentMaterialIssueId == undefined && item.pendingQuantity != 0)
                 		{
                        	$scope.totalIndentQuantity=$scope.totalIndentQuantity+item.indentAppprovedQuantity;
                        	if(item.issueQuantity != 0){
                        		$scope.totalIndentQuantity = $scope.totalIndentQuantity - item.issueQuantity;
                        	}
                 		}
                        var batchesIssuedQty=0;
                        if(item.batches!=undefined)
                            {
                        for(let batch of item.batches)
                            {
                            batchesIssuedQty=batchesIssuedQty+batch.issueQuantity;
                            }
                            }
                        item.issuedQty=batchesIssuedQty;
                        $scope.totalQuantityIssued=$scope.totalQuantityIssued+batchesIssuedQty;
//                        item.issueQuantity=0;
                    	var totalIssuedQTy=0;
                    	totalIssuedQTy=totalIssuedQTy+item.issueQuantity;
                    	item.selectedItems.forEach(function (sub) {
                    		var batchesIssuedQtySub=0;
                            if(sub.newBatch!=undefined)
                                {
                            for(let batch of sub.newBatch)
                                {
                            	batchesIssuedQtySub=batchesIssuedQtySub+batch.issueQuantity;
                                }
                                }
                            sub.issuedQty=batchesIssuedQtySub;
//                            $scope.totalQuantityIssued=$scope.totalQuantityIssued+batchesIssuedQtySub;
//                            sub.issueQuantity=0;
                    		totalIssuedQTy=totalIssuedQTy+sub.issueQuantity;
                    	})
                    	item.pendingQuantity=(item.indentAppprovedQuantity!=undefined && item.indentAppprovedQuantity!=null ? item.indentAppprovedQuantity : 0)-(totalIssuedQTy!=undefined && totalIssuedQTy!=null ? totalIssuedQTy :0);
                    	item.selectedItems.forEach(function (sub) {
                    		sub.indentAppprovedQuantity=item.indentAppprovedQuantity;
                    		sub.pendingQuantity=item.pendingQuantity;
                            sub.uomDatalist = item.uom.find(function (uom) {
                                return sub.uomTypeId == uom.uomTypeId;
                            });  
                            sub.uomType = sub.uomDatalist.uomDetails;
                        
                            if(sub.issueQuantity == undefined || sub.issueQuantity == null || sub.issueQuantity == ""){
                                sub.issueQuantity = 0;
                            }
                  
                            var uomdata = sub.uomDatalist.uomDetails.split(' ')[0];
                            if(item.isThreeUOMApplicable == false){
                                if(uomdata == 'P'){
                                  sub.receivedQtyConSub = (sub.issueQuantity +"*"+ item.uomListdata[0].conversion);
                                  sub.receivedQtyCalculationSub = (sub.issueQuantity * item.uomListdata[0].conversion);
                                  sub.uomUnitDataSub = item.uomListdata[2].uomUnit;
                                  
                                }
                                else if(uomdata == 'S'){
                                  
                                }
                                else{
                                  sub.receivedQtyConSub = (sub.issueQuantity);
                                  sub.receivedQtyCalculationSub = (sub.issueQuantity);
                                  sub.uomUnitDataSub = item.uomListdata[2].uomUnit;
                                }
                            } 
                            else{
                                if(uomdata == 'P'){
                                  sub.receivedQtyConSub = (sub.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                  sub.receivedQtyCalculationSub = (sub.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                  sub.uomUnitDataSub = item.uom[2].uomUnit;
                                }
                                else if(uomdata == 'S'){
                                  sub.receivedQtyConSub = (sub.issueQuantity +"*"+ item.uom[1].conversion);
                                  sub.receivedQtyCalculationSub = (sub.issueQuantity * item.uom[1].conversion);
                                  sub.uomUnitDataSub = item.uom[2].uomUnit;
                                }
                                else{
                                    sub.receivedQtyConSub = (sub.issueQuantity);
                                    sub.receivedQtyCalculationSub = (sub.issueQuantity);
                                    sub.uomUnitDataSub = item.uom[2].uomUnit;
                                }
                            }
                            sub.newBatch.forEach(function (subData) {
                                subData.uomDatalist = item.uom.find(function (uom) {
                                    return subData.uomTypeId == uom.uomTypeId;
                                });  
                                subData.uomType = subData.uomDatalist.uomDetails;
                            
                                if(subData.issueQuantity == undefined || subData.issueQuantity == null || subData.issueQuantity == ""){
                                    subData.issueQuantity = 0;
                                }
                      
                                var uomdata = subData.uomDatalist.uomDetails.split(' ')[0];
                                if(item.isThreeUOMApplicable == false){
                                    if(uomdata == 'P'){
                                        subData.receivedQtyConSub = (subData.issueQuantity +"*"+ item.uomListdata[0].conversion);
                                        subData.receivedQtyCalculation = (subData.issueQuantity * item.uomListdata[0].conversion);
                                        subData.uomUnitData = item.uomListdata[2].uomUnit;
                                      
                                    }
                                    else if(uomdata == 'S'){
                                      
                                    }
                                    else{
                                        subData.receivedQtyConSub = (subData.issueQuantity);
                                        subData.receivedQtyCalculation = (subData.issueQuantity);
                                        subData.uomUnitData = item.uomListdata[2].uomUnit;
                                    }
                                } 
                                else{
                                    if(uomdata == 'P'){
                                        subData.receivedQtyConSub = (subData.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                                        subData.receivedQtyCalculation = (subData.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                                        subData.uomUnitData = item.uom[2].uomUnit;
                                    }
                                    else if(uomdata == 'S'){
                                        subData.receivedQtyConSub = (subData.issueQuantity +"*"+ item.uom[1].conversion);
                                        subData.receivedQtyCalculation = (subData.issueQuantity * item.uom[1].conversion);
                                        subData.uomUnitData = item.uom[2].uomUnit;
                                    }
                                    else{
                                        subData.receivedQtyConSub = (subData.issueQuantity);
                                        subData.receivedQtyCalculation = (subData.issueQuantity);
                                        subData.uomUnitData = item.uom[2].uomUnit;
                                    }
                                }
                            });
                    	})
                    	item.uomDatalist = item.uom.find(function (uom) {
                            return item.uomTypeId == uom.uomTypeId;
                        });     
                        item.uomType = item.uomDatalist.uomDetails;
                        if(item.isThreeUOMApplicable == false){
                            item.uomListdata = item.uom;
                            item.uom = [];
                            angular.forEach(item.uomListdata, function(data){
                              if(data.uomType != 'Stock'){
                                item.uom.push(data);
                              }
                            });
                        }       

                        if(item.issueQuantity == undefined || item.issueQuantity == null || item.issueQuantity == ""){
                            item.issueQuantity = 0;
                        }
              
                        var uomdata = item.uomDatalist.uomDetails.split(' ')[0];
                        if(item.isThreeUOMApplicable == false){
                            if(uomdata == 'P'){
                              item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomListdata[0].conversion);
                              item.receivedQtyCalculation = (item.issueQuantity * item.uomListdata[0].conversion);
                              item.uomUnitData = item.uomListdata[2].uomUnit;
                            }
                            else if(uomdata == 'S'){
                              
                            }
                            else{
                              item.receivedQtyCon = (item.issueQuantity);
                              item.receivedQtyCalculation = (item.issueQuantity);
                              item.uomUnitData = item.uomListdata[2].uomUnit;
                            }
                        } 
                        else{
                            if(uomdata == 'P'){
                              item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                              item.receivedQtyCalculation = (item.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                              item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else if(uomdata == 'S'){
                              item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[1].conversion);
                              item.receivedQtyCalculation = (item.issueQuantity * item.uom[1].conversion);
                              item.uomUnitData = item.uom[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.issueQuantity);
                                item.receivedQtyCalculation = (item.issueQuantity);
                                item.uomUnitData = item.uom[2].uomUnit;
                            }
                        }
                        if(self.model.materialIssuePendingDetail.status == 'Pending'){
                            if(item.batches && self.model.materialIssuePendingDetail.pickerName !=undefined)
                            {
                                item['batch'] = [item.batch[0]];
                                item['batch']['uomList'] = [];
                                item.batchListData2 = [];
                                angular.forEach(item.batches, function(batc, key){
                                    var obj = {
                                        batchId : '',
                                        batchNo : '',
                                        expiry : '',
                                        uomType : '',
                                        uomUnit : '',
                                        displayQuantity : '',
                                        pickerQuantity : '',
                                        uomTypeId : '',
                                        uomUnitId : ''
                                        }
                                    obj.batchId = batc.batchId;
                                    obj.batchNo = batc.batchNo;
                                    obj.expiry = batc.expiry;
                                    obj.uomTypeId = item.uomTypeId;
                                    obj.uomUnitId = item.uomUnitId;

                                    angular.forEach(batc.uom, function(u, k){
                                        if(u.uomTypeId==batc.uomTypeId)
                                        {
                                            obj.uomType = u.uomType;      
                                        }
                                    });
                                
                                    angular.forEach(batc.uom, function(u, k){
                                        if(u.uomUnitId==batc.uomUnitId)
                                        {
                                            obj.uomUnit = u.uomUnit;   
                                        }
                                    });
                                    obj.displayQuantity = batc.availableStockQuantity;
                                    obj.pickerQuantity = batc.issueQuantity;
                                    obj.issueQuantity =batc.issueQuantity;
                                    item.batchListData2.push(obj);
                                });
                                angular.forEach(item.batchListData2, function (b){
                                    var conversionDespense;
                                    var conversionPurchase;
                                    var conversionStock;
                                    b.selectedBatch = item.batch[0];
                                    b.expiry = b.selectedBatch.expiry;
                                    b.selectedBatch = item.batch[0];
                                    b.expiry = b.selectedBatch.expiry;
                                    b.selectedBatch.uom.forEach(function (uom) {
                                        if (uom.uomTypeId == 1) {
                                            conversionPurchase = uom.conversion;
                                        }
                                        else if (uom.uomTypeId == 2) {
                                            conversionStock = uom.conversion
                                        }
                                        else {
                                            conversionDespense = uom.conversion
                                        }
                                    });
                                    var storeUom = b.selectedBatch.uom.find(function (obj) {
                                        return obj.storeUom;
                                    });
                                    item.uomData = item.uom.find(function (uom) {
                                        return item.uomTypeId == uom.uomTypeId;
                                    });     
                                    b.uomType = item.uomData.uomDetails;
                                    if(item.isThreeUOMApplicable == false){
                                        b.uomListdata = b.uom;
                                        b.uom = [];
                                        angular.forEach(b.uomListdata, function(data){
                                          if(data.uomType != 'Stock'){
                                            b.uom.push(data);
                                          }
                                        });
                                    }       

                                    if(b.issueQuantity == undefined || b.issueQuantity == null || b.issueQuantity == ""){
                                        b.issueQuantity = 0;
                                    }
                          
                                    var uomdata = item.uomData.uomDetails.split(' ')[0];
                                    if(item.isThreeUOMApplicable == false){
                                        if(uomdata == 'P'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uomListdata[0].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uomListdata[0].conversion);
                                          b.uomUnitData = b.uomListdata[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                          
                                        }
                                        else{
                                          b.receivedQtyCon = (b.issueQuantity);
                                          b.receivedQtyCalculation = (b.issueQuantity);
                                          b.uomUnitData = b.uomListdata[2].uomUnit;
                                        }
                                    } 
                                    else{
                                        if(uomdata == 'P'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[0].conversion+"*"+ b.uom[1].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uom[0].conversion * b.uom[1].conversion);
                                          b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[1].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uom[1].conversion);
                                          b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                        else{
                                          b.receivedQtyCon = (b.issueQuantity);
                                          b.receivedQtyCalculation = (b.issueQuantity);
                                          b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                    }           
                                    b.uomTypeId = item.uomTypeId;
                                    b.uomUnit = item.uomUnit;
                                    b.uomUnitId = item.uomUnitId;
                                    if (b.uomTypeId == 1) {
                                        b.displayQuantity = parseInt((((b.selectedBatch.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                                    }
                                    else if (b.uomTypeId == 2) {
                                        b.displayQuantity = parseInt((b.selectedBatch.availableStockQuantity / conversionStock) / conversionDespense);
                                    }
                                    else {
                                        b.displayQuantity = parseInt(b.selectedBatch.availableStockQuantity / conversionDespense);
                                    }
                                });
                                // item.batches.forEach(function (batch) {
                                //     batch.batchId = batch.batchId.toString();
                                //     batchOnChange(batch, item);
                                // });
                            }
                            if(item.batches && !self.model.materialIssuePendingDetail.pickerName)
                            {
                                item['batch'] = [item.batch[0]];
                                item['batch']['uomList'] = [];
                                angular.forEach(item.batches, function(bat, key){
                                    angular.forEach(item.batch, function(batc, key){
                                        if(bat.batchId == batc.batchId){
                                            item.batchListData = [];
                                            var obj = {
                                                batchId : '',
                                                batchNo : '',
                                                expiry : '',
                                                uomType : '',
                                                uomUnit : '',
                                                displayQuantity : '',
                                                pickerQuantity : '',
                                                uomTypeId : '',
                                                uomUnitId : ''
                                            }
                                            obj.batchId = batc.batchId;
                                            obj.batchNo = batc.batchNo;
                                            obj.expiry = batc.expiry;
                                            obj.uomType = item.uomType;
                                            obj.uomTypeId = item.uomTypeId;
                                            obj.uomUnitId = item.uomUnitId;
                                            obj.uomUnit = item.uomUnit;
                                            obj.displayQuantity = batc.availableStockQuantity;
                                            obj.pickerQuantity = bat.issueQuantity;
                                            item.batchListData.push(obj);
                                        }
                                    });
                                });
                                angular.forEach(item.batchListData, function (b){
                                    var conversionDespense;
                                    var conversionPurchase;
                                    var conversionStock;
                                    b.selectedBatch = item.batch[0];
                                    b.expiry = b.selectedBatch.expiry;
                                    b.selectedBatch = item.batch[0];
                                    b.expiry = b.selectedBatch.expiry;
                                    b.selectedBatch.uom.forEach(function (uom) {
                                        if (uom.uomTypeId == 1) {
                                            conversionPurchase = uom.conversion;
                                        }
                                        else if (uom.uomTypeId == 2) {
                                            conversionStock = uom.conversion
                                        }
                                        else {
                                            conversionDespense = uom.conversion
                                        }
                                    });
                                    var storeUom = b.selectedBatch.uom.find(function (obj) {
                                        return obj.storeUom;
                                    });
                                    item.uomData = item.uom.find(function (uom) {
                                        return item.uomTypeId == uom.uomTypeId;
                                    });     
                                    b.uomType = item.uomData.uomDetails;
                                    if(item.isThreeUOMApplicable == false){
                                        b.uomListdata = b.uom;
                                        b.uom = [];
                                        angular.forEach(b.uomListdata, function(data){
                                          if(data.uomType != 'Stock'){
                                            b.uom.push(data);
                                          }
                                        });
                                    }       

                                    if(b.issueQuantity == undefined || b.issueQuantity == null || b.issueQuantity == ""){
                                        b.issueQuantity = 0;
                                    }
                          
                                    var uomdata = item.uomData.uomDetails.split(' ')[0];
                                    if(item.isThreeUOMApplicable == false){
                                        if(uomdata == 'P'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uomListdata[0].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uomListdata[0].conversion);
                                          b.uomUnitData = b.uomListdata[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                          
                                        }
                                        else{
                                          b.receivedQtyCon = (b.issueQuantity);
                                          b.receivedQtyCalculation = (b.issueQuantity);
                                          b.uomUnitData = b.uomListdata[2].uomUnit;
                                        }
                                    } 
                                    else{
                                        if(uomdata == 'P'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[0].conversion+"*"+ b.uom[1].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uom[0].conversion * b.uom[1].conversion);
                                          b.uomUnitData = b.uomListdata[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[1].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uom[1].conversion);
                                          b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                        else{
                                            b.receivedQtyCon = (b.issueQuantity);
                                            b.receivedQtyCalculation = (b.issueQuantity);
                                            b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                    }
                                    b.uomTypeId = item.uomTypeId;
                                    b.uomUnit = item.uomUnit;
                                    b.uomUnitId = item.uomUnitId;
                                    if (b.uomTypeId == 1) {
                                        b.displayQuantity = parseInt((((b.selectedBatch.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                                    }
                                    else if (b.uomTypeId == 2) {
                                        b.displayQuantity = parseInt((b.selectedBatch.availableStockQuantity / conversionStock) / conversionDespense);
                                    }
                                    else {
                                        b.displayQuantity = parseInt(b.selectedBatch.availableStockQuantity / conversionDespense);
                                    }
                                });
                                // item.batches.forEach(function (batch) {
                                //     batch.batchId = batch.batchId.toString();
                                //     batchOnChange(batch, item);
                                // });
                            }
                            if(item.batch && !self.model.materialIssuePendingDetail.pickerName){
                            
                            	var testIndex = null;
                            	angular.forEach(item.batch,function(v,k){
                            		if(v.uom.length > 0 && testIndex ==null ){
                            			testIndex = k;
                            		}
                            	});
                            	
                                item['batchData'] = [item.batch[testIndex]];
                                item['batchData']['uomList'] = [];
                                angular.forEach(item.batchData, function(bat, key){
                                    angular.forEach(item.batch, function(batc, key){
                                        if(bat.batchId == batc.batchId){
                                            item.batchListData1 = [];
                                            var obj = {
                                                batchId : '',
                                                batchNo : '',
                                                expiry : '',
                                                uomType : '',
                                                uomUnit : '',
                                                displayQuantity : '',
                                                availableStockQuantity: '',
                                                pickerQuantity : '',
                                                uomTypeId : '',
                                                uomUnitId : '',
                                                uom:[]
                                            }
                                            obj.batchId = batc.batchId;
                                            obj.batchNo = batc.batchNo;
                                            obj.expiry = "";
                                            obj.uomType = "";
                                            obj.uomTypeId = "";
                                            obj.uomUnitId = "";
                                            obj.uomUnit = "";
                                            obj.displayQuantity = "";
                                            obj.availableStockQuantity = "";
                                            obj.pickerQuantity = "";
                                            obj.uom = bat.uom;
                                            item.batchListData1.push(obj);
                                        }
                                    });
                                });
                                
                                angular.forEach(item.batchListData1, function (b){
                                    var conversionDespense;
                                    var conversionPurchase;
                                    var conversionStock;
                                    b.selectedBatch = item.batch[0];
                                    b.expiry = b.selectedBatch.expiry;
                                    b.selectedBatch.uom.forEach(function (uom) {
                                        if (uom.uomTypeId == 1) {
                                            conversionPurchase = uom.conversion;
                                        }
                                        else if (uom.uomTypeId == 2) {
                                            conversionStock = uom.conversion
                                        }
                                        else {
                                            conversionDespense = uom.conversion
                                        }
                                    });
                                    var storeUom = b.selectedBatch.uom.find(function (obj) {
                                        return obj.storeUom;
                                    });
                                    item.uomData = item.uom.find(function (uom) {
                                        return item.uomTypeId == uom.uomTypeId;
                                    });     
                                    b.uomType = item.uomData.uomDetails;
                                    if(item.isThreeUOMApplicable == false){
                                        b.uomListdata = b.uom;
                                        b.uom = [];
                                        angular.forEach(b.uomListdata, function(data){
                                          if(data.uomType != 'Stock'){
                                            b.uom.push(data);
                                          }
                                        });
                                    }       

                                    if(b.issueQuantity == undefined || b.issueQuantity == null || b.issueQuantity == ""){
                                        b.issueQuantity = 0;
                                    }
                          
                                    var uomdata = item.uomData.uomDetails.split(' ')[0];
                                    if(item.isThreeUOMApplicable == false){
                                        if(uomdata == 'P'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uomListdata[0].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uomListdata[0].conversion);
                                          b.uomUnitData = b.uomListdata[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                          
                                        }
                                        else{
                                          b.receivedQtyCon = (b.issueQuantity);
                                          b.receivedQtyCalculation = (b.issueQuantity);
                                          b.uomUnitData = b.uomListdata[2].uomUnit;
                                        }
                                    } 
                                    else{
                                        if(uomdata == 'P'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[0].conversion+"*"+ b.uom[1].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uom[0].conversion * b.uom[1].conversion);
                                          b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                          b.receivedQtyCon = (b.issueQuantity +"*"+ b.uom[1].conversion);
                                          b.receivedQtyCalculation = (b.issueQuantity * b.uom[1].conversion);
                                          b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                        else{
                                            b.receivedQtyCon = (b.issueQuantity);
                                            b.receivedQtyCalculation = (b.issueQuantity);
                                            b.uomUnitData = b.uom[2].uomUnit;
                                        }
                                    }
                                    b.uomTypeId = item.uomTypeId;
                                    b.uomUnit = item.uomUnit;
                                    b.uomUnitId = item.uomUnitId;
                                    if (b.uomTypeId == 1) {
                                        b.displayQuantity = parseInt((((b.selectedBatch.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                                    }
                                    else if (b.uomTypeId == 2) {
                                        b.displayQuantity = parseInt((b.selectedBatch.availableStockQuantity / conversionStock) / conversionDespense);
                                    }
                                    else {
                                        b.displayQuantity = parseInt(b.selectedBatch.availableStockQuantity / conversionDespense);
                                    }
                                });
                            }
                            if(item.stockAvailable == 0){
                                growl.error("Stock is not available");
                            }
                        }
                        else{
                            item.batchListData2 = [];
                            if(item.batches == undefined || item.batches.length == 0){
                                if(item.batch != undefined && item.batch != null){
                            	    item['batchData'] = [item.batch[0]];
                                    angular.forEach(item.batchData, function(bat, key){
                                        angular.forEach(item.batch, function(batc, key){
                                	        if(bat.batchId == batc.batchId){
                                                var obj = {
                                                    batchId : '',
                                                    batchNo : '',
                                                    expiry : '',
                                                    uomType : '',
                                                    uomUnit : '',
                                                    displayQuantity : '',
                                                    pickerQuantity : '',
                                                    uomTypeId : '',
                                                    uomUnitId : ''
                                                }
                                                obj.batchId = batc.batchId;
                                                obj.batchNo = batc.batchNo;
                                                obj.expiry = batc.expiry;
                                                obj.uomTypeId = item.uomTypeId;
                                                obj.uomUnitId = item.uomUnitId;

                                                angular.forEach(batc.uom, function(u, k){
                                                    if(u.uomTypeId == item.uomTypeId)
                                                    {
                                                        obj.uomType = u.uomDetails;      
                                                    }
                                                });
                                    
                                                angular.forEach(batc.uom, function(u, k){
                                                    if(u.uomUnitId == item.uomUnitId)
                                                    {
                                                        obj.uomUnit = u.uomUnit;   
                                                    }
                                                });
                                                
                                                if(item.isThreeUOMApplicable == false){
                                                    batc.uomListdata = batc.uom;
                                                    batc.uom = [];
                                                    angular.forEach(batc.uomListdata, function(data){
                                                        if(data.uomType != 'Stock'){
                                                            batc.uom.push(data);
                                                        }
                                                    });
                                                }       

                                                if(batc.issueQuantity == undefined || batc.issueQuantity == null || batc.issueQuantity == ""){
                                                    batc.issueQuantity = 0;
                                                }
                          
                                                var uomdata = item.uomType.split(' ')[0];
                                                if(item.isThreeUOMApplicable == false){
                                                    if(uomdata == 'P'){
                                                        batc.receivedQtyCon = (batc.issueQuantity +"*"+ batc.uomListdata[0].conversion);
                                                        batc.receivedQtyCalculation = (batc.issueQuantity * batc.uomListdata[0].conversion);
                                                        batc.uomUnitData = batc.uomListdata[2].uomUnit;
                                                    }
                                                    else if(uomdata == 'S'){
                                          
                                                    }
                                                    else{
                                                        batc.receivedQtyCon = (batc.issueQuantity);
                                                        batc.receivedQtyCalculation = (batc.issueQuantity);
                                                        batc.uomUnitData = batc.uomListdata[2].uomUnit;
                                                    }
                                                } 
                                                else{
                                                    if(uomdata == 'P'){
                                                        batc.receivedQtyCon = (batc.issueQuantity +"*"+ batc.uom[0].conversion+"*"+ batc.uom[1].conversion);
                                                        batc.receivedQtyCalculation = (batc.issueQuantity * batc.uom[0].conversion * batc.uom[1].conversion);
                                                        batc.uomUnitData = batc.uom[2].uomUnit;
                                                    }
                                                    else if(uomdata == 'S'){
                                                        batc.receivedQtyCon = (batc.issueQuantity +"*"+ batc.uom[1].conversion);
                                                        batc.receivedQtyCalculation = (batc.issueQuantity * batc.uom[1].conversion);
                                                        batc.uomUnitData = batc.uom[2].uomUnit;
                                                    }
                                                    else{
                                                        batc.receivedQtyCon = (batc.issueQuantity);
                                                        batc.receivedQtyCalculation = (batc.issueQuantity);
                                                        batc.uomUnitData = batc.uom[2].uomUnit;
                                                    }
                                                }

                                                obj.displayQuantity = batc.availableStockQuantity;
                                                obj.pickerQuantity = batc.issueQuantity;
                                                obj.issueQuantity =batc.issueQuantity;
                                                batc.uomType = item.uomType;
                                                obj.selectedBatch = batc;
                                                if(item.isThreeUOMApplicable == false){
                                                    if (uomdata == 'P') {
                                                        obj.selectedBatch.displayQuantity = parseInt((((obj.selectedBatch.availableStockQuantity / batc.uomListdata[0].conversion) / batc.uomListdata[1].conversion) / batc.uomListdata[2].conversion));
                                                    }
                                                    else if (uomdata == 'S') {
                                                        obj.selectedBatch.displayQuantity = parseInt((obj.selectedBatch.availableStockQuantity / batc.uomListdata[1].conversion) / batc.uomListdata[2].conversion);
                                                    }
                                                    else {
                                                        obj.selectedBatch.displayQuantity = parseInt(obj.selectedBatch.availableStockQuantity / batc.uomListdata[2].conversion);
                                                    }
                                                }
                                                else{
                                                    if (uomdata == 'P') {
                                                        obj.selectedBatch.displayQuantity = parseInt((((obj.selectedBatch.availableStockQuantity / batc.uom[0].conversion) / batc.uom[1].conversion) / batc.uom[2].conversion));
                                                    }
                                                    else if (uomdata == 'S') {
                                                        obj.selectedBatch.displayQuantity = parseInt((obj.selectedBatch.availableStockQuantity / batc.uom[1].conversion) / batc.uom[2].conversion);
                                                    }
                                                    else {
                                                        obj.selectedBatch.displayQuantity = parseInt(obj.selectedBatch.availableStockQuantity / batc.uom[2].conversion);
                                                    }
                                                }
                                                item.batchListData2.push(obj);
                                                item.batches = [];
                                            }
                                        });
                                    });
                                }
                                item.batchListData2 = [item.batchListData2[0]];
                                var uomdata = item.uomType.split(' ')[0];
                                if(item.batchListData2[0] != undefined && item.batchListData2[0] != null){
                                    if(item.batchListData2[0].displayQuantity==undefined || item.batchListData2[0].displayQuantity==null || item.batchListData2[0].displayQuantity==0)
                                    {
                                        if(item.isThreeUOMApplicable == false){
                                            if(uomdata == 'P'){
                                                item.batch[0].displayQuantity=item.batch[0].availableStockQuantity/(item.batch[0].uom[0].conversion*item.batch[0].uom[1].conversion)
                                            }
                                            else if(uomdata == 'S'){
                                
                                            }
                                            else{
                                                item.batch[0].displayQuantity=item.batch[0].availableStockQuantity/(item.batch[0].uom[1].conversion)
                                            }
                                        } 
                                        else{
                                            if(uomdata == 'P'){
                                                item.batch[0].displayQuantity=item.batch[0].availableStockQuantity/(item.batch[0].uom[0].conversion*item.batch[0].uom[1].conversion*item.batch[0].uom[2].conversion)
                                            }
                                            else if(uomdata == 'S'){
                                                item.batch[0].displayQuantity=item.batch[0].availableStockQuantity/(item.batch[0].uom[1].conversion*item.batch[0].uom[2].conversion)
                                            }
                                            else{
                                                item.batch[0].displayQuantity=item.batch[0].availableStockQuantity/(item.batch[0].uom[2].conversion)
                                            }
                                        }
                                    }
                                    item.batchListData2[0].selectedBatch = (item.batchListData2[0].displayQuantity!=undefined && item.batchListData2[0].displayQuantity!=null && item.batchListData2[0].displayQuantity>0) ? item.batchListData2[0] :item.batch[0];
                                }
                            }
                            else{
                                angular.forEach(item.batches, function(batc, key){
                                    var obj = {
                                        batchId : '',
                                        batchNo : '',
                                        expiry : '',
                                        uomType : '',
                                        uomUnit : '',
                                        displayQuantity : '',
                                        pickerQuantity : '',
                                        uomTypeId : '',
                                        uomUnitId : ''
                                    }
                                    obj.batchId = batc.batchId;
                                    obj.batchNo = batc.batchNo;
                                    obj.expiry = batc.expiry;
                                    obj.uomTypeId = item.uomTypeId;
                                    obj.uomUnitId = item.uomUnitId;

                                    if(item.isThreeUOMApplicable == false){
                                    	angular.forEach(item.uomListdata, function(u, k){
                                            if(u.uomTypeId==batc.uomTypeId)
                                            {
                                                obj.uomType = u.uomDetails;      
                                            }
                                        });
                                        
                                        angular.forEach(item.uomListdata, function(u, k){
                                            if(u.uomUnitId==batc.uomUnitId)
                                            {
                                                obj.uomUnit = u.uomUnit;   
                                            }
                                        });
                                    }
                                    else{
                                    	angular.forEach(batc.uom, function(u, k){
                                            if(u.uomTypeId==batc.uomTypeId)
                                            {
                                                obj.uomType = u.uomDetails;      
                                            }
                                        });
                                        
                                        angular.forEach(batc.uom, function(u, k){
                                            if(u.uomUnitId==batc.uomUnitId)
                                            {
                                                obj.uomUnit = u.uomUnit;   
                                            }
                                        });
                                    }

                                    if(item.isThreeUOMApplicable == false){
                                    	if(batc.uom[2] == undefined || batc.uom[2] == null){
                                    		batc.uomListdata = item.uomListdata;
                                            batc.uom = [];
                                            angular.forEach(batc.uomListdata, function(data){
                                              if(data.uomType != 'Stock'){
                                                batc.uom.push(data);
                                              }
                                            });
                                    	}
                                    	else{
                                    		batc.uomListdata = batc.uom;
                                            batc.uom = [];
                                            angular.forEach(batc.uomListdata, function(data){
                                              if(data.uomType != 'Stock'){
                                                batc.uom.push(data);
                                              }
                                            });
                                    	}
                                    }       

                                    if(batc.issueQuantity == undefined || batc.issueQuantity == null || batc.issueQuantity == ""){
                                        batc.issueQuantity = 0;
                                    }
                          
                                    var uomdata = item.uomType.split(' ')[0];
                                    if(item.isThreeUOMApplicable == false){
                                        if(uomdata == 'P'){
                                            batc.receivedQtyCon = (batc.issueQuantity +"*"+ batc.uomListdata[0].conversion);
                                            batc.receivedQtyCalculation = (batc.issueQuantity * batc.uomListdata[0].conversion);
                                            batc.uomUnitData = batc.uomListdata[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                          
                                        }
                                        else{
                                            batc.receivedQtyCon = (batc.issueQuantity);
                                            batc.receivedQtyCalculation = (batc.issueQuantity);
                                            batc.uomUnitData = batc.uomListdata[2].uomUnit;
                                        }
                                    } 
                                    else{
                                        if(uomdata == 'P'){
                                            batc.receivedQtyCon = (batc.issueQuantity +"*"+ batc.uom[0].conversion+"*"+ batc.uom[1].conversion);
                                            batc.receivedQtyCalculation = (batc.issueQuantity * batc.uom[0].conversion * batc.uom[1].conversion);
                                            batc.uomUnitData = batc.uom[2].uomUnit;
                                        }
                                        else if(uomdata == 'S'){
                                            batc.receivedQtyCon = (batc.issueQuantity +"*"+ batc.uom[1].conversion);
                                            batc.receivedQtyCalculation = (batc.issueQuantity * batc.uom[1].conversion);
                                            batc.uomUnitData = batc.uom[2].uomUnit;
                                        }
                                        else{
                                            batc.receivedQtyCon = (batc.issueQuantity);
                                            batc.receivedQtyCalculation = (batc.issueQuantity);
                                            batc.uomUnitData = batc.uom[2].uomUnit;
                                        }
                                    }
                                    
                                    obj.displayQuantity = batc.availableStockQuantity;
                                    obj.pickerQuantity = batc.issueQuantity;
                                    obj.issueQuantity =batc.issueQuantity;
                                    item.batchListData2.push(obj);
                                });
                                angular.forEach(item.batchListData2 , function(d){
                                    d.issueQuantity=0;
                                })

                                item.batchListData2 = [item.batchListData2[0]];
                                var uomdata = item.uomType.split(' ')[0];
                                if(item.batchListData2[0].displayQuantity==undefined || item.batchListData2[0].displayQuantity==null || item.batchListData2[0].displayQuantity==0)
                                	{
                                	var checkDisplayQtyFlag=true;
                                	angular.forEach(item.batch , function(batch,key){
                                		if(checkDisplayQtyFlag==true)
                                			{
                                if(item.isThreeUOMApplicable == false){
                                    if(uomdata == 'P'){
                                    	item.batch[key].displayQuantity=Math.floor(item.batch[key].availableStockQuantity/(item.batch[key].uom[0].conversion*item.batch[key].uom[1].conversion))
                                    }
                                    else if(uomdata == 'S'){
                          
                                    }
                                    else{
                                    	item.batch[key].displayQuantity=Math.floor(item.batch[key].availableStockQuantity/(item.batch[key].uom[1].conversion))
                                    }
                                } 
                                else{
                                    if(uomdata == 'P'){
                                    	item.batch[key].displayQuantity=Math.floor(item.batch[key].availableStockQuantity/(item.batch[key].uom[0].conversion*item.batch[key].uom[1].conversion*item.batch[key].uom[2].conversion))
                                    }
                                    else if(uomdata == 'S'){
                                    	item.batch[key].displayQuantity=Math.floor(item.batch[key].availableStockQuantity/(item.batch[key].uom[1].conversion*item.batch[key].uom[2].conversion))
                                    }
                                    else{
                                    	item.batch[key].displayQuantity=Math.floor(item.batch[key].availableStockQuantity/(item.batch[key].uom[2].conversion))
                                    }
                                	}
                                if(item.batch[key].displayQuantity>0)
                                	{
                                	item.batchListData2[0].selectedBatch = (item.batchListData2[0].displayQuantity!=undefined && item.batchListData2[0].displayQuantity!=null && item.batchListData2[0].displayQuantity>0) ? item.batchListData2[0] :batch;
                                	checkDisplayQtyFlag=false;
                                	}
                                	}
                                	});
                                	}
                                else
                                	{
                                	if(item.isThreeUOMApplicable == false){
                                        if(uomdata == 'P'){
                                        	item.batchListData2[0].displayQuantity=Math.floor(item.batchListData2[0].displayQuantity/(item.uom[0].conversion*item.uom[1].conversion))
                                        }
                                        else if(uomdata == 'S'){
                              
                                        }
                                        else{
                                        	item.batchListData2[0].displayQuantity=Math.floor(item.batchListData2[0].displayQuantity/(item.uom[1].conversion))
                                        }
                                    } 
                                    else{
                                        if(uomdata == 'P'){
                                        	item.batchListData2[0].displayQuantity=Math.floor(item.batchListData2[0].displayQuantity/(item.uom[0].conversion*item.uom[1].conversion*item.uom[2].conversion))
                                        }
                                        else if(uomdata == 'S'){
                                        	item.batchListData2[0].displayQuantity=Math.floor(item.batchListData2[0].displayQuantity/(item.uom[1].conversion*item.uom[2].conversion))
                                        }
                                        else{
                                        	item.batchListData2[0].displayQuantity=Math.floor(item.batchListData2[0].displayQuantity/(item.uom[2].conversion))
                                        }
                                    	}
                                	item.batchListData2[0].selectedBatch = item.batchListData2[0];
                                	}
                                angular.forEach(item.batches, function(d){
                                    angular.forEach(d.uom, function(u){
                                        if(d.uomUnitId == u.uomUnitId){
                                            d.uomUnit = u.uomUnit;
                                        }
                                        if(d.uomTypeId == u.uomTypeId){
                                            d.uomType = u.uomDetails;
                                            d.conversion = u.conversion;
                                        }
                                    });
                                    var conversionDespense = 1;
                                    var conversionPurchase = 1;
                                    var conversionStock = 1;
                                    if (d.uomTypeId == 1) {
                                        conversionPurchase = d.conversion;
                                    }
                                    else if (d.uomTypeId == 2) {
                                        conversionStock = d.conversion
                                    }
                                    else {
                                        conversionDespense = d.conversion
                                    }
                                    if (d.uomTypeId == 1) {
                                        d.displayQuantity = parseInt((((d.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                                    }
                                    else if (d.uomTypeId == 2) {
                                        d.displayQuantity = parseInt((d.availableStockQuantity / conversionStock) / conversionDespense);
                                    }
                                    else {
                                        d.displayQuantity = parseInt(d.availableStockQuantity / conversionDespense);
                                    }
                                });
                            }
                            if(item.selectedItems != undefined && item.selectedItems.length > 0){
                            	angular.forEach(item.selectedItems, function(val , key){
                            		val.savedSubBatch = true;
                            		angular.forEach(val.newBatch, function(d){
                                        /*angular.forEach(d.uom, function(u){
                                            if(d.uomUnitId == u.uomUnitId){
                                                d.uomUnit = u.uomUnit;
                                            }
                                            if(d.uomTypeId == u.uomTypeId){
                                                d.uomType = u.uomType;
                                                d.conversion = u.conversion;
                                            }
                                        });*/
                                        var conversionDespense = 0;
                                        var conversionPurchase = 0;
                                        var conversionStock = 0;
                                        
                                        d.displayQuantity = parseInt(d.availableStockQuantity);
                                     
                                        
                                    });
                            	});
                            }
                            if(item.stockAvailable == 0){
                                growl.error("Stock is not available");
                            }
                        }
                    });
                	}
                    
                    
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('No Data Found');
                });
        }

        $scope.calculateReceivedQtyBatch = function (item, batch){
            if(item.issueQuantity == undefined || item.issueQuantity == null || item.issueQuantity == ""){
                item.issueQuantity = 0;
            }
            if(batch.issueQuantity == undefined || batch.issueQuantity == null || batch.issueQuantity == ""){
                batch.issueQuantity = 0;
            }
            if(item.isThreeUOMApplicable == false){
                if(item.uomDatalist.uomTypeId == 1){
                    item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomListdata[0].conversion);
                    item.receivedQtyCalculation = (item.issueQuantity * item.uomListdata[0].conversion);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
                    batch.selectedBatch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion);
                    batch.selectedBatch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[0].conversion);
                    batch.selectedBatch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                }
                else if(item.uomDatalist.uomTypeId == 2){
                
                }
                else{
                    item.receivedQtyCon = (item.issueQuantity);
                    item.receivedQtyCalculation = (item.issueQuantity);
                    item.uomUnitData = item.uomListdata[2].uomUnit;
                    batch.selectedBatch.receivedQtyCon = (batch.issueQuantity);
                    batch.selectedBatch.receivedQtyCalculation = (batch.issueQuantity);
                    batch.selectedBatch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                }
            }
            else{
                if(item.uomDatalist.uomTypeId == 1){
                    item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                    batch.selectedBatch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion+"*"+ batch.selectedBatch.uom[1].conversion);
                    batch.selectedBatch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[0].conversion * batch.selectedBatch.uom[1].conversion);
                    batch.selectedBatch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                }
                else if(item.uomDatalist.uomTypeId == 2){
                    item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[1].conversion);
                    item.receivedQtyCalculation = (item.issueQuantity * item.uom[1].conversion);
                    item.uomUnitData = item.uom[2].uomUnit;
                    batch.selectedBatch.receivedQtyCon = (batch.issueQuantity +"*"+ (batch.selectedBatch.uom!=undefined && batch.selectedBatch.uom!=null ? batch.selectedBatch.uom[1].conversion : item.uom[1].conversion ));
                    batch.selectedBatch.receivedQtyCalculation = (batch.issueQuantity * (batch.selectedBatch.uom!=undefined && batch.selectedBatch.uom!=null ? batch.selectedBatch.uom[1].conversion : item.uom[1].conversion ));
                    batch.selectedBatch.uomUnitData = batch.selectedBatch.uom!=undefined && batch.selectedBatch.uom!=null ? batch.selectedBatch.uom[2].uomUnit : item.uom[2].uomUnit ;
                }
                else{
                    item.receivedQtyCon = (item.issueQuantity);
                    item.receivedQtyCalculation = (item.issueQuantity);
                    item.uomUnitData = item.uom[2].uomUnit;
                    batch.selectedBatch.receivedQtyCon = (batch.issueQuantity);
                    batch.selectedBatch.receivedQtyCalculation = (batch.issueQuantity);
                    batch.selectedBatch.uomUnitData = batch.selectedBatch.uom!=undefined && batch.selectedBatch.uom!=null ? batch.selectedBatch.uom[2].uomUnit:item.uom[2].uomUnit;
                }
            }
        }

        $scope.calculateReceivedQtySub = function (item, batch){
            if(item.issueQuantity == undefined || item.issueQuantity == null || item.issueQuantity == ""){
                item.issueQuantity = 0;
            }
            if(batch.issueQuantity == undefined || batch.issueQuantity == null || batch.issueQuantity == ""){
                batch.issueQuantity = 0;
            }
            if(item.isThreeUOMApplicable == false){
                if(item.selectedUom.uomTypeId == 1){
                    item.receivedQtyConSub = (item.issueQuantity +"*"+ item.uoms[0].conversion);
                    item.receivedQtyCalculationSub = (item.issueQuantity * item.uoms[0].conversion);
                    item.uomUnitDataSub = item.uoms[2].uomUnit;
                    batch.selectedBatch.receivedQtyConSub = (batch.issueQuantity +"*"+ item.uoms[0].conversion);
                    batch.selectedBatch.receivedQtyCalculationSub = (batch.issueQuantity * item.uoms[0].conversion);
                    batch.selectedBatch.uomUnitDataSub = item.uoms[2].uomUnit;
                }
                else if(item.selectedUom.uomTypeId == 2){
                
                }
                else{
                    item.receivedQtyConSub = (item.issueQuantity);
                    item.receivedQtyCalculationSub = (item.issueQuantity);
                    item.uomUnitDataSub = item.uoms[2].uomUnit;
                    batch.selectedBatch.receivedQtyConSub = (batch.issueQuantity);
                    batch.selectedBatch.receivedQtyCalculationSub = (batch.issueQuantity);
                    batch.selectedBatch.uomUnitDataSub = item.uoms[2].uomUnit;
                }
            }
            else{
                if(item.selectedUom.uomTypeId == 1){
                    item.receivedQtyConSub = (item.issueQuantity +"*"+ item.uoms[0].conversion+"*"+ item.uoms[1].conversion);
                    item.receivedQtyCalculationSub = (item.issueQuantity * item.uoms[0].conversion * item.uoms[1].conversion);
                    item.uomUnitDataSub = item.uoms[2].uomUnit;
                    batch.selectedBatch.receivedQtyConSub = (batch.issueQuantity +"*"+ item.uoms[0].conversion+"*"+ item.uoms[1].conversion);
                    batch.selectedBatch.receivedQtyCalculationSub = (batch.issueQuantity * item.uoms[0].conversion * item.uoms[1].conversion);
                    batch.selectedBatch.uomUnitDataSub = item.uoms[2].uomUnit;
                }
                else if(item.selectedUom.uomTypeId == 2){
                    item.receivedQtyConSub = (item.issueQuantity +"*"+ item.uoms[1].conversion);
                    item.receivedQtyCalculationSub = (item.issueQuantity * item.uoms[1].conversion);
                    item.uomUnitDataSub = item.uoms[2].uomUnit;
                    batch.selectedBatch.receivedQtyConSub = (batch.issueQuantity +"*"+ item.uoms[1].conversion);
                    batch.selectedBatch.receivedQtyCalculationSub = (batch.issueQuantity * item.uoms[1].conversion);
                    batch.selectedBatch.uomUnitDataSub = item.uoms[2].uomUnit;
                }
                else{
                    item.receivedQtyConSub = (item.issueQuantity);
                    item.receivedQtyCalculationSub = (item.issueQuantity);
                    item.uomUnitDataSub = item.uoms[2].uomUnit;
                    batch.selectedBatch.receivedQtyConSub = (batch.issueQuantity);
                    batch.selectedBatch.receivedQtyCalculationSub = (batch.issueQuantity);
                    batch.selectedBatch.uomUnitDataSub = item.uoms[2].uomUnit;
                }
            }
        }

        $scope.calculateReceivedQty = function (item, batch) {
            if(batch.issueQuantity == undefined || batch.issueQuantity == null || batch.issueQuantity == ""){
                batch.issueQuantity = 0;
            }
            if(item.issueQuantity == undefined || item.issueQuantity == null || item.issueQuantity == ""){
                item.issueQuantity = 0;
            }
            if(item.isThreeUOMApplicable == false){
            	if(item.uomDatalist == undefined || item.uomDatalist == null){
            		if(item.uomTypeId == 1){
                        item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomListdata[0].conversion);
                        item.receivedQtyCalculation = (item.issueQuantity * item.uomListdata[0].conversion);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion);
                        batch.receivedQtyConSub = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion);
                        batch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[0].conversion);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
                    else if(item.uomTypeId == 2){
                    
                    }
                    else{
                        item.receivedQtyCon = (item.issueQuantity);
                        item.receivedQtyCalculation = (item.issueQuantity);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity);
                        batch.receivedQtyConSub = (batch.issueQuantity);
                        batch.receivedQtyCalculation = (batch.issueQuantity);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
            	}
            	else{
            		if(item.uomDatalist.uomTypeId == 1){
                        item.receivedQtyCon = (item.issueQuantity +"*"+ item.uomListdata[0].conversion);
                        item.receivedQtyCalculation = (item.issueQuantity * item.uomListdata[0].conversion);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion);
                        batch.receivedQtyConSub = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion);
                        batch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[0].conversion);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
                    else if(item.uomDatalist.uomTypeId == 2){
                    
                    }
                    else{
                        item.receivedQtyCon = (item.issueQuantity);
                        item.receivedQtyCalculation = (item.issueQuantity);
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity);
                        batch.receivedQtyConSub = (batch.issueQuantity);
                        batch.receivedQtyCalculation = (batch.issueQuantity);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
            	}
            }
            else{
            	if(item.uomDatalist == undefined || item.uomDatalist == null){
            		if(item.uomTypeId == 1){
                        item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion+"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyConSub = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion+"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[0].conversion * batch.selectedBatch.uom[1].conversion);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
                    else if(item.uomTypeId == 2){
                        item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.issueQuantity * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyConSub = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[1].conversion);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
                    else{
                        item.receivedQtyCon = (item.issueQuantity);
                        item.receivedQtyCalculation = (item.issueQuantity);
                        item.uomUnitData = item.uom[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity);
                        batch.receivedQtyConSub = (batch.issueQuantity);
                        batch.receivedQtyCalculation = (batch.issueQuantity);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
            	}
            	else{
            		if(item.uomDatalist.uomTypeId == 1){
                        item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion+"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyConSub = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[0].conversion+"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[0].conversion * batch.selectedBatch.uom[1].conversion);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
                    else if(item.uomDatalist.uomTypeId == 2){
                        item.receivedQtyCon = (item.issueQuantity +"*"+ item.uom[1].conversion);
                        item.receivedQtyCalculation = (item.issueQuantity * item.uom[1].conversion);
                        item.uomUnitData = item.uom[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyConSub = (batch.issueQuantity +"*"+ batch.selectedBatch.uom[1].conversion);
                        batch.receivedQtyCalculation = (batch.issueQuantity * batch.selectedBatch.uom[1].conversion);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
                    else{
                        item.receivedQtyCon = (item.issueQuantity);
                        item.receivedQtyCalculation = (item.issueQuantity);
                        item.uomUnitData = item.uom[2].uomUnit;
                        batch.receivedQtyCon = (batch.issueQuantity);
                        batch.receivedQtyConSub = (batch.issueQuantity);
                        batch.receivedQtyCalculation = (batch.issueQuantity);
                        batch.uomUnitData = batch.selectedBatch.uom[2].uomUnit;
                    }
            	}
            }
        }

        var calculateIssueQty = function (item, index) {
            // if(validateIssueQty(item)){
                $scope.quantitysds =0;
                item.QuantityInserted=true;
//                if(item.batches){    
//                    angular.forEach(item.batches, function(b){
//                        $scope.quantitysds += parseInt(b.issueQuantity);
//                    }); 
//                } 
                $scope.quantitysds1 = 0;
                angular.forEach(item.batchListData, function(data, key){     
                    $scope.quantitysds1 += parseInt(data.issueQuantity);
                }); 
                angular.forEach(item.batchListData1, function(data, key){     
                    $scope.quantitysds1 += parseInt(data.issueQuantity);
                }); 
                angular.forEach(item.batchListData2, function(data, key){     
                    $scope.quantitysds1 += parseInt(data.issueQuantity);
                });      
                if(!$scope.quantitysds1){
                    $scope.quantitysds1 = 0;
                }
                if(!$scope.quantitysds){
                    $scope.quantitysds = 0;
                }
                item.issueQuantity = $scope.quantitysds + $scope.quantitysds1;
            // }
        }

        var ApprovePending = function () {
            if( $scope.saveDisabled == true){
                $scope.saveDisabled = false;
        	    if (validatePending()) {
                    ActualApprovePending();
                }
            }
        }
        

        $scope.saveDisabled = true;
        var ActualApprovePending = function () {
            var store = self.model;
            var pId = commonDetailService.getDataId();
            var pickerId=self.model.materialIssuePendingDetail.pickerId;
            var count=0;
            console.log("Picker Details Id From ApprovePending : ",pickerId);

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_PENDING_ISSUE.SAVE_API;

            // var URI = constants.apiurl + constants.inventoryApi +
			// '/stores/material/issue/pending';
            if (validatePending()) {
                $scope.saveDisabled = false;
                var data = {
                    "id": pId,
                    "statusId": setStatus(generateStatus()),
                    "pickerDetailsId" : pickerId,
                    // "approvalStatusId": 1,
                     "carrierId": parseInt(self.model.id),
//                    "carrierId": $scope.common.userId,
                    "remark": self.model.remark,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId

                };
            
                angular.forEach(store.materialIssuePendingDetail.items, function (item, key) {
                	var issueQuantity1=0;
                	 var index=self.model.materialIssuePendingDetail.items.indexOf(item)
                	 var pendingQuantity=self.model.materialIssuePendingDetail.items[index].pendingQuantity;
            
                    if(self.model.materialIssuePendingDetail.items[index].QuantityInserted)
                    	{
                    	issueQuantity1=issueQuantity1+self.model.materialIssuePendingDetail.items[index].issueQuantity;
                    	}
                    	angular.forEach(self.model.materialIssuePendingDetail.items[index].selectedItems , function(subVal, index){
                    		if(subVal.QuantityInserted)
                        	{
                        	issueQuantity1=issueQuantity1+subVal.issueQuantity;
                        	}
                    	})
                      if(issueQuantity1>pendingQuantity)
                      {
                      	count++;
                      }
                	/*start code for resolved  HIS-10288 Not able to save material issue with multiple items*/
                	//add  condition pendingQuantity > 0 
                	/*end code for resolved  HIS-10288 Not able to save material issue with multiple items*/  
                	 //  if (item.issueQuantity > 0 && item.pendingQuantity > 0 ) { //HIS-11221
                	
                	if(item.selectedItems != undefined && item.selectedItems.length > 0){
                		angular.forEach(item.selectedItems , function(valSub, keySub){
                			if(valSub.issueQuantity != undefined && valSub.issueQuantity != 0 ){
                			 var storeSubItem = {
                					 "id": null ,
                                     "parentId":valSub.id,
                                     "itemId" : valSub.itemId,
                                     "storeIndentDetailId" : valSub.storeIndentDetailId,
                                     //"statusId": 17,
                                     "issueQuantity": valSub.issueQuantity,
                                     "batches": []
                                 }
                			 
                    		if(valSub.batchListData){
                                if(valSub.isBatchRequired){
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(valSub.batchListData, function(batch1){
                                            angular.forEach(valSub.batches, function (batch) {
                                                // if(batch.pendingQuantity == 0){
                                                    var batch = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": batch.id ? batch.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batch1.selectedBatch.batchId,
                                                        "uomTypeId": batch1.uomTypeId || batch1.selectedBatch.uomTypeId,
                                                        "uomUnitId": batch1.uomUnitId || batch1.selectedBatch.uomTypeId,
                                                        "issueQuantity":batch1.issueQuantity
                                                    }
                                                    if(valSub.QuantityInserted)
                                                	{
                                                    	storeSubItem.batches.push(batch);
                                                	}
                                                // }
                                            });
                                        });    
                                    }
                                    else{
                                        angular.forEach(valSub.batchListData, function(batch1){
                                            angular.forEach(valSub.batches, function (batch) {    
                                                var batch = {
                                                    //"id": item.id ? item.id : undefined,
                                                    //change because its a batchMapper id rather than materialIssueDetailId
                                                    // "id": batch.id ? batch.id : undefined,
                                                    "id": undefined,
                                                    "batchId": batch1.selectedBatch.batchId,
                                                    "uomTypeId":batch1.uomTypeId || batch1.selectedBatch.uomTypeId,
                                                    "uomUnitId":batch1.uomUnitId || batch1.selectedBatch.uomTypeId,
                                                    "issueQuantity": batch1.issueQuantity
                                                }
                                                if(valSub.QuantityInserted)
                                            	{
                                                	storeSubItem.batches.push(batch);
                                            	}
                                            
                                            });
                                        });
                                    }
                                }
                                else{
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(valSub.batchListData, function(batch1){
                                            angular.forEach(valSub.batches, function (batch) {
                                                // if(batch.pendingQuantity == 0){    
                                                    var batch = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": batch.id ? batch.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batch1.selectedBatch.batchId,
                                                        "uomTypeId": batch.uomTypeId || valSub.uomTypeId || batch1.selectedBatch.uomTypeId,
                                                        "uomUnitId": batch.uomUnitId || valSub.uomUnitId || batch1.selectedBatch.uomTypeId,
                                                        "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                    }
                                                    if(valSub.QuantityInserted)
                                                	{
                                                    	storeSubItem.batches.push(batch);
                                                	}
                                                // }
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(valSub.batchListData, function(batch1){
                                            angular.forEach(valSub.batches, function (batch) {
                                                var batch = {
                                                    //"id": item.id ? item.id : undefined,
                                                    //change because its a batchMapper id rather than materialIssueDetailId
                                                    // "id": batch.id ? batch.id : undefined,
                                                    "id": undefined,
                                                    "batchId": batch1.batchId,
                                                    "uomTypeId": batch.uomTypeId || valSub.uomTypeId || batch1.selectedBatch.uomTypeId,
                                                    "uomUnitId": batch.uomUnitId || valSub.uomUnitId || batch1.selectedBatch.uomTypeId,
                                                    "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                }
                                                if(valSub.QuantityInserted)
                                            	{
                                                	storeSubItem.batches.push(batch);
                                            	}
                                            });
                                        });
                                    }
                                }
                            }
                			 data.items.push(storeSubItem);
                		}
                		});
                	}
                    if(item.pendingQuantity != undefined && item.pendingQuantity != null && item.pendingQuantity != 0 
                    		&& (item.selectedItems == undefined || item.selectedItems.length == 0)){
                        if (item.issueQuantity > 0 ) { //HIS-11221
                            var storeItem = {
                                "id": item.id,
                                "itemId" : item.itemId,
                                //"statusId": 17,
                                "issueQuantity": item.issueQuantity,
                                "batches": []
                            }
                            if(item.batchListData){
                                if(item.isBatchRequired){
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {
                                                // if(batch.pendingQuantity == 0){
                                                    var batch = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": batch.id ? batch.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batch1.selectedBatch.batchId,
                                                        "uomTypeId": batch.uomTypeId || batch1.uomTypeId,
                                                        "uomUnitId": batch.uomUnitId || batch1.uomUnitId,
                                                        "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batch);
                                                    	}
                                                // }
                                            });
                                        });    
                                    }
                                    else{
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {    
                                                var batch = {
                                                    //"id": item.id ? item.id : undefined,
                                                    //change because its a batchMapper id rather than materialIssueDetailId
                                                    // "id": batch.id ? batch.id : undefined,
                                                    "id": undefined,
                                                    "batchId": batch.batchId,
                                                    "uomTypeId": batch.uomTypeId || batch1.uomTypeId,
                                                    "uomUnitId": batch.uomUnitId || batch1.uomUnitId,
                                                    "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                }
                                                if(item.QuantityInserted)
                                            	{
                                            	storeItem.batches.push(batch);
                                            	}
                                            
                                            });
                                        });
                                    }
                                }
                                else{
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {
                                                // if(batch.pendingQuantity == 0){    
                                                    var batch = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": batch.id ? batch.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batch1.selectedBatch.batchId,
                                                        "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                        "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                        "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batch);
                                                	}
                                                // }
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {
                                                var batch = {
                                                    //"id": item.id ? item.id : undefined,
                                                    //change because its a batchMapper id rather than materialIssueDetailId
                                                    // "id": batch.id ? batch.id : undefined,
                                                    "id": undefined,
                                                    "batchId": batch1.batchId,
                                                    "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                    "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                    "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                }
                                                if(item.QuantityInserted)
                                            	{
                                            	storeItem.batches.push(batch);
                                            	}
                                            });
                                        });
                                    }
                                }
                            }
                            else if(item.batchListData1){
                                if(item.isBatchRequired){
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){
                                                    angular.forEach(batchdata.uom, function(b){
                                                        if(b.uomUnit == batchdata.uomUnit){
                                                            databath.uomUnitId = b.uomUnitId;
                                                        }     
                                                        if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                        }
                                                    });
                                                    if(batchdata.batchId == ''){
                                                        batchdata.batchId = batchdata.selectedBatch;
                                                    }
                                                    if(databath.batchId == batchdata.batchId){
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": databath.batchId,
                                                            "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                            "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // }
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(batchdata.batchId == ''){
                                                    batchdata.batchId = batchdata.selectedBatch;
                                                }
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": databath.batchId,
                                                        "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                        "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }    
                                            });
                                        });
                                    }
                                }
                                else{
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){
                                                    angular.forEach(batchdata.uom, function(b){
                                                        if(b.uomUnit == batchdata.uomUnit){
                                                            databath.uomUnitId = b.uomUnitId;
                                                        }     
                                                        if(b.uomType == batchdata.uomType){
                                                                databath.uomTypeId = b.uomTypeId;
                                                        }
                                                    });
                                                    if(batchdata.batchId == ''){
                                                        batchdata.batchId = batchdata.selectedBatch;
                                                    }
                                                    if(databath.batchId == batchdata.batchId){
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": batchdata.selectedBatch.batchId,
                                                            "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                            "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // } 
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(batchdata.batchId == ''){
                                                    batchdata.batchId = batchdata.selectedBatch;
                                                }
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": databath.batchId,
                                                        "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                        "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }    
                                            });
                                        });
                                    }
                                }
                            }
                            else{
                                if(item.isBatchRequired || !item.isBatchRequired ){
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){
                                                    angular.forEach(batchdata.uom, function(b){
                                                        if(b.uomUnit == batchdata.uomUnit){
                                                            databath.uomUnitId = b.uomUnitId;
                                                        }     
                                                        if(b.uomType == batchdata.uomType){
                                                                databath.uomTypeId = b.uomTypeId;
                                                        }
                                                    });
                                                    if(databath.batchId == batchdata.selectedBatch.batchId){
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": batchdata.selectedBatch.batchId,
                                                            "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                            "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // } 
                                            });
                                        });
                                    }
                                    else{
                                         angular.forEach(item.batches, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batchdata.batchId,
                                                        "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                        "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }   
                                            });
                                        });
                                    }
                                }
                                else{
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batches, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){    
                                                    angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                    });
                                                    if(databath.batchId == batchdata.batchId){    
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": batchdata.selectedBatch.batchId,
                                                            "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                            "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // } 
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batches, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": databath.batchId,
                                                        "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                        "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }    
                                            });
                                        });
                                    }
                                }
                            }
                            // var items = item.batches.filter(function (detail) {
                            // return detail.itemId == item.itemId;
                            // });
                            data.items.push(storeItem);
                        }
                    }
                    else  if(item.pendingQuantity != undefined && item.pendingQuantity != null && item.pendingQuantity != 0 
                    		&& (item.selectedItems.length > 0)){
                        if (item.issueQuantity > 0 ) { //HIS-11221
                            var storeItem = {
                                "id": item.id,
                                "itemId" : item.itemId,
                                //"statusId": 17,
                                "issueQuantity": item.issueQuantity,
                                "batches": []
                            }
                            if(item.batchListData){
                                if(item.isBatchRequired){
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {
                                                // if(batch.pendingQuantity == 0){
                                                    var batch = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": batch.id ? batch.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batch1.selectedBatch.batchId,
                                                        "uomTypeId": batch.uomTypeId || batch1.uomTypeId,
                                                        "uomUnitId": batch.uomUnitId || batch1.uomUnitId,
                                                        "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batch);
                                                	}
                                                // }
                                            });
                                        });    
                                    }
                                    else{
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {    
                                                var batch = {
                                                    //"id": item.id ? item.id : undefined,
                                                    //change because its a batchMapper id rather than materialIssueDetailId
                                                    // "id": batch.id ? batch.id : undefined,
                                                    "id": undefined,
                                                    "batchId": batch.batchId,
                                                    "uomTypeId": batch.uomTypeId || batch1.uomTypeId,
                                                    "uomUnitId": batch.uomUnitId || batch1.uomUnitId,
                                                    "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                }
                                                if(item.QuantityInserted)
                                            	{
                                            	storeItem.batches.push(batch);
                                            	}
                                            
                                            });
                                        });
                                    }
                                }
                                else{
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {
                                                // if(batch.pendingQuantity == 0){    
                                                    var batch = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": batch.id ? batch.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batch1.selectedBatch.batchId,
                                                        "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                        "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                        "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batch);
                                                	}
                                                // }
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batchListData, function(batch1){
                                            angular.forEach(item.batches, function (batch) {
                                                var batch = {
                                                    //"id": item.id ? item.id : undefined,
                                                    //change because its a batchMapper id rather than materialIssueDetailId
                                                    // "id": batch.id ? batch.id : undefined,
                                                    "id": undefined,
                                                    "batchId": batch1.batchId,
                                                    "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                    "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                    "issueQuantity": batch.issueQuantity || batch1.issueQuantity
                                                }
                                                if(item.QuantityInserted)
                                            	{
                                            	storeItem.batches.push(batch);
                                            	}
                                            });
                                        });
                                    }
                                }
                            }
                            else if(item.batchListData1){
                                if(item.isBatchRequired){
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){
                                                    angular.forEach(batchdata.uom, function(b){
                                                        if(b.uomUnit == batchdata.uomUnit){
                                                            databath.uomUnitId = b.uomUnitId;
                                                        }     
                                                        if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                        }
                                                    });
                                                    if(batchdata.batchId == ''){
                                                        batchdata.batchId = batchdata.selectedBatch;
                                                    }
                                                    if(databath.batchId == batchdata.batchId){
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": databath.batchId,
                                                            "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                            "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // }
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(batchdata.batchId == ''){
                                                    batchdata.batchId = batchdata.selectedBatch;
                                                }
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": databath.batchId,
                                                        "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                        "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }    
                                            });
                                        });
                                    }
                                }
                                else{
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){
                                                    angular.forEach(batchdata.uom, function(b){
                                                        if(b.uomUnit == batchdata.uomUnit){
                                                            databath.uomUnitId = b.uomUnitId;
                                                        }     
                                                        if(b.uomType == batchdata.uomType){
                                                                databath.uomTypeId = b.uomTypeId;
                                                        }
                                                    });
                                                    if(batchdata.batchId == ''){
                                                        batchdata.batchId = batchdata.selectedBatch;
                                                    }
                                                    if(databath.batchId == batchdata.batchId){
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": batchdata.selectedBatch.batchId,
                                                            "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                            "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // } 
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batch, function(databath){
                                            angular.forEach(item.batchListData1, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(batchdata.batchId == ''){
                                                    batchdata.batchId = batchdata.selectedBatch;
                                                }
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": databath.batchId,
                                                        "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                        "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }    
                                            });
                                        });
                                    }
                                }
                            }
                            else{
                                if(item.isBatchRequired){
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batches, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){
                                                    angular.forEach(batchdata.uom, function(b){
                                                        if(b.uomUnit == batchdata.uomUnit){
                                                            databath.uomUnitId = b.uomUnitId;
                                                        }     
                                                        if(b.uomType == batchdata.uomType){
                                                                databath.uomTypeId = b.uomTypeId;
                                                        }
                                                    });
                                                    if(databath.batchId == batchdata.batchId){
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": batchdata.selectedBatch.batchId,
                                                            "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                            "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // } 
                                            });
                                        });
                                    }
                                    else{
                                         angular.forEach(item.batches, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": batchdata.batchId,
                                                        "uomTypeId": databath.uomTypeId || batchdata.uomTypeId,
                                                        "uomUnitId": databath.uomUnitId || batchdata.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }   
                                            });
                                        });
                                    }
                                }
                                else{
                                    if(store.materialIssuePendingDetail.status == 'Partially Completed'){
                                        angular.forEach(item.batches, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                // if(batchdata.pendingQuantity == 0){    
                                                    angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                    });
                                                    if(databath.batchId == batchdata.batchId){    
                                                        var batchdata1 = {
                                                            //"id": item.id ? item.id : undefined,
                                                            //change because its a batchMapper id rather than materialIssueDetailId
                                                            // "id": databath.id ? databath.id : undefined,
                                                            "id": undefined,
                                                            "batchId": batchdata.selectedBatch.batchId,
                                                            "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                            "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                            "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                        }
                                                        if(item.QuantityInserted)
                                                    	{
                                                    	storeItem.batches.push(batchdata1);
                                                    	}
                                                    }   
                                                // } 
                                            });
                                        });
                                    }
                                    else{
                                        angular.forEach(item.batches, function(databath){
                                            angular.forEach(item.batchListData2, function(batchdata){
                                                angular.forEach(batchdata.uom, function(b){
                                                    if(b.uomUnit == batchdata.uomUnit){
                                                        databath.uomUnitId = b.uomUnitId;
                                                    }     
                                                    if(b.uomType == batchdata.uomType){
                                                            databath.uomTypeId = b.uomTypeId;
                                                    }
                                                });
                                                if(databath.batchId == batchdata.batchId){
                                                    var batchdata1 = {
                                                        //"id": item.id ? item.id : undefined,
                                                        //change because its a batchMapper id rather than materialIssueDetailId
                                                        // "id": databath.id ? databath.id : undefined,
                                                        "id": undefined,
                                                        "batchId": databath.batchId,
                                                        "uomTypeId": batchdata.uomTypeId || item.uomTypeId,
                                                        "uomUnitId": batchdata.uomUnitId || item.uomUnitId,
                                                        "issueQuantity": batchdata.issueQuantity || batchdata.issueQuantity
                                                    }
                                                    if(item.QuantityInserted)
                                                	{
                                                	storeItem.batches.push(batchdata1);
                                                	}
                                                }    
                                            });
                                        });
                                    }
                                }
                            }
                            // var items = item.batches.filter(function (detail) {
                            // return detail.itemId == item.itemId;
                            // });
                            data.items.push(storeItem);
                        }
                    }
                });
                var itemList=[];
                angular.forEach(data.items, function(databath){
                	if(databath.batches.length!=0)
                		{
                		itemList.push(databath);
                		}
                })
                data.items=itemList;
                if(count>0)
                	{
                	growl.error("Issue Quantity Should be less than Pending Indent Quantity");
                	$scope.saveDisabled = true;
                    isValid = false;
                	}
                if (data.items.length > 0 && $scope.saveDisabled == false) {
                    $rootScope.startLoader();
                    data.unitId = $scope.unitId;
                    data.organizationId = $scope.organizationId;
                    GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            // ClearRequests();
                            $rootScope.stopLoader();
                            NavigteToMaterialPending();
                            growl.success(response.data.message);
                            $scope.saveDisabled = true;
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            //growl.error('Something Went Wrong');
                            growl.error('Please enter quantity.');
                        });
                }
                else {
                    $rootScope.stopLoader();
                    NavigteToMaterialPending();
                }
            }
        }
        var SelectAllItems = function () {

        	if (self.model.selectAllRows) {
        	if(self.model.SelectedItemsIds){
        	self.model.data = self.model.substituteDetails.map(function (item) {
        		self.model.SelectedItems.push(item);	
        	item.isSelected = true;
        	self.model.isItemChecked = true;
        	self.model.SelectedItemsIds.push(item.itemId);
        	return self.model.SelectedItemsIds;
        	});
        	}
        	else{
        	self.model.SelectedItemsIds = self.model.substituteDetails.map(function (item) {
        	self.model.SelectedItems.push(item);
        	item.isSelected = true;
        	self.model.isItemChecked = true;



        	return item.itemId;
        	});
        	}
        	} else {
        	self.model.SelectedItemsIds = [];
        	self.model.substituteDetails.forEach(function (item) {
        	self.model.SelectedItems.push(item);
        	item.isSelected = false;
        	self.model.isItemChecked = false;



        	});
        	}
        	}
        $scope.pIndex;
        function validatePending() {
            var tempList = [];
            $scope.count1 = 0;
            var selectedBatchIssueCount=0;
            if(self.model.materialIssuePendingDetail.items.length > 1){
                angular.forEach(self.model.materialIssuePendingDetail.items, function(l){
                    if(l.issueQuantity){
                        tempList.push(l);
                        $scope.count1++;
                    }
                    angular.forEach(l.selectedItems, function(sub){
                    	if(sub.issueQuantity){
                            tempList.push(l);
                            $scope.count1++;
                        }
                    })
                });
            }
            var isValid = true;
            if($scope.count1 != 0){
                var materialPendingDetail = tempList;
            }
            else{
                var materialPendingDetail = self.model.materialIssuePendingDetail.items;
            }
            var substitutePendingDetail = [];
            // !self.model.fromStore.id || !self.model.toStore.id ||
			// !self.model.priority.id ||
            if (materialPendingDetail.length < 1) {
                growl.error('Error', {
                    title: "Please validate all required fields"
                });
                isValid = false;
                $scope.saveDisabled = true;
                
                //for substitute chetana
                angular.forEach(materialPendingDetail, function(val, key){
                	if(val.selectedItems != undefined && val.selectedItems.length < 1){
                   	 growl.error('Error', {
                            title: "Please validate all required fields"
                        });
                        isValid = false;
                        $scope.saveDisabled = true;
                   }
                });
                
                
            }
            for (let pendingDetail of materialPendingDetail) {
            	if(pendingDetail.issueQuantity==0)
            	{
                    //growl.error("Please Enter Issue Quantity");
                    isValid = false;
                    $scope.saveDisabled = true;
            	}
                if (pendingDetail.issueQuantity < 0) {
                    growl.error("Please Enter Valid Issue Quantity");
                    isValid = false;
                    $scope.saveDisabled = true;
                    
                    //for substitute chetana
                    if(pendingDetail.selectedItems != undefined && pendingDetail.selectedItems.length > 0){
                    	for (let substitutePendingDetail of pendingDetail.selectedItems) {
                            if (substitutePendingDetail.issueQuantity < 0) {
                                growl.error("Please Enter Valid Issue Quantity");
                                isValid = false;
                                $scope.saveDisabled = true;
                            }
                        }
                    }
                }
                if (pendingDetail.issueQuantity == 0) {
                    //for substitute chetana
                    if(pendingDetail.selectedItems != undefined && pendingDetail.selectedItems.length > 0){
                    	for (let substitutePendingDetail of pendingDetail.selectedItems) {
                            if (substitutePendingDetail.issueQuantity < 0) {
                                growl.error("Please Enter Valid Issue Quantity");
                                isValid = false;
                                $scope.saveDisabled = true;
                            }
                        }
                    }
                }
            }
            for (let pendingDetail of materialPendingDetail) {
            	/*start code for resolved  HIS-10288 Not able to save material issue with multiple items*/
            	//add  condition pendingQuantity > 0 
            	/*end code for resolved  HIS-10288 Not able to save material issue with multiple items*/            	
                if (pendingDetail.issueQuantity > pendingDetail.indentQuantity && pendingDetail.indentQuantity > 0 ) {
                    growl.error("Issue Quantity Should be less than Pending Quantity");
                    isValid = false;
                    $scope.saveDisabled = true;
                }
                // var batchId = pendingDetail.batch.map(function (item) {
                // return item.batchId;
                // });
                // if (!method1(pendingDetail)) {
                // isValid = false;
                // }
                
                //for substitute chetana
                if(pendingDetail.selectedItems != undefined && pendingDetail.selectedItems.length > 0){
                	 for (substitutePendingDetail of pendingDetail.selectedItems) {
                         if (substitutePendingDetail.issueQuantity > substitutePendingDetail.indentQuantity && substitutePendingDetail.indentQuantity > 0 ) {
                             growl.error("Issue Quantity Should be less than Pending Quantity");
                             isValid = false;
                             $scope.saveDisabled = true;
                         }
                     }
                }
            }
            for (let pendingDetail of materialPendingDetail) {
                if(pendingDetail.pendingQuantity != 0 && pendingDetail.selectedItems != undefined){
                    if (pendingDetail.batchListData) {
                        for (let batchvalid of pendingDetail.batchListData){
                        	if(batchvalid.issueQuantity != undefined && batchvalid.issueQuantity != null && batchvalid.issueQuantity >0){
                        		selectedBatchIssueCount=selectedBatchIssueCount+1;
                             }
                            if(batchvalid.selectedBatch == '' || batchvalid.selectedBatch == undefined){
                                growl.error("Please Select Batch");
                                isValid = false;
                            	batchvalid.selectedBatch=pendingDetail.batchListData[0];
                                $scope.saveDisabled = true;
                            }
                            if(batchvalid.selectedBatch.displayQuantity){    
                                if(batchvalid.selectedBatch.displayQuantity == 0 || batchvalid.selectedBatch.displayQuantity == undefined){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(batchvalid.issueQuantity > batchvalid.selectedBatch.displayQuantity){
                                    growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                    batchvalid.issueQuantity = 0;
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                            else{    
                                if(batchvalid.displayQuantity == 0 || batchvalid.displayQuantity == undefined){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(batchvalid.issueQuantity > batchvalid.displayQuantity){
                                    growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                    batchvalid.issueQuantity = 0;
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                            if(pendingDetail.disabled == true){
                                for (substitutePendingDetail of pendingDetail.selectedItems) {
                                    if(substitutePendingDetail.issueQuantity == 0 || substitutePendingDetail.issueQuantity == undefined){
                                        growl.error("Enter issue quantity");
                                        isValid = false;
                                        $scope.saveDisabled = true;
                                    }
                                }
                            }
                            else{
                                if(batchvalid.issueQuantity == 0 || batchvalid.issueQuantity == undefined){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                        }
                        // if(pendingDetail.selectedBatch == ''){
                        //     growl.error("Please Select Batch");
                        //     isValid = false;
                        //     $scope.saveDisabled = true;
                        // }
                        if(pendingDetail.disabled == true){
                            for (substitutePendingDetail of pendingDetail.selectedItems) {
                                if(substitutePendingDetail.issueQuantity == 0 || substitutePendingDetail.issueQuantity == undefined){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                        }
                        else{
                            if(pendingDetail.issueQuantity == 0 || pendingDetail.issueQuantity == undefined){
                                growl.error("Enter issue quantity");
                                isValid = false;
                                $scope.saveDisabled = true;
                            }
                        }
                        if(pendingDetail.stockAvailable == 0){
                            growl.error("Stock is not available");
                            isValid = false;
                            $scope.saveDisabled = true;
                        }
                    }
                    if (pendingDetail.batchListData1) {
                        for (let batchvalid of pendingDetail.batchListData1){
                        	if(batchvalid.issueQuantity != undefined && batchvalid.issueQuantity != null && batchvalid.issueQuantity >0){
                        		selectedBatchIssueCount=selectedBatchIssueCount+1;
                             }
                            if(batchvalid.selectedBatch == '' || batchvalid.selectedBatch == undefined){
                                growl.error("Please Select Batch");
                                isValid = false;
                            	batchvalid.selectedBatch=pendingDetail.batchListData1[0];
                                $scope.saveDisabled = true;
                            }
                            if(batchvalid.selectedBatch.displayQuantity){    
                                if(batchvalid.selectedBatch.displayQuantity == 0 || batchvalid.selectedBatch.displayQuantity == undefined){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(batchvalid.issueQuantity > batchvalid.selectedBatch.displayQuantity){
                                    growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                    batchvalid.issueQuantity = 0;
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                            else{    
                                if(batchvalid.displayQuantity == 0 || batchvalid.displayQuantity == undefined){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(batchvalid.issueQuantity > batchvalid.displayQuantity){
                                    growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                    batchvalid.issueQuantity = 0;
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                            if(pendingDetail.disabled == true){
                                for (substitutePendingDetail of pendingDetail.selectedItems) {
                                    if(substitutePendingDetail.issueQuantity == 0 || substitutePendingDetail.issueQuantity == undefined){
                                        growl.error("Enter issue quantity");
                                        isValid = false;
                                        $scope.saveDisabled = true;
                                    }
                                }
                            }
                            else{
                                if(batchvalid.issueQuantity == 0 || batchvalid.issueQuantity == undefined){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                        }
                        // if(pendingDetail.selectedBatch == ''){
                        //     growl.error("Please Select Batch");
                        //     isValid = false;
                        //     $scope.saveDisabled = true;
                        // }
                        if(pendingDetail.disabled == true){
                            for (substitutePendingDetail of pendingDetail.selectedItems) {
                                if(substitutePendingDetail.issueQuantity == 0 || substitutePendingDetail.issueQuantity == undefined){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                        }
                        else{
                            if(pendingDetail.issueQuantity == 0 || pendingDetail.issueQuantity == undefined){
                                growl.error("Enter issue quantity");
                                isValid = false;
                                $scope.saveDisabled = true;
                            }
                        }
                        if(pendingDetail.stockAvailable == 0){
                            growl.error("Stock is not available");
                            isValid = false;
                            $scope.saveDisabled = true;
                        }
                    }
                    if ( pendingDetail.batchListData2!=undefined && pendingDetail.batchListData2!=null && pendingDetail.batchListData2.length>0 && pendingDetail.batchListData2 && pendingDetail.batchListData2[0]!=undefined) {
                        for (let batchvalid of pendingDetail.batchListData2){
                        	// if(batchvalid.selectedBatch==undefined || batchvalid.selectedBatch==null)
                        	// 	{
                        	// 	batchvalid['selectedBatch']=batchvalid;
                        	// 	}
                        	if(batchvalid.issueQuantity != undefined && batchvalid.issueQuantity != null && batchvalid.issueQuantity >0){
                        		selectedBatchIssueCount=selectedBatchIssueCount+1;
                             }
                            if(batchvalid.selectedBatch == '' || batchvalid.selectedBatch == undefined){
                                growl.error("Please Select Batch");
                                isValid = false;
                            	batchvalid.selectedBatch=pendingDetail.batchListData2[0];
                                $scope.saveDisabled = true;
                            }
                            if(batchvalid.selectedBatch.displayQuantity){
                                if(batchvalid.selectedBatch.displayQuantity == 0 || batchvalid.selectedBatch.displayQuantity == undefined){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(batchvalid.issueQuantity > batchvalid.selectedBatch.displayQuantity){
                                    growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                    batchvalid.issueQuantity = 0;
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                            else{
                                if(batchvalid.displayQuantity == 0 || batchvalid.displayQuantity == undefined){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(batchvalid.issueQuantity > batchvalid.displayQuantity){
                                    growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                    batchvalid.issueQuantity = 0;
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                            if(pendingDetail.disabled == true){
                                for (substitutePendingDetail of pendingDetail.selectedItems) {
                                    if(substitutePendingDetail.issueQuantity == 0 || substitutePendingDetail.issueQuantity == undefined){
                                        growl.error("Enter issue quantity");
                                        isValid = false;
                                        $scope.saveDisabled = true;
                                    }
                                }
                            }
                            else{
                                if(batchvalid.issueQuantity == 0 || batchvalid.issueQuantity == undefined){
                                    //                                    growl.error("Enter issue quantity");
                                    //                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                        }
                        // if(pendingDetail.selectedBatch == ''){
                        //     growl.error("Please Select Batch");
                        //     isValid = false;
                        //     $scope.saveDisabled = true;
                        // }
                        if(pendingDetail.disabled == true){
                            for (substitutePendingDetail of pendingDetail.selectedItems) {
                                if(substitutePendingDetail.issueQuantity == 0 || substitutePendingDetail.issueQuantity == undefined){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                            }
                        }
                        else{
                            if(pendingDetail.issueQuantity == 0 || pendingDetail.issueQuantity == undefined){
                                growl.error("Enter issue quantity");
                                isValid = false;
                                $scope.saveDisabled = true;
                            }
                        }
                        if(pendingDetail.stockAvailable == 0){
                            growl.error("Stock is not available");
                            isValid = false;
                            $scope.saveDisabled = true;
                        }
                    }
                }
                
                //for substitute chetana
                if(pendingDetail.selectedItems != undefined && pendingDetail.selectedItems.length > 0){
                	for (let substitutePendingDetail of pendingDetail.selectedItems) {
                        if(substitutePendingDetail.pendingQuantity != 0 && substitutePendingDetail.substituteBtn == true){
                            if (substitutePendingDetail.batchListData && substitutePendingDetail.isBatchRequired) {
                                for (let batchvalid of substitutePendingDetail.batchListData){
                                	if(batchvalid.issueQuantity != undefined && batchvalid.issueQuantity != null && batchvalid.issueQuantity >0){
                                		selectedBatchIssueCount=selectedBatchIssueCount+1;
                                     }
                                    if(batchvalid.selectedBatch == '' || batchvalid.selectedBatch == undefined){
                                        growl.error("Please Select Batch");
                                        isValid = false;
                                    	batchvalid.selectedBatch=pendingDetail.batchListData[0];
                                        $scope.saveDisabled = true;
                                    }
                                    if(batchvalid.selectedBatch.displayQuantity == 0 || batchvalid.selectedBatch.displayQuantity == undefined){
                                        growl.error("Stock is not available");
                                        isValid = false;
                                        $scope.saveDisabled = true;
                                    }
                                    if(batchvalid.issueQuantity > batchvalid.selectedBatch.displayQuantity){
                                        growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                        batchvalid.issueQuantity = 0;
                                        isValid = false;
                                        $scope.saveDisabled = true;
                                    }
                                    if(batchvalid.issueQuantity == 0 || batchvalid.issueQuantity == undefined){
                                        growl.error("Enter issue quantity");
                                        isValid = false;
                                        $scope.saveDisabled = true;
                                    }
                                }
                                if(substitutePendingDetail.issueQuantity != undefined && substitutePendingDetail.issueQuantity != null && substitutePendingDetail.issueQuantity !=''){
                                	selectedBatchIssueCount=selectedBatchIssueCount+1;
                                 }
                                if(substitutePendingDetail.selectedBatch == ''){
                                    growl.error("Please Select Batch");
                                    isValid = false;
                                	batchvalid.selectedBatch=substitutePendingDetail.batchListData[0];
                                    $scope.saveDisabled = true;
                                }
                                if(substitutePendingDetail.issueQuantity == 0){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(substitutePendingDetail.issueQuantity != 0){
                                    isValid = true;
                                }
                                if(substitutePendingDetail.stockAvailable == 0){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                    $scope.saveDisabled = true;
                                }
                                if(substitutePendingDetail.issueQuantity > substitutePendingDetail.indentQuantity){
//                                    growl.error("Issue Quantity Should be less than Pending Indent Quantity");
                                    isValid = false;
                                }
                            }
                           /* if (substitutePendingDetail.batchListData1 && substitutePendingDetail.isBatchRequired) {
                                for (let batchvalid of pendingDetail.batchListData1){
                                    if(batchvalid.selectedBatch == '' || batchvalid.selectedBatch == undefined){
                                        growl.error("Please Select Batch");
                                        isValid = false;
                                    }
                                    if(batchvalid.displayQuantity == 0 || batchvalid.displayQuantity == undefined){
                                        growl.error("Stock is not available");
                                        isValid = false;
                                    }
                                    if(batchvalid.issueQuantity > batchvalid.displayQuantity){
                                        growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                        batchvalid.issueQuantity = 0;
                                        isValid = false;
                                    }
                                    if(batchvalid.issueQuantity == 0 || batchvalid.issueQuantity == undefined){
                                        growl.error("Enter issue quantity");
                                        isValid = false;
                                    }
                                }
                                if(substitutePendingDetail.selectedBatch == ''){
                                    growl.error("Please Select Batch");
                                    isValid = false;
                                }
                                if(substitutePendingDetail.issueQuantity == 0){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                }
                                if(substitutePendingDetail.issueQuantity != 0){
                                    isValid = true;
                                }
                                
                                if(substitutePendingDetail.stockAvailable == 0){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                }
                            }
                            if (substitutePendingDetail.batchListData2 && substitutePendingDetail.isBatchRequired) {
                                for (let batchvalid of substitutePendingDetail.batchListData2){
                                    if(batchvalid.selectedBatch == '' || batchvalid.selectedBatch == undefined){
                                        growl.error("Please Select Batch");
                                        isValid = false;
                                    }
                                    if(batchvalid.displayQuantity == 0 || batchvalid.displayQuantity == undefined){
                                        growl.error("Stock is not available");
                                        isValid = false;
                                    }
                                    if(batchvalid.issueQuantity > batchvalid.displayQuantity){
                                        growl.error("Issue Quantity Should be less than Available Stock Quantity");
                                        batchvalid.issueQuantity = 0;
                                        isValid = false;
                                    }
                                    if(batchvalid.issueQuantity == 0 || batchvalid.issueQuantity == undefined){
                                        growl.error("Enter issue quantity");
                                        isValid = false;
                                    }
                                }
                                if(substitutePendingDetail.selectedBatch == ''){
                                    growl.error("Please Select Batch");
                                    isValid = false;
                                }
                                if(substitutePendingDetail.issueQuantity == 0){
                                    growl.error("Enter issue quantity");
                                    isValid = false;
                                }
                                if(substitutePendingDetail.stockAvailable == 0){
                                    growl.error("Stock is not available");
                                    isValid = false;
                                }
                            }*/
                        }
                    }
                }
            }
            if(selectedBatchIssueCount<=0)
            	{
            	growl.error("Please Enter Issue Quantity");
                isValid = false; 
                $scope.saveDisabled = true;
            	}
            else{
                isValid = true;         
            	}
            return isValid;
        }

        var validateIssueQty = function (item){
            var isValid = true;
            item.QuantityInserted=true;
            var index;
            if(self.model.parIndx==undefined || self.model.parIndx==null)
            	{
            	 index=self.model.materialIssuePendingDetail.items.indexOf(item)
            	}
            var pendingQuantity=self.model.materialIssuePendingDetail.items[self.model.parIndx!=undefined && self.model.parIndx!=null ? self.model.parIndx :index].pendingQuantity;
            if(item.batchListData){
                angular.forEach(item.batchListData, function(data1, key){
                    calculateIssueQty(item, key);
                    if(data1.selectedBatch == undefined || data1.selectedBatch == null){
                        growl.error("Please select batch");
                        data1.issueQuantity = 0;
                        isValid = false;
                    }
                    if(item.indentQuantity < item.issueQuantity){
                        growl.error("Issue Quantity Should be less than Indent Quantity");
                        // data1.issueQuantity = 0;
                        calculateIssueQty(item, key);
                        isValid = false;
                    }
                    else{
                    	calculateIssueQty(item, key);
                    }
                    if(data1.selectedBatch != undefined){
                        if(data1.issueQuantity > data1.selectedBatch.displayQuantity){
                            growl.error("Issue Quantity Should be less than Available Stock Quantity");
                            // data1.issueQuantity = 0;
                            isValid = false;
                        }
                        if(data1.issueQuantity > data1.selectedBatch.displayQuantity){
                            growl.error("Stock is not available");
                            // data1.issueQuantity = 0;
                            isValid = false;
                        }
                    }
                    if(self.model.parIndx != undefined && self.model.materialIssuePendingDetail.items[self.model.parIndx].issueQuantity !=undefined){
                    	var subIssuedQty=0;
                        angular.forEach(self.model.materialIssuePendingDetail.items[self.model.parIndx].selectedItems , function(val, index){
                        	if(val.issuedQty!=undefined)
                        		{
                        		subIssuedQty=subIssuedQty+parseInt(val.issuedQty);
                        		}
                            angular.forEach(val.batchListData , function(val1, index1){ 
                                    if(data1.batchId == val1.batchId){
                                       var totalIssueSum = self.model.materialIssuePendingDetail.items[self.model.parIndx].issueQuantity+(self.model.materialIssuePendingDetail.items[self.model.parIndx].QuantityInserted!=undefined && self.model.materialIssuePendingDetail.items[self.model.parIndx].QuantityInserted==true ? self.model.materialIssuePendingDetail.items[self.model.parIndx].issuedQty : 0) + parseInt(val.issueQuantity)+subIssuedQty;
                                       if(totalIssueSum > self.model.materialIssuePendingDetail.items[self.model.parIndx].indentQuantity){
                                            growl.error("Issue Quantity Should be less than Indent Quantity");
                                            val1.issueQuantity = 0;
                                            isValid = false;
                                       }   
                                    }
                            });
                        });
                    }
                });
            }
            if(item.batchListData1){
                // angular.forEach(item.batchListData1, function(data1, key){
                //     data1.issueQuantity = parseInt(data1.issueQuantity);
                //     item.issueQuantity += data1.issueQuantity;
                // });
                angular.forEach(item.batchListData1, function(data1, key){
                    calculateIssueQty(item, key);
                    if(data1.selectedBatch == undefined || data1.selectedBatch == null){
                        growl.error("Please select batch");
                        data1.issueQuantity = 0;
                        isValid = false;
                    }
                    if(item.indentQuantity < item.issueQuantity){
                        growl.error("Issue Quantity Should be less than Indent Quantity");
                        // data1.issueQuantity = 0;
                        calculateIssueQty(item, key);
                        isValid = false;
                    }
                    else{
                    	calculateIssueQty(item, key);
                    	}
                    if(data1.selectedBatch != undefined){
                        if(data1.issueQuantity > data1.selectedBatch.displayQuantity){
                            growl.error("Issue Quantity Should be less than Available Stock Quantity");
                            data1.issueQuantity = 0;
                            isValid = false;
                        }
                        if(data1.issueQuantity > data1.selectedBatch.displayQuantity){
                            growl.error("Stock is not available");
                            data1.issueQuantity = 0;
                            isValid = false;
                        }
                    }
                    if(data1.issueQuantity > data1.displayQuantity){
                        growl.error("Issue Quantity Should be less than Available Stock Quantity");
                        data1.issueQuantity = 0;
                        isValid = false;
                    }
                });
            }
            if(item.batchListData2){
                angular.forEach(item.batchListData2, function(data1,key){
                    calculateIssueQty(item, key);
                    if(data1.selectedBatch == undefined || data1.selectedBatch == null){
                        growl.error("Please select batch");
                        data1.issueQuantity = 0;
                        isValid = false;
                    }
                    if(item.indentQuantity < item.issueQuantity){
                        growl.error("Issue Quantity Should be less than Indent Quantity");
                        data1.issueQuantity = 0;
                        calculateIssueQty(item, key);
                        isValid = false;
                    }
                    else{
                    	calculateIssueQty(item, key);
                    	}
                    if(item.pendingQuantity < parseInt(data1.issueQuantity)){
                        growl.error("Issue Quantity Should be less than Pending Indent Quantity");
                        data1.issueQuantity = 0;
                        $scope.saveDisabled = true;
                        calculateIssueQty(item, key);
                        isValid = false;
                    }
                    if(item.pendingQuantity < parseInt(data1.issueQuantity)){
                        growl.error("Issue Quantity Should be less than Pending Indent Quantity");
                        data1.issueQuantity = 0;
                        calculateIssueQty(item, key);
                        $scope.saveDisabled = true;
                        isValid = false;
                    }
                    if(data1.selectedBatch != undefined){
                        if(data1.issueQuantity > data1.selectedBatch.displayQuantity){
                            growl.error("Issue Quantity Should be less than Available Stock Quantity");
                            data1.issueQuantity = 0;
                            isValid = false;
                        }
                        if(data1.issueQuantity > data1.selectedBatch.displayQuantity){
                            growl.error("Stock is not available");
                            data1.issueQuantity = 0;
                            isValid = false;
                        }
                    }
                });
            }
        	var issueQuantity1=0;
          if(self.model.materialIssuePendingDetail.items[self.model.parIndx!=undefined && self.model.parIndx!=null ? self.model.parIndx :index].QuantityInserted)
          	{
          	issueQuantity1=issueQuantity1+self.model.materialIssuePendingDetail.items[self.model.parIndx!=undefined && self.model.parIndx!=null ? self.model.parIndx :index].issueQuantity;
          	}
          	angular.forEach(self.model.materialIssuePendingDetail.items[self.model.parIndx!=undefined && self.model.parIndx!=null ? self.model.parIndx :index].selectedItems , function(subVal, index){
          		if(subVal.QuantityInserted)
              	{
              	issueQuantity1=issueQuantity1+subVal.issueQuantity;
              	}
          	})
            if(issueQuantity1>pendingQuantity)
            {
            	growl.error("Issue Quantity Should be less than Pending Indent Quantity");
            	$scope.saveDisabled = true;
                isValid = false;
            }
            return isValid;
        }

        function generateStatus() {

            var status = 'Completed';
            var materialPendingDetail = self.model.materialIssuePendingDetail.items;
            var substituteDetail = null;
            // $scope.count = self.model.materialIssuePendingDetail.items.length;
            $scope.statusCount = 0;
            if(materialPendingDetail != undefined && materialPendingDetail !=null){
                 for (let pendingDetail of materialPendingDetail)
            {
                     if(pendingDetail.QuantityInserted)
                    	 {
                    	 $scope.totalQuantityIssued=$scope.totalQuantityIssued+pendingDetail.issueQuantity;
                    	 }
                if (pendingDetail.issueQuantity < pendingDetail.indentAppprovedQuantity && pendingDetail.pendingQuantity != 0)
                {
                    status = 'Partially Completed';
                }
                else if(pendingDetail.issueQuantity == 0 && pendingDetail.pendingQuantity != 0)
                {
                    status = 'Pending';
                }
                else{
                    status = 'Completed';
                    $scope.statusCount = $scope.statusCount + 1; 
                }


                if(pendingDetail.selectedItems != undefined && pendingDetail.selectedItems != null && pendingDetail.selectedItems.length > 0){
                        for (let substituteDetail of pendingDetail.selectedItems)
                            {
                        	if(substituteDetail.QuantityInserted)
                       	 {
                       	 $scope.totalQuantityIssued=$scope.totalQuantityIssued+substituteDetail.issueQuantity;
                       	 }
                                    if (substituteDetail.issueQuantity < substituteDetail.indentAppprovedQuantity && substituteDetail.pendingQuantity != 0)
                                        {
                                            status = 'Partially Completed';
                                        }
                                        else if(substituteDetail.issueQuantity == 0 && substituteDetail.pendingQuantity != 0)
                                        {
                                            status = 'Pending';
                                        }
                                        else{
                                            status = 'Completed';
                                            $scope.statusCount = $scope.statusCount + 1; 
                                        }

                            }
                }
            }   
            
            }   
            
            

            if($scope.count <= $scope.statusCount){
                status = "Completed";
            }
            else{
                status = "Partially Completed";
            }
            if($scope.totalQuantityIssued>=$scope.totalIndentQuantity)
            	{
            	status = "Completed";
            	}
            else
            	{
            	status = "Partially Completed";
            	}
            return status;
        }
        
        function setStatus(x) {
            console.log("Checking Status : ",x);
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        StatusService.GetStatus().then(function (status) {
            self.model.statuses = status;
        });

        var AddDuplicateRow = function (item, batch) {
            if(item.batchListData){
                var data = angular.copy(batch);
                data.expiry = '';
                data.uomType = '';
                data.uomUnit = '';
                data.batchId = '';
                data.displayQuantity = '';
                data.issueQuantity = '';
                data.batchNo = '';
                data.statusId = '';
                data.selectedBatch = '';
                data.receivedQtyCon = 0;
                data.receivedQtyCalculation = 0;
                data.uomUnitData = "";
                item.batchListData.push(data);
            }
            else if(item.batchListData1){
                var data = angular.copy(batch);
                data.expiry = '';
                data.uomType = '';
                data.uomUnit = '';
                data.batchId = '';
                data.displayQuantity = '';
                data.issueQuantity = '';
                data.batchNo = '';
                data.statusId = '';
                data.selectedBatch = '';
                data.receivedQtyCon = 0;
                data.receivedQtyCalculation = 0;
                data.uomUnitData = "";
                item.batchListData1.push(data);
            }
            else{
                var data = angular.copy(batch);
                data.expiry = '';
                data.uomType = '';
                data.uomUnit = '';
                data.batchId = '';
                data.displayQuantity = '';
                data.issueQuantity = '';
                data.batchNo = '';
                data.statusId = '';
                data.selectedBatch = '';
                data.receivedQtyCon = 0;
                data.receivedQtyCalculation = 0;
                data.uomUnitData = "";
                item.batchListData2.push(data);
            }
        }


        var RemoveRowData = function (item , batch) {
            if(item.batchListData1){
                var index = item.batchListData1.indexOf(batch);
                item.batchListData1.splice(index, 1);
            }
            if(item.batchListData){
                var index = item.batchListData.indexOf(batch);
                item.batchListData.splice(index, 1);
            }
            if(item.batchListData2){
                var index = item.batchListData2.indexOf(batch);
                item.batchListData2.splice(index, 1);
            }
        }

        var NavigteToMaterialPending = function () {
            $state.go('materialIssue.pendingIssue');
        }

        
        //For Substitute List
        
        var isSelected = function (item) {
            return item.isSelected;
        }
        
        var SubstituteItems = function  (item,index) {
            // self.model.parentIndex=parentIndex;
       	  
       	 self.model.SelectedItems = [];
             self.model.parIndx = index;
             self.model.formulationTypeId = item.formulationTypeId;
             self.model.selectedParentId = item.id;
             self.model.instructionDesc = item.instructionDesc;
             self.model.duration = item.duration;
             self.model.frequencyDesc = item.frequencyDesc;
             self.model.selectedItemId = item.itemId;
             self.model.selectedGenricId = item.genericId;
             self.model.itemName = item.itemName;
             self.model.dose = item.dose;
             self.model.storeIndentDetailId = item.storeIndentDetailId;
             self.model.doseUnit = item.doseUnit!=undefined ? item.doseUnit : " ";
             self.model.genericName = item.genericName;
             self.model.productCategoryId = item.productCategoryId; 
             self.model.mainItmSelectedUomTypeId = item.uomTypeId; 
             self.model.mainItmSelectedUomType = item.uomType; 
             self.model.mainItmSelectedUomUnitId = item.uomUnitId; 
             self.model.mainItmSelectedUomUnit = item.uomUnit; 
             self.model.indentAppprovedQuantity = item.indentAppprovedQuantity;
             var subIssuedQty=0;
             item.selectedItems.forEach(function (subItem) {
            	 subIssuedQty=subIssuedQty+(subItem.issueQuantity!=null && subItem.issueQuantity!=undefined && subItem.issueQuantity!="" ? subItem.issueQuantity:0);
             })
             self.model.pendingQuantity = item.indentQuantity-(item.issueQuantity+subIssuedQty+(item.QuantityInserted!=undefined && item.QuantityInserted==true ? item.issuedQty:0 ));
             
             $scope.parentIssueDetailsId = item.id!=null ? item.id:item.parentIssueDetailsId;
             $scope.isFromAssignPicker = item.isFromAssignPicker;
             if(item.selectedUom == undefined || item.selectedUom.uomTypeId == undefined){
            	 self.model.selectedQuantity = item.quantityOrdered;
            }else{
            	  var mulQty = item.selectedUom.conversion * item.quantityIssued;
                  var  newOrderQty =  item.quantityOrdered - mulQty;
                  self.model.selectedQuantity = newOrderQty;
            }
            // self.model.selectedQuantity = item.quantityOrdered;
             self.model.substituteNewQty = item.indentQuantity;
             self.model.taxAmount = item.taxAmount;
             self.model.taxId = item.taxId;
             self.model.taxPercentage = item.taxPercentage;
             self.model.issuedQuantity = item.issuedQuantity;
             self.model.pendingIssueQuantity = item.pendingIssueQuantity;
             self.model.prescriptionDetailId = item.id!=null ? item.id:item.parentIssueDetailsId;
             self.model.frequency = item.frequency;
             self.model.dose = item.dose+' '+self.model.doseUnit;
             self.model.startDate = item.startDate;
             self.model.stopDate = item.stopDate;
             self.model.substituteDetails = [];
             self.model.selectAllRows = false;
             self.model.isItemChecked = false;
             self.model.pickedQuantity =item.pickedQuantity!=undefined ? item.pickedQuantity :0;
             item.isNarcotic = false;
             item.substititeFlag=true;
             
//             var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic;
             var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic+ '/' +  self.model.materialIssuePendingDetail.toStoreId+'/'+ self.model.formulationTypeId;
             $rootScope.startLoaderPopup();
             GenericService.serviceAction("GET", URI, {}).then(
                 function (response) {
                     self.model.substituteDetails = response.data;

                     if (!self.model.substituteDetails || self.model.substituteDetails == []) {
                         return;
                     } else {
                    	 var itemsList=[];
                         self.model.substituteDetails.forEach(function (item) {
                             self.model.selectAllRows = false;
                             item.isSelected = false;
                            /* item.batch.unshift({
                                 "batchDetails": "Select Batch"
                                 // "batchType": "Select Batch"
                             });
                             item.uom = [{
                                 "uomDetails": "Select UOM"
                             }],*/

                             //item.selectedBatch = item.batch[0];
                             item.batchDataList = item.batch;
                             item.batch = [];
                             angular.forEach(item.batchDataList, function(b){
                                if(b.availableStockQuantity != 0){
                                    item.batch.push(b);
                                }
                             })
                             var batch1=[];
                             angular.forEach(item.batch, function(b, key){
                                 var conversionDespense;
                                 var conversionPurchase;
                                 var conversionStock;
                             b.uom.forEach(function (uom) {
		                   	    if (uom.uomTypeId == 1) {
		                            conversionPurchase = uom.conversion;
		                        }
		                        else if (uom.uomTypeId == 2) {
		                            conversionStock = uom.conversion
		                        }
		                        else {
		                            conversionDespense = uom.conversion
		                        }
		                    });
		                    var storeUom = b.uom.find(function (obj) {
		                        return obj.storeUom;
		                    });
				                    b.uomType = self.model.mainItmSelectedUomType;
				                    b.uomTypeId = self.model.mainItmSelectedUomTypeId;
				                    b.uomUnit = self.model.mainItmSelectedUomUnit;
				                    b.uomUnitId = self.model.mainItmSelectedUomUnitId;
				                    if (self.model.mainItmSelectedUomTypeId == 1) {
//				                        b.availableStockQuantity = parseInt((((b.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
				                    }
				                    else if (self.model.mainItmSelectedUomTypeId == 2) {
//				                        b.availableStockQuantity = parseInt((b.availableStockQuantity / conversionStock) / conversionDespense);
				                    }
				                    else {
//				                        b.availableStockQuantity = parseInt(b.availableStockQuantity / conversionDespense);
				                    }   
				                    if(isNaN(b.availableStockQuantity) || b.availableStockQuantity==undefined || b.availableStockQuantity==null)
				                    	{
				                    	b.availableStockQuantity=0;
				                    	}
				                    if(b.availableStockQuantity!=0)
	                            	 {
	                            	 batch1.push(b);
	                            	 }
		                        });
                             item.batch=batch1;
                             if(item.batch.length>0)
                            	 {
                            	 itemsList.push(item)
                            	 }
                             item.selectedBatch = commonDetailService.getDefaultBatchFromConfig(item.batch);
                             //item.selectedUom = item.uom[1];
                         });
                         self.model.substituteDetails=itemsList;
                     }
                     $rootScope.stopLoaderPopup();
                 });

         }
        
        var Add = function () {
        	//$scope.savedSubItem=false;
            if (!self.model.SelectedItems || self.model.SelectedItems == undefined || self.model.SelectedItems == '') {
                growl.error('Please select items')
                return false;
            } else {
            	 if(validateSubSelectedBatch(self.model.SelectedItems)){
                self.model.SelectedItems.forEach(function (selectedItem) {
                	selectedItem['subBatchList'] = [];
                	selectedItem['itemName'] = selectedItem.itemName;
                    selectedItem['itemCode'] = selectedItem.itemCode;
                     selectedItem['genericName'] = selectedItem.genericName;
                    selectedItem['dose'] = selectedItem.dose;
                    selectedItem['indentQuantity'] = self.model.substituteNewQty;
                    selectedItem['id'] = self.model.selectedParentId;
                    selectedItem['substituteNewQty'] = self.model.substituteNewQty;
                    selectedItem['substituteBtn'] = true;
                    selectedItem['parentItemId'] = self.model.selectedItemId;
                    selectedItem['selectedUom'] = selectedItem.selectedUom;
                    selectedItem['selectedBatch'] = selectedItem.selectedBatch;
                    selectedItem['issueQuantity'] = selectedItem.issueQuantity;
                    selectedItem['isBatchRequired'] = selectedItem.isBatchRequired;
                    selectedItem['storeIndentDetailId'] = self.model.storeIndentDetailId;
                    selectedItem['savedSubItem'] = false;
                    selectedItem['itemId'] = selectedItem.itemId;
                    selectedItem['genericId'] =selectedItem.genericId;
                    selectedItem['currentStock'] = selectedItem.stockAvailable;
                    selectedItem['indentAppprovedQuantity'] = self.model.indentAppprovedQuantity; 
                    selectedItem['pendingQuantity'] = self.model.pendingQuantity;
//                    selectedItem['uomUnitId'] = selectedItem.uoms[2].uomUnitId;
//                    selectedItem['uomTypeId'] = selectedItem.uoms[2].uomTypeId;
//                    selectedItem['uomUnit'] = selectedItem.uoms[2].uomUnit;
//                    selectedItem['uomType'] = selectedItem.uoms[2].uomType;
                    selectedItem.batch.forEach(function (batch) {
                         selectedItem['rate'] = batch.sellingPrice;
                        });
                    selectedItem['batchListData']=[selectedItem.selectedBatch];
                    selectedItem['batches']=[selectedItem.selectedBatch];
                    selectedItem['newBatch']=[selectedItem.selectedBatch]; 
//                    selectedItem.batchListData.selectedBatch=[];
//                    selectedItem.batchListData.selectedBatch = [selectedItem.batch[0]];

                    selectedItem.batchListData.forEach(function (b) {
//                    	bth.selectedBatch = [selectedItem.batch[0]];
                    	 var conversionDespense;
                         var conversionPurchase;
                         var conversionStock;
                         b.selectedBatch = selectedItem.batch[0];
                         b.expiry = b.selectedBatch.expiry;
                         b.selectedBatch = selectedItem.batch[0];
                         b.expiry = b.selectedBatch.expiry;
                         b.selectedBatch.uom.forEach(function (uom) {
                        	 if (uom.uomTypeId == 1) {
                                 conversionPurchase = uom.conversion;
                             }
                             else if (uom.uomTypeId == 2) {
                                 conversionStock = uom.conversion
                             }
                             else {
                                 conversionDespense = uom.conversion
                             }
                         });
                         var storeUom = b.selectedBatch.uom.find(function (obj) {
                             return obj.storeUom;
                         });
                         b.uomType = self.model.mainItmSelectedUomType;
                         b.uomTypeId = self.model.mainItmSelectedUomTypeId;
                         b.uomUnit = self.model.mainItmSelectedUomUnit;
                         b.uomUnitId = self.model.mainItmSelectedUomUnitId;
                         
                         if(b.issueQuantity == undefined || b.issueQuantity == null || b.issueQuantity == ""){
                            b.issueQuantity = 0;
                         }

                         if(selectedItem.isThreeUOMApplicable == false){
                            if (self.model.mainItmSelectedUomTypeId == 1) {
                                b.receivedQtyConSub = (b.issueQuantity +"*"+ conversionPurchase);
                                b.receivedQtyCalculationSub = (b.issueQuantity * conversionPurchase);
                                b.uomUnitDataSub = b.selectedBatch.uom[2].uomUnit;
                            }
                            else if (self.model.mainItmSelectedUomTypeId == 2) {

                            }
                            else {
                                b.receivedQtyConSub = (b.issueQuantity);
                                b.receivedQtyCalculationSub = (b.issueQuantity);
                                b.uomUnitDataSub = b.selectedBatch.uom[2].uomUnit;
                            }
                         }
                         else{
                            if (self.model.mainItmSelectedUomTypeId == 1) {
                                b.receivedQtyConSub = (b.issueQuantity +"*"+ conversionPurchase +"*"+ conversionStock);
                                b.receivedQtyCalculationSub = (b.issueQuantity * conversionPurchase * conversionStock);
                                b.uomUnitDataSub = b.selectedBatch.uom[2].uomUnit;
                            }
                            else if (self.model.mainItmSelectedUomTypeId == 2) {
                                b.receivedQtyConSub = (b.issueQuantity +"*"+ conversionStock);
                                b.receivedQtyCalculationSub = (b.issueQuantity * conversionStock);
                                b.uomUnitDataSub = b.selectedBatch.uom[2].uomUnit;
                            }
                            else {
                                b.receivedQtyConSub = (b.issueQuantity);
                                b.receivedQtyCalculationSub = (b.issueQuantity);
                                b.uomUnitDataSub = b.selectedBatch.uom[2].uomUnit;
                            }
                         }
                         b.displayQuantity = b.selectedBatch.availableStockQuantity;
                         selectedItem['uomUnitId'] = b.uomUnitId;
                         selectedItem['uomTypeId'] = b.uomTypeId;
                         selectedItem['uomUnit'] = b.uomUnit;
                         selectedItem['uomType'] = b.uomType;
                         selectedItem['receivedQtyConSub'] = b.receivedQtyConSub;
                         selectedItem['receivedQtyCalculationSub'] = b.receivedQtyCalculationSub;
                         selectedItem['uomUnitDataSub'] = b.uomUnitDataSub;
                     });
                    /*selectedItem.uoms.forEach(function (uoms) {
                       if(uoms.ipUom==true){
                        selectedItem['uomUnitId'] = uoms.uomUnitId;
                        selectedItem['uomTypeId'] = uoms.uomTypeId;
                        selectedItem['uomUnit'] = uoms.uomUnit;
                        selectedItem['uomType'] = uoms.uomType;
                       }
                    });*/
                });
                $rootScope.startLoader();
                self.model.materialIssuePendingDetail.items.forEach(function (list) {
              	  
                	  if(!list.itemDisabled){
                    self.model.SelectedItems.forEach(function (item) {
                        item.uom = [{
                            uomDetails: "Select UOM",
                        }];
                        if(item.selectedBatch != undefined){    
                          if (!!item.selectedBatch.batchId) {
                        	  var conversionDespense=1;
                              var conversionPurchase=1;
                              var conversionStock=1;
                        	  item.selectedBatch.uom.forEach(function (uom) {
                             	 if (uom.uomTypeId == 1) {
                                      conversionPurchase = uom.conversion;
                                  }
                                  else if (uom.uomTypeId == 2) {
                                      conversionStock = uom.conversion;
                                  }
                                  else {
                                      conversionDespense = uom.conversion;
                                  }
                              });
                        	  if(item.isThreeUOMApplicable == false){
                        		  if(item.uomTypeId==1)
                        		  {
                        		  item.currentStock=math.floor(item.currentStock/(conversionPurchase*conversionDespense));
                        		  }
                        	  else if(item.uomTypeId==2)
                        		  {
                        		  
                        		  }
                        	  else
                        		  {
                        		  item.currentStock=math.floor(item.currentStock/(conversionDespense));
                        		  }
                        	  }else{
                        		  if(item.uomTypeId==1)
                        		  {
                        		  item.currentStock=math.floor(item.currentStock/(conversionPurchase*conversionStock*conversionDespense));
                        		  }
                        	  else if(item.uomTypeId==2)
                        		  {
                        		  item.currentStock=math.floor(item.currentStock/(conversionStock*conversionDespense));
                        		  }
                        	  else
                        		  {
                        		  item.currentStock=math.floor(item.currentStock/(conversionDespense));
                        		  }
                        		  
                        	  }
                        	  
                        	  item.availableStockQuantity=item.currentStock;
                              item.stockAvailable=item.selectedBatch.availableStockQuantity;
                              item.substituteNewQty = item.selectedBatch.availableStockQuantity;
                              item.uom = item.selectedBatch.uom;
                              item.selectedUom = $filter('filter')(item.selectedBatch.uom, {
                                  opUom: true
                              }, true)[0];
//                              item.selectedBatch['uomUnitId'] = item.selectedUom.uomUnitId;
//                        	  item.selectedBatch['uomTypeId'] = item.selectedUom.uomTypeId;
//                        	  item.selectedBatch['uomUnit'] = item.selectedUom.uomUnit;
//                        	  item.selectedBatch['uomType'] = item.selectedUom.uomType;
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
                        
                    });

                    if (self.model.selectedGenricId  == list.genericId) {
                      	if(list.substititeFlag){
                      	if((list.issueQuantity == null || list.issueQuantity == 0)){
                          list['disabled'] = true;
                          list.issueQuantity = null;
                      	}
                      	if(list.batchListData2!=undefined && list.batchListData2!=null)
        				{
                      	list.batchListData2.forEach(function (batch) {
                      		  list['disabled'] = true;
                      		if(batch!=undefined && batch!=null)
                            {
                                batch.disableItem=true;
                            }
                  		});
        				}
                      	if(list.batchListData1!=undefined && list.batchListData1!=null)
        				{
                      	list.batchListData1.forEach(function (batch) {
                      		  list['disabled'] = true;
                      		if(batch!=undefined && batch!=null)
                            {
                                batch.disableItem=true;
                            }
                  		});
        				}
                         
                         
                          // list.selectedUom = list.selectedUom[0];
                          
                          if (!list['selectedItems'])
                              list['selectedItems'] = self.model.SelectedItems;
                          else {
                              list['selectedItems'] = list['selectedItems'].concat(self.model.SelectedItems);
                          }
                          list.substititeFlag=false;
                          list.substititeCheckFlag=false;
                          console.log("selectedSubList", list);
                          $rootScope.stopLoader();
                      }
                      }
                }
                });
  			self.model.materialIssuePendingDetail.items[self.model.parIndx].isShow = false;
                self.model.SelectedItems = [];
            }
            }
            /*var popup = angular.element('#substituteModal');
            popup.modal('hide');*/
        }
        
        var validateSubstituteIssueQty= function(subItem){
       	 var isValid = true; 
       	 var tempIssueQty=0;
       	subItem.QuantityInserted=true;
       	angular.forEach(self.model.materialIssuePendingDetail.items, function(val, key){
       	   if(subItem.storeIndentDetailId == val.storeIndentDetailId){
       	       if(val.selectedItems != undefined && val.selectedItems.length > 0){
       			angular.forEach(val.selectedItems, function(valSub, keySub){
                    if(valSub.selectedBatch.selectedBatch.availableStockQuantity >= valSub.selectedBatch.issueQuantity){
                        tempIssueQty += valSub.issueQuantity;
                    }else{
                     growl.error("Issue Quantity Should be less than Available Stock Quantity");
                     valSub.selectedBatch.issueQuantity = 0; 
                     isValid = false;
                 
                    }

               	});
       		}
       	   }     
       	});
       	if(tempIssueQty > subItem.indentQuantity){
   		        growl.error("Issue Quantity Should be less than Pending Indent Quantity");
                   subItem.issueQuantity = 0; 
                   $scope.saveDisabled = true;
                   isValid = false; 
				} 
       	return isValid;
       }

        var validateSubSelectedBatch=function(listSub){
      	  var isValid=true;
      	  var count=0;
      	  angular.forEach(listSub, function(val , key){
      		  if(val.selectedBatch==undefined){
                    growl.error("Please select batch.");
                    var popup = angular.element('#substituteModal');
                    popup.modal('show');
//                    $("#substituteModal").show();
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
        
        var AddSubstituteItem = function (index, bill) {
            bill.splice(index + 1, 0, CopySubstituteItem(index, bill));
        }

        var CopySubstituteItem = function (index, bill) {
            var item = bill[index];
            return {
            	addedItem: item.addedItem,
            	batch: item.batch,
            	genericId: item.genericId,
            	genericName: item.genericName,
            	interventionFlag: item.interventionFlag,
            	isOtc: item.isOtc,
            	isSelected: item.isSelected,
            	itemCode: item.itemCode,
            	itemId: item.itemId,
            	itemName: item.itemName,
            	manufacturerName: item.manufacturerName,
            	markupStatus: item.markupStatus,
            	narcoticFlag: item.narcoticFlag,
            	productCategoryId: item.productCategoryId,
            	rackNo:  item.rackNo,
            	selectedBatch: null,
            	stockAvailable: item.stockAvailable,
            	taxId: item.taxId,
            	selectedUom: item.uoms[0],
            	taxPercentage: item.taxPercentage,
            	uoms: item.uoms,
            	
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
        var SelectSubstituteItem = function (item) {
            var itemIds = self.model.SelectedItems;

            if (item.isSelected) {
                itemIds.push(item);
            } else {
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
        $scope.removeSubstituteItem1=function(ind,parentItemId){
        	var selectedIndex = ind;
        	angular.forEach(self.model.materialIssuePendingDetail.items, function(value , index){
        		if(parentItemId==value.itemId)
        			{
        			if(value.batchListData2!=undefined && value.batchListData2!=null)
        				{
        			value.batchListData2.forEach(function (batch) {
                	  batch.disableItem=false;
            		});
        				}
        			if(value.batchListData1!=undefined && value.batchListData1!=null)
    				{
    			value.batchListData1.forEach(function (batch) {
            	  batch.disableItem=false;
        		});
    				}
    			if((value.selectedItems!=undefined && value.selectedItems!= null)){
    				angular.forEach(value.selectedItems, function(val1, ind1){
    					if(val1.savedSubItem!=undefined){
    						if(val1.savedSubItem==false  && selectedIndex == ind1){
    						value.selectedItems.splice(ind1, 1);
    						value.disabled = false;
    						value.substititeCheckFlag=false;
    					}
    					}
    				});
    			}
        	}
    		});
        }

        $scope.subFlagChange = function()
        {
            self.model.materialIssuePendingDetail.items.forEach(function (item) {
                item.substititeFlag=false;
            });
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsMaterialPending", {
            url: '/detailsMaterialPending',
            templateUrl: 'views/PharmacyInventory/InPatient/details-material-pending.html',
            controller: "MaterialPendingListDetails.Controller",
            controllerAs: 'vm',
            title: 'Material Issue'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("MaterialPendingListDetails.Controller", materialPendingListDetailsController)
})();  