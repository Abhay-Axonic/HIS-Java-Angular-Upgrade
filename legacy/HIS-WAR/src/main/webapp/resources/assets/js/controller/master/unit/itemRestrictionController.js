(function () {
    "use strict";

    function itemRestrictionController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, $filter, $state, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                Stores: [{
                    "store": "Select Store"
                }],
                AssetTypes: [{
                    "type": "Select Asset Type"
                }],
                AssetCategories: [{
                    "category": "Select Asset Category"
                }],
                ProductCategories: [{
                    "category": "Select Product Category"
                }],
                selectAllRows: false,
                SelectedItemsIds: [],
                SelectedItemIds: [],
                itemRestrictionList: [],
                pagination: {
                    currentPage: 1,
                    totalPages: 0,
                    pageSize: '10',
                    search: '',
                    searchData: PaginationService.searchData,
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                  },
            };
            self.model.GetCategoryMappingList = GetCategoryMappingList;
            self.model.PopulateAssetCategory = PopulateAssetCategory;
            self.model.PopulateProductCategory = PopulateProductCategory;
            self.model.searchNewItem = searchNewItem;
            self.model.GetItemList = GetItemList;
            self.model.SelectAllItems = SelectAllItems;
            self.model.isItemChecked = false;
            self.model.searchNewItemList = [];
            self.model.SelectItem = SelectItem;
            self.model.AddItems = AddItems;
            self.model.GetItemRestriction = GetItemRestriction;
            self.model.SaveItem = SaveItem;
            self.model.UpdateItem = UpdateItem;
            self.model.ToggleStatus = ToggleStatus;
            self.model.validPopup = validPopup;
            self.model.SetOldValueStore = SetOldValueStore;
            self.model.ChangeFromStore = ChangeFromStore;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            self.model.OpenPopup = OpenPopup;
            self.model.ClosePopup = ClosePopup;
            SelectedItemsIds: [];
            itemSelectionList: [];
            //   GetItemRestriction();
            PopulateStore();
            PopulateAssetType();
        }

        //Get Item Restriction list 

        var GetItemRestriction = function (store) {


            if (store.id == undefined) {
                store.id = 0;
            }
            // else if(self.modal.store.id == undefined){
            //     store.id = 0;
            // }

            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.ITEM_RESTRICTION.GET_ITEM_RESTRICTION + store.id;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.itemRestrictionList = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.itemRestrictionList, self.model.pagination);
                      }
                    // self.model.id = response.data.itemId;
                    // console.log("id", self.model.id)
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
        }


        var GetCategoryMappingList = function (store) {
            if (store.id == undefined) {
                store.id = 0;
            }
            var categoryMapping = self.model;
            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.PRODUCT_CATEGORY_MAPPING.PRODUCT_CATEGORY_MAPPING_API+$scope.organizationId+"/" + store.id;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.categoryMappingList = [response.data];
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
        }

        //Method to Populate Store Dropdown
        var PopulateStore = function () {
            var categoryMapping = self.model;

            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;;

            categoryMapping.store = categoryMapping.Stores[0];

            categoryMapping.Stores.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    categoryMapping.Stores = categoryMapping.Stores.concat(response.data);
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
        }

        var PopulateAssetType = function () {
            var itemRestriction = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_TYPE_API + "/"+$scope.unitId + "/"+$scope.organizationId;

            itemRestriction.assetType = itemRestriction.AssetTypes[0];
            // itemRestriction.assetCategory = itemRestriction.AssetCategories[0];
            // itemRestriction.productCategory = itemRestriction.ProductCategories[0];

            itemRestriction.AssetTypes.splice(1);
            // itemRestriction.AssetCategories.splice(1);
            // itemRestriction.ProductCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    itemRestriction.AssetTypes = itemRestriction.AssetTypes.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var PopulateAssetCategory = function (assetType) {
            if (assetType.id == undefined) {
                assetType.id = 0;
            }
            var itemRestriction = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + assetType.id + "/" + $scope.unitId + "/"+ $scope.organizationId;

            itemRestriction.assetCategory = itemRestriction.AssetCategories[0];
            itemRestriction.productCategory = itemRestriction.ProductCategories[0];

            itemRestriction.AssetCategories.splice(1);
            itemRestriction.ProductCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    itemRestriction.AssetCategories = itemRestriction.AssetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var PopulateProductCategory = function (assetCategory) {
            if (assetCategory.id == undefined) {
                assetCategory.id = 0;
            }
            var itemRestriction = self.model;

            itemRestriction.productCategory = itemRestriction.ProductCategories[0];

            itemRestriction.ProductCategories.splice(1);

            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + assetCategory.id + '/'+$scope.organizationId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    itemRestriction.ProductCategories = itemRestriction.ProductCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }


        var validPopup = function () {
            var isValid = true;
            if (!self.model.assetType.id) {
                growl.error("Select Asset Type");
                isValid = false;
            }
            return isValid;
        };
        function searchNewItem() {
            if (validPopup()) {
                if($scope.itemName){
                    if($scope.itemName.length >= 3){         
                        var URI = CONSTANTS.GLOBAL.ITEM_API_FOR_MASTER + "org/"+$scope.organizationId;
                        const data = {
                            "assetTypeId": self.model.assetType.id ? self.model.assetType.id : undefined,
                            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                            "storeId": self.model.store.id,
                            "itemName": $scope.itemName,
                            "itemCode": $scope.itemCode
                        }

                        self.model.searchNewItemList = [];
                        $rootScope.startLoader();
                        GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            $rootScope.stopLoader();
                            self.model.searchNewItemList = response.data;
                            self.model.selectAllRows = false;
                            self.model.isItemChecked = false;
                        },
                        function errorCallback(response) {
                            $rootScope.stopLoader();
                            growl.error('Error');
                        });
                    }
                }
                else if($scope.itemCode){
                    if($scope.itemCode.length >= 3){
                         var URI = CONSTANTS.GLOBAL.ITEM_API_FOR_MASTER + "org/"+$scope.organizationId;
                        const data = {
                            "assetTypeId": self.model.assetType.id ? self.model.assetType.id : undefined,
                            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                            "storeId": self.model.store.id,
                            "itemName": $scope.itemName,
                            "itemCode": $scope.itemCode
                        }

                        self.model.searchNewItemList = [];
                        $rootScope.startLoader();
                        GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            $rootScope.stopLoader();
                            self.model.searchNewItemList = response.data;
                            self.model.selectAllRows = false;
                            self.model.isItemChecked = false;
                        },
                        function errorCallback(response) {
                            $rootScope.stopLoader();
                            growl.error('Error');
                        });
                    }
                }
                else{
                    self.model.searchNewItemList = [];
                }
            }
        }

        function GetItemList() {

            self.model.action = 'Open';
            var popup = angular.element('#addNewItemWasteStore');
            popup.modal('show');
            self.model.itemSelectionList = [];
            self.model.SelectedItemsIds = [];
            self.model.itemCode = "";
            self.model.itemName = "";
            self.model.isItemChecked = false;
            self.model.selectAllRows = false;
            self.model.assetType = self.model.AssetTypes[0];
            self.model.assetCategory = [self.model.AssetCategories[0]];
            self.model.assetCategory = self.model.AssetCategories[0];
            self.model.productCategory = [self.model.ProductCategories[0]];
            self.model.productCategory = self.model.ProductCategories[0];

            self.model.AssetCategories.splice(1);
            self.model.ProductCategories.splice(1);
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.searchNewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var SelectItem = function (item) {
            var itemIds = self.model.SelectedItemsIds;

            if (item.isSelected) {
                itemIds.push(item);
            } else {
                var index = itemIds.indexOf(item);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.searchNewItemList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }

            self.model.isItemChecked = false;
            self.model.searchNewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var isSelectedItem = function (item) {
            return item.isSelectedItem;

        }

        var AddItems = function () {
            self.model.selectAllRows = false;
            self.model.itemSelectionList = self.model.SelectedItemsIds;
            self.model.itemSelectionList.forEach(function (item) {
                var isFound = self.model.itemRestrictionList.find(function (x) {
                    return x.itemId == item.itemId;
                    var popup = angular.element('#addNewItemWasteStore');
                    popup.modal('hide');
                });

                if (!isFound) {
                    self.model.itemRestrictionList.push(item);
                    self.model.itemId = item.itemId;
                    self.model.assetCategory = item.assetCategory;
                    self.model.productCategory = item.productCategory;
                    self.model.itemCode = item.itemCode;
                    self.model.itemName = item.itemName;

                } else {
                    growl.error("Items are alredy exists");
                }
            });
            var itemIdArray = [];
            angular.forEach(self.model.itemSelectionList, function (x) {
                itemIdArray.push(x.itemId)
            });
            self.model.itemIdArray = itemIdArray;
            SaveItem();

        }

        var ToggleStatus = function (itemRestrictionMaster) {
            self.model.productCategoryId = itemRestrictionMaster.productCategoryId;
            self.model.StoreId = itemRestrictionMaster.storeId;
            self.model.id = itemRestrictionMaster.id;
            if (itemRestrictionMaster.status == true) {
                SaveItem(itemRestrictionMaster);
            } else {
                UpdateItem(itemRestrictionMaster);
            }
        }


        var SaveItem = function (itemRestrictionMaster) {
            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.ITEM_RESTRICTION.SAVE_API;

            var data = {
                "storeId": self.model.store.id,
                "itemsList": self.model.itemIdArray,
                "status": true,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            }

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    GetItemRestriction(self.model.store);
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                    ClearFields();
                    self.model.itemIdArray = [];
                    self.model.itemSelectionList = [];
                    self.model.itemId = [];

                    var popup = angular.element('#addNewItemWasteStore');
                    popup.modal('hide');
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Something went wrong');
                });
            self.model.selectAllRows = false;

        }


        var UpdateItem = function (itemRestrictionMaster) {
            var URI = CONSTANTS.MASTER.UNIT.PHARAMACY.ITEM_RESTRICTION.ITEM_RESTRICTION_API + itemRestrictionMaster.id;

            var data = {
                "storeId": itemRestrictionMaster.storeId,
                "itemId": itemRestrictionMaster.itemId,
                "status": itemRestrictionMaster.status,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId
            }

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    GetItemRestriction(self.model.store);

                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    itemRestrictionMaster.status = !itemRestrictionMaster.status;
                    growl.error('Something went wrong');
                });
            ClearFields();
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

        var SetOldValueStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeFromStore = function (store) {
            if (!!self.model.itemRestrictionList.length) {
                self.model.store = self.model.oldStore;
                self.model.storeNew = JSON.parse(JSON.stringify(store));
                OpenPopup('Open', 'confirmStore');
            }
            else {
                GetItemRestriction(store);
            }
        }

        var ConfirmStorePopup = function (storeData) {
            self.model.store = self.model.storeNew;
            ClosePopup('close', 'confirmStore');
            GetItemRestriction(self.model.store);
            //self.model.storeGenericList = [];
        }

        //Method to clear the fileds
        function ClearFields() {
            self.model.pagination.search = '';
            self.model.searchNewItemList = "";
            self.model.itemIdArray = [];
            self.model.itemSelectionList = [];
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("productRestriction.itemRestriction", {
            url: "/itemRestriction",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/item-restriction.html',
            controller: "ItemRestriction.Controller",
            controllerAs: "vm",
            title: "Product Restriction"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("ItemRestriction.Controller", itemRestrictionController)
})();