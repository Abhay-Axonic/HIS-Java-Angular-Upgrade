(function () {
  "use strict";

  function addNewLoanInController($scope, $rootScope, $http, $filter, $state, CONSTANTS, GenericService,PaginationService, commonDetailService, CurrencyService, rejectReasonService, StatusService, growl,$cookies,stopTransactionService) {
    var self = this;
    var getNextMonth = new Date(new Date().getFullYear(), new Date().getMonth()+1, 1);

    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;
    $scope.storeId = cookieObject.storeId; 
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
        dcDate: new Date(),
        dcDateOptions: {
          minDate: new Date(),
          formatYear: 'yyyy',
          showWeeks: false
        },
        expiryDateOptions: {
          minDate: getNextMonth,
          minMode: "month"
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
        loanInId: null,
        indentItemDetails: [],
        isQuantity: [],
        isCop: [],
        isBatchNumber: [],
        isExpiryDate: [],
        dateOpen: [],
        expiryDate: [],
        searchItem: {
          itemName: undefined,
          itemCode: undefined
        },
        assetTypes: [{
          "type": "Select Asset Type"
        }]
      };

      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      });

      rejectReasonService.GetReason().then(function (reason) {
        self.model.rejectReasons = reason;
      });

      StatusService.GetStatus().then(function (status) {
        self.model.statusList = status;
      });

      self.model.PopulateAssetCategoryDropdown = PopulateAssetCategoryDropdown;
      self.model.PopulateProductCategory = PopulateProductCategory;
      self.model.GetItemList = GetItemList;
      self.model.OpenExpiryDate = OpenExpiryDate;
      self.model.OpenDCDate = OpenDCDate;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.AddItems = AddItems;
      self.model.RemoveItem = RemoveItem;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SaveLoanInRequest = SaveLoanInRequest;
      self.model.NavigateToLoanIn = NavigateToLoanIn;
      self.model.GetItems = GetItems;
      self.model.SetDecimal = SetDecimal;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.AddVendorName = AddVendorName;
      self.model.uomInfoOnClick = uomInfoOnClick;
      self.model.uomModalOnClick = uomModalOnClick;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];

      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;
      self.model.SetOldValueAssetType = SetOldValueAssetType;
      self.model.ChangeFromAssetType = ChangeFromAssetType;
      self.model.ConfirmAssetTypePopup = ConfirmAssetTypePopup;
      self.model.OpenPopup = OpenPopup;
      self.model.ClosePopup = ClosePopup;
      self.model.ConfirmAdd = ConfirmAdd;

      PopulateDropdown();
    }

    var OpenExpiryDate = function (idx) {
      self.model.dateOpen[idx] = true;
    }

    var OpenDCDate = function () {
      self.model.dcDateOpened = true;
    }

    var ValidConversionFactor = function (data) {
      var isValid = true;
      data.find(function (obj) {
        if (!parseInt(obj.conversion)) {
          growl.error('Conversion factor should be valid');
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
          var data = {
            'uomUnit': item.uomUnit,
            'uomUnitId': item.uomUnitId
          };
          self.model.selectedUOMUnit.push(data);

          var uomUnitId = null,
            conversion = 0;
          if (!batchItem.poUom || !batchItem.poUom.length) {
            uomUnitId = data;
            conversion = item.conversion;
          } else {
            uomUnitId = batchItem.poUom[index].uomUnitId;
            conversion = batchItem.poUom[index].conversion;
          }

          self.model.selectedUOMList.push({
            id: item.id,
            uomType: item.uomType,
            uomUnitId: uomUnitId,
            conversion: conversion,
            ipUom: item.ipUom,
            opUom: item.opUom,
            storeUom: item.storeUom,
            uomTypeId: item.uomTypeId,
            uomUnit: item.uomUnit,
            uomDetails: item.uomDetails
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
              id: modal.id,
              uomType: modal.uomType,
              uomUnitId: modal.uomUnitId,
              conversion: parseInt(modal.conversion),
              ipUom: modal.ipUom,
              opUom: modal.opUom,
              storeUom: modal.storeUom,
              uomTypeId: modal.uomTypeId,
              uomUnit: modal.uomUnit,
              uomDetails: modal.uomDetails
            });
          });

          var popup = angular.element('#selectUOM_modal');
          popup.modal('hide');
        }
      }
    }

    var setStatus = function (value) {
      var status = self.model.statusList.find(function (obj) {
        return obj.status == value;
      });

      return status.id;
    }

    var PopulateDropdown = function () {
      try {
        $rootScope.startLoader();
        //self.model.store = self.model.stores[0];
        self.model.assetType = self.model.assetTypes[0];


        var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            self.model.stores = self.model.stores.concat(response.data.store);
            self.model.assetTypes = self.model.assetTypes.concat(response.data.assetType);
            var loanInId = commonDetailService.getDataId();
            $rootScope.stopLoader();
            var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
            for(var i=0;self.model.stores.length > i;i++)
            {
              if(self.model.stores[i].id==(selectedTopStore != null ? selectedTopStore.storeId : undefined))
              {
                self.model.store = self.model.stores[i];
              }
            }
            if (!!loanInId) {
              commonDetailService.setDataId(null);
              GetLoanIn(loanInId);
            }
          },
          function (err) {
            $rootScope.stopLoader();
          });
      } catch (error) {
        alert(error);
      }
    }

    var GetItems = function () {
      if (ValidateDetails()) {
        var popup = angular.element('#addNewItemWasteStore');
        popup.modal('show');
        PopulateAssetCategoryDropdown();
        self.model.items = [];
        self.model.pagination.data=[];
        self.model.itemCode = undefined;
        self.model.itemName = undefined;
      }
    }

    var GetLoanIn = function (loanInId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.DETAILS_API + $scope.unitId + "/"  + loanInId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.loanInId = response.data.id;

          angular.forEach(response.data.items, function (item) {
            item.expiryDate = new Date(item.expiryDate);
          });

          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
          });

          self.model.assetType = self.model.assetTypes.find(function (item) {
            return response.data.assetTypeId == item.id;
          });

          self.model.remark = response.data.remark;
          self.model.vendorName = response.data.vendor;
          self.model.vendorId = response.data.vendorId;
          self.model.dcNumber = response.data.dcNo;
          self.model.dcDate = new Date(response.data.dcDate);
          self.model.itemDetails = [];

          angular.forEach(response.data.items, function (item) {
            var poUom = [];
            angular.forEach(item.uomList, function (uom) {
              poUom.push({
                id: uom.id,
                uomUnitId: {
                  uomUnit: uom.uomUnit,
                  uomUnitId: uom.uomUnitId
                },
                conversion: parseInt(uom.conversion),
                ipUom: uom.ipUom,
                opUom: uom.opUom,
                storeUom: uom.storeUom,
                uomTypeId: uom.uomTypeId,
                uomUnit: uom.uomUnit,
                uomType: uom.uomType,
                uomDetails: uom.uomDetails
              });
            });

            var indentItem = {
              "id": item.id,
              "itemId": item.itemId,
              "itemCode": item.itemCode,
              "itemName": item.itemName,
              "batchNumber": item.batchNo,
              "expiryDate": item.expiryDate,
              "quantity": item.quantity,
              "cop": item.cop.toFixed(2),
              "totalAmount": item.totalAmount,
              "specification": item.specification,
              "remark": item.remark
            }

            if (response.data.assetTypeId == 1) {
              indentItem.selectedUom = item.uomList.find(function (uom) {
                return item.uomTypeId == uom.uomTypeId;
              });

              indentItem.uom = item.uomList;
              indentItem.poUom = poUom;
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

    var ValidateDetails = function () {
      var isValid = true;

      if (!self.model.store.id) {
        isValid = false;
        self.model.isStoreValid = true;
      }

      if (!self.model.assetType.id) {
        isValid = false;
        self.model.isAssetTypeValid = true;
      }

      if (!self.model.vendorId || !self.model.vendorName) {
        isValid = false;
        self.model.vendorIdValid = true;
      }

      if (!self.model.store.id || !self.model.assetType.id || !self.model.vendorId || !self.model.vendorName) {
        isValid = false;
        growl.error('Please select required field');
      }

      return isValid;
    }

    var ValidateItems = function () {
      var isValidItem = true;

      if (!!self.model.dcNumber){
        if (!/^[a-z0-9]*[-]*[/]?[a-z0-9]*$/i.test(self.model.dcNumber)) {
          growl.error("Please enter valid DC number");
          self.model.dcNumberValid = true;
          isValidItem = false;
        }
      }

      if(!self.model.dcNumber){
        growl.error('Please enter DC Number');
        isValidItem = false;
      }
      
      if (!self.model.dcDate) {
        growl.error('Please enter valid DC date');
        isValidItem = false;
      }

      if (self.model.itemDetails.length <= 0) {
        growl.error('Please add items');
        isValidItem = false;
      } else {
        for (var i = 0; i < self.model.itemDetails.length; i++) {
          if (!parseInt(self.model.itemDetails[i].quantity)) {
            growl.error("Please enter valid quantity");
            self.model.isQuantity[i] = true;
            isValidItem = false;
          }

          if (!self.model.itemDetails[i].batchNumber) {
            growl.error("Please enter valid batch number");
            self.model.isBatchNumber[i] = true;
            isValidItem = false;
          } else{
            if (!/^[a-z0-9]*[-]*[/]?[a-z0-9]*$/i.test(self.model.itemDetails[i].batchNumber)) {
              growl.error("Please enter valid batch number");
              isValidItem = false;
              self.model.isBatchNumber[i] = true;
            }
          }

          if (self.model.assetType.id == 1) {
            if (!self.model.itemDetails[i].expiryDate) {
              growl.error("Please enter valid expiry date");
              self.model.isExpiryDate[i] = true;
              isValidItem = false;
            }
          }
          
          if (!parseFloat(self.model.itemDetails[i].cop)) {
            growl.error("Please enter valid cop");
            self.model.isCop[i] = true;
            isValidItem = false;
          }
        }
      }

      return isValidItem;
    }

    var SetDecimal = function (item, value) {
      if (value == item.cop) {
        if (isNaN(item.cop) || !item.cop || item.cop == 0) {
          item.cop = 0;
        } else {
          item.cop = parseFloat(item.cop).toFixed(2);
        }
      }
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

    var PopulateIndentDropdown = function () {
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
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var Expand = function (item) {
      item.Expanded = !item.Expanded;
    }

    var GetItemList = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      if(self.model.itemName){
        if(self.model.itemName.length >= 3){      
          var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

          var data = {
            "assetTypeId": self.model.assetType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName
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
              PaginationService.searchDataByField(self.model.items, self.model.pagination);
            }
            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            growl.error(err.data.message);
          });
        }
      }
      else if(self.model.itemCode){
        if(self.model.itemCode.length >= 3){
          var URI = CONSTANTS.GLOBAL.ITEM_API +"unit/"+$scope.unitId;

          var data = {
            "assetTypeId": self.model.assetType.id,
            "assetCategoryId": self.model.assetCategory.id,
            "productCategoryId": self.model.productCategory.id,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName
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
              PaginationService.searchDataByField(self.model.items, self.model.pagination);
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
    	 if (!self.model.SelectedItemsIds.length) {
    	        growl.error('Please select items to add');
    	        return false;
    	      }else{
    	    	  
    	    	  ActaulAddItems();
    	    	  
    	    	  /*
    	    	  var stopData = {
    	   	                "itemIds": self.model.SelectedItemsIds,
    	   	                "storeId": self.model.store.id	      		 
    	   	          };
    	        	stopTransactionService.GetstopTransactionData(stopData).then(function(responseData){        
    	                var StopTransactionData = responseData;  
    	                if(StopTransactionData.length > 0 ){    
    	                         var startdate =  $filter('date')(StopTransactionData[0].startDate, "dd-MM-yyyy");
    	                         var stopdate=  $filter('date')(StopTransactionData[0].stopDate, "dd-MM-yyyy");
    	                         growl.warning("Stop Transation is in Process for this Item(s) <br/>Start Date : "+ startdate +" <br/> Stop Date: "+ stopdate +" <br/> Days: "+ StopTransactionData[0].days +" <br/>  Hours: "+ StopTransactionData[0].hours +" <br/>  Minutes: "+ StopTransactionData[0].minutes +"  ");                    
    	                         $rootScope.stopLoader();
    	                         self.model.SelectedItemsIds = [];
    	                         return false;
    	                 }
    	                else{
    	                	ActaulAddItems();
    	                }
    	       	  });    
    	      */}
    }

    var ActaulAddItems = function () {     
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
        var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;

        var data = {
          "itemIds": self.model.SelectedItemsIds,
          "storeId": self.model.store.id
        };

        $rootScope.startLoaderPopup();
        GenericService.serviceAction("POST", URI, data).then(
          function (response) {
            response.data.forEach(function (item) {
              var isFound = self.model.itemDetails.find(function (value) {
                return value.itemId == item.itemId;
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

            $rootScope.stopLoaderPopup();
          },
          function (err) {
            $rootScope.stopLoaderPopup();
            // self.model.SelectedItemsIds = [];
            growl.error(err.data.message);
          });      
    }

    var selectUOM = function (item) {
      item.selectedUom = item.uom.find(function (obj) {
        if(!!obj.storeUom){
          return obj;
        }
      });

      if (!item.selectedUom) {
        item.selectedUom = item.uom[0];
      }
      
      item.poUom = [];

      item.uom.forEach(function (puom) {
        item.poUom.push({
          "conversion": puom.conversion,
          "id": puom.id,
          "ipUom": puom.ipUom,
          "opUom": puom.opUom,
          "storeUom": puom.storeUom,
          "uomTypeId": puom.uomTypeId,
          "uomUnitId": {
            'uomUnit': puom.uomUnit,
            'uomUnitId': puom.uomUnitId
          },
          "uomType": puom.uomType,
          "uomUnit": puom.uomUnit,
          "uomDetails": puom.uomDetails
        });
      });
    }

    var isSelectedItem = function (item) {
      return item.isSelectedItem;
    }

    var getSelectedItems = function () {
      var hash = {};
      self.model.indentItemDetails.forEach(function (item) {
        var itemId = item.itemId;
        var indentId = item.id;
        if (hash.hasOwnProperty(itemId)) {
          hash[itemId][indentId] = {
            pendingQuantity: item.pendingQuantity
          }
        } else {
          hash[itemId] = {};
          hash[itemId][indentId] = {
            pendingQuantity: item.pendingQuantity
          }
        }
      });
      return hash;
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

    var SaveLoanInRequest = function () {
      if (ValidateItems()) {
        if (!self.model.loanInId) {
          CreateLoanIn();
        } else {
          UpdateLoanIn();
        }
      }
    }

    var CreateLoanIn = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.SAVE_API;

      var data = {
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "assetTypeId": self.model.assetType.id,
        "remark": self.model.remark,
        "statusId": setStatus("New"),
        "totalAmount": self.model.totalAmount,
        "dcNo": self.model.dcNumber,
        "dcDate": moment(self.model.dcDate).format('YYYY-MM-DD'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemDetails, function (item) {
        if (self.model.assetType.id == 1) {
          var expDateMonth = item.expiryDate.getMonth() + 1;
          var expDateYear = item.expiryDate.getFullYear();
          var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
        }

        var poUom = [];
        angular.forEach(item.poUom, function (puom) {
          poUom.push({
            "conversion": puom.conversion,
            "id": puom.id,
            "ipUom": puom.ipUom,
            "opUom": puom.opUom,
            "storeUom": puom.storeUom,
            "uomTypeId": puom.uomTypeId,
            "uomUnitId": puom.uomUnitId.uomUnitId,
            "uomType": puom.uomType,
            "uomUnit": puom.uomUnit,
            "uomDetails": puom.uomDetails
          });
        });

        var loanItem = {
          "itemId": item.itemId,
          "batchNo": item.batchNumber,
          "quantity": parseInt(item.quantity),
          "cop": parseFloat(item.cop),
          "totalAmount": item.totalAmount,
          "remark": item.remark,
          "specification": item.specification,
          "statusId": setStatus("New")
        }

        if (self.model.assetType.id == 1) {
          loanItem.uomTypeId = item.selectedUom.uomTypeId;
          loanItem.uomUnitId = item.selectedUom.uomUnitId;
          if(expDateMonth < 10){
              expDateMonth = "0" + expDateMonth;
          }
          loanItem.expiryDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
          loanItem.uom = poUom;
        }

        data.items.push(loanItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
          NavigateToLoanIn();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateLoanIn = function () {
      var URI = CONSTANTS.PROCUREMENT.LOAN_IN_OUT.LOAN_IN.DETAILS_API + self.model.loanInId;

      var data = {
        "remark": self.model.remark,
        "dcNo": self.model.dcNumber,
        "dcDate": moment(self.model.dcDate).format('YYYY-MM-DD'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(self.model.itemDetails, function (item) {
        if (self.model.assetType.id == 1) {
          var expDateMonth = item.expiryDate.getMonth() + 1;
          var expDateYear = item.expiryDate.getFullYear();
          var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
        }

        var poUom = [];
        angular.forEach(item.poUom, function (uom) {
          poUom.push({
            "conversion": uom.conversion,
            "id": uom.id,
            "ipUom": uom.ipUom,
            "opUom": uom.opUom,
            "storeUom": uom.storeUom,
            "uomTypeId": uom.uomTypeId,
            "uomUnitId": uom.uomUnitId.uomUnitId,
            "uomType": uom.uomType,
            "uomUnit": uom.uomUnit,
            "uomDetails": uom.uomDetails
          });
        });

        var loanItem = {
          "id": item.id,
          "itemId": item.itemId,
          "batchNo": item.batchNumber,
          "quantity": parseInt(item.quantity),
          "cop": item.cop,
          "totalAmount": item.totalAmount,
          "remark": item.remark,
          "specification": item.specification,
          "statusId": setStatus("New")
        }

        if (self.model.assetType.id == 1) {
          loanItem.uomTypeId = item.selectedUom.uomTypeId;
          loanItem.uomUnitId = item.selectedUom.uomUnitId;
          if(expDateMonth < 10){
              expDateMonth = "0" + expDateMonth;
          }
          loanItem.expiryDate = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
          if (!item.id) {
            loanItem.uom = item.uom;
          } else {
            loanItem.uom = poUom;
          }
        }

        data.items.push(loanItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigateToLoanIn();
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

    function AddVendorName(vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }

    var NavigateToLoanIn = function () {
      $state.go('loanIn');
    }

    // store change confirmation
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

    var ConfirmAdd = function (index, bill, parentIndex) 
    {
      bill.splice(index + 1, 0, CopyItem(index, bill));
    }

    var CopyItem = function (index, bill) {
      var item = bill[index];

      return {
        itemId: item.itemId,
        itemCode: item.itemCode,
        itemName: item.itemName,
        genericName: item.genericName,
        uom: item.uom,
        selectedUom: item.uom[0]
      }
    }

    $scope.validateBatchNo = function (item, index) {
      var isValid = true;
      if(self.model.itemDetails.length > 1){
        angular.forEach(self.model.itemDetails, function(data, key){
          if(data.itemId == item.itemId && data.batchNumber == item.batchNumber && index != key){
            growl.error("Please enter different Batch No");
            isValid = false;
            item.batchNumber = '';
          }
        });
      }
      return isValid;
    }


    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewLoanIn", {
      url: "/addNewLoanIn",
      templateUrl: 'views/procurement/consignment/add-new-loan-in.html',
      controller: "AddNewLoanIn.Controller",
      controllerAs: "vm",
      title: 'Loan In'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewLoanIn.Controller", addNewLoanInController);
})();