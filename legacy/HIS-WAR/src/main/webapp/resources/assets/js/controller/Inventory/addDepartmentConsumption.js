(function () {
    "use strict";

    function addDepartmentConsumption($scope, $rootScope, $http,$window,$timeout, commonDetailService,PaginationService, $state, CONSTANTS, GenericService, growl,$cookies,$filter) {
        var self = this;
        var batchId;
        //Default Constructor
        var cookieObjectAppConfig = {};
        [cookieObjectAppConfig] = $rootScope.checkCookies();

        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;

        if (cookieObjectAppConfig == undefined) {
        $state.go('login');
        return;
        }
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
                SelectedItemIds: [],
                NewItemList: [],
                itemList: []


            };

            self.model.GetItemList = GetItemList;
            self.model.GetAssetCategoryList = GetAssetCategoryList;
            self.model.GetProductCategoryList = GetProductCategoryList;
            self.model.searchNewItem = searchNewItem;
            self.model.isSelected = isSelected;
            self.model.SelectAllItems = SelectAllItems;
            self.model.SelectItem = SelectItem;
            self.model.getAllItemDetails = getAllItemDetails;
            self.model.removeRowData = removeRowData;
            self.model.SaveNonBillableConsumption = SaveNonBillableConsumption;
            self.model.NavigteToBack = NavigteToBack;
            self.model.GetConsumptionDetails = GetConsumptionDetails;
            self.model.SaveNewConsumption = SaveNewConsumption;
            self.model.validateSave = validateSave;
            self.model.onStoreChange = onStoreChange;
            self.model.confirmClear = confirmClear;
            self.model.itemRemovePopup = itemRemovePopup;
            self.model.SetOldValueStore = SetOldValueStore;
            self.model.ConfirmStorePopup = ConfirmStorePopup;
            self.model.ChangeFromStore = ChangeFromStore;
            self.model.RemoveItem = RemoveItem;
            self.model.ConfirmRemove = ConfirmRemove;
            self.model.ValidateQuantity = ValidateQuantity;
            self.model.batchChange = batchChange;
            self.model.SelectedItemsIds = [];
            self.model.NewItemList1 = [];
            self.model.reqAssetTypeId=1;
            self.model.SetOldValueAssetType = SetOldValueAssetType;
            self.model.ChangeFromAssetType = ChangeFromAssetType;
            self.model.BarcodeScan = BarcodeScan;
            self.model.ClearBarcode = ClearBarcode;
            SetFocus();
            PopulateDropdown();
        }

        var ClearBarcode = function () {
            self.model.barcode = '';
        }

        var BarcodeScan = function (event, barcode) {
            var enterCode = 13;
            if(self.model.barcode == undefined || self.model.barcode.length == 0 || self.model.barcode == null)
    		{
    		growl.error("Barcode Number Should Not Be Blank...")
    		return false;
    		}
            
            if(!barcode) 
    		{
    	  		barcode = self.model.barcode;
			}

            if (!!barcode) {
//                var itemId = barcode.split(' ')[0];
                var batchId = barcode;

                if (!!batchId) {
                	
                	var data = {
                  		  
              		       "priPrintedBarcodeNo":self.model.barcode,
              		        "unitId": $scope.unitId,
              		        "items":[]
              		      };
              	
              	var URI = CONSTANTS.PROCUREMENT.QUOTATION.GET_BATCHES_API+"/prePrintedBarcode/getBarcodes";
                  $rootScope.startLoader();
                  GenericService.serviceAction("POST", URI, data).then(function (response) {        
           	        $rootScope.stopLoader();
           	        $scope.prePrintedBarcode= false;
           	        if(response.data.length > 0)
           	        {
           	        angular.forEach(response.data, function (value, key) {
           	          if(response.data[0].isManualBarcodeRequired==true )
           		        {
           	        	  if(value.prePrintedBarcode==barcode){
           	                
           	               $scope.prePrintedBarcode= true;
           	               	  $rootScope.stopLoader();
                             $scope.getBarcodeDetails(value.batchId); 
           	        	  }
           	        	 else{
           	        		$rootScope.stopLoader();
               	        	$scope.getBarcodeDetails(batchId);
               	          }
           		        } 
           	          else{
           	        	$rootScope.stopLoader();
           	        	$scope.getBarcodeDetails(batchId);
           	          }
           	        });
                  }
           	     else{
           	    	$rootScope.stopLoader();
           	    	$scope.getBarcodeDetails(batchId);
           	     }
                  });
                  
                }
            }
        }

        var SetFocus = function () {
            self.model.barcode = undefined;
            $window.document.getElementById('barcode').focus();
        }

        var AddItem = function (batchId) {
            var check = false;
            angular.forEach(self.model.itemList,function(value,key){
                  
                      if(batchId == value.batchId) {
                          check = true;
                      }
                      
                });
            
            if(check==true){
                growl.error('There should not be the same batch for the same item.'); 
                
              return false;
             }
            angular.forEach(self.model.NewUniqueItemList, function(data){
                if(data.batchId == parseInt(batchId)){
                    data.uomResponses.find(function(d){
                        if(d.uomTypeId == data.uomTypeId){
                            data.uomType = d.uomDetails;
                        }
                    });
                    
             angular.forEach(data.batches, function(selectedbatch){
                if(selectedbatch.batchNo == data.batchNo && selectedbatch.expiry == data.expiry)  
                
                    data.selectedBatch = selectedbatch;
                });
                    
                    self.model.itemList.push(data);
                }
            });
        }

        $scope.getBarcodeDetails = function(batchId){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        	var batchId = batchId;
        if (!!batchId) {
        	
        	 var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;

             if(self.model.visitType){
                 if (self.model.visitType.id == 1) {
                     visitNumber = self.model.visitNumber.visitNo;
                 } else {
                     visitNumber = self.model.visitNumberText;
                 }
             }

             var data = {
                 "assetTypeId": 1,
                 "storeId": self.model.store.id ? self.model.store.id : undefined,
                 "assetCategoryId": self.model.assetCategory ? self.model.assetCategory.id : undefined,
                 "productCategoryId": self.model.productCategory ? self.model.productCategory.id : undefined,
                 "itemCode": self.model.itemCode,
                 "itemName": self.model.itemName,
                 "batchName": self.model.batchNo,
                 "visitTypeId": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                 "admissionId": self.model.admissionId !=undefined ? self.model.admissionId : undefined,
                 "encounterId":self.model.encounterId !=undefined ? self.model.encounterId : undefined
             }
             self.model.NewItemList = [];
     
             //added by minesh for search new item function response (i.e : SearchNewItem)
             self.model.NewUniqueItemList = [];  

             $rootScope.startLoaderPopup();
             GenericService.serviceAction("POST", URI, data).then(
             function (response) {
                 $rootScope.stopLoaderPopup();
                 self.model.pagination.data=[];
                 self.model.pagination.totalPages=0;
                 self.model.NewItemList = response.data;
                 // added by minesh for search new item function response
                 // (i.e : SearchNewItem)
                 self.model.NewUniqueItemList = getUniqueSearchNewItemResponse(response.data);
                 console.log(self.model.NewUniqueItemList);
                 // added by minesh for add getitem with same itemId from
                 // NewItemList
                 for (let item of self.model.NewUniqueItemList){
                     item.batches= getItemFromBatch(item.itemCode);
                 
                     /*Added by Atmiya 20191114 : sort batch by expiry date
                     * select by default first batch after sort*/
                     item.batches.sort(item.batches.expiry);
                     item.selectedBatch = item.batches[0];
                     /*End Added by Atmiya 20191114*/
                 } 
                 // end added by minesh for add getitem with same itemId
                 // from NewItemList    
                 //added by minesh 02-01-2020 for single Unique Batch
                 $scope.newList = self.model.NewUniqueItemList;
                 angular.forEach(self.model.NewUniqueItemList, function(list,key){
                     var batchNo = [];
                     var uniqueBatches = [];
                     list.batches.forEach(function (batch) {
                         var index = batchNo.indexOf(batch.batchNo);                    			 
                         if(index === -1)
                         {
                             batchNo.push(batch.batchNo);
                             uniqueBatches.push(batch);
                         }else{
                                 uniqueBatches[index].availableQuantity += batch.availableQuantity;
                                 batch.availableQuantity = uniqueBatches[index].availableQuantity ;
                         }
                     });
                 });
                 AddItem(batchId);
                 //added by minesh 02-01-2020 for single Unique Batch
             },
             function errorCallback(response) {
                 $rootScope.stopLoaderPopup();
                 growl.error('Error');
             });
        	}
        	event.stopPropagation();
        	event.preventDefault();

        	SetFocus();
        }
        
        var PopulateDropdown = function () {
            var consumption = self.model;

            var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
            //consumption.store = consumption.stores[0];

            consumption.stores.splice(1);

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    consumption.stores = consumption.stores.concat(response.data);
                    var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
                    var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
                    for(var i=0;i<consumption.stores.length;i++)
                    {
                        if(consumption.stores[i].id==selectedStoreId)
                        {
                            consumption.store = consumption.stores[i];
                        }
                    }
                    $rootScope.stopLoader();
                    var consumptionId = commonDetailService.getDataId();
                    $rootScope.stopLoader();
                    if (!!consumptionId) {
                        commonDetailService.setDataId(null);
                        GetConsumptionDetails(consumptionId);
                    } else {
                        return false;
                    }
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        function GetItemList() {
            self.model.selectAllRows = false;
            self.model.action = 'Open';
            var popup = angular.element('#additemlist');
            popup.modal('show');
            self.model.isItemChecked = false;
            self.model.selectAllRows = false;
            self.model.search = {};
            GetAssetCategoryList();
            self.model.NewItemList = [];
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
        }
        
        var SetOldValueAssetType = function (typeId) {
            self.model.oldAssetTypeId = typeId;
            self.model.assetType = self.model.oldAssetTypeId;
          }
        
        var ChangeFromAssetType = function (typeId) {
            self.model.reqAssetTypeId = typeId;
             
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
         //   var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id;
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

        var batchChange = function (item){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            if(item.batchNo == item.selectedBatch.batchNo){
                item.availableQuantity = $scope.availableqty;
            }
            else{
                var data = item.selectedBatch;
                item.availableQuantity = data.availableQuantity;
            }
        }

        function searchNewItem() {
            if(self.model.search.itemName){
                 if(self.model.search.itemName!=undefined  &&  (self.model.search.itemName.replace(/\s/g, "").length) == 2 ){
                    //var URI = CONSTANTS.GLOBAL.BATCH_API;
                    var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
                    const data = {
                        "assetTypeId": self.model.reqAssetTypeId,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo
                    }

                    self.model.NewItemList = [];
                    self.model.NewUniqueItemList = [];  
                    self.model.pagination.data=[];
                    $scope.itemListWithZeroStock = [];
                    self.model.pagination.totalPages=0;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.NewItemList = response.data;
                        self.model.NewUniqueItemList = getUniqueSearchNewItemResponse(response.data);
                        console.log(self.model.NewUniqueItemList);
                        
                        // start code for resolved HIS-10035 Non Billable consumption - Newly created batches are not visible on the main screen
                        for (let item of self.model.NewUniqueItemList){
                            item.batches= getItemFromBatch(item.itemCode);
                            item.batches.sort(item.batches.expiry);
                            //item.selectedBatch = item.batches[0];
                            item.selectedBatch = item.batches.find(function (batch) {
                                return batch.batchId == item.batchId;
                            });
                        }
                        // end code for resolved HIS-10035 Non Billable consumption - Newly created batches are not visible on the main screen
                        
                        /* start code by minesh for club batch and consignment logic with item id*/
                        let UniqueItemList = [];
                        let objFound=undefined;
                        angular.forEach(self.model.NewUniqueItemList, function(item) {
                            objFound = UniqueItemList.find(function (uniqueItem) {
                                return uniqueItem.batchNo == item.batchNo && uniqueItem.itemId ==item.itemId;
                            });
                            if(!objFound)
                            {
                                if(item.availableQuantity != 0){
                                    UniqueItemList.push(item);
                                }
                            } else{
                                objFound.availableQuantity += item.availableQuantity;
                            }
                            if(item.uomResponses != undefined && item.uomResponses != null && item.uomResponses.length != 0){
                                item.uomResponses.find(function(d){
                                    if(d.uomTypeId == item.uomTypeId){
                                        item.uomType = d.uomDetails;
                                    }
                                });
                            }
                        });                    
                        self.model.NewItemList = UniqueItemList;
                        // end code by minesh for club batch and consignment logic /
                        $scope.itemListWithZeroStock = self.model.NewUniqueItemList;
                        self.model.NewUniqueItemList = [];
                        angular.forEach($scope.itemListWithZeroStock, function(d){
                            if(d.availableQuantity != 0){
                                self.model.NewUniqueItemList.push(d);
                            }
                        })
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewUniqueItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }else{
                                  			 if(self.model.search.itemName !=undefined &&  (self.model.search.itemName.replace(/\s/g, "").length) > 0){
                                                     var list =  $filter('filter')(self.model.NewUniqueItemList, {
                                                                         $: self.model.search.itemName
                                                                        });

                                                       PaginationService.searchDataByField(list, self.model.pagination);
                                                   }

                                  	}
            }
            else if(self.model.search.itemCode){
                if(self.model.search.itemCode!=undefined  &&  (self.model.search.itemCode.replace(/\s/g, "").length) == 2 ){
                     var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
                    const data = {
                        "assetTypeId": self.model.reqAssetTypeId,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo
                    }

                    self.model.NewItemList = [];
                    self.model.NewUniqueItemList = [];  
                    self.model.pagination.data=[];
                    $scope.itemListWithZeroStock = [];
                    self.model.pagination.totalPages=0;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.NewItemList = response.data;
                        self.model.NewUniqueItemList = getUniqueSearchNewItemResponse(response.data);
                        console.log(self.model.NewUniqueItemList);
                        
                        // start code for resolved HIS-10035 Non Billable consumption - Newly created batches are not visible on the main screen
                        for (let item of self.model.NewUniqueItemList){
                            item.batches= getItemFromBatch(item.itemCode);
                            item.batches.sort(item.batches.expiry);
                            //item.selectedBatch = item.batches[0];
                            item.selectedBatch = item.batches.find(function (batch) {
                                return batch.batchId == item.batchId;
                            });
                        }
                        // end code for resolved HIS-10035 Non Billable consumption - Newly created batches are not visible on the main screen
                        
                        /* start code by minesh for club batch and consignment logic with item id*/
                        let UniqueItemList = [];
                        let objFound=undefined;
                        angular.forEach(self.model.NewUniqueItemList, function(item) {
                            objFound = UniqueItemList.find(function (uniqueItem) {
                                return uniqueItem.batchNo == item.batchNo && uniqueItem.itemId ==item.itemId;
                                });
                            if(!objFound)
                            {
                                if(item.availableQuantity != 0){
                                    UniqueItemList.push(item);
                                }
                            } else{
                                objFound.availableQuantity += item.availableQuantity;
                            }
                        });                    
                        self.model.NewItemList = UniqueItemList;
                        // end code by minesh for club batch and consignment logic /
                        $scope.itemListWithZeroStock = self.model.NewUniqueItemList;
                        self.model.NewUniqueItemList = [];
                        angular.forEach($scope.itemListWithZeroStock, function(d){
                            if(d.availableQuantity != 0){
                                self.model.NewUniqueItemList.push(d);
                            }
                        })
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewUniqueItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }else{
                                                   			 if(self.model.search.itemCode !=undefined &&  (self.model.search.itemCode.replace(/\s/g, "").length) > 0){
                                                                      var list =  $filter('filter')(self.model.NewUniqueItemList, {
                                                                                          $: self.model.search.itemCode
                                                                                         });

                                                                        PaginationService.searchDataByField(list, self.model.pagination);
                                                                    }

                                                   	}
            }
            else if(self.model.search.batchNo){
                 if(self.model.search.batchNo!=undefined  &&  (self.model.search.batchNo.replace(/\s/g, "").length) == 2 ){
                     var URI = CONSTANTS.GLOBAL.BATCH_API + "unit/"+$scope.unitId;
                    const data = {
                        "assetTypeId": self.model.reqAssetTypeId,
                        "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
                        "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
                        "storeId": self.model.store.id,
                        "itemCode": self.model.search.itemCode,
                        "itemName": self.model.search.itemName,
                        "batchName": self.model.search.batchNo
                    }

                    self.model.NewItemList = [];
                    self.model.NewUniqueItemList = [];  
                    self.model.pagination.data=[];
                    $scope.itemListWithZeroStock = [];
                    self.model.pagination.totalPages=0;
                    $rootScope.startLoaderPopup();
                    GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoaderPopup();
                        self.model.NewItemList = response.data;
                        self.model.NewUniqueItemList = getUniqueSearchNewItemResponse(response.data);
                        console.log(self.model.NewUniqueItemList);
                        
                        // start code for resolved HIS-10035 Non Billable consumption - Newly created batches are not visible on the main screen
                        for (let item of self.model.NewUniqueItemList){
                            item.batches= getItemFromBatch(item.itemCode);
                            item.batches.sort(item.batches.expiry);
                            //item.selectedBatch = item.batches[0];
                            item.selectedBatch = item.batches.find(function (batch) {
                                return batch.batchId == item.batchId;
                            });
                        }
                        // end code for resolved HIS-10035 Non Billable consumption - Newly created batches are not visible on the main screen
                        
                        /* start code by minesh for club batch and consignment logic with item id*/
                        let UniqueItemList = [];
                        let objFound=undefined;
                        angular.forEach(self.model.NewUniqueItemList, function(item) {
                            objFound = UniqueItemList.find(function (uniqueItem) {
                                return uniqueItem.batchNo == item.batchNo && uniqueItem.itemId ==item.itemId;
                                });
                            if(!objFound)
                            {
                                if(item.availableQuantity != 0){
                                    UniqueItemList.push(item);
                                }
                            } else{
                                objFound.availableQuantity += item.availableQuantity;
                            }
                        });                    
                        self.model.NewItemList = UniqueItemList;
                        // end code by minesh for club batch and consignment logic /
                        $scope.itemListWithZeroStock = self.model.NewUniqueItemList;
                        self.model.NewUniqueItemList = [];
                        angular.forEach($scope.itemListWithZeroStock, function(d){
                            if(d.availableQuantity != 0){
                                self.model.NewUniqueItemList.push(d);
                            }
                        })
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.NewUniqueItemList, self.model.pagination);
                        }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoaderPopup();
                        growl.error('Error');
                    });
                }else{
                                                   			 if(self.model.search.batchNo !=undefined &&  (self.model.search.batchNo.replace(/\s/g, "").length) > 0){
                                                                      var list =  $filter('filter')(self.model.NewUniqueItemList, {
                                                                                          $: self.model.search.batchNo
                                                                                         });

                                                                        PaginationService.searchDataByField(list, self.model.pagination);
                                                                    }

                                                   	}
            }
            else{
                self.model.NewItemList = [];
                self.model.NewUniqueItemList = [];  
                self.model.pagination.data=[];    
            }
        }

        function getUniqueSearchNewItemResponse(responseData){
        	/* var data=angular.copy(responseData);
            const uniqueResponse =  data.map(e=> e['itemCode']). 
            // store the keys of the unique objects
            map((e,i,final) =>final.indexOf(e) === i && i) 
            // eliminate the dead keys & return unique objects
            .filter((e)=> data[e]).map(e=>data[e]);
            return uniqueResponse;*/
        	
        	  var data=angular.copy(responseData);
              const uniqueResponse =  data.map(e=> e['batchId']). 
              // store the keys of the unique objects
              map((e,i,final) =>final.indexOf(e) === i && i) 
              // eliminate the dead keys & return unique objects
             .filter((e)=> data[e]).map(e=>data[e]);
              return uniqueResponse;
        }

        //for select dropdownbatch value 
        var  getItemFromBatch = function (itemCode){
            const getItemFromBatch =  self.model.NewItemList.filter(ic =>ic.itemCode == itemCode);
            return getItemFromBatch;
        }

        var SelectAllItems = function () {
            if (self.model.selectAllRows) {
                if(self.model.SelectedItemsIds){
                    self.model.data = self.model.NewItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedItemsIds.push(item.itemId);
                        return self.model.SelectedItemsIds;
                    });
                }
                else{
                    self.model.SelectedItemsIds = self.model.NewItemList.map(function (item) {
                        item.isSelected = true;
                        self.model.isItemChecked = true;
                        return item.itemId;
                    });
                }
            } else {
                self.model.SelectedItemsIds = [];
                self.model.NewItemList.forEach(function (item) {
                    item.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

        var SelectItem = function (item) {
            if (item.isSelected) {
                self.model.SelectedItemsIds.push(item.itemId);
                self.model.NewItemList1.push(item);
            } else {
                var index = self.model.SelectedItemsIds.indexOf(item.itemId);
                if (index > -1) {
                    self.model.SelectedItemsIds.splice(index, 1);
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
            $rootScope.startLoader();
            if(self.model.NewItemList1.length != 0){
                var batchQuantityUpdateList1 = [];
                self.model.NewItemList1.forEach(function (item) {
                    if(item.consumedQuantity == undefined || item.consumedQuantity == null){
                        item.consumedQuantity = 0;
                    }
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[0].conversion);
                            item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[0].conversion);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            
                        }
                        else{
                            item.receivedQtyCon = (item.consumedQuantity);
                            item.receivedQtyCalculation = (item.consumedQuantity);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                    }
                    else{
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[0].conversion +"*"+ item.uomResponses[1].conversion);
                            item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[0].conversion * item.uomResponses[1].conversion);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[1].conversion);
                            item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[1].conversion);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.consumedQuantity);
                            item.receivedQtyCalculation = (item.consumedQuantity);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                    }
                    self.model.SelectedItemsIds.forEach(function (sitem) {
                        if (item.itemId == sitem && item.isSelected == true) {
                            var isFound = self.model.itemList.find(function (x) {
                                return x.batchId == item.batchId;
                            });
                            if (!isFound) {
                                self.model.itemList.push(item);
                                $scope.availableqty = item.availableQuantity;
                                batchQuantityUpdateList1.push(item);
                            }
                            else {
                               // growl.error('Item batch already present in the list')
                            }
                        }
                       
                    });
                });
                // self.model.SelectedItemsIds = [];
                if(batchQuantityUpdateList1 !== undefined && batchQuantityUpdateList1 !== null && batchQuantityUpdateList1.length > 0){
                    $scope.updateBatchQuantity(batchQuantityUpdateList1);
                }
                $rootScope.stopLoader();
            }
            else{
                self.model.NewItemList.forEach(function (item) {
                    var batchQuantityUpdateList = [];
                    if(item.consumedQuantity == undefined || item.consumedQuantity == null){
                        item.consumedQuantity = 0;
                    }
                    if(item.isThreeUOMApplicable == false){
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[0].conversion);
                            item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[0].conversion);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            
                        }
                        else{
                            item.receivedQtyCon = (item.consumedQuantity);
                            item.receivedQtyCalculation = (item.consumedQuantity);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                    }
                    else{
                        if(item.uomTypeId ==1){
                            item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[0].conversion +"*"+ item.uomResponses[1].conversion);
                            item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[0].conversion * item.uomResponses[1].conversion);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                        else if(item.uomTypeId == 2){
                            item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[1].conversion);
                            item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[1].conversion);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                        else{
                            item.receivedQtyCon = (item.consumedQuantity);
                            item.receivedQtyCalculation = (item.consumedQuantity);
                            item.uomUnitData = item.uomResponses[2].uomUnit;
                        }
                    }
                    self.model.SelectedItemsIds.forEach(function (sitem) {
                        if (item.itemId == sitem && item.isSelected == true) {
                            var isFound = self.model.itemList.find(function (x) {
                                return x.batchId == item.batchId;
                            });
                            if (!isFound) {
                                self.model.itemList.push(item);
                                $scope.availableqty = item.availableQuantity;
                            }
                            else {
                              //  growl.error('Item batch already present in the list')
                            }
                        }
                    });
                });
                if(batchQuantityUpdateList !== undefined && batchQuantityUpdateList !== null && batchQuantityUpdateList.length > 0){
                    $scope.updateBatchQuantity(batchQuantityUpdateList);
                }
                $rootScope.stopLoader();
            }
            $rootScope.stopLoader();
        }

        function removeRowData(item) {
            var index = self.model.itemList.indexOf(item);
            self.model.itemList.splice(index, 1);
        }

        var SaveNewConsumption = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var store = self.model;
            var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.SAVE_API;
            var data = {
                "storeId": store.store.id,
                "statusId": 1,
                "approvalStatusId": 1,
                "remark": self.model.remark,
                "batch": [],
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId,
                "requestFrom":'DepartmentConsumption'
            };

            angular.forEach(store.itemList, function (item, key) {
                /*var storeItem = {
                    "batchId": item.batchId,
                    "uomTypeId": item.uomTypeId,
                    "uomUnitId": item.uomUnitId,
                    "consumedQuantity": item.consumedQuantity,
                    "statusId": 1
                }*/
                
            	/* start code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
            	// unknown bug  during testing HIS-10494
                var storeItem = {
                        "batchId": item.selectedBatch.batchId,
                        "uomTypeId": item.selectedBatch.uomTypeId,
                        "uomUnitId": item.selectedBatch.uomUnitId,
                        "consumedQuantity": item.consumedQuantity,
                        "statusId": 1
                    }
                /* end code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
                
                data.batch.push(storeItem);

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

        var NavigteToBack = function () {
            $state.go('departmentConsumption');
        }

        var GetConsumptionDetails = function (consumptionId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            // var opbId = openingBalanceService.getOpeningBalanceId();
            var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.DETAIL_API + consumptionId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.consumptionId = response.data.id;
                    self.model.remark = response.data.remark;
                    self.model.store = self.model.stores.find(function (consumption) {
                        return response.data.storeId == consumption.id;
                        //self.model.store.id = response.data.storeId;
                    });

                    self.model.itemList = [];
                    self.model.itemList = response.data.batch;
                    
                    
                    /* start code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
                    /* start code by minesh for club batch and consignment logic with item id */
                    let UniqueItemList = [];
                    let objFound=undefined;
                    angular.forEach(self.model.itemList, function(item) {
                    	angular.forEach(item.batches, function(itemBatch) {
	                    	objFound = UniqueItemList.find(function (uniqueItem) {
	                    		return uniqueItem.batchNo == itemBatch.batchNo && uniqueItem.itemId ==itemBatch.itemId;
	                    		});
	                    	if(!objFound)
	                    	{
	                    		UniqueItemList.push(itemBatch);
	                    		} else{
	                    			objFound.availableQuantity += itemBatch.availableQuantity;
	                    			}
                    	}); 
                    	
                    	item.batches = UniqueItemList;
                    	// end code by minesh for club batch and consignment logic /                            
                        
                    	item.selectedBatch = item.batches.find(function (batch) {
                            return batch.batchId == item.batchId;
                            });
                    	if(!item.selectedBatch){
                        	item.selectedBatch= item.batches[0];
                        	}
                    	
                    });
                    /* end code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */

                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        $scope.saveBtnDisabled=false;
        var SaveNonBillableConsumption = function () {
            $scope.saveBtnDisabled=true;
            if (validateSave()) {
                if (!self.model.consumptionId) {
                    SaveNewConsumption();
                } else {
                    UpdateBatchChange();
                }
            }
        }
        var UpdateBatchChange = function () {

            var consumption = self.model;
            var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.UPDATE_API + consumption.consumptionId;
            var data = {
                "remark": self.model.remark,
                "batch": []
            };

            angular.forEach(self.model.itemList, function (item, key) {
                var storeItem = {
                    "id": item.id,
                    "consumedQuantity": item.consumedQuantity
                    
                    /* start code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
                    ,"batchId" : item.selectedBatch.batchId,
                    "uomTypeId" : item.selectedBatch.uomTypeId,
                    "uomUnitId" : item.selectedBatch.uomUnitId,
                    "statusId": 1
                    /* end code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
                }

                if (item.id == '' || item.id == null) {
                    storeItem.batchId = item.batchId,
                        storeItem.uomTypeId = item.uomTypeId,
                        storeItem.uomUnitId = item.uomUnitId,
                        storeItem.consumedQuantity = item.consumedQuantity,
                        storeItem.statusId = 1
                }
                data.batch.push(storeItem);


            });
            $rootScope.startLoader();
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
            // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 
            if (ItemDetailList.length < 1) {
                growl.error("Please add items");
                isValid = false;
            }
            for (let ItemDetail of ItemDetailList) {
                if (!ValidateQuantity(ItemDetail)) {
                    isValid = false;
                }
                if(ItemDetail.selectedBatch == ""){
                    growl.error("Please select batch")
                    isValid = false;
                }
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }
        var onStoreChange = function () {
            if (self.model.itemList.length > 0) {
                $("#storeChange").modal()
            }
        }

        var itemRemovePopup = function (item) {
            self.model.itemRemove = '';
            self.model.itemRemove = item;
            $("#itemRemovePopup").modal()

        }

        var confirmClear = function () {
            self.model.itemList = [];

        }



        var SetOldValueStore = function (store) {
            self.model.oldStore = store;
            self.model.store = self.model.oldStore;
        }

        var ChangeFromStore = function (store) {
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
            self.model.onclose.isReject = false;
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
            self.model.NewItemList1 = newArray;
            var popup = angular.element('#confirmRemove');
            popup.modal('hide');
        }

        $scope.goBack = function(){
       	 	$state.go('departmentConsumption');
        }
        
        
        var ValidateQuantity = function (item) {
            var isValid = true;

            if (isNaN(parseFloat(item.consumedQuantity))) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error("Please Enter Consumed Quantity");
            } else if (parseInt(item.consumedQuantity) == 0) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Consumed Quantity cannot be zero');
            } else if (parseInt(item.consumedQuantity) > item.availableQuantity) {
                item.isQuantityValid = true;
                isValid = false;
                growl.error('Entered  consumed quantity is exceeding the stock available quantity');
            } else {
                item.consumedQuantity = parseInt(item.consumedQuantity);
                isValid = true;
            }

            return isValid;
        }

        $scope.calculateReceivedQty = function (item) {
            if(item.consumedQuantity == undefined || item.consumedQuantity == null || item.consumedQuantity == ""){
                item.consumedQuantity = 0;
            }
			if(item.isThreeUOMApplicable == false){
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[0].conversion);
					item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[0].conversion);
                    item.uomUnitData = item.uomResponses[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
					
				}
				else{
					item.receivedQtyCon = (item.consumedQuantity);
					item.receivedQtyCalculation = (item.consumedQuantity);
                    item.uomUnitData = item.uomResponses[2].uomUnit;
				}
			}
			else{
				if(item.uomTypeId == 1){
					item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[0].conversion+"*"+ item.uomResponses[1].conversion);
					item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[0].conversion * item.uomResponses[1].conversion);
                    item.uomUnitData = item.uomResponses[2].uomUnit;
				}
				else if(item.uomTypeId == 2){
					item.receivedQtyCon = (item.consumedQuantity +"*"+ item.uomResponses[1].conversion);
					item.receivedQtyCalculation = (item.consumedQuantity * item.uomResponses[1].conversion);
                    item.uomUnitData = item.uomResponses[2].uomUnit;
				}
				else{
					item.receivedQtyCon = (item.consumedQuantity);
					item.receivedQtyCalculation = (item.consumedQuantity);
                    item.uomUnitData = item.uomResponses[2].uomUnit;
				}
			}
		}

        $scope.updateBatchQuantity = function (batchQuantityUpdateList) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
            var URI = BASE_URL + ROOT_URL + "/api/global/items/billableConsumption/updateBatchQuantity";

            var data = {};
            var dataList = [];
            angular.forEach(batchQuantityUpdateList, function(item){
//                angular.forEach(item.batches, function(batch){
                data.batchId = item.batchId;
                data.storeId =  $cookies.getObject('cookieObject').storeId ;
                data.availableQuantity = item.availableQuantity;
                data.unitId = $scope.unitId;
                data.organizationId = $scope.organizationId;
                dataList.push(data);
//                });
            });

            if(dataList !== null && dataList.length > 0){
                GenericService.serviceAction("POST", URI, dataList).then(
                    function (response) {
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    });
            }
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addNewDepartmentConsumption", {
            url: "/addNewDepartmentConsumption",
            templateUrl: 'views/PharmacyInventory/InPatient/add-new-non-billable-consumption.html',
            controller: "AddNewDepartmentConsumption.Controller",
            controllerAs: "vm",
            title: 'Department Consumption'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("AddNewDepartmentConsumption.Controller", addDepartmentConsumption);
})();
  	/*Non Billable Consumption*/
