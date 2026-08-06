(function () {
  "use strict";

  function addNewStoreIndentingController($scope, $state, $rootScope, $http, $filter, CONSTANTS, StatusService,PaginationService, GenericService, commonDetailService, growl,$cookies) {
    var self = this;

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
        statuses: [{
          "status": "All"
        }],
        SelectedItemsIds: [],
        storeIndentData: [],
        selectAllRows: false,
        SelectedItemIds: [],
        ItemDetailList: [],
      };

      self.model.GetAssetCategoryList = GetAssetCategoryList;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.IsMainStore = IsMainStore;
      self.model.GetItemList = GetItemList;
      self.model.searchNewItem = searchNewItem;
      self.model.SelectItem = SelectItem;
      self.model.SelectAllItems = SelectAllItems;
      self.model.AddItems = AddItems;
      self.model.isSelected = isSelected;
      self.model.SaveStoreIndent = SaveStoreIndent;
      self.model.NavigteToBack = NavigteToBack;
      self.model.removeRowData = removeRowData;
      self.model.onChange = onChange
      self.model.confirmClear = confirmClear;
      self.model.itemRemovePopup = itemRemovePopup;
      self.model.SetOldValueFromStore = SetOldValueFromStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmFromStorePopup = ConfirmFromStorePopup;
      self.model.SetOldValueToStore = SetOldValueToStore;
      self.model.ChangeToStore = ChangeToStore;
      self.model.ConfirmToStorePopup = ConfirmToStorePopup;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.RemoveItem = RemoveItem;
      self.model.SetOldValueIsConsignment = SetOldValueIsConsignment;
      self.model.ChangeIsConsignment = ChangeIsConsignment;
      self.model.ConfirmIsConsignmentPopup = ConfirmIsConsignmentPopup;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.ChangeValueForUpdate = ChangeValueForUpdate;
      self.model.OpenPopup = OpenPopup;
      self.model.ConfirmedUOM = ConfirmedUOM;
      self.model.ClosePopup = ClosePopup;
      self.model.CalculateStock = CalculateStock;
      PopulateDropdown();
    }

    var ChangeValueForUpdate = function (item, selectedUom, index) {
      if (!!item.id) {
        self.model.newUom = JSON.parse(JSON.stringify(item.selectedUom));
        item.selectedUom = self.model.oldUOM;
        self.model.uomIndex = item;
        OpenPopup('Open', 'confirmForUOM');
      } else {
        self.model.CalculateStock(item, item.uom, item.selectedUom);
      }
    }

    var OpenPopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('show');
    }

    var ClosePopup = function (action, modal) {
      self.model.action = action;
      var popup = angular.element('#' + modal);
      popup.modal('hide');
    }

    var ConfirmedUOM = function () {
      self.model.uomIndex.selectedUom = self.model.newUom;
      self.model.CalculateStock(self.model.uomIndex, self.model.uomIndex.uom, self.model.uomIndex.selectedUom);
      ClosePopup('close', 'confirmForUOM');
    }

    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList.uomTypeId) {
        var stockAvailable = item.stockAvailable;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        
        item.uomUnit = uomList.uomUnit;
        item.uomUnitId = uomList.uomUnitId;
        item.uomType = uomList.uomType;
        item.uomTypeId = uomList.uomTypeId;

        if(item.quantity == undefined || item.quantity == null){
          item.quantity = 0;
        }

        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];

        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newStockAvailableQuantity = parseInt(stockAvailable / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newCurrenMonthSale = parseInt(item.currentMonthSale / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newLastMonthSale = parseInt(item.lastMonthSale / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newStockInTransit = parseInt(item.stockInTransit / (item.uomListdata[0].conversion * item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.quantity +"*"+ item.uomListdata[0].conversion);
            item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newStockAvailableQuantity = parseInt(stockAvailable / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newCurrenMonthSale = parseInt(item.currentMonthSale / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newLastMonthSale = parseInt(item.lastMonthSale / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
            item.newStockInTransit = parseInt(item.stockInTransit / (item.uomListdata[1].conversion * item.uomListdata[2].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newStockAvailableQuantity = parseInt(stockAvailable / (item.uomListdata[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion));
            item.newCurrenMonthSale = parseInt(item.currentMonthSale / (item.uomListdata[2].conversion));
            item.newLastMonthSale = parseInt(item.lastMonthSale / (item.uomListdata[2].conversion));
            item.newStockInTransit = parseInt(item.stockInTransit / (item.uomListdata[2].conversion));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = item.uomListdata[2].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newStockAvailableQuantity = parseInt(stockAvailable / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newCurrenMonthSale = parseInt(item.currentMonthSale / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newLastMonthSale = parseInt(item.lastMonthSale / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.newStockInTransit = parseInt(item.stockInTransit / (uom[0].conversion * uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon = (item.quantity +"*"+ uom[0].conversion+"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[0].conversion * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newStockAvailableQuantity = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion));
            item.newCurrenMonthSale = parseInt(item.currentMonthSale / (uom[1].conversion * uom[2].conversion));
            item.newLastMonthSale = parseInt(item.lastMonthSale / (uom[1].conversion * uom[2].conversion));
            item.newStockInTransit = parseInt(item.stockInTransit / (uom[1].conversion * uom[2].conversion));
            item.receivedQtyCon=(item.quantity +"*"+ uom[1].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[1].conversion);
            item.uomUnitData = uom[2].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.newStockAvailableQuantity = parseInt(stockAvailable / (uom[2].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion));
            item.newCurrenMonthSale = parseInt(item.currentMonthSale / (uom[2].conversion));
            item.newLastMonthSale = parseInt(item.lastMonthSale / (uom[2].conversion));
            item.newStockInTransit = parseInt(item.stockInTransit / (uom[2].conversion));
            item.receivedQtyCon=(item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = uom[2].uomUnit;
          }
        }
      } else {
        item.newStockAvailableQuantity = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
        item.newCurrenMonthSale = 0;
        item.newLastMonthSale = 0;
        item.newStockInTransit = 0;
      }
    }

    var IsMainStore = function (isOn) {
      var indent = self.model;

      if (isOn) {
        indent.toStores = indent.defaultStores.concat($filter('filter')(indent.allStores, {
          isMainStore: true
        }, true));
      } else {
        indent.toStores = indent.defaultStores.concat(indent.allStores);
      }
    }

    function setStatus(x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var PopulateDropdown = function () {
      var indent = self.model;
      self.model.indentType = [];

      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;
      indent.fromStores = indent.defaultStores;
      indent.toStores = indent.defaultStores;
      indent.status = indent.statuses[0];
      indent.toStore = indent.toStores[0];

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
        	 $rootScope.stopLoader();
          indent.fromStores = indent.defaultStores.concat(response.data.fromStore);
          indent.allStores = response.data.toStore;
          indent.toStores = indent.defaultStores.concat(response.data.toStore);
          indent.statuses = indent.statuses.concat(response.data.status);
          indent.priorities = response.data.priority;
          //indent.priority = indent.priorities[1];
          indent.priority = indent.priorities ? indent.priorities[1] : undefined ;
          self.model.indentType = response.data.indentType;
          for(var i=0;i<indent.fromStores.length;i++)
          {
        	  if(indent.fromStores[1].id == undefined)
              {
                growl.error("Please mapped store ");  
              }
              else
              {
                   var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
                   var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
                   if(indent.fromStores[i].id==selectedStoreId)
                     {
                       indent.fromStore = indent.fromStores[i];
                     }
              }
          }

          $rootScope.stopLoader();
          var indentId = commonDetailService.getDataId();
          $rootScope.stopLoader();
          if (!!indentId) {
            commonDetailService.setDataId(null);
            GetStoreIndent(indentId);
          } else {
            return false;
          }
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    function GetItemList() {
      if (self.model.fromStore.id == self.model.toStore.id) {
        growl.error("Please Select Different Stores");
      }
      else {
        self.model.action = 'Open';
        var popup = angular.element('#addNewItemWasteStore');
        popup.modal('show');
        self.model.searchNewItemList = [];
        self.model.pagination.data=[];
        self.model.isItemChecked = false;
        self.model.selectAllRows = false;
        self.model.SelectedItemsIds = [];
        self.model.search = {};
        GetAssetCategoryList();
      }

    }
    var GetAssetCategoryList = function () {
      var indent = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

      indent.assetCategory = indent.assetCategories[0];
      indent.productCategory = indent.productCategories[0];
      indent.assetCategories.splice(1);
      indent.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indent.assetCategories = indent.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetProductCategoryList = function (id) {
      var indent = self.model;

      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;

      self.model.productCategory = self.model.productCategories[0];
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indent.productCategories = indent.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    function searchNewItem() {
      if(self.model.search.itemName){
        if(self.model.search.itemName.length >= 3){
          var URI = CONSTANTS.GLOBAL.STORE_INDENT_ITEM_API;

          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
            "isConsignment": self.model.isConsignment ? self.model.isConsignment.id : undefined,
            "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId
          }
          // self.model.searchNewItemList = [];
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
            growl.error('Error');
          });
        }
      }
      else if(self.model.search.itemCode){
        if(self.model.search.itemCode.length >= 3){
           var URI = CONSTANTS.GLOBAL.STORE_INDENT_ITEM_API;

          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
            "isConsignment": self.model.isConsignment ? self.model.isConsignment.id : undefined,
            "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId
          }
          // self.model.searchNewItemList = [];
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
            growl.error('Error');
          });
        }
      }
      else{
        self.model.searchNewItemList = [];
        self.model.pagination.data = [];
      }
    }
    var SelectAllItems = function () {
      console.log("Whole Data : ",self.model.searchNewItemList);
      console.log("Pagination Data : ",self.model.pagination.data);
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

    var ValidateQuantity = function (item) {
      var isValid = true;

      CalculateStock(item, item.uom, item.selectedUom);

      if (isNaN(parseFloat(item.quantity))) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error("Please Enter Quantity");
      } else if (parseInt(item.quantity) == 0) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Quantity Cannot Be Zero');
      }
      else if (parseInt(item.quantity) > item.newMaxOrderQuantity) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Entered quantity is exceeding the max order quantity');
      } else {
        item.quantity = parseInt(item.quantity);
        isValid = true;
      }

      return isValid;
    }

    var AddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.fromStore.id
      };
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
           response.data.forEach(function (item) {
              var isFound = self.model.ItemDetailList.find(function (x) {
                return x.itemId == item.itemId;
              });
              if (!isFound) {
                self.model.ItemDetailList.push(item);
              } else {
                growl.error('Item Present In the List')
              }

            });
          self.model.SelectedItemsIds = [];
          self.model.items = [];
          var selectedUom;

          angular.forEach(self.model.ItemDetailList, function (x) {
            if (x.uom) {
              selectedUom = x.uom.find(function (sUom) {
                return sUom.storeUom == true;
              });
              if(selectedUom) {
                x["uomType"] = selectedUom.uomType;
                x["uomUnit"] = selectedUom.uomUnit;
                x["uomTypeId"] = selectedUom.uomTypeId;
                x["uomUnitId"] = selectedUom.uomUnitId; 
              }
            }
            else {
              x["uomType"] = x.uomType;
              x["uomUnit"] = x.uomUnit;
              x["uomTypeId"] = x.uomTypeId;
              x["uomUnitId"] = x.uomUnitId; 
            }
            x.selectedUom = x.uom.find(function (uom) {
              return x.uomTypeId == uom.uomTypeId;
            });
            if(x.isThreeUOMApplicable == false){
              if(x.uom.length == 3){
                x.uomListdata = x.uom;
                x.uom = [];
                angular.forEach(x.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    x.uom.push(data);
                  }
                });
              }
            }
            var conversionPurchase;
            var conversionStock;
            var conversionDespense;
            self.model.itemArray = [];
              self.model.ItemDetailList.forEach(function (item) {
                if(item.isThreeUOMApplicable == false){
                  if(item.uomListdata)
                  {
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
                  }
                }
                else{
                  if(item.uom)
                  {
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
                  }
                }

                if(item.quantity == undefined || item.quantity == null){
                  item.quantity = 0;
                }

                if (item.uomTypeId == 1 && (item.uom != undefined ||  item.uom != null)) {
                  item.newMaxOrderQuantity = Math.floor((((item.maxOrderQuantity / conversionPurchase) / conversionStock) / conversionDespense));
                  item.newStockAvailableQuantity = Math.floor((((item.stockAvailable / conversionPurchase) / conversionStock) / conversionDespense));
                  item.newCurrenMonthSale = Math.floor((((item.currentMonthSale / conversionPurchase) / conversionStock) / conversionDespense));
                  item.newLastMonthSale = Math.floor((((item.lastMonthSale / conversionPurchase) / conversionStock) / conversionDespense));
                  item.newStockInTransit = Math.floor((((item.stockInTransit / conversionPurchase) / conversionStock) / conversionDespense));
                  if(item.isThreeUOMApplicable == false){
                    item.receivedQtyCon=(item.quantity +"*"+ conversionPurchase);
                    item.receivedQtyCalculation = (item.quantity * conversionPurchase);
                    if(item.uom){
                      if(item.uom[2] != undefined){
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                      else{
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                      }
                    }
                    else{
                      item.uomUnitData = item.uomListdata[2].uomUnit;
                    }
                  }
                  else{
                    item.receivedQtyCon = (item.quantity +"*"+ conversionPurchase +"*"+ conversionStock);
                    item.receivedQtyCalculation = (item.quantity * conversionPurchase * conversionStock);
                    if(item.uom){
                      if(item.uom[2] != undefined){
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                      else{
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                      }
                    }
                    else{
                      item.uomUnitData = item.uomListdata[2].uomUnit;
                    }
                  }
                }
                else if (item.uomTypeId == 2 && (item.uom != undefined ||  item.uom != null)) {
                  item.newStockAvailableQuantity = Math.floor((item.stockAvailable / conversionStock) / conversionDespense);
                  item.newMaxOrderQuantity = Math.floor((item.maxOrderQuantity / conversionStock) / conversionDespense);
                  item.newCurrenMonthSale = Math.floor((item.currentMonthSale / conversionStock) / conversionDespense);
                  item.newLastMonthSale = Math.floor((item.lastMonthSale / conversionStock) / conversionDespense);
                  item.newStockInTransit = Math.floor((item.stockInTransit / conversionStock) / conversionDespense);
                  if(item.isThreeUOMApplicable != false){
                    item.receivedQtyCon=(item.quantity +"*"+ conversionStock);
                    item.receivedQtyCalculation = (item.quantity * conversionStock);
                    if(item.uom){
                      if(item.uom[2] != undefined){
                        item.uomUnitData = item.uom[2].uomUnit;
                      }
                      else{
                        item.uomUnitData = item.uomListdata[2].uomUnit;
                      }
                    }
                    else{
                      item.uomUnitData = item.uomListdata[2].uomUnit;
                    }
                  }
                }
                else {
                  item.newStockAvailableQuantity = Math.floor(item.stockAvailable);
                  item.newMaxOrderQuantity = Math.floor(item.maxOrderQuantity);
                  item.newCurrenMonthSale = Math.floor(item.currentMonthSale);
                  item.newLastMonthSale = Math.floor(item.lastMonthSale);
                  item.newStockInTransit = Math.floor(item.stockInTransit);
                  item.receivedQtyCon=(item.quantity);
                  item.receivedQtyCalculation = (item.quantity);
                  if(item.uom){
                    if(item.uom[2] != undefined){
                      item.uomUnitData = item.uom[2].uomUnit;
                    }
                    else{
                      item.uomUnitData = item.uomListdata[2].uomUnit;
                    }
                  }
                  else{
                    item.uomUnitData = item.uomListdata[2].uomUnit;
                  }
                }
              });

          });

          $rootScope.stopLoader();
          self.model.searchNewItemList = [];
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var isSelected = function (item) {
      return item.isSelected;
    }
    $scope.saveBtnDisabled=false;
    var SaveStoreIndent = function () {
        $scope.saveBtnDisabled=true;
      if (validateSave()) {
        if (!self.model.indentId) {
          saveNewStoreIndentRequest();
        } else {
          UpdateStoreIndent();
        }
      }
    }
    var saveNewStoreIndentRequest = function () {
      var store = self.model;

      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.SAVE_API;

      var data = {
        "fromStoreId": store.fromStore.id,
        "toStoreId": store.toStore.id,
        "priorityId": store.priority.id,
        "isConsignment": store.isConsignment ? store.isConsignment : false,
        //"indentTypeId": self.model.indentType[0].id,
        "indentTypeId": 1,
        "remark": store.remark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      if( self.model.indentType!=undefined &&  self.model.indentType!=null &&  self.model.indentType.length>0){
    	  angular.forEach(store.ItemDetailList, function (value,index) {
    		  if(value.type == "Manual"){
    			  data.indentTypeId = value.id;
    		  }
    	  });
      }
      angular.forEach(store.ItemDetailList, function (item, key) {
        var storeItem = {
          "itemId": item.itemId,
          "uomTypeId": item.uomTypeId,
          "uomUnitId": item.uomUnitId,
          "quantity": parseInt(item.quantity),
          "statusId": setStatus('New')
        }
        data.items.push(storeItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigteToBack();
          growl.success(response.data.message);
        },

        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }

    var UpdateStoreIndent = function () {
      var indent = self.model;
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.UPDATE_API + indent.indentId;
      var data = {
        "toStoreId": indent.toStore.id,
        "priorityId": indent.priority.id,
        "isConsignment": indent.isConsignment,
        "remark": indent.remark,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(indent.ItemDetailList, function (item, key) {

        var indentItem = {
          "id": item.id,
          "itemId": item.itemId,
          "uomTypeId": item.uomTypeId,
          "uomUnitId": item.uomUnitId,
          "quantity": parseInt(item.quantity),
        }
        if (item.id == '' || item.id == null) {
          indentItem.statusId = setStatus('New')
        }

        data.items.push(indentItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {

          $rootScope.stopLoader();
          NavigteToBack();
          growl.success(response.data.message);
        },

        function (err) {
          $rootScope.stopLoader();
          if(!!err && !!err.data && !!err.data.summary){
            growl.error(err.data.summary);
          } else{
            growl.error('Something Went Wrong');
          }
          
        });
    }


    var NavigteToBack = function () {
      $state.go('storeIndenting');
    }

    function validateSave() {
      var isValid = true;
      var ItemDetailList = self.model.ItemDetailList;
      // !self.model.fromStore.id || !self.model.toStore.id || !self.model.priority.id || 

      if (ItemDetailList.length < 1) {
        growl.error("Please Add Items To Save");
        isValid = false;
      }
      if (!self.model.toStore.id) {
        growl.warning("Please Select Issuing Stores");
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
      }
      if(!isValid)
      {
      $scope.saveBtnDisabled=false;
      }
      return isValid;
    }
    var onChange = function () {
      if (self.model.ItemDetailList.length > 0) {
        $("#storeChange").modal()
      }
    }

    var itemRemovePopup = function (item) {
      self.model.itemRemove = '';
      self.model.itemRemove = item;
      $("#itemRemovePopup").modal()

    }

    var confirmClear = function () {
      self.model.ItemDetailList = [];

    }
    
    var GetStoreIndent = function (indentId) {

      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DETAILS_API + $scope.unitId + '/'+$scope.organizationId + '/' + indentId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.indentId = response.data.id;

          self.model.fromStore = self.model.fromStores.find(function (item) {
            return response.data.fromStoreId == item.id;
          });

          self.model.toStore = self.model.toStores.find(function (item) {
            return response.data.toStoreId == item.id;
          });

          self.model.priority = self.model.priorities.find(function (item) {
            return response.data.priorityId == item.id;
          });

          self.model.isConsignment = response.data.isConsignment;

          self.model.remark = response.data.remark;

          self.model.itemDetails = [];


          self.model.ItemDetailList = response.data.items;

          self.model.ItemDetailList.forEach(function(item){
            item.newMaxOrderQuantity = item.maxOrderQuantity;
            item.newCurrenMonthSale  =item.currentMonthSale;
            item.newLastMonthSale = item.lastMonthSale;
            item.newStockAvailableQuantity = item.stockAvailable;
            item.newStockInTransit = item.stockInTransit; 
            item.selectedUom = item.uom.find(function (uom) {
              return item.uomTypeId == uom.uomTypeId;
            });     
            if(item.isThreeUOMApplicable == false){
              item.uomListdata = item.uom;
              item.uom = [];
              angular.forEach(item.uomListdata, function(data){
                if(data.uomType != 'Stock'){
                  item.uom.push(data);
                }
              });
            }       

            var uomdata = item.selectedUom.uomDetails.split(' ')[0];
            if(item.isThreeUOMApplicable == false){
              if(uomdata == 'P'){
                item.receivedQtyCon=(item.quantity +"*"+ item.uomListdata[0].conversion);
                item.receivedQtyCalculation = (item.quantity * item.uomListdata[0].conversion);
                item.uomUnitData = item.uomListdata[2].uomUnit;
              }
              else if(uomdata == 'S'){
                
              }
              else{
                item.receivedQtyCon=(item.quantity);
                item.receivedQtyCalculation = (item.quantity);
                item.uomUnitData = item.uomListdata[2].uomUnit;
              }
            } 
            else{
              if(uomdata == 'P'){
                item.receivedQtyCon = (item.quantity +"*"+ item.uom[0].conversion+"*"+ item.uom[1].conversion);
                item.receivedQtyCalculation = (item.quantity * item.uom[0].conversion * item.uom[1].conversion);
                item.uomUnitData = item.uom[2].uomUnit;
              }
              else if(uomdata == 'S'){
                item.receivedQtyCon=(item.quantity +"*"+ item.uom[1].conversion);
                item.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                item.uomUnitData = item.uom[2].uomUnit;
              }
              else{
                item.receivedQtyCon=(item.quantity);
                item.receivedQtyCalculation = (item.quantity);
                item.uomUnitData = item.uom[2].uomUnit;
              }
            }
          });
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    function removeRowData() {
      var index = self.model.ItemDetailList.indexOf(self.model.itemRemove);
      self.model.ItemDetailList.splice(index, 1);
    }

    var SetOldValueFromStore = function (fromStore) {
      self.model.oldFromStore = fromStore;
      self.model.fromStore = self.model.oldFromStore;
    }

    var ChangeFromStore = function (fromStore) {
      if (!!self.model.ItemDetailList.length) {
        self.model.fromStore = self.model.oldFromStore;
        self.model.fromStoreNew = JSON.parse(JSON.stringify(fromStore));
        $("#storeChange").modal()
      }
    }

    var ConfirmFromStorePopup = function () {
      self.model.fromStore = self.model.fromStoreNew;
      self.model.ItemDetailList = [];
      ModalDismiss('storeChange');
    }

    var SetOldValueToStore = function (toStore) {
      self.model.oldToStore = toStore;
      self.model.toStore = self.model.oldToStore;
    }

    var ChangeToStore = function (toStore, oldToStore) {
      if (!!oldToStore.id && !!self.model.ItemDetailList.length) {
        self.model.toStore = oldToStore;
        self.model.toStoreNew = JSON.parse(JSON.stringify(toStore));
        $("#tostoreChange").modal()
      }
    }

    var ConfirmToStorePopup = function () {
      self.model.toStore = self.model.toStoreNew;
      self.model.ItemDetailList = [];
      ModalDismiss('tostoreChange');
    }


    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var ConfirmRemove = function (item) {
      var popup = angular.element('#itemRemovePopup');
      popup.modal('show');
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var newArray = [];

      self.model.ItemDetailList.map(function (obj) {
        if (obj !== item) {
          newArray.push(obj);
        }
      });
      self.model.ItemDetailList = newArray;
      var popup = angular.element('#itemRemovePopup');
      popup.modal('hide');
    }


    var SetOldValueIsConsignment = function (isConsignment) {
      self.model.oldIsConsignment = isConsignment;
      self.model.isConsignment = self.model.oldIsConsignment;
    }

    var ChangeIsConsignment = function (isConsignment) {
      if (!!self.model.ItemDetailList.length) {
        self.model.isConsignment = self.model.oldIsConsignment;
        self.model.isConsignmentNew = JSON.parse(JSON.stringify(isConsignment));
        $("#IsConsignment").modal()
      }
    }

    var ConfirmIsConsignmentPopup = function () {
      self.model.isConsignment = self.model.isConsignmentNew;
      self.model.ItemDetailList = [];
      ModalDismiss('IsConsignment');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewStoreIndenting", {
      url: "/addNewStoreIndenting",
      templateUrl: 'views/PharmacyInventory/InPatient/add-new-store-indenting.html',
      controller: "AddNewStoreIndenting.Controller",
      controllerAs: "vm",
      title: 'Store Indent'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewStoreIndenting.Controller", addNewStoreIndentingController);
})();