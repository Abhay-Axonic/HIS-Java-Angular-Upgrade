(function () {
  "use strict";

  function addNewLoanOutReceiveController($scope, $rootScope, $http, $filter, $state, CONSTANTS, GenericService, commonDetailService, CurrencyService, rejectReasonService, StatusService, growl,$cookies) {
    var self = this;

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;

      self.model = {
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
        dcDateOptions: {
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
          "store": "Select Store"
        }],
        toStores: [{
          "store": "Select Store"
        }],
        SelectedItemsIds: [],
        itemDetails: [],
        loanInOutId: null,
        intendItemIds: [],
        indentItemDetails: [],
        isQuantity: [],
        dateOpen: [],
        expiryDate: [],
        searchItem: {
          loanOutItem: undefined
        },
        assetTypes: [{
          "type": "Select Asset Type"
        }]
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
      self.model.OpenExpiryDate = OpenExpiryDate;
      self.model.OpenDCDate = OpenDCDate;
      self.model.OpenfromDate = OpenfromDate;
      self.model.OpentoDate = OpentoDate;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SaveLoanInOut = SaveLoanInOut;
      self.model.NavigateToLoanOutReceive = NavigateToLoanOutReceive;
      self.model.GetItems = GetItems;
      self.model.GetIndentList = GetIndentList;
      self.model.Expand = Expand;
      self.model.SelectAllIndentItems = SelectAllIndentItems;
      self.model.SelectIndentItem = SelectIndentItem;
      self.model.SetDecimal = SetDecimal;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.uomInfoOnClick = uomInfoOnClick;
      self.model.uomModalOnClick = uomModalOnClick;
      self.model.SearchLoanOut = SearchLoanOut;
      self.model.AddLoanOutItems = AddLoanOutItems;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
      self.model.ValidateQuantity =ValidateQuantity;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeStore = ChangeStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;

      PopulateDropdown();
    }

    var OpenExpiryDate = function (idx) {
      self.model.dateOpen[idx] = true;
    }

    var OpenDCDate = function () {
      self.model.dcDateOpened = true;
    }

    var OpenfromDate = function () {
      self.model.fromDateOpened = true;
    }

    var OpentoDate = function () {
      self.model.toDateOpened = true;
    }

    var ValidConversionFactor = function(data){
      var isValid = true;
      data.find(function(obj){
        if (!parseInt(obj.conversion)) {
          growl.error('Conversion factor should valid');
          isValid = false;
        }
      });

      return isValid;
    }

    var uomInfoOnClick = function (batchItem, index) {
      self.model.selectedUOMList = [];
      self.model.selectedUOMUnit = [];
      self.model.selectedItemIndex = index;
      if (batchItem)
        batchItem.uom.forEach(function (item, index) {
          var data = { 'uomUnit': item.uomUnit, 'uomUnitId': item.uomUnitId };
          self.model.selectedUOMUnit.push(data);

          var uomUnitId = null, conversion = 0;
          if (!batchItem.poUom || batchItem.poUom.length == 0) {
            uomUnitId = data;
            conversion = item.conversion;
          }
          else {
            uomUnitId = batchItem.poUom[index].uomUnitId;
            conversion = batchItem.poUom[index].conversion;
          }

          self.model.selectedUOMList.push({
            uomType: item.uomType,
            uomUnitId: uomUnitId,
            conversion: conversion,
            ipUom: item.ipUom,
            opUom: item.opUom,
            storeUom: item.storeUom,
            uomTypeId: item.uomTypeId
          });
        });
    }

    var uomModalOnClick = function () {
      self.model.itemDetails[self.model.selectedItemIndex].poUom = [];
      var modal_list = self.model.selectedUOMList;

      if (ValidConversionFactor(self.model.selectedUOMList)) {
        if (modal_list) {
          modal_list.forEach(function (modal, index) {
            self.model.itemDetails[self.model.selectedItemIndex].poUom.push({
              uomUnitId: modal.uomUnitId,
              conversion: parseInt(modal.conversion),
              ipUom: modal.ipUom,
              opUom: modal.opUom,
              storeUom: modal.storeUom,
              uomTypeId: modal.uomTypeId
            });
          });

          var popup = angular.element('#selectUOM_modal');
          popup.modal('hide');
        }
      }
    }

    var setStatus = function (x){
      var status = self.model.statusList.find(function(obj){
        return obj.status == x;
      });
      
      return status.id;
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
       // self.model.store = self.model.stores[0];
        self.model.assetType = self.model.assetTypes[0];

        var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.DROPDOWN_API+ '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
            $rootScope.stopLoader();
            /*for(var i=0;self.model.stores.length;i++)
            {
              if(self.model.stores[i].id==$cookies.getObject('selectedTopStore').storeId)
              {
                self.model.store = self.model.stores[i];
              }
            }*/
            
            // start code for resolved HIS-6625 Loan Out Receive
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            for(var i=0; i < self.model.stores.length ;i++)
            {
              if(self.model.stores[i].id==(selectedTopStore != null ? selectedTopStore.storeId : undefined))
              {
                self.model.store = self.model.stores[i];
                break;
              }
            }
            // end code for resolved HIS-6625 Loan Out Receive
            
            var loanInOutId = commonDetailService.getDataId();
            if (!!loanInOutId) {
              commonDetailService.setDataId(null);
              GetLoanIn(loanInOutId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var SearchLoanOut = function() {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.SEARCH_LOAN_OUT;
      var data ={
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "loanInNo": self.model.loanOutNo ? self.model.loanOutNo : undefined,
        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
        "assetTypeId": self.model.assetType.id,
        "unitId":$scope.unitId
        
      }
      
      $rootScope.startLoaderPopup();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          self.model.loanOutList = response.data;
            angular.forEach(self.model.loanOutList, function (loanIn) {
              loanIn["isChecked"] = false;
              loanIn.isSelectedIndent = false;
              angular.forEach(loanIn.items, function (item) {
                item["isChecked"] = false;
              });
            });
            $rootScope.stopLoaderPopup();
        },
        function (err) {
          growl.error(err.data.message);
          $rootScope.stopLoaderPopup();
        });
    }

    var getSelectedIds = function () {
      var selecteItemIds = self.model.indentItemDetails.map(function (item) {
        return item.id;
      });
      return _.uniq(selecteItemIds);
    }

    var SelectedItems = function () {
      var po = self.model.isSelectedIndent;
      if (po) {
        self.model.poDetails = {};
        var poData = {
          "loanOutId": []
        }
        angular.forEach(po.items, function (item) {
          if (item.isChecked) {
            poData.loanOutId.push(item.id);
          } else {
            var index = poData.loanOutId.indexOf(item.id);
            if (index > -1) {
              poData.loanOutId.splice(index, 1);
            }
          }
        });
        self.model.poDetails = poData;
        self.model.poDetails.assetTypeId = self.model.assetType.id;
        // if (po.isChecked && poData.loanOutId.length > 0) {
        //     self.model.poDetails = poData;
        // } 
      }

      // return self.model.poDetails;
    }

    var AddLoanOutItems = function () {    
      SelectedItems();
      if (!self.model.intendItemIds.length  || !self.model.poDetails.loanOutId.length) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.ADD_LOAN_OUT_ITEM;

        // var selectedIds = getSelectedIds();

        var data = self.model.poDetails;
        data.unitId = $scope.unitId;
        data.organizationId = $scope.organizationId;
        data.userId = $scope.userId;
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            self.model.loanOutId = response.data.id;
            self.model.dcNumber = response.data.dcNo;
            self.model.dcDate = moment(response.data.dcDate).format('DD/MM/YYYY');
            self.model.loanOutNumber = response.data.loanOutNo;
            response.data.items.forEach(function(item) {
              var isFound = self.model.itemDetails.find(function(x){
                return x.itemId == item.itemId;
              });
              if (!isFound) {
                // item.receiveQuantity = 0;
                self.model.itemDetails.push(item);
              }
            });

            self.model.indentItemDetails = [];
            self.model.intendItemIds = [];

            var popup = angular.element('#getloanOutModal');
            popup.modal('hide');
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    // select indent row
    var SelectAllIndentItems = function () {
      var selecteItem = self.model.isSelectedIndent;
      self.model.intendItemIds = [];
      angular.forEach(self.model.loanOutList, function(po){
        if (po.id !== selecteItem.id) {
          po.items.forEach(function (x) {
            x.isSelectedItem = false;
            x.isChecked = false;
            po.isChecked = false;
          });
        } else {
          po.items.forEach(function (x) {
            self.model.indentItemDetails.push(x);
            x.isSelectedItem = true;
            x.isChecked = true;
            po.isChecked = true;
            self.model.intendItemIds.push(x.itemId);
          });
        }
      });
    }

    var SelectIndentItem = function (item, indent) {
      var itemIds = self.model.intendItemIds;
      if(self.model.isSelectedIndent != indent){
        angular.forEach(self.model.loanOutList, function(po){
          if (po.id !== indent.id) {
            po.items.forEach(function (x) {
              x.isSelectedItem = false;
              x.isChecked = false;
              po.isChecked = false;
            });
          }
        });
      }
      if (item.isSelectedItem) {
        item.isChecked = true;
        indent.isChecked = true;
        self.model.isSelectedIndent = indent;
        itemIds.push(item.itemId);
        self.model.indentItemDetails.push(item);
      } else {
        item.isChecked = false;
        indent.isChecked = false;
        var index = itemIds.indexOf(item.itemId);
        if (index > -1) {
          itemIds.splice(index, 1);
        }

        var itemIndex = self.model.indentItemDetails.indexOf(item);
        if (itemIndex > -1) {
          self.model.indentItemDetails.splice(itemIndex, 1);
        }
      }

      if (indent.items.every(isSelectedItem)) {
         self.model.isSelectedIndent = indent;
      } else {
        //  self.model.isSelectedIndent = null;
      }
    }

    var GetItems = function (){
      if (ValidateDetails()) {
        var popup = angular.element('#getloanOutModal');
        popup.modal('show');

        self.model.loanOutList = [];
        self.model.fromDate = new Date();
        self.model.toDate = new Date();
        self.model.loanOutNo = undefined;
        self.model.searchItem.loanOutItem = undefined;
      }
    }

    var GetLoanIn = function (loanInOutId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.DETAILS_API + $scope.unitId + '/' + loanInOutId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.loanInOutId = response.data.id;

          angular.forEach(response.data.items, function (item) {
            item.expiryDate = new Date(item.expiryDate);
          });

          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
          });

          self.model.assetType = self.model.assetTypes.find(function (item) {
            return response.data.assetTypeId == item.id;
          });

          self.model.loanOutNumber = response.data.loanOutReceiveNo;
          self.model.remark = response.data.remark;
          self.model.vendorName = response.data.vendor;
          self.model.vendorId = response.data.vendorId;
          self.model.dcNumber = response.data.dcNo;
          self.model.dcDate = moment(response.data.dcDate).format('DD/MM/YYYY');
          self.model.loanInNumber = new Date(response.data.loanInNumber);
          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {
            var poUom = [];
            angular.forEach(item.poUom, function (uom) {
              poUom.push({
                id: uom.id,
                uomUnitId: { uomUnit: uom.uomUnit, uomUnitId: uom.uomUnitId },
                conversion: parseInt(uom.conversion),
                ipUom: uom.ipUom,
                opUom: uom.opUom,
                storeUom: uom.storeUom,
                uomTypeId: uom.uomTypeId
              });
            });

            var indentItem = {
              "id": item.id,
              "loanInDetailId": item.loanInDetailId,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "uom": item.uom,
              "uomUnit": item.uomUnit,
              "batchNo": item.batchNo,
              "batchId": item.batchId,
              "expiryDate": item.expiryDate,
              "receiveQuantity": item.receiveQuantity,
              "cop": item.cop,
              "totalAmount": item.totalAmount,
              "uomTypeId": item.uomTypeId,
              "uomUnitId": item.uomUnitId,
              "remark": item.remark,
              "loanOutQuantity": item.loanOutQuantity
            }

            self.model.itemDetails.push(indentItem);
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

      if (!self.model.vendorId) {
        isValid = false;
        self.model.isVendorId = true;
      }

      if (!self.model.assetType.id) {
        isValid = false;
        self.model.isAssetTypeId = true;
      }

      if (!self.model.store.id || !self.model.vendorId || !self.model.assetType.id) {
        isValid = false;
        growl.error('Please select required field');
      }

      return isValid;
    }

    var ValidateItems = function (){
      var isValidItem = true;

      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add items');
        isValidItem = false;
      } else {
        for (var i = 0; i < self.model.itemDetails.length; i++) {
          if(!ValidateQuantity(self.model.itemDetails[i])){
            isValid = false;
          }
        }
      }

      return isValidItem;
    }

    var SetDecimal = function (item) {
      if (isNaN(item.receiveQuantity) || !item.receiveQuantity) {
        item.receiveQuantity = 0;
      } else {
        item.receiveQuantity = parseFloat(item.receiveQuantity).toFixed(2);
      }
    }

    var PopulateAssetCategoryDropdown = function () {
      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + self.model.assetType.id;

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

      self.model.fromStore = self.model.fromStores[0];
      self.model.toStore = self.model.toStores[0];

      self.model.fromStores.splice(1);
      self.model.toStores.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.fromStores = self.model.fromStores.concat(response.data.fromStore);
          self.model.toStores = self.model.toStores.concat(response.data.toStore);
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
      }else if (fromStore == toStore) {
        self.model.indents = [];
        growl.error('From store and to store should not be same');
        return false;
      } else{
        var URI = CONSTANTS.GLOBAL.GET_INDENTS_API + $scope.unitId + '/' + fromStore + '/' + toStore;

        GenericService.serviceAction("POST", URI, {}).then(
          function (response) {
            self.model.indents = response.data;
            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var Expand = function (item) {
      item.Expanded = !item.Expanded;
    }

    var GetItemList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

      var data = {
        "assetCategoryId": self.model.assetCategory.id,
        "productCategoryId": self.model.productCategory.id,
        "storeId": self.model.store.id,
        "loanoutId": self.model.loanOutNo
        
      }

      self.model.items = [];
      $rootScope.startLoaderPopup();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          self.model.items = response.data;
          $rootScope.stopLoaderPopup();
        },
        function (err) {
          $rootScope.stopLoaderPopup();
          growl.error(err.data.message);
        });
    }

    var AddItems = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if (!self.model.SelectedItemsIds.length) {
        growl.error('Please select items to add');
        return false;
      } else {
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id
        };

        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function(item) {
              var isFound = self.model.itemDetails.find(function(val){
                return val.itemId == item.itemId;
              });
              if (!isFound) {
                self.model.itemDetails.push(item);
                selectUOM(item);
              }
            });
            self.model.SelectedItemsIds = [];
            self.model.items = [];

            var popup = angular.element('#addNewItemWasteStore');
            popup.modal('hide');

            $rootScope.stopLoader();
          },
          function (err) {
            $rootScope.stopLoader();
            growl.error(err.data.message);
          });
      }
    }

    var selectUOM = function (item) {
      item.selectedUom = item.uom[0];
    }

    // Against Indent Item Selection
    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    var getSelectedItems = function(){
      var hash = {};
      self.model.indentItemDetails.forEach(function(item) {
        var itemId = item.itemId;
        var indentId = item.id;
        if(hash.hasOwnProperty(itemId)){
          hash[itemId][indentId] = { pendingQuantity: item.pendingQuantity }
        }else {
          hash[itemId] = {};
          hash[itemId][indentId] = { pendingQuantity: item.pendingQuantity }
        }
      });
      return hash;
    }

    var isSelected = function (item) {
      return item.isSelected;
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItemsIds = self.model.items.map(function (item) {
          item.isSelected = true;
          return item.itemId;
        });
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

    var SaveLoanInOut = function () {
      if (ValidateItems()) {
        if (!self.model.loanInOutId) {
          CreateLoanInOut();
        } else {
          UpdateLoanInOut();
        }
      }
    }

    var CreateLoanInOut = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.SAVE_API;

      var data = {
        "loanoutId": self.model.loanOutId,
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "assetTypeId": self.model.assetType.id,
        "totalAmount": self.model.totalAmount,
        "remark": self.model.remark,
        "statusId": setStatus("New"),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemDetails, function (item) {
        var loanItem = {
          "loanOutDetailId": item.id,
          "receiveQuantity": parseInt(item.receiveQuantity),
          "totalAmount": item.totalAmount,
          "remark": item.remark,
          "batchId": item.batchId
        }

        if (self.model.assetType.id == 1) {
          loanItem.uomUnitId = item.uomUnitId;
          loanItem.uomTypeId = item.uomTypeId;
        }

        data.items.push(loanItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
          NavigateToLoanOutReceive();
        },
        function (err) {
          $rootScope.stopLoader();
          if(err.data.summary ==' Please configure prefix number ')
          {
           growl.error(err.data.summary);
          }
          else
          {
            growl.error(err.data.message);  
          }
        });
    }

    var UpdateLoanInOut = function () {
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_OUT_RECEIVE.DETAILS_API + self.model.loanInOutId;

      var data = {
        "totalAmount": self.model.totalAmount,
        "remark": self.model.remark,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemDetails, function (item) {
        var loanItem = {
          "id": item.id,
          "batchId": item.batchId,
          "receiveQuantity": parseInt(item.receiveQuantity),
          "remark": item.remark,
          "totalAmount": item.totalAmount
        }

        if (self.model.assetType.id == 1) {
          loanItem.uomUnitId = item.uomUnitId;
          loanItem.uomTypeId = item.uomTypeId;
        }

        data.items.push(loanItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToLoanOutReceive();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    function SearchVendorNames(search) {
      if (search.length < 1) return;

      if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
        self.model.prevSearch = search;
        var URI = CONSTANTS.GLOBAL.VENDOR_SEARCH_API + search + "/"+$scope.unitId;
        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
        .then(function (response) {
          if (!!response.data && response.data.length > 0) {
            self.model.vendorNames = response.data;
            $rootScope.stopLoader();
            return $filter('filter')(self.model.vendorNames, {
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
        if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].itemFound != undefined && !self.model.vendorNames[0].itemFound) {
          return nameNotFound(search);
        } else {
          return $filter('filter')(self.model.vendorNames, {
            $: search
          });
        }
      }
    }

    function nameNotFound(search) {
      var item = {
        "itemFound": false,
        "detail": "Vendor not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName (vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }

    var NavigateToLoanOutReceive = function () {
      $state.go('loanOutReceive');
    }

    var ValidateQuantity = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.receiveQuantity))) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error("Please Enter Quantity");
      } else if (parseInt(item.receiveQuantity) == 0) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Quantity cannot be zero');
      } else if (parseInt(item.receiveQuantity) > item.loanOutQuantity) {
        item.isQuantityValid = true;
        isValid = false;
        growl.error('Receive quantity is exceeding the loan out quantity');
      } else {
        item.receiveQuantity = parseInt(item.receiveQuantity);
        isValid = true;
      }

      return isValid;
    }

    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeStore = function (store) {
      if (!!self.model.itemDetails.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        $("#storeChange").modal()
      }
    }

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      self.model.itemDetails = [];
      ModalDismiss('storeChange');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewLoanOutReceive", {
      url: '/addNewLoanOutReceive',
      templateUrl: 'views/procurement/consignment/add-new-loan-out-receive.html',
      controller: "AddNewLoanOutReceive.Controller",
      controllerAs: "vm",
      title: 'Loan Out Receive'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewLoanOutReceive.Controller", addNewLoanOutReceiveController);
})();