(function () {
    "use strict";

    function addNewAssetIndentController($scope, $rootScope, $http, $state, CONSTANTS, commonDetailService, GenericService, growl,$cookies) {
        var self = this;

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
            var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            $scope.storeId = cookieObjectStore != null ? cookieObjectStore.storeId : undefined;
            self.model = {
                fromStores: [{
                    "store": "Select Store"
                }],
                toStores: [{
                    "store": "Select Store"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                dateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false
                },
                dateOpen: [],
                SelectedItemsIds: [],
                selectAllRows: false,
                NewItemList: [],
                itemList: []


            };
            self.model.ItemDetailList = [];
            self.model.accessoryList = [];

            self.model.IsMainStore = IsMainStore;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.getItemList = getItemList;
            self.model.searchNewItem = searchNewItem;
            self.model.SelectItem = SelectItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.AddItems = AddItems;
            self.model.openAddItemPopup = openAddItemPopup;
            self.model.AddItemsPopup = AddItemsPopup;
            self.model.OpenPopup = OpenPopup;
            self.model.AddAccessoryDetails = AddAccessoryDetails;
            self.model.SaveNewAssetIndent = SaveNewAssetIndent;
            self.model.SaveAccessoryDetails = SaveAccessoryDetails;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateSpecification = ValidateSpecification;
            self.model.ValidateQuantityPopup = ValidateQuantityPopup;
            self.model.ValidateSpecificationPopup = ValidateSpecificationPopup;
            self.model.removeRowData = removeRowData;
            self.model.removeRowDataPopup = removeRowDataPopup;
            self.model.openViewModal = openViewModal;
            self.model.SelectItemPopup = SelectItemPopup;
            self.model.SelectAllItemsPopup = SelectAllItemsPopup;
            self.model.validateAccessoryPopup = validateAccessoryPopup;

            self.model.SetOldValueFromStore = SetOldValueFromStore;
            self.model.ConfirmFromStorePopup = ConfirmFromStorePopup;
            self.model.ChangeFromStore = ChangeFromStore;
            self.model.SetOldValueToStore = SetOldValueToStore;
            self.model.ConfirmToStorePopup = ConfirmToStorePopup;
            self.model.ChangeToStore = ChangeToStore;
            self.model.ModalDismiss = ModalDismiss;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;

            PopulateDropdown();
        }

        var IsMainStore = function (isOn) {
            var asset = self.model;

            if (isOn) {
                asset.toStores = asset.toStores.concat($filter('filter')(asset.allStores, {
                    isMainStore: true
                }, true));
            } else {
                asset.toStores = asset.toStores.concat(asset.allStores);
            }
        }
        $scope.indentTypeIdt=1;
        var PopulateDropdown = function () {
            var assetIndent = self.model;
            assetIndent.fromStore = assetIndent.fromStores[0];
            assetIndent.toStore = assetIndent.toStores[0];
            var URI = CONSTANTS.ASSET.ASSET_INDENT.DROPDOWN_API +"/"+ $scope.unitId +"/"+ $scope.organizationId+"/"+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    assetIndent.fromStores = assetIndent.fromStores.concat(response.data.fromStore);
                    assetIndent.allStores = response.data.toStore;
                    assetIndent.toStores = assetIndent.toStores.concat(response.data.toStore);
                    assetIndent.priorities = response.data.priority;
                    assetIndent.priority = assetIndent.priorities[1];
                    $scope.indentTypeIdt= response.data.indentType[0].id;
                    self.model.fromStore = self.model.fromStores.find(function (store) {
                        return $scope.storeId == store.id;
                    });
                    $rootScope.stopLoader();
                    var indentId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!indentId) {
                        commonDetailService.setDataId(null);
                        GetAssetIndent(indentId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var GetAssetIndent = function (indentId) {

            var URI = CONSTANTS.ASSET.ASSET_INDENT.DETAIL_API + $scope.unitId+"/"+ $scope.organizationId +"/"+ indentId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.indentId = response.data.id;

                    self.model.fromStore = self.model.fromStores.find(function (item) {
                        return response.data.fromStoreId == item.id;
                    });

                    self.model.toStore = self.model.toStores.find(function (item) {
                        return response.data.toStoreId == item.id;
                    });

                    self.model.priority = self.model.priorities.find(function (item) {
                        return response.data.priorityId == item.id;
                    });


                    self.model.remark = response.data.remark;

                    self.model.itemDetails = [];


                    self.model.ItemDetailList = response.data.items;

                    self.model.ItemDetailList.forEach(function (item) {
                        item.indentQuantity = item.quantity;
                    });

                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var GetAssetCategoryList = function () {
            var assets = self.model;
            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

            assets.assetCategory = assets.assetCategories[0];
            assets.productCategory = assets.productCategories[0];

            assets.assetCategories.splice(1);
            assets.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    assets.assetCategories = assets.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var assets = self.model;
            assets.productCategories.splice(1);
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    assets.productCategories = assets.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function getItemList() {
            if (self.model.fromStore.id == self.model.toStore.id) {
                growl.error('Please Select Different Requesting Store & Issuing Store')
            }
            else {
                self.model.action = 'Open';
                var popup = angular.element('#addNewItemWasteStore');
                popup.modal('show');
                self.model.searchNewItemList = [];
                self.model.isItemChecked = false;
                self.model.search = [];
                GetAssetCategoryList();
            }
        }

        function searchNewItem() {
            if(self.model.search.itemName){
                if(self.model.search.itemName.length  >= 3){
                    //var URI = CONSTANTS.GLOBAL.ITEM_API_ASSET_INDENT;
                    var URI = CONSTANTS.GLOBAL.STORE_INDENT_ITEM_API;

                    const data = {
                        "assetTypeId": 2,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                        "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId

                    }
                    // self.model.searchNewItemList = [];
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                    // $rootScope.stopLoader();
                        self.model.searchNewItemList = response.data;
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        // self.model.SelectedItemsIds = []
                        $rootScope.stopLoaderPopup();
                        console.log("Loader Stopped");
                        console.log("Data : ",self.model.searchNewItemList);
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }
            }
            else if(self.model.search.itemCode){
                if(self.model.search.itemCode.length >= 3){
                    var URI = CONSTANTS.GLOBAL.STORE_INDENT_ITEM_API;

                    const data = {
                        "assetTypeId": 2,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                        "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId

                    }
                    // self.model.searchNewItemList = [];
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                    // $rootScope.stopLoader();
                        self.model.searchNewItemList = response.data;
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        // self.model.SelectedItemsIds = []
                        $rootScope.stopLoaderPopup();
                        console.log("Loader Stopped");
                        console.log("Data : ",self.model.searchNewItemList);
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }
            }
            else{
                self.model.searchNewItemList = [];
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
        var SelectItemPopup = function (item) {
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
                self.model.selectAllRowsPopup = true;
            } else {
                self.model.selectAllRowsPopup = false;
            }
            self.model.isItemCheckedPopup = false;
            self.model.searchNewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemCheckedPopup = true;
                    return;
                }
            });
        }
        var isSelected = function (item) {
            return item.isSelected;
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
        var SelectAllItemsPopup = function () {
            if (self.model.selectAllRowsPopup) {
                self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemCheckedPopup = true;

                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.searchNewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemCheckedPopup = false;

                });
            }
        }

        var AddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.fromStore.id,
                "toStoreId":self.model.toStore.id
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    response.data.forEach(function (item) {

                        var isFound = self.model.ItemDetailList.find(function (x) {
                            return x.itemId == item.itemId;
                        });
                        if (!isFound) {
                            item["accessories"] = [];
                            self.model.ItemDetailList.push(item);
                        }
                    });
                    self.model.SelectedItemsIds = [];
                    self.model.items = [];
                    $rootScope.stopLoader();
                    self.model.searchNewItemList = [];
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var openAddItemPopup = function () {
            $("#addNewItemWasteStore1").modal();
            self.model.SelectedItemsIds = [];
            self.model.searchNewItemList = [];
            GetAssetCategoryList();
            self.model.isPopup = true;
        }

        var AddItemsPopup = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.fromStore.id
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    response.data.forEach(function (item) {
                        var isFound = self.model.accessoryList.find(function (x) {
                            return x.itemId == item.itemId;
                        });
                        if (!isFound) {
                            self.model.accessoryList.push(item);
                        }
                    });
                    $rootScope.stopLoader();
                    self.model.searchNewItemList = [];
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var OpenPopup = function (modal) {
            var popup = angular.element('#' + modal);
            popup.modal('show');

        }

        var AddAccessoryDetails = function (item) {
            self.model.currentItem = item;
            if (self.model.indentId) {
                self.model.accessoryList = [];
                self.model.accessoryList = item.accessories;
            }
        }

        var SaveAccessoryDetails = function () {
            if (validateAccessory()) {
                self.model.ItemDetailList.forEach(function (obj) {
                    if (obj.itemId == self.model.currentItem.itemId) {
                        self.model.accessoryList.forEach(function (item) {

                            obj.accessories.push({
                                "itemId": item.itemId,
                                "itemCode": item.itemCode,
                                "itemName": item.itemName,
                                "specification": item.specification,
                                "quantity": item.quantity
                            })
                        });
                    }
                });

                var popup = angular.element('#add_accessories_modal');
                popup.modal('hide');
            }
        }
        var validateAccessory = function () {
            var isValid = true;
            self.model.accessoryList.forEach(function (item) {

                if (!validateAccessoryPopup(item)) {
                    isValid = false;
                }
                if (!validateAccessorySpecification(item)) {
                    isValid = false;
                }
            });
            return isValid;

        }
        var validateAccessorySpecification = function (item) {
            var isValid = true;
            if (!item.specification) {
                item.isSpecificationValid = true;
                isValid = false;
                growl.error("Please Enter Specification");
            }
            return isValid;
        }
        var validateAccessoryPopup = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.quantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.quantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }
            else if (parseInt(item.quantity) > item.newMaxOrderQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max order quantity');
            } else {
                item.quantity = parseInt(item.quantity);
                isValid = true;
            }

            return isValid;

        }
        function validateSave() {
            var isValid = true;
            var ItemDetailList = self.model.ItemDetailList;
            // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 

            if (ItemDetailList.length < 1) {
                growl.error("Please Add Items To Save");
                isValid = false;
            }
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateQuantity(ItemDetail)) {
                    isValid = false;
                }
//                if (!ValidateSpecification(ItemDetail)) {
//                    isValid = false;
//                }

            }

            return isValid;
        }
        var SaveNewAssetIndent = function () {
            if (validateSave()) {
                if (!self.model.indentId) {
                    SaveAssetIndent();
                } else {
                    UpdateAssetIndent();
                }
            }
        }
        var SaveAssetIndent = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

            var URI = CONSTANTS.ASSET.ASSET_INDENT.SAVE_API;

            var data = {
                "fromStoreId": self.model.fromStore.id,
                "toStoreId": self.model.toStore.id,
                "priorityId": self.model.priority.id,
                "indentTypeId":  $scope.indentTypeIdt,
                "remark": self.model.remark,
                "statusId": 1,
                "approvalStatusId": 1,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "storeId":$scope.storeId,
                "items": []
            }

            angular.forEach(self.model.ItemDetailList, function (item, key) {

                var itemAccessories = [];
                item.accessories.forEach(function (element) {
                    itemAccessories.push({
                        "itemId": element.itemId,
                        "specification": element.specification,
                        "quantity": parseInt(element.quantity)
                    });
                });

                var newdata = {
                    "itemId": item.itemId,
                    "quantity": parseInt(item.indentQuantity),
                    "statusId": 1,
                    "specification": item.specification,
                    "accessories": itemAccessories,
                }

                data.items.push(newdata);
            });

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                    NavigateToBack();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Error while saving new Asset Indent ');
                });
        }
        var UpdateAssetIndent = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var indent = self.model;
            var URI = CONSTANTS.ASSET.ASSET_INDENT.UPDATE_API + indent.indentId;
            var data = {
                "priorityId": self.model.priority.id,
                "remark": self.model.remark,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "storeId":$scope.storeId,
                "items": []
            }


            angular.forEach(self.model.ItemDetailList, function (item, key) {

                var itemAccessories = [];
                item.accessories.forEach(function (element) {
                    itemAccessories.push({
                        "id": element.id,
                        "itemId": element.itemId,
                        "specification": element.specification,
                        "quantity": parseInt(element.quantity)
                    });
                });

                var newdata = {
                    "id": item.id,
                    "itemId": item.itemId,
                    "quantity": parseInt(item.indentQuantity),
                    "statusId": 1,
                    "specification": item.specification,
                    "accessories": itemAccessories,
                }

                data.items.push(newdata);
            });

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {

                    $rootScope.stopLoader();
                    NavigateToBack();
                    growl.success(response.data.message);
                },

                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var NavigateToBack = function () {
            $state.go('assetIndent');
        }


        var ValidateQuantity = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.indentQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.indentQuantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }
            else if (parseInt(item.indentQuantity) > item.maxOrderQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max order quantity');
            } else {
                item.indentQuantity = parseInt(item.indentQuantity);
                isValid = true;
            }

            return isValid;
        }

        var ValidateSpecification = function (item) {
            var isValid = true;

            if (!item.specification) {
                item.isSpecificationValid = true;
                isValid = false;
                growl.error("Please Enter Specification");
            }
            return isValid;
        }

        var ValidateQuantityPopup = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.quantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.quantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }
            else if (parseInt(item.quantity) > item.maxOrderQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the max order quantity');
            } else {
                item.quantity = parseInt(item.quantity);
                isValid = true;
            }

            return isValid;
        }



        var ValidateSpecificationPopup = function (item) {
            var isValid = true;

            if (!item.specification) {
                item.isSpecificationValid = true;
                isValid = false;
                growl.error("Please Enter Specification");
            }
            return isValid;
        }

        function removeRowDataPopup(item) {
            var index = self.model.accessoryList.indexOf(item);
            self.model.accessoryList.splice(index, 1);
        }
        function removeRowData(item) {
            var index = self.model.ItemDetailList.indexOf(item);
            self.model.ItemDetailList.splice(index, 1);
        }


        var openViewModal = function (item) {
            self.model.viewlist = [];
            self.model.viewlist = item.accessories;
            $("#view_accessories_modal").modal();
        }

        var SetOldValueFromStore = function (fromStore) {
            self.model.oldFromStore = fromStore;
            self.model.fromStore = self.model.oldFromStore;
        }

        var ChangeFromStore = function (fromStore) {
            if (!!self.model.ItemDetailList.length) {
                self.model.fromStore = self.model.oldFromStore;
                self.model.fromStoreNew = JSON.parse(JSON.stringify(fromStore));
                $("#storeChange").modal()
            }
        }

        var ConfirmFromStorePopup = function () {
            self.model.fromStore = self.model.fromStoreNew;
            self.model.ItemDetailList = [];
            ModalDismiss('storeChange');
        }


        var SetOldValueToStore = function (toStore) {
            self.model.oldToStore = toStore;
            self.model.toStore = self.model.oldToStore;
        }

        var ChangeToStore = function (toStore) {
            if (!!self.model.ItemDetailList.length) {
                self.model.toStore = self.model.oldToStore;
                self.model.toStoreNew = JSON.parse(JSON.stringify(toStore));
                $("#tostoreChange").modal()
            }
        }

        var ConfirmToStorePopup = function () {
            self.model.toStore = self.model.toStoreNew;
            self.model.ItemDetailList = [];
            ModalDismiss('tostoreChange');
        }


        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var ConfirmRemove = function (item) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
            self.model.itemForRemove = item;
        }

        var RemoveItem = function (item) {
            var newArray = [];

            self.model.ItemDetailList.map(function (obj) {
                if (obj !== item) {
                    newArray.push(obj);
                }
            });
            self.model.ItemDetailList = newArray;
            var popup = angular.element('#itemRemovePopup');
            popup.modal('hide');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewAssetIndent", {
            url: "/addNewAssetIndent",
            templateUrl: 'views/master/UnitMaster/assetManagement/add-new-asset-indent.html',
            controller: "AddNewAssetIndent.Controller",
            controllerAs: "vm",
            title: 'Asset Indent'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewAssetIndent.Controller", addNewAssetIndentController);
})();

