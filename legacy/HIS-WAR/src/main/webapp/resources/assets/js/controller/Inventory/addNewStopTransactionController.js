(function () {
    "use strict";

    function addNewStopTransactionController($scope, $rootScope, $http, commonDetailService, CurrencyService, $state, $filter, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;
        var stopTransactionId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                assetCategories: [{
                    "category": "Select Asset Category"
                }],
                productCategoriesPopup: [{
                    "category": "Select Product Category"
                }],
                stores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All Status"
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
                SelectedRequestIds: [],
                selectAllRows: false,
                startDateTime: new Date(),
                startDateTimeOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },

                stopDateTime: new Date(),
                stopDateTimeOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                SelectedItemsIds: [],
                selectAllRows: false,
                SelectedItemIds: [],
                productCategories: []



            };
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.productCategories = [];
            self.model.OpenStopDateTime = OpenStopDateTime;
            self.model.OpenStartDateTime = OpenStartDateTime;
            self.model.ActiveTab = 1;
            self.model.addNewStopTransactionSearch = addNewStopTransactionSearch;
            self.model.zeroStockDataSearch = zeroStockDataSearch;
            self.model.AddProductCategory = AddProductCategory;
            self.model.SearchProductCategory = SearchProductCategory;
            self.model.nameNotFound = nameNotFound;
            self.model.AddProductCategory = AddProductCategory;
            self.model.search = '';
            self.model.itemManupulation = itemManupulation;
            self.model.prevSearch = '';
            self.model.productCategory = [];
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.AddItems = AddItems;
            self.model.openAddItem = openAddItem;
            self.model.searchNewItem = searchNewItem;
            self.model.itemSelect = false;
            self.model.onAddButtonClick = onAddButtonClick;
            self.model.isVitalSelect = isVitalSelect;
            self.model.isDiseriableSelect = isDiseriableSelect;
            self.model.isEssentialSelect = isEssentialSelect;
            self.model.vedId = [];

            self.model.itemexclude = itemexclude;
            self.model.SaveStopTransaction = SaveStopTransaction;
            self.model.NavigateToBack = NavigateToBack;
            self.model.isDetail = isDetail;
            self.model.removeRowDataProductCategory = removeRowDataProductCategory;
            self.model.removeRowDataItemExclusion = removeRowDataItemExclusion; 
            self.model.sendAlert = sendAlert;
            self.model.isUpdate = false;
            self.model.selectedPatientIds =[];
            self.model.SelectAllStopItem = SelectAllStopItem;
            self.model.SelectedStopItems = [];
            self.model.selectAllRowsStopItem = false;
            self.model.SelectItemStopItem = SelectItemStopItem;
            self.model.allItemsSelect = allItemsSelect;
            PopulateDropdown();
            isDetail();
            GetAssetCategoryList();

        }
        
        var allItemsSelect = function(isSlected) {
            if(isSlected){
            self.model.itemList = []; 
            self.model.isVital = null;
            self.model.isEssential = null;
            self.model.isDiseriable = null;
            self.model.isA = null;
            self.model.isB= null;
            self.model.isC  = null;   
            }                  
      }

        var SelectAllStopItem = function () {
            if (self.model.selectAllRowsStopItem) {
                self.model.SelectedStopItems = self.model.itemList.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    return item;
                });
            } else {
                self.model.SelectedStopItems = [];
                self.model.itemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;

                });
            }
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

            if (self.model.itemList.every(isSelected)) {
                self.model.selectAllRowsStopItem = true;
            } else {
                self.model.selectAllRowsStopItem = false;
            }
            self.model.isItemChecked = false;
            self.model.itemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        var isDetail = function () {
            
            if (commonDetailService.getDataId().isDetail) {
                stopTransactionId = commonDetailService.getDataId().id;
                self.model.isDetail = commonDetailService.getDataId().isDetail;
                GetStopTransactionDetails(stopTransactionId);
            }
            stopTransactionId = commonDetailService.getDataId();
            GetStopTransactionDetails(stopTransactionId.id);
        }     
               
       
        var GetStopTransactionDetails = function (stopTransactionId) {

            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.DETAIL_API + stopTransactionId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.stopTransactionDetail = response.data;
                    self.model.stopTransactionId = response.data.id;
                    self.model.statusId = response.data.statusId;
                    
                    if(self.model.statusId == 9)
                    	{
                    	self.model.isUpdate= true;
                    	}
                    
                    self.model.remark = response.data.remark;
                    self.model.isVital = response.data.isVital;
                    self.model.isEssential = response.data.isEssential;
                    self.model.isDiseriable = response.data.isDiseriable;
                    self.model.isA = response.data.isA;
                    self.model.isB = response.data.isB;
                    self.model.isC = response.data.isC;
                    self.model.itemList = response.data.items;
                    self.model.newItemList = angular.copy(self.model.itemList);                    
                    self.model.productCategories = response.data.productCategoryItems;
                    self.model.itemDetailList = response.data.exclusionItems;
                    angular.forEach(self.model.itemList, function (item, key) {
                        item.cop = item.cop.toFixed(2);
                        item.mrp = item.mrp.toFixed(2);
                        
                        item.isSelected = true;
                        self.model.selectAllRowsStopItem = true;
                        self.model.SelectedStopItems = self.model.itemList; 
                    });
                    if(self.model.itemList != undefined)
                        self.model.isUpdate = true;
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var isVitalSelect = function () {
            if (self.model.isVital == true) {
                self.model.vedId.push(1)
            }
        }

        var isEssentialSelect = function () {
            if (self.model.isEssential == true) {
                self.model.vedId.push(2)
            }
        }

        var isDiseriableSelect = function () {
            if (self.model.isDiseriable == true) {
                self.model.vedId.push(3)
            }
        }

        // function fromStopTransaction() {
        //     var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.DETAIL_API + commonDetailService.getDataId();
        //     var data = {};
        //     GenericService.serviceAction("GET", URI, data).then(
        //         function (response) {
        //             self.model.stopTransactionDetail = response.data;
        //         });
        // }

        var PopulateDropdown = function () {
            var stransaction = self.model;
            stransaction.store = stransaction.stores[0];
            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    stransaction.stores = stransaction.stores.concat(response.data);
                    $rootScope.stopLoader();

                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var OpenStartDateTime = function () {
            self.model.startDateOpened = true;
            self.model.stopDateTimeOptions.minDate = self.model.startDateTime;

        }

        var OpenStopDateTime = function () {
            self.model.stopDateOpened = true;
            self.model.stopDateTimeOptions.maxDate = self.model.stopDateTime;
        }

        var zeroStockDataSearch = function () {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.ZERO_STOCK_SEARCH;            
            if(self.model.isVital)
                self.model.vedId.push(1);
            if(self.model.isEssential)
               self.model.vedId.push(2);
            if(self.model.isDiseriable)
                self.model.vedId.push(3);            
            var data = {
                "fromDate": moment(self.model.startDateTime).format('YYYY-MM-DD'),
                "toDate": moment(self.model.stopDateTime).format('YYYY-MM-DD'),
                "assetTypeId": 1,
                "storeId": self.model.stopTransactionDetail.storeId,
                "vedId": self.model.vedId ? self.model.vedId : undefined,
                "unitId": $scope.unitId
            };
            $rootScope.startLoader()

            var percent = 0;
            if (self.model.isA) percent = 70;
            if (self.model.isB) percent = percent + 20;
            if (self.model.isC) percent = percent + 10;

            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.isUpdate= false;
                    angular.forEach(self.model.stopTransactionDetail.items, function(sdata){
                        angular.forEach(response.data, function(rdata){
                            if(sdata.batchId == rdata.batchId){
                                self.model.selectAllRowsStopItem = false;
                                rdata.isSelected = true;
                            }
                        });
                    });
                    self.model.itemList = response.data;

                    var totalAmount = 0;
                    angular.forEach(self.model.itemList, function (item, key) {
                        totalAmount = totalAmount + item.totalMrp;
                        item.cop = item.cop.toFixed(2);
                        item.mrp = item.mrp.toFixed(2);
                    });

                    var percentAmount = (totalAmount * percent) / 100;
                    totalAmount = 0;
                    var itemList = self.model.itemList;
                    angular.forEach(itemList, function (item, key) {
                        totalAmount = totalAmount + item.totalMrp;
                        if (percent > 0 && totalAmount > percentAmount)
                            self.model.itemList.splice(key, itemList.length);

                    });
                    self.model.newItemList = [];
                    self.model.newItemList = self.model.itemList;
                    self.model.vedId = [];
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }

        var addNewStopTransactionSearch = function () {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.SEARCH_ITEM;            
            if(self.model.isVital)
                self.model.vedId.push(1);
            if(self.model.isEssential)
               self.model.vedId.push(2);
            if(self.model.isDiseriable)
                self.model.vedId.push(3);            
            var data = {
                //"fromDate": moment(self.model.stopTransactionDetail.startDateTime).format('YYYY-MM-DD'),
              //  "toDate": moment(self.model.stopTransactionDetail.stopDateTime).format('YYYY-MM-DD'),
                "fromDate": moment(self.model.startDateTime).format('YYYY-MM-DD'),
                "toDate": moment(self.model.stopDateTime).format('YYYY-MM-DD'),
                "assetTypeId": 1,
                "storeId": self.model.stopTransactionDetail.storeId,
                "vedId": self.model.vedId ? self.model.vedId : undefined,
                "unitId": $scope.unitId
            };
            $rootScope.startLoader()

            var percent = 0;
            if (self.model.isA) percent = 70;
            if (self.model.isB) percent = percent + 20;
            if (self.model.isC) percent = percent + 10;

            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.isUpdate= false;
                    angular.forEach(self.model.stopTransactionDetail.items, function(sdata){
                        angular.forEach(response.data, function(rdata){
                            if(sdata.batchId == rdata.batchId){
                                self.model.selectAllRowsStopItem = false;
                                rdata.isSelected = true;
                            }
                        });
                    });
                    self.model.itemList = response.data;

                    var totalAmount = 0;
                    angular.forEach(self.model.itemList, function (item, key) {
                        totalAmount = totalAmount + item.totalMrp;
                        item.cop = item.cop.toFixed(2);
                        item.mrp = item.mrp.toFixed(2);
                    });

                    var percentAmount = (totalAmount * percent) / 100;
                    totalAmount = 0;
                    var itemList = self.model.itemList;
                    angular.forEach(itemList, function (item, key) {
                        totalAmount = totalAmount + item.totalMrp;
                        if (percent > 0 && totalAmount > percentAmount)
                            self.model.itemList.splice(key, itemList.length);

                    });
                    self.model.newItemList = [];
                    self.model.newItemList = self.model.itemList;
                    self.model.vedId = [];
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }
        var sendAlert = function () {
      	   var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.SEND_ALERT +  self.model.stopTransactionDetail.storeId + "/" + $scope.unitId;   
      	   $rootScope.startLoader();
      	   GenericService.serviceAction("PATCH", URI, "").then(
      		 function(response){ 
                   $rootScope.stopLoader();
      			 growl.success('Stop transaction alert send successfully');    			 
      		 },
      		 function errorCallback(response) {
                   $rootScope.stopLoader();
                   growl.error('Error');
               });    	   
          }

        function SearchProductCategory(search) {
            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.PRODUCT_CATEGORY_API + search;
                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        if (!!response.data && response.data.length > 0) {
                            self.model.productCategory = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.productCategory, {
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
                if (!!self.model.productCategory && self.model.productCategory.length > 0 && self.model.productCategory[0].itemFound != undefined && !self.model.productCategory[0].itemFound) {
                    return nameNotFound(search);
                } else {
                    return $filter('filter')(self.model.productCategory, {
                        $: search
                    });
                }
            }
        }

        function nameNotFound(search) {
            var item = {
                "itemFound": false,
                "category": "Product Category Not found with name " + search
            };
            self.model.productCategory = [item];
            return self.model.productCategory;
        }

        function AddProductCategory(productCategory) {
        	  self.model.productCategoryName = productCategory.assetCategory; 
           // self.model.productCategoryName = productCategory.category;
            self.model.productCategoryId = productCategory.id;
            self.model.productCategoryCode = productCategory.code;
        }

        var onAddButtonClick = function () {
        	 self.model.productCategories = self.model.productCategories || [];
        	 var data = self.model.productCategories.find(ob => ob['pCategoryId'] === self.model.productCategoryId);     
            if (!self.model.productCategoryId) {
                growl.error('Please Select Valid Product Category')
            }
            else if(self.model.productCategories.length > 0 && data != null && data !="" ) {
                growl.error('Product Category Already Exist')
            }
            else {
                self.model.proIds = [];
                var name = self.model.productCategoryName;
                var id = self.model.productCategoryId;
               // var code = self.model.productCategoryId; 
                var code = self.model.productCategoryCode;               
            
                   
                self.model.productCategories.push({
                	assetCategory: name, pCategoryId: id, productCategory: code    
                });
                self.model.proIds.push(id);
                itemManupulation()
                //self.model.productCategoryName = '';
                $('#productCategory').val('');
                
                if(self.model.isUpdate)
                	removeItemsByProductId(id);
            }
        }
        var removeItemsByProductId = function (productCategoryId) {
        var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.REMOVE_ITEM_BY_PRODUCTCATEGORY + productCategoryId + "/" + self.model.stopTransactionDetail.id ;            
        var data = {
                "userId":$scope.userId
              };
        $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    var status = response.data;                  
                    $rootScope.stopLoader();          
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }
        
        var removeItemsByItemIds = function (itemIds) {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.REMOVE_ITEM_BY_ITEMID;
            var data = {
                    "itemIds": itemIds,
                    "stopTransactionId": [self.model.stopTransactionDetail.id]  ,
                    "userId":$scope.userId
                  };
                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        var status = response.data;                  
                        $rootScope.stopLoader();          
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    });
            }
        
        var itemexclude = function () {
            itemManupulation()
        }

        var itemManupulation = function () {

            angular.forEach(self.model.productCategories, function (pcitem, pckey) {
                var keyList = []
                var itemList = angular.copy(self.model.newItemList)
                angular.forEach(self.model.newItemList, function (item, key) {
                    if (item.productCategoryId == pcitem.pCategoryId) {
                        keyList.push(key)
                    }
                });
                keyList = keyList.sort(function (a, b) { return b - a });
                angular.forEach(keyList, function (item, key) {
                    itemList.splice(item, 1)
                });
                self.model.itemList = itemList
            });

            angular.forEach(self.model.itemDetailList, function (selectteditem, key) {
                var keyList = [];
                //self.model.newItemList = angular.copy(self.model.ItemList);
                var itemList = self.model.newItemList; 
                angular.forEach(self.model.newItemList, function (item, key) {
                    if (item.itemId == selectteditem.itemId) {
                        keyList.push(key)
                    }
                });

                keyList = keyList.sort(function (a, b) { return b - a });
                angular.forEach(keyList, function (item, key) {
                    itemList.splice(item, 1)
                });

                self.model.itemList = itemList
            });
        }

        function removeRowDataProductCategory(item) {
            var index = self.model.productCategories.indexOf(item);
            self.model.productCategories.splice(index, 1);      
            itemManupulation();        
        }    
           
        
        function removeRowDataItemExclusion(item) {
        	self.model.itemDetailList = self.model.itemDetailList || []; 
            var index = self.model.itemDetailList.indexOf(item);
            self.model.itemDetailList.splice(index, 1);
            itemManupulation();           
        }    

        var GetAssetCategoryList = function () {
            var indents = self.model;
            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            indents.assetCategory = indents.assetCategories[0];
            indents.productCategory = indents.productCategoriesPopup[0];

            indents.assetCategories.splice(1);
            indents.productCategoriesPopup.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    indents.assetCategories = indents.assetCategories.concat(response.data);
                    $rootScope.stopLoader();  
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var GetProductCategoryList = function (id) {
            var indents = self.model;
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            indents.productCategory = indents.productCategoriesPopup[0];

            indents.productCategoriesPopup.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    indents.productCategoriesPopup = indents.productCategoriesPopup.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
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
            self.model.SelectedIds = [];	
            self.model.SelectedIds = self.model.SelectedItemsIds;
            self.model.itemDetailListnew = ""; 
            self.model.itemDetailListnew = angular.copy(self.model.itemDetailList); 
            self.model.itemDetailListnew = self.model.itemDetailListnew || [];
            
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": self.model.stopTransactionDetail.storeId
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.itemDetailList = response.data;
                    
                    
                    if(self.model.itemDetailListnew.length > 0){
                    	 self.model.itemDetailListnew.forEach(function (item) {
                             if (item != "") {
                                self.model.itemDetailList.push(item);
                             }
                         });
                    }                   
                    
                    $rootScope.stopLoader();
                    if(self.model.isUpdate)
                    	removeItemsByItemIds(self.model.SelectedItemsIds);
                    // getAllItemDetails();
                    self.model.searchNewItemList = [];
                    self.model.SelectedItemsIds = [];
                    itemexclude();                   
                   
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.message);
                });
        }

        function searchNewItem() {

            var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

            const data = {
                "assetTypeId": 1,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                "storeId": self.model.stopTransactionDetail.storeId
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
        // Against Indent Item Selection
        var isSelected = function (item) {
            return item.isSelected;
        }

        var openAddItem = function () {
            $("#addNewItemWasteStore").modal();
            GetAssetCategoryList();
        }       
    
        var SaveStopTransaction = function () {   
          		var store = self.model;
                var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.UPDATE_API + self.model.stopTransactionDetail.id;
                var data = {
                    "isVital": self.model.isVital ? self.model.isVital : false,
                    "isEssential": self.model.isEssential ? self.model.isEssential : false,
                    "isDiseriable": self.model.isDiseriable ? self.model.isDiseriable : false,
                    "isA": self.model.isA ? self.model.isA : false,
                    "isB": self.model.isB ? self.model.isB : false,
                    "isC": self.model.isC ? self.model.isC : false,
                    "statusId": 1,
                    "remark": self.model.remark,
                    "batchId": [],
                    "productCategoryExclusion":[], 
                    "stopTransactionId": [self.model.stopTransactionDetail.id], 
                    "itemExclusion": [], 
                    "items": [],
                    "unitId": $scope.unitId,
                    "userId": $scope.userId,
                    "organizationId":$scope.organizationId 
                };        

                    angular.forEach(self.model.SelectedStopItems, function (item, key) {
                        var storeItem = {
                            "batchId": item.batchId,
                            "uomTypeId": item.uomTypeId,
                            "uomUnitId":item.uomUnitId,
                            "statusId":item.statusId,
                            "itemId" :item.itemId
                        }             
                        
                        if (item.id == '' || item.id == null) {
                        		storeItem.batchId = item.batchId,
                                storeItem.uomTypeId = item.uomTypeId,
                                storeItem.uomUnitId = item.uomUnitId,                
                                storeItem.statusId = 1
                        }
                        data.items.push(storeItem);
                    });
                    
                angular.forEach(self.model.SelectedStopItems, function (item, key) {
                    data.batchId.push(item.batchId);
                });
               
                angular.forEach(store.productCategories, function (item, key) {
                    data.productCategoryExclusion.push({"productCategoryId":[item.pCategoryId]});
                 });           
                                          
                angular.forEach(store.itemDetailList, function (item, key) {
                	 data.itemExclusion.push({"itemId":[item.itemId]});
                 });            

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        // ClearRequests();
                        $rootScope.stopLoader();
                        NavigateToBack();
                        growl.success(response.data.message);
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Something Went Wrong');
                    });       	
            }
        
