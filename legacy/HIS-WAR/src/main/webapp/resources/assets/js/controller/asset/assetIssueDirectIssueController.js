(function () {
    "use strict";

    function assetIssueDirectIssueController($scope, $rootScope, $http, $filter, commonDetailService, $state, $cookies, CONSTANTS, StatusService, GenericService, growl) {
        var self = this;
        var currentItem, savedAccessDetails;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){
        	$state.go('login'); 
        	return;
        	}
        
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.storeId = cookieObject.storeId; 
        
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                fromStoreList: [{
                    "store": "Select From Store"
                }],
                toStoreList: [{
                    "store": "Select To Store"
                }],
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
                accessoryList: [],
                ItemDetailList:[]
            };

            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            self.model.yearList = [];
            self.model.openAccessory = openAccessory;
            self.model.GetItemList = GetItemList;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.SearchNewItem = SearchNewItem;
            self.model.NewItemList = [];
            self.model.SelectItem = SelectItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.AddItems = AddItems;
            self.model.isPopup = false;
            self.model.OpenPopup = OpenPopup;
            self.model.ViewAccessoryDetails = ViewAccessoryDetails;
            self.model.RemoveRowData = RemoveRowData;
            self.model.SaveNewfixedAssetMaster = SaveNewfixedAssetMaster;
            self.model.GetAssetIssueListDetails = GetAssetIssueListDetails;
            self.model.DirectIssueId = commonDetailService.getDataId();
            PopulateDropdown();

        }

        var PopulateDropdown = function () {
            var URI = CONSTANTS.ASSET.ASEET_ISSUE.ISSUELIST_DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.fromStore = self.model.fromStoreList[0];
            self.model.toStore = self.model.toStoreList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.fromStoreList = self.model.fromStoreList.concat(response.data.fromStore);
                    self.model.toStoreList = self.model.toStoreList.concat(response.data.toStore);

                    $rootScope.stopLoader();
                    GetAssetIssueListDetails(commonDetailService.getDataId());
                },
                function (err) {
                    $rootScope.stopLoader();
                });

        }

        var openAccessory = function () {
            $state.go('fixedAssetAddAcessory');
            commonDetailService.setDataId(self.model.store.id);
        }

        var ValidateToGetItems = function () {
            var isValid = true;

            // if (!self.model.store.id) {
            //     self.model.isStoreValid = true;
            //     isValid = false;
            //     growl.error('Select store')
            // }

            return isValid;
        }

        var GetItemList = function () {
            if (ValidateToGetItems()) {
                GetAssetCategoryList();
                self.model.NewItemList = [];
                self.model.selectAllRows = false,
                // self.model.itemSearch.itemCode = undefined;
                // self.model.itemSearch.itemName = undefined;
                // self.model.itemSearch.batchNo = undefined;
                self.model.action = 'Open';
                var popup = angular.element('#addNewItemWasteStore');
                popup.modal('show');
            }
        }

        var GetAssetCategoryList = function () {
            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

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

        var GetProductCategoryList = function (data) {

            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + data.id + '/'+$scope.organizationId;
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

        var SearchNewItem = function () {

            // var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
            var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

            var data = {
                "assetTypeId": 2,
                "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            }
            self.model.NewItemList = [];
            self.model.SelectedItemsIds = [];
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

        var AddItems = function () {
            // var URI = constants.apiurl + '/api/common/items/details'; 
            var URI = CONSTANTS.GLOBAL.BATCH_ITEM_API;

            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.fromStore.id, // todo
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId

            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    response.data.forEach(function (item) {
                        var isFound = self.model.ItemDetailList.find(function (x) {
                            return x.itemId == item.itemId;
                        });
                        if (!isFound) {
                            self.model.ItemDetailList.push(item);
                        }
                    });

                    self.model.SelectedItemsIds = [];
                    self.model.items = [];
                    $rootScope.stopLoader();
                    self.model.searchNewItemList = [];
                    var popup = angular.element('#addNewItemWasteStore');
                    popup.modal('hide');
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var OpenPopup = function (modal, item) {
            // self.model.mainSelectedItem = item;
            // self.model.accessoryList = item.accessoryList;
            var popup = angular.element('#' + modal);
            popup.modal('show');

        }

        var ViewAccessoryDetails = function (item) {
            self.model.ItemDetailList.find(function (x) {
                if (x.itemId == item.itemId) {
                    self.model.itemForAccessoryDetail = item.accessories;
                }
            });
        }

        var RemoveRowData = function (index) {
            var ItemDetailList = self.model.ItemDetailList;
            self.model.ItemDetailList = ItemDetailList.slice(0, index).concat(ItemDetailList.slice(index + 1))
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var SaveNewfixedAssetMaster = function () {
            if (Validate()) {
                var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.SAVE_API;

                var data = {
                    "fromStoreId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "toStoreId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": setStatus('Completed'),
                    "carrierId": parseInt(self.model.id),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "item": []
                };

                angular.forEach(asset.ItemDetailList.item, function (item, key) {
                    if (item.issueQuantity > 0) {
                        var assetItem = {
                            "itemId": item.id,
                            "issueQuantity": item.issueQuantity,
                            "batches": []
                        }

                        angular.forEach(item.batches, function (batch) {
                            var batch = {
                                "batchId": batch.batchId,
                                "issueQuantity": batch.issueQuantity
                            }
                            assetItem.batches.push(batch);
                        });

                        data.item.push(assetItem);
                    }
                });

                if (data.item.length > 0) {
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                        function (response) {
                            // ClearRequests();
                            $rootScope.stopLoader();
                            NavigteToAssetIssueMaster();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error('Something Went Wrong');
                        });
                }
                else {
                    $rootScope.stopLoader();
                    NavigteToAssetIssueMaster();
                }
            }
        }

        var Validate = function () {
            var isValid = true;

            var ItemDetailList = self.model.ItemDetailList;
            if (!ItemDetailList || ItemDetailList.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                ItemDetailList.forEach(function (item) {

                    if (!item.serialNo || !item.yearOfMAnufacture || !item.vendor || !item.serviceProvider || !item.warrantyFrom || !item.warrantyTo || !item.quantity || !item.pmsfrequencies || !item.workingCondition || !item.specification) {
                        isValid = false;
                        growl.error('Please fill all the fields')
                    }

                    if (!item.serialNo) {
                        item['isSerialNoValid'] = true;
                        isValid = false;
                    }

                    if (!item.yearOfMAnufacture) {
                        item['isYearOfMAnufactureValid'] = true;
                        isValid = false;
                    }

                    if (!item.vendor) {
                        item['isVendorValid'] = true;
                        isValid = false;
                    }

                    if (!item.serviceProvider) {
                        item['isServiceProviderValid'] = true;
                        isValid = false;
                    }

                    if (!item.warrantyFrom) {
                        item['isWarrantyFromValid'] = true;
                        isValid = false;
                    }

                    if (!item.warrantyTo) {
                        item['isWarrantyToValid'] = true;
                        isValid = false;
                    }

                    if (!item.quantity) {
                        item['isQuantityValid'] = true;
                        isValid = false;
                    }

                    if (!item.pmsfrequencies) {
                        item['isPmsfrequenciesValid'] = true;
                        isValid = false;
                    }

                    if (!item.workingCondition) {
                        item['isWorkingConditionValid'] = true;
                        isValid = false;
                    }

                    if (!item.specification) {
                        item['isSpecificationValid'] = true;
                        isValid = false;
                    }

                });
            }
            return isValid;
        }

        var GetAssetIssueListDetails = function (DirectIssueId) {
            var URI = CONSTANTS.ASSET.ASEET_ISSUE.ISSUELIST_DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + DirectIssueId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.ItemDetailList = response.data.items;
                    // self.model.accessoryList = response.data.items.accesories;
                    response.data.items.forEach(function (item) {
                        self.model.accessoryList = item.accesories;
                    });

                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assetIssueDirectIssue", {
            url: "/assetIssueDirectIssue",
            templateUrl: 'views/master/UnitMaster/assetManagement/asset-issue-direct-issue.html',
            controller: "AssetIssueDirectIssue.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AssetIssueDirectIssue.Controller", assetIssueDirectIssueController);
})();