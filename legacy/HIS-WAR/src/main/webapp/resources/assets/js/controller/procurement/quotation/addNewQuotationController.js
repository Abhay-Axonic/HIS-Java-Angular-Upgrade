 (function () {
  "use strict";

  function addNewQuotationController($scope, $rootScope, $http, $filter, $state, CONSTANTS, GenericService,PaginationService, commonDetailService, growl, CurrencyService, StatusService,$cookies) {
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
          "storeName": "Select Store"
        }],
        toStores: [{
          "storeName": "Select Store"
        }],
        assetTypes: [{
          "type": "Select Asset Type"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        intendItemIds: [],
        indentItemDetails: [],
        prQuantity: [],
        prDetails: [],
        prModalList: [],
        prDetailList: [],
        expectedAccessories: [], 
        accessoryItem: [],
        activeTab: 1,
        termsList: [],
        search: {
          itemName: undefined,
          itemCode: undefined
        },
        accessoryFlag: false,
        termsFlag: false,
        requestType: 1,
        prRequestTypeId: 1,
        uomArray: [{
          "uomType": "Select UOM"
        }],
        accessoryList: [{"name": "", "details": ""}],
        screenId: 37
      };

      StatusService.GetStatus().then(function (status) {
        self.model.statuses = status;
      });

      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });

      self.model.OpenFromDate = OpenFromDate;
      self.model.OpenToDate = OpenToDate;
      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SaveQuotationEnquiry = SaveQuotationEnquiry;
      self.model.NavigateToQuotationEnquiry = NavigateToQuotationEnquiry;
      self.model.ChangeRequestType = ChangeRequestType;
      self.model.GetIndentList = GetIndentList;
      self.model.Expand = Expand;
      self.model.SelectAllIndentItems = SelectAllIndentItems;
      self.model.SelectIndentItem = SelectIndentItem;
      self.model.AddIndentItems = AddIndentItems;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.fillPRModalList = fillPRModalList;
      self.model.PrDetails = PrDetails;
      self.model.FillAccessoryDetails = FillAccessoryDetails;
      self.model.AddAccessory = AddAccessory;
      self.model.LessAccessory = LessAccessory;
      self.model.SaveAccessoryDetails = SaveAccessoryDetails;
      self.model.ViewTerms = ViewTerms;
      self.model.SaveTerms = SaveTerms;

      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
      self.model.ConfirmValidation = ConfirmValidation;
      self.model.EmptyList = EmptyList;
      self.model.ChangeValueForUpdate = ChangeValueForUpdate;
      self.model.SetOldValueForUpdate = SetOldValueForUpdate;
      self.model.ConfirmedUOM = ConfirmedUOM;
      self.model.ValidatePRQuantity = ValidatePRQuantity;
      self.model.CalculateStock = CalculateStock;
      self.model.SetActiveTab = SetActiveTab;

      PopulateDropdown();
    }

    var SetActiveTab = function (tab) {
      self.model.activeTab = tab;
    }

    var ViewTerms = function (item) {
      var popup = angular.element('#vendorSelectionDetailsModal2');
      popup.modal('show');
      self.model.activeTab = 1;

      self.model.termsList = item;
      self.model.warrantyPeriod = item.warrantyPeriod;
      self.model.supportTerms = item.supportTerms;
      self.model.paymentTerms = item.paymentTerms;
      self.model.technicalSpecification = item.technicalSpecification;
    }

    var SaveTerms = function () {
      if (ValidateTerms()) {
        self.model.termsList["warrantyPeriod"] = self.model.warrantyPeriod;
        self.model.termsList["supportTerms"] = self.model.supportTerms;
        self.model.termsList["paymentTerms"] = self.model.paymentTerms;
        self.model.termsList["technicalSpecification"] = self.model.technicalSpecification;

        self.model.termsList["isValidTerms"] = false;
        ClosePopup('Close', 'vendorSelectionDetailsModal2');
      }
    }

    var ValidateTerms = function () {
      var isValid = true;
      if (self.model.assetType.id == 1) {
        if (!self.model.paymentTerms) {
          growl.error('Expected payment terms are required');
          isValid = false;
        }
      }else{
        if (!self.model.warrantyPeriod || !self.model.supportTerms || !self.model.paymentTerms || !self.model.technicalSpecification) {
          growl.error("Please enter all support terms");
          item.allTermsValid = true;
          isValidItem = false;
        }
      }

      return isValid;
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

    // asset type change confirmation
    var SetOldValueAssetType = function(assetType){
      self.model.oldAssetType = assetType;
      self.model.assetType = self.model.oldAssetType;
    }

    var ChangeFromAssetType = function(assetType){
      if (!!self.model.itemDetails.length) {
        self.model.assetType = self.model.oldAssetType;
        self.model.assetTypeNew = JSON.parse(JSON.stringify(assetType));
        OpenPopup('Open', 'confirmAssetTypePopup');
      }
    }

    var ConfirmAssetTypePopup = function(){
      self.model.assetType = self.model.assetTypeNew;
      ClosePopup('close', 'confirmAssetTypePopup');
      self.model.itemDetails = [];
    }

    var ConfirmValidation = function(prType){
      self.model.prRequestTypeId = prType;
      if (!!self.model.itemDetails.length) {
        OpenPopup('open', 'confirmValidation');
      } else {
        self.model.requestType = prType;
      }
    }

    var EmptyList = function(){
      self.model.requestType = self.model.prRequestTypeId;
      self.model.itemDetails = [];
      
      ClosePopup('close', 'confirmValidation');
    }

    var ChangeValueForUpdate = function(item, selectedUom, index){
      if (!!item.id) {
        self.model.newUom = JSON.parse(JSON.stringify(item.selectedUom));
        item.selectedUom = self.model.oldUOM;
        self.model.uomIndex = item;
        OpenPopup('Open', 'confirmForUOM');
      } else {
        CalculateStock(item, item.uom, item.selectedUom);
      }
    }

    var SetOldValueForUpdate = function(item, detailId){
       self.model.oldUOM = item.selectedUom;
    }

    var ConfirmedUOM = function(){
      self.model.uomIndex.selectedUom = self.model.newUom;
      self.model.CalculateStock(self.model.uomIndex,self.model.uomIndex.uom,self.model.uomIndex.selectedUom);
      ClosePopup('close', 'confirmForUOM');
    }

    var PrDetails = function (prDetailList) {
      var popup = angular.element('#PRDetails');
      popup.modal('show');
      self.model.prDetailList = prDetailList;
    }

    var fillPRModalList = function (list) {
      self.model.prModalList = list;
    }

    var FillAccessoryDetails = function (item) {
      self.model.OpenPopup('Open', 'AccessoriesDetailsQEModal');
      self.model.accessoryItem = item;
      self.model.expectedAccessories = item.expectedAccessories;
    }

    var AddAccessory = function () {
      self.model.expectedAccessories.push({"name": "", "details": ""});
    }

    var LessAccessory = function (item) {
      var index = self.model.expectedAccessories.indexOf(item);
      self.model.expectedAccessories.splice(index, 1);
    }

    var SaveAccessoryDetails = function () {
      if (ValidateAccessories()) {
        self.model.accessoryItem.expectedAccessories = self.model.expectedAccessories;
        self.model.accessoryItem.accessoryValid = false;
        ClosePopup('Close', 'AccessoriesDetailsQEModal');
      }
    }

    var ValidateAccessories = function () {
      var isValid = true;
      if (self.model.assetType.id == 2) {
        angular.forEach(self.model.expectedAccessories, function(accessory){
          if (!accessory.name || !accessory.details) {
            growl.error('All fields are required');
            isValid = false;
          }
        });
      }

      return isValid;
    }

    var setStatus = function (x) {
      var status = self.model.statuses.find(function (obj) {
        return obj.status == x;
      });

      return status.id;
    }

    var OpenFromDate = function () {
      self.model.fromDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
    }

    var OpenToDate = function () {
      self.model.toDateOpened = true;
      self.model.toDateOptions.minDate = self.model.fromDate;
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

    var CalculateStock = function (item, uom, uomList) {
      if (!!uomList.uomTypeId) {
        var maxOrderQuantity = item.maxOrderQuantity || 0;
        var maxStoreQuantity = item.maxStoreQuantity || 0;
        var prQuantity = item.newPrQuantity || 0;
        
        if(item.prQty == undefined || item.prQty == null){
          item.prQty = 0;
        }

        uomList.uomDetailsData = uomList.uomDetails.split(' ')[0];
        if(item.isThreeUOMApplicable == false){
          if (uomList.uomDetailsData == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.prQuantity = parseInt(item.prQty / (item.uomListdata[1].conversion * item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.prQuantity +"*"+ item.uomListdata[1].conversion);
            item.receivedQtyCalculation = (item.prQuantity * item.uomListdata[1].conversion);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
            item.prQuantity = parseInt(item.prQty / (item.uomListdata[2].conversion * item.uomListdata[3].conversion));
          } else if (uomList.uomDetailsData == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (item.uomListdata[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (item.uomListdata[3].conversion));
            item.prQuantity = parseInt(item.prQty / (item.uomListdata[3].conversion));
            item.receivedQtyCon=(item.prQuantity);
            item.receivedQtyCalculation = (item.prQuantity);
            item.uomUnitData = item.uomListdata[3].uomUnit;
          }
        }
        else{
          if (uomList.uomDetailsData == "P") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.prQuantity = parseInt(item.prQty / (uom[1].conversion * uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon = (item.prQuantity +"*"+ uom[1].conversion+"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.prQuantity * uom[1].conversion * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "S") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[2].conversion * uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[2].conversion * uom[3].conversion));
            item.prQuantity = parseInt(item.prQty / (uom[2].conversion * uom[3].conversion));
            item.receivedQtyCon=(item.prQuantity +"*"+ uom[2].conversion);
            item.receivedQtyCalculation = (item.prQuantity * uom[2].conversion);
            item.uomUnitData = uom[3].uomUnit;
          } else if (uomList.uomDetailsData == "D") {
            item.newMaxOrderQuantity = parseInt(maxOrderQuantity / (uom[3].conversion));
            item.newMaxStoreQuantity = parseInt(maxStoreQuantity / (uom[3].conversion));
            item.prQuantity = parseInt(item.prQty / (uom[3].conversion));
            item.receivedQtyCon=(item.prQuantity);
            item.receivedQtyCalculation = (item.prQuantity);
            item.uomUnitData = uom[3].uomUnit;
          }
        }
      } else {
        item.newMaxOrderQuantity = 0;
        item.newMaxStoreQuantity = 0;
        item.prQuantity = 0;
      }
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];
        self.model.assetType = self.model.assetTypes[0];

        var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);

            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            var selectedStoreId = selectedTopStore != null ? selectedTopStore.storeId : undefined;
            for(var i=0;i<self.model.stores.length;i++)
            {
              if(self.model.stores[i].id==selectedStoreId)
              {
                self.model.store = self.model.stores[i];
              }
            }
            var prId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            if (!!prId) {
              commonDetailService.setDataId(null);
              GetQuotationEnquiry(prId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var ChangeRequestType = function (requestType) {
      if (ValidateDetails()) {
        if (requestType == 3) {
          self.model.action = 'Open';
          var popup = angular.element('#againstPRmodal');
          self.model.fromDate = new Date();
          self.model.toDate = new Date();
          self.model.prNumber = undefined;
          self.model.searchItem = undefined;
          self.model.indents = [];
          popup.modal('show');
        } else if (requestType == 1) {
          self.model.items = [];
          self.model.pagination.data=[];
          self.model.selectAllRows = false;
          self.model.SelectedItemsIds = [];
          PopulateAssetCategoryDropdown();
          self.model.search.itemName = undefined;
          self.model.search.itemCode = undefined;
          self.model.action = 'Open';
          var popup = angular.element('#addNewItemWasteStore');
          popup.modal('show');
        }
      }
    }

    var GetQuotationEnquiry = function (prId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.SAVE_API + prId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.quotationId = response.data.id;
          self.model.store = self.model.stores.find(function (store) {
            return response.data.storeId == store.id;
          });
          self.model.assetType = self.model.assetTypes.find(function (assetType) {
            return response.data.assetTypeId == assetType.id;
          });

          self.model.requestType = response.data.requestTypeId;
          self.model.prRequestTypeId = response.data.requestTypeId;
          self.model.remark = response.data.remark;

          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {

            var expectedAccessories = [];
            angular.forEach(item.expectedAccessories, function (accessoryItem) {
              var item = {
                "id": accessoryItem.id,
                "name": accessoryItem.name,
                "details": accessoryItem.details
              }
              expectedAccessories.push(item);
            });

            var prIds = [];
            item.prDetails.forEach(function(x){
              prIds.push(x.id);
            });

            var prItem = {
              "id": item.id,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "uom": item.uom,
              "prQuantity": item.quantity,
              "prDetailList": item.prDetails,
              "expectedAccessories": expectedAccessories,
              "warrantyPeriod": item.warrantyPeriod,
              "supportTerms": item.supportTerms,
              "paymentTerms": item.paymentTerms,
              "maxStoreQuantity": item.maxStoreQuantity,
              "maxOrderQuantity": item.qrPendingquantity,
              "prMaxStoreQty": item.quantity,
              "technicalSpecification": item.technicalSpecification,
              "prIds": prIds,
              "newPrQuantity": item.quantity,
              "pendingQuantity": item.qrPendingquantity,
              "isThreeUOMApplicable": item.isThreeUOMApplicable
            }
            self.model.itemDetails.push(prItem);
            if (self.model.assetType.id == 1) {
              prItem.selectedUom = item.uom.find(function (uom) {
                return item.uomTypeId == uom.uomTypeId;
              });
              prItem.uom = self.model.uomArray.concat(item.uom);
              item.uom = self.model.uomArray.concat(item.uom);
              item.uom[0].uomDetails = item.uom[0].uomType; 
              if(item.isThreeUOMApplicable == false){
                prItem.uomListdata = item.uom;
                prItem.uom = [];
                angular.forEach(prItem.uomListdata, function(data){
                  if(data.uomType != 'Stock'){
                    prItem.uom.push(data);
                  }
                });
              }
              //  CalculateStock(prItem, item.uom, prItem.selectedUom);
              prItem.newMaxOrderQuantity = item.qrPendingquantity;
              prItem.pendingQuantity = item.qrPendingquantity;
              item.pendingQuantity = item.qrPendingquantity;
              if (prItem.selectedUom.uomType == "Purchase") {
                prItem.newMaxStoreQuantity = parseInt(item.maxStoreQuantity / (item.uom[1].conversion * item.uom[2].conversion * item.uom[3].conversion));
                if(item.isThreeUOMApplicable == false){
                  prItem.receivedQtyCon=(item.quantity +"*"+ item.uom[1].conversion);
                  prItem.receivedQtyCalculation = (item.quantity * item.uom[1].conversion);
                  prItem.uomUnitData = item.uom[3].uomUnit;
                }
                else{
                  prItem.receivedQtyCon = (item.quantity +"*"+ item.uom[1].conversion+"*"+ item.uom[2].conversion);
                  prItem.receivedQtyCalculation = (item.quantity * item.uom[1].conversion * item.uom[2].conversion);
                  prItem.uomUnitData = item.uom[3].uomUnit;
                }
              } else if (prItem.selectedUom.uomType == "Stock") {
                prItem.newMaxStoreQuantity = parseInt(item.maxStoreQuantity / (item.uom[2].conversion * item.uom[3].conversion));
                if(item.isThreeUOMApplicable != false){
                  prItem.receivedQtyCon=(item.quantity +"*"+ item.uom[2].conversion);
                  prItem.receivedQtyCalculation = (item.quantity * item.uom[2].conversion);
                  prItem.uomUnitData = item.uom[3].uomUnit;
                }
              } else if (prItem.selectedUom.uomType == "Dispense") {
                prItem.newMaxStoreQuantity = parseInt(item.maxStoreQuantity / (item.uom[3].conversion));
                prItem.receivedQtyCon=(item.quantity);
                prItem.receivedQtyCalculation = (item.quantity);
                prItem.uomUnitData = item.uom[3].uomUnit;
              }
            } else {
              /*prItem.newMaxOrderQuantity = item.qrPendingquantity;
              if (prItem.selectedUom.uomType == "Purchase") {
                prItem.newMaxStoreQuantity = parseInt(item.maxStoreQuantity / (item.uom[1].conversion * item.uom[2].conversion * item.uom[3].conversion));
              } else if (prItem.selectedUom.uomType == "Stock") {
                prItem.newMaxStoreQuantity = parseInt(item.maxStoreQuantity / (item.uom[2].conversion * item.uom[3].conversion));
              } else if (prItem.selectedUom.uomType == "Dispense") {
                prItem.newMaxStoreQuantity = parseInt(item.maxStoreQuantity / (item.uom[3].conversion));
              }*/
            	
              prItem.newMaxOrderQuantity = item.qrPendingquantity;
              prItem.newMaxStoreQuantity = item.maxStoreQuantity;
            }

            if(self.model.assetType.id != 2){
              var prQty = parseInt(item.quantity);
              if(prItem.selectedUom.uomType== "Dispense")
              {
                prItem.prQty = prQty;
              }
              else if(prItem.selectedUom.uomType=="Stock")
              {
                prItem.prQty = prQty * (item.uom[2].conversion);
              }
              else if(prItem.selectedUom.uomType=="Purchase")
              {
                prItem.prQty = prQty * (item.uom[2].conversion* item.uom[1].conversion);
              }
            }
          });

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var ValidateDetails = function () {
      var isValid = true;

      if (!self.model.store.id) {
        self.model.isStoreValid = true;
        isValid = false;
      }

      if (!self.model.assetType.id) {
        self.model.isAssetTypeValid = true;
        isValid = false;
      }

      if (!self.model.requestType) {
        self.model.isRequestTypeValid = true;
        isValid = false;
      }

      if (!self.model.store.id || !self.model.assetType.id || !self.model.requestType) {
        growl.error('Please select required fields');
        isValid = false;
      }

      return isValid;
    }

    var ValidatePRQuantity = function(item){
      var isValidItem = true;
      var prQty = parseInt(item.prQuantity);

      if(item.selectedUom){
        if(item.selectedUom.uomType== "Dispense")
        {
          item.prQty = prQty;
        }
        else if(item.selectedUom.uomType=="Stock")
        {
          item.prQty = prQty * (item.uom[2].conversion);
        }
        else if(item.selectedUom.uomType=="Purchase")
        {
          item.prQty = prQty * (item.uom[2].conversion* item.uom[1].conversion);
        }
      }

      var newMaxOrderQuantity = item.newMaxOrderQuantity + item.prMaxStoreQty;
      if (newMaxOrderQuantity > item.newMaxStoreQuantity) {
        newMaxOrderQuantity = item.newMaxStoreQuantity;
      }

      if(self.model.assetType.id != 2){
        CalculateStock(item, item.uom, item.selectedUom);
      }

      if (prQty > newMaxOrderQuantity) {
        growl.error('Quantity should be less than pending quantity');
        item.isPRQuantityValid = true;
        isValidItem = false;
      }

      if (!prQty) {
        growl.error('Please enter valid quantity');
        item.isPRQuantityValid = true;
        isValidItem = false;
      }

      return item, isValidItem;
    }

    var ValidateItems = function () {
      var isValidItem = true;
      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add item to save quotation enquiry');
        isValidItem = false;
      } else {
        angular.forEach(self.model.itemDetails, function (item, index) {
          // todo - for now qty validation added only for direct request
          if (self.model.prRequestTypeId == 1) {
            self.model.itemDetails[index], isValidItem = ValidatePRQuantity(self.model.itemDetails[index]);
          }

          if (self.model.assetType.id == 1) {
            if (!item.selectedUom.uomUnitId) {
              item.uomValid = true;
              growl.error('Please select UOM');
              isValidItem = false;
            }

            if (!item.paymentTerms) {
              item.isValidTerms = true;
              growl.error('Expected payment terms are required');
              isValidItem = false;
            }

            /*angular.forEach(item.expectedAccessories, function(accessory){
            	
            	
              if (!accessory.name || !accessory.details) {
            	  
            	  if($scope.assetId == 1){
             		 item.accessoryValid = true;	
             		 isValidItem = false;
             		 item.accessoryValid = false;
             	}
             	else
             		{
             		 item.accessoryValid = true;
                     growl.error('All expected accessories are required');
                     isValidItem = false;
             		}
            	  
               
              }
            });*/
          }
          else {
            if (!item.warrantyPeriod || !item.supportTerms || !item.paymentTerms || !item.technicalSpecification) {
              item.isValidTerms = true;
              growl.error('All expected terms are required');
              isValidItem = false;
            }

            angular.forEach(item.expectedAccessories, function(accessory){
              if (!accessory.name || !accessory.details) {
                item.accessoryValid = true;
                growl.error('All expected accessories are required');
                isValidItem = false;
              }
            });
          }
        });
      }

      return isValidItem;
    }

    var PopulateAssetCategoryDropdown = function () {
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetType.id+ '/'+$scope.unitId+ '/'+$scope.organizationId;

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

    var ValidateDate = function () {
      var isValid = true;
      if (self.model.fromDate > self.model.toDate) {
        growl.error('From date should be lesser than to date');
        isValid = false;
      }

      return isValid;
    }

    var GetIndentList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if (ValidateDate()) {
        var URI = CONSTANTS.GLOBAL.AGAINST_PR_SEARCH;

        var data = {
          "storeId": self.model.store.id,
          "assetTypeId": self.model.assetType.id,
          "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
          "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
          "prNo": self.model.prNumber,
          "screenId": self.model.screenId,
          "unitId":$scope.unitId,
          "organizationId":$scope.organizationId
        }
        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.indents = response.data;
            angular.forEach(self.model.indents, function (pr) {
              pr["isChecked"] = false;
              angular.forEach(pr.items, function (item) {
                item["isChecked"] = false;
              });
            });

            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            self.model.indents = [];
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
        if(self.model.search.itemName.length >= 3){    
          var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;
          
          var data = {
            "assetTypeId": self.model.assetType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
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
        }
      }
      else if(self.model.search.itemCode){
        if(self.model.search.itemCode.length >= 3){
           var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;
          
          var data = {
            "assetTypeId": self.model.assetType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
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
        }
      }
      else{
        self.model.items = [];
        self.model.pagination.data=[];
      }
    }

    var AddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if (self.model.SelectedItemsIds.length == 0) {
        growl.error('Please select items to add');
        return false;
      } 
      else {
      //   if(self.model.search.itemCode != undefined || self.model.search.itemName != undefined){
      //     self.model.SelectedItemsIds = 0;
      //     self.model.SelectedItemsIds = self.model.pagination.data.map(function (item) {
      //       return item.itemId;
      //     });
      // }
      // else{
     
      // }
        var URI = CONSTANTS.GLOBAL.DIRECT_PR_DETAILS+ "/"+$scope.organizationId + "/"+$scope.unitId;
        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id
        };
        var popup = angular.element('#addNewItemWasteStore');
        popup.modal('hide');

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.itemDetailsList.forEach(function (value) {
              value["expectedAccessories"] = [{"name": "", "details": ""}];
              var isFound = self.model.itemDetails.find(function (x) {
                return x.itemId == value.itemId;
              });

              if (value.maxOrderQuantity <= 0) {
                growl.error("Maxorder Quantity is less than 0 for an item.");
              }

              // get items which has valid maxOrderQuantity
              if (!isFound && (value.maxOrderQuantity > 0)) {
                self.model.itemDetails.push(value);
                value.prMaxStoreQty = 0;
                value.newQty = value.stockAvailable;
                if (self.model.assetType.id == 1) {
                  selectUOM(value);
                } else{
                  value.newMaxOrderQuantity = value.maxOrderQuantity;
                  value.newMaxStoreQuantity = value.maxStoreQuantity;
                }
              }
            });
            
            self.model.SelectedItemsIds = [];
            self.model.items = [];
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            // self.model.SelectedsItemsIds = [];
            growl.error(err.data.message);
          });
      }
    }

    var selectUOM = function (item) {
      item.uom = self.model.uomArray.concat(item.uom);
      item.selectedUom = item.selectedUom ? item.selectedUom : item.uom[0];
      item.uom[0].uomDetails = item.uom[0].uomType; 
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

    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    var SelectAllIndentItems = function (indent, items) {
      if (indent.isSelectedIndent) {
        items.map(function (x) {
          self.model.indentItemDetails.push(x);
          x.isSelectedItem = true;
          x.isChecked = true;
          indent.isChecked = true;
          return self.model.intendItemIds.push(x.itemId);
        });
      } else {
        self.model.intendItemIds = [];
        items.forEach(function (x) {
          x.isSelectedItem = false;
          x.isChecked = false;
        });
        indent.isChecked = false;
        items.map(function (x) {
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
        item.isChecked = true;
        indent.isChecked = true;
        itemIds.push(item.itemId);
        self.model.indentItemDetails.push(item);
      } else {
        item.isChecked = false;
        indent.isChecked = false;
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

    var updatePrItemDetails = function (prResponseData) {
      var newItemsHash = {};
      var itemDetailsHash = {};
      self.model.itemDetails.forEach(function (itemDetail) {
        itemDetailsHash[itemDetail.itemId] = itemDetail;
      });

      prResponseData.forEach(function (prItem) {
        var prNo = prItem.prNo;
        var prId1 = prItem.id;
        prItem.items.forEach(function (item) {
          var quotationDetails = [];
          var itemId = item.itemId;
          var prId = item.id;
          var prQuantity = Number(item.prQuantity);
          var prDate = moment(prItem.prDate).format('YYYY-MM-DD');
          var prObj = { id: prId, prNo: prNo, prQuantity: prQuantity, prDate: prDate};
          var prIdList = {prId: prId1, prDetailId: [prId]};
          if (itemDetailsHash.hasOwnProperty(itemId)) {
            // item found in already saved list
            var itemDetail = itemDetailsHash[itemId];
            if (itemDetail.prIds && itemDetail.prIds.length) {
              // prIds list is present
              if (itemDetail.prIds.indexOf(prId) == -1) {
                itemDetail.prDetailList.push(prObj);
                itemDetail.prDataList.push(prIdList);
                itemDetail.prIds.push(prId);
                // itemDetail.prQuantity = item.prQuantity
              }
            } else {
              // prIds list not present
              itemDetail.prDetailList = [prObj]
              itemDetail.prDataList = [prIdList]
              itemDetail.prIds = [prId];
            }

          } else if (newItemsHash.hasOwnProperty(itemId)) {
            // item found in new item hash
            var savedItem = newItemsHash[itemId];
            savedItem.prDetailList.push(prObj);
            savedItem.prDataList.push(prIdList);
            savedItem.prIds.push(prId);
          } else {
            // item not found in item hash
            var clonedItem = JSON.parse(JSON.stringify(item));
            delete clonedItem.id;
            delete clonedItem.prQuantity;
            if(item.prQuantity != 0){
              clonedItem.prDetailList = [prObj]
              clonedItem.prDataList = [prIdList]
              clonedItem.prIds = [prId];
              newItemsHash[itemId] = clonedItem;
              newItemsHash[itemId].prQuantity = prQuantity;
            } 
          }
        });
      });

      for (var itemId in newItemsHash) {
        newItemsHash[itemId]["expectedAccessories"] = [{"name": "", "details": ""}];
        if(newItemsHash[itemId].uom){
          newItemsHash[itemId]["selectedUom"] = newItemsHash[itemId].uom.find(function (x){
            return x.uomTypeId == newItemsHash[itemId].uomTypeId;
          })
          newItemsHash[itemId].uom = self.model.uomArray.concat(newItemsHash[itemId].uom);
          newItemsHash[itemId].uom[0].uomDetails = newItemsHash[itemId].uom[0].uomType; 
          if(newItemsHash[itemId].isThreeUOMApplicable == false){
            newItemsHash[itemId].uomListdata = newItemsHash[itemId].uom;
            newItemsHash[itemId].uom = [];
            angular.forEach(newItemsHash[itemId].uomListdata, function(data){
              if(data.uomType != 'Stock'){
                newItemsHash[itemId].uom.push(data);
              }
            });
          }
        }
      
        newItemsHash[itemId]["newPrQuantity"] = newItemsHash[itemId]["prQuantity"];
        self.model.itemDetails.push(newItemsHash[itemId]);
        console.log("PR Quantity : ",newItemsHash[itemId].prQuantity);
        console.log("Max Order Quantity : ",newItemsHash[itemId].maxOrderQuantity);
        console.log("Max Store Quantity : ",newItemsHash[itemId].maxStoreQuantity);

        newItemsHash[itemId].prQuantity=newItemsHash[itemId].prQuantity;
        newItemsHash[itemId].newMaxOrderQuantity=newItemsHash[itemId].maxOrderQuantity;
        newItemsHash[itemId].newMaxStoreQuantity=newItemsHash[itemId].maxStoreQuantity;

        console.log("PR Quantity : ",newItemsHash[itemId].prQuantity);
        console.log("New Max Order Quantity : ",newItemsHash[itemId].newMaxOrderQuantity);
        console.log("New Max Store Quantity : ",newItemsHash[itemId].newMaxStoreQuantity);

        if(newItemsHash[itemId].isThreeUOMApplicable == false){
          if(newItemsHash[itemId].uomTypeId==1)
          {
            newItemsHash[itemId].newMaxOrderQuantity = parseInt(newItemsHash[itemId].maxOrderQuantity / (newItemsHash[itemId].uomListdata[1].conversion *newItemsHash[itemId].uomListdata[2].conversion * newItemsHash[itemId].uomListdata[3].conversion));
            newItemsHash[itemId].newMaxStoreQuantity = parseInt(newItemsHash[itemId].maxStoreQuantity / (newItemsHash[itemId].uomListdata[1].conversion * newItemsHash[itemId].uomListdata[2].conversion * newItemsHash[itemId].uomListdata[3].conversion));
            newItemsHash[itemId].receivedQtyCon=(newItemsHash[itemId].prQuantity +"*"+ newItemsHash[itemId].uomListdata[1].conversion);
            newItemsHash[itemId].receivedQtyCalculation = (newItemsHash[itemId].prQuantity * newItemsHash[itemId].uomListdata[1].conversion);
            newItemsHash[itemId].uomUnitData = newItemsHash[itemId].uomListdata[3].uomUnit;
            // newItemsHash[itemId].prQuantity = parseInt(newItemsHash[itemId].prQuantity / (newItemsHash[itemId].uom[1].conversion * newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
          }
          else if(newItemsHash[itemId].uomTypeId==2)
          {
            newItemsHash[itemId].newMaxOrderQuantity = parseInt(newItemsHash[itemId].maxOrderQuantity / (newItemsHash[itemId].uomListdata[2].conversion * newItemsHash[itemId].uomListdata[3].conversion));
            newItemsHash[itemId].newMaxStoreQuantity = parseInt(newItemsHash[itemId].maxStoreQuantity / (newItemsHash[itemId].uomListdata[2].conversion * newItemsHash[itemId].uomListdata[3].conversion));
            // newItemsHash[itemId].prQuantity = parseInt(newItemsHash[itemId].prQuantity / (newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
          }
          else if(newItemsHash[itemId].uomTypeId==3)
          {
            newItemsHash[itemId].newMaxOrderQuantity = parseInt(newItemsHash[itemId].maxOrderQuantity / (newItemsHash[itemId].uomListdata[3].conversion));
            newItemsHash[itemId].newMaxStoreQuantity = parseInt(newItemsHash[itemId].maxStoreQuantity / (newItemsHash[itemId].uomListdata[3].conversion));
            newItemsHash[itemId].receivedQtyCon=(newItemsHash[itemId].prQuantity);
            newItemsHash[itemId].receivedQtyCalculation = (newItemsHash[itemId].prQuantity);
            newItemsHash[itemId].uomUnitData = newItemsHash[itemId].uomListdata[3].uomUnit;
            // newItemsHash[itemId].prQuantity = parseInt(newItemsHash[itemId].prQuantity / (newItemsHash[itemId].uom[3].conversion));
          }
        }
        else{
          if(newItemsHash[itemId].uomTypeId==1)
          {
            newItemsHash[itemId].newMaxOrderQuantity = parseInt(newItemsHash[itemId].maxOrderQuantity / (newItemsHash[itemId].uom[1].conversion *newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
            newItemsHash[itemId].newMaxStoreQuantity = parseInt(newItemsHash[itemId].maxStoreQuantity / (newItemsHash[itemId].uom[1].conversion * newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
            newItemsHash[itemId].receivedQtyCon = (newItemsHash[itemId].prQuantity +"*"+ newItemsHash[itemId].uom[1].conversion+"*"+ newItemsHash[itemId].uom[2].conversion);
            newItemsHash[itemId].receivedQtyCalculation = (newItemsHash[itemId].prQuantity * newItemsHash[itemId].uom[1].conversion * newItemsHash[itemId].uom[2].conversion);
            newItemsHash[itemId].uomUnitData = newItemsHash[itemId].uom[3].uomUnit;
            // newItemsHash[itemId].prQuantity = parseInt(newItemsHash[itemId].prQuantity / (newItemsHash[itemId].uom[1].conversion * newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
          }
          else if(newItemsHash[itemId].uomTypeId==2)
          {
            newItemsHash[itemId].newMaxOrderQuantity = parseInt(newItemsHash[itemId].maxOrderQuantity / (newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
            newItemsHash[itemId].newMaxStoreQuantity = parseInt(newItemsHash[itemId].maxStoreQuantity / (newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
            newItemsHash[itemId].receivedQtyCon=(newItemsHash[itemId].prQuantity +"*"+ newItemsHash[itemId].uom[2].conversion);
            newItemsHash[itemId].receivedQtyCalculation = (newItemsHash[itemId].prQuantity * newItemsHash[itemId].uom[2].conversion);
            newItemsHash[itemId].uomUnitData = newItemsHash[itemId].uom[3].uomUnit;
            // newItemsHash[itemId].prQuantity = parseInt(newItemsHash[itemId].prQuantity / (newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[3].conversion));
          }
          else if(newItemsHash[itemId].uomTypeId==3)
          {
            newItemsHash[itemId].newMaxOrderQuantity = parseInt(newItemsHash[itemId].maxOrderQuantity / (newItemsHash[itemId].uom[3].conversion));
            newItemsHash[itemId].newMaxStoreQuantity = parseInt(newItemsHash[itemId].maxStoreQuantity / (newItemsHash[itemId].uom[3].conversion));
            newItemsHash[itemId].receivedQtyCon=(newItemsHash[itemId].prQuantity);
            newItemsHash[itemId].receivedQtyCalculation = (newItemsHash[itemId].prQuantity);
            newItemsHash[itemId].uomUnitData = newItemsHash[itemId].uom[3].uomUnit;
            // newItemsHash[itemId].prQuantity = parseInt(newItemsHash[itemId].prQuantity / (newItemsHash[itemId].uom[3].conversion));
          }
        }

        if(self.model.assetType.id != 2){
          var prQty = parseInt(newItemsHash[itemId].prQuantity);
          if(newItemsHash[itemId].selectedUom.uomType== "Dispense")
          {
            newItemsHash[itemId].prQty = prQty;
          }
          else if(newItemsHash[itemId].selectedUom.uomType=="Stock")
          {
            newItemsHash[itemId].prQty = prQty * (newItemsHash[itemId].uom[2].conversion);
          }
          else if(newItemsHash[itemId].selectedUom.uomType=="Purchase")
          {
            newItemsHash[itemId].prQty = prQty * (newItemsHash[itemId].uom[2].conversion * newItemsHash[itemId].uom[1].conversion);
          }
        }

       // CalculateStock(newItemsHash[itemId], newItemsHash[itemId].uom, newItemsHash[itemId].selectedUom);

        newItemsHash[itemId].prMaxStoreQty = 0;
        if (self.model.assetType.id == 2) {
          newItemsHash[itemId].newMaxOrderQuantity = newItemsHash[itemId].maxOrderQuantity;
          newItemsHash[itemId].newMaxStoreQuantity = newItemsHash[itemId].maxStoreQuantity;
        }

        console.log("Final PR Quantity : ",newItemsHash[itemId].prQuantity);
        console.log("Final New Max Order Quantity : ",newItemsHash[itemId].newMaxOrderQuantity);
        console.log("Final New Max Store Quantity : ",newItemsHash[itemId].newMaxStoreQuantity);
      }
    }

    var SelectedItems = function () {
      self.model.prDetails = [];
      var itemIdArray = [];
      angular.forEach(self.model.indents, function (pr) {
        var prData = null;
        if (pr.isChecked) {
          prData = {
            "prId": pr.id,
            "itemIds": []
          }
        }

        angular.forEach(pr.items, function (item) {
          if (item.isChecked) {
            prData.itemIds.push(item.id);
          }
        });

        if (pr.isChecked && prData.itemIds.length > 0)
          self.model.prDetails.push(prData);
      });
    }

    var AddIndentItems = function () {
      if (!self.model.intendItemIds.length) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.GLOBAL.AGAINST_PR_DETAILS + "/"+$scope.organizationId + "/"+$scope.unitId;

        SelectedItems();

        var data = self.model.prDetails;

        var popup = angular.element('#againstPRmodal');
        popup.modal('hide');

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            var responseData = response.data;
            var purchaseRequestListResponse = responseData.purchaseRequestList;
            updatePrItemDetails(purchaseRequestListResponse);
            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];
            self.model.items = [];
            self.model.indents = [];
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

    var SaveQuotationEnquiry = function () {
    	$scope.assetId=self.model.assetType.id;
      if (ValidateItems()) {
        if (!self.model.quotationId) {
          CreateQuotationEnquiry();
        } else {
          UpdateQuotationEnquiry();
        }
      }
    }

    var CreateQuotationEnquiry = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.SAVE_API;

      var data = {
        "storeId": self.model.store.id,
        "assetTypeId": self.model.assetType.id,
        "requestTypeId": self.model.requestType,
        "remark": self.model.remark,
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId": $scope.userId
      };  

      angular.forEach(self.model.itemDetails, function (item) {
        // var list = [];
        // if (self.model.assetType.id == 2) {
        //   if (item.expectedAccessories.length == 1 && !item.expectedAccessories[0].name && !item.expectedAccessories[0].details) {
        //     list = [];
        //   }
        // }

        var prItem = {
          "itemId": item.itemId,
          "quantity": item.prQuantity,
          "statusId": setStatus('New'),
          "prIds": item.prDataList,
          "expectedAccessories": item.expectedAccessories,
          "warrantyPeriod": item.warrantyPeriod,
          "supportTerms": item.supportTerms,
          "paymentTerms": item.paymentTerms,
          "technicalSpecification": item.technicalSpecification,
          "qrPendingQuantity" : item.newMaxOrderQuantity
        }

        if (self.model.assetType.id == 1) {
          prItem.uomTypeId = item.selectedUom.uomTypeId;
          prItem.uomUnitId = item.selectedUom.uomUnitId;
        }

        data.items.push(prItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToQuotationEnquiry();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateQuotationEnquiry = function () {
      var URI = CONSTANTS.PROCUREMENT.QUOTATION.QUOTATION_ENQUIRY.SAVE_API + self.model.quotationId;

      var data = {
        "id": self.model.quotationId,
        "remark": self.model.remark,
        "assetTypeId": self.model.assetType.id,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId": $scope.userId
      };
      angular.forEach(self.model.itemDetails, function (item) {
        var expectedAccessories = [];
        angular.forEach(item.expectedAccessories, function (accessoryItem) {
          var item = {
            "id": accessoryItem.id,
            "name": accessoryItem.name,
            "details": accessoryItem.details
          }

          expectedAccessories.push(item);
        });
        
        var prItem = {
          "id": item.id,
          "itemId": item.itemId,
          "quantity": parseInt(item.prQuantity),
          "prId": item.prIds,
          "expectedAccessories": expectedAccessories,
          "warrantyPeriod": item.warrantyPeriod,
          "supportTerms": item.supportTerms,
          "paymentTerms": item.paymentTerms,
          "technicalSpecification": item.technicalSpecification
        }

        if (!item.id) {
          prItem.statusId = setStatus('New');
        }

        if (self.model.assetType.id == 1) {
          prItem.uomTypeId = item.selectedUom.uomTypeId;
          prItem.uomUnitId = item.selectedUom.uomUnitId;
        }

        data.items.push(prItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToQuotationEnquiry();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var NavigateToQuotationEnquiry = function () {
      $state.go('quotationEnquiry');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addItemquotationEnquiry", {
      url: "/addItemquotationEnquiry",
      templateUrl: 'views/procurement/quotation/add-item-quotation-enquiry.html',
      controller: "AddNewQuotation.Controller",
      controllerAs: "vm",
      title: 'Quotation Enquiry'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewQuotation.Controller", addNewQuotationController);
})();