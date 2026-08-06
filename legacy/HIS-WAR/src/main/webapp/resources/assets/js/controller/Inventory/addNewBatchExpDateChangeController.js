(function () {
    "use strict";

    function addNewBatchExpDateChangeController($scope, $rootScope, $http, commonDetailService,PaginationService, CurrencyService, $state, CONSTANTS, GenericService, growl,$cookies,stopTransactionService,$filter) {
        var self = this;
        var batchId;
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
                    "storeName": "Select Store"
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

                /*expiryDateOptions: {
                    minMode: "month"
                },*/
               /* start code for resolved HIS-10198 Batch/MRP/Expiry Date Change : Able to Save New Expiry Date as a Past Date. */
                expiryDateOptions: {
                	minMode: "month",
                	minDate:  new Date()
                },
               /* end code for resolved HIS-10198 Batch/MRP/Expiry Date Change : Able to Save New Expiry Date as a Past Date. */
                dateOpen: [],
                SelectedItemsIds: [],
                selectAllRows: false,
                SelectedItemIds: [],
                NewItemList: [],
                newlist : [],
                itemList: []
            };

            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })

            self.model.GetItemList = GetItemList;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.isSelected = isSelected;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.removeRowData = removeRowData;
            self.model.OpenExpiryDate = OpenExpiryDate;
            self.model.SaveBatchChange = SaveBatchChange;
            self.model.SaveNewBatchRequest = SaveNewBatchRequest;
            self.model.UpdateBatchChange = UpdateBatchChange;
            self.model.NavigteToBatchChange = NavigteToBatchChange;
            self.model.setSelectedDate = setSelectedDate;
            self.model.GetBatchDetails = GetBatchDetails;
            self.model.validateSave = validateSave;
            self.model.SetOldValueStore = SetOldValueStore;
            self.model.ChangeFromStore = ChangeFromStore;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.RemoveItem = RemoveItem;
            self.model.ValidateBatch = ValidateBatch;
            self.model.ValidateExpiry = ValidateExpiry;
            self.model.ValidateMRP = ValidateMRP;
            self.model.ValidateMrpUom = ValidateMrpUom;
            self.model.validateCop = validateCop;
            PopulateDropdown();
            var batchId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!batchId) {
                commonDetailService.setDataId(null);
                GetBatchDetails(batchId);
            } else {
                //PopulateDropdown();
                return false;
            }

        }

        var PopulateDropdown = function () {
            var batch = self.model;
            var userId=$scope.userId;
            var URI = CONSTANTS.GLOBAL.USER_STORE_BY_ID_API+userId+"/"+$cookies.getObject('cookieObject').unitId;
           // batch.store = batch.stores[0];

            batch.stores.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    batch.stores = batch.stores.concat(response.data.stores);
                    var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
            for(var i=0;i<batch.stores.length;i++)
                    {
                        if(batch.stores[i].storeId==selectedStoreId)
                        {
                            batch.store = batch.stores[i];
                        }
                    }
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var setStore = function (storeId) {
            console.log("Store Id : ",storeId);
            var batch = self.model;
            var userId=$scope.userId;
            var URI = CONSTANTS.GLOBAL.USER_STORE_BY_ID_API+userId+"/"+$scope.unitId;
            // batch.store = batch.stores[0];

            batch.stores.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    batch.stores = batch.stores.concat(response.data.stores);
                    for(var i=0;i<batch.stores.length;i++)
                    {
                        if(batch.stores[i].storeId==storeId)
                        {
                            batch.store = batch.stores[i];
                        }
                    }
                    console.log("Set Store Id : ",batch.store);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });

        }

        function GetItemList() {
            self.model.selectAllRows = false;
            self.model.NewItemList = [];
            self.model.pagination.data = [];
            self.model.action = 'Open';
            var popup = angular.element('#additemlist');
            popup.modal('show');
            self.model.isItemChecked = false;
            self.model.search = {};
            GetAssetCategoryList();
        }

        var GetAssetCategoryList = function () {
            var batch = self.model;
            batch.assetCategories=[{
                "category": "Select Asset Category"
            }];
            batch.productCategories=[{
                "category": "Select Product Category"
            }];
            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            batch.assetCategory = self.model.assetCategories[0];
            batch.productCategory = self.model.productCategories[0];

            // batch.assetCategories.splice(1);
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    batch.assetCategories = batch.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var batch = self.model;
            batch.productCategory = batch.productCategories[0];
            batch.productCategories.splice(1);
            //TODO : Add asset category to the uri
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    batch.productCategories = batch.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function searchNewItem() {
            if(self.model.search.itemName){
                if(self.model.search.itemName.length >= 3){
                    //var URI = CONSTANTS.GLOBAL.BATCH_API_STORE_WISE;
                    var URI = CONSTANTS.GLOBAL.BATCH_API_WITHOUT_GENERIC;
                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.storeId,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo,
                        "unitId":$scope.unitId
                    }

                    // self.model.NewItemList = [];
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;

                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        // self.model.pagination.data=[];
                        self.model.pagination.totalPages=0;
                        self.model.NewItemList = response.data;
                        angular.forEach(response.data, function (d){     
                            var obj = {
                                assetCategory: '',
                                assetCategoryId: '',
                                availableQuantity: '',
                                batchId: '',
                                batchNo: '',
                                cop: '',
                                expiry: '',
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
                                uomType: "",
                                uomTypeId: '',
                                uomUnit: "",
                                uomUnitId: 98
                            }
                            obj.assetCategory = d.assetCategory;
                            obj.assetCategoryId = d.assetCategoryId;
                            obj.availableQuantity = d.availableQuantity;
                            obj.batchId = d.batchId;
                            obj.batchNo = d.batchNo;
                            obj.cop = d.cop;
                            obj.expiry = d.expiry;
                            obj.isBatchRequired = d.isBatchRequired;
                            obj.itemCode = d.itemCode;
                            obj.itemDetails = d.itemDetails;
                            obj.itemId = d.itemId;
                            obj.itemName = d.itemName;
                            obj.markupStatus = d.markupStatus;
                            obj.mrp = d.mrp;
                            obj.prodCatId = d.prodCatId;
                            obj.productCategory = d.productCategory;
                            obj.productCategoryId = d.productCategoryId;
                            obj.taxId = d.taxId;
                            obj.taxName = d.taxName;
                            obj.taxPercentage = d.taxPercentage;
                            obj.uomType = d.uomType;
                            obj.uomTypeId = d.uomTypeId;
                            obj.uomUnit = d.uomUnit;
                            obj.uomUnitId = d.uomUnitId;
                            angular.forEach(d.uomResponses, function(u){
                                var obj1 = {
                                    batchId: '',
                                    conversion: '',
                                    ipUom: '',
                                    opUom: '',
                                    storeUom: '',
                                    uomType: '',
                                    uomTypeId: '',
                                    uomUnit: '',
                                    uomUnitId: ''
                                }
                                obj1.batchId = u.batchId;
                                obj1.conversion = u.conversion;
                                obj1.ipUom = u.ipUom;
                                obj1.opUom = u.opUom;
                                obj1.storeUom = u.storeUom;
                                obj1.uomType = u.uomType;
                                obj1.uomTypeId = u.uomTypeId;
                                obj1.uomUnit = u.uomUnit;
                                obj1.uomUnitId = u.uomUnitId;
                                obj.uomResponses.push(obj1);
                            });
                            self.model.newlist.push(obj);
                        });
                        angular.forEach(self.model.NewItemList, function(item){
                            if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                item.uomResponses.find(function(d){
                                    if(d.uomTypeId == item.uomTypeId){
                                        item.uomType = d.uomDetails;
                                    }
                                });
                            }
                        })
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }
            }
            else if(self.model.search.itemCode){
                if(self.model.search.itemCode.length >= 3){
                    var URI = CONSTANTS.GLOBAL.BATCH_API_WITHOUT_GENERIC;
                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.storeId,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo,
                        "unitId":$scope.unitId
                    }

                    // self.model.NewItemList = [];
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;

                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        // self.model.pagination.data=[];
                        self.model.pagination.totalPages=0;
                        self.model.NewItemList = response.data;
                        angular.forEach(response.data, function (d){     
                            var obj = {
                                assetCategory: '',
                                assetCategoryId: '',
                                availableQuantity: '',
                                batchId: '',
                                batchNo: '',
                                cop: '',
                                expiry: '',
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
                                uomType: "",
                                uomTypeId: '',
                                uomUnit: "",
                                uomUnitId: 98
                            }
                            obj.assetCategory = d.assetCategory;
                            obj.assetCategoryId = d.assetCategoryId;
                            obj.availableQuantity = d.availableQuantity;
                            obj.batchId = d.batchId;
                            obj.batchNo = d.batchNo;
                            obj.cop = d.cop;
                            obj.expiry = d.expiry;
                            obj.isBatchRequired = d.isBatchRequired;
                            obj.itemCode = d.itemCode;
                            obj.itemDetails = d.itemDetails;
                            obj.itemId = d.itemId;
                            obj.itemName = d.itemName;
                            obj.markupStatus = d.markupStatus;
                            obj.mrp = d.mrp;
                            obj.prodCatId = d.prodCatId;
                            obj.productCategory = d.productCategory;
                            obj.productCategoryId = d.productCategoryId;
                            obj.taxId = d.taxId;
                            obj.taxName = d.taxName;
                            obj.taxPercentage = d.taxPercentage;
                            obj.uomType = d.uomType;
                            obj.uomTypeId = d.uomTypeId;
                            obj.uomUnit = d.uomUnit;
                            obj.uomUnitId = d.uomUnitId;
                            angular.forEach(d.uomResponses, function(u){
                                var obj1 = {
                                    batchId: '',
                                    conversion: '',
                                    ipUom: '',
                                    opUom: '',
                                    storeUom: '',
                                    uomType: '',
                                    uomTypeId: '',
                                    uomUnit: '',
                                    uomUnitId: ''
                                }
                                obj1.batchId = u.batchId;
                                obj1.conversion = u.conversion;
                                obj1.ipUom = u.ipUom;
                                obj1.opUom = u.opUom;
                                obj1.storeUom = u.storeUom;
                                obj1.uomType = u.uomType;
                                obj1.uomTypeId = u.uomTypeId;
                                obj1.uomUnit = u.uomUnit;
                                obj1.uomUnitId = u.uomUnitId;
                                obj.uomResponses.push(obj1);
                            });
                            self.model.newlist.push(obj);
                        });
                        angular.forEach(self.model.NewItemList, function(item){
                            if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                item.uomResponses.find(function(d){
                                    if(d.uomTypeId == item.uomTypeId){
                                        item.uomType = d.uomDetails;
                                    }
                                });
                            }
                        })
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }
            }
            else if(self.model.search.batchNo){
                var URI = CONSTANTS.GLOBAL.BATCH_API_WITHOUT_GENERIC;
                const data = {
                    "assetTypeId": 1,
                    "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                    "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                    "storeId": self.model.store.storeId,
                    "itemCode": self.model.search.itemCode,
                    "itemName": self.model.search.itemName,
                    "batchName": self.model.search.batchNo,
                    "unitId":$scope.unitId
                }

                // self.model.NewItemList = [];
                self.model.selectAllRows = false;
                self.model.isItemChecked = false;

                $rootScope.startLoaderPopup();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    // self.model.pagination.data=[];
                    self.model.pagination.totalPages=0;
                    self.model.NewItemList = response.data;
                    angular.forEach(response.data, function (d){     
                            var obj = {
                                assetCategory: '',
                                assetCategoryId: '',
                                availableQuantity: '',
                                batchId: '',
                                batchNo: '',
                                cop: '',
                                expiry: '',
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
                                uomType: "",
                                uomTypeId: '',
                                uomUnit: "",
                                uomUnitId: 98
                            }
                            obj.assetCategory = d.assetCategory;
                            obj.assetCategoryId = d.assetCategoryId;
                            obj.availableQuantity = d.availableQuantity;
                            obj.batchId = d.batchId;
                            obj.batchNo = d.batchNo;
                            obj.cop = d.cop;
                            obj.expiry = d.expiry;
                            obj.isBatchRequired = d.isBatchRequired;
                            obj.itemCode = d.itemCode;
                            obj.itemDetails = d.itemDetails;
                            obj.itemId = d.itemId;
                            obj.itemName = d.itemName;
                            obj.markupStatus = d.markupStatus;
                            obj.mrp = d.mrp;
                            obj.prodCatId = d.prodCatId;
                            obj.productCategory = d.productCategory;
                            obj.productCategoryId = d.productCategoryId;
                            obj.taxId = d.taxId;
                            obj.taxName = d.taxName;
                            obj.taxPercentage = d.taxPercentage;
                            obj.uomType = d.uomType;
                            obj.uomTypeId = d.uomTypeId;
                            obj.uomUnit = d.uomUnit;
                            obj.uomUnitId = d.uomUnitId;
                            angular.forEach(d.uomResponses, function(u){
                                var obj1 = {
                                    batchId: '',
                                    conversion: '',
                                    ipUom: '',
                                    opUom: '',
                                    storeUom: '',
                                    uomType: '',
                                    uomTypeId: '',
                                    uomUnit: '',
                                    uomUnitId: ''
                                }
                                obj1.batchId = u.batchId;
                                obj1.conversion = u.conversion;
                                obj1.ipUom = u.ipUom;
                                obj1.opUom = u.opUom;
                                obj1.storeUom = u.storeUom;
                                obj1.uomType = u.uomType;
                                obj1.uomTypeId = u.uomTypeId;
                                obj1.uomUnit = u.uomUnit;
                                obj1.uomUnitId = u.uomUnitId;
                                obj.uomResponses.push(obj1);
                            });
                            self.model.newlist.push(obj);
                        });
                        angular.forEach(self.model.NewItemList, function(item){
                            if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                item.uomResponses.find(function(d){
                                    if(d.uomTypeId == item.uomTypeId){
                                        item.uomType = d.uomDetails;
                                    }
                                });
                            }
                        })
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchDataByField(self.model.NewItemList, self.model.pagination);
                    }
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
            }
            else{
                self.model.NewItemList = [];
                self.model.pagination.data = [];
            }
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item.itemId;
                });
            } else {
                self.model.SelectedItemsIds = [];
                self.model.pagination.data.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        $scope.list = [];
        var SelectItem = function (item) {
            var itemIds = self.model.SelectedItemsIds;

            if (item.isSelected) {
                itemIds.push(item.itemId);
                var obj = {
                    "batchId" : ''
                }
                obj.batchId = item.batchId;
                $scope.list.push(obj);
            } else {
                var index = itemIds.indexOf(item.itemId);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }
            if (self.model.pagination.data.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.pagination.data.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelected = function (item) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            return item.isSelected;
        }
        
        function getAllItemDetails() {
        	
        	ActaulgetAllItemDetails();
      /* 	 self.model.stopItemIds = [];
        	self.model.NewItemList.forEach(function (item) {
              if (item.isSelected == true) {
            	  self.model.stopItemIds.push(item.itemId);
              }
             });        	
        	var stopData = {
   	                "itemIds": self.model.stopItemIds,
   	                "storeId": self.model.store.storeId	      		 
   	          };
        	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                var StopTransactionData = responseData;  
                if(StopTransactionData.length > 0 ){    
                         var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                         var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                         growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                    
                         $rootScope.stopLoader(); 
                         self.model.stopItemIds = [];
                         return false;
                 }
                else{
                	ActaulgetAllItemDetails();
                }
       	  });      	
        	*/
        }

        function ActaulgetAllItemDetails() {
            if($scope.list.length != 0){
                $scope.list.forEach(function (item1) {
                    self.model.NewItemList.forEach(function (item){
                        if(item1.batchId == item.batchId){
                            item.mrpUOM = item.uomResponses;
                            if(item.isThreeUOMApplicable == false){
                                item.mrpUOMData = item.mrpUOM;
                                item.mrpUOM = [];
                                angular.forEach(item.mrpUOMData, function(d){
                                    if(d.uomTypeId != 2){
                                        item.mrpUOM.push(d);
                                    }
                                })
                            }
                            if(self.model.itemList.length > 1){
                                var isFound = self.model.itemList.find(function (x) {
                                    return x.batchId == item.batchId;
                                });
                                if (!isFound) {
                                    item.mrp = item.mrp.toFixed(2);
                                    if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                        item.uomResponses.find(function(d){
                                            if(d.uomTypeId == item.uomTypeId){
                                                item.uomType = d.uomDetails;
                                            }
                                        });
                                    }
                                    self.model.itemList.push(item);
                                }
                                else 
                                {
                                    growl.error('Item already present in the list')
                                }
                            }
                            else{
                                if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                    item.uomResponses.find(function(d){
                                        if(d.uomTypeId == item.uomTypeId){
                                            item.uomType = d.uomDetails;
                                        }
                                    });
                                }
                                self.model.itemList.push(item);
                            }
                        }
                    });
                });
            }
            else{
                self.model.pagination.data.forEach(function (item1) {
                    self.model.NewItemList.forEach(function (item){
                        if(item1.batchId == item.batchId){
                            item.mrpUOM = item.uomResponses;
                            if(item.isThreeUOMApplicable == false){
                                item.mrpUOMData = item.mrpUOM;
                                item.mrpUOM = [];
                                angular.forEach(item.mrpUOMData, function(d){
                                    if(d.uomTypeId != 2){
                                        item.mrpUOM.push(d);
                                    }
                                })
                            }
                            if(self.model.itemList.length > 1){
                                var isFound = self.model.itemList.find(function (x) {
                                    return x.batchId == item.batchId;
                                });
                                if (!isFound) {
                                    item.mrp = item.mrp.toFixed(2);
                                    if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                        item.uomResponses.find(function(d){
                                            if(d.uomTypeId == item.uomTypeId){
                                                item.uomType = d.uomDetails;
                                            }
                                        });
                                    }
                                    self.model.itemList.push(item);
                                }
                                else 
                                {
                                    growl.error('Item already present in the list')
                                }
                            }
                            else{
                                if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                    item.uomResponses.find(function(d){
                                        if(d.uomTypeId == item.uomTypeId){
                                            item.uomType = d.uomDetails;
                                        }
                                    });
                                }
                                self.model.itemList.push(item);
                            }
                        }
                    });
                });
            }
            
            $scope.list = [];
            self.model.newlist = [];
        }

        function removeRowData(item) {
            var index = self.model.itemList.indexOf(item);
            self.model.itemList.splice(index, 1);
        }

        var OpenExpiryDate = function (idx) {
            self.model.dateOpen[idx] = true;
        }

        var GetBatchDetails = function (batchId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            // var opbId = openingBalanceService.getOpeningBalanceId();

            var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.DETAILS_API + batchId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.batchId = response.data.id;

                    /*self.model.store = self.model.stores.find(function (batch) {
                        return response.data.storeId == batch.id;
                        //self.model.store.id = response.data.storeId;
                    });*/

                   /* for(var i=0;i<self.model.stores.length;i++)
                    {
                        if(self.model.stores[i].storeId==response.data.storeId)
                        {
                            self.model.store = self.model.stores[i];
                        }
                    }*/
                    
                    self.model.itemList = [];
                    self.model.itemList = response.data.batch;
                   // self.model.store = response.data.storeId;
                    angular.forEach(self.model.itemList, function (itemChange, key) {
                        itemChange.newExpiry = new Date(itemChange.newBatchExpiry);
                        itemChange.batchNo = itemChange.oldBatchNo;
                        itemChange.expiry = new Date(itemChange.oldBatchExpiry);
                        itemChange.mrp = itemChange.oldMrp;
                        itemChange.newcop = itemChange.cop;
                        itemChange.newMrp = itemChange.newMrp.toFixed(2);
                        itemChange.lcop = parseFloat(itemChange.lcop.toFixed(2));
                        itemChange.mrp = itemChange.mrp.toFixed(2);
                        itemChange.mrpUOM = itemChange.uomList;
                        if(itemChange.isThreeUOMApplicable == false){
                            itemChange.mrpUOMData = itemChange.mrpUOM;
                            itemChange.mrpUOM = [];
                            angular.forEach(itemChange.mrpUOMData, function(d){
                                if(d.uomTypeId != 2){
                                    itemChange.mrpUOM.push(d);
                                }
                            })
                        }
                        itemChange.selectedMrpUom = itemChange.mrpUOM.find(function (mrp) {
                            return itemChange.mrpUomTypeId == mrp.uomTypeId;
                        });
                        itemChange.uomList.find(function(d){
                            if(d.uomType == itemChange.uomType){
                                itemChange.uomType = d.uomDetails;
                            }
                        });
                    });

                    $rootScope.stopLoader();
                    console.log("Store Id : ",response.data.storeId);
                    setStore(response.data.storeId);
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }



        var SaveNewBatchRequest = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.SAVE_API;

            var data = {
                "storeId": store.store.storeId,
                "statusId": 1,
                "approvalStatusId": 1,
                "batch": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId

            };

            angular.forEach(store.itemList, function (item, key) {
                
                if(item.expiry != undefined || item.expiry != null){
                    var response = commonDetailService.addSingleDigitMonthToZero(item.expiry);
                    item.expiry = response;
                }

                if(item.newBatchExpiry != undefined || item.newBatchExpiry != null){
                    var responseData = commonDetailService.addSingleDigitMonthToZero(item.newBatchExpiry);
                    item.newBatchExpiry = responseData;
                }
                
                var storeItem = {
                    "batchId": item.batchId,
                    "oldBatchNo": item.batchNo,
                    "oldBatchExpiry": item.expiry,
                    "oldMrp": item.mrp,
                    "newBatchNo": item.newBatchNo,
                    "newBatchExpiry": item.newBatchExpiry,
                    "cop" : item.newcop,
                    "newMrp": item.newMrp,
                    "mrpUomTypeId": item.selectedMrpUom.uomTypeId,
                    "statusId": 1,
                    "lcop": item.lcop ? item.lcop : 0
                }
                data.batch.push(storeItem);

            });
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    // ClearRequests();
                    $rootScope.stopLoader();
                    NavigteToBatchChange();
                    growl.success(response.data.message);
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }

        var NavigteToBatchChange = function () {
            $state.go('batchExpDateChange');
        }

        var setSelectedDate = function (item) {
            var expDateYear = item.newExpiry.getFullYear();
            var expDateMonth = item.newExpiry.getMonth() + 1;
            var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
            item.newBatchExpiry = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
        }

        var SaveBatchChange = function () {
            if (validateSave()) {
                if (!self.model.batchId) {
                    SaveNewBatchRequest();
                } else {
                    UpdateBatchChange();
                }
            }
        }
        
        var UpdateBatchChange = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

            var batch = self.model;

            var URI = CONSTANTS.INVENTORY.STORE.BATCH_CHANGE.UPDATE_API + batch.batchId;
            var data = {

                "batch": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "storeId":$scope.storeId

            };

            angular.forEach(batch.itemList, function (item, key) {
                var response = commonDetailService.addSingleDigitMonthToZero(item.expiry);
                item.expiry = response;

                var responseData = commonDetailService.addSingleDigitMonthToZero(item.newBatchExpiry);
                item.newBatchExpiry = responseData;

                var storeItem = {
                	"batchId": item.batchId,
                	"oldBatchNo": item.batchNo,
                    "oldBatchExpiry": item.expiry,
                    "oldMrp": item.mrp,
                    "id": item.id,
                    "newBatchNo": item.newBatchNo,
                    "newBatchExpiry": item.newBatchExpiry,
                    "cop": item.newcop,
                    "newMrp": item.newMrp,
                    "mrpUomTypeId": item.selectedMrpUom.uomTypeId,
                    "lcop": item.lcop ? item.lcop : 0,
                    "statusId": 1,
             
                    
                }

                if (item.id == '' || item.id == null) {
                    storeItem.batchId = item.batchId,
                        storeItem.oldBatchNo = item.batchNo,
                        storeItem.oldBatchExpiry = item.expiry,
                        storeItem.oldMrp = item.mrp,
                        storeItem.statusId = 1
                }
                data.batch.push(storeItem);


            });
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    NavigteToBatchChange();
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
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateMRP(ItemDetail)) {
                    isValid = false;
                }
                if(ItemDetail.isBatchRequired==true)
                {
                   if (!ValidateExpiry(ItemDetail)) {
                    isValid = false;
                }     
                
                if (!ValidateBatch(ItemDetail)) {
                    isValid = false;
                } 
                }
                if (!ValidateMrpUom(ItemDetail)) {
                    isValid = false;
                }
                if(!validateCop(ItemDetail)){
                    isValid = false;
                }
                if(!$scope.validateRate(ItemDetail)){
                    isValid = false;
                }
            }

            return isValid;
        }

        var SetOldValueStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeFromStore = function (store) {
            if (!!self.model.itemList.length && store != null) {
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

        var ValidateBatch = function (item) {
            var isValid = true;
            if (!item.newBatchNo) {
                item.isBatchNoValid = true;
                isValid = false;
                growl.error("Please Enter New Batch No.");
            }

            return isValid;
        }
        var ValidateExpiry = function (item) {
            var isValid = true;
            if (!item.newExpiry) {
                item.isExpiryDateValid = true;
                isValid = false;
                growl.error("Please Select New Expiry Date");
            } 
            // else {
            //     var expYear = new Date().getFullYear();
            //     var expMonth = new Date().getMonth() + 1;
            //     var selectedYear = new Date(item.newExpiry).getFullYear();
            //     var selectedMonth = new Date(item.newExpiry).getMonth() + 1;

            //     // if (selectedYear <= expYear && selectedMonth < expMonth) {
            //     //     growl.error('Expiry date should be later than current month and year');
            //     //     item.isExpiryDateValid = true;
            //     //     isValid = false;
            //     // }
            // }

            return isValid;
        }

        var ValidateMRP = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.newMrp))) {
                item.newMrp = '';
                item.isMrpValid = true;
                isValid = false;
                growl.error("Please Enter New MRP");
            } 
            // else if (parseFloat(item.newMrp) == 0) {
            //     item.isMrpValid = true;
            //     isValid = false;
            //     growl.error('MRP cannot be zero');
            // } 
            else if (parseFloat(item.newMrp) > 0) {
                item.newMrp = parseFloat(item.newMrp).toFixed(2);
            }
            return isValid;
        }
        var ValidateMrpUom = function (item) {
            var isValid = true;
            if (!item.selectedMrpUom) {
                item.isMrpUomValid = true;
                isValid = false;
                growl.error("Please Select MRP UOM");
            }
            return isValid;
        }
        var validateCop = function (item) {
            var isValid = true;
            if (!item.newcop) {
                item.isCopValid = true;
                isValid = false;
                growl.error("Please enter cop");
            }
            return isValid;
        }

        $scope.validateRate = function (item) {
            var isValid = true;
            if(parseFloat(item.newMrp) < parseFloat(item.newcop) && item.newMrp != 0){
                isValid = false;
                growl.error("Mrp should be greater than cop");
            }
            return isValid;
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("additemBatchChange", {
            url: "/additemBatchChange",
            templateUrl: 'views/PharmacyInventory/Store/add-item-batch-exp-date-change.html',
            controller: "AddItemBatchChange.Controller",
            controllerAs: "vm",
            title: 'Batch/MRP/Expiry Date Change'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddItemBatchChange.Controller", addNewBatchExpDateChangeController);
})();