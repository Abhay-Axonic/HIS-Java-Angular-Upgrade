
(function () {
    "use strict";

    function addNewScrapRequestController($scope, $rootScope, $http, $state, commonDetailService, PaginationService, CurrencyService, CONSTANTS, GenericService, growl,$cookies,$filter) {
        var self = this;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
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
                stores: [{
                    "store": "Select Stores"
                }],
                assetTypes: [{
                    "type": "Select Asset Type"
                }],
                nearExpired: [{
                    "expiry": "Select Expiry"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                },
                toDateOptions1: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                fromDate: new Date(),
                toDate: new Date(),
                SelectedItemsIds: [],
                selectAllRows: false,
                NewItemList: [],
                itemList: [],
                selectscraporauction: 1
            };
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.GetItemList = GetItemList;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.getReturnReasonList = getReturnReasonList;
            self.model.searchNewItem = searchNewItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isSelected = isSelected;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.removeRowData = removeRowData;
            self.model.SaveScrapRequest = SaveScrapRequest;
            self.model.NavigteToBack = NavigteToBack;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.searchByNearExpiry = searchByNearExpiry;
            self.model.GetScrapRequestDetails = GetScrapRequestDetails;
            self.model.onNearExpiryChange = onNearExpiryChange;
            self.model.confirmClear = confirmClear;
            self.model.SaveNewScrapRequest = SaveNewScrapRequest;
            self.model.UpdateScrapRequest = UpdateScrapRequest;
            self.model.validateSave = validateSave;
            self.model.itemRemovePopup = itemRemovePopup;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateReason = ValidateReason;
            self.model.CalculateCOP = CalculateCOP;
            self.model.SetOldValueStore = SetOldValueStore;
            self.model.ChangeStore = ChangeStore;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            self.model.searchByExpiry = searchByExpiry;

            getReturnReasonList();
            PopulateDropdown();

        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        function removeRowData(item) {
            var index = self.model.itemList.indexOf(item);
            self.model.itemList.splice(index, 1);
        }

        var PopulateDropdown = function () {
            var scrap = self.model;
           // scrap.store = scrap.stores[0];
            scrap.assetType = scrap.assetTypes[0];
            scrap.nearExpiry = scrap.nearExpired[0];
            $scope.expiryType = [{"id": 2, "expiry":" Expired"},{"id":1,"expiry": " Near Expiry"}];
            var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    scrap.stores = scrap.stores.concat(response.data.store);
                    scrap.assetTypes = scrap.assetTypes.concat(response.data.assetType);
                    scrap.nearExpired = scrap.nearExpired.concat($scope.expiryType);
                    scrap.assetType = scrap.assetTypes[1];
                    var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
                    var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
                    for(var i=0;i<scrap.stores.length;i++)
                    {
                        if(scrap.stores[i].id==selectedStoreId)
                        {
                            scrap.store = scrap.stores[i];
                            self.model.store = scrap.store;
                        }
                    }
                    $rootScope.stopLoader();
                    var scrapId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!scrapId) {
                        commonDetailService.setDataId(null);
                        GetScrapRequestDetails(scrapId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function GetItemList() {
           // if(self.model.nearExpiry.expiry == "Near Expiry" || self.model.nearExpiry.expiry == "Expired"){
        	 if(self.model.nearExpiry.expiry == "Near Expiry"){
                self.model.action = 'Open';
                var popup = angular.element('#nearExpiry1');
                popup.modal('show');
                self.model.NewItemList = [];
                if(self.model.nearExpiry.expiry == "Expired"){
                    $("#searchByExpiry").show();
                    $("#searchByNearExpiry").hide();
                }
                else{
                    $("#searchByExpiry").hide();
                    $("#searchByNearExpiry").show();
                }
            }
            
            else if (self.model.nearExpiry.expiry == "Expired"){
                self.model.action = 'Open';
                var popup = angular.element('#nearExpired');
                popup.modal('show');
                self.model.NewItemList = [];
                $("#searchByNearExpiry").hide();
                $("#searchByExpiry").hide();
            	
            }
            
            else {
                self.model.action = 'Open';
                var popup = angular.element('#additemlist');
                popup.modal('show');
                GetAssetCategoryList();
                self.model.NewItemList = [];
                self.model.pagination.data=[];
                self.model.pagination.totalPages=0;
            }
        }

        var GetAssetCategoryList = function () {
            var item = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetType.id+ '/'+$scope.unitId+ '/'+$scope.organizationId;


            item.assetCategory = item.assetCategories[0];
            item.productCategory = item.productCategories[0];
            item.assetCategories.splice(1);
            item.productCategories.splice(1);


            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    item.assetCategories = item.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var item = self.model;
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;

            item.productCategory = item.productCategories[0];
            item.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    item.productCategories = item.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var getReturnReasonList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var scrap = self.model;

            var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.SCRAP_REASON_API+"/"+$scope.organizationId ;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    scrap.scrapReasons = response.data;
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function searchNewItem(getallItems) {

          if (
                (self.model.search != undefined && self.model.search.itemName != undefined  && self.model.search.itemName.replace(/\s/g, "").length == 2) ||
                (self.model.search != undefined && self.model.search.itemCode != undefined  && self.model.search.itemCode.replace(/\s/g, "").length == 2) ||
                (self.model.search != undefined && self.model.search.batchNo != undefined  && self.model.search.batchNo.replace(/\s/g, "").length == 2 || getallItems == true)
            ) {

            var URI = CONSTANTS.GLOBAL.BATCH_API_STORE_WISE;
            const data = {
                "assetTypeId": self.model.assetType.id,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                "storeId": self.model.stores.id || self.model.store.id,
                "itemCode": undefined,
                "itemName": undefined,
                "batchNo": undefined,
                "unitId":$scope.unitId,
                "fromScrap":true,
                "stockAvailable" :self.model.nearExpiry.id
            }
            if(self.model.search){
                data.itemCode =  self.model.search.itemCode;
                data.itemName = self.model.search.itemName;
                data.batchNo = self.model.search.batchNo;
            }

            // self.model.NewItemList = [];
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    self.model.NewItemList = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                    }
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
                 } else if (self.model.search && self.model.search.itemName != undefined && self.model.search.itemName.replace(/\s/g, "").length > 0) {
                        var list  = $filter('filter')(self.model.NewItemList, {
                            $: self.model.search.itemName
                        });
                        PaginationService.searchDataByField(list, self.model.pagination);
                    } else if (self.model.search && self.model.search.itemCode != undefined && self.model.search.itemCode.replace(/\s/g, "").length > 0) {
                        var list = $filter('filter')(self.model.NewItemList, {
                            $: self.model.search.itemCode
                        });
                        PaginationService.searchDataByField(list, self.model.pagination);
                    } else if (self.model.search && self.model.search.batchNo != undefined && self.model.search.batchNo.replace(/\s/g, "").length > 0) {
                       var list = $filter('filter')(self.model.NewItemList, {
                            $: self.model.search.batchNo
                        });
                        PaginationService.searchDataByField(list, self.model.pagination);
                    }
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                if(self.model.SelectedItemsIds){
                    self.model.data = self.model.NewItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedItemsIds.push(item.itemId);
                        return self.model.SelectedItemsIds;
                    });
                }
                else{
                    self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        return item.itemId;
                    });
                }
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

        function getAllItemDetails() {

            self.model.NewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    if (self.model.assetType.id == 1) {
                        var isFound = self.model.itemList.find(function (x) {
                            return x.batchId == item.batchId;

                        });
                        item.uomList = item.uomResponses;
                        if (!isFound) {
                            item.cop = item.cop.toFixed(2);
                            item.uomList.find(function(u){
                                if(u.uomType == item.uomType){
                                    item.uomType = u.uomDetails;
                                }
                            });
                            if(item.scrapQuantity == undefined || item.scrapQuantity == null){
                                item.scrapQuantity = 0;
                            }
                            if(item.isThreeUOMApplicable == false){
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            self.model.itemList.push(item);
                        }
                        else {
                            growl.error('Item already present in the list')
                        }
                    }
                    else {
                        var isFound = self.model.itemList.find(function (x) {
                            return x.batchId == item.batchId;

                        });
                        if (!isFound) {
                           if(item.cop){
                            item.cop = item.cop.toFixed(2);
                            item.uomList.find(function(u){
                                if(u.uomType == item.uomType){
                                    item.uomType = u.uomDetails;
                                }
                            })
                            if(item.isThreeUOMApplicable == false){
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            self.model.itemList.push(item);
                           }
                          else{
                            item.cop = 0;
                            item.uomList.find(function(u){
                                if(u.uomType == item.uomType){
                                    item.uomType = u.uomDetails;
                                }
                            })
                            if(item.isThreeUOMApplicable == false){
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            self.model.itemList.push(item);
                          }
                           
                        }
                        else {
                            growl.error('Item already present in the list')
                        }
                    }
                }
            });
        }

        var SaveScrapRequest = function () {
            if (validateSave()) {
                if (!self.model.scrapId) {
                    SaveNewScrapRequest();
                } else {
                    UpdateScrapRequest();
                }
            }
        }

        var SaveNewScrapRequest = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.SAVE_API;
            // if(self.model.nearExpiry.expiry == "Select Expiry"){
            //     self.model.nearExpiry.expiry = false;
            // }
            // if(self.model.nearExpiry.expiry == "Near Expiry"){
            //     self.model.nearExpiry.expiry = true;
            // }
            var data = {

                "storeId": self.model.store.id,
                "assetTypeId": self.model.assetType.id,
                "nearByExpiry": self.model.nearExpiry.expiry.id,
                "remark": self.model.remark,
                "statusId": 1,
                "approvalStatusId": 1,
                "scrapType": self.model.selectscraporauction,
                "organizationId":$scope.organizationId,
                "unitId": $scope.unitId,
                "userId": $scope.userId,
                "items": []
            };
            angular.forEach(store.itemList, function (item, key) {
                var storeItem = {
                    "batchId": item.batchId,
                    "scrapQuantity": parseInt(item.scrapQuantity),
                    "scrapReasonId": item.scrapReason.id,
                    "uomTypeId": item.uomTypeId,
                    "uomUnitId": item.uomUnitId,
                    "amountCop": item.amountAtCop,
                    "statusId": 1
                }
                data.items.push(storeItem);

            });
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    // ClearRequests();
                    $rootScope.stopLoader();
                    NavigteToBack();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var NavigteToBack = function () {
            $state.go('scrapRequest');
        }
        var ValidateDate = function () {
            var isValid = true;

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        function searchByNearExpiry() {
            if (ValidateDate()) {
                var URI = CONSTANTS.GLOBAL.NEAR_EXPIRY;
                const data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "assetTypeId":self.model.assetType.id,
                    "fromStoreId": self.model.store.id,
                    "isConsignment": self.model.isConsignment ? self.model.isConsignment : false,
                     "unitId": $scope.unitId 
                }

                // self.model.NewItemList = [];
                $rootScope.startLoaderPopup();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    self.model.NewItemList = response.data;
                    angular.forEach(self.model.NewItemList, function(item){
                        if(item.uomType != undefined && item.uomType != null){
                            item.uomList.find(function(u){
                                if(u.uomType == item.uomType){
                                    item.uomType = u.uomDetails;
                                }
                            })
                            if(item.isThreeUOMApplicable == false){
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                            else{
                                if(item.uomTypeId == 1){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else if(item.uomTypeId == 2){
                                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                                else{
                                    item.receivedQtyCon = (item.scrapQuantity);
                                    item.receivedQtyCalculation = (item.scrapQuantity);
                                    item.uomUnitData = item.uomList[2].uomUnit;
                                }
                            }
                        }
                    });
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
            }
        }

        
        
        $scope.searchByNearExpired = function() {
            var URI = CONSTANTS.GLOBAL.DATE_NEAR_EXPIRY;
            const data = {
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "assetTypeId": self.model.assetType.id,
                "fromStoreId": self.model.store.id,
                "isConsignment": self.model.isConsignment ? self.model.isConsignment : false,
                 "unitId":$scope.unitId
            }
            // self.model.NewItemList = [];
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoaderPopup();
                self.model.NewItemList = response.data;
                angular.forEach(self.model.NewItemList, function(item){
                    if(item.uomType != undefined && item.uomType != null){
                        item.uomList.find(function(u){
                            if(u.uomType == item.uomType){
                                item.uomType = u.uomDetails;
                            }
                        })
                        if(item.isThreeUOMApplicable == false){
                            if(item.uomTypeId == 1){
                                item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                                item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){
                                
                            }
                            else{
                                item.receivedQtyCon = (item.scrapQuantity);
                                item.receivedQtyCalculation = (item.scrapQuantity);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uomTypeId == 1){
                                item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){
                                item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                                item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.scrapQuantity);
                                item.receivedQtyCalculation = (item.scrapQuantity);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                        }
                    }
                });
            },
            function errorCallback(response) {
                $rootScope.stopLoaderPopup();
                growl.error('Error');
            });
        }
        
        
        function searchByExpiry() {
            if (ValidateDate()) {
                var URI = CONSTANTS.GLOBAL.EXPIRY;
                const data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "assetTypeId": 1,
                    "fromStoreId": self.model.store.id,
                    "isConsignment": self.model.isConsignment ? self.model.isConsignment : false,
                    "unitId":$scope.unitId		
                }

                self.model.NewItemList = [];
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.NewItemList = response.data;
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
            }
        }

        var GetScrapRequestDetails = function (scrapId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.DETAIL_API + scrapId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.scrapId = response.data.id;
                    self.model.remark = response.data.remark;
                    self.model.itemList = [];
                    self.model.itemList = response.data.items;

                    self.model.store = self.model.stores.find(function (scrap) {
                        return response.data.storeId == scrap.id;
                    });

                    self.model.assetType = self.model.assetTypes.find(function (scrap) {
                        return response.data.assetTypeId == scrap.id;
                    });

                    angular.forEach(self.model.itemList, function (item, key) {
                        if(item.cop){
                            item.cop = item.cop.toFixed(2);
                        }
                        item.amountAtCop = item.amountCop.toFixed(2);
                        item.scrapReason = self.model.scrapReasons.find(function (scrap) {
                            return scrap.id == item.scrapReasonId;
                        });
                        item.uomList.find(function(u){
                            if(u.uomType == item.uomType){
                                item.uomType = u.uomDetails;
                            }
                        })
                        if(item.isThreeUOMApplicable == false){
                            if(item.uomTypeId == 1){
                                item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                                item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){
                                
                            }
                            else{
                                item.receivedQtyCon = (item.scrapQuantity);
                                item.receivedQtyCalculation = (item.scrapQuantity);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                        }
                        else{
                            if(item.uomTypeId == 1){
                                item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion +"*"+ item.uomList[1].conversion);
                                item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                            else if(item.uomTypeId == 2){
                                item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                                item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                            else{
                                item.receivedQtyCon = (item.scrapQuantity);
                                item.receivedQtyCalculation = (item.scrapQuantity);
                                item.uomUnitData = item.uomList[2].uomUnit;
                            }
                        }
                    });

                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }


        var itemRemovePopup = function (item) {
            self.model.itemRemove = '';
            self.model.itemRemove = item;
            $("#itemRemovePopup").modal()

        }

        var confirmClear = function () {
            self.model.ItemDetailList = [];

        }
        var onNearExpiryChange = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if (self.model.itemList.length > 0) {
                $("#confirmLogout1").modal()
            }
        }


        var UpdateScrapRequest = function () {

            var scrap = self.model;

            var URI = CONSTANTS.INVENTORY.SCRAP.SCRAP_REQUEST.UPDATE_API + scrap.scrapId;
            var data = {
                "remark": self.model.remark,
                "organizationId":$scope.organizationId,
                "unitId": $scope.unitId,
                "userId":$scope.userId,
                "item": []
            };

            angular.forEach(self.model.itemList, function (item, key) {
                var storeItem = {
                    "id": item.id,
                    "scrapQuantity": parseInt(item.scrapQuantity),
                    "scrapReasonId": item.scrapReason.id,
                    "amountCop": item.amountAtCop,
                }

                if (item.id == '' || item.id == null) {
                    storeItem.batchId = item.batchId,
                        storeItem.scrapQuantity = parseInt(item.scrapQuantity),
                        storeItem.scrapReasonId = item.scrapReason.id,
                        storeItem.uomTypeId = item.uomTypeId,
                        storeItem.uomUnitId = item.uomUnitId,
                        storeItem.amountCop = item.amountAtCop,
                        storeItem.statusId = 1
                }
                data.item.push(storeItem);


            });
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    NavigteToBack();
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var validateSave = function () {
            var isValid = true;
            var ItemDetailList = self.model.itemList;
            // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 
            if (ItemDetailList.length < 1) {
                growl.error('Error', {
                    title: "Please validate all required fields"
                });
                isValid = false;
            }
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateQuantity(ItemDetail)) {
                    isValid = false;
                }
                if (!ValidateReason(ItemDetail)) {
                    isValid = false;
                }
            }

            return isValid;
        }

        var ValidateQuantity = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.scrapQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Scrap Quantity");
            } else if (parseInt(item.scrapQuantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Scrap Quantity cannot be zero');
            } else if (parseInt(item.scrapQuantity) > item.availableQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered scrap quantity is exceeding the stock available quantity');
            } else {
                item.scrapQuantity = parseInt(item.scrapQuantity);
                isValid = true;
            }

            return isValid;
        }

        var ValidateReason = function (item) {
            var isValid = true;

            if (!item.scrapReason) {
                item.isReasonValid = true;
                isValid = false;
                growl.error("Please Select Reason");
            }
            return isValid;
        }
        
        var CalculateCOP = function (item) {
            if (parseInt(item.scrapQuantity) > 0 && parseFloat(item.cop) > 0) {
                item.amountAtCop = (parseInt(item.scrapQuantity) * parseFloat(item.cop)).toFixed(2);
            } else {
                item.amountAtCop = 0;
            }
        }

        var SetOldValueStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeStore = function (store) {
            if (!!self.model.itemList.length) {
                self.model.store = self.model.oldStore;
                self.model.storeNew = JSON.parse(JSON.stringify(store));
                $("#storeChange").modal()
            }
        }

        var ConfirmStorePopup = function () {
            self.model.store = self.model.storeNew;
            self.model.itemList = [];
            ModalDismiss('storeChange');

        }

        $scope.calculateReceivedQty = function(item){
            if(item.isThreeUOMApplicable == false){
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion);
                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                        
                }
                else{
                    item.receivedQtyCon = (item.scrapQuantity);
                    item.receivedQtyCalculation = (item.scrapQuantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
            else{
                if(item.uomTypeId == 1){
                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else if(item.uomTypeId == 2){
                    item.receivedQtyCon = (item.scrapQuantity +"*"+ item.uomList[1].conversion);
                    item.receivedQtyCalculation = (item.scrapQuantity * item.uomList[1].conversion);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
                else{
                    item.receivedQtyCon = (item.scrapQuantity);
                    item.receivedQtyCalculation = (item.scrapQuantity);
                    item.uomUnitData = item.uomList[2].uomUnit;
                }
            }
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addItemScrapApproval", {
            url: "/addItemScrapApproval",
            templateUrl: 'views/PharmacyInventory/Store/scrap-approval-add-new-item.html',
            controller: "AddNewScrapRequest.Controller",
            controllerAs: "vm",
            title: 'Scrap Request'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewScrapRequest.Controller", addNewScrapRequestController);
})();

