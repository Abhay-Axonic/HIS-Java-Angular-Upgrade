(function () {
  "use strict";

  function addNewLocalPRController($scope, $rootScope, $http, $state, CONSTANTS, GenericService,PaginationService, commonDetailService, CurrencyService, rejectReasonService, StatusService, growl,$cookies, $filter) {
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
        screenId: 5,
        assetCategories: [{
          "category": "Select Asset Category"
        }],
        productCategories: [{
          "category": "Select Product Category"
        }],
        fromStoresData: [{
          "store": "Select Store"
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
        intendItemIds: [],
        indentItemDetails: [],
        isUOMValid: [],
        isCop: [],
        search: {
          itemName: undefined,
          itemCode: undefined
        },
        uomArray: [{
          "uomType": "Select UOM"
        }],
        prRequestTypeId: 1,
        reqAssetTypeId: 1,
        PRType: 1,
        assetType: 1,
        POUoms: []
      };

      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });

      rejectReasonService.GetReason().then(function(reason) {
        self.model.rejectReasons = reason;
      });
      
      StatusService.GetStatus().then(function(status) {
        self.model.statusList = status;
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
      self.model.NavigateToLocalPR = NavigateToLocalPR;
      self.model.ChangePRType = ChangePRType;
      self.model.GetIndentList = GetIndentList;
      self.model.Expand = Expand;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.SelectAllIndentItems = SelectAllIndentItems;
      self.model.SelectIndentItem = SelectIndentItem;
      self.model.AddIndentItems = AddIndentItems;
      self.model.CalculateStock = CalculateStock;
      self.model.SetDecimal = SetDecimal;
      self.model.ValidatePRQuantity = ValidatePRQuantity;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.ChangeValueForUpdate = ChangeValueForUpdate;
      self.model.ConfirmedUOM = ConfirmedUOM;
      self.model.SetOldValueForUpdate = SetOldValueForUpdate;
      self.model.ConfirmValidation = ConfirmValidation;
      self.model.EmptyList = EmptyList;
      self.model.cancelRequestPopup = cancelRequestPopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.EmptyAssetList = EmptyAssetList;
      self.model.cancelAssetPopup = cancelAssetPopup;
      self.model.ViewPOUom = ViewPOUom;

      PopulateDropdown();
    }

    var setStatus = function (x){
      var status = self.model.statusList.find(function(obj){
        return obj.status == x;
      });
      
      return status.id;
    }

    var SetOldValueStore = function(store){
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeFromStore = function(store){
      if (!!self.model.itemDetails.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        OpenPopup('Open', 'confirmStore');
      }
    }

    var ConfirmStorePopup = function(){
      self.model.store = self.model.storeNew;
      ClosePopup('close', 'confirmStore');
      self.model.itemDetails = [];
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

    var ChangeValueForUpdate = function(item, selectedUom, index){
      if (!!item.id) {
        self.model.newUom = JSON.parse(JSON.stringify(item.selectedUom));
        item.selectedUom = self.model.oldUOM;
        self.model.uomIndex = item;
        OpenPopup('Open', 'confirmForUOM');
      } else {
        self.model.CalculateStock(item, item.uom, item.selectedUom);
      }
    }

    var SetOldValueForUpdate = function(item, detailId){
       self.model.oldUOM = item.selectedUom;
    }

    var ConfirmedUOM = function(){
      self.model.uomIndex.selectedUom = self.model.newUom;
      self.model.CalculateStock(self.model.uomIndex, self.model.uomIndex.uom, self.model.uomIndex.selectedUom);
      ClosePopup('close', 'confirmForUOM');
    }

    var ConfirmValidation = function(prType){
      self.model.prRequestTypeId = prType;
      if (!!self.model.itemDetails.length) {
        OpenPopup('open', 'confirmValidation');
      } else {
        self.model.PRType = prType;
      }
    }

    var EmptyList = function(){
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

    var SetOldValueAssetType = function(typeId){
      self.model.oldAssetTypeId = typeId;
      self.model.assetType = self.model.oldAssetTypeId;
    }
    
    var ChangeFromAssetType = function(typeId){
      self.model.reqAssetTypeId = typeId;
      if (!!self.model.itemDetails.length) {
        OpenPopup('open', 'confirmFromAssetType');
      } else {
        self.model.assetType = typeId;
      }
    }

    var EmptyAssetList = function(){
      self.model.assetType = self.model.reqAssetTypeId;
      self.model.itemDetails = [];
      
      ClosePopup('close', 'confirmFromAssetType');
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
        var localPurchase = self.model;
        localPurchase.store = localPurchase.stores[0];
        localPurchase.fromStoreData = localPurchase.fromStoresData[0];

        var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            localPurchase.stores = localPurchase.stores.concat(response.data.deliveryStores);
            localPurchase.fromStoresData = localPurchase.fromStoresData.concat(response.data.requestingStores);
            localPurchase.priorities = response.data.priority;
            localPurchase.priority = response.data.priority[1];
            var purchaseId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
            for(var i=0; i<localPurchase.fromStoresData.length;i++)
            {
              if(i !=0)
              {
                if(localPurchase.fromStoresData[i].id==selectedStoreId)
                {
                  localPurchase.fromStoreData = localPurchase.fromStoresData[i];
                }  
              }
            }
            
            if (!!purchaseId) {
              commonDetailService.setDataId(null);
              GetLocalPR(purchaseId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var ChangePRType = function (prType){
      if (ValidateDetails()) {
        if (prType == 1) {
          self.model.action = 'Open';
          var popup = angular.element('#addNewItemWasteStore');
          popup.modal('show');
          PopulateAssetCategoryDropdown();
          self.model.items = [];
          self.model.pagination.data=[];
          self.model.selectAllRows = false;
          self.model.SelectedItemsIds = [];
          self.model.search.itemCode = undefined;
          self.model.search.itemName = undefined;
        }else if(prType == 2){
          self.model.action = 'Open';
          var popup = angular.element('#getRequestWasteStore');
          popup.modal('show');
          PopulateIndentDropdown();
          self.model.indents = [];
          self.model.searchIndent = undefined;
        }
      }
    }

    var GetLocalPR = function (purchaseId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.SAVE_API + $scope.unitId + '/'+$scope.organizationId + '/' + purchaseId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.purchaseId = response.data.id;
          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
          });

          self.model.fromStoreData = self.model.fromStoresData.find(function (item) {
            return response.data.requestingStoreId == item.id;
          });

          self.model.priority = self.model.priorities.find(function (item) {
            return response.data.priorityId == item.id;
          });
          // self.model.assetType = response.data.assetTypeId;
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
              "availableQuantity": item.availableQuantity,
              "itemSpecifications": item.specifications,
              "quantity": item.quantity,
              "cop": item.cop.toFixed(2),
              "indentQuantity": item.indentQuantity,
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
                item.uom = [];
                indentItem.uom = [];
                angular.forEach(indentItem.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    item.uom.push(data);
                    indentItem.uom.push(data);
                  }
                });
              }
              if(indentItem.selectedUom){
                if(indentItem.selectedUom.uomType=="Dispense")
                {
                  indentItem.prQuantity = indentItem.quantity;
                }
                else if(indentItem.selectedUom.uomType=="Stock")
                {
                  indentItem.prQuantity = indentItem.quantity * (indentItem.uom[2].conversion);
                }
                else if(indentItem.selectedUom.uomType=="Purchase")
                {
                  indentItem.prQuantity = indentItem.quantity * (indentItem.uom[2].conversion* indentItem.uom[1].conversion);
                }
              }
              CalculateStock(indentItem, item.uom, indentItem.selectedUom);
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

    var ValidateDetails = function (){
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
        growl.error('Please select required fields');
      }

      return isValid;
    }

    var ValidatePRQuantity = function(item){
      var isValidItem = true;
      var prQty = parseInt(item.quantity);

      if(item.selectedUom){
        if(item.selectedUom.uomType=="Dispense")
        {
          item.prQuantity = prQty;
        }
        else if(item.selectedUom.uomType=="Stock")
        {
          item.prQuantity = prQty * (item.uom[2].conversion);
        }
        else if(item.selectedUom.uomType=="Purchase")
        {
          item.prQuantity = prQty * (item.uom[2].conversion * item.uom[1].conversion);
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
        item.isQuantity = true;
        isValidItem = false;
      }

      if (!prQty) {
        growl.error('Please enter valid PR quantity');
        item.isQuantity = true;
        isValidItem = false;
      }

      return item, isValidItem;
    }

    var ValidateItems = function (){
      var isValidItem = true;

      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add items');
        isValidItem = false;
      } else if (!self.model.priority) {
        growl.error('Please select priority');
        isValidItem = false;
      } else {
        for (var i = 0; i < self.model.itemDetails.length; i++) {
          self.model.itemDetails[i], isValidItem = ValidatePRQuantity(self.model.itemDetails[i]);

          if (!parseFloat(self.model.itemDetails[i].cop)) {
            growl.error('Please enter valid COP');
            self.model.isCop[i] = true;
            isValidItem = false;
          }

          if (self.model.assetType == 1) {
            if (!self.model.itemDetails[i].selectedUom.uomUnitId) {
              self.model.isUOMValid[i] = true;
              growl.error('Please select valid UOM');
              isValidItem = false;
            }
          }
        }
      }

      return isValidItem;
    }

    var SetDecimal = function (item) {
      if (isNaN(item.cop) || !item.cop) {
        item.cop = 0;
      } else {
        item.cop = parseFloat(item.cop).toFixed(2);
      }
    }

    var PopulateAssetCategoryDropdown = function () {
      var purchaseRequest = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetType+ '/'+$scope.unitId+ '/'+$scope.organizationId;

      purchaseRequest.assetCategory = purchaseRequest.assetCategories[0];
      purchaseRequest.productCategory = purchaseRequest.productCategories[0];

      purchaseRequest.assetCategories.splice(1);
      purchaseRequest.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          purchaseRequest.assetCategories = purchaseRequest.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateProductCategory = function (assetCategory) {
      var purchaseRequest = self.model;

      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + self.model.assetCategory.id + '/'+$scope.organizationId;
      
      purchaseRequest.productCategory = purchaseRequest.productCategories[0];
      purchaseRequest.productCategories.splice(1);
      
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          purchaseRequest.productCategories = purchaseRequest.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var PopulateIndentDropdown = function () {
      var purchaseRequest = self.model;
      self.model.indents = [];
      var URI = CONSTANTS.INVENTORY.STORE.STORE_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

      purchaseRequest.fromStore = purchaseRequest.fromStores[0];
      purchaseRequest.toStore = purchaseRequest.toStores[0];

      purchaseRequest.fromStores.splice(1);
      purchaseRequest.toStores.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          purchaseRequest.fromStores = purchaseRequest.fromStores.concat(response.data.fromStore);
          purchaseRequest.toStores = purchaseRequest.toStores.concat(response.data.toStore);
          $rootScope.stopLoader();
        }, function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetIndentList = function () {
      var fromStore = self.model.fromStore.id;
      var toStore = self.model.toStore.id;
      if (!fromStore || !toStore) {
        self.model.indents = [];
        growl.error('Please select valid store');
        return false;
      }/*else if (fromStore == toStore) {
        self.model.indents = [];
        growl.error('From store and to store should not be same');
        return false;
      }*/
      else{
        var URI = CONSTANTS.GLOBAL.GET_INDENTS_API  + $scope.unitId + '/'+ fromStore + '/' + toStore;
        $rootScope.startLoaderPopup();
        var data={};
        data.assetTypeId=self.model.assetType
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if(self.model.search.itemName){

         if ( self.model.search.itemName!=undefined  &&  (self.model.search.itemName.replace(/\s/g, "").length) == 2  ) {

          var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;

          var data = {
            "assetTypeId": self.model.assetType,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "requestingStoreId": self.model.fromStoreData.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName
          }

          self.model.items = [];
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
            function (response) {
              self.model.selectAllRows = false;
              self.model.items = response.data;
              if(!!response && !!response.data && response.data.length > 0){            
                PaginationService.searchDataByField( self.model.items, self.model.pagination);
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
        if (self.model.search.itemCode!=undefined  &&  (self.model.search.itemCode.replace(/\s/g, "").length) == 2  ) {

          var URI = CONSTANTS.GLOBAL.Item_Api_store + "unit/"+$scope.unitId;

          var data = {
            "assetTypeId": self.model.assetType,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "requestingStoreId": self.model.fromStoreData.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName
          }

          self.model.items = [];
          self.model.pagination.data=[];
          self.model.pagination.totalPages=0;
          $rootScope.startLoaderPopup();
          GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.selectAllRows = false;
            self.model.items = response.data;
            if(!!response && !!response.data && response.data.length > 0){            
              PaginationService.searchDataByField( self.model.items, self.model.pagination);
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
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
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id,
          "screenId": self.model.screenId
        };

        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function(item) {
              var isFound = self.model.itemDetails.find(function(x){
                return x.itemId == item.itemId;
              });

              // get items which has valid maxOrderQuantity
              if (!isFound) {
                self.model.itemDetails.push(item);
                item.newQty = item.stockAvailable;
                item.prMaxStoreQty = 0;
                if (self.model.assetType == 1) {
                  selectUOM(item);
                } else{
                  item.newMaxOrderQuantity = item.maxOrderQuantity;
                  item.newMaxStoreQuantity = item.maxStoreQuantity;
                }
              }
            });
            self.model.SelectedItemsIds = [];
            self.model.items = [];

            var popup = angular.element('#addNewItemWasteStore');
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

    var selectUOM = function (item) {
      item.uom = self.model.uomArray.concat(item.uom);
      item.uom[0].uomDetails = item.uom[0].uomType;
      item.selectedUom = item.uom[0];
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
        if (!!item.id) {
          var stockAvailable = item.availableQuantity;
        } else {
          var stockAvailable = item.stockAvailable;
        }
        if(item.prQuantity == undefined || item.prQuantity == null){
          item.prQuantity = 0;
        }

        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.quantity = parseInt(item.prQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.quantity +"*"+ item.uomListdata[1].conversion);
            item.receivedQtyCalculation = (item.quantity * item.uomListdata[1].conversion);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(stockAvailable / (item.uomListdata[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[3].conversion));
            item.quantity = parseInt(item.prQuantity / (item.uomListdata[3].conversion));
            item.receivedQtyCon = (item.quantity);
            item.receivedQtyCalculation = (item.quantity);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newQty = parseInt(stockAvailable / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.quantity = parseInt(item.prQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon = (item.quantity +"*"+ uom[1].conversion+"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[1].conversion * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newQty = parseInt(stockAvailable / (uom[2].conversion * uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion * uom[3].conversion));
            item.quantity = parseInt(item.prQuantity / (uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon=(item.quantity +"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.quantity * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.newQty = parseInt(stockAvailable / (uom[3].conversion));
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[3].conversion));
            item.quantity = parseInt(item.prQuantity / (uom[3].conversion));
            item.receivedQtyCon = (item.quantity);
            item.receivedQtyCalculation = (item.quantity);
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
        items.map(function(x){
          self.model.indentItemDetails.push(x);
          x.isSelectedItem = true;
          x.indentId = indent.indentId;
          return self.model.intendItemIds.push(x.itemId);
        });
      } else {
        self.model.intendItemIds = [];
        items.forEach(function (x) {
          x.isSelectedItem = false;
        });
        items.map(function(x){
          var idx = self.model.indentItemDetails.indexOf(x);
          if (idx > -1) {
            self.model.indentItemDetails.splice(idx, 1);
          }
        });
      }
    }

    var SelectIndentItem = function (item, indent) {
      var itemIds = self.model.intendItemIds;
      if (item.isSelectedItem) {
        itemIds.push(item.itemId);
        self.model.indentItemDetails.push(item);
      } else {
        var index = itemIds.indexOf(item.isSelectedItem);
        if (index > -1) {
          itemIds.splice(index, 1);
        }

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

    var getSelectedItems = function(){
      var hash = {};
      self.model.indentItemDetails.forEach(function(item) {
        var itemId = item.itemId;
        var indentId = item.id;
        if(hash.hasOwnProperty(itemId)){
          hash[itemId][indentId] = { pendingQuantity: item.quantity }
        }else {
          hash[itemId] = {};
          hash[itemId][indentId] = { pendingQuantity: item.quantity }
        }
      });
      return hash;
    }

    var updateItemDetails = function(responseData, selectedItems) {
      var itemDetailsHash = {};
      self.model.itemDetails.forEach(function(itemDetail){
        itemDetailsHash[itemDetail.itemId] = itemDetail;
      });

      responseData.forEach(function(item){
        var itemId = item.itemId;
        if(selectedItems.hasOwnProperty(itemId)) {
          var selectedItem = selectedItems[itemId]; 
          if(itemDetailsHash.hasOwnProperty(itemId)) {
            //found in old list
            var itemDetail = itemDetailsHash[itemId];
            if(itemDetail.indentDetailId && itemDetail.indentDetailId.length) {
              // indentDetailId List is present
              var indentDetailIds = itemDetail.indentDetailId;
               for(var indentId in selectedItem){
                if(indentDetailIds.indexOf(indentId) == -1){
                  // indentId not found indentDetailIds list
                  itemDetail.indentDetailId.push(indentId);
                  itemDetail.pendingQuantity += selectedItem[indentId].pendingQuantity;  
                }
              }
            }else {
              // indentDetailId List not present
              itemDetail.pendingQuantity = 0;
              itemDetail.indentDetailId = [];
              for(var indentId in selectedItem){
                itemDetail.indentDetailId.push(indentId);
                itemDetail.pendingQuantity += selectedItem[indentId].pendingQuantity;
              }
            }
          }else {
            // not found in old list
            item.pendingQuantity = 0;
            item.indentDetailId = [];
            for(var indentId in selectedItem){
              item.indentDetailId.push(indentId);
              item.pendingQuantity += selectedItem[indentId].pendingQuantity;
            }

            item.prMaxStoreQty = 0;
            self.model.itemDetails.push(item);
            if (self.model.assetType == 1) {
              selectUOM(item);
            }else {
              item.newMaxOrderQuantity = item.maxOrderQuantity;
              item.newMaxStoreQuantity = item.maxStoreQuantity;
            }
          }
        }
      });
      console.log(self.model.itemDetails);
    }

    var AddIndentItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if (self.model.intendItemIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var selectedItems = getSelectedItems();
        var selectedIds = [];
        for(var key in selectedItems){
          selectedIds.push(key);
        }
        
        var data = {
          "itemIds": selectedIds,
          "storeId": self.model.store.id
        };
    
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var responseData = response.    data;
            var responseData11 =[];
            responseData11=responseData;
       self.model.indents.forEach(function(item){
           if(item.isSelectedIndent == true)
         item.items.forEach(function(i){

          responseData11.forEach(function(data){
        	   data.availableQuantity=data.stockAvailable;
                  if(i.itemId ==data.itemId)
                  {
                      responseData.indentDetailsId=i.id;

                  }

                         } );
                });

         
        });


            var newResponse = [];

            responseData.map(function(obj){
              if(obj.maxOrderQuantity>0){
                newResponse.push(obj);
              }
            });
            
            responseData = newResponse;
            updateItemDetails(responseData, selectedItems);
            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];
            
            var popup = angular.element('#getRequestWasteStore');
            popup.modal('hide');

            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
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

    var ConfirmRemove = function(item){
      var popup = angular.element('#confirmRemove');
      popup.modal('show');
      self.model.itemForRemove = item;
    }

    var RemoveItem = function (item) {
      var newArray = [];

      self.model.itemDetails.map(function(obj){
        if(obj !== item){
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
          CreateLocalPR();
        } else {
          UpdateLocalPR();
        }
      }
    }

    var CreateLocalPR = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var localPurchase = self.model;

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.SAVE_API;

      var data = {
        "storeId": localPurchase.store.id,
        "requestingStoreId": localPurchase.fromStoreData.id,
        "priorityId": localPurchase.priority ? localPurchase.priority.id : undefined,
        "assetTypeId": localPurchase.assetType,
        "prTypeId": localPurchase.PRType,
        "remark": localPurchase.remark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "items": [],
        "organizationId":$scope.organizationId,
        "unitId":$scope.unitId,
        "userId":$scope.userId
      };

      angular.forEach(localPurchase.itemDetails, function (item, key) {
        var purchaseItem = {
          "itemId": item.itemId,
          "quantity": item.quantity,
          "cop": item.cop,
          "amountCop": item.quantity * item.cop,
          "indentQuantity": item.pendingQuantity,
          "specification": item.itemSpecifications,
          "statusId": setStatus('New'),
          "indentId": item.indentDetailId, 
          "pendingQuantity": item.newMaxOrderQuantity
        }
        if (self.model.assetType == 2) {
          purchaseItem.uomTypeId = undefined;
          purchaseItem.uomUnitId = undefined;
        } else if(self.model.assetType == 1) {
          purchaseItem.uomTypeId = item.selectedUom.uomTypeId;
          purchaseItem.uomUnitId = item.selectedUom.uomUnitId;
        }

        data.items.push(purchaseItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToLocalPR();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateLocalPR = function () {
      var localPurchase = self.model;

      var URI = CONSTANTS.PROCUREMENT.PURCHASE.LOCAL_PURCHASE_REQUEST.SAVE_API + localPurchase.purchaseId;

      var data = {
        "priorityId": localPurchase.priority.id,
        "remark": localPurchase.remark,
        "assetTypeId": localPurchase.assetType,
        "prTypeId": localPurchase.PRType,
        "items": [],
        "organizationId":$scope.organizationId,
        "unitId":$scope.unitId,
        "userId":$scope.userId
      };
      angular.forEach(self.model.itemDetails, function (item, key) {
        var purchaseItem = {
          "id": item.id,
          "itemId": item.itemId,
          "quantity": item.quantity,
          "cop": item.cop,
          "amountCop": item.quantity * item.cop,
          "indentQuantity": item.pendingQuantity,
          "specification": item.itemSpecifications,
          "statusId": setStatus('New'),
          "indentId": item.indentDetailId
        }
        if (self.model.assetType == 2) {
          purchaseItem.uomTypeId = undefined;
          purchaseItem.uomUnitId = undefined;
        } else if(self.model.assetType == 1) {
          purchaseItem.uomTypeId = item.selectedUom.uomTypeId;
          purchaseItem.uomUnitId = item.selectedUom.uomUnitId;
        }

        data.items.push(purchaseItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToLocalPR();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var NavigateToLocalPR = function () {
      $state.go('localPurchhaseRequest');
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
    var ViewPOUom = function (item) {
        let uomList = item.uom.filter(x => x.uomType != "Select UOM");
        self.model.POUoms = uomList;
      }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewlocalPurchhaseRequest", {
      url: "/addNewlocalPurchhaseRequest",
      templateUrl: 'views/procurement/purchase/add-new-local-purchase-request.html',
      controller: "addNewLocalPR.Controller",
      controllerAs: "vm",
      title: 'Local Purchase Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("addNewLocalPR.Controller", addNewLocalPRController);
})();