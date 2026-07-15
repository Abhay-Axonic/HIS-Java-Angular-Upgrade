(function () {
    "use strict";

    function stockAdjustmentDetailsController($scope, $rootScope, $http,$filter, commonDetailService,PaginationService, CurrencyService,  CONSTANTS, $state, GenericService, growl,BillingGenericService,reportService,$cookies,fileReader,GenericServiceFileUpload) { 
        var self = this;
        var uploadId;
        var flag;
        var stopTransactionId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
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
                stores: [{
                    "store": "Select Store"
                }],
                reqAssetTypeId: 1,             
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategories: [{
                    "category": "Select Product Category"
                }],
                SelectedItemsIds: [],
                selectAllRows: false,   
                asseteTypeId: 1,
            };
            
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.SelectedItemsIds = [];
            self.model.ActiveTab = 1;
            self.model.PopulateDropdown = PopulateDropdown;   
            self.model.searchNewItem = searchNewItem;
            self.model.searchItemList = searchItemList;
            self.model.saveStockAdjustment = saveStockAdjustment;
            self.model.NavigateToBack = NavigateToBack;
            self.model.selectAllRowsStopItem = false;
            self.model.SelectAllStopItem = SelectAllStopItem;
            self.model.SelectItemStopItem = SelectItemStopItem;
            self.model.SetOldValueAssetType = SetOldValueAssetType;
            self.model.GetItemList = GetItemList;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.SearchItems = SearchItems;
            self.model.AddItemToBill = AddItemToBill;
            self.model.calculateVariance = calculateVariance;
            self.model.SelectedStopItems = [];
            self.model.pagination.data = [];
            self.model.ChangeFromAssetType = ChangeFromAssetType;
            PopulateDropdown();
        }      

        var ConfirmRemove = function (item) {
            var newArray = [];

            self.model.pagination.data.map(function (obj) {
                if (obj.batchId !== item.batchId) {
                newArray.push(obj);
                }
            });
            self.model.pagination.data = newArray;
        }

        var SearchItems = function (search) {
            if ((!self.model.prevSearch || self.model.prevSearch != search) ) {
               self.model.prevSearch = search;

                var URI = CONSTANTS.GLOBAL.ITEM_API  + $scope.unitId+ "/" + false + "/" + search + "/" + $cookies.getObject('cookieObject').storeId +"/asset/"+self.model.reqAssetTypeId;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                .then(function (response) {
                    $rootScope.stopLoader();
                    if (!!response.data && response.data.length > 0) {
                        self.model.items = response.data;
                        return $filter('filter')(self.model.items, {
                           $: search
                        });
                    } else {
                        NoItemFound(search);
                        return self.model.items;
                    }
                }, function (err) {
                    NoItemFound(search);
                    $rootScope.stopLoader();
                    return self.model.items;
                });
            } else {
                if (!!self.model.items && self.model.items.length > 0 &&
                    self.model.items[0].itemFound != undefined && !self.model.items[0].itemFound) {
                    return NoItemFound(search);
                } else {
                    return $filter('filter')(self.model.items, {
                        $: search
                    });
                }
            }
        }

        var NoItemFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "No item found with item code or name " + search
            };
            self.model.items = [item];
        }

     	$scope.buttonDisabled = true;
        var AddItemToBill = function (item) {
        	
         	$scope.buttonDisabled = false;
            var itemname = item.split('| ');
            var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/" + $scope.unitId;                 
            var data = {
                "assetTypeId": self.model.reqAssetTypeId,
                "storeId": self.model.store.id,
                "itemName": itemname[1],
                "screenName":"Stock Adjustment"
            };
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoaderPopup();
                angular.forEach(response.data, function(item){
                    var obj ={
                        expiryDate: '',
                        avilableQuantity: '',
                        assetCategory: '',
                        assetCategoryId: '',
                        batchId: '',
                        batchNo: '',
                        conversion: '',
                        cop: '',
                        genericName: '',
                        isBatchRequired: '',
                        itemCode: '',
                        itemDetails: '',
                        itemId: '',
                        itemName: '',
                        markupStatus: '',
                        mrp: '',
                        prodCatId: '',
                        productCategory: '',
                        productCategoryId: '',
                        taxId: '',
                        taxName: '',
                        taxPercentage: '',
                        uomResponses: [],
                        uomType: '',
                        uomTypeId: '',
                        uomUnit: '',
                        uomUnitId: ''
                    }
                    obj.expiryDate = item.expiry;
                    obj.avilableQuantity = item.availableQuantity;
                    obj.assetCategory = item.assetCategory;
                    obj.assetCategoryId = item.assetCategoryId;
                    obj.batchId = item.batchId;
                    obj.batchNo = item.batchNo;
                    obj.conversion = item.conversion;
                    obj.cop = item.cop;
                    obj.genericName = item.genericName;
                    obj.isBatchRequired = item.isBatchRequired;
                    obj.itemCode = item.itemCode;
                    obj.itemDetails = item.itemDetails;
                    obj.itemId = item.itemId;
                    obj.itemName = item.itemName;
                    obj.markupStatus = item.markupStatus;
                    obj.mrp = item.mrp;
                    obj.prodCatId = item.prodCatId;
                    obj.productCategory = item.productCategory;
                    obj.productCategoryId = item.productCategoryId;
                    obj.taxId = item.taxId;
                    obj.taxName = item.taxName;
                    obj.taxPercentage = item.taxPercentage;
                    obj.uomResponses = item.uomResponses;
                    obj.uomType = item.uomType;
                    obj.uomTypeId = item.uomTypeId;
                    obj.uomUnit = item.uomUnit;
                    obj.uomUnitId = item.uomUnitId;
                    obj.isThreeUOMApplicable = item.isThreeUOMApplicable;
                    obj.originalCop=item.cop;
                    obj.originalMrp=item.mrp;
                    self.model.pagination.data.push(obj);
                });
                $scope.item.itemName = '';
                var cleaned = [];
                self.model.pagination.data.forEach(function(itm) {
                    if(itm.uploadedQuantity == undefined || itm.uploadedQuantity == null){
                        itm.uploadedQuantity = 0;
                    }
                    if(itm.uomResponses != undefined && itm.uomResponses != null && itm.uomResponses.length != 0){
                        itm.uomResponses.find(function(u){
                            if(u.uomType == itm.uomType){
                                itm.uomType = u.uomDetails;
                            }
                        })
                        if(itm.isThreeUOMApplicable == false){
                            if(itm.uomTypeId == 1){
                                itm.receivedQtyCon = (itm.uploadedQuantity +"*"+ itm.uomResponses[0].conversion);
                                itm.receivedQtyCalculation = (itm.uploadedQuantity * itm.uomResponses[0].conversion);
                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
                            }
                            else if(itm.uomTypeId == 2){
                                
                            }
                            else{
                                itm.receivedQtyCon = (itm.uploadedQuantity);
                                itm.receivedQtyCalculation = (itm.uploadedQuantity);
                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
                            }
                        }
                        else{
                            if(itm.uomTypeId == 1){
                                itm.receivedQtyCon = (itm.uploadedQuantity +"*"+ itm.uomResponses[0].conversion +"*"+ itm.uomResponses[1].conversion);
                                itm.receivedQtyCalculation = (itm.uploadedQuantity * itm.uomResponses[0].conversion * itm.uomResponses[1].conversion);
                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
                            }
                            else if(itm.uomTypeId == 2){
                                itm.receivedQtyCon = (itm.uploadedQuantity +"*"+ itm.uomResponses[1].conversion);
                                itm.receivedQtyCalculation = (itm.uploadedQuantity * itm.uomResponses[1].conversion);
                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
                            }
                            else{
                                itm.receivedQtyCon = (itm.uploadedQuantity);
                                itm.receivedQtyCalculation = (itm.uploadedQuantity);
                                itm.uomUnitData = itm.uomResponses[2].uomUnit;
                            }
                        }
                    }
                    var unique = true;
                    cleaned.forEach(function(itm2) {
                        if (_.isEqual(itm.batchId, itm2.batchId)) unique = false;
                    });
                    if (unique)  cleaned.push(itm);
                    var uomResponse = itm.uomResponses !== undefined && itm.uomResponses !== null ? itm.uomResponses : itm.uomList;
                    var originalCop = itm.originalCop !== undefined && itm.originalCop !== null ? itm.originalCop : itm.cop;
                    var originalMrp = itm.originalMrp !== undefined && itm.originalMrp !== null ? itm.originalMrp : itm.mrp;
                    itm.cop = commonDetailService.calculateRate(uomResponse, itm.uomTypeId, originalCop);
                    itm.mrp = commonDetailService.calculateRate(uomResponse, itm.uomTypeId, originalMrp);
                    calculateVariance(itm);
                });
                self.model.pagination.data= cleaned;
            },
            function errorCallback(response) {
                $rootScope.stopLoaderPopup();
                growl.error('Error');
            });
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
            	self.model.itemListData.forEach(function (item) {
            		$scope.list.push(item);
            	})
                if(self.model.SelectedItemsIds){
                    self.model.data = self.model.itemListData.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedItemsIds.push(item.itemId);
                        return self.model.SelectedItemsIds;
                    });
                }
                else{
                    self.model.SelectedItemsIds = self.model.itemListData.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        return item.itemId;
                    });
                }
            } else {
                self.model.SelectedItemsIds = [];
                self.model.itemListData.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        $scope.list = [];
        var SelectItem = function (item) {
            $scope.list.push(item);
            if (item.isSelected) {
                self.model.SelectedItemsIds.push(item.itemId);
            } else {
                var index = self.model.SelectedItemsIds.indexOf(item.itemId);
                if (index > -1) {
                    self.model.SelectedItemsIds.splice(index, 1);
                }
            }

            if (self.model.itemListData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }

            self.model.isItemChecked = false;
            self.model.itemListData.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelected = function (item) {
            return item.isSelected;
        }

        $scope.buttonDisabled = true;
        function getAllItemDetails() {
        	$scope.buttonDisabled = false;
            if($scope.list.length > 0){
                $scope.list.forEach(function (item) {
                    self.model.SelectedItemsIds.forEach(function (sitem) {
                        if (item.itemId == sitem && item.isSelected == true) {
                            item.avilableQuantity = item.availableQuantity;
                            if(item.uploadedQuantity==undefined && item.uploadedQuantity==null)
                            	{
                            	item.uploadedQuantity = 0;
                            	}
                            item.cop = commonDetailService.calculateRate(item.uomResponses, item.uomTypeId, item.originalCop);
                            item.mrp = commonDetailService.calculateRate(item.uomResponses, item.uomTypeId, item.originalMrp);
                            item.expiryDate = item.expiry;
                            item.varianceQty = item.avilableQuantity - item.uploadedQuantity;
                            item.varianceCOP = item.avilableQuantity * item.cop - item.uploadedQuantity * item.cop;
                            item.varianceMRP = item.avilableQuantity * item.mrp - item.uploadedQuantity * item.mrp;
                            if(self.model.reqAssetTypeId != 2){
                            	if(item.uomResponses!=undefined && item.uomResponses!=null)
                                {
                                item.uomResponses.find(function(u){
                                    if(u.uomType == item.uomType){
                                    item.uomType = u.uomDetails;
                                    }
                                })
                                if(item.isThreeUOMApplicable == false){
                                    if(item.uomTypeId == 1){
                                        item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomResponses[0].conversion);
                                        item.receivedQtyCalculation = (item.uploadedQuantity * item.uomResponses[0].conversion);
                                        if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                                    	{
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    	}
                                    }
                                    else if(item.uomTypeId == 2){
                                        
                                    }
                                    else{
                                        item.receivedQtyCon = (item.uploadedQuantity);
                                        item.receivedQtyCalculation = (item.uploadedQuantity);
                                        if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                                    	{
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    	}
                                    }
                                }
                                else{
                                    if(item.uomTypeId == 1){
                                        item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomResponses[0].conversion +"*"+ item.uomResponses[1].conversion);
                                        item.receivedQtyCalculation = (item.uploadedQuantity * item.uomResponses[0].conversion * item.uomResponses[1].conversion);
                                        if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                                    	{
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    	}
                                    }
                                    else if(item.uomTypeId == 2){
                                        item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomResponses[1].conversion);
                                        item.receivedQtyCalculation = (item.uploadedQuantity * item.uomResponses[1].conversion);
                                        if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                                    	{
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    	}
                                    }
                                    else{
                                        item.receivedQtyCon = (item.uploadedQuantity);
                                        item.receivedQtyCalculation = (item.uploadedQuantity);
                                        if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                                    	{
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    	}
                                    }
                                }
                            		}
                            }
                            self.model.pagination.data.push(item);
                        }
                    });
                });
                var cleaned = [];
                self.model.pagination.data.forEach(function(itm) {
                    var unique = true;
                    cleaned.forEach(function(itm2) {
                        if (_.isEqual(itm.batchId, itm2.batchId)) unique = false;
                    });
                    if (unique)  cleaned.push(itm);
                });
                self.model.pagination.data= cleaned;
            }
        }

        var calculateVariance = function(item){
            if(item.uploadedQuantity == undefined){
                item.uploadedQuantity = 0;
            }
            item.varianceQty = item.avilableQuantity - item.uploadedQuantity;
            item.varianceCOP = item.avilableQuantity * item.cop - item.uploadedQuantity * item.cop;
            item.varianceMRP = item.avilableQuantity * item.mrp - item.uploadedQuantity * item.mrp;
        }

        function GetItemList() {
            self.model.selectAllRows = false;
            self.model.action = 'Open';
            self.model.itemCode='';
            self.model.itemName='';
            self.model.batchNo='';
            var popup = angular.element('#additemlist');
            popup.modal('show');
            self.model.isItemChecked = false;
            self.model.selectAllRows = false;
            self.model.search = {};
            GetAssetCategoryList();
            self.model.itemListData = [];
        }

        var GetAssetCategoryList = function () {
            var consumption = self.model;

            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.reqAssetTypeId + "/"+$scope.unitId + "/"+$scope.organizationId ;

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
            consumption.productCategory = consumption.productCategories[0];

            consumption.productCategories.splice(1);
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

        var SetOldValueAssetType = function (typeId) {
            self.model.oldAssetTypeId = typeId;
            self.model.assetType = self.model.oldAssetTypeId;
        }

        var ChangeFromAssetType = function (typeId) {
            self.model.reqAssetTypeId = typeId;
            self.model.prevSearch='';
            self.model.pagination.data=[];
            if (!!self.model.pagination.data.length) {
             
            } else {
              self.model.asseteTypeId = typeId;
            }
          }
        
        var isSelected = function (item) {
            return item.isSelected;
        }

        var SelectItemStopItem = function (item) {
            var items = self.model.SelectedStopItems;
            
            if (item.isSelected) {
            	items.push(item);
            } else {
                var index = items.indexOf(item);
                if (index > -1) {
                	items.splice(index, 1);
                }
            }

            if (self.model.itemDetailList.every(isSelected)) {
                self.model.selectAllRowsStopItem = true;
            } else {
                self.model.selectAllRowsStopItem = false;
            }
            self.model.isItemChecked = false;
            self.model.itemDetailList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }        

        var SelectAllStopItem = function () {
            if (self.model.selectAllRowsStopItem) {
                self.model.SelectedStopItems = self.model.itemDetailList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item;
                });
            } else {
                self.model.SelectedStopItems = [];
                self.model.itemDetailList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;

                });
            }
        } 
        
        var PopulateDropdown = function () {
            var upload = self.model;

            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
            upload.store = upload.stores[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                upload.stores = upload.stores.concat(response.data);
                try{
                    var selectedTopStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
                    $scope.storeId = selectedTopStore.storeId;
                    self.model.store  =upload.stores.filter(x=>x.id==$scope.storeId)[0];            		 
                }catch (e) {
                    console.log("error!",e);          
                }                     
                $rootScope.stopLoader();
                var uploadData = commonDetailService.getDataId();
                var getflag=commonDetailService.getDataFlag();
                if(uploadData !=null){
                uploadId = uploadData.id;
                flag=getflag.flag;
                self.model.isDetail = uploadData.isDetail;
                $rootScope.stopLoader();
                if(uploadData.isDetail){
                if (!!uploadId) {
                    commonDetailService.setDataId(null);
                    GetUploadStockDetails(uploadId,flag);
                } else {
                    return false;
                }
                }
                }
            },
            function (err) {
                $rootScope.stopLoader();
            });
        }

        function searchNewItem(getAllItemFlag) {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.ZERO_STOCK_SEARCH;                   
            var data = {
                "fromDate": moment(new Date()).format('YYYY-MM-DD'),
                "toDate": moment(new Date()).format('YYYY-MM-DD'),
                "assetTypeId": self.model.reqAssetTypeId,
                "storeId": self.model.store.id,
                "vedId": [],
                "unitId": $scope.unitId,
                "itemName": getAllItemFlag==true ? undefined :self.model.itemName,
                "batchNo": self.model.batchNo
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoader();
                self.model.itemDetailList = response.data;

                var totalAmount = 0;
                angular.forEach(self.model.itemDetailList, function (item, key) {
                	item.originalCop=item.cop;
                	item.originalMrp=item.mrp;
                    totalAmount = totalAmount + item.totalMrp;
                    item.cop = parseInt(item.cop).toFixed(2);
                    item.mrp = parseInt(item.mrp).toFixed(2);
                    item.uom.find(function(u){
                        if(u.uomType == item.uomType){
                           item.uomType = u.uomDetails;
                        }
                    })
                    if(item.uploadedQuantity == undefined || item.uploadedQuantity == null){
                        item.uploadedQuantity = 0;
                    }
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uom[0].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uom[0].conversion);
                            item.uomUnitData = item.uom[2]!=undefined && item.uom[2]!= null ? item.uom[2].uomUnit : item.uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            
                        }
                        else{
                            item.receivedQtyCon = (item.uploadedQuantity);
                            item.receivedQtyCalculation = (item.uploadedQuantity);
                            item.uomUnitData = item.uom[2]!=undefined && item.uom[2]!= null ? item.uom[2].uomUnit : item.uomUnit;
                        }
                    }
                    else{
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uom[0].conversion +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uom[0].conversion * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2]!=undefined && item.uom[2]!= null ? item.uom[2].uomUnit : item.uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uom[1].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uom[1].conversion);
                            item.uomUnitData = item.uom[2]!=undefined && item.uom[2]!= null ? item.uom[2].uomUnit : item.uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.uploadedQuantity);
                            item.receivedQtyCalculation = (item.uploadedQuantity);
                            item.uomUnitData = item.uom[2]!=undefined && item.uom[2]!= null ? item.uom[2].uomUnit : item.uomUnit;
                        }
                    }
                    if(item.uomResponses == undefined || item.uomResponses == null){
                    	item.uomResponses = item.uom;
                    }
                    item.cop = commonDetailService.calculateRate(item.uomResponses, item.uomTypeId, item.originalCop);
                    item.mrp = commonDetailService.calculateRate(item.uomResponses, item.uomTypeId, item.originalMrp);
                    calculateVariance(item);
                });
                //if(!!response && !!response.data && response.data.length > 0){            
                 //   PaginationService.searchData(self.model.itemDetailList, self.model.pagination);
               // }
               self.model.pagination.data = self.model.itemDetailList;
                self.model.newItemList = [];
                self.model.newItemList = self.model.itemDetailList;
                self.model.vedId = [];
            },
            function errorCallback(response) {
                $rootScope.stopLoader();
                growl.error('Error');
            });
        }
