(function () {
  "use strict";

  function addNewConsignmentInwardController($scope, $rootScope, $http, CONSTANTS, $state, commonDetailService,PaginationService, $filter, CurrencyService, GenericService, growl,$cookies) {
    var self = this;
    var dt = new Date();
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
        SelectedItemsIds: [],
        storeIndentData: [],
        selectAllRows: false,
        SelectedItemIds: [],
        dateOptions: {
          formatYear: 'yyyy',
          showWeeks: false
        },

        expiryDateOptions: {
          minDate: dt.setMonth(dt.getMonth() + 1),
          minMode: "month",
          maxMode: "year",
          datepickerMode: "month"
        },
        dateOpen: [],
        ItemDetailList: [],
        dcDate: new Date(),
      };

      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      });

      self.model.OpenExpiryDate = OpenExpiryDate;
      self.model.PopulateDropdown = PopulateDropdown;
      self.model.GetAssetCategoryList = GetAssetCategoryList;
      self.model.GetProductCategoryList = GetProductCategoryList;
      self.model.searchNewItem = searchNewItem;
      self.model.GetItemList = GetItemList;
      self.model.AddItems = AddItems;
      self.model.fillItemList = fillItemList;
      self.model.SelectAllItems = SelectAllItems;
      self.model.SelectItem = SelectItem;
      self.model.isSelected = isSelected;
      self.model.SearchVendorNames = SearchVendorNames;
      self.model.nameNotFound = nameNotFound;
      self.model.AddVendorName = AddVendorName;
      self.model.search = '';
      self.model.prevSearch = '';
      self.model.vendorNames = [];
      self.model.OpenDcDate = OpenDcDate;
      self.model.uomInfoOnClick = uomInfoOnClick;
      self.model.uomModalOnClick = uomModalOnClick;
      self.model.uomSelectEvent = uomSelectEvent;
      self.model.modalUOMSelectEvent = modalUOMSelectEvent;
      self.model.selectonchange = selectonchange;
      self.model.CalculateTotalAmount = CalculateTotalAmount;
      self.model.GetUOMUnitList = GetUOMUnitList;
      self.model.setSelectedDate = setSelectedDate;
      self.model.AddDuplicateRow = AddDuplicateRow;
      self.model.ConfirmRemove = ConfirmRemove;
      self.model.RemoveItem = RemoveItem;
      self.model.ModalDismiss = ModalDismiss;
      self.model.discountPercentCal = discountPercentCal;
      self.model.ValidateCOP = ValidateCOP;
      self.model.SaveConsignmentInwward = SaveConsignmentInwward;
      self.model.NavigteToConsignmentInward = NavigteToConsignmentInward;
      self.model.validateSave = validateSave;
      self.model.ValidateExpiry = ValidateExpiry;
      self.model.ValidateUom = ValidateUom;
      self.model.ValidateBatch = ValidateBatch;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.validateUomPopup = validateUomPopup;

      GetUOMUnitList();
      PopulateDropdown();

    }
    function GetItemList() {
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

    var OpenDcDate = function () {
      self.model.dcDateOpened = true;
    }

    var OpenExpiryDate = function (idx) {
      self.model.dateOpen[idx] = true;
    }

    var PopulateDropdown = function () {
      var inward = self.model;

      var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
    //  inward.store = inward.stores[0];

      inward.stores.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          inward.stores = inward.stores.concat(response.data);
          for(var i=0;i<inward.stores.length;i++)
          {
        	 	 if(inward.stores!=undefined && inward.stores[i].id!=undefined){
        	 		 var selectedTopStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
        	 		 if(inward.stores[i].id==(selectedTopStore != null ? selectedTopStore.storeId : undefined))
        		 {
        			 inward.store = inward.stores[i];
        		 }
        	  }
          }
          $rootScope.stopLoader();
          var inwardId = commonDetailService.getDataId();
          $rootScope.stopLoader();
          if (!!inwardId) {
            GetInward(inwardId);
          }
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetAssetCategoryList = function () {
      var inward = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

      inward.assetCategory = inward.assetCategories[0];
      inward.productCategory = inward.productCategories[0];

      inward.assetCategories.splice(1);
      inward.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          inward.assetCategories = inward.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetProductCategoryList = function (id) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var inward = self.model;

      //TODO : Add asset category to the uri
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
      self.model.productCategory = self.model.productCategories[0];
      self.model.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          inward.productCategories = inward.productCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    function searchNewItem() {
      if(self.model.search.itemName){
        if(self.model.search.itemName.length >= 3){
          var URI = CONSTANTS.GLOBAL.ITEM_API + true;
          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "storeId": self.model.store.id,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName,
            "unitId" : $scope.unitId
          }

          self.model.searchNewItemList = [];
          self.model.pagination.data=[];
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
          var URI = CONSTANTS.GLOBAL.ITEM_API + true;
          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "storeId": self.model.store.id,
            "itemCode": self.model.search.itemCode,
            "itemName": self.model.search.itemName,
            "unitId" : $scope.unitId
          }

          self.model.searchNewItemList = [];
          self.model.pagination.data=[];
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
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      // new test api
      var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API +'/org/' + $scope.organizationId +'/unit/'+$scope.unitId;
      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.store.id
      };

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          fillItemList(response.data);
          self.model.SelectedItemsIds = [];
          self.model.items = [];

          $rootScope.stopLoader();

        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.message);
        });
    }

    var fillItemList = function (list) {
      list.forEach(function (item) {
        var defaultUOM = [];
        for (let uom of item.uom) {
          defaultUOM.push({
            uomTypeId: uom.uomTypeId,
            uomUnitId: uom.uomUnitId,
            conversion: uom.conversion,
            ipUom: uom.ipUom,
            opUom: uom.opUom,
            storeUom: uom.storeUom
          });
        }

        item['defaultUOM'] = defaultUOM;
        self.model.ItemDetailList.push(item);

      });
    }

    var SelectAllItems = function () {
      if (self.model.selectAllRows) {
        self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
          item.isSelected = true;
          self.model.isItemChecked = true;
          return item.itemId;
        });
      } else {
        self.model.SelectedItemsIds = [];
        self.model.searchNewItemList.forEach(function (item) {
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

    var isSelected = function (item) {
      return item.isSelected;
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
        "detail": "Vendor Not found with name " + search
      };
      self.model.vendorNames = [item];
      return self.model.vendorNames;
    }

    function AddVendorName(vendor) {
      self.model.vendorName = vendor.vendorName;
      self.model.vendorId = vendor.id;
    }

    var uomSelectEvent = function (datum) {
      datum.uom.forEach(function (uom) {
        if (uom.uomTypeId == datum.uomTypeId) datum.selectedUom = uom;
      });
    }

    var GetUOMUnitList = function () {
      var URI = CONSTANTS.GLOBAL.UOM_UNITS + "/"+ $scope.organizationId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.uomUnits = response.data;
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var selectonchange = function (item) {
      if (item.selectedUom.uomTypeId == 1) {
        item.newmaxquantity = (item.maxOrderQuantity / 10) / 10;
      } else if (item.selectedUom.uomTypeId == 2) {
        item.newmaxquantity = (item.maxOrderQuantity / 10);

      } else {
        item.newmaxquantity = item.maxOrderQuantity;
      }

    }

    var modalUOMSelectEvent = function (datum) {
      self.model.uomUnits.forEach(function (uom) {
        if (uom.id == datum.uomUnitId)
          datum.selectedUOM = uom;
      });
    }

    var uomInfoOnClick = function (item, index) {
      self.model.selectedUOMList = [];
      self.model.selectedUOMUnit = [];
      self.model.selectedItem = item;
      if (!!item.uom) {
        item.uom.forEach(function (uom) {
          self.model.selectedUOMList.push(angular.copy(uom));
        });
      }
    }

    var uomModalOnClick = function () {
      if (self.model.selectedUOMList) {
        if (validateUomPopup()) {
          self.model.selectedUOMList.forEach(function (item) {
            var defaultUom = self.model.selectedItem.defaultUOM.find(function (uom) {
              return uom.uomTypeId == item.uomTypeId;
            });

            var uom = self.model.selectedItem.uom.find(function (uom) {
              return uom.uomTypeId == item.uomTypeId;
            });

            uom.uomUnitId = item.uomUnitId;
            uom.uomUnit = item.uomUnit;
            uom.conversion = parseInt(item.conversion);

            defaultUom.uomUnitId = item.uomUnitId;
            defaultUom.conversion = parseInt(item.conversion);

            if (!!self.model.selectedItem.selectedUom && self.model.selectedItem.selectedUom.uomTypeId == item.uomTypeId) {
              self.model.selectedItem.selectedUom.uomUnitId = item.uomUnitId;
              self.model.selectedItem.selectedUom.uomUnit = item.uomUnit;
              self.model.selectedItem.selectedUom.conversion = parseInt(item.conversion);

              self.model.selectedItem.uomUnit = self.model.selectedItem.selectedUom.uomUnit;
            }
          });

          // ValidateMaxOrderQuantity(self.model.selectedItem);

          $('#selectUOM_modal').modal('hide');
        }
      }
    }

    var validateUomPopup = function () {
      var isValid = true;
      self.model.selectedUOMList.forEach(function (item) {
        if (!parseInt(item.conversion)) {
          growl.error('Please Insert Conversion Factor')
          isValid = false;
        } else if (parseInt(item.conversion) == 0) {
          growl.error('Conversion Factor cannot be zero')
          isValid = false;
        }
      });
      return isValid;
    }

    var setSelectedDate = function (item) {
      var expDateYear = item.expiryDate.getFullYear();
      var expDateMonth = item.expiryDate.getMonth() + 1;
      var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
      item.expiry = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
    }

    var AddDuplicateRow = function (item) {
      var data = angular.copy(item);
      data.cop = '';
      data.copAmount = 0;
      data.isQuantityValid = false;
      data.batchNo = '';
      data.expiryDate = '';
      data.remark = '';
      data.uomUnit = '';
      data.mrp = '';
      data.quantity = '';
      data.taxAmount = 0;
      data.isBatchNoValid = false;
      data.isCopValid = false;
      data.isExpiryDateValid = false;
      data.isQuantityValid = false;
      data.isUomValid = false;

      delete data.id;
      delete data.selectedUom;
      delete data.uomType;
      delete data.uomTypeId;

      self.model.ItemDetailList.push(data);
    }

    var ConfirmRemove = function (item) {
      var popup = angular.element('#confirmRemove');
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
      // ValidateMaxOrderQuantity(item);
      var popup = angular.element('#confirmRemove');
      popup.modal('hide');
    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
    }

    var discountPercentCal = function (type, item, value, index) {
      if (type == 'percent') {
        item.discountPercentage = parseFloat(item.discountPercentage).toFixed(2);
        if (isNaN(item.discountPercentage) || !item.discountPercentage || item.discountPercentage == 0) {
          item.discountPercentage = 0;
          item.discountAmount = 0;
        } else {
          if (parseFloat(item.discountPercentage) > 100) {
            growl.error('Discount must be less than 100% of total amount');
          } else {
            item.discountAmount = (item.quantity * item.cop) * (item.discountPercentage / 100);
            item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
            item.lcopAmount = (((item.totalAmount + item.taxAmount) - item.discountAmount) / item.quantity);
          }
        }

      } else if (type == 'amount') {
        item.discountAmount = parseFloat(item.discountAmount).toFixed(2);
        if (isNaN(item.discountAmount) || !item.discountAmount || item.discountAmount == 0) {
          item.discountAmount = 0;
          item.discountPercentage = 0;
        } else {
          item.discountPercentage = ((parseFloat(item.discountAmount) / (item.quantity * item.cop)) * 100);
          item.discountPercentage = parseFloat(item.discountPercentage).toFixed(2);
          item.lcopAmount = (((item.totalAmount + item.taxAmount) - item.discountAmount) / item.quantity);
        }
      }
    }

    var ValidateCOP = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.cop))) {
        item.cop = '';
        item.isCopValid = true;
        isValid = false;
        growl.error("Please Enter COP");
      } else if (parseFloat(item.cop) == 0) {
        item.isCopValid = true;
        isValid = false;
        growl.error('COP cannot be zero');
      } else if (item.cop > 0) {
        item.cop = parseFloat(item.cop).toFixed(2);
      }

      return isValid;
    }

    var CalculateTotalAmount = function (item) {
      item.totalAmount = (item.quantity ? item.quantity : 0) * (item.cop ? item.cop : 0.00);
      item.taxAmount = (((item.totalAmount) - (item.discountAmount ? item.discountAmount : 0.00)) * item.taxPercent) / 100;
      if(item.discountAmount == undefined){
        item.discountAmount = 0;
      }
      item.lcopAmount = (((item.totalAmount + item.taxAmount) - item.discountAmount) / item.quantity);
    }

    var SaveConsignmentInwward = function () {
      if (validateSave()) {
        if (!self.model.inwardId) {
          SaveNewConsignmentInward();
        } else {
          UpdateConsignmentInward();
        }
      }
    }

    var SaveNewConsignmentInward = function () {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
      var store = self.model;
      var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.SAVE_API;

      var data = {
        "storeId": self.model.store.id,
        "vendorId": self.model.vendorId,
        "remark": self.model.remark,
        "statusId": 1,
        "totalNetAmount": self.model.totalNetAmount,
        "totalAmount": self.model.totalAmount,
        "totalDiscount": self.model.discountAmount,
        "totalTaxAmount": self.model.totalTaxAmount,
        "dcNo": self.model.dcNo,
        "dcDate": self.model.dcDate,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(store.ItemDetailList, function (item, key) {
        var storeItem = {
          "itemId": item.itemId,
          "uomTypeId": item.selectedUom ? item.selectedUom.uomTypeId : null,
          "uomUnitId": item.selectedUom ? item.selectedUom.uomUnitId : null,
          "batchNo": item.batchNo,
          "expiryDate": item.expiry,
          "quantity": item.quantity,
          "cop": item.cop,
          "totalAmount": item.totalAmount,
          "lcop": item.lcopAmount,
          "totalDiscount": item.discountAmount,
          "discountPercentage": item.discountPercentage,
          "totalNetAmount": item.netAmount,
          "totalTaxAmount": item.taxAmount,
          "taxId": item.taxId,
          "taxPercentage": item.taxPercent,
          "remark": item.remark,
          "uom": item.defaultUOM,
        }
        data.items.push(storeItem);

      });

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigteToConsignmentInward();

          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }

    var UpdateConsignmentInward = function () {

      var inward = self.model;

      var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.UPDATE_API + inward.inwardId;

      var data = {
        "remark": self.model.remark,
        "totalNetAmount": self.model.totalNetAmount,
        "totalAmount": self.model.totalAmount,
        "totalDiscount": self.model.discountAmount,
        "totalTaxAmount": self.model.totalTaxAmount,
        "dcNo": self.model.dcNo,
        "dcDate": self.model.dcDate,
        "items": [],
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId,
        "userId":$scope.userId
      };

      angular.forEach(inward.ItemDetailList, function (item, key) {
        var uom = [];

        item.defaultUOM.forEach(function (units) {
          var uomUnit = item.uom.find(function (uomUnit) {
            return uomUnit.uomTypeId == units.uomTypeId;
          });

          var unit = angular.copy(units);
          unit.id = uomUnit.id;
          uom.push(unit);
        });

        var storeItem = {
          "id": item.id,
          "itemId": item.itemId,
          "uomTypeId": item.selectedUom ? item.selectedUom.uomTypeId : null,
          "uomUnitId": item.selectedUom ? item.selectedUom.uomUnitId : null,
          "batchNo": item.batchNo,
          "expiryDate": item.expiry,
          "quantity": item.quantity,
          "cop": item.cop,
          "totalAmount": item.totalAmount,
          "lcop": item.lcopAmount,
          "totalDiscount": item.discountAmount,
          "discountPercentage": item.discountPercentage,
          "totalNetAmount": item.netAmount,
          "totalTaxAmount": item.taxAmount,
          "taxId": item.taxId,
          "taxPercentage": item.taxPercent,
          "uom": uom
        }

        if (!!item.id) {
          storeItem.id = item.id;
        }

        data.items.push(storeItem);

      });
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          $rootScope.stopLoader();
          NavigteToConsignmentInward();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }

    var NavigteToConsignmentInward = function () {
      $state.go('consignmentInward');
    }

    var validateSave = function () {
      var isValid = true;

      var ItemDetailList = self.model.ItemDetailList;

      if (!self.model.inwardId && !self.model.vendorId) {
        growl.error("Please enter vendor");
        isValid = false;
      }
      else if (ItemDetailList.length < 1) {
        growl.error("Please add atleast one item");
        isValid = false;
      }
      for (let ItemDetail of ItemDetailList) {

        if (!ValidateBatch(ItemDetail)) {
          isValid = false;
        }
        if (!ValidateExpiry(ItemDetail)) {
          isValid = false;
        }
        if (!ValidateQuantity(ItemDetail)) {
          isValid = false;
        }
        if (!ValidateCOP(ItemDetail)) {
          isValid = false;
        }
      }

      return isValid;
    }

    var ValidateUom = function (item) {
      var isValid = true;
      if (!item.selectedUom) {
        item.isUomValid = true;
        isValid = false;
        growl.error("Please Select UOM");
      }
      return isValid;
    }

    var ValidateBatch = function (item) {
      var isValid = true;
      if (!item.batchNo) {
        item.isBatchNoValid = true;
        isValid = false;
        growl.error("Please Enter Batch No.");
      }

      return isValid;
    }

    var ValidateExpiry = function (item) {
      var isValid = true;
      if (!item.expiryDate) {
        item.isExpiryDateValid = true;
        isValid = false;
        growl.error("Please Select Expiry Date");
      } else {
        var expYear = new Date().getFullYear();
        var expMonth = new Date().getMonth() + 1;
        var selectedYear = new Date(item.expiryDate).getFullYear();
        var selectedMonth = new Date(item.expiryDate).getMonth() + 1;

        if (selectedYear <= expYear && selectedMonth < expMonth) {
          growl.error('Expiry date should be later than current month and year');
          item.isExpiryDateValid = true;
          isValid = false;
        }
      }

      return isValid;
    }

    var ValidateQuantity = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.quantity))) {
        item.isQuantityValid = true;
        item.leaseQuantity = 0;
        isValid = false;
        growl.error("Please Enter Quantity");
      } else if (parseInt(item.quantity) == 0) {
        item.isQuantityValid = true;
        item.leaseQuantity = 0;
        isValid = false;
        growl.error('Quantity cannot be zero');
      } else {
        item.quantity = parseInt(item.quantity);
        isValid = true;
        // isValid = ValidateMaxOrderQuantity(item);
      }

      return isValid;
    }

    var GetInward = function (inwardId) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)

      var URI = CONSTANTS.PROCUREMENT.CONSIGNMENT.CONSIGNMENT_INWARD.DETAIL_API + $scope.unitId + '/' + inwardId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.inwardId = response.data.id;

          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
            //self.model.store.id = response.data.storeId;
          });
          self.model.vendorId = response.data.vendorId;
          self.model.vendorName = response.data.vendorName;
          self.model.dcDate = response.data.dcDate;
          self.model.dcNo = response.data.dcNo;
          self.model.itemDetails = [];
          angular.forEach(response.data.item, function (item, key) {
            item.expiry = new Date(item.expiryDate);
            item.cop = item.cop.toFixed(2);
            item.taxPercent = item.taxPercentage;
            item.discountAmount = item.totalDiscount;
            item.netAmount = item.totalNetAmount;
            item.taxAmount = item.totalTaxAmount;
            item.totalAmount = item.totalAmount;
            item.lcop = item.lcop;
            item.uom = item.uomList;
          });

          if (response.data.item)
            self.model.fillItemList(response.data.item);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }
    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewConsignmentInward", {
      url: "/addNewConsignmentInward",
      templateUrl: 'views/procurement/consignment/add-new-consignment-inward.html',
      controller: "AddNewConsignmentInward.Controller",
      controllerAs: "vm",
      title: 'Consignment Inward'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewConsignmentInward.Controller", addNewConsignmentInwardController);
})();
