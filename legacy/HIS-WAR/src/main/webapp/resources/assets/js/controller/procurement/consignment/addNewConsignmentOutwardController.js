(function () {
    "use strict";

    function addNewConsignmentOutwardController($scope, $rootScope, $http, $filter, $state, CONSTANTS, CurrencyService,PaginationService, GenericService, commonDetailService, cancelReasonService, StatusService, growl,$cookies) {
        var self = this;

        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                pagination: {
                    currentPage: 1,
                    totalPages: 0,
                    pageSize: '10',
                    search: '',
                    searchData: PaginationService.searchData,
                    searchDataByField:PaginationService.searchDataByField,
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                },
                dcDate: new Date(),
                dcNo: '',
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                stores: [{
                    "store": "Select Store"
                }],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },

                SelectedItemsIds: [],
                itemList: [],


            };

            cancelReasonService.GetReason().then(function (reason) {
                self.model.cancelReasons = reason;
            });
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            StatusService.GetStatus().then(function (status) {
                self.model.statusList = status;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.GetConsignmentOutwardDetails = GetConsignmentOutwardDetails;
            self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.NavigateToAddNew = NavigateToAddNew;
            self.model.NavigateToDetails = NavigateToDetails;
            self.model.GetItemList = GetItemList;
            self.model.GetAllItemDetails = GetAllItemDetails;
            self.model.NavigateToEdit = NavigateToEdit;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isItemChecked = false;
            self.model.RemoveItem = RemoveItem;
            self.model.isDisable = isDisable;
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.AddVendorName = AddVendorName;
            self.model.VendorDetails = VendorDetails;
            self.model.ModalDismiss = ModalDismiss;
            self.model.PopupAction = PopupAction;
            self.model.isEdit = isEdit;
            self.model.isCancel = isCancel;
            self.model.SaveConsignmentOutwardRequestDetails = SaveConsignmentOutwardRequestDetails;
            self.model.CreateConsignmentOutwardRequestDetails = CreateConsignmentOutwardRequestDetails;
            self.model.UpdateConsignmentOutwardRequestDetails = UpdateConsignmentOutwardRequestDetails
            self.model.ValidateDetails = ValidateDetails;
            self.model.ValidetReturnQuantity = ValidetReturnQuantity;
            self.model.SetOldValueStore = SetOldValueStore;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.ChangeFromStore = ChangeFromStore;
            self.model.searchNewItem = searchNewItem;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            PopulateDropdown();
        }

        var PopulateDropdown = function () {
            try {
                $rootScope.startLoader();
              //  self.model.store = self.model.stores[0];
                var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
                GenericService.serviceAction("GET", URI, {}).then(
                    function (response) {
                        self.model.stores = self.model.stores.concat(response.data.store);
                        for(var i=0;i<self.model.stores.length;i++)
                        {
                        	 	 if(self.model.stores!=undefined && self.model.stores[i].id!=undefined){
                        	 		 var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
                        	 		 if(self.model.stores[i].id==(selectedTopStore != null ? selectedTopStore.storeId : undefined))
                        		 {
                        			 self.model.store = self.model.stores[i];
                        		 }
                        	 }
                        }

                        var outwardId = commonDetailService.getDataId();
                        $rootScope.stopLoader();
                        if (!!outwardId) {
                            GetConsignmentOutwardDetails(outwardId);
                        }
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
            } catch (error) {
                alert(error);
            }
        }

        var GetProductCategoryList = function (assetCategory) {
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + self.model.assetCategory.id + '/'+$scope.organizationId;

            self.model.productCategory = self.model.productCategories[0];
            self.model.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.productCategories = self.model.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var VendorDetails = function (vendorDetails) {
            self.model.personName = vendorDetails.contactPerson;
            self.model.address = vendorDetails.address;
            self.model.country = vendorDetails.country;
            self.model.state = vendorDetails.state;
            self.model.city = vendorDetails.city;
            self.model.area = vendorDetails.area;
            self.model.zipCode = vendorDetails.zipCode;
            self.model.vendorId = vendorDetails.id;
        }

        var setStatus = function (x) {
            var status = self.model.statusList.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.maxDate = self.model.dcDate;
        }

        var SetOldValueStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeFromStore = function (store) {
            if (!!self.model.itemList.length) {
                self.model.store = self.model.oldStore;
                self.model.storeNew = JSON.parse(JSON.stringify(store));
                PopupAction('Open', 'confirmStore');
            }
        }

        var ConfirmStorePopup = function () {
            self.model.store = self.model.storeNew;
            PopupAction('close', 'confirmStore');
            self.model.itemDetails = [];
        }

        var PopupAction = function (action, modal) {
            var popup = angular.element('#' + modal);
            self.model.action = action;
            if (action == "Open") {
                popup.modal('show');
            } else {
                popup.modal('hide');
            }
        }

        var GetConsignmentOutwardDetails = function (outwardId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        
            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.DETAILS_API + $scope.unitId + '/'+ outwardId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.outwardId = response.data.id;
                    self.model.store = self.model.stores.find(function (batch) {
                        return response.data.storeId == batch.id;
                        //self.model.store.id = response.data.storeId;
                    });
                    self.model.itemList = [];
                    self.model.remark = response.data.remark;
                    self.model.itemList = response.data.item;
                    self.model.dcDate = response.data.dcDate;
                    self.model.dcNo = response.data.dcNo;
                    self.model.vendorName = response.data.vendorName;
                    self.model.vendorId = response.data.vendorId;
                    angular.forEach(self.model.itemList, function (item, key) {
                        // item.cop = item.cop.toFixed(2);
                    });

                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ValidateDate = function () {
            var isValid = true;
            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            } else if (self.model.fromDate == self.model.toDate) {
                isValid = true;
            }

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var ValidateDetails = function () {
            var isValid = true;

            if (!self.model.store.id) {
                isValid = false;
                self.model.isStoreValid = true;
            }
            if (!self.model.store.id) {
                isValid = false;
                growl.error('Please select required field');
            }

            return isValid;
        }

        var ValidetReturnQuantity = function (item) {
            var isValid = true;
            if (item.quantity ==0) {
                growl.error('Return Quantity should not be zero.');
                item.isReturnQtyValid = true;
                isValid = false;
            }
            if (item.quantity > item.availableQuantity) {
                growl.error('Return Quantity should be less than or equal to available quantity');
                item.isReturnQtyValid = true;
                isValid = false;
            }
            if (!item.quantity) {
                item.isReturnQtyValid = true;
                isValid = false;
                growl.error("Please Enter Return Quantity.");

            }
            return isValid;
        }

        var ConfirmRemove = function (item) {
            self.model.PopupAction('Open', 'confirmRemove');
            self.model.itemForRemove = item;
        }

        var RemoveItem = function (item) {
            var newArray = [];

            self.model.itemList.map(function (obj) {
                if (obj !== item) {
                    newArray.push(obj);
                }
            });
            self.model.itemList = newArray;
            self.model.PopupAction('Close', 'confirmRemove');
        }

        var searchNewItem = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if(self.model.search.itemName){
                if(self.model.search.itemName.length >= 3){
                    var URI = CONSTANTS.GLOBAL.NEW_BATCH_API + true;
                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "vendorId": self.model.vendorId,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo,
                        "unitId":$scope.unitId

                    }

                    self.model.NewItemList = [];
                    self.model.pagination.data=[];
                    self.model.pagination.totalPages=0;
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;

                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.NewItemList = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                }
            }
            else if(self.model.search.itemCode){
                if(self.model.search.itemCode.length >= 3){
                    var URI = CONSTANTS.GLOBAL.NEW_BATCH_API + true;
                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "vendorId": self.model.vendorId,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo,
                        "unitId":$scope.unitId

                    }

                    self.model.NewItemList = [];
                    self.model.pagination.data=[];
                    self.model.pagination.totalPages=0;
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;

                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.NewItemList = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                }
            }
            else{
                self.model.NewItemList = [];
                self.model.pagination.data=[];    
            }
        }

        var GetItemList = function () {
            if (ValidateDetails()) {
                self.model.selectAllRows = false;
                self.model.NewItemList = [];
                self.model.pagination.data=[];
                self.model.action = 'Open';
                var popup = angular.element('#additemlist');
                popup.modal('show');
                self.model.isItemChecked = false;
                self.model.search = {};
                PopulateAssetCategoryDropdown();
            }
        }

        var PopulateAssetCategoryDropdown = function () {
            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            self.model.assetCategory = self.model.assetCategories[0];
            self.model.productCategory = self.model.productCategories[0];

            self.model.assetCategories.splice(1);
            self.model.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.assetCategories = self.model.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetAllItemDetails = function () {

            self.model.NewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    var isFound = self.model.itemList.find(function (x) {
                        return x.batchId == item.batchId;

                    });
                    if (!isFound) {
                        item.mrp = item.mrp.toFixed(2);
                        self.model.itemList.push(item);
                    }
                    else {
                        growl.error('Item already present in the list')
                    }
                }
            });
        }

        var ValidateItems = function () {
            var isValidItem = true;
            var items = self.model.itemList;
            if (!self.model.outwardId && !self.model.vendorId) {
                growl.error("Please enter vendor");
                isValid = false;
            }
            else if (!items.length) {
                growl.error('Please add items');
                isValidItem = false;
            }
            else {
                angular.forEach(items, function (item) {
                    if (item.quantity ==0) {
                        growl.error('Return Quantity should not be zero.');
                        item.isReturnQtyValid = true;
                        isValid = false;
                    }
                    if (!item.quantity) {
                        item.isReturnQtyValid = true;
                        growl.error("Please Enter Return Quantity.");
                        isValidItem = false;
                    }
                    if (item.quantity > item.availableQuantity) {
                        growl.error('Return Quantity should be less than or equal to available quantity');
                        item.isReturnQtyValid = true;
                        isValidItem = false;
                    }
                })
            }
            return isValidItem;
        }

        var NavigateToAddNew = function () {
            $state.go('addNewConsignmentOutward');
        }

        var NavigateToDetails = function (workOrder) {
            commonDetailService.setDataId(workOrder.id);
            $state.go('detailsConsignmentOutward');
        }

        var NavigateToEdit = function (workOrder) {
            commonDetailService.setDataId(workOrder.id);
            $state.go('addNewConsignmentOutward');
        }

        var isDisable = function (datum) {
            if (datum.status != 'New') {
                return true;
            }
            return false;
        }

        var isEdit = function (datum) {
            if (datum.status == 'Pending') {
                return true;
            } else if (datum.status == 'New') {
                return false;
            } else if (datum.status == 'Closed' || datum.status == 'Cancelled') {
                return true;
            }
        }

        var isCancel = function (datum) {
            if (datum.status == 'Pending') {
                return true;
            } else if (datum.status == 'New') {
                return false;
            } else if (datum.status == 'Closed' || datum.status == 'Cancelled') {
                return true;
            }
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.NewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var SelectItem = function (item) {
            var itemIds = self.model.SelectedItemsIds;

            if (item.isSelected) {
                itemIds.push(item.itemId);
            } else {
                var index = itemIds.indexOf(item.itemId);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.NewItemList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }

            self.model.isItemChecked = false;
            self.model.NewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var SaveConsignmentOutwardRequestDetails = function () {
            if (ValidateItems()) {
                if (!self.model.outwardId) {
                    CreateConsignmentOutwardRequestDetails();
                } else {
                    UpdateConsignmentOutwardRequestDetails();
                }
            }
        }

        var CreateConsignmentOutwardRequestDetails = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.SAVE_API;

            var data = {
                "storeId": self.model.store.id,
                "statusId": 1,
                "vendorId": self.model.vendorId,
                "dcDate": self.model.dcDate,
                "dcNo": self.model.dcNo,
                "totalAmount": self.model.totalAmount,
                "totalTaxAmount": self.model.taxAmount,
                "totalNetAmount": self.model.netAmount,
                "remark": self.model.remark,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            };

            angular.forEach(self.model.itemList, function (item, key) {
                var storeItem = {
                    "batchId": item.batchId,
                    "quantity": item.quantity,
                    "totalAmount": item.totalAmount,
                    "taxId": item.taxId,
                    "taxPercentage": item.taxPercentage,
                    "totalTaxAmount": item.taxAmount,
                    "totalNetAmount": item.netAmount,
                    "uomUnitId": item.uomUnitId,
                    "uomTypeId": item.uomTypeId,
                    "cop": item.cop
                }
                data.items.push(storeItem);
            });

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    $state.go('consignmentOutward');
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var UpdateConsignmentOutwardRequestDetails = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_OUTWARD.UPDATE_API + self.model.outwardId;
            var data = {
                "id": self.model.outwardId,
                "dcDate": self.model.dcDate,
                "dcNo": self.model.dcNo,
                "vendorId": self.model.vendorId,
                "totalAmount": self.model.totalAmount,
                "totalTaxAmount": self.model.taxAmount,
                "totalNetAmount": self.model.netAmount,
                "remark": self.model.remark,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "storeId":$scope.storeId
            };

            angular.forEach(self.model.itemList, function (item, key) {
                if (!!item.id) {
                    var storeItem = {
                        "id": item.id,
                        "batchId": item.batchId,
                        "quantity": item.quantity,
                        "uomTypeId": item.uomTypeId,
                        "uomUnitId": item.uomUnitId,
                        "cop": item.cop,
                        "taxId": item.taxId,
                        "taxPercentage": item.taxPercentage,
                        "totalAmount": item.totalAmount,
                        "taxAmount": item.taxAmount,
                        "netAmount": item.netAmount
                    }
                }
                else {
                    var storeItem = {
                        "id": item.id,
                        "batchId": item.batchId,
                        "quantity": item.quantity,
                        "uomTypeId": item.uomTypeId,
                        "uomUnitId": item.uomUnitId,
                        "cop": item.cop,
                        "taxId": item.taxId,
                        "taxPercentage": item.taxPercentage,
                        "totalAmount": item.totalAmount,
                        "taxAmount": item.taxAmount,
                        "netAmount": item.netAmount
                    }
                }

                data.items.push(storeItem);
            });

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $state.go('consignmentOutward');
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var ModalDismiss = function (modal) {
            var popup = angular.element('#' + modal);
            popup.modal('hide');
            self.model.cancelNote = undefined;
            self.model.cancelReason = undefined;
        }

        function SearchVendorNames(search) {
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

        function nameNotFound(search) {
            var item = {
                "itemFound": false,
                "detail": "Vendor Not found with name " + search
            };
            self.model.vendorNames = [item];
            return self.model.vendorNames;
        }

        function AddVendorName(vendor) {
            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state('addNewConsignmentOutward', {
            url: '/addNewConsignmentOutward',
            templateUrl: 'views/procurement/consignment/add-new-consignment-outward.html',
            controller: "AddNewConsignmentOutward.Controller",
            controllerAs: "vm",
            title: 'Consignment Outward'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewConsignmentOutward.Controller", addNewConsignmentOutwardController);
})();