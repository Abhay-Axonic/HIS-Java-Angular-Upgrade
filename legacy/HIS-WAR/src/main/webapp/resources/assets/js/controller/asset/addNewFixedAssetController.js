(function () {
    "use strict";

    function addNewFixedAssetController($scope, $rootScope, $http, $filter, commonDetailService, $state, $cookies, CONSTANTS, StatusService, GenericService, growl) {
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
                stores: [{
                    "store": "Select Store"
                }],
                statuses: [{
                    "status": "All Status"
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

                warrantyFrom: new Date(),
                warrantyTo: new Date(),
                policyFrom: new Date(),
                policyTo: new Date(),
                policyFromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                },
                policyToDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                },
                warrantyFromDateOpen: [],
                warrantyToDateOpen: [],
                fixedAssetList: [],
                accessoryList: [],
                sparesList: []
                               
                ,warrantyFromDateOptions: {
                    maxDate:  new Date(new Date().getFullYear(), new Date().getMonth(), 1),
                    minMode: "month"
                  },
                  warrantyToDateOptions: {
                    minDate:  new Date(new Date().getFullYear(), new Date().getMonth() + 1, 1),
                    minMode: "month"
                  }
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
            self.model.serviceProviders = [];
            self.model.SearchServiceProvider = SearchServiceProvider;
            self.model.serviceProviderNameNotFound = serviceProviderNameNotFound;
            self.model.AddServiceProviderName = AddServiceProviderName;
            self.model.openAccessory = openAccessory;
            self.model.openAddItem = openAddItem;
            self.model.SearchInsauranceProvider = SearchInsauranceProvider;
            self.model.AddInsauranceProviderName = AddInsauranceProviderName;
            self.model.insauranceeProviderNameNotFound = insauranceeProviderNameNotFound;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.searchAccessoriesItem = searchAccessoriesItem;
            self.model.searchSparesItem = searchSparesItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.AddItems = AddItems;
            self.model.yearListData = yearListData;
            self.model.openAddItemPopup = openAddItemPopup;
            self.model.openAddItemPopup1 = openAddItemPopup1;
            self.model.isPopup = false;
            self.model.AddItemsPopup = AddItemsPopup;
            self.model.AddItemsPopupSpares = AddItemsPopupSpares;
            self.model.OpenWarrantyFromDate = OpenWarrantyFromDate;
            self.model.OpenWarrantyToDate = OpenWarrantyToDate;
            self.model.OpenPolicyFromDate = OpenPolicyFromDate;
            self.model.OpenPolicyToDate = OpenPolicyToDate;
            self.model.SaveInsuranceDetails = SaveInsuranceDetails;
            self.model.SaveAccessoryDetails = SaveAccessoryDetails;
            self.model.SaveSparesDetails = SaveSparesDetails;
            self.model.OpenPopup = OpenPopup;
            self.model.AddAccessoryDetails = AddAccessoryDetails;
            self.model.AddSparesDetails = AddSparesDetails;
            self.model.AddInsuranceDetails = AddInsuranceDetails;
            self.model.ViewAccessoryDetails = ViewAccessoryDetails;
            self.model.ViewSparesDetails = ViewSparesDetails;
            self.model.ViewInsuranceDetails = ViewInsuranceDetails;
            self.model.RemoveAssetRow = RemoveAssetRow;
            self.model.RemoveAccessoryRow = RemoveAccessoryRow;
            self.model.RemoveSparesRow = RemoveSparesRow;
            self.model.SaveNewfixedAssetMaster = SaveNewfixedAssetMaster;
            self.model.GetFixedAssetMasterDetails = GetFixedAssetMasterDetails;
            self.model.fixedAssetId = commonDetailService.getDataId();

            self.model.SetOldValueStore = SetOldValueStore;
            self.model.ChangeFromStore = ChangeFromStore;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.removeRowData = removeRowData;
            self.model.AddDuplicateRow = AddDuplicateRow;
            PopulateDropdown();

            yearListData();
            
            self.model.ValidateDates = ValidateDates;
            self.model.policyToDateOptionsNew = policyToDateOptionsNew;
            self.model.RemoveAccessoryRowFromItem=RemoveAccessoryRowFromItem;
            self.model.RemoveSparesRowFromItem=RemoveSparesRowFromItem;
        }

        var OpenExpiryDate = function (idx) {
            self.model.dateOpen[idx] = true;
        }

        var OpenWarrantyFromDate = function (idx) {
            self.model.warrantyFromDateOpen[idx] = true;
        }

        var OpenWarrantyToDate = function (idx) {
            self.model.warrantyToDateOpen[idx] = true;
        }

        var OpenPolicyFromDate = function () {
            self.model.policyFromDateOpened = true;
        }

        var OpenPolicyToDate = function () {
            self.model.policyToDateOpened = true;
        }
       //start code to change for resolved HIS-9719 Fixed asset opening balance - Insurance policy to date should be greater than from date
        var policyToDateOptionsNew = function () {
        	if (self.model.policyFromDate){
        		self.model.policyToDateOptions= {
        			minDate: angular.copy(self.model.policyFromDate),
                    formatYear: 'yyyy',
                    showWeeks: false,
                }
        	}
        }
      //end code to change for resolved HIS-9719 Fixed asset opening balance - Insurance policy to date should be greater than from date

        var PopulateDropdown = function () {
            var fixed = self.model;
            fixed.store = fixed.stores[0];
            fixed.status = fixed.statuses[0];
            var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    fixed.stores = fixed.stores.concat(response.data.store);
                    fixed.statuses = fixed.statuses.concat(response.data.status);
                    fixed.pmsfrequencies = response.data.pmsfrequencies;
                    fixed.workingConditions = response.data.workingConditions;
                    var id = commonDetailService.getDataId();
                    $rootScope.stopLoader();

                    if (!!id) {
                        commonDetailService.setDataId(null);
                        GetFixedAssetMasterDetails(id);
                    }
                    self.model.fixedOpbId = commonDetailService.getDataId();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var yearListData = function () {
            var d = new Date();
            var y = parseInt(d.getFullYear());
            var i = parseInt(d.getFullYear()) - 50;
            while (i <= y) {
                self.model.yearList.push(i);
                i++;
            }
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

        function AddVendorName(vendor, item) {
            if(vendor.itemFound != false){
                item.vendorName = vendor.vendorName;
                item.vendorId = vendor.id;
            }
            else{
                /*self.model.fixedAssetList.forEach(function (obj) {
                    obj.vendor = '';
                });*/
            	
            	//start code for resolved HIS-8492 Fixed asset opening balance - Entering on the vendor not found on the second line item removed first line item entrty of the vendor
            	item.vendor = '';
            	//end code for resolved HIS-8492 Fixed asset opening balance - Entering on the vendor not found on the second line item removed first line item entrty of the vendor
            }
        }

        function SearchServiceProvider(search) {
            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.SERVICE_PROVIDER + $scope.organizationId + '/' + search;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        if (!!response.data && response.data.length > 0) {
                            self.model.serviceProviders = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.serviceProviders, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return serviceProviderNameNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return serviceProviderNameNotFound(search);
                    });
            } else {
                if (!!self.model.serviceProviders && self.model.serviceProviders.length > 0 && self.model.serviceProviders[0].itemFound != undefined && !self.model.serviceProviders[0].itemFound) {
                    return serviceProviderNameNotFound(search);
                } else {
                    return $filter('filter')(self.model.serviceProviders, {
                        $: search
                    });
                }
            }
        }
        var AddDuplicateRow = function (item) {
            var data = angular.copy(item);
            data.serialNo = '';
            data.yearOfMAnufacture = '';
            data.vendorId = '';
            data.vendorName = '';
            data.serviceProviderId = '';
            data.serviceProvider = '';
            data.warrantyFrom = '';
            data.warrantyTo = '';
            data.quantity = '';
            data.workingCondition = '';
            data.accesories=[];
            data.spares=[];
            data.selectedAccesoriesList = [];
            data.selectedSparesList = [];
            data.insurance = {};
            data.specification = '';
            data.isBatchRequired= false
            data.isPmsfrequenciesValid= false
            data.isQuantityValid= false
            data.isSerialNoValid= false
            data.isServiceProviderValid= false
            data.isSpecificationValid= false
            data.isVendorValid= false
            data.isWarrantyFromValid=false
            data.isWarrantyToValid= false
            data.isWorkingConditionValid= false
            data.isYearOfMAnufactureValid= false
 

            self.model.fixedAssetList.push(data);
          }
        function serviceProviderNameNotFound(search) {
            var item = {
                "itemFound": false,
                "detail": "Service Provider Not found with name " + search
            };
            self.model.serviceProviders = [item];
            return self.model.serviceProviders;
        }

        function AddServiceProviderName(serviceProvider, item) {
            if(serviceProvider.itemFound != false){
                item.serviceProviders = serviceProvider.serviceProviders;
                item.serviceProviderId = serviceProvider.id;
            }
            else{
                self.model.fixedAssetList.forEach(function (obj) {
                    obj.serviceProvider = '';
                });
            }   
        }

        var openAccessory = function () {
            $state.go('fixedAssetAddAcessory');
            commonDetailService.setDataId(self.model.store.id);
        }

        $scope.validatemrp = function(item){
            var isValid = true;
            if(parseInt(item.cop) > parseInt(item.mrp)){
                growl.error("MRP should be greater than or equal to COP");
                item.mrp = 0;
                isValid = false;
            }
            return isValid;
        }

        $scope.validatecop = function(item){
            var isValid = true;
            if(parseInt(item.cop) > parseInt(item.mrp)){
                growl.error("COP should be less than or equal to MRP");
                item.cop = 0;
                isValid = false;
            }
            return isValid;
        }

        function SearchInsauranceProvider(search) {
            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.INSAURANCE_COMPANY_API + $scope.unitId + '/' + search;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        if (!!response.data && response.data.length > 0) {
                            self.model.insauranceCompanies = response.data;
                            self.model.insuranceCompanyName = response.data.description;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.insauranceCompanies, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return insauranceeProviderNameNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return insauranceeProviderNameNotFound(search);
                    });
            } else {
                if (!!self.model.insauranceCompanies && self.model.insauranceCompanies.length > 0 && self.model.insauranceCompanies[0].itemFound != undefined && !self.model.insauranceCompanies[0].itemFound) {
                    return insauranceeProviderNameNotFound(search);
                } else {
                    return $filter('filter')(self.model.insauranceCompanies, {
                        $: search
                    });
                }
            }
        }

        function insauranceeProviderNameNotFound(search) {
            var item = {
                "itemFound": false,
                "detail": "Insaurance Company Not found with name " + search
            };
            self.model.insuranceCompany = [item];
            return self.model.insuranceCompany;
        }

        function AddInsauranceProviderName(insuranceCompany) {
            self.model.insuranceCompany = insuranceCompany.description;
            self.model.insuranceCompanyId = insuranceCompany.id;
        }

        var openAddItem = function () {
            self.model.selectAllRows = false;
            self.model.search = '';
            self.model.SelectedItemsIds = [];
            self.model.searchNewItemList = [];
            $("#addNewItemWasteStore").modal();
            GetAssetCategoryList();
        }

        var openAddItemPopup = function () {
            self.model.selectAllRows = false;
            self.model.search = '';
            self.model.searchNewItemList = [];
            $("#addNewItemWasteStore1").modal();
            self.model.SelectedItemsIds = [];
            GetAssetCategoryList();
            self.model.isPopup = true;
        }

        var openAddItemPopup1 = function () {
            self.model.selectAllRows = false;
            self.model.search = '';
            self.model.searchNewItemList = [];
            $("#addNewItemWasteStore2").modal();
            self.model.SelectedItemsIds = [];
            GetAssetCategoryList();
            self.model.isPopup = true;
        }

        var GetAssetCategoryList = function () {
            var asset = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 2 + "/"+$scope.unitId + "/"+$scope.organizationId;

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

        var searchAccessoriesItem = function () {
            var URI = CONSTANTS.GLOBAL.ACCESSORIES_SPARES_ITEM_API + $scope.unitId+"/"+1;

            const data = {
                "assetTypeId": 2,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                "storeId": self.model.store.id ? self.model.store.id : undefined,
                "itemCode": self.model.search.itemCode,
                "itemName": self.model.search.itemName
            }
            self.model.searchNewItemList = [];
            self.model.selectAllRows = false;
            self.model.SelectedItemIds = [];
            self.model.isItemChecked = false;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    self.model.searchNewItemList = response.data;
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
        }

        var searchSparesItem = function () {
            var URI = CONSTANTS.GLOBAL.ACCESSORIES_SPARES_ITEM_API +$scope.unitId+"/"+2;

            const data = {
                "assetTypeId": 2,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                "storeId": self.model.store.id ? self.model.store.id : undefined,
                "itemCode": self.model.search.itemCode,
                "itemName": self.model.search.itemName
            }
            self.model.searchNewItemList = [];
            self.model.selectAllRows = false;
            self.model.SelectedItemIds = [];
            self.model.isItemChecked = false;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    self.model.searchNewItemList = response.data;
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
        }

        var searchNewItem = function () {
            if(self.model.search.itemName){
                if(self.model.search.itemName.length >= 3){        
                    var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

                    const data = {
                        "assetTypeId": 2,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id ? self.model.store.id : undefined,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName
                    }
                    self.model.searchNewItemList = [];
                    self.model.selectAllRows = false;
                    self.model.SelectedItemIds = [];
                    self.model.isItemChecked = false;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.searchNewItemList = response.data;
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }
            }
            else if(self.model.search.itemCode){
                if(self.model.search.itemCode.length >= 3){
                    var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

                    const data = {
                        "assetTypeId": 2,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id ? self.model.store.id : undefined,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName
                    }
                    self.model.searchNewItemList = [];
                    self.model.selectAllRows = false;
                    self.model.SelectedItemIds = [];
                    self.model.isItemChecked = false;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.searchNewItemList = response.data;
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
                if(self.model.SelectedItemsIds){
                    self.model.data = self.model.searchNewItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedItemsIds.push(item.itemId);
                        return self.model.SelectedItemsIds;
                    });
                }
                else{
                    self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;

                        return item.itemId;
                    });
                }
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
                if(item.itemId){
                    itemIds.push(item.itemId);
                }
                else{
                    itemIds.push(item.itemid);
                }
            } else {
                if(item.itemId){
                    var index = itemIds.indexOf(item.itemId);
                    if (index > -1) {
                        itemIds.splice(index, 1);
                    }
                }
                else{
                    var index = itemIds.indexOf(item.itemid);
                    if (index > -1) {
                        itemIds.splice(index, 1);
                    }
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
                "storeId": self.model.store.id
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    response.data.forEach(function (item) {
                        var isFound = self.model.fixedAssetList.find(function (x) {
                            return x.itemId == item.itemId;
                        });
                        if (!isFound) {
                            item["insurance"] = {};
                            item["accesories"] = [];
                            item["spares"] = [];
                            self.model.fixedAssetList.push(item);
                        }
                    });
                    $rootScope.stopLoader();
                    // getAllItemDetails();
                    self.model.searchNewItemList = [];
                    self.model.SelectedItemsIds = [];

                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        var AddItemsPopup = function () {
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.store.id
            };
            $rootScope.startLoaderPopup();
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
                    $rootScope.stopLoaderPopup();
                    self.model.searchNewItemList = [];
                },
                function (err) {
                    $rootScope.stopLoaderPopup();
                    growl.error(err.data.message);
                });
        }

        var AddItemsPopupSpares = function () {
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.store.id
            };
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    response.data.forEach(function (item) {
                        var isFound = self.model.sparesList.find(function (x) {
                            return x.itemId == item.itemId;
                        });
                        if (!isFound) {
                            self.model.sparesList.push(item);
                        }
                    });
                    $rootScope.stopLoaderPopup();
                    self.model.searchNewItemList = [];
                },
                function (err) {
                    $rootScope.stopLoaderPopup();
                    growl.error(err.data.message);
                });
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        var OpenPopup = function (modal, item) {
            var popup = angular.element('#' + modal);
            popup.modal('show');

        }

        var AddAccessoryDetails = function (item, index) {
            self.model.currentItem = item;
            $scope.indexNo=index;
          //start code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
            self.model.accessoryList=[];  
            if(item.selectedAccesoriesList){
            	self.model.accessoryList=angular.copy(item.selectedAccesoriesList);
            	}
          //end code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
        }

        var SaveAccessoryDetails = function () {
            if (ValidateAccesories()) {
                self.model.fixedAssetList.forEach(function (obj) {
                	 var indexNo= self.model.fixedAssetList.indexOf(obj);
                    if (obj.itemId == self.model.currentItem.itemId && $scope.indexNo == indexNo) {
                            $scope.indexNo=-1;
                        self.model.accessoryList.forEach(function (item) {
                           /* obj.accesories.push({
                                "itemId": item.itemId,
                                "itemCode": item.itemCode,
                                "itemName": item.itemName,
                                "serialNumber": item.serialNo,
                                "warrantyFrom": moment(item.warrantyFrom).format('YYYY-MM-DD'),
                                "warrantyTo": moment(item.warrantyTo).format('YYYY-MM-DD'),
                                "specification": item.specification,
                                "quantity": item.quantity,
                                "workingConditionId": item.workingCondition
                            })*/
                            
                        	//start code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
                            var isFound = obj.accesories.find(function (x) {
                            	return x.itemId == item.itemId;
                            });
                            if (isFound) {                            	
                                    isFound.itemId= item.itemId,
                                    isFound.itemCode= item.itemCode,
                                    isFound.itemName= item.itemName,
                                    isFound.serialNumber= item.serialNo,
                                    isFound.warrantyFrom= moment(item.warrantyFrom).format('YYYY-MM-DD'),
                                    isFound.warrantyTo= moment(item.warrantyTo).format('YYYY-MM-DD'),
                                    isFound.specification= item.specification,
                                    isFound.quantity= item.quantity,
                                    isFound.workingConditionId= item.workingCondition                                
                            }else {
                            	obj.accesories.push({
                                    "itemId": item.itemId,
                                    "itemCode": item.itemCode,
                                    "itemName": item.itemName,
                                    "serialNumber": item.serialNo,
                                    "warrantyFrom": moment(item.warrantyFrom).format('YYYY-MM-DD'),
                                    "warrantyTo": moment(item.warrantyTo).format('YYYY-MM-DD'),
                                    "specification": item.specification,
                                    "quantity": item.quantity,
                                    "workingConditionId": item.workingCondition
                                })
                            }
                          //end code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data                            
                        });
                      //add code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
                       obj.selectedAccesoriesList=angular.copy(self.model.accessoryList);
                    }
                });

                var popup = angular.element('#accessoryAddModal');
                popup.modal('hide');
            }
        }

        var AddSparesDetails = function (item,index) {
            self.model.currentItem = item;
            $scope.indexNo=index;
          //start code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
            self.model.sparesList=[];  
            if(item.selectedSparesList){
            	self.model.sparesList=angular.copy(item.selectedSparesList);
            	}
          //end code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
        }

        var SaveSparesDetails = function () {
            if (ValidateSpares()) {
                self.model.fixedAssetList.forEach(function (obj) {
                	 var indexNo= self.model.fixedAssetList.indexOf(obj);
                    if (obj.itemId == self.model.currentItem.itemId && $scope.indexNo == indexNo) {
                        self.model.sparesList.forEach(function (item) {
                            /*obj.spares.push({
                                "itemId": item.itemId,
                                "itemCode": item.itemCode,
                                "itemName": item.itemName,
                                "serialNumber": item.serialNo,
                                "warrantyFrom": moment(item.warrantyFrom).format('YYYY-MM-DD'),
                                "warrantyTo": moment(item.warrantyTo).format('YYYY-MM-DD'),
                                "specification": item.specification,
                                "quantity": item.quantity,
                                "workingConditionId": item.workingCondition
                            })*/
                        	
                        	//start code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
                        	 var isFound = obj.spares.find(function (x) {
                             	return x.itemId == item.itemId;
                             });
                             if (isFound) {                            	
                                     isFound.itemId= item.itemId,
                                     isFound.itemCode= item.itemCode,
                                     isFound.itemName= item.itemName,
                                     isFound.serialNumber= item.serialNo,
                                     isFound.warrantyFrom= moment(item.warrantyFrom).format('YYYY-MM-DD'),
                                     isFound.warrantyTo= moment(item.warrantyTo).format('YYYY-MM-DD'),
                                     isFound.specification= item.specification,
                                     isFound.quantity= item.quantity,
                                     isFound.workingConditionId= item.workingCondition                                
                             }else {
                            	 obj.spares.push({
                                     "itemId": item.itemId,
                                     "itemCode": item.itemCode,
                                     "itemName": item.itemName,
                                     "serialNumber": item.serialNo,
                                     "warrantyFrom": moment(item.warrantyFrom).format('YYYY-MM-DD'),
                                     "warrantyTo": moment(item.warrantyTo).format('YYYY-MM-DD'),
                                     "specification": item.specification,
                                     "quantity": item.quantity,
                                     "workingConditionId": item.workingCondition
                                 })
                             }
                           //end code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
                        });
                        //add code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
                        obj.selectedSparesList=angular.copy(self.model.sparesList);
                    }
                });

                var popup = angular.element('#SparesAddModal');
                popup.modal('hide');
            }
        }

        var ViewAccessoryDetails = function (item, index) {
        	//var indexNo=self.model.fixedAssetList.indexOf(item);
            //self.model.fixedAssetList.find(function (x) {
              //  if (x.itemId == item.itemId) {
                    self.model.itemForAccessoryDetail = item;
               // }
           // });
        }

        var ViewSparesDetails = function (item) {
          //  self.model.fixedAssetList.find(function (x) {
             //   if (x.itemId == item.itemId) {
                    self.model.itemForSparesDetail = item;
              //  }
           // });
        }

        var AddInsuranceDetails = function (item,index) {
            self.model.currentItemForInsurance = item;
            $scope.indexNo=index;
            // self.model.insuranceCompanyId = null;
            // self.model.insuranceCompany = null;
            // self.model.policyNumber = null;
            // self.model.policyFromDate = null;
            // self.model.policyToDate = null;
            // self.model.amountInsured = null;
            // self.model.remark = null;
            
            /* start code for resolved HIS-10330 Fixed asset opening balance - Fixed asset insurance added to one line item shows in the second line item */
            let currentItemId = item.itemId
            FillItemInsuranceDetailsModal(currentItemId);
            /* end code for resolved HIS-10330 Fixed asset opening balance - Fixed asset insurance added to one line item shows in the second line item */

        }

        var SaveInsuranceDetails = function () {
            if (ValidateInsurance()) {
                self.model.fixedAssetList.forEach(function (obj) {
                	 var indexNo= self.model.fixedAssetList.indexOf(obj);
                    if (obj.itemId == self.model.currentItemForInsurance.itemId && $scope.indexNo == indexNo) {
                        obj.insurance.insuranceCompanyId = self.model.insuranceCompanyId ? self.model.insuranceCompanyId : 1;
                        obj.insurance.insuranceCompany = self.model.insuranceCompany;
                        obj.insurance.policyNumber = self.model.policyNumber;
                        obj.insurance.policyFromDate = self.model.policyFromDate;
                        obj.insurance.policyToDate = self.model.policyToDate;
                        obj.insurance.amountInsured = self.model.amountInsured;
                        obj.insurance.remark = self.model.remark;
                    }
                });

                var popup = angular.element('#insuranceDetailsAddModal');
                popup.modal('hide');
            }
        }

        var ViewInsuranceDetails = function (item) {

          //  self.model.fixedAssetList.find(function (x) {
              //  if (x.itemId == item.itemId) {
                    self.model.insuranceCompanyId = item.insuranceCompanyId;
                    self.model.insuranceCompany = item.insuranceCompany;
                    self.model.policyNumber = item.policyNumber;
                    self.model.policyFromDate = item.policyFromDate;
                    self.model.policyToDate = item.policyToDate;
                    self.model.amountInsured = parseFloat(item.amountInsured);
                    self.model.remark = item.remark;
              //  }
           // });

        }

        var RemoveAssetRow = function (index) {
            var fixedAssetList = self.model.fixedAssetList;
            self.model.fixedAssetList = fixedAssetList.slice(0, index).concat(fixedAssetList.slice(index + 1))
        }

        var RemoveAccessoryRow = function (index) {
            var accessoryList = self.model.accessoryList;
            self.model.accessoryList = accessoryList.slice(0, index).concat(accessoryList.slice(index + 1))
        }
        
        //start code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
        var RemoveAccessoryRowFromItem = function (index) {
            var accessoryList = self.model.currentItem.accesories;
            var selectedAccessoryList = self.model.currentItem.selectedAccesoriesList;
            
            self.model.currentItem.accesories = accessoryList.slice(0, index).concat(accessoryList.slice(index + 1))
            self.model.currentItem.selectedAccesoriesList = selectedAccessoryList.slice(0, index).concat(selectedAccessoryList.slice(index + 1))
        }
        //end code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data

        var RemoveSparesRow = function (index) {
            var sparesList = self.model.sparesList;
            self.model.sparesList = sparesList.slice(0, index).concat(sparesList.slice(index + 1))
        }
        
      //start code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
        var RemoveSparesRowFromItem = function (index) {            
            var sparesList = self.model.currentItem.spares;
            var selectedSparesList = self.model.currentItem.selectedSparesList;
            
            self.model.currentItem.spares = sparesList.slice(0, index).concat(sparesList.slice(index + 1))
            self.model.currentItem.selectedSparesList = selectedSparesList.slice(0, index).concat(selectedSparesList.slice(index + 1))
        }
      //end code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data

        var SetStatus = function (x) {
            var status = self.model.Status.find(function (obj) {
                return obj.status == x;
            });
            return status.id;
        }

        var SaveNewfixedAssetMaster = function () {
           
            if (self.model.fixedAssetId) {
                UpdatefixedAssetMaster();
            } else {
                CreateNewfixedAssetMaster();
            }
        }

        var CreateNewfixedAssetMaster = function () {
            if (Validate()) {
                var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.SAVE_API;

                var data = {
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": SetStatus('New'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "items": []
                }

                angular.forEach(self.model.fixedAssetList, function (item, key) {
                    var itemAccessories = [];
                    var itemSpares = [];
                    item.accesories.forEach(function (element) {
                        itemAccessories.push({
                            "itemId": element.itemId,
                            "serialNumber": element.serialNumber,
                            "warrantyFrom": element.warrantyFrom,
                            "warrantyTo": element.warrantyTo,
                            "specification": element.specification,
                            "quantity": element.quantity,
                            "workingConditionId": element.workingConditionId
                        });
                    });

                    item.spares.forEach(function (element) {
                        itemSpares.push({
                            "itemId": element.itemId,
                            "serialNumber": element.serialNumber,
                            "warrantyFrom": element.warrantyFrom,
                            "warrantyTo": element.warrantyTo,
                            "specification": element.specification,
                            "quantity": element.quantity,
                            "workingConditionId": element.workingConditionId
                        });
                    });

                    delete item.insurance.insuranceCompany;

                    var item = {
                        "itemId": item.itemId,
                        "quantity": item.quantity,
                        "specification": item.specification,
                        "statusId": SetStatus('New'),
                        "serialNumber": item.serialNo,
                        "yearOfManufacture": item.yearOfMAnufacture,
                        "vendorId": item.vendorId,
                        "serviceProviderId": item.serviceProviderId,
                        "warrantyFrom": moment(item.warrantyFrom).format('YYYY-MM-DD'),
                        "warrantyTo": moment(item.warrantyTo).format('YYYY-MM-DD'),
                        "pmsFrequencyId": item.pmsfrequencies.id,
                        "workingConditionId": item.workingCondition,
                        "accessories": itemAccessories,
                        "spares": itemSpares,
                        "insurance": item.insurance,
                        "cop":item.cop,
                        "mrp":item.mrp
                    }
                    data.items.push(item);
                });
                $rootScope.startLoader();
                NavigateToBack();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success('New fixed asset master successfully saved');
                         $state.go('addfixedAssetMaster');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.summary);
                    });
            }
        }


        var NavigateToBack = function () { 
            $state.go('addfixedAssetMaster');
        }
        var UpdatefixedAssetMaster = function () {
            if (Validate()) {
                var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.UPDATE_API + self.model.fixedAssetId;

                var data = {
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": SetStatus('New'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "items": []
                }

                angular.forEach(self.model.fixedAssetList, function (item, key) {


                         angular.forEach(item.accesories, function (a, key) {
                                delete a.serialNo
                             });

                              angular.forEach(item.spares, function (s, key) {
                                delete s.serialNo
                             });

                    var item = {
                        "id": item.id,
                        "itemId": item.itemId,
                        "quantity": item.quantity,
                        "specification": item.specification,
                        "statusId": SetStatus('New'),
                        "serialNumber": item.serialNo,
                        "yearOfManufacture": item.yearOfMAnufacture,
                        "vendorId": item.vendorId,
                        "serviceProviderId": item.serviceProviderId,
                        "warrantyFrom": moment(item.warrantyFrom).format('YYYY-MM-DD'),
                        "warrantyTo": moment(item.warrantyTo).format('YYYY-MM-DD'),
                        "pmsFrequencyId": item.pmsfrequencies.id,
                        "workingConditionId": item.workingConditionId == undefined ? item.workingCondition : item.workingConditionId ,
                        "accessories": item.accesories,
                        "spares": item.spares,
                        "insurance": item.insurance,
                        "cop":item.cop,
                        "mrp":item.mrp
                    }
                    data.items.push(item);
                });
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        growl.success('New fixed asset master successfully saved');
                        $state.go('addfixedAssetMaster');
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving new fixed asset master ');
                    });
            }
        }
        var ValidateSerialNo = function (item) {
            var isValid = true;
            if (!item.serialNo) {
                item.isSerialNoValid = true;
                isValid = false;
                growl.error("Please Enter Serial No.");
            }

            return isValid;
        }
        var ValidateYearOfManufacture = function (item) {
            var isValid = true;
            if (!item.yearOfMAnufacture) {
                item.isYearOfMAnufactureValid = true;
                isValid = false;
                growl.error("Please Select Year of Manufacture");
            }

            return isValid;
        }
        var ValidateVendor = function (item) {
            var isValid = true;
            if(item.id){
                if (!item.vendor) {
                    item.isVendorValid = true;
                    isValid = false;
                    item.vendor = null;
                    growl.error("Please  Select valid Vendor");
                }
            }
            else{
                if (!item.vendorId) {
                    item.isVendorValid = true;
                    isValid = false;
                    item.vendor = null;
                    growl.error("Please  Select valid Vendor");
                }
            }
            return isValid;
        }

        var ValidateServiceProvider = function (item) {
            var isValid = true;
            if(item.id){    
                if (!item.serviceProvider) {
                    item.isServiceProviderValid = true;
                    isValid = false;
                    item.serviceProvider = null;
                    growl.error("Please  Select valid Service Provider");
                }
            }
            else{        
                if (!item.serviceProviderId) {
                    item.isServiceProviderValid = true;
                    isValid = false;
                    item.serviceProvider = null;
                    growl.error("Please  Select valid Service Provider");
                }
            }
            return isValid;
        }
        var ValidateWarrantyFrom = function (item) {
            var isValid = true;
            if (!item.warrantyFrom) {
                item.isWarrantyFromValid = true;
                isValid = false;
                growl.error("Please Select Warranty From");
            }

            return isValid;
        }
        var ValidateWarrantyTo = function (item) {
            var isValid = true;
            if (!item.warrantyTo) {
                item.isWarrantyToValid = true;
                isValid = false;
                growl.error("Please Select Warranty To");
            }

            return isValid;
        }
        var ValidateQuantity = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.quantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Quantity");
            } else if (parseInt(item.quantity) == 0) {
                item.isQuantityValid = true; v
                isValid = false;
                growl.error('Quantity cannot be zero');
            } else {
                item.quantity = parseInt(item.quantity);
                isValid = true;
                // isValid = ValidateMaxOrderQuantity(item);
            }

            return isValid;
        }

        var ValidatePMSFrequency = function (item) {
            var isValid = true;
            if (!item.pmsfrequencies) {
                item.isPmsfrequenciesValid = true;
                isValid = false;
                growl.error("Please Select PMS Frequency");
            }
            return isValid;
        }
        var ValidateDates = function (item) {
            var isValid = true;
            console.log("Warranty From : ",item.warrantyFrom);
            console.log("Warranty To : ",item.warrantyTo);
            if(item.warrantyFrom && item.warrantyTo)
            {
                if (item.warrantyFrom > item.warrantyTo || item.warrantyTo < item.warrantyFrom) {
                    isValid = false;
                    growl.error("Warranty From Should be less than Warranty To");
                }
            }

            return isValid;
        }
        var ValidateWorkingCondition = function (item) {
            var isValid = true;
            if (!item.workingCondition) {
                item.isWorkingConditionValid = true;
                isValid = false;
                growl.error("Please Select Working Condition");
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
        var Validate = function () {
            var isValid = true;

            var fixedAssetList = self.model.fixedAssetList;
            if (!fixedAssetList || fixedAssetList.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the bill");
            } else {
                fixedAssetList.forEach(function (item) {
                    if (!ValidatePMSFrequency(item)) {
                        isValid = false
                    }
                    if (!ValidateWorkingCondition(item)) {
                        isValid = false
                    }
                    /*if (!ValidateSpecification(item)) {
                        isValid = false
                    }*/
                    if (!ValidateSerialNo(item)) {
                        isValid = false
                    }
                    if (!ValidateYearOfManufacture(item)) {
                        isValid = false
                    }
                    if (!ValidateVendor(item)) {
                        isValid = false
                    }
                    if (!ValidateServiceProvider(item)) {
                        isValid = false
                    }
                    if (!ValidateWarrantyFrom(item)) {
                        isValid = false
                    }
                    if (!ValidateWarrantyTo(item)) {
                        isValid = false
                    }
                    if (!ValidateQuantity(item)) {
                        isValid = false
                    }
                    if (!ValidateDates(item)) {
                        isValid = false
                    }
                    // if(!ValidateInsurances(item)){
                    //     isValid = false
                    // }
                });
            }
            return isValid;
        }

        var ValidateAccesories = function () {
            var isValid = true;

            var accessoryList = self.model.accessoryList;
            if (!accessoryList || accessoryList.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
            } else {
                accessoryList.forEach(function (item) {

                    if (!item.serialNo || !item.warrantyFrom || !item.warrantyTo || !item.quantity || !item.workingCondition || !item.specification) {
                        isValid = false;
                        growl.error('Please fill all the fields');
                    }

                    if (!item.serialNo) {
                        item['isSerialNoValid'] = true;
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

        var ValidateSpares = function () {
            var isValid = true;

            var sparesList = self.model.sparesList;
            if (!sparesList || sparesList.length <= 0) {
                isValid = false;
                growl.error("Please add some items in the list");
            } else {
                sparesList.forEach(function (item) {

                    if (!item.serialNo || !item.warrantyFrom || !item.warrantyTo || !item.quantity || !item.workingCondition || !item.specification) {
                        isValid = false;
                        growl.error('Please fill all the fields');
                    }

                    if (!item.serialNo) {
                        item['isSerialNoValid'] = true;
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

        var ValidateInsurance = function () {
            var isValid = true;

            if (!self.model.insuranceCompany || !self.model.policyNumber || !self.model.policyFromDate || !self.model.policyToDate || !self.model.amountInsured || !self.model.remark) {
                isValid = false;
                growl.error('Please fill all the fields')
            }

            if (!self.model.insuranceCompany) {
                self.model.isInsuranceCompanyValid = true;
                isValid = false;
            }

            if (!self.model.policyNumber) {
                self.model.ispolicyNumberValid = true;
                isValid = false;
            }

            if (!self.model.policyFromDate) {
                self.model.isPolicyFromDateValid = true;
                isValid = false;
            }

            if (!self.model.policyToDate) {
                self.model.isPolicyToDateValid = true;
                isValid = false;
            }
          //start code to change for resolved HIS-9719 Fixed asset opening balance - Insurance policy to date should be greater than from date
            if (self.model.policyFromDate > self.model.policyToDate) {
                isValid = false;
                //growl.error("Policy From Date Should be less than Policy To Date");
                growl.error("Policy To Date Should be greater than Policy From Date");
            }
          //end code to change for resolved HIS-9719 Fixed asset opening balance - Insurance policy to date should be greater than from date
            if (!self.model.amountInsured) {
                self.model.isAmountInsuredValid = true;
                isValid = false;
            }

            if (!self.model.remark) {
                self.model.isRemarkValid = true;
                isValid = false;
            }
            return isValid;
        }

        // var ValidateInsurances = function (item) {
        //     var isValid = true;
        //     var insurance = Object.keys(item.insurance).length;
        //     if (insurance === 0) {
        //         isValid = false;
        //         growl.error("Please add insurance");
        //     }

        //     return isValid;
        // }

        var GetFixedAssetMasterDetails = function (fixedAssetId) {
            var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.DETAIL_API  + $scope.unitId + '/'+$scope.organizationId + '/' + fixedAssetId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.fixedAssetList = response.data.items;
                    self.model.store = self.model.stores.find(function (item) {
                        return response.data.storeId == item.id;
                        //self.model.store.id = response.data.storeId;
                    });
                    response.data.items.forEach(function (item) {
                        self.model.accessoryList = item.accesories;
                        self.model.sparesList = item.spares;
                        /*item.yearOfMAnufacture = self.model.yearList.find(function (year) {
                            return item.yearOfMAnufacture == year.yearOfMAnufacture;
                        });
                        item.pmsfrequencies = self.model.pmsfrequencies.find(function (pms) {
                            return item.pmsfrequencies == pms.pmsfrequencies;
                        });
                        item.workingCondition = self.model.workingConditions.find(function (workingCondition) {
                            return item.workingConditionId == workingCondition.id;
                        });*/
                        
                      //start code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
                        angular.forEach(self.model.accessoryList,function(accessory){
                        	accessory.serialNo = accessory.serialNumber
                        	accessory.workingCondition = self.model.workingConditions.find(function (workingCondition) {
                                return accessory.workingConditionId == workingCondition.id;
                            }).id;
                        });
                        item.selectedAccesoriesList=angular.copy(self.model.accessoryList);
                        
                        angular.forEach(self.model.sparesList,function(spares){
                        	spares.serialNo = spares.serialNumber
                        	spares.workingCondition = self.model.workingConditions.find(function (workingCondition) {
                                return spares.workingConditionId == workingCondition.id;
                            }).id;
                        });
                        item.selectedSparesList=angular.copy(self.model.sparesList);
                        
                        item.yearOfMAnufacture = self.model.yearList.find(function (year) {
                        	return item.yearOfManufacture == year;
                        });                        
                        item.pmsfrequencies = self.model.pmsfrequencies.find(function (pms) {
                        	return item.pmsfrequencyid == pms.id;
                        });                               
                        item.workingCondition = self.model.workingConditions.find(function (workingCondition) {
                            return item.workingConditionId == workingCondition.id;
                        }).id;
                      //end code for resolved HIS-9722 Fixed asset opening balance - Edit mode removes data
                        
                    });
                    // self.model.insuranceCompany = response.data.items.insuranceDetails[0].insuranceCompany;
                    // self.model.insuranceCompanyId = response.data.items.insuranceDetails[0].insuranceCompanyId;
                    // self.model.policyNumber = response.data.items.insuranceDetails[0].policyNumber;
                    // self.model.policyFromDate = response.data.items.insuranceDetails[0].policyFromDate;
                    // self.model.policyToDate = response.data.items.insuranceDetails[0].policyToDate;
                    // self.model.amountInsured = response.data.items.insuranceDetails[0].amountInsured;
                    // self.model.remark = response.data.items.insuranceDetails[0].remark;

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


        var SetOldValueStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeFromStore = function (store) {
            if (!!self.model.fixedAssetList.length) {
                self.model.store = self.model.oldStore;
                self.model.storeNew = JSON.parse(JSON.stringify(store));
                $("#storeChange").modal()
            }
        }

        var ConfirmStorePopup = function () {
            self.model.store = self.model.storeNew;
            self.model.fixedAssetList = [];
            ModalDismiss('storeChange');

        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var ConfirmRemove = function (item) {
            var popup = angular.element('#confirmRemove');
            popup.modal('show');
            self.model.itemForRemove = item;
        }
        function removeRowData() {
            var index = self.model.fixedAssetList.indexOf(self.model.itemRemove);
            self.model.fixedAssetList.splice(index, 1);
            var popup = angular.element('#confirmRemove');
            popup.modal('hide');
        }
        
        /* start code for resolved HIS-10330 Fixed asset opening balance - Fixed asset insurance added to one line item shows in the second line item */
        function FillItemInsuranceDetailsModal(currentItemId){        	
        	self.model.fixedAssetList.find(function (x) {
                if (x.itemId == currentItemId) {
                    self.model.insuranceCompanyId = x.insurance.insuranceCompanyId;
                    self.model.insuranceCompany = x.insurance.insuranceCompany;
                    self.model.policyNumber = x.insurance.policyNumber;
                    self.model.policyFromDate = x.insurance.policyFromDate;
                    self.model.policyToDate = x.insurance.policyToDate;
                    self.model.amountInsured = parseFloat(x.insurance.amountInsured);
                    self.model.remark = x.insurance.remark;
                }
            });
        }
        /* end code for resolved HIS-10330 Fixed asset opening balance - Fixed asset insurance added to one line item shows in the second line item */
        

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewfixedAssetMaster", {
            url: "/addNewfixedAssetMaster",
            templateUrl: 'views/master/UnitMaster/assetManagement/add-new-fixed-asset-master.html',
            controller: "AddNewfixedAssetMaster.Controller",
            controllerAs: "vm",
            title: 'Add Fixed Asset Opening Balance'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewfixedAssetMaster.Controller", addNewFixedAssetController);
})();