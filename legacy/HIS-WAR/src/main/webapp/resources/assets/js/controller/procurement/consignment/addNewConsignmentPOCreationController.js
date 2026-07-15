(function () {
    "use strict";

    function addNewConsignmentPOCreationController($scope, $rootScope, $http, CONSTANTS, $state, StatusService, commonDetailService, $filter, CurrencyService, GenericService, growl,$cookies) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {

                fromDate: new Date(),
                toDate: new Date(),
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                storeList: [{
                    "store": "Select Store"
                }],
                itemDetails: [],

            };

            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.ValidateDate = ValidateDate;
            self.model.AddItemForPO = AddItemForPO;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.AddVendorName = AddVendorName;
            self.model.ValidateDetails = ValidateDetails;
            self.model.SetDecimal = SetDecimal;
            self.model.ValidateConsumableAmount = ValidateConsumableAmount;
            self.model.OpenPopup = OpenPopup;
            self.model.ClosePopup = ClosePopup;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;
            self.model.savePOCreation = savePOCreation;
            PopulateDropdown();

        }

        StatusService.GetStatus().then(function (status) {
            self.model.statuses = status;
        });

        var setStatus = function (x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var ValidateDate = function () {
            var isValid = true;
            if (self.model.fromDate > self.model.toDate) {
                growl.error('From date should be less than to Date');
                isValid = false;
            } else if (self.model.fromDate == self.model.toDate) {
                isValid = true;
            }

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Please enter valid date');
                isValid = false;
            }

            return isValid;
        }

        var SearchVendorNames = function (search) {
            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        if (!!response.data && response.data.length > 0) {
                            self.model.vendorNames = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.vendorNames, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return nameNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return nameNotFound(search);
                    });
            } else {
                if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].itemFound != undefined && !self.model.vendorNames[0].itemFound) {
                    return nameNotFound(search);
                } else {
                    return $filter('filter')(self.model.vendorNames, {
                        $: search
                    });
                }
            }
        }

        var AddVendorName = function (vendor) {
            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
        }

        var ValidateDetails = function () {
            var isValid = true;

            if (!self.model.store.id) {
                isValid = false;
                self.model.isStoreValid = true;
            }
            if (!self.model.vendorId || !self.model.vendorName) {
                isValid = false;
                self.model.isVendorValid = true;
            }
            if (!self.model.store.id || !self.model.vendorName || !self.model.vendorId) {
                isValid = false;
                growl.error('Please select required field');
            }

            return isValid;
        }

        var PopulateDropdown = function () {
            //self.model.store = self.model.storeList[0];
          
        	var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_CREATION.DROPDOWN_API + "/"+$scope.unitId +"/"+ $scope.organizationId +"/"+$scope.userId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeList = self.model.storeList.concat(response.data.requestingStores);
                    for(var i=0;self.model.storeList.length > i;i++)
                    {
                        if(self.model.storeList[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
                        {
                            self.model.store = self.model.storeList[i];
                        }
                    }
                    var purchaseId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!purchaseId) {
                        commonDetailService.setDataId(null);
                        GetPurchaseOrder(purchaseId);
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetPurchaseOrder = function (poId) {
            commonDetailService.setDataId(null);

            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_CREATION.DETAILS_API + $scope.unitId + '/' + poId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.poId = response.data.id;
                    self.model.vendorId = response.data.vendorId;
                    self.model.vendorName = response.data.vendorName;
                    self.model.store = self.model.storeList.find(function (store) {
                        return response.data.storeId == store.id;
                    });
                    self.model.totalAmount = response.data.totalAmount;
                    self.model.totalTax = response.data.taxAmount;
                    self.model.totalNetAmount = response.data.netAmount;
                    self.model.purchaseOrderRemark = response.data.remark;

                    self.model.itemDetails = [];
                    angular.forEach(response.data.items, function (item) {
                        var issueList = [];
                        angular.forEach(item.issueList, function (dt) {
                            issueList.push({
                                issueId: dt.issueId,
                                issueNo: dt.issueNo,
                                issueDate: dt.issueDate,
                                visitTypeId: dt.visitTypeId,
                                visitType: dt.visitType,
                                visitNo: dt.visitNo,
                                patientDetails: dt.patientDetails,
                                issueItemCode: dt.issueItemCode,
                                batchNo: dt.batchNo,
                                issueItemName: dt.issueItemName,
                                batchId: dt.batchId,
                                issueExpiry: dt.issueExpiry,
                                issueQty: dt.issueQty,
                                issueCop: dt.issueCop,
                                issueMrp: dt.issueMrp,
                                issueTaxCode: dt.issueTaxCode,
                                issueTaxPercentage: dt.issueTaxPercentage,
                                issueNetAmount: dt.issueNetAmount,
                                patientId: dt.patientId
                            });
                        });

                        var poItem = {
                            "id": item.id,
                            "store": item.store,
                            "storeId": item.storeId,
                            "itemId": item.itemId,
                            "itemCode": item.itemCode,
                            "itemName": item.itemName,
                            "batchNo": item.batchNo,
                            "batchId": item.batchId,
                            "expiry": item.expiry,
                            "quantity": item.quantity,
                            "cop": item.cop.toFixed(2),
                            "mrp": item.mrp.toFixed(2),
                            "taxCode": item.taxCode,
                            "taxPercentage": item.taxPercentage.toFixed(2),
                            "taxAmount": item.taxAmount,
                            "totalAmount": item.totalAmount,
                            "netAmount": item.netAmount,
                            "remark": item.remark,
                            "issueList": issueList
                        }
                        self.model.itemDetails.push(poItem);
                    });
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var AddItemForPO = function () {
            if (ValidateDate() && ValidateDetails()) {

                var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_CREATION.GET_ITEM;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "vendorId": self.model.vendorId,
                    "unitId":$scope.unitId
                }

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.vendorId = response.data.vendorId;
                        self.model.storeId = response.data.storeId;
                        angular.forEach(response.data.items, function (item) {
                            var issueList = [];
                            angular.forEach(item.issueList, function (dt) {
                                issueList.push({
                                    issueId: dt.issueId,
                                    issueNo: dt.issueNo,
                                    issueDate: dt.issueDate,
                                    visitTypeId: dt.visitTypeId,
                                    visitType: dt.visitType,
                                    visitNo: dt.visitNo,
                                    patientDetails: dt.patientDetails,
                                    issueItemCode: dt.issueItemCode,
                                    batchNo: dt.batchNo,
                                    issueItemName: dt.issueItemName,
                                    batchId: dt.batchId,
                                    issueExpiry: dt.issueExpiry,
                                    issueQty: dt.issueQty,
                                    issueCop: dt.issueCop,
                                    issueMrp: dt.issueMrp,
                                    issueTaxCode: dt.issueTaxCode,
                                    issueTaxPercentage: dt.issueTaxPercentage,
                                    issueNetAmount: dt.issueNetAmount,
                                    patientId: dt.patientId
                                });
                            });

                            var poItem = {
                                "id": item.id,
                                "store": item.store,
                                "storeId": item.storeId,
                                "itemId": item.itemId,
                                "itemCode": item.itemCode,
                                "itemName": item.itemName,
                                "batchNo": item.batchNo,
                                "batchId": item.batchId,
                                "expiry": item.expiry,
                                "quantity": item.quantity,
                                "cop": item.cop.toFixed(2),
                                "mrp": item.mrp.toFixed(2),
                                "taxId": item.taxId,
                                "taxCode": item.taxCode,
                                "taxPercentage": item.taxPercentage.toFixed(2),
                                "taxAmount": item.taxAmount,
                                "totalAmount": item.totalAmount,
                                "netAmount": item.netAmount,
                                "remark": item.remark,
                                "issueList": issueList
                            }
                            self.model.itemDetails.push(poItem);
                        });
                        //  self.model.purchaseOrders = response.data;
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.status);
                    });
            }
        }

        var SetDecimal = function (name, item, value) {
            if (name == "costOfPurchase") {
                if (isNaN(item.costOfPurchase) || !item.costOfPurchase || item.costOfPurchase == 0) {
                    item.costOfPurchase = 0;
                } else {
                    item.costOfPurchase = parseFloat(item.costOfPurchase).toFixed(2);
                }
            }

            if (name == "POAmountMRP") {
                if (isNaN(item.POAmountMRP) || !item.POAmountMRP || item.POAmountMRP == 0) {
                    item.POAmountMRP = 0;
                } else {
                    item.POAmountMRP = parseFloat(item.POAmountMRP).toFixed(2);
                }
            }

            if (name == "taxPercent") {
                if (isNaN(item.taxPercent) || !item.taxPercent) {
                    item.taxPercent = 0;
                } else {
                    item.taxPercent = parseFloat(item.taxPercent).toFixed(2);
                }
            }

            if (name == "taxPercentFixed") {
                if (isNaN(item.selectedTaxPercent) || !item.selectedTaxPercent) {
                    item.selectedTaxPercent = 0;
                } else {
                    item.selectedTaxPercent = parseFloat(item.selectedTaxPercent).toFixed(2);
                }
            }
        }

        var OpenPopup = function (action, modal) {
            self.model.action = action;
            var popup = angular.element('#' + modal);
            popup.modal('show');
        }
        var ClosePopup = function (action, modal) {
            self.model.action = action;
            var popup = angular.element('#' + modal);
            popup.modal('hide');
        }
        var ConfirmRemove = function (item) {
            self.model.OpenPopup('Open', 'confirmRemove');
            self.model.itemForRemove = item;
        }
        var RemoveItem = function (item) {
            var newArray = [];
            self.model.purchaseOrders.items.map(function (obj) {
                if (obj.itemId !== item.itemId) {
                    newArray.push(obj);
                }
            });
            self.model.purchaseOrders = newArray;
            self.model.ClosePopup('Close', 'confirmRemove');
        }

        var ValidateConsumableAmount = function (item, index) {
            var isValidItem = true;
            if (parseFloat(item.mrp) < parseFloat(item.cop)) {
                growl.error('MRP should be greater than COP');
                self.model.costOfPurchase[index] = true;
                self.model.mrp[index] = true;
                isValidItem = false;
            } else {
                self.model.costOfPurchase[index] = false;
                self.model.POAmountMRP[index] = false;
            }
            return isValidItem;
        }

        var NavigateToPO = function () {
            $state.go('consignmentPOCreation');
        }

        var ValidateItems = function () {
            var isValidItem = true;
            if (self.model.itemDetails.length <= 0) {
                growl.error('Please add items to save purchase order');
                isValidItem = false;
            } else {
                angular.forEach(self.model.itemDetails, function (item, index) {
                    if (!item.cop) {
                        self.model.costOfPurchase[index] = true;
                        growl.error("Please enter valid COP");
                        isValidItem = false;
                    }
                    if (parseFloat(item.mrp) < parseFloat(item.cop) || !item.mrp) {
                        self.model.mrp[index] = true;
                        growl.error('MRP should be greater than COP');
                        isValidItem = false;
                    }
                });
                return isValidItem;
            }
        }

        var savePOCreation = function () {
            if (ValidateItems()) {
                if (!self.model.poId) {
                    CreatePurchaseOrder();
                } else {
                    UpdatePurchaseOrder();
                }
            }
        }

        var CreatePurchaseOrder = function () {
            var purchaseOrder = self.model;

            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_CREATION.SAVE_API;

            var data = {
                "vendorId": purchaseOrder.vendorId,
                "storeId": purchaseOrder.storeId,
                "totalAmount": purchaseOrder.totalAmount,
                "totalTaxAmount": parseFloat(purchaseOrder.totalTaxAmount),
                "netAmount": parseFloat(purchaseOrder.totalNetAmount),
                "status": setStatus('New'),
                "approvalStatusId": setStatus('New'),
                "remark": self.model.purchaseOrderRemark,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };

            angular.forEach(self.model.itemDetails, function (item, key) {

                var issueList = [];
                angular.forEach(item.issueList, function (dt) {
                    issueList.push({
                        "issueId": dt.issueId,
                        "patientId": dt.patientId
                    });
                });

                var poItem = {
                    "itemId": item.itemId,
                    "batchId": item.batchId,
                    "poQuantity": parseInt(item.quantity),
                    "cop": parseFloat(item.cop),
                    "mrp": parseFloat(item.mrp),
                    "uomTypeId": item.uomTypeId,
                    "uomUnitId": item.uomUnitId,
                    "taxId": item.taxId,
                    "taxPercentage": parseFloat(item.taxPercentage),
                    "taxAmount": item.taxAmount.toFixed(2),
                    "totalAmount": parseFloat(item.totalAmnt),
                    "netAmount": item.netAmount.toFixed(2),
                    "statusId": setStatus('New'),
                    "remark": item.remark,
                    "issueList": issueList
                }

                data.items.push(poItem);
            });

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    NavigateToPO();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var UpdatePurchaseOrder = function () {
            var purchaseOrder = self.model;

            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_PO_CREATION.UPDATE_API + self.model.poId;
            var data = {
                "totalAmount": purchaseOrder.totalAmount,
                "totalTaxAmount": parseFloat(purchaseOrder.totalTaxAmount),
                "netAmount": parseFloat(purchaseOrder.totalNetAmount),
                "remark": self.model.purchaseOrderRemark,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            }

            angular.forEach(self.model.itemDetails, function (item, key) {
                var poItem = {
                    "id": item.id,
                    "itemId": item.itemId,
                    "batchId": item.batchId,
                    "cop": parseFloat(item.cop),
                    "mrp": parseFloat(item.mrp),
                    "taxAmount": item.taxAmount.toFixed(2),
                    "totalAmount": parseFloat(item.totalAmnt),
                    "netAmount": item.netAmount.toFixed(2),
                    "statusId": setStatus('New'),
                    "remark": item.remark,
                }
                data.items.push(poItem);
            });

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    NavigateToPO();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("pendingForPOCreation", {
            url: "/pendingForPOCreation",
            templateUrl: 'views/procurement/consignment/pending-for-po-creation.html',
            controller: "AddNewConsignmentPOCreation.Controller",
            controllerAs: "vm",
            title: 'Consignment PO Creation'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewConsignmentPOCreation.Controller", addNewConsignmentPOCreationController);
})();
