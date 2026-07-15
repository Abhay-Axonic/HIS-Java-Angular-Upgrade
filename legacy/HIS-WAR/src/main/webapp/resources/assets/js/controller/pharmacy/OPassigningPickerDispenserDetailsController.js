(function () {
    "use strict";

    function OPassigningPickerDispenserDetailsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $window, $filter, $state, growl, CurrencyService, StatusService, assigningPickerDispenserService) {
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

            self.model.ClearBill = ClearBill;

            AssigningPickerDispenserDetails(assigningPickerDispenserService.getAssigningPickerDispenserId());
            SetFocus();
        }

        var AssigningPickerDispenserDetails = function (itemId) {
            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.DETAIL_API  + $scope.unitId + "/"+$scope.organizationId + "/"+$scope.storeId+ "/"+$scope.userId+"/"  + itemId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.assigningPickerDispenserDetails = response.data;

                    if (self.model.assigningPickerDispenserDetails.items) {
                        self.model.assigningPickerDispenserDetails.items.forEach(function (item) {
                            item.issuedQty = item.issuedQuantity;
                            item.pendingIssueQty = item.pendingIssueQuantity;
                            if(item.quantityOrdered == (item.pendingIssueQuantity + item.issuedQuantity)){
                                item.status = 'Completed';
                                item.statusId = 4;
                            }
                            if (item.status == 'Completed') {
                                item.itemDisabled = true;
                            }
                            if(item.batch) {
                                if(item.batch.length > 0) {
                                    self.model.IsEnbaledSave = true;
                                }
                            }
                            item.selectedBatch = item.batch.find(function (x) {
                                return !!x.isSelected;
                            });
                            if (item.selectedBatch) {
                                item.selectedUom = item.selectedBatch.uom.find(function (obj) {
                                    return obj.uomTypeId == item.selectedBatch.uomTypeId;
                                });
                                item.uom = item.selectedBatch.uom;
                                item.stockAvailable = item.selectedBatch.availableStockQuantity;
                            } else {
                                item.stockAvailable = 0;
                            }
                            item.quantity = 0;
                            ConvertQtyOrdered(item, item.uom, item.selectedUom);
                        });
                    }
                    $rootScope.stopLoader();

                });
        }

        var BatchChange = function (index, bill, batch, parentIndex) {
            var item = null;
            if (parentIndex == null) {
                bill.items[index].uom = batch.uom;
                bill.items[index].stockAvailable = batch.availableStockQuantity;
                item = bill.items[index];
            } else {
                bill.items[parentIndex].selectedItems[index].uom = batch.uom;
                bill.items[parentIndex].selectedItems[index].stockAvailable = batch.availableStockQuantity;
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
            ConvertQtyOrdered(item, item.uom, item.selectedUom);
        }

        var ConvertQtyOrdered = function (item, uom, uomList) {
            var newUOM = [];
            if (uom) {
                uom.find(function (value) {
                    if (!!value.uomTypeId) {
                        newUOM.push(value);
                    }
                });
            }
            var quantityOrdered = item.quantityOrdered;
            item.quantity = 0;
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

                var items = parentItem[0].selectedItems.filter(function (detail) {
                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
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


                var items = self.model.assigningPickerDispenserDetails.items.filter(function (detail) {
                    return detail.itemId == item.itemId && item.selectedBatch && item.selectedUom && item.selectedUom.uomTypeId > 0 && item.selectedBatch.batchId > 0 && detail.selectedBatch.batchId == item.selectedBatch.batchId && detail.selectedUom.uomTypeId == item.selectedUom.uomTypeId;
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
            if (parentIndex == null) {
                bill.items.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
            } else {
                bill.items[parentIndex].selectedItems.splice(index + 1, 0, CopyItem(index, bill, parentIndex));
                //item = bill.items[parentIndex].selectedItems[index];
            }
        }

        var CopyItem = function (index, bill, parentIndex) {
            // var item = bill.items[index];
            var item = null;
            if (parentIndex == null) {
                item = bill.items[index];
            } else {
                item = bill.items[parentIndex].selectedItems[index];
            }

            return {
                id: item.id,
                itemCode: item.itemCode,
                itemName: item.itemName,
                itemId: item.itemId,
                genericName: item.genericName,
                quantityOrdered: item.quantityOrdered,
                // newQty: item.newQty,
                batch: item.batch,
                selectedBatch: null,
                uom: item.uom,
                selectedUom: null,
                stockAvailable: item.stockAvailable,
                rackNo: item.rackNo,
                picker: item.picker,
                substituteBtn: (parentIndex == null ? false : true),
                isShow: true,
                frequency: item.frequency,
                dose: item.dose,
                startDate: item.startDate,
                stopDate: item.stopDate,
                quantity: 0,
                prescriptionDetailId: item.prescriptionDetailId,
                issuedQuantity: item.issuedQuantity,
                pendingIssueQuantity: item.pendingIssueQuantity,
                issuedQty: item.issuedQuantity,
                pendingIssueQty: item.pendingIssueQuantity

            }
        }

        var RemoveItem = function (index, bill, parentIndex) {
            // var item = bill.items[index];
            var item = null;
            if (parentIndex == null) {
                item = bill.items[index];
            } else {
                item = bill.items[parentIndex].selectedItems[index];
            }

            //bill.items.splice(index, 1);
            if (parentIndex == null) {
                bill.items.splice(index, 1);
            } else {
                bill.items[parentIndex].selectedItems.splice(index, 1);
                if(bill.items[parentIndex].selectedItems.length === 0){
                    bill.items[parentIndex]['disabled'] = false;
                    bill.items[parentIndex]['itemDisabled'] = false;
                }
            }
        }

        var SubstituteItems = function (item) {
            self.model.selectedItemId = item.genericId;
            self.model.itemName = item.itemName;
            self.model.genericName = item.genericName;
            self.model.dose = item.dose;
            self.model.selectedQuantity = item.quantityOrdered;
            self.model.substituteNewQty = item.newQty;
            self.model.issuedQuantity = item.issuedQuantity;
            self.model.pendingIssueQuantity = item.pendingIssueQuantity;
            self.model.prescriptionDetailId = item.id;
            self.model.frequency = item.frequency;
            self.model.dose = item.dose;
            self.model.startDate = item.startDate;
            self.model.stopDate = item.stopDate;
            self.model.substituteDetails = [];
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;

            var URI = CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SUBSTITUTE_API + $scope.unitId + '/' + item.genericId + '/' + item.itemId+ '/' + item.isNarcotic;
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
                            });
                            item.uom = [{
                                "uomDetails": "Select UOM"
                            }],

                                item.selectedBatch = item.batch[0];
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
                    // selectedItem['stopDate'] = self.model.stopDate;
                    // selectedItem['startDate'] = self.model.startDate;


                    // selectedItem.selectedUom = [{
                    //     uomDetails: "Select UOM"
                    // }];
                    // selectedItem.selectedUom = selectedItem.selectedUom[0];
                });

                self.model.assigningPickerDispenserDetails.items.forEach(function (list) {
                    self.model.SelectedItems.forEach(function (item) {

                        item.uom = [{
                            uomDetails: "Select UOM",
                        }];
                        if (!!item.selectedBatch.batchId) {
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

                    if (self.model.selectedItemId == list.genericId) {
                        list['disabled'] = true;
                        if (!list['selectedItems'])
                            list['selectedItems'] = self.model.SelectedItems;
                        else {
                            list['selectedItems'] = list['selectedItems'].concat(self.model.SelectedItems);
                        }
                        // list.selectedUom = list.selectedUom[0];
                        list.quantity = null;
                    }
                });

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
                itemCode: item.itemCode,
                itemName: item.itemName,
                genericName: item.genericName,
                // manufacturerName: item.manufacturerName,
                batch: item.batch,
                selectedBatch: item.batch[0],
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
            bill.splice(index, 1);
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
                                    "prescriptionDetailId": item.prescriptionDetailId,
                                    "batchId": item.selectedBatch.batchId,
                                    "uomTypeId": item.selectedUom.uomTypeId,
                                    "uomUnitId": item.selectedUom.uomUnitId,
                                    "quanityIssued": Number(item.quantity)                            
                                }
                                data.items.push(billItem);
                                if (item.statusId == SetStatus('Partially Completed')) {
                                    data.statusId = SetStatus('Partially Completed');
                                }
                            }

                            if (item.selectedItems && !item.itemDisabled) {
                                angular.forEach(item.selectedItems, function (item, key) {
                                    var billSubItem = {
                                        "statusId": SetStatus('Partially Completed'),
                                        "id": item.id,
                                        "prescriptionDetailId": item.prescriptionDetailId,
                                        "batchId": item.selectedBatch.batchId,
                                        "uomTypeId": item.selectedUom.uomTypeId,
                                        "uomUnitId": item.selectedUom.uomUnitId,
                                        "quanityIssued": Number(item.quantity)
                                    }                           

                                    data.items.push(billSubItem);
                                });
                            }
                        }
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

        var ValidateQuantity = function (parentItem, item) {
            var isValid = true; var totalQuantitys = 0
            if (!!parentItem) {
                var list = self.model.assigningPickerDispenserDetails.items.filter(x=> x.prescriptionDetailId == parentItem.prescriptionDetailId)
                if(list.length > 0){
                    for(let lt of list){
                        if(lt.quantity) {
                            totalQuantitys = totalQuantitys + Number(lt.quantity)
                        }
                    }
                    totalQuantitys = totalQuantitys + parentItem.issuedQuantity + parentItem.pendingIssueQuantity
                    if(Number(totalQuantitys) > parentItem.newQty){
                        parentItem.isParentQuantityValid = true;
                        growl.error("Enter quantity is more than order quantity");
                        isValid = false;
                    } else {
                        parentItem.isParentQuantityValid = false;
                    }
                }
                if (!parentItem.quantity) {
                    parentItem.isParentQuantityValid = true;
                    growl.error("Please enter quantity");
                    isValid = false;
                }
                else if (parseInt(parentItem.quantity) > parentItem.newQty) {
                    parentItem.isParentQuantityValid = true;
                    growl.error("Please enter quantity less than or equal to ordered quantity");
                    isValid = false;
                }
                else if (parseInt(parentItem.quantity) > parentItem.stockAvailable) {
                    parentItem.isParentQuantityValid = true;
                    growl.error("Entered Quantity is exceeding stock available quantity");
                    isValid = false;
                }
                else {
                    parentItem.quantity = parseInt(parentItem.quantity);
                    isValid = true;
                }
            }
            if (!!item) {
                if (!item.quantity) {
                    item.isQuantityValid = true;
                    growl.error("Please enter quantity");
                    isValid = false;
                }
                else if (parseInt(item.quantity) > item.substituteNewQty) {
                    item.isQuantityValid = true;
                    growl.error("Please enter quantity less than or equal to ordered quantity");
                    isValid = false;
                }
                else if (!item.quantity > item.stockAvailable) {
                    item.isQuantityValid = true;
                    growl.error("Entered Quantity is exceeding stock available quantity");
                    isValid = false;
                }
            }
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
                    AddBatchToDetail(self.model.assigningPickerDispenserDetails, itemId, batchId);
                }

                event.stopPropagation();
                event.preventDefault();

                SetFocus();
            }
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

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("OPassigningPickerDispenserDetails", {
            url: "/OPassigningPickerDispenserDetails",
            templateUrl: 'views/PharmacyInventory/InPatient/op-assigning-picker-dispenser-details.html',
            controller: "OPAssigningPickerDispenserDetails.Controller",
            controllerAs: "vm",
            title: 'Pending List for Assigning Picker'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("OPAssigningPickerDispenserDetails.Controller", OPassigningPickerDispenserDetailsController)
})();