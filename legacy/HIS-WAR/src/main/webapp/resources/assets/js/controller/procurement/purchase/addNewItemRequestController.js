(function () {
  "use strict";

  function addNewItemRequestController($scope, $rootScope, $filter, $http, $state, $cookies, CONSTANTS, GenericService, commonDetailService, StatusService, growl, CurrencyService) {
    var self = this;

    
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    
    function initializeController() 
    {
      $rootScope.loginpage = true;

      self.model = {
        isItemNameValid: [],
        isManufacturerValid: [],
        isVendorValid: [],
        isPriceValid: [],
        isReasonValid: [],
        items: [],
        vendorSearch: '',
        manufacturerSearch: '',
        prevSearch: '',
        mPrevSearch: '',
        vendorNames: [],
        manufacturerNames: [],
        assetType: 1,
        isEnabled: false
      };
      
      StatusService.GetStatus().then(function(status) {
        self.model.statuses = status;
      });

      CurrencyService.GetCurrency().then(function(currency){
        self.model.Currency = currency;
      });

      self.model.SavePurchaseRequest = SavePurchaseRequest;
      self.model.NavigateToItemRequest = NavigateToItemRequest;
      self.model.AddDuplicateRow = AddDuplicateRow;
      self.model.RemoveRowData = RemoveRowData;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.SearchManufacturer = SearchManufacturer;
      self.model.AddVendorName = AddVendorName;
      self.model.AddManufacturer = AddManufacturer;
      self.model.SetDecimal = SetDecimal;
      self.model.ValidateItemName = ValidateItemName;
      self.model.ValidateManufacture = ValidateManufacture;
      self.model.ValidateVendorName = ValidateVendorName;

      PopulateItems();
    }

    var setStatus = function (x){
      var status = self.model.statuses.find(function(obj){
        return obj.status == x;
      });
      
      return status.id;
    }

    var SearchVendorNames = function(search) {
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
        if (!!self.model.vendorNames && self.model.vendorNames.length > 0 && self.model.vendorNames[0].vendorFound != undefined && !self.model.vendorNames[0].vendorFound) {
          return nameNotFound(search);
        } else {
          return $filter('filter')(self.model.vendorNames, {
            $: search
          });
        }
      }
    }

    var nameNotFound = function(search) {
      var item = {
        "vendorFound": false,
        "detail": "Vendor Not Found with Name " + search,
        "search": search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    var AddVendorName = function (vendor, item) {
      if (!vendor.id) {
        item.vendorName = vendor.search;
        item.vendorId = undefined
      } else {
        item.vendorName = vendor.vendorName;
        item.vendorId = vendor.id;
      }
    }

    var SearchManufacturer = function(search) {
      if (search.length < 1) return;
      if ((!self.model.mPrevSearch || self.model.mPrevSearch != search) && (search.length == 1)) {
        self.model.mPrevSearch = search;
        var URI = CONSTANTS.GLOBAL.MANUFACTURER_SEARCH_API +$scope.organizationId + "/"+ search;

        $rootScope.startLoader();
        return GenericService.serviceAction("GET", URI, {})
        .then(function (response) {
          if (!!response.data && response.data.length > 0) {
            self.model.manufacturerNames = response.data;
            $rootScope.stopLoader();
            return $filter('filter')(self.model.manufacturerNames, {
              $: search
            });
          } else {
            $rootScope.stopLoader();
            return manufacturerNotFound(search);
          }
        }, function (err) {
          $rootScope.stopLoader();
          return manufacturerNotFound(search);
        });
      } else {
        if (!!self.model.manufacturerNames && self.model.manufacturerNames.length > 0 && self.model.manufacturerNames[0].manufacturerFound != undefined && !self.model.manufacturerNames[0].manufacturerFound) {
          return manufacturerNotFound(search);
        } else {
          return $filter('filter')(self.model.manufacturerNames, {
            $: search
          });
        }
      }
    }

    var manufacturerNotFound = function (search) {
      var item = {
        "manufacturerFound": false,
        "detail": "Manufacturer Not Found with Name " + search,
        "search": search
      };
      self.model.manufacturerNames = [item];
      return self.model.manufacturerNames;
    }

    var AddManufacturer = function (manufacture, item) {
      if (!manufacture.id) {
        item.manufacturer = manufacture.search;
        item.manufacturerId = undefined;
      } else {
        item.manufacturer = manufacture.manufacturerName;
        item.manufacturerId = manufacture.id;
      }
    }
    
    var GetEmptyData = function (index){
      // self.model.isItemNameValid[index] = false; 
      // self.model.isManufacturerValid[index] = false; 
      // self.model.isVendorValid[index] = false; 
      // self.model.isPriceValid[index] = false; 
      // self.model.isReasonValid[index] = false; 

      return {
        itemName: '',
        manufacturer: '',
        vendorName: '',
        expectedPrice: '',
        reasonForItemCreation: '',
        status: ''
      }
    }
      
    var AddDuplicateRow = function (index){
      self.model.items.splice(index+1, 0, GetEmptyData(index));
    }

    var RemoveRowData = function (index){
      var popup = angular.element('#confirmRemove');
      popup.modal('show');
      self.model.removeItemIndex = index;
    }

    var ConfirmRemove = function (){
      var items = self.model.items;
      self.model.items = items.slice(0, self.model.removeItemIndex).concat(items.slice(self.model.removeItemIndex + 1));
      var popup = angular.element('#confirmRemove');
      popup.modal('hide');
    }

    var SetDecimal = function (item, index) {
      if(self.model.items.length > 1){
        
        if (isNaN(item.expectedPrice) || !item.expectedPrice || item.expectedPrice == 0) {
          item.expectedPrice = 0;
        } else {
          item.expectedPrice = parseFloat(item.expectedPrice).toFixed(2);
        }
      
        let object = self.model.items.filter(x => x.itemName === item.itemName && x.manufacturer === item.manufacturer && x.vendorName === item.vendorName && x.expectedPrice === item.expectedPrice)
        if(object && object.length > 1){
          if(self.model.assetType == 2){
          self.model.isItemNameValid[index] = true;
          self.model.isManufacturerValid[index] = true;
        //  self.model.isVendorValid[index] = true;
         // self.model.isPriceValid[index] = true;
          self.model.isEnabled = false;
          growl.error("Item request already added with same data in the list");
          }
          else{
          self.model.isItemNameValid[index] = true;
          self.model.isManufacturerValid[index] = true;
        // self.model.isVendorValid[index] = true;
         // self.model.isPriceValid[index] = true;
          self.model.isEnabled = false;
          growl.error("Item request already added with same data in the list");
          }
        
        } else {
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
         // self.model.isVendorValid[index] = false;
          //self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
        }
      } else{
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
         // self.model.isVendorValid[index] = false;
         // self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
          if (isNaN(item.expectedPrice) || !item.expectedPrice || item.expectedPrice == 0) {
            item.expectedPrice = 0;
          } else {
            item.expectedPrice = parseFloat(item.expectedPrice).toFixed(2);
          }
        }
    }

    var ValidateItemName = function(item, index){
      var isValid = true;
      if(self.model.items.length > 1){
        let object = self.model.items.filter(x => x.itemName === item.itemName && x.manufacturer === item.manufacturer && x.vendorName === item.vendorName && x.expectedPrice === item.expectedPrice)
        if(object && object.length > 1){
          if(self.model.assetType == 2){
            self.model.isItemNameValid[index] = true;
            self.model.isManufacturerValid[index] = true;
          //  self.model.isVendorValid[index] = true;
           // self.model.isPriceValid[index] = true;
            self.model.isEnabled = false;
            growl.error("Item request already added with same data in the list");
            }
            else{
            self.model.isItemNameValid[index] = true;
            self.model.isManufacturerValid[index] = true;
           // self.model.isVendorValid[index] = true;
           // self.model.isPriceValid[index] = true;
            self.model.isEnabled = false;
            growl.error("Item request already added with same data in the list");
            }
        } else {
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
        //  self.model.isVendorValid[index] = false;
         // self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
        }
      } else {
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
        //  self.model.isVendorValid[index] = false;
         // self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
          item.itemName = (item.itemName).replace(/\s+/g,' ') ;
          // if (!/^[a-z0-9]*(([ ][a-z0-9 ])?[a-z0-9]*)*$/i.test(item.itemName)) {
          //   self.model.isItemNameValid[index] = true;
          //   growl.error("Please enter valid item name");
          //   isValid = false;
          // }
        }
      return isValid;
    }

    var ValidateManufacture = function(item, index){
      var isValid = true;
      if(self.model.items.length > 1){
        let object = self.model.items.filter(x => x.itemName === item.itemName && x.manufacturer === item.manufacturer && x.vendorName === item.vendorName && x.expectedPrice === item.expectedPrice)
        if(object && object.length > 1){
          if(self.model.assetType == 2){
            self.model.isItemNameValid[index] = true;
            self.model.isManufacturerValid[index] = true;
           // self.model.isVendorValid[index] = true;
           // self.model.isPriceValid[index] = true;
            self.model.isEnabled = false;
            growl.error("Item request already added with same data in the list");
            }
            else{
            self.model.isItemNameValid[index] = true;
            self.model.isManufacturerValid[index] = true;
           // self.model.isVendorValid[index] = true;
           // self.model.isPriceValid[index] = true;
            self.model.isEnabled = false;
            growl.error("Item request already added with same data in the list");
            }
        } else {
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
        //  self.model.isVendorValid[index] = false;
       //   self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
        }
      } else {
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
       //   self.model.isVendorValid[index] = false;
         // self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
          if (/[^ a-z A-Z \.]/.test(item.manufacturer)) {
            self.model.isManufacturerValid[index] = true;
            growl.error("Please enter valid manufacture name");
            isValid = false;
          }
        }
        return isValid;
    }

    var ValidateVendorName = function(item, index){
      var isValid = true;
      if(self.model.items.length > 1){
        let object = self.model.items.filter(x => x.itemName === item.itemName && x.manufacturer === item.manufacturer && x.vendorName === item.vendorName && x.expectedPrice === item.expectedPrice)
        if(object && object.length > 1){
          if(self.model.assetType == 2){
            self.model.isItemNameValid[index] = true;
            self.model.isManufacturerValid[index] = true;
            self.model.isVendorValid[index] = true;
           // self.model.isPriceValid[index] = true;
            self.model.isEnabled = false;
            growl.error("Item request already added with same data in the list");
            }
            else{
            self.model.isItemNameValid[index] = true;
            self.model.isManufacturerValid[index] = true;
            self.model.isVendorValid[index] = true;
           // self.model.isPriceValid[index] = true;
            self.model.isEnabled = false;
            growl.error("Item request already added with same data in the list");
            }
        } else {
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
          self.model.isVendorValid[index] = false;
        //  self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
        }
      } else {
          self.model.isItemNameValid[index] = false;
          self.model.isManufacturerValid[index] = false;
          self.model.isVendorValid[index] = false;
         // self.model.isPriceValid[index] = false;
          self.model.isEnabled = true;
          if (/[^ a-z A-Z \.]/.test(item.vendorName)) {
            self.model.isVendorValid[index] = true;
            growl.error("Please enter valid vendor name");
            isValid = false;
          }
        }
        return isValid;
    }

    var PopulateItems = function () {
      try {
        $rootScope.startLoader();
        var itemId = commonDetailService.getDataId();

        $rootScope.stopLoader();
        AddDuplicateRow(0);
        if (!!itemId) {
          commonDetailService.setDataId(null);
          GetItemRequest(itemId);
        }
      } catch (error) {
        alert(error);
      }
    }

    var GetItemRequest = function (itemId) {
      var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_REQUEST.DETAILS_API + itemId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.itemId = response.data.id;
          self.model.assetType = response.data.assetTypeId;
          self.model.items = [];
          self.model.statustoDisable = response.data.status;

          angular.forEach(response.data.items, function (item) {
            if (!item.manufacturer) {
              item.manufacturer = item.manufactureTemp;
            }
            if (!item.vendor) {
              item.vendor = item.vendorTemp;
            }

            var indentItem = {
              "id": item.id,
              "itemName": item.itemName,
              "manufacturer": item.manufacturer,
              "vendorName": item.vendor,
              "vendorId": item.vendorId,
              "manufacturerId": item.manufacturerId,
              "expectedPrice": item.expectedPrice ? parseFloat(item.expectedPrice.toFixed(2)) : 0,
              "reasonForItemCreation": item.itemCreationReason,
              "statusId": item.statusId
            }
            self.model.items.push(indentItem);

          });
          
          /* start code for resolved  HIS-10623 After Remove item from item request ,save button showing disable */
          self.model.isEnabled = true;
          /* end code for resolved  HIS-10623 After Remove item from item request ,save button showing disable */

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var ValidateItems = function (){
      var isValidItem = true;

      if (self.model.items.length <= 0) {
        isValidItem = false;
      }else {
        if (!self.model.assetType) {
          isValidItem = false;
          self.model.isAssetTypeValid = true;
        }

        for (var i = 0; i < self.model.items.length; i++) {
          // if (!/^[a-z0-9]*(([ ][a-z0-9 ])?[a-z0-9]*)*$/i.test(self.model.items[i].itemName) || !self.model.items[i].itemName) {
          if(!self.model.items[i].itemName){
            self.model.isItemNameValid[i] = true;
            growl.error("Please enter valid item name");
            isValidItem = false;
          }

          // if (/[^ a-z A-Z \.]/.test(self.model.items[i].manufacturer) || !self.model.items[i].manufacturer) {
          if(!self.model.items[i].manufacturer){
            self.model.isManufacturerValid[i] = true;
            growl.error("Please enter valid manufacturer name");
            isValidItem = false;
          }

          // if (/[^ a-z A-Z \.]/.test(self.model.items[i].vendorName) || !self.model.items[i].vendorName) {
          if(!self.model.items[i].vendorName){
            self.model.isVendorValid[i] = true;
            growl.error("Please enter valid vendor name");
            isValidItem = false;
          }

          if (!parseFloat(self.model.items[i].expectedPrice)) {
            growl.error("Please enter valid expected price of item");
            self.model.isPriceValid[i] = true;
            isValidItem = false;
          }

          if (!self.model.items[i].reasonForItemCreation) {
            growl.error("Please enter valid reason for item creation");
            self.model.isReasonValid[i] = true;
            isValidItem = false;
          }
        }
      }

      return isValidItem;
    }

    var SavePurchaseRequest = function () {
      if (ValidateItems()) {
        if (!self.model.itemId) {
          CreateItemRequest();
        } 
        else {
          UpdateItemRequest();
        }
      }
    }

    var CreateItemRequest = function () {
      var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_REQUEST.SAVE_API;

      var data = {
        "statusId": setStatus('New'),
        "approvalStatusId": setStatus('New'),
        "assetTypeId": self.model.assetType,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "storeId":$scope.storeId

      };

      angular.forEach(self.model.items, function (item, key) {
        var requestItem = {
          "itemName": item.itemName,
          "manufacturerId": item.manufacturerId,
          "vendorId": item.vendorId,
          "expectedPrice": item.expectedPrice,
          "itemCreationReason": item.reasonForItemCreation,
          "manufactureTemp": item.manufacturer,
          "vendorTemp": item.vendorName,
          "statusId": setStatus('New'),
        }

        data.items.push(requestItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
          NavigateToItemRequest();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var UpdateItemRequest = function () {
      var URI = CONSTANTS.PROCUREMENT.PURCHASE.ITEM_REQUEST.SAVE_API + self.model.itemId;

      var data = {
        // "requestNumber": "REQ-001",
        // "requestDate": moment(new Date()).format('YYYY-MM-DD'),
         "assetTypeId": self.model.assetType,
        // "statusId": setStatus('New'),
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId,
        "storeId":$scope.storeId

      };

      angular.forEach(self.model.items, function (item) {
        var requestItem = {
          "id": item.id,
          "itemName": item.itemName,
          "expectedPrice": item.expectedPrice,
          "itemCreationReason": item.reasonForItemCreation,
          "manufacturerId": item.manufacturerId ? item.manufacturerId : undefined,
          "vendorId": item.vendorId ? item.vendorId : undefined,
          "manufactureTemp": item.manufacturer ? item.manufacturer : undefined,
          "vendorTemp": item.vendorName ? item.vendorName : undefined,
          "statusId": setStatus('New')
        }

        data.items.push(requestItem);
      });

      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          growl.success(response.data.message);
          NavigateToItemRequest();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.data.message);
        });
    }

    var NavigateToItemRequest = function () {
      $state.go('itemRequest');
    }

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewitemRequest", {
      url: "/addNewitemRequest",
      templateUrl: 'views/procurement/purchase/add-item-request-purchase.html',
      controller: "AddNewItemRequest.Controller",
      controllerAs: "vm",
      title: 'Item Request'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewItemRequest.Controller", addNewItemRequestController);
})();