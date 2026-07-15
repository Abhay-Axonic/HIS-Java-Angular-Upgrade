(function () {
  "use strict";

  function addNewPurchaseRequestController($scope, $rootScope, $http, $state, GenericService,$cookies, PaginationService, PurchaseRequestService, StatusService, growl, CONSTANTS,$filter) {
    var self = this;

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
          searchDataByField: PaginationService.searchDataByField,
          setTotalPages: PaginationService.setTotalPages,
          getPagesList: PaginationService.getPagesList,
          setPage: PaginationService.setPage,
          setFirstPage: PaginationService.setFirstPage,
          setPreviousPage: PaginationService.setPreviousPage,
          setNextPage: PaginationService.setNextPage,
          setLastPage: PaginationService.setLastPage,
          sortBy: PaginationService.sortBy
        },
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
        screenId: 4,
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],
        stores: [{
          "store": "Select Store"
        }],
        fromStores: [{
          "store": "Select Store"
        }],
        toStores: [{
          "store": "Select Store"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        purchaseId: null,
        indentItemDetails: [],
        isUOMValid: [],
        search: {
          itemName: undefined,
          itemCode: undefined
        },
        uomArray: [{
          "uomType": "Select UOM"
        }],
        // changed
        prRequestTypeId: 1,
        reqAssetTypeId: 1,
        PRType: 1,
        assetType: 1,
        POUoms: []
      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SavePurchaseRequest = SavePurchaseRequest;
      self.model.NavigateToPurchaseRequest = NavigateToPurchaseRequest;
      self.model.ChangePRType = ChangePRType;
      self.model.GetIndentList = GetIndentList;
      self.model.Expand = Expand;
      self.model.SelectAllIndentItems = SelectAllIndentItems;
      self.model.SelectIndentItem = SelectIndentItem;
      self.model.AddIndentItems = AddIndentItems;
      self.model.CalculateStock = CalculateStock;
      self.model.CalculateStock1 = CalculateStock1;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.EmptyList = EmptyList;
      self.model.cancelRequestPopup = cancelRequestPopup;
      self.model.EmptyAssetList = EmptyAssetList;
      self.model.cancelAssetPopup = cancelAssetPopup;
      self.model.ConfirmValidation = ConfirmValidation;
      self.model.ValidatePRQuantity = ValidatePRQuantity;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ChangeValueForUpdate = ChangeValueForUpdate;
      self.model.ConfirmedUOM = ConfirmedUOM;
      self.model.SetOldValueForUpdate = SetOldValueForUpdate;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.ViewPOUom = ViewPOUom;
      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.setOldFromStore=setOldFromStore;

      PopulateDropdown();
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var setStatus = function (x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var SetOldValueAssetType = function (typeId) {
      self.model.oldAssetTypeId = typeId;
      self.model.assetType = self.model.oldAssetTypeId;
    }

    var ChangeFromAssetType = function (typeId) {
      self.model.reqAssetTypeId = typeId;
      if (!!self.model.itemDetails.length) {
        OpenPopup('open', 'confirmFromAssetType');
      } else {
        self.model.assetType = typeId;
      }
    }

    var EmptyAssetList = function () {
      self.model.assetType = self.model.reqAssetTypeId;
      self.model.itemDetails = [];

      ClosePopup('close', 'confirmFromAssetType');
    }

    var cancelAssetPopup = function (reqAssetTypeId){
      if(reqAssetTypeId == 2){
        self.model.reqAssetTypeId = 1;
      }
      else{
        self.model.reqAssetTypeId = 2;
      }
      var popup = angular.element('#confirmFromAssetType');
      popup.modal('hide');
    }

    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }
    
    var setOldFromStore=function (store) {
        self.model.oldFromStore = angular.copy(store);
      }

    var ChangeFromStore = function (store) {
      if (!!self.model.itemDetails.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        OpenPopup('Open', 'confirmStore');
      }
      else
      {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        self.model.store = self.model.storeNew;
      }
    }

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      ClosePopup('close', 'confirmStore');
      self.model.itemDetails = [];
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

    var SetOldValueForUpdate = function (item, detailId) {
      self.model.oldUOM = item.selectedUom;
    }

    var ConfirmedUOM = function () {
      self.model.uomIndex.selectedUom = self.model.newUom;
      self.model.CalculateStock(self.model.uomIndex, self.model.uomIndex.uom, self.model.uomIndex.selectedUom);
      ClosePopup('close', 'confirmForUOM');
    }

    var ConfirmValidation = function (prType) {
      self.model.prRequestTypeId = prType;
      if (!!self.model.itemDetails.length) {
        OpenPopup('open', 'confirmValidation');
      } else {
        self.model.PRType = prType;
      }
    }

    var EmptyList = function () {
      self.model.PRType = self.model.prRequestTypeId;
      self.model.itemDetails = [];

      ClosePopup('close', 'confirmValidation');
    }

    var cancelRequestPopup = function (prRequestTypeId) {
      if(prRequestTypeId == 2){
        self.model.prRequestTypeId = 1;
      }
      else{
        self.model.prRequestTypeId = 2;
      }

      var popup = angular.element('#confirmValidation');
      popup.modal('hide');
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

    var PopulateDropdown = function () {
      var cookieObject = $cookies.getObject('cookieObject');
      if(cookieObject == undefined){
        $state.go('login');
        return;
      }
      $scope.storeId = cookieObject.storeId;
      try {
        $rootScope.startLoader();
        var purchaseRequests = self.model;
        purchaseRequests.store = purchaseRequests.stores[0];
        purchaseRequests.fromStore = purchaseRequests.fromStores[0];

        var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.DROPDOWN_API + '/' + $scope.unitId+ '/'+$scope.userId;


        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            purchaseRequests.stores = purchaseRequests.stores.concat(response.data.requestingStores);
            purchaseRequests.fromStores = purchaseRequests.fromStores.concat(response.data.deliveryStores);
            purchaseRequests.priorities = response.data.priority;
            purchaseRequests.priority = response.data.priority[1];
            purchaseRequests.fromStore = purchaseRequests.fromStores.find(function (store) {
              return $scope.storeId == store.id;
          });
            var purchaseId = PurchaseRequestService.GetPurchaseId();
            $rootScope.stopLoader();
            if (!!purchaseId) {
              PurchaseRequestService.SetPurchaseId(null);
              GetPurchaseRequest(purchaseId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var ChangePRType = function (prType) {
      if (ValidateDetails()) {
        if (prType == 1) {
          self.model.action = 'Open';
          var popup = angular.element('#addItemListPop');
          popup.modal('show');
          PopulateAssetCategoryDropdown();
          self.model.items = [];
          self.model.pagination.data = [];
          self.model.selectAllRows = false;
          self.model.SelectedItemsIds = [];
          self.model.search.itemCode = undefined;
          self.model.search.itemName = undefined;
        } else if (prType == 2) {
          self.model.action = 'Open';
          var popup = angular.element('#getRequestWasteStore');
          popup.modal('show');
          PopulateIndentDropdown();
          self.model.indents = [];
          self.model.searchIndent = undefined;
        }
      }
    }

    var GetPurchaseRequest = function (purchaseId) {
      var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.SAVE_API + $scope.unitId + '/'+$scope.organizationId + '/'  + purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.purchaseId = response.data.id;
          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
          });

          self.model.fromStore = self.model.fromStores.find(function (item) {
            return response.data.requestingStoreId == item.id;
          });

          self.model.priority = self.model.priorities.find(function (item) {
            return response.data.priorityId == item.id;
          });

          self.model.reqAssetTypeId = response.data.assetTypeId;
          self.model.prRequestTypeId = response.data.prTypeId;
          self.model.assetType = response.data.assetTypeId;
          self.model.PRType = response.data.prTypeId;
          self.model.remark = response.data.remark;
          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {
            var indentItem = {
              "id": item.id,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "uom": item.uom,
              "PRQuantity": item.quantity,
              "pendingQuantity": item.pendingQuantity,
              "stockAvailable": item.availableQuantity,
              "itemSpecifications": item.specification,
              "indentQuantity": item.quantity,
              "maxStoreQuantity": item.maxStoreQuantity,
              "maxOrderQuantity": item.maxOrderQuantity,
              "prMaxStoreQty": item.quantity,
              "isThreeUOMApplicable": item.isThreeUOMApplicable
            }
            self.model.itemDetails.push(indentItem);
            if (response.data.assetTypeId == 1) {
              indentItem.selectedUom = item.uom.find(function (uom) {
                return item.uomTypeId == uom.uomTypeId;
              });

              indentItem.uom = self.model.uomArray.concat(item.uom);
              item.uom = self.model.uomArray.concat(item.uom);
              item.uom[0].uomDetails = item.uom[0].uomType; 
              if(item.isThreeUOMApplicable == false){
                indentItem.uomListdata = item.uom;
                indentItem.uom = [];
                angular.forEach(indentItem.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    indentItem.uom.push(data);
                  }
                });
              }
              if(indentItem.selectedUom){
                if(indentItem.selectedUom.uomType=="Despense")
                {
                  indentItem.prQuantity = indentItem.indentQuantity;
                }
                else if(indentItem.selectedUom.uomType=="Stock")
                {
                  indentItem.prQuantity = indentItem.indentQuantity * (indentItem.uom[2].conversion);
                }
                else if(indentItem.selectedUom.uomType=="Purchase")
                {
                  indentItem.prQuantity = indentItem.indentQuantity * (indentItem.uom[2].conversion* indentItem.uom[1].conversion);
                }
              }
              CalculateStock1(indentItem, item.uom, indentItem.selectedUom);
            } else {
              indentItem.newMaxOrderQuantity = item.maxOrderQuantity;
              indentItem.newMaxStoreQuantity = item.maxStoreQuantity;
              indentItem.newQty = item.availableQuantity;
            }
          });

          $rootScope.stopLoader();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
    }

    var ValidateDetails = function () {
      var isValid = true;
      if (!self.model.store.id) {
        isValid = false;
        self.model.isStoreValid = true;
      }

      if (!self.model.assetType) {
        isValid = false;
        self.model.isAssetTypeValid = true;
      }

      if (!self.model.PRType) {
        isValid = false;
        self.model.isPRTypeValid = true;
      }

      if (!self.model.store.id || !self.model.assetType || !self.model.PRType) {
        isValid = false;
        growl.error('Please select required field');
      }

      return isValid;
    }

    var ValidatePRQuantity = function (item) {
      var isValidItem = true;
      var prQty = parseInt(item.indentQuantity);
      item.prQuantity=prQty;
      
      if(item.selectedUom){
        if(item.selectedUom.uomType=="Despense")
        {
          item.prQuantity = item.prQuantity;
        }
        else if(item.selectedUom.uomType=="Stock")
        {
          item.prQuantity = item.prQuantity*(item.uom[2].conversion);
        }
        else if(item.selectedUom.uomType=="Purchase")
        {
          item.prQuantity = item.prQuantity*(item.uom[2].conversion* item.uom[1].conversion);
        }
      }
      
      var newMaxOrderQuantity = item.newMaxOrderQuantity + item.prMaxStoreQty;
      if (newMaxOrderQuantity > item.newMaxStoreQuantity) {
        newMaxOrderQuantity = item.newMaxStoreQuantity;
      }

      if(self.model.reqAssetTypeId != 2){
        CalculateStock(item, item.uom, item.selectedUom);
      }
      
      if (prQty > newMaxOrderQuantity) {
        growl.error('PR quantity should be less than pending quantity');
        item.isPRQuantityValid = true;
        isValidItem = false;
      }

      if (!prQty) {
        growl.error('Please enter valid PR quantity');
        item.isPRQuantityValid = true;
        isValidItem = false;
      }

      return item, isValidItem;
    }

    var ValidateItems = function () {
      var isValidItem = true;
      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add at list one item');
        isValidItem = false;
      } else {
        for (var i = 0; i < self.model.itemDetails.length; i++) {
          self.model.itemDetails[i], isValidItem = ValidatePRQuantity(self.model.itemDetails[i]);

          if (self.model.assetType == 1) {
            if (!self.model.itemDetails[i].selectedUom.uomUnitId) {
              growl.error('Please select UOM');
              self.model.isUOMValid[i] = true;
              isValidItem = false;
            }
          }
        }
      }

      return isValidItem;
    }

    var PopulateAssetCategoryDropdown = function () {
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetType+ '/'+$scope.unitId+ '/'+$scope.organizationId;

      self.model.assetCategory = self.model.assetCategories[0];
      self.model.productCategory = self.model.productCategories[0];

      self.model.assetCategories.splice(1);
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.assetCategories = self.model.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateProductCategory = function (assetCategory) {
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + self.model.assetCategory.id + '/'+$scope.organizationId;

      self.model.productCategory = self.model.productCategories[0];
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.productCategories = self.model.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateIndentDropdown = function () {
      self.model.indents = [];
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

      //self.model.fromStore = self.model.fromStores[0];
      //self.model.toStore = self.model.toStores[0];

      //self.model.fromStores.splice(1);
      //self.model.toStores.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          //self.model.fromStores = self.model.fromStores.concat(response.data.fromStore);
          //self.model.toStores = self.model.toStores.concat(response.data.toStore);
          /*for(var i=0;i<self.model.fromStores.length;i++)
          {
            if(self.model.fromStores[i].id==$cookies.getObject('selectedTopStore').storeId)
            {
              self.model.fromStore = self.model.fromStores[i];
            }
          }*/
          $rootScope.stopLoader();
        }, function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetIndentList = function () {
      var fromStore = self.model.fromStore.id;
      var toStore = self.model.store.id;

      if (!fromStore || !toStore) {
        self.model.indents = [];
        growl.error('Please select valid store');
        return false;
      }
      // start code for resolved HIS-9596 If PR is against indent then requesting store and delivery store can be same
      /*else if (fromStore == toStore) {
        self.model.indents = [];
        growl.error('From store and to store should not be same');
        return false;
      } */
      // end code for resolved HIS-9596 If PR is against indent then requesting store and delivery store can be same
      else {
        var URI = CONSTANTS.GLOBAL.GET_INDENTS_API  + $scope.unitId + '/' + fromStore + '/' + toStore;
        var data = {
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "assetTypeId":self.model.assetType
        }
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.indents = response.data;
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
      }
    }

    var Expand = function (item) {
      item.Expanded = !item.Expanded;
    }

    var GetItemList = function () {
      if(self.model.search.itemName){

        if ( self.model.search.itemName!=undefined  &&  (self.model.search.itemName.replace(/\s/g, "").length) == 2  ) {


          var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;

          var data = {
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "requestingStoreId": self.model.fromStore.id,
            "assetTypeId": self.model.assetType,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName
          }

          self.model.items = [];
          self.model.pagination.data = [];
          self.model.pagination.totalPages = 0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.selectAllRows = false;
            self.model.items = response.data;
            if (!!response && !!response.data && response.data.length > 0) {
              PaginationService.searchDataByField(self.model.items, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }else{

        if( self.model.search.itemName !=undefined &&  (self.model.search.itemName.replace(/\s/g, "").length) > 0){
                                var list = $filter('filter')(self.model.items, {
                                                                 $: self.model.search.itemName
                                                               });
                                PaginationService.searchDataByField( list, self.model.pagination);
                            }

        }
      }
      else if(self.model.search.itemCode){
        if ( self.model.search.itemCode!=undefined  &&  (self.model.search.itemCode.replace(/\s/g, "").length) == 2  ) {

          var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;

          var data = {
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "requestingStoreId": self.model.fromStore.id,
            "assetTypeId": self.model.assetType,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName
          }

          self.model.items = [];
          self.model.pagination.data = [];
          self.model.pagination.totalPages = 0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.selectAllRows = false;
            self.model.items = response.data;
            if (!!response && !!response.data && response.data.length > 0) {
              PaginationService.searchDataByField(self.model.items, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }else{

         if( self.model.search.itemCode !=undefined &&  ( self.model.search.itemCode.replace(/\s/g, "").length) > 0){
                                        var list = $filter('filter')(self.model.items, {
                                                                         $: self.model.search.itemCode
                                                                       });
                                        PaginationService.searchDataByField( list, self.model.pagination);
                                    }

        }
      }
      else{
        self.model.items = [];
        self.model.pagination.data = [];
      }
    }

    var AddItems = function () {
      var popup = angular.element('#addItemListPop');
      popup.modal('hide');
      if (self.model.SelectedItemsIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      } else {
      //   if(self.model.search.itemCode != undefined || self.model.search.itemName != undefined){
      //     self.model.SelectedItemsIds = 0;
      //     self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
      //       return item.itemId;
      //     });
      // }
      // else{
     
      // }

        var URI = CONSTANTS.GLOBAL.PURCHASE_ITEM_DETAILS_API;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id,
          "screenId": self.model.screenId,       
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId

        };

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function (item) {
              var isFound = self.model.itemDetails.find(function (x) {
                return x.itemId == item.itemId;
              });
              if (item.maxOrderQuantity <= 0) {
                growl.error("Maxorder Quantity is less than 0 for an item.");
              }
              // get items which has valid maxOrderQuantity
              if (!isFound && (item.maxOrderQuantity > 0)) {
                self.model.itemDetails.push(item);
                item.prMaxStoreQty = 0;
                item.newQty = item.stockAvailable;
                if (self.model.assetType == 1) {
                  selectUOM(item);
                } else {
                  item.newMaxOrderQuantity = item.maxOrderQuantity;
                  item.newMaxStoreQuantity = item.maxStoreQuantity;
                }
              }
            });

            self.model.SelectedItemsIds = [];            
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var selectUOM = function (item) {
      item.uom = self.model.uomArray.concat(item.uom);
      item.uom[0].uomDetails = item.uom[0].uomType; 
      item.selectedUom = item.uom[0];
      
      // start code for resolved HIS-9319 UOM should fetch from the Indent request on the purchase request against indent screen
      //as per reference from addNewStoreIndentingController.js
      if(self.model.PRType == 2){
    	  let selectedUom = item.uom.find(function (sUom) {
    		  return sUom.storeUom == true;
    		  });
    	  item.selectedUom = selectedUom;
      }
      // end code for resolved HIS-9319 UOM should fetch from the Indent request on the purchase request against indent screen
    
      if(item.isThreeUOMApplicable == false){
        item.uomListdata = item.uom;
        item.uom = [];
        angular.forEach(item.uomListdata, function(data){
          if(data.uomType != 'Stock'){
            item.uom.push(data);
          }
        });
      }
      CalculateStock(item, item.uom, item.selectedUom);
    }
    
    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList.uomTypeId) {
        if(item.prQuantity == undefined || item.prQuantity == null || isNaN(item.prQuantity)){
          item.prQuantity = 0;
        }
        if(item.indentQuantity == undefined || item.indentQuantity == null){
          item.indentQuantity = 0;
        }
        var stockAvailable = item.stockAvailable;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.indentQuantity= parseInt(item.prQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.indentQuantity +"*"+ item.uomListdata[1].conversion);
            item.receivedQtyCalculation = (item.indentQuantity * item.uomListdata[1].conversion);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.indentQuantity= parseInt(item.prQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[3].conversion));
            item.indentQuantity= parseInt(item.prQuantity / (item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.indentQuantity);
            item.receivedQtyCalculation = (item.indentQuantity);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.indentQuantity = parseInt(item.prQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon = (item.indentQuantity +"*"+ uom[1].conversion+"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.indentQuantity * uom[1].conversion * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(stockAvailable / (uom[2].conversion * uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion * uom[3].conversion));
            item.indentQuantity= parseInt(item.prQuantity / (uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon=(item.indentQuantity +"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.indentQuantity * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(stockAvailable / (uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[3].conversion));
            item.indentQuantity= parseInt(item.prQuantity / (uom[3].conversion));
            item.receivedQtyCon=(item.indentQuantity);
            item.receivedQtyCalculation = (item.indentQuantity);
            item.uomUnitData = uom[3].uomUnit;
          }
        }
      } else {
        item.newQty = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
    }

    var CalculateStock1 = function (item, uom, uomList) {
      if (!!uomList.uomTypeId) {
        var stockAvailable = item.stockAvailable;
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.indentQuantity +"*"+ item.uomListdata[1].conversion);
            item.receivedQtyCalculation = (item.indentQuantity * item.uomListdata[1].conversion);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.indentQuantity);
            item.receivedQtyCalculation = (item.indentQuantity);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon = (item.indentQuantity +"*"+ uom[1].conversion+"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.indentQuantity * uom[1].conversion * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(stockAvailable / (uom[2].conversion * uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon=(item.indentQuantity +"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.indentQuantity * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(stockAvailable / (uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[3].conversion));
            item.receivedQtyCon=(item.indentQuantity);
            item.receivedQtyCalculation = (item.indentQuantity);
            item.uomUnitData = uom[3].uomUnit;
          }
        }
      } else {
        item.newQty = 0;
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
      }
    }

    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    var SelectAllIndentItems = function (indent, items) {
      if (indent.isSelectedIndent) {
        items.map(function (x) {
          self.model.indentItemDetails.push(x);
          x.isSelectedItem = true;
          x.indentId = indent.indentId;
        });
      } else {
        items.forEach(function (x) {
          x.isSelectedItem = false;
        });
        items.map(function (x) {
          var idx = self.model.indentItemDetails.indexOf(x);
          if (idx > -1) {
            self.model.indentItemDetails.splice(idx, 1);
          }
        });
      }
    }

    var SelectIndentItem = function (item, indent) {
      if (item.isSelectedItem) {
        self.model.indentItemDetails.push(item);
      } else {
        var itemIndex = self.model.indentItemDetails.indexOf(item);
        if (itemIndex > -1) {
          self.model.indentItemDetails.splice(itemIndex, 1);
        }
      }

      if (indent.items.every(isSelectedItem)) {
        indent.isSelectedIndent = true;
      } else {
        indent.isSelectedIndent = false;
      }
    }

    var getSelectedItems = function () {
      var hash = {};
      self.model.indentItemDetails.forEach(function (item) {
        var itemId = item.itemId;
        var indentId = item.id;
        if (hash.hasOwnProperty(itemId)) {
          //hash[itemId][indentId] = { pendingQuantity: item.availableQuantity }
        	
        	/* start code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
        	hash[itemId][indentId] = { pendingQuantity: item.quantity }
        	/* end code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
        } else {
          hash[itemId] = {};
          hash[itemId][indentId] = { pendingQuantity: item.quantity }
        }
      });
      return hash;
    }

    var updateItemDetails = function (responseData, selectedItems) {
      var itemDetailsHash = {};
      self.model.itemDetails.forEach(function (itemDetail) {
        itemDetailsHash[itemDetail.itemId] = itemDetail;
      });
      
      var newItemDetails = [];

      responseData.forEach(function (item) {
        var itemId = item.itemId;
        if (selectedItems.hasOwnProperty(itemId)) {
          var selectedItem = selectedItems[itemId];
          if (itemDetailsHash.hasOwnProperty(itemId)) {
            //found in old list
            var itemDetail = itemDetailsHash[itemId];
            if (itemDetail.indentDetailId && itemDetail.indentDetailId.length) {
              // indentDetailId List is present
              var indentDetailIds = itemDetail.indentDetailId;
              for (var indentId in selectedItem) {
                if (indentDetailIds.indexOf(indentId) == -1) {
                  // indentId not found indentDetailIds list
                  //itemDetail.indentDetailId.push(indentId);
                  //itemDetail.pendingQuantity += selectedItem[indentId].pendingQuantity;
                  
                  /* start code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
                	//comment above 2 line
                  let obj = angular.copy(itemDetail);
                  obj.indentDetailId = [];
                  obj.indentDetailId.push(indentId);
                  obj.pendingQuantity = selectedItem[indentId].pendingQuantity;
                  newItemDetails.push(obj);
                  /* end code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
                  
                }
              }

            } else {
              // indentDetailId List not present
              itemDetail.pendingQuantity = 0;
              itemDetail.indentDetailId = [];
              let singleIdflag =true ;
              for (var indentId in selectedItem) {
                //itemDetail.indentDetailId.push(indentId);
                //itemDetail.pendingQuantity += selectedItem[indentId].pendingQuantity;
                
                /* start code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */ 
            	//comment above 2 line
            	if(singleIdflag){
            		itemDetail.indentDetailId.push(indentId);
                    itemDetail.pendingQuantity = selectedItem[indentId].pendingQuantity;
                    singleIdflag =false;
            	}else {
            		let obj = angular.copy(itemDetail);
            		obj.indentDetailId = [];
            		obj.indentDetailId.push(indentId);
            		obj.pendingQuantity = selectedItem[indentId].pendingQuantity;
            		newItemDetails.push(obj);
            	}
                /* end code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
              }
            }
          } else {
            // not found in old list
            item.pendingQuantity = 0;
            item.indentDetailId = [];
            let singleIdflag =true ;
            for (var indentId in selectedItem) {
              //item.indentDetailId.push(indentId);
              //item.pendingQuantity += selectedItem[indentId].pendingQuantity;
              
            	/* start code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */ 
            	//comment above 2 line
            	if(singleIdflag){
            		item.indentDetailId.push(indentId);
            		item.pendingQuantity = selectedItem[indentId].pendingQuantity;
                    singleIdflag =false;
            	}else {
            		let obj = angular.copy(item);
            		obj.indentDetailId = [];
            		obj.indentDetailId.push(indentId);
            		obj.prMaxStoreQty = 0;
            		obj.newQty = obj.stockAvailable;
                    if (self.model.assetType == 1) {
                      selectUOM(obj);
                    } else {
                    	obj.newMaxOrderQuantity = obj.maxOrderQuantity;
                    	obj.newMaxStoreQuantity = obj.maxStoreQuantity;
                    }
            		obj.pendingQuantity = selectedItem[indentId].pendingQuantity;
            		newItemDetails.push(obj);
            	}
                /* end code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
            }

            // item.maxStoreQuantity = 1000;
            item.prMaxStoreQty = 0;
            self.model.itemDetails.push(item);
            item.newQty = item.stockAvailable;

            if (self.model.assetType == 1) {
              selectUOM(item);
            } else {
              item.newMaxOrderQuantity = item.maxOrderQuantity;
              item.newMaxStoreQuantity = item.maxStoreQuantity;
            }
          }
        }
      });
      
      /* start code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
      if(newItemDetails.length > 0 ) {
    	  self.model.itemDetails = self.model.itemDetails.concat(newItemDetails);
      }
      /* end code for resolved HIS-10265 PR against indent - Not able to add multiple indent request */
      
    }

    var AddIndentItems = function () {
      if (self.model.indentItemDetails.length == 0) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var selectedItems = getSelectedItems();
        var selectedIds = [];
        for (var key in selectedItems) {
          selectedIds.push(key);
        }

        var data = {
          "itemIds": selectedIds,
          "storeId": self.model.store.id
        };

        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var responseData = response.data;
            var newResponse = [];

            responseData.map(function (obj) {
              if (obj.maxOrderQuantity > 0) {
                newResponse.push(obj);
              }
            });
            responseData = newResponse;
            updateItemDetails(responseData, selectedItems);
            self.model.indentItemDetails = [];

            var popup = angular.element('#getRequestWasteStore');
            popup.modal('hide');
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            // self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        if(self.model.SelectedItemsIds){
          self.model.data = self.model.items.map(function (item) {
            item.isSelected = true;
            self.model.SelectedItemsIds.push(item.itemId);
            return self.model.SelectedItemsIds;
          });
        }
        else{
          self.model.SelectedItemsIds = self.model.items.map(function (item) {
            item.isSelected = true;
            return item.itemId;
          });
        }
      } else {
        self.model.SelectedItemsIds = [];
        self.model.items.forEach(function (item) {
          item.isSelected = false;
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

      if (self.model.items.every(isSelected)) {
        self.model.selectAllRows = true;
      } else {
        self.model.selectAllRows = false;
      }
    }

    var ConfirmRemove = function (item) {
      var popup = angular.element('#confirmRemove');
      popup.modal('show');
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var newArray = [];

      self.model.itemDetails.map(function (obj) {
        if (obj !== item) {
          newArray.push(obj);
        }
      });
      self.model.itemDetails = newArray;
      var popup = angular.element('#confirmRemove');
      popup.modal('hide');
    }

    var SavePurchaseRequest = function () {
      if (ValidateItems()) {
        if (!self.model.purchaseId) {
          CreatePurchaseRequest();
        } else {
          UpdatePurchaseRequest();
        }
      }
    }

    var CreatePurchaseRequest = function () {
      var purchaseRequest = self.model;

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.SAVE_API;

      var data = {
        "storeId": purchaseRequest.store.id,
        "requestingStoreId": purchaseRequest.fromStore.id,
        "priorityId": purchaseRequest.priority ? purchaseRequest.priority.id : undefined,
        "assetTypeId": purchaseRequest.assetType,
        "prTypeId": purchaseRequest.PRType,
        "remark": purchaseRequest.remark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "requestTypeId": purchaseRequest.PRType,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(purchaseRequest.itemDetails, function (item) {
        var purchaseItem = {
          "itemId": item.itemId, 
          "quantity": item.indentQuantity,
          "specification": item.itemSpecifications,
          "statusId": setStatus('New'),
          "indentId": item.indentDetailId ? item.indentDetailId : undefined,
          "pendingQuantity": item.newMaxOrderQuantity
        }

        if (self.model.assetType == 2) {
          purchaseItem.uomTypeId = undefined;
          purchaseItem.uomUnitId = undefined;
        } else if (self.model.assetType == 1) {
          purchaseItem.uomTypeId = item.selectedUom.uomTypeId;
          purchaseItem.uomUnitId = item.selectedUom.uomUnitId;
        }
        data.items.push(purchaseItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToPurchaseRequest();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });

    }

    var UpdatePurchaseRequest = function () {
      var purchaseRequest = self.model;

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.SAVE_API + self.model.purchaseId;

      var data = {
        "priorityId": purchaseRequest.priority.id,
        "remark": purchaseRequest.remark,
        "assetTypeId": purchaseRequest.assetType,
        "requestTypeId": purchaseRequest.PRType,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };
      angular.forEach(self.model.itemDetails, function (item) {
        var purchaseItem = {
          "id": item.id,
          "itemId": item.itemId,
          "quantity": parseInt(item.indentQuantity),
          "specification": item.itemSpecifications,
          "statusId": setStatus('New'),
          "indentId": item.indentDetailId ? item.indentDetailId : undefined,
          "pendingQuantity": item.newMaxOrderQuantity
        }

        if (isNaN(item.pendingQuantity)) {
          purchaseItem.indentQuantity = undefined;
        } else {
          purchaseItem.indentQuantity = item.indentQuantity;
        }

        if (self.model.assetType == 2) {
          purchaseItem.uomTypeId = undefined;
          purchaseItem.uomUnitId = undefined;
        } else if (self.model.assetType == 1) {
          purchaseItem.uomTypeId = item.selectedUom.uomTypeId;
          purchaseItem.uomUnitId = item.selectedUom.uomUnitId;
        }

        data.items.push(purchaseItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
          NavigateToPurchaseRequest();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoader();
        });
    }

    var NavigateToPurchaseRequest = function () {
      $state.go('purchaseRequest');
    }

    var ViewPOUom = function (item) {
      let uomList = item.uom.filter(x => x.uomType != "Select UOM");
      self.model.POUoms = uomList;
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewpurchaseRequest", {
      url: "/addNewpurchaseRequest",
      templateUrl: 'views/procurement/purchase/add-new-purchase-request.html',
      controller: "AddNewPurchaseRequest.Controller",
      controllerAs: "vm",
      title: 'Purchase Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewPurchaseRequest.Controller", addNewPurchaseRequestController);
})();