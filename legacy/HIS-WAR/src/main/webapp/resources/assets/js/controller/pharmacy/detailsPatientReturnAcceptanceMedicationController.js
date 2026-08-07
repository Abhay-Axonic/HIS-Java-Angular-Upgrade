(function () {
    "use strict";

    function detailsPatientReturnAcceptanceMedicationController($scope, $rootScope, $http, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, $cookies, CONSTANTS, rejectReasonService, StatusService,pharamcyContractService,reportService) {
        var self = this;
        var selectedItemId;
       

        // Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId; 
        var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
        $scope.storeName = cookieObjectStore != null ? cookieObjectStore.storeName : '';
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                isRejectQuantityValid: [],
                SelectedRequestItems: [],
                isSelectAll: false
            };

            // Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });
            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })
            self.model.GetDetailsPatientReturnAcceptanceMedication = GetDetailsPatientReturnAcceptanceMedication;
            self.model.SavePopup = SavePopup;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.RejectQty = RejectQty;
            self.model.SaveRejectPopup = SaveRejectPopup;
            self.model.ValidateBill = ValidateBill;
            self.model.SavePatientReturnAcceptance = SavePatientReturnAcceptance;
            self.model.NevigateToPatientReturnAcceptance = NevigateToPatientReturnAcceptance;
            self.model.CancelRejection = CancelRejection;
            self.model.ChangeItemStatus = ChangeItemStatus;
            self.model.calculateAtcop = calculateAtcop;
            self.model.GetGenericMaster = GetGenericMaster;
            self.model.GetStrengthUnitList = GetStrengthUnitList;
            self.model.ViewCombinationDetails = ViewCombinationDetails;
            self.model.valueOnChange = valueOnChange;
            self.model.ApprovedQuantity =ApprovedQuantity;
            self.model.ModalDismissRejectionPopup = ModalDismissRejectionPopup;
            $scope.patientId = pharamcyContractService.billObj.patientId;
            self.model.validate = validate;
            GetDetailsPatientReturnAcceptanceMedication(commonDetailService.getDataId());
        }
        $scope.saveBtnFlag=true;

        var validate = function(){
            var isValid = true;
            if(self.model.qty > self.model.selectedItem.acceptQuantity){
                growl.error("Reject quantity is greater than accept quantity");
                self.model.qty = 0;
                isValid = false;
            }
            return isValid;
        }

        var GetDetailsPatientReturnAcceptanceMedication = function (itemId) {

            var URI =  CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.MEDICATION_DETAIL_API + $scope.unitId + '/' + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    $scope.isDisabledFlag=true;
                  // $scope.saveBtnFlag=false;
                    self.model.detailsPatientReturnAcceptanceMedicationList = response.data;
                    if(response.data.statusId == 8 || response.data.status == 'Completed') {
                        self.model.isSelectAll = true;
                    }
                    self.model.detailsPatientReturnAcceptanceMedicationList.items.forEach(function(item){
                    	var uom=[];
                    	item.batch.forEach(function(batch){
                    		if(batch.uom!=undefined && batch.uom!=null)
                    			{
                    			uom=batch.uom;
                    			}
                    	})
                    	item.batch.forEach(function(batch){
                    		if(batch.uom==undefined || batch.uom==null || batch.uom.length==0)
                    			{
                    			batch.uom=uom;
                    			}
                    		if(item.batchId==batch.batchId && item.uom=="Dispense")
                            {
                    			item.mrp=((item.rate).toFixed(2)*(batch.uom[2].conversion)).toFixed(2);
                    			item.actualLendingPrice=((item.actualLendingPrice).toFixed(2)*(batch.uom[2].conversion)).toFixed(2);
                            }
                    		else if(item.batchId==batch.batchId && item.uom=="Stock")
                            {
                			    item.mrp=((item.rate).toFixed(2)*(batch.uom[1].conversion*batch.uom[2].conversion)).toFixed(2);
                			    item.actualLendingPrice=((item.actualLendingPrice).toFixed(2)*(batch.uom[1].conversion*batch.uom[2].conversion)).toFixed(2);
                			}
                    		else if(item.batchId==batch.batchId && item.uom=="Purchase")
                            {
                    			item.mrp=((item.rate).toFixed(2)*(batch.uom[0].conversion*batch.uom[1].conversion*batch.uom[2].conversion)).toFixed(2);
                    			item.actualLendingPrice=((item.actualLendingPrice).toFixed(2)*(batch.uom[0].conversion*batch.uom[1].conversion*batch.uom[2].conversion)).toFixed(2);
                            }
                    	});
                        item.isDisabled = true;
                        if(item.statusId == 4){
                            $scope.count = $scope.count + 1;
                        }
                        if(response.data.status == 'Pending'){
                            item.acceptQty = item.acceptReturnQuantity;
                            item.rejectedQuantity = 0;
                        }
                        else{
                            item.acceptQuantity = item.acceptedQuantity;
                            item.acceptQty = item.acceptedQuantity;
                            if(item.acceptedQuantity == null){
                                item.acceptQty = item.acceptReturnQuantity;
                            }
                            if(item.rejectedQuantity){
                            	item.isRejected = true;
                            }
                        }
                        if(item.isReturnAcceptance == true)
                        {
                        	item.isDisabled = true;
                        	// $scope.saveBtnFlag=true;
                        }
                        else
                        {
                        	item.isDisabled = false;
                        	// $scope.saveBtnFlag=false;
                        }
                        if(!!item.acceptQuantity && !!item.returnQuantity){
                            ChangeItemStatus(item);
                        } 
                        if(!!item.rejectQuantity){
                            item.isRejected = true;
                            item.rejectReasonId = 1;
                        }
                        angular.forEach(item.batch,function(d){
                            if(item.batchId == d.batchId){
                                d.uom.find(function(uoml){
                                    if(uoml.uomType == item.uom){
                                       item['uomDetails'] = uoml.uomDetails;
                                       item['uomTypeId'] = uoml.uomTypeId;
                                    }
                                });
                                if(item.isThreeUOMApplicable == false){
                                    if(item.acceptedQuantity == undefined || item.acceptedQuantity == null){
                                        if(item.uomTypeId ==1){
                                            item.receivedQtyCon = (item.returnQuantity +"*"+ d.uom[0].conversion);
                                            item.receivedQtyCalculation = (item.returnQuantity * d.uom[0].conversion);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                        else if(item.uomTypeId == 2){
                                             
                                        }
                                        else{
                                            item.receivedQtyCon = (item.returnQuantity);
                                            item.receivedQtyCalculation = (item.returnQuantity);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                    }
                                    else{
                                        if(item.uomTypeId ==1){
                                            item.receivedQtyCon = (item.acceptedQuantity +"*"+ d.uom[0].conversion);
                                            item.receivedQtyCalculation = (item.acceptedQuantity * d.uom[0].conversion);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                        else if(item.uomTypeId == 2){
                                             
                                        }
                                        else{
                                            item.receivedQtyCon = (item.acceptedQuantity);
                                            item.receivedQtyCalculation = (item.acceptedQuantity);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                    }
                                }
                                else{
                                    if(item.acceptedQuantity == undefined || item.acceptedQuantity == null){
                                        if(item.uomTypeId ==1){
                                            item.receivedQtyCon = (item.returnQuantity +"*"+ d.uom[0].conversion +"*"+ d.uom[1].conversion);
                                            item.receivedQtyCalculation = (item.returnQuantity * d.uom[0].conversion * d.uom[1].conversion);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                        else if(item.uomTypeId == 2){
                                            item.receivedQtyCon = (item.returnQuantity +"*"+ d.uom[1].conversion);
                                            item.receivedQtyCalculation = (item.returnQuantity * d.uom[1].conversion);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                        else{
                                            item.receivedQtyCon = (item.returnQuantity);
                                            item.receivedQtyCalculation = (item.returnQuantity);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                    }
                                    else{
                                        if(item.uomTypeId ==1){
                                            item.receivedQtyCon = (item.acceptedQuantity +"*"+ d.uom[0].conversion +"*"+ d.uom[1].conversion);
                                            item.receivedQtyCalculation = (item.acceptedQuantity * d.uom[0].conversion * d.uom[1].conversion);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                        else if(item.uomTypeId == 2){
                                            item.receivedQtyCon = (item.acceptedQuantity +"*"+ d.uom[1].conversion);
                                            item.receivedQtyCalculation = (item.acceptedQuantity * d.uom[1].conversion);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                        else{
                                            item.receivedQtyCon = (item.returnQuantity);
                                            item.receivedQtyCalculation = (item.returnQuantity);
                                            item.uomUnitData = d.uom[2].uomUnit;
                                        }
                                    }
                                }
                            }
                        });
                    });
                    $rootScope.stopLoader();
                });
            }

        $scope.calculateReceivedQty = function (item) {
            if(item.acceptQty == undefined || item.acceptQty == null || item.acceptQty == ""){
                item.acceptQty = 0;
            }
            angular.forEach(item.batch,function(d){
                if(item.batchId == d.batchId){
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.acceptQty +"*"+ d.uom[0].conversion);
                            item.receivedQtyCalculation = (item.acceptQty * d.uom[0].conversion);
                            item.uomUnitData = d.uom[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                        
                        }
                        else{
                            item.receivedQtyCon = (item.acceptQty);
                            item.receivedQtyCalculation = (item.acceptQty);
                            item.uomUnitData = d.uom[2].uomUnit;
                        }
                    }
                    else{
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.acceptQty +"*"+ d.uom[0].conversion+"*"+ d.uom[1].conversion);
                            item.receivedQtyCalculation = (item.acceptQty * d.uom[0].conversion * d.uom[1].conversion);
                            item.uomUnitData = d.uom[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.acceptQty +"*"+ d.uom[1].conversion);
                            item.receivedQtyCalculation = (item.acceptQty * d.uom[1].conversion);
                            item.uomUnitData = d.uom[2].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.acceptQty);
                            item.receivedQtyCalculation = (item.acceptQty);
                            item.uomUnitData = d.uom[2].uomUnit;
                        }
                    }
                }
            });
        }

        var SelectAllRequests = function () {

            if (self.model.selectAllRows) {
            	$scope.saveBtnFlag=false;
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientReturnAcceptanceMedicationList.items.forEach(function (item) {
                    if(item.status != 'Completed'){    
                        item.isSelected = true;
                        item.isDisabled = false;
                        self.model.SelectedRequestItems.push(item);
                    }
                });
            } else {
            	$scope.saveBtnFlag=true;
                self.model.SelectedRequestItems = [];
                self.model.detailsPatientReturnAcceptanceMedicationList.items.forEach(function (item) {
                    item.isSelected = false;
                    item.isDisabled = true;
                });
            }
            // console.log(self.model.SelectedRequestItems);
        }

        var calculateAtcop = function () {
            angular.forEach(self.model.detailsPatientReturnAcceptanceMedicationList.items, function (d){
                if($scope.atCop == true){
                    d.rate = d.actualLendingPrice;
                    Calculate(d, self.model.detailsPatientReturnAcceptanceMedicationList);
                }
                else{
                    d.rate = d.rate;
                    Calculate(d, self.model.detailsPatientReturnAcceptanceMedicationList);
                }
            });
        }
        
        var Calculate = function(item, bill){
        	// if(item.discountPerItem>100)
        	// 	{
        	// 	 growl.error("Discount should be less than or equal to 100%");
        	// 	 item.discountPerItem=0;
        	// 	 return false;
        	// 	}
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
                "discount": item.discountPerItem ? item.discountPerItem : 0,
                "isDiscPercent": !!bill.discountType && bill.discountType == 1 ? true : false,
                "tax": item.taxPercentage,
                "qty": item.quantityIssued,
                // "qty": item.quantity ? item.quantity : item.qtyPrescribed,
                "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId,
                "patientClassPercentage": item.patientClassPercentage,
            }
  
            var response = commonDetailService.calculateAmount(data);
  
            // item.rate = response.rate;
            // item.taxAmount = response.taxAmount;
            // self.model.responseTaxAmount =  item.taxAmount;
            // item.unitDiscountAmount = response.discountAmount;
            // item.itemtotalAmount = response.totalAmount;
            // item.netAmount = response.netAmount;
            // item.beforeCalculationRate = response.rate;
            // self.model.itemsAfterCalculation = item;
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
        var CalculateAmount = function (item) {
            if ($scope.taxInclusive == true) {
                item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : 0));
                item.netAmount = (((item.itemtotalAmount) - (item.itemDiscount)));
                item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
            }
            else {
                item.itemtotalAmount = ((item.rate ? item.rate : 0) * (item.quantityIssued ? item.quantityIssued : 0));
                item.taxAmount = (((item.itemtotalAmount) - (item.itemDiscount)) * (item.taxPercentage / 100));
                item.netAmount = (((item.itemtotalAmount) - (item.itemDiscount) + item.taxAmount));
                item.selfPayable = (isNaN(item.selfPayable)?0:item.selfPayable);
                item.creditPayable = (isNaN(item.creditPayable)?0:item.creditPayable);
            }
            if(!item.isConsumed)
       	   {
       	     item.selfPayable = item.netAmount;
       	   }
        }
        
        function valueOnChange(item, isSelectAll) {
            if (isSelectAll)
                self.model.detailsPatientReturnAcceptanceMedicationList.items.forEach(function (item) {
                    if (self.model.selectAllRows) {
                        item.acceptReturnQuantity = item.returnQuantity;
                        item.rejectQuantity = 0;
                        item.isReject = false;
                    } else
                        item.acceptReturnQuantity = 0;
                });
            else
                if (item.isSelected) {
                    item.acceptQuantity = item.quantityIssued;
                    item.rejectQuantity = 0;
                    item.isReject = false;
                } else
                    item.acceptQuantity = 0;
        }
        
        
        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectRequestItem = function (item) {

        	
        	var itemIds = self.model.SelectedRequestItems;

            if (item.isSelected) {
            	$scope.saveBtnFlag=false;
                item.acceptQty = item.acceptReturnQuantity;
                itemIds.push(item);
                item.isDisabled = false;
            } else {
                var index = itemIds.indexOf(item);
                item.isDisabled = true;
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
                
                if(itemIds.length == 0)
                {
                   $scope.saveBtnFlag=true;       
                }
                itemIds.forEach(function(item){
                    if(item.isSelected == true)
                    {
                        $scope.saveBtnFlag=false;      
                    }
                    else
                    {
                        $scope.saveBtnFlag=true;
                    } 
                })
//                item.acceptQty = 0;
//                item.rejectedQuantity = 0;
//                item.isRejected = false;
            }

            if (self.model.detailsPatientReturnAcceptanceMedicationList.items.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            // console.log(itemIds);
        }

        var ValidateBill = function (itemList) {
            var isValid = true;
            var isValidAllItems=true;
            if (!self.model.SelectedRequestItems || self.model.SelectedRequestItems.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                self.model.SelectedRequestItems.forEach(function (item) {
                    item['isRejectQuantityValid'] = false;

                    if ((item.acceptReturnQuantity > (item.returnQuantity)) || !item.returnQuantity) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter valid quantity");
                    }

                    if (item.acceptReturnQuantity == 0) {
                        item['isRejectQuantityValid'] = true;
                        isValid = false;
                        growl.error("Please enter quantity greater than zero");
                    }

                    if(item.acceptQty > item.returnQuantity){
                        growl.error('Accepted quantity is greater than return quantity');
                        isValid = false;
                        item.acceptQty = 0;
                    }
                    if(item.acceptQty==0 && item.rejectedQuantity==0)
                    	{
                    	growl.error('Accepted & Reject quantity both should not be zero');
                        isValid = false;
                        isValidAllItems=false;
                        item.acceptQty = 0;
                    	}
                });
            }
            if(isValidAllItems==false)
            	{
            	isValid=false;
            	}
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }

        var ClearFields = function (itemList) {

            itemList.Id = "1" + (new Date()).getTime();
            itemList.rejectedNo = null;
            itemList.rejectedDate = null;
            itemList.wardName = null;
            itemList.uhid = null;
            itemList.patientDetail = null;
            itemList.doctorDetail = null;
            itemList.store = null;
            itemList.items = [];
        }

        var RejectQty = function (item) {
            self.model.selectedItem = item;

            if (item.isRejected) {
                selectedItemId = item.id;
                self.model.qty = item.rejectedQuantity;
                self.model.rejectReason = self.model.rejectReasons[0];
                self.model. rejectionFlag = false;

                self.model.action = 'Open';
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
              } 
              else {
                item.isRejected = true;
                item.rejectReasonId = 1;
                var popup = angular.element('#cancelStatus');
                popup.modal('show');
              } 
        }

        var SaveRejectPopup = function () {

            if(self.model.qty == undefined || self.model.qty == null || self.model.qty == 0){
                growl.error('Please Enter Rejected Quantity');
                return false;
            }
            self.model.SelectedRequestItems.forEach(function (item) {
                if(item.isRejected){
                    if (item.id == selectedItemId) {
                        if(parseInt(self.model.qty) > item.returnQuantity) {
                            growl.error("Please enter quantity less than or equal to Return Quantity");
                            item.isRejected = false;
                            return false;
                        }  
                        // item.rejectQuantity = self.model.qty;
                        item.rejectedQuantity = self.model.qty;
                        // item.acceptReturnQuantity = item.returnQuantity - self.model.qty;
                        item.acceptQty = item.returnQuantity - self.model.qty;
                        item['rejectReasonId'] = self.model.rejectReason.id;
                        item['note'] = self.model.rejectNote!=undefined ? self.model.rejectNote : "";
                        $scope.calculateReceivedQty(item);
                    }
                }
            });
            
            self.model.rejectReason = undefined;
            self.model.rejectNote = undefined;
            var popup = angular.element('#qtyaccepatReject');
            popup.modal('hide');
        }

        var ModalDismissRejectionPopup = function (item){
            item.items.forEach(function (data){
                var prevRequest = self.model.SelectedRequestItems.find(function(prevItem){
                    return prevItem.id == data.id && selectedItemId == prevItem.id;
                });
        
                if (prevRequest) {
                    data.statusId = prevRequest.statusId;
                    var acceptReturnQuantity = parseInt(prevRequest.acceptReturnQuantity);
                    if(data.rejectQuantity == undefined){
                        data.rejectQuantity = 0;
                    }
                    data.acceptReturnQuantity =  acceptReturnQuantity +  data.rejectQuantity;
                    data.acceptQty = parseInt(data.acceptQty) + data.rejectedQuantity;
                    data.rejectedQuantity = 0;
                    data.rejectQuantity = 0;
                    data.isRejected = false;
                    $scope.calculateReceivedQty(data);
                }
            });
            $('#qtyaccepatReject').modal('hide');
        } 

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var CancelRejection = function (item) {
            self.model.selectedItem.isRejected = false;
            self.model.qty = 0;
            self.model.selectedItem.rejectedQuantity = 0;
            self.model.selectedItem.acceptQty = self.model.selectedItem.returnQuantity;
            $scope.calculateReceivedQty(self.model.selectedItem);
        }

        $scope.validate = function(item){
            var isValid = true;
            if(item.acceptQty > item.returnQuantity){
                growl.error('Accepted quantity is greater than return quantity');
                isValid = false;
                item.acceptQty = 0;
            }
            return isValid;
        }

         var ApprovedQuantity = function (item) {
            if($scope.validate(item)){
                if (item.acceptQty > item.returnQuantity) {
                    item.rejectedQuantity = 0;
                    self.model.qty = undefined;
                    item.isRejected = false;
                    } else if (item.acceptQty == 0 || !item.acceptQty) {
                    item.rejectedQuantity = item.returnQuantity;
                    self.model.qty = item.rejectedQuantity;
                    item.isRejected = true;
                    RejectQty(item)
                    } else if (item.acceptQty < item.returnQuantity) {
                    item.rejectedQuantity = item.returnQuantity - item.acceptQty;
                    self.model.qty = item.rejectedQuantity;
                    item.isRejected = true;
                    RejectQty(item)
                    } else if (item.acceptQty == item.returnQuantity) {
                    item.rejectedQuantity = item.returnQuantity - item.acceptQty;
                    self.model.qty = item.rejectedQuantity;
                    item.isRejected = false;
                }
            }
        }

       var ChangeItemStatus = function(item){
           if(!!item.rejectQuantity){
                item.isRejected = true;
                item.rejectReasonId = 1;
                self.model.action = 'Open';
                self.model.qty = Number(item.returnQuantity) - Number(item.acceptQty);
                var popup = angular.element('#qtyaccepatReject');
                popup.modal('show');
            }
            // as discussed only completed status is sufficient
            if(item.returnQuantity > parseInt(item.acceptQty)){
            	
               item.statusId = SetStatus('Partially Completed');
            	//   item.statusId =  SetStatus('Completed');
            } else if(item.returnQuantity == parseInt(item.acceptQty)) {
               // item.statusId = SetStatus('Closed');
            	  item.statusId =  SetStatus('Completed');
            }
        }
        
        
       var ClosePopup = function (item) {
           item.isRejected = false;
           item.rejectReasonId = null;
           self.model.selectedItem.isRejected = false;
           self.model.qty = 0;
           self.model.selectedItem.rejectQuantity = 0;
           self.model.selectedItem.acceptQuantity = self.model.selectedItem.quantityIssued;
           var popup = angular.element('#qtyaccepatReject');
           popup.modal('hide');
       }

       $scope.saveBtnFlag1=false;
       $scope.saveClick=0;

        $scope.count = 0;
        $scope.saveBtnDisabled=false;
        var SavePatientReturnAcceptance = function (itemList) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        	$scope.saveBtnFlag1=true;
            $scope.saveBtnDisabled=true;
        	$scope.saveClick=$scope.saveClick+1;
        	if($scope.saveClick>1)
        		{
        		return false;
        		}
        	else {
            if (ValidateBill(itemList)) {
            	
                var URI =  CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.MEDICATION_SAVE_API;

                var data = {
                    "id": itemList.id,
                    "statusId": SetStatus('Completed'),
                    "items": [],
                	"patientId": $scope.patientId,
                	"storeId":itemList.storeId,
                	"unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":itemList.storeId,
                    "wardId":itemList.wardId!=undefined?itemList.wardId:null,
                    "issueId":itemList.issueId
                }
                angular.forEach(itemList.items, function (item, key) {
                    if (item.isSelected) {
                        if(!item.rejectQuantity){
                            item.rejectQuantity = item.rejectedQuantity;
                        }
                        var PatientData = {
                            "id": item.id,
                            "statusId": SetStatus('Completed'),
                            "acceptQuantity": Number(item.acceptQty),
                            "rejectedQuantity": item.rejectQuantity,
                            "rejectReason": item.rejectReasonId,
                            "rejectedNote": item.note,
                            "batchId":item.batchId,
                            "returnQuantity" : item.returnQuantity,
                            "issueDetailBatchMapper":item.issueDetailBatchMapper,
                        }
                        // if(item.returnQuantity > parseInt(item.acceptQty)){
                        //     PatientData.statusId = SetStatus('Partially Completed');
                        // } else if(item.returnQuantity == parseInt(item.acceptQty)) {
                        //     PatientData.statusId =  SetStatus('Completed');
                        // }
                        data.items.push(PatientData);
                    }
                });
                // if(itemList.items.length > 1){
                    angular.forEach(data.items, function (value, key) {
                        if(value.statusId == 4){
                            $scope.count = $scope.count + 1;
                        }
                    });
                // }
                if($scope.count == itemList.items.length){
                    data.statusId =  SetStatus('Completed');
                }
                else{
                    data.statusId =  SetStatus('Partially Completed');
                }
                /*
				 * data.items.forEach(function(key){ var itemStatusList = [];
				 * itemStatusList.push(key.statusId);
				 * 
				 * if(itemStatusList.indexOf(8) !== -1){ // Closed status id
				 * data.statusId = 8; // Closed stutus } else { data.statusId =
				 * undefined; // no stutus } });
				 */
                // console.log(data);
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(

                    function (response) {
                    	
                    	if(response.data.id != null){
                    		var data = {
                					"appName" : "HIS",
                					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
                					"reportDesignFilePath" : "Patient_Return_Acceptance.rpt",
                					"reportFormat" : "PDF",
                					"exportFileName" : "PatientReturnAcceptance",
                					"reportParams" : [ {
                						"paramName" : "id",
                						"paramType" : "Integer",
                						"paramValue" : response.data.id
                					} ]
                				};
                				reportService.generate(REPORT_API_URI1, data);
                    	}
                        $rootScope.stopLoader();
                        ClearFields(itemList);
                        NevigateToPatientReturnAcceptance();
                        growl.success('Patient return acceptance successfully saved');
                        $scope.saveBtnFlag1=false;
                        $scope.saveClick=0;
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                        $scope.saveBtnFlag1=false;
                        $scope.saveClick=0;
                    });
            }
        }
        }

        var SavePopup = function () {
        	if(	$scope.saveBtnFlag==true)
        		{
        		
        		}
        	else
        		{
        		 var popup = angular.element('#confirmSave');
                 popup.modal('show');
        		}
           
        }

        var NevigateToPatientReturnAcceptance = function () {
            document.getElementById('popupClose').click();
            setTimeout(function () {
                $state.go('patientReturnAcceptance');
            }, 1000);
        }

          // Method to get the list of Generic
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

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("detailsPatientReturnAcceptanceMedication", {
            url: "/detailsPatientReturnAcceptanceMedication",
            templateUrl: 'views/PharmacyInventory/InPatient/details-medication-patient-return-acceptance.html',
            controller: "DetailsPatientReturnAcceptanceMedication.Controller",
            controllerAs: "vm",
            title: 'Patient Return Acceptance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("DetailsPatientReturnAcceptanceMedication.Controller", detailsPatientReturnAcceptanceMedicationController)
})();

