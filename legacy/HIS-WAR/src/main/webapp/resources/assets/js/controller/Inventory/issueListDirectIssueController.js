(function () {
  "use strict";
  var CACHE = {};

  function issueListDirectIssueController($scope,$timeout, $state, $rootScope, $http,  CONSTANTS, $filter, GenericService,PaginationService, commonDetailService, growl,$cookies) {
    var self = this;
    var mainStoreList = [];

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
        defaultStores: [{
          "store": "Select Store"
        }],
        carriers: [{
          "name": "Select Carrier"
        }],
        SelectedItemsIds: [],
        storeIndentData: [],
        selectAllRows: false,
        SelectedItemIds: [],
      };
      // self.model.SelectedStore = selectedStore;
      self.model.GetItemList = getItemList;
      self.model.GetAssetCategoryList = GetAssetCategoryList;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.SelectedAll = selectedAll;
      self.model.searchNewItem = searchNewItem;
      self.model.SelectItem = SelectItem;
      self.model.RemoveRowData = RemoveRowData;
      self.model.AddDuplicateRow = AddDuplicateRow;
      self.model.AddItems = AddItems;
      self.model.fillItemList = fillItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.ItemDetailList = [];
      // self.model.isConsignment = false;
      self.model.fromStoreId = null;
      self.model.toStoreId = null;
      self.model.priorityId = null;
      self.model.assetCategoryId = null;
      self.model.isItemChecked = false;
      self.model.productCategoryId = null;
      self.model.remark = null;
      self.model.assetCategoriesList = [];
      self.model.productCategoryList = [];
      self.model.searchNewItemList = [];
      self.model.saveIssue = saveIssue;
      self.model.batchOnChange = batchOnChange;
      self.model.NavigteToMaterialIssue = NavigteToMaterialIssue;
      self.model.validateDirectIssue = validateDirectIssue;
      self.model.removeMainRowData = removeMainRowData;
      self.model.onChangeStore = onChangeStore;
      self.model.confirmClear = confirmClear;
      self.model.itemRemovePopup = itemRemovePopup;
      self.model.smartIssueDirectIssue = smartIssueDirectIssue;
      getDropdownsList();
    }


    var getDropdownsList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      try {
        var directissue = self.model;

        directissue.fromStores = directissue.defaultStores;
        directissue.toStores = directissue.defaultStores;

        directissue.fromStores = directissue.defaultStores;
        directissue.toStores = directissue.defaultStores;
        directissue.carrier = directissue.carriers[0];
        directissue.fromStore = directissue.fromStores[0];
        //directissue.toStore = directissue.toStores[0];

        var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_ISSUE_LIST.DIRECT_ISSUE_DROPDOWN +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            directissue.fromStores = directissue.defaultStores.concat(response.data.fromStore);
            directissue.allStores = response.data.toStore;
            directissue.toStores = directissue.defaultStores.concat(response.data.toStore);
            directissue.carriers = directissue.carriers.concat(response.data.carrier);
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
            for (var i=0;i<directissue.toStores.length;i++)
            {
              if(directissue.toStores[i].id==selectedStoreId)
              {
                directissue.toStore=directissue.toStores[i];
              }
            }

            var issueId = commonDetailService.getDataId();
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var batchOnChange = function (selectedBatch, batch, item, index) {
      var conversionPurchase;
      var conversionStock;
      var conversionDespense;
      $scope.isValid = true;
       
      if(item != undefined){
        angular.forEach(item.batchList, function(bdata){
          if(item.batchList.length > 1){
            if(selectedBatch != ''){
              if(bdata.batchId == selectedBatch.batchId){
                growl.error("Please Select Different Batch");
                $scope.isValid = false;
                return false;
              }
            }
          }
        });
      }
     

      batch.expiry = selectedBatch.expiry;
      var storeUom = selectedBatch.uom.find(function (obj) {
        return obj.storeUom == true;
      });

      selectedBatch.uom.forEach(function (uom) {
        if (uom.uomTypeId == 1) {
          conversionPurchase = uom.conversion;
        }
        else if (uom.uomTypeId == 2) {
          conversionStock = uom.conversion
        }
        else {
          conversionDespense = uom.conversion
        }
      });

      if(storeUom != undefined){
        if(storeUom.uomTypeId != undefined){  
          batch.selectedBatch.uomType = storeUom.uomType;
          batch.selectedBatch.uomTypeId = storeUom.uomTypeId;
          batch.selectedBatch.uomUnit = storeUom.uomUnit;
          batch.selectedBatch.uomUnitId = storeUom.uomUnitId;
        }
      }
     
      angular.forEach(item.batchList, function(b){
        if(!b.batchId){
          b.availableStockQuantity = selectedBatch.availableStockQuantity;
          b.batchDetails = selectedBatch.batchDetails;
          b.batchId = selectedBatch.batchId;
          b.batchNo = selectedBatch.batchNo;
          b.displayQuantity = selectedBatch.displayQuantity;
          b.expiry = selectedBatch.expiry;
          b.sellingPrice = selectedBatch.sellingPrice;
          b.uomType = selectedBatch.uomType;
          b.uomTypeId = selectedBatch.uomTypeId;
          b.uomUnit = selectedBatch.uomUnit;
          b.uomUnitId = selectedBatch.uomUnitId;
        }
      });
      
      // item.batchList1 = item.batchList;
      angular.forEach(item.batchList, function(b, key){
        if(key == index && $scope.isValid == false){
          item.batchList.splice(index, 1);
          AddDuplicateRow(item);
        }
      });
      $scope.convertQtyOrderedOnBatchChange(selectedBatch, item);
    }

    var uomSelectEvent = function (batch) {
      batch.uom.forEach(function (uom) {
        if (uom.uomTypeId == batch.uomTypeId) batch.selectedUom = uom;
      });
    }

    var GetAssetCategoryList = function () {
      var directissue = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;
      directissue.assetCategory = directissue.assetCategories[0];
      directissue.productCategory = directissue.productCategories[0];

      directissue.assetCategories.splice(1);
      directissue.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          directissue.assetCategories = directissue.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetProductCategoryList = function (id) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var indents = self.model;
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
      indents.productCategory = indents.productCategories[0];

      indents.productCategories.splice(1);


      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indents.productCategories = indents.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    function removeRowData(index) {
      var searchNewItemList = self.model.searchNewItemList;
      self.model.searchNewItemList = searchNewItemList.slice(0, index).concat(searchNewItemList.slice(index + 1))
    }

    function selectedAll() {
      self.model.SelectedItemIds = [];
      self.model.searchNewItemList.forEach(function (x) {
        if (self.model.selectAllRows) {
          x.isSelected = true;
          self.model.SelectedItemIds.push(x.itemId);
        } else {
          x.isSelected = false;
        }
      });
    }

    function searchNewItem() {
      if(self.model.search.itemName){
        if(self.model.search.itemName.length >= 3){

//          var URI = CONSTANTS.GLOBAL.ITEM_API +"available/unit/"+$scope.unitId;
        	

          var URI = CONSTANTS.GLOBAL.ITEM_API +"available/unit/"+$scope.unitId;
        	

          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "storeId": self.model.toStore.id ? self.model.toStore.id : undefined,
            // "isConsignment": self.model.isConsignment ? self.model.isConsignment : undefined,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName
          }
          self.model.searchNewItemList = [];
          self.model.selectAllRows = false;
          self.model.isItemChecked = false;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoaderPopup();
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            self.model.searchNewItemList = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField(self.model.searchNewItemList, self.model.pagination);
            }
          },
          function errorCallback(response) {
            $rootScope.stopLoaderPopup();
            growl.error('Error', {
              title: response.status
            });
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
            "storeId": self.model.toStore.id ? self.model.toStore.id : undefined,
            // "isConsignment": self.model.isConsignment ? self.model.isConsignment : undefined,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName
          }
          self.model.searchNewItemList = [];
          self.model.selectAllRows = false;
          self.model.isItemChecked = false;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            $rootScope.stopLoaderPopup();
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            self.model.searchNewItemList = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField(self.model.searchNewItemList, self.model.pagination);
            }
          },
          function errorCallback(response) {
            $rootScope.stopLoaderPopup();
            growl.error('Error', {
              title: response.status
            });
          });
        }
      }
      else{
        self.model.searchNewItemList = [];
        self.model.pagination.data=[];
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

    $scope.convertQtyOrderedOnPlusButtonClick = function (uom,item) {
      var newUOM = [];
      if(item.isThreeUOMApplicable == false){
        item.uomListdata.find(function (value) {
          if (!!value.uomTypeId) {
            newUOM.push(value);
          }
        });
      }
      else{
        uom.find(function (value) {
          if (!!value.uomTypeId) {
            newUOM.push(value);
          }
        });
      }
      
      item.batchList.forEach(function (batch) {
        if(batch.selectedBatch.issueQuantity == undefined || batch.selectedBatch.issueQuantity == null || batch.selectedBatch.issueQuantity == ""){
          batch.selectedBatch.issueQuantity = 0;
        }
        var quanity=batch.selectedBatch.availableStockQuantity;
        if(item.isThreeUOMApplicable == false){
          if (item.uomTypeId == 1) {
            batch.selectedBatch.displayQuantity = Math.floor(batch.selectedBatch.availableStockQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            batch.selectedBatch.receivedQtyCon = (batch.selectedBatch.issueQuantity +"*"+ item.uomListdata[0].conversion);
            batch.selectedBatch.receivedQtyCalculation = (batch.selectedBatch.issueQuantity * item.uomListdata[0].conversion);
            batch.selectedBatch.uomUnitData = item.uomListdata[2].uomUnit;
          } else if (item.uomTypeId == 2) {
            batch.selectedBatch.displayQuantity = Math.floor(batch.selectedBatch.availableStockQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (item.uomTypeId == 3) {
            batch.selectedBatch.displayQuantity = Math.floor(batch.selectedBatch.availableStockQuantity / (item.uomListdata[2].conversion));
            batch.selectedBatch.receivedQtyCon = (batch.selectedBatch.issueQuantity);
            batch.selectedBatch.receivedQtyCalculation = (batch.selectedBatch.issueQuantity);
            batch.selectedBatch.uomUnitData = item.uomListdata[2].uomUnit;
          }
        }
        else{
          if (item.uomTypeId == 1) {
            batch.selectedBatch.displayQuantity = Math.floor(batch.selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
            batch.selectedBatch.receivedQtyCon = (batch.selectedBatch.issueQuantity +"*"+ newUOM[0].conversion +"*"+ newUOM[1].conversion);
            batch.selectedBatch.receivedQtyCalculation = (batch.selectedBatch.issueQuantity * newUOM[0].conversion * newUOM[1].conversion);
            batch.selectedBatch.uomUnitData = newUOM[2].uomUnit;
          } else if (item.uomTypeId == 2) {
            batch.selectedBatch.displayQuantity = Math.floor(batch.selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
            batch.selectedBatch.receivedQtyCon = (batch.selectedBatch.issueQuantity +"*"+ newUOM[1].conversion);
            batch.selectedBatch.receivedQtyCalculation = (batch.selectedBatch.issueQuantity * newUOM[1].conversion);
            batch.selectedBatch.uomUnitData = newUOM[2].uomUnit;
          } else if (item.uomTypeId == 3) {
            batch.selectedBatch.displayQuantity = Math.floor(batch.selectedBatch.availableStockQuantity / (newUOM[2].conversion));
            batch.selectedBatch.receivedQtyCon = (batch.selectedBatch.issueQuantity);
            batch.selectedBatch.receivedQtyCalculation = (batch.selectedBatch.issueQuantity);
            batch.selectedBatch.uomUnitData = newUOM[2].uomUnit;
          }
        }
      }) 
    }

    $scope.convertQtyOrderedOnBatchChange = function (selectedBatch,item) {
      var storeUom = selectedBatch.uom.find(function (obj) {
        return obj.uomTypeId == item.uomTypeId;
      });

      if(storeUom != undefined){
        if(storeUom.uomTypeId != undefined){  
          selectedBatch.uomType = storeUom.uomDetails;
          selectedBatch.uomTypeId = storeUom.uomTypeId;
          selectedBatch.uomUnit = storeUom.uomUnit;
          selectedBatch.uomUnitId = storeUom.uomUnitId;
        }
      }

      var newUOM = [];
     
      var quanity=selectedBatch.availableStockQuantity;
      if(selectedBatch.issueQuantity == undefined || selectedBatch.issueQuantity == null || selectedBatch.issueQuantity == ""){
        selectedBatch.issueQuantity = 0;
      }
      if(item.isThreeUOMApplicable == false){
        item.uomListdata.find(function (value) {
          if (!!value.uomTypeId) {
            newUOM.push(value);
          }
        });
        if (item.uomTypeId == 1) {
          selectedBatch.displayQuantity = Math.floor(selectedBatch.availableStockQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity +"*"+ item.uomListdata[0].conversion);
          selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity * item.uomListdata[0].conversion);
          selectedBatch.uomUnitData = item.uomListdata[2].uomUnit;
        } else if (item.uomTypeId == 2) {
          selectedBatch.displayQuantity = Math.floor(selectedBatch.availableStockQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
        } else if (item.uomTypeId == 3) {
          selectedBatch.displayQuantity = Math.floor(selectedBatch.availableStockQuantity / (item.uomListdata[2].conversion));
          selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity);
          selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity);
          selectedBatch.uomUnitData = item.uomListdata[2].uomUnit;
        }
      }
      else{
        selectedBatch.uom.find(function (value) {
          if (!!value.uomTypeId) {
            newUOM.push(value);
          }
        });
        if (item.uomTypeId == 1) {
          selectedBatch.displayQuantity = Math.floor(selectedBatch.availableStockQuantity / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
          selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity +"*"+ newUOM[0].conversion +"*"+ newUOM[1].conversion);
          selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity * newUOM[0].conversion * newUOM[1].conversion);
          selectedBatch.uomUnitData = newUOM[2].uomUnit;
        } else if (item.uomTypeId == 2) {
          selectedBatch.displayQuantity = Math.floor(selectedBatch.availableStockQuantity / (newUOM[1].conversion * newUOM[2].conversion));
          selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity +"*"+ newUOM[1].conversion);
          selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity * newUOM[1].conversion);
          selectedBatch.uomUnitData = newUOM[2].uomUnit;
        } else if (item.uomTypeId == 3) {
          selectedBatch.displayQuantity = Math.floor(selectedBatch.availableStockQuantity / (newUOM[2].conversion));
          selectedBatch.receivedQtyCon = (selectedBatch.issueQuantity);
          selectedBatch.receivedQtyCalculation = (selectedBatch.issueQuantity);
          selectedBatch.uomUnitData = newUOM[2].uomUnit;
        }
      } 
    }

    $scope.convertQtyOrdered = function (uom,item) {
      var newUOM = [];
     
      var quanity=item.stockAvailable;
      if(item.isThreeUOMApplicable != undefined && item.isThreeUOMApplicable == true)
      {
        item.uom.find(function (value) {
          if (!!value.uomTypeId) {
            newUOM.push(value);
          }
        });
        if (item.uomTypeId == 1) {
          item.newStockAvailableQuantity = Math.floor(item.stockAvailable / (newUOM[0].conversion * newUOM[1].conversion * newUOM[2].conversion));
        } else if (item.uomTypeId == 2) {
          item.newStockAvailableQuantity = Math.floor(item.stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
        } else if (item.uomTypeId == 3) {
          item.newStockAvailableQuantity = Math.floor(item.stockAvailable / (newUOM[2].conversion));
        }
      }
      else
      {
        item.uomListdata.find(function (value) {
          if (!!value.uomTypeId) {
            newUOM.push(value);
          }
        });
        if (item.uomTypeId == 1) {
          item.newStockAvailableQuantity = Math.floor(item.stockAvailable / (newUOM[0].conversion * newUOM[1].conversion));
        } else if (item.uomTypeId == 2) {
//               item.newStockAvailableQuantity = Math.floor(item.stockAvailable / (newUOM[1].conversion * newUOM[2].conversion));
        } else if (item.uomTypeId == 3) {
          item.newStockAvailableQuantity = Math.floor(item.stockAvailable / (newUOM[1].conversion));
        }
      }   
    };

    $scope.calculateReceivedQty = function (item, data) {
      if(data.issueQuantity == undefined || data.issueQuantity == null){
        data.issueQuantity = 0;
      }
      if(item.issueQuantity == undefined || item.issueQuantity == null){
        item.issueQuantity = 0;
      }
      if(data.isThreeUOMApplicable == false){
        if(data.uomTypeId == 1){
          data.receivedQtyCon = (data.issueQuantity +"*"+ item.uom[0].conversion);
          data.receivedQtyCalculation = (data.issueQuantity * item.uom[0].conversion);
          data.uomUnitData = item.uom[2].uomUnit;
          item.selectedBatch.receivedQtyCon = (item.issueQuantity +"*"+ item.selectedBatch.uom[0].conversion);
          item.selectedBatch.receivedQtyCalculation = (item.issueQuantity * item.selectedBatch.uom[0].conversion);
          item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
        }
        else if(data.uomTypeId == 2){
        
        }
        else{
          data.receivedQtyCon = (data.issueQuantity);
          data.receivedQtyCalculation = (data.issueQuantity);
          data.uomUnitData = item.uom[2].uomUnit;
          item.selectedBatch.receivedQtyCon = (item.issueQuantity);
          item.selectedBatch.receivedQtyCalculation = (item.issueQuantity);
          item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
        }
      }
      else{
        if(data.uomTypeId == 1){
          data.receivedQtyCon = (data.issueQuantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
          data.receivedQtyCalculation = (data.issueQuantity * item.uom[0].conversion * item.uom[1].conversion);
          data.uomUnitData = item.uom[2].uomUnit;
          item.selectedBatch.receivedQtyCon = (item.issueQuantity +"*"+ item.selectedBatch.uom[0].conversion+"*"+ item.selectedBatch.uom[1].conversion);
          item.selectedBatch.receivedQtyCalculation = (item.issueQuantity * item.selectedBatch.uom[0].conversion * item.selectedBatch.uom[1].conversion);
          item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
        }
        else if(data.uomTypeId == 2){
          data.receivedQtyCon = (data.issueQuantity +"*"+ item.uom[1].conversion);
          data.receivedQtyCalculation = (data.issueQuantity * item.uom[1].conversion);
          data.uomUnitData = item.uom[2].uomUnit;
          item.selectedBatch.receivedQtyCon = (item.issueQuantity +"*"+ item.selectedBatch.uom[1].conversion);
          item.selectedBatch.receivedQtyCalculation = (item.issueQuantity * item.selectedBatch.uom[1].conversion);
          item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
        }
        else{
          data.receivedQtyCon = (data.issueQuantity);
          data.receivedQtyCalculation = (data.issueQuantity);
          data.uomUnitData = item.uom[2].uomUnit;
          item.selectedBatch.receivedQtyCon = (item.issueQuantity);
          item.selectedBatch.receivedQtyCalculation = (item.issueQuantity);
          item.selectedBatch.uomUnitData = item.selectedBatch.uom[2].uomUnit;
        }
      }
    }

    var AddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
    	$scope.saveBtnDisabled=true;
      var conversionPurchase;
      var conversionStock;
      var conversionDespense;

      var URI = CONSTANTS.GLOBAL.BATCH_ITEM_API;
      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.fromStore.id,
        "toStoreId" :self.model.toStore.id,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId

      };

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          response.data.forEach(function (item1) {       	          	  
            var isFound = self.model.ItemDetailList.forEach(function (x) {
              if (x.itemId == item1.itemId)
                return true
            });
            if (!isFound) {
              if(item1.isThreeUOMApplicable == false){
                item1.uomListdata = item1.uom;
                item1.uom = [];
                angular.forEach(item1.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item1.uom.push(data);
                  }
                });
              }       
              self.model.ItemDetailList.push(item1);
            }

            self.model.ItemDetailList.forEach(function (item) {
              var storeUom = item.uom.find(function (obj) {
                return obj.storeUom == true;
              });
              if(storeUom) {
                item.uomType = storeUom.uomType;
                item.uomUnit = storeUom.uomUnit;
                item.uomTypeId = storeUom.uomTypeId;
                item.uomUnitId = storeUom.uomUnitId;
              }
 
              item.uomDatalist = item.uom.find(function (uom) {
                return item.uomTypeId == uom.uomTypeId;
              }); 
              if(item.uomDatalist!=undefined){
              item.uomType = item.uomDatalist.uomDetails;
              item.uomTypeId = item.uomDatalist.uomTypeId;
              }
              
              
              angular.forEach(item.batch, function(d){
                if(d.batchDetails == 'NA'){
                  d.batchNo = d.batchDetails;
                } 
              });

              if (item.batch) {
                if(item.batch.length > 0) {
                  item['batchList'] = [item.batch[0]];
                }
              }
              else {
                item['batchList'] = [];
              }
              item['batchList']['uomList'] = [];

              if(item.issueQuantity == undefined || item.issueQuantity == null || item.issueQuantity == ""){
                item.issueQuantity = 0;
              }

              if(item.isThreeUOMApplicable == false){
                item.uomListdata.forEach(function (uom) {
                  if (uom.uomTypeId == 1) {
                    conversionPurchase = uom.conversion;
                  }
                  else if (uom.uomTypeId == 2) {
                    conversionStock = uom.conversion
                  }
                  else {
                    conversionDespense = uom.conversion
                  }
                });

                if (item.uomTypeId == 1) {
                  item.newMaxOrderQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                  item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
                  item.receivedQtyCon = (item.issueQuantity +"*"+ conversionPurchase);
                  item.receivedQtyCalculation = (item.issueQuantity * conversionPurchase);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
                else if (item.uomTypeId == 2) {
                  item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
                  item.newMaxOrderQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
                }
                else {
                  item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
                  item.newMaxOrderQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
                  item.receivedQtyCon = (item.issueQuantity);
                  item.receivedQtyCalculation = (item.issueQuantity);
                  item.uomUnitData = item.uomListdata[2].uomUnit;
                }
              }
              else{
                item.uom.forEach(function (uom) {
                  if (uom.uomTypeId == 1) {
                    conversionPurchase = uom.conversion;
                  }
                  else if (uom.uomTypeId == 2) {
                    conversionStock = uom.conversion
                  }
                  else {
                    conversionDespense = uom.conversion
                  }
                });
  
                if (item.uomTypeId == 1) {
                  item.newMaxOrderQuantity = Math.round((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                  item.newStockAvailableQuantity = Math.round((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
                  item.receivedQtyCon = (item.issueQuantity +"*"+ conversionPurchase +"*"+ conversionStock);
                  item.receivedQtyCalculation = (item.issueQuantity * conversionPurchase * conversionStock);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else if (item.uomTypeId == 2) {
                  item.newStockAvailableQuantity = Math.round((item.stockAvailable / conversionStock) / conversionDespense);
                  item.newMaxOrderQuantity = Math.round((item.maxOrderQuantity / conversionStock) / conversionDespense);
                  item.receivedQtyCon = (item.issueQuantity +"*"+ conversionStock);
                  item.receivedQtyCalculation = (item.issueQuantity * conversionStock);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
                else {
                  item.newStockAvailableQuantity = Math.round(item.stockAvailable / conversionDespense);
                  item.newMaxOrderQuantity = Math.round(item.maxOrderQuantity / conversionDespense);
                  item.receivedQtyCon = (item.issueQuantity);
                  item.receivedQtyCalculation = (item.issueQuantity);
                  item.uomUnitData = item.uom[2].uomUnit;
                }
              }
              
              item.batchList.forEach(function (b){
                b.selectedBatch = item.batch[0];
                if(storeUom){
                b.selectedBatch.uomType = storeUom.uomDetails;
                b.selectedBatch.uomTypeId = storeUom.uomTypeId;
                b.selectedBatch.uomUnit = storeUom.uomUnit;
                }
//                if (storeUom.uomTypeId == 1) {
//                  b.selectedBatch.displayQuantity = Math.round((((b.availableStockQuantity / conversionPurchase) / conversionStock) / conversionDespense));
//                }
//                else if (storeUom.uomTypeId == 2) {
//                  b.selectedBatch.displayQuantity = Math.round((b.availableStockQuantity / conversionStock) / conversionDespense);
//                }
//                else {
//                  b.selectedBatch.displayQuantity = Math.round(b.availableStockQuantity / conversionDespense);
//                }
              });
              $scope.convertQtyOrdered(item.uom,item);
              if(item.batchList[0] == undefined){
                  item.batchList.uom = item.uom;
                  $scope.convertQtyOrderedOnPlusButtonClick(item.batchList.uom, item);
              }
              else{
                    $scope.convertQtyOrderedOnPlusButtonClick(item.batchList[0].uom,item);
              }

            });
          });

          
        //added by minesh 02-01-2020 for single Unique Batch
         
      /*	angular.forEach(self.model.ItemDetailList, function(list,key){
      		 var batchNo = [];
             var uniqueBatches = [];
      		if(list.batch){ //added if condition on 15-04-2020 (only condition not body code )
	      		list.batch.forEach(function (batch) {
	      			var index = batchNo.indexOf(batch.batchNo);                    			 
	              	if(index === -1)
	              	{
	              		batchNo.push(batch.batchNo);
	              		uniqueBatches.push(batch);
	              	}else{
	              			uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
	      					batch.availableStockQuantity = uniqueBatches[index].availableStockQuantity ;
	              		 if(uniqueBatches.length >=index)
                        {
                          uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
	      					        batch.availableStockQuantity = uniqueBatches[index].availableStockQuantity ;  
                        }
	              			
	              	}
	      		});
	      		list.batch=uniqueBatches; //added if condition on 15-04-2020 (only condition not body code)
      		}
      	
      	});*/
          //added by minesh 02-01-2020 for single Unique Batch
          
          
          // self.model.ItemDetailList.forEach(function (item) {


          // });
          // angular.forEach(self.model.ItemDetailList, function (item) {
         
          // });
          self.model.SelectedItemsIds = [];
          self.model.items = [];



          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.message);
        });
      angular.element('#addNewItemWasteStore').modal('hide');
    }

    var fillItemList = function (list) {
      list.forEach(function (item) {

        for (let batch of item.batch) {

          var defaultUOM = [];
          for (let uom of batch.uom) {
            defaultUOM.push({
              uomTypeId: uom.uomTypeId,
              uomUnitId: uom.uomUnitId,
              conversion: uom.conversion
            });
          }

          batch['defaultUOM'] = defaultUOM;
        }
        self.model.ItemDetailList.push(item);
      });
    }


    var AddDuplicateRow = function (item) {
      var data = (angular.copy(item.batchList[0]));
      data.expiry = "";
      data.uomType = '';
      data.uomUnit = '';
      data.issueQuantity = '';
      data.displayQuantity = '';
      data.batchNo = '';
      data.statusId = '';
      data.batchId = '';
      data.batchDetails = '';
      data.availableStockQuantity = '';
      data.sellingPrice = '';
      data.selectedBatch = [];
      item.batchList.push(data);
    }


    var RemoveRowData = function (item) {
      var index = item.batchList.indexOf(item);
      item.batchList.splice(index, 1);
    }
    // Against Indent Item Selection
    var isSelectedItem = function (item) {
      return item.isSelectedItem;

    }    // Against Indent Item Selection
    var isSelected = function (item) {
      return item.isSelected;
    }



    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        if(self.model.SelectedItemsIds){
          self.model.data = self.model.pagination.data.map(function (item) {
              item.isSelected = true;
              self.model.isItemChecked = true;
              self.model.SelectedItemsIds.push(item.itemId);
              return self.model.SelectedItemsIds;
          });
        }
        else{
          self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
            item.isSelected = true;
            self.model.isItemChecked = true;
            return item.itemId;
          });
        }
      } else {
        self.model.SelectedItemsIds = [];
        self.model.pagination.data.forEach(function (item) {
          item.isSelected = false;
          self.model.isItemChecked = false;
        });
      }
    }
    function getItemList() {
        $scope.saveBtnDisabled=false;
        self.model.SelectedItemsIds=[];
      if (self.model.fromStore.id == self.model.toStore.id) {
        growl.error("Please Select Different Stores");
      }else if(self.model.fromStore.id == undefined || self.model.fromStore.id == null || self.model.fromStore.id ==""){
    	  growl.warning("Please Select Requesting Store",{
				title : 'Warning!'
			});
			$rootScope.stopLoader();
  		return;
      }else if(self.model.toStore.id == undefined || self.model.toStore.id == null || self.model.toStore.id ==""){
    	  growl.warning("Please Select Issuing Store",{
				title : 'Warning!'
			});
			$rootScope.stopLoader();
		return;
    }
      else {
        self.model.pagination.data = [];
        self.model.pagination.totalPages = 0;
        self.model.action = 'Open';
        self.model.search = '';
        self.model.isItemChecked = false;
        var popup = angular.element('#addNewItemWasteStore');
        popup.modal('show');
        self.model.searchNewItemList = [];
        GetAssetCategoryList();
      }
    }

    $scope.saveDisabled = true;
    $scope.saveBtnDisabled=false;
    var saveIssue = function () {
    	$scope.saveBtnDisabled=true;
      $scope.saveDisabled = false;
      var store = self.model;


      var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_ISSUE_LIST.SAVE_API;
      if (validateDirectIssue()) {
        $scope.saveDisabled = false;
        var data = {
          "fromStoreId": self.model.fromStore.id,
          "toStoreId": self.model.toStore.id,
          "statusId": 4,
          "carrierId": self.model.carrier.id,
          "remark": self.model.remark ? self.model.remark : undefined,
          // "isConsignment": self.model.isConsignment ? self.model.isConsignment : false,
          "items": [],
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId,
          "userId":$scope.userId
        };

        angular.forEach(store.ItemDetailList, function (item, key) {
          if(item.issueQuantity !=0 && item.issueQuantity != undefined && item.issueQuantity != null){
            var storeItem = {
              "itemId": item.itemId,
              "issueQuantity": item.issueQuantity,
              "statusId": 4,
              "batches": []
            }
  
            angular.forEach(item.batchList, function (batch) {
//              angular.forEach(batch.uom, function(u){
//                if(batch.uomType == u.uomDetails){
//                  batch.uomTypeId = u.uomTypeId;
//                }
//              });
              angular.forEach(batch.uom, function(u){
                  if(batch.uomTypeId == u.uomTypeId){
                        batch.uomUnitId = u.uomUnitId;
                     }
                });
              var batch = {
               // "batchId": batch.batchId, //comment by minesh.
                "batchId": batch.selectedBatch.batchId, //added by minesh.
                "uomTypeId": batch.uomTypeId,
                "uomUnitId": batch.uomUnitId,
                "issueQuantity": parseInt(batch.issueQuantity)
              }
              storeItem.batches.push(batch);
            });
            data.items.push(storeItem);
          }
        });
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            // ClearRequests();
            $rootScope.stopLoader();
            NavigteToMaterialIssue();
            growl.success(response.data.message);
            $scope.saveDisabled = true;
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error('Something Went Wrong');
          });

      }

    }

    function validateDirectIssue() {

      // var isValid = true;
      // var ItemDetail = self.model.ItemDetailList;
      var tempList = [];
      $scope.count1 = 0;
      if(self.model.ItemDetailList.length > 1){
          angular.forEach(self.model.ItemDetailList, function(l){
              if(l.issueQuantity){
                  tempList.push(l);
                  $scope.count1++;
              }
          });
      }
      var isValid = true;
      if($scope.count1 != 0){
          var ItemDetail = tempList;
      }
      else{
          var ItemDetail = self.model.ItemDetailList;
      }
      if (ItemDetail.length == 0) {
        growl.error('Error', {
          title: "Please validate all required fields"
        });
        isValid = false;
        $scope.saveDisabled = true;
      }
      if(!self.model.fromStore.id){
        growl.error('error', {
          title: "Please Select Requesting Store"
        });
        isValid = false;
        $scope.saveDisabled = true;
      }
      if (self.model.fromStore.id == self.model.toStore.id) {
        growl.warning('Warning', {
          title: "Please Select Different Stores"
        });
        isValid = false;
        $scope.saveDisabled = true;
      }
//      for (let item of self.model.ItemDetailList) {
//        if (!item.issueQuantity || item.issueQuantity > item.newMaxOrderQuantity) {
//          growl.error("Please enter issue quantity less than or equal to max order quantity");
//          isValid = false;
//        }
//        else if (!item.issueQuantity) {
//          growl.error("Please enter issue quantity");
//          isValid = false;
//        }
//      }

      var isBatchSelected = false;
      
      for (let item of ItemDetail) {
        var abc=0;
    	  for (let batch of item.batchList) {
          if (batch.selectedBatch) {
            batch.selectedBatch.issueQuantity = parseInt(batch.selectedBatch.issueQuantity);
        	  abc += batch.selectedBatch.issueQuantity;
          }
        }
    	  isBatchSelected = true;
    	  if(item.newStockAvailableQuantity < abc){ 
          growl.error("Please enter issue quantity less than or equal to available stock quantity");
    	    isValid = false;
          $scope.saveDisabled = true;
    	  }
    	  else if (!item.issueQuantity) {
    	    isValid = false;
          $scope.saveDisabled = true;
    	  }

  
        if (!isBatchSelected) {
          if(item.isBatchRequired){
            growl.error("Please Select Batch");
            isValid = false;
            $scope.saveDisabled = true;
          }
        }
      }
      var counter=0;
      var quantityCounter=0;
      var batchIdArray=[];
      for (let item of ItemDetail) {
      	  for (let batch of item.batchList) {
      		batchIdArray.push(batch);
          }
      }
	      for (let batch of batchIdArray) {
	    	  if(batch.batchId!=undefined && batch.batchId!="")
	    		  {
	    		  counter++;
	    		  }
	    	  if(batch.issueQuantity!=undefined && batch.issueQuantity!="" && !isNaN(batch.issueQuantity))
	    		  {
	    		  quantityCounter++;
	    		  }
	      }
      	  if(counter!=batchIdArray.length)
      		  {
      		$scope.saveDisabled = true;
      		isValid=false;
      		growl.error("Please Select Batch");
      		  }
      	  if(quantityCounter!=batchIdArray.length)
      		  {
      		 growl.error("Please enter issue quantity");
      		$scope.saveDisabled = true;
      		isValid=false;
      		  }
          if(!isValid)
          {
          $scope.saveBtnDisabled=false;
          }
      return isValid;
    }


    var NavigteToMaterialIssue = function () {
      $state.go('materialIssue.issueList');
    }

    function removeMainRowData(item) {
      self.model.ItemDetailList.splice(item.index, 1);
    }

    var onChangeStore = function () {
      if (self.model.ItemDetailList.length > 0) {
        $("#storeChange").modal()
      }
    }

    var itemRemovePopup = function (item, index) {
        item.index = index;
        self.model.itemRemove = '';
        self.model.itemRemove = item;
        $("#itemRemovePopup").modal()

    }

    var confirmClear = function () {
      self.model.ItemDetailList = [];

    }
    var validateSmartIssue = function () {
      var isValid = true;
      angular.forEach(self.model.ItemDetailList, function (item, key) {
        if (!item.issueQuantity) {
          growl.error('Please Enter Issue Quantity')
          isValid = false;
        }
      });
      return isValid;
    }

			$scope.validateQty=function(item)
				{
				   if(item.issueQuantity!=undefined && item.issueQuantity!=null && item.issueQuantity!="")
					   {
					   if(parseInt(item.issueQuantity)>item.selectedBatch.displayQuantity)
						   {
						   growl.error("Issue Quantity Must Be Less Than Or Equal To Available Stock Quantity");
						   item.issueQuantity="";
						   return false
						   }
					   if(parseInt(item.issueQuantity)==0 || parseInt(item.issueQuantity)=="")
					   {
					   growl.error("Please Enter Valid Issue Quantity");
					   item.issueQuantity="";
					   return false
					   }
					   }
				}

    var smartIssueDirectIssue = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

      if (validateSmartIssue()) {

        var URI = CONSTANTS.GLOBAL.SMART_ISSUE;

        var data = {
          "storeId": self.model.fromStore.id,
          "items": []
        };
        angular.forEach(self.model.ItemDetailList, function (item, key) {
          var storeItem = {
            "itemId": item.itemId,
            "issueQuantity": item.issueQuantity
          }
          data.items.push(storeItem);
        });
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.smartIssueData = response.data;
            self.model.smartIssueData.forEach(function (smart) {
              angular.forEach(self.model.ItemDetailList, function (item, key) {
                if (smart.itemId == item.itemId) {
                  smart.batch.forEach(function (smartBatch) {
                    item.batch.forEach(function (batch) {
                      if (batch.batchId == smartBatch.batchId) {
                        item.batchList[0].selectedBatch = batch;
                        batchOnChange(item.batchList[0].selectedBatch, batch);
                        self.model.activeItem = smart.itemId;
                      }
                    });

                  });
                }
              });
            });
            $rootScope.stopLoader();
            growl.success(response.data.message);


          },

          function (err) {
            $rootScope.stopLoader();
            growl.error('Something Went Wrong');
          });
      }
    } 

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("issueListdirectIssue", {
      url: "/issueListdirectIssue",
      templateUrl: 'views/PharmacyInventory/InPatient/issue-list-directIssue.html',
      controller: "IssueListDirectIssue.Controller",
      controllerAs: "vm",
      title: 'Material Issue'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("IssueListDirectIssue.Controller", issueListDirectIssueController);
})();