$scope.itemStoreList =[];
function searchItemList() {
    if (
        (self.model.itemName != undefined && self.model.itemName.replace(/\s/g, "").length == 2) ||
        (self.model.itemCode != undefined && self.model.itemCode.replace(/\s/g, "").length == 2) ||
        (self.model.batchNo != undefined && self.model.batchNo.replace(/\s/g, "").length == 2)
    ) {
        $rootScope.startLoaderPopup();
        self.model.itemListData = [];

        var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/" + $scope.unitId;
        var data = {
            "assetTypeId": self.model.reqAssetTypeId,
            "storeId": self.model.store.id,
            "assetCategoryId": self.model.assetCategory ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory ? self.model.productCategory.id : undefined,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "batchName": self.model.batchNo,
            "screenName": "Stock Adjustment"
        };

        GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                self.model.itemListData = response.data;
                $scope.itemStoreList =[];
                angular.forEach(self.model.itemListData, function (item) {
                    item.originalCop = item.cop;
                    item.originalMrp = item.mrp;

                    if (self.model.reqAssetTypeId !== 2) {
                        if (item.uomResponses && item.uomResponses.length > 0) {
                            item.uomResponses.find(function (u) {
                                if (u.uomType === item.uomType) {
                                    item.uomType = u.uomDetails;
                                }
                            });

                            if (!item.isThreeUOMApplicable) {
                                if (item.uomTypeId === 1) {
                                    item.receivedQtyCon = `${item.uploadedQuantity}*${item.uomResponses[0].conversion}`;
                                    item.receivedQtyCalculation = item.uploadedQuantity * item.uomResponses[0].conversion;

                                    if (item.uomResponses[2]) {
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    }
                                } else if (item.uomTypeId === 2) {
                                    // Add logic for type 2 if needed
                                } else {
                                    item.receivedQtyCon = item.uploadedQuantity;
                                    item.receivedQtyCalculation = item.uploadedQuantity;

                                    if (item.uomResponses[2]) {
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    }
                                }
                            } else {
                                if (item.uomTypeId === 1) {
                                    item.receivedQtyCon = `${item.uploadedQuantity}*${item.uomResponses[0].conversion}*${item.uomResponses[1].conversion}`;
                                    item.receivedQtyCalculation = item.uploadedQuantity * item.uomResponses[0].conversion * item.uomResponses[1].conversion;

                                    if (item.uomResponses[2]) {
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    }
                                } else if (item.uomTypeId === 2) {
                                    item.receivedQtyCon = `${item.uploadedQuantity}*${item.uomResponses[1].conversion}`;
                                    item.receivedQtyCalculation = item.uploadedQuantity * item.uomResponses[1].conversion;

                                    if (item.uomResponses[2]) {
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    }
                                } else {
                                    item.receivedQtyCon = item.uploadedQuantity;
                                    item.receivedQtyCalculation = item.uploadedQuantity;

                                    if (item.uomResponses[2]) {
                                        item.uomUnitData = item.uomResponses[2].uomUnit;
                                    }
                                }
                            }
                        }
                    }

                     $scope.itemStoreList.push(item);
                });

                $rootScope.stopLoaderPopup();
            },
            function errorCallback(response) {
                $rootScope.stopLoaderPopup();
                growl.error("Error");
            }
        );
    } else if (self.model.itemName && self.model.itemName.replace(/\s/g, "").length > 0) {
        self.model.itemListData = $filter('filter')($scope.itemStoreList, {
            $: self.model.itemName
        });
    } else if (self.model.itemCode && self.model.itemCode.replace(/\s/g, "").length > 0) {
        self.model.itemListData = $filter('filter')($scope.itemStoreList, {
            $: self.model.itemCode
        });
    } else if (self.model.batchNo && self.model.batchNo.replace(/\s/g, "").length > 0) {
        self.model.itemListData = $filter('filter')($scope.itemStoreList, {
            $: self.model.batchNo
        });
    }
}


        $scope.validateQty = function (){
            var isvalid = true;
            if(self.model.pagination.data.length == 0){
                isvalid = false;
                growl.error("Please add item");
            }
            angular.forEach(self.model.pagination.data, function (item, key) {
                if(!item.id){
                    // item.isSelected == true &&
                    if(item.uploadedQuantity==undefined || item.uploadedQuantity==null || item.uploadedQuantity < 0){
                        isvalid = false;
                        growl.error('Please enter uploaded quantity');
                    }
                }
                if(item.id){
                    if(item.uploadedQuantity==undefined || item.uploadedQuantity==null || item.uploadedQuantity<0){
                        isvalid = false;
                        growl.error('Please enter uploaded quantity');
                    }
                }

                // if(item.isSelected == false || item.isSelected == undefined){
                //     isvalid = false;
                //     growl.error("Select Items to Update and Save");
                // }
                if(!item.id){
                    // item.isSelected == undefined && 
                    if(item.uploadedQuantity==undefined || item.uploadedQuantity==null || item.uploadedQuantity < 0){
                        isvalid = false;
                        growl.error('Please enter uploaded quantity');
                    }
                }
            });
            return isvalid;
        }

        var saveStockAdjustment = function () {
            if($scope.validateQty()){
                var store = self.model;
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.SAVE_API;
                var data = {
                    "storeId": store.store.id,
                    "statusId": 1,
                    "approvalStatusId": 1,
                    "stopTransactionId": self.model.stopTransactionId,
                    "items": [],
                    "organizationId": $scope.organizationId,
                    "unitId": $scope.unitId,
                    "userId": $scope.userId,
                    "id":$scope.idToUpdate,
                    "remark":self.model.GRNRemark
                };
                store.itemDetailList = self.model.pagination.data;
                if(store.itemDetailList.length<=0)
              {
                      growl.error("Please select atleast one item");
              }else{
            	  angular.forEach(store.itemDetailList, function (item, key) {
                    if(item.isSelected == true || item.isSelected == undefined ){    
                        var storeItem = {
                            "batchId": item.batchId,
                            "uomTypeId": item.uomTypeId,
                            "uomUnitId": item.uomUnitId,
                            "quantity": parseInt(item.avilableQuantity),
                            "uploadedQuantity": parseInt(item.uploadedQuantity), 
                            "varianceQty": item.varianceQty,
                            "varianceCop": item.varianceCOP,
                            "varianceMrp": item.varianceMRP,
                            "statusId": 1,
                            "id":item.id
                        }
                        data.items.push(storeItem);
                     }
                });
             
                
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
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
            }
        }

        var NavigateToBack = function () {
            $state.go('stockAdjustment');
        }
$scope.idToUpdate;


$scope.flagCheck=false;
$scope.flagCheckButtonDisable=false;
        var GetUploadStockDetails = function (uploadId,flag1) {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.UPLOAD_STOCK.DETAIL_API + uploadId + "/" + $scope.unitId;
            $scope.idToUpdate=uploadId;
            $rootScope.startLoader();
            if(flag1==true)
            	{
            	$scope.flagCheckButtonDisable=true;
            	}
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                self.model.uploadId = response.data.id;
                self.model.startDateTime = response.data.requestStartDate;
                self.model.stopDateTime = response.data.requestStopDate;
                $scope.statusId= response.data.statusId;
                self.model.GRNRemark=response.data.remark;
                self.model.reqAssetTypeId = response.data.asseteTypeId;
                self.model.store = self.model.stores.find(function (upload) {
                    return response.data.storeId == upload.id;
                });

                
                self.model.itemDetailList = [];
                self.model.itemDetailListOldBatch=[]; 
                $scope.flagCheck=flag1;
                	       
                var CurrentBatchItems = response.data.items.filter(function(e) { return e.quantity >= 0 })                     
                var oldBatchItems = response.data.items.filter(function(e) { return e.quantity == 0 }) 

                self.model.itemDetailList = CurrentBatchItems;
                
                self.model.pagination.data = self.model.itemDetailList;
                angular.forEach(self.model.pagination.data,function(dataItem){
                    dataItem.avilableQuantity = dataItem.quantity;
                    dataItem.varianceCOP = dataItem.varianceCop;
                    dataItem.varianceMRP = dataItem.varianceMrp;
                    dataItem.uomList.find(function(u){
                        if(u.uomType == dataItem.uomType){
                            dataItem.uomType = u.uomDetails;
                        }
                    })
                    
                    if(response.data.asseteTypeId == 2){
                    	 dataItem.receivedQtyCon = (dataItem.uploadedQuantity);
                         dataItem.receivedQtyCalculation = (dataItem.uploadedQuantity);
                    }else{
                    	
		                    if(dataItem.isThreeUOMApplicable == false){
		                        if(dataItem.uomTypeId == 1){
		                            dataItem.receivedQtyCon = (dataItem.uploadedQuantity +"*"+ dataItem.uomList[0].conversion);
		                            dataItem.receivedQtyCalculation = (dataItem.uploadedQuantity * dataItem.uomList[0].conversion);
		                            dataItem.uomUnitData = dataItem.uomList[2].uomUnit;
		                        }
		                        else if(dataItem.uomTypeId == 2){
		                            
		                        }
		                        else{
		                            dataItem.receivedQtyCon = (dataItem.uploadedQuantity);
		                            dataItem.receivedQtyCalculation = (dataItem.uploadedQuantity);
		                            dataItem.uomUnitData = dataItem.uomList[2].uomUnit;
		                        }
		                    }
		                    else{
		                        if(dataItem.uomTypeId == 1){
		                            dataItem.receivedQtyCon = (dataItem.uploadedQuantity +"*"+ dataItem.uomList[0].conversion +"*"+ dataItem.uomList[1].conversion);
		                            dataItem.receivedQtyCalculation = (dataItem.uploadedQuantity * dataItem.uomList[0].conversion * dataItem.uomList[1].conversion);
		                            dataItem.uomUnitData = dataItem.uomList[2].uomUnit;
		                        }
		                        else if(dataItem.uomTypeId == 2){
		                            dataItem.receivedQtyCon = (dataItem.uploadedQuantity +"*"+ dataItem.uomList[1].conversion);
		                            dataItem.receivedQtyCalculation = (dataItem.uploadedQuantity * dataItem.uomList[1].conversion);
		                            dataItem.uomUnitData = dataItem.uomList[2].uomUnit;
		                        }
		                        else{
		                            dataItem.receivedQtyCon = (dataItem.uploadedQuantity);
		                            dataItem.receivedQtyCalculation = (dataItem.uploadedQuantity);
		                            dataItem.uomUnitData = dataItem.uomList[2].uomUnit;
		                        }
		                    }
                    }
                });
                self.model.itemDetailListOldBatch = oldBatchItems; 
                
                
                self.model.requestNo = response.data.requestNo;
                self.model.itemDetailList.forEach(function (item) {
                    item.mrp = item.mrp.toFixed(2);
                    item.cop = item.cop.toFixed(2);
                    item.cop = commonDetailService.calculateRate(item.uomList, item.uomTypeId, item.cop);
                    item.mrp = commonDetailService.calculateRate(item.uomList, item.uomTypeId, item.mrp);
                    stopTransactionId = item.stopTransactionId;
                    $rootScope.stopLoader();
                })
                self.model.itemDetailListOldBatch.forEach(function (item) {
                    item.mrp = item.mrp.toFixed(2);
                    item.cop = item.cop.toFixed(2);
                    item.cop = commonDetailService.calculateRate(item.uomList, item.uomTypeId, item.cop);
                    item.mrp = commonDetailService.calculateRate(item.uomList, item.uomTypeId, item.mrp);
                    stopTransactionId = item.stopTransactionId;
                    $rootScope.stopLoader();
                }) 
                
                $rootScope.stopLoader();
            },
            function (err) {
                $rootScope.stopLoader();
            });
        }

        $scope.calculateReceivedQty = function(item){
            if(self.model.reqAssetTypeId != 2){
                if(item.uomResponses == undefined){
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[0].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[0].conversion);
                            item.uomUnitData = item.uomList[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                                
                        }
                        else{
                            item.receivedQtyCon = (item.uploadedQuantity);
                            item.receivedQtyCalculation = (item.uploadedQuantity);
                            item.uomUnitData = item.uomList[2].uomUnit;
                        }
                    }
                    else{
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[0].conversion+"*"+ item.uomList[1].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[0].conversion * item.uomList[1].conversion);
                            item.uomUnitData = item.uomList[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomList[1].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uomList[1].conversion);
                            item.uomUnitData = item.uomList[2].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.uploadedQuantity);
                            item.receivedQtyCalculation = (item.uploadedQuantity);
                            item.uomUnitData = item.uomList[2].uomUnit;
                        }
                    }
                }
                else{
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomResponses[0].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uomResponses[0].conversion);
                            if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                        	{
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        	}
                        }
                        else if(item.uomTypeId == 2){
                                
                        }
                        else{
                            item.receivedQtyCon = (item.uploadedQuantity);
                            item.receivedQtyCalculation = (item.uploadedQuantity);
                            if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                        	{
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        	}
                        }
                    }
                    else{
                        if(item.uomTypeId == 1){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomResponses[0].conversion+"*"+ item.uomResponses[1].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uomResponses[0].conversion * item.uomResponses[1].conversion);
                            if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                        	{
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        	}
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.uploadedQuantity +"*"+ item.uomResponses[1].conversion);
                            item.receivedQtyCalculation = (item.uploadedQuantity * item.uomResponses[1].conversion);
                            if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                        	{
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        	}
                        }
                        else{
                            item.receivedQtyCon = (item.uploadedQuantity);
                            item.receivedQtyCalculation = (item.uploadedQuantity);
                            if(item.uomResponses[2]!=undefined && item.uomResponses[2]!=null)
                        	{
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        	}
                        }
                    }
                }
            }
        }
    
        initializeController(); 
    }

    function config($stateProvider) {
        $stateProvider.state("detailsStockAdjustment", {
            url: "/detailsStockAdjustment",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/details-stock-adjustment.html',
            controller: "StockAdjustmentDetails.Controller",
            controllerAs: "vm",
            title: 'Stock Adjustment'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("StockAdjustmentDetails.Controller", stockAdjustmentDetailsController);
})();
