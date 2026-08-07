(function () {
    "use strict";

    function addNewAssetReturnController($scope, $rootScope, $http,  CONSTANTS, commonDetailService, $state, GenericService, growl,$cookies) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                fromStores: [{
                    "store": "Select Stores"
                }],
                toStores: [{
                    "store": "Select Stores"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                carriers: [{
                    "name": "Select Carrier "
                }],
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
                fromDate: new Date(),
                toDate: new Date(),
                SelectedItemsIds: [],
                selectAllRows: false,
                selectAllRows1: false,
                searchNewItemList: [],
                itemList: []
            };
            self.model.openAddItem = openAddItem;
            self.model.openAddIssue = openAddIssue;
            self.model.GetIssueList = GetIssueList;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectAllItems1 = SelectAllItems1;
            self.model.SelectItem = SelectItem;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.getAllIssueDetails = getAllIssueDetails;
            self.model.SaveAssetReturn = SaveAssetReturn;
            self.model.getReturnReasonList = getReturnReasonList;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateReason = ValidateReason;
            self.model.validateSave = validateSave;
            self.model.viewAcessory = viewAcessory;
            self.model.SaveAccessoryDetails = SaveAccessoryDetails;
            self.model.ConfirmRemove =ConfirmRemove;
            self.model.RemoveItem = RemoveItem;
            self.model.Expand = Expand;
            self.model.SelectItem1 = SelectItem1;
            self.model.SelectIndentItem = SelectIndentItem;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.GetIssueSearch = GetIssueSearch;

            getReturnReasonList();
            getDropdownsList();
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var GetIssueSearch = function () {
        	
            var URI = CONSTANTS.ASSET.ASSET_RETURN.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD')
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoader();
                for (var i = 0; i < response.data.length; i++) {
                    response.data[i]["isChecked"] = false;
                }
                self.model.assetReturnData = response.data;
                self.model.selectAllRows = false;
                self.model.isItemChecked = false;

            },
            function errorCallback(response) {
                $rootScope.stopLoader();
                growl.error('Error');
            });
        }

        var Expand = function (item) {
            item.Expanded = !item.Expanded;
        }

        var SelectItem1 = function (item, indent) {
            var itemIds = self.model.SelectedItemsIds;
            indent.forEach(function(data){
                if (item.isSelected) {
                    data.isSelectedItem = true;
                    itemIds.push(data.itemId);
                } else {
                    var index = itemIds.indexOf(data.itemId);
                    if (index > -1) {
                        itemIds.splice(index, 1);
                    }
                }
            });

            if (self.model.searchNewIssueList.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }

            self.model.isItemChecked = false;
            self.model.searchNewIssueList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var SelectIndentItem = function (item, indent) {
            var itemIds = self.model.SelectedItemsIds;
            if (item.isSelectedItem) {
                item.isSelectedItem = true;
                itemIds.push(item.itemId);
            } else {
                item.isSelectedItem = false;
                var index = itemIds.indexOf(item.isSelectedItem);
                if (index > -1) {
                itemIds.splice(index, 1);
                }

                // // var itemIndex = self.model.indentItemDetails.indexOf(item);
                // if (itemIndex > -1) {
                // self.model.indentItemDetails.splice(itemIndex, 1);
                // }
            }
            if (indent.itemResponse.every(isSelectedItem)) {
                indent.isSelected = true;
            } else {
                indent.isSelected = false;
            }
            self.model.isItemChecked = false;
            self.model.searchNewIssueList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelectedItem = function (item) {
            return item.isSelectedItem;
        }

        var getDropdownsList = function () {
            var areturn = self.model;
           // areturn.fromStore = areturn.fromStores[0];
            areturn.toStore = areturn.toStores[0];
            areturn.carrier = areturn.carriers[0];

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.ADD_NEW_DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    areturn.fromStores = areturn.fromStores.concat(response.data.fromStore);
                    areturn.toStores = areturn.toStores.concat(response.data.toStore);
                    areturn.carriers = areturn.carriers.concat(response.data.carrier);
                    var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
                    for(var i=0;i<areturn.fromStores.length;i++)
                    {
                        if(areturn.fromStores[i].id==(selectedTopStore != null ? selectedTopStore.storeId : undefined))
                        {
                            areturn.fromStore = areturn.fromStores[i];
                        }
                    }
                    $rootScope.stopLoader();
                    var areturnId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!areturnId) {
                        commonDetailService.setDataId(null);
                        GetAssetReturnDetails(areturnId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var GetAssetReturnDetails = function (areturnId) {
        	
            var URI = CONSTANTS.ASSET.ASSET_RETURN.DETAIL_API + $scope.unitId + '/'+$scope.organizationId + '/' + areturnId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.areturnId = response.data.id;
                    self.model.remark = response.data.remark;
                    self.model.itemList = [];
                    self.model.itemList = response.data.item;

                    self.model.fromStore = self.model.fromStores.find(function (areturnId) {
                        return response.data.fromStoreId == areturnId.id;
                    });
                    self.model.toStore = self.model.toStores.find(function (areturnId) {
                        return response.data.toStoreId == areturnId.id;
                    });

                    self.model.carrier = self.model.carriers.find(function (areturnId) {
                        return response.data.carrierId == areturnId.id;
                    });

                    angular.forEach(self.model.itemList, function (item, key) {
                        item.returnReason = self.model.returnReasons.find(function (areturnId) {
                            return areturnId.id == item.returnReasonId;
                        });
                    });


                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var GetAssetCategoryList = function () {
            var areturnId = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

            areturnId.assetCategory = areturnId.assetCategories[0];
            areturnId.productCategory = areturnId.productCategories[0];

            areturnId.assetCategories.splice(1);
            areturnId.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    areturnId.assetCategories = areturnId.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var areturnId = self.model;

            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            areturnId.productCategory = areturnId.productCategories[0];
            areturnId.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    areturnId.productCategories = areturnId.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var openAddItem = function () {
            if (self.model.fromStore.id == self.model.toStore.id) {
                growl.error('Select Different From & To Store');
            }
            self.model.search = '';
            self.model.searchNewItemList = [];
            self.model.isItemChecked = false;
            self.model.SelectedItemsIds = [];
            $("#addNewItemWasteStore").modal()
            GetAssetCategoryList();
        }

        var openAddIssue = function () {
            if (self.model.fromStore.id == self.model.toStore.id) {
                growl.error('Select Different From & To Store');
            }
            self.model.search = '';
            self.model.searchNewItemList = [];
            self.model.isItemChecked = false;
            self.model.SelectedItemsIds = [];
            $("#addNewIssue").modal()
            GetIssueList();
        }

        var GetIssueList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.ASSET.ASSET_RETURN.ISSUELIST_API;
            var data = {
                "fromStoreId": self.model.fromStore.id,
                "toStoreId": self.model.toStore.id,
                "unitId": $scope.unitId,
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD')
            }
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoaderPopup();
                self.model.searchNewIssueList = response.data;
                self.model.selectAllRows = false;
            },
            function errorCallback(response) {
                $rootScope.stopLoaderPopup();
                growl.error('Error');
            });
        }

        function searchNewItem() {
            if(self.model.search.itemName){
                if(self.model.search.itemName.length >= 3){
                    var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
                    
                    const data = {
                        "assetTypeId": 2,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.fromStore.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName
                    }

                    // self.model.searchNewItemList = [];
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.searchNewItemList = response.data;
                        self.model.selectAllRows = false;
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }
            }
            else if(self.model.search.itemCode){
                if(self.model.search.itemCode.length >= 3){
                    var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
                    
                    const data = {
                        "assetTypeId": 2,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.fromStore.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName
                    }

                    // self.model.searchNewItemList = [];
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.searchNewItemList = response.data;
                        self.model.selectAllRows = false;
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
                angular.forEach(item.itemResponse, function(data){
                    data.isSelectedItem = true;
                })
                itemIds.push(item.itemId);
            } else {
                var index = itemIds.indexOf(item.itemId);
                angular.forEach(item.itemResponse, function(data){
                    data.isSelectedItem = false;
                })
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

        function getAllItemDetails() {
        	 if(self.model.itemList == undefined){
                 self.model.itemList    = [];
         }
        	angular.forEach(self.model.searchNewItemList,function(item){
        		 if (item.isSelected == true) {
                     self.model.itemList.push(item);
                 }
        	});
            /*self.model.searchNewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.itemList.push(item);
                }
            });*/

        }

        function getAllIssueDetails() {
            $rootScope.startLoader();
            self.model.searchNewIssueList.forEach(function (item) {
                if (item.isSelected == true) {
                    item.itemResponse.forEach(function(data){
                        data.issueNo = item.issueNo;
                        data.issueDate = item.issueDate;
                        self.model.itemList.push(data);
                    });
                }
            });
            $rootScope.stopLoader();
        }

        var isSelected = function (item) {
            return item.isSelected;
        }
        var SaveAssetReturn = function () {
            if (validateSave()) {
                if (!self.model.areturnId) {
                    SaveNewAssetReturn();
                } else {
                    UpdateAssetReturn();
                }
            }
        }

        var SaveNewAssetReturn = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var store = self.model;
            var URI = CONSTANTS.ASSET.ASSET_RETURN.SAVE_API;

            var data = {
                "fromStoreId": self.model.fromStore.id,
                "toStoreId": self.model.toStore.id,
                "carrierId": self.model.carrier.id,
                "statusId": 1,
                "remark": self.model.remark,
                "approvalStatusId": 1,
                "userId":$scope.userId,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "storeId":$scope.storeId,
                "items": []
            };

            angular.forEach(store.itemList, function (item, key) {
                var storeItem = {
                    "issueDetailId": item.id,
                    "batchId": item.batchId,
                    "returnQuantity": parseInt(item.returnQuantity),
                    "returnReasonId": item.returnReason.id,
                    "statusId": 1,
                    "accessories": item.accessoriesList ? item.accessoriesList : [],
                    "issueBatchMapperId":item.batchMapperId
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
        var UpdateAssetReturn = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

            var areturnId = self.model;

            var URI = CONSTANTS.ASSET.ASSET_RETURN.UPDATE_API + areturnId.areturnId;
            var data = {
                "carrierId": self.model.carrier.id,
                "remark": self.model.remark,
                "userId":$scope.userId,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "storeId":$scope.storeId,
                "items": []
            };

            angular.forEach(self.model.itemList, function (item, key) {
                var storeItem = {
                    "id": item.id,
                    "batchId": item.batchId,
                    "returnQuantity": parseInt(item.returnQuantity),
                    "returnReasonId": item.returnReason.id,
                    "accessories": item.accessoriesList,
                    "issueBatchMapperId":item.batchMapperId
                }

                if (item.id == '' || item.id == null) {
                    storeItem.batchId = item.batchId,
                        storeItem.returnQuantity = item.returnQuantity,
                        storeItem.returnReasonId = item.returnReason.id,
                        storeItem.uomTypeId = item.uomTypeId,
                        storeItem.uomUnitId = item.uomUnitId,
                        storeItem.statusId = 1
                }
                data.items.push(storeItem);


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
            if (ItemDetailList.length < 1) {
                growl.error("Please validate all required fields");
                isValid = false;
            }
            if (self.model.fromStore.id == self.model.toStore.id) {
                growl.warning("Please Select Different Stores");
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

            if (!self.model.carrier.id) {
                growl.error("Please Select Carrier");
                isValid = false;
            }

            return isValid;
        }

        var ValidateQuantity = function (mreturn) {
            var isValid = true;

            if (isNaN(parseFloat(mreturn.returnQuantity))) {
                mreturn.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Return Quantity");
            } else if (parseInt(mreturn.returnQuantity) == 0) {
                mreturn.isQuantityValid = true;
                isValid = false;
                growl.error('Return Quantity cannot be zero');
            } else if(mreturn.issueNo){
                if (parseInt(mreturn.returnQuantity) > mreturn.acceptedQuantity) {
                    mreturn.isQuantityValid = true;
                    isValid = false;
                    growl.error('Entered  return quantity is exceeding the accepted quantity');
                }
            }
            else if(!mreturn.issueNo){
                if (parseInt(mreturn.returnQuantity) > mreturn.availableQuantity) {
                    mreturn.isQuantityValid = true;
                    isValid = false;
                    growl.error('Entered  return quantity is exceeding the stock available quantity');
                }
            } else {
                mreturn.returnQuantity = parseInt(mreturn.returnQuantity);
                isValid = true;
            }

            return isValid;
        }

        var ValidateReason = function (mreturn) {
            var isValid = true;

            if (!mreturn.returnReason) {
                mreturn.isReasonValid = true;
                isValid = false;
                growl.error("Please Select Reason");
            }
            return isValid;
        }
        var NavigteToBack = function () {
            $state.go('assetReturn');
        }
        var getReturnReasonList = function () {
            var areturnId = self.model;

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.RETURN_REASON_API + "/" + $scope.unitId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    areturnId.returnReasons = response.data.returnReason;
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var viewAcessory = function (batch) {
            self.model.accessoryList = [];
            self.model.accessoryList = batch.accessories;
            $("#viewAccessoriesDetailsModal").modal();
            self.model.currentBatch = batch;
        }
        var SaveAccessoryDetails = function () {
            if (ValidateAccesories()) {
                self.model.itemList.forEach(function (obj) {
                    obj.accessoriesList = [];
                    if (obj.batchId == self.model.currentBatch.batchId) {
                        self.model.accessoryList.forEach(function (item) {
                            obj.accessoriesList.push({
                                "batchId": item.batchId,
                                "returnQuantity": item.returnQuantity,
                                "returnReasonId": item.returnReason.id
                            })
                        });
                    }
                });
                self.model.accessoryList = [];

                var popup = angular.element('#viewAccessoriesDetailsModal');
                popup.modal('hide');
            }
        }
        var ValidateAccesories = function () {
            var isValid = true;
            self.model.accessoryList.forEach(function (item) {
                if (!ValidateQuantity(item)) {
                    isValid = false;
                }
                if (!ValidateReason(item)) {
                    isValid = false;
                }
            })
            return isValid;
        }
 
        var ConfirmRemove = function (item) {
            var popup = angular.element('#itemRemovePopup');
            popup.modal('show');
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
            var popup = angular.element('#itemRemovePopup');
            popup.modal('hide');
        }

        var SelectAllItems1 = function () {
            if (self.model.selectAllRows1) {
                self.model.SelectedItemsIds = self.model.searchNewIssueList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.searchNewIssueList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
            console.log("Selected Item Ids : ",self.model.searchNewIssueList);
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewAssetReturn", {
            url: '/addNewAssetReturn',
            templateUrl: 'views/master/UnitMaster/assetManagement/add-new-asset-return.html',
            controller: "AddNewAssetReturn.Controller",
            controllerAs: "vm",
            title: 'Asset Return'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewAssetReturn.Controller", addNewAssetReturnController);
})(); 