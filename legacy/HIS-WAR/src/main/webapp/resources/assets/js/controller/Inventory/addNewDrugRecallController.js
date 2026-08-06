(function () {
    "use strict";

    function addNewDrugRecallController($scope, $rootScope, $state, $http, commonDetailService, $cookies, CONSTANTS,PaginationService, GenericService, growl,stopTransactionService,$filter) {
        var self = this;
        var drugId;
        var selectedItemName;
        var selectedItemCode;
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

            };
            self.model.itemDetailList = [];
            self.model.itemList = [];
            self.model.batchIdList=[];
            self.model.batchDetailsList=[];
            self.model.expiryDateList=[];
            self.model.availableStockList=[];
            self.model.storeWiseItemList=[];
            self.model.patients=[];
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.searchNewItem = searchNewItem;
            self.model.AddItems = AddItems;
            self.model.openAddItem = openAddItem;
            self.model.searchBatchItem = searchBatchItem;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.SaveNewDrugRecall = SaveNewDrugRecall;
            self.model.updateOpen = updateOpen;
            self.model.GetDrugDetails = GetDrugDetails;
            self.model.UpdateDrugRecall = UpdateDrugRecall;
            self.model.SaveDrugRecall = SaveDrugRecall
            self.model.validateSave = validateSave;
            self.model.removeRowData = removeRowData;
            self.model.itemRemovePopup = itemRemovePopup;
            self.model.ValidateReason = ValidateReason;

            self.model.SetOldValueAgainst = SetOldValueAgainst;
            self.model.ChangeAgainst = ChangeAgainst;
            self.model.ConfirmAgainstPopup = ConfirmAgainstPopup;
            self.model.openItemDetails=openItemDetails;
            self.model.openBatchDetails=openBatchDetails;
            self.model.GetStoreStockDetails=getItemWiseStoreStock;
            self.model.getBatchWiseStoreStock=getBatchWiseStoreStock;
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            $scope.storeId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
            
            updateOpen();

        }

        var updateOpen = function () {
            var drugId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!drugId) {
                commonDetailService.setDataId(null);
                GetDrugDetails(drugId);
            } else {
                return false;
            }
        }


        var GetDrugDetails = function (drugId) {
            var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.DETAIL_API + drugId;

            $rootScope.startLoaderPopup();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.drugId = response.data.id;
                    self.model.againstId = response.data.requestTypeId.toString();
                    self.model.itemDetailList = [];
                    self.model.itemList = [];
                    self.model.itemDetailList = response.data.items;
                    
                    /* start code for resolved HIS-10205 Expiry Date and Batch No. not Getting Display in Drug Recall */
                    angular.forEach(self.model.itemDetailList,function(item){
                    	item.expiry = item.expiry ? item.expiry : item.expiryDate;
                    });
                    /* end code for resolved HIS-10205 Expiry Date and Batch No. not Getting Display in Drug Recall */

                    $rootScope.stopLoaderPopup();
                },
                function (err) {
                    $rootScope.stopLoaderPopup();
                });
        }

        var getItemWiseStoreStock=function (itemId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.ITEM_WISE_BATCH_API + $scope.unitId + '/' +itemId
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeWiseItemList=response.data;
                    console.log('Stock : ',self.model.storeWiseItemList);
                    $rootScope.stopLoaderPopup();
                },
                function (err) {
                    $rootScope.stopLoaderPopup();
                });
            getPatientsListForItem(itemId);

        }

        var getBatchWiseStoreStock=function (batchId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.INVENTORY.STORE.STORE_STOCK_DASHBOARD.BATCH_WISE_BATCH_API+$scope.unitId + "/"+batchId
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeWiseItemList=response.data;
                    console.log('Stock : ',self.model.storeWiseItemList);
                    $rootScope.stopLoaderPopup();
                },
                function (err) {
                    $rootScope.stopLoaderPopup();
                });
            getPatientsListForBatch(batchId);

        }

        var getPatientsListForItem=function (itemId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.ISSUE_SEARCH_FOR_DRUG_RECALL_API;
            var data = {
               // "storeId":1,
                "itemId":itemId,
                "unitId":$scope.unitId

            }
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.patients=response.data;
                    console.log('Item Wise Patients : ',self.model.patients);
                    $rootScope.stopLoaderPopup();
                },
                /*function (err) {
                    $rootScope.stopLoader();
                });*/
            function errorCallback(response) {
                $rootScope.stopLoaderPopup();
                growl.error('Error');
            });


        }

        var getPatientsListForBatch=function (batchId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.ISSUE_SEARCH_FOR_DRUG_RECALL_API;
            var data = {
                //"storeId":1,
                "batchId":batchId
            }
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.patients=response.data;
                    console.log('Batch Wise Patients : ',self.model.patients);
                    $rootScope.stopLoaderPopup();
                },
                /*function (err) {
                    $rootScope.stopLoader();
                });*/
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });


        }

        var GetAssetCategoryList = function () {
            var drug = self.model;
            var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

            drug.assetCategory = drug.assetCategories[0];
            drug.productCategory = drug.productCategories[0];

            drug.assetCategories.splice(1);
            drug.productCategories.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    drug.assetCategories = drug.assetCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        // Against Indent Item Selection
        var isSelected = function (item) {
            return item.isSelected;
        }


        var GetProductCategoryList = function (id) {
            var drug = self.model;

            //TODO : Add asset category to the uri
            var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
            drug.productCategory = drug.productCategories[0];

            drug.productCategories.splice(1);
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    drug.productCategories = drug.productCategories.concat(response.data);
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }



        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
                    item.isSelected = true;
                    self.model.isItemChecked = true;
                    console.log("Selected Item Id : ",item.itemId," Name : ",item.name);
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

        function searchNewItem() {
            if(self.model.search.itemName){
                if(self.model.search.itemName.length >= 3){
                    var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                    }
                    self.model.pagination.totalPages=0;
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;

                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.searchNewItemList = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.searchNewItemList, self.model.pagination);
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
                    var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

                    const data = {
                        "assetTypeId": 1,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                    }
                    self.model.pagination.totalPages=0;
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;

                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.searchNewItemList = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.searchNewItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }
            }
            else{
                self.model.searchNewItemList = [];
                self.model.pagination.data=[];
            }
        }

        var AddItems = function () {
        	ActaulAddItems();
        	/*
        	var stopData = {
   	                "itemIds": self.model.SelectedItemsIds,
   	                "storeId": $scope.storeId	      		 
   	          };
        	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
                var StopTransactionData = responseData;  
                if(StopTransactionData.length > 0 ){    
                         var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
                         var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
                         growl.error("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                    
                         $rootScope.stopLoader(); 
                         self.model.searchNewItemList = [];
                         self.model.SelectedItemsIds = [];
                         return false;
                 }
                else{
                	ActaulAddItems();
                }
       	  });      	
        */}
        var ActaulAddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
            var data = {
                "itemIds": self.model.SelectedItemsIds,
                "storeId": $scope.storeId
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    response.data.forEach(function (item) {
                        var isFound = self.model.itemDetailList.find(function (x) {
                            return x.itemId == item.itemId;
                        });
                        if (!isFound) {
                            self.model.itemDetailList.push(item);
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

        var openAddItem = function () {
            self.model.isItemChecked = false;
            self.model.selectAllRows = false;
            self.model.searchNewItemList = [];
            self.model.search = {};
            if (self.model.againstId == 10) {
                self.model.pagination.data=[];
                self.model.pagination.totalPages=0;
                $("#addNewItemWasteStore").modal();
            }
            else if (self.model.againstId == 11) {
                self.model.pagination.data=[];
                self.model.pagination.totalPages=0;
                $("#getItemListDrugRecall").modal();
            }

            GetAssetCategoryList();
        }

        var openItemDetails=function (itemId) {
            $("#detailsDrugRecallModal").modal();
            console.log('Inside openItemDetails');
            getItemWiseStoreStock(itemId);

        }
        var openBatchDetails=function (batchId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            $("#detailsDrugRecallModal").modal();
            console.log('Inside openBatchDetails');
            getBatchWiseStoreStock(batchId);

        }

        function searchBatchItem() {

            var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
            const data = {
                "assetTypeId": 1,
                "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                "storeId": 1,
                "itemCode": self.model.search.itemCode,
                "itemName": self.model.search.itemName,
                "batchName": self.model.search.batchNo,
            }

            self.model.searchNewItemList = [];
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoaderPopup();
                    self.model.searchNewItemList = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchDataByField(self.model.searchNewItemList, self.model.pagination);
                    }
                },
                function errorCallback(response) {
                    $rootScope.stopLoaderPopup();
                    growl.error('Error');
                });
        }


        function getAllItemDetails() {
            self.model.itemDetailList = [];
            self.model.searchNewItemList.forEach(function (item) {
                if (item.isSelected == true) {
                    self.model.itemDetailList.push(item);
                }
            });

        }

        var SaveDrugRecall = function () {
            if (validateSave()) {
                if (!self.model.drugId) {
                    SaveNewDrugRecall();
                } else {
                    UpdateDrugRecall();
                }
            }
        }
        var UpdateDrugRecall = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

            var drug = self.model;
            var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.UPDATE_API + drug.drugId;
            var data = {
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "storeId":$scope.storeId

            };

            if (self.model.againstId == 10) {
                angular.forEach(drug.itemDetailList, function (item, key) {
                    var storeItem = {
                        "id": item.id,
                        "itemId": item.itemId,
                        "reason": item.reason
                    }
                    data.items.push(storeItem);

                });
            }
            else {
                angular.forEach(drug.itemDetailList, function (item, key) {
                    var storeItem = {
                        "id": item.id,
                        "itemId": item.itemId,
                        "batchId": item.batchId,
                        "reason": item.reason
                    }
                    data.items.push(storeItem);

                });
            }
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
        var SaveNewDrugRecall = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.SAVE_API;
            var data = {
                "requestTypeId": parseInt(self.model.againstId),
                "statusId": 1,
                "items": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "storeId":$scope.storeId

            };
            if (self.model.againstId == 10) {
                angular.forEach(store.itemDetailList, function (item, key) {
                    var storeItem = {
                        "itemId": item.itemId,
                        "reason": item.reason
                    }
                    data.items.push(storeItem);

                });
            }
            else {
                angular.forEach(store.itemDetailList, function (item, key) {
                    var storeItem = {
                        "itemId": item.itemId,
                        "batchId": item.batchId,
                        "reason": item.reason
                    }
                    data.items.push(storeItem);

                });
            }
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

        var NavigteToBack = function () {
            $state.go('drugRecall');
        }



        var validateSave = function () {
            var isValid = true;
            var ItemDetailList = self.model.itemDetailList;

            if (ItemDetailList.length < 1) {
                growl.error("Please validate all required fields");
                isValid = false;
            }
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateReason(ItemDetail)) {
                    isValid = false;
                }
            }
            return isValid;
        }


        var ValidateReason = function (item) {
            var isValid = true;

            if (!item.reason) {
                item.isReasonValid = true;
                isValid = false;
                growl.error("Please Enter Reason");
            }

            return isValid;
        }

        function removeRowData(item) {
            var index = self.model.itemDetailList.indexOf(item);
            self.model.itemDetailList.splice($scope.indexNo, 1);
        }

        
        var itemRemovePopup = function (item, index) {
        	$scope.indexNo=index;
            self.model.itemRemove = '';
            self.model.itemRemove = item;
            $("#itemRemovePopup").modal()

        }

        var SetOldValueAgainst = function (againstId) {
            self.model.oldAgainst = againstId;
            self.model.againstId = self.model.oldAgainst;
        }

        var ChangeAgainst = function (againstId) {
            if (!!self.model.itemDetailList.length) {
                self.model.againstId = self.model.oldAgainst;
                self.model.againstIdNew = JSON.parse(JSON.stringify(againstId));
                $("#storeChange").modal()
            }

        }
        var ConfirmAgainstPopup = function () {
            self.model.againstId = self.model.againstIdNew;
            self.model.itemDetailList = [];
            ModalDismiss('storeChange');

        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
          }
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("AddNewDrugRecall", {
            url: "/AddNewDrugRecall",
            templateUrl: 'views/PharmacyInventory/Store/add-new-drug-recall.html',
            controller: "AddNewDrugRecall.Controller",
            controllerAs: "vm",
            title: 'Drug Recall'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewDrugRecall.Controller", addNewDrugRecallController);
})();