//        var SaveStopTransaction = function () {        	
//            var store = self.model;
//            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.UPDATE_API + self.model.stopTransactionDetail.id;
//            var data = {
//                "isVital": self.model.isVital ? self.model.isVital : false,
//                "isEssential": self.model.isEssential ? self.model.isEssential : false,
//                "isDiseriable": self.model.isDiseriable ? self.model.isDiseriable : false,
//                "isA": self.model.isA ? self.model.isA : false,
//                "isB": self.model.isB ? self.model.isB : false,
//                "isC": self.model.isC ? self.model.isC : false,
//                "statusId": 1,
//                "remark": self.model.remark,
//                "batchId": [],
//                "productCategoryExclusion":[], 
//                "stopTransactionId": [self.model.stopTransactionDetail.id], 
////                "productCategoryExclusion": [
////                    {
////                        "productCategoryId": self.model.proIds
////                    }
////                ],
//                "itemExclusion": [], 
//                "items": [] 
////                "itemExclusion": [
////                    {
////                        "itemId": self.model.SelectedIds
////                    }
////                ],
//
//
//
//            };        
//
//                angular.forEach(store.itemList, function (item, key) {
//                    var storeItem = {
//                        "batchId": item.batchId,
//                        "uomTypeId": item.uomTypeId,
//                        "uomUnitId":item.uomUnitId,
//                        "statusId":item.statusId,
//                        "itemId" :item.itemId
//                    }             
//                    
//                    if (item.id == '' || item.id == null) {
//                    		storeItem.batchId = item.batchId,
//                            storeItem.uomTypeId = item.uomTypeId,
//                            storeItem.uomUnitId = item.uomUnitId,                
//                            storeItem.statusId = 1
//                    }
//                    data.items.push(storeItem);
//                });
//                
//            angular.forEach(store.itemList, function (item, key) {
//                data.batchId.push(item.batchId);
//            });//            
//            angular.forEach(store.productCategories, function (item, key) {
//                data.productCategoryExclusion.push({"productCategoryId":[item.pCategoryId]});
//             });
//            
//            // angular.forEach(store.productCategories, function (item, key) {
//            //     data.productCategoryExclusion.push(item.id);
//            // });
//            // var proItem = {
//            //     "productCategoryId": self.model.proIds
//            // }
//
//
//                        
//            angular.forEach(store.itemDetailList, function (item, key) {
//            	 data.itemExclusion.push({"itemId":[item.itemId]});
//             });
//            
////             var excludedItem = {
////                 "itemId": self.model.SelectedIds
////             }
////             data.itemExclusion.push(excludedItem); 
//
//
//            $rootScope.startLoader();
//            GenericService.serviceAction("PATCH", URI, data).then(
//                function (response) {
//                    // ClearRequests();
//                    $rootScope.stopLoader();
//                    NavigateToBack();
//                    growl.success(response.data.message);
//                },
//                function (err) {
//                    $rootScope.stopLoader();
//                    growl.error('Something Went Wrong');
//                });
//        }

        var NavigateToBack = function () {
            $state.go('stopTransaction');
        }

        $scope.searchItem = function (search) {
            var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.SEARCH_ITEM;            
            if(self.model.isVital)
                self.model.vedId.push(1);
            if(self.model.isEssential)
               self.model.vedId.push(2);
            if(self.model.isDiseriable)
                self.model.vedId.push(3);            
            var data = {
                "fromDate": moment(self.model.startDateTime).format('YYYY-MM-DD'),
                "toDate": moment(self.model.stopDateTime).format('YYYY-MM-DD'),
                "assetTypeId": 1,
                "storeId": self.model.stopTransactionDetail.storeId,
                "vedId": self.model.vedId ? self.model.vedId : undefined,
                "unitId": $scope.unitId,
                "itemName": search
            };
            $rootScope.startLoader()

            var percent = 0;
            if (self.model.isA) percent = 70;
            if (self.model.isB) percent = percent + 20;
            if (self.model.isC) percent = percent + 10;

            GenericService.serviceAction("POST", URI, data).then(
            function (response) {
                $rootScope.stopLoader();
                self.model.itemList = response.data;

                var totalAmount = 0;
                angular.forEach(self.model.itemList, function (item, key) {
                    totalAmount = totalAmount + item.totalMrp;
                    item.cop = item.cop.toFixed(2);
                    item.mrp = item.mrp.toFixed(2);
                });

                var percentAmount = (totalAmount * percent) / 100;
                totalAmount = 0;
                var itemList = self.model.itemList;
                angular.forEach(itemList, function (item, key) {
                    totalAmount = totalAmount + item.totalMrp;
                    if (percent > 0 && totalAmount > percentAmount)
                        self.model.itemList.splice(key, itemList.length);

                });
                self.model.newItemList = [];
                self.model.newItemList = self.model.itemList;
                self.model.vedId = [];
            },
            function errorCallback(response) {
                $rootScope.stopLoader();
                growl.error('Error');
            });      
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewStopTransaction", {
            url: "/addNewStopTransaction",
            templateUrl: 'views/PharmacyInventory/StockAdjustment/add-new-stop-transaction.html',
            controller: "AddNewStopTransaction.Controller",
            controllerAs: "vm",
            title: 'Stop Transaction'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewStopTransaction.Controller", addNewStopTransactionController);
})(); 