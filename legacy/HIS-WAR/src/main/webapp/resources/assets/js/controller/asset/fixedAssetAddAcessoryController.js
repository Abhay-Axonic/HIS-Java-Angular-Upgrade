(function () {
    "use strict";

    function fixedAssetAddAcessoryController($scope, $rootScope, $http,$state, commonDetailService, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                SelectedItemsIds: [],
                storeIndentData: [],
                selectAllRows: false,
                SelectedItemIds: [],
                warrantyFrom: new Date(),
                warrantyTo: new Date(),
                warrantyFromOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                },
                warrantyToOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                },

                warrantyFromDateOpen: [],
                warrantyToDateOpen: [],

            };
            self.model.accessories =[];
            self.model.openAddItem = openAddItem;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.searchNewItem = searchNewItem;
            self.model.AddItems = AddItems;
            self.model.OpenWarrantyFrom = OpenWarrantyFrom;
            self.model.OpenWarrantyTo = OpenWarrantyTo;
            self.model.onSaveClick = onSaveClick
        }
        var OpenWarrantyFrom = function (idx) {
            self.model.warrantyFromDateOpen[idx] = true;
        }

        var OpenWarrantyTo = function (idx) {
            self.model.warrantyToDateOpen[idx] = true;
        }
        var openAddItem = function () {
            $("#addNewItemWasteStore").modal();
            GetAssetCategoryList();
        }

        var GetAssetCategoryList = function () {
            var asset = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            asset.assetCategory = asset.assetCategories[0];
            asset.productCategory = asset.productCategories[0];

            // asset.assetCategories.splice(1);
            // asset.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    asset.assetCategories = asset.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var asset = self.model;

            //TODO : Add asset category to the uri
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    asset.productCategories = asset.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function searchNewItem() {

            var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

            const data = {
                "assetTypeId": 1,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                "storeId": commonDetailService.getDataId()
            }
            self.model.searchNewItemList = [];
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.searchNewItemList = response.data;
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }


        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;

                    return item.itemId;
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
                itemIds.push(item.itemId);
            } else {
                var index = itemIds.indexOf(item.itemId);
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


        var AddItems = function () {
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": commonDetailService.getDataId()
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.itemDetailList = response.data;
                    
                    $rootScope.stopLoader();
                    // getAllItemDetails();
                    self.model.searchNewItemList = [];
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }
        var isSelected = function (item) {
            return item.isSelected;
        }

        var onSaveClick = function () {

            angular.forEach(self.model.itemDetailList, function (item, key) {
                var storeItem = {
                    "itemId": item.itemId,
                    "serialNumber": item.itemId,
                    "warrentyFrom": item.warrantyFrom,
                    "warrentyTo": item.warrantyTo,
                    "specification": item.specification,
                    "quantity": item.quantity,
                    "workingConditionId": 1
                }
                self.model.accessories.push(storeItem);

            });
            commonDetailService.setDataId(self.model.accessories);
            $state.go('addNewfixedAssetMaster');

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("fixedAssetAddAcessory", {
            url: "/fixedAssetAddAcessory",
            templateUrl: 'views/master/UnitMaster/assetManagement/fixed-asset-add-accessory.html',
            controller: "FixedAssetAddAcessory.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("FixedAssetAddAcessory.Controller", fixedAssetAddAcessoryController);
})();