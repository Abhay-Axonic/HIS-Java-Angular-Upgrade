(function () {
    "use strict";

    function addNewNonReturnableGatePassController($scope, $rootScope, $http, $filter, commonDetailService, $state, CONSTANTS, StatusService, GenericService, growl,$cookies) {
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
                stores: [{
                    "store": "Select Store"
                }],
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                SelectedItemsIds: [],
                selectAllRows: false,
                SelectedItemIds: [],
                NewItemList: [],
                accessoryList: [],
                itemList: [],
                requestItemIds: [],
                requestItemDetails: []
            };

            StatusService.GetStatus().then(function (status) {
                self.model.Status = status;
            })

            self.model.yearList = [];
            self.model.SearchVendorNames = SearchVendorNames;
            self.model.nameNotFound = nameNotFound;
            self.model.AddVendorName = AddVendorName;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.vendorNames = [];
            self.model.OpenPopup = OpenPopup;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.isSelected = isSelected;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.AddRequestItems = AddRequestItems;
            self.model.Expand = Expand;
            self.model.GetRequestItemList = GetRequestItemList;
            self.model.SelectAllRequestItems = SelectAllRequestItems;
            self.model.SelectRequestItem = SelectRequestItem;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;

            self.model.ViewAccessoryDetails = ViewAccessoryDetails;
            self.model.RemoveItemRow = RemoveItemRow;
            self.model.OnChangeAgainst = OnChangeAgainst;
            self.model.SaveNewNonReturnableGatePass = SaveNewNonReturnableGatePass;
            self.model.NavigateToNonReturnableGatePass = NavigateToNonReturnableGatePass;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.ValidateAmount = ValidateAmount;
            self.model.ValidateRemark = ValidateRemark;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;

            self.model.ConfirmStorePopup = ConfirmStorePopup;
            self.model.ChangeStore = ChangeStore;
            self.model.SetOldValueStore = SetOldValueStore;

            self.model.ConfirmAgainstPopup = ConfirmAgainstPopup;
            self.model.ChangeAgainst = ChangeAgainst;
            self.model.SetOldValueAgainst = SetOldValueAgainst;
            
            PopulateDropdown();

        }

        var OpenExpiryDate = function (idx) {
            self.model.dateOpen[idx] = true;
        }

        var PopulateDropdown = function () {
            //self.model.store = self.model.stores[0];
            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.stores = self.model.stores.concat(response.data);
                    for(var i=0;i<self.model.stores.length;i++)
                    {
                        if(self.model.stores[i].id==JSON.parse(sessionStorage.getItem('selectedTopStore')).storeId)
                        {
                            self.model.store = self.model.stores[i];
                        }
                    }
                    $rootScope.stopLoader();
                    var returnableId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!returnableId) {
                        commonDetailService.setDataId(null);
                        GetNonReturnableGatepassDetails(returnableId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var GetNonReturnableGatepassDetails = function (returnableId) {
            var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.DETAILS_API  + returnableId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.returnableId = response.data.id;
                    self.model.remark = response.data.remark;
                    self.model.store = self.model.stores.find(function (returnable) {
                        return response.data.storeId == returnable.id;
                    });
                    self.model.against=  response.data.requestTypeId.toString();
                    self.model.vendor = response.data.vendor;
                    self.model.vendorId = response.data.vendorId;

                    self.model.itemList = [];
                    self.model.itemList = response.data.items;
                    self.model.itemList.forEach(function (item) {
                        item.gatepassAmount = item.amount;
                        item.batchNo = item.serialNo;
                        item.gatepassQuantity = item.quantity;
                        item.itemFlag = true;

                    });
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
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

        var nameNotFound = function (search) {
            var item = {
                "itemFound": false,
                "detail": "Vendor Not found with name " + search
            };
            self.model.vendorNames = [item];
            return self.model.vendorNames;
        }

        var AddVendorName = function (vendor, item) {

            self.model.vendorName = vendor.vendorName;
            self.model.vendorId = vendor.id;
        }

        var itemValidation = function () {
            var isValid = true;

            if (!self.model.against) {
                isValid = false;
                growl.error('Please select against option');
            }
            if (!self.model.store.id) {
                isValid = false;
                growl.error('Please select store');
            }
            if (!self.model.vendorId) {
                isValid = false;
                growl.error('Please select vendor');
            }
            return isValid
        }

        var OpenPopup = function () {
            self.model.NewItemList = [];
            self.model.search = '';
            if (itemValidation()) {
                if (self.model.against == 1) {
                    self.model.requestItemList = [];
                    self.model.requestItemDetails = [];
                    var popup = angular.element('#addRequestItemList');
                    popup.modal('show');
                } else if (self.model.against == 2) {
                    var popup = angular.element('#additemlist');
                    popup.modal('show');
                    GetAssetCategoryList();
                    self.model.NewItemList = [];
                }
            }

        }

        var GetAssetCategoryList = function () {
            var consumption = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            consumption.assetCategory = consumption.assetCategories[0];
            consumption.productCategory = consumption.productCategories[0];

            consumption.assetCategories.splice(1);
            consumption.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    consumption.assetCategories = consumption.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var consumption = self.model;

            //TODO : Add asset category to the uri
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    consumption.productCategories = consumption.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var searchNewItem = function () {

            var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
            const data = {
                "assetTypeId": 2,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                "storeId": self.model.store.id
            }

            self.model.NewItemList = [];
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.NewItemList = response.data;
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;
                    self.model.SelectedItemsIds = [];
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

        var getAllItemDetails = function () {
            self.model.NewItemList.forEach(function (item) {
                item.itemFlag = true;
                if (item.isSelected == true) {
                    self.model.itemList.push(item);
                }
            });

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
                growl.error('From Date should lesser than To Date');
                isValid = false;
            }

            return isValid;
        }

        var GetRequestItemList = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.GET_REQUEST_API + '/' + $scope.unitId + '/'+$scope.organizationId;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "vendorId": self.model.vendorId,
                    "requestNo": self.model.requestNo,
                    "unitId":$scope.unitId,
                }

                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.requestItemList = response.data;
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        self.model.requestItemList = [];
                        growl.error(err.data.message);
                    });
            }
        }

        var Expand = function (item) {
            item.Expanded = !item.Expanded;
        }

        var getSelectedIds = function () {
            var selecteItem = self.model.requestItemDetails.map(function (item) {
                return item;
            });
            return _.uniq(selecteItem);
            console.log(selecteItem);
        }

        var SelectAllRequestItems = function () {
            var selectItem = self.model.isSelectedRequestItem;
            self.model.auctionRequisitionId = self.model.isSelectedRequestItem.auctionRequisitionId;
            self.model.requestItemIds = [];
            angular.forEach(self.model.requestItemList, function (item) {
                if (item.id !== selectItem.id) {
                    item.items.forEach(function (x) {
                        x.isSelectedItem = false;
                        x.isChecked = false;
                        item.isChecked = false;
                    });
                } else {
                    item.items.forEach(function (x) {
                        self.model.requestItemDetails.push(x);
                        x.isSelectedItem = true;
                        x.isChecked = true;
                        item.isChecked = true;
                        self.model.requestItemIds.push(x.itemId);
                    });
                }
            });
        }

        var isSelectedItem = function (item) {
            return item.isSelectedItem;
        }

        var SelectRequestItem = function (item, indent) {
            var itemIds = self.model.requestItemIds;
            if (self.model.isSelectedRequestItem != indent) {
                angular.forEach(self.model.requestItemList, function (po) {
                    if (po.id !== indent.id) {
                        po.items.forEach(function (x) {
                            x.isSelectedItem = false;
                            x.isChecked = false;
                            po.isChecked = false;
                        });
                    }
                });
            }
            if (item.isSelectedItem) {
                item.isChecked = true;
                indent.isChecked = true;
                itemIds.push(item.itemId);
                self.model.requestItemDetails.push(item);
            } else {
                item.isChecked = false;
                indent.isChecked = false;
                var index = itemIds.indexOf(item.isSelectedItem);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }

                var itemIndex = self.model.requestItemDetails.indexOf(item);
                if (itemIndex > -1) {
                    self.model.requestItemDetails.splice(itemIndex, 1);
                }
            }

            if (indent.items.every(isSelectedItem)) {
                self.model.isSelectedRequestItem = indent;
            } else {
                self.model.isSelectedRequestItem = null;
            }
        }

        var SelectedItems = function () {
            var reqItem = self.model.isSelectedRequestItem;
            if (reqItem) {
                self.model.reqItemDetails = [];
                var reqItemData = {
                    "reqItemId": reqItem.id,
                    "itemIds": []
                }
                angular.forEach(reqItem.items, function (item) {
                    if (item.isChecked) {
                        reqItemData.itemIds.push(item.itemId);
                    }
                });
                if (reqItem.isChecked && reqItemData.itemIds.length > 0) {
                    self.model.reqItemDetails.push(reqItemData);
                }
            }
        }

        var AddRequestItems = function (requestItemList) {

            getSelectedIds().forEach(function (item) {
                item.requestItemFlag = true;
                var isFound = self.model.itemList.find(function (x) {
                    return x.itemId == item.itemId;
                });
                if (!isFound) {
                    self.model.itemList.push(item);
                }
            });
            var popup = angular.element('#addRequestItemList');
            popup.modal('hide');
        }

        var ViewAccessoryDetails = function (item) {
            var popup = angular.element('#accessoryViewModal');
            popup.modal('show');
            self.model.itemList.find(function (x) {
                if (x.itemId == item.itemId) {
                    self.model.itemForAccessoryDetail = item.accessories ? item.accessories : item.accessorriesList;
                    // self.model.itemForAccessoryDetail.forEach(function(item){
                    //     item.batchNo = item.serialNo;
                    // })
                }
            });
        }

        var RemoveItemRow = function (index) {
            var itemList = self.model.itemList;
            self.model.itemList = itemList.slice(0, index).concat(itemList.slice(index + 1))
        }

        var OnChangeAgainst = function () {
            self.model.itemList = [];
        }

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }
        var SaveNewNonReturnableGatePass = function () {
            // if (validateSave()) {
            if (!self.model.returnableId) {
                SaveNonReturnableGatePass();
            } else {
                UpdateNonReturnableGatePass();
            }
            // }
        }
        var SaveNonReturnableGatePass = function () {
            if (Validate()) {
                var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.SAVE_API;

                var data = {
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "vendorId": !!self.model.vendorId ? self.model.vendorId : undefined,
                    "requestTypeId": parseInt(self.model.against),
                    "auctionRequisitionId": self.model.auctionRequisitionId,
                    "statusId": SetStatus('New'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "items": []
                }

                angular.forEach(self.model.itemList, function (item, key) {
                    var itemAccessories = [];
                    item.accessories.forEach(function (element) {
                        itemAccessories.push({
                            "batchId": element.batchId,
                            "quantity": element.quantity,
                            // "amount": element.amount
                        });
                    });

                    var item = {
                        "statusId": SetStatus('New'),
                        "batchId": item.batchId,
                        "auctionRequisitionDetailId": item.auctionRequisitionDetailId,
                        "quantity": parseInt(item.gatepassQuantity),
                        "amount": parseInt(item.gatepassAmount),
                        "remark": item.remark,
                        "accessories": itemAccessories,
                    }
                    data.items.push(item);
                });
                NavigateToNonReturnableGatePass();
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success('New non returnable gatePass successfully saved');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving new non returnable gate pass');
                    });
            }
        }

        var UpdateNonReturnableGatePass = function () {

        //    var URI = CONSTANTS.INVENTORY.STORE.RETURNABLE_GATEPASS.UPDATE_API;
            var URI = CONSTANTS.ASSET.NON_RETURNABLE_GATEPASS.UPDATE_API;
            var data = {
                "id": self.model.returnableId,
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "items": []
            }

            angular.forEach(self.model.itemList, function (item, key) {
                var itemAccessories = [];
                // item.accessories.forEach(function (element) {
                //     itemAccessories.push({
                //         "id": element.id,
                //         "batchId": element.batchId,
                //         "quantity": element.quantity,
                //         // "amount": element.amount
                //     });
                // });

                var item = {
                    "id": item.id,
                    "quantity": parseInt(item.gatepassQuantity),
                    "amount": parseInt(item.gatepassAmount),
                    "remark": item.remark,
                    "accessories": itemAccessories,
                }
                data.items.push(item);
            });
            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                    NavigateToNonReturnableGatePass();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }


        var Validate = function () {
            var isValid = true;

            var itemList = self.model.itemList;
            if (!itemList || itemList.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the List");
            } else {
                itemList.forEach(function (item) {

                    if (self.model.against == 2) {
                        if (!ValidateAmount(item)) {
                            isValid = false;
                        }
                        if (!ValidateQuantity(item)) {
                            isValid = false;
                        }
                        if (!ValidateRemark(item)) {
                            isValid = false;
                        }
                    } else if (self.model.against == 1) {
                        if (!item.remark) {
                            item['isRemarkValid'] = true;
                            isValid = false;
                            growl.error('Please fill all the fields')
                        }
                    }
                });
            }
            return isValid;
        }

        var NavigateToNonReturnableGatePass = function () {
            $state.go('nonReturnableGatePass');
        }


        var ValidateQuantity = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.gatepassQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.gatepassQuantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Quantity Cannot Be Zero');
            }
            // else if (parseInt(item.quantity) > item.newMaxOrderQuantity) {
            //   item.isQuantityValid = true;
            //   isValid = false;
            //   growl.error('Entered quantity is exceeding the max order quantity');
            // }
            else {
                item.gatepassQuantity = parseInt(item.gatepassQuantity);
                isValid = true;
            }

            return isValid;
        }

        var ValidateAmount = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.gatepassAmount))) {
                item.isGatepassAmountValid = true;
                isValid = false;
                growl.error("Please Enter Amount");
            } else if (parseInt(item.gatepassAmount) == 0) {
                item.isGatepassAmountValid = true;
                isValid = false;
                growl.error('Amount Cannot Be Zero');
            }
            else {
                item.gatepassAmount = parseInt(item.gatepassAmount);
                isValid = true;
            }

            return isValid;
        }
        var ValidateRemark = function (item) {
            var isValid = true;

            if (!item.remark) {
                item.isRemarkValid = true;
                isValid = false;
                growl.error("Please Enter Remark");
            }

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
      
          var SetOldValueAgainst = function (against) {
            self.model.oldAgainst = against;
            self.model.against = self.model.oldAgainst;
          }
      
          var ChangeAgainst = function (against) {
            if (!!self.model.itemList.length) {
              self.model.against = self.model.oldAgainst;
              self.model.againstNew = JSON.parse(JSON.stringify(against));
              $("#againstChange").modal()
            }
          }
      
          var ConfirmAgainstPopup = function () {
            self.model.against = self.model.againstNew;
            self.model.itemList = [];
            ModalDismiss('againstChange');
          }
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewNonReturnableGatePass", {
            url: "/addNewNonReturnableGatePass",
            templateUrl: 'views/Asset Management/add-new-non-returnable-gatepass.html',
            controller: "AddNewNonReturnableGatePass.Controller",
            controllerAs: "vm",
            title: 'Non Returnable Gate Pass'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewNonReturnableGatePass.Controller", addNewNonReturnableGatePassController);
})();