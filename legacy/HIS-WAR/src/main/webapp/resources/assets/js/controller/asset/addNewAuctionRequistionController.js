(function () {
    "use strict";
    function addNewAuctionRequistionController($scope, $rootScope, $http, $state, CONSTANTS, GenericService, growl, commonDetailService,$cookies) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                stores: [{
                    "store": "All Stores"
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
                SelectedItemIds: [],
                NewItemList: [],
                itemList: []

            };
            self.model.GetAssetItemList = GetAssetItemList;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isSelected = isSelected;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.removeRowData = removeRowData;
            self.model.OpenExpiryDate = OpenExpiryDate;
            self.model.saveNewAuctionRequisition = saveNewAuctionRequisition;
            self.model.AddAssets = AddAssets;
            self.model.viewAssets = viewAssets;
            self.model.createNewAuctionRequisition = createNewAuctionRequisition;
            self.model.UpdateNewAuctionRequisition = UpdateNewAuctionRequisition;
            self.model.NavigteToAuctionChange = NavigteToAuctionChange;
            self.model.GetAuctionRequisitionData = GetAuctionRequisitionData;
            self.model.openAddItemPopup = openAddItemPopup;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;

            self.model.ChangeStore = ChangeStore;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            self.model.SetOldValueStore = SetOldValueStore;
            self.model.SaveAccessoryDetails = SaveAccessoryDetails;
            self.model.ValidateSpecification =ValidateSpecification;
            // self.model.ValidateQuantity = ValidateQuantity;
            PopulateDropdown();
        }

        var openAddItemPopup = function () {
            $("#addNewItemWasteStore1").modal();
            self.model.SelectedItemsIds = [];
            self.model.searchNewItemList = [];
            GetAssetCategoryList();
            self.model.isPopup = true;
        }

        var PopulateDropdown = function () {
            var auction = self.model;
            //auction.store = auction.stores[0];
            // auction.status = auction.statuses[0];
            var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.DROPDOWN_API+ '/' + $scope.unitId  + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    auction.stores = auction.stores.concat(response.data.store);
                    //   auction.statuses = auction.statuses.concat(response.data.status);
                    for(var i=0;i<auction.stores.length;i++)
                    {
                        if(auction.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
                        {
                            auction.store = auction.stores[i];
                        }
                    }
                    $rootScope.stopLoader();
                    self.model.auctionId = commonDetailService.getDataId();
                    if (!! self.model.auctionId ) {
                        commonDetailService.setDataId(null);
                        GetAuctionRequisitionData(self.model.auctionId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }



        function GetAssetItemList() {
            self.model.action = 'Open';
            var popup = angular.element('#addAssetItemlist');
            popup.modal('show');
            GetAssetCategoryList();
            self.model.NewItemList = [];
            self.model.selectAllRows = false;
            self.model.isItemChecked = false;
            self.model.search = '';
        }

        var GetAssetCategoryList = function () {
            var auction = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

            auction.assetCategory = auction.assetCategories[0];
            auction.productCategory = auction.productCategories[0];

            auction.assetCategories.splice(1);
            auction.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    auction.assetCategories = auction.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var GetProductCategoryList = function (id) {
            var auction = self.model;

            //TODO : Add asset category to the uri
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            auction.productCategory = auction.productCategories[0];

            auction.productCategories.splice(1);
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    auction.productCategories = auction.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
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
                        "storeId": self.model.store.id,
                        "itemName": self.model.search.itemName,
                        "itemCode": self.model.search.itemCode
                    }

                    // self.model.NewItemList = [];
                    self.model.selectAllRows = false;
                    // self.model.SelectedItemIds = [];
                    self.model.isItemChecked = false;
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.NewItemList = response.data;
                        angular.forEach(self.model.NewItemList, function (x) {
                            self.model.accessoriesDetails = x.accessories;
                        });
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
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
                        "storeId": self.model.store.id,
                        "itemName": self.model.search.itemName,
                        "itemCode": self.model.search.itemCode
                    }

                    // self.model.NewItemList = [];
                    self.model.selectAllRows = false;
                    // self.model.SelectedItemIds = [];
                    self.model.isItemChecked = false;
                    $rootScope.startLoader();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.NewItemList = response.data;
                        angular.forEach(self.model.NewItemList, function (x) {
                            self.model.accessoriesDetails = x.accessories;
                        });
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                }
            }
            else{
                self.model.NewItemList = [];
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

        function getAllItemDetails() {
            self.model.NewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.itemList.push(item);
                }
            });
        }

        function removeRowData(item) {
            var index = self.model.itemList.indexOf(item);
            self.model.itemList.splice(index, 1);
        }

        var OpenExpiryDate = function (idx) {
            self.model.dateOpen[idx] = true;
        }

        var saveNewAuctionRequisition = function () {
             if (validateSave()) {
            if (!self.model.auctionRequisitionId) {
                createNewAuctionRequisition();
            } else {
                UpdateNewAuctionRequisition();
            }
             }
        }

        var createNewAuctionRequisition = function () { 
                var auction = self.model;

                var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.SAVE_API;

                var data = {
                    "storeId": auction.store.id,
                    "statusId": 1,
                    "approvalStatusId": 1,
                    "remark": auction.remark,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "items": [],
                };

                self.model.postData = [];
                angular.forEach(self.model.itemList, function (item) {
                    var postData = {
                        "batchId": item.batchId,
                        "auctionQty": item.auctionQty,
                        "remark": item.remark,
                        "statusId": 1,
                        "accessories": item.accessoriesList
                    }
                    data.items.push(postData);
                });

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        // ClearRequests();
                        $rootScope.stopLoader();
                        NavigteToAuctionChange();
                        growl.success(response.data.message);
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Something Went Wrong');
                    }); 
        }

        var UpdateNewAuctionRequisition = function () { 
                var auction = self.model;

                var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.DETAILS_API  + self.model.auctionRequisitionId;

                var data = {
                    "storeId": auction.store.id,
                    "remark": auction.remark,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId
                };

                self.model.postData = [];
                angular.forEach(self.model.itemList, function (item) {
                    var postData = {
                        "id": item.id,
                        "batchId": item.batchId,
                        "auctionQty": item.auctionQty,
                        "remark": item.remark,
                        "accessories": []
                    }
                    data.items.push(postData);
                });
                angular.forEach(self.model.itemList.accessories, function (item) {
                    var postAssetDetailsData = {
                        "id": item.id,
                        "batchId": item.id
                    }
                    postData.assetDetails.push(postAssetDetailsData);
                });
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        NavigteToAuctionChange();
                        growl.success(response.data.message);
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    }); 
        }
        var NavigteToAuctionChange = function () {
            $state.go('auctionRequistion');
        }
        var AddAssets = function (item) {
            self.model.accessoriesDetails = item.accessories;
        }

        var viewAssets = function (item) {
            self.model.accessoriesDetails = [];
            self.model.currentBatch = item;
            self.model.accessoriesDetails = item.accessories ; 
            angular.forEach( self.model.accessoriesDetails,function(item){
                item.isAcessory  = true;
            });
            $('#viewAsset').modal();
        }


        var GetAuctionRequisitionData = function (auctionRequisitionId) {
            self.model.auctionRequisitionId = auctionRequisitionId;
            var URI = CONSTANTS.ASSET.AUCTION_REQUISITION.DETAILS_API + $scope.unitId+ '/'+$scope.organizationId + '/' + $scope.userId + '/' + auctionRequisitionId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.auctionRequisitionId = response.data.id;

                    self.model.store = self.model.stores.find(function (auction) {
                        return response.data.storeId == auction.id;
                    });
                    self.model.itemList = [];
                    angular.forEach(response.data.items, function (item) {
                        var auctionItem = {
                            "id": item.id,
                            "assetId": item.assetId,
                            "itemCode": item.assetCode,
                            "itemName": item.assetName,
                            "batchNo": item.serialNo,
                            "vendorId": item.vendorId,
                            "vendor": item.vendorName,
                            "manufactureYear": item.manufactureYear,
                            "auctionQty": item.auctionQty,
                            "availableQuantity": item.availableQty,
                            "assetDetails": item.assetDetails,
                            "remark": item.remark,
                            "batchId": item.batchId
                        }
                        self.model.itemList.push(auctionItem);
                    });

                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
 
        var ValidateQuantity = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.auctionQty))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.auctionQty) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }
            else if (parseInt(item.auctionQty) > item.availableQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered quantity is exceeding the available quantity');
            } else {
                item.auctionQty = parseInt(item.auctionQty);
                isValid = true;
            }

            return isValid;
        }
        var ConfirmRemove = function (item) {
            var popup = angular.element('#confirmRemove');
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
            var popup = angular.element('#confirmRemove');
            popup.modal('hide');
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

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }
        var SaveAccessoryDetails = function () {
            if (ValidateAccesories()) {
            self.model.itemList.forEach(function (obj) {
                obj.accessoriesList = [];
                if (obj.batchId == self.model.currentBatch.batchId) {
                    self.model.accessoriesDetails.forEach(function (item) {
                        obj.accessoriesList.push({
                            "id": item.id,
                            "batchId": item.batchId,
                            "auctionQuantity": item.auctionQty,
                            "specification": item.specification
                        })
                    });
                }
            });
            self.model.accessoryList = [];

            var popup = angular.element('#viewAsset');
            popup.modal('hide');
            }
        }
        var ValidateAccesories = function () {
            var isValid = true;
            self.model.accessoriesDetails.forEach(function (item) {
                if (!ValidateQuantity(item)) {
                    isValid = false;
                }
                if (!ValidateSpecification(item)) {
                    isValid = false;
                }
            })
            return isValid;
        }

        var ValidateSpecification = function (item) {
            var isValid = true;
            if(item.isAcessory){
                if (!item.specification) {
                    item.isSpecificationValid = true;
                    isValid = false;
                    growl.error("Please Enter Specification");
                  }
            }
            else{
                if (!item.remark) {
                    item.isRemarkValid = true;
                    isValid = false;
                    growl.error("Please Enter Remark");
                  } 
            }
            
            return isValid;
          }
      
        function validateSave() {
            var isValid = true;
            var ItemDetailList = self.model.itemList;
            // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 
      
            if (ItemDetailList.length < 1) {
              growl.error("Please Add Items To Save");
              isValid = false;
            } 
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateQuantity(ItemDetail)) {
                  isValid = false;
                }
                if (!ValidateSpecification(ItemDetail)) {
                  isValid = false;
                }
            }
      
            return isValid;
          }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewAuctionRequistion", {
            url: "/addNewAuctionRequistion",
            templateUrl: 'views/master/GlobalMaster/AssetManagenent/Master/add-new-auction-requistion.html',
            controller: "AddNewAuctionRequistion.Controller",
            controllerAs: "vm",
            title: 'Auction Requisition'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewAuctionRequistion.Controller", addNewAuctionRequistionController);
})();
