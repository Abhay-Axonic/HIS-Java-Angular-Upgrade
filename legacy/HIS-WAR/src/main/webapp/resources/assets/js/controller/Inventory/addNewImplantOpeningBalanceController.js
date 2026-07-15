(function () {
  "use strict";

  function addNewImplantOpeningBalanceController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, $state, openingBalanceService, CurrencyService, productService, growl,stopTransactionService,$filter) {
    var self = this;
    var totalQuantity = 0

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
        requestData: [],
        openingBalanceData: [],
        selectAllRows: false,
        dateOptions: {
          formatYear: 'yyyy',
          showWeeks: false
        },
        expiryDateOptions: {
          minDate: new Date(),
          minMode: "month",
          maxMode: "year",
          datepickerMode: "month"
        },
        dateOpen: [],
        storeFlag: false
      };

      CurrencyService.GetCurrency().then(function (currency) {
        self.model.Currency = currency;
      })

      self.model.GetItemList = GetItemList;
      self.model.storeId = null;

      self.model.AddItemListData = AddItemListData;
      self.model.removeRowData = removeRowData;
      self.model.AddDuplicateRow = AddDuplicateRow;
      self.model.GetAssetCategoryList = GetAssetCategoryList;
      self.model.SaveNewItemRequest = SaveNewItemRequest;
      self.model.totalQuantity = 0;
      // self.model.AssetCategoryNameList = AssetCategoryNameList;
      // self.model.ProductCategoryNameList = ProductCategoryNameList;
      self.model.AddButtonClick = AddButtonClick;
      self.model.OpenExpiryDate = OpenExpiryDate;
      self.model.searchNewItem = searchNewItem;
      self.model.SelectItem = SelectItem;
      self.model.SelectAllItems = SelectAllItems;
      self.model.AddItems = AddItems;
      self.model.PopulateDropdown = PopulateDropdown;

      self.model.GetProductCategoryList = GetProductCategoryList;
     
      self.model.SelectTax=SelectTax;
      self.model.selectedItemIndex = 0;
      self.model.NavigteToOpeningBalance = NavigteToOpeningBalance;
      self.model.SaveOpeningBalance = SaveOpeningBalance;
      self.model.isItemChecked = false;
      self.model.ItemDetailList = [];
      self.model.searchNewItemList = [];
      self.model.validateSave = validateSave;
      self.model.setSelectedDate = setSelectedDate;
      self.model.ValidateConsumableAmount = ValidateConsumableAmount;
      self.model.ValidateQuantity = ValidateQuantity;
      self.model.onStoreChange = onStoreChange;
      self.model.confirmClear = confirmClear;
      self.model.itemRemovePopup = itemRemovePopup;
      self.model.CalculateCOP = CalculateCOP;
      self.model.CalculateTax=CalculateTax;
      self.model.SetOldValueStore = SetOldValueStore;
      self.model.ChangeFromStore = ChangeFromStore;
      self.model.ConfirmStorePopup = ConfirmStorePopup;

      self.model.ConfirmRemove = ConfirmRemove;
      self.model.RemoveItem = RemoveItem;
      self.model.ValidateCOP = ValidateCOP;
      self.model.ValidateBatch = ValidateBatch;
     
      self.model.ValidateExpiry = ValidateExpiry;
      self.model.ValidateMRP = ValidateMRP;
      // self.model.ValidateMaxOrderQuantity = ValidateMaxOrderQuantity;
      self.model.ValidateMarkup = ValidateMarkup;
      

      PopulateDropdown();
    }
    var CalculateCOP = function (item) {
      if (parseInt(item.quantity) > 0 && parseFloat(item.cop) > 0) {
        item.copAmount = (parseInt(item.quantity) * parseFloat(item.cop)).toFixed(2);
        item.lcop = (((item.cop * item.quantity) + ((item.copAmount * item.selectedTax.taxPercent)/100)) / item.quantity).toFixed(2);
      } else {
        item.copAmount = 0;
        item.lcop = 0;
      }
    }
    var CalculateTax = function (tax,item) {
      if (!!tax) {
        item.selectedTaxPercent = tax.taxPercent.toFixed(2);
        item.taxAmount=( (item.copAmount * tax.taxPercent )/ 100 ); 
      }
    }
    var OpenExpiryDate = function (idx) {
      self.model.dateOpen[idx] = true;
    }

    var PopulateDropdown = function () {
      var obp = self.model;

      var URI = CONSTANTS.GLOBAL.USER_STORE_API + "/"+$scope.organizationId + "/"+$scope.unitId+ "/"+$scope.userId ;
      obp.store = obp.stores[0];

      obp.stores.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          obp.stores = obp.stores.concat(response.data);
          $rootScope.stopLoader();
          var opbId = openingBalanceService.getOpeningBalanceId();
          $rootScope.stopLoader();
          if (!!opbId) {
            openingBalanceService.setOpeningBalanceId(null);
            GetOpeningBalance(opbId);
          } else {
            return false;
          }
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var GetAssetCategoryList = function () {
      var indents = self.model;

      var URI = CONSTANTS.GLOBAL.ASSET_CATEGORY_API + 1 + "/"+$scope.unitId + "/"+$scope.organizationId ;

      indents.assetCategory = indents.assetCategories[0];
      indents.productCategory = indents.productCategories[0];

      indents.assetCategories.splice(1);
      indents.productCategories.splice(1);

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          indents.assetCategories = indents.assetCategories.concat(response.data);
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
        searchNewItem();
    }



    var GetProductCategoryList = function (id) {
      var indents = self.model;

      //TODO : Add asset category to the uri
      var URI = CONSTANTS.GLOBAL.PRODUCT_CATEGORY_API + id + "/"+$scope.organizationId ;
      self.model.productCategory = self.model.productCategories[0];
      self.model.productCategories.splice(1);

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

    function searchNewItem() {
      if(self.model.itemName){
        if(self.model.itemName.length >= 3){
          var URI = CONSTANTS.INVENTORY.STOCK.IMPLANT_OPENING_BALANCE.SEARCH_ITEM;
          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId": $scope.unitId
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
      else if(self.model.itemCode){
        if(self.model.itemCode.length >= 3){
          var URI = CONSTANTS.INVENTORY.STOCK.IMPLANT_OPENING_BALANCE.SEARCH_ITEM;
          const data = {
            "assetTypeId": 1,
            "assetCategoryId": self.model.assetCategory.id ? self.model.assetCategory.id : undefined,
            "productCategoryId": self.model.productCategory.id ? self.model.productCategory.id : undefined,
            "storeId": self.model.store.id,
            "itemCode": self.model.itemCode,
            "itemName": self.model.itemName,
            "unitId": $scope.unitId
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

    var GetOpeningBalance = function (opbId) {
    	  var URI = CONSTANTS.INVENTORY.STOCK.IMPLANT_OPENING_BALANCE.DETAILS_API +"/" + opbId;

      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, {}).then(
        function (response) {
          self.model.taxList=response.data.taxList;
          self.model.opbId = response.data.id;
         
          self.model.store = self.model.stores.find(function (item) {
            return response.data.storeId == item.id;
            //self.model.store.id = response.data.storeId;
          });
          self.model.ItemDetailList= [];
          self.model.ItemDetailList =response.data.items;
          angular.forEach(self.model.ItemDetailList, function (item, key) {
            item.expiryDate = new Date(item.expiryDate);
            item.mrp = item.mrp.toFixed(2);
            item.cop = item.cop.toFixed(2);
            item.markup = item.markupPercentage.toFixed(2);
            item.selectedTax = item.taxList.find(function (data) {
              return item.taxId==data.taxId;
            });       
          });

          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
        });
    }

    var SaveOpeningBalance = function () {
      if (validateSave()) {
        if (!self.model.opbId) {
          SaveNewItemRequest();
        } else {
          UpdateOpeningBalance();
        }
      }
    }
    var UpdateOpeningBalance = function () {

      var opb = self.model;

      var URI = CONSTANTS.INVENTORY.STOCK.IMPLANT_OPENING_BALANCE.UPDATE_API + opb.opbId;

      var data = {
        "statusId":1,
        "organizationId": $scope.organizationId,
        "userId": $scope.userId,
        "unitId": $scope.unitId,
        "items": []
      };

      angular.forEach(opb.ItemDetailList, function (item, key) {
              
        var storeItem = {
          "itemId": item.itemId,
          "batchNo": item.batchNo,
          "isSterilized":item.isSterilized,
          "expiryDate": item.expiryDate,
          "quantity": item.quantity,
          "cop": item.cop,
          "mrp": item.mrp,
          "copAmount": item.copAmount,
          "lcop": item.lcop,
          "markupPercent": item.markup,
          "taxId": item.selectedTax.taxId,
          "taxPercent": item.selectedTax.taxPercent,
          "taxAmount": item.taxAmount,
          "remark": item.remark,
          "statusId": 1,
          "manufacturerId":item.manufacturerId
         
        }

        if (!!item.id) {
          storeItem.id = item.id;
        }

        data.items.push(storeItem);

      });
      GenericService.serviceAction("PATCH", URI, data).then(
        function (response) {
          NavigteToOpeningBalance();
          $rootScope.stopLoader();
          growl.success(response.data.message);
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }

    var AddButtonClick = function (index) {
      var data = self.model.openingBalanceData[index];
      self.model.selectAllRows = false;
    }

    function GetItemList() {
      self.model.action = 'Open';
      var popup = angular.element('#addNewItemWasteStore');
      popup.modal('show');
      self.model.searchNewItemList = [];
      self.model.pagination.data=[];
      GetAssetCategoryList();
      self.model.itemCode = "";
      self.model.itemName = "";
      self.model.selectAllRows = false;
      self.model.isItemChecked = false;
    }


    var AddItemListData = function () {
      self.model.selectAllRows = false;
      for (var i = 0; i < self.model.storeDataList.length; i++) {
        if (self.model.storeDataList[i]["isChecked"] == true) {
          self.model.openingBalanceData.push(self.model.storeDataList[i]);
        }
      }
    }

    var validateSave = function () {
      var isValid = true;

      var ItemDetailList = self.model.ItemDetailList;

      if (ItemDetailList.length < 1) {
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

        if (!ValidateMRP(ItemDetail)) {
          isValid = false;
        }

        if (!ValidateMarkup(ItemDetail)) {
          isValid = false;
        }
   
      }

      return isValid;
    }

    var ValidateConsumableAmount = function (item, index) {
      if (parseFloat(item.mrp) < parseFloat(item.cop)) {
        growl.error('MRP Should be Greater Than COP');
        item.isValidItem = false;
      } else if (parseInt(item.cop) == 0 && parseInt(item.mrp) == 0) {
        growl.error('MRP & COP cannot be zero');
        item.isValidItem = false;
      }
    }

    var ValidateBatch = function (item) {
      var isValid = true;
      if (item.isSterilized) {
        if (!item.batchNo) {
          item.isBatchNoValid = true;
          isValid = false;
          growl.error("Please Enter Batch No.");
        }
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

    // var ValidateMaxOrderQuantity = function (item) {
    //   var isValid = true;

    //   item.isQuantityValid = false;
    //   var totalLeaseQuantity = 0;

    //   CalculateLeaseQuantity(item);

    //   var items = self.model.ItemDetailList.filter(function (detail) {
    //     return detail.itemId == item.itemId;
    //   });

    //   items.forEach(function (detail) {
    //     totalLeaseQuantity = totalLeaseQuantity + detail.leaseQuantity;
    //   });

    //   if (totalLeaseQuantity > item.) {
    //     item.isQuantityValid = true;
    //     isValid = false;
    //     growl.error('Entered quantity is exceeding the max order quantity');
    //   }

    //   return isValid;
    // }

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

      if (parseFloat(item.cop) > 0 && parseFloat(item.mrp) > 0) {
        if (parseFloat(item.cop) > parseFloat(item.mrp)) {
          item.isCopValid = true;
          isValid = false;
          growl.error('COP cannot be greater than MRP');
        } else {
          item.isCopValid = false;
          item.isMrpValid = false;
        }
      }

      return isValid;
    }

    var ValidateMRP = function (item) {
      var isValid = true;

      if (isNaN(parseFloat(item.mrp))) {
        item.mrp = '';
        item.isMrpValid = true;
        isValid = false;
        growl.error("Please Enter MRP");
      } else if (parseFloat(item.mrp) == 0) {
        item.isMrpValid = true;
        isValid = false;
        growl.error('MRP cannot be zero');
      } else if (parseFloat(item.mrp) > 0) {
        item.mrp = parseFloat(item.mrp).toFixed(2);
      }

      if (parseFloat(item.cop) > 0 && parseFloat(item.mrp) > 0) {
        if (parseFloat(item.cop) > parseFloat(item.mrp)) {
          item.isMrpValid = true;
          isValid = false;
          growl.error('MRP cannot be less than COP');
        } else {
          item.isCopValid = false;
          item.isMrpValid = false;
        }
      }

      return isValid;
    }

    var ValidateMarkup = function (item) {
      var isValid = true;
      if (isNaN(parseFloat(item.markup))) {
        item.markup = '0.00';
      } else if (parseFloat(item.markup) == 0) {
        item.markup = parseFloat(item.markup).toFixed(2);
      } else if (parseFloat(item.markup) > 0 && parseFloat(item.markup) < 100) {
        item.markup = parseFloat(item.markup).toFixed(2);
      } else if (parseFloat(item.markup) >= 100) {
        item.markup = parseFloat(item.markup).toFixed(2);
        isValid = false;
        item.isMarkupValid = true;
        growl.error('Markup cannot be greater than 99.99%');
      }

      return isValid;
    }

    var AddDuplicateRow = function (item) {
      var data = angular.copy(item);
      data.cop = '';
      data.copAmount = 0;
      data.lcop = 0;
      data.isQuantityValid = false;
      data.batchNo = '';
      data.expiryDate = '';
      data.remark = '';
      data.mrp = '';
      data.quantity = '';
      data.taxAmount = 0;
      data.markup = '';
      data.leaseQuantity = 0;
      data.isBatchNoValid = false;
      data.isCopValid = false;
      data.isExpiryDateValid = false;
      data.isMrpValid = false;
      data.isQuantityValid = false;

      delete data.id;
       self.model.ItemDetailList.push(data);
    }

    var AddItems = function () {
    	
    	 ActualAddItems();
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
                  self.model.SelectedItemsIds = [];
                  self.model.items = [];
                  $rootScope.stopLoader();                     
                  return false;
          }
         else{
        	 ActualAddItems();
         }
	  });
    */}
    
    var ActualAddItems = function () {
      // new test api
      var URI = CONSTANTS.GLOBAL.ITEM_DETAILS_API_NEW;
      var data = {
        "itemIds": self.model.SelectedItemsIds,
        "storeId": self.model.store.id,
        "unitId":$scope.unitId,
        "organizationId":$scope.organizationId
      };

      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          angular.forEach(response.data, function (item){
            item.selectedTax = item.taxList.find(function (val) {
              return item.taxId == val.taxId;
            });
            if(!item.selectedTax) {
              item.taxId = 11;
              item.selectedTax = item.taxList.find(function (val) {
                return item.taxId == val.taxId;
              });
            }
          })
          self.model.ItemDetailList = self.model.ItemDetailList.concat(response.data);
          self.model.SelectedItemsIds = [];
          self.model.items = [];
          $rootScope.stopLoader();
        },
        function (err) {
          $rootScope.stopLoader();
          growl.error(err.message);
        });
    }

    // Against Indent Item Selection
    var isSelectedItem = function (item) {
      return item.isSelectedItem;

    } // Against Indent Item Selection
    var isSelected = function (item) {
      return item.isSelected;
    }

    var SaveNewItemRequest = function () {
      var store = self.model;
      var URI = CONSTANTS.INVENTORY.STOCK.IMPLANT_OPENING_BALANCE.SAVE_API;

      var data = {
        "storeId": store.store.id,
        "statusId": 1,
        "items": [],
        "organizationId":$scope.organizationId,
        "unitId": $scope.unitId,
        "userId": $scope.userId
      };

      angular.forEach(store.ItemDetailList, function (item, key) {
        var storeItem = {
          "itemId": item.itemId,
          "batchNo": item.batchNo,
          "isSterilized":item.isSterilized,
          "expiryDate": item.expiry,
          "quantity": item.quantity,
          "cop": item.cop,
          "mrp": item.mrp,
          "copAmount": item.copAmount,
          "lcop": item.lcop,
          "markupPercent": item.markup,
          "taxId": item.selectedTax.taxId,
          "taxPercent": item.selectedTax.taxPercent,
          "taxAmount": item.taxAmount,
          "remark": item.remark,
          "statusId": 1,
          "manufacturerId":item.manufacturerId
        }
        data.items.push(storeItem);

      });
      $rootScope.startLoader();
      GenericService.serviceAction("POST", URI, data).then(
        function (response) {
          // ClearRequests();
          $rootScope.stopLoader();
          NavigteToOpeningBalance();

          growl.success(response.data.message);
        },



        function (err) {
          $rootScope.stopLoader();
          growl.error('Something Went Wrong');
        });
    }

    var SelectTax = function(tax, item) {
      if (!!tax) {
        item.selectedTaxPercent = tax.taxPercent.toFixed(2);
        item.taxAmount=( (item.copAmount * tax.taxPercent )/ 100 ); 
        item.lcop = ((item.copAmount + item.taxAmount) / item.quantity);
      }
    }

    var NavigteToOpeningBalance = function () {
      $state.go('implantOpeningBalance');
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
          self.model.SelectedItemsIds = self.model.searchNewItemList.map(function (item) {
            item.isSelected = true;
            self.model.isItemChecked = true;
            return item.itemId;
          });
        }
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

    var setSelectedDate = function (item) {
      var expDateYear = item.expiryDate.getFullYear();
      var expDateMonth = item.expiryDate.getMonth() + 1;
      var expDateDay = new Date(expDateYear, expDateMonth, 0).getDate();
      item.expiry = (expDateYear + "-" + expDateMonth + "-" + expDateDay);
    }

    var onStoreChange = function () {
      if (!!self.model.ItemDetailList.length) {
        $("#storeChange").modal()
      }
    }

    var confirmClear = function () {
      self.model.ItemDetailList = [];
    }

    var itemRemovePopup = function (item) {
      self.model.itemRemove = '';
      self.model.itemRemove = item;
      $("#itemRemovePopup").modal()

    }

    function removeRowData() {
      var index = self.model.ItemDetailList.indexOf(self.model.itemRemove);
      self.model.ItemDetailList.splice(index, 1);
    }

    var SetOldValueStore = function (store) {
      self.model.oldStore = store;
      self.model.store = self.model.oldStore;
    }

    var ChangeFromStore = function (store) {
      if (!!self.model.ItemDetailList.length) {
        self.model.store = self.model.oldStore;
        self.model.storeNew = JSON.parse(JSON.stringify(store));
        $("#storeChange").modal()
      }
    }

    var ConfirmStorePopup = function () {
      self.model.store = self.model.storeNew;
      self.model.ItemDetailList = [];
      ModalDismiss('storeChange');

    }

    var ModalDismiss = function (popupName) {
      var popup = angular.element('#' + popupName);
      popup.modal('hide');
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

    initializeController();
  }

  function config($stateProvider) {
    $stateProvider.state("addNewImplantOpeningBalance", {
      url: "/addNewImplantOpeningBalance",
      templateUrl: 'views/PharmacyInventory/Store/add-new-implant-opening-balance.html',
      controller: "AddNewImplantOpeningBalance.Controller",
      controllerAs: "vm",
      title: 'Implant Opening Balance'
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AddNewImplantOpeningBalance.Controller", addNewImplantOpeningBalanceController);
})();